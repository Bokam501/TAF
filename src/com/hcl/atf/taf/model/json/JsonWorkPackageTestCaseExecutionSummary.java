package com.hcl.atf.taf.model.json;

import java.text.ParseException;
import java.util.HashMap;
import java.util.Locale;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.simple.JSONObject;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.hcl.atf.taf.controller.utilities.DateUtility;
import com.hcl.atf.taf.model.Environment;
import com.hcl.atf.taf.model.EnvironmentCombination;
import com.hcl.atf.taf.model.RunConfiguration;
import com.hcl.atf.taf.model.dto.WorkPackageBuildTestCaseSummaryDTO;
import com.hcl.atf.taf.model.dto.WorkPackageTestCaseSummaryDTO;


public class JsonWorkPackageTestCaseExecutionSummary implements java.io.Serializable{
	
	private static final Log log = LogFactory.getLog(JsonWorkPackageTestCaseExecutionSummary.class);
	
	@JsonProperty	
	private Integer id;
	@JsonProperty
	private String workPackageName;
	@JsonProperty	
	private Integer productId;
	@JsonProperty
	private String productName;
	@JsonProperty	
	private Integer pBuildId;
	@JsonProperty
	private String pBuildName;
	@JsonProperty
	private String description;
	@JsonProperty
	private Integer status;
	@JsonProperty
	private HashMap workFlowstageNameList;//Status List
	@JsonProperty
	private String plannedStartDate;
	@JsonProperty
	private String plannedEndDate;
	@JsonProperty
	private String actualStartDate;
	@JsonProperty
	private String actualEndDate;	
	@JsonProperty
	private Integer executedTestCasesCount;
	@JsonProperty
	private Integer defectiveTestCasesCount;
	@JsonProperty
	private Integer approvedTestCasesCount;
	@JsonProperty
	private Integer rejectedTestCasesCount;
	@JsonProperty
	private String executedDate;	
	@JsonProperty
	private Integer testCasesCount;
	@JsonProperty
	private Integer defectsCount;
	@JsonProperty
	private Integer approvedTestCaseCount;
	@JsonProperty
	private Integer rejectedTestCaseCount;
	@JsonProperty
	private Integer totalTestCaseForExecutionCount;
	@JsonProperty
	private Integer completedTestCaseCount;
	@JsonProperty
	private Float completedTestCasePercentage;
	@JsonProperty
	private Integer notCompletedTestCaseCount;
	@JsonProperty
	private Integer selectedTestCasesCount;
	@JsonProperty
	private Integer selectedTestSuitesCount;
	@JsonProperty
	private Integer selectedFeaturesCount;
	@JsonProperty
	private Integer totalTestCaseCount;
	@JsonProperty
	private Integer teststepcount;
	@JsonProperty
	private Integer plannedExecutionDateCount;
	@JsonProperty
	 private String environmentList;
	@JsonProperty
	 private Integer environmentCount;
	@JsonProperty
	private Integer approvedDefectsCount;
	@JsonProperty
	private Integer totalDefectsCount;
	@JsonProperty
	private String shiftName;
	@JsonProperty
	private Integer totalNumberOfDays;
	@JsonProperty
	private Integer totalNumberOfDaysCompleted;
	@JsonProperty
	private Integer workpackageStatus;
	@JsonProperty
	 private String environmentCombination;
	@JsonProperty
	 private String runConfiguration;
	
	@JsonProperty
	private String workPackageType;
	
	@JsonProperty
	private Integer totalNumberOfCompDays;
	@JsonProperty
	private String productType;

	@JsonProperty
	private Integer iterationNo;
	@JsonProperty
	private Integer lifeCycleId;
	@JsonProperty
	private HashMap lifeCycleNameList;//Life Cycle List
	@JsonProperty
	private Integer testRunJobId;
	@JsonProperty
	private Integer testRunStatus;
	@JsonProperty
	private String testRunFailureMessage;
	@JsonProperty
	private String testRunEvidenceMessage;
	@JsonProperty
	private Integer passedCount;
	@JsonProperty
	private Integer failedCount;
	@JsonProperty
	private Integer blockedCount;
	@JsonProperty
	private Integer norunCount;
	@JsonProperty
	private Integer notexecutedCount;
	@JsonProperty
	private String wpcreatedUser;
	@JsonProperty
	private Integer jobsCount;
	@JsonProperty
	private Integer testRunJobsCompleted;
	@JsonProperty
	private String testRunStatusName;
	@JsonProperty
	private String result;
	@JsonProperty
	private Integer testPlanId;
	@JsonProperty
	private String automationMode;	
	@JsonProperty
    private String useIntelligentTestPlan;
	
