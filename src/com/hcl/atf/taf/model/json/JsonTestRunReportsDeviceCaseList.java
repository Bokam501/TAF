package com.hcl.atf.taf.model.json;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.hcl.atf.taf.model.custom.TestRunReportsDeviceCaseList;

public class JsonTestRunReportsDeviceCaseList implements java.io.Serializable {

	
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
	private String devicePlatformVersion;
	@JsonProperty	
	private String devicePlatformName;	
	@JsonProperty
	private Integer testRunConfigurationChildId;
	@JsonProperty
	private String testRunconfigurationName;
	@JsonProperty
	private String testRunTriggeredTime;	
	@JsonProperty
	private String testRunStatus;
	@JsonProperty
	private String testRunFailureMessage;
	@JsonProperty
	private Integer testSuiteId;
	@JsonProperty
	private String testSuiteName;
	@JsonProperty
	private Integer testCaseId;
	@JsonProperty
	private String testCaseName;
	@JsonProperty
	private String testCaseDescription;
	@JsonProperty
	private String testCaseResult;
	@JsonProperty
	private String testCaseStartTime;
	@JsonProperty
	private String testCaseEndTime;

	
	public JsonTestRunReportsDeviceCaseList(){
		
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

	public String getTestRunTriggeredTime() {
		return testRunTriggeredTime;
	}

	public void setTestRunTriggeredTime(String testRunTriggeredTime) {
		this.testRunTriggeredTime = testRunTriggeredTime;
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

	

	
	public JsonTestRunReportsDeviceCaseList(TestRunReportsDeviceCaseList testRunReportsDeviceCaseList){
		
		testRunNo=testRunReportsDeviceCaseList.getTestRunNo();
		testRunListId=testRunReportsDeviceCaseList.getTestRunListId();
		productName=testRunReportsDeviceCaseList.getProductName();
		productVersionName=testRunReportsDeviceCaseList.getProductVersionName();
		deviceId=testRunReportsDeviceCaseList.getDeviceId();
		devicePlatformVersion=testRunReportsDeviceCaseList.getDevicePlatformVersion();
		devicePlatformName=testRunReportsDeviceCaseList.getDevicePlatformName();
		testRunConfigurationChildId=testRunReportsDeviceCaseList.getTestRunConfigurationChildId();
		testRunconfigurationName=testRunReportsDeviceCaseList.getTestRunconfigurationName();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		testRunTriggeredTime=sdf.format(testRunReportsDeviceCaseList.getTestRunTriggeredTime());
		testRunStatus=testRunReportsDeviceCaseList.getTestRunStatus();
		testRunFailureMessage=testRunReportsDeviceCaseList.getTestRunFailureMessage();
		testSuiteId=testRunReportsDeviceCaseList.getTestSuiteId();
		testSuiteName=testRunReportsDeviceCaseList.getTestSuiteName();
		testCaseId=testRunReportsDeviceCaseList.getTestCaseId();
		testCaseName=testRunReportsDeviceCaseList.getTestCaseName();
		testCaseDescription=testRunReportsDeviceCaseList.getTestCaseDescription();
		testCaseResult=testRunReportsDeviceCaseList.getTestCaseResult();
		if (testRunReportsDeviceCaseList.getTestCaseStartTime()!=null){
				testCaseStartTime=sdf.format(testRunReportsDeviceCaseList.getTestCaseStartTime());
		}
		if (testRunReportsDeviceCaseList.getTestCaseEndTime()!=null){
				testCaseEndTime=sdf.format(testRunReportsDeviceCaseList.getTestCaseEndTime());
		}
	}
	@JsonIgnore
	public TestRunReportsDeviceCaseList getTestRunReportsDeviceCaseList(){
		TestRunReportsDeviceCaseList testRunReportsDeviceCaseList = new TestRunReportsDeviceCaseList();
		testRunReportsDeviceCaseList.setTestRunNo(testRunNo);
		testRunReportsDeviceCaseList.setTestRunListId(testRunListId);
		testRunReportsDeviceCaseList.setProductName(productName);
		testRunReportsDeviceCaseList.setProductVersionName(productVersionName);
		testRunReportsDeviceCaseList.setDeviceId(deviceId);
		testRunReportsDeviceCaseList.setDevicePlatformVersion(devicePlatformVersion);
		testRunReportsDeviceCaseList.setDevicePlatformName(devicePlatformName);
		testRunReportsDeviceCaseList.setTestRunConfigurationChildId(testRunConfigurationChildId);
		testRunReportsDeviceCaseList.setTestRunconfigurationName(testRunconfigurationName);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			testRunReportsDeviceCaseList.setTestRunTriggeredTime(sdf.parse(testRunTriggeredTime));
			testRunReportsDeviceCaseList.setTestCaseStartTime(sdf.parse(testCaseStartTime));
			testRunReportsDeviceCaseList.setTestCaseEndTime(sdf.parse(testCaseEndTime));
		} catch (ParseException e) {
		}
		testRunReportsDeviceCaseList.setTestRunStatus(testRunStatus);
		testRunReportsDeviceCaseList.setTestRunFailureMessage(testRunFailureMessage);
		testRunReportsDeviceCaseList.setTestSuiteId(testSuiteId);
		testRunReportsDeviceCaseList.setTestCaseId(testCaseId);
		testRunReportsDeviceCaseList.setTestCaseName(testCaseName);
		testRunReportsDeviceCaseList.setTestCaseDescription(testCaseDescription);
		testRunReportsDeviceCaseList.setTestCaseResult(testCaseResult);
		return testRunReportsDeviceCaseList;
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

	public Integer getTestCaseId() {
		return testCaseId;
	}

	public void setTestCaseId(Integer testCaseId) {
		this.testCaseId = testCaseId;
	}

	public String getTestCaseName() {
		return testCaseName;
	}

	public void setTestCaseName(String testCaseName) {
		this.testCaseName = testCaseName;
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

	public String getTestSuiteName() {
		return testSuiteName;
	}

	public void setTestSuiteName(String testSuiteName) {
		this.testSuiteName = testSuiteName;
	}

	public String getTestCaseResult() {
		return testCaseResult;
	}

	public void setTestCaseResult(String testCaseResult) {
		this.testCaseResult = testCaseResult;
	}

	public String getTestCaseStartTime() {
		return testCaseStartTime;
	}

	public void setTestCaseStartTime(String testCaseStartTime) {
		this.testCaseStartTime = testCaseStartTime;
	}

	public String getTestCaseEndTime() {
		return testCaseEndTime;
	}

	public void setTestCaseEndTime(String testCaseEndTime) {
		this.testCaseEndTime = testCaseEndTime;
	}

	public String getTestCaseDescription() {
		return testCaseDescription;
	}

	public void setTestCaseDescription(String testCaseDescription) {
		this.testCaseDescription = testCaseDescription;
	}
}
