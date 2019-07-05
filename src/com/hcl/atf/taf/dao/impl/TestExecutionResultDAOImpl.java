package com.hcl.atf.taf.dao.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Hibernate;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.hcl.atf.taf.constants.TAFConstants;
import com.hcl.atf.taf.constants.TestExecutionStatus;
import com.hcl.atf.taf.dao.TestCaseListDAO;
import com.hcl.atf.taf.dao.TestCaseStepsListDAO;
import com.hcl.atf.taf.dao.TestExecutionResultDAO;
import com.hcl.atf.taf.dao.TestRunListDAO;
import com.hcl.atf.taf.model.ExecutionPriority;
import com.hcl.atf.taf.model.ProductMaster;
import com.hcl.atf.taf.model.TestCaseList;
import com.hcl.atf.taf.model.TestCasePriority;
import com.hcl.atf.taf.model.TestCaseStepsList;
import com.hcl.atf.taf.model.TestExecutionResult;
import com.hcl.atf.taf.model.TestExecutionResultBugList;
import com.hcl.atf.taf.model.TestExecutionResultsExportData;
import com.hcl.atf.taf.model.TestRunList;
import com.hcl.atf.taf.model.TestSuiteList;
import com.hcl.atf.taf.model.custom.TestRunListCustom;
import com.hcl.atf.taf.model.json.JsonTestExecutionResult;

@Repository
public class TestExecutionResultDAOImpl implements TestExecutionResultDAO {
	private static final Log log = LogFactory.getLog(TestExecutionResultDAOImpl.class);
	
	@Autowired(required=true)
    private SessionFactory sessionFactory;
	@Autowired(required=true)
    private TestCaseListDAO testCaseDAO;
	@Autowired(required=true)
    private TestCaseStepsListDAO testCaseStepsDAO;
	@Autowired(required=true)
    private TestRunListDAO testRunListDAO;
	
	@Override
	@Transactional
	public void delete(TestExecutionResult testExecutionResult) {
		log.debug("deleting TestExecutionResult instance");
		try {
			sessionFactory.getCurrentSession().delete(testExecutionResult);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
		}		
	}

	@Override
	@Transactional
	public TestExecutionResult add(TestExecutionResult testExecutionResult) {
		log.debug("adding TestExecutionResult instance");
		try {
			TestExecutionResult ter = validateAndTruncate(testExecutionResult);	
			
			sessionFactory.getCurrentSession().save(ter);
			
			ter = getTestExecutionResultByIdentifier(testExecutionResult.getTestExecutionResultId());
			if(ter==null){
				log.info("Unable insert test execution result due to unknown reason");
				return null;
			}
			Hibernate.initialize(ter.getTestRunList().getDeviceList().getDevicePlatformVersionListMaster().getDevicePlatformMaster());
			Hibernate.initialize(ter.getTestSuiteList().getProductMaster());
			Hibernate.initialize(ter.getTestResultStatusMaster());				
			Hibernate.initialize(ter.getTestCaseList());
			Hibernate.initialize(ter.getTestCaseStepsList());
			Hibernate.initialize(ter.getTestRunList().getTestRunConfigurationChild());
			if(ter.getTestCaseList()!= null){
				Hibernate.initialize(ter.getTestCaseList().getProductFeature());
			}
			log.info("Added ter : " + ter.getTestExecutionResultId());
					
			log.debug("Inserted Test Execution Result");
			return ter;
		} catch (RuntimeException re) {
			
			log.error("Unable insert test execution result", re);
			return null;
		}
	}

	@Override
	@Transactional
	public TestExecutionResult processTestExecutionResult(TestExecutionResult testExecutionResult, JsonTestExecutionResult jsonTestExecutionResult) {
		log.debug("Processing TestExecutionResult instance");
		try {
			testExecutionResult = getTestExecutionResultByIdentifier(testExecutionResult.getTestExecutionResultId());
			if(testExecutionResult==null){
				log.info("Unable insert test execution result due to unknown reason");
				return null;
			}
		
			testExecutionResult = associateTestCaseNew(testExecutionResult, jsonTestExecutionResult);
			log.debug("Associated Test cases and test step successfully");
		} catch (RuntimeException re) {
			log.error("Unable to associate test case and test step to execution", re);
			//throw re;
		}
		try {
			createBug(testExecutionResult);
		} catch (RuntimeException re) {
			log.error("Unable to create bug for failed test step", re);
		}
		return testExecutionResult;
	}
	
	private TestExecutionResult validateAndTruncate(TestExecutionResult testExecutionResult){
		if(testExecutionResult != null){
		if (testExecutionResult.getTestStepDescription() != null && testExecutionResult.getTestStepDescription().length() > 2000) {
			testExecutionResult.setTestStepDescription(testExecutionResult.getTestStepDescription().substring(0,2000));
		}
		if (testExecutionResult.getTestStepInput() != null && testExecutionResult.getTestStepInput().length() > 2000) {
			testExecutionResult.setTestStepInput(testExecutionResult.getTestStepInput().substring(0, 2000));
		}
		if (testExecutionResult.getTestStepExpectedOutput() != null && testExecutionResult.getTestStepExpectedOutput().length() > 2000) {
			testExecutionResult.setTestStepExpectedOutput(testExecutionResult.getTestStepExpectedOutput().substring(0, 2000));
		}
		if (testExecutionResult.getTestStepObservedOutput() != null && testExecutionResult.getTestStepObservedOutput().length() > 2000) {
			testExecutionResult.setTestStepObservedOutput(testExecutionResult.getTestStepObservedOutput().substring(0, 2000));
		}
		if (testExecutionResult.getTestCaseDescription() != null && testExecutionResult.getTestCaseDescription().length() > 2000) {
			testExecutionResult.setTestCaseDescription(testExecutionResult.getTestCaseDescription().substring(0, 2000));
		}		
		if (testExecutionResult.getExecutionRemarks() != null && testExecutionResult.getExecutionRemarks().length() > 2000) {
			testExecutionResult.setExecutionRemarks(testExecutionResult.getExecutionRemarks().substring(0, 2000));
		}
		if (testExecutionResult.getFailureReason() != null && testExecutionResult.getFailureReason().length() > 2000) {
			testExecutionResult.setFailureReason(testExecutionResult.getFailureReason().substring(0, 2000));
		}	
	}
		return testExecutionResult;
	}
	
