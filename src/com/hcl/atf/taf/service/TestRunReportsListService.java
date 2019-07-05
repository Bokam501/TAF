package com.hcl.atf.taf.service;

import java.util.List;

import com.hcl.atf.taf.model.custom.TestRunReportsList;

public interface TestRunReportsListService  {	 
	
		List<TestRunReportsList> listAllTestRunReportsResult();
		List<TestRunReportsList> listTestRunReportsResult(int testRunNo);
		TestRunReportsList getByTestRunReportsResultId(int testRunNo);
		List<TestRunReportsList> listAllTestRunReportsResult(int startIndex, int pageSize);
		List<TestRunReportsList> listAllTestRunReportsResult(int startIndex, int pageSize,Integer testRunNo,String productName,String productVersionName); 
		List<TestRunReportsList> listTestRunReportsResult(int testRunNo,int startIndex, int pageSize);
		int getTotalRecordsOfTestRunReportsResult();	
}
