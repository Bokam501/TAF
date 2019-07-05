package com.hcl.atf.taf.controller;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.export.JRPdfExporter;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hcl.atf.taf.constants.ClientReponseMessage;
import com.hcl.atf.taf.constants.HostStatus;
import com.hcl.atf.taf.constants.IDPAConstants;
import com.hcl.atf.taf.constants.TAFConstants;
import com.hcl.atf.taf.controller.utilities.CommonUtility;
import com.hcl.atf.taf.controller.utilities.DateUtility;
import com.hcl.atf.taf.controller.utilities.UploadForm;
import com.hcl.atf.taf.controller.utilities.ValidationUtility;
import com.hcl.atf.taf.dao.TestCasePriorityDAO;
import com.hcl.atf.taf.dao.WorkPackageDAO;
import com.hcl.atf.taf.integration.CustomConnectorForTesting;
import com.hcl.atf.taf.integration.CustomTestSystemConnectorsManager;
import com.hcl.atf.taf.integration.testManagementSystem.TAFTestManagementSystemIntegrator;
import com.hcl.atf.taf.model.Attachment;
import com.hcl.atf.taf.model.DefectManagementSystem;
import com.hcl.atf.taf.model.DeviceLab;
import com.hcl.atf.taf.model.DeviceMakeMaster;
import com.hcl.atf.taf.model.DeviceModelMaster;
import com.hcl.atf.taf.model.EntityConfigurationProperties;
import com.hcl.atf.taf.model.EntityMaster;
import com.hcl.atf.taf.model.EnvironmentCombination;
import com.hcl.atf.taf.model.Evidence;
import com.hcl.atf.taf.model.EvidenceType;
import com.hcl.atf.taf.model.ExecutionPriority;
import com.hcl.atf.taf.model.ExecutionTypeMaster;
import com.hcl.atf.taf.model.GenericDevices;
import com.hcl.atf.taf.model.HostHeartbeat;
import com.hcl.atf.taf.model.HostList;
import com.hcl.atf.taf.model.MobileType;
import com.hcl.atf.taf.model.PlatformType;
import com.hcl.atf.taf.model.Processor;
import com.hcl.atf.taf.model.ProductFeature;
import com.hcl.atf.taf.model.ProductMaster;
import com.hcl.atf.taf.model.ProductType;
import com.hcl.atf.taf.model.ProductVersionListMaster;
import com.hcl.atf.taf.model.RunConfiguration;
import com.hcl.atf.taf.model.StorageDrive;
import com.hcl.atf.taf.model.TestCaseExecutionResult;
import com.hcl.atf.taf.model.TestCaseList;
import com.hcl.atf.taf.model.TestCaseStepsList;
import com.hcl.atf.taf.model.TestExecutionResultBugList;
import com.hcl.atf.taf.model.TestManagementSystem;
import com.hcl.atf.taf.model.TestRunJob;
import com.hcl.atf.taf.model.TestRunList;
import com.hcl.atf.taf.model.TestRunPlan;
import com.hcl.atf.taf.model.TestRunPlanGroup;
import com.hcl.atf.taf.model.TestStepExecutionResult;
import com.hcl.atf.taf.model.TestSuiteList;
import com.hcl.atf.taf.model.TestcaseExecutionEvent;
import com.hcl.atf.taf.model.UserList;
import com.hcl.atf.taf.model.WorkFlow;
import com.hcl.atf.taf.model.WorkFlowEvent;
import com.hcl.atf.taf.model.WorkPackage;
import com.hcl.atf.taf.model.WorkPackageTestCaseExecutionPlan;
import com.hcl.atf.taf.model.WorkPackageTestSuite;
import com.hcl.atf.taf.model.WorkPackageTestSuiteExecutionPlan;
import com.hcl.atf.taf.model.WorkpackageRunConfiguration;
import com.hcl.atf.taf.model.dto.WorkPackageBuildTestCaseSummaryDTO;
import com.hcl.atf.taf.model.json.JsonEntityConfigurationProperties;
import com.hcl.atf.taf.model.json.JsonGenericDevice;
import com.hcl.atf.taf.model.json.JsonTestcaseExecutionEvent;
import com.hcl.atf.taf.model.json.JsonWorkPackageTestCaseExecutionPlanForTerminal;
import com.hcl.atf.taf.model.json.jtable.JTableResponse;
import com.hcl.atf.taf.model.json.jtable.JTableSingleResponse;
import com.hcl.atf.taf.model.json.terminal.JsonDeviceList;
import com.hcl.atf.taf.model.json.terminal.JsonFileData;
import com.hcl.atf.taf.model.json.terminal.JsonHostList;
import com.hcl.atf.taf.model.json.terminal.JsonProductVersionListMaster;
import com.hcl.atf.taf.model.json.terminal.JsonTestExecutionResult;
import com.hcl.atf.taf.model.json.terminal.JsonTestRunConfigurationChild;
import com.hcl.atf.taf.model.json.terminal.JsonTestRunList;
import com.hcl.atf.taf.model.json.terminal.JsonTestSuiteList;
import com.hcl.atf.taf.mongodb.service.MongoDBService;
import com.hcl.atf.taf.report.Report;
import com.hcl.atf.taf.service.AttachmentService;
import com.hcl.atf.taf.service.DefectManagementService;
import com.hcl.atf.taf.service.DeviceListService;
import com.hcl.atf.taf.service.EmailService;
import com.hcl.atf.taf.service.EntityConfigurationPropertiesService;
import com.hcl.atf.taf.service.EnvironmentService;
import com.hcl.atf.taf.service.EventsService;
import com.hcl.atf.taf.service.ExecutionTypeMasterService;
import com.hcl.atf.taf.service.HostHeartBeatService;
import com.hcl.atf.taf.service.HostListService;
import com.hcl.atf.taf.service.ProductListService;
import com.hcl.atf.taf.service.TestCaseService;
import com.hcl.atf.taf.service.TestExecutionBugsService;
import com.hcl.atf.taf.service.TestExecutionService;
import com.hcl.atf.taf.service.TestManagementService;
import com.hcl.atf.taf.service.TestRunConfigurationService;
import com.hcl.atf.taf.service.TestSuiteConfigurationService;
import com.hcl.atf.taf.service.UserListService;
import com.hcl.atf.taf.service.WorkPackageExecutionService;
import com.hcl.atf.taf.service.WorkPackageService;
import com.hcl.atf.taf.util.Configuration;
import com.hcl.atf.taf.util.ZipTool;
import com.hcl.connector.rest.hpqcrest.ConnectorHPQCRest;
import com.hcl.ilcm.workflow.model.WorkflowStatus;
import com.hcl.ilcm.workflow.service.WorkflowStatusPolicyService;
import com.hcl.ilcm.workflow.service.WorkflowStatusService;
import com.microsoft.tfs.util.json.JSONObject;

@Controller
public class TerminalClientController {

	private static final Log log = LogFactory.getLog(TerminalClientController.class);

	@Autowired
	private TestExecutionService testExecutionService; 
	@Autowired
	private TestRunConfigurationService testRunConfigurationService;
	@Autowired
	private TestSuiteConfigurationService testSuiteConfigurationService;
	@Autowired
	private HostListService hostListService;
	@Autowired
	private DeviceListService deviceListService;
	@Autowired
	private TestExecutionBugsService testExecutionBugsService;
	@Autowired
	private HostHeartBeatService hostHeartBeatService;
	@Autowired
	private EventsService eventsService;
	@Autowired
	private ToolIntegrationController toolsController;
	@Autowired
	TAFTestManagementSystemIntegrator testManagementInegrator;
	@Autowired
	TestManagementService testManagementService;
	@Autowired
	DefectManagementService defectManagementService;
	@Autowired
	private EnvironmentService environmentService;
	@Autowired
	private ProductListService productListService;
	@Autowired
	private WorkPackageService workPackageService;
	@Autowired
	private WorkPackageExecutionService workPackageExecutionService;
	@Autowired
	private TestCaseService testCaseService;
	@Autowired
	private UserListService userListService;
	@Autowired
	private EntityConfigurationPropertiesService entityConfigurationPropertiesService;
	@Autowired
	private TAFTestManagementSystemIntegrator tafTestManagementSystemIntegrator;
	@Autowired
	private MongoDBService mongoDBService;	
	@Autowired
	private Report report;
	@Autowired
	private AttachmentService attachmentService;
	@Autowired
	private ExecutionTypeMasterService executionTypeMasterService;
	@Autowired
	private TestCasePriorityDAO testCasePriorityDAO;
	@Autowired
	private HttpServletRequest request;
	@Autowired
	private WorkPackageDAO workPackageDAO;	
	@Autowired
	private WorkflowStatusPolicyService workflowStatusPolicyService;	
	@Autowired
	private WorkflowStatusService workflowStatusService;
	
	@Autowired 
	private CustomTestSystemConnectorsManager customTestSystemConnectorsManager;
	
	@Value("#{ilcmProps['Testlink_2_DefectManagementSystem']}")
    private static String  testlink_2_DefectManagementSystem;

	@Value("#{ilcmProps['IMAGES_FOLDER']}")
	private String images_Folder;
	@Value("#{ilcmProps['EVIDENCE_FOLDER']}")
	private String evidence_Folder;
	@Value("#{ilcmProps['SELECTIVE_TESTCASES_FOLDER']}")
	private String selective_TestCases_Folder;
	@Value("#{ilcmProps['GENERATED_TEST_SCRIPTS_DESTINATION_FOLDER']}")
	private String testScriptsDestinationDirectory;
	@Value("#{ilcmProps['GENERATED_TEST_SCRIPTS_DESTINATION_FOLDER_APPCONTEXT']}")
	private String testScriptsDestinationDirectoryAppContext;
	@Value("#{ilcmProps['GENERATED_TEST_SCRIPTS_DESTINATION_FOLDER_APPCONTEXT_FOR_URL']}")
	private String testScriptsDestinationDirectoryAppContextForUrl;
	@Value("#{ilcmProps['html.report.generate.driveLocation']}")
	private String htmlReportGenLocation;
	@Value("#{ilcmProps['GEN_EVIDENCE_FOLDER']}")
	private String gen_evidence_Folder;
	@Value("#{ilcmProps['JOB_ROOT_EVIDENCE_FOLDER_BACKUP_REQD']}")
	private String job_root_evidence_folder_backup_reqd;
	@Autowired
	private EmailService emailService;

