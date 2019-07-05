package com.hcl.atf.taf.model.json;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.hcl.atf.taf.model.custom.TestRunReportsDeviceCaseStepList;

public class JsonTestRunReportsDeviceCaseStepList implements java.io.Serializable {

	@JsonProperty
	private Integer testRunNo;
	@JsonProperty
	private Integer testRunListId;
	@JsonProperty
	private Integer productId;
	@JsonProperty
	private String productName;
	@JsonProperty
	private Integer productVersionListId;
	@JsonProperty
	private String productVersionName;
	@JsonProperty
	private String deviceId;	
	@JsonProperty	
	private String devicePlatformName;
	@JsonProperty	
	private String devicePlatformVersion;
	@JsonProperty
	private String testEnvironmentName;
	@JsonProperty
	private Integer testRunConfigurationChildId;
	@JsonProperty
	private String testRunconfigurationName;
	@JsonProperty
	private String testRunTriggeredTime;	
	@JsonProperty
	private Integer testRunStatus;	
	@JsonProperty
	private Integer testSuiteId;
	@JsonProperty
	private String testSuiteName;
	@JsonProperty
	private Integer testCaseId;
	@JsonProperty
	private String testCaseName;
	@JsonProperty
	private Integer testStepId;
	@JsonProperty
	private String testStepName;
	@JsonProperty
	private String testStepDescription;
	@JsonProperty
	private String testStepInput;
	@JsonProperty
	private String testStepExpectedOutput;
	@JsonProperty
	private String testStepObservedOutput;
	@JsonProperty
	private String testStepResult;
	@JsonProperty
	private String executionRemarks;
	@JsonProperty
	private String failureReason;
	@JsonProperty
	private String testStepStartTime;
	@JsonProperty
	private String testStepEndTime;
	@JsonProperty
	private String screenShotPath;
	@JsonProperty
	private String screenShotLabel;
		
	public JsonTestRunReportsDeviceCaseStepList(){
		
	}
	
	public JsonTestRunReportsDeviceCaseStepList(TestRunReportsDeviceCaseStepList testRunReportsDeviceCaseStepList){
		
		testRunNo=testRunReportsDeviceCaseStepList.getTestrunno();
		testRunListId=testRunReportsDeviceCaseStepList.getTestrunlistid();
		productId=testRunReportsDeviceCaseStepList.getProductid();
		productName=testRunReportsDeviceCaseStepList.getProductname();
		productVersionListId=testRunReportsDeviceCaseStepList.getProductversionlistid();
		productVersionName=testRunReportsDeviceCaseStepList.getProductversionname();
		deviceId=testRunReportsDeviceCaseStepList.getDeviceid();
		devicePlatformVersion=testRunReportsDeviceCaseStepList.getDeviceplatformversion();
		devicePlatformName=testRunReportsDeviceCaseStepList.getDeviceplatformname();
		testEnvironmentName=testRunReportsDeviceCaseStepList.getTestenvironmentname();
		testRunConfigurationChildId=testRunReportsDeviceCaseStepList.getTestrunconfigurationchildid();
		testRunconfigurationName=testRunReportsDeviceCaseStepList.getTestrunconfigurationname();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		testRunTriggeredTime=sdf.format(testRunReportsDeviceCaseStepList.getTestruntriggeredtime());
		testRunStatus=testRunReportsDeviceCaseStepList.getTestrunstatus();
		testSuiteId=testRunReportsDeviceCaseStepList.getTestsuiteid();
		testSuiteName=testRunReportsDeviceCaseStepList.getTestsuitename();
		testCaseId=testRunReportsDeviceCaseStepList.getTestsuiteid();
		testCaseName=testRunReportsDeviceCaseStepList.getTestcasename();
		testStepId=testRunReportsDeviceCaseStepList.getTeststepid();
		testStepName=testRunReportsDeviceCaseStepList.getTeststepname();
		testStepDescription=testRunReportsDeviceCaseStepList.getTeststepdescription();
		testStepInput=testRunReportsDeviceCaseStepList.getTeststepinput();
		testStepExpectedOutput=testRunReportsDeviceCaseStepList.getTeststepexpectedoutput();
		testStepObservedOutput=testRunReportsDeviceCaseStepList.getTeststepobservedoutput();
		testStepResult=testRunReportsDeviceCaseStepList.getTeststepresult();
		executionRemarks=testRunReportsDeviceCaseStepList.getExecutionremarks();
		failureReason=testRunReportsDeviceCaseStepList.getFailurereason();
		
		if (testRunReportsDeviceCaseStepList.getTeststepstarttime() != null){
			testStepStartTime=sdf.format(testRunReportsDeviceCaseStepList.getTeststepstarttime());
		}
		if (testRunReportsDeviceCaseStepList.getTeststependtime() != null){
				testStepEndTime=sdf.format(testRunReportsDeviceCaseStepList.getTeststependtime());
		}
		
		screenShotPath=testRunReportsDeviceCaseStepList.getScreenshotpath();
		screenShotLabel=testRunReportsDeviceCaseStepList.getScreenshotlabel();
	}

