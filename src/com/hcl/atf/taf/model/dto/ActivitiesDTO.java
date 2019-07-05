package com.hcl.atf.taf.model.dto;
import java.util.Date;

import com.hcl.atf.taf.model.Activity;
import com.hcl.atf.taf.model.ActivityTask;
import com.hcl.atf.taf.model.ActivityWorkPackage;
import com.hcl.atf.taf.model.ProductMaster;
import com.hcl.ilcm.workflow.model.WorkflowStatus;


public class ActivitiesDTO {
	private Activity activity;
	private ActivityWorkPackage activityWorkPackage;
	private ActivityTask activityTask;
	private ProductMaster productMaster;
	private WorkflowStatus workflowStatus;	
	private Integer taskOpenStatusValue = 0;
	private Integer taskOnHoldStatusRecords = 0;
	private Integer taskCompletedStatusRecords = 0;
	private Integer totalTaskCount = 0;
	private Integer loggedInUserTaskCount = 0;
	private Integer taskInProgressStatusRecords = 0;
	private String activityName;
	private String description;
	private Integer productFeatureId;	
	private String productFeatureName;
	private String activityTrackerNumber;
	private Integer activityMasterId;
	private String activityMasterName;
	private Float weightage;
	private String clarificationNumber;
	private String environmentCombinations;
	private String changeRequest;
    private Integer categoryId;
	private String categoryName;
	private Integer assigneeId;
	private String assigneeName;
	private Integer reviewerId;
	private String reviewerName;
	private Integer statusId;	
	private String statusName;
	private String statusDisplayName;
	
	private Integer statusCategoryId;	
	private String statusCategoryName;
	private Integer priorityId;
	private String priorityName;
	private String remark;
	private String baselineStartDate;	
	private String baselineEndDate;
	private String plannedStartDate;	
	private String plannedEndDate;
	private String actualStartDate;
	private String actualEndDate;
	private String createdDate;	
	private String modifiedDate;
	private Integer createdById;	
	private String createdByName;	
	private Integer modifiedById;	
	private String modifiedByName;	
	private Integer isActive;
	private Integer baselineActivitySize;
	private Integer plannedActivitySize;
	private Integer actualActivitySize;
	private String productName;
	private Integer engagementId;
	private String engagementName;
	private String complexity;
	private Float percentageCompletion;

	private Integer changeRequestCount = 0;	
	private Integer clarificationCount = 0;	
	private Integer totalEffort;
	private Integer plannedEffort;
	private Integer baselineEffort;
	private Float baselineUnit;
	private Float plannedUnit;
	private Float actualUnit;
	
	private Integer workflowId;		
	private Integer lifeCycleStageId;
	private String lifeCycleStageName;	
	private String workflowStatusCategoryName;
	private Integer userTagActivity;	
	private String workflowStatusType;
	private Integer productBuildId;
	
	private Date tempModifiedDate;
	
	private String activityPredecessors;
	
	public Integer getProductBuildId() {
		return productBuildId;
	}

	public void setProductBuildId(Integer productBuildId) {
		this.productBuildId = productBuildId;
	}

