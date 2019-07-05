package com.hcl.atf.taf.model.dto;

public class WorkPackageBuildTestCaseSummaryDTO {
	
	private Integer workPackageId;
	private String workPackageName;
	private Integer testRunJobId;
	private Integer testRunStatus;
	private String testRunFailureMessage;
	private String testRunEvidenceStatus;
	private Integer passedCount;
	private Integer failedCount;
	private Integer blockedCount;
	private Integer norunCount;
	private Integer notexecutedCount;
	private Integer teststepcount;
	private Integer defectsCount;
	
	public WorkPackageBuildTestCaseSummaryDTO(){
		this.workPackageId = 0;
		this.workPackageName = "";
		this.testRunJobId = 0;
		this.testRunStatus = 0;
		this.testRunFailureMessage = "";
		this.testRunEvidenceStatus = "";
		this.passedCount = 0;
		this.failedCount = 0;
		this.blockedCount = 0;
		this.norunCount = 0;
		this.notexecutedCount = 0;
		this.teststepcount = 0;
		this.defectsCount = 0;
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
	public Integer getTestRunJobId() {
		return testRunJobId;
	}
	public void setTestRunJobId(Integer testRunJobId) {
		this.testRunJobId = testRunJobId;
	}
	public Integer getTestRunStatus() {
		return testRunStatus;
	}
	public void setTestRunStatus(Integer testRunStatus) {
		this.testRunStatus = testRunStatus;
	}
	public String getTestRunFailureMessage() {
		return testRunFailureMessage;
	}
	public void setTestRunFailureMessage(String testRunFailureMessage) {
		this.testRunFailureMessage = testRunFailureMessage;
	}
	public String getTestRunEvidenceStatus() {
		return testRunEvidenceStatus;
	}
	public void setTestRunEvidenceStatus(String testRunEvidenceStatus) {
		this.testRunEvidenceStatus = testRunEvidenceStatus;
	}
	public Integer getPassedCount() {
		return passedCount;
	}
	public void setPassedCount(Integer passedCount) {
		this.passedCount = passedCount;
	}
	public Integer getFailedCount() {
		return failedCount;
	}
	public void setFailedCount(Integer failedCount) {
		this.failedCount = failedCount;
	}
	public Integer getBlockedCount() {
		return blockedCount;
	}
	public void setBlockedCount(Integer blockedCount) {
		this.blockedCount = blockedCount;
	}
	public Integer getNorunCount() {
		return norunCount;
	}
	public void setNorunCount(Integer norunCount) {
		this.norunCount = norunCount;
	}
	public Integer getNotexecutedCount() {
		return notexecutedCount;
	}
	public void setNotexecutedCount(Integer notexecutedCount) {
		this.notexecutedCount = notexecutedCount;
	}
	public Integer getTeststepcount() {
		return teststepcount;
	}
	public void setTeststepcount(Integer teststepcount) {
		this.teststepcount = teststepcount;
	}
	public Integer getDefectsCount() {
		return defectsCount;
	}
	public void setDefectsCount(Integer defectsCount) {
		this.defectsCount = defectsCount;
	}
}
