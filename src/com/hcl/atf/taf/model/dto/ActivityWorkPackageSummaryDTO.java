package com.hcl.atf.taf.model.dto;

import java.util.Date;


public class ActivityWorkPackageSummaryDTO {
	private Integer activityWorkPackageId;
	private String activityWorkPackageName;
	private String description;
	private Integer productBuildId;
	private String productBuildName;
	private Integer productVersionListId;
	private String productVersionName;	
	private  Date baselineStartDate; 
	private  Date baselineEndDate;
	private  Date plannedStartDate; 
	private  Date plannedEndDate;
	private  Date actualStartDate;
	private  Date actualEndDate;

	private  Integer statusCategoryId;
	private  String statusCategoryName;
	private  Integer  priorityId;
	private  String priorityName;
	private  Integer  competencyId;
	private  String competencyName;
	private Integer ownerId;
	private String ownerName;
	private String activityWorkPackageTag;
	private String activityWorkPackageType;
	private String remark;
	private Integer isActive;
	

	private  Date createdDate;
	private  Date modifiedDate;
	private  Integer createdById;
	private  String createdByName;
	private  Integer modifiedById;
	private  String modifiedByName;
	
	private Integer statusId;
	private String statusName;
	private String statusDisplayName;
	private String workflowStatusName;
	private String workflowStatusDisplayName;
	
	private Integer totalEffort;
	private Integer workflowId;
	private String actors;
	private String completedBy;
	private Integer remainingHours;
	private String workflowIndicator;
	private String workflowRAG;	
	private String remainingHrsMins;	
	private String modifiedField;
	private String 	modifiedFieldTitle;	
	private String oldFieldID;
	private String	oldFieldValue;
	private String	modifiedFieldID;
	private String modifiedFieldValue;
	private Float percentageCompletion;
	
	private Integer productId;
	private String productName;
	
	private Integer clarificationCount;
	private Integer changeRequestCount;
	private Integer attachmentCount;
	
	private Integer actBeginCount;
	private Integer actIntermediateCount;
	private Integer actAbortCount;
	private Integer actEndCount;	
	private Integer activityCount;	
	
