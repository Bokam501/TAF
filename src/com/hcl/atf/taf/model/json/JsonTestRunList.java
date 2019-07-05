package com.hcl.atf.taf.model.json;



import java.text.ParseException;
import java.text.SimpleDateFormat;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.hcl.atf.taf.model.DeviceList;
import com.hcl.atf.taf.model.TestResultStatusMaster;
import com.hcl.atf.taf.model.TestRunConfigurationChild;
import com.hcl.atf.taf.model.TestRunList;
import com.hcl.atf.taf.model.TrccExecutionPlan;


public class JsonTestRunList implements java.io.Serializable {

	@JsonProperty
	private Integer testRunListId;
	@JsonProperty
	private String testResultStatus;
	@JsonProperty
	private Integer deviceListId;
	@JsonProperty
	private String deviceId;	
	@JsonProperty	
	private String devicePlatformVersion;
	//ref to DevicePlatformVersionListMaster.devicePlatformName
	@JsonProperty	
	private String devicePlatformName;	
	
	@JsonProperty
	private Integer testRunConfigurationChildId;	
	@JsonProperty
	private String productVersionName;
	@JsonProperty
	private String productName;
	@JsonProperty
	private String testRunConfigurationName;
	@JsonProperty
	private String testRunTriggeredTime;
	@JsonProperty
	private String testRunFailureMessage;	
	@JsonProperty
	private int buildNo;	
	@JsonProperty
	private int runNo;
	@JsonProperty
	private String testRunStartTime;
	@JsonProperty
	private String testRunEndTime;
	@JsonIgnore
	private String testEnvironmentName;
	@JsonProperty
	private Integer averageTestRunExecutionTime;
	@JsonIgnore
	private Integer testRunExecutionTime;
	@JsonProperty
	private String testRunEvidenceStatus;
	@JsonProperty		
	private Integer trccExecutionPlanId;
	@JsonProperty
	private Integer combinedResultsReportingJobId;

	
	public JsonTestRunList() {
	}
	
	public JsonTestRunList(TestRunList testRunList) {
		testRunListId = testRunList.getTestRunListId();
		if(testRunList.getTestResultStatusMaster()!=null)
			testResultStatus = testRunList.getTestResultStatusMaster().getTestResultStatus();
		if(testRunList.getDeviceList()!=null){
			deviceListId = testRunList.getDeviceList().getDeviceListId();
			deviceId = testRunList.getDeviceList().getDeviceId();
			if(testRunList.getDeviceList().getDevicePlatformVersionListMaster()!=null){
				devicePlatformVersion = testRunList.getDeviceList().getDevicePlatformVersionListMaster().getDevicePlatformVersion();
				if(testRunList.getDeviceList().getDevicePlatformVersionListMaster().getDeviceLists()!=null)
				devicePlatformName = testRunList.getDeviceList().getDevicePlatformVersionListMaster().getDevicePlatformMaster().getDevicePlatformName();
			}
		}
		if(testRunList.getTestRunConfigurationChild()!=null){
			testRunConfigurationChildId = testRunList.getTestRunConfigurationChild().getTestRunConfigurationChildId();
			testRunConfigurationName=testRunList.getTestRunConfigurationChild().getTestRunConfigurationName();
			if(testRunList.getTestRunConfigurationChild().getProductVersionListMaster()!=null){
				
				productVersionName = testRunList.getTestRunConfigurationChild().getProductVersionListMaster().getProductVersionName();
				productName=testRunList.getTestRunConfigurationChild().getProductVersionListMaster().getProductMaster().getProductName();
			}
		}
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		if (testRunList.getTestRunTriggeredTime() != null)
			testRunTriggeredTime=sdf.format(testRunList.getTestRunTriggeredTime());
		if (testRunList.getTestRunStartTime() != null)
			testRunStartTime=sdf.format(testRunList.getTestRunStartTime());
		if (testRunList.getTestRunEndTime() != null)
			testRunEndTime=sdf.format(testRunList.getTestRunEndTime());
		if (testRunList.getTestRunStartTime() != null && testRunList.getTestRunEndTime() != null) {
			
			testRunExecutionTime = (int)(testRunList.getTestRunEndTime().getTime() - testRunList.getTestRunStartTime().getTime()) / 1000;
		}
		testRunFailureMessage=testRunList.getTestRunFailureMessage();	
		buildNo=testRunList.getBuildNo();
		runNo=testRunList.getRunNo();
		if (testRunList.getTestEnvironmentDevices() != null)
			testEnvironmentName = testRunList.getTestEnvironmentDevices().getName();
		testRunEvidenceStatus = testRunList.getTestRunEvidenceStatus();
		
		if (testRunList.getTrccExecutionPlan() != null)
			trccExecutionPlanId = testRunList.getTrccExecutionPlan().getTrccExecutionPlanId();
	}
	
