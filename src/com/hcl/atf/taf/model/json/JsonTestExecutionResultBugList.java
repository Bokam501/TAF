package com.hcl.atf.taf.model.json;


import java.util.Comparator;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.hcl.atf.taf.controller.utilities.DateUtility;
import com.hcl.atf.taf.model.DefectApprovalStatusMaster;
import com.hcl.atf.taf.model.DefectIdentificationStageMaster;
import com.hcl.atf.taf.model.DefectSeverity;
import com.hcl.atf.taf.model.DefectTypeMaster;
import com.hcl.atf.taf.model.EnvironmentCombination;
import com.hcl.atf.taf.model.ExecutionPriority;
import com.hcl.atf.taf.model.ProductBuild;
import com.hcl.atf.taf.model.ProductVersionListMaster;
import com.hcl.atf.taf.model.RunConfiguration;
import com.hcl.atf.taf.model.TestCaseExecutionResult;
import com.hcl.atf.taf.model.TestExecutionResultBugList;
import com.hcl.atf.taf.model.UserList;
import com.hcl.atf.taf.model.WorkFlow;
import com.hcl.atf.taf.model.dto.DefectReportDTO;

public class JsonTestExecutionResultBugList implements java.io.Serializable {
	private static final Log log = LogFactory
			.getLog(JsonTestExecutionResultBugList.class);
	@JsonProperty
	private Integer testExecutionResultBugId;
	@JsonProperty
	private String bugTitle;
	@JsonProperty
	private String bugDescription;
	@JsonProperty
	private String bugManagementSystemName;
	@JsonProperty
	private String bugManagementSystemBugId;
	@JsonProperty
	private boolean fileBugInBugManagementSystem;
	@JsonProperty
	private Integer bugFilingStatusId;
	@JsonProperty
	private String bugFilingStatusName;
	@JsonProperty
	private String remarks;

	@JsonProperty	
	private Integer testCaseExecutionResultId;
	
	@JsonProperty
	private String bugCreationTime;
	@JsonProperty
	private String	bugModifiedTime;
	@JsonProperty
	private String	environments;
	@JsonProperty
	private Integer severityId;
	@JsonProperty
	private String severityName;
	@JsonProperty
	private String runConfiguration;
	@JsonProperty
	private Integer testcaseId;
	@JsonProperty
	private String testcaseName;
	@JsonProperty
	private Integer productVersionId;
	@JsonProperty
	private String productVersionName;
	@JsonProperty
	private String	userId;
	
	@JsonProperty
	private Integer defectTypeId;
	@JsonProperty
	private String defectTypeName;
	@JsonProperty
	private Integer defectIdentifiedInStageId;
	@JsonProperty
	private String defectIdentifiedInStageName;
	@JsonProperty
	private Integer assigneeId;
	@JsonProperty
	private String assigneeName;
	@JsonProperty
	private String ccList;
	@JsonProperty
	private Integer isApproved;
	@JsonProperty
	private Integer approverId;
	@JsonProperty
	private String approverName;
	@JsonProperty
	private String approvedOn;
	@JsonProperty
	private Integer approversDefectSeverityId;
	@JsonProperty
	private String approversDefectSeverityName;
	@JsonProperty
	private Integer defectApprovalStatusId;
	@JsonProperty
	private String defectApprovalStatusName;
	@JsonProperty
	private String approvalRemarks;
	
	
	@JsonProperty
	private Integer testersPriorityId;
	@JsonProperty
	private String testersPriorityName;
	@JsonProperty
	private Integer approversPriorityId;
	@JsonProperty
	private String approversPriorityName;
	
	@JsonProperty
	private String isReproducableOnLive;
	@JsonProperty
	private String isThereABugAlready;
	@JsonProperty
	private String wasOnPrevDayWebRelease;
	
