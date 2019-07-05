package com.hcl.atf.taf.model.json;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.hcl.atf.taf.controller.utilities.DateUtility;
import com.hcl.atf.taf.model.EntityConfigurationPropertiesMaster;
import com.hcl.atf.taf.model.EntityMaster;

public class JsonEntityConfigurationPropertiesMaster {
	@JsonProperty
	private Integer entityConfigPropertiesMasterId;
	@JsonProperty
	private String name;
	@JsonProperty
	private String description;
	@JsonProperty
	private String type;
	@JsonProperty
	private String options;
	@JsonProperty
	private Integer status;
	@JsonProperty
	private String createdDate;
	@JsonProperty
	private String modifiedDate;
	@JsonProperty
	private Integer entityMasterId;
	@JsonProperty
	private String entityMasterName;
	
	
	public JsonEntityConfigurationPropertiesMaster(EntityConfigurationPropertiesMaster entityConfigurationPropertiesMaster){
		this.entityConfigPropertiesMasterId=entityConfigurationPropertiesMaster.getEntityConfigPropertiesMasterId();
		this.name=entityConfigurationPropertiesMaster.getName();
		this.description=entityConfigurationPropertiesMaster.getDescription();
		this.type=entityConfigurationPropertiesMaster.getType();
		this.options=entityConfigurationPropertiesMaster.getOptions();
		this.status=entityConfigurationPropertiesMaster.getStatus();
		if(entityConfigurationPropertiesMaster.getEntityMaster()!=null){
			this.entityMasterId=entityConfigurationPropertiesMaster.getEntityMaster().getEntitymasterid();
			this.entityMasterName=entityConfigurationPropertiesMaster.getEntityMaster().getEntitymastername();
		}
		
		
		if(entityConfigurationPropertiesMaster.getCreatedDate()!=null){
			this.createdDate= DateUtility.dateToStringInSecond(entityConfigurationPropertiesMaster.getCreatedDate());
		}if(entityConfigurationPropertiesMaster.getModifiedDate()!=null){
			this.modifiedDate= DateUtility.dateToStringInSecond(entityConfigurationPropertiesMaster.getModifiedDate());
		}
		
	}
	@JsonIgnore
	public EntityConfigurationPropertiesMaster getEntityConfigurationPropertiesMaster() {
		EntityConfigurationPropertiesMaster entityConfigurationPropertiesMaster=new EntityConfigurationPropertiesMaster();
		
		entityConfigurationPropertiesMaster.setEntityConfigPropertiesMasterId(entityConfigPropertiesMasterId);
		entityConfigurationPropertiesMaster.setName(name);
		entityConfigurationPropertiesMaster.setDescription(description);
		entityConfigurationPropertiesMaster.setType(type);
		entityConfigurationPropertiesMaster.setOptions(options);
		entityConfigurationPropertiesMaster.setStatus(status);
		EntityMaster entityMaster=new EntityMaster();
		entityMaster.setEntitymasterid(entityMasterId);
		entityConfigurationPropertiesMaster.setEntityMaster(entityMaster);
		entityConfigurationPropertiesMaster.setCreatedDate(DateUtility.toDateInSec(createdDate));
		entityConfigurationPropertiesMaster.setModifiedDate(DateUtility.toDateInSec(modifiedDate));
		return entityConfigurationPropertiesMaster;
		
	}
	public Integer getEntityConfigPropertiesMasterId() {
		return entityConfigPropertiesMasterId;
	}
	public void setEntityConfigPropertiesMasterId(
			Integer entityConfigPropertiesMasterId) {
		this.entityConfigPropertiesMasterId = entityConfigPropertiesMasterId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getOptions() {
		return options;
	}
	public void setOptions(String options) {
		this.options = options;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
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
	public Integer getEntityMasterId() {
		return entityMasterId;
	}
	public void setEntityMasterId(Integer entityMasterId) {
		this.entityMasterId = entityMasterId;
	}
	public String getEntityMasterName() {
		return entityMasterName;
	}
	public void setEntityMasterName(String entityMasterName) {
		this.entityMasterName = entityMasterName;
	}
	
	
}
