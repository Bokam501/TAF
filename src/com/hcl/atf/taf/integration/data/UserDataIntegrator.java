package com.hcl.atf.taf.integration.data;

import java.io.InputStream;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.hcl.atf.taf.model.WorkPackage;
import com.hcl.atf.taf.model.WorkPackageTestCaseExecutionPlan;
import com.hcl.jira.rest.JiraConnector;

public interface UserDataIntegrator {

	public boolean exportTestCases();
	public boolean exportTestExecutionResults();
	public boolean importTestCases();
	public boolean exportTestCases(int productId, String exportLocation);
	public boolean exportTestExecutionResults(int testSuiteId);
	public boolean importTestCases(String excelDataFile, int productId,
			String testCaseSource);
	public String exportDefects(int testRunNo, int testRunConfigChild, String exportLocation);
	public String exportDeviceDefects(int testRunNo, int testRunConfigurationChildId, int deviceListId, String exportLocation);		
	public boolean exportTestResults(int testRunNo, String exportLocation);
	//Jira specific defects export
	public int exportDefectsToJira(int testRunNo, int testRunConfigChild, JiraConnector jiraConnector);
	public int exportDeviceDefectsToJira(int testRunNo, int testRunConfigurationChildId, int deviceListId, JiraConnector jiraConnector);	
	public int exportDefectToJira(int testExecResultBugId,  JiraConnector jiraConnector);
	public boolean workPackageTestCaseExecutionPlanDataExport(List<WorkPackageTestCaseExecutionPlan> workPackageTestCaseExecutionPlanList,WorkPackage workPackage, String exportLocation);
	public boolean importTestCases(String excelDataFile, int productId, InputStream is);
	public String importUserLists(HttpServletRequest request, String excelDataFile, InputStream is, String userType);	
	public String importResourceDemandWeekly(HttpServletRequest request,String excelDataFile, InputStream is, Integer workPackageId, Integer shiftId,String demandUploadPermission);
}
