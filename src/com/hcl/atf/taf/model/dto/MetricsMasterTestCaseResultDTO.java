package com.hcl.atf.taf.model.dto;

public class MetricsMasterTestCaseResultDTO {
	private Integer testCaseExecutionResultId;
	private String result;
	
	private Integer productId;	
	private String productName;	
	private Integer workPackageId;
	private String workPackageName;
	
	
	private Integer testCasePassedCount;
	private Integer testCaseFailedCount;
	private Integer testCaseBlockedCount;
	private Integer testCaseNorunCount;
	
	
	
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
	public Integer getTestCaseExecutionResultId() {
		return testCaseExecutionResultId;
	}
	public void setTestCaseExecutionResultId(Integer testCaseExecutionResultId) {
		this.testCaseExecutionResultId = testCaseExecutionResultId;
	}
	public String getResult() {
		return result;
	}
	public void setResult(String result) {
		this.result = result;
	}
	public Integer getTestCasePassedCount() {
		return testCasePassedCount;
	}
	public void setTestCasePassedCount(Integer testCasePassedCount) {
		this.testCasePassedCount = testCasePassedCount;
	}
	public Integer getTestCaseFailedCount() {
		return testCaseFailedCount;
	}
	public void setTestCaseFailedCount(Integer testCaseFailedCount) {
		this.testCaseFailedCount = testCaseFailedCount;
	}
	public Integer getTestCaseBlockedCount() {
		return testCaseBlockedCount;
	}
	public void setTestCaseBlockedCount(Integer testCaseBlockedCount) {
		this.testCaseBlockedCount = testCaseBlockedCount;
	}
	public Integer getTestCaseNorunCount() {
		return testCaseNorunCount;
	}
	public void setTestCaseNorunCount(Integer testCaseNorunCount) {
		this.testCaseNorunCount = testCaseNorunCount;
	}
}
