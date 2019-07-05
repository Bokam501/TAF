package com.hcl.atf.taf.model;

// Generated Feb 4, 2014 4:30:16 PM by Hibernate Tools 3.4.0.CR1

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import static javax.persistence.GenerationType.IDENTITY;

import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * TestExecutionResultBugList generated by hbm2java
 */
@Entity
@Table(name = "test_execution_result_bug_list")
public class TestExecutionResultBugList implements java.io.Serializable {

	private Integer testExecutionResultBugId;
	private TestCaseExecutionResult testCaseExecutionResult;
	private String bugTitle;
	private String bugDescription;
	private String bugManagementSystemName;
	private String bugManagementSystemBugId;
	private Boolean fileBugInBugManagementSystem;
	private WorkFlow bugFilingStatus;
	private String remarks;
	private Date bugCreationTime;
	private Date bugModifiedTime;
	private DefectSeverity defectSeverity;
	private DefectTypeMaster defectType;
	private DefectIdentificationStageMaster defectFoundStage;
	private UserList assignee;
	private String ccList;
	private int isApproved;
	private UserList approvedBy;
	private Date approvedOn;
	private DefectSeverity approversDefectSeverity;
	private DefectApprovalStatusMaster defectApprovalStatus;
	private String approvalRemarks;
	
	private  ExecutionPriority testersPriority;
	private  ExecutionPriority approversPriority;
	private String isReproducableOnLive;
	private String isThereABugAlready;
	private String wasOnPrevDayWebRelease; 
	private String onsiteComments;
	private Integer uploadFlag;
	private TestStepExecutionResult testStepExecutionResult;
	private Integer analysedFlag;
	private Integer reportedBy;
	private Date modifiedDate;
	
