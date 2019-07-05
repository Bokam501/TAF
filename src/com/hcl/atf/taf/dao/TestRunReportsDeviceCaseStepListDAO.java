package com.hcl.atf.taf.dao;

import java.util.List;

import com.hcl.atf.taf.model.custom.TestRunReportsDeviceCaseList;
import com.hcl.atf.taf.model.custom.TestRunReportsDeviceCaseStepList;
import com.hcl.atf.taf.model.custom.TestRunReportsDeviceList;
import com.hcl.atf.taf.model.custom.TestRunReportsList;

public interface TestRunReportsDeviceCaseStepListDAO  {
	List<TestRunReportsDeviceCaseStepList> listAll();
	List<TestRunReportsDeviceCaseStepList> listAll(int startIndex, int pageSize);
	TestRunReportsDeviceCaseStepList getByTestRunReportsDeviceCaseStepListId(int testRunListId);
	List<TestRunReportsDeviceCaseStepList> list(int startIndex, int pageSize,Integer testRunListId,Integer testRunConfigurationChildId, Integer testCaseId);
	List<TestRunReportsDeviceCaseStepList> list(Integer testRunListId,Integer testRunConfigurationChildId, Integer testCaseId);
	int getTotalRecords();
	int getTotalRecordsFilteredTestRunReportsDeviceCaseStepList(Integer testRunListId,Integer testRunConfigurationChildId, Integer testCaseId);
	List<TestRunReportsDeviceCaseStepList> listForEvidenceGrid(Integer testRunListId,Integer testRunConfigurationChildId);
	int getTotalRecordsOfEvidence(Integer testRunNo, Integer testRunConfigurationChildId);
	//Added for iLCM TAF integration and Generating Excel reports  - Bugzilla Id 717
	List<TestRunReportsDeviceCaseStepList> getByJobId(int testRunJobId);
}
