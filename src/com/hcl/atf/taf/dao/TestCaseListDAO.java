package com.hcl.atf.taf.dao;

import java.util.Date;
import java.util.List;
import java.util.Set;

import com.hcl.atf.taf.model.ProductMaster;
import com.hcl.atf.taf.model.ProductVersionListMaster;
import com.hcl.atf.taf.model.TestCaseList;
import com.hcl.atf.taf.model.TestCaseScript;
import com.hcl.atf.taf.model.TestCaseStepsList;
import com.hcl.atf.taf.model.TestExecutionResult;
import com.hcl.atf.taf.model.TestSuiteList;
import com.hcl.atf.taf.model.dto.TestCaseListDTO;
import com.hcl.atf.taf.model.dto.WorkPackageTCEPResultSummaryDTO;
import com.hcl.atf.taf.model.json.terminal.JsonTestExecutionResult;

public interface TestCaseListDAO  {	 
	int add (TestCaseList testCaseList);
	Integer update (TestCaseList testCaseList);
	TestCaseList update (TestCaseList testCaseList,Integer count, Integer maxLimit);

	void updateTestCasesManually (TestCaseList testCaseList);
	void delete (TestCaseList testCaseList);
	List<TestCaseList> list();
	List<TestCaseList> list(int testSuiteId);		
	List<TestCaseList> list(int startIndex, int pageSize);
	List<TestCaseList> list(int testSuiteId,int startIndex, int pageSize);
	TestCaseList getByTestCaseId(int testCaseId);
	int getTotalRecords();
	
	TestCaseList getTestCaseByName(String testCaseName, int productId);
	TestCaseStepsList getTestCaseStepByName(String testCaseStepName, int productId);
	int addTestCaseStep(TestCaseStepsList testCaseStep);
	List<TestCaseList> getTestCasesByName(String testCaseName);
	List<TestCaseStepsList> getTestCaseStepsByName(String testCaseStepName);
	List<TestCaseList> getTestCaseListByProductId(int productId, Integer jtStartIndex, Integer jtPageSize);
	TestCaseList getTestCaseByCode(String testCaseCode);
	Boolean IsTestCaseExistsByCode(String testCaseCode);
	TestCaseList getTestCaseByCodeProduct(String testCaseCode, int productId);
	TestCaseList getTestCaseByNameProduct(String testCaseName, int productId);
	//Changes for Test Management tools integration
	List<TestCaseList> getTestCaseByCodeNameProduct(String testCaseCode,String testCaseName, int productId);
	int addTestCase(int testCaseId, int testSuiteId);
	List<TestCaseList> listAllTestCases(int productId);
	List<TestCaseList> listTestCases(int testSuiteId);
	int addTestCasetoProductVersion(int testcaseId, int versionId);	
	TestCaseList getTestCaseByTestCaseCodeTestCaseId(String testCaseCode,Integer testCaseId, int productId);
	TestCaseList getExistingTestCaseForTER(TestExecutionResult testExecutionResult, JsonTestExecutionResult jsonTestExecutionResult, ProductMaster product);
	TestCaseList getByTestCaseCode(String testCaseCode, ProductMaster product);
	Set<TestSuiteList> getTestSuiteSetByTestCaseId(int testCaseId);	
	public TestSuiteList updateTestCasesTestSuites(int testSuiteId, int testCaseId);
	TestCaseList listBytestSuiteId(int testSuiteId);
	public List<TestCaseList> getTestCaseByProductVersionId(int startIndex, int pageSize,int productVersionListId);
	List<TestCaseList> getTestCaseListByProductIdByType(int productId,int executionType);
	public List<ProductVersionListMaster> getTestCasesVersions(int productId, int testCaseId);
	TestCaseList processTestExecutionResult(TestCaseList testCaseList,JsonTestExecutionResult jsonTestCase);
	TestCaseList processTestExecutionResult(TestCaseList testCaseList, JsonTestExecutionResult jsonWorkPackageTestCaseExecutionPlanForTerminal, Integer testSuiteId);
	List<TestCaseList> getTestCasesMappedToFeature(Integer productFeatureId);
	void updateTestCasesInImport(List<TestCaseListDTO> modifiedTestCasesListDTO, String tcAddOrUpdateAction, Integer maxBatchProcessingLimit);
	Integer getTestCaseListSize(Integer productId);
	TestCaseList getByTestCaseIdBare(int testCaseId);
	Integer getUnMappedFeatureCountOfTestCaseByTestCaseId(int productId,
			int testCaseId);
	List<Object[]> getUnMappedFeatureByTestCaseId(int productId,
			int testCaseId, Integer jtStartIndex, Integer jtPageSize);
	List<Object[]> getMappedTestCaseListByProductFeatureId(int productId,
			int productFeatureId, Integer jtStartIndex, Integer jtPageSize);
	List<Object[]> getMappedFeatureBytestCaseId(int productId, int testCaseId,
			Integer jtStartIndex, Integer jtPageSize);

	TestCaseList updateTestSuiteTestCasesOneToMany(int testCaseId,
			int testSuiteId, String maporunmap);
	List<String> getExistingTestStepsCodes(TestCaseList testCase, ProductMaster product);
	List<String> getExistingTestStepsNames(TestCaseList testCase, ProductMaster product);
	List<String> getExistingTestCaseCodes(ProductMaster product);
	List<String> getExistingTestCaseNames(ProductMaster product);
	List<TestCaseList> listAllProductTestCasesByLastSyncDate(int startIndex,int pageSize, Date startDate,Date endDate);
	List<WorkPackageTCEPResultSummaryDTO> getWPTestCaseExecutionResultSummary(Integer testCaseId, Integer workPackageId, Integer productId, String dataLevel);
	void deleteTCWithMappings(TestCaseList testCaseList);	
	int unmapTestcaseFromFeatureMapping(Integer testCaseId);
	TestCaseList reloadTestCase(TestCaseList testCase);
	int unmapTestcaseFromVersionMapping(Integer testCaseId);
	int unmapTestcaseFromTestSuiteMapping(Integer testCaseId);
	int unmapTestcaseFromDecouplingCategoryMapping(Integer testCaseId);
	TestCaseList getTestCaseByNameInTestSuite(String testCaseName, int productId, Integer testSuiteId);
	TestCaseStepsList getTestCaseStepByName(String testCaseStepName, int productId, int testCaseId);
	List<Object[]> getUnMappedTestScriptsByProductId(int productId, int testCaseId, int jtStartIndex, int jtPageSize);
	List<Object[]> getMappedTestScriptsByTestCaseId(int testCaseId);
	int getUnMappedTestScriptsCountByProductId(int productId, int testCaseId);
	void updateTestCaseScriptToTestCase(Integer testCaseId, Integer scriptId, String maporunmap);
	
	int addBulkTestCases(List<TestCaseList> testCaseList, int batchSize);
	List<TestCaseScript> getMappedTestScriptsByProductIdAndTestcaseId(int productId, int testCaseId);
	List<TestCaseList> listTescaseforPredecessors(Set<Integer> testCaseIds);
	TestCaseList getTestCaseByNameAndCode(String testCaseName,String testCaseCode, int productId);
	
	Date getLatestTestcaseCreatedDate(String tableName,String dateField);
	TestCaseList getSimilarTestCasesByName(String testCaseName, int productId);
	TestCaseList getByTestCaseIdwithoutInitialization(int testCaseId);
}