	private TestExecutionResult associateTestCaseNew(TestExecutionResult testExecutionResult, JsonTestExecutionResult jsonTestExecutionResult) {
		
		//Get the Product 
		int testRunListId = testExecutionResult.getTestRunList().getTestRunListId();
		TestRunList testRunList = (TestRunList) sessionFactory.getCurrentSession().get(TestRunList.class, testRunListId);
		ProductMaster product = testRunList.getTestRunConfigurationChild().getProductVersionListMaster().getProductMaster();
		
		TestCaseStepsList testStep = null;
		TestCaseList testCase = null;
		boolean createNewTestCaseStep = false;
		//If test step ID is available, get the teststep and testcase
		if (!(jsonTestExecutionResult.getTestStepId() == null || jsonTestExecutionResult.getTestStepId() <= 0)) {
			log.info("Test Step ID is available : " + jsonTestExecutionResult.getTestStepId());
			testStep = testCaseStepsDAO.getByTestCaseId(jsonTestExecutionResult.getTestStepId());
			if (testStep != null) {
				log.info("Found testStep and Test case based on ID provided");
				testCase = testStep.getTestCaseList();
				testExecutionResult.setTestCaseList(testCase);
				testExecutionResult.setTestCaseStepsList(testStep);
				try {
					sessionFactory.getCurrentSession().saveOrUpdate(testExecutionResult);
					sessionFactory.getCurrentSession().saveOrUpdate(testCase);
					sessionFactory.getCurrentSession().saveOrUpdate(testStep);
					log.debug("Successfully associated result to existing test case, step based on test step ID/ Code");
				} catch (RuntimeException r) {
					log.error("Unable to associate testStep, testCase", r);
				}
				return testExecutionResult;
			} else {
				log.info("The Teststep ID provided is not correct : " + jsonTestExecutionResult.getTestStepId());
			}
		} else {
			log.info("Test Step ID is not available");
		}
		
		//If Teststep ID is not available or valid, try with teststep code
		if (!(jsonTestExecutionResult.getTestStepCode() == null || jsonTestExecutionResult.getTestStepCode().trim().isEmpty())) {
					
			log.info("Test Step Code is available : " + jsonTestExecutionResult.getTestStepCode());
			testStep = testCaseStepsDAO.getByTestStepCode(jsonTestExecutionResult.getTestStepCode(), product);
			if (testStep != null) {
				log.info("Found testStep and Test case based on Code provided");
				testCase = testStep.getTestCaseList();
				testExecutionResult.setTestCaseList(testCase);
				testExecutionResult.setTestCaseStepsList(testStep);
				try {
					sessionFactory.getCurrentSession().saveOrUpdate(testExecutionResult);
					sessionFactory.getCurrentSession().saveOrUpdate(testCase);
					sessionFactory.getCurrentSession().saveOrUpdate(testStep);
					log.debug("Successfully associated result to existing test case, step based on test step ID/ Code");
				} catch (RuntimeException r) {
					log.error("Unable to associate testStep, testCase", r);
				}
				return testExecutionResult;
			} else { 
				log.info("The Teststep Code provided is not correct. Hence the result is an orphan result : " + jsonTestExecutionResult.getTestStepCode());
				return testExecutionResult;
			}
		} else {
			log.info("Test Step Code is not available");
		}
		
		//Since teststep could not be obtained with ID or code, Try to get it with teststep name
		//For this, first get the test case
		//Get testcase by ID
		if (!(jsonTestExecutionResult.getTestCaseId() == null || jsonTestExecutionResult.getTestCaseId() <= 0)) {
			log.info("Test Case Id is available : " + jsonTestExecutionResult.getTestCaseId());
			testCase = testCaseDAO.getByTestCaseId(jsonTestExecutionResult.getTestCaseId());
		} else {
			log.info("Test Case Id is not avaiable");
		}
		//If not obtained through ID, try to get it with testcase code
		if (testCase == null) {
			if (!(jsonTestExecutionResult.getTestCaseCode() == null || jsonTestExecutionResult.getTestCaseCode().trim().isEmpty())) {
				log.info("Test Case Code is available : " + jsonTestExecutionResult.getTestCaseCode());
				testCase = testCaseDAO.getByTestCaseCode(jsonTestExecutionResult.getTestCaseCode(), product);
			} else {
				log.info("Test Case code is not avaiable");
			}
		} else {
			log.info("Found Test case based on ID provided");
		}
		//If not available by ID and code, try to get by name
		if (testCase == null) {
			testCase = getTestCaseByName(testExecutionResult.getTestCase(), product.getProductId());
		} else {
			log.info("Found Test case based on Code provided");
		}
		//If not available by name also, create a new testcase
		if (testCase == null) {

			log.info("Test case was not found with given name. Creating new one : " + testExecutionResult.getTestCase());
			testCase = new TestCaseList(testExecutionResult.getTestCase().trim());
			testCase.setTestCaseDescription(testExecutionResult.getTestCaseDescription());
			
			TestCasePriority tcp = new TestCasePriority();
			tcp.setPriorityName(jsonTestExecutionResult.getTestCasePriority());
			testCase.setTestCasePriority(tcp);			
			testCase.setTestCaseCreatedDate(new Date(System.currentTimeMillis()) );
			testCase.setTestCaseSource(TAFConstants.TESTCASE_SOURCE_TAF);
			testCase.setTestCaseType(TAFConstants.TESTCASE_AUTOMATED);			
			testCase.setProductMaster(product);
			//Associate Test case to test suite. New m-n relationship
			TestSuiteList testSuite = testExecutionResult.getTestSuiteList();
			testSuite.getTestCaseLists().add(testCase);
			testCase.getTestSuiteLists().add(testSuite);
			try {
				sessionFactory.getCurrentSession().save(testCase);
				sessionFactory.getCurrentSession().saveOrUpdate(testSuite);
				log.info("Created new testcase : " + testCase.getTestCaseName());
			} catch (RuntimeException r) {
				log.error("Scenario 3 : Unable to create new testcase", r);
				return testExecutionResult;
			}
		} else {
			log.info("Found Test case using name : " + testCase.getTestCaseName() + " : " + testCase.getTestCaseId());

			// Commented the following methods when removing the "testCaseScriptQualifiedName" and "testCaseScriptFileName" from Test_case_list table. By: Logeswari, On: Feb-11-2015
			// The following logic is implemented to update ScriptQualifiedName, ScriptFileName, testCasePriority if they don't exist in DB.
				TestCasePriority tcp = new TestCasePriority();
			if (testCase.getTestCasePriority() == null && jsonTestExecutionResult.getTestCasePriority() != null)
				tcp.setPriorityName(jsonTestExecutionResult.getTestCasePriority());
				testCase.setTestCasePriority(tcp);
				ExecutionPriority exp = new ExecutionPriority();
			try {
				sessionFactory.getCurrentSession().saveOrUpdate(testCase);
				log.info("Updated existing testcase : " + testCase.getTestCaseName() + "with few details") ;
			} catch (RuntimeException r) {
				log.error("Unable to update testcase details", r);
			}
			
			// The following logic is implemented to fix the issue with TestCase Testsuite association
			//Associate Test case to test suite. New m-n relationship
			TestSuiteList testSuite = testExecutionResult.getTestSuiteList();
			Set<TestCaseList> testCaseListInTestSuite = testSuite.getTestCaseLists();
			if (testCaseListInTestSuite == null || !testCaseListInTestSuite.contains(testCase)) {
				testSuite.getTestCaseLists().add(testCase);
				//testCase.getTestSuiteLists().add(testSuite);
				try {
					//sessionFactory.getCurrentSession().saveOrUpdate(testCase);
					sessionFactory.getCurrentSession().saveOrUpdate(testSuite);
					log.info("Updated testcase - testsuite association");
				} catch (RuntimeException r) {
					log.error("Scenario 4 : Unable to create association between testcase and testsuite", r);
				}
			} else 
				log.info("Already testcase-testSuite Association Exists") ;
		}
			
		//Get the test step with test step name. 
		testStep = getTestCaseStepByName(testExecutionResult.getTestStep(), testCase);
		//If not found by name, create a test step
		if (testStep == null) {
			
			testStep = new TestCaseStepsList(testExecutionResult.getTestStep().trim());
			testStep.setTestStepDescription(testExecutionResult.getTestStepDescription());
			testStep.setTestStepInput(testExecutionResult.getTestStepInput());
			testStep.setTestStepExpectedOutput(testExecutionResult.getTestStepExpectedOutput());
			testStep.setTestStepCreatedDate(new Date(System.currentTimeMillis()));
			testStep.setTestStepSource(TAFConstants.TESTCASE_SOURCE_TAF);
			testStep.setTestCaseList(testCase);
			try {
				sessionFactory.getCurrentSession().save(testStep);
				sessionFactory.getCurrentSession().saveOrUpdate(testCase);
				log.info("Created new test step : " + testExecutionResult.getTestStep());
			} catch (RuntimeException r) {
				log.error("Creating testStep failed", r);
				return testExecutionResult;
			}
		} else {
			log.info("Found Test step using name : " + testStep.getTestStepName() + " : " + testStep.getTestStepId());
		}

		//Associate TER to the teststep and test case
		try {
			testExecutionResult.setTestCaseStepsList(testStep);
			testExecutionResult.setTestCaseList(testCase);
			sessionFactory.getCurrentSession().saveOrUpdate(testExecutionResult);
			log.debug("Successfully associated test case and created a new TestStep");
			return testExecutionResult;
		} catch (RuntimeException r) {
			log.error("Associating result to Testcase, test step Scenario 2 : Failed", r);
		}
		return testExecutionResult;
	}

	
	private TestExecutionResult associateTestCase(TestExecutionResult testExecutionResult, JsonTestExecutionResult jsonTestExecutionResult) {
		
		//Get the Product 
		int testRunListId = testExecutionResult.getTestRunList().getTestRunListId();
		TestRunList testRunList = (TestRunList) sessionFactory.getCurrentSession().get(TestRunList.class, testRunListId);
		ProductMaster product = testRunList.getTestRunConfigurationChild().getProductVersionListMaster().getProductMaster();
		
		TestCaseStepsList testStep = null;
		TestCaseList testCase = null;
		boolean createNewTestCaseStep = false;
		//Scenario 1 : Test step already exists. Hence Test case also already exists
		testStep = testCaseStepsDAO.getExistingTestStepForTER(testExecutionResult, jsonTestExecutionResult, product);
		if (testStep != null) {
			testCase = testStep.getTestCaseList();
			testExecutionResult.setTestCaseList(testCase);
			testExecutionResult.setTestCaseStepsList(testStep);
			try {
				sessionFactory.getCurrentSession().saveOrUpdate(testExecutionResult);
				sessionFactory.getCurrentSession().saveOrUpdate(testCase);
				sessionFactory.getCurrentSession().saveOrUpdate(testStep);
				log.debug("Successfully associated result to existing test case, step based on test step ID/ Code");
				return testExecutionResult;
			} catch (RuntimeException r) {
				log.info("Created new teststep : " + testStep.getTestStepName());
				log.error("Unable to create teststep", r);
			}
		}

		//Scenario 2 : Teststep does not exist, but TestCase exists
		//testCase = testCaseDAO.getExistingTestCaseForTER(testExecutionResult, jsonTestExecutionResult, product);
		if (testCase != null) {
			//Since Teststep does not exist, create a new one
			testStep = new TestCaseStepsList(testExecutionResult.getTestStep().trim());
			testStep.setTestStepDescription(testExecutionResult.getTestStepDescription());
			testStep.setTestStepInput(testExecutionResult.getTestStepInput());
			testStep.setTestStepExpectedOutput(testExecutionResult.getTestStepExpectedOutput());
			testStep.setTestStepCreatedDate(new Date(System.currentTimeMillis()));
			testStep.setTestStepSource(TAFConstants.TESTCASE_SOURCE_TAF);
			testStep.setTestCaseList(testCase);
			try {
				sessionFactory.getCurrentSession().save(testStep);
				sessionFactory.getCurrentSession().save(testCase);
				testExecutionResult.setTestCaseStepsList(testStep);
				testExecutionResult.setTestCaseList(testCase);
				sessionFactory.getCurrentSession().saveOrUpdate(testExecutionResult);
				log.debug("Successfully associated result to existing test case and created a new TestStep");
				return testExecutionResult;
			} catch (RuntimeException r) {
				log.error("Associating result to Testcase, test step Scenario 2 : Failed", r);
			}
		}
		
		//Scenario 3 : Both Testcase and Teststep do not exist. This will be applicable to
		//scenarios where test cases are being imported into TAF through scripts
		testCase = new TestCaseList(testExecutionResult.getTestCase().trim());
		testCase.setTestCaseDescription(testExecutionResult.getTestCaseDescription());
		testCase.setTestCaseCreatedDate(new Date(System.currentTimeMillis()) );
		testCase.setTestCaseSource(TAFConstants.TESTCASE_SOURCE_TAF);
		testCase.setTestCaseType(TAFConstants.TESTCASE_AUTOMATED);			
		testCase.setProductMaster(product);
		//Associate Test case to test suite. New m-n relationship
		TestSuiteList testSuite = testExecutionResult.getTestSuiteList();
		testSuite.getTestCaseLists().add(testCase);
		testCase.getTestSuiteLists().add(testSuite);
		try {
			sessionFactory.getCurrentSession().save(testCase);
			sessionFactory.getCurrentSession().saveOrUpdate(testSuite);
			log.info("Created new testcase : " + testCase.getTestCaseName());
			testExecutionResult.setTestCaseList(testCase);
			sessionFactory.getCurrentSession().saveOrUpdate(testExecutionResult);
		} catch (RuntimeException r) {
			log.error("Scenario 3 : Unable to create new testcase", r);
			return testExecutionResult;
		}
		testStep = new TestCaseStepsList(testExecutionResult.getTestStep().trim());
		testStep.setTestStepDescription(testExecutionResult.getTestStepDescription());
		testStep.setTestStepInput(testExecutionResult.getTestStepInput());
		testStep.setTestStepExpectedOutput(testExecutionResult.getTestStepExpectedOutput());
		testStep.setTestStepCreatedDate(new Date(System.currentTimeMillis()));
		testStep.setTestStepSource(TAFConstants.TESTCASE_SOURCE_TAF);
		testStep.setTestCaseList(testCase);
		try {
			sessionFactory.getCurrentSession().save(testStep);
			testExecutionResult.setTestCaseStepsList(testStep);
			sessionFactory.getCurrentSession().saveOrUpdate(testExecutionResult);
			//
			log.info("Created new teststep : " + testStep.getTestStepName());
			return testExecutionResult;
		} catch (RuntimeException r) {
			log.debug("Scenario 3 : Unable to create teststep", r);
			return testExecutionResult;
		} 
	}
	
