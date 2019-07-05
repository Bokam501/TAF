package com.hcl.atf.taf.model.json;

import com.fasterxml.jackson.annotation.JsonProperty;

public class JsonTimeSheetStatistics implements java.io.Serializable{
	
	private static final long serialVersionUID = 1L;
	
	@JsonProperty	
	private Integer testLeadId;
	@JsonProperty	
	private String testLeadName;
	@JsonProperty
	private String actualExecutionDate;
	@JsonProperty	
	private Integer shiftId;
	@JsonProperty	
	private String shiftName;
	@JsonProperty
	private String shiftStartDateTime;
	@JsonProperty
	private String shiftEndDateTime;
	@JsonProperty
	private String shiftDuration;
	@JsonProperty
	private String testerBarCode;
	@JsonProperty
	private String customerCode; //FieldName in report: customerId
	@JsonProperty	
	private Integer testerId;
	@JsonProperty	
	private String testerName;
	@JsonProperty
	private String fullNameLF;
	@JsonProperty	
	private String vendorName;
	@JsonProperty
	private String userStatus;
	@JsonProperty
	private String testFactoryName;
	@JsonProperty
	private String productId;
	@JsonProperty
	private String productName;
	@JsonProperty
	private String projectCode;
	@JsonProperty	
	private String productResourceShiftCheckInDateTime;
	@JsonProperty	
	private String productResourceShiftCheckOutDateTime;
	@JsonProperty
	private String productResourceShiftWorkDuration;  // Used for Product Work Duration hours
	@JsonProperty	
	private Integer productResourceShiftCheckInId;
	//Not relevant if report generated on weekly basis, only relevant if generated on daily basis
	@JsonProperty
	private String nextDayConfirmation;
	@JsonProperty
	private String attendanceStatus;
	@JsonProperty
	private String attendanceStartDateTime;
	@JsonProperty
	private String attendanceEndDateTime;
	@JsonProperty
	private String attendanceDuration;
	@JsonProperty
	private String notes;
	
	@JsonProperty
	private Integer workPackageId;
	@JsonProperty	
	private String workPackageName;
	@JsonProperty
	private String roleName;
	@JsonProperty
	private String roleTier;
	@JsonProperty
	private String lab;
	@JsonProperty
	private String station;
	@JsonProperty
	private String breaksHours; //(Field Added, was not there in initial requirement)
	@JsonProperty
	private String unAuthorisedBreaksHours; //(Field Added, was not there in initial requirement)
	@JsonProperty
	private String lunchHours; 
	@JsonProperty
	private String unAuthorisedLunchHours;
	@JsonProperty
	private String WorkPackageShiftHours; //(Field Added, was not there in initial requirement)
	@JsonProperty
	private String WorkPackageShiftNetHours; //(Field Added, was not there in initial requirement)
	
	@JsonProperty
	private String WorkPackageShiftStandardHours; //(Field Added, was not there in initial requirement)
	@JsonProperty
	private String WorkPackageShiftOverTimeHours; //(Field Added, was not there in initial requirement)
	
	@JsonProperty
	private String shiftTotalWorkHours;	//TotalShiftDuration field in initial requirement
	@JsonProperty
	private String shiftStandardHours; //(Field Added, was not there in initial requirement)
	@JsonProperty
	private String shiftOverTimeHours; //(Field Added, was not there in initial requirement)
	
	@JsonProperty
	private String dailyTotalWorkHours; //totalDailyDuration field
	@JsonProperty
	private String dailyStandardHours;
	@JsonProperty
	private String dailyOverTimeHours;
	
	private String payrollDailyTotalWorkHours; //payrollTotalDailyDuration field
											   // Minutes in 100 decimal format. Eg. .45hrs = .75hrs in decimal format
	@JsonProperty
	private String weeklyTotalWorkHours; //overallTimeCalculations field in the initial requirement
	@JsonProperty
	private String weeklyOverTimeHours; //overTime field in the initial requirement
	@JsonProperty
	private String weeklyStandardHours; //netWeekHours field in the initial requirement
	@JsonProperty
	private String projectTimePerWeek;
	@JsonProperty
	private String payrollWeeklyTotalWorkHours;
	@JsonProperty
	private String payrollWeeklyStandardHours; //payrollNetWeekHours field in the initial requirement
	@JsonProperty
	private String payrollWeeklyOverTimeHours; //PayrollOverTime field in the initial requirement
	@JsonProperty
	private String payrollWeeklyHoursRemaining;
	@JsonProperty
	private String weekStartDate;
	@JsonProperty
	private String dayOfWeek;
	@JsonProperty
	private String monthHalf;
	@JsonProperty
	private String costCenter;
	@JsonProperty
	private String errorCheck;
	@JsonProperty
	private String currentHighestRank;

