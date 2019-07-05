package com.hcl.atf.taf.model.dto;

import java.util.Date;

public class TimeSheetStatisticsDTO {
	
	private Date actualExecutionDate;
	
	private Integer shiftId;
	private String shiftName;

	private Date workShiftStartDateTime;
	private Date workShiftEndDateTime;
	private Integer shiftTypeId;

	private Date actualShiftStartDateTime;
	private Date actualShiftEndDateTime;
	
	private Integer testerId;
	private String testerName;
	private String userFirstName;
	private String userLastName;
	private Integer userRoleId;
	private String userRoleName;
	private Integer customerId;  // PALM reference for a customer of a product
	private String vendorName;
	
	private Integer productResourceShiftCheckInId;
	private Date productResourceShiftCheckIn;
	private Date productResourceShiftCheckOut;

	private Integer testFactoryId;
	private String testFactoryName;
	
	private Integer productId;
	private String productName;
	private String projectCode;
	
	private Integer workPackageId;
	private String workPackageName;
	
	private Integer hoursWorked;
	private Integer minutesWorked;
	
	private Integer shiftAttendanceGraceTime;
	private Integer shiftLunchAuthorisedTime;
	private Integer shiftBreaksAuthorisedTime;
	private Integer weeklyOverTimeLimit;
	
	public Date getActualExecutionDate() {
		return actualExecutionDate;
	}
	public void setActualExecutionDate(Date actualExecutionDate) {
		this.actualExecutionDate = actualExecutionDate;
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
	public Date getWorkShiftStartDateTime() {
		return workShiftStartDateTime;
	}
	public void setWorkShiftStartDateTime(Date workShiftStartDateTime) {
		this.workShiftStartDateTime = workShiftStartDateTime;
	}
	public Date getWorkShiftEndDateTime() {
		return workShiftEndDateTime;
	}
	public void setWorkShiftEndDateTime(Date workShiftEndDateTime) {
		this.workShiftEndDateTime = workShiftEndDateTime;
	}
	public Integer getShiftTypeId() {
		return shiftTypeId;
	}
	public void setShiftTypeId(Integer shiftTypeId) {
		this.shiftTypeId = shiftTypeId;
	}
	public Date getActualShiftStartDateTime() {
		return actualShiftStartDateTime;
	}
	public void setActualShiftStartDateTime(Date actualShiftStartDateTime) {
		this.actualShiftStartDateTime = actualShiftStartDateTime;
	}
	public Date getActualShiftEndDateTime() {
		return actualShiftEndDateTime;
	}
	public void setActualShiftEndDateTime(Date actualShiftEndDateTime) {
		this.actualShiftEndDateTime = actualShiftEndDateTime;
	}
	public Integer getTesterId() {
		return testerId;
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
	public String getUserFirstName() {
		return userFirstName;
	}
	public void setUserFirstName(String userFirstName) {
		this.userFirstName = userFirstName;
	}
	public String getUserLastName() {
		return userLastName;
	}
	public void setUserLastName(String userLastName) {
		this.userLastName = userLastName;
	}
	public Integer getUserRoleId() {
		return userRoleId;
	}
	public void setUserRoleId(Integer userRoleId) {
		this.userRoleId = userRoleId;
	}
	public String getUserRoleName() {
		return userRoleName;
	}
	public void setUserRoleName(String userRoleName) {
		this.userRoleName = userRoleName;
	}
	public Integer getCustomerId() {
		return customerId;
	}
	public void setCustomerId(Integer customerId) {
		this.customerId = customerId;
	}
	public String getVendorName() {
		return vendorName;
	}
	public void setVendorName(String vendorName) {
		this.vendorName = vendorName;
	}
	public Integer getProductResourceShiftCheckInId() {
		return productResourceShiftCheckInId;
	}
	public void setProductResourceShiftCheckInId(
			Integer productResourceShiftCheckInId) {
		this.productResourceShiftCheckInId = productResourceShiftCheckInId;
	}
	public Date getProductResourceShiftCheckIn() {
		return productResourceShiftCheckIn;
	}
	public void setProductResourceShiftCheckIn(Date productResourceShiftCheckIn) {
		this.productResourceShiftCheckIn = productResourceShiftCheckIn;
	}
	public Date getProductResourceShiftCheckOut() {
		return productResourceShiftCheckOut;
	}
	public void setProductResourceShiftCheckOut(Date productResourceShiftCheckOut) {
		this.productResourceShiftCheckOut = productResourceShiftCheckOut;
	}
	public Integer getTestFactoryId() {
		return testFactoryId;
	}
	public void setTestFactoryId(Integer testFactoryId) {
		this.testFactoryId = testFactoryId;
	}
	public String getTestFactoryName() {
		return testFactoryName;
	}
	public void setTestFactoryName(String testFactoryName) {
		this.testFactoryName = testFactoryName;
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
	public String getProjectCode() {
		return projectCode;
	}
	public void setProjectCode(String projectCode) {
		this.projectCode = projectCode;
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
	public Integer getHoursWorked() {
		return hoursWorked;
	}
	public void setHoursWorked(Integer hoursWorked) {
		this.hoursWorked = hoursWorked;
	}
	public Integer getMinutesWorked() {
		return minutesWorked;
	}
	public void setMinutesWorked(Integer minutesWorked) {
		this.minutesWorked = minutesWorked;
	}
	public Integer getShiftAttendanceGraceTime() {
		return shiftAttendanceGraceTime;
	}
	public void setShiftAttendanceGraceTime(Integer shiftAttendanceGraceTime) {
		this.shiftAttendanceGraceTime = shiftAttendanceGraceTime;
	}
	public Integer getShiftLunchAuthorisedTime() {
		return shiftLunchAuthorisedTime;
	}
	public void setShiftLunchAuthorisedTime(Integer shiftLunchAuthorisedTime) {
		this.shiftLunchAuthorisedTime = shiftLunchAuthorisedTime;
	}
	public Integer getShiftBreaksAuthorisedTime() {
		return shiftBreaksAuthorisedTime;
	}
	public void setShiftBreaksAuthorisedTime(Integer shiftBreaksAuthorisedTime) {
		this.shiftBreaksAuthorisedTime = shiftBreaksAuthorisedTime;
	}
	public Integer getWeeklyOverTimeLimit() {
		return weeklyOverTimeLimit;
	}
	public void setWeeklyOverTimeLimit(Integer weeklyOverTimeLimit) {
		this.weeklyOverTimeLimit = weeklyOverTimeLimit;
	}
}

