package com.hcl.atf.taf.model.dto;



public class ClarificationTrackerDTO {	
	private Integer clarificationTrackerId;
	private String clarificationTitle;
	private Integer clarificationScopeId;
	private Integer activityId;
	private String clarificationDescription;
	private String clarificationTypeName;
	private Integer clarificationTypeId;
	private String raisedDate;
	private Integer raisedById;
	private String raisedByName;
	private Integer priorityId;
	private String priorityName;
	private Integer ownerId;
	private String ownerName;
	private Integer workflowStatusId;
	private String workflowStatusName;
	private Integer dependentCR;
	private Integer dependentActivityTracker;
	private Integer productId;
	private Integer entityTypeId;
	private String entityTypeName;
	private Integer entityTypeId2;
	private Integer entityInstanceId;
	private String plannedStartDate;
	private String plannedEndDate;
	private String actualStartDate;
	private String actualEndDate;
	private Integer resolution;
	private Integer testFactoryId;	
	private String entityType;	
	private String entityInstanceName;
	private Integer planExpectedValue;
	private String modifiedField;
	private String 	modifiedFieldTitle;	
	private String oldFieldID;
	private String	oldFieldValue;
	private String	modifiedFieldID;
	private String modifiedFieldValue;
	private Integer attachmentCount;
	
	public ClarificationTrackerDTO(){
		this.clarificationTrackerId = 0;
		this.clarificationTitle = "";
		this.clarificationScopeId = 0;
		this.activityId = 0;
		this.clarificationDescription = "";
		this.clarificationTypeName = "";
		this.clarificationTypeId = 0;
		this.raisedDate = "";
		this.raisedById = 0;
		this.raisedByName = "";
		this.priorityId = 0;
		this.priorityName = "";
		this.ownerId = 0;
		this.ownerName = "";
		this.workflowStatusId = 0;
		this.workflowStatusName = "";
		this.dependentCR = 0;
		this.dependentActivityTracker = 0;
		this.productId = 0;
		this.entityTypeId = 0;
		this.entityTypeName = "";
		this.entityTypeId2 = 0;
		this.entityInstanceId = 0;
		this.plannedStartDate = "";
		this.plannedEndDate = "";
		this.actualStartDate = "";
		this.actualEndDate = "";
		this.resolution = 0;
		this.testFactoryId = 0;	
		this.entityType = "";	
		this.entityInstanceName = "";
		this.planExpectedValue = 0;
		this.modifiedField = "";
		this.	modifiedFieldTitle = "";	
		this.oldFieldID = "";
		this.oldFieldValue = "";
		this.modifiedFieldID = "";
		this.modifiedFieldValue = "";
		this.attachmentCount = 0;
	}

	public Integer getClarificationTrackerId() {
		return clarificationTrackerId;
	}

	public void setClarificationTrackerId(Integer clarificationTrackerId) {
		this.clarificationTrackerId = clarificationTrackerId;
	}

	public String getClarificationTitle() {
		return clarificationTitle;
	}

	public void setClarificationTitle(String clarificationTitle) {
		this.clarificationTitle = clarificationTitle;
	}

	public Integer getClarificationScopeId() {
		return clarificationScopeId;
	}

	public void setClarificationScopeId(Integer clarificationScopeId) {
		this.clarificationScopeId = clarificationScopeId;
	}

	public Integer getActivityId() {
		return activityId;
	}

	public void setActivityId(Integer activityId) {
		this.activityId = activityId;
	}

	public String getClarificationDescription() {
		return clarificationDescription;
	}

	public void setClarificationDescription(String clarificationDescription) {
		this.clarificationDescription = clarificationDescription;
	}

	public String getClarificationTypeName() {
		return clarificationTypeName;
	}

	public void setClarificationTypeName(String clarificationTypeName) {
		this.clarificationTypeName = clarificationTypeName;
	}

	public Integer getClarificationTypeId() {
		return clarificationTypeId;
	}

	public void setClarificationTypeId(Integer clarificationTypeId) {
		this.clarificationTypeId = clarificationTypeId;
	}

	public String getRaisedDate() {
		return raisedDate;
	}

	public void setRaisedDate(String raisedDate) {
		this.raisedDate = raisedDate;
	}

	public Integer getRaisedById() {
		return raisedById;
	}

	public void setRaisedById(Integer raisedById) {
		this.raisedById = raisedById;
	}

	public String getRaisedByName() {
		return raisedByName;
	}

	public void setRaisedByName(String raisedByName) {
		this.raisedByName = raisedByName;
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

	public Integer getDependentCR() {
		return dependentCR;
	}

	public void setDependentCR(Integer dependentCR) {
		this.dependentCR = dependentCR;
	}

	public Integer getDependentActivityTracker() {
		return dependentActivityTracker;
	}

	public void setDependentActivityTracker(Integer dependentActivityTracker) {
		this.dependentActivityTracker = dependentActivityTracker;
	}

	public Integer getProductId() {
		return productId;
	}

	public void setProductId(Integer productId) {
		this.productId = productId;
	}

	public Integer getEntityTypeId() {
		return entityTypeId;
	}

	public void setEntityTypeId(Integer entityTypeId) {
		this.entityTypeId = entityTypeId;
	}

	public Integer getEntityTypeId2() {
		return entityTypeId2;
	}

	public void setEntityTypeId2(Integer entityTypeId2) {
		this.entityTypeId2 = entityTypeId2;
	}

	public Integer getEntityInstanceId() {
		return entityInstanceId;
	}

	public void setEntityInstanceId(Integer entityInstanceId) {
		this.entityInstanceId = entityInstanceId;
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

	public Integer getResolution() {
		return resolution;
	}

	public void setResolution(Integer resolution) {
		this.resolution = resolution;
	}

	public Integer getTestFactoryId() {
		return testFactoryId;
	}

	public void setTestFactoryId(Integer testFactoryId) {
		this.testFactoryId = testFactoryId;
	}

	public String getEntityType() {
		return entityType;
	}

	public void setEntityType(String entityType) {
		this.entityType = entityType;
	}

	public String getEntityTypeName() {
		return entityTypeName;
	}

	public void setEntityTypeName(String entityTypeName) {
		this.entityTypeName = entityTypeName;
	}

	public String getEntityInstanceName() {
		return entityInstanceName;
	}

	public void setEntityInstanceName(String entityInstanceName) {
		this.entityInstanceName = entityInstanceName;
	}

	public Integer getPlanExpectedValue() {
		return planExpectedValue;
	}

	public void setPlanExpectedValue(Integer planExpectedValue) {
		this.planExpectedValue = planExpectedValue;
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

	public Integer getAttachmentCount() {
		return attachmentCount;
	}

	public void setAttachmentCount(Integer attachmentCount) {
		this.attachmentCount = attachmentCount;
	}
}
