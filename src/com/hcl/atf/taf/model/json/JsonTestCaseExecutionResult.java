package com.hcl.atf.taf.model.json;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.hcl.atf.taf.controller.utilities.DateUtility;
import com.hcl.atf.taf.model.EnvironmentCombination;
import com.hcl.atf.taf.model.TestCaseExecutionResult;
import com.hcl.atf.taf.model.TestSuiteList;
import com.hcl.atf.taf.model.UserList;
import com.hcl.ilcm.workflow.model.WorkflowStatus;

public class JsonTestCaseExecutionResult implements java.io.Serializable{

	private static final Log log = LogFactory.getLog(JsonWorkPackage.class);

	@JsonProperty	
	private Integer testCaseExecutionResultId;
	@JsonProperty	
	private String result;
	@JsonProperty	
	private Integer defectsCount;
	@JsonProperty	
	private String defectIds;
	@JsonProperty	
	private String comments;
	@JsonProperty	
	private Integer isApproved;
	@JsonProperty	
	private Integer isReviewed;
	@JsonProperty	
	private String observedOutput;
	@JsonProperty	
	private String evidencePath;
	@JsonProperty	
	private String evidenceLabel;
	@JsonProperty	
	private Long executionTime;
	@JsonProperty	
	private String startTime; 
	@JsonProperty	
	private String endTime; 
	@JsonProperty	
	private Integer testSuiteId; 
	@JsonProperty	
	private Integer workflowStatusId;
	@JsonProperty	
	private String workflowStatusName;
	@JsonProperty	
	private Integer assigneeId;
	@JsonProperty	
	private String assigneeName;
	@JsonProperty	
	private Integer reviewerId;
	@JsonProperty	
	private String reviewerName;
	@JsonProperty	
	private String analysisRemarks;
	@JsonProperty
	private String analysisOutCome;
	@JsonProperty	
	private Integer environmentCombinationId;
	
	public JsonTestCaseExecutionResult(){
		
	}
	
