package com.hcl.atf.taf.model.json;


import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Set;

import org.json.JSONObject;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.hcl.atf.taf.controller.utilities.DateUtility;
import com.hcl.atf.taf.model.DecouplingCategory;
import com.hcl.atf.taf.model.Environment;
import com.hcl.atf.taf.model.ExecutionPriority;
import com.hcl.atf.taf.model.HostList;
import com.hcl.atf.taf.model.ProductFeature;
import com.hcl.atf.taf.model.TestCaseExecutionResult;
import com.hcl.atf.taf.model.TestCaseList;
import com.hcl.atf.taf.model.TestSuiteList;
import com.hcl.atf.taf.model.UserList;
import com.hcl.atf.taf.model.WorkPackage;
import com.hcl.atf.taf.model.WorkPackageTestCaseExecutionPlan;
import com.hcl.atf.taf.model.WorkShiftMaster;
import com.hcl.atf.taf.model.WorkpackageRunConfiguration;
import com.hcl.atf.taf.model.dto.TestCaseDTO;
import com.hcl.atf.taf.model.dto.WorkPackageTCEPSummaryDTO;


public class JsonWorkPackageTestCaseExecutionPlanForTester implements java.io.Serializable{
	
	@JsonProperty	
	private Integer id;
	@JsonProperty	
	private Integer testcaseId;
	@JsonProperty	
	private String testcaseName;
	@JsonProperty	
	private String testcaseDescription;
	@JsonProperty	
	private String testcaseCode;
	@JsonProperty	
	private Integer environmentId;
	@JsonProperty	
	private String environmentName;
	@JsonProperty
	private Integer workPackageId;
	@JsonProperty	
	private String workPackageName;
	@JsonProperty
	private Integer testLeadId;
	@JsonProperty	
	private String testLeadName;
	@JsonProperty
	private Integer testerId;
	@JsonProperty	
	private String testerName;
	@JsonProperty	
	private String plannedExecutionDate;
	@JsonProperty	
	private String actualExecutionDate;
	@JsonProperty	
	private String createdDate;
	@JsonProperty	
	private String modifiedDate;
	@JsonProperty	
	private Integer isExecuted;
	@JsonProperty	
	private Integer executionStatus;
	@JsonProperty	
	private Integer testCaseExecutionResultId;
	@JsonProperty	
	private String startTime;
	@JsonProperty	
	private String endTime;
	@JsonProperty	
	private String result;
	@JsonProperty	
	private String comments;
	@JsonProperty	
	private String defectsIds;
	@JsonProperty	
	private int defectsCount;
	@JsonProperty
	private String exeType;
	@JsonProperty
	private String runCode;
	@JsonProperty
	private Integer plannedShiftId;
	
	@JsonProperty
	private String plannedShiftName;
	
	@JsonProperty
	private Integer actualShiftId;
	
	@JsonProperty
	private String actualShiftName;
	@JsonProperty
	private Integer isApproved;
	@JsonProperty
	private Integer isReviewed;
	@JsonProperty	
	private Integer localeId;
	
	@JsonProperty	
	private String localeName;
	
	
	@JsonProperty	
	private String testcaseType;
	
	@JsonProperty
	private Integer executionPriorityId;
	
	@JsonProperty	
	private String executionPriorityName;
	@JsonProperty	
	private String executionPriorityDisplayName;
	@JsonProperty	
	private String preconditions;
	@JsonProperty	
	private String testcaseInput;
	@JsonProperty	
	private String expectedOutput;
	@JsonProperty
	private String observedOutput;
	@JsonProperty
	private String evidencePath;
	@JsonProperty
	private String evidenceLabel;
	@JsonProperty	
	private String executionName;
	@JsonProperty	
	private Long executionTime;
	@JsonProperty	
	private int environmentCount;
	@JsonProperty	
	private String environmentCombinationName;
	@JsonProperty	
	private Integer environmentCombinationId;
	@JsonProperty	
	private String overalResult;
	

	@JsonProperty	
	private String runConfigurationName;
	@JsonProperty	
	private Integer runConfigurationId;
	@JsonProperty	
	private Integer testsuiteId;
	
	@JsonProperty	
	private String testsuiteName;

	@JsonProperty	
	private String deviceName;
	@JsonProperty
	private String devplatformTypeName;
	@JsonProperty	
	private Integer deviceId;
	@JsonProperty	
	private String testcasePriority;
	@JsonProperty	
	private Integer hostId;

	@JsonProperty	
	private String hostName;
	@JsonProperty	
	private String hostIPAddress;
	@JsonProperty	
	private String testCaseScriptQualifiedName;
	@JsonProperty	
	private String testCaseScriptFileName;
	@JsonProperty
	private int testCaseCountOfRunconfig;
	@JsonProperty
	private Integer wprunConfigurationId;
	@JsonProperty
	private Integer totalWPTestCase;
	@JsonProperty
	private Integer totalExecutedTesCases;
	@JsonProperty
	private Integer teststepcount;
	@JsonProperty
	private Integer notExecuted;
	@JsonProperty
	private Integer testRunJobId;
	

	@JsonProperty
	private Integer totalPass;
	@JsonProperty
	private Integer totalFail;
	@JsonProperty
	private Integer totalNoRun;
	@JsonProperty
	private Integer totalBlock;
	
	
	@JsonProperty
	private Integer p2totalPass;
	@JsonProperty
	private Integer p2totalFail;
	@JsonProperty
	private Integer p2totalNoRun;
	@JsonProperty
	private Integer p2totalBlock;
	
	@JsonProperty
	private Integer p3totalPass;
	@JsonProperty
	private Integer p3totalFail;
	@JsonProperty
	private Integer p3totalNoRun;
	@JsonProperty
	private Integer p3totalBlock;
	
	@JsonProperty
	private Integer p4totalPass;
	@JsonProperty
	private Integer p4totalFail;
	@JsonProperty
	private Integer p4totalNoRun;
	@JsonProperty
	private Integer p4totalBlock;
	
	@JsonProperty
	private Integer p5totalPass;
	@JsonProperty
	private Integer p5totalFail;
	@JsonProperty
	private Integer p5totalNoRun;
	@JsonProperty
	private Integer p5totalBlock;
	
	@JsonProperty
	private String featureName;
	@JsonProperty
	private String decoupleName;
	@JsonProperty
	private Integer productVersionId;
	@JsonProperty	
	private String productVersionName;
/*	@JsonProperty	
	private Integer productBuildId;*/
	@JsonProperty	
	private String productBuildName;
	@JsonProperty
	private Integer productId;
	@JsonProperty	
	private String productName;
	@JsonProperty
	private Integer productTotalTC;
	@JsonProperty
	private Integer wpPlannedTC;
	@JsonProperty
	private String wpStartEnddayDiff;
	@JsonProperty
	private String wpnthDayfromStrart;
	@JsonProperty
	private Integer totalTcForRowWise;
	@JsonProperty
	private Integer testBedCount;
	@JsonProperty
	private String wpStatus;
	@JsonProperty
	private String executionBeforeAfter;
	@JsonProperty
	private String plannedBeforeAfterCurrentDate;
	@JsonProperty	
	private String firstActualExecutionDate;
	@JsonProperty	
	private String lastActualExecutionDate;
	@JsonProperty	
	private String executedLastHour;
	@JsonProperty
	private Integer totalWpFeature;
	@JsonProperty
	private Integer totalWpTs;	
	@JsonProperty	
	private String sourceType;
	@JsonProperty
	private Integer totalExecutionTCs;
	@JsonProperty
	private String jobStatus;
	@JsonProperty
	private String jobFailureMessage;
	@JsonProperty
	private String jobResult;
	@JsonProperty
	private String wpResult;
	@JsonProperty
	private Integer jobsCount;
	@JsonProperty
	private Integer jobsExecuting;
	@JsonProperty
	private Integer jobsQueued;
	@JsonProperty
	private Integer jobsCompleted;
	@JsonProperty
	private Integer jobsAborted;
	
	@JsonProperty
	private Integer workflowStatusId;
	@JsonProperty
	private String workflowStatusName;
	@JsonProperty
	private Integer assigneeId;
	@JsonProperty
	private String assigneeName;
	@JsonProperty
	private Integer reviewerId;
	@JsonProperty
	private String reviewerName;
	@JsonProperty
	private String analysisRemarks;
	
	@JsonProperty
	private String analysisOutCome;
	
	@JsonProperty
	private Integer mappedFeatureCount;
	@JsonProperty
	private Integer mappedTestscriptCount;
	
	//Testcase Metrics & Mesures
	@JsonProperty
	private String testcaseSuccessRate;
	@JsonProperty
	private String featureCoverage;
	@JsonProperty
	private String buildCoverage;
	@JsonProperty
	private String testCaseAvgExecutionTime;
	@JsonProperty
	private String testCaseAge;
	@JsonProperty
	private String testcaseQualityIndex;
	@JsonProperty
	private String testCasePercentage;
	@JsonProperty
	private String executionMode;	
	@JsonProperty
	private Integer testPlanId;
	@JsonProperty
    private String useIntelligentTestPlan;
	@JsonProperty
	private String testPlanExecutionMode;	
	@JsonProperty
	private String testPlanName;	
	
	public String getExecutionMode() {
		return executionMode;
	}


