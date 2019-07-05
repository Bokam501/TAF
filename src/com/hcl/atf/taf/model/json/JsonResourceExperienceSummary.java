package com.hcl.atf.taf.model.json;

import com.fasterxml.jackson.annotation.JsonProperty;

public class JsonResourceExperienceSummary implements java.io.Serializable {
	
	@JsonProperty	
	private Integer experienceSummaryId;
	@JsonProperty
	private Integer wpCount;
	@JsonProperty
	private Integer executedTestCaseCount;
	@JsonProperty
	private Integer reportedDefectsCount;
	@JsonProperty
	private Integer approvedDefectsCount;
	@JsonProperty
	private Integer userId;
	@JsonProperty
	private String userLoginId;
	@JsonProperty
	private String userRoleName;
	@JsonProperty
	private String userSkillName;
	@JsonProperty
	private Integer productId;
	@JsonProperty
	private String productName;
	@JsonProperty
	private Integer productVersionId;
	@JsonProperty
	private String productVersionName;
	@JsonProperty
	private Integer userAveragePerformanceRating;
	
	public Integer getExperienceSummaryId() {
		return experienceSummaryId;
	}
	public void setExperienceSummaryId(Integer experienceSummaryId) {
		this.experienceSummaryId = experienceSummaryId;
	}
	public Integer getWpCount() {
		return wpCount;
	}
	public void setWpCount(Integer wpCount) {
		this.wpCount = wpCount;
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
	public String getUserRoleName() {
		return userRoleName;
	}
	public void setUserRoleName(String userRoleName) {
		this.userRoleName = userRoleName;
	}
	public String getUserSkillName() {
		return userSkillName;
	}
	public void setUserSkillName(String userSkillName) {
		this.userSkillName = userSkillName;
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
	public Integer getUserAveragePerformanceRating() {
		return userAveragePerformanceRating;
	}
	public void setUserAveragePerformanceRating(Integer userAveragePerformanceRating) {
		this.userAveragePerformanceRating = userAveragePerformanceRating;
	}
	
}
