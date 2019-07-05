package com.hcl.atf.taf.model.json;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.hcl.atf.taf.model.DeviceMakeMaster;
import com.hcl.atf.taf.model.DeviceModelMaster;

public class JsonDeviceModelMaster implements java.io.Serializable {
	@JsonProperty
	private Integer deviceModelListId;
	
	//ref to DeviceMakeMsster.deviceMake
	@JsonProperty	
	private String deviceMake;
	
	@JsonProperty
	private String deviceModel;
	
	@JsonProperty
	private String deviceResolution;
	
	public JsonDeviceModelMaster() {
	}

	public JsonDeviceModelMaster(DeviceModelMaster deviceModelMaster) {
		this.deviceModelListId = deviceModelMaster.getDeviceModelListId();
		this.deviceMake = deviceModelMaster.getDeviceMakeMaster().getDeviceMake();
		this.deviceModel = deviceModelMaster.getDeviceModel();
		this.deviceResolution = deviceModelMaster.getDeviceResolution();
	}

	public Integer getDeviceModelListId() {
		return deviceModelListId;
	}

	public void setDeviceModelListId(Integer deviceModelListId) {
		this.deviceModelListId = deviceModelListId;
	}

	public String getDeviceMake() {
		return deviceMake;
	}

	public void setDeviceMake(String deviceMake) {
		this.deviceMake = deviceMake;
	}

	public String getDeviceModel() {
		return deviceModel;
	}

	public void setDeviceModel(String deviceModel) {
		this.deviceModel = deviceModel;
	}

	public String getDeviceResolution() {
		return deviceResolution;
	}

	public void setDeviceResolution(String deviceResolution) {
		this.deviceResolution = deviceResolution;
	}
	
	
	@JsonIgnore
	public DeviceModelMaster getDeviceModelMaster(){
		DeviceModelMaster deviceModelMaster = new DeviceModelMaster();
		deviceModelMaster.setDeviceModelListId(deviceModelListId);
		deviceModelMaster.setDeviceModel(deviceModel);
		deviceModelMaster.setDeviceResolution(deviceResolution);
		
		DeviceMakeMaster deviceMakeMaster = new DeviceMakeMaster();
		deviceMakeMaster.setDeviceMake(deviceMake);
		deviceModelMaster.setDeviceMakeMaster(deviceMakeMaster);
		return deviceModelMaster;
	}
	
	
}
