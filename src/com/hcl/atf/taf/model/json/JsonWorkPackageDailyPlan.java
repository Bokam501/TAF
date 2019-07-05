package com.hcl.atf.taf.model.json;


import java.util.Comparator;

import com.fasterxml.jackson.annotation.JsonProperty;


public class JsonWorkPackageDailyPlan implements java.io.Serializable{
	
	private static final long serialVersionUID = 1L;
	
	@JsonProperty	
	private Integer dailyPlanId;
	@JsonProperty
	private int workPackageId;
	@JsonProperty
	private Integer shiftId;
	@JsonProperty	
	private String shiftName;
	@JsonProperty	
	private Integer weekNo;
	
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
	
	public JsonWorkPackageDailyPlan() {

		this.day1ResourceCount = new Integer(0);
		this.day2ResourceCount = new Integer(0);
		this.day3ResourceCount = new Integer(0);
		this.day4ResourceCount = new Integer(0);
		this.day5ResourceCount = new Integer(0);
		this.day6ResourceCount = new Integer(0);
		this.day7ResourceCount = new Integer(0);
	}

	public int getDailyPlanId() {
		return dailyPlanId;
	}

	public void setDailyPlanId(int dailyPlanId) {
		this.dailyPlanId = dailyPlanId;
	}
	
	public int getWorkPackageId() {
		return workPackageId;
	}

	public void setWorkPackageId(int workPackageId) {
		this.workPackageId = workPackageId;
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
	
	public static Comparator<JsonWorkPackageDailyPlan> jsonWorkPackageDailyPlanComparator = new Comparator<JsonWorkPackageDailyPlan>() {

		public int compare(JsonWorkPackageDailyPlan shift1, JsonWorkPackageDailyPlan shift2) {
			String shift1Id = shift1.getShiftName().toUpperCase();
			String shift2Id = shift2.getShiftName().toUpperCase();

			return shift1Id.compareTo(shift2Id);
		}
	};
}
