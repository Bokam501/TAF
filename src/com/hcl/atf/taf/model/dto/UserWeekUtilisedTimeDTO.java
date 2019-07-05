package com.hcl.atf.taf.model.dto;

import java.util.Date;

public class UserWeekUtilisedTimeDTO {
	private Integer id;
	private Integer userId;
	private Integer workPackageId;
	private Integer shiftId;
	private Date startDate;
	private Date endDate;
	private Integer timeSheetTotalHours;
	private Integer timeSheetTotalMins;
	private String timeSheetDuration;
	private String shiftBookingDuration;
	private Integer shiftBookingCount;
	
	public UserWeekUtilisedTimeDTO(){
		
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

	public Integer getWorkPackageId() {
		return workPackageId;
	}

	public void setWorkPackageId(Integer workPackageId) {
		this.workPackageId = workPackageId;
	}

	public Integer getShiftId() {
		return shiftId;
	}

	public void setShiftId(Integer shiftId) {
		this.shiftId = shiftId;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public Integer getTimeSheetTotalHours() {
		return timeSheetTotalHours;
	}

	public void setTimeSheetTotalHours(Integer timeSheetTotalHours) {
		this.timeSheetTotalHours = timeSheetTotalHours;
	}

	public Integer getTimeSheetTotalMins() {
		return timeSheetTotalMins;
	}

	public void setTimeSheetTotalMins(Integer timeSheetTotalMins) {
		this.timeSheetTotalMins = timeSheetTotalMins;
	}

	public String getTimeSheetDuration() {
		return timeSheetDuration;
	}

	public void setTimeSheetDuration(String timeSheetDuration) {
		this.timeSheetDuration = timeSheetDuration;
	}

	public String getShiftBookingDuration() {
		return shiftBookingDuration;
	}

	public void setShiftBookingDuration(String shiftBookingDuration) {
		this.shiftBookingDuration = shiftBookingDuration;
	}

	public Integer getShiftBookingCount() {
		return shiftBookingCount;
	}

	public void setShiftBookingCount(Integer shiftBookingCount) {
		this.shiftBookingCount = shiftBookingCount;
	}
	
	@Override
	public boolean equals(Object o) {
		UserWeekUtilisedTimeDTO userDTO = (UserWeekUtilisedTimeDTO) o;
		if (this.userId == userDTO.getUserId()) {
			return true;
		}else{
			return false;
		}
	}
	
	@Override
	public int hashCode(){
	    return (int) userId;
	  }
	
}
