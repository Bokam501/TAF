package com.hcl.atf.taf.model.dto;

import java.util.Date;

public class WorkPackageResultsStatisticsDTO {
	
	private Integer workPackageId;
	private String workPackageName;
	private Integer shiftId;
	private String shiftName;
	private Date actualExecutionDate;
	private Integer testLeadId;
	private String testLeadName;
	private Integer testerId;
	private String testerName;
	private Integer testCasesCount;
	private Integer defectsCount;
	
	public String getShiftName() {
		return shiftName;
	}

	public void setShiftName(String shiftName) {
		this.shiftName = shiftName;
	}

	public Integer getWorkPackageId() {
		return this.workPackageId;
	}

	public void setWorkPackageId(Integer workPackageId) {
		this.workPackageId = workPackageId;
	}
	
	public String getWorkPackageName() {
		return workPackageName;
	}

	public void setWorkPackageName(String workPackageName) {
		this.workPackageName = workPackageName;
	}

	public Integer getShiftId() {
		return this.shiftId;
	}

	public void setShiftId(Integer shiftId) {
		this.shiftId = shiftId;
	}
	
	public Date getActualExecutionDate() {
		return actualExecutionDate;
	}
	
	public void setActualExecutionDate(Date actualExecutionDate) {
		this.actualExecutionDate = actualExecutionDate;
	}
	
	public Integer getTestLeadId() {
		return this.testLeadId;
	}

	public void setTestLeadId(Integer testLeadId) {
		this.testLeadId = testLeadId;
	}

	public String getTestLeadName() {
		return testLeadName;
	}

	public void setTestLeadName(String testLeadName) {
		this.testLeadName = testLeadName;
	}

	public Integer getTesterId() {
		return this.testerId;
	}

	public void setTesterId(Integer testerId) {
		this.testerId = testerId;
	}

	public String getTesterName() {
		return testerName;
	}

	public void setTesterName(String testerName) {
		this.testerName = testerName;
	}

	public Integer getDefectsCount() {
		return this.defectsCount;
	}

	public void setDefectsCount(Integer defectsCount) {
		this.defectsCount = defectsCount;
	}
	
	public Integer getTestCasesCount() {
		return this.testCasesCount;
	}

	public void setTestCasesCount(Integer testCasesCount) {
		this.testCasesCount = testCasesCount;
	}
}
