package com.hcl.atf.taf.controller;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.StreamingOutput;

import org.apache.commons.collections.MultiHashMap;
import org.apache.commons.collections.MultiMap;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hcl.atf.taf.constants.IDPAConstants;
import com.hcl.atf.taf.constants.TAFConstants;
import com.hcl.atf.taf.controller.utilities.CommonUtility;
import com.hcl.atf.taf.controller.utilities.DateUtility;
import com.hcl.atf.taf.controller.utilities.RestResponseUtility;
import com.hcl.atf.taf.dao.ProductBuildDAO;
import com.hcl.atf.taf.dao.ProductMasterDAO;
import com.hcl.atf.taf.dao.ProductTeamResourcesDao;
import com.hcl.atf.taf.dao.WorkPackageDAO;
import com.hcl.atf.taf.integration.CustomTestSystemConnectorsManager;
import com.hcl.atf.taf.model.AuthenticationType;
import com.hcl.atf.taf.model.CommonActiveStatusMaster;
import com.hcl.atf.taf.model.Customer;
import com.hcl.atf.taf.model.EngagementTypeMaster;
import com.hcl.atf.taf.model.ExecutionTypeMaster;
import com.hcl.atf.taf.model.Languages;
import com.hcl.atf.taf.model.LifeCyclePhase;
import com.hcl.atf.taf.model.ProductBuild;
import com.hcl.atf.taf.model.ProductFeature;
import com.hcl.atf.taf.model.ProductFeatureProductBuildMapping;
import com.hcl.atf.taf.model.ProductMaster;
import com.hcl.atf.taf.model.ProductMode;
import com.hcl.atf.taf.model.ProductTeamResources;
import com.hcl.atf.taf.model.ProductType;
import com.hcl.atf.taf.model.ProductUserRole;
import com.hcl.atf.taf.model.ProductVersionListMaster;
import com.hcl.atf.taf.model.RunConfiguration;
import com.hcl.atf.taf.model.TestCaseList;
import com.hcl.atf.taf.model.TestCasePriority;
import com.hcl.atf.taf.model.TestFactory;
import com.hcl.atf.taf.model.TestRunJob;
import com.hcl.atf.taf.model.TestRunPlan;
import com.hcl.atf.taf.model.TestRunPlanTSHasTC;
import com.hcl.atf.taf.model.TestSuiteList;
import com.hcl.atf.taf.model.TestcaseTypeMaster;
import com.hcl.atf.taf.model.TestfactoryResourcePool;
import com.hcl.atf.taf.model.UserList;
import com.hcl.atf.taf.model.UserRoleMaster;
import com.hcl.atf.taf.model.UserTypeMasterNew;
import com.hcl.atf.taf.model.VendorMaster;
import com.hcl.atf.taf.model.WorkFlow;
import com.hcl.atf.taf.model.WorkFlowEvent;
import com.hcl.atf.taf.model.WorkPackage;
import com.hcl.atf.taf.model.dto.ScriptLessExecutionDTO;
import com.hcl.atf.taf.model.dto.TestCaseDTO;
import com.hcl.atf.taf.model.dto.VerificationResult;
import com.hcl.atf.taf.model.json.JsonEngagementTypeMaster;
import com.hcl.atf.taf.model.json.JsonProductBuild;
import com.hcl.atf.taf.model.json.JsonProductFeature;
import com.hcl.atf.taf.model.json.JsonProductMaster;
import com.hcl.atf.taf.model.json.JsonProductType;
import com.hcl.atf.taf.model.json.JsonTestCaseList;
import com.hcl.atf.taf.model.json.JsonTestCasePriority;
import com.hcl.atf.taf.model.json.JsonTestFactory;
import com.hcl.atf.taf.model.json.JsonTestRunPlan;
import com.hcl.atf.taf.model.json.JsonTestStoryGeneratedScripts;
import com.hcl.atf.taf.model.json.JsonTestcaseTypeMaster;
import com.hcl.atf.taf.model.json.JsonUserList;
import com.hcl.atf.taf.model.json.JsonWorkFlow;
import com.hcl.atf.taf.model.json.JsonWorkPackageTestCaseExecutionPlanForTester;
import com.hcl.atf.taf.model.json.JsonWorkPackageTestCaseExecutionResultSummary;
import com.hcl.atf.taf.model.json.JsonWorkPackageTestCaseExecutionSummary;
import com.hcl.atf.taf.model.json.jtable.JTableResponse;
import com.hcl.atf.taf.model.json.jtable.JTableSingleResponse;
import com.hcl.atf.taf.model.json.terminal.JsonTestSuiteList;
import com.hcl.atf.taf.mongodb.service.MongoDBService;
import com.hcl.atf.taf.service.ATLASNodeService;
import com.hcl.atf.taf.service.ActivityService;
import com.hcl.atf.taf.service.ActivityTaskService;
import com.hcl.atf.taf.service.ActivityTypeService;
import com.hcl.atf.taf.service.ActivityWorkPackageService;
import com.hcl.atf.taf.service.CommonService;
import com.hcl.atf.taf.service.CommonTestManagementService;
import com.hcl.atf.taf.service.CustomerService;
import com.hcl.atf.taf.service.DataTreeService;
import com.hcl.atf.taf.service.EmailService;
import com.hcl.atf.taf.service.EventsService;
import com.hcl.atf.taf.service.ExecutionTypeMasterService;
import com.hcl.atf.taf.service.NotificationService;
import com.hcl.atf.taf.service.PasswordEncryptionService;
import com.hcl.atf.taf.service.ProductListService;
import com.hcl.atf.taf.service.TestCaseScriptGenerationService;
import com.hcl.atf.taf.service.TestCaseService;
import com.hcl.atf.taf.service.TestFactoryManagementService;
import com.hcl.atf.taf.service.TestReportService;
import com.hcl.atf.taf.service.TestSuiteConfigurationService;
import com.hcl.atf.taf.service.UserListService;
import com.hcl.atf.taf.service.WorkPackageService;
import com.hcl.ilcm.workflow.dao.WorkflowActivityDAO;
import com.hcl.ilcm.workflow.model.WorkflowStatus;
import com.hcl.ilcm.workflow.service.ConfigurationWorkFlowService;
import com.hcl.ilcm.workflow.service.WorkflowEventService;

@SuppressWarnings({ "unchecked", "unused" })
@Path("/atlas/taf")
public class ATLASNodeController {

	private static final Log log = LogFactory.getLog(ATLASNodeController.class);
	
	@Autowired
	private ProductListService productListService;
	@Autowired
	private WorkPackageService workPackageService;
	@Autowired
	private ExecutionTypeMasterService executionTypeMasterService;
	@Autowired
	private ProductBuildDAO productBuildDAO;
	@Autowired
	private UserListService userListService;
	@Autowired
	private DataTreeService dataTreeService;
	@Autowired
	private CommonService commonService;
	@Autowired
	private EventsService eventsService;
	@Autowired
	private TestCaseService testCaseService;
	@Autowired
	private TestSuiteConfigurationService testSuiteConfigurationService;
	@Autowired
	private TestCaseScriptGenerationService testCaseScriptGenerationService;
	@Autowired
	private TestReportService testReportService;	
	@Autowired
	private ATLASNodeService atlasService;
	@Autowired
	private HttpServletRequest request;
	@Autowired
	private TestFactoryManagementService testFactoryManagementService;
	@Autowired
	private CustomerService customerService;
	@Autowired
	private ActivityTaskService activityTaskService;
	@Autowired
	private ActivityWorkPackageService activityWorkPackageService;
	@Autowired
	private ActivityTypeService activityTypeService;
	@Autowired
	private NotificationService notificationService;
	@Autowired
	private WorkPackageDAO workPackageDAO;
	
	@Autowired
	private ProductMasterDAO productMasterDAO;
	
	
	@Autowired
	private ProductTeamResourcesDao productTeamResourcesDao;
	
	@Autowired
	private PasswordEncryptionService passwordEncryptionService;
	
	@Autowired
	private MongoDBService mongoDBService;
	
	@Autowired
	private CommonTestManagementService commonTestManagementService;
	
	@Autowired
	private CustomTestSystemConnectorsManager customTestSystemConnectorsManager;
	
	@Autowired
	private EmailService emailService;
	
	@Autowired
	private ActivityService activityService;
	
	@Autowired
	private WorkflowEventService workflowEventService;
	
	@Autowired
	private ConfigurationWorkFlowService configurationWorkFlowService;
	
	@Autowired
	private WorkflowActivityDAO workflowActivityDAO;
	
	
	@Value("#{ilcmProps['USER_AUTHENTICATION_REQUIRED']}")
    private String atlasUserAuthenticationRequired;
	
	
	/**
	 * Purpose : New JAX-RS based REST Service to initiate Test Plan execution. Created for Node-Red platform assist.
	 * Author : HCL
	 * Date : 18-04-2018
	 * Request : JSON Object with required and optional request or query parameters
	 * Response : JSON String / JSON Object to successor nodes on Node-Red platform
	 */

