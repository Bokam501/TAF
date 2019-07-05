package com.hcl.atf.taf.report.xml.beans;
import static javax.persistence.GenerationType.IDENTITY;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;

import static javax.persistence.GenerationType.IDENTITY;

import javax.persistence.CascadeType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

import com.hcl.ilcm.workflow.model.WorkflowStatus;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = {"testCaseId", "testCaseName","testCaseCode","startTime","endTime","result","testcaseType","featureCovered","testStep"})
public class TestCaseListBean implements java.io.Serializable {

	private Integer testCaseId;
	@XmlTransient
	private ProductMasterBean productMaster;
	private String testCaseName;
	@XmlTransient
	private String testCaseDescription;
	private String testCaseCode;

	//Changes for integration of Testmanagement tools - Priya.B
	//	private ProductFeature productFeature;	
	@XmlTransient
	private Date testCaseCreatedDate;
	private String testcaseType;
	//	private String testCasePriority;
	@XmlTransient
	private Date testCaseLastUpdatedDate;
	@XmlTransient
	private String testCaseSource;
	@XmlTransient
	private Integer testcaseExecutionType;
	@XmlTransient
	private String testcaseinput;
	@XmlTransient
	private String testcaseexpectedoutput;
	@XmlTransient
	private String preconditions;

	//Changes for integration of Testmanagement tools - Priya.B - ends
	@XmlTransient
	private String testCaseScriptQualifiedName;
	@XmlTransient
	private String testCaseScriptFileName;
	@XmlTransient
	private Set<TestSuiteListBean> testSuiteLists = new HashSet<TestSuiteListBean>(0);
	@XmlTransient
	private Set<TestExecutionResultBean> testExecutionResults = new HashSet<TestExecutionResultBean>(
			0);
	@XmlElement(name="testStep")
	private Set<TestCaseStepsListBean> testStep = new HashSet<TestCaseStepsListBean>(
			0);
	@XmlTransient
	private Set<WorkPackageBean> workPackageList = new HashSet<WorkPackageBean>(0);
	//private Set<ProductVersionListMasterBean> productVersionList = new HashSet<ProductVersionListMasterBean>(0);

	//private Set<TrccExecutionPlanDetails> trccExecutionPlanDetails = new HashSet<TrccExecutionPlanDetails>(0);
	//private ExecutionTypeMasterBean executionTypeMaster;
	//private TestCasePriorityBean testCasePriority;
	//private TestcaseTypeMasterBean testcaseTypeMaster;

	//private Set<WorkPackageTestCaseExecutionPlanBean>  workPackageTestCaseExecutionPlan=new HashSet<WorkPackageTestCaseExecutionPlanBean>(0);
	@XmlTransient
	private Set<TestRunJobBean>  testRunJobSet=new HashSet<TestRunJobBean>(0);
	//private Set<TestRunPlanBean> testRunPlanList = new HashSet<TestRunPlanBean>(0);
	@XmlTransient
	private String status;

	//private Set<Risk> risk;

	//private Set<ProductFeatureBean> productFeature = new HashSet<ProductFeatureBean>(0);
	//private Set<TestCaseScript> testscript = new HashSet<TestCaseScript>(0);
	@XmlTransient
	private WorkflowStatus workflowStatus;
	@XmlTransient
	private Integer totalEffort;
	@XmlTransient
	private Integer testCaseExecutionOrder;
	//private Set<TestCaseEntityGroupBean> testCaseEntityGroup = new HashSet<TestCaseEntityGroup>(0);

	//private Set<TestCaseScriptBean> testCaseScripts = new HashSet<TestCaseScriptBean>(0);
	private String featureCovered;
	@XmlTransient
	private String testScriptsCovered;
	
	private Date startTime;
	private Date endTime;
	private String result;
	public Integer getTestCaseId() {
		return testCaseId;
	}

	public void setTestCaseId(Integer testCaseId) {
		this.testCaseId = testCaseId;
	}

	public ProductMasterBean getProductMaster() {
		return productMaster;
	}