	public void setExecutionMode(String executionMode) {
		this.executionMode = executionMode;
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

	
	public Integer getTotalWpFeature() {
		return totalWpFeature;
	}
	

	public void setTotalWpFeature(Integer totalWpFeature) {
		this.totalWpFeature = totalWpFeature;
	}

	public Integer getTotalWpTs() {
		return totalWpTs;
	}

	public void setTotalWpTs(Integer totalWpTs) {
		this.totalWpTs = totalWpTs;
	}

	public JsonWorkPackageTestCaseExecutionPlanForTester() {
	}	

	public JsonWorkPackageTestCaseExecutionPlanForTester(WorkPackageTestCaseExecutionPlan workPackageTestCaseExecutionPlan) {
		this.id=workPackageTestCaseExecutionPlan.getId();
		
		if(workPackageTestCaseExecutionPlan.getTestCase()!=null){
			this.testcaseId=workPackageTestCaseExecutionPlan.getTestCase().getTestCaseId();
			this.testcaseName=workPackageTestCaseExecutionPlan.getTestCase().getTestCaseName();
			this.testcaseDescription=workPackageTestCaseExecutionPlan.getTestCase().getTestCaseDescription();
			this.testcaseCode=workPackageTestCaseExecutionPlan.getTestCase().getTestCaseCode();
			if(workPackageTestCaseExecutionPlan.getTestCase().getTestCasePriority() != null){
				this.testcasePriority=workPackageTestCaseExecutionPlan.getTestCase().getTestCasePriority().getPriorityName();
			}
			if(workPackageTestCaseExecutionPlan.getTestRunJob() != null){
				this.testRunJobId = workPackageTestCaseExecutionPlan.getTestRunJob().getTestRunJobId();
			}
			if(workPackageTestCaseExecutionPlan.getTestCase().getTestcaseTypeMaster() != null){
				this.testcaseType=workPackageTestCaseExecutionPlan.getTestCase().getTestcaseTypeMaster().getName();
			}
			this.testcaseInput =workPackageTestCaseExecutionPlan.getTestCase().getTestcaseinput();
			if(workPackageTestCaseExecutionPlan.getTestCase().getTestcaseexpectedoutput() != null){
				this.expectedOutput=workPackageTestCaseExecutionPlan.getTestCase().getTestcaseexpectedoutput();	
			}else{
				this.expectedOutput="";
			}
			
			if(workPackageTestCaseExecutionPlan.getTestCase().getPreconditions() != null){
				this.preconditions=workPackageTestCaseExecutionPlan.getTestCase().getPreconditions();
			}else{
				this.preconditions="";
			}
			
	this.testCaseScriptFileName=workPackageTestCaseExecutionPlan.getTestCase().getTestCaseScriptFileName();
			this.testCaseScriptQualifiedName=workPackageTestCaseExecutionPlan.getTestCase().getTestCaseScriptQualifiedName();
		}
	
		if(workPackageTestCaseExecutionPlan.getTestSuiteList()!=null){
			this.testsuiteId=workPackageTestCaseExecutionPlan.getTestSuiteList().getTestSuiteId();
			this.testsuiteName=workPackageTestCaseExecutionPlan.getTestSuiteList().getTestSuiteName();
		}
	
		if(workPackageTestCaseExecutionPlan.getWorkPackage()!=null){
			this.workPackageId=workPackageTestCaseExecutionPlan.getWorkPackage().getWorkPackageId();
			this.workPackageName=workPackageTestCaseExecutionPlan.getWorkPackage().getName();
			if(workPackageTestCaseExecutionPlan.getWorkPackage().getProductBuild() != null){
				this.productBuildName=workPackageTestCaseExecutionPlan.getWorkPackage().getProductBuild().getBuildname();
				if(workPackageTestCaseExecutionPlan.getWorkPackage().getProductBuild().getProductVersion() != null){
					this.productVersionId = workPackageTestCaseExecutionPlan.getWorkPackage().getProductBuild().getProductVersion().getProductVersionListId();
					this.productVersionName = workPackageTestCaseExecutionPlan.getWorkPackage().getProductBuild().getProductVersion().getProductVersionName();
					if(workPackageTestCaseExecutionPlan.getWorkPackage().getProductBuild().getProductVersion().getProductMaster()!=null){
						this.productName=workPackageTestCaseExecutionPlan.getWorkPackage().getProductBuild().getProductVersion().getProductMaster().getProductName();
					}
					
				}
			}
		}
		
		if(workPackageTestCaseExecutionPlan.getEnvironmentList()!=null){
			Set<Environment> environments=workPackageTestCaseExecutionPlan.getEnvironmentList();
			if(environments.size()!=0){
				environmentCount=environments.size();
				for (Environment environment : environments) {
					if(environmentName==null || environmentName.equals(""))
						environmentName=environment.getEnvironmentName();
					else
						environmentName=environmentName+"-"+environment.getEnvironmentName();
				}
			}else{
				environmentCount=0;
			}
			
		}
		
		
		if(workPackageTestCaseExecutionPlan.getTestLead()!=null){
			this.testLeadId =workPackageTestCaseExecutionPlan.getTestLead().getUserId();
			this.testLeadName =workPackageTestCaseExecutionPlan.getTestLead().getLoginId();
		}else{
			this.testLeadId=0;
			this.testLeadName=null;
		}
		if(workPackageTestCaseExecutionPlan.getTester()!=null){
			this.testerId =workPackageTestCaseExecutionPlan.getTester().getUserId();
			this.testerName =workPackageTestCaseExecutionPlan.getTester().getLoginId();
		}else{
			this.testerId=0;
			this.testerName=null;
		}
		
		if(workPackageTestCaseExecutionPlan.getPlannedExecutionDate()!=null){
			this.plannedExecutionDate=DateUtility.sdfDateformatWithOutTime(workPackageTestCaseExecutionPlan.getPlannedExecutionDate());
		}
		if(workPackageTestCaseExecutionPlan.getActualExecutionDate()!=null){
			this.actualExecutionDate=DateUtility.sdfDateformatWithOutTime(workPackageTestCaseExecutionPlan.getActualExecutionDate());
		}
		
		if(workPackageTestCaseExecutionPlan.getCreatedDate()!=null)
			this.createdDate=DateUtility.sdfDateformatWithOutTime(workPackageTestCaseExecutionPlan.getCreatedDate());
		if(workPackageTestCaseExecutionPlan.getModifiedDate()!=null)
			this.modifiedDate=DateUtility.sdfDateformatWithOutTime(workPackageTestCaseExecutionPlan.getModifiedDate());
		if(workPackageTestCaseExecutionPlan.getExecutionStatus()!=null )
			this.executionStatus=workPackageTestCaseExecutionPlan.getExecutionStatus();
		if(workPackageTestCaseExecutionPlan.getIsExecuted()!=null )
			this.isExecuted=workPackageTestCaseExecutionPlan.getIsExecuted();
		else
			this.isExecuted=0;
		if(workPackageTestCaseExecutionPlan.getTestCaseExecutionResult() != null) {
			this.testCaseExecutionResultId = workPackageTestCaseExecutionPlan.getTestCaseExecutionResult().getTestCaseExecutionResultId();
			this.result = workPackageTestCaseExecutionPlan.getTestCaseExecutionResult().getResult();
			this.comments = workPackageTestCaseExecutionPlan.getTestCaseExecutionResult().getComments();
			this.defectsCount = workPackageTestCaseExecutionPlan.getTestCaseExecutionResult().getDefectsCount();
			this.defectsIds = workPackageTestCaseExecutionPlan.getTestCaseExecutionResult().getDefectIds();
			if(workPackageTestCaseExecutionPlan.getTestCaseExecutionResult().getIsApproved()==null)
				this.isApproved=0;
			else
				this.isApproved=workPackageTestCaseExecutionPlan.getTestCaseExecutionResult().getIsApproved();
			if(workPackageTestCaseExecutionPlan.getTestCaseExecutionResult().getIsReviewed()==null)
				this.isReviewed=0;
			else
				this.isReviewed=workPackageTestCaseExecutionPlan.getTestCaseExecutionResult().getIsReviewed();
			this.observedOutput =workPackageTestCaseExecutionPlan.getTestCaseExecutionResult().getObservedOutput();
			this.executionTime=workPackageTestCaseExecutionPlan.getTestCaseExecutionResult().getExecutionTime();
			
			this.jobFailureMessage=	workPackageTestCaseExecutionPlan.getTestCaseExecutionResult().getFailureReason();

			if(workPackageTestCaseExecutionPlan.getTestCaseExecutionResult().getStartTime() !=null){
				String dt= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(workPackageTestCaseExecutionPlan.getTestCaseExecutionResult().getStartTime());
				this.startTime =dt;
			}
			if(workPackageTestCaseExecutionPlan.getTestCaseExecutionResult().getEndTime()!=null){
				String dt= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(workPackageTestCaseExecutionPlan.getTestCaseExecutionResult().getEndTime());
				this.endTime =dt;
			}			
		}
		if(workPackageTestCaseExecutionPlan.getPlannedWorkShiftMaster()!=null){
			this.plannedShiftId=workPackageTestCaseExecutionPlan.getPlannedWorkShiftMaster().getShiftId();
			this.plannedShiftName=workPackageTestCaseExecutionPlan.getPlannedWorkShiftMaster().getShiftName();
		}else{
			this.plannedShiftId=0;
			this.plannedShiftName="";
		}
		
		if(workPackageTestCaseExecutionPlan.getActualWorkShiftMaster()!=null){
			this.actualShiftId=workPackageTestCaseExecutionPlan.getActualWorkShiftMaster().getShiftId();
			this.actualShiftName=workPackageTestCaseExecutionPlan.getActualWorkShiftMaster().getShiftName();
		}
		
		if(workPackageTestCaseExecutionPlan.getExecutionPriority()!=null){
			this.executionPriorityId=workPackageTestCaseExecutionPlan.getExecutionPriority().getExecutionPriorityId();
			this.executionPriorityName=workPackageTestCaseExecutionPlan.getExecutionPriority().getExecutionPriority();
			this.executionPriorityDisplayName=workPackageTestCaseExecutionPlan.getExecutionPriority().getDisplayName();
			this.executionName=workPackageTestCaseExecutionPlan.getExecutionPriority().getExecutionPriorityName();
		}
		

		if(workPackageTestCaseExecutionPlan.getRunConfiguration()!=null){
			if(workPackageTestCaseExecutionPlan.getRunConfiguration().getRunconfiguration()!=null){

				runConfigurationName=workPackageTestCaseExecutionPlan.getRunConfiguration().getRunconfiguration().getRunconfigName();
				runConfigurationId=workPackageTestCaseExecutionPlan.getRunConfiguration().getWorkpackageRunConfigurationId();
				if(workPackageTestCaseExecutionPlan.getRunConfiguration().getRunconfiguration().getGenericDevice() != null)
					deviceName=workPackageTestCaseExecutionPlan.getRunConfiguration().getRunconfiguration().getGenericDevice().getUDID();
				if(runConfigurationName.contains(":")){
					environmentCombinationName=runConfigurationName.substring(0,runConfigurationName.indexOf(":"));
				}else{
					environmentCombinationName=runConfigurationName;
					
				}
				if(workPackageTestCaseExecutionPlan.getRunConfiguration().getRunconfiguration().getGenericDevice() != null){			
					if(workPackageTestCaseExecutionPlan.getRunConfiguration().getRunconfiguration().getGenericDevice().getPlatformType() != null){
					
						this.devplatformTypeName = workPackageTestCaseExecutionPlan.getRunConfiguration().getRunconfiguration().getGenericDevice().getPlatformType().getName();	
					}else{
						this.devplatformTypeName = "";
				}				
				}else{
					if(workPackageTestCaseExecutionPlan.getWorkPackage().getProductBuild().getProductVersion().getProductMaster().getProductType() != null
						&& workPackageTestCaseExecutionPlan.getWorkPackage().getProductBuild().getProductVersion().getProductMaster().getProductType().getProductTypeId() == 2){					
						this.devplatformTypeName = "N/A";
					}
				}
			}
		}

		if(workPackageTestCaseExecutionPlan.getHostList()!=null){
				this.hostId=workPackageTestCaseExecutionPlan.getHostList().getHostId();
				this.hostName=workPackageTestCaseExecutionPlan.getHostList().getHostName();
				this.hostIPAddress=workPackageTestCaseExecutionPlan.getHostList().getHostIpAddress();
			}
		this.sourceType=workPackageTestCaseExecutionPlan.getSourceType();
		if(workPackageTestCaseExecutionPlan.getTestCase() != null && workPackageTestCaseExecutionPlan.getTestCase().getExecutionTypeMaster() != null){
			if(workPackageTestCaseExecutionPlan.getTestCase().getExecutionTypeMaster().getName() != null)
			this.exeType = workPackageTestCaseExecutionPlan.getTestCase().getExecutionTypeMaster().getName();
		}
		if(workPackageTestCaseExecutionPlan.getFeature() != null){
			ProductFeature productFeature = workPackageTestCaseExecutionPlan.getFeature();
			if(productFeature != null && productFeature.getProductFeatureName() != null){
				this.featureName = productFeature.getProductFeatureName();
			}
		}
		
	}
	public JsonWorkPackageTestCaseExecutionPlanForTester(TestCaseList testcaseList){
		
		this.id=testcaseList.getTestCaseId();
		this.testcaseId=testcaseList.getTestCaseId();
		this.testcaseName=testcaseList.getTestCaseName();
		this.testcaseDescription=testcaseList.getTestCaseDescription();
		this.testcaseCode=testcaseList.getTestCaseCode();
		if(testcaseList.getTestCasePriority() != null){
			this.testcasePriority=testcaseList.getTestCasePriority().getPriorityName();
		}
		if(testcaseList.getTestcaseTypeMaster() != null){
			this.testcaseType=testcaseList.getTestcaseTypeMaster().getName();
		}
		this.testcaseInput =testcaseList.getTestcaseinput();
		if(testcaseList.getTestcaseexpectedoutput() != null){
			this.expectedOutput=testcaseList.getTestcaseexpectedoutput();	
		}else{
			this.expectedOutput="";
		}
		if(testcaseList.getPreconditions() != null){
			this.preconditions=testcaseList.getPreconditions();
		}else{
			this.preconditions="";
		}
		this.testCaseScriptFileName=testcaseList.getTestCaseScriptFileName();
		this.testCaseScriptQualifiedName=testcaseList.getTestCaseScriptQualifiedName();
		
		if(testcaseList.getTestSuiteLists()!=null && !testcaseList.getTestSuiteLists().isEmpty()){
			Set<TestSuiteList> testSuiteSet = testcaseList.getTestSuiteLists();
			String tsID = "";
			String tsName = "";
			for (TestSuiteList testSuiteList : testSuiteSet) {
				tsID = tsID +"||"+ testSuiteList.getTestSuiteId();
				tsName = tsName + "||" + testSuiteList.getTestSuiteName();
			}
			this.testsuiteId=0;
			this.testsuiteName=tsName;
		}
		
		if(testcaseList.getWorkPackageList() != null && !testcaseList.getWorkPackageList().isEmpty()){
			String wpId = "";
			String wpName = "";
			Set<WorkPackage> wpSet = testcaseList.getWorkPackageList();
			for (WorkPackage workPackage : wpSet) {
				wpId = wpId +"||"+workPackage.getWorkPackageId();
				wpName = wpName + "||" +workPackage.getName();
			}
			this.workPackageId=0;
			this.workPackageName=wpName;
		}
		if(testcaseList.getExecutionTypeMaster() != null){
			this.exeType = testcaseList.getExecutionTypeMaster().getName();
		}
		
		if(testcaseList.getWorkPackageTestCaseExecutionPlan() != null && !testcaseList.getWorkPackageTestCaseExecutionPlan().isEmpty()){
			if(testcaseList.getWorkPackageTestCaseExecutionPlan().iterator().next().getWorkPackage() != null){
				this.workPackageId=testcaseList.getWorkPackageTestCaseExecutionPlan().iterator().next().getWorkPackage().getWorkPackageId();
				this.workPackageName=testcaseList.getWorkPackageTestCaseExecutionPlan().iterator().next().getWorkPackage().getName();
				if(testcaseList.getWorkPackageTestCaseExecutionPlan().iterator().next().getWorkPackage().getProductBuild() != null){
					this.productBuildName=testcaseList.getWorkPackageTestCaseExecutionPlan().iterator().next().getWorkPackage().getProductBuild().getBuildname();
					if(testcaseList.getWorkPackageTestCaseExecutionPlan().iterator().next().getWorkPackage().getProductBuild().getProductVersion() != null){
						this.productVersionId = testcaseList.getWorkPackageTestCaseExecutionPlan().iterator().next().getWorkPackage().getProductBuild().getProductVersion().getProductVersionListId();
						this.productVersionName = testcaseList.getWorkPackageTestCaseExecutionPlan().iterator().next().getWorkPackage().getProductBuild().getProductVersion().getProductVersionName();
						if(testcaseList.getWorkPackageTestCaseExecutionPlan().iterator().next().getWorkPackage().getProductBuild().getProductVersion().getProductMaster() != null){
							this.productName = testcaseList.getWorkPackageTestCaseExecutionPlan().iterator().next().getWorkPackage().getProductBuild().getProductVersion().getProductMaster().getProductName();
						}
					}
				}
			}
		}
		if(testcaseList.getStatus() != null ){
			this.executionStatus = testcaseList.getStatus();
		}
		
		if(testcaseList.getTotalEffort() != null)
			this.totalWpTs = testcaseList.getTotalEffort();
		
		if(testcaseList.getTestCaseCreatedDate() != null)
			this.createdDate = DateUtility.sdfDateformatWithOutTime(testcaseList.getTestCaseCreatedDate());
		
		if(testcaseList.getTestCaseLastUpdatedDate() != null)
			this.modifiedDate = DateUtility.sdfDateformatWithOutTime(testcaseList.getTestCaseLastUpdatedDate());
		
		if(testcaseList.getTestCaseSource() != null)
			this.sourceType = testcaseList.getTestCaseSource();
		
		this.mappedFeatureCount = testcaseList.getMappedFeatueCount();
		this.mappedTestscriptCount = testcaseList.getMappedTestscriptCount();
		
		}
	public JsonWorkPackageTestCaseExecutionPlanForTester(TestCaseDTO testCaseDTO) {
		this.totalTcForRowWise=0;
		if(testCaseDTO.getTestCaseId()!=null){
			this.testcaseId=testCaseDTO.getTestCaseId();
		}else{
			this.testcaseId=0;
		}
		if(testCaseDTO.getWorkPackageId()!=null){
			this.workPackageId=testCaseDTO.getWorkPackageId();
		}
		
			this.testerName=testCaseDTO.getTesterName();
			this.testcaseName=testCaseDTO.getTestCaseName();
			this.runConfigurationId=testCaseDTO.getRunConfigurationId();
			this.wprunConfigurationId=testCaseDTO.getWprunConfigurationId();
			this.testRunJobId=testCaseDTO.getTestRunJobId();
			this.runConfigurationName=testCaseDTO.getRunConfigurationName();
			this.testcaseDescription=testCaseDTO.getTestCaseDescription();
			this.testcaseCode=testCaseDTO.getTestCaseCode();
			this.environmentCombinationName=testCaseDTO.getEnvCombName();
			this.deviceName=testCaseDTO.getDeviceName();
			if(testCaseDTO.getExecutionPriorityId()!=null){
			if(testCaseDTO.getExecutionPriorityId()==5){
			this.executionPriorityName="P0";
			}
			else if(testCaseDTO.getExecutionPriorityId()==4){
			this.executionPriorityName="P1";
			}else if(testCaseDTO.getExecutionPriorityId()==3){
			this.executionPriorityName="P2";
			}else if(testCaseDTO.getExecutionPriorityId()==2){
			this.executionPriorityName="P3";
			}else if(testCaseDTO.getExecutionPriorityId()==1){
			this.executionPriorityName="P4";
			}
	}
			if(testCaseDTO.getJobFailureMessage()!=null){
				jobFailureMessage=testCaseDTO.getJobFailureMessage();
			}
			if(testCaseDTO.getJobStatus()!=null){
				jobStatus=testCaseDTO.getJobStatus();
			}
			if(testCaseDTO.getJobResult()!=null){
				jobResult=testCaseDTO.getJobResult();
			}
			if(testCaseDTO.getWpResult()!=null){
				wpResult=testCaseDTO.getWpResult();
			}
			if(testCaseDTO.getEnvironmentCount()!=null){
				this.environmentCount=(Integer)testCaseDTO.getEnvironmentCount().intValue();
			}else{
				this.environmentCount=0;
			}
			if(testCaseDTO.getTotalWpFeature()!=null){
				this.totalWpFeature=testCaseDTO.getTotalWpFeature();
			}
			if(testCaseDTO.getTotalWptestSuite()!=null){
				this.totalWpTs=testCaseDTO.getTotalWptestSuite();
			}
			if(testCaseDTO.getWpStatus()!=null){
				wpStatus=testCaseDTO.getWpStatus();
			}
			if(testCaseDTO.getWpName()!=null){
				workPackageName=testCaseDTO.getWpName();
			}
			
			this.testcaseInput =testCaseDTO.getTestcaseinput();
			this.expectedOutput=testCaseDTO.getTestcaseexpectedoutput();
			this.preconditions=testCaseDTO.getPreconditions();
			this.observedOutput=testCaseDTO.getObservedOutput();
			if(testCaseDTO.getDefectsCount()!=null){
				this.defectsCount=(Integer)testCaseDTO.getDefectsCount().intValue();
			}else{
				this.defectsCount=0;
			}
			if(testCaseDTO.getTestCaseCountOfRunconfig()!=null){
				this.testCaseCountOfRunconfig=(Integer)testCaseDTO.getTestCaseCountOfRunconfig().intValue();
			}
			this.totalExecutedTesCases=(Integer)testCaseDTO.getTotalExecutedTesCases();
			this.totalWPTestCase=(Integer)testCaseDTO.getTotalWPTestCase();
			this.totalPass=(Integer)testCaseDTO.getTotalPass();
			this.totalFail=(Integer)testCaseDTO.getTotalFail();
			this.totalNoRun=(Integer)testCaseDTO.getTotalNoRun();
			this.totalBlock=(Integer)testCaseDTO.getTotalBlock();
			this.notExecuted=(Integer)testCaseDTO.getNotExecuted();
			if(testCaseDTO.getTotalExecutionPlanTestCase()!=null){
				this.totalExecutionTCs=testCaseDTO.getTotalExecutionPlanTestCase();
			}
		Integer testCaseExeResArr[]=testCaseDTO.getTestCaseExeResArr();
		if(testCaseExeResArr!=null){
			this.totalPass=testCaseExeResArr[0];
			this.totalFail=testCaseExeResArr[1];
			this.totalNoRun=testCaseExeResArr[2];
			this.totalBlock=testCaseExeResArr[3];
			this.defectsCount=testCaseExeResArr[4];
			this.overalResult="Not Executed";
			if(totalPass!=0){
				this.overalResult="Pass";
			}
			if(totalBlock!=0 || totalNoRun!=0){
				this.overalResult="Not Completed";
			}
			if(totalFail!=0){
				this.overalResult="Fail";
			}
		}
		if(testCaseDTO.getTestSuiteName()!=null){
			this.testsuiteName=testCaseDTO.getTestSuiteName();
		}else{
			this.testsuiteName="";
		}
		
			if(testCaseDTO.getExecutionPriorityId()!=null ){
				this.totalTcForRowWise=totalPass+totalFail+totalNoRun+totalBlock;
			}
			ProductFeature pf=testCaseDTO.getProductFeature();
			DecouplingCategory decouple=testCaseDTO.getDecuplingCategory();
			if(decouple!=null){
				this.decoupleName=decouple.getDecouplingCategoryName();
				}
			if(pf!=null){
			this.featureName=pf.getProductFeatureName();
			}else{
				this.featureName="";
			}
			int prirityArr[][]=new int[5][4];
			if(testCaseDTO.getFeaturepriotiesArry()!=null){
				prirityArr=(int[][])testCaseDTO.getFeaturepriotiesArry();
			}else if(testCaseDTO.getDeCouplepriotiesArry()!=null){
				prirityArr=null;
			}else{
				prirityArr=null;
			}
			 
			if(prirityArr!=null){
				this.totalTcForRowWise=0;
					this.p5totalPass=prirityArr[0][0];
					this.p5totalFail=prirityArr[0][1];
					this.p5totalNoRun=prirityArr[0][2];
					this.p5totalBlock=prirityArr[0][3];
					
					this.p4totalPass=prirityArr[1][0];
					this.p4totalFail=prirityArr[1][1];
					this.p4totalNoRun=prirityArr[1][2];
					this.p4totalBlock=prirityArr[1][3];
					
					this.p3totalPass=prirityArr[2][0];
					this.p3totalFail=prirityArr[2][1];
					this.p3totalNoRun=prirityArr[2][2];
					this.p3totalBlock=prirityArr[2][3];
					
					this.p2totalPass=prirityArr[3][0];
					this.p2totalFail=prirityArr[3][1];
					this.p2totalNoRun=prirityArr[3][2];
					this.p2totalBlock=prirityArr[3][3];
					
							this.totalPass=prirityArr[4][0];
							this.totalFail=prirityArr[4][1];
							this.totalNoRun=prirityArr[4][2];
							this.totalBlock=prirityArr[4][3];
							
						this.totalTcForRowWise=prirityArr[0][0]+prirityArr[0][1]+prirityArr[0][2]+prirityArr[0][3]+prirityArr[1][0]+prirityArr[1][1]+prirityArr[1][2]+prirityArr[1][3]+prirityArr[2][0]+prirityArr[2][1]+prirityArr[2][2]+prirityArr[2][3]+prirityArr[3][0]+prirityArr[3][1]+prirityArr[3][2]+prirityArr[3][3]+prirityArr[4][0]+prirityArr[4][1]+prirityArr[4][2]+prirityArr[4][3];	
					
				
				
		}
			
			this.testcaseSuccessRate = testCaseDTO.getTestcaseSuccessRate();
			this.featureCoverage = testCaseDTO.getFeatureCoverage();
			this.buildCoverage = testCaseDTO.getBuildCoverage();
			this.testCaseAge = testCaseDTO.getTestCaseAge();
			this.testCasePercentage = testCaseDTO.getTestCasePercentage();
			this.testCaseAvgExecutionTime = testCaseDTO.getTestCaseAvgExecutionTime();
			this.testcaseQualityIndex = testCaseDTO.getTestcaseQualityIndex();
			
		}
	
		@JsonIgnore
		public JSONObject getCleanJson() {
			
			JSONObject responseJson = new JSONObject();
			try {
				responseJson.put("testCaseExecutionResultId", testCaseExecutionResultId);
				responseJson.put("actualExecutionDate", actualExecutionDate);
				responseJson.put("testcaseCode", testcaseCode);
				responseJson.put("testcaseId", testcaseId);
				responseJson.put("testcaseName", testcaseName);
				responseJson.put("runConfigurationName", runConfigurationName);
				responseJson.put("result", result);
				responseJson.put("startTime", startTime);
				responseJson.put("defectsCount", defectsCount);
				responseJson.put("testerName", testerName);
				responseJson.put("endTime", endTime);
				responseJson.put("testRunJobId", testRunJobId);
				responseJson.put("workPackageId", workPackageId);
				responseJson.put("teststepcount", teststepcount);
				responseJson.put("productName", productName);
				responseJson.put("exeType", exeType);
				responseJson.put("productVersionName", productVersionName);
				responseJson.put("productBuildName", productBuildName);
				responseJson.put("workPackageName", workPackageName);
			} catch (Exception e){
	
			}
			return responseJson;
		}
	public JsonWorkPackageTestCaseExecutionPlanForTester(TestCaseDTO testCaseDTO, String wpsummaryfilter) {	
		
		if(testCaseDTO.getTestRunJobId()!=null){
			this.testRunJobId=testCaseDTO.getTestCaseId();
		}else{
			this.testRunJobId=0;
		}
		
		if(testCaseDTO.getTestCaseId()!=null){
			this.testcaseId=testCaseDTO.getTestCaseId();
		}else{
			this.testcaseId=0;
		}
		if(testCaseDTO.getTestCaseName()!=null){
			this.testcaseName=testCaseDTO.getTestCaseName();
		}else{
			this.testcaseName="";
		}
		if(testCaseDTO.getTestCaseCode()!=null){
			this.testcaseCode=testCaseDTO.getTestCaseCode();
		}else{
			this.testcaseCode="";
		}
		if(testCaseDTO.getWptcepId()!=null){
			this.testCaseExecutionResultId=testCaseDTO.getWptcepId();
		}else{
			this.testCaseExecutionResultId=0;
		}
		if(testCaseDTO.getStartTime() !=null){
			String dt= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(testCaseDTO.getStartTime());
			this.startTime =dt;
		}
		if(testCaseDTO.getEndTime()!=null){
			String dt= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(testCaseDTO.getEndTime());
			this.endTime =dt;
		}
		if(testCaseDTO.getWptcepId()!=null){
			this.testCaseExecutionResultId=testCaseDTO.getWptcepId();
		}else{
			this.testCaseExecutionResultId=0;
		}
		
		
		if(testCaseDTO.getActualExecutionDate()!=null){
			this.actualExecutionDate=testCaseDTO.getActualExecutionDate().toString();
		}else{
			this.actualExecutionDate="";
		}
		if(testCaseDTO.getTesterName()!=null){
			this.testerName=testCaseDTO.getTesterName();
		}else{
			this.testerName="";
		}
		if(testCaseDTO.getResult()!=null){
			this.result=testCaseDTO.getResult();
		}else{
			this.result="";
		}
		if(testCaseDTO.getTcerComments()!=null){
			this.comments=testCaseDTO.getTcerComments();
		}else{
			this.comments="";
		}
		if(testCaseDTO.getTestRunJobId()!=null){
			this.testRunJobId=testCaseDTO.getTestRunJobId();
		}
		if(testCaseDTO.getRunConfigurationName()!=null){
			this.runConfigurationName=testCaseDTO.getRunConfigurationName();
		}else{
			this.runConfigurationName="";
		}		
		if(testCaseDTO.getTeststepcount() != null){
			this.teststepcount = testCaseDTO.getTeststepcount();
		}
		if(testCaseDTO.getDefectsCount() != null){
			this.defectsCount = testCaseDTO.getDefectsCount().intValue();
		}else{
			this.defectsCount = 0;
		}
		
			this.assigneeId = testCaseDTO.getAssigneeId();
			this.assigneeName = testCaseDTO.getAssigneeName();
		
			this.reviewerId = testCaseDTO.getReviewerId();
			this.reviewerName = testCaseDTO.getReviewerName();
		
			this.workflowStatusId = testCaseDTO.getWorkflowStatusId();
			this.workflowStatusName = testCaseDTO.getWorkflowStatusName();
			this.analysisRemarks = testCaseDTO.getAnalysisRemarks();
		
		
		
	}
	
	public JsonWorkPackageTestCaseExecutionPlanForTester(WorkPackageTCEPSummaryDTO wtceptSummaryDTO){
		this.productName = wtceptSummaryDTO.getProductName();
		this.workPackageId = wtceptSummaryDTO.getWorkPackageId();
		this.workPackageName = wtceptSummaryDTO.getWorkPackageName();
		this.wpStatus = "---";
		this.wpStatus = wtceptSummaryDTO.getWorkFlowStageName();//WP Stage Name
		this.wpStartEnddayDiff=wtceptSummaryDTO.getWpStartEnddayDiff().toString();
		if(wtceptSummaryDTO.getActualStartDate() != null)
		this.firstActualExecutionDate = wtceptSummaryDTO.getActualStartDate().toString();
		
		if(wtceptSummaryDTO.getActualEndDate() != null)
		this.lastActualExecutionDate = wtceptSummaryDTO.getActualEndDate().toString();
		
		this.totalWPTestCase = wtceptSummaryDTO.getTestCaseCount();
		this.totalWpTs = wtceptSummaryDTO.getTestSuiteCount();
		this.totalWpFeature = wtceptSummaryDTO.getFeatueCount();
		this.jobStatus = wtceptSummaryDTO.getJobCount().toString();//jobCount
		this.jobsCount = wtceptSummaryDTO.getJobCount();
		this.jobsExecuting = wtceptSummaryDTO.getJobsExecuting();
		this.jobsQueued = wtceptSummaryDTO.getJobsQueued();
		this.jobsAborted = wtceptSummaryDTO.getJobsAborted();
		this.jobsCompleted = wtceptSummaryDTO.getJobsCompleted();		
		this.defectsCount = wtceptSummaryDTO.getDefectsCount();
		this.exeType = wtceptSummaryDTO.getExeType();
		this.p2totalPass = wtceptSummaryDTO.getPassedCount();
		this.p2totalFail = wtceptSummaryDTO.getFailedCount();
		this.p2totalBlock = wtceptSummaryDTO.getBlockedCount();
		this.p2totalNoRun = wtceptSummaryDTO.getNorunCount();
		this.notExecuted = wtceptSummaryDTO.getNotexecutedCount();
		/*this.notExecuted = this.totalWPTestCase - (this.p2totalPass +  this.p2totalFail);
		this.p2totalNoRun = this.notExecuted;
		*/this.totalExecutedTesCases = p2totalPass+p2totalFail+p2totalBlock+p2totalNoRun;
		this.testCaseCountOfRunconfig = p2totalPass+p2totalFail+p2totalBlock+p2totalNoRun+notExecuted;
		this.teststepcount = wtceptSummaryDTO.getTeststepcount();
		this.result = "---";
		if(wpStatus != null){
			if(wpStatus.equalsIgnoreCase("Completed")){
				if(jobsAborted  > 0 ){
					this.result = "Failed";
				}else if(jobsCompleted > 0){
					if(p2totalFail >0 || p2totalBlock >0 || p2totalNoRun > 0){
						this.result = "Failed";
					}else if(p2totalPass >0){
						this.result = "Passed";
					}else{
						this.result = "Failed";
					}
				}						
			}else if(wpStatus.equalsIgnoreCase("Aborted")){				
				this.result = "Failed";										
			}else if(wpStatus.equalsIgnoreCase("Execution")){
				if(p2totalFail > 0)
					this.result = "Failed";
				else if(p2totalPass > 0)
					this.result = "Passed [Intermediate]";
				else
					this.result = "---";
			} 	else if(wpStatus.equalsIgnoreCase("Restarted")){				
				this.result = "Restart";										
			}
		}else{
			this.result = "---";
			this.wpStatus = "---";
		}
		 
	}
		

	public JsonWorkPackageTestCaseExecutionPlanForTester(
			WorkPackageTestCaseExecutionPlan workPackageTestCaseExecutionPlan,
			String filter) {
		// TODO Auto-generated constructor stub

		this.id=workPackageTestCaseExecutionPlan.getId();
		
		if(workPackageTestCaseExecutionPlan.getTestCase()!=null){
			this.testcaseId=workPackageTestCaseExecutionPlan.getTestCase().getTestCaseId();
			this.testcaseName=workPackageTestCaseExecutionPlan.getTestCase().getTestCaseName();
			this.testcaseDescription=workPackageTestCaseExecutionPlan.getTestCase().getTestCaseDescription();
			this.testcaseCode=workPackageTestCaseExecutionPlan.getTestCase().getTestCaseCode();
			if(workPackageTestCaseExecutionPlan.getTestCase().getTestCasePriority() != null){
				this.testcasePriority=workPackageTestCaseExecutionPlan.getTestCase().getTestCasePriority().getPriorityName();
			}
			if(workPackageTestCaseExecutionPlan.getTestRunJob() != null){
				this.testRunJobId = workPackageTestCaseExecutionPlan.getTestRunJob().getTestRunJobId();
			}
			if(workPackageTestCaseExecutionPlan.getTestCase().getTestcaseTypeMaster() != null){
				this.testcaseType=workPackageTestCaseExecutionPlan.getTestCase().getTestcaseTypeMaster().getName();
			}
			this.testcaseInput =workPackageTestCaseExecutionPlan.getTestCase().getTestcaseinput();
			if(workPackageTestCaseExecutionPlan.getTestCase().getTestcaseexpectedoutput() != null){
				this.expectedOutput=workPackageTestCaseExecutionPlan.getTestCase().getTestcaseexpectedoutput();	
			}else{
				this.expectedOutput="";
			}
			
			if(workPackageTestCaseExecutionPlan.getTestCase().getPreconditions() != null){
				this.preconditions=workPackageTestCaseExecutionPlan.getTestCase().getPreconditions();
			}else{
				this.preconditions="";
			}
			
	this.testCaseScriptFileName=workPackageTestCaseExecutionPlan.getTestCase().getTestCaseScriptFileName();
			this.testCaseScriptQualifiedName=workPackageTestCaseExecutionPlan.getTestCase().getTestCaseScriptQualifiedName();
		}
	
		if(workPackageTestCaseExecutionPlan.getTestSuiteList()!=null){
			this.testsuiteId=workPackageTestCaseExecutionPlan.getTestSuiteList().getTestSuiteId();
			this.testsuiteName=workPackageTestCaseExecutionPlan.getTestSuiteList().getTestSuiteName();
		}
	
		if(workPackageTestCaseExecutionPlan.getWorkPackage()!=null){
			this.workPackageId=workPackageTestCaseExecutionPlan.getWorkPackage().getWorkPackageId();
			this.workPackageName=workPackageTestCaseExecutionPlan.getWorkPackage().getName();
			if(workPackageTestCaseExecutionPlan.getWorkPackage().getProductBuild() != null){
				this.productBuildName=workPackageTestCaseExecutionPlan.getWorkPackage().getProductBuild().getBuildname();
				if(workPackageTestCaseExecutionPlan.getWorkPackage().getProductBuild().getProductVersion() != null){
					this.productVersionId = workPackageTestCaseExecutionPlan.getWorkPackage().getProductBuild().getProductVersion().getProductVersionListId();
					this.productVersionName = workPackageTestCaseExecutionPlan.getWorkPackage().getProductBuild().getProductVersion().getProductVersionName();
					if(workPackageTestCaseExecutionPlan.getWorkPackage().getProductBuild().getProductVersion().getProductMaster()!=null){
						this.productName=workPackageTestCaseExecutionPlan.getWorkPackage().getProductBuild().getProductVersion().getProductMaster().getProductName();
					}
					
				}
			}
		}
	
		if(workPackageTestCaseExecutionPlan.getEnvironmentList()!=null && !workPackageTestCaseExecutionPlan.getEnvironmentList().isEmpty()){
			Set<Environment> environments=workPackageTestCaseExecutionPlan.getEnvironmentList();
			if(environments.size()!=0){
				environmentCount=environments.size();
				for (Environment environment : environments) {
					if(environmentName==null || environmentName.equals(""))
						environmentName=environment.getEnvironmentName();
					else
						environmentName=environmentName+"-"+environment.getEnvironmentName();
				}
			}else{
				environmentCount=0;
			}			
		}else{
			if(workPackageTestCaseExecutionPlan.getRunConfiguration() != null){
				if(workPackageTestCaseExecutionPlan.getRunConfiguration().getRunconfiguration() != null){
					if(workPackageTestCaseExecutionPlan.getRunConfiguration().getRunconfiguration().getEnvironmentcombination()!= null){
						environmentName = workPackageTestCaseExecutionPlan.getRunConfiguration().getRunconfiguration().getEnvironmentcombination().getEnvironmentCombinationName();
					}
				}
			}		
		}
		
		
		if(workPackageTestCaseExecutionPlan.getTestLead()!=null){
			this.testLeadId =workPackageTestCaseExecutionPlan.getTestLead().getUserId();
			this.testLeadName =workPackageTestCaseExecutionPlan.getTestLead().getLoginId();
		}else{
			this.testLeadId=0;
			this.testLeadName=null;
		}
		if(workPackageTestCaseExecutionPlan.getTester()!=null){
			this.testerId =workPackageTestCaseExecutionPlan.getTester().getUserId();
			this.testerName =workPackageTestCaseExecutionPlan.getTester().getLoginId();
		}else{
			this.testerId=0;
			this.testerName=null;
		}
		
		if(workPackageTestCaseExecutionPlan.getPlannedExecutionDate()!=null){
			this.plannedExecutionDate=DateUtility.sdfDateformatWithOutTime(workPackageTestCaseExecutionPlan.getPlannedExecutionDate());
		}
		if(workPackageTestCaseExecutionPlan.getActualExecutionDate()!=null){
			this.actualExecutionDate=DateUtility.sdfDateformatWithOutTime(workPackageTestCaseExecutionPlan.getActualExecutionDate());
		}
		
		if(workPackageTestCaseExecutionPlan.getCreatedDate()!=null)
			this.createdDate=DateUtility.sdfDateformatWithOutTime(workPackageTestCaseExecutionPlan.getCreatedDate());
		if(workPackageTestCaseExecutionPlan.getModifiedDate()!=null)
			this.modifiedDate=DateUtility.sdfDateformatWithOutTime(workPackageTestCaseExecutionPlan.getModifiedDate());
		if(workPackageTestCaseExecutionPlan.getExecutionStatus()!=null )
			this.executionStatus=workPackageTestCaseExecutionPlan.getExecutionStatus();
		if(workPackageTestCaseExecutionPlan.getIsExecuted()!=null )
			this.isExecuted=workPackageTestCaseExecutionPlan.getIsExecuted();
		else
			this.isExecuted=0;
		if(workPackageTestCaseExecutionPlan.getTestCaseExecutionResult() != null) {
			this.testCaseExecutionResultId = workPackageTestCaseExecutionPlan.getTestCaseExecutionResult().getTestCaseExecutionResultId();
			String res = workPackageTestCaseExecutionPlan.getTestCaseExecutionResult().getResult();
			if(filter != null && filter.equalsIgnoreCase("EOD")){
				if(res.equalsIgnoreCase("PASSED")){
					this.result = "1";
				}else if(res.equalsIgnoreCase("FAILED")){
					this.result = "2";
				}
			}else{
				this.result = workPackageTestCaseExecutionPlan.getTestCaseExecutionResult().getResult();	
			}		
			if(workPackageTestCaseExecutionPlan.getTestCaseExecutionResult().getComments() != null){
				this.comments = workPackageTestCaseExecutionPlan.getTestCaseExecutionResult().getComments();
			}else{
				this.comments = "";
			}
			
			this.defectsCount = workPackageTestCaseExecutionPlan.getTestCaseExecutionResult().getDefectsCount();
			if(workPackageTestCaseExecutionPlan.getTestCaseExecutionResult().getTestExecutionResultBugListSet() != null && !workPackageTestCaseExecutionPlan.getTestCaseExecutionResult().getTestExecutionResultBugListSet().isEmpty()){
				this.defectsIds = ""+ workPackageTestCaseExecutionPlan.getTestCaseExecutionResult().getTestExecutionResultBugListSet().iterator().next().getTestExecutionResultBugId();
			}
			this.defectsIds = workPackageTestCaseExecutionPlan.getTestCaseExecutionResult().getDefectIds();
			if(workPackageTestCaseExecutionPlan.getTestCaseExecutionResult().getIsApproved()==null)
				this.isApproved=0;
			else
				this.isApproved=workPackageTestCaseExecutionPlan.getTestCaseExecutionResult().getIsApproved();
			if(workPackageTestCaseExecutionPlan.getTestCaseExecutionResult().getIsReviewed()==null)
				this.isReviewed=0;
			else
				this.isReviewed=workPackageTestCaseExecutionPlan.getTestCaseExecutionResult().getIsReviewed();
			this.observedOutput =workPackageTestCaseExecutionPlan.getTestCaseExecutionResult().getObservedOutput();
			this.executionTime=workPackageTestCaseExecutionPlan.getTestCaseExecutionResult().getExecutionTime();
			
			this.jobFailureMessage=	workPackageTestCaseExecutionPlan.getTestCaseExecutionResult().getFailureReason();

			if(workPackageTestCaseExecutionPlan.getTestCaseExecutionResult().getStartTime() !=null){
				String dt= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(workPackageTestCaseExecutionPlan.getTestCaseExecutionResult().getStartTime());
				this.startTime =dt;
			}
			if(workPackageTestCaseExecutionPlan.getTestCaseExecutionResult().getEndTime()!=null){
				String dt= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(workPackageTestCaseExecutionPlan.getTestCaseExecutionResult().getEndTime());
				this.endTime =dt;
			}			
		}
		if(workPackageTestCaseExecutionPlan.getPlannedWorkShiftMaster()!=null){
			this.plannedShiftId=workPackageTestCaseExecutionPlan.getPlannedWorkShiftMaster().getShiftId();
			this.plannedShiftName=workPackageTestCaseExecutionPlan.getPlannedWorkShiftMaster().getShiftName();
		}else{
			this.plannedShiftId=0;
			this.plannedShiftName="";
		}
		
		if(workPackageTestCaseExecutionPlan.getActualWorkShiftMaster()!=null){
			this.actualShiftId=workPackageTestCaseExecutionPlan.getActualWorkShiftMaster().getShiftId();
			this.actualShiftName=workPackageTestCaseExecutionPlan.getActualWorkShiftMaster().getShiftName();
		}else{
			this.actualShiftName="NA";
		}
		
		if(workPackageTestCaseExecutionPlan.getExecutionPriority()!=null){
			this.executionPriorityId=workPackageTestCaseExecutionPlan.getExecutionPriority().getExecutionPriorityId();
			this.executionPriorityName=workPackageTestCaseExecutionPlan.getExecutionPriority().getExecutionPriority();
			this.executionPriorityDisplayName=workPackageTestCaseExecutionPlan.getExecutionPriority().getDisplayName();
			this.executionName=workPackageTestCaseExecutionPlan.getExecutionPriority().getExecutionPriorityName();
		}
		

		if(workPackageTestCaseExecutionPlan.getRunConfiguration()!=null){
			if(workPackageTestCaseExecutionPlan.getRunConfiguration().getRunconfiguration()!=null){

				runConfigurationName=workPackageTestCaseExecutionPlan.getRunConfiguration().getRunconfiguration().getRunconfigName();
				runConfigurationId=workPackageTestCaseExecutionPlan.getRunConfiguration().getWorkpackageRunConfigurationId();
				if(workPackageTestCaseExecutionPlan.getRunConfiguration().getRunconfiguration().getGenericDevice() != null)
					deviceName=workPackageTestCaseExecutionPlan.getRunConfiguration().getRunconfiguration().getGenericDevice().getUDID();
				if(runConfigurationName.contains(":")){
					environmentCombinationName=runConfigurationName.substring(0,runConfigurationName.indexOf(":"));
				}else{
					environmentCombinationName=runConfigurationName;
					
				}
				if(workPackageTestCaseExecutionPlan.getRunConfiguration().getRunconfiguration().getGenericDevice() != null){			
					if(workPackageTestCaseExecutionPlan.getRunConfiguration().getRunconfiguration().getGenericDevice().getPlatformType() != null){
						
						this.devplatformTypeName = workPackageTestCaseExecutionPlan.getRunConfiguration().getRunconfiguration().getGenericDevice().getPlatformType().getName();	
					}else{
						this.devplatformTypeName = "";
					}
					
				}
			}
		}

	if(workPackageTestCaseExecutionPlan.getHostList()!=null){
			this.hostId=workPackageTestCaseExecutionPlan.getHostList().getHostId();
			this.hostName=workPackageTestCaseExecutionPlan.getHostList().getHostName();
			this.hostIPAddress=workPackageTestCaseExecutionPlan.getHostList().getHostIpAddress();
		}
	this.sourceType=workPackageTestCaseExecutionPlan.getSourceType();	
	}


	@JsonIgnore
	public WorkPackageTestCaseExecutionPlan getWorkPackageTestCaseExecutionPlan() {
	
		WorkPackageTestCaseExecutionPlan workPackageTestCaseExecutionPlan = new WorkPackageTestCaseExecutionPlan();
		workPackageTestCaseExecutionPlan.setId(this.getId());
		
		TestCaseList testCase = new TestCaseList();
		testCase.setTestCaseId(this.testcaseId);
		workPackageTestCaseExecutionPlan.setTestCase(testCase);
		
		
		WorkPackage workPackage = new WorkPackage();
		workPackage.setWorkPackageId(this.workPackageId);
		workPackageTestCaseExecutionPlan.setWorkPackage(workPackage);
		
		UserList testLead = new UserList();
		if(this.testLeadName!=null && !this.testLeadName.equals("")){
			if(isInteger(this.testLeadName)){
				testLead= new UserList();
				testLead.setUserId(new Integer(this.testLeadName));
				workPackageTestCaseExecutionPlan.setTestLead(testLead);
			}else{
				testLead= new UserList();
				testLead.setFirstName(this.testLeadName);	
				workPackageTestCaseExecutionPlan.setTestLead(testLead);
			}
		}
				
		
		
		UserList tester =  new UserList();
		if(this.testerName!=null && !this.testerName.equals("")){
			if(isInteger(this.testerName)){
				tester = new UserList();
				tester.setUserId(new Integer(this.testerName));
				workPackageTestCaseExecutionPlan.setTester(tester);
			}else{
				tester = new UserList();
				tester.setFirstName(this.testerName);
				workPackageTestCaseExecutionPlan.setTester(tester);
			}
		}
	
		
		if(this.plannedExecutionDate == null || this.plannedExecutionDate.trim().isEmpty()) {
			workPackageTestCaseExecutionPlan.setPlannedExecutionDate(DateUtility.getCurrentTime());
		} else {
		
//			workPackageTestCaseExecutionPlan.setPlannedExecutionDate(DateUtility.dateFormatWithOutSeconds(this.plannedExecutionDate));
			workPackageTestCaseExecutionPlan.setPlannedExecutionDate(new Date(this.plannedExecutionDate));
		}
		
		
		if(this.actualExecutionDate == null || this.actualExecutionDate.trim().isEmpty()) {
			workPackageTestCaseExecutionPlan.setActualExecutionDate(DateUtility.getCurrentTime());
		} else {
			workPackageTestCaseExecutionPlan.setActualExecutionDate(new Date(this.actualExecutionDate));
		}
	
		if(this.createdDate == null || this.createdDate.trim().isEmpty()) {
			workPackageTestCaseExecutionPlan.setCreatedDate(DateUtility.getCurrentTime());
		} else {
			workPackageTestCaseExecutionPlan.setCreatedDate(new Date(this.createdDate));
			
		}
		workPackageTestCaseExecutionPlan.setModifiedDate(DateUtility.getCurrentTime());
		workPackageTestCaseExecutionPlan.setExecutionStatus(executionStatus);
		workPackageTestCaseExecutionPlan.setIsExecuted(this.isExecuted);
		
		TestCaseExecutionResult testCaseExecutionResult = new TestCaseExecutionResult();
		testCaseExecutionResult.setComments(comments);
		testCaseExecutionResult.setDefectIds(defectsIds);
		testCaseExecutionResult.setDefectsCount(defectsCount);
		testCaseExecutionResult.setResult(result);
		testCaseExecutionResult.setTestCaseExecutionResultId(testCaseExecutionResultId);
		testCaseExecutionResult.setIsApproved(isApproved);
		testCaseExecutionResult.setIsReviewed(isReviewed);
		workPackageTestCaseExecutionPlan.setExecutionStatus(executionStatus);
		workPackageTestCaseExecutionPlan.setIsExecuted(this.isExecuted);
		
		workPackageTestCaseExecutionPlan.setTestCaseExecutionResult(testCaseExecutionResult);
		
		WorkShiftMaster plannedWorkShiftMaster=new WorkShiftMaster();
		if(this.plannedShiftName!=null && !this.plannedShiftName.equals("")){
			if(isInteger(this.plannedShiftName)){
				plannedWorkShiftMaster= new WorkShiftMaster();
				plannedWorkShiftMaster.setShiftId(new Integer(this.plannedShiftName));
				workPackageTestCaseExecutionPlan.setPlannedWorkShiftMaster(plannedWorkShiftMaster);
			}else{
				plannedWorkShiftMaster= new WorkShiftMaster();
				plannedWorkShiftMaster.setShiftName(this.plannedShiftName);
				workPackageTestCaseExecutionPlan.setPlannedWorkShiftMaster(plannedWorkShiftMaster);
			}
		}
		
		
		WorkShiftMaster actualWorkShiftMaster=new WorkShiftMaster();
		if(this.actualShiftName!=null && !this.actualShiftName.equals("")){
			if(isInteger(this.actualShiftName)){
				actualWorkShiftMaster= new WorkShiftMaster();
				actualWorkShiftMaster.setShiftId(new Integer(this.actualShiftName));
				workPackageTestCaseExecutionPlan.setActualWorkShiftMaster(actualWorkShiftMaster);
			}else{
				actualWorkShiftMaster= new WorkShiftMaster();
				actualWorkShiftMaster.setShiftName(this.actualShiftName);
				workPackageTestCaseExecutionPlan.setActualWorkShiftMaster(actualWorkShiftMaster);
			}
		}
		

		if(executionPriorityId!=null){
			ExecutionPriority executionPriority = new ExecutionPriority();
			executionPriority.setExecutionPriorityId(executionPriorityId);
			executionPriority.setExecutionPriority(executionPriorityName);
			executionPriority.setExecutionPriorityName(executionName);
			workPackageTestCaseExecutionPlan.setExecutionPriority(executionPriority);
		}
		
		WorkpackageRunConfiguration wprc= new WorkpackageRunConfiguration();
		wprc.setWorkpackageRunConfigurationId(runConfigurationId);
		workPackageTestCaseExecutionPlan.setRunConfiguration(wprc);
		
		TestSuiteList testSuiteList = new TestSuiteList();
		if(testsuiteId!=null){
			testSuiteList.setTestSuiteId(testsuiteId);
			workPackageTestCaseExecutionPlan.setTestSuiteList(testSuiteList);
		}
			HostList hostList = new HostList();
		if(hostId!=null){
			hostList.setHostId(hostId);
			workPackageTestCaseExecutionPlan.setHostList(hostList);
		}
		
		workPackageTestCaseExecutionPlan.setSourceType(sourceType);
		return workPackageTestCaseExecutionPlan;
	}
	

	public static boolean isInteger(String s) {
	    try { 
	        Integer.parseInt(s); 
	    } catch(NumberFormatException e) { 
	        return false; 
	    }
	    // only got here if we didn't return false
	    return true;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getTestcaseId() {
		return testcaseId;
	}

	public void setTestcaseId(int testcaseId) {
		this.testcaseId = testcaseId;
	}

	public String getTestcaseName() {
		return testcaseName;
	}

	public void setTestcaseName(String testcaseName) {
		this.testcaseName = testcaseName;
	}

	public String getTestcaseDescription() {
		return testcaseDescription;
	}

	public void setTestcaseDescription(String testcaseDescription) {
		this.testcaseDescription = testcaseDescription;
	}

	public String getTestcaseCode() {
		return testcaseCode;
	}

	public void setTestcaseCode(String testcaseCode) {
		this.testcaseCode = testcaseCode;
	}

	public Integer getEnvironmentId() {
		return environmentId;
	}

	public void setEnvironmentId(Integer environmentId) {
		this.environmentId = environmentId;
	}

	public String getEnvironmentName() {
		return environmentName;
	}

	public void setEnvironmentName(String environmentName) {
		this.environmentName = environmentName;
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

	public Integer getTestLeadId() {
		return testLeadId;
	}

	public void setTestLeadId(Integer testLeadId) {
		this.testLeadId = testLeadId;
	}

	public String getTestLeadName() {
		return testLeadName;
	}

	public void setTestLeadName(String testLeadName) {
		this.testLeadName = testLeadName;
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

	public String getPlannedExecutionDate() {
		return plannedExecutionDate;
	}

	public void setPlannedExecutionDate(String plannedExecutionDate) {
		this.plannedExecutionDate = plannedExecutionDate;
	}

	public String getActualExecutionDate() {
		return actualExecutionDate;
	}

	public void setActualExecutionDate(String actualExecutionDate) {
		this.actualExecutionDate = actualExecutionDate;
	}

	public String getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
	}

	public String getModifiedDate() {
		return modifiedDate;
	}

	public void setModifiedDate(String modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

	public Integer getIsExecuted() {
		return isExecuted;
	}

	public void setIsExecuted(Integer isExecuted) {
		this.isExecuted = isExecuted;
	}

	public Integer getExecutionStatus() {
		return executionStatus;
	}

	public void setExecutionStatus(Integer executionStatus) {
		this.executionStatus = executionStatus;
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

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public String getDefectsIds() {
		return defectsIds;
	}

	public void setDefectsIds(String defectsIds) {
		this.defectsIds = defectsIds;
	}

	public int getDefectsCount() {
		return defectsCount;
	}

	public void setDefectsCount(int defectsCount) {
		this.defectsCount = defectsCount;
	}

	public String getExeType() {
		return exeType;
	}


	public void setExeType(String exeType) {
		this.exeType = exeType;
	}


	public String getRunCode() {
		return runCode;
	}

	public void setRunCode(String runCode) {
		this.runCode = runCode;
	}

	public Integer getPlannedShiftId() {
		return plannedShiftId;
	}

	public void setPlannedShiftId(Integer plannedShiftId) {
		this.plannedShiftId = plannedShiftId;
	}

	public String getPlannedShiftName() {
		return plannedShiftName;
	}

	public void setPlannedShiftName(String plannedShiftName) {
		this.plannedShiftName = plannedShiftName;
	}

	public Integer getActualShiftId() {
		return actualShiftId;
	}

	public void setActualShiftId(Integer actualShiftId) {
		this.actualShiftId = actualShiftId;
	}

	public String getActualShiftName() {
		return actualShiftName;
	}

	public void setActualShiftName(String actualShiftName) {
		this.actualShiftName = actualShiftName;
	}

	public Integer getIsApproved() {
		return isApproved;
	}

	public void setIsApproved(Integer isApproved) {
		this.isApproved = isApproved;
	}

	public Integer getIsReviewed() {
		return isReviewed;
	}

	public void setIsReviewed(Integer isReviewed) {
		this.isReviewed = isReviewed;
	}

	public Integer getLocaleId() {
		return localeId;
	}

	public void setLocaleId(Integer localeId) {
		this.localeId = localeId;
	}

	public String getLocaleName() {
		return localeName;
	}

	public void setLocaleName(String localeName) {
		this.localeName = localeName;
	}

	public String getTestcaseType() {
		return testcaseType;
	}

	public void setTestcaseType(String testcaseType) {
		this.testcaseType = testcaseType;
	}

	public Integer getExecutionPriorityId() {
		return executionPriorityId;
	}

	public void setExecutionPriorityId(Integer executionPriorityId) {
		this.executionPriorityId = executionPriorityId;
	}

	public String getExecutionPriorityName() {
		return executionPriorityName;
	}

	public void setExecutionPriorityName(String executionPriorityName) {
		this.executionPriorityName = executionPriorityName;
	}

	public String getExecutionPriorityDisplayName() {
		return executionPriorityDisplayName;
	}

	public void setExecutionPriorityDisplayName(String executionPriorityDisplayName) {
		this.executionPriorityDisplayName = executionPriorityDisplayName;
	}

	public String getPreconditions() {
		return preconditions;
	}

	public void setPreconditions(String preconditions) {
		this.preconditions = preconditions;
	}

	public String getTestcaseInput() {
		return testcaseInput;
	}

	public void setTestcaseInput(String testcaseInput) {
		this.testcaseInput = testcaseInput;
	}

	public String getExpectedOutput() {
		return expectedOutput;
	}

	public void setExpectedOutput(String expectedOutput) {
		this.expectedOutput = expectedOutput;
	}

	public String getObservedOutput() {
		return observedOutput;
	}

	public void setObservedOutput(String observedOutput) {
		this.observedOutput = observedOutput;
	}

	public String getEvidencePath() {
		return evidencePath;
	}

	public void setEvidencePath(String evidencePath) {
		this.evidencePath = evidencePath;
	}

	public String getEvidenceLabel() {
		return evidenceLabel;
	}

	public void setEvidenceLabel(String evidenceLabel) {
		this.evidenceLabel = evidenceLabel;
	}

	public String getExecutionName() {
		return executionName;
	}

	public void setExecutionName(String executionName) {
		this.executionName = executionName;
	}

	public Long getExecutionTime() {
		return executionTime;
	}

	public void setExecutionTime(Long executionTime) {
		this.executionTime = executionTime;
	}

	public int getEnvironmentCount() {
		return environmentCount;
	}

	public void setEnvironmentCount(int environmentCount) {
		this.environmentCount = environmentCount;
	}

	public String getEnvironmentCombinationName() {
		return environmentCombinationName;
	}

	public void setEnvironmentCombinationName(String environmentCombinationName) {
		this.environmentCombinationName = environmentCombinationName;
	}

	public Integer getEnvironmentCombinationId() {
		return environmentCombinationId;
	}

	public void setEnvironmentCombinationId(Integer environmentCombinationId) {
		this.environmentCombinationId = environmentCombinationId;
	}

	public String getOveralResult() {
		return overalResult;
	}

	public void setOveralResult(String overalResult) {
		this.overalResult = overalResult;
	}

	public String getRunConfigurationName() {
		return runConfigurationName;
	}

	public void setRunConfigurationName(String runConfigurationName) {
		this.runConfigurationName = runConfigurationName;
	}

	public Integer getRunConfigurationId() {
		return runConfigurationId;
	}

	public void setRunConfigurationId(Integer runConfigurationId) {
		this.runConfigurationId = runConfigurationId;
	}

	public Integer getTestsuiteId() {
		return testsuiteId;
	}

	public void setTestsuiteId(Integer testsuiteId) {
		this.testsuiteId = testsuiteId;
	}

	public String getTestsuiteName() {
		return testsuiteName;
	}

	public void setTestsuiteName(String testsuiteName) {
		this.testsuiteName = testsuiteName;
	}

	public String getDeviceName() {
		return deviceName;
	}

	public void setDeviceName(String deviceName) {
		this.deviceName = deviceName;
	}

	public String getDevplatformTypeName() {
		return devplatformTypeName;
	}


	public void setDevplatformTypeName(String devplatformTypeName) {
		this.devplatformTypeName = devplatformTypeName;
	}


	public Integer getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(Integer deviceId) {
		this.deviceId = deviceId;
	}

	public String getTestcasePriority() {
		return testcasePriority;
	}

	public void setTestcasePriority(String testcasePriority) {
		this.testcasePriority = testcasePriority;
	}

	public int getTestCaseCountOfRunconfig() {
		return testCaseCountOfRunconfig;
	}

	public void setTestCaseCountOfRunconfig(int testCaseCountOfRunconfig) {
		this.testCaseCountOfRunconfig = testCaseCountOfRunconfig;
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

	public Integer getHostId() {
		return hostId;
	}
	public void setHostId(Integer hostId) {
		this.hostId = hostId;
	}

	public String getHostName() {
		return hostName;
	}

	public void setHostName(String hostName) {
		this.hostName = hostName;
	}	
	public String getHostIPAddress() {
		return hostIPAddress;
	}
	public void setHostIPAddress(String hostIPAddress) {
		this.hostIPAddress = hostIPAddress;
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

	public Integer getTotalExecutedTesCases() {
		return totalExecutedTesCases;
	}

	public void setTotalExecutedTesCases(Integer totalExecutedTesCases) {
		this.totalExecutedTesCases = totalExecutedTesCases;
	}

	public Integer getNotExecuted() {
		return notExecuted;
	}

	public void setNotExecuted(Integer notExecuted) {
		this.notExecuted = notExecuted;
	}

	public void setTestcaseId(Integer testcaseId) {
		this.testcaseId = testcaseId;
	}

	public Integer getTestRunJobId() {
		return testRunJobId;
	}

	public void setTestRunJobId(Integer testRunJobId) {
		this.testRunJobId = testRunJobId;
	}

	public Integer getP2totalPass() {
		return p2totalPass;
	}

	public void setP2totalPass(Integer p2totalPass) {
		this.p2totalPass = p2totalPass;
	}

	public Integer getP2totalFail() {
		return p2totalFail;
	}

	public void setP2totalFail(Integer p2totalFail) {
		this.p2totalFail = p2totalFail;
	}

	public Integer getP2totalNoRun() {
		return p2totalNoRun;
	}

	public void setP2totalNoRun(Integer p2totalNoRun) {
		this.p2totalNoRun = p2totalNoRun;
	}

	public Integer getP2totalBlock() {
		return p2totalBlock;
	}

	public void setP2totalBlock(Integer p2totalBlock) {
		this.p2totalBlock = p2totalBlock;
	}

	public Integer getP3totalPass() {
		return p3totalPass;
	}

	public void setP3totalPass(Integer p3totalPass) {
		this.p3totalPass = p3totalPass;
	}

	public Integer getP3totalFail() {
		return p3totalFail;
	}

	public void setP3totalFail(Integer p3totalFail) {
		this.p3totalFail = p3totalFail;
	}

	public Integer getP3totalNoRun() {
		return p3totalNoRun;
	}

	public void setP3totalNoRun(Integer p3totalNoRun) {
		this.p3totalNoRun = p3totalNoRun;
	}

	public Integer getP3totalBlock() {
		return p3totalBlock;
	}

	public void setP3totalBlock(Integer p3totalBlock) {
		this.p3totalBlock = p3totalBlock;
	}

	public Integer getP4totalPass() {
		return p4totalPass;
	}

	public void setP4totalPass(Integer p4totalPass) {
		this.p4totalPass = p4totalPass;
	}

	public Integer getP4totalFail() {
		return p4totalFail;
	}

	public void setP4totalFail(Integer p4totalFail) {
		this.p4totalFail = p4totalFail;
	}

	public Integer getP4totalNoRun() {
		return p4totalNoRun;
	}

	public void setP4totalNoRun(Integer p4totalNoRun) {
		this.p4totalNoRun = p4totalNoRun;
	}

	public Integer getP4totalBlock() {
		return p4totalBlock;
	}

	public void setP4totalBlock(Integer p4totalBlock) {
		this.p4totalBlock = p4totalBlock;
	}

	public Integer getP5totalPass() {
		return p5totalPass;
	}

	public void setP5totalPass(Integer p5totalPass) {
		this.p5totalPass = p5totalPass;
	}

	public Integer getP5totalFail() {
		return p5totalFail;
	}

	public void setP5totalFail(Integer p5totalFail) {
		this.p5totalFail = p5totalFail;
	}

	public Integer getP5totalNoRun() {
		return p5totalNoRun;
	}

	public void setP5totalNoRun(Integer p5totalNoRun) {
		this.p5totalNoRun = p5totalNoRun;
	}

	public Integer getP5totalBlock() {
		return p5totalBlock;
	}

	public void setP5totalBlock(Integer p5totalBlock) {
		this.p5totalBlock = p5totalBlock;
	}

	public String getFeatureName() {
		return featureName;
	}

	public void setFeatureName(String featureName) {
		this.featureName = featureName;
	}

	public String getDecoupleName() {
		return decoupleName;
	}

	public void setDecoupleName(String decoupleName) {
		this.decoupleName = decoupleName;
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

	public Integer getProductId() {
		return productId;
	}

	public void setProductId(Integer productId) {
		this.productId = productId;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public Integer getProductTotalTC() {
		return productTotalTC;
	}

	public void setProductTotalTC(Integer productTotalTC) {
		this.productTotalTC = productTotalTC;
	}

	public Integer getWpPlannedTC() {
		return wpPlannedTC;
	}

	public void setWpPlannedTC(Integer wpPlannedTC) {
		this.wpPlannedTC = wpPlannedTC;
	}

	public String getWpStartEnddayDiff() {
		return wpStartEnddayDiff;
	}

	public void setWpStartEnddayDiff(String wpStartEnddayDiff) {
		this.wpStartEnddayDiff = wpStartEnddayDiff;
	}

	public String getWpnthDayfromStrart() {
		return wpnthDayfromStrart;
	}

	public void setWpnthDayfromStrart(String wpnthDayfromStrart) {
		this.wpnthDayfromStrart = wpnthDayfromStrart;
	}	
	public Integer getTotalTcForRowWise() {
		return totalTcForRowWise;
	}

	public void setTotalTcForRowWise(Integer totalTcForRowWise) {
		this.totalTcForRowWise = totalTcForRowWise;
	}

	public Integer getTestBedCount() {
		return testBedCount;
	}

	public void setTestBedCount(Integer testBedCount) {
		this.testBedCount = testBedCount;
	}

	public String getWpStatus() {
		return wpStatus;
	}

	public void setWpStatus(String wpStatus) {
		this.wpStatus = wpStatus;
	}

	public String getExecutionBeforeAfter() {
		return executionBeforeAfter;
	}

	public void setExecutionBeforeAfter(String executionBeforeAfter) {
		this.executionBeforeAfter = executionBeforeAfter;
	}

	public String getPlannedBeforeAfterCurrentDate() {
		return plannedBeforeAfterCurrentDate;
	}

	public void setPlannedBeforeAfterCurrentDate(
			String plannedBeforeAfterCurrentDate) {
		this.plannedBeforeAfterCurrentDate = plannedBeforeAfterCurrentDate;
	}

	public String getFirstActualExecutionDate() {
		return firstActualExecutionDate;
	}

	public void setFirstActualExecutionDate(String firstActualExecutionDate) {
		this.firstActualExecutionDate = firstActualExecutionDate;
	}

	public String getLastActualExecutionDate() {
		return lastActualExecutionDate;
	}

	public void setLastActualExecutionDate(String lastActualExecutionDate) {
		this.lastActualExecutionDate = lastActualExecutionDate;
	}

	public String getExecutedLastHour() {
		return executedLastHour;
	}

	public void setExecutedLastHour(String executedLastHour) {
		this.executedLastHour = executedLastHour;
	}
/*
	public Integer getProductBuildId() {
		return productBuildId;
	}


	public void setProductBuildId(Integer productBuildId) {
		this.productBuildId = productBuildId;
	}
*/

	public String getProductBuildName() {
		return productBuildName;
	}

	public void setProductBuildName(String productBuildName) {
		this.productBuildName = productBuildName;
	}

	public String getSourceType() {
		return sourceType;
	}

	public void setSourceType(String sourceType) {
		this.sourceType = sourceType;
	}
	public Integer getTotalExecutionTCs() {
		return totalExecutionTCs;
	}

	public void setTotalExecutionTCs(Integer totalExecutionTCs) {
		this.totalExecutionTCs = totalExecutionTCs;
	}


	public Integer getTeststepcount() {
		return teststepcount;
	}


	public void setTeststepcount(Integer teststepcount) {
		this.teststepcount = teststepcount;
	}


	public String getWpResult() {
		return wpResult;
	}


	public void setWpResult(String wpResult) {
		this.wpResult = wpResult;
	}


	public String getJobResult() {
		return jobResult;
	}


	public void setJobResult(String jobResult) {
		this.jobResult = jobResult;
	}


	public Integer getJobsCount() {
		return jobsCount;
	}


	public void setJobsCount(Integer jobsCount) {
		this.jobsCount = jobsCount;
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


	public String getStartTime() {
		return startTime;
	}


	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}


	public String getEndTime() {
		return endTime;
	}


	public void setEndTime(String endTime) {
		this.endTime = endTime;
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


	public Integer getMappedFeatureCount() {
		return mappedFeatureCount;
	}


	public Integer getMappedTestscriptCount() {
		return mappedTestscriptCount;
	}


	public void setMappedFeatureCount(Integer mappedFeatureCount) {
		this.mappedFeatureCount = mappedFeatureCount;
	}


	public void setMappedTestscriptCount(Integer mappedTestscriptCount) {
		this.mappedTestscriptCount = mappedTestscriptCount;
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


	public Integer getTestPlanId() {
		return testPlanId;
	}


	public void setTestPlanId(Integer testPlanId) {
		this.testPlanId = testPlanId;
	}


	public String getUseIntelligentTestPlan() {
		return useIntelligentTestPlan;
	}


	public void setUseIntelligentTestPlan(String useIntelligentTestPlan) {
		this.useIntelligentTestPlan = useIntelligentTestPlan;
	}


	public String getTestPlanExecutionMode() {
		return testPlanExecutionMode;
	}


	public void setTestPlanExecutionMode(String testPlanExecutionMode) {
		this.testPlanExecutionMode = testPlanExecutionMode;
	}


	public String getTestPlanName() {
		return testPlanName;
	}


	public void setTestPlanName(String testPlanName) {
		this.testPlanName = testPlanName;
	}


	
}
