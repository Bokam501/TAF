package com.hcl.atf.taf.model.json;


import java.util.Date;

import javax.persistence.Column;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.hcl.atf.taf.model.TestcaseExecutionEvent;

/**
 * JsonTestcaseExecutionEvent 
 */
public class JsonTestcaseExecutionEvent implements java.io.Serializable {

	@JsonProperty	
	private Integer testcaseExecutionEventId;
	@JsonProperty	
	private String testcaseName;
	@JsonProperty	
	private Integer jobId;
	@JsonProperty	
	private Integer workpackageId;

	@JsonProperty	
	private String eventName;
	@JsonProperty	
	private String payload;
	@JsonProperty	
	private long eventReportedTime;

	@JsonProperty	
	private Integer expiryPolicy;
	@JsonProperty	
	private long eventExpiryTime;
	
	public JsonTestcaseExecutionEvent() {
		
	}

	public JsonTestcaseExecutionEvent(TestcaseExecutionEvent testcaseExecutionEvent) {
	
		this.testcaseExecutionEventId = testcaseExecutionEvent.getTestcaseExecutionEventId();
		this.testcaseName = testcaseExecutionEvent.getTestcaseName();
		this.jobId = testcaseExecutionEvent.getJobId();
		this.workpackageId = testcaseExecutionEvent.getWorkpackageId();
		this.eventName = testcaseExecutionEvent.getEventName();
		this.payload = testcaseExecutionEvent.getPayload();
		
		if (testcaseExecutionEvent.getEventReportedTime() != null)
			this.eventReportedTime = testcaseExecutionEvent.getEventReportedTime().getTime();

		this.expiryPolicy = testcaseExecutionEvent.getExpiryPolicy();
		
		if (testcaseExecutionEvent.getEventExpiryTime() != null)
			this.eventExpiryTime = testcaseExecutionEvent.getEventExpiryTime().getTime();
	}

	public Integer getTestcaseExecutionEventId() {
		return testcaseExecutionEventId;
	}

	public void setTestcaseExecutionEventId(Integer testcaseExecutionEventId) {
		this.testcaseExecutionEventId = testcaseExecutionEventId;
	}

	
	public String getTestcaseName() {
		return testcaseName;
	}

	public void setTestcaseId(String testcaseName) {
		this.testcaseName = testcaseName;
	}

	public Integer getJobId() {
		return jobId;
	}

	public void setJobId(Integer jobId) {
		this.jobId = jobId;
	}

	@Column(name = "workpackageId")
	public Integer getWorkpackageId() {
		return workpackageId;
	}

	public void setWorkpackageId(Integer workpackageId) {
		this.workpackageId = workpackageId;
	}

	public String getEventName() {
		return eventName;
	}

	public void setEventName(String eventName) {
		this.eventName = eventName;
	}

	public String getPayload() {
		return payload;
	}

	public void setPayload(String payload) {
		this.payload = payload;
	}

	public long getEventReportedTime() {
		return eventReportedTime;
	}

	public void setEventReportedTime(Date eventReportedTime) {
		this.eventReportedTime = eventReportedTime.getTime();
	}

	public Integer getExpiryPolicy() {
		return expiryPolicy;
	}

	public void setExpiryPolicy(Integer expiryPolicy) {
		this.expiryPolicy = expiryPolicy;
	}

	public long getEventExpiryTime() {
		return eventExpiryTime;
	}

	public void setEventExpiryTime(Date eventExpiryTime) {
		this.eventExpiryTime = eventExpiryTime.getTime();
	}

}
