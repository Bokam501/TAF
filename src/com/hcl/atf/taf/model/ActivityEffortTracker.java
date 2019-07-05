package com.hcl.atf.taf.model;

import static javax.persistence.GenerationType.IDENTITY;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "activity_effort_tracker")
public class ActivityEffortTracker {

	
	private Integer activityEffortTrackerId;
	private ActivityTask entity;
	private ActivityEntityMaster entityType;
	private ActivityStatus currentStatus;
	private ActivityStatus targetStatus;
	private UserList modifiedBy;
	private Date lastUpdatedDate;
	private Date plannedStartDate;
	private Date plannedEndDate;
	private Date actualStartDate;
	private Date actualEndDate;
	private Integer plannedEffort;
	private Integer actualEffort;
	private Integer entityGroupId;
	private String comments;
	
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "activityEffortTrackerId",unique = true, nullable = false)
	public Integer getActivityEffortTrackerId() {
		return activityEffortTrackerId;
	}
	public void setActivityEffortTrackerId(Integer activityEffortTrackerId) {
		this.activityEffortTrackerId = activityEffortTrackerId;
	}
	@ManyToOne (fetch = FetchType.LAZY)
	@JoinColumn (name = "entityId")
	public ActivityTask getEntity() {
		return entity;
	}
	public void setEntity(ActivityTask entity) {
		this.entity = entity;
	}
	@ManyToOne (fetch = FetchType.LAZY)
	@JoinColumn (name = "entityTypeId")
	public ActivityEntityMaster getEntityType() {
		return entityType;
	}
	public void setEntityType(ActivityEntityMaster entityType) {
		this.entityType = entityType;
	}
	@ManyToOne (fetch = FetchType.LAZY)
	@JoinColumn (name = "currentStatusId")			
	public ActivityStatus getCurrentStatus() {
		return currentStatus;
	}
	
	public void setCurrentStatus(ActivityStatus currentStatus) {
		this.currentStatus = currentStatus;
	}
	@ManyToOne (fetch = FetchType.LAZY)
	@JoinColumn (name = "targetStatusId")
	public ActivityStatus getTargetStatus() {
		return targetStatus;
	}
	public void setTargetStatus(ActivityStatus targetStatus) {
		this.targetStatus = targetStatus;
	}
	@ManyToOne (fetch = FetchType.LAZY)
	@JoinColumn (name = "modifiedBy")
	public UserList getModifiedBy() {
		return modifiedBy;
	}
	public void setModifiedBy(UserList modifiedBy) {
		this.modifiedBy = modifiedBy;
	}	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "lastUpdatedDate")
	public void setLastUpdatedDate(Date lastUpdatedDate) {
		this.lastUpdatedDate = lastUpdatedDate;
	}
	public Date getLastUpdatedDate() {
		return lastUpdatedDate;
	}
    @Column(name = "plannedStartDate")
	public Date getPlannedStartDate() {
		return plannedStartDate;
	}
	public void setPlannedStartDate(Date plannedStartDate) {
		this.plannedStartDate = plannedStartDate;
	}
	@Column(name = "plannedEndDate")
	public Date getPlannedEndDate() {
		return plannedEndDate;
	}
	public void setPlannedEndDate(Date plannedEndDate) {
		this.plannedEndDate = plannedEndDate;
	}
	@Column(name = "actualStartDate")
	public Date getActualStartDate() {
		return actualStartDate;
	}
	public void setActualStartDate(Date actualStartDate) {
		this.actualStartDate = actualStartDate;
	}
	@Column(name = "actualEndDate")
	public Date getActualEndDate() {
		return actualEndDate;
	}
	public void setActualEndDate(Date actualEndDate) {
		this.actualEndDate = actualEndDate;
	}
	@Column(name = "plannedEffort")
	public Integer getPlannedEffort() {
		return plannedEffort;
	}
	public void setPlannedEffort(Integer plannedEffort) {
		this.plannedEffort = plannedEffort;
	}
	@Column(name = "actualEffort")
	public Integer getActualEffort() {
		return actualEffort;
	}
	public void setActualEffort(Integer actualEffort) {
		this.actualEffort = actualEffort;
	}
	@Column(name = "entityGroupId")
	public Integer getEntityGroupId() {
		return entityGroupId;
	}
	public void setEntityGroupId(Integer entityGroupId) {
		this.entityGroupId = entityGroupId;
	}
	@Column(name = "comments")
	public String getComments() {
		return comments;
	}
	public void setComments(String comments) {
		this.comments = comments;
	}

}
