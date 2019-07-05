package com.hcl.atf.taf.model.json;

import com.fasterxml.jackson.annotation.JsonProperty;

public class JsonReservedResourcesForBooking implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	
	@JsonProperty
	private Integer reservedResourceBookingId;
	@JsonProperty	
	private String reservationDate;
	@JsonProperty
	private Integer availableNotBookedCount;
	@JsonProperty
	private Integer notAvailableAndNotBookedCount;
	@JsonProperty
	private Integer userId;
	@JsonProperty
	private String userLoginId;
	@JsonProperty
	private Integer userRoleId;
	@JsonProperty
	private String userRoleName;
	@JsonProperty
	private Integer shiftTypeId;
	@JsonProperty
	private String shiftTypeName;
	@JsonProperty
	private Integer resourcePoolId;
	@JsonProperty
	private String resourcePoolName;
	
	public JsonReservedResourcesForBooking(){
		this.reservationDate = new String();
		this.availableNotBookedCount = new Integer(0);
		this.notAvailableAndNotBookedCount = new Integer(0);
		this.userId = new Integer(0);
		this.userLoginId = new String();
		this.userRoleId  = new Integer(0);
		this.userRoleName = new String();
		this.shiftTypeId  = new Integer(0);
		this.shiftTypeName = new String();
		this.resourcePoolId  = new Integer(0);
		this.resourcePoolName = new String();
		
	}
	
	public Integer getReservedResourceBookingId() {
		return reservedResourceBookingId;
	}
	public void setReservedResourceBookingId(Integer reservedResourceBookingId) {
		this.reservedResourceBookingId = reservedResourceBookingId;
	}
	public String getReservationDate() {
		return reservationDate;
	}
	public void setReservationDate(String reservationDate) {
		this.reservationDate = reservationDate;
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
	public Integer getUserId() {
		return userId;
	}
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	public String getUserLoginId() {
		return userLoginId;
	}
	public void setUserLoginId(String userLoginId) {
		this.userLoginId = userLoginId;
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

	public Integer getShiftTypeId() {
		return shiftTypeId;
	}

	public void setShiftTypeId(Integer shiftTypeId) {
		this.shiftTypeId = shiftTypeId;
	}

	public String getShiftTypeName() {
		return shiftTypeName;
	}

	public void setShiftTypeName(String shiftTypeName) {
		this.shiftTypeName = shiftTypeName;
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
