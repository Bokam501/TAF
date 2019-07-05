package com.hcl.atf.taf.model.dto;

import java.util.Date;


public class WorkPackageTCEPSummaryDTO {
	private String productName;
	private Integer workPackageId;
	private String workPackageName;
	private String workFlowStageName;
	private Date actualStartDate;
	private Date actualEndDate;
	private String exeType;
	private Integer testCaseCount;
	private Integer testSuiteCount;
	private Integer featueCount;
	private Integer jobCount;
	private Integer jobsExecuting;
	private Integer jobsQueued;
	private Integer jobsCompleted;
	private Integer jobsAborted;
	private Integer defectsCount;
	private Integer passedCount;
	private Integer failedCount;
	private Integer blockedCount;
	private Integer norunCount;
	private Integer notexecutedCount;
	private Integer teststepcount;
	private Integer wpStartEnddayDiff;
	private Date plannedStartDate;
	private Date plannedEndDate;
	private String WpnthDayfromStrart;
	//private String TestSystemName;
	private Integer toolIntagrationId;
	private String toolIntagrationName;
	
	private String ResultCode;
	private String ExportedDate;
	
	public WorkPackageTCEPSummaryDTO(){
		this.workPackageId = 0;
		this.workPackageName = "";
		this.workFlowStageName = "";
		this.testCaseCount = 0;
		this.testSuiteCount = 0;
		this.featueCount = 0;
		this.jobCount = 0;
		this.jobsExecuting = 0;
		this.jobsQueued = 0;
		this.jobsAborted = 0;
		this.jobsCompleted = 0;		
		this.defectsCount = 0;
		this.passedCount = 0;
		this.failedCount = 0;
		this.blockedCount = 0;
		this.norunCount = 0;
		this.notexecutedCount = 0;	
		this.wpStartEnddayDiff=0;
		this.ResultCode = "";
		//this.TestSystemName = "";
		this.toolIntagrationId = 0;
		this.toolIntagrationName = "";
		this.ExportedDate = "";
				
	}
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
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
	public String getWorkFlowStageName() {
		return workFlowStageName;
	}
	public void setWorkFlowStageName(String workFlowStageName) {
		this.workFlowStageName = workFlowStageName;
	}
	public Date getActualStartDate() {
		return actualStartDate;
	}
	public void setActualStartDate(Date actualStartDate) {
		this.actualStartDate = actualStartDate;
	}
	public Date getActualEndDate() {
		return actualEndDate;
	}
	public void setActualEndDate(Date actualEndDate) {
		this.actualEndDate = actualEndDate;
	}
	public String getExeType() {
		return exeType;
	}
	public void setExeType(String exeType) {
		this.exeType = exeType;
	}
	public Integer getTestCaseCount() {
		return testCaseCount;
	}
	public void setTestCaseCount(Integer testCaseCount) {
		this.testCaseCount = testCaseCount;
	}
	public Integer getTestSuiteCount() {
		return testSuiteCount;
	}
	public void setTestSuiteCount(Integer testSuiteCount) {
		this.testSuiteCount = testSuiteCount;
	}
	public Integer getFeatueCount() {
		return featueCount;
	}
	public void setFeatueCount(Integer featueCount) {
		this.featueCount = featueCount;
	}
	public Integer getJobCount() {
		return jobCount;
	}
	public void setJobCount(Integer jobCount) {
		this.jobCount = jobCount;
	}
	public Integer getJobsExecuting() {
		return jobsExecuting;
	}
	public void setJobsExecuting(Integer jobsExecuting) {
		this.jobsExecuting = jobsExecuting;
	}
	public Integer getJobsQueued() {
		return jobsQueued;
	}
	public void setJobsQueued(Integer jobsQueued) {
		this.jobsQueued = jobsQueued;
	}
	public Integer getJobsCompleted() {
		return jobsCompleted;
	}
	public void setJobsCompleted(Integer jobsCompleted) {
		this.jobsCompleted = jobsCompleted;
	}
	public Integer getJobsAborted() {
		return jobsAborted;
	}
	public void setJobsAborted(Integer jobsAborted) {
		this.jobsAborted = jobsAborted;
	}
	public Integer getDefectsCount() {
		return defectsCount;
	}
	public void setDefectsCount(Integer defectsCount) {
		this.defectsCount = defectsCount;
	}
	public Integer getPassedCount() {
		return passedCount;
	}
	public void setPassedCount(Integer passedCount) {
		this.passedCount = passedCount;
	}
	public Integer getFailedCount() {
		return failedCount;
	}
	public void setFailedCount(Integer failedCount) {
		this.failedCount = failedCount;
	}
	public Integer getBlockedCount() {
		return blockedCount;
	}
	public void setBlockedCount(Integer blockedCount) {
		this.blockedCount = blockedCount;
	}
	public Integer getNorunCount() {
		return norunCount;
	}
	public void setNorunCount(Integer norunCount) {
		this.norunCount = norunCount;
	}
	public Integer getNotexecutedCount() {
		return notexecutedCount;
	}
	public void setNotexecutedCount(Integer notexecutedCount) {
		this.notexecutedCount = notexecutedCount;
	}
	public Integer getTeststepcount() {
		return teststepcount;
	}
	public void setTeststepcount(Integer teststepcount) {
		this.teststepcount = teststepcount;
	}
	public Integer getWpStartEnddayDiff() {
		return wpStartEnddayDiff;
	}
	public void setWpStartEnddayDiff(Integer wpStartEnddayDiff) {
		this.wpStartEnddayDiff = wpStartEnddayDiff;
	}
	public Date getPlannedStartDate() {
		return plannedStartDate;
	}
	public void setPlannedStartDate(Date plannedStartDate) {
		this.plannedStartDate = plannedStartDate;
	}
	public Date getPlannedEndDate() {
		return plannedEndDate;
	}
	public void setPlannedEndDate(Date plannedEndDate) {
		this.plannedEndDate = plannedEndDate;
	}
	public String getExportedDate() {
		return ExportedDate;
	}
	public void setExportedDate(String ExportedDate) {
		this.ExportedDate = ExportedDate;
	}
	public String getResultCode() {
		return ResultCode;
	}
	public void setResultCode(String ResultCode) {
		this.ResultCode = ResultCode;
	}
	public Integer getToolIntagrationId() {
		return toolIntagrationId;
	}
	public void setToolIntagrationId(Integer toolIntagrationId) {
		this.toolIntagrationId = toolIntagrationId;
	}
	public String getToolIntagrationName() {
		return toolIntagrationName;
	}
	public void setToolIntagrationName(String toolIntagrationName) {
		this.toolIntagrationName = toolIntagrationName;
	}	
	
	
}