	@JsonProperty
	private String onsiteComments;
	@JsonProperty
	private Integer uploadFlag;
	@JsonProperty
	private Integer analysedFlag;
	@JsonProperty
	private Integer reportedBy;
	@JsonProperty
	private String modifiedDate;
	@JsonProperty
	private Integer reportedInBuildId;
	@JsonProperty
	private String reportedInBuildName;
	@JsonProperty
	private Integer fixedInBuildId;
	@JsonProperty
	private String fixedInBuildName;
	@JsonProperty
	private String sourceFilesModifiedForFixing;
	
	
	public JsonTestExecutionResultBugList() {
	}
	public JsonTestExecutionResultBugList(TestExecutionResultBugList testExecutionResultBugList) {
		
		testExecutionResultBugId=testExecutionResultBugList.getTestExecutionResultBugId();
		bugTitle=testExecutionResultBugList.getBugTitle();
		bugDescription=testExecutionResultBugList.getBugDescription();
		bugManagementSystemName=testExecutionResultBugList.getBugManagementSystemName();
		bugManagementSystemBugId=testExecutionResultBugList.getBugManagementSystemBugId();
		if (testExecutionResultBugList.getFileBugInBugManagementSystem() != null){
			fileBugInBugManagementSystem=testExecutionResultBugList.getFileBugInBugManagementSystem();
		}
		if(testExecutionResultBugList.getBugFilingStatus() != null){
			bugFilingStatusId=testExecutionResultBugList.getBugFilingStatus().getWorkFlowId();
			bugFilingStatusName=testExecutionResultBugList.getBugFilingStatus().getStageName();
		}
		remarks=testExecutionResultBugList.getRemarks();
		if (testExecutionResultBugList.getUploadFlag() != null){
			this.uploadFlag=testExecutionResultBugList.getUploadFlag();	
		}else{
			this.uploadFlag=0;
		}
		
		if (testExecutionResultBugList.getAnalysedFlag() != null){
			this.analysedFlag=testExecutionResultBugList.getAnalysedFlag();	
		}else{
			this.analysedFlag=0;
		}
		if(testExecutionResultBugList.getModifiedDate()!=null){
			this.modifiedDate = DateUtility.dateformatWithOutTime(testExecutionResultBugList.getModifiedDate());
		}
			
			testCaseExecutionResultId=testExecutionResultBugList.getTestCaseExecutionResult().getTestCaseExecutionResultId();
			if (!testExecutionResultBugList.getTestCaseExecutionResult().getTestCaseExecutionResultId().equals(-100)){
				if(testExecutionResultBugList.getTestCaseExecutionResult().getWorkPackageTestCaseExecutionPlan()!=null){
					if(testExecutionResultBugList.getTestCaseExecutionResult().getWorkPackageTestCaseExecutionPlan().getTester() != null){
						this.userId=testExecutionResultBugList.getTestCaseExecutionResult().getWorkPackageTestCaseExecutionPlan().getTester().getLoginId();
						this.assigneeId = testExecutionResultBugList.getTestCaseExecutionResult().getWorkPackageTestCaseExecutionPlan().getTester().getUserId();
					}				
				}
				if(testExecutionResultBugList.getTestCaseExecutionResult().getWorkPackageTestCaseExecutionPlan().getTestCase()!=null){
					this.testcaseId=testExecutionResultBugList.getTestCaseExecutionResult().getWorkPackageTestCaseExecutionPlan().getTestCase().getTestCaseId();
					if(testExecutionResultBugList.getTestCaseExecutionResult().getWorkPackageTestCaseExecutionPlan().getTestCase().getTestCaseName()!=null){
						this.testcaseName=testExecutionResultBugList.getTestCaseExecutionResult().getWorkPackageTestCaseExecutionPlan().getTestCase().getTestCaseName();
					}
					if(testExecutionResultBugList.getTestCaseExecutionResult().getWorkPackageTestCaseExecutionPlan().getWorkPackage().getProductBuild().getProductVersion() != null){
						ProductVersionListMaster pv = testExecutionResultBugList.getTestCaseExecutionResult().getWorkPackageTestCaseExecutionPlan().getWorkPackage().getProductBuild().getProductVersion();
						productVersionId = pv.getProductVersionListId();
						productVersionName = pv.getProductVersionName();
					}
				}
			}
			
			if(testExecutionResultBugList.getBugCreationTime()!=null){
				this.bugCreationTime=DateUtility.sdfDateformatWithOutTime(testExecutionResultBugList.getBugCreationTime());
			}
			if(testExecutionResultBugList.getBugModifiedTime()!=null){
				this.bugModifiedTime = DateUtility.sdfDateformatWithOutTime(testExecutionResultBugList.getBugModifiedTime());
				}
			
			if(testExecutionResultBugList.getDefectSeverity() != null){
				this.severityId = testExecutionResultBugList.getDefectSeverity().getSeverityId();
				this.severityName = testExecutionResultBugList.getDefectSeverity().getSeverityName();
			}else{
				this.severityId = 0;
				this.severityName = null;
			}
			if(testExecutionResultBugList.getTestersPriority() != null){
				this.testersPriorityId = testExecutionResultBugList.getTestersPriority().getExecutionPriorityId();
				this.testersPriorityName = testExecutionResultBugList.getTestersPriority().getExecutionPriorityName();
			}else{
				this.testersPriorityId = 0;
				this.testersPriorityName = null;
			}
			
			if(testExecutionResultBugList.getApproversPriority()!= null){
				this.approversPriorityId = testExecutionResultBugList.getApproversPriority().getExecutionPriorityId();
				this.approversPriorityName = testExecutionResultBugList.getApproversPriority().getExecutionPriorityName();
			}else{
				this.approversPriorityId = 0;
				this.approversPriorityName = null;
			}
			
			
			if(testExecutionResultBugList.getDefectType() != null){
				this.defectTypeId = testExecutionResultBugList.getDefectType().getDefectTypeId();
				this.defectTypeName = testExecutionResultBugList.getDefectType().getDefectTypeName();
			}else{
				this.defectTypeId = 0;
				this.defectTypeName = null;
			}
			
			if(testExecutionResultBugList.getDefectFoundStage() != null){
				this.defectIdentifiedInStageId = testExecutionResultBugList.getDefectFoundStage().getStageId();
				this.defectIdentifiedInStageName = testExecutionResultBugList.getDefectFoundStage().getStageName();
			}else{
				this.defectIdentifiedInStageId = 0;
				this.defectIdentifiedInStageName = null;
			}
		
			if(testExecutionResultBugList.getCcList() != null){
				this.ccList = testExecutionResultBugList.getCcList();
			}
			
			if(testExecutionResultBugList.getApproversDefectSeverity() != null){
				this.approversDefectSeverityId = testExecutionResultBugList.getApproversDefectSeverity().getSeverityId();
				this.approversDefectSeverityName = testExecutionResultBugList.getApproversDefectSeverity().getSeverityName();
			}else{
				this.approversDefectSeverityId = 0;
				this.approversDefectSeverityName = null;
			}
			
			if(testExecutionResultBugList.getDefectApprovalStatus() != null){
				this.defectApprovalStatusId = testExecutionResultBugList.getDefectApprovalStatus().getApprovalStatusId();
				this.defectApprovalStatusName = testExecutionResultBugList.getDefectApprovalStatus().getApprovalStatusName();
			}else{
				this.defectApprovalStatusId = 0;
				this.defectApprovalStatusName = null;
			}
		
			if(testExecutionResultBugList.getApprovedBy() != null){
				this.approverId = testExecutionResultBugList.getApprovedBy().getUserId();
				this.approverName = testExecutionResultBugList.getApprovedBy().getLoginId();
			}else{
				this.approverId = 0;
				this.approverName = null;
			}
			
			if(testExecutionResultBugList.getApprovedOn()!=null){
				this.approvedOn = DateUtility.sdfDateformatWithOutTime(testExecutionResultBugList.getApprovedOn());
			}
			this.isApproved = testExecutionResultBugList.getIsApproved();
			this.approvalRemarks = testExecutionResultBugList.getApprovalRemarks();
			
			this.isReproducableOnLive = testExecutionResultBugList.getIsReproducableOnLive();
			this.onsiteComments = testExecutionResultBugList.getOnsiteComments();
			
			this.isThereABugAlready = testExecutionResultBugList.getIsThereABugAlready();
			this.wasOnPrevDayWebRelease = testExecutionResultBugList.getWasOnPrevDayWebRelease();
			
			if(testExecutionResultBugList.getApprovedOn()!=null){
				this.approvedOn = DateUtility.sdfDateformatWithOutTime(testExecutionResultBugList.getApprovedOn());
			}
			this.isApproved = testExecutionResultBugList.getIsApproved();
			this.isReproducableOnLive = testExecutionResultBugList.getIsReproducableOnLive();
			this.onsiteComments = testExecutionResultBugList.getOnsiteComments();
			this.isThereABugAlready = testExecutionResultBugList.getIsThereABugAlready();
			this.wasOnPrevDayWebRelease = testExecutionResultBugList.getWasOnPrevDayWebRelease();
			this.approvalRemarks = testExecutionResultBugList.getApprovalRemarks();
			if(testExecutionResultBugList != null && testExecutionResultBugList.getFixedInBuild() != null) {
				this.fixedInBuildId = testExecutionResultBugList.getFixedInBuild().getProductBuildId();
				this.fixedInBuildName = testExecutionResultBugList.getFixedInBuild().getBuildname();
			}
			
			if(testExecutionResultBugList !=null && testExecutionResultBugList.getReportedInBuild() != null) {
				this.reportedInBuildId  = testExecutionResultBugList.getReportedInBuild().getProductBuildId();
				this.reportedInBuildName = testExecutionResultBugList.getReportedInBuild().getBuildname();
			}
			
			this.sourceFilesModifiedForFixing = testExecutionResultBugList.getSourceFilesModifiedForFixing();
	}
	
