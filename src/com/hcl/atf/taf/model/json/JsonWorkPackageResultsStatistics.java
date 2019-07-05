package com.hcl.atf.taf.model.json;

import com.fasterxml.jackson.annotation.JsonProperty;

public class JsonWorkPackageResultsStatistics implements java.io.Serializable{
	
	private static final long serialVersionUID = 1L;

	//Report related fields
	@JsonProperty	
	private Integer workPackageId;
	@JsonProperty	
	private String workPackageName;
	@JsonProperty	
	private Integer shiftId;
	@JsonProperty	
	private String shiftName;
	@JsonProperty	
	private String actualExecutionDate;
	@JsonProperty	
	private Integer testLeadId;
	@JsonProperty	
	private String testLeadName;
	@JsonProperty	
	private Integer testerId;
	@JsonProperty	
	private String testerName;
	@JsonProperty	
	private Integer testCasesCount;
	@JsonProperty	
	private Integer defectsCount;
	@JsonProperty	
	private String hoursToday;
	@JsonProperty	
	private Integer actualShiftId;

	//Rating related fields
	@JsonProperty	
	private Integer ratings;
	@JsonProperty	
	private String raterComments;
	@JsonProperty	
	private Integer ratedByUserId;
	@JsonProperty	
	private String ratedByUserName;
	@JsonProperty	
	private String ratedOn;
	@JsonProperty	
	private Integer isRatingApproved;
	@JsonProperty	
	private Integer ratingApprovedByUserId;
	@JsonProperty	
	private String ratingApprovedByUserName;
	@JsonProperty	
	private String ratingApprovedOn;
	@JsonProperty	
	private String ratingApproverComments;
	
	
	public JsonWorkPackageResultsStatistics() {

		this.workPackageId = new Integer(0);
		this.workPackageName = new String();
		this.shiftId = new Integer(0);
		this.shiftName = new String();
		this.actualExecutionDate = new String();
		this.testLeadId = new Integer(0);
		this.testLeadName = new String();
		this.testerId = new Integer(0);
		this.testerName = new String();
		this.testCasesCount = new Integer(0);
		this.defectsCount = new Integer(0);
		this.hoursToday = new String();
		this.actualShiftId = new Integer(0);
		this.ratings = new Integer(0);
		this.raterComments = new String();
	}


	public Integer getIsRatingApproved() {
		return isRatingApproved;
	}


	public void setIsRatingApproved(Integer isRatingApproved) {
		this.isRatingApproved = isRatingApproved;
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


	public String getActualExecutionDate() {
		return actualExecutionDate;
	}


	public void setActualExecutionDate(String actualExecutionDate) {
		this.actualExecutionDate = actualExecutionDate;
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


	public Integer getTestCasesCount() {
		return testCasesCount;
	}


	public void setTestCasesCount(Integer testCasesCount) {
		this.testCasesCount = testCasesCount;
	}


	public Integer getDefectsCount() {
		return defectsCount;
	}


	public void setDefectsCount(Integer defectsCount) {
		this.defectsCount = defectsCount;
	}


	public String getHoursToday() {
		return hoursToday;
	}


	public void setHoursToday(String hoursToday) {
		this.hoursToday = hoursToday;
	}


	public Integer getActualShiftId() {
		return actualShiftId;
	}


	public void setActualShiftId(Integer actualShiftId) {
		this.actualShiftId = actualShiftId;
	}


	public Integer getRatings() {
		return ratings;
	}


	public void setRatings(Integer ratings) {
		this.ratings = ratings;
	}


	public String getRaterComments() {
		return raterComments;
	}


	public void setRaterComments(String raterComments) {
		this.raterComments = raterComments;
	}


	public Integer getRatedByUserId() {
		return ratedByUserId;
	}


	public void setRatedByUserId(Integer ratedByUserId) {
		this.ratedByUserId = ratedByUserId;
	}


	public String getRatedByUserName() {
		return ratedByUserName;
	}


	public void setRatedByUserName(String ratedByUserName) {
		this.ratedByUserName = ratedByUserName;
	}


	public String getRatedOn() {
		return ratedOn;
	}


	public void setRatedOn(String ratedOn) {
		this.ratedOn = ratedOn;
	}


	public Integer getRatingApprovedByUserId() {
		return ratingApprovedByUserId;
	}


	public void setRatingApprovedByUserId(Integer ratingApprovedByUserId) {
		this.ratingApprovedByUserId = ratingApprovedByUserId;
	}


	public String getRatingApprovedByUserName() {
		return ratingApprovedByUserName;
	}


	public void setRatingApprovedByUserName(String ratingApprovedByUserName) {
		this.ratingApprovedByUserName = ratingApprovedByUserName;
	}


	public String getRatingApprovedOn() {
		return ratingApprovedOn;
	}


	public void setRatingApprovedOn(String ratingApprovedOn) {
		this.ratingApprovedOn = ratingApprovedOn;
	}


	public String getRatingApproverComments() {
		return ratingApproverComments;
	}


	public void setRatingApproverComments(String ratingApproverComments) {
		this.ratingApproverComments = ratingApproverComments;
	}


	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
