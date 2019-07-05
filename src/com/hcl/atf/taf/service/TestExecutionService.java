package com.hcl.atf.taf.service;

import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import com.hcl.atf.taf.controller.TestRunExecutionStatusVO;
import com.hcl.atf.taf.model.DeviceList;
import com.hcl.atf.taf.model.TestCaseList;
import com.hcl.atf.taf.model.TestExecutionResult;
import com.hcl.atf.taf.model.TestExecutionResultsExportData;
import com.hcl.atf.taf.model.TestRunConfigurationChild;
import com.hcl.atf.taf.model.TestRunList;
import com.hcl.atf.taf.model.TestRunPlan;
import com.hcl.atf.taf.model.TestSuiteList;
import com.hcl.atf.taf.model.TrccExecutionPlan;
import com.hcl.atf.taf.model.TrccExecutionPlanDetails;
import com.hcl.atf.taf.model.custom.TestRunListCustom;
import com.hcl.atf.taf.model.json.JsonTestExecutionResult;

public interface TestExecutionService  {
	
	List<DeviceList> getTotalDevicesInTestRunConfigurationChild(int testRunConfigurationChildId);
	List<DeviceList> getTotalDevicesInTestRunConfigurationChild(TestRunConfigurationChild testRunConfigurationChild);
	Integer getAverageTestRunExecutionTime(TestRunList testRunList);
	void updateTestRunListEvidenceStatus (TestRunList testRunList);

	//TestExecutionResult - START
	TestExecutionResult addTestExecutionResult (TestExecutionResult testExecutionResult);
	void updateTestExecutionResult (TestExecutionResult testExecutionResult);
	void deleteTestExecutionResult (TestExecutionResult testExecutionResult);
	
	List<TestExecutionResult> listAllTestExecutionResult();
	List<TestExecutionResult> listTestExecutionResult(int testRunListId);
	List<TestExecutionResult> listTestExecutionResult(int testRunListId,int testSuiteId);
	List<TestExecutionResult> listAllTestExecutionResult(int startIndex, int pageSize);  
	List<TestExecutionResult> listTestExecutionResult(int testRunListId,int startIndex, int pageSize);
	List<TestExecutionResult> listTestExecutionResult(int testRunListId,int testSuiteId,int startIndex, int pageSize);

	TestExecutionResult getByTestExecutionResultId(int testExecutionResultId);

	int getTotalRecordsOfTestExecutionResult();
	int getTotalRecordsOfTestExecutionResult(int testRunListId);
	int getTotalRecordsOfTestExecutionResult(int testRunListId,int testSuiteId);

	//Get the Test Execution Result specific to Runlist and Test Case
	List<TestExecutionResult> getTestExecResultsForTestCase(int testRunListId, int TestCaseId);
	// Adding data to TestExecutionResultsExportData on export of Test results
	void addTestExecutionResultsExportData(TestExecutionResultsExportData testExecutionResultsExportData);
	TestExecutionResult processTestExecutionResult(TestExecutionResult testExecutionResult, JsonTestExecutionResult jsonTestExecutionResult);
	List<TestRunListCustom> getExecutedStepsCountofTestRunlist(List<TestRunList> testRunList);
	//TestExecutionResult - END

	//TestRunList - START
	void addTestRunList (TestRunList testRunList);
	void updateTestRunList (TestRunList testRunList);
	void deleteTestRunList (TestRunList testRunList);
	
	List<TestRunList> listAllTestRunList();
	List<TestRunList> listTestRunList(int testRunConfigurationChildId);
	List<TestRunList> listAllTestRunList(int startIndex, int pageSize);  
	List<TestRunList> listTestRunList(int testRunConfigurationChildId,int startIndex, int pageSize);
	
	TestRunList getByTestRunListId(int testRunListId);
	List<TestRunList> listByHostId(int hostId, String testStatus);
	List<TestRunList> listExecutedTestRunList(int hours,int startIndex,int pageSize);
	List<TestRunList> listExecutingTestRunList();
	int getTotalTestRunListInLast24Hours(int hours);
	
	int getTotalRecordsOfTestRunList();	
	int getTotalRecordsOfTestRunList(int testRunConfigurationChildId);
	
	void clearTestRunList();
	//TestRunList - END
	
	//Test Execution Plan Methods - Start
	List<TrccExecutionPlan> listTrccExecutionPlan(int testRunConfigurationChildId);
	void addTrccExecutionPlan (TrccExecutionPlan trccExecutionPlan);
	void updateTrccExecutionPlan (TrccExecutionPlan trccExecutionPlan);
	void deleteTrccExecutionPlan (int trccExecutionPlanId);
	int getTotalRecordsOfTrccExecutionPlan(int testRunConfigurationChildId);
	TrccExecutionPlan getTrccExecutionPlanByName(String planName);
	TrccExecutionPlan getTrccExecutionPlanById(int trccExecutionPlanId);
	//Test Execution Plan Methods - End
	
	//Test Execution Plan Details Methods - Start
	void addTrccExecutionPlanDetail(int trccExecutionPlanId, int deviceListId, String[] testCaseLists);
	void updateTrccExecutionPlanDetail(TrccExecutionPlanDetails trccExecutionPlanDetails,int trccExecutionPlanId, int deviceListId);
	List<TrccExecutionPlanDetails> listTrccExecutionPlanDetails(int trccExecutionPlanId, int deviceListId);
	List<TestCaseList> getSelectedTestCasesFromPlan(int testRunListId);
	List<TestCaseList> getSelectedTestCasesFromPlan(TestRunList testRunList);
	//Test Execution Plan Details Methods - End
	
	//Test Run Execution calls - START
	TestRunExecutionStatusVO executeTestRun(int testRunConfigurationChildId);
	TestRunExecutionStatusVO executeTestRun(TestRunConfigurationChild testRunConfigurationChild);
	List<TestRunExecutionStatusVO> executeTestRuns(int testRunConfigurationParentId);
	List<TestRunExecutionStatusVO> executeTestRuns(int testRunConfigurationParentId, HttpServletRequest request);
	//Test Run Execution calls - END
	
	//Selective Test Cases Execution Module - Execution calls - START
	TestRunExecutionStatusVO executeTestRunPlan(int testRunConfigurationChildId, int trccExecutionPlanId);
	TestRunExecutionStatusVO executeTestRunPlan(TestRunConfigurationChild testRunConfigurationChild, TrccExecutionPlan trccExecutionPlan);
	TestRunExecutionStatusVO executeTestRunPlanOnADevice(int testRunConfigurationChildId, int trccExecutionPlanId, int deviceListId);
	List<TestRunExecutionStatusVO> executeTestRunPlans(int testRunConfigurationChildId);
	//Selective Test Cases Execution Module - Execution calls - END	
	String prepareBDDTestScriptsForJobExecution(TestSuiteList testSuite, int testRunJobId);
	String prepareBDDTestScriptsForJobExecution(TestSuiteList testSuite, int testRunJobId, int testCaseId);
	String prepareBDDTestScriptsForJobExecution(TestSuiteList testSuite, int testRunJobId, Set<TestCaseList> testCases);
	String prepareBDDTestScriptsForJobExecution(TestSuiteList testSuite, int testRunJobId, Set<TestCaseList> testCases, TestRunPlan testRunPlan);
	
}
