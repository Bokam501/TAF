package com.hcl.atf.taf.model.json;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.hcl.atf.taf.model.ActivityEntityMaster;

public class JsonActivityEntityMaster {

	@JsonProperty
	private Integer entityId;
	@JsonProperty
	private String entityName;
	@JsonProperty
	private String description;
	
	public JsonActivityEntityMaster() {
	}

	public JsonActivityEntityMaster(ActivityEntityMaster activityEntityMaster) {
		this.entityId = activityEntityMaster.getEntityId();
		this.entityName = activityEntityMaster.getEntityName();
		
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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	

}
