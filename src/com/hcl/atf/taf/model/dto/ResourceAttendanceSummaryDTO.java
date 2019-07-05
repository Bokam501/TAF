package com.hcl.atf.taf.model.dto;

import java.util.Date;

public class ResourceAttendanceSummaryDTO {
	
	private Integer resourcePoolId;		
	private String 	resourcePoolName;	
	private Integer bookedCount;
	private Integer presentCount;		
	private Integer fullBillingCount;
	private Integer benchCount;
	private Integer availableCount;
	private Integer demandCount;
	private Integer gapCount;
	private Date workDate;
	private Integer shiftTypeId;
	private Integer totalBookings;
	private Integer showUp;
	private Integer onTime;
	private Integer userId;
	private String userName;
	private String roleName;
	private String vendorName;
	
	public void JsonWorkPackageDemandProjection() {
		this.resourcePoolId = new Integer(0);	
		this.resourcePoolName = new String();	
		this.bookedCount = new Integer(0);	
		this.presentCount = new Integer(0);		
		this.fullBillingCount = new Integer(0);	
		this.benchCount = new Integer(0);	
		this.availableCount = new Integer(0);	
		this.demandCount = new Integer(0);
		this.gapCount = new Integer(0);	
		this.workDate = null;
		this.shiftTypeId = new Integer(0);
		this.totalBookings = new Integer(0);	
		this.showUp = new Integer(0);	
		this.onTime = new Integer(0);
		this.userId = new Integer(0);
		this.userName = new String();
		this.roleName = new String();
		this.vendorName = new String();
		
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
	public Integer getBookedCount() {
		return bookedCount;
	}
	public void setBookedCount(Integer bookedCount) {
		this.bookedCount = bookedCount;
	}
	public Integer getPresentCount() {
		return presentCount;
	}
	public void setPresentCount(Integer presentCount) {
		this.presentCount = presentCount;
	}
	public Integer getFullBillingCount() {
		return fullBillingCount;
	}
	public void setFullBillingCount(Integer fullBillingCount) {
		this.fullBillingCount = fullBillingCount;
	}
	public Integer getBenchCount() {
		return benchCount;
	}
	public void setBenchCount(Integer benchCount) {
		this.benchCount = benchCount;
	}
	public Integer getAvailableCount() {
		return availableCount;
	}
	public void setAvailableCount(Integer availableCount) {
		this.availableCount = availableCount;
	}
	public Integer getDemandCount() {
		return demandCount;
	}
	public void setDemandCount(Integer demandCount) {
		this.demandCount = demandCount;
	}
	public Integer getGapCount() {
		return gapCount;
	}
	public void setGapCount(Integer gapCount) {
		this.gapCount = gapCount;
	}
	public Date getWorkDate() {
		return workDate;
	}
	public void setWorkDate(Date workDate) {
		this.workDate = workDate;
	}
	public Integer getShiftTypeId() {
		return shiftTypeId;
	}
	public void setShiftTypeId(Integer shiftTypeId) {
		this.shiftTypeId = shiftTypeId;
	}

	public Integer getTotalBookings() {
		return totalBookings;
	}

	public void setTotalBookings(Integer totalBookings) {
		this.totalBookings = totalBookings;
	}

	public Integer getShowUp() {
		return showUp;
	}

	public void setShowUp(Integer showUp) {
		this.showUp = showUp;
	}

	public Integer getOnTime() {
		return onTime;
	}

	public void setOnTime(Integer onTime) {
		this.onTime = onTime;
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

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public String getVendorName() {
		return vendorName;
	}

	public void setVendorName(String vendorName) {
		this.vendorName = vendorName;
	}

}
