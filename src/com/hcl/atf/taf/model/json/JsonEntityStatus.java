package com.hcl.atf.taf.model.json;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.hcl.atf.taf.model.StatusCategory;
import com.hcl.atf.taf.model.EntityStatus;



public class JsonEntityStatus {	
	
	@JsonProperty
	private Integer entityStatusId;
	@JsonProperty
	private String entityStatusName;
	@JsonProperty
	private String entityStatusDescription;
	@JsonProperty
	private Integer statusCategoryId;
	@JsonProperty
	private String statusCategoryName;
	@JsonProperty
	private Integer activeStatus;
	@JsonProperty
	private Integer entityTypeId;
	@JsonProperty
	private String entityTypeName;
	@JsonProperty
	private Integer levelId;
	@JsonProperty
	private String level;
	@JsonProperty
	private Integer weightage;
	@JsonProperty
	private Integer entityId;
	
	@JsonProperty
	private Integer parentStatusId;
	@JsonProperty
	private Integer resolution;
	
	
	
	public Integer getParentStatusId() {
		return parentStatusId;
	}

	public void setParentStatusId(Integer parentStatusId) {
		this.parentStatusId = parentStatusId;
	}

	public JsonEntityStatus() {
	}

	public JsonEntityStatus(EntityStatus entityStatus) {
		this.entityStatusId = entityStatus.getEntityStatusId();
		this.entityStatusName = entityStatus.getEntityStatusName();
		this.entityStatusDescription = entityStatus.getEntityStatusDescription();
		if(entityStatus.getStatusCategory() != null){
			this.statusCategoryId = entityStatus.getStatusCategory().getStatusCategoryId();
			this.statusCategoryName = entityStatus.getStatusCategory().getStatusCategoryName();
		}
		this.activeStatus = entityStatus.getActiveStatus();
	}


	public String getEntityStatusName() {
		return entityStatusName;
	}

	public void setEntityStatusName(String entityStatusName) {
		this.entityStatusName = entityStatusName;
	}

	public String getEntityStatusDescription() {
		return entityStatusDescription;
	}

	public void setEntityStatusDescription(String entityStatusDescription) {
		this.entityStatusDescription = entityStatusDescription;
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

	public Integer getActiveStatus() {
		return activeStatus;
	}

	public void setActiveStatus(Integer activeStatus) {
		this.activeStatus = activeStatus;
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

	public Integer getLevelId() {
		return levelId;
	}

	public void setLevelId(Integer levelId) {
		this.levelId = levelId;
	}

	public String getLevel() {
		return level;
	}

	public void setLevel(String level) {
		this.level = level;
	}

	public Integer getWeightage() {
		return weightage;
	}

	public void setWeightage(Integer weightage) {
		this.weightage = weightage;
	}

	public Integer getEntityId() {
		return entityId;
	}

	public void setEntityId(Integer entityId) {
		this.entityId = entityId;
	}

	public void setEntityStatusId(Integer entityStatusId) {
		this.entityStatusId = entityStatusId;
	}

	public Integer getEntityStatusId() {
		return entityStatusId;
	}
	
	public Integer getResolution() {
		return resolution;
	}

	public void setResolution(Integer resolution) {
		this.resolution = resolution;
	}

	@JsonIgnore
	public EntityStatus getEntityStatus() {
		EntityStatus entityStatus = new EntityStatus();
		if(this.entityStatusId != null){
			entityStatus.setEntityStatusId(this.entityStatusId);
		}
		entityStatus.setEntityStatusName(this.entityStatusName);
		entityStatus.setEntityStatusDescription(this.entityStatusDescription);
		StatusCategory statusCategory = new StatusCategory();
		if(this.statusCategoryId != null){
			statusCategory.setStatusCategoryId(this.statusCategoryId);
		}else{
			statusCategory.setStatusCategoryId(1);
		}
		entityStatus.setStatusCategory(statusCategory);
		if(this.activeStatus != null){
			entityStatus.setActiveStatus(this.activeStatus);
		}else{
			entityStatus.setActiveStatus(1);
		}
		return entityStatus;
	}

	
}
