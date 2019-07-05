package com.hcl.atf.taf.model.json;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.hcl.atf.taf.model.ActivityGroup;


public class JsonActivityGroup {
	private static final Log log = LogFactory.getLog(JsonActivityGroup.class);
	
	@JsonProperty
	private Integer activityGroupId;
	@JsonProperty
	private String activityGroupName;
	@JsonProperty
	private String activityGroupDescription;
	@JsonProperty
	private Integer activityStatus;
	@JsonProperty
	private String activityGroupDisplayName;


	public JsonActivityGroup(){	
	}
	
	
	public JsonActivityGroup(ActivityGroup activityGroup) {
		this.activityGroupId = activityGroup.getActivityGroupId();
		this.activityGroupName = activityGroup.getActivityGroupName();	
		this.activityGroupDescription= activityGroup.getActivityGroupDescription();
		this.activityStatus = activityGroup.getActivityStatus();
		this.activityGroupDisplayName = activityGroup.getActivityGroupDisplayName();
	}
	
	@JsonIgnore
	public ActivityGroup getActivityGroup() {
		ActivityGroup activityGroup = new ActivityGroup();
		activityGroup.setActivityGroupId(activityGroupId);
		activityGroup.setActivityGroupName(activityGroupName);
		activityGroup.setActivityGroupDescription(activityGroupDescription);
		activityGroup.setActivityStatus(activityStatus);
		activityGroup.setActivityGroupDisplayName(activityGroupDisplayName);
		return activityGroup;
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


	public String getActivityGroupDescription() {
		return activityGroupDescription;
	}


	public void setActivityGroupDescription(String activityGroupDescription) {
		this.activityGroupDescription = activityGroupDescription;
	}


	public Integer getActivityStatus() {
		return activityStatus;
	}


	public void setActivityStatus(Integer activityStatus) {
		this.activityStatus = activityStatus;
	}


	public String getActivityGroupDisplayName() {
		return activityGroupDisplayName;
	}


	public void setActivityGroupDisplayName(String activityGroupDisplayName) {
		this.activityGroupDisplayName = activityGroupDisplayName;
	}

	

	
}
