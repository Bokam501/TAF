/**
 * 
 */
package com.hcl.ilcm.workflow.model.json;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.hcl.atf.taf.controller.utilities.DateUtility;
import com.hcl.atf.taf.model.EntityMaster;
import com.hcl.atf.taf.model.StatusCategory;
import com.hcl.atf.taf.model.WorkflowStatusCategory;
import com.hcl.ilcm.workflow.model.WorkflowStatus;
import com.hcl.ilcm.workflow.model.WorkflowStatusPolicy;

/**
 * @author silambarasur
 *
 */
public class JsonWorkflowStatusPolicy {
	
	@JsonProperty
	private Integer workflowStatusPolicyId;
	@JsonProperty
	private Integer workflowStatusId;
	@JsonProperty
	private String workflowStatusName;
	@JsonProperty
	private String workflowStatusDisplayName;
	@JsonProperty
	private String statusPolicyType;
	@JsonProperty
	private Integer entityTypeId;
	@JsonProperty
	private String entityTypeName;
	@JsonProperty
	private Integer entityId;
	@JsonProperty
	private Integer entityInstanceId;
	@JsonProperty
	private String level;
	@JsonProperty
	private Integer levelId;
	@JsonProperty
	private Integer activeStatus;
	@JsonProperty
	private Integer weightage;
	@JsonProperty
	private String actionScope;
	@JsonProperty
	private Integer slaDuration;
	@JsonProperty
	private String slaViolationAction;
	@JsonProperty
	private String description;
	@JsonProperty
	private Integer statusCategoryId;
	@JsonProperty
	private String statusCategoryName;
	@JsonProperty
	private Integer statusOrder;
	
	@JsonProperty
	private String workflowStatusType;
	
	@JsonProperty
	private String stautsTransitionPolicy;
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
	private Integer isLifeCycleStage;
	
	@JsonProperty
	private Integer workflowStatusCategoryId;
	
	@JsonProperty
	private String workflowStatusCategoryName;
	
	@JsonProperty
	private String plannedStartDate;
	@JsonProperty
	private String plannedEndDate;
	@JsonProperty
	private Float plannedEffort;
	@JsonProperty
	private String actualStartDate;
	@JsonProperty
	private String actualEndDate;
	@JsonProperty
	private Float actualEffort;
	@JsonProperty
	private String statusActors;
	
	public JsonWorkflowStatusPolicy() {
		
	}
	
	public Integer getWorkflowStatusCategoryId() {
		return workflowStatusCategoryId;
	}

	public void setWorkflowStatusCategoryId(Integer workflowStatusCategoryId) {
		this.workflowStatusCategoryId = workflowStatusCategoryId;
	}

	public String getWorkflowStatusCategoryName() {
		return workflowStatusCategoryName;
	}

	public void setWorkflowStatusCategoryName(String workflowStatusCategoryName) {
		this.workflowStatusCategoryName = workflowStatusCategoryName;
	}