	private ProductBuild reportedInBuild;
	private ProductBuild fixedInBuild;
	private String sourceFilesModifiedForFixing;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "priorityId")
	public ExecutionPriority getTestersPriority() {
		return testersPriority;
	}

	public void setTestersPriority(ExecutionPriority testersPriority) {
		this.testersPriority = testersPriority;
	}

	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "approversPriorityId")
	public ExecutionPriority getApproversPriority() {
		return approversPriority;
	}

	public void setApproversPriority(ExecutionPriority approversPriority) {
		this.approversPriority = approversPriority;
	}

	@Column(name = "isReproducableOnLive")
	public String getIsReproducableOnLive() {
		return isReproducableOnLive;
	}

	public void setIsReproducableOnLive(String isReproducableOnLive) {
		this.isReproducableOnLive = isReproducableOnLive;
	}

	@Column(name = "isThereABugAlready")
	public String getIsThereABugAlready() {
		return isThereABugAlready;
	}

	public void setIsThereABugAlready(String isThereABugAlready) {
		this.isThereABugAlready = isThereABugAlready;
	}
	@Column(name = "wasOnPrevDayWebRelease")
	public String getWasOnPrevDayWebRelease() {
		return wasOnPrevDayWebRelease;
	}

	public void setWasOnPrevDayWebRelease(String wasOnPrevDayWebRelease) {
		this.wasOnPrevDayWebRelease = wasOnPrevDayWebRelease;
	}

	@Column(name = "onsiteComments")
	public String getOnsiteComments() {
		return onsiteComments;
	}

	public void setOnsiteComments(String onsiteComments) {
		this.onsiteComments = onsiteComments;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "bugModifiedTime")
	public Date getBugModifiedTime() {
		return bugModifiedTime;
	}

	public void setBugModifiedTime(Date bugModifiedTime) {
		this.bugModifiedTime = bugModifiedTime;
	}

	public TestExecutionResultBugList() {
	}

	public TestExecutionResultBugList(TestCaseExecutionResult testCaseExecutionResult) {
		this.testCaseExecutionResult = testCaseExecutionResult;
	}

	public TestExecutionResultBugList(TestCaseExecutionResult testCaseExecutionResult,
			String bugTitle, String bugDescription,
			String bugManagementSystemName, String bugManagementSystemBugId,
			Boolean fileBugInBugManagementSystem, WorkFlow bugFilingStatus,
			String remarks,Date bugCreationTime) {
		this.testCaseExecutionResult = testCaseExecutionResult;
		this.bugTitle = bugTitle;
		this.bugDescription = bugDescription;
		this.bugManagementSystemName = bugManagementSystemName;
		this.bugManagementSystemBugId = bugManagementSystemBugId;
		this.fileBugInBugManagementSystem = fileBugInBugManagementSystem;
		this.bugFilingStatus = bugFilingStatus;
		this.remarks = remarks;
		this.bugCreationTime = bugCreationTime;
		this.modifiedDate=modifiedDate;
	}

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "testExecutionResultBugId", unique = true, nullable = false)
	public Integer getTestExecutionResultBugId() {
		return this.testExecutionResultBugId;
	}

	public void setTestExecutionResultBugId(Integer testExecutionResultBugId) {
		this.testExecutionResultBugId = testExecutionResultBugId;
	}

	@ManyToOne(fetch = FetchType.LAZY,cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH, CascadeType.DETACH})
	@JoinColumn(name = "testCaseExecutionResultId") 
	public TestCaseExecutionResult getTestCaseExecutionResult() {
		return testCaseExecutionResult;
	}

	public void setTestCaseExecutionResult(
			TestCaseExecutionResult testCaseExecutionResult) {
		this.testCaseExecutionResult = testCaseExecutionResult;
	}

	@Column(name = "bugTitle", length = 1000)
	public String getBugTitle() {
		return this.bugTitle;
	}

	public void setBugTitle(String bugTitle) {
		this.bugTitle = bugTitle;
	}

	@Column(name = "bugDescription", length = 1000)
	public String getBugDescription() {
		return this.bugDescription;
	}

	public void setBugDescription(String bugDescription) {
		this.bugDescription = bugDescription;
	}

	@Column(name = "bugManagementSystemName", length = 45)
	public String getBugManagementSystemName() {
		return this.bugManagementSystemName;
	}

	public void setBugManagementSystemName(String bugManagementSystemName) {
		this.bugManagementSystemName = bugManagementSystemName;
	}

	@Column(name = "bugManagementSystemBugId", length = 45)
	public String getBugManagementSystemBugId() {
		return this.bugManagementSystemBugId;
	}

	public void setBugManagementSystemBugId(String bugManagementSystemBugId) {
		this.bugManagementSystemBugId = bugManagementSystemBugId;
	}

	public Date getModifiedDate() {
		return modifiedDate;
	}

	public void setModifiedDate(Date modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

	@Column(name = "fileBugInBugManagementSystem")
	public Boolean getFileBugInBugManagementSystem() {
		return this.fileBugInBugManagementSystem;
	}

	public void setFileBugInBugManagementSystem(
			Boolean fileBugInBugManagementSystem) {
		this.fileBugInBugManagementSystem = fileBugInBugManagementSystem;
	}

	@ManyToOne(fetch = FetchType.LAZY,cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH, CascadeType.DETACH})
	@JoinColumn(name = "bugFilingStatus") 
	public WorkFlow getBugFilingStatus() {
		return this.bugFilingStatus;
	}

	public void setBugFilingStatus(WorkFlow bugFilingStatus) {
		this.bugFilingStatus = bugFilingStatus;
	}

	@ManyToOne(fetch = FetchType.LAZY,cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH, CascadeType.DETACH})
	@JoinColumn(name = "severityId")
	public DefectSeverity getDefectSeverity() {
		return defectSeverity;
	}

	public void setDefectSeverity(DefectSeverity defectSeverity) {
		this.defectSeverity = defectSeverity;
	}

	@Column(name = "remarks", length = 1000)
	public String getRemarks() {
		return this.remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "bugCreationTime")
		public Date getBugCreationTime() {
		return bugCreationTime;
	}

	public void setBugCreationTime(Date bugCreationTime) {
		this.bugCreationTime = bugCreationTime;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "defectTypeId")
	public DefectTypeMaster getDefectType() {
		return defectType;
	}

	public void setDefectType(DefectTypeMaster defectType) {
		this.defectType = defectType;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "foundInStageId")
	public DefectIdentificationStageMaster getDefectFoundStage() {
		return defectFoundStage;
	}

	public void setDefectFoundStage(DefectIdentificationStageMaster defectFoundStage) {
		this.defectFoundStage = defectFoundStage;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "assigneeId")
	public UserList getAssignee() {
		return assignee;
	}

	public void setAssignee(UserList assignee) {
		this.assignee = assignee;
	}

	@Column(name = "ccList")
	public String getCcList() {
		return ccList;
	}

	public void setCcList(String ccList) {
		this.ccList = ccList;
	}

	@Column(name = "isApproved")
	public int getIsApproved() {
		return isApproved;
	}

	public void setIsApproved(int isApproved) {
		this.isApproved = isApproved;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "approvedBy")
	public UserList getApprovedBy() {
		return approvedBy;
	}

	public void setApprovedBy(UserList approvedBy) {
		this.approvedBy = approvedBy;
	}

	@Column(name = "approvedOn")
	public Date getApprovedOn() {
		return approvedOn;
	}

	public void setApprovedOn(Date approvedOn) {
		this.approvedOn = approvedOn;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "approversSeverityId")
	public DefectSeverity getApproversDefectSeverity() {
		return approversDefectSeverity;
	}

	public void setApproversDefectSeverity(DefectSeverity approversDefectSeverity) {
		this.approversDefectSeverity = approversDefectSeverity;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "approvalStatusId")
	public DefectApprovalStatusMaster getDefectApprovalStatus() {
		return defectApprovalStatus;
	}

	public void setDefectApprovalStatus(
			DefectApprovalStatusMaster defectApprovalStatus) {
		this.defectApprovalStatus = defectApprovalStatus;
	}

	@Column(name = "approvalRemarks")
	public String getApprovalRemarks() {
		return approvalRemarks;
	}

	public void setApprovalRemarks(String approvalRemarks) {
		this.approvalRemarks = approvalRemarks;
	}
	
	@Column(name = "uploadFlag")
	public Integer getUploadFlag() {
		return uploadFlag;
	}

	public void setUploadFlag(Integer uploadFlag) {
		this.uploadFlag = uploadFlag;
	}

	@ManyToOne(fetch = FetchType.LAZY,cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH, CascadeType.DETACH})
	@JoinColumn(name = "testStepExecutionResultId") 
	public TestStepExecutionResult getTestStepExecutionResult() {
		return testStepExecutionResult;
	}

	public void setTestStepExecutionResult(
			TestStepExecutionResult testStepExecutionResult) {
		this.testStepExecutionResult = testStepExecutionResult;
	}
	
	@Column(name = "analysedFlag")
	public Integer getAnalysedFlag() {
		return analysedFlag;
	}

	public void setAnalysedFlag(Integer analysedFlag) {
		this.analysedFlag = analysedFlag;
	}
	
	@Column(name = "reportedBy")
	public Integer getReportedBy() {
		return reportedBy;
	}

	public void setReportedBy(Integer reportedBy) {
		this.reportedBy = reportedBy;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "reportedInBuildId")
	public ProductBuild getReportedInBuild() {
		return reportedInBuild;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "fixedInBuildId")
	public ProductBuild getFixedInBuild() {
		return fixedInBuild;
	}

	@Column(name="sourceFilesModifiedForFixing")
	public String getSourceFilesModifiedForFixing() {
		return sourceFilesModifiedForFixing;
	}

	public void setReportedInBuild(ProductBuild reportedInBuild) {
		this.reportedInBuild = reportedInBuild;
	}

	public void setFixedInBuild(ProductBuild fixedInBuild) {
		this.fixedInBuild = fixedInBuild;
	}

	public void setSourceFilesModifiedForFixing(String sourceFilesModifiedForFixing) {
		this.sourceFilesModifiedForFixing = sourceFilesModifiedForFixing;
	}
	
	

}