package com.hcl.atf.taf.model.json;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.hcl.atf.taf.controller.utilities.DateUtility;
import com.hcl.atf.taf.model.EnvironmentCombination;
import com.hcl.atf.taf.model.TestCaseExecutionResult;
import com.hcl.atf.taf.model.TestCaseList;
import com.hcl.atf.taf.model.TestCaseStepsList;
import com.hcl.atf.taf.model.TestStepExecutionResult;

public class JsonTestStepExecutionResult implements java.io.Serializable{

	private static final Log log = LogFactory.getLog(JsonWorkPackage.class);


	@JsonProperty	
	private Integer teststepexecutionresultid;
	@JsonProperty	
	private String result;
	@JsonProperty	
	private Integer testCaseExecutionResultId;
	@JsonProperty	
	private String comments;
	@JsonProperty	
	private Integer isApproved;
	@JsonProperty	
	private Integer isReviewed;
	@JsonProperty	
	private String observedOutput;
	@JsonProperty	
	private Integer testStepsId;
	@JsonProperty	
	private String testStepsName;
	@JsonProperty	
	private String description;
	@JsonProperty	
	private String input;
	@JsonProperty	
	private String expectedOutput;
	@JsonProperty	
	private String code;
	
	@JsonProperty	
	private Integer testcaseId;
	@JsonProperty	
	private String evidencePath;
	@JsonProperty	
	private String evidenceLabel;
	
	@JsonProperty	
	private String testStepStarttime;
	@JsonProperty	
	private String testStepEndtime;
	@JsonProperty	
	private String failureReason;
	@JsonProperty	
	private Integer environmentCombinationId;
	@JsonProperty	
	private Integer testjobId;
	
	
	public JsonTestStepExecutionResult(){
		
	}
	
	public JsonTestStepExecutionResult(TestStepExecutionResult testStepExecutionResult){
		this.teststepexecutionresultid = testStepExecutionResult.getTeststepexecutionresultid();
		this.result=testStepExecutionResult.getResult();
		this.comments=testStepExecutionResult.getComments();
		this.isApproved=testStepExecutionResult.getIsApproved();
		this.isReviewed=testStepExecutionResult.getIsReviewed();
		this.observedOutput=testStepExecutionResult.getObservedOutput();
		if(testStepExecutionResult.getTestCaseExecutionResult()!=null)
			this.testCaseExecutionResultId=testStepExecutionResult.getTestCaseExecutionResult().getTestCaseExecutionResultId();
		if(testStepExecutionResult.getTestSteps()!=null){
			this.testStepsId=testStepExecutionResult.getTestSteps().getTestStepId();
			this.testStepsName=testStepExecutionResult.getTestSteps().getTestStepName();
			this.description=testStepExecutionResult.getTestSteps().getTestStepDescription();
			this.input=testStepExecutionResult.getTestSteps().getTestStepInput();
			this.expectedOutput=testStepExecutionResult.getTestSteps().getTestStepExpectedOutput();
			this.code=testStepExecutionResult.getTestSteps().getTestStepCode();
		}
		if(testStepExecutionResult.getTestcase()!=null){
			this.testcaseId=testStepExecutionResult.getTestcase().getTestCaseId();
		}
		
		
		if(testStepExecutionResult.getTestStepStarttime() !=null)
		{
			this.testStepStarttime =   DateUtility.dateToStringInSecond(testStepExecutionResult.getTestStepEndtime());
		}
		
		if(testStepExecutionResult.getTestStepEndtime()!=null)
		{
			this.testStepEndtime =   DateUtility.dateToStringInSecond(testStepExecutionResult.getTestStepStarttime());
		}
		this.failureReason=testStepExecutionResult.getFailureReason();
		if(testStepExecutionResult.getEnvironmentCombination() != null ){
			this.environmentCombinationId = testStepExecutionResult.getEnvironmentCombination().getEnvironment_combination_id();
		}	
	}
	
