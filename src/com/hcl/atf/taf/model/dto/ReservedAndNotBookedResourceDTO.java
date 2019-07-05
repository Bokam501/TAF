package com.hcl.atf.taf.model.dto;

import java.util.Date;

public class ReservedAndNotBookedResourceDTO {
	private Date workDate;
	private Integer resourcePoolId;		
	private String 	resourcePoolName;
	private Integer shiftTypeId;
	private Integer availableNotBookedCount;
	private Integer notAvailableAndNotBookedCount;
	
	public Date getWorkDate() {
		return workDate;
	}
	public void setWorkDate(Date workDate) {
		this.workDate = workDate;
	}
	public Integer getResourcePoolId() {
		return resourcePoolId;
	}
	public void setResourcePoolId(Integer resourcePoolId) {
		this.resourcePoolId = resourcePoolId;
	}
	public String getResourcePoolName() {
		return resourcePoolName;
	}
	public void setResourcePoolName(String resourcePoolName) {
		this.resourcePoolName = resourcePoolName;
	}
	public Integer getShiftTypeId() {
		return shiftTypeId;
	}
	public void setShiftTypeId(Integer shiftTypeId) {
		this.shiftTypeId = shiftTypeId;
	}
	public Integer getAvailableNotBookedCount() {
		return availableNotBookedCount;
	}
	public void setAvailableNotBookedCount(Integer availableNotBookedCount) {
		this.availableNotBookedCount = availableNotBookedCount;
	}
	public Integer getNotAvailableAndNotBookedCount() {
		return notAvailableAndNotBookedCount;
	}
	public void setNotAvailableAndNotBookedCount(
			Integer notAvailableAndNotBookedCount) {
		this.notAvailableAndNotBookedCount = notAvailableAndNotBookedCount;
	}
	
	
}
