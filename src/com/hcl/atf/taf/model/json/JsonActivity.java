package com.hcl.atf.taf.model.json;

import java.util.Date;

import org.json.JSONObject;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.hcl.atf.taf.controller.utilities.DateUtility;
import com.hcl.atf.taf.model.Activity;
import com.hcl.atf.taf.model.ActivityMaster;
import com.hcl.atf.taf.model.ActivityWorkPackage;
import com.hcl.atf.taf.model.ClarificationTracker;
import com.hcl.atf.taf.model.ExecutionPriority;
import com.hcl.atf.taf.model.ExecutionTypeMaster;
import com.hcl.atf.taf.model.ProductFeature;
import com.hcl.atf.taf.model.ProductMaster;
import com.hcl.atf.taf.model.StatusCategory;
import com.hcl.atf.taf.model.UserList;
import com.hcl.atf.taf.model.dto.ActivitiesDTO;
import com.hcl.ilcm.workflow.model.WorkflowStatus;

public class JsonActivity {

	@JsonProperty
	private Integer activityId;
	@JsonProperty
	private String activityName;
	@JsonProperty
	private Integer activityWorkPackageId;
	@JsonProperty
	private String activityWorkPackageName;
	@JsonProperty
	private Integer productFeatureId;
	@JsonProperty
	private String productFeatureName;
	
    @JsonProperty
	private String description;
	@JsonProperty
	private Integer activityMasterId;
	@JsonProperty
	private String activityMasterName;
	@JsonProperty
	private Integer clarificationTrackerId;
	@JsonProperty
	private String activityTrackerNumber;
	@JsonProperty
	private Integer drReferenceNumber;
	@JsonProperty
	private Integer categoryId;
	@JsonProperty
	private String categoryName;
	@JsonProperty
	private String clarificationNumber;
	@JsonProperty
	private Integer assigneeId;
	@JsonProperty
	private String assigneeName;
	@JsonProperty
	private Integer reviewerId;
	@JsonProperty
	private String reviewerName;
	@JsonProperty
	private Integer autoAllocateReferenceId;
	@JsonProperty
	private Integer isImmidiateAutoAllocation;
	@JsonProperty
	private Integer partitionId;
	@JsonProperty
	private String partitionName;
	@JsonProperty
	private Integer statusCategoryId;
	@JsonProperty
	private String statusCategoryName;
	@JsonProperty
	private Integer priorityId;
	@JsonProperty
	private String priorityName;
	@JsonProperty
	private String remark;
	@JsonProperty
	private String baselineStartDate;
	@JsonProperty
	private String baselineEndDate;
	@JsonProperty
	private String plannedStartDate;
	@JsonProperty
	private String plannedEndDate;
	@JsonProperty
	private String actualStartDate;
	@JsonProperty
	private String actualEndDate;
	@JsonProperty
	private String createdDate;
	@JsonProperty
	private String modifiedDate;
	@JsonProperty
	private Integer createdById;
	@JsonProperty
	private String createdByName;
	@JsonProperty
	private Integer modifiedById;
	@JsonProperty
	private String modifiedByName;
	@JsonProperty
	private Integer isActive;
	@JsonProperty
	private Integer baselineActivitySize;
	@JsonProperty
	private Integer plannedActivitySize;
	@JsonProperty
	private Integer actualActivitySize;
	@JsonProperty
	private Integer taskOpenStatusValue;
	@JsonProperty
	private Integer taskOnHoldStatusRecords;
	@JsonProperty
	private Integer taskCompletedStatusRecords;
	@JsonProperty
	private Integer totalTaskCount;
	@JsonProperty
	private Integer loggedInUserTaskCount = 0;
	@JsonProperty
	private Integer taskInProgressStatusRecords = 0;
	@JsonProperty
	private String enviCombination;
	@JsonProperty
	private String changeRequest;
	@JsonProperty
	private String productName;
	@JsonProperty
	private String engagementName;

	@JsonProperty
	private Integer statusId;
	@JsonProperty
	private String statusName;
	@JsonProperty
	private String statusDisplayName;
	@JsonProperty
	private String workflowStatusCategoryName;
	@JsonProperty
	private Integer totalEffort;
	@JsonProperty
	private Integer workflowId;
	@JsonProperty
	private String actors;
	@JsonProperty
	private String completedBy;
	@JsonProperty
	private Integer remainingHours;
	@JsonProperty
	private String workflowIndicator;
	@JsonProperty
	private String workflowRAG;
	@JsonProperty
	private Integer attachmentCount;
	
	@JsonProperty
	private String remainingHrsMins;
	@JsonProperty
	private Integer lifeCycleStageId;
	@JsonProperty
	private String lifeCycleStageName;
	@JsonProperty
	private String modifiedField;
	@JsonProperty
	private String 	modifiedFieldTitle;	
	@JsonProperty
	private String oldFieldID;
	@JsonProperty
	private String	oldFieldValue;
	@JsonProperty
	private String	modifiedFieldID;
	@JsonProperty	
	private String modifiedFieldValue;
	@JsonProperty
	private Float percentageCompletion;
	
	@JsonProperty
	private Integer productId;
	@JsonProperty
	private Integer baselineEffort;
	@JsonProperty
	private Integer plannedEffort;
	@JsonProperty
	private String complexity;
	@JsonProperty
	private Integer changeRequestCount;
	@JsonProperty
	private Integer clarificationCount;
	@JsonProperty
	private Float weightage;
	@JsonProperty
	private Float baselineUnit;
	@JsonProperty
	private Float plannedUnit;
	@JsonProperty
	private Float actualUnit;
	
	@JsonProperty
	private boolean visibleEventComment;
	
	@JsonProperty
	private Integer userTagActivity;
	
	@JsonProperty
	private Integer engagementId;
	
	@JsonProperty
	private Integer productBuildId;
	
	@JsonProperty
	private String productBuildName;
	