	private void createBug(TestExecutionResult testExecutionResult) {

		if (testExecutionResult.getTestResultStatusMaster().getTestResultStatus().equalsIgnoreCase(TestExecutionStatus.FAILED.toString())) {
			TestExecutionResultBugList bug = createNewBugEntity(testExecutionResult);
			bug = checkIfBugIsDuplicate(bug);
			addBug(bug);
		}
	}
	
	private TestCaseList getTestCaseByName(String testCaseName, int productId) {
		
		log.info("Testcase Name : " + testCaseName + " : productId : " +  productId);

		if (testCaseName == null || testCaseName.trim().equals("")) {
			log.debug("Testcase Name is null. Cannot find it");
			return null;
		}
		List<TestCaseList> list=null;
		String hql = "from TestCaseList c where testCaseName = :name and c.productMaster.productId = :prId";
		try {
			list = sessionFactory.getCurrentSession().createQuery(hql).
					setParameter("name", testCaseName.trim()).
					setParameter("prId", productId).list();
			log.debug("Get by name successful");
		} catch (RuntimeException re) {
			log.error("Get Testcase by name failed", re);
		}
		if (list != null && !list.isEmpty())
			return list.get(0);
		else 
			return null;
	}
	
	private TestCaseStepsList getTestCaseStepByName(String testCaseStepName, TestCaseList testCase) {
		
		if (testCaseStepName == null || testCaseStepName.trim().equals("")) {
			log.debug("Teststep Name is null. Cannot find it");
			return null;
		}
		List<TestCaseStepsList> list=null;
		String hql = "from TestCaseStepsList c where testStepName = :name and c.testCaseList.testCaseId = :testCaseId";
		try {
			list = sessionFactory.getCurrentSession().createQuery(hql).
					setParameter("name", testCaseStepName).
					setParameter("testCaseId", testCase.getTestCaseId()).
					list();
			log.debug("Get by name successful");
		} catch (RuntimeException re) {
			log.error("Get Teststep by name failed " , re);
		}
		if (list != null && !list.isEmpty())
			return list.get(0);
		else 
			return null;
	}
	
