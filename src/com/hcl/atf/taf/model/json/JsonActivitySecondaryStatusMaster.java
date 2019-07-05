package com.hcl.atf.taf.model.json;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.hcl.atf.taf.model.ActivitySecondaryStatusMaster;

public class JsonActivitySecondaryStatusMaster {
	@JsonProperty
	private Integer activitySecondaryStatusId;
	@JsonProperty
	private String activitySecondaryStatusName;
	@JsonProperty
	private String description;
	@JsonProperty
	private Integer activeStatus;
	
	public JsonActivitySecondaryStatusMaster() {
	}
	
	public JsonActivitySecondaryStatusMaster(ActivitySecondaryStatusMaster activitySecondaryStatusMaster) {
		this.activitySecondaryStatusId = activitySecondaryStatusMaster.getActivitySecondaryStatusId();
		this.activitySecondaryStatusName = activitySecondaryStatusMaster.getActivitySecondaryStatusName();
		this.description = activitySecondaryStatusMaster.getDescription();
		this.activeStatus = activitySecondaryStatusMaster.getActiveStatus();
	}

	public Integer getActivitySecondaryStatusId() {
		return activitySecondaryStatusId;
	}

	public void setActivitySecondaryStatusId(Integer activitySecondaryStatusId) {
		this.activitySecondaryStatusId = activitySecondaryStatusId;
	}

	public String getActivitySecondaryStatusName() {
		return activitySecondaryStatusName;
	}

	public void setActivitySecondaryStatusName(String activitySecondaryStatusName) {
		this.activitySecondaryStatusName = activitySecondaryStatusName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	public Integer getActiveStatus() {
		return activeStatus;
	}

	public void setActiveStatus(Integer activeStatus) {
		this.activeStatus = activeStatus;
	}
	
	@JsonIgnore
	public ActivitySecondaryStatusMaster getActivitySecondaryStatusMaster() {
		ActivitySecondaryStatusMaster activitySecondaryStatusMaster = new ActivitySecondaryStatusMaster();
		if(this.activitySecondaryStatusId != null){
			activitySecondaryStatusMaster.setActivitySecondaryStatusId(this.activitySecondaryStatusId);
		}
		activitySecondaryStatusMaster.setActivitySecondaryStatusName(this.activitySecondaryStatusName);
		activitySecondaryStatusMaster.setDescription(this.description);
		if(this.activeStatus != null){
			activitySecondaryStatusMaster.setActiveStatus(this.activeStatus);
		}else{
			activitySecondaryStatusMaster.setActiveStatus(1);
		}
		return activitySecondaryStatusMaster;
	}
	
}
