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
@XmlType(propOrder = {"testSuiteId", "testSuiteName","testCase"})
public class TestSuiteListBean implements java.io.Serializable {

	private Integer testSuiteId;
	//private ScriptTypeMasterBean scriptTypeMaster;
	@XmlTransient
	private ProductMasterBean productMaster;
	//private ProductVersionListMasterBean productVersionListMaster;
	@XmlTransient
	private String testSuiteScriptFileLocation;
	private String testSuiteName;
	@XmlTransient
	private String scriptPlatformLocation;
	@XmlElement(name="testCase")
	private Set<TestCaseListBean> testCase = new HashSet<TestCaseListBean>(0);
	/*private Set<TestRunConfigurationChildBean> testRunConfigurationChilds = new HashSet<TestRunConfigurationChildBean>(
			0);
	private Set<TestExecutionResultBean> testExecutionResults = new HashSet<TestExecutionResultBean>(
			0);*/
	@XmlTransient
	private Integer status;
	@XmlTransient
	private Date statusChangeDate;
	//Changes for Test Management tools integration.
	@XmlTransient
	private String testSuiteCode;
	//Changes for Dynamic Script generation from different test script sources
	@XmlTransient
	private String testScriptSource;
	//private ExecutionTypeMasterBean executionTypeMaster;
	@XmlTransient
	private Set<TestSuiteListBean> testSuiteList = new HashSet<TestSuiteListBean>(0);
	@XmlTransient
	private Set<WorkPackageBean> workPackageList = new HashSet<WorkPackageBean>(0);
	//private Set<TestRunPlanBean> testRunPlanList = new HashSet<TestRunPlanBean>(0);
	@XmlTransient
	private Set<TestRunJobBean>  testRunJobSet=new HashSet<TestRunJobBean>(0);
	@XmlTransient
	private String description;
	@XmlTransient
	private Integer totalTestCases;
	@XmlTransient
	private Integer passedTestCases;
	@XmlTransient
	private Integer totalTestSteps;
	@XmlTransient
	private Integer passedTestSteps;
	//private TestCasePriorityBean executionPriority;
	//private Set<WorkPackageTestCaseExecutionPlanBean> wptcePlanSet = new HashSet<WorkPackageTestCaseExecutionPlanBean>(0);
	//Added for Composite Run Plan execution
	//private Set<RunConfigurationBean> runConfigList = new HashSet<RunConfigurationBean>(0);
	//private SCMSystem scmSystem;
	
	public TestSuiteListBean(){
		
	}
	

	public Integer getTestSuiteId() {
		return testSuiteId;
	}

	public void setTestSuiteId(Integer testSuiteId) {
		this.testSuiteId = testSuiteId;
	}

	public ProductMasterBean getProductMaster() {
		return productMaster;
	}

	public void setProductMaster(ProductMasterBean productMaster) {
		this.productMaster = productMaster;
	}

	public String getTestSuiteScriptFileLocation() {
		return testSuiteScriptFileLocation;
	}

	public void setTestSuiteScriptFileLocation(String testSuiteScriptFileLocation) {
		this.testSuiteScriptFileLocation = testSuiteScriptFileLocation;
	}

	public String getTestSuiteName() {
		return testSuiteName;
	}

	public void setTestSuiteName(String testSuiteName) {
		this.testSuiteName = testSuiteName;
	}

	public String getScriptPlatformLocation() {
		return scriptPlatformLocation;
	}

	public void setScriptPlatformLocation(String scriptPlatformLocation) {
		this.scriptPlatformLocation = scriptPlatformLocation;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Date getStatusChangeDate() {
		return statusChangeDate;
	}

	public void setStatusChangeDate(Date statusChangeDate) {
		this.statusChangeDate = statusChangeDate;
	}

	public String getTestSuiteCode() {
		return testSuiteCode;
	}

	public void setTestSuiteCode(String testSuiteCode) {
		this.testSuiteCode = testSuiteCode;
	}

	public String getTestScriptSource() {
		return testScriptSource;
	}

	public void setTestScriptSource(String testScriptSource) {
		this.testScriptSource = testScriptSource;
	}

	public Set<TestSuiteListBean> getTestSuiteList() {
		return testSuiteList;
	}

	public void setTestSuiteList(Set<TestSuiteListBean> testSuiteList) {
		this.testSuiteList = testSuiteList;
	}

	public Set<WorkPackageBean> getWorkPackageList() {
		return workPackageList;
	}

	public void setWorkPackageList(Set<WorkPackageBean> workPackageList) {
		this.workPackageList = workPackageList;
	}

	public Set<TestRunJobBean> getTestRunJobSet() {
		return testRunJobSet;
	}

	public void setTestRunJobSet(Set<TestRunJobBean> testRunJobSet) {
		this.testRunJobSet = testRunJobSet;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Set<TestCaseListBean> getTestCase() {
		return testCase;
	}

	public void setTestCase(Set<TestCaseListBean> testCase) {
		this.testCase = testCase;
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
	
	
	

	/*public TestSuiteList(ScriptTypeMaster scriptTypeMaster,
			ProductMaster productMaster,
			ProductVersionListMaster productVersionListMaster, SCMSystem scmSystem,			
			String testSuiteScriptFileLocation, String testSuiteName,String testScriptSource) {
		this.scriptTypeMaster = scriptTypeMaster;
		this.productMaster = productMaster;
		this.productVersionListMaster = productVersionListMaster;
		this.testSuiteScriptFileLocation = testSuiteScriptFileLocation;
		this.testSuiteName = testSuiteName;
		this.testScriptSource = testScriptSource;
		this.scmSystem = scmSystem;
	}

	public TestSuiteList(ScriptTypeMaster scriptTypeMaster,
			ProductMaster productMaster,
			ProductVersionListMaster productVersionListMaster, SCMSystem scmSystem,
			String testSuiteScriptFileLocation, String testSuiteName,String testSuiteCode,			 
			Set<TestCaseList> testCaseLists,
			Set<TestRunConfigurationChild> testRunConfigurationChilds,
			Set<TestExecutionResult> testExecutionResults,String testScriptSource) {
		this.scriptTypeMaster = scriptTypeMaster;
		this.productMaster = productMaster;
		this.productVersionListMaster = productVersionListMaster;
		this.testSuiteScriptFileLocation = testSuiteScriptFileLocation;
		this.testSuiteName = testSuiteName;
		this.testSuiteCode = testSuiteCode;
		this.testCaseLists = testCaseLists;
		this.testRunConfigurationChilds = testRunConfigurationChilds;
		this.testExecutionResults = testExecutionResults;
		this.testScriptSource = testScriptSource;
		this.scmSystem = scmSystem;
	}*/

	
	
	
}