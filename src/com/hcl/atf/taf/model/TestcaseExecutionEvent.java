package com.hcl.atf.taf.model;


import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;

import static javax.persistence.GenerationType.IDENTITY;

import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.web.bind.annotation.RequestParam;

/**
 * TestcaseExecutionEvent 
 */
@Entity
@Table(name = "testcase_execution_event")
public class TestcaseExecutionEvent implements java.io.Serializable {
	
	public static final Integer TEV_EXPIRY_POLICY_SINGLEUSE = 0;
	public static final Integer TEV_EXPIRY_POLICY_JOB_COMPLETION = 1;
	public static final Integer TEV_EXPIRY_POLICY_WORKPACKAGE_COMPLETION = 2; // Default
	public static final Integer TEV_EXPIRY_POLICY_TIME_BASED = 3;
	public static final Integer TEV_EXPIRY_POLICY_IMMORTAL = 4; //Not recommended

	private Integer testcaseExecutionEventId;
	private String testcaseName;
	private Integer jobId;
	private Integer workpackageId;

	private String eventName;
	private String payload;
	private Date eventReportedTime;

	private Integer expiryPolicy;
	private Date eventExpiryTime;
	
	public TestcaseExecutionEvent() {
		
	}

	public TestcaseExecutionEvent(String testcaseName, Integer jobId, Integer workpackageId, String eventName, String payload, Long eventReportedTime, Integer expiryPolicy, Long expiryTime) {
	
		this.testcaseName = testcaseName;
		this.jobId = jobId;
		this.workpackageId = workpackageId;
		this.eventName = eventName;
		this.payload = payload;
		
		if (eventReportedTime != null)
			this.eventReportedTime = new Date(eventReportedTime);
		else
			this.eventReportedTime = new Date(System.currentTimeMillis());

		this.expiryPolicy = expiryPolicy;
		
		if (expiryTime != null)
			this.eventExpiryTime = new Date(expiryTime);
	}

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "testcaseExecutionEventId", unique = true, nullable = false)
	public Integer getTestcaseExecutionEventId() {
		return testcaseExecutionEventId;
	}

	public void setTestcaseExecutionEventId(Integer testcaseExecutionEventId) {
		this.testcaseExecutionEventId = testcaseExecutionEventId;
	}

	@Column(name = "testcaseName")
	public String getTestcaseName() {
		return testcaseName;
	}

	public void setTestcaseName(String testcaseName) {
		this.testcaseName = testcaseName;
	}

	@Column(name = "jobId")
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

	@Column(name = "eventName")
	public String getEventName() {
		return eventName;
	}

	public void setEventName(String eventName) {
		this.eventName = eventName;
	}

	@Column(name = "payload")
	public String getPayload() {
		return payload;
	}

	public void setPayload(String payload) {
		this.payload = payload;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "eventReportedTime")
	public Date getEventReportedTime() {
		return eventReportedTime;
	}

	public void setEventReportedTime(Date eventReportedTime) {
		this.eventReportedTime = eventReportedTime;
	}

	@Column(name = "expiryPolicy")
	public Integer getExpiryPolicy() {
		return expiryPolicy;
	}

	public void setExpiryPolicy(Integer expiryPolicy) {
		this.expiryPolicy = expiryPolicy;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "eventExpiryTime")
	public Date getEventExpiryTime() {
		return eventExpiryTime;
	}

	public void setEventExpiryTime(Date eventExpiryTime) {
		this.eventExpiryTime = eventExpiryTime;
	}

}