	private TestExecutionResultBugList createNewBugEntity(TestExecutionResult testExecutionResult) {
		String testStepObservedOutput = "";
		if(testExecutionResult.getTestStepObservedOutput() != null){
			testStepObservedOutput = testExecutionResult.getTestStepObservedOutput();
		}
		
		TestExecutionResultBugList bug = new TestExecutionResultBugList();
		bug.setBugTitle(testExecutionResult.getTestCase()+ " : " + testExecutionResult.getTestStep() + " : " + "Failed");
		
		bug.setBugDescription("Expected : " + testExecutionResult.getTestStepExpectedOutput() + "\n" + "Actual Result" + " : " + testStepObservedOutput);
		bug.setFileBugInBugManagementSystem(false);
		bug.setBugCreationTime(testExecutionResult.getEndTime());
		System.out.println("New Bug Data : Title: " + bug.getBugTitle() 
						+ " : Bug Description: " + bug.getBugDescription()
						+ " : File Bug: " + bug.getFileBugInBugManagementSystem()
						+ " : Filing Status: " + bug.getBugFilingStatus());
		return bug;
	}
	
	
	public List<TestExecutionResultBugList> getBugsSameAsThisBug(TestExecutionResultBugList bug, SessionFactory sessionFactory) {
		
		List<TestExecutionResultBugList> bugs=null;
		return bugs;
	}