	@JsonIgnore
	public TestRunReportsDeviceCaseStepList getTestRunReportsDeviceCaseStepList(){
		TestRunReportsDeviceCaseStepList testRunReportsDeviceCaseStepList = new TestRunReportsDeviceCaseStepList();
		testRunReportsDeviceCaseStepList.setTestrunno(testRunNo);
		testRunReportsDeviceCaseStepList.setTestrunlistid(testRunListId);
		testRunReportsDeviceCaseStepList.setProductid(productId);
		testRunReportsDeviceCaseStepList.setProductname(productName);
		testRunReportsDeviceCaseStepList.setProductversionlistid(productVersionListId);
		testRunReportsDeviceCaseStepList.setProductversionname(productVersionName);
		testRunReportsDeviceCaseStepList.setDeviceid(deviceId);
		testRunReportsDeviceCaseStepList.setDeviceplatformversion(devicePlatformVersion);
		testRunReportsDeviceCaseStepList.setDeviceplatformname(devicePlatformName);
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			testRunReportsDeviceCaseStepList.setTestruntriggeredtime(sdf.parse(testRunTriggeredTime));
			testRunReportsDeviceCaseStepList.setTeststepstarttime(sdf.parse(testStepStartTime));
			testRunReportsDeviceCaseStepList.setTeststependtime(sdf.parse(testStepEndTime));
		} catch (ParseException e) {
		}
		testRunReportsDeviceCaseStepList.setTestenvironmentname(testEnvironmentName);
		testRunReportsDeviceCaseStepList.setTestrunconfigurationchildid(testRunConfigurationChildId);
		testRunReportsDeviceCaseStepList.setTestrunconfigurationname(testRunconfigurationName);
		testRunReportsDeviceCaseStepList.setTestrunstatus(testRunStatus);
		testRunReportsDeviceCaseStepList.setTestsuiteid(testSuiteId);
		testRunReportsDeviceCaseStepList.setTestsuitename(testSuiteName);
		testRunReportsDeviceCaseStepList.setTestcaseid(testCaseId);
		testRunReportsDeviceCaseStepList.setTestcasename(testCaseName);
		testRunReportsDeviceCaseStepList.setTeststepid(testStepId);
		testRunReportsDeviceCaseStepList.setTeststepname(testStepName);
		testRunReportsDeviceCaseStepList.setTeststepdescription(testStepDescription);
		testRunReportsDeviceCaseStepList.setTeststepinput(testStepInput);
		testRunReportsDeviceCaseStepList.setTeststepexpectedoutput(testStepExpectedOutput);
		testRunReportsDeviceCaseStepList.setTeststepobservedoutput(testStepObservedOutput);
		testRunReportsDeviceCaseStepList.setTeststepresult(testStepResult);
		testRunReportsDeviceCaseStepList.setExecutionremarks(executionRemarks);
		testRunReportsDeviceCaseStepList.setFailurereason(failureReason);
		testRunReportsDeviceCaseStepList.setScreenshotpath(screenShotPath);
		testRunReportsDeviceCaseStepList.setScreenshotlabel(screenShotLabel);		
		return testRunReportsDeviceCaseStepList;
	}

	public Integer getTestRunNo() {
		return testRunNo;
	}

	public void setTestRunNo(Integer testRunNo) {
		this.testRunNo = testRunNo;
	}

	public Integer getTestRunListId() {
		return testRunListId;
	}

	public void setTestRunListId(Integer testRunListId) {
		this.testRunListId = testRunListId;
	}

	public Integer getProductId() {
		return productId;
	}

	public void setProductId(Integer productId) {
		this.productId = productId;
	}
	
	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public Integer getProductVersionListId() {
		return productVersionListId;
	}

	public void setProductVersionListId(Integer productVersionListId) {
		this.productVersionListId = productVersionListId;
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

	public String getDevicePlatformName() {
		return devicePlatformName;
	}

	public void setDevicePlatformName(String devicePlatformName) {
		this.devicePlatformName = devicePlatformName;
	}

	public String getDevicePlatformVersion() {
		return devicePlatformVersion;
	}

	public void setDevicePlatformVersion(String devicePlatformVersion) {
		this.devicePlatformVersion = devicePlatformVersion;
	}

	public String getTestEnvironmentName() {
		return testEnvironmentName;
	}

	public void setTestEnvironmentName(String testEnvironmentName) {
		this.testEnvironmentName = testEnvironmentName;
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

	public String getTestRunTriggeredTime() {
		return testRunTriggeredTime;
	}

	public void setTestRunTriggeredTime(String testRunTriggeredTime) {
		this.testRunTriggeredTime = testRunTriggeredTime;
	}
	
	public Integer getTestRunStatus() {
		return testRunStatus;
	}

	public void setTestRunStatus(Integer testRunStatus) {
		this.testRunStatus = testRunStatus;
	}

	public Integer getTestSuiteId() {
		return testSuiteId;
	}

	public void setTestSuiteId(Integer testSuiteId) {
		this.testSuiteId = testSuiteId;
	}

	public String getTestSuiteName() {
		return testSuiteName;
	}

	public void setTestSuiteName(String testSuiteName) {
		this.testSuiteName = testSuiteName;
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

	public Integer getTestStepId() {
		return testStepId;
	}

	public void setTestStepId(Integer testStepId) {
		this.testStepId = testStepId;
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

	public String getTestStepObservedOutput() {
		return testStepObservedOutput;
	}

	public void setTestStepObservedOutput(String testStepObservedOutput) {
		this.testStepObservedOutput = testStepObservedOutput;
	}

	public String getTestStepResult() {
		return testStepResult;
	}

	public void setTestStepResult(String testStepResult) {
		this.testStepResult = testStepResult;
	}
	
	public String getExecutionRemarks() {
		return executionRemarks;
	}

	public void setExecutionRemarks(String executionRemarks) {
		this.executionRemarks = executionRemarks;
	}

	public String getFailureReason() {
		return failureReason;
	}

	public void setFailureReason(String failureReason) {
		this.failureReason = failureReason;
	}

	public String getTestStepStartTime() {
		return testStepStartTime;
	}

	public void setTestStepStartTime(String testStepStartTime) {
		this.testStepStartTime = testStepStartTime;
	}

	public String getTestStepEndTime() {
		return testStepEndTime;
	}

	public void setTestStepEndTime(String testStepEndTime) {
		this.testStepEndTime = testStepEndTime;
	}

	public String getScreenShotPath() {
		return screenShotPath;
	}
	
	public void setScreenShotPath(String screenShotPath) {
		this.screenShotPath = screenShotPath;
	}
	
	public String getScreenShotLabel() {
		return screenShotLabel;
	}
	public void setScreenShotLabel(String screenShotLabel) {
		this.screenShotLabel = screenShotLabel;
	}
}
