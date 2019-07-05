package com.hcl.atf.taf.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.QueryParam;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hcl.atf.taf.constants.IDPAConstants;
import com.hcl.atf.taf.constants.TAFConstants;
import com.hcl.atf.taf.controller.utilities.CommonUtility;
import com.hcl.atf.taf.controller.utilities.DateUtility;
import com.hcl.atf.taf.controller.utilities.UploadForm;
import com.hcl.atf.taf.dao.ProductBuildDAO;
import com.hcl.atf.taf.dao.TestManagementSystemDAO;
import com.hcl.atf.taf.dao.WorkPackageDAO;
import com.hcl.atf.taf.model.DeviceList;
import com.hcl.atf.taf.model.DevicePlatformMaster;
import com.hcl.atf.taf.model.DevicePlatformVersionListMaster;
import com.hcl.atf.taf.model.ExecutionTypeMaster;
import com.hcl.atf.taf.model.HostHeartbeat;
import com.hcl.atf.taf.model.HostList;
import com.hcl.atf.taf.model.LifeCyclePhase;
import com.hcl.atf.taf.model.ProductBuild;
import com.hcl.atf.taf.model.ProductMaster;
import com.hcl.atf.taf.model.ProductVersionListMaster;
import com.hcl.atf.taf.model.ScriptTypeMaster;
import com.hcl.atf.taf.model.TestCategoryMaster;
import com.hcl.atf.taf.model.TestEnviromentMaster;
import com.hcl.atf.taf.model.TestRunConfigurationChild;
import com.hcl.atf.taf.model.TestRunConfigurationParent;
import com.hcl.atf.taf.model.TestRunPlan;
import com.hcl.atf.taf.model.TestRunSelectedDeviceList;
import com.hcl.atf.taf.model.TestSuiteList;
import com.hcl.atf.taf.model.UserList;
import com.hcl.atf.taf.model.WorkFlowEvent;
import com.hcl.atf.taf.model.WorkPackage;
import com.hcl.atf.taf.model.json.JsonHostList;
import com.hcl.atf.taf.model.json.JsonTestRunList;
import com.hcl.atf.taf.model.json.JsonTestSuiteList;
import com.hcl.atf.taf.model.json.jtable.JTableSingleResponse;
import com.hcl.atf.taf.mongodb.service.MongoDBService;
import com.hcl.atf.taf.service.DeviceListService;
import com.hcl.atf.taf.service.EventsService;
import com.hcl.atf.taf.service.ExecutionTypeMasterService;
import com.hcl.atf.taf.service.HostHeartBeatService;
import com.hcl.atf.taf.service.HostListService;
import com.hcl.atf.taf.service.ProductListService;
import com.hcl.atf.taf.service.TestCaseScriptGenerationService;
import com.hcl.atf.taf.service.TestExecutionService;
import com.hcl.atf.taf.service.TestRunConfigurationService;
import com.hcl.atf.taf.service.TestSuiteConfigurationService;
import com.hcl.atf.taf.service.UserListService;
import com.hcl.atf.taf.service.WorkPackageService;
import com.hcl.atf.taf.util.Configuration;

@Controller
@RequestMapping("/rest_API")
public class RestController {

	private static final Log log = LogFactory.getLog(RestController.class);

	@Autowired
	private TestSuiteConfigurationService testSuiteConfigurationService;
	@Autowired
	private TestRunConfigurationService testRunConfigurationService;
	@Autowired
	private ProductListService productListService;
	@Autowired
	private DeviceListService deviceListService;
	@Autowired
	private HostListService hostListService;
	@Autowired
	private TestExecutionService testExecutionService;
	@Autowired
	private ToolIntegrationController toolsController;
	@Autowired
	private HostHeartBeatService hostHeartBeatService;
	@Autowired
	private EventsService eventsService;
	@Autowired
	private WorkPackageService workPackageService;
	@Autowired
	private ExecutionTypeMasterService executionTypeMasterService;
	@Autowired
	private ProductBuildDAO productBuildDAO;
	@Autowired
	private MongoDBService mongoDBService;
	@Autowired
	private UserListService userListService;	
	@Autowired
	private	TestManagementSystemDAO testManagementSystemDAO;
	@Autowired
	private	WorkPackageDAO workPackageDAO;
	@Autowired
	private HttpServletRequest request;
	@Autowired
	private TestCaseScriptGenerationService testCaseScriptGenerationService;
	
