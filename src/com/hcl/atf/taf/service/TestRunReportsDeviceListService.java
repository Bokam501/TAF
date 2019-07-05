package com.hcl.atf.taf.service;

import java.util.List;

import com.hcl.atf.taf.model.custom.TestRunReportsDeviceList;

public interface TestRunReportsDeviceListService  {	 
		
		List<TestRunReportsDeviceList> listAllTestRunReportsDeviceResult();
		List<TestRunReportsDeviceList> listTestRunReportsDeviceResult(int testRunNo);
		TestRunReportsDeviceList getByTestRunReportsDeviceListId(int testRunNo);
		List<TestRunReportsDeviceList> listAllTestRunReportsDeviceResult(int startIndex, int pageSize);
		List<TestRunReportsDeviceList> listAllTestRunReportsDeviceResult(int startIndex, int pageSize,Integer testRunNo,String productName,String productVersionName); 
		List<TestRunReportsDeviceList> listTestRunReportsDeviceResult(int testRunListId,int startIndex, int pageSize);
		List<TestRunReportsDeviceList> listAllTestRunReportsDeviceResult(Integer testRunNo,String productName,String productVersionName,String testRunTriggeredTime);
		List<TestRunReportsDeviceList> listAllTestRunReportsDeviceResult(Integer testRunNo,String productName,String productVersionName);		
		List<TestRunReportsDeviceList> listAllTestRunReportsDeviceResult(Integer testRunNo);
		List<TestRunReportsDeviceList> listAllTestRunReportsDeviceResult(Integer testRunNo, Integer testRunConfigurationChildId);
		int getTotalRecordsOfTestRunReportsDeviceListResult();
		int getTotalRecordsOfTestRunReportsDeviceListResult(Integer testRunNo,Integer testRunConfigurationChildId);
		int getTotalRecordsOfTestRunReportsDeviceListResult(Integer testRunNo,String productName,String productVersionName);
		int getTotalRecordsOfTestRunReportsDeviceListResult(Integer testRunNo,String productName,String productVersionName,String testRunTriggeredTime);
}