	public JsonTestExecutionResultBugList(TestExecutionResultBugList testExecutionResultBugList,EnvironmentCombination environmentCombination) {
		
		testExecutionResultBugId=testExecutionResultBugList.getTestExecutionResultBugId();
		bugTitle=testExecutionResultBugList.getBugTitle();
		bugDescription=testExecutionResultBugList.getBugDescription();
		bugManagementSystemName=testExecutionResultBugList.getBugManagementSystemName();
		bugManagementSystemBugId=testExecutionResultBugList.getBugManagementSystemBugId();
		fileBugInBugManagementSystem=testExecutionResultBugList.getFileBugInBugManagementSystem();
		bugFilingStatusId=testExecutionResultBugList.getBugFilingStatus().getWorkFlowId();
		bugFilingStatusName=testExecutionResultBugList.getBugFilingStatus().getStageName();
			remarks=testExecutionResultBugList.getRemarks();
			if (testExecutionResultBugList.getUploadFlag() != null){
				this.uploadFlag=testExecutionResultBugList.getUploadFlag();	
			}else{
				this.uploadFlag=0;
			}
			
			if (testExecutionResultBugList.getAnalysedFlag() != null){
				this.analysedFlag=testExecutionResultBugList.getAnalysedFlag();	
			}else{
				this.analysedFlag=0;
			}
			
			testCaseExecutionResultId=testExecutionResultBugList.getTestCaseExecutionResult().getTestCaseExecutionResultId();
			
			if(testExecutionResultBugList.getTestCaseExecutionResult().getWorkPackageTestCaseExecutionPlan().getTestCase()!=null){
				this.testcaseId=testExecutionResultBugList.getTestCaseExecutionResult().getWorkPackageTestCaseExecutionPlan().getTestCase().getTestCaseId();
				this.testcaseName=testExecutionResultBugList.getTestCaseExecutionResult().getWorkPackageTestCaseExecutionPlan().getTestCase().getTestCaseName();
			}
			
			if(testExecutionResultBugList.getModifiedDate()!=null){
				this.modifiedDate = DateUtility.dateformatWithOutTime(testExecutionResultBugList.getModifiedDate());
			}
			
			if(testExecutionResultBugList.getBugCreationTime()!=null){
				
				this.bugCreationTime=DateUtility.sdfDateformatWithOutTime(testExecutionResultBugList.getBugCreationTime());
			}
			if(testExecutionResultBugList.getBugModifiedTime()!=null){
				this.bugModifiedTime = DateUtility.sdfDateformatWithOutTime(testExecutionResultBugList.getBugModifiedTime());
				}

			if(environmentCombination!=null)
				this.environments=environmentCombination.getEnvironmentCombinationName();
			
			if(testExecutionResultBugList.getDefectSeverity() != null){
				this.severityId = testExecutionResultBugList.getDefectSeverity().getSeverityId();				
			}
			
			if(testExecutionResultBugList.getTestersPriority() != null){
				this.testersPriorityId = testExecutionResultBugList.getTestersPriority().getExecutionPriorityId();
				this.testersPriorityName = testExecutionResultBugList.getTestersPriority().getExecutionPriorityName();
			}else{
				this.testersPriorityId = 0;
				this.testersPriorityName = null;
			}
			
			
			if(testExecutionResultBugList.getApproversPriority()!= null){
				this.approversPriorityId = testExecutionResultBugList.getApproversPriority().getExecutionPriorityId();
				this.approversPriorityName = testExecutionResultBugList.getApproversPriority().getExecutionPriorityName();
			}else{
				this.approversPriorityId = 0;
				this.approversPriorityId = null;
			}
			
			
			if(testExecutionResultBugList.getTestCaseExecutionResult().getWorkPackageTestCaseExecutionPlan()!=null){
				this.userId=testExecutionResultBugList.getTestCaseExecutionResult().getWorkPackageTestCaseExecutionPlan().getTester().getLoginId();
			}
			
			if(testExecutionResultBugList.getDefectType() != null){
				this.defectTypeId = testExecutionResultBugList.getDefectType().getDefectTypeId();
				this.defectTypeName = testExecutionResultBugList.getDefectType().getDefectTypeName();
			}else{
				this.defectTypeId = 0;
				this.defectTypeName = null;
			}
			
			if(testExecutionResultBugList.getDefectFoundStage() != null){
				this.defectIdentifiedInStageId = testExecutionResultBugList.getDefectFoundStage().getStageId();
				this.defectIdentifiedInStageName = testExecutionResultBugList.getDefectFoundStage().getStageName();
			}else{
				this.defectIdentifiedInStageId = 0;
				this.defectIdentifiedInStageName = null;
			}
			
			if(testExecutionResultBugList.getCcList() != null){
				this.ccList = testExecutionResultBugList.getCcList();
			}
			
			if(testExecutionResultBugList.getApproversDefectSeverity() != null){
				this.approversDefectSeverityId = testExecutionResultBugList.getApproversDefectSeverity().getSeverityId();
				this.approversDefectSeverityName = testExecutionResultBugList.getApproversDefectSeverity().getSeverityName();
			}else{
				this.approversDefectSeverityId = 0;
				this.approversDefectSeverityName = null;
			}
			
			if(testExecutionResultBugList.getDefectApprovalStatus() != null){
				this.defectApprovalStatusId = testExecutionResultBugList.getDefectApprovalStatus().getApprovalStatusId();
				this.defectApprovalStatusName = testExecutionResultBugList.getDefectApprovalStatus().getApprovalStatusName();
			}else{
				this.defectApprovalStatusId = 0;
				this.defectApprovalStatusName = null;
			}
		
			if(testExecutionResultBugList.getApprovedBy() != null){
				this.approverId = testExecutionResultBugList.getApprovedBy().getUserId();
				this.approverName = testExecutionResultBugList.getApprovedBy().getLoginId();
			}else{
				this.approverId = 0;
				this.approverName = null;
			}
			
			if(testExecutionResultBugList.getApprovedOn()!=null){
				this.approvedOn = DateUtility.sdfDateformatWithOutTime(testExecutionResultBugList.getApprovedOn());
			}
			this.isApproved = testExecutionResultBugList.getIsApproved();
			this.isReproducableOnLive = testExecutionResultBugList.getIsReproducableOnLive();
			this.onsiteComments = testExecutionResultBugList.getOnsiteComments();
			this.isThereABugAlready = testExecutionResultBugList.getIsThereABugAlready();
			this.wasOnPrevDayWebRelease = testExecutionResultBugList.getWasOnPrevDayWebRelease();
			
			this.approvalRemarks = testExecutionResultBugList.getApprovalRemarks();
			
			if(testExecutionResultBugList.getApprovedOn()!=null){
				this.approvedOn = DateUtility.sdfDateformatWithOutTime(testExecutionResultBugList.getApprovedOn());
			}
			this.isApproved = testExecutionResultBugList.getIsApproved();
			this.isReproducableOnLive = testExecutionResultBugList.getIsReproducableOnLive();
			this.onsiteComments = testExecutionResultBugList.getOnsiteComments();
			this.isThereABugAlready = testExecutionResultBugList.getIsThereABugAlready();
			this.wasOnPrevDayWebRelease = testExecutionResultBugList.getWasOnPrevDayWebRelease();
			this.approvalRemarks = testExecutionResultBugList.getApprovalRemarks();
			if(testExecutionResultBugList != null && testExecutionResultBugList.getFixedInBuild() != null) {
				this.fixedInBuildId = testExecutionResultBugList.getFixedInBuild().getProductBuildId();
				this.fixedInBuildName = testExecutionResultBugList.getFixedInBuild().getBuildname();
			}
			
			if(testExecutionResultBugList !=null && testExecutionResultBugList.getReportedInBuild() != null) {
				this.reportedInBuildId  = testExecutionResultBugList.getReportedInBuild().getProductBuildId();
				this.reportedInBuildName = testExecutionResultBugList.getReportedInBuild().getBuildname();
			}
			
			this.sourceFilesModifiedForFixing = testExecutionResultBugList.getSourceFilesModifiedForFixing();
			
	}

public JsonTestExecutionResultBugList(TestExecutionResultBugList testExecutionResultBugList,RunConfiguration runConfiguration) {
		
		testExecutionResultBugId=testExecutionResultBugList.getTestExecutionResultBugId();
		bugTitle=testExecutionResultBugList.getBugTitle();
		bugDescription=testExecutionResultBugList.getBugDescription();
		bugManagementSystemName=testExecutionResultBugList.getBugManagementSystemName();
		bugManagementSystemBugId=testExecutionResultBugList.getBugManagementSystemBugId();
		if(testExecutionResultBugList.getFileBugInBugManagementSystem() != null){
			fileBugInBugManagementSystem=testExecutionResultBugList.getFileBugInBugManagementSystem();	
		}
		
		bugFilingStatusId=testExecutionResultBugList.getBugFilingStatus().getWorkFlowId();
		bugFilingStatusName=testExecutionResultBugList.getBugFilingStatus().getStageName();
			remarks=testExecutionResultBugList.getRemarks();
			if (testExecutionResultBugList.getUploadFlag() != null){
				this.uploadFlag=testExecutionResultBugList.getUploadFlag();	
			}else{
				this.uploadFlag=0;
			}
			
			if (testExecutionResultBugList.getAnalysedFlag() != null){
				this.analysedFlag=testExecutionResultBugList.getAnalysedFlag();	
			}else{
				this.analysedFlag=0;
			}
			
			testCaseExecutionResultId=testExecutionResultBugList.getTestCaseExecutionResult().getTestCaseExecutionResultId();
			if(testExecutionResultBugList.getTestCaseExecutionResult().getWorkPackageTestCaseExecutionPlan()!=null){
				if(testExecutionResultBugList.getTestCaseExecutionResult().getWorkPackageTestCaseExecutionPlan().getTester() != null){
					this.userId=testExecutionResultBugList.getTestCaseExecutionResult().getWorkPackageTestCaseExecutionPlan().getTester().getLoginId();	
				}				
			}
			
			if(testExecutionResultBugList.getTestCaseExecutionResult().getWorkPackageTestCaseExecutionPlan().getTestCase()!=null){
				this.testcaseId=testExecutionResultBugList.getTestCaseExecutionResult().getWorkPackageTestCaseExecutionPlan().getTestCase().getTestCaseId();
				this.testcaseName=testExecutionResultBugList.getTestCaseExecutionResult().getWorkPackageTestCaseExecutionPlan().getTestCase().getTestCaseName();
			}
			
			if(testExecutionResultBugList.getBugCreationTime()!=null){
				
				this.bugCreationTime=DateUtility.sdfDateformatWithOutTime(testExecutionResultBugList.getBugCreationTime());
			}
			if(testExecutionResultBugList.getBugModifiedTime()!=null){
				this.bugModifiedTime = DateUtility.sdfDateformatWithOutTime(testExecutionResultBugList.getBugModifiedTime());
				}
		
			if(runConfiguration!=null)
				this.runConfiguration=runConfiguration.getRunconfigName();
			
			if(testExecutionResultBugList.getDefectSeverity() != null){
				this.severityId = testExecutionResultBugList.getDefectSeverity().getSeverityId();				
			}
			
			if(testExecutionResultBugList.getModifiedDate()!=null){
				this.modifiedDate = DateUtility.dateformatWithOutTime(testExecutionResultBugList.getModifiedDate());
			}
				
			
			if(testExecutionResultBugList.getTestersPriority() != null){
				this.testersPriorityId = testExecutionResultBugList.getTestersPriority().getExecutionPriorityId();
				this.testersPriorityName = testExecutionResultBugList.getTestersPriority().getExecutionPriorityName();
			}else{
				this.testersPriorityId = 0;
				this.testersPriorityName = null;
			}
			
			if(testExecutionResultBugList.getApproversPriority()!= null){
				this.approversPriorityId = testExecutionResultBugList.getApproversPriority().getExecutionPriorityId();
				this.approversPriorityName = testExecutionResultBugList.getApproversPriority().getExecutionPriorityName();
			}else{
				this.approversPriorityId = 0;
				this.approversPriorityName = null;
			}
			
			if(testExecutionResultBugList.getDefectType() != null){
				this.defectTypeId = testExecutionResultBugList.getDefectType().getDefectTypeId();
				this.defectTypeName = testExecutionResultBugList.getDefectType().getDefectTypeName();
			}else{
				this.defectTypeId = 0;
				this.defectTypeName = null;
			}
			
			if(testExecutionResultBugList.getDefectFoundStage() != null){
				this.defectIdentifiedInStageId = testExecutionResultBugList.getDefectFoundStage().getStageId();
				this.defectIdentifiedInStageName = testExecutionResultBugList.getDefectFoundStage().getStageName();
			}else{
				this.defectIdentifiedInStageId = 0;
				this.defectIdentifiedInStageName = null;
			}
			
			if(testExecutionResultBugList.getCcList() != null){
				this.ccList = testExecutionResultBugList.getCcList();
			}
			
			if(testExecutionResultBugList.getApproversDefectSeverity() != null){
				this.approversDefectSeverityId = testExecutionResultBugList.getApproversDefectSeverity().getSeverityId();
				this.approversDefectSeverityName = testExecutionResultBugList.getApproversDefectSeverity().getSeverityName();
			}else{
				this.approversDefectSeverityId = 0;
				this.approversDefectSeverityName = null;
			}
			if(testExecutionResultBugList.getDefectApprovalStatus() != null){
				this.defectApprovalStatusId = testExecutionResultBugList.getDefectApprovalStatus().getApprovalStatusId();
				this.defectApprovalStatusName = testExecutionResultBugList.getDefectApprovalStatus().getApprovalStatusName();
			}else{
				this.defectApprovalStatusId = 0;
				this.defectApprovalStatusName = null;
			}
		
			if(testExecutionResultBugList.getApprovedBy() != null){
				this.approverId = testExecutionResultBugList.getApprovedBy().getUserId();
				this.approverName = testExecutionResultBugList.getApprovedBy().getLoginId();
			}else{
				this.approverId = 0;
				this.approverName = null;
			}
			
			if(testExecutionResultBugList.getApprovedOn()!=null){
				this.approvedOn = DateUtility.sdfDateformatWithOutTime(testExecutionResultBugList.getApprovedOn());
			}
			this.isApproved = testExecutionResultBugList.getIsApproved();
			this.isReproducableOnLive = testExecutionResultBugList.getIsReproducableOnLive();
			this.onsiteComments = testExecutionResultBugList.getOnsiteComments();
			this.isThereABugAlready = testExecutionResultBugList.getIsThereABugAlready();
			this.wasOnPrevDayWebRelease = testExecutionResultBugList.getWasOnPrevDayWebRelease();
			this.approvalRemarks = testExecutionResultBugList.getApprovalRemarks();
			if(testExecutionResultBugList != null && testExecutionResultBugList.getFixedInBuild() != null) {
				this.fixedInBuildId = testExecutionResultBugList.getFixedInBuild().getProductBuildId();
				this.fixedInBuildName = testExecutionResultBugList.getFixedInBuild().getBuildname();
			}
			
			if(testExecutionResultBugList !=null && testExecutionResultBugList.getReportedInBuild() != null) {
				this.reportedInBuildId  = testExecutionResultBugList.getReportedInBuild().getProductBuildId();
				this.reportedInBuildName = testExecutionResultBugList.getReportedInBuild().getBuildname();
			}
			
			this.sourceFilesModifiedForFixing = testExecutionResultBugList.getSourceFilesModifiedForFixing();
			
	}

