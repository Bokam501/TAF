package com.hcl.atf.taf.model.json;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.hcl.atf.taf.controller.utilities.DateUtility;
import com.hcl.atf.taf.model.ActivityEffortTracker;
import com.hcl.atf.taf.model.ActivityStatus;
import com.hcl.atf.taf.model.ActivityTask;
import com.hcl.atf.taf.model.UserList;

public class JsonActivityEffortTracker {
	private static final Log log = LogFactory.getLog(JsonActivity.class);

	
	@JsonProperty
	private Integer activityEffortTrackerId;
	@JsonProperty	
	private Integer entityId;
	@JsonProperty	
	private String entityName;
	@JsonProperty
	private Integer entityTypeId;
	@JsonProperty
	private String entityTypeName;
	@JsonProperty
	private Integer modifiedById;
	@JsonProperty
	private String modifiedByName;
	@JsonProperty
	private Integer currentStatusId;
	@JsonProperty
	private String currentStatusName;
	@JsonProperty
	private Integer targetStatusId;
	@JsonProperty
	private String targetStatusName;
	@JsonProperty
	private String lastUpdatedDate;
	@JsonProperty
	private String plannedStartDate;
	@JsonProperty
	private String comments;
	
	

	@JsonProperty private String plannedEndDate;
	@JsonProperty private String actualStartDate;
	@JsonProperty private String actualEndDate;
	@JsonProperty private Integer plannedEffort;
	@JsonProperty private Integer actualEffort;
	@JsonProperty private Integer entityGroupId;
	
	
	public JsonActivityEffortTracker() {
	}
	
