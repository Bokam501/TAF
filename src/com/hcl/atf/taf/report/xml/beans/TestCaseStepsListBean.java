package com.hcl.atf.taf.report.xml.beans;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;

import static javax.persistence.GenerationType.IDENTITY;

import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = {"testStepId","testStepCode", "testStepName","testStepDescription","environment","testStepInput","testStepExpectedOutput","observedOutput","startTime","endTime","testStepStatus","failureReason","screenshot","duration"})
public class TestCaseStepsListBean implements java.io.Serializable {

	private Integer testStepId;
	@XmlTransient
	private TestCaseListBean testCaseList;
	private String testStepName;
	private String testStepDescription;
	private String environment;
	private String testStepInput;
	private String testStepExpectedOutput;
	@XmlTransient
	private Set<TestExecutionResultBean> testExecutionResults = new HashSet<TestExecutionResultBean>(
			0);
	//Changes for TestManagement tools 
	private String testStepCode;
	@XmlTransient
	private String testStepSource;
	@XmlTransient
	private Date testStepCreatedDate;
	@XmlTransient
	private Date testStepLastUpdatedDate;
	private String observedOutput;
    private String testStepStatus;
    private String failureReason;
    private String screenshot;
    private String duration;
	private Date startTime;
	private Date endTime;
	public TestCaseStepsListBean() {
	}

	public TestCaseStepsListBean(String testStepName) {
		this.testStepName = testStepName;
	}

	public Integer getTestStepId() {
		return testStepId;
	}

	public void setTestStepId(Integer testStepId) {
		this.testStepId = testStepId;
	}

	public TestCaseListBean getTestCaseList() {
		return testCaseList;
	}

	public void setTestCaseList(TestCaseListBean testCaseList) {
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

	public Set<TestExecutionResultBean> getTestExecutionResults() {
		return testExecutionResults;
	}

	public void setTestExecutionResults(
			Set<TestExecutionResultBean> testExecutionResults) {
		this.testExecutionResults = testExecutionResults;
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

	public String getObservedOutput() {
		return observedOutput;
	}

	public void setObservedOutput(String observedOutput) {
		this.observedOutput = observedOutput;
	}

	public String getTestStepStatus() {
		return testStepStatus;
	}

	public void setTestStepStatus(String testStepStatus) {
		this.testStepStatus = testStepStatus;
	}

	public String getFailureReason() {
		return failureReason;
	}

	public void setFailureReason(String failureReason) {
		this.failureReason = failureReason;
	}

	public String getScreenshot() {
		return screenshot;
	}

	public void setScreenshot(String screenshot) {
		this.screenshot = screenshot;
	}

	public String getDuration() {
		return duration;
	}

	public void setDuration(String duration) {
		this.duration = duration;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public String getEnvironment() {
		return environment;
	}

	public void setEnvironment(String environment) {
		this.environment = environment;
	}
	

	/*public TestCaseStepsList(TestCaseList testCaseList, String testStepName,
			String testStepDescription, String testStepInput,
			String testStepExpectedOutput,
			Set<TestExecutionResult> testExecutionResults) {
		this.testCaseList = testCaseList;
		this.testStepName = testStepName;
		this.testStepDescription = testStepDescription;
		this.testStepInput = testStepInput;
		this.testStepExpectedOutput = testStepExpectedOutput;
		this.testExecutionResults = testExecutionResults;
	}*/


}
