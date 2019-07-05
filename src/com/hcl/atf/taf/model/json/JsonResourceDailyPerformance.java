package com.hcl.atf.taf.model.json;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.hcl.atf.taf.controller.utilities.DateUtility;
import com.hcl.atf.taf.model.ResourceDailyPerformance;

public class JsonResourceDailyPerformance {
	
	@JsonProperty
	private Integer resourcePerformanceId;
	@JsonProperty
	private Integer userId;
	@JsonProperty
	private String userName;
	@JsonProperty
	private Integer workPackageId;
	@JsonProperty
	private String workPackageName;
	@JsonProperty
	private Integer ratedByUserId;
	@JsonProperty
	private String ratedByUserName;
	@JsonProperty
	private String raterComments;
	@JsonProperty
	private String ratedOn;
	@JsonProperty
	private Integer actualShiftId;
	@JsonProperty
	private Integer performanceLevelId;
	@JsonProperty
	private String performanceLevelName;
	@JsonProperty
	private String workDate;
	@JsonProperty
	private Integer approvedByUserId;
	@JsonProperty
	private String approvedByUserName;
	@JsonProperty
	private String approverComments;
	@JsonProperty
	private String approvedOn;
	@JsonProperty
	private Integer shiftId;
	@JsonProperty
	private String shiftName;
	
	public JsonResourceDailyPerformance(){
		
	}
	
	public JsonResourceDailyPerformance(ResourceDailyPerformance resourceDailyPerformance){
		this.resourcePerformanceId = resourceDailyPerformance.getResourcePerformanceId();
		if(resourceDailyPerformance.getUser() != null){
			this.userId = resourceDailyPerformance.getUser().getUserId();
			this.userName = resourceDailyPerformance.getUser().getLoginId();
		}		
		if(resourceDailyPerformance.getWorkPackage() != null){
			this.workPackageId = resourceDailyPerformance.getWorkPackage().getWorkPackageId();
			this.workPackageName = resourceDailyPerformance.getWorkPackage().getName();
		}
		if(resourceDailyPerformance.getRatedByUser() != null){
			this.ratedByUserId = resourceDailyPerformance.getRatedByUser().getUserId();
			this.ratedByUserName = resourceDailyPerformance.getRatedByUser().getLoginId();
			this.raterComments = resourceDailyPerformance.getRaterComments();
			if(resourceDailyPerformance.getRatedOn() != null){
				this.ratedOn = DateUtility.dateToStringInSecond(resourceDailyPerformance.getRatedOn());
			}
		}
		if(resourceDailyPerformance.getApprovedByUser() != null){
			this.approvedByUserId = resourceDailyPerformance.getApprovedByUser().getUserId();
			this.approvedByUserName = resourceDailyPerformance.getApprovedByUser().getLoginId();
			this.approverComments = resourceDailyPerformance.getApproverComments();
			if(resourceDailyPerformance.getApprovedOn() != null){
				this.approvedOn = DateUtility.dateToStringInSecond(resourceDailyPerformance.getApprovedOn());
			}
		}
		
		if(resourceDailyPerformance.getPerformanceLevel() != null){
			this.performanceLevelId = resourceDailyPerformance.getPerformanceLevel().getPerformanceLevelId();
			this.performanceLevelName = resourceDailyPerformance.getPerformanceLevel().getLevelName();
		}
		
		if(resourceDailyPerformance.getActualShift() != null){
			this.actualShiftId  = resourceDailyPerformance.getActualShift().getActualShiftId();
			if(resourceDailyPerformance.getActualShift().getShift() != null){
				this.shiftId  = resourceDailyPerformance.getActualShift().getShift().getShiftId();
				this.shiftName  = resourceDailyPerformance.getActualShift().getShift().getShiftName();
			}
		}
		
		if(resourceDailyPerformance.getWorkDate() != null){
			this.workDate = DateUtility.dateToStringInSecond(resourceDailyPerformance.getWorkDate());
		}
		if(resourceDailyPerformance.getWorkDate() != null){
			this.workDate = DateUtility.dateToStringInSecond(resourceDailyPerformance.getWorkDate());
		}
		
	}
	
	public Integer getResourcePerformanceId() {
		return resourcePerformanceId;
	}
	public void setResourcePerformanceId(Integer resourcePerformanceId) {
		this.resourcePerformanceId = resourcePerformanceId;
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
	public String getRaterComments() {
		return raterComments;
	}
	public void setRaterComments(String raterComments) {
		this.raterComments = raterComments;
	}
	public String getRatedOn() {
		return ratedOn;
	}
	public void setRatedOn(String ratedOn) {
		this.ratedOn = ratedOn;
	}
	public Integer getActualShiftId() {
		return actualShiftId;
	}
	public void setActualShiftId(Integer actualShiftId) {
		this.actualShiftId = actualShiftId;
	}
	public Integer getPerformanceLevelId() {
		return performanceLevelId;
	}
	public void setPerformanceLevelId(Integer performanceLevelId) {
		this.performanceLevelId = performanceLevelId;
	}
	public String getPerformanceLevelName() {
		return performanceLevelName;
	}
	public void setPerformanceLevelName(String performanceLevelName) {
		this.performanceLevelName = performanceLevelName;
	}
	public String getWorkDate() {
		return workDate;
	}
	public void setWorkDate(String workDate) {
		this.workDate = workDate;
	}
	public Integer getApprovedByUserId() {
		return approvedByUserId;
	}
	public void setApprovedByUserId(Integer approvedByUserId) {
		this.approvedByUserId = approvedByUserId;
	}
	public String getApprovedByUserName() {
		return approvedByUserName;
	}
	public void setApprovedByUserName(String approvedByUserName) {
		this.approvedByUserName = approvedByUserName;
	}
	public String getApproverComments() {
		return approverComments;
	}
	public void setApproverComments(String approverComments) {
		this.approverComments = approverComments;
	}
	public String getApprovedOn() {
		return approvedOn;
	}
	public void setApprovedOn(String approvedOn) {
		this.approvedOn = approvedOn;
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
	
}
