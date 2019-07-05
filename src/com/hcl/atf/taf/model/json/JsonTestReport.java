package com.hcl.atf.taf.model.json;


import java.text.SimpleDateFormat;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.hcl.atf.taf.model.TestExecutionResult;

public class JsonTestReport implements java.io.Serializable {

	@JsonProperty
	private Integer testExecutionResultId;
	@JsonProperty
	private String testResultStatus;
	@JsonProperty
	private Integer testRunListId;	
	@JsonProperty
	private Integer deviceListId;
	@JsonProperty
	private String deviceId;	
	@JsonProperty 
	private Integer devicePlatformVersionListId;
	@JsonProperty
	private String devicePlatformVersion;
	@JsonProperty
	private String devicePlatformName;
	@JsonProperty
	private String testRunTriggeredTime;	
	@JsonProperty
	private int buildNo;	
	@JsonProperty
	private int runNo;
	
	
	@JsonProperty
	private Integer testSuiteId;
	@JsonProperty
	private Integer productId;
	@JsonProperty
	private String productName;
	
	@JsonProperty
	private Integer productVersionListId;
	@JsonProperty
	private String productVersionListName;
	@JsonProperty
	private Integer targetDevicePlatformVersionListId;	
	@JsonProperty
	private String targetDevicePlatformName;	
	@JsonProperty
	private String targetDevicePlatformVersion;

	
	
	
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
	


	public JsonTestReport() {
	}
	public JsonTestReport(TestExecutionResult testExecutionResult) {
		
		testExecutionResultId=testExecutionResult.getTestExecutionResultId();
		if(testExecutionResult.getTestResultStatusMaster()!=null)
			testResultStatus=testExecutionResult.getTestResultStatusMaster().getTestResultStatus();
		if(testExecutionResult.getTestRunList()!=null){
			testRunListId=testExecutionResult.getTestRunList().getTestRunListId();
			if(testExecutionResult.getTestRunList().getDeviceList()!=null){
				deviceListId = testExecutionResult.getTestRunList().getDeviceList().getDeviceListId();
				deviceId = testExecutionResult.getTestRunList().getDeviceList().getDeviceId();
				if(testExecutionResult.getTestRunList().getDeviceList().getDevicePlatformVersionListMaster()!=null){
					devicePlatformVersionListId = testExecutionResult.getTestRunList().getDeviceList().getDevicePlatformVersionListMaster().getDevicePlatformVersionListId();
					devicePlatformVersion = testExecutionResult.getTestRunList().getDeviceList().getDevicePlatformVersionListMaster().getDevicePlatformVersion();
					if(testExecutionResult.getTestRunList().getDeviceList().getDevicePlatformVersionListMaster().getDevicePlatformMaster()!=null)
						devicePlatformName = testExecutionResult.getTestRunList().getDeviceList().getDevicePlatformVersionListMaster().getDevicePlatformMaster().getDevicePlatformName();
				}
	
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				testRunTriggeredTime=sdf.format(testExecutionResult.getTestRunList().getTestRunTriggeredTime());
				buildNo = testExecutionResult.getTestRunList().getBuildNo();
				runNo = testExecutionResult.getTestRunList().getRunNo();
				
			}
			
		}		
		if(testExecutionResult.getTestSuiteList()!=null){
			testSuiteId=testExecutionResult.getTestSuiteList().getTestSuiteId();
			if(testExecutionResult.getTestSuiteList().getProductVersionListMaster()!=null){
				if(testExecutionResult.getTestSuiteList().getProductVersionListMaster().getProductMaster()!=null){
					productId = testExecutionResult.getTestSuiteList().getProductVersionListMaster().getProductMaster().getProductId();
					productName = testExecutionResult.getTestSuiteList().getProductVersionListMaster().getProductMaster().getProductName();
				}
				productVersionListId = testExecutionResult.getTestSuiteList().getProductVersionListMaster().getProductVersionListId();
				productVersionListName = testExecutionResult.getTestSuiteList().getProductVersionListMaster().getProductVersionName();
			}
		}
		testStep=testExecutionResult.getTestStep();
		testStepDescription=testExecutionResult.getTestStepDescription();
		testStepInput=testExecutionResult.getTestStepInput();
		testStepExpectedOutput=testExecutionResult.getTestStepExpectedOutput();
		testStepObservedOutput=testExecutionResult.getTestStepObservedOutput();	
		
	}
	public Integer getTestExecutionResultId() {
		return testExecutionResultId;
	}
	public void setTestExecutionResultId(Integer testExecutionResultId) {
		this.testExecutionResultId = testExecutionResultId;
	}
	public String getTestResultStatus() {
		return testResultStatus;
	}
	public void setTestResultStatus(String testResultStatus) {
		this.testResultStatus = testResultStatus;
	}
	public Integer getTestRunListId() {
		return testRunListId;
	}
	public void setTestRunListId(Integer testRunListId) {
		this.testRunListId = testRunListId;
	}
	public Integer getDeviceListId() {
		return deviceListId;
	}
	public void setDeviceListId(Integer deviceListId) {
		this.deviceListId = deviceListId;
	}
	public String getDeviceId() {
		return deviceId;
	}
	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}
	public String getTestRunTriggeredTime() {
		return testRunTriggeredTime;
	}
	public void setTestRunTriggeredTime(String testRunTriggeredTime) {
		this.testRunTriggeredTime = testRunTriggeredTime;
	}
	public int getBuildNo() {
		return buildNo;
	}
	public void setBuildNo(int buildNo) {
		this.buildNo = buildNo;
	}
	public int getRunNo() {
		return runNo;
	}
	public void setRunNo(int runNo) {
		this.runNo = runNo;
	}
	public Integer getTestSuiteId() {
		return testSuiteId;
	}
	public void setTestSuiteId(Integer testSuiteId) {
		this.testSuiteId = testSuiteId;
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
	public String getProductVersionListName() {
		return productVersionListName;
	}
	public void setProductVersionListName(String productVersionListName) {
		this.productVersionListName = productVersionListName;
	}
	public Integer getDevicePlatformVersionListId() {
		return devicePlatformVersionListId;
	}
	public void setDevicePlatformVersionListId(Integer devicePlatformVersionListId) {
		this.devicePlatformVersionListId = devicePlatformVersionListId;
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
}