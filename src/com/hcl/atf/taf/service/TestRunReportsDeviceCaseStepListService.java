package com.hcl.atf.taf.service;

import java.util.List;

import com.hcl.atf.taf.model.custom.TestRunReportsDeviceCaseStepList;

public interface TestRunReportsDeviceCaseStepListService  {	 
		
		List<TestRunReportsDeviceCaseStepList> listAllTestRunReportsDeviceCaseStepResult();
		TestRunReportsDeviceCaseStepList getByTestRunReportsDeviceCaseStepListId(int testRunListId);
		List<TestRunReportsDeviceCaseStepList> listAllTestRunReportsDeviceCaseStepResult(int startIndex, int pageSize);
		List<TestRunReportsDeviceCaseStepList> listAllTestRunReportsDeviceCaseStepResult(int startIndex, int pageSize,Integer testRunListId,Integer testRunConfigurationChildId,Integer testCaseId);
		List<TestRunReportsDeviceCaseStepList> listAllTestRunReportsDeviceCaseStepResult(Integer testRunListId,Integer testRunConfigurationChildId,Integer testCaseId);
		int getTotalRecordsOfTestRunReportsDeviceCaseStepListResult();
		int getTotalRecordsOfTestRunReportsDeviceCaseStepListResult(Integer testRunList,Integer testRunConfigurationChildId,Integer testCaseId);
		List<TestRunReportsDeviceCaseStepList> listAllTestRunReportsDeviceCaseStepResult(Integer testRunListId, Integer testRunConfigurationChildId); // Included for evidence module
		int getTotalRecordsOfEvidence(Integer testRunListId, Integer testRunConfigurationChildId); // Included for evidence module
		//Added for iLCM TAF integration and Generating Excel reports  - Bugzilla Id 717
		List<TestRunReportsDeviceCaseStepList> getByJobId(int testRunJobId);
}
