package com.hcl.atf.taf.service;

import java.util.Date;
import java.util.List;
import java.util.Set;

import com.hcl.atf.taf.model.ProductMaster;
import com.hcl.atf.taf.model.ProductVersionListMaster;
import com.hcl.atf.taf.model.ScriptTypeMaster;
import com.hcl.atf.taf.model.TestCaseList;
import com.hcl.atf.taf.model.TestCaseStepsList;
import com.hcl.atf.taf.model.TestExecutionResultsExportData;
import com.hcl.atf.taf.model.TestSuiteList;
import com.hcl.atf.taf.model.dto.TestCaseListDTO;
import com.hcl.atf.taf.model.json.jtable.JTableSingleResponse;

public interface TestSuiteConfigurationService {
	int addTestSuite (TestSuiteList testSuiteList);	
	int addTestSuiteMapping (int testSuiteId, int testSuiteIdtobeMapped);	
	void updateTestSuite (TestSuiteList testSuiteList);
	void deleteTestSuite (int testSuiteId);
	List<TestSuiteList> listTestSuite();
	TestSuiteList getByTestSuiteId(int testSuitId);
	List<TestSuiteList> listTestSuite(int startIndex, int pageSize);  
	Set<TestSuiteList> getTestSuiteByProductId(int productId);
	int getTotalRecordsOfTestSuite();
	
	int addTestCase (TestCaseList testCaseList);
	void updateTestCase (TestCaseList testCaseList);
	void updateTestCaseManually (TestCaseList testCaseList);
	void deleteTestCase (int testCaseId);
	List<TestCaseList> listTestCase();
	List<TestCaseList> listTestCase(int testSuiteId);		
	List<TestCaseList> listTestCase(int startIndex, int pageSize);
	List<TestCaseList> listTestCase(int testSuiteId,int startIndex, int pageSize);
	TestCaseList getByTestCaseId(int testCaseId);
	
	List<TestSuiteList> testSuitesList(int productVersionListId);
	List<ScriptTypeMaster> testScriptTypeList();
	ScriptTypeMaster getScriptTypeMasterByscriptType(String scriptTypeName);
	List<TestSuiteList> listMappedTestSuite(int testSuiteId);	
	
	void updateTestCaseSteps (TestCaseStepsList testCaseStepsList);
	void deleteTestCaseSteps (int testCaseId);
	List<TestCaseStepsList> listTestCaseSteps();
	List<TestCaseStepsList> listTestCaseSteps(int testCaseId);		
	List<TestCaseStepsList> listTestCaseSteps(int startIndex, int pageSize);
	List<TestCaseStepsList> listTestCaseSteps(int testCaseId,int startIndex, int pageSize);
	TestCaseStepsList getByTestCaseStepsId(int testCaseId);
	TestCaseStepsList getByTestStepId(int testStepId);
	int getTotalRecordsOfTestCaseSteps();
	
	TestCaseList getTestCaseByName(String testCaseName, int productId);
	TestCaseStepsList getTestCaseStepByName(String testCaseStepName, int productId);
	int addTestCaseStep(TestCaseStepsList testCaseStep);
	List<TestSuiteList> listTestSuite(int status);
	List<TestSuiteList> listTestSuite(int startIndex, int pageSize,
			int status);
	void reactivateTestSuite(int testSuiteId);
	void addTestCases(List<TestCaseList> newTestCases);
	public List<TestSuiteList> getByProductId(Integer startIndex, Integer pageSize,int productId);
	public List<TestSuiteList> getByProductId(int productId);
	List<TestCaseList> getTestCaseListByProductId(int productId);
	void updateTestCasesInImport(List<TestCaseListDTO> newTestCases, String tcAddOrUpdateAction);
	void updateTestCases(List<TestCaseList> testCases);
	public List<TestSuiteList> getByProductVersionId(int startIndex, int pageSize,int productVersionListId);
	
