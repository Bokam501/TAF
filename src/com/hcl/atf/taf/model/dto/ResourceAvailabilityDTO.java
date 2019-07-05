package com.hcl.atf.taf.model.dto;

import java.util.Date;

public class ResourceAvailabilityDTO {
	
	private Integer shiftTypeId;
	private Date workDate;
	private Float resourceAvailabilityCount;
	
	
	public Integer getShiftTypeId() {
		return shiftTypeId;
	}
	public void setShiftTypeId(Integer shiftTypeId) {
		this.shiftTypeId = shiftTypeId;
	}
	public Date getWorkDate() {
		return workDate;
	}
	public void setWorkDate(Date workDate) {
		this.workDate = workDate;
	}

	public Float getResourceAvailabilityCount() {
		return resourceAvailabilityCount;
	}

	public void setResourceAvailabilityCount(Float resourceAvailabilityCount) {
		this.resourceAvailabilityCount = resourceAvailabilityCount;
	}
	
	
	
	
}