	@RequestMapping(value="client.test.result.add",method=RequestMethod.POST ,produces="application/json")
	public @ResponseBody JTableResponse addTestExecutionResult(@ModelAttribute JsonTestExecutionResult jsonTestExecutionResult, BindingResult result) {
		log.debug("Job : " + jsonTestExecutionResult.getTestRunListId() + " : Received TER from terminal");

		JTableResponse jTableResponse = null;
		if(result.hasErrors()){
			jTableResponse  = new JTableResponse("ERROR","Invalid form!"); 
		}

		try {		
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			if (jsonTestExecutionResult.getTestCase() == null || jsonTestExecutionResult.getTestCase().trim().isEmpty()) {
				jsonTestExecutionResult.setTestCase(jsonTestExecutionResult.getTestStep());
				jsonTestExecutionResult.setTestCaseDescription(jsonTestExecutionResult.getTestStepDescription());
			}

			String errorMessage = ValidationUtility.validateForNewTER(jsonTestExecutionResult);	

			if (errorMessage != null) {
				jTableResponse = new JTableResponse("ERROR",errorMessage);
				log.info("Received and Failed to Add TER. Mandatory fields missing. " + errorMessage);
				return jTableResponse;
			}

			WorkPackageTestCaseExecutionPlan workPackageTestCaseExecutionPlanFromTerminal = jsonTestExecutionResult.getDecodedTestExecutionResult();
			TestRunJob testRunJob = workPackageExecutionService.getTestRunJobById(jsonTestExecutionResult.getTestRunListId());			
			WorkPackage workPackage = testRunJob.getWorkPackage();
			//If execution type for workpackage is manual then set execution type for workpackage_testcase_execution_plan as manual and vice versa
			Integer executionTypeId = -1;
			if(workPackage != null){
				if(workPackage.getWorkPackageType() != null && workPackage.getWorkPackageType().getExecutionTypeId() != null){
					executionTypeId = workPackage.getWorkPackageType().getExecutionTypeId();
				}
			}
			TestCaseList testCaseList = null;
			TestSuiteList testSuiteList = null;
			ProductMaster product = testRunJob.getWorkPackage().getProductBuild().getProductVersion().getProductMaster();	

			//If Test Suite ID is obtained from TAFTerminal JSON result; else set ID from testRunJob
			if(workPackageTestCaseExecutionPlanFromTerminal.getTestSuiteList() != null && workPackageTestCaseExecutionPlanFromTerminal.getTestSuiteList().getTestSuiteId() != null){
				testSuiteList=testSuiteConfigurationService.getByTestSuiteId(workPackageTestCaseExecutionPlanFromTerminal.getTestSuiteList().getTestSuiteId());
				jsonTestExecutionResult.setTestSuiteId(testSuiteList.getTestSuiteId());
				testRunJob.setTestSuite(testSuiteList);
			} else {
				testSuiteList = testRunJob.getTestSuite();
				jsonTestExecutionResult.setTestSuiteId(testSuiteList.getTestSuiteId());
			}


			log.info("Test Run Job Id: "+testRunJob.getTestRunJobId()+" Test Run Plan Id: "+testRunJob.getTestRunPlan().getTestRunPlanId()+" Test Suite Id "+testSuiteList.getTestSuiteId()+" Test Suite Code: "+testSuiteList.getTestSuiteCode());

			if(workPackageTestCaseExecutionPlanFromTerminal.getTestCase() != null && workPackageTestCaseExecutionPlanFromTerminal.getTestCase().getTestCaseId() != null){
				testCaseList = testCaseService.getTestCaseById(workPackageTestCaseExecutionPlanFromTerminal.getTestCase().getTestCaseId());
			}

			//Adding the code for distinguishing between executing job and the reporting job
			//The jsonTestExecutionResult object contains the executing Job Id
			//This needs to be changed to the reporting Job Id
			//1. Copy the default Job id to the executing JoB Id attribute
			jsonTestExecutionResult.setExecutingTestRunListId(jsonTestExecutionResult.getTestRunListId());
			//2. Replace the reporting Job id in the default Job Id attribute
			if (workPackage.getCombinedResultsReportingJob() != null) {
				jsonTestExecutionResult.setTestRunListId(workPackage.getCombinedResultsReportingJob().getTestRunJobId());
			} 

			log.info("Executing Job ID : "+ jsonTestExecutionResult.getExecutingTestRunListId() +" ; Reporting Job ID : "+jsonTestExecutionResult.getTestRunListId());
			//testCaseList=testCaseService.processTestExecutionResult(testCaseList,jsonTestExecutionResult);
			testCaseList = testCaseService.processTestExecutionResult(testCaseList, jsonTestExecutionResult, testSuiteList.getTestSuiteId());
			// Logic enhanced for setting combined test job or actual test job for finding workpackagetescase execution plan / workpackage testsuite execution plan
			WorkPackageTestCaseExecutionPlan workPackageTestCaseExecutionPlan = null;
			WorkPackageTestSuiteExecutionPlan workPackageTestSuiteExecutionPlan = null;
			if (workPackage.getCombinedResultsReportingJob() != null) {
				workPackageTestCaseExecutionPlan = workPackageService.workPackageTestCasesExecutionPlanByJobId(workPackage.getCombinedResultsReportingJob(),testRunJob, testCaseList);
				workPackageTestSuiteExecutionPlan = workPackageService.workPackageTestSuiteExecutionPlanByJobId(workPackage.getCombinedResultsReportingJob(),testSuiteList);
			} else {

				workPackageTestCaseExecutionPlan = workPackageService.workPackageTestCasesExecutionPlanByJobId(testRunJob,testCaseList);
				workPackageTestSuiteExecutionPlan = workPackageService.workPackageTestSuiteExecutionPlanByJobId(testRunJob,testSuiteList);
			}

			log.info("WorkpackageTestcaseExecutionPlan for : " + workPackage.getWorkPackageId() + "-" + jsonTestExecutionResult.getTestRunListId() +  " : Testcase : " + testCaseList.getTestCaseId() +  "-" + jsonTestExecutionResult.getTestStepId() + " : " + workPackageTestCaseExecutionPlan != null ? "Found" : "Not found" );
			TestCaseExecutionResult testCaseExecutionResult =null;				
			WorkFlowEvent workFlowEvent = new WorkFlowEvent();
			List<WorkPackageTestSuite> workPackageTestSuites = new ArrayList<WorkPackageTestSuite>();
			WorkPackageTestSuite workPackageTestSuite = workPackageService.workPackageTestCasesByJobId(testRunJob);
			if(workPackageTestSuite==null){
				workPackageTestSuite = new WorkPackageTestSuite();
				workPackageTestSuite.setTestSuite(testSuiteList);
				workPackageTestSuite.setWorkPackage(workPackage);
				workPackageTestSuite.setCreatedDate(new Date(System.currentTimeMillis()));
				workPackageTestSuite.setEditedDate(new Date(System.currentTimeMillis()));
				workPackageTestSuite.setIsSelected(1);
				workPackageTestSuite.setStatus("ACTIVE");

				if(workPackageTestSuite.getWorkPackage().getWorkFlowEvent().getWorkFlow().getStageId()!=IDPAConstants.WORKFLOW_STAGE_ID_PLANNING && workPackageTestSuite.getWorkPackage().getWorkFlowEvent().getWorkFlow().getStageValue()<20){
					WorkFlow workFlow=workPackageService.getWorkFlowByEntityIdStageId(IDPAConstants.WORKPACKAGE_ENTITY_ID, IDPAConstants.WORKFLOW_STAGE_ID_PLANNING);
					workFlowEvent = new WorkFlowEvent();
					workFlowEvent.setEventDate(DateUtility.getCurrentTime());
					workFlowEvent.setRemarks("Planning Workpackage :"+workPackage.getName());
					UserList userAdmin= userListService.getUserByLoginId(IDPAConstants.ADMIN_LOGIN_ID);
					workFlowEvent.setUser(userAdmin);
					workFlowEvent.setWorkFlow(workFlow);
					workPackage.setWorkFlowEvent(workFlowEvent);
					workPackageService.addWorkFlowEvent(workFlowEvent);
					workPackageService.updateWorkPackage(workPackage);
				}
				workPackageTestSuites.add(workPackageTestSuite);
				workPackageService.mapWorkpackageWithTestSuite(workPackageTestSuite.getWorkPackage().getWorkPackageId(),testSuiteList.getTestSuiteId(),"Add");
				log.info("New testcases to be added for the workpackage : "+ workPackage.getWorkPackageId() + " : count : " + workPackageTestSuites.size());
				int workPackagesTestSuiteCount = workPackageService.addNewWorkPackageTestSuite(workPackageTestSuites);
				Set<WorkPackageTestSuite> workPackageTS=new HashSet<WorkPackageTestSuite>();
				workPackageTS.addAll(workPackageTestSuites);
				workPackage.setWorkPackageTestSuites(workPackageTS);
				workPackageService.updateWorkPackage(workPackage);
			}
			//Mapping test suite to workpackage
			workPackage=workPackageService.getWorkPackageById(workPackage.getWorkPackageId());

			Set<WorkPackageTestSuite> wpts=workPackage.getWorkPackageTestSuites();

			TestRunPlan testRunPlan=testRunJob.getTestRunPlan(); 
			RunConfiguration runConfiguration = testRunJob.getRunConfiguration();

			WorkpackageRunConfiguration wpRunConfiguration= new WorkpackageRunConfiguration();
			workPackageService.addRunConfigurationWorkpackage(workPackage.getWorkPackageId(),runConfiguration.getRunconfigId(),"testsuite");
			workPackageService.addRunConfigurationWorkpackage(workPackage.getWorkPackageId(),runConfiguration.getRunconfigId(),"testcase");

			wpRunConfiguration=workPackageService.getWorkpackageRunConfiguration(workPackage.getWorkPackageId(), runConfiguration.getRunconfigId(), "testsuite");

			if(workPackageTestSuiteExecutionPlan==null){
				workPackageTestSuiteExecutionPlan = new WorkPackageTestSuiteExecutionPlan();
				workPackageTestSuiteExecutionPlan.setCreatedDate(DateUtility.getCurrentTime());
				workPackageTestSuiteExecutionPlan.setTestsuite(testSuiteList);
				workPackageTestSuiteExecutionPlan.setModifiedDate(DateUtility.getCurrentTime());
				workPackageTestSuiteExecutionPlan.setPlannedExecutionDate(workPackage.getPlannedStartDate());
				workPackageTestSuiteExecutionPlan.setRunConfiguration(wpRunConfiguration);
				workPackageTestSuiteExecutionPlan.setWorkPackage(workPackage);
				workPackageTestSuiteExecutionPlan.setStatus(1);
				workPackageTestSuiteExecutionPlan.setTestRunJob(testRunJob);
				ExecutionPriority executionPriority=null;
				if(workPackageTestSuite.getTestSuite().getExecutionPriority()!=null)
					executionPriority= workPackageService.getExecutionPriorityByName(CommonUtility.getExecutionPriority(workPackageTestSuite.getTestSuite().getExecutionPriority().getPriorityName()));
				else
					executionPriority= workPackageService.getExecutionPriorityByName(IDPAConstants.EXECUTION_PRIORITY_MEDIUM);
				workPackageTestSuiteExecutionPlan.setExecutionPriority(executionPriority);				
				workPackageService.addWorkpackageTestSuiteExecutionPlan(workPackageTestSuiteExecutionPlan);
			}

			TestCaseExecutionResult testCaseExecutionResultFromTerminal = null;
			if(workPackageTestCaseExecutionPlan == null) {				
				log.info("Creating new WorkpackageTestcaseExecutionlan for Workpackage : " + workPackage.getWorkPackageId() + "-" + jsonTestExecutionResult.getTestRunListId() +  " : Testcase : " + testCaseList.getTestCaseId() +  "-" + jsonTestExecutionResult.getTestStepId());
				workPackageTestCaseExecutionPlan = new WorkPackageTestCaseExecutionPlan();
				workPackageTestCaseExecutionPlan.setTestCase(testCaseList);
				workPackageTestCaseExecutionPlan.setTestSuiteList(testSuiteList);
				workPackageTestCaseExecutionPlan.setWorkPackage(workPackage);			

				EnvironmentCombination environmentCombination = testRunJob.getEnvironmentCombination();
				GenericDevices genericDevices=testRunJob.getGenericDevices();
				HostList hostList=testRunJob.getHostList();

				workPackageTestCaseExecutionPlan.setRunConfiguration(wpRunConfiguration);
				workPackageTestCaseExecutionPlan.setEnvironmentCombination(environmentCombination);
				workPackageTestCaseExecutionPlan.setExecutionStatus(2);
				workPackageTestCaseExecutionPlan.setIsExecuted(1);
				workPackageTestCaseExecutionPlan.setHostList(hostList);
				workPackageTestCaseExecutionPlan.setStatus(1);				
				ExecutionPriority executionPriority=null;
				if(testCaseList != null && testCaseList.getTestCasePriority()!=null)
					executionPriority= workPackageService.getExecutionPriorityByName(CommonUtility.getExecutionPriority(testCaseList.getTestCasePriority().getPriorityName()));
				else
					executionPriority= workPackageService.getExecutionPriorityByName(IDPAConstants.EXECUTION_PRIORITY_HIGH);
				workPackageTestCaseExecutionPlan.setExecutionPriority(executionPriority);
				if(testCaseList != null){
					List<ProductFeature> featureList = productListService.getFeaturesMappedToTestCase(testCaseList.getTestCaseId());
					if(featureList != null && !featureList.isEmpty())
						workPackageTestCaseExecutionPlan.setFeature(featureList.get(0));
					else 
						workPackageTestCaseExecutionPlan.setFeature(null);
				}
				workPackageTestCaseExecutionPlan.setSourceType("TestSuite");
				workPackageTestCaseExecutionPlan.setPlannedExecutionDate(DateUtility.getCurrentDate());
				workPackageTestCaseExecutionPlan.setActualExecutionDate(DateUtility.getCurrentDate());
				testCaseExecutionResult = new TestCaseExecutionResult();
				testCaseExecutionResultFromTerminal = workPackageTestCaseExecutionPlan.getTestCaseExecutionResult();

				if(jsonTestExecutionResult.getTestResultStatus()!=null && jsonTestExecutionResult.getTestResultStatus().equalsIgnoreCase(IDPAConstants.EXECUTION_RESULT_PASSED)){
					testCaseExecutionResult.setResult(IDPAConstants.EXECUTION_RESULT_PASSED);
				}else if(jsonTestExecutionResult.getTestResultStatus()!=null && jsonTestExecutionResult.getTestResultStatus().equalsIgnoreCase(IDPAConstants.EXECUTION_RESULT_FAILED)){
					testCaseExecutionResult.setResult(IDPAConstants.EXECUTION_RESULT_FAILED);
				} else if(jsonTestExecutionResult.getTestResultStatus().equalsIgnoreCase(IDPAConstants.EXECUTION_RESULT_RESTART)) {
					testCaseExecutionResult.setResult(IDPAConstants.EXECUTION_RESULT_RESTART);
				}

				testCaseExecutionResult.setComments(jsonTestExecutionResult.getExecutionRemarks());
				testCaseExecutionResult.setDefectsCount(0);
				testCaseExecutionResult.setDefectIds("");
				testCaseExecutionResult.setIsApproved(0);
				testCaseExecutionResult.setIsReviewed(0);
				if (jsonTestExecutionResult.getTestStepObservedOutput() != null && jsonTestExecutionResult.getTestStepObservedOutput().length() >= 5000 ) {
					testCaseExecutionResult.setObservedOutput(jsonTestExecutionResult.getTestStepObservedOutput().substring(4999));
				} else {
					testCaseExecutionResult.setObservedOutput(jsonTestExecutionResult.getTestStepObservedOutput());
				}
				if (jsonTestExecutionResult.getFailureReason() != null && jsonTestExecutionResult.getFailureReason().length() >= 5000 ) {
					testCaseExecutionResult.setFailureReason(jsonTestExecutionResult.getFailureReason().substring(4999));
				} else {
					testCaseExecutionResult.setFailureReason(jsonTestExecutionResult.getFailureReason());
				}

				testCaseExecutionResult.setStartTime(new Date(System.currentTimeMillis()));
				log.debug("jsonTestExecutionResult.getStartTime()"+jsonTestExecutionResult.getStartTime());
				log.debug("jsonTestExecutionResult.getStartTime()"+jsonTestExecutionResult.getEndTime());
				//Need to check with treminal team to get the format of datetime
				log.debug("start time:"+jsonTestExecutionResult.getStartTime());
				if (jsonTestExecutionResult.getStartTime() == null){
					testCaseExecutionResult.setStartTime(new Date(System.currentTimeMillis()));
				}
				else{
					testCaseExecutionResult.setStartTime(sdf.parse(jsonTestExecutionResult.getStartTime()));
				}				

				//set testsuite id for updation
				testCaseExecutionResult.setTestSuiteList(testSuiteList);
				ExecutionTypeMaster executionTypeMaster = executionTypeMasterService.getExecutionTypeByExecutionTypeId(executionTypeId);
				if(executionTypeMaster != null){
					workPackageTestCaseExecutionPlan.setExecutionTypeMaster(executionTypeMaster);
				}else{
					executionTypeMaster = executionTypeMasterService.getExecutionTypeByExecutionTypeId(7);
					workPackageTestCaseExecutionPlan.setExecutionTypeMaster(executionTypeMaster);
				}
				testCaseExecutionResult.setWorkPackageTestCaseExecutionPlan(workPackageTestCaseExecutionPlan);
				testCaseExecutionResult.setEnvironmentCombination(testRunJob.getEnvironmentCombination());	            
				workPackageTestCaseExecutionPlan.setTestCaseExecutionResult(testCaseExecutionResult);		            
			} else {
				EnvironmentCombination environmentCombination = testRunJob.getEnvironmentCombination();
				workPackageTestCaseExecutionPlan.setEnvironmentCombination(environmentCombination);
			}
			workPackageService.updateWorkPackageTestCaseExecutionPlan(workPackageTestCaseExecutionPlan);
			workPackageTestCaseExecutionPlan.setActualTestRunJob(testRunJob);			
			// If it is Combined Reporting Job set Reporting Job as executing Job across all job objects.
			if(workPackage != null && workPackage.getCombinedResultsReportingJob() != null) {
				workPackageTestCaseExecutionPlan.setTestRunJob(workPackage.getCombinedResultsReportingJob());
			} else {
				workPackageTestCaseExecutionPlan.setTestRunJob(testRunJob);
			}
			log.info("Workpackage Testcase Execution Plan : " + workPackageTestCaseExecutionPlan.getId()+" ; Test Run Job ID : " + workPackageTestCaseExecutionPlan.getActualTestRunJob().getTestRunJobId()+ " ; Combined Test Job Id : "+workPackageTestCaseExecutionPlan.getTestRunJob().getTestRunJobId());
			log.info("Test Job : "+testRunJob.getTestRunJobId()+ " ; Environment Combination : "+testRunJob.getEnvironmentCombination().getEnvironmentCombinationName());

			TestCaseStepsList testCaseStepsList= null;
			if(jsonTestExecutionResult.getTestStepId()!=null)
				testCaseStepsList=testSuiteConfigurationService.getByTestStepId(jsonTestExecutionResult.getTestStepId());
			else {    			
				testCaseStepsList=testSuiteConfigurationService.getTestCaseStepByName(jsonTestExecutionResult.getTestStep(), workPackage.getProductBuild().getProductVersion().getProductMaster().getProductId(), testCaseList.getTestCaseId());
			}
			TestStepExecutionResult testStepExecutionResult = new TestStepExecutionResult();
			//testStepExecutionResult.setComments(jsonTestExecutionResult.getExecutionRemarks());
			testStepExecutionResult.setIsApproved(0);
			testStepExecutionResult.setIsReviewed(0);
			if (jsonTestExecutionResult.getTestStepObservedOutput() != null && jsonTestExecutionResult.getTestStepObservedOutput().length() >= 5000 ) {
				testStepExecutionResult.setObservedOutput(jsonTestExecutionResult.getTestStepObservedOutput().substring(4999));
			} else {
				testStepExecutionResult.setObservedOutput(jsonTestExecutionResult.getTestStepObservedOutput());
			}
			if(jsonTestExecutionResult.getTestResultStatus()!=null && jsonTestExecutionResult.getTestResultStatus().equalsIgnoreCase(IDPAConstants.EXECUTION_RESULT_PASSED)){
				testStepExecutionResult.setResult(IDPAConstants.EXECUTION_RESULT_PASSED);
			} else if(jsonTestExecutionResult.getTestResultStatus()!=null && jsonTestExecutionResult.getTestResultStatus().equalsIgnoreCase(IDPAConstants.EXECUTION_RESULT_FAILED)){

				testStepExecutionResult.setResult(IDPAConstants.EXECUTION_RESULT_FAILED);
				testCaseExecutionResultFromTerminal=workPackageTestCaseExecutionPlan.getTestCaseExecutionResult();
				if(testCaseExecutionResultFromTerminal!=null){
					testCaseExecutionResultFromTerminal.setResult(IDPAConstants.EXECUTION_RESULT_FAILED);
					workPackageService.updateTestCaseExecutionResult(testCaseExecutionResultFromTerminal);
					if(testCaseExecutionResultFromTerminal != null && testCaseExecutionResultFromTerminal.getTestCaseExecutionResultId() != null){
						mongoDBService.addTestCaseExecutionResult(testCaseExecutionResultFromTerminal.getTestCaseExecutionResultId());						
					}
				}
			}
			testStepExecutionResult.setTestCaseExecutionResult(workPackageTestCaseExecutionPlan.getTestCaseExecutionResult());
			testStepExecutionResult.setTestcase(workPackageTestCaseExecutionPlan.getTestCase());
			if (jsonTestExecutionResult.getFailureReason() != null && jsonTestExecutionResult.getFailureReason().length() >= 5000 ) {
				testStepExecutionResult.setFailureReason(jsonTestExecutionResult.getFailureReason().substring(4999));
				testStepExecutionResult.setComments(jsonTestExecutionResult.getFailureReason().substring(4999));
			} else if (jsonTestExecutionResult.getFailureReason() != null ){
				testStepExecutionResult.setFailureReason(jsonTestExecutionResult.getFailureReason());
				testStepExecutionResult.setComments(jsonTestExecutionResult.getFailureReason());
			} else {
				testStepExecutionResult.setFailureReason("");
				testStepExecutionResult.setComments("");
			}
			
			testStepExecutionResult.setTestStepStarttime(new Date(System.currentTimeMillis()));
			testStepExecutionResult.setTestStepEndtime(new Date(System.currentTimeMillis()));			
			if (jsonTestExecutionResult.getStartTime() == null){
				testStepExecutionResult.setTestStepStarttime(new Date(System.currentTimeMillis()));
			} else {
				testStepExecutionResult.setTestStepStarttime(sdf.parse(jsonTestExecutionResult.getStartTime()));
			}

			if (jsonTestExecutionResult.getEndTime() == null){
				testStepExecutionResult.setTestStepEndtime(new Date(System.currentTimeMillis()));
			} else { 
				testStepExecutionResult.setTestStepEndtime(sdf.parse(jsonTestExecutionResult.getEndTime()));
			}

			testStepExecutionResult.setEnvironmentCombination(testRunJob.getEnvironmentCombination());
			testStepExecutionResult.setTestJob(testRunJob);
			testStepExecutionResult.setTestSteps(testCaseStepsList);
			//Update test Step execution result additionally with Expected Output
			if(jsonTestExecutionResult.getTestStepExpectedOutput() != null && !jsonTestExecutionResult.getTestStepExpectedOutput().isEmpty())
				testStepExecutionResult.setTestStepExpectedOutput(jsonTestExecutionResult.getTestStepExpectedOutput());
			else if(testCaseStepsList.getTestStepExpectedOutput() != null)
				testStepExecutionResult.setTestStepExpectedOutput(testCaseStepsList.getTestStepExpectedOutput());
			else
				testStepExecutionResult.setTestStepExpectedOutput(null);

			//Update test Step execution result additionally with Input

			if(jsonTestExecutionResult.getTestStepInput() != null && !jsonTestExecutionResult.getTestStepInput().isEmpty())
				testStepExecutionResult.setTestStepInput(jsonTestExecutionResult.getTestStepInput());
			else if(testCaseStepsList.getTestStepInput() != null)
				testStepExecutionResult.setTestStepInput(testCaseStepsList.getTestStepInput());
			else
				testStepExecutionResult.setTestStepInput(null);

			//Adding Evidence path to the test step execution result
			if(jsonTestExecutionResult.getScreenShotPath() != null)
				testStepExecutionResult.setEvidencePath(evidence_Folder+"\\"+workPackageTestCaseExecutionPlan.getTestRunJob().getTestRunJobId()+File.separator+IDPAConstants.EVIDENCE_SCREENSHOT+File.separator+jsonTestExecutionResult.getScreenShotPath());
			workPackageService.saveTestStepExecutionResult(testStepExecutionResult);
			//End time changes start
			testCaseExecutionResult = workPackageTestCaseExecutionPlan.getTestCaseExecutionResult();
			if (jsonTestExecutionResult.getEndTime() == null){
				testCaseExecutionResult.setEndTime(new Date(System.currentTimeMillis()));
			}else{
				testCaseExecutionResult.setEndTime(sdf.parse(jsonTestExecutionResult.getEndTime()));
			}
			workPackageTestCaseExecutionPlan.setTestCaseExecutionResult(testCaseExecutionResult);
			workPackageService.updateWorkPackageTestCaseExecutionPlan(workPackageTestCaseExecutionPlan);
			//Workflow implementation start 
			WorkflowStatus workflowStatus = null;

			if(workflowStatus == null || workflowStatus.getWorkflowStatusId() == null || workflowStatus.getWorkflowStatusId() == 0){

			}
			testCaseExecutionResult.setWorkflowStatus(workflowStatus);

			//End time changes end
			testStepExecutionResult =workPackageService.getTestStepExecutionResultById(testStepExecutionResult.getTeststepexecutionresultid());
			testStepExecutionResult.setTestSteps(testCaseStepsList);
			try{
				if(testStepExecutionResult.getResult().equals(IDPAConstants.EXECUTION_RESULT_FAILED)){
					workPackageService.createBug(testStepExecutionResult, workPackage.getProductBuild());
				}
			}catch(Exception e){
				log.error("Error while creating bug "+ e);
			}

			Evidence evidence=new Evidence();
			evidence.setDescription(null);
			EntityMaster entityMaster=workPackageService.getEntityMasterById(IDPAConstants.ENTITY_TEST_STEP_EVIDENCE_ID);
			String fileName=jsonTestExecutionResult.getScreenShotPath();			
			log.debug("screenshot:"+jsonTestExecutionResult.getScreenShotPath());
			log.debug("lable:"+jsonTestExecutionResult.getScreenShotLabel());
			String extension="";
			if(fileName!=null){
				if(fileName.contains(".")){
					extension=fileName.substring(fileName.lastIndexOf(".")).toLowerCase();
				}
			}

			evidence.setEvidencename(jsonTestExecutionResult.getScreenShotLabel());
			evidence.setEntityMaster(entityMaster);
			evidence.setFiletype(extension);
			log.debug("evidence_Folder:"+evidence_Folder);	

			evidence.setFileuri(evidence_Folder+"\\"+workPackageTestCaseExecutionPlan.getTestRunJob().getTestRunJobId()+File.separator+IDPAConstants.EVIDENCE_SCREENSHOT+File.separator+fileName);
			log.debug("FileURI:"+evidence_Folder+"\\"+workPackageTestCaseExecutionPlan.getTestRunJob().getTestRunJobId()+File.separator+IDPAConstants.EVIDENCE_SCREENSHOT+File.separator+fileName);
			evidence.setEntityvalue(testStepExecutionResult.getTeststepexecutionresultid());
			evidence.setSize(null);
			EvidenceType evidenceType=workPackageService.getEvidenceTypeById(1);
			evidence.setEvidenceType(evidenceType);
			workPackageService.addEvidence(evidence);

			List<JsonTestExecutionResult> tmpList = new ArrayList<JsonTestExecutionResult>();
			workPackageTestCaseExecutionPlan=workPackageService.getWorkpackageTestcaseExecutionPlanById(workPackageTestCaseExecutionPlan.getId());
			tmpList.add(new JsonTestExecutionResult(workPackageTestCaseExecutionPlan));
			workPackageTestCaseExecutionPlan = null;
			jTableResponse = new JTableResponse("OK",tmpList ,1);

			//Add or update the test case into MongoDB instance for ITAX process
			if(testCaseList != null){
				try{
					mongoDBService.addProductTestCaseToMongoDB(testCaseList.getTestCaseId());
				}catch(Exception e){
					log.error("Unable to update Test case to MongoDB ", e);
				}
			}
		} catch(Exception e){
			log.error("Job : " + jsonTestExecutionResult.getTestRunListId() + " : Problem while processing TER from terminal", e);
		}
		log.debug("Job : " + jsonTestExecutionResult.getTestRunListId() + " : Processed TER from terminal");
		return jTableResponse;
	}

	//Method Added for Evidence module
	@RequestMapping(value="client.test.result.evidence.add",method=RequestMethod.POST ,produces="application/json")
	public @ResponseBody String addTestExecutionResultEvidence(@RequestParam String jobId, @ModelAttribute(value="FORM1") UploadForm form,BindingResult result){
		if(!result.hasErrors()){
			Configuration.checkAndCreateDirectory(CommonUtility.getCatalinaPath()+ File.separator+ evidence_Folder);
			FileOutputStream outputStream = null;
			WorkPackage workPackage = null;
			String OriginalFileName = "";
			if (form.getFile() != null) {
				OriginalFileName = form.getFile().getOriginalFilename();	
			}

			String filePath =CommonUtility.getCatalinaPath()+ File.separator+ evidence_Folder + File.separator + OriginalFileName;
			TestRunJob testRunJob = workPackageExecutionService.getTestRunJobById(Integer.parseInt(jobId));
			if(testRunJob != null && testRunJob.getWorkPackage() != null)
				workPackage = testRunJob.getWorkPackage();           
			try {
				log.debug("filePath:"+filePath);
				File file = new File(filePath);            	
				outputStream = new FileOutputStream(file);
				outputStream.write(form.getFile().getFileItem().get());
				outputStream.close();

				if (ZipTool.isValidZipArchive(filePath)) {
					log.debug("destination:"+CommonUtility.getCatalinaPath()+ File.separator+ evidence_Folder);
					//If value is yes then it will be written to ROOT folder of Tomcat
					if(job_root_evidence_folder_backup_reqd != null && job_root_evidence_folder_backup_reqd.equalsIgnoreCase("YES")) { 
						//Unzip the evidence pack to the evidence repository in Web server
						ZipTool.unzip(filePath, CommonUtility.getCatalinaPath()+ File.separator+ evidence_Folder);
					}
					//Unzip the same folder structure inside Context Path location for direct viewing on a browser            
					String insideContextEvidencePath = CommonUtility.getCatalinaPath()+ 
							File.separator+ "webapps" + File.separator + request.getContextPath() + File.separator + "Evidence" + File.separator;
					if(!Configuration.isValidFileOrFolder(insideContextEvidencePath))
						Configuration.checkAndCreateDirectory(insideContextEvidencePath);
					log.debug("The job's evidence files unzipping started");
					ZipTool.unzip(filePath, insideContextEvidencePath);
					//The actual job evidence pack unzipping in the evidence repository is completed.

					//If value is YES then it will be written to ROOT folder of Tomcat
					if(job_root_evidence_folder_backup_reqd != null && job_root_evidence_folder_backup_reqd.equalsIgnoreCase("YES")) {
						String fileNameWithoutExtension = "";
						int extensionStartPos = OriginalFileName.lastIndexOf('.');
						if (extensionStartPos != -1){
							fileNameWithoutExtension = OriginalFileName.substring(0, extensionStartPos);            			
							String jobEvidenceFolderCreated = CommonUtility.getCatalinaPath()+ File.separator+ evidence_Folder + File.separator + fileNameWithoutExtension;
							log.debug("jobEvidenceFolderCreated:"+jobEvidenceFolderCreated);
							if (Configuration.isValidFileOrFolder(jobEvidenceFolderCreated)) {
								log.debug("The job's evidence files have been unzipped");
								Configuration.checkAndRemoveFile(filePath);
								log.debug("The job's evidence file (Archive) is removed after unzipping");
							}
						}
					} else {
						Configuration.checkAndRemoveFile(filePath);
						log.debug("The job's evidence file (Archive) is removed after unzipping");
					}
				} else {
					log.error("The evidence file uploaded is not a valid archive");
					return TAFConstants.EVIDENCE_PACK_NOT_A_VALID_ARCHIVE;
				}
			} catch (Exception e) {
				log.error("Error while saving Evidence pack archive file", e);               
				return TAFConstants.EVIDENCE_PACK_PROBLEM_WHILE_SAVING_AT_SERVER;
			}
			log.debug("Evidence pack successfully uploaded");
			return TAFConstants.EVIDENCE_PACK_SERVER_PUSH_SUCCESSFUL;
		}else{
			log.error("Problems in request for saving evidence pack archive");
			return TAFConstants.EVIDENCE_PACK_SENDREQUEST_HAS_ISSUES;
		}
	}	

	@RequestMapping(value="client.test.result.image.add",method=RequestMethod.POST ,produces="application/json")
	public @ResponseBody String addTestExecutionResultImage(@ModelAttribute(value="FORM1") UploadForm form,BindingResult result){
		if(!result.hasErrors()){
			FileOutputStream outputStream = null;            
			String filePath = images_Folder + File.separator + form.getFile().getOriginalFilename();
			try {

				File file = new File(filePath);            	
				outputStream = new FileOutputStream(file);
				outputStream.write(form.getFile().getFileItem().get());
				outputStream.close();
			} catch (Exception e) {
				log.error("Error while saving file", e);               
				return "ERROR";
			}
			return "OK";
		}else{
			return "ERROR";
		}
	}

