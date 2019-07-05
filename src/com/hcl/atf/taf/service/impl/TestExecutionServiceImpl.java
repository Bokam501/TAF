package com.hcl.atf.taf.service.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hcl.atf.taf.constants.ClientReponseMessage;
import com.hcl.atf.taf.constants.DeviceStatus;
import com.hcl.atf.taf.constants.TAFConstants;
import com.hcl.atf.taf.constants.TestExecutionStatus;
import com.hcl.atf.taf.controller.TestRunExecutionStatusVO;
import com.hcl.atf.taf.controller.utilities.CommonUtility;
import com.hcl.atf.taf.dao.DeviceListDAO;
import com.hcl.atf.taf.dao.HostHeartbeatDAO;
import com.hcl.atf.taf.dao.TestCaseListDAO;
import com.hcl.atf.taf.dao.TestEnvironmentDAO;
import com.hcl.atf.taf.dao.TestExecutionResultDAO;
import com.hcl.atf.taf.dao.TestRunConfigurationChildDAO;
import com.hcl.atf.taf.dao.TestRunConfigurationParentDAO;
import com.hcl.atf.taf.dao.TestRunListDAO;
import com.hcl.atf.taf.dao.TestRunSelectedDeviceListDAO;
import com.hcl.atf.taf.dao.TrccExecutionPlansCommonDAO;
import com.hcl.atf.taf.integration.testManagementSystem.TAFTestManagementSystemIntegrator;
import com.hcl.atf.taf.model.DeviceList;
import com.hcl.atf.taf.model.HostHeartbeat;
import com.hcl.atf.taf.model.TestCaseList;
import com.hcl.atf.taf.model.TestEnvironmentDevices;
import com.hcl.atf.taf.model.TestExecutionResult;
import com.hcl.atf.taf.model.TestExecutionResultsExportData;
import com.hcl.atf.taf.model.TestResultStatusMaster;
import com.hcl.atf.taf.model.TestRunConfigurationChild;
import com.hcl.atf.taf.model.TestRunConfigurationParent;
import com.hcl.atf.taf.model.TestRunList;
import com.hcl.atf.taf.model.TestRunPlan;
import com.hcl.atf.taf.model.TestRunSelectedDeviceList;
import com.hcl.atf.taf.model.TestSuiteList;
import com.hcl.atf.taf.model.TrccExecutionPlan;
import com.hcl.atf.taf.model.TrccExecutionPlanDetails;
import com.hcl.atf.taf.model.custom.TestRunListCustom;
import com.hcl.atf.taf.model.json.JsonTestExecutionResult;
import com.hcl.atf.taf.service.EmailService;
import com.hcl.atf.taf.service.EventsService;
import com.hcl.atf.taf.service.TestCaseScriptGenerationService;
import com.hcl.atf.taf.service.TestExecutionService;
import com.hcl.atf.taf.service.TestReportService;
@Service
public class TestExecutionServiceImpl implements TestExecutionService {	 
	private static final Log log = LogFactory.getLog(TestExecutionServiceImpl.class);

	@Autowired
	private TestExecutionResultDAO testExecutionResultDAO;
	@Autowired
	private TestRunListDAO testRunListDAO;
	@Autowired
	private TestRunSelectedDeviceListDAO testRunSelectedDeviceListDAO;
	@Autowired
	private TestRunConfigurationChildDAO testRunConfigurationChildDAO;
	@Autowired
	private TestRunConfigurationParentDAO testRunConfigurationParentDAO;
	@Autowired
	private DeviceListDAO deviceListDAO;
	@Autowired
	private HostHeartbeatDAO hostHeartbeatDAO;
	@Autowired
	private TestReportService testReportService;
	@Autowired
	private EmailService eMailService;
	@Autowired
	private TestEnvironmentDAO testEnvironmentDAO;
	@Autowired
	private EventsService eventsService;
	@Autowired
	private TAFTestManagementSystemIntegrator tafTestManagementIntegrator;	
	@Autowired
	private TestCaseListDAO testCaseListDAO;
	@Autowired
	TrccExecutionPlansCommonDAO trccExecutionPlansCommonDAO;
	@Autowired
	private TestCaseScriptGenerationService testCaseScriptGenerationService;
	@Value("#{ilcmProps['GENERATED_TEST_SCRIPTS_DESTINATION_FOLDER']}")
    private String testScriptsDestinationDirectory;
	@Value("#{ilcmProps['APP_PATH']}")
    private String iLCMAppPath;

		
	@Override
	@Transactional
	public void addTestRunList(TestRunList testRunList) {
		testRunListDAO.add(testRunList);
	}

	@Override
	@Transactional
	public void deleteTestRunList(TestRunList testRunList) {
		testRunListDAO.delete(testRunList);
		
	}

	@Override
	@Transactional
	public TestRunList getByTestRunListId(int testRunListId) {
		
		return testRunListDAO.getByTestRunListId(testRunListId);
	}

	@Override
	@Transactional
	public int getTotalRecordsOfTestRunList() {
		
		return testRunListDAO.getTotalRecords();
	}

	@Override
	@Transactional
	public int getTotalRecordsOfTestRunList(int testRunConfigurationChildId) {
		
		return testRunListDAO.getTotalRecords(testRunConfigurationChildId);
	}

