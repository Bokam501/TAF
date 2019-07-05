package com.hcl.atf.taf.integration.testManagementSystem.hpqc;


import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.sql.Time;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;

import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.google.gson.Gson;
import com.hcl.atf.taf.constants.IDPAConstants;
import com.hcl.atf.taf.constants.TAFConstants;
import com.hcl.atf.taf.controller.utilities.CommonUtility;
import com.hcl.atf.taf.controller.utilities.DateUtility;
import com.hcl.atf.taf.controller.utilities.FileParser;
import com.hcl.atf.taf.dao.ExecutionTypeMasterDAO;
import com.hcl.atf.taf.dao.TestCasePriorityDAO;
import com.hcl.atf.taf.dao.TestExecutionResultDAO;
import com.hcl.atf.taf.dao.TestcaseTypeMasterDAO;
import com.hcl.atf.taf.dao.WorkPackageDAO;
import com.hcl.atf.taf.integration.testManagementSystem.TAFTestManagementSystemIntegrator;
import com.hcl.atf.taf.model.DefectExportData;
import com.hcl.atf.taf.model.Evidence;
import com.hcl.atf.taf.model.ExecutionTypeMaster;
import com.hcl.atf.taf.model.ProductFeature;
import com.hcl.atf.taf.model.ProductMaster;
import com.hcl.atf.taf.model.ProductVersionListMaster;
import com.hcl.atf.taf.model.ScriptTypeMaster;
import com.hcl.atf.taf.model.TestCaseExecutionResult;
import com.hcl.atf.taf.model.TestCaseList;
import com.hcl.atf.taf.model.TestCasePriority;
import com.hcl.atf.taf.model.TestCaseStepsList;
import com.hcl.atf.taf.model.TestExecutionResult;
import com.hcl.atf.taf.model.TestExecutionResultBugList;
import com.hcl.atf.taf.model.TestExecutionResultsExportData;
import com.hcl.atf.taf.model.TestManagementSystem;
import com.hcl.atf.taf.model.TestRunConfigurationChild;
import com.hcl.atf.taf.model.TestRunJob;
import com.hcl.atf.taf.model.TestRunList;
import com.hcl.atf.taf.model.TestStepExecutionResult;
import com.hcl.atf.taf.model.TestSuiteList;
import com.hcl.atf.taf.model.TestcaseTypeMaster;
import com.hcl.atf.taf.model.WorkPackage;
import com.hcl.atf.taf.model.WorkPackageTestCaseExecutionPlan;
import com.hcl.atf.taf.mongodb.service.MongoDBService;
import com.hcl.atf.taf.service.EventsService;
import com.hcl.atf.taf.service.ExecutionTypeMasterService;
import com.hcl.atf.taf.service.ProductListService;
import com.hcl.atf.taf.service.TestExecutionBugsService;
import com.hcl.atf.taf.service.TestExecutionService;
import com.hcl.atf.taf.service.TestManagementService;
import com.hcl.atf.taf.service.TestSuiteConfigurationService;
import com.hcl.atf.taf.service.WorkPackageService;
import com.hcl.atf.taf.tfs.TFSIntegrator;
import com.hcl.atf.taf.tfs.model.TestStep;
import com.hcl.atf.taf.tfs.model.TestSuite;
import com.hcl.atf.taf.tfs.result.TFSTestBugResult;
import com.hcl.atf.taf.tfs.result.TFSTestCaseResult;
import com.hcl.atf.taf.tfs.result.TFSTestResult;
import com.hcl.atf.taf.tfs.result.TFSTestSetResult;
import com.hcl.atf.taf.tfs.result.TFSTestStepResult;
import com.hcl.connector.rest.Result.DefectResultRest;
import com.hcl.connector.rest.Result.TestCaseResultRest;
import com.hcl.connector.rest.Result.TestSetResultRest;
import com.hcl.connector.rest.Result.TestStepResultRest;
import com.hcl.connector.rest.hpqcrest.ConnectorHPQCRest;
import com.hcl.connector.rest.hpqcrest.DefectRest;
import com.hcl.connector.rest.hpqcrest.DesignStepRest;
import com.hcl.connector.rest.hpqcrest.RequirementRest;
import com.hcl.connector.rest.hpqcrest.TestCaseRest;
import com.hcl.connector.rest.hpqcrest.TestResultRest;
import com.hcl.connector.rest.hpqcrest.TestSetRest;
import com.hcl.connector.rest.types.SEVERITY_REST;
import com.hcl.connector.rest.types.STATUS_REST;
import com.hcl.hpqc.connector.ConnectorHPQC;
import com.hcl.ota.DesignStep;
import com.hcl.ota.TestCase;
import com.hcl.ota.TestResult;
import com.hcl.ota.TestSet;
import com.hcl.result.TestCaseResult;
import com.hcl.result.TestSetResult;
import com.hcl.result.TestStepResult;
import com.hcl.types.SEVERITY;
import com.hcl.types.STATUS;
import com.microsoft.tfs.core.TFSTeamProjectCollection;

import com4j.Holder;


public class HPQCIntegrator implements  TAFTestManagementSystemIntegrator{

	@Autowired
	ProductListService productService;
	@Autowired
	private ProductListService productListService;
	@Autowired
	TestSuiteConfigurationService testSuiteConfigurationService;
	
	@Autowired
	private  TestExecutionService testExecutionService;	
	
	@Autowired
	EventsService eventsService;	
	@Autowired
    private ExecutionTypeMasterDAO executionTypeMasterDAO;
	@Autowired
    private TestCasePriorityDAO testCasePriorityDAO;
	@Autowired
	private TestcaseTypeMasterDAO testcaseTypeMasterDAO;
	@Autowired
	private ExecutionTypeMasterService executionTypeMasterService;
	@Autowired
	private WorkPackageService workPackageService;
	@Autowired
	private WorkPackageDAO workPackageDAO;
	@Autowired
	private TestExecutionResultDAO testExecutionResultDAO;
	
	@Autowired
	private TestExecutionBugsService testExecutionBugsService;
	@Autowired
	private	TestManagementService testManagementService;
	@Autowired
	private MongoDBService mongoDBService;	
	
	private static final Log log = LogFactory.getLog(HPQCIntegrator.class);
	
	static final String DATA_ADD = "ADD";
	private static final String DATA_UPDATE= "UPDATE";
	private static final String DATA_ERROR = "ERROR";
	

	
	@Override
	@Transactional
	public String importTestCasesOTA(int productId, ConnectorHPQC hpqcConnector, String testCaseSource) {
		
		log.info("Starting import of test cases from HPQC for product Id : " + productId);
		String status = "";

		try {
		
			String source = testCaseSource;		
			List<TestCaseList> testCases = new ArrayList<TestCaseList>(0);
			
			log.info("checking the productService class object:"+productService);
			ProductMaster productMaster = productService.getProductById(productId);
			
			List<TestCase> hpqcTestCaseList = hpqcConnector.getTestCases("");
			//Raising event for importing testCases from HPQC
			if(hpqcTestCaseList != null && !hpqcTestCaseList.isEmpty()){
				
			}
			processTestCasesOTA(hpqcTestCaseList, productMaster, source);		
			
			status = importTestSetsOTA(productMaster, hpqcConnector );
			
		} catch (Exception e) {
			log.error("Problem while importing testcases from HPQC", e);
		}
		return status;
	}

	@Override
	@Transactional
	public String importTestSetsOTA(ProductMaster productMaster, ConnectorHPQC hpqcConnector) {
			
		TestSuiteList testSuite = new TestSuiteList();		
		List<TestSet> testSets = new ArrayList<TestSet>(0);			
		
		TestCaseList testCaseList = new TestCaseList();
		List<TestCaseList> testCases = new ArrayList<TestCaseList>();
		
		int productId = productMaster.getProductId(); 
				
		ProductVersionListMaster productVersionListMaster= testSuiteConfigurationService.getLatestProductVersion(productMaster);
		
		testSets = hpqcConnector.getTestSets("");

		if(testSets!= null && !testSets.isEmpty()){
			
			for(TestSet testSet : testSets){					
			
				if(testSet.getId().equals("0")){
					continue;
				} else{
					testCases = new ArrayList<TestCaseList>();
					
					testSuite = testSuiteConfigurationService.getByProductTestSuiteCode(productId,testSet.getId());
					if(testSuite== null){
						//The test suite does not exist. Create a new one
						log.info("New TestSuite created : " + testSet.getId() + " : " +testSet.getName());
						testSuite = new TestSuiteList();
						testSuite.setTestSuiteCode(testSet.getId());
						testSuite.setTestSuiteName(testSet.getName().trim());
						testSuite.setProductMaster(productMaster);	
						testSuite.setTestScriptSource("HPQC_TEST_PLAN");
						ExecutionTypeMaster executionTypeMasterFromUI = executionTypeMasterService.getExecutionTypeByExecutionTypeId(3);
						testSuite.setExecutionTypeMaster(executionTypeMasterFromUI);
						ScriptTypeMaster scriptTypeMaster = new ScriptTypeMaster();
						scriptTypeMaster.setScriptType("JUNIT");
						testSuite.setScriptTypeMaster(scriptTypeMaster);
						
						testSuite.setProductVersionListMaster(productVersionListMaster);	
						int testSuiteId = testSuiteConfigurationService.addTestSuite(testSuite);
						
						testSuite = testSuiteConfigurationService.getByTestSuiteId(testSuiteId);
					} else {
						//Test Suite already exists. Prepare it with updated data for saving later.
						testSuite.setTestSuiteName(testSet.getName());
						log.info("Existing TestSuite to be updated : " + testSuite.getTestSuiteCode() + " : " +testSuite.getTestSuiteName());
					}
					
					List<TestCase> hpqcTestCases = testSet.getTestCases();	
					log.info("No of testcases in testsuite : " + testSuite.getTestSuiteCode() + " : " +testSuite.getTestSuiteName() + " : " + hpqcTestCases.size());
					if(hpqcTestCases!= null && !hpqcTestCases.isEmpty()){
						
						Set<TestCaseList> testSuiteTestCases = testSuite.getTestCaseLists();
						for(TestCase  hpqcTestCase: hpqcTestCases){	
							
							testCaseList = testSuiteConfigurationService.getTestCaseByCodeProduct(hpqcTestCase.getId(), productId);
							if (testCaseList == null) {
								//The test case does not exist. Skip it.
								log.info("Testcase is an orphan. Not present in system : " + hpqcTestCase.getId() + " : " +hpqcTestCase.getName());
								continue;
							}
							if (testSuiteTestCases.contains(testCaseList)) {
								//The test case is already part of the test suite. Ignore
								log.info("Testcase is already part of testsuite : " + testCaseList.getTestCaseName());
							} else {
								testSuite.getTestCaseLists().add(testCaseList);
								testCaseList.getTestSuiteLists().add(testSuite);
								testCases.add(testCaseList);							
								log.info("Testcase is to be added to testsuite : " + testCaseList.getTestCaseName());
							}

						}
					}
					testSuiteConfigurationService.updateTestCases((testCases));
					testSuiteConfigurationService.updateTestSuiteList(testSuite);
					String eventDescription = "Imported TestSets from HPQC";
					eventsService.raiseTestSuiteImportedEvent(testSuite, getTestManagementSystem(productMaster), testCases.size(), eventDescription);
					
				}				
				
			}
		}
		return "";
	}		
	
	private void processTestCasesRest(ConnectorHPQCRest hpqcConnector, List<TestCaseRest> hpqcTestCaseList, ProductMaster productMaster,String source){
		
		List<TestCaseList> testCasesList = new ArrayList<TestCaseList>(0);
		TestCaseList testCaseList = new TestCaseList();
		
		TestCaseStepsList testCaseStep = new TestCaseStepsList();	
		Set<TestCaseStepsList> testCaseStepsSet = new HashSet<TestCaseStepsList>(0);		
		boolean isNewTestCase = false;
		
		if(hpqcTestCaseList!= null && !hpqcTestCaseList.isEmpty()){
			
			for(TestCaseRest hpqcTestCase : hpqcTestCaseList){
				log.debug("testcasecode>>"+hpqcTestCase.getTestcaseID());
				//Check if this test case already exists, If not, create a new one. Else, update the existing one
				TestCaseList existingTestCase = testSuiteConfigurationService.getTestCaseByCodeProduct(hpqcTestCase.getTestcaseID(), productMaster.getProductId());
				if (existingTestCase == null) {
					log.debug("New testcase created : " + hpqcTestCase.getTitle());
					testCaseList = new TestCaseList();
					//This is a new testcase from HPQC. Create one in TAF			
					testCaseList = convertToTestCaseFromHPQCDataRest(testCaseList, hpqcTestCase, productMaster, source);
					int newTestCaseId = testSuiteConfigurationService.addTestCase(testCaseList);
					testCaseList.setTestCaseExecutionOrder(newTestCaseId);
					testSuiteConfigurationService.updateTestCase(testCaseList);
					log.debug("testcase testcasecode>>"+testCaseList.getTestCaseCode());
					//TODO : Check if the saved object has an Id. If yes, delete the next line
					testCaseList = testSuiteConfigurationService.getByTestCaseId(newTestCaseId);
					isNewTestCase = true;
					
				} else {
				
					//This testcase already exists in TAF. Just update it 
					existingTestCase = convertToTestCaseFromHPQCDataRest(existingTestCase, hpqcTestCase, productMaster, source);
					testSuiteConfigurationService.updateTestCase(existingTestCase);
					testCaseList = testSuiteConfigurationService.getByTestCaseId(existingTestCase.getTestCaseId());
					isNewTestCase = false;
					log.debug("Existing test case updated : " + existingTestCase.getTestCaseCode() + " : " +existingTestCase.getTestCaseName());

				}
				//Process the test steps for the test case
				try{
					List<DesignStepRest> hpqcTestStepList = hpqcConnector.getDesignStepsList(hpqcTestCase.getTestcaseID());
					
					if(hpqcTestStepList!= null && !hpqcTestStepList.isEmpty()){
						
						for(DesignStepRest hpqcTestStep : hpqcTestStepList){
							log.debug("hpqcTestStep:"+hpqcTestStep.getDesignStepID());
							//Check whether this teststep is already in TAF. If not, create a new one. Else, just update the existing one.
							//If the test case is a new one, the test step will also be a new one.
							//Check if the teststep already exists for the test case
							TestCaseStepsList existingTestStep = testSuiteConfigurationService.getTestCaseStepsByCodeAndProduct(hpqcTestStep.getDesignStepID(), productMaster.getProductId());
							if (isNewTestCase || (existingTestStep == null)) {
								
								log.debug("New test step created : " + hpqcTestStep.getDesignStepID() + " : " + hpqcTestStep.getName());
								testCaseStep = new TestCaseStepsList();	
								//Either the test case is new or the test step does not exist. In either case, create a new test step
								testCaseStep = convertToTestStepFromHPQCDataRest(testCaseStep, hpqcTestStep, source);
								testCaseStep.setTestCaseList(testCaseList);							
								testSuiteConfigurationService.addTestCaseStep(testCaseStep);
								//TODO : Check if the below line is needed
								testSuiteConfigurationService.updateTestCase(testCaseList);
							} else {
	
								//The test step already exists. Just update it.
								existingTestStep = convertToTestStepFromHPQCDataRest(existingTestStep, hpqcTestStep, source);
								existingTestStep.setTestCaseList(testCaseList);
								testSuiteConfigurationService.updateTestCaseSteps(existingTestStep);
								log.debug("Existing test step updated : " + existingTestStep.getTestStepCode() + " : " + existingTestStep.getTestStepName());
							}
						}						
					}
				} catch(Exception e){
					log.info("Error in getting test steps", e);
				}
			}
			String eventDescription = "Imported testcases from HPQC";
			eventsService.raiseTestCasesImportedEvent(productMaster, getTestManagementSystem(productMaster), hpqcTestCaseList.size(), eventDescription);
		}
	}
	