	@RequestMapping(value="client.test.run.post",method=RequestMethod.POST ,produces="application/json")
	public @ResponseBody JTableResponse addTestRun(@ModelAttribute JsonTestRunList jsonTestRunList, BindingResult result) {
		JTableResponse jTableResponse;
		if(result.hasErrors()){
			jTableResponse = new JTableResponse("ERROR","Invalid form!"); 
			return jTableResponse;
		}
		TestRunJob testRunJob = null;
		String jobResult = IDPAConstants.JOB_STATUS_FAILED;
		int workPackage_WORKFLOWSTATUS = 0;
		int workPackageId = 0;
		Boolean isNextTPAvailable = false;
		try {
			if(jsonTestRunList.getTestRunListId()!=null){
				testRunJob=environmentService.getTestRunJobById(jsonTestRunList.getTestRunListId());
				TestRunJob testRunJobDB = environmentService.getTestRunJobById(jsonTestRunList.getTestRunListId());
				if (testRunJobDB != null && testRunJobDB.getTestRunEvidenceStatus() != null) {	
					testRunJob.setTestRunEvidenceStatus(testRunJobDB.getTestRunEvidenceStatus());
				} 	
				//Process Completed TestRunJob

				/*if(jsonTestRunList.getTestResultStatus()!=null && (jsonTestRunList.getTestResultStatus().equals(IDPAConstants.JOB_STATUS_PASSED)
						|| jsonTestRunList.getTestResultStatus().equals(IDPAConstants.JOB_STATUS_FAILED ) ) ){*/
				if(jsonTestRunList.getTestResultStatus()!=null && jsonTestRunList.getTestResultStatus().equals(IDPAConstants.JOB_STATUS_PASSED)){
					testRunJob.setTestRunStatus(IDPAConstants.JOB_COMPLETED);
					WorkPackage workPackage =testRunJob.getWorkPackage();
					workPackageId = workPackage.getWorkPackageId();
					if(workPackage!=null && workPackage.getWorkPackageId()!=null){
						mongoDBService.addWorkPackage(workPackage.getWorkPackageId());
					}
					TestRunPlan testRunPlan= testRunJob.getTestRunPlan();
					Set<TestRunJob> testRunJobs=workPackage.getTestRunJobSet();
					Boolean flag=true;
					if (testRunPlan != null && testRunJobs != null) {
						if(testRunPlan.getRunConfigurationList().size()==testRunJobs.size()){
							for (TestRunJob trj : testRunJobs) {
								if(trj.getTestRunStatus()!=5){
									flag=false;
									break;
								}
							}
							if(flag){
								if(workPackage.getSourceType().equals(IDPAConstants.WORKPACKAGE_SOURCE_TESTRUNPLANGROUP)){
									TestRunPlanGroup testRunPlanGroup =workPackage.getTestRunPlanGroup();
									TestRunPlan nextTestRunPlan=workPackageService.getNextTestRunPlan(testRunPlanGroup,testRunPlan);
									if(nextTestRunPlan != null){
										isNextTPAvailable = true;
										workPackageService.addWorkpackageToTestRunPlan(nextTestRunPlan, null, null, null, testRunPlanGroup,workPackage);
									}else
										isNextTPAvailable = false;
								}
							}
						}
					}
					testRunJob.setTestRunStartTime(DateUtility.toDateInSec(jsonTestRunList.getTestRunStartTime()));
					testRunJob.setTestRunEndTime(DateUtility.toDateInSec(jsonTestRunList.getTestRunEndTime()));
					testRunJob.setTestRunFailureMessage(jsonTestRunList.getTestRunFailureMessage());

				}
				WorkPackage workPackage =testRunJob.getWorkPackage();
				workPackageId = workPackage.getWorkPackageId();
				UserList user= userListService.getUserByLoginId(IDPAConstants.ADMIN_LOGIN_ID);
				if (jsonTestRunList.getTestResultStatus().equals("PASSED") || jsonTestRunList.getTestResultStatus().equals("FAILED")){
					testRunJob.setTestRunStatus(IDPAConstants.JOB_COMPLETED);
					testRunJob.setTestRunStartTime(DateUtility.toDateInSec(jsonTestRunList.getTestRunStartTime()));
					testRunJob.setTestRunEndTime(DateUtility.toDateInSec(jsonTestRunList.getTestRunEndTime()));
					testRunJob.setTestRunFailureMessage(jsonTestRunList.getTestRunFailureMessage());
					workPackage_WORKFLOWSTATUS = IDPAConstants.WORKFLOW_STAGE_ID_COMPLETED;
					//Logic to derive pass or fail
					List<WorkPackageBuildTestCaseSummaryDTO> wpBuildTCSummaryDTOList =workPackageService.listWorkPackageTestCaseExecutionBuildSummary(workPackageId,-1);
					if(wpBuildTCSummaryDTOList != null){
						WorkPackageBuildTestCaseSummaryDTO wpBuildTCSummaryDTO = wpBuildTCSummaryDTOList.parallelStream().findFirst().get();
						if(wpBuildTCSummaryDTO != null){
							if(wpBuildTCSummaryDTO.getFailedCount() >0 || wpBuildTCSummaryDTO.getBlockedCount() >0 || wpBuildTCSummaryDTO.getNorunCount() >0){
								jobResult = IDPAConstants.JOB_STATUS_FAILED;
							}else if(wpBuildTCSummaryDTO.getPassedCount() >0){
								jobResult = IDPAConstants.JOB_STATUS_PASSED;
							}else{
								jobResult = IDPAConstants.JOB_STATUS_FAILED;
							}
						}
						testRunJob.setResult(jobResult);
					}
				} else if (jsonTestRunList.getTestResultStatus().equals("ABORTED") ){
					testRunJob.setTestRunStatus(IDPAConstants.JOB_ABORTED);
					testRunJob.setTestRunStartTime(DateUtility.toDateInSec(jsonTestRunList.getTestRunStartTime()));
					testRunJob.setTestRunEndTime(DateUtility.toDateInSec(jsonTestRunList.getTestRunEndTime()));
					testRunJob.setTestRunFailureMessage(jsonTestRunList.getTestRunFailureMessage());
					workPackage_WORKFLOWSTATUS = IDPAConstants.WORKFLOW_STAGE_ID_ABORTED;
					testRunJob.setResult(IDPAConstants.JOB_STATUS_FAILED);
				} else if (jsonTestRunList.getTestResultStatus().equals(IDPAConstants.JOB_STATUS_EXECUTING)) {
					testRunJob.setTestRunStatus(IDPAConstants.JOB_EXECUTING);	
					workPackage_WORKFLOWSTATUS = IDPAConstants.WORKFLOW_STAGE_ID_EXECUTION;
					testRunJob.setResult(IDPAConstants.JOB_STATUS_EXECUTING);
				} else if (jsonTestRunList.getTestResultStatus().equals(IDPAConstants.JOB_STATUS_RESTART)) {
					testRunJob.setTestRunStatus(IDPAConstants.JOB_RESTART);	
					workPackage_WORKFLOWSTATUS = IDPAConstants.WORKFLOW_STAGE_ID_EXECUTION;
					
					//Update the Host Heartbeat to indicate that restart Jobs are available for the host
					//This will ensure that the next pulse from the host will pick up the restart jobs
					hostHeartBeatService.setHostResponseToHeartbeatAsJobsAvailable(testRunJob.getHostList().getHostId());
				}		
				environmentService.updateTestRunJob(testRunJob);				

				if(workPackage != null && workPackage_WORKFLOWSTATUS != 0 ){
					workPackageService.updateWPStatus(testRunJob,workPackage.getWorkPackageId(), workPackage_WORKFLOWSTATUS, user.getUserId(),isNextTPAvailable);
					if(workPackage_WORKFLOWSTATUS == IDPAConstants.WORKFLOW_STAGE_ID_COMPLETED) {
						workPackage = workPackageService.getWorkPackageById(workPackage.getWorkPackageId());
						if(null != workPackage.getWorkFlowEvent()){
							if(null != workPackage.getWorkFlowEvent().getWorkFlow() && null != workPackage.getWorkFlowEvent().getWorkFlow().getStageId()){
								if(null != workPackage.getWorkFlowEvent().getWorkFlow().getEntityMaster()){
									if(null != workPackage.getWorkFlowEvent().getWorkFlow().getEntityMaster().getEntitymasterid()){
										if(workPackage.getWorkFlowEvent().getWorkFlow().getEntityMaster().getEntitymasterid() == IDPAConstants.WORKPACKAGE_ENTITY_ID && (workPackage.getWorkFlowEvent().getWorkFlow().getStageId() == IDPAConstants.WORKFLOW_STAGE_ID_COMPLETED)){
											log.info("Sending Workpackage status to email");							
											//emailService.sendWorkPackageReportMail(workPackage);
											String recipients="";
											if(workPackage.getTestRunPlan() != null && workPackage.getTestRunPlan().getNotifyByMail() != null){
												recipients=workPackage.getTestRunPlan().getNotifyByMail();
											}											
											emailService.sendWorkpackageCompletedReport(workPackage,recipients, "");
											log.info("Sent Workpackage status to email");				
										}							
									}
								}						
							}					
						}
					}
				}

				if(testRunJob!=null){
					if(testRunJob.getTestRunJobId()!=null)
						log.info("Updating Testrun to Mongo ID "+testRunJob.getTestRunJobId());
					mongoDBService.addTestRunJobToMongoDB(testRunJob.getTestRunJobId());
				}
			} else {
				testRunJob=environmentService.getTestRunJobById(jsonTestRunList.getTestRunListId());

				WorkPackage workPackage =testRunJob.getWorkPackage();
				workPackageId = workPackage.getWorkPackageId();
				UserList user= userListService.getUserByLoginId(IDPAConstants.ADMIN_LOGIN_ID);				
				if(jsonTestRunList.getTestResultStatus().equals("PASSED") || jsonTestRunList.getTestResultStatus().equals("FAILED")) {
					testRunJob.setTestRunStatus(IDPAConstants.JOB_COMPLETED);
					testRunJob.setTestRunFailureMessage(jsonTestRunList.getTestRunFailureMessage());
					testRunJob.setTestRunEndTime(DateUtility.toDateInSec(jsonTestRunList.getTestRunEndTime()));
					testRunJob.setTestRunStartTime(DateUtility.toDateInSec(jsonTestRunList.getTestRunStartTime()));
					workPackage_WORKFLOWSTATUS = IDPAConstants.WORKFLOW_STAGE_ID_COMPLETED;	
				} else if(jsonTestRunList.getTestResultStatus().equals("ABORTED") ) {
					testRunJob.setTestRunStatus(IDPAConstants.JOB_ABORTED);
					testRunJob.setTestRunFailureMessage(jsonTestRunList.getTestRunFailureMessage());
					testRunJob.setTestRunEndTime(DateUtility.toDateInSec(jsonTestRunList.getTestRunEndTime()));
					testRunJob.setTestRunStartTime(DateUtility.toDateInSec(jsonTestRunList.getTestRunStartTime()));
					workPackage_WORKFLOWSTATUS = IDPAConstants.WORKFLOW_STAGE_ID_ABORTED;		

				} else if (jsonTestRunList.getTestResultStatus().equals(IDPAConstants.JOB_STATUS_EXECUTING)) {
					testRunJob.setTestRunStatus(IDPAConstants.JOB_EXECUTING);
					workPackage_WORKFLOWSTATUS = IDPAConstants.WORKFLOW_STAGE_ID_EXECUTION;	

				} else if (jsonTestRunList.getTestResultStatus().equals(IDPAConstants.JOB_STATUS_RESTART)) {
					testRunJob.setTestRunStatus(IDPAConstants.JOB_RESTART);	
					workPackage_WORKFLOWSTATUS = IDPAConstants.WORKFLOW_STAGE_ID_EXECUTION;
					//Update the Host Heartbeat to indicate that restart Jobs are available for the host
					//This will ensure that the next pulse from the host will pick up the restart jobs
					hostHeartBeatService.setHostResponseToHeartbeatAsJobsAvailable(testRunJob.getHostList().getHostId());
				}				
				environmentService.addTestRunJob(testRunJob);

				if(workPackage != null && workPackage_WORKFLOWSTATUS != 0 ){
					workPackageService.updateWPStatus(testRunJob,workPackage.getWorkPackageId(), workPackage_WORKFLOWSTATUS, user.getUserId(), false);
				}	
				if(testRunJob!=null){
					if(testRunJob.getTestRunJobId()!=null)
						mongoDBService.addTestRunJobToMongoDB(testRunJob.getTestRunJobId());
				}

			}			
			List<JsonTestRunList> tmpList =new ArrayList();
			jsonTestRunList.setTestRunListId(testRunJob.getTestRunJobId());
			tmpList.add(jsonTestRunList);
			jTableResponse = new JTableResponse("OK",tmpList ,1);
			log.debug("Job : " + testRunJob.getTestRunJobId() + " Updated Job Status : " + testRunJob.getTestRunStatus());
		} catch (Exception e) {
			jTableResponse = new JTableResponse("ERROR","Error Updating Test Run Job!");
			log.error("Job : " + testRunJob.getTestRunJobId() +  "Error Updating Test Run Job", e);
		}

		return jTableResponse;
	}


	@RequestMapping(value="client.test.run.configuration.child.get",method=RequestMethod.POST ,produces="application/json")
	public @ResponseBody JTableResponse getTestRunConfigurationChild(@RequestParam int testRunConfigurationChildId) {
		JTableResponse jTableResponse;

		try {

			log.info("Getting Child config for job"+testRunConfigurationChildId);
			TestRunPlan testRunPlan = productListService.getTestRunPlanBytestRunPlanId(testRunConfigurationChildId);
			List<JsonTestRunConfigurationChild> tmpList =new ArrayList<JsonTestRunConfigurationChild>();
			tmpList.add(new JsonTestRunConfigurationChild(testRunPlan));
			jTableResponse = new JTableResponse("OK",tmpList ,1);
			testRunPlan = null;
		} catch (Exception e) {
			jTableResponse = new JTableResponse("ERROR","Error Getting record!");
			log.error("JSON ERROR", e);
		}

		return jTableResponse;
	}

	//Method Added for Selective TestCase Execution Module
	@RequestMapping(value="client.test.run.selectivetestcases.get",produces="application/txt")
	public @ResponseBody byte[] getSelectiveTestCaseListFile(@RequestParam String testRunListId,HttpServletRequest request,HttpServletResponse response) {
		log.info(testRunListId);
		log.info("inside client.test.run.selectivetestcases.get getSelectiveTestCaseListFile()");
		byte[] stcFileBinary  = null;

		try {
			log.info("Writing selective test case content to the Test Job : "+testRunListId);
			String selectiveTC = createSelectedTestCasesConfigurationFile(testRunListId);
			log.info("Selective Test Case Content : " +selectiveTC);

			String stcFileRealPath= selective_TestCases_Folder+File.separator+testRunListId + ".txt";

			FileInputStream fis = new FileInputStream(stcFileRealPath);

			stcFileBinary = IOUtils.toByteArray(fis);

			String strContentType="application/octet-stream";
			ServletOutputStream servletOutputStream = response.getOutputStream();
			response.setContentType(strContentType);
			response.setHeader("Content-Disposition", "inline; filename=" + testRunListId + ".txt");

			servletOutputStream.write(stcFileBinary);
			servletOutputStream.flush();
			servletOutputStream.close();
			fis.close();
		} catch (Exception e) {
			log.error("Issue in getting selective test cases file", e);
		}
		return stcFileBinary;
	}

	/**
	 * This method returns the Jobs in queue for the host
	 * @param hostId
	 * @return
	 */
	@RequestMapping(value="client.test.run.get",method=RequestMethod.POST ,produces="application/json")
	public @ResponseBody JTableResponse getTestRunList(@RequestParam int hostId) {
		JTableResponse jTableResponse;		
		try {
			List<TestRunJob> testRunJobs = environmentService.listByHostId(hostId, IDPAConstants.JOB_QUEUED);			
			List<TestRunJob> abortedTestRunList = environmentService.listByHostId(hostId,  IDPAConstants.JOB_ABORT);
			List<TestRunJob> restartTestRunList = environmentService.listByHostId(hostId,  IDPAConstants.JOB_RESTART);

			//Add the restart jobs to the test run jobs list
			testRunJobs.addAll(restartTestRunList);
			testRunJobs.addAll(abortedTestRunList);

			List<JsonTestRunList> jsonTestRunLists =new ArrayList<JsonTestRunList>();
			String  jobHasTestSuiteTestCases =  "";	//Obtain Test Suite Test Cases for a Test Run Job
			List<Integer> testSuiteIdsList = new LinkedList<Integer>();
			int selectiveTestCaseFlag = 0;

			if(testRunJobs!=null){
				log.info("Host : "+hostId + " : Queued Jobs : " + testRunJobs.size());
				for(TestRunJob trj : testRunJobs){
					List<TestCaseList> testCaseLists = null;
					List<TestCaseList> alreadyExecutedTestCases = null;
					List<TestCaseList> updatedtestCasesList = new ArrayList<TestCaseList>();;


					//Added support for Multiple Test Suite List to a single job starts								
					if(trj.getTestRunPlan().getMultipleTestSuites().equalsIgnoreCase("1")){
						//If multiple test suites; pass test suites to terminal via test run job
						String tcnames = "";
						if(trj.getTestRunPlan().getTestScriptsLevel().equalsIgnoreCase(IDPAConstants.MULTIPLE_TEST_SUITE_LEVEL_SCRIPT_PACKS)) {		
							for(TestSuiteList tsl : trj.getRunConfiguration().getTestSuiteLists()){
								if(tsl != null){																
									testSuiteIdsList.add(tsl.getTestSuiteId());
									testCaseLists = workPackageService.getSelectedTestCasesFromTestRunJobTestSuite(trj.getTestRunJobId(), tsl.getTestSuiteId());
									if (trj.getTestRunStatus() == IDPAConstants.JOB_RESTART) {
										//TODO : Get list of already executed test cases in this restarted job
										alreadyExecutedTestCases = workPackageService.getAlreadyExecutedTestcasesForJob(trj, tsl.getTestSuiteId());
										//alreadyExecutedTestCases = new ArrayList<TestCaseList>();
										if (!(alreadyExecutedTestCases == null || alreadyExecutedTestCases.isEmpty())) {
											for(TestCaseList tc : testCaseLists){
												if (!alreadyExecutedTestCases.contains(tc)) {
													updatedtestCasesList.add(tc);
												} else {
													log.info("Job : " + trj.getTestRunJobId() + " : Testcase already executed in the restarted job. Removing : " + tc.getTestCaseName());
												}
											}
										} else {
											updatedtestCasesList = testCaseLists;
										}
									} else {

										updatedtestCasesList = testCaseLists;
									}
									for(TestCaseList tc : updatedtestCasesList){
										tcnames += tc.getTestCaseName()+",";						
									}	
								}
								if(! tcnames.isEmpty()){
									tcnames = tcnames.substring(0, tcnames.lastIndexOf(","));
									jobHasTestSuiteTestCases += tsl.getTestSuiteId()+","+tcnames+"$";
								}
							}
						}					
					} else { // default single test suite behavior
						//Re-initialize the testSuiteIdList
						testSuiteIdsList = new LinkedList<Integer>();
						
						if(trj.getTestSuite()!= null && trj.getTestSuite().getTestSuiteId() != null){
							log.info("Test Suite for the Test Suite initialised : "+trj.getTestSuite().getTestSuiteId());
							testSuiteIdsList.add(trj.getTestSuite().getTestSuiteId());
						}
						// pass list of selected test cases for a run job to Terminal
						testCaseLists = workPackageService.getSelectedTestCasesFromTestRunJob(trj.getTestRunJobId());
						if (trj.getTestRunStatus() == IDPAConstants.JOB_RESTART) {
							//TODO : Get list of already executed test cases in this restarted job
							alreadyExecutedTestCases = workPackageService.getAlreadyExecutedTestcasesForJob(trj, trj.getTestSuite().getTestSuiteId());
							if (!(alreadyExecutedTestCases == null || alreadyExecutedTestCases.isEmpty())) {
								for(TestCaseList tc : testCaseLists){
									if (!alreadyExecutedTestCases.contains(tc)) {
										updatedtestCasesList.add(tc);
									} else {
										log.info("Job : " + trj.getTestRunJobId() + " : Testcase already executed in the restarted job. Removing : " + tc.getTestCaseName());
									}
								}
							} else {
								updatedtestCasesList = testCaseLists;
							}
						} else {
							updatedtestCasesList = testCaseLists;
						}
						String tcnames = "";
						if(!(updatedtestCasesList == null || updatedtestCasesList.isEmpty())) {
							for(TestCaseList tc : updatedtestCasesList){
								tcnames += tc.getTestCaseName()+",";						
							}	
							if(!tcnames.isEmpty()){
								tcnames = tcnames.substring(0, tcnames.lastIndexOf(","));
								if(trj.getTestSuite() != null && trj.getTestSuite().getTestSuiteId() != null)
									jobHasTestSuiteTestCases += trj.getTestSuite().getTestSuiteId()+","+ tcnames+"$";
								else 
									jobHasTestSuiteTestCases += ","+ tcnames+"$";
							}
						} 
					}				

					//Added support for Multiple Test Suite List to a single job ends					
					if(updatedtestCasesList!=null && !updatedtestCasesList.isEmpty())
						selectiveTestCaseFlag=1;
					List<Attachment> attachmentsForTestRunPlan = attachmentService.listDataRepositoryAttachments(trj.getTestRunPlan().getTestRunPlanId());
					
					//Configuration Properties - starts
					List<JsonEntityConfigurationProperties> jsonEntityConfigurationProperties =  new ArrayList<JsonEntityConfigurationProperties>();
					String envConfigProperties = "";
					try {
						log.info("Getting configuration properties for job : "+ trj.getTestRunJobId());				
						Integer runConfigId = trj.getRunConfiguration().getRunconfigId();
						
						List<EntityConfigurationProperties> jobConfigurationProperties = entityConfigurationPropertiesService.getEntityConfigurePropertiesByEntityId(runConfigId, IDPAConstants.ENTITY_RUN_CONFIGURATION_ID, -1);
						log.info("Job : " + trj.getTestRunJobId() + " : Test Configuration properties count : " + jobConfigurationProperties.size());
						
						List<EntityConfigurationProperties> testRunPlanConfigurationProperties = entityConfigurationPropertiesService.getEntityConfigurePropertiesByEntityId(trj.getTestRunPlan().getTestRunPlanId(), IDPAConstants.ENTITY_TEST_RUN_PLAN_ID, -1);
						log.info("Job : " + trj.getTestRunJobId() + " : Test Plan Configuration properties count : " + testRunPlanConfigurationProperties.size());
						
						//Test Plan Level Configuration Property
						if (testRunPlanConfigurationProperties != null) {
							for (EntityConfigurationProperties testRunPlanConfigurationProperty : testRunPlanConfigurationProperties) {
								JsonEntityConfigurationProperties jsontestRunPlanConfigurationProperty = new JsonEntityConfigurationProperties(testRunPlanConfigurationProperty);
								jsonEntityConfigurationProperties.add(jsontestRunPlanConfigurationProperty);
							}
						}
						//Test Configuration Level Configuration Property
						if (jobConfigurationProperties != null) {
							for (EntityConfigurationProperties jobConfigurationProperty : jobConfigurationProperties) {
								JsonEntityConfigurationProperties jsonJobConfigurationProperty = new JsonEntityConfigurationProperties(jobConfigurationProperty);
								jsonEntityConfigurationProperties.add(jsonJobConfigurationProperty);								
							}
						}
						log.info("Job : " + trj.getTestRunJobId() + " : Job Configuration properties count sent to server : " + testRunPlanConfigurationProperties.size());						
					} catch (Exception e) {						
						log.error("JSON ERROR", e);
					}
					
					//Construct JSONString for Environment Configuration Property
					JSONArray jsonArray = new JSONArray();
					org.json.JSONObject envObj = new org.json.JSONObject();
					if(jsonEntityConfigurationProperties != null && !jsonEntityConfigurationProperties.isEmpty()){
						for(JsonEntityConfigurationProperties prop : jsonEntityConfigurationProperties){
							org.json.JSONObject obj = new org.json.JSONObject();
							if (!(prop.getEntityConfigPropertiesMasterName().equalsIgnoreCase(IDPAConstants.LOCALE) 
									|| prop.getEntityConfigPropertiesMasterName().equalsIgnoreCase(IDPAConstants.TEST_DATA))) {								
								obj.put(prop.getProperty(), prop.getValue());
							} else if (prop.getEntityConfigPropertiesMasterName().equalsIgnoreCase(IDPAConstants.LOCALE)) {
								obj.put(IDPAConstants.LOCALE, prop.getValue());
							} else if (prop.getEntityConfigPropertiesMasterName().equalsIgnoreCase(IDPAConstants.TEST_DATA)) {
								obj.put(IDPAConstants.TEST_DATA, prop.getValue());
							}
							jsonArray.put(obj);
						}
					}		
					envObj.put("envMap", jsonArray);
					//Configuration Properties - ends

					JsonTestRunList jsonTestRunList = new JsonTestRunList(trj, selectiveTestCaseFlag, attachmentsForTestRunPlan, "testData.xlsx", "uiObject.xlsx",envObj.toString());					
					//Update the Reporting Job Id
					if (trj.getWorkPackage().getCombinedResultsReportingJob() !=  null && trj.getWorkPackage().getCombinedResultsReportingJob().getTestRunJobId() != null)
						jsonTestRunList.setCombinedResultsReportingJobId(trj.getWorkPackage().getCombinedResultsReportingJob().getTestRunJobId());
					else 
						jsonTestRunList.setCombinedResultsReportingJobId(trj.getTestRunJobId());
					Integer execTime = 0;
					try{
						execTime = environmentService.getAverageTestRunExecutionTime(trj);
					} catch(Exception e) {
						log.error("Error in retrieving Average Execution Time ");
					}

					if (execTime != null){
						jsonTestRunList.setAverageTestRunExecutionTime(execTime);
					} else {
						jsonTestRunList.setAverageTestRunExecutionTime(new Integer(0));
					}

					//Added for Test Suite Test Cases List
					jsonTestRunList.setJobHasTestSuiteTestCases(jobHasTestSuiteTestCases);
					//Added for Test Suite List to Test Run job
					jsonTestRunList.setJobHasTestSuite(StringUtils.join(testSuiteIdsList, ','));

					jsonTestRunLists.add(jsonTestRunList);
				}
			}
			hostListService.isHostActive(hostId);
			if(abortedTestRunList != null){ 
				log.info("Host : "+hostId + " : Abort Jobs count : " + abortedTestRunList.size());
				for(TestRunJob trj:abortedTestRunList){
					JsonTestRunList jsonTestRunList = new JsonTestRunList(trj,0);
					//Update the Reporting Job Id
					if(trj.getWorkPackage().getCombinedResultsReportingJob() != null)
						jsonTestRunList.setCombinedResultsReportingJobId(trj.getWorkPackage().getCombinedResultsReportingJob().getTestRunJobId());

					jsonTestRunLists.add(jsonTestRunList);
					log.info("Job : "+jsonTestRunList.getTestRunListId() + " : " + trj.getTestRunStatus());
				}
			}
			log.info("Host : " + hostId + " : Queued or aborted Jobs : " + jsonTestRunLists.size());
			jTableResponse = new JTableResponse("OK",jsonTestRunLists ,jsonTestRunLists.size());
			testRunJobs = null;
		} catch (Exception e) {
			jTableResponse = new JTableResponse("ERROR","Error getting queued or aborted jobs!");
			log.error("Host : " + hostId + "Error getting queued or aborted jobs", e);
		}
		return jTableResponse;
	}