	private TestExecutionResultBugList checkIfBugIsDuplicate(TestExecutionResultBugList bug) {
		
		//Get the bugs created against the testcasestep earlier
		List<TestExecutionResultBugList> similarBugs = getBugsSameAsThisBug(bug, sessionFactory);
		//If no bugs are there, no action needed
		if (similarBugs == null || similarBugs.isEmpty()) {
			return bug;
		}
		boolean isDuplicateBugInTAF = false;
		boolean bugAlreadyReportedInDefectSystem = false;
		String bugCode = null;
		//Find if the bugs are similar and take action
		for (TestExecutionResultBugList similarBug : similarBugs) {
			
			//Check if the bug is actually a duplicate
			if (similarBug.getBugTitle().equalsIgnoreCase(bug.getBugTitle())) {
				if (similarBug.getBugDescription().equalsIgnoreCase(bug.getBugDescription())) {
				}
			}
		}
		if (bugAlreadyReportedInDefectSystem) {
			bug.setRemarks(bug.getRemarks() + "\n It has already been filed in the defect management system (" + bugCode + ")");
		}
		return bug;
	}
	
	private void addBug(TestExecutionResultBugList bug) {
		log.debug("adding bug instance");
		try {
			sessionFactory.getCurrentSession().save(bug);
			log.debug("add successful");
		} catch (RuntimeException re) {
			log.error("Unable to create bug for failed test step", re);
		}
				
	}



	@Override
	@Transactional
	public void update(TestExecutionResult testExecutionResult) {
		log.debug("updating TestExecutionResult instance");
		try {
			testExecutionResult = validateAndTruncate(testExecutionResult);
			sessionFactory.getCurrentSession().saveOrUpdate(testExecutionResult);
			log.debug("update successful");
		} catch (RuntimeException re) {
			log.error("update failed", re);
		}
		
	}
    
	
	
	@Override
	@Transactional
	public List<TestExecutionResult> listAll() {
		log.debug("listing all TestExecutionResult instance");
		List<TestExecutionResult> testExecutionResult=null;
		try {
			testExecutionResult=sessionFactory.getCurrentSession().createQuery("from TestExecutionResult").list();
			log.debug("list all successful");
		} catch (RuntimeException re) {
			log.error("list all failed", re);
		}
		return testExecutionResult;
	}
	
