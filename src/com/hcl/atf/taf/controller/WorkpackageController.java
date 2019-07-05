package com.hcl.atf.taf.controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Response;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.zkoss.json.JSONValue;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;
import com.google.gson.Gson;
import com.hcl.atf.taf.constants.IDPAConstants;
import com.hcl.atf.taf.constants.TAFConstants;
import com.hcl.atf.taf.controller.utilities.CommonUtility;
import com.hcl.atf.taf.controller.utilities.DateUtility;
import com.hcl.atf.taf.controller.utilities.RestResponseUtility;
import com.hcl.atf.taf.dao.ProductBuildDAO;
import com.hcl.atf.taf.dao.TestCaseAutomationScriptDAO;
import com.hcl.atf.taf.dao.TestCaseListDAO;
import com.hcl.atf.taf.model.DefectApprovalStatusMaster;
import com.hcl.atf.taf.model.DefectIdentificationStageMaster;
import com.hcl.atf.taf.model.DefectManagementSystem;
import com.hcl.atf.taf.model.DefectSeverity;
import com.hcl.atf.taf.model.DefectTypeMaster;
import com.hcl.atf.taf.model.EntityConfigurationProperties;
import com.hcl.atf.taf.model.EntityMaster;
import com.hcl.atf.taf.model.Environment;
import com.hcl.atf.taf.model.EnvironmentCombination;
import com.hcl.atf.taf.model.Evidence;
import com.hcl.atf.taf.model.EvidenceType;
import com.hcl.atf.taf.model.ExecutionPriority;
import com.hcl.atf.taf.model.LifeCyclePhase;
import com.hcl.atf.taf.model.ProductBuild;
import com.hcl.atf.taf.model.ProductFeature;
import com.hcl.atf.taf.model.ProductLocale;
import com.hcl.atf.taf.model.ProductMaster;
import com.hcl.atf.taf.model.ProductUserRole;
import com.hcl.atf.taf.model.ProductVersionListMaster;
import com.hcl.atf.taf.model.RunConfiguration;
import com.hcl.atf.taf.model.Skill;
import com.hcl.atf.taf.model.TestCaseConfiguration;
import com.hcl.atf.taf.model.TestCaseExecutionResult;
import com.hcl.atf.taf.model.TestCaseList;
import com.hcl.atf.taf.model.TestCycle;
import com.hcl.atf.taf.model.TestExecutionResultBugList;
import com.hcl.atf.taf.model.TestFactory;
import com.hcl.atf.taf.model.TestFactoryResourceReservation;
import com.hcl.atf.taf.model.TestManagementSystem;
import com.hcl.atf.taf.model.TestRunJob;
import com.hcl.atf.taf.model.TestRunPlan;
import com.hcl.atf.taf.model.TestRunPlanGroup;
import com.hcl.atf.taf.model.TestRunPlangroupHasTestRunPlan;
import com.hcl.atf.taf.model.TestStepExecutionResult;
import com.hcl.atf.taf.model.TestSuiteList;
import com.hcl.atf.taf.model.UserList;
import com.hcl.atf.taf.model.UserRoleMaster;
import com.hcl.atf.taf.model.UserTypeMasterNew;
import com.hcl.atf.taf.model.WorkFlow;
import com.hcl.atf.taf.model.WorkFlowEvent;
import com.hcl.atf.taf.model.WorkPackage;
import com.hcl.atf.taf.model.WorkPackageDemandProjection;
import com.hcl.atf.taf.model.WorkPackageFeature;
import com.hcl.atf.taf.model.WorkPackageFeatureExecutionPlan;
import com.hcl.atf.taf.model.WorkPackageTestCase;
import com.hcl.atf.taf.model.WorkPackageTestCaseExecutionPlan;
import com.hcl.atf.taf.model.WorkPackageTestSuite;
import com.hcl.atf.taf.model.WorkPackageTestSuiteExecutionPlan;
import com.hcl.atf.taf.model.WorkShiftMaster;
import com.hcl.atf.taf.model.WorkpackageRunConfiguration;
import com.hcl.atf.taf.model.dto.ScriptLessExecutionDTO;
import com.hcl.atf.taf.model.dto.TestCaseDTO;
import com.hcl.atf.taf.model.dto.TestStepExecutionResultDTO;
import com.hcl.atf.taf.model.dto.VerificationResult;
import com.hcl.atf.taf.model.dto.WorkPackageBuildTestCaseSummaryDTO;
import com.hcl.atf.taf.model.dto.WorkPackageExecutionPlanUserDetails;
import com.hcl.atf.taf.model.dto.WorkPackageTCEPResultSummaryDTO;
import com.hcl.atf.taf.model.dto.WorkPackageTestCaseExecutionPlanStatusDTO;
import com.hcl.atf.taf.model.dto.WorkPackageTestCaseSummaryDTO;
import com.hcl.atf.taf.model.json.JsonEnvironment;
import com.hcl.atf.taf.model.json.JsonEnvironmentCombination;
import com.hcl.atf.taf.model.json.JsonEvidence;
import com.hcl.atf.taf.model.json.JsonProductLocale;
import com.hcl.atf.taf.model.json.JsonRunConfiguration;
import com.hcl.atf.taf.model.json.JsonTestCaseConfiguration;
import com.hcl.atf.taf.model.json.JsonTestCaseExecutionResult;
import com.hcl.atf.taf.model.json.JsonTestCaseList;
import com.hcl.atf.taf.model.json.JsonTestCycle;
import com.hcl.atf.taf.model.json.JsonTestExecutionResult;
import com.hcl.atf.taf.model.json.JsonTestExecutionResultBugList;
import com.hcl.atf.taf.model.json.JsonTestRunJob;
import com.hcl.atf.taf.model.json.JsonTestRunPlan;
import com.hcl.atf.taf.model.json.JsonTestStepExecutionResult;
import com.hcl.atf.taf.model.json.JsonUserList;
import com.hcl.atf.taf.model.json.JsonWorkPackage;
import com.hcl.atf.taf.model.json.JsonWorkPackageDailyPlan;
import com.hcl.atf.taf.model.json.JsonWorkPackageDemandProjection;
import com.hcl.atf.taf.model.json.JsonWorkPackageExecutionPlanUserDetails;
import com.hcl.atf.taf.model.json.JsonWorkPackageFeature;
import com.hcl.atf.taf.model.json.JsonWorkPackageFeatureExecutionPlan;
import com.hcl.atf.taf.model.json.JsonWorkPackageStatusSummary;
import com.hcl.atf.taf.model.json.JsonWorkPackageTestCase;
import com.hcl.atf.taf.model.json.JsonWorkPackageTestCaseExecutionPlan;
import com.hcl.atf.taf.model.json.JsonWorkPackageTestCaseExecutionPlanForTester;
import com.hcl.atf.taf.model.json.JsonWorkPackageTestCaseExecutionPlanStatus;
import com.hcl.atf.taf.model.json.JsonWorkPackageTestCaseExecutionSummary;
import com.hcl.atf.taf.model.json.JsonWorkPackageTestSuite;
import com.hcl.atf.taf.model.json.JsonWorkPackageTestSuiteExecutionPlan;
import com.hcl.atf.taf.model.json.JsonWorkShiftMaster;
import com.hcl.atf.taf.model.json.jtable.JTableResponse;
import com.hcl.atf.taf.model.json.jtable.JTableResponseOptions;
import com.hcl.atf.taf.model.json.jtable.JTableSingleResponse;
import com.hcl.atf.taf.mongodb.dao.TestCaseDefectsMongoDAO;
import com.hcl.atf.taf.mongodb.service.MongoDBService;
import com.hcl.atf.taf.report.Report;
import com.hcl.atf.taf.service.CommonService;
import com.hcl.atf.taf.service.DataTreeService;
import com.hcl.atf.taf.service.DefectManagementService;
import com.hcl.atf.taf.service.EntityConfigurationPropertiesService;
import com.hcl.atf.taf.service.EnvironmentService;
import com.hcl.atf.taf.service.EventsService;
import com.hcl.atf.taf.service.ExecutionTypeMasterService;
import com.hcl.atf.taf.service.ProductListService;
import com.hcl.atf.taf.service.ResourceManagementService;
import com.hcl.atf.taf.service.RiskListService;
import com.hcl.atf.taf.service.SkillService;
import com.hcl.atf.taf.service.TestCaseService;
import com.hcl.atf.taf.service.TestExecutionBugsService;
import com.hcl.atf.taf.service.TestFactoryManagementService;
import com.hcl.atf.taf.service.TestManagementService;
import com.hcl.atf.taf.service.TestSuiteConfigurationService;
import com.hcl.atf.taf.service.UserListService;
import com.hcl.atf.taf.service.WorkPackageService;
import com.hcl.ilcm.workflow.service.ConfigurationWorkFlowService;

@Controller
public class WorkpackageController {

	private static final Log log = LogFactory.getLog(WorkpackageController.class);
	
	@Autowired
	private WorkPackageService workPackageService;
	@Autowired
	private TestSuiteConfigurationService testSuiteConfigurationService;
	@Autowired
	private ResourceManagementService resourceManagementService;
	
	@Autowired
	private ProductListService productListService;
	@Autowired
	private TestCaseService testCaseService;
	@Autowired
	private UserListService	userListService;
	@Autowired
	private DataTreeService dataTreeService;
	@Autowired
	private TestFactoryManagementService testFactoryManagementService;
	@Autowired
	private SkillService skillService;
	@Autowired
	private EnvironmentService environmentService;
	@Autowired
	private TestExecutionBugsService testExecutionBugsService;
	@Autowired
	private TestManagementService testManagementService;
	@Autowired
	private DefectManagementService defectManagementService;
	@Autowired
	private ToolIntegrationController toolsController;
	@Autowired
	private ExecutionTypeMasterService executionTypeMasterService;
	@Autowired
	private TestCaseDefectsMongoDAO testCaseDefectsMongoDAO;	
	@Autowired
	private MongoDBService mongoDBService;	
	@Autowired
	private EventsService eventsService;
	@Autowired
	private RiskListService riskListService;
	@Autowired
	private CommonService commonService;
	@Value("#{ilcmProps['MONGODB_AVAILABLE']}")
    private String MONGODB_AVAILABLE;
	
	@Value("#{ilcmProps['EVIDENCE_FOLDER']}")
    private String evidence_Folder;
	
	@Autowired
	private EntityConfigurationPropertiesService entityConfigurationPropertiesService;
	
	@Autowired
	private ConfigurationWorkFlowService configurationWorkFlowService;
	
	@Autowired
	private TestCaseAutomationScriptDAO testCaseAutomationScriptDAO;
		
	@Autowired
	private Report report;
	
	@Autowired
	private TestCaseListDAO testCaseListDAO;
	
	@Autowired
	private ProductBuildDAO productBuildDao;
	
	@RequestMapping(value="workpackage.testcase.list",method=RequestMethod.POST ,produces="application/json")
	public @ResponseBody JTableResponse listWorkpackageTestCases(@RequestParam int workPackageId, @RequestParam int jtStartIndex, @RequestParam int jtPageSize) {
			log.debug("inside workpackage.testcase.list");

			JTableResponse jTableResponse = null;
			log.info("Loading workpackage testcases for Workpackage Id **: " + workPackageId);
			try {
				List<WorkPackageTestCase> wptcTotal=null;
				Integer wptcTotalCount = 0;
				Integer workPackageTestCasesTotalCount = 0;
				int pageSize=0;
				List<WorkPackageTestCase> workPackageTestCasesTotal= new ArrayList<WorkPackageTestCase>();
				workPackageTestCasesTotalCount= workPackageService.getWorkPackageTestCasesCount(workPackageId,-1,-1);
				List<WorkPackageTestCase> workPackageTestCases=new ArrayList<WorkPackageTestCase>();
				
				List<JsonWorkPackageTestCase> jsonWorkPackageTestCases=new ArrayList<JsonWorkPackageTestCase>();
				int productId = workPackageService.getProductIdByWorkpackageId(workPackageId);		
				Integer workPackageType = workPackageService.getexecutionTypeIdByWorkpackageId(workPackageId);
				int executionId=-1;
				if(workPackageType==7)
					executionId=1;
				List<TestCaseList> allTestCasesForProduct = new ArrayList<TestCaseList>();
				int productTestCasesCount=0;
				productTestCasesCount = workPackageService.getProductTestCaseCount(productId, executionId);
				JsonWorkPackageTestCase jsonWorkPackageTestCase = null;
				if (workPackageTestCasesTotalCount == null || workPackageTestCasesTotalCount==0) {
					log.info("There are no testcases for this WPTC. Initializing");
					int count=workPackageService.initializeWorkPackageWithTestCases(workPackageId);
					wptcTotalCount = workPackageService.getWorkPackageTestCasesCount(workPackageId,-1,-1);
					pageSize = wptcTotalCount; 
					jsonWorkPackageTestCases = workPackageService.listJsonWorkPackageTestCases(workPackageId,jtStartIndex,jtPageSize);
					jTableResponse = new JTableResponse("OK", jsonWorkPackageTestCases,pageSize);
					workPackageTestCases = null;
				}
				else if (workPackageTestCasesTotalCount < productTestCasesCount){
					//Seed the remaining test cases
					List<TestCaseList> newTestCases = new ArrayList<TestCaseList>();
					List<TestCaseList> testCasesListForWorkpackage = new ArrayList<TestCaseList>();
					workPackageTestCasesTotal=workPackageService.listWorkPackageTestCases(workPackageId);
					for(WorkPackageTestCase workPackageTestCase :workPackageTestCasesTotal){
						testCasesListForWorkpackage.add(workPackageTestCase.getTestCase());
					}
					allTestCasesForProduct = testCaseService.getTestCaseListByProductIdByType(productId,executionId);
					for(TestCaseList testcase: allTestCasesForProduct){
						if(!testCasesListForWorkpackage.contains(testcase)){
							newTestCases.add(testcase);
						}
					}
					
					int count=workPackageService.seedWorkPackageWithNewTestCases(newTestCases,workPackageId);
					wptcTotalCount = workPackageService.getWorkPackageTestCasesCount(workPackageId,-1,-1);
					pageSize = wptcTotalCount; 
			        
					jsonWorkPackageTestCases = workPackageService.listJsonWorkPackageTestCases(workPackageId,jtStartIndex,jtPageSize);
					jTableResponse = new JTableResponse("OK", jsonWorkPackageTestCases, pageSize);
					workPackageTestCases = null;

				} 
				else {
					log.info("workPackageTestCases count>>>>"+workPackageTestCasesTotalCount);
					wptcTotalCount = workPackageService.getWorkPackageTestCasesCount(workPackageId,-1,-1);
					pageSize = wptcTotalCount; 
					jsonWorkPackageTestCases = workPackageService.listJsonWorkPackageTestCases(workPackageId,jtStartIndex,jtPageSize);
					jTableResponse = new JTableResponse("OK", jsonWorkPackageTestCases, pageSize);
					workPackageTestCases = null;
				}
			} catch (Exception e) {
			    jTableResponse = new JTableResponse("ERROR","Error fetching records!");
			    log.error("JSON ERROR", e);	            
			}
	        return jTableResponse;
	    }
	
	@RequestMapping(value="workpackage.testsuite.list",method=RequestMethod.POST ,produces="application/json")
	public @ResponseBody JTableResponse listWorkpackageTestSuites(@RequestParam int workPackageId, @RequestParam int jtStartIndex, @RequestParam int jtPageSize) {
			log.debug("inside workpackage.testsuite.list");
			JTableResponse jTableResponse = null;
			try{
				
				jTableResponse=workPackageService.listWorkpackageTestSuite(workPackageId,jtStartIndex,jtPageSize);
			}catch (Exception e) {
			    jTableResponse = new JTableResponse("ERROR","Error fetching records!");
			    log.error("JSON ERROR", e);	            
			}
	        return jTableResponse;
	}

	@RequestMapping(value="workpackage.feature.list",method=RequestMethod.POST ,produces="application/json")
	public @ResponseBody JTableResponse listWorkpackageFeature(@RequestParam int workPackageId, @RequestParam int jtStartIndex, @RequestParam int jtPageSize) {
			log.debug("inside workpackage.feature.list");
			JTableResponse jTableResponse = null;
			try{
				
				jTableResponse=workPackageService.listWorkpackageFeature(workPackageId,jtStartIndex,jtPageSize);
			}catch (Exception e) {
			    jTableResponse = new JTableResponse("ERROR","Error fetching records!");
			    log.error("JSON ERROR", e);	            
			}
	        return jTableResponse;
	}
	private JsonWorkPackageTestCase setEnvironmentSelectionsInJsonWorkPackageTestCase(JsonWorkPackageTestCase jsonWorkPackageTestCase, WorkPackageTestCase workPackageTestCase ) {
		
		//Get the list of environments for the product
		List<Environment> allEnvironments = productListService.getEnvironmentListByProductId(workPackageTestCase.getWorkPackage().getProductBuild().getProductVersion().getProductMaster().getProductId());
	
		//extracted from the workpackagetestcaseplan
		List<Environment> selectedEnvironments = workPackageService.getWorkPackageTestCasesExecutionPlanEnvironments(workPackageTestCase.getWorkPackage().getWorkPackageId(), workPackageTestCase.getTestCase().getTestCaseId());
	

		if (allEnvironments.size() == selectedEnvironments.size()) {
			
			jsonWorkPackageTestCase.setEnv1("1");
			jsonWorkPackageTestCase.setEnv2("1");
			jsonWorkPackageTestCase.setEnv3("1");
			jsonWorkPackageTestCase.setEnv4("1");
			jsonWorkPackageTestCase.setEnv5("1");
			jsonWorkPackageTestCase.setEnv6("1");
			jsonWorkPackageTestCase.setEnv7("1");
			jsonWorkPackageTestCase.setEnv8("1");
			jsonWorkPackageTestCase.setEnv9("1");
			jsonWorkPackageTestCase.setEnv10("1");
			
			jsonWorkPackageTestCase.setEnv11("1");
			jsonWorkPackageTestCase.setEnv12("1");
			jsonWorkPackageTestCase.setEnv13("1");
			jsonWorkPackageTestCase.setEnv14("1");
			jsonWorkPackageTestCase.setEnv15("1");
			jsonWorkPackageTestCase.setEnv16("1");
			jsonWorkPackageTestCase.setEnv17("1");
			jsonWorkPackageTestCase.setEnv18("1");
			jsonWorkPackageTestCase.setEnv19("1");
			jsonWorkPackageTestCase.setEnv20("1");
			
		} else {
		
			jsonWorkPackageTestCase.setAllEnvironments("0");
			int countOfAllEnvironments = allEnvironments.size();
			log.info("Total Environments : " + countOfAllEnvironments);
			log.info("Selected Environments : " + selectedEnvironments.size());
			//int countOfSelectedEnvironments = selectedEnvironments.size();
			if (1 <= countOfAllEnvironments) {
				if (selectedEnvironments.contains(allEnvironments.get(0))) {
					jsonWorkPackageTestCase.setEnv1("1");
				} else {
					jsonWorkPackageTestCase.setEnv1("0");
				}
			}
			if (2 <= countOfAllEnvironments) {
				if (selectedEnvironments.contains(allEnvironments.get(1))) {
					jsonWorkPackageTestCase.setEnv2("1");
				} else {
					jsonWorkPackageTestCase.setEnv2("0");
				}
			}
			if (3 <= countOfAllEnvironments) {
				if (selectedEnvironments.contains(allEnvironments.get(2))) {
					jsonWorkPackageTestCase.setEnv3("1");
				} else {
					jsonWorkPackageTestCase.setEnv3("0");
				}
			}
			if (4 <= countOfAllEnvironments) {
				if (selectedEnvironments.contains(allEnvironments.get(3))) {
					jsonWorkPackageTestCase.setEnv4("1");
				} else {
					jsonWorkPackageTestCase.setEnv4("0");
				}
			}
			if (5 <= countOfAllEnvironments) {
				if (selectedEnvironments.contains(allEnvironments.get(4))) {
					jsonWorkPackageTestCase.setEnv5("1");
				} else {
					jsonWorkPackageTestCase.setEnv5("0");
				}
			}
			if (6 <= countOfAllEnvironments) {
				if (selectedEnvironments.contains(allEnvironments.get(5))) {
					jsonWorkPackageTestCase.setEnv6("1");
				} else {
					jsonWorkPackageTestCase.setEnv6("0");
				}
			}
			if (7 <= countOfAllEnvironments) {
				if (selectedEnvironments.contains(allEnvironments.get(6))) {
					jsonWorkPackageTestCase.setEnv7("1");
				} else {
					jsonWorkPackageTestCase.setEnv7("0");
				}
			}
			if (8 <= countOfAllEnvironments) {
				if (selectedEnvironments.contains(allEnvironments.get(7))) {
					jsonWorkPackageTestCase.setEnv8("1");
				} else {
					jsonWorkPackageTestCase.setEnv8("0");
				}
			}
			if (9 <= countOfAllEnvironments) {
				if (selectedEnvironments.contains(allEnvironments.get(8))) {
					jsonWorkPackageTestCase.setEnv9("1");
				} else {
					jsonWorkPackageTestCase.setEnv9("0");
				}
			}
			if (10 <= countOfAllEnvironments) {
				if (selectedEnvironments.contains(allEnvironments.get(9))) {
					jsonWorkPackageTestCase.setEnv10("1");
				} else {
					jsonWorkPackageTestCase.setEnv10("0");
				}
			}
			if (11 <= countOfAllEnvironments) {
				if (selectedEnvironments.contains(allEnvironments.get(10))) {
					jsonWorkPackageTestCase.setEnv11("1");
				} else {
					jsonWorkPackageTestCase.setEnv11("0");
				}
			}
			if (12 <= countOfAllEnvironments) {
				if (selectedEnvironments.contains(allEnvironments.get(11))) {
					jsonWorkPackageTestCase.setEnv12("1");
				} else {
					jsonWorkPackageTestCase.setEnv12("0");
				}
			}
			if (13 <= countOfAllEnvironments) {
				if (selectedEnvironments.contains(allEnvironments.get(12))) {
					jsonWorkPackageTestCase.setEnv13("1");
				} else {
					jsonWorkPackageTestCase.setEnv13("0");
				}
			}
			if (14 <= countOfAllEnvironments) {
				if (selectedEnvironments.contains(allEnvironments.get(13))) {
					jsonWorkPackageTestCase.setEnv14("1");
				} else {
					jsonWorkPackageTestCase.setEnv14("0");
				}
			}
			if (15 <= countOfAllEnvironments) {
				if (selectedEnvironments.contains(allEnvironments.get(14))) {
					jsonWorkPackageTestCase.setEnv15("1");
				} else {
					jsonWorkPackageTestCase.setEnv15("0");
				}
			}
			if (16 <= countOfAllEnvironments) {
				if (selectedEnvironments.contains(allEnvironments.get(15))) {
					jsonWorkPackageTestCase.setEnv16("1");
				} else {
					jsonWorkPackageTestCase.setEnv16("0");
				}
			}
			if (17 <= countOfAllEnvironments) {
				if (selectedEnvironments.contains(allEnvironments.get(16))) {
					jsonWorkPackageTestCase.setEnv17("1");
				} else {
					jsonWorkPackageTestCase.setEnv17("0");
				}
			}
			if (18 <= countOfAllEnvironments) {
				if (selectedEnvironments.contains(allEnvironments.get(17))) {
					jsonWorkPackageTestCase.setEnv18("1");
				} else {
					jsonWorkPackageTestCase.setEnv18("0");
				}
			}
			if (19 <= countOfAllEnvironments) {
				if (selectedEnvironments.contains(allEnvironments.get(18))) {
					jsonWorkPackageTestCase.setEnv19("1");
				} else {
					jsonWorkPackageTestCase.setEnv19("0");
				}
			}
			if (20 <= countOfAllEnvironments) {
				if (selectedEnvironments.contains(allEnvironments.get(19))) {
					jsonWorkPackageTestCase.setEnv20("1");
				} else {
					jsonWorkPackageTestCase.setEnv20("0");
				}
			}
		}
		return jsonWorkPackageTestCase;
	}

	
	@RequestMapping(value="workpackage.testcase.update",method=RequestMethod.POST ,produces="application/json")
	public @ResponseBody JTableResponse updateWorkPackageTestCase(@ModelAttribute JsonWorkPackageTestCase jsonWorkPackageTestCase, BindingResult result) {
		
		JTableResponse jTableResponse = null;
		if(result.hasErrors()){
			jTableResponse = new JTableResponse("ERROR","Invalid Form!"); 
		}
		
		try {			

			WorkPackageTestCase workPackageTestCaseFromUI = jsonWorkPackageTestCase.getWorkPackageTestCase();
			workPackageTestCaseFromUI.setTestCase(testSuiteConfigurationService.getByTestCaseId(jsonWorkPackageTestCase.getTestcaseId()));
			workPackageTestCaseFromUI.setWorkPackage(workPackageService.getWorkPackageById(jsonWorkPackageTestCase.getWorkPackageId()));
			
			WorkPackageTestCase updatedWorkPackageTestCase = workPackageService.updateWorkPackageTestCase(workPackageTestCaseFromUI, jsonWorkPackageTestCase);

			List<JsonWorkPackageTestCase> testCases = new ArrayList<JsonWorkPackageTestCase>();

			JsonWorkPackageTestCase updatedJsonWorkPackageTestCase = new JsonWorkPackageTestCase(updatedWorkPackageTestCase);
			testCases.add(updatedJsonWorkPackageTestCase);
			jTableResponse = new JTableResponse("OK",testCases,1);
	    } catch (Exception e) {
	        jTableResponse = new JTableResponse("ERROR","Unable to update the testcase selection!");
	        log.error("JSON ERROR", e);
	    }	        
        return jTableResponse;
    }
		
	@RequestMapping(value="administration.workpackage.list",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableResponse listWorkpackages(@RequestParam int productBuildId) {
		log.debug("inside workpackage.list");
		JTableResponse jTableResponse = null;
			try {		        
				List<WorkPackage> workPackages = workPackageService.listWorkPackages(productBuildId);
				log.info("workPackageService.size====="+workPackages.size());
				List<JsonWorkPackage> jsonWorkPackage = new ArrayList<JsonWorkPackage>();
				
				for(WorkPackage pm: workPackages){
					jsonWorkPackage.add(new JsonWorkPackage(pm));
				}				
	            jTableResponse = new JTableResponse("OK", jsonWorkPackage,jsonWorkPackage.size());     
		        } catch (Exception e) {
		            jTableResponse = new JTableResponse("ERROR","Error fetching records!");
		            log.error("JSON ERROR", e);
		        }
	        return jTableResponse;
    }

	@RequestMapping(value="administration.workpackage.add",method=RequestMethod.POST ,produces="application/json")
	public @ResponseBody Response addWorkpackages(HttpServletRequest req,@RequestParam Map<String, String>  mapData,
			@RequestParam String copyDataType) 
	{			
		JSONObject jsonResponse = new JSONObject();
		JSONObject workPackageResponse = new JSONObject();
		JTableSingleResponse jTableSingleResponse =null;
		Integer productKey = null;
		Integer productBuildKey = Integer.parseInt(mapData.get("productBuildId"));
		ProductBuild productBuild=productListService.getProductBuildById(Integer.parseInt(mapData.get("productBuildId")), 0);
		if(productBuild != null){
			productKey = productBuild.getProductVersion().getProductMaster().getProductId();
		}
		String errorMessage = commonService.duplicateName(mapData.get("workpackageName"), "WorkPackage", "name", "WorkPackage","productBuild="+productBuild.getProductBuildId());
		
		UserList user=(UserList)req.getSession().getAttribute("USER");
		if (errorMessage != null) {
			jTableSingleResponse = new JTableSingleResponse("ERROR",errorMessage);
			jsonResponse.put("statusCode", -1);
			jsonResponse.put("status", 400);
			jsonResponse.put("message", errorMessage);
			//return -1;
			return Response.ok(jsonResponse.toString()).build();
		}else{
			Date productBuildDate=productBuild.getBuildDate();
			Date startDate=DateUtility.dateformatWithOutTime(mapData.get("plannedStartDate"));
			if(productBuildDate.compareTo(startDate)>0){
				jTableSingleResponse = new JTableSingleResponse("ERROR","WorkPackage Create Date should be Greaterthan ProductBuild date!");
				int validationId=0;
				//return validationId;
				errorMessage = "WorkPackage Create Date should be Greaterthan ProductBuild date!";
				jsonResponse.put("statusCode", validationId);
				jsonResponse.put("status", 400);
				jsonResponse.put("message", errorMessage);
				return Response.ok(jsonResponse.toString()).build();
			}
			WorkPackage workPackage= workPackageService.cloneWorkpackage(mapData,user,copyDataType,MONGODB_AVAILABLE);
					
			UserList userObj = userListService.getUserListById(user.getUserId());
			EntityMaster entityMaster = workPackageService.getEntityMasterByName(IDPAConstants.ENTITY_PRODUCT);
			//Entity Audition History //Addition
			eventsService.addNewEntityEvent(IDPAConstants.ENTITY_WORK_PACKAGE, workPackage.getWorkPackageId(), workPackage.getName(), userObj);
					
			req.getSession().setAttribute("workpackageId", workPackage.getWorkPackageId());
			req.getSession().setAttribute("type", "create");
			req.getSession().setAttribute("productKey", productKey);
			req.getSession().setAttribute("productBuildKey", productBuildKey);
			String productName = "", productVersionName ="", productBuildName ="";
			if(productBuild.getBuildname() != null){
				productBuildName = productBuild.getBuildname();
				if(productBuild.getProductVersion() != null){
					productVersionName = productBuild.getProductVersion().getProductVersionName();
					if(productBuild.getProductVersion().getProductMaster() != null){
						productName = productBuild.getProductVersion().getProductMaster().getProductName();
					}
				}
			}			
			
			String wpMessage = "";
			wpMessage = workPackage.getName() + " manual execution initiated with WorkPackageId["+workPackage.getWorkPackageId()+"] of "
			+ productName + "-"+productVersionName + "-"+productBuildName;
			jsonResponse.put("wpMessage", wpMessage);
			jsonResponse.put("statusCode", workPackage.getWorkPackageId());
			jsonResponse.put("status", "200");			
			//return workPackage.getWorkPackageId();
			return Response.ok(jsonResponse.toString()).build();
		}
	}
		


		/*@RequestMapping(value="administration.workpackage.add",method=RequestMethod.POST ,produces="application/json")
	    public @ResponseBody Integer addWorkpackages1(HttpServletRequest req,@RequestParam Map<String, String>  mapData,@RequestParam String copyDataType) {			
			JTableSingleResponse jTableSingleResponse =null;
			Integer productKey = null;
			Integer productBuildKey = Integer.parseInt(mapData.get("productBuildId"));
			ProductBuild productBuild=productListService.getProductBuildById(Integer.parseInt(mapData.get("productBuildId")), 0);
			if(productBuild != null){
				productKey = productBuild.getProductVersion().getProductMaster().getProductId();
				log.info("productKey ***** "+productKey);
			}
			String errorMessage = commonService.duplicateName(mapData.get("workpackageName"), "WorkPackage", "name", "WorkPackage","productBuild="+productBuild.getProductBuildId());
			
			
			UserList user=(UserList)req.getSession().getAttribute("USER");

			if (errorMessage != null) {
				jTableSingleResponse = new JTableSingleResponse("ERROR",errorMessage);
				return -1;
			}
			Date productBuildDate=productBuild.getBuildDate();
		Date startDate=DateUtility.dateformatWithOutTime(mapData.get("plannedStartDate"));
		log.info("productBuildDate------>"+productBuildDate);
		log.info("startDate------>"+startDate);
			if(productBuildDate.compareTo(startDate)>0){
				jTableSingleResponse = new JTableSingleResponse("ERROR","WorkPackage Create Date should be Greaterthan ProductBuild date!");
				 int validationId=0;
				 return validationId;
			}
				WorkPackage workPackage= workPackageService.cloneWorkpackage(mapData,user,copyDataType,MONGODB_AVAILABLE);
				
				UserList userObj = userListService.getUserListById(user.getUserId());
				EntityMaster entityMaster = workPackageService.getEntityMasterByName(IDPAConstants.ENTITY_PRODUCT);
				//Entity Audition History //Addition
				eventsService.addNewEntityEvent(IDPAConstants.ENTITY_WORK_PACKAGE, workPackage.getWorkPackageId(), workPackage.getName(), userObj);
				
				req.getSession().setAttribute("workpackageId", workPackage.getWorkPackageId());
				req.getSession().setAttribute("type", "create");
				req.getSession().setAttribute("productKey", productKey);
				req.getSession().setAttribute("productBuildKey", productBuildKey);
			return workPackage.getWorkPackageId();
	    }*/
		
		@RequestMapping(value="administration.workpackage.success")
		public String adminWorkPackageTestCasePlan(ModelMap model) throws Exception {
			String userDisplayName="HCL";
			log.debug("inside navigation Controller and adminhost method");
			try{
				String userName=SecurityContextHolder.getContext()
			     .getAuthentication().getName();
				NavigationController navigationController= new NavigationController();
				navigationController.setUserNameAndRole(userName);
				userDisplayName = navigationController.getUserNameAndRole();
				
			}catch(Exception e){}
			model.addAttribute("user",userDisplayName);
			return "workPackageTestCasePlan";
			//return "host_new";
		}
		
		@RequestMapping(value="administration.workpackage.update",method=RequestMethod.POST ,produces="application/json")
		public @ResponseBody JTableResponse updateWorkpackage(@RequestParam Integer workpackageId,@RequestParam String modifiedfField,@RequestParam String modifiedValue) {
			JTableResponse jTableResponse;
				
			try {			
					WorkPackage tmpworkPackage =workPackageService.getWorkPackageById(workpackageId);
					JsonWorkPackage jsonWorkPackage = new JsonWorkPackage();
					if(modifiedfField.equals("wpkg_name")){
						tmpworkPackage.setName(modifiedValue);
					}else if(modifiedfField.equals("wpkg_desc")){
						tmpworkPackage.setDescription(modifiedValue);
					}
					workPackageService.updateWorkPackage(tmpworkPackage);
					if(tmpworkPackage!=null && tmpworkPackage.getWorkPackageId()!=null){
						mongoDBService.addWorkPackage(tmpworkPackage.getWorkPackageId());
					}
					
					List<JsonWorkPackage> tmpList = new ArrayList();
					tmpList.add(new JsonWorkPackage(tmpworkPackage));
					jTableResponse = new JTableResponse("OK",tmpList ,1);					
					
		        } catch (Exception e) {
		            jTableResponse = new JTableResponse("ERROR","Error updating workPackage!");
		            log.error("JSON ERROR", e);
		            e.printStackTrace();
		        }       
	        return jTableResponse;
	    }
		
		
		@RequestMapping(value="administration.workpackage.delete",method=RequestMethod.POST ,produces="application/json")
	    public @ResponseBody JTableResponse deleteWorkpackages(@RequestParam int id) {			
			JTableResponse jTableResponse;
			try {
					workPackageService.deleteWorkPackage(id);
		            
		            jTableResponse = new JTableResponse("OK");
		        } catch (Exception e) {
		            jTableResponse = new JTableResponse("ERROR","Error deleting WorkPackage!");
		            e.printStackTrace();
		            log.error("JSON ERROR", e);
		        }
	        return jTableResponse;
	    }
		
		@RequestMapping(value="administration.workpackage.get",method=RequestMethod.POST ,produces="application/json")
	    public @ResponseBody JTableSingleResponse getWorkpackage(@RequestParam int id) {
			JTableSingleResponse jTSingleResponse;
			log.info("Getting Workpackage :"+id);
			try {						
				WorkPackage workPackage = workPackageService.getWorkPackageById(id);				
				jTSingleResponse = new JTableSingleResponse("OK",new JsonWorkPackage(workPackage));	
				
		        } catch (Exception e) {
		        	jTSingleResponse = new JTableSingleResponse("ERROR");
		            log.error("JSON ERROR", e);	            
		        }
		        
	        return jTSingleResponse;
	    }	
		
		@RequestMapping(value="administration.workpackage.details",method=RequestMethod.POST ,produces="application/json")
	    public @ResponseBody JTableResponse getWorkpackageById(@RequestParam int wpId) {
			log.debug("inside administration.workpackage.details");		
			int jtStartIndex=0;
			int jtPageSize=10;
			JTableResponse jTableResponse = null;
				try {
					WorkPackage workPackage = workPackageService.getWorkPackageById(wpId);	
					List<JsonWorkPackage> jsonWorkPackage = new ArrayList<JsonWorkPackage>();				
				
					
					if (workPackage == null ) {
						
						//TODO : Initialize the workpackage with the test cases for the product
						jTableResponse = new JTableResponse("OK", jsonWorkPackage, 0);
					} else {
						jsonWorkPackage.add(new JsonWorkPackage(workPackage));
						
						jTableResponse = new JTableResponse("OK", jsonWorkPackage, jsonWorkPackage.size());
						workPackage = null;
					}
		        } catch (Exception e) {
		            jTableResponse = new JTableResponse("ERROR","Error fetching records!");
		            log.error("JSON ERROR", e);	            
		        }
	        return jTableResponse;
	    }	
		
	
		@RequestMapping(value="workpackage.testcase.plan",method=RequestMethod.POST ,produces="application/json")
	    public @ResponseBody JTableResponse listWorkpackageTestCaseExecutionPlan(@RequestParam int workPackageId,@RequestParam String testLeadId,@RequestParam String timeStamp,@RequestParam int jtStartIndex, @RequestParam int jtPageSize,HttpServletRequest req,@RequestParam String plannedExecutionDate,@RequestParam String envId,@RequestParam int localeId,@RequestParam String testerId,@RequestParam String dcId,@RequestParam String executionPriority,@RequestParam int status) {
			List<JsonWorkPackageTestCaseExecutionPlan> jsonWorkPackageTestCaseExecutionPlan=new ArrayList<JsonWorkPackageTestCaseExecutionPlan>();
			JTableResponse jTableResponse = null;
			UserList user=(UserList)req.getSession().getAttribute("USER");
			Map<String, String> searchStrings = new HashMap<String, String>();

			 	String searchTestCaseId = req.getParameter("searchTestCaseId");
	            String searchTestCaseName=req.getParameter("searchTestCaseName");
	            String searchTestCaseCode=req.getParameter("searchTestCaseCode");
	            String searchTestSuiteName=req.getParameter("searchTestSuiteName");
	            String searchDescription=req.getParameter("searchDescription");
	            String searchDCName=req.getParameter("searchDCName");
	            String searchFeatureName = req.getParameter("searchFeatureName");
	            String searchsourceType = req.getParameter("searchsourceType");
	            String searchECName=req.getParameter("searchECName");
	            String searchDeviceName=req.getParameter("searchDeviceName");
	            String searchTestLeadName=req.getParameter("searchTestLeadName");
	            String searchTesterName=req.getParameter("searchTesterName");
	            String searchPED=req.getParameter("searchPED");
	            String searchPlannedShift=req.getParameter("searchPlannedShift");
	            String searchExecutionStatus=req.getParameter("searchExecutionStatus");
	            searchStrings.put("searchTestCaseId", searchTestCaseId);
	            searchStrings.put("searchTestCaseName", searchTestCaseName);
	            searchStrings.put("searchTestCaseCode", searchTestCaseCode);
	            searchStrings.put("searchTestSuiteName", searchTestSuiteName);
	            searchStrings.put("searchDescription", searchDescription);
	            searchStrings.put("searchDCName", searchDCName);
	            searchStrings.put("searchFeatureName", searchFeatureName);
	            searchStrings.put("searchsourceType", searchsourceType);
	            searchStrings.put("searchECName", searchECName);
	            searchStrings.put("searchDeviceName", searchDeviceName);
	            searchStrings.put("searchTestLeadName", searchTestLeadName);
	            searchStrings.put("searchTesterName", searchTesterName);
	            searchStrings.put("searchPED", searchPED);
	            searchStrings.put("searchPlannedShift", searchPlannedShift);
	            searchStrings.put("searchExecutionStatus", searchExecutionStatus);
	            
				try {
					if(workPackageId<=0){
						jTableResponse = new JTableResponse("OK",jsonWorkPackageTestCaseExecutionPlan,0);
					}					
					jsonWorkPackageTestCaseExecutionPlan = workPackageService.listJsonWorkPackageTestCaseExecutionPlan(searchStrings,workPackageId, jtStartIndex,jtPageSize,testLeadId,testerId,envId,localeId,plannedExecutionDate,dcId,executionPriority,status);
					if (jsonWorkPackageTestCaseExecutionPlan == null || jsonWorkPackageTestCaseExecutionPlan.isEmpty()) {						
						//TODO : Initialize the workpackage with the test cases for the product
						log.info("No workpackage testcase execution plan for workpackage id:"+workPackageId  );
						jTableResponse = new JTableResponse("OK", jsonWorkPackageTestCaseExecutionPlan, 0);
					} else {
						int totalCount=workPackageService.getTotalRecordWPTCEPCount(workPackageId,status);
						jTableResponse = new JTableResponse("OK", jsonWorkPackageTestCaseExecutionPlan, totalCount);
						jsonWorkPackageTestCaseExecutionPlan = null;
					}
		        } catch (Exception e) {
		            jTableResponse = new JTableResponse("ERROR","Error fetching records!");
		            log.error("JSON ERROR", e);	   
		            e.printStackTrace();
		        }
	        return jTableResponse;
	    }
		
		@RequestMapping(value="workpackage.featureplan.list",method=RequestMethod.POST ,produces="application/json")
	    public @ResponseBody JTableResponse listWorkpackageFeatureExecutionPlan(@RequestParam int workpackageId,@RequestParam int jtStartIndex, @RequestParam int jtPageSize,HttpServletRequest req,@RequestParam int status) {
			List<JsonWorkPackageFeatureExecutionPlan> jsonWorkPackageFeatureExecutionPlan=new ArrayList<JsonWorkPackageFeatureExecutionPlan>();
			JTableResponse jTableResponse = null;
			Map<String, String> searchStrings = new HashMap<String, String>();
			
			String searchFeatureName = req.getParameter("searchFeatureName");
            String searchFeatureDescription=req.getParameter("searchFeatureDescription");
            String searchECName=req.getParameter("searchECName");
            String searchDeviceName=req.getParameter("searchDeviceName");
            String searchPED=req.getParameter("searchPED");
            String searchPlannedShift = req.getParameter("searchPlannedShift");
            String searchTestLeadName=req.getParameter("searchTestLeadName");
            String searchTesterName=req.getParameter("searchTesterName");
            
            searchStrings.put("searchFeatureName", searchFeatureName);
            searchStrings.put("searchFeatureDescription", searchFeatureDescription);
            searchStrings.put("searchECName", searchECName);
            searchStrings.put("searchDeviceName", searchDeviceName);
            searchStrings.put("searchPED", searchPED);
            searchStrings.put("searchPlannedShift", searchPlannedShift);
            searchStrings.put("searchTestLeadName", searchTestLeadName);
            searchStrings.put("searchTesterName", searchTesterName);
	            
				try {
					if(workpackageId<=0){
						jTableResponse = new JTableResponse("OK",jsonWorkPackageFeatureExecutionPlan,0);
					}					
					jsonWorkPackageFeatureExecutionPlan=workPackageService.listJsonWorkPackageFeatureExecutionPlan(searchStrings,workpackageId, jtStartIndex,jtPageSize,status);				

					if (jsonWorkPackageFeatureExecutionPlan == null || jsonWorkPackageFeatureExecutionPlan.isEmpty()) {						
						log.info("No workpackage testcase execution plan for workpackage id:"+workpackageId  );
						jTableResponse = new JTableResponse("OK", jsonWorkPackageFeatureExecutionPlan, 0);
					} else {
						int totalCount=workPackageService.getTotalRecordWPFEPCount(workpackageId,status);						
						jTableResponse = new JTableResponse("OK", jsonWorkPackageFeatureExecutionPlan, totalCount);
						jsonWorkPackageFeatureExecutionPlan = null;
					}
		        } catch (Exception e) {
		            jTableResponse = new JTableResponse("ERROR","Error fetching records!");
		            log.error("JSON ERROR", e);	   
		            e.printStackTrace();
		        }
	        return jTableResponse;
	    }

		
		@RequestMapping(value="workpackage.testcase.planbyenv",method=RequestMethod.POST ,produces="application/json")
	    public @ResponseBody JTableResponse listWorkpackageTestCaseExecutionPlanByEnv(@RequestParam int workPackageId,@RequestParam int envId, @RequestParam int jtStartIndex, @RequestParam int jtPageSize,HttpServletRequest req) {
			log.debug("inside workpackage.testcase.planbyenv");
			JTableResponse jTableResponse = null;
			UserList user=(UserList)req.getSession().getAttribute("USER");
				try {
					List<JsonWorkPackageTestCaseExecutionPlan> jsonWorkPackageTestCaseExecutionPlan=new ArrayList<JsonWorkPackageTestCaseExecutionPlan>();
					if(workPackageId<=0){
						jTableResponse = new JTableResponse("OK",jsonWorkPackageTestCaseExecutionPlan,0);
					}
					
					List<WorkPackageTestCaseExecutionPlan> workPackageTestCaseExecutionPlanList=workPackageService.listWorkPackageTestCasesExecutionPlanByEnv(workPackageId, jtStartIndex,jtPageSize,envId);
					
					
					
					if (workPackageTestCaseExecutionPlanList == null || workPackageTestCaseExecutionPlanList.isEmpty()) {
						log.info("No workpackage testcase execution plan for workpackage id:"+workPackageId  );
						jTableResponse = new JTableResponse("OK", jsonWorkPackageTestCaseExecutionPlan, 0);
					} else {
						for(WorkPackageTestCaseExecutionPlan workPackageTestCaseExecutionPlan: workPackageTestCaseExecutionPlanList){
							jsonWorkPackageTestCaseExecutionPlan.add(new JsonWorkPackageTestCaseExecutionPlan(workPackageTestCaseExecutionPlan));
						}
				        
						jTableResponse = new JTableResponse("OK", jsonWorkPackageTestCaseExecutionPlan, workPackageService.totalRecordsCountForWorkPackageTestCasesExecutionPlan(workPackageId,user));
						workPackageTestCaseExecutionPlanList = null;
					}
		        } catch (Exception e) {
		            jTableResponse = new JTableResponse("ERROR","Error fetching records!");
		            log.error("JSON ERROR", e);	            
		        }
	        return jTableResponse;
	    }
		
		@RequestMapping(value="workpackage.testcase.testleadplanbyenv",method=RequestMethod.POST ,produces="application/json")
	    public @ResponseBody JTableResponse listWorkpackageTestCaseExecutionPlanTestLeadByEnv(@RequestParam int workPackageId, @RequestParam int envId,@RequestParam int jtStartIndex, @RequestParam int jtPageSize,HttpServletRequest req) {
			log.debug("inside workpackage.testcase.testleadplanbyenv");
			JTableResponse jTableResponse = null;
			UserList user=(UserList)req.getSession().getAttribute("USER");
				try {
					if(workPackageId<=0){
						workPackageId=1;
					}
					
					List<WorkPackageTestCaseExecutionPlan> workPackageTestCaseExecutionPlanList=workPackageService.listWorkPackageTestCasesExecutionPlanTestLeadByEnv(workPackageId, jtStartIndex,jtPageSize,user,envId);
					
					List<JsonWorkPackageTestCaseExecutionPlan> jsonWorkPackageTestCaseExecutionPlan=new ArrayList<JsonWorkPackageTestCaseExecutionPlan>();
					
					if (workPackageTestCaseExecutionPlanList == null || workPackageTestCaseExecutionPlanList.isEmpty()) {
						
						log.info("No workpackage testcase execution plan for workpackage id:"+workPackageId  );
						jTableResponse = new JTableResponse("OK", jsonWorkPackageTestCaseExecutionPlan, 0);
					} else {
						for(WorkPackageTestCaseExecutionPlan workPackageTestCaseExecutionPlan: workPackageTestCaseExecutionPlanList){
							jsonWorkPackageTestCaseExecutionPlan.add(new JsonWorkPackageTestCaseExecutionPlan(workPackageTestCaseExecutionPlan));
						}
				        
						jTableResponse = new JTableResponse("OK", jsonWorkPackageTestCaseExecutionPlan, workPackageService.totalRecordsCountForWorkPackageTestCasesExecutionPlan(workPackageId,user));
						workPackageTestCaseExecutionPlanList = null;
					}
		        } catch (Exception e) {
		            jTableResponse = new JTableResponse("ERROR","Error fetching records!");
		            log.error("JSON ERROR", e);	            
		        }
	        return jTableResponse;
	    }
		
		@RequestMapping(value="workpackage.testcase.testleadplan",method=RequestMethod.POST ,produces="application/json")
	    public @ResponseBody JTableResponse listWorkpackageTestCaseExecutionPlanTestLead(@RequestParam int workPackageId, @RequestParam int jtStartIndex, @RequestParam int jtPageSize,HttpServletRequest req) {
			log.debug("inside workpackage.testcase.testleadplan");
			JTableResponse jTableResponse = null;
			UserList user=(UserList)req.getSession().getAttribute("USER");
				try {
					if(workPackageId<=0){
						workPackageId=1;
					}
					
					List<WorkPackageTestCaseExecutionPlan> workPackageTestCaseExecutionPlanList=workPackageService.listWorkPackageTestCasesExecutionPlanTestLead(workPackageId, jtStartIndex,jtPageSize,user);
					
					List<JsonWorkPackageTestCaseExecutionPlanForTester> jsonWorkPackageTestCaseExecutionPlan=new ArrayList<JsonWorkPackageTestCaseExecutionPlanForTester>();
					
					if (workPackageTestCaseExecutionPlanList == null || workPackageTestCaseExecutionPlanList.isEmpty()) {
						log.info("No workpackage testcase execution plan for workpackage id:"+workPackageId  );
						jTableResponse = new JTableResponse("OK", jsonWorkPackageTestCaseExecutionPlan, 0);
					} else {
						for(WorkPackageTestCaseExecutionPlan workPackageTestCaseExecutionPlan: workPackageTestCaseExecutionPlanList){
							jsonWorkPackageTestCaseExecutionPlan.add(new JsonWorkPackageTestCaseExecutionPlanForTester(workPackageTestCaseExecutionPlan));
						}
				        
						jTableResponse = new JTableResponse("OK", jsonWorkPackageTestCaseExecutionPlan, workPackageService.totalRecordsCountForWorkPackageTestCasesExecutionPlan(workPackageId,user));
						workPackageTestCaseExecutionPlanList = null;
					}
		        } catch (Exception e) {
		            jTableResponse = new JTableResponse("ERROR","Error fetching records!");
		            log.error("JSON ERROR", e);	            
		        }
	        return jTableResponse;
	    }
		
		@RequestMapping(value="workpackage.testcase.plan.tester",method=RequestMethod.POST ,produces="application/json")
	    public @ResponseBody JTableResponse listWorkpackageTestCaseExecutionPlansForTester(@RequestParam int workPackageId, @RequestParam int jtStartIndex, @RequestParam int jtPageSize,HttpServletRequest req) {
			log.debug("inside workpackage.testcase.plan.tester");
			UserList user=(UserList)req.getSession().getAttribute("USER");
			JTableResponse jTableResponse = null;
				try {
					if(workPackageId<=0){
						workPackageId=1;
					}
					
					List<WorkPackageTestCaseExecutionPlan> workPackageTestCaseExecutionPlanList=workPackageService.listWorkPackageTestCasesExecutionPlan(workPackageId, jtStartIndex,jtPageSize,user,"");
					
					List<JsonWorkPackageTestCaseExecutionPlanForTester> jsonWorkPackageTestCaseExecutionPlans=new ArrayList<JsonWorkPackageTestCaseExecutionPlanForTester>();
					
					if (workPackageTestCaseExecutionPlanList == null || workPackageTestCaseExecutionPlanList.isEmpty()) {
						
						log.info("No workpackage testcase execution plan for workpackage id:"+workPackageId  );
						jTableResponse = new JTableResponse("OK", jsonWorkPackageTestCaseExecutionPlans, 0);
					} else {
						for(WorkPackageTestCaseExecutionPlan workPackageTestCaseExecutionPlan: workPackageTestCaseExecutionPlanList){
							jsonWorkPackageTestCaseExecutionPlans.add(new JsonWorkPackageTestCaseExecutionPlanForTester(workPackageTestCaseExecutionPlan));
						}
				        
						jTableResponse = new JTableResponse("OK", jsonWorkPackageTestCaseExecutionPlans, jsonWorkPackageTestCaseExecutionPlans.size());
						workPackageTestCaseExecutionPlanList = null;
					}
		        } catch (Exception e) {
		            jTableResponse = new JTableResponse("ERROR","Error fetching records!");
		            log.error("JSON ERROR", e);	            
		        }
	        return jTableResponse;
	    }	

		@RequestMapping(value="workpackage.testcase.plan.testerByEnv",method=RequestMethod.POST ,produces="application/json")
	    public @ResponseBody JTableResponse listWorkpackageTestCaseExecutionPlansForTesterByEnv(@RequestParam int workPackageId, @RequestParam int jtStartIndex, @RequestParam int jtPageSize,HttpServletRequest req,@RequestParam int envId) {
			log.debug("inside workpackage.testcase.plan.testerByEnv");
			UserList user=(UserList)req.getSession().getAttribute("USER");

			JTableResponse jTableResponse = null;
				try {
					if(workPackageId<=0){
						workPackageId=1;
					}
					
					List<WorkPackageTestCaseExecutionPlan> workPackageTestCaseExecutionPlanList=workPackageService.listWorkPackageTestCasesExecutionPlanTesterByEnv(workPackageId, jtStartIndex,jtPageSize,user,envId);
					
					List<JsonWorkPackageTestCaseExecutionPlanForTester> jsonWorkPackageTestCaseExecutionPlans=new ArrayList<JsonWorkPackageTestCaseExecutionPlanForTester>();
					
					if (workPackageTestCaseExecutionPlanList == null || workPackageTestCaseExecutionPlanList.isEmpty()) {
						
						//TODO : Initialize the workpackage with the test cases for the product
						log.info("No workpackage testcase execution plan for workpackage id:"+workPackageId  );
						jTableResponse = new JTableResponse("OK", jsonWorkPackageTestCaseExecutionPlans, 0);
					} else {
						for(WorkPackageTestCaseExecutionPlan workPackageTestCaseExecutionPlan: workPackageTestCaseExecutionPlanList){
							jsonWorkPackageTestCaseExecutionPlans.add(new JsonWorkPackageTestCaseExecutionPlanForTester(workPackageTestCaseExecutionPlan));
						}
				        
						jTableResponse = new JTableResponse("OK", jsonWorkPackageTestCaseExecutionPlans,jsonWorkPackageTestCaseExecutionPlans.size());
						workPackageTestCaseExecutionPlanList = null;
					}
		        } catch (Exception e) {
		            jTableResponse = new JTableResponse("ERROR","Error fetching records!");
		            log.error("JSON ERROR", e);	            
		        }
	        return jTableResponse;
	    }	
		@RequestMapping(value="administration.user.testerList",method=RequestMethod.POST ,produces="application/json")
	    public @ResponseBody JTableResponseOptions listTester(@RequestParam int workPackageId,@RequestParam String plannedExecutionDate,@RequestParam int shiftId) {
			log.info("inside administration.user.testerList"+workPackageId);
			JTableResponseOptions jTableResponseOptions;
				try {
					
					List<TestFactoryResourceReservation> testFactoryResourceReservationList = workPackageService.getUserByBlockedStatus(workPackageId, IDPAConstants.ROLE_ID_TESTER,plannedExecutionDate,shiftId);
					
					List<UserList> testerList = new ArrayList<UserList>();
					
					for(TestFactoryResourceReservation testFactoryResourceReservation:testFactoryResourceReservationList){
						testerList.add(testFactoryResourceReservation.getBlockedUser());
					}
					
					List<TestFactoryResourceReservation> testFactoryResourceReservationTLList = workPackageService.getUserByBlockedStatus(workPackageId, IDPAConstants.ROLE_ID_TEST_LEAD,plannedExecutionDate,shiftId);
					for(TestFactoryResourceReservation testFactoryResourceReservation:testFactoryResourceReservationTLList){
						testerList.add(testFactoryResourceReservation.getBlockedUser());
					}
					List<JsonUserList> jsonUserList = new ArrayList<JsonUserList>();
					
					for(UserList tester: testerList){
						jsonUserList.add(new JsonUserList(tester));
					}
					jTableResponseOptions = new JTableResponseOptions("OK", jsonUserList);
		            
			        } catch (Exception e) {
			        	jTableResponseOptions = new JTableResponseOptions("ERROR","Error fetching records!");
			            log.error("JSON ERROR", e);
			        }
			        
		        return jTableResponseOptions;
	    }
		
		@RequestMapping(value="administration.user.testerListExcludingTestLead",method=RequestMethod.POST ,produces="application/json")
	    public @ResponseBody JTableResponseOptions listTesterExcludingTestLead(@RequestParam int workPackageId,@RequestParam String plannedExecutionDate,@RequestParam int shiftId) {
			log.info("inside administration.user.testerListExcludingTestLead--Starts------>"+DateUtility.getCurrentTime());
			JTableResponseOptions jTableResponseOptions;
				try {					
					jTableResponseOptions = workPackageService.getUserByBlockedStatusForWPAssociation(workPackageId, IDPAConstants.ROLE_ID_TESTER,plannedExecutionDate,shiftId);
					    
			        } catch (Exception e) {
			        	jTableResponseOptions = new JTableResponseOptions("ERROR","Error fetching records!");
			            log.error("JSON ERROR", e);
			        }
				log.info("inside administration.user.testerListExcludingTestLead--ends------>"+DateUtility.getCurrentTime());     
				log.info("");
		        return jTableResponseOptions;
	    }
		
		@RequestMapping(value="administration.user.testLeadList",method=RequestMethod.POST ,produces="application/json")
	    public @ResponseBody JTableResponseOptions listTestLead(@RequestParam int workPackageId,@RequestParam String plannedExecutionDate,@RequestParam int shiftId) {
			log.info("inside administration.user.testLeadList");
			JTableResponseOptions jTableResponseOptions = null;
			try {
				
				List<TestFactoryResourceReservation> testFactoryResourceReservationList = workPackageService.getUserByBlockedStatus(workPackageId, IDPAConstants.ROLE_ID_TEST_LEAD,plannedExecutionDate,shiftId);
				
				List<TestFactoryResourceReservation> testFactoryResourceReservationTMList = workPackageService.getUserByBlockedStatus(workPackageId, IDPAConstants.ROLE_ID_TEST_MANAGER,plannedExecutionDate,shiftId);
				List<UserList> testLeadList = new ArrayList<UserList>();
			//	
				for(TestFactoryResourceReservation testFactoryResourceReservation:testFactoryResourceReservationList){
					testLeadList.add(testFactoryResourceReservation.getBlockedUser());
				}
				for(TestFactoryResourceReservation testFactoryResourceReservation:testFactoryResourceReservationTMList){
					testLeadList.add(testFactoryResourceReservation.getBlockedUser());
				}
				List<JsonUserList> jsonUserList = new ArrayList<JsonUserList>();
				
				for(UserList testlead: testLeadList){
					jsonUserList.add(new JsonUserList(testlead));
				}
				jTableResponseOptions = new JTableResponseOptions("OK", jsonUserList);
	            
		        } catch (Exception e) {
		        	jTableResponseOptions = new JTableResponseOptions("ERROR","Error fetching records!");
		            log.error("JSON ERROR", e);
		        }	
			        
		        return jTableResponseOptions;
	    }
		
		@RequestMapping(value="administration.user.testLeadListExcludingTester",method=RequestMethod.POST ,produces="application/json")
	    public @ResponseBody JTableResponseOptions listTestLeadExcludingTester(@RequestParam int workPackageId,@RequestParam String plannedExecutionDate,@RequestParam int shiftId) {
			log.info("inside administration.user.testLeadListExcludingTester--Starts--->"+DateUtility.getCurrentTime());
			JTableResponseOptions jTableResponseOptions = null;
			try {				
				jTableResponseOptions = workPackageService.getUserByBlockedStatusForWPAssociation(workPackageId, IDPAConstants.ROLE_ID_TEST_LEAD,plannedExecutionDate,shiftId);				
		        } catch (Exception e) {
		        	jTableResponseOptions = new JTableResponseOptions("ERROR","Error fetching records!");
		            log.error("JSON ERROR", e);
		        }	
			log.info("inside administration.user.testLeadListExcludingTester--Ends--->"+DateUtility.getCurrentTime());
			log.info("");
		        return jTableResponseOptions;
	    }
		
		@RequestMapping(value="workpackage.testcaseplan.update",method=RequestMethod.POST ,produces="application/json")
		public @ResponseBody JTableResponse updateWorkPackageTestCaseExecutionPlan(@ModelAttribute JsonWorkPackageTestCaseExecutionPlan jsonWorkPackageTestCaseExecutionPlan, BindingResult result) {
			
			JTableResponse jTableResponse = null;
			boolean isTestCaseExecutionStarted = false;
			if(result.hasErrors()){
				jTableResponse = new JTableResponse("ERROR","Invalid Form!"); 
			}
			
			try {
				WorkPackageTestCaseExecutionPlan workPackageTestCaseExecutionPlanFromUI = jsonWorkPackageTestCaseExecutionPlan.getWorkPackageTestCaseExecutionPlan();
				/** Testcase cannot be reallocated. Once the test case has been executed.
				 Fix for Bug Id:  1976. 
				 Fixed by : Logeswari on 09-Jan-2016**/
				/** Start**/
				if(workPackageTestCaseExecutionPlanFromUI.getIsExecuted() == 1){
					isTestCaseExecutionStarted = true;
				}
				if(workPackageTestCaseExecutionPlanFromUI.getIsExecuted() == 0 && workPackageTestCaseExecutionPlanFromUI.getExecutionStatus()==2 ){
					jTableResponse = new JTableResponse("INFORMATION","Test Case is not executed please execute the testcase for complete status !"); 
					return jTableResponse;
				}
				if(isTestCaseExecutionStarted){
					jTableResponse = new JTableResponse("INFORMATION","Testcase cannot be reallocated. Once the test case has been executed.!"); 
					/** End **/
				}else{	
					
					WorkPackage workPackage=workPackageService.getWorkPackageById(workPackageTestCaseExecutionPlanFromUI.getWorkPackage().getWorkPackageId());
					workPackageTestCaseExecutionPlanFromUI.setTestCase(testSuiteConfigurationService.getByTestCaseId(workPackageTestCaseExecutionPlanFromUI.getTestCase().getTestCaseId()));
					workPackageTestCaseExecutionPlanFromUI.setWorkPackage(workPackage);
					
					
					workPackageTestCaseExecutionPlanFromUI.setRunConfiguration(workPackageService.getWorkpackageRunConfigurationByWPTCEP(workPackageTestCaseExecutionPlanFromUI.getRunConfiguration().getWorkpackageRunConfigurationId()));
					
					if(jsonWorkPackageTestCaseExecutionPlan.getExecutionPriorityId() != null){
						workPackageTestCaseExecutionPlanFromUI.setExecutionPriority(workPackageService.getExecutionPriorityById(jsonWorkPackageTestCaseExecutionPlan.getExecutionPriorityId()));	
					}
					
					if(workPackageTestCaseExecutionPlanFromUI.getTester()!=null && workPackageTestCaseExecutionPlanFromUI.getTester().getUserId()!=null && !workPackageTestCaseExecutionPlanFromUI.getTester().getUserId().equals("0")){
						workPackageTestCaseExecutionPlanFromUI.setTester(userListService.getUserListById(workPackageTestCaseExecutionPlanFromUI.getTester().getUserId()));
					}
					if(workPackageTestCaseExecutionPlanFromUI.getTester()!=null && workPackageTestCaseExecutionPlanFromUI.getTester().getFirstName()!=null && !workPackageTestCaseExecutionPlanFromUI.getTester().getFirstName().equals("")){
						workPackageTestCaseExecutionPlanFromUI.setTester(userListService.getUserListByUserName(workPackageTestCaseExecutionPlanFromUI.getTester().getFirstName()));
					}
					
					if(workPackageTestCaseExecutionPlanFromUI.getTestLead()!=null && workPackageTestCaseExecutionPlanFromUI.getTestLead().getUserId()!=null && !workPackageTestCaseExecutionPlanFromUI.getTestLead().getUserId().equals("0")){
						workPackageTestCaseExecutionPlanFromUI.setTestLead(userListService.getUserListById(workPackageTestCaseExecutionPlanFromUI.getTestLead().getUserId()));
					}
					if(workPackageTestCaseExecutionPlanFromUI.getTestLead()!=null && workPackageTestCaseExecutionPlanFromUI.getTestLead().getFirstName()!=null && !workPackageTestCaseExecutionPlanFromUI.getTestLead().getFirstName().equals("")){
						workPackageTestCaseExecutionPlanFromUI.setTestLead(userListService.getUserListByUserName(workPackageTestCaseExecutionPlanFromUI.getTestLead().getFirstName()));
					}
					if(workPackageTestCaseExecutionPlanFromUI.getPlannedWorkShiftMaster()!=null && workPackageTestCaseExecutionPlanFromUI.getPlannedWorkShiftMaster().getShiftId()!=null && !workPackageTestCaseExecutionPlanFromUI.getPlannedWorkShiftMaster().getShiftId().equals("0")){
						workPackageTestCaseExecutionPlanFromUI.setPlannedWorkShiftMaster(workPackageService.getWorkShiftById(workPackageTestCaseExecutionPlanFromUI.getPlannedWorkShiftMaster().getShiftId()));
					}
	
					if(workPackageTestCaseExecutionPlanFromUI.getActualWorkShiftMaster()!=null && workPackageTestCaseExecutionPlanFromUI.getActualWorkShiftMaster().getShiftId()!=null && !workPackageTestCaseExecutionPlanFromUI.getActualWorkShiftMaster().getShiftId().equals("0")){
						workPackageTestCaseExecutionPlanFromUI.setActualWorkShiftMaster(workPackageService.getWorkShiftById(workPackageTestCaseExecutionPlanFromUI.getActualWorkShiftMaster().getShiftId()));
					}
	
					if(workPackageTestCaseExecutionPlanFromUI.getPlannedWorkShiftMaster()!=null && workPackageTestCaseExecutionPlanFromUI.getPlannedWorkShiftMaster().getShiftName()!=null && !workPackageTestCaseExecutionPlanFromUI.getPlannedWorkShiftMaster().getShiftName().equals("")){
						workPackageTestCaseExecutionPlanFromUI.setPlannedWorkShiftMaster(workPackageService.getWorkShiftByName(workPackageTestCaseExecutionPlanFromUI.getPlannedWorkShiftMaster().getShiftName()));
					}
	
					if(workPackageTestCaseExecutionPlanFromUI.getActualWorkShiftMaster()!=null && workPackageTestCaseExecutionPlanFromUI.getActualWorkShiftMaster().getShiftName()!=null && !workPackageTestCaseExecutionPlanFromUI.getActualWorkShiftMaster().getShiftName().equals("")){
						workPackageTestCaseExecutionPlanFromUI.setActualWorkShiftMaster(workPackageService.getWorkShiftByName(workPackageTestCaseExecutionPlanFromUI.getActualWorkShiftMaster().getShiftName()));
					}
					
					
					if(workPackageTestCaseExecutionPlanFromUI.getTester()!=null && workPackageTestCaseExecutionPlanFromUI.getTestLead()!=null){
						workPackageTestCaseExecutionPlanFromUI.setExecutionStatus(1);
					}
					
					
					if(workPackageTestCaseExecutionPlanFromUI.getPlannedExecutionDate()==null){
						workPackageTestCaseExecutionPlanFromUI.setPlannedExecutionDate(workPackage.getPlannedStartDate());
					}
					log.info("sourcetype>>>"+workPackageTestCaseExecutionPlanFromUI.getSourceType());
					if(workPackageTestCaseExecutionPlanFromUI.getSourceType().startsWith("Feature")){
						workPackageTestCaseExecutionPlanFromUI.setSourceType("Feature");
					}else if(workPackageTestCaseExecutionPlanFromUI.getSourceType().startsWith("TestSuite")){
						workPackageTestCaseExecutionPlanFromUI.setSourceType("TestSuite");
					}
					else{
						workPackageTestCaseExecutionPlanFromUI.setSourceType(workPackageTestCaseExecutionPlanFromUI.getSourceType());
					}
					if(workPackageTestCaseExecutionPlanFromUI.getFeature()!=null && workPackageTestCaseExecutionPlanFromUI.getFeature().getProductFeatureId()!=null){
						
						workPackageTestCaseExecutionPlanFromUI.setFeature(productListService.getByProductFeatureId(workPackageTestCaseExecutionPlanFromUI.getFeature().getProductFeatureId()));
					}
					if(workPackageTestCaseExecutionPlanFromUI.getTestSuiteList()!=null && workPackageTestCaseExecutionPlanFromUI.getTestSuiteList().getTestSuiteId()!=null){
						workPackageTestCaseExecutionPlanFromUI.setTestSuiteList(testSuiteConfigurationService.getByTestSuiteId(workPackageTestCaseExecutionPlanFromUI.getTestSuiteList().getTestSuiteId()));
					}
					
					if(workPackageTestCaseExecutionPlanFromUI.getTestRunJob()!=null && workPackageTestCaseExecutionPlanFromUI.getTestRunJob().getTestRunJobId()!=null)
						workPackageTestCaseExecutionPlanFromUI.setTestRunJob(workPackageService.getTestRunJobById(workPackageTestCaseExecutionPlanFromUI.getTestRunJob().getTestRunJobId()));
					workPackageService.updateWorkPackageTestCaseExecutionPlan(workPackageTestCaseExecutionPlanFromUI);
					
					if(workPackageTestCaseExecutionPlanFromUI.getWorkPackage().getWorkFlowEvent().getWorkFlow().getStageId()!=IDPAConstants.WORKFLOW_STAGE_ID_PLANNING && workPackageTestCaseExecutionPlanFromUI.getWorkPackage().getWorkFlowEvent().getWorkFlow().getStageValue()<20){
						WorkFlow workFlow=workPackageService.getWorkFlowByEntityIdStageId(IDPAConstants.WORKPACKAGE_ENTITY_ID, IDPAConstants.WORKFLOW_STAGE_ID_PLANNING);
						WorkFlowEvent workFlowEvent = new WorkFlowEvent();
						workFlowEvent.setEventDate(DateUtility.getCurrentTime());
						workFlowEvent.setRemarks("Planning Workapckage :"+workPackageTestCaseExecutionPlanFromUI.getWorkPackage().getName());
						UserList user= userListService.getUserByLoginId(IDPAConstants.ADMIN_LOGIN_ID);
						workFlowEvent.setUser(user);
						workFlowEvent.setWorkFlow(workFlow);
						workPackage.setWorkFlowEvent(workFlowEvent);
						workPackageService.addWorkFlowEvent(workFlowEvent);
						workPackageService.updateWorkPackage(workPackage);
					}
					List<JsonWorkPackageTestCaseExecutionPlan> plannedTestCases = new ArrayList<JsonWorkPackageTestCaseExecutionPlan>();
					JsonWorkPackageTestCaseExecutionPlan jsonWpExecPlan = new JsonWorkPackageTestCaseExecutionPlan(workPackageTestCaseExecutionPlanFromUI);
					plannedTestCases.add(jsonWpExecPlan);
					
					jTableResponse = new JTableResponse("OK",plannedTestCases,1);
				}
		    } catch (Exception e) {
		        jTableResponse = new JTableResponse("ERROR","Unable to plan the testcase executionselection!");
		        log.error("JSON ERROR", e);
		    }
		        
		        
	        return jTableResponse;
	    }

		@RequestMapping(value="workpackage.featureplan.update",method=RequestMethod.POST ,produces="application/json")
		public @ResponseBody JTableResponse updateWorkPackageFeatureExecutionPlan(@ModelAttribute JsonWorkPackageFeatureExecutionPlan jsonWorkPackageFeatureExecutionPlan, BindingResult result) {
			
			JTableResponse jTableResponse = null;
			if(result.hasErrors()){
				jTableResponse = new JTableResponse("ERROR","Invalid Form!"); 
			}
			try {
				
				WorkPackageFeatureExecutionPlan workPackageFeatureExecutionPlanFromUI = jsonWorkPackageFeatureExecutionPlan.getWorkPackageFeatureExecutionPlan();
				
				/** Feature cannot be reallocated. Once the test case execution has been initiated.
				 Fix for Bug Id:  1976. 
				 Fixed by : Logeswari on 09-Jan-2016**/
				/** Start**/
				boolean isTestCaseExecutionStarted = false;
				List<WorkPackageTestCaseExecutionPlan> currentTestCaseExecutionPlanListOfSelectedFeature = null;
				if(workPackageFeatureExecutionPlanFromUI.getFeature() != null && workPackageFeatureExecutionPlanFromUI.getRunConfiguration().getRunconfiguration() != null){
					currentTestCaseExecutionPlanListOfSelectedFeature = workPackageService.getWorkPackageTestcaseExecutionPlanByWorkPackageId(workPackageFeatureExecutionPlanFromUI.getWorkPackage().getWorkPackageId(), -1, -1, workPackageFeatureExecutionPlanFromUI.getFeature().getProductFeatureId(), "Feature",workPackageFeatureExecutionPlanFromUI.getRunConfiguration().getRunconfiguration().getRunconfigId());
					if(currentTestCaseExecutionPlanListOfSelectedFeature != null && currentTestCaseExecutionPlanListOfSelectedFeature.size()>0){
						for(WorkPackageTestCaseExecutionPlan wptcep:currentTestCaseExecutionPlanListOfSelectedFeature){
							if(wptcep.getIsExecuted() == 1){
								isTestCaseExecutionStarted = true;
								break;
							}
							else{
								continue;
							}

						}
					}
				}
			
				
				if(isTestCaseExecutionStarted){
					jTableResponse = new JTableResponse("INFORMATION","Feature cannot be reallocated, as execution has been initiated!"); 
					/** End **/
				}else{	
						WorkPackage workPackage=workPackageService.getWorkPackageById(workPackageFeatureExecutionPlanFromUI.getWorkPackage().getWorkPackageId());
						
						workPackageFeatureExecutionPlanFromUI.setFeature(productListService.getByProductFeatureId(workPackageFeatureExecutionPlanFromUI.getFeature().getProductFeatureId()));
						workPackageFeatureExecutionPlanFromUI.setWorkPackage(workPackage);
						workPackageFeatureExecutionPlanFromUI.setRunConfiguration(workPackageService.getWorkpackageRunConfigurationByWPTCEP(workPackageFeatureExecutionPlanFromUI.getRunConfiguration().getWorkpackageRunConfigurationId()));
						
						if(workPackageFeatureExecutionPlanFromUI.getTester()!=null && workPackageFeatureExecutionPlanFromUI.getTester().getUserId()!=null && !workPackageFeatureExecutionPlanFromUI.getTester().getUserId().equals("0")){
							workPackageFeatureExecutionPlanFromUI.setTester(userListService.getUserListById(workPackageFeatureExecutionPlanFromUI.getTester().getUserId()));
						}
						if(workPackageFeatureExecutionPlanFromUI.getTester()!=null && workPackageFeatureExecutionPlanFromUI.getTester().getFirstName()!=null && !workPackageFeatureExecutionPlanFromUI.getTester().getFirstName().equals("")){
							workPackageFeatureExecutionPlanFromUI.setTester(userListService.getUserListByUserName(workPackageFeatureExecutionPlanFromUI.getTester().getFirstName()));
						}
						
						if(workPackageFeatureExecutionPlanFromUI.getTestLead()!=null && workPackageFeatureExecutionPlanFromUI.getTestLead().getUserId()!=null && !workPackageFeatureExecutionPlanFromUI.getTestLead().getUserId().equals("0")){
							workPackageFeatureExecutionPlanFromUI.setTestLead(userListService.getUserListById(workPackageFeatureExecutionPlanFromUI.getTestLead().getUserId()));
						}
						if(workPackageFeatureExecutionPlanFromUI.getTestLead()!=null && workPackageFeatureExecutionPlanFromUI.getTestLead().getFirstName()!=null && !workPackageFeatureExecutionPlanFromUI.getTestLead().getFirstName().equals("")){
							workPackageFeatureExecutionPlanFromUI.setTestLead(userListService.getUserListByUserName(workPackageFeatureExecutionPlanFromUI.getTestLead().getFirstName()));
						}
						if(workPackageFeatureExecutionPlanFromUI.getPlannedWorkShiftMaster()!=null && workPackageFeatureExecutionPlanFromUI.getPlannedWorkShiftMaster().getShiftId()!=null && !workPackageFeatureExecutionPlanFromUI.getPlannedWorkShiftMaster().getShiftId().equals("0")){
							workPackageFeatureExecutionPlanFromUI.setPlannedWorkShiftMaster(workPackageService.getWorkShiftById(workPackageFeatureExecutionPlanFromUI.getPlannedWorkShiftMaster().getShiftId()));
						}
		
						if(workPackageFeatureExecutionPlanFromUI.getActualWorkShiftMaster()!=null && workPackageFeatureExecutionPlanFromUI.getActualWorkShiftMaster().getShiftId()!=null && !workPackageFeatureExecutionPlanFromUI.getActualWorkShiftMaster().getShiftId().equals("0")){
							workPackageFeatureExecutionPlanFromUI.setActualWorkShiftMaster(workPackageService.getWorkShiftById(workPackageFeatureExecutionPlanFromUI.getActualWorkShiftMaster().getShiftId()));
						}
		
						if(workPackageFeatureExecutionPlanFromUI.getPlannedWorkShiftMaster()!=null && workPackageFeatureExecutionPlanFromUI.getPlannedWorkShiftMaster().getShiftName()!=null && !workPackageFeatureExecutionPlanFromUI.getPlannedWorkShiftMaster().getShiftName().equals("")){
							workPackageFeatureExecutionPlanFromUI.setPlannedWorkShiftMaster(workPackageService.getWorkShiftByName(workPackageFeatureExecutionPlanFromUI.getPlannedWorkShiftMaster().getShiftName()));
						}
		
						if(workPackageFeatureExecutionPlanFromUI.getActualWorkShiftMaster()!=null && workPackageFeatureExecutionPlanFromUI.getActualWorkShiftMaster().getShiftName()!=null && !workPackageFeatureExecutionPlanFromUI.getActualWorkShiftMaster().getShiftName().equals("")){
							workPackageFeatureExecutionPlanFromUI.setActualWorkShiftMaster(workPackageService.getWorkShiftByName(workPackageFeatureExecutionPlanFromUI.getActualWorkShiftMaster().getShiftName()));
						}
						if(workPackageFeatureExecutionPlanFromUI.getTestRunJob()!=null && workPackageFeatureExecutionPlanFromUI.getTestRunJob().getTestRunJobId()!=null)
							workPackageFeatureExecutionPlanFromUI.setTestRunJob(workPackageService.getTestRunJobById(workPackageFeatureExecutionPlanFromUI.getTestRunJob().getTestRunJobId()));
						
						if(workPackageFeatureExecutionPlanFromUI.getExecutionPriority() != null && workPackageFeatureExecutionPlanFromUI.getExecutionPriority().getExecutionPriorityId() != null){
							workPackageFeatureExecutionPlanFromUI.setExecutionPriority(workPackageService.getExecutionPriorityById(workPackageFeatureExecutionPlanFromUI.getExecutionPriority().getExecutionPriorityId()));
						}
						
						workPackageService.updateWorkPackageFeatureExecutionPlan(workPackageFeatureExecutionPlanFromUI);
						
						List<WorkPackageTestCaseExecutionPlan> currentWorkPackageTestCaseExecutionPlanList = null;
						currentWorkPackageTestCaseExecutionPlanList=workPackageService.getWorkPackageTestcaseExecutionPlanByWorkPackageId(workPackageFeatureExecutionPlanFromUI.getWorkPackage().getWorkPackageId(), -1, -1, workPackageFeatureExecutionPlanFromUI.getFeature().getProductFeatureId(), "Feature",workPackageFeatureExecutionPlanFromUI.getRunConfiguration().getRunconfiguration().getRunconfigId());
						for(WorkPackageTestCaseExecutionPlan wptcep:currentWorkPackageTestCaseExecutionPlanList){
							if(wptcep.getPlannedExecutionDate()==null){
								wptcep.setPlannedExecutionDate(DateUtility.dateFormatWithOutTimeStamp(DateUtility.sdfDateformatWithOutTime(workPackage.getPlannedStartDate())));
							}
							if(workPackageFeatureExecutionPlanFromUI.getPlannedWorkShiftMaster()!=null && workPackageFeatureExecutionPlanFromUI.getPlannedWorkShiftMaster().getShiftName()!=null)
								wptcep.setPlannedWorkShiftMaster(workPackageService.getWorkShiftByName(workPackageFeatureExecutionPlanFromUI.getPlannedWorkShiftMaster().getShiftName()));
							if(workPackageFeatureExecutionPlanFromUI.getTestLead()!=null)
								wptcep.setTestLead(workPackageFeatureExecutionPlanFromUI.getTestLead());
							if(workPackageFeatureExecutionPlanFromUI.getTester()!=null )
								wptcep.setTester(workPackageFeatureExecutionPlanFromUI.getTester());
							workPackageService.updateWorkPackageTestCaseExecutionPlan(wptcep);
						}
						
						if(workPackageFeatureExecutionPlanFromUI.getWorkPackage().getWorkFlowEvent() != null && 
								workPackageFeatureExecutionPlanFromUI.getWorkPackage().getWorkFlowEvent().getWorkFlow() != null &&
								workPackageFeatureExecutionPlanFromUI.getWorkPackage().getWorkFlowEvent().getWorkFlow().getStageId()!=IDPAConstants.WORKFLOW_STAGE_ID_PLANNING && 
								workPackageFeatureExecutionPlanFromUI.getWorkPackage().getWorkFlowEvent().getWorkFlow().getStageValue()<20){
							WorkFlow workFlow=workPackageService.getWorkFlowByEntityIdStageId(IDPAConstants.WORKPACKAGE_ENTITY_ID, IDPAConstants.WORKFLOW_STAGE_ID_PLANNING);
							WorkFlowEvent workFlowEvent = new WorkFlowEvent();
							workFlowEvent.setEventDate(DateUtility.getCurrentTime());
							workFlowEvent.setRemarks("Planning Workapckage Feature :"+workPackageFeatureExecutionPlanFromUI.getWorkPackage().getName());
							UserList user= userListService.getUserByLoginId(IDPAConstants.ADMIN_LOGIN_ID);
							workFlowEvent.setUser(user);
							workFlowEvent.setWorkFlow(workFlow);
							workPackage.setWorkFlowEvent(workFlowEvent);
							workPackageService.addWorkFlowEvent(workFlowEvent);
							workPackageService.updateWorkPackage(workPackage);
						}
						List<JsonWorkPackageFeatureExecutionPlan> plannedFeature = new ArrayList<JsonWorkPackageFeatureExecutionPlan>();
						JsonWorkPackageFeatureExecutionPlan jsonWpExecPlan = new JsonWorkPackageFeatureExecutionPlan(workPackageFeatureExecutionPlanFromUI);
						jsonWpExecPlan.setProductModeId(jsonWorkPackageFeatureExecutionPlan.getProductModeId());
						jsonWpExecPlan.setProductModeName(jsonWorkPackageFeatureExecutionPlan.getProductModeName());
						plannedFeature.add(jsonWpExecPlan);
						
						jTableResponse = new JTableResponse("OK",plannedFeature,1);
				}
		    } catch (Exception e) {
		        jTableResponse = new JTableResponse("ERROR","Unable to plan the feature executionselection!");
		        log.error("JSON ERROR", e);
		    }
		        
		        
	        return jTableResponse;
	    }

		@RequestMapping(value="administration.user.userList",method=RequestMethod.POST ,produces="application/json")
	    public @ResponseBody JTableResponseOptions listAllUser() {
			log.debug("inside administration.user.userList");
			JTableResponseOptions jTableResponseOptions = null;
				try {
				
					List<UserList> testLeadList = workPackageService.userListByRole(TAFConstants.ROLE_ALL);
					List<JsonUserList> jsonUserList = new ArrayList<JsonUserList>();
					
					for(UserList testLead: testLeadList){
						jsonUserList.add(new JsonUserList(testLead));
					}
					
					jTableResponseOptions = new JTableResponseOptions("OK", jsonUserList,true);     
			        } catch (Exception e) {
			        	jTableResponseOptions = new JTableResponseOptions("ERROR","Error fetching records!");
			            log.error("JSON ERROR", e);
			        }
			        
		        return jTableResponseOptions;
	    }
		
		@RequestMapping(value="workpackage.demand.projection.list",method=RequestMethod.POST ,produces="application/json")
	    public @ResponseBody JTableResponse listWorkpackageDemandProjection(HttpServletRequest req, @RequestParam int workPackageId, @RequestParam int weekNumber) {
			log.info("inside workpackage.demand.projection");
			JTableResponse jTableResponse = null;
			int weekNo = weekNumber;
			log.info("Week No from Request : " + weekNo);

			try {
					if(workPackageId<=0){
						workPackageId=1;
					}
					if (weekNo < 0) {
						weekNo = DateUtility.getWeekOfYear();
					}
					req.getSession().setAttribute("currentWorkPackageResourceDemandWeekNo", weekNo);
					
					log.info("Setting weekNo in session : " + weekNo);
					List<JsonWorkPackageDemandProjection> jsonWorkPackageDemandProjectionList=workPackageService.listWorkPackageDemandProjection(workPackageId, weekNo);
					if(jsonWorkPackageDemandProjectionList != null){
						Collections.sort(jsonWorkPackageDemandProjectionList, JsonWorkPackageDemandProjection.jsonWorkPackageDemandComparator);
					}
					if (jsonWorkPackageDemandProjectionList == null || jsonWorkPackageDemandProjectionList.isEmpty()) {
						
						log.info("No Demand projections found. Or workshifts have not been defined for the TestFactory : "+workPackageId  );
						jTableResponse = new JTableResponse("ERROR", "No Demand projections found. Or workshifts have not been defined for the TestFactory");
					} else {
						log.info("JsonWorkPackageDemandProjection size="+jsonWorkPackageDemandProjectionList.size());
						for (JsonWorkPackageDemandProjection jsonWorkPackageDemandProjection : jsonWorkPackageDemandProjectionList) {
							log.info(">>>>>>>>>>>>>..id  "+jsonWorkPackageDemandProjection.getWpDemandProjectionId());
							log.info(">>>>>>>>>>>>>..Work Date "+jsonWorkPackageDemandProjection.getWorkDate());
							log.info(">>>>>>>>>>>>>..shift Name "+jsonWorkPackageDemandProjection.getShiftName());
							log.info(">>>>>>>>>>>>>..Day1 Resource Count  "+jsonWorkPackageDemandProjection.getDay1ResourceCount());
						}
						
						jTableResponse = new JTableResponse("OK", jsonWorkPackageDemandProjectionList, jsonWorkPackageDemandProjectionList.size());
					}
		        } catch (Exception e) {
		            jTableResponse = new JTableResponse("ERROR","Error getting Resource Demand for Workpackage!");
		            log.error("JSON ERROR", e);	            
		        }
	        return jTableResponse;
	    }
		
	
		@RequestMapping(value="workpackage.executionplan.weekly",method=RequestMethod.POST ,produces="application/json")
	    public @ResponseBody JTableResponse listWorkPackageDayWisePlan(HttpServletRequest req, @RequestParam int workPackageId, @RequestParam int weekNo) {
			log.info("inside workpackage.executionplan.weekly");
			JTableResponse jTableResponse = null;
			log.info("Week No from Request : " + weekNo);

			try {
				if(workPackageId<=0){
					workPackageId=1;
				}
				if (weekNo < 0) {
					weekNo = DateUtility.getWeekOfYear();
				}
				req.getSession().setAttribute("currentWorkPackageResourceDemandWeekNo", weekNo);
					
				log.info("Setting weekNo in session : " + weekNo);
				List<JsonWorkPackageDailyPlan> jsonWorkPackageDailyPlans = workPackageService.listWorkPackageDemandDayWisePlan(workPackageId, weekNo);
				if(jsonWorkPackageDailyPlans != null){
					Collections.sort(jsonWorkPackageDailyPlans, JsonWorkPackageDailyPlan.jsonWorkPackageDailyPlanComparator);
				}
				if(jsonWorkPackageDailyPlans != null){
					log.info("JsonWorkPackageDemandProjection size="+jsonWorkPackageDailyPlans.size());
					jTableResponse = new JTableResponse("OK", jsonWorkPackageDailyPlans, jsonWorkPackageDailyPlans.size());
				}else{
					jTableResponse = new JTableResponse("OK");
				}
				jsonWorkPackageDailyPlans = null;
				 
		        } catch (Exception e) {
		            jTableResponse = new JTableResponse("ERROR","Error fetching records!");
		            log.error("JSON ERROR", e);	            
		        }
	        return jTableResponse;
	    }
		
		@RequestMapping(value="workpackage.testcaseplan.testerupdate",method=RequestMethod.POST ,produces="application/json")
		public @ResponseBody JTableResponse updateTesterWorkPackageTestCaseExecutionPlan(@ModelAttribute JsonWorkPackageTestCaseExecutionPlanForTester jsonWorkPackageTestCaseExecutionPlanForTester, BindingResult result) {
			
			JTableResponse jTableResponse = null;
			if(result.hasErrors()){
				jTableResponse = new JTableResponse("ERROR","Invalid Form!"); 
			}
			
			try {
				WorkPackageTestCaseExecutionPlan workPackageTestCaseExecutionPlanFromUI = jsonWorkPackageTestCaseExecutionPlanForTester.getWorkPackageTestCaseExecutionPlan();
				TestCaseExecutionResult testCaseExecutionResultFromUI= new TestCaseExecutionResult();
				testCaseExecutionResultFromUI.setTestCaseExecutionResultId(jsonWorkPackageTestCaseExecutionPlanForTester.getTestCaseExecutionResultId());
				testCaseExecutionResultFromUI.setComments(jsonWorkPackageTestCaseExecutionPlanForTester.getComments());
				testCaseExecutionResultFromUI.setDefectIds(jsonWorkPackageTestCaseExecutionPlanForTester.getDefectsIds());
				testCaseExecutionResultFromUI.setDefectsCount(jsonWorkPackageTestCaseExecutionPlanForTester.getDefectsCount());
				testCaseExecutionResultFromUI.setIsApproved(jsonWorkPackageTestCaseExecutionPlanForTester.getIsApproved());
				testCaseExecutionResultFromUI.setIsReviewed(jsonWorkPackageTestCaseExecutionPlanForTester.getIsReviewed());
				testCaseExecutionResultFromUI.setObservedOutput(jsonWorkPackageTestCaseExecutionPlanForTester.getObservedOutput());
				testCaseExecutionResultFromUI.setExecutionTime(jsonWorkPackageTestCaseExecutionPlanForTester.getExecutionTime());

				testCaseExecutionResultFromUI.setResult(jsonWorkPackageTestCaseExecutionPlanForTester.getResult());
				
				workPackageTestCaseExecutionPlanFromUI.setTestCaseExecutionResult(testCaseExecutionResultFromUI);				
				
				if(jsonWorkPackageTestCaseExecutionPlanForTester.getTestRunJobId() != null)
					workPackageTestCaseExecutionPlanFromUI.setTestRunJob(workPackageService.getTestRunJobById(jsonWorkPackageTestCaseExecutionPlanForTester.getTestRunJobId()));
				
				workPackageTestCaseExecutionPlanFromUI.setSourceType(jsonWorkPackageTestCaseExecutionPlanForTester.getSourceType());
				workPackageTestCaseExecutionPlanFromUI.setTestCase(testSuiteConfigurationService.getByTestCaseId(workPackageTestCaseExecutionPlanFromUI.getTestCase().getTestCaseId()));
				workPackageTestCaseExecutionPlanFromUI.setWorkPackage(workPackageService.getWorkPackageByIdWithMinimalnitialization(workPackageTestCaseExecutionPlanFromUI.getWorkPackage().getWorkPackageId()));
				WorkPackageTestCaseExecutionPlan workPackageTestCaseExecutionPlanFromDB = workPackageService.getWorkpackageTestcaseExecutionPlanById(workPackageTestCaseExecutionPlanFromUI.getId());
				
				
				
				if((workPackageTestCaseExecutionPlanFromDB.getStatus()!=null)){
					log.info("* status *="+workPackageTestCaseExecutionPlanFromDB.getStatus());
					workPackageTestCaseExecutionPlanFromUI.setStatus(workPackageTestCaseExecutionPlanFromDB.getStatus());
				}
				if((workPackageTestCaseExecutionPlanFromDB.getFeature()!=null)){
					log.info("* feature*="+workPackageTestCaseExecutionPlanFromDB.getFeature());
					workPackageTestCaseExecutionPlanFromUI.setFeature(workPackageTestCaseExecutionPlanFromDB.getFeature());
				}
				if((workPackageTestCaseExecutionPlanFromDB.getTestRunJob()!=null)){
					log.info("*testrunjob *="+workPackageTestCaseExecutionPlanFromDB.getTestRunJob());
					workPackageTestCaseExecutionPlanFromUI.setTestRunJob(workPackageTestCaseExecutionPlanFromDB.getTestRunJob());
				}
				
				workPackageTestCaseExecutionPlanFromUI.setRunConfiguration(workPackageService.getWorkpackageRunConfigurationByWPTCEP(workPackageTestCaseExecutionPlanFromUI.getRunConfiguration().getWorkpackageRunConfigurationId()));

				
				if(jsonWorkPackageTestCaseExecutionPlanForTester.getExecutionPriorityId()!=null)
					workPackageTestCaseExecutionPlanFromUI.setExecutionPriority(workPackageService.getExecutionPriorityById(jsonWorkPackageTestCaseExecutionPlanForTester.getExecutionPriorityId()));
				
				if(workPackageTestCaseExecutionPlanFromUI.getTester()!=null && workPackageTestCaseExecutionPlanFromUI.getTester().getUserId()!=null && !workPackageTestCaseExecutionPlanFromUI.getTester().getUserId().equals("0")){
					workPackageTestCaseExecutionPlanFromUI.setTester(userListService.getUserListById(workPackageTestCaseExecutionPlanFromUI.getTester().getUserId()));
				}
				if(workPackageTestCaseExecutionPlanFromUI.getTester()!=null && workPackageTestCaseExecutionPlanFromUI.getTester().getFirstName()!=null && !workPackageTestCaseExecutionPlanFromUI.getTester().getFirstName().equals("")){
					workPackageTestCaseExecutionPlanFromUI.setTester(userListService.getUserListByUserName(workPackageTestCaseExecutionPlanFromUI.getTester().getFirstName()));
				}
				
				if(workPackageTestCaseExecutionPlanFromUI.getTestLead()!=null && workPackageTestCaseExecutionPlanFromUI.getTestLead().getUserId()!=null && !workPackageTestCaseExecutionPlanFromUI.getTestLead().getUserId().equals("0")){
					workPackageTestCaseExecutionPlanFromUI.setTestLead(userListService.getUserListById(workPackageTestCaseExecutionPlanFromUI.getTestLead().getUserId()));
				}
				if(workPackageTestCaseExecutionPlanFromUI.getTestLead()!=null && workPackageTestCaseExecutionPlanFromUI.getTestLead().getFirstName()!=null && !workPackageTestCaseExecutionPlanFromUI.getTestLead().getFirstName().equals("")){
					workPackageTestCaseExecutionPlanFromUI.setTestLead(userListService.getUserListByUserName(workPackageTestCaseExecutionPlanFromUI.getTestLead().getFirstName()));
				}
				
				
				if(workPackageTestCaseExecutionPlanFromUI.getPlannedWorkShiftMaster()!=null && workPackageTestCaseExecutionPlanFromUI.getPlannedWorkShiftMaster().getShiftId()!=null && !workPackageTestCaseExecutionPlanFromUI.getPlannedWorkShiftMaster().getShiftId().equals("0")){
					workPackageTestCaseExecutionPlanFromUI.setPlannedWorkShiftMaster(workPackageService.getWorkShiftById(workPackageTestCaseExecutionPlanFromUI.getPlannedWorkShiftMaster().getShiftId()));
				}

				if(workPackageTestCaseExecutionPlanFromUI.getActualWorkShiftMaster()!=null && workPackageTestCaseExecutionPlanFromUI.getActualWorkShiftMaster().getShiftId()!=null && !workPackageTestCaseExecutionPlanFromUI.getActualWorkShiftMaster().getShiftId().equals("0")){
					workPackageTestCaseExecutionPlanFromUI.setActualWorkShiftMaster(workPackageService.getWorkShiftById(workPackageTestCaseExecutionPlanFromUI.getActualWorkShiftMaster().getShiftId()));
				}

				if(workPackageTestCaseExecutionPlanFromUI.getPlannedWorkShiftMaster()!=null && workPackageTestCaseExecutionPlanFromUI.getPlannedWorkShiftMaster().getShiftName()!=null && !workPackageTestCaseExecutionPlanFromUI.getPlannedWorkShiftMaster().getShiftName().equals("")){
					workPackageTestCaseExecutionPlanFromUI.setPlannedWorkShiftMaster(workPackageService.getWorkShiftByName(workPackageTestCaseExecutionPlanFromUI.getPlannedWorkShiftMaster().getShiftName()));
				}

				if(workPackageTestCaseExecutionPlanFromUI.getActualWorkShiftMaster()!=null && workPackageTestCaseExecutionPlanFromUI.getActualWorkShiftMaster().getShiftName()!=null && !workPackageTestCaseExecutionPlanFromUI.getActualWorkShiftMaster().getShiftName().equals("")){
					workPackageTestCaseExecutionPlanFromUI.setActualWorkShiftMaster(workPackageService.getWorkShiftByName(workPackageTestCaseExecutionPlanFromUI.getActualWorkShiftMaster().getShiftName()));
				}
				
				
				workPackageService.updateWorkPackageTestCaseExecutionPlan(workPackageTestCaseExecutionPlanFromUI);
				workPackageTestCaseExecutionPlanFromUI=workPackageService.getWorkpackageTestcaseExecutionPlanById(workPackageTestCaseExecutionPlanFromUI.getId());
				
				if(workPackageTestCaseExecutionPlanFromUI.getWorkPackage().getWorkFlowEvent().getWorkFlow().getStageId()!=IDPAConstants.WORKFLOW_STAGE_ID_EXECUTION && workPackageTestCaseExecutionPlanFromUI.getWorkPackage().getWorkFlowEvent().getWorkFlow().getStageValue()<30) {
					WorkPackage workPackage = workPackageService.getWorkPackageById(workPackageTestCaseExecutionPlanFromUI.getWorkPackage().getWorkPackageId());
					WorkFlow workFlow=workPackageService.getWorkFlowByEntityIdStageId(IDPAConstants.WORKPACKAGE_ENTITY_ID, IDPAConstants.WORKFLOW_STAGE_ID_EXECUTION);
					WorkFlowEvent workFlowEvent = new WorkFlowEvent();
					workFlowEvent.setEventDate(DateUtility.getCurrentTime());
					workFlowEvent.setRemarks("Executing Workapckage :"+workPackage.getName());
					UserList user= userListService.getUserByLoginId(IDPAConstants.ADMIN_LOGIN_ID);
					workFlowEvent.setUser(user);
					workFlowEvent.setWorkFlow(workFlow);
					workPackage.setWorkFlowEvent(workFlowEvent);
					workPackageService.addWorkFlowEvent(workFlowEvent);
					workPackageService.updateWorkPackage(workPackage);
				}
				
	
				List<JsonWorkPackageTestCaseExecutionPlanForTester> plannedTestCases = new ArrayList<JsonWorkPackageTestCaseExecutionPlanForTester>();
				plannedTestCases.add(new JsonWorkPackageTestCaseExecutionPlanForTester(workPackageTestCaseExecutionPlanFromUI));
				
				jTableResponse = new JTableResponse("OK",plannedTestCases,1);
		    } catch (Exception e) {
		        jTableResponse = new JTableResponse("ERROR","Unable to plan the testcase execution selection!");
		        log.error("JSON ERROR", e);
		    }
		        
		        
	        return jTableResponse;
	    }

		@RequestMapping(value="workpackage.demand.projection.update",method=RequestMethod.POST ,produces="application/json")
		public @ResponseBody JTableResponse updateWorkPackageDemandProjection(@ModelAttribute JsonWorkPackageDemandProjection jsonWorkPackageDemandProjection, BindingResult result) {
			log.info("inside workpackage.demand.projection.update");
			JTableResponse jTableResponse = null;
			if(result.hasErrors()){
				jTableResponse = new JTableResponse("ERROR","Invalid Form!"); 
			}
			
			try {
				
				JsonWorkPackageDemandProjection updatedJsonWorkPackageDemandProjection = workPackageService.updateWorkPackageDemandProjection(jsonWorkPackageDemandProjection);
	
				List<JsonWorkPackageDemandProjection> jsonWorkPackageDemandProjectionList = new ArrayList<JsonWorkPackageDemandProjection>();
				jsonWorkPackageDemandProjectionList.add(updatedJsonWorkPackageDemandProjection);
				
				log.info("returning data");
				jTableResponse = new JTableResponse("OK",jsonWorkPackageDemandProjectionList,1);
		    } catch (Exception e) {
		        jTableResponse = new JTableResponse("ERROR","Unable to update workdemand projection !");
		        log.error("JSON ERROR", e);
		    }
		        
	        return jTableResponse;
	    }
				
		@RequestMapping(value="administration.shift.workShiftList",method=RequestMethod.POST ,produces="application/json")
	    public @ResponseBody JTableResponseOptions listAllWorkShift() {
			log.debug("inside administration.shift.workShiftList");
			JTableResponseOptions jTableResponseOptions = null;
				try {
				
					List<WorkShiftMaster> workShiftMasterList = workPackageService.listAllWorkShift();
					
					List<JsonWorkShiftMaster> jsonWorkShiftMaster= new ArrayList<JsonWorkShiftMaster>();
					
					for(WorkShiftMaster workShiftMaster: workShiftMasterList){
						jsonWorkShiftMaster.add(new JsonWorkShiftMaster(workShiftMaster));
					}
					
					jTableResponseOptions = new JTableResponseOptions("OK", jsonWorkShiftMaster);     
			        } catch (Exception e) {
			        	jTableResponseOptions = new JTableResponseOptions("ERROR","Error fetching records!");
			            log.error("JSON ERROR", e);
			        }
			        
		        return jTableResponseOptions;
	    }

		@RequestMapping(value="administration.workpackage.mapenvironmentcombination",method=RequestMethod.POST ,produces="application/json")
		public  @ResponseBody JTableResponse mapEnvironmentCombinationWithWorkpackage(@ModelAttribute JsonRunConfiguration jsonRunConfiguration, BindingResult result,@RequestParam Integer workpackageId) {
			log.info("inside mapEnvironmentWIthWorkpackage");
			JTableResponse jTableResponse = null;
			int envId=0;
			 WorkPackage workPackage=workPackageService.getWorkPackageById(workpackageId);

			//checking whether already testcase exists
			Set<WorkPackageTestCase> workPackageTestCases = workPackage.getWorkPackageTestCases();
			Set<WorkPackageTestCaseExecutionPlan> workPackageTestCaseExecutionPlanListFromDB =workPackage.getWorkPackageTestCaseExecutionPlan();
			List<WorkPackageTestCaseExecutionPlan> workPackageTestCaseExecutionPlanListForUpdate= new ArrayList<WorkPackageTestCaseExecutionPlan>();
			RunConfiguration runConfiguration=productListService.getRunConfigurationById(jsonRunConfiguration.getRunconfigId());
			
			List<WorkPackageTestCaseExecutionPlan>	deleteList= new ArrayList<WorkPackageTestCaseExecutionPlan>();
			
			if(jsonRunConfiguration.getStatus()==0){
				List<WorkPackageTestCaseExecutionPlan> workPackageTestCaseExecutionPlanList= new ArrayList<WorkPackageTestCaseExecutionPlan>(workPackageTestCaseExecutionPlanListFromDB);
				workPackageService.deleteWorkPackageTestcaseExecutionPlan(workPackageTestCaseExecutionPlanList);
				workPackage=workPackageService.deleteRunConfigurationWorkpackage(workpackageId,runConfiguration.getRunconfigId(),"testcase");
				
			}else if (jsonRunConfiguration.getStatus()==1){
				workPackageService.addRunConfigurationWorkpackage(workpackageId,runConfiguration.getRunconfigId(),"testcase");

				WorkpackageRunConfiguration workpackageRunConfiguration=workPackageService.getWorkpackageRunConfiguration(workpackageId, runConfiguration.getRunconfigId(), "testcase");
				for(WorkPackageTestCase workPackageTestCase:workPackageTestCases){
					if(workPackageTestCase.getIsSelected()==1){
						
						
						WorkPackageTestCaseExecutionPlan workPackageTestCaseExecutionPlan = new WorkPackageTestCaseExecutionPlan();
						workPackageTestCaseExecutionPlan.setTestCase(workPackageTestCase.getTestCase());
						workPackageTestCaseExecutionPlan.setWorkPackage(workPackageService.getWorkPackageById(workpackageId));
						workPackageTestCaseExecutionPlan.setRunConfiguration(workpackageRunConfiguration);
						workPackageTestCaseExecutionPlan.setExecutionStatus(3);
						workPackageTestCaseExecutionPlan.setIsExecuted(0);
						ExecutionPriority executionPriority=null;
						if(workPackageTestCase.getTestCase().getTestCasePriority()!=null)
							executionPriority= workPackageService.getExecutionPriorityByName(CommonUtility.getExecutionPriority(workPackageTestCase.getTestCase().getTestCasePriority().getPriorityName()));
						else
							executionPriority= workPackageService.getExecutionPriorityByName(IDPAConstants.EXECUTION_PRIORITY_HIGH);
						
						workPackageTestCaseExecutionPlan.setExecutionPriority(executionPriority);
						
						TestCaseExecutionResult testCaseExecutionResult = new TestCaseExecutionResult();
						testCaseExecutionResult.setResult("");
						testCaseExecutionResult.setComments("");
						testCaseExecutionResult.setDefectsCount(0);
						testCaseExecutionResult.setDefectIds("");
						testCaseExecutionResult.setIsApproved(0);
						testCaseExecutionResult.setIsReviewed(0);
						testCaseExecutionResult.setObservedOutput("");
						
						//Commented the following statements when remove the WorkPackageTestcaseExecutionId column from Test Case Execution Result table. By: Logeswari, On :  11-Feb-2015
						testCaseExecutionResult.setWorkPackageTestCaseExecutionPlan(workPackageTestCaseExecutionPlan);
						workPackageTestCaseExecutionPlan.setTestCaseExecutionResult(testCaseExecutionResult);
						workPackageTestCaseExecutionPlanListForUpdate.add(workPackageTestCaseExecutionPlan);
					}
					
				}
				workPackageService.addWorkPackageTestcaseExecutionPlan(workPackageTestCaseExecutionPlanListForUpdate);
				
				jTableResponse = new JTableResponse("OK","Environment Added Successfully");
			}
			 return jTableResponse;
	    }
		
		
		@RequestMapping(value="administration.workpackage.mapenvironment",method=RequestMethod.POST ,produces="application/json")
		public  @ResponseBody JTableResponse mapEnvironmentWithWorkpackage(@ModelAttribute JsonEnvironment jsonEnvironment, BindingResult result,@RequestParam Integer workpackageId,@RequestParam Integer productId) {
			log.info("inside mapEnvironmentWIthWorkpackage");
			JTableResponse jTableResponse = null;
			String destinationArray="";
			List<Environment> environmentSourceListDB = productListService.getEnvironmentListByProductId(productId);
			Set<Environment> environmentDestinationListDB = null;//workPackageService.getEnvironmentMappedToWorkpackage(workpackageId);
			int envId=0;
			WorkPackageTestCase updatedWorkPackageTestCase=null;
			WorkPackage workPackage=null;
			
			if(jsonEnvironment.getIsSelected()==0){
				workPackage = workPackageService.mapWorkpackageEnv(workpackageId,jsonEnvironment.getEnvironmentId(),"Remove");
				if(workPackage==null){
					 jTableResponse = new JTableResponse("ERROR","Unable to remove the environments");
					 return jTableResponse;
				}
			}else if (jsonEnvironment.getIsSelected()==1){
				workPackage = workPackageService.mapWorkpackageEnv(workpackageId,jsonEnvironment.getEnvironmentId(),"Add");
			}
			
			workPackage=workPackageService.getWorkPackageById(workpackageId);

			//checking whether already testcase exists
			Set<WorkPackageTestCase> workPackageTestCases = workPackage.getWorkPackageTestCases();
			Set<WorkPackageTestCaseExecutionPlan> workPackageTestCaseExecutionPlanListFromDB =workPackage.getWorkPackageTestCaseExecutionPlan();

			if(workPackageTestCases.size()>0){
				List<Environment> environmentUpdatedList = new ArrayList<Environment>();
				environmentUpdatedList.add(workPackageService.getEnvironmentById(jsonEnvironment.getEnvironmentId()));
				if(jsonEnvironment.getIsSelected()==1){
					updatedWorkPackageTestCase = workPackageService.updateWorkPackageTestCase(workPackageTestCases,environmentUpdatedList,workPackageTestCaseExecutionPlanListFromDB,workpackageId);
					jTableResponse = new JTableResponse("OK","Environment Added Successfully");
				}else if(jsonEnvironment.getIsSelected()==0){
					updatedWorkPackageTestCase = workPackageService.deleteWorkPackageTestCase(workPackageTestCases,environmentUpdatedList,workPackageTestCaseExecutionPlanListFromDB);
					jTableResponse = new JTableResponse("OK","Environment Removed Successfully");
				}
			}
			 return jTableResponse;
	    }
		
		public String[] convertStringToStringArray(String delimiter,String value){
			String[] result=null;
			if(value!=null && value.trim().length()>0){
				result=value.split(delimiter);
			}
			return result;
		}
		
		@RequestMapping(value="administration.workpackage.maplocale")
		public String mapLocaleWithWorkpackage(ModelMap model,ServletRequest req) {
			log.info("inside mapLocaleWithWorkpackage");
			
			String destinationArray = req.getParameter("destinationArray");
			String sourceArray = req.getParameter("sourceArray");
			String workpackageId = req.getParameter("workpackageId");
			String productId = req.getParameter("productId");
			
			String[] destinationLocaleUI = convertStringToStringArray(",",destinationArray);
			String[] sourceLocaleUI = convertStringToStringArray(",",sourceArray);
			List<ProductLocale> localeSourceListDB = productListService.getProductLocaleListByProductId(Integer.parseInt(productId));
			Set<ProductLocale> localeDestinationListDB = workPackageService.getLocaleMappedToWorkpackage(Integer.parseInt(workpackageId));
			
			
			List<String> localeUpdated=new ArrayList<String>();
			List<String> localeRemoved=new ArrayList<String>();
	
			List<String> localeDestinationDBName=new ArrayList<String>();
			for(ProductLocale localeDB:localeDestinationListDB){
				localeDestinationDBName.add(localeDB.getLocaleName());
			}
			List<String> localeDestinationUIName=new ArrayList<String>();
			if(destinationLocaleUI!=null && !(destinationLocaleUI.length<=0)){
				localeDestinationUIName = Arrays.asList(destinationLocaleUI);
			}
			for(String valueDB:localeDestinationDBName){
				if(!localeDestinationUIName.contains(valueDB)){
					localeRemoved.add(valueDB);
				}
			}
			
			for(String valueDB:localeDestinationUIName){
				if(!localeDestinationDBName.contains(valueDB)){
					localeUpdated.add(valueDB);
				}
			}
			
			int localeId=0;
			WorkPackage workPackage=null;
			//Removed
			if(localeRemoved!=null && ! localeRemoved.isEmpty()){
				for(String localeName:localeRemoved){
					if(localeName!=null){
						localeId=productListService.getLocaleByNameByProduct(localeName,productId).getProductLocaleId();
						workPackage = workPackageService.mapWorkpackageLocale(Integer.parseInt(workpackageId),localeId,"Remove");
					}
				}
			}
			
			// Adding
			if(localeUpdated!=null && !localeUpdated.isEmpty()){
				for(String localeName:localeUpdated){
					localeId=productListService.getLocaleByNameByProduct(localeName,productId).getProductLocaleId();
					workPackage = workPackageService.mapWorkpackageLocale(Integer.parseInt(workpackageId),localeId,"Add");
				}
			}
			
			workPackage=workPackageService.getWorkPackageById(Integer.parseInt(workpackageId));
			
			//checking whether already testcase exists
			Set<WorkPackageTestCase> workPackageTestCases = workPackage.getWorkPackageTestCases();
			Set<WorkPackageTestCaseExecutionPlan> workPackageTestCaseExecutionPlanListFromDB =workPackage.getWorkPackageTestCaseExecutionPlan();
			if(workPackageTestCases.size()>0){
				List<ProductLocale> localeUpdatedList = new ArrayList<ProductLocale>();
				List<ProductLocale> localeRemovedList = new ArrayList<ProductLocale>();
				for(String localeName:localeUpdated){
					localeUpdatedList.add(productListService.getLocaleByName(localeName));
				}
				for(String localeName:localeRemoved){
					localeRemovedList.add(productListService.getLocaleByName(localeName));
				}
						WorkPackageTestCase updatedWorkPackageTestCase=null;
						WorkPackageTestCase deleteWorkPackageTestCase =null;
				if(localeUpdatedList!=null && !localeUpdatedList.isEmpty() && !(localeUpdatedList.size()<=0))
					updatedWorkPackageTestCase = workPackageService.updateWorkPackageTestCaseLocale(workPackageTestCases,localeUpdatedList,workPackageTestCaseExecutionPlanListFromDB);
				if(localeRemovedList!=null && !localeRemovedList.isEmpty() && !(localeRemovedList.size()<=0))
					deleteWorkPackageTestCase = workPackageService.deleteWorkPackageTestCaseLocale(workPackageTestCases,localeRemovedList,workPackageTestCaseExecutionPlanListFromDB);
			}
	        return "workpackageWithLocale";
	    }
		
		private JsonWorkPackageTestCase setLocaleSelectionsInJsonWorkPackageTestCase(JsonWorkPackageTestCase jsonWorkPackageTestCase, WorkPackageTestCase workPackageTestCase ) {
			
			List<ProductLocale> allLocales = productListService.getProductLocaleListByProductId(workPackageTestCase.getWorkPackage().getProductBuild().getProductVersion().getProductMaster().getProductId());
			List<ProductLocale> selectedLocales = workPackageService.getWorkPackageTestCasesExecutionPlanLocales(workPackageTestCase.getWorkPackage().getWorkPackageId(), workPackageTestCase.getTestCase().getTestCaseId());
			if (allLocales.size() == selectedLocales.size()) {
				
				jsonWorkPackageTestCase.setLoc1("1");
				jsonWorkPackageTestCase.setLoc2("1");
				jsonWorkPackageTestCase.setLoc3("1");
				jsonWorkPackageTestCase.setLoc4("1");
				jsonWorkPackageTestCase.setLoc5("1");
				jsonWorkPackageTestCase.setLoc6("1");
				jsonWorkPackageTestCase.setLoc7("1");
				jsonWorkPackageTestCase.setLoc8("1");
				jsonWorkPackageTestCase.setLoc9("1");
				jsonWorkPackageTestCase.setLoc10("1");
			} else {
			
				int countOfAllLocales = allLocales.size();
				log.info("Total Locales : " + countOfAllLocales);
				log.info("Selected locales : " + selectedLocales.size());
				if (1 <= countOfAllLocales) {
					if (selectedLocales.contains(allLocales.get(0))) {
						jsonWorkPackageTestCase.setLoc1("1");
					} else {
						jsonWorkPackageTestCase.setLoc1("0");
					}
				}
				if (2 <= countOfAllLocales) {
					if (selectedLocales.contains(allLocales.get(1))) {
						jsonWorkPackageTestCase.setLoc2("1");
					} else {
						jsonWorkPackageTestCase.setLoc2("0");
					}
				}
				if (3 <= countOfAllLocales) {
					if (selectedLocales.contains(allLocales.get(2))) {
						jsonWorkPackageTestCase.setLoc3("1");
					} else {
						jsonWorkPackageTestCase.setLoc3("0");
					}
				}
				if (4 <= countOfAllLocales) {
					if (selectedLocales.contains(allLocales.get(3))) {
						jsonWorkPackageTestCase.setLoc4("1");
					} else {
						jsonWorkPackageTestCase.setLoc4("0");
					}
				}
				if (5 <= countOfAllLocales) {
					if (selectedLocales.contains(allLocales.get(4))) {
						jsonWorkPackageTestCase.setLoc5("1");
					} else {
						jsonWorkPackageTestCase.setLoc5("0");
					}
				}
				if (6 <= countOfAllLocales) {
					if (selectedLocales.contains(allLocales.get(5))) {
						jsonWorkPackageTestCase.setLoc6("1");
					} else {
						jsonWorkPackageTestCase.setLoc6("0");
					}
				}
				if (7 <= countOfAllLocales) {
					if (selectedLocales.contains(allLocales.get(6))) {
						jsonWorkPackageTestCase.setLoc7("1");
					} else {
						jsonWorkPackageTestCase.setLoc7("0");
					}
				}
				if (8 <= countOfAllLocales) {
					if (selectedLocales.contains(allLocales.get(7))) {
						jsonWorkPackageTestCase.setLoc8("1");
					} else {
						jsonWorkPackageTestCase.setLoc8("0");
					}
				}
				if (9 <= countOfAllLocales) {
					if (selectedLocales.contains(allLocales.get(8))) {
						jsonWorkPackageTestCase.setLoc9("1");
					} else {
						jsonWorkPackageTestCase.setLoc9("0");
					}
				}
				if (10 <= countOfAllLocales) {
					if (selectedLocales.contains(allLocales.get(9))) {
						jsonWorkPackageTestCase.setLoc10("1");
					} else {
						jsonWorkPackageTestCase.setLoc10("0");
					}
				}
			}
			return jsonWorkPackageTestCase;
		}
		

		@RequestMapping(value="workPackage.status.summary.list",method=RequestMethod.POST ,produces="application/json")
		public @ResponseBody JTableResponse viewWorkPackageStatusSummary(@RequestParam int productId,HttpServletRequest req) {
			log.info("workPackage.status.summary.list");
			JTableResponse jTableResponse = null;
			log.info("productId: " + productId);
			
			
			UserList user=(UserList)req.getSession().getAttribute("USER");
			ProductUserRole productUserRole= productListService.getProductUserRoleByUserId(user.getUserId());
			
			try {
					
					List<ProductBuild> listOfProductBuilds = new ArrayList<ProductBuild>();
					List<ProductVersionListMaster> listOfProductVersions = productListService.listProductVersion(productId);
					
					log.info("listOfProductVersions size: "+listOfProductVersions.size());
					for (ProductVersionListMaster productVersionListMaster : listOfProductVersions) {
						List<ProductBuild> subListOfProductBuilds = null;
						if(productVersionListMaster != null){
							log.info("productVersionListMaster.getProductVersionListId()::::  "+productVersionListMaster.getProductVersionListId());
							subListOfProductBuilds = productListService.listProductBuild(productVersionListMaster.getProductVersionListId());
							log.info("subListOfProductBuilds ::::  "+subListOfProductBuilds.size());
							if(listOfProductBuilds.size() == 0){
								listOfProductBuilds = subListOfProductBuilds;
							}else{
								listOfProductBuilds.addAll(subListOfProductBuilds);
							}
						}
					}
					
					log.info("***********listOfProductBuilds size::::: "+listOfProductBuilds.size());
					List<JsonWorkPackageStatusSummary> jsonWorkPackageStatusSummary = workPackageService.listWorkPackageStatusSummary(listOfProductBuilds,productUserRole);
					log.info("JsonResourceAvailability size="+jsonWorkPackageStatusSummary.size());
					jTableResponse = new JTableResponse("OK", jsonWorkPackageStatusSummary, jsonWorkPackageStatusSummary.size());
					jsonWorkPackageStatusSummary = null;
		        } catch (Exception e) {
		            jTableResponse = new JTableResponse("ERROR","Error fetching records!");
		            log.error("JSON ERROR", e);	            
		        }
	        return jTableResponse;
		}
		
		@RequestMapping(value="workPackage.status.summary.selected.environments.list",method=RequestMethod.POST ,produces="application/json")
		public @ResponseBody JTableResponse listSelectedEnvironmentsOfworkPackage(@RequestParam int workPackageId) {
			log.info("workPackage.status.summary.selected.environments.list**********");
			JTableResponse jTableResponse = null;
			log.info("workPackageId: " + workPackageId);
			try {
				Set<RunConfiguration> runConfigurationListDB = workPackageService.getEnvironmentMappedToWorkpackage(workPackageId);
				List<JsonRunConfiguration> jsonruncconfiguration = new ArrayList<JsonRunConfiguration>();
				for (RunConfiguration rc : runConfigurationListDB) {
					jsonruncconfiguration.add(new JsonRunConfiguration(rc));
				}
				 jTableResponse = new JTableResponse("OK", jsonruncconfiguration,jsonruncconfiguration.size());     
	        } catch (Exception e) {
	            jTableResponse = new JTableResponse("ERROR","Error fetching records!");
	            log.error("JSON ERROR", e);
	        }
	        return jTableResponse;
		}
			
		@RequestMapping(value="workPackage.status.summary.selected.locale.list",method=RequestMethod.POST ,produces="application/json")
		public @ResponseBody JTableResponse listSelectedLocalesOfworkPackage(@RequestParam int workPackageId) {
			log.info("workPackage.status.summary.selected.locale.list**********");
			JTableResponse jTableResponse = null;
			log.info("workPackageId: " + workPackageId);
			try {
				Set<ProductLocale> productLocaleList = workPackageService.getLocaleMappedToWorkpackage(workPackageId);
				List<JsonProductLocale> jsonProductLocale = new ArrayList<JsonProductLocale>();
				for (ProductLocale productLocale : productLocaleList) {
					jsonProductLocale.add(new JsonProductLocale(productLocale));
				}
				 jTableResponse = new JTableResponse("OK", jsonProductLocale,jsonProductLocale.size());     
	        } catch (Exception e) {
	            jTableResponse = new JTableResponse("ERROR","Error fetching records!");
	            log.error("JSON ERROR", e);
	        }
	        return jTableResponse;
		}
		
		@RequestMapping(value="workPackage.status.summary.selected.testcases.list",method=RequestMethod.POST ,produces="application/json")
		public @ResponseBody JTableResponse listSelectedTestCasesOfworkPackage(@RequestParam int workPackageId,@RequestParam int jtStartIndex,@RequestParam int jtPageSize) {
			log.info("workPackage.status.summary.selected.total.testcases.list**********jtStartIndex: "+jtStartIndex+"   jtPageSize: "+jtPageSize);
			JTableResponse jTableResponse = null;
			log.info("workPackageId: " + workPackageId);
			try {
				List<WorkPackageTestCase> workPackageTestCases = workPackageService.listWorkPackageTestCases(workPackageId);
				List<JsonWorkPackageTestCase> jsonWorkPackageTestCases = new ArrayList<JsonWorkPackageTestCase>();
				JsonWorkPackageTestCase jsonWorkPackageTestCase = null;
				if (workPackageTestCases != null && workPackageTestCases.size()>0) {
					for(WorkPackageTestCase workPackageTestCase: workPackageTestCases){
						jsonWorkPackageTestCase = new JsonWorkPackageTestCase(workPackageTestCase);
						jsonWorkPackageTestCases.add(jsonWorkPackageTestCase);
					}
					log.info("workPackage.status.summary.notcompleted.testcases.list : "+jsonWorkPackageTestCases.size());
					jTableResponse = new JTableResponse("OK", jsonWorkPackageTestCases, jsonWorkPackageTestCases.size());
					workPackageTestCases = null;
				}
	        } catch (Exception e) {
	            jTableResponse = new JTableResponse("ERROR","Error fetching records!");
	            log.error("JSON ERROR", e);
	        }
	        return jTableResponse;
		}
		
		@RequestMapping(value="workPackage.status.summary.total.testcases.for.execution.list",method=RequestMethod.POST ,produces="application/json")
		public @ResponseBody JTableResponse listTotalTestcaseForExecutionOfworkPackage(@RequestParam int workPackageId,@RequestParam int jtStartIndex,@RequestParam int jtPageSize,HttpServletRequest req) {
			log.info("workPackage.status.summary.total.testcases.for.execution.list**********");
			JTableResponse jTableResponse = null;
			log.info("workPackageId: " + workPackageId);
			
			UserList user=(UserList)req.getSession().getAttribute("USER");
			ProductUserRole productUserRole= productListService.getProductUserRoleByUserId(user.getUserId());
			
			try {
				List<WorkPackageTestCaseExecutionPlan> workPackageTestCaseExecutionPlanList = workPackageService.listWorkPackageTestCasesExecutionPlan(workPackageId, jtStartIndex,jtPageSize,productUserRole);
				List<JsonWorkPackageTestCaseExecutionPlan> jsonWorkPackageTestCaseExecutionPlanList = new ArrayList<JsonWorkPackageTestCaseExecutionPlan>();
				JsonWorkPackageTestCaseExecutionPlan jsonWorkPackageTestCaseExecutionPlan = null;
				if (workPackageTestCaseExecutionPlanList != null && workPackageTestCaseExecutionPlanList.size()>0) {
					for(WorkPackageTestCaseExecutionPlan workPackageTestCaseExecutionPlan: workPackageTestCaseExecutionPlanList){
						jsonWorkPackageTestCaseExecutionPlan = new JsonWorkPackageTestCaseExecutionPlan(workPackageTestCaseExecutionPlan);
						jsonWorkPackageTestCaseExecutionPlanList.add(jsonWorkPackageTestCaseExecutionPlan);
					}
					log.info("workPackage.status.summary.testcases.for.execution.list : "+jsonWorkPackageTestCaseExecutionPlanList.size());
					jTableResponse = new JTableResponse("OK", jsonWorkPackageTestCaseExecutionPlanList, jsonWorkPackageTestCaseExecutionPlanList.size());
					workPackageTestCaseExecutionPlanList = null;
				}
	        } catch (Exception e) {
	            jTableResponse = new JTableResponse("ERROR","Error fetching records!");
	            log.error("JSON ERROR", e);
	        }
	        return jTableResponse;
		}
		
		@RequestMapping(value="workPackage.status.summary.completed.testcases.list",method=RequestMethod.POST ,produces="application/json")
		public @ResponseBody JTableResponse listCompletedTestCasesOfworkPackage(@RequestParam int workPackageId,@RequestParam int jtStartIndex,@RequestParam int jtPageSize,HttpServletRequest req) {
			log.info("workPackage.status.summary.completed.testcases.list**********");
			JTableResponse jTableResponse = null;
			log.info("workPackageId: " + workPackageId);
			int executedStatusId = 1;
						
			UserList user=(UserList)req.getSession().getAttribute("USER");
			ProductUserRole productUserRole= productListService.getProductUserRoleByUserId(user.getUserId());
			
			try {
				List<WorkPackageTestCaseExecutionPlan> workPackageTestCaseExecutionPlanList = workPackageService.listCompletedOrNotCompletedTestCasesOfworkPackage(workPackageId, jtStartIndex,jtPageSize,executedStatusId,productUserRole);
				List<JsonWorkPackageTestCaseExecutionPlan> jsonWorkPackageTestCaseExecutionPlanList = new ArrayList<JsonWorkPackageTestCaseExecutionPlan>();
				JsonWorkPackageTestCaseExecutionPlan jsonWorkPackageTestCaseExecutionPlan = null;
				if (workPackageTestCaseExecutionPlanList != null && workPackageTestCaseExecutionPlanList.size()>0) {
					for(WorkPackageTestCaseExecutionPlan workPackageTestCaseExecutionPlan: workPackageTestCaseExecutionPlanList){
						jsonWorkPackageTestCaseExecutionPlan = new JsonWorkPackageTestCaseExecutionPlan(workPackageTestCaseExecutionPlan);
						jsonWorkPackageTestCaseExecutionPlanList.add(jsonWorkPackageTestCaseExecutionPlan);
					}
					log.info("workPackage.status.summary.completed.testcases.list : "+jsonWorkPackageTestCaseExecutionPlanList.size());
					jTableResponse = new JTableResponse("OK", jsonWorkPackageTestCaseExecutionPlanList, jsonWorkPackageTestCaseExecutionPlanList.size());
					workPackageTestCaseExecutionPlanList = null;
				}
	        } catch (Exception e) {
	            jTableResponse = new JTableResponse("ERROR","Error fetching records!");
	            log.error("JSON ERROR", e);
	        }
	        return jTableResponse;
		}
		
		@RequestMapping(value="workPackage.status.summary.notcompleted.testcases.list",method=RequestMethod.POST ,produces="application/json")
		public @ResponseBody JTableResponse listNotCompletedTestCasesOfworkPackage(@RequestParam int workPackageId,@RequestParam int jtStartIndex,@RequestParam int jtPageSize,HttpServletRequest req) {
			log.info("workPackage.status.summary.notcompleted.testcases.list**********");
			JTableResponse jTableResponse = null;
			log.info("workPackageId: " + workPackageId);
			int notExecutedStatusId = 0;
			
			UserList user=(UserList)req.getSession().getAttribute("USER");
			ProductUserRole productUserRole= productListService.getProductUserRoleByUserId(user.getUserId());
			
			try {
				List<WorkPackageTestCaseExecutionPlan> workPackageTestCaseExecutionPlanList = workPackageService.listCompletedOrNotCompletedTestCasesOfworkPackage(workPackageId, jtStartIndex,jtPageSize,notExecutedStatusId,productUserRole);
				List<JsonWorkPackageTestCaseExecutionPlan> jsonWorkPackageTestCaseExecutionPlanList = new ArrayList<JsonWorkPackageTestCaseExecutionPlan>();
				JsonWorkPackageTestCaseExecutionPlan jsonWorkPackageTestCaseExecutionPlan = null;
				if (workPackageTestCaseExecutionPlanList != null && workPackageTestCaseExecutionPlanList.size()>0) {
					for(WorkPackageTestCaseExecutionPlan workPackageTestCaseExecutionPlan: workPackageTestCaseExecutionPlanList){
						jsonWorkPackageTestCaseExecutionPlan = new JsonWorkPackageTestCaseExecutionPlan(workPackageTestCaseExecutionPlan);
						jsonWorkPackageTestCaseExecutionPlanList.add(jsonWorkPackageTestCaseExecutionPlan);
					}
					log.info("workPackage.status.summary.notcompleted.testcases.list : "+jsonWorkPackageTestCaseExecutionPlanList.size());
					jTableResponse = new JTableResponse("OK", jsonWorkPackageTestCaseExecutionPlanList, jsonWorkPackageTestCaseExecutionPlanList.size());
					workPackageTestCaseExecutionPlanList = null;
				}
	        } catch (Exception e) {
	            jTableResponse = new JTableResponse("ERROR","Error fetching records!");
	            log.error("JSON ERROR", e);
	        }
	        return jTableResponse;
		}
		
		
		@RequestMapping(value="workpackage.testcaseplan.update.bulk",method=RequestMethod.POST ,produces="application/json")
		public @ResponseBody JTableSingleResponse updateWorkPackageTestCaseExecutionPlanBulk(
	    		@RequestParam Integer testerId, @RequestParam Integer testLeadId, @RequestParam String wptcepListsFromUI, @RequestParam String plannedExecutionDate,@RequestParam Integer executionPriorityId,@RequestParam Integer shiftId) {
			
			JTableSingleResponse jTableSingleResponse = null;
			
			try {
				log.info("wptcepListsFromUI==>"+wptcepListsFromUI);
				String[] wptcepLists = wptcepListsFromUI.split(",");
			
				
				
				String[] filteredWptcepLists = new String[wptcepLists.length];
				
				WorkPackageTestCaseExecutionPlan workPackageTestCaseExecutionPlanFromUI=null;
				
				int counter=0;
				for(String Id : wptcepLists){
					workPackageTestCaseExecutionPlanFromUI=workPackageService.getWorkpackageTestcaseExecutionPlanById(Integer.parseInt(Id));
					
		    		if(workPackageTestCaseExecutionPlanFromUI.getIsExecuted() != 1){
						
		    			filteredWptcepLists[counter]=Id;
		    			counter++;
		    			
					}
		    		
				}
				
				workPackageService.updateWorkPackageTestCaseExecutionPlan(filteredWptcepLists,testerId,testLeadId,plannedExecutionDate,executionPriorityId,shiftId);
				jTableSingleResponse = new JTableSingleResponse("OK");
		    } 
			catch (Exception e) {
	            jTableSingleResponse = new JTableSingleResponse("ERROR","Unable to plan the testcase executionselection!");
	            log.error("JSON ERROR", e);	            
	        }
		        
		        
	        return jTableSingleResponse;
	    }
		
		@RequestMapping(value="workpackage.featureplan.update.bulk",method=RequestMethod.POST ,produces="application/json")
		public @ResponseBody JTableSingleResponse updateWorkPackageFeatureExecutionPlanBulk(
	    		@RequestParam Integer testerId, @RequestParam Integer testLeadId, @RequestParam String wptcepListsFromUI, @RequestParam String plannedExecutionDate,@RequestParam Integer executionPriorityId,@RequestParam Integer shiftId) {
			
			JTableSingleResponse jTableSingleResponse = null;
			
			try {
				String[] wptcepLists = wptcepListsFromUI.split(",");
				workPackageService.updateWorkPackageFeatureExecutionPlan(wptcepLists,testerId,testLeadId,plannedExecutionDate,executionPriorityId,shiftId);
	
				jTableSingleResponse = new JTableSingleResponse("OK");
		    } 
			catch (Exception e) {
	            jTableSingleResponse = new JTableSingleResponse("ERROR","Unable to plan the testcase executionselection!");
	            log.error("JSON ERROR", e);	            
	        }
		        
		        
	        return jTableSingleResponse;
	    }
		@RequestMapping(value="workPackage.plan.status",method=RequestMethod.POST ,produces="application/json")
		public @ResponseBody JTableResponse listWorkpackageTestCaseExecutionPlanningStatus(@RequestParam int workPackageId) {
			log.info("workPackage.status.summaryview"+workPackageId);
			JTableResponse jTableResponse = null;
			try {
				JsonWorkPackageTestCaseExecutionPlanStatus jsonWorkPackageTestCaseExecutionPlanStatus =null;
				WorkPackageTestCaseExecutionPlanStatusDTO workPackageTestCaseExecutionPlanStatusDTO  = workPackageService.listWorkpackageTestCaseExecutionPlanningStatus(workPackageId);
					log.info("getAssignedTesterCount size="+workPackageTestCaseExecutionPlanStatusDTO.getAssignedTesterCount());
					log.info("getAssignedTestLeadCount size="+workPackageTestCaseExecutionPlanStatusDTO.getAssignedTestLeadCount());
					log.info("getTotalTestCaseCount size="+workPackageTestCaseExecutionPlanStatusDTO.getTotalTestCaseCount());
					log.info("getPlannedExecutionDateCount size="+workPackageTestCaseExecutionPlanStatusDTO.getPlannedExecutionDateCount());
					
					List<JsonWorkPackageTestCaseExecutionPlanStatus> jsonWorkPackageTestCaseExecutionPlanStatusList = new ArrayList<JsonWorkPackageTestCaseExecutionPlanStatus>();
					if(workPackageTestCaseExecutionPlanStatusDTO!=null){
						jsonWorkPackageTestCaseExecutionPlanStatus = new JsonWorkPackageTestCaseExecutionPlanStatus(workPackageTestCaseExecutionPlanStatusDTO);
						jsonWorkPackageTestCaseExecutionPlanStatusList.add(jsonWorkPackageTestCaseExecutionPlanStatus);
					}
					log.info("workPackage.plan.status : "+jsonWorkPackageTestCaseExecutionPlanStatusList.size());
					jTableResponse = new JTableResponse("OK", jsonWorkPackageTestCaseExecutionPlanStatusList, jsonWorkPackageTestCaseExecutionPlanStatusList.size());
		        } catch (Exception e) {
		            jTableResponse = new JTableResponse("ERROR","Error fetching records!");
		            log.error("JSON ERROR", e);	            
		        }
	        return jTableResponse;
		}
		@RequestMapping(value="workpackage.testcase.execution.list",method=RequestMethod.POST ,produces="application/json")
	    public @ResponseBody JTableResponse listWorkpackageTestCaseExecutionListForTester(@RequestParam int workPackageId, HttpServletRequest req,@RequestParam String nodeType,@RequestParam int testcaseId) {
			log.debug("inside workpackage.testcase.execution.list");
			UserList user=(UserList)req.getSession().getAttribute("USER");
			int jtStartIndex=0;
			int jtPageSize=10;
			JTableResponse jTableResponse = null;
				try {
					if(workPackageId<=0){
						workPackageId=1;
					}
					
					List<WorkPackageTestCaseExecutionPlan> workPackageTestCaseExecutionPlanList=workPackageService.listWorkPackageTestCasesExecutionPlan(workPackageId, jtStartIndex,jtPageSize,user,"");
					
					List<JsonWorkPackageTestCaseExecutionPlanForTester> jsonWorkPackageTestCaseExecutionPlans=new ArrayList<JsonWorkPackageTestCaseExecutionPlanForTester>();
					
					if (workPackageTestCaseExecutionPlanList == null || workPackageTestCaseExecutionPlanList.isEmpty()) {
						
						//TODO : Initialize the workpackage with the test cases for the product
						log.info("No workpackage testcase execution plan for workpackage id:"+workPackageId  );
						jTableResponse = new JTableResponse("OK", jsonWorkPackageTestCaseExecutionPlans, 0);
					} else {
						for(WorkPackageTestCaseExecutionPlan workPackageTestCaseExecutionPlan: workPackageTestCaseExecutionPlanList){
							jsonWorkPackageTestCaseExecutionPlans.add(new JsonWorkPackageTestCaseExecutionPlanForTester(workPackageTestCaseExecutionPlan));
						}
				        req.setAttribute("workPackageTestCaseExecutionPlanList", workPackageTestCaseExecutionPlanList);
						jTableResponse = new JTableResponse("OK", jsonWorkPackageTestCaseExecutionPlans,jsonWorkPackageTestCaseExecutionPlans.size() );
						workPackageTestCaseExecutionPlanList = null;
					}
		        } catch (Exception e) {
		            jTableResponse = new JTableResponse("ERROR","Error fetching records!");
		            log.error("JSON ERROR", e);	            
		        }
	        return jTableResponse;
	    }	
		
		@RequestMapping(value="workpackage.testcase.details",method=RequestMethod.POST ,produces="application/json")
	    public @ResponseBody JTableResponse getWorkpackageTestcaseExecutionTestcaseDetails(@RequestParam int testcaseId, HttpServletRequest req,@RequestParam int wptcepId,@RequestParam String mode) {
			log.debug("inside workpackage.testcase.details");
			UserList user=(UserList)req.getSession().getAttribute("USER");
			int jtStartIndex=0;
			int jtPageSize=10;
			JTableResponse jTableResponse = null;
				try {

					req.getSession().setAttribute("user", user);
					
					req.getSession().setAttribute("tcermode", mode);
					
					WorkPackageTestCaseExecutionPlan testcaseDetails=workPackageService.listWorkPackageTestCasesExecutionPlan(wptcepId, testcaseId);
					
					List<JsonWorkPackageTestCaseExecutionPlanForTester> jsonWorkPackageTestCaseExecutionPlans=new ArrayList<JsonWorkPackageTestCaseExecutionPlanForTester>();
					
					if (testcaseDetails == null ) {
						
						//TODO : Initialize the workpackage with the test cases for the product
						jTableResponse = new JTableResponse("OK", jsonWorkPackageTestCaseExecutionPlans, 0);
					} else {
						TestCaseExecutionResult testCaseExecutionResult = workPackageService.getTestCaseExecutionResultByID(wptcepId);
						//Set WorkPackage ActualExecutionStartDate if not exists(actualexedate)
						WorkPackage wp = testCaseExecutionResult.getWorkPackageTestCaseExecutionPlan().getWorkPackage();
						//Check WP was created by TRP or Manually created.
						Integer wpTypeId = 0;
						if(wp.getTestRunPlan() == null){
							wpTypeId = 4;
						}else{//WP was created by TRP
							//Check if WP-TRP Execution Type Automated or Manual 
							wpTypeId = workPackageService.getWPTypeByTestRunPlanExecutionType(wp.getWorkPackageId());	
						}						
						testCaseExecutionResult.setStartTime(new Date(System.currentTimeMillis()));
						if(wpTypeId == 3){//Automated
							//Handled at Jobs execution stage
						}else if(wpTypeId == 4){//Manual
							if(wp.getActualStartDate() == null){
								//chk if any TC for WP was FIRST executed, then fetch StartTime and set it to WP ActualStartDate
								Date tcexeStartTime = workPackageService.getFirstExecutedTCStartTimeofWP(wp.getWorkPackageId());
								if(tcexeStartTime != null){ //So a TC was executed First already...
									workPackageService.setUpdateWPActualStartDate(wp.getWorkPackageId(), tcexeStartTime);
								}else{//if no was TC executed, if this is the 1st TC to be executed, then set current Time to WP ActualStartDate
									workPackageService.setUpdateWPActualStartDate(wp.getWorkPackageId(), testCaseExecutionResult.getStartTime());
								}
							}							
						}
						
						
						
						workPackageService.updateTestCaseResults(testCaseExecutionResult);
						jsonWorkPackageTestCaseExecutionPlans.add(new JsonWorkPackageTestCaseExecutionPlanForTester(testcaseDetails));
						
						jTableResponse = new JTableResponse("OK", jsonWorkPackageTestCaseExecutionPlans, jsonWorkPackageTestCaseExecutionPlans.size());
						testcaseDetails = null;
					}
		        } catch (Exception e) {
		            jTableResponse = new JTableResponse("ERROR","Error fetching records!");
		            log.error("JSON ERROR", e);	            
		        }
	        return jTableResponse;
	    }	
		
		@RequestMapping(value="workpackage.result.testcase.details",method=RequestMethod.POST ,produces="application/json")
	    public @ResponseBody JTableResponse getWorkpackageTestcaseExecutionResultTestcaseDetails(@RequestParam int testCaseExecutionResultId, @RequestParam int testcaseId, HttpServletRequest req) {
			log.debug("inside workpackage.testcase.details");
			UserList user=(UserList)req.getSession().getAttribute("USER");
			int jtStartIndex=0;
			int jtPageSize=10;
			String evidence ="";
			int count=0;
			JTableResponse jTableResponse = null;
				try {
					req.getSession().setAttribute("user", user);					
					WorkPackageTestCaseExecutionPlan testcaseDetails=workPackageService.listWorkPackageTestCasesExecutionPlanOfTCExeId(testCaseExecutionResultId);
					if(testcaseDetails != null){
						List<Evidence> evidenList = workPackageService.testcaseListByEvidence(testcaseDetails.getId(),"testcase");
						if(evidenList.size()>0){
							String exportLocation = CommonUtility.getCatalinaPath();
							log.info("exportLocation ="+exportLocation);
							for(Evidence evedence : evidenList){
							String url = evedence.getFileuri();
							if(count>0){
									evidence = evidence+"," ;
								}
							evidence = evidence +exportLocation+url+"@"+evedence.getEvidencename();
								++count;
							}
						}
						
					}
					
					List<JsonWorkPackageTestCaseExecutionPlanForTester> jsonWorkPackageTestCaseExecutionPlans=new ArrayList<JsonWorkPackageTestCaseExecutionPlanForTester>();
				
					if (testcaseDetails == null ) {			
						jTableResponse = new JTableResponse("OK", jsonWorkPackageTestCaseExecutionPlans, 0);
					} else {	
						JsonWorkPackageTestCaseExecutionPlanForTester jsonwptester =	new JsonWorkPackageTestCaseExecutionPlanForTester(testcaseDetails);
						Set<Integer> listOfTestCaseIds = new HashSet<Integer>();
						listOfTestCaseIds.add(testcaseId);
						ListMultimap<Integer, Object> multimap = ArrayListMultimap.create();
						multimap = report.getFeatureNamesByTestCaseIdList(listOfTestCaseIds);
						if(multimap != null){
							List<Object> mappedFeaturesToTestCase = multimap.get(testcaseId);							
							String featurenames = "";
							StringBuffer sbFeatureNames = new StringBuffer();
							if(mappedFeaturesToTestCase != null && mappedFeaturesToTestCase.size() > 0){
								for(int ii=0;ii < mappedFeaturesToTestCase.size();ii++){
									if(mappedFeaturesToTestCase.get(ii) != null){
										featurenames = mappedFeaturesToTestCase.get(ii).toString();
										sbFeatureNames.append(featurenames+",");
									}
								}
								featurenames = sbFeatureNames.toString();
								if(featurenames != null && !featurenames.isEmpty()){
									featurenames = featurenames.substring(0,featurenames.length()-1);
									jsonwptester.setFeatureName(featurenames);									
								}	
							}													
						}
						
						if(evidence != ""){
							jsonwptester.setEvidenceLabel(evidence);
						}
						jsonWorkPackageTestCaseExecutionPlans.add(jsonwptester);
						
						jTableResponse = new JTableResponse("OK", jsonWorkPackageTestCaseExecutionPlans, jsonWorkPackageTestCaseExecutionPlans.size());
						testcaseDetails = null;
					}
		        } catch (Exception e) {
		            jTableResponse = new JTableResponse("ERROR","Error fetching records!");
		            log.error("JSON ERROR", e);	            
		        }
	        return jTableResponse;
	    }	
		

		@RequestMapping(value="product.testcase.overall.details",method=RequestMethod.POST ,produces="application/json")
	    public @ResponseBody JTableResponse getProductTestCaseOverAllDetails(@RequestParam int testCaseId, HttpServletRequest req) {
			log.debug("inside workpackage.testcase.details");
			UserList user=(UserList)req.getSession().getAttribute("USER");
			JTableResponse jTableResponse = null;
				try {
					req.getSession().setAttribute("user", user);					
					TestCaseList tcObj = testCaseService.getTestCaseById(testCaseId);			
					Integer mappedTestScriptCount=testCaseAutomationScriptDAO.getMappedTestscriptCountByTestCaseId(testCaseId);
					Integer mappedFeatureCount=testCaseService.getMappedFeatureCountByTestcaseId(testCaseId);
					List<JsonWorkPackageTestCaseExecutionPlanForTester> jsonWorkPackageTestCaseExecutionPlans=new ArrayList<JsonWorkPackageTestCaseExecutionPlanForTester>();
					if (tcObj == null ) {						
						//TODO : Initialize the workpackage with the test cases for the product
						jTableResponse = new JTableResponse("OK", jsonWorkPackageTestCaseExecutionPlans, 0);
					} else {					
						tcObj.setMappedFeatueCount(mappedFeatureCount);
						tcObj.setMappedTestscriptCount(mappedTestScriptCount);
						jsonWorkPackageTestCaseExecutionPlans.add(new JsonWorkPackageTestCaseExecutionPlanForTester(tcObj));
						
						jTableResponse = new JTableResponse("OK", jsonWorkPackageTestCaseExecutionPlans, jsonWorkPackageTestCaseExecutionPlans.size());
						tcObj = null;
					}
		        } catch (Exception e) {
		            jTableResponse = new JTableResponse("ERROR","Error fetching records!");
		            log.error("JSON ERROR", e);	            
		        }
	        return jTableResponse;
	    }
		
		@RequestMapping(value="testcase.defect.list",method=RequestMethod.POST ,produces="application/json")
	    public @ResponseBody JTableResponse getWorkpackageTestcaseExecutionTestcaseDefectDetails(@RequestParam int tcerId, HttpServletRequest req,int jtStartIndex, int jtPageSize) {
			log.debug("inside testcase.defect.list");
			UserList user=(UserList)req.getSession().getAttribute("USER");
			WorkpackageRunConfiguration workpackageRunConfiguration=null;
			JTableResponse jTableResponse = null;
				try {
					List<TestExecutionResultBugList> defectList=workPackageService.listDefectsByTestcaseExecutionPlanId(tcerId,jtStartIndex, jtPageSize);
					if(defectList.size()==0){
						log.info("defectList.size()==>"+defectList.size());
					}
					TestCaseExecutionResult testCaseExecutionResult =  workPackageService.getTestCaseExecutionResultByID(tcerId);
				    	
					if(testCaseExecutionResult.getWorkPackageTestCaseExecutionPlan()!=null){
						if(testCaseExecutionResult.getWorkPackageTestCaseExecutionPlan().getRunConfiguration()!=null){
						workpackageRunConfiguration=testCaseExecutionResult.getWorkPackageTestCaseExecutionPlan().getRunConfiguration();
						}
					}
					 
					RunConfiguration runConfiguration=null;
					if(workpackageRunConfiguration!=null){
						runConfiguration =workpackageRunConfiguration.getRunconfiguration();
					}
				//	
					List<JsonTestExecutionResultBugList> jsonTestExecutionResultBugList=new ArrayList<JsonTestExecutionResultBugList>();
					
					if (defectList == null || defectList.isEmpty() || defectList.size()==0  ) {
						
						jTableResponse = new JTableResponse("OK", jsonTestExecutionResultBugList, 0);
					} else {
						for(TestExecutionResultBugList defect: defectList){
							
							jsonTestExecutionResultBugList.add(new JsonTestExecutionResultBugList(defect,runConfiguration));
						}
						
						jTableResponse = new JTableResponse("OK", jsonTestExecutionResultBugList,jsonTestExecutionResultBugList.size() );
						defectList = null;
					}
		        } catch (Exception e) {
		            jTableResponse = new JTableResponse("ERROR","Error fetching records!");
		            log.error("JSON ERROR", e);	            
		        }
	        return jTableResponse;
	    }	
		@RequestMapping(value="testcase.defect.add",method=RequestMethod.POST ,produces="application/json")
	    public @ResponseBody JTableSingleResponse addTestCaseDefect(HttpServletRequest request, @ModelAttribute JsonTestExecutionResultBugList jsonTestExecutionResultBugList, BindingResult result,@RequestParam int tcerId) {			
			JTableSingleResponse jTableSingleResponse;
			if(result.hasErrors()){
				jTableSingleResponse = new JTableSingleResponse("ERROR","Invalid form!"); 
			}			
			try {
				Integer svId=jsonTestExecutionResultBugList.getSeverityId();
				jsonTestExecutionResultBugList.setApproversDefectSeverityId(svId);
				log.info("jsonTestExecutionResultBugList.getApproversDefectSeverityId();==>"+jsonTestExecutionResultBugList.getApproversDefectSeverityId());
				UserList userlog = (UserList)request.getSession().getAttribute("USER");
				TestExecutionResultBugList testExecutionResultBugList  =jsonTestExecutionResultBugList.getTestExecutionResultBugList();
				
				if(testExecutionResultBugList.getBugFilingStatus() != null &&testExecutionResultBugList.getBugFilingStatus().getWorkFlowId() != null){
					int bugFilingStatusId=testExecutionResultBugList.getBugFilingStatus().getWorkFlowId();	
					WorkFlow workFlow=new WorkFlow();					
					workFlow=workPackageService.getWorkFlowByEntityIdWorkFlowId(IDPAConstants.ENTITY_DEFECT_ID, bugFilingStatusId);
					testExecutionResultBugList.setBugFilingStatus(workFlow);
				}
				
				
				if(jsonTestExecutionResultBugList.getSeverityId() != null){
					DefectSeverity defectSeverity =  testExecutionBugsService.getDefectSeverityByseverityId(jsonTestExecutionResultBugList.getSeverityId());
					testExecutionResultBugList.setDefectSeverity(defectSeverity);
					testExecutionResultBugList.setApproversDefectSeverity(defectSeverity);
				}
				
				if(jsonTestExecutionResultBugList.getDefectTypeId() != null){
					DefectTypeMaster defectType =  testExecutionBugsService.getDefectTypeById(jsonTestExecutionResultBugList.getDefectTypeId());
					testExecutionResultBugList.setDefectType(defectType);
				}
				
				if(jsonTestExecutionResultBugList.getTestersPriorityId() != null){
					ExecutionPriority testersExecutionPriority = new ExecutionPriority();
					testersExecutionPriority.setExecutionPriorityId(jsonTestExecutionResultBugList.getTestersPriorityId());
					testExecutionResultBugList.setTestersPriority(testersExecutionPriority);
					// Adding the testers priority as approver's priority. So that this can be changes if the approver feels the defect priority needs to  be changed.
					testExecutionResultBugList.setApproversPriority(testersExecutionPriority);
				}
				
				if(jsonTestExecutionResultBugList.getIsReproducableOnLive() != null){
					testExecutionResultBugList.setIsReproducableOnLive(jsonTestExecutionResultBugList.getIsReproducableOnLive());
				}else{
					testExecutionResultBugList.setIsReproducableOnLive("2");
				}
				
				if(jsonTestExecutionResultBugList.getIsThereABugAlready() != null){
					testExecutionResultBugList.setIsThereABugAlready(jsonTestExecutionResultBugList.getIsThereABugAlready());
				}else{
					testExecutionResultBugList.setIsThereABugAlready("2");
				}
				
				if(jsonTestExecutionResultBugList.getWasOnPrevDayWebRelease() != null){
					testExecutionResultBugList.setWasOnPrevDayWebRelease(jsonTestExecutionResultBugList.getWasOnPrevDayWebRelease());
				}else{
					testExecutionResultBugList.setWasOnPrevDayWebRelease("2");
				}
				
				testExecutionResultBugList.setIsApproved(0);
				testExecutionResultBugList.setUploadFlag(0);
				DefectApprovalStatusMaster defectApprovalStatus = new DefectApprovalStatusMaster();
				defectApprovalStatus.setApprovalStatusId(1);
				testExecutionResultBugList.setDefectApprovalStatus(defectApprovalStatus);
				
				//
				TestCaseExecutionResult testCaseExecutionResult = null;
				WorkPackageTestCaseExecutionPlan workPackageTestCaseExecutionPlan=null;
				int defectsCount=0;
				if(tcerId!=0){
					testCaseExecutionResult = workPackageService.getTestCaseExecutionResultByID(tcerId);
					defectsCount = testCaseExecutionResult.getDefectsCount();
					testExecutionResultBugList.setTestCaseExecutionResult(testCaseExecutionResult);
					workPackageTestCaseExecutionPlan = testCaseExecutionResult.getWorkPackageTestCaseExecutionPlan();
				}
				if(workPackageTestCaseExecutionPlan.getWorkPackage().getProductBuild().getBuildType() != null){
					int buildTypeId = workPackageTestCaseExecutionPlan.getWorkPackage().getProductBuild().getBuildType().getStageId();
					//log.info("********* Build Type: "+buildTypeId);
					DefectIdentificationStageMaster defectIdentificationStage =  testExecutionBugsService.getDefectIdentificationStageMasterById(buildTypeId);
					testExecutionResultBugList.setDefectFoundStage(defectIdentificationStage);
				}	
				
				testCaseExecutionResult.setDefectsCount(defectsCount+1);
				workPackageService.updateTestCaseExecutionResult(testCaseExecutionResult);
				if(testCaseExecutionResult != null && testCaseExecutionResult.getTestCaseExecutionResultId() != null){					
					mongoDBService.addTestCaseExecutionResult(testCaseExecutionResult.getTestCaseExecutionResultId());					
				}
				workPackageService.addTestCaseDefect(testExecutionResultBugList);
				mongoDBService.addBug(testExecutionResultBugList.getTestExecutionResultBugId());
				// Added for auto post defect starts
				
				TestRunPlan testRunPlan=workPackageTestCaseExecutionPlan.getWorkPackage().getTestRunPlan();
				if(testRunPlan!=null && testRunPlan.getAutoPostBugs()!=null && testRunPlan.getAutoPostBugs()==1 ){
					ProductMaster productMaster=workPackageTestCaseExecutionPlan.getWorkPackage().getProductBuild().getProductVersion().getProductMaster();
					int defectSystemId=-1;
					DefectManagementSystem  defectManagementSystem=defectManagementService.getPrimaryDMSByProductId(productMaster.getProductId());
					toolsController.testDefectExportByBug(testExecutionResultBugList.getTestExecutionResultBugId(), request, null, defectManagementSystem.getDefectManagementSystemId()+"");
					
				}
				// Added for auto post defect ends
				
				log.info(">>>>>testExecutionResultBugList"+testExecutionResultBugList.getTestExecutionResultBugId());
			
				if(workPackageTestCaseExecutionPlan.getWorkPackage().getWorkFlowEvent().getWorkFlow() != null 
						&& workPackageTestCaseExecutionPlan.getWorkPackage().getWorkFlowEvent().getWorkFlow().getStageId()!=IDPAConstants.WORKFLOW_STAGE_ID_EXECUTION 
						&& workPackageTestCaseExecutionPlan.getWorkPackage().getWorkFlowEvent().getWorkFlow().getStageValue()<30) {
					WorkPackage workPackage = workPackageService.getWorkPackageById(workPackageTestCaseExecutionPlan.getWorkPackage().getWorkPackageId());
					WorkFlow workFlowWP=workPackageService.getWorkFlowByEntityIdStageId(IDPAConstants.WORKPACKAGE_ENTITY_ID, IDPAConstants.WORKFLOW_STAGE_ID_EXECUTION);
					WorkFlowEvent workFlowEvent = new WorkFlowEvent();
					workFlowEvent.setEventDate(DateUtility.getCurrentTime());
					workFlowEvent.setRemarks("Executing Workapckage :"+workPackage.getName());
					UserList user= userListService.getUserByLoginId(IDPAConstants.ADMIN_LOGIN_ID);
					workFlowEvent.setUser(user);
					workFlowEvent.setWorkFlow(workFlowWP);
					workPackage.setWorkFlowEvent(workFlowEvent);
					workPackageService.addWorkFlowEvent(workFlowEvent);
					workPackageService.updateWorkPackage(workPackage);
				}
		            jTableSingleResponse = new JTableSingleResponse("OK",new JsonTestExecutionResultBugList(testExecutionResultBugList));
		        } catch (Exception e) {
		            jTableSingleResponse = new JTableSingleResponse("ERROR","Error adding a record!");
		            log.error("JSON ERROR", e);
		        }		        
	        return jTableSingleResponse;			
	    }
		
		@RequestMapping(value="testcase.defect.update",method=RequestMethod.POST ,produces="application/json")
		public @ResponseBody JTableResponse updateTestCaseDefect(@ModelAttribute JsonTestExecutionResultBugList jsonTestExecutionResultBugList, BindingResult result,@RequestParam int tcerId) {
			JTableResponse jTableResponse;
			if(result.hasErrors()){
				jTableResponse = new JTableResponse("ERROR","Invalid Form!"); 
			}			
			try {	
		
				log.info("BugId---> :"+jsonTestExecutionResultBugList.getTestExecutionResultBugId());
				log.info("TestCaseId---> :"+jsonTestExecutionResultBugList.getTestCaseExecutionResultId());
				log.info("tcerId---> :"+tcerId);
				TestExecutionResultBugList testExecutionResultBugListFromUI  =jsonTestExecutionResultBugList.getTestExecutionResultBugList();
				
				if(tcerId!=0){
					TestCaseExecutionResult testCaseExecutionResult=new TestCaseExecutionResult();
					testCaseExecutionResult = workPackageService.getTestCaseExecutionResultByID(tcerId);
					testExecutionResultBugListFromUI.setTestCaseExecutionResult(testCaseExecutionResult);
				}
				if(jsonTestExecutionResultBugList.getSeverityId() != null){
					DefectSeverity defectSeverity =  testExecutionBugsService.getDefectSeverityByseverityId(jsonTestExecutionResultBugList.getSeverityId());
					testExecutionResultBugListFromUI.setDefectSeverity(defectSeverity);
				}
				
				if(jsonTestExecutionResultBugList.getDefectTypeId() != null){
					DefectTypeMaster defectType =  testExecutionBugsService.getDefectTypeById(jsonTestExecutionResultBugList.getDefectTypeId());
					testExecutionResultBugListFromUI.setDefectType(defectType);
				}
				
				if(jsonTestExecutionResultBugList.getDefectIdentifiedInStageId() != null){
					DefectIdentificationStageMaster defectIdentificationStage =  testExecutionBugsService.getDefectIdentificationStageMasterById(jsonTestExecutionResultBugList.getDefectIdentifiedInStageId());
					testExecutionResultBugListFromUI.setDefectFoundStage(defectIdentificationStage);
				}		
					workPackageService.updateTestCaseDefect(testExecutionResultBugListFromUI);
					log.info("log.info**"+testExecutionResultBugListFromUI.getTestExecutionResultBugId());
					
					mongoDBService.addBug(testExecutionResultBugListFromUI.getTestExecutionResultBugId());
					log.info("mongo*"+testExecutionResultBugListFromUI.getTestExecutionResultBugId());
					List<JsonTestExecutionResultBugList> tmpList = new ArrayList();
					tmpList.add(new JsonTestExecutionResultBugList(testExecutionResultBugListFromUI));
					jTableResponse = new JTableResponse("OK",tmpList ,1);					
					
		        } catch (Exception e) {
		            jTableResponse = new JTableResponse("ERROR","Error updating record!");
		            log.error("JSON ERROR", e);
		            e.printStackTrace();
		        }       
	        return jTableResponse;
	    }
		
		@RequestMapping(value="testcase.results.list",method=RequestMethod.POST ,produces="application/json")
	    public @ResponseBody JTableResponse getTestcaseResultDetails(@RequestParam int tcerId, HttpServletRequest req) {
			log.debug("inside testcase.results.list");
			UserList user=(UserList)req.getSession().getAttribute("USER");
			
			JTableResponse jTableResponse = null;
				try {
					List<TestCaseExecutionResult> resultList=workPackageService.listResultsByTestcaseExecutionPlanId(tcerId);
					
					List<JsonTestCaseExecutionResult> jsonTestCaseExecutionResult=new ArrayList<JsonTestCaseExecutionResult>();
					
					if (resultList == null || resultList.isEmpty()) {
						
						jTableResponse = new JTableResponse("OK", jsonTestCaseExecutionResult, 0);
					} else {
						for(TestCaseExecutionResult result: resultList){
							jsonTestCaseExecutionResult.add(new JsonTestCaseExecutionResult(result));
						}
						jTableResponse = new JTableResponse("OK", jsonTestCaseExecutionResult,jsonTestCaseExecutionResult.size() );
						resultList = null;
					}
		        } catch (Exception e) {
		            jTableResponse = new JTableResponse("ERROR","Error fetching records!");
		            log.error("JSON ERROR", e);	            
		        }
	        return jTableResponse;
	    }
		
		@RequestMapping(value="workpackage.testcase.execution.summary.list",method=RequestMethod.POST ,produces="application/json")
	    public @ResponseBody JTableResponse getWorkPackageTestcaseExecutionSummary(@RequestParam int workPackageId, HttpServletRequest req) {
			log.debug("workpackage.testcase.execution.summary.list");
			UserList user=(UserList)req.getSession().getAttribute("USER");
			int tcerId = 0;
			JTableResponse jTableResponse = null;
				try {
					
					WorkPackageTestCaseSummaryDTO workPackageTestCaseSummaryDTO=workPackageService.listWorkPackageTestCaseExecutionSummary(workPackageId);
					Locale locale=(Locale)req.getSession().getAttribute("locale");
					String localeId=(String)req.getSession().getAttribute("localeId");
					
					List<JsonWorkPackageTestCaseExecutionSummary> jsonWorkPackageTestCaseExecutionSummaryList = new ArrayList<JsonWorkPackageTestCaseExecutionSummary>();
					
									
					
					if (workPackageTestCaseSummaryDTO == null ) {
						
						jTableResponse = new JTableResponse("OK", jsonWorkPackageTestCaseExecutionSummaryList, 0);
					} else {
						
						jsonWorkPackageTestCaseExecutionSummaryList.add(new JsonWorkPackageTestCaseExecutionSummary(workPackageTestCaseSummaryDTO,locale));
						jTableResponse = new JTableResponse("OK", jsonWorkPackageTestCaseExecutionSummaryList,jsonWorkPackageTestCaseExecutionSummaryList.size() );
						workPackageTestCaseSummaryDTO = null;
					}
					
		        } catch (Exception e) {
		            jTableResponse = new JTableResponse("ERROR","Error fetching records!");
		            log.error("JSON ERROR", e);	            
		        }
	        return jTableResponse;
	    }
		
		@RequestMapping(value="workpackage.testcase.execution.summary.listByDate",method=RequestMethod.POST ,produces="application/json")
	    public @ResponseBody JTableResponse getWorkPackageTestcaseExecutionSummaryByDate(@RequestParam Integer workPackageId, HttpServletRequest req) {
			log.debug("workpackage.testcase.execution.summary.list");
			UserList user=(UserList)req.getSession().getAttribute("USER");
			int tcerId = 0;
			JTableResponse jTableResponse = null;
				try {
					
					List<WorkPackageTestCaseSummaryDTO> workPackageTestCaseSummaryDTOList=workPackageService.listWorkPackageTestCaseExecutionSummaryByDate(workPackageId);
		
					
					List<JsonWorkPackageTestCaseExecutionSummary> jsonWorkPackageTestCaseExecutionSummaryList = new ArrayList<JsonWorkPackageTestCaseExecutionSummary>();
					
									
					
					if (workPackageTestCaseSummaryDTOList== null || workPackageTestCaseSummaryDTOList.isEmpty() ) {
						
						jTableResponse = new JTableResponse("OK", jsonWorkPackageTestCaseExecutionSummaryList, 0);
					} else {
						for(WorkPackageTestCaseSummaryDTO workPackageTestCaseSummaryDTO:workPackageTestCaseSummaryDTOList){
							jsonWorkPackageTestCaseExecutionSummaryList.add(new JsonWorkPackageTestCaseExecutionSummary(workPackageTestCaseSummaryDTO));
						}
						jTableResponse = new JTableResponse("OK", jsonWorkPackageTestCaseExecutionSummaryList,jsonWorkPackageTestCaseExecutionSummaryList.size() );
						workPackageTestCaseSummaryDTOList = null;
					}
					
		        } catch (Exception e) {
		            jTableResponse = new JTableResponse("ERROR","Error fetching records!");
		            log.error("JSON ERROR", e);	            
		        }
	        return jTableResponse;
	    }
		
		@RequestMapping(value="workpackage.testcase.execution.summary",method=RequestMethod.POST ,produces="application/json")
	    public @ResponseBody JTableResponse getWorkPackageTestcaseExecutionSummaryReport(@RequestParam Integer workpackageId, HttpServletRequest req) {
			log.info("workpackage.testcase.execution.summary of workpackageId -	"+workpackageId);
			
			JTableResponse jTableResponse = null;
				try {
					
					JsonWorkPackageTestCaseExecutionPlanForTester jwptecpt = workPackageService.listWorkPackageTestCaseExecutionSummaryReport(workpackageId);
					List<JsonWorkPackageTestCaseExecutionPlanForTester> jwptecptList= new ArrayList<JsonWorkPackageTestCaseExecutionPlanForTester>();									
					
					if (jwptecpt== null  ) {						
						jTableResponse = new JTableResponse("OK", jwptecptList, 0);
					} else {
						jwptecptList.add(jwptecpt);
						jTableResponse = new JTableResponse("OK", jwptecptList,jwptecptList.size() );
						jwptecpt = null;
					}	
					
		        } catch (Exception e) {
		            jTableResponse = new JTableResponse("ERROR","Error fetching WorkPackage Summary for Tiles records!");
		            log.error("JSON ERROR", e);	            
		        }
	        return jTableResponse;
	    }
		
		@RequestMapping(value="workpackage.typeof.testrunplan.bywpId",method=RequestMethod.POST ,produces="application/json")
		public @ResponseBody JTableSingleResponse getWPTypeByTestRunPlanType(HttpServletRequest request, @RequestParam int workPackageId) {
			JTableSingleResponse jTableSingleResponse = null;
			
			try {
				Integer wpTypeId = workPackageService.getWPTypeByTestRunPlanExecutionType(workPackageId);
				jTableSingleResponse = new JTableSingleResponse("OK", wpTypeId);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				jTableSingleResponse = new JTableSingleResponse("ERROR","Error obtaining WP Type!");
				e.printStackTrace();
			}
			return jTableSingleResponse;
		}
			
			
		@RequestMapping(value="testcase.results.update",method=RequestMethod.POST ,produces="application/json")
		public @ResponseBody JTableResponse updateTestcaseResults(HttpServletRequest request, @RequestParam int tcerId,@RequestParam String modifiedfField,@RequestParam String modifiedValue ,@RequestParam String executionTime) {
			JTableResponse jTableResponse;
			Long executionSeconds=null;
			UserList userObj = null;
			
			try {
				userObj = (UserList)request.getSession().getAttribute("USER");
				TestCaseExecutionResult testCaseExecutionResult  =workPackageService.getTestCaseExecutionResultByID(tcerId);
				WorkPackageTestCaseExecutionPlan workPackageTestCaseExecutionPlan = workPackageService.getWorkpackageTestcaseExecutionPlanById(tcerId);
				if((modifiedfField != null) && (modifiedfField != "")) {
					if(modifiedfField.equalsIgnoreCase("observedOutput")){
						testCaseExecutionResult.setObservedOutput(modifiedValue);
					}else if(modifiedfField.equalsIgnoreCase("comments")){
						testCaseExecutionResult.setComments(modifiedValue);
					}else if(modifiedfField.equalsIgnoreCase("result")){
						if(modifiedValue.equalsIgnoreCase("Pass")){
							String exceptedOutPut=workPackageTestCaseExecutionPlan.getTestCase().getTestcaseexpectedoutput();
							
							if(exceptedOutPut==null || exceptedOutPut==""){
								exceptedOutPut="Passed";
							}
							testCaseExecutionResult.setObservedOutput(exceptedOutPut);
						testCaseExecutionResult.setResult(IDPAConstants.EXECUTION_RESULT_PASSED);
						}
						else if(modifiedValue.equalsIgnoreCase("Fail")){
							testCaseExecutionResult.setObservedOutput("Failed");
	
							testCaseExecutionResult.setResult(IDPAConstants.EXECUTION_RESULT_FAILED);}
						else if(modifiedValue.equalsIgnoreCase("No Run")){
							testCaseExecutionResult.setObservedOutput("No Run");
	
							testCaseExecutionResult.setResult(IDPAConstants.EXECUTION_RESULT_NORUN);}
						else if(modifiedValue.equalsIgnoreCase("Blocked")){
							testCaseExecutionResult.setObservedOutput("Blocked");
	
							testCaseExecutionResult.setResult(IDPAConstants.EXECUTION_RESULT_BLOCKED);
						}
						workPackageTestCaseExecutionPlan.setActualExecutionDate(DateUtility.getCurrentTime());
						workPackageTestCaseExecutionPlan.setIsExecuted(1);
						workPackageTestCaseExecutionPlan.setExecutionStatus(2);
						workPackageTestCaseExecutionPlan.setModifiedDate(DateUtility.getCurrentTime());
						workPackageService.updateWorkPackageTestCaseExecutionPlan(workPackageTestCaseExecutionPlan);
						
						// Added for autopost result
						WorkPackage workPackage =workPackageTestCaseExecutionPlan.getWorkPackage();
					//	workPackage.getTestcaseList();
						ProductMaster productMaster =workPackage.getProductBuild().getProductVersion().getProductMaster();
						if(workPackage.getTestRunPlan()!=null){
							TestManagementSystem testManagementSystem = testManagementService.getPrimaryTMSByProductId(productMaster.getProductId());
							if(testManagementSystem!=null){
								// TODo after Adpater change
							}

						}
							
					}
				}
				if(executionTime!=null && executionTime!=""){
					executionSeconds=CommonUtility.getSeconds(executionTime);
					if(executionSeconds!=0){
						testCaseExecutionResult.setExecutionTime(executionSeconds);
					}
				}
				testCaseExecutionResult.setWorkPackageTestCaseExecutionPlan(workPackageService.getWorkpackageTestcaseExecutionPlanById(workPackageTestCaseExecutionPlan.getId()));
				//Check WP was created by TRP or Manually created.
				Integer wpTypeId = 0;
				WorkPackage wp = testCaseExecutionResult.getWorkPackageTestCaseExecutionPlan().getWorkPackage();
				if(wp.getTestRunPlan() == null){
					wpTypeId = 4;
				}else{//WP was created by TRP
					//Check if WP-TRP Execution Type Automated or Manual 
					wpTypeId = workPackageService.getWPTypeByTestRunPlanExecutionType(wp.getWorkPackageId());	
				}						
				testCaseExecutionResult.setEndTime(new Date(System.currentTimeMillis()));
				if(wpTypeId == 3){//Automated
					//Handled at Jobs execution stage
				}else if(wpTypeId == 4){//Manual
					//Whether any TC was last executed or not, put current time as WP ActualEndDate, as current TC got executed.
					workPackageService.setUpdateWPActualEndtDate(wp.getWorkPackageId(), testCaseExecutionResult.getEndTime());
				}			
				
				workPackageService.updateTestCaseResults(testCaseExecutionResult);
				mongoDBService.addTestCaseExecutionResult(testCaseExecutionResult.getTestCaseExecutionResultId());
				if(modifiedfField.equalsIgnoreCase("actualDate")){
					workPackageTestCaseExecutionPlan.setActualExecutionDate(DateUtility.dateformatWithOutTime(modifiedValue));
					workPackageService.updateWorkPackageTestCaseExecutionPlan(workPackageTestCaseExecutionPlan);
				}else if(modifiedfField.equalsIgnoreCase("actualshift")){
					workPackageTestCaseExecutionPlan.setActualWorkShiftMaster(workPackageService.getWorkShiftById(Integer.parseInt(modifiedValue)));
					workPackageService.updateWorkPackageTestCaseExecutionPlan(workPackageTestCaseExecutionPlan);
				}else if(modifiedfField.equalsIgnoreCase("executionStatus") ){
					workPackageTestCaseExecutionPlan.setIsExecuted(Integer.parseInt(modifiedValue));
					workPackageTestCaseExecutionPlan.setExecutionStatus(2);
					workPackageService.updateWorkPackageTestCaseExecutionPlan(workPackageTestCaseExecutionPlan);
				}
				
					Integer wpid = workPackageTestCaseExecutionPlan.getWorkPackage().getWorkPackageId();					
					List<WorkPackageTestCaseExecutionPlan> wptcep =workPackageService.getNotExecutedTestCaseListByWpId(wpid);					
					log.info("getNotExecutedTestCaseListByWpId");
					 if(wptcep == null || wptcep.isEmpty() || wptcep.size() == 0){
						log.info("getNotExecutedTestCaseListByWpId");
					}
					else if(workPackageTestCaseExecutionPlan.getWorkPackage().getWorkFlowEvent().getWorkFlow().getStageId()!=IDPAConstants.WORKFLOW_STAGE_ID_EXECUTION && workPackageTestCaseExecutionPlan.getWorkPackage().getWorkFlowEvent().getWorkFlow().getStageValue()<30){
						
						
					}
					 
					 //Updating status job level - Manual
					 log.info("getNotExecutedTestCaseListByJobId");
					 Integer jobid = workPackageTestCaseExecutionPlan.getTestRunJob().getTestRunJobId();
					 List<WorkPackageTestCaseExecutionPlan> jobWpStatusList =workPackageService.getNotExecutedTestCaseListByJobId(jobid);
					 log.info("getNotExecutedTestCaseListByWpId");
					 if(jobWpStatusList == null || jobWpStatusList.isEmpty() || jobWpStatusList.size() == 0){
						 log.info("getNotExecutedTestCaseListByJobId");
						 TestRunJob testRunJob = workPackageTestCaseExecutionPlan.getTestRunJob();
						 testRunJob.setTestRunStatus(IDPAConstants.JOB_COMPLETED);
						 workPackageService.updateTestRunJob(testRunJob);
						 if(testRunJob!=null){
								if(testRunJob.getTestRunJobId()!=null)
								log.info("Updating Testrun to Mongo ID "+testRunJob.getTestRunJobId());
								mongoDBService.addTestRunJobToMongoDB(testRunJob.getTestRunJobId());
							}
					 }					
						
						 else if(workPackageTestCaseExecutionPlan.getTestRunJob().getTestRunStatus()!=IDPAConstants.JOB_EXECUTING){
						TestRunJob	 testRunJob = workPackageTestCaseExecutionPlan.getTestRunJob();
						 testRunJob.setTestRunStatus(IDPAConstants.EXECUTION_STATUS);
						 workPackageService.updateTestRunJob(testRunJob);
							
							
						}				
					
					List<JsonTestCaseExecutionResult> tmpList = new ArrayList();
					tmpList.add(new JsonTestCaseExecutionResult(testCaseExecutionResult));
					jTableResponse = new JTableResponse("OK",tmpList ,1);					
					
		        } catch (Exception e) {
		            jTableResponse = new JTableResponse("ERROR","Error updating Test case Result!");
		            log.error("JSON ERROR", e);
		            e.printStackTrace();
		        }       
	        return jTableResponse;
	    }
		
		@RequestMapping(value="workpackage.demand.projection.add",method=RequestMethod.POST ,produces="application/json")
		public @ResponseBody JTableSingleResponse addSkillSpecificDemandProjectionForWorkPackage(HttpServletRequest request, @ModelAttribute JsonWorkPackageDemandProjection jsonWorkPackageDemandProjection, BindingResult result) {
			JTableSingleResponse jTableSingleResponse;
			UserList user = null;
			try {  
				if(request != null ){
					user = (UserList)request.getSession().getAttribute("USER");
				}
				log.info("DATE : -->"+jsonWorkPackageDemandProjection.getWorkDate());
				log.info("DATE Format : -->"+DateUtility.dateformatWithOutTime(jsonWorkPackageDemandProjection.getWorkDate()));
				WorkPackage workPackage = workPackageService.getWorkPackageById(jsonWorkPackageDemandProjection.getWorkPackageId());
				WorkShiftMaster shift = testFactoryManagementService.getWorkShiftsByshiftId(jsonWorkPackageDemandProjection.getShiftId());
				Skill skill = skillService.getBySkillId(jsonWorkPackageDemandProjection.getSkillId());
				UserRoleMaster userRole = userListService.getRoleById(jsonWorkPackageDemandProjection.getUserRoleId());
				WorkPackageDemandProjection workPackageDemandProjection = jsonWorkPackageDemandProjection.workPackageDemandProjection();
				workPackageDemandProjection.setWorkPackage(workPackage);
				workPackageDemandProjection.setWorkShiftMaster(shift);
				
				UserTypeMasterNew userType =new UserTypeMasterNew();
				userType.setUserTypeId(1);
				workPackageDemandProjection.setUserTypeMasterNew(userType);
				
				workPackageDemandProjection.setSkill(skill);
				workPackageDemandProjection.setUserRole(userRole);
				workPackageDemandProjection.setWorkDate(DateUtility.dateformatWithOutTime(jsonWorkPackageDemandProjection.getWorkDate()));
				workPackageDemandProjection.setResourceCount(jsonWorkPackageDemandProjection.getSkillandRoleBasedresourceCount());
				workPackageDemandProjection.setDemandRaisedOn(new Date());
				workPackageDemandProjection.setDemandRaisedByUser(user);
				Float resourceCount = workPackageDemandProjection.getResourceCount();
					if (resourceCount != null){
						if(resourceCount == 0 || resourceCount<0){
							jTableSingleResponse = new JTableSingleResponse("INFORMATION","Please provide a valid value for resource(s) count");
						}else{
							workPackageService.addWorkPackageDemandProjection(workPackageDemandProjection);	
							jTableSingleResponse = new JTableSingleResponse("OK",new JsonWorkPackageDemandProjection(workPackageDemandProjection));
						}
					}else{
						jTableSingleResponse = new JTableSingleResponse("INFORMATION","Please provide the resource(s) count.");
					}
		        } catch (Exception e) {
		            jTableSingleResponse = new JTableSingleResponse("ERROR","Error adding record!");
		            log.error("JSON ERROR", e);
		        }
	        return jTableSingleResponse;
	    }
		
		@RequestMapping(value="workpackage.demand.projection.list.by.date",method=RequestMethod.POST ,produces="application/json")
	    public @ResponseBody JTableResponse listWorkpackageDemandProjectionByDate(@RequestParam int workPackageId, @RequestParam int shiftId, @RequestParam String workDate) {
			log.info("inside workpackage.demand.projection.list.by.date ***************** workPackageId: "+workPackageId);
			log.info("inside workpackage.demand.projection.list.by.date ***************** workDate: "+workDate);
			JTableResponse jTableResponse = null;
			Date date = null;
			try {
					if(workPackageId<=0){
						workPackageId=1;
					}
					if(workDate != null){
						date = DateUtility.dateformatWithOutTime(workDate);
					}
					log.info("inside workpackage.demand.projection.list.by.date ***************** date: "+date);
					List<JsonWorkPackageDemandProjection> jsonWorkPackageDemandProjectionList = workPackageService.listWorkPackageDemandProjectionByDate(workPackageId, shiftId, date);
					
					if (jsonWorkPackageDemandProjectionList != null && jsonWorkPackageDemandProjectionList.size()>0) {
						log.info("successful");
						log.info("JsonWorkPackageDemandProjection size="+jsonWorkPackageDemandProjectionList.size());
						jTableResponse = new JTableResponse("OK", jsonWorkPackageDemandProjectionList, jsonWorkPackageDemandProjectionList.size());
					} else{
						jTableResponse = new JTableResponse("OK", null, 0);
					}
					jsonWorkPackageDemandProjectionList = null;
		        } catch (Exception e) {
		            jTableResponse = new JTableResponse("ERROR","Error getting Resource Demand for Workpackage!");
		            log.error("JSON ERROR", e);	            
		        }
	        return jTableResponse;
	    }
		
		
		@RequestMapping(value="workpackage.skill.demand.projection.list.by.date",method=RequestMethod.POST ,produces="application/json")
	    public @ResponseBody JTableResponse listWorkpackageSkillSpecificDemandProjectionByDate(@RequestParam int workPackageId, @RequestParam int shiftId, @RequestParam String workDate) {
			log.info("inside workpackage.demand.projection.list.by.date ***************** workPackageId: "+workPackageId);
			log.info("inside workpackage.demand.projection.list.by.date ***************** workDate: "+workDate);
			JTableResponse jTableResponse = null;
			Date date = null;
			try {
					if(workPackageId<=0){
						workPackageId=1;
					}
					if(workDate != null){
						date = DateUtility.dateformatWithOutTime(workDate);
					}
					log.info("inside workpackage.demand.projection.list.by.date ***************** date: "+date);
					List<JsonWorkPackageDemandProjection> jsonWorkPackageDemandProjectionList = workPackageService.listWorkPackageDemandProjectionByDate(workPackageId, shiftId, date);
					
					if (jsonWorkPackageDemandProjectionList != null && jsonWorkPackageDemandProjectionList.size()>0) {
						log.info("successful");
						log.info("JsonWorkPackageDemandProjection size="+jsonWorkPackageDemandProjectionList.size());
						jTableResponse = new JTableResponse("OK", jsonWorkPackageDemandProjectionList, jsonWorkPackageDemandProjectionList.size());
					} else{
						jTableResponse = new JTableResponse("OK", null, 0);
					}
					jsonWorkPackageDemandProjectionList = null;
		        } catch (Exception e) {
		            jTableResponse = new JTableResponse("ERROR","Error getting Resource Demand for Workpackage!");
		            log.error("JSON ERROR", e);	            
		        }
	        return jTableResponse;
	    }
		
		@RequestMapping(value="workpackage.demand.projection.delete",method=RequestMethod.POST ,produces="application/json")
	    public @ResponseBody JTableResponse deleteSkillSpecificDemandProjectionForWorkPackage(@RequestParam int wpDemandProjectionId) {
			JTableResponse jTableResponse;
			try {
		            workPackageService.deleteWorkPackageDemandProjection(wpDemandProjectionId);
		            jTableResponse = new JTableResponse("OK");
		        } catch (Exception e) {
		            jTableResponse = new JTableResponse("ERROR","Not able to delete the WorkPackageDemandProjection.");
		            log.error("JSON ERROR", e);
		        }
			return jTableResponse;
	    }
		
		
		@RequestMapping(value="workpackage.demand.projection.skill.update",method=RequestMethod.POST ,produces="application/json")
		public @ResponseBody JTableResponse updateSkillSpecificDemandProjectionForWorkPackage(@ModelAttribute JsonWorkPackageDemandProjection jsonWorkPackageDemandProjection, BindingResult result) {
			JTableResponse jTableResponse = null;
			if(result.hasErrors()){
				jTableResponse = new JTableResponse("ERROR","Invalid Form!"); 
			}
			try {	
					log.info("In update---->jsonWorkPackageDemandProjection DATE ---->"+jsonWorkPackageDemandProjection.getWorkDate());
					log.info("In update---->jsonWorkPackageDemandProjection Demand Raised DATE ---->"+jsonWorkPackageDemandProjection.getDemandRaisedOnDate());
					WorkPackageDemandProjection wpDemandProjectionFromUI = jsonWorkPackageDemandProjection.workPackageDemandProjection();	
					wpDemandProjectionFromUI.setWorkDate(DateUtility.dateformatWithOutTime(jsonWorkPackageDemandProjection.getWorkDate()));
				
					if(jsonWorkPackageDemandProjection.getWorkPackageId() != 0){				
						wpDemandProjectionFromUI.setWorkPackage(workPackageService.getWorkPackageById(jsonWorkPackageDemandProjection.getWorkPackageId()));
					}
					
					if(jsonWorkPackageDemandProjection.getShiftId() != 0){				
						wpDemandProjectionFromUI.setWorkShiftMaster(testFactoryManagementService.getWorkShiftsByshiftId(jsonWorkPackageDemandProjection.getShiftId()));
					}
					
					if(jsonWorkPackageDemandProjection.getDemandRaisedByUserId() != 0 || jsonWorkPackageDemandProjection.getDemandRaisedByUserId() != null){
						log.info("SET Demand Raised by user on update");
						wpDemandProjectionFromUI.setDemandRaisedByUser(userListService.getUserListById(jsonWorkPackageDemandProjection.getDemandRaisedByUserId()));
					}
					wpDemandProjectionFromUI.setDemandRaisedOn(DateUtility.toDateInSec1(jsonWorkPackageDemandProjection.getDemandRaisedOnDate()));
					wpDemandProjectionFromUI.setResourceCount(jsonWorkPackageDemandProjection.getSkillandRoleBasedresourceCount());
					
					workPackageService.updateWorkPackageDemandProjection(wpDemandProjectionFromUI);		
					List<JsonWorkPackageDemandProjection> tmpList = new ArrayList();
					tmpList.add(jsonWorkPackageDemandProjection);
		            jTableResponse = new JTableResponse("OK",tmpList ,1);            
				
		        } catch (Exception e) {
		            jTableResponse = new JTableResponse("ERROR","Error updating Work Package Demand Projection!");
		            log.error("JSON ERROR", e);
		            e.printStackTrace();
		        }
			return jTableResponse;
	    }
		@RequestMapping(value="testcase.defect.list.byWorkPackageId",method=RequestMethod.POST ,produces="application/json")
	    public @ResponseBody JTableResponse getWorkpackageTestcaseExecutionTestcaseDefectDetailsByWorkPackgeId(@RequestParam Integer workPackageId, HttpServletRequest req, int jtStartIndex, int jtPageSize) {
			log.debug("testcase.defect.list.byWorkPackageId");
			UserList user=(UserList)req.getSession().getAttribute("USER");
			List<TestExecutionResultBugList> defectList=null;
			JTableResponse jTableResponse = null;
			List<JsonTestExecutionResultBugList> jsonTestExecutionResultBugList=new ArrayList<JsonTestExecutionResultBugList>();
			JsonTestExecutionResultBugList jsonTestExecutionResultBug = new JsonTestExecutionResultBugList();
			Set<WorkPackageTestCaseExecutionPlan> workPackageTestCaseExecutionPlanSet =new HashSet<WorkPackageTestCaseExecutionPlan>();
				try {
					if(workPackageId!=null && workPackageId!=0){
					WorkPackage workPackage =workPackageService.getWorkPackageById(workPackageId);
					if(workPackage != null) {
						workPackageTestCaseExecutionPlanSet = workPackage.getWorkPackageTestCaseExecutionPlan();
					}
							
				if(workPackageTestCaseExecutionPlanSet != null && workPackageTestCaseExecutionPlanSet.size()!=0){
				for(WorkPackageTestCaseExecutionPlan workPackageTestCaseExecutionPlan:workPackageTestCaseExecutionPlanSet){
					TestCaseExecutionResult testCaseExecutionResult=workPackageTestCaseExecutionPlan.getTestCaseExecutionResult();
					if(testCaseExecutionResult!=null){
					defectList	=workPackageService.listDefectsByTestcaseExecutionPlanId(testCaseExecutionResult.getTestCaseExecutionResultId(),jtStartIndex, jtPageSize);
					if (defectList.size()!=0) {
					for(TestExecutionResultBugList defect: defectList){
						jsonTestExecutionResultBug = new JsonTestExecutionResultBugList(defect);
						jsonTestExecutionResultBug.setRunConfiguration(workPackageTestCaseExecutionPlan.getRunConfiguration().getRunconfiguration().getRunconfigName());
						jsonTestExecutionResultBugList.add(jsonTestExecutionResultBug);
					}
					defectList=null;
					}
				}
				}
					}	
				
					}
					if(jsonTestExecutionResultBugList.size()!=0) {
						jTableResponse = new JTableResponse("OK", jsonTestExecutionResultBugList,jsonTestExecutionResultBugList.size() );
						//defectList = null;
					}else{    jTableResponse = new JTableResponse("OK", jsonTestExecutionResultBugList, 0);  }
		        } catch (Exception e) {
		            jTableResponse = new JTableResponse("ERROR","Error fetching records!");
		            log.error("JSON ERROR", e);	            
		        }
	        return jTableResponse;
	    }	
		@RequestMapping(value="administration.workpackage.environment",method=RequestMethod.POST ,produces="application/json")
		public  @ResponseBody JTableResponse  mapWorkpackageWithEnvironment(HttpServletRequest req,@RequestParam Integer workpackageId,@RequestParam Integer productId) throws Exception {
			JTableResponse jTableResponse = null;
			String userDisplayName="HCL";
			List<Environment> environmentSourceList = new ArrayList<Environment>();
			Set<Environment> environmentDestinationList = new HashSet<Environment>(0);
			
			List<JsonEnvironment> jsonEnvironmentList = new ArrayList<JsonEnvironment>();
			
			log.debug("inside navigation Controller and mapWorkpackageWithEnvironment");
			try{

				environmentSourceList=productListService.getEnvironmentListByProductId(productId);
				environmentDestinationList=null;//workPackageService.getEnvironmentMappedToWorkpackage(workpackageId);
				
				for(Environment env:environmentDestinationList){
					if(environmentSourceList.contains(env)){
						environmentSourceList.remove(env);
					}
				}
					
				
				for(Environment environment:environmentDestinationList){
					jsonEnvironmentList.add(new JsonEnvironment(environment,"Selected"));
				}
				
				for(Environment environment:environmentSourceList){
					jsonEnvironmentList.add(new JsonEnvironment(environment,"NotSelected"));
				}
			jTableResponse = new JTableResponse("OK", jsonEnvironmentList,jsonEnvironmentList.size());
			
	        } catch (Exception e) {
	            jTableResponse = new JTableResponse("ERROR","Error fetching records!");
	            log.error("JSON ERROR", e);
	        }	     
		 return jTableResponse;
		}
		
		@RequestMapping(value="testcase.results.update.evidence",method=RequestMethod.POST ,produces="text/plain")
		public @ResponseBody JTableResponse updateTestcaseResultImage(HttpServletRequest request,@RequestParam int tcerId,@RequestParam String modifiedfField,@RequestParam String modifiedValue ,@RequestParam String type,@RequestParam String executionTime) {
			JTableResponse jTableResponse =null;
			Long executionSeconds=null;
			try {	
				List<JsonTestCaseExecutionResult> jsonTestCaseExecutionResList = new ArrayList<JsonTestCaseExecutionResult>();
				List<JsonEvidence> jsonEvidenceList = new ArrayList<JsonEvidence>();
				String serverFolderPath = CommonUtility.getCatalinaPath() + evidence_Folder;

				MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
				CommonsMultipartFile multipartFile = null;
				String fileName = "";
				String description = "";
				
				if(multipartRequest.getParameter("description")!=null)
					description = (String)multipartRequest.getParameter("description");
				tcerId=Integer.valueOf(multipartRequest.getParameter("tcerId"));
				Integer evidenceTypeId=0;
				if(multipartRequest.getParameter("evidenceType")!=null)
					evidenceTypeId = Integer.valueOf(multipartRequest.getParameter("evidenceType"));
				log.info("evidenceTypeId:"+evidenceTypeId);
				TestCaseExecutionResult testCaseExecutionResult  =workPackageService.getTestCaseExecutionResultByID(tcerId);
				TestStepExecutionResult testStepExecutionResult=null;
				Iterator<String> iterator = multipartRequest.getFileNames();
				while (iterator.hasNext()) {
					String key = (String) iterator.next();
					multipartFile = (CommonsMultipartFile) multipartRequest.getFile(key);
					fileName = multipartFile.getOriginalFilename();
					long size=multipartFile.getSize();
					String Path = multipartFile.getName();
					
					
					String extn[] = {".jpg",".gif",".png",".docx",".txt",".xls",".xlsx",".mp4",".3gp",".jpeg"}; 
					if(size > 20971520){
						jTableResponse = new JTableResponse("ERROR","Evidence size should not exceed 2Mb!");
						return jTableResponse;
					}else if (!Arrays.asList(extn).contains(fileName.substring(fileName.lastIndexOf(".")).toLowerCase())) {
					//	
						jTableResponse = new JTableResponse("ERROR","Please upload an Evidence file in .jpg/.gif/.png format!");
							return  jTableResponse;
					}
					Evidence evidence=new Evidence();

					EntityMaster entityMaster= null;
					WorkPackageTestCaseExecutionPlan workPackageTestCaseExecutionPlan =null;

					if(type.equalsIgnoreCase("testcase")){
						entityMaster=workPackageService.getEntityMasterById(IDPAConstants.ENTITY_TEST_CASE_EVIDENCE_ID);
							 workPackageTestCaseExecutionPlan = workPackageService.getWorkpackageTestcaseExecutionPlanById(tcerId);
							 if(executionTime!=null && executionTime!=""){
									executionSeconds=CommonUtility.getSeconds(executionTime);
									if(executionSeconds!=0){
										testCaseExecutionResult.setExecutionTime(executionSeconds);
										workPackageService.updateTestCaseResults(testCaseExecutionResult);
									}
								}
								evidence.setEntityvalue(tcerId);

					}else if(type.equalsIgnoreCase("teststep")){
						evidence.setDescription(description);
						entityMaster=workPackageService.getEntityMasterById(IDPAConstants.ENTITY_TEST_STEP_EVIDENCE_ID);
						testStepExecutionResult=workPackageService.getTestStepExecutionResultById(tcerId);
						evidence.setEntityvalue(testStepExecutionResult.getTeststepexecutionresultid());
						workPackageTestCaseExecutionPlan=testStepExecutionResult.getTestCaseExecutionResult().getWorkPackageTestCaseExecutionPlan();
					} 
					
					
					
					String extension=fileName.substring(fileName.lastIndexOf(".")).toLowerCase();
					fileName=CommonUtility.getEvidenceFileName(workPackageTestCaseExecutionPlan,extension);
					evidence.setEvidencename(fileName);
					EvidenceType evidenceType= workPackageService.getEvidenceTypeById(evidenceTypeId);
					evidence.setEvidenceType(evidenceType);
					InputStream content = multipartFile.getInputStream();
					File filePath = new File(serverFolderPath);
					
					if (!filePath.isDirectory()) {
						FileUtils.forceMkdir(filePath);
					}
					
					serverFolderPath=serverFolderPath+"\\"+workPackageTestCaseExecutionPlan.getTestRunJob().getTestRunJobId();
					 filePath = new File(serverFolderPath);
					 if (!filePath.exists()) {
							filePath.mkdirs();
					}
					 String evidenceTypeName="";
					 String tempServerFolderPath=serverFolderPath;
					 if(evidenceTypeId==1){
						serverFolderPath=tempServerFolderPath+"\\"+IDPAConstants.EVIDENCE_SCREENSHOT;
						evidenceTypeName=IDPAConstants.EVIDENCE_SCREENSHOT;
						 filePath = new File(serverFolderPath);
						 if (!filePath.exists()) {
								filePath.mkdirs();
						}
				 }else  if(evidenceTypeId==2){
						serverFolderPath=tempServerFolderPath+"\\"+IDPAConstants.EVIDENCE_VIDEO;
						evidenceTypeName=IDPAConstants.EVIDENCE_VIDEO;
						 filePath = new File(serverFolderPath);
						 if (!filePath.exists()) {
								filePath.mkdirs();
						}
				 }else  if(evidenceTypeId==3){
						serverFolderPath=tempServerFolderPath+"\\"+IDPAConstants.EVIDENCE_LOG;
						evidenceTypeName=IDPAConstants.EVIDENCE_LOG;
						 filePath = new File(serverFolderPath);
						 if (!filePath.exists()) {
								filePath.mkdirs();
						}
					 }else  if(evidenceTypeId==4){
							serverFolderPath=tempServerFolderPath+"\\"+IDPAConstants.EVIDENCE_OTHER;
							evidenceTypeName=IDPAConstants.EVIDENCE_OTHER;
							 filePath = new File(serverFolderPath);
							 if (!filePath.exists()) {
									filePath.mkdirs();
							}
						 }
					 else  if(evidenceTypeId==5){
							serverFolderPath=tempServerFolderPath+"\\"+IDPAConstants.EVIDENCE_REPORT;
							evidenceTypeName=IDPAConstants.EVIDENCE_REPORT;
							 filePath = new File(serverFolderPath);
							 if (!filePath.exists()) {
									filePath.mkdirs();
							}
						 } else  {
							 serverFolderPath=tempServerFolderPath+"\\"+IDPAConstants.EVIDENCE_SCREENSHOT;
								evidenceTypeName=IDPAConstants.EVIDENCE_SCREENSHOT;
								 filePath = new File(serverFolderPath);
								 if (!filePath.exists()) {
										filePath.mkdirs();
								}
							 }
					File image = new File(serverFolderPath + "\\"+ fileName);
					

					CommonUtility.copyInputStreamToFile(content, image);
					
					

					evidence.setEntityMaster(entityMaster);
					evidence.setFiletype(extension);
					evidence.setFileuri(evidence_Folder+"\\"+workPackageTestCaseExecutionPlan.getTestRunJob().getTestRunJobId()+"\\"+evidenceTypeName+"\\"+fileName);
					evidence.setSize(size);
					
					workPackageService.addEvidence(evidence);
					
					if(workPackageTestCaseExecutionPlan!=null &&  workPackageTestCaseExecutionPlan.getWorkPackage()!=null &&
							workPackageTestCaseExecutionPlan.getWorkPackage().getWorkFlowEvent()!=null && workPackageTestCaseExecutionPlan.getWorkPackage().getWorkFlowEvent().getWorkFlow()!=null &&
							workPackageTestCaseExecutionPlan.getWorkPackage().getWorkFlowEvent().getWorkFlow().getStageId()!=IDPAConstants.WORKFLOW_STAGE_ID_EXECUTION && workPackageTestCaseExecutionPlan.getWorkPackage().getWorkFlowEvent().getWorkFlow().getStageValue()<30){
						WorkPackage workPackage = workPackageService.getWorkPackageById(workPackageTestCaseExecutionPlan.getWorkPackage().getWorkPackageId());
						WorkFlow workFlow=workPackageService.getWorkFlowByEntityIdStageId(IDPAConstants.WORKPACKAGE_ENTITY_ID, IDPAConstants.WORKFLOW_STAGE_ID_EXECUTION);
						WorkFlowEvent workFlowEvent = new WorkFlowEvent();
						workFlowEvent.setEventDate(DateUtility.getCurrentTime());
						workFlowEvent.setRemarks("Executing Workapckage :"+workPackage.getName());
						UserList user= userListService.getUserByLoginId(IDPAConstants.ADMIN_LOGIN_ID);
						workFlowEvent.setUser(user);
						workFlowEvent.setWorkFlow(workFlow);
						workPackage.setWorkFlowEvent(workFlowEvent);
						workPackageService.addWorkFlowEvent(workFlowEvent);
						workPackageService.updateWorkPackage(workPackage);
					}
				
					if(type.equalsIgnoreCase("testcase")){
						jsonTestCaseExecutionResList.add(new JsonTestCaseExecutionResult(testCaseExecutionResult));
						
					}else if(type.equalsIgnoreCase("teststep")){
						jsonEvidenceList.add(new JsonEvidence(evidence));
						
					} 
									
				}
				if(jsonTestCaseExecutionResList!=null && jsonTestCaseExecutionResList.size()!=0){
					jTableResponse = new JTableResponse("OK",jsonTestCaseExecutionResList ,jsonTestCaseExecutionResList.size());	
					
				}else if(jsonEvidenceList!=null && jsonEvidenceList.size()!=0){
					jTableResponse = new JTableResponse("OK",jsonEvidenceList ,jsonEvidenceList.size());
					
				}else{
					jTableResponse = new JTableResponse("OK");
				}
				
		        } catch (Exception e) {
		            jTableResponse = new JTableResponse("ERROR","Error updating Test case Result!");
		            log.error("JSON ERROR", e);
		            e.printStackTrace();
		        }       
	        return jTableResponse;
	    }
		@RequestMapping(value="testcase.list.eveidence",method=RequestMethod.POST ,produces="application/json")
	    public @ResponseBody JTableResponse testcaseListByEvidence(@RequestParam Integer tcerId,@RequestParam String type) {
			JTableResponse jTableResponse = null;
			Date date = null;
			try {		        
				List<Evidence> evidenList = workPackageService.testcaseListByEvidence(tcerId,type);
				List<JsonEvidence> jsonEvidence = new ArrayList<JsonEvidence>();
				
				for(Evidence ev: evidenList){
					jsonEvidence.add(new JsonEvidence(ev));
				}				
	            jTableResponse = new JTableResponse("OK", jsonEvidence,jsonEvidence.size());     
		        } catch (Exception e) {
		            jTableResponse = new JTableResponse("ERROR","Error fetching records!");
		            log.error("JSON ERROR", e);
		        }
	        return jTableResponse;
	    }
		@RequestMapping(value="administration.evidence.delete",method=RequestMethod.POST ,produces="application/json")
	    public @ResponseBody JTableResponse deleteEvidences(@RequestParam int evidenceId) {			
			JTableResponse jTableResponse;
			Evidence evidence=null;
			try {
				log.info("evidenceId==>"+evidenceId);
				evidence=workPackageService.getEvidenceById(evidenceId);
				
				/* for deleting evidence from folder*/
				String url=	evidence.getFileuri();
				String serverFolderPath = CommonUtility.getCatalinaPath()+url;
				serverFolderPath=serverFolderPath.replace("\\\\", "\\");
				   File file = new File(serverFolderPath);
				   boolean blnDeleted = file.delete();
				log.info("serverFolderPath==>"+serverFolderPath);
				workPackageService.deleteEvidence(evidence);
			
		         jTableResponse = new JTableResponse("OK","Evidence Deleted");
		        } catch (Exception e) {
		            jTableResponse = new JTableResponse("ERROR","Error deleting Evidence!");
		            e.printStackTrace();
		            log.error("JSON ERROR", e);
		        }
	        return jTableResponse;
	    }
		@RequestMapping(value="workpackage.testcase.plan.resultsByWorkPackage",method=RequestMethod.POST ,produces="application/json")
	    public @ResponseBody JTableResponse listWorkpackageTestCaseExecutionPlansByWorkPackageId(@RequestParam int workPackageId, @RequestParam int jtStartIndex, @RequestParam int jtPageSize,HttpServletRequest req) {
			log.debug("inside workpackage.testcase.plan.resultsByWorkPackage");
			JTableResponse jTableResponse = null;
				try {
					if(workPackageId<=0){
						workPackageId=1;
					}
					
					List<WorkPackageTestCaseExecutionPlan> workPackageTestCaseExecutionPlanList=workPackageService.listWorkPackageTestCasesExecutionPlanByWorkPackageId(workPackageId);
					
					List<JsonWorkPackageTestCaseExecutionPlanForTester> jsonWorkPackageTestCaseExecutionPlans=new ArrayList<JsonWorkPackageTestCaseExecutionPlanForTester>();
					
					if (workPackageTestCaseExecutionPlanList == null || workPackageTestCaseExecutionPlanList.isEmpty()) {
						
						//TODO : Initialize the workpackage with the test cases for the product
						log.info("No workpackage testcase execution plan for workpackage id:"+workPackageId  );
						jTableResponse = new JTableResponse("OK", jsonWorkPackageTestCaseExecutionPlans, 0);
					} else {
						for(WorkPackageTestCaseExecutionPlan workPackageTestCaseExecutionPlan: workPackageTestCaseExecutionPlanList){
							jsonWorkPackageTestCaseExecutionPlans.add(new JsonWorkPackageTestCaseExecutionPlanForTester(workPackageTestCaseExecutionPlan));
						}
				        
						jTableResponse = new JTableResponse("OK", jsonWorkPackageTestCaseExecutionPlans, jsonWorkPackageTestCaseExecutionPlans.size());
						workPackageTestCaseExecutionPlanList = null;
					}
		        } catch (Exception e) {
		            jTableResponse = new JTableResponse("ERROR","Error fetching records!");
		            log.error("JSON ERROR", e);	            
		        }
	        return jTableResponse;
	    }	
		
		@RequestMapping(value="teststep.plan.list",method=RequestMethod.POST ,produces="application/json")
		public @ResponseBody JTableResponse listTestStepPlan(@RequestParam Integer testCaseId,@RequestParam Integer tcerId) {
			log.debug("inside teststep.plan.list");
			JTableResponse jTableResponse;
				try {
				
					List<TestStepExecutionResult> testStepExecutionResultList=workPackageService.listTestStepPlan(testCaseId,tcerId);
					
					List<JsonTestStepExecutionResult> jsonTestStepExecutionResultList=new ArrayList<JsonTestStepExecutionResult>();
					
					for(TestStepExecutionResult testStepExecutionResult: testStepExecutionResultList){
						jsonTestStepExecutionResultList.add(new JsonTestStepExecutionResult(testStepExecutionResult));
						
					}
					jTableResponse = new JTableResponse("OK", jsonTestStepExecutionResultList,jsonTestStepExecutionResultList.size());
			           
		        } catch (Exception e) {
		            jTableResponse = new JTableResponse("ERROR","Error fetching records!");
		            log.error("JSON ERROR", e);
		        }
		        
	        return jTableResponse;
	    }
		
		@RequestMapping(value="teststep.plan.update",method=RequestMethod.POST ,produces="application/json")
		public @ResponseBody JTableResponse updateTestStepPlan(@ModelAttribute JsonTestStepExecutionResult jsonTestStepExecutionResult, BindingResult result) {
			JTableResponse jTableResponse;
			if(result.hasErrors()){
				jTableResponse = new JTableResponse("ERROR","Invalid Form!"); 
			}			
			try {	
		
				log.info("TestCaseId---> :"+jsonTestStepExecutionResult.getTestcaseId());
				log.info("jsonTestStepExecutionResult.getTestCaseExecutionResultId()---> :"+jsonTestStepExecutionResult.getTestCaseExecutionResultId());
				log.info("jsonTestStepExecutionResult.getTestStepExecutionResult()---> :"+jsonTestStepExecutionResult.getTestStepExecutionResult().getTeststepexecutionresultid());
				
				TestStepExecutionResult testStepExecutionResult =workPackageService.getTestStepExecutionResultById(jsonTestStepExecutionResult.getTeststepexecutionresultid());
				
				
				TestCaseList testCaseList = testCaseService.getTestCaseById(jsonTestStepExecutionResult.getTestStepExecutionResult().getTestcase().getTestCaseId());
				TestCaseExecutionResult testCaseExecutionResult = workPackageService.getTestCaseExecutionResultByID(jsonTestStepExecutionResult.getTestCaseExecutionResultId());
				
				testStepExecutionResult.setTestcase(testCaseList);
				testStepExecutionResult.setTestCaseExecutionResult(testCaseExecutionResult);
				testStepExecutionResult.setComments(jsonTestStepExecutionResult.getComments());
				testStepExecutionResult.setObservedOutput(jsonTestStepExecutionResult.getObservedOutput());
				testStepExecutionResult.setResult(jsonTestStepExecutionResult.getResult());
				
				workPackageService.updateTestStepExecutionResult(testStepExecutionResult);
				
					List<JsonTestStepExecutionResult> tmpList = new ArrayList();
					tmpList.add(new JsonTestStepExecutionResult(testStepExecutionResult));
					jTableResponse = new JTableResponse("OK",tmpList ,1);					
					
		        } catch (Exception e) {
		            jTableResponse = new JTableResponse("ERROR","Error updating record!");
		            log.error("JSON ERROR", e);
		            e.printStackTrace();
		        }       
	        return jTableResponse;
	    }
		
		@RequestMapping(value="testcase.list.evidence.add",method=RequestMethod.POST ,produces="application/json")
	    public @ResponseBody JTableSingleResponse addEvidenceForTestStep(@ModelAttribute JsonEvidence jsonEvidence, BindingResult result,HttpServletRequest req,@RequestParam String type) {			
			JTableSingleResponse jTableSingleResponse;
			if(result.hasErrors()){
				jTableSingleResponse = new JTableSingleResponse("ERROR","Invalid form!"); 
			}			
			try {
				UserList user=(UserList)req.getSession().getAttribute("USER");
				Evidence evidence = jsonEvidence.getEvidence();
				
				EntityMaster entityMaster= null;
				
				if(type.equalsIgnoreCase("testcase")){
						workPackageService.getEntityMasterById(IDPAConstants.ENTITY_TEST_CASE_EVIDENCE_ID);
				}else if(type.equalsIgnoreCase("teststep")){
					workPackageService.getEntityMasterById(IDPAConstants.ENTITY_TEST_STEP_EVIDENCE_ID);
				} 
				evidence.setEntityMaster(entityMaster);
				workPackageService.addEvidence(evidence);		

				
		            jTableSingleResponse = new JTableSingleResponse("OK",new JsonEvidence(evidence));
		        } catch (Exception e) {
		            jTableSingleResponse = new JTableSingleResponse("ERROR","Error adding new WorkPackage!");
		            log.error("JSON ERROR", e);
		        }		        
	        return jTableSingleResponse;			
	    }
		
		@RequestMapping(value="administration.workpackage.update.summary",method=RequestMethod.POST ,produces="application/json")
		public @ResponseBody JTableResponse updateWorkpackageSummary(HttpServletRequest request,@RequestParam Integer workpackageId,@RequestParam String modifiedfField,@RequestParam String modifiedValue ) {
			JTableResponse jTableResponse;
			String remarks = "";
			
			try {	
				Locale locale=(Locale)request.getSession().getAttribute("locale");
				String localeId=(String)request.getSession().getAttribute("localeId");
				UserList user=(UserList)request.getSession().getAttribute("USER");
				String modifiedfFieldLabel = "";
				String modifiedValueLabel = "";
				String modifiedValueNewLabel = "";
				TestFactory testFactory = null;
				ProductMaster productMaster = null;
				
				WorkPackage workPackage  =workPackageService.getWorkPackageById(workpackageId);
				productMaster = workPackage.getProductBuild().getProductVersion().getProductMaster();
				testFactory = productMaster.getTestFactory();				
				Date ProductBuildadate=workPackage.getProductBuild().getBuildDate();
				if(modifiedfField.equalsIgnoreCase("workPackageName")){
					modifiedfFieldLabel = modifiedfField;
					modifiedValueLabel = workPackage.getName();//old value
					workPackage.setName(modifiedValue);
					modifiedValueNewLabel = modifiedValue;	//new value				
				}else if(modifiedfField.equalsIgnoreCase("description")){
					modifiedfFieldLabel = modifiedfField;
					modifiedValueLabel = workPackage.getDescription();//old value
					workPackage.setDescription(modifiedValue);					
					modifiedValueNewLabel = modifiedValue; //new value	
				}else if(modifiedfField.equalsIgnoreCase("start")){
					 String date[]=modifiedValue.split("~");
					 Date actualStartDate=DateUtility.dateformatWithOutTime(date[0]);
					Date actualEndDate=DateUtility.dateformatWithOutTime(date[1]);
					modifiedfFieldLabel = modifiedfField;
					modifiedValueLabel = workPackage.getActualEndDate().toString();	//old value			
					workPackage.setActualStartDate(actualStartDate);
					workPackage.setActualEndDate(actualEndDate);
					modifiedValueNewLabel = actualStartDate.toString(); //new value
					
				}
				else if(modifiedfField.equalsIgnoreCase("plannedStartDate")){
					if(modifiedValue!=null){
                        String date[]=modifiedValue.split("~");
						modifiedfFieldLabel = modifiedfField;
                        modifiedValueLabel = workPackage.getPlannedEndDate().toString(); //old Value                        
						Date plannedstartDate=DateUtility.dateformatWithOutTime(date[0]);
						Date plannedEndDate=DateUtility.dateformatWithOutTime(date[1]);
						workPackage.setPlannedStartDate(plannedstartDate);
						workPackage.setPlannedEndDate(plannedEndDate);
						modifiedValueNewLabel = plannedstartDate.toString(); // new value						
						if(ProductBuildadate.compareTo(plannedstartDate)>0)
						{
							jTableResponse = new JTableResponse("ERROR"," Warning :WorkPackage Planned Date should be Greaterthan ProductBuild date! Build date= ("+ProductBuildadate+")");
					        return jTableResponse;
						}
					}
				}
				else if(modifiedfField.equalsIgnoreCase("dropdown")){
					//work here, pending
					modifiedfFieldLabel = "workfloweventid";
					WorkFlow workFlow =workPackageService.getWorkFlowByEntityIdWorkFlowId(IDPAConstants.WORKPACKAGE_ENTITY_ID, Integer.parseInt(modifiedValue)); 
					if(modifiedValue!=null && modifiedValue.equals("11")){
						workPackage.setStatus(0);
						workPackage.setIsActive(0);
					}
					if(workPackage.getWorkFlowEvent().getWorkFlow().getStageValue()<workFlow.getStageValue()){
						modifiedValueLabel = workPackage.getWorkFlowEvent().getWorkFlow().getStageName();//Old value
						WorkFlowEvent workFlowEvent = new WorkFlowEvent();
						workFlowEvent.setEventDate(DateUtility.getCurrentTime());
						workFlowEvent.setRemarks(workFlow.getStageName()+"  Workapckage :"+workPackage.getName());
						
						//UserList user=(UserList)request.getSession().getAttribute("USER");
						workFlowEvent.setUser(user);
						workFlowEvent.setWorkFlow(workFlow);
						workPackage.setWorkFlowEvent(workFlowEvent);
						modifiedValueNewLabel = workFlow.getStageName(); //New value
						workPackageService.addWorkFlowEvent(workFlowEvent);
					}else{
						jTableResponse = new JTableResponse("ERROR","Error updating Workpackage Status backwards Not supported!");
				        return jTableResponse;
 
					}
				}else if(modifiedfField.equalsIgnoreCase("iterationNo")){
					modifiedfFieldLabel = modifiedfField;
                    modifiedValueLabel = workPackage.getIterationNumber().toString(); //old Value
					workPackage.setIterationNumber(Integer.parseInt(modifiedValue));
					modifiedValueNewLabel = modifiedValue; //new Value
				}else if(modifiedfField.equalsIgnoreCase("lifecyclephase")){
					modifiedfFieldLabel = modifiedfField;
					modifiedValueLabel = workPackage.getLifeCyclePhase().getName(); //Old Value
					LifeCyclePhase lifeCyclePhase = new LifeCyclePhase();
					lifeCyclePhase.setLifeCyclePhaseId(Integer.parseInt(modifiedValue));
					workPackage.setLifeCyclePhase(lifeCyclePhase);
					LifeCyclePhase lifeCycle = riskListService.getLifeCyclePhaseBylifeCyclePhaseId(lifeCyclePhase.getLifeCyclePhaseId());
					modifiedValueNewLabel = lifeCycle.getName();//New value
				}
				
					if(workPackage.getCreateDate()!=null){
						workPackage.setCreateDate(workPackage.getCreateDate());
					}else{
						workPackage.setCreateDate(DateUtility.getCurrentTime());
					}
					workPackage.setModifiedDate(DateUtility.getCurrentTime());
				
					workPackageService.updateWorkPackage(workPackage);
					if(workPackage!=null && workPackage.getWorkPackageId()!=null){
						mongoDBService.addWorkPackage(workPackage.getWorkPackageId());
						int userId=user.getUserId();
						UserList userObj = userListService.getUserListById(userId);	
						remarks = "TestFactory :"+testFactory.getTestFactoryName()+", Product :"+productMaster.getProductName()+", WorkPackage :"+workPackage.getName();
						eventsService.addEntityChangedEvent(IDPAConstants.ENTITY_WORK_PACKAGE, workPackage.getWorkPackageId(), workPackage.getName(),
								modifiedfField, modifiedfFieldLabel,
								modifiedValueLabel, modifiedValueNewLabel, user, remarks);
					}
					List<JsonWorkPackage> tmpList = new ArrayList();
					tmpList.add(new JsonWorkPackage(workPackage));
					jTableResponse = new JTableResponse("OK",tmpList ,1);					
					
		        } catch (Exception e) {
		            jTableResponse = new JTableResponse("ERROR","Error updating Workpackage!");
		            log.error("JSON ERROR", e);
		            e.printStackTrace();
		        }       
	        return jTableResponse;
	    }

		@RequestMapping(value="workpackage.testcase.list.workpackageId",method=RequestMethod.POST ,produces="application/json")
		public @ResponseBody JTableResponse listAllTestcaseOfWorkpackages(@RequestParam Integer workPackageId) {
			log.debug("inside workpackage.testcase.list.workpackageId");
			JTableResponse jTableResponse;
			List<JsonTestCaseList> jsonTestCaseList=new ArrayList<JsonTestCaseList>();
			try {
				
					WorkPackage workPackage=workPackageService.getWorkPackageById(workPackageId);
					
					Set<TestCaseList> testCaseList = workPackage.getTestcaseList();
						for(TestCaseList testCase: testCaseList){
							jsonTestCaseList.add(new JsonTestCaseList(testCase));
							
						}
						jTableResponse = new JTableResponse("OK", jsonTestCaseList,jsonTestCaseList.size());
		        } catch (Exception e) {
		            jTableResponse = new JTableResponse("ERROR","Error fetching records!");
		            log.error("JSON ERROR", e);
		        }
		        
	        return jTableResponse;
	    }
		
		@RequestMapping(value="administration.workpackage.testcase.executionPriority",method=RequestMethod.POST ,produces="application/json")
		public @ResponseBody JTableResponse updateWorkpackageTestcaseExecutionPriority(HttpServletRequest request,@RequestParam String executionPriority) {
			JTableResponse jTableResponse;
			Integer executionPriorityId=0;
			Integer wptceId=0;
			try {	
				if(executionPriority!=null && !executionPriority.equals("undefined")){
					executionPriorityId=Integer.parseInt(executionPriority.substring(0,executionPriority.indexOf("~")));
					wptceId=Integer.parseInt(executionPriority.substring(executionPriority.indexOf("~")+1));
				}else if(executionPriority.equals("undefined")) {
					jTableResponse = new JTableResponse("ERROR","Error updating executionPriority !");
			        return jTableResponse;

				}else{
					
					jTableResponse = new JTableResponse("ERROR","Error updating executionPriority !");
			        return jTableResponse;

				}
				ExecutionPriority executionPriorityEntity = workPackageService.getExecutionPriorityById(executionPriorityId);
				
				WorkPackageTestCaseExecutionPlan workPackageTestCaseExecutionPlan=workPackageService.getWorkpackageTestcaseExecutionPlanById(wptceId);
				
				workPackageTestCaseExecutionPlan.setExecutionPriority(executionPriorityEntity);
				
				workPackageService.updateWorkPackageTestCaseExecutionPlan(workPackageTestCaseExecutionPlan);
				List<JsonWorkPackageTestCaseExecutionPlan> tmpList = new ArrayList();
					tmpList.add(new JsonWorkPackageTestCaseExecutionPlan(workPackageTestCaseExecutionPlan));
					jTableResponse = new JTableResponse("OK",tmpList ,1);					
					
		        } catch (Exception e) {
		            jTableResponse = new JTableResponse("ERROR","Error updating executionPriority !");
		            log.error("JSON ERROR", e);
		            e.printStackTrace();
		        }       
	        return jTableResponse;
	    }
		
		@RequestMapping(value="workpackage.allocate.testcase.bulk",method=RequestMethod.POST ,produces="application/json")
		public @ResponseBody JTableResponseOptions listUserForWorkpackageByRole(@RequestParam Integer workPackageId,@RequestParam String param) {
			log.debug("inside workpackage.allocate.testcase.bulk");
			JTableResponseOptions jTableResponseOptions = null;
			List<JsonUserList> jsonUserList=new ArrayList<JsonUserList>();
			List<UserList> listOfUser=new ArrayList<UserList>();
			try {
					if(param!=null && param.equalsIgnoreCase("testlead")){
					
						listOfUser = workPackageService.getAllocatedUserListByRole(workPackageId,IDPAConstants.ROLE_ID_TEST_LEAD);
					}else{
						listOfUser = workPackageService.getAllocatedUserListByRole(workPackageId,IDPAConstants.ROLE_ID_TESTER);
					}
					
					
					if(listOfUser!=null && !listOfUser.isEmpty()){
						for(UserList user: listOfUser){
							jsonUserList.add(new JsonUserList(user));
						}
							jTableResponseOptions = new JTableResponseOptions("OK", jsonUserList);	
					}
			           
		        } catch (Exception e) {
		        	jTableResponseOptions = new JTableResponseOptions("ERROR","Error fetching records!");
		            log.error("JSON ERROR", e);
		        }
		        
	        return jTableResponseOptions;
	    }
		
		@RequestMapping(value="workpackage.execution.list",method=RequestMethod.POST ,produces="application/json")
		public @ResponseBody JTableResponse listAllWorkPackageTestcase(@RequestParam String workPackageId,@RequestParam String envId,@RequestParam String executionPriority,@RequestParam String result,@RequestParam int sortBy,@RequestParam int testcaseId) {
			log.info("inside workpackage.execution.list");
			JTableResponse jTableResponse;
			List<JsonWorkPackageTestCaseExecutionPlanForTester> jsonWorkPackageTestCaseExecutionPlan=new ArrayList<JsonWorkPackageTestCaseExecutionPlanForTester>();
			try {
				
				List<TestCaseDTO> testCaseDTOList=workPackageService.listWorkPackageTestCasesExecutionPlanByWorkPackageIdGroupByTestcase(Integer.parseInt(workPackageId),envId,executionPriority,result,0,10,sortBy,testcaseId);
				if(testCaseDTOList==null || testCaseDTOList.isEmpty()){
					jTableResponse = new JTableResponse("ERROR","No Test case execution plan for workpackage");
					 return jTableResponse;
				}
				else{
					if(sortBy==4){
						int size=testCaseDTOList.size()-1;
						for(int i=size;i>=0;i--){
							TestCaseDTO	testCaseDTO=testCaseDTOList.get(i);
							jsonWorkPackageTestCaseExecutionPlan.add(new JsonWorkPackageTestCaseExecutionPlanForTester(testCaseDTO));
							
						}
					}else{
						for(TestCaseDTO testCaseDTO: testCaseDTOList){
							jsonWorkPackageTestCaseExecutionPlan.add(new JsonWorkPackageTestCaseExecutionPlanForTester(testCaseDTO));
						}
					}
					
				}
				//Bug Fix 3876
				for(JsonWorkPackageTestCaseExecutionPlanForTester jsonWPTCE : jsonWorkPackageTestCaseExecutionPlan){
					Integer jobTotalTestcasesCount = 0;
					Integer workPackageTotalTestcasesCount = 0;
					Integer notExecutedCount = 0;
					//JobStatus will not be null for job level
					if(jsonWPTCE.getJobStatus() != null && (jsonWPTCE.getJobStatus().equalsIgnoreCase("ABORTED") || jsonWPTCE.getJobStatus().equalsIgnoreCase("FAILED"))){
						jobTotalTestcasesCount = workPackageService.getExecutionTCCountForJob(jsonWPTCE.getTestRunJobId());
						notExecutedCount = jobTotalTestcasesCount - (jsonWPTCE.getTotalPass() + jsonWPTCE.getTotalFail());
						jsonWPTCE.setNotExecuted(notExecutedCount);
						jsonWPTCE.setTotalExecutionTCs(jobTotalTestcasesCount);
						jsonWPTCE.setTotalNoRun(notExecutedCount);
					}
					else{
						//JobStatus will be null for workPackage level
						WorkPackage workPackage=workPackageService.getWorkPackageById(Integer.parseInt(workPackageId));
						if(workPackage.getTestRunJobSet() != null){
							for(TestRunJob trj : workPackage.getTestRunJobSet()){
								workPackageTotalTestcasesCount += workPackageService.getExecutionTCCountForJob(trj.getTestRunJobId());
							}
						}
						notExecutedCount = workPackageTotalTestcasesCount - (jsonWPTCE.getTotalPass() + jsonWPTCE.getTotalFail());
						jsonWPTCE.setNotExecuted(notExecutedCount);
						jsonWPTCE.setTotalExecutionTCs(workPackageTotalTestcasesCount);
						jsonWPTCE.setTotalNoRun(notExecutedCount);
						//Bug Fix for 5099, If the workpackage has not executed testcases, then make the workpackage status as failed.
						if(notExecutedCount > 0){
							jsonWPTCE.setWpResult("FAILED");
						}
					}
				}
				jTableResponse = new JTableResponse("OK", jsonWorkPackageTestCaseExecutionPlan,jsonWorkPackageTestCaseExecutionPlan.size());
			} catch (Exception e) {
	            jTableResponse = new JTableResponse("ERROR","Error fetching records!");
	            log.error("JSON ERROR", e);
		    }
	        return jTableResponse;
	    }
		
		@RequestMapping(value="workpackage.testcase.plan.resultsBytcerId",method=RequestMethod.POST ,produces="application/json")
		public @ResponseBody JTableResponse listAllTestcaseExecutionById(@RequestParam Integer wptceId,@RequestParam Integer testcaseId) {
			log.info("inside workpackage.testcase.plan.resultsBytcerId");
			JTableResponse jTableResponse;
			List<JsonWorkPackageTestCaseExecutionPlanForTester> jsonWorkPackageTestCaseExecutionPlan=new ArrayList<JsonWorkPackageTestCaseExecutionPlanForTester>();
			try {
				
					WorkPackageTestCaseExecutionPlan workPackageTestCaseExecutionPlan=workPackageService.listWorkPackageTestCasesExecutionPlan(wptceId, testcaseId);
					
					if(workPackageTestCaseExecutionPlan==null ){
						jTableResponse = new JTableResponse("ERROR","No Test case execution plan for workpackage");
					}else{
						
						jsonWorkPackageTestCaseExecutionPlan.add(new JsonWorkPackageTestCaseExecutionPlanForTester(workPackageTestCaseExecutionPlan));
						jTableResponse = new JTableResponse("OK", jsonWorkPackageTestCaseExecutionPlan,1);
					}
			           
		        } catch (Exception e) {
		            jTableResponse = new JTableResponse("ERROR","Error fetching records!");
		            log.error("JSON ERROR", e);
		        }
		        
	        return jTableResponse;
	    }
		
		@RequestMapping(value="workpackage.testcase.select.bulk",method=RequestMethod.POST ,produces="application/json")
		public @ResponseBody JTableResponse updateWorkPackageTestCase(@RequestParam String tcListsFromUI,@RequestParam String workpackageId) {
			List<JsonWorkPackageTestCase> testCases = new ArrayList<JsonWorkPackageTestCase>();
			JTableResponse jTableResponse = null;
			try {			
				String[] tcLists = tcListsFromUI.split(",");
				
				for(String tcWpId : tcLists){
					
					WorkPackageTestCase workPackageTestCaseFromUI = workPackageService.getWorkpackageTestCaseByPlanId(Integer.parseInt(tcWpId));
					workPackageTestCaseFromUI.setIsSelected(1);
					JsonWorkPackageTestCase jsonWorkPackageTestCase =new JsonWorkPackageTestCase(workPackageTestCaseFromUI);
					
					WorkPackageTestCase updatedWorkPackageTestCase = workPackageService.updateWorkPackageTestCase(workPackageTestCaseFromUI, jsonWorkPackageTestCase);
	
	
					JsonWorkPackageTestCase updatedJsonWorkPackageTestCase = new JsonWorkPackageTestCase(updatedWorkPackageTestCase);

					testCases.add(updatedJsonWorkPackageTestCase);
				}
				jTableResponse = new JTableResponse("OK",testCases,1);
		    } catch (Exception e) {
		        jTableResponse = new JTableResponse("ERROR","Unable to update the testcase selection!");
		        log.error("JSON ERROR", e);
		    }	        
	        return jTableResponse;
	    }
//Bulk operation for Testsuite start
		@RequestMapping(value="workpackage.testsuite.select.bulk",method=RequestMethod.POST ,produces="application/json")
		public @ResponseBody JTableResponse updateWorkPackageTestSuite(@RequestParam String tsListsFromUI,@RequestParam String workpackageId) {
			List<JsonWorkPackageTestSuite> testSuites = new ArrayList<JsonWorkPackageTestSuite>();
			JTableResponse jTableResponse = null;
			try {	
				
				String[] tsLists = tsListsFromUI.split(",");
			
				
				WorkPackage workPackage=workPackageService.getWorkPackageById(Integer.parseInt(workpackageId));
				for(String tsWpId : tsLists){
			
				WorkPackageTestSuite workPackageTestSuiteFromUI=new WorkPackageTestSuite();
				
				WorkPackageTestSuite workPackageTestSuiteFromDB = workPackageService.getWorkpackageTestSuiteByPlanId(Integer.parseInt(tsWpId));
			
				workPackageTestSuiteFromDB.setIsSelected(1);
				WorkPackageTestSuite updatedWorkPackageTestSuite = workPackageService.updateWorkPackageTestSuite(workPackageTestSuiteFromDB, null);
				JsonWorkPackageTestSuite jsonWorkPackageTestSuite =new JsonWorkPackageTestSuite(updatedWorkPackageTestSuite);
				
				JsonWorkPackageTestSuite updatedJsonWorkPackageTestSuite = new JsonWorkPackageTestSuite(updatedWorkPackageTestSuite);

					testSuites.add(updatedJsonWorkPackageTestSuite);
				}
				jTableResponse = new JTableResponse("OK",testSuites,1);
		    } catch (Exception e) {
		    
		        jTableResponse = new JTableResponse("ERROR"," selection!");
		        log.error("JSON ERROR", e);
		    }	        
	        return jTableResponse;
	    }
		//Bulk opeation for TestsuiteEnd

		@RequestMapping(value="workpackage.feature.select.bulk",method=RequestMethod.POST ,produces="application/json")
		public @ResponseBody JTableResponse updateWorkPackageFeature(@RequestParam String tsListsFromUI,@RequestParam String workpackageId) {
			List<JsonWorkPackageFeature> features = new ArrayList<JsonWorkPackageFeature>();
			JTableResponse jTableResponse = null;
			try {	
				
				String[] tsLists = tsListsFromUI.split(",");
			
				
				WorkPackage workPackage=workPackageService.getWorkPackageById(Integer.parseInt(workpackageId));
				for(String tsWpId : tsLists){
			
				WorkPackageFeature workPackageFeatureFromUI=new WorkPackageFeature();
				
				WorkPackageFeature workPackageFeatureFromDB = workPackageService.getWorkpackageFeaturePlanById(Integer.parseInt(tsWpId));
			
				workPackageFeatureFromDB.setIsSelected(1);
				WorkPackageFeature updatedWorkPackageFeature = workPackageService.updateWorkPackageFeature(workPackageFeatureFromDB, null);
				JsonWorkPackageFeature jsonWorkPackageFeature =new JsonWorkPackageFeature(updatedWorkPackageFeature);
				
				JsonWorkPackageFeature updatedJsonWorkPackageFeature = new JsonWorkPackageFeature(updatedWorkPackageFeature);

				features.add(updatedJsonWorkPackageFeature);
				}
				jTableResponse = new JTableResponse("OK",features,1);
		    } catch (Exception e) {
		    
		        jTableResponse = new JTableResponse("ERROR"," selection!");
		        log.error("JSON ERROR", e);
		    }	        
	        return jTableResponse;
	    }

		@RequestMapping(value="workpackage.testcase.plan.resultsByWorkPackageByTestCase",method=RequestMethod.POST ,produces="application/json")
	    public @ResponseBody JTableResponse listWorkpackageTestCaseExecutionPlansByWorkPackageIdByTestcase(@RequestParam int workPackageId,@RequestParam int testcaseId,HttpServletRequest req) {
			log.debug("inside workpackage.testcase.plan.resultsByWorkPackageByTestCase");
			JTableResponse jTableResponse = null;
				try {
					if(workPackageId<=0){
						workPackageId=1;
					}
					
					List<WorkPackageTestCaseExecutionPlan> workPackageTestCaseExecutionPlanList=workPackageService.listWorkPackageTestCasesExecutionPlanByWorkPackageId(workPackageId,testcaseId);
					
					List<JsonWorkPackageTestCaseExecutionPlanForTester> jsonWorkPackageTestCaseExecutionPlans=new ArrayList<JsonWorkPackageTestCaseExecutionPlanForTester>();
					
					if (workPackageTestCaseExecutionPlanList == null || workPackageTestCaseExecutionPlanList.isEmpty()) {
						
						//TODO : Initialize the workpackage with the test cases for the product
						log.info("No workpackage testcase execution plan for workpackage id:"+workPackageId  );
						jTableResponse = new JTableResponse("OK", jsonWorkPackageTestCaseExecutionPlans, 0);
					} else {
						for(WorkPackageTestCaseExecutionPlan workPackageTestCaseExecutionPlan: workPackageTestCaseExecutionPlanList){
							jsonWorkPackageTestCaseExecutionPlans.add(new JsonWorkPackageTestCaseExecutionPlanForTester(workPackageTestCaseExecutionPlan));
						}
				        
						jTableResponse = new JTableResponse("OK", jsonWorkPackageTestCaseExecutionPlans, jsonWorkPackageTestCaseExecutionPlans.size());
						workPackageTestCaseExecutionPlanList = null;
					}
		        } catch (Exception e) {
		            jTableResponse = new JTableResponse("ERROR","Error fetching records!");
		            log.error("JSON ERROR", e);	            
		        }
	        return jTableResponse;
	    }
		@RequestMapping(value="testCase.configuration.listBywpId",method=RequestMethod.POST ,produces="application/json")
		public @ResponseBody JTableResponse listTestCaseConfigurationBywpId(@RequestParam Integer workPackageId) {
			log.info("testCase.configuration.listBywpId");
			JTableResponse jTableResponse=null;
			List<JsonTestCaseConfiguration> jsonTestCaseConfigurationList=new ArrayList<JsonTestCaseConfiguration>();
			try {
				WorkPackage workPackage=workPackageService.getWorkPackageById(workPackageId);
			Set<WorkPackageTestCaseExecutionPlan> workPackageTestCaseExecutionPlanSet	=workPackage.getWorkPackageTestCaseExecutionPlan();
			for(WorkPackageTestCaseExecutionPlan workPackageTestCaseExecutionPlan:workPackageTestCaseExecutionPlanSet){
					Set<TestCaseConfiguration> testCaseConfigurationSet=workPackageTestCaseExecutionPlan.getTestCaseConfigurationSet();
					if(testCaseConfigurationSet!=null ){
						for(TestCaseConfiguration testCaseConfiguration:testCaseConfigurationSet){
						jsonTestCaseConfigurationList.add(new JsonTestCaseConfiguration(testCaseConfiguration));
					}
					}
					testCaseConfigurationSet=null;
			}
						jTableResponse = new JTableResponse("OK", jsonTestCaseConfigurationList,jsonTestCaseConfigurationList.size());
		
			
			           
		        } catch (Exception e) {
		            jTableResponse = new JTableResponse("ERROR","Error fetching records!");
		            log.error("JSON ERROR", e);
		        }
		        
	        return jTableResponse;
	    }
		@RequestMapping(value="testCase.configuration.add",method=RequestMethod.POST ,produces="application/json")
	    public @ResponseBody JTableSingleResponse addTestCaseConfiguration(@ModelAttribute JsonTestCaseConfiguration jsonTestCaseConfiguration) {			
			JTableSingleResponse jTableSingleResponse =null;
			
			try {
				TestCaseConfiguration testCaseConfiguration=jsonTestCaseConfiguration.getTestCaseConfiguration();
				workPackageService.addTestCaseConfiguration(testCaseConfiguration);

		            jTableSingleResponse = new JTableSingleResponse("OK",new JsonTestCaseConfiguration(testCaseConfiguration));
		        } catch (Exception e) {
		            jTableSingleResponse = new JTableSingleResponse("ERROR","Error adding new testCaseConfiguration!");
		            log.error("JSON ERROR", e);
		        }
	        return jTableSingleResponse;			
	    }

		@RequestMapping(value="workpackage.allocateTestCase.update",method=RequestMethod.POST ,produces="application/json")
		public @ResponseBody JTableResponse updateAllocateTestCase(@RequestParam String modifiedColumn,@RequestParam String modifiedValue,@RequestParam Integer rowId) {
			JTableResponse jTableResponse;
						
			try {	
				WorkPackageTestCaseExecutionPlan wptcep=workPackageService.getWorkpackageTestcaseExecutionPlanById(rowId);
				
				if(modifiedColumn.equalsIgnoreCase("PlannedExecutionDate")){
					wptcep.setPlannedExecutionDate(DateUtility.dateformatWithOutTime(modifiedValue));
					workPackageService.updateWorkPackageTestCaseExecutionPlan(wptcep);
				}
//				
					List<JsonWorkPackageTestCaseExecutionPlan> tmpList = new ArrayList();
					tmpList.add(new JsonWorkPackageTestCaseExecutionPlan(wptcep));
					jTableResponse = new JTableResponse("OK",tmpList ,1);					
					
		        } catch (Exception e) {
		            jTableResponse = new JTableResponse("ERROR","Error updating record!");
		            log.error("JSON ERROR", e);
		            e.printStackTrace();
		        }       
	        return jTableResponse;
	    }
		
		@RequestMapping(value="workpackage.allocateFeature.update",method=RequestMethod.POST ,produces="application/json")
		public @ResponseBody JTableResponse updateAllocateFeature(@RequestParam String modifiedColumn,@RequestParam String modifiedValue,@RequestParam Integer rowId) {
			JTableResponse jTableResponse;
						
			try {	
				WorkPackageFeatureExecutionPlan wptcep=workPackageService.getWorkpackageFeatureExecutionPlanById(rowId);
				if(modifiedColumn.equalsIgnoreCase("PlannedExecutionDate")){
					wptcep.setPlannedExecutionDate(DateUtility.dateformatWithOutTime(modifiedValue));
					workPackageService.updateWorkPackageFeatureExecutionPlan(wptcep);
				}
				
				List<WorkPackageTestCaseExecutionPlan> currentWorkPackageTestCaseExecutionPlanList = null;
				currentWorkPackageTestCaseExecutionPlanList=workPackageService.getWorkPackageTestcaseExecutionPlanByWorkPackageId(wptcep.getWorkPackage().getWorkPackageId(), -1, -1, wptcep.getFeature().getProductFeatureId(), "Feature",wptcep.getRunConfiguration().getRunconfiguration().getRunconfigId());
				for(WorkPackageTestCaseExecutionPlan wptc:currentWorkPackageTestCaseExecutionPlanList){
					wptc.setPlannedExecutionDate(DateUtility.dateformatWithOutTime(modifiedValue));
					workPackageService.updateWorkPackageTestCaseExecutionPlan(wptc);
				}
				
//				
					List<JsonWorkPackageFeatureExecutionPlan> tmpList = new ArrayList();
					tmpList.add(new JsonWorkPackageFeatureExecutionPlan(wptcep));
					jTableResponse = new JTableResponse("OK",tmpList ,1);					
					
		        } catch (Exception e) {
		            jTableResponse = new JTableResponse("ERROR","Error updating record!");
		            log.error("JSON ERROR", e);
		            e.printStackTrace();
		        }       
	        return jTableResponse;
	    }
		
		@RequestMapping(value="workpackage.testcase.execution.listBySort",method=RequestMethod.POST ,produces="application/json")
		public @ResponseBody JTableResponse listAllWorkPackageTestcaseExecutionList(@RequestParam Integer workPackageId/*,@RequestParam String envId,@RequestParam String executionPriority,@RequestParam String result*/,@RequestParam int jtStartIndex, @RequestParam int jtPageSize/*,@RequestParam int sortBy*/) {
			log.info("inside workpackage.testcase.execution.listBySort");
			JTableResponse jTableResponse;
			List<JsonWorkPackageTestCaseExecutionPlanForTester> jsonWorkPackageTestCaseExecutionPlan=new ArrayList<JsonWorkPackageTestCaseExecutionPlanForTester>();
			try {
				
					List<TestCaseDTO> testCaseDTOList=workPackageService.listWorkPackageTestCasesExecutionPlanByWorkPackageIdGroupByTestcase(workPackageId,null,null,null,jtStartIndex,jtPageSize,0,0);
					
					if(testCaseDTOList==null || testCaseDTOList.isEmpty()){
						jTableResponse = new JTableResponse("ERROR","No Test case execution plan for workpackage");
					}else{
						
						for(TestCaseDTO testCaseDTO: testCaseDTOList){
							jsonWorkPackageTestCaseExecutionPlan.add(new JsonWorkPackageTestCaseExecutionPlanForTester(testCaseDTO));
							
						}
						jTableResponse = new JTableResponse("OK", jsonWorkPackageTestCaseExecutionPlan,jsonWorkPackageTestCaseExecutionPlan.size());
					}
			           
		        } catch (Exception e) {
		            jTableResponse = new JTableResponse("ERROR","Error fetching records!");
		            log.error("JSON ERROR", e);
		        }
		        
	        return jTableResponse;
	    }

		@RequestMapping(value="testcase.configuration.list",method=RequestMethod.POST ,produces="application/json")
	    public @ResponseBody JTableResponse listTestCaseConfiguration(@RequestParam int workpackageId) {
			log.debug("inside testcase.configuration.list");
			JTableResponse jTableResponse = null;
				try {		        
					List<TestCaseConfiguration> testCaseConfigurations = workPackageService.listTestCaseConfigurations(workpackageId);
					List<JsonTestCaseConfiguration> jsonTestCaseConfigurations = new ArrayList<JsonTestCaseConfiguration>();
					
					for(TestCaseConfiguration tcc: testCaseConfigurations){
						jsonTestCaseConfigurations.add(new JsonTestCaseConfiguration(tcc));
					}				
		            jTableResponse = new JTableResponse("OK", jsonTestCaseConfigurations,jsonTestCaseConfigurations.size());     
			        } catch (Exception e) {
			            jTableResponse = new JTableResponse("ERROR","Error fetching records!");
			            log.error("JSON ERROR", e);
			        }
		        return jTableResponse;
	    }
		
		@RequestMapping(value="environmentCombination.workpackage",method=RequestMethod.POST ,produces="application/json")
	    public @ResponseBody JTableResponse listEnvironmnetCombinationById(@RequestParam int workPackageId ) {			
			JTableResponse jTableResponse=null;;			 
			JsonEnvironmentCombination jsonEnvironmentCombination=null;
			List<JsonEnvironmentCombination> jsonEnvironmentCombinationList=new ArrayList<JsonEnvironmentCombination>();

			try {

				if(workPackageId!=-1){
					WorkPackage workPackage=workPackageService.getWorkPackageById(workPackageId);
					Set<EnvironmentCombination> environmentCombinationSet= workPackage.getEnvironmentCombinationList();
					if(environmentCombinationSet!=null && environmentCombinationSet.size()!=0){
						for(EnvironmentCombination environmentCombinationFromWP:environmentCombinationSet){
							jsonEnvironmentCombination=	new JsonEnvironmentCombination(environmentCombinationFromWP);
							jsonEnvironmentCombinationList.add(jsonEnvironmentCombination);
						}
					}
				}
				
				jTableResponse = new JTableResponse("OK", jsonEnvironmentCombinationList,jsonEnvironmentCombinationList.size());  
				
				//jsonEnvironmentCombination = null;
		        } catch (Exception e) {
		            jTableResponse = new JTableResponse("ERROR","Unable to show Workpackage Test Environments Combination!");
		            log.error("JSON ERROR", e);
		        }		        
	        return jTableResponse;
	    }
		

		@RequestMapping(value="workpackage.testsuite.update",method=RequestMethod.POST ,produces="application/json")
		public @ResponseBody JTableResponse updateWorkPackageTestSuite(@ModelAttribute JsonWorkPackageTestSuite jsonWorkPackageTestSuite, BindingResult result) {
			log.info("testsuite update");
			JTableResponse jTableResponse = null;
			if(result.hasErrors()){
				jTableResponse = new JTableResponse("ERROR","Invalid Form!"); 
			}
			
			try {	
				TestSuiteList testSuite = testSuiteConfigurationService.getByTestSuiteId(jsonWorkPackageTestSuite.getTestsuiteId());
				if(testSuite != null){
					if(testSuite.getTestCaseLists().size()==0){
						 jTableResponse = new JTableResponse("ALERT","Test Case(s) not mapped to this Test Suite. Please map test cases and perform planning");
					}else{
						WorkPackageTestSuite workPackageTestSuiteFromUI=new WorkPackageTestSuite();
						workPackageTestSuiteFromUI.setWorkpackageTestSuiteId(jsonWorkPackageTestSuite.getId());
						workPackageTestSuiteFromUI.setTestSuite(testSuiteConfigurationService.getByTestSuiteId(jsonWorkPackageTestSuite.getTestsuiteId()));
						workPackageTestSuiteFromUI.setWorkPackage(workPackageService.getWorkPackageById(jsonWorkPackageTestSuite.getWorkPackageId()));
						workPackageTestSuiteFromUI.setEditedDate(new Date(System.currentTimeMillis()));
						workPackageTestSuiteFromUI.setIsSelected(Integer.parseInt(jsonWorkPackageTestSuite.getIsSelected()));
						workPackageTestSuiteFromUI.setStatus(jsonWorkPackageTestSuite.getStatus());
						WorkPackageTestSuite updatedWorkPackageTestSuite = workPackageService.updateWorkPackageTestSuite(workPackageTestSuiteFromUI, jsonWorkPackageTestSuite);
		
						log.info("updated the table");
						List<JsonWorkPackageTestSuite> testSuites = new ArrayList<JsonWorkPackageTestSuite>();
		
						JsonWorkPackageTestSuite updatedJsonWorkPackageTestSuite = new JsonWorkPackageTestSuite(updatedWorkPackageTestSuite);
		
						testSuites.add(updatedJsonWorkPackageTestSuite);
						jTableResponse = new JTableResponse("OK",testSuites,1);
					}
				}
		    } catch (Exception e) {
		        jTableResponse = new JTableResponse("ERROR","Unable to update the testSuite selection!");
		        log.error("JSON ERROR", e);
		    }	        
	        return jTableResponse;
	    }


		@RequestMapping(value="administration.workpackage.maprunconfig",method=RequestMethod.POST ,produces="application/json")
		public  @ResponseBody JTableResponse mapRunConfigWorkpackage(@ModelAttribute JsonRunConfiguration jsonRunConfiguration, BindingResult result,@RequestParam Integer workpackageId) {
			log.info("inside mapEnvironmentWIthWorkpackage");
			JTableResponse jTableResponse = null;
			int envId=0;
			 WorkPackage workPackage=workPackageService.getWorkPackageById(workpackageId);

			//checking whether already testcase exists
			Set<WorkPackageTestSuite> workPackageTestSuites = workPackage.getWorkPackageTestSuites();
			Set<WorkPackageTestCaseExecutionPlan> workPackageTestCaseExecutionPlanListFromDB =workPackage.getWorkPackageTestCaseExecutionPlan();
			List<WorkPackageTestCaseExecutionPlan> workPackageTestCaseExecutionPlanListForUpdate= new ArrayList<WorkPackageTestCaseExecutionPlan>();
			RunConfiguration runConfiguration=productListService.getRunConfigurationById(jsonRunConfiguration.getRunconfigId());
			
			List<WorkPackageTestCaseExecutionPlan>	deleteList= new ArrayList<WorkPackageTestCaseExecutionPlan>();
			if(jsonRunConfiguration.getStatus()==0){
				for(WorkPackageTestCaseExecutionPlan wptcep :workPackageTestCaseExecutionPlanListFromDB){
					if(wptcep.getRunConfiguration().getRunconfiguration().getRunconfigId().equals(jsonRunConfiguration.getRunconfigId())){
						workPackageService.deleteTestStepResult(wptcep.getTestCaseExecutionResult().getTestCaseExecutionResultId());
						workPackageService.deleteWorkPackageTestCaseExecutionPlan(wptcep);
					}
				}
				workPackage=workPackageService.deleteRunConfigurationWorkpackage(workpackageId,runConfiguration.getRunconfigId(),"testsuite");
			}else if (jsonRunConfiguration.getStatus()==1){
				
				workPackageService.addRunConfigurationWorkpackage(workpackageId,runConfiguration.getRunconfigId(),"testsuite");

				WorkpackageRunConfiguration workpackageRunConfiguration=workPackageService.getWorkpackageRunConfiguration(workpackageId, runConfiguration.getRunconfigId(), "testsuite");
				for(WorkPackageTestSuite workPackageTestSuite:workPackageTestSuites){
					
					if(workPackageTestSuite.getIsSelected()==1){
						Set<TestCaseList> testCaseLists=workPackageTestSuite.getTestSuite().getTestCaseLists();
						for(TestCaseList testCaseList:testCaseLists){
						
							WorkPackageTestCaseExecutionPlan workPackageTestCaseExecutionPlan = new WorkPackageTestCaseExecutionPlan();
							workPackageTestCaseExecutionPlan.setTestSuiteList(workPackageTestSuite.getTestSuite());
							workPackageTestCaseExecutionPlan.setWorkPackage(workPackage);
							workPackageTestCaseExecutionPlan.setRunConfiguration(workpackageRunConfiguration);
							workPackageTestCaseExecutionPlan.setExecutionStatus(3);
							workPackageTestCaseExecutionPlan.setIsExecuted(0);
							ExecutionPriority executionPriority=null;
							if(testCaseList.getTestCasePriority()!=null)
								executionPriority= workPackageService.getExecutionPriorityByName(CommonUtility.getExecutionPriority(testCaseList.getTestCasePriority().getPriorityName()));
							else
								executionPriority= workPackageService.getExecutionPriorityByName(IDPAConstants.EXECUTION_PRIORITY_HIGH);
							
							workPackageTestCaseExecutionPlan.setExecutionPriority(executionPriority);
							
							TestCaseExecutionResult testCaseExecutionResult = new TestCaseExecutionResult();
							testCaseExecutionResult.setResult("");
							testCaseExecutionResult.setComments("");
							testCaseExecutionResult.setDefectsCount(0);
							testCaseExecutionResult.setDefectIds("");
							testCaseExecutionResult.setIsApproved(0);
							testCaseExecutionResult.setIsReviewed(0);
							testCaseExecutionResult.setObservedOutput("");
							
							//Commented the following statements when remove the WorkPackageTestcaseExecutionId column from Test Case Execution Result table. By: Logeswari, On :  11-Feb-2015
							testCaseExecutionResult.setWorkPackageTestCaseExecutionPlan(workPackageTestCaseExecutionPlan);
							workPackageTestCaseExecutionPlan.setTestCaseExecutionResult(testCaseExecutionResult);
							workPackageTestCaseExecutionPlanListForUpdate.add(workPackageTestCaseExecutionPlan);
						}
					}
				}
				workPackageService.addWorkPackageTestcaseExecutionPlan(workPackageTestCaseExecutionPlanListForUpdate);
				
				jTableResponse = new JTableResponse("OK","Environment Added Successfully");
			}
			 return jTableResponse;
	    }

		
		@RequestMapping(value="administration.workpackage.testrunplan",method=RequestMethod.POST ,produces="application/json")
	    public @ResponseBody JTableSingleResponse addWorkpackageWithTestRunPlan(HttpServletRequest req,@RequestParam Map<String, String>  mapData) {			
			JTableSingleResponse jTableSingleResponse =null;
			Integer testRunPlanId=Integer.parseInt(mapData.get("testRunPlanId"));
			TestRunPlan testRunPlan=productListService.getTestRunPlanBytestRunPlanId(testRunPlanId);
			UserList user=(UserList)req.getSession().getAttribute("USER");
			Integer productKey = null;
			Integer productBuildKey = Integer.parseInt(mapData.get("productBuildId"));
			ProductBuild productBuild=productListService.getProductBuildById(Integer.parseInt(mapData.get("productBuildId")), 0);			
			if(productBuild != null){
				productKey = productBuild.getProductVersion().getProductMaster().getProductId();
				log.info("productKey ***** "+productKey);
			}
		
			try {					
				//Check if call to ISE has to be made
				//TRP is not configured for ISE use. Go with normal flow
				log.info("Normal WP flow as TRP is not configured for ISE use"); 
				WorkPackage workPackage= workPackageService.addWorkpackageToTestRunPlan(testRunPlan,mapData,user,req,null,null);				
				String jobIds = ScriptLessExecutionDTO.getJobIDs();
				if(jobIds != null && !jobIds.trim().equalsIgnoreCase(""))
					jTableSingleResponse = new JTableSingleResponse("OK", testRunPlan.getTestRunPlanName() + " execution initiated with Workpackage [" + workPackage.getWorkPackageId() + "] " + workPackage.getName() + System.lineSeparator() + System.lineSeparator() + "Jobs " + jobIds.substring(0, jobIds.length()-1) + " queued for execution.");
				else 
					jTableSingleResponse = new JTableSingleResponse("OK", testRunPlan.getTestRunPlanName() + " execution not initiated as there are no valid Test Jobs");				
				req.getSession().setAttribute("workpackageId", workPackage.getWorkPackageId());
				req.getSession().setAttribute("type", "fromTestRunPlan");
				req.getSession().setAttribute("productKey", productKey);
		        req.getSession().setAttribute("productBuildKey", productBuildKey);
		        
			}catch (Exception e) {
		            jTableSingleResponse = new JTableSingleResponse("ERROR","Error adding new WorkPackage!");
		            log.error("JSON ERROR", e);
		        }
			
	        return jTableSingleResponse;			
	    }
		
		/*
		 * This is the method that gets invoked for executing test run plan
		 */
		@RequestMapping(value="administration.workpackage.testrunplan.devices",method=RequestMethod.POST ,produces="application/json")
	    public @ResponseBody JTableSingleResponse addWorkpackageWithTestRunPlanWithDevices(HttpServletRequest req,@RequestParam Map<String, String>  mapData) {
			JTableSingleResponse jTableSingleResponse =null;
			Integer testRunPlanId=Integer.parseInt(mapData.get("testRunPlanId"));			
			TestRunPlan testRunPlan=productListService.getTestRunPlanBytestRunPlanId(testRunPlanId);
			List<EntityConfigurationProperties> testRunPlanConfigurationProperties = entityConfigurationPropertiesService.getEntityConfigurePropertiesByEntityId(testRunPlanId, IDPAConstants.ENTITY_TEST_RUN_PLAN_ID, -1);
			Map<String, String> envMap = new HashMap<String, String>();
			if(testRunPlanConfigurationProperties != null && testRunPlanConfigurationProperties.size() > 0){
				for(EntityConfigurationProperties ecf : testRunPlanConfigurationProperties){
					if(ecf != null && ecf.getProperty() != null && ecf.getValue() != null){
						envMap.put(ecf.getProperty(), ecf.getValue());
					}
				}
			}
			if(envMap != null){
				if(envMap.get("action") != null && envMap.get("action").equalsIgnoreCase("UpdateTSPack") && envMap.get("jenkins.build.url") != null 
						&& envMap.get("jenkins.status.url") != null &&  envMap.get("jenkins.lastbuild.url") != null) {
					JTableResponse response = triggerJenkinsBuild(testRunPlanId, envMap);
					if(response != null && response.getResult() != null){
						String msg = response.getResult();
						if(msg != null && msg != "" && !msg.equalsIgnoreCase("Failed")){	
							Map<String, String> map = new Gson().fromJson(msg, Map.class);
							testSuiteConfigurationService.updateTestSuiteScriptPack(Integer.valueOf(map.get("testSuiteId")), 
									"", testRunPlan.getProductVersionListMaster().getProductMaster().getProductId(), 
									map.get("testSuitePath"), map.get("testSuitePackSource"));
						} else {
							jTableSingleResponse = new JTableSingleResponse("ERROR","Unable to import script pack from CI System");
							return jTableSingleResponse;
						}
					} else {
						jTableSingleResponse = new JTableSingleResponse("ERROR","Unable to import script pack from CI System");
						return jTableSingleResponse;
					}					
				} else if(envMap.get("action") != null && envMap.get("action").equalsIgnoreCase("UpdateTSPack")) {
					jTableSingleResponse = new JTableSingleResponse("ERROR","Unable to trigger CI build from TAF conitnuing Test Run Plan execution");
					return jTableSingleResponse;
				}
			}			
			log.info("administration.workpackage.testrunplan.devices");						
			UserList user=(UserList)req.getSession().getAttribute("USER");
			Integer productKey = null;
			Integer productBuildKey = Integer.parseInt(mapData.get("productBuildId"));
			
			ProductBuild productBuild=productListService.getProductBuildById(Integer.parseInt(mapData.get("productBuildId")), 0);
			if(productBuild != null){
				productKey = productBuild.getProductVersion().getProductMaster().getProductId();
				log.info("productKey ***** "+productKey);
			}		
			String runconfigId= mapData.get("runconfigId");
			try {
					WorkPackage workPackage = workPackageService.addWorkpackageToTestRunPlan(testRunPlan,mapData,user,req,null,null,runconfigId);									
					if(testRunPlan.getAutomationMode() != null && testRunPlan.getAutomationMode().equalsIgnoreCase(IDPAConstants.AUTOMATION_MODE_ATTENDED)){
						String jobIds = ScriptLessExecutionDTO.getJobIDs();
						if(jobIds != null && !jobIds.trim().equalsIgnoreCase("")){
							jTableSingleResponse = new JTableSingleResponse("OK", testRunPlan.getTestRunPlanName() + " execution initiated with Workpackage [" + workPackage.getWorkPackageId() + "] " + workPackage.getName() + System.lineSeparator() + System.lineSeparator() + "Jobs " + jobIds.substring(0, jobIds.length()-1) + " queued for execution.");
						} else {
							jTableSingleResponse = new JTableSingleResponse("ERROR", testRunPlan.getTestRunPlanName() + " execution not initiated as there are no valid Test Jobs");
						}
					} else{
						jTableSingleResponse = new JTableSingleResponse("OK", "Workpackage ID :" + workPackage.getWorkPackageId());
					}				
					req.getSession().setAttribute("workpackageId", workPackage.getWorkPackageId());
					req.getSession().setAttribute("type", "fromTestRunPlan");
					req.getSession().setAttribute("productKey", productKey);
		            req.getSession().setAttribute("productBuildKey", productBuildKey);				
		        }catch (Exception e) {
		            jTableSingleResponse = new JTableSingleResponse("ERROR","Test Run Plan could not be executed for some reason!");
		            log.error("JSON ERROR", e);
		        }
	        return jTableSingleResponse;			
	    }
		
		@RequestMapping(value="administration.workpackage.testrunplangroup",method=RequestMethod.POST ,produces="application/json")
	    public @ResponseBody JTableSingleResponse addWorkpackageWithTestRunPlanGroup(HttpServletRequest req,@RequestParam Map<String, String>  mapData) {			
			JTableSingleResponse jTableSingleResponse =null;
			Integer testRunPlanGroupId=Integer.parseInt(mapData.get("testRunPlanGroupId"));
			TestRunPlanGroup testRunPlanGroup=productListService.getTestRunPlanGroupById(testRunPlanGroupId);
			UserList user=(UserList)req.getSession().getAttribute("USER");
		
			try {
				//Validation check for Test Plan readiness  - starts
				Set<TestRunPlangroupHasTestRunPlan> mappedTestPlans = testRunPlanGroup.getTestRunPlangroupHasTestRunPlans();
				if(mappedTestPlans.isEmpty() || mappedTestPlans.size() ==0)
					return new JTableSingleResponse("ERROR","There is no TestPlan available for execution");
				for(TestRunPlangroupHasTestRunPlan tpg : mappedTestPlans){
					TestRunPlan testPlan = tpg.getTestrunplan(); 
					if(testPlan != null){
						VerificationResult testPlanReadinessCheck = productListService.testPlanReadinessCheck(testPlan.getTestRunPlanId());
						if(testPlanReadinessCheck != null && testPlanReadinessCheck.getIsReady() != null && testPlanReadinessCheck.getIsReady().equalsIgnoreCase("No")){
							return new JTableSingleResponse("ERROR","Test Plan Group execution not initiated as it is not ready for execution due to\n"+testPlanReadinessCheck.getVerificationMessage());
						}
					}
				}
				//Validation check for Test Plan readiness  - ends
				workPackageService.addWorkpackageToTestRunPlanGroup(testRunPlanGroup,mapData,user,req);
		        jTableSingleResponse = new JTableSingleResponse("OK","Test Plan Group triggered successfully");
		    } catch (Exception e) {
		    	jTableSingleResponse = new JTableSingleResponse("ERROR","Error adding new WorkPackage!");
		        log.error("JSON ERROR", e);
		    }
	        return jTableSingleResponse;			
	    }
		@RequestMapping(value="administration.workPackage.list.bytestRunPlanId",method=RequestMethod.POST ,produces="application/json")
	    public @ResponseBody JTableResponse listWorkPackageByTestRunPlanId(HttpServletRequest req,@RequestParam Integer testRunPlanId) {			
			JTableResponse jTableResponse =null;
			try {
				List<WorkPackage> workPackageList=workPackageService.listWorkPackageBytestrunplanId(testRunPlanId);
				List<JsonWorkPackage> jsonWpList=new ArrayList<JsonWorkPackage>();
				for(WorkPackage wp:workPackageList){
					jsonWpList.add(new JsonWorkPackage(wp));
				}
				jTableResponse = new JTableResponse("OK",jsonWpList,jsonWpList.size());
		        } catch (Exception e) {
		        	jTableResponse = new JTableResponse("ERROR","Error adding new WorkPackage!");
		            log.error("JSON ERROR", e);
		        }
	        return jTableResponse;			
	    }
		
		@RequestMapping(value="is.valid.workpackage.status",method=RequestMethod.POST ,produces="application/json")
	    public @ResponseBody JTableResponseOptions isValidWorkPackageStatus(@RequestParam int workPackageId, @RequestParam String type) {
			log.info("inside workpackage.demand.projection.list.by.date ***************** workPackageId: "+workPackageId);
			JTableResponseOptions jTableResponseOptions = null;
			String message = "";
			try {
					if(workPackageId == 0 || workPackageId == -1){
						return null;
					}else{
						WorkPackage workPackageBean = workPackageService.getWorkPackageById(workPackageId);
						WorkFlow workFlow=null;
							if(workPackageBean.getWorkFlowEvent()!=null){
								workFlow=workPackageBean.getWorkFlowEvent().getWorkFlow();
								if(workFlow!=null){
									if(workFlow.getEntityMaster()!=null){
										if(workFlow.getEntityMaster().getEntitymasterid()==IDPAConstants.WORKPACKAGE_ENTITY_ID){
										int stageId = workFlow.getStageId();
										log.info("WP: "+workPackageBean.getName());
										log.info("stageId: "+stageId);
										if(type.equalsIgnoreCase("Reservation")){
											message = "Sorry. You cannot reserve resource as Work Package is Aborted.";
										}else if(type.equalsIgnoreCase("RaiseDemand")){
											message = "Sorry. You cannot raise resource demand as Work Package is Aborted.";
										}
										if(stageId == IDPAConstants.WORKFLOW_STAGE_ID_ABORTED){
											jTableResponseOptions = new JTableResponseOptions("ERROR",message);
										}
										else if(stageId == IDPAConstants.WORKFLOW_STAGE_ID_CLOSED){
											 jTableResponseOptions = new JTableResponseOptions("ERROR",message);
										}else{
											jTableResponseOptions = new JTableResponseOptions("OK");
										}
									}
								}
							}
						}
					}
		        } catch (Exception e) {
		        	jTableResponseOptions = new JTableResponseOptions("ERROR","Error Raising Resource Demand for Work package!");
		            log.error("JSON ERROR", e);	            
		        }
	        return jTableResponseOptions;
	    }

		@RequestMapping(value="administration.workPackage.planByid",method=RequestMethod.POST ,produces="application/json")
	    public @ResponseBody JTableResponse listWorkPackageByWPTCEPId(HttpServletRequest req,@RequestParam Integer wptcepId) {			
			JTableResponse jTableResponse =null;
			try {
				WorkPackageTestCaseExecutionPlan wptcep =workPackageService.getWorkpackageTestcaseExecutionPlanById(wptcepId);
				List<JsonWorkPackageTestCaseExecutionPlan> jsonWpList=new ArrayList<JsonWorkPackageTestCaseExecutionPlan>();
				
				if(wptcep!=null){
					jsonWpList.add(new JsonWorkPackageTestCaseExecutionPlan(wptcep));
				}
				jTableResponse = new JTableResponse("OK",jsonWpList,jsonWpList.size());
		        } catch (Exception e) {
		        	jTableResponse = new JTableResponse("ERROR","Error adding new wptcep!");
		            log.error("JSON ERROR", e);
		        }
	        return jTableResponse;			
	    }
		
		@RequestMapping(value="workpackage.product.mode",method=RequestMethod.POST ,produces="application/json")
		public @ResponseBody String getProductModeFromWorkPackage(@RequestParam int wpId){
			String productMode = null;
			try {			
				WorkPackage workPackage = workPackageService.getWorkPackageById(wpId);
				if(workPackage != null){
					ProductMaster product = workPackageService.getProductMasterByWorkpackageId(workPackage.getWorkPackageId());
					if(product != null){
						productMode = product.getProductMode().getModeName();
					}
				}
		        } catch (Exception e) {
		            log.error("JSON ERROR", e);
		        }
			return productMode;
		}

		@RequestMapping(value="workpackage.for.testmanager",method=RequestMethod.POST ,produces="application/json")
	    public @ResponseBody JTableResponse getWorkpackageForTestManager(HttpServletRequest request) {
			log.info("Fetching WorkpackageForTestManager");
			JTableResponse jTableResponse = null;
			UserList user = (UserList)request.getSession().getAttribute("USER");
			
				try {
					
					List<JsonWorkPackage> jsonWorkPackage = new ArrayList<JsonWorkPackage>();				
					List<ProductMaster> listOfProducts = null;
					List<WorkPackage> wpList = new ArrayList<WorkPackage>();
					
						log.info("Get Product of test Manager......");
						listOfProducts = dataTreeService.getUserAssociatedProducts(user.getUserRoleMaster().getUserRoleId(),user.getUserId(),1);
						for (ProductMaster product : listOfProducts) {
							wpList.addAll(workPackageService.listActiveWorkPackages( product.getProductId()));
						}
					
					if (wpList == null || wpList.isEmpty()) {
						jTableResponse = new JTableResponse("OK", jsonWorkPackage, 0);
					} else {
						for (WorkPackage wp : wpList) {
							jsonWorkPackage.add(new JsonWorkPackage(wp));
						}
						
						jTableResponse = new JTableResponse("OK", jsonWorkPackage, jsonWorkPackage.size());
						wpList = null;
					}
		        } catch (Exception e) {
		            jTableResponse = new JTableResponse("ERROR","Error fetching records!");
		            log.error("JSON ERROR", e);	            
		        }
	        return jTableResponse;
	    }	

		
		@RequestMapping(value="administration.workPackage.testbed",method=RequestMethod.POST ,produces="application/json")
		public @ResponseBody String listTestBedsForWorkPackage(@RequestParam Integer workPackageId) {			
			String finalResult="";
			try {
				List<JsonTestRunPlan> jsonTestRunPlanList=new ArrayList<JsonTestRunPlan>();
				JSONObject finalObj = new JSONObject();
				JSONObject tsTitle= new JSONObject();
				JSONObject pfTitle= new JSONObject();
				JSONObject tcTitle= new JSONObject();
				JSONObject rcTitle= null;
				JSONArray list = new JSONArray();
				JSONArray columnData = new JSONArray();
				JSONArray columnData1 = new JSONArray();
				JsonTestRunPlan jsonTestRunPlan=	null;
				String filterArr[]={"TestSuiteTestCase","ProductFeature"};
				List<TestCaseList> testCaseList=new ArrayList<TestCaseList>(0);
				Map<TestSuiteList,List<TestCaseList>> tsMap=new HashMap<TestSuiteList,List<TestCaseList>>(0);
				Map<ProductFeature,List<TestCaseList>> pfMap=new HashMap<ProductFeature,List<TestCaseList>>(0);
				Map<TestCaseList,Integer> tcMap=new HashMap<TestCaseList,Integer>(0);
				// Integer countForTestSuite=0;
				Integer countForTestCase=0;
				String status="";


				tcTitle.put("title", "Test Case");
				list.add(tcTitle);
				tsTitle.put("title", "Test Suite");
				list.add(tsTitle);
				pfTitle.put("title", "Feature");
				list.add(pfTitle);
				List<RunConfiguration> runConfigList=workPackageService.listRunConfigurationBywpId(workPackageId);
				for(RunConfiguration rc:runConfigList){
					rcTitle= new JSONObject();
					rcTitle.put("title", rc.getRunconfigName());
					list.add(rcTitle);

				}
				finalObj.put("COLUMNS", list);
				for(String filter:filterArr){
					columnData = new JSONArray();
					countForTestCase=0;
					List<WorkPackageTestCaseExecutionPlan> wptcplanListforTestSuite=workPackageService.listWorkPackageTestCasesExecutionPlanBywpId(workPackageId,filter);
					//
					if(wptcplanListforTestSuite.size()!=0){
						for(WorkPackageTestCaseExecutionPlan wptcPlan:wptcplanListforTestSuite){
							if(filter.equals("TestSuiteTestCase")){
								if(wptcPlan.getTestSuiteList()!=null){
									tcMap=new HashMap<TestCaseList,Integer>(0);
									if(tsMap.containsKey(wptcPlan.getTestSuiteList())){
										List<TestCaseList> tclistFromMap=tsMap.get(wptcPlan.getTestSuiteList());
										if(!tclistFromMap.contains(wptcPlan.getTestCase())){
											testCaseList.add( wptcPlan.getTestCase());
											tsMap.put(wptcPlan.getTestSuiteList(), testCaseList);
											if(++countForTestCase>1){

												columnData1.add(columnData);
												columnData = new JSONArray();

											}
											columnData.add(wptcPlan.getTestCase().getTestCaseName());
											columnData.add(wptcPlan.getTestSuiteList().getTestSuiteName());
											columnData.add("");
										}


									}else{
										testCaseList=new ArrayList<TestCaseList>(0);
										testCaseList.add( wptcPlan.getTestCase());
										tsMap.put(wptcPlan.getTestSuiteList(), testCaseList);
										if(++countForTestCase>1){

											columnData1.add(columnData);
											columnData = new JSONArray();
										}

										columnData.add(wptcPlan.getTestCase().getTestCaseName());
										columnData.add(wptcPlan.getTestSuiteList().getTestSuiteName());
										columnData.add("");
									}
								}	
								else{
									if(tcMap.containsKey(wptcPlan.getTestCase())){
										tcMap.put(wptcPlan.getTestCase(), wptcPlan.getRunConfiguration().getWorkpackageRunConfigurationId());
									}else{
										tcMap.put(wptcPlan.getTestCase(), wptcPlan.getRunConfiguration().getWorkpackageRunConfigurationId());

										if(++countForTestCase>1){
											columnData1.add(columnData);
											columnData = new JSONArray();
										}

										columnData.add(wptcPlan.getTestCase().getTestCaseName());
										columnData.add("");
										columnData.add("");
									}

								}
							}

							if(filter.equals("ProductFeature")){
								tcMap=new HashMap<TestCaseList,Integer>(0);
								if(pfMap.containsKey(wptcPlan.getFeature())){
									List<TestCaseList> tclistFromMap=pfMap.get(wptcPlan.getFeature());
									if(!tclistFromMap.contains(wptcPlan.getTestCase())){
										testCaseList.add( wptcPlan.getTestCase());
										pfMap.put(wptcPlan.getFeature(), testCaseList);
										if(++countForTestCase>1){
											columnData1.add(columnData);
											columnData = new JSONArray();

										}
										columnData.add(wptcPlan.getTestCase().getTestCaseName());
										columnData.add("");
										columnData.add(wptcPlan.getFeature().getProductFeatureName());
									}


								}else{
									testCaseList=new ArrayList<TestCaseList>(0);
									testCaseList.add( wptcPlan.getTestCase());
									pfMap.put(wptcPlan.getFeature(), testCaseList);
									if(++countForTestCase>1){
										columnData1.add(columnData);
										columnData = new JSONArray();
									}

									columnData.add(wptcPlan.getTestCase().getTestCaseName());
									columnData.add("");
									columnData.add(wptcPlan.getFeature().getProductFeatureName());
								}
							}


							for(RunConfiguration rc:runConfigList){
								if(rc.getRunconfigId().equals(wptcPlan.getRunConfiguration().getRunconfiguration().getRunconfigId())){

									TestCaseExecutionResult tcres=wptcPlan.getTestCaseExecutionResult();
									status="Not Executed";
									if(tcres!=null ){
										if(tcres.getResult().equalsIgnoreCase(IDPAConstants.EXECUTION_RESULT_PASSED) ){
											status="Pass";
										}else if(tcres.getResult().equalsIgnoreCase(IDPAConstants.EXECUTION_RESULT_FAILED)  ){
											status="Fail";
										}else if(tcres.getResult().equalsIgnoreCase(IDPAConstants.EXECUTION_RESULT_NORUN) ){
											status="No Run";
										}else if(tcres.getResult().equalsIgnoreCase(IDPAConstants.EXECUTION_RESULT_BLOCKED)  ){
											status="Blocked";
										}
									}
									columnData.add(status);

									//}
									break;
								}else{
									columnData.add("");
								}
							}

						}
						columnData1.add(columnData);
						columnData = new JSONArray();
					}
				}

				finalObj.put("DATA", columnData1);

				finalResult=finalObj.toString();
				jsonTestRunPlan = null;
				return "["+finalResult+"]";

			} catch (Exception e) {
				e.printStackTrace();
				log.error("JSON ERROR", e);
			}		        
			return "["+finalResult+"]";
		}
		
		@RequestMapping(value="testcase.execution.history",method=RequestMethod.POST ,produces="application/json")
		public @ResponseBody JTableResponse listTestCaseExecutionHistory(@RequestParam Integer testCaseId,@RequestParam Integer workPackageId, @RequestParam String dataLevel, @RequestParam int jtStartIndex, @RequestParam int jtPageSize) {			
			JTableResponse jTableResponse = null;
			List<JsonWorkPackageTestCaseExecutionPlanForTester> jwptcepList = new LinkedList<JsonWorkPackageTestCaseExecutionPlanForTester>();
			JsonWorkPackageTestCaseExecutionPlanForTester jsonwtcep = null;		
			Integer totWPTCEPlanCountOfTCID = 0;
			try {
				if(testCaseId != 0){
					totWPTCEPlanCountOfTCID = workPackageService.getWPTCEPCountOfATestCaseId(testCaseId, -1, -1);
					//totWPTCEPlanCountOfTCID = workPackageService.getWPTCEPCountOfWPId(testCaseId,workPackageId);
				}
				
				
				if(totWPTCEPlanCountOfTCID !=0){
					List<Object[]> tcexecuteHistory = null;
					tcexecuteHistory = workPackageService.getTescaseExecutionHistory(testCaseId, workPackageId, dataLevel, jtStartIndex, jtPageSize);
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
									
					jTableResponse = new JTableResponse("OK",jwptcepList, totWPTCEPlanCountOfTCID);
				}else{
					jTableResponse = new JTableResponse("OK",jwptcepList, totWPTCEPlanCountOfTCID);
				}
				
				} catch (Exception e) {
		           jTableResponse = new JTableResponse("ERROR","Unable to Execution History!");
		            log.error("JSON ERROR", e);
				}
			return jTableResponse;
		}
		@RequestMapping(value="workpackage.feature.update",method=RequestMethod.POST ,produces="application/json")
		public @ResponseBody JTableResponse updateWorkPackageFeature(@ModelAttribute JsonWorkPackageFeature jsonWorkPackageFeature, BindingResult result) {
			log.info("testsuite update");
			JTableResponse jTableResponse = null;
			if(result.hasErrors()){
				jTableResponse = new JTableResponse("ERROR","Invalid Form!"); 
			}
			
			try {			
				ProductFeature feature = productListService.getByProductFeatureId(jsonWorkPackageFeature.getFeatureId());
				if(feature != null){
					if(feature.getTestCaseList().size()==0){
						 jTableResponse = new JTableResponse("ALERT","Test Case(s) not mapped to this feature. Please map test cases and perform planning");
					}else{
						WorkPackageFeature workPackageFeatureFromUI=new WorkPackageFeature();
						workPackageFeatureFromUI.setWorkpackageFeatureId(jsonWorkPackageFeature.getId());
						workPackageFeatureFromUI.setFeature(productListService.getByProductFeatureId(jsonWorkPackageFeature.getFeatureId()));
						workPackageFeatureFromUI.setWorkPackage(workPackageService.getWorkPackageById(jsonWorkPackageFeature.getWorkPackageId()));
						workPackageFeatureFromUI.setEditedDate(new Date(System.currentTimeMillis()));
						workPackageFeatureFromUI.setIsSelected(Integer.parseInt(jsonWorkPackageFeature.getIsSelected()));
						workPackageFeatureFromUI.setStatus(jsonWorkPackageFeature.getStatus());
						WorkPackageFeature updatedWorkPackageFeature = workPackageService.updateWorkPackageFeature(workPackageFeatureFromUI, jsonWorkPackageFeature);

						log.info("updated the table");
						List<JsonWorkPackageFeature> features = new ArrayList<JsonWorkPackageFeature>();

						JsonWorkPackageFeature updatedJsonWorkPackageFeature = new JsonWorkPackageFeature(updatedWorkPackageFeature);

						features.add(updatedJsonWorkPackageFeature);
						jTableResponse = new JTableResponse("OK",features,1);
					}
				}
			
		    } catch (Exception e) {
		        jTableResponse = new JTableResponse("ERROR","Unable to update the Feature selection!");
		        log.error("JSON ERROR", e);
		    }	        
	        return jTableResponse;
	    }

		@RequestMapping(value="workpackage.testSuiteplan.list",method=RequestMethod.POST ,produces="application/json")
	    public @ResponseBody JTableResponse listWorkpackageTestSuiteExecutionPlan(@RequestParam int workpackageId,@RequestParam int jtStartIndex, @RequestParam int jtPageSize,HttpServletRequest req,@RequestParam int status) {
			List<JsonWorkPackageTestSuiteExecutionPlan> jsonWorkPackageTestSuiteExecutionPlan=new ArrayList<JsonWorkPackageTestSuiteExecutionPlan>();
			JTableResponse jTableResponse = null;
			Map<String, String> searchStrings = new HashMap<String, String>();
			
			String searchTestSuiteName = req.getParameter("searchTestSuiteName");
            String searchTestSuiteDescription=req.getParameter("searchTestSuiteDescription");
            String searchECName=req.getParameter("searchECName");
            String searchDeviceName=req.getParameter("searchDeviceName");
            String searchPED=req.getParameter("searchPED");
            String searchPlannedShift = req.getParameter("searchPlannedShift");
            String searchTestLeadName=req.getParameter("searchTestLeadName");
            String searchTesterName=req.getParameter("searchTesterName");
            
            searchStrings.put("searchTestSuiteName", searchTestSuiteName);
            searchStrings.put("searchTestSuiteDescription", searchTestSuiteDescription);
            searchStrings.put("searchECName", searchECName);
            searchStrings.put("searchDeviceName", searchDeviceName);
            searchStrings.put("searchPED", searchPED);
            searchStrings.put("searchPlannedShift", searchPlannedShift);
            searchStrings.put("searchTestLeadName", searchTestLeadName);
            searchStrings.put("searchTesterName", searchTesterName);
				try {
					if(workpackageId<=0){
						jTableResponse = new JTableResponse("OK",jsonWorkPackageTestSuiteExecutionPlan,0);
					}					
					jsonWorkPackageTestSuiteExecutionPlan = workPackageService.listJsonWorkPackageTestSuiteExecutionPlan(searchStrings,workpackageId, jtStartIndex,jtPageSize,status);
					if (jsonWorkPackageTestSuiteExecutionPlan == null || jsonWorkPackageTestSuiteExecutionPlan.isEmpty()) {						
						//TODO : Initialize the workpackage with the test cases for the product
						log.info("No workpackage testcase execution plan for workpackage id:"+workpackageId  );
						jTableResponse = new JTableResponse("OK", jsonWorkPackageTestSuiteExecutionPlan, 0);
					} else {
						int totalCount=workPackageService.getTotalRecordWPTSEPCount(workpackageId,status);						
						jTableResponse = new JTableResponse("OK", jsonWorkPackageTestSuiteExecutionPlan, totalCount);
						jsonWorkPackageTestSuiteExecutionPlan = null;
					}
		        } catch (Exception e) {
		            jTableResponse = new JTableResponse("ERROR","Error fetching records!");
		            log.error("JSON ERROR", e);	   
		            e.printStackTrace();
		        }
	        return jTableResponse;
	    }


		@RequestMapping(value="workpackage.testSuiteplan.update",method=RequestMethod.POST ,produces="application/json")
		public @ResponseBody JTableResponse updateWorkPackageTestSuiteExecutionPlan(@ModelAttribute JsonWorkPackageTestSuiteExecutionPlan jsonWorkPackageTestSuiteExecutionPlan, BindingResult result) {
			
			JTableResponse jTableResponse = null;
			if(result.hasErrors()){
				jTableResponse = new JTableResponse("ERROR","Invalid Form!"); 
			}
			
			try {
				WorkPackageTestSuiteExecutionPlan workPackageTestSuiteExecutionPlanFromUI = jsonWorkPackageTestSuiteExecutionPlan.getWorkPackageTestSuiteExecutionPlan();
				
				/** Test Suite cannot be reallocated. Once the test case execution has been initiated.
				 Fix for Bug Id:  1976. 
				 Fixed by : Logeswari on 09-Jan-2016**/
				/** Start**/
				boolean isTestCaseExecutionStarted = false;
				List<WorkPackageTestCaseExecutionPlan> currentTestCaseExecutionPlanListOfSelectedFeature = null;
				if(workPackageTestSuiteExecutionPlanFromUI.getRunConfiguration() != null){
					currentTestCaseExecutionPlanListOfSelectedFeature = workPackageService.getWorkPackageTestcaseExecutionPlanByWorkPackageId(workPackageTestSuiteExecutionPlanFromUI.getWorkPackage().getWorkPackageId(), -1, workPackageTestSuiteExecutionPlanFromUI.getTestsuite().getTestSuiteId(),-1, "TestSuite",-1);
					if(currentTestCaseExecutionPlanListOfSelectedFeature != null && currentTestCaseExecutionPlanListOfSelectedFeature.size()>0){
						for(WorkPackageTestCaseExecutionPlan wptcep:currentTestCaseExecutionPlanListOfSelectedFeature){
							if(wptcep.getIsExecuted() == 1){
								isTestCaseExecutionStarted = true;
								break;
							}
							else{
								continue;
							}
						}
					}
				}
				
				if(isTestCaseExecutionStarted){
					jTableResponse = new JTableResponse("INFORMATION","Test Suite cannot be reallocated, as execution has been initiated!"); 
					/** End **/
				}else{
				
				WorkPackage workPackage=workPackageService.getWorkPackageById(workPackageTestSuiteExecutionPlanFromUI.getWorkPackage().getWorkPackageId());
				workPackageTestSuiteExecutionPlanFromUI.setTestsuite(testSuiteConfigurationService.getByTestSuiteId(workPackageTestSuiteExecutionPlanFromUI.getTestsuite().getTestSuiteId()));
				workPackageTestSuiteExecutionPlanFromUI.setWorkPackage(workPackage);
				
				
				workPackageTestSuiteExecutionPlanFromUI.setRunConfiguration(workPackageService.getWorkpackageRunConfigurationByWPTCEP(workPackageTestSuiteExecutionPlanFromUI.getRunConfiguration().getWorkpackageRunConfigurationId()));
				
			
				if(workPackageTestSuiteExecutionPlanFromUI.getTester()!=null && workPackageTestSuiteExecutionPlanFromUI.getTester().getUserId()!=null && !workPackageTestSuiteExecutionPlanFromUI.getTester().getUserId().equals("0")){
					workPackageTestSuiteExecutionPlanFromUI.setTester(userListService.getUserListById(workPackageTestSuiteExecutionPlanFromUI.getTester().getUserId()));
				}
				if(workPackageTestSuiteExecutionPlanFromUI.getTester()!=null && workPackageTestSuiteExecutionPlanFromUI.getTester().getFirstName()!=null && !workPackageTestSuiteExecutionPlanFromUI.getTester().getFirstName().equals("")){
					workPackageTestSuiteExecutionPlanFromUI.setTester(userListService.getUserListByUserName(workPackageTestSuiteExecutionPlanFromUI.getTester().getFirstName()));
				}
				
				if(workPackageTestSuiteExecutionPlanFromUI.getTestLead()!=null && workPackageTestSuiteExecutionPlanFromUI.getTestLead().getUserId()!=null && !workPackageTestSuiteExecutionPlanFromUI.getTestLead().getUserId().equals("0")){
					workPackageTestSuiteExecutionPlanFromUI.setTestLead(userListService.getUserListById(workPackageTestSuiteExecutionPlanFromUI.getTestLead().getUserId()));
				}
				if(workPackageTestSuiteExecutionPlanFromUI.getTestLead()!=null && workPackageTestSuiteExecutionPlanFromUI.getTestLead().getFirstName()!=null && !workPackageTestSuiteExecutionPlanFromUI.getTestLead().getFirstName().equals("")){
					workPackageTestSuiteExecutionPlanFromUI.setTestLead(userListService.getUserListByUserName(workPackageTestSuiteExecutionPlanFromUI.getTestLead().getFirstName()));
				}
				if(workPackageTestSuiteExecutionPlanFromUI.getPlannedWorkShiftMaster()!=null && workPackageTestSuiteExecutionPlanFromUI.getPlannedWorkShiftMaster().getShiftId()!=null && !workPackageTestSuiteExecutionPlanFromUI.getPlannedWorkShiftMaster().getShiftId().equals("0")){
					workPackageTestSuiteExecutionPlanFromUI.setPlannedWorkShiftMaster(workPackageService.getWorkShiftById(workPackageTestSuiteExecutionPlanFromUI.getPlannedWorkShiftMaster().getShiftId()));
				}

				if(workPackageTestSuiteExecutionPlanFromUI.getActualWorkShiftMaster()!=null && workPackageTestSuiteExecutionPlanFromUI.getActualWorkShiftMaster().getShiftId()!=null && !workPackageTestSuiteExecutionPlanFromUI.getActualWorkShiftMaster().getShiftId().equals("0")){
					workPackageTestSuiteExecutionPlanFromUI.setActualWorkShiftMaster(workPackageService.getWorkShiftById(workPackageTestSuiteExecutionPlanFromUI.getActualWorkShiftMaster().getShiftId()));
				}

				if(workPackageTestSuiteExecutionPlanFromUI.getPlannedWorkShiftMaster()!=null && workPackageTestSuiteExecutionPlanFromUI.getPlannedWorkShiftMaster().getShiftName()!=null && !workPackageTestSuiteExecutionPlanFromUI.getPlannedWorkShiftMaster().getShiftName().equals("")){
					workPackageTestSuiteExecutionPlanFromUI.setPlannedWorkShiftMaster(workPackageService.getWorkShiftByName(workPackageTestSuiteExecutionPlanFromUI.getPlannedWorkShiftMaster().getShiftName()));
				}

				if(workPackageTestSuiteExecutionPlanFromUI.getActualWorkShiftMaster()!=null && workPackageTestSuiteExecutionPlanFromUI.getActualWorkShiftMaster().getShiftName()!=null && !workPackageTestSuiteExecutionPlanFromUI.getActualWorkShiftMaster().getShiftName().equals("")){
					workPackageTestSuiteExecutionPlanFromUI.setActualWorkShiftMaster(workPackageService.getWorkShiftByName(workPackageTestSuiteExecutionPlanFromUI.getActualWorkShiftMaster().getShiftName()));
				}
				
				if(workPackageTestSuiteExecutionPlanFromUI.getExecutionPriority() != null && workPackageTestSuiteExecutionPlanFromUI.getExecutionPriority().getExecutionPriorityId() != null){
					workPackageTestSuiteExecutionPlanFromUI.setExecutionPriority(workPackageService.getExecutionPriorityById(workPackageTestSuiteExecutionPlanFromUI.getExecutionPriority().getExecutionPriorityId()));
				}
				
				if(workPackageTestSuiteExecutionPlanFromUI.getTestRunJob()!=null && workPackageTestSuiteExecutionPlanFromUI.getTestRunJob().getTestRunJobId()!=null)
					workPackageTestSuiteExecutionPlanFromUI.setTestRunJob(workPackageService.getTestRunJobById(workPackageTestSuiteExecutionPlanFromUI.getTestRunJob().getTestRunJobId()));	
				workPackageService.updateWorkPackageTestSuiteExecutionPlan(workPackageTestSuiteExecutionPlanFromUI);
				
				List<WorkPackageTestCaseExecutionPlan> currentWorkPackageTestCaseExecutionPlanList = null;
				currentWorkPackageTestCaseExecutionPlanList=workPackageService.getWorkPackageTestcaseExecutionPlanByWorkPackageId(workPackageTestSuiteExecutionPlanFromUI.getWorkPackage().getWorkPackageId(), -1, workPackageTestSuiteExecutionPlanFromUI.getTestsuite().getTestSuiteId(),-1, "TestSuite",workPackageTestSuiteExecutionPlanFromUI.getRunConfiguration().getRunconfiguration().getRunconfigId());
				for(WorkPackageTestCaseExecutionPlan wptcep:currentWorkPackageTestCaseExecutionPlanList){
					if(wptcep.getPlannedExecutionDate()==null){
						wptcep.setPlannedExecutionDate(DateUtility.dateFormatWithOutTimeStamp(DateUtility.sdfDateformatWithOutTime(workPackage.getPlannedStartDate())));
					}
					wptcep.setPlannedWorkShiftMaster(workPackageService.getWorkShiftByName(workPackageTestSuiteExecutionPlanFromUI.getPlannedWorkShiftMaster().getShiftName()));
					if(workPackageTestSuiteExecutionPlanFromUI.getTestLead()!=null)
						wptcep.setTestLead(workPackageTestSuiteExecutionPlanFromUI.getTestLead());
					if(workPackageTestSuiteExecutionPlanFromUI.getTester()!=null)
						wptcep.setTester(workPackageTestSuiteExecutionPlanFromUI.getTester());
					workPackageService.updateWorkPackageTestCaseExecutionPlan(wptcep);
				}
				
				if(workPackageTestSuiteExecutionPlanFromUI.getWorkPackage().getWorkFlowEvent().getWorkFlow().getStageId()!=IDPAConstants.WORKFLOW_STAGE_ID_PLANNING && workPackageTestSuiteExecutionPlanFromUI.getWorkPackage().getWorkFlowEvent().getWorkFlow().getStageValue()<20){
					WorkFlow workFlow=workPackageService.getWorkFlowByEntityIdStageId(IDPAConstants.WORKPACKAGE_ENTITY_ID, IDPAConstants.WORKFLOW_STAGE_ID_PLANNING);
					WorkFlowEvent workFlowEvent = new WorkFlowEvent();
					workFlowEvent.setEventDate(DateUtility.getCurrentTime());
					workFlowEvent.setRemarks("Planning Workapckage TestSuite :"+workPackageTestSuiteExecutionPlanFromUI.getWorkPackage().getName());
					UserList user= userListService.getUserByLoginId(IDPAConstants.ADMIN_LOGIN_ID);
					workFlowEvent.setUser(user);
					workFlowEvent.setWorkFlow(workFlow);
					workPackage.setWorkFlowEvent(workFlowEvent);
					workPackageService.addWorkFlowEvent(workFlowEvent);
					workPackageService.updateWorkPackage(workPackage);
				}
				List<JsonWorkPackageTestSuiteExecutionPlan> plannedSuite = new ArrayList<JsonWorkPackageTestSuiteExecutionPlan>();
				JsonWorkPackageTestSuiteExecutionPlan jsonWpExecPlan = new JsonWorkPackageTestSuiteExecutionPlan(workPackageTestSuiteExecutionPlanFromUI);
				jsonWpExecPlan.setProductModeId(jsonWorkPackageTestSuiteExecutionPlan.getProductModeId());
				jsonWpExecPlan.setProductModeName(jsonWorkPackageTestSuiteExecutionPlan.getProductModeName());
				plannedSuite.add(jsonWpExecPlan);
				
				jTableResponse = new JTableResponse("OK",plannedSuite,1);
			}
	    } catch (Exception e) {
	        jTableResponse = new JTableResponse("ERROR","Unable to plan the testSuite executionselection!");
	        log.error("JSON ERROR", e);
	    }
		        
	    return jTableResponse;
	    }

		@RequestMapping(value="workpackage.allocateTestSuite.update",method=RequestMethod.POST ,produces="application/json")
		public @ResponseBody JTableResponse updateAllocateTestSuite(@RequestParam String modifiedColumn,@RequestParam String modifiedValue,@RequestParam Integer rowId) {
			JTableResponse jTableResponse;
						
			try {	
				WorkPackageTestSuiteExecutionPlan wptcep=workPackageService.getWorkpackageTestSuiteExecutionPlanById(rowId);
				if(modifiedColumn.equalsIgnoreCase("PlannedExecutionDate")){
					wptcep.setPlannedExecutionDate(DateUtility.dateformatWithOutTime(modifiedValue));
					workPackageService.updateWorkPackageTestSuiteExecutionPlan(wptcep);
				}
				
				List<WorkPackageTestCaseExecutionPlan> currentWorkPackageTestCaseExecutionPlanList = null;
				currentWorkPackageTestCaseExecutionPlanList=workPackageService.getWorkPackageTestcaseExecutionPlanByWorkPackageId(wptcep.getWorkPackage().getWorkPackageId(), -1, wptcep.getTestsuite().getTestSuiteId(),-1, "Feature",wptcep.getRunConfiguration().getRunconfiguration().getRunconfigId());
				for(WorkPackageTestCaseExecutionPlan wptc:currentWorkPackageTestCaseExecutionPlanList){
					wptc.setPlannedExecutionDate(DateUtility.dateformatWithOutTime(modifiedValue));
					workPackageService.updateWorkPackageTestCaseExecutionPlan(wptc);
				}
				
//				
					List<JsonWorkPackageTestSuiteExecutionPlan> tmpList = new ArrayList();
					tmpList.add(new JsonWorkPackageTestSuiteExecutionPlan(wptcep));
					jTableResponse = new JTableResponse("OK",tmpList ,1);					
					
		        } catch (Exception e) {
		            jTableResponse = new JTableResponse("ERROR","Error updating record!");
		            log.error("JSON ERROR", e);
		            e.printStackTrace();
		        }       
	        return jTableResponse;
	    }
	

		@RequestMapping(value="workpackage.testsuiteplan.update.bulk",method=RequestMethod.POST ,produces="application/json")
		public @ResponseBody JTableSingleResponse updateWorkPackageTestSuiteExecutionPlanBulk(
	    		@RequestParam Integer testerId, @RequestParam Integer testLeadId, @RequestParam String wptcepListsFromUI, @RequestParam String plannedExecutionDate,@RequestParam Integer executionPriorityId,@RequestParam Integer shiftId) {
			
			JTableSingleResponse jTableSingleResponse = null;
			
			try {
				String[] wptcepLists = wptcepListsFromUI.split(",");
				workPackageService.updateWorkPackageTestSuiteExecutionPlan(wptcepLists,testerId,testLeadId,plannedExecutionDate,executionPriorityId,shiftId);
	
				jTableSingleResponse = new JTableSingleResponse("OK");
		    } 
			catch (Exception e) {
	            jTableSingleResponse = new JTableSingleResponse("ERROR","Unable to plan the testcase executionselection!");
	            log.error("JSON ERROR", e);	            
	        }
		        
		        
	        return jTableSingleResponse;
	    }

		@RequestMapping(value="workPackage.planfeature.status",method=RequestMethod.POST ,produces="application/json")
		public @ResponseBody JTableResponse listWorkpackageFeatureExecutionPlanningStatus(@RequestParam int workPackageId) {
			log.info("workPackage.status.summaryview"+workPackageId);
			JTableResponse jTableResponse = null;
			try {
				JsonWorkPackageTestCaseExecutionPlanStatus jsonWorkPackageTestCaseExecutionPlanStatus =null;
				WorkPackageTestCaseExecutionPlanStatusDTO workPackageTestCaseExecutionPlanStatusDTO  = workPackageService.listWorkpackageFeatureExecutionPlanningStatus(workPackageId);
					
					List<JsonWorkPackageTestCaseExecutionPlanStatus> jsonWorkPackageTestCaseExecutionPlanStatusList = new ArrayList<JsonWorkPackageTestCaseExecutionPlanStatus>();
					if(workPackageTestCaseExecutionPlanStatusDTO!=null){
						jsonWorkPackageTestCaseExecutionPlanStatus = new JsonWorkPackageTestCaseExecutionPlanStatus(workPackageTestCaseExecutionPlanStatusDTO);
						jsonWorkPackageTestCaseExecutionPlanStatusList.add(jsonWorkPackageTestCaseExecutionPlanStatus);
					}
					log.info("workPackage.plan.status : "+jsonWorkPackageTestCaseExecutionPlanStatusList.size());
					jTableResponse = new JTableResponse("OK", jsonWorkPackageTestCaseExecutionPlanStatusList, jsonWorkPackageTestCaseExecutionPlanStatusList.size());
		        } catch (Exception e) {
		            jTableResponse = new JTableResponse("ERROR","Error fetching records!");
		            log.error("JSON ERROR", e);	            
		        }
	        return jTableResponse;
		}
		
		@RequestMapping(value="workPackage.plantestsuite.status",method=RequestMethod.POST ,produces="application/json")
		public @ResponseBody JTableResponse listWorkpackageTestSuiteExecutionPlanningStatus(@RequestParam int workPackageId) {
			log.info("workPackage.status.summaryview"+workPackageId);
			JTableResponse jTableResponse = null;
			try {
				JsonWorkPackageTestCaseExecutionPlanStatus jsonWorkPackageTestCaseExecutionPlanStatus =null;
				WorkPackageTestCaseExecutionPlanStatusDTO workPackageTestCaseExecutionPlanStatusDTO  = workPackageService.listWorkpackageTestSuiteExecutionPlanningStatus(workPackageId);
					
					List<JsonWorkPackageTestCaseExecutionPlanStatus> jsonWorkPackageTestCaseExecutionPlanStatusList = new ArrayList<JsonWorkPackageTestCaseExecutionPlanStatus>();
					if(workPackageTestCaseExecutionPlanStatusDTO!=null){
						jsonWorkPackageTestCaseExecutionPlanStatus = new JsonWorkPackageTestCaseExecutionPlanStatus(workPackageTestCaseExecutionPlanStatusDTO);
						jsonWorkPackageTestCaseExecutionPlanStatusList.add(jsonWorkPackageTestCaseExecutionPlanStatus);
					}
					log.info("workPackage.plan.status : "+jsonWorkPackageTestCaseExecutionPlanStatusList.size());
					jTableResponse = new JTableResponse("OK", jsonWorkPackageTestCaseExecutionPlanStatusList, jsonWorkPackageTestCaseExecutionPlanStatusList.size());
		        } catch (Exception e) {
		            jTableResponse = new JTableResponse("ERROR","Error fetching records!");
		            log.error("JSON ERROR", e);	            
		        }
	        return jTableResponse;
		}
		
		@RequestMapping(value="administration.workpackage.feature.executionPriority",method=RequestMethod.POST ,produces="application/json")
		public @ResponseBody JTableResponse updateWorkpackageFeatureExecutionPriority(HttpServletRequest request,@RequestParam String executionPriority) {
			JTableResponse jTableResponse;
			Integer executionPriorityId=0;
			Integer wptceId=0;
			try {	
				if(executionPriority!=null && !executionPriority.equals("undefined")){
					executionPriorityId=Integer.parseInt(executionPriority.substring(0,executionPriority.indexOf("~")));
					wptceId=Integer.parseInt(executionPriority.substring(executionPriority.indexOf("~")+1));
				}else if(executionPriority.equals("undefined")) {
					jTableResponse = new JTableResponse("ERROR","Error updating executionPriority !");
			        return jTableResponse;

				}else{
					
					jTableResponse = new JTableResponse("ERROR","Error updating executionPriority !");
			        return jTableResponse;

				}
				ExecutionPriority executionPriorityEntity = workPackageService.getExecutionPriorityById(executionPriorityId);
				
				WorkPackageFeatureExecutionPlan workPackageFeatureExecutionPlan=workPackageService.getWorkpackageFeatureExecutionPlanById(wptceId);
				
				workPackageFeatureExecutionPlan.setExecutionPriority(executionPriorityEntity);
				
				workPackageService.updateWorkPackageFeatureExecutionPlan(workPackageFeatureExecutionPlan);
				
				List<WorkPackageTestCaseExecutionPlan> currentWorkPackageTestCaseExecutionPlanList = null;
				currentWorkPackageTestCaseExecutionPlanList=workPackageService.getWorkPackageTestcaseExecutionPlanByWorkPackageId(workPackageFeatureExecutionPlan.getWorkPackage().getWorkPackageId(), -1, -1,workPackageFeatureExecutionPlan.getFeature().getProductFeatureId(), "Feature",workPackageFeatureExecutionPlan.getRunConfiguration().getRunconfiguration().getRunconfigId());
				for(WorkPackageTestCaseExecutionPlan wptcep:currentWorkPackageTestCaseExecutionPlanList){
					wptcep.setExecutionPriority(executionPriorityEntity);
					workPackageService.updateWorkPackageTestCaseExecutionPlan(wptcep);
				}
				
				
				List<JsonWorkPackageFeatureExecutionPlan> tmpList = new ArrayList();
					tmpList.add(new JsonWorkPackageFeatureExecutionPlan(workPackageFeatureExecutionPlan));
					jTableResponse = new JTableResponse("OK",tmpList ,1);					
					
		        } catch (Exception e) {
		            jTableResponse = new JTableResponse("ERROR","Error updating executionPriority !");
		            log.error("JSON ERROR", e);
		            e.printStackTrace();
		        }       
	        return jTableResponse;
	    }

		
		@RequestMapping(value="administration.workpackage.testsuite.executionPriority",method=RequestMethod.POST ,produces="application/json")
		public @ResponseBody JTableResponse updateWorkpackageTestSuiteExecutionPriority(HttpServletRequest request,@RequestParam String executionPriority) {
			JTableResponse jTableResponse;
			Integer executionPriorityId=0;
			Integer wptceId=0;
			try {	
				if(executionPriority!=null && !executionPriority.equals("undefined")){
					executionPriorityId=Integer.parseInt(executionPriority.substring(0,executionPriority.indexOf("~")));
					wptceId=Integer.parseInt(executionPriority.substring(executionPriority.indexOf("~")+1));
				}else if(executionPriority.equals("undefined")) {
					jTableResponse = new JTableResponse("ERROR","Error updating executionPriority !");
			        return jTableResponse;

				}else{
					
					jTableResponse = new JTableResponse("ERROR","Error updating executionPriority !");
			        return jTableResponse;

				}
				ExecutionPriority executionPriorityEntity = workPackageService.getExecutionPriorityById(executionPriorityId);
				
				WorkPackageTestSuiteExecutionPlan workPackageTestSuiteExecutionPlan=workPackageService.getWorkpackageTestSuiteExecutionPlanById(wptceId);
				
				workPackageTestSuiteExecutionPlan.setExecutionPriority(executionPriorityEntity);
				
				workPackageService.updateWorkPackageTestSuiteExecutionPlan(workPackageTestSuiteExecutionPlan);
				
				List<WorkPackageTestCaseExecutionPlan> currentWorkPackageTestCaseExecutionPlanList = null;
				currentWorkPackageTestCaseExecutionPlanList=workPackageService.getWorkPackageTestcaseExecutionPlanByWorkPackageId(workPackageTestSuiteExecutionPlan.getWorkPackage().getWorkPackageId(), -1, workPackageTestSuiteExecutionPlan.getTestsuite().getTestSuiteId(),-1, "TestSuite",workPackageTestSuiteExecutionPlan.getRunConfiguration().getRunconfiguration().getRunconfigId());
				for(WorkPackageTestCaseExecutionPlan wptcep:currentWorkPackageTestCaseExecutionPlanList){
					wptcep.setExecutionPriority(executionPriorityEntity);
					workPackageService.updateWorkPackageTestCaseExecutionPlan(wptcep);
				}
				
				
				List<JsonWorkPackageTestSuiteExecutionPlan> tmpList = new ArrayList();
					tmpList.add(new JsonWorkPackageTestSuiteExecutionPlan(workPackageTestSuiteExecutionPlan));
					jTableResponse = new JTableResponse("OK",tmpList ,1);					
					
		        } catch (Exception e) {
		            jTableResponse = new JTableResponse("ERROR","Error updating executionPriority !");
		            log.error("JSON ERROR", e);
		            e.printStackTrace();
		        }       
	        return jTableResponse;
	    }
		
		@RequestMapping(value="workPackage.plan.tester.status",method=RequestMethod.POST ,produces="application/json")
		public @ResponseBody JTableResponse listWorkpackageExecutionTesterPlanStatus(@RequestParam int workPackageId,@RequestParam String plannedExecutionDate) {
			log.info("workPackage.status.summaryview"+workPackageId);
			JTableResponse jTableResponse = null;
			try {
				JsonWorkPackageExecutionPlanUserDetails jsonWorkPackageExecutionPlanUserDetails =null;
				List<WorkPackageExecutionPlanUserDetails> workPackageExecutionPlanUserDetails  = workPackageService.listWorkpackageExecutionPlanUserDetails(workPackageId,plannedExecutionDate,"Tester");
					
					List<JsonWorkPackageExecutionPlanUserDetails> jsonWorkPackageExecutionPlanUserDetailList = new ArrayList<JsonWorkPackageExecutionPlanUserDetails>();
					if(workPackageExecutionPlanUserDetails!=null && !workPackageExecutionPlanUserDetails.isEmpty()){
						for(WorkPackageExecutionPlanUserDetails wpepud:workPackageExecutionPlanUserDetails){
							jsonWorkPackageExecutionPlanUserDetails = new JsonWorkPackageExecutionPlanUserDetails(wpepud);
							jsonWorkPackageExecutionPlanUserDetailList.add(jsonWorkPackageExecutionPlanUserDetails);
						}
					}
					log.info("workPackage.plan.status : "+jsonWorkPackageExecutionPlanUserDetailList.size());
					jTableResponse = new JTableResponse("OK", jsonWorkPackageExecutionPlanUserDetailList, jsonWorkPackageExecutionPlanUserDetailList.size());
		        } catch (Exception e) {
		            jTableResponse = new JTableResponse("ERROR","Error fetching records!");
		            log.error("JSON ERROR", e);	            
		        }
	        return jTableResponse;
		}
		
		@RequestMapping(value="workPackage.plan.testlead.status",method=RequestMethod.POST ,produces="application/json")
		public @ResponseBody JTableResponse listWorkpackageExecutionTestLeadPlanStatus(@RequestParam int workPackageId,@RequestParam String plannedExecutionDate) {
			log.info("workPackage.status.summaryview"+workPackageId);
			JTableResponse jTableResponse = null;
			try {
				JsonWorkPackageExecutionPlanUserDetails jsonWorkPackageExecutionPlanUserDetails =null;
				List<WorkPackageExecutionPlanUserDetails> workPackageExecutionPlanUserDetails  = workPackageService.listWorkpackageExecutionPlanUserDetails(workPackageId,plannedExecutionDate,"TestLead");
					
					List<JsonWorkPackageExecutionPlanUserDetails> jsonWorkPackageExecutionPlanUserDetailList = new ArrayList<JsonWorkPackageExecutionPlanUserDetails>();
					if(workPackageExecutionPlanUserDetails!=null && !workPackageExecutionPlanUserDetails.isEmpty()){
						for(WorkPackageExecutionPlanUserDetails wpepud:workPackageExecutionPlanUserDetails){
							jsonWorkPackageExecutionPlanUserDetails = new JsonWorkPackageExecutionPlanUserDetails(wpepud);
							jsonWorkPackageExecutionPlanUserDetailList.add(jsonWorkPackageExecutionPlanUserDetails);
						}
					}
					log.info("workPackage.plan.status : "+jsonWorkPackageExecutionPlanUserDetailList.size());
					jTableResponse = new JTableResponse("OK", jsonWorkPackageExecutionPlanUserDetailList, jsonWorkPackageExecutionPlanUserDetailList.size());
		        } catch (Exception e) {
		            jTableResponse = new JTableResponse("ERROR","Error fetching records!");
		            log.error("JSON ERROR", e);	            
		        }
	        return jTableResponse;
		}
		
		public JSONObject putDataInJSStateTreeMap(long keyId, String entityName, String entityType,Integer executionId){
			String name="";
					JSONObject jsonObj = new JSONObject();
					if(entityType.equalsIgnoreCase("TestCase")){
						String arr[]=entityName.split(":");
						String testCaseId=arr[0];
						jsonObj.put("data", String.valueOf(keyId)+"~"+entityType+"~"+testCaseId);
						jsonObj.put("executionId",executionId);
					}else{
						jsonObj.put("data", String.valueOf(keyId)+"~"+entityType+"~"+entityName);
					}//
					if(entityName.length()>=27){
					if(entityType.equalsIgnoreCase("feature") || entityType.equalsIgnoreCase("testsuite") ){
						name=entityName.substring(0,26)+"...";
						
					}else if(entityType.equalsIgnoreCase("testRunjob")){
						name=entityName.substring(0,26)+"...";
					}else{
						name=entityName.substring(0,26)+"...";
					}
					jsonObj.put("text",name);
					}else{
						jsonObj.put("text",entityName);
					}
					jsonObj.put("tooltip", entityName);
					
					
					return jsonObj;
				}
				
				@RequestMapping(value="workpackage.testcase.execution.tester",method=RequestMethod.POST ,produces="application/json")
			    public @ResponseBody String listWorkpackageTestCaseExecutionViewForTester(@RequestParam int workPackageId, HttpServletRequest req,@RequestParam String nodeType,@RequestParam String plannedExecutionDate,String filter) {
					log.debug("inside workpackage.testcase.execution.tester");
					
					JSONArray finalTreeArry = new JSONArray();
					JSONArray jsonarr = new JSONArray();
					JSONArray childTreeArry = new JSONArray();
						JSONObject parentObj = new JSONObject();
						JSONObject objFinal = new JSONObject();
					
					
					List<HashMap<String,Object>> wptcepList=new ArrayList<HashMap<String,Object>>(0);
					
					List<HashMap<String,Object>> testRunJobList=new ArrayList<HashMap<String,Object>>(0);
					HashMap<String,Object> testRunJobMap=new HashMap<String,Object>(0);
					List<WorkPackageTestCaseExecutionPlan> workPackageTestCaseExecutionPlanList=null;
					String jsonTree="";
					UserList user=(UserList)req.getSession().getAttribute("USER");
					JTableResponse jTableResponse = null;
						try {
							 Set<TestRunJob> testRunJobSet=new HashSet<TestRunJob>();
							 List<TestRunJob> testRunJobListForUser=new ArrayList<TestRunJob>();
							
		                        if(filter.equals("0")){
								List<WorkpackageRunConfiguration> wphasRunConfig= workPackageService.getTestRunJobByWPTCEP(workPackageId,user.getUserId(),plannedExecutionDate);
								if(wphasRunConfig.size()!=0){
								for(WorkpackageRunConfiguration wprunconfig:wphasRunConfig){
									RunConfiguration runconfig=wprunconfig.getRunconfiguration();
									testRunJobSet=runconfig.getTestRunJobSet();
											for(TestRunJob testrunJob:testRunJobSet){
										if(!testRunJobListForUser.contains(testrunJob)){
											testRunJobListForUser.add(testrunJob);
										}
											}
								}
								for(TestRunJob testrunJob:testRunJobListForUser){
									
								RunConfiguration	runConfig=testrunJob.getRunConfiguration();
								String testRunJobName=testrunJob.getTestRunJobId()+":"+ runConfig.getRunconfigName();
								parentObj = new JSONObject();
								 childTreeArry = new JSONArray();
								
									for(WorkpackageRunConfiguration wpRunConfig:wphasRunConfig){
											if(wpRunConfig.getRunconfiguration().getRunconfigId().equals(runConfig.getRunconfigId())){
												log.info("testing===>");
												 workPackageTestCaseExecutionPlanList=workPackageService.listWorkPackageTestCasesExecutionPlan(workPackageId,user,plannedExecutionDate,filter,wpRunConfig);
												if(workPackageTestCaseExecutionPlanList.size()!=0){
													
													childTreeArry= getTestCaseExecutionByTestRunJob(childTreeArry, workPackageTestCaseExecutionPlanList,wpRunConfig.getType(), workPackageId,user.getUserId(),plannedExecutionDate,wpRunConfig.getWorkpackageRunConfigurationId());
													
													}
												
											}
											
								}
									parentObj = putDataInJSStateTreeMap(runConfig.getRunconfigId(),testRunJobName, "testRunjob",0);
									parentObj.put("Records",childTreeArry);
									jsonTree= JSONValue.toJSONString(parentObj);
									finalTreeArry.add(parentObj);
									
									
							}
								jsonTree= JSONValue.toJSONString(finalTreeArry);
		                        }
							}else{
								 workPackageTestCaseExecutionPlanList=workPackageService.listWorkPackageTestCasesExecutionPlan(workPackageId,user,plannedExecutionDate,filter,null);
		if(workPackageTestCaseExecutionPlanList.size()!=0){
			finalTreeArry= getTestCaseExecutionByTestRunJob(finalTreeArry,workPackageTestCaseExecutionPlanList,filter, workPackageId,user.getUserId(),plannedExecutionDate,null);
			 jsonTree= JSONValue.toJSONString(finalTreeArry);
		}
								
							}
							
				        } catch (Exception e) {
				            jTableResponse = new JTableResponse("ERROR","Error fetching records!");
				            log.error("JSON ERROR", e);	            
				        }
			        return jsonTree;
			    }






		public JSONArray getTestCaseExecutionByTestRunJob(JSONArray finalTreeArry,List<WorkPackageTestCaseExecutionPlan> workPackageTestCaseExecutionPlanList,String filter,int workPackageId,Integer userId,String plannedExecutionDate,Integer wpHasRunConfigId){
					int count=0;
					int testRunJobCount=0;
					Set<Object> pfSet=new HashSet<Object>(0);
					HashMap<String,Object> pfMap=new HashMap<String,Object>(0);
					List<HashMap<String,Object>> pfList=new ArrayList<HashMap<String,Object>>(0);
					HashMap<String,Object> wptcepMap=new HashMap<String,Object>(0);
					List<HashMap<String,Object>> wptcepList=new ArrayList<HashMap<String,Object>>(0);
					String jsonTree="";
					JSONObject parentObj = new JSONObject();
					JSONObject childObj = new JSONObject();
					JSONArray childTreeArry = new JSONArray();
					
					for(WorkPackageTestCaseExecutionPlan workPackageTestCaseExecutionPlan: workPackageTestCaseExecutionPlanList){
						if(filter.equals("1") || filter.equals("feature")){
							ProductFeature pf=workPackageTestCaseExecutionPlan.getFeature();
							if(pf!=null){
								
							if(!pfSet.contains(pf)){
								pfSet.add(pf);
								if(++count>1){
									parentObj.put("Records", childTreeArry);
									finalTreeArry.add(parentObj);
									parentObj = new JSONObject();
									childTreeArry = new JSONArray();
								}
								
								
								parentObj = putDataInJSStateTreeMap(pf.getProductFeatureId(), pf.getProductFeatureName(), "feature",0);
								
							}
						}
						}
						else if(filter.equals("2") || filter.equals("testsuite")){
							//pfSet=new HashSet<>();
							TestSuiteList testSuilteList=workPackageTestCaseExecutionPlan.getTestSuiteList();
							if(testSuilteList!=null){
							if(!pfSet.contains(testSuilteList)){
								
								pfSet.add(testSuilteList);
								if(++count>1){
									parentObj.put("Records", childTreeArry);
									finalTreeArry.add(parentObj);
									parentObj = new JSONObject();
									childTreeArry = new JSONArray();
								}
								parentObj = putDataInJSStateTreeMap(testSuilteList.getTestSuiteId(), testSuilteList.getTestSuiteName(), "TestSuite",0);
								
							}
						}
						}
						String testCaseExecName=workPackageTestCaseExecutionPlan.getTestCase().getTestCaseId()+":"+workPackageTestCaseExecutionPlan.getRunConfiguration().getRunconfiguration().getRunconfigName();
						Integer isExecuted=workPackageTestCaseExecutionPlan.getIsExecuted();
						childObj = putDataInJSStateTreeMap(workPackageTestCaseExecutionPlan.getId(),testCaseExecName, "TestCase",isExecuted);
						childTreeArry.add(childObj);
					
				}

					if(filter.equals("testcase") ){
						childObj = putDataInJSStateTreeMap(0,"Test Case", "Test Cases",0);
						childObj.put("Records", childTreeArry);
						finalTreeArry.add(childObj);
						return finalTreeArry;
						
					}else if(filter.equals("testsuite") || filter.equals("feature")) {
							parentObj.put("Records", childTreeArry);
							finalTreeArry.add(parentObj);
							jsonTree= JSONValue.toJSONString(finalTreeArry);
							return finalTreeArry;
						
					}
					else if(filter.equals("3")){
						return childTreeArry;
					}else{
						parentObj.put("Records", childTreeArry);
						finalTreeArry.add(parentObj);
						return finalTreeArry;
					}
				}
		
		@RequestMapping(value="defects.review.and.approval.update",method=RequestMethod.POST ,produces="application/json")
		public @ResponseBody JTableResponse reviewAndApproveDefects(HttpServletRequest req,@ModelAttribute JsonTestExecutionResultBugList jsonTestExecutionResultBugList, BindingResult result) {
				JTableResponse jTableResponse;
				if(result.hasErrors()){
					jTableResponse = new JTableResponse("ERROR","Invalid Form!"); 
				}			
				try {			
						TestExecutionResultBugList defectFromUI = jsonTestExecutionResultBugList.getTestExecutionResultBugList();
						TestExecutionResultBugList defectFromDB = testExecutionBugsService.getByBugId(jsonTestExecutionResultBugList.getTestExecutionResultBugId());
						int isApprovedStautsFromUI = jsonTestExecutionResultBugList.getIsApproved();
						int isApprovedStautsFromDB = defectFromDB.getIsApproved();
							if(jsonTestExecutionResultBugList.getBugFilingStatusId() != null){
								int bugFilingStatusId = defectFromUI.getBugFilingStatus().getWorkFlowId();
								WorkFlow workFlow=new WorkFlow();
								workFlow=workPackageService.getWorkFlowByEntityIdWorkFlowId(IDPAConstants.ENTITY_DEFECT_ID, bugFilingStatusId);
								defectFromUI.setBugFilingStatus(workFlow);
							}
							if(jsonTestExecutionResultBugList.getApprovalRemarks() != null){
								defectFromUI.setApprovalRemarks(jsonTestExecutionResultBugList.getApprovalRemarks());
							}
							if(jsonTestExecutionResultBugList.getDefectApprovalStatusId() != null){
								DefectApprovalStatusMaster defectApprovalStatus = new DefectApprovalStatusMaster();
								defectApprovalStatus.setApprovalStatusId(jsonTestExecutionResultBugList.getDefectApprovalStatusId());
								defectFromUI.setDefectApprovalStatus(defectApprovalStatus);
							}
							if(jsonTestExecutionResultBugList.getApproversDefectSeverityId() != null){
								DefectSeverity approverDefectSeverity = new DefectSeverity();
								approverDefectSeverity.setSeverityId(jsonTestExecutionResultBugList.getApproversDefectSeverityId());
								defectFromUI.setApproversDefectSeverity(approverDefectSeverity);
							}
							
							if(jsonTestExecutionResultBugList.getIsReproducableOnLive() != null){
								defectFromUI.setIsReproducableOnLive(jsonTestExecutionResultBugList.getIsReproducableOnLive());
							}
							
							if(jsonTestExecutionResultBugList.getIsThereABugAlready() != null){
								defectFromUI.setIsThereABugAlready(jsonTestExecutionResultBugList.getIsThereABugAlready());
							}
							UserList approver = (UserList) req.getSession().getAttribute("USER");
							testExecutionBugsService.reviewAndApproveDefect(defectFromUI,approver,isApprovedStautsFromUI);
							List<JsonTestExecutionResultBugList> tmpList = new ArrayList<JsonTestExecutionResultBugList>();
							tmpList.add(jsonTestExecutionResultBugList);
				            jTableResponse = new JTableResponse("OK",tmpList ,1); 
			        } catch (Exception e) {
			            jTableResponse = new JTableResponse("ERROR","Error updating or approving Defects");
			            log.error("JSON ERROR", e);
			            e.printStackTrace();
			        }       
		        return jTableResponse;
		    }
		
		@RequestMapping(value="administration.workPackage.feature.testbed",method=RequestMethod.POST ,produces="application/json")
		public @ResponseBody String listFeaturewithTestBedsForWorkPackage(@RequestParam Integer workPackageId) {			
			String finalResult="";
			try {
				List<JsonTestRunPlan> jsonTestRunPlanList=new ArrayList<JsonTestRunPlan>();
				JSONObject finalObj = new JSONObject();
				JSONObject tcIdTitle= new JSONObject();
				JSONObject tcCodeTitle= new JSONObject();
				JSONObject tcPriorityTitle= new JSONObject();
				JSONObject tcPreConditionTitle= new JSONObject();
				JSONObject tcDescriptionTitle= new JSONObject();
				JSONObject tcExpectedResultTitle= new JSONObject();
				JSONObject tcTypeTitle= new JSONObject();
				JSONObject pfTitle= new JSONObject();
				JSONObject tcTitle= new JSONObject();
				JSONObject rcTitle= null;
				JSONArray list = new JSONArray();
				 JSONArray columnData = new JSONArray();
				JSONArray columnData1 = new JSONArray();
				JsonTestRunPlan jsonTestRunPlan=	null;
				List<TestCaseList> testCaseList=new ArrayList<TestCaseList>(0);
				 Map<TestSuiteList,List<TestCaseList>> tsMap=new HashMap<TestSuiteList,List<TestCaseList>>(0);
				 Map<ProductFeature,List<TestCaseList>> pfMap=new HashMap<ProductFeature,List<TestCaseList>>(0);
				 Map<TestCaseList,Integer> tcMap=new HashMap<TestCaseList,Integer>(0);
				 Integer countForTestCase=0;
			     String status="";
				
			     pfTitle.put("title", "Feature");
					list.add(pfTitle);
					tcIdTitle.put("title", "Test Case Id");
					list.add(tcIdTitle);
					tcCodeTitle.put("title", "Test Case Code");
					list.add(tcCodeTitle);
					tcTitle.put("title", "Test Case");
					list.add(tcTitle);
					tcPreConditionTitle.put("title", "Pre Condition");
					list.add(tcPreConditionTitle);
					tcPriorityTitle.put("title", "Priority");
						list.add(tcPriorityTitle);
						tcTypeTitle.put("title", "Type");
						list.add(tcTypeTitle);
						
				List<RunConfiguration> runConfigList=workPackageService.listRunConfigurationBywpId(workPackageId);
					for(RunConfiguration rc:runConfigList){
						rcTitle= new JSONObject();
						rcTitle.put("title", rc.getRunconfigName());
						list.add(rcTitle);
						
					}
					finalObj.put("COLUMNS", list);
						 columnData = new JSONArray();
						 countForTestCase=0;
						List<WorkPackageTestCaseExecutionPlan> wptcplanListforTestSuite=workPackageService.listWorkPackageTestCasesExecutionPlanBywpId(workPackageId,"ProductFeature");
					
						if(wptcplanListforTestSuite.size()!=0){
						for(WorkPackageTestCaseExecutionPlan wptcPlan:wptcplanListforTestSuite){
						 if("ProductFeature".equals("ProductFeature")){
							tcMap=new HashMap<TestCaseList,Integer>(0);
						if(pfMap.containsKey(wptcPlan.getFeature())){
							List<TestCaseList> tclistFromMap=pfMap.get(wptcPlan.getFeature());
							if(!tclistFromMap.contains(wptcPlan.getTestCase())){
								testCaseList.add( wptcPlan.getTestCase());
								pfMap.put(wptcPlan.getFeature(), testCaseList);
								if(++countForTestCase>1){
									 columnData1.add(columnData);
									 columnData = new JSONArray();
									
								}
								columnData.add(wptcPlan.getFeature().getProductFeatureName());
								columnData.add(wptcPlan.getTestCase().getTestCaseId());
								columnData.add(wptcPlan.getTestCase().getTestCaseCode());
								columnData.add(wptcPlan.getTestCase().getTestCaseName());
								columnData.add(wptcPlan.getTestCase().getPreconditions());
								columnData.add(wptcPlan.getTestCase().getTestCasePriority().getPriorityName());
								columnData.add(wptcPlan.getTestCase().getExecutionTypeMaster().getName());
							}
							
							
						}else{
							testCaseList=new ArrayList<TestCaseList>(0);
							testCaseList.add( wptcPlan.getTestCase());
							pfMap.put(wptcPlan.getFeature(), testCaseList);
							if(++countForTestCase>1){
								 columnData1.add(columnData);
								 columnData = new JSONArray();
							}
							columnData.add(wptcPlan.getFeature().getProductFeatureName());
							columnData.add(wptcPlan.getTestCase().getTestCaseId());
							columnData.add(wptcPlan.getTestCase().getTestCaseCode());
							columnData.add(wptcPlan.getTestCase().getTestCaseName());
							columnData.add(wptcPlan.getTestCase().getPreconditions());
							columnData.add(wptcPlan.getTestCase().getTestCasePriority().getPriorityName());
							columnData.add(wptcPlan.getTestCase().getExecutionTypeMaster().getName());
							
						}
				}
							
							
							for(RunConfiguration rc:runConfigList){
								if(rc.getRunconfigId().equals(wptcPlan.getRunConfiguration().getRunconfiguration().getRunconfigId())){
							
							TestCaseExecutionResult tcres=wptcPlan.getTestCaseExecutionResult();
							  status="Not Executed";
								if(tcres!=null ){
									if(tcres.getResult().equalsIgnoreCase(IDPAConstants.EXECUTION_RESULT_PASSED) ){
										status="Pass";
									}else if(tcres.getResult().equalsIgnoreCase(IDPAConstants.EXECUTION_RESULT_FAILED)  ){
										status="Fail";
									}else if(tcres.getResult().equalsIgnoreCase(IDPAConstants.EXECUTION_RESULT_NORUN) ){
										status="No Run";
									}else if(tcres.getResult().equalsIgnoreCase(IDPAConstants.EXECUTION_RESULT_BLOCKED) ){
										status="Blocked";
									}
								}
								columnData.add(status);
							break;
							}
						}
							
						}
						 columnData1.add(columnData);
						 columnData = new JSONArray();
					}
					finalObj.put("DATA", columnData1);
					
				
				finalResult=finalObj.toString();
				jsonTestRunPlan = null;
			    return "["+finalResult+"]";
								
				} catch (Exception e) {
		            log.error("JSON ERROR", e);
		        }		        
		    return "["+finalResult+"]";
		}
		
		@RequestMapping(value="administration.workPackage.featureResByTestBed",method=RequestMethod.POST ,produces="application/json")
		public @ResponseBody String listFeatureTestBedsForWorkPackage(@RequestParam Integer workPackageId) {			
			String finalResult="";
			try {
				log.info("administration.workPackage.featureResByTestBed");
				
				JSONObject finalObj = new JSONObject();
				JSONObject rcTitle= null;
				JSONArray list = new JSONArray();
				 JSONArray columnData = new JSONArray();
				List<RunConfiguration> runConfigList=workPackageService.listRunConfigurationBywpId(workPackageId);
				int totalEnvComb=runConfigList.size();
				if(totalEnvComb!=0){
					for(RunConfiguration rc:runConfigList){
						rcTitle= new JSONObject();
						rcTitle.put("title", rc.getRunconfigName());
						list.add(rcTitle);
						
					}
					columnData=workPackageService.listFeaturesByTestRunBeds(workPackageId,runConfigList);
				}
					
					finalObj.put("COLUMNS", list);
					
					
					finalObj.put("DATA", columnData);
					
				finalResult=finalObj.toString();
			    return "["+finalResult+"]";
								
				} catch (Exception e) {
		            log.error("JSON ERROR", e);
		        }		        
		    return "["+finalResult+"]";
		}
		
		
		@RequestMapping(value="feature.testbeds.list",method=RequestMethod.POST ,produces="application/json")
	    public @ResponseBody JTableResponse listTestBedsByFeature(@RequestParam int workpackageId,@RequestParam int featureId, @RequestParam int jtStartIndex, 
	    		@RequestParam int jtPageSize) {			
			JTableResponse jTableResponse=null;;			 
			try {
				JsonRunConfiguration jsonRunConfiguration=null;
				Set<RunConfiguration> runConfigurationsActive = workPackageService.listTestBedByFeature(featureId, workpackageId,1);
				Set<RunConfiguration> runConfigurationsInActive = workPackageService.listTestBedByFeature(featureId, workpackageId,0);
				List<JsonRunConfiguration> jsonRunConfigurations=new ArrayList<JsonRunConfiguration>();
				
				for(RunConfiguration runConfig:runConfigurationsActive){
					jsonRunConfiguration=	new JsonRunConfiguration(runConfig);
					jsonRunConfiguration.setStatus(1);
					jsonRunConfigurations.add(jsonRunConfiguration);
				}
				
				for(RunConfiguration runConfig:runConfigurationsInActive){
					jsonRunConfiguration=	new JsonRunConfiguration(runConfig);
					jsonRunConfiguration.setStatus(0);
					jsonRunConfigurations.add(jsonRunConfiguration);
				}
				log.info("jsonRunConfigurations size:"+jsonRunConfigurations.size());
				if(jsonRunConfigurations!=null && jsonRunConfigurations.isEmpty()){
					WorkPackage workPackage = workPackageService.getWorkPackageById(workpackageId);
					List<WorkpackageRunConfiguration> workpackageRunConfigurations =workPackageService.getWorkpackageRunConfigurationOfWPwithrcStatus(workpackageId, 1);
					log.info("workpackageRunConfigurations size:"+workpackageRunConfigurations.size());

					if(workpackageRunConfigurations!=null && !workpackageRunConfigurations.isEmpty()){
						for (WorkpackageRunConfiguration workpackageRunConfiguration : workpackageRunConfigurations) {
							if(jsonRunConfigurations.size()==0){
								jsonRunConfiguration=	new JsonRunConfiguration(workpackageRunConfiguration.getRunconfiguration());
								jsonRunConfiguration.setStatus(1);
								jsonRunConfigurations.add(jsonRunConfiguration);
							}else{
								jsonRunConfiguration=	new JsonRunConfiguration(workpackageRunConfiguration.getRunconfiguration());
								if(!jsonRunConfigurations.contains(jsonRunConfiguration)){
									jsonRunConfiguration.setStatus(1);
									jsonRunConfigurations.add(jsonRunConfiguration);
								}
							}
						}
					}
				}
				
				jTableResponse = new JTableResponse("OK", jsonRunConfigurations,jsonRunConfigurations.size());  
				
				runConfigurationsActive = null;
				runConfigurationsInActive = null;

		        } catch (Exception e) {
		            jTableResponse = new JTableResponse("ERROR","Unable to show Test Beds!");
		            log.error("JSON ERROR", e);
		        }		        
	        return jTableResponse;
	    }
		
		@RequestMapping(value="administration.testBed.plan",method=RequestMethod.POST ,produces="application/json")
        public @ResponseBody JTableResponse testBedFeature(@RequestParam Integer featureId,@RequestParam String type,@RequestParam Integer workpackageId,@RequestParam Integer runConfigId,@RequestParam Integer testSuiteId,@RequestParam Integer testcaseId,@RequestParam String sourceType) {                  
			JTableResponse jTableResponse=null;;                  
			try {
				if(type.equals("unmap")){
					if(sourceType.equalsIgnoreCase("Feature")){
						WorkPackageFeatureExecutionPlan workPackageFeatureExecutionPlan=workPackageService.getWorkpackageFeatureExecutionPlan(workpackageId, featureId, runConfigId);
						if (workPackageFeatureExecutionPlan!=null) {
							workPackageFeatureExecutionPlan.setStatus(0);
							workPackageService.updateWorkPackageFeatureExecutionPlan(workPackageFeatureExecutionPlan);
						}
						List<WorkPackageTestCaseExecutionPlan> workPackageTestcaseExecutionPlans=workPackageService.listWorkPackageTestcaseExecutionPlan(featureId, workpackageId, runConfigId,sourceType,-1,-1);

						for (WorkPackageTestCaseExecutionPlan workPackageTestCaseExecutionPlan : workPackageTestcaseExecutionPlans) {
							workPackageTestCaseExecutionPlan.setStatus(0);
							workPackageService.updateWorkPackageTestCaseExecutionPlan(workPackageTestCaseExecutionPlan);
						}
					}else if(sourceType.equalsIgnoreCase("TestSuite")){
						WorkPackageTestSuiteExecutionPlan workPackageTestSuiteExecutionPlan=workPackageService.getWorkpackageTestSuiteExecutionPlan(workpackageId, testSuiteId, runConfigId);
						if (workPackageTestSuiteExecutionPlan!=null) {
							workPackageTestSuiteExecutionPlan.setStatus(0);
							workPackageService.updateWorkPackageTestSuiteExecutionPlan(workPackageTestSuiteExecutionPlan);
						}
						List<WorkPackageTestCaseExecutionPlan> workPackageTestcaseExecutionPlans=workPackageService.listWorkPackageTestcaseExecutionPlan(-1, workpackageId, runConfigId,sourceType,testSuiteId,-1);

						for (WorkPackageTestCaseExecutionPlan workPackageTestCaseExecutionPlan : workPackageTestcaseExecutionPlans) {
							workPackageTestCaseExecutionPlan.setStatus(0);
							workPackageService.updateWorkPackageTestCaseExecutionPlan(workPackageTestCaseExecutionPlan);
						}
					}else if(sourceType.equalsIgnoreCase("TestCase")){
						List<WorkPackageTestCaseExecutionPlan> workPackageTestcaseExecutionPlans=workPackageService.listWorkPackageTestcaseExecutionPlan(-1, workpackageId, runConfigId,sourceType,-1,-1);
						for (WorkPackageTestCaseExecutionPlan workPackageTestCaseExecutionPlan : workPackageTestcaseExecutionPlans) {
							workPackageTestCaseExecutionPlan.setStatus(0);
							workPackageService.updateWorkPackageTestCaseExecutionPlan(workPackageTestCaseExecutionPlan);
						}
					}
				}else if(type.equals("map")){
					if(sourceType.equalsIgnoreCase("Feature")){
						WorkPackageFeatureExecutionPlan workPackageFeatureExecutionPlan=workPackageService.getWorkpackageFeatureExecutionPlan(workpackageId, featureId, runConfigId);
						if (workPackageFeatureExecutionPlan!=null) {
							workPackageFeatureExecutionPlan.setStatus(1);
							workPackageService.updateWorkPackageFeatureExecutionPlan(workPackageFeatureExecutionPlan);
						}
						List<WorkPackageTestCaseExecutionPlan> workPackageTestcaseExecutionPlans=workPackageService.listWorkPackageTestcaseExecutionPlan(featureId, workpackageId, runConfigId,sourceType,-1,-1);

						for (WorkPackageTestCaseExecutionPlan workPackageTestCaseExecutionPlan : workPackageTestcaseExecutionPlans) {
							workPackageTestCaseExecutionPlan.setStatus(1);
							workPackageService.updateWorkPackageTestCaseExecutionPlan(workPackageTestCaseExecutionPlan);
						}
					}else if(sourceType.equalsIgnoreCase("TestSuite")){
						WorkPackageTestSuiteExecutionPlan workPackageTestSuiteExecutionPlan=workPackageService.getWorkpackageTestSuiteExecutionPlan(workpackageId, testSuiteId, runConfigId);
						if (workPackageTestSuiteExecutionPlan!=null) {
							workPackageTestSuiteExecutionPlan.setStatus(1);
							workPackageService.updateWorkPackageTestSuiteExecutionPlan(workPackageTestSuiteExecutionPlan);
						}
						List<WorkPackageTestCaseExecutionPlan> workPackageTestcaseExecutionPlans=workPackageService.listWorkPackageTestcaseExecutionPlan(-1, workpackageId, runConfigId,sourceType,testSuiteId,-1);

						for (WorkPackageTestCaseExecutionPlan workPackageTestCaseExecutionPlan : workPackageTestcaseExecutionPlans) {
							workPackageTestCaseExecutionPlan.setStatus(1);
							workPackageService.updateWorkPackageTestCaseExecutionPlan(workPackageTestCaseExecutionPlan);
						}
					}else if(sourceType.equalsIgnoreCase("TestCase")){
						List<WorkPackageTestCaseExecutionPlan> workPackageTestcaseExecutionPlans=workPackageService.listWorkPackageTestcaseExecutionPlan(-1, workpackageId, runConfigId,sourceType,-1,-1);
						for (WorkPackageTestCaseExecutionPlan workPackageTestCaseExecutionPlan : workPackageTestcaseExecutionPlans) {
							workPackageTestCaseExecutionPlan.setStatus(1);
							workPackageService.updateWorkPackageTestCaseExecutionPlan(workPackageTestCaseExecutionPlan);
						}
					}
				}
				
				jTableResponse = new JTableResponse("OK");
			} catch (Exception e) {
				jTableResponse = new JTableResponse("ERROR","Unable to UnMap TestBeds!");
				log.error("JSON ERROR", e);
			}                   
	        	return jTableResponse;
		} 
		
		@RequestMapping(value="testsuite.testbeds.list",method=RequestMethod.POST ,produces="application/json")
	    public @ResponseBody JTableResponse listTestBedsByTestSuite(@RequestParam int workpackageId,@RequestParam int testsuiteId, @RequestParam int jtStartIndex, 
	    		@RequestParam int jtPageSize) {			
			JTableResponse jTableResponse=null;			 
			try {
				JsonRunConfiguration jsonRunConfiguration=null;
				Set<RunConfiguration> runConfigurationsActive = workPackageService.listTestBedByTestSuite(testsuiteId, workpackageId,1);
				Set<RunConfiguration> runConfigurationsInActive = workPackageService.listTestBedByTestSuite(testsuiteId, workpackageId,0);
				List<JsonRunConfiguration> jsonRunConfigurations=new ArrayList<JsonRunConfiguration>();
				
				for(RunConfiguration runConfig:runConfigurationsActive){
					jsonRunConfiguration=	new JsonRunConfiguration(runConfig);
					jsonRunConfiguration.setStatus(1);
					jsonRunConfigurations.add(jsonRunConfiguration);
				}
				
				for(RunConfiguration runConfig:runConfigurationsInActive){
					jsonRunConfiguration=	new JsonRunConfiguration(runConfig);
					jsonRunConfiguration.setStatus(0);
					jsonRunConfigurations.add(jsonRunConfiguration);
				}
				
				log.info("jsonRunConfigurations size:"+jsonRunConfigurations.size());
				if(jsonRunConfigurations!=null && jsonRunConfigurations.isEmpty()){
					WorkPackage workPackage = workPackageService.getWorkPackageById(workpackageId);
					List<WorkpackageRunConfiguration> workpackageRunConfigurations =workPackageService.getWorkpackageRunConfigurationOfWPwithrcStatus(workpackageId, 1);
					log.info("workpackageRunConfigurations size:"+workpackageRunConfigurations.size());

					if(workpackageRunConfigurations!=null && !workpackageRunConfigurations.isEmpty()){
						for (WorkpackageRunConfiguration workpackageRunConfiguration : workpackageRunConfigurations) {
							if(jsonRunConfigurations.size()==0){
								jsonRunConfiguration=	new JsonRunConfiguration(workpackageRunConfiguration.getRunconfiguration());
								jsonRunConfiguration.setStatus(1);
								jsonRunConfigurations.add(jsonRunConfiguration);
							}else{
								jsonRunConfiguration=	new JsonRunConfiguration(workpackageRunConfiguration.getRunconfiguration());
								if(!jsonRunConfigurations.contains(jsonRunConfiguration)){
									jsonRunConfiguration.setStatus(1);
									jsonRunConfigurations.add(jsonRunConfiguration);
								}
							}
						}
					}
				}
				
				jTableResponse = new JTableResponse("OK", jsonRunConfigurations,jsonRunConfigurations.size());  
				
				runConfigurationsActive = null;
				runConfigurationsInActive = null;

		        } catch (Exception e) {
		            jTableResponse = new JTableResponse("ERROR","Unable to show Test Beds!");
		            log.error("JSON ERROR", e);
		        }		        
	        return jTableResponse;
	    }
		
		@RequestMapping(value="testrunjob.listbyBuildId",method=RequestMethod.POST ,produces="application/json")
	    public @ResponseBody JTableResponse listTestRunJobByProductBuildId(@RequestParam Integer productBuildId, @RequestParam Integer workPackageType, @RequestParam int jtStartIndex, @RequestParam int jtPageSize) {
			
			List<TestRunJob> testRunJob=null;
			JTableResponse jTableResponse = null;
			List<JsonTestRunJob> jsonTestRunJobList=new ArrayList<JsonTestRunJob>();
			List<TestRunJob>testRunJobList=new ArrayList();
				try {
					if(productBuildId!=null && productBuildId!=0){
						testRunJobList=	workPackageService.getTestRunJobByBuildID(productBuildId,workPackageType,jtStartIndex, jtPageSize);
						if(testRunJobList!=null){
							for(TestRunJob testrp: testRunJobList){
								jsonTestRunJobList.add(new JsonTestRunJob(testrp));
							}
						}
					}	
					
					jTableResponse = new JTableResponse("OK", jsonTestRunJobList,jsonTestRunJobList.size() );
					
		        } catch (Exception e) {
		            jTableResponse = new JTableResponse("ERROR","Error fetching records!");
		            log.error("JSON ERROR", e);	            
		        }
	        return jTableResponse;
	    }	
		
				
		
		@RequestMapping(value="testcasesexecution.of.testrunjob.Id",method=RequestMethod.POST ,produces="application/json")
	    public @ResponseBody JTableResponse listTestCaseOfTestRunJobById(@RequestParam Integer testRunJobId, @RequestParam int jtStartIndex, @RequestParam int jtPageSize) {
			log.info("testcasesexecution.of.testrunjob.Id");
			JTableResponse jTableResponse = null;
			List<JsonTestCaseList> jsonTestCaseList=new ArrayList<JsonTestCaseList>();
			List<TestCaseList> testCaseList=new ArrayList<TestCaseList>();
			List<TestCaseDTO> testCaseDTOlist = new ArrayList<TestCaseDTO>();
		
			List<JsonWorkPackageTestCaseExecutionPlanForTester> jsonwptcepList=new ArrayList<JsonWorkPackageTestCaseExecutionPlanForTester>();
			try {						
					if(testRunJobId!=null && testRunJobId!=0){
						testCaseDTOlist = workPackageService.listTestCaseExecutionDetailsOfTRJob(testRunJobId, 0);
						
						if(testCaseDTOlist != null && testCaseDTOlist.size() >0){
							for (TestCaseDTO testCaseDTO : testCaseDTOlist) {
								jsonwptcepList.add(new JsonWorkPackageTestCaseExecutionPlanForTester(testCaseDTO, "yes"));	
							}
						}
					}
					jTableResponse = new JTableResponse("OK", jsonwptcepList,jsonwptcepList.size() );
		        } catch (Exception e) {
		            jTableResponse = new JTableResponse("ERROR","Error fetching records!");
		            log.error("JSON ERROR", e);	            
		        }
	        return jTableResponse;
	    }	
		
		@RequestMapping(value="testcasesexecution.of.wptcexplan.Id",method=RequestMethod.POST ,produces="application/json")
	    public @ResponseBody JTableResponse listTestCaseExecutionsOfWPTCEPByworkPackageId(@RequestParam Integer workPackageId, @RequestParam int jtStartIndex, @RequestParam int jtPageSize) {
			log.info("testcasesexecution.of.wptcexplan.Id");
			JTableResponse jTableResponse = null;
			List<JsonTestCaseList> jsonTestCaseList=new ArrayList<JsonTestCaseList>();
			List<TestCaseList> testCaseList=new ArrayList<TestCaseList>();
			List<TestCaseDTO> testCaseDTOlist = new ArrayList<TestCaseDTO>();
		
			List<JsonWorkPackageTestCaseExecutionPlanForTester> jsonwptcepList=new ArrayList<JsonWorkPackageTestCaseExecutionPlanForTester>();
			try {						
					if(workPackageId!=null && workPackageId!=0){
						testCaseDTOlist = workPackageService.listTestCaseExecutionDetailsOfTRJob(0, workPackageId);
						
						if(testCaseDTOlist != null && testCaseDTOlist.size() >0){
							for (TestCaseDTO testCaseDTO : testCaseDTOlist) {
								jsonwptcepList.add(new JsonWorkPackageTestCaseExecutionPlanForTester(testCaseDTO, "yes"));	
							}
						}
					}
					jTableResponse = new JTableResponse("OK", jsonwptcepList,jsonwptcepList.size() );
		        } catch (Exception e) {
		            jTableResponse = new JTableResponse("ERROR","Error fetching records!");
		            log.error("JSON ERROR", e);	            
		        }
	        return jTableResponse;
	    }	
		
		@RequestMapping(value="teststepsexecution.of.testcaseexecutionId",method=RequestMethod.POST ,produces="application/json")
	    public @ResponseBody JTableResponse listTestStepsExecutinofTCByTestRunJobById(@RequestParam Integer wptcepId, @RequestParam int jtStartIndex, @RequestParam int jtPageSize) {
			log.info("teststepsexecution.of.testcaseexecutionId");
			JTableResponse jTableResponse = null;
			List<JsonTestCaseList> jsonTestCaseList=new ArrayList<JsonTestCaseList>();
			List<TestCaseList> testCaseList=new ArrayList<TestCaseList>();
			List<TestCaseDTO> testCaseDTOlist = new ArrayList<TestCaseDTO>();
			List<TestStepExecutionResultDTO> tStepExecutionResultDTOList = new ArrayList<TestStepExecutionResultDTO>();
			
			List<JsonTestExecutionResult> jsonterList=new ArrayList<JsonTestExecutionResult>();
			try {						
					if(wptcepId!=null && wptcepId!=0){									
						tStepExecutionResultDTOList = workPackageService.listTestStepExecutionDetailsOfTCExecutionId(wptcepId);
						if(tStepExecutionResultDTOList != null && tStepExecutionResultDTOList.size() >0){
							for (TestStepExecutionResultDTO tStepExeDTOObj : tStepExecutionResultDTOList) {
								jsonterList.add(new JsonTestExecutionResult(tStepExeDTOObj));
							}	
						}
					}
					jTableResponse = new JTableResponse("OK", jsonterList,jsonterList.size() );
		        } catch (Exception e) {
		            jTableResponse = new JTableResponse("ERROR","Error fetching records!");
		            log.error("JSON ERROR", e);	            
		        }
	        return jTableResponse;
	    }	
		
		@RequestMapping(value="workpackage.executiondetails.productorbuildlevel.list",method=RequestMethod.POST ,produces="application/json")
	    public @ResponseBody JTableResponse listWPExecutionProductOrBuildLevel(@RequestParam Integer testFactoryId,@RequestParam Integer productId, @RequestParam Integer productBuildId,@RequestParam int jtStartIndex, @RequestParam int jtPageSize) {			
			JTableResponse jTableResponse = null;
			List<JsonWorkPackageTestCaseExecutionPlanForTester> jsonWPTCEP = new ArrayList<JsonWorkPackageTestCaseExecutionPlanForTester>();
				try {
					int totalWPCount = 0;
					
					if(testFactoryId > 0 || productId > 0 || productBuildId > 0){
						jsonWPTCEP = workPackageService.getWPTCExecutionSummaryByProdIdBuildId(testFactoryId,productId, productBuildId, jtStartIndex, jtPageSize, null);
						//totalWPCount = workPackageService.getWPTCExecutionSummaryCount(testFactoryId,productId, productBuildId);
						totalWPCount = workPackageService.getTotalWPCount(testFactoryId,productId, productBuildId);
					}
									
					if(jsonWPTCEP == null ){
						jTableResponse = new JTableResponse("ERROR", "No data" );	
					}				
					
					jTableResponse = new JTableResponse("OK", jsonWPTCEP,totalWPCount );					
		        } catch (Exception e) {
		            jTableResponse = new JTableResponse("ERROR","Error WPExecutionBuildLevel records!");
		            log.error("JSON ERROR", e);	            
		        }
	        return jTableResponse;
	    }	

		@RequestMapping(value="workpackage.testcase.execution.summary.build.list",method=RequestMethod.POST ,produces="application/json")
		public @ResponseBody JTableResponse getWorkPackageTestcaseExecutionBuildSummary(@RequestParam int workPackageId,@RequestParam int productBuildId, HttpServletRequest req) {
			log.debug("workpackage.testcase.execution.summary.build.list");
			JTableResponse jTableResponse = null;
			try {
				List<WorkPackageBuildTestCaseSummaryDTO> wpBuildTCSummaryDTOList = new ArrayList<WorkPackageBuildTestCaseSummaryDTO>();
				wpBuildTCSummaryDTOList=workPackageService.listWorkPackageTestCaseExecutionBuildSummary(workPackageId,productBuildId);
				
				List<JsonWorkPackageTestCaseExecutionSummary> jsonWorkPackageTestCaseExecutionSummaryList = new ArrayList<JsonWorkPackageTestCaseExecutionSummary>();
				if (wpBuildTCSummaryDTOList == null && wpBuildTCSummaryDTOList.size() ==0 ) {

					jTableResponse = new JTableResponse("OK", jsonWorkPackageTestCaseExecutionSummaryList, 0);
				} else {
					for (WorkPackageBuildTestCaseSummaryDTO workPackageBuildTestCaseSummaryDTO : wpBuildTCSummaryDTOList) {
						TestRunJob trj = workPackageService.getTestRunJobById(workPackageBuildTestCaseSummaryDTO.getTestRunJobId());
						JsonWorkPackageTestCaseExecutionSummary jstces = new JsonWorkPackageTestCaseExecutionSummary(workPackageBuildTestCaseSummaryDTO);
						if(trj != null && trj.getRunConfiguration() != null && trj.getRunConfiguration().getRunconfigName() != null)
							jstces.setEnvironmentCombination(trj.getRunConfiguration().getRunconfigName());
						if(trj != null && trj.getTestRunPlan() != null && trj.getTestRunPlan() != null){
							jstces.setTestPlanId(trj.getTestRunPlan().getTestRunPlanId());
							jstces.setAutomationMode(trj.getTestRunPlan().getAutomationMode());
							jstces.setUseIntelligentTestPlan(trj.getTestRunPlan().getUseIntelligentTestPlan());
						}
						Integer totalTCCount = workPackageService.getExecutionTCCountForJob(trj.getTestRunJobId());
						jstces.setTotalTestCaseCount(totalTCCount);
						Integer notExecutedTCCount = totalTCCount - (jstces.getPassedCount() + jstces.getFailedCount());
						jstces.setNotexecutedCount(notExecutedTCCount);
						jsonWorkPackageTestCaseExecutionSummaryList.add(jstces);
					}				
					jTableResponse = new JTableResponse("OK", jsonWorkPackageTestCaseExecutionSummaryList,jsonWorkPackageTestCaseExecutionSummaryList.size() );
					wpBuildTCSummaryDTOList = null;
				}
			} catch (Exception e) {
				jTableResponse = new JTableResponse("ERROR","Error fetching records!");
				log.error("JSON ERROR", e);	            
			}
			return jTableResponse;
		}
		
		@RequestMapping(value="trigger.jenkins.build",method=RequestMethod.POST ,produces="application/json")
	    public @ResponseBody JTableResponse triggerJenkinsBuild(@RequestParam int testRunPlanId, Map<String, String> envMap) {
			log.debug("inside administration.hostAndDevice.mappedList");
			JTableResponse jTableResponse = null;			 
			try {
				// Obtain Test Management System details for Jenkins through test run plan
				TestRunPlan trp = productListService.getTestRunPlanById(testRunPlanId);				
					if( trp != null){
						// Invoking CI tool build mechanism through TAF
						HttpClient client = new DefaultHttpClient();
						String url = envMap.get("jenkins.build.url");
						url = url + "?action="+envMap.get("action");
						HttpPost request = new HttpPost(url);
						HttpResponse response = client.execute(request);
						int statusCode = response.getStatusLine().getStatusCode();
						System.out.println("Response Code : " + statusCode);
						BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
						String line = "";
						rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
						while ((line = rd.readLine()) != null) {}
						Thread.sleep(10000);
						if(statusCode == 200 || statusCode == 201){	
							boolean flg = true;
							url = envMap.get("jenkins.status.url");
							while(flg){
								String msg = checkCIMessage(url);
								if(msg.contains("SUCCESS") || msg.contains("FAILURE"))
									flg= false;
							}	
							
							try{				
								client = new DefaultHttpClient();
								line = "";
								url = envMap.get("jenkins.lastbuild.url");
								request = new HttpPost(url);
								response = client.execute(request);		
								rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
								Map<String, String> testSuitePackData = new HashMap<String, String>();
								while ((line = rd.readLine()) != null) {
									log.info(line);
									if(line.startsWith("Test Suite ID#")){
										testSuitePackData.put("testSuiteId",line.replace("Test Suite ID#", ""));									
									}
									
									if(line.startsWith("Test Suite Path#")){
										testSuitePackData.put("testSuitePath",line.replace("Test Suite Path#", ""));
									}
									
									if(line.startsWith("Test Script Pack Source#")){
										testSuitePackData.put("testSuitePackSource",line.replace("Test Script Pack Source#", ""));
									}
									if(line.equalsIgnoreCase("END")){
										String testScriptPack = new Gson().toJson(testSuitePackData);	
										jTableResponse = new JTableResponse(testScriptPack);
									}
								}
								rd.close();
							} catch(Exception e){
								e.printStackTrace();
								jTableResponse = new JTableResponse("Failed");
							}
						}
					}							
			} catch (Exception e) {
				jTableResponse = new JTableResponse("Failed");
				log.error("JSON ERROR : Error listing Hosts Mapped", e);
			}
			return jTableResponse;
	    }
			
		private static String checkCIMessage(String url){
			String msg = "";
			try{				
				HttpClient client = new DefaultHttpClient();
				String line = "";
				HttpPost request = new HttpPost(url);
				HttpResponse response = client.execute(request);		
				BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));	
				while ((line = rd.readLine()) != null) {
					msg = line;
				}
				rd.close();
			} catch(Exception e){
				return null;
			}
			return msg;
		}
		
		@RequestMapping(value="workpackage.executiondetails.testrunplan.list",method=RequestMethod.POST ,produces="application/json")
	    public @ResponseBody JTableResponse listWPExecutionTestRunPlanLevel(@RequestParam Integer testRunPlanId,@RequestParam int jtStartIndex, @RequestParam int jtPageSize) {			
			JTableResponse jTableResponse = null;
			List<JsonWorkPackageTestCaseExecutionPlanForTester> jsonWPTCEP = new ArrayList<JsonWorkPackageTestCaseExecutionPlanForTester>();
				try {
					int totalWPCount = 0;
					
					if(testRunPlanId !=null && testRunPlanId != 0){
						jsonWPTCEP = workPackageService.getWPTCExecutionSummaryByTestRunPlanId(testRunPlanId, jtStartIndex, jtPageSize);
					}			
					
					if(jsonWPTCEP == null ){
						jTableResponse = new JTableResponse("ERROR", "No data" );	
					}				
					
					totalWPCount = jsonWPTCEP.size();
					jTableResponse = new JTableResponse("OK", jsonWPTCEP,totalWPCount );					
		        } catch (Exception e) {
		            jTableResponse = new JTableResponse("ERROR","Error WPExecutionTRPLevel records!");
		            log.error("JSON ERROR", e);	            
		        }
	        return jTableResponse;
	    }
		
		
		@RequestMapping(value = "process.tesecase.executionResult.update", method = RequestMethod.POST, produces = "application/json")
		public @ResponseBody JTableSingleResponse updateTestCaseExecutionResult(@ModelAttribute JsonTestCaseExecutionResult jsonTestCaseExecutionResult,BindingResult result, HttpServletRequest request) {
			JTableSingleResponse jTableSingleResponse;
			try {
				TestCaseExecutionResult testCaseExecutionResult = jsonTestCaseExecutionResult.getTestCaseExecutionResult();
				workPackageService.updateTestCaseExecutionResult(testCaseExecutionResult);
				
				UserList assigneeUserList=null;
				UserList reviewerUserList=null;			
				if(testCaseExecutionResult.getAssignee()!=null){
					 assigneeUserList=userListService.getUserListById(testCaseExecutionResult.getAssignee().getUserId());
				}
				if(testCaseExecutionResult.getReviewer()!=null){
					 reviewerUserList=userListService.getUserListById(testCaseExecutionResult.getReviewer().getUserId());
				}
				configurationWorkFlowService.changeInstnaceActorMapping(testCaseExecutionResult.getWorkPackageTestCaseExecutionPlan().getWorkPackage().getProductMaster().getProductId(),  IDPAConstants.ENTITY_TEST_CASE_EXECUTION_RESULT_ID, null, testCaseExecutionResult.getTestCaseExecutionResultId(), testCaseExecutionResult.getWorkflowStatus().getWorkflow().getWorkflowId(), assigneeUserList);
				jTableSingleResponse = new JTableSingleResponse("OK",new JsonTestCaseExecutionResult(testCaseExecutionResult));
			} catch (Exception e) {
				jTableSingleResponse = new JTableSingleResponse("ERROR","Error updating test case execution record!");
				log.error("JSON ERROR", e);
			}
			return jTableSingleResponse;
		}
		
	@RequestMapping(value="testrunplan.unattended.execution",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableSingleResponse executeWorkpackageUnattendeddMode(HttpServletRequest req,@RequestParam Map<String, String>  mapData) {
		JTableSingleResponse jTableSingleResponse =null;
		Integer testRunPlanId=Integer.parseInt(mapData.get("testRunPlanId"));			
		TestRunPlan testRunPlan=productListService.getTestRunPlanBytestRunPlanId(testRunPlanId);							
		UserList user=(UserList)req.getSession().getAttribute("USER");
		Integer productKey = null;
		Integer productBuildKey = Integer.parseInt(mapData.get("productBuildId"));			
		ProductBuild productBuild=productListService.getProductBuildById(Integer.parseInt(mapData.get("productBuildId")), 0);
		Integer totalTestRunPlanTestCasesCount = productListService.getTotalTestCaseCountForATestRunPlan(testRunPlanId);
		int executionTCCount = 0;
		int tcCount = 0;
		Set<TestSuiteList> testSuiteList = new HashSet<TestSuiteList>();
		Set<TestCaseList> totalTestCaseList =new HashSet<TestCaseList>();
		List<TestCaseList> testCaseList=new ArrayList<TestCaseList>();
		if(productBuild != null){
			productKey = productBuild.getProductVersion().getProductMaster().getProductId();
			log.info("productKey ***** "+productKey);
		}		
		String runconfigId= mapData.get("runconfigId");
		try {
			WorkPackage wp = workPackageService.getWorkPackageById(Integer.parseInt(mapData.get("workpackageId")));
			wp = workPackageService.executeTestRunPlanWorkPackageUnattendedMode(testRunPlan,mapData, user, req, null, wp, runconfigId);
			try{
				WorkFlowEvent workFlowEvent = new WorkFlowEvent();
				workFlowEvent.setEventDate(DateUtility.getCurrentTime());
				if(ScriptLessExecutionDTO.getJobIDs() != null && !ScriptLessExecutionDTO.getJobIDs().isEmpty())
					workFlowEvent.setRemarks(ScriptLessExecutionDTO.getJobsCount() + " Jobs created with IDs " + ScriptLessExecutionDTO.getJobIDs().substring(0, ScriptLessExecutionDTO.getJobIDs().length()-1) + System.lineSeparator() + "."); // TODO : + "The total number of testcase execution that will " + ScriptLessExecutionDTO.getTotalTestcaseExecutionsCount() + ".");
				else 
					workFlowEvent.setRemarks("No Jobs created.");
				//Added for displaying additional test plan info to the user
				if(testRunPlan.getRunConfigurationList() != null){
					Set<RunConfiguration> rcList = testRunPlan.getRunConfigurationList();
					for(RunConfiguration rc : rcList){
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
					}
				}
				
				String eventRemarks = "A total of  "+ tcCount +" testcases will be executed "+ executionTCCount+ " times across " + ScriptLessExecutionDTO.getJobIDs().split(",").length + " test jobs."
										+ System.lineSeparator() + "Test Job IDs are " +  ScriptLessExecutionDTO.getJobIDs();
				workFlowEvent.setRemarks(eventRemarks);
				//workFlowEvent.setRemarks("Workpackage ID : "+ wp.getWorkPackageId()+" is executing and its Test Plan : "+testRunPlanId+" has Job IDs are " + ScriptLessExecutionDTO.getJobIDs()+" Total Test Cases : "+totalTestRunPlanTestCasesCount);
				workFlowEvent.setUser(user);
				workFlowEvent.setWorkFlow(workPackageService.getWorkFlowByEntityIdStageId(IDPAConstants.WORKPACKAGE_ENTITY_ID,IDPAConstants.WORKFLOW_STAGE_ID_EXECUTION));
				workFlowEvent.setEntityTypeRefId(wp.getWorkPackageId());
				workPackageService.addWorkFlowEvent(workFlowEvent);
				wp.setWorkFlowEvent(workFlowEvent);
				workPackageService.updateWorkPackage(wp);
				log.info("Updated Workpackage : " +wp.getWorkPackageId());
			} catch(Exception e){
				log.error("Error while updating workflow to Workpackage ID : "+wp.getWorkPackageId());
			}
			
			String jobIds = ScriptLessExecutionDTO.getJobIDs();
			if (jobIds != null && !jobIds.trim().equalsIgnoreCase("")) {
				jTableSingleResponse = new JTableSingleResponse("OK","Test Run Plan execution initiated. Workpackage "+ wp.getWorkPackageId() + "["
								+ wp.getName() + "]"
								+ " created. Jobs are "
								+ jobIds.substring(0, jobIds.length() - 1));				
			} else {
				jTableSingleResponse = new JTableSingleResponse("OK",
						"Test Run Plan execution initiated. Workpackage "
								+ wp.getWorkPackageId() + "["
								+ wp.getName() + "]" + " created.");
			}
			
			
		} catch (Exception e) {
			jTableSingleResponse = new JTableSingleResponse("ERROR","Test Run Plan could not be executed for some reason!");
			log.error("JSON ERROR", e);
		}
        return jTableSingleResponse;			
    }
	
	@RequestMapping(value = "workpackage.summary", method = RequestMethod.POST, produces = "application/json")
	public @ResponseBody String getWorkpackageWorkflowEvent(@RequestParam Integer wpId) {	
		JSONObject finalObj = new JSONObject();
		JSONArray arr = new JSONArray();
		WorkPackage wp = null;
		TestRunPlan tplan = null;
		Integer productId = null;
		try {
			wp = workPackageService.getWorkPackageById(wpId);
			
			if (wp.getTestRunPlan() != null)
				tplan = wp.getTestRunPlan();
			
			finalObj.put("Title", "Summary : Workpackage Name : " +wp.getName()+" ;\r\n\t Test Plan Name : "+tplan.getTestRunPlanName());
			finalObj.put("productId", wp.getProductBuild().getProductVersion().getProductMaster().getProductId());
			finalObj.put("workpackageId", wpId);
			finalObj.put("workpackageName", wp.getName());
			
			productId = wp.getProductBuild().getProductVersion().getProductMaster().getProductId();
			
			if (tplan != null && tplan.getTestRunPlanId() != null)
				finalObj.put("testplanId", tplan.getTestRunPlanId());
			else
				finalObj.put("testplanId", "N/A");
			
			if (tplan != null && tplan.getTestRunPlanName() != null)
				finalObj.put("testplanName", tplan.getTestRunPlanName());
			else
				finalObj.put("testplanName", "N/A");
			
			finalObj.put("triggeredTime", "\""+wp.getCreateDate()+"\"");
			finalObj.put("currentStage", wp.getStatus());
			
			List<WorkFlowEvent> wfeList = workPackageService.workFlowEventlist(wpId);
			Map<Integer, String> wfemap = new LinkedHashMap<Integer, String>();
			
			if(wfeList != null && !wfeList.isEmpty()){
				for(WorkFlowEvent wfe : wfeList){
					if(wfe != null && wfe.getWorkFlow().getWorkFlowId() != null){
						wfemap.put(wfe.getWorkFlow().getWorkFlowId(), wfe.getRemarks());
					}						
				}		
			}			
			
			if(wfemap != null && !wfemap.isEmpty()){
				for(Integer wfId : wfemap.keySet()){
					JSONObject obj = new JSONObject();					
					if(wfId != null){
						if(wfId == 9 || wfId == 23){
							String remark = getWPSummary(productId, wpId);
							obj.put("Remarks" , remark);
						} else {
							if(wfemap.get(wfId) != null && !wfemap.get(wfId).isEmpty())					
								obj.put("Remarks", wfemap.get(wfId)); 
							else 
								obj.put("Remarks","");
						}						
						arr.add(obj);
					}
				}
			}			
			finalObj.put("Records", arr);
			finalObj.put("Result", "OK");
			finalObj.put("TotalRecordCount", wfemap.size());
		} catch (Exception e) {
			log.error("JSON ERROR", e);
		}
		return finalObj.toString();
	}
	
	
	@RequestMapping(value="testcase.metrics.details",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableResponse getTestCaseMeticslDetails(@RequestParam Integer testCaseId, HttpServletRequest req) {
		log.debug("inside testcase.metrics.details");
		UserList user=(UserList)req.getSession().getAttribute("USER");
		JTableResponse jTableResponse = null;
		List<JsonWorkPackageTestCaseExecutionPlanForTester> jsonWorkPackageTestCaseExecutionPlans = new ArrayList<JsonWorkPackageTestCaseExecutionPlanForTester>();
			try {
				req.getSession().setAttribute("user", user);					
				TestCaseDTO testcaseMetricsSummary=new TestCaseDTO();
				Integer productId=null;
				Integer ageOfTestCase=null;
				Integer totalBuild=0;
				Integer testCaseRunBuild=0;
				TestCaseList tcObj = testCaseService.getTestCaseById(testCaseId);
				if(tcObj != null) {
					productId = tcObj.getProductMaster().getProductId();
					List<TestSuiteList> testSuiteList=testSuiteConfigurationService.getTestSuitesMappedToTestCaseByTestCaseId(testCaseId, 0, 10000);
					if(testSuiteList != null && testSuiteList.size() >0 ) {
						
						for(TestSuiteList testSuite:testSuiteList) {
							
							if(testSuite.getTestRunPlanList() != null && testSuite.getTestRunPlanList().size()>0) {
								testCaseRunBuild++;
							}
						}
					}
					
					List<ProductBuild> productBuildList=productBuildDao.listBuildsByProductId(productId);
					
					if(productBuildList != null && productBuildList.size() >0) {
						totalBuild=productBuildList.size();
					}
					if(tcObj.getTestCaseCreatedDate() !=null) {
					 testcaseMetricsSummary.setTestCaseAge(DateUtility.getTestCaseAge(tcObj.getTestCaseCreatedDate()));
					} else {
						testcaseMetricsSummary.setTestCaseAge("0");
					}
				}
				
				Integer mappedFeatureCount=testCaseService.getMappedFeatureCountByTestcaseId(testCaseId);
				Integer featureCount=productListService.getFeatureListSize(productId);
				
				Double featureMappedPercentage = 0.0;
				if(featureCount != null && featureCount>0 && mappedFeatureCount != null && mappedFeatureCount >0) {
					featureMappedPercentage = (Double.valueOf(mappedFeatureCount)/Double.valueOf(featureCount))*100;
					testcaseMetricsSummary.setFeatureCoverage(mappedFeatureCount.toString()+"/"+featureCount.toString()+" ["+String.valueOf(Math.round(featureMappedPercentage))+"%]");
				} else {
					testcaseMetricsSummary.setFeatureCoverage("0/0[0%]");
				}
				
				List<WorkPackageTCEPResultSummaryDTO> wptcepResultList = testCaseListDAO.getWPTestCaseExecutionResultSummary(testCaseId, -1, productId, "productLevel");
				
				 int totalExecutedTCs=0;
				 int successCount=0;
				 int failureCount=0;
				
				if( wptcepResultList !=null && wptcepResultList.size()!=0){
					for (WorkPackageTCEPResultSummaryDTO workPackageTCEPResultSummaryDTO : wptcepResultList) {	
							String resultId=workPackageTCEPResultSummaryDTO.getResult();
						 if(resultId.equalsIgnoreCase(IDPAConstants.EXECUTION_RESULT_PASSED)  ){
							 totalExecutedTCs+=workPackageTCEPResultSummaryDTO.getTotalResultCount();
						 }else if(resultId.equalsIgnoreCase(IDPAConstants.EXECUTION_RESULT_FAILED) ){
							 failureCount+=workPackageTCEPResultSummaryDTO.getTotalResultCount();
						 }else if(resultId.equalsIgnoreCase(IDPAConstants.EXECUTION_RESULT_NORUN) ){
							 
						 }else if(resultId.equalsIgnoreCase(IDPAConstants.EXECUTION_RESULT_BLOCKED) ){
							 
						 }
					}
					totalExecutedTCs=successCount+failureCount;
				}
				
				
				Double successRatePercentage = 0.0;
				if(totalExecutedTCs>0 && failureCount >0) {
					successRatePercentage = (Double.valueOf(failureCount)/Double.valueOf(totalExecutedTCs))*100;
					testcaseMetricsSummary.setTestcaseSuccessRate(failureCount+"/"+totalExecutedTCs+" ["+String.valueOf(Math.round(successRatePercentage))+"%]");
				} else {
					testcaseMetricsSummary.setTestcaseSuccessRate("0/0[0%]");
				}
				
				Double buildCoveragePercentage = 0.0;
				if(totalBuild>0 && testCaseRunBuild >0) {
					buildCoveragePercentage = (Double.valueOf(testCaseRunBuild)/Double.valueOf(totalBuild))*100;
					testcaseMetricsSummary.setBuildCoverage(testCaseRunBuild+"/"+totalBuild+" ["+String.valueOf(Math.round(buildCoveragePercentage))+"%]");
				} else {
					testcaseMetricsSummary.setBuildCoverage("0/0[0%]");
				}
				
				jsonWorkPackageTestCaseExecutionPlans.add(new JsonWorkPackageTestCaseExecutionPlanForTester(testcaseMetricsSummary));
					
				jTableResponse = new JTableResponse("OK", jsonWorkPackageTestCaseExecutionPlans, jsonWorkPackageTestCaseExecutionPlans.size());
				
	        } catch (Exception e) {
	            jTableResponse = new JTableResponse("ERROR","Error fetching records!");
	            log.error("JSON ERROR", e);	            
	        }
        return jTableResponse;
    }
	
	@RequestMapping(value = "workpackage.summary.list", method = RequestMethod.POST, produces = "application/json")
	public @ResponseBody String getWPSummary(@RequestParam Integer productId, @RequestParam Integer wpId) {	
		String remarks = "";
		String status = "";
		List<JsonWorkPackageTestCaseExecutionPlanForTester> jsonWPTCEP = new ArrayList<JsonWorkPackageTestCaseExecutionPlanForTester>();
		try {			
			jsonWPTCEP = workPackageService.getWPTCExecutionSummaryByProdIdBuildId(-1,productId, -1, 0, 10, null);
			for(JsonWorkPackageTestCaseExecutionPlanForTester jp : jsonWPTCEP){
				if(jp.getWorkPackageId().equals(wpId)){
					if(jp.getResult() != null)
						status = jp.getResult();
					else
						status = "NA";
					remarks = "Completed Workpackage Execution."+System.lineSeparator()+"Result is "+jp.getWpStatus()+" with "+jp.getP2totalPass()+" PASSED out of "+jp.getTestCaseCountOfRunconfig()+" testcases"
						 	+System.lineSeparator()+" and "+jp.getP2totalFail()+" FAILED out of "+jp.getTestCaseCountOfRunconfig()+" testcases."+System.lineSeparator()+" Jobs completed : "+jp.getJobsCompleted()+" out of "+jp.getJobsCount()+System.lineSeparator()+". Workpackage status : "+status;	
					break;
				}
			}					
		} catch (Exception e) {
			log.error("JSON ERROR", e);
		}
		return remarks;
	}
	
	/*@RequestMapping(value = "testjob.livelog.status", method = RequestMethod.POST, produces = "application/json")
	public @ResponseBody String getWPSummary(@RequestParam Integer testJobId) {	
		String status = "";
		StringBuffer sb = new StringBuffer();
		try {			
			String filePath = CommonUtility.getCatalinaPath() + evidence_Folder + File.separator + testJobId + File.separator + IDPAConstants.EVIDENCE_LOG + File.separator + "ILCM-Terminal-Job-"+testJobId+".txt";
			if(new File(filePath).exists()) {
				LineIterator it = FileUtils.lineIterator(new File(filePath), "UTF-8");
				try {
				    while (it.hasNext()) {
				        String line = it.nextLine();
				        //Add it to the string buffer
				        sb.append(line);
				        sb.append(System.lineSeparator());
				    }
				} finally {
				    LineIterator.closeQuietly(it);
				}
				if(sb != null)
					status = sb.toString();
				log.info("Job ID : "+testJobId+" ; Live Log Message : " + status);				
			}
		} catch (Exception e) {
			log.error("JSON ERROR", e);
		}
		return status;
	}
*/	
	@RequestMapping(value="test.cycle.list",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableResponse getTetsCycleList(@RequestParam Integer testFactoryId,@RequestParam Integer productId,@RequestParam Integer productVersionId,@RequestParam Integer testPlanGroupId,@RequestParam int jtStartIndex, @RequestParam int jtPageSize) {			
		JTableResponse jTableResponse = null;
		List<JsonTestCycle> jsonTestCycleList = new ArrayList<JsonTestCycle>();
		List<TestCycle> testCycleList = new ArrayList<TestCycle>();
			try {
				testCycleList = workPackageService.getTestCycleList(testFactoryId,productId,productVersionId,testPlanGroupId,jtStartIndex,jtPageSize);
				for(TestCycle tc : testCycleList){
					jsonTestCycleList.add(new JsonTestCycle(tc));
				}
				jTableResponse = new JTableResponse("OK", jsonTestCycleList,jsonTestCycleList.size() );					
	        } catch (Exception e) {
	            jTableResponse = new JTableResponse("ERROR","Error WPExecutionTRPLevel records!");
	            log.error("JSON ERROR", e);	            
	        }
        return jTableResponse;
    }
	@RequestMapping(value="workpackage.executiondetails.testcyclelevel.list",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableResponse listWPExecutionByTestCycleLevel(@RequestParam Integer testCycleId,@RequestParam int jtStartIndex, @RequestParam int jtPageSize) {			
		JTableResponse jTableResponse = null;
		List<JsonWorkPackageTestCaseExecutionPlanForTester> jsonWPTCEP = new ArrayList<JsonWorkPackageTestCaseExecutionPlanForTester>();
			try {
				//int totalWPCount = 0;
				if(testCycleId != null){
					jsonWPTCEP = workPackageService.getWPTCExecutionSummaryByTestCycleId(testCycleId, jtStartIndex, jtPageSize);
					//totalWPCount = workPackageService.getWPTCExecutionSummaryCount(testPlanGroupId);						
				}
								
				if(jsonWPTCEP == null ){
					jTableResponse = new JTableResponse("ERROR", "No data" );	
				}				
				
				jTableResponse = new JTableResponse("OK", jsonWPTCEP,jsonWPTCEP.size() );					
	        } catch (Exception e) {
	            jTableResponse = new JTableResponse("ERROR","Error WPExecution Test Cycle Level records!");
	            log.error("JSON ERROR", e);	            
	        }
        return jTableResponse;
    }	
	
}