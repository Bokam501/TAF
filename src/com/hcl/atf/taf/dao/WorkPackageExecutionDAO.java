package com.hcl.atf.taf.dao;

import java.util.Date;
import java.util.List;

import com.hcl.atf.taf.model.TestRunJob;
import com.hcl.atf.taf.model.TestcaseExecutionEvent;
import com.hcl.atf.taf.model.dto.WorkPackageTCEPStatusSummaryDTO;

public interface WorkPackageExecutionDAO {

	List<WorkPackageTCEPStatusSummaryDTO> listWorkPackageTCEPStatusSummaryForTester(Integer testerId);

	TestRunJob getTestRunJobById(Integer testRunJobId);
	String getTestRunJobResultStatus(int testRunJobId);
	String getWorkpackageResultStatus(int workPackageId);
	int getCompletedJobsCount(int workPackageId);
	Integer countAllWorkpackages(Date startDate, Date endDate);

	void addTestcaseExecutionEvent(TestcaseExecutionEvent testcaseExecutionEvent);
	TestcaseExecutionEvent getTestcaseExecutiontEvent(String eventName, String testcaseName, Integer jobId, Integer workPackageId);
	void deleteTestcaseExecutiontEvent(TestcaseExecutionEvent testcaseExecutionEvent);
	void deleteTestcaseExecutiontEvents(String eventName, Integer testcaseId, Integer jobId, Integer workPackageId, Integer expiryPolicy, Long expiryTime);
}
