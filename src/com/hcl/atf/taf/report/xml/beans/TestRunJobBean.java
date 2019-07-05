package com.hcl.atf.taf.report.xml.beans;

// Generated Feb 4, 2014 4:30:16 PM by Hibernate Tools 3.4.0.CR1

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = {"testRunJobId", "environment","deviceId", "deviceplatform", "status", "passedTestSteps","totalTestSteps", "passedTestCases", "totalTestCases", "startTime", "endTime","hostIpAddress","hostName","testToolName","scriptType","comments","testSuite"})
public class TestRunJobBean implements java.io.Serializable {

	private Integer testRunJobId;
	//private TestRunPlanBean testRunPlanBean;
	@XmlTransient
	private Integer testRunPlanId;
	@XmlTransient
	private String testRunPlanName; 
	@XmlTransient
	private String description;
	@XmlTransient
	private Date testRunScheduledStartTime;
	@XmlTransient
	private Date testRunScheduledEndTime;
	@XmlTransient
	private Date testRunTriggeredTime;
	private String status;
	private String comments;
	@XmlTransient
	private Date createdDate;
	@XmlTransient
	private WorkPackageBean workPackageBean;
	private String hostName;
	private String hostIpAddress;
	
	private Date startTime;
	private Date endTime;
	@XmlTransient
	private String testRunEvidenceStatus;	
	//private EnvironmentCombinationBean environmentCombinationBean;
	@XmlTransient
	private Integer	environmentId;
	@XmlTransient
	private Integer envionmentCombinationStatus;
	
