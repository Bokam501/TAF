package com.hcl.atf.taf.model.json;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.hcl.atf.taf.model.ActivityType;

public class JsonActivityType {
	private static final Log log = LogFactory.getLog(JsonActivityType.class);
	

	@JsonProperty
	private Integer activityTypeId;
	@JsonProperty
	private String activityTypeName;
	@JsonProperty
	private String description;
	@JsonProperty
	private Integer activityGroupId;
	@JsonProperty
	private String activityGroupName;

	public JsonActivityType(){	
	}
	
	public JsonActivityType(ActivityType activityType){
		
		this.activityTypeId = activityType.getActivityTypeId();
		this.activityTypeName = activityType.getActivityTypeName();
		this.description = activityType.getDescription();
		
		if (activityType.getActivityGroup() != null) {
			this.activityGroupId = activityType.getActivityGroup().getActivityGroupId();
			this.activityGroupName = activityType.getActivityGroup().getActivityGroupName();			
		}
	}
	
	
	@JsonIgnore
	public ActivityType getActivityType() {
		ActivityType activityType = new ActivityType();
		activityType.setActivityTypeId(activityTypeId);		
		activityType.setActivityTypeName(activityTypeName);
		activityType.setDescription(description);
		return activityType;
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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Integer getActivityGroupId() {
		return activityGroupId;
	}

	public void setActivityGroupId(Integer activityGroupId) {
		this.activityGroupId = activityGroupId;
	}

	public String getActivityGroupName() {
		return activityGroupName;
	}

	public void setActivityGroupName(String activityGroupName) {
		this.activityGroupName = activityGroupName;
	}
		

	

}
