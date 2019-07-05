package com.hcl.atf.taf.model.dto;


public class ResourceExperienceSummaryDTO {
	private Integer resourceExperienceSummaryId;
	private Integer executedTestCaseCount;
	private Integer reportedDefectsCount;
	private Integer approvedDefectsCount;
	private Integer wpCount;
	private Integer userId;
	private String userLoginId;
	private Integer productId;
	private String productName;
	private Integer productVersionId;
	private String productVersionName;
	private Integer workPackageId;
	private String workPackageName;
	private Integer userAveragePerformanceRating;
	
	
	public Integer getResourceExperienceSummaryId() {
		return resourceExperienceSummaryId;
	}
	public void setResourceExperienceSummaryId(Integer resourceExperienceSummaryId) {
		this.resourceExperienceSummaryId = resourceExperienceSummaryId;
	}
	public Integer getExecutedTestCaseCount() {
		return executedTestCaseCount;
	}
	public void setExecutedTestCaseCount(Integer executedTestCaseCount) {
		this.executedTestCaseCount = executedTestCaseCount;
	}
	public Integer getReportedDefectsCount() {
		return reportedDefectsCount;
	}
	public void setReportedDefectsCount(Integer reportedDefectsCount) {
		this.reportedDefectsCount = reportedDefectsCount;
	}
	public Integer getApprovedDefectsCount() {
		return approvedDefectsCount;
	}
	public void setApprovedDefectsCount(Integer approvedDefectsCount) {
		this.approvedDefectsCount = approvedDefectsCount;
	}
	public Integer getWpCount() {
		return wpCount;
	}
	public void setWpCount(Integer wpCount) {
		this.wpCount = wpCount;
	}
	public Integer getUserId() {
		return userId;
	}
	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public String getUserLoginId() {
		return userLoginId;
	}
	public void setUserLoginId(String userLoginId) {
		this.userLoginId = userLoginId;
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
	public Integer getProductVersionId() {
		return productVersionId;
	}
	public void setProductVersionId(Integer productVersionId) {
		this.productVersionId = productVersionId;
	}
	public String getProductVersionName() {
		return productVersionName;
	}
	public void setProductVersionName(String productVersionName) {
		this.productVersionName = productVersionName;
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
	public Integer getUserAveragePerformanceRating() {
		return userAveragePerformanceRating;
	}
	public void setUserAveragePerformanceRating(Integer userAveragePerformanceRating) {
		this.userAveragePerformanceRating = userAveragePerformanceRating;
	}
}