	public Integer getTestRunListId() {
		return testRunListId;
	}
	public void setTestRunListId(Integer testRunListId) {
		this.testRunListId = testRunListId;
	}
	public String getTestResultStatus() {
		return testResultStatus;
	}
	public void setTestResultStatus(String testResultStatus) {
		this.testResultStatus = testResultStatus;
	}
	public Integer getDeviceListId() {
		return deviceListId;
	}
	public void setDeviceListId(Integer deviceListId) {
		this.deviceListId = deviceListId;
	}
	public Integer getTestRunConfigurationChildId() {
		return testRunConfigurationChildId;
	}
	public void setTestRunConfigurationChildId(Integer testRunConfigurationChildId) {
		this.testRunConfigurationChildId = testRunConfigurationChildId;
	}
	public String getTestRunTriggeredTime() {
		return testRunTriggeredTime;
	}
	public void setTestRunTriggeredTime(String testRunTriggeredTime) {
		this.testRunTriggeredTime = testRunTriggeredTime;
	}
	public String getTestRunFailureMessage() {
		return testRunFailureMessage;
	}
	public void setTestRunFailureMessage(String testRunFailureMessage) {
		this.testRunFailureMessage = testRunFailureMessage;
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
	public String getProductVersionName() {
		return productVersionName;
	}
	public void setProductVersionName(String productVersionName) {
		this.productVersionName = productVersionName;
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
	public Integer getAverageTestRunExecutionTime() {
		return averageTestRunExecutionTime;
	}
	public void setAverageTestRunExecutionTime(Integer averageTestRunExecutionTime) {
		this.averageTestRunExecutionTime = averageTestRunExecutionTime;
	}
	public Integer getTestRunExecutionTime() {
		return testRunExecutionTime;
	}
	public void setTestRunExecutionTime(Integer testRunExecutionTime) {
		this.testRunExecutionTime = testRunExecutionTime;
	}

	@JsonIgnore
	public TestRunList getTestRunList(){
		TestRunList testRunList = new TestRunList();
		testRunList.setTestRunListId(testRunListId);
		
		TestResultStatusMaster testResultStatusMaster = new TestResultStatusMaster();
		testResultStatusMaster.setTestResultStatus(testResultStatus);
		testRunList.setTestResultStatusMaster(testResultStatusMaster);
		
		DeviceList deviceList = new DeviceList();
		deviceList.setDeviceListId(deviceListId);
		testRunList.setDeviceList(deviceList);
		
		
		TestRunConfigurationChild testRunConfigurationChild = new TestRunConfigurationChild();
		testRunConfigurationChild.setTestRunConfigurationChildId(testRunConfigurationChildId);
		testRunConfigurationChild.setTestRunConfigurationName(testRunConfigurationName);
		testRunList.setTestRunConfigurationChild(testRunConfigurationChild);
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		if (testRunTriggeredTime != null) {
			try {
				testRunList.setTestRunTriggeredTime(sdf.parse(testRunTriggeredTime));
			} catch (ParseException e) {				
				e.printStackTrace();
			}
		}
		
		testRunList.setTestRunFailureMessage(testRunFailureMessage);
		testRunList.setBuildNo(buildNo);
		testRunList.setRunNo(runNo);
		if (testRunStartTime != null) {
			try {
				testRunList.setTestRunStartTime(sdf.parse(testRunStartTime));
			} catch (ParseException e) { e.printStackTrace();}
		}
		if (testRunEndTime != null) {
			try {
				testRunList.setTestRunEndTime(sdf.parse(testRunEndTime));
			} catch (ParseException e) { e.printStackTrace();}
		}
		testRunList.setTestRunEvidenceStatus(testRunEvidenceStatus);
		
		if (trccExecutionPlanId != null) {
			
			TrccExecutionPlan trccExecutionPlan = new TrccExecutionPlan();
			trccExecutionPlan.setTrccExecutionPlanId(trccExecutionPlanId);
			testRunList.setTrccExecutionPlan(trccExecutionPlan);
		}
		else {
			
			testRunList.setTrccExecutionPlan(null);
		}
			
		
		return testRunList;
	}
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public String getTestRunConfigurationName() {
		return testRunConfigurationName;
	}
	public void setTestRunConfigurationName(String testRunConfigurationName) {
		this.testRunConfigurationName = testRunConfigurationName;
	}
	public String getTestRunEvidenceStatus() {
		return this.testRunEvidenceStatus;
	}
	public void setTestRunEvidenceStatus(String testRunEvidenceStatus) {
		this.testRunEvidenceStatus = testRunEvidenceStatus;
	}
	public Integer getTrccExecutionPlanId() {
		return this.trccExecutionPlanId;
	}
	public void setTrccExecutionPlanId(Integer trccExecutionPlanId) {
		this.trccExecutionPlanId = trccExecutionPlanId;
	}
	public Integer getCombinedResultsReportingJobId() {
		return combinedResultsReportingJobId;
	}
	public void setCombinedResultsReportingJobId(Integer combinedResultsReportingJobId) {
		this.combinedResultsReportingJobId = combinedResultsReportingJobId;
	}

}
