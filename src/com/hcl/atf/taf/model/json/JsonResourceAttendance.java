package com.hcl.atf.taf.model.json;

import java.text.ParseException;
import java.util.Locale;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.hcl.atf.taf.controller.utilities.DateUtility;
import com.hcl.atf.taf.model.ResourceAvailability;
import com.hcl.atf.taf.model.dto.ResourceAttendanceSummaryDTO;

public class JsonResourceAttendance implements java.io.Serializable{
	
	private static final long serialVersionUID = 1L;
	
	@JsonProperty	
	private Integer resourceAvailabilityId;
	@JsonProperty
	private Integer resourcePoolId;
	@JsonProperty	
	private String resourcePoolName;
	@JsonProperty
	private Integer userId;
	@JsonProperty	
	private String userName;
	@JsonProperty	
	private String workDate;
	@JsonProperty	
	private String roleName;
	@JsonProperty	
	private String userTypeName;
	@JsonProperty	
	private Integer isAvailable;
	@JsonProperty	
	private Integer bookForShift;
	@JsonProperty	
	private Integer shiftAttendance;
	@JsonProperty	
	private String attendanceCheckInTime;
	@JsonProperty	
	private String attendanceCheckOutTime;
	@JsonProperty	
	private Integer shiftBillingModeIsFull;
	@JsonProperty	
	private String shiftBillingModeIsFullName;
	@JsonProperty
	private Integer vendorId;
	@JsonProperty
	private String registeredCompanyName;
	@JsonProperty
	private Integer bookedCount;
	@JsonProperty
	private Integer presentCount;
	@JsonProperty
	private Integer	fullBillingCount;
	@JsonProperty
	private Integer benchCount;
	@JsonProperty
	private Integer availableCount;
	@JsonProperty
	private Integer demandCount;
	@JsonProperty
	private Integer gapCount;
	@JsonProperty
	private Integer totalBookings;
	@JsonProperty
	private Integer showUp;
	@JsonProperty
	private Integer onTime;
	@JsonProperty
	private StringBuffer skillName;
	@JsonProperty
	private int skillNameCharacterlength;
	@JsonProperty
	private String shiftTypeName;

	public Integer getShiftBillingModeIsFull() {
		return shiftBillingModeIsFull;
	}

	public void setShiftBillingModeIsFull(Integer shiftBillingModeIsFull) {
		this.shiftBillingModeIsFull = shiftBillingModeIsFull;
	}

	public String getShiftBillingModeIsFullName() {
		return shiftBillingModeIsFullName;
	}

	public void setShiftBillingModeIsFullName(String shiftBillingModeIsFullName) {
		this.shiftBillingModeIsFullName = shiftBillingModeIsFullName;
	}

	public JsonResourceAttendance() {
		
		this.resourcePoolId = new Integer(0);
		this.resourcePoolName = new String();
		this.userId = new Integer(0);
		this.userName = new String();
		this.workDate = new String();
		this.roleName = new String();
		this.userTypeName = new String();
		this.shiftBillingModeIsFull = new Integer(0);
		this.vendorId=new Integer(0);
		this.registeredCompanyName=new String();
		this.bookedCount = new Integer(0);
		this.presentCount = new Integer(0);
		this.fullBillingCount = new Integer(0);
		this.benchCount = new Integer(0);
		this.availableCount = new Integer(0);
		this.demandCount = new Integer(0);
		this.gapCount = new Integer(0);
		this.totalBookings = new Integer(0);
		this.showUp  = new Integer(0);
		this.onTime = new Integer(0);
		this.shiftTypeName = new String();
		
	}

