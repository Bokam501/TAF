package com.hcl.atf.taf.model.dto;

public class WorkPackageStatusSummaryDTO {
	
	private Integer workPackageStatusSummaryId;
	private Integer selectedEnvironmentsCount;
	private Integer selectedLocalesCount;
	private Integer selectedTestCasesCount;
	private Integer totalTestCaseForExecutionCount;
	private Integer completedTestCaseCount;
	private Integer notCompletedTestCaseCount;
	private Integer workPackageId;
	private String workPackageName;
	
	public Integer getWorkPackageStatusSummaryId() {
		return workPackageStatusSummaryId;
	}
	public void setWorkPackageStatusSummaryId(Integer workPackageStatusSummaryId) {
		this.workPackageStatusSummaryId = workPackageStatusSummaryId;
	}
	public Integer getSelectedEnvironmentsCount() {
		return selectedEnvironmentsCount;
	}
	public void setSelectedEnvironmentsCount(Integer selectedEnvironmentsCount) {
		this.selectedEnvironmentsCount = selectedEnvironmentsCount;
	}
	public Integer getSelectedLocalesCount() {
		return selectedLocalesCount;
	}
	public void setSelectedLocalesCount(Integer selectedLocalesCount) {
		this.selectedLocalesCount = selectedLocalesCount;
	}
	public Integer getSelectedTestCasesCount() {
		return selectedTestCasesCount;
	}
	public void setSelectedTestCasesCount(Integer selectedTestCasesCount) {
		this.selectedTestCasesCount = selectedTestCasesCount;
	}
	public Integer getCompletedTestCaseCount() {
		return completedTestCaseCount;
	}
	public void setCompletedTestCaseCount(Integer completedTestCaseCount) {
		this.completedTestCaseCount = completedTestCaseCount;
	}
	public Integer getNotCompletedTestCaseCount() {
		return notCompletedTestCaseCount;
	}
	public void setNotCompletedTestCaseCount(Integer notCompletedTestCaseCount) {
		this.notCompletedTestCaseCount = notCompletedTestCaseCount;
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
	public Integer getTotalTestCaseForExecutionCount() {
		return totalTestCaseForExecutionCount;
	}
	public void setTotalTestCaseForExecutionCount(
			Integer totalTestCaseForExecutionCount) {
		this.totalTestCaseForExecutionCount = totalTestCaseForExecutionCount;
	}
	

	
	
}
