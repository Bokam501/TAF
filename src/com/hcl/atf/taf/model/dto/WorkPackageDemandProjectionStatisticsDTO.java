package com.hcl.atf.taf.model.dto;

import java.util.Date;

public class WorkPackageDemandProjectionStatisticsDTO {
	private Integer wpDemandProjectionId;
	private Date workDate;
	private Float resourceCount;	
	
	private Integer workPackageId;
	private String workPackageName;
		
	private Integer shiftId;	
	private String shiftName;
	
	private Integer skillId;	
	private String skillName;	
	
	private Integer userRoleId;
	private String roleName;
	
	public Integer getWpDemandProjectionId() {
		return wpDemandProjectionId;
	}
	public void setWpDemandProjectionId(Integer wpDemandProjectionId) {
		this.wpDemandProjectionId = wpDemandProjectionId;
	}
	public Date getWorkDate() {
		return workDate;
	}
	public void setWorkDate(Date workDate) {
		this.workDate = workDate;
	}
	public Float getResourceCount() {
		return resourceCount;
	}
	public void setResourceCount(Float resourceCount) {
		this.resourceCount = resourceCount;
	}
	public Integer getWorkPackageId() {
		return workPackageId;
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
		return shiftId;
	}
	public void setShiftId(Integer shiftId) {
		this.shiftId = shiftId;
	}
	public String getShiftName() {
		return shiftName;
	}
	public void setShiftName(String shiftName) {
		this.shiftName = shiftName;
	}
	public Integer getSkillId() {
		return skillId;
	}
	public void setSkillId(Integer skillId) {
		this.skillId = skillId;
	}
	public String getSkillName() {
		return skillName;
	}
	public void setSkillName(String skillName) {
		this.skillName = skillName;
	}
	public Integer getUserRoleId() {
		return userRoleId;
	}
	public void setUserRoleId(Integer userRoleId) {
		this.userRoleId = userRoleId;
	}
	public String getRoleName() {
		return roleName;
	}
	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}
	
		
}
