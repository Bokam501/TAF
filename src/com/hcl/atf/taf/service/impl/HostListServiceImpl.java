package com.hcl.atf.taf.service.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hcl.atf.taf.constants.HostStatus;
import com.hcl.atf.taf.constants.IDPAConstants;
import com.hcl.atf.taf.constants.TestExecutionStatus;
import com.hcl.atf.taf.controller.utilities.CommonUtility;
import com.hcl.atf.taf.controller.utilities.DateUtility;
import com.hcl.atf.taf.dao.DeviceListDAO;
import com.hcl.atf.taf.dao.HostHeartbeatDAO;
import com.hcl.atf.taf.dao.HostListDAO;
import com.hcl.atf.taf.dao.HostPlatformMasterDAO;
import com.hcl.atf.taf.dao.TestRunListDAO;
import com.hcl.atf.taf.dao.WorkPackageDAO;
import com.hcl.atf.taf.model.CommonActiveStatusMaster;
import com.hcl.atf.taf.model.DeviceList;
import com.hcl.atf.taf.model.HostHeartbeat;
import com.hcl.atf.taf.model.HostList;
import com.hcl.atf.taf.model.HostPlatformMaster;
import com.hcl.atf.taf.model.RunConfiguration;
import com.hcl.atf.taf.model.TestCaseList;
import com.hcl.atf.taf.model.TestRunJob;
import com.hcl.atf.taf.model.TestRunList;
import com.hcl.atf.taf.model.UserList;
import com.hcl.atf.taf.model.WorkFlow;
import com.hcl.atf.taf.model.WorkFlowEvent;
import com.hcl.atf.taf.model.WorkPackage;
import com.hcl.atf.taf.model.json.JsonHostList;
import com.hcl.atf.taf.model.json.terminal.JsonTestRunList;
import com.hcl.atf.taf.mongodb.service.MongoDBService;
import com.hcl.atf.taf.service.EnvironmentService;
import com.hcl.atf.taf.service.EventsService;
import com.hcl.atf.taf.service.HostListService;
import com.hcl.atf.taf.service.UserListService;
import com.hcl.atf.taf.service.WorkPackageService;

@Service
public class HostListServiceImpl implements HostListService {
	private static final Log log = LogFactory.getLog(HostListServiceImpl.class);
	
	@Autowired
    private HostListDAO hostListDAO;
	@Autowired
	private HostPlatformMasterDAO hostPlatformMasterDAO;
	
	@Autowired
    private DeviceListDAO deviceListDAO;
	@Autowired
    private TestRunListDAO testRunListDAO;
	@Autowired
	private HostHeartbeatDAO hostHeartbeatDAO;
	@Autowired
	private EventsService eventsService;
	@Autowired
	private WorkPackageDAO workPackageDAO;
	@Autowired
	private EnvironmentService environmentService;
	@Autowired
	private MongoDBService mongoDBService;
	@Autowired
	private UserListService userListService;
	@Autowired
	private WorkPackageService workPackageService;
	
	@Value("#{ilcmProps['TIMER_INTERVAL_IN_MILLISEC']}")
	private Integer timerInterval;
	
	@Value("#{ilcmProps['WPREPORT_FOLDER_TIMER_INTERVAL_IN_MINS']}")
	private Integer deleteTimerInterval;
	
	@Autowired
	private HttpServletRequest request;
	
	@Override
	@Transactional
	public void add(HostList hostList) {
		hostListDAO.add(hostList);
	}

	    
	
	
	@Override
	@Transactional
	public List<HostList> list() {	 
		return hostListDAO.list();
	}

	@Override
	@Transactional
	public void delete(int hostId) {
		hostListDAO.delete(hostListDAO.getByHostId(hostId));			
	}

	@Override
	@Transactional
	public void update(HostList hostList) {
		hostListDAO.update(hostList);	
		
	}




	@Override
	@Transactional
	public List<HostList> list(int startIndex, int pageSize) {
		
		return hostListDAO.list(startIndex,pageSize);
	}




	@Override
	@Transactional
	public List<HostPlatformMaster> platformsList() {
		return hostPlatformMasterDAO.list();
	}




	@Override
	@Transactional
	public int getTotalRecords() {
		
		return hostListDAO.getTotalRecords();
	}


	

	@Override
	@Transactional
	public List<HostList> listByHostIp(String hostIP) {
		
		return hostListDAO.listByHostIP(hostIP);
	}

