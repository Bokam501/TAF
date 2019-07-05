package com.hcl.atf.taf.dao;

import java.util.List;

import com.hcl.atf.taf.model.custom.TestRunReportsDeviceCaseList;
import com.hcl.atf.taf.model.custom.TestRunReportsDeviceList;
import com.hcl.atf.taf.model.custom.TestRunReportsList;

public interface TestRunReportsDeviceCaseListDAO  {
	List<TestRunReportsDeviceCaseList> listAll();
	List<TestRunReportsDeviceCaseList> listAll(int startIndex, int pageSize);
	TestRunReportsDeviceCaseList getByTestRunReportsDeviceCaseListId(int testRunListId);
	List<TestRunReportsDeviceCaseList> list(int startIndex, int pageSize,Integer testRunListId,Integer testRunConfigurationChildId);
	List<TestRunReportsDeviceCaseList> list(Integer testRunListId,Integer testRunConfigurationChildId);
	int getTotalRecords();
	int getTotalRecordsFilteredTestRunReportsDeviceCaseList(Integer testRunListId,Integer testRunConfigurationChildId);
	//Added for iLCM TAF integration - Generating Excel reports  - Bugzilla Id 717
	public List<TestRunReportsDeviceCaseList> getTestRunReportsByTestRunJobId(int testRunJobId); 
}
