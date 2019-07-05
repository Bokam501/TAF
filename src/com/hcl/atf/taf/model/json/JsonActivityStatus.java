package com.hcl.atf.taf.model.json;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.hcl.atf.taf.model.ActivityStatus;
import com.hcl.atf.taf.model.StatusCategory;


public class JsonActivityStatus {

	
	@JsonProperty
	private Integer activityStatusId;
	@JsonProperty
	private String activityStatusName;
	@JsonProperty
	private String activityStatusDescription;
	@JsonProperty
	private Integer statusCategoryId;
	@JsonProperty
	private String statusCategoryName;
	@JsonProperty
	private Integer activeStatus;
	
	public JsonActivityStatus() {
	}

	public JsonActivityStatus(ActivityStatus activityStatus) {
		this.activityStatusId = activityStatus.getActivityStatusId();
		this.activityStatusName = activityStatus.getActivityStatusName();
		this.activityStatusDescription = activityStatus.getActivityStatusDescription();
		if(activityStatus.getStatusCategory() != null){
			this.statusCategoryId = activityStatus.getStatusCategory().getStatusCategoryId();
			this.statusCategoryName = activityStatus.getStatusCategory().getStatusCategoryName();
		}
		this.activeStatus = activityStatus.getActiveStatus();
	}

	public Integer getActivityStatusId() {
		return activityStatusId;
	}

	public void setActivityStatusId(Integer activityStatusId) {
		this.activityStatusId = activityStatusId;
	}

	public String getActivityStatusName() {
		return activityStatusName;
	}

	public void setActivityStatusName(String activityStatusName) {
		this.activityStatusName = activityStatusName;
	}

	public String getActivityStatusDescription() {
		return activityStatusDescription;
	}

	public void setActivityStatusDescription(String activityStatusDescription) {
		this.activityStatusDescription = activityStatusDescription;
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

	@JsonIgnore
	public ActivityStatus getActivityStatus() {
		ActivityStatus activityStatus = new ActivityStatus();
		if(this.activityStatusId != null){
			activityStatus.setActivityStatusId(this.activityStatusId);
		}
		activityStatus.setActivityStatusName(this.activityStatusName);
		activityStatus.setActivityStatusDescription(this.activityStatusDescription);
		StatusCategory statusCategory = new StatusCategory();
		if(this.statusCategoryId != null){
			statusCategory.setStatusCategoryId(this.statusCategoryId);
		}else{
			statusCategory.setStatusCategoryId(1);
		}
		activityStatus.setStatusCategory(statusCategory);
		if(this.activeStatus != null){
			activityStatus.setActiveStatus(this.activeStatus);
		}else{
			activityStatus.setActiveStatus(1);
		}
		return activityStatus;
	}
	
	
	
}
