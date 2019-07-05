package com.hcl.atf.taf.model.dto;


public class WeeklyResourceReservationDTO {

	private Integer workPackageId;
	private String workPackageName;
	
	private Integer userId;
	private String userName;
	
	private Integer userRoleId;
	private String userRoleName;
	
	
	private Integer shiftId;
	private String shiftName;
	
	private Integer reservationWeek;
	private Integer reservationYear;
	
	private String userCode;
	
	private Integer userTypeId;
	private String userTypeName;
	
	private Long groupReservationId;
	private float reservationPercentage;
	
	private Integer resourcePoolId;
	private String resourcePoolName;
	
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
	public Integer getReservationWeek() {
		return reservationWeek;
	}
	public void setReservationWeek(Integer reservationWeek) {
		this.reservationWeek = reservationWeek;
	}
	public Integer getReservationYear() {
		return reservationYear;
	}
	public void setReservationYear(Integer reservationYear) {
		this.reservationYear = reservationYear;
	}
	public Long getGroupReservationId() {
		return groupReservationId;
	}
	public void setGroupReservationId(Long groupReservationId) {
		this.groupReservationId = groupReservationId;
	}
	public float getReservationPercentage() {
		return reservationPercentage;
	}
	public void setReservationPercentage(float reservationPercentage) {
		this.reservationPercentage = reservationPercentage;
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
	public String getUserCode() {
		return userCode;
	}
	public void setUserCode(String userCode) {
		this.userCode = userCode;
	}
	public Integer getUserTypeId() {
		return userTypeId;
	}
	public void setUserTypeId(Integer userTypeId) {
		this.userTypeId = userTypeId;
	}
	public String getUserTypeName() {
		return userTypeName;
	}
	public void setUserTypeName(String userTypeName) {
		this.userTypeName = userTypeName;
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
	
	
}