	public JsonTestExecutionResultBugList(DefectReportDTO  defectReportDTO){
		if(defectReportDTO.getDefectsApprovalStatusId() != null ){
			if(defectReportDTO.getDefectsApprovalStatusId() == 1){
				this.defectApprovalStatusName = "Open";
			}else if(defectReportDTO.getDefectsApprovalStatusId() == 2){
				this.defectApprovalStatusName = "Approved";
			}else if(defectReportDTO.getDefectsApprovalStatusId() == 3){
				this.defectApprovalStatusName = "Corrected Approved";
			}else if(defectReportDTO.getDefectsApprovalStatusId() == 4){
				this.defectApprovalStatusName = "Noise";
			}else if(defectReportDTO.getDefectsApprovalStatusId() == 5){
				this.defectApprovalStatusName = "Voice";
			}else if(defectReportDTO.getDefectsApprovalStatusId() == 6){
				this.defectApprovalStatusName = "Rejected";
			}
		}
	}


	@JsonIgnore
	public TestExecutionResultBugList getTestExecutionResultBugList(){
		
		TestExecutionResultBugList testExecutionResultBugList=new TestExecutionResultBugList();
		
		testExecutionResultBugList.setBugTitle(bugTitle);
		
		testExecutionResultBugList.setBugManagementSystemName(bugManagementSystemName);
		
		WorkFlow workFlow= new WorkFlow();
		workFlow.setWorkFlowId(bugFilingStatusId);
		workFlow.setStageName(bugFilingStatusName);
		
		testExecutionResultBugList.setBugFilingStatus(workFlow);
		
		testExecutionResultBugList.setBugManagementSystemBugId(bugManagementSystemBugId);
		testExecutionResultBugList.setBugDescription(bugDescription);
		testExecutionResultBugList.setFileBugInBugManagementSystem(fileBugInBugManagementSystem);
		testExecutionResultBugList.setTestExecutionResultBugId(testExecutionResultBugId);
		testExecutionResultBugList.setRemarks(remarks);
		testExecutionResultBugList.setModifiedDate(DateUtility.getCurrentTime());
		if(this.bugCreationTime == null || this.bugCreationTime.trim().isEmpty()) {
			testExecutionResultBugList.setBugCreationTime(DateUtility.getCurrentTime());
    	} else {
    	
    		testExecutionResultBugList.setBugCreationTime(DateUtility.dateFormatWithOutTimeStamp(this.bugCreationTime));
    	}
		
		testExecutionResultBugList.setBugModifiedTime(DateUtility.getCurrentTime());
		
		if(this.testCaseExecutionResultId!=null){
			TestCaseExecutionResult testCaseExecutionResult=new TestCaseExecutionResult();
			testCaseExecutionResult.setTestCaseExecutionResultId(testCaseExecutionResultId);
			testExecutionResultBugList.setTestCaseExecutionResult(testCaseExecutionResult);
		}
		if(this.severityId != null && this.severityId >0){
			DefectSeverity defectSeverity = new DefectSeverity();
			defectSeverity.setSeverityId(severityId);
			testExecutionResultBugList.setDefectSeverity(defectSeverity);
		} 
		
		
		if(this.testersPriorityId != null && this.testersPriorityId >0){
			ExecutionPriority executionPriority = new ExecutionPriority();
			executionPriority.setExecutionPriorityId(this.testersPriorityId);
			executionPriority.setExecutionPriorityName(this.testersPriorityName);
			testExecutionResultBugList.setTestersPriority(executionPriority);
		}
		
		if(this.approversPriorityId != null && this.approversPriorityId >0){
			ExecutionPriority approversPriority = new ExecutionPriority();
			approversPriority.setExecutionPriorityId(this.approversPriorityId);
			approversPriority.setExecutionPriorityName(this.approversPriorityName);
			testExecutionResultBugList.setApproversPriority(approversPriority);
		}
		
		
		if(this.defectTypeId != null && this.defectTypeId >0){
			DefectTypeMaster defectType = new DefectTypeMaster();
			defectType.setDefectTypeId(this.defectTypeId);
			testExecutionResultBugList.setDefectType(defectType);
		}
		
		if(this.defectIdentifiedInStageId != null && this.defectIdentifiedInStageId >0){
			DefectIdentificationStageMaster defectIdentifiedStage = new DefectIdentificationStageMaster();
			defectIdentifiedStage.setStageId(this.defectIdentifiedInStageId);
			testExecutionResultBugList.setDefectFoundStage(defectIdentifiedStage);
		}
	
		
		if(this.ccList != null){
			testExecutionResultBugList.setCcList(this.ccList);
		}
		
		if(this.approversDefectSeverityId != null && this.approversDefectSeverityId >0){
			DefectSeverity approversDefectSeverity = new DefectSeverity();
			approversDefectSeverity.setSeverityId(approversDefectSeverityId);
			approversDefectSeverity.setSeverityName(approversDefectSeverityName);
			testExecutionResultBugList.setApproversDefectSeverity(approversDefectSeverity);
		}
		
		if(this.defectApprovalStatusId != null && this.defectApprovalStatusId  >0){
			DefectApprovalStatusMaster defectApprovalStatus = new DefectApprovalStatusMaster();
			defectApprovalStatus.setApprovalStatusId(defectApprovalStatusId);
			defectApprovalStatus.setApprovalStatusName(defectApprovalStatusName);
			testExecutionResultBugList.setDefectApprovalStatus(defectApprovalStatus);
		}
		
		if(this.approverId != null && this.approverId >0){
			UserList approver = new UserList();
			approver.setUserId(this.approverId);
			approver.setLoginId(this.approverName);
		}
	
		if(this.approvedOn == null || this.approvedOn.trim().isEmpty()) {
			testExecutionResultBugList.setApprovedOn(DateUtility.getCurrentTime());
    	} else {
    	
    		testExecutionResultBugList.setApprovedOn(DateUtility.dateFormatWithOutSeconds(this.approvedOn));
    	}
		
		if(this.isApproved != null){
			testExecutionResultBugList.setIsApproved(isApproved);
		}
		
		
		if(this.isReproducableOnLive != null){
			testExecutionResultBugList.setIsReproducableOnLive(isReproducableOnLive);
		}
		
		
		if(this.onsiteComments != null){
			testExecutionResultBugList.setOnsiteComments(onsiteComments);
		}
		
		
		if(this.isThereABugAlready != null){
			testExecutionResultBugList.setIsThereABugAlready(isThereABugAlready);
		}
		if(this.wasOnPrevDayWebRelease != null){
			testExecutionResultBugList.setWasOnPrevDayWebRelease(wasOnPrevDayWebRelease);
		}
	
		if(this.approvalRemarks  != null){
			testExecutionResultBugList.setApprovalRemarks(approvalRemarks);
		}
		
		if(this.uploadFlag  != null){
			testExecutionResultBugList.setUploadFlag(uploadFlag);
		}
		
		if (this.analysedFlag != null){
			testExecutionResultBugList.setAnalysedFlag(analysedFlag);	
		}
		
		if(this.reportedInBuildId != null) {
			ProductBuild reportedIndBuild= new ProductBuild();
			reportedIndBuild.setProductBuildId(this.reportedInBuildId);
			testExecutionResultBugList.setReportedInBuild(reportedIndBuild);
		}
		
		if(this.fixedInBuildId != null ) {
			ProductBuild fixedInBuild = new ProductBuild();
			fixedInBuild.setProductBuildId(this.fixedInBuildId);
			testExecutionResultBugList.setFixedInBuild(fixedInBuild);
		}
		
		testExecutionResultBugList.setSourceFilesModifiedForFixing(this.sourceFilesModifiedForFixing);
		return testExecutionResultBugList;
	}
	public String getBugModifiedTime() {
		return bugModifiedTime;
	}
	public void setBugModifiedTime(String bugModifiedTime) {
		this.bugModifiedTime = bugModifiedTime;
	}
	public Integer getTestExecutionResultBugId() {
		return testExecutionResultBugId;
	}
	public void setTestExecutionResultBugId(Integer testExecutionResultBugId) {
		this.testExecutionResultBugId = testExecutionResultBugId;
	}
	public String getBugTitle() {
		return bugTitle;
	}
	public void setBugTitle(String bugTitle) {
		this.bugTitle = bugTitle;
	}
	public String getBugDescription() {
		return bugDescription;
	}
	public void setBugDescription(String bugDescription) {
		this.bugDescription = bugDescription;
	}
	public String getBugManagementSystemName() {
		return bugManagementSystemName;
	}
	public void setBugManagementSystemName(String bugManagementSystemName) {
		this.bugManagementSystemName = bugManagementSystemName;
	}
	public String getBugManagementSystemBugId() {
		return bugManagementSystemBugId;
	}
	public void setBugManagementSystemBugId(String bugManagementSystemBugId) {
		this.bugManagementSystemBugId = bugManagementSystemBugId;
	}
	public boolean isFileBugInBugManagementSystem() {
		return fileBugInBugManagementSystem;
	}
	public void setFileBugInBugManagementSystem(boolean fileBugInBugManagementSystem) {
		this.fileBugInBugManagementSystem = fileBugInBugManagementSystem;
	}
	
