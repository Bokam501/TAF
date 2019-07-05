package com.hcl.atf.taf.model.dto;

import java.util.Date;

import com.hcl.atf.taf.model.EntityConfigurationPropertiesMaster;


public class EntityConfigurationPropertiesDTO {
	private Integer entityConfigPropertyId;
	private String value;
	private Integer status;
	private Date createdDate;
	private Date modifiedDate;
	private Integer entityId;
	private EntityConfigurationPropertiesMaster entityConfigurationPropertiesMaster;
	private Integer runConfigurationId;
	private String runConfigurationName;
	
	public EntityConfigurationPropertiesDTO(){
		
	}

	public Integer getEntityConfigPropertyId() {
		return entityConfigPropertyId;
	}

	public void setEntityConfigPropertyId(Integer entityConfigPropertyId) {
		this.entityConfigPropertyId = entityConfigPropertyId;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
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

	public Integer getEntityId() {
		return entityId;
	}

	public void setEntityId(Integer entityId) {
		this.entityId = entityId;
	}

	public EntityConfigurationPropertiesMaster getEntityConfigurationPropertiesMaster() {
		return entityConfigurationPropertiesMaster;
	}

	public void setEntityConfigurationPropertiesMaster(
			EntityConfigurationPropertiesMaster entityConfigurationPropertiesMaster) {
		this.entityConfigurationPropertiesMaster = entityConfigurationPropertiesMaster;
	}

	public Integer getRunConfigurationId() {
		return runConfigurationId;
	}

	public void setRunConfigurationId(Integer runConfigurationId) {
		this.runConfigurationId = runConfigurationId;
	}

	public String getRunConfigurationName() {
		return runConfigurationName;
	}

	public void setRunConfigurationName(String runConfigurationName) {
		this.runConfigurationName = runConfigurationName;
	}

}