	@JsonProperty
	private String productVersionName;
	
	@JsonProperty
	private String isModified;
	
	@JsonProperty
	private String workflowStatusType;
	
	@JsonProperty
	private String latestComment;
	
	private Date tempModifiedDate;
	
	private String customFields;
	
	private String from;
	
	private String to;
	
	private String progressIndicator;
	
	private String activityPredecessors;
	
	private String activityWorkPackageOwner;
	
	public JsonActivity() {
	}

	public JsonActivity(Activity activity) {
		this.activityId = activity.getActivityId();
		this.activityName = activity.getActivityName();
		this.activityTrackerNumber = activity.getActivityTrackerNumber();
		this.drReferenceNumber = activity.getDrReferenceNumber();
		this.activityTrackerNumber = activity.getActivityTrackerNumber();
		this.remark = activity.getRemark();
		this.plannedActivitySize = activity.getPlannedActivitySize();
		this.actualActivitySize = activity.getActualActivitySize();
		this.complexity = activity.getComplexity();
		//this.totalEffort = activity.getTotalEffort();
		if(activity.getBaselineActivitySize() != null){
			this.baselineActivitySize = activity.getBaselineActivitySize();
		}else{
			this.baselineActivitySize = this.plannedActivitySize;
		}
		if (activity.getActivityWorkPackage() != null) {
			this.activityWorkPackageId = activity.getActivityWorkPackage()
					.getActivityWorkPackageId();	
			this.activityWorkPackageName = activity.getActivityWorkPackage()
					.getActivityWorkPackageName();
			if(activity.getActivityWorkPackage().getOwner() != null) {
				this.activityWorkPackageOwner = activity.getActivityWorkPackage().getOwner().getLoginId();
			}
		}

        if (activity.getPriority() != null) {
			this.priorityId = activity.getPriority().getExecutionPriorityId();
			this.priorityName = activity.getPriority()
					.getExecutionPriorityName();
		}
		if (activity.getIsActive()!= null) {
			this.isActive= activity.getIsActive();
			
		}
		
		if (activity.getStatusCategory() != null) {
			this.statusCategoryId = activity.getStatusCategory().getStatusCategoryId();
			this.statusCategoryName = activity.getStatusCategory().getStatusCategoryName();
		}
		if (activity.getCategory() != null) {
			this.categoryId = activity.getCategory().getExecutionTypeId();
			this.categoryName = activity.getCategory().getName();
		}

		if (activity.getAssignee() != null) {
			this.assigneeId = activity.getAssignee().getUserId();
			this.assigneeName = activity.getAssignee().getLoginId();
		}
		if (activity.getProductFeature() != null) {
			this.productFeatureId = activity.getProductFeature()
					.getProductFeatureId();
			this.productFeatureName = activity.getProductFeature()
					.getProductFeatureName();
		}
		if (activity.getReviewer() != null) {
			this.reviewerId = activity.getReviewer().getUserId();
			this.reviewerName = activity.getReviewer().getLoginId();
		}
		this.autoAllocateReferenceId = activity.getAutoAllocateReferenceId();
		if (activity.getActivityMaster() != null) {
			this.activityMasterId = activity.getActivityMaster().getActivityMasterId();
			this.activityMasterName = activity.getActivityMaster().getActivityMasterName();
			this.weightage = activity.getActivityMaster().getWeightage();
		}
		if (activity.getClarificationTracker() != null) {
			this.clarificationTrackerId = activity.getClarificationTracker()
					.getClarificationTrackerId();

		}


		if(activity.getCreatedDate() != null){
			this.createdDate = DateUtility.dateToStringWithSeconds1(activity.getCreatedDate());
		}
		if(activity.getModifiedDate() != null){
			this.modifiedDate = DateUtility.dateformatWithOutTime(activity.getModifiedDate());
		}
		if(activity.getModifiedDate() != null){
			this.tempModifiedDate=activity.getModifiedDate();
		}
		
		
		
		if (activity.getCreatedBy() != null) {

			this.createdById = activity.getCreatedBy().getUserId();
			this.createdByName = activity.getCreatedBy().getLoginId();
		}

		if (activity.getModifiedBy() != null) {

			this.modifiedById = activity.getModifiedBy().getUserId();
			this.modifiedByName = activity.getModifiedBy().getLoginId();

		}

		if (activity.getPlannedStartDate() != null) {
			this.plannedStartDate = DateUtility.dateformatWithOutTime(activity
					.getPlannedStartDate());
		}else {
			this.plannedStartDate = "dd-mm-yy";
		}

		if (activity.getPlannedEndDate() != null) {
			this.plannedEndDate = DateUtility.dateformatWithOutTime(activity
					.getPlannedEndDate());
		}else {
			this.plannedEndDate = "dd-mm-yy";
		}
		
		if (activity.getPlannedStartDate() != null) {
			this.from = DateUtility.dateToStringWithoutHyphenSecond(activity
					.getPlannedStartDate());
		}

		if (activity.getPlannedEndDate() != null) {
			this.to = DateUtility.dateToStringWithoutHyphenSecond(activity
					.getPlannedEndDate());
		}

		if (activity.getBaselineStartDate() != null) {
			this.baselineStartDate = DateUtility.dateformatWithOutTime(activity.getBaselineStartDate());
		}else {
			this.baselineStartDate = this.plannedStartDate;
		}

		if (activity.getBaselineEndDate() != null) {
			this.baselineEndDate = DateUtility.dateformatWithOutTime(activity.getBaselineEndDate());
		}else {
			this.baselineEndDate = this.plannedEndDate;
		}
		
		if (activity.getActualStartDate() != null) {
			this.actualStartDate = DateUtility.dateformatWithOutTime(activity
					.getActualStartDate());
		} else {
			this.actualStartDate = "dd-mm-yy";
		}

		if (activity.getActualEndDate() != null) {
			this.actualEndDate = DateUtility.dateformatWithOutTime(activity
					.getActualEndDate());
		}else {
			this.actualEndDate = "dd-mm-yy";
		}
		
		if(activity.getActivityWorkPackage().getProductBuild().getProductVersion().getProductMaster() != null){
			this.productId = activity.getActivityWorkPackage().getProductBuild().getProductVersion().getProductMaster().getProductId();
			this.productName = activity.getActivityWorkPackage().getProductBuild().getProductVersion().getProductMaster().getProductName();
		}
		
		if(activity.getActivityWorkPackage().getProductBuild().getProductVersion().getProductMaster().getTestFactory() != null){
			 this.engagementId = activity.getActivityWorkPackage().getProductBuild().getProductVersion().getProductMaster().getTestFactory().getTestFactoryId();
		    this.engagementName = activity.getActivityWorkPackage().getProductBuild().getProductVersion().getProductMaster().getTestFactory().getTestFactoryName();
		}

		if (activity.getActivityWorkPackage().getProductBuild() != null)
		{ 
			this.productBuildId = activity.getActivityWorkPackage().getProductBuild().getProductBuildId();
			this.productBuildName = activity.getActivityWorkPackage().getProductBuild().getBuildname();
			
			if(activity.getActivityWorkPackage().getProductBuild().getProductVersion() != null){
				this.productVersionName = activity.getActivityWorkPackage().getProductBuild().getProductVersion().getProductVersionName();			
			}
		}
		
		
		if(activity.getWorkflowStatus() != null){
			this.statusId = activity.getWorkflowStatus().getWorkflowStatusId();
			this.statusName = activity.getWorkflowStatus().getWorkflowStatusName();
			this.statusDisplayName = activity.getWorkflowStatus().getWorkflowStatusDisplayName();
			this.workflowStatusType = activity.getWorkflowStatus().getWorkflowStatusType();
			if(activity.getWorkflowStatus().getWorkflowStatusCategory() != null){
	    		   this.workflowStatusCategoryName = "["+activity.getWorkflowStatus().getWorkflowStatusCategory().getWorkflowStatusCategoryName()+"]";
			}else{
				this.workflowStatusCategoryName = "";
			}
		}else{
			this.statusName = "--";
			this.statusDisplayName = "--";
			this.workflowStatusCategoryName = "";
		}
		
		if(activity.getTotalEffort() != null){
			this.totalEffort = activity.getTotalEffort();
		}else{
			this.totalEffort = 0;
		}
		
		if(activity.getPlannedEffort() != null){
			this.plannedEffort = activity.getPlannedEffort();
		}else{
			this.plannedEffort = 0;
		}
		
		if(activity.getBaselineEffort() != null){
			this.baselineEffort = activity.getBaselineEffort();
		}else{
			this.baselineEffort = this.plannedEffort;
		}
		
		this.actors = actors;
		this.completedBy=completedBy;
		this.remainingHours=remainingHours;
		this.workflowIndicator = workflowIndicator;
		this.remainingHrsMins = remainingHrsMins;
		this.workflowRAG = workflowRAG;
				
		if(activity.getLifeCycleStage() != null){
			this.lifeCycleStageId = activity.getLifeCycleStage().getWorkflowStatusId();
			this.lifeCycleStageName = activity.getLifeCycleStage().getWorkflowStatusDisplayName();
		}
		if(activity.getPercentageCompletion() != null){
			this.percentageCompletion = activity.getPercentageCompletion();
		}else{
			this.percentageCompletion = 0.0F;
		}
		this.visibleEventComment=visibleEventComment;
		this.userTagActivity=userTagActivity;
		if(activity.getWorkflowStatus() != null && activity.getWorkflowStatus().getWorkflow() != null) {
			this.workflowId = activity.getWorkflowStatus().getWorkflow().getWorkflowId();
		} else {
			this.workflowId =0;
		}
		
		this.customFields = customFields;
		this.progressIndicator = progressIndicator;
		this.activityPredecessors = activity.getActivityPredecessors();
	}

