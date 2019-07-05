package com.hcl.atf.taf.model.json;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.hcl.atf.taf.model.DeviceLab;

public class JsonDeviceLab {

private static final long serialVersionUID = 1L;
@JsonProperty
	private Integer deviceLabId;
@JsonProperty
	private String createdDate;
@JsonProperty
	private String modifiedDate;
@JsonProperty
	private String deviceLabName;
@JsonProperty
	private String deviceLabDescription;
@JsonProperty
	private Integer status;

public JsonDeviceLab(DeviceLab deviceLab) {
	this.deviceLabId=deviceLab.getDevice_lab_Id();
		this.deviceLabName=deviceLab.getDevice_lab_name();
		this.deviceLabDescription=deviceLab.getDevice_lab_description();
}
@JsonIgnore
public DeviceLab getDeviceLab() {
	
	DeviceLab deviceLab = new DeviceLab();
	return deviceLab;
	
}
public Integer getDeviceLabId() {
	return deviceLabId;
}
public void setDeviceLabId(Integer deviceLabId) {
	this.deviceLabId = deviceLabId;
}
public String getCreatedDate() {
	return createdDate;
}
public void setCreatedDate(String createdDate) {
	this.createdDate = createdDate;
}
public String getModifiedDate() {
	return modifiedDate;
}
public void setModifiedDate(String modifiedDate) {
	this.modifiedDate = modifiedDate;
}
public String getDeviceLabName() {
	return deviceLabName;
}
public void setDeviceLabName(String deviceLabName) {
	this.deviceLabName = deviceLabName;
}
public String getDeviceLabDescription() {
	return deviceLabDescription;
}
public void setDeviceLabDescription(String deviceLabDescription) {
	this.deviceLabDescription = deviceLabDescription;
}
public Integer getStatus() {
	return status;
}
public void setStatus(Integer status) {
	this.status = status;
}

}