	public JsonWorkPackageTestCaseExecutionSummary(){
		
	}
	public JsonWorkPackageTestCaseExecutionSummary(WorkPackageTestCaseSummaryDTO workPackageTestCaseSummaryDTO){
		this.id = workPackageTestCaseSummaryDTO.getWorkPackageId();
		this.workPackageName = workPackageTestCaseSummaryDTO.getWorkPackageName();
		this.workPackageType = workPackageTestCaseSummaryDTO.getWorkPackageType();
		this.productId = workPackageTestCaseSummaryDTO.getProductId();		
		this.productName = workPackageTestCaseSummaryDTO.getProductName();			
		this.pBuildId = workPackageTestCaseSummaryDTO.getpBuildId();
		this.pBuildName = workPackageTestCaseSummaryDTO.getpBuildName();	
		this.description =  workPackageTestCaseSummaryDTO.getDescription();	
		if(workPackageTestCaseSummaryDTO.getStatus() != null){
			this.status = workPackageTestCaseSummaryDTO.getStatus();	
		}else{
			this.status = -1;
		}
		if(workPackageTestCaseSummaryDTO.getWorkFlowstageNameList() != null){
			this.workFlowstageNameList = workPackageTestCaseSummaryDTO.getWorkFlowstageNameList();
		}else{
			HashMap<Integer, String> defaultValue= new HashMap<Integer, String>();
			defaultValue.put(-1, "--");
			this.workFlowstageNameList = defaultValue;
		}
			
		
		if (workPackageTestCaseSummaryDTO.getPlannedStartDate() != null) {
			this.plannedStartDate = DateUtility.sdfDateformatWithOutTime(workPackageTestCaseSummaryDTO.getPlannedStartDate());
		}
		
		if (workPackageTestCaseSummaryDTO.getPlannedEndDate() != null) {
			this.plannedEndDate = DateUtility.sdfDateformatWithOutTime(workPackageTestCaseSummaryDTO.getPlannedEndDate());
		}
		
		if (workPackageTestCaseSummaryDTO.getActualStartDate() != null) {
			this.actualStartDate = DateUtility.sdfDateformatWithOutTime(workPackageTestCaseSummaryDTO.getActualStartDate());
		}
		
		if (workPackageTestCaseSummaryDTO.getActualEndDate() != null) {
			this.actualEndDate = DateUtility.sdfDateformatWithOutTime(workPackageTestCaseSummaryDTO.getActualEndDate());
		}
		
		this.executedTestCasesCount = workPackageTestCaseSummaryDTO.getTotalTestCaseForExecutionCount();
		this.defectiveTestCasesCount = workPackageTestCaseSummaryDTO.getDefectsCount();
		
		if(workPackageTestCaseSummaryDTO.getSelectedTestCasesCount() != null){
			this.selectedTestCasesCount = workPackageTestCaseSummaryDTO.getSelectedTestCasesCount();		
		}else{
			this.selectedTestCasesCount = 0;
		}
		
		if(workPackageTestCaseSummaryDTO.getSelectedTestSuitesCount() != null){
			this.selectedTestSuitesCount = workPackageTestCaseSummaryDTO.getSelectedTestSuitesCount();		
		}else{
			this.selectedTestSuitesCount = 0;
		}
		
		if(workPackageTestCaseSummaryDTO.getSelectedFeaturesCount() != null){
			this.selectedFeaturesCount = workPackageTestCaseSummaryDTO.getSelectedFeaturesCount();		
		}else{
			this.selectedFeaturesCount = 0;
		}
		if(workPackageTestCaseSummaryDTO.getTotalTestCaseCount() != null){
			this.totalTestCaseCount = workPackageTestCaseSummaryDTO.getTotalTestCaseCount();
		}else{
			this.totalTestCaseCount=0;
		}
		
		if(workPackageTestCaseSummaryDTO.getTotalTestCaseForExecutionCount()!=null)
			this.totalTestCaseForExecutionCount = workPackageTestCaseSummaryDTO.getTotalTestCaseForExecutionCount();
		else
			this.totalTestCaseForExecutionCount=0;
		
		if(this.totalTestCaseForExecutionCount != null && this.totalTestCaseCount != null){
			this.completedTestCasePercentage = (float)((totalTestCaseForExecutionCount*100.0)/totalTestCaseForExecutionCount);
		}else{
			this.completedTestCasePercentage =(float)0;
		}
		
		this.completedTestCaseCount = workPackageTestCaseSummaryDTO.getCompletedTestCaseCount();
		
		
		
		if(selectedTestCasesCount!=null && completedTestCaseCount!=null)
			this.notCompletedTestCaseCount = this.selectedTestCasesCount - this.completedTestCaseCount;
		
		
		
		
		this.defectsCount = workPackageTestCaseSummaryDTO.getDefectsCount();
		this.approvedTestCaseCount = workPackageTestCaseSummaryDTO.getApprovedTestCaseCount();
		this.rejectedTestCaseCount = workPackageTestCaseSummaryDTO.getRejectedTestCaseCount();
		
		if(workPackageTestCaseSummaryDTO.getDefectsCount() != null){
			this.defectsCount = workPackageTestCaseSummaryDTO.getDefectsCount();	
		}else{
			this.defectsCount = 0;
		}
		
		if(workPackageTestCaseSummaryDTO.getActualExecutionDate()!=null)
			this.executedDate = DateUtility.sdfDateformatWithOutTime(workPackageTestCaseSummaryDTO.getActualExecutionDate());
			
		if(workPackageTestCaseSummaryDTO.getEnvironmentList()!=null){
			for (Environment env : workPackageTestCaseSummaryDTO.getEnvironmentList()) {
				if(environmentList==null || environmentList=="")
					environmentList=env.getEnvironmentName();
				else
					environmentList=environmentList+","+env.getEnvironmentName();
				
			}
			
			
		}
		
		if(workPackageTestCaseSummaryDTO.getEnvironmentCombination()!=null){
			environmentCount= workPackageTestCaseSummaryDTO.getEnvironmentCombination().size();
			for (EnvironmentCombination envc : workPackageTestCaseSummaryDTO.getEnvironmentCombination()) {
				if(environmentCombination==null || environmentCombination=="")
					environmentCombination=envc.getEnvironmentCombinationName();
				else
					environmentCombination=environmentCombination+","+envc.getEnvironmentCombinationName();
				
			}
		}
		
		if(workPackageTestCaseSummaryDTO.getTotalDefectsCount()!=null){
			this.totalDefectsCount=workPackageTestCaseSummaryDTO.getTotalDefectsCount();
		}else{
			this.totalDefectsCount=0;
		}
		
		if(workPackageTestCaseSummaryDTO.getApprovedDefectsCount()!=null){
			this.approvedDefectsCount=workPackageTestCaseSummaryDTO.getApprovedDefectsCount();
		}else{
			this.approvedDefectsCount=0;
		}
		
		this.shiftName=workPackageTestCaseSummaryDTO.getShiftName();
		if(workPackageTestCaseSummaryDTO.getPlannedStartDate()!=null && workPackageTestCaseSummaryDTO.getPlannedEndDate()!=null)
			this.totalNumberOfDays=((Long)DateUtility.DateDifferenceInSeconds(DateUtility.sdfDateformatWithOutTime(workPackageTestCaseSummaryDTO.getPlannedStartDate()),DateUtility.sdfDateformatWithOutTime(workPackageTestCaseSummaryDTO.getPlannedEndDate()))).intValue();
		try {
			if(workPackageTestCaseSummaryDTO.getPlannedStartDate()!=null )
			this.totalNumberOfDaysCompleted=((Long)DateUtility.DateDifferenceInSeconds(DateUtility.sdfDateformatWithOutTime(workPackageTestCaseSummaryDTO.getPlannedStartDate()),DateUtility.getCurrentTimeInYYYYMMDD())).intValue();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		this.workpackageStatus=workPackageTestCaseSummaryDTO.getWorkpackageStatus();
		this.iterationNo = workPackageTestCaseSummaryDTO.getIterationNo();
		this.lifeCycleId = workPackageTestCaseSummaryDTO.getLifecyclePhaseId();
		if(workPackageTestCaseSummaryDTO.getLifecyclePhaseList() != null){
			this.lifeCycleNameList = workPackageTestCaseSummaryDTO.getLifecyclePhaseList();
		}else{
			HashMap<Integer, String> defaultValue= new HashMap<Integer, String>();
			defaultValue.put(-1, "--");
			this.lifeCycleNameList = defaultValue;
		}
		
	}
	
	
	public JsonWorkPackageTestCaseExecutionSummary(WorkPackageTestCaseSummaryDTO workPackageTestCaseSummaryDTO,Locale locale) throws Exception{
		this.id = workPackageTestCaseSummaryDTO.getWorkPackageId();
		this.workPackageName = workPackageTestCaseSummaryDTO.getWorkPackageName();
		this.workPackageType = workPackageTestCaseSummaryDTO.getWorkPackageType();
		if(workPackageTestCaseSummaryDTO.getProductId() != null){
			this.productId = workPackageTestCaseSummaryDTO.getProductId();	
		}		
		this.productName = workPackageTestCaseSummaryDTO.getProductName();	
		if(workPackageTestCaseSummaryDTO.getpBuildId() != null){
			this.pBuildId = workPackageTestCaseSummaryDTO.getpBuildId();	
		}		
		this.pBuildName = workPackageTestCaseSummaryDTO.getpBuildName();	
		this.description =  workPackageTestCaseSummaryDTO.getDescription();	
		if(workPackageTestCaseSummaryDTO.getProductType() != null){
			this.productType = workPackageTestCaseSummaryDTO.getProductType();
		}
		if(workPackageTestCaseSummaryDTO.getStatus() != null){
			this.status = workPackageTestCaseSummaryDTO.getStatus();	
		}else{
			this.status = -1;
		}
		if(workPackageTestCaseSummaryDTO.getWorkFlowstageNameList() != null){
			this.workFlowstageNameList = workPackageTestCaseSummaryDTO.getWorkFlowstageNameList();
		}else{
			HashMap<Integer, String> defaultValue= new HashMap<Integer, String>();
			defaultValue.put(-1, "--");
			this.workFlowstageNameList = defaultValue;
		}
			
		
		if (workPackageTestCaseSummaryDTO.getPlannedStartDate() != null) {
			this.plannedStartDate = DateUtility.sdfDateformatWithOutTime(workPackageTestCaseSummaryDTO.getPlannedStartDate());
		}
		
		if (workPackageTestCaseSummaryDTO.getPlannedEndDate() != null) {
			this.plannedEndDate = DateUtility.sdfDateformatWithOutTime(workPackageTestCaseSummaryDTO.getPlannedEndDate());
		}
		
		if (workPackageTestCaseSummaryDTO.getActualStartDate() != null) {
			this.actualStartDate = DateUtility.sdfDateformatWithOutTime(workPackageTestCaseSummaryDTO.getActualStartDate());
		}
		
		if (workPackageTestCaseSummaryDTO.getActualEndDate() != null) {
			this.actualEndDate = DateUtility.sdfDateformatWithOutTime(workPackageTestCaseSummaryDTO.getActualEndDate());
		}
		
		this.executedTestCasesCount = workPackageTestCaseSummaryDTO.getTotalTestCaseForExecutionCount();
		this.defectiveTestCasesCount = workPackageTestCaseSummaryDTO.getDefectsCount();
		
		if(workPackageTestCaseSummaryDTO.getSelectedTestCasesCount() != null){
			this.selectedTestCasesCount = workPackageTestCaseSummaryDTO.getSelectedTestCasesCount();		
		}else{
			this.selectedTestCasesCount = 0;
		}

		if(workPackageTestCaseSummaryDTO.getSelectedTestSuitesCount() != null){
			this.selectedTestSuitesCount = workPackageTestCaseSummaryDTO.getSelectedTestSuitesCount();		
		}else{
			this.selectedTestSuitesCount = 0;
		}
		
		if(workPackageTestCaseSummaryDTO.getSelectedFeaturesCount() != null){
			this.selectedFeaturesCount = workPackageTestCaseSummaryDTO.getSelectedFeaturesCount();		
		}else{
			this.selectedFeaturesCount = 0;
		}
		
		if(workPackageTestCaseSummaryDTO.getTotalTestCaseCount() != null){
			this.totalTestCaseCount = workPackageTestCaseSummaryDTO.getTotalTestCaseCount();
		}else{
			this.totalTestCaseCount=0;
		}
		
		if(workPackageTestCaseSummaryDTO.getTotalTestCaseForExecutionCount()!=null)
			this.totalTestCaseForExecutionCount = workPackageTestCaseSummaryDTO.getTotalTestCaseForExecutionCount();
		else
			this.totalTestCaseForExecutionCount=0;
		
		if(this.totalTestCaseForExecutionCount != null && this.totalTestCaseCount != null && totalTestCaseForExecutionCount > 0){
			this.completedTestCasePercentage = (float)((totalTestCaseForExecutionCount*100.0)/totalTestCaseForExecutionCount);
		}else{
			this.completedTestCasePercentage =(float)0;
		}
		
		this.completedTestCaseCount = workPackageTestCaseSummaryDTO.getCompletedTestCaseCount();
		
		
		
		if(selectedTestCasesCount!=null && completedTestCaseCount!=null)
			this.notCompletedTestCaseCount = this.selectedTestCasesCount - this.completedTestCaseCount;
		this.defectsCount = workPackageTestCaseSummaryDTO.getDefectsCount();
		this.approvedTestCaseCount = workPackageTestCaseSummaryDTO.getApprovedTestCaseCount();
		this.rejectedTestCaseCount = workPackageTestCaseSummaryDTO.getRejectedTestCaseCount();
		
		if(workPackageTestCaseSummaryDTO.getDefectsCount() != null){
			this.defectsCount = workPackageTestCaseSummaryDTO.getDefectsCount();	
		}else{
			this.defectsCount = 0;
		}
		
		if(workPackageTestCaseSummaryDTO.getActualExecutionDate()!=null)
			this.executedDate = DateUtility.sdfDateformatWithOutTime(workPackageTestCaseSummaryDTO.getActualExecutionDate());
			
		if(workPackageTestCaseSummaryDTO.getEnvironmentList()!=null){
			for (Environment env : workPackageTestCaseSummaryDTO.getEnvironmentList()) {
				if(environmentList==null || environmentList=="")
					environmentList=env.getEnvironmentName();
				else
					environmentList=environmentList+","+env.getEnvironmentName();
				
			}
			
			
		}
		
		
		if(workPackageTestCaseSummaryDTO.getRunConfigurations()!=null){
			environmentCount= workPackageTestCaseSummaryDTO.getRunConfigurations().size();
			for (RunConfiguration envc : workPackageTestCaseSummaryDTO.getRunConfigurations()) {
				if(environmentCombination==null || environmentCombination=="")
					environmentCombination=envc.getRunconfigName();
				else
					environmentCombination=environmentCombination+","+envc.getRunconfigName();
				
			}
		}

		if(workPackageTestCaseSummaryDTO.getTotalDefectsCount()!=null){
			this.totalDefectsCount=workPackageTestCaseSummaryDTO.getTotalDefectsCount();
		}else{
			this.totalDefectsCount=0;
		}
		
		if(workPackageTestCaseSummaryDTO.getApprovedDefectsCount()!=null){
			this.approvedDefectsCount=workPackageTestCaseSummaryDTO.getApprovedDefectsCount();
		}else{
			this.approvedDefectsCount=0;
		}
		
		this.shiftName=workPackageTestCaseSummaryDTO.getShiftName();
		if(workPackageTestCaseSummaryDTO.getPlannedStartDate()!=null && workPackageTestCaseSummaryDTO.getPlannedEndDate()!=null){
			Integer difference=((Long)DateUtility.DateDifference(DateUtility.sdfDateformatWithOutTime(workPackageTestCaseSummaryDTO.getPlannedStartDate()),DateUtility.sdfDateformatWithOutTime(workPackageTestCaseSummaryDTO.getPlannedEndDate()))).intValue();
			if(workPackageTestCaseSummaryDTO.getPlannedStartDate().equals(workPackageTestCaseSummaryDTO.getPlannedEndDate())){
				this.totalNumberOfDays=1;
				if(workPackageTestCaseSummaryDTO.getPlannedEndDate().equals(DateUtility.getCurrentTimeInYYYYMMDD())){
					
					this.totalNumberOfDaysCompleted=0;
				}else{
					this.totalNumberOfDaysCompleted=1;
				}
			}else{
				if(workPackageTestCaseSummaryDTO.getPlannedEndDate().equals(DateUtility.getCurrentTimeInYYYYMMDD())){
					this.totalNumberOfDays=difference;
					this.totalNumberOfDaysCompleted=difference-1;
				}else if(workPackageTestCaseSummaryDTO.getPlannedEndDate().before(DateUtility.getCurrentDateWithTime())){
					this.totalNumberOfDays=difference;
					this.totalNumberOfDaysCompleted=difference;
				}else{
					this.totalNumberOfDays=difference;
					this.totalNumberOfDaysCompleted=((Long)DateUtility.DateDifference(DateUtility.sdfDateformatWithOutTime(workPackageTestCaseSummaryDTO.getPlannedStartDate()),DateUtility.getCurrentTimeInYYYYMMDD())).intValue();
					this.totalNumberOfDaysCompleted=this.totalNumberOfDaysCompleted+1;
				}
			}
		}
		
		this.workpackageStatus=workPackageTestCaseSummaryDTO.getWorkpackageStatus();
		this.iterationNo = workPackageTestCaseSummaryDTO.getIterationNo();
		this.lifeCycleId = workPackageTestCaseSummaryDTO.getLifecyclePhaseId();
		if(workPackageTestCaseSummaryDTO.getLifecyclePhaseList() != null){
			this.lifeCycleNameList = workPackageTestCaseSummaryDTO.getLifecyclePhaseList();
		}else{
			HashMap<Integer, String> defaultValue= new HashMap<Integer, String>();
			defaultValue.put(-1, "--");
			this.lifeCycleNameList = defaultValue;
		}
		if(workPackageTestCaseSummaryDTO.getWpcreatedUser() != null){
			this.wpcreatedUser = workPackageTestCaseSummaryDTO.getWpcreatedUser();	
		}	
		if(workPackageTestCaseSummaryDTO.getJobsCount() != null){
			this.jobsCount = workPackageTestCaseSummaryDTO.getJobsCount();	
		}	
		if(workPackageTestCaseSummaryDTO.getTestRunJobsCompleted() != null){
			this.testRunJobsCompleted = workPackageTestCaseSummaryDTO.getTestRunJobsCompleted();
		}
	}	

	public JsonWorkPackageTestCaseExecutionSummary(WorkPackageBuildTestCaseSummaryDTO workPackageBuildTestCaseSummaryDTO){
		this.id = workPackageBuildTestCaseSummaryDTO.getWorkPackageId();
		this.workPackageName = workPackageBuildTestCaseSummaryDTO.getWorkPackageName();
		this.testRunJobId = workPackageBuildTestCaseSummaryDTO.getTestRunJobId();
		this.testRunStatus = workPackageBuildTestCaseSummaryDTO.getTestRunStatus();
		if(testRunStatus != null){
			if(testRunStatus == 3){
				this.testRunStatusName = "Executing";
			}else if(testRunStatus == 4){
				this.testRunStatusName = "Queued";
			}else if(testRunStatus == 5){
				this.testRunStatusName = "Completed";
			}else if(testRunStatus == 6){
				this.testRunStatusName = "Abort";
			}else if(testRunStatus == 7){
				this.testRunStatusName = "Aborted";
			}else if(testRunStatus == 8){
				this.testRunStatusName = "Restarted";
			}else{
				this.testRunStatusName = "---";
			}
		}
		
		this.testRunFailureMessage = workPackageBuildTestCaseSummaryDTO.getTestRunFailureMessage();
		this.testRunEvidenceMessage = workPackageBuildTestCaseSummaryDTO.getTestRunEvidenceStatus();
		this.passedCount = workPackageBuildTestCaseSummaryDTO.getPassedCount();
		this.failedCount = workPackageBuildTestCaseSummaryDTO.getFailedCount();
		this.blockedCount = workPackageBuildTestCaseSummaryDTO.getBlockedCount();
		this.norunCount = workPackageBuildTestCaseSummaryDTO.getNorunCount();
		this.notexecutedCount = workPackageBuildTestCaseSummaryDTO.getNotexecutedCount();
		this.totalTestCaseForExecutionCount = passedCount + failedCount + blockedCount + norunCount;
		this.totalTestCaseCount = passedCount + failedCount + blockedCount + norunCount + notexecutedCount;
		this.executedTestCasesCount = passedCount + failedCount;
		this.teststepcount = workPackageBuildTestCaseSummaryDTO.getTeststepcount();
		this.defectsCount = workPackageBuildTestCaseSummaryDTO.getDefectsCount();
		if(testRunStatusName != null){
			if(testRunStatusName.equalsIgnoreCase("Completed")){
				if(failedCount >0 || blockedCount >0 || norunCount >0){
					this.result = "Failed";
				}else if(passedCount >0){
					this.result = "Passed";
				}else{
					this.result = "Failed";
				}
			} else if(testRunStatusName.equalsIgnoreCase("Aborted")){
				this.result = "Failed";
			} else if(testRunStatusName.equalsIgnoreCase("Executing")){
				this.result = "In progress";
			} else if(testRunStatusName.equalsIgnoreCase("Queued")){
				this.result = "Not yet started execution";
			} else if(testRunStatusName.equalsIgnoreCase("Restarted")){
				this.result = "On Hold";
			}
		}else{
			this.result = "---";
		}	
	}

	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getWorkPackageName() {
		return workPackageName;
	}
	public void setWorkPackageName(String workPackageName) {
		this.workPackageName = workPackageName;
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
	public Integer getpBuildId() {
		return pBuildId;
	}
	public void setpBuildId(Integer pBuildId) {
		this.pBuildId = pBuildId;
	}
	public String getpBuildName() {
		return pBuildName;
	}
	public void setpBuildName(String pBuildName) {
		this.pBuildName = pBuildName;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
	public String getPlannedStartDate() {
		return plannedStartDate;
	}
	public void setPlannedStartDate(String plannedStartDate) {
		this.plannedStartDate = plannedStartDate;
	}
	public String getPlannedEndDate() {
		return plannedEndDate;
	}
	public void setPlannedEndDate(String plannedEndDate) {
		this.plannedEndDate = plannedEndDate;
	}
	public String getActualStartDate() {
		return actualStartDate;
	}
	public void setActualStartDate(String actualStartDate) {
		this.actualStartDate = actualStartDate;
	}
	public String getActualEndDate() {
		return actualEndDate;
	}
	public void setActualEndDate(String actualEndDate) {
		this.actualEndDate = actualEndDate;
	}
	public Integer getExecutedTestCasesCount() {
		return executedTestCasesCount;
	}
	public void setExecutedTestCasesCount(Integer executedTestCasesCount) {
		this.executedTestCasesCount = executedTestCasesCount;
	}
	public Integer getDefectiveTestCasesCount() {
		return defectiveTestCasesCount;
	}
	public void setDefectiveTestCasesCount(Integer defectiveTestCasesCount) {
		this.defectiveTestCasesCount = defectiveTestCasesCount;
	}
	public Integer getApprovedTestCasesCount() {
		return approvedTestCasesCount;
	}
	public void setApprovedTestCasesCount(Integer approvedTestCasesCount) {
		this.approvedTestCasesCount = approvedTestCasesCount;
	}
	public Integer getRejectedTestCasesCount() {
		return rejectedTestCasesCount;
	}
	public void setRejectedTestCasesCount(Integer rejectedTestCasesCount) {
		this.rejectedTestCasesCount = rejectedTestCasesCount;
	}
	public String getExecutedDate() {
		return executedDate;
	}
	public void setExecutedDate(String executedDate) {
		this.executedDate = executedDate;
	}
	public Integer getTestCasesCount() {
		return testCasesCount;
	}
	public void setTestCasesCount(Integer testCasesCount) {
		this.testCasesCount = testCasesCount;
	}
	public Integer getDefectsCount() {
		return defectsCount;
	}
	public void setDefectsCount(Integer defectsCount) {
		this.defectsCount = defectsCount;
	}
	public Integer getApprovedTestCaseCount() {
		return approvedTestCaseCount;
	}
	public void setApprovedTestCaseCount(Integer approvedTestCaseCount) {
		this.approvedTestCaseCount = approvedTestCaseCount;
	}
	public Integer getRejectedTestCaseCount() {
		return rejectedTestCaseCount;
	}
	public void setRejectedTestCaseCount(Integer rejectedTestCaseCount) {
		this.rejectedTestCaseCount = rejectedTestCaseCount;
	}
	public Integer getTotalTestCaseForExecutionCount() {
		return totalTestCaseForExecutionCount;
	}
	public void setTotalTestCaseForExecutionCount(
			Integer totalTestCaseForExecutionCount) {
		this.totalTestCaseForExecutionCount = totalTestCaseForExecutionCount;
	}
	public Integer getCompletedTestCaseCount() {
		return completedTestCaseCount;
	}
	public void setCompletedTestCaseCount(Integer completedTestCaseCount) {
		this.completedTestCaseCount = completedTestCaseCount;
	}
	
	
	public Float getCompletedTestCasePercentage() {
		return completedTestCasePercentage;
	}
	public void setCompletedTestCasePercentage(Float completedTestCasePercentage) {
		this.completedTestCasePercentage = completedTestCasePercentage;
	}
	public Integer getNotCompletedTestCaseCount() {
		return notCompletedTestCaseCount;
	}
	public void setNotCompletedTestCaseCount(Integer notCompletedTestCaseCount) {
		this.notCompletedTestCaseCount = notCompletedTestCaseCount;
	}
	public Integer getSelectedTestCasesCount() {
		return selectedTestCasesCount;
	}
	public void setSelectedTestCasesCount(Integer selectedTestCasesCount) {
		this.selectedTestCasesCount = selectedTestCasesCount;
	}
	public Integer getTotalTestCaseCount() {
		return totalTestCaseCount;
	}
	public void setTotalTestCaseCount(Integer totalTestCaseCount) {
		this.totalTestCaseCount = totalTestCaseCount;
	}
	public Integer getPlannedExecutionDateCount() {
		return plannedExecutionDateCount;
	}
	public void setPlannedExecutionDateCount(Integer plannedExecutionDateCount) {
		this.plannedExecutionDateCount = plannedExecutionDateCount;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public HashMap getWorkFlowstageNameList() {
		return workFlowstageNameList;
	}
	public void setWorkFlowstageNameList(HashMap workFlowstageNameList) {
		this.workFlowstageNameList = workFlowstageNameList;
	}
	public String getEnvironmentList() {
		return environmentList;
	}
	public void setEnvironmentList(String environmentList) {
		this.environmentList = environmentList;
	}
	public Integer getEnvironmentCount() {
		return environmentCount;
	}
	public void setEnvironmentCount(Integer environmentCount) {
		this.environmentCount = environmentCount;
	}
	public Integer getApprovedDefectsCount() {
		return approvedDefectsCount;
	}
	public void setApprovedDefectsCount(Integer approvedDefectsCount) {
		approvedDefectsCount = approvedDefectsCount;
	}
	public Integer getTotalDefectsCount() {
		return totalDefectsCount;
	}
	public void setTotalDefectsCount(Integer totalDefectsCount) {
		this.totalDefectsCount = totalDefectsCount;
	}
	public String getShiftName() {
		return shiftName;
	}
	public void setShiftName(String shiftName) {
		this.shiftName = shiftName;
	}
	public Integer getTotalNumberOfDays() {
		return totalNumberOfDays;
	}
	public void setTotalNumberOfDays(Integer totalNumberOfDays) {
		this.totalNumberOfDays = totalNumberOfDays;
	}
	public Integer getTotalNumberOfDaysCompleted() {
		return totalNumberOfDaysCompleted;
	}
	public void setTotalNumberOfDaysCompleted(Integer totalNumberOfDaysCompleted) {
		this.totalNumberOfDaysCompleted = totalNumberOfDaysCompleted;
	}
	public Integer getWorkpackageStatus() {
		return workpackageStatus;
	}
	public void setWorkpackageStatus(Integer workpackageStatus) {
		this.workpackageStatus = workpackageStatus;
	}
	public String getEnvironmentCombination() {
		return environmentCombination;
	}
	public void setEnvironmentCombination(String environmentCombination) {
		this.environmentCombination = environmentCombination;
	}
	public String getRunConfiguration() {
		return runConfiguration;
	}
	public void setRunConfiguration(String runConfiguration) {
		this.runConfiguration = runConfiguration;
	}
	
		
	public String getWorkPackageType() {
		return workPackageType;
	}
	public void setWorkPackageType(String workPackageType) {
		this.workPackageType = workPackageType;
	}
	public Integer getSelectedTestSuitesCount() {
		return selectedTestSuitesCount;
	}
	public void setSelectedTestSuitesCount(Integer selectedTestSuitesCount) {
		this.selectedTestSuitesCount = selectedTestSuitesCount;
	}
	public Integer getSelectedFeaturesCount() {
		return selectedFeaturesCount;
	}
	public void setSelectedFeaturesCount(Integer selectedFeaturesCount) {
		this.selectedFeaturesCount = selectedFeaturesCount;
	}
	public Integer getTotalNumberOfCompDays() {
		return totalNumberOfCompDays;
	}
	public void setTotalNumberOfCompDays(Integer totalNumberOfCompDays) {
		this.totalNumberOfCompDays = totalNumberOfCompDays;
	}
	public String getProductType() {
		return productType;
	}
	public void setProductType(String productType) {
		this.productType = productType;
	}
	public Integer getIterationNo() {
		return iterationNo;
	}
	public void setIterationNo(Integer iterationNo) {
		this.iterationNo = iterationNo;
	}
	public Integer getLifeCycleId() {
		return lifeCycleId;
	}
	public void setLifeCycleId(Integer lifeCycleId) {
		this.lifeCycleId = lifeCycleId;
	}
	public HashMap getLifeCycleNameList() {
		return lifeCycleNameList;
	}
	public void setLifeCycleNameList(HashMap lifeCycleNameList) {
		this.lifeCycleNameList = lifeCycleNameList;
	}
	public Integer getTestRunJobId() {
		return testRunJobId;
	}
	public void setTestRunJobId(Integer testRunJobId) {
		this.testRunJobId = testRunJobId;
	}
	public Integer getTestRunStatus() {
		return testRunStatus;
	}
	public void setTestRunStatus(Integer testRunStatus) {
		this.testRunStatus = testRunStatus;
	}
	public String getTestRunFailureMessage() {
		return testRunFailureMessage;
	}
	public void setTestRunFailureMessage(String testRunFailureMessage) {
		this.testRunFailureMessage = testRunFailureMessage;
	}
	public String getTestRunEvidenceMessage() {
		return testRunEvidenceMessage;
	}
	public void setTestRunEvidenceMessage(String testRunEvidenceMessage) {
		this.testRunEvidenceMessage = testRunEvidenceMessage;
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
	public String getWpcreatedUser() {
		return wpcreatedUser;
	}
	public void setWpcreatedUser(String wpcreatedUser) {
		this.wpcreatedUser = wpcreatedUser;
	}
	public Integer getJobsCount() {
		return jobsCount;
	}
	public void setJobsCount(Integer jobsCount) {
		this.jobsCount = jobsCount;
	}
	public Integer getTestRunJobsCompleted() {
		return testRunJobsCompleted;
	}
	public void setTestRunJobsCompleted(Integer testRunJobsCompleted) {
		this.testRunJobsCompleted = testRunJobsCompleted;
	}
	public String getTestRunStatusName() {
		return testRunStatusName;
	}
	public void setTestRunStatusName(String testRunStatusName) {
		this.testRunStatusName = testRunStatusName;
	}
	public String getResult() {
		return result;
	}
	public void setResult(String result) {
		this.result = result;
	}
	public Integer getTeststepcount() {
		return teststepcount;
	}
	public void setTeststepcount(Integer teststepcount) {
		this.teststepcount = teststepcount;
	}
	public Integer getTestPlanId() {
		return testPlanId;
	}
	public void setTestPlanId(Integer testPlanId) {
		this.testPlanId = testPlanId;
	}
	public String getAutomationMode() {
		return automationMode;
	}
	public void setAutomationMode(String automationMode) {
		this.automationMode = automationMode;
	}
	public String getUseIntelligentTestPlan() {
		return useIntelligentTestPlan;
	}
	public void setUseIntelligentTestPlan(String useIntelligentTestPlan) {
		this.useIntelligentTestPlan = useIntelligentTestPlan;
	}
	
	@JsonIgnore
	public JSONObject getCleanJson() {
		
		JSONObject responseJson = new JSONObject();
		try {
			responseJson.put("testRunJob", testRunJobId);
			responseJson.put("environmentCombination", environmentCombination);
			responseJson.put("testRunstatus", testRunStatusName);
			responseJson.put("result", result);
			responseJson.put("pass",passedCount );
			responseJson.put("fail", failedCount);
			responseJson.put("productId", productId);
			responseJson.put("productName", productName);
			responseJson.put("buildId", pBuildId);
			responseJson.put("buildName", pBuildName);
			responseJson.put("plannedStartDate", plannedStartDate);
			responseJson.put("plannedEndDate", plannedEndDate);
			responseJson.put("workPackageName", workPackageName);
			responseJson.put("totalExecutionTestcase", totalTestCaseForExecutionCount);
			responseJson.put("totalTestcase", totalTestCaseCount);
			responseJson.put("notExecuted", notexecutedCount);
			responseJson.put("noRun", norunCount);
			responseJson.put("blocked", blockedCount);
			
		}catch(Exception e) {
			
		}
		return responseJson;
	}
}