	//private RunConfigurationBean runConfigurationBean;
	@XmlTransient
	private Set<TestCaseListBean> testCase = new HashSet<TestCaseListBean>(0);
	@XmlTransient
	private Integer testCaseId;
	@XmlTransient
	private String testCaseName;
	@XmlTransient
	private String testCaseDescription;
	@XmlTransient
	private String testCaseCode;
	@XmlTransient
	private Integer testCaseStatus;
	private String environment;
	//@XmlElement(name="Test Suite")
	@XmlElement(name="testSuite")
	private Set<TestSuiteListBean> testSuite = new HashSet<TestSuiteListBean>(0);
	//private Set<ProductFeatureBean> featureSet = new HashSet<ProductFeatureBean>(0);
	//private Set<WorkPackageTestCaseExecutionPlanBean> workPackageTestCaseExecutionPlans = new HashSet<WorkPackageTestCaseExecutionPlanBean>(0);
	//private Set<WorkPackageFeatureExecutionPlanBean> workPackageFeatureExecutionPlans = new HashSet<WorkPackageFeatureExecutionPlanBean>(0);
	//private Set<WorkPackageTestSuiteExecutionPlanBean> workPackageTestSuiteExecutionPlans = new HashSet<WorkPackageTestSuiteExecutionPlanBean>(0);
	@XmlTransient
	private String testScriptFileLocation;
	@XmlTransient
	private String scriptPathLocation;
	//private ScriptTypeMasterBean scriptTypeMasterBean;
	private String scriptType;
	//private TestToolMasterBean testToolMasterBean;
	@XmlTransient
	private Integer testToolId;
	private String testToolName;
	private String deviceId;
	private String deviceplatform;
	private Integer totalTestCases;
	private Integer passedTestCases;
	private Integer totalTestSteps;
	private Integer passedTestSteps;
	
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
	public String getTestRunPlanName() {
		return testRunPlanName;
	}
	public void setTestRunPlanName(String testRunPlanName) {
		this.testRunPlanName = testRunPlanName;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Date getTestRunScheduledStartTime() {
		return testRunScheduledStartTime;
	}
	public void setTestRunScheduledStartTime(Date testRunScheduledStartTime) {
		this.testRunScheduledStartTime = testRunScheduledStartTime;
	}
	public Date getTestRunScheduledEndTime() {
		return testRunScheduledEndTime;
	}
	public void setTestRunScheduledEndTime(Date testRunScheduledEndTime) {
		this.testRunScheduledEndTime = testRunScheduledEndTime;
	}
	public Date getTestRunTriggeredTime() {
		return testRunTriggeredTime;
	}
	public void setTestRunTriggeredTime(Date testRunTriggeredTime) {
		this.testRunTriggeredTime = testRunTriggeredTime;
	}
	
	
	public String getComments() {
		return comments;
	}
	public void setComments(String comments) {
		this.comments = comments;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public Date getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}
	public WorkPackageBean getWorkPackageBean() {
		return workPackageBean;
	}
	public void setWorkPackageBean(WorkPackageBean workPackageBean) {
		this.workPackageBean = workPackageBean;
	}
	/*public TestSuiteListBean getTestSuite() {
		return testSuite;
	}
	public void setTestSuite(TestSuiteListBean testSuite) {
		this.testSuite = testSuite;
	}*/
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
	public String getTestRunEvidenceStatus() {
		return testRunEvidenceStatus;
	}
	public void setTestRunEvidenceStatus(String testRunEvidenceStatus) {
		this.testRunEvidenceStatus = testRunEvidenceStatus;
	}
	public Integer getEnvionmentCombinationStatus() {
		return envionmentCombinationStatus;
	}
	public void setEnvionmentCombinationStatus(Integer envionmentCombinationStatus) {
		this.envionmentCombinationStatus = envionmentCombinationStatus;
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
	public String getTestCaseDescription() {
		return testCaseDescription;
	}
	public void setTestCaseDescription(String testCaseDescription) {
		this.testCaseDescription = testCaseDescription;
	}
	public String getTestCaseCode() {
		return testCaseCode;
	}
	public void setTestCaseCode(String testCaseCode) {
		this.testCaseCode = testCaseCode;
	}
	public Integer getTestCaseStatus() {
		return testCaseStatus;
	}
	public void setTestCaseStatus(Integer testCaseStatus) {
		this.testCaseStatus = testCaseStatus;
	}
	public Set<TestSuiteListBean> getTestSuite() {
		return testSuite;
	}
	public void setTestSuite(Set<TestSuiteListBean> testSuite) {
		this.testSuite = testSuite;
	}
	public String getTestScriptFileLocation() {
		return testScriptFileLocation;
	}
	public void setTestScriptFileLocation(String testScriptFileLocation) {
		this.testScriptFileLocation = testScriptFileLocation;
	}
	public String getScriptPathLocation() {
		return scriptPathLocation;
	}
	public void setScriptPathLocation(String scriptPathLocation) {
		this.scriptPathLocation = scriptPathLocation;
	}
	public String getScriptType() {
		return scriptType;
	}
	public void setScriptType(String scriptType) {
		this.scriptType = scriptType;
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
	public Set<TestCaseListBean> getTestCase() {
		return testCase;
	}
	public void setTestCaseList(Set<TestCaseListBean> testCase) {
		this.testCase = testCase;
	}
	public String getDeviceId() {
		return deviceId;
	}
	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}
	public String getDeviceplatform() {
		return deviceplatform;
	}
	public void setDeviceplatform(String deviceplatform) {
		this.deviceplatform = deviceplatform;
	}
	public Integer getTotalTestCases() {
		return totalTestCases;
	}
	public void setTotalTestCases(Integer totalTestCases) {
		this.totalTestCases = totalTestCases;
	}
	public Integer getPassedTestCases() {
		return passedTestCases;
	}
	public void setPassedTestCases(Integer passedTestCases) {
		this.passedTestCases = passedTestCases;
	}
	public Integer getTotalTestSteps() {
		return totalTestSteps;
	}
	public void setTotalTestSteps(Integer totalTestSteps) {
		this.totalTestSteps = totalTestSteps;
	}
	public Integer getPassedTestSteps() {
		return passedTestSteps;
	}
	public void setPassedTestSteps(Integer passedTestSteps) {
		this.passedTestSteps = passedTestSteps;
	}
	public String getEnvironment() {
		return environment;
	}
	public void setEnvironment(String environment) {
		this.environment = environment;
	}
	public Integer getEnvironmentId() {
		return environmentId;
	}
	public void setEnvironmentId(Integer environmentId) {
		this.environmentId = environmentId;
	}
	public Date getStartTime() {
		return startTime;
	}
	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}
	public Date getEndTime() {
		return endTime;
	}
	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
	

}