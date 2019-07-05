package com.hcl.atf.taf.dao;

import java.util.List;

import com.hcl.atf.taf.model.ProductMaster;
import com.hcl.atf.taf.model.TestCaseStepsList;
import com.hcl.atf.taf.model.TestExecutionResult;
import com.hcl.atf.taf.model.json.JsonTestExecutionResult;

public interface TestCaseStepsListDAO  {	 
	void add (TestCaseStepsList testCaseStepsList);
	void update (TestCaseStepsList testCaseStepsList);
	void update (TestCaseStepsList testCaseStepsList,Integer count);

	void delete (TestCaseStepsList testCaseStepsList);
	List<TestCaseStepsList> list();
	List<TestCaseStepsList> list(int testCaseId);		
	List<TestCaseStepsList> list(int startIndex, int pageSize);
	List<TestCaseStepsList> list(int testCaseId,int startIndex, int pageSize);
	TestCaseStepsList getByTestCaseId(int testCaseId);
	TestCaseStepsList getByTestStepId(int testStepId);
	TestCaseStepsList getByTestStepName(String testStepName);
	int getTotalRecords();
	//Changes for Test Management tools integration
	TestCaseStepsList getTestCaseStepsByCodeAndProduct(String testStepCode, int productId);
	TestCaseStepsList getTestCaseStepByTestStepCodeAndTestStepId(String testStepCode, Integer testStepId, int productId);
	TestCaseStepsList getExistingTestStepForTER(TestExecutionResult testExecutionResult, JsonTestExecutionResult jsonTestExecutionResult, ProductMaster product);
	TestCaseStepsList getByTestStepCode(String testStepCode, ProductMaster product);
	int addBulk(List<TestCaseStepsList> testCaseSteps, Integer batchSize);
	List<TestCaseStepsList> getTestCaseStepsByProductId(int productId);
	void updateTestCaseStepsListInImport(List<TestCaseStepsList> modifiedTestCaseStepsList,
			String tcAddOrUpdateAction, Integer maxBatchProcessingLimit);
	TestCaseStepsList updateTestCaseStepsList(TestCaseStepsList testCaseStepsList, Integer count, Integer maxLimit);
	TestCaseStepsList getTestCaseStepByCodeProduct(String testStepCode, int productId);
	List<TestCaseStepsList> getTestStepListByStatus(int testCaseId,Integer status);
	int getTotalRecordsOfTestCaseStepsByTestCaseId(int testCaseId);
}