	@Override
	@Transactional
	public List<TestRunList> listTestRunList(int testRunConfigurationChildId) {
		
		return testRunListDAO.list(testRunConfigurationChildId);
	}

	@Override
	@Transactional
	public List<TestRunList> listTestRunList(int testRunConfigurationChildId,
			int startIndex, int pageSize) {
		
		return testRunListDAO.list(testRunConfigurationChildId, startIndex, pageSize);
	}

	@Override
	@Transactional
	public List<TestRunList> listAllTestRunList() {
		
		return testRunListDAO.listAll();
	}

	@Override
	@Transactional
	public List<TestRunList> listAllTestRunList(int startIndex, int pageSize) {
		
		return testRunListDAO.listAll(startIndex, pageSize);
	}

	@Override
	@Transactional
	public void updateTestRunList(TestRunList testRunList) {
		testRunListDAO.update(testRunList);
		
		//Check if the TestRun has completed all the TestRunList jobs.
		//Send a mail notification only after all the jobs in the TestRun are complete
		boolean testRunCompleted = testRunListDAO.hasTestRunCompleted(testRunList);
		if (!testRunCompleted) {
			log.info("TestRun has not completed yet. Waiting till TestRun is completed before sending mail");
			return;
		}
		log.info("TestRun has been completed. Initiating report dispatch by EMail.");
		//Send a mail notification since all the jobs in the TestRun are complete
		eMailService.sendTestRunCompletionMail(testRunList);
	}
	
	
	
	@Override
	@Transactional
	public void updateTestRunListEvidenceStatus(TestRunList testRunList) {
		testRunListDAO.update(testRunList);
	}

	@Override
	@Transactional
	public TestExecutionResult addTestExecutionResult(TestExecutionResult testExecutionResult) {
		
		return testExecutionResultDAO.add(testExecutionResult);
	}
	
	@Override
	@Transactional
	public void deleteTestExecutionResult(TestExecutionResult testExecutionResult) {
		testExecutionResultDAO.delete(testExecutionResult);
	}
	
	@Override
	@Transactional
	public TestExecutionResult processTestExecutionResult(TestExecutionResult testExecutionResult, JsonTestExecutionResult jsonTestExecutionResult) {
		
		return testExecutionResultDAO.processTestExecutionResult(testExecutionResult, jsonTestExecutionResult);
	}

	@Override
	@Transactional
	public TestExecutionResult getByTestExecutionResultId(
			int testExecutionResultId) {
		
		return testExecutionResultDAO.getByTestExecutionResultId(testExecutionResultId);
	}

	@Override
	@Transactional
	public int getTotalRecordsOfTestExecutionResult() {
		
		return testExecutionResultDAO.getTotalRecords();
	}

	@Override
	@Transactional
	public int getTotalRecordsOfTestExecutionResult(int testRunListId) {
		
		return testExecutionResultDAO.getTotalRecords(testRunListId);
	}

	@Override
	@Transactional
	public int getTotalRecordsOfTestExecutionResult(int testRunListId, int testSuiteId) {
		
		return testExecutionResultDAO.getTotalRecords(testRunListId, testSuiteId);
	}

	@Override
	@Transactional
	public List<TestExecutionResult> listTestExecutionResult(int testRunListId) {
		
		return testExecutionResultDAO.list(testRunListId);
	}

	@Override
	@Transactional
	public List<TestExecutionResult> listTestExecutionResult(int testRunListId, int testSuiteId) {
		
		return testExecutionResultDAO.list(testRunListId, testSuiteId);
	}

	@Override
	@Transactional
	public List<TestExecutionResult> listTestExecutionResult(int testRunListId, int startIndex,
			int pageSize) {
		
		return testExecutionResultDAO.list(testRunListId, startIndex, pageSize);
	}

	@Override
	@Transactional
	public List<TestExecutionResult> listTestExecutionResult(int testRunListId, int testSuiteId,
			int startIndex, int pageSize) {
		
		return testExecutionResultDAO.list(testRunListId, testSuiteId, startIndex, pageSize);
	}

	@Override
	@Transactional
	public List<TestExecutionResult> listAllTestExecutionResult() {
		
		return testExecutionResultDAO.listAll();
	}

	@Override
	@Transactional
	public List<TestExecutionResult> listAllTestExecutionResult(int startIndex, int pageSize) {
		
		return testExecutionResultDAO.listAll(startIndex, pageSize);
	}

	@Override
	@Transactional
	public void updateTestExecutionResult(TestExecutionResult testExecutionResult) {
		testExecutionResultDAO.update(testExecutionResult);
		
	}
	
	@Override
	@Transactional
	public List<TestRunList> listByHostId(int hostId, String testStatus) {
		
		return testRunListDAO.listByHostId(hostId, testStatus);
	}
	
	@Override
	@Transactional
	public Integer getAverageTestRunExecutionTime(TestRunList testRunList) {
		
		return testRunListDAO.getAverageTestRunExecutionTime(testRunList);
	}
	
	@Override
	@Transactional
	public List<DeviceList> getTotalDevicesInTestRunConfigurationChild(int testRunConfigurationChildId) {
		TestRunConfigurationChild testRunConfigurationChild=testRunConfigurationChildDAO.getByTestRunConfigurationChildId(testRunConfigurationChildId);
		if (testRunConfigurationChild == null)
			return null;
		return getTotalDevicesInTestRunConfigurationChild(testRunConfigurationChild);
	}
	
