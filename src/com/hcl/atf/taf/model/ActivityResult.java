package com.hcl.atf.taf.model;

import static javax.persistence.GenerationType.IDENTITY;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "activity_result_master")
public class ActivityResult {
	private Integer activityResultId;
	private String activityResultName;
	private String activityResultDescription;
	
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "activityResultId", unique = true, nullable = false)
	public Integer getActivityResultId() {
		return activityResultId;
	}
	public void setActivityResultId(Integer activityResultId) {
		this.activityResultId = activityResultId;
	}
	@Column(name = "activityResultName")
	public String getActivityResultName() {
		return activityResultName;
	}
	public void setActivityResultName(String activityResultName) {
		this.activityResultName = activityResultName;
	}
	@Column(name = "activityResultDescription")
	public String getActivityResultDescription() {
		return activityResultDescription;
	}
	public void setActivityResultDescription(String activityResultDescription) {
		this.activityResultDescription = activityResultDescription;
	}
	
	
}
