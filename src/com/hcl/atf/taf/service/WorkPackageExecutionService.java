package com.hcl.atf.taf.service;

import java.util.Date;
import java.util.List;

import com.hcl.atf.taf.model.TestRunJob;
import com.hcl.atf.taf.model.TestcaseExecutionEvent;
import com.hcl.atf.taf.model.UserList;
import com.hcl.atf.taf.model.WorkPackage;
import com.hcl.atf.taf.model.json.JsonLogFileContent;

public interface WorkPackageExecutionService {

	boolean abortAutomatedTestRunJob(Integer testRunJobId);
	TestRunJob getTestRunJobById(int testRunJobId);
	String getTestRunJobResultStatus(int testRunJobId);
	String getWorkpackageResultStatus(int workPackageId);
	int getCompletedJobsCount(int workPackageId);
	Integer countAllWorkpackages(Date startDate, Date endDate);
	boolean executeSingleAutomatedTestCase(Integer testCaseId, Integer testRunPlanId, UserList user , String runConfigStrFromUI);
	WorkPackage createNewWorkpackageForAutomatedExecution(Integer testCaseId, Integer testRunPlanId, UserList loginuser);
	void postEvent(TestcaseExecutionEvent testcaseExecutionEvent);
	TestcaseExecutionEvent getTestcaseExecutiontEvent(String eventName, String testcaseName, Integer jobId, Integer workPackageId);
	void killTestcaseExecutiontEvent(TestcaseExecutionEvent testcaseExecutionEvent);
	void clearJobTestcaseExecutiontEvents(Integer jobId);
	void clearImmortalTestcaseExecutiontEvents();
	void clearTimeBasedTestcaseExecutiontEvents(Date expiryTime);
	void clearNamedTestcaseExecutiontEvents(String eventName);
	void clearWorkpackageTestcaseExecutiontEvents(Integer workpackageId);
	JsonLogFileContent getJobLogUpdatedContent(Integer testJobId, Integer lastLine);
	boolean abortAutomatedWorkPackage(Integer workPackageId);
}