	@RequestMapping(value = "/create", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody
	TestSuite addTestSuite() {
		TestSuite jTableSingleResponse = new TestSuite();
		return jTableSingleResponse;
	}

	@RequestMapping(value = "/tafExecutionTrigger", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody
	JTableSingleResponse addTestSuite1(@RequestParam String testSuiteName,
			String runName, String runConfigName, String productName,
			String productVersionName, String devicePlatformName,
			String devicePlatformVersion, String runEnvironment,
			String testSuitePath, String toolName,
			String deviceid) {
		JTableSingleResponse jTableSingleResponse;
		try {

			TestSuiteList testSuiteList = new TestSuiteList();

			ProductMaster productMaster = productListService
					.getProductByName(productName);
			ProductVersionListMaster productVersionListMaster = null;

			if (productMaster != null) {
				productVersionListMaster = productListService
						.getProductVersionListMasterByName(productVersionName);

				if (productVersionListMaster != null) {

					productVersionListMaster.setProductMaster(productMaster);

					DeviceList deviceList = testRunConfigurationService
							.deviceslistById(deviceid);
					if (deviceList != null && deviceList.getCommonActiveStatusMaster().getStatus().equals("ACTIVE")) {

						List<DevicePlatformVersionListMaster> versionListMasters = deviceListService
								.platformVersionsList(devicePlatformName);
						DevicePlatformMaster devicePlatformMaster = new DevicePlatformMaster();
						DevicePlatformVersionListMaster devicePlatformVersionListMaster = new DevicePlatformVersionListMaster();
						for (DevicePlatformVersionListMaster versionlist : versionListMasters) {
							if (versionlist.getDevicePlatformVersion().equals(
									devicePlatformVersion)) {
								devicePlatformMaster = versionlist
										.getDevicePlatformMaster();
								devicePlatformVersionListMaster = versionlist;
							}
						}

						if (testScriptFileAvailablity(testSuitePath)) {
							testSuiteList
									.setProductVersionListMaster(productVersionListMaster);
							testSuiteList.setProductMaster(productMaster);

							testSuiteList.setTestSuiteName(testSuiteName);
							testSuiteList
									.setTestSuiteScriptFileLocation(testSuitePath);

							ScriptTypeMaster scriptTypeMaster = new ScriptTypeMaster();
							scriptTypeMaster
									.setScriptType(TAFConstants.TESTSCRIPT_TYPE_JUNIT);
							testSuiteList.setScriptTypeMaster(scriptTypeMaster);

							testSuiteList
									.setTestScriptSource(TAFConstants.TEST_SCRIPT_SOURCE_HUDSON);

							testSuiteConfigurationService
									.addTestSuite(testSuiteList);

							TestRunConfigurationParent testRunConfigurationParent = new TestRunConfigurationParent();
							testRunConfigurationParent
									.setProductMaster(productMaster);
							UserList userList = new UserList();
							userList.setUserId(1);
							testRunConfigurationParent.setUserList(userList);

							testRunConfigurationParent
									.setTestRunconfigurationName(runName);
							testRunConfigurationParent
									.setTestRunConfigurationDescription("Hudson creation");

							testRunConfigurationService
									.addTestRunConfigurationParent(testRunConfigurationParent);

							TestRunConfigurationChild testRunConfigurationChild = new TestRunConfigurationChild();
							testRunConfigurationChild
									.setTestRunConfigurationParent(testRunConfigurationParent);

							devicePlatformVersionListMaster
									.setDevicePlatformMaster(devicePlatformMaster);

							testRunConfigurationChild
									.setTestSuiteList(testSuiteList);

							TestEnviromentMaster testEnviromentMaster = new TestEnviromentMaster();
							List<TestEnviromentMaster> enviromentMasters = testRunConfigurationService
									.testEnviromentsList(runEnvironment);
							for (TestEnviromentMaster enMas : enviromentMasters) {
								if (enMas.getTestToolMaster().getTestToolName()
										.equals(toolName)) {
									testEnviromentMaster = enMas;
								}
							}

							testRunConfigurationChild
									.setTestEnviromentMaster(testEnviromentMaster);

							String testRunScheduledStartTime = null;
							int testRunScheduledIntervalInHour = 0;
							int testRunRecurrenceLimit = 0;
							String testRunScheduledEndTime = "";
							int lastRunNo = 0;
							String testCategory = "REGRESSION";
							String testRunCronSchedule = "";
							String description = "";
							String testRunConfigurationName = "";
							String notifyByMail = "";
							String locale = "English";

							SimpleDateFormat sdf = new SimpleDateFormat(
									"yyyy-MM-dd HH:mm:ss");
							try {
								if (!(testRunScheduledStartTime == null || testRunScheduledStartTime
										.trim() == ""))
									testRunConfigurationChild
											.setTestRunScheduledStartTime(sdf
													.parse(testRunScheduledStartTime));
							} catch (ParseException e) {
								e.printStackTrace();
							}
							testRunConfigurationChild
									.setTestRunScheduledIntervalInHour(testRunScheduledIntervalInHour);
							testRunConfigurationChild
									.setTestRunRecurrenceLimit(testRunRecurrenceLimit);

							testRunConfigurationChild
									.setLastRunNo(testRunConfigurationService
											.getMaxRunNo() + 1);

							TestCategoryMaster testCategoryMaster = new TestCategoryMaster();
							if (testCategory != null) {
								testCategoryMaster
										.setTestCategory(testCategory);
							}
							testRunConfigurationChild
									.setTestCategoryMaster(testCategoryMaster);

							testRunConfigurationChild
									.setTestRunCronSchedule(testRunCronSchedule);
							testRunConfigurationChild
									.setDescription(description);
							testRunConfigurationChild
									.setTestRunConfigurationName(testRunConfigurationName);
							try {
								if (!(testRunScheduledEndTime == null || testRunScheduledEndTime
										.trim() == ""))
									testRunConfigurationChild
											.setTestRunScheduledEndTime(sdf
													.parse(testRunScheduledEndTime));
							} catch (ParseException e) {
								e.printStackTrace();
							}
							testRunConfigurationChild
									.setNotifyByMail(notifyByMail);
							testRunConfigurationChild.setLocale(locale);

							testRunConfigurationChild.setStatus(1);
							testRunConfigurationChild
									.setStatusChangeDate(new Date(System
											.currentTimeMillis()));

							// deviceListService.resetDevicesStatus(deviceid);
							List<DeviceList> deviceListArray = deviceListService
									.listHostIdByDevice(deviceid);
							DeviceList deviceListForHost = new DeviceList();
							for (int i = 0; i < deviceListArray.size(); i++) {
								deviceListForHost = deviceListArray.get(i);
							}
							Integer hostId = deviceListForHost.getHostList()
									.getHostId();

							hostListService.resetHostsStatus(hostId);

							testRunConfigurationService
									.addTestRunConfigurationChild(testRunConfigurationChild);

							TestRunSelectedDeviceList testRunSelectedDeviceList = new TestRunSelectedDeviceList();
							testRunSelectedDeviceList.setDeviceList(deviceList);
							testRunSelectedDeviceList
									.setTestRunConfigurationChild(testRunConfigurationChild);

							testRunConfigurationService
									.addTestRunConfigurationChildDevice(testRunSelectedDeviceList);

							// Test Run

							// host list
							HostList hostList = deviceListForHost.getHostList();
							List<HostList> tmplist = hostListService
									.listByHostName(hostList.getHostName());
							if (tmplist != null && tmplist.size() != 0) {
								hostList.setHostId(tmplist.get(0).getHostId());
								hostListService.update(hostList);
							} else {
								hostListService.add(hostList);
							}

							HostHeartbeat hostHeartbeat = new HostHeartbeat(
									hostList.getHostId(),
									System.currentTimeMillis(), false,
									(short) -1);
							hostHeartBeatService.update(hostHeartbeat);
							log.info("host '" + hostList.getHostName()
									+ "' Connected");
							eventsService.raiseTerminalConnectedEvent(hostList,
									"Terminal connected to Server");

							JsonHostList tmpList = new JsonHostList(hostList);
							jTableSingleResponse = new JTableSingleResponse(
									"OK", tmpList);


							TestRunExecutionStatusVO statusVO = testExecutionService
									.executeTestRun(testRunConfigurationChild);

							jTableSingleResponse = new JTableSingleResponse(
									"OK", new JsonTestRunList(
											statusVO.testRunList));
							jTableSingleResponse = new JTableSingleResponse(
									"OK", new JsonTestSuiteList(testSuiteList));
						} else {
							jTableSingleResponse = new JTableSingleResponse(
									"ERROR", "Test Script is not available : "
											+ testSuitePath);
						}
					} else {
						jTableSingleResponse = new JTableSingleResponse(
								"ERROR", "Device is not available/connected in TAF : "
										+ deviceid);
					}
				} else {
					jTableSingleResponse = new JTableSingleResponse("ERROR",
							"Product Version is not available in TAF : "
									+ productVersionName);
				}
			} else {
				jTableSingleResponse = new JTableSingleResponse("ERROR",
						"Product is not available in TAF : " + productName);
			}

		} catch (Exception e) {
			jTableSingleResponse = new JTableSingleResponse("ERROR",
					"Error in Executing test scripts from Hudson!");
			log.error("JSON ERROR", e);
		}
		return jTableSingleResponse;
	}

	public boolean testScriptFileAvailablity(String testSuitePath) {
		URL url = null;
		ReadableByteChannel rbc = null;
		boolean fileAvailable = false;
		try {
			url = new URL(testSuitePath);
			rbc = Channels.newChannel(url.openStream());
			if (rbc != null) {
				fileAvailable = true;
			}
		} catch (Exception e) {
			//e.printStackTrace();
		}
		return fileAvailable;
	}

	public boolean isTestRunConfigurationParentExists(
			String testRunconfigurationName) {
		boolean bResult = false;
		try {
			bResult = testRunConfigurationService
					.isTestRunParentExistsByName(testRunconfigurationName);
		} catch (Exception exception) {
			log.error("JSON ERROR", exception);
		}
		return bResult;
	}

	class TestSuite {
		String testSuiteName;
		String runName;
		String runConfigName;
		String productName;
		String productVersionName;
		String devicePlatformName;
		String devicePlatformVersion;
		String runEnvironment;
		String testSuitePath;
		String toolName;
		String deviceid;

		public String getTestSuiteName() {
			return testSuiteName;
		}

		public void setTestSuiteName(String testSuiteName) {
			this.testSuiteName = testSuiteName;
		}

		public String getProductName() {
			return productName;
		}

		public void setProductName(String productName) {
			this.productName = productName;
		}

		public String getProductVersionName() {
			return productVersionName;
		}

		public void setProductVersionName(String productVersionName) {
			this.productVersionName = productVersionName;
		}

		public String getDevicePlatformName() {
			return devicePlatformName;
		}

		public void setDevicePlatformName(String devicePlatformName) {
			this.devicePlatformName = devicePlatformName;
		}

		public String getDevicePlatformVersion() {
			return devicePlatformVersion;
		}

		public void setDevicePlatformVersion(String devicePlatformVersion) {
			this.devicePlatformVersion = devicePlatformVersion;
		}

		public String getRunEnvironment() {
			return runEnvironment;
		}

		public void setRunEnvironment(String runEnvironment) {
			this.runEnvironment = runEnvironment;
		}

	
		public String getRunName() {
			return runName;
		}

		public void setRunName(String runName) {
			this.runName = runName;
		}

		public String getRunConfigName() {
			return runConfigName;
		}

		public void setRunConfigName(String runConfigName) {
			this.runConfigName = runConfigName;
		}

		public String getTestSuitePath() {
			return testSuitePath;
		}

		public void setTestSuitePath(String testSuitePath) {
			this.testSuitePath = testSuitePath;
		}

		public String getToolName() {
			return toolName;
		}

		public void setToolName(String toolName) {
			this.toolName = toolName;
		}

		public String getDeviceid() {
			return deviceid;
		}

		public void setDeviceid(String deviceid) {
			this.deviceid = deviceid;
		}

	}
	
	@RequestMapping(value = "/testRunPlanExecution", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody
	JTableSingleResponse testRunPlanExecutionForRest(@RequestParam Integer testRunPlanId) {
		JTableSingleResponse jTableSingleResponse = null;
		try {
			TestRunPlan testRunPlan = productListService.getTestRunPlanBytestRunPlanId(testRunPlanId);			
			WorkPackage newWorkpackage=new WorkPackage();
			String name=testRunPlan.getProductVersionListMaster().getProductMaster().getProductName()+"-"+testRunPlan.getProductVersionListMaster().getProductVersionName()+"-"+DateUtility.getCurrentTime();
			
			newWorkpackage.setName(name);
			newWorkpackage.setDescription(name +" created.");
			
			newWorkpackage.setCreateDate(DateUtility.getCurrentTime());
			newWorkpackage.setModifiedDate(DateUtility.getCurrentTime());
			newWorkpackage.setStatus(1);
			newWorkpackage.setIsActive(1);
			if(testRunPlan.getExecutionType().getExecutionTypeId()==3)
			{
				ExecutionTypeMaster executionTypeMaster=executionTypeMasterService.getExecutionTypeByExecutionTypeId(7);
				newWorkpackage.setWorkPackageType(executionTypeMaster);
			}else{
				ExecutionTypeMaster executionTypeMaster=executionTypeMasterService.getExecutionTypeByExecutionTypeId(8);
				newWorkpackage.setWorkPackageType(executionTypeMaster);
			}
			
			ProductBuild productBuild=productBuildDAO.getLatestProductBuild(testRunPlan.getProductVersionListMaster().getProductMaster().getProductId());
			newWorkpackage.setProductBuild(productBuild);
			
			newWorkpackage.setTestRunPlan(testRunPlan);
			newWorkpackage.setTestRunPlanGroup(null);
			newWorkpackage.setSourceType(IDPAConstants.WORKPACKAGE_SOURCE_TESTRUNPLAN_CI_REST);
			
			WorkFlowEvent workFlowEvent = new WorkFlowEvent();
			workFlowEvent.setEventDate(DateUtility.getCurrentTime());
			workFlowEvent.setRemarks("New Workapckage Added :"+newWorkpackage.getName());
			workFlowEvent.setUser(null);
			workFlowEvent.setWorkFlow(workPackageService.getWorkFlowByEntityIdStageId(IDPAConstants.WORKPACKAGE_ENTITY_ID,IDPAConstants.WORKFLOW_STAGE_ID_NEW));
			
			workPackageService.addWorkFlowEvent(workFlowEvent);
			newWorkpackage.setWorkFlowEvent(workFlowEvent);
			newWorkpackage.setPlannedEndDate(DateUtility.getCurrentTime());
			newWorkpackage.setPlannedStartDate(DateUtility.getCurrentTime());
			UserList userList= userListService.getUserByLoginId(IDPAConstants.USER_FOR_TESTRUN_PLAN);
			newWorkpackage.setUserList(userList);
			newWorkpackage.setIterationNumber(-1);
			LifeCyclePhase lifeCyclePhase = new LifeCyclePhase();
			lifeCyclePhase.setLifeCyclePhaseId(4);
			newWorkpackage.setLifeCyclePhase(lifeCyclePhase);
			newWorkpackage.setWorkPackageId(0);
			if(productBuild!=null){
				workPackageService.addWorkPackage(newWorkpackage);
				if(newWorkpackage!=null && newWorkpackage.getWorkPackageId()!=null){
					mongoDBService.addWorkPackage(newWorkpackage.getWorkPackageId());
				}
				workPackageService.workpackageExxecutionPlan(newWorkpackage,testRunPlan,null);
			}else{
				log.info("Unable to execute Test Run Plan. This could be because the Product Build specified is not active");
			}

		}
		catch(Exception e){
			log.error("Problem while executing Test Run Plan Through REST call ", e);
		}
		return jTableSingleResponse;
		
	}

	@RequestMapping(value = "/testRunPlanExecutionByDeviceByTestcase", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody JTableSingleResponse testRunPlanExecutionForRest(@RequestParam Integer testRunPlanId,@RequestParam String deviceNames,@RequestParam String testcaseNames) {

		JTableSingleResponse jTableSingleResponse = null;
		try {
			TestRunPlan testRunPlan = productListService.getTestRunPlanBytestRunPlanId(testRunPlanId);			
			WorkPackage newWorkpackage=new WorkPackage();
			String name=testRunPlan.getProductVersionListMaster().getProductMaster().getProductName()+"-"+testRunPlan.getProductVersionListMaster().getProductVersionName()+"-"+DateUtility.getCurrentTime();
			
			newWorkpackage.setName(name);
			newWorkpackage.setDescription(name +" created.");
			
			newWorkpackage.setCreateDate(DateUtility.getCurrentTime());
			newWorkpackage.setModifiedDate(DateUtility.getCurrentTime());
			newWorkpackage.setStatus(1);
			newWorkpackage.setIsActive(1);
			if(testRunPlan.getExecutionType().getExecutionTypeId()==3)
			{
				ExecutionTypeMaster executionTypeMaster=executionTypeMasterService.getExecutionTypeByExecutionTypeId(7);
				newWorkpackage.setWorkPackageType(executionTypeMaster);
			}else{
				ExecutionTypeMaster executionTypeMaster=executionTypeMasterService.getExecutionTypeByExecutionTypeId(8);
				newWorkpackage.setWorkPackageType(executionTypeMaster);
			}
			
			ProductBuild productBuild=productBuildDAO.getLatestProductBuild(testRunPlan.getProductVersionListMaster().getProductMaster().getProductId());
			newWorkpackage.setProductBuild(productBuild);
			
			newWorkpackage.setTestRunPlan(testRunPlan);
			newWorkpackage.setTestRunPlanGroup(null);
			newWorkpackage.setSourceType(IDPAConstants.WORKPACKAGE_SOURCE_TESTRUNPLAN_CI_REST);
			
			WorkFlowEvent workFlowEvent = new WorkFlowEvent();
			workFlowEvent.setEventDate(DateUtility.getCurrentTime());
			workFlowEvent.setRemarks("New Workapckage Added :"+newWorkpackage.getName());
			workFlowEvent.setUser(null);
			workFlowEvent.setWorkFlow(workPackageService.getWorkFlowByEntityIdStageId(IDPAConstants.WORKPACKAGE_ENTITY_ID,IDPAConstants.WORKFLOW_STAGE_ID_NEW));
			
			workPackageService.addWorkFlowEvent(workFlowEvent);
			newWorkpackage.setWorkFlowEvent(workFlowEvent);
			newWorkpackage.setPlannedEndDate(DateUtility.getCurrentTime());
			newWorkpackage.setPlannedStartDate(DateUtility.getCurrentTime());
			UserList userList= userListService.getUserByLoginId(IDPAConstants.USER_FOR_TESTRUN_PLAN);
			newWorkpackage.setUserList(userList);
			newWorkpackage.setIterationNumber(-1);
			LifeCyclePhase lifeCyclePhase = new LifeCyclePhase();
			lifeCyclePhase.setLifeCyclePhaseId(4);
			newWorkpackage.setLifeCyclePhase(lifeCyclePhase);
			newWorkpackage.setWorkPackageId(0);
			if(productBuild!=null){
				workPackageService.addWorkPackage(newWorkpackage);
				if(newWorkpackage!=null && newWorkpackage.getWorkPackageId()!=null){
					mongoDBService.addWorkPackage(newWorkpackage.getWorkPackageId());
				}
				workPackageService.workpackageExxecutionPlan(newWorkpackage,testRunPlan,null,deviceNames,testcaseNames);
			}else{
				log.info("Unable to execute Test Run Plan. This could be because the Product Build specified is not active");
			}

		}
		catch(Exception e){
			log.error("Problem while executing Test Run Plan Through REST call ", e);
		}
		return jTableSingleResponse;
	
	}
	
		
	@RequestMapping(value = "/verify.host.active",method=RequestMethod.GET ,produces="application/json")
	public @ResponseBody boolean isHostActive(@RequestParam String hostIP) {
		boolean isHostActive = false;
		try {			
			List<HostList> hostNameList = hostListService.listByHostIp(hostIP);		
			if(hostNameList != null && hostNameList.size() > 0){
				for(HostList hName : hostNameList){
					if(hName.getHostIpAddress().trim().equalsIgnoreCase(hostIP)){
						isHostActive = true;
						log.info("Check Host is active : "+isHostActive);
					}
				}
			}	
		} catch(Exception e){
			log.error("Problem while verifying host is actve or not ", e);
		}
		return isHostActive;	
	}


	@RequestMapping(value = "/updateTestSuiteScriptPack", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody
	JTableSingleResponse updateTestSuiteScriptPack(@RequestParam Integer testSuiteId, @RequestParam String testSuiteName,
			Integer productId, String testSuitePath, String testScriptPackSource) {
		
		return testSuiteConfigurationService.updateTestSuiteScriptPack(testSuiteId, testSuiteName, productId, testSuitePath, testScriptPackSource);
	}
	
	@RequestMapping(value = "/importTestSuiteScriptPack", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody JTableSingleResponse importTestScriptsPacksFromCI(@ModelAttribute(value="FORM1") UploadForm form , @RequestParam Integer testSuiteId, String testSuitePath) {
		JTableSingleResponse jTableSingleResponse;
		try{
			String path = CommonUtility.getCatalinaPath()+ File.separator+ "webapps"+request.getContextPath()+File.separator+"TestScripts"+File.separator+testSuiteId;
			Configuration.checkAndCreateDirectory(path);
        	FileOutputStream outputStream = null;      	
        	String originalFileName = "";
        	if (form.getFile() != null) {
        		originalFileName = form.getFile().getOriginalFilename();	
        	}        	
            String filePath = path + File.separator + originalFileName;
        	log.debug("filePath:"+filePath);
        	File file = new File(filePath);            	
            outputStream = new FileOutputStream(file);
            outputStream.write(form.getFile().getFileItem().get());
            outputStream.close();              
            jTableSingleResponse = new JTableSingleResponse("OK", file.getAbsolutePath() , "Imported Script Pack for Test Suite from CI");
   		} catch(Exception e){
   			jTableSingleResponse = new JTableSingleResponse("ERROR", "Unable to imported Script Pack for Test Suite from CI");
		}
		return jTableSingleResponse;
	}

	/*
	 *  This method allows the addition of a new build to a product version of a product.
	 *  It can be used for adding new builds in a CI scenario, when a build is successfully created in a CI server
	 */
	@RequestMapping(value = "/addNewProductBuild", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody JTableSingleResponse addNewProductBuild(@RequestParam String productBuildName, @RequestParam String productBuildNo, @RequestParam String productBuildDescription, @RequestParam Integer buildTypeId, @RequestParam String productBuildDate, @RequestParam Integer productVersionId, @RequestParam String productVersionName, @RequestParam Integer productId, @RequestParam String productName) {

		JTableSingleResponse jTableSingleResponse;

		try{
			ProductMaster product = null;
			if(productId == null || productId < 0){
				if(productName == null || productName.trim().isEmpty()){
					jTableSingleResponse = new JTableSingleResponse("ERROR", productId , "Not able to add Product Build as Product Id and Product Name are missing or invalid");
					return jTableSingleResponse;
				} else {
					product = productListService.getProductByName(productName);
				}
			} else {
				product = productListService.getProductById(productId);
			}
			ProductVersionListMaster productVersion = null;
			if(productVersionId == null || productVersionId < 0){
				if(productVersionName == null || productVersionName.trim().isEmpty()){
					jTableSingleResponse = new JTableSingleResponse("ERROR", "", "Not able to add Product Build as Product Version Id / Name is missing or invalid");
					return jTableSingleResponse;
				} else {
					productVersion = productListService.getProductVersionListMasterByName(productVersionName);
				}
			} else {
				productVersion = productListService.getProductVersionListMasterById(productVersionId);
			}
			if (productVersion == null) {
				if (product == null) {
					jTableSingleResponse = new JTableSingleResponse("ERROR", "", "Not able to add Product Build as Product Version Id / Name is invalid");
					return jTableSingleResponse;
				} else { 
					productVersion = productListService.getLatestProductVersionListMaster(product.getProductId());
				}
			}
			if(productBuildName == null || productBuildName.trim().isEmpty() || productBuildNo == null || productBuildNo.trim().isEmpty()){
	            jTableSingleResponse = new JTableSingleResponse("ERROR", "", "Not able to add Product Build as Build Name or Build No are not available");
				return jTableSingleResponse;
			}
		
			Integer productBuildId = productListService.addProductBuildFromExternalSource(productBuildName, productBuildNo, productBuildDescription, buildTypeId, productBuildDate, productVersion, productVersion.getProductMaster());
            jTableSingleResponse = new JTableSingleResponse("OK", productBuildId, "Imported Script Pack for Test Suite from CI");

		} catch(Exception e){
   			jTableSingleResponse = new JTableSingleResponse("ERROR", "Unable to imported Script Pack for Test Suite from CI");
		}
		return jTableSingleResponse;
	}
	@RequestMapping(value = "/product.story.script", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody String downloadTestStoriesAndGeneratedTestScriptBundle(@QueryParam("productId") Integer productId) {
		log.info("Downloading Product Test Story and Script bundle to the Product");
		File sendGeneratedStoryScriptBundle = null;
		String bundleName = null;
		try{
			
			Map<String, String> queryMap = new HashMap<String, String>();
			queryMap.put("productId", String.valueOf(productId));
			bundleName = testCaseScriptGenerationService.downloadStoryScriptBundle(queryMap);
			if(bundleName == null || bundleName.isEmpty())
				return null;
			if(bundleName != null && !bundleName.isEmpty())
				sendGeneratedStoryScriptBundle = new File(bundleName); 
			
		} catch (Exception e){
			log.error("Error in downloading. Try again sometime due to "+e.getMessage());
		}
		
		return  bundleName;
	}

}

