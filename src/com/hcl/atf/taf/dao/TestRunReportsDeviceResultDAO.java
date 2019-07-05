package com.hcl.atf.taf.dao;

import java.util.List;

import com.hcl.atf.taf.model.custom.TestRunReportsDeviceList;
import com.hcl.atf.taf.model.custom.TestRunReportsList;

public interface TestRunReportsDeviceResultDAO  {
	List<TestRunReportsDeviceList> listAll();
	List<TestRunReportsDeviceList> list(int testRunListId);
	TestRunReportsDeviceList getByTestRunReportsDeviceListId(int testRunNo);
	List<TestRunReportsDeviceList> listAll(int startIndex, int pageSize);  
	List<TestRunReportsDeviceList> listAll(int startIndex, int pageSize,Integer testRunNo,String productName,String productVersionName);
	List<TestRunReportsDeviceList> list(int testRunListId,int startIndex, int pageSize);
	List<TestRunReportsDeviceList> list(Integer testRunNo,String productName,String productVersionName,String testRunTriggeredTime);
	List<TestRunReportsDeviceList> list(Integer testRunNo,String productName,String productVersionName);
	List<TestRunReportsDeviceList> list(Integer testRunNo, String deviceId);
	List<TestRunReportsDeviceList> list(Integer testRunNo);
	List<TestRunReportsDeviceList> list(int testRunListId,Integer testRunConfigurationChildId);
	public List<TestRunReportsDeviceList> listFilteredTestRunReportsDeviceList(int testRunNo, String productName, String productVersionName,String deviceId);
	int getTotalRecords();
	int getTotalRecordsFilteredTestRunReportsDeviceList(int testRunNo,Integer testRunConfigurationChildId);
	int getTotalRecordsFilteredTestRunReportsDeviceList(int testRunNo, String productName, String productVersionName);
	int getTotalRecordsFilteredTestRunReportsDeviceList(int testRunNo, String productName, String productVersionName,String testRunTriggeredTime);
	
}
