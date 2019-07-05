package com.hcl.atf.taf.model.json;

import com.fasterxml.jackson.annotation.JsonProperty;

public class JsonTimeSheetDaySummary {
	@JsonProperty	
	private Integer id;
	@JsonProperty
	private Integer userId;
	@JsonProperty
	private String timeSheetEntryDate;
	@JsonProperty
	private String morningShiftTime;
	@JsonProperty
	private String nightShiftTime;
	@JsonProperty
	private String graveyardShiftTime;
	@JsonProperty
	private Integer resourceId;
	@JsonProperty
	private Integer weekNo;
	@JsonProperty
	private Integer totalHours;
	@JsonProperty
	private Integer totalMins;
	@JsonProperty
	private String totalTime;
	@JsonProperty
	private Integer morningShiftHours;
	@JsonProperty
	private Integer morningShiftMins;
	@JsonProperty
	private Integer nightShiftHours;
	@JsonProperty
	private Integer nightShiftMins;
	@JsonProperty
	private Integer graveyardShiftHours;
	@JsonProperty
	private Integer graveyardShiftMins;
	
	public JsonTimeSheetDaySummary(){
		morningShiftTime = "";
		nightShiftTime = "";
		graveyardShiftTime = "";
		totalTime = "";
		morningShiftHours = 0;
		morningShiftMins = 0;
		nightShiftHours = 0;
		nightShiftMins = 0;
		graveyardShiftHours = 0;
		graveyardShiftMins = 0;
		totalHours = 0;
		totalMins = 0;
		
	}
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	public String getTimeSheetEntryDate() {
		return timeSheetEntryDate;
	}
	public void setTimeSheetEntryDate(String timeSheetEntryDate) {
		this.timeSheetEntryDate = timeSheetEntryDate;
	}
	public String getMorningShiftTime() {
		return morningShiftTime;
	}
	public void setMorningShiftTime(String morningShiftTime) {
		this.morningShiftTime = morningShiftTime;
	}
	public String getNightShiftTime() {
		return nightShiftTime;
	}
	public void setNightShiftTime(String nightShiftTime) {
		this.nightShiftTime = nightShiftTime;
	}
	public String getGraveyardShiftTime() {
		return graveyardShiftTime;
	}
	public void setGraveyardShiftTime(String graveyardShiftTime) {
		this.graveyardShiftTime = graveyardShiftTime;
	}
	public Integer getResourceId() {
		return resourceId;
	}
	public void setResourceId(Integer resourceId) {
		this.resourceId = resourceId;
	}
	public Integer getWeekNo() {
		return weekNo;
	}
	public void setWeekNo(Integer weekNo) {
		this.weekNo = weekNo;
	}
	public Integer getTotalHours() {
		return totalHours;
	}

	public void setTotalHours(Integer totalHours) {
		this.totalHours = totalHours;
	}

	public Integer getTotalMins() {
		return totalMins;
	}

	public void setTotalMins(Integer totalMins) {
		this.totalMins = totalMins;
	}
	public String getTotalTime() {
		return totalTime;
	}
	public void setTotalTime(String totalTime) {
		this.totalTime = totalTime;
	}

	public Integer getMorningShiftHours() {
		return morningShiftHours;
	}

	public void setMorningShiftHours(Integer morningShiftHours) {
		this.morningShiftHours = morningShiftHours;
	}

	public Integer getMorningShiftMins() {
		return morningShiftMins;
	}

	public void setMorningShiftMins(Integer morningShiftMins) {
		this.morningShiftMins = morningShiftMins;
	}

	public Integer getNightShiftHours() {
		return nightShiftHours;
	}

	public void setNightShiftHours(Integer nightShiftHours) {
		this.nightShiftHours = nightShiftHours;
	}

	public Integer getNightShiftMins() {
		return nightShiftMins;
	}

	public void setNightShiftMins(Integer nightShiftMins) {
		this.nightShiftMins = nightShiftMins;
	}

	public Integer getGraveyardShiftHours() {
		return graveyardShiftHours;
	}

	public void setGraveyardShiftHours(Integer graveyardShiftHours) {
		this.graveyardShiftHours = graveyardShiftHours;
	}

	public Integer getGraveyardShiftMins() {
		return graveyardShiftMins;
	}

	public void setGraveyardShiftMins(Integer graveyardShiftMins) {
		this.graveyardShiftMins = graveyardShiftMins;
	}
}