	@Override
	@Transactional
	public List<DeviceList> getTotalDevicesInTestRunConfigurationChild(TestRunConfigurationChild testRunConfigurationChild) {
	
		List<DeviceList> targetDevices = new ArrayList<DeviceList> ();
		try {

			Set<TestEnvironmentDevices> testEnvironmentDevicesList = null;
			testEnvironmentDevicesList=testRunConfigurationChild.getTestEnvironmentDeviceses();
			//Process the devices in the test environments first
			if (testEnvironmentDevicesList == null || testEnvironmentDevicesList.isEmpty()) {
			} else {
				for (TestEnvironmentDevices ted: testEnvironmentDevicesList) {
					Set<DeviceList> devicesList = ted.getDeviceList();
					if (devicesList == null || devicesList.isEmpty()) {
					} else {
						for (DeviceList device : devicesList) {
							
							if (device != null) {
								Hibernate.initialize(device.getDeviceListId());
								Hibernate.initialize(device.getDeviceModelMaster());
								if (device.getDeviceModelMaster() != null)
									Hibernate.initialize(device.getDeviceModelMaster().getDeviceMakeMaster());
								
								Hibernate.initialize(device.getCommonActiveStatusMaster());
		
								Hibernate.initialize(device.getDevicePlatformVersionListMaster());
								if (device.getDevicePlatformVersionListMaster() != null)
									Hibernate.initialize(device.getDevicePlatformVersionListMaster().getDevicePlatformMaster());
								Hibernate.initialize(device.getHostList());
							}

							if (targetDevices.contains(device)) {
								continue;
							} else {
								targetDevices.add(device);
							}
						}
					}
				}
			}
			
			//Process the standalone devices
			List<TestRunSelectedDeviceList> testRunSelectedDeviceList=testRunSelectedDeviceListDAO.list(testRunConfigurationChild.getTestRunConfigurationChildId());
			if (testRunSelectedDeviceList == null || testRunSelectedDeviceList.isEmpty()) {
			} else {
				for (TestRunSelectedDeviceList list:testRunSelectedDeviceList){
					
					DeviceList device = deviceListDAO.getByDeviceListId(list.getDeviceList().getDeviceListId());
					//Check if the device was already present in one of the environments. If yes, ignore, else, process it
					if (targetDevices.contains(device)) {
						continue;
					} else  {
						if (device != null) {
							Hibernate.initialize(device.getDeviceListId());
							Hibernate.initialize(device.getDeviceModelMaster());
							if (device.getDeviceModelMaster() != null)
								Hibernate.initialize(device.getDeviceModelMaster().getDeviceMakeMaster());
							
							Hibernate.initialize(device.getCommonActiveStatusMaster());
	
							Hibernate.initialize(device.getDevicePlatformVersionListMaster());
							if (device.getDevicePlatformVersionListMaster() != null)
								Hibernate.initialize(device.getDevicePlatformVersionListMaster().getDevicePlatformMaster());
							Hibernate.initialize(device.getHostList());
						}
						targetDevices.add(device);
					}
				}
			}
		} catch (Exception e) {
			log.error("Problem getting devices for TestRunConfigurationChild", e);
		}
		return targetDevices;
	}



	@Override
	@Transactional
	public List<TestRunListCustom> getExecutedStepsCountofTestRunlist(List<TestRunList> testRunList){
		return testExecutionResultDAO.getExecutedStepsCountofTestRunlist(testRunList);
	}
	
	@Override
	@Transactional
	public List<TestRunList> listExecutedTestRunList(int hours,int startIndex, int pageSize) {
		
		return testRunListDAO.listExecutedJobList(hours,startIndex,pageSize);
	}
	
	@Override
	@Transactional
	public List<TestRunList> listExecutingTestRunList() {
		
		return testRunListDAO.listExecutingJobList();
	}
	
	@Override
	@PostConstruct
	public void clearTestRunList(){
		//reset queued and executing runs
		testRunListDAO.clearTestRuns();		
		
	}

	@Override
	@Transactional
	public List<TestExecutionResult> getTestExecResultsForTestCase(int testRunListId, int TestCaseId) {
		return testExecutionResultDAO.getTestExecResultsForTestCase(testRunListId, TestCaseId);
		
	}

	@Override
	@Transactional
	public void addTestExecutionResultsExportData(TestExecutionResultsExportData testExecutionResultsExportData) {
		testExecutionResultDAO.addTestExportData(testExecutionResultsExportData);
		
	}

	@Override
	@Transactional
	public int getTotalTestRunListInLast24Hours(int hours) {
		return testRunListDAO.getTotalTestRunListInLast24Hours(hours);
	}

	@Override
	public List<TestRunExecutionStatusVO> executeTestRuns(int testRunConfigurationParentId) {
		return null;
	}
	
	@Transactional
	@Override
	public List<TestCaseList> getSelectedTestCasesFromPlan(TestRunList testRunList) {
		try {
			int trccExecutionPlanId = testRunList.getTrccExecutionPlan().getTrccExecutionPlanId();
			Integer deviceListId = testRunList.getDeviceList().getDeviceListId();
			List<TestCaseList> selectedTestCases = trccExecutionPlansCommonDAO.getSelectedTestCasesFromPlanDetails(trccExecutionPlanId, deviceListId);

			if (selectedTestCases == null) {
				log.debug("Unable to get selected testcases for TestRun List");
				return null;
			}
			return selectedTestCases;
		} catch (Exception e) {
			
			log.error("Some problem getting selected test cases for Test run list", e);
			return null;
		}
	}
	
