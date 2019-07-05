package com.hcl.atf.taf.model.json;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.hcl.atf.taf.model.ResourceAvailability;
import com.hcl.atf.taf.model.ShiftTypeMaster;
import com.hcl.atf.taf.model.UserList;


public class JsonResourceAvailability implements java.io.Serializable{
	
	private static final long serialVersionUID = 1L;
	
	@JsonProperty	
	private Integer resourceAvailabilityId;
	
	@JsonProperty
	private int resourceId;
	@JsonProperty	
	private String resourceName;
	@JsonProperty
	private Integer shiftId;
	@JsonProperty	
	private String shiftName;
	@JsonProperty	
	private String weekStartDate;
	@JsonProperty	
	private Integer weekNo;
	@JsonProperty	
	private String day1ValueforToolTip;
	@JsonProperty	
	private String day2ValueforToolTip;
	@JsonProperty	
	private String day3ValueforToolTip;
	@JsonProperty	
	private String day4ValueforToolTip;
	@JsonProperty	
	private String day5ValueforToolTip;
	@JsonProperty	
	private String day6ValueforToolTip;
	@JsonProperty	
	private String day7ValueforToolTip;
	


	public String getDay1ValueforToolTip() {
		return day1ValueforToolTip;
	}

	public void setDay1ValueforToolTip(String day1ValueforToolTip) {
		this.day1ValueforToolTip = day1ValueforToolTip;
	}

	public String getDay2ValueforToolTip() {
		return day2ValueforToolTip;
	}

	public void setDay2ValueforToolTip(String day2ValueforToolTip) {
		this.day2ValueforToolTip = day2ValueforToolTip;
	}

	public String getDay3ValueforToolTip() {
		return day3ValueforToolTip;
	}

	public void setDay3ValueforToolTip(String day3ValueforToolTip) {
		this.day3ValueforToolTip = day3ValueforToolTip;
	}

	public String getDay4ValueforToolTip() {
		return day4ValueforToolTip;
	}

	public void setDay4ValueforToolTip(String day4ValueforToolTip) {
		this.day4ValueforToolTip = day4ValueforToolTip;
	}

	public String getDay5ValueforToolTip() {
		return day5ValueforToolTip;
	}

	public void setDay5ValueforToolTip(String day5ValueforToolTip) {
		this.day5ValueforToolTip = day5ValueforToolTip;
	}

	public String getDay6ValueforToolTip() {
		return day6ValueforToolTip;
	}

	public void setDay6ValueforToolTip(String day6ValueforToolTip) {
		this.day6ValueforToolTip = day6ValueforToolTip;
	}

	public String getDay7ValueforToolTip() {
		return day7ValueforToolTip;
	}

	public void setDay7ValueforToolTip(String day7ValueforToolTip) {
		this.day7ValueforToolTip = day7ValueforToolTip;
	}

	@JsonProperty	
	private String  day1;
	@JsonProperty	
	private String day2;
	@JsonProperty	
	private String day3;
	@JsonProperty	
	private String day4;
	@JsonProperty	
	private String day5;
	@JsonProperty	
	private String day6;
	@JsonProperty	
	private String day7;
	
	@JsonProperty	
	private Integer day1ResourceCount;
	@JsonProperty	
	private Integer day2ResourceCount;
	@JsonProperty	
	private Integer day3ResourceCount;
	@JsonProperty	
	private Integer day4ResourceCount;
	@JsonProperty	
	private Integer day5ResourceCount;
	@JsonProperty	
	private Integer day6ResourceCount;
	@JsonProperty	
	private Integer day7ResourceCount;
	
	
	public JsonResourceAvailability() {
		this.day1ResourceCount = new Integer(0);
		this.day2ResourceCount = new Integer(0);
		this.day3ResourceCount = new Integer(0);
		this.day4ResourceCount = new Integer(0);
		this.day5ResourceCount = new Integer(0);
		this.day6ResourceCount = new Integer(0);
		this.day7ResourceCount = new Integer(0);
		
		this.day1 = new String("-");
		this.day2 = new String("-");
		this.day3= new String("-");
		this.day4 = new String("-");
		this.day5= new String("-");
		this.day6 = new String("-");
		this.day7 = new String("-");
		
		this.day1ValueforToolTip=new String();
		this.day2ValueforToolTip=new String();
		this.day3ValueforToolTip=new String();
		this.day4ValueforToolTip=new String();
		this.day5ValueforToolTip=new String();
		this.day6ValueforToolTip=new String();
		this.day7ValueforToolTip=new String();
	}	