	private TestCaseList convertToTestCaseFromHPQCDataRest(TestCaseList testCase, TestCaseRest hpqcTestCase, ProductMaster product, String source) {
		
		testCase.setTestCaseCode(hpqcTestCase.getTestcaseID().trim());
		testCase.setTestCaseName(hpqcTestCase.getTitle().trim());
		testCase.setTestCaseScriptFileName(hpqcTestCase.getTitle().trim());
		testCase.setTestCaseDescription(hpqcTestCase.getDescription().replaceAll("\\<.*?\\>", "").replaceAll("\\s", " ").trim());
		testCase.setTestcaseTypeMaster(testcaseTypeMasterDAO.getTestcaseTypeMasterBytestcaseTypeId(IDPAConstants.TESTCASE_TYPE_FUNCTIONAL));
		
		testCase.setTestCasePriority(testCasePriorityDAO.getTestCasePriorityBytestcasePriorityId(IDPAConstants.TESTCASE_DEFAULT_PRIORITY_ID));
		testCase.setExecutionTypeMaster(executionTypeMasterService.getExecutionTypeByExecutionTypeId(IDPAConstants.TESTCASE_EXECUTION_AUTOMATION));
		testCase.setTestCaseSource(source);	
		// To update custom test case details
		testCase.setStatus(1);
		testCase.setProductMaster(product);		
		testCase = getParsedTestCaseDataFromHPQCOTA(testCase, testCase.getTestCaseDescription());
		return testCase;
	}
	
	
	

	private TestCaseStepsList convertToTestStepFromHPQCDataRest(TestCaseStepsList testCaseStep, DesignStepRest hpqcTestStep, String source) {
		
		testCaseStep.setTestStepCode(hpqcTestStep.getDesignStepID().trim());
		testCaseStep.setTestStepDescription(hpqcTestStep.getDescription().replaceAll("\\<.*?\\>", "").replaceAll("\\s", " ").trim());
		testCaseStep.setTestStepName(hpqcTestStep.getName().trim());		
		testCaseStep.setTestStepExpectedOutput(hpqcTestStep.getExpectedResult().replaceAll("\\<.*?\\>", "").replaceAll("\\s", " ").trim());//Fix for Bug 839 - HTML tags display in report		
		testCaseStep.setTestStepInput(hpqcTestStep.getDescription().replaceAll("\\<.*?\\>", "").replaceAll("\\s", " ").trim());
		testCaseStep.setTestStepSource(source);
		
		return testCaseStep;
	}
	private void processTestCasesOTA(List<TestCase> hpqcTestCaseList, ProductMaster productMaster,String source){
			
		List<TestCaseList> testCasesList = new ArrayList<TestCaseList>(0);
		TestCaseList testCaseList = new TestCaseList();
		
		TestCaseStepsList testCaseStep = new TestCaseStepsList();	
		Set<TestCaseStepsList> testCaseStepsSet = new HashSet<TestCaseStepsList>(0);		
		boolean isNewTestCase = false;
		
		if(hpqcTestCaseList!= null && !hpqcTestCaseList.isEmpty()){
			
			for(TestCase hpqcTestCase : hpqcTestCaseList){
				
				//Check if this test case already exists, If not, create a new one. Else, update the existing one
				TestCaseList existingTestCase = testSuiteConfigurationService.getTestCaseByCodeProduct(hpqcTestCase.getId(), productMaster.getProductId());
				if (existingTestCase == null) {
					log.debug("New testcase created : " + hpqcTestCase.getName());
					testCaseList = new TestCaseList();
					//This is a new testcase from HPQC. Create one in TAF			
					testCaseList = convertToTestCaseFromHPQCDataOTA(testCaseList, hpqcTestCase, productMaster, source);
					int newTestCaseId = testSuiteConfigurationService.addTestCase(testCaseList);
					//TODO : Check if the saved object has an Id. If yes, delete the next line
					testCaseList = testSuiteConfigurationService.getByTestCaseId(newTestCaseId);
					isNewTestCase = true;
					
				} else {
				
					//This testcase already exists in TAF. Just update it 
					existingTestCase = convertToTestCaseFromHPQCDataOTA(existingTestCase, hpqcTestCase, productMaster, source);
					testSuiteConfigurationService.updateTestCase(existingTestCase);
					testCaseList = testSuiteConfigurationService.getByTestCaseId(existingTestCase.getTestCaseId());
					isNewTestCase = false;
					log.debug("Existing test case updated : " + existingTestCase.getTestCaseCode() + " : " +existingTestCase.getTestCaseName());

				}
				
				//Process the test steps for the test case
				List<DesignStep> hpqcTestStepList = hpqcTestCase.getDesignSteps("");
				
				if(hpqcTestStepList!= null && !hpqcTestStepList.isEmpty()){
					
					for(DesignStep hpqcTestStep : hpqcTestStepList){
					
						//Check whether this teststep is already in TAF. If not, create a new one. Else, just update the existing one.
						//If the test case is a new one, the test step will also be a new one.
						//Check if the teststep already exists for the test case
						TestCaseStepsList existingTestStep = testSuiteConfigurationService.getTestCaseStepsByCodeAndProduct(hpqcTestStep.getId(), productMaster.getProductId());
						if (isNewTestCase || (existingTestStep == null)) {
							
							log.debug("New test step created : " + hpqcTestStep.getId() + " : " + hpqcTestStep.getName());
							testCaseStep = new TestCaseStepsList();	
							//Either the test case is new or the test step does not exist. In either case, create a new test step
							testCaseStep = convertToTestStepFromHPQCDataOTA(testCaseStep, hpqcTestStep, source);
							testCaseStep.setTestCaseList(testCaseList);							
							testSuiteConfigurationService.addTestCaseStep(testCaseStep);
							//TODO : Check if the below line is needed
							testSuiteConfigurationService.updateTestCase(testCaseList);
						} else {

							//The test step already exists. Just update it.
							existingTestStep = convertToTestStepFromHPQCDataOTA(existingTestStep, hpqcTestStep, source);
							existingTestStep.setTestCaseList(testCaseList);
							testSuiteConfigurationService.updateTestCaseSteps(existingTestStep);
							log.debug("Existing test step updated : " + existingTestStep.getTestStepCode() + " : " + existingTestStep.getTestStepName());
						}
					}						
				}
			}
			String eventDescription = "Imported testcases from HPQC";
			eventsService.raiseTestCasesImportedEvent(productMaster, getTestManagementSystem(productMaster), hpqcTestCaseList.size(), eventDescription);
		}
	}
	
	private TestCaseList convertToTestCaseFromHPQCDataOTA(TestCaseList testCase, TestCase hpqcTestCase, ProductMaster product, String source) {
		
		testCase.setTestCaseCode(hpqcTestCase.getId());
		testCase.setTestCaseName(hpqcTestCase.getName().trim());
		testCase.setTestCaseDescription(hpqcTestCase.getDescription().replaceAll("\\<.*?\\>", "").replaceAll("\\s", " "));
		testCase.setTestCaseType(hpqcTestCase.getType());
		TestCasePriority tcp = new TestCasePriority();
		tcp=testCasePriorityDAO.getTestCasePriorityBytestcasePriorityId(3);
		testCase.setTestCasePriority(tcp);
		ExecutionTypeMaster executionTypeMaster=executionTypeMasterDAO.getExecutionTypeByExecutionTypeId(1);
		testCase.setExecutionTypeMaster(executionTypeMaster);
		TestcaseTypeMaster testcaseTypeMaster=testcaseTypeMasterDAO.getTestcaseTypeMasterBytestcaseTypeId(1);
		testCase.setTestcaseTypeMaster(testcaseTypeMaster);
		testCase.setTestCaseSource(source);
		testCase.setProductMaster(product);		
		testCase = getParsedTestCaseDataFromHPQCOTA(testCase, testCase.getTestCaseDescription());
		return testCase;
	}
	
	private TestCaseStepsList convertToTestStepFromHPQCDataOTA(TestCaseStepsList testCaseStep, DesignStep hpqcTestStep, String source) {
		
		testCaseStep.setTestStepCode(hpqcTestStep.getId());
		testCaseStep.setTestStepDescription(hpqcTestStep.getDescription().replaceAll("\\<.*?\\>", "").replaceAll("\\s", " "));
		testCaseStep.setTestStepName(hpqcTestStep.getName().trim());
		testCaseStep.setTestStepExpectedOutput(hpqcTestStep.getExpectedResult());
		testCaseStep.setTestStepInput(hpqcTestStep.getDescription().replaceAll("\\<.*?\\>", "").replaceAll("\\s", " "));
		testCaseStep.setTestStepSource(source);
		
		return testCaseStep;
	}

		@Override
		@Transactional
		public String importTestSetsOTA(int testRunNo, int testRunConfigChild,
				ConnectorHPQC hpqcConnector) {
			return null;
		}

		
	@Override
	@Transactional
	public String ExportTestExecutionResultsOTA(int testRunListId, ConnectorHPQC hpqcConnector) {
		
		log.debug("Export Test Execution Results");
		
		TestManagementSystem testManagementSystem;
		String status = "";
		TestRunList testRunList = null;
		TestRunConfigurationChild testRunConfigChild = null;
		TestSuiteList testSuiteList = null;
		Set<TestCaseList> testCaseLists = null;		
		List<TestExecutionResult> testExecutionResultsForTestCase = null;
		TestExecutionResultsExportData testExecutionResultsExportData = new TestExecutionResultsExportData();
		
		boolean isTestCasePassed = true;
		List<TestStepResult> testStepResults = new ArrayList<TestStepResult>();
		List<TestCaseResult> testCaseResults =  new ArrayList<TestCaseResult>(); 
		TestSetResult testSetResult = new TestSetResult();
		Time testCaseDurationStart;
		Time testCaseDurationEnd;
		
		testRunList = testExecutionService.getByTestRunListId(testRunListId);
		List<TestExecutionResult> testExecutionResults   = testExecutionService.listTestExecutionResult(testRunListId);		
		
		testRunConfigChild = testRunList.getTestRunConfigurationChild();
		testSuiteList = testRunConfigChild.getTestSuiteList();
		testCaseLists = testSuiteList.getTestCaseLists();		
	
		testSetResult.setTestsetID(testSuiteList.getTestSuiteCode());
		
		try{
			for(TestCaseList testCaseList : testCaseLists){
				
				isTestCasePassed = true;
				TestCaseResult testCaseResult = new TestCaseResult();
				testStepResults = new ArrayList<TestStepResult>();
				testCaseDurationStart = null;
				testCaseDurationEnd = null;
				
				testExecutionResultsForTestCase = testExecutionService.getTestExecResultsForTestCase(testRunListId, testCaseList.getTestCaseId());
				
				if(testExecutionResultsForTestCase!= null && !testExecutionResultsForTestCase.isEmpty()){
					for(TestExecutionResult testExecutionResult : testExecutionResultsForTestCase){
						
						TestStepResult testStepResult = new TestStepResult();
						
						//Actual results , Failure Reasona and Execution Remarks are appended and displayed
						String actualResult = testExecutionResult.getTestStepObservedOutput();
						if(testExecutionResult.getFailureReason()!= null && !testExecutionResult.getFailureReason().isEmpty()){
							actualResult = actualResult + "\n FailureReason : "+ testExecutionResult.getFailureReason();
						}
						
						if(testExecutionResult.getExecutionRemarks()!= null && !testExecutionResult.getExecutionRemarks().isEmpty()){
							actualResult = actualResult + "\n Execution Remarks : "+ testExecutionResult.getExecutionRemarks();
						}
						
						//Adding to include the defect code into the result for traceability
						Set<TestExecutionResultBugList> bugs =null; 
						
						if (bugs == null || bugs.isEmpty()) {
						} else {
							log.debug("Bugs available for this result");
							for (TestExecutionResultBugList bug : bugs) {
								if (bug.getBugFilingStatus().equals("FILED")) {
									actualResult = actualResult + "\n Defect Trace : "+ bug.getBugManagementSystemName() + " -> " + bug.getBugManagementSystemBugId();
									log.debug("Bug trace added to result for HPQC : " + bug.getBugManagementSystemName() + " -> " + bug.getBugManagementSystemBugId());
								}
							}
						}
						testStepResult.setActaulResult(actualResult);					
						
						TestCaseStepsList testStepList = testExecutionResult.getTestCaseStepsList();
						if(testStepList!= null){
							testStepResult.setStepId(testStepList.getTestStepCode());
						}
							
						if(testExecutionResult.getTestResultStatusMaster()!= null){
							if((testExecutionResult.getTestResultStatusMaster().getTestResultStatus()).equalsIgnoreCase("PASSED")){
								testStepResult.setStatus(STATUS.PASSED);
							}
							if((testExecutionResult.getTestResultStatusMaster().getTestResultStatus()).equalsIgnoreCase("FAILED")){
								testStepResult.setStatus(STATUS.FAILED);
								isTestCasePassed = false;
							}
						}
						Time execTime = new Time(testExecutionResult.getStartTime().getTime());
						Date execDate = new Date(testExecutionResult.getStartTime().getTime());
						
						testStepResult.setExecutionTime(execTime);
						testStepResult.setExecutionDate(execDate);
						
						//Setting the exec date and time for testCaseResult
						testCaseResult.setExecutionDate(execDate);
						testCaseResult.setExecutionTime(execTime);	
						testStepResults.add(testStepResult);
					}
					testCaseResult.setTestcaseID(testCaseList.getTestCaseCode());
					testCaseResult.setSteps(testStepResults);
					if(isTestCasePassed){
						testCaseResult.setStatus(STATUS.PASSED);
					} else {
						testCaseResult.setStatus(STATUS.FAILED);
					}
					testCaseResults.add(testCaseResult);
				}
			}
		} catch(Exception e){
				log.error("Could not export test execution results due to problem in preparing data for export", e);
				return "Could not export test execution results due to problem in preparing data for export";
		}
		
		try{
			if(testCaseResults!= null && !testCaseResults.isEmpty() ){
				testSetResult.setTestCases(testCaseResults);
				log.debug("Updating TestSet Results");
				List<TestResult> testResults = hpqcConnector.updateTestSetResult(testSetResult);
				if(testResults!= null){
					log.debug("TestResults Updated in Test Management System");
					for(TestResult testResult :testResults){		
						testResult.getStepId();
						testResult.getTestCaseId();
						testResult.getTestSetId();
						testExecutionResultsExportData.setResultCode(testResult.getRunId());
						testSuiteConfigurationService.addTestExecutionResultsExportData(testExecutionResultsExportData);
						log.debug("TestResult Run id : " + testResult.getRunId() + "Run Name : " + testResult.getRunName());
					}
					testManagementSystem = 	getTestManagementSystem(testRunList.getTestRunConfigurationChild().getProductVersionListMaster().getProductMaster());
					String eventDescription = "Exported Test Results to HPQC";
					eventsService.raiseTestResultsExportedEvent(testRunList,testManagementSystem, testResults.size(), eventDescription);
					
				}
			}	
		} catch(Exception e){
			log.error("Problem in connecting to HP Test Management system ", e);
			return "Problem in connecting to HP Test Management system ";		
		}		
		return status;
	}
		
