package com.hcl.atf.taf.model.json;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.hcl.atf.taf.model.DeviceMakeMaster;

public class JsonDeviceMakeMaster implements java.io.Serializable {

	@JsonProperty
	private String deviceMake;
	@JsonProperty
	private Integer deviceMakeId;
	
	public String getDeviceMake() {
		return deviceMake;
	}

	public void setDeviceMake(String deviceMake) {
		this.deviceMake = deviceMake;
	}

	public Integer getDeviceMakeId() {
		return deviceMakeId;
	}

	public void setDeviceMakeId(Integer deviceMakeId) {
		this.deviceMakeId = deviceMakeId;
	}

	public JsonDeviceMakeMaster() {
	}

	public JsonDeviceMakeMaster(DeviceMakeMaster deviceMakeMaster) {
		this.deviceMake = deviceMakeMaster.getDeviceMake();
		if(deviceMakeMaster.getDeviceMakeId() != null){
			this.deviceMakeId = deviceMakeMaster.getDeviceMakeId();
		}		
	}

	@JsonIgnore
	public DeviceMakeMaster getDeviceMakeMaster(){
		DeviceMakeMaster deviceMakeMaster = new DeviceMakeMaster();
		deviceMakeMaster.setDeviceMake(deviceMake);
		if(deviceMakeId != null){
			deviceMakeMaster.setDeviceMakeId(deviceMakeId);
		}		
		return deviceMakeMaster;
	}
}