	public Integer getBugFilingStatusId() {
		return bugFilingStatusId;
	}
	public void setBugFilingStatusId(Integer bugFilingStatusId) {
		this.bugFilingStatusId = bugFilingStatusId;
	}
	public String getBugFilingStatusName() {
		return bugFilingStatusName;
	}
	public void setBugFilingStatusName(String bugFilingStatusName) {
		this.bugFilingStatusName = bugFilingStatusName;
	}
	public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	public String getBugCreationTime() {
		return bugCreationTime;
	}
	public void setBugCreationTime(String bugCreationTime) {
		this.bugCreationTime = bugCreationTime;
	}
	public Integer getTestCaseExecutionResultId() {
		return testCaseExecutionResultId;
	}
	public void setTestCaseExecutionResultId(Integer testCaseExecutionResultId) {
		this.testCaseExecutionResultId = testCaseExecutionResultId;
	}
	public String getEnvironments() {
		return environments;
	}
	public void setEnvironments(String environments) {
		this.environments = environments;
	}
	public Integer getSeverityId() {
		return severityId;
	}
	public void setSeverityId(Integer severityId) {
		this.severityId = severityId;
	}
	public String getSeverityName() {
		return severityName;
	}
	public void setSeverityName(String severityName) {
		this.severityName = severityName;
	}
	public String getRunConfiguration() {
		return runConfiguration;
	}
	public void setRunConfiguration(String runConfiguration) {
		this.runConfiguration = runConfiguration;
	}
	public Integer getTestcaseId() {
		return testcaseId;
	}
	public void setTestcaseId(Integer testcaseId) {
		this.testcaseId = testcaseId;
	}
	public String getTestcaseName() {
		return testcaseName;
	}
	public void setTestcaseName(String testcaseName) {
		this.testcaseName = testcaseName;
	}
	public Integer getProductVersionId() {
		return productVersionId;
	}
	public void setProductVersionId(Integer productVersionId) {
		this.productVersionId = productVersionId;
	}
	public String getProductVersionName() {
		return productVersionName;
	}
	public void setProductVersionName(String productVersionName) {
		this.productVersionName = productVersionName;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public Integer getDefectTypeId() {
		return defectTypeId;
	}
	public void setDefectTypeId(Integer defectTypeId) {
		this.defectTypeId = defectTypeId;
	}
	public String getDefectTypeName() {
		return defectTypeName;
	}
	public void setDefectTypeName(String defectTypeName) {
		this.defectTypeName = defectTypeName;
	}
	public Integer getDefectIdentifiedInStageId() {
		return defectIdentifiedInStageId;
	}
	public void setDefectIdentifiedInStageId(Integer defectIdentifiedInStageId) {
		this.defectIdentifiedInStageId = defectIdentifiedInStageId;
	}
	public String getDefectIdentifiedInStageName() {
		return defectIdentifiedInStageName;
	}
	public void setDefectIdentifiedInStageName(String defectIdentifiedInStageName) {
		this.defectIdentifiedInStageName = defectIdentifiedInStageName;
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
	public String getCcList() {
		return ccList;
	}
	public void setCcList(String ccList) {
		this.ccList = ccList;
	}
	public Integer getIsApproved() {
		return isApproved;
	}
	public void setIsApproved(Integer isApproved) {
		this.isApproved = isApproved;
	}
	public Integer getApproverId() {
		return approverId;
	}
	public void setApproverId(Integer approverId) {
		this.approverId = approverId;
	}
	public String getApproverName() {
		return approverName;
	}
	public void setApproverName(String approverName) {
		this.approverName = approverName;
	}
	public String getApprovedOn() {
		return approvedOn;
	}
	public void setApprovedOn(String approvedOn) {
		this.approvedOn = approvedOn;
	}
	public Integer getApproversDefectSeverityId() {
		return approversDefectSeverityId;
	}
	public void setApproversDefectSeverityId(Integer approversDefectSeverityId) {
		this.approversDefectSeverityId = approversDefectSeverityId;
	}
	public String getApproversDefectSeverityName() {
		return approversDefectSeverityName;
	}
	public void setApproversDefectSeverityName(String approversDefectSeverityName) {
		this.approversDefectSeverityName = approversDefectSeverityName;
	}
	public Integer getDefectApprovalStatusId() {
		return defectApprovalStatusId;
	}
	public void setDefectApprovalStatusId(Integer defectApprovalStatusId) {
		this.defectApprovalStatusId = defectApprovalStatusId;
	}
	public String getDefectApprovalStatusName() {
		return defectApprovalStatusName;
	}
	public void setDefectApprovalStatusName(String defectApprovalStatusName) {
		this.defectApprovalStatusName = defectApprovalStatusName;
	}
	public String getApprovalRemarks() {
		return approvalRemarks;
	}
	public void setApprovalRemarks(String approvalRemarks) {
		this.approvalRemarks = approvalRemarks;
	}
	public Integer getTestersPriorityId() {
		return testersPriorityId;
	}
	public void setTestersPriorityId(Integer testersPriorityId) {
		this.testersPriorityId = testersPriorityId;
	}
	public String getTestersPriorityName() {
		return testersPriorityName;
	}
	public void setTestersPriorityName(String testersPriorityName) {
		this.testersPriorityName = testersPriorityName;
	}
	public Integer getApproversPriorityId() {
		return approversPriorityId;
	}
	public void setApproversPriorityId(Integer approversPriorityId) {
		this.approversPriorityId = approversPriorityId;
	}
	public String getApproversPriorityName() {
		return approversPriorityName;
	}
	public void setApproversPriorityName(String approversPriorityName) {
		this.approversPriorityName = approversPriorityName;
	}
	public String getIsReproducableOnLive() {
		return isReproducableOnLive;
	}
	public void setIsReproducableOnLive(String isReproducableOnLive) {
		this.isReproducableOnLive = isReproducableOnLive;
	}
	public String getWasOnPrevDayWebRelease() {
		return wasOnPrevDayWebRelease;
	}
	public void setWasOnPrevDayWebRelease(String wasOnPrevDayWebRelease) {
		this.wasOnPrevDayWebRelease = wasOnPrevDayWebRelease;
	}
	public String getIsThereABugAlready() {
		return isThereABugAlready;
	}
	public void setIsThereABugAlready(String isThereABugAlready) {
		this.isThereABugAlready = isThereABugAlready;
	}
	public String getOnsiteComments() {
		return onsiteComments;
	}
	public void setOnsiteComments(String onsiteComments) {
		this.onsiteComments = onsiteComments;
	}
	
	public Integer getUploadFlag() {
		return uploadFlag;
	}
	public void setUploadFlag(Integer uploadFlag) {
		this.uploadFlag = uploadFlag;
	}

	public static Comparator<JsonTestExecutionResultBugList> jsonTestExecutionResultComparator = new Comparator<JsonTestExecutionResultBugList>() {

		public int compare(JsonTestExecutionResultBugList testbugid1, JsonTestExecutionResultBugList testbugid2) {
			Integer testbug1Id = testbugid1.getTestExecutionResultBugId();
			Integer testbug2Id = testbugid2.getTestExecutionResultBugId();
			return testbug1Id.compareTo(testbug2Id);
		}
	};

	public Integer getAnalysedFlag() {
		return analysedFlag;
	}
	public void setAnalysedFlag(Integer analysedFlag) {
		this.analysedFlag = analysedFlag;
	}
	public Integer getReportedBy() {
		return reportedBy;
	}
	public void setReportedBy(Integer reportedBy) {
		this.reportedBy = reportedBy;
	}
	public Integer getReportedInBuildId() {
		return reportedInBuildId;
	}
	public String getReportedInBuildName() {
		return reportedInBuildName;
	}
	public Integer getFixedInBuildId() {
		return fixedInBuildId;
	}
	public String getFixedInBuildName() {
		return fixedInBuildName;
	}
	public String getSourceFilesModifiedForFixing() {
		return sourceFilesModifiedForFixing;
	}
	public void setReportedInBuildId(Integer reportedInBuildId) {
		this.reportedInBuildId = reportedInBuildId;
	}
	public void setReportedInBuildName(String reportedInBuildName) {
		this.reportedInBuildName = reportedInBuildName;
	}
	public void setFixedInBuildId(Integer fixedInBuildId) {
		this.fixedInBuildId = fixedInBuildId;
	}
	public void setFixedInBuildName(String fixedInBuildName) {
		this.fixedInBuildName = fixedInBuildName;
	}
	public void setSourceFilesModifiedForFixing(String sourceFilesModifiedForFixing) {
		this.sourceFilesModifiedForFixing = sourceFilesModifiedForFixing;
	}
}