	@Transactional
	@Override
	public List<TestCaseList> getSelectedTestCasesFromPlan(int testRunListId) {
		log.info("testRunListId="+testRunListId);
		log.info("testRunListDAO="+testRunListDAO);
		TestRunList testRunList = testRunListDAO.getByTestRunListId(testRunListId);
		if (testRunList == null)
			return null;
		return getSelectedTestCasesFromPlan(testRunList);
	}

	

	@Override
	@Transactional
	public List<TrccExecutionPlan> listTrccExecutionPlan(int testRunConfigurationChildId) {
		return trccExecutionPlansCommonDAO.listTrccExecutionPlan(testRunConfigurationChildId);
	}

	@Override
	@Transactional
	public void addTrccExecutionPlan(TrccExecutionPlan trccExecutionPlan) {
		trccExecutionPlansCommonDAO.addTrccExecutionPlan(trccExecutionPlan);
	}

	@Override
	@Transactional
	public void updateTrccExecutionPlan(TrccExecutionPlan trccExecutionPlan) {
		trccExecutionPlansCommonDAO.updateTrccExecutionPlan(trccExecutionPlan);
	}

	@Override
	@Transactional
	public void deleteTrccExecutionPlan(int trccExecutionPlanId) {
		TrccExecutionPlan trccExecutionPlan=trccExecutionPlansCommonDAO.getTrccExecutionPlanById(trccExecutionPlanId);
		trccExecutionPlansCommonDAO.deleteTrccExecutionPlan(trccExecutionPlan);
	}
	@Override
	@Transactional
	public int getTotalRecordsOfTrccExecutionPlan(int testConfigurationChildId) {
		return trccExecutionPlansCommonDAO.getTotalRecordsOfTrccExecutionPlan(testConfigurationChildId);
	}

	@Override
	@Transactional
	public TrccExecutionPlan getTrccExecutionPlanByName(String planName) {
		return trccExecutionPlansCommonDAO.getTrccExecutionPlanByName(planName);
	}

	@Override
	@Transactional
	public TrccExecutionPlan getTrccExecutionPlanById(int trccExecutionPlanId) {
		return trccExecutionPlansCommonDAO.getTrccExecutionPlanById(trccExecutionPlanId);
	}

	@Override
	@Transactional
	public void addTrccExecutionPlanDetail(int trccExecutionPlanId, int deviceListId, String [] testCaseLists) {
		List<TrccExecutionPlanDetails> trccExecutionPlanDetails = new ArrayList<TrccExecutionPlanDetails>();
		trccExecutionPlanDetails = trccExecutionPlansCommonDAO.listTrccExecutionPlanDetails(trccExecutionPlanId, deviceListId);
		if(trccExecutionPlanDetails!=null){
			trccExecutionPlansCommonDAO.deleteExistingTrccExecutionPlanDetails(trccExecutionPlanId, deviceListId);
		}
		
		DeviceList deviceList = null;
		try {
			deviceList = deviceListDAO.getByDeviceListId(deviceListId);
		} catch (Exception e) {
			
			log.info("Some problem in getting device info", e);
			return;
		}
		TrccExecutionPlan trccExecutionPlan = null;
		try
		{
			trccExecutionPlan = trccExecutionPlansCommonDAO.getTrccExecutionPlanById(trccExecutionPlanId);
		} catch (Exception e) {
			
			log.info("Some problem in getting Plan info", e);
			return;
		}
		trccExecutionPlanDetails = new ArrayList<TrccExecutionPlanDetails>();
		try
		{
	    	for(String Id : testCaseLists){
	    		TrccExecutionPlanDetails trccExecutionPlanDetail=new TrccExecutionPlanDetails();
	    		int testCaseId = Integer.parseInt(Id);
	    		log.info("id="+Id);
	    		TestCaseList testCaseList = testCaseListDAO.getByTestCaseId(testCaseId);
	    		trccExecutionPlanDetail.setTestCaseList(testCaseList);
	    		trccExecutionPlanDetail.setDeviceList(deviceList);
	    		trccExecutionPlanDetail.setTrccExecutionPlan(trccExecutionPlan);
	    		trccExecutionPlanDetails.add(trccExecutionPlanDetail);
	    	}
	    	trccExecutionPlansCommonDAO.addTrccExecutionPlanDetails(trccExecutionPlanDetails);
		} catch (Exception e) {
			log.info("Some problem in adding Plan details records", e);
			return;
		}
	}
	
	@Override
	@Transactional
	public void updateTrccExecutionPlanDetail(TrccExecutionPlanDetails trccExecutionPlanDetails,int trccExecutionPlanId,int deviceListId) {
		DeviceList deviceList = deviceListDAO.getByDeviceListId(deviceListId);
		TrccExecutionPlan trccExecutionPlan = trccExecutionPlansCommonDAO.getTrccExecutionPlanById(trccExecutionPlanId);
		trccExecutionPlanDetails.setDeviceList(deviceList);
		trccExecutionPlanDetails.setTrccExecutionPlan(trccExecutionPlan);
		trccExecutionPlansCommonDAO.updateTrccExecutionPlanDetail(trccExecutionPlanDetails);
	}
	@Override
	@Transactional
	public List<TrccExecutionPlanDetails> listTrccExecutionPlanDetails(int trccExecutionPlanId, int deviceListId) {
		return trccExecutionPlansCommonDAO.listTrccExecutionPlanDetails(trccExecutionPlanId, deviceListId);
	}
	
