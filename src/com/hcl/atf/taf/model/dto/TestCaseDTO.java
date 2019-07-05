package com.hcl.atf.taf.model.dto;

import java.util.Date;

import com.hcl.atf.taf.model.DecouplingCategory;
import com.hcl.atf.taf.model.ProductFeature;
import com.hcl.atf.taf.model.ProductMaster;

public class TestCaseDTO {
	private Integer testCaseId;
	private Integer workPackageId;
	private ProductMaster productMaster;
	private String testCaseName;
	private String testCaseDescription;
	private String testCaseCode;
	private Date testCaseCreatedDate;
	private String testcaseType;
	private Date testCaseLastUpdatedDate;
	private String testCaseSource;
	private Integer testcaseExecutionType;
	private String testcaseinput;
	private String testcaseexpectedoutput;
	private String preconditions;
	private Long environmentCount;
	private String observedOutput;
	private String result;
	private Long defectsCount;
	private Long testCaseCountOfRunconfig;
	private Integer testCaseExecutionResultId;
	private Integer wprunConfigurationId;
	private Integer runConfigurationId;
	private String runConfigurationName;
	private String envCombName;
	private String deviceName;
	private Integer totalPass;
	private Integer totalFail;
	private Integer totalNoRun;
	private Integer totalBlock;
	private Integer totalWPTestCase;
	private Integer totalExecutedTesCases;
	private Integer executionPriorityId;
	private Integer notExecuted;
	private Integer teststepcount;
	private Integer testRunJobId;
	private ProductFeature productFeature;
	private int featurepriotiesArry[][];
	private DecouplingCategory decuplingCategory;
	private Integer deCouplepriotiesArry[][];
	private Integer testCaseExeResArr[];
	private String testSuiteName;
	private Integer totalWptestSuite;
	private Integer totalWpFeature;
	private Integer totalExecutionPlanTestCase;
	private Integer testerId;
	private String testerName;
	private Date startTime;
	private Date endTime;
	private String jobFailureMessage;
	private String jobStatus;
	private String jobResult;
	private String wpResult;
	private String wpStatus;
	private String wpName;
	private Integer wptcepId;
	private Date actualExecutionDate;
	private String tcerComments;
	
	private Integer workflowStatusId;
	private String workflowStatusName;
	private Integer assigneeId;
	private String assigneeName;
	private Integer reviewerId;
	private String reviewerName;
	private String analysisRemarks;
	
	private String analysisOutCome;
	
	private String testcaseSuccessRate;
	private String featureCoverage;
	private String buildCoverage;
	private String testCaseAvgExecutionTime;
	private String testCaseAge;
	private String testcaseQualityIndex;
	private String testCasePercentage;
	
	
	