	public JsonTimeSheetStatistics() {

		this.workPackageId = new Integer(0);
		this.workPackageName = new String();
		this.shiftId = new Integer(0);
		this.shiftName = new String();
		this.actualExecutionDate = new String();
		this.testLeadId = new Integer(0);
		this.testLeadName = new String();
		this.testerId = new Integer(0);
		this.testerName = new String();
		this.vendorName = new String();
	}

	public Integer getTestLeadId() {
		return testLeadId;
	}

	public void setTestLeadId(Integer testLeadId) {
		this.testLeadId = testLeadId;
	}

	public String getTestLeadName() {
		return testLeadName;
	}

	public void setTestLeadName(String testLeadName) {
		this.testLeadName = testLeadName;
	}

	public String getActualExecutionDate() {
		return actualExecutionDate;
	}

	public void setActualExecutionDate(String actualExecutionDate) {
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

	public String getShiftStartDateTime() {
		return shiftStartDateTime;
	}

	public void setShiftStartDateTime(String shiftStartDateTime) {
		this.shiftStartDateTime = shiftStartDateTime;
	}

	public String getShiftEndDateTime() {
		return shiftEndDateTime;
	}

	public void setShiftEndDateTime(String shiftEndDateTime) {
		this.shiftEndDateTime = shiftEndDateTime;
	}

	public String getShiftDuration() {
		return shiftDuration;
	}

	public void setShiftDuration(String shiftDuration) {
		this.shiftDuration = shiftDuration;
	}

	public String getTesterBarCode() {
		return testerBarCode;
	}

	public void setTesterBarCode(String testerBarCode) {
		this.testerBarCode = testerBarCode;
	}

	public String getCustomerCode() {
		return customerCode;
	}

	public void setCustomerCode(String customerCode) {
		this.customerCode = customerCode;
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

	public String getFullNameLF() {
		return fullNameLF;
	}

	public void setFullNameLF(String fullNameLF) {
		this.fullNameLF = fullNameLF;
	}

	public String getVendorName() {
		return vendorName;
	}

	public void setVendorName(String vendorName) {
		this.vendorName = vendorName;
	}

	public String getUserStatus() {
		return userStatus;
	}

	public void setUserStatus(String userStatus) {
		this.userStatus = userStatus;
	}

	public String getTestFactoryName() {
		return testFactoryName;
	}

	public void setTestFactoryName(String testFactoryName) {
		this.testFactoryName = testFactoryName;
	}

	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
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

	public String getProductResourceShiftCheckInDateTime() {
		return productResourceShiftCheckInDateTime;
	}

	public void setProductResourceShiftCheckInDateTime(
			String productResourceShiftCheckInDateTime) {
		this.productResourceShiftCheckInDateTime = productResourceShiftCheckInDateTime;
	}

	public String getProductResourceShiftCheckOutDateTime() {
		return productResourceShiftCheckOutDateTime;
	}

	public void setProductResourceShiftCheckOutDateTime(
			String productResourceShiftCheckOutDateTime) {
		this.productResourceShiftCheckOutDateTime = productResourceShiftCheckOutDateTime;
	}

	public String getProductResourceShiftWorkDuration() {
		return productResourceShiftWorkDuration;
	}

	public void setProductResourceShiftWorkDuration(
			String productResourceShiftWorkDuration) {
		this.productResourceShiftWorkDuration = productResourceShiftWorkDuration;
	}

	public Integer getProductResourceShiftCheckInId() {
		return productResourceShiftCheckInId;
	}

	public void setProductResourceShiftCheckInId(
			Integer productResourceShiftCheckInId) {
		this.productResourceShiftCheckInId = productResourceShiftCheckInId;
	}

	public String getNextDayConfirmation() {
		return nextDayConfirmation;
	}

	public void setNextDayConfirmation(String nextDayConfirmation) {
		this.nextDayConfirmation = nextDayConfirmation;
	}

	public String getAttendanceStatus() {
		return attendanceStatus;
	}

	public void setAttendanceStatus(String attendanceStatus) {
		this.attendanceStatus = attendanceStatus;
	}

	public String getAttendanceStartDateTime() {
		return attendanceStartDateTime;
	}

	public void setAttendanceStartDateTime(String attendanceStartDateTime) {
		this.attendanceStartDateTime = attendanceStartDateTime;
	}

	public String getAttendanceEndDateTime() {
		return attendanceEndDateTime;
	}

	public void setAttendanceEndDateTime(String attendanceEndDateTime) {
		this.attendanceEndDateTime = attendanceEndDateTime;
	}

	public String getAttendanceDuration() {
		return attendanceDuration;
	}

	public void setAttendanceDuration(String attendanceDuration) {
		this.attendanceDuration = attendanceDuration;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
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

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public String getRoleTier() {
		return roleTier;
	}

	public void setRoleTier(String roleTier) {
		this.roleTier = roleTier;
	}

	public String getLab() {
		return lab;
	}

	public void setLab(String lab) {
		this.lab = lab;
	}

	public String getStation() {
		return station;
	}

	public void setStation(String station) {
		this.station = station;
	}

	public String getBreaksHours() {
		return breaksHours;
	}

	public void setBreaksHours(String breaksHours) {
		this.breaksHours = breaksHours;
	}

	public String getUnAuthorisedBreaksHours() {
		return unAuthorisedBreaksHours;
	}

	public void setUnAuthorisedBreaksHours(String unAuthorisedBreaksHours) {
		this.unAuthorisedBreaksHours = unAuthorisedBreaksHours;
	}

	public String getLunchHours() {
		return lunchHours;
	}

	public void setLunchHours(String lunchHours) {
		this.lunchHours = lunchHours;
	}

	public String getUnAuthorisedLunchHours() {
		return unAuthorisedLunchHours;
	}

	public void setUnAuthorisedLunchHours(String unAuthorisedLunchHours) {
		this.unAuthorisedLunchHours = unAuthorisedLunchHours;
	}

	public String getWorkPackageShiftHours() {
		return WorkPackageShiftHours;
	}

	public void setWorkPackageShiftHours(String workPackageShiftHours) {
		WorkPackageShiftHours = workPackageShiftHours;
	}

	public String getWorkPackageShiftNetHours() {
		return WorkPackageShiftNetHours;
	}

	public void setWorkPackageShiftNetHours(String workPackageShiftNetHours) {
		WorkPackageShiftNetHours = workPackageShiftNetHours;
	}

	public String getWorkPackageShiftStandardHours() {
		return WorkPackageShiftStandardHours;
	}

	public void setWorkPackageShiftStandardHours(
			String workPackageShiftStandardHours) {
		WorkPackageShiftStandardHours = workPackageShiftStandardHours;
	}

	public String getWorkPackageShiftOverTimeHours() {
		return WorkPackageShiftOverTimeHours;
	}

	public void setWorkPackageShiftOverTimeHours(
			String workPackageShiftOverTimeHours) {
		WorkPackageShiftOverTimeHours = workPackageShiftOverTimeHours;
	}

	public String getShiftTotalWorkHours() {
		return shiftTotalWorkHours;
	}

	public void setShiftTotalWorkHours(String shiftTotalWorkHours) {
		this.shiftTotalWorkHours = shiftTotalWorkHours;
	}

	public String getShiftStandardHours() {
		return shiftStandardHours;
	}

	public void setShiftStandardHours(String shiftStandardHours) {
		this.shiftStandardHours = shiftStandardHours;
	}

	public String getShiftOverTimeHours() {
		return shiftOverTimeHours;
	}

	public void setShiftOverTimeHours(String shiftOverTimeHours) {
		this.shiftOverTimeHours = shiftOverTimeHours;
	}

	public String getDailyTotalWorkHours() {
		return dailyTotalWorkHours;
	}

	public void setDailyTotalWorkHours(String dailyTotalWorkHours) {
		this.dailyTotalWorkHours = dailyTotalWorkHours;
	}

	public String getDailyStandardHours() {
		return dailyStandardHours;
	}

	public void setDailyStandardHours(String dailyStandardHours) {
		this.dailyStandardHours = dailyStandardHours;
	}

	public String getDailyOverTimeHours() {
		return dailyOverTimeHours;
	}

	public void setDailyOverTimeHours(String dailyOverTimeHours) {
		this.dailyOverTimeHours = dailyOverTimeHours;
	}

	public String getPayrollDailyTotalWorkHours() {
		return payrollDailyTotalWorkHours;
	}

	public void setPayrollDailyTotalWorkHours(String payrollDailyTotalWorkHours) {
		this.payrollDailyTotalWorkHours = payrollDailyTotalWorkHours;
	}

	public String getWeeklyTotalWorkHours() {
		return weeklyTotalWorkHours;
	}

	public void setWeeklyTotalWorkHours(String weeklyTotalWorkHours) {
		this.weeklyTotalWorkHours = weeklyTotalWorkHours;
	}

	public String getWeeklyOverTimeHours() {
		return weeklyOverTimeHours;
	}

	public void setWeeklyOverTimeHours(String weeklyOverTimeHours) {
		this.weeklyOverTimeHours = weeklyOverTimeHours;
	}

	public String getWeeklyStandardHours() {
		return weeklyStandardHours;
	}

	public void setWeeklyStandardHours(String weeklyStandardHours) {
		this.weeklyStandardHours = weeklyStandardHours;
	}

	public String getProjectTimePerWeek() {
		return projectTimePerWeek;
	}

	public void setProjectTimePerWeek(String projectTimePerWeek) {
		this.projectTimePerWeek = projectTimePerWeek;
	}

	public String getPayrollWeeklyTotalWorkHours() {
		return payrollWeeklyTotalWorkHours;
	}

	public void setPayrollWeeklyTotalWorkHours(String payrollWeeklyTotalWorkHours) {
		this.payrollWeeklyTotalWorkHours = payrollWeeklyTotalWorkHours;
	}

	public String getPayrollWeeklyStandardHours() {
		return payrollWeeklyStandardHours;
	}

	public void setPayrollWeeklyStandardHours(String payrollWeeklyStandardHours) {
		this.payrollWeeklyStandardHours = payrollWeeklyStandardHours;
	}

	public String getPayrollWeeklyOverTimeHours() {
		return payrollWeeklyOverTimeHours;
	}

	public void setPayrollWeeklyOverTimeHours(String payrollWeeklyOverTimeHours) {
		this.payrollWeeklyOverTimeHours = payrollWeeklyOverTimeHours;
	}

	public String getPayrollWeeklyHoursRemaining() {
		return payrollWeeklyHoursRemaining;
	}

	public void setPayrollWeeklyHoursRemaining(String payrollWeeklyHoursRemaining) {
		this.payrollWeeklyHoursRemaining = payrollWeeklyHoursRemaining;
	}

	public String getWeekStartDate() {
		return weekStartDate;
	}

	public void setWeekStartDate(String weekStartDate) {
		this.weekStartDate = weekStartDate;
	}

	public String getDayOfWeek() {
		return dayOfWeek;
	}

	public void setDayOfWeek(String dayOfWeek) {
		this.dayOfWeek = dayOfWeek;
	}

	public String getMonthHalf() {
		return monthHalf;
	}

	public void setMonthHalf(String monthHalf) {
		this.monthHalf = monthHalf;
	}

	public String getCostCenter() {
		return costCenter;
	}

	public void setCostCenter(String costCenter) {
		this.costCenter = costCenter;
	}

	public String getErrorCheck() {
		return errorCheck;
	}

	public void setErrorCheck(String errorCheck) {
		this.errorCheck = errorCheck;
	}

	public String getCurrentHighestRank() {
		return currentHighestRank;
	}

	public void setCurrentHighestRank(String currentHighestRank) {
		this.currentHighestRank = currentHighestRank;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