	@JsonIgnore
	public TestStepExecutionResult getTestStepExecutionResult() {
		TestStepExecutionResult testStepExecutionResult = new TestStepExecutionResult();
		testStepExecutionResult.setTeststepexecutionresultid(this.teststepexecutionresultid);
		testStepExecutionResult.setResult(this.result);
		testStepExecutionResult.setComments(this.comments);
		testStepExecutionResult.setObservedOutput(this.observedOutput);
		
		testStepExecutionResult.setTestStepStarttime(DateUtility.toDateInSec(this.testStepStarttime));
		testStepExecutionResult.setTestStepEndtime(DateUtility.toDateInSec(this.testStepEndtime));
		testStepExecutionResult.setFailureReason(this.failureReason);
		
		if(this.testCaseExecutionResultId!=null){
			TestCaseExecutionResult testCaseExecutionResult = new TestCaseExecutionResult();
			testCaseExecutionResult.setTestCaseExecutionResultId(testCaseExecutionResultId);
			testStepExecutionResult.setTestCaseExecutionResult(testCaseExecutionResult);
		}
		if(this.testStepsId!=null){
			TestCaseStepsList testCaseStepsList = new TestCaseStepsList();
			testCaseStepsList.setTestStepId(testStepsId);
			testStepExecutionResult.setTestSteps(testCaseStepsList);
		}
		if(this.testcaseId!=null){
			TestCaseList testCaseList= new TestCaseList();
			testCaseList.setTestCaseId(testcaseId);
			testStepExecutionResult.setTestcase(testCaseList);
		}
		if( this.environmentCombinationId != null) {
			EnvironmentCombination environmentCombination = new EnvironmentCombination();
			environmentCombination.setEnvironment_combination_id(environmentCombinationId);
			testStepExecutionResult.setEnvironmentCombination(environmentCombination);
		}
		return testStepExecutionResult;
	}

	public Integer getTeststepexecutionresultid() {
		return teststepexecutionresultid;
	}

	public void setTeststepexecutionresultid(Integer teststepexecutionresultid) {
		this.teststepexecutionresultid = teststepexecutionresultid;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public Integer getTestCaseExecutionResultId() {
		return testCaseExecutionResultId;
	}

	public void setTestCaseExecutionResultId(Integer testCaseExecutionResultId) {
		this.testCaseExecutionResultId = testCaseExecutionResultId;
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

	public Integer getTestStepsId() {
		return testStepsId;
	}

	public void setTestStepsId(Integer testStepsId) {
		this.testStepsId = testStepsId;
	}

	public String getTestStepsName() {
		return testStepsName;
	}

	public void setTestStepsName(String testStepsName) {
		this.testStepsName = testStepsName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getInput() {
		return input;
	}

	public void setInput(String input) {
		this.input = input;
	}

	public String getExpectedOutput() {
		return expectedOutput;
	}

	public void setExpectedOutput(String expectedOutput) {
		this.expectedOutput = expectedOutput;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Integer getTestcaseId() {
		return testcaseId;
	}

	public void setTestcaseId(Integer testcaseId) {
		this.testcaseId = testcaseId;
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

	public String getTestStepStarttime() {
		return testStepStarttime;
	}

	public void setTestStepStarttime(String testStepStarttime) {
		this.testStepStarttime = testStepStarttime;
	}

	public String getTestStepEndtime() {
		return testStepEndtime;
	}

	public void setTestStepEndtime(String testStepEndtime) {
		this.testStepEndtime = testStepEndtime;
	}

	public String getFailureReason() {
		return failureReason;
	}

	public void setFailureReason(String failureReason) {
		this.failureReason = failureReason;
	}

	public Integer getTestjobId() {
		return testjobId;
	}

	public void setTestjobId(Integer testjobId) {
		this.testjobId = testjobId;
	}

	public Integer getEnvironmentCombinationId() {
		return environmentCombinationId;
	}

	public void setEnvironmentCombinationId(Integer environmentCombinationId) {
		this.environmentCombinationId = environmentCombinationId;
	}

	
	
}
