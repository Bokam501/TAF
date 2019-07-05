package com.hcl.atf.taf.integration.data;

import java.io.InputStream;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.hcl.atf.taf.model.TestDataItemValues;
import com.hcl.atf.taf.model.TestDataItems;
import com.hcl.atf.taf.model.UIObjectItems;
import com.hcl.atf.taf.model.WorkPackage;
import com.hcl.atf.taf.model.WorkPackageTestCaseExecutionPlan;
import com.hcl.jira.rest.JiraConnector;

public interface TestDataIntegrator {

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
	public String importTestCases(String excelDataFile, int productId, InputStream is);
	String importProductFeatures(String excelDataFile, int productId, InputStream is);

	//Added for iLCM TAF integration - Generating Excel reports  - Bugzilla Id 717
	Boolean exportLocaleReports(int testRunJobId, String exportLocation);
	Boolean exportFeatureReports(int testRunJobId, String exportLocation);	
	boolean importDefectfromBugnizer(String excelDataFile, InputStream is);
	
	public String exportWordTestResults(int testRunListId, String deviceId, String exportLocation);
	String importActivities(HttpServletRequest request,String excelDataFile, int testFactoryId, int productId, 
			InputStream is);
	String importChangeRequests(HttpServletRequest request,String excelDataFile, int productId, InputStream is, Integer entityType);
	
	String importActivityTasks(HttpServletRequest request,String excelDataFile, int activitywpId, InputStream is);
	
	public String importWorkflowTemplateStatus(HttpServletRequest request,String excelDataFile,InputStream is);
		
	public String importEntityWorkflowMappingStatusPolicyForUserandRole(HttpServletRequest request,String excelDataFile,InputStream is,Integer productId);
	public String importSkills(HttpServletRequest request,String excelDataFile,InputStream is);
	public boolean uiObjectsExport(List<UIObjectItems> uiObjectsItemsList,
			String exportLocation, String fileName);
	public boolean testDataExport(List<TestDataItems> testDataItemList,
			String exportLocation, String fileName,
			Integer testDataItemMaxValuesCount);
}
