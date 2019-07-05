package com.hcl.atf.taf.model;

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
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;


@Entity
@Table(name = "workpackage_testcase_execution_plan")
public class WorkPackageTestCaseExecutionPlan  implements java.io.Serializable{
	
	private Integer id;
	@Column(name="plannedExecutionDate")
	private Date plannedExecutionDate;
	@Column(name="actualExecutionDate")
	private Date actualExecutionDate;
	@Column(name="createdDate")
	private Date createdDate;
	@Column(name="modifiedDate")
	private Date modifiedDate;
	@Column(name="executionStatus")
	private Integer executionStatus;
	@Column(name="isExecuted")
	private Integer isExecuted;
	private WorkPackage workPackage;
	private TestCaseList testCase;
	private UserList testLead;
	private UserList tester;
	private String runCode;
	private TestCaseExecutionResult testCaseExecutionResult;
	private WorkShiftMaster plannedWorkShiftMaster;
	private WorkShiftMaster actualWorkShiftMaster;
	private ExecutionPriority executionPriority;
	private Set<TestCaseConfiguration>  testCaseConfigurationSet=new HashSet<TestCaseConfiguration>(0);
	private WorkpackageRunConfiguration runConfiguration;
	private TestSuiteList testSuiteList;
	private HostList hostList;
	private ProductFeature feature;
	private String sourceType;
	private Integer status;
	private TestRunJob testRunJob; //This is the reporting job
	private ExecutionTypeMaster executionTypeMaster;
	private TestRunJob actualTestRunJob; //This is the job under which the execution happened
	
	private EnvironmentCombination environmentCombination; //This is the environment in which the actual execution happened


public WorkPackageTestCaseExecutionPlan  () {
}

	@OneToOne(fetch = FetchType.LAZY, mappedBy = "workPackageTestCaseExecutionPlan", cascade = CascadeType.ALL)
	public TestCaseExecutionResult getTestCaseExecutionResult() {
		return testCaseExecutionResult;
	}

