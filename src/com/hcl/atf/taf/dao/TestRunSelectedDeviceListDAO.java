package com.hcl.atf.taf.dao;

import java.util.ArrayList;
import java.util.List;

import com.hcl.atf.taf.model.TestRunConfigurationChild;
import com.hcl.atf.taf.model.TestRunSelectedDeviceList;

public interface TestRunSelectedDeviceListDAO  {	 
	void add (TestRunSelectedDeviceList testRunSelectedDeviceList);	
	void delete (TestRunSelectedDeviceList testRunSelectedDeviceList);
	List<TestRunSelectedDeviceList> list(int testConfigurationChildId);	
	TestRunSelectedDeviceList getBySelectedDeviceListId(int selectedDeviceListId);
	int getTotalRecords(int testRunConfigurationChildId);
	ArrayList listDeviceList(int testRunConfigurationChildId);
	TestRunConfigurationChild listTestRunConfigurationChild(
			int testRunConfigurationChildId);
	
	
}
