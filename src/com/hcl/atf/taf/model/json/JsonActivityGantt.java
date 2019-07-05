/**
 * 
 */
package com.hcl.atf.taf.model.json;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author silambarasur
 * 
 */
public class JsonActivityGantt {

	@JsonProperty
	private Integer id;
	@JsonProperty
	private String name;
	@JsonProperty
	private Integer activityTypeId;
	@JsonProperty
	private String activityTypeName;
	@JsonProperty
	private Integer activityWorkPackageId;
	@JsonProperty
	private String activityWorkPackageName;
	@JsonProperty
	private String from;
	@JsonProperty
	private String to;
	@JsonProperty
	private String actualStartDate;
	@JsonProperty
	private String actualEndDate;
	@JsonProperty
	private String assignee;
	@JsonProperty
	private String priority;
	@JsonProperty
	private String status;
	@JsonProperty
	private String color;
	@JsonProperty
	private String activityWorkPackageOwner;

	@JsonProperty
	private Map<String, String> progress = null;

	@JsonProperty
	private List<Map<String, String>> dependencies = null;

	public JsonActivityGantt() {

	}

	public JsonActivityGantt(JsonActivity activity) {
		this.id = activity.getActivityId();
		this.name = activity.getActivityName();
		this.activityWorkPackageId = activity.getActivityWorkPackageId();
		this.activityWorkPackageName = activity.getActivityWorkPackageName();
		this.from = activity.getFrom();
		this.to = activity.getTo();
		this.actualStartDate = activity.getPlannedEndDate();
		this.actualEndDate = activity.getPlannedEndDate();
		this.assignee = activity.getAssigneeName();
		this.priority = activity.getPriorityName();
		this.status = activity.getStatusName();
		this.activityTypeId = activity.getActivityMasterId();
		this.activityTypeName = activity.getActivityMasterName();
		this.color = activity.getProgressIndicator();
		this.activityWorkPackageOwner = activity.getActivityWorkPackageOwner();
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getActivityWorkPackageId() {
		return activityWorkPackageId;
	}

	public void setActivityWorkPackageId(Integer activityWorkPackageId) {
		this.activityWorkPackageId = activityWorkPackageId;
	}

	public String getActivityWorkPackageName() {
		return activityWorkPackageName;
	}

	public void setActivityWorkPackageName(String activityWorkPackageName) {
		this.activityWorkPackageName = activityWorkPackageName;
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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public String getTo() {
		return to;
	}

	public void setTo(String to) {
		this.to = to;
	}

	public String getAssignee() {
		return assignee;
	}

	public void setAssignee(String assignee) {
		this.assignee = assignee;
	}

	public String getPriority() {
		return priority;
	}

	public void setPriority(String priority) {
		this.priority = priority;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Map<String, String> getProgress() {
		return progress;
	}

	public void setProgress(Map<String, String> progress) {
		this.progress = progress;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public String getActivityWorkPackageOwner() {
		return activityWorkPackageOwner;
	}

	public void setActivityWorkPackageOwner(String activityWorkPackageOwner) {
		this.activityWorkPackageOwner = activityWorkPackageOwner;
	}

	public List<Map<String, String>> getDependencies() {
		return dependencies;
	}

	public void setDependencies(List<Map<String, String>> dependencies) {
		this.dependencies = dependencies;
	}

}
