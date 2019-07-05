package com.hcl.atf.taf.dao;

import java.util.List;

import com.hcl.atf.taf.model.TestRunList;

public interface TestRunListDAO  {	 
	void add (TestRunList testRunList);
	void update (TestRunList testRunList);
	void delete (TestRunList testRunList);
	List<TestRunList> listAll();
	List<TestRunList> list(int testRunConfigurationChildId);
	TestRunList getByTestRunListId(int testRunListId);
	List<TestRunList> listAll(int startIndex, int pageSize);  
	List<TestRunList> list(int testRunConfigurationChildId,int startIndex, int pageSize);
	int getTotalRecords();
	int getTotalRecords(int testRunConfigurationChildId);	
	List<TestRunList> listByHostId(int hostId, String testStatus);	
	void failTestRun(int hostId);
	void clearTestRuns();
	List<TestRunList> listExecutedJobList(int hours, int startIndex, int pageSize);
	List<TestRunList> listExecutingJobList();
	boolean hasTestRunCompleted(TestRunList testRunList);
	int getTotalTestRunListInLast24Hours(int hours);
	Integer getAverageTestRunExecutionTime(TestRunList testRunList);
}
