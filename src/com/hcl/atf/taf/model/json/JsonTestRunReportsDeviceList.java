package com.hcl.atf.taf.model.json;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.hcl.atf.taf.model.custom.TestRunReportsDeviceList;

public class JsonTestRunReportsDeviceList implements java.io.Serializable {

	
	@JsonProperty
	private Integer testRunNo;
	@JsonProperty
	private Integer testRunListId;
	@JsonProperty
	private String productName;
	@JsonProperty
	private String productVersionName;
	@JsonProperty
	private String deviceId;	
	@JsonProperty
	private Integer deviceListId;	
	@JsonProperty	
	private String devicePlatformVersion;
	@JsonProperty	
	private String devicePlatformName;	
	@JsonProperty	
	private String deviceModel;
	@JsonProperty
	private String testRunTriggeredTime;	
	@JsonProperty
	private String testRunStatus;
	@JsonProperty
	private String testRunFailureMessage;
	@JsonProperty
	private Integer testSuiteId;
	@JsonProperty
	private String testCaseName;
	@JsonProperty
	private String testStep;
	@JsonProperty
	private String testStepDescription;
	@JsonProperty
	private String testStepInput;
	@JsonProperty
	private String testStepExpectedOutput;
	@JsonProperty
	private String testStepObservedOutput;
	@JsonProperty
	private String testResultStatus;
	@JsonProperty
	private String deviceTestRunResultStatus;
	@JsonProperty
	private Integer testRunConfigurationChildId;
	@JsonProperty
	private String testRunconfigurationName;
	@JsonProperty
	private String testRunStartTime;	
	@JsonProperty
	private String testRunEndTime;	
	@JsonProperty
	private String testRunEvidenceStatus;
	
	public JsonTestRunReportsDeviceList(){
		
	}
	
	public Integer getTestRunListId() {
		return testRunListId;
	}

	public void setTestRunListId(Integer testRunListId) {
		this.testRunListId = testRunListId;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getProductVersionName() {
		return productVersionName;
	}

	public void setProductVersionName(String productVersionName) {
		this.productVersionName = productVersionName;
	}

	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}
	public Integer getDeviceListId() {
		return deviceListId;
	}

	public void setDeviceListId(Integer deviceListId) {
		this.deviceListId = deviceListId;
	}
	public String getDevicePlatformVersion() {
		return devicePlatformVersion;
	}

	public void setDevicePlatformVersion(String devicePlatformVersion) {
		this.devicePlatformVersion = devicePlatformVersion;
	}

	public String getDevicePlatformName() {
		return devicePlatformName;
	}

	public void setDevicePlatformName(String devicePlatformName) {
		this.devicePlatformName = devicePlatformName;
	}

	public String getDeviceModel() {
		return deviceModel;
	}

	public void setDeviceModele(String deviceModel) {
		this.deviceModel = deviceModel;
	}
	
	public String getTestRunTriggeredTime() {
		return testRunTriggeredTime;
	}

	public void setTestRunTriggeredTime(String testRunTriggeredTime) {
		this.testRunTriggeredTime = testRunTriggeredTime;
	}

	public String getTestRunStartTime() {
		return testRunStartTime;
	}

	public void setTestRunStartTime(String testRunStartTime) {
		this.testRunStartTime = testRunStartTime;
	}
	public String getTestRunEndTime() {
		return testRunEndTime;
	}

	public void setTestRunEndTime(String testRunEndTime) {
		this.testRunEndTime = testRunEndTime;
	}
	public String getTestRunStatus() {
		return testRunStatus;
	}

	public void setTestRunStatus(String testRunStatus) {
		this.testRunStatus = testRunStatus;
	}

	public String getTestRunFailureMessage() {
		return testRunFailureMessage;
	}

	public void setTestRunFailureMessage(String testRunFailureMessage) {
		this.testRunFailureMessage = testRunFailureMessage;
	}

	public String getDeviceTestRunResultStatus() {
		return deviceTestRunResultStatus;
	}

