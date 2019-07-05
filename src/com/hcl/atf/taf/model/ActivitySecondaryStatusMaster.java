package com.hcl.atf.taf.model;

import static javax.persistence.GenerationType.IDENTITY;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "activity_secondary_status_master")
public class ActivitySecondaryStatusMaster {

	private Integer activitySecondaryStatusId;	
	private String activitySecondaryStatusName;
	private String description;
	private DimensionMaster dimensionId;
	private Integer activeStatus;
	private Set<ActivityStatus> activityStatus = new HashSet<ActivityStatus>(0);
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "activitySecondaryStatusId",unique = true, nullable = false)
	public Integer getActivitySecondaryStatusId() {
		return activitySecondaryStatusId;
	}
	public void setActivitySecondaryStatusId(Integer activitySecondaryStatusId) {
		this.activitySecondaryStatusId = activitySecondaryStatusId;
	}
	@Column(name = "activitySecondaryStatusName")
	public String getActivitySecondaryStatusName() {
		return activitySecondaryStatusName;
	}
	public void setActivitySecondaryStatusName(String activitySecondaryStatusName) {
		this.activitySecondaryStatusName = activitySecondaryStatusName;
	}
	@Column(name = "description")
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}

    @ManyToMany(fetch = FetchType.LAZY,cascade=CascadeType.ALL)
	@JoinTable(name = "activitystatus_has_secondarystatus", joinColumns = { @JoinColumn(name = "activityStatusId", nullable = false, updatable = false) }, inverseJoinColumns = { @JoinColumn(name = "activitySecondaryStatusId", nullable = false, updatable = false) })

	public Set<ActivityStatus> getActivityStatus() {
		return activityStatus;
	}
	public void setActivityStatus(Set<ActivityStatus> activityStatus) {
		this.activityStatus = activityStatus;
	}	

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "dimensionId")
	public DimensionMaster getDimensionId() {
		return dimensionId;
	}
	public void setDimensionId(DimensionMaster dimensionId) {
		this.dimensionId = dimensionId;
	}
	
	@Column(name = "activeStatus")
	public Integer getActiveStatus() {
		return activeStatus;
	}
	public void setActiveStatus(Integer activeStatus) {
		this.activeStatus = activeStatus;
	}
}
