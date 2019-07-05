package com.hcl.atf.taf.model;

// Generated Feb 4, 2014 4:30:16 PM by Hibernate Tools 3.4.0.CR1

import static javax.persistence.GenerationType.IDENTITY;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

/**
 * TestRunList generated by hbm2java
 */
@Entity
@Table(name = "test_run_job")
public class TestRunJob implements java.io.Serializable {

	private Integer testRunJobId;
	private TestRunPlan testRunPlan;
	private Date testRunTriggeredTime;
	private Integer testRunStatus;
	private String testRunFailureMessage;
	private GenericDevices genericDevices;
	private WorkPackage workPackage;
	private TestSuiteList testSuite;
	private HostList hostList;
	private Date testRunStartTime;
	private Date testRunEndTime;
	private String testRunEvidenceStatus;	
	private EnvironmentCombination environmentCombination;
	private RunConfiguration runConfiguration;
	private Set<TestCaseList> testCaseListSet=new HashSet<TestCaseList>(0);
	private Set<TestSuiteList> testSuiteSet=new HashSet<TestSuiteList>(0);
	private Set<ProductFeature> featureSet=new HashSet<ProductFeature>(0);
	private Set<WorkPackageTestCaseExecutionPlan> workPackageTestCaseExecutionPlans = new HashSet<WorkPackageTestCaseExecutionPlan>(0);
	private Set<WorkPackageFeatureExecutionPlan> workPackageFeatureExecutionPlans = new HashSet<WorkPackageFeatureExecutionPlan>(0);
	private Set<WorkPackageTestSuiteExecutionPlan> workPackageTestSuiteExecutionPlans = new HashSet<WorkPackageTestSuiteExecutionPlan>(0);
	private String testScriptFileLocation;
	private String scriptPathLocation;
	private ScriptTypeMaster scriptTypeMaster;
	private TestToolMaster testToolMaster;
	private String result;
	
	public TestRunJob() {
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
	
	@Column(name = "testRunEvidenceStatus", length = 50)
	public String getTestRunEvidenceStatus() {
		return this.testRunEvidenceStatus;
	}

	public void setTestRunEvidenceStatus(String testRunEvidenceStatus) {
		this.testRunEvidenceStatus = testRunEvidenceStatus;
	}	

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "testRunJobId", unique = true, nullable = false)
	public Integer getTestRunJobId() {
		return testRunJobId;
	}

	public void setTestRunJobId(Integer testRunJobId) {
		this.testRunJobId = testRunJobId;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "testRunPlanId")
	public TestRunPlan getTestRunPlan() {
		return testRunPlan;
	}

	public void setTestRunPlan(TestRunPlan testRunPlan) {
		this.testRunPlan = testRunPlan;
	}

	@Column(name = "testRunStatus", length = 50)
	public Integer getTestRunStatus() {
		return testRunStatus;
	}

	public void setTestRunStatus(Integer testRunStatus) {
		this.testRunStatus = testRunStatus;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "genericDeviceId")
	public GenericDevices getGenericDevices() {
		return genericDevices;
	}

