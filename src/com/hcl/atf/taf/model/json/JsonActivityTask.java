package com.hcl.atf.taf.model.json;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.hcl.atf.taf.controller.utilities.DateUtility;
import com.hcl.atf.taf.model.Activity;
import com.hcl.atf.taf.model.ActivityResult;
import com.hcl.atf.taf.model.ActivitySecondaryStatusMaster;
import com.hcl.atf.taf.model.ActivityTask;
import com.hcl.atf.taf.model.ActivityTaskType;
import com.hcl.atf.taf.model.EnvironmentCombination;
import com.hcl.atf.taf.model.ExecutionPriority;
import com.hcl.atf.taf.model.ExecutionTypeMaster;
import com.hcl.atf.taf.model.ProductMaster;
import com.hcl.atf.taf.model.UserList;
import com.hcl.ilcm.workflow.model.WorkflowStatus;

public class JsonActivityTask {
	private static final Log log = LogFactory.getLog(JsonActivityTask.class);
	
	
	@JsonProperty
	private Integer activityTaskId;
	@JsonProperty
	private String activityTaskName;	
	@JsonProperty
	private Integer activityMasterId;
	@JsonProperty
	private String activityMasterName;
	@JsonProperty
	private Integer activityId;
	@JsonProperty
	private String activityName;
	@JsonProperty
	private Integer activityWorkpackageId;
	@JsonProperty
	private String activityWorkpackageName;
	@JsonProperty
	private Integer activityTypeId;
	@JsonProperty
	private String activityTypeName;
	@JsonProperty
	private Integer changeRequestId;
	@JsonProperty
	private Integer categoryId;
	@JsonProperty
	private String categoryName;
	@JsonProperty
	private Integer assigneeId;
	@JsonProperty
	private String assigneeName;
	@JsonProperty
	private Integer reviewerId;
	@JsonProperty
	private String reviewerName;	
    @JsonProperty
	private Integer statusId;
	@JsonProperty
	private String statusName;
	@JsonProperty
	private String statusDisplayName;
	@JsonProperty
	private String workflowStatusCategoryName;
	@JsonProperty
	private Integer activityTaskTypeId;
	@JsonProperty
	private String activityTaskTypeName;
	@JsonProperty
	private Integer priorityId;
	@JsonProperty
	private String priorityName;	
	@JsonProperty
	private Integer enviromentCombinationId;
	@JsonProperty
	private String enviromentCombinationName;
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
	private Integer resultId;
	@JsonProperty
	private String resultName;
	@JsonProperty
	private String remark;
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
	private Integer isSelfReviewed;
	@JsonProperty
	private Integer isActive;
	@JsonProperty
	private Integer isPeerReviewed;
	@JsonProperty
	private Integer isPqaReviewed;
	@JsonProperty
	private Integer baselineEffort;
	@JsonProperty
	private Integer plannedEffort;
	@JsonProperty
	private Integer actualEffort;
	@JsonProperty
	private Integer secondaryStatusId;
	@JsonProperty
	private String secondaryStatusName;
	@JsonProperty
	private Integer activityEffortTrackerId;
	@JsonProperty
	private String activityEffortTrackerName;
	@JsonProperty
	private Integer totalEffort;

	@JsonProperty
	private Integer baselineTaskSize;
	@JsonProperty
	private Integer plannedTaskSize;
	@JsonProperty
	private Integer actualTaskSize;
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
	private String productName;
	@JsonProperty
	private String complexity;
	
	@JsonProperty
	private boolean visibleEventComment;
	
	@JsonProperty
	private Integer userTagActivity;
	
	@JsonProperty
	private String isModified;
	
	
	public JsonActivityTask() {
	}

