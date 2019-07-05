package com.hcl.atf.taf.report.xml.beans;

// Generated Feb 4, 2014 4:30:16 PM by Hibernate Tools 3.4.0.CR1

import java.sql.Time;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;

import static javax.persistence.GenerationType.IDENTITY;

import javax.persistence.CascadeType;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;


public class TestCaseExecutionResultBean implements java.io.Serializable {

	private Integer testCaseExecutionResultId;
	private String result;
	//private TestResultStatusMaster testResultStatusMaster;
	//private WorkPackageTestCaseExecutionPlanBean workPackageTestCaseExecutionPlan;
	private Integer defectsCount;
	private String defectIds;
	private String comments;
	private Integer isApproved;
	private Integer isReviewed;
	private String observedOutput;
	private String failureReason;
	private Long executionTime;

	private Date startTime; 
	private Date endTime; 
	private TestSuiteListBean testSuiteList;
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
	/*public WorkPackageTestCaseExecutionPlanBean getWorkPackageTestCaseExecutionPlan() {
		return workPackageTestCaseExecutionPlan;
	}
	public void setWorkPackageTestCaseExecutionPlan(
			WorkPackageTestCaseExecutionPlanBean workPackageTestCaseExecutionPlan) {
		this.workPackageTestCaseExecutionPlan = workPackageTestCaseExecutionPlan;
	}*/
	public Integer getDefectsCount() {
		return defectsCount;
	}
	public void setDefectsCount(Integer defectsCount) {
		this.defectsCount = defectsCount;
	}
	public String getDefectIds() {
		return defectIds;
	}
	public void setDefectIds(String defectIds) {
		this.defectIds = defectIds;
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
	public String getFailureReason() {
		return failureReason;
	}
	public void setFailureReason(String failureReason) {
		this.failureReason = failureReason;
	}
	public Long getExecutionTime() {
		return executionTime;
	}
	public void setExecutionTime(Long executionTime) {
		this.executionTime = executionTime;
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
	public TestSuiteListBean getTestSuiteList() {
		return testSuiteList;
	}
	public void setTestSuiteList(TestSuiteListBean testSuiteList) {
		this.testSuiteList = testSuiteList;
	}


	//private Set<TestExecutionResultBugList> testExecutionResultBugListSet;

	


}