	public ActivityWorkPackageSummaryDTO(){
		this.activityWorkPackageId = 0;
		this.activityWorkPackageName = "";
		this.description = "";
		this.productBuildId = 0;
		this.productBuildName = "";

		this.productVersionListId = 0;;
		this.productVersionName = "";	
		this.statusCategoryId = 0;
		this.statusCategoryName = "";
		this.priorityId = 0;
		this.priorityName = "";
		this.competencyId = 0;
		this.competencyName = "";
		this.ownerId = 0;
		this.ownerName = "";
		this.activityWorkPackageTag = "";
		this.activityWorkPackageType = "";
		this.remark = "";
		this.isActive = 0;
		


		this.createdById = 0;
		this.createdByName = "";
		this.modifiedById = 0;
		this.modifiedByName = "";
		
		this.statusId = 0;
		this.statusName = "";
		this.statusDisplayName = "";
		this.workflowStatusName = "";
		this.workflowStatusDisplayName = "";
		this.totalEffort = 0;
		this.workflowId = 0;
		this.actors = "";
		this.completedBy = "";
		this.remainingHours = 0;
		this.workflowIndicator = "";
		this.workflowRAG = "";
		this.attachmentCount = 0;	
		this.remainingHrsMins = "";	
		this.modifiedField = "";
		this.modifiedFieldTitle = "";	
		this.oldFieldID = "";
		this.oldFieldValue = "";
		this.modifiedFieldID = "";
		this.modifiedFieldValue = "";
		this.percentageCompletion = 0f;
		
		this.productId = 0;
		this.productName = "";
		
		this.clarificationCount = 0;
		this.changeRequestCount = 0;
		this.attachmentCount = 0;
		
		this.actBeginCount = 0;
		this.actIntermediateCount = 0;
		this.actAbortCount = 0;
		this.actEndCount = 0;
		this.activityCount = 0;
		
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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
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

	public Integer getProductVersionListId() {
		return productVersionListId;
	}

	public void setProductVersionListId(Integer productVersionListId) {
		this.productVersionListId = productVersionListId;
	}

	public String getProductVersionName() {
		return productVersionName;
	}

	public void setProductVersionName(String productVersionName) {
		this.productVersionName = productVersionName;
	}

	public Date getBaselineStartDate() {
		return baselineStartDate;
	}

	public void setBaselineStartDate(Date baselineStartDate) {
		this.baselineStartDate = baselineStartDate;
	}

	public Date getBaselineEndDate() {
		return baselineEndDate;
	}

	public void setBaselineEndDate(Date baselineEndDate) {
		this.baselineEndDate = baselineEndDate;
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

	public Integer getCompetencyId() {
		return competencyId;
	}

	public void setCompetencyId(Integer competencyId) {
		this.competencyId = competencyId;
	}

	public String getCompetencyName() {
		return competencyName;
	}

	public void setCompetencyName(String competencyName) {
		this.competencyName = competencyName;
	}

	public Integer getOwnerId() {
		return ownerId;
	}

	public void setOwnerId(Integer ownerId) {
		this.ownerId = ownerId;
	}

	public String getOwnerName() {
		return ownerName;
	}

	public void setOwnerName(String ownerName) {
		this.ownerName = ownerName;
	}

	public String getActivityWorkPackageTag() {
		return activityWorkPackageTag;
	}

	public void setActivityWorkPackageTag(String activityWorkPackageTag) {
		this.activityWorkPackageTag = activityWorkPackageTag;
	}

	public String getActivityWorkPackageType() {
		return activityWorkPackageType;
	}

	public void setActivityWorkPackageType(String activityWorkPackageType) {
		this.activityWorkPackageType = activityWorkPackageType;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Integer getIsActive() {
		return isActive;
	}

	public void setIsActive(Integer isActive) {
		this.isActive = isActive;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public Date getModifiedDate() {
		return modifiedDate;
	}

	public void setModifiedDate(Date modifiedDate) {
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

	
	public String getWorkflowStatusName() {
		return workflowStatusName;
	}

	public void setWorkflowStatusName(String workflowStatusName) {
		this.workflowStatusName = workflowStatusName;
	}

	public String getWorkflowStatusDisplayName() {
		return workflowStatusDisplayName;
	}

	public void setWorkflowStatusDisplayName(String workflowStatusDisplayName) {
		this.workflowStatusDisplayName = workflowStatusDisplayName;
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

	public Integer getClarificationCount() {
		return clarificationCount;
	}

	public void setClarificationCount(Integer clarificationCount) {
		this.clarificationCount = clarificationCount;
	}

	public Integer getChangeRequestCount() {
		return changeRequestCount;
	}

	public void setChangeRequestCount(Integer changeRequestCount) {
		this.changeRequestCount = changeRequestCount;
	}

	public Integer getActBeginCount() {
		return actBeginCount;
	}

	public void setActBeginCount(Integer actBeginCount) {
		this.actBeginCount = actBeginCount;
	}

	public Integer getActIntermediateCount() {
		return actIntermediateCount;
	}

	public void setActIntermediateCount(Integer actIntermediateCount) {
		this.actIntermediateCount = actIntermediateCount;
	}

	public Integer getActAbortCount() {
		return actAbortCount;
	}

	public void setActAbortCount(Integer actAbortCount) {
		this.actAbortCount = actAbortCount;
	}

	public Integer getActEndCount() {
		return actEndCount;
	}

	public void setActEndCount(Integer actEndCount) {
		this.actEndCount = actEndCount;
	}

	public Integer getActivityCount() {
		return activityCount;
	}

	public void setActivityCount(Integer activityCount) {
		this.activityCount = activityCount;
	}


	
}