	public JsonTestCaseExecutionResult(TestCaseExecutionResult testCaseExecutionResult){
		testCaseExecutionResultId=testCaseExecutionResult.getTestCaseExecutionResultId();
		result=testCaseExecutionResult.getResult();
		defectsCount=testCaseExecutionResult.getDefectsCount();
		defectIds=testCaseExecutionResult.getDefectIds();
		comments=testCaseExecutionResult.getComments();
		isApproved=testCaseExecutionResult.getIsApproved();
		isReviewed=testCaseExecutionResult.getIsReviewed();
		observedOutput=testCaseExecutionResult.getObservedOutput();
		executionTime=testCaseExecutionResult.getExecutionTime();
		
		if(testCaseExecutionResult.getStartTime() !=null)
		{
			this.startTime =   DateUtility.dateToStringInSecond(testCaseExecutionResult.getStartTime());
		}
		
		if(testCaseExecutionResult.getStartTime() !=null)
		{
			this.endTime =  DateUtility.dateToStringInSecond(testCaseExecutionResult.getEndTime());
		}
		testSuiteId = testCaseExecutionResult.getTestSuiteList().getTestSuiteId();
		
		if(testCaseExecutionResult.getAssignee() != null) {
			this.assigneeId = testCaseExecutionResult.getAssignee().getUserId();
			this.assigneeName = testCaseExecutionResult.getAssignee().getLoginId();
		}
		
		if(testCaseExecutionResult.getReviewer() != null) {
			this.reviewerId = testCaseExecutionResult.getReviewer().getUserId();
			this.reviewerName = testCaseExecutionResult.getReviewer().getLoginId();
		}
		
		if(testCaseExecutionResult.getWorkflowStatus() != null) {
			this.workflowStatusId = testCaseExecutionResult.getWorkflowStatus().getWorkflowStatusId();
			this.workflowStatusName = testCaseExecutionResult.getWorkflowStatus().getWorkflowStatusName();
		}
		this.analysisRemarks = testCaseExecutionResult.getAnalysisRemarks();
		
		this.analysisOutCome = testCaseExecutionResult.getAnalysisOutCome();
		if(testCaseExecutionResult.getEnvironmentCombination() != null ){
			this.environmentCombinationId = testCaseExecutionResult.getEnvironmentCombination().getEnvironment_combination_id();
		}
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

	public String getEvidencePath() {
		return evidencePath;
	}

	public void setEvidencePath(String evidencePath) {
		this.evidencePath = evidencePath;
	}

	public String getEvidenceLabel() {
		return evidenceLabel;
	}

	public void setEvidenceLabel(String evidenceLabel) {
		this.evidenceLabel = evidenceLabel;
	}
	
	@JsonIgnore
	public TestCaseExecutionResult getTestCaseExecutionResult() {
		TestCaseExecutionResult testCaseExecutionResult = new TestCaseExecutionResult();
		TestSuiteList testSuiteList = new TestSuiteList();
		testCaseExecutionResult.setTestCaseExecutionResultId(testCaseExecutionResultId);
		testCaseExecutionResult.setResult(result);
		testCaseExecutionResult.setDefectsCount(defectsCount);
		testCaseExecutionResult.setDefectIds(defectIds);
		testCaseExecutionResult.setComments(comments);
		testCaseExecutionResult.setIsApproved(isApproved);
		testCaseExecutionResult.setIsReviewed(isReviewed);
		testCaseExecutionResult.setObservedOutput(observedOutput);
		testCaseExecutionResult.setExecutionTime(executionTime);
		
		testCaseExecutionResult.setStartTime(DateUtility.toDateInSec(this.startTime));
		
		testCaseExecutionResult.setEndTime(DateUtility.toDateInSec(this.endTime));
		testSuiteList.setTestSuiteId(testSuiteId);
		testCaseExecutionResult.setTestSuiteList(testSuiteList);
		
		if( this.assigneeId != null) {
			UserList assignee = new UserList();
			assignee.setUserId(this.assigneeId);
			testCaseExecutionResult.setAssignee(assignee);
		}
		if( this.reviewerId != null) {
			UserList reviewer = new UserList();
			reviewer.setUserId(this.reviewerId);
			testCaseExecutionResult.setReviewer(reviewer);
		}
		
		if( this.workflowStatusId != null) {
			WorkflowStatus status = new WorkflowStatus();
			status.setWorkflowStatusId(workflowStatusId);
			testCaseExecutionResult.setWorkflowStatus(status);
		}
		if( this.environmentCombinationId != null) {
			EnvironmentCombination environmentCombination = new EnvironmentCombination();
			environmentCombination.setEnvironment_combination_id(environmentCombinationId);
			testCaseExecutionResult.setEnvironmentCombination(environmentCombination);
		}
		
		testCaseExecutionResult.setAnalysisRemarks(analysisRemarks);
		return testCaseExecutionResult;
		
	}

	public Long getExecutionTime() {
		return executionTime;
	}

	public void setExecutionTime(Long executionTime) {
		this.executionTime = executionTime;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public Integer getTestSuiteId() {
		return testSuiteId;
	}

	public void setTestSuiteId(Integer testSuiteId) {
		this.testSuiteId = testSuiteId;
	}

	public Integer getWorkflowStatusId() {
		return workflowStatusId;
	}

	public void setWorkflowStatusId(Integer workflowStatusId) {
		this.workflowStatusId = workflowStatusId;
	}

	public String getWorkflowStatusName() {
		return workflowStatusName;
	}

	public void setWorkflowStatusName(String workflowStatusName) {
		this.workflowStatusName = workflowStatusName;
	}

	public Integer getAssigneeId() {
		return assigneeId;
	}

	public void setAssigneeId(Integer assigneeId) {
		this.assigneeId = assigneeId;
	}

	public String getAssigneeName() {
		return assigneeName;
	}

	public void setAssigneeName(String assigneeName) {
		this.assigneeName = assigneeName;
	}

	public Integer getReviewerId() {
		return reviewerId;
	}

	public void setReviewerId(Integer reviewerId) {
		this.reviewerId = reviewerId;
	}

	public String getReviewerName() {
		return reviewerName;
	}

	public void setReviewerName(String reviewerName) {
		this.reviewerName = reviewerName;
	}

	public String getAnalysisRemarks() {
		return analysisRemarks;
	}

	public void setAnalysisRemarks(String analysisRemarks) {
		this.analysisRemarks = analysisRemarks;
	}

	public String getAnalysisOutCome() {
		return analysisOutCome;
	}

	public void setAnalysisOutCome(String analysisOutCome) {
		this.analysisOutCome = analysisOutCome;
	}

	public Integer getEnvironmentCombinationId() {
		return environmentCombinationId;
	}

	public void setEnvironmentCombinationId(Integer environmentCombinationId) {
		this.environmentCombinationId = environmentCombinationId;
	}
	
	
	
}