	@Override
	@Transactional
	public String ExportTestExecutionResultsOTA(WorkPackage workPackage, ConnectorHPQC hpqcConnector) {
		
		log.info("Export Test Execution Results");
		
		TestManagementSystem testManagementSystem;
		String status = "";
		TestRunList testRunList = null;
		TestRunConfigurationChild testRunConfigChild = null;
		TestSuiteList testSuiteList = null;
		Set<TestCaseList> testCaseLists = null;		
		List<TestExecutionResult> testExecutionResultsForTestCase = null;
		TestExecutionResultsExportData testExecutionResultsExportData = new TestExecutionResultsExportData();
		
		boolean isTestCasePassed = true;
		List<TestStepResult> testStepResults = new ArrayList<TestStepResult>();
		List<TestCaseResult> testCaseResults =  new ArrayList<TestCaseResult>(); 
		TestSetResult testSetResult = new TestSetResult();
		
		Time testCaseDurationStart;
		Time testCaseDurationEnd;
		Set<TestRunJob> testRunJobs = workPackage.getTestRunJobSet();
		
		for (TestRunJob testRunJob : testRunJobs) {
			Set<TestSuiteList> testSuiteLists = testRunJob.getTestSuiteSet();
		
			for (TestSuiteList tsl : testSuiteLists) {
			
				testSetResult = new TestSetResult();
				List<WorkPackageTestCaseExecutionPlan> workPackageTestCaseExecutionPlans=workPackageService.getWorkPackageTestCaseExecutionPlanByTestRunJob(testRunJob,tsl.getTestSuiteId());
			
				testSetResult.setTestsetID(tsl.getTestSuiteCode());
			
				TestCaseExecutionResult testCaseExecutionResult=null;
				try{
					for (WorkPackageTestCaseExecutionPlan workPackageTestCaseExecutionPlan : workPackageTestCaseExecutionPlans) {
						
						testCaseExecutionResult=workPackageTestCaseExecutionPlan.getTestCaseExecutionResult();
						TestCaseResult testCaseResult = new TestCaseResult();
						testStepResults = new ArrayList<TestStepResult>();
						Set<TestStepExecutionResult> testStepExecutionResults=testCaseExecutionResult.getTestStepExecutionResultSet();
						TestStepResult testStepResult = null;
						String actualResult="";
						Time execTime = new Time(testCaseExecutionResult.getExecutionTime());
						Date execDate = new Date(testCaseExecutionResult.getWorkPackageTestCaseExecutionPlan().getActualExecutionDate().getTime());
						for (TestStepExecutionResult testStepExecutionResult : testStepExecutionResults) {
							testStepResult = new TestStepResult();
							if(actualResult.equals("")){
								actualResult =testStepExecutionResult.getObservedOutput();
							}else{
								actualResult =actualResult+","+testStepExecutionResult.getObservedOutput();
							}
							
							if(testCaseExecutionResult.getFailureReason()!= null && !testCaseExecutionResult.getFailureReason().isEmpty()){
								actualResult = actualResult + "\n FailureReason : "+ testCaseExecutionResult.getFailureReason();
							}
							
							if(testCaseExecutionResult.getComments()!= null && !testCaseExecutionResult.getComments().isEmpty()){
								actualResult = actualResult + "\n Execution Remarks : "+ testCaseExecutionResult.getComments();
							}
							
							//Adding to include the defect code into the result for traceability
							Set<TestExecutionResultBugList> bugs = testCaseExecutionResult.getTestExecutionResultBugListSet();
							if (bugs == null || bugs.isEmpty()) {
							} else {
								log.debug("Bugs available for this result");
								for (TestExecutionResultBugList bug : bugs) {
									if (bug.getBugFilingStatus().equals("FILED")) {
										actualResult = actualResult + "\n Defect Trace : "+ bug.getBugManagementSystemName() + " -> " + bug.getBugManagementSystemBugId();
										log.debug("Bug trace added to result for HPQC : " + bug.getBugManagementSystemName() + " -> " + bug.getBugManagementSystemBugId());
									}
									if(bug!= null){
										hpqcConnector.createBug(bug.getBugTitle(),new Time(System.currentTimeMillis()), SEVERITY.MEDIUM,false);
									}
								}
							}
							testStepResult.setActaulResult(actualResult);		
							testStepResult.setStepId(testStepExecutionResult.getTestSteps().getTestStepCode());
				
							if(testStepExecutionResult.getResult()!= null){
								if((testStepExecutionResult.getResult()).equalsIgnoreCase("PASS") || (testStepExecutionResult.getResult()).equalsIgnoreCase("PASSED")){
									testStepResult.setStatus(STATUS.PASSED);
								}
								if((testStepExecutionResult.getResult()).equalsIgnoreCase("FAIL") || (testStepExecutionResult.getResult()).equalsIgnoreCase("FAILED")){
									testStepResult.setStatus(STATUS.FAILED);
									isTestCasePassed = false;
								}
							}
							
							testStepResult.setExecutionTime(execTime);
							testStepResult.setExecutionDate(execDate);
							testStepResults.add(testStepResult);
						}
						testCaseResult.setExecutionDate(execDate);
						testCaseResult.setExecutionTime(execTime);	
						testCaseResult.setTestcaseID(workPackageTestCaseExecutionPlan.getTestCase().getTestCaseCode());
						testCaseResult.setSteps(testStepResults);
					
						if(isTestCasePassed){
							testCaseResult.setStatus(STATUS.PASSED);
						} else {
							testCaseResult.setStatus(STATUS.FAILED);
						}
						testCaseResults.add(testCaseResult);
					}
				} catch(Exception e){
						log.error("Could not export test execution results due to problem in preparing data for export", e);
						return "Could not export test execution results due to problem in preparing data for export";
				}
				
				try{
					if(testCaseResults!= null && !testCaseResults.isEmpty() ){
						testSetResult.setTestCases(testCaseResults);
						log.debug("Updating TestSet Results");
						List<TestResult> testResults = hpqcConnector.updateTestSetResult(testSetResult);
						if(testResults!= null){
							log.info("TestResults Updated in Test Management System");
							for(TestResult testResult :testResults){		
								testResult.getStepId();
								testResult.getTestCaseId();
								testResult.getTestSetId();
								testExecutionResultsExportData.setResultCode(testResult.getRunId());
								testExecutionService.addTestExecutionResultsExportData(testExecutionResultsExportData);
								log.info("TestResult Run id : " + testResult.getRunId() + "Run Name : " + testResult.getRunName());
							}
							testManagementSystem = 	getTestManagementSystem(testRunList.getTestRunConfigurationChild().getProductVersionListMaster().getProductMaster());
							String eventDescription = "Exported Test Results to HPQC";
							eventsService.raiseTestResultsExportedEvent(testRunList,testManagementSystem, testResults.size(), eventDescription);
						}
					}	
				} catch(Exception e){
					log.error("Problem in connecting to HP Test Management system ", e);
					return "Problem in connecting to HP Test Management system ";		
				}	
			}
		}
		return status;
	}
		
	@Override
	@Transactional
	public String ExportTestExecutionResultsOTA(TestRunJob testRunJob, ConnectorHPQC hpqcConnector) {
		
		log.debug("Export Test Execution Results");
		
		TestManagementSystem testManagementSystem;
		String status = "";
		TestRunList testRunList = null;
		TestRunConfigurationChild testRunConfigChild = null;
		TestSuiteList testSuiteList = null;
		Set<TestCaseList> testCaseLists = null;		
		List<TestExecutionResult> testExecutionResultsForTestCase = null;
		TestExecutionResultsExportData testExecutionResultsExportData = new TestExecutionResultsExportData();
		
		boolean isTestCasePassed = true;
		List<TestStepResult> testStepResults = new ArrayList<TestStepResult>();
		List<TestCaseResult> testCaseResults =  new ArrayList<TestCaseResult>(); 
		TestSetResult testSetResult = null;
		
		Time testCaseDurationStart;
		Time testCaseDurationEnd;
		Set<TestSuiteList> testSuiteLists = testRunJob.getTestSuiteSet();

		for (TestSuiteList tsl : testSuiteLists) {
			 testSetResult = new TestSetResult();
			testSetResult.setTestsetID(tsl.getTestSuiteCode());
		
		List<WorkPackageTestCaseExecutionPlan> workPackageTestCaseExecutionPlans=workPackageService.getWorkPackageTestCaseExecutionPlanByTestRunJob(testRunJob,tsl.getTestSuiteId());
		TestCaseExecutionResult testCaseExecutionResult=null;
		try{
			for (WorkPackageTestCaseExecutionPlan workPackageTestCaseExecutionPlan : workPackageTestCaseExecutionPlans) {
				testCaseExecutionResult=workPackageTestCaseExecutionPlan.getTestCaseExecutionResult();
				TestCaseResult testCaseResult = new TestCaseResult();
				testStepResults = new ArrayList<TestStepResult>();
				Set<TestStepExecutionResult> testStepExecutionResults=testCaseExecutionResult.getTestStepExecutionResultSet();
				TestStepResult testStepResult = null;
				String actualResult="";
				Time execTime = new Time(testCaseExecutionResult.getExecutionTime());
				Date execDate = new Date(testCaseExecutionResult.getWorkPackageTestCaseExecutionPlan().getActualExecutionDate().getTime());
				for (TestStepExecutionResult testStepExecutionResult : testStepExecutionResults) {
					testStepResult = new TestStepResult();
					if(actualResult.equals("")){
						actualResult =testStepExecutionResult.getObservedOutput();
					}else{
						actualResult =actualResult+","+testStepExecutionResult.getObservedOutput();
					}
					
					if(testCaseExecutionResult.getFailureReason()!= null && !testCaseExecutionResult.getFailureReason().isEmpty()){
						actualResult = actualResult + "\n FailureReason : "+ testCaseExecutionResult.getFailureReason();
					}
					
					if(testCaseExecutionResult.getComments()!= null && !testCaseExecutionResult.getComments().isEmpty()){
						actualResult = actualResult + "\n Execution Remarks : "+ testCaseExecutionResult.getComments();
					}
					
					//Adding to include the defect code into the result for traceability
					Set<TestExecutionResultBugList> bugs = testCaseExecutionResult.getTestExecutionResultBugListSet();
					if (bugs == null || bugs.isEmpty()) {
					} else {
						log.debug("Bugs available for this result");
						for (TestExecutionResultBugList bug : bugs) {
							if (bug.getBugFilingStatus().equals("FILED")) {
								actualResult = actualResult + "\n Defect Trace : "+ bug.getBugManagementSystemName() + " -> " + bug.getBugManagementSystemBugId();
								log.debug("Bug trace added to result for HPQC : " + bug.getBugManagementSystemName() + " -> " + bug.getBugManagementSystemBugId());
							}
							if(bug!= null){
								hpqcConnector.createBug(bug.getBugTitle(),new Time(System.currentTimeMillis()), SEVERITY.MEDIUM,false);
							}
						}
					}
					testStepResult.setActaulResult(actualResult);		
					testStepResult.setStepId(testStepExecutionResult.getTestSteps().getTestStepCode());
		
					if(testStepExecutionResult.getResult()!= null){
						if((testStepExecutionResult.getResult()).equalsIgnoreCase("PASS")){
							testStepResult.setStatus(STATUS.PASSED);
						}
						if((testStepExecutionResult.getResult()).equalsIgnoreCase("FAIL")){
							testStepResult.setStatus(STATUS.FAILED);
							isTestCasePassed = false;
						}
					}
					
					testStepResult.setExecutionTime(execTime);
					testStepResult.setExecutionDate(execDate);
					testStepResults.add(testStepResult);
				}
				testCaseResult.setExecutionDate(execDate);
				testCaseResult.setExecutionTime(execTime);	
				testCaseResult.setTestcaseID(workPackageTestCaseExecutionPlan.getTestCase().getTestCaseCode());
				testCaseResult.setSteps(testStepResults);
			
				if(isTestCasePassed){
					testCaseResult.setStatus(STATUS.PASSED);
				} else {
					testCaseResult.setStatus(STATUS.FAILED);
				}
				testCaseResults.add(testCaseResult);
			}
		} catch(Exception e){
				log.error("Could not export test execution results due to problem in preparing data for export", e);
				return "Could not export test execution results due to problem in preparing data for export";
		}
		
		try{
			if(testCaseResults!= null && !testCaseResults.isEmpty() ){
				testSetResult.setTestCases(testCaseResults);
				log.debug("Updating TestSet Results");
				List<TestResult> testResults = hpqcConnector.updateTestSetResult(testSetResult);
				if(testResults!= null){
					log.info("TestResults Updated in Test Management System");
					for(TestResult testResult :testResults){		
						testResult.getStepId();
						testResult.getTestCaseId();
						testResult.getTestSetId();
						testExecutionResultsExportData.setResultCode(testResult.getRunId());
						testExecutionService.addTestExecutionResultsExportData(testExecutionResultsExportData);
						log.debug("TestResult Run id : " + testResult.getRunId() + "Run Name : " + testResult.getRunName());
					}
					testManagementSystem = 	getTestManagementSystem(testRunList.getTestRunConfigurationChild().getProductVersionListMaster().getProductMaster());
					String eventDescription = "Exported Test Results to HPQC";
					eventsService.raiseTestResultsExportedEvent(testRunList,testManagementSystem, testResults.size(), eventDescription);
				}
			}	
		} catch(Exception e){
			log.error("Problem in connecting to HP Test Management system ", e);
			return "Problem in connecting to HP Test Management system ";		
		}	
		}
		return status;
	}
	
	
	private TestManagementSystem getTestManagementSystem(ProductMaster product){
			
			if(product == null){
				return null;
			}
			Set<TestManagementSystem> testManagementSystems = product.getTestManagementSystems();
			if(testManagementSystems == null || testManagementSystems.isEmpty()){
				return null;				
			} else {
				for (TestManagementSystem testManagementSystem : testManagementSystems) {
					if (testManagementSystem.getToolIntagration().getName().equalsIgnoreCase(TAFConstants.TEST_MANAGEMENT_SYSTEM_HPQC) || testManagementSystem.getToolIntagration().getName().equalsIgnoreCase(TAFConstants.TEST_MANAGEMENT_SYSTEM_TFS)){
						return testManagementSystem;
					}
				}
				return null;
			} 
		}

	@Override
	@Transactional
	public String processTestScriptsOTA(ConnectorHPQC hpqcConnector, TestSuiteList testSuite, String scriptName, String checkOutPath, String uploadPath, String testScriptSource) {		
		
		String initialCheckoutPath = checkOutPath + "\\"+scriptName;
		//Changes for checking out the scripts from Test Resources
		String testSuiteCode = testSuite.getTestSuiteCode();
		
		List<TestSet> testSets = hpqcConnector.getTestSets("");
		
		for (TestSet testSet : testSets) {
			if(testSet.getId() != null && testSet.getId().equals(testSuiteCode)) {
				if((TAFConstants.TEST_SCRIPT_SOURCE_HPQC_TEST_RESOURCES.equalsIgnoreCase(testScriptSource))) {
					
					checkOutResourceScriptsOTA(hpqcConnector, testSuite, checkOutPath, scriptName);	
					
				} else if(TAFConstants.TEST_SCRIPT_SOURCE_HPQC_TEST_PLAN.equalsIgnoreCase(testScriptSource)){
					List<TestCase> testCases =  testSet.getTestCases();					
					for (TestCase testCase : testCases) {
						Holder holder = new Holder<String>(initialCheckoutPath +"\\checkoutFolder\\scripts\\"+testCase.getId());
						testCase.getAllAttachments(holder);
					}
				}
				
				Holder<String> path = new Holder<String>("");
				String attachmentsDownloadPath = testSet.getAllAttachments(path);
				
				
				//Copy the attachments from temp folder to processing location
				File srcAttachmentsPath = new File(attachmentsDownloadPath);    
				File targetAttachmentsPath = new File(initialCheckoutPath+"\\checkoutFolder");
				try {
					FileUtils.copyDirectory(srcAttachmentsPath,targetAttachmentsPath);
				} catch (IOException e) {
					e.printStackTrace();
				}	
				FileParser.fileParser(scriptName, initialCheckoutPath, uploadPath);
			}
		}
		return null;
	}
	