	@Override
	@Transactional
    public List<TestExecutionResult> listAll(int startIndex, int pageSize) {
		log.debug("listing TestExecutionResult instance");
		List<TestExecutionResult> testExecutionResult=null;
		try {
			testExecutionResult=sessionFactory.getCurrentSession().createQuery("from TestExecutionResult")
	                .setFirstResult(startIndex)
	                .setMaxResults(pageSize)
	                .list();
			log.debug("list successful");
		} catch (RuntimeException re) {
			log.error("list failed", re);
		}
		return testExecutionResult;       
    }
	
	
	@Override
	@Transactional
	public TestExecutionResult getByTestExecutionResultId(int testExecutionResultId){
		log.debug("getting TestExecutionResult instance by id");
		TestExecutionResult testExecutionResult=null;
		try {
			testExecutionResult=(TestExecutionResult) sessionFactory.getCurrentSession().get(TestExecutionResult.class,testExecutionResultId);
			log.debug("getByTesRunId successful");
		} catch (RuntimeException re) {
			log.error("getByTestRunId failed", re);
		}
		return testExecutionResult;
        
	}

	@Override
	@Transactional
	public int getTotalRecords() {
		log.debug("getting TestExecutionResult total records");
		int count =0;
		try {
			count=((Number) sessionFactory.getCurrentSession().createSQLQuery("select count(*) from test_execution_result").uniqueResult()).intValue();
			
			log.debug("total records fetch successful");
		} catch (RuntimeException re) {
			log.error("total records fetch failed", re);			
		}
		return count;
	
	}

	@Override
	@Transactional
	public List<TestExecutionResult> list(int testRunListId) {
		log.debug("listing specific TestExecutionResult");
		List<TestExecutionResult> testExecutionResult=null;
		try {
			testExecutionResult=sessionFactory.getCurrentSession().createQuery("from TestExecutionResult where testRunListId=:testRunListId")
														.setParameter("testRunListId", testRunListId).list();
			
			if (!(testExecutionResult == null || testExecutionResult.isEmpty())) {
				for (TestExecutionResult ter : testExecutionResult){
					Hibernate.initialize(ter.getTestRunList().getDeviceList().getDevicePlatformVersionListMaster().getDevicePlatformMaster());
					Hibernate.initialize(ter.getTestRunList().getDeviceList().getDeviceModelMaster().getDeviceMakeMaster());
					Hibernate.initialize(ter.getTestSuiteList().getProductMaster());
					Hibernate.initialize(ter.getTestResultStatusMaster());				
					Hibernate.initialize(ter.getTestCaseList());
					Hibernate.initialize(ter.getTestCaseStepsList());
					Hibernate.initialize(ter.getTestRunList().getTestRunConfigurationChild());
					if(ter.getTestCaseList()!= null){
						Hibernate.initialize(ter.getTestCaseList().getProductFeature());
					}
					Hibernate.initialize(ter.getTestSuiteList().getProductVersionListMaster());
				}
			}
			
			log.debug("list specific successful");
		} catch (RuntimeException re) {
			log.error("list specific failed", re);
			//throw re;
		}
		return testExecutionResult;
	}

	@Override
	@Transactional
	public List<TestExecutionResult> list(int testRunListId, int startIndex, int pageSize) {
		log.debug("listing specific TestExecutionResult instance");
		List<TestExecutionResult> testExecutionResult=null;
		try {
			testExecutionResult=sessionFactory.getCurrentSession().createQuery("from TestExecutionResult where testRunListId=:testRunListId")
														.setParameter("testRunListId", testRunListId).setFirstResult(startIndex)
														.setMaxResults(pageSize).list();
			log.debug("list specific successful");
		} catch (RuntimeException re) {
			log.error("list specific failed", re);
		}
		return testExecutionResult;
	}

	
	
	@Override
	@Transactional
	public int getTotalRecords(int testRunListId) {
		log.debug("getting TestExecutionResult records for TestRunConfigurationChildId");
		int count =0;
		try {
			count=((Number) sessionFactory.getCurrentSession().createSQLQuery("select count(*) from test_execution_result where testRunListId=:testRunListId").setParameter("testRunListId", testRunListId).uniqueResult()).intValue();
			
			log.debug("total records fetch successful");
		} catch (RuntimeException re) {
			log.error("total records fetch failed", re);			
			//throw re;
		}
		return count;
	}