	/**
	 * This method returns the Jobs in queue for the host
	 * @param hostId
	 * @return
	 */
	@RequestMapping(value="client.test.run.get.old",method=RequestMethod.POST ,produces="application/json")
	public @ResponseBody JTableResponse getTestRunListOld(@RequestParam int hostId) {
		JTableResponse jTableResponse;		
		try {
			List<TestRunJob> testRunJobs = environmentService.listByHostId(hostId, IDPAConstants.JOB_QUEUED);			
			List<TestRunJob> abortedTestRunList = environmentService.listByHostId(hostId,  IDPAConstants.JOB_ABORT);
			List<TestRunJob> restartTestRunList = environmentService.listByHostId(hostId,  IDPAConstants.JOB_RESTART);

			List<JsonTestRunList> jsonTestRunLists =new ArrayList<JsonTestRunList>();
			String  jobHasTestSuiteTestCases =  "";	//Obtain Test Suite Test Cases for a Test Run Job
			List<Integer> testSuiteIdsList = new LinkedList<Integer>();
			int selectiveTestCaseFlag = 0;

			if(testRunJobs!=null){
				log.info("Host : "+hostId + " : Queued Jobs : " + testRunJobs.size());
				for(TestRunJob trj : testRunJobs){
					List<TestCaseList> testCaseLists = null;	

					//Added support for Multiple Test Suite List to a single job starts								
					if(trj.getTestRunPlan().getMultipleTestSuites().equalsIgnoreCase("1")){
						//If multiple test suites; pass test suites to terminal via test run job
						String tcnames = "";
						if(trj.getTestRunPlan().getTestScriptsLevel().equalsIgnoreCase(IDPAConstants.MULTIPLE_TEST_SUITE_LEVEL_SCRIPT_PACKS)) {		
							for(TestSuiteList tsl : trj.getRunConfiguration().getTestSuiteLists()){
								if(tsl != null){																
									testSuiteIdsList.add(tsl.getTestSuiteId());
									testCaseLists = workPackageService.getSelectedTestCasesFromTestRunJobTestSuite(trj.getTestRunJobId(), tsl.getTestSuiteId());
									for(TestCaseList tc : testCaseLists){
										tcnames += tc.getTestCaseName()+",";						
									}	
								}
								if(! tcnames.isEmpty()){
									tcnames = tcnames.substring(0, tcnames.lastIndexOf(","));
									jobHasTestSuiteTestCases += tsl.getTestSuiteId()+","+tcnames+"$";
								}
							}
						}					
					} else { // default single test suite behavior
						if(trj.getTestSuite()!= null && trj.getTestSuite().getTestSuiteId() != null){
							log.info("Test Suite for the Test Suite initialised : "+trj.getTestSuite().getTestSuiteId());
							testSuiteIdsList.add(trj.getTestSuite().getTestSuiteId());
						}
						// pass list of selected test cases for a run job to Terminal
						testCaseLists = workPackageService.getSelectedTestCasesFromTestRunJob(trj.getTestRunJobId());
						String tcnames = "";
						if(testCaseLists != null && testCaseLists.size() > 0){
							for(TestCaseList tc : testCaseLists){
								tcnames += tc.getTestCaseName()+",";						
							}	
							if(!tcnames.isEmpty()){
								tcnames = tcnames.substring(0, tcnames.lastIndexOf(","));
								jobHasTestSuiteTestCases += trj.getTestSuite().getTestSuiteId()+","+ tcnames+"$";
							}
						}	
					}				

					//Added support for Multiple Test Suite List to a single job ends					
					if(testCaseLists!=null && !testCaseLists.isEmpty())
						selectiveTestCaseFlag=1;
					List<Attachment> attachmentsForTestRunPlan = attachmentService.listDataRepositoryAttachments(trj.getTestRunPlan().getTestRunPlanId());

					JsonTestRunList jsonTestRunList = new JsonTestRunList(trj, selectiveTestCaseFlag, attachmentsForTestRunPlan, "testData.xlsx", "uiObject.xlsx", "");					
					//Update the Reporting Job Id
					if (trj.getWorkPackage().getCombinedResultsReportingJob() !=  null && trj.getWorkPackage().getCombinedResultsReportingJob().getTestRunJobId() != null)
						jsonTestRunList.setCombinedResultsReportingJobId(trj.getWorkPackage().getCombinedResultsReportingJob().getTestRunJobId());
					else 
						jsonTestRunList.setCombinedResultsReportingJobId(trj.getTestRunJobId());
					Integer execTime = 0;
					try{
						execTime = environmentService.getAverageTestRunExecutionTime(trj);
					} catch(Exception e) {
						log.error("Error in retrieving Average Execution Time ");
					}

					if (execTime != null){
						jsonTestRunList.setAverageTestRunExecutionTime(execTime);
					} else {
						jsonTestRunList.setAverageTestRunExecutionTime(new Integer(0));
					}

					//Added for Test Suite Test Cases List
					jsonTestRunList.setJobHasTestSuiteTestCases(jobHasTestSuiteTestCases);
					//Added for Test Suite List to Test Run job
					jsonTestRunList.setJobHasTestSuite(StringUtils.join(testSuiteIdsList, ','));

					jsonTestRunLists.add(jsonTestRunList);
				}
			}
			hostListService.isHostActive(hostId);
			if(abortedTestRunList != null){ 
				log.info("Host : "+hostId + " : Abort Jobs count : " + abortedTestRunList.size());
				for(TestRunJob trj:abortedTestRunList){
					JsonTestRunList jsonTestRunList = new JsonTestRunList(trj,0);
					//Update the Reporting Job Id
					jsonTestRunList.setCombinedResultsReportingJobId(trj.getWorkPackage().getCombinedResultsReportingJob().getTestRunJobId());

					jsonTestRunLists.add(jsonTestRunList);
					log.info("Job : "+jsonTestRunList.getTestRunListId() + " : " + trj.getTestRunStatus());
				}
			}
			log.info("Host : " + hostId + " : Queued or aborted Jobs : " + jsonTestRunLists.size());
			jTableResponse = new JTableResponse("OK",jsonTestRunLists ,jsonTestRunLists.size());
			testRunJobs = null;
		} catch (Exception e) {
			jTableResponse = new JTableResponse("ERROR","Error getting queued or aborted jobs!");
			log.error("Host : " + hostId + "Error getting queued or aborted jobs", e);
		}
		return jTableResponse;
	}


	@RequestMapping(value="client.test.run.update.evidencestatus",produces="application/json")
	public @ResponseBody String updateTestRunListEvidenceStatus(@RequestParam int testRunListId, String testRunEvidenceStatus) {
		try {
			log.debug("TerminalClientController.updateTestRunListEvidenceStatus() testRunEvidenceStatus"+testRunEvidenceStatus);
			TestRunJob testRunJob=workPackageService.getTestRunJobById(testRunListId);
			if(testRunJob!=null){
				testRunJob.setTestRunEvidenceStatus(testRunEvidenceStatus);
				environmentService.updateTestRunJob(testRunJob);

				if(testRunJob!=null){
					if(testRunJob.getTestRunJobId()!=null)
						log.debug("Updating Testrun to Mongo ID "+testRunJob.getTestRunJobId());
					mongoDBService.addTestRunJobToMongoDB(testRunJob.getTestRunJobId());
				}
				EntityMaster entityMaster = workPackageService.getEntityMasterById(IDPAConstants.JOBEVIDENCE_ENTITY);
				String evidencename="";
				Evidence evidence=null;
				EvidenceType evidenceType=null;
				log.debug("catalina path:"+CommonUtility.getCatalinaPath());
				log.debug("evidence_Folder:"+evidence_Folder);
				Boolean folderExist=Configuration.isValidFileOrFolder(CommonUtility.getCatalinaPath()+File.separator+evidence_Folder+File.separator+testRunJob.getTestRunJobId()+File.separator+IDPAConstants.EVIDENCE_LOG);
				File file= new File(CommonUtility.getCatalinaPath()+File.separator+evidence_Folder+File.separator+testRunJob.getTestRunJobId()+File.separator+IDPAConstants.EVIDENCE_LOG);
				if(folderExist){
					File[] files=file.listFiles();
					if(files!=null && files.length>0){
						for (File tempFile : files) {
							evidence = new Evidence();
							evidence.setEntityMaster(entityMaster);
							evidence.setEntityvalue(testRunJob.getTestRunJobId());
							evidence.setEvidencename(evidencename);
							evidenceType=workPackageService.getEvidenceTypeById(3);
							evidence.setEvidenceType(evidenceType);
							evidence.setFileuri(evidence_Folder+File.separator+testRunJob.getTestRunJobId()+File.separator+IDPAConstants.EVIDENCE_LOG+File.separator+tempFile.getName());
							evidence.setFiletype(tempFile.getName().substring(tempFile.getName().lastIndexOf(".")).toLowerCase());
							workPackageService.addEvidence(evidence);
						}
					}
				}

				folderExist=Configuration.isValidFileOrFolder(CommonUtility.getCatalinaPath()+File.separator+evidence_Folder+File.separator+testRunJob.getTestRunJobId()+File.separator+IDPAConstants.EVIDENCE_REPORT);
				file= new File(evidence_Folder+File.separator+testRunJob.getTestRunJobId()+File.separator+IDPAConstants.EVIDENCE_REPORT);
				if(folderExist){
					File[] files=file.listFiles();
					if(files!=null && files.length>0){
						for (File tempFile : files) {
							evidence = new Evidence();
							evidence.setEntityMaster(entityMaster);
							evidence.setEntityvalue(testRunJob.getTestRunJobId());
							evidence.setEvidencename(evidencename);
							evidenceType=workPackageService.getEvidenceTypeById(5);
							evidence.setEvidenceType(evidenceType);
							evidence.setFileuri(evidence_Folder+File.separator+testRunJob.getTestRunJobId()+File.separator+IDPAConstants.EVIDENCE_REPORT+File.separator+tempFile.getName());
							evidence.setFiletype(tempFile.getName().substring(tempFile.getName().lastIndexOf(".")).toLowerCase());
							workPackageService.addEvidence(evidence);
						}
					}
				}

				folderExist=Configuration.isValidFileOrFolder(CommonUtility.getCatalinaPath()+File.separator+evidence_Folder+File.separator+testRunJob.getTestRunJobId()+File.separator+IDPAConstants.EVIDENCE_VIDEO);
				file= new File(evidence_Folder+File.separator+testRunJob.getTestRunJobId()+File.separator+IDPAConstants.EVIDENCE_VIDEO);
				if(folderExist){
					File[] files=file.listFiles();
					if(files!=null && files.length>0){
						for (File tempFile : files) {
							evidence = new Evidence();
							evidence.setEntityMaster(entityMaster);
							evidence.setEntityvalue(testRunJob.getTestRunJobId());
							evidence.setEvidencename(evidencename);
							evidenceType=workPackageService.getEvidenceTypeById(3);
							evidence.setEvidenceType(evidenceType);
							evidence.setFileuri(evidence_Folder+File.separator+testRunJob.getTestRunJobId()+File.separator+IDPAConstants.EVIDENCE_VIDEO+File.separator+tempFile.getName());
							evidence.setFiletype(tempFile.getName().substring(tempFile.getName().lastIndexOf(".")).toLowerCase());
							workPackageService.addEvidence(evidence);
						}
					}
				}

				testExecResultsAutoPost(testRunJob.getTestRunJobId(),null,null);
				defectsAutoPost(testRunJob.getTestRunJobId(),-1,null,null);
			}

		} catch (Exception e) {
			log.error("Error while updating testRunEvidenceStatus", e);
			return TAFConstants.EVIDENCE_STATUS_NOT_UPDATED;
		}
		return TAFConstants.EVIDENCE_STATUS_UPDATED;
	}


	@RequestMapping(value="client.test.run.selected.devices.get",method=RequestMethod.POST ,produces="application/json")
	public String getTestRunSelectedDeviceList(HttpServletRequest request,HttpServletResponse response) {
		return "forward:test.run.child.device.list";
	}

	@RequestMapping(value="client.test.run.product.master.get",method=RequestMethod.POST ,produces="application/json")
	public @ResponseBody JTableSingleResponse getProductVersionListMaster(@RequestParam int productVersionListId) {

		JTableSingleResponse jTSingleResponse;

		try {			
			ProductVersionListMaster productVersionListMaster = productListService.getProductVersionListMasterById(productVersionListId);
			jTSingleResponse = new JTableSingleResponse("OK",new JsonProductVersionListMaster(productVersionListMaster));				

		} catch (Exception e) {
			jTSingleResponse = new JTableSingleResponse("ERROR");
			log.error("JSON ERROR", e);	            
		}

		return jTSingleResponse;
	}


	@RequestMapping(value="client.test.suite.get",method=RequestMethod.POST ,produces="application/json")
	public @ResponseBody JTableResponse getTestSuite(@RequestParam int testSuiteId) {
		JTableResponse jTableResponse;		
		try {
			log.info("Retrieving Test Suite for the  ID : " + testSuiteId);
			TestSuiteList testSuiteList = testSuiteConfigurationService.getByTestSuiteId(testSuiteId);
			List<JsonTestSuiteList> tmpList = new ArrayList<JsonTestSuiteList>();
			JsonTestSuiteList jsonTestSuite = new JsonTestSuiteList(testSuiteList);
			log.info("Obtained Test Cases Count : " +jsonTestSuite.getTestCaseCount() + " and Test Steps Count : " +jsonTestSuite.getTestStepsCount());
			tmpList.add(jsonTestSuite);			
			jTableResponse = new JTableResponse("OK",tmpList ,1);
			testSuiteList = null;
		} catch (Exception e) {
			jTableResponse = new JTableResponse("ERROR","Error Getting record!");
			log.error("JSON ERROR", e);
		}        
		return jTableResponse;
	}

	@RequestMapping(value="client.test.suite.get.dynamic",method=RequestMethod.POST ,produces="application/json")
	public @ResponseBody JTableResponse getTestSuite(@RequestParam int testSuiteId, @RequestParam int testRunListId, HttpServletRequest request) {
		JTableResponse jTableResponse;		
		try {
			TestSuiteList testSuiteList = testSuiteConfigurationService.getByTestSuiteId(testSuiteId);

			List<JsonTestSuiteList> tmpList =new ArrayList();
			JsonTestSuiteList jsonTestSuiteList = new JsonTestSuiteList(testSuiteList);

			if (testSuiteList.getScriptTypeMaster().getScriptType().equalsIgnoreCase("GHERKIN")) {
				if (testSuiteList.getTestSuiteScriptFileLocation() == null || testSuiteList.getTestSuiteScriptFileLocation().trim().isEmpty()) {
					String testStoryPackLocation = CommonUtility.getDirectoryForBDDStoriesZipForJobForURL(testRunListId, testScriptsDestinationDirectoryAppContextForUrl);
					testStoryPackLocation = testStoryPackLocation + ".zip";
					String testStoryPackLocationURL = "http://" + request.getServerName()+":" + request.getServerPort() +testStoryPackLocation;
					log.info(" Setting BDD Story Pack File URL for Job : " + testRunListId + " : " + testStoryPackLocationURL);
					jsonTestSuiteList.setTestSuiteScriptFileLocation(testStoryPackLocationURL);
				}
			} else if (testSuiteList.getTestScriptSource()!= null 
					&& !testSuiteList.getTestScriptSource().equals(IDPAConstants.TEST_SCRIPT_SOURCE_ILCM) 
					&&  !testSuiteList.getTestScriptSource().equals(IDPAConstants.TEST_SCRIPT_SOURCE_HUDSON) ) {
				String testSuitePath = "http://" + request.getServerName()+":" + request.getServerPort()+request.getContextPath()+"/TestScripts/";
				String testScriptFileLocation = testSuiteConfigurationService.constructTestScriptFileNameForExternalScriptSource(testSuiteList, testRunListId,testSuitePath);
				jsonTestSuiteList.setTestSuiteScriptFileLocation(testScriptFileLocation);
			}
			tmpList.add(jsonTestSuiteList);
			jTableResponse = new JTableResponse("OK",tmpList ,1);
			testSuiteList = null;
		} catch (Exception e) {
			jTableResponse = new JTableResponse("ERROR","Error Getting record!");
			log.error("JSON ERROR", e);
		}
		return jTableResponse;
	}

	@RequestMapping(value="client.util.ip",method=RequestMethod.POST ,produces="application/json")
	public @ResponseBody String getClientIpAddress(HttpServletRequest req,HttpServletResponse res) {
		String ipAddress="UNKNOWN";		
		try {
			ipAddress=req.getRemoteAddr();

		} catch (Exception e) {            
			log.error("ERROR", e);
		}

		return ipAddress;
	}

	@RequestMapping(value="client.util.register",method=RequestMethod.POST ,produces="application/json")
	public @ResponseBody JTableSingleResponse registerClient(@ModelAttribute JsonHostList jsonHostList, BindingResult result) {
		JTableSingleResponse jTableSingleResponse;

		if(result.hasErrors()){
			jTableSingleResponse  = new JTableSingleResponse("ERROR","Invalid form!"); 
		}

		try {
			HostList hostList = jsonHostList.getHostList();
			List<HostList> tmplist = hostListService.listByHostName(hostList.getHostName());
			if(tmplist!=null && tmplist.size()!=0){
				hostList.setHostId(tmplist.get(0).getHostId());					
				hostListService.update(hostList);
			}else{
				hostListService.add(hostList);
			}
			HostHeartbeat hostHeartbeat = new HostHeartbeat(hostList.getHostId(), System.currentTimeMillis(),false, (short) -1);
			hostHeartBeatService.update(hostHeartbeat);
			log.info("host '"+hostList.getHostName()+"' Connected");
			eventsService.raiseTerminalConnectedEvent(hostList, "Terminal connected to Server");
			JsonHostList tmpList = new JsonHostList(hostList);
			jsonHostList.setHostId(hostList.getHostId());
			jTableSingleResponse = new JTableSingleResponse("OK",tmpList);
		} catch (Exception e) {
			jTableSingleResponse = new JTableSingleResponse("ERROR","Error registering terminal client");
			log.error("JSON ERROR", e);
		}

		return jTableSingleResponse;
	}

	@RequestMapping(value="client.util.heartpulse",method=RequestMethod.POST ,produces="application/json")
	public @ResponseBody int  updateHeartPulse(@RequestParam int hostId) {

		log.debug("Received Heartbeat for Host : " + hostId);
		int response=ClientReponseMessage.PULSE_ACK_OK;
		//keeping it as a simple integer response as to minimize the overhead of de-marshallig at the client
		//heart pulse is sent every 5 seconds from client
		try {
			HostHeartbeat host=hostHeartBeatService.getByHostId(hostId);
			if(host==null) {
				log.debug("No previous heart beat. ReRegistering host : " + hostId);
				HostList hostList = hostListService.getHostById(hostId);
				if(hostList!=null){
					hostList.getCommonActiveStatusMaster().setStatus(HostStatus.ACTIVE.toString());			
					hostListService.update(hostList);
				}
				host = new HostHeartbeat(hostId, System.currentTimeMillis(),false, (short) ClientReponseMessage.PULSE_ACK_REREGISTERED);
				hostHeartBeatService.update(host);
				response=ClientReponseMessage.PULSE_ACK_REREGISTERED;
			}else{
				if(host.isHasResponse()){						
					response=host.getResponseToSend();
					log.info("Server has response for host : " + hostId);
					host.setHasResponse(false);
				} else {
					//Double check to resolve the issue of jobs not reaching hosts in some cases
					//This is most probably because of some pulse timing issues
					boolean hasJobs = false;
					hasJobs = checkIfHostHasJobsInQueue(host);
					log.info("Double check if jobs are available for host : " + hostId + " has jobs : " + hasJobs);
					if (hasJobs) {
						response = ClientReponseMessage.PULSE_ACK_JOBS_AVAILABLE;
						log.info("Set response to indicate host has jobs : " + hostId + " has jobs : " + hasJobs);
					}
					//No need to set the response flag to false, as it is already false
				}
				host.setLastHeartPulseReceivedTime(System.currentTimeMillis());		
				hostHeartBeatService.update(host);
				log.debug("Updated heart beat time : " + hostId + " : " + System.currentTimeMillis());
			}
			host=null;
		} catch (Exception e) {
			log.error("ERROR", e);
			response=ClientReponseMessage.PULSE_ACK_ERROR;
		}
		log.debug("Heart Beat Pulse response to Host : " + hostId + " : " + response);
		return response;
	}
	
	/*
	 * Added this method for double checking if a host has jobs, as part of the heart beat response
	 * There are some jobs that are falling through the cracks due to timing issues
	 */
	public boolean checkIfHostHasJobsInQueue(HostHeartbeat host) {
	
		if (host == null)
			return false;
		
		List<TestRunJob> testRunJobs = environmentService.listByHostId(host.getHostId(), IDPAConstants.JOB_QUEUED);			
		List<TestRunJob> abortedTestRunList = environmentService.listByHostId(host.getHostId(),  IDPAConstants.JOB_ABORT);
		List<TestRunJob> restartTestRunList = environmentService.listByHostId(host.getHostId(),  IDPAConstants.JOB_RESTART);

		int jobsForHost = 0;
		if (testRunJobs != null)
			jobsForHost += testRunJobs.size();
		if (abortedTestRunList != null)
			jobsForHost += abortedTestRunList.size();
		if (restartTestRunList != null)
			jobsForHost += restartTestRunList.size();
		
		if (jobsForHost > 0) {
			log.info("Host : " + host.getHostId() + " has jobs : " + jobsForHost);
			return true;
		}
		return false;
	}

	/*
	 * Returns devices attached to a host
	 */
	@RequestMapping(value="client.util.device.get",method=RequestMethod.POST ,produces="application/json")
	public @ResponseBody JTableResponse getDeviceListForHost(@RequestParam int hostId) {

		JTableResponse jTableResponse;		
		try {
			List<GenericDevices> deviceList= environmentService.getGenericDevicesByHostId(hostId);
			List<JsonDeviceList> jsonDeviceList=new ArrayList<JsonDeviceList>();
			for(GenericDevices dl: deviceList){				
				jsonDeviceList.add(new JsonDeviceList(dl));
			}

			jTableResponse = new JTableResponse("OK", jsonDeviceList,jsonDeviceList.size());
			deviceList = null;

		} catch (Exception e) {
			jTableResponse = new JTableResponse("ERROR","Error fetching records!");
			log.error("JSON ERROR", e);
		}
		return jTableResponse;
	}

	/*
	 * Returns devices attached to a host
	 */
	@RequestMapping(value="client.util.storagedevice.get",method=RequestMethod.POST ,produces="application/json")
	public @ResponseBody JTableResponse getStorageDeviceListForHost(@RequestParam int hostId) {

		JTableResponse jTableResponse;		
		try {
			List<GenericDevices> deviceList= environmentService.getGenericDevicesByHostIdAndDeviceTypeId(hostId, 6);
			List<JsonDeviceList> jsonDeviceList=new ArrayList<JsonDeviceList>();
			for(GenericDevices dl: deviceList){
				jsonDeviceList.add(new JsonDeviceList(dl));
			}

			jTableResponse = new JTableResponse("OK", jsonDeviceList,jsonDeviceList.size());
			deviceList = null;

		} catch (Exception e) {
			jTableResponse = new JTableResponse("ERROR","Error fetching records!");
			log.error("JSON ERROR", e);
		}
		return jTableResponse;
	}

	/**
	 * Changes have been done to update the device make, model and platform version dynamically even if wrong data exists in DB.
	 * 19-4-2016  
	 * @param jsonDeviceList
	 * @param result
	 * @return
	 */