	public JsonActivityTask(ActivityTask activityTask) {
		this.activityTaskId = activityTask.getActivityTaskId();		
		this.activityTaskName = activityTask.getActivityTaskName();		
		this.remark = activityTask.getRemark();	
		
		this.plannedTaskSize = activityTask.getPlannedTaskSize();
		this.actualTaskSize = activityTask.getActualTaskSize();

		if(activityTask.getBaselineTaskSize() != null){
			this.baselineTaskSize = activityTask.getBaselineTaskSize();
		}else{
			this.baselineTaskSize = this.plannedTaskSize;
		}
		
		this.complexity = activityTask.getComplexity();
		if (activityTask.getIsActive() != null) {
			this.isActive = activityTask.getIsActive();
			
		}
		if (activityTask.getActivity().getActivityMaster() != null) {
			this.activityMasterId =activityTask.getActivity().getActivityMaster().getActivityMasterId();
			
			this.activityMasterName =activityTask.getActivity().getActivityMaster().getActivityMasterName();
			
		}
		if (activityTask.getActivity() != null) {
			this.activityId = activityTask.getActivity().getActivityId();
			this.activityName = activityTask.getActivity().getActivityName();
			this.activityWorkpackageId = activityTask.getActivity().getActivityWorkPackage().getActivityWorkPackageId();
			this.activityWorkpackageName = activityTask.getActivity().getActivityWorkPackage().getActivityWorkPackageName();
		}
		
		if(activityTask.getProductMaster()!= null){
			
			this.productId = activityTask.getProductMaster().getProductId();
			this.productName = activityTask.getProductMaster().getProductName();
			
			
		}
		if (activityTask.getChangeRequest() != null) {
			this.changeRequestId = activityTask.getChangeRequest().getChangeRequestId();
			
		}
		
		if (activityTask.getPriority() != null) {
			this.priorityId = activityTask.getPriority().getExecutionPriorityId();
			this.priorityName = activityTask.getPriority()
					.getExecutionPriorityName();
		}
        if (activityTask.getStatus() != null) {
			this.statusId = activityTask.getStatus().getWorkflowStatusId();
			this.statusName = activityTask.getStatus().getWorkflowStatusName();
			this.statusDisplayName = activityTask.getStatus().getWorkflowStatusDisplayName();
			if(activityTask.getStatus().getWorkflowStatusCategory() != null){
	    		   this.workflowStatusCategoryName = "["+activityTask.getStatus().getWorkflowStatusCategory().getWorkflowStatusCategoryName()+"]";
			}else{
				this.workflowStatusCategoryName = "";
			}
		}else{
			this.statusName = "--";
			this.statusDisplayName = "--";
			this.workflowStatusCategoryName = "";
		}
		if (activityTask.getActivityTaskType() != null) {
			this.activityTaskTypeId = activityTask.getActivityTaskType().getActivityTaskTypeId();
			this.activityTaskTypeName = activityTask.getActivityTaskType().getActivityTaskTypeName();
		}
		if (activityTask.getResult() != null) {
			this.resultId = activityTask.getResult().getActivityResultId();
			this.resultName = activityTask.getResult().getActivityResultName();
		}
		if (activityTask.getCategory() != null) {
			this.categoryId = activityTask.getCategory().getExecutionTypeId();
			this.categoryName = activityTask.getCategory().getName();
		}
		if (activityTask.getEnvironmentCombination() != null) {
			this.enviromentCombinationId = activityTask.getEnvironmentCombination().getEnvironment_combination_id();
			this.enviromentCombinationName = activityTask.getEnvironmentCombination().getEnvironmentCombinationName();
		}else{
			this.enviromentCombinationId = 0;
		}
        if (activityTask.getAssignee() != null) {
			this.assigneeId = activityTask.getAssignee().getUserId();	
			this.assigneeName = activityTask.getAssignee().getLoginId();			
		}
		if (activityTask.getReviewer() != null) {
			this.reviewerId = activityTask.getReviewer().getUserId();
			this.reviewerName = activityTask.getReviewer().getLoginId();
		}
	
		if (activityTask.getActivity().getActivityMaster() != null) {
			this.activityTypeId = activityTask.getActivity().getActivityMaster().getActivityMasterId();
			this.activityTypeName = activityTask.getActivity().getActivityMaster().getActivityMasterName();
		}

		if(activityTask.getCreatedDate()!=null){
			this.createdDate = DateUtility.dateToStringWithSeconds1(activityTask.getCreatedDate());
		}
		if(activityTask.getModifiedDate()!=null){
			this.modifiedDate = DateUtility.dateformatWithOutTime(activityTask.getModifiedDate());
		}
       if (activityTask.getCreatedBy() != null) {
         	this.createdById = activityTask.getCreatedBy().getUserId();
			this.createdByName = activityTask.getCreatedBy().getLoginId();
		}
         if (activityTask.getModifiedBy() != null) {

			this.modifiedById = activityTask.getModifiedBy().getUserId();
			this.modifiedByName = activityTask.getModifiedBy().getLoginId();

		}
         if (activityTask.getPlannedStartDate() != null) {
			this.plannedStartDate = DateUtility.dateformatWithOutTime(activityTask
					.getPlannedStartDate());
		}
         	if (activityTask.getPlannedEndDate() != null) {
			this.plannedEndDate = DateUtility.dateformatWithOutTime(activityTask.getPlannedEndDate());
		}
      
     	if (activityTask.getBaselineStartDate() != null) {
			this.baselineStartDate = DateUtility.dateformatWithOutTime(activityTask.getBaselineStartDate());
		}else{
			this.baselineStartDate = this.plannedStartDate;
		}
        if (activityTask.getBaselineEndDate() != null) {
			this.baselineEndDate = DateUtility.dateformatWithOutTime(activityTask.getBaselineEndDate());
		}else{
			this.baselineEndDate = this.plannedEndDate;
		}
             	
		if (activityTask.getActualStartDate()!= null)	{
		    this.actualStartDate = DateUtility.dateformatWithOutTime(activityTask.getActualStartDate());}
		else {
			
			this.actualStartDate = "dd-mm-yy";
		}

		if (activityTask.getActualEndDate() != null) {
			this.actualEndDate = DateUtility.dateformatWithOutTime(activityTask.getActualEndDate());
		}else {
			this.actualEndDate = "dd-mm-yy";
		}
		
		if(activityTask.getTotalEffort() != null){
			this.totalEffort = activityTask.getTotalEffort();
		}else{
			this.totalEffort = 0;
		}
		this.plannedEffort = activityTask.getPlannedEffort();
		this.actualEffort = activityTask.getActualEffort();
		if(activityTask.getBaselineEffort() != null){
			this.baselineEffort = activityTask.getBaselineEffort();
		}else{
			this.baselineEffort = this.plannedEffort;
		}
		if (activityTask.getSecondaryStatus()!= null) {
			this.secondaryStatusId = activityTask.getSecondaryStatus().getActivitySecondaryStatusId();
			this.secondaryStatusName = activityTask.getSecondaryStatus().getActivitySecondaryStatusName();
		}
		this.actors = actors;
		this.completedBy=completedBy;
		this.remainingHours=remainingHours;
		this.workflowIndicator = workflowIndicator;
		this.workflowRAG = workflowRAG;
		if(activityTask.getLifeCycleStage() != null){
			this.lifeCycleStageId = activityTask.getLifeCycleStage().getWorkflowStatusId();
			this.lifeCycleStageName = activityTask.getLifeCycleStage().getWorkflowStatusDisplayName();
		}
		if(activityTask.getPercentageCompletion() != null){
			this.percentageCompletion = activityTask.getPercentageCompletion();
		}else{
			this.percentageCompletion = 0.0F;
		}
		this.visibleEventComment=visibleEventComment;
}