	@Override
	@Transactional
	public List<TestRunExecutionStatusVO> executeTestRunPlans(int testRunConfigurationChildId) {

		TestRunConfigurationChild testRunConfigurationChild = null;
		List<TrccExecutionPlan> trccExecutionPlans = null;
		try
		{
			testRunConfigurationChild=testRunConfigurationChildDAO.getByTestRunConfigurationChildId(testRunConfigurationChildId);
			trccExecutionPlans = trccExecutionPlansCommonDAO.listTrccExecutionPlan(testRunConfigurationChildId);
		}
		catch (Exception e) {        	
	        log.error("Problem executing Test Run Plans", e);            
	        return null;
	    }
		
		if (testRunConfigurationChild == null)
			return null;
		if (trccExecutionPlans == null)
			return null;
		
		List<TestRunExecutionStatusVO> statusList = new ArrayList<TestRunExecutionStatusVO>();
		TestRunExecutionStatusVO status = null;
		for (TrccExecutionPlan trccExecutionPlan : trccExecutionPlans) {
			status = executeTestRunPlan(testRunConfigurationChild, trccExecutionPlan);
			if (status != null)
				statusList.add(status);
		}
		return statusList;
	}
	
	@Override
	@Transactional
	public TestRunExecutionStatusVO executeTestRunPlan(int testRunConfigurationChildId, int trccExecutionPlanId) {
		
		TestRunConfigurationChild testRunConfigurationChild=testRunConfigurationChildDAO.getByTestRunConfigurationChildId(testRunConfigurationChildId);
		if (testRunConfigurationChild == null)
			return null;
		TrccExecutionPlan trccExecutionPlan = new TrccExecutionPlan(trccExecutionPlanId);
		return executeTestRunPlan(testRunConfigurationChild, trccExecutionPlan);
	}
	
	@Override
	@Transactional
	public TestRunExecutionStatusVO executeTestRunPlan(TestRunConfigurationChild testRunConfigurationChild, TrccExecutionPlan trccExecutionPlan) {
		
		TestRunList testRunList=null;
		TestRunExecutionStatusVO statusVO = new TestRunExecutionStatusVO();
		Set<TestEnvironmentDevices> testEnvironmentDevicesList = null;
		List<DeviceList> targetDevices = new ArrayList<DeviceList> ();

		try {
			//get configured list of devices
			List<TestRunSelectedDeviceList> testRunSelectedDeviceList=testRunSelectedDeviceListDAO.list(testRunConfigurationChild.getTestRunConfigurationChildId());
			int lastRunNo=testRunConfigurationChild.getLastRunNo();
			testRunConfigurationChild.setLastRunNo(lastRunNo+1);
			testRunConfigurationChildDAO.update(testRunConfigurationChild);
			lastRunNo++;
			//get configured list of TestEnviornmnets
			testEnvironmentDevicesList=testRunConfigurationChild.getTestEnvironmentDeviceses();

			//Added for Hudson
			String scriptSource=testRunConfigurationChild.getTestSuiteList().getTestScriptSource();
			
			//Process the devices in the test environments first
			
			if (testEnvironmentDevicesList == null || testEnvironmentDevicesList.isEmpty()) {
				log.info("There are no test environments specified for the test run configuration");
			} else {
				log.info("No of test environments specified for the test run configuration : " + testEnvironmentDevicesList.size());
				for (TestEnvironmentDevices ted: testEnvironmentDevicesList) {	
					
					Set<DeviceList> devicesList = ted.getDeviceList();
					if (devicesList == null || devicesList.isEmpty()) {
						log.info("There are no test devices in the test environment : " + ted.getName());
					} else {
						for (DeviceList device : devicesList) {
							if (targetDevices.contains(device)) {
								log.info("The device was already processed in this test run : " + device.getDeviceId());
								continue;
							} else {
								targetDevices.add(device);
								if(scriptSource.equalsIgnoreCase(TAFConstants.TEST_SCRIPT_SOURCE_HUDSON)) {
									testRunList = createJobForDevice(testRunConfigurationChild, device, lastRunNo, null);
								}
								else {
									TrccExecutionPlan trccExecutionPlanToExecute = validateAndGetTrccExecutionPlanToExecute(trccExecutionPlan, device.getDeviceListId());
									testRunList = createJobForDevice(testRunConfigurationChild, device, lastRunNo, trccExecutionPlanToExecute);
								}
								
								if (testRunList != null) {
									log.info("inside the tesTRunList after the Create Jobs for device");
									testRunListDAO.update(testRunList);
								}
							}
						}
					}
				}
			}
			
			//Process the standalone devices
			if (testRunSelectedDeviceList == null || testRunSelectedDeviceList.isEmpty()) {
				log.info("There are no standalone devices specified for the test run configuration");
			} else {
				log.info("Standalone devices specified for the test run configuration : " + testRunSelectedDeviceList.size());
				for (TestRunSelectedDeviceList list:testRunSelectedDeviceList){
					
					DeviceList device = deviceListDAO.getByDeviceListId(list.getDeviceList().getDeviceListId());
					//Check if the device was already present in one of the environments. If yes, ignore, else, process it
					if (targetDevices.contains(device)) {
						log.info("The device was already processed in this test run : " + device.getDeviceId());
						continue;
					} else  {
						targetDevices.add(device);
						if(scriptSource.equalsIgnoreCase(TAFConstants.TEST_SCRIPT_SOURCE_HUDSON)) {
							testRunList = createJobForDevice(testRunConfigurationChild, device, lastRunNo, null);   //commented for selective test cases execution
						}
						else {
							TrccExecutionPlan trccExecutionPlanToExecute = validateAndGetTrccExecutionPlanToExecute(trccExecutionPlan, device.getDeviceListId());
							testRunList = createJobForDevice(testRunConfigurationChild, device, lastRunNo, trccExecutionPlanToExecute);
						}
					}
				}
			}		

			if(targetDevices==null || targetDevices.isEmpty()){	
				log.info("No devices specified for the Test Run");
				statusVO.hasExecutedSuccessfully = false;
				statusVO.executionMessage = "Devices have not been specified or active";
				return statusVO;
			} else {
				log.info("Total No of devices specified for the Test Run : " + targetDevices.size());
			}
			statusVO.hasExecutedSuccessfully = true;
			statusVO.executionMessage = "Jobs created for active devices";
			statusVO.testRunList = testRunList;
        
		} catch (Exception e) {        	
            log.error("Problem executing Test Run ", e);            
			statusVO.hasExecutedSuccessfully = false;
			statusVO.executionMessage = "Problems in executing. Some jobs may not have been created";
			statusVO.testRunList = testRunList;
        }
		return statusVO;
	}

