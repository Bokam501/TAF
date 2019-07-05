package com.hcl.atf.taf.service;

import java.util.Date;
import java.util.List;

import com.hcl.atf.taf.model.TestExecutionResult;
import com.hcl.atf.taf.model.TestRunList;
import com.hcl.atf.taf.model.json.jtable.JTableResponse;

public interface TestReportService  {	 
	
		//TestExecutionResult		
		List<TestExecutionResult> listAllTestExecutionResult();
		List<TestExecutionResult> listTestExecutionResult(int testRunListId);
		List<TestExecutionResult> listTestExecutionResult(int testRunListId,int testSuiteId);
		TestExecutionResult getByTestExecutionResultId(int testExecutionResultId);
		List<TestExecutionResult> listAllTestExecutionResult(int startIndex, int pageSize);  
		List<TestExecutionResult> listTestExecutionResult(int testRunListId,int startIndex, int pageSize);
		List<TestExecutionResult> listTestExecutionResult(int testRunListId,int testSuiteId,int startIndex, int pageSize);
		List<TestExecutionResult> listFilteredTestExecutionResult(int startIndex, int pageSize,int productId,String platformName,int runNo,Date timeFrom,Date timeTo);
		int getTotalRecordsOfTestExecutionResult();
		int getTotalRecordsOfTestExecutionResult(int testRunListId);
		int getTotalRecordsOfTestExecutionResult(int testRunListId,int testSuiteId);
		public int getTotalRecordsFiltered(int productId, String platformName,
				int runNo, Date timeFrom, Date timeTo);
		//TestExecutionResult END
		String generateRunDeviceReport_PDF(TestRunList testRunList, Integer testRunNo, String deviceId, Integer testRunConfigurationChildId) throws Exception;
		String saveTestRunReportToFile_PDF(TestRunList testRunList) throws Exception;
		TestRunList getTestRunListById(int testRunListId) throws Exception;
		
		public JTableResponse downloadReport(Integer workpackageOrJobId, String saveFileLocation) throws Exception;
		
		String evidenceReportHtml(Integer testRunNo,Integer testRunConfigurationChildId,String deviceId,Integer testRunJobId,String reportType,String viewType) throws Exception;
		String saveTestRunReportToFile_HTML(TestRunList testRunList) throws Exception;
		
}