	public JsonResourceAttendance(ResourceAvailability resourceAvailability) {
	
	if(resourceAvailability.getShiftBillingModeIsFull() != null){	
		this.shiftBillingModeIsFull = resourceAvailability.getShiftBillingModeIsFull();
	}else{
		this.shiftBillingModeIsFull = 0;
	}
	this.resourceAvailabilityId = resourceAvailability.getResourceAvailabilityId();
	this.resourcePoolId = resourceAvailability.getResource().getResourcePool().getResourcePoolId();
	this.resourcePoolName = resourceAvailability.getResource().getResourcePool().getResourcePoolName();
	this.userId = resourceAvailability.getResource().getUserId();
	this.userName = resourceAvailability.getResource().getLoginId();
	this.workDate = resourceAvailability.getWorkDate().toString();
	this.roleName = resourceAvailability.getResource().getUserRoleMaster().getRoleName();
	this.userTypeName = resourceAvailability.getResource().getUserTypeMasterNew().getUserTypeLabel();
	this.vendorId=resourceAvailability.getResource().getVendor().getVendorId();
	this.registeredCompanyName=resourceAvailability.getResource().getVendor().getRegisteredCompanyName();
	this.shiftTypeName = resourceAvailability.getShiftTypeMaster().getShiftName();
	if(resourceAvailability.getIsAvailable() == null){
		this.isAvailable = 0;
	}else{ 
		this.isAvailable = resourceAvailability.getIsAvailable(); 
	}
	
	if(resourceAvailability.getBookForShift() == null){
		this.bookForShift = 0;
	}else{
		this.bookForShift = resourceAvailability.getBookForShift();
	}
	
	if(resourceAvailability.getShiftAttendance() == null){
		this.shiftAttendance = 0;
	}else{
		this.shiftAttendance = resourceAvailability.getShiftAttendance();
	}
	
	if(resourceAvailability.getAttendanceCheckInTime() == null){
		this.attendanceCheckInTime="";
	}else{
		this.attendanceCheckInTime = DateUtility.dateToStringInSecond(resourceAvailability.getAttendanceCheckInTime());
		this.attendanceCheckInTime=this.attendanceCheckInTime.replace(" ", ",");
	}	
	if(resourceAvailability.getAttendanceCheckOutTime() == null){
		this.attendanceCheckOutTime="";
	}else{
		this.attendanceCheckOutTime = DateUtility.dateToStringInSecond(resourceAvailability.getAttendanceCheckOutTime());
		this.attendanceCheckOutTime=this.attendanceCheckOutTime.replace(" ", ",");
	}	
	
}
	
	public JsonResourceAttendance(ResourceAvailability resourceAvailability,Locale locale) {
		
			this.shiftBillingModeIsFull = resourceAvailability.getShiftBillingModeIsFull();
			
	
		this.resourceAvailabilityId = resourceAvailability.getResourceAvailabilityId();
		this.resourcePoolId = resourceAvailability.getResource().getResourcePool().getResourcePoolId();
		this.resourcePoolName = resourceAvailability.getResource().getResourcePool().getResourcePoolName();
		this.userId = resourceAvailability.getResource().getUserId();
		this.userName = resourceAvailability.getResource().getLoginId();
		this.workDate = resourceAvailability.getWorkDate().toString();
		this.roleName = resourceAvailability.getResource().getUserRoleMaster().getRoleName();
		this.userTypeName = resourceAvailability.getResource().getUserTypeMasterNew().getUserTypeLabel();
		this.vendorId=resourceAvailability.getResource().getVendor().getVendorId();
		this.registeredCompanyName=resourceAvailability.getResource().getVendor().getRegisteredCompanyName();
		this.shiftTypeName = resourceAvailability.getShiftTypeMaster().getShiftName();
		if(resourceAvailability.getIsAvailable() == null){
			this.isAvailable = 0;
		}else{ 
			this.isAvailable = resourceAvailability.getIsAvailable(); 
		}
		
		if(resourceAvailability.getBookForShift() == null){
			this.bookForShift = 0;
		}else{
			this.bookForShift = resourceAvailability.getBookForShift();
		}
		
		if(resourceAvailability.getShiftAttendance() == null){
			this.shiftAttendance = 0;
		}else{
			this.shiftAttendance = resourceAvailability.getShiftAttendance();
		}
		
		if(resourceAvailability.getAttendanceCheckInTime() == null){
			this.attendanceCheckInTime="";
		}else{
			if(resourceAvailability.getAttendanceCheckInTime()!=null)
				try {
					this.attendanceCheckInTime=DateUtility.convertUTCToLocale(locale, resourceAvailability.getAttendanceCheckInTime().toString());
				} catch (ParseException e) {
					e.printStackTrace();
				}
			this.attendanceCheckInTime=this.attendanceCheckInTime.replace(" ", ",");
		}	
		if(resourceAvailability.getAttendanceCheckOutTime() == null){
			this.attendanceCheckOutTime="";
		}else{
			this.attendanceCheckOutTime = DateUtility.dateToStringInSecond(resourceAvailability.getAttendanceCheckOutTime());
			this.attendanceCheckOutTime=this.attendanceCheckOutTime.replace(" ", ",");
		}	
		
	}

