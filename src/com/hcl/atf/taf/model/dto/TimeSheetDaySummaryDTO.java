package com.hcl.atf.taf.model.dto;

import java.util.Date;

public class TimeSheetDaySummaryDTO {
	
	private Integer id;
	private Integer userId;
	private Date timeSheetEntryDate;
	private String morningShiftTime;
	private String nightShiftTime;
	private String graveyardShiftTime;
	private Integer resourceId;
	private Integer weekNo;
	private Integer totalHours;
	private Integer totalMins;
	private String totalTime;
	private Integer shiftId;
	private Integer shiftTypeId;
	
	public TimeSheetDaySummaryDTO(){
		
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

	public Date getTimeSheetEntryDate() {
		return timeSheetEntryDate;
	}
	public void setTimeSheetEntryDate(Date timeSheetEntryDate) {
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

	public Integer getShiftId() {
		return shiftId;
	}

	public void setShiftId(Integer shiftId) {
		this.shiftId = shiftId;
	}

	public Integer getShiftTypeId() {
		return shiftTypeId;
	}

	public void setShiftTypeId(Integer shiftTypeId) {
		this.shiftTypeId = shiftTypeId;
	}


}
