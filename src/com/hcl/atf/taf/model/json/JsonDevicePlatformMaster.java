package com.hcl.atf.taf.model.json;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.hcl.atf.taf.model.DevicePlatformMaster;


public class JsonDevicePlatformMaster implements java.io.Serializable {
	@JsonProperty
	private String devicePlatformName;
	

	public JsonDevicePlatformMaster() {
	}

	public JsonDevicePlatformMaster(String devicePlatformName) {
		this.devicePlatformName = devicePlatformName;
	}

	public JsonDevicePlatformMaster(DevicePlatformMaster devicePlatformMaster) {
		this.devicePlatformName = devicePlatformMaster.getDevicePlatformName();		
	}

	
	public String getDevicePlatformName() {
		return this.devicePlatformName;
	}

	public void setDevicePlatformName(String devicePlatformName) {
		this.devicePlatformName = devicePlatformName;
	}
	
	
	@JsonIgnore
	public DevicePlatformMaster getDevicePlatformMaster(){
		DevicePlatformMaster devicePlatformMaster = new DevicePlatformMaster();
		if(devicePlatformName!=null){
			devicePlatformMaster.setDevicePlatformName(devicePlatformName);
		}
		return devicePlatformMaster;
	}

}
