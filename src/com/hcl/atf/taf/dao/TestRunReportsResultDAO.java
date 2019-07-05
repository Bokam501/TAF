package com.hcl.atf.taf.dao;

import java.util.List;

import com.hcl.atf.taf.model.custom.TestRunReportsList;

public interface TestRunReportsResultDAO  {	 

	List<TestRunReportsList> listAll();
	List<TestRunReportsList> list(int testRunListId);
	TestRunReportsList getByTestRunReportListId(int testExecutionResultId);
	List<TestRunReportsList> listAll(int startIndex, int pageSize);  
	List<TestRunReportsList> listAll(int startIndex, int pageSize,Integer testRunListId,String productName,String productVersionName);
	List<TestRunReportsList> list(int testRunNo,int startIndex, int pageSize);
	public List<TestRunReportsList> listFilteredTestRunReportsList(int testRunNo, String productName, String productVersionListName);
	int getTotalRecords();
	int getTotalRecordsFilteredtestRunReportsList(int testRunNo, String productName, String productVersionListName);
}
