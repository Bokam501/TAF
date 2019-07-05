package com.hcl.atf.taf.model.json;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.hcl.atf.taf.controller.utilities.DateUtility;
import com.hcl.atf.taf.model.ActualShift;

public class JsonActualShift implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	private static final Log log = LogFactory.getLog(JsonActualShift.class);
	
	@JsonProperty
	private Integer actualShiftId;
	@JsonProperty
	private Integer testFactoryId;
	@JsonProperty
	private String workDate;
	@JsonProperty
	private Integer shiftId;
	@JsonProperty
	private String startTime;
	@JsonProperty
	private String endTime;
	@JsonProperty
	private String startTimeRemarks;
	@JsonProperty
	private String endTimeRemarks;
	@JsonProperty
	private String shiftName;
	@JsonProperty
	private String testFactoryName;
	@JsonProperty
	private String actualDisplayName;;
	public JsonActualShift() {
	}
	
	
	public JsonActualShift(ActualShift actualShift){
		this.actualShiftId = actualShift.getActualShiftId();
		this.workDate = DateUtility.dateformatWithOutTime(actualShift.getWorkdate());
		log.info("----- Constructor ------------");
		log.info("actualShift.getWorkdate() --"+actualShift.getWorkdate());
		log.info("this.workDate-------"+this.workDate);
		log.info("----- Constructor --------Ends----");
		this.shiftId = actualShift.getShift().getShiftId();
		this.shiftName = actualShift.getShift().getShiftName();
		this.testFactoryName=actualShift.getShift().getTestFactory().getTestFactoryName();
		this.actualDisplayName=" ["+testFactoryName+"] "+shiftName;
		if(actualShift.getStartTime() !=null)
		{
			this.startTime =  DateUtility.dateToStringInSecond(actualShift.getStartTime());
		}
		if(actualShift.getEndTime() !=null)
		{
			this.endTime =  DateUtility.dateToStringInSecond(actualShift.getEndTime());
		}
		if( actualShift.getStartTimeRemarks() !=null){
			this.startTimeRemarks = actualShift.getStartTimeRemarks();
		}
		if( actualShift.getEndTimeRemarks() !=null){
			this.endTimeRemarks = actualShift.getEndTimeRemarks();
		}
		
	}
	
	
	@JsonIgnore
	public ActualShift getActualShift() {
		ActualShift actualShift = new ActualShift();
		log.info("----  Getter  -------------");
		log.info(" Work date---"+this.workDate);
		actualShift.setWorkdate(DateUtility.dateformatWithOutTime(this.workDate));
		log.info("this.workDate --	"+this.workDate);
		log.info("actualShift.getWorkdate() ---	"+actualShift.getWorkdate());
		log.info("----  Getter  ------Ends-------");
		actualShift.setStartTime(DateUtility.toDateInSec(this.startTime));
		actualShift.setEndTime(DateUtility.toDateInSec(this.endTime));
		actualShift.setStartTimeRemarks(this.startTimeRemarks);
		actualShift.setEndTimeRemarks(this.endTimeRemarks);
		
		return actualShift;
	}
	

	public String getActualDisplayName() {
		return actualDisplayName;
	}


	public void setActualDisplayName(String actualDisplayName) {
		this.actualDisplayName = actualDisplayName;
	}


	public String getTestFactoryName() {
		return testFactoryName;
	}


	public void setTestFactoryName(String testFactoryName) {
		this.testFactoryName = testFactoryName;
	}


	public Integer getActualShiftId() {
		return actualShiftId;
	}

	public void setActualShiftId(Integer actualShiftId) {
		this.actualShiftId = actualShiftId;
	}

	public Integer getTestFactoryId() {
		return testFactoryId;
	}

	public void setTestFactoryId(Integer testFactoryId) {
		this.testFactoryId = testFactoryId;
	}

	public String getWorkDate() {
		return workDate;
	}

	public void setWorkDate(String workDate) {
		this.workDate = workDate;
	}

	public Integer getShiftId() {
		return shiftId;
	}

	public void setShiftId(Integer shiftId) {
		this.shiftId = shiftId;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}


	public String getStartTimeRemarks() {
		return startTimeRemarks;
	}


	public void setStartTimeRemarks(String startTimeRemarks) {
		this.startTimeRemarks = startTimeRemarks;
	}


	public String getEndTimeRemarks() {
		return endTimeRemarks;
	}


	public void setEndTimeRemarks(String endTimeRemarks) {
		this.endTimeRemarks = endTimeRemarks;
	}


	public String getShiftName() {
		return shiftName;
	}


	public void setShiftName(String shiftName) {
		this.shiftName = shiftName;
	}

}
