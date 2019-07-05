package com.hcl.atf.taf.model.dto;

import com.google.gdata.data.dublincore.Date;

public class MetricsMasterDTO {
	private Integer workPackageId;
	private String workPackageName;
	private Integer productId;	
	private String productName;	
	private Integer testCaseAllocatedCount;
	private Integer testCaseCompletedCount;
	private Integer testCaseNotStartedCount;
	private Integer completedTestCasePercentage;
	private Date plannedExecutionDate;
	private Integer executionStatus;
	private Integer userId;
	private String loginId;
	private Integer totalTCAllocatedCount;
	
	
	
	
	
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

	public Integer getTestCaseAllocatedCount() {
		return testCaseAllocatedCount;
	}
	public void setTestCaseAllocatedCount(Integer testCaseAllocatedCount) {
		this.testCaseAllocatedCount = testCaseAllocatedCount;
	}
	public Integer getTestCaseCompletedCount() {
		return testCaseCompletedCount;
	}
	public void setTestCaseCompletedCount(Integer testCaseCompletedCount) {
		this.testCaseCompletedCount = testCaseCompletedCount;
	}
	public Integer getTestCaseNotStartedCount() {
		return testCaseNotStartedCount;
	}
	public void setTestCaseNotStartedCount(Integer testCaseNotStartedCount) {
		this.testCaseNotStartedCount = testCaseNotStartedCount;
	}
	public Integer getCompletedTestCasePercentage() {
		return completedTestCasePercentage;
	}
	public void setCompletedTestCasePercentage(Integer completedTestCasePercentage) {
		this.completedTestCasePercentage = completedTestCasePercentage;
	}
	public Date getPlannedExecutionDate() {
		return plannedExecutionDate;
	}
	public void setPlannedExecutionDate(Date plannedExecutionDate) {
		this.plannedExecutionDate = plannedExecutionDate;
	}
	public Integer getExecutionStatus() {
		return executionStatus;
	}
	public void setExecutionStatus(Integer executionStatus) {
		this.executionStatus = executionStatus;
	}
	public Integer getUserId() {
		return userId;
	}
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	public String getLoginId() {
		return loginId;
	}
	public void setLoginId(String loginId) {
		this.loginId = loginId;
	}
	public Integer getTotalTCAllocatedCount() {
		return totalTCAllocatedCount;
	}
	public void setTotalTCAllocatedCount(Integer totalTCAllocatedCount) {
		this.totalTCAllocatedCount = totalTCAllocatedCount;
	}
	
	
	
}
