package com.hcl.atf.taf.model.dto;

import java.util.Date;

public class WorkPackageExecutionPlanUserDetails {
	
	private String loginId;
	private Integer totalAllocatedCount;
	private Date plannedExecutionDate;
	public String getLoginId() {
		return loginId;
	}
	public void setLoginId(String loginId) {
		this.loginId = loginId;
	}
	public Integer getTotalAllocatedCount() {
		return totalAllocatedCount;
	}
	public void setTotalAllocatedCount(Integer totalAllocatedCount) {
		this.totalAllocatedCount = totalAllocatedCount;
	}
	public Date getPlannedExecutionDate() {
		return plannedExecutionDate;
	}
	public void setPlannedExecutionDate(Date plannedExecutionDate) {
		this.plannedExecutionDate = plannedExecutionDate;
	}
	
		
	
}