	@RequestMapping(value="client.util.device.register",method=RequestMethod.POST ,produces="application/json")
	public @ResponseBody JTableSingleResponse registerDevice(@ModelAttribute JsonDeviceList jsonDeviceList, BindingResult result) {
		JTableSingleResponse jTableSingleResponse;
		if(result.hasErrors()){
			log.error("Errors in device details received from terminal "  +   result.toString());
			jTableSingleResponse = new JTableSingleResponse("ERROR","Invalid form!"); 
		}
		JsonDeviceList josnList = null;
		try {
			GenericDevices deviceList = environmentService.getGenericDevices(jsonDeviceList.getDeviceId());
			GenericDevices tempList=jsonDeviceList.getGenericDeviceList(jsonDeviceList);

			boolean isRegisteringToNewHost=false;int hostId=-1;				
			if(deviceList==null){

				//The device is a new one. Not present in the environment earlier. Add it
				String validation = ValidationUtility.validateForNewDeviceAddition(tempList);
				if (validation != null) {

					jTableSingleResponse = new JTableSingleResponse("ERROR","Unable to add device! Contact administrator. " + validation);
					log.error("Unable to add device! Contact administrator. "+ validation);
					return jTableSingleResponse;
				} else {
					//getDevicePlatformVersionList adds the device platform version if not found in master record
					DeviceMakeMaster deviceMakeMaster = null;
					DeviceModelMaster deviceModelMaster = null;
					if(jsonDeviceList.getDeviceMake() != null){
						deviceMakeMaster=deviceListService.getDeviceMakeMaster(jsonDeviceList.getDeviceMake());
						if(deviceMakeMaster != null ){
							deviceModelMaster=deviceListService.getDeviceModelMaster(jsonDeviceList.getDeviceModel());
							if(deviceModelMaster == null){
								deviceModelMaster = new DeviceModelMaster();
								deviceModelMaster.setDeviceMakeMaster(deviceMakeMaster);
								deviceModelMaster.setDeviceModel(jsonDeviceList.getDeviceModel());
								deviceModelMaster.setDeviceResolution(jsonDeviceList.getDeviceModel());
								deviceModelMaster.setDeviceName(jsonDeviceList.getDeviceName());
								int deviceModelMasterId =deviceListService.addDeviceModelMaster(deviceModelMaster);
								deviceModelMaster = deviceListService.getDeviceModelMasterById(deviceModelMasterId);
							}
						}
					}

					log.info("version:"+jsonDeviceList.getDeviceList().getDevicePlatformVersionListMaster().getDevicePlatformVersion());
					log.info("name:"+jsonDeviceList.getDeviceList().getDevicePlatformVersionListMaster().getDevicePlatformMaster().getDevicePlatformName());
					log.info("version:"+jsonDeviceList.getDevicePlatformVersion());
					log.info("name:"+jsonDeviceList.getDevicePlatformName());


					/* Adding Generic Device as Mobile Type */
					MobileType mobileType = new MobileType();						

					PlatformType platformType=deviceListService.getPlatFormType(jsonDeviceList.getDevicePlatformName(),jsonDeviceList.getDevicePlatformVersion());
					if(platformType != null) {
						if(!platformType.getVersion().equalsIgnoreCase(jsonDeviceList.getDevicePlatformVersion()) && jsonDeviceList.getDevicePlatformVersion()!=null && !jsonDeviceList.getDevicePlatformVersion().isEmpty()){
							//Adding A Platform with new version
							PlatformType platformnew = new PlatformType();
							platformnew.setName(platformType.getName());
							platformnew.setEntityMaster(platformType.getEntityMaster());
							log.info("Adding platform with new Version "+jsonDeviceList.getDevicePlatformVersion());
							platformnew.setVersion(jsonDeviceList.getDevicePlatformVersion());
							int platFormId =deviceListService.addPlatformType(platformnew);
							platformnew = deviceListService.getPlatFormType(platFormId);
							mobileType.setPlatformType(platformnew);
						}else{
							mobileType.setPlatformType(platformType);
						}
					}else if(jsonDeviceList.getDevicePlatformVersion()!=null && !jsonDeviceList.getDevicePlatformVersion().isEmpty()){//Adding A New Platform
						PlatformType platformnew = new PlatformType();
						platformnew.setName(jsonDeviceList.getDevicePlatformName());
						EntityMaster entMas = new EntityMaster();
						entMas = workPackageService.getEntityMasterById(7);							
						platformnew.setEntityMaster(entMas);
						platformnew.setVersion(jsonDeviceList.getDevicePlatformVersion());
						int platFormId =deviceListService.addPlatformType(platformnew);
						platformnew = deviceListService.getPlatFormType(platFormId);
						mobileType.setPlatformType(platformnew);
					}
					if(deviceMakeMaster != null && deviceMakeMaster.getDeviceMakeId() != 0){
						mobileType.setDeviceMakeMaster(deviceMakeMaster);	
					}
					if(deviceModelMaster != null){
						mobileType.setDeviceModelMaster(deviceModelMaster);
					}



					mobileType.setStatus(1);
					String status=jsonDeviceList.getDeviceStatus();
					if(status.equalsIgnoreCase(IDPAConstants.DEVICE_ACTIVE)){
						mobileType.setAvailableStatus(1);
					}else if(status.equalsIgnoreCase(IDPAConstants.DEVICE_INACTIVE)){
						mobileType.setAvailableStatus(0);
					}
					mobileType.setUDID(jsonDeviceList.getDeviceId());
					mobileType.setName(jsonDeviceList.getDeviceId());
					HostList hostList= new HostList();
					hostList=hostListService.getHostById(jsonDeviceList.getHostId());
					mobileType.setHostList(hostList);
					DeviceLab deviceLab=deviceListService.getDeviceLabByDeviceLabId(1);
					mobileType.setDeviceLab(deviceLab);

					environmentService.addGenericDevice((GenericDevices)mobileType);
					josnList= new JsonDeviceList(environmentService.getGenericDevices(mobileType.getGenericsDevicesId()));
				}

			}else{
				//Device is an already registered one. Update it's info and availability
				if(jsonDeviceList!=null && deviceList!=null && deviceList.getHostList()!=null){
					if(!jsonDeviceList.getHostId().equals(deviceList.getHostList().getHostId())){
						isRegisteringToNewHost=true;
						hostId=deviceList.getHostList().getHostId();
					}		
				}
				deviceList=deviceList;
				if(jsonDeviceList.getHostId()!=null && deviceList.getHostList()!=null)
					deviceList.getHostList().setHostId(jsonDeviceList.getHostId());
				String status=jsonDeviceList.getDeviceStatus();
				if(status.equalsIgnoreCase(IDPAConstants.DEVICE_ACTIVE)){
					deviceList.setAvailableStatus(1);
				}else if(status.equalsIgnoreCase(IDPAConstants.DEVICE_INACTIVE)){
					deviceList.setAvailableStatus(0);
				}
				DeviceMakeMaster deviceMakeMaster = null;
				if(jsonDeviceList.getDeviceMake() != null){
					deviceMakeMaster=deviceListService.getDeviceMakeMaster(jsonDeviceList.getDeviceMake());
					if(deviceMakeMaster != null ){
						if(jsonDeviceList.getDeviceModel() != null){
							String devmodel = jsonDeviceList.getDeviceModel().trim();
							DeviceModelMaster devmodelmaster = deviceListService.getDeviceModelMaster(devmodel);
							if(devmodelmaster == null){
								devmodelmaster = new DeviceModelMaster();
								devmodelmaster.setDeviceMakeMaster(deviceMakeMaster);
								devmodelmaster.setDeviceModel(jsonDeviceList.getDeviceModel());
								devmodelmaster.setDeviceResolution(jsonDeviceList.getDeviceModel());
								devmodelmaster.setDeviceName(jsonDeviceList.getDeviceName());
								int deviceModelMasterId =deviceListService.addDeviceModelMaster(devmodelmaster);
								devmodelmaster = deviceListService.getDeviceModelMasterById(deviceModelMasterId);
							} else {
								devmodelmaster.setDeviceName(jsonDeviceList.getDeviceName());
								deviceListService.updateDeviceModelMaster(devmodelmaster);
							}
							deviceList.setDeviceModelMaster(devmodelmaster);	
							MobileType mb = (MobileType) deviceList;
							mb.setDeviceMakeMaster(deviceMakeMaster);
							deviceList = mb;
						}
					} else if(deviceMakeMaster == null || deviceMakeMaster.equals(null) || deviceMakeMaster.equals("null")){
						DeviceMakeMaster devMakeMaster = new DeviceMakeMaster();							
						devMakeMaster.setDeviceMake(jsonDeviceList.getDeviceMake().trim());
						int devMakeId = deviceListService.addDeviceMakeMaster(devMakeMaster);
						devMakeMaster = deviceListService.getDeviceMakeMasterById(devMakeId);

						String devmodel = jsonDeviceList.getDeviceModel().trim();
						if(devmodel != null){
							DeviceModelMaster devmodelmaster = deviceListService.getDeviceModelMaster(devmodel);
							if(devmodelmaster == null){
								devmodelmaster = new DeviceModelMaster();
								devmodelmaster.setDeviceMakeMaster(devMakeMaster);
								devmodelmaster.setDeviceModel(jsonDeviceList.getDeviceModel());
								devmodelmaster.setDeviceResolution(jsonDeviceList.getDeviceModel());
								devmodelmaster.setDeviceName(jsonDeviceList.getDeviceName());
								int deviceModelMasterId =deviceListService.addDeviceModelMaster(devmodelmaster);
								devmodelmaster = deviceListService.getDeviceModelMasterById(deviceModelMasterId);
							} else {
								devmodelmaster.setDeviceName(jsonDeviceList.getDeviceName());
								deviceListService.updateDeviceModelMaster(devmodelmaster);
							}
							deviceList.setDeviceModelMaster(devmodelmaster);

							Set<DeviceModelMaster>  devModelSet = new HashSet<>();
							devModelSet.add(devmodelmaster);
						}							
					}
				}

				PlatformType platformType=deviceListService.getPlatFormType(jsonDeviceList.getDevicePlatformName(),jsonDeviceList.getDevicePlatformVersion());
				if(platformType != null){
					if(!platformType.getVersion().equalsIgnoreCase(jsonDeviceList.getDevicePlatformVersion()) && jsonDeviceList.getDevicePlatformVersion()!=null && !jsonDeviceList.getDevicePlatformVersion().isEmpty()){
						//Adding A Platform with new version
						PlatformType platformnew = new PlatformType();
						platformnew.setName(platformType.getName());
						platformnew.setEntityMaster(platformType.getEntityMaster());
						deviceList.setPlatformType(platformnew);
					}
				}else if(jsonDeviceList.getDevicePlatformVersion()!=null && !jsonDeviceList.getDevicePlatformVersion().isEmpty()){//Adding A New Platform
					PlatformType platformnew = new PlatformType();
					platformnew.setName(platformType.getName());
					platformnew.setEntityMaster(platformType.getEntityMaster());
					deviceList.setPlatformType(platformnew);
				}
				environmentService.updateGenericDevice(deviceList);
				josnList = new JsonDeviceList(deviceList);	

			}
			deviceList = null;
			jTableSingleResponse = new JTableSingleResponse("OK",josnList);
		} catch (Exception e) {
			jTableSingleResponse = new JTableSingleResponse("ERROR","Unable to register device!");
			log.error("Unable to register device : " + jsonDeviceList.getDeviceId(), e);
		}

		return jTableSingleResponse;
	}


	/*
	 * host updates the device status to BUSY when the device is being used for executing test
	 */

	@RequestMapping(value="client.util.device.update",method=RequestMethod.POST ,produces="application/json")
	public @ResponseBody JTableSingleResponse updateDeviceStatus(@ModelAttribute JsonDeviceList jsonDeviceList, BindingResult result) {
		JTableSingleResponse jTableSingleResponse;
		if(result.hasErrors()){
			jTableSingleResponse  = new JTableSingleResponse("ERROR","Invalid form!"); 
		}

		try {

			GenericDevices deviceList = environmentService.getGenericDevices(jsonDeviceList.getDeviceId());
			String status=jsonDeviceList.getDeviceStatus();
			if(status.equalsIgnoreCase(IDPAConstants.DEVICE_ACTIVE)){
				deviceList.setAvailableStatus(1);
			}else if(status.equalsIgnoreCase(IDPAConstants.DEVICE_INACTIVE)){
				deviceList.setAvailableStatus(0);
			}
			deviceListService.genericupdate(deviceList);				

			jTableSingleResponse = new JTableSingleResponse("OK",jsonDeviceList);
		} catch (Exception e) {
			jTableSingleResponse = new JTableSingleResponse("ERROR","Error updating record!");
			log.error("JSON ERROR", e);
		}
		return jTableSingleResponse;
	}

	@RequestMapping(value="client.util.device.unregister",method=RequestMethod.POST ,produces="application/json")
	public @ResponseBody JTableSingleResponse unRegisterDevice(@ModelAttribute JsonDeviceList jsonDeviceList, BindingResult result) {
		JTableSingleResponse jTableSingleResponse;
		if(result.hasErrors()){
			jTableSingleResponse  = new JTableSingleResponse("ERROR","Invalid form!"); 
		}

		try {
			GenericDevices deviceList = environmentService.getGenericDevices(jsonDeviceList.getDeviceId());
			deviceList.setStatus(0);
			deviceList.setAvailableStatus(0);
			deviceListService.genericupdate(deviceList);

			jTableSingleResponse = new JTableSingleResponse("OK",jsonDeviceList);
		} catch (Exception e) {
			jTableSingleResponse = new JTableSingleResponse("ERROR","Error updating record!");
			log.error("JSON ERROR", e);
		}
		return jTableSingleResponse;
	}

