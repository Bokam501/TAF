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
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

@Entity
@Table(name = "workpackage")
public class WorkPackage implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	
	private Integer workPackageId;
	private String name;
	private String description;
	private Integer status;
	private Date plannedStartDate;
	private Date plannedEndDate;
	private Date actualStartDate;
	private Date actualEndDate;
	private Date createDate;
	private Date modifiedDate;
	private ProductBuild  productBuild;
	private String runCode;
	private Integer isActive;
	private Set<Environment> environmentList = new HashSet<Environment>(0);
	private Set<WorkPackageTestCase> workPackageTestCases = new HashSet<WorkPackageTestCase>(0);
	private Set<ProductLocale> localeList = new HashSet<ProductLocale>(0);
	private Set<WorkPackageTestCaseExecutionPlan> workPackageTestCaseExecutionPlan = new HashSet<WorkPackageTestCaseExecutionPlan>(0);
	private Set<TimeSheetEntryMaster> timeSheetEntryMaster = new HashSet<TimeSheetEntryMaster>(0);
	private Set<TestCaseList> testcaseList = new HashSet<TestCaseList>(0);
	private Set<TestSuiteList> testSuiteList = new HashSet<TestSuiteList>(0);
	private WorkFlowEvent workFlowEvent;
	private Integer workpackageCloneId;
	private Set<EnvironmentCombination> environmentCombinationList = new HashSet<EnvironmentCombination>(0);
	private ExecutionTypeMaster workPackageType;
	private Set<WorkPackageTestSuite> workPackageTestSuites = new HashSet<WorkPackageTestSuite>(0);
	private Set<WorkpackageRunConfiguration> workPackageRunConfigSet = new HashSet<WorkpackageRunConfiguration>(0);
	private Set<RunConfiguration> runConfigurationList = new HashSet<RunConfiguration>(0);
	private Set<TestRunJob> testRunJobSet=new HashSet<TestRunJob>(0);
	private TestRunPlan  testRunPlan;
	private TestRunPlanGroup  testRunPlanGroup;
	private String sourceType;
	private Set<WorkPackageFeature> workPackageFeature = new HashSet<WorkPackageFeature>(0);

	private Set<ProductFeature> productFeature = new HashSet<ProductFeature>(0);
	private UserList userList;
	private Integer iterationNumber;
	private LifeCyclePhase lifeCyclePhase;

	private ProductMaster productMaster;
	private String resultsReportingMode;
	private String testExecutionMode;

    ///If the results are to be reported in a combined mode, the Run Configuration that will act as the results aggregator
	private TestRunJob combinedResultsReportingJob;
	
	private TestCycle testCycle;
	private String result;
	
	public WorkPackage() {
	}
	
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "workPackageId", unique = true, nullable = false)
	public Integer getWorkPackageId() {
		return this.workPackageId;
	}

	public void setWorkPackageId(Integer workPackageId) {
		this.workPackageId = workPackageId;
	}
	
	
	@Column(name = "name", nullable = false, length = 80)
	public String getName() {
		return name;
	}
	
	
	public void setName(final String paramName) {
		name = paramName;
	}

	@Column(name = "description", length = 200)
	public String getDescription() {
		return description;
	}

	public void setDescription(final String paramDescription) {
		description = paramDescription;
	}

	@Column(name = "status")
	public Integer getStatus() {
		return status;
	}

	public void setStatus(final Integer paramStatus) {
		status = paramStatus;
	}
	@Column(name = "plannedStartDate", length = 45)
	public Date getPlannedStartDate() {
		return plannedStartDate;
	}

	public void setPlannedStartDate(final Date paramPlannedStartDate) {
		plannedStartDate = paramPlannedStartDate;
	}
	@Column(name = "plannedEndDate", length = 45)
	public Date getPlannedEndDate() {
		return plannedEndDate;
	}

	public void setPlannedEndDate(final Date paramPlannedEndDate) {
		plannedEndDate = paramPlannedEndDate;
	}

	@Column(name = "actualStartDate", length = 45)
	public Date getActualStartDate() {
		return actualStartDate;
	}

	public void setActualStartDate(final Date paramActualStartDate) {
		actualStartDate = paramActualStartDate;
	}

	@Column(name = "actualEndDate", length = 45)
	public Date getActualEndDate() {
		return actualEndDate;
	}

	public void setActualEndDate(final Date paramActualEndDate) {
		actualEndDate = paramActualEndDate;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "productBuildId") 
	public ProductBuild getProductBuild() {
		return productBuild;
	}

	public void setProductBuild(ProductBuild productBuild) {
		this.productBuild = productBuild;
	}
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "createDate")
	public Date getCreateDate() {
		return createDate;
	}
	
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;	
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "modifiedDate")
	public Date getModifiedDate() {
		return modifiedDate;
	}
	
	public void setModifiedDate(Date modifiedDate) {
		this.modifiedDate = modifiedDate;		
	}
	
	@Column(name = "runCode", length = 200)
	public String getRunCode() {
		return runCode;
	}

	public void setRunCode(String runCode) {
		this.runCode = runCode;
	}
	
	@Column(name = "isActive", length = 11)
	public Integer getIsActive() {
		return isActive;
	}

	public void setIsActive(Integer isActive) {
		this.isActive = isActive;
	}
	
	@ManyToMany(fetch = FetchType.LAZY,cascade=CascadeType.ALL)
	@JoinTable(name = "workpackage_has_environment", joinColumns = { @JoinColumn(name = "workpackageId", nullable = false, updatable = false) }, inverseJoinColumns = { @JoinColumn(name = "environmentId", nullable = false, updatable = false) })
	public Set<Environment> getEnvironmentList() {
		return environmentList;
	}

	public void setEnvironmentList(Set<Environment> environmentList) {
		this.environmentList = environmentList;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "workPackage",cascade=CascadeType.ALL)
	public Set<WorkPackageTestCase> getWorkPackageTestCases() {
		return workPackageTestCases;
	}

	public void setWorkPackageTestCases(Set<WorkPackageTestCase> workPackageTestCases) {
		this.workPackageTestCases = workPackageTestCases;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	@ManyToMany(fetch = FetchType.LAZY,cascade=CascadeType.ALL)
	@JoinTable(name = "workpackage_has_locale", joinColumns = { @JoinColumn(name = "workpackageId", nullable = false, updatable = false) }, inverseJoinColumns = { @JoinColumn(name = "productLocaleId", nullable = false, updatable = false) })
	public Set<ProductLocale> getLocaleList() {
		return localeList;
	}

	public void setLocaleList(Set<ProductLocale> localeList) {
		this.localeList = localeList;
	}
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "workPackage",cascade=CascadeType.ALL)
	public Set<WorkPackageTestCaseExecutionPlan> getWorkPackageTestCaseExecutionPlan() {
		return workPackageTestCaseExecutionPlan;
	}

	public void setWorkPackageTestCaseExecutionPlan(Set<WorkPackageTestCaseExecutionPlan> workPackageTestCaseExecutionPlan) {
		this.workPackageTestCaseExecutionPlan = workPackageTestCaseExecutionPlan;
	}
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "workPackage",cascade=CascadeType.ALL)
	public Set<TimeSheetEntryMaster> getTimeSheetEntryMaster() {
		return timeSheetEntryMaster;
	}

	public void setTimeSheetEntryMaster(
			Set<TimeSheetEntryMaster> timeSheetEntryMaster) {
		this.timeSheetEntryMaster = timeSheetEntryMaster;
	}

	@Override
	public boolean equals(Object o) {
		WorkPackage workPackage = (WorkPackage) o;
		if (this.workPackageId == workPackage.getWorkPackageId()) {
			return true;
		}else{
			return false;
		}
	}
	
	@Override
	public int hashCode(){
	    return (int) workPackageId;
	  }
	@ManyToMany(fetch = FetchType.LAZY,cascade=CascadeType.ALL)
	@JoinTable(name = "workpackage_has_testcase", joinColumns = { @JoinColumn(name = "workpackageId", nullable = false, updatable = false) }, inverseJoinColumns = { @JoinColumn(name = "testCaseId", nullable = false, updatable = false) })
		public Set<TestCaseList> getTestcaseList() {
		return testcaseList;
	}

	public void setTestcaseList(Set<TestCaseList> testcaseList) {
		this.testcaseList = testcaseList;
	}
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "workfloweventid") 
	public WorkFlowEvent getWorkFlowEvent() {
		return workFlowEvent;
	}

	public void setWorkFlowEvent(WorkFlowEvent workFlowEvent) {
		this.workFlowEvent = workFlowEvent;
	}

	@Column(name = "workpackageCloneId")
	public Integer getWorkpackageCloneId() {
		return workpackageCloneId;
	}

	public void setWorkpackageCloneId(Integer workpackageCloneId) {
		this.workpackageCloneId = workpackageCloneId;
	}
	@ManyToMany(fetch = FetchType.LAZY,cascade=CascadeType.ALL)
	@JoinTable(name = "workpackage_has_environmentcombination", joinColumns = { @JoinColumn(name = "workpackageId", nullable = false, updatable = false) }, inverseJoinColumns = { @JoinColumn(name = "environmentCombinationId", nullable = false, updatable = false) })
		public Set<EnvironmentCombination> getEnvironmentCombinationList() {
		return environmentCombinationList;
	}

	public void setEnvironmentCombinationList(
			Set<EnvironmentCombination> environmentCombinationList) {
		this.environmentCombinationList = environmentCombinationList;
	}
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "workPackageType", nullable = false)
	public ExecutionTypeMaster getWorkPackageType() {
		return workPackageType;
	}

	public void setWorkPackageType(ExecutionTypeMaster workPackageType) {
		this.workPackageType = workPackageType;
	}
	@ManyToMany(fetch = FetchType.LAZY,cascade=CascadeType.ALL)
	@JoinTable(name = "workpackage_has_testsuite", joinColumns = { @JoinColumn(name = "workpackageId", nullable = false, updatable = false) }, inverseJoinColumns = { @JoinColumn(name = "testSuiteId", nullable = false, updatable = false) })
	public Set<TestSuiteList> getTestSuiteList() {
		return testSuiteList;
	}

	public void setTestSuiteList(Set<TestSuiteList> testSuiteList) {
		this.testSuiteList = testSuiteList;
	}
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "workPackage",cascade=CascadeType.ALL)
	public Set<WorkPackageTestSuite> getWorkPackageTestSuites() {
		return workPackageTestSuites;
	}

	public void setWorkPackageTestSuites(
			Set<WorkPackageTestSuite> workPackageTestSuites) {
		this.workPackageTestSuites = workPackageTestSuites;
	}

	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "workPackage",cascade=CascadeType.ALL)
	public Set<RunConfiguration> getRunConfigurationList() {
		return runConfigurationList;
	}

	public void setRunConfigurationList(
			Set<RunConfiguration> runConfigurationList) {
		this.runConfigurationList = runConfigurationList;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "testRunPlanId") 
	public TestRunPlan getTestRunPlan() {
		return testRunPlan;
	}

	public void setTestRunPlan(TestRunPlan testRunPlan) {
		this.testRunPlan = testRunPlan;
	}
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "workpackage",cascade=CascadeType.ALL)
	public Set<WorkpackageRunConfiguration> getWorkPackageRunConfigSet() {
		return workPackageRunConfigSet;
	}

	public void setWorkPackageRunConfigSet(
			Set<WorkpackageRunConfiguration> workPackageRunConfigSet) {
		this.workPackageRunConfigSet = workPackageRunConfigSet;
	}
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "workPackage",cascade=CascadeType.ALL)
	public Set<TestRunJob> getTestRunJobSet() {
		return testRunJobSet;
	}

	public void setTestRunJobSet(Set<TestRunJob> testRunJobSet) {
		this.testRunJobSet = testRunJobSet;
	}
	
	@Transient
	public Integer getPlannedTestCasesCount() {
		if (testRunJobSet == null)
			return 0;
		else {
			Integer plannedTestcasesCount = 0;
			for (TestRunJob job : testRunJobSet) {
				plannedTestcasesCount += job.getTotalTestCasesCount();
			}
			return plannedTestcasesCount;
		}
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "workPackage",cascade=CascadeType.ALL)
	public Set<WorkPackageFeature> getWorkPackageFeature() {
		return workPackageFeature;
	}

	public void setWorkPackageFeature(Set<WorkPackageFeature> workPackageFeature) {
		this.workPackageFeature = workPackageFeature;
	}

	

	@ManyToMany(fetch = FetchType.LAZY,cascade=CascadeType.ALL)
	@JoinTable(name = "workpackage_has_feature", joinColumns = { @JoinColumn(name = "workpackageId", nullable = false, updatable = false) }, inverseJoinColumns = { @JoinColumn(name = "featureId", nullable = false, updatable = false) })
	public Set<ProductFeature> getProductFeature() {
		return productFeature;
	}

	public void setProductFeature(Set<ProductFeature> productFeature) {
		this.productFeature = productFeature;
	}
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "testRunPlanGroupId") 
	public TestRunPlanGroup getTestRunPlanGroup() {
		return testRunPlanGroup;
	}

	public void setTestRunPlanGroup(TestRunPlanGroup testRunPlanGroup) {
		this.testRunPlanGroup = testRunPlanGroup;
	}
	@Column(name = "sourceType")
	public String getSourceType() {
		return sourceType;
	}

	public void setSourceType(String sourceType) {
		this.sourceType = sourceType;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "workPackageCreatedBy") 
	public UserList getUserList() {
		return userList;
	}

	public void setUserList(UserList userList) {
		this.userList = userList;
	}
	
	@Column(name = "iterationNumber")
	public Integer getIterationNumber() {
		return iterationNumber;
	}

	public void setIterationNumber(Integer iterationNumber) {
		this.iterationNumber = iterationNumber;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "lifeCyclePhaseId") 
	public LifeCyclePhase getLifeCyclePhase() {
		return lifeCyclePhase;
	}

	public void setLifeCyclePhase(LifeCyclePhase lifeCyclePhase) {
		this.lifeCyclePhase = lifeCyclePhase;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "productId")
	public ProductMaster getProductMaster() {
		return productMaster;
	}

	public void setProductMaster(ProductMaster productMaster) {
		this.productMaster = productMaster;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "combinedResultsReportingJobId")
	public TestRunJob getCombinedResultsReportingJob() {
		return combinedResultsReportingJob;
	}

	public void setCombinedResultsReportingJob(TestRunJob combinedResultsReportingJob) {
		this.combinedResultsReportingJob = combinedResultsReportingJob;
	}
	
	@Column(name = "executionMode")
	public String getTestExecutionMode() {
		return testExecutionMode;
	}

	public void setTestExecutionMode(String testExecutionMode) {
		this.testExecutionMode = testExecutionMode;
	}
	
	@Column(name = "resultsreportingmode")
	public String getResultsReportingMode() {
		return resultsReportingMode;
	}

	public void setResultsReportingMode(String resultsReportingMode) {
		this.resultsReportingMode = resultsReportingMode;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "test_cycle_id")
	public TestCycle getTestCycle() {
		return testCycle;
	}
	public void setTestCycle(TestCycle testCycle) {
		this.testCycle = testCycle;
	}
	
	@Column(name = "result", length = 50)
	public String getResult() {
		return result;
	}
	public void setResult(String result) {
		this.result = result;
	}
}
