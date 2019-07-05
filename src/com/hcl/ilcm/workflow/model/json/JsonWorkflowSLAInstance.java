/**
 * 
 */
package com.hcl.ilcm.workflow.model.json;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author silambarasur
 *
 */
public class JsonWorkflowSLAInstance {
	
	
	@JsonProperty
	private Integer instanceId;
	@JsonProperty
	private String instanceName;
	@JsonProperty
	private Integer productId;
	@JsonProperty
	private String productName;
	@JsonProperty
	private Integer entityTypeId;
	@JsonProperty
	private String entityTypeName;
	@JsonProperty
	private Integer entityId;
	@JsonProperty
	private String entityName;
	@JsonProperty
	private Integer workflowStatusId;
	@JsonProperty
	private String workflowStatusName;
	@JsonProperty
	private String workflowStatusCategoryName;
	@JsonProperty
	private Integer totalEffort;
	@JsonProperty
	private String actors;
	@JsonProperty
	private String completedBy;
	@JsonProperty
	private Integer remainingHours;
	@JsonProperty
	private String workflowIndicator;
	@JsonProperty
	private Integer plannedSize;
	@JsonProperty
	private Integer actualSize;
	@JsonProperty
	private Integer plannedEffort;
	@JsonProperty
	private Integer actualEffort;
	
	@JsonProperty
	private String remainingHrsMins;
	
	public String getRemainingHrsMins() {
		return remainingHrsMins;
	}

	public void setRemainingHrsMins(String remainingHrsMins) {
		this.remainingHrsMins = remainingHrsMins;
	}

	public JsonWorkflowSLAInstance() {
		
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
	public String getEntityName() {
		return entityName;
	}
	public void setEntityName(String entityName) {
		this.entityName = entityName;
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

	public Integer getInstanceId() {
		return instanceId;
	}

	public void setInstanceId(Integer instanceId) {
		this.instanceId = instanceId;
	}

	public String getInstanceName() {
		return instanceName;
	}

	public void setInstanceName(String instanceName) {
		this.instanceName = instanceName;
	}

	public Integer getPlannedSize() {
		return plannedSize;
	}

	public void setPlannedSize(Integer plannedSize) {
		this.plannedSize = plannedSize;
	}

	public Integer getActualSize() {
		return actualSize;
	}

	public void setActualSize(Integer actualSize) {
		this.actualSize = actualSize;
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
	
	
	

}