	//Changes for integration of TestManagement tools
	TestCaseList getTestCaseByCode(String testCaseCode);
	Boolean IsTestCaseExistsByCode(String testCaseCode);
	
	
	TestCaseList getTestCaseByCodeProduct(String testCaseCode,int productId);	
	TestCaseList getTestCaseByNameProduct(String testCaseName, int productId);
	
	List<TestCaseList> getTestCaseByCodeNameProduct(String testCaseCode,String testCaseName, int productId);
	
	//Changes for Test Management tools integration
	void addTestSuites(List<TestSuiteList> testSuites);	
	void addTestCasesAndSteps(List<TestCaseList> newTestCases);
	void updateTestSuites(List<TestSuiteList> testSuites);
	TestSuiteList getByTestSuiteCode(String testSuiteCode);	
	TestSuiteList getByProductTestSuiteCode(int productId, String testSuiteCode);	
	
	
	TestCaseStepsList getTestCaseStepsByCodeAndProduct(String testStepCode, int productId);
	void addTestCaseSteps(List<TestCaseStepsList> testCaseSteps);
	void updateTestCaseStepsLists(List<TestCaseStepsList> testCaseStepsLists);
	void updateTestSuiteTestCase(Set<TestCaseList> TestCases, TestSuiteList testSuite);
	void updateTestSuiteList(TestSuiteList testSuiteList);
	List<TestSuiteList> filterTestSuites(int jtStartIndex, int jtPageSize, Integer productId);
	int addTestCase(int testCaseId, int testSuiteId);
	int add(TestCaseList testCaseList);
	List<TestCaseList> listAllTestCases(int productId);
	int addTestCasetoProductVersion(int testcaseId, int versionId);
	String constructTestScriptFileNameForExternalScriptSource(TestSuiteList testSuiteList, int testRunListId, String testSuitePath);
	List<TestSuiteList> getByProductVersionId(Integer jtStartIndex,Integer jtPageSize,Integer versionId,Integer testRunPlanId);
	int updateTestCasetoProductVersionMapping(int testcaseId, String productVersionName, String mappingStatus);
	int updateTCtoPVMapping(int testcaseId, int productVersionId,String mappingStatus);
	
	boolean isVersionTestSuiteExistingByName(String testSuiteName, Integer versionId);
	ProductVersionListMaster getLatestProductVersion(ProductMaster productMaster);
	TestCaseList getByTestCaseIdBare(int testCaseId);
	void addTestExecutionResultsExportData(TestExecutionResultsExportData testExecutionResultsExportData);
	List<TestCaseList> listTestCaseByTestSuite(int testSuiteId, int jtStartIndex, int jtPageSize);
	TestCaseStepsList getTestCaseStepByName(String testCaseStepName, int productId, int testCaseId);
	void updateTestCaseStepsListInImport(List<TestCaseStepsList> listOfTestStepsList, String tcAddOrUpdateAction);
	List<TestSuiteList> returnVersionTestSuiteId(String testSuiteName,Integer versionId);
	Integer countAllTestSuites(Date startDate, Date endDate);
	List<TestSuiteList> listAllTestSuites(int startIndex, int pageSize,Date startDate, Date endDate);
	List<TestSuiteList> getTestSuitesMappedToTestCaseByTestCaseId(int testCaseId, Integer startIndex, Integer pageSize);
	JTableSingleResponse updateTestSuiteScriptPack(Integer testSuiteId, String testSuiteName, Integer productId,
			String testSuitePath, String testScriptPackSource);
	TestSuiteList getByTestSuiteName(String testSuiteName);
	void delAllTestCaseMappingsByTestCaseId(Integer testCaseId);
	TestSuiteList getByProductIdAndTestSuiteNandAndTestSuiteCode(int productId, String testSuiteName,String testSuiteCode);
	TestSuiteList getTestSuiteByProductIdOrTestSuiteNandOrTestSuiteCode(int productId,String testSuiteName,String testSuiteCode);
}
