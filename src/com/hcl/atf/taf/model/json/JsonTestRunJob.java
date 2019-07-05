package com.hcl.atf.taf.model.json;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.hcl.atf.taf.constants.IDPAConstants;
import com.hcl.atf.taf.model.GenericDevices;
import com.hcl.atf.taf.model.TestRunJob;
import com.hcl.atf.taf.model.TestRunPlan;
import com.hcl.atf.taf.model.TestSuiteList;
import com.hcl.atf.taf.model.TestToolMaster;
import com.hcl.atf.taf.model.WorkPackage;

public class JsonTestRunJob {
	@JsonProperty
	private Integer testRunJobId;
	@JsonProperty
	private Integer testRunPlanId;
	@JsonProperty
	private String testRunTriggeredTime;
	@JsonProperty
	private Integer testRunStatus;
	@JsonProperty
	private String testRunFailureMessage;
	@JsonProperty
	private Integer genericDeviceId;
	@JsonProperty
	private Integer workPackageId;
	@JsonProperty
	private String workPackageName;
	@JsonProperty
	private Integer testSuiteId;
	
	@JsonProperty
	private String testRunStartTime;
	@JsonProperty
	private String testRunEndTime;
	@JsonProperty
	private String testRunEvidenceStatus;
	@JsonProperty
	private Integer averageTestRunExecutionTime;
	@JsonProperty
	private Integer hostId;
	
	@JsonProperty
	private Integer environmentCombinationId;
	