	public void setGenericDevices(GenericDevices genericDevices) {
		this.genericDevices = genericDevices;
	}
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "workpackageId")
	public WorkPackage getWorkPackage() {
		return workPackage;
	}

	public void setWorkPackage(WorkPackage workPackage) {
		this.workPackage = workPackage;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "testSuiteId")
	public TestSuiteList getTestSuite() {
		return testSuite;
	}

	public void setTestSuite(TestSuiteList testSuite) {
		this.testSuite = testSuite;
	}
	
	
	
	@Override
	public boolean equals(Object testRunJobList) {
	
		if (testRunJobList == null)
			return false;
		TestRunJob testRunJob =null;
		if(testRunJobList instanceof TestRunJob){
		testRunJob=(TestRunJob) testRunJobList;
		if (testRunJob.getTestRunJobId().equals(this.testRunJobId)) {
			return true;
		} else {
			return false;
		}
		}else{
			int testRunJobId=(Integer)testRunJobList;
			if (testRunJobId == this.testRunJobId) {
				return true;
			} else {
				return false;
			}
		}
	}
	@Override
	public int hashCode(){
	    return (int) testRunJobId;
	  }

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "hostId")
	public HostList getHostList() {
		return hostList;
	}

	public void setHostList(HostList hostList) {
		this.hostList = hostList;
	}


	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "environmentCombinationId")
	public EnvironmentCombination getEnvironmentCombination() {
		return environmentCombination;
	}



	public void setEnvironmentCombination(
			EnvironmentCombination environmentCombination) {
		this.environmentCombination = environmentCombination;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "runConfigurationId")
	public RunConfiguration getRunConfiguration() {
		return runConfiguration;
	}



	public void setRunConfiguration(RunConfiguration runConfiguration) {
		this.runConfiguration = runConfiguration;
	}

	
	@ManyToMany(fetch = FetchType.LAZY,cascade=CascadeType.ALL)
	@JoinTable(name = "testrunjob_has_testcase", joinColumns = { @JoinColumn(name = "testRunJobId", nullable = false, updatable = false) }, inverseJoinColumns = { @JoinColumn(name = "testCaseId", nullable = false, updatable = false) })
	public Set<TestCaseList> getTestCaseListSet() {
		return testCaseListSet;
	}

	public void setTestCaseListSet(Set<TestCaseList> testCaseListSet) {
		this.testCaseListSet = testCaseListSet;
	}

	@Transient
	public Integer getPlannedTestCasesCount() {
		if (testCaseListSet == null)
			return 0;
		else 
			return testCaseListSet.size();
	}

	@ManyToMany(fetch = FetchType.LAZY,cascade=CascadeType.ALL)
	@JoinTable(name = "testrunjob_has_testsuite", joinColumns = { @JoinColumn(name = "testRunJobId", nullable = false, updatable = false) }, inverseJoinColumns = { @JoinColumn(name = "testSuiteId", nullable = false, updatable = false) })
	public Set<TestSuiteList> getTestSuiteSet() {
		return testSuiteSet;
	}



	public void setTestSuiteSet(Set<TestSuiteList> testSuiteSet) {
		this.testSuiteSet = testSuiteSet;
	}

	
	@Transient
	public Integer getTotalTestCasesCount() {
		if (testSuiteSet == null)
			return 0;
		else{ 
			Integer tcSize = 0;
			for(TestSuiteList tsl : testSuiteSet){
				tcSize += tsl.getTestCaseLists().size();
			}
			return tcSize;
		}
	}

	@ManyToMany(fetch = FetchType.LAZY,cascade=CascadeType.ALL)
	@JoinTable(name = "testrunjob_has_feature", joinColumns = { @JoinColumn(name = "testRunJobId", nullable = false, updatable = false) }, inverseJoinColumns = { @JoinColumn(name = "featureId", nullable = false, updatable = false) })
	public Set<ProductFeature> getFeatureSet() {
		return featureSet;
	}



	public void setFeatureSet(Set<ProductFeature> featureSet) {
		this.featureSet = featureSet;
	}


	@OneToMany(fetch = FetchType.LAZY, mappedBy = "testRunJob",cascade=CascadeType.ALL)
	public Set<WorkPackageTestCaseExecutionPlan> getWorkPackageTestCaseExecutionPlans() {
		return workPackageTestCaseExecutionPlans;
	}



	public void setWorkPackageTestCaseExecutionPlans(
			Set<WorkPackageTestCaseExecutionPlan> workPackageTestCaseExecutionPlans) {
		this.workPackageTestCaseExecutionPlans = workPackageTestCaseExecutionPlans;
	}


	@OneToMany(fetch = FetchType.LAZY, mappedBy = "testRunJob",cascade=CascadeType.ALL)
	public Set<WorkPackageFeatureExecutionPlan> getWorkPackageFeatureExecutionPlans() {
		return workPackageFeatureExecutionPlans;
	}



	public void setWorkPackageFeatureExecutionPlans(
			Set<WorkPackageFeatureExecutionPlan> workPackageFeatureExecutionPlans) {
		this.workPackageFeatureExecutionPlans = workPackageFeatureExecutionPlans;
	}


	@OneToMany(fetch = FetchType.LAZY, mappedBy = "testRunJob",cascade=CascadeType.ALL)
	public Set<WorkPackageTestSuiteExecutionPlan> getWorkPackageTestSuiteExecutionPlans() {
		return workPackageTestSuiteExecutionPlans;
	}



	public void setWorkPackageTestSuiteExecutionPlans(
			Set<WorkPackageTestSuiteExecutionPlan> workPackageTestSuiteExecutionPlans) {
		this.workPackageTestSuiteExecutionPlans = workPackageTestSuiteExecutionPlans;
	}
	
	@Column(name="scriptFileLocation")
	public String getTestScriptFileLocation() {
		return testScriptFileLocation;
	}
	
	/**
	 * @param testScriptFileLocation the testScriptFileLocation to set
	 */
	public void setTestScriptFileLocation(String testScriptFileLocation) {
		this.testScriptFileLocation = testScriptFileLocation;
	}

	@Column(name="scriptPathLocation")
	public String getScriptPathLocation() {
		return scriptPathLocation;
	}

	public void setScriptPathLocation(String scriptPathLocation) {
		this.scriptPathLocation = scriptPathLocation;
	}	
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "testToolId", nullable = true)
	public TestToolMaster getTestToolMaster() {
		return testToolMaster;
	}
	public void setTestToolMaster(TestToolMaster testToolMaster) {
		this.testToolMaster = testToolMaster;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "scriptLanguage")
	public ScriptTypeMaster getScriptTypeMaster() {
		return this.scriptTypeMaster;
	}

	public void setScriptTypeMaster(ScriptTypeMaster scriptTypeMaster) {
		this.scriptTypeMaster = scriptTypeMaster;
	}
	
	@Column(name = "result", length = 50)
	public String getResult() {
		return result;
	}
	public void setResult(String result) {
		this.result = result;
	}
}