	public JsonResourceAttendance(ResourceAttendanceSummaryDTO resourceAttendanceSummaryDTO) {
		this.resourcePoolId = resourceAttendanceSummaryDTO.getResourcePoolId();
		this.resourcePoolName = resourceAttendanceSummaryDTO.getResourcePoolName();
		this.bookedCount = resourceAttendanceSummaryDTO.getBookedCount();
		this.presentCount = resourceAttendanceSummaryDTO.getPresentCount();
		if(resourceAttendanceSummaryDTO.getFullBillingCount() != null){
			this.fullBillingCount = resourceAttendanceSummaryDTO.getFullBillingCount();
		}else{
			this.fullBillingCount = 0;
		}
		this.benchCount = resourceAttendanceSummaryDTO.getBenchCount();
		this.availableCount= resourceAttendanceSummaryDTO.getAvailableCount();
		if(resourceAttendanceSummaryDTO.getDemandCount() != null)
		{
			this.demandCount = resourceAttendanceSummaryDTO.getDemandCount();
		}else{
			this.demandCount = 0;
		}
		if(resourceAttendanceSummaryDTO.getGapCount() != null)
		{
			this.gapCount = resourceAttendanceSummaryDTO.getGapCount();
		}else{
			this.gapCount = 0;
		}
		if(resourceAttendanceSummaryDTO.getTotalBookings() != null)
		{
			this.totalBookings = resourceAttendanceSummaryDTO.getTotalBookings();
		}else{
			this.totalBookings = 0;
		}
		if(resourceAttendanceSummaryDTO.getShowUp() != null)
		{
			this.showUp = resourceAttendanceSummaryDTO.getShowUp();
		}else{
			this.showUp = 0;
		}
		if(resourceAttendanceSummaryDTO.getOnTime() != null)
		{
			this.onTime = resourceAttendanceSummaryDTO.getOnTime();
		}else{
			this.onTime = 0;
		}
	}

	public Integer getResourceAvailabilityId() {
		return resourceAvailabilityId;
	}

	public void setResourceAvailabilityId(Integer resourceAvailabilityId) {
		this.resourceAvailabilityId = resourceAvailabilityId;
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

	public String getWorkDate() {
		return workDate;
	}

	public void setWorkDate(String workDate) {
		this.workDate = workDate;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public String getUserTypeName() {
		return userTypeName;
	}

	public void setUserTypeName(String userTypeName) {
		this.userTypeName = userTypeName;
	}

	public Integer getIsAvailable() {
		return isAvailable;
	}

	public void setIsAvailable(Integer isAvailable) {
		this.isAvailable = isAvailable;
	}

	public Integer getBookForShift() {
		return bookForShift;
	}

	public void setBookForShift(Integer bookForShift) {
		this.bookForShift = bookForShift;
	}

	public Integer getShiftAttendance() {
		return shiftAttendance;
	}

	public void setShiftAttendance(Integer shiftAttendance) {
		this.shiftAttendance = shiftAttendance;
	}

	public String getAttendanceCheckInTime() {
		return attendanceCheckInTime;
	}

	public void setAttendanceCheckInTime(String attendanceCheckInTime) {
		this.attendanceCheckInTime = attendanceCheckInTime;
	}

	public String getAttendanceCheckOutTime() {
		return attendanceCheckOutTime;
	}

	public void setAttendanceCheckOutTime(String attendanceCheckOutTime) {
		this.attendanceCheckOutTime = attendanceCheckOutTime;
	}

	public Integer getVendorId() {
		return vendorId;
	}

	public void setVendorId(Integer vendorId) {
		this.vendorId = vendorId;
	}

	public String getRegisteredCompanyName() {
		return registeredCompanyName;
	}

	public void setRegisteredCompanyName(String registeredCompanyName) {
		this.registeredCompanyName = registeredCompanyName;
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

	public StringBuffer getSkillName() {
		return skillName;
	}

	public void setSkillName(StringBuffer skillName) {
		this.skillName = skillName;
	}

	public int getSkillNameCharacterlength() {
		return skillNameCharacterlength;
	}

	public void setSkillNameCharacterlength(int skillNameCharacterlength) {
		this.skillNameCharacterlength = skillNameCharacterlength;
	}

	public String getShiftTypeName() {
		return shiftTypeName;
	}

	public void setShiftTypeName(String shiftTypeName) {
		this.shiftTypeName = shiftTypeName;
	}

	
}