	/**
	 * Method used for checking out the scripts from Test Resources in HPQC
	 * @param hpqcConnector
	 * @param testSuite
	 * @param scriptsCheckoutPath
	 * @param scriptName
	 */
	@Transactional
	public void checkOutResourceScriptsOTA(ConnectorHPQC hpqcConnector, TestSuiteList testSuite, String scriptsCheckoutPath, String scriptName){
		
		String testScriptResourceCheckout = scriptsCheckoutPath+"\\"+scriptName+"\\ResourcesFolder\\scripts";
		File testScriptSearchDirectory = new File(testScriptResourceCheckout); 		
		if(!testScriptSearchDirectory.exists()){
			testScriptSearchDirectory.mkdirs();
		}
		
		File testScriptsSource = new File(scriptsCheckoutPath+"\\"+scriptName+"\\checkoutFolder\\scripts");
		try{
			hpqcConnector.getTestResourceData(testScriptResourceCheckout);				
		} catch(Exception e){
			log.error("Exception in hpqc resources data checkout:", e);
		}
		
		
		Set<TestCaseList> testCases = testSuite.getTestCaseLists();
		try{
			String testcaseName = "";
			File testScriptFile = null;
			for (TestCaseList testCaseList : testCases) {	
				
				if(testcaseName!= null && !testcaseName.endsWith(".java")){
					testScriptFile = new File(testScriptSearchDirectory+ "\\"+ testcaseName +".java");
				} else {
					testScriptFile = new File(testScriptSearchDirectory+ "\\"+ testcaseName);
				}					
				if(FileUtils.directoryContains(testScriptSearchDirectory, testScriptFile)){
					log.info("TestScript file is found and the file name is:" + testScriptFile.getName());
					FileUtils.copyFileToDirectory(testScriptFile, testScriptsSource);
				}
			}
		} catch (IOException e) {
			log.error("Error inparsing search directory", e);
		}
	}
	
	/**
	 * Method for parsing the testcase Description field in HPQC and retrieving the Test case related data
	 * 
	 * @param testCase
	 * @param description
	 * @return
	 */
	private TestCaseList getParsedTestCaseDataFromHPQCOTA(TestCaseList testCase, String description){
	
		log.debug("TestCase Data Parsing starts:" );
		String token = TAFConstants.TAF_PARSE_TOKEN;
		String inputFromHPQC = description;
		String updatedDescription = "";
		String[] parsedData;		
		String testCaseScriptFileName = "";
		int Updateindex = 0;
		if(inputFromHPQC.contains(TAFConstants.TAF_DATA)){
			String processString = inputFromHPQC.substring(TAFConstants.TAF_DATA.length());
			
			//Removing the TestCase inputs from Description field
			Updateindex = inputFromHPQC.indexOf("#");
			updatedDescription = inputFromHPQC.substring(0,Updateindex);
			if(updatedDescription!= null){
				testCase.setTestCaseDescription(updatedDescription);
			}
				
			log.debug(processString);
			parsedData = processString.split(token);
			log.debug("Size of parsed data:" + parsedData.length);
			for(int i = 0; i < parsedData.length; i++){
				log.debug("parsed data:" + parsedData[i]);				
			}
			//TestCase Script File name
			testCaseScriptFileName = getParamValue(parsedData,TAFConstants.TESTCASE_SCRIPT_FILE_NAME);			
			if(testCaseScriptFileName!= null && testCaseScriptFileName.endsWith(".java")){
				testCaseScriptFileName = testCaseScriptFileName.substring(0,testCaseScriptFileName.indexOf(".java"));
			}
			
			log.debug(getParamValue(parsedData,TAFConstants.TESTCASE_QUALIFIED_SCRIPT_NAME));
			log.debug(getParamValue(parsedData,TAFConstants.TESTCASE_SCRIPT_FILE_NAME));
			log.debug(getParamValue(parsedData,TAFConstants.TESTCASE_PRIORITY));
		}
		log.debug("parsed Testcase data from HPQC ");
		return testCase;
	}
	
	//Method for parsing HPQC split data 
	private String getParamValue(String params[],String paramKey){
		String res=null;
		
		for(int i=0;i<params.length;i++){
			if(params[i].startsWith(paramKey)){
				String[] str=params[i].split(":",2);
				if(str.length==2) {
					if(str[1]!= null ){
						str[1] = str[1].trim();
					}						
					return str[1];
				} else {
					if(str[0]!= null){
						str[0] = str[0].trim();
					}
					return str[0];
				}
			}
		}
		return res;
	}
	
	/**
	 * This method is used for importing test cases from HPALM to TAF using Rest API.
	 * @param int - productId
	 * @param ConnectorHPQCRest - hpqcConnector
	 * @param String - testCaseSource
	 * 
	 */
	@Override
	@Transactional
	public String importTestCasesRest(int productId, ConnectorHPQCRest hpqcConnector,
			String testCaseFolder,String testSetFolder, String testSetFolderByName, String testCaseSource) {     //Changes for Bug 792 - Adding TestSetFilter by name 
	
		log.debug("Starting import of test cases from HPQC for product Id : " + productId);
		String status = "";
		String testCaseFilter = testCaseFolder;
		String testSetFilter = testSetFolder;
		String testSetFilterByName = testSetFolderByName;
		
		try {
		
			String source = testCaseSource;		
			List<TestCaseList> testCases = new ArrayList<TestCaseList>(0);
			
			log.debug("checking the productService class object:"+productService);
			ProductMaster productMaster = productService.getProductById(productId);
			
			status = importTestSetsRest(productMaster, hpqcConnector, testCaseFilter, testSetFilter,testSetFilterByName,source);//Changes for Bug 792 - Adding TestSetFilter by name 
			
			
			
		} catch (Exception e) {
			log.error("Problem while importing testcases from HPQC", e);
		}
		return status;
	}
	
	
	
