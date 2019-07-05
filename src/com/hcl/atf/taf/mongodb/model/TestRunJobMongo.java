package com.hcl.atf.taf.mongodb.model;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.hcl.atf.taf.constants.IDPAConstants;
import com.hcl.atf.taf.model.TestRunJob;

@Document(collection = "testrunJob")
public class TestRunJobMongo {
	@Id
	private String id;
	private Integer testRunJobId;
	
	private Integer workPackageId;
	private String workPackageName;
	private Integer productId;
	private String productName;
	private Integer testfactoryId;
	private String testfactoryName;
	private Integer testCentersId;
	private String testCentersName;
	
	private Integer buildId;
	private String buildName;
	private Integer versionId;
	private String versionName;
	
	private Integer testRunPlanId;
	private String testRunPlanName;
	private Date testRunTriggeredTime;
	private String testRunStatus;
	private String testRunFailureMessage;
	private String genericDevices;
	private String workPackage;
	private String testSuite;
	private String hostListName;
	private Date testRunStartTime;
	private Date testRunEndTime;
	private String testRunEvidenceStatus;	
	private String environmentCombinationName;
	private String runConfigurationName;
	private String executionType;
	private String lifeCyclePhase;
	private String result;
	
	
	private Date createDate;
	private Date modifiedDate;
	
	
	
	
	