	// Added for Selective Test Case Execution module - To execute DevicePlan directly
	@Override
	@Transactional
	public TestRunExecutionStatusVO executeTestRunPlanOnADevice(int testRunConfigurationChildId, int trccExecutionPlanId, int deviceListId) {
		
		TestRunList testRunList=null;
		TestRunExecutionStatusVO statusVO = new TestRunExecutionStatusVO();

		try {
			
			TestRunConfigurationChild testRunConfigurationChild = testRunConfigurationChildDAO.getByTestRunConfigurationChildId(testRunConfigurationChildId);;
			
			int lastRunNo=testRunConfigurationChild.getLastRunNo();
			testRunConfigurationChild.setLastRunNo(lastRunNo+1);
			testRunConfigurationChildDAO.update(testRunConfigurationChild);
			lastRunNo++;
			
			DeviceList device = deviceListDAO.getByDeviceListId(deviceListId);
			TrccExecutionPlan trccExecutionPlanToExecute = validateAndGetTrccExecutionPlanToExecute(trccExecutionPlanId, deviceListId);
			testRunList = createJobForDevice(testRunConfigurationChild, device, lastRunNo, trccExecutionPlanToExecute);
			
			log.info("Total No of devices specified for the Test Run - Plan: 1");
			
			statusVO.hasExecutedSuccessfully = true;
			statusVO.executionMessage = "Jobs created for active devices";
			statusVO.testRunList = testRunList;
        
		} catch (Exception e) {        	
            log.error("Problem executing Test Run ", e);            
			statusVO.hasExecutedSuccessfully = false;
			statusVO.executionMessage = "Problems in executing. Some jobs may not have been created";
			statusVO.testRunList = testRunList;
        }
		return statusVO;
	}

	@Override
	@Transactional
	public List<TestRunExecutionStatusVO> executeTestRuns(int testRunConfigurationParentId, HttpServletRequest request) {
		
		log.trace("In Execute TestRunConfigurationParent");
		TestRunConfigurationParent testRunConfigurationParent=testRunConfigurationParentDAO.getByTestRunConfigurationParentId(testRunConfigurationParentId);
		if (testRunConfigurationParent == null) {
			log.debug("TestRunConfigurationParent not found : " + testRunConfigurationParentId);
			return null;
		}
		Set<TestRunConfigurationChild> testRunConfigurationChildList = testRunConfigurationParent.getTestRunConfigurationChilds();
		if (testRunConfigurationChildList == null || testRunConfigurationChildList.isEmpty()) {
			log.debug("No child configurations found for TestRunConfigurationParent : " + testRunConfigurationParentId);
			return null;
		} 
		List<TestRunExecutionStatusVO> statusList = new ArrayList<TestRunExecutionStatusVO>();
		TestRunExecutionStatusVO status = null;
		for (TestRunConfigurationChild testRunConfigurationChild : testRunConfigurationChildList) {
			status = executeTestRun(testRunConfigurationChild);
			if (status != null)
				statusList.add(status);
		}
		return statusList;
	}

	@Override
	@Transactional
	public TestRunExecutionStatusVO executeTestRun(int testRunConfigurationChildId) {
		
		TestRunConfigurationChild testRunConfigurationChild=testRunConfigurationChildDAO.getByTestRunConfigurationChildId(testRunConfigurationChildId);
		if (testRunConfigurationChild == null)
			return null;
		return executeTestRun(testRunConfigurationChild);
	}
	