	@Override
	@Transactional
	public String importTestSetsRest(ProductMaster productMaster, ConnectorHPQCRest hpqcConnector, String testCaseFolderId, String testSetFolderId,String testSetFilterByName,String source) {
			String importTestSetStatus="";
		List<TestSetRest> testSets = new ArrayList<TestSetRest>(0);			
		try{		
			if(testSetFolderId!=null && !testSetFolderId.trim().equals("")){//Folder Id wise filtering test sets
				StringTokenizer st1 = new StringTokenizer(testSetFolderId, ",");
				while (st1.hasMoreElements()) {					
					testSets = hpqcConnector.getTestSetsByFilter(st1.nextElement().toString(),"id");
					importTestSetStatus=saveHpqcTestSets(productMaster,hpqcConnector,testSets,source);
				}				
			}if(testSetFilterByName!=null && !testSetFilterByName.trim().equals("")){//Folder Name wise filtering test sets
				StringTokenizer st1 = new StringTokenizer(testSetFilterByName, ",");
				while (st1.hasMoreElements()) {					
					testSets = hpqcConnector.getTestSetsByFilter(st1.nextElement().toString(),"name");
					if(testSets ==null || testSets.size() == 0) {
						TestSetRest testSet= new TestSetRest();
						testSet.setName(testSetFilterByName);
						testSets.add(testSet);
						hpqcConnector.postTestSet(testSets);
					}
					importTestSetStatus=saveHpqcTestSets(productMaster,hpqcConnector,testSets,source);
				}
			}
			if((testSetFolderId==null || testSetFolderId.trim().equals("")) && (testSetFilterByName==null || testSetFilterByName.trim().equals(""))){//Fetching all test set folders
				testSets = hpqcConnector.getTestSetsByFilter();
				
				importTestSetStatus=saveHpqcTestSets(productMaster,hpqcConnector,testSets,source);
			}		
			
		} catch(Exception e){
		log.error("Error in getting test sets:" + e.getMessage(), e);
	}
		return importTestSetStatus;
	}
	public String saveHpqcTestSets(ProductMaster productMaster, ConnectorHPQCRest hpqcConnector,List<TestSetRest> testSets,String source){
		String eventDescription ="";
		try{
			TestSuiteList testSuite = new TestSuiteList();	
			TestCaseList testCaseList = new TestCaseList();
			List<TestCaseList> testCases = new ArrayList<TestCaseList>();
			
			ProductVersionListMaster productVersionListMaster= testSuiteConfigurationService.getLatestProductVersion(productMaster);
			int productId = productMaster.getProductId(); 
			if(testSets!= null && !testSets.isEmpty()){
				
				for(TestSetRest testSet : testSets){					
				
					if(testSet.getTestSetID().equals("0")){
						continue;
					} else{
						testCases = new ArrayList<TestCaseList>();
						List<TestCaseRest> hpqcTestCaseList = hpqcConnector.getTestSetTestCasesList(testSet.getTestSetID());
						//Raising event for importing testCases from HPQC
						if(hpqcTestCaseList != null && !hpqcTestCaseList.isEmpty()){
							processTestCasesRest(hpqcConnector, hpqcTestCaseList, productMaster, source);	
						}
						
						testSuite = testSuiteConfigurationService.getByProductTestSuiteCode(productId,testSet.getTestSetID());
						
						if(testSuite == null) {
							testSuite = testSuiteConfigurationService.getTestSuiteByProductIdOrTestSuiteNandOrTestSuiteCode(productId, testSet.getName().trim(), null);
						}
						if(testSuite== null){
							//The test suite does not exist. Create a new one
							log.debug("New TestSuite created : " + testSet.getTestSetID() + " : " +testSet.getName());
							testSuite = new TestSuiteList();
							testSuite.setTestSuiteCode(testSet.getTestSetID());
							testSuite.setTestSuiteName(testSet.getName().trim());
							testSuite.setProductMaster(productMaster);	
							
							testSuite.setExecutionPriority(testCasePriorityDAO.getTestCasePriorityBytestcasePriorityId(IDPAConstants.TESTCASE_DEFAULT_PRIORITY_ID));
							testSuite.setExecutionTypeMaster(executionTypeMasterService.getExecutionTypeByExecutionTypeId(IDPAConstants.TESTSUITE_EXECUTION_AUTOMATION));
							testSuite.setProductVersionListMaster(productVersionListMaster);
							if(testSet.getTestSetScriptFileLocation() != null){
								testSuite.setTestSuiteScriptFileLocation(testSet.getTestSetScriptFileLocation().trim());
							}
							int testSuiteId = testSuiteConfigurationService.addTestSuite(testSuite);
							testSuite = testSuiteConfigurationService.getByTestSuiteId(testSuiteId);
						} else {
							//Test Suite already exists. Prepare it with updated data for saving later.
							testSuite.setTestSuiteName(testSet.getName());
							testSuite.setTestSuiteCode(testSet.getTestSetID());
							log.debug("Existing TestSuite to be updated : " + testSuite.getTestSuiteCode() + " : " +testSuite.getTestSuiteName());
						}
						
						try
						{
							List<TestCaseRest> hpqcTestCases = hpqcConnector.getTestSetTestCasesList(testSet.getTestSetID());	
							log.debug("No of testcases in testsuite : " + testSuite.getTestSuiteCode() + " : " +testSuite.getTestSuiteName() + " : " + hpqcTestCases.size());
							if(hpqcTestCases!= null && !hpqcTestCases.isEmpty()){
								
								Set<TestCaseList> testSuiteTestCases = testSuite.getTestCaseLists();
								for(TestCaseRest  hpqcTestCase: hpqcTestCases){	
									
									testCaseList = testSuiteConfigurationService.getTestCaseByCodeProduct(hpqcTestCase.getTestcaseID(), productId);
									if (testCaseList == null) {
										//The test case does not exist. Skip it.
										log.debug("Testcase is an orphan. Not present in system : " + hpqcTestCase.getTestcaseID() + " : " +hpqcTestCase.getTitle());
										continue;
									}
									if (testSuiteTestCases.contains(testCaseList)) {
										//The test case is already part of the test suite. Ignore
										log.debug("Testcase is already part of testsuite : " + testCaseList.getTestCaseName());
									} else {
										testSuite.getTestCaseLists().add(testCaseList);
										testCaseList.getTestSuiteLists().add(testSuite);
										testCases.add(testCaseList);							
										log.info("Testcase is to be added to testsuite : " + testCaseList.getTestCaseName());
									}
								}
							}
							if(testSet.getTestSetScriptFileLocation() != null){
								testSuite.setTestSuiteScriptFileLocation(testSet.getTestSetScriptFileLocation().trim());
							}
							testSuiteConfigurationService.updateTestCases(testCases);
							testSuiteConfigurationService.updateTestSuiteList(testSuite);
						
							eventDescription = "Imported TestSets from HPQC";
							eventsService.raiseTestSuiteImportedEvent(testSuite, getTestManagementSystem(productMaster), testCases.size(), eventDescription);
					} catch(Exception e){
						log.error("Error in getting testcases for testSet:" + e.getMessage(), e);
					}
						
					}				
					
				}
			}
		}catch(Exception e){
			log.error("Error in saving saveHpqcTestSets()", e);
		}
		return eventDescription;
	}
	@Override
	@Transactional
	public String processTestScriptsRest(ConnectorHPQCRest hpqcConnector, TestSuiteList testSuite, String scriptName, String checkOutPath, String uploadPath, String testScriptSource,
			String resourceFolderId) {		
		
		String initialCheckoutPath = checkOutPath + "\\"+scriptName;
		//Changes for checking out the scripts from Test Resources
		String testSuiteCode = testSuite.getTestSuiteCode();
		
		try{

			List<TestSetRest> testSets = hpqcConnector.getTestSetsById(testSuiteCode);
			
			for (TestSetRest testSet : testSets) {
				if(testSet.getTestSetID() != null && testSet.getTestSetID().equals(testSuiteCode)) {
					if((TAFConstants.TEST_SCRIPT_SOURCE_HPQC_TEST_RESOURCES.equalsIgnoreCase(testScriptSource))) {
						
						checkOutResourceScriptsREST(hpqcConnector, initialCheckoutPath, resourceFolderId);	
						log.debug("Check out Resources using Rest call");
						
					} else if(TAFConstants.TEST_SCRIPT_SOURCE_HPQC_TEST_PLAN.equalsIgnoreCase(testScriptSource)){
						
						List<TestCaseRest> testCases =  hpqcConnector.getTestSetTestCasesList(testSet.getTestSetID());					
						for (TestCaseRest testCase : testCases) {
							String downloadpath = initialCheckoutPath +"\\checkoutFolder\\scripts\\"+testCase.getTestcaseID();
							File file = new File(downloadpath);
							if(!file.exists()){
								file.mkdirs();
							}
							hpqcConnector.getTestAttachments(testCase.getTestcaseID(), "TEST", downloadpath);
							hpqcConnector.getScripts(testCase.getTestcaseID(), downloadpath);
						}
					}
					
					//Commenting the testset attachment retrieval code for JNJ customization
					String attachmentsDownloadPath =initialCheckoutPath+"\\checkoutFolder";
					try {
						File file = new File(attachmentsDownloadPath);
						if(!file.exists()){
							file.mkdirs();
						}
						hpqcConnector.getTestAttachments(testSet.getTestSetID(), "TESTSET", attachmentsDownloadPath);
					} catch (Exception e1) {
						log.error("exception in getting attachments from test set", e1);
					}
					
					//Copy the attachments from temp folder to processing location
					//Commenting the code for Rest implementation as the attachments are directly downloaded to
					//Processing directory
					
					File srcAttachmentsPath = new File(attachmentsDownloadPath);    
					File targetAttachmentsPath = new File(initialCheckoutPath+"\\checkoutFolder");
					try {
						FileUtils.copyDirectory(srcAttachmentsPath,targetAttachmentsPath);
					} catch (IOException e) {
						e.printStackTrace();
					}	
					FileParser.fileParser(scriptName, initialCheckoutPath, uploadPath);
				}
			}
			
		} catch(Exception exe){
			log.error("Exception in processing scripts from HP ALM Instance", exe);
		}
		return null;
	}
	
	
	@Override
	@Transactional
	public String exportTestExecutionResultsRest(TestRunJob testRunJob, ConnectorHPQCRest hpqcConnector,TestManagementSystem testManagementSystem) {
		
		log.debug("Export Test Execution Results");
		
		String status = "";
		TestExecutionResultsExportData testExecutionResultsExportData = new TestExecutionResultsExportData();
		
		boolean isTestCasePassed = true;
		List<TestStepResultRest> testStepResults = new ArrayList<TestStepResultRest>();
		List<TestCaseResultRest> testCaseResults =  new ArrayList<TestCaseResultRest>(); ; 
		TestSetResultRest testSetResult = null;
		Set<TestSuiteList> testSuiteLists = testRunJob.getTestSuiteSet();
		
		for (TestSuiteList tsl : testSuiteLists) {
			
			testSetResult = new TestSetResultRest();
			Set<WorkPackageTestCaseExecutionPlan> workPackageTestCaseExecutionPlans=	testRunJob.getWorkPackageTestCaseExecutionPlans();
		
			testSetResult.setTestsetID(tsl.getTestSuiteCode());
			testSetResult.setTestExecutionId(testRunJob.getTestRunJobId());
		
			TestCaseExecutionResult testCaseExecutionResult=null;
			try{
				TestCaseResultRest testCaseResult =null;
			
				for (WorkPackageTestCaseExecutionPlan workPackageTestCaseExecutionPlan : workPackageTestCaseExecutionPlans) {
					
					if ((workPackageTestCaseExecutionPlan.getTestCase().getTestCaseSource() == null) || (!workPackageTestCaseExecutionPlan.getTestCase().getTestCaseSource().trim().equals(IDPAConstants.HPQC_TOOL))) {
						//Since the result corresponds to a testcase that has not been imported from HPQC, it cannot be exported to HPQC. Ignore it.
						log.info("Testcase : " + workPackageTestCaseExecutionPlan.getTestCase().getTestCaseId() + " has not been imported from HPQC. Ignoring it");
						continue;
					}
					
					testCaseExecutionResult=workPackageTestCaseExecutionPlan.getTestCaseExecutionResult();
					
					
					testCaseResult = new TestCaseResultRest();
					testStepResults = new ArrayList<TestStepResultRest>();
				
					//Fix for Bugzilla defect 844 -  test steps are not in order in HPQC
					List<TestStepExecutionResult> testStepExecutionResults=workPackageService.listTestStepResultByCaseExecId(testCaseExecutionResult.getTestCaseExecutionResultId());
				
					TestStepResultRest testStepResult = null;
					String actualResult="";
					Time execTime = null;
					isTestCasePassed = true;
					if(testCaseExecutionResult.getExecutionTime()!=null){
						execTime = new Time(testCaseExecutionResult.getExecutionTime());
					} else { 					
					execTime = new Time(new Date(System.currentTimeMillis()).getTime());
					}
					Date execDate = new Date(new Date(System.currentTimeMillis()).getTime());
					for (TestStepExecutionResult testStepExecutionResult : testStepExecutionResults) {
						testStepResult = new TestStepResultRest();
						actualResult="";
						String runconfig="";
						runconfig=	testStepExecutionResult.getTestCaseExecutionResult().getWorkPackageTestCaseExecutionPlan().getRunConfiguration().getRunconfiguration().getRunconfigName();
						if(actualResult.equals("")){
							actualResult =testStepExecutionResult.getObservedOutput()+"\nRun Configuration Details : "+runconfig;
						}else{
							actualResult =actualResult+","+testStepExecutionResult.getObservedOutput()+"\nRun Configuration Details : "+runconfig;
						}
					
						if(testStepExecutionResult.getFailureReason()!= null && !testStepExecutionResult.getFailureReason().isEmpty()){
							actualResult = actualResult + "\nFailure Reason : "+ testStepExecutionResult.getFailureReason();
					}
					
					if(testStepExecutionResult.getComments()!= null && !testStepExecutionResult.getComments().isEmpty()){
						actualResult = actualResult + "\nExecution Remarks : "+ testStepExecutionResult.getComments();
					} else {
						actualResult = actualResult + "\nExecution Remarks : "+ "N/A";
					}					
					//Adding to include the defect code into the result for traceability
					Set<TestExecutionResultBugList> bugs = testCaseExecutionResult.getTestExecutionResultBugListSet();
					if (bugs == null || bugs.isEmpty()) {
					} else {
						log.debug("Bugs available for this result");
						for (TestExecutionResultBugList bug : bugs) {
							if (bug.getBugFilingStatus().equals("FILED")) {
								actualResult = actualResult + "\nDefect Trace : "+ bug.getBugManagementSystemName() + " -> " + bug.getBugManagementSystemBugId();
								log.debug("Bug trace added to result for HPQC : " + bug.getBugManagementSystemName() + " -> " + bug.getBugManagementSystemBugId());
							}
							if(bug!= null){
							}
						}
					}
					testStepResult.setActualResult(actualResult);	
					
					if(testStepExecutionResult.getTestSteps()!=null){
						testStepResult.setName(testStepExecutionResult.getTestSteps().getTestStepName());
						testStepResult.setStepId(testStepExecutionResult.getTestSteps().getTestStepCode());
						testStepResult.setDescription(testStepExecutionResult.getTestSteps().getTestStepDescription());
						testStepResult.setExpectedResult(testStepExecutionResult.getTestSteps().getTestStepExpectedOutput());
					}
					
					
		
					if(testStepExecutionResult.getResult()!= null){
						if((testStepExecutionResult.getResult().trim()).equalsIgnoreCase("PASS") || (testStepExecutionResult.getResult().trim()).equalsIgnoreCase("PASSED")){
							testStepResult.setStatus(STATUS_REST.PASSED);
						}
						if((testStepExecutionResult.getResult()).equalsIgnoreCase("FAIL") || (testStepExecutionResult.getResult()).equalsIgnoreCase("FAILED")){
							testStepResult.setStatus(STATUS_REST.FAILED);
							isTestCasePassed = false;
						}
					}
					//Evidence
					String screenShotLocation="";
					
					List<Evidence> evidenceList =workPackageService.testcaseListByEvidence(testStepExecutionResult.getTeststepexecutionresultid(),"teststep");
					Evidence evidence =null;
					if(evidenceList!=null && !evidenceList.isEmpty() && evidenceList.size()==1){
						evidence=evidenceList.get(0);
						log.info("Evidence File URI: "+evidence.getFileuri());
						if(evidence.getFileuri()!= null && evidence.getFileuri()!=""){
							testStepResult.setScreenShotLocation(CommonUtility.getCatalinaPath()+File.separator+evidence.getFileuri());
							log.info("ScreenShotLocation: "+testStepResult.getScreenShotLocation());
						} else{
							log.info("ScreenShotLocation: "+testStepResult.getScreenShotLocation()+" may be empty or null");
						}
					}
					
					testStepResult.setExecutionTime(execTime);
					testStepResult.setExecutionDate(execDate);
					testStepResults.add(testStepResult);
				}
				
				testCaseResult.setExecutionDate(execDate);
				testCaseResult.setExecutionTime(execTime);	
				testCaseResult.setTestcaseID(workPackageTestCaseExecutionPlan.getTestCase().getTestCaseCode());
				testCaseResult.setSteps(testStepResults);
			
				if(isTestCasePassed){
					testCaseResult.setStatus(STATUS_REST.PASSED);
				} else {
					testCaseResult.setStatus(STATUS_REST.FAILED);
				}
				
				testCaseResults.add(testCaseResult);
			}
		}
		 catch(Exception e){
				log.error("Could not export test execution results due to problem in preparing data for export", e);
				return "Could not export test execution results due to problem in preparing data for export";
		}
		
		
		try{
			if(testCaseResults!= null && !testCaseResults.isEmpty() ){
				log.debug("Updating TestSet Results"+testCaseResults.size());

				testSetResult.setTestCases(testCaseResults);
				List<TestResultRest> testResults = hpqcConnector.updateTestSetResultsRest(testSetResult);
				
				if(testResults!= null && !testResults.isEmpty()){
					log.debug("TestResults Updated in Test Management System"+testResults.size());
					
					for(TestResultRest testResult :testResults){		
						testResult.getStepId();
						testResult.getTestCaseId();
						testResult.getTestSetId();
						testExecutionResultsExportData.setTestExecutionsResultId(testResult.getTestExecutionId());
						testExecutionResultsExportData.setResultCode(testResult.getRunId());
						testExecutionResultsExportData.setTestManagementSystemId(testManagementSystem.getTestManagementSystemId());
						testExecutionResultsExportData.setExportedDate(DateUtility.getCurrentDateWithTime());
						log.info("TestResult Run id : " + testResult.getRunId() + "Run Name : " + testResult.getRunName());						
					}
					testSuiteConfigurationService.addTestExecutionResultsExportData(testExecutionResultsExportData);
				}
			}	
		} catch(Exception e){
			log.error("Problem in connecting/exporting results to HP Test Management system", e);
			return "Problem in connecting to HP Test Management system ";		
		}	
		}
		return status;
	}

	
	/**
	 * Method for updating overall defects in HPQC through Rest services
	 *  
	 * @param testRunListId
	 * @param hpqcConnector
	 * 
	 */
	@Override
	@Transactional
	public String exportTestExecutionDefectsRest(TestExecutionResultBugList bug, ConnectorHPQCRest hpqcConnector) {
		
		log.debug("Export Test Execution Defects");
		String defectscreenShotLocation="";
		String status = "";		
		DefectRest defect = new DefectRest();
		if (bug == null ) {
				log.debug("no bugs available for filing");
				status = "no bugs available for filing";
			} else {
			log.debug("Bugs available for this result");
				if (bug.getBugFilingStatus().equals("FILED")) {
					log.debug("Bug already filed in a defect management system : " + bug.getBugManagementSystemName() + " -> " + bug.getBugManagementSystemBugId());
				}
				if(bug!= null){
					Date date = new Date(System.currentTimeMillis());						
					defect.setDefectSummary(bug.getBugDescription());
					defect.setDefectTitle(bug.getBugTitle());						
					defect.setDetectedDate(date.toString());
					defect.setSeverity(SEVERITY_REST.MEDIUM);
										
					//Evidence
					List<Evidence> defectEvidenceList =workPackageService.testcaseListByEvidence(bug.getTestStepExecutionResult().getTeststepexecutionresultid(),"teststep");
					Evidence evidence =null;
					if(defectEvidenceList!=null && !defectEvidenceList.isEmpty() && defectEvidenceList.size()==1){
						evidence=defectEvidenceList.get(0);
						log.info("Evidence File URI: "+evidence.getFileuri());
						if(evidence.getFileuri()!= null && evidence.getFileuri()!=""){
							defect.setScreenshotlocation(CommonUtility.getCatalinaPath()+File.separator+evidence.getFileuri());
							log.info("ScreenShotLocation: "+defect.getScreenshotlocation());
						} else{
							log.info("ScreenShotLocation: "+defect.getScreenshotlocation()+" may be empty or null");
						}
						
						hpqcConnector.postDefects(defect);
					testExecutionBugsService.update(bug);
				}
			}	
	}
 return status;
}
	
	@Override
	@Transactional
	public String exportTestExecutionDefectsRest(List<TestExecutionResultBugList> bugs, ConnectorHPQCRest hpqcConnector,int defectManagementSystemId) {
		
		log.debug("Export Test Execution Defects");
		
		String status = "";		
		DefectRest defect = new DefectRest();
		DefectExportData defectsExportData=null;
		DefectResultRest defectResultRest =null;
		
		for (TestExecutionResultBugList bug : bugs) {
			if (bug == null ) {
				log.debug("no bugs available for filing");
				status = "no bugs available for filing";
			} else {
			log.debug("Bugs available for this result");
				if (bug.getBugFilingStatus().equals("FILED")) {
					log.debug("Bug already filed in a defect management system : " + bug.getBugManagementSystemName() + " -> " + bug.getBugManagementSystemBugId());
				}
				if(bug!= null){
					Date date = new Date(System.currentTimeMillis());						
					defect.setDefectSummary(bug.getBugDescription());
					defect.setDefectTitle(bug.getBugTitle());						
					defect.setDetectedDate(date.toString());
					defect.setSeverity(SEVERITY_REST.MEDIUM);
					defectResultRest=hpqcConnector.postDefects(defect);
					testExecutionBugsService.update(bug);

					defectsExportData = new DefectExportData();
					defectsExportData.setDefectManagementSystemId(defectManagementSystemId);
					defectsExportData.setDefectSystemCode(defectResultRest.getDefectId());
					defectsExportData.setTestExecutionResultsBugId(bug.getTestExecutionResultBugId());

					testExecutionBugsService.addDefectExportData(defectsExportData);


				}
			}
			
		}
				
		return status;
	}
	
	
	
	@Override
	@Transactional
	public String ExportTestExecutionDefectsRest(WorkPackage workPackage,ConnectorHPQCRest hpqcConnector,int defectManagementSystemId) {
		
		log.debug("Export Test Execution Defects");
		
		String status = "";		
		DefectRest defect = new DefectRest();
		DefectResultRest defectResultRest=null;
		List<TestExecutionResultBugList> testExecutionResultBugs = workPackageService.listDefectsByWorkpackageId(workPackage.getWorkPackageId());
		DefectExportData defectsExportData=null;
		for (TestExecutionResultBugList bug : testExecutionResultBugs) {
			if (bug == null ) {
				log.debug("no bugs available for filing");
				status = "no bugs available for filing";
			} else {
			log.info("Bugs available for this result");
				if (bug.getBugFilingStatus().equals("FILED")) {
					log.debug("Bug already filed in a defect management system : " + bug.getBugManagementSystemName() + " -> " + bug.getBugManagementSystemBugId());
				}
				if(bug!= null){
					Date date = new Date(System.currentTimeMillis());						
					defect.setDefectSummary(bug.getBugDescription());
					defect.setDefectTitle(bug.getBugTitle());						
					defect.setDetectedDate(date.toString());
					defect.setSeverity(SEVERITY_REST.MEDIUM);
					defectResultRest=hpqcConnector.postDefects(defect);
					
					testExecutionBugsService.update(bug);
					
					defectsExportData = new DefectExportData();
					defectsExportData.setDefectManagementSystemId(defectManagementSystemId);
					defectsExportData.setDefectSystemCode(defectResultRest.getDefectId());
					defectsExportData.setTestExecutionResultsBugId(bug.getTestExecutionResultBugId());

					testExecutionBugsService.addDefectExportData(defectsExportData);

				}
			}
		}
				
		return status;
	}
	/**
	 * Method for updating selective defects in HPQC 
	 * @param testExecutionResultBug
	 * @param hpqcConnector
	 * 
	 */
	@Override
	@Transactional	
	public String exportTestExecutionSelectiveDefectRest(TestExecutionResultBugList testExecutionResultBug, ConnectorHPQCRest hpqcConnector,int defectManagementSystemId) {
		
		log.debug("Export Test Execution Results");
		DefectRest defect = new DefectRest();
		DefectResultRest defectResult;
		DefectExportData defectsExportData=null;
		String status = "";		
		if(testExecutionResultBug != null ){
			if (testExecutionResultBug.getBugFilingStatus().equals("FILED")) {
				log.debug("Bug already filed in a defect management system : " + testExecutionResultBug.getBugManagementSystemName() + " -> " + testExecutionResultBug.getBugManagementSystemBugId());
				}			
					
				Date date = new Date(System.currentTimeMillis());						
				defect.setDefectSummary(testExecutionResultBug.getBugDescription());
				defect.setDefectTitle(testExecutionResultBug.getBugTitle());						
				defect.setDetectedDate(date.toString());
				defect.setSeverity(SEVERITY_REST.MEDIUM);
				
				defectResult = hpqcConnector.postDefects(defect);
				
				String bugId = defectResult.getDefectId();
				log.debug("Defect Id from HPQC : " +  bugId); 
				if(testExecutionResultBug.getRemarks()!= null){
				testExecutionResultBug.setRemarks(testExecutionResultBug.getRemarks() + " Defect filed in HPQC" + "Defect Reference: " +  bugId);
				} else {
					testExecutionResultBug.setRemarks( "Defect filed in HPQC " + " Defect Reference: " +  bugId);
				}
								
				testExecutionBugsService.update(testExecutionResultBug);
				defectsExportData = new DefectExportData();
				defectsExportData.setDefectManagementSystemId(defectManagementSystemId);
				defectsExportData.setDefectSystemCode(defectResult.getDefectId());
				defectsExportData.setTestExecutionResultsBugId(testExecutionResultBug.getTestExecutionResultBugId());

				testExecutionBugsService.addDefectExportData(defectsExportData);

				
				status = "Filed";
				}
			return status;
		}


