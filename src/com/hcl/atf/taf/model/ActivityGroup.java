package com.hcl.atf.taf.model;

import static javax.persistence.GenerationType.IDENTITY;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Table(name = "activity_group_master")
public class ActivityGroup {
	
	
	private Integer activityGroupId;
	private String activityGroupName;
	private String activityGroupDescription;
	private Integer activityStatus;
	private String activityGroupDisplayName;
	private Date createdDate;
	private Date modifiedDate;
	
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "activityGroupId", unique = true, nullable = false)	
	public Integer getActivityGroupId() {
		return activityGroupId;
	}	
	public void setActivityGroupId(Integer activityGroupId) {
		this.activityGroupId = activityGroupId;
	}
	@Column(name = "activityGroupName")
	public String getActivityGroupName() {
		return activityGroupName;
	}
	@Column(name = "activityGroupDescription")
	public void setActivityGroupName(String activityGroupName) {
		this.activityGroupName = activityGroupName;
	}
	public String getActivityGroupDescription() {
		return activityGroupDescription;
	}
	
	public void setActivityGroupDescription(String activityGroupDescription) {
		this.activityGroupDescription = activityGroupDescription;
	}
	@Column(name = "activityStatus")
	public Integer getActivityStatus() {
		return activityStatus;
	}
	
	public void setActivityStatus(Integer activityStatus) {
		this.activityStatus = activityStatus;
	}
	@Column(name = "activityGroupDisplayName")
	public String getActivityGroupDisplayName() {
		return activityGroupDisplayName;
	}
	
	public void setActivityGroupDisplayName(String activityGroupDisplayName) {
		this.activityGroupDisplayName = activityGroupDisplayName;
	}
	@Column(name = "createdDate")
	public Date getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}
	@Column(name = "modifiedDate")
	public Date getModifiedDate() {
		return modifiedDate;
	}
	public void setModifiedDate(Date modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

	
}