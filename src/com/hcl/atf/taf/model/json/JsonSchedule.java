package com.hcl.atf.taf.model.json;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.hcl.atf.taf.constants.IDPAConstants;
import com.hcl.atf.taf.controller.utilities.DateUtility;
import com.hcl.atf.taf.model.DataExtractorScheduleMaster;
import com.hcl.atf.taf.model.TestRunPlan;

public class JsonSchedule implements java.io.Serializable {

	private static final long serialVersionUID = 352724414128452983L;

	@JsonProperty
	private Integer ownerEntityId;
	@JsonProperty
	private String ownerEntityType;
	@JsonProperty
	private String cronExpression;
	@JsonProperty
	private String startDate;
	@JsonProperty
	private String endDate;
	@JsonProperty
	private List<String> upcomingFireTimes;
	@JsonProperty
	private String upcomingFireDate;
	@JsonProperty
	private String message;

	public JsonSchedule() {
	}
	
	public JsonSchedule(Object scheduleObject) {
		
		if (scheduleObject == null)
			return;
		
		if(scheduleObject instanceof TestRunPlan){
			TestRunPlan testRunPlan = (TestRunPlan) scheduleObject;
			ownerEntityId = testRunPlan.getTestRunPlanId();
			ownerEntityType = IDPAConstants.ENTITY_TEST_RUN_PLAN;
			cronExpression = testRunPlan.getTestRunCronSchedule();
			if(testRunPlan.getTestRunScheduledStartTime() != null){
				startDate = DateUtility.sdfDateformatWithOutTime(testRunPlan.getTestRunScheduledStartTime());
			}
			if(testRunPlan.getTestRunScheduledEndTime() != null){
				endDate = DateUtility.sdfDateformatWithOutTime(testRunPlan.getTestRunScheduledEndTime());
			}
		}else if(scheduleObject instanceof DataExtractorScheduleMaster){
			DataExtractorScheduleMaster dataExtractorScheduleMaster = (DataExtractorScheduleMaster) scheduleObject;
			ownerEntityId = dataExtractorScheduleMaster.getId();
			ownerEntityType = IDPAConstants.DATA_EXTRACTOR;
			cronExpression = dataExtractorScheduleMaster.getCronExpression();
			if(dataExtractorScheduleMaster.getStartDate() != null){
				startDate = DateUtility.sdfDateformatWithOutTime(dataExtractorScheduleMaster.getStartDate());
			}
			if(dataExtractorScheduleMaster.getEndDate() != null){
				endDate = DateUtility.sdfDateformatWithOutTime(dataExtractorScheduleMaster.getEndDate());
			}
		}
		
		
	}

	public Integer getOwnerEntityId() {
		return ownerEntityId;
	}

	public void setOwnerEntityId(Integer ownerEntityId) {
		this.ownerEntityId = ownerEntityId;
	}

	public String getOwnerEntityType() {
		return ownerEntityType;
	}

	public void setOwnerEntityType(String ownerEntityType) {
		this.ownerEntityType = ownerEntityType;
	}

	public String getCronExpression() {
		return cronExpression;
	}

	public void setCronExpression(String cronExpression) {
		this.cronExpression = cronExpression;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public List<String> getUpcomingFireTimes() {
		return upcomingFireTimes;
	}

	public void setUpcomingFireTimes(List<String> upcomingFireTimes) {
		this.upcomingFireTimes = upcomingFireTimes;
	}

	public void setUpcomingFireTimesDates(List<Date> upcomingFireTimesDates) {
		if (upcomingFireTimes == null)
			upcomingFireTimes = new ArrayList<String>();
		if (upcomingFireTimesDates != null && upcomingFireTimesDates.size() > 0) {
			for (Date fireTime : upcomingFireTimesDates)
				upcomingFireTimes.add(DateUtility.dateToStringInSecond(fireTime));
		}
	}

	public String getUpcomingFireDate() {
		return upcomingFireDate;
	}

	public void setUpcomingFireDate(String upcomingFireDate) {
		this.upcomingFireDate = upcomingFireDate;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	

}