	@Override
	public String importFeatures(Integer productId, ConnectorHPQCRest hpqcConnector) {
		String status = "";
		log.debug("Starting import of Features from HPQC for product Id : " + productId);

		try {
			
			List<ProductFeature> features = new ArrayList<ProductFeature>(0);
			
			log.debug("checking the productService class object:"+productService);
			ProductMaster productMaster = productService.getProductById(productId);
			
			List<RequirementRest> hpqcFeaturesList = hpqcConnector.getRequirements();
			//Raising event for importing features from HPQC
			if(hpqcFeaturesList != null && !hpqcFeaturesList.isEmpty()){
				
				for (RequirementRest requirementRest : hpqcFeaturesList) {
					
					ProductFeature feature = new ProductFeature();
					feature.setProductMaster(productMaster);
					feature.setProductFeatureCode(requirementRest.getRequirementId());	
					feature.setProductFeatureDescription(requirementRest.getDescription());
					feature.setProductFeatureName(requirementRest.getName());
					productListService.addProductFeature(feature);
					
					features.add(feature);
				}
			}
		} catch (Exception e) {
			log.error("Problem while importing features from HPQC", e);
		}
		return status;
	}

	@Override
	public void processFeatureTCMappings(String excelDataFilePath, Integer productId, ConnectorHPQCRest hpqcConnector) {
		
		log.debug("Import - Excel file from location:" + excelDataFilePath);		
	
		int blankRowCount = 0;
		int colCount = 0;
		int rowNum = 0;
		int colNum = 0;
		int startRow = 0;
		int requirementIndex = 0;
		int testCaseIdIndex = 5;
		URL url = null;	
		int numberOfSheets = 0;
		FileInputStream fis;
		String dataValidStatus = "";
		int maxBlankRowCount = 3;
		try {

			//Get the Excel file		
			fis = new FileInputStream(excelDataFilePath);
			Workbook workbook = null;
			if (excelDataFilePath.endsWith(".xls")) {
				workbook = new HSSFWorkbook(fis);
			}
			if (excelDataFilePath.endsWith(".xlsx")) {
				workbook = new XSSFWorkbook(fis);
			}

			//Get the worksheet containing the data.
			//If not available, get the first worksheet.
			numberOfSheets = workbook.getNumberOfSheets();

			Sheet sheet = null;
			for (int i = 0; i < numberOfSheets; i++) {
				if (workbook.getSheetName(i).equalsIgnoreCase("Data")) {
					sheet = workbook.getSheetAt(i);
					break;
				}
			}
			if (sheet == null) {
				sheet = workbook.getSheetAt(0);
			}

			int rowCount = sheet.getPhysicalNumberOfRows();
			log.info("Physical row count:" + rowCount);
			
			if (rowCount <  1) {
				log.info("No rows present in the worksheet");			
			} else {
				for (rowNum=0; rowNum < rowCount;rowNum++){
					Row row;
					
					log.info("Physical row num:" + rowNum);
					row = sheet.getRow(rowNum);
					if(row == null){
						log.info("Empty Row");
						rowNum++;
						continue;
					}
					if (row.getRowNum() <= startRow) {
						log.info("The title row");
						colCount = row.getLastCellNum();
						rowNum++;
						continue;
					} else {

						//Read the info
						
						boolean requirementIdIsMissing = false;
						boolean testCaseIDIsMissing = false;
						int requirementId=0;
						int testCaseId=0;
						/*String requirementIdValue="";
						String testCaseIdValue="";*/

						//Requirement Id is mandatory. If not present skip row
						Cell cell1 = row.getCell(requirementIndex);
						if (isCellValid(cell1)) {		
							requirementId = (int) cell1.getNumericCellValue();
							log.debug("Requirement Id: "+cell1.getNumericCellValue());
						} else {
							requirementIdIsMissing = true;
						}

						Cell cell2 = row.getCell(testCaseIdIndex);
						
						if (isCellValid(cell2)) {		
							testCaseId = (int)(cell2.getNumericCellValue());
							log.debug("Test Case Code: "+cell1.getNumericCellValue());
						} else {
							testCaseIDIsMissing = true;
						}	
						
						if (requirementIdIsMissing && testCaseIDIsMissing) {
							blankRowCount++;
							log.debug("No of Blank Rows : " + blankRowCount);
							if (blankRowCount > maxBlankRowCount) {
								log.info("Max Blank rows encountered. Exiting the document");
								
								break;
							} else {
								log.debug("Blank row encountered. Skipping to next row");
								rowNum++;
								continue;
							}
						} else {
							//Reset the blankrow counter
							blankRowCount = 0;
							if (requirementIdIsMissing) {
								log.debug("requirementIdIsMissing is missing. Skipping the row");
								rowNum++;
								continue;
							}
						}
						
						Set<ProductFeature> productFeatures = new HashSet<ProductFeature>();
						Set<TestCaseList> testCases = new HashSet<TestCaseList>();
						
						ProductFeature productFeature = productListService.getByProductFeatureCode(requirementId);
						TestCaseList testCase = testSuiteConfigurationService.getTestCaseByCode(String.valueOf(testCaseId));
						
						if(productFeature != null && testCase != null){
							//Getting the existing mapping of productFeatures and test cases													
							productFeatures = testCase.getProductFeature();
							//Adding the new mappings
							productFeatures.add(productFeature);
							
						
							//Updating the mappings
							testSuiteConfigurationService.updateTestCase(testCase);
						}
							
					}										
				}
					
			}
		} catch (IOException IOE) {
			log.error("Error reading testcases from file", IOE);
		}
			catch (Exception e) {
			log.error("Error reading testcases from file", e);
		}
	
	}
	
	
	
	/**
	 * Method used for checking out the scripts from Test Resources in HPQC
	 * @param hpqcConnector
	 * @param testSuite
	 * @param scriptsCheckoutPath
	 * @param scriptName
	 */
	@Transactional
	public void checkOutResourceScriptsREST(ConnectorHPQCRest hpqcConnector, String initialCheckoutPath, String resourceFolderId){
		
		//Customization for JNJ. The common files(lib, properties, respositories and src) 
		//are get from Resources folder. Passing the Resource folder id and Path to download.
		String resourceDownloadPath =initialCheckoutPath+"\\checkoutFolder\\resources";
		try {
			File file = new File(resourceDownloadPath);
			if(!file.exists()){
				file.mkdirs();
			}
			hpqcConnector.getResourceByFolderId(resourceFolderId,resourceDownloadPath);
		} catch (Exception e1) {
			log.error("exception in getting attachments from test set", e1);
		}
	}
	
	
	//Cell Validation
			private boolean isCellValid(Cell cell){
				boolean validCell = false;
				if(cell != null){
				switch(cell.getCellType()){
				case Cell.CELL_TYPE_BLANK:
					validCell = false;
					break;
				case Cell.CELL_TYPE_STRING:
					if((cell.getStringCellValue()== null) || (cell.getStringCellValue()!= null && !"".equals(cell.getStringCellValue())
					&& !" ".equals(cell.getStringCellValue()))){
						validCell = true;	
					}
					break;
				case Cell.CELL_TYPE_NUMERIC:			
					if(!(Double.isNaN(cell.getNumericCellValue()))){
					validCell = true;
					}
					break;		
				}	
				
			}
				return validCell;
				
			}
			
		//Changes for Bug 777 - Report attachment to HPQC TestSet -starts
			/**
			 * This method is used for uploading reports to TestSets in HPQC.
			 * @throws Exception
			 */
			public Boolean UploadReportsToTestSet(TestRunJob testRunJob,ConnectorHPQCRest connection, File filePath, String entity, String id){
				Boolean status=false;
				try{
					status = connection.uploadAttachments(filePath,entity,id);
					
				} catch(Exception e){
					log.error("Exception in uploading the reports", e);
				}
				return status;
			}
			//Changes for Bug 777 - Report attachment to HPQC TestSet - Ends
			
			public List<String> getDomainProjectNameList(ConnectorHPQCRest hpqcConnector, String domainName){
				List<String> projectNames = new ArrayList<String>();
				try{
					projectNames =  hpqcConnector.getDomainProjectNames(domainName);
					log.info("Project Size --> "+projectNames.size());
				}catch(Exception e){
					log.error("Unable to get projects for Domain : " + domainName, e);
				}
				return projectNames;					
			}
			
			//Added for validating Domain names
			public List<String> getDomainNameList(ConnectorHPQCRest hpqcConnector){
				List<String> domainNames = new ArrayList<String>();
				try{
					domainNames =  hpqcConnector.getDomainNames();
					log.info("Domain Size --> "+domainNames.size());
				}catch(Exception e){
					log.error("Unable to get Domains : ", e);
				}
				return domainNames;					
			}
			
			@Override
			public String importTestCasesFromTFS(int productId,	int tfsTestPlanId, int tfsTestSuiteId, String tfsProjectCollectionPath, String tfsProjectName, String source, TFSIntegrator tfsConnector, TFSTeamProjectCollection projectCollection) {
				String importSucessMessage = null;
				try{
					log.info("Importing Test Cases from TFS Tool for the Product ID : "+productId);
					ProductMaster productMaster = productService.getProductById(productId);					
					importSucessMessage = importTestSuiteFromTFS(productMaster, tfsTestPlanId, tfsTestSuiteId, tfsProjectCollectionPath, tfsProjectName, source, tfsConnector, projectCollection);
				}catch(Exception e){
					log.error("Unable to import Test Cases from TFS due to some unknown problems",e);
				}
				return importSucessMessage;
			}

			@Override
			public String importTestSuiteFromTFS(ProductMaster productMaster, int tfsTestPlanId, int tfsTestSuiteId, String tfsProjectCollectionPath, String tfsProjectName, String source, TFSIntegrator tfsConnector, TFSTeamProjectCollection projectCollection) {
				TestSuite testSuite = new TestSuite();		
				try{			
					String tfsUrl = tfsProjectCollectionPath.concat("/"+tfsProjectName.trim()).trim();
					if(projectCollection!=null){
						testSuite  = tfsConnector.getTestSuite(projectCollection, tfsUrl, tfsTestPlanId, tfsTestSuiteId);
						if(testSuite!=null){
							saveTfsTestSuite(productMaster, testSuite, source);
						}else {
							log.info("Problem with the inputs...Please check the configuration.");
						}
					} else {
						log.info("Unable to communicate with TFS...Please check the configuration.");
					}
				}catch(Exception e){
					log.error("Unable to import test suite from TFS due to some unknown problems",e);
				}
				return null;
			}
			
			public void saveTfsTestSuite(ProductMaster productMaster, TestSuite tfsTestSuite, String source){
				try{
					TestSuiteList testSuite = new TestSuiteList();	
					TestCaseList testCaseList = new TestCaseList();
					List<TestCaseList> testCases = new ArrayList<TestCaseList>();
					
					ProductVersionListMaster productVersionListMaster= testSuiteConfigurationService.getLatestProductVersion(productMaster);
					int productId = productMaster.getProductId(); 
					
					if(tfsTestSuite != null){
						Set<com.hcl.atf.taf.tfs.model.TestCase> tfsTestCases = tfsTestSuite.getTestCases();
						if (tfsTestCases != null && !tfsTestCases.isEmpty()){
							processTfsTestCases(tfsTestCases, productMaster, source);	
						}
						
						testSuite = testSuiteConfigurationService.getByProductTestSuiteCode(productId,String.valueOf(tfsTestSuite.getTestSuiteCode()));
						
						if(testSuite== null){
							//The test suite does not exist. Create a new one
							log.info("New TestSuite created : " + tfsTestSuite.getTestSuiteCode() + " : " +tfsTestSuite.getTestSuiteName());
							testSuite = new TestSuiteList();
							testSuite.setTestSuiteCode(String.valueOf(tfsTestSuite.getTestSuiteCode()));
							testSuite.setTestSuiteName(tfsTestSuite.getTestSuiteName().trim());
							testSuite.setProductMaster(productMaster);	
							
							testSuite.setExecutionPriority(testCasePriorityDAO.getTestCasePriorityBytestcasePriorityId(IDPAConstants.TESTCASE_DEFAULT_PRIORITY_ID));
							testSuite.setExecutionTypeMaster(executionTypeMasterService.getExecutionTypeByExecutionTypeId(IDPAConstants.TESTSUITE_EXECUTION_AUTOMATION));
							testSuite.setProductVersionListMaster(productVersionListMaster);
							int testSuiteId = testSuiteConfigurationService.addTestSuite(testSuite);
							testSuite = testSuiteConfigurationService.getByTestSuiteId(testSuiteId);
						} else {
							//Test Suite already exists. Prepare it with updated data for saving later.
							testSuite.setTestSuiteName(tfsTestSuite.getTestSuiteName().trim());
							log.info("Existing TestSuite to be updated : " + testSuite.getTestSuiteCode() + " : " +testSuite.getTestSuiteName());
						}
											
						log.info("No of testcases in testsuite : " + tfsTestSuite.getTestSuiteCode() + " : " +testSuite.getTestSuiteName() + " : " + tfsTestCases.size());
						if(tfsTestCases!= null && !tfsTestCases.isEmpty()){							
							Set<TestCaseList> testSuiteTestCases = testSuite.getTestCaseLists();
							for(com.hcl.atf.taf.tfs.model.TestCase tfsTestCase: tfsTestCases){									
								testCaseList = testSuiteConfigurationService.getTestCaseByCodeProduct(String.valueOf(tfsTestCase.getTestCaseId()).trim(), productId);
								if (testCaseList == null) {
									//The test case does not exist. Skip it.
									log.debug("Testcase is an orphan. Not present in system : " + tfsTestCase.getTestCaseId() + " : " +tfsTestCase.getTestCaseName());
									continue;
								}
								if (testSuiteTestCases.contains(testCaseList)) {
									//The test case is already part of the test suite. Ignore
									log.debug("Testcase is already part of testsuite : " + testCaseList.getTestCaseName());
								} else {
									testSuite.getTestCaseLists().add(testCaseList);
									testCaseList.getTestSuiteLists().add(testSuite);
									testCases.add(testCaseList);							
									log.info("Testcase is to be added to testsuite : " + testCaseList.getTestCaseName());
								}
							}
						}
						testSuiteConfigurationService.updateTestCases((testCases));
						testSuiteConfigurationService.updateTestSuiteList(testSuite);
					
						String eventDescription = "Imported TestSets from TFS";
						eventsService.raiseTestSuiteImportedEvent(testSuite, getTestManagementSystem(productMaster), testCases.size(), eventDescription);
					}					
				}catch(Exception e){
					e.printStackTrace();
				}
			}
			
			
			private void processTfsTestCases(Set<com.hcl.atf.taf.tfs.model.TestCase> tfsTestCases, ProductMaster productMaster,String source){
				
				List<TestCaseList> testCasesList = new ArrayList<TestCaseList>(0);
				TestCaseList testCaseList = new TestCaseList();
				
				TestCaseStepsList testCaseStep = new TestCaseStepsList();	
				Set<TestCaseStepsList> testCaseStepsSet = new HashSet<TestCaseStepsList>(0);		
				boolean isNewTestCase = false;
				
				if(tfsTestCases!= null && !tfsTestCases.isEmpty()){
					
					for(com.hcl.atf.taf.tfs.model.TestCase testCase : tfsTestCases){
						log.info("Test Case Code >>"+testCase.getTestCaseId());
						//Check if this test case already exists, If not, create a new one. Else, update the existing one
						TestCaseList existingTestCase = testSuiteConfigurationService.getTestCaseByCodeProduct(String.valueOf(testCase.getTestCaseId()), productMaster.getProductId());
						if (existingTestCase == null) {
							log.info("New testcase created : " + testCase.getTestCaseName());
							testCaseList = new TestCaseList();
							//This is a new testcase from HPQC. Create one in TAF			
							testCaseList = convertToTestCaseFromTFSDataRest(testCaseList, testCase, productMaster, "TFS");
							int newTestCaseId = testSuiteConfigurationService.addTestCase(testCaseList);
							testCaseList.setTestCaseExecutionOrder(newTestCaseId);
							testSuiteConfigurationService.updateTestCase(testCaseList);
							log.info("testcase testcasecode>>"+testCaseList.getTestCaseCode());
							//TODO : Check if the saved object has an Id. If yes, delete the next line
							testCaseList = testSuiteConfigurationService.getByTestCaseId(newTestCaseId);
							isNewTestCase = true;
							
						} else {						
							//This testcase already exists in TAF. Just update it 
							existingTestCase = convertToTestCaseFromTFSDataRest(existingTestCase, testCase, productMaster, "TFS");
							testSuiteConfigurationService.updateTestCase(existingTestCase);
							testCaseList = testSuiteConfigurationService.getByTestCaseId(existingTestCase.getTestCaseId());
							isNewTestCase = false;
							log.info("Existing test case updated : " + existingTestCase.getTestCaseCode() + " : " +existingTestCase.getTestCaseName());

						}
						//Process the test steps for the test case
						try{
							List<TestStep> tfsTestSteps = testCase.getTestSteps().getTestSteps();							
							if(tfsTestSteps!= null && !tfsTestSteps.isEmpty()){								
								for(TestStep tfsTestStep : tfsTestSteps){
									log.info("tfsTestStep code:"+tfsTestStep.getTestStepId());
									//Check whether this teststep is already in TAF. If not, create a new one. Else, just update the existing one.
									//If the test case is a new one, the test step will also be a new one.
									//Check if the teststep already exists for the test case
									TestCaseStepsList existingTestStep = testSuiteConfigurationService.getTestCaseStepsByCodeAndProduct(String.valueOf(tfsTestStep.getTestStepId()).trim(), productMaster.getProductId());
									if (isNewTestCase || (existingTestStep == null)) {										
										log.info("New test step created : " + tfsTestStep.getTestStepId() + " : " + tfsTestStep.getStepName());
										testCaseStep = new TestCaseStepsList();	
										//Either the test case is new or the test step does not exist. In either case, create a new test step
										testCaseStep = convertToTestStepFromTFSDataRest(testCaseStep, tfsTestStep, source);
										testCaseStep.setTestCaseList(testCaseList);							
										testSuiteConfigurationService.addTestCaseStep(testCaseStep);
										//TODO : Check if the below line is needed
										testSuiteConfigurationService.updateTestCase(testCaseList);
									} else {			
										//The test step already exists. Just update it.
										existingTestStep = convertToTestStepFromTFSDataRest(existingTestStep, tfsTestStep, source);
										existingTestStep.setTestCaseList(testCaseList);
										testSuiteConfigurationService.updateTestCaseSteps(existingTestStep);
										log.debug("Existing test step updated : " + existingTestStep.getTestStepCode() + " : " + existingTestStep.getTestStepName());
									}
								}						
							}
						} catch(Exception e){
							log.info("Error in getting test steps", e);
						}
					}
					String eventDescription = "Imported testcases from HPQC";
					eventsService.raiseTestCasesImportedEvent(productMaster, getTestManagementSystem(productMaster), tfsTestCases.size(), eventDescription);
				}
			}
			
