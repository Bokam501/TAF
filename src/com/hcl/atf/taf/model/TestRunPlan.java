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
@Table(name = "test_run_plan")
public class TestRunPlan implements java.io.Serializable{

	private Integer testRunPlanId;
	private String testRunPlanName; 
	private String description;
	private ExecutionTypeMaster executionType;
	private Set<TestSuiteList> testSuiteLists;
	private ProductVersionListMaster productVersionListMaster;
	private ProductBuild productBuild;
	private Set<RunConfiguration> runConfigurationList;
	
	private Date testRunScheduledStartTime;
	private Integer testRunScheduledIntervalInHour;
	private Integer testRunRecurrenceLimit;
	private String testRunCronSchedule;
	private Date testRunScheduledEndTime;
	private String notifyByMail;
	
	private Integer status;
	private Date createdDate;
	
	private Set<WorkPackage> workPackageSet;
	private TestToolMaster testToolMaster;
	private Set<TestCaseList> testCaseList;
	private Set<ProductFeature> featureList;
	private Set<TestRunPlangroupHasTestRunPlan> testRunPlangroupHasTestRunPlans = new HashSet<TestRunPlangroupHasTestRunPlan>(0);
	
	private Integer autoPostBugs;
	private Integer autoPostResults;
	private UserList userList;
	
	private Attachment testData;
	

	private Attachment objectRepository;
	
	@Transient
	private Set<Attachment> attachments = new HashSet<Attachment>(0);
	
	//private String xmlContentTestRunPlan;
    
    //Changes for Dynamic Script generation from different test script sources
    private String testScriptSource; 
    //The location of the test scripts to be executed by the test run plan
    private String testSuiteScriptFileLocation;
    //This field specifies if the Test Run Plan has multiple test suites selected for execution
    private String multipleTestSuites;
    /*This filed specifies which scripts should be used during the test run plan execution
     * Option 1 : Test Suite Script packs.
     * Option 2 : test Run Plan Script pack.
     */
    private String testScriptsLevel;
    
    private ScriptTypeMaster scriptTypeMaster; 

    //This attribute will define whether a call needs to be made to ISE
    //while executing the TRP
    private String useIntelligentTestPlan;
    
    private String automationMode; 
    //Adding attribute for managing mode of reporting : Job Level Reporting / Combined Reporting
    private String resultsReportingMode;
    ///If the results are to be reported in a combined mode, the Run Configuration that will act as the results aggregator
	private RunConfiguration combinedResultsRunConfiguration;
    