	@JsonIgnore
	public Activity getActivity() {
		Activity activity = new Activity();
		activity.setActivityId(activityId);
		activity.setActivityName(activityName);		
		activity.setActivityTrackerNumber(activityTrackerNumber);
		activity.setDrReferenceNumber(drReferenceNumber);
		activity.setIsActive(isActive);
		activity.setPlannedEffort(plannedEffort);
		activity.setTotalEffort(totalEffort);
		activity.setBaselineUnit(baselineUnit);
		activity.setPlannedUnit(plannedUnit);
		activity.setActualUnit(actualUnit);
		activity.setRemark(remark);
		if(this.baselineEffort != null){
			activity.setBaselineEffort(this.baselineEffort);
		}else{
			activity.setBaselineEffort(this.plannedEffort);
		}
		activity.setComplexity(complexity);		
		
		if (this.activityWorkPackageId != null) {
			ActivityWorkPackage awp = new ActivityWorkPackage();
			awp.setActivityWorkPackageId(activityWorkPackageId);			
			activity.setActivityWorkPackage(awp);

		}
		StatusCategory statusCategory = new StatusCategory();
		if (this.statusCategoryId != null && this.statusCategoryId > 0) {
			statusCategory.setStatusCategoryId(statusCategoryId);
			statusCategory.setStatusCategoryName(statusCategoryName);
		}else{
			statusCategory.setStatusCategoryId(1);
		}
		activity.setStatusCategory(statusCategory);
		
		if (this.priorityId != null) {
			ExecutionPriority priority = new ExecutionPriority();
			priority.setExecutionPriorityId(priorityId);
			priority.setExecutionPriorityName(priorityName);
			activity.setPriority(priority);
		}
		if (this.categoryId != null) {
			ExecutionTypeMaster executionTypeMaster = new ExecutionTypeMaster();
			executionTypeMaster.setExecutionTypeId(categoryId);
			executionTypeMaster.setName(categoryName);
			activity.setCategory(executionTypeMaster);
		}
		if (this.assigneeId != null) {
			UserList userlist = new UserList();
			userlist.setUserId(assigneeId);
			userlist.setLoginId(assigneeName);
			activity.setAssignee(userlist);
		}
		if (this.reviewerId != null) {
			UserList userlist = new UserList();
			userlist.setUserId(reviewerId);
			userlist.setLoginId(reviewerName);
			activity.setReviewer(userlist);
		}
		if(this.autoAllocateReferenceId == null || this.autoAllocateReferenceId == 0){
			activity.setAutoAllocateReferenceId(null);
		}else{
			activity.setAutoAllocateReferenceId(this.autoAllocateReferenceId);
		}
		if (this.productFeatureId != null) {
			ProductFeature productFeature = new ProductFeature();
			productFeature.setProductFeatureId(productFeatureId);
			productFeature.setProductFeatureName(productFeatureName);
			activity.setProductFeature(productFeature);
		}
		if (this.activityMasterId != null) {
			ActivityMaster activityMaster = new ActivityMaster();
			activityMaster.setActivityMasterId(activityMasterId);
			activityMaster.setActivityMasterName(activityMasterName);
			activityMaster.setWeightage(weightage);
			activity.setActivityMaster(activityMaster);
		}
		if (this.clarificationTrackerId != null) {
			ClarificationTracker activityType = new ClarificationTracker();
			activityType.setClarificationTrackerId(clarificationTrackerId);
		}

		if (this.plannedStartDate != null) {
			if(this.plannedStartDate.equals("mm/dd/yy")){
	             activity.setPlannedStartDate(null);	
			}
	       else if(this.plannedStartDate!="mm/dd/yy"){		
			activity.setPlannedStartDate(DateUtility
					.dateformatWithOutTime(this.plannedStartDate));
	       }
		}

		if (this.plannedEndDate != null) {
			
			if(this.plannedEndDate.equals("mm/dd/yy")){
	             activity.setPlannedEndDate(null);	
			}
	       else if(this.plannedEndDate!="mm/dd/yy"){		
			activity.setPlannedEndDate(DateUtility
					.dateformatWithOutTime(this.plannedEndDate));
	       }
		}
		
		if (this.baselineStartDate != null) {
			if(this.baselineStartDate.equals("mm/dd/yy")){
	             activity.setBaselineStartDate(activity.getPlannedStartDate());	
			}
	       else if(this.baselineStartDate!="mm/dd/yy"){		
			activity.setBaselineStartDate(DateUtility
					.dateformatWithOutTime(this.baselineStartDate));
	       }
		}

		if (this.baselineEndDate != null) {
			
			if(this.baselineEndDate.equals("mm/dd/yy")){
	             activity.setBaselineEndDate(activity.getPlannedEndDate());	
			}
	       else if(this.baselineEndDate!="mm/dd/yy"){		
			activity.setBaselineEndDate(DateUtility
					.dateformatWithOutTime(this.baselineEndDate));
	       }
		}
		
		if (this.actualStartDate != null) {
			if(this.actualStartDate.equals("mm/dd/yy"))
		             activity.setActualStartDate(null);				
		       else if(this.actualStartDate!="mm/dd/yy")
			activity.setActualStartDate(DateUtility.dateformatWithOutTime(this.actualStartDate));
		 }
			
		if (this.actualEndDate != null) {
			if(this.actualEndDate.equals("mm/dd/yy"))
	             activity.setActualEndDate(null);				
	       else if(this.actualEndDate!="mm/dd/yy")
		activity.setActualEndDate(DateUtility.dateformatWithOutTime(this.actualEndDate));
	 }
		
		if(this.createdById!=null){
			UserList user=new UserList();
			user.setUserId(createdById);
			user.setLoginId(createdByName);
			activity.setCreatedBy(user);
		}
		
		if(this.modifiedById!=null){
			UserList user=new UserList();
			user.setUserId(createdById);
			user.setLoginId(createdByName);
			activity.setModifiedBy(user);
		}
		
		
		if(this.createdDate == null || this.createdDate.trim().isEmpty()) {
			activity.setCreatedDate(DateUtility.getCurrentTime());
		} else {		
			activity.setCreatedDate(DateUtility.toFormatDate(this.createdDate));
		}
		activity.setModifiedDate(DateUtility.getCurrentTime());
		
		if(this.statusId != null){
			WorkflowStatus workflowStatus = new WorkflowStatus();
			workflowStatus.setWorkflowStatusId(this.statusId);
			activity.setWorkflowStatus(workflowStatus);
		}
		
		if(this.lifeCycleStageId != null && this.lifeCycleStageId>0){
			WorkflowStatus workflowStatus = new WorkflowStatus();
			workflowStatus.setWorkflowStatusId(this.lifeCycleStageId);
			activity.setLifeCycleStage(workflowStatus);
		}
		
		if(this.productId != null){
			ProductMaster productMaster = new ProductMaster();
			productMaster.setProductId(this.productId);
			activity.setProductMaster(productMaster);
			
			
		}
		
		if(this.percentageCompletion != null){
			activity.setPercentageCompletion(this.percentageCompletion);
		}else{
			activity.setPercentageCompletion(0.0F);
		}
		
		if(this.plannedActivitySize != null){
			activity.setPlannedActivitySize(this.plannedActivitySize);
		}
		
		if(this.baselineActivitySize != null){
			activity.setBaselineActivitySize(this.baselineActivitySize);
		}else{
			activity.setBaselineActivitySize(this.plannedActivitySize);
		}
		
		if(this.actualActivitySize != null){
			activity.setActualActivitySize(this.actualActivitySize);
		}
		
		if(this.activityPredecessors != null) {
			activity.setActivityPredecessors(this.activityPredecessors);
		}
		
		return activity;
	}

