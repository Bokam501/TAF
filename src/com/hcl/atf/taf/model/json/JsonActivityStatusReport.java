package com.hcl.atf.taf.model.json;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.hcl.atf.taf.controller.utilities.DateUtility;
import com.hcl.atf.taf.model.ActivityTask;
@JsonIgnoreProperties
public class JsonActivityStatusReport {
	@JsonProperty
	private Integer productId;
	@JsonProperty
	private String productName;
	@JsonProperty
	private Integer productFeatureId;
	@JsonProperty
	private String productFeatureName;
	@JsonProperty
	private Integer productVersionId;
	@JsonProperty
	private String productVersionName;
	@JsonProperty
	private Integer productBuildId;
	@JsonProperty
	private String productBuildName;
	@JsonProperty
	private Integer activityWorkPackageId;
	@JsonProperty
	private String activityWorkPackageName;
	@JsonProperty
	private Integer buildTypeId;
	@JsonProperty
	private String buildTypeName;
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
	private Integer isActive;
	public JsonActivityStatusReport(ActivityTask activityTask){
		this.activityTaskId = activityTask.getActivityTaskId();		
		this.activityTaskName = activityTask.getActivityTaskName();		
		this.remark = activityTask.getRemark();	
		if (activityTask.getIsActive() != null) {
			this.isActive = activityTask.getIsActive();
			
		}
		if (activityTask.getActivity().getActivityMaster() != null) {
			this.activityMasterId =activityTask.getActivity().getActivityMaster().getActivityMasterId();
			this.activityMasterName =activityTask.getActivity().getActivityMaster().getActivityMasterName();			
		}
		
		if(activityTask.getActivity().getProductFeature() != null){
			this.productFeatureId = activityTask.getActivity().getProductFeature().getProductFeatureId();
			this.productFeatureName = activityTask.getActivity().getProductFeature().getProductFeatureName();
		}
		
		if (activityTask.getActivity().getActivityWorkPackage() != null){
			this.activityWorkPackageId = activityTask.getActivity().getActivityWorkPackage().getActivityWorkPackageId();
			this.activityWorkPackageName = activityTask.getActivity().getActivityWorkPackage().getActivityWorkPackageName();
		}
		
		if (activityTask.getActivity().getActivityWorkPackage().getProductBuild() != null){
			this.productBuildId = activityTask.getActivity().getActivityWorkPackage().getProductBuild().getProductBuildId();
			this.productBuildName = activityTask.getActivity().getActivityWorkPackage().getProductBuild().getBuildname();
		}
		
		if (activityTask.getActivity().getActivityWorkPackage().getProductBuild().getBuildType() != null){
			this.buildTypeId = activityTask.getActivity().getActivityWorkPackage().getProductBuild().getBuildType().getStageId();
			this.buildTypeName = activityTask.getActivity().getActivityWorkPackage().getProductBuild().getBuildType().getStageName();
		}
		
		if (activityTask.getActivity().getActivityWorkPackage().getProductBuild().getProductVersion() != null){
			this.productVersionId =  activityTask.getActivity().getActivityWorkPackage().getProductBuild().getProductVersion().getProductVersionListId();
			this.productVersionName = activityTask.getActivity().getActivityWorkPackage().getProductBuild().getProductVersion().getProductVersionName();
		}
		
		if (activityTask.getActivity().getActivityWorkPackage().getProductBuild().getProductVersion().getProductMaster() != null){
			this.productId =  activityTask.getActivity().getActivityWorkPackage().getProductBuild().getProductVersion().getProductMaster().getProductId();
			this.productName = activityTask.getActivity().getActivityWorkPackage().getProductBuild().getProductVersion().getProductMaster().getProductName();
		}
		
		if (activityTask.getActivity() != null) {
			this.activityId = activityTask.getActivity().getActivityId();
			
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
			this.statusName = activityTask.getStatus().getWorkflowStatusDisplayName();
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
		if (activityTask.getCreatedDate() != null)
			this.createdDate = DateUtility.dateToStringInSecond(activityTask
					.getCreatedDate());
		if (activityTask.getModifiedDate() != null)
			this.modifiedDate = DateUtility.dateToStringInSecond(activityTask
					.getCreatedDate());

		if (activityTask.getCreatedBy() != null) {

			this.createdById = activityTask.getCreatedBy().getUserId();
			this.createdByName = activityTask.getCreatedBy().getLoginId();
		}

		if (activityTask.getModifiedBy() != null) {

			this.modifiedById = activityTask.getModifiedBy().getUserId();
			this.modifiedByName = activityTask.getModifiedBy().getLoginId();

		}
		
		if (activityTask.getBaselineStartDate() != null) {
			this.baselineStartDate = DateUtility.dateformatWithOutTime(activityTask.getBaselineStartDate());
		}

		if (activityTask.getBaselineEndDate() != null) {
			this.baselineEndDate = DateUtility.dateformatWithOutTime(activityTask.getBaselineEndDate());
		}
		
		if (activityTask.getPlannedStartDate() != null) {
			this.plannedStartDate = DateUtility.dateformatWithOutTime(activityTask
					.getPlannedStartDate());
		}

		if (activityTask.getPlannedEndDate() != null) {
			this.plannedEndDate = DateUtility.dateformatWithOutTime(activityTask.getPlannedEndDate());
		}
		
		if (activityTask.getActualStartDate()!= null)			
			this.actualStartDate = DateUtility.dateformatWithOutTime(activityTask.getActualStartDate());
		else {
			this.actualStartDate = "mm-dd-yy";
		}

		if (activityTask.getActualEndDate() != null) {
			this.actualEndDate = DateUtility.dateformatWithOutTime(activityTask.getActualEndDate());
		}
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


	public Integer getProductBuildId() {
		return productBuildId;
	}


	public void setProductBuildId(Integer productBuildId) {
		this.productBuildId = productBuildId;
	}


	public String getProductBuildName() {
		return productBuildName;
	}


	public void setProductBuildName(String productBuildName) {
		this.productBuildName = productBuildName;
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


	public Integer getBuildTypeId() {
		return buildTypeId;
	}


	public void setBuildTypeId(Integer buildTypeId) {
		this.buildTypeId = buildTypeId;
	}


	public String getBuildTypeName() {
		return buildTypeName;
	}


	public void setBuildTypeName(String buildTypeName) {
		this.buildTypeName = buildTypeName;
	}


	public Integer getActivityTaskId() {
		return activityTaskId;
	}


	public void setActivityTaskId(Integer activityTaskId) {
		this.activityTaskId = activityTaskId;
	}


	public String getActivityTaskName() {
		return activityTaskName;
	}


	public void setActivityTaskName(String activityTaskName) {
		this.activityTaskName = activityTaskName;
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


	public Integer getActivityId() {
		return activityId;
	}


	public void setActivityId(Integer activityId) {
		this.activityId = activityId;
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
	public Integer getIsActive() {
		return isActive;
	}


	public void setIsActive(Integer isActive) {
		this.isActive = isActive;
	}
}