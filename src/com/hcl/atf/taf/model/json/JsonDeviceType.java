package com.hcl.atf.taf.model.json;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.hcl.atf.taf.model.DeviceType;

public class JsonDeviceType {
	@JsonProperty
	private Integer deviceTypeId;
	@JsonProperty
	private String deviceTypeName;
	@JsonProperty
	private Integer deviceType_parentId;
	public JsonDeviceType() {
	}

	public JsonDeviceType(DeviceType deviceType) {
		this.deviceTypeId = deviceType.getDeviceTypeId();
		this.deviceTypeName=deviceType.getDeviceTypeName();
	}

	public Integer getDeviceTypeId() {
		return deviceTypeId;
	}

	public void setDeviceTypeId(Integer deviceTypeId) {
		this.deviceTypeId = deviceTypeId;
	}

	public String getDeviceTypeName() {
		return deviceTypeName;
	}

	public void setDeviceTypeName(String deviceTypeName) {
		this.deviceTypeName = deviceTypeName;
	}

	public Integer getDeviceType_parentId() {
		return deviceType_parentId;
	}

	public void setDeviceType_parentId(Integer deviceType_parentId) {
		this.deviceType_parentId = deviceType_parentId;
	}

	
}
