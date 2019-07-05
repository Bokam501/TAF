package com.hcl.atf.taf.service;

import java.util.List;

import com.hcl.atf.taf.model.custom.TestRunReportsDeviceCaseList;

public interface TestRunReportsDeviceCaseListService  {	 
		
		List<TestRunReportsDeviceCaseList> listAllTestRunReportsDeviceCaseResult();
		TestRunReportsDeviceCaseList getByTestRunReportsDeviceCaseListId(int testRunListId);
		List<TestRunReportsDeviceCaseList> listAllTestRunReportsDeviceCaseResult(int startIndex, int pageSize);
		List<TestRunReportsDeviceCaseList> listAllTestRunReportsDeviceCaseResult(int startIndex, int pageSize,Integer testRunListId,Integer testRunConfigurationChildId);
		List<TestRunReportsDeviceCaseList> listAllTestRunReportsDeviceCaseResult(Integer testRunListId,Integer testRunConfigurationChildId);
		int getTotalRecordsOfTestRunReportsDeviceCaseListResult();
		int getTotalRecordsOfTestRunReportsDeviceCaseListResult(Integer testRunList,Integer testRunConfigurationChildId);
		List<TestRunReportsDeviceCaseList> getTestRunReportsByTestRunJobId(int testRunJobId);
}
