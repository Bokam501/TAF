package com.hcl.atf.taf.model.dto;

public class WorkPackageTCEPStatusSummaryDTO {
	
	private Integer workPackageTCEPStatusSummaryId;
	private Integer workPackageId;
	private String workPackageName;
	private Integer testLeadId;
	private String testLeadName;
	private Integer testerId;
	private String testerName;
	private Integer totalTestCaseForExecutionCount;
	private Integer completedTestCaseCount;
	private Integer notCompletedTestCaseCount;
	
	public Integer getWorkPackageTCEPStatusSummaryId() {
		return workPackageTCEPStatusSummaryId;
	}
	public void setWorkPackageTCEPStatusSummaryId(
			Integer workPackageTCEPStatusSummaryId) {
		this.workPackageTCEPStatusSummaryId = workPackageTCEPStatusSummaryId;
	}
	public Integer getWorkPackageId() {
		return workPackageId;
	}
	public void setWorkPackageId(Integer workPackageId) {
		this.workPackageId = workPackageId;
	}
	public String getWorkPackageName() {
		return workPackageName;
	}
	public void setWorkPackageName(String workPackageName) {
		this.workPackageName = workPackageName;
	}
	public Integer getTestLeadId() {
		return testLeadId;
	}
	public void setTestLeadId(Integer testLeadId) {
		this.testLeadId = testLeadId;
	}
	public String getTestLeadName() {
		return testLeadName;
	}
	public void setTestLeadName(String testLeadName) {
		this.testLeadName = testLeadName;
	}
	public Integer getTesterId() {
		return testerId;
	}
	public void setTesterId(Integer testerId) {
		this.testerId = testerId;
	}
	public String getTesterName() {
		return testerName;
	}
	public void setTesterName(String testerName) {
		this.testerName = testerName;
	}
	public Integer getTotalTestCaseForExecutionCount() {
		return totalTestCaseForExecutionCount;
	}
	public void setTotalTestCaseForExecutionCount(Integer totalTestCaseForExecutionCount) {
		this.totalTestCaseForExecutionCount = totalTestCaseForExecutionCount;
	}
	public Integer getCompletedTestCaseCount() {
		return completedTestCaseCount;
	}
	public void setCompletedTestCaseCount(Integer completedTestCaseCount) {
		this.completedTestCaseCount = completedTestCaseCount;
		if (this.totalTestCaseForExecutionCount != null)
			this.notCompletedTestCaseCount = this.totalTestCaseForExecutionCount - completedTestCaseCount;
	}
	public Integer getNotCompletedTestCaseCount() {
		return notCompletedTestCaseCount;
	}
	public void setNotCompletedTestCaseCount(Integer notCompletedTestCaseCount) {
		this.notCompletedTestCaseCount = notCompletedTestCaseCount;
		if (this.totalTestCaseForExecutionCount != null)
			this.completedTestCaseCount = this.totalTestCaseForExecutionCount - notCompletedTestCaseCount;
	}
	
}