			private TestCaseList convertToTestCaseFromTFSDataRest(TestCaseList testCase, com.hcl.atf.taf.tfs.model.TestCase tfsTestCase, ProductMaster product, String source) {
				
				testCase.setTestCaseCode(String.valueOf(tfsTestCase.getTestCaseId()).trim());
				testCase.setTestCaseName(tfsTestCase.getTestCaseName().trim());
				testCase.setTestCaseScriptFileName(tfsTestCase.getTestCaseName().trim());
				testCase.setTestCaseDescription(tfsTestCase.getTestCaseDescription().replaceAll("\\<.*?\\>", "").replaceAll("\\s", " ").trim());
				testCase.setTestcaseTypeMaster(testcaseTypeMasterDAO.getTestcaseTypeMasterBytestcaseTypeId(IDPAConstants.TESTCASE_TYPE_FUNCTIONAL));				
				testCase.setTestCasePriority(testCasePriorityDAO.getTestCasePriorityBytestcasePriorityId(IDPAConstants.TESTCASE_DEFAULT_PRIORITY_ID));
				testCase.setExecutionTypeMaster(executionTypeMasterService.getExecutionTypeByExecutionTypeId(IDPAConstants.TESTCASE_EXECUTION_AUTOMATION));
				testCase.setTestCaseSource(source);	
				// To update custom test case details
				testCase.setStatus(1);
				testCase.setProductMaster(product);	
				return testCase;
			}
			
			private TestCaseStepsList convertToTestStepFromTFSDataRest(TestCaseStepsList testCaseStep, TestStep tfsTestStep, String source) {		
				testCaseStep.setTestStepCode(String.valueOf(tfsTestStep.getTestStepId()).trim());
				testCaseStep.setTestStepName(tfsTestStep.getStepName().trim());		
				testCaseStep.setTestStepExpectedOutput(tfsTestStep.getExpectedResult().replaceAll("\\<.*?\\>", "").replaceAll("\\s", " ").trim());//Fix for Bug 839 - HTML tags display in report	
				testCaseStep.setTestStepSource(source);		
				return testCaseStep;
			}

			@Override
		@Transactional
		public List<Integer> tfsExportTestResults(TestRunJob testRunJob, TFSIntegrator tfsConnector, TestManagementSystem testManagementSystem,TFSTeamProjectCollection projectCollection, String tfsProductName) {
			
			log.debug("Export Test Execution Results");
			String status = "";
			TestExecutionResultsExportData testExecutionResultsExportData = new TestExecutionResultsExportData();
			List<TFSTestResult> testResults = new ArrayList<TFSTestResult>();
			List<Integer> runIds = new ArrayList<Integer>();
			boolean isTestCasePassed = true;
			
			List<TFSTestStepResult> testStepResults = new ArrayList<TFSTestStepResult>();
			List<TFSTestCaseResult> testCaseResults =  new ArrayList<TFSTestCaseResult>(); ; 
			TFSTestSetResult testSetResult = null;
			
			Set<TestSuiteList> testSuiteLists = testRunJob.getTestSuiteSet();
			
			for (TestSuiteList tsl : testSuiteLists) {
				
				testSetResult = new TFSTestSetResult();
				Set<WorkPackageTestCaseExecutionPlan> workPackageTestCaseExecutionPlans=testRunJob.getWorkPackageTestCaseExecutionPlans();
							
				testSetResult.setTestSuiteId(tsl.getTestSuiteCode());
				testSetResult.setTestExecutionId(testRunJob.getTestRunJobId());
			
				TestCaseExecutionResult testCaseExecutionResult=null;
				try{
					TFSTestCaseResult testCaseResult =null;
				
					for (WorkPackageTestCaseExecutionPlan workPackageTestCaseExecutionPlan : workPackageTestCaseExecutionPlans) {
						
						if ((workPackageTestCaseExecutionPlan.getTestCase().getTestCaseSource() == null) || (!workPackageTestCaseExecutionPlan.getTestCase().getTestCaseSource().trim().equals(IDPAConstants.TFS_TOOL))) {
							log.info("Testcase : " + workPackageTestCaseExecutionPlan.getTestCase().getTestCaseId() + " has not been imported from TFS. Ignoring it");
							continue;
						}
						
						testCaseExecutionResult=workPackageTestCaseExecutionPlan.getTestCaseExecutionResult();							
						
						testCaseResult = new TFSTestCaseResult();
						testStepResults = new ArrayList<TFSTestStepResult>();
						
						List<TestStepExecutionResult> testStepExecutionResults=workPackageService.listTestStepResultByCaseExecId(testCaseExecutionResult.getTestCaseExecutionResultId());
					
						TFSTestStepResult testStepResult = null;
						for (TestStepExecutionResult testStepExecutionResult : testStepExecutionResults) {
							testStepResult = new TFSTestStepResult();									
							testStepResult.setTestCaseId(testStepExecutionResult.getTestcase().getTestCaseCode());
							testStepResult.setStepId(testStepExecutionResult.getTestSteps().getTestStepCode());
							testStepResult.setName(testStepExecutionResult.getTestSteps().getTestStepName());
							testStepResult.setDescription(testStepExecutionResult.getFailureReason());
							if(testStepExecutionResult.getResult()!= null){
								if((testStepExecutionResult.getResult().trim()).equalsIgnoreCase("PASS") || (testStepExecutionResult.getResult().trim()).equalsIgnoreCase("PASSED")){
									testStepResult.setStatus(com.hcl.atf.taf.tfs.util.STATUS.PASSED);
								}
								if((testStepExecutionResult.getResult()).equalsIgnoreCase("FAIL") || (testStepExecutionResult.getResult()).equalsIgnoreCase("FAILED")){
									testStepResult.setStatus(com.hcl.atf.taf.tfs.util.STATUS.FAILED);
									isTestCasePassed = false;
								}
							}
															
							List<Evidence> evidenceList =workPackageService.testcaseListByEvidence(testStepExecutionResult.getTeststepexecutionresultid(),"teststep");
							Evidence evidence =null;
							if(evidenceList!=null && !evidenceList.isEmpty() && evidenceList.size()==1){
								evidence=evidenceList.get(0);
								log.info("Evidence File URI: "+evidence.getFileuri());
								if(evidence.getFileuri()!= null && evidence.getFileuri()!=""){
									testStepResult.setScreenShotLocation(CommonUtility.getCatalinaPath()+File.separator+evidence.getFileuri());
									log.info("ScreenShotLocation: "+testStepResult.getScreenShotLocation());
								} else{
									log.info("ScreenShotLocation: "+testStepResult.getScreenShotLocation()+" may be empty or null");
								}
							}							
						testStepResults.add(testStepResult);
					}
					testCaseResult.setTestcaseId(workPackageTestCaseExecutionPlan.getTestCase().getTestCaseCode());
					testCaseResult.setSteps(testStepResults);
				
					if(isTestCasePassed){
						testCaseResult.setStatus(com.hcl.atf.taf.tfs.util.STATUS.PASSED);
					} else {
						testCaseResult.setStatus(com.hcl.atf.taf.tfs.util.STATUS.FAILED);
					}
					
					testCaseResults.add(testCaseResult);
				}
			}
			 catch(Exception e){
					log.error("Could not export test execution results due to problem in preparing data for export", e);
			}
			
			
			try{
				if(testCaseResults!= null && !testCaseResults.isEmpty() ){
					log.debug("Updating TestSet Results"+testCaseResults.size());

					testSetResult.setTestCases(testCaseResults);
					
					log.info(testSetResult.getTestSuiteId());
					String tfsUrl = testManagementSystem.getConnectionUri().trim().concat("/"+tfsProductName.trim()).trim();
					testResults = tfsConnector.createTestRunForTestRunJobResult(tfsUrl,Integer.valueOf(testManagementSystem.getConnectionProperty3()), Integer.valueOf(testManagementSystem.getConnectionProperty2()), projectCollection, testSetResult);
					status = "Success";					
					if(testResults!= null && !testResults.isEmpty()){
						log.debug("TestResults Updated in Test Management System"+testResults.size());						
						for(TFSTestResult testResult :testResults){	
							runIds.add(Integer.valueOf(testResult.getRunId()));
							testExecutionResultsExportData.setTestExecutionsResultId(testResult.getTestExecutionId());
							testExecutionResultsExportData.setResultCode(testResult.getRunId());
							testExecutionResultsExportData.setTestManagementSystemId(testManagementSystem.getTestManagementSystemId());
							testExecutionResultsExportData.setExportedDate(DateUtility.getCurrentDateWithTime());
							log.info("TestResult Run Id : " + testResult.getRunId() + "Run Name : " + testResult.getRunName());						
						}
						testSuiteConfigurationService.addTestExecutionResultsExportData(testExecutionResultsExportData);
					}
				}	
			} catch(Exception e){
				log.error("Problem in connecting/exporting results to TFS Test Management system", e);						
			}	
			}
			return runIds;
		}

		@Override
		public Boolean tfsUploadReportsToTestRun(Integer tfsTestRunId,TFSIntegrator tfsConnector,TFSTeamProjectCollection projectCollection, String projectName,String file) {
			boolean flag = false;
			try{	
				log.info("Uploading test report to a tfs test run --> Starts");
				log.info("PDF Report file location : "+file);
				tfsConnector.addAttachment(projectName, file, tfsTestRunId,"TestRun", projectCollection);
				flag = true;
				log.info("PDF Report file location : "+file);
				log.info("Uploading test report to a tfs test run --> Ends");
			}catch(Exception e){
				flag = false;
				log.error("Error in uploading Test Run Report",e);				
			}
			return flag;
		}

		@Override
		@Transactional
		public String exportTestExecutionDefectsRest(List<TestExecutionResultBugList> bugs, TFSIntegrator tfsConnector,TFSTeamProjectCollection projectCollection, int defectManagementSystemId,String projectUrl) {
			
			log.debug("Export Test Execution Defects");
			
			String status = "";		
			
			DefectExportData defectsExportData=null;
			TFSTestBugResult bugResult = null;
			int bugId = 0;
			int testCaseId = 0;
			for (TestExecutionResultBugList bug : bugs) {				
				if (bug == null ) {
					log.debug("no bugs available for filing");
					status = "no bugs available for filing";
				} else {
				log.debug("Bugs available for this result");
					if (bug.getBugFilingStatus().equals("FILED")) {
						log.debug("Bug already filed in a defect management system : " + bug.getBugManagementSystemName() + " -> " + bug.getBugManagementSystemBugId());
					}
					if(bug!= null){		
						testCaseId = Integer.valueOf(bug.getTestStepExecutionResult().getTestcase().getTestCaseCode());
						bugResult = new TFSTestBugResult();
						bugResult.setTitle(bug.getBugTitle());
						bugResult.setDescription(bug.getBugDescription());
						bugResult.setBugTestCaseId(testCaseId);
						bugId = tfsConnector.createBug(projectUrl,projectCollection,bugResult);
						testExecutionBugsService.update(bug);

						defectsExportData = new DefectExportData();
						defectsExportData.setDefectManagementSystemId(defectManagementSystemId);
						defectsExportData.setDefectSystemCode(String.valueOf(bugId));
						defectsExportData.setTestExecutionResultsBugId(bug.getTestExecutionResultBugId());
						defectsExportData.setDefectExportDate(DateUtility.getCurrentDateWithTime());

						testExecutionBugsService.addDefectExportData(defectsExportData);


					}
				}
				
			}					
			return status;
		}
		
