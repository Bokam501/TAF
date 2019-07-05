package com.hcl.atf.taf.model;

// Generated Feb 4, 2014 4:30:16 PM by Hibernate Tools 3.4.0.CR1

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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * TestRunList generated by hbm2java
 */
@Entity
@Table(name = "test_run_list")
public class TestRunList implements java.io.Serializable {

	private Integer testRunListId;
	private TestResultStatusMaster testResultStatusMaster;
	private DeviceList deviceList;
	private TestRunConfigurationChild testRunConfigurationChild;
	private Date testRunTriggeredTime;
	private String testRunFailureMessage;
	private int buildNo;
	private int runNo;
	private Date testRunStartTime;
	private Date testRunEndTime;
	private TestEnvironmentDevices testEnvironmentDevices;
	private Set<TestExecutionResult> testExecutionResults = new HashSet<TestExecutionResult>(
			0);
	private String testRunEvidenceStatus;
	
	private TrccExecutionPlan trccExecutionPlan;
	
	public TestRunList() {
	}

	public TestRunList(TestResultStatusMaster testResultStatusMaster,
			DeviceList deviceList,
			TestRunConfigurationChild testRunConfigurationChild,
			Date testRunTriggeredTime, int buildNo, int runNo) {
		this.testResultStatusMaster = testResultStatusMaster;
		this.deviceList = deviceList;
		this.testRunConfigurationChild = testRunConfigurationChild;
		this.testRunTriggeredTime = testRunTriggeredTime;
		this.buildNo = buildNo;
		this.runNo = runNo;
	}

	public TestRunList(TestResultStatusMaster testResultStatusMaster,
			DeviceList deviceList,
			TestRunConfigurationChild testRunConfigurationChild,
			Date testRunTriggeredTime, String testRunFailureMessage,
			int buildNo, int runNo,
			Set<TestExecutionResult> testExecutionResults) {
		this.testResultStatusMaster = testResultStatusMaster;
		this.deviceList = deviceList;
		this.testRunConfigurationChild = testRunConfigurationChild;
		this.testRunTriggeredTime = testRunTriggeredTime;
		this.testRunFailureMessage = testRunFailureMessage;
		this.buildNo = buildNo;
		this.runNo = runNo;
		this.testExecutionResults = testExecutionResults;
	}
	
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "testRunListId", unique = true, nullable = false)
	public Integer getTestRunListId() {
		return this.testRunListId;
	}

	public void setTestRunListId(Integer testRunListId) {
		this.testRunListId = testRunListId;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "testRunStatus")
	public TestResultStatusMaster getTestResultStatusMaster() {
		return this.testResultStatusMaster;
	}

	public void setTestResultStatusMaster(
			TestResultStatusMaster testResultStatusMaster) {
		this.testResultStatusMaster = testResultStatusMaster;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "deviceListId", nullable = false)
	public DeviceList getDeviceList() {
		return this.deviceList;
	}

	public void setDeviceList(DeviceList deviceList) {
		this.deviceList = deviceList;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "testEnvironmentDevicesId")
	public TestEnvironmentDevices getTestEnvironmentDevices() {
		return this.testEnvironmentDevices;
	}

	public void setTestEnvironmentDevices(TestEnvironmentDevices testEnvironmentDevices) {
		this.testEnvironmentDevices = testEnvironmentDevices;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "testRunConfigurationChildId", nullable = false)
	public TestRunConfigurationChild getTestRunConfigurationChild() {
		return this.testRunConfigurationChild;
	}

	public void setTestRunConfigurationChild(
			TestRunConfigurationChild testRunConfigurationChild) {
		this.testRunConfigurationChild = testRunConfigurationChild;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "testRunTriggeredTime")
	public Date getTestRunTriggeredTime() {
		return this.testRunTriggeredTime;
	}

	public void setTestRunTriggeredTime(Date testRunTriggeredTime) {
		this.testRunTriggeredTime = testRunTriggeredTime;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "testRunStartTime", nullable = true, length = 19)
	public Date getTestRunStartTime() {
		return this.testRunStartTime;
	}

	public void setTestRunStartTime(Date testRunStartTime) {
		this.testRunStartTime = testRunStartTime;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "testRunEndTime", nullable = true, length = 19)
	public Date getTestRunEndTime() {
		return this.testRunEndTime;
	}

	public void setTestRunEndTime(Date testRunEndTime) {
		this.testRunEndTime = testRunEndTime;
	}

	@Column(name = "testRunFailureMessage", length = 100)
	public String getTestRunFailureMessage() {
		return this.testRunFailureMessage;
	}

	public void setTestRunFailureMessage(String testRunFailureMessage) {
		this.testRunFailureMessage = testRunFailureMessage;
	}

	@Column(name = "buildNo")
	public int getBuildNo() {
		return this.buildNo;
	}

	public void setBuildNo(int buildNo) {
		this.buildNo = buildNo;
	}

	@Column(name = "runNo")
	public int getRunNo() {
		return this.runNo;
	}

	public void setRunNo(int runNo) {
		this.runNo = runNo;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "testRunList")
	public Set<TestExecutionResult> getTestExecutionResults() {
		return this.testExecutionResults;
	}

	public void setTestExecutionResults(
			Set<TestExecutionResult> testExecutionResults) {
		this.testExecutionResults = testExecutionResults;
	}
	
	@Column(name = "testRunEvidenceStatus", length = 50)
	public String getTestRunEvidenceStatus() {
		return this.testRunEvidenceStatus;
	}

	public void setTestRunEvidenceStatus(String testRunEvidenceStatus) {
		this.testRunEvidenceStatus = testRunEvidenceStatus;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "trccExecutionPlanId")
	public TrccExecutionPlan getTrccExecutionPlan() {
		return this.trccExecutionPlan;
	}

	public void setTrccExecutionPlan(TrccExecutionPlan trccExecutionPlan) {
		this.trccExecutionPlan = trccExecutionPlan;
	}
}