	@Override
	@Transactional
	public List<TestRunListCustom> getExecutedStepsCountofTestRunlist(List<TestRunList> testRunList) {
		log.debug("getting ExecutedStepsCount");
		TestRunListCustom testRunListCustom;
		int liExeStepscount=0;
		List<TestRunListCustom> liExectutedStepscount=new ArrayList<TestRunListCustom>();
		
		try {
			for(TestRunList ts:testRunList){
				testRunListCustom=new TestRunListCustom();
				testRunListCustom.setTestRunList(ts);
				try{
					liExeStepscount=((Number)sessionFactory.getCurrentSession().createSQLQuery("select count(testExecutionResultId) as executedTestStepsCounts from test_execution_result where testRunListId = :testRunListId group by testRunListId")
							.setParameter("testRunListId", ts.getTestRunListId())
							.uniqueResult()).intValue();
				}catch(NullPointerException exception){
					liExeStepscount=0;
				}
				testRunListCustom.setExecutedTestStepsCounts(liExeStepscount);
				if (ts.getTestRunConfigurationChild().getTestSuiteList().getTestCaseLists() != null) {
					Set<TestCaseList> testCases = ts.getTestRunConfigurationChild().getTestSuiteList().getTestCaseLists();
					int testStepsCount = 0;
					for (TestCaseList testCase : testCases) {
						if (testCase.getTestCaseStepsLists() != null)
						testStepsCount = testStepsCount + testCase.getTestCaseStepsLists().size();
					}
					testRunListCustom.setTotalTestStepCount(testStepsCount);
				}
				int averageExecutionTime = testRunListDAO.getAverageTestRunExecutionTime(ts);
				testRunListCustom.setAverageTestExecutionTime(averageExecutionTime);
				if (ts.getTestRunStartTime() != null) {
					int elapsedTime = (int)(System.currentTimeMillis() - ts.getTestRunStartTime().getTime())/1000;
					testRunListCustom.setElapsedTestExecutionTime(elapsedTime);
					testRunListCustom.setRemainingTestExecutionTime(((averageExecutionTime - elapsedTime) > 0 ) ? (averageExecutionTime - elapsedTime) : 0); 
				}
				liExectutedStepscount.add(testRunListCustom);
			}
			log.debug("total records fetch successful");
		} catch (RuntimeException re) {
			log.error("total records fetch failed", re);			
			throw re;
		}
		return liExectutedStepscount;
	
	}
	@Override
	@Transactional
	public int getTotalRecords(int testRunListId, int testSuiteId) {
		log.debug("getting TestExecutionResult records for testRunListId AND testSuiteId");
		int count =0;
		try {
			count=((Number) sessionFactory.getCurrentSession().createSQLQuery("select count(*) from test_execution_result where testRunListId=:testRunListId AND testSuiteId=:testSuitId").setParameter("testRunListId", testRunListId).setParameter("testSuiteID",testSuiteId).uniqueResult()).intValue();
			
			log.debug("total records fetch successful");
		} catch (RuntimeException re) {
			log.error("total records fetch failed", re);			
		}
		return count;
	}

	@Override
	@Transactional
	public List<TestExecutionResult> list(int testRunListId, int testSuiteId) {
		log.debug("listing specific TestExecutionResult");
		List<TestExecutionResult> testExecutionResult=null;
		try {
			testExecutionResult=sessionFactory.getCurrentSession().createQuery("from TestExecutionResult where testRunListId=:testRunListId")
														.setParameter("testRunListId", testRunListId).setParameter("testSuiteID",testSuiteId).list();
			log.debug("list specific successful");
		} catch (RuntimeException re) {
			log.error("list specific failed", re);
		}
		return testExecutionResult;
	}

	@Override
	@Transactional
	public List<TestExecutionResult> list(int testRunListId, int testSuiteId,
			int startIndex, int pageSize) {
		log.debug("listing specific TestExecutionResult instance");
		List<TestExecutionResult> testExecutionResult=null;
		try {
			testExecutionResult=sessionFactory.getCurrentSession().createQuery("from TestExecutionResult where testRunListId=:testRunListId")
														.setParameter("testRunListId", testRunListId).setParameter("testSuiteID",testSuiteId).setFirstResult(startIndex)
														.setMaxResults(pageSize).list();
			log.debug("list specific successful");
		} catch (RuntimeException re) {
			log.error("list specific failed", re);
		}
		return testExecutionResult;
	}

	@Override
	@Transactional
	public List<TestExecutionResult> listFilteredTestExecutionResult(
			int startIndex, int pageSize, int productId, String platformName,
			int runNo, Date timeFrom, Date timeTo) {
		log.debug("listing filtered TestExecutionResult instance");
		List<TestExecutionResult> testExecutionResult=null;
		try {
			
			StringBuffer qry= new StringBuffer("from TestExecutionResult");
			boolean isANDrequired=false;
			if(productId!=-1){
				qry.append(" e where");
				qry.append(" e.testRunList.testRunConfigurationChild.testRunConfigurationParent.productMaster.productId=:productId");
				isANDrequired=true;
			}
			if(platformName!=null){
				if(isANDrequired)qry.append(" AND ");else qry.append(" e where");
				qry.append(" e.testRunList.testRunConfigurationChild.productVersionListMaster.devicePlatformVersionListMaster.devicePlatformMaster.devicePlatformName=:platformName");
				isANDrequired=true;
			}			
			
			if(runNo!=-1){
				if(isANDrequired)qry.append(" AND ");else qry.append(" e where");
				qry.append(" e.testRunList.runNo=:runNo");
				isANDrequired=true;
			}
			if(timeFrom!=null){
				if(isANDrequired)qry.append(" AND ");else qry.append(" e where");
				qry.append(" e.testRunList.testRunTriggeredTime>=:timeFrom");
				isANDrequired=true;
			}
			if(timeTo!=null){
				if(isANDrequired)qry.append(" AND ");else qry.append(" e where");
				qry.append(" e.testRunList.testRunTriggeredTime<=:timeTo");
				isANDrequired=true;
			}
			
			Query query = sessionFactory.getCurrentSession().createQuery(qry.toString());
			
			if(productId!=-1){
				query.setParameter("productId", productId);
			}
			if(platformName!=null){
				query.setParameter("platformName", platformName);
			}
			if(runNo!=-1){
				query.setParameter("runNo", runNo);
			}
			if(timeFrom!=null){
				query.setDate("timeFrom", timeFrom);
			}
			if(timeTo!=null){
				query.setDate("timeTo", timeTo);
			}			
			
			
			testExecutionResult = query.setFirstResult(startIndex).setMaxResults(pageSize).list();
			if (!(testExecutionResult == null || testExecutionResult.isEmpty())) {
				for (TestExecutionResult ter : testExecutionResult){
					Hibernate.initialize(ter.getTestRunList().getDeviceList().getDevicePlatformVersionListMaster().getDevicePlatformMaster());
					if(ter.getTestSuiteList().getProductVersionListMaster()!=null){
						Hibernate.initialize(ter.getTestSuiteList().getProductVersionListMaster().getProductMaster());
					}
					Hibernate.initialize(ter.getTestResultStatusMaster());				
				}
			}
			log.debug("list filtered successful");
		} catch (RuntimeException re) {
			log.error("list filtered failed", re);
		}
		return testExecutionResult;
	}
	
