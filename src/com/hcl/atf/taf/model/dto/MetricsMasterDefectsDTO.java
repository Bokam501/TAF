package com.hcl.atf.taf.model.dto;


public class MetricsMasterDefectsDTO {
	private Integer testExecutionResultBugId;
	private Integer bugFilingStatus;
	private Integer productId;	
	private String productName;	
	private Integer severityId;
	private Integer workPackageId;
	private String workPackageName;
	private Integer blockerDefects;
	private Integer normalDefects;
	
	
	private Integer testCaseDefectsPassedCount;
	private Integer totalTestCaseDefectsCount;
	private Integer totalTestCaseInvalidDefectsCount;
	private Integer completedTestCaseDefectsPercentage;
	
	
	private Integer defectsBugfilingStatusNewCount;
	private Integer defectsBugfilingStatusreferbackCount;
	private Integer defectsBugfilingStatusreviewedCount;
	private Integer defectsBugfilingStatusapprovedCount;
	
	private Integer defectsBugfilingStatusClosedCount;
	private Integer defectsBugfilingStatusDuplicateCount;
	private Integer defectsBugfilingStatusFixedCount;
	private Integer defectsBugfilingStatusVerifiedCount;
	private Integer defectsBugfilingStatusIntendedBehaviourCount;
	private Integer defectsBugfilingStatusNotReproducibleCount;

	
	
	
	
	
	public Integer getTestExecutionResultBugId() {
		return testExecutionResultBugId;
	}
	public void setTestExecutionResultBugId(Integer testExecutionResultBugId) {
		this.testExecutionResultBugId = testExecutionResultBugId;
	}
	public Integer getBugFilingStatus() {
		return bugFilingStatus;
	}
	public void setBugFilingStatus(Integer bugFilingStatus) {
		this.bugFilingStatus = bugFilingStatus;
	}
	public Integer getProductId() {
		return productId;
	}
	public void setProductId(Integer productId) {
		this.productId = productId;
	}
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public Integer getSeverityId() {
		return severityId;
	}
	public void setSeverityId(Integer severityId) {
		this.severityId = severityId;
	}
	public Integer getTestCaseDefectsPassedCount() {
		return testCaseDefectsPassedCount;
	}
	public void setTestCaseDefectsPassedCount(Integer testCaseDefectsPassedCount) {
		this.testCaseDefectsPassedCount = testCaseDefectsPassedCount;
	}
	public Integer getTotalTestCaseDefectsCount() {
		return totalTestCaseDefectsCount;
	}
	public void setTotalTestCaseDefectsCount(Integer totalTestCaseDefectsCount) {
		this.totalTestCaseDefectsCount = totalTestCaseDefectsCount;
	}
	public Integer getCompletedTestCaseDefectsPercentage() {
		return completedTestCaseDefectsPercentage;
	}
	public void setCompletedTestCaseDefectsPercentage(
			Integer completedTestCaseDefectsPercentage) {
		this.completedTestCaseDefectsPercentage = completedTestCaseDefectsPercentage;
	}
	public Integer getTotalTestCaseInvalidDefectsCount() {
		return totalTestCaseInvalidDefectsCount;
	}
	public void setTotalTestCaseInvalidDefectsCount(
			Integer totalTestCaseInvalidDefectsCount) {
		this.totalTestCaseInvalidDefectsCount = totalTestCaseInvalidDefectsCount;
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
	public Integer getDefectsBugfilingStatusNewCount() {
		return defectsBugfilingStatusNewCount;
	}
	public void setDefectsBugfilingStatusNewCount(
			Integer defectsBugfilingStatusNewCount) {
		this.defectsBugfilingStatusNewCount = defectsBugfilingStatusNewCount;
	}
	public Integer getDefectsBugfilingStatusreferbackCount() {
		return defectsBugfilingStatusreferbackCount;
	}
	public void setDefectsBugfilingStatusreferbackCount(
			Integer defectsBugfilingStatusreferbackCount) {
		this.defectsBugfilingStatusreferbackCount = defectsBugfilingStatusreferbackCount;
	}
	public Integer getDefectsBugfilingStatusreviewedCount() {
		return defectsBugfilingStatusreviewedCount;
	}
	public void setDefectsBugfilingStatusreviewedCount(
			Integer defectsBugfilingStatusreviewedCount) {
		this.defectsBugfilingStatusreviewedCount = defectsBugfilingStatusreviewedCount;
	}
	public Integer getDefectsBugfilingStatusapprovedCount() {
		return defectsBugfilingStatusapprovedCount;
	}
	public void setDefectsBugfilingStatusapprovedCount(
			Integer defectsBugfilingStatusapprovedCount) {
		this.defectsBugfilingStatusapprovedCount = defectsBugfilingStatusapprovedCount;
	}
	public Integer getDefectsBugfilingStatusClosedCount() {
		return defectsBugfilingStatusClosedCount;
	}
	public void setDefectsBugfilingStatusClosedCount(
			Integer defectsBugfilingStatusClosedCount) {
		this.defectsBugfilingStatusClosedCount = defectsBugfilingStatusClosedCount;
	}
	public Integer getDefectsBugfilingStatusDuplicateCount() {
		return defectsBugfilingStatusDuplicateCount;
	}
	public void setDefectsBugfilingStatusDuplicateCount(
			Integer defectsBugfilingStatusDuplicateCount) {
		this.defectsBugfilingStatusDuplicateCount = defectsBugfilingStatusDuplicateCount;
	}
	public Integer getDefectsBugfilingStatusFixedCount() {
		return defectsBugfilingStatusFixedCount;
	}
	public void setDefectsBugfilingStatusFixedCount(
			Integer defectsBugfilingStatusFixedCount) {
		this.defectsBugfilingStatusFixedCount = defectsBugfilingStatusFixedCount;
	}
	public Integer getDefectsBugfilingStatusVerifiedCount() {
		return defectsBugfilingStatusVerifiedCount;
	}
	public void setDefectsBugfilingStatusVerifiedCount(
			Integer defectsBugfilingStatusVerifiedCount) {
		this.defectsBugfilingStatusVerifiedCount = defectsBugfilingStatusVerifiedCount;
	}
	public Integer getDefectsBugfilingStatusIntendedBehaviourCount() {
		return defectsBugfilingStatusIntendedBehaviourCount;
	}
	public void setDefectsBugfilingStatusIntendedBehaviourCount(
			Integer defectsBugfilingStatusIntendedBehaviourCount) {
		this.defectsBugfilingStatusIntendedBehaviourCount = defectsBugfilingStatusIntendedBehaviourCount;
	}
	public Integer getDefectsBugfilingStatusNotReproducibleCount() {
		return defectsBugfilingStatusNotReproducibleCount;
	}
	public void setDefectsBugfilingStatusNotReproducibleCount(
			Integer defectsBugfilingStatusNotReproducibleCount) {
		this.defectsBugfilingStatusNotReproducibleCount = defectsBugfilingStatusNotReproducibleCount;
	}
	public Integer getBlockerDefects() {
		return blockerDefects;
	}
	public void setBlockerDefects(Integer blockerDefects) {
		this.blockerDefects = blockerDefects;
	}
	public Integer getNormalDefects() {
		return normalDefects;
	}
	public void setNormalDefects(Integer normalDefects) {
		this.normalDefects = normalDefects;
	}
	
}
