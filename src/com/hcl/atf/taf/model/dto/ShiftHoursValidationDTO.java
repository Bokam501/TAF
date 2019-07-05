package com.hcl.atf.taf.model.dto;


public class ShiftHoursValidationDTO {	
	
	private String exceededShiftHours;
	private String exceededTotalHours;	
	private String hoursWithinLimit;
	private String exceededResourceShiftCheckInHours;
	private String errorMsg;
	public String getExceededShiftHours() {
		return exceededShiftHours;
	}
	public void setExceededShiftHours(String exceededShiftHours) {
		this.exceededShiftHours = exceededShiftHours;
	}
	public String getExceededTotalHours() {
		return exceededTotalHours;
	}
	public void setExceededTotalHours(String exceededTotalHours) {
		this.exceededTotalHours = exceededTotalHours;
	}
	public String getHoursWithinLimit() {
		return hoursWithinLimit;
	}
	public void setHoursWithinLimit(String hoursWithinLimit) {
		this.hoursWithinLimit = hoursWithinLimit;
	}
	public String getErrorMsg() {
		return errorMsg;
	}
	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}
	public String getExceededResourceShiftCheckInHours() {
		return exceededResourceShiftCheckInHours;
	}
	public void setExceededResourceShiftCheckInHours(
			String exceededResourceShiftCheckInHours) {
		this.exceededResourceShiftCheckInHours = exceededResourceShiftCheckInHours;
	}
	
	
}