	public JsonActivityEffortTracker(ActivityEffortTracker activityEffortTracker) {
		this.activityEffortTrackerId = activityEffortTracker.getActivityEffortTrackerId();
	    this.lastUpdatedDate = DateUtility.dateformatWithOutTime(activityEffortTracker.getLastUpdatedDate());
	    this.actualEffort = activityEffortTracker.getActualEffort();
	    this.plannedEffort = activityEffortTracker.getPlannedEffort();
	    this.comments = activityEffortTracker.getComments();
	    
	    if (activityEffortTracker.getEntityType() != null) {
			this.entityTypeId = activityEffortTracker.getEntityType().getEntityId();
			this.entityTypeName = activityEffortTracker.getEntityType().getEntityName();
		}
		if (activityEffortTracker.getEntityType() != null) {			
			this.entityId = activityEffortTracker.getEntity().getActivityTaskId();
			this.entityName = activityEffortTracker.getEntity().getActivityTaskName();
		}
		if (activityEffortTracker.getCurrentStatus() != null) {			
			this.currentStatusId = activityEffortTracker.getCurrentStatus().getActivityStatusId();
			this.currentStatusName = activityEffortTracker.getCurrentStatus().getActivityStatusName();
		}
		if (activityEffortTracker.getTargetStatus() != null) {			
			this.targetStatusId = activityEffortTracker.getTargetStatus().getActivityStatusId();
			this.targetStatusName = activityEffortTracker.getTargetStatus().getActivityStatusName();
		}
		if (activityEffortTracker.getModifiedBy() != null) {			
			this.modifiedById = activityEffortTracker.getModifiedBy().getUserId();
			this.modifiedByName = activityEffortTracker.getModifiedBy().getLoginId();
		}
		
	}
	@JsonIgnore
	public ActivityEffortTracker getActivityEffortTracker() {
		ActivityEffortTracker activityEffortTracker = new ActivityEffortTracker();
		activityEffortTracker.setActivityEffortTrackerId(activityEffortTrackerId);
		activityEffortTracker.setPlannedEffort(plannedEffort);
		activityEffortTracker.setActualEffort(actualEffort);
		activityEffortTracker.setComments(comments);
		
		if (this.entityId != null) {
			ActivityTask activityTask = new ActivityTask();			
			activityTask.setActivityTaskId(entityTypeId);
			activityTask.setActivityTaskName(entityTypeName);
			activityEffortTracker.setEntity(activityTask);
		}
		if (this.currentStatusId != null) {
			ActivityStatus activityStatus = new ActivityStatus();
			activityStatus.setActivityStatusId(currentStatusId);	
			activityStatus.setActivityStatusName(currentStatusName);	
			activityEffortTracker.setCurrentStatus(activityStatus);
			
		}
		if (this.targetStatusId != null) {
			ActivityStatus activityStatus = new ActivityStatus();
			activityStatus.setActivityStatusId(targetStatusId);	
			activityStatus.setActivityStatusName(targetStatusName);	
			activityEffortTracker.setTargetStatus(activityStatus);
			
		}
		if (this.modifiedById != null) {
			UserList userList = new UserList();
			userList.setUserId(modifiedById);
			userList.setLoginId(modifiedByName);
			activityEffortTracker.setModifiedBy(userList);
		}
		activityEffortTracker.setLastUpdatedDate(DateUtility.getCurrentTime());
		activityEffortTracker.setActualStartDate(DateUtility.getCurrentTime());
		activityEffortTracker.setActualEndDate(DateUtility.getCurrentTime());
		activityEffortTracker.setPlannedStartDate(DateUtility.getCurrentTime());
		activityEffortTracker.setPlannedEndDate(DateUtility.getCurrentTime());
		
		return activityEffortTracker;
	    }

	
	public Integer getActivityEffortTrackerId() {
		return activityEffortTrackerId;
	}
	public void setActivityEffortTrackerId(Integer activityEffortTrackerId) {
		this.activityEffortTrackerId = activityEffortTrackerId;
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
	public Integer getEntityTypeId() {
		return entityTypeId;
	}
	public void setEntityTypeId(Integer entityTypeId) {
		this.entityTypeId = entityTypeId;
	}
	public String getEntityTypeName() {
		return entityTypeName;
	}
	public void setEntityTypeName(String entityTypeName) {
		this.entityTypeName = entityTypeName;
	}
	
	public Integer getCurrentStatusId() {
		return currentStatusId;
	}
	public void setCurrentStatusId(Integer currentStatusId) {
		this.currentStatusId = currentStatusId;
	}
	public String getCurrentStatusName() {
		return currentStatusName;
	}
	public void setCurrentStatusName(String currentStatusName) {
		this.currentStatusName = currentStatusName;
	}
	public Integer getTargetStatusId() {
		return targetStatusId;
	}
	public void setTargetStatusId(Integer targetStatusId) {
		this.targetStatusId = targetStatusId;
	}
	public String getTargetStatusName() {
		return targetStatusName;
	}
	public void setTargetStatusName(String targetStatusName) {
		this.targetStatusName = targetStatusName;
	}
	public String getLastUpdatedDate() {
		return lastUpdatedDate;
	}
	public void setLastUpdatedDate(String lastUpdatedDate) {
		this.lastUpdatedDate = lastUpdatedDate;
	}

	public Integer getModifiedById() {
		return modifiedById;
	}

	public void setModifiedById(Integer modifiedById) {
		this.modifiedById = modifiedById;
	}

	public String getModifiedByName() {
		return modifiedByName;
	}

	public void setModifiedByName(String modifiedByName) {
		this.modifiedByName = modifiedByName;
	}

	public String getPlannedStartDate() {
		return plannedStartDate;
	}

	public void setPlannedStartDate(String plannedStartDate) {
		this.plannedStartDate = plannedStartDate;
	}

	public String getPlannedEndDate() {
		return plannedEndDate;
	}

	public void setPlannedEndDate(String plannedEndDate) {
		this.plannedEndDate = plannedEndDate;
	}

	public String getActualStartDate() {
		return actualStartDate;
	}

	public void setActualStartDate(String actualStartDate) {
		this.actualStartDate = actualStartDate;
	}

	public String getActualEndDate() {
		return actualEndDate;
	}

	public void setActualEndDate(String actualEndDate) {
		this.actualEndDate = actualEndDate;
	}

	public Integer getPlannedEffort() {
		return plannedEffort;
	}

	public void setPlannedEffort(Integer plannedEffort) {
		this.plannedEffort = plannedEffort;
	}

	public Integer getActualEffort() {
		return actualEffort;
	}

	public void setActualEffort(Integer actualEffort) {
		this.actualEffort = actualEffort;
	}

	public Integer getEntityGroupId() {
		return entityGroupId;
	}

	public void setEntityGroupId(Integer entityGroupId) {
		this.entityGroupId = entityGroupId;
	}	

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}
	
}