	@Override
	@Transactional
	public List<HostList> listByHostName(String hostName) {
		
		return hostListDAO.listByHostName(hostName);
	}
	

	
	@Override
	@Transactional
	public void CheckIfHostActive() {		
	    long curTime = System.currentTimeMillis();	 
	    	List<HostHeartbeat> list =hostHeartbeatDAO.listExpiredHost(curTime-(20*timerInterval));
	    	if(list!=null && list.size() >0) {
	    		for (HostHeartbeat host : list ) {
	    	
				//if no pulse receive within time , unregister the host
					HostList hostList = hostListDAO.getByHostId(host.getHostId());
					if(hostList != null) {
			    		log.info("Expired Host is  : " + hostList.getHostName()+" -- "+hostList.getHostId());		    		
						hostList.setCommonActiveStatusMaster(new CommonActiveStatusMaster(HostStatus.INACTIVE.toString()));
						hostListDAO.update(hostList);
						eventsService.raiseTerminalDisconnectedEvent(hostList, "Terminal Disconnected");
						//reset the devices connect to the host to INACTIVE status 
						deviceListDAO.resetDevicesStatus(host.getHostId());
						Set<DeviceList> devices = hostList.getDeviceLists();
						if (devices != null && devices.size() >0) {
							for (DeviceList device : devices) {
								eventsService.raiseDeviceDisconnectedEvent(device, "Device disconnected due to terminal disconnection");
							}
						}
						//fail the test run if any if executed by the disconnected host			
						
						CheckIfHostActive(host.getHostId());
						HostHeartbeat heartBeat = hostHeartbeatDAO.getByHostId(host.getHostId());
						if(heartBeat != null)
							hostHeartbeatDAO.delete(heartBeat);
						log.info(hostList.getHostName() +" is disconnected");
					}
	    		}
	    		list=null;	
	    	}
	}
		
	@Override
	@PostConstruct
	@Transactional
	public void resetHostsStatus(){
		//reset hosts status when the server is launched for the first time or when restarted
		hostListDAO.resetHostsStatus();	
		new Timer().scheduleAtFixedRate(new TimerTask() {		
			@Override
			public void run() {				
				CheckIfHostActive();			
			}
		}, timerInterval+timerInterval, timerInterval+timerInterval);
	}

	@Override
	@Transactional
	public HostList getHostById(int hostId) {
		return hostListDAO.getByHostId(hostId);
	}

	@Override
	@Transactional
	public boolean isHostExistingByName(String hostName) {
		return hostListDAO.isHostExistingByName(hostName);
	}

	@Override
	@Transactional
	public void resetHostsStatus(int hostId) {
		hostListDAO.resetHostsStatus(hostId);
	}
	
	@Override
	@Transactional
	public List<JsonHostList> getUnMappedHostListOfProductfromRunConfigurationWorkPackageLevel(int productId, int ecId, int runConfigStatus, int workpackageId) {
		return hostListDAO.getUnMappedHostListOfProductfromRunConfigurationWorkPackageLevel(productId, ecId, runConfigStatus, workpackageId);
	}

	@Override
	@Transactional
	public List<JsonHostList> getUnMappedHostListOfProductfromRunConfigurationTestRunPlanLevel(int productId, int ecId, int runConfigStatus, int testRunPlanId) {
		return hostListDAO.getUnMappedHostListOfProductfromRunConfigurationTestRunPlanLevel(productId, ecId, runConfigStatus, testRunPlanId);
	}
	
	@Override
	@Transactional
	public RunConfiguration isRunConfigurationOfHostOfWorkPackageExisting(Integer environmentCombinationId, Integer workpackageId,Integer hostId) {		
		return hostListDAO.isRunConfigurationOfHostOfWorkPackageExisting(environmentCombinationId, workpackageId, hostId);
	}

	@Override
	@Transactional
	public RunConfiguration isRunConfigurationOfHostOfTestRunPlanExisting(Integer environmentCombinationId, Integer testRunPlanId,Integer hostId) {
		return hostListDAO.isRunConfigurationOfHostOfTestRunPlanExisting(environmentCombinationId, testRunPlanId, hostId);
	}
	
	@Override
	@Transactional
	public void isHostActive(int hostId) {
		final int hostNumber = hostId;
		new Timer().scheduleAtFixedRate(new TimerTask() {		
			@Override
			public void run() {						
				CheckIfHostActive(hostNumber);			
			}
		}, timerInterval+timerInterval, timerInterval+timerInterval);		
	}
	