	public void setDeviceTestRunResultStatus(String deviceTestRunResultStatus) {
		this.deviceTestRunResultStatus = deviceTestRunResultStatus;
	}

	
	public JsonTestRunReportsDeviceList(TestRunReportsDeviceList testRunReportsDeviceList){
		
		testRunNo=testRunReportsDeviceList.getTestRunNo();
		testRunEvidenceStatus=testRunReportsDeviceList.getTestRunEvidenceStatus();
		testRunListId=testRunReportsDeviceList.getTestRunListId();
		productName=testRunReportsDeviceList.getProductName();
		productVersionName=testRunReportsDeviceList.getProductVersionName();
		deviceId=testRunReportsDeviceList.getDeviceId();
		deviceListId=testRunReportsDeviceList.getDeviceListId();
		devicePlatformVersion=testRunReportsDeviceList.getDevicePlatformVersion();
		devicePlatformName=testRunReportsDeviceList.getDevicePlatformName();	
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		testRunTriggeredTime=sdf.format(testRunReportsDeviceList.getTestRunTriggeredTime());
		testRunStatus=testRunReportsDeviceList.getTestRunStatus();
		testRunFailureMessage=testRunReportsDeviceList.getTestRunFailureMessage();
		testSuiteId=testRunReportsDeviceList.getTestSuiteId();
		testCaseName=testRunReportsDeviceList.getTestCaseName();
		testStep=testRunReportsDeviceList.getTestStep();
		testStepDescription=testRunReportsDeviceList.getTestStepDescription();
		testStepInput=testRunReportsDeviceList.getTestStepInput();
		testStepExpectedOutput=testRunReportsDeviceList.getTestStepExpectedOutput();
		testStepObservedOutput=testRunReportsDeviceList.getTestStepObservedOutput();
		testResultStatus=testRunReportsDeviceList.getTestResultStatus();
		deviceTestRunResultStatus=testRunReportsDeviceList.getDeviceTestRunResultStatus();
		testRunConfigurationChildId=testRunReportsDeviceList.getTestRunConfigurationChildId();
		testRunconfigurationName=testRunReportsDeviceList.getTestRunconfigurationName();
		if (testRunReportsDeviceList.getTestRunStartTime() != null) {
			testRunStartTime=sdf.format(testRunReportsDeviceList.getTestRunStartTime());
		}
		if (testRunReportsDeviceList.getTestRunEndTime() != null) {
			testRunEndTime=sdf.format(testRunReportsDeviceList.getTestRunEndTime());
		}
		
	}
	@JsonIgnore
	public TestRunReportsDeviceList getTestRunReportsDeviceList(){
		TestRunReportsDeviceList testRunReportsDeviceList = new TestRunReportsDeviceList();
		testRunReportsDeviceList.setTestRunNo(testRunNo);
		testRunReportsDeviceList.setTestRunEvidenceStatus(testRunEvidenceStatus);
		testRunReportsDeviceList.setTestRunListId(testRunListId);
		testRunReportsDeviceList.setProductName(productName);
		testRunReportsDeviceList.setProductVersionName(productVersionName);
		testRunReportsDeviceList.setDeviceId(deviceId);
		testRunReportsDeviceList.setDevicePlatformVersion(devicePlatformVersion);
		testRunReportsDeviceList.setDevicePlatformName(devicePlatformName);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			testRunReportsDeviceList.setTestRunTriggeredTime(sdf.parse(testRunTriggeredTime));
		} catch (ParseException e) {
		}
		testRunReportsDeviceList.setTestRunStatus(testRunStatus);
		testRunReportsDeviceList.setTestRunFailureMessage(testRunFailureMessage);
		testRunReportsDeviceList.setTestSuiteId(testSuiteId);
		testRunReportsDeviceList.setTestCaseName(testCaseName);
		testRunReportsDeviceList.setTestStep(testStep);
		testRunReportsDeviceList.setTestStepDescription(testStepDescription);
		testRunReportsDeviceList.setTestStepInput(testStepInput);
		testRunReportsDeviceList.setTestStepExpectedOutput(testStepExpectedOutput);
		testRunReportsDeviceList.setTestStepObservedOutput(testStepObservedOutput);
		testRunReportsDeviceList.setTestResultStatus(testResultStatus);
		testRunReportsDeviceList.setDeviceTestRunResultStatus(deviceTestRunResultStatus);
		testRunReportsDeviceList.setDeviceListId(deviceListId);
		testRunReportsDeviceList.setTestRunConfigurationChildId(testRunConfigurationChildId);
		testRunReportsDeviceList.setTestRunconfigurationName(testRunconfigurationName);
		if (testRunStartTime != null) {
			try {
				testRunReportsDeviceList.setTestRunStartTime(sdf.parse(testRunStartTime));
			} catch (ParseException e) { e.printStackTrace();}
		}
		if (testRunEndTime != null) {
			try {
				testRunReportsDeviceList.setTestRunEndTime(sdf.parse(testRunEndTime));
			} catch (ParseException e) { e.printStackTrace();}
		}
		
		return testRunReportsDeviceList;
	}

	public Integer getTestRunNo() {
		return testRunNo;
	}

	public void setTestRunNo(Integer testRunNo) {
		this.testRunNo = testRunNo;
	}

	public Integer getTestSuiteId() {
		return testSuiteId;
	}

	public void setTestSuiteId(Integer testSuiteId) {
		this.testSuiteId = testSuiteId;
	}

	public String getTestCaseName() {
		return testCaseName;
	}

	public void setTestCaseName(String testCaseName) {
		this.testCaseName = testCaseName;
	}

	public String getTestStep() {
		return testStep;
	}

	public void setTestStep(String testStep) {
		this.testStep = testStep;
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

	public String getTestStepObservedOutput() {
		return testStepObservedOutput;
	}

	public void setTestStepObservedOutput(String testStepObservedOutput) {
		this.testStepObservedOutput = testStepObservedOutput;
	}

	public String getTestResultStatus() {
		return testResultStatus;
	}

	public void setTestResultStatus(String testResultStatus) {
		this.testResultStatus = testResultStatus;
	}

	public Integer getTestRunConfigurationChildId() {
		return testRunConfigurationChildId;
	}

	public void setTestRunConfigurationChildId(Integer testRunConfigurationChildId) {
		this.testRunConfigurationChildId = testRunConfigurationChildId;
	}

	public String getTestRunconfigurationName() {
		return testRunconfigurationName;
	}

	public void setTestRunconfigurationName(String testRunconfigurationName) {
		this.testRunconfigurationName = testRunconfigurationName;
	}
	public String getTestRunEvidenceStatus() {
		return testRunEvidenceStatus;
	}
	public void setTestRunEvidenceStatus(String testRunEvidenceStatus) {
		this.testRunEvidenceStatus = testRunEvidenceStatus;
	}
}