	public JsonResourceAvailability(ResourceAvailability resourceAvailability) {
		this.resourceAvailabilityId=resourceAvailability.getResourceAvailabilityId();

		this.resourceId=resourceAvailability.getResource().getUserId();
		this.resourceName=resourceAvailability.getResource().getLoginId();
		
		this.shiftId=resourceAvailability.getShiftTypeMaster().getShiftTypeId();
		this.shiftName=resourceAvailability.getShiftTypeMaster().getShiftName();
		
		this.day1ResourceCount = new Integer(0);
		this.day2ResourceCount = new Integer(0);
		this.day3ResourceCount = new Integer(0);
		this.day4ResourceCount = new Integer(0);
		this.day5ResourceCount = new Integer(0);
		this.day6ResourceCount = new Integer(0);
		this.day7ResourceCount = new Integer(0);
		
		
		this.day1 = new String();
		this.day2 = new String();
		this.day3= new String();
		this.day4 = new String();
		this.day5= new String();
		this.day6 = new String();
		this.day7 = new String();
		
		this.day1ValueforToolTip=new String();
		this.day2ValueforToolTip=new String();
		this.day3ValueforToolTip=new String();
		this.day4ValueforToolTip=new String();
		this.day5ValueforToolTip=new String();
		this.day6ValueforToolTip=new String();
		this.day7ValueforToolTip=new String();
	}

	public int getResourceAvailabilityId() {
		return resourceAvailabilityId;
	}

	public void setResourceAvailabilityId(int resourceAvailabilityId) {
		this.resourceAvailabilityId = resourceAvailabilityId;
	}
	
	
	
	public int getResourceId() {
		return resourceId;
	}

	public String getDay1() {
		return day1;
	}

	public void setDay1(String day1) {
		this.day1 = day1;
	}

	public String getDay2() {
		return day2;
	}

	public void setDay2(String day2) {
		this.day2 = day2;
	}

	public String getDay3() {
		return day3;
	}

	public void setDay3(String day3) {
		this.day3 = day3;
	}

	public String getDay4() {
		return day4;
	}

	public void setDay4(String day4) {
		this.day4 = day4;
	}

	public String getDay5() {
		return day5;
	}

	public void setDay5(String day5) {
		this.day5 = day5;
	}

	public String getDay6() {
		return day6;
	}

	public void setDay6(String day6) {
		this.day6 = day6;
	}

	public String getDay7() {
		return day7;
	}

	public void setDay7(String day7) {
		this.day7 = day7;
	}

	public void setResourceAvailabilityId(Integer resourceAvailabilityId) {
		this.resourceAvailabilityId = resourceAvailabilityId;
	}

	public void setResourceId(int resourceId) {
		this.resourceId = resourceId;
	}

	public String getResourceName() {
		return resourceName;
	}

	public void setResourceName(String resourceName) {
		this.resourceName = resourceName;
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

	@JsonIgnore
	public ResourceAvailability getResourceAvailability() {
	
		ResourceAvailability resourceAvailability = new ResourceAvailability();
		resourceAvailability.setResourceAvailabilityId(this.resourceAvailabilityId);
		
		UserList resource = new UserList();
		resource.setUserId(this.resourceId);
		resourceAvailability.setResource(resource);
		
		ShiftTypeMaster shiftTypeMaster = new ShiftTypeMaster();
		shiftTypeMaster.setShiftTypeId(this.shiftId);
		resourceAvailability.setShiftTypeMaster(shiftTypeMaster);
		return resourceAvailability;
	}
	
	public Integer getDay1ResourceCount() {
		return day1ResourceCount;
	}

	public void setDay1ResourceCount(Integer day1ResourceCount) {
		this.day1ResourceCount = day1ResourceCount;
	}

	public Integer getDay2ResourceCount() {
		return day2ResourceCount;
	}

	public void setDay2ResourceCount(Integer day2ResourceCount) {
		this.day2ResourceCount = day2ResourceCount;
	}
	
	public Integer getDay3ResourceCount() {
		return day3ResourceCount;
	}

	public void setDay3ResourceCount(Integer day3ResourceCount) {
		this.day3ResourceCount = day3ResourceCount;
	}
	
	public Integer getDay4ResourceCount() {
		return day4ResourceCount;
	}

	public void setDay4ResourceCount(Integer day4ResourceCount) {
		this.day4ResourceCount = day4ResourceCount;
	}
	
	public Integer getDay5ResourceCount() {
		return day5ResourceCount;
	}

	public void setDay5ResourceCount(Integer day5ResourceCount) {
		this.day5ResourceCount = day5ResourceCount;
	}
	
	public Integer getDay6ResourceCount() {
		return day6ResourceCount;
	}

	public void setDay6ResourceCount(Integer day6ResourceCount) {
		this.day6ResourceCount = day6ResourceCount;
	}
	
	public Integer getDay7ResourceCount() {
		return day7ResourceCount;
	}

	public void setDay7ResourceCount(Integer day7ResourceCount) {
		this.day7ResourceCount = day7ResourceCount;
	}
	
	public Integer getWeekNo() {
		return weekNo;
	}

	public void setWeekNo(Integer weekNo) {
		this.weekNo = weekNo;
	}

	public String getWeekStartDate() {
		return weekStartDate;
	}

	public void setWeekStartDate(String weekStartDate) {
		this.weekStartDate = weekStartDate;
	}
}