	@ExceptionHandler(Exception.class)
	public void handleException(Throwable ex, HttpServletResponse response)
			throws Exception {

		response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
				ex.getMessage());
		ex.printStackTrace();
	}

	public String createSelectedTestCasesConfigurationFile(String testRunListId) {
		Integer testRunListIdI;
		List<TestCaseList> updatedtestCasesList = new ArrayList<TestCaseList>();
		if (testRunListId == null || testRunListId.trim().length() == 0) {
			log.debug("TestRunList Id is not provided");
			return null;
		}
		//Create the file for the selected testcases for the testrunlist into the specific txt file
		List<TestCaseList> selectedTestCases = new ArrayList<TestCaseList>(); 
		selectedTestCases = workPackageService.getSelectedTestCasesFromTestRunJob(Integer.parseInt(testRunListId));
		for(TestCaseList tcl : selectedTestCases){
			log.info("selectedTestCases from DB"+tcl.getTestCaseName());
		}
		TestRunJob trj = workPackageService.getTestRunJobById(Integer.parseInt(testRunListId));
		log.info("TestNG Test Job : "+trj.getTestRunJobId()+" & Status : "+trj.getTestRunStatus());
		if(trj.getTestRunStatus() !=  null && trj.getTestRunStatus() == 8) {			
			List<TestCaseList> alreadyExecutedTestCases = workPackageService.getAlreadyExecutedTestcasesForJob(trj, trj.getTestSuite().getTestSuiteId());
			//alreadyExecutedTestCases = new ArrayList<TestCaseList>();
			if (!(alreadyExecutedTestCases == null || alreadyExecutedTestCases.isEmpty())) {
				for(TestCaseList tc : selectedTestCases){
					if (!alreadyExecutedTestCases.contains(tc)) {
						updatedtestCasesList.add(tc);
					} else {
						log.info("Job : " + trj.getTestRunJobId() + " : Testcase already executed in the restarted job. Removing : " + tc.getTestCaseName());
					}
				}
			} else {
				updatedtestCasesList = selectedTestCases;
			}
			
			selectedTestCases = new ArrayList<TestCaseList>();
			selectedTestCases = updatedtestCasesList;
		}		
		if (selectedTestCases == null) {
			return null;
		}
		String selectedTestCasesFileName = saveToFile(testRunListId, selectedTestCases);
		log.info("Selected Test Cases for execution to the Test Job Id : "+testRunListId +" is "+selectedTestCasesFileName);
		return selectedTestCasesFileName;
	}

	private String saveToFile(String testRunListId, List<TestCaseList> selectedTestCases) {		
		StringBuffer testCaseNames = new StringBuffer();
		//Construct the string to be written to the file
		//The string is the names of the ordered testcases
		log.info("Job : " + testRunListId + " : Selective Testcases Count : " + selectedTestCases.size());
		for (TestCaseList testCase : selectedTestCases) {			
			testCaseNames.append(testCase.getTestCaseScriptQualifiedName());
			testCaseNames.append(";");
			testCaseNames.append(testCase.getTestCaseName().replace("_", ""));
			testCaseNames.append(System.getProperty("line.separator"));
			log.info("Job : " + testRunListId + " : Selective Testcase : " + testCase.getTestCaseScriptQualifiedName() + "." + testCase.getTestCaseName());
		}
		log.info("Job : " + testRunListId + " : Selective Testcases file content : " + testCaseNames.toString());
		//Write the string to the file
		Configuration.checkAndCreateDirectory(selective_TestCases_Folder);		
		String fileName = selective_TestCases_Folder+File.separator+testRunListId + ".txt";
		File selectiveTestCasesFile = new File(fileName);
		try
		{
			FileUtils.writeStringToFile(selectiveTestCasesFile,testCaseNames.toString());
			return fileName;
		} catch (Exception e) {
			log.error("Issue with writing selectiveTestCases names into a file: "+e);
			return null;	
		}
	}


	@RequestMapping(value="client.test.run.results.autopost",method=RequestMethod.POST ,produces="application/json")
	public @ResponseBody JTableSingleResponse testExecResultsAutoPost(@RequestParam int testRunListId,HttpServletRequest request, HttpServletResponse response) {
		JTableSingleResponse jTableSingleResponse;
		log.info("client.test.run.results.autopost");
		TestRunJob testRunJob = null;
		try {
			testRunJob=environmentService.getTestRunJob(testRunListId);
			TestRunPlan testRunPlan =testRunJob.getTestRunPlan();
			ProductVersionListMaster productVersionList = testRunJob.getTestRunPlan().getProductVersionListMaster();
			ProductMaster productMaster = productVersionList.getProductMaster();

			if(testRunPlan.getAutoPostResults().equals("1") || testRunPlan.getAutoPostResults().equals(1) || testRunPlan.getAutoPostResults()==1){
				TestManagementSystem testManagementSystem = testManagementService.getPrimaryTMSByProductId(productMaster.getProductId());
				if(testManagementSystem!=null) {
					if ((testManagementSystem != null && testManagementSystem.getToolIntagration() !=null)&& testManagementSystem.getToolIntagration().getName().trim().equalsIgnoreCase(IDPAConstants.HPQC_TOOL) || (testManagementSystem != null && testManagementSystem.getToolIntagration() !=null)&& testManagementSystem.getToolIntagration().getName().trim().equalsIgnoreCase(IDPAConstants.TFS_TOOL)) {
						String exportLocation = runDeviceReport_PDF(testRunListId,request, response);
						toolsController.testResultsExportByTestrunjobRest(testRunListId,request, response,testManagementSystem.getTestManagementSystemId()+"",exportLocation);

						//Export the PDF report to the test system	
						if ((testManagementSystem != null && testManagementSystem.getToolIntagration() !=null)&& testManagementSystem.getToolIntagration().getName().trim().equalsIgnoreCase(IDPAConstants.HPQC_TOOL)){
							uploadReportsToTestSet(exportLocation,testRunListId,testManagementSystem);
						}
						if (testRunJob.getTestRunFailureMessage() != null) {
							testRunJob.setTestRunFailureMessage(testRunJob.getTestRunFailureMessage() + System.lineSeparator() +"Test Execution Results successfully posted");
						} else {
							testRunJob.setTestRunFailureMessage("Test Execution Results successfully posted");
						}
						environmentService.updateTestRunJob(testRunJob);

						jTableSingleResponse = new JTableSingleResponse("Test Execution Results successfully posted to Test management system");
						return jTableSingleResponse;
					} else {
						log.info("Test Results not exported as the Test management system is not supported yet : " + testManagementSystem.getTestSystemName());
						jTableSingleResponse = new JTableSingleResponse("Test Results not exported as the Test management system is not supported yet : " + testManagementSystem.getTestSystemName());
					}
				} else if (true) {
					//Going forward new test management system connectors will be added through Custom connectors interface as connector libraries
					//These libraries have to be discovered dynamically
					customTestSystemConnectorsManager.initializeTestSystemConnectorsManager(testManagementSystem.getToolIntagration().getName(),testManagementSystem.getTestSystemVersion(),testManagementSystem.getSystemType().getName(),testlink_2_DefectManagementSystem);
					if (customTestSystemConnectorsManager.isConnectorAvailable(testManagementSystem.getToolIntagration().getName(),testManagementSystem.getTestSystemVersion(),testManagementSystem.getSystemType().getName())) {
						//Post the results/defects to the system
						String testExecutionResults=tafTestManagementSystemIntegrator.exportTestExecutionResultsToTestLink(testRunJob, testManagementSystem);
						String systemConnectionDetailsJson="";
						customTestSystemConnectorsManager.reportTestExecutionResultsToSystem(systemConnectionDetailsJson, testExecutionResults);
					} else {
						log.info("Connector for the specified Test Management System & version is not available.");
					}
				} else {
					log.info("No primary test management system is configured. Auto posting of results not done");
					jTableSingleResponse = new JTableSingleResponse("No primary test management system is configured. Auto posting of results not done");
				}
			}
			jTableSingleResponse = new JTableSingleResponse("Autopost setting is false. Not exported results to test management system");
		} catch (Exception e) {
			jTableSingleResponse = new JTableSingleResponse("ERROR","Error posting Test Execution Results to Test Management System");
			if (testRunJob.getTestRunFailureMessage() != null) 
				testRunJob.setTestRunFailureMessage(testRunJob.getTestRunFailureMessage() + System.lineSeparator() + "Error posting Test Execution Results to Test Management System" + e.getMessage());
			else
				testRunJob.setTestRunFailureMessage("Error posting Test Execution Results to Test Management System" + e.getMessage());
			environmentService.updateTestRunJob(testRunJob);
			log.error("JSON ERROR", e);
		}
		return jTableSingleResponse;
	}


	@RequestMapping(value="client.test.run.defect.autopost",method=RequestMethod.POST ,produces="application/json")
	public @ResponseBody JTableSingleResponse defectsAutoPost(@RequestParam int testRunListId,@RequestParam int testRunNo,HttpServletRequest request, HttpServletResponse response) {
		JTableSingleResponse jTableSingleResponse;
		List<TestExecutionResultBugList> bugs = new ArrayList<TestExecutionResultBugList>();
		int defectSystemId = -1;
		log.info("inside client.test.run.defect.autopost:");
		TestRunJob testRunJob = null;
		try {
			testRunJob=environmentService.getTestRunJob(testRunListId);
			TestRunPlan testRunPlan =testRunJob.getTestRunPlan();
			Set<WorkPackageTestCaseExecutionPlan> workPackageTestCaseExecutionPlans=testRunJob.getWorkPackageTestCaseExecutionPlans();
			for (WorkPackageTestCaseExecutionPlan workPackageTestCaseExecutionPlan : workPackageTestCaseExecutionPlans) {
				if(workPackageTestCaseExecutionPlan!=null && workPackageTestCaseExecutionPlan.getTestCaseExecutionResult()!=null && workPackageTestCaseExecutionPlan.getTestCaseExecutionResult().getTestExecutionResultBugListSet()!=null)
					bugs.addAll(workPackageTestCaseExecutionPlan.getTestCaseExecutionResult().getTestExecutionResultBugListSet());
			}

			ProductVersionListMaster productVersionList = testRunPlan.getProductVersionListMaster();
			ProductMaster productMaster = productVersionList.getProductMaster();

			DefectManagementSystem defectManagementSystem = defectManagementService.getPrimaryDMSByProductId(productMaster.getProductId());

			if(testRunPlan.getAutoPostBugs()!=1 && (bugs==null || bugs.isEmpty())){
				log.info("No primary defect management systems are configured to the Product:");
			} else {
				if(testRunPlan.getAutoPostBugs().equals("1") || testRunPlan.getAutoPostBugs().equals(1) || testRunPlan.getAutoPostBugs()==1){
					if(defectManagementSystem!=null ){
						if( defectManagementSystem.getToolIntagration() != null && defectManagementSystem.getToolIntagration().getName().trim().equalsIgnoreCase(IDPAConstants.JIRA_TOOL)){
							log.info("Primary defect management system is JIRA");
							defectSystemId = defectManagementSystem.getDefectManagementSystemId();
							toolsController.testDefectJiraExportTestRunJob(testRunJob.getTestRunJobId(), request, response, defectSystemId+"");
							if (testRunJob.getTestRunFailureMessage() != null) 
								testRunJob.setTestRunFailureMessage(testRunJob.getTestRunFailureMessage() + System.lineSeparator() + "Auto posted successfully to JIRA");
							else 
								testRunJob.setTestRunFailureMessage("Auto posted successfully to JIRA");
							environmentService.updateTestRunJob(testRunJob);
							jTableSingleResponse = new JTableSingleResponse("Auto Defects posted successfully to JIRA ");
							return jTableSingleResponse;
						}else if(defectManagementSystem.getToolIntagration() != null && defectManagementSystem.getToolIntagration().getName().trim().equalsIgnoreCase(IDPAConstants.HPQC_TOOL)){
							defectSystemId = defectManagementSystem.getDefectManagementSystemId();
							log.info("Primary defect management system is HPQC");
							toolsController.defectsExportHPQCTestRunJob(testRunJob.getTestRunJobId(),request,defectSystemId);
							if (testRunJob.getTestRunFailureMessage() != null) 
								testRunJob.setTestRunFailureMessage(testRunJob.getTestRunFailureMessage() + System.lineSeparator() + "Auto posted successfully to HPQC");
							else 
								testRunJob.setTestRunFailureMessage("Auto posted successfully to HPQC");
							environmentService.updateTestRunJob(testRunJob);

							jTableSingleResponse = new JTableSingleResponse("Auto Defects posted successfully to HPQC ");
							return jTableSingleResponse;
						}else if(defectManagementSystem.getToolIntagration().getName().trim().equalsIgnoreCase(IDPAConstants.TFS_TOOL)){
							defectSystemId = defectManagementSystem.getDefectManagementSystemId();
							log.info("Primary defect management system is TFS");
							toolsController.defectsExportTFSTestRunJob(testRunJob.getTestRunJobId(),request,defectSystemId, defectManagementSystem);
							if (testRunJob.getTestRunFailureMessage() != null) 
								testRunJob.setTestRunFailureMessage(testRunJob.getTestRunFailureMessage() + System.lineSeparator() + "Auto posted successfully to TFS");
							else 
								testRunJob.setTestRunFailureMessage("Auto posted successfully to TFS");
							environmentService.updateTestRunJob(testRunJob);

							jTableSingleResponse = new JTableSingleResponse("Auto Defects posted successfully to TFS ");
							return jTableSingleResponse;
						} /*else if(defectManagementSystem.getToolIntagration().getName().trim().equalsIgnoreCase(IDPAConstants.TESTLINK_TOOL)){
							defectSystemId = defectManagementSystem.getDefectManagementSystemId();
							log.info("Primary defect management system is TESTLINK");
							String defectsJson=toolsController.defectsExportTestLinkTestRunJob(testRunJob.getTestRunJobId(),request,defectSystemId, defectManagementSystem);
							
							if (testRunJob.getTestRunFailureMessage() != null) 
								testRunJob.setTestRunFailureMessage(testRunJob.getTestRunFailureMessage() + System.lineSeparator() + "Auto posted successfully to TEST LINK");
							else 
								testRunJob.setTestRunFailureMessage("Auto posted successfully to TESTLINK");
							
							environmentService.updateTestRunJob(testRunJob);
							String systemConnectionDetailsJson="";
							customTestSystemConnectorsManager.reportDefectsToSystem(systemConnectionDetailsJson, defectsJson);
						}*/ else {
							
							customTestSystemConnectorsManager.initializeTestSystemConnectorsManager(defectManagementSystem.getToolIntagration().getName(),defectManagementSystem.getDefectSystemVersion(),"Defect Management System",testlink_2_DefectManagementSystem);
							if (customTestSystemConnectorsManager.isConnectorAvailable(defectManagementSystem.getToolIntagration().getName(),defectManagementSystem.getDefectSystemVersion(),"Defect Management System")) {
								//Post the results/defects to the system
								String defectsJson=toolsController.defectsExportTestLinkTestRunJob(testRunJob.getTestRunJobId(),request,defectSystemId, defectManagementSystem);
								String systemConnectionDetailsJson="";
								customTestSystemConnectorsManager.reportDefectsToSystem(systemConnectionDetailsJson, defectsJson);
							} else {
								log.info("Connector for the specified Test Management System & version is not available.");
							}

							log.info("Test Results not exported as the Test management system is not supported yet : " + defectManagementSystem.getToolIntagration().getName());
							jTableSingleResponse = new JTableSingleResponse("Test Results not exported as the Test management system is not supported yet : " + defectManagementSystem.getToolIntagration().getName());
						}
					}else{
						log.info("No primary defect management systems are configured to the Product:");
						if (testRunJob.getTestRunFailureMessage() != null) 
							testRunJob.setTestRunFailureMessage(testRunJob.getTestRunFailureMessage() + "Error posting defects to Defect Management System. No primary defect management systems are configured to the Product");
						else
							testRunJob.setTestRunFailureMessage("Error posting defects to Defect Management System. No primary defect management systems are configured to the Product");
						environmentService.updateTestRunJob(testRunJob);
						jTableSingleResponse = new JTableSingleResponse("No primary defect management systems are configured to the Product");
						return jTableSingleResponse;
					}
				}
			}
			jTableSingleResponse = new JTableSingleResponse("Defects Autopost is not set. Not posting defects to defect management system");
		} catch (Exception e) {
			jTableSingleResponse = new JTableSingleResponse("ERROR","Error posting defects to Defect Management System");
			if (testRunJob.getTestRunFailureMessage() != null) 
				testRunJob.setTestRunFailureMessage(testRunJob.getTestRunFailureMessage() + System.lineSeparator() + "Error posting defects to Test Management System" + e.getMessage());
			else 
				testRunJob.setTestRunFailureMessage("Error posting defects to Test Management System" + e.getMessage());
			environmentService.updateTestRunJob(testRunJob);
			log.error("JSON ERROR", e);
		}
		return jTableSingleResponse;
	}

	public   String runDeviceReport_PDF(Integer testRunJobId,HttpServletRequest request,HttpServletResponse response) throws Exception {

		JTableResponse jTableResponse;
		TestRunJob testRunJob=null;
		ProductType productType =null;
		String exportLocation =null;
		String serverFolderPath = null;
		String reportType=null;
		String loginUserName = "";

		if(System.getProperty("os.name").contains("Linux")){
			serverFolderPath = CommonUtility.getCatalinaPath() + IDPAConstants.JASPERREPORTS_PATH_LINUX;
		} else {
			serverFolderPath = CommonUtility.getCatalinaPath() + IDPAConstants.JASPERREPORTS_PATH;
		}        	

		String testSuiteName = "";//Changes for Bugzilla bug 784 - Report customizations
		String workpackageName="";
		Integer testRunConfigurationChildId = 0;
		String deviceId = null;
		Integer testRunNo = 0;

		String imageFileName = "";
		BufferedImage logo = null;

		String imageServerPath = null;
		if(System.getProperty("os.name").contains("Linux")){
			imageServerPath= CommonUtility.getCatalinaPath().concat("/webapps/Logo/");
		} else {
			imageServerPath= CommonUtility.getCatalinaPath().concat("\\webapps\\Logo\\");
		}

		if(testRunJobId!=-1){  // Test Run Job Level
			testRunJob=workPackageService.getTestRunJobById(testRunJobId);
			testRunNo=testRunJob.getWorkPackage().getWorkPackageId();
			if(testRunJob.getWorkPackage()!=null){
				loginUserName = testRunJob.getWorkPackage().getUserList().getLoginId();
			}
			if(testRunJob.getWorkPackage().getTestRunPlan()!=null){
				testRunConfigurationChildId=testRunJob.getWorkPackage().getTestRunPlan().getTestRunPlanId();
				testSuiteName = testRunJob.getTestSuite().getTestSuiteName();
				productType =testRunJob.getWorkPackage().getTestRunPlan().getProductVersionListMaster().getProductMaster().getProductType();
				log.info("product Type Master Name "+productType.getTypeName());
				imageFileName = testRunJob.getWorkPackage().getTestRunPlan().getProductVersionListMaster().getProductMaster().getCustomer().getImageURI();	
				log.info("ImageFileName==>"+imageFileName);
			}
			if(testRunJob.getGenericDevices()!=null){
				deviceId= testRunJob.getGenericDevices().getUDID();
			}
		}else{ // Work Package Level
			WorkPackage wp=workPackageService.getWorkPackageById(testRunNo);
			if(wp!=null){				
				loginUserName = wp.getUserList().getLoginId();
			}
			workpackageName = wp.getName();
			productType=wp.getProductBuild().getProductVersion().getProductMaster().getProductType();
			if(wp.getTestRunPlan()!=null){
				testRunConfigurationChildId=wp.getTestRunPlan().getTestRunPlanId();
				imageFileName = wp.getTestRunPlan().getProductVersionListMaster().getProductMaster().getCustomer().getImageURI();	
				log.info("ImageFileName==>"+imageFileName);
			}					
		}

		if(imageFileName != null){
			imageServerPath = imageServerPath.concat(imageFileName);	
			log.info("ImageServerPath==>"+imageServerPath);
		} else {
			if(System.getProperty("os.name").contains("Linux")){
				imageServerPath= CommonUtility.getCatalinaPath().concat("/webapps/iLCM/css/images/noimage.jpg");
			} else {
				imageServerPath= CommonUtility.getCatalinaPath().concat("\\webapps\\iLCM\\css\\images\\noimage.jpg");
			}	
		}
		File imageFile = new File(imageServerPath);
		if(imageFile.exists() && imageFile != null){
			logo = ImageIO.read(imageFile);	
		}

		if(testRunNo!=-1){
			serverFolderPath=serverFolderPath+File.separator+"WorkPackages"+File.separator+testRunNo;
			exportLocation = serverFolderPath+File.separator+"TestRun-"+testRunNo+".pdf";
			log.info("wp exportLocation==>"+exportLocation);
		}else{
			serverFolderPath=serverFolderPath+File.separator+"Jobs"+File.separator+testRunJobId;
			exportLocation = serverFolderPath+File.separator+"TestRunJob-"+testRunJobId+"-"+testSuiteName+".pdf";
			log.info("job exportLocation==>"+exportLocation);
		}

		log.info("Report Inputs : " + testRunNo + " : " + testRunConfigurationChildId);

		JasperPrint jasperPrint=null;

		String strContentType=null;
		String strPrintMode=null;

		strPrintMode="PDF";
		strContentType="application/pdf";

		if (deviceId!=null && !deviceId.equals("")){
			log.info("Going to TestRunDeviceListReport : " + testRunNo + " : " + testRunConfigurationChildId);
			jasperPrint=report.generateTestRunDeviceListReport(testRunNo,testRunJobId,deviceId,testRunConfigurationChildId,strPrintMode,logo,loginUserName,reportType);
		}else {
			log.info("Going to TestRunListReport : " + testRunNo + " : " + testRunConfigurationChildId);
			jasperPrint=report.generateTestRunListReport(testRunNo,testRunConfigurationChildId,strPrintMode,productType,logo,loginUserName);
		}



		File file = new File(serverFolderPath);
		if (!file.isDirectory()) {
			file.mkdirs();
		}
		FileOutputStream  fos=new FileOutputStream(exportLocation);
		JRPdfExporter jRPdfExporter =new JRPdfExporter();
		jRPdfExporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
		jRPdfExporter.setParameter(JRExporterParameter.OUTPUT_STREAM, fos );

		jRPdfExporter.exportReport();

		return exportLocation;
	}




	//Method for getting the aborted jobs from the server
	@RequestMapping(value="client.test.run.get.abortedjobs",method=RequestMethod.POST ,produces="application/json")
	public @ResponseBody JTableResponse getAbortedTestRunList(@RequestParam int hostId) {
		JTableResponse jTableResponse;

		try {

			List<TestRunList> testRunList = testExecutionService.listByHostId(hostId,"");// TestExecutionStatus.ABORTED.toString());

			List<JsonTestRunList> jsonTestRunLists =new ArrayList<JsonTestRunList>();
			if(testRunList!=null)
				for(TestRunList trl:testRunList){
					JsonTestRunList jsonTestRunList = new JsonTestRunList(trl);
					jsonTestRunLists.add(jsonTestRunList);
				}

			jTableResponse = new JTableResponse("OK",jsonTestRunLists ,jsonTestRunLists.size());
			testRunList = null;
		} catch (Exception e) {
			jTableResponse = new JTableResponse("ERROR","Error Getting record!");
			log.error("JSON ERROR", e);
		}

		return jTableResponse;
	}

	//client.test.run.get.jobStatus
	//Changes for getting current job status
	@RequestMapping(value="client.test.run.get.jobStatus",method=RequestMethod.POST ,produces="application/json")
	public @ResponseBody JTableSingleResponse getCurrentJobStatus(@RequestParam int testRunListId) {
		JTableSingleResponse response = null;
		log.info("client.test.run.get.jobStatus");
		try {
			log.info("Status request from terminal for Job : " + testRunListId);
			int selectiveTestCaseFlag=0;
			TestRunJob testRunJob= workPackageExecutionService.getTestRunJobById(testRunListId);
			List<TestCaseList> testCaseLists=workPackageService.getSelectedTestCasesFromTestRunJob(testRunJob.getTestRunJobId());
			if(testCaseLists!=null && !testCaseLists.isEmpty())
				selectiveTestCaseFlag=1;
			if(testRunJob!=null){
				log.info("Status request from terminal for Job : " + testRunListId + " : Found Job");
				JsonTestRunList jsonTestRun = new JsonTestRunList(testRunJob,selectiveTestCaseFlag);
				response = new JTableSingleResponse("OK",jsonTestRun);
				log.info("Status request from terminal for Job : " + testRunListId + " : " + jsonTestRun.getTestResultStatus());
			}else{
				log.info("No Status" );
				response = new JTableSingleResponse("ERROR", "Some unknown problem in getting Job status");
			}
		}catch (Exception e) {
			response = new JTableSingleResponse("ERROR", "Some unknown problem in getting job status");
			log.error("Some unknown problem in executing Test Run", e);	            
		}
		return response;
	}

	@RequestMapping(value="client.util.genericdevice.unregister",method=RequestMethod.POST ,produces="application/json")
	public @ResponseBody JTableSingleResponse unRegisterDevice(@ModelAttribute JsonGenericDevice jsonGenericDeviceList, BindingResult result) {
		JTableSingleResponse jTableSingleResponse;
		if(result.hasErrors()){
			jTableSingleResponse  = new JTableSingleResponse("ERROR","Invalid form!"); 
		}

		try {

			GenericDevices deviceList=jsonGenericDeviceList.getGenericDevices();
			deviceList = environmentService.getGenericDevices(deviceList.getUDID());
			deviceList.setStatus(0);
			deviceList.setAvailableStatus(0);
			deviceListService.genericupdate(deviceList);

			jTableSingleResponse = new JTableSingleResponse("OK",jsonGenericDeviceList);
		} catch (Exception e) {
			jTableSingleResponse = new JTableSingleResponse("ERROR","Error updating record!");
			log.error("JSON ERROR", e);
		}


		return jTableSingleResponse;
	}


	@RequestMapping(value="client.test.suite.get.dynamic.plan",method=RequestMethod.POST ,produces="application/json")
	public @ResponseBody JTableResponse getTestSuiteByPlan(@RequestParam int testSuiteId, @RequestParam int testRunJobId, HttpServletRequest request) {
		JTableResponse jTableResponse;		
		try {

			TestSuiteList testSuiteList = testSuiteConfigurationService.getByTestSuiteId(testSuiteId);

			List<JsonTestSuiteList> tmpList =new ArrayList();
			JsonTestSuiteList jsonTestSuiteList = new JsonTestSuiteList(testSuiteList);

			if (testSuiteList.getTestScriptSource()!= null && !testSuiteList.getTestScriptSource().equals(TAFConstants.TEST_SCRIPT_SOURCE_TAF) &&  !testSuiteList.getTestScriptSource().equals(TAFConstants.TEST_SCRIPT_SOURCE_HUDSON) ) {
				String testSuitePath = "http://" + request.getServerName()+":" + request.getServerPort()+request.getContextPath()+"/TestScripts/";
				String testScriptFileLocation = testSuiteConfigurationService.constructTestScriptFileNameForExternalScriptSource(
						testSuiteList, testRunJobId,testSuitePath);
				jsonTestSuiteList.setTestSuiteScriptFileLocation(testScriptFileLocation);
			}
			tmpList.add(jsonTestSuiteList);

			jTableResponse = new JTableResponse("OK",tmpList ,1);
			testSuiteList = null;
		} catch (Exception e) {
			jTableResponse = new JTableResponse("ERROR","Error Getting record!");
			log.error("JSON ERROR", e);
		}
		return jTableResponse;
	}


	/*			@RequestMapping(value="client.test.run.job.post",method=RequestMethod.POST ,produces="application/json")
		    public @ResponseBody JTableResponse addTestRunJob(@ModelAttribute JsonTestRunJob jsonTestRunJob, BindingResult result) {
				JTableResponse jTableResponse;
				if(result.hasErrors()){
					jTableResponse = new JTableResponse("ERROR","Invalid form!"); 
				}
				try {

						TestRunJob testRunJob = null;
						if(jsonTestRunJob.getTestRunJobId()!=null){
							testRunJob=environmentService.getTestRunJobById(jsonTestRunJob.getTestRunJobId());
							TestRunJob testRunJobDB = environmentService.getTestRunJobById(jsonTestRunJob.getTestRunJobId());
							if (testRunJobDB != null && testRunJobDB.getTestRunEvidenceStatus() != null)
							{	
								testRunJob.setTestRunEvidenceStatus(testRunJobDB.getTestRunEvidenceStatus());
							}		
							if(jsonTestRunJob.getTestRunStatus()!=null && jsonTestRunJob.getTestRunStatus().equals(5)){
								testRunJob.setTestRunStatus(jsonTestRunJob.getTestRunStatus());
								WorkPackage workPackage =testRunJob.getWorkPackage();
								WorkFlow workFlow=workPackageService.getWorkFlowByEntityIdStageId(IDPAConstants.WORKPACKAGE_ENTITY_ID, IDPAConstants.WORKFLOW_STAGE_ID_COMPLETED);
								WorkFlowEvent workFlowEvent = new WorkFlowEvent();
								workFlowEvent.setEventDate(DateUtility.getCurrentTime());
								workFlowEvent.setRemarks("Workapckage :"+workPackage.getName()+" Completed");
								UserList user= userListService.getUserByLoginId(IDPAConstants.ADMIN_LOGIN_ID);
								workFlowEvent.setUser(user);
								workFlowEvent.setWorkFlow(workFlow);
								workFlowEvent.setEntityTypeRefId(workPackage.getWorkPackageId());
								workPackage.setWorkFlowEvent(workFlowEvent);
								workPackageService.addWorkFlowEvent(workFlowEvent);
								workPackageService.updateWorkPackage(workPackage);

							}
							environmentService.updateTestRunJob(testRunJob);

							if(testRunJob!=null){
								if(testRunJob.getTestRunJobId()!=null)
								log.info("Updating Testrun to Mongo ID "+testRunJob.getTestRunJobId());
								mongoDBService.addTestRunJobToMongoDB(testRunJob.getTestRunJobId());
							}


							log.info("TestResult Status: " + testRunJob.getTestRunStatus());

						}else{
							testRunJob=jsonTestRunJob.getTestRunJob();
							environmentService.addTestRunJob(testRunJob);
						}				

						List<JsonTestRunJob> tmpList =new ArrayList();
						jsonTestRunJob.setTestRunJobId(testRunJob.getTestRunJobId());
						tmpList.add(jsonTestRunJob);
			            jTableResponse = new JTableResponse("OK",tmpList ,1);
			        } catch (Exception e) {
			            jTableResponse = new JTableResponse("ERROR","Error adding record!");
			            log.error("JSON ERROR", e);
			        }

		        return jTableResponse;
		    }
	 */
	/*			@RequestMapping(value="client.test.run.job.end",method=RequestMethod.POST ,produces="application/json")
		    public @ResponseBody JTableResponse updateStatusTestRunJob(@ModelAttribute JsonTestRunJob jsonTestRunJob, BindingResult result) {
				JTableResponse jTableResponse;
				if(result.hasErrors()){
					jTableResponse = new JTableResponse("ERROR","Invalid form!"); 
				}
				try {

						TestRunJob testRunJob = null;
						if(jsonTestRunJob.getTestRunJobId()!=null){
							testRunJob=environmentService.getTestRunJobById(jsonTestRunJob.getTestRunJobId());
							TestRunJob testRunJobDB = environmentService.getTestRunJobById(jsonTestRunJob.getTestRunJobId());
							if (testRunJobDB != null && testRunJobDB.getTestRunEvidenceStatus() != null)
							{	
								testRunJob.setTestRunEvidenceStatus(testRunJobDB.getTestRunEvidenceStatus());
							}		
							testRunJob.setTestRunStatus(jsonTestRunJob.getTestRunStatus());
							environmentService.updateTestRunJob(testRunJob);

							if(testRunJob!=null){
								if(testRunJob.getTestRunJobId()!=null)
								log.info("Updating Testrun to Mongo ID "+testRunJob.getTestRunJobId());
								mongoDBService.addTestRunJobToMongoDB(testRunJob.getTestRunJobId());
							}

							log.info("TestResult Status: " + testRunJob.getTestRunStatus());
							log.info("TestResult Status: " + testRunJob.getTestRunJobId());

						}else{
							testRunJob=jsonTestRunJob.getTestRunJob();
							environmentService.addTestRunJob(testRunJob);
						}				

						List<JsonTestRunJob> tmpList =new ArrayList();
						jsonTestRunJob.setTestRunJobId(testRunJob.getTestRunJobId());
						tmpList.add(jsonTestRunJob);
			            jTableResponse = new JTableResponse("OK",tmpList ,1);
			        } catch (Exception e) {
			            jTableResponse = new JTableResponse("ERROR","Error ending Jobd!");
			            log.error("JSON ERROR", e);
			        }
		        return jTableResponse;
		    }
	 */
	/*			@RequestMapping(value="client.test.case.result.add",method=RequestMethod.POST ,produces="application/json")
		    public @ResponseBody JTableResponse addTestCaseExecutionResult(@ModelAttribute JsonWorkPackageTestCaseExecutionPlanForTerminal jsonWorkPackageTestCaseExecutionPlan, BindingResult result) {

				JTableResponse jTableResponse;
				if(result.hasErrors()){
					jTableResponse  = new JTableResponse("ERROR","Invalid form!"); 
				}

				try {
					if (jsonWorkPackageTestCaseExecutionPlan.getTestcaseName() == null || jsonWorkPackageTestCaseExecutionPlan.getTestcaseName().trim().isEmpty()) {
						jsonWorkPackageTestCaseExecutionPlan.setTestcaseName(jsonWorkPackageTestCaseExecutionPlan.getTestStepsName());
						jsonWorkPackageTestCaseExecutionPlan.setTestcaseDescription(jsonWorkPackageTestCaseExecutionPlan.getDescription());
					}
					String errorMessage = ValidationUtility.validateForNewTER(jsonWorkPackageTestCaseExecutionPlan);	

					if (errorMessage != null) {
					   	jTableResponse = new JTableResponse("ERROR",errorMessage);
					   	log.info("Received and Failed to Add TER. Mandatory fields missing. " + errorMessage);
						return jTableResponse;
					}
					WorkPackageTestCaseExecutionPlan workPackageTestCaseExecutionPlanFromTerminal=jsonWorkPackageTestCaseExecutionPlan.getDecodedTestExecutionResult();
					TestRunJob testRunJob = environmentService.getTestRunJobById(jsonWorkPackageTestCaseExecutionPlan.getTestRunJobId());
					WorkPackage workPackage= workPackageService.getWorkPackageById(jsonWorkPackageTestCaseExecutionPlan.getWorkPackageId());

					jsonWorkPackageTestCaseExecutionPlan.setProductVersionId(workPackage.getProductBuild().getProductVersion().getProductVersionListId());
					jsonWorkPackageTestCaseExecutionPlan.setProductVersionName(workPackage.getProductBuild().getProductVersion().getProductVersionName());
					jsonWorkPackageTestCaseExecutionPlan.setProductId(workPackage.getProductBuild().getProductVersion().getProductMaster().getProductId());
					jsonWorkPackageTestCaseExecutionPlan.setTestsuiteId(testRunJob.getTestSuite().getTestSuiteId());

					TestCaseList testCaseList=testCaseService.getTestCaseById(workPackageTestCaseExecutionPlanFromTerminal.getTestCase().getTestCaseId());
					//commented for taf intergation
					TestSuiteList testSuiteList=testSuiteConfigurationService.getByTestSuiteId(workPackageTestCaseExecutionPlanFromTerminal.getTestSuiteList().getTestSuiteId());

					WorkPackageTestCaseExecutionPlan workPackageTestCaseExecutionPlan = workPackageService.workPackageTestCasesExecutionPlanByJobId(testRunJob,testCaseList);
					WorkPackageTestSuiteExecutionPlan workPackageTestSuiteExecutionPlan = workPackageService.workPackageTestSuiteExecutionPlanByJobId(testRunJob,testSuiteList);

					WorkFlowEvent workFlowEvent = new WorkFlowEvent();
					List<WorkPackageTestSuite> workPackageTestSuites = new ArrayList<WorkPackageTestSuite>();

					WorkPackageTestSuite workPackageTestSuite = workPackageService.workPackageTestCasesByJobId(testRunJob);
					if(workPackageTestSuite==null){
							workPackageTestSuite = new WorkPackageTestSuite();
							workPackageTestSuite.setTestSuite(testSuiteList);
							workPackageTestSuite.setWorkPackage(workPackage);
							workPackageTestSuite.setCreatedDate(new Date(System.currentTimeMillis()));
							workPackageTestSuite.setEditedDate(new Date(System.currentTimeMillis()));
							workPackageTestSuite.setIsSelected(1);
							workPackageTestSuite.setStatus("ACTIVE");
							if(workPackageTestSuite.getWorkPackage().getWorkFlowEvent().getWorkFlow().getStageId()!=IDPAConstants.WORKFLOW_STAGE_ID_PLANNING&& workPackageTestSuite.getWorkPackage().getWorkFlowEvent().getWorkFlow().getStageValue()<20){
								WorkFlow workFlow=workPackageService.getWorkFlowByEntityIdStageId(IDPAConstants.WORKPACKAGE_ENTITY_ID, IDPAConstants.WORKFLOW_STAGE_ID_PLANNING);
								workFlowEvent = new WorkFlowEvent();
								workFlowEvent.setEventDate(DateUtility.getCurrentTime());
								workFlowEvent.setRemarks("Planning Workapckage :"+workPackage.getName());
								UserList userAdmin= userListService.getUserByLoginId(IDPAConstants.ADMIN_LOGIN_ID);
								workFlowEvent.setUser(userAdmin);
								workFlowEvent.setWorkFlow(workFlow);
								workPackage.setWorkFlowEvent(workFlowEvent);
								workPackageService.addWorkFlowEvent(workFlowEvent);
								workPackageService.updateWorkPackage(workPackage);
							}
							workPackageTestSuites.add(workPackageTestSuite);
							workPackageService.mapWorkpackageWithTestSuite(workPackageTestSuite.getWorkPackage().getWorkPackageId(),testSuiteList.getTestSuiteId(),"Add");

						log.info("New testcases to be added for the workpackage : "+ workPackage.getWorkPackageId() + " : count : " + workPackageTestSuites.size());
						int workPackagesTestSuiteCount = workPackageService.addNewWorkPackageTestSuite(workPackageTestSuites);
						Set<WorkPackageTestSuite> workPackageTS=new HashSet<WorkPackageTestSuite>();
						workPackageTS.addAll(workPackageTestSuites);
						workPackage.setWorkPackageTestSuites(workPackageTS);
						workPackageService.updateWorkPackage(workPackage);
					}
					//Mapping test suite to workpackage
					workPackage=workPackageService.getWorkPackageById(workPackage.getWorkPackageId());

					Set<WorkPackageTestSuite> wpts=workPackage.getWorkPackageTestSuites();

                    TestRunPlan testRunPlan=testRunJob.getTestRunPlan(); 
                    RunConfiguration runConfiguration = testRunJob.getRunConfiguration();

					WorkpackageRunConfiguration wpRunConfiguration= new WorkpackageRunConfiguration();
					workPackageService.addRunConfigurationWorkpackage(workPackage.getWorkPackageId(),runConfiguration.getRunconfigId(),"testsuite");
                    workPackageService.addRunConfigurationWorkpackage(workPackage.getWorkPackageId(),runConfiguration.getRunconfigId(),"testcase");

                	wpRunConfiguration=workPackageService.getWorkpackageRunConfiguration(workPackage.getWorkPackageId(), runConfiguration.getRunconfigId(), "testsuite");


					if(workPackageTestSuiteExecutionPlan==null){
						workPackageTestSuiteExecutionPlan = new WorkPackageTestSuiteExecutionPlan();
						workPackageTestSuiteExecutionPlan.setCreatedDate(DateUtility.getCurrentTime());
						workPackageTestSuiteExecutionPlan.setTestsuite(testSuiteList);
						workPackageTestSuiteExecutionPlan.setModifiedDate(DateUtility.getCurrentTime());
						workPackageTestSuiteExecutionPlan.setPlannedExecutionDate(workPackage.getPlannedStartDate());
						workPackageTestSuiteExecutionPlan.setRunConfiguration(wpRunConfiguration);
						workPackageTestSuiteExecutionPlan.setWorkPackage(workPackage);
						workPackageTestSuiteExecutionPlan.setStatus(1);

						ExecutionPriority executionPriority=null;
						if(workPackageTestSuite.getTestSuite().getExecutionPriority()!=null)
							executionPriority= workPackageService.getExecutionPriorityByName(CommonUtility.getExecutionPriority(workPackageTestSuite.getTestSuite().getExecutionPriority().getPriorityName()));
						else
							executionPriority= workPackageService.getExecutionPriorityByName(IDPAConstants.EXECUTION_PRIORITY_MEDIUM);
						workPackageTestSuiteExecutionPlan.setExecutionPriority(executionPriority);


						workPackageService.addWorkpackageTestSuiteExecutionPlan(workPackageTestSuiteExecutionPlan);

					}
					if(workPackageTestCaseExecutionPlan==null){

						workPackageTestCaseExecutionPlan= new WorkPackageTestCaseExecutionPlan();

						workPackageTestCaseExecutionPlan.setTestCase(testCaseList);
						workPackageTestCaseExecutionPlan.setTestSuiteList(testSuiteList);

						workPackageTestCaseExecutionPlan.setWorkPackage(workPackage);


						EnvironmentCombination environmentCombination= testRunJob.getEnvironmentCombination();
	                    GenericDevices genericDevices=testRunJob.getGenericDevices();
	                    HostList hostList=testRunJob.getHostList();

						workPackageTestCaseExecutionPlan.setRunConfiguration(wpRunConfiguration);
						workPackageTestCaseExecutionPlan.setExecutionStatus(3);
						workPackageTestCaseExecutionPlan.setIsExecuted(0);
						workPackageTestCaseExecutionPlan.setHostList(hostList);
						workPackageTestCaseExecutionPlan.setStatus(1);

						ExecutionPriority executionPriority=null;
						if(testCaseList.getTestCasePriority()!=null)
							executionPriority= workPackageService.getExecutionPriorityByName(CommonUtility.getExecutionPriority(testCaseList.getTestCasePriority().getPriorityName()));
						else
							executionPriority= workPackageService.getExecutionPriorityByName(IDPAConstants.EXECUTION_PRIORITY_HIGH);
						workPackageTestCaseExecutionPlan.setExecutionPriority(executionPriority);
						if(testCaseList != null){
							List<ProductFeature> featureList = productListService.getFeaturesMappedToTestCase(testCaseList.getTestCaseId());
							if(featureList != null && !featureList.isEmpty())
								workPackageTestCaseExecutionPlan.setFeature(featureList.get(0));
							else 
								workPackageTestCaseExecutionPlan.setFeature(null);
						}
						workPackageTestCaseExecutionPlan.setSourceType("TestSuite");
						TestCaseExecutionResult testCaseExecutionResult = new TestCaseExecutionResult();
						TestCaseExecutionResult testCaseExecutionResultFromTerminal = workPackageTestCaseExecutionPlan.getTestCaseExecutionResult();

						testCaseExecutionResult.setResult(jsonWorkPackageTestCaseExecutionPlan.getResult());
						testCaseExecutionResult.setComments(jsonWorkPackageTestCaseExecutionPlan.getComments());
						testCaseExecutionResult.setDefectsCount(0);
						testCaseExecutionResult.setDefectIds("");
						testCaseExecutionResult.setIsApproved(0);
						testCaseExecutionResult.setIsReviewed(0);
						testCaseExecutionResult.setObservedOutput(jsonWorkPackageTestCaseExecutionPlan.getObservedOutput());

			            testCaseExecutionResult.setWorkPackageTestCaseExecutionPlan(workPackageTestCaseExecutionPlan);
			            workPackageTestCaseExecutionPlan.setTestCaseExecutionResult(testCaseExecutionResult);

			           workPackageService.updateWorkPackageTestCaseExecutionPlan(workPackageTestCaseExecutionPlan);
					}

		           List<TestStepExecutionResult> testStepExecutionResults= new ArrayList<TestStepExecutionResult>();

            		TestCaseStepsList testCaseStepsList= null;
            		if(jsonWorkPackageTestCaseExecutionPlan.getTestStepsId()!=null)
            			testCaseStepsList=testSuiteConfigurationService.getByTestStepId(jsonWorkPackageTestCaseExecutionPlan.getTestStepsId());
            		else
            			testCaseStepsList=testSuiteConfigurationService.getTestCaseStepByName(jsonWorkPackageTestCaseExecutionPlan.getTestStepsName(), workPackage.getProductBuild().getProductVersion().getProductMaster().getProductId());
            		TestStepExecutionResult testStepExecutionResult = new TestStepExecutionResult();
					testStepExecutionResult.setComments(jsonWorkPackageTestCaseExecutionPlan.getTestStepcomments());
					testStepExecutionResult.setIsApproved(0);
					testStepExecutionResult.setIsReviewed(0);
					testStepExecutionResult.setObservedOutput(jsonWorkPackageTestCaseExecutionPlan.getTestStepObservedOutput());
					testStepExecutionResult.setResult(jsonWorkPackageTestCaseExecutionPlan.getTestStepResult());
					testStepExecutionResult.setTestCaseExecutionResult(workPackageTestCaseExecutionPlan.getTestCaseExecutionResult());
					testStepExecutionResult.setTestcase(workPackageTestCaseExecutionPlan.getTestCase());
					testStepExecutionResult.setTestSteps(testCaseStepsList);
					testStepExecutionResults.add(testStepExecutionResult);


					if(testStepExecutionResults!=null && !testStepExecutionResults.isEmpty()){
						workPackageService.saveTestStepExecutionResult(testStepExecutionResults);
					}

					Evidence evidence=new Evidence();
					evidence.setDescription(null);
					EntityMaster entityMaster=workPackageService.getEntityMasterById(IDPAConstants.ENTITY_TEST_STEP_EVIDENCE_ID);
					String fileName=jsonWorkPackageTestCaseExecutionPlan.getEvidencePath();
					String extension="";
					if(fileName!=null){
						 extension=fileName.substring(fileName.lastIndexOf(".")).toLowerCase();
						fileName=CommonUtility.getEvidenceFileName(workPackageTestCaseExecutionPlan,extension);
					}

					evidence.setEvidencename(fileName);

					evidence.setEntityMaster(entityMaster);
					evidence.setFiletype(extension);
					if(System.getProperty("os.name").contains("Linux")){
						evidence.setFileuri(IDPAConstants.EVIDENCE_PATH_LINUX+"/"+workPackageTestCaseExecutionPlan.getWorkPackage().getWorkPackageId()+"/"+DateUtility.getCurrentDateInddmmyyyy()+ "/"+ fileName);
					} else {
						evidence.setFileuri(IDPAConstants.EVIDENCE_PATH+"\\"+workPackageTestCaseExecutionPlan.getWorkPackage().getWorkPackageId()+"\\"+DateUtility.getCurrentDateInddmmyyyy()+ "\\"+ fileName);
					}
					evidence.setEntityvalue(testStepExecutionResult.getTeststepexecutionresultid());
					evidence.setSize(null);

					workPackageService.addEvidence(evidence);

					List<JsonWorkPackageTestCaseExecutionPlan> tmpList =new ArrayList();
					workPackageTestCaseExecutionPlan=workPackageService.getWorkpackageTestcaseExecutionPlanById(workPackageTestCaseExecutionPlan.getId());
					JsonWorkPackageTestCaseExecutionPlan jwptcep= new JsonWorkPackageTestCaseExecutionPlan(workPackageTestCaseExecutionPlan);
					jwptcep.setId(workPackageTestCaseExecutionPlan.getId());

					tmpList.add(jwptcep);
					workPackageTestCaseExecutionPlan = null;
			        jTableResponse = new JTableResponse("OK",tmpList ,1);
			    } catch (Exception e) {
				   	jTableResponse = new JTableResponse("ERROR","Error adding record!");
				   	log.debug("Received and Failed to Add TER : ");
				   	log.error("JSON ERROR", e);
			   	}
				return jTableResponse;
			}
	 */
	/*			@RequestMapping(value="client.util.jobstatus.update",method=RequestMethod.POST ,produces="application/json")
			public @ResponseBody JTableSingleResponse updateJobStatus(@ModelAttribute JsonTestRunJob jsonTestRunJob, BindingResult result) {
				JTableSingleResponse jTableSingleResponse;
				if(result.hasErrors()){
					jTableSingleResponse  = new JTableSingleResponse("ERROR","Invalid form!"); 
				}

				try {

						TestRunJob testRunJob =jsonTestRunJob.getTestRunJob();
						testRunJob.setTestRunStatus(5);
						deviceListService.updateTestRunJob(testRunJob);
						if(testRunJob!=null){
							if(testRunJob.getTestRunJobId()!=null)
							log.info("Updating Testrun to Mongo ID "+testRunJob.getTestRunJobId());
							mongoDBService.addTestRunJobToMongoDB(testRunJob.getTestRunJobId());
						}

						WorkPackage workPackage= testRunJob.getWorkPackage();
						WorkFlowEvent workFlowEvent = new WorkFlowEvent();
						workFlowEvent.setEventDate(DateUtility.getCurrentTime());
						workFlowEvent.setRemarks("Workpackage Completed :"+workPackage.getName());
						workFlowEvent.setWorkFlow(workPackageService.getWorkFlowByEntityIdStageId(IDPAConstants.WORKPACKAGE_ENTITY_ID,IDPAConstants.WORKFLOW_STAGE_ID_COMPLETED));
						workFlowEvent.setEntityTypeRefId(workPackage.getWorkPackageId());
						workPackageService.addWorkFlowEvent(workFlowEvent);						
						workPackage.setWorkFlowEvent(workFlowEvent);
						workPackage.setModifiedDate(DateUtility.getCurrentTime());
						workPackageService.updateWorkPackage(workPackage);
						TestRunPlan testRunPlan= testRunJob.getTestRunPlan();
						Set<TestRunJob> testRunJobs=workPackage.getTestRunJobSet();
						Boolean flag=true;
						if(testRunPlan.getRunConfigurationList().size()==testRunJobs.size()){
							for (TestRunJob trj : testRunJobs) {
								if(trj.getTestRunStatus()!=5){
									flag=false;
									break;
								}
							}
							if(flag){
								if(workPackage.getSourceType().equals("TestRunPlanGroup")){
									TestRunPlanGroup testRunPlanGroup =workPackage.getTestRunPlanGroup();
									TestRunPlan nextTestRunPlan=workPackageService.getNextTestRunPlan(testRunPlanGroup,testRunPlan);
									workPackageService.addWorkpackageToTestRunPlan(nextTestRunPlan, null, null, null, testRunPlanGroup,workPackage);

								}
							}
						}
						jsonTestRunJob.setTestRunStatus(5);
						jTableSingleResponse = new JTableSingleResponse("OK",jsonTestRunJob);

			        } catch (Exception e) {
			        	jTableSingleResponse = new JTableSingleResponse("ERROR","Error updating record!");
			            log.error("JSON ERROR", e);
			        }


		        return jTableSingleResponse;
		    }
	 */			
	@RequestMapping(value="client.test.run.list.get.config.properties",method=RequestMethod.POST ,produces="application/json")
	public @ResponseBody JTableResponse getEntityCnfigurationPropertiesForJob(@RequestParam int testRunJobId) {
		JTableResponse jTableResponse;
		try {
			log.info("Getting configuration properties for job : "+ testRunJobId);
			TestRunJob testRunJob = workPackageService.getTestRunJobById(testRunJobId);
			if (testRunJob == null) {
				log.info("Unable to find Test Run Job : " + testRunJobId + ". Hence not able to fetch it's config properties");
				jTableResponse = new JTableResponse("ERROR","Unable to find Test Run Job : " + testRunJobId + ". Hence not able to fetch it's config properties");
				return jTableResponse;
			}
			Integer runConfigId = testRunJob.getRunConfiguration().getRunconfigId();
			List<EntityConfigurationProperties> jobConfigurationProperties = entityConfigurationPropertiesService.getEntityConfigurePropertiesByEntityId(runConfigId, IDPAConstants.ENTITY_RUN_CONFIGURATION_ID, -1);
			log.info("Job : " + testRunJobId + " : Job Configuration properties count : " + jobConfigurationProperties.size());
			testRunJob.getTestRunPlan();
			List<EntityConfigurationProperties> testRunPlanConfigurationProperties = entityConfigurationPropertiesService.getEntityConfigurePropertiesByEntityId(testRunJob.getTestRunPlan().getTestRunPlanId(), IDPAConstants.ENTITY_TEST_RUN_PLAN_ID, -1);
			log.info("Job : " + testRunJobId + " : Test Run Plan Configuration properties count : " + testRunPlanConfigurationProperties.size());

			List<JsonEntityConfigurationProperties> jsonEntityConfigurationProperties =  new ArrayList<JsonEntityConfigurationProperties>();
			if (testRunPlanConfigurationProperties != null) {
				for (EntityConfigurationProperties testRunPlanConfigurationProperty : testRunPlanConfigurationProperties) {
					JsonEntityConfigurationProperties jsontestRunPlanConfigurationProperty = new JsonEntityConfigurationProperties(testRunPlanConfigurationProperty);
					jsonEntityConfigurationProperties.add(jsontestRunPlanConfigurationProperty);
				}
			}

			if (jobConfigurationProperties != null) {
				for (EntityConfigurationProperties jobConfigurationProperty : jobConfigurationProperties) {
					/*boolean propertyFound = false;
					for (JsonEntityConfigurationProperties jsonEntityConfigurationProperty : jsonEntityConfigurationProperties) {
						if (jobConfigurationProperty.getEntityConfigurationPropertiesMaster().getName().equalsIgnoreCase(jsonEntityConfigurationProperty.getEntityConfigPropertiesMasterName())) {

							jsonEntityConfigurationProperty.setValue(jobConfigurationProperty.getValue());
							propertyFound = true;
							break;
						}
					}
					if (!propertyFound) {*/
						JsonEntityConfigurationProperties jsonJobConfigurationProperty = new JsonEntityConfigurationProperties(jobConfigurationProperty);
						jsonEntityConfigurationProperties.add(jsonJobConfigurationProperty);
					//}
				}
			}
			log.info("Job : " + testRunJobId + " : Job Configuration properties count sent to server : " + testRunPlanConfigurationProperties.size());
			jTableResponse = new JTableResponse("OK",jsonEntityConfigurationProperties ,jsonEntityConfigurationProperties.size());
		} catch (Exception e) {
			jTableResponse = new JTableResponse("ERROR","Error Getting TestRunJob config properties!");
			log.error("JSON ERROR", e);
		}

		return jTableResponse;
	}

	public void uploadReportsToTestSet( String filePath, Integer testRunJobId,TestManagementSystem testManagementSystem) {
		log.debug("Upload Reports to TestSet");
		ConnectorHPQCRest hpqcConnector;

		Boolean status = false;
		String entityId = "";
		try {
			log.info("testRunJobId input:" + testRunJobId);	
			TestRunJob testRunJob = workPackageService.getTestRunJobById(testRunJobId);
			int productId = testRunJob.getWorkPackage().getTestRunPlan().getProductVersionListMaster().getProductMaster().getProductId();

			entityId = testRunJob.getTestSuite().getTestSuiteCode();
			ProductMaster product = productListService.getProductById(productId);
			if(testManagementSystem.getTestSystemName().trim().equalsIgnoreCase("HPQC")){
				hpqcConnector =  toolsController.getHPQCConnectorRest(product, null);
				File fileForProcess = new File(filePath);			
				if(fileForProcess != null){
					status = tafTestManagementSystemIntegrator.UploadReportsToTestSet(testRunJob, hpqcConnector, fileForProcess, "test-sets", entityId);
				} 	
				ConnectorHPQCRest.logout(hpqcConnector);
			} 
			if(status != null){
				log.info("Reports upload to "+testManagementSystem.getTestSystemName().trim()+" TestSet Completed.");
			} else{
				log.info("Problem in Import Features");
			}
		} catch (Exception e) {
			log.error("JSON ERROR", e);
		}
	}	


	//Intel implementation - For Storage Drive registration
	@RequestMapping(value="client.util.storagedevice.register",method=RequestMethod.POST ,produces="application/json")
	public @ResponseBody JTableSingleResponse registerStorageDevice(@ModelAttribute JsonDeviceList jsonDeviceList, BindingResult result) {
		JTableSingleResponse jTableSingleResponse;
		if(result.hasErrors()){
			log.error("Errors in storage drive details received from terminal "  +   result.toString());
			jTableSingleResponse = new JTableSingleResponse("ERROR","Invalid form!"); 
		}
		JsonDeviceList josnList = null;
		try {
			GenericDevices deviceList = environmentService.getGenericDevices(jsonDeviceList.getDeviceId());
			GenericDevices tempList=jsonDeviceList.getGenericDeviceList(jsonDeviceList);

			boolean isRegisteringToNewHost=false;int hostId=-1;				
			if(deviceList==null){					
				//The device is a new one. Not present in the environment earlier. Add it
				String validation = ValidationUtility.validateForNewDeviceAddition(tempList);
				if (validation != null) {

					jTableSingleResponse = new JTableSingleResponse("ERROR","Unable to storage drive! Contact administrator. " + validation);
					log.error("Unable to add storage drive! Contact administrator. "+ validation);
					return jTableSingleResponse;
				} else {					
					DeviceMakeMaster deviceMakeMaster = null;
					DeviceModelMaster deviceModelMaster = null;
					if(jsonDeviceList.getDeviceMake() != null){
						deviceMakeMaster=deviceListService.getDeviceMakeMaster(jsonDeviceList.getDeviceMake());
						if(deviceMakeMaster != null ){
							deviceModelMaster=deviceListService.getDeviceModelMaster(jsonDeviceList.getDeviceModel());
							if(deviceModelMaster == null){
								deviceModelMaster = new DeviceModelMaster();
								deviceModelMaster.setDeviceMakeMaster(deviceMakeMaster);
								deviceModelMaster.setDeviceModel(jsonDeviceList.getDeviceModel());
								deviceModelMaster.setDeviceResolution(jsonDeviceList.getDeviceModel());
								int deviceModelMasterId =deviceListService.addDeviceModelMaster(deviceModelMaster);
								deviceModelMaster = deviceListService.getDeviceModelMasterById(deviceModelMasterId);
							}
						}
					}

					/* Adding Generic Device as StorageDrive Type */
					StorageDrive storageDriveType = new StorageDrive();						

					PlatformType platformType=deviceListService.getPlatFormType(jsonDeviceList.getDevicePlatformName(),jsonDeviceList.getDevicePlatformVersion());
					if(platformType != null) {
						if(!platformType.getVersion().equalsIgnoreCase(jsonDeviceList.getDevicePlatformVersion()) && jsonDeviceList.getDevicePlatformVersion()!=null && !jsonDeviceList.getDevicePlatformVersion().isEmpty()){
							//Adding A Platform with new version
							PlatformType platformnew = new PlatformType();
							platformnew.setName(platformType.getName());
							platformnew.setEntityMaster(platformType.getEntityMaster());
							log.info("Adding platform with new Version "+jsonDeviceList.getDevicePlatformVersion());
							platformnew.setVersion(jsonDeviceList.getDevicePlatformVersion());
							int platFormId =deviceListService.addPlatformType(platformnew);
							platformnew = deviceListService.getPlatFormType(platFormId);
							storageDriveType.setPlatformType(platformnew);
						}else{
							storageDriveType.setPlatformType(platformType);
						}
					}else if(jsonDeviceList.getDevicePlatformVersion()!=null && !jsonDeviceList.getDevicePlatformVersion().isEmpty()){//Adding A New Platform
						PlatformType platformnew = new PlatformType();
						platformnew.setName(jsonDeviceList.getDevicePlatformName());
						EntityMaster entMas = new EntityMaster();
						entMas = workPackageService.getEntityMasterById(7);							
						platformnew.setEntityMaster(entMas);
						platformnew.setVersion(jsonDeviceList.getDevicePlatformVersion());
						int platFormId =deviceListService.addPlatformType(platformnew);
						platformnew = deviceListService.getPlatFormType(platFormId);
						storageDriveType.setPlatformType(platformnew);
					}
					if(deviceMakeMaster != null && deviceMakeMaster.getDeviceMakeId() != 0){
						storageDriveType.setDeviceMakeMaster(deviceMakeMaster);	
					}
					if(deviceModelMaster != null){
						storageDriveType.setDeviceModelMaster(deviceModelMaster);
					}					

					storageDriveType.setStatus(1);
					String status=jsonDeviceList.getDeviceStatus();
					if(status.equalsIgnoreCase(IDPAConstants.DEVICE_ACTIVE)){
						storageDriveType.setAvailableStatus(1);
					}else if(status.equalsIgnoreCase(IDPAConstants.DEVICE_INACTIVE)){
						storageDriveType.setAvailableStatus(0);
					}
					storageDriveType.setUDID(jsonDeviceList.getDeviceId());
					storageDriveType.setName(jsonDeviceList.getDeviceId());
					storageDriveType.setFirmware(jsonDeviceList.getFirmwareInfo());
					storageDriveType.setBootLoader(jsonDeviceList.getBootloaderInfo());
					if(jsonDeviceList.getDeviceSpaceUnit()!=null && jsonDeviceList.getDeviceSpaceUnit() !=""){
						String[] size = jsonDeviceList.getDeviceSpaceUnit().split(" ");
						storageDriveType.setStorageSize(Double.valueOf(size[0]).longValue());
						storageDriveType.setStorageSizeUnit(size[1]);
					} else {
						storageDriveType.setStorageSize(new Long(0));
						storageDriveType.setStorageSizeUnit("");
					}						
					storageDriveType.setDriveVersion(jsonDeviceList.getDeviceVersion());
					HostList hostList= new HostList();
					hostList=hostListService.getHostById(jsonDeviceList.getHostId());
					storageDriveType.setHostList(hostList);
					DeviceLab deviceLab=deviceListService.getDeviceLabByDeviceLabId(1);
					storageDriveType.setDeviceLab(deviceLab);
					Processor processor = new Processor();
					processor.setProcessorId(14);
					storageDriveType.setProcessor(processor);
					environmentService.addGenericDevice((GenericDevices)storageDriveType);
					josnList= new JsonDeviceList(environmentService.getGenericDevices(storageDriveType.getGenericsDevicesId()));					
				}

			}else{
				//Device is an already registered one. Update it's info and availability
				if(jsonDeviceList!=null && deviceList!=null && deviceList.getHostList()!=null){
					if(!jsonDeviceList.getHostId().equals(deviceList.getHostList().getHostId())){
						isRegisteringToNewHost=true;
						hostId=deviceList.getHostList().getHostId();
					}		
				}
				deviceList=deviceList;
				if(jsonDeviceList.getHostId()!=null && deviceList.getHostList()!=null)
					deviceList.getHostList().setHostId(jsonDeviceList.getHostId());
				String status=jsonDeviceList.getDeviceStatus();
				if(status.equalsIgnoreCase(IDPAConstants.DEVICE_ACTIVE)){
					deviceList.setAvailableStatus(1);
				}else if(status.equalsIgnoreCase(IDPAConstants.DEVICE_INACTIVE)){
					deviceList.setAvailableStatus(0);
				}
				DeviceMakeMaster deviceMakeMaster = null;
				if(jsonDeviceList.getDeviceMake() != null){
					deviceMakeMaster=deviceListService.getDeviceMakeMaster(jsonDeviceList.getDeviceMake());
					if(deviceMakeMaster != null ){
						if(jsonDeviceList.getDeviceModel() != null){
							String devmodel = jsonDeviceList.getDeviceModel().trim();
							DeviceModelMaster devmodelmaster = deviceListService.getDeviceModelMaster(devmodel);
							if(devmodelmaster == null){
								devmodelmaster = new DeviceModelMaster();
								devmodelmaster.setDeviceMakeMaster(deviceMakeMaster);
								devmodelmaster.setDeviceModel(jsonDeviceList.getDeviceModel());
								devmodelmaster.setDeviceResolution(jsonDeviceList.getDeviceModel());
								int deviceModelMasterId =deviceListService.addDeviceModelMaster(devmodelmaster);
								devmodelmaster = deviceListService.getDeviceModelMasterById(deviceModelMasterId);
							}
							deviceList.setDeviceModelMaster(devmodelmaster);	
							StorageDrive strg = (StorageDrive) deviceList;
							strg.setDeviceMakeMaster(deviceMakeMaster);
							strg.setFirmware(jsonDeviceList.getFirmwareInfo());
							strg.setBootLoader(jsonDeviceList.getBootloaderInfo());
							if(jsonDeviceList.getDeviceSpaceUnit()!=null && jsonDeviceList.getDeviceSpaceUnit() !=""){
								String[] size = jsonDeviceList.getDeviceSpaceUnit().split(" ");
								strg.setStorageSize(Double.valueOf(size[0]).longValue());
								strg.setStorageSizeUnit(size[1]);
							} else {
								strg.setStorageSize(new Long(0));
								strg.setStorageSizeUnit("");
							}						
							strg.setDriveVersion(jsonDeviceList.getDeviceVersion());
							deviceList = strg;
						}
					} else if(deviceMakeMaster == null || deviceMakeMaster.equals(null) || deviceMakeMaster.equals("null")){						
						DeviceMakeMaster devMakeMaster = new DeviceMakeMaster();							
						devMakeMaster.setDeviceMake(jsonDeviceList.getDeviceMake().trim());						
						int devMakeId = deviceListService.addDeviceMakeMaster(devMakeMaster);
						devMakeMaster = deviceListService.getDeviceMakeMasterById(devMakeId);

						String devmodel = jsonDeviceList.getDeviceModel().trim();
						if(devmodel != null){
							DeviceModelMaster devmodelmaster = deviceListService.getDeviceModelMaster(devmodel);
							if(devmodelmaster == null){
								devmodelmaster = new DeviceModelMaster();
								devmodelmaster.setDeviceMakeMaster(devMakeMaster);
								devmodelmaster.setDeviceModel(jsonDeviceList.getDeviceModel());
								devmodelmaster.setDeviceResolution(jsonDeviceList.getDeviceModel());
								int deviceModelMasterId =deviceListService.addDeviceModelMaster(devmodelmaster);
								devmodelmaster = deviceListService.getDeviceModelMasterById(deviceModelMasterId);
							}
							deviceList.setDeviceModelMaster(devmodelmaster);

							Set<DeviceModelMaster>  devModelSet = new HashSet<>();
							devModelSet.add(devmodelmaster);
						}							
					}
				}

				PlatformType platformType=deviceListService.getPlatFormType(jsonDeviceList.getDevicePlatformName(),jsonDeviceList.getDevicePlatformVersion());
				if(platformType != null){
					if(!platformType.getVersion().equalsIgnoreCase(jsonDeviceList.getDevicePlatformVersion()) && jsonDeviceList.getDevicePlatformVersion()!=null && !jsonDeviceList.getDevicePlatformVersion().isEmpty()){
						//Adding A Platform with new version
						PlatformType platformnew = new PlatformType();
						platformnew.setName(platformType.getName());
						platformnew.setEntityMaster(platformType.getEntityMaster());								
						deviceList.setPlatformType(platformnew);
					}
				}else if(jsonDeviceList.getDevicePlatformVersion()!=null && !jsonDeviceList.getDevicePlatformVersion().isEmpty()){//Adding A New Platform
					PlatformType platformnew = new PlatformType();
					platformnew.setName(platformType.getName());
					platformnew.setEntityMaster(platformType.getEntityMaster());							
					deviceList.setPlatformType(platformnew);
				}						
				environmentService.updateGenericDevice(deviceList);
				josnList = new JsonDeviceList(deviceList);				     											
			}
			deviceList = null;
			jTableSingleResponse = new JTableSingleResponse("OK",josnList);
		} catch (Exception e) {
			e.printStackTrace();
			jTableSingleResponse = new JTableSingleResponse("ERROR","Unable to register storage drive!");
			log.error("Unable to register storage drive : " + jsonDeviceList.getDeviceId(), e);
		}	        
		return jTableSingleResponse;
	}


	@RequestMapping(value="client.testrunjob.testcaseexecutionorder",method=RequestMethod.POST ,produces="application/json")
	public @ResponseBody String getTestRunJobTestCaseList(@RequestParam int testRunListId,@RequestParam boolean isSelective) {	
		List<TestCaseList> testCases = null;
		if(testRunListId != -1){
			if(isSelective){
				TestRunJob trj = environmentService.getTestRunJobById(testRunListId);
				testCases =new ArrayList<TestCaseList>(workPackageService.getSelectedTestCasesFromTestRunJob(trj.getTestRunJobId()));			
			} else {
				TestRunJob trj = environmentService.getTestRunJobById(testRunListId);
				testCases = new ArrayList<TestCaseList>(trj.getTestSuite().getTestCaseLists());
			}
			if(testCases.size()>0){
				Iterator<TestCaseList> firstIt = testCases.iterator();
				boolean orderHasZeroflg = false;			
				while (firstIt.hasNext()) {
					Integer executionOrder = (Integer) firstIt.next().getTestCaseExecutionOrder();
					if(executionOrder == null || executionOrder == 0)
						orderHasZeroflg = true;
					if(orderHasZeroflg)
						break;
				}			
				if(orderHasZeroflg) {
					java.util.Collections.sort(testCases, new Comparator<TestCaseList>(){
						@Override
						public int compare(TestCaseList tcl1, TestCaseList tcl2) {
							return tcl1.getTestCaseId().compareTo(tcl2.getTestCaseId());
						}
					});
				} else {
					java.util.Collections.sort(testCases, new Comparator<TestCaseList>(){
						@Override
						public int compare(TestCaseList tcl1, TestCaseList tcl2) {					
							return tcl1.getTestCaseExecutionOrder().compareTo(tcl2.getTestCaseExecutionOrder());					
						}			
					});
				}
				StringBuffer sb = new StringBuffer();
				for(int i=0 ;i<testCases.size();i++){
					sb.append(testCases.get(i).getTestCaseName().replaceAll(" ", "_"));
					sb.append(".story");
					if((testCases.size()-1) != i)
						sb.append(",");
				}
				return sb.toString();
			} else {
				return "";
			}
		} else {
			return "";
		}
	}	

	@RequestMapping(value="client.test.external.report.save",produces="application/json")
	public @ResponseBody String saveExternalReportFiles(@RequestParam int testRunListId,@RequestParam int workPackageById,@ModelAttribute(value="FORM1") UploadForm form) {
		FileOutputStream outputStream = null;
		log.info("Test Job Id: "+testRunListId);
		log.info("WorkPackage Id: "+workPackageById);
		String path = htmlReportGenLocation + File.separator +"EXTERNAL_REPORTS"+ File.separator + workPackageById + File.separator;
		File folder = new File(path);
		if (!folder.isDirectory()) {
			folder.mkdirs();
		}
		log.info("File path for external report file : "+ path);
		Configuration.checkAndCreateDirectory(path);
		String filePath = path+File.separator+form.getFile().getOriginalFilename();
		System.out.println("Filepath value : "+ filePath);
		try {            	            	
			File file = new File(filePath);            	
			outputStream = new FileOutputStream(file);
			outputStream.write(form.getFile().getFileItem().get());
			outputStream.close();
		} catch (Exception e) {
			log.error("Error while saving external report file", e);               
			return "ERROR";
		}        
		return "OK";
	}

	@RequestMapping(value="client.job.testcase.execution.event.post",produces="application/json")
	public @ResponseBody String postTestcaseExecutionEvent(@RequestParam String testcaseName, @RequestParam Integer jobId, @RequestParam String eventName, @RequestParam String payload, @RequestParam Long eventTime, @RequestParam Integer expiryPolicy, @RequestParam Long expiryTime) {
		if (testcaseName == null || testcaseName.isEmpty())
			return "Error : Testcase Name is invalid";

		if (eventName == null || eventName.trim().isEmpty())
			return "Error : Event name is mandatory";
		TestRunJob testJob = null;
		if(jobId != -1)
			testJob = workPackageExecutionService.getTestRunJobById(jobId);
		
		WorkPackage workPackage = null;
		Integer workPackageId = -1;
		if(testJob != null)
			workPackage = testJob.getWorkPackage();
		if(workPackage  != null)
			workPackageId = workPackage.getWorkPackageId(); 

		TestcaseExecutionEvent testcaseExecutionEvent = new TestcaseExecutionEvent(testcaseName, jobId, workPackageId , eventName, payload, eventTime, expiryPolicy, expiryTime);
		try {
			workPackageExecutionService.postEvent(testcaseExecutionEvent);
		} catch (Exception e) {
			log.error("Could not post event", e);               
			return "Error : Could not post event " + e.getMessage();
		}
		return "OK";
	}

	@RequestMapping(value="client.job.testcase.execution.event.get",produces="application/json")
	public @ResponseBody JTableSingleResponse getTestcaseExecutionEvent(@RequestParam String eventName, @RequestParam String testcaseName, @RequestParam Integer jobId, @RequestParam Integer workPackageId) {

		JTableSingleResponse jTableSingleResponse;
		if (eventName == null || eventName.trim().isEmpty()) {
			jTableSingleResponse = new JTableSingleResponse("ERROR","Event name is missing");
			return jTableSingleResponse;
		}

		TestRunJob testJob = null;
		if(jobId != -1)
			testJob = workPackageExecutionService.getTestRunJobById(jobId);
		
		WorkPackage workPackage = null;		
		if(workPackageId == -1){
			if(testJob != null){
				workPackage = testJob.getWorkPackage();
				if(workPackage  != null)
					workPackageId = workPackage.getWorkPackageId();
			}
		}
		
		TestcaseExecutionEvent testcaseExecutionEvent;
		try {
			testcaseExecutionEvent = workPackageExecutionService.getTestcaseExecutiontEvent(eventName, testcaseName, jobId, workPackageId);
			if (testcaseExecutionEvent == null)
				jTableSingleResponse = new JTableSingleResponse("ERROR","Event with sepcified criteria could not be found");
			else {
				JsonTestcaseExecutionEvent jsonTestcaseExecutionEvent = new JsonTestcaseExecutionEvent(testcaseExecutionEvent);
				jTableSingleResponse = new JTableSingleResponse("OK", jsonTestcaseExecutionEvent);
			}
		} catch (Exception e) {
			log.error("Could not find event", e);               
			jTableSingleResponse = new JTableSingleResponse("ERROR","Some problem while getting the testcase execution event");
		}
		return jTableSingleResponse;
	}
	@RequestMapping(value="client.util.deregister",method=RequestMethod.POST ,produces="application/json")
	public @ResponseBody JTableSingleResponse unregisterClient(@ModelAttribute JsonHostList jsonHostList, BindingResult result) {
		JTableSingleResponse jTableSingleResponse;

		if(result.hasErrors()){
			jTableSingleResponse  = new JTableSingleResponse("ERROR","Invalid form!"); 
		}

		try {
			//convert JsonHostList to HostList for persist operation				
			HostList hostList = jsonHostList.getHostList();
			if( hostList.getCommonActiveStatusMaster() != null)
				hostList.getCommonActiveStatusMaster().setStatus("INACTIVE");
			List<HostList> tmplist = hostListService.listByHostName(hostList.getHostName());
			if(tmplist!=null && tmplist.size()!=0){
				hostList.setHostId(tmplist.get(0).getHostId());					
				hostListService.update(hostList);
			}
			HostHeartbeat hostHeartbeat = new HostHeartbeat(hostList.getHostId(), System.currentTimeMillis(),false, (short) -1);
			hostHeartBeatService.update(hostHeartbeat);
			log.info("host '"+hostList.getHostName()+"' disconnected");
			eventsService.raiseTerminalConnectedEvent(hostList, "Terminal disconnected to Server");
			JsonHostList tmpList = new JsonHostList(hostList);
			jsonHostList.setHostId(hostList.getHostId());
			jTableSingleResponse = new JTableSingleResponse("OK",tmpList);
		} catch (Exception e) {
			jTableSingleResponse = new JTableSingleResponse("ERROR","Error unregistering terminal client");
			log.error("JSON ERROR", e);
		}	        
		return jTableSingleResponse;
	}

	@RequestMapping(value="client.util.job.testcase.execution.status.update",method=RequestMethod.POST ,produces="application/json")
	public @ResponseBody JTableSingleResponse updateTestcaseExecutionCompletion(@ModelAttribute JsonWorkPackageTestCaseExecutionPlanForTerminal jsonWorkPackageTestCaseExecutionPlan, BindingResult result) {
		JTableSingleResponse jTableSingleResponse;
		if(result.hasErrors()){
			jTableSingleResponse  = new JTableSingleResponse("ERROR","Invalid form!"); 
		}

		try {
			//TODO : Get the testcase execution result from database and update its status to completed
			//Get the Job
			TestRunJob testRunJob=workPackageExecutionService.getTestRunJobById(jsonWorkPackageTestCaseExecutionPlan.getTestRunJobId());
			if (testRunJob == null) {
				jTableSingleResponse = new JTableSingleResponse("ERROR","Problem updating test case completion due to invalid Job Id : " + jsonWorkPackageTestCaseExecutionPlan.getTestRunJobId());
				return jTableSingleResponse;
			}
			//Get the Testcase based on name
			TestCaseList testCaseList = testCaseService.getTestCaseByName(jsonWorkPackageTestCaseExecutionPlan.getTestcaseName(), jsonWorkPackageTestCaseExecutionPlan.getProductId());
			if (testCaseList == null) {
				jTableSingleResponse = new JTableSingleResponse("ERROR","Problem updating test case completion as test case was not found. Testcase name : " + jsonWorkPackageTestCaseExecutionPlan.getTestcaseName() + " : Product ID " + jsonWorkPackageTestCaseExecutionPlan.getProductId());
				return jTableSingleResponse;
			}
			//Get the workpackage testcase execution plan for the test case in the job
			WorkPackageTestCaseExecutionPlan workPackageTestCaseExecutionPlan = workPackageService.workPackageTestCasesExecutionPlanByJobId(testRunJob, testCaseList);

			//TODO : Find out the result of the testcase, based on the test step execution results and and update the test case result accordingly
			//Is this needed ?
			Integer testcaseResult = 1;
			//If there are no test step execution results, then the test case is not run
			//If there is at least one failed test step execution result, then the test case has failed
			//If all the test steps have passed, then the test case is passed

			//Update the workpackage testcase execution plan
			workPackageTestCaseExecutionPlan.setExecutionStatus(IDPAConstants.TESTCASE_EXECUTION_STATUS_COMPLETED);
			//workPackageTestCaseExecutionPlan.setStatus(testcaseResult);
			workPackageService.updateWorkPackageTestCaseExecutionPlan(workPackageTestCaseExecutionPlan);

			jTableSingleResponse = new JTableSingleResponse("OK",jsonWorkPackageTestCaseExecutionPlan);

		} catch (Exception e) {
			jTableSingleResponse = new JTableSingleResponse("ERROR","Error updating test case completion!");
			log.error("JSON ERROR", e);
		}

		return jTableSingleResponse;
	}
	
	@RequestMapping(value="client.test.result.joblog.add",method=RequestMethod.POST ,produces="application/json")
	public @ResponseBody String addLiveJobLogNew(@RequestParam Integer testJobId, @ModelAttribute JsonFileData fileData,BindingResult result){
        if(!result.hasErrors()){                 
           
            String genfilePath = CommonUtility.getCatalinaPath()+ File.separator + "webapps" + request.getContextPath()+ File.separator + "Evidence"+File.separator+testJobId+File.separator+IDPAConstants.EVIDENCE_LOG+ File.separator + "Live-log-"+testJobId+".txt";
            try {           	
            	if(!new File(CommonUtility.getCatalinaPath()+ File.separator + "webapps" + request.getContextPath()+ File.separator + "Evidence"+File.separator+testJobId+File.separator+IDPAConstants.EVIDENCE_LOG+ File.separator).exists()){
            		new File(CommonUtility.getCatalinaPath()+ File.separator + "webapps" + request.getContextPath()+ File.separator + "Evidence"+File.separator+testJobId+File.separator+IDPAConstants.EVIDENCE_LOG+ File.separator).mkdirs();
            	}           	
            	
            	File genfile = new File(genfilePath);
            	genfile.setExecutable(true);
            	genfile.setReadable(true);
            	genfile.setWritable(true);
            	if(!genfile.exists()) {
            		genfile = new File(genfilePath);
            		genfile.setExecutable(true);
                	genfile.setReadable(true);
                	genfile.setWritable(true);
            	}       
            	
            	//log.info("Job File Contents : " + fileData.getFileData());       	   
        	
            	//FileOutputStream newfos = new FileOutputStream(file, true);
            	FileOutputStream fos = new FileOutputStream(genfile, true);
            	String filecontents = fileData.getFileData();
            	if(filecontents != null) {
	            	byte[] bytes = filecontents.getBytes();
	            	if(bytes != null){ 
	            		fos.write(bytes);	            		
	            	}
            	}
            	fos.close();
            	
                //FileUtils.writeStringToFile(file, fileData.getFileData(), true);
                //FileUtils.writeStringToFile(genfile, fileData.getFileData(), true);   
            } catch (Exception e) {
            	log.error("Error while saving file", e);               
                return "ERROR";
            }
            return "OK";
        }else{
            return "ERROR";
        }
    }
}