	@JsonIgnore
	public ActivityTask getActivityTask() {
		ActivityTask activityTask= new ActivityTask();
		
		activityTask.setActivityTaskId(activityTaskId);
		activityTask.setIsActive(isActive);
		activityTask.setComplexity(complexity);
		
		if (this.activityTaskName!=null){
		activityTask.setActivityTaskName(activityTaskName);
	    }
		
		if (this.activityId != null) {
			Activity activity = new Activity();			
			activity.setActivityId(activityId);			
			activityTask.setActivity(activity);

		}
		if (this.statusId != null) {
			WorkflowStatus workflowStatus=new WorkflowStatus();
			workflowStatus.setWorkflowStatusId(statusId);
			workflowStatus.setWorkflowStatusName(statusName);
			activityTask.setStatus(workflowStatus);
			
		}
		if (this.activityTaskTypeId != null) {
			ActivityTaskType activityTaskType = new ActivityTaskType();
			activityTaskType.setActivityTaskTypeId(activityTaskTypeId);
			activityTaskType.setActivityTaskTypeName(activityTaskTypeName);
			activityTask.setActivityTaskType(activityTaskType);
		}

		if (this.resultId != null) {
			ActivityResult activityResult = new ActivityResult();
			activityResult.setActivityResultId(resultId);
			activityResult.setActivityResultName(resultName);
			activityTask.setResult(activityResult);
		}

		if (this.priorityId != null) {
			ExecutionPriority priority = new ExecutionPriority();
			priority.setExecutionPriorityId(priorityId);
			priority.setExecutionPriorityName(priorityName);
			activityTask.setPriority(priority);
		}
		if (this.categoryId != null) {
			ExecutionTypeMaster executionTypeMaster = new ExecutionTypeMaster();
			executionTypeMaster.setExecutionTypeId(categoryId);
			executionTypeMaster.setName(categoryName);
			activityTask.setCategory(executionTypeMaster);
		}

		if (this.assigneeId != null) {
			UserList userlist = new UserList();
			userlist.setUserId(assigneeId);
			userlist.setLoginId(assigneeName);
			activityTask.setAssignee(userlist);
		}
		if (this.reviewerId != null) {
			UserList userlist = new UserList();
			userlist.setUserId(reviewerId);
			userlist.setLoginId(reviewerName);
			activityTask.setReviewer(userlist);
		}
		if (this.enviromentCombinationId != null && this.enviromentCombinationId > 0) {
			log.info("SET to DB EnvCombi"+enviromentCombinationId);			
			EnvironmentCombination enviromentCombination = new EnvironmentCombination();
			enviromentCombination.setEnvironment_combination_id(enviromentCombinationId);
			enviromentCombination.setEnvironmentCombinationName(enviromentCombinationName);
			activityTask.setEnvironmentCombination(enviromentCombination);
		}
		if (this.plannedStartDate != null && !(this.plannedStartDate.trim().isEmpty())) {
			activityTask.setPlannedStartDate(DateUtility.dateformatWithOutTime(this.plannedStartDate));
		}

		if (this.plannedEndDate != null	&& !(this.plannedEndDate.trim().isEmpty())) {
			activityTask.setPlannedEndDate(DateUtility.dateformatWithOutTime(this.plannedEndDate));
		}
		
		if (this.baselineStartDate != null && !(this.baselineStartDate.trim().isEmpty())) {
			activityTask.setBaselineStartDate(DateUtility.dateformatWithOutTime(this.baselineStartDate));
		}

		if (this.baselineEndDate != null	&& !(this.baselineEndDate.trim().isEmpty())) {
			activityTask.setBaselineEndDate(DateUtility.dateformatWithOutTime(this.baselineEndDate));
		}
		
			if (this.actualStartDate != null) {				
				if(this.actualStartDate.equals("mm/dd/yy"))
					activityTask.setActualStartDate(null);				
			       else if(this.actualStartDate!="mm/dd/yy")
			    	   activityTask.setActualStartDate(DateUtility.dateformatWithOutTime(this.actualStartDate));
			 }
			

			if (this.actualEndDate != null) {
				if(this.actualEndDate.equals("mm/dd/yy"))
					activityTask.setActualEndDate(null);				
			       else if(this.actualEndDate!="mm/dd/yy")
			    	   activityTask.setActualEndDate(DateUtility.dateformatWithOutTime(this.actualEndDate));
			 }
		if(this.plannedEffort != null){
			activityTask.setPlannedEffort(this.plannedEffort);
		}else{
			activityTask.setPlannedEffort(0);
		}
		
		if(this.baselineEffort != null){
			activityTask.setBaselineEffort(this.baselineEffort);
		}else{
			activityTask.setBaselineEffort(activityTask.getPlannedEffort());
		}
		
		if(this.actualEffort != null){
			activityTask.setActualEffort(this.actualEffort);
		}else{
			activityTask.setActualEffort(0);
		}

		if(this.totalEffort != null){
			activityTask.setTotalEffort(totalEffort);
		}else{
			activityTask.setTotalEffort(0);
		}

		if(this.createdDate == null || this.createdDate.trim().isEmpty()) {
			activityTask.setCreatedDate(DateUtility.getCurrentTime());
		} else {		
			activityTask.setCreatedDate(DateUtility.toFormatDate(this.createdDate));
		}
		activityTask.setModifiedDate(DateUtility.getCurrentTime());
		
		if (this.secondaryStatusId != null) {
			ActivitySecondaryStatusMaster activitySecondaryStatusMaster = new ActivitySecondaryStatusMaster();
			activitySecondaryStatusMaster.setActivitySecondaryStatusId(secondaryStatusId);
			activitySecondaryStatusMaster.setActivitySecondaryStatusName(secondaryStatusName);
			activityTask.setSecondaryStatus(activitySecondaryStatusMaster);
		}

		if(this.createdById!=null){
			UserList user=new UserList();
			user.setUserId(createdById);
			user.setLoginId(createdByName);
			activityTask.setCreatedBy(user);
		}
		
		if(this.modifiedById!=null){
			UserList user=new UserList();
			user.setUserId(createdById);
			user.setLoginId(createdByName);
			activityTask.setModifiedBy(user);
		}
		
		if(this.lifeCycleStageId != null && this.lifeCycleStageId >0){
			WorkflowStatus workflowStatus = new WorkflowStatus();
			workflowStatus.setWorkflowStatusId(this.lifeCycleStageId);
			activityTask.setLifeCycleStage(workflowStatus);
		}
		if(this.percentageCompletion != null ){
			activityTask.setPercentageCompletion(this.percentageCompletion);
		}else{
			activityTask.setPercentageCompletion(0.0F);
		}
		
		if(this.productId != null){
			ProductMaster productMaster = new ProductMaster();
			productMaster.setProductId(this.productId);
			productMaster.setProductName(this.productName);
			activityTask.setProductMaster(productMaster);
			
			
		}
		
		this.userTagActivity=userTagActivity;
		this.isModified = isModified;
		
		return activityTask;
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

	public Integer getActivityTaskId() {
		return activityTaskId;
	}

	public void setActivityTaskId(Integer activityTaskId) {
		this.activityTaskId = activityTaskId;
	}

	public Integer getActivityId() {
		return activityId;
	}

	public void setActivityId(Integer activityId) {
		this.activityId = activityId;
	}

	

	public Integer getChangeRequestId() {
		return changeRequestId;
	}

	public void setChangeRequestId(Integer changeRequestId) {
		this.changeRequestId = changeRequestId;
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

	public Integer getResultId() {
		return resultId;
	}

	public void setResultId(Integer resultId) {
		this.resultId = resultId;
	}

	public String getResultName() {
		return resultName;
	}

	public void setResultName(String resultName) {
		this.resultName = resultName;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
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

	public Integer getActivityTypeId() {
		return activityTypeId;
	}

	public void setActivityTypeId(Integer activityTypeId) {
		this.activityTypeId = activityTypeId;
	}

	public String getActivityTypeName() {
		return activityTypeName;
	}

	public void setActivityTypeName(String activityTypeName) {
		this.activityTypeName = activityTypeName;
	}	
	public String getActivityTaskName() {
		return activityTaskName;
	}

	public void setActivityTaskName(String activityTaskName) {
		this.activityTaskName = activityTaskName;
	}
	public Integer getEnviromentCombinationId() {
		return enviromentCombinationId;
	}

	public void setEnviromentCombinationId(Integer enviromentCombinationId) {
		this.enviromentCombinationId = enviromentCombinationId;
	}

	public String getEnviromentCombinationName() {
		return enviromentCombinationName;
	}

	public void setEnviromentCombinationName(String enviromentCombinationName) {
		this.enviromentCombinationName = enviromentCombinationName;
	}

	public Integer getIsSelfReviewed() {
		return isSelfReviewed;
	}

	public void setIsSelfReviewed(Integer isSelfReviewed) {
		this.isSelfReviewed = isSelfReviewed;
	}
	public Integer getIsActive() {
		return isActive;
	}

	public void setIsActive(Integer isActive) {
		this.isActive = isActive;
	}

	public Integer getIsPeerReviewed() {
		return isPeerReviewed;
	}

	public void setIsPeerReviewed(Integer isPeerReviewed) {
		this.isPeerReviewed = isPeerReviewed;
	}

	public Integer getIsPqaReviewed() {
		return isPqaReviewed;
	}

	public void setIsPqaReviewed(Integer isPqaReviewed) {
		this.isPqaReviewed = isPqaReviewed;
	}

	public Integer getActivityTaskTypeId() {
		return activityTaskTypeId;
	}

	public void setActivityTaskTypeId(Integer activityTaskTypeId) {
		this.activityTaskTypeId = activityTaskTypeId;
	}

	public String getActivityTaskTypeName() {
		return activityTaskTypeName;
	}

	public void setActivityTaskTypeName(String activityTaskTypeName) {
		this.activityTaskTypeName = activityTaskTypeName;
	}

	public Integer getBaselineEffort() {
		return baselineEffort;
	}

	public void setBaselineEffort(Integer baselineEffort) {
		this.baselineEffort = baselineEffort;
	}

	public Integer getPlannedEffort() {
		return plannedEffort;
	}

	public void setPlannedEffort(Integer plannedEffort) {
		this.plannedEffort = plannedEffort;
	}

	public Integer getActualEffort() {
		return actualEffort;
	}

	public void setActualEffort(Integer actualEffort) {
		this.actualEffort = actualEffort;
	}
	
	public Integer getTotalEfforts() {
		return totalEffort;
	}

	public void setTotalEfforts(Integer totalEfforts) {
	}


	public Integer getSecondaryStatusId() {
		return secondaryStatusId;
	}

	public void setSecondaryStatusId(Integer secondaryStatusId) {
		this.secondaryStatusId = secondaryStatusId;
	}

	public String getSecondaryStatusName() {
		return secondaryStatusName;
	}

	public void setSecondaryStatusName(String secondaryStatusName) {
		this.secondaryStatusName = secondaryStatusName;
	}

	public Integer getActivityEffortTrackerId() {
		return activityEffortTrackerId;
	}

	public void setActivityEffortTrackerId(Integer activityEffortTrackerId) {
		this.activityEffortTrackerId = activityEffortTrackerId;
	}

	public String getActivityEffortTrackerName() {
		return activityEffortTrackerName;
	}

	public void setActivityEffortTrackerName(String activityEffortTrackerName) {
		this.activityEffortTrackerName = activityEffortTrackerName;
	}

	public Integer getBaselineTaskSize() {
		return baselineTaskSize;
	}

	public void setBaselineTaskSize(Integer baselineTaskSize) {
		this.baselineTaskSize = baselineTaskSize;
	}

	public Integer getPlannedTaskSize() {
		return plannedTaskSize;
	}

	public void setPlannedTaskSize(Integer plannedTaskSize) {
		this.plannedTaskSize = plannedTaskSize;
	}

	public Integer getActualTaskSize() {
		return actualTaskSize;
	}

	public void setActualTaskSize(Integer actualTaskSize) {
		this.actualTaskSize = actualTaskSize;
	}

	public String getActivityName() {
		return activityName;
	}

	public void setActivityName(String activityName) {
		this.activityName = activityName;
	}

	public Integer getActivityWorkpackageId() {
		return activityWorkpackageId;
	}

	public void setActivityWorkpackageId(Integer activityWorkpackageId) {
		this.activityWorkpackageId = activityWorkpackageId;
	}

	public String getActivityWorkpackageName() {
		return activityWorkpackageName;
	}

	public void setActivityWorkpackageName(String activityWorkpackageName) {
		this.activityWorkpackageName = activityWorkpackageName;
	}

	public Integer getTotalEffort() {
		return totalEffort;
	}

	public void setTotalEffort(Integer totalEffort) {
		this.totalEffort = totalEffort;
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

	public Integer getWorkflowId() {
		return workflowId;
	}

	public void setWorkflowId(Integer workflowId) {
		this.workflowId = workflowId;
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

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getComplexity() {
		return complexity;
	}

	public void setComplexity(String complexity) {
		this.complexity = complexity;
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

	public String getIsModified() {
		return isModified;
	}

	public void setIsModified(String isModified) {
		this.isModified = isModified;
	}
}