	@Override
	@Transactional
	public void CheckIfHostActive(int hostId) {
		if(hostId > 0) {
			try{
				HostList hostList = hostListDAO.getByHostId(hostId);			
				if(hostList.getCommonActiveStatusMaster().getStatus().equalsIgnoreCase("INACTIVE")){				
					List<TestRunList> runList =  testRunListDAO.listByHostId(hostId , TestExecutionStatus.EXECUTING.toString());
					
					for(TestRunList testRunList:runList){
						testRunList.getTestResultStatusMaster().setTestResultStatus(TestExecutionStatus.FAILED.toString());
						testRunList.setTestRunEndTime(new Date(System.currentTimeMillis()));
						testRunList.setTestRunFailureMessage("Connection to terminal lost!");
						testRunListDAO.update(testRunList);
					}
					
					List<WorkPackage> wpSetForAHost = workPackageDAO.getWorkpackageSetForAHost(hostId);
					List<TestRunJob> testRunJobSetForAHost = new ArrayList<TestRunJob>(0);
					for(WorkPackage workPackage : wpSetForAHost){
						testRunJobSetForAHost.addAll(workPackageDAO.incompleteTestRunJobSetForAWorkpackage(workPackage.getWorkPackageId()));
						if(testRunJobSetForAHost.size() > 0){
							int selectiveTestCaseFlag = 0;
							for(TestRunJob  trj : testRunJobSetForAHost){							
								List<TestCaseList> testCaseLists=workPackageService.getSelectedTestCasesFromTestRunJob(trj.getTestRunJobId());
								if(testCaseLists!=null && !testCaseLists.isEmpty())
									selectiveTestCaseFlag=1;
								JsonTestRunList jsonTestRunList = new JsonTestRunList(trj, selectiveTestCaseFlag);
								trj.setTestRunStatus(IDPAConstants.JOB_ABORTED);
								trj.setTestRunFailureMessage("Job did not complete because the host was closed unexpectedly.");
								trj.setTestRunEndTime(DateUtility.toDateInSec(jsonTestRunList.getTestRunEndTime()));
								trj.setTestRunStartTime(DateUtility.toDateInSec(jsonTestRunList.getTestRunStartTime()));						
								log.info("Test Run Job is getting updated " +trj.getTestRunJobId() +" and status : "+trj.getTestRunStatus());
								environmentService.updateTestRunJob(trj);
								if (trj != null) {
									if (trj.getTestRunJobId() != null){
										log.info("Updating Testrun to Mongo ID "+trj.getTestRunJobId());
										mongoDBService.addTestRunJobToMongoDB(trj.getTestRunJobId());
									}
								}							
								workPackage =trj.getWorkPackage();
								WorkFlow workFlow=workPackageService.getWorkFlowByEntityIdStageId(IDPAConstants.WORKPACKAGE_ENTITY_ID, IDPAConstants.WORKFLOW_STAGE_ID_COMPLETED);
								WorkFlowEvent workFlowEvent = new WorkFlowEvent();
								workFlowEvent.setEventDate(DateUtility.getCurrentTime());
								workFlowEvent.setRemarks("Workpackage :"+workPackage.getName()+" Completed");
								UserList user = workPackage.getUserList();
								if(user==null){
									user = userListService.getUserByLoginId(IDPAConstants.ADMIN_LOGIN_ID);
								}
								workFlowEvent.setUser(user);
								workFlowEvent.setWorkFlow(workFlow);
								workPackage.setWorkFlowEvent(workFlowEvent);
								workPackage.setActualEndDate(new Date());
								workPackageService.addWorkFlowEvent(workFlowEvent);
								workPackageService.updateWorkPackage(workPackage);
							}						
						}
					}
				}
			} catch(Exception e){
				log.error("Error in updating inactive host workpackage status ", e);
			}							
		}	
	}
	
	@Override
	@Transactional
	public List<HostList> listHostIdByStatus(String status) {
		return hostListDAO.listHostIdByStatus(status);
	}
	
	@Override
	@PostConstruct
	@Transactional
	public void deleteWPReportFolder(){
		//delete folder once every deleteTimer interval minutes to space up the memory
		new Timer().scheduleAtFixedRate(new TimerTask() {		
			@Override
			public void run() {				
				try {
					//log.info("Trying to delete the folder location at current time : " + new Date());
					String deleteFolderLocation = CommonUtility.getCatalinaPath()+ 
							File.separator+ "webapps" + File.separator + request.getContextPath() + File.separator + "WPReport";
					File folder = new File(deleteFolderLocation);
					if (folder.exists()) {
						File[] listFiles = folder.listFiles();
						deleteFilesInFolder(listFiles);
					} else {
						log.info("No such folder "+deleteFolderLocation+" exists to delete.");
					}
				} catch (Exception e) {
					//log.error("Error in deleting folder due to " +e);
				}			
			}
		}, deleteTimerInterval, deleteTimerInterval);
	}
	
	private void deleteFilesInFolder(File[] files) {
		try{
			for (File listFile : files) {
				if(listFile.isDirectory()){
					deleteFilesInFolder(listFile.listFiles());
				} else {
					System.out.println("File name : "+ listFile.getName() + " & Diff : " + (TimeUnit.MILLISECONDS.toMinutes(System.currentTimeMillis()) - TimeUnit.MILLISECONDS.toMinutes(listFile
									.lastModified())));
					if ((TimeUnit.MILLISECONDS.toMinutes(System.currentTimeMillis()) - TimeUnit.MILLISECONDS.toMinutes(listFile.lastModified())) > deleteTimerInterval) {
						if(!listFile.delete()){
							System.out.println("Unable to delete the file");
						}
					}
				}
			}
		} catch(Exception e){
			//log.error("Error in deleting files and folders due to " +e);
		}
	}
}