	public JsonWorkflowStatusPolicy(WorkflowStatusPolicy workflowStatusPolicy) {
		this.actionScope=workflowStatusPolicy.getActionScope();
		this.activeStatus=workflowStatusPolicy.getActiveStatus();
		this.entityId=workflowStatusPolicy.getEntityId();
		this.entityInstanceId=workflowStatusPolicy.getEntityInstanceId();
		if(workflowStatusPolicy.getEntityType() != null) {
			this.entityTypeId=workflowStatusPolicy.getEntityType().getEntitymasterid();
			this.entityTypeName=workflowStatusPolicy.getEntityType().getEntitymastername();
		}
		this.level= workflowStatusPolicy.getLevel();
		this.levelId =workflowStatusPolicy.getLevelId();
		if(workflowStatusPolicy.getSlaDuration() != null){
			this.slaDuration=workflowStatusPolicy.getSlaDuration();
		}else{
			this.slaDuration = 1;
		}
		this.slaViolationAction=workflowStatusPolicy.getSlaViolationAction();
		this.statusPolicyType=workflowStatusPolicy.getStatusPolicyType();
		if(workflowStatusPolicy.getWeightage() != null){
			this.weightage=workflowStatusPolicy.getWeightage();
		}
		if(workflowStatusPolicy.getWorkflowStatus() != null) {
			this.workflowStatusId=workflowStatusPolicy.getWorkflowStatus().getWorkflowStatusId();
			this.workflowStatusName=workflowStatusPolicy.getWorkflowStatus().getWorkflowStatusName();
			this.workflowStatusDisplayName=workflowStatusPolicy.getWorkflowStatus().getWorkflowStatusDisplayName();
			this.description=workflowStatusPolicy.getWorkflowStatus().getWorkflowStatusDescription();
			this.statusOrder=workflowStatusPolicy.getWorkflowStatus().getStatusOrder();
			this.workflowStatusType = workflowStatusPolicy.getWorkflowStatus().getWorkflowStatusType();
			if(workflowStatusPolicy.getWorkflowStatus().getStatusCategory() != null) {
				this.statusCategoryId=workflowStatusPolicy.getWorkflowStatus().getStatusCategory().getStatusCategoryId();
				this.statusCategoryName=workflowStatusPolicy.getWorkflowStatus().getStatusCategory().getStatusCategoryName();
			}
			if(workflowStatusPolicy.getWorkflowStatus().getWorkflowStatusCategory() != null) {
				this.workflowStatusCategoryId=workflowStatusPolicy.getWorkflowStatus().getWorkflowStatusCategory().getWorkflowStatusCategoryId();
				this.workflowStatusCategoryName=workflowStatusPolicy.getWorkflowStatus().getWorkflowStatusCategory().getWorkflowStatusCategoryName();
			}else{
				this.workflowStatusCategoryId = 0;
				this.workflowStatusCategoryName = "--";
			}
			this.isLifeCycleStage = workflowStatusPolicy.getWorkflowStatus().getIsLifeCycleStage();
		}
		this.workflowStatusPolicyId=workflowStatusPolicy.getWorkflowStatusPolicyId();
		this.stautsTransitionPolicy = workflowStatusPolicy.getStautsTransitionPolicy();
		if(workflowStatusPolicy.getPlannedStartDate() != null){
			this.plannedStartDate = DateUtility.dateToStringWithSeconds1(workflowStatusPolicy.getPlannedStartDate());
		}
		if(workflowStatusPolicy.getPlannedEndDate() != null){
			this.plannedEndDate = DateUtility.dateToStringWithSeconds1(workflowStatusPolicy.getPlannedEndDate());
		}
		this.plannedEffort = workflowStatusPolicy.getPlannedEffort();
		if(workflowStatusPolicy.getActualStartDate() != null){
			this.actualStartDate = DateUtility.dateToStringWithSeconds1(workflowStatusPolicy.getActualStartDate());
		}
		if(workflowStatusPolicy.getActualEndDate() != null){
			this.actualEndDate = DateUtility.dateToStringWithSeconds1(workflowStatusPolicy.getActualEndDate());
		}
		this.actualEffort = workflowStatusPolicy.getActualEffort();

	}
	public Integer getWorkflowStatusPolicyId() {
		return workflowStatusPolicyId;
	}
	public void setWorkflowStatusPolicyId(Integer workflowStatusPolicyId) {
		this.workflowStatusPolicyId = workflowStatusPolicyId;
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
	public String getWorkflowStatusDisplayName() {
		return workflowStatusDisplayName;
	}
	public void setWorkflowStatusDisplayName(String workflowStatusDisplayName) {
		this.workflowStatusDisplayName = workflowStatusDisplayName;
	}
	public String getStatusPolicyType() {
		return statusPolicyType;
	}
	public void setStatusPolicyType(String statusPolicyType) {
		this.statusPolicyType = statusPolicyType;
	}
	public Integer getEntityTypeId() {
		return entityTypeId;
	}
	public void setEntityTypeId(Integer entityTypeId) {
		this.entityTypeId = entityTypeId;
	}
	public String getEntityTypeName() {
		return entityTypeName;
	}
	public void setEntityTypeName(String entityTypeName) {
		this.entityTypeName = entityTypeName;
	}
	public Integer getEntityId() {
		return entityId;
	}
	public void setEntityId(Integer entityId) {
		this.entityId = entityId;
	}
	public Integer getEntityInstanceId() {
		return entityInstanceId;
	}
	public void setEntityInstanceId(Integer entityInstanceId) {
		this.entityInstanceId = entityInstanceId;
	}
	public String getLevel() {
		return level;
	}
	public void setLevel(String level) {
		this.level = level;
	}
	public Integer getLevelId() {
		return levelId;
	}
	public void setLevelId(Integer levelId) {
		this.levelId = levelId;
	}
	public Integer getActiveStatus() {
		return activeStatus;
	}
	public void setActiveStatus(Integer activeStatus) {
		this.activeStatus = activeStatus;
	}
	public Integer getWeightage() {
		return weightage;
	}
	public void setWeightage(Integer weightage) {
		this.weightage = weightage;
	}
	public String getActionScope() {
		return actionScope;
	}
	public void setActionScope(String actionScope) {
		this.actionScope = actionScope;
	}
	public Integer getSlaDuration() {
		return slaDuration;
	}
	public void setSlaDuration(Integer slaDuration) {
		this.slaDuration = slaDuration;
	}
	public String getSlaViolationAction() {
		return slaViolationAction;
	}
	public void setSlaViolationAction(String slaViolationAction) {
		this.slaViolationAction = slaViolationAction;
	}
	
	@JsonIgnore
	public WorkflowStatusPolicy getWorkflowStatusPolicy() {
		WorkflowStatusPolicy workflowStatusPolicy = new WorkflowStatusPolicy();
		EntityMaster entityType=new EntityMaster();
		WorkflowStatus workflowStatus= new WorkflowStatus(); 
		StatusCategory statusCategory = new StatusCategory();
		workflowStatusPolicy.setActionScope(actionScope);
		workflowStatusPolicy.setActiveStatus(activeStatus);
		if(entityId != null && entityId != 0){
			workflowStatusPolicy.setEntityId(entityId);
		}else{
			workflowStatusPolicy.setEntityId(null);
		}
		if(entityInstanceId != null && entityInstanceId != 0){
			workflowStatusPolicy.setEntityInstanceId(entityInstanceId);
		}else{
			workflowStatusPolicy.setEntityInstanceId(null);
		}
		if(entityTypeId != null){
			entityType.setEntitymasterid(entityTypeId);
			entityType.setEntitymastername(entityTypeName);
		}else{
			entityType = null;
		}
		workflowStatusPolicy.setEntityType(entityType);
		workflowStatusPolicy.setLevel(level);
		if(this.slaDuration != null){
			workflowStatusPolicy.setSlaDuration(slaDuration);
		}else{
			workflowStatusPolicy.setSlaDuration(1);
		}
		workflowStatusPolicy.setLevelId(levelId);
		workflowStatusPolicy.setSlaViolationAction(slaViolationAction);
		if(statusPolicyType == null) {
			workflowStatusPolicy.setStatusPolicyType("Workflow");
		}else {
			workflowStatusPolicy.setStatusPolicyType(statusPolicyType);
		}
		if(this.weightage != null){
			workflowStatusPolicy.setWeightage(weightage);
		}
		workflowStatusPolicy.setStautsTransitionPolicy(stautsTransitionPolicy);
		workflowStatusPolicy.setWorkflowStatusPolicyId(workflowStatusPolicyId);
		workflowStatus.setWorkflowStatusId(workflowStatusId);
		workflowStatus.setWorkflowStatusName(workflowStatusName);
		workflowStatus.setWorkflowStatusDisplayName(workflowStatusDisplayName);
		workflowStatus.setStatusOrder(statusOrder);
		workflowStatus.setWorkflowStatusType(workflowStatusType);
		workflowStatus.setWorkflowStatusDescription(this.description);
		workflowStatus.setActiveStatus(activeStatus);
		if(this.statusCategoryId != null && this.statusCategoryId > 0){
			statusCategory.setStatusCategoryId(statusCategoryId);
			statusCategory.setStatusCategoryName(statusCategoryName);
		}else{
			statusCategory.setStatusCategoryId(1);
		}
		workflowStatus.setStatusCategory(statusCategory);
		
		if(this.workflowStatusCategoryId != null && this.workflowStatusCategoryId > 0){
			WorkflowStatusCategory workflowStatusCategory = new WorkflowStatusCategory(); 
			workflowStatusCategory.setWorkflowStatusCategoryId(workflowStatusCategoryId);
			workflowStatusCategory.setWorkflowStatusCategoryName(workflowStatusCategoryName);
			workflowStatus.setWorkflowStatusCategory(workflowStatusCategory);
		}
		
		if(isLifeCycleStage !=null) {
			workflowStatus.setIsLifeCycleStage(isLifeCycleStage);
		}else {
			workflowStatus.setIsLifeCycleStage(1);
		}
		workflowStatusPolicy.setWorkflowStatus(workflowStatus);
		if(this.plannedStartDate != null && !this.plannedStartDate.trim().isEmpty()){
			workflowStatusPolicy.setPlannedStartDate(DateUtility.toFormatDate(this.plannedStartDate));
		}
		if(this.plannedEndDate != null && !this.plannedEndDate.trim().isEmpty()){
			workflowStatusPolicy.setPlannedEndDate(DateUtility.toFormatDate(this.plannedEndDate));
		}
		workflowStatusPolicy.setPlannedEffort(this.plannedEffort);
		if(this.actualStartDate != null && !this.actualStartDate.trim().isEmpty()){
			workflowStatusPolicy.setActualStartDate(DateUtility.toFormatDate(this.actualStartDate));
		}
		if(this.actualEndDate != null && !this.actualEndDate.trim().isEmpty()){
			workflowStatusPolicy.setActualEndDate(DateUtility.toFormatDate(this.actualEndDate));
		}
		workflowStatusPolicy.setActualEffort(this.actualEffort);
		return workflowStatusPolicy;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
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

	public Integer getStatusOrder() {
		return statusOrder;
	}

	public void setStatusOrder(Integer statusOrder) {
		this.statusOrder = statusOrder;
	}

	public String getWorkflowStatusType() {
		return workflowStatusType;
	}

	public void setWorkflowStatusType(String workflowStatusType) {
		this.workflowStatusType = workflowStatusType;
	}
	
	public String getStautsTransitionPolicy() {
		return stautsTransitionPolicy;
	}

	public void setStautsTransitionPolicy(String stautsTransitionPolicy) {
		this.stautsTransitionPolicy = stautsTransitionPolicy;
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

	public Integer getIsLifeCycleStage() {
		return isLifeCycleStage;
	}

	public void setIsLifeCycleStage(Integer isLifeCycleStage) {
		this.isLifeCycleStage = isLifeCycleStage;
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

	public Float getPlannedEffort() {
		return plannedEffort;
	}

	public void setPlannedEffort(Float plannedEffort) {
		this.plannedEffort = plannedEffort;
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

	public Float getActualEffort() {
		return actualEffort;
	}

	public void setActualEffort(Float actualEffort) {
		this.actualEffort = actualEffort;
	}

	public String getStatusActors() {
		return statusActors;
	}

	public void setStatusActors(String statusActors) {
		this.statusActors = statusActors;
	}

}
