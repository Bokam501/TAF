package com.hcl.atf.taf.model.dto;

import java.util.Date;

import com.hcl.atf.taf.model.DefectApprovalStatusMaster;


public class DefectWeeklyReportDTO {
	private Integer id;	
	private Integer totalLiveReportCount;
	private Integer totalWebReportCount;
	private Integer day1LiveReportCount;
	private Integer day2LiveReportCount;
	private Integer day3LiveReportCount;
	private Integer day4LiveReportCount;
	private Integer day5LiveReportCount;
	private Integer day6LiveReportCount;
	private Integer day7LiveReportCount;
	private Integer day1WebReportCount;
	private Integer day2WebReportCount;
	private Integer day3WebReportCount;
	private Integer day4WebReportCount;
	private Integer day5WebReportCount;
	private Integer day6WebReportCount;
	private Integer day7WebReportCount;
	private Date bugCreatedDate;
	private Integer bugsCount;
	private Integer foundInStageId;
	private String foundInStageName;
	private Integer defectsApprovalStatusId;
	private String defectsApprovalStatusName;
	private Integer weekNo;
	private Integer defectTyepId;
	private String defectTypeName; 
	private Integer dayNumberOfWeek;
	private DefectApprovalStatusMaster defectApprovalStatus;
	
	private Integer dateWiseDefectDetailsArry[][];
	
	public DefectWeeklyReportDTO(){
		
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getTotalLiveReportCount() {
		return totalLiveReportCount;
	}

	public void setTotalLiveReportCount(Integer totalLiveReportCount) {
		this.totalLiveReportCount = totalLiveReportCount;
	}

	public Integer getTotalWebReportCount() {
		return totalWebReportCount;
	}

	public void setTotalWebReportCount(Integer totalWebReportCount) {
		this.totalWebReportCount = totalWebReportCount;
	}

	public Integer getDay1LiveReportCount() {
		return day1LiveReportCount;
	}

	public void setDay1LiveReportCount(Integer day1LiveReportCount) {
		this.day1LiveReportCount = day1LiveReportCount;
	}

	public Integer getDay2LiveReportCount() {
		return day2LiveReportCount;
	}

	public void setDay2LiveReportCount(Integer day2LiveReportCount) {
		this.day2LiveReportCount = day2LiveReportCount;
	}

	public Integer getDay3LiveReportCount() {
		return day3LiveReportCount;
	}

	public void setDay3LiveReportCount(Integer day3LiveReportCount) {
		this.day3LiveReportCount = day3LiveReportCount;
	}

	public Integer getDay4LiveReportCount() {
		return day4LiveReportCount;
	}

	public void setDay4LiveReportCount(Integer day4LiveReportCount) {
		this.day4LiveReportCount = day4LiveReportCount;
	}

	public Integer getDay5LiveReportCount() {
		return day5LiveReportCount;
	}

	public void setDay5LiveReportCount(Integer day5LiveReportCount) {
		this.day5LiveReportCount = day5LiveReportCount;
	}

	public Integer getDay6LiveReportCount() {
		return day6LiveReportCount;
	}

	public void setDay6LiveReportCount(Integer day6LiveReportCount) {
		this.day6LiveReportCount = day6LiveReportCount;
	}

	public Integer getDay7LiveReportCount() {
		return day7LiveReportCount;
	}

	public void setDay7LiveReportCount(Integer day7LiveReportCount) {
		this.day7LiveReportCount = day7LiveReportCount;
	}

	public Integer getDay1WebReportCount() {
		return day1WebReportCount;
	}

	public void setDay1WebReportCount(Integer day1WebReportCount) {
		this.day1WebReportCount = day1WebReportCount;
	}

	public Integer getDay2WebReportCount() {
		return day2WebReportCount;
	}

	public void setDay2WebReportCount(Integer day2WebReportCount) {
		this.day2WebReportCount = day2WebReportCount;
	}

	public Integer getDay3WebReportCount() {
		return day3WebReportCount;
	}

	public void setDay3WebReportCount(Integer day3WebReportCount) {
		this.day3WebReportCount = day3WebReportCount;
	}

	public Integer getDay4WebReportCount() {
		return day4WebReportCount;
	}

	public void setDay4WebReportCount(Integer day4WebReportCount) {
		this.day4WebReportCount = day4WebReportCount;
	}

	public Integer getDay5WebReportCount() {
		return day5WebReportCount;
	}

	public void setDay5WebReportCount(Integer day5WebReportCount) {
		this.day5WebReportCount = day5WebReportCount;
	}

	public Integer getDay6WebReportCount() {
		return day6WebReportCount;
	}

	public void setDay6WebReportCount(Integer day6WebReportCount) {
		this.day6WebReportCount = day6WebReportCount;
	}

	public Integer getDay7WebReportCount() {
		return day7WebReportCount;
	}

	public void setDay7WebReportCount(Integer day7WebReportCount) {
		this.day7WebReportCount = day7WebReportCount;
	}

	public Date getBugCreatedDate() {
		return bugCreatedDate;
	}

	public void setBugCreatedDate(Date bugCreatedDate) {
		this.bugCreatedDate = bugCreatedDate;
	}

	public Integer getBugsCount() {
		return bugsCount;
	}

	public void setBugsCount(Integer bugsCount) {
		this.bugsCount = bugsCount;
	}

	public Integer getFoundInStageId() {
		return foundInStageId;
	}

	public void setFoundInStageId(Integer foundInStageId) {
		this.foundInStageId = foundInStageId;
	}

	public String getFoundInStageName() {
		return foundInStageName;
	}

	public void setFoundInStageName(String foundInStageName) {
		this.foundInStageName = foundInStageName;
	}

	public Integer getDefectsApprovalStatusId() {
		return defectsApprovalStatusId;
	}

	public void setDefectsApprovalStatusId(Integer defectsApprovalStatusId) {
		this.defectsApprovalStatusId = defectsApprovalStatusId;
	}

	public String getDefectsApprovalStatusName() {
		return defectsApprovalStatusName;
	}

	public void setDefectsApprovalStatusName(String defectsApprovalStatusName) {
		this.defectsApprovalStatusName = defectsApprovalStatusName;
	}

	public Integer getWeekNo() {
		return weekNo;
	}

	public void setWeekNo(Integer weekNo) {
		this.weekNo = weekNo;
	}

	public Integer getDefectTyepId() {
		return defectTyepId;
	}

	public void setDefectTyepId(Integer defectTyepId) {
		this.defectTyepId = defectTyepId;
	}

	public String getDefectTypeName() {
		return defectTypeName;
	}

	public void setDefectTypeName(String defectTypeName) {
		this.defectTypeName = defectTypeName;
	}

	public Integer[][] getDateWiseDefectDetailsArry() {
		return dateWiseDefectDetailsArry;
	}

	public void setDateWiseDefectDetailsArry(Integer[][] dateWiseDefectDetailsArry) {
		this.dateWiseDefectDetailsArry = dateWiseDefectDetailsArry;
	}

	public Integer getDayNumberOfWeek() {
		return dayNumberOfWeek;
	}

	public void setDayNumberOfWeek(Integer dayNumberOfWeek) {
		this.dayNumberOfWeek = dayNumberOfWeek;
	}

	public DefectApprovalStatusMaster getDefectApprovalStatus() {
		return defectApprovalStatus;
	}

	public void setDefectApprovalStatus(
			DefectApprovalStatusMaster defectApprovalStatus) {
		this.defectApprovalStatus = defectApprovalStatus;
	}

}