	public TestRunPlan () {
    }
	
	
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "testRunPlanId", unique = true, nullable = false)
	public  Integer getTestRunPlanId() {
		return testRunPlanId;
	}
	public  void setTestRunPlanId(Integer testRunPlanId) {
		this.testRunPlanId = testRunPlanId;
	}
	@Column(name = "testRunPlanName", nullable = false, length = 80)
	public  String getTestRunPlanName() {
		return testRunPlanName;
	}
	public  void setTestRunPlanName(String testRunPlanName) {
		this.testRunPlanName = testRunPlanName;
	}
	@Column(name = "description", nullable = false, length = 80)
	public  String getDescription() {
		return description;
	}
	public  void setDescription(String description) {
		this.description = description;
	}
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "executionTypeId", nullable = true)
	public  ExecutionTypeMaster getExecutionType() {
		return executionType;
	}
	public  void setExecutionType(ExecutionTypeMaster executionType) {
		this.executionType = executionType;
	}
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "productVersionId")
	public  ProductVersionListMaster getProductVersionListMaster() {
		return productVersionListMaster;
	}
	public  void setProductVersionListMaster(
			ProductVersionListMaster productVersionListMaster) {
		this.productVersionListMaster = productVersionListMaster;
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
	@Column(name = "testRunScheduledStartTime", length = 19)
	public  Date getTestRunScheduledStartTime() {
		return testRunScheduledStartTime;
	}
	public  void setTestRunScheduledStartTime(Date testRunScheduledStartTime) {
		this.testRunScheduledStartTime = testRunScheduledStartTime;
	}
	@Column(name = "testRunScheduledIntervalInHour")
	public  Integer getTestRunScheduledIntervalInHour() {
		return testRunScheduledIntervalInHour;
	}
	public  void setTestRunScheduledIntervalInHour(
			Integer testRunScheduledIntervalInHour) {
		this.testRunScheduledIntervalInHour = testRunScheduledIntervalInHour;
	}
	@Column(name = "testRunRecurrenceLimit")
	public  Integer getTestRunRecurrenceLimit() {
		return testRunRecurrenceLimit;
	}
	public  void setTestRunRecurrenceLimit(Integer testRunRecurrenceLimit) {
		this.testRunRecurrenceLimit = testRunRecurrenceLimit;
	}
	@Column(name = "testRunCronSchedule", length = 45)
	public  String getTestRunCronSchedule() {
		return testRunCronSchedule;
	}
	public  void setTestRunCronSchedule(String testRunCronSchedule) {
		this.testRunCronSchedule = testRunCronSchedule;
	}
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "testRunScheduledEndTime", length = 19)
	public  Date getTestRunScheduledEndTime() {
		return testRunScheduledEndTime;
	}
	public  void setTestRunScheduledEndTime(Date testRunScheduledEndTime) {
		this.testRunScheduledEndTime = testRunScheduledEndTime;
	}
	
	@Column(name = "notifyByMail", length = 1000)
	public  String getNotifyByMail() {
		return notifyByMail;
	}
	public  void setNotifyByMail(String notifyByMail) {
		this.notifyByMail = notifyByMail;
	}
	@Column(name = "status")
	public  Integer getStatus() {
		return status;
	}
	public  void setStatus(Integer status) {
		this.status = status;
	}
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "createdDate")
	public  Date getCreatedDate() {
		return createdDate;
	}
	public  void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	
	@ManyToMany(fetch = FetchType.LAZY,cascade=CascadeType.ALL)
	@JoinTable(name = "testrunplan_has_testsuite", joinColumns = { @JoinColumn(name = "testRunPlanId", nullable = false, updatable = false) }, inverseJoinColumns = { @JoinColumn(name = "testSuiteId", nullable = false, updatable = false) })
	public  Set<TestSuiteList> getTestSuiteLists() {
		return testSuiteLists;
	}
	public  void setTestSuiteLists(Set<TestSuiteList> testSuiteLists) {
		this.testSuiteLists = testSuiteLists;
	}
	
	@ManyToMany(fetch = FetchType.LAZY,cascade=CascadeType.ALL)
	@JoinTable(name = "testrunplan_has_runconfig", joinColumns = { @JoinColumn(name = "testRunPlanId", nullable = false, updatable = false) }, inverseJoinColumns = { @JoinColumn(name = "runConfigurationId", nullable = false, updatable = false) })
	public  Set<RunConfiguration> getRunConfigurationList() {
		return runConfigurationList;
	}
	public  void setRunConfigurationList(
			Set<RunConfiguration> runConfigurationList) {
		this.runConfigurationList = runConfigurationList;
	}
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "testRunPlan",cascade=CascadeType.ALL)
	public Set<WorkPackage> getWorkPackageSet() {
		return workPackageSet;
	}
	public void setWorkPackageSet(Set<WorkPackage> workPackageSet) {
		this.workPackageSet = workPackageSet;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "testToolId", nullable = true)
	public TestToolMaster getTestToolMaster() {
		return testToolMaster;
	}
	public void setTestToolMaster(TestToolMaster testToolMaster) {
		this.testToolMaster = testToolMaster;
	}
	
	@ManyToMany(fetch = FetchType.LAZY,cascade=CascadeType.ALL)
	@JoinTable(name = "testrunplan_has_testcase", joinColumns = { @JoinColumn(name = "testRunPlanId", nullable = false, updatable = false) }, inverseJoinColumns = { @JoinColumn(name = "testcaseId", nullable = false, updatable = false) })
	public Set<TestCaseList> getTestCaseList() {
		return testCaseList;
	}
	public void setTestCaseList(Set<TestCaseList> testCaseList) {
		this.testCaseList = testCaseList;
	}
	
	@ManyToMany(fetch = FetchType.LAZY,cascade=CascadeType.ALL)
	@JoinTable(name = "testrunplan_has_feature", joinColumns = { @JoinColumn(name = "testRunPlanId", nullable = false, updatable = false) }, inverseJoinColumns = { @JoinColumn(name = "featureId", nullable = false, updatable = false) })
	public Set<ProductFeature> getFeatureList() {
		return featureList;
	}
	public void setFeatureList(Set<ProductFeature> featureList) {
		this.featureList = featureList;
	}
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "testrunplan",cascade=CascadeType.ALL)
	public Set<TestRunPlangroupHasTestRunPlan> getTestRunPlangroupHasTestRunPlans() {
		return testRunPlangroupHasTestRunPlans;
	}
	public void setTestRunPlangroupHasTestRunPlans(
			Set<TestRunPlangroupHasTestRunPlan> testRunPlangroupHasTestRunPlans) {
		this.testRunPlangroupHasTestRunPlans = testRunPlangroupHasTestRunPlans;
	}
	
	
	
	@Column(name = "autoPostResults", length = 1000)
	public Integer getAutoPostResults() {
		return autoPostResults;
	}


	public void setAutoPostResults(Integer autoPostResults) {
		this.autoPostResults = autoPostResults;
	}

	@Column(name = "autoPostBugs", length = 1000)
	public Integer getAutoPostBugs() {
		return autoPostBugs;
	}


	public void setAutoPostBugs(Integer autoPostBugs) {
		this.autoPostBugs = autoPostBugs;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "testRunPlanCreatedBy") 
	public UserList getUserList() {
		return userList;
	}

	public void setUserList(UserList userList) {
		this.userList = userList;
	}


	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "testData") 
	public Attachment getTestData() {
		return testData;
	}


	public void setTestData(Attachment testData) {
		this.testData = testData;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "objectRepository") 
	public Attachment getObjectRepository() {
		return objectRepository;
	}


	public void setObjectRepository(Attachment objectRepository) {
		this.objectRepository = objectRepository;
	}


	/**
	 * @return the attachments
	 */
	@Transient
	public Set<Attachment> getAttachments() {
		return attachments;
	}


	/**
	 * @param attachments the attachments to set
	 */
	public void setAttachments(Set<Attachment> attachments) {
		this.attachments = attachments;
	}

	@Column(name = "testScriptSource")
	public String getTestScriptSource() {
		return testScriptSource;
	}
	public void setTestScriptSource(String testScriptSource) {
		this.testScriptSource = testScriptSource;
	}


	@Column(name = "testSuiteScriptFileLocation")
	public String getTestSuiteScriptFileLocation() {
		return testSuiteScriptFileLocation;
	}
	public void setTestSuiteScriptFileLocation(String testSuiteScriptFileLocation) {
		this.testSuiteScriptFileLocation = testSuiteScriptFileLocation;
	}


	@Column(name = "multipleTestSuites")
	public String getMultipleTestSuites() {
		return multipleTestSuites;
	}
	public void setMultipleTestSuites(String multipleTestSuites) {
		this.multipleTestSuites = multipleTestSuites;
	}

	@Column(name = "testScriptsLevel")
	public String getTestScriptsLevel() {
		return testScriptsLevel;
	}
	public void setTestScriptsLevel(String testScriptsLevel) {
		this.testScriptsLevel = testScriptsLevel;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "scriptType")
	public ScriptTypeMaster getScriptTypeMaster() {
		return this.scriptTypeMaster;
	}

	public void setScriptTypeMaster(ScriptTypeMaster scriptTypeMaster) {
		this.scriptTypeMaster = scriptTypeMaster;
	}


	@Column(name = "useIntelligentTestPlan")
	public String getUseIntelligentTestPlan() {
		return useIntelligentTestPlan;
	}

	public void setUseIntelligentTestPlan(String useIntelligentTestPlan) {
		this.useIntelligentTestPlan = useIntelligentTestPlan;
	}

	@Column(name = "automationMode")
	public String getAutomationMode() {
		return automationMode;
	}

	public void setAutomationMode(String automationMode) {
		this.automationMode = automationMode;
	}

	@Column(name = "resultsReportMode")
	public String getResultsReportingMode() {
		return resultsReportingMode;
	}

	public void setResultsReportingMode(String resultsReportingMode) {
		this.resultsReportingMode = resultsReportingMode;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "combinedResultsRunConfigurationId") 
	public RunConfiguration getCombinedResultsRunConfiguration() {
		return combinedResultsRunConfiguration;
	}

	public void setCombinedResultsRunConfiguration(RunConfiguration combinedResultsRunConfiguration) {
		this.combinedResultsRunConfiguration = combinedResultsRunConfiguration;
	}

	@Override
	public boolean equals(Object o) {
		TestRunPlan testRunPlan = (TestRunPlan) o;
		if(testRunPlan!=null){
		if (this.testRunPlanId == testRunPlan.getTestRunPlanId()) {
			return true;
		}else{
			return false;
		}
	}
		return true;
	}

	@Override
	public int hashCode(){
	    return (int) testRunPlanId;
	  }	
}