	@JsonProperty
	private String UDID;
	@JsonProperty
	private String platformType;
	@JsonProperty
	private String hostName; //Changes for Web App support
	@JsonProperty
	private String hostIpAddress; //Changes for Web App support
	@JsonProperty
	private String hostStatus; //Changes for Web App support	
	@JsonProperty	
	private String hostPlatform;
	@JsonProperty
	private String environmentCombinationName;
	@JsonProperty
	private String status;
	@JsonProperty
	private Integer testToolId;
	@JsonProperty
	private String testToolName;
	@JsonProperty
	private String testScriptType;
	@JsonProperty
	private Integer combinedResultsReportingJobId;

	
	public Integer getTestRunJobId() {
		return testRunJobId;
	}
	public void setTestRunJobId(Integer testRunJobId) {
		this.testRunJobId = testRunJobId;
	}
	public Integer getTestRunPlanId() {
		return testRunPlanId;
	}
	public void setTestRunPlanId(Integer testRunPlanId) {
		this.testRunPlanId = testRunPlanId;
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
	public String getTestRunFailureMessage() {
		return testRunFailureMessage;
	}
	public void setTestRunFailureMessage(String testRunFailureMessage) {
		this.testRunFailureMessage = testRunFailureMessage;
	}
	public Integer getGenericDeviceId() {
		return genericDeviceId;
	}
	public void setGenericDeviceId(Integer genericDeviceId) {
		this.genericDeviceId = genericDeviceId;
	}

	public Integer getWorkPackageId() {
		return workPackageId;
	}
	public void setWorkPackageId(Integer workPackageId) {
		this.workPackageId = workPackageId;
	}
	public String getWorkPackageName() {
		return workPackageName;
	}
	public void setWorkPackageName(String workPackageName) {
		this.workPackageName = workPackageName;
	}
	public Integer getTestSuiteId() {
		return testSuiteId;
	}
	public void setTestSuiteId(Integer testSuiteId) {
		this.testSuiteId = testSuiteId;
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
	public String getTestRunEvidenceStatus() {
		return testRunEvidenceStatus;
	}
	public void setTestRunEvidenceStatus(String testRunEvidenceStatus) {
		this.testRunEvidenceStatus = testRunEvidenceStatus;
	}

	public JsonTestRunJob(){
		
	}
	
	public JsonTestRunJob(TestRunJob testRunJob){
		testRunJobId = testRunJob.getTestRunJobId();
		if(testRunJob.getTestRunPlan() != null){
			this.testRunPlanId = testRunJob.getTestRunPlan().getTestRunPlanId();
		}		
		Integer testRunJobStatus=testRunJob.getTestRunStatus();
		if(testRunJobStatus.equals(4)||testRunJobStatus==4)
			this.status = IDPAConstants.JOB_STATUS_QUEUED;
		else if(testRunJobStatus.equals(5)||testRunJobStatus==5)
			this.status = IDPAConstants.JOB_STATUS_COMPLETED;
		else if(testRunJobStatus.equals(6)||testRunJobStatus==6)
			this.status = IDPAConstants.JOB_STATUS_ABORTED;
		else if(testRunJobStatus.equals(3)||testRunJobStatus==3)
			this.status = IDPAConstants.JOB_STATUS_EXECUTING;
		
		this.testRunStatus=testRunJobStatus;
		this.testRunFailureMessage=testRunJob.getTestRunFailureMessage();	
		
		if(testRunJob.getGenericDevices() != null){
			this.genericDeviceId = testRunJob.getGenericDevices().getGenericsDevicesId();
			this.UDID=testRunJob.getGenericDevices().getUDID();
			if(testRunJob.getGenericDevices().getPlatformType()!=null)
			this.platformType=testRunJob.getGenericDevices().getPlatformType().getName();
		}
		
		if(testRunJob.getEnvironmentCombination() != null){
			this.environmentCombinationId = testRunJob.getEnvironmentCombination().getEnvironment_combination_id();
			this.environmentCombinationName=testRunJob.getEnvironmentCombination().getEnvironmentCombinationName();
		}
		if(testRunJob.getWorkPackage() != null){
			this.workPackageId = testRunJob.getWorkPackage().getWorkPackageId();
			this.workPackageName = testRunJob.getWorkPackage().getName();
		}
		
		if(testRunJob.getTestSuite() != null){
			this.testSuiteId =	testRunJob.getTestSuite().getTestSuiteId();
		}
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		if (testRunJob.getTestRunTriggeredTime() != null)
			testRunTriggeredTime= sdf.format(testRunJob.getTestRunTriggeredTime());
		if (testRunJob.getTestRunStartTime() != null)
			testRunStartTime=sdf.format(testRunJob.getTestRunStartTime());
		if (testRunJob.getTestRunEndTime() != null)
			testRunEndTime=sdf.format(testRunJob.getTestRunEndTime());
		this.testRunEvidenceStatus = testRunJob.getTestRunEvidenceStatus();		
		if(testRunJob.getHostList()!=null){
			this.hostId=testRunJob.getHostList().getHostId();
			this.hostName=testRunJob.getHostList().getHostName();
			this.hostIpAddress=testRunJob.getHostList().getHostIpAddress();
		}
		if(testRunJob.getTestToolMaster() != null){
			this.testToolId = testRunJob.getTestToolMaster().getTestToolId();
			this.testToolName = testRunJob.getTestToolMaster().getTestToolName();
		}
		if(testRunJob.getScriptTypeMaster() != null){
			this.testScriptType = testRunJob.getScriptTypeMaster().getScriptType();
		}
		if(testRunJob.getWorkPackage() != null){
			this.combinedResultsReportingJobId = testRunJob.getWorkPackage().getCombinedResultsReportingJob().getTestRunJobId();
		}
	}
	
	@JsonIgnore
	public TestRunJob getTestRunJob(){
		TestRunJob testRunJob = new TestRunJob();
		testRunJob.setTestRunJobId(testRunJobId);
		if(testRunPlanId != null){
			TestRunPlan testRunPlan = new TestRunPlan();
			testRunPlan.setTestRunPlanId(testRunPlanId);
			testRunJob.setTestRunPlan(testRunPlan);
		}
		testRunJob.setTestRunStatus(this.testRunStatus);
		testRunJob.setTestRunFailureMessage(testRunFailureMessage);
		if(genericDeviceId != null){
			GenericDevices genericDevices = new GenericDevices();
			genericDevices.setGenericsDevicesId(genericDeviceId);
			testRunJob.setGenericDevices(genericDevices);
		}
		if(workPackageId != null){
			WorkPackage workPackage = new WorkPackage();
			workPackage.setWorkPackageId(workPackageId);			
			testRunJob.setWorkPackage(workPackage);
		}
		
		if(workPackageName != null){
			WorkPackage workPackage = new WorkPackage();
			workPackage.setName(workPackageName);			
			testRunJob.setWorkPackage(workPackage);
		}
		
		if(testSuiteId != null){
			TestSuiteList testSuite = new TestSuiteList();
			testSuite.setTestSuiteId(testSuiteId);			
			testRunJob.setTestSuite(testSuite);
		}
			
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		if (testRunTriggeredTime != null) {
			try {
				testRunJob.setTestRunTriggeredTime(sdf.parse(testRunTriggeredTime));				
			} catch (ParseException e) {				
				e.printStackTrace();
			}
		}
		if (testRunStartTime != null) {
			try {
				testRunJob.setTestRunStartTime(sdf.parse(testRunStartTime));				
			} catch (ParseException e) {				
				e.printStackTrace();
			}
		}
		if (testRunEndTime != null) {
			try {
				testRunJob.setTestRunEndTime(sdf.parse(testRunEndTime));				
			} catch (ParseException e) {				
				e.printStackTrace();
			}
		}
		if(testToolId != null){
			TestToolMaster testToolMaster = new TestToolMaster();
			testToolMaster.setTestToolId(testToolId);
			testToolMaster.setTestToolName(testToolName);
			testRunJob.setTestToolMaster(testToolMaster);
		}
		return testRunJob;
	}
	public Integer getAverageTestRunExecutionTime() {
		return averageTestRunExecutionTime;
	}
	public void setAverageTestRunExecutionTime(Integer averageTestRunExecutionTime) {
		this.averageTestRunExecutionTime = averageTestRunExecutionTime;
	}
	public Integer getHostId() {
		return hostId;
	}
	public void setHostId(Integer hostId) {
		this.hostId = hostId;
	}
	public Integer getEnvironmentCombinationId() {
		return environmentCombinationId;
	}
	public void setEnvironmentCombinationId(Integer environmentCombinationId) {
		this.environmentCombinationId = environmentCombinationId;
	}
	public String getUDID() {
		return UDID;
	}
	public void setUDID(String uDID) {
		UDID = uDID;
	}
	public String getPlatformType() {
		return platformType;
	}
	public void setPlatformType(String platformType) {
		this.platformType = platformType;
	}
	public String getHostName() {
		return hostName;
	}
	public void setHostName(String hostName) {
		this.hostName = hostName;
	}
	public String getHostIpAddress() {
		return hostIpAddress;
	}
	public void setHostIpAddress(String hostIpAddress) {
		this.hostIpAddress = hostIpAddress;
	}
	public String getHostStatus() {
		return hostStatus;
	}
	public void setHostStatus(String hostStatus) {
		this.hostStatus = hostStatus;
	}
	public String getHostPlatform() {
		return hostPlatform;
	}
	public void setHostPlatform(String hostPlatform) {
		this.hostPlatform = hostPlatform;
	}
	public String getEnvironmentCombinationName() {
		return environmentCombinationName;
	}
	public void setEnvironmentCombinationName(String environmentCombinationName) {
		this.environmentCombinationName = environmentCombinationName;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public Integer getTestToolId() {
		return testToolId;
	}
	public void setTestToolId(Integer testToolId) {
		this.testToolId = testToolId;
	}
	public String getTestToolName() {
		return testToolName;
	}
	public void setTestToolName(String testToolName) {
		this.testToolName = testToolName;
	}
	public String getTestScriptType() {
		return testScriptType;
	}
	public void setTestScriptType(String testScriptType) {
		this.testScriptType = testScriptType;
	}	
	public Integer getCombinedResultsReportingJobId() {
		return combinedResultsReportingJobId;
	}
	public void setCombinedResultsReportingJobId(Integer combinedResultsReportingJobId) {
		this.combinedResultsReportingJobId = combinedResultsReportingJobId;
	}

}
