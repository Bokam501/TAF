package com.hcl.atf.taf.model.json;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public class JsonResourceAvailabilityPlan implements java.io.Serializable{
	
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
	private Integer dayShiftAvailibility;
	@JsonProperty	
	private Integer nightShiftAvailibility;
	@JsonProperty	
	private Integer graveyardShiftAvailibility;
	@JsonProperty	
	private Integer dayShiftBooking;
	@JsonProperty	
	private Integer nightShiftBooking;
	@JsonProperty	
	private Integer graveyardShiftBooking;
	@JsonProperty	
	private String roleName;
	@JsonProperty	
	private String userTypeName;
	@JsonProperty	
	private Integer shiftBillingModeIsFull;
	@JsonProperty	
	private String shiftBillingModeIsFullName;
	@JsonProperty
	private Integer vendorId;
	@JsonProperty
	private String registeredCompanyName;
	@JsonProperty	
	private Integer shiftId;
	@JsonProperty	
	private Integer shiftAvailibility;
	@JsonProperty	
	private Integer shiftBooking;
	
	@JsonProperty	
	private Integer shiftTypeBooking;
	
	@JsonProperty	
	private Integer shiftTypeAvailability;
	
	@JsonProperty	
	private Integer shiftTypeId;
	@JsonProperty
	private  Long bookedHrs;
	@JsonProperty
	private  String timeSheetHours;
	@JsonProperty
	private StringBuffer skillName;
	@JsonProperty
	private int skillNameCharacterlength;
	@JsonProperty	
	private Integer roleId;
	public Integer getShiftBooking() {
		return shiftBooking;
	}

	public void setShiftBooking(Integer shiftBooking) {
		this.shiftBooking = shiftBooking;
	}

	public Integer getShiftAvailibility() {
		return shiftAvailibility;
	}

	public void setShiftAvailibility(Integer shiftAvailibility) {
		this.shiftAvailibility = shiftAvailibility;
	}

	
	public Integer getShiftId() {
		return shiftId;
	}

	public void setShiftId(Integer shiftId) {
		this.shiftId = shiftId;
	}

	
	public Integer getShiftTypeBooking() {
		return shiftTypeBooking;
	}

	public void setShiftTypeBooking(Integer shiftTypeBooking) {
		this.shiftTypeBooking = shiftTypeBooking;
	}

	public Integer getShiftTypeAvailability() {
		return shiftTypeAvailability;
	}

	public void setShiftTypeAvailability(Integer shiftTypeAvailability) {
		this.shiftTypeAvailability = shiftTypeAvailability;
	}

	public Integer getShiftTypeId() {
		return shiftTypeId;
	}

	public void setShiftTypeId(Integer shiftTypeId) {
		this.shiftTypeId = shiftTypeId;
	}



	public JsonResourceAvailabilityPlan() {
		this.shiftId=new Integer(0);
		this.resourcePoolId = new Integer(0);
		this.resourcePoolName = new String();
		this.userId = new Integer(0);
		this.userName = new String();
		this.workDate = new String();
		
		this.dayShiftAvailibility=new Integer(0);
		this.nightShiftAvailibility=new Integer(0);
		this.graveyardShiftAvailibility=new Integer(0);
		
		this.nightShiftBooking = new Integer(0);
		this.dayShiftBooking = new Integer(0);
		this.graveyardShiftBooking = new Integer(0);
		
		this.shiftTypeId = new Integer(0);
		this.shiftTypeAvailability = new Integer(0);
		this.shiftTypeBooking = new Integer(0);
		
		
		this.roleName = new String();
		this.roleId= new Integer(0);
		this.userTypeName = new String();
		this.shiftBillingModeIsFull = new Integer(0);
		this.shiftBillingModeIsFullName=new String();
        this.vendorId=new Integer(0);
        this.registeredCompanyName=new String();
        this.bookedHrs = new Long(0);
    	this.timeSheetHours = new String("0");
		this.shiftTypeId=shiftTypeId;
		this.shiftTypeAvailability=shiftTypeAvailability;
		this.shiftTypeBooking=shiftTypeBooking;
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

	public Integer getDayShiftAvailibility() {
		return dayShiftAvailibility;
	}

	public void setDayShiftAvailibility(Integer dayShiftAvailibility) {
		this.dayShiftAvailibility = dayShiftAvailibility;
	}

	public Integer getNightShiftAvailibility() {
		return nightShiftAvailibility;
	}

	public void setNightShiftAvailibility(Integer nightShiftAvailibility) {
		this.nightShiftAvailibility = nightShiftAvailibility;
	}

	public Integer getGraveyardShiftAvailibility() {
		return graveyardShiftAvailibility;
	}

	public void setGraveyardShiftAvailibility(Integer graveyardShiftAvailibility) {
		this.graveyardShiftAvailibility = graveyardShiftAvailibility;
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

	public static long getSerialversionuid() {
		return serialVersionUID;
	}


	public Integer getDayShiftBooking() {
		return dayShiftBooking;
	}

	public void setDayShiftBooking(Integer dayShiftBooking) {
		this.dayShiftBooking = dayShiftBooking;
	}

	public Integer getNightShiftBooking() {
		return nightShiftBooking;
	}

	public void setNightShiftBooking(Integer nightShiftBooking) {
		this.nightShiftBooking = nightShiftBooking;
	}

	public Integer getGraveyardShiftBooking() {
		return graveyardShiftBooking;
	}

	public void setGraveyardShiftBooking(Integer graveyardShiftBooking) {
		this.graveyardShiftBooking = graveyardShiftBooking;
	}
	
	
	public Long getBookedHrs() {
		return bookedHrs;
	}

	public void setBookedHrs(Long bookedHrs) {
		this.bookedHrs = bookedHrs;
	}

	public String getTimeSheetHours() {
		return timeSheetHours;
	}

	public void setTimeSheetHours(String timeSheetHours) {
		this.timeSheetHours = timeSheetHours;
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

	@JsonIgnore
	public JsonResourceAvailabilityPlan getJsonResourceAvailabilityPlan(){
		JsonResourceAvailabilityPlan jsonResourceAvailabilityPlan = new JsonResourceAvailabilityPlan();
		jsonResourceAvailabilityPlan.setUserId(userId);
		jsonResourceAvailabilityPlan.setShiftTypeAvailability(shiftTypeAvailability);
		jsonResourceAvailabilityPlan.setShiftTypeBooking(shiftTypeBooking);
		jsonResourceAvailabilityPlan.setShiftTypeId(shiftTypeId);
		
		return jsonResourceAvailabilityPlan;
	}

	public Integer getRoleId() {
		return roleId;
	}

	public void setRoleId(Integer roleId) {
		this.roleId = roleId;
	}
	
	
}