		@Override
		@Transactional
		public String exportWorkpackageTestExecutionDefectsRest(WorkPackage workPackage, TFSIntegrator tfsConnector,TFSTeamProjectCollection projectCollection, int defectManagementSystemId,String projectUrl) {
			
			log.debug("Export Test Execution Defects");
			
			String status = "";
			List<TestExecutionResultBugList> testExecutionResultBugs = workPackageService.listDefectsByWorkpackageId(workPackage.getWorkPackageId());	
			DefectExportData defectsExportData=null;
			TFSTestBugResult bugResult = null;
			int bugId = 0;
			int testCaseId = 0;
			for (TestExecutionResultBugList bug : testExecutionResultBugs) {				
				if (bug == null ) {
					log.debug("no bugs available for filing");
					status = "no bugs available for filing";
				} else {
				log.debug("Bugs available for this result");
					if (bug.getBugFilingStatus().equals("FILED")) {
						log.debug("Bug already filed in a defect management system : " + bug.getBugManagementSystemName() + " -> " + bug.getBugManagementSystemBugId());
					}
					if(bug!= null){		
						testCaseId = Integer.valueOf(bug.getTestStepExecutionResult().getTestcase().getTestCaseCode());
						bugResult = new TFSTestBugResult();
						bugResult.setTitle(bug.getBugTitle());
						bugResult.setDescription(bug.getBugDescription());
						bugResult.setBugTestCaseId(testCaseId);
						bugId = tfsConnector.createBug(projectUrl,projectCollection,bugResult);
						testExecutionBugsService.update(bug);

						defectsExportData = new DefectExportData();
						defectsExportData.setDefectManagementSystemId(defectManagementSystemId);
						defectsExportData.setDefectSystemCode(String.valueOf(bugId));
						defectsExportData.setTestExecutionResultsBugId(bug.getTestExecutionResultBugId());
						defectsExportData.setDefectExportDate(DateUtility.getCurrentDateWithTime());
						testExecutionBugsService.addDefectExportData(defectsExportData);


					}
				}
				
			}					
			return status;
		}			
		
		@Override
		@Transactional
		public String exportTFSTestRunJobTestExecutionDefectsRest(TestExecutionResultBugList bug, TFSIntegrator tfsConnector,TFSTeamProjectCollection projectCollection, int defectManagementSystemId, String projectUrl) {
			
			log.debug("Export Test Execution Defects");
			String status = "";		
			DefectExportData defectsExportData=null;
			TFSTestBugResult bugResult = null;
			int bugId = 0;
			int testCaseId = 0;
			if (bug == null ) {
				log.debug("no bugs available for filing");
				status = "no bugs available for filing";
			} else {
			log.debug("Bugs available for this result");
				if (bug.getBugFilingStatus().equals("FILED")) {
					log.debug("Bug already filed in a defect management system : " + bug.getBugManagementSystemName() + " -> " + bug.getBugManagementSystemBugId());
				}
				if(bug!= null){		
					testCaseId = Integer.valueOf(bug.getTestStepExecutionResult().getTestcase().getTestCaseCode());
					bugResult = new TFSTestBugResult();
					bugResult.setTitle(bug.getBugTitle());
					bugResult.setDescription(bug.getBugDescription());
					bugResult.setBugTestCaseId(testCaseId);
					bugId = tfsConnector.createBug(projectUrl,projectCollection,bugResult);
					testExecutionBugsService.update(bug);

					defectsExportData = new DefectExportData();
					defectsExportData.setDefectManagementSystemId(defectManagementSystemId);
					defectsExportData.setDefectSystemCode(String.valueOf(bugId));
					defectsExportData.setTestExecutionResultsBugId(bug.getTestExecutionResultBugId());
					defectsExportData.setDefectExportDate(DateUtility.getCurrentDateWithTime());
					testExecutionBugsService.addDefectExportData(defectsExportData);


				}
			}
	 return status;
	}

	@Override
	public String importTestCasesRest(int productId, ConnectorHPQCRest hpqcConnector, String testCaseSource, String testCaseFolder, String testSetFolder, String testSetFolderByName, String resourceBundleDownloadPath) {
		log.debug("Starting import of test cases from HPQC for product Id : " + productId);
		String status = "";
		String testCaseFilter = testCaseFolder;
		String testSetFilter = testSetFolder;
		String testSetFilterByName = testSetFolderByName;
		String downloadPath = resourceBundleDownloadPath;		
		try {		
			String source = testCaseSource;					
			log.debug("checking the productService class object:"+productService);
			ProductMaster productMaster = productService.getProductById(productId);			
			status = importTestSetsRest(productMaster, hpqcConnector, testCaseFilter, testSetFilter,testSetFilterByName,source,downloadPath);//Changes for Bug 792 - Adding TestSetFilter by name
		} catch (Exception e) {
			log.error("Problem while importing testcases from HPQC", e);			
		}
		return status;
	}

	@Override
	public String importTestSetsRest(ProductMaster productMaster, ConnectorHPQCRest hpqcConnector, String testCaseFolder, String testSetFolder, String testSetFolderByName, String source, String downloadPath) {
		List<TestSetRest> testSets = new ArrayList<TestSetRest>(0);		
		Map<String,String> testSetResourceBundleAttachments = new HashMap<String,String>(0);
		try{	
			if(testSetFolder!=null && !testSetFolder.trim().equals("")){
				StringTokenizer st1 = new StringTokenizer(testSetFolder, ",");
				while (st1.hasMoreElements()) {					
					testSets = hpqcConnector.getTestSetsByFilter(st1.nextElement().toString(),"id");
				}				
			}if(testSetFolderByName!=null && !testSetFolderByName.trim().equals("")){//Folder Name wise filtering test sets
				StringTokenizer st1 = new StringTokenizer(testSetFolderByName, ",");
				while (st1.hasMoreElements()) {					
					testSets = hpqcConnector.getTestSetsByFilter(st1.nextElement().toString(),"name");
				}
			}
			if((testSetFolder==null || testSetFolder.trim().equals("")) && (testSetFolderByName==null || testSetFolderByName.trim().equals(""))){				
				testSets = hpqcConnector.getTestSetsByFilter();				
			}	
			for(TestSetRest tsr : testSets){
				testSetResourceBundleAttachments = hpqcConnector.getTestSetResourceBundleAttachments(tsr.getTestSetID(),tsr.getName(),downloadPath);
				if(testSetResourceBundleAttachments != null && testSetResourceBundleAttachments.size() > 0){					
					for(Map.Entry<String, String> tsString : testSetResourceBundleAttachments.entrySet()){
						tsr.setTestSetScriptFileLocation(tsString.getValue().trim());
					}
				}
			}			
			saveHpqcTestSets(productMaster,hpqcConnector,testSets,source);
			return "Successfully imported Test Set from HP ALM System";
		} catch(Exception e){
			log.error("Error in getting test sets:" + e.getMessage(), e);
		}
		return "";
	}
	
	@Override
	@Transactional
	public String exportTestExecutionResultsToTestLink(TestRunJob testRunJob,TestManagementSystem testManagementSystem) {
		
		log.debug("Export Test Execution Results");
		
		String workpackageExecutionDetails = "";
		TestExecutionResultsExportData testExecutionResultsExportData = new TestExecutionResultsExportData();
		
		boolean isTestCasePassed = true;
		List<TestStepResultRest> testStepResults = new ArrayList<TestStepResultRest>();
		List<TestCaseResultRest> testCaseResults =  new ArrayList<TestCaseResultRest>(); ; 
		TestSetResultRest testSetResult = null;
		Set<TestSuiteList> testSuiteLists = testRunJob.getTestSuiteSet();
		
		for (TestSuiteList tsl : testSuiteLists) {
			
			testSetResult = new TestSetResultRest();
			Set<WorkPackageTestCaseExecutionPlan> workPackageTestCaseExecutionPlans=	testRunJob.getWorkPackageTestCaseExecutionPlans();
		
			testSetResult.setTestsetID(tsl.getTestSuiteCode());
			testSetResult.setTestExecutionId(testRunJob.getTestRunJobId());
		
			TestCaseExecutionResult testCaseExecutionResult=null;
			try{
				TestCaseResultRest testCaseResult =null;
			
				for (WorkPackageTestCaseExecutionPlan workPackageTestCaseExecutionPlan : workPackageTestCaseExecutionPlans) {
					
					if ((workPackageTestCaseExecutionPlan.getTestCase().getTestCaseSource() == null) || (!workPackageTestCaseExecutionPlan.getTestCase().getTestCaseSource().trim().equals(IDPAConstants.HPQC_TOOL))) {
						//Since the result corresponds to a testcase that has not been imported from HPQC, it cannot be exported to HPQC. Ignore it.
						log.info("Testcase : " + workPackageTestCaseExecutionPlan.getTestCase().getTestCaseId() + " has not been imported from HPQC. Ignoring it");
						continue;
					}
					
					testCaseExecutionResult=workPackageTestCaseExecutionPlan.getTestCaseExecutionResult();
					
					
					testCaseResult = new TestCaseResultRest();
					testStepResults = new ArrayList<TestStepResultRest>();
				
					//Fix for Bugzilla defect 844 -  test steps are not in order in HPQC
					List<TestStepExecutionResult> testStepExecutionResults=workPackageService.listTestStepResultByCaseExecId(testCaseExecutionResult.getTestCaseExecutionResultId());
				
					TestStepResultRest testStepResult = null;
					String actualResult="";
					Time execTime = null;
					isTestCasePassed = true;
					if(testCaseExecutionResult.getExecutionTime()!=null){
						execTime = new Time(testCaseExecutionResult.getExecutionTime());
					} else { 					
					execTime = new Time(new Date(System.currentTimeMillis()).getTime());
					}
					Date execDate = new Date(new Date(System.currentTimeMillis()).getTime());
					for (TestStepExecutionResult testStepExecutionResult : testStepExecutionResults) {
						testStepResult = new TestStepResultRest();
						actualResult="";
						String runconfig="";
						runconfig=	testStepExecutionResult.getTestCaseExecutionResult().getWorkPackageTestCaseExecutionPlan().getRunConfiguration().getRunconfiguration().getRunconfigName();
						if(actualResult.equals("")){
							actualResult =testStepExecutionResult.getObservedOutput()+"\nRun Configuration Details : "+runconfig;
						}else{
							actualResult =actualResult+","+testStepExecutionResult.getObservedOutput()+"\nRun Configuration Details : "+runconfig;
						}
					
						if(testStepExecutionResult.getFailureReason()!= null && !testStepExecutionResult.getFailureReason().isEmpty()){
							actualResult = actualResult + "\nFailure Reason : "+ testStepExecutionResult.getFailureReason();
					}
					
					if(testStepExecutionResult.getComments()!= null && !testStepExecutionResult.getComments().isEmpty()){
						actualResult = actualResult + "\nExecution Remarks : "+ testStepExecutionResult.getComments();
					} else {
						actualResult = actualResult + "\nExecution Remarks : "+ "N/A";
					}					
					//Adding to include the defect code into the result for traceability
					Set<TestExecutionResultBugList> bugs = testCaseExecutionResult.getTestExecutionResultBugListSet();
					if (bugs == null || bugs.isEmpty()) {
					} else {
						log.debug("Bugs available for this result");
						for (TestExecutionResultBugList bug : bugs) {
							if (bug.getBugFilingStatus().equals("FILED")) {
								actualResult = actualResult + "\nDefect Trace : "+ bug.getBugManagementSystemName() + " -> " + bug.getBugManagementSystemBugId();
								log.debug("Bug trace added to result for HPQC : " + bug.getBugManagementSystemName() + " -> " + bug.getBugManagementSystemBugId());
							}
							if(bug!= null){
							}
						}
					}
					testStepResult.setActualResult(actualResult);	
					
					if(testStepExecutionResult.getTestSteps()!=null){
						testStepResult.setName(testStepExecutionResult.getTestSteps().getTestStepName());
						testStepResult.setStepId(testStepExecutionResult.getTestSteps().getTestStepCode());
						testStepResult.setDescription(testStepExecutionResult.getTestSteps().getTestStepDescription());
						testStepResult.setExpectedResult(testStepExecutionResult.getTestSteps().getTestStepExpectedOutput());
					}
					
					
		
					if(testStepExecutionResult.getResult()!= null){
						if((testStepExecutionResult.getResult().trim()).equalsIgnoreCase("PASS") || (testStepExecutionResult.getResult().trim()).equalsIgnoreCase("PASSED")){
							testStepResult.setStatus(STATUS_REST.PASSED);
						}
						if((testStepExecutionResult.getResult()).equalsIgnoreCase("FAIL") || (testStepExecutionResult.getResult()).equalsIgnoreCase("FAILED")){
							testStepResult.setStatus(STATUS_REST.FAILED);
							isTestCasePassed = false;
						}
					}
					//Evidence
					String screenShotLocation="";
					
					List<Evidence> evidenceList =workPackageService.testcaseListByEvidence(testStepExecutionResult.getTeststepexecutionresultid(),"teststep");
					Evidence evidence =null;
					if(evidenceList!=null && !evidenceList.isEmpty() && evidenceList.size()==1){
						evidence=evidenceList.get(0);
						log.info("Evidence File URI: "+evidence.getFileuri());
						if(evidence.getFileuri()!= null && evidence.getFileuri()!=""){
							testStepResult.setScreenShotLocation(CommonUtility.getCatalinaPath()+File.separator+evidence.getFileuri());
							log.info("ScreenShotLocation: "+testStepResult.getScreenShotLocation());
						} else{
							log.info("ScreenShotLocation: "+testStepResult.getScreenShotLocation()+" may be empty or null");
						}
					}
					
					testStepResult.setExecutionTime(execTime);
					testStepResult.setExecutionDate(execDate);
					testStepResults.add(testStepResult);
				}
				
				testCaseResult.setExecutionDate(execDate);
				testCaseResult.setExecutionTime(execTime);	
				testCaseResult.setTestcaseID(workPackageTestCaseExecutionPlan.getTestCase().getTestCaseCode());
				testCaseResult.setSteps(testStepResults);
			
				if(isTestCasePassed){
					testCaseResult.setStatus(STATUS_REST.PASSED);
				} else {
					testCaseResult.setStatus(STATUS_REST.FAILED);
				}
				
				testCaseResults.add(testCaseResult);
			}
		}
		 catch(Exception e){
				log.error("Could not export test execution results due to problem in preparing data for export", e);
				return "Could not export test execution results due to problem in preparing data for export";
		}
		
		
		try{
			if(testCaseResults!= null && !testCaseResults.isEmpty() ){
				log.debug("Updating TestSet Results"+testCaseResults.size());

				testSetResult.setTestCases(testCaseResults);
				//List<TestResultRest> testResults = hpqcConnector.updateTestSetResultsRest(testSetResult);
				
				/*if(testResults!= null && !testResults.isEmpty()){
					log.debug("TestResults Updated in Test Management System"+testResults.size());
					
					for(TestResultRest testResult :testResults){		
						testResult.getStepId();
						testResult.getTestCaseId();
						testResult.getTestSetId();
						testExecutionResultsExportData.setTestExecutionsResultId(testResult.getTestExecutionId());
						testExecutionResultsExportData.setResultCode(testResult.getRunId());
						testExecutionResultsExportData.setTestManagementSystemId(testManagementSystem.getTestManagementSystemId());
						testExecutionResultsExportData.setExportedDate(DateUtility.getCurrentDateWithTime());
						log.info("TestResult Run id : " + testResult.getRunId() + "Run Name : " + testResult.getRunName());						
					}
					testSuiteConfigurationService.addTestExecutionResultsExportData(testExecutionResultsExportData);
				}*/
				testExecutionResultsExportData.setTestManagementSystemId(testManagementSystem.getTestManagementSystemId());
				testExecutionResultsExportData.setExportedDate(DateUtility.getCurrentDateWithTime());
				
			}	
			workpackageExecutionDetails= new Gson().toJson(testExecutionResultsExportData);
		} catch(Exception e){
			log.error("Error in exportTestExecutionResultsToTestLinkRest", e);
			return "Problem in exportTestExecutionResultsToTestLinkRest Test Management system ";		
		}	
		}
		return workpackageExecutionDetails;
	}
}