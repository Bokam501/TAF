package com.hcl.atf.taf.model.dto;



import java.util.Date;

import com.hcl.atf.taf.model.TestCaseList;


public class TestStepExecutionResultDTO implements java.io.Serializable {

	private Integer teststepexecutionresultId;
	private String result;
	private String comments;
	private Integer isApproved;
	private Integer isReviewed;
	private String observedOutput;	
	private Integer testcaseId;	
	private Date testStepStarttime; 
	private Date testStepEndtime; 
	private String failureReason;	
	private Integer testStepId;
	private TestCaseList testCaseList;
	private String testStepName;
	private String testStepDescription;
	private String testStepInput;
	private String testStepExpectedOutput;
	private String testStepCode;
	private String testStepSource;
	private Date testStepCreatedDate;
	private Date testStepLastUpdatedDate;
	public Integer getTeststepexecutionresultId() {
		return teststepexecutionresultId;
	}
	public void setTeststepexecutionresultId(Integer teststepexecutionresultId) {
		this.teststepexecutionresultId = teststepexecutionresultId;
	}
	public String getResult() {
		return result;
	}
	public void setResult(String result) {
		this.result = result;
	}
	public String getComments() {
		return comments;
	}
	public void setComments(String comments) {
		this.comments = comments;
	}
	public Integer getIsApproved() {
		return isApproved;
	}
	public void setIsApproved(Integer isApproved) {
		this.isApproved = isApproved;
	}
	public Integer getIsReviewed() {
		return isReviewed;
	}
	public void setIsReviewed(Integer isReviewed) {
		this.isReviewed = isReviewed;
	}
	public String getObservedOutput() {
		return observedOutput;
	}
	public void setObservedOutput(String observedOutput) {
		this.observedOutput = observedOutput;
	}
	public Integer getTestcaseId() {
		return testcaseId;
	}
	public void setTestcaseId(Integer testcaseId) {
		this.testcaseId = testcaseId;
	}
	public Date getTestStepStarttime() {
		return testStepStarttime;
	}
	public void setTestStepStarttime(Date testStepStarttime) {
		this.testStepStarttime = testStepStarttime;
	}
	public Date getTestStepEndtime() {
		return testStepEndtime;
	}
	public void setTestStepEndtime(Date testStepEndtime) {
		this.testStepEndtime = testStepEndtime;
	}
	public String getFailureReason() {
		return failureReason;
	}
	public void setFailureReason(String failureReason) {
		this.failureReason = failureReason;
	}
	public Integer getTestStepId() {
		return testStepId;
	}
	public void setTestStepId(Integer testStepId) {
		this.testStepId = testStepId;
	}
	public TestCaseList getTestCaseList() {
		return testCaseList;
	}
	public void setTestCaseList(TestCaseList testCaseList) {
		this.testCaseList = testCaseList;
	}
	public String getTestStepName() {
		return testStepName;
	}
	public void setTestStepName(String testStepName) {
		this.testStepName = testStepName;
	}
	public String getTestStepDescription() {
		return testStepDescription;
	}
	public void setTestStepDescription(String testStepDescription) {
		this.testStepDescription = testStepDescription;
	}
	public String getTestStepInput() {
		return testStepInput;
	}
	public void setTestStepInput(String testStepInput) {
		this.testStepInput = testStepInput;
	}
	public String getTestStepExpectedOutput() {
		return testStepExpectedOutput;
	}
	public void setTestStepExpectedOutput(String testStepExpectedOutput) {
		this.testStepExpectedOutput = testStepExpectedOutput;
	}
	public String getTestStepCode() {
		return testStepCode;
	}
	public void setTestStepCode(String testStepCode) {
		this.testStepCode = testStepCode;
	}
	public String getTestStepSource() {
		return testStepSource;
	}
	public void setTestStepSource(String testStepSource) {
		this.testStepSource = testStepSource;
	}
	public Date getTestStepCreatedDate() {
		return testStepCreatedDate;
	}
	public void setTestStepCreatedDate(Date testStepCreatedDate) {
		this.testStepCreatedDate = testStepCreatedDate;
	}
	public Date getTestStepLastUpdatedDate() {
		return testStepLastUpdatedDate;
	}
	public void setTestStepLastUpdatedDate(Date testStepLastUpdatedDate) {
		this.testStepLastUpdatedDate = testStepLastUpdatedDate;
	}
	
}
