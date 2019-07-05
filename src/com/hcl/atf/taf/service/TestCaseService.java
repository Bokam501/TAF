package com.hcl.atf.taf.service;

import java.util.HashMap;
import java.util.List;

import com.hcl.atf.taf.model.ProductFeature;
import com.hcl.atf.taf.model.ProductMaster;
import com.hcl.atf.taf.model.ProductVersionListMaster;
import com.hcl.atf.taf.model.TestCaseList;
import com.hcl.atf.taf.model.TestCasePriority;
import com.hcl.atf.taf.model.TestCaseStepsList;
import com.hcl.atf.taf.model.TestSuiteList;
import com.hcl.atf.taf.model.TestcaseTypeMaster;
import com.hcl.atf.taf.model.json.JsonFeatureTestCaseDefect;
import com.hcl.atf.taf.model.json.JsonTestCaseList;
import com.hcl.atf.taf.model.json.JsonTestExecutionResultBugList;
import com.hcl.atf.taf.model.json.JsonTestRunPlan;
import com.hcl.atf.taf.model.json.JsonWorkPackageTestCaseExecutionPlanForTester;
import com.hcl.atf.taf.model.json.JsonWorkPackageTestCaseExecutionResultSummary;
import com.hcl.atf.taf.model.json.terminal.JsonTestExecutionResult;

public interface TestCaseService {

	List<TestCaseList> getTestCaseListByProductId(int productId, Integer jtStartIndex, Integer jtPageSize);
	TestCaseList getTestCaseById(int testCaseId);
	int getTotalRecordsOfTestCases();
	int addTestCase(TestCaseList testCaseList);
	int addTestCaseReturnId(TestCaseList testCaseList);
	void update (TestCaseList testCaseList);
	void delete(TestCaseList testCaseList);
	List<TestCaseStepsList> listTestCaseSteps(int testCaseId);
	int getTotalRecordsOfTestCaseSteps();
	public TestSuiteList updateTestCasesTestSuites(int testSuiteId, int testCaseId); 
	public List<TestCaseList> getTestCaseByProductVersionId(int startIndex, int pageSize,int productVersionListId);
	TestCaseList updateTestSuiteTestCasesOneToMany(int testCaseId,	int testSuiteId, String maporunmap);
	List<TestCasePriority> listTestCasePriority();
	List<TestcaseTypeMaster> listTestcaseTypeMaster();
	TestCasePriority getTestCasePriorityBytestcasePriorityId(int testcasePriorityId);
	TestCasePriority getPrioirtyByName(String testcasePriorityName);
	List<TestCaseList> getTestCaseListByProductIdByType(int productId,int executionType);
	TestcaseTypeMaster getTestcaseTypeMasterBytestcaseTypeId(int testcaseTypeId);
	TestcaseTypeMaster getTestcaseTypeMasterByName(String name);
	
	public List<ProductVersionListMaster> getTestCasesVersions(int productId, int testCaseId);
	public List<JsonTestCaseList> getTestCaseListWithProductVersionMappingByProductId(Integer productId);
	TestCaseList processTestExecutionResult(TestCaseList testCaseList,JsonTestExecutionResult jsonTestCase);
	public List<TestCaseList> getTestCasesMappedToFeature(Integer productFeatureId);
	public List<JsonFeatureTestCaseDefect> getFeatureTestCaseDefect(Integer productId, List<ProductFeature> featureList);
	List<JsonTestExecutionResultBugList> getFeatureTestCaseDefectList(
			Integer testCaseId);
	HashMap<Integer, JsonWorkPackageTestCaseExecutionPlanForTester> getFeatureTestCaseExecutionResultList(
			Integer testCaseId);
	
	Integer getTestCaseListSize(Integer productId);
	List<JsonFeatureTestCaseDefect> getFeatureTestCaseDefectCount(Integer testCaseId);
	Integer getUnMappedFeatureCountOfTestCaseByTestCaseId(int productId, int testCaseId);
	List<Object[]> getUnMappedFeatureByTestCaseId(int productId, int testCaseId, Integer jtStartIndex, Integer jtPageSize);
	List<Object[]> getMappedTestCaseListByProductFeatureId(int productId,
			int productFeatureId, Integer jtStartIndex, Integer jtPageSize);
	List<Object[]> getMappedFeatureBytestCaseId(int productId, int testCaseId,
			Integer jtStartIndex, Integer jtPageSize);
	TestCaseList getTestCaseByCode(String testCaseCode);
	TestCaseList getTestCaseByCode(String testCaseCode, ProductMaster product);
	List<String> getExistingTestCaseCodes(ProductMaster product);
	List<String> getExistingTestCaseNames(ProductMaster product);
	List<String> getExistingTestStepsCodes(TestCaseList testCase, ProductMaster product);
	List<String> getExistingTestStepsNames(TestCaseList testCase, ProductMaster product);
	int addTestStepsBulk(List<TestCaseStepsList> testStepsList);
	List<JsonWorkPackageTestCaseExecutionResultSummary> getTestCaseExecutionResultSummary(Integer testCaseId, Integer workPackageId, Integer productId, String dataLevel);
	void deleteTCWithMappings(TestCaseList testCaseList);
	int unmapTestcaseFromFeatureMapping(Integer testCaseId);
	List<JsonTestRunPlan> getTestCaseTestRunPlans(Integer testCaseId);
	TestCaseList processTestExecutionResult(TestCaseList testCaseList, JsonTestExecutionResult jsonTestExecutionResult,Integer testSuiteId);
	TestCaseStepsList getTestCaseStepByCode(String testStepCode, ProductMaster product);
	List<Object[]> getUnMappedTestScriptsByProductId(int productId, int testCaseId, int jtStartIndex, int jtPageSize);
	List<Object[]> getMappedTestScriptsByTestCaseId(int testCaseId);
	int getUnMappedTestScriptsCountByProductId(int productId, int testCaseId);
	void updateTestCaseScriptToTestCase(Integer testCaseId, Integer scriptId, String maporunmap);
	int addBulkTestCases(List<TestCaseList> testCaseList);
	void updateTestCasePredecessors(TestCaseList testcase, String predecessorsString);
	Integer getMappedFeatureCountByTestcaseId(int testCaseId);
	List<TestCaseList> getTestCasePredecessorByTestCaseId(Integer testCaseId);
	List<TestCaseList> getSimilarToTestCaseSByTestCaseId(Integer testCaseId);
	List<TestCaseStepsList> listTestCaseStepsByStatus(int testCaseId,Integer statu);
	TestCaseList getTestCaseByName(String testCaseName, int productId);
	TestCaseList getTestCaseByNameAndCode(String testCaseName,String testCaseCode, int productId);
	TestCaseList getSimilarTestCasesByName(String testCaseName, int productId);
	TestCaseList getByTestCaseIdwithoutInitialization(int testCaseId);
	int getTotalRecordsOfTestCaseStepsByTestCaseId(int testCaseId);
}