	@Override
	@Transactional
	public TestRunExecutionStatusVO executeTestRun(TestRunConfigurationChild testRunConfigurationChild) {

		return executeTestRunPlan(testRunConfigurationChild, null);
	}
	private TestRunList createJobForDevice(TestRunConfigurationChild testRunConfigurationChild, DeviceList device, int lastRunNo, TrccExecutionPlan trccExecutionPlan) {
		
        log.info("Creating job for device : " + device.getDeviceId());            
		if (device == null)
			return null;

		try {
			
			//Get the device status
			String deviceStatus=device.getCommonActiveStatusMaster().getStatus();
			
			//Create a new TestRunList entry for the device
			//buildNo will be updated by client later when test is actually getting executed 
			TestRunList testRunList = new TestRunList();
			testRunList.setBuildNo(0);
			testRunList.setRunNo(lastRunNo);
			
			DeviceList deviceList = new DeviceList();
			deviceList.setDeviceListId(device.getDeviceListId());
			testRunList.setDeviceList(deviceList);						
			TestResultStatusMaster status = new TestResultStatusMaster();
			status.setTestResultStatus(TestExecutionStatus.FAILED.toString());
			testRunList.setTestResultStatusMaster(status);						
			testRunList.setTestRunConfigurationChild(testRunConfigurationChild);				
			
			//And work around for "om.mysql.jdbc.MysqlDataTruncation:" error
			testRunList.setTestRunTriggeredTime(new Date(System.currentTimeMillis()));
			
			TrccExecutionPlan trccExecutionPlanToExecute = null;
			if (trccExecutionPlan != null)
			{	
				trccExecutionPlanToExecute = new TrccExecutionPlan(trccExecutionPlan.getTrccExecutionPlanId());
			}	
			testRunList.setTrccExecutionPlan(trccExecutionPlanToExecute);
			if(deviceStatus.equals(DeviceStatus.INACTIVE.toString()) || deviceStatus.equals(DeviceStatus.BUSY.toString())){
										
		        log.info("Creating job - Device is not active or busy : " + device.getDeviceId());            
				//Device is not active. No need to publish the job. Set the reason for failure alone						
				if(deviceStatus.equals(DeviceStatus.INACTIVE.toString()))
					testRunList.setTestRunFailureMessage("Device Offline!");
				else
					testRunList.setTestRunFailureMessage("Device Busy!");
				
				testRunListDAO.add(testRunList);
	            log.info("Created Job No : " + testRunList.getTestRunListId() + " for device : " + device.getDeviceId() 
	            												+ ". Not posting to terminal as device is not active");            
				
			}else if(deviceStatus.equals(DeviceStatus.ACTIVE.toString())){						
				//queue the run and inform the client/terminal
				testRunList.getTestResultStatusMaster().setTestResultStatus(TestExecutionStatus.QUEUED.toString());
				
				testRunListDAO.add(testRunList);						
				HostHeartbeat host =null;// hostHeartbeatDAO.getByHostId(device.getHostList().getHostId());
				host.setHasResponse(true); //Will this work ? It will throw an NPE
				host.setResponseToSend((short)ClientReponseMessage.PULSE_ACK_JOBS_AVAILABLE);
				hostHeartbeatDAO.update(host);
	            log.info("Created Job for host : " + device.getHostList().getHostName() + " : " + device.getHostList().getHostIpAddress());            
	            log.info("Created Job No : " + testRunList.getTestRunListId() + " for device : " + device.getDeviceId() 
	            												   + " and queued for terminal : " + host.getHostId());            
			}
			return testRunList;
		} catch (Exception e) {
            log.error("Problem in creating job for device : " + device.getDeviceId(), e);            
			return null;
		}
	}
	
	private TrccExecutionPlan validateAndGetTrccExecutionPlanToExecute (int trccExecutionPlanId, int deviceListId)
	{
		TrccExecutionPlan trccExecutionPlanToExecute = null;
		int trccExecutionPlanDetailsRecordCount = trccExecutionPlansCommonDAO.getTotalRecordsOfTrccExecutionPlanDetailsOnADevice(
																							   trccExecutionPlanId, deviceListId);
		if (trccExecutionPlanDetailsRecordCount > 0)
			trccExecutionPlanToExecute = new TrccExecutionPlan(trccExecutionPlanId);
		return trccExecutionPlanToExecute;
	}
	
	private TrccExecutionPlan validateAndGetTrccExecutionPlanToExecute (TrccExecutionPlan trccExecutionPlan, int deviceListId)
	{
		TrccExecutionPlan trccExecutionPlanToExecute = null;
		if (trccExecutionPlan != null)
		{	int trccExecutionPlanId = trccExecutionPlan.getTrccExecutionPlanId();	
			int trccExecutionPlanDetailsRecordCount = trccExecutionPlansCommonDAO.getTotalRecordsOfTrccExecutionPlanDetailsOnADevice(
																							   trccExecutionPlanId, deviceListId);
			if (trccExecutionPlanDetailsRecordCount > 0)
				trccExecutionPlanToExecute = new TrccExecutionPlan(trccExecutionPlanId);
		}	
		return trccExecutionPlanToExecute;
	}
	