	@POST
	@Path("/testExecution/executetestplan")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response executeTestPlan(String nodeRedTafJSON) {		
		log.info("Node-Red JSON String : "+nodeRedTafJSON);
		SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);		
		Integer testPlanId = null;
		String deviceNames = null;
		String testcaseNames = null;
		TestRunPlan testRunPlan = null;
		Integer productBuildId = null;
		ProductBuild productBuild = null;
		JSONObject workpackageJsonObj = new JSONObject();
		try {
			JSONParser parser = new JSONParser();
			org.json.simple.JSONObject jsonFormatObject = (org.json.simple.JSONObject) parser.parse(nodeRedTafJSON);
			
			if(jsonFormatObject == null || jsonFormatObject.isEmpty()){
				return RestResponseUtility.prepareErrorResponseWithoutData("No inputs provided", "No inputs provided");
			}
			
			//Authenticate the user
			String userName=jsonFormatObject.get("user") != null ? jsonFormatObject.get("user").toString():"";
			Response authResponse = authenticateUser(userName, "/testExecution/executetestplan");
			if (authResponse != null)
				return authResponse;

			if((String) jsonFormatObject.get("testPlanId") != null){
				try {
					testPlanId = Integer.valueOf((String) jsonFormatObject.get("testPlanId"));
				} catch (Exception er) {
					testPlanId = -1;
				}
				log.info("Test Plan Id : " +testPlanId);
				testRunPlan = productListService.getTestRunPlanBytestRunPlanId(testPlanId);	
			}
			if(testRunPlan == null){
				return RestResponseUtility.prepareErrorResponseWithoutData("Testplan ID is missing or invalid", "Testplan ID is missing or invalid");
			}
			
			//Check authorization for user
			authResponse = checkUserAuthorization(testRunPlan.getProductVersionListMaster().getProductMaster().getProductId(), userName, "/testExecution/executetestplan");
			if (authResponse != null)
				return authResponse;
			
			deviceNames = (String) jsonFormatObject.get("deviceNames");
			testcaseNames = (String) jsonFormatObject.get("testcases");
			if((String) jsonFormatObject.get("productBuildId") != null){
				try {
					productBuildId = Integer.valueOf((String) jsonFormatObject.get("productBuildId"));
				} catch (Exception er) {
					productBuildId = -1;
				}
				log.info("Product Build Id : " +productBuildId);
				productBuild = productListService.getProductBuildById(productBuildId,0);
				if(productBuild == null){
					return RestResponseUtility.prepareErrorResponseWithoutData("ProductBuild ID is missing or invalid", "ProductBuild ID is missing or invalid");
				}
					
			}
			
			//Check Test Plan readiness check and create the workpkacge if the status is Yes / Ready for execution
			VerificationResult testPlanReadinessCheck = productListService.testPlanReadinessCheck(testPlanId);
			if(testPlanReadinessCheck != null && testPlanReadinessCheck.getIsReady() != null && testPlanReadinessCheck.getIsReady().equalsIgnoreCase("No")){
				return RestResponseUtility.prepareResponse("ERROR", "400", "Test Plan execution not initiated as it is not ready for execution", testPlanReadinessCheck.getVerificationMessage(), "");
			}
			
			
				
			WorkPackage newWorkpackage = new WorkPackage();
			String name = testRunPlan.getProductVersionListMaster().getProductMaster().getProductName()+"-"+testRunPlan.getProductVersionListMaster().getProductVersionName()+"-"+DateUtility.getCurrentTime();

			newWorkpackage.setName(name);
			newWorkpackage.setDescription(name +" created.");
			newWorkpackage.setActualStartDate(DateUtility.getCurrentTime());
			newWorkpackage.setCreateDate(DateUtility.getCurrentTime());
			newWorkpackage.setModifiedDate(DateUtility.getCurrentTime());
			newWorkpackage.setStatus(1);
			newWorkpackage.setIsActive(1);
			
			if(testRunPlan.getExecutionType().getExecutionTypeId() == IDPAConstants.EXECUTION_TYPE_AUTO_CODE) {
				ExecutionTypeMaster executionTypeMaster = executionTypeMasterService.getExecutionTypeByExecutionTypeId(7);
				newWorkpackage.setWorkPackageType(executionTypeMaster);
			} else if (testRunPlan.getExecutionType().getExecutionTypeId() == IDPAConstants.EXECUTION_TYPE_MANUAL_CODE) {
				ExecutionTypeMaster executionTypeMaster = executionTypeMasterService.getExecutionTypeByExecutionTypeId(8);
				newWorkpackage.setWorkPackageType(executionTypeMaster);
			}

			if (productBuildId != null) {
				productBuild = productBuildDAO.getByProductBuildId(productBuildId, 0);
			} else {
				productBuild = testRunPlan.getProductBuild(); 
			}
			
			newWorkpackage.setProductBuild(productBuild);
			newWorkpackage.setTestRunPlan(testRunPlan);
			newWorkpackage.setTestRunPlanGroup(null);
			newWorkpackage.setSourceType(IDPAConstants.WORKPACKAGE_SOURCE_TESTRUNPLAN_CI_REST);

			WorkFlowEvent workFlowEvent = new WorkFlowEvent();
			workFlowEvent.setEventDate(DateUtility.getCurrentTime());
			workFlowEvent.setRemarks("New Workpackage Added :"+newWorkpackage.getName());
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
			newWorkpackage.setTestExecutionMode(testRunPlan.getAutomationMode());
			if(productBuild != null){
				workPackageService.addWorkPackage(newWorkpackage);
				if(newWorkpackage != null) {
					workPackageService.workpackageExxecutionPlan(newWorkpackage,testRunPlan,null,deviceNames,testcaseNames);
					log.info("Test Plan execution initiated.");
				}
			} else {
				log.info("Unable to execute Test Plan. This could be because the Product Build specified is not active");
			}
			workpackageJsonObj.put("TP Execution Initiated", "Yes");
			workpackageJsonObj.put("Workpackage ID", newWorkpackage.getWorkPackageId());
			workpackageJsonObj.put("Workpackage Name", newWorkpackage.getName());
			workpackageJsonObj.put("Test Plan Id", testRunPlan.getTestRunPlanId());
			workpackageJsonObj.put("Test Plan Name", testRunPlan.getTestRunPlanName());
			workpackageJsonObj.put("Total testcases", newWorkpackage.getPlannedTestCasesCount());
			workpackageJsonObj.put("Average execution time", "TBD");
			workpackageJsonObj.put("Execution expected to be completed by", "TBD");
			
			Integer totalJobs = newWorkpackage.getTestRunJobSet().size();
			workpackageJsonObj.put("Total Jobs", totalJobs);
			workpackageJsonObj.put("Job Nos", ScriptLessExecutionDTO.getJobIDs().substring(0, ScriptLessExecutionDTO.getJobIDs().length()-1));
			workpackageJsonObj.put("Status", "TBD");
			workpackageJsonObj.put("Result", "TBD");
			
			Set<TestRunJob> jobs = newWorkpackage.getTestRunJobSet();
			JSONObject jobJsonObject = null;
			JSONArray jobJsonObjectsArray = new JSONArray();
			for (TestRunJob job : jobs) {
				jobJsonObject = new JSONObject();
				jobJsonObject.put("Job ID", job.getTestRunJobId());
				jobJsonObject.put("Environment", job.getEnvironmentCombination().getEnvironmentCombinationName());
				jobJsonObject.put("Test System", job.getHostList().getHostName());
				jobJsonObject.put("Test System IP", job.getHostList().getHostIpAddress());
				if (!(job.getGenericDevices() == null)) {
					jobJsonObject.put("Test Device", job.getGenericDevices().getName());
					jobJsonObject.put("Type", job.getGenericDevices().getDeviceModelMaster().getDeviceType().getDeviceTypeName());
					jobJsonObject.put("Model", job.getGenericDevices().getDeviceModelMaster().getDeviceModel() + " : " + job.getGenericDevices().getDeviceModelMaster().getDeviceName());
					jobJsonObject.put("Resolution", job.getGenericDevices().getDeviceModelMaster().getDeviceResolution());
				} else {
					jobJsonObject.put("Test Device", "NA");
					jobJsonObject.put("Type", "NA");
					jobJsonObject.put("Model", "NA");
					jobJsonObject.put("Resolution", "NA");
				}
				jobJsonObject.put("Total testcases", job.getPlannedTestCasesCount());
				jobJsonObject.put("Average execution time", "TBD");
				jobJsonObject.put("Execution expected to be completed by", "TBD");
				jobJsonObject.put("Status", "TBD");
				jobJsonObject.put("Result", "TBD");
				jobJsonObject.put("Execution_Mode",  testRunPlan.getAutomationMode());
				jobJsonObjectsArray.put(jobJsonObject);
			}
			workpackageJsonObj.put("Jobs", jobJsonObjectsArray);
			return RestResponseUtility.prepareSuccessResponse("Test Execution Completed", workpackageJsonObj.toString());
		} catch(Exception e){
			try {
				return RestResponseUtility.prepareErrorResponse("Test Plan execution not initiated due to some issue." + System.lineSeparator() +  e, e.getStackTrace().toString(), workpackageJsonObj.toString());
			} catch (Exception e1) {
				log.error("Problem while executing Test Plan through REST call ", e1);
			}
			log.error("Problem while executing Test Plan through REST call ", e);
		}
		return Response.noContent().build();
	}	
	@POST
	@Path("/testExecution/query/modifyTestplanTestcases")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response modifyTestPlanTestcases(String nodeRedTafJSON) {		
		SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);	
		Integer testPlanId = null;
		Integer testSuiteId = null;
		String action = null;
		String testCaseIds = null;
		TestRunPlan testPlan = null;
		List<Integer> testCaseIdList = new ArrayList<Integer>();
		TestSuiteList testSuiteList = null;
		try{
			JSONParser parser = new JSONParser();
			org.json.simple.JSONObject jsonFormatObject = (org.json.simple.JSONObject) parser.parse(nodeRedTafJSON); 
			
			if(jsonFormatObject == null) {
				return RestResponseUtility.prepareErrorResponseWithoutData("No inputs provided", "No inputs provided");
			}
			
			String userName=jsonFormatObject.get("user") != null ? jsonFormatObject.get("user").toString():"";
			Response authResponse = authenticateUser(userName, "/testExecution/query/modifyTestplanTestcases");
			if (authResponse != null)
				return authResponse;
			
			if((String) jsonFormatObject.get("testPlanId") != null) {
				try {
					testPlanId = Integer.valueOf((String) jsonFormatObject.get("testPlanId"));
				} catch (Exception er) {
					testPlanId = -1;
				}
				testPlan = productListService.getTestRunPlanById(testPlanId);
			}
			if (testPlan == null) {
				return RestResponseUtility.prepareErrorResponseWithoutData("Testplan ID is missing or invalid", "Testplan ID is missing or invalid");
			}
			
			//Check authorization for user
			authResponse = checkUserAuthorization(testPlan.getProductBuild().getProductVersion().getProductMaster().getProductId(), userName, "/testExecution/query/modifyTestplanTestcases");
			if (authResponse != null)
				return authResponse;
			
			if((String) jsonFormatObject.get("testSuiteId") != null) {
				try {
					testSuiteId = Integer.valueOf((String) jsonFormatObject.get("testSuiteId"));
				} catch (Exception er) {
					testSuiteId = -1;
				}
				testSuiteList=testSuiteConfigurationService.getByTestSuiteId(Integer.valueOf((String) jsonFormatObject.get("testSuiteId")));
			}
			if (testSuiteList == null) {
				return RestResponseUtility.prepareErrorResponseWithoutData("Test Suite ID is missing or invalid", "Test Suite ID is missing or invalid");
			}
			testCaseIds = (String) jsonFormatObject.get("testCaseIds");
			try{
				for(String testCaseId : testCaseIds.split(",")){
					testCaseIdList.add(Integer.valueOf(testCaseId));
				}
			}catch(Exception e){
				return RestResponseUtility.prepareErrorResponseWithoutData("Invalid testcase Id", "Invalid testcase Id");
			}
			if(testCaseIdList == null || testCaseIdList.isEmpty()){
				return RestResponseUtility.prepareErrorResponseWithoutData("Testcase Ids are missing or invalid", "Testcase Ids are missing or invalid");
			}
			action = (String) jsonFormatObject.get("action");
			if (action == null || action.trim().isEmpty() || (!action.equalsIgnoreCase("Map") && !action.equalsIgnoreCase("Unmap"))) {
				return RestResponseUtility.prepareErrorResponseWithoutData("Mapping option specified is not a valid one. It has to be Map or Unmap", "Mapping option specified is not a valid one. It has to be Map or Unmap");
			}
			TestSuiteList ts = null;
			if(testSuiteId == null || testSuiteId == -1)
				ts = testPlan.getTestSuiteLists().parallelStream().findFirst().get();
			else 
				ts = testSuiteConfigurationService.getByTestSuiteId(testSuiteId);
			String message = "";
			if(action != null && action.equalsIgnoreCase("map")) {
				for(Integer tc : testCaseIdList) {
					TestCaseList tcase = testCaseService.getTestCaseById(tc);
					TestRunPlanTSHasTC testPlanTC = productListService.getTestPlanTestSuiteTestCase(testPlan.getTestRunPlanId(), ts.getTestSuiteId(),tcase.getTestCaseId());
					if(testPlanTC != null){
						return RestResponseUtility.prepareErrorResponseWithoutData("Test Case is already mapped", "Test Case is already mapped");
					}
					try{
						productListService.mapTestSuiteTestCasesTestRunPlan(testPlan.getTestRunPlanId(), ts.getTestSuiteId(),tcase.getTestCaseId(),"Add");
					} catch(Exception e) {
						log.error("Error while mapping Testcase to Test Suite");
					}
				}
				return RestResponseUtility.prepareSuccessResponse("Testcases mapped to the test plan", "Testcases mapped to the test plan"+testPlanId);
			} else if(action != null && action.equalsIgnoreCase("unmap")) {
				for(Integer tc : testCaseIdList) {
					TestCaseList tcase = testCaseService.getTestCaseById(tc);
					try{
						productListService.mapTestSuiteTestCasesTestRunPlan(testPlan.getTestRunPlanId(), ts.getTestSuiteId(),tcase.getTestCaseId(),"Remove");
					} catch(Exception e) {
						log.error("Error while unmapping Testcase to Test Suite");
					}
				}
				return RestResponseUtility.prepareSuccessResponse("Valid testcases unmapped from the test plan ", "Valid testcases unmapped from the test plan"+testPlanId);
			}			
		} catch(Exception e) {
			log.error(e);
			try {
				return RestResponseUtility.prepareErrorResponseWithoutData("Problem while mapping/unmapping testcases to test plan. " + e, "Problem while mapping/unmapping testcases to test plan. " + e);
			} catch (Exception e1) {
				log.error("Error in processing REST Service to the method ",e1);
			}
		}
		return Response.noContent().build();
	}
	
	@POST
	@Path("/testExecution/query/modifyTestplanTestConfigurations")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response modifyTestPlanTestConfigurations(String nodeRedTafJSON) {		
		SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);	
		Integer testPlanId = null;
		Integer testConfigId = null;
		String action = null;
		TestRunPlan testPlan = null;
		RunConfiguration testConfig = null;
		Integer productId=-1;
		try{
			JSONParser parser = new JSONParser();
			org.json.simple.JSONObject jsonFormatObject = (org.json.simple.JSONObject) parser.parse(nodeRedTafJSON);
			JSONObject obj = new JSONObject();
			if(jsonFormatObject == null) {
				return RestResponseUtility.prepareErrorResponseWithoutData("No inputs provided", "No inputs provided");
			}
			String userName=jsonFormatObject.get("user") != null ? jsonFormatObject.get("user").toString():"";
			Response authResponse = authenticateUser(userName, "/testExecution/query/modifyTestplanTestConfigurations");
			if (authResponse != null)
				return authResponse;
			if((String) jsonFormatObject.get("testPlanId") != null) {
				try {
					testPlanId = Integer.valueOf((String) jsonFormatObject.get("testPlanId"));
				} catch (Exception er) {
					testPlanId = -1;
				}
				testPlan = productListService.getTestRunPlanById(testPlanId);
				
			}
			if (testPlan == null) {
				return RestResponseUtility.prepareErrorResponseWithoutData("Testplan ID is missing or invalid", "Testplan ID is missing or invalid");
			}
			productId = testPlan.getProductBuild().getProductVersion().getProductMaster().getProductId();
			if((String)jsonFormatObject.get("testConfigId") != null) {
				try {
					testConfigId = Integer.valueOf((String)jsonFormatObject.get("testConfigId"));
				} catch (Exception er) {
					testConfigId = -1;
				}
				testConfig = productListService.getRunConfigurationById(testConfigId);
				
			}
			if (testConfig == null) {
				return RestResponseUtility.prepareErrorResponseWithoutData("Test configuration Id is missing or invalid", "Test configuration Id is missing or invalid");
				
			}
			productId = testConfig.getProductVersion().getProductMaster().getProductId();
			action = (String)jsonFormatObject.get("action");
			if (action == null || action.trim().isEmpty() || (!action.equalsIgnoreCase("Map") && !action.equalsIgnoreCase("UnMap"))) {
				return RestResponseUtility.prepareErrorResponseWithoutData("action is missing or invalid. It has to be Map or UnMap", "action is missing or invalid. It has to be Map or UnMap");
			}
			
			//Check authorization for user
			authResponse = checkUserAuthorization(productId, userName, "/testExecution/query/modifyTestplanTestConfigurations");
			if (authResponse != null)
				return authResponse;
			action = action.equalsIgnoreCase("Map") ? "Add" : "Remove";
			productListService.mapTestRunPlanWithTestRunconfiguration(testPlanId, testConfigId, action);
			if(action.equalsIgnoreCase("Remove")){
				return RestResponseUtility.prepareSuccessResponse("Test configuration successfully unmapped", "Test configuration successfully unmapped to test plan : "+testPlanId);
			} else {
				return RestResponseUtility.prepareSuccessResponse("Test configuration successfully mapped", "Test configuration successfully mapped to test plan : "+testPlanId);
			}
		} catch(Exception e) {
			log.error(e);
			try {
				return RestResponseUtility.prepareErrorResponseWithoutData("Problem while mapping/unmapping test configuration to testplan. " + e, "Problem while mapping/unmapping test configuration to testplan. " + e);
			} catch (Exception e1) {
				log.error(e1);
			}
		}
		return Response.noContent().build();
	}
	
	@POST
	@Path("/testExecution/query/testPlans")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response getTestPlans(String nodeRedTafJSONQuery) {
		SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);	
		Integer testPlanId = null;
		Integer productId = -1;
		Integer productVersionId = null;
		Integer productBuildId = null;
		List<TestRunPlan> testPlans = new ArrayList<TestRunPlan>();;
		TestRunPlan testRunPlan = new TestRunPlan();
		String testPlanNames = null;
		try{
			JSONParser parser = new JSONParser();
			org.json.simple.JSONObject jsonFormatObject = (org.json.simple.JSONObject) parser.parse(nodeRedTafJSONQuery);
			JSONObject obj = new JSONObject();
			if(jsonFormatObject == null) {
				return RestResponseUtility.prepareErrorResponseWithoutData("No inputs provided", "No inputs provided");
			}
			
			//Authenticate the user
			String userName=jsonFormatObject.get("user") != null ? jsonFormatObject.get("user").toString():"";
			Response authResponse = authenticateUser(userName, "/testExecution/query/testPlans");
			if (authResponse != null)
				return authResponse;
			
			if((String) jsonFormatObject.get("testPlanId") != null) {
				try {
					testPlanId = Integer.valueOf((String) jsonFormatObject.get("testPlanId"));
				} catch (Exception er) {
					testPlanId = -1;
				}
				testRunPlan = productListService.getTestRunPlanById(testPlanId);
				if (testRunPlan == null) {
					return RestResponseUtility.prepareErrorResponseWithoutData("Testplan ID is invalid", "Testplan ID is invalid");
				}
				testPlans.add(testRunPlan);
			}
			if((String)jsonFormatObject.get("productId") != null){
				try {
					productId = Integer.valueOf((String)jsonFormatObject.get("productId"));
				} catch (Exception er) {
					productId = -1;
				}
				ProductMaster product = productListService.getProductById(productId);
				if (product == null) {
					return RestResponseUtility.prepareErrorResponseWithoutData("Product ID invalid", "Product ID invalid");
				}
				testPlans = productListService.listTestRunPlanByProductId(productId);
			} else if((String)jsonFormatObject.get("productVersionId") != null){
				try {
					productVersionId = Integer.valueOf((String)jsonFormatObject.get("productVersionId"));
				} catch (Exception er) {
					productVersionId = -1;
				}
				ProductVersionListMaster prodversion = productListService.getProductVersionListMasterById(productVersionId);
				if (prodversion == null) {
					return RestResponseUtility.prepareErrorResponseWithoutData("ProductVersion ID is invalid", "ProductVersion ID is invalid");
				}
				testPlans = productListService.listTestRunPlanByProductId(prodversion.getProductMaster().getProductId());
			} else if((String)jsonFormatObject.get("productBuildId") != null){
				try {
					productBuildId = Integer.valueOf((String)jsonFormatObject.get("productBuildId"));
				} catch (Exception er) {
					productBuildId = -1;
				}
				ProductBuild build = productListService.getProductBuildById(productBuildId,0);
				if (build == null) {
					return RestResponseUtility.prepareErrorResponseWithoutData("Product Build ID is invalid", "Product Build ID is invalid");
				}
				testPlans = productListService.listTestRunPlanByProductId(build.getProductMaster().getProductId());
			}
			
			if(testPlans != null && testPlans.size() >0) {
				productId=testPlans.get(0).getProductVersionListMaster().getProductMaster().getProductId();
				//Check authorization for user
				authResponse = checkUserAuthorization(productId, userName, "/testExecution/query/testPlans");
				if (authResponse != null)
					return authResponse;
				
				//Iterate all test plans and set it to the string as a response				
				for(TestRunPlan testPlan : testPlans) {
					obj.put(testPlan.getTestRunPlanId().toString() , testPlan.getTestRunPlanName());
				}
				testPlanNames = obj.toString();;					
				log.info("List of Test Plans : "+ testPlanNames);			
				return RestResponseUtility.prepareSuccessResponse("Test plan list is successful", testPlanNames);
			}
			else{
				log.info("No Test Plans found");			
				return RestResponseUtility.prepareSuccessResponse("No Test Plans found", "");
			}
			
		} catch(Exception e) {
			try {
				return RestResponseUtility.prepareErrorResponseWithoutData("Problem in fetching testplans. " + e, "Problem in fetching testplans. " + e);
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			return Response.noContent().build();
		}
	}
	
	@POST
	@Path("/testExecution/query/testPlanDetails")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response getTestPlanDetails(String nodeRedTafJSONQuery) {		
		SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);	
		Integer testPlanId = null;
		TestRunPlan testPlan = null;
		VerificationResult testPlanReadinessCheck = null;
		JsonTestRunPlan jsonTestPlan = null;
		ObjectMapper mapper = new ObjectMapper();
		Integer productId=-1;
		try{
			JSONParser parser = new JSONParser();
			org.json.simple.JSONObject jsonFormatObject = (org.json.simple.JSONObject) parser.parse(nodeRedTafJSONQuery);
			JSONObject obj = new JSONObject();
			if(jsonFormatObject == null) {
				return RestResponseUtility.prepareErrorResponseWithoutData("No inputs provided", "No inputs provided");
			}
			
			//Authenticate the user
			String userName=jsonFormatObject.get("user") != null ? jsonFormatObject.get("user").toString():"";
			Response authResponse = authenticateUser(userName, "/testExecution/query/testPlanDetails");
			if (authResponse != null)
				return authResponse;
			
			if((String) jsonFormatObject.get("testPlanId") != null) {
				try {
					testPlanId = Integer.valueOf((String) jsonFormatObject.get("testPlanId"));
				} catch (Exception er) {
					testPlanId = -1;
				}
				testPlan = productListService.getTestRunPlanById(testPlanId);
			}
			if (testPlan == null) {
				return RestResponseUtility.prepareErrorResponseWithoutData("Testplan ID is missing or invalid", "Testplan ID is missing or invalid");
			}
			
			
			
			if(testPlan != null) {
				productId = testPlan.getProductVersionListMaster().getProductMaster().getProductId();
				//Check authorization for user
				authResponse = checkUserAuthorization(productId, userName, "/testExecution/query/testPlanDetails");
				if (authResponse != null)
					return authResponse;
				jsonTestPlan = new JsonTestRunPlan(testPlan);
			}
			JTableResponse response  = new JTableResponse("OK", jsonTestPlan);
			log.info("Test Plan Details : "+ mapper.writeValueAsString(response));			
			return RestResponseUtility.prepareSuccessResponse("Listing test plans for "+testPlanId,  mapper.writeValueAsString(response));
			
			
		} catch(Exception e)  {		
			try{
				return RestResponseUtility.prepareErrorResponseWithoutData("Problem in listing testplandetails. " + e, "Problem in listing testplandetails. " + e);
			} catch(Exception e1) {
				e1.printStackTrace();
			}
			return Response.noContent().build();
		}
	}
	
	private JSONObject getRunConfigurationJson(RunConfiguration runConfiguration) {		
		JSONObject rcObj = new JSONObject();
		try {		
			rcObj.put("Id", runConfiguration.getRunconfigId());
			rcObj.put("Name", runConfiguration.getRunconfigName());			
			if (runConfiguration.getGenericDevice() != null) {
				rcObj.put("Description", runConfiguration.getGenericDevice().getDescription());
				rcObj.put("Device", runConfiguration.getGenericDevice().getGenericsDevicesId());
				rcObj.put("Device host", runConfiguration.getGenericDevice().getHostList());				
			}			
			rcObj.put("Host", runConfiguration.getHostList().getHostName());
			rcObj.put("Host IP", runConfiguration.getHostList().getHostIpAddress());
			rcObj.put("Product Type", runConfiguration.getProductType().getTypeName());
			rcObj.put("Test Environment Id", runConfiguration.getEnvironmentcombination().getEnvironment_combination_id());
			rcObj.put("Test Environment Name", runConfiguration.getEnvironmentcombination().getEnvironmentCombinationName());
		} catch (Exception e) {
			rcObj = null;
		}
		return rcObj;
	}

	@POST
	@Path("/testExecution/query/testPlanTestcases")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response getTestPlanTestcases(String nodeRedTafJSONQuery) {		
		SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);	
		Integer testPlanId = null;
		TestRunPlan testPlan = null;
		TestSuiteList tsl = null;
		JSONArray arr = new JSONArray();
		Integer productId=-1;
		try{
			JSONParser parser = new JSONParser();
			org.json.simple.JSONObject jsonFormatObject = (org.json.simple.JSONObject) parser.parse(nodeRedTafJSONQuery);
			JSONObject obj = null;
			if(jsonFormatObject == null) {
				return RestResponseUtility.prepareErrorResponseWithoutData("No inputs provided", "No inputs provided");
			}	
			
			//Authenticate the user
			String userName=jsonFormatObject.get("user") != null ? jsonFormatObject.get("user").toString():"";
			Response authResponse = authenticateUser(userName, "/testExecution/executetestplan");
			if (authResponse != null)
				return authResponse;
			
			if((String) jsonFormatObject.get("testPlanId") != null) {
				try {
					testPlanId = Integer.valueOf((String) jsonFormatObject.get("testPlanId"));
				} catch (Exception er) {
					testPlanId = -1;
				}
				testPlan = productListService.getTestRunPlanById(testPlanId);
			}
			if (testPlan == null) {
				return RestResponseUtility.prepareErrorResponseWithoutData("Testplan ID is missing or invalid", "Testplan ID is missing or invalid");
			}
			
			
			if(testPlan != null) {
				productId = testPlan.getProductVersionListMaster().getProductMaster().getProductId();
				//Check authorization for user
				authResponse = checkUserAuthorization(productId, userName, "/testExecution/query/testPlanTestcases");
				if (authResponse != null)
					return authResponse;
				
				if(testPlan.getTestSuiteLists() != null && !testPlan.getTestSuiteLists().isEmpty())
					tsl = testPlan.getTestSuiteLists().stream().findFirst().get();
				else{
					return RestResponseUtility.prepareErrorResponseWithoutData("TestPlan : "+testPlan.getTestRunPlanName()+" does not have test suite", "TestPlan : "+testPlan.getTestRunPlanName()+" does not have test suite");
				}
				Integer testSuiteId = testPlan.getTestSuiteLists().stream().findFirst().get().getTestSuiteId();
				List<TestCaseList> testcases = productListService.getTestSuiteTestCaseMapped(testPlan.getTestRunPlanId(), testSuiteId);
				if(testcases != null && !testcases.isEmpty()) {
					for(TestCaseList tc : testcases){
						obj = new JSONObject();
						String tcStatus = tc.getStatus() == 2 ? "INACTIVE" : "ACTIVE";
						obj.put("Testcase ID",tc.getTestCaseId());
						obj.put("Name",tc.getTestCaseName());
						obj.put("Status",tcStatus);
						arr.put(obj);
					}						
				}else{
					return RestResponseUtility.prepareErrorResponseWithoutData("TestPlan : "+testPlan.getTestRunPlanName()+" does not have test case", "TestPlan : "+testPlan.getTestRunPlanName()+" does not have test case");
				}
			}			
			log.info("Test Plan Testcases to the Test Plan : "+testPlanId + " : "+testPlan.getTestRunPlanName() + " is "+obj.toString());
			return RestResponseUtility.prepareSuccessResponseWithJSONArray("Listing Testcase Names to the Test Plan : "+testPlanId, arr);
		} catch(Exception e)  {			
			try {
				return RestResponseUtility.prepareErrorResponseWithoutData("Problem while listing Testcase Names to the Test Plan. " + e, "Problem while listing Testcase Names to the Test Plan. " + e);
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			return Response.noContent().build();
		}
	}

	@POST
	@Path("/testExecution/query/testConfigurationTestcases")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response getTestConfigurationTestcases(String nodeRedTafJSONQuery) {
		SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);	
		Integer testPlanId = null;
		String testPlanTestcases = null;
		JSONArray arr = new JSONArray();
		TestRunPlan testPlan = null;
		Integer productId=-1;
		try{
			JSONParser parser = new JSONParser();
			org.json.simple.JSONObject jsonFormatObject = (org.json.simple.JSONObject) parser.parse(nodeRedTafJSONQuery);
			if(jsonFormatObject == null) {
				return RestResponseUtility.prepareErrorResponseWithoutData("No inputs provided", "No inputs provided");
			}
			
			//Authenticate the user
			String userName=jsonFormatObject.get("user") != null ? jsonFormatObject.get("user").toString():"";
			Response authResponse = authenticateUser(userName, "/testExecution/query/testConfigurationTestcases");
			if (authResponse != null)
				return authResponse;
			
			if((String) jsonFormatObject.get("testPlanId") != null) {
				try {
					testPlanId = Integer.valueOf((String) jsonFormatObject.get("testPlanId"));
				} catch (Exception er) {
					testPlanId = -1;
				}
				testPlan = productListService.getTestRunPlanById(testPlanId);
			}
			
			if (testPlan == null) {
				return RestResponseUtility.prepareErrorResponseWithoutData("Testplan ID is missing or invalid", "Testplan ID is missing or invalid");
			}
			if(testPlan != null) {
				productId = testPlan.getProductVersionListMaster().getProductMaster().getProductId();
				//Check authorization for user
				authResponse = checkUserAuthorization(productId, userName, "/testExecution/query/testConfigurationTestcases");
				if (authResponse != null)
					return authResponse;
				Set<RunConfiguration> runConfigs = testPlan.getRunConfigurationList();
				Set<TestCaseList> tc = new HashSet<TestCaseList>();
				for(RunConfiguration tconfig : runConfigs){
					for(TestSuiteList ts : tconfig.getTestSuiteLists()){
						tc.addAll(productListService.getRunConfigTestSuiteTestCaseMapped(tconfig.getRunconfigId(), ts.getTestSuiteId()));
					}
				}
				
				for(TestCaseList tcase : tc) {
					JSONObject obj = new JSONObject();
					obj.put("Testcase ID",tcase.getTestCaseId());
					obj.put("Name",tcase.getTestCaseName());
					arr.put(obj);
				}
			}			
			log.info("Test Plan Testcases to the Test Configuration : "+testPlanId + " : "+testPlan.getTestRunPlanName() + " is "+testPlanTestcases);
			return RestResponseUtility.prepareSuccessResponseWithJSONArray("Listing Testcases mapped to the Test Configuration for testplan : "+testPlanId, arr);
		} catch(Exception e)  {			
			try {
				return RestResponseUtility.prepareErrorResponseWithoutData("Problem while listing Testcases mapped to the Test Configuration for testplan. " + e, "Problem while listing Testcases mapped to the Test Configuration for testplan. " + e);
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			return Response.noContent().build();
		}
	}

	@POST
	@Path("/testExecution/query/testPlanReadiness")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response isTestPlanReadyForExecution(String nodeRedTafJSONQuery) {
		SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);	
		Integer testPlanId = null;
		TestRunPlan testPlan = null;
		VerificationResult testPlanReadinessCheck = null;
		Integer productId=-1;
		try{
			JSONParser parser = new JSONParser();
			org.json.simple.JSONObject jsonFormatObject = (org.json.simple.JSONObject) parser.parse(nodeRedTafJSONQuery);
			JSONObject obj = new JSONObject();
			if(jsonFormatObject == null) {
				return RestResponseUtility.prepareErrorResponseWithoutData("No inputs provided", "No inputs provided");
			}
			
			//Authenticate the user
			String userName=jsonFormatObject.get("user") != null ? jsonFormatObject.get("user").toString():"";
			Response authResponse = authenticateUser(userName, "/testExecution/query/testPlanReadiness");
			if (authResponse != null)
				return authResponse;
			
			if((String) jsonFormatObject.get("testPlanId") != null) {
				try {
					testPlanId = Integer.valueOf((String) jsonFormatObject.get("testPlanId"));
				} catch (Exception er) {
					testPlanId = -1;
				}
				testPlan = productListService.getTestRunPlanById(testPlanId);
			}
			if (testPlan == null) {
				return RestResponseUtility.prepareErrorResponseWithoutData("Testplan ID is missing or invalid", "Testplan ID is missing or invalid");
			}

			if(testPlan != null) {
				productId = testPlan.getProductVersionListMaster().getProductMaster().getProductId();
				//Check authorization for user
				authResponse = checkUserAuthorization(productId, userName, "/testExecution/query/testPlanReadiness");
				if (authResponse != null)
					return authResponse;
				testPlanReadinessCheck = productListService.testPlanReadinessCheck(testPlanId);				
				obj.put("Test Plan Readiness Status",testPlanReadinessCheck.getIsReady());
				obj.put("Message",testPlanReadinessCheck.getVerificationMessage());
			}		 
			return RestResponseUtility.prepareSuccessResponseWithJSONObject("Test Plan readiness check for the testplan : "+testPlanId, obj);
			
		} catch(Exception e)  {			
			try {
				return RestResponseUtility.prepareErrorResponseWithoutData("Problem while checking testplan readiness" + e, "Problem while checking testplan readiness" + e);
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			return Response.noContent().build();
		}
	}

	@POST
	@Path("/testExecution/query/testDeviceReadiness")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response isTestDeviceReadyForExecution(String nodeRedTafJSONQuery) {
		SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);	
		Integer testPlanId = null;
		Integer testConfigId = null;
		TestRunPlan testPlan = null;
		RunConfiguration testConfiguration = null;
		VerificationResult deviceReadinessCheck = null;
		Integer productId=-1;
		JSONArray deviceReadinessArr = new JSONArray();
		try{
			JSONParser parser = new JSONParser();
			org.json.simple.JSONObject jsonFormatObject = (org.json.simple.JSONObject) parser.parse(nodeRedTafJSONQuery);
			JSONObject obj = new JSONObject();
			if(jsonFormatObject == null) {
				return RestResponseUtility.prepareErrorResponseWithoutData("No inputs provided", "No inputs provided");
			}
			
			//Authenticate the user
			String userName=jsonFormatObject.get("user") != null ? jsonFormatObject.get("user").toString():"";
			Response authResponse = authenticateUser(userName, "/testExecution/query/testDeviceReadiness");
			if (authResponse != null)
				return authResponse;
			
			if((String) jsonFormatObject.get("testPlanId") != null) {
				try {
					testPlanId = Integer.valueOf((String) jsonFormatObject.get("testPlanId"));
				} catch (Exception er) {
					testPlanId = -1;
				}
				testPlan = productListService.getTestRunPlanById(testPlanId);
				if (testPlan == null) {
					return RestResponseUtility.prepareErrorResponseWithoutData("Testplan ID is invalid", "Testplan ID is invalid");
				}
				productId = testPlan.getProductVersionListMaster().getProductMaster().getProductId();
			}
			if((String) jsonFormatObject.get("testConfigId") != null) {
				try {
					testConfigId = Integer.valueOf((String) jsonFormatObject.get("testConfigId"));
				} catch (Exception er) {
					testConfigId = -1;
				}
				testConfiguration = productListService.getRunConfigurationById(testConfigId);
				if (testConfiguration == null) {
					return RestResponseUtility.prepareErrorResponseWithoutData("Test Configuration ID is or invalid", "Test Configuration ID is or invalid");
				}
				productId = testConfiguration.getProduct().getProductId();
			}
			
			
			//Check authorization for user
			authResponse = checkUserAuthorization(productId, userName, "/testExecution/query/testDeviceReadiness");
			if (authResponse != null)
				return authResponse;
			
			if(testPlan != null) {
					for(RunConfiguration rc : testPlan.getRunConfigurationList()){
						deviceReadinessCheck = productListService.testConfigurationReadinessCheck(rc, testPlan, null);
						obj = new JSONObject();
						obj.put("Device Name", rc.getRunconfigName());
						obj.put("Readiness Status",deviceReadinessCheck.getIsReady());
						obj.put("Message",deviceReadinessCheck.getVerificationMessage());
						deviceReadinessArr.put(obj);
					}							
			}else if(testConfiguration != null){
				deviceReadinessCheck = productListService.testConfigurationReadinessCheck(testConfiguration, testPlan, null);
				obj = new JSONObject();
				obj.put("Device Name",testConfiguration.getRunconfigName());
				obj.put("Readiness Status",deviceReadinessCheck.getIsReady());
				obj.put("Message",deviceReadinessCheck.getVerificationMessage());
				deviceReadinessArr.put(obj);

			}else{
				return RestResponseUtility.prepareErrorResponseWithoutData("Please specify test plan id or test configuration id.", "Please specify test plan id or test configuration id.");
			}
			return RestResponseUtility.prepareSuccessResponseWithJSONArray("Device readiness check completed", deviceReadinessArr);
			
			
		} catch(Exception e)  {			
			try {
				return RestResponseUtility.prepareErrorResponseWithoutData( "Problem while checking device readiness" + e,  "Problem while checking device readiness" + e);
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			return Response.noContent().build();
		}
	}

	@POST
	@Path("/testExecution/query/workpackageExecutionStatus")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response getWorkpackageExecutionStatus(String nodeRedTafJSONQuery) {

		SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);	
		Integer wpId = null;
		WorkPackage wp = null;
		List<JsonWorkPackageTestCaseExecutionPlanForTester> jsonWPTCEP = new ArrayList<JsonWorkPackageTestCaseExecutionPlanForTester>();
		JSONArray arr = new JSONArray();
		ObjectMapper mapper = new ObjectMapper();
		Integer productId = -1;
		List<ProductMaster> products= new ArrayList<>();
		Integer testFactoryId = null;
		Integer productBuildId = null; 
		String filter = "MONTH";
	
		try{
			JSONParser parser = new JSONParser();
			org.json.simple.JSONObject jsonFormatObject = (org.json.simple.JSONObject) parser.parse(nodeRedTafJSONQuery);
			JSONObject obj = new JSONObject();
			if(jsonFormatObject == null) {
				return RestResponseUtility.prepareErrorResponseWithoutData("No inputs provided", "No inputs provided");
			}
			
			//Authenticate the user
			String userName=jsonFormatObject.get("user") != null ? jsonFormatObject.get("user").toString():"";
			Response authResponse = authenticateUser(userName, "/testExecution/query/workpackageExecutionStatus");
			if (authResponse != null)
				return authResponse;
			
			if((String) jsonFormatObject.get("workpackageId") != null){
				try {
					wpId = Integer.valueOf((String) jsonFormatObject.get("workpackageId"));
				} catch (Exception er) {
					wpId = -1;
				}
				wp = workPackageService.getWorkPackageById(wpId);
			}
			if (wp == null) {
				return RestResponseUtility.prepareErrorResponseWithoutData("Workpackage ID is missing or invalid", "Workpackage ID is missing or invalid");
			}
			
			if(wp != null) {
				if(wp.getProductBuild() != null && wp.getProductBuild().getProductVersion() != null && wp.getProductBuild().getProductVersion().getProductMaster() != null)
					productId = wp.getProductBuild().getProductVersion().getProductMaster().getProductId();
				
				if(wp.getProductBuild() != null) 
					productBuildId = wp.getProductBuild().getProductBuildId();
				
				WorkFlowEvent wpEvent = wp.getWorkFlowEvent();
				if(wpEvent != null) {
					//sb.append("Workpackage ID : " +wpId+" : Name : "+wp.getName() + " : Current execution status : "+ wpEvent.getRemarks());
					obj.put("wpId", wpId);
					obj.put("workPackageName", wp.getName());
					obj.put("wpStatus", wpEvent.getRemarks());
					//arr.put(obj);
				}						
			}
			
			//Check authorization for user
			authResponse = checkUserAuthorization(productId, userName, "/testExecution/query/workpackageExecutionStatus");
			if (authResponse != null)
				return authResponse;
			
			if(jsonFormatObject.get("testFactoryId") != null){
				if((String) jsonFormatObject.get("testFactoryId") != null)
					testFactoryId = Integer.valueOf((String) jsonFormatObject.get("testFactoryId"));					
			}
			
			if(jsonFormatObject.get("filter") != null){
				filter = (String) jsonFormatObject.get("filter");				
			}
						
			if(testFactoryId == null) {
				testFactoryId = -1;					
			}
			
			if(productBuildId == null)
				productBuildId = -1;
			
			if(testFactoryId != null && testFactoryId > 0 || productId != null && productId > 0 || productBuildId != null && productBuildId > 0){
				jsonWPTCEP = workPackageService.getWPTCExecutionSummaryByProdIdBuildIdWorkpackageId(testFactoryId,productId, productBuildId,wpId,-1, -1, filter);	
			}	
			
			
			if(productId ==-1)  {
				UserList userList = userListService.getUserByLoginId(userName);
				products= productMasterDAO.getProductDetailsByUserId(userList.getUserId());
				if(products == null || products.size() == 0) {
					return RestResponseUtility.prepareErrorResponseWithoutData("REST service : /testExecution/query/workpackageExecutionStatus Problem while authorizing user : " + userList.getLoginId(), "REST service : /testExecution/query/workpackageExecutionStatus Problem while authorizing user : " + userList.getLoginId());
				}
				for(ProductMaster product:products) {
					jsonWPTCEP.addAll(workPackageService.getWPTCExecutionSummaryByProdIdBuildIdWorkpackageId(product.getTestFactory().getTestFactoryId(),product.getProductId(), productBuildId,wpId,-1, -1, filter));	
				}
			}
			
			for(JsonWorkPackageTestCaseExecutionPlanForTester js : jsonWPTCEP) {
				if(js != null && js.getWorkPackageId().equals(wpId)) {
					List<JsonWorkPackageTestCaseExecutionPlanForTester> jj = new ArrayList<JsonWorkPackageTestCaseExecutionPlanForTester>();
					js.setNotExecuted(js.getTotalWPTestCase() - (js.getP2totalPass() + js.getP2totalFail() ));
					js.setP2totalNoRun(js.getTotalWPTestCase() - (js.getP2totalPass() + js.getP2totalFail()));
					String firstActualExecutionDate = "NA";
					if(wp.getCreateDate() != null && wp.getCreateDate().toString().contains(" ")){
						String createdDate = wp.getCreateDate().toString();
						firstActualExecutionDate = createdDate.split(" ")[0];
					}
					js.setFirstActualExecutionDate(firstActualExecutionDate);
					jj.add(js);
					JSONObject object = new JSONObject(mapper.writeValueAsString(js));
					for(String key : JSONObject.getNames(obj)){
						object.put(key, obj.get(key));
					}
					
					arr.put(object);
					break;
				}
			}
			
			List<JsonWorkPackageTestCaseExecutionSummary> jsonWPTC = atlasService.listTestJobsWorkpackageSummary(wpId, productBuildId);	
			JSONArray jobArr = new JSONArray();
			
			if(jsonWPTC != null && !jsonWPTC.isEmpty()) {
				for(JsonWorkPackageTestCaseExecutionSummary js : jsonWPTC) {					
					JSONObject object = new JSONObject(mapper.writeValueAsString(js));					
					jobArr.put(object);
				}
			} 
			
			log.info("Response : "+arr);
			return RestResponseUtility.prepareSuccessResponseWithRecords("Recieved WorkPackage execution status for the workpackage : "+wpId, "", jobArr, arr);
			
		} catch(Exception e)  {		
			log.error("Error in retrieving Workpackage Execution Status for the WP : "+ wp.getWorkPackageId() ,e);
			try {
				return RestResponseUtility.prepareErrorResponseWithoutData("Failed to get report", "Failed to get report");
				
			} catch (Exception e1) {
				log.error("Error in retrieving Workpackage Execution Status for the WP : "+ wp.getWorkPackageId(),e1);
			}
			return Response.noContent().build();
		}
	}
		
	/**
	 * Purpose : To fetch a list of product builds to a product
	 */
	
	@POST
	@Path("/testExecution/query/productBuilds")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response getProductBuilds(String nodeRedTafJSONQuery) {
		SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);	
		Integer productId = -1;
		ProductMaster prod = null;
		String filter = "WEEK";
		
		List<ProductBuild> productBuilds = new ArrayList<ProductBuild>();
		JSONArray arr = new JSONArray();
		ObjectMapper mapper = new ObjectMapper();
		List<ProductMaster> products=new ArrayList<ProductMaster>();
		List<JsonProductBuild> jsonProductBuildList= new ArrayList<JsonProductBuild>();
		try{
			org.json.simple.JSONObject jsonFormatObject = validateRequestQueryString(nodeRedTafJSONQuery);			
			if(jsonFormatObject == null) {
				return RestResponseUtility.prepareErrorResponseWithoutData("No inputs provided", "No inputs provided");
			}
			
			//Authenticate the user
			String userName=jsonFormatObject.get("user") != null ? jsonFormatObject.get("user").toString():"";
			Response authResponse = authenticateUser(userName, "/testExecution/query/productBuilds");
			if (authResponse != null)
				return authResponse;
			UserList userList = userListService.getUserByLoginId(userName);
			if(jsonFormatObject != null) {
				if((String)jsonFormatObject.get("productId") != null){
					try {
						productId = Integer.valueOf((String)jsonFormatObject.get("productId"));
					} catch (Exception er) {
						productId = -1;
					}
				}
				prod = productListService.getProductById(productId);
				if (prod == null) {
					return RestResponseUtility.prepareErrorResponseWithoutData("Product ID is missing or invalid", "Product ID is missing or invalid");
				}
				if(jsonFormatObject.get("filter") != null)
					filter = (String) jsonFormatObject.get("filter");
			}
			if(prod != null) {
				//Check authorization for user
				authResponse = checkUserAuthorization(prod.getProductId(), userName, "/testExecution/executetestplan");
				if (authResponse != null)
					return authResponse;
				products.add(prod);
			} else if(userList != null) {
				products= productMasterDAO.getProductDetailsByUserId(userList.getUserId());
				if(products == null || products.size() == 0) {
					return RestResponseUtility.prepareErrorResponseWithoutData("User not mapped for the given product", "User not mapped for the given product");
				}
				for(ProductMaster product:products) {
					productBuilds.addAll(productListService.listBuildsByProductId(product.getProductId())); 
				}
			}
			if(prod != null) {
				productBuilds = productListService.listBuildsByProductId(productId); 
			} else {
				if(userList != null) {
					jsonProductBuildList = dataTreeService.getProductBuildDetails(userList.getUserRoleMaster().getUserRoleId(),userList.getUserId(), filter);
				} else {
					jsonProductBuildList = dataTreeService.getProductBuildDetails(1,1, filter);
				}
				
				for(JsonProductBuild pb : jsonProductBuildList) {
					JSONObject obj = new JSONObject();
					obj.put("id", pb.getProductBuildId());
					obj.put("name", pb.getProductBuildName());
					obj.put("created_date", pb.getCreatedDate());
					obj.put("product_id", pb.getProductId());
					obj.put("product_name", pb.getProductName());
					ProductMaster product = null;
					if(products != null && products.size() >0) {
						for(ProductMaster productMaster :products){
							if(pb.getProductId() != null && productMaster.getProductId().equals(pb.getProductId())) {
								//Obtain product for the product build
								product = productListService.getProductById(pb.getProductId());						
							} else {
								log.info("User is not mapped to this product"+pb.getProductName());
								continue;
							}
								
							
							//Obtain product type for the product build
							if(product != null && product.getProductType() != null && product.getProductType().getTypeName() != null)
								obj.put("product_type", product.getProductType().getTypeName());
							else
								obj.put("product_type", "N/A");
							if(product != null && product.getProductName() != null)
								obj.put("product_name", product.getProductName());
							else
								obj.put("product_name", "N/A");
							Integer productBuildTestedCount = workPackageService.getProductBuildsTestedCount(pb.getProductBuildId());
							obj.put("build_tested_count", productBuildTestedCount);
							JSONObject object = new JSONObject(mapper.writeValueAsString(pb));
							for(String key : JSONObject.getNames(obj)){
								object.put(key, obj.get(key));
							}
							arr.put(object);					
						}
					} else {
						product = productListService.getProductById(pb.getProductId());	
						//Obtain product type for the product build
						if(product != null && product.getProductType() != null && product.getProductType().getTypeName() != null)
							obj.put("product_type", product.getProductType().getTypeName());
						else
							obj.put("product_type", "N/A");
						if(product != null && product.getProductName() != null)
							obj.put("product_name", product.getProductName());
						else
							obj.put("product_name", "N/A");
						Integer productBuildTestedCount = workPackageService.getProductBuildsTestedCount(pb.getProductBuildId());
						obj.put("build_tested_count", productBuildTestedCount);
						JSONObject object = new JSONObject(mapper.writeValueAsString(pb));
						for(String key : JSONObject.getNames(obj)){
							object.put(key, obj.get(key));
						}
						arr.put(object);
					}
				}
			}
			
			if(productBuilds != null && !productBuilds.isEmpty()) {	
				for(ProductBuild pb : productBuilds){					
					JSONObject obj = new JSONObject();
					obj.put("id", pb.getProductBuildId());
					obj.put("name", pb.getBuildname());
					obj.put("created_date", pb.getCreatedDate());
					obj.put("product_id", pb.getProductMaster().getProductId());
					obj.put("product_name", pb.getProductMaster().getProductName());
					ProductMaster product = null;
					if(pb.getProductMaster().getProductId() != null) {
						//Obtain product for the product build
						product = productListService.getProductById(pb.getProductMaster().getProductId());						
					}
					//Obtain product type for the product build
					if(product != null && product.getProductType() != null && product.getProductType().getTypeName() != null)
						obj.put("product_type", product.getProductType().getTypeName());
					else
						obj.put("product_type", "N/A");
					if(product != null && product.getProductName() != null)
						obj.put("product_name", product.getProductName());
					else
						obj.put("product_name", "N/A");
					Integer productBuildTestedCount = workPackageService.getProductBuildsTestedCount(pb.getProductBuildId());
					obj.put("build_tested_count", productBuildTestedCount);
					JSONObject object = new JSONObject(mapper.writeValueAsString(new JsonProductBuild(pb)));
					for(String key : JSONObject.getNames(obj)){
						object.put(key, obj.get(key));
					}
					arr.put(object);	
				}
			}				
			log.info("Product Builds : " +arr);
			return RestResponseUtility.prepareSuccessResponseWithJSONArray("Product Build listing success", arr);
		} catch(Exception e)  {			
			try {
				return RestResponseUtility.prepareErrorResponseWithoutData( "Error listing product builds!",  "Error listing product builds!");
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			return Response.noContent().build();
		}
	}	
	
	/**
	 * Purpose : To fetch a list of test plans builds to product builds
	 */
	
	@POST
	@Path("/testExecution/query/testPlansForProductBuilds")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response getTestPlansForProductBuilds(String nodeRedTafJSONQuery) {

		SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);	
		String productBuildIds = "";
		ProductBuild productBuild = null;
		JSONArray arr = new JSONArray();
		List<TestRunPlan> testPlans = new ArrayList<TestRunPlan>();		
		ObjectMapper mapper = new ObjectMapper();
		Integer productId=-1;
		try{
			JSONParser parser = new JSONParser();
			org.json.simple.JSONObject jsonFormatObject = (org.json.simple.JSONObject) parser.parse(nodeRedTafJSONQuery);
			if(jsonFormatObject == null || jsonFormatObject.isEmpty()) {
				return RestResponseUtility.prepareErrorResponseWithoutData("No inputs provided", "No inputs provided");
			}
			//Authenticate the user
			String userName=jsonFormatObject.get("user") != null ? jsonFormatObject.get("user").toString():"";
			Response authResponse = authenticateUser(userName, "/testExecution/query/testPlansForProductBuilds");
			if (authResponse != null)
				return authResponse;
			if(jsonFormatObject != null) {
				productBuildIds = (String) jsonFormatObject.get("productBuildId");
			}
			if(productBuildIds != null && !productBuildIds.isEmpty()) {
				String[] productBuildIdsArray = productBuildIds.split(",");
				productBuild = productListService.getProductBuildById(new Integer(productBuildIdsArray[0]), 0);
				/*testPlans = productListService.listTestRunPlanByProductId(productBuild.getProductMaster().getProductId());
				productId =productBuild.getProductVersion().getProductMaster().getProductId();*/
			}
			if(productBuild == null){
				return RestResponseUtility.prepareErrorResponseWithoutData("Product Build ID is missing or invalid", "Product Build ID is missing or invalid");
			}
			testPlans = productListService.getTestPlansByProductBuildIds(productBuild.getProductBuildId().toString());
			if(testPlans == null || testPlans.isEmpty()){
				return RestResponseUtility.prepareErrorResponseWithoutData("There are no test plans associated to the given build", "There are no test plans associated to the given build");
			}
			if(productBuild != null && productBuild.getProductMaster() != null)
				productId = productBuild.getProductMaster().getProductId();
			//Check authorization for user
			authResponse = checkUserAuthorization(productId, userName, "/testExecution/query/testPlansForProductBuilds");
			if (authResponse != null)
				return authResponse;
			
			if(testPlans != null && !testPlans.isEmpty()){
				for(TestRunPlan testPlan : testPlans) {
					if(testPlan.getTestRunPlanId() != null && testPlan.getTestRunPlanName() != null){
						JSONObject obj = new JSONObject();
						obj.put("testplan_id", testPlan.getTestRunPlanId());
						obj.put("testplan_name", testPlan.getTestRunPlanName());
						VerificationResult testPlanReadinessCheck = productListService.testPlanReadinessCheck(testPlan.getTestRunPlanId());				
						
						JsonTestRunPlan jsonTestRunPlan= new JsonTestRunPlan(testPlan);
						jsonTestRunPlan.setIsReady(testPlanReadinessCheck.getIsReady());
						jsonTestRunPlan.setMessage(testPlanReadinessCheck.getVerificationMessage());
						JSONObject object = new JSONObject(mapper.writeValueAsString(jsonTestRunPlan));
						for(String key : JSONObject.getNames(obj)){
							object.put(key, obj.get(key));
						}
						String avgExecutionTime = null;
						try{
							avgExecutionTime = workPackageService.getExecutionTimeForEachWPByTPId(testPlan.getTestRunPlanId());
						} catch(Exception e) {}
						if(avgExecutionTime == null)
							avgExecutionTime = "00:00:00";
						
						object.put("avg_exec_time", avgExecutionTime);						
						object.put("testplan_readiness_status",testPlanReadinessCheck.getIsReady());
						object.put("message",testPlanReadinessCheck.getVerificationMessage());
						
						//Set No. of Testcases, Total Test Configurations, Test Configuration details, No. of test config testcases
						int executionTCCount = 0;
						int tcCount = 0;
						Set<TestSuiteList> testSuiteList = new HashSet<TestSuiteList>();
						Set<TestCaseList> totalTestCaseList = new HashSet<TestCaseList>();
						List<TestCaseList> testCaseList = new ArrayList<TestCaseList>();
						//Iterate TestPlan mapped testsuites and get the testcases						
						/*if (testPlan.getTestSuiteLists().size() > 0) {
							for (TestSuiteList ts : testPlan.getTestSuiteLists()) {
								testCaseList = productListService.getTestSuiteTestCaseMapped(testPlan.getTestRunPlanId(), ts.getTestSuiteId());
								executionTCCount = executionTCCount + testCaseList.size();								
							}
						}*/
						testCaseList = new ArrayList<TestCaseList>();
						totalTestCaseList = new HashSet<TestCaseList>();
						object.put("executiontestcases", executionTCCount);						
						String runConfigName = "";
						JSONArray array = new JSONArray();
						if(testPlan.getRunConfigurationList() != null){
							Set<RunConfiguration> rcList = testPlan.getRunConfigurationList();
							if(rcList != null && !rcList.isEmpty()) {
								for(RunConfiguration rc : rcList){
									runConfigName += rc.getRunconfigName()+", ";
									JSONObject rcObj = new JSONObject();
									testSuiteList = rc.getTestSuiteLists();
									if (testSuiteList.size() > 0) {
										for (TestSuiteList ts : testSuiteList) {
											testCaseList = productListService.getRunConfigTestSuiteTestCaseMapped(rc.getRunconfigId(), ts.getTestSuiteId());
											executionTCCount = executionTCCount + testCaseList.size();
											for(TestCaseList tl : testCaseList)
												totalTestCaseList.add(tl);
										}
									}
									tcCount = 	totalTestCaseList.size();
									rcObj.put("env_combination", rc.getEnvironmentcombination().getEnvironmentCombinationName());
									rcObj.put("host_name", rc.getHostList().getHostName());
									rcObj.put("host_ip", rc.getHostList().getHostIpAddress());									
									rcObj.put("testcases", tcCount);
									rcObj.put("prod_type", rc.getProductType().getTypeName());
									
									if(rc.getGenericDevice() != null)
										rcObj.put("device", rc.getGenericDevice().getName());								
									array.put(rcObj);
									totalTestCaseList = new HashSet<TestCaseList>();
								}
								if(runConfigName.length() > 1)
									runConfigName = runConfigName.substring(0, runConfigName.trim().length()-1);
							}							
						}			
						if(runConfigName.isEmpty())
							runConfigName = "NA";
						object.put("testcasescount", executionTCCount+"["+tcCount+"]");		
						object.put("test_configuration", array);
						object.put("testConfigurationNames", runConfigName);
						arr.put(object);						
					}
				}
			}			
			log.info("Test Plans for the product builds : "+arr);
			return RestResponseUtility.prepareSuccessResponseWithJSONArray("test plan for product build listing successfull", arr);
		} catch(Exception e)  {			
			try {
				if (e.getMessage() == null){
					return RestResponseUtility.prepareErrorResponseWithoutData("Problem in listing Test Plans. Could be due to old or stale data. Please contact system administrator.", "Problem in listing Test Plans. Could be due to old or stale data. Please contact system administrator.");
				}else{ 
					return RestResponseUtility.prepareErrorResponseWithoutData("Problem in listing Test Plans. Could be due to old or stale data. Please contact system administrator.", e.toString());
				}
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			return Response.noContent().build();
		}
	}
	
	@POST
	@Path("/testExecution/query/workpackageTestReport")
	public Response getWorkpackageTestReport(String nodeRedTafJSONQuery) throws IOException{	

		InputStream in = null;
		try{
			SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);		
			
			Integer wpId = null;		
			JTableResponse jTableResponse = null;
			WorkPackage workPackage = null;
			JSONParser parser = new JSONParser();
			org.json.simple.JSONObject jsonFormatObject = (org.json.simple.JSONObject) parser.parse(nodeRedTafJSONQuery);
			Integer productId =-1;
			if(jsonFormatObject == null) {
				return RestResponseUtility.prepareErrorResponseWithoutData("No inputs provided", "No inputs provided");
			}
			
			//Authenticate the user
			String userName=jsonFormatObject.get("user") != null ? jsonFormatObject.get("user").toString():"";
			Response authResponse = authenticateUser(userName, "/testExecution/query/workpackageTestReport");
			if (authResponse != null)
				return authResponse;

			if((String) jsonFormatObject.get("workpackageId") != null){
				try{
					wpId = Integer.valueOf((String) jsonFormatObject.get("workpackageId"));
				}catch (Exception e){
					wpId = -1;
				}
				workPackage = workPackageService.getWorkPackageById(wpId);
				productId = workPackage.getProductBuild().getProductVersion().getProductMaster().getProductId();
			}
			//Check authorization for user
			authResponse = checkUserAuthorization(productId, userName, "/testExecution/query/workpackageTestReport");
			if (authResponse != null)
				return authResponse;
			
			//jTableResponse = testReportService.downloadReport(wpId, (String) jsonFormatObject.get("saveLocation") != null ? (String) jsonFormatObject.get("saveLocation") : null);
			//Constructing JSOn Object as a JSON response to the Node-Red nodes
			//in = new FileInputStream("C:\\Program Files\\Apache Software Foundation\\Tomcat 7.0\\webapps\\iLCM\\WPReport\\scriptless_selenium-WP-1222-Evidence\\scriptless_selenium-WP-1222-Evidence.html");
			/*responseJson.put("result", "OK");
			responseJson.put("status", "200");
			responseJson.put("data", jTableResponse);		*/				
			//log.info("Byte Array contents : "+org.apache.commons.io.IOUtils.toByteArray(in));
			
			//New logic
			
			StreamingOutput fileStream =  new StreamingOutput()
	        {
	            @Override
	            public void write(java.io.OutputStream output)
	            {
	                try {
	                    java.nio.file.Path path = Paths.get("C:\\Program Files\\Apache Software Foundation\\Tomcat 7.0\\webapps\\iLCM\\WPReport\\SmartTestWeb-WP--1332-Evidence.html");
	                    byte[] data = Files.readAllBytes(path);
	                    output.write(data);
	                    output.flush();
	                } catch (Exception e) {
	                    log.error("File Not Found !!");
	                }
	            }
	        };
	        return Response.ok(fileStream, MediaType.APPLICATION_OCTET_STREAM).header("content-disposition","attachment; filename = evidence.html")
	                .build();
	        
		} catch(Exception e)  {			
			try {
			} catch (Exception e1) {
				log.error("JSON ERROR " ,e1);
			}
			return Response.ok(in).build();
		}
	}
	
	@POST
	@Path("/testExecution/query/syncProductwithTMS")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response doSyncForProductWithTestManagementSystem(String nodeRedTafJSONQuery) {
	
		//Download the HTML test rport to the specified location and return the report location zip.
		
		return null;
	}
	
	@POST
	@Path("/testExecution/query/syncTestStoriesWithTMS")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response doSyncForProductTestStoriesWithSCMSystem(String nodeRedTafJSONQuery) {
	
		//Download the HTML test rport to the specified location and return the report location zip.
		
		return null;
	}

	@POST
	@Path("/testExecution/query/postWorkpackageDefects")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response reportDefectsForWorkpackageInDefectSystem(String nodeRedTafJSONQuery) {
		SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);		
		
		Integer wpId = null;
		String defectIds = null;
		JTableResponse jTableResponse = null;
		try{			
			JSONParser parser = new JSONParser();
			org.json.simple.JSONObject jsonFormatObject = (org.json.simple.JSONObject) parser.parse(nodeRedTafJSONQuery);
			if(jsonFormatObject == null) {
				return RestResponseUtility.prepareErrorResponseWithoutData("No inputs provided", "No inputs provided");
			}
			if((String) jsonFormatObject.get("workpackageId") != null)
				wpId = Integer.valueOf((String) jsonFormatObject.get("workpackageId"));			
			defectIds = (String) jsonFormatObject.get("defectIds");
			jTableResponse = atlasService.postWorkpackageDefects(wpId, defectIds, request);
			return RestResponseUtility.prepareSuccessResponse("", jTableResponse.toString());
		} catch(Exception e)  {			
			try {
				return RestResponseUtility.prepareErrorResponseWithoutData(e.toString(), e.toString());
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			return Response.noContent().build();
		}
		
	}

	@POST
	@Path("/testExecution/query/postWorkpackageTestResults")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response reportTestResultsForWorkpackageIntestManagementSystem(String nodeRedTafJSONQuery) {
		SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);		
		
		Integer wpId = null;
		String testId = null;
		JTableResponse jTableResponse = null;
		try{			
			JSONParser parser = new JSONParser();
			org.json.simple.JSONObject jsonFormatObject = (org.json.simple.JSONObject) parser.parse(nodeRedTafJSONQuery);
			if(jsonFormatObject == null) {
				return RestResponseUtility.prepareErrorResponseWithoutData("No inputs provided", "No inputs provided");
			}
			if((String) jsonFormatObject.get("workpackageId") != null)
				wpId = Integer.valueOf((String) jsonFormatObject.get("workpackageId"));			
			testId = (String) jsonFormatObject.get("testId");
			jTableResponse = atlasService.postWorkpackageTestResults(wpId, Integer.parseInt(testId), request);
			return RestResponseUtility.prepareSuccessResponse("", jTableResponse.toString());
		} catch(Exception e)  {			
			try {
				return RestResponseUtility.prepareErrorResponseWithoutData(e.toString(), e.toString());
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			return Response.noContent().build();
		}
	}
		
	@POST
	@Path("/testExecution/query/recentProductBuilds")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response getRecentProductBuilds(String nodeRedTafJSONQuery) {

		log.info(this.getClass().getName()+": recentProductBuilds : " + "enter");
		SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);	
		String filter = "WEEK";
		JSONArray arr = new JSONArray();
		JTableResponse jTableResponse = null;
		ObjectMapper mapper = new ObjectMapper();
		try{
			log.info(this.getClass().getName()+": recentProductBuilds : " + "Before parsing");
			JSONParser parser = new JSONParser();
			org.json.simple.JSONObject jsonFormatObject = (org.json.simple.JSONObject) parser.parse(nodeRedTafJSONQuery);	
			if(jsonFormatObject == null) {
				log.info("Could not parse request : /testExecution/query/productBuilds");
				log.info(this.getClass().getName()+": recentProductBuilds : " + "exit : No inputs provided");
				return RestResponseUtility.prepareErrorResponseWithoutData("No inputs provided", "No inputs provided");
			}
			//Authenticate the user
			String userName=jsonFormatObject.get("user") != null ? jsonFormatObject.get("user").toString():"";
			Response authResponse = authenticateUser(userName, "/testExecution/query/recentProductBuilds");
			if (authResponse != null)
				return authResponse;
			
			if(jsonFormatObject.get("filter") != null) {
				filter = (String) jsonFormatObject.get("filter");
			}
			if (filter == null || filter.isEmpty() || (!filter.equalsIgnoreCase("Day") && !filter.equalsIgnoreCase("Week") && !filter.equalsIgnoreCase("Month"))) {
				log.info(this.getClass().getName()+": recentProductBuilds : " + "exit : filter is missing or invalid");
				return RestResponseUtility.prepareErrorResponseWithoutData("filter is missing or invalid", "filter is missing or invalid");
			}
			
			UserList userList=userListService.getUserByLoginId(userName);
			
			List<JsonProductBuild> jsonProductBuildList= new ArrayList<JsonProductBuild>();
			if(userList != null) {
				jsonProductBuildList = dataTreeService.getProductBuildDetails(userList.getUserRoleMaster().getUserRoleId(), userList.getUserId(), filter);
			}else {
				jsonProductBuildList = dataTreeService.getProductBuildDetails(1, 1, filter);
			}
			for(JsonProductBuild pb : jsonProductBuildList) {
				JSONObject jsonpb = new JSONObject(mapper.writeValueAsString(pb));
				Integer productBuildTestedCount = workPackageService.getProductBuildsTestedCount(pb.getProductBuildId());
				jsonpb.put("build_tested_count", productBuildTestedCount);
				//Obtain product type for the product build
				ProductMaster product = productListService.getProductById(pb.getProductId());
				if(product != null && product.getProductType() != null && product.getProductType().getTypeName() != null)
					jsonpb.put("product_type", product.getProductType().getTypeName());
				else
					jsonpb.put("product_type", "N/A");
				if(userList !=null && userName != "" && product != null ){
					boolean isProductAuthorizedForUser = productListService.isProductUserRoleExits(product.getProductId(), userList.getUserId(), userList.getUserRoleMaster().getUserRoleId());
					if(isProductAuthorizedForUser) {
						arr.put(jsonpb);
					}
				} else {
					arr.put(jsonpb);
				}
			}
			
			log.info("Recent product builds : "+arr);
			log.info(this.getClass().getName()+": recentProductBuilds : " + "exit : returned response");
			return RestResponseUtility.prepareSuccessResponseWithJSONArray("Recent Product Build listing success.", arr);
		} catch(Exception e)  {			
			try {
				return RestResponseUtility.prepareErrorResponseWithoutData("Error Listing recent product builds", "Error Listing recent product builds");
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			log.info(this.getClass().getName()+": recentProductBuilds : " + "exit : Error Listing recent product build");
			return Response.noContent().build();
		}
		
	}
	
	@POST
	@Path("/testExecution/query/addNewProductBuild")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response addNewBuildForProduct(String nodeRedTafJSONQuery) {	
		SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);	
		Integer productId = -1;
		Integer productVersionId = null;
		String productBuildNo = null;
		String productBuildName = null;
		String productBuildDescription = null;
		String createdDate = null;
		JTableSingleResponse jTableSingleResponse = null;
		JSONArray arr = new JSONArray();
		ObjectMapper mapper = new ObjectMapper();
		try{
			
			JSONParser parser = new JSONParser();
			org.json.simple.JSONObject jsonFormatObject = (org.json.simple.JSONObject) parser.parse(nodeRedTafJSONQuery);			
			if(jsonFormatObject == null) {
				log.info("Could not parse request : /testExecution/query/productBuilds");
				return RestResponseUtility.prepareErrorResponseWithoutData("No inputs provided", "No inputs provided");
			}
			
			//Authenticate the user
			String userName=jsonFormatObject.get("user") != null ? jsonFormatObject.get("user").toString():"";
			Response authResponse = authenticateUser(userName, "/testExecution/query/addNewProductBuild");
			if (authResponse != null)
				return authResponse;
			
			if((String) jsonFormatObject.get("productId") != null){
				try {
					productId = Integer.valueOf((String) jsonFormatObject.get("productId"));
				} catch (Exception er) {
					productId = -1;
				}
			}
			if (productId == null || productId == -1) {
				return RestResponseUtility.prepareErrorResponseWithoutData("Product Id is missing or invalid", "Product Id is missing or invalid");
			}
			if((String) jsonFormatObject.get("productVersionId") != null){
				try {
					productVersionId = Integer.valueOf((String) jsonFormatObject.get("productVersionId"));
				} catch (Exception er) {
					productVersionId = -1;
				}
			}
			if (productVersionId == null || productVersionId == -1) {
				return RestResponseUtility.prepareErrorResponseWithoutData("Product Version Id is missing or invalid", "Product Version Id is missing or invalid");
			}
		
			//Check authorization for user
			authResponse = checkUserAuthorization(productId, userName, "/testExecution/query/addNewProductBuild");
			if (authResponse != null)
				return authResponse;
			
			if(jsonFormatObject.get("productBuildName") != null)
				productBuildName = (String) jsonFormatObject.get("productBuildName");
			else{
				productBuildName = productId + " - "+DateUtility.getCurrentTime();
			}
			if(jsonFormatObject.get("productBuildNo") != null)
				productBuildNo = (String) jsonFormatObject.get("productBuildNo");
			else
				productBuildNo = productId + " - "+DateUtility.getCurrentTime();
			if(jsonFormatObject.get("productBuildDesc") != null)
				productBuildDescription = (String) jsonFormatObject.get("productBuildDesc");
			else
				productBuildDescription = productId + " - "+DateUtility.getCurrentTime();
		
			if(productId != null && productId != -1) {
				ProductMaster product = productListService.getProductById(productId);
				if (product == null) {
					return RestResponseUtility.prepareErrorResponseWithoutData("Product Id is invalid", "Product Id is invalid");
				}
				if(productVersionId != null && productVersionId != -1) {
					ProductVersionListMaster productVersion = productListService.getProductVersionListMasterById(productVersionId);
					if (productVersion == null) {
						return RestResponseUtility.prepareErrorResponseWithoutData("Product Version Id is invalid", "Product Version Id is invalid");
					}
					if(productVersion != null && product != null && productVersion.getProductMaster().getProductId() != product.getProductId()){
						log.info("Invalid product and product version assoaciation : /testExecution/query/productBuilds");
						return RestResponseUtility.prepareErrorResponseWithoutData("Product Version : "+productVersionId +" is not associated to the Product : " +productId, "Product Version : "+productVersionId +" is not associated to the Product : " +productId);
					}
					// Construct Product Build object
					if(productVersion == null){
						productVersion = productListService.getLatestProductVersionListMaster(product.getProductId());
						if(productVersion ==null){
							productVersion = new ProductVersionListMaster();
							String errorMessage=commonService.duplicateName(productBuildName, "ProductVersionListMaster", "productVersionName", "Product Version", "productMaster.productId="+product.getProductId());
							if (errorMessage != null) {
								log.info("Invalid product version name : /testExecution/query/productBuilds");
								productBuildName = productBuildName + DateUtility.getCurrentTime();
							}
							productVersion.setProductVersionName(productBuildName);
							productVersion.setProductVersionDescription(productBuildName);
							ProductMaster productMaster = new ProductMaster();
							productMaster.setProductId(productId);		
							productVersion.setProductMaster(productMaster);
							productVersion.setCreatedDate(DateUtility.getCurrentTime());
							productVersion.setStatusChangeDate(DateUtility.getCurrentTime());
							productVersion.setStatus(1);	
							productVersion.setReleaseDate(DateUtility.getCurrentTime());
							productListService.addProductVersion(productVersion);
						}
					}
					ProductBuild productBuild = new ProductBuild();				
					productBuild.setBuildNo(productBuildNo);
					productBuild.setBuildname(productBuildName);
					productBuild.setBuildDescription(productBuildDescription);
					productBuild.setProductMaster(product);					
					productBuild.setProductVersion(productVersion);			
					productBuild.setCreatedDate(DateUtility.getCurrentTime());					
					productBuild.setModifiedDate(DateUtility.getCurrentTime());									
					productBuild.setStatus(1);
					productBuild.setBuildDate(DateUtility.getCurrentTime());			
					String errorMessage=commonService.duplicateName(productBuild.getBuildname(), "product_build", "buildname", "Product Build", "productId="+productId);
					if (errorMessage != null) {					
						return RestResponseUtility.prepareErrorResponseWithoutData(errorMessage, errorMessage);
					}
					Integer buildId = productListService.addProductBuild(productBuild,true);	
					productBuild.setProductBuildId(buildId);
					JSONObject object = new JSONObject(mapper.writeValueAsString(new JsonProductBuild(productBuild)));
					arr.put(object);
					return RestResponseUtility.prepareSuccessResponseWithJSONArray("Product Build added", arr);
				}
			}					
		} catch(Exception e ) {
				try {
				log.error(e);
				return RestResponseUtility.prepareErrorResponseWithoutData("Error adding Product Build", "Error adding Product Build");
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			return Response.noContent().build();
		}		
		return Response.noContent().build();
	}
	
	@POST
	@Path("/testExecution/query/addNewTestcase")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response addNewTestcaseForProduct(String testcasesJson) throws JSONException {

		SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);		
		JSONObject responseJson = new JSONObject();
		try{
			responseJson=commonTestManagementService.addTestcases(testcasesJson);
		} catch(Exception e ) {
			return RestResponseUtility.prepareErrorResponseWithoutData("Error adding TestCases", "Error adding TestCases");
		}			
		return Response.ok(responseJson.toString()).build();
	}
	
	//Start from here
	@POST
	@Path("/testExecution/query/mapTestcasesToTestsuite")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response mapTestcasesToTestsuite(String nodeRedTafJSONQuery) throws JSONException {	
		SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);		
		JSONObject responseJson = new JSONObject();
		try{
			responseJson=commonTestManagementService.mapTestcasesToTestsuite(nodeRedTafJSONQuery);
		} catch(Exception e ) {
			try {
				log.error(e);
				return RestResponseUtility.prepareErrorResponseWithoutData("Error mapping testcase!", "Error mapping testcase!");
			} catch (Exception e1) {
				log.error(e1);
			}
			return Response.noContent().build();
		}			
		return Response.ok(responseJson.toString()).build();
	}

	@POST
	@Path("/testExecution/query/addTestStoryForTestcase")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response addTestStoryForTestcase(String nodeRedTafJSONQuery) {	
		SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);		
	
		Integer testCaseId = null;
		String testEngine = null;
		JTableSingleResponse jTableSingleResponse = null;
		try{			
			JSONParser parser = new JSONParser();
			org.json.simple.JSONObject jsonFormatObject = (org.json.simple.JSONObject) parser.parse(nodeRedTafJSONQuery);
			
			if(jsonFormatObject == null) {
				return RestResponseUtility.prepareErrorResponseWithoutData("No inputs provided", "No inputs provided");
			}
			
			//Authenticate the user
			String userName=jsonFormatObject.get("user") != null ? jsonFormatObject.get("user").toString():"";
			Response authResponse = authenticateUser(userName, "/testExecution/query/addTestStoryForTestcase");
			if (authResponse != null)
				return authResponse;
			
			if((String) jsonFormatObject.get("testCaseId") != null)
				testCaseId = Integer.valueOf((String) jsonFormatObject.get("testCaseId"));			
			testEngine = (String) jsonFormatObject.get("testEngine");
			if (testCaseId == null) {
				return RestResponseUtility.prepareErrorResponseWithoutData("No inputs provided", "No inputs provided");
			}
			
			TestCaseList testCase = testCaseService.getTestCaseById(testCaseId);
			if(testCase == null) {
				return RestResponseUtility.prepareErrorResponseWithoutData("TestcaseId is invalid", "TestcaseId is invalid");
			}
			//Check authorization for user
			authResponse = checkUserAuthorization(testCase.getProductMaster().getProductId(), userName, "/testExecution/query/addTestStoryForTestcase");
			if (authResponse != null)
				return authResponse;
			
			String automationScript = (String) jsonFormatObject.get("script");
			String message = testCaseScriptGenerationService.saveTestCaseAutomationStory(testCaseId,testEngine, automationScript, "");
			jTableSingleResponse = message.startsWith("SUCCESS") ? new JTableSingleResponse("OK", "Story saved as new version") : new JTableSingleResponse("ERROR", message);
			return RestResponseUtility.prepareSuccessResponseWithJTableSingleResponse("Story addedd successfully!", jTableSingleResponse);
			
		} catch(Exception e ) {
			try {
				log.error(e);
				return RestResponseUtility.prepareErrorResponseWithoutData( "Testplan ID is missing or invalid",  "Testplan ID is missing or invalid");
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			return Response.noContent().build();
		}			
	}

	@POST
	@Path("/testExecution/query/getTestScriptForTestcase")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response getTestScriptForTestcase(String nodeRedTafJSONQuery) {

		SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);		
	
		Integer testCaseStoryId = null;
		String testEngine = null;
		String languageName = null;
		JTableResponse jTableResponse = null;
		ObjectMapper mapper = new ObjectMapper();
		try{			
			JSONParser parser = new JSONParser();
			org.json.simple.JSONObject jsonFormatObject = (org.json.simple.JSONObject) parser.parse(nodeRedTafJSONQuery);
			if(jsonFormatObject == null) {
				return RestResponseUtility.prepareErrorResponseWithoutData("No inputs provided", "No inputs provided");
			}
			//Authenticate the user
			String userName=jsonFormatObject.get("user") != null ? jsonFormatObject.get("user").toString():"";
			Response authResponse = authenticateUser(userName, "/testExecution/query/getTestScriptForTestcase");
			if (authResponse != null)
				return authResponse;
			
			if((String) jsonFormatObject.get("testCaseStoryId") != null)
				testCaseStoryId = Integer.valueOf((String) jsonFormatObject.get("testCaseStoryId"));			
			testEngine = (String) jsonFormatObject.get("testEngine");
			languageName = (String) jsonFormatObject.get("languageName");
			
			JsonTestStoryGeneratedScripts jsonTestStoryGeneratedScripts = null;		

			if(testCaseStoryId != null){
				jsonTestStoryGeneratedScripts = this.testCaseScriptGenerationService.getAutomationScript(testCaseStoryId,languageName,testEngine);
			}
			if(jsonTestStoryGeneratedScripts != null){
				
				jTableResponse = new JTableResponse("OK", jsonTestStoryGeneratedScripts);
			}else{
				jTableResponse = new JTableResponse("ERROR", "Script is not available for this story");
			}
			return RestResponseUtility.prepareSuccessResponse("", mapper.writeValueAsString(jTableResponse).toString());
			
		} catch(Exception e ) {
			try {
				log.error(e);
				return RestResponseUtility.prepareErrorResponseWithoutData("Testplan ID is missing or invalid","Testplan ID is missing or invalid");
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			return Response.noContent().build();
		}			
	}

	@POST
	@Path("/testExecution/query/getRecommendedTestcases")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response getISERecommendedTestcasesforTestPlan(String nodeRedTafJSONQuery) {

		SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);		
		
		Integer testPlanId = null;	
		TestRunPlan testPlan = null;
		JSONArray arr = new JSONArray();
		List<JsonTestCaseList> jsonIseRecommendationTestcases = new LinkedList<JsonTestCaseList>();
		ObjectMapper mapper = new ObjectMapper();
		try {	
			JSONParser parser = new JSONParser();
			org.json.simple.JSONObject jsonFormatObject = (org.json.simple.JSONObject) parser.parse(nodeRedTafJSONQuery);	
			if(jsonFormatObject == null) {
				log.info("Could not parse request : /testExecution/query/getRecommendedTestcases");
				return RestResponseUtility.prepareErrorResponseWithoutData("No inputs provided", "No inputs provided");
			}
			
			//Authenticate the user
			String userName=jsonFormatObject.get("user") != null ? jsonFormatObject.get("user").toString():"";
			Response authResponse = authenticateUser(userName, "/testExecution/query/getRecommendedTestcases");
			if (authResponse != null)
				return authResponse;
			
			if(jsonFormatObject.get("testPlanId") != null) {
				try{
					testPlanId = Integer.valueOf((String) jsonFormatObject.get("testPlanId"));
				}catch(Exception e){
					testPlanId = -1;
				}
				log.info("Test Plan Id : " +testPlanId);
				testPlan = productListService.getTestRunPlanBytestRunPlanId(testPlanId);
			}
			if (testPlan == null) {
				return RestResponseUtility.prepareErrorResponseWithoutData("Testplan ID is missing or invalid", "Testplan ID is missing or invalid");
			}
			
			
			//Check authorization for user
			authResponse = checkUserAuthorization(testPlan.getProductVersionListMaster().getProductMaster().getProductId(), userName, "/testExecution/query/getRecommendedTestcases");
			if (authResponse != null)
				return authResponse;
			Integer recommendedTCCount = Integer.valueOf(0);
			Integer totalTCCount = Integer.valueOf(0);
			//Fetch ISE Recommended Test Cases
			if(testPlan.getUseIntelligentTestPlan() != null && testPlan.getUseIntelligentTestPlan().equalsIgnoreCase("YES")) {
				jsonIseRecommendationTestcases = atlasService.getISERecommendedTestcases(testPlan);
				if(jsonIseRecommendationTestcases != null && !jsonIseRecommendationTestcases.isEmpty()) {
					for(JsonTestCaseList jtcl : jsonIseRecommendationTestcases) {
						if(jtcl.getRecommendedTestCaseCount() != null)
							recommendedTCCount = jtcl.getRecommendedTestCaseCount();
						if(jtcl.getTotalTestCaseCount() != null)
							totalTCCount = jtcl.getTotalTestCaseCount();
						//JSONObject object = new JSONObject(mapper.writeValueAsString(jtcl));
						//arr.put(object);
					}
				}
			}
			
			Integer totalTestCaseCount = 0;		
			/*Set<RunConfiguration> runConfigurations = testPlan.getRunConfigurationList();
			for(RunConfiguration rc : runConfigurations){
				for(TestSuiteList tsl : rc.getTestSuiteLists()){
					totalTestCaseCount =  productListService.getRunConfigTestSuiteTestCaseMapped(rc.getRunconfigId(), tsl.getTestSuiteId()).size();
				}
			}*/
			String executionTitle = null;
			if(jsonIseRecommendationTestcases != null && !jsonIseRecommendationTestcases.isEmpty())
				executionTitle = recommendedTCCount +" testcases recommended out of "+ totalTCCount+" for execution";
			else {
				//Obtain the testcase count from the product level 
				if(testPlan.getProductBuild() != null && testPlan.getProductBuild().getProductMaster() != null && testPlan.getProductBuild().getProductMaster().getProductId() != null) {
					log.info("No recommended test cases from ISE, obtain it from TAF");
					totalTestCaseCount = testCaseService.getTestCaseListByProductId(testPlan.getProductBuild().getProductMaster().getProductId(), null, null).size();
					log.info("Total Testcases from TAF is "+totalTestCaseCount);
				}
				executionTitle = "0 testcases recommended out of "+ totalTestCaseCount+" for execution";
			}
			//Retrieve test bed information for the test plan
			String testBedData = atlasService.listTestBeds(testPlanId);
			JSONObject object = new JSONObject();
			object.put("executionTitle", executionTitle);
			object.put("testBed", testBedData);
			arr.put(object);
			return RestResponseUtility.prepareSuccessResponseWithJSONArray(executionTitle, arr);
	    } catch (Exception e) {
            try {
            	return RestResponseUtility.prepareErrorResponseWithoutData("Error getting ISE testcases!", e.toString());
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			return Response.noContent().build();
	    }		        
	}	
	@POST
	@Path("/testExecution/query/workpackageTestJobsExecutionStatus")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response getWorkpackageTestJobsExecutionStatus(String nodeRedTafJSONQuery) {

		SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);	
	
		TestRunJob testJob = null;
		JSONObject object = new JSONObject();
		ObjectMapper mapper = new ObjectMapper();
		Integer jobId = -1; 
		try{
			JSONParser parser = new JSONParser();
			org.json.simple.JSONObject jsonFormatObject = (org.json.simple.JSONObject) parser.parse(nodeRedTafJSONQuery);
			
			if(jsonFormatObject == null) {
				log.info("Could not parse request : /testExecution/query/workpackageTestJobsExecutionStatus");
				return RestResponseUtility.prepareErrorResponseWithoutData("No inputs provided", "No inputs provided");
			}
			
			//Authenticate the user
			String userName=jsonFormatObject.get("user") != null ? jsonFormatObject.get("user").toString():"";
			Response authResponse = authenticateUser(userName, "/testExecution/query/workpackageTestJobsExecutionStatus");
			if (authResponse != null)
				return authResponse;
			
			if(jsonFormatObject.get("jobId") != null){
				try {
					jobId = Integer.valueOf((String) jsonFormatObject.get("jobId"));
				} catch (Exception er) {
					jobId = -1;
				}
			}
			
			testJob = workPackageService.getTestRunJobById(jobId);
			
			if (testJob == null) {
				return RestResponseUtility.prepareErrorResponseWithoutData("Job Id is missing or invalid", "Job Id is missing or invalid");
			}
			
			
			int productBuildId = -1;
			if(testJob.getTestRunPlan() != null && testJob.getTestRunPlan().getProductBuild() != null 
					&& testJob.getTestRunPlan().getProductBuild().getProductBuildId() != null)
				productBuildId = testJob.getTestRunPlan().getProductBuild().getProductBuildId();
			
			//Check authorization for user
			authResponse = checkUserAuthorization(testJob.getTestRunPlan().getProductBuild().getProductVersion().getProductMaster().getProductId(), userName, "/testExecution/query/workpackageTestJobsExecutionStatus");
			if (authResponse != null)
				return authResponse;
			List<JsonWorkPackageTestCaseExecutionSummary> jsonWPTC = atlasService.listTestJobsWorkpackageSummary(testJob.getWorkPackage().getWorkPackageId(), productBuildId);	
			if(jsonWPTC != null && !jsonWPTC.isEmpty()) {
				for(JsonWorkPackageTestCaseExecutionSummary js : jsonWPTC) {
					
					if(js.getTestRunJobId().equals(testJob.getTestRunJobId())) {
						object = new JSONObject(mapper.writeValueAsString(js.getCleanJson()));
						break;
					}
				}
			}			
			log.info("Response : "+object.toString());
			return RestResponseUtility.prepareSuccessResponseWithJSONObject("Found Test Job details.", object);
			
		} catch(Exception e)  {			
			try {
				return RestResponseUtility.prepareErrorResponseWithoutData( "Error in getting job details.",  "Error in getting job details.");
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			return Response.noContent().build();
		}
	}
	
	@POST
	@Path("/testExecution/query/recentWorkpackages")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response getRecentWorkpackages(String nodeRedTafJSONQuery) {

		SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
		
		WorkPackage wp = null;
		List<JsonWorkPackageTestCaseExecutionPlanForTester> jsonWPTCEP = new ArrayList<JsonWorkPackageTestCaseExecutionPlanForTester>();
		JSONArray arr = new JSONArray();
		ObjectMapper mapper = new ObjectMapper();
		String filter = "WEEK";
		Integer productId = -1;
		Integer productBuildId = -1;
		Integer wpId = -1;
		ProductBuild build = null;
		ProductMaster productMaster = null;
		try{
			JSONParser parser = new JSONParser();
			org.json.simple.JSONObject jsonFormatObject = (org.json.simple.JSONObject) parser.parse(nodeRedTafJSONQuery);
			if(jsonFormatObject == null ) {
				return RestResponseUtility.prepareErrorResponseWithoutData("No inputs provided", "No inputs provided");
			}
			//Authenticate the user
			String userName=jsonFormatObject.get("user") != null ? jsonFormatObject.get("user").toString():"";
			Response authResponse = authenticateUser(userName, "/testExecution/query/recentWorkpackages");
			if (authResponse != null)
				return authResponse;
			if(jsonFormatObject != null) {
				if((String) jsonFormatObject.get("filter") != null){
					filter = (String) jsonFormatObject.get("filter");
					if (filter == null || filter.isEmpty() || (!filter.equalsIgnoreCase("Day") && !filter.equalsIgnoreCase("Week") && !filter.equalsIgnoreCase("Month"))) {
						log.info(this.getClass().getName()+": recentProductBuilds : " + "exit : filter is missing or invalid");
						return RestResponseUtility.prepareErrorResponseWithoutData("filter is missing or invalid", "filter is missing or invalid");
					}
				}
				if(filter.equalsIgnoreCase("MONTH"))
					filter = "WEEK";						
				if((String)jsonFormatObject.get("productId") != null){
					try {
						productId = Integer.valueOf((String)jsonFormatObject.get("productId"));
					} catch (Exception er) {
						productId = -1;
					}
					productMaster = productListService.getProductById(productId);
					if (productMaster == null) {
						return RestResponseUtility.prepareErrorResponseWithoutData("Product ID is invalid", "Product ID is invalid");
					}
				}
				if((String) jsonFormatObject.get("workpackageId") != null){
					try {
						wpId = Integer.valueOf((String) jsonFormatObject.get("workpackageId"));
					} catch (Exception er) {
						wpId = -1;
					}
					wp = workPackageService.getWorkPackageById(wpId);
					if (wp == null) {
						return RestResponseUtility.prepareErrorResponseWithoutData("Workpackage ID is invalid", "Workpackage ID is invalid");
					}
				}
				if((String) jsonFormatObject.get("productBuildId") != null){
					try {
						productBuildId = Integer.valueOf((String) jsonFormatObject.get("productBuildId"));
					} catch (Exception er) {
						productBuildId = -1;
					}
					build = productListService.getProductBuildById(productBuildId, 0);
					if (build == null) {
						return RestResponseUtility.prepareErrorResponseWithoutData("Build Id is invalid", "Build Id is invalid");
					}
				}
			}
			
			jsonWPTCEP = workPackageService.getWPTCExecutionSummaryByProdIdBuildId(-1,productId, productBuildId, -1, -1, filter);	
			UserList userList = userListService.getUserByLoginId(userName);
			if(userList != null && productId == -1) {
				List<ProductMaster> products= productMasterDAO.getProductDetailsByUserId(userList.getUserId());
				if(products == null || products.isEmpty()) {
					return RestResponseUtility.prepareErrorResponseWithoutData( "User not authorised for the given product",  "User not authorised for the given product");
				}
				if(jsonWPTCEP != null && jsonWPTCEP.size() >0) {
					for(JsonWorkPackageTestCaseExecutionPlanForTester js : jsonWPTCEP) {
						for(ProductMaster product:products) {
							if(product.getProductId().equals(js.getProductId())) {
								JSONObject object = new JSONObject(mapper.writeValueAsString(js));					
								arr.put(object);				
							}
						}
					}
				}
			}else if(userList != null && productId != -1) {
				//Check authorization for user
				authResponse = checkUserAuthorization(productId, userName, "/testExecution/query/recentWorkpackages");
				if (authResponse != null)
					return authResponse;
				if(jsonWPTCEP != null && jsonWPTCEP.size() >0) {
					
					for(JsonWorkPackageTestCaseExecutionPlanForTester js : jsonWPTCEP) {
						if(js.getProductId().equals(productId)) {
							JSONObject object = new JSONObject(mapper.writeValueAsString(js));					
							arr.put(object);				
						}
					}
				}
			}else {
					
				for(JsonWorkPackageTestCaseExecutionPlanForTester js : jsonWPTCEP) {
					JSONObject object = new JSONObject(mapper.writeValueAsString(js));					
					arr.put(object);				
				}		
			}
			return RestResponseUtility.prepareSuccessResponseWithJSONArray("Recent Workpackages listing success", arr);
		} catch(Exception e)  {		
			log.error("Error in retrieving Workpackage Execution Status",e);
			try {
				return RestResponseUtility.prepareErrorResponseWithoutData( "Error in retrieving Workpackage Execution Status",  "Error in retrieving Workpackage Execution Status");
			} catch (Exception e1) {
				log.error("Error in retrieving Workpackage Execution Status",e1);
			}
			return Response.noContent().build();
		}
	}
	
	//Util methods
	private org.json.simple.JSONObject validateRequestQueryString(String nodeRedTafJSONQuery) {        
        if (nodeRedTafJSONQuery == null || nodeRedTafJSONQuery.trim().isEmpty()) {
              log.info("/testExecution/query/recentProductBuilds - getRecentProductBuilds : Request is empty");
              return null;
        }
        JSONParser parser = new JSONParser();
        org.json.simple.JSONObject jsonFormatObject = null;
        try {
              jsonFormatObject = (org.json.simple.JSONObject) parser.parse(nodeRedTafJSONQuery);  
  
              if (jsonFormatObject == null) {
                    log.info("Cound not parse request " + System.lineSeparator() + nodeRedTafJSONQuery);
                    return null;
              }
        } catch (Exception e) {
              log.error("Cound not parse request " + System.lineSeparator() + nodeRedTafJSONQuery, e);
        }
        return jsonFormatObject;
	}
  
	/*private boolean validateUser(String jwt) {
	  return true;
	}*/
	
	@POST
	@Path("/testExecution/query/featureForProductBuild")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response getProductBuildFeatures(String nodeRedTafJSONQuery) {

		SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);	
		Integer productBuildId = 0;
	
		JSONArray arr = new JSONArray();
		JSONArray fullFeaturesJsonArray = new JSONArray();
		List<ProductFeature> mappedFeaturesList = new ArrayList<ProductFeature>();		
		List<ProductFeature> fullFeaturesList = new ArrayList<ProductFeature>();		
		ObjectMapper mapper = new ObjectMapper();
		ProductBuild build = null;
		try{
			JSONParser parser = new JSONParser();
			org.json.simple.JSONObject jsonFormatObject = (org.json.simple.JSONObject) parser.parse(nodeRedTafJSONQuery);
			
			if(jsonFormatObject == null) {
				log.info("Could not parse request : /testExecution/query/productBuilds");
				return RestResponseUtility.prepareErrorResponseWithoutData("No inputs provided", "No inputs provided");
			}		
			//Authenticate the user
			String userName=jsonFormatObject.get("user") != null ? jsonFormatObject.get("user").toString():"";
			Response authResponse = authenticateUser(userName, "/testExecution/query/featureForProductBuild");
			if (authResponse != null)
				return authResponse;
			if((String) jsonFormatObject.get("productBuildId") != null){
				try {
					productBuildId = Integer.valueOf((String) jsonFormatObject.get("productBuildId"));
				} catch (Exception er) {
					productBuildId = -1;
				}
			}
			build = productListService.getProductBuildById(productBuildId, 0);
			if (build == null) {
				return RestResponseUtility.prepareErrorResponseWithoutData("Build Id is missing or invalid", "Build Id is missing or invalid");
			}
			ProductMaster product =null;
			if(build != null && build.getProductVersion().getProductMaster() != null){
				product = build.getProductVersion().getProductMaster();			
			}
			//Check authorization for user
			authResponse = checkUserAuthorization(product.getProductId(), userName, "/testExecution/query/featureForProductBuild");
			if (authResponse != null)
				return authResponse;
			//Get the list of features mapped to Product Build
			if(productBuildId != null)
				mappedFeaturesList = productListService.getFeatureListByProductIdAndVersionIdAndBuild(product.getProductId(),0,productBuildId,1, 0, 10000);
			if(mappedFeaturesList != null && !mappedFeaturesList.isEmpty()){
				for(ProductFeature feature : mappedFeaturesList) {
					if(feature.getProductFeatureId() != null && feature.getProductFeatureId() != null){
						JSONObject object = new JSONObject(mapper.writeValueAsString(new JsonProductFeature(feature)));						
						arr.put(object);						
					}
				}
			}			
			log.info("Features for the product builds are : "+arr);
			
			//Get the complete list of features for the product and indicate whether the feature is mapped to the build
			fullFeaturesList = productListService.getFeatureListByProductId(product.getProductId(), null, null, false);
			if(fullFeaturesList == null || fullFeaturesList.isEmpty()){
				return RestResponseUtility.prepareErrorResponseWithoutData("There are no features available", "There are no features available");
			}
			for (ProductFeature feature : fullFeaturesList) {
				JSONObject featureJson = new JSONObject(mapper.writeValueAsString(new JsonProductFeature(feature)));						
				if (mappedFeaturesList != null && mappedFeaturesList.contains(feature)) {
					featureJson.put("mappedToBuild", "Yes");
				} else {
					featureJson.put("mappedToBuild", "No");
				}
				fullFeaturesJsonArray.put(featureJson);
			}
			
			JSONObject fullData = new JSONObject();
			fullData.put("mappedFeatures", arr);
			fullData.put("allFeatures", fullFeaturesJsonArray);
			fullData.put("productBuildId", productBuildId);
			fullData.put("productId", product.getProductId());
			return RestResponseUtility.prepareSuccessResponseWithJSONObject("Found features to the specified product build", fullData);
		} catch(Exception e)  {			
			try {
				return RestResponseUtility.prepareErrorResponseWithoutData(e.toString(), e.toString());
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			return Response.noContent().build();
		}
	}
	
	@POST
	@Path("/testExecution/query/mapFeatureToProductBuild")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response mapFeatureToProductBuild(String nodeRedTafJSONQuery) throws JSONException {

		SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);	
		Integer productBuildId = 0;
		Integer featureId = 0;
		Integer isMapped = 0;
		String action = null;
	
		ProductFeature feature = null;		
		ProductMaster product =null;
		boolean isFeatureAlreadyMapped = false;
		List<ProductFeature> featuresList =new ArrayList<ProductFeature>();
		List<ProductFeatureProductBuildMapping> featureBuildList =new ArrayList<ProductFeatureProductBuildMapping>();
		ObjectMapper mapper = new ObjectMapper();
		try{
			JSONParser parser = new JSONParser();
			org.json.simple.JSONObject jsonFormatObject = (org.json.simple.JSONObject) parser.parse(nodeRedTafJSONQuery);
			if(jsonFormatObject == null) {
				log.info("Could not parse request : /testExecution/query/productBuilds");
				return RestResponseUtility.prepareErrorResponseWithoutData("No inputs provided", "No inputs provided");
			}	
			
			//Authenticate the user
			String userName=jsonFormatObject.get("user") != null ? jsonFormatObject.get("user").toString():"";
			Response authResponse = authenticateUser(userName, "/testExecution/query/mapFeatureToProductBuild");
			if (authResponse != null)
				return authResponse;
			
			if((String) jsonFormatObject.get("featureId") != null){
				try {
					featureId = Integer.valueOf((String) jsonFormatObject.get("featureId"));
				} catch (Exception er) {
					featureId = -1;
				}
			}
			feature = productListService.getByProductFeatureId(featureId);
			if (feature == null) {
				return RestResponseUtility.prepareErrorResponseWithoutData("feature Id is missing or invalid", "feature Id is missing or invalid");
			}
			if((String) jsonFormatObject.get("productBuildId") != null){
				try {
					productBuildId = Integer.valueOf((String) jsonFormatObject.get("productBuildId"));
				} catch (Exception er) {
					productBuildId = -1;
				}
			}
			ProductBuild build = productListService.getProductBuildById(productBuildId, 0);
			if (build == null) {
				return RestResponseUtility.prepareErrorResponseWithoutData("Build Id is missing or invalid","Build Id is missing or invalid");
			}
			if((String) jsonFormatObject.get("action") != null){
				try{
					action = (String) jsonFormatObject.get("action");
				}catch(Exception e){
					action = null;
				}
			}
			if (action == null || (!action.equalsIgnoreCase("map") && !action.equalsIgnoreCase("unmap"))) {
				return RestResponseUtility.prepareErrorResponseWithoutData("action is missing or invalid.Valid values are map/unmap","action is missing or invalid.Valid values are map/unmap");
			}
			if(build != null){
				product = build.getProductMaster();		
			}
			
			//Check authorization for user
			authResponse = checkUserAuthorization(product.getProductId(), userName, "/testExecution/query/mapFeatureToProductBuild");
			if (authResponse != null)
				return authResponse;
			
			featuresList = productListService.getFeatureListByProductId(product.getProductId(), 1, null, null);
			if(featuresList!= null && !featuresList.isEmpty() && !featuresList.contains(feature)){
				return RestResponseUtility.prepareErrorResponseWithoutData("No features available for the product","No features available for the product");
			}
			featureBuildList = productListService.getProductFeatureAndProductBuildMappingByVersionIdOrBuildId(product.getProductId(), -1, build.getProductBuildId());
			if(featureBuildList != null){
				for (ProductFeatureProductBuildMapping pf : featureBuildList){
					if(action.equalsIgnoreCase("map") && pf.getFeatureId().equals(feature.getProductFeatureId())){
						return RestResponseUtility.prepareErrorResponseWithoutData("Feature already mapped","Feature already mapped");
					}else if(action.equalsIgnoreCase("unmap") && pf.getFeatureId().equals(feature.getProductFeatureId())){
						isFeatureAlreadyMapped = true;
						break;
					}
				}
			}
			if(!isFeatureAlreadyMapped && action.equalsIgnoreCase("unmap")){
				return RestResponseUtility.prepareErrorResponseWithoutData("Feature is not associated to the given productBuild. Unmap failed","Feature is not associated to the given productBuild. Unmap failed");
			}
			
			if(!(build.getProductMaster().getProductId().equals(feature.getProductMaster().getProductId()))) {
				return RestResponseUtility.prepareErrorResponseWithoutData("BuildId : "+build.getProductBuildId() +" and FeatureId :"+feature.getProductFeatureId()+" is not associated to the same Product","BuildId : "+build.getProductBuildId() +" and FeatureId :"+feature.getProductFeatureId()+" is not associated to the same Product");
			}

			ProductFeatureProductBuildMapping productFeatureProductBuildMapping = new ProductFeatureProductBuildMapping();
			if(action.equalsIgnoreCase("map")) 
				productFeatureProductBuildMapping.setIsMapped(1);
			else
				productFeatureProductBuildMapping.setIsMapped(0);
			productFeatureProductBuildMapping.setBuildId(productBuildId);
			productFeatureProductBuildMapping.setFeatureId(featureId);
			productFeatureProductBuildMapping.setProduct(product);
			productFeatureProductBuildMapping.setCreatedDate(new Date());
			productFeatureProductBuildMapping.setModifiedDate(new Date());
			if(action.equalsIgnoreCase("map")) 
				productListService.mappingProductFeatureToProductBuild(productFeatureProductBuildMapping);
			else
				productListService.unMappingProductFeatureToProductBuild(productFeatureProductBuildMapping);
			log.info("Product Feature and Build mapping successfully");
			if(action.equalsIgnoreCase("map")) {
				return RestResponseUtility.prepareSuccessResponse("Feature mapped to Product Build", "");
			}else{
				return RestResponseUtility.prepareSuccessResponse("Feature unmapped to Product Build", "");
			}
		} catch(Exception e)  {			
			try {
				return RestResponseUtility.prepareErrorResponseWithoutData("Error mapping Product Feature and Product Build",  "Error mapping Product Feature and Product Build"+e);
			} catch (Exception e1) {
				log.error(e1);
			}
			return Response.noContent().build();
		}
	}
	
	@POST
	@Path("/testExecution/query/addProduct")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response addProduct(String nodeRedTafJSONQuery) {

		SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);	
		Integer testFactoryId = null;
		Integer customerId = null;
		String productName = "";
		String productDescription = "";
		Integer productTypeId = null;
		String productTypeName = null;
		Integer modeId = null;
		String projectCode = "";
		String projectName = "";
		Integer status = null;
		int userId;
		int roleId;
		JSONObject productDetailsJSON = new JSONObject();
	
		ProductType productType=null;
		//String ProductName =null;
		ProductMaster productMaster = new ProductMaster();
		ProductUserRole productUserRole=new ProductUserRole();
		Customer customer=null;
		try{
			JSONParser parser = new JSONParser();
			org.json.simple.JSONObject jsonFormatObject = (org.json.simple.JSONObject) parser.parse(nodeRedTafJSONQuery);
			if(jsonFormatObject == null) {
				log.info("Could not parse request : /testExecution/query/productBuilds");
				return RestResponseUtility.prepareErrorResponseWithoutData("No inputs provided", "No inputs provided");
			}
			
			//Authenticate the user
			String userName=jsonFormatObject.get("user") != null ? jsonFormatObject.get("user").toString():"";
			Response authResponse = authenticateUser(userName, "/testExecution/executetestplan");
			if (authResponse != null)
				return authResponse;
			
			UserList user = userListService.getUserByLoginId(userName);
			
			if(jsonFormatObject != null) {
				if((String) jsonFormatObject.get("testFactoryId") != null){
					try {
						testFactoryId = Integer.valueOf((String) jsonFormatObject.get("testFactoryId"));
					} catch (Exception er) {
						testFactoryId = -1;
					}
				}
				if (testFactoryId == null || testFactoryId == -1) {
					//If test factory id is not specified then add it to default testFactoryId : 1
					testFactoryId = 1;
				}
				if((String) jsonFormatObject.get("customerId") != null){
					try {
						customerId = Integer.valueOf((String) jsonFormatObject.get("customerId"));
						customer=customerService.getCustomerId(customerId);
						if(customer == null) {
							return RestResponseUtility.prepareErrorResponseWithoutData("Customer Id is invalid", "Customer Id is invalid");
						}
					} catch (Exception er) {
						customerId = -1;
					}
				}
				if (customerId == null || customerId == -1) {
					//If customer id is not specified then add it to default customerId : 1
					customerId = 1;
					customer=customerService.getCustomerId(customerId);
					if(customer == null) {
						return RestResponseUtility.prepareErrorResponseWithoutData("Customer Id is invalid", "Customer Id is invalid");
					}
				}
				//testFactoryId = Integer.valueOf((String) jsonFormatObject.get("testFactoryId"));
				//customerId = Integer.valueOf((String) jsonFormatObject.get("customerId"));
				//productTypeId = Integer.valueOf((String) jsonFormatObject.get("productTypeId"));
				if((String) jsonFormatObject.get("productTypeName") != null){
					productTypeName = (String) jsonFormatObject.get("productTypeName");
					if(productTypeName != null)
						productType = productListService.getProductTypeByName(productTypeName);
				}else if((String) jsonFormatObject.get("productTypeId") != null){
					try {
						productTypeId = Integer.valueOf((String) jsonFormatObject.get("productTypeId"));
					} catch (Exception er) {
						productTypeId = 2;
					}
					productType = productListService.getProductTypeById(productTypeId);
				}else{
					productType = productListService.getProductTypeById(2);
				}
				if(productType == null){
					return RestResponseUtility.prepareErrorResponseWithoutData( "Product Type is invalid",  "Product Type is invalid");
				}
				if((String) jsonFormatObject.get("modeId") != null){
					try {
						modeId = Integer.valueOf((String) jsonFormatObject.get("modeId"));
					} catch (Exception er) {
						modeId = -1;
					}
				}
				if (modeId == null || modeId == -1) {
					//If modeId is not specified then add it to default modeId : 2
					modeId = 2;
				}
				if((String) jsonFormatObject.get("status") != null){
					try {
						status = Integer.valueOf((String) jsonFormatObject.get("status"));
					} catch (Exception er) {
						status = -1;
					}
				}
				if (status == null || status == -1) {
					//If status is not specified then add it to default status : 1
					status = 1;
				}
				productName = (String) jsonFormatObject.get("productName");
				if(productName == null){
					return RestResponseUtility.prepareErrorResponseWithoutData( "Product Name is required",  "Product Name is required");
				}
				productDescription = (String) jsonFormatObject.get("productDescription");
				if(productDescription == null)
					productDescription = productName;
				projectCode = (String) jsonFormatObject.get("projectCode");
				if(projectCode == null)
					projectCode = productName;
			}
			
			//String errorMessage = ValidationUtility.validateForNewProductAddition(productName.trim(), productListService);
//			if (errorMessage!= null) {
				//boolean errorFlagTestFactory=productListService.isProductExitsInsameTestFactory(testFactoryId,productName);
				ProductMaster existingProduct=productListService.getProductExitsInsameTestFactory(testFactoryId, productName);
				if(existingProduct != null){
					
					if(existingProduct.getStatus() ==1) {
						return RestResponseUtility.prepareErrorResponseWithoutData("Product already Exist", "Product already Exist");
					} else {
						existingProduct.setStatus(1);
						productListService.updateProduct(existingProduct);
						JSONObject productObject= new JsonProductMaster(existingProduct).getCleanJson();
						return RestResponseUtility.prepareErrorResponseWithoutData("Updated product status for inactive to active", productObject.toString());
					}
				}
//			}
			TestFactory testFactory=testFactoryManagementService.getTestFactoryById(testFactoryId);
			if(testFactory == null) {
				return RestResponseUtility.prepareErrorResponseWithoutData("Testfactory Id is invalid", "Testfactory Id is invalid");
			}
			//Customer customer=customerService.getCustomerId(customerId);
			ProductMode prodMode=	productListService.getProductModeById(modeId);
			//Set values to productMaster Obj
			productMaster.setProductName(productName);
			productMaster.setProjectCode(projectCode);
			productMaster.setProjectName(projectName);
			productMaster.setProductDescription(productDescription);
			productMaster.setCreatedDate(DateUtility.getCurrentTime());
			productMaster.setStatusChangeDate(DateUtility.getCurrentTime());
			productMaster.setStatus(status);			
			productMaster.setTestFactory(testFactory);
			productMaster.setCustomer(customer);
			productMaster.setProductType(productType);
			productMaster.setProductMode(prodMode);
			Integer productId = productListService.addProduct(productMaster);
			if (productId == null) 
				productDetailsJSON.put("productId", "");
			else 
				productDetailsJSON.put("productId", productId);

			productDetailsJSON.put("productName", productName);
			productDetailsJSON.put("engagementId", testFactory.getTestFactoryId());
			productDetailsJSON.put("engagementName", testFactory.getTestFactoryName());
			productDetailsJSON.put("customerId", customer.getCustomerId());
			productDetailsJSON.put("customerName", customer.getCustomerName());
			productDetailsJSON.put("productType", productType.getTypeName());
			productDetailsJSON.put("productMode", prodMode.getModeName());
			String message="";
			if(productId != null && user != null) {

				ProductMaster product=productListService.getProductById(productId);
				if(product != null) {
					addProductPremissionToUser(user, product);
					message += "  - User "+user.getLoginId()+" mapped to "+product.getProductName()+" Product Successfully";
				}
				
			}
			return RestResponseUtility.prepareSuccessResponseWithJSONObject("Product added successfully"+message, productDetailsJSON);
		} catch(Exception e)  {			
			try {
				return RestResponseUtility.prepareSuccessResponse("Error adding product!", "Error adding product!");
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			return Response.noContent().build();
		}
	}

	@POST
	@Path("/testExecution/query/latestProductBuild")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response getLatestProductBuild(String nodeRedTafJSONQuery) {

		SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);	
		Integer productId = -1;
		ProductMaster product = null;
		ProductBuild productBuild = null;
		JSONArray arr = new JSONArray();
		ObjectMapper mapper = new ObjectMapper();
		try{
			org.json.simple.JSONObject jsonFormatObject = validateRequestQueryString(nodeRedTafJSONQuery);			
			if(jsonFormatObject == null) {
				return RestResponseUtility.prepareErrorResponseWithoutData("No inputs provided", "No inputs provided");
			}
			//Authenticate the user
			String userName=jsonFormatObject.get("user") != null ? jsonFormatObject.get("user").toString():"";
			Response authResponse = authenticateUser(userName, "/testExecution/query/latestProductBuild");
			if (authResponse != null)
				return authResponse;
			
			if(jsonFormatObject != null) {
				if((String)jsonFormatObject.get("productId") != null){
					try {
						productId = Integer.valueOf((String)jsonFormatObject.get("productId"));
					} catch (Exception er) {
						productId = -1;
					}
				}
				product = productListService.getProductById(productId);
				if (product == null) {
					return RestResponseUtility.prepareErrorResponseWithoutData("Product ID is missing or invalid", "Product ID is missing or invalid");
				}
			}
			productBuild = productBuildDAO.getLatestProductBuild(productId); 
			if (productBuild == null) {
				return RestResponseUtility.prepareErrorResponseWithoutData("Product does not have builds", "Product does not have builds");
			}
			if(productBuild != null) {	
				
				//Check authorization for user
				authResponse = checkUserAuthorization(productBuild.getProductVersion().getProductMaster().getProductId(), userName, "/testExecution/query/latestProductBuild");
				if (authResponse != null)
					return authResponse;
					JSONObject obj = new JSONObject();
					obj.put("id", productBuild.getProductBuildId());
					obj.put("name", productBuild.getBuildname());
					obj.put("created_date", productBuild.getCreatedDate());
					obj.put("product_id", productBuild.getProductMaster().getProductId());
					obj.put("product_name", productBuild.getProductMaster().getProductName());
					//ProductMaster product = null;
					if(productBuild.getProductMaster().getProductId() != null) {
						//Obtain product for the product build
						product = productListService.getProductById(productBuild.getProductMaster().getProductId());						
					}
					//Obtain product type for the product build
					if(product != null && product.getProductType() != null && product.getProductType().getTypeName() != null)
						obj.put("product_type", product.getProductType().getTypeName());
					else
						obj.put("product_type", "N/A");
					if(product != null && product.getProductName() != null)
						obj.put("product_name", product.getProductName());
					else
						obj.put("product_name", "N/A");
					Integer productBuildTestedCount = workPackageService.getProductBuildsTestedCount(productBuild.getProductBuildId());
					obj.put("build_tested_count", productBuildTestedCount);
					JSONObject object = new JSONObject(mapper.writeValueAsString(new JsonProductBuild(productBuild)));
					for(String key : JSONObject.getNames(obj)){
						object.put(key, obj.get(key));
					}
					arr.put(object);	
			}				
			log.info("Product Builds : " +arr);
			return RestResponseUtility.prepareSuccessResponseWithJSONArray("Latest Product build found", arr);
		} catch(Exception e)  {			
			try {
				return RestResponseUtility.prepareErrorResponseWithoutData("Unable to find the latest product build", "Unable to find the latest product build" + System.lineSeparator() + e);
			} catch (Exception e1) {
				log.error(e1);
			}
			return Response.noContent().build();
		}
	}
	
	@POST
	@Path("/testManagement/query/getproductBuildDetails")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response getProductBuildDetails(String nodeRedTafJSONQuery) {

		SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);	
		Integer productBuildId = null;
	
		ProductBuild productBuild = null;
		ObjectMapper mapper = new ObjectMapper();
		try{
			org.json.simple.JSONObject jsonFormatObject = validateRequestQueryString(nodeRedTafJSONQuery);			
			if(jsonFormatObject == null) {
				return RestResponseUtility.prepareErrorResponseWithoutData("No inputs provided", "No inputs provided");
			}
			//Authenticate the user
			String userName=jsonFormatObject.get("user") != null ? jsonFormatObject.get("user").toString():"";
			Response authResponse = authenticateUser(userName, "/testManagement/query/getproductBuildDetails");
			if (authResponse != null)
				return authResponse;
			
			if(jsonFormatObject != null) {
				if((String)jsonFormatObject.get("productBuildId") != null){
					try {
						productBuildId = Integer.valueOf((String)jsonFormatObject.get("productBuildId"));
					} catch (Exception er) {
						productBuildId = -1;
					}
				}
			}
			
			
			
			productBuild = productListService.getProductBuildById(productBuildId, 0); 
			if (productBuild == null) {
				return RestResponseUtility.prepareErrorResponseWithoutData("Product build not found", "Product build was not found. This could be because of missing or invalid id");
			}
			//Check authorization for user
			authResponse = checkUserAuthorization(productBuild.getProductVersion().getProductMaster().getProductId(), userName, "/testManagement/query/getproductBuildDetails");
			if (authResponse != null)
				return authResponse;

			JSONObject buildJson = new JSONObject();
			buildJson.put("productBuildId", productBuild.getProductBuildId());
			buildJson.put("productBuildName", productBuild.getBuildname());
			buildJson.put("created_date", productBuild.getCreatedDate());
			buildJson.put("productId", productBuild.getProductMaster().getProductId());
			buildJson.put("productName", productBuild.getProductMaster().getProductName());
			ProductMaster product = productBuild.getProductMaster();
			//Obtain product type for the product build
			if(product != null && product.getProductType() != null && product.getProductType().getTypeName() != null)
				buildJson.put("productType", product.getProductType().getTypeName());
			else
				buildJson.put("productType", "N/A");
			Integer productBuildTestedCount = workPackageService.getProductBuildsTestedCount(productBuild.getProductBuildId());
			buildJson.put("build_tested_count", productBuildTestedCount);
			return RestResponseUtility.prepareSuccessResponseWithJSONObject("Product build found", buildJson);
		} catch(Exception e)  {			
			try {
				return RestResponseUtility.prepareErrorResponseWithoutData("Unable to get the specified build", "Unable to get the specified build");
			} catch (Exception e1) {
				log.error(e1);
			}
		}
		return Response.noContent().build();
	}

	@POST
	@Path("/testManagement/query/getProduct")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response getProduct(String nodeRedTafJSONQuery) {

		SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);	
		Integer productId = -1;
		
		ProductMaster product = null;
		ObjectMapper mapper = new ObjectMapper();
		try{
			org.json.simple.JSONObject jsonFormatObject = validateRequestQueryString(nodeRedTafJSONQuery);			
			if(jsonFormatObject == null) {
				return RestResponseUtility.prepareErrorResponseWithoutData("No inputs provided", "No inputs provided");
			}
			//Authenticate the user
			String userName=jsonFormatObject.get("user") != null ? jsonFormatObject.get("user").toString():"";
			Response authResponse = authenticateUser(userName, "/testManagement/query/getProduct");
			if (authResponse != null)
				return authResponse;
			//If product id is specified, then get the product and return
			if((String)jsonFormatObject.get("productId") != null){
				try {
					productId = Integer.valueOf((String)jsonFormatObject.get("productId"));
					product = productListService.getProductById(productId);
				} catch (Exception er) {
					productId = -1;
				}
			} else if ((String)jsonFormatObject.get("productName") != null) {
				String productName = (String)jsonFormatObject.get("productName");
				if (productName != null & !productName.trim().isEmpty()) {
					product = productListService.getProductByName(productName); 		
				}
			}
			if (product == null) {
				return RestResponseUtility.prepareErrorResponseWithoutData("Product was not found", "Product was not found. This could be because of missing or invalid id/name");
			}
			//Check authorization for user
			authResponse = checkUserAuthorization(product.getProductId(), userName, "/testExecution/executetestplan");
			if (authResponse != null)
				return authResponse;
			JSONObject productJson = new JSONObject();
			productJson.put("productId", product.getProductId());
			productJson.put("productName", product.getProductName());
			productJson.put("productDescription", product.getProductDescription());
			productJson.put("productType", product.getProductType().getTypeName());
			productJson.put("productMode", product.getProductMode());
			productJson.put("productStatus", product.getStatus());
			productJson.put("createdDate", product.getCreatedDate());
			return RestResponseUtility.prepareSuccessResponseWithJSONObject( "Product found", productJson);
		} catch(Exception e)  {			
			try {
				return RestResponseUtility.prepareErrorResponseWithoutData("Unable to get the specified product", e.toString());
			} catch (Exception e1) {
				log.error(e1);
			}
		}
		return Response.noContent().build();
	}


	@POST
	@Path("/testManagement/query/getProductTestCases")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response getProductTestcases(String nodeRedTafJSONQuery) {
		SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);	
		Integer productId = -1;
		
		ProductMaster product = new ProductMaster();
		JSONArray arr = new JSONArray();
		ObjectMapper mapper = new ObjectMapper();
		try{
			org.json.simple.JSONObject jsonFormatObject = validateRequestQueryString(nodeRedTafJSONQuery);			
			if(jsonFormatObject == null) {
				return RestResponseUtility.prepareErrorResponseWithoutData("No inputs provided", "No inputs provided");
			}
			
			//Authenticate the user
			String userName=jsonFormatObject.get("user") != null ? jsonFormatObject.get("user").toString():"";
			Response authResponse = authenticateUser(userName, "/testManagement/query/getProductTestCases");
			if (authResponse != null)
				return authResponse;
			
			if(jsonFormatObject != null) {
				if((String)jsonFormatObject.get("productId") != null){
					try {
						productId = Integer.valueOf((String)jsonFormatObject.get("productId"));
					} catch (Exception er) {
						productId = -1;
					}
				}
			}
			product = productListService.getProductById(productId); 	
			if (product == null) {
				return RestResponseUtility.prepareErrorResponseWithoutData("Product was not found",  "Product Id is missing or invalid");
			}
			//Check authorization for user
			authResponse = checkUserAuthorization(product.getProductId(), userName, "/testManagement/query/getProductTestCases");
			if (authResponse != null)
				return authResponse;
			List<TestCaseList> testCaseList=testCaseService.getTestCaseListByProductId(product.getProductId(), null, null);
			if (testCaseList == null || testCaseList.isEmpty()) {
				return RestResponseUtility.prepareErrorResponseWithoutData("Product does not have testcases", "Product does not have testcases");
			}
			for(TestCaseList tcl : testCaseList){
				JsonTestCaseList josnTestcaseList=new JsonTestCaseList(tcl);
				arr.put(josnTestcaseList.getCleanJson());
			}
			return RestResponseUtility.prepareSuccessResponseWithJSONArray( "TestCases found", arr);
		} catch(Exception e)  {			
			try {
				return RestResponseUtility.prepareErrorResponseWithoutData("Unable to get Testcases for the specified product", e.toString());
			} catch (Exception e1) {
				log.error(e1);
			}
		}
		return Response.noContent().build();
	}
	
	@POST
	@Path("/testManagement/query/getProductFeatures")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response getProductFeatures(String nodeRedTafJSONQuery) {

		SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);	
		Integer productId = -1;
		
		ProductMaster product = new ProductMaster();
		List<ProductFeature> featuresList =new ArrayList<ProductFeature>();
		JSONArray arr = new JSONArray();
		ObjectMapper mapper = new ObjectMapper();
		try{
			org.json.simple.JSONObject jsonFormatObject = validateRequestQueryString(nodeRedTafJSONQuery);			
			if(jsonFormatObject == null) {
				return RestResponseUtility.prepareErrorResponseWithoutData("No inputs provided", "No inputs provided");
			}
			
			//Authenticate the user
			String userName=jsonFormatObject.get("user") != null ? jsonFormatObject.get("user").toString():"";
			Response authResponse = authenticateUser(userName, "/testManagement/query/getProductFeatures");
			if (authResponse != null)
				return authResponse;
			
			if(jsonFormatObject != null) {
				if((String)jsonFormatObject.get("productId") != null){
					try {
						productId = Integer.valueOf((String)jsonFormatObject.get("productId"));
					} catch (Exception er) {
						productId = -1;
					}
				}
			}
			product = productListService.getProductById(productId); 	
			if (product == null) {
				return RestResponseUtility.prepareErrorResponseWithoutData("Product was not found", "Product Id is missing or invalid");
			}
			
			
			//Check authorization for user
			authResponse = checkUserAuthorization(product.getProductId(), userName, "/testManagement/query/getProductFeatures");
			if (authResponse != null)
				return authResponse;
			featuresList = productListService.getFeatureListByProductId(product.getProductId(), 1, null, null);
			if (featuresList == null || featuresList.isEmpty()) {
				return RestResponseUtility.prepareErrorResponseWithoutData("Product does not have features", "Product does not have features");
			}
			for(ProductFeature feature : featuresList){
				
				JSONObject object = new JsonProductFeature(feature).getCleanJson();
				arr.put(object);
			}
			return RestResponseUtility.prepareSuccessResponseWithJSONArray("Features found", arr);
		} catch(Exception e)  {			
			try {
				return RestResponseUtility.prepareErrorResponseWithoutData("Unable to get the features for the specified product", e.toString());
			} catch (Exception e1) {
				log.error(e1);
			}
		}
		return Response.noContent().build();
	}

	@POST
	@Path("/testManagement/query/getProductTestsuites")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response getProductTestsuites(String nodeRedTafJSONQuery) {
		SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);	
		Integer productId = -1;
		
		ProductMaster product = new ProductMaster();
		JSONArray arr = new JSONArray();
		ObjectMapper mapper = new ObjectMapper();
		try{
			org.json.simple.JSONObject jsonFormatObject = validateRequestQueryString(nodeRedTafJSONQuery);			
			if(jsonFormatObject == null) {
				return RestResponseUtility.prepareErrorResponseWithoutData("No inputs provided", "No inputs provided");
			}
			//Authenticate the user
			String userName=jsonFormatObject.get("user") != null ? jsonFormatObject.get("user").toString():"";
			Response authResponse = authenticateUser(userName, "/testManagement/query/getProductTestsuites");
			if (authResponse != null)
				return authResponse;
			
			if(jsonFormatObject != null) {
				if((String)jsonFormatObject.get("productId") != null){
					try {
						productId = Integer.valueOf((String)jsonFormatObject.get("productId"));
					} catch (Exception er) {
						productId = -1;
					}
				}
			}
			product = productListService.getProductById(productId); 	
			if (product == null) {
				return RestResponseUtility.prepareErrorResponseWithoutData( "Product was not found","Product Id is missing or invalid");
			}
			
			//Check authorization for user
			authResponse = checkUserAuthorization(product.getProductId(), userName, "/testManagement/query/getProductTestsuites");
			if (authResponse != null)
				return authResponse;
			List<TestSuiteList> testSuiteListTotal=testSuiteConfigurationService.getByProductId(null, null, product.getProductId());
			if (testSuiteListTotal == null || testSuiteListTotal.isEmpty()) {
				return RestResponseUtility.prepareErrorResponseWithoutData( "Product does not have testsuites","Product does not have testsuites");
			}
			for(TestSuiteList tsl : testSuiteListTotal){
				JSONObject object = new JSONObject(mapper.writeValueAsString(new JsonTestSuiteList(tsl)));
				arr.put(object);
			}
			return RestResponseUtility.prepareSuccessResponseWithJSONArray("Test Suites found", arr);
		} catch(Exception e)  {			
			try {
				return RestResponseUtility.prepareErrorResponseWithoutData("Unable to get Test suites for the specified product", e.toString());
			} catch (Exception e1) {
				log.error(e1);
			}
		}
		return Response.noContent().build();
	}

	@POST
	@Path("/testManagement/query/getTestsuiteTestcases")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response getTestsuiteTestcases(String nodeRedTafJSONQuery) throws JSONException {

		SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);	
		Integer testSuiteId = -1;
		TestSuiteList testSuiteList = null;
	
		JSONArray arr = new JSONArray();
		ObjectMapper mapper = new ObjectMapper();
		try{
			org.json.simple.JSONObject jsonFormatObject = validateRequestQueryString(nodeRedTafJSONQuery);			
			if(jsonFormatObject == null) {
				return RestResponseUtility.prepareErrorResponseWithoutData("No inputs provided", "No inputs provided");
			}
			//Authenticate the user
			String userName=jsonFormatObject.get("user") != null ? jsonFormatObject.get("user").toString():"";
			Response authResponse = authenticateUser(userName, "/testManagement/query/getTestsuiteTestcases");
			if (authResponse != null)
				return authResponse;
			
			if(jsonFormatObject != null) {
				if((String)jsonFormatObject.get("testSuiteId") != null){
					try {
						testSuiteId = Integer.valueOf((String)jsonFormatObject.get("testSuiteId"));
					} catch (Exception er) {
						testSuiteId = -1;
					}
				}
			}
			
			
			testSuiteList = testSuiteConfigurationService.getByTestSuiteId(testSuiteId);
			if (testSuiteList == null) {
				return RestResponseUtility.prepareErrorResponseWithoutData("Testsuite was not found", "Testsuite was not found");
			}
			//Check authorization for user
			authResponse = checkUserAuthorization(testSuiteList.getProductVersionListMaster().getProductMaster().getProductId(), userName, "/testManagement/query/getTestsuiteTestcases");
			if (authResponse != null)
				return authResponse;
			Set<TestCaseList> listTestCase = testSuiteList.getTestCaseLists();
			for(TestCaseList tcl : listTestCase){
				JsonTestCaseList josnTestcaseList =new JsonTestCaseList(tcl);
				
				arr.put(josnTestcaseList.getCleanJson());
			}
			if (arr.length() > 0) {
				return RestResponseUtility.prepareSuccessResponseWithJSONArray("Test Cases Listing success", arr);
			}else {
				return RestResponseUtility.prepareSuccessResponse("Testsuite does not have testcases", "");
			}
		} catch(Exception e)  {			
			try {
				return RestResponseUtility.prepareErrorResponseWithoutData("Unable to get Test cases for the specified product", e.toString());
			} catch (Exception e1) {
				log.error(e1);
			}
		}
		return Response.noContent().build();
	}
	
	@POST
	@Path("/testManagement/query/addTestSuite")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response addTestSuiteForProduct(String testSuitesJson) {

		SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);		
		JTableSingleResponse jTableSingleResponse = null;
		JSONObject responseJson = new JSONObject();
		try{
			responseJson= commonTestManagementService.addTestSuites(testSuitesJson);
		} catch(Exception e ) {
			try {
				return RestResponseUtility.prepareErrorResponseWithoutData("Error adding TestSuite", "Error adding TestSuite");
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			return Response.ok(responseJson.toString()).build();
		}			
		return Response.ok(responseJson.toString()).build();
	}

	@POST
	@Path("/testExecution/query/modifyTestConfigurationTestcases")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response modifyTestConfigurationTestcases(String nodeRedTafJSONQuery) {

		SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);	
		Integer testSuiteId = null;
		Integer testConfigId = null;
		Integer testCaseId = null;
		RunConfiguration testConfiguration = null;
		TestSuiteList testSuiteList = null;
		TestCaseList testCaseList = null;
		String action = null;
	
		ObjectMapper mapper = new ObjectMapper();
		try{
			JSONParser parser = new JSONParser();
			org.json.simple.JSONObject jsonFormatObject = (org.json.simple.JSONObject) parser.parse(nodeRedTafJSONQuery);	
			if(jsonFormatObject == null) {
				return RestResponseUtility.prepareErrorResponseWithoutData("No inputs provided", "No inputs provided");
			}
			
			//Authenticate the user
			String userName=jsonFormatObject.get("user") != null ? jsonFormatObject.get("user").toString():"";
			Response authResponse = authenticateUser(userName, "/testExecution/modifyTestConfigurationTestcases");
			if (authResponse != null)
				return authResponse;
			
			if(jsonFormatObject != null) {
				if((String)jsonFormatObject.get("testConfigId") != null){
					try {
						testConfigId = Integer.valueOf((String)jsonFormatObject.get("testConfigId"));
					} catch (Exception er) {
						testConfigId = -1;
					}
				}
			}
			testConfiguration = productListService.getRunConfigurationById(testConfigId);
			if (testConfiguration == null) {
				return RestResponseUtility.prepareErrorResponseWithoutData( "Test Config Id is missing or invalid", "Test Config Id is missing or invalid");
			}
			if((String)jsonFormatObject.get("testSuiteId") != null){
				try {
					testSuiteId = Integer.valueOf((String)jsonFormatObject.get("testSuiteId"));
				} catch (Exception er) {
					testSuiteId = -1;
				}
			}
			testSuiteList = testSuiteConfigurationService.getByTestSuiteId(testSuiteId);
			if (testSuiteList == null) {
				return RestResponseUtility.prepareErrorResponseWithoutData( "Test Suite Id is missing or invalid", "Test Suite Id is missing or invalid");
			}
			if((String)jsonFormatObject.get("testCaseId") != null){
				try {
					testCaseId = Integer.valueOf((String)jsonFormatObject.get("testCaseId"));
				} catch (Exception er) {
					testCaseId = -1;
				}
			}
			testCaseList = testCaseService.getTestCaseById(testCaseId);
			if (testCaseList == null) {
				return RestResponseUtility.prepareErrorResponseWithoutData( "Test Case Id is missing or invalid", "Test Case Id is missing or invalid");
			}
			action = (String) jsonFormatObject.get("action");
			if (action == null || action.trim().isEmpty() || (!action.equalsIgnoreCase("Map") && !action.equalsIgnoreCase("UnMap"))) {
				return RestResponseUtility.prepareErrorResponseWithoutData( "Mapping option specified is not a valid one. It has to be Map or UnMap", "Mapping option specified is not a valid one. It has to be Map or UnMap");
			}
			//Check authorization for user
			authResponse = checkUserAuthorization(testCaseList.getProductMaster().getProductId(), userName, "/testExecution/modifyTestConfigurationTestcases");
			if (authResponse != null)
				return authResponse;
			
			//Check authorization for user
			authResponse = checkUserAuthorization(testConfiguration.getProduct().getProductId(), userName, "/testExecution/modifyTestConfigurationTestcases");
			if (authResponse != null)
				return authResponse;
			
			
			if(testConfiguration.getProduct().getProductId() != testCaseList.getProductMaster().getProductId()){
				return RestResponseUtility.prepareErrorResponseWithoutData("Test configuration and test case does not belong to the same product","Test configuration and test case does not belong to the same product");
			}
			if((testConfiguration.getProduct().getProductId() != testSuiteList.getProductMaster().getProductId())){
				return RestResponseUtility.prepareErrorResponseWithoutData("Test configuration and test suite does not belong to the same product","Test configuration and test suite does not belong to the same product");
			}
			String message;
			action = action.equalsIgnoreCase("Map") ? "Add" : "Remove";
			List<TestCaseList> runConfigTestCaseList = productListService.getRunConfigTestSuiteTestCaseMapped(testConfiguration.getRunconfigId(), testSuiteList.getTestSuiteId());
			if(runConfigTestCaseList.contains(testCaseList) && action.equalsIgnoreCase("Add")){
				return RestResponseUtility.prepareErrorResponseWithoutData("Failed : "+testCaseList.getTestCaseName()+" is already mapped","Failed : "+testCaseList.getTestCaseName()+" is already mapped");
			}
			if(!runConfigTestCaseList.contains(testCaseList) && action.equalsIgnoreCase("Remove")){
				return RestResponseUtility.prepareErrorResponseWithoutData("Failed : "+testCaseList.getTestCaseName()+" is not mapped","Failed : "+testCaseList.getTestCaseName()+" is not mapped");
			}
			productListService.mapTestSuiteTestCasesRunConfiguration(testConfigId, testSuiteId,testCaseId,action);
			if(action.equalsIgnoreCase("Add")){
				message = "Test Case mapped to Run Configuration";
			}else{
				message = "Test Case unmapped from Run Configuration";
			}
			return RestResponseUtility.prepareSuccessResponse(message, "");
		} catch(Exception e)  {			
			try {
				return RestResponseUtility.prepareErrorResponseWithoutData("Unable to map/unmap testcases to test configuration", "Unable to map/unmap testcases to test configuration");
			}catch(Exception e1) {
				log.error(e1);
			}
		}
		return Response.noContent().build();
	}
	
	
	/**
	 * 
	 * @param
	 *  {
			"testPlanId" : "10",
			"productId" : "2",
			"testConfigurations" : [{
								"testConfigurationId" : "3",
								"testConfigurationName" : "XXX",
								"environmentName" : "win-Chrome",
								"device" : "device1",
								"testcaseIds" : "2,5,10,12",
								"testcaseNames" : "testcase2, testcase5, testcase10,testcase12,testcase23,testcase24"
							},
			               	{
								"device" : "device2",
								"testcaseIds" : "81,89,98,204,207,511",
								"testcaseNames" : "testcase81, testcase89, testcase98"
							}
			               ]
		}
	 * @return
	 */
	@POST
	@Path("/testExecution/query/executeSelectiveTestcasesTestPlan")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response executeSelectiveTestCasesTestPlan(String nodeRedTafJSONQuery) { 

		SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);		
		JSONObject workpackageJsonObj = new JSONObject();
		JSONObject responseJson = new JSONObject();	
		String replaceStr1= "\"\\\"";
		String replaceStr2 = "\"\\\"";
		try{
			String formattedJsonString = nodeRedTafJSONQuery;
			if(formattedJsonString != null && !formattedJsonString.isEmpty())
				nodeRedTafJSONQuery = formattedJsonString.replace(replaceStr1, "\"").replace(replaceStr2,"").replace("\"{", "{").replace("}\"", "\"}").replace("\"\"","\"").replace("\\","").replace("$a","");
			log.info("JSON Input : "+nodeRedTafJSONQuery);
			responseJson = atlasService.executeSelectiveTestCasesTestPlan(nodeRedTafJSONQuery);				
		} catch (Exception e){
			try {
				workpackageJsonObj.put("Result", "FAILED");
				workpackageJsonObj.put("message", "Test Plan execution not initiated due to some issue." + System.lineSeparator() +  e);
				workpackageJsonObj.put("Failure_Details", e.getStackTrace());
				return RestResponseUtility.prepareErrorResponse("Test Plan execution not initiated due to some issue." + System.lineSeparator() +  e,  e.getStackTrace().toString(), workpackageJsonObj.toString());
			} catch (Exception e1) {
				log.error("Problem while executing Test Plan through REST call ", e1);
			}
			log.error("Problem while executing Test Plan through REST call ", e);
		}		
		return Response.ok(responseJson.toString()).build();
	}
	
	@POST
	@Path("/testExecution/query/mapTestConfigTestCasesByAction")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response mapTestConfigTestCasesByAction(String nodeRedTafJSONQuery) {

		SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);		
		
		Integer testSuiteId = null;
		Integer testConfigId = null;
		Integer testCaseId = null;
		RunConfiguration testConfiguration = null;
		TestSuiteList testSuiteList = null;
		TestCaseList testCaseList = null;
		JSONArray arr = new JSONArray();
		ObjectMapper mapper = new ObjectMapper();
		String action = null;
		String testCaseIds = null;
		String[] testCaseIdArr = null;
		JSONObject object = new JSONObject();
		Set<TestCaseList> testSuiteTestCaseList = new HashSet<TestCaseList>();
		Set<TestCaseList> inputTestCaseList = new HashSet<TestCaseList>();
		List<TestCaseList> runConfigTestCaseList = new ArrayList<TestCaseList>();
		try {	
			JSONParser parser = new JSONParser();
			org.json.simple.JSONObject jsonFormatObject = (org.json.simple.JSONObject) parser.parse(nodeRedTafJSONQuery);
			if(jsonFormatObject == null) {
				return RestResponseUtility.prepareErrorResponseWithoutData("No inputs provided", "No inputs provided");
			}
			
			//Authenticate the user
			String userName=jsonFormatObject.get("user") != null ? jsonFormatObject.get("user").toString():"";
			Response authResponse = authenticateUser(userName, "/testExecution/mapTestConfigTestCasesByAction");
			if (authResponse != null)
				return authResponse;
			
			action = (String)jsonFormatObject.get("action");
			if (action == null || action.trim().isEmpty() || (!action.equals("Map") && !action.equals("UnMap") && !action.equals("MapAll") && !action.equals("UnMapAll") && !action.equals("MapOnly"))) {
				return RestResponseUtility.prepareErrorResponseWithoutData("action is missing or invalid. Valid values are Map/UnMap/MapOnly/MapAll/UnmapAll", "action is missing or invalid. Valid values are Map/UnMap/MapOnly/MapAll/UnmapAll");
			}
			if(jsonFormatObject != null) {
				if((String)jsonFormatObject.get("testConfigId") != null){
					try {
						testConfigId = Integer.valueOf((String)jsonFormatObject.get("testConfigId"));
					} catch (Exception er) {
						testConfigId = -1;
					}
				}
			}
			testConfiguration = productListService.getRunConfigurationById(testConfigId);
			if (testConfiguration == null ) {
				return RestResponseUtility.prepareErrorResponseWithoutData("Test Config Id is missing or invalid", "Test Config Id is missing or invalid");
			}
			if((String)jsonFormatObject.get("testSuiteId") != null){
				try {
					testSuiteId = Integer.valueOf((String)jsonFormatObject.get("testSuiteId"));
				} catch (Exception er) {
					testSuiteId = -1;
				}
				testSuiteList = testSuiteConfigurationService.getByTestSuiteId(testSuiteId);
			}
			//If testSuite id is not specified, take the first testsuite mapped to the test configuration.
			if (testSuiteList == null) {
				testSuiteList = testConfiguration.getTestSuiteLists().parallelStream().findFirst().get();
			}
			if(testSuiteList == null){
				return RestResponseUtility.prepareErrorResponseWithoutData( "Test Suite Id is missing or invalid", "Test Suite Id is missing or invalid");
			}
			
			//Check authorization for user
			authResponse = checkUserAuthorization(testSuiteList.getProductMaster().getProductId(), userName, "/testExecution/mapTestConfigTestCasesByAction");
			if (authResponse != null)
				return authResponse;
			boolean isTestSuiteMappedToTestConfig = false;
			if(testConfiguration.getTestSuiteLists() != null){
				for(TestSuiteList tsl : testConfiguration.getTestSuiteLists()){
					if(testSuiteList.getTestSuiteId().equals(tsl.getTestSuiteId())){
						isTestSuiteMappedToTestConfig = true;
					}
				}
			}
			if(!isTestSuiteMappedToTestConfig){
				return RestResponseUtility.prepareErrorResponseWithoutData("Test Suite : "+testSuiteId+" is not mapped to the testconfig : "+testConfigId, "Test Suite : "+testSuiteId+" is not mapped to the testconfig : "+testConfigId);
			}
			testCaseIds = (String)jsonFormatObject.get("testCaseIds");
			if(testCaseIds != null){
				try{
					testCaseIdArr = testCaseIds.split(",");
					for(String tc : testCaseIdArr) {	
						int tcId = Integer.valueOf(tc);
						TestCaseList testCase = testCaseService.getTestCaseById(tcId);
						inputTestCaseList.add(testCase);
					}
				} catch(Exception e) {
					inputTestCaseList = null;
					log.error("Error while mapping Testcase to Test Suite");
				}
			}
			if(inputTestCaseList == null && !(action.equals("MapAll") || action.equals("UnMapAll"))) {
				return RestResponseUtility.prepareErrorResponseWithoutData("One or more TestcaseIds are invalid.", "One or more TestcaseIds are invalid.");
			}
			runConfigTestCaseList = productListService.getRunConfigTestSuiteTestCaseMapped(testConfiguration.getRunconfigId(), testSuiteList.getTestSuiteId());
			testSuiteTestCaseList = testSuiteList.getTestCaseLists();
			switch(action){
				case "Map":
					log.info("action is Map");
					for(TestCaseList tcl: inputTestCaseList){		
						if(runConfigTestCaseList.contains(tcl)){
							object.put(tcl.getTestCaseName(), tcl.getTestCaseName()+" already mapped to the testconfig");
							continue;
						}else{
							if(testSuiteTestCaseList != null && testSuiteTestCaseList.contains(tcl)){
								productListService.mapTestSuiteTestCasesRunConfiguration(testConfiguration.getRunconfigId(), testSuiteList.getTestSuiteId(), tcl.getTestCaseId(),"Add");
								log.info("mapped Test Case : " +tcl.getTestCaseName()+" to test configuration");
								object.put(tcl.getTestCaseName(), "mapped to testconfig successfully");
							}else{
								testSuiteConfigurationService.addTestCase(tcl.getTestCaseId(),testSuiteList.getTestSuiteId());
								productListService.mapTestSuiteTestCasesRunConfiguration(testConfiguration.getRunconfigId(), testSuiteList.getTestSuiteId(), tcl.getTestCaseId(),"Add");
								log.info("mapped Test Case : " +tcl.getTestCaseName()+" to Test Suite and Test Configuration ");	
								object.put(tcl.getTestCaseName(), "mapped to testSuite and testconfig successfully");
							}
						}
					}
					break;
				case "UnMap":
					log.info("action is unMap");
					for(TestCaseList tcl: inputTestCaseList){		
						if(runConfigTestCaseList.contains(tcl)){
							productListService.mapTestSuiteTestCasesRunConfiguration(testConfiguration.getRunconfigId(), testSuiteList.getTestSuiteId(), tcl.getTestCaseId(),"Remove");
							log.info("mapped Test Case : " +tcl.getTestCaseName()+" to test configuration");
								object.put(tcl.getTestCaseName(), "unmapped from testconfig successfully");
						}else{
							object.put(tcl.getTestCaseName(), tcl.getTestCaseName()+" is not mapped to the testconfig to unmap");
							continue;
						}
					}
					break;
				case "MapOnly":
					log.info("action is ISE");
					for(TestCaseList tcl: inputTestCaseList){										
						if(runConfigTestCaseList != null && !runConfigTestCaseList.contains(tcl)){						
							if(testSuiteTestCaseList != null && testSuiteTestCaseList.contains(tcl)){						
								productListService.mapTestSuiteTestCasesRunConfiguration(testConfiguration.getRunconfigId(), testSuiteList.getTestSuiteId(), tcl.getTestCaseId(),"Add");
								log.info("mapped Test Case : " +tcl.getTestCaseName()+" to test configuration");
								object.put(tcl.getTestCaseName(), "mapped to testconfig successfully");
							}else{
								testSuiteConfigurationService.addTestCase(tcl.getTestCaseId(),testSuiteList.getTestSuiteId());
								productListService.mapTestSuiteTestCasesRunConfiguration(testConfiguration.getRunconfigId(), testSuiteList.getTestSuiteId(), tcl.getTestCaseId(),"Add");
								log.info("mapped Test Case : " +tcl.getTestCaseName()+" to Test Suite and Test Configuration ");
								object.put(tcl.getTestCaseName(), "mapped to testsuite and testconfig successfully");
							}
						}else{
							object.put(tcl.getTestCaseName(), tcl.getTestCaseName()+" already mapped to the testconfig");
							continue;
						}
					}
					for(TestCaseList tcl : runConfigTestCaseList){
						if(inputTestCaseList != null && !inputTestCaseList.contains(tcl)){
							productListService.mapTestSuiteTestCasesRunConfiguration(testConfiguration.getRunconfigId(), testSuiteList.getTestSuiteId(), tcl.getTestCaseId(),"Remove");
							log.info("mapped Test Case : " +tcl.getTestCaseName()+" to test configuration");
							object.put(tcl.getTestCaseName(), "unmapped from testconfig successfully");
						}
					}
					break;
				case "MapAll":
					log.info("action is MapAll");
					for(TestCaseList tcl : testSuiteTestCaseList){
						if(runConfigTestCaseList != null && !runConfigTestCaseList.contains(tcl)){						
							productListService.mapTestSuiteTestCasesRunConfiguration(testConfiguration.getRunconfigId(), testSuiteList.getTestSuiteId(), tcl.getTestCaseId(),"Add");
							log.info("mapped Test Case : " +tcl.getTestCaseName()+" to test configuration");		
							object.put(tcl.getTestCaseName(), "mapped to testconfig successfully");
						}else{
							object.put(tcl.getTestCaseName(), tcl.getTestCaseName()+" already mapped to the testconfig");
							continue;
						}
					}
					if(testSuiteTestCaseList.isEmpty()){
						object.put("Error","There are no testcases available in the testsuite to map");
					}
					break;
				case "UnMapAll":
					log.info("action in UnMapAll");
					for(TestCaseList tcl : runConfigTestCaseList){
						productListService.mapTestSuiteTestCasesRunConfiguration(testConfiguration.getRunconfigId(), testSuiteList.getTestSuiteId(), tcl.getTestCaseId(),"Remove");
						log.info("mapped Test Case : " +tcl.getTestCaseName()+" to test configuration");
						object.put(tcl.getTestCaseName(), "unmapped to testconfig successfully");
					}
					if(runConfigTestCaseList.isEmpty()){
						object.put("Error","There are no testcases available in the testsuite to unmap");
					}
					break;
			}
			arr.put(object);
			return RestResponseUtility.prepareSuccessResponseWithJSONArray(arr.toString(), arr);
	    } catch (Exception e) {
            try {
            	log.error(e);
            	return RestResponseUtility.prepareErrorResponseWithoutData( "Error mapping testcases!", e.toString());
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			return Response.noContent().build();
	    }		        
	}
	
	@POST
	@Path("/testManagement/query/engagements")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response getEngagements(String nodeRedTafJSONQuery) {
		SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
		JSONObject obj = new JSONObject();
		JSONArray arr = new JSONArray();
		ObjectMapper mapper = new ObjectMapper();
		try{
			if(nodeRedTafJSONQuery == null || nodeRedTafJSONQuery.trim().isEmpty()) {
				log.info("no input data : /testManagement/query/engagements");
				return RestResponseUtility.prepareErrorResponseWithoutData("No inputs provided", "No inputs provided");
			}
			JSONParser parser = new JSONParser();
			org.json.simple.JSONObject jsonFormatObject = (org.json.simple.JSONObject) parser.parse(nodeRedTafJSONQuery);
		if(jsonFormatObject == null) {
			log.info("Could not parse request : /testManagement/query/mapUnMapFeaturesToProductBuild");
			return RestResponseUtility.prepareErrorResponseWithoutData("No inputs provided", "No inputs provided");
		}
		//Authenticate the user
		String userName=jsonFormatObject.get("user") != null ? jsonFormatObject.get("user").toString():"";
		Response authResponse = authenticateUser(userName, "/testManagement/query/engagements");
		if (authResponse != null)
			return authResponse;
			//List<TestFactory> engagementTypes = testFactoryManagementService.getTestFactoryList();
			List<TestFactory> engagementTypes = testFactoryManagementService.getTestFactoryList(1,TAFConstants.ENTITY_STATUS_ACTIVE,2);
			if (engagementTypes == null) {
				return RestResponseUtility.prepareErrorResponseWithoutData("No inputs provided", "No inputs provided");
			}
			
			for(TestFactory engagement : engagementTypes){
				arr.put(new JsonTestFactory(engagement).getCleanJson());
			}
			return RestResponseUtility.prepareSuccessResponseWithJSONArray("Engagements found", arr);
		}catch(Exception e){
            try {
            	return RestResponseUtility.prepareErrorResponseWithoutData("Error getting Engagements!", e.toString());
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			return Response.noContent().build();
	    }
	}
	
	@POST
	@Path("/testManagement/query/addEngagement")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response addEngagement(String nodeRedTafJSONQuery) {
		SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);	
		String engagementTypeName = null;
		String engagementDesc = null;
		Integer engagementTypeId = null;
		JSONArray arr = new JSONArray();
		ObjectMapper mapper = new ObjectMapper();
		
		try{
			
			JSONParser parser = new JSONParser();
			org.json.simple.JSONObject jsonFormatObject = (org.json.simple.JSONObject) parser.parse(nodeRedTafJSONQuery);			
			if(jsonFormatObject == null) {
				log.info("Could not parse request : /testManagement/query/addEngagement");
				return RestResponseUtility.prepareErrorResponseWithoutData("No inputs provided", "No inputs provided");
			}
			
			//Authenticate the user
			String userName=jsonFormatObject.get("user") != null ? jsonFormatObject.get("user").toString():"";
			Response authResponse = authenticateUser(userName, "/testManagement/query/addEngagement");
			if (authResponse != null)
				return authResponse;
			
			if((String) jsonFormatObject.get("engagementName") != null){
				engagementTypeName = (String) jsonFormatObject.get("engagementName");
			}
			
			if((String) jsonFormatObject.get("engagementDesc") != null){
					engagementDesc = (String) jsonFormatObject.get("engagementDesc");
			}
			if((String)jsonFormatObject.get("engagementTypeId") != null){
				try {
					engagementTypeId = Integer.valueOf((String)jsonFormatObject.get("engagementTypeId"));
				} catch (Exception er) {
					engagementTypeId = null;
				}
			}
			if (engagementTypeId == null) {
				return RestResponseUtility.prepareErrorResponseWithoutData( "engagementType Id is missing or invalid",  "engagementType Id is missing or invalid");
			}
			EngagementTypeMaster engagement = new EngagementTypeMaster();
			String errorMessage=commonService.duplicateName(engagementTypeName, "EngagementTypeMaster", "engagementTypeName", "Engagement", null);
			if (errorMessage != null) {					
				return RestResponseUtility.prepareErrorResponseWithoutData( errorMessage, errorMessage);
			}
			engagement.setEngagementTypeName(engagementTypeName);
			engagement.setDescription(engagementDesc);
			engagement.setEngagementTypeId(engagementTypeId);
			int engagementId = testFactoryManagementService.addEngagement(engagement);
			JSONObject object = new JSONObject(mapper.writeValueAsString(new JsonEngagementTypeMaster(engagement)));
			arr.put(object);
			return RestResponseUtility.prepareSuccessResponseWithJSONArray("Engagement added", arr);
		} catch(Exception e ) {
            try {
            	log.error(e);
            	return RestResponseUtility.prepareErrorResponseWithoutData( "Error getting Engagements!", e.toString());
			} catch (Exception e1) {
				e1.printStackTrace();
				log.error(e1);
			}
			return Response.noContent().build();
	    }
	}
	
	@POST
	@Path("/testExecution/query/getTestcaseExecutionResults")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response getTestCaseExecutionResults(String nodeRedTafJSONQuery) {
		SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
		
		JSONArray arr = new JSONArray();
		JSONObject object = new JSONObject();
		ObjectMapper mapper = new ObjectMapper();
		List<Integer> workPackageIdList = new ArrayList<Integer>();
		WorkPackage workPackage = null;
		Integer limit = -1;
		Integer offset = -1;
		List<WorkPackage> workPackageList = new ArrayList<WorkPackage>();
		List<TestCaseDTO> testCaseDTOlist = new ArrayList<TestCaseDTO>();
		try{
			JSONParser parser = new JSONParser();
			org.json.simple.JSONObject jsonFormatObject = (org.json.simple.JSONObject) parser.parse(nodeRedTafJSONQuery);
			if(jsonFormatObject == null) {
				log.info("Could not parse request : /testExecution/query/getTestcaseExecutionResults");
				return RestResponseUtility.prepareErrorResponseWithoutData("No inputs provided", "No inputs provided");
			}
			
			//Authenticate the user
			String userName=jsonFormatObject.get("user") != null ? jsonFormatObject.get("user").toString():"";
			Response authResponse = authenticateUser(userName, "/testExecution/getTestcaseExecutionResults");
			if (authResponse != null)
				return authResponse;
			
			//If workpackages are specified, they will get the first pref
			//Get the results for the specified workpackages. 
			String workPackageIds = (String) jsonFormatObject.get("workPackageIds");
			String productId = (String) jsonFormatObject.get("productId");
			String productBuildId = (String) jsonFormatObject.get("productBuildId");
			String productName = (String) jsonFormatObject.get("productName");
			//String fromDate = (String) jsonFormatObject.get("fromDate");
			//String toDate = (String) jsonFormatObject.get("toDate");
			if((String) jsonFormatObject.get("limit") != null){
				try {
					limit = Integer.valueOf((String)jsonFormatObject.get("limit"));
				} catch (Exception er) {
					limit = -1;
				}
			}
			if((String) jsonFormatObject.get("offset") != null){
				try {
					offset = Integer.valueOf((String)jsonFormatObject.get("offset"));
				} catch (Exception er) {
					offset = -1;
				}
			}
			if(workPackageIds == null){
				return RestResponseUtility.prepareErrorResponseWithoutData("Workpackage Ids are missing", "Workpackage Ids are missing");
			}
			
			if (workPackageIds != null && !workPackageIds.trim().isEmpty()) {
				try{
					for(String workPackageId : workPackageIds.split(",")){
						workPackageIdList.add(Integer.valueOf(workPackageId));
					}
				}catch(Exception e){
					return RestResponseUtility.prepareErrorResponseWithoutData("Invalid workPackage Id(s)","Invalid workPackage Id(s)");
				}
				for(Integer wpId : workPackageIdList){
					workPackage = workPackageService.getWorkPackageById(wpId);
					if(workPackage != null)
						workPackageList.add(workPackage);
				}
				if(workPackageList == null || workPackageList.isEmpty()){
					return RestResponseUtility.prepareErrorResponseWithoutData("workpackage Ids are invalid", "Workpackage Ids specified are not valid");
				}
				
				ProductMaster product=workPackageList.get(0).getProductBuild().getProductVersion().getProductMaster();
				//Check authorization for user
				authResponse = checkUserAuthorization(product.getProductId(), userName, "/testExecution/getTestcaseExecutionResults");
				if (authResponse != null)
					return authResponse;
				for(WorkPackage wp : workPackageList){
					testCaseDTOlist = workPackageService.listTestCaseExecutionDetailsOfTRJob(0, wp.getWorkPackageId());
					for(TestCaseDTO tcer : testCaseDTOlist){
						JSONObject jsonTCER = new JsonWorkPackageTestCaseExecutionPlanForTester(tcer, "yes").getCleanJson();
						//JSONObject jsonTCER = new JSONObject(mapper.writeValueAsString(new JsonWorkPackageTestCaseExecutionPlanForTester(tcer, "yes")));
						jsonTCER.put("workPackageId", wp.getWorkPackageId());
						String actualExecutionDate = "NA";
						if(wp.getCreateDate() != null && wp.getCreateDate().toString().contains(" ")){
							String createdDate = wp.getCreateDate().toString();
							actualExecutionDate = createdDate.split(" ")[0];
						}
						jsonTCER.put("actualExecutionDate", actualExecutionDate);
						arr.put(jsonTCER);
					}
				}
				//Constructing JSOn Object as a JSON response to the Node-Red nodes
			} else if (productId != null || productName != null){
				//Check if product details are specified. If yes, get results for the specified products
				//from date is mandatory
				ProductMaster productMaster = null;
				Integer prodId = null;
				if(productId != null){
					try {
						prodId = Integer.valueOf((String)jsonFormatObject.get("productId"));
					} catch (Exception er) {
						prodId = -1;
					}
					productMaster = productListService.getProductById(prodId);
				}else if(productName != null){
					productMaster = productListService.getProductByName(productName);
				}
				if (productMaster == null) {
					return RestResponseUtility.prepareErrorResponseWithoutData("Product ID/Name is invalid", "Product ID/Name is invalid");
				}
				
				//Check authorization for user
				authResponse = checkUserAuthorization(productMaster.getProductId(), userName, "/testExecution/executetestplan");
				if (authResponse != null)
					return authResponse;
				
				workPackageList = workPackageDAO.listAllWorkPackagesBasedOnProductBuildIdAndDateFilters(Integer.valueOf(productBuildId), null, null);
				for(WorkPackage wp : workPackageList){
					testCaseDTOlist = workPackageService.listTestCaseExecutionDetailsOfTRJob(0, wp.getWorkPackageId());
					for(TestCaseDTO tcer : testCaseDTOlist){
						JSONObject jsonTCER = new JSONObject(mapper.writeValueAsString(new JsonWorkPackageTestCaseExecutionPlanForTester(tcer, "yes")));
						jsonTCER.put("workPackageId", wp.getWorkPackageId());
						arr.put(jsonTCER);
					}
				}
			} else {
			
				List<ProductMaster> products=new ArrayList<>();
				UserList userList = userListService.getUserByLoginId(userName);
				if(userList != null) {
					products= productMasterDAO.getProductDetailsByUserId(userList.getUserId());
					if(products == null || products.size() == 0) {
						return RestResponseUtility.prepareErrorResponseWithoutData("User not mapped for the given product", "User not mapped for the given product");
					}
				}
				if(workPackageList != null && workPackageList.size() > 0){
					for(WorkPackage wp : workPackageList){
						testCaseDTOlist = workPackageService.listTestCaseExecutionDetailsOfTRJob(0, wp.getWorkPackageId());
						for(TestCaseDTO tcer : testCaseDTOlist){
							for(ProductMaster product:products) {
								if(tcer.getProductMaster().getProductId().equals(product.getProductId())) {
										JSONObject jsonTCER = new JSONObject(mapper.writeValueAsString(new JsonWorkPackageTestCaseExecutionPlanForTester(tcer, "yes")));
										jsonTCER.put("workPackageId", wp.getWorkPackageId());
										arr.put(jsonTCER);
								}
							}
						}
					}	
				}
			}
			return RestResponseUtility.prepareSuccessResponseWithJSONArray("Test Case Execution results listing success", arr);
		}catch(Exception e){
            try {
            	return RestResponseUtility.prepareErrorResponseWithoutData("Error getting Test Case Execution results!", e.toString());
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			return Response.noContent().build();
	    }
	}
	
	
	@POST
	@Path("/testExecution/query/getTestPlanTestcasesForUI")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response getTestPlanAndConfigurationSelectedTestCasesUsecases(String nodeRedTafJSONQuery) {
	
		Integer testPlanId = null;
		SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
		TestRunPlan testPlan = null;
		try{
			JSONParser parser = new JSONParser();
			org.json.simple.JSONObject jsonFormatObject = (org.json.simple.JSONObject) parser.parse(nodeRedTafJSONQuery);	
			if(jsonFormatObject == null) {			
				return RestResponseUtility.prepareErrorResponseWithoutData("No inputs provided", "No inputs provided");
			}
			
			//Authenticate the user
			String userName=jsonFormatObject.get("user") != null ? jsonFormatObject.get("user").toString():"";
			Response authResponse = authenticateUser(userName, "/testExecution/getTestPlanTestcasesForUI");
			if (authResponse != null)
				return authResponse;
			
			if((String) jsonFormatObject.get("testPlanId") != null) {
				try {
					testPlanId = Integer.valueOf((String) jsonFormatObject.get("testPlanId"));
				} catch (Exception er) {
					testPlanId = -1;
				}
				testPlan = productListService.getTestRunPlanById(testPlanId);
			}
			if (testPlan == null) {
				return RestResponseUtility.prepareErrorResponseWithoutData("Testplan ID is missing or invalid", "Testplan ID is missing or invalid");
			}
			
			//Check authorization for user
			authResponse = checkUserAuthorization(testPlan.getProductVersionListMaster().getProductMaster().getProductId(), userName, "/testExecution/getTestPlanTestcasesForUI");
			if (authResponse != null)
				return authResponse;
			
			JSONArray list = new JSONArray();
			JSONObject finalObj = new JSONObject();
			JSONObject tsTitle = new JSONObject();
			JSONObject tcTitle = new JSONObject();
			JSONObject rcTitle = new JSONObject();
			JSONObject tsData = new JSONObject();
			JSONObject tcData = new JSONObject();
			JSONObject rcData = new JSONObject();
			JSONArray columnData = new JSONArray();
			JSONArray allcolumnsData = new JSONArray();
			JsonTestRunPlan jsonTestRunPlan = null;
			List<Integer> runConfigIds = new LinkedList<Integer>();
			String  finalResult = "";
			if(testPlan != null ){	
				String testSuiteName="";
				String testCaseName="";
				String runConfigMapping="";
				
				tsTitle.put("title", "TestSuite");					
				list.put(tsTitle);
				
				tcTitle.put("title", "TestCase");					
				list.put(tcTitle);

				Set<RunConfiguration> runConfigurations = testPlan.getRunConfigurationList();
				MultiMap testSuiteRunConfigurationMap = new MultiHashMap();
				for(RunConfiguration rc : runConfigurations){
					rcTitle= new JSONObject();
					rcTitle.put("title", rc.getRunconfigName());
					rcTitle.put("id", String.valueOf(rc.getRunconfigId()));
					list.put(rcTitle);
					runConfigIds.add(rc.getRunconfigId());
				}				
				finalObj.put("COLUMNS", list);
				
				Set<TestSuiteList> testSuiteLists = new HashSet<TestSuiteList>();
				Set<TestCaseList> testCaseLists = new HashSet<TestCaseList>();	
				
				for(RunConfiguration rc : runConfigurations){
					testSuiteLists.addAll(rc.getTestSuiteLists());
					for(TestSuiteList tsl : testSuiteLists){
						for (TestCaseList tc : productListService.getRunConfigTestSuiteTestCaseMapped(rc.getRunconfigId(), tsl.getTestSuiteId())){
							testSuiteRunConfigurationMap.put(tc.getTestCaseId(),rc.getRunconfigId());
						}
					}
				}
				
				for(TestSuiteList tsl : testSuiteLists){
					testSuiteName = tsl.getTestSuiteName();
					for(RunConfiguration rc : testPlan.getRunConfigurationList()){
						if(testPlan.getUseIntelligentTestPlan() != null && !testPlan.getUseIntelligentTestPlan().equalsIgnoreCase("Yes"))
							testCaseLists.addAll(productListService.getRunConfigTestSuiteTestCaseMapped(rc.getRunconfigId(), tsl.getTestSuiteId()));
						else
							testCaseLists.addAll(tsl.getTestCaseLists());
					}
					for (TestCaseList testCaseList : testCaseLists) {
						boolean isRecommended = false;
						testCaseName = testCaseList.getTestCaseName();						
						columnData = new JSONArray();
						columnData.put(testSuiteName+"["+tsl.getTestSuiteId()+"]");
						columnData.put(testCaseName+"["+testCaseList.getTestCaseId()+"]");
						
						for(RunConfiguration rc : runConfigurations){							
							List<String> s = (List<String>) testSuiteRunConfigurationMap.get(testCaseList.getTestCaseId());				
							if(s != null && !s.isEmpty() && s.contains(rc.getRunconfigId())){
								//runConfigMapping = rc.getRunconfigName()+"["+rc.getRunconfigId()+"]";																
								runConfigMapping = "Yes";																
							} else {
								runConfigMapping = "No";
							}
							//columnData.add(isRecommended);
							columnData.put(runConfigMapping);
						}
						allcolumnsData.put(columnData);
					}
					testCaseLists = null;
					testCaseLists = new HashSet<TestCaseList>();
				}
				
				finalObj.put("DATA", allcolumnsData);
			}			
			finalResult="["+finalObj.toString()+"]";
			return RestResponseUtility.prepareSuccessResponse( "Test bed distribution Success", finalResult.toString());
		}catch(Exception e){
            try {
            	return RestResponseUtility.prepareErrorResponseWithoutData("Error getting Test Bed Distribution!", e.toString());
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			return Response.noContent().build();
	    }
	}
	
	@POST
	@Path("/testExecution/query/getTestCaseExecutionSummaryAndHistory")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response getTestCaseExecutionSummaryAndHistory(String nodeRedTafJSONQuery) {
		SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
	
		JSONArray summaryArr = new JSONArray();
		JSONArray historyArr = new JSONArray();
		JSONObject object = new JSONObject();
		ObjectMapper mapper = new ObjectMapper();
		Integer testCaseId = null;
		TestCaseList testCaseList = null;
		JsonWorkPackageTestCaseExecutionPlanForTester jsonwtcep = null;	
		List<JsonWorkPackageTestCaseExecutionPlanForTester> jwptcepList = new LinkedList<JsonWorkPackageTestCaseExecutionPlanForTester>();
		try{
			JSONParser parser = new JSONParser();
			org.json.simple.JSONObject jsonFormatObject = (org.json.simple.JSONObject) parser.parse(nodeRedTafJSONQuery);
			if(jsonFormatObject == null) {
				log.info("Could not parse request : /testExecution/query/getTestCaseExecutionSummaryAndHistory");
				return RestResponseUtility.prepareErrorResponseWithoutData("No inputs provided", "No inputs provided");
			}
			//Authenticate the user
			String userName=jsonFormatObject.get("user") != null ? jsonFormatObject.get("user").toString():"";
			Response authResponse = authenticateUser(userName, "/testExecution/getTestCaseExecutionSummaryAndHistory");
			if (authResponse != null)
				return authResponse;
			
			if((String)jsonFormatObject.get("testCaseId") != null){
				try {
					testCaseId = Integer.valueOf((String)jsonFormatObject.get("testCaseId"));
				} catch (Exception er) {
					testCaseId = -1;
				}
				testCaseList = testCaseService.getTestCaseById(testCaseId);
			}
			if (testCaseList == null) {
				return RestResponseUtility.prepareErrorResponseWithoutData("Test Case Id is missing or invalid", "Test Case Id is missing or invalid");
			}
			
			//Check authorization for user
			authResponse = checkUserAuthorization(testCaseList.getProductMaster().getProductId(), userName, "/testExecution/getTestCaseExecutionSummaryAndHistory");
			if (authResponse != null)
				return authResponse;
			List<JsonWorkPackageTestCaseExecutionResultSummary>  jsonWPTCEPTObjList = testCaseService.getTestCaseExecutionResultSummary(testCaseId, -1, -1, "productLevel");
			for(JsonWorkPackageTestCaseExecutionResultSummary summary : jsonWPTCEPTObjList){
				JSONObject jsonsummary = new JSONObject(mapper.writeValueAsString(summary.getCleanJson()));
				summaryArr.put(jsonsummary);
			}
			List<Object[]> tcexecuteHistory = workPackageService.getTescaseExecutionHistory(testCaseId, -1, "productLevel", 0, 100);
			if(tcexecuteHistory != null && !tcexecuteHistory.isEmpty()){
				for (Object[] objects : tcexecuteHistory) {
					jsonwtcep = new JsonWorkPackageTestCaseExecutionPlanForTester();								
					
					jsonwtcep.setEnvironmentCombinationName((String)objects[0]);
					jsonwtcep.setTestCaseExecutionResultId((Integer)objects[1]);
					jsonwtcep.setTestcaseId((Integer)objects[2]);					
					jsonwtcep.setTestcaseName((String)objects[3]);
					
					if(objects[4] != null){	
						String executionDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(objects[4]);
						jsonwtcep.setActualExecutionDate(executionDate);							
					}	else { jsonwtcep.setActualExecutionDate("Not Available"); }
					jsonwtcep.setProductName((String)objects[5]);
					jsonwtcep.setProductVersionName((String)objects[6]);
					jsonwtcep.setProductBuildName((String)objects[7]);
					jsonwtcep.setWorkPackageId((Integer)objects[8]);
					jsonwtcep.setWorkPackageName((String)objects[9]);
					jsonwtcep.setExeType((String)objects[10]);
					
					String result = (String)objects[11];
					jsonwtcep.setResult("NOT EXECUTED");
					if(result.equals("1") || result.equals("PASS") || result.equals(IDPAConstants.EXECUTION_RESULT_PASSED)){
						jsonwtcep.setResult("PASS");
					}else if(result.equals("2") || result.equals("FAIL") || result.equals(IDPAConstants.EXECUTION_RESULT_FAILED)){
						jsonwtcep.setResult("FAIL");
					}else if(result.equals("3")  || result.equals(IDPAConstants.EXECUTION_RESULT_NORUN)){
						jsonwtcep.setResult("NOT RUN");
					}else if(result.equals("4") || result.equals(IDPAConstants.EXECUTION_RESULT_BLOCKED)){
						jsonwtcep.setResult("BLOCKED");
					}				
										
					jsonwtcep.setTestRunJobId((Integer)objects[12]);
					if(objects[12] != null){
						String devName = workPackageService.getDeviceNameByTestRunJob((Integer)objects[12]);
						jsonwtcep.setDeviceName(devName);
					}						
					jwptcepList.add(jsonwtcep);
				}
			}
			for(JsonWorkPackageTestCaseExecutionPlanForTester jsonwpece : jwptcepList){
				historyArr.put(jsonwpece.getCleanJson());
			}
			
			if(summaryArr.length() == 0 || historyArr.length() == 0){
				return RestResponseUtility.prepareSuccessResponse("No testcase summary and history for the testcase.Please check valid testcase id or valid execution data", "No testcase summary and history for the testcase.Please check valid testcase id or valid execution data");
			} /*else {
				responseJson.put("data", "Summary and History found for the testcase");
				responseJson.put("message", "Summary and History found for the testcase");
			}*/
			
			return RestResponseUtility.prepareSuccessResponseWithSummaryandHistory("Summary and History found for the testcase", "Summary and History found for the testcase", summaryArr.toString(), historyArr.toString());
		}catch(Exception e){
            try {
            	return RestResponseUtility.prepareErrorResponseWithoutData("Error getting Test Case Execution results!", e.toString());
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			return Response.noContent().build();
	    }
	}
	
	@POST
	@Path("/testExecution/query/getIntelligentTestcases")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response getIntellignetTestcases(String nodeRedTafJSONQuery) {

		SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);		
		
		Integer testPlanId = null;
		Integer productBuildId = null;
		TestRunPlan testPlan = null;
		JSONArray arr = new JSONArray();
		List<JsonTestCaseList> jsonIseRecommendationTestcases = new LinkedList<JsonTestCaseList>();
		Integer totalTCCount = 0;
		Integer recommendedTCCount = 0;
		ObjectMapper mapper = new ObjectMapper();
		try {	
			JSONParser parser = new JSONParser();
			org.json.simple.JSONObject jsonFormatObject = (org.json.simple.JSONObject) parser.parse(nodeRedTafJSONQuery);
			if (jsonFormatObject == null) {
				log.info("Could not parse request : /testExecution/query/getIntelligentTestcases");
				return RestResponseUtility.prepareErrorResponseWithoutData("No inputs provided", "No inputs provided");
			}
			//Authenticate the user
			String userName=jsonFormatObject.get("user") != null ? jsonFormatObject.get("user").toString():"";
			Response authResponse = authenticateUser(userName, "/testExecution/getIntelligentTestcases");
			if (authResponse != null)
				return authResponse;
			if((String) jsonFormatObject.get("testPlanId") != null) {
				try {
					testPlanId = Integer.valueOf((String) jsonFormatObject.get("testPlanId"));
				} catch (Exception er) {
					testPlanId = -1;
				}
				testPlan = productListService.getTestRunPlanById(testPlanId);
			}
			if (testPlan == null) {
				return RestResponseUtility.prepareErrorResponseWithoutData("Testplan ID is missing or invalid", "Testplan ID is missing or invalid");
			}
			log.info("Test Plan Id : " +testPlanId);
			if((String) jsonFormatObject.get("productBuildId") != null){
				try {
					productBuildId = Integer.valueOf((String) jsonFormatObject.get("productBuildId"));
				} catch (Exception er) {
					productBuildId = -1;
				}
				ProductBuild build = productListService.getProductBuildById(productBuildId, 0);
				if(build == null){
					return RestResponseUtility.prepareErrorResponseWithoutData("Product Build ID is missing or invalid", "Product Build ID is missing or invalid");
				}
			}
			//Check authorization for user
			authResponse = checkUserAuthorization(testPlan.getProductVersionListMaster().getProductMaster().getProductId(), userName, "/testExecution/getIntelligentTestcases");
			if (authResponse != null)
				return authResponse;
			jsonIseRecommendationTestcases = atlasService.getISERecommendedTestcases(testPlan , productBuildId);
			totalTCCount = jsonIseRecommendationTestcases.size();
			if(jsonIseRecommendationTestcases == null || jsonIseRecommendationTestcases.isEmpty()){
				return RestResponseUtility.prepareErrorResponseWithoutData(recommendedTCCount+" testcases recommended out of "+ totalTCCount, "");
			}
			
			for(JsonTestCaseList tc : jsonIseRecommendationTestcases){
				JSONObject object = new JSONObject();
				object.put("testCaseId", tc.getTestCaseId());
				object.put("testCaseName", tc.getTestCaseName());
				if(tc.getIseRecommended() != null)
					object.put("isRecommended", tc.getIseRecommended());
				else
					object.put("isRecommended", "NO");
				//object.put("isRecommended", "YES");
				if(tc.getRecommendedCategory() != null)
					object.put("category", tc.getRecommendedCategory());
				else
					object.put("category", "NA");
				arr.put(object);
				recommendedTCCount = tc.getRecommendedTestCaseCount();
			}
			
			return RestResponseUtility.prepareSuccessResponseWithJSONArray( recommendedTCCount+" testcases recommended out of "+ totalTCCount, arr);
	    } catch (Exception e) {
            try {
            	return RestResponseUtility.prepareErrorResponseWithoutData("Error getting ISE testcases!",e.toString());
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			return Response.noContent().build();
	    }		        
	}
	
	/*
	 * This service creates a user and assigns the user a role and maps the user the specified products
	 */
	@POST
	@Path("testManagement/query/createUser")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response createUser(String userDetails) throws JSONException {
		SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
		
		ObjectMapper mapper = new ObjectMapper();
		JSONArray arr = new JSONArray();
		StringBuffer sb = new StringBuffer();
		String password="";
		try {
			JSONParser parser = new JSONParser();
			org.json.simple.JSONObject jsonFormatObject = (org.json.simple.JSONObject) parser.parse(userDetails);
			if(jsonFormatObject == null) {
				log.info("Could not parse request : /createUser");
				return RestResponseUtility.prepareErrorResponseWithoutData("No inputs provided", "No inputs provided");
			}

			String userName=jsonFormatObject.get("userName") != null ?jsonFormatObject.get("userName").toString():"";
			String productNames=jsonFormatObject.get("productName") != null ?jsonFormatObject.get("productName").toString():"";
			if(userName == null || userName.trim().isEmpty()){
				return RestResponseUtility.prepareErrorResponseWithoutData("userName is required", "userName is required");
			}	
			UserList existingUserList=userListService.getUserByLoginId(userName);
			if(existingUserList != null ) {
				if(existingUserList.getStatus() == 1) {
					if(productNames != null && !productNames.isEmpty()) {
						String[] productsArray = productNames.split(",");
						for (String productName : productsArray) {
							ProductMaster product=productMasterDAO.getProductByName(productName);
							if(product != null) {
								if(productTeamResourcesDao.isExistsTeamResourceByUserIdandProductIdandUserId(product.getProductId(), existingUserList.getUserId())) {
									continue;
								}
								if(productListService.isProductUserRoleExits(product.getProductId(), existingUserList.getUserId(), existingUserList.getUserRoleMaster().getUserRoleId())) {
									continue;
								}
								addProductPremissionToUser(existingUserList, product);
							}
						}
					}
					return RestResponseUtility.prepareErrorResponseWithoutData("User Already Exist", "User Already Exist");
				} else {
					existingUserList.setStatus(1);
					userListService.update(existingUserList);
					JSONObject object = new JSONObject(mapper.writeValueAsString(new JsonUserList(existingUserList)));
					arr.put(object);
					return RestResponseUtility.prepareSuccessResponse("Updated user status for inactive to active", arr.toString());
				}
			}

			String role=jsonFormatObject.get("role") != null ?jsonFormatObject.get("role").toString():"";
			String emailId=jsonFormatObject.get("emailId") != null ?jsonFormatObject.get("emailId").toString():"";
			String phoneNumber=jsonFormatObject.get("contactNumber") != null ?jsonFormatObject.get("contactNumber").toString():"";
			String authenticationType=jsonFormatObject.get("authenticationType") != null ?jsonFormatObject.get("authenticationType").toString():"";
			
			if(emailId == "" || emailId.trim().isEmpty()) {
				if(!CommonUtility.validateEmail(emailId)) {
					return RestResponseUtility.prepareErrorResponseWithoutData("EmailId field is required", "EmailId field is required");
				}
			}
			
			if(phoneNumber == "" || phoneNumber.trim().isEmpty()) {
				if(!CommonUtility.validatePhoneNumber(phoneNumber)) {
					return RestResponseUtility.prepareErrorResponseWithoutData("Contact Number field is required", "Contact Number field is required");
				}
			}
			if (role.trim().isEmpty()) {
				role="TECHNICAL MANAGER";
			} 
			
			if(password == "" || password.isEmpty()) {
				password="Idpa@123";
			}
			AuthenticationType userAuthenticationType=new AuthenticationType();
			
			
			if(authenticationType != null && !authenticationType.isEmpty()) {
				if(authenticationType.equalsIgnoreCase("local")) {
					userAuthenticationType.setAuthenticationTypeId(1);
				} else if(authenticationType.equalsIgnoreCase("enterprise")){
					userAuthenticationType.setAuthenticationTypeId(2);
				}
			} else {
				userAuthenticationType.setAuthenticationTypeId(2);
			}
			
			String ilcmRole=CommonUtility.roleConversion(role); 
			UserTypeMasterNew userTypeMasterNew = new UserTypeMasterNew();
			userTypeMasterNew.setUserTypeId(1);
			
			TestfactoryResourcePool resourcePool= new TestfactoryResourcePool();
			resourcePool.setResourcePoolId(-10);
			VendorMaster vendor = new  VendorMaster();
			vendor.setVendorId(-10);

			UserList userList = new UserList();
			UserRoleMaster userRoleMaster=userListService.getRoleByLabel(ilcmRole);
			userList.setEmailId(emailId);
			userList.setFirstName(userName);
			
			userList.setUserPassword(CommonUtility.encrypt(password));
			userList.setLoginId(userName);
			userList.setStatus(1);
			userList.setUserDisplayName(userName);
			userList.setContactNumber(phoneNumber);
			//userList.setUserCode(userName);
			userList.setUserRoleMaster(userRoleMaster);
			userList.setUserTypeMasterNew(userTypeMasterNew);
			userList.setAuthenticationType(userAuthenticationType);
			userList.setResourcePool(resourcePool);
			userList.setVendor(vendor);
			CommonActiveStatusMaster commonActiveStatusMaster= new CommonActiveStatusMaster();
			commonActiveStatusMaster.setStatus("ACTIVE");
			userList.setCommonActiveStatusMaster(commonActiveStatusMaster);
			Languages languages = new Languages();
			languages.setId(1);
			userList.setLanguages(languages);
			userListService.add(userList);
			String message="";
			//Map user to products
		
			if (productNames.trim().isEmpty()) {
				//Do nothing
			} else {
				String products="";
				String mappedUserPermissions="";
				String mappedTeamUsers="";
				String[] productsArray = productNames.split(",");
				for (String productName : productsArray) {
					
					ProductMaster product=productMasterDAO.getProductByName(productName);
					//UserList userList=userListService.getUserByLoginId(user);

					if(product ==null) {
						if(products == "") {
							products=productName;
						} else {
							products +=","+productName;
						}
						continue;
					}

					if(productTeamResourcesDao.isExistsTeamResourceByUserIdandProductIdandUserId(product.getProductId(), userList.getUserId())) {
						if(mappedTeamUsers == "") {
							mappedTeamUsers=product.getProductId().toString();
						} else {
							mappedTeamUsers+=","+product.getProductId().toString();
							
						}
						continue;
					}

					if(productListService.isProductUserRoleExits(product.getProductId(), userList.getUserId(), userList.getUserRoleMaster().getUserRoleId())) {
						if(mappedUserPermissions == "") {
							mappedUserPermissions+=product.getProductId().toString()+":"+userList.getUserRoleMaster().getUserRoleId();
						} else {
							mappedUserPermissions+=","+product.getProductId().toString()+":"+userList.getUserRoleMaster().getUserRoleId();
						}
						continue;
					}

					if(userList != null){
						
						addProductPremissionToUser(userList, product);
					}
					if(message == ""){
						message = product.getProductName();
					}else{
						message += ", "+product.getProductName();
					}
					
				}
				
					if(products != ""){
						sb.append("Invalid Products :"+products+" ");
					} else {
						message += "  - products are mapped successfully";
					}
					if(mappedTeamUsers != "") {
						sb.append(" User Already mapped with this userpermission to the products:"+mappedTeamUsers+" ");
					}
					
					if(mappedUserPermissions !="") {
				
						sb.append(" User Already mapped with this product User Team to the products:"+mappedUserPermissions+" ");	
					}
				
				
			}


			JSONObject object = new JSONObject(mapper.writeValueAsString(new JsonUserList(userList)));
			arr.put(object);

			if(message !="" && !message.trim().isEmpty() && sb != null && sb.length() >0) {
				return RestResponseUtility.prepareSuccessResponseWithJSONArray("User created Successfully and "+message+"- "+sb.toString(), arr);
			} else if(message == "" && (sb != null && sb.length() >0)){
				return RestResponseUtility.prepareSuccessResponseWithJSONArray( "User created Successfully "+"- "+sb.toString(), arr);
			} else {
				return RestResponseUtility.prepareSuccessResponseWithJSONArray( "User created Successfully",arr);
			}

		} catch(Exception e) {
			return RestResponseUtility.prepareErrorResponseWithoutData("Error in User creation", "Error in User creation");
		}
	}

	@POST
	@Path("/testManagement/query/mapUserToProduct")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response mappedUserToProduct(String mappedUserDetails) throws JSONException {
	SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
	
	try {
		
		JSONParser parser = new JSONParser();
		org.json.simple.JSONObject jsonFormatObject = (org.json.simple.JSONObject) parser.parse(mappedUserDetails);
		if(jsonFormatObject == null) {
			log.info("Could not parse request : /map user to Product");
			return RestResponseUtility.prepareErrorResponseWithoutData("No inputs provided", "No inputs provided");
		}
		
		//Authenticate the user
		String user=jsonFormatObject.get("user") != null ? jsonFormatObject.get("user").toString():"";
		Response authResponse = authenticateUser(user, "/testManagement/query/mapUserToProduct");
		if (authResponse != null)
			return authResponse;
		
		
		String productName=(String)jsonFormatObject.get("productName");
		String userToBeMapped = (String)jsonFormatObject.get("userToBeMapped");
		if(userToBeMapped ==null ||userToBeMapped.trim().isEmpty()) {
			return RestResponseUtility.prepareErrorResponseWithoutData( "userToBeMapped is required",  "userToBeMapped is required");
		}
		if (userToBeMapped == null || userToBeMapped.trim().isEmpty())
			userToBeMapped = user;
		
		if(productName ==null || productName.trim().isEmpty()) {
			return RestResponseUtility.prepareErrorResponseWithoutData( "ProductName is required",  "ProductName is required");
		}
		
		if(user ==null ||user.trim().isEmpty()) {
			return RestResponseUtility.prepareErrorResponseWithoutData( "User is required",  "User is required");
		}
		
		if((productName != null && !productName.trim().isEmpty())&&(user != null && !user.trim().isEmpty())) {
			
			ProductMaster product=productMasterDAO.getProductByName(productName);
			UserList userList=userListService.getUserByLoginId(user);
			UserList userToBeMappedList=userListService.getUserByLoginId(userToBeMapped);
			
			if(product ==null) {
				return RestResponseUtility.prepareErrorResponseWithoutData("Invalid product", "Invalid product");
			}
			if(userList == null) {
				return RestResponseUtility.prepareErrorResponseWithoutData("Invalid User", "Invalid User");
			}
			
			if(userToBeMappedList == null) {
				return RestResponseUtility.prepareErrorResponseWithoutData("Invalid User", "Invalid User");
			}
			
			//Check authorization for user
			authResponse = checkUserAuthorization(product.getProductId(), user, "/testExecution/query/mapUserToProduct");
			if (authResponse != null)
				return authResponse;
			
			ProductUserRole productUserRole=productMasterDAO.getProductUserRole(product.getProductId(), userList.getUserId());
			
			if(productUserRole != null) {
				if(productUserRole.getRole().getUserRoleId().equals(IDPAConstants.ROLE_ID_ADMIN) || productUserRole.getRole().getUserRoleId().equals(IDPAConstants.ROLE_ID_TEST_MANAGER) || productUserRole.getRole().getUserRoleId().equals(IDPAConstants.ROLE_ID_TEST_FACTORY_MANAGER)|| productUserRole.getRole().getUserRoleId().equals(IDPAConstants.ROLE_ID_PROGRAM_MANAGER) || productUserRole.getRole().getUserRoleId().equals(IDPAConstants.ROLE_ID_RESOURCE_MANAGER) ) {
					
				} else {
					return RestResponseUtility.prepareErrorResponseWithoutData("You are not authorised to perform this action. To perform this action, user role must be a Testmanager or Admin",
							"You are not authorised to perform this action. To perform this action, user role must be Testmanager or Admin");
				}
			} 

			if(productTeamResourcesDao.isExistsTeamResourceByUserIdandProductIdandUserId(product.getProductId(), userToBeMappedList.getUserId())) {
				return RestResponseUtility.prepareErrorResponseWithoutData("User Already mapped with this userpermission", "User Already mapped with this userpermission");
			}
			
			if(productListService.isProductUserRoleExits(product.getProductId(), userToBeMappedList.getUserId(), userToBeMappedList.getUserRoleMaster().getUserRoleId())) {
				return RestResponseUtility.prepareErrorResponseWithoutData("User Already mapped with this product User Team", "User Already mapped with this product User Team");
			}
			
			if(userToBeMappedList != null){
				ProductTeamResources productTeamUser = new ProductTeamResources();
					productTeamUser.setProductTeamResourceId(null);
					productTeamUser.setProductMaster(product);
					productTeamUser.setUserList(userToBeMappedList);
					productTeamUser.setStatus(1);
					
				//	productTeamUser.setFromDate(plannedStartDate);
				//	productTeamUser.setToDate(plannedEndDate);
					productTeamResourcesDao.addProductTeamResource(productTeamUser);

					ProductUserRole productUserPermission = new ProductUserRole();
					productUserPermission.setProduct(product);
					productUserPermission.setRole(userToBeMappedList.getUserRoleMaster());
					productUserPermission.setUser(userToBeMappedList);
					productUserPermission.setStatus(1);
					productListService.addProductUserRole(productUserPermission);
			}
		}
		
		return RestResponseUtility.prepareSuccessResponse("User mapped to Product Successfully", "");
	
		}catch(Exception e) {
			return RestResponseUtility.prepareErrorResponseWithoutData("Error in mapping user to the product", "Error in mapping user to the product");
		}
	 }
	
	@POST
	@Path("/testManagement/query/addFeatures")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response addFeatures(String featureDetails) throws JSONException {
	SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
	JSONObject responseJson = new JSONObject();
		try {
			responseJson=commonTestManagementService.addFeatures(featureDetails);
		}catch(Exception e) {
			return RestResponseUtility.prepareErrorResponseWithoutData("Error in Features creation", "Error in Features creation");
		}
		return Response.ok(responseJson.toString()).build();	
	 }
	
	

	@POST
	@Path("/testManagement/query/mapFeatureToTestcases")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response mapFeatureToTestcaseMapping(String mappedFeatureToTestcaseDetails) throws JSONException {
	SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
	JSONObject responseJson = new JSONObject();
	
		try {
			responseJson=commonTestManagementService.mapFeatureToTestcases(mappedFeatureToTestcaseDetails);
		}catch(Exception e) {
			return RestResponseUtility.prepareErrorResponseWithoutData("Error in mapping feature to Testcases", "Error in mapping feature to Testcases");
			
		}
		return Response.ok(responseJson.toString()).build();
	 }
	
	@POST
	@Path("/testManagement/query/getUserRoles")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response getUserRoles(String nodeRedTafJSONQuery) throws JSONException {
		SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
		JSONObject responseJson = new JSONObject();
		List<UserRoleMaster> roleList = new ArrayList<UserRoleMaster>();
		JSONArray arr = new JSONArray();
		ObjectMapper mapper = new ObjectMapper();
		try {
			if(nodeRedTafJSONQuery == null || nodeRedTafJSONQuery.trim().isEmpty()) {
				log.info("no input data : /testManagement/query/getUserRoles");
				return RestResponseUtility.prepareErrorResponseWithoutData("No inputs provided", "No inputs provided");
			}
			JSONParser parser = new JSONParser();
			org.json.simple.JSONObject jsonFormatObject = (org.json.simple.JSONObject) parser.parse(nodeRedTafJSONQuery);
		if(jsonFormatObject == null) {
			log.info("Could not parse request : /testManagement/query/getUserRoles");
			return RestResponseUtility.prepareErrorResponseWithoutData("No inputs provided", "No inputs provided");
		}
		//Authenticate the user
		String userName=jsonFormatObject.get("user") != null ? jsonFormatObject.get("user").toString():"";
		Response authResponse = authenticateUser(userName, "/testManagement/query/getUserRoles");
		if (authResponse != null)
			return authResponse;
		
			roleList=userListService.listRole();
			if(roleList != null && roleList.size() >0) {
				for(UserRoleMaster userRole:roleList) {
				JSONObject object = new JSONObject(mapper.writeValueAsString(userRole));
				arr.put(object);
				}
			}
			return RestResponseUtility.prepareSuccessResponseWithJSONArray("User role listing success", arr);
		}catch(Exception e) {
			return RestResponseUtility.prepareErrorResponseWithoutData( "Error in user role list",  "Error in user role list");
		}
	}
	@POST
	@Path("/testManagement/query/mapUnMapFeaturesToProductBuild")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response mapFeaturesToProductBuild(String nodeRedTafJSONQuery) {

		SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);	
		Integer productBuildId = 0;
		String featureIds = null;
		Integer isMapped = 0;
		List<Integer> featureIdList = new ArrayList<>();
		String action = null;
	
		boolean isFeatureAlreadyMapped = false;
		Set<ProductFeature> inputFeaturesList =new HashSet<ProductFeature>();
		List<ProductFeature> featuresList =new ArrayList<ProductFeature>();
		List<ProductFeature> mappedFeaturesList =new ArrayList<ProductFeature>();
		List<ProductFeature> featureBuildList =new ArrayList<ProductFeature>();
		JSONArray arr = new JSONArray();
		try{
			JSONParser parser = new JSONParser();
			org.json.simple.JSONObject jsonFormatObject = (org.json.simple.JSONObject) parser.parse(nodeRedTafJSONQuery);
			if(jsonFormatObject == null) {
				log.info("Could not parse request : /testManagement/query/mapUnMapFeaturesToProductBuild");
				return RestResponseUtility.prepareErrorResponseWithoutData("No inputs provided", "No inputs provided");
			}
			
			//Authenticate the user
			String userName=jsonFormatObject.get("user") != null ? jsonFormatObject.get("user").toString():"";
			Response authResponse = authenticateUser(userName, "/testExecution/executetestplan");
			if (authResponse != null)
				return authResponse;
			
			featureIds = (String) jsonFormatObject.get("featureIds");
			if(featureIds == null){
				return RestResponseUtility.prepareErrorResponseWithoutData("Feature Id is missing", "Feature Id is missing");
			}
			for(String featureId : featureIds.split(",")){
				try{
					featureIdList.add(Integer.valueOf(featureId));
				}catch(Exception e){
					featureIdList = null;
				}
			}if(featureIdList == null){
				return RestResponseUtility.prepareErrorResponseWithoutData("Invalid feature Id", "Invalid feature Id");
			}
			for(Integer featureId : featureIdList){
				ProductFeature feature = productListService.getByProductFeatureId(featureId);
				inputFeaturesList.add(feature);
			}
			if (inputFeaturesList == null || inputFeaturesList.contains(null)) {
				return RestResponseUtility.prepareErrorResponseWithoutData("feature Id is invalid", "feature Id is invalid");
			}
			if((String) jsonFormatObject.get("productBuildId") != null){
				try {
					productBuildId = Integer.valueOf((String) jsonFormatObject.get("productBuildId"));
				} catch (Exception er) {
					productBuildId = -1;
				}
			}
			ProductBuild build = productListService.getProductBuildById(productBuildId, 0);
			if (build == null) {
				return RestResponseUtility.prepareErrorResponseWithoutData("Build Id is missing or invalid","Build Id is missing or invalid");
			}

			action = (String) jsonFormatObject.get("action");
			if (action == null || (!action.equalsIgnoreCase("map") && !action.equalsIgnoreCase("unmap"))) {
				return RestResponseUtility.prepareErrorResponseWithoutData("action is missing or invalid.Valid values are map/unmap","action is missing or invalid.Valid values are map/unmap");
			}
			
			//Check authorization for user
			authResponse = checkUserAuthorization(build.getProductVersion().getProductMaster().getProductId(), userName, "/testExecution/executetestplan");
			if (authResponse != null)
				return authResponse;
			featureBuildList = productListService.getFeatureListByProductIdAndVersionIdAndBuild(build.getProductMaster().getProductId(),0,productBuildId,1, 0, 10000);
			for(ProductFeature inputfeature : inputFeaturesList){
				JSONObject message = new JSONObject();
				if(action.equalsIgnoreCase("map") && featureBuildList.contains(inputfeature)){
					mappedFeaturesList.add(inputfeature);
					message.put("Error", inputfeature.getProductFeatureName()+" Id : "+inputfeature.getProductFeatureId() +" is already mapped");
				}else if((action.equalsIgnoreCase("unmap") && !featureBuildList.contains(inputfeature))){
					mappedFeaturesList.add(inputfeature);
					message.put("Error", inputfeature.getProductFeatureName()+" Id : "+inputfeature.getProductFeatureId() +" is not available to unmap");
				}else{
					if(action.equalsIgnoreCase("map"))
						message.put("OK", inputfeature.getProductFeatureName()+" Id : "+inputfeature.getProductFeatureId() +" is mapped");
					else
						message.put("OK", inputfeature.getProductFeatureName()+" Id : "+inputfeature.getProductFeatureId() +" is unmapped");
				}
				arr.put(message);
			}
			inputFeaturesList.removeAll(mappedFeaturesList);
			productListService.mapFeaturesToBuild(build, inputFeaturesList, action);
			if(action.equalsIgnoreCase("map")) {
				return RestResponseUtility.prepareSuccessResponseWithJSONArray("Features mapped to Product Build", arr);
			}else{
				return RestResponseUtility.prepareSuccessResponseWithJSONArray("Features unmapped to Product Build", arr);
			}
		} catch(Exception e)  {			
			try {
				return RestResponseUtility.prepareErrorResponseWithoutData("Error mapping Features to Product Build", "Error mapping Features to Product Build");
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			return Response.noContent().build();
		}
	}
	
	
	@POST
	@Path("/testManagement/query/getProductTypes")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response getProductTypes(String nodeRedTafJSONQuery) throws JSONException {
		SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
		
		JSONArray arr = new JSONArray();
		ObjectMapper mapper = new ObjectMapper();
		JSONParser parser = new JSONParser();
		try {
			if(nodeRedTafJSONQuery == null || nodeRedTafJSONQuery.trim().isEmpty()) {
				log.info("no input data : /testManagement/query/getProductTypes");
				return RestResponseUtility.prepareErrorResponseWithoutData("No inputs provided", "No inputs provided");
			}
			org.json.simple.JSONObject jsonFormatObject = (org.json.simple.JSONObject) parser.parse(nodeRedTafJSONQuery);
			if(jsonFormatObject == null) {
				log.info("Could not parse request : /testManagement/query/mapUnMapFeaturesToProductBuild");
				return RestResponseUtility.prepareErrorResponseWithoutData("No inputs provided", "No inputs provided");
			}
			//Authenticate the user
			String userName=jsonFormatObject.get("user") != null ? jsonFormatObject.get("user").toString():"";
			Response authResponse = authenticateUser(userName, "/testManagement/query/getProductTypes");
			if (authResponse != null)
				return authResponse;
			
			List<ProductType> productTypes = productListService.listProductTyper();
			if(productTypes != null && productTypes.size() >0) {
				for(ProductType pt : productTypes){
					JSONObject object = new JSONObject(mapper.writeValueAsString(new JsonProductType(pt)));
					arr.put(object);
				}
			}
			return RestResponseUtility.prepareSuccessResponseWithJSONArray("Listing the Product Types", arr);
		}catch(Exception e) {
			return RestResponseUtility.prepareErrorResponseWithoutData("Error in product type list", "Error in product type list");
		}
	}
	
	
	@POST
	@Path("/testManagement/query/getTestcaseTypes")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response getTestcaseTypes(String nodeRedTafJSONQuery) throws JSONException {
		SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
		
		JSONArray arr = new JSONArray();
		ObjectMapper mapper = new ObjectMapper();
		try {
			if(nodeRedTafJSONQuery == null || nodeRedTafJSONQuery.trim().isEmpty()) {
				log.info("no input data : /testManagement/query/getTestcaseTypes");
				return RestResponseUtility.prepareErrorResponseWithoutData("No inputs provided", "No inputs provided");
			}
			JSONParser parser = new JSONParser();
			org.json.simple.JSONObject jsonFormatObject = (org.json.simple.JSONObject) parser.parse(nodeRedTafJSONQuery);
			if(jsonFormatObject == null) {
				log.info("Could not parse request : /testManagement/query/getTestcaseTypes");
				return RestResponseUtility.prepareErrorResponseWithoutData("No inputs provided", "No inputs provided");
			}
			//Authenticate the user
			String userName=jsonFormatObject.get("user") != null ? jsonFormatObject.get("user").toString():"";
			Response authResponse = authenticateUser(userName, "/testManagement/query/getProductTypes");
			if (authResponse != null)
				return authResponse;
			List<TestcaseTypeMaster> testcaseTypeMasters = testCaseService.listTestcaseTypeMaster();
		
			if(testcaseTypeMasters != null && testcaseTypeMasters.size() >0) {
				for(TestcaseTypeMaster ttm : testcaseTypeMasters){
					JSONObject object = new JSONObject(mapper.writeValueAsString(new JsonTestcaseTypeMaster(ttm)));
					arr.put(object);
				}
			}
			return RestResponseUtility.prepareSuccessResponseWithJSONArray("Listing the TestCase Types", arr);
		}catch(Exception e) {
			return RestResponseUtility.prepareErrorResponseWithoutData("Error in Testcase type list", "Error in Testcase type list");
		}
	}
	
	
	@POST
	@Path("/testManagement/query/getPriortyList")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response getPrioritiesMaster(String nodeRedTafJSONQuery) throws JSONException {
		SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
		
		JSONArray arr = new JSONArray();
		ObjectMapper mapper = new ObjectMapper();
		try {
			if(nodeRedTafJSONQuery == null || nodeRedTafJSONQuery.trim().isEmpty()) {
				log.info("no input data : /testManagement/query/getPriortyList");
				return RestResponseUtility.prepareErrorResponseWithoutData("No inputs provided", "No inputs provided");
			}
			JSONParser parser = new JSONParser();
			org.json.simple.JSONObject jsonFormatObject = (org.json.simple.JSONObject) parser.parse(nodeRedTafJSONQuery);
		if(jsonFormatObject == null) {
			log.info("Could not parse request : /testManagement/query/getPriortyList");
			return RestResponseUtility.prepareErrorResponseWithoutData("No inputs provided", "No inputs provided");
		}
		//Authenticate the user
		String userName=jsonFormatObject.get("user") != null ? jsonFormatObject.get("user").toString():"";
		Response authResponse = authenticateUser(userName, "/testManagement/query/getProductTypes");
		if (authResponse != null)
			return authResponse;
			
			List<TestCasePriority> priorityList=productListService.listFeatureExecutionPriority();
		
			if(priorityList !=null && priorityList.size() >0) {
				for(TestCasePriority priority: priorityList){
					JSONObject object = new JSONObject(mapper.writeValueAsString(new JsonTestCasePriority(priority)));
					arr.put(object);
				}
			}
			return RestResponseUtility.prepareSuccessResponseWithJSONArray( "Listing the Priority Types", arr);
		}catch(Exception e) {
			return RestResponseUtility.prepareErrorResponseWithoutData("Error in priority list", "Error in priority list");
		}
	}
	
	
	@POST
	@Path("/testManagement/query/getWorkpackageExecutionStatusList")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response getWorkpackageExecutionStatusList(String nodeRedTafJSONQuery) throws JSONException {
		SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
		JSONObject responseJson = new JSONObject();
		JSONArray arr = new JSONArray();
		ObjectMapper mapper = new ObjectMapper();
		try {
			if(nodeRedTafJSONQuery == null || nodeRedTafJSONQuery.trim().isEmpty()) {
				log.info("no input data : /testManagement/query/getWorkpackageExecutionStatusList");
				return RestResponseUtility.prepareErrorResponseWithoutData("No inputs provided", "No inputs provided");
			}
			JSONParser parser = new JSONParser();
			org.json.simple.JSONObject jsonFormatObject = (org.json.simple.JSONObject) parser.parse(nodeRedTafJSONQuery);
		if(jsonFormatObject == null) {
			log.info("Could not parse request : /testManagement/query/getWorkpackageExecutionStatusList");
			return RestResponseUtility.prepareErrorResponseWithoutData("No inputs provided", "No inputs provided");
		}
		//Authenticate the user
		String userName=jsonFormatObject.get("user") != null ? jsonFormatObject.get("user").toString():"";
		Response authResponse = authenticateUser(userName, "/testManagement/query/getWorkpackageExecutionStatusList");
		if (authResponse != null)
			return authResponse;
			
			int entityType=2;// Entity master WorkpackgeId
			List<WorkFlow> workFlowList=workPackageService.getworkFlowListByEntity(entityType);
			if(workFlowList != null && workFlowList.size() >0) {
				for(WorkFlow workFlow: workFlowList){
					JSONObject object = new JSONObject(mapper.writeValueAsString(new JsonWorkFlow(workFlow)));
					arr.put(object);
				}	
		
			}
			return RestResponseUtility.prepareSuccessResponseWithJSONArray("Listing Workpackage Execution Types", arr);
		}catch(Exception e) {
			return RestResponseUtility.prepareErrorResponseWithoutData("Error in Workpackage execution status list", "Error in Workpackage execution status list");
		}
	}
	
	@POST
	@Path("/testManagement/query/getProductDetailsByUser")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response getProductDetailsByUser(String userFormData) throws JSONException {
	SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
	
	JSONArray arr = new JSONArray();
	ObjectMapper mapper = new ObjectMapper();
	try {
		
		JSONParser parser = new JSONParser();
		org.json.simple.JSONObject jsonFormatObject = (org.json.simple.JSONObject) parser.parse(userFormData);
		if(jsonFormatObject == null) {
			log.info("Could not parse request : /map user to Product");
			return RestResponseUtility.prepareErrorResponseWithoutData("No inputs provided", "No inputs provided");
		}
		
		String user=(String)jsonFormatObject.get("user");
		
		if(user ==null ||user.trim().isEmpty()) {
			return RestResponseUtility.prepareErrorResponseWithoutData("User is required","User is required");
		}
		if(user != null && !user.trim().isEmpty()) {
			
			UserList userList=userListService.getUserByLoginId(user);
			
			if(userList == null) {
				return RestResponseUtility.prepareErrorResponseWithoutData("Invalid User","Invalid User");
			}
			List<ProductMaster> products= productMasterDAO.getProductDetailsByUserId(userList.getUserId());
			if(products == null || products.size() == 0) {
				return RestResponseUtility.prepareErrorResponseWithoutData("User not mapped for the given product","User not mapped for the given product");
			}
			for(ProductMaster product:products) {
				JSONObject object = new JsonProductMaster(product).getCleanJson();
				arr.put(object);
			}
		}
		return RestResponseUtility.prepareSuccessResponseWithJSONArray("Listing mapped User Product details ", arr);
	
		}catch(Exception e) {
			return RestResponseUtility.prepareErrorResponseWithoutData("Error in listing mapped User Product detailst","Error in listing mapped User Product detailst");
		}
	 }
	
	@POST
	@Path("/testExecution/query/notification/sendWorkpackageTestReportByMail")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response sendWorkpackageCompletedReport(String workpackageIdJson) throws IOException, JSONException{	
		
		try{
			SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);		
			Integer wpId = null;		
			JTableResponse jTableResponse = null;
			WorkPackage workPackage = null;
			JSONParser parser = new JSONParser();
			org.json.simple.JSONObject jsonFormatObject = (org.json.simple.JSONObject) parser.parse(workpackageIdJson);
			if(jsonFormatObject == null) {
				return RestResponseUtility.prepareErrorResponseWithoutData("No inputs provided", "No inputs provided");
			}
			
			//Authenticate the user
			String userName=jsonFormatObject.get("user") != null ? jsonFormatObject.get("user").toString():"";
			Response authResponse = authenticateUser(userName, "/testExecution/sendWorkpackageTestReportByMail");
			if (authResponse != null)
				return authResponse;
			
			String toRecipients="";
			if(jsonFormatObject.get("toRecipients") != null && jsonFormatObject.get("toRecipients").toString().trim() != "") {
				toRecipients=(String)jsonFormatObject.get("toRecipients");
			} else {
				return RestResponseUtility.prepareErrorResponseWithoutData("To Recipients required", "To Recipients required");
			}
			String ccRecipients="";
			if(jsonFormatObject.get("ccRecipients") != null && jsonFormatObject.get("ccRecipients").toString().trim() != "") {
				ccRecipients=(String)jsonFormatObject.get("ccRecipients");
			}
			if((String) jsonFormatObject.get("workpackageId") != null){
				try{
					wpId = Integer.valueOf((String) jsonFormatObject.get("workpackageId"));
				}catch (Exception e){
					return RestResponseUtility.prepareErrorResponseWithoutData("Workpackage Id not specified. It is mandatory", "Workpackage Id not specified. It is mandatory");
					//wpId = -1;
				}
				
				workPackage = workPackageService.getWorkPackageById(wpId);
				
				
				if(null != workPackage){
					//Check authorization for user
					authResponse = checkUserAuthorization(workPackage.getProductBuild().getProductVersion().getProductMaster().getProductId(), userName, "/testExecution/sendWorkpackageTestReportByMail");
					if (authResponse != null)
						return authResponse;
					if(null != workPackage.getWorkFlowEvent()){
						if(null != workPackage.getWorkFlowEvent().getWorkFlow() && null != workPackage.getWorkFlowEvent().getWorkFlow().getStageId()){
							if(null != workPackage.getWorkFlowEvent().getWorkFlow().getEntityMaster()){
								if(null != workPackage.getWorkFlowEvent().getWorkFlow().getEntityMaster().getEntitymasterid()){
									if(workPackage.getWorkFlowEvent().getWorkFlow().getEntityMaster().getEntitymasterid() == IDPAConstants.WORKPACKAGE_ENTITY_ID){
										log.info("Sending Workpackage status to email");							
										String result =  emailService.sendWorkpackageCompletedReport(workPackage,toRecipients,ccRecipients);
										if (result == null || result.startsWith("FAIL")) {
											return RestResponseUtility.prepareErrorResponseWithoutData(result, result);
										} 
										log.info("Sent Workpackage status to email");				
									}							
								}
							}						
						}					
					}				
				} else {
					return RestResponseUtility.prepareErrorResponseWithoutData("Workpackage Id is not a valid one : " + wpId, "Workpackage Id is not a valid one : " + wpId);
				}
			} else {
				return RestResponseUtility.prepareErrorResponseWithoutData("Workpackage Id not specified. It is mandatory","Workpackage Id not specified. It is mandatory");
			}
			return RestResponseUtility.prepareSuccessResponse("Sent Workpackage status report through mail", "");
	        
		} catch(Exception e)  {	
			return RestResponseUtility.prepareErrorResponseWithoutData( "Unable to send workpackage report by email : " + e.getMessage().toString(),  "Unable to send workpackage report by email : " + e.getMessage().toString());
		}
	}
	
	private Response checkUserAuthorization(Integer productId, String userName, String serviceName) {
		
		boolean isProductAuthorizedForUser=false;
		JSONObject responseJson = new JSONObject();
		UserList user = userListService.getUserByLoginId(userName);
		try {
			if (atlasUserAuthenticationRequired != null && atlasUserAuthenticationRequired.equalsIgnoreCase("yes")) {
			
				isProductAuthorizedForUser = productListService.isUserPermissionByProductIdandUserId(productId, user.getUserId(), user.getUserRoleMaster().getUserRoleId());
				if (!isProductAuthorizedForUser) {
					
					log.info("REST service : " + serviceName + " : User : " + user.getLoginId() + " is not authorized for product Id : " + productId);
					return RestResponseUtility.prepareErrorResponseWithoutData("User : " + user.getLoginId() + " is not authorized for product Id : " + productId, "User : " + user.getLoginId() + " is not authorized for product Id : " + productId);
				}
			}
		}catch(Exception e) {
			log.error("Problem while authorizing user : " + user.getLoginId(), e);
			try {
				return RestResponseUtility.prepareErrorResponseWithoutData("REST service : " + serviceName + "Problem while authorizing user : " + user.getLoginId(), "REST service : " + serviceName + "Problem while authorizing user : " + user.getLoginId());
			} catch (Exception r) {
				log.error("Problem while authorizing user : " + user.getLoginId(), e);
			}
		}
		return null;
	}
	
	private Response authenticateUser(String userName, String serviceName) {
		
		JSONObject responseJson = new JSONObject();
		try {
			UserList userList = null;
			if (atlasUserAuthenticationRequired != null && atlasUserAuthenticationRequired.equalsIgnoreCase("yes")) {
				if(userName == null || userName.trim().isEmpty()) {
					log.info("REST service : " + serviceName + " : Registered user name is missing. It is mandatory");
					return RestResponseUtility.prepareErrorResponseWithoutData("User is required", "Registered user name is missing. It is mandatory");
				} else {
					userList = userListService.getUserByLoginId(userName);
					if (userList == null) {
						log.info("REST service : " + serviceName + " : Username is not a registered user : " + userName);
						return RestResponseUtility.prepareErrorResponseWithoutData( "User is invalid", "User name specified : "  + userName + "  is invalid");
					}
				}
				log.info("REST service : " + serviceName + " : Username is a valid user : " + userName);
				return null;
			}
		} catch (Exception e) {
			log.error("Problem while validating user : " + userName, e);
			try {
				return RestResponseUtility.prepareErrorResponseWithoutData("REST service : " + serviceName + "Problem while validating user : " + userName, "REST service : " + serviceName + "Problem while validating user : " + userName);
			} catch (Exception r) {
				log.error("Problem while validating user : " + userName, e);
			}
		}
		return null;
	}
	
	@POST
	@Path("/testExecution/query/workpackagesummarybytestplan")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response getWorkpackageSummaryByTestPlan(String nodeRedTafJSONQuery) {

		SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);	
		Integer testPlanId = null;
		TestRunPlan testPlan = null;
		List<JsonWorkPackageTestCaseExecutionPlanForTester> jsonWPTCEP = new ArrayList<JsonWorkPackageTestCaseExecutionPlanForTester>();
		JSONArray arr = new JSONArray();
		ObjectMapper mapper = new ObjectMapper();
		Integer productId = -1;
		List<ProductMaster> products= new ArrayList<>();
		Integer testFactoryId = null;
		Integer productBuildId = null; 
		ProductBuild productBuild = null;
		String filter = "MONTH";
		WorkPackage wp = null;
		Integer wpId = null;
		try{
			JSONParser parser = new JSONParser();
			org.json.simple.JSONObject jsonFormatObject = (org.json.simple.JSONObject) parser.parse(nodeRedTafJSONQuery);
			JSONObject obj = new JSONObject();
			if(jsonFormatObject == null) {
				return RestResponseUtility.prepareErrorResponseWithoutData("No inputs provided", "No inputs provided");
			}
			
			//Authenticate the user
			String userName=jsonFormatObject.get("user") != null ? jsonFormatObject.get("user").toString():"";
			Response authResponse = authenticateUser(userName, "/testExecution/query/workpackagesummarybytestplan");
			if (authResponse != null)
				return authResponse;
			
			if((String) jsonFormatObject.get("testPlanId") != null){
				try {
					testPlanId = Integer.valueOf((String) jsonFormatObject.get("testPlanId"));
				} catch (Exception er) {
					testPlanId = -1;
				}
				log.info("Test Plan Id : " +testPlanId);
				testPlan = productListService.getTestRunPlanBytestRunPlanId(testPlanId);
			}
			if (testPlan == null) {
				return RestResponseUtility.prepareErrorResponseWithoutData("TestPlan ID is missing or invalid", "TestPlan ID is missing or invalid");
			}
			if((String) jsonFormatObject.get("productBuildId") != null){
				try {
					productBuildId = Integer.valueOf((String) jsonFormatObject.get("productBuildId"));
				} catch (Exception er) {
					productBuildId = -1;
				}
				log.info("Product Build Id : " +productBuildId);
				productBuild = productListService.getProductBuildById(productBuildId,0);
			}
			if(productBuild == null){
				return RestResponseUtility.prepareErrorResponseWithoutData("ProductBuild ID is missing or invalid", "ProductBuild ID is missing or invalid");
			}

			//Get the latest workpackage for the given testPlanId
			wp = workPackageService.getworkpackageByTestPlanId(testPlanId, productBuildId);
			if(wp == null){
				return RestResponseUtility.prepareErrorResponseWithoutData("There are no workpackages available", "There are no workpackages available");
			}
			wpId = wp.getWorkPackageId();
			if(testPlan.getProductBuild() != null && testPlan.getProductBuild().getProductVersion() != null && testPlan.getProductBuild().getProductVersion().getProductMaster() != null)
				productId = testPlan.getProductBuild().getProductVersion().getProductMaster().getProductId();
			
			/*if(testPlan.getProductBuild() != null) 
				productBuildId = testPlan.getProductBuild().getProductBuildId();*/
			
			WorkFlowEvent wpEvent = wp.getWorkFlowEvent();
			if(wpEvent != null) {
				//sb.append("Workpackage ID : " +wpId+" : Name : "+wp.getName() + " : Current execution status : "+ wpEvent.getRemarks());
				obj.put("wpId", wp.getWorkPackageId());
				obj.put("workPackageName", wp.getName());
				obj.put("wpStatus", wpEvent.getRemarks());
				//arr.put(obj);
			}						
			
			//Check authorization for user
			authResponse = checkUserAuthorization(productId, userName, "/testExecution/query/workpackageExecutionStatus");
			if (authResponse != null)
				return authResponse;
			
			if(jsonFormatObject.get("testFactoryId") != null){
				if((String) jsonFormatObject.get("testFactoryId") != null)
					testFactoryId = Integer.valueOf((String) jsonFormatObject.get("testFactoryId"));					
			}
			
			if(jsonFormatObject.get("filter") != null){
				filter = (String) jsonFormatObject.get("filter");				
			}
						
			if(testFactoryId == null) {
				testFactoryId = -1;					
			}
			
			if(productBuildId == null)
				productBuildId = -1;
			
			if(testFactoryId != null && testFactoryId > 0 || productId != null && productId > 0 || productBuildId != null && productBuildId > 0){
				jsonWPTCEP = workPackageService.getWPTCExecutionSummaryByProdIdBuildIdWorkpackageId(testFactoryId,productId, productBuildId,wpId,-1, -1, filter);	
			}	
			
			
			if(productId ==-1)  {
				UserList userList = userListService.getUserByLoginId(userName);
				products= productMasterDAO.getProductDetailsByUserId(userList.getUserId());
				if(products == null || products.size() == 0) {
					return RestResponseUtility.prepareErrorResponseWithoutData("REST service : /testExecution/query/workpackageExecutionStatus Problem while authorizing user : " + userList.getLoginId(), "REST service : /testExecution/query/workpackageExecutionStatus Problem while authorizing user : " + userList.getLoginId());
				}
				for(ProductMaster product:products) {
					jsonWPTCEP.addAll(workPackageService.getWPTCExecutionSummaryByProdIdBuildIdWorkpackageId(product.getTestFactory().getTestFactoryId(),product.getProductId(), productBuildId,wpId,-1, -1, filter));	
				}
			}
			
			for(JsonWorkPackageTestCaseExecutionPlanForTester js : jsonWPTCEP) {
				if(js != null && js.getWorkPackageId().equals(wpId)) {
					List<JsonWorkPackageTestCaseExecutionPlanForTester> jj = new ArrayList<JsonWorkPackageTestCaseExecutionPlanForTester>();
					js.setNotExecuted(js.getTotalWPTestCase() - (js.getP2totalPass() + js.getP2totalFail() ));
					js.setP2totalNoRun(js.getTotalWPTestCase() - (js.getP2totalPass() + js.getP2totalFail()));
					String firstActualExecutionDate = "NA";
					if(wp.getCreateDate() != null && wp.getCreateDate().toString().contains(" ")){
						String createdDate = wp.getCreateDate().toString();
						firstActualExecutionDate = createdDate.split(" ")[0];
					}
					js.setFirstActualExecutionDate(firstActualExecutionDate);
					jj.add(js);
					JSONObject object = new JSONObject(mapper.writeValueAsString(js));
					for(String key : JSONObject.getNames(obj)){
						object.put(key, obj.get(key));
					}
					
					arr.put(object);
					break;
				}
			}
			
			List<JsonWorkPackageTestCaseExecutionSummary> jsonWPTC = atlasService.listTestJobsWorkpackageSummary(wpId, productBuildId);	
			JSONArray jobArr = new JSONArray();
			
			if(jsonWPTC != null && !jsonWPTC.isEmpty()) {
				for(JsonWorkPackageTestCaseExecutionSummary js : jsonWPTC) {					
					JSONObject object = new JSONObject(mapper.writeValueAsString(js));					
					jobArr.put(object);
				}
			} 
			
			log.info("Response : "+arr);
			return RestResponseUtility.prepareSuccessResponseWithRecords("Recieved WorkPackage execution status for the workpackage : "+wpId, "", jobArr, arr);
			
		} catch(Exception e)  {		
			log.error("Error in retrieving Workpackage Execution Status for the WP : "+ wp.getWorkPackageId() ,e);
			try {
				return RestResponseUtility.prepareErrorResponseWithoutData("Failed to get report", "Failed to get report");
				
			} catch (Exception e1) {
				log.error("Error in retrieving Workpackage Execution Status for the WP : "+ wp.getWorkPackageId(),e1);
			}
			return Response.noContent().build();
		}
	}
		
	
	@POST
	@Path("/testExecution/query/recentProductBuildsformwc")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response getRecentProductBuildsForMWC(String nodeRedTafJSONQuery) {

		log.info(this.getClass().getName()+": recentProductBuildsformwc : " + "enter");
		SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);	
		String filter = "WEEK";
		JSONArray arr = new JSONArray();
		JTableResponse jTableResponse = null;
		ObjectMapper mapper = new ObjectMapper();
		WorkPackage wp = null;
		String result = "N/A";
		WorkPackage sanityWP = null;
		WorkPackage functionalWP = null;
		WorkPackage integrationWP = null;
		try{
			log.info(this.getClass().getName()+": recentProductBuildsformwc : " + "Before parsing");
			JSONParser parser = new JSONParser();
			org.json.simple.JSONObject jsonFormatObject = (org.json.simple.JSONObject) parser.parse(nodeRedTafJSONQuery);	
			if(jsonFormatObject == null) {
				log.info("Could not parse request : /testExecution/query/recentProductBuildsformwc");
				log.info(this.getClass().getName()+": recentProductBuildsformwc : " + "exit : No inputs provided");
				return RestResponseUtility.prepareErrorResponseWithoutData("No inputs provided", "No inputs provided");
			}
			//Authenticate the user
			String userName=jsonFormatObject.get("user") != null ? jsonFormatObject.get("user").toString():"";
			Response authResponse = authenticateUser(userName, "/testExecution/query/recentProductBuildsformwc");
			if (authResponse != null)
				return authResponse;
			
			if(jsonFormatObject.get("filter") != null) {
				filter = (String) jsonFormatObject.get("filter");
			}
			if (filter == null || filter.isEmpty() || (!filter.equalsIgnoreCase("Day") && !filter.equalsIgnoreCase("Week") && !filter.equalsIgnoreCase("Month"))) {
				log.info(this.getClass().getName()+": recentProductBuildsformwc : " + "exit : filter is missing or invalid");
				return RestResponseUtility.prepareErrorResponseWithoutData("filter is missing or invalid", "filter is missing or invalid");
			}
			
			UserList userList=userListService.getUserByLoginId(userName);
			
			List<JsonProductBuild> jsonProductBuildList= new ArrayList<JsonProductBuild>();
			if(userList != null) {
				jsonProductBuildList = dataTreeService.getProductBuildDetails(userList.getUserRoleMaster().getUserRoleId(), userList.getUserId(), filter);
			}else {
				jsonProductBuildList = dataTreeService.getProductBuildDetails(1, 1, filter);
			}
			for(JsonProductBuild pb : jsonProductBuildList) {
				JSONObject jsonpb = new JSONObject(mapper.writeValueAsString(pb));
				Integer productBuildTestedCount = workPackageService.getProductBuildsTestedCount(pb.getProductBuildId());
				jsonpb.put("build_tested_count", productBuildTestedCount);
				//Obtain product type for the product build
				ProductMaster product = productListService.getProductById(pb.getProductId());
				if(product != null && product.getProductType() != null && product.getProductType().getTypeName() != null)
					jsonpb.put("product_type", product.getProductType().getTypeName());
				else
					jsonpb.put("product_type", "N/A");
				
				//Get the total features mapped to the productbuild
				List<ProductFeatureProductBuildMapping> productFeatureBuildMappedList=productListService.getProductFeatureAndProductBuildMappingByVersionIdOrBuildId(product.getProductId(),pb.getProductVersionListId(),pb.getProductBuildId());
				if(productFeatureBuildMappedList != null && !productFeatureBuildMappedList.isEmpty()){
					jsonpb.put("mappedFeatureCount", productFeatureBuildMappedList.size());
				}else
					jsonpb.put("mappedFeatureCount", 0);
				Integer totalFeaturesCount = productListService.getFeatureListSize(pb.getProductId());
				jsonpb.put("totalFeaturesCount", totalFeaturesCount);
				String sanityStatus = "";
				String functionalStatus = "";
				String integrationStatus  = "";
				String testPlanName = "";
				//Get the status based on the testPlan
				log.info("recentProductBuildsformwc : Performance check : Before getting workpackage");
				wp = workPackageService.getWorkPackageByProductBuild(pb.getProductBuildId());
				if(wp != null){
					log.info("recentProductBuildsformwc : Performance check : After getting workpackage");
					List<JsonWorkPackageTestCaseExecutionPlanForTester> jsonWPTCEPList = workPackageService.getWPTCExecutionSummaryByProdIdBuildIdWorkpackageId(-1,product.getProductId(), pb.getProductBuildId(),wp.getWorkPackageId(),-1, -1, filter);
					log.info("recentProductBuildsformwc : Performance check : After getting jsonWPTCEP");
					if(jsonWPTCEPList != null && !jsonWPTCEPList.isEmpty()){
						JsonWorkPackageTestCaseExecutionPlanForTester jsonWPTCEP = jsonWPTCEPList.get(0);
						result = jsonWPTCEP.getResult();
					}
					testPlanName = wp.getTestRunPlan().getTestRunPlanName();
				}
				if(testPlanName.contains("Sanity"))
					testPlanName = new String("Sanity Testing");
				else if(testPlanName.contains("Functional"))
					testPlanName = new String("Functional Testing");
				else if (testPlanName.contains("Integration"))
					testPlanName = new String("Integration Testing");
				else{
					testPlanName = new String("New");
					result = "N/A";
				}
				jsonpb.put("stage", testPlanName);
				jsonpb.put("result", result);
				jsonpb.put("execution_status", testPlanName + "~" + result);
				//Get the latest testcase execution summary for sanity test plan 
				TestRunPlan SanityTestPlan = productListService.getTestRunPlanBytestRunPlanNameAndProductBuild("Sanity", -1);
				if(SanityTestPlan != null){
					sanityWP = workPackageService.getworkpackageByTestPlanId(SanityTestPlan.getTestRunPlanId(), pb.getProductBuildId()); 
				}
				if(sanityWP != null){
					List<JsonWorkPackageTestCaseExecutionPlanForTester> jsonWPTCEPList = workPackageService.getWPTCExecutionSummaryByProdIdBuildIdWorkpackageId(-1,product.getProductId(), pb.getProductBuildId(),sanityWP.getWorkPackageId(),-1, -1, filter);
					if(jsonWPTCEPList != null && !jsonWPTCEPList.isEmpty()){
						JsonWorkPackageTestCaseExecutionPlanForTester jsonWPTCEP = (JsonWorkPackageTestCaseExecutionPlanForTester)jsonWPTCEPList.get(0);
						result = jsonWPTCEP.getResult();
						if(result.equalsIgnoreCase("---")){
							result = "In Progress";
						}
						sanityStatus = result+"~"+jsonWPTCEP.getP2totalPass()+"~"+jsonWPTCEP.getP2totalFail()+"~"+jsonWPTCEP.getP2totalNoRun()+"~"+jsonWPTCEP.getTotalWPTestCase();
						/*if(result.equalsIgnoreCase("Passed"))
							sanityStatus = jsonWPTCEP.getP2totalPass()+ "/" + jsonWPTCEP.getTotalWPTestCase();
						else
							sanityStatus = jsonWPTCEP.getP2totalFail() + jsonWPTCEP.getP2totalNoRun() + "/" + jsonWPTCEP.getTotalWPTestCase();*/
					}
				}else
					sanityStatus = "N/A";

				//Get the latest testcase execution summary for Functional test plan 
				TestRunPlan funtionalTestPlan = productListService.getTestRunPlanBytestRunPlanNameAndProductBuild("Functional", -1);
				if(funtionalTestPlan != null){
					functionalWP = workPackageService.getworkpackageByTestPlanId(funtionalTestPlan.getTestRunPlanId(), pb.getProductBuildId()); 
				}
				if(functionalWP != null){
					List<JsonWorkPackageTestCaseExecutionPlanForTester> jsonWPTCEPList = workPackageService.getWPTCExecutionSummaryByProdIdBuildIdWorkpackageId(-1,product.getProductId(), pb.getProductBuildId(),functionalWP.getWorkPackageId(),-1, -1, filter);
					if(jsonWPTCEPList != null && !jsonWPTCEPList.isEmpty()){
						JsonWorkPackageTestCaseExecutionPlanForTester jsonWPTCEP = (JsonWorkPackageTestCaseExecutionPlanForTester)jsonWPTCEPList.get(0);
						result = jsonWPTCEP.getResult();
						if(result.equalsIgnoreCase("---")){
							result = "In Progress";
						}
						functionalStatus = result+"~"+jsonWPTCEP.getP2totalPass()+"~"+jsonWPTCEP.getP2totalFail()+"~"+jsonWPTCEP.getP2totalNoRun()+"~"+jsonWPTCEP.getTotalWPTestCase();
						/*if(result.equalsIgnoreCase("Passed"))
							functionalStatus = jsonWPTCEP.getP2totalPass()+ "/" + jsonWPTCEP.getTotalWPTestCase();
						else
							functionalStatus = jsonWPTCEP.getP2totalFail() + jsonWPTCEP.getP2totalNoRun() + "/" + jsonWPTCEP.getTotalWPTestCase();*/
					}
				}else
					functionalStatus = "N/A";
				//Get the latest testcase execution summary for Integration test plan 
				TestRunPlan integrationTestPlan = productListService.getTestRunPlanBytestRunPlanNameAndProductBuild("Integration", -1);
				if(integrationTestPlan != null){
					integrationWP = workPackageService.getworkpackageByTestPlanId(integrationTestPlan.getTestRunPlanId(), pb.getProductBuildId()); 
				}
				if(integrationWP != null){
					List<JsonWorkPackageTestCaseExecutionPlanForTester> jsonWPTCEPList = workPackageService.getWPTCExecutionSummaryByProdIdBuildIdWorkpackageId(-1,product.getProductId(), pb.getProductBuildId(),integrationWP.getWorkPackageId(),-1, -1, filter);
					if(jsonWPTCEPList != null && !jsonWPTCEPList.isEmpty()){
						JsonWorkPackageTestCaseExecutionPlanForTester jsonWPTCEP = (JsonWorkPackageTestCaseExecutionPlanForTester)jsonWPTCEPList.get(0);
						result = jsonWPTCEP.getResult();
						if(result.equalsIgnoreCase("---")){
							result = "In Progress";
						}
						integrationStatus = result+"~"+jsonWPTCEP.getP2totalPass()+"~"+jsonWPTCEP.getP2totalFail()+"~"+jsonWPTCEP.getP2totalNoRun()+"~"+jsonWPTCEP.getTotalWPTestCase();
						/*if(result.equalsIgnoreCase("Passed"))
							integrationStatus = jsonWPTCEP.getP2totalPass()+ "/" + jsonWPTCEP.getTotalWPTestCase();
						else
							integrationStatus = jsonWPTCEP.getP2totalFail() + jsonWPTCEP.getP2totalNoRun() + "/" + jsonWPTCEP.getTotalWPTestCase();*/
					}
				}else
					integrationStatus = "N/A";
				
				jsonpb.put("sanityStatus", sanityStatus);
				jsonpb.put("functionalStatus", functionalStatus);
				jsonpb.put("integrationStatus", integrationStatus);
				
				if(userList !=null && userName != "" && product != null ){
					boolean isProductAuthorizedForUser = productListService.isProductUserRoleExits(product.getProductId(), userList.getUserId(), userList.getUserRoleMaster().getUserRoleId());
					if(isProductAuthorizedForUser) {
						arr.put(jsonpb);
					}
				} else {
					arr.put(jsonpb);
				}
			}
			
			log.info("Recent product builds : "+arr);
			log.info(this.getClass().getName()+": recentProductBuilds : " + "exit : returned response");
			return RestResponseUtility.prepareSuccessResponseWithJSONArray("Recent Product Build listing success.", arr);
		} catch(Exception e)  {			
			try {
				log.info("Error Listing recent product builds"+e);
				return RestResponseUtility.prepareErrorResponseWithoutData("Error Listing recent product builds", "Error Listing recent product builds");
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			log.info(this.getClass().getName()+": recentProductBuilds : " + "exit : Error Listing recent product build");
			return Response.noContent().build();
		}
		
	}
	
	@POST
	@Path("/testManagement/query/mapFeatureNameorIdToProductBuild")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response mapFeatureToProductBuildByIdorName(String nodeRedTafJSONQuery) throws JSONException {

		SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);	
		Integer productBuildId = 0;
		Integer featureId = 0;
		String featureName = null;
		Integer isMapped = 0;
		String action = null;
	
		ProductFeature feature = null;		
		ProductMaster product =null;
		boolean isFeatureAlreadyMapped = false;
		List<ProductFeature> featuresList =new ArrayList<ProductFeature>();
		List<ProductFeatureProductBuildMapping> featureBuildList =new ArrayList<ProductFeatureProductBuildMapping>();
		ObjectMapper mapper = new ObjectMapper();
		try{
			JSONParser parser = new JSONParser();
			org.json.simple.JSONObject jsonFormatObject = (org.json.simple.JSONObject) parser.parse(nodeRedTafJSONQuery);
			if(jsonFormatObject == null) {
				log.info("Could not parse request : /testManagement/query/mapFeatureNameorIdToProductBuild");
				return RestResponseUtility.prepareErrorResponseWithoutData("No inputs provided", "No inputs provided");
			}	
			
			//Authenticate the user
			String userName=jsonFormatObject.get("user") != null ? jsonFormatObject.get("user").toString():"";
			Response authResponse = authenticateUser(userName, "/testExecution/query/mapFeatureToProductBuild");
			if (authResponse != null)
				return authResponse;
			
			if((String) jsonFormatObject.get("productBuildId") != null){
				try {
					productBuildId = Integer.valueOf((String) jsonFormatObject.get("productBuildId"));
				} catch (Exception er) {
					productBuildId = -1;
				}
			}
			ProductBuild build = productListService.getProductBuildById(productBuildId, 0);
			if (build == null) {
				return RestResponseUtility.prepareErrorResponseWithoutData("Build Id is missing or invalid","Build Id is missing or invalid");
			}
			//If feature Id is present, then get the feature in TAF by ID else check if the feature name is available
			if((String) jsonFormatObject.get("featureId") != null){
				try {
					featureId = Integer.valueOf((String) jsonFormatObject.get("featureId"));
				} catch (Exception er) {
					featureId = -1;
				}
				feature = productListService.getByProductFeatureId(featureId);
			}else if((String) jsonFormatObject.get("featureName") != null){
				try {
					featureName = (String) jsonFormatObject.get("featureName");
				} catch (Exception er) {
					featureName = null;
				}
				feature = productListService.getByProductFeatureName(featureName);
				//If featureName is not available then,	Add a new feature to the product
				if(feature == null){
					feature = addFeature(featureName, userName, build);
				}
			}
			log.info("After getting feature");
			if (feature == null) {
				return RestResponseUtility.prepareErrorResponseWithoutData("feature Id/Name is missing or invalid", "feature Id/Name is missing or invalid");
			}
			
			if((String) jsonFormatObject.get("action") != null){
				try{
					action = (String) jsonFormatObject.get("action");
				}catch(Exception e){
					action = null;
				}
			}
			if (action == null || (!action.equalsIgnoreCase("map") && !action.equalsIgnoreCase("unmap"))) {
				return RestResponseUtility.prepareErrorResponseWithoutData("action is missing or invalid.Valid values are map/unmap","action is missing or invalid.Valid values are map/unmap");
			}
			if(build != null){
				product = build.getProductMaster();		
			}
			log.info("Before validating user");
			//Check authorization for user
			authResponse = checkUserAuthorization(product.getProductId(), userName, "/testExecution/query/mapFeatureToProductBuild");
			if (authResponse != null)
				return authResponse;
			
			featuresList = productListService.getFeatureListByProductId(product.getProductId(), 1, null, null);
			if(featuresList!= null && !featuresList.isEmpty() && !featuresList.contains(feature)){
				return RestResponseUtility.prepareErrorResponseWithoutData("No features available for the product","No features available for the product");
			}
			featureBuildList = productListService.getProductFeatureAndProductBuildMappingByVersionIdOrBuildId(product.getProductId(), -1, build.getProductBuildId());
			if(featureBuildList != null){
				for (ProductFeatureProductBuildMapping pf : featureBuildList){
					if(action.equalsIgnoreCase("map") && pf.getFeatureId().equals(feature.getProductFeatureId())){
						return RestResponseUtility.prepareErrorResponseWithoutData("Feature already mapped","Feature already mapped");
					}else if(action.equalsIgnoreCase("unmap") && pf.getFeatureId().equals(feature.getProductFeatureId())){
						isFeatureAlreadyMapped = true;
						break;
					}
				}
			}
			if(!isFeatureAlreadyMapped && action.equalsIgnoreCase("unmap")){
				return RestResponseUtility.prepareErrorResponseWithoutData("Feature is not associated to the given productBuild. Unmap failed","Feature is not associated to the given productBuild. Unmap failed");
			}
			
			if(!(build.getProductMaster().getProductId().equals(feature.getProductMaster().getProductId()))) {
				return RestResponseUtility.prepareErrorResponseWithoutData("BuildId : "+build.getProductBuildId() +" and FeatureId :"+feature.getProductFeatureId()+" is not associated to the same Product","BuildId : "+build.getProductBuildId() +" and FeatureId :"+feature.getProductFeatureId()+" is not associated to the same Product");
			}

			ProductFeatureProductBuildMapping productFeatureProductBuildMapping = new ProductFeatureProductBuildMapping();
			if(action.equalsIgnoreCase("map")) 
				productFeatureProductBuildMapping.setIsMapped(1);
			else
				productFeatureProductBuildMapping.setIsMapped(0);
			productFeatureProductBuildMapping.setBuildId(productBuildId);
			productFeatureProductBuildMapping.setFeatureId(feature.getProductFeatureId());
			productFeatureProductBuildMapping.setProduct(product);
			productFeatureProductBuildMapping.setCreatedDate(new Date());
			productFeatureProductBuildMapping.setModifiedDate(new Date());
			if(action.equalsIgnoreCase("map")) 
				productListService.mappingProductFeatureToProductBuild(productFeatureProductBuildMapping);
			else
				productListService.unMappingProductFeatureToProductBuild(productFeatureProductBuildMapping);
			log.info("Product Feature and Build mapping successfully");
			if(action.equalsIgnoreCase("map")) {
				return RestResponseUtility.prepareSuccessResponse("Feature mapped to Product Build", "");
			}else{
				return RestResponseUtility.prepareSuccessResponse("Feature unmapped to Product Build", "");
			}
		} catch(Exception e)  {			
			try {
				return RestResponseUtility.prepareErrorResponseWithoutData("Error mapping Product Feature and Product Build",  "Error mapping Product Feature and Product Build"+e);
			} catch (Exception e1) {
				log.error(e1);
			}
			return Response.noContent().build();
		}
	}
	private ProductFeature addFeature(String featureName, String userName,ProductBuild build) {
		StringBuffer responseMessage = new StringBuffer();
		ProductFeature feature= new ProductFeature();
		ProductFeature parentFeature = new ProductFeature();
		TestCasePriority testCasePriority = new TestCasePriority();
		feature.setProductMaster(build.getProductVersion().getProductMaster());
		feature.setProductFeatureName(featureName);
		feature.setProductFeatureCode(featureName);
		feature.setDisplayName(featureName);
		feature.setProductFeatureDescription(featureName);
		testCasePriority.setTestcasePriorityId(1);
		feature.setExecutionPriority(testCasePriority);
		feature.setProductFeaturestatus(1);
		WorkflowStatus workflowStatus = new WorkflowStatus();
		workflowStatus.setWorkflowStatusId(-1);
		feature.setWorkflowStatus(workflowStatus);
		Integer parentFeatureId=1;
			parentFeature=productListService.getByProductFeatureId(parentFeatureId);
			if(parentFeature== null) {
				
				responseMessage.append("Parent feature id " + parentFeatureId + ". Not adding parent feature" + System.lineSeparator());
				//continue;

				//responseJson.put("result", "ERROR");
				//responseJson.put("status", "400");	
				//responseJson.put("data", "");
				//responseJson.put("message", "Invalid ParentFeatureId");
				//responseJson.put("Failure_Details", "Invalid ParentFeatureId");
				//return responseJson;
				parentFeature = new ProductFeature();
				parentFeature.setProductFeatureId(1);
			} else {
				feature.setParentFeature(parentFeature.getParentFeature());
			}
		feature.setSourceType(IDPAConstants.FEATURE_ADD_SOURCE_TYPE);
		feature.setCreatedDate(new Date());
		feature.setModifiedDate(new Date());
		productListService.addProductFeature(feature);
		if(feature!=null){
			if(feature.getProductFeatureId()!=null){
				mongoDBService.addProductFeature(feature.getProductFeatureId());
				UserList userList = userListService.getUserByLoginId(userName);
				eventsService.addNewEntityEvent(IDPAConstants.ENTITY_PRODUCT_FEATURE_LABEL, feature.getProductFeatureId(), feature.getProductFeatureName(), userList);
			}					
		}
		return feature;
	}
	
	private void addProductPremissionToUser(UserList userList,ProductMaster product) {
		try {
			ProductTeamResources productTeamUser = new ProductTeamResources();
			productTeamUser.setProductTeamResourceId(null);
			productTeamUser.setProductMaster(product);
			productTeamUser.setUserList(userList);
			productTeamUser.setStatus(1);

			//	productTeamUser.setFromDate(plannedStartDate);
			//	productTeamUser.setToDate(plannedEndDate);
			productTeamResourcesDao.addProductTeamResource(productTeamUser);

			ProductUserRole productUserPermission = new ProductUserRole();
			productUserPermission.setProduct(product);
			productUserPermission.setRole(userList.getUserRoleMaster());
			productUserPermission.setUser(userList);
			productUserPermission.setStatus(1);
			productListService.addProductUserRole(productUserPermission);
		}catch(Exception e) {
			log.error("Error in addProductPremissionToUser",e);
		}
	}
	
	@POST
	@Path("/testExecution/query/executeTestPlanGroup")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response executeTestPlanGroup(String nodeRedTafJSONQuery) { 

		SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);		
		JSONObject workpackageJsonObj = new JSONObject();
		JSONObject responseJson = new JSONObject();	
		String replaceStr1= "\"\\\"";
		String replaceStr2 = "\"\\\"";
		try{
			String formattedJsonString = nodeRedTafJSONQuery;
			if(formattedJsonString != null && !formattedJsonString.isEmpty())
				nodeRedTafJSONQuery = formattedJsonString.replace(replaceStr1, "\"").replace(replaceStr2,"").replace("\"{", "{").replace("}\"", "\"}").replace("\"\"","\"").replace("\\","").replace("$a","");
			log.info("JSON Input : "+nodeRedTafJSONQuery);
			responseJson = atlasService.executeTestPlanGroup(nodeRedTafJSONQuery);				
		} catch (Exception e){
			try {
				workpackageJsonObj.put("Result", "FAILED");
				workpackageJsonObj.put("message", "Test Plan execution not initiated due to some issue." + System.lineSeparator() +  e);
				workpackageJsonObj.put("Failure_Details", e.getStackTrace());
				return RestResponseUtility.prepareErrorResponse("Test Plan execution not initiated due to some issue." + System.lineSeparator() +  e,  e.getStackTrace().toString(), workpackageJsonObj.toString());
			} catch (Exception e1) {
				log.error("Problem while executing Test Plan through REST call ", e1);
			}
			log.error("Problem while executing Test Plan through REST call ", e);
		}		
		return Response.ok(responseJson.toString()).build();
	}
}