	public void setTestCaseExecutionResult(
			TestCaseExecutionResult testCaseExecutionResult) {
		this.testCaseExecutionResult = testCaseExecutionResult;
	}

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "wptcepId", unique = true, nullable = false)
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "workPackageId") 
	public WorkPackage getWorkPackage() {
		return workPackage;
	}

	public void setWorkPackage(WorkPackage workPackage) {
		this.workPackage = workPackage;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "testcaseId") 
	public TestCaseList getTestCase() {
		return testCase;
	}

	public void setTestCase(TestCaseList testCase) {
		this.testCase = testCase;
	}

	
	@ManyToOne(fetch = FetchType.LAZY,cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH, CascadeType.DETACH})
	@JoinColumn(name="testLeadId")
	public UserList getTestLead() {
		return testLead;
	}

	public void setTestLead(UserList testLead) {
		this.testLead = testLead;
	}

	@ManyToOne(fetch = FetchType.LAZY,cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH, CascadeType.DETACH})
	@JoinColumn(name="testerId")
	public UserList getTester() {
		return tester;
	}

	public void setTester(UserList tester) {
		this.tester = tester;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public Date getPlannedExecutionDate() {
		return plannedExecutionDate;
	}

	public void setPlannedExecutionDate(Date plannedExecutionDate) {
		this.plannedExecutionDate = plannedExecutionDate;
	}

	public Date getActualExecutionDate() {
		return actualExecutionDate;
	}

	public void setActualExecutionDate(Date actualExecutionDate) {
		this.actualExecutionDate = actualExecutionDate;
	}
	@Temporal(TemporalType.TIMESTAMP)
	public Date getModifiedDate() {
		return modifiedDate;
	}

	public void setModifiedDate(Date modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

	public Integer getExecutionStatus() {
		return executionStatus;
	}

	public void setExecutionStatus(Integer executionStatus) {
		this.executionStatus = executionStatus;
	}
	
	@Column(name = "runCode", length = 200)
	public String getRunCode() {
		return runCode;
	}

	public void setRunCode(String runCode) {
		this.runCode = runCode;
	}
	@ManyToOne(fetch = FetchType.LAZY,cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH, CascadeType.DETACH})
	@JoinColumn(name = "plannedShiftId") 
	public WorkShiftMaster getPlannedWorkShiftMaster() {
		return plannedWorkShiftMaster;
	}

	public void setPlannedWorkShiftMaster(WorkShiftMaster plannedWorkShiftMaster) {
		this.plannedWorkShiftMaster = plannedWorkShiftMaster;
	}
	@ManyToOne(fetch = FetchType.LAZY,cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH, CascadeType.DETACH})
	@JoinColumn(name = "actualShiftId") 
	public WorkShiftMaster getActualWorkShiftMaster() {
		return actualWorkShiftMaster;
	}

	public void setActualWorkShiftMaster(WorkShiftMaster actualWorkShiftMaster) {
		this.actualWorkShiftMaster = actualWorkShiftMaster;
	}


	public Integer getIsExecuted() {
		return isExecuted;
	}

	public void setIsExecuted(Integer isExecuted) {
		this.isExecuted = isExecuted;
	}
	
	@ManyToOne(fetch = FetchType.LAZY,cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH, CascadeType.DETACH})
	@JoinColumn(name = "executionPriorityId", nullable=true) 
	@NotFound(action=NotFoundAction.IGNORE)
	public ExecutionPriority getExecutionPriority() {
		return executionPriority;
	}

	public void setExecutionPriority(ExecutionPriority executionPriority) {
		this.executionPriority = executionPriority;
	}
	
	private Set<Environment> environmentList = new HashSet<Environment>(0);

	@ManyToMany(fetch = FetchType.LAZY, mappedBy = "workPackageTestCaseExecutionPlanList",cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH, CascadeType.DETACH})
	public Set<Environment> getEnvironmentList() {
		return environmentList;
	}

	public void setEnvironmentList(Set<Environment> environmentList) {
		this.environmentList = environmentList;
	}
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "workpackage_run_list",cascade=CascadeType.ALL)
	public Set<TestCaseConfiguration> getTestCaseConfigurationSet() {
		return testCaseConfigurationSet;
	}

	public void setTestCaseConfigurationSet(
			Set<TestCaseConfiguration> testCaseConfigurationSet) {
		this.testCaseConfigurationSet = testCaseConfigurationSet;
	}
	@ManyToOne(fetch = FetchType.LAZY,cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH, CascadeType.DETACH})
	@JoinColumn(name = "testRunConfigurationId") 
	public WorkpackageRunConfiguration getRunConfiguration() {
		return runConfiguration;
	}

	public void setRunConfiguration(WorkpackageRunConfiguration runConfiguration) {
		this.runConfiguration = runConfiguration;
	}

	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "testsuiteId") 
	public TestSuiteList getTestSuiteList() {
		return testSuiteList;
	}

	
	public void setTestSuiteList(TestSuiteList testSuiteList) {
		this.testSuiteList = testSuiteList;
	}

	

	@ManyToOne(fetch = FetchType.LAZY,cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH, CascadeType.DETACH})
	@JoinColumn(name = "hostId") 
	public HostList getHostList() {
		return hostList;
	}

	public void setHostList(HostList hostList) {
		this.hostList = hostList;
	}

	
	
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "featureId") 
	public ProductFeature getFeature() {
		return feature;
	}

	public void setFeature(ProductFeature feature) {
		this.feature = feature;
	}
	@Column(name = "sourceType", length = 200)
	public String getSourceType() {
		return sourceType;
	}

	public void setSourceType(String sourceType) {
		this.sourceType = sourceType;
	}
	
	@Column(name = "status")
	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "testRunJobId") 
	public TestRunJob getTestRunJob() {
		return testRunJob;
	}

	public void setTestRunJob(TestRunJob testRunJob) {
		this.testRunJob = testRunJob;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "actualtestrunjobid") 
	public TestRunJob getActualTestRunJob() {
		return actualTestRunJob;
	}

	public void setActualTestRunJob(TestRunJob actualTestRunJob) {
		this.actualTestRunJob = actualTestRunJob;
	}

	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "executionType") 
	public ExecutionTypeMaster getExecutionTypeMaster() {
		return executionTypeMaster;
	}
	public void setExecutionTypeMaster(ExecutionTypeMaster executionTypeMaster) {
		this.executionTypeMaster = executionTypeMaster;
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
}