	public JsonActivity(ActivitiesDTO activityDTO) {
	
		
			this.taskOpenStatusValue = activityDTO.getTaskOpenStatusValue();		
			this.taskOnHoldStatusRecords = activityDTO.getTaskOnHoldStatusRecords();
			this.taskCompletedStatusRecords = activityDTO.getTaskCompletedStatusRecords();
			this.totalTaskCount = activityDTO.getTotalTaskCount();		
			this.taskInProgressStatusRecords = activityDTO.getTaskInProgressStatusRecords();
			this.loggedInUserTaskCount = activityDTO.getLoggedInUserTaskCount();	
			this.activityName = activityDTO.getActivityName();
			
			this.weightage=activityDTO.getWeightage();
			this.activityWorkPackageName = activityDTO.getActivityWorkPackage().getActivityWorkPackageName();
			this.activityWorkPackageId = activityDTO.getActivityWorkPackage().getActivityWorkPackageId();
			this.productFeatureId=activityDTO.getProductFeatureId();
			this.productFeatureName=activityDTO.getProductFeatureName();
			this.activityTrackerNumber = activityDTO.getActivityTrackerNumber();			
			this.remark = activityDTO.getRemark();
			this.plannedActivitySize = activityDTO.getPlannedActivitySize();
			this.baselineActivitySize = activityDTO.getBaselineActivitySize();
			this.actualActivitySize = activityDTO.getActualActivitySize();			
			this.priorityId = activityDTO.getPriorityId();
			this.priorityName = activityDTO.getPriorityName();
			this.isActive= activityDTO.getIsActive();
			this.statusCategoryId = activityDTO.getStatusCategoryId(); 
			this.statusCategoryName = activityDTO.getStatusCategoryName();
			this.categoryName = activityDTO.getCategoryName();
			this.categoryId = activityDTO.getCategoryId();	
			this.assigneeName =  activityDTO.getAssigneeName();	
			this.assigneeId =  activityDTO.getAssigneeId();
			this.reviewerId = activityDTO.getReviewerId();
			this.reviewerName = activityDTO.getReviewerName();
			this.activityMasterId=activityDTO.getActivityMasterId();
			this.activityMasterName = activityDTO.getActivityMasterName();
			this.clarificationNumber = activityDTO.getClarificationNumber();
		    this.createdDate =activityDTO.getCreatedDate();			
			this.modifiedDate = activityDTO.getModifiedDate();
			this.tempModifiedDate = DateUtility.getddmmyyyytoyyyymmddwithSec(this.modifiedDate);
			this.createdById = activityDTO.getCreatedById();
			this.createdByName =   activityDTO.getCreatedByName();
			this.modifiedById = activityDTO.getModifiedById();
			this.modifiedByName =  activityDTO.getModifiedByName();
		    this.plannedStartDate =  activityDTO.getPlannedStartDate();
			this.plannedEndDate = activityDTO.getPlannedEndDate();			
			this.baselineStartDate =  activityDTO.getBaselineStartDate();
			this.baselineEndDate = activityDTO.getBaselineEndDate();			
			this.actualStartDate = activityDTO.getActualStartDate();			
			this.actualEndDate = activityDTO.getActualEndDate();
			this.enviCombination=activityDTO.getEnvironmentCombinations();
			this.changeRequestCount=activityDTO.getChangeRequestCount();
			this.clarificationCount=activityDTO.getClarificationCount();
			this.productName=activityDTO.getProductName();
			this.engagementId = activityDTO.getEngagementId();
			this.engagementName=activityDTO.getEngagementName();
			this.complexity = activityDTO.getComplexity();
			this.totalEffort = activityDTO.getTotalEffort();
			this.plannedEffort = activityDTO.getPlannedEffort();
			this.baselineEffort = activityDTO.getBaselineEffort();
			this.actualUnit = activityDTO.getActualUnit();
			this.plannedUnit = activityDTO.getPlannedUnit();
			this.baselineUnit = activityDTO.getBaselineUnit();
			
			if(activityDTO.getProductMaster() != null){
				this.productId = activityDTO.getProductMaster().getProductId();
			}
			if(activityDTO.getActivity() != null){
				this.activityId = activityDTO.getActivity().getActivityId();
			}
			this.percentageCompletion = activityDTO.getPercentageCompletion();
			
			this.statusId = activityDTO.getStatusId();
			this.statusName = activityDTO.getStatusName();
			this.statusDisplayName = activityDTO.getStatusDisplayName(); 
			this.workflowStatusCategoryName = activityDTO.getWorkflowStatusCategoryName();
			
			if(activityDTO.getWorkflowStatus() != null){
				this.workflowStatusType = activityDTO.getWorkflowStatus().getWorkflowStatusType();
			}	
			
			if(activityDTO.getWorkflowStatus() != null && activityDTO.getWorkflowStatus().getWorkflow() != null) {
				this.workflowId = activityDTO.getWorkflowStatus().getWorkflow().getWorkflowId();
			} else {
				this.workflowId =0;
			}
			
			this.lifeCycleStageId = activityDTO.getLifeCycleStageId(); 
			this.lifeCycleStageName = activityDTO.getLifeCycleStageName();
			this.userTagActivity = userTagActivity;
			
			if (activityDTO.getActivityWorkPackage().getProductBuild() != null)
			{ 
				this.productBuildId = activityDTO.getActivityWorkPackage().getProductBuild().getProductBuildId();
				this.productBuildName = activityDTO.getActivityWorkPackage().getProductBuild().getBuildname();
				
				if(activityDTO.getActivityWorkPackage().getProductBuild().getProductVersion() != null){
					this.productVersionName = activityDTO.getActivityWorkPackage().getProductBuild().getProductVersion().getProductVersionName();			
				}
			}
			
			this.customFields = customFields;
			
			this.activityPredecessors = activityDTO.getActivityPredecessors();
		}
	
	
	public String getClarificationNumber() {
		return clarificationNumber;
	}