	public TestRunJobMongo(){
		
	}
	
	
	
public TestRunJobMongo(TestRunJob testRunJob) {

	this.id= testRunJob.getTestRunJobId()+"";
	this.testRunJobId =testRunJob.getTestRunJobId();
	
	this.workPackageId = testRunJob.getWorkPackage().getWorkPackageId();
	this.workPackageName = testRunJob.getWorkPackage().getName();
	
	
	
	if(testRunJob.getWorkPackage().getProductBuild().getProductVersion().getProductMaster()!=null){
		this.productId = testRunJob.getWorkPackage().getProductBuild().getProductVersion().getProductMaster().getProductId();
		this.productName = testRunJob.getWorkPackage().getProductBuild().getProductVersion().getProductMaster().getProductName();
	}
	
	if(testRunJob.getWorkPackage().getProductBuild().getProductVersion().getProductMaster().getTestFactory()!=null){
		this.testfactoryId = testRunJob.getWorkPackage().getProductBuild().getProductVersion().getProductMaster().getTestFactory().getTestFactoryId();
		if(testRunJob.getWorkPackage().getProductBuild().getProductVersion().getProductMaster().getTestFactory().getTestFactoryLab()!=null){
			this.testCentersId=testRunJob.getWorkPackage().getProductBuild().getProductVersion().getProductMaster().getTestFactory().getTestFactoryLab().getTestFactoryLabId();
			this.testCentersName=testRunJob.getWorkPackage().getProductBuild().getProductVersion().getProductMaster().getTestFactory().getTestFactoryLab().getTestFactoryLabName();
			
		}
		
	}
	
	this.buildId = testRunJob.getWorkPackage().getProductBuild().getProductBuildId();
	this.buildName = testRunJob.getWorkPackage().getProductBuild().getBuildname();
	
	this.versionId = testRunJob.getWorkPackage().getProductBuild().getProductVersion().getProductVersionListId();
	this.versionName = testRunJob.getWorkPackage().getProductBuild().getProductVersion().getProductVersionName();
	
	if(testRunJob.getTestRunPlan()!=null){
		this.testRunPlanName = testRunJob.getTestRunPlan().getTestRunPlanName();
		this.testRunPlanId = testRunJob.getTestRunPlan().getTestRunPlanId();
		
	}
	this.testRunTriggeredTime = testRunJob.getTestRunTriggeredTime();
	
	if(testRunJob.getTestRunStatus()==IDPAConstants.JOB_NEW){
		this.testRunStatus="New";
	}else if(testRunJob.getTestRunStatus()==IDPAConstants.JOB_EXECUTING){
		this.testRunStatus="Executing";
	}else if(testRunJob.getTestRunStatus()==IDPAConstants.JOB_QUEUED){
		this.testRunStatus="Queued";
	}else if(testRunJob.getTestRunStatus()==IDPAConstants.JOB_COMPLETED){
		this.testRunStatus="Completed";
	}else if(testRunJob.getTestRunStatus()==IDPAConstants.JOB_ABORT){
		this.testRunStatus="Abort";
	}else if(testRunJob.getTestRunStatus()==IDPAConstants.JOB_ABORTED){
		this.testRunStatus="Aborted";
	}
	
	
	
	if (testRunJob.getWorkPackage().getTestRunPlan() != null)
		this.executionType = testRunJob.getWorkPackage().getTestRunPlan().getExecutionType().getName();
	else 
		this.executionType = "Manual";
	
	if (testRunJob.getWorkPackage().getLifeCyclePhase() != null)
		this.lifeCyclePhase = testRunJob.getWorkPackage().getLifeCyclePhase().getName();
	


	
	
	this.testRunFailureMessage = testRunJob.getTestRunFailureMessage();
	if(testRunJob.getGenericDevices()!=null){
		this.genericDevices = testRunJob.getGenericDevices().getName();
	}
	this.workPackage = testRunJob.getWorkPackage().getName();
	if(testRunJob.getTestSuite()!=null){
		this.testSuite = testRunJob.getTestSuite().getTestSuiteName();
	}
	
	if(testRunJob.getHostList()!=null){
		this.hostListName = testRunJob.getHostList().getHostName();
	}
	
	if(testRunJob.getTestRunStartTime()!=null){
		this.testRunStartTime = testRunJob.getTestRunStartTime();
	}
	if(testRunJob.getTestRunEndTime()!=null){
		this.testRunEndTime = testRunJob.getTestRunEndTime();
	}
	
	if(testRunJob.getTestRunEvidenceStatus()!=null){
		this.testRunEvidenceStatus=testRunJob.getTestRunEvidenceStatus();
	}
	if(testRunJob.getEnvironmentCombination()!=null){
		this.environmentCombinationName=testRunJob.getEnvironmentCombination().getEnvironmentCombinationName();
	}
	if(testRunJob.getRunConfiguration()!=null){
		this.runConfigurationName=testRunJob.getRunConfiguration().getRunconfigName();
	}
	
	
	if(testRunJob.getTestRunStartTime()!=null){
		this.createDate = testRunJob.getTestRunStartTime();
	}
	if(testRunJob.getTestRunEndTime()!=null){
		this.createDate = testRunJob.getTestRunEndTime();
	}
	
	
}


public String getId() {
	return id;
}



public void setId(String _id) {
	this.id = _id;
}



public Integer getTestRunJobId() {
	return testRunJobId;
}



public void setTestRunJobId(Integer testRunJobId) {
	this.testRunJobId = testRunJobId;
}



public String getTestRunPlanName() {
	return testRunPlanName;
}



public void setTestRunPlanName(String testRunPlanName) {
	this.testRunPlanName = testRunPlanName;
}



public Date getTestRunTriggeredTime() {
	return testRunTriggeredTime;
}



public void setTestRunTriggeredTime(Date testRunTriggeredTime) {
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



public String getGenericDevices() {
	return genericDevices;
}



public void setGenericDevices(String genericDevices) {
	this.genericDevices = genericDevices;
}



public String getWorkPackage() {
	return workPackage;
}



public void setWorkPackage(String workPackage) {
	this.workPackage = workPackage;
}



public String getTestSuite() {
	return testSuite;
}



public void setTestSuite(String testSuite) {
	this.testSuite = testSuite;
}



public String getHostListName() {
	return hostListName;
}



public void setHostListName(String hostListName) {
	this.hostListName = hostListName;
}



public Date getTestRunStartTime() {
	return testRunStartTime;
}



public void setTestRunStartTime(Date testRunStartTime) {
	this.testRunStartTime = testRunStartTime;
}



public Date getTestRunEndTime() {
	return testRunEndTime;
}



public void setTestRunEndTime(Date testRunEndTime) {
	this.testRunEndTime = testRunEndTime;
}



public String getTestRunEvidenceStatus() {
	return testRunEvidenceStatus;
}



public void setTestRunEvidenceStatus(String testRunEvidenceStatus) {
	this.testRunEvidenceStatus = testRunEvidenceStatus;
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



public Integer getTestfactoryId() {
	return testfactoryId;
}



public void setTestfactoryId(Integer testfactoryId) {
	this.testfactoryId = testfactoryId;
}



public String getTestfactoryName() {
	return testfactoryName;
}



public void setTestfactoryName(String testfactoryName) {
	this.testfactoryName = testfactoryName;
}



public Integer getBuildId() {
	return buildId;
}



public void setBuildId(Integer buildId) {
	this.buildId = buildId;
}



public String getBuildName() {
	return buildName;
}



public void setBuildName(String buildName) {
	this.buildName = buildName;
}



public Integer getVersionId() {
	return versionId;
}



public void setVersionId(Integer versionId) {
	this.versionId = versionId;
}



public String getVersionName() {
	return versionName;
}



public void setVersionName(String versionName) {
	this.versionName = versionName;
}



public Date getCreateDate() {
	return createDate;
}



public void setCreateDate(Date createDate) {
	this.createDate = createDate;
}



public Date getModifiedDate() {
	return modifiedDate;
}



public void setModifiedDate(Date modifiedDate) {
	this.modifiedDate = modifiedDate;
}



public String getEnvironmentCombinationName() {
	return environmentCombinationName;
}



public void setEnvironmentCombinationName(String environmentCombinationName) {
	this.environmentCombinationName = environmentCombinationName;
}



public String getRunConfigurationName() {
	return runConfigurationName;
}



public void setRunConfigurationName(String runConfigurationName) {
	this.runConfigurationName = runConfigurationName;
}



public Integer getTestRunPlanId() {
	return testRunPlanId;
}



public void setTestRunPlanId(Integer testRunPlanId) {
	this.testRunPlanId = testRunPlanId;
}



public String getExecutionType() {
	return executionType;
}



public void setExecutionType(String executionType) {
	this.executionType = executionType;
}



public String getLifeCyclePhase() {
	return lifeCyclePhase;
}



public void setLifeCyclePhase(String lifeCyclePhase) {
	this.lifeCyclePhase = lifeCyclePhase;
}



public String getResult() {
	return result;
}



public void setResult(String result) {
	this.result = result;
}



Integer getTestCentersId() {
	return testCentersId;
}



void setTestCentersId(Integer testCentersId) {
	this.testCentersId = testCentersId;
}



String getTestCentersName() {
	return testCentersName;
}



void setTestCentersName(String testCentersName) {
	this.testCentersName = testCentersName;
}
	
	
	
}
