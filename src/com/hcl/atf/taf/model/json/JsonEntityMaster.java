package com.hcl.atf.taf.model.json;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.hcl.atf.taf.model.EntityMaster;

public class JsonEntityMaster {
	private static final Log log = LogFactory.getLog(JsonEntityMaster.class);
	

	@JsonProperty
	private Integer entityMasterId;
	@JsonProperty
	private String entityMasterName;
	@JsonProperty
	private String entityDisplayName;
	@JsonProperty
	private String description;
	@JsonProperty
	private Integer isWorkflowCapable;
	
	public JsonEntityMaster(){	
	}
	
	public JsonEntityMaster(EntityMaster entityMaster){
		this.entityMasterId = entityMaster.getEntitymasterid();
		this.entityMasterName = entityMaster.getEntitymastername();
		this.entityDisplayName = entityMaster.getEntityDisplayName();
		this.description = entityMaster.getDescription();
		this.isWorkflowCapable = entityMaster.getIsWorkflowCapable();
	}
	
	@JsonIgnore
	public EntityMaster getEntityMaster() {
		EntityMaster entityMaster = new EntityMaster();
		entityMaster.setEntitymasterid(this.entityMasterId);		
		entityMaster.setEntitymastername(this.entityMasterName);
		entityMaster.setDescription(this.description);
		entityMaster.setIsWorkflowCapable(this.isWorkflowCapable);
		return entityMaster;
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

	public String getEntityDisplayName() {
		return entityDisplayName;
	}

	public void setEntityDisplayName(String entityDisplayName) {
		this.entityDisplayName = entityDisplayName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Integer getIsWorkflowCapable() {
		return isWorkflowCapable;
	}

	public void setIsWorkflowCapable(Integer isWorkflowCapable) {
		this.isWorkflowCapable = isWorkflowCapable;
	}

	
}
