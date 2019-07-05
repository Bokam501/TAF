package com.hcl.atf.taf.dao;

import java.util.List;
import java.util.Set;

import com.hcl.atf.taf.model.TestCaseList;
import com.hcl.atf.taf.model.TestEnvironmentDevices;
import com.hcl.atf.taf.model.TestRunConfigurationChild;
import com.hcl.atf.taf.model.TestRunList;
import com.hcl.atf.taf.model.TrccExecutionPlan;

public interface TestRunConfigurationChildDAO  {	 
	void add (TestRunConfigurationChild testRunConfigurationChild);
	void update (TestRunConfigurationChild testRunConfigurationChild);
	void delete (TestRunConfigurationChild testRunConfigurationChild);
	List<TestRunConfigurationChild> listAll();
	List<TestRunConfigurationChild> list(int testRunConfigurationParentId);	
	List<TestRunConfigurationChild> listAll(int startIndex, int pageSize);
	List<TestRunConfigurationChild> list(int testRunConfigurationParentId,int startIndex, int pageSize);	
	TestRunConfigurationChild getByTestRunConfigurationChildId(int testRunConfigurationChildId);
	TestRunConfigurationChild getByTestRunConfigurationChildName(String testRunConfigurationChildName);
	int getTotalRecords();	
	int getTotalRecords(int testRunConfigurationParentId);
	int getMaxRunNo();	
	List<TestRunList> getRunNo(int productId, String pltfm_id);	
	boolean isTestConfigurationChildExistingByName(String testRunConfigurationName);
	List<TestRunConfigurationChild> list(int testRunConfigurationParentId,int status);
	void delete(Set<TestRunConfigurationChild> testRunConfigurationChilds);
	void reactivate(TestRunConfigurationChild testRunConfigurationChild);
	void reactivate(Set<TestRunConfigurationChild> testRunConfigurationChilds);
	boolean addTestEnvironmentToTestRunConfigurationChild(int testRunConfigurationChildId, int testEnvironmentId);
	boolean removeTestEnvironmentFromTestRunConfigurationChild(int testRunConfigurationChildId, int testEnvironmentId);
	List<TestEnvironmentDevices> listTestEnvironmentsOfTestRunConfigurationChild(
			int testRunConfigurationChildId);
}