	public Integer getTotalExecutionPlanTestCase() {
		return totalExecutionPlanTestCase;
	}
	public void setTotalExecutionPlanTestCase(Integer totalExecutionPlanTestCase) {
		this.totalExecutionPlanTestCase = totalExecutionPlanTestCase;
	}
	public Integer getTestCaseExecutionResultId() {
		return testCaseExecutionResultId;
	}
	public void setTestCaseExecutionResultId(Integer testCaseExecutionResultId) {
		this.testCaseExecutionResultId = testCaseExecutionResultId;
	}
	public String getResult() {
		return result;
	}
	public void setResult(String result) {
		this.result = result;
	}
	public Long getDefectsCount() {
		return defectsCount;
	}
	public void setDefectsCount(Long defectsCount) {
		this.defectsCount = defectsCount;
	}
	public Long getEnvironmentCount() {
		return environmentCount;
	}
	public void setEnvironmentCount(Long environmentCount) {
		this.environmentCount = environmentCount;
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
	public String getTestcaseType() {
		return testcaseType;
	}
	public void setTestcaseType(String testcaseType) {
		this.testcaseType = testcaseType;
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
	public String getObservedOutput() {
		return observedOutput;
	}
	public void setObservedOutput(String observedOutput) {
		this.observedOutput = observedOutput;
	}
	public Long getTestCaseCountOfRunconfig() {
		return testCaseCountOfRunconfig;
	}
	public void setTestCaseCountOfRunconfig(Long testCaseCountOfRunconfig) {
		this.testCaseCountOfRunconfig = testCaseCountOfRunconfig;
	}
	public Integer getRunConfigurationId() {
		return runConfigurationId;
	}
	public void setRunConfigurationId(Integer runConfigurationId) {
		this.runConfigurationId = runConfigurationId;
	}
	public String getRunConfigurationName() {
		return runConfigurationName;
	}
	public void setRunConfigurationName(String runConfigurationName) {
		this.runConfigurationName = runConfigurationName;
	}
	public String getEnvCombName() {
		return envCombName;
	}
	public void setEnvCombName(String envCombName) {
		this.envCombName = envCombName;
	}
	public String getDeviceName() {
		return deviceName;
	}
	public void setDeviceName(String deviceName) {
		this.deviceName = deviceName;
	}
	public Integer getTotalPass() {
		return totalPass;
	}
	public void setTotalPass(Integer totalPass) {
		this.totalPass = totalPass;
	}
	public Integer getTotalFail() {
		return totalFail;
	}
	public void setTotalFail(Integer totalFail) {
		this.totalFail = totalFail;
	}
	public Integer getTotalNoRun() {
		return totalNoRun;
	}
	public void setTotalNoRun(Integer totalNoRun) {
		this.totalNoRun = totalNoRun;
	}
	public Integer getTotalBlock() {
		return totalBlock;
	}
	public void setTotalBlock(Integer totalBlock) {
		this.totalBlock = totalBlock;
	}
	public Integer getWprunConfigurationId() {
		return wprunConfigurationId;
	}
	public void setWprunConfigurationId(Integer wprunConfigurationId) {
		this.wprunConfigurationId = wprunConfigurationId;
	}
	public Integer getTotalWPTestCase() {
		return totalWPTestCase;
	}
	public void setTotalWPTestCase(Integer totalWPTestCase) {
		this.totalWPTestCase = totalWPTestCase;
	}
	public Integer getTotalExecutedTesCases() {
		return totalExecutedTesCases;
	}
	public void setTotalExecutedTesCases(Integer totalExecutedTesCases) {
		this.totalExecutedTesCases = totalExecutedTesCases;
	}
	public Integer getExecutionPriorityId() {
		return executionPriorityId;
	}
	public void setExecutionPriorityId(Integer executionPriorityId) {
		this.executionPriorityId = executionPriorityId;
	}
	public Integer getNotExecuted() {
		return notExecuted;
	}
	public void setNotExecuted(Integer notExecuted) {
		this.notExecuted = notExecuted;
	}
	public Integer getTeststepcount() {
		return teststepcount;
	}
	public void setTeststepcount(Integer teststepcount) {
		this.teststepcount = teststepcount;
	}
	public Integer getTestRunJobId() {
		return testRunJobId;
	}
	public void setTestRunJobId(Integer testRunJobId) {
		this.testRunJobId = testRunJobId;
	}
	
	public ProductFeature getProductFeature() {
		return productFeature;
	}
	public void setProductFeature(ProductFeature productFeature) {
		this.productFeature = productFeature;
	}
	public int[][] getFeaturepriotiesArry() {
		return featurepriotiesArry;
	}
	public void setFeaturepriotiesArry(int[][] featurepriotiesArry) {
		this.featurepriotiesArry = featurepriotiesArry;
	}
	public DecouplingCategory getDecuplingCategory() {
		return decuplingCategory;
	}
	public void setDecuplingCategory(DecouplingCategory decuplingCategory) {
		this.decuplingCategory = decuplingCategory;
	}
	public Integer[][] getDeCouplepriotiesArry() {
		return deCouplepriotiesArry;
	}
	public void setDeCouplepriotiesArry(Integer[][] deCouplepriotiesArry) {
		this.deCouplepriotiesArry = deCouplepriotiesArry;
	}
	public String getTestSuiteName() {
		return testSuiteName;
	}
	public void setTestSuiteName(String testSuiteName) {
		this.testSuiteName = testSuiteName;
	}
	public Integer[] getTestCaseExeResArr() {
		return testCaseExeResArr;
	}
	public void setTestCaseExeResArr(Integer[] testCaseExeResArr) {
		this.testCaseExeResArr = testCaseExeResArr;
	}
	public Integer getTotalWptestSuite() {
		return totalWptestSuite;
	}
	public void setTotalWptestSuite(Integer totalWptestSuite) {
		this.totalWptestSuite = totalWptestSuite;
	}
	public Integer getTotalWpFeature() {
		return totalWpFeature;
	}
	public void setTotalWpFeature(Integer totalWpFeature) {
		this.totalWpFeature = totalWpFeature;
	}
	public Integer getWorkPackageId() {
		return workPackageId;
	}
	public void setWorkPackageId(Integer workPackageId) {
		this.workPackageId = workPackageId;
	}
	public Integer getTesterId() {
		return testerId;
	}
	public void setTesterId(Integer testerId) {
		this.testerId = testerId;
	}
	public String getTesterName() {
		return testerName;
	}
	public void setTesterName(String testerName) {
		this.testerName = testerName;
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
	public String getJobFailureMessage() {
		return jobFailureMessage;
	}
	public void setJobFailureMessage(String jobFailureMessage) {
		this.jobFailureMessage = jobFailureMessage;
	}
	public String getJobStatus() {
		return jobStatus;
	}
	public void setJobStatus(String jobStatus) {
		this.jobStatus = jobStatus;
	}
	public String getJobResult() {
		return jobResult;
	}
	public void setJobResult(String jobResult) {
		this.jobResult = jobResult;
	}
	public String getWpResult() {
		return wpResult;
	}
	public void setWpResult(String wpResult) {
		this.wpResult = wpResult;
	}
	public String getWpStatus() {
		return wpStatus;
	}
	public void setWpStatus(String wpStatus) {
		this.wpStatus = wpStatus;
	}
	public String getWpName() {
		return wpName;
	}
	public void setWpName(String wpName) {
		this.wpName = wpName;
	}
	public Integer getWptcepId() {
		return wptcepId;
	}
	public void setWptcepId(Integer wptcepId) {
		this.wptcepId = wptcepId;
	}
	public Date getActualExecutionDate() {
		return actualExecutionDate;
	}
	public void setActualExecutionDate(Date actualExecutionDate) {
		this.actualExecutionDate = actualExecutionDate;
	}
	public String getTcerComments() {
		return tcerComments;
	}
	public void setTcerComments(String tcerComments) {
		this.tcerComments = tcerComments;
	}
	public ProductMaster getProductMaster() {
		return productMaster;
	}
	public void setProductMaster(ProductMaster productMaster) {
		this.productMaster = productMaster;
	}
	public Date getTestCaseCreatedDate() {
		return testCaseCreatedDate;
	}
	public void setTestCaseCreatedDate(Date testCaseCreatedDate) {
		this.testCaseCreatedDate = testCaseCreatedDate;
	}
	public Date getTestCaseLastUpdatedDate() {
		return testCaseLastUpdatedDate;
	}
	public void setTestCaseLastUpdatedDate(Date testCaseLastUpdatedDate) {
		this.testCaseLastUpdatedDate = testCaseLastUpdatedDate;
	}
	public Integer getWorkflowStatusId() {
		return workflowStatusId;
	}
	public void setWorkflowStatusId(Integer workflowStatusId) {
		this.workflowStatusId = workflowStatusId;
	}
	public String getWorkflowStatusName() {
		return workflowStatusName;
	}
	public void setWorkflowStatusName(String workflowStatusName) {
		this.workflowStatusName = workflowStatusName;
	}
	public Integer getAssigneeId() {
		return assigneeId;
	}
	public void setAssigneeId(Integer assigneeId) {
		this.assigneeId = assigneeId;
	}
	public String getAssigneeName() {
		return assigneeName;
	}
	public void setAssigneeName(String assigneeName) {
		this.assigneeName = assigneeName;
	}
	public Integer getReviewerId() {
		return reviewerId;
	}
	public void setReviewerId(Integer reviewerId) {
		this.reviewerId = reviewerId;
	}
	public String getReviewerName() {
		return reviewerName;
	}
	public void setReviewerName(String reviewerName) {
		this.reviewerName = reviewerName;
	}
	public String getAnalysisRemarks() {
		return analysisRemarks;
	}
	public void setAnalysisRemarks(String analysisRemarks) {
		this.analysisRemarks = analysisRemarks;
	}
	public String getAnalysisOutCome() {
		return analysisOutCome;
	}
	public void setAnalysisOutCome(String analysisOutCome) {
		this.analysisOutCome = analysisOutCome;
	}
	public String getTestcaseSuccessRate() {
		return testcaseSuccessRate;
	}
	public String getFeatureCoverage() {
		return featureCoverage;
	}
	public String getBuildCoverage() {
		return buildCoverage;
	}
	public String getTestCaseAvgExecutionTime() {
		return testCaseAvgExecutionTime;
	}
	public String getTestCaseAge() {
		return testCaseAge;
	}
	public String getTestcaseQualityIndex() {
		return testcaseQualityIndex;
	}
	public String getTestCasePercentage() {
		return testCasePercentage;
	}
	public void setTestcaseSuccessRate(String testcaseSuccessRate) {
		this.testcaseSuccessRate = testcaseSuccessRate;
	}
	public void setFeatureCoverage(String featureCoverage) {
		this.featureCoverage = featureCoverage;
	}
	public void setBuildCoverage(String buildCoverage) {
		this.buildCoverage = buildCoverage;
	}
	public void setTestCaseAvgExecutionTime(String testCaseAvgExecutionTime) {
		this.testCaseAvgExecutionTime = testCaseAvgExecutionTime;
	}
	public void setTestCaseAge(String testCaseAge) {
		this.testCaseAge = testCaseAge;
	}
	public void setTestcaseQualityIndex(String testcaseQualityIndex) {
		this.testcaseQualityIndex = testcaseQualityIndex;
	}
	public void setTestCasePercentage(String testCasePercentage) {
		this.testCasePercentage = testCasePercentage;
	}
	
	
	
}