	/*
	 * Prepare for a Testsuite
	 */
	@Override
	@Transactional
	public String prepareBDDTestScriptsForJobExecution(TestSuiteList testSuite, int testRunJobId) {
		
		try {
			
			String destinationDirectory = CommonUtility.getDirectoryForBDDStoriesZipForJob(testRunJobId, testScriptsDestinationDirectory);
			log.info("Destination Directory : " + destinationDirectory);

			if (destinationDirectory == null) {
				log.info("FAILED : Unable to specific destination directory for stories : " + testRunJobId);
				return "FAILED : Unable to specific destination directory for stories : " + testRunJobId;
			} else  {
				String storiesZip = testCaseScriptGenerationService.generateBDDTestCaseAutomationScriptFiles(testSuite.getTestSuiteId(), "TestSuite", "GHERKIN", "", destinationDirectory);
				if (storiesZip == null || storiesZip.startsWith("FAILED")) {
					log.info("FAILED : Unable to create Stories pack for Job : " + testRunJobId);
				}
				return storiesZip;
			}
		} catch (Exception e) {
			log.error("FAILED : Unable to create Stories pack for Job : " + testRunJobId, e);
			return "FAILED : Unable to create Stories pack for Job : " + testRunJobId;
		}
	}

	/*
	 * Prepare for a Testcase
	 */
	@Override
	@Transactional
	public String prepareBDDTestScriptsForJobExecution(TestSuiteList testSuite, int testRunJobId, int testCaseId) {
		
		try {
			String destinationDirectory = CommonUtility.getDirectoryForBDDStoriesZipForJob(testRunJobId, testScriptsDestinationDirectory);
			log.info("Destination Directory : " + destinationDirectory);

			if (destinationDirectory == null) {
				log.info("FAILED : Unable to specific destination directory for stories : " + testRunJobId);
				return "FAILED : Unable to specific destination directory for stories : " + testRunJobId;
			} else  {
				String storiesZip = testCaseScriptGenerationService.generateBDDTestCaseAutomationScriptFiles(testCaseId, "TestCase", "GHERKIN", "", destinationDirectory);
				if (storiesZip == null || storiesZip.startsWith("FAILED")) {
					log.info("FAILED : Unable to create Stories pack for Job : " + testRunJobId);
				}
				return storiesZip;
			}
		} catch (Exception e) {
			log.error("FAILED : Unable to create Stories pack for Job : " + testRunJobId, e);
			return "FAILED : Unable to create Stories pack for Job : " + testRunJobId;
		}
	}

	/*
	 * Prepare for a Testcase
	 */
	@Override
	@Transactional
	public String prepareBDDTestScriptsForJobExecution(TestSuiteList testSuite, int testRunJobId, Set<TestCaseList> testCases) {
		
		return prepareBDDTestScriptsForJobExecution(testSuite, testRunJobId, testCases, null);
	}
		
	/*
	 * Prepare for a Testcase
	 */
	@Override
	@Transactional
	public String prepareBDDTestScriptsForJobExecution(TestSuiteList testSuite, int testRunJobId, Set<TestCaseList> testCases, TestRunPlan testRunPlan) {

		try {
			String destinationDirectory = CommonUtility.getDirectoryForBDDStoriesZipForJob(testRunJobId, testScriptsDestinationDirectory);
			destinationDirectory = CommonUtility.getCatalinaPath() + File.separator + destinationDirectory;
			log.info("BDD Script Pack Destination Directory for Job : " + testRunJobId + " : " + destinationDirectory);
			//sort the testcases if order exists
			List<TestCaseList> tcltoSort = new ArrayList<>();
			if(testCases.size()>0){
					Iterator<TestCaseList> firstIt = testCases.iterator();
					tcltoSort.addAll(testCases);
					boolean orderHasZeroflg = false;			
					while (firstIt.hasNext()) {
						Integer executionOrder = (Integer) firstIt.next().getTestCaseExecutionOrder();
						if(executionOrder == null || executionOrder == 0)
							orderHasZeroflg = true;
						if(orderHasZeroflg)
							break;
					}			
					if(orderHasZeroflg) {
						java.util.Collections.sort(tcltoSort, new Comparator<TestCaseList>(){
							@Override
							public int compare(TestCaseList tcl1, TestCaseList tcl2) {
								return tcl1.getTestCaseId().compareTo(tcl2.getTestCaseId());
							}
						});
					} else {
						java.util.Collections.sort(tcltoSort, new Comparator<TestCaseList>(){
							@Override
							public int compare(TestCaseList tcl1, TestCaseList tcl2) {					
								return tcl1.getTestCaseExecutionOrder().compareTo(tcl2.getTestCaseExecutionOrder());					
							}			
						});
					}
				}
				testCases = new LinkedHashSet();
				testCases.addAll(tcltoSort);
			
			log.info("Total Test cases : " + testCases.size());
			if (destinationDirectory == null) {
				log.info("FAILED : Unable to specific destination directory for stories : " + testRunJobId);
				return "FAILED : Unable to specific destination directory for stories : " + testRunJobId;
			} else  {
				String storiesZip = testCaseScriptGenerationService.generateBDDTestCaseAutomationScriptFiles(testCases, "GHERKIN", "", destinationDirectory, testRunPlan);
				if (storiesZip == null || storiesZip.startsWith("FAILED")) {
					log.info("FAILED : Unable to create Stories pack for Job : " + testRunJobId);
				}
				return storiesZip;
			}
		} catch (Exception e) {
			log.error("FAILED : Unable to create Stories pack for Job : " + testRunJobId, e);
			return "FAILED : Unable to create Stories pack for Job : " + testRunJobId;
		}
	}
}