	public void setClarificationNumber(String clarificationNumber) {
		this.clarificationNumber = clarificationNumber;
	}

	public Integer getActivityId() {
		return activityId;
	}

	public void setActivityId(Integer activityId) {
		this.activityId = activityId;
	}

	public Integer getActivityWorkPackageId() {
		return activityWorkPackageId;
	}

	public void setActivityWorkPackageId(Integer activityWorkPackageId) {
		this.activityWorkPackageId = activityWorkPackageId;
	}

	public String getActivityWorkPackageName() {
		return activityWorkPackageName;
	}

	public void setActivityWorkPackageName(String activityWorkPackageName) {
		this.activityWorkPackageName = activityWorkPackageName;
	}

	
	public String getActivityTrackerNumber() {
		return activityTrackerNumber;
	}

	public void setActivityTrackerNumber(String activityTrackerNumber) {
		this.activityTrackerNumber = activityTrackerNumber;
	}

	public Integer getDrReferenceNumber() {
		return drReferenceNumber;
	}

	public void setDrReferenceNumber(Integer drReferenceNumber) {
		this.drReferenceNumber = drReferenceNumber;
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

	public Integer getAutoAllocateReferenceId() {
		return autoAllocateReferenceId;
	}

	public void setAutoAllocateReferenceId(Integer autoAllocateReferenceId) {
		this.autoAllocateReferenceId = autoAllocateReferenceId;
	}
	public Integer getIsImmidiateAutoAllocation() {
		return isImmidiateAutoAllocation;
	}

	public void setIsImmidiateAutoAllocation(Integer isImmidiateAutoAllocation) {
		this.isImmidiateAutoAllocation = isImmidiateAutoAllocation;
	}

	public Integer getPriorityId() {
		return priorityId;
	}

	public void setPriorityId(Integer priorityId) {
		this.priorityId = priorityId;
	}

	public String getPriorityName() {
		return priorityName;
	}

	public void setPriorityName(String priorityName) {
		this.priorityName = priorityName;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getBaselineStartDate() {
		return baselineStartDate;
	}

	public void setBaselineStartDate(String baselineStartDate) {
		this.baselineStartDate = baselineStartDate;
	}

	public String getBaselineEndDate() {
		return baselineEndDate;
	}

	public void setBaselineEndDate(String baselineEndDate) {
		this.baselineEndDate = baselineEndDate;
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

	public Integer getCreatedById() {
		return createdById;
	}

	public void setCreatedById(Integer createdById) {
		this.createdById = createdById;
	}

	public String getCreatedByName() {
		return createdByName;
	}

	public void setCreatedByName(String createdByName) {
		this.createdByName = createdByName;
	}

	public Integer getModifiedById() {
		return modifiedById;
	}

	public void setModifiedById(Integer modifiedById) {
		this.modifiedById = modifiedById;
	}

	public String getModifiedByName() {
		return modifiedByName;
	}

	public void setModifiedByName(String modifiedByName) {
		this.modifiedByName = modifiedByName;
	}

	public Integer getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(Integer categoryId) {
		this.categoryId = categoryId;
	}

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	public Integer getProductFeatureId() {
		return productFeatureId;
	}

	public void setProductFeatureId(Integer productFeatureId) {
		this.productFeatureId = productFeatureId;
	}

	public String getProductFeatureName() {
		return productFeatureName;
	}

	public void setProductFeatureName(String productFeatureName) {
		this.productFeatureName = productFeatureName;
	}

	public Integer getPartitionId() {
		return partitionId;
	}

	public void setPartitionId(Integer partitionId) {
		this.partitionId = partitionId;
	}

	public String getPartitionName() {
		return partitionName;
	}

	public void setPartitionName(String partitionName) {
		this.partitionName = partitionName;
	}

	public Integer getActivityMasterId() {
		return activityMasterId;
	}

	public void setActivityMasterId(Integer activityMasterId) {
		this.activityMasterId = activityMasterId;
	}

	public String getActivityMasterName() {
		return activityMasterName;
	}

	public void setActivityMasterName(String activityMasterName) {
		this.activityMasterName = activityMasterName;
	}
	public Integer getStatusCategoryId() {
		return statusCategoryId;
	}

	public void setStatusCategoryId(Integer statusCategoryId) {
		this.statusCategoryId = statusCategoryId;
	}

	public String getStatusCategoryName() {
		return statusCategoryName;
	}

	public void setStatusCategoryName(String statusCategoryName) {
		this.statusCategoryName = statusCategoryName;
	}
	
	public Integer getClarificationTrackerId() {
		return clarificationTrackerId;
	}

	public void setClarificationTrackerId(Integer clarificationTrackerId) {
		this.clarificationTrackerId = clarificationTrackerId;
	}

	public String getActivityName() {
		return activityName;
	}

	public void setActivityName(String activityName) {
		this.activityName = activityName;
	}

	public Integer getIsActive() {
		return isActive;
	}

	public void setIsActive(Integer isActive) {
		this.isActive = isActive;
	}
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Integer getPlannedActivitySize() {
		return plannedActivitySize;
	}

	public void setPlannedActivitySize(Integer plannedActivitySize) {
		this.plannedActivitySize = plannedActivitySize;
	}

	public Integer getActualActivitySize() {
		return actualActivitySize;
	}

	public void setActualActivitySize(Integer actualActivitySize) {
		this.actualActivitySize = actualActivitySize;
	}
	public Integer getTaskOpenStatusValue() {
		return taskOpenStatusValue;
	}

	public void setTaskOpenStatusValue(Integer taskOpenStatusValue) {
		this.taskOpenStatusValue = taskOpenStatusValue;
	}

	public Integer getTaskOnHoldStatusRecords() {
		return taskOnHoldStatusRecords;
	}

	public void setTaskOnHoldStatusRecords(Integer taskOnHoldStatusRecords) {
		this.taskOnHoldStatusRecords = taskOnHoldStatusRecords;
	}

	public Integer getTaskCompletedStatusRecords() {
		return taskCompletedStatusRecords;
	}

	public void setTaskCompletedStatusRecords(Integer taskCompletedStatusRecords) {
		this.taskCompletedStatusRecords = taskCompletedStatusRecords;
	}

	public Integer getTotalTaskCount() {
		return totalTaskCount;
	}

	public void setTotalTaskCount(Integer totalTaskCount) {
		this.totalTaskCount = totalTaskCount;
	}

	public Integer getLoggedInUserTaskCount() {
		return loggedInUserTaskCount;
	}

	public void setLoggedInUserTaskCount(Integer loggedInUserTaskCount) {
		this.loggedInUserTaskCount = loggedInUserTaskCount;
	}

	public Integer getTaskInProgressStatusRecords() {
		return taskInProgressStatusRecords;
	}

	public void setTaskInProgressStatusRecords(Integer taskInProgressStatusRecords) {
		this.taskInProgressStatusRecords = taskInProgressStatusRecords;
	}

	public String getEnviCombination() {
		return enviCombination;
	}

	public void setEnviCombination(String enviCombination) {
		this.enviCombination = enviCombination;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public Integer getStatusId() {
		return statusId;
	}

	public void setStatusId(Integer statusId) {
		this.statusId = statusId;
	}

	public String getStatusName() {
		return statusName;
	}

	public void setStatusName(String statusName) {
		this.statusName = statusName;
	}

	public String getStatusDisplayName() {
		return statusDisplayName;
	}

	public void setStatusDisplayName(String statusDisplayName) {
		this.statusDisplayName = statusDisplayName;
	}

	public String getWorkflowStatusCategoryName() {
		return workflowStatusCategoryName;
	}

	public void setWorkflowStatusCategoryName(String workflowStatusCategoryName) {
		this.workflowStatusCategoryName = workflowStatusCategoryName;
	}

	public Integer getTotalEffort() {
		return totalEffort;
	}

	public void setTotalEffort(Integer totalEffort) {
		this.totalEffort = totalEffort;
	}

	public Integer getWorkflowId() {
		return workflowId;
	}

	public void setWorkflowId(Integer workflowId) {
		this.workflowId = workflowId;
	}

	public String getActors() {
		return actors;
	}

	public void setActors(String actors) {
		this.actors = actors;
	}

	public String getCompletedBy() {
		return completedBy;
	}

	public void setCompletedBy(String completedBy) {
		this.completedBy = completedBy;
	}

	public Integer getRemainingHours() {
		return remainingHours;
	}

	public void setRemainingHours(Integer remainingHours) {
		this.remainingHours = remainingHours;
	}

	public String getWorkflowIndicator() {
		return workflowIndicator;
	}

	public void setWorkflowIndicator(String workflowIndicator) {
		this.workflowIndicator = workflowIndicator;
	}

	public String getWorkflowRAG() {
		return workflowRAG;
	}

	public void setWorkflowRAG(String workflowRAG) {
		this.workflowRAG = workflowRAG;
	}
	
	public Integer getAttachmentCount() {
		return attachmentCount;
	}

	public void setAttachmentCount(Integer attachmentCount) {
		this.attachmentCount = attachmentCount;
	}

	public String getRemainingHrsMins() {
		return remainingHrsMins;
	}

	public void setRemainingHrsMins(String remainingHrsMins) {
		this.remainingHrsMins = remainingHrsMins;
	}

	public Integer getLifeCycleStageId() {
		return lifeCycleStageId;
	}

	public void setLifeCycleStageId(Integer lifeCycleStageId) {
		this.lifeCycleStageId = lifeCycleStageId;
	}

	public String getLifeCycleStageName() {
		return lifeCycleStageName;
	}

	public void setLifeCycleStageName(String lifeCycleStageName) {
		this.lifeCycleStageName = lifeCycleStageName;
	}

	public String getModifiedField() {
		return modifiedField;
	}

	public void setModifiedField(String modifiedField) {
		this.modifiedField = modifiedField;
	}

	public String getModifiedFieldTitle() {
		return modifiedFieldTitle;
	}

	public void setModifiedFieldTitle(String modifiedFieldTitle) {
		this.modifiedFieldTitle = modifiedFieldTitle;
	}

	public String getOldFieldID() {
		return oldFieldID;
	}

	public void setOldFieldID(String oldFieldID) {
		this.oldFieldID = oldFieldID;
	}

	public String getOldFieldValue() {
		return oldFieldValue;
	}

	public void setOldFieldValue(String oldFieldValue) {
		this.oldFieldValue = oldFieldValue;
	}

	public String getModifiedFieldID() {
		return modifiedFieldID;
	}

	public void setModifiedFieldID(String modifiedFieldID) {
		this.modifiedFieldID = modifiedFieldID;
	}

	public String getModifiedFieldValue() {
		return modifiedFieldValue;
	}

	public void setModifiedFieldValue(String modifiedFieldValue) {
		this.modifiedFieldValue = modifiedFieldValue;
	}

	public Float getPercentageCompletion() {
		return percentageCompletion;
	}

	public void setPercentageCompletion(Float percentageCompletion) {
		this.percentageCompletion = percentageCompletion;
	}

	public Integer getProductId() {
		return productId;
	}

	public void setProductId(Integer productId) {
		this.productId = productId;
	}

	public Integer getPlannedEffort() {
		return plannedEffort;
	}

	public void setPlannedEffort(Integer plannedEffort) {
		this.plannedEffort = plannedEffort;
	}

	public Integer getBaselineActivitySize() {
		return baselineActivitySize;
	}

	public void setBaselineActivitySize(Integer baselineActivitySize) {
		this.baselineActivitySize = baselineActivitySize;
	}

	public Integer getBaselineEffort() {
		return baselineEffort;
	}

	public void setBaselineEffort(Integer baselineEffort) {
		this.baselineEffort = baselineEffort;
	}

	public String getComplexity() {
		return complexity;
	}

	public void setComplexity(String complexity) {
		this.complexity = complexity;
	}

	public Integer getChangeRequestCount() {
		return changeRequestCount;
	}

	public void setChangeRequestCount(Integer changeRequestCount) {
		this.changeRequestCount = changeRequestCount;
	}

	public Integer getClarificationCount() {
		return clarificationCount;
	}

	public void setClarificationCount(Integer clarificationCount) {
		this.clarificationCount = clarificationCount;
	}

	public Float getWeightage() {
		return weightage;
	}

	public void setWeightage(Float weightage) {
		this.weightage = weightage;
	}

	public Float getBaselineUnit() {
		return baselineUnit;
	}

	public void setBaselineUnit(Float baselineUnit) {
		this.baselineUnit = baselineUnit;
	}

	public Float getPlannedUnit() {
		return plannedUnit;
	}

	public void setPlannedUnit(Float plannedUnit) {
		this.plannedUnit = plannedUnit;
	}

	public Float getActualUnit() {
		return actualUnit;
	}

	public void setActualUnit(Float actualUnit) {
		this.actualUnit = actualUnit;
	}

	public boolean isVisibleEventComment() {
		return visibleEventComment;
	}

	public void setVisibleEventComment(boolean visibleEventComment) {
		this.visibleEventComment = visibleEventComment;
	}

	public Integer getUserTagActivity() {
		return userTagActivity;
	}

	public void setUserTagActivity(Integer userTagActivity) {
		this.userTagActivity = userTagActivity;
	}

	public String getEngagementName() {
		return engagementName;
	}

	public Integer getEngagementId() {
		return engagementId;
	}

	public Integer getProductBuildId() {
		return productBuildId;
	}

	public String getProductBuildName() {
		return productBuildName;
	}

	public String getProductVersionName() {
		return productVersionName;
	}

	
	public void setEngagementName(String engagementName) {
		this.engagementName = engagementName;
	}

	public void setEngagementId(Integer engagementId) {
		this.engagementId = engagementId;
	}

	public void setProductBuildId(Integer productBuildId) {
		this.productBuildId = productBuildId;
	}

	public void setProductBuildName(String productBuildName) {
		this.productBuildName = productBuildName;
	}

	public void setProductVersionName(String productVersionName) {
		this.productVersionName = productVersionName;
	}

	public String getIsModified() {
		return isModified;
	}

	public void setIsModified(String isModified) {
		this.isModified = isModified;
	}

	public String getWorkflowStatusType() {
		return workflowStatusType;
	}

	public void setWorkflowStatusType(String workflowStatusType) {
		this.workflowStatusType = workflowStatusType;
	}

	public String getLatestComment() {
		return latestComment;
	}

	public void setLatestComment(String latestComment) {
		this.latestComment = latestComment;
	}

	public Date getTempModifiedDate() {
		return tempModifiedDate;
	}

	public void setTempModifiedDate(Date tempModifiedDate) {
		this.tempModifiedDate = tempModifiedDate;
	}

	public String getCustomFields() {
		return customFields;
	}

	public void setCustomFields(String customFields) {
		this.customFields = customFields;
	}

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public String getTo() {
		return to;
	}

	public void setTo(String to) {
		this.to = to;
	}

	public String getProgressIndicator() {
		return progressIndicator;
	}

	public void setProgressIndicator(String progressIndicator) {
		this.progressIndicator = progressIndicator;
	}

	public String getActivityPredecessors() {
		return activityPredecessors;
	}

	public void setActivityPredecessors(String activityPredecessors) {
		this.activityPredecessors = activityPredecessors;
	}

	public String getActivityWorkPackageOwner() {
		return activityWorkPackageOwner;
	}

	public void setActivityWorkPackageOwner(String activityWorkPackageOwner) {
		this.activityWorkPackageOwner = activityWorkPackageOwner;
	}
	
	@JsonIgnore
	public JSONObject getCleanJson() {
		
		JSONObject responseJson = new JSONObject();
		try {
			responseJson.put("activityId", activityId);
			responseJson.put("activityName", activityName);
			responseJson.put("description", description);
			responseJson.put("activityTypeId", activityMasterId);
			responseJson.put("activityTypeName", activityMasterName);
			responseJson.put("activityWorkPackageId", activityWorkPackageId);
			responseJson.put("activityWorkPackageName", activityWorkPackageName);
			responseJson.put("assigneeId", assigneeId);
			responseJson.put("assigneeName", assigneeName);
			responseJson.put("reviewerId", reviewerId);
			responseJson.put("reviewerName", reviewerName);
			responseJson.put("engagementId", engagementId);
			responseJson.put("engagementName", engagementName);
			responseJson.put("productId", productId);
			responseJson.put("productName", productName);
			responseJson.put("productBuildId", productBuildId);
			responseJson.put("productBuildName", productBuildName);
			responseJson.put("productVersionName",productVersionName);
			responseJson.put("plannedStartDate", plannedStartDate);
			responseJson.put("plannedEndDate", plannedEndDate);
			responseJson.put("actualStartDate", actualStartDate);
			responseJson.put("actualEndDate", actualEndDate);
			responseJson.put("createdDate", createdDate);
			responseJson.put("modifiedDate", modifiedDate);
			responseJson.put("createdByName", createdByName);
			responseJson.put("modifiedByName", modifiedByName);
			responseJson.put("statusId", statusId);
			responseJson.put("statusName", statusName);
			responseJson.put("workflowId", workflowId);
			responseJson.put("workflowStatusType", workflowStatusType);
			responseJson.put("PendingWith",actors);
			responseJson.put("plannedEffort",plannedEffort);
			responseJson.put("totalEffort",totalEffort);
			responseJson.put("priorityName",priorityName);
			
		} catch (Exception e){

		}
		return responseJson;
	}
}