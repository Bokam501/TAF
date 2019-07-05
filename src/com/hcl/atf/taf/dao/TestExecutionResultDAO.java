package com.hcl.atf.taf.dao;

import java.util.Date;
import java.util.List;

import com.hcl.atf.taf.model.TestExecutionResult;
import com.hcl.atf.taf.model.TestExecutionResultsExportData;
import com.hcl.atf.taf.model.TestRunList;
import com.hcl.atf.taf.model.custom.TestRunListCustom;
import com.hcl.atf.taf.model.json.JsonTestExecutionResult;

public interface TestExecutionResultDAO  {	 
	TestExecutionResult add (TestExecutionResult testExecutionResult);
	void update (TestExecutionResult testExecutionResult);
	void delete (TestExecutionResult testExecutionResult);
	List<TestExecutionResult> listAll();
	List<TestExecutionResult> list(int testRunListId);
	List<TestExecutionResult> list(int testRunListId,int testSuiteId);
	TestExecutionResult getByTestExecutionResultId(int testExecutionResultId);
	List<TestExecutionResult> listAll(int startIndex, int pageSize);  
	List<TestExecutionResult> list(int testRunListId,int startIndex, int pageSize);
	List<TestExecutionResult> list(int testRunListId,int testSuiteId,int startIndex, int pageSize);
	public List<TestExecutionResult> listFilteredTestExecutionResult(
			int startIndex, int pageSize, int productId, String platformName,
			int runNo, Date timeFrom, Date timeTo); 
	int getTotalRecords();
	int getTotalRecords(int testRunListId);
	int getTotalRecords(int testRunListId,int testSuiteId);
	int getTotalRecordsFiltered(int productId, String platformName, int runNo,
			Date timeFrom, Date timeTo);
	List<TestRunListCustom> getExecutedStepsCountofTestRunlist(List<TestRunList> testRunList);
	TestExecutionResult processTestExecutionResult(TestExecutionResult testExecutionResult, JsonTestExecutionResult jsonTestExecutionResult);
	TestExecutionResult getTestExecutionResultByIdentifier(int testExecutionResultId);
	
	//Get the Test Execution Result specific to Runlist and Test Case
	List<TestExecutionResult> getTestExecResultsForTestCase(int testRunListId, int TestCaseId);
	
	//Add the TestResults Export data 
	void addTestExportData(TestExecutionResultsExportData testResultsExportData);
}