	@Override
	@Transactional
	public int getTotalRecordsFiltered(int productId, String platformName,
			int runNo, Date timeFrom, Date timeTo) {
		log.debug("getting TestExecutionResult Filtered total records");
		int count =0;
		try {
			StringBuffer qry= new StringBuffer("select count(*) from TestExecutionResult");
			boolean isANDrequired=false;
			if(productId!=-1){
				qry.append(" e where");
				qry.append(" e.testRunList.testRunConfigurationChild.testRunConfigurationParent.productMaster.productId=:productId");
				isANDrequired=true;
			}
			if(platformName!=null){
				if(isANDrequired)qry.append(" AND ");else qry.append(" e where");
				qry.append(" e.testRunList.testRunConfigurationChild.productVersionListMaster.devicePlatformVersionListMaster.devicePlatformMaster.devicePlatformName=:platformName");
				isANDrequired=true;
			}
			if(runNo!=-1){
				if(isANDrequired)qry.append(" AND ");else qry.append(" e where");
				qry.append(" e.testRunList.runNo=:runNo");
				isANDrequired=true;
			}
			if(timeFrom!=null){
				if(isANDrequired)qry.append(" AND ");else qry.append(" e where");
				qry.append(" e.testRunList.testRunTriggeredTime>=:timeFrom");
				isANDrequired=true;
			}
			if(timeTo!=null){
				if(isANDrequired)qry.append(" AND ");else qry.append(" e where");
				qry.append(" e.testRunList.testRunTriggeredTime<=:timeTo");
				isANDrequired=true;
			}
			
			Query query = sessionFactory.getCurrentSession().createQuery(qry.toString());
			
			if(productId!=-1){
				query.setParameter("productId", productId);
			}
			if(platformName!=null){
				query.setParameter("platformName", platformName);
			}
			if(runNo!=-1){
				query.setParameter("runNo", runNo);
			}
			if(timeFrom!=null){
				query.setDate("timeFrom", timeFrom);
			}
			if(timeTo!=null){
				query.setDate("timeTo", timeTo);
			}
			count=((Number) query.uniqueResult()).intValue();
			
			
			
			log.debug("total Filtered records fetch successful");
		} catch (RuntimeException re) {
			log.error("total Filtered records fetch failed", re);			
		}
		return count;
	
	}
	

	@Override
	@Transactional
	public TestExecutionResult getTestExecutionResultByIdentifier(int testExecutionResultId){
		log.debug("getting TestExecutionResult instance by identifier");
		List<TestExecutionResult> testExecutionResults=null;
		TestExecutionResult testExecutionResult = null;
		try {
			testExecutionResults= sessionFactory.getCurrentSession().createQuery("from TestExecutionResult where testExecutionResultId=:testExecutionResultId")
					.setParameter("testExecutionResultId", testExecutionResultId).list();
			
			if(testExecutionResults != null && !testExecutionResults.isEmpty()){
				testExecutionResult = testExecutionResults.get(0);
				if(testExecutionResult!= null){					
					Hibernate.initialize(testExecutionResult.getTestRunList().getDeviceList());				
				if(testExecutionResult.getTestCaseList()!= null){
					Hibernate.initialize(testExecutionResult.getTestCaseList().getProductFeature());
					}
				}
			}
			log.debug("getTestExecutionResultByIdentifier successful");
		} catch (RuntimeException re) {
			log.error("getTestExecutionResultByIdentifier failed", re);			
		}
		return testExecutionResult;
        
	}

	@Override
	@Transactional
	public List<TestExecutionResult> getTestExecResultsForTestCase(int testRunListId, int testCaseId) {
		log.debug("Getting TestExecution Results specific to RunList and TestCase");
		
		List<TestExecutionResult> testExecutionResults=null;
		try{
			testExecutionResults=sessionFactory.getCurrentSession().createQuery("from TestExecutionResult where testRunListId=:testRunListId and testCaseId=:testCaseId")
					.setParameter("testRunListId", testRunListId).setParameter("testCaseId",testCaseId).list();
			
			if(testExecutionResults != null && testExecutionResults.isEmpty()){
				for(TestExecutionResult testExecutionResult : testExecutionResults){
					Hibernate.initialize(testExecutionResult.getTestCaseList());
					Hibernate.initialize(testExecutionResult.getTestCaseStepsList());					
					Hibernate.initialize(testExecutionResult.getTestResultStatusMaster());
					Hibernate.initialize(testExecutionResult.getTestSuiteList());
				}
			}			
		} catch(Exception e){
			log.error("Error in retrieving testExecResults specific to Runlist and TestCase", e);
			
		}		
		return testExecutionResults;
	}

	@Override
	@Transactional
	public void addTestExportData(TestExecutionResultsExportData testResultsExportData) {
		log.debug("Adding  TestExecution Results Run Id information from the test management system");
		
		try{
			sessionFactory.getCurrentSession().save(testResultsExportData);
		} catch(Exception e){
			log.error("Error in adding the test execution results export data ",e);
		}
		
	}
}