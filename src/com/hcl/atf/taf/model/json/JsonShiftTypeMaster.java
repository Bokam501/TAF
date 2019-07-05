package com.hcl.atf.taf.model.json;



import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.hcl.atf.taf.controller.utilities.DateUtility;
import com.hcl.atf.taf.model.ShiftTypeMaster;
import com.hcl.atf.taf.model.WorkShiftMaster;
public class JsonShiftTypeMaster implements java.io.Serializable {

		
	@JsonProperty
	private Integer shiftTypeId;
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
	
	

	public JsonShiftTypeMaster() {
	}

	public JsonShiftTypeMaster(ShiftTypeMaster shiftTypeMaster) {
		
		this.shiftTypeId=shiftTypeMaster.getShiftTypeId();
		this.shiftName=shiftTypeMaster.getShiftName();
		this.displayLabel=shiftTypeMaster.getDisplayLabel();
		this.description=shiftTypeMaster.getDescription();
		if(shiftTypeMaster.getStartTime()!=null)
			this.startTime=DateUtility.dateToStringInSecond(shiftTypeMaster.getStartTime());
		if(shiftTypeMaster.getEndTime()!=null)
			this.endTime=DateUtility.dateToStringInSecond(shiftTypeMaster.getEndTime());
		this.status=shiftTypeMaster.getStatus();
	}


	
	

	public Integer getShiftTypeId() {
		return shiftTypeId;
	}

	public void setShiftTypeId(Integer shiftTypeId) {
		this.shiftTypeId = shiftTypeId;
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

	@JsonIgnore
	public WorkShiftMaster getWorkShiftMaster(){
		WorkShiftMaster workShiftMaster=new WorkShiftMaster();
				
		workShiftMaster.setShiftId(shiftTypeId);
		workShiftMaster.setShiftName(shiftName);
		workShiftMaster.setDisplayLabel(displayLabel);
		workShiftMaster.setDescription(description);
		if(startTime!=null)
			workShiftMaster.setStartTime(DateUtility.dateFormatWithOutSeconds(startTime));
		if(endTime!=null)
			workShiftMaster.setEndTime(DateUtility.dateFormatWithOutSeconds(endTime));
		workShiftMaster.setStatus(status);
		
		return workShiftMaster;
	}

}
