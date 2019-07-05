package com.hcl.atf.taf.model.json;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.hcl.atf.taf.controller.utilities.DateUtility;
import com.hcl.atf.taf.model.TestCycle;

public class JsonTestCycle {

	@JsonProperty
	private Integer testCycleId;
	@JsonProperty
	private Integer testRunPlanGroupId;
	@JsonProperty
	private String testRunPlanGroupName;
	@JsonProperty
	private Integer status;
	@JsonProperty
	private String result;
	@JsonProperty
	private String startTime;
	@JsonProperty
	private String endTime;
	@JsonProperty
	private String testCycleStatus;
	
	public JsonTestCycle(){
		
	}
	public JsonTestCycle(TestCycle testCycle){
		this.testCycleId = testCycle.getTestCycleId();
		if(testCycle.getTestRunPlanGroup() != null){
			this.testRunPlanGroupId = testCycle.getTestRunPlanGroup().getTestRunPlanGroupId();
			this.testRunPlanGroupName = testCycle.getTestRunPlanGroup().getName();
		}
		this.status = testCycle.getStatus();
		this.result = testCycle.getResult();
		if(testCycle.getStartTime() != null){
			this.startTime=DateUtility.dateToStringInSecond(testCycle.getStartTime());
		}
		if(testCycle.getEndTime() != null){
			this.endTime=DateUtility.dateToStringInSecond(testCycle.getEndTime());
		}
		this.testCycleStatus = testCycle.getTestCycleStatus();
	}
	
	public Integer getTestCycleId() {
		return testCycleId;
	}
	public void setTestCycleId(Integer testCycleId) {
		this.testCycleId = testCycleId;
	}
	public Integer getTestRunPlanGroupId() {
		return testRunPlanGroupId;
	}
	public void setTestRunPlanGroupId(Integer testRunPlanGroupId) {
		this.testRunPlanGroupId = testRunPlanGroupId;
	}
	public String getTestRunPlanGroupName() {
		return testRunPlanGroupName;
	}
	public void setTestRunPlanGroupName(String testRunPlanGroupName) {
		this.testRunPlanGroupName = testRunPlanGroupName;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public String getResult() {
		return result;
	}
	public void setResult(String result) {
		this.result = result;
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
	public String getTestCycleStatus() {
		return testCycleStatus;
	}
	public void setTestCycleStatus(String testCycleStatus) {
		this.testCycleStatus = testCycleStatus;
	}

}
