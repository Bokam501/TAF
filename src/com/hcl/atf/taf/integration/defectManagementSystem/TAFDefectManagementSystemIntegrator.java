package com.hcl.atf.taf.integration.defectManagementSystem;

import java.util.List;
import java.util.Set;

import com.hcl.atf.taf.model.TestExecutionResultBugList;
import com.hcl.jira.rest.JiraConnector;

public interface TAFDefectManagementSystemIntegrator {
	
	public String fileDefectToJira(int testExecResultBugId,  JiraConnector jiraConnector);
	public String fileDefectsToJira(int testRunNo, int testRunConfigChild, JiraConnector jiraConnector);	
	public String fileDefectsToJira(int workpackageId, JiraConnector jiraConnector);	
	public String fileDefectsToJira(int workpackageId,JiraConnector jiraConnector,TestExecutionResultBugList testExecutionResultBugList);		

	public String fileDeviceDefectsToJira(int testRunNo, int testRunConfigurationChildId, int deviceListId, JiraConnector jiraConnector);
	public String jiraDefectsFile(Set<TestExecutionResultBugList> testExecutionResultBugsList,JiraConnector jiraConnector);	
	public String jiraDefectsFile(List<TestExecutionResultBugList> testExecutionResultBugsList,JiraConnector jiraConnector);	

}
