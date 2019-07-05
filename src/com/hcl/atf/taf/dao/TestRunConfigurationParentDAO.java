package com.hcl.atf.taf.dao;

import java.util.List;

import com.hcl.atf.taf.model.TestCaseList;
import com.hcl.atf.taf.model.TestRunConfigurationParent;

public interface TestRunConfigurationParentDAO  {	 
	void add (TestRunConfigurationParent testRunConfigurationParent);
	void update (TestRunConfigurationParent testRunConfigurationParent);
	void delete (TestRunConfigurationParent testRunConfigurationParent);
	List<TestRunConfigurationParent> listAll();
	List<TestRunConfigurationParent> list(int userId);
	List<TestRunConfigurationParent> list(int userId,int productId);
	List<TestRunConfigurationParent> listAll(int startIndex, int pageSize);
	List<TestRunConfigurationParent> list(int userId,int startIndex, int pageSize);
	List<TestRunConfigurationParent> list(int userId,int productId,int startIndex, int pageSize);
	TestRunConfigurationParent getByTestRunConfigurationParentId(int testRunConfigurationParentId);
	int getTotalRecords();
	int getTotalRecords(int userId);
	int getTotalRecords(int userId,int productId);
	boolean getTestRunConfigurationParentByName(String testRunconfigurationName);
}
