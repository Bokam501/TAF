package com.hcl.atf.taf.model.json;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.hcl.atf.taf.model.DevicePlatformMaster;
import com.hcl.atf.taf.model.DevicePlatformVersionListMaster;


public class JsonDevicePlatformVersionListMaster implements java.io.Serializable {
	
	@JsonProperty
	private Integer devicePlatformVersionListId;
	
	@JsonProperty	
	private String devicePlatformMaster;
	
	@JsonProperty
	private String devicePlatformVersion;
	
	@JsonProperty
	private String devicePlatformVersionDescription;
	
	public JsonDevicePlatformVersionListMaster() {
	}

	public JsonDevicePlatformVersionListMaster(DevicePlatformVersionListMaster devicePlatformVersionListMaster) {
		this.devicePlatformVersionListId=devicePlatformVersionListMaster.getDevicePlatformVersionListId();
		this.devicePlatformMaster = devicePlatformVersionListMaster.getDevicePlatformMaster().getDevicePlatformName();
		this.devicePlatformVersion = devicePlatformVersionListMaster.getDevicePlatformVersion();
		this.devicePlatformVersionDescription = devicePlatformVersionListMaster.getDevicePlatformVersionDescription();
	
	}

	public Integer getDevicePlatformVersionListId() {
		return this.devicePlatformVersionListId;
	}

	public void setDevicePlatformVersionListId(
			Integer devicePlatformVersionListId) {
		this.devicePlatformVersionListId = devicePlatformVersionListId;
	}

	public String getDevicePlatformMaster() {
		return this.devicePlatformMaster;
	}

	public void setDevicePlatformMaster(
			String devicePlatformMaster) {
		this.devicePlatformMaster = devicePlatformMaster;
	}

	public String getDevicePlatformVersion() {
		return this.devicePlatformVersion;
	}

	public void setDevicePlatformVersion(String devicePlatformVersion) {
		this.devicePlatformVersion = devicePlatformVersion;
	}

	public String getDevicePlatformVersionDescription() {
		return this.devicePlatformVersionDescription;
	}

	public void setDevicePlatformVersionDescription(
			String devicePlatformVersionDescription) {
		this.devicePlatformVersionDescription = devicePlatformVersionDescription;
	}
	
	@JsonIgnore
	public DevicePlatformVersionListMaster getDevicePlatformVersionListMaster(){
		DevicePlatformVersionListMaster devicePlatformVersionListMaster = new DevicePlatformVersionListMaster();
		if(devicePlatformVersionListId!=null){
			devicePlatformVersionListMaster.setDevicePlatformVersionListId(devicePlatformVersionListId);
		}
		devicePlatformVersionListMaster.setDevicePlatformVersion(devicePlatformVersion);
		devicePlatformVersionListMaster.setDevicePlatformVersionDescription(devicePlatformVersionDescription);
			
		DevicePlatformMaster devicePlatformMaster = new DevicePlatformMaster();
		devicePlatformMaster.setDevicePlatformName(this.devicePlatformMaster);
		devicePlatformVersionListMaster.setDevicePlatformMaster(devicePlatformMaster);
		
		return devicePlatformVersionListMaster;
	}

	
}

