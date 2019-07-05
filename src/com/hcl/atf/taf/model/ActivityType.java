package com.hcl.atf.taf.model;

import static javax.persistence.GenerationType.IDENTITY;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "activity_type")
public class ActivityType {

	private Integer activityTypeId;
	private String activityTypeName;
	private String description;
	private ActivityGroup activityGroup;
	
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "activityTypeId",unique = true, nullable = false)	
	public Integer getActivityTypeId() {
		return activityTypeId;
	}
	public void setActivityTypeId(Integer activityTypeId) {
		this.activityTypeId = activityTypeId;
	}	
	@Column(name = "activityTypeName")
	public String getActivityTypeName() {
		return activityTypeName;
	}
	public void setActivityTypeName(String activityTypeName) {
		this.activityTypeName = activityTypeName;
	}
	@Column(name = "description")
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "activityGroupId")
	public ActivityGroup getActivityGroup() {
		return activityGroup;
	}
	public void setActivityGroup(ActivityGroup activityGroup) {
		this.activityGroup = activityGroup;
	}
	
}