	public void setProductMaster(ProductMasterBean productMaster) {
		this.productMaster = productMaster;
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

	public Date getTestCaseCreatedDate() {
		return testCaseCreatedDate;
	}

	public void setTestCaseCreatedDate(Date testCaseCreatedDate) {
		this.testCaseCreatedDate = testCaseCreatedDate;
	}

	public String getTestcaseType() {
		return testcaseType;
	}

	public void setTestcaseType(String testcaseType) {
		this.testcaseType = testcaseType;
	}

	public Date getTestCaseLastUpdatedDate() {
		return testCaseLastUpdatedDate;
	}

	public void setTestCaseLastUpdatedDate(Date testCaseLastUpdatedDate) {
		this.testCaseLastUpdatedDate = testCaseLastUpdatedDate;
	}

	public String getTestCaseSource() {
		return testCaseSource;
	}

	public void setTestCaseSource(String testCaseSource) {
		this.testCaseSource = testCaseSource;
	}

	public Integer getTestcaseExecutionType() {
		return testcaseExecutionType;
	}

	public void setTestcaseExecutionType(Integer testcaseExecutionType) {
		this.testcaseExecutionType = testcaseExecutionType;
	}

	public String getTestcaseinput() {
		return testcaseinput;
	}

	public void setTestcaseinput(String testcaseinput) {
		this.testcaseinput = testcaseinput;
	}

	public String getTestcaseexpectedoutput() {
		return testcaseexpectedoutput;
	}

	public void setTestcaseexpectedoutput(String testcaseexpectedoutput) {
		this.testcaseexpectedoutput = testcaseexpectedoutput;
	}

	public String getPreconditions() {
		return preconditions;
	}

	public void setPreconditions(String preconditions) {
		this.preconditions = preconditions;
	}

	public String getTestCaseScriptQualifiedName() {
		return testCaseScriptQualifiedName;
	}

	public void setTestCaseScriptQualifiedName(String testCaseScriptQualifiedName) {
		this.testCaseScriptQualifiedName = testCaseScriptQualifiedName;
	}

	public String getTestCaseScriptFileName() {
		return testCaseScriptFileName;
	}

	public void setTestCaseScriptFileName(String testCaseScriptFileName) {
		this.testCaseScriptFileName = testCaseScriptFileName;
	}

	public Set<TestSuiteListBean> getTestSuiteLists() {
		return testSuiteLists;
	}

	public void setTestSuiteLists(Set<TestSuiteListBean> testSuiteLists) {
		this.testSuiteLists = testSuiteLists;
	}

	public Set<TestExecutionResultBean> getTestExecutionResults() {
		return testExecutionResults;
	}

	public void setTestExecutionResults(
			Set<TestExecutionResultBean> testExecutionResults) {
		this.testExecutionResults = testExecutionResults;
	}

	public Set<TestCaseStepsListBean> getTestStep() {
		return testStep;
	}

	public void setTestStep(Set<TestCaseStepsListBean> testStep) {
		this.testStep = testStep;
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

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public WorkflowStatus getWorkflowStatus() {
		return workflowStatus;
	}

	public void setWorkflowStatus(WorkflowStatus workflowStatus) {
		this.workflowStatus = workflowStatus;
	}

	public Integer getTotalEffort() {
		return totalEffort;
	}

	public void setTotalEffort(Integer totalEffort) {
		this.totalEffort = totalEffort;
	}

	public Integer getTestCaseExecutionOrder() {
		return testCaseExecutionOrder;
	}

	public void setTestCaseExecutionOrder(Integer testCaseExecutionOrder) {
		this.testCaseExecutionOrder = testCaseExecutionOrder;
	}

	public String getFeatureCovered() {
		return featureCovered;
	}

	public void setFeatureCovered(String featureCovered) {
		this.featureCovered = featureCovered;
	}

	public String getTestScriptsCovered() {
		return testScriptsCovered;
	}

	public void setTestScriptsCovered(String testScriptsCovered) {
		this.testScriptsCovered = testScriptsCovered;
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

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}


}
