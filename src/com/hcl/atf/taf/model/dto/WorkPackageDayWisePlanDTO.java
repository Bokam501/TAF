package com.hcl.atf.taf.model.dto;

import java.util.Date;

public class WorkPackageDayWisePlanDTO {
	
	private Integer workPackageId;
	private Integer shiftId;
	private String shiftName;
	private Date planDate;
	private Integer plannedTestCasesCount;

	
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
	public Date getPlanDate() {
		return planDate;
	}
	public void setPlanDate(Date planDate) {
		this.planDate = planDate;
	}
	public Integer getPlannedTestCasesCount() {
		return plannedTestCasesCount;
	}
	public void setPlannedTestCasesCount(Integer plannedTestCasesCount) {
		this.plannedTestCasesCount = plannedTestCasesCount;
	}
	public String getShiftName() {
		return shiftName;
	}
	public void setShiftName(String shiftName) {
		this.shiftName = shiftName;
	}
}
