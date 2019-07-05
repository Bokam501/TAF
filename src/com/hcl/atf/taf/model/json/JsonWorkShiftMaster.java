package com.hcl.atf.taf.model.json;



import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.hcl.atf.taf.controller.utilities.DateUtility;
import com.hcl.atf.taf.model.ShiftTypeMaster;
import com.hcl.atf.taf.model.TestFactory;
import com.hcl.atf.taf.model.WorkShiftMaster;
public class JsonWorkShiftMaster implements java.io.Serializable {

		
	@JsonProperty
	private Integer shiftId;
	@JsonProperty
	private String shiftName;
	@JsonProperty
	private String displayLabel;
	@JsonProperty
	private String description;
	@JsonProperty
	private String startTime;
	@JsonProperty
	private String endTime;
	@JsonProperty
	private Integer status;
	@JsonProperty
	private Integer shiftTypeId;
	@JsonProperty
	private Integer testFactoryId;
	@JsonProperty
	private String testFactoryName;
	@JsonProperty
	private String shiftTypeName;
	

	public JsonWorkShiftMaster() {
	}

	public JsonWorkShiftMaster(WorkShiftMaster workShiftMaster) {
		
		this.shiftId=workShiftMaster.getShiftId();
		this.shiftName=workShiftMaster.getShiftName();
		this.displayLabel=workShiftMaster.getDisplayLabel();
		this.description=workShiftMaster.getDescription();
		if(workShiftMaster.getStartTime()!=null)
			this.startTime=DateUtility.getTimeStampinHHmm(workShiftMaster.getStartTime());
		if(workShiftMaster.getEndTime()!=null)
			this.endTime=DateUtility.getTimeStampinHHmm(workShiftMaster.getEndTime());
		this.status=workShiftMaster.getStatus();
		this.shiftTypeId=workShiftMaster.getShiftType().getShiftTypeId();
		this.testFactoryId=workShiftMaster.getTestFactory().getTestFactoryId();
		this.testFactoryName=workShiftMaster.getTestFactory().getTestFactoryName();
		this.shiftTypeName=workShiftMaster.getShiftType().getShiftName();
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

	public String getDisplayLabel() {
		return displayLabel;
	}

	public void setDisplayLabel(String displayLabel) {
		this.displayLabel = displayLabel;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
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

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}
	
	public Integer getShiftTypeId() {
		return shiftTypeId;
	}

	public void setShiftTypeId(Integer shiftTypeId) {
		this.shiftTypeId = shiftTypeId;
	}

	public Integer getTestFactoryId() {
		return testFactoryId;
	}

	public void setTestFactoryId(Integer testFactoryId) {
		this.testFactoryId = testFactoryId;
	}

	@JsonIgnore
	public WorkShiftMaster getWorkShiftMaster(){
		WorkShiftMaster workShiftMaster=new WorkShiftMaster();
				
		workShiftMaster.setShiftId(this.shiftId);
		workShiftMaster.setShiftName(this.shiftName);
		workShiftMaster.setDisplayLabel(this.displayLabel);
		workShiftMaster.setDescription(this.description);
		if(this.startTime != null && this.startTime != "")
			workShiftMaster.setStartTime(DateUtility.getStringconvertedTime(new Date(), this.startTime));
		if(this.endTime!=null && this.endTime != "")
			workShiftMaster.setEndTime(DateUtility.getStringconvertedTime(new Date(), this.endTime));
		workShiftMaster.setStatus(this.status);
		ShiftTypeMaster shifttypeMaster = new ShiftTypeMaster();
		shifttypeMaster.setShiftTypeId(this.shiftTypeId);
		workShiftMaster.setShiftType(shifttypeMaster);
		TestFactory testFactory=new TestFactory();
		testFactory.setTestFactoryId(this.testFactoryId);
		workShiftMaster.setTestFactory(testFactory);
		
		return workShiftMaster;
	}

	public String getTestFactoryName() {
		return testFactoryName;
	}

	public void setTestFactoryName(String testFactoryName) {
		this.testFactoryName = testFactoryName;
	}

	public String getShiftTypeName() {
		return shiftTypeName;
	}

	public void setShiftTypeName(String shiftTypeName) {
		this.shiftTypeName = shiftTypeName;
	}



}