	public ActivityTask getActivityTask() {
		return activityTask;
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

	public void setActivityTask(ActivityTask activityTask) {
		this.activityTask = activityTask;
	}

	public Activity getActivity() {
		return activity;
	}

	public void setActivity(Activity activity) {
		this.activity = activity;
	}

	public ActivityWorkPackage getActivityWorkPackage() {
		return activityWorkPackage;
	}

	public void setActivityWorkPackage(ActivityWorkPackage activityWorkPackage) {
		this.activityWorkPackage = activityWorkPackage;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}



	public String getClarificationNumber() {
		return clarificationNumber;
	}

	public void setClarificationNumber(String clarificationNumber) {
		this.clarificationNumber = clarificationNumber;
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

	public Integer getIsActive() {
		return isActive;
	}

	public void setIsActive(Integer isActive) {
		this.isActive = isActive;
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
	
	public Float getWeightage() {
		return weightage;
	}

	public void setWeightage(Float weightage) {
		this.weightage = weightage;
	}

	public String getActivityTrackerNumber() {
		return activityTrackerNumber;
	}

	public void setActivityTrackerNumber(String activityTrackerNumber) {
		this.activityTrackerNumber = activityTrackerNumber;
	}

	public String getActivityName() {
		return activityName;
	}

	public void setActivityName(String activityName) {
		this.activityName = activityName;
	}

	public Integer getBaselineActivitySize() {
		return baselineActivitySize;
	}

	public void setBaselineActivitySize(Integer baselineActivitySize) {
		this.baselineActivitySize = baselineActivitySize;
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

	public String getEnvironmentCombinations() {
		return environmentCombinations;
	}

	public void setEnvironmentCombinations(String environmentCombinations) {
		this.environmentCombinations = environmentCombinations;
	}

	public String getChangeRequest() {
		return changeRequest;
	}

	public void setChangeRequest(String changeRequest) {
		this.changeRequest = changeRequest;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}
	
	public String getEngagementName() {
		return engagementName;
	}

	public void setEngagementName(String engagementName) {
		this.engagementName = engagementName;
	}

	public ProductMaster getProductMaster() {
		return productMaster;
	}

	public void setProductMaster(ProductMaster productMaster) {
		this.productMaster = productMaster;
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
	public String getComplexity() {
		return complexity;
	}

	public void setComplexity(String complexity) {
		this.complexity = complexity;
	}

	public Float getPercentageCompletion() {
		return percentageCompletion;
	}

	public void setPercentageCompletion(Float percentageCompletion) {
		this.percentageCompletion = percentageCompletion;
	}

	public Integer getTotalEffort() {
		return totalEffort;
	}

	public void setTotalEffort(Integer totalEffort) {
		this.totalEffort = totalEffort;
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

	public String getLifeCycleStageName() {
		return lifeCycleStageName;
	}

	public void setLifeCycleStageName(String lifeCycleStageName) {
		this.lifeCycleStageName = lifeCycleStageName;
	}
	
	public Integer getPlannedEffort() {
		return plannedEffort;
	}

	public void setPlannedEffort(Integer plannedEffort) {
		this.plannedEffort = plannedEffort;
	}

	public Integer getBaselineEffort() {
		return baselineEffort;
	}

	public void setBaselineEffort(Integer baselineEffort) {
		this.baselineEffort = baselineEffort;
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

	public Integer getEngagementId() {
		return engagementId;
	}

	public void setEngagementId(Integer engagementId) {
		this.engagementId = engagementId;
	}

	public Integer getWorkflowId() {
		return workflowId;
	}

	public void setWorkflowId(Integer workflowId) {
		this.workflowId = workflowId;
	}

	public Integer getLifeCycleStageId() {
		return lifeCycleStageId;
	}

	public void setLifeCycleStageId(Integer lifeCycleStageId) {
		this.lifeCycleStageId = lifeCycleStageId;
	}

	public String getWorkflowStatusCategoryName() {
		return workflowStatusCategoryName;
	}

	public void setWorkflowStatusCategoryName(String workflowStatusCategoryName) {
		this.workflowStatusCategoryName = workflowStatusCategoryName;
	}
	
	public Integer getUserTagActivity() {
		return userTagActivity;
	}

	public void setUserTagActivity(Integer userTagActivity) {
		this.userTagActivity = userTagActivity;
	}
	
	public String getWorkflowStatusType() {
		return workflowStatusType;
	}

	public void setWorkflowStatusType(String workflowStatusType) {
		this.workflowStatusType = workflowStatusType;
	}
	
	public WorkflowStatus getWorkflowStatus() {
		return workflowStatus;
	}

	public void setWorkflowStatus(WorkflowStatus workflowStatus) {
		this.workflowStatus = workflowStatus;
	}

	public Date getTempModifiedDate() {
		return tempModifiedDate;
	}

	public void setTempModifiedDate(Date tempModifiedDate) {
		this.tempModifiedDate = tempModifiedDate;
	}

	public String getActivityPredecessors() {
		return activityPredecessors;
	}

	public void setActivityPredecessors(String activityPredecessors) {
		this.activityPredecessors = activityPredecessors;
	}
	
}
