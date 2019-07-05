package com.hcl.atf.taf.model.json;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.hcl.atf.taf.model.ActualShift;
import com.hcl.atf.taf.model.ResourceShiftCheckIn;
import com.hcl.atf.taf.model.UserList;

public class JsonResourceShiftCheckinDetailsForWeek {
private static final long serialVersionUID = 1L;
	
	@JsonProperty	
	private Integer resourceShiftCheckInId;
	@JsonProperty
	private int resourceId;
	@JsonProperty	
	private String resourceName;
	@JsonProperty
	private Integer shiftTypeId;
	@JsonProperty	
	private String shiftTypeName;
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
	@JsonProperty
	private Integer actaulShiftId;
	@JsonProperty
	private String actualShiftName;
	@JsonProperty
	private Integer shiftId;
	@JsonProperty
	private String shiftName;


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
	private String day1CheckInAndTimeSheetHours;
	@JsonProperty	
	private String day2CheckInAndTimeSheetHours;
	@JsonProperty	
	private String day3CheckInAndTimeSheetHours;
	@JsonProperty	
	private String day4CheckInAndTimeSheetHours;
	@JsonProperty	
	private String day5CheckInAndTimeSheetHours;
	@JsonProperty	
	private String day6CheckInAndTimeSheetHours;
	@JsonProperty	
	private String day7CheckInAndTimeSheetHours;
	
	
	public JsonResourceShiftCheckinDetailsForWeek() {
		this.day1CheckInAndTimeSheetHours = new String("00:00 / 00:00");
		this.day2CheckInAndTimeSheetHours = new String("00:00 / 00:00");
		this.day3CheckInAndTimeSheetHours = new String("00:00 / 00:00");
		this.day4CheckInAndTimeSheetHours = new String("00:00 / 00:00");
		this.day5CheckInAndTimeSheetHours = new String("00:00 / 00:00");
		this.day6CheckInAndTimeSheetHours = new String("00:00 / 00:00");
		this.day7CheckInAndTimeSheetHours = new String("00:00 / 00:00");
		
		
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

	public JsonResourceShiftCheckinDetailsForWeek(ResourceShiftCheckIn resourceShiftCheckIn) {
		this.resourceShiftCheckInId = resourceShiftCheckIn.getResourceShiftCheckInId();

		this.resourceId=resourceShiftCheckIn.getUserList().getUserId();
		this.resourceName=resourceShiftCheckIn.getUserList().getLoginId();
		
		this.shiftId = resourceShiftCheckIn.getActualShift().getShift().getShiftId();
		this.shiftName = resourceShiftCheckIn.getActualShift().getShift().getShiftName();
		
		this.shiftTypeId = resourceShiftCheckIn.getActualShift().getShift().getShiftType().getShiftTypeId();
		this.shiftTypeName = resourceShiftCheckIn.getActualShift().getShift().getShiftType().getShiftName();
				
		this.day1CheckInAndTimeSheetHours = new String("00:00 / 00:00");
		this.day2CheckInAndTimeSheetHours = new String("00:00 / 00:00");
		this.day3CheckInAndTimeSheetHours = new String("00:00 / 00:00");
		this.day4CheckInAndTimeSheetHours = new String("00:00 / 00:00");
		this.day5CheckInAndTimeSheetHours = new String("00:00 / 00:00");
		this.day6CheckInAndTimeSheetHours = new String("00:00 / 00:00");
		this.day7CheckInAndTimeSheetHours = new String("00:00 / 00:00");
		
		
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

	public void setResourceId(int resourceId) {
		this.resourceId = resourceId;
	}

	public String getResourceName() {
		return resourceName;
	}

	public void setResourceName(String resourceName) {
		this.resourceName = resourceName;
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

	@JsonIgnore
	public ResourceShiftCheckIn getResourceShiftCheckIn() {
	
		ResourceShiftCheckIn resourceShiftCheckIn = new ResourceShiftCheckIn();
		resourceShiftCheckIn.setResourceShiftCheckInId(this.resourceShiftCheckInId);
		
		UserList resource = new UserList();
		resource.setUserId(this.resourceId);
		resourceShiftCheckIn.setUserList(resource);
		
		ActualShift actualShift = new ActualShift();
		actualShift.setActualShiftId(this.actaulShiftId);
		resourceShiftCheckIn.setActualShift(actualShift);
		
		return resourceShiftCheckIn;
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

	public Integer getActaulShiftId() {
		return actaulShiftId;
	}

	public void setActaulShiftId(Integer actaulShiftId) {
		this.actaulShiftId = actaulShiftId;
	}

	public String getActualShiftName() {
		return actualShiftName;
	}

	public void setActualShiftName(String actualShiftName) {
		this.actualShiftName = actualShiftName;
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

	public Integer getResourceShiftCheckInId() {
		return resourceShiftCheckInId;
	}

	public void setResourceShiftCheckInId(Integer resourceShiftCheckInId) {
		this.resourceShiftCheckInId = resourceShiftCheckInId;
	}

	public String getDay1CheckInAndTimeSheetHours() {
		return day1CheckInAndTimeSheetHours;
	}

	public void setDay1CheckInAndTimeSheetHours(String day1CheckInAndTimeSheetHours) {
		this.day1CheckInAndTimeSheetHours = day1CheckInAndTimeSheetHours;
	}

	public String getDay2CheckInAndTimeSheetHours() {
		return day2CheckInAndTimeSheetHours;
	}

	public void setDay2CheckInAndTimeSheetHours(String day2CheckInAndTimeSheetHours) {
		this.day2CheckInAndTimeSheetHours = day2CheckInAndTimeSheetHours;
	}

	public String getDay3CheckInAndTimeSheetHours() {
		return day3CheckInAndTimeSheetHours;
	}

	public void setDay3CheckInAndTimeSheetHours(String day3CheckInAndTimeSheetHours) {
		this.day3CheckInAndTimeSheetHours = day3CheckInAndTimeSheetHours;
	}

	public String getDay4CheckInAndTimeSheetHours() {
		return day4CheckInAndTimeSheetHours;
	}

	public void setDay4CheckInAndTimeSheetHours(String day4CheckInAndTimeSheetHours) {
		this.day4CheckInAndTimeSheetHours = day4CheckInAndTimeSheetHours;
	}

	public String getDay5CheckInAndTimeSheetHours() {
		return day5CheckInAndTimeSheetHours;
	}

	public void setDay5CheckInAndTimeSheetHours(String day5CheckInAndTimeSheetHours) {
		this.day5CheckInAndTimeSheetHours = day5CheckInAndTimeSheetHours;
	}

	public String getDay6CheckInAndTimeSheetHours() {
		return day6CheckInAndTimeSheetHours;
	}

	public void setDay6CheckInAndTimeSheetHours(String day6CheckInAndTimeSheetHours) {
		this.day6CheckInAndTimeSheetHours = day6CheckInAndTimeSheetHours;
	}

	public String getDay7CheckInAndTimeSheetHours() {
		return day7CheckInAndTimeSheetHours;
	}

	public void setDay7CheckInAndTimeSheetHours(String day7CheckInAndTimeSheetHours) {
		this.day7CheckInAndTimeSheetHours = day7CheckInAndTimeSheetHours;
	}

}
