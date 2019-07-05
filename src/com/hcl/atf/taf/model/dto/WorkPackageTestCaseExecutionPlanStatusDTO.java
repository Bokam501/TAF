package com.hcl.atf.taf.model.dto;


public class WorkPackageTestCaseExecutionPlanStatusDTO {
	
	private Integer workPackageTestCaseExecutionPlanStatusId;
	
	private Integer totalTestCaseCount;
	private Integer assignedTestLeadCount;
	private Integer assignedTesterCount;
	private Integer plannedExecutionDateCount;
	private Integer workPackageId;
	private String workPackageName;
	private Integer workPackageStatusSummaryId;
	private Integer totalTestSuiteCount;
	private Integer totalFeatureCount;
	private Integer activeTestCaseCount;
	private Integer inActiveTestCaseCount;
	
	
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
	public Integer getWorkPackageTestCaseExecutionPlanStatusId() {
		return workPackageTestCaseExecutionPlanStatusId;
	}
	public void setWorkPackageTestCaseExecutionPlanStatusId(
			Integer workPackageTestCaseExecutionPlanStatusId) {
		this.workPackageTestCaseExecutionPlanStatusId = workPackageTestCaseExecutionPlanStatusId;
	}
	public Integer getTotalTestCaseCount() {
		return totalTestCaseCount;
	}
	public void setTotalTestCaseCount(Integer totalTestCaseCount) {
		this.totalTestCaseCount = totalTestCaseCount;
	}
	public Integer getAssignedTestLeadCount() {
		return assignedTestLeadCount;
	}
	public void setAssignedTestLeadCount(Integer assignedTestLeadCount) {
		this.assignedTestLeadCount = assignedTestLeadCount;
	}
	public Integer getAssignedTesterCount() {
		return assignedTesterCount;
	}
	public void setAssignedTesterCount(Integer assignedTesterCount) {
		this.assignedTesterCount = assignedTesterCount;
	}
	public Integer getPlannedExecutionDateCount() {
		return plannedExecutionDateCount;
	}
	public void setPlannedExecutionDateCount(Integer plannedExecutionDateCount) {
		this.plannedExecutionDateCount = plannedExecutionDateCount;
	}
	public Integer getWorkPackageStatusSummaryId() {
		return workPackageStatusSummaryId;
	}
	public void setWorkPackageStatusSummaryId(Integer workPackageStatusSummaryId) {
		this.workPackageStatusSummaryId = workPackageStatusSummaryId;
	}
	public Integer getTotalTestSuiteCount() {
		return totalTestSuiteCount;
	}
	public void setTotalTestSuiteCount(Integer totalTestSuiteCount) {
		this.totalTestSuiteCount = totalTestSuiteCount;
	}
	public Integer getTotalFeatureCount() {
		return totalFeatureCount;
	}
	public void setTotalFeatureCount(Integer totalFeatureCount) {
		this.totalFeatureCount = totalFeatureCount;
	}
	public Integer getActiveTestCaseCount() {
		return activeTestCaseCount;
	}
	public void setActiveTestCaseCount(Integer activeTestCaseCount) {
		this.activeTestCaseCount = activeTestCaseCount;
	}
	public Integer getInActiveTestCaseCount() {
		return inActiveTestCaseCount;
	}
	public void setInActiveTestCaseCount(Integer inActiveTestCaseCount) {
		this.inActiveTestCaseCount = inActiveTestCaseCount;
	}
	
	
}
