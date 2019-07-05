package com.hcl.atf.taf.model.dto;

import java.util.Date;

public class TimeSheetResourceCheckinDTO {
	private Integer id;	
	private Date timeSheetEntryDate;
	private String morningShiftTime;
	private String nightShiftTime;
	private String graveyardShiftTime;
	private Integer resourceId;
	private Integer weekNo;
	private Integer totalHours;
	private Integer totalMins;
	private String totalTime;	
	private Integer shiftTypeId;	
	private String shiftName;
	private Integer timeSheetEntryId;	
	private Integer actualShiftId;
	private Integer hours;	
	private Integer mins;	
	private Integer workShiftId;			
	private String workShiftName;	
	private Integer workPackageId;	
	private String workPackageName;	
	private Integer productId;		
	private String productName;	
	private String comments;	
	private Integer userId;		
	private String userName;	
	private Integer roleId;		
	private String roleName;	
	private Integer isApproved;	
	private String approvalComments;	
	private String approvedDate;	
	private Integer approverId;	
	private String approverName;	
	private Integer status;	
	private String createdDate;	
	private String modifiedDate;	
	private Integer activityTypeId;		
	private String activityTypeName;	
	private Integer resourceShiftCheckInId;	
	private String resourceShiftCheckInDisplayName;
	private Date resourceCheckInDate;
	
	public TimeSheetResourceCheckinDTO(){
		
	}
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
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

	public Integer getShiftTypeId() {
		return shiftTypeId;
	}

	public void setShiftTypeId(Integer shiftTypeId) {
		this.shiftTypeId = shiftTypeId;
	}

	public String getShiftName() {
		return shiftName;
	}
	public void setShiftName(String shiftName) {
		this.shiftName = shiftName;
	}
	public Integer getTimeSheetEntryId() {
		return timeSheetEntryId;
	}

	public Integer getActualShiftId() {
		return actualShiftId;
	}
	public void setActualShiftId(Integer actualShiftId) {
		this.actualShiftId = actualShiftId;
	}
	public void setTimeSheetEntryId(Integer timeSheetEntryId) {
		this.timeSheetEntryId = timeSheetEntryId;
	}

	public Integer getHours() {
		return hours;
	}

	public void setHours(Integer hours) {
		this.hours = hours;
	}

	public Integer getMins() {
		return mins;
	}

	public void setMins(Integer mins) {
		this.mins = mins;
	}

	

	public Integer getWorkShiftId() {
		return workShiftId;
	}
	public void setWorkShiftId(Integer workShiftId) {
		this.workShiftId = workShiftId;
	}
	public String getWorkShiftName() {
		return workShiftName;
	}
	public void setWorkShiftName(String workShiftName) {
		this.workShiftName = workShiftName;
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

	public Integer getProductId() {
		return productId;
	}

	public void setProductId(Integer productId) {
		this.productId = productId;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public Integer getRoleId() {
		return roleId;
	}

	public void setRoleId(Integer roleId) {
		this.roleId = roleId;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public Integer getIsApproved() {
		return isApproved;
	}

	public void setIsApproved(Integer isApproved) {
		this.isApproved = isApproved;
	}

	public String getApprovalComments() {
		return approvalComments;
	}

	public void setApprovalComments(String approvalComments) {
		this.approvalComments = approvalComments;
	}

	public String getApprovedDate() {
		return approvedDate;
	}

	public void setApprovedDate(String approvedDate) {
		this.approvedDate = approvedDate;
	}

	public Integer getApproverId() {
		return approverId;
	}

	public void setApproverId(Integer approverId) {
		this.approverId = approverId;
	}

	public String getApproverName() {
		return approverName;
	}

	public void setApproverName(String approverName) {
		this.approverName = approverName;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
	}

	public String getModifiedDate() {
		return modifiedDate;
	}

	public void setModifiedDate(String modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

	public Integer getActivityTypeId() {
		return activityTypeId;
	}

	public void setActivityTypeId(Integer activityTypeId) {
		this.activityTypeId = activityTypeId;
	}

	public String getActivityTypeName() {
		return activityTypeName;
	}

	public void setActivityTypeName(String activityTypeName) {
		this.activityTypeName = activityTypeName;
	}

	public Integer getResourceShiftCheckInId() {
		return resourceShiftCheckInId;
	}

	public void setResourceShiftCheckInId(Integer resourceShiftCheckInId) {
		this.resourceShiftCheckInId = resourceShiftCheckInId;
	}

	public String getResourceShiftCheckInDisplayName() {
		return resourceShiftCheckInDisplayName;
	}

	public void setResourceShiftCheckInDisplayName(
			String resourceShiftCheckInDisplayName) {
		this.resourceShiftCheckInDisplayName = resourceShiftCheckInDisplayName;
	}
	public Date getResourceCheckInDate() {
		return resourceCheckInDate;
	}
	public void setResourceCheckInDate(Date resourceCheckInDate) {
		this.resourceCheckInDate = resourceCheckInDate;
	}
	
}
