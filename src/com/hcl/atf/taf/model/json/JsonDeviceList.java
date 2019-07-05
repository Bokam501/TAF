package com.hcl.atf.taf.model.json;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.hcl.atf.taf.model.CommonActiveStatusMaster;
import com.hcl.atf.taf.model.DeviceLab;
import com.hcl.atf.taf.model.DeviceList;
import com.hcl.atf.taf.model.DeviceMakeMaster;
import com.hcl.atf.taf.model.DeviceModelMaster;
import com.hcl.atf.taf.model.DevicePlatformMaster;
import com.hcl.atf.taf.model.DevicePlatformVersionListMaster;
import com.hcl.atf.taf.model.HostList;
public class JsonDeviceList implements java.io.Serializable {

	@JsonProperty
	private Integer deviceListId;
	
	//ref to HostList.hostId
	@JsonProperty
	private Integer hostId;
	//ref to HostList.hostName
	@JsonProperty
	private String hostName;
	
	//ref to DevicemodelMaster.deviceModeListId
	@JsonProperty	
	private Integer deviceModelListId;
	//ref to DevicemodelMaster.deviceMode
	@JsonProperty
	private String deviceModel;
	//ref to DevicemodelMaster.deviceResolution
	@JsonProperty
	private String deviceResolution;
	
	//ref to DeviceMakeMaster.deviceMake
	@JsonProperty	
	private String deviceMake;
	
	
	//ref to DevicePlatformVersionListMaster.devicePlatformVersionListId
	@JsonProperty	
	private Integer devicePlatformVersionListId;
	//ref to DevicePlatformVersionListMaster.devicePlatformVersion
	@JsonProperty	
	private String devicePlatformVersion;
	//ref to DevicePlatformVersionListMaster.devicePlatformName
	@JsonProperty	
	private String devicePlatformName;	
	

	//ref CommonActiveStatusMaster.status
	@JsonProperty	
	private String deviceStatus;
	
	@JsonProperty
	private String deviceId;

	@JsonProperty
	private Integer deviceLabId;

	@JsonProperty
	private String deviceLabName;

	
	public JsonDeviceList() {
	}

	public JsonDeviceList(DeviceList deviceList) {
		this.deviceId = deviceList.getDeviceId();
		this.deviceListId=deviceList.getDeviceListId();
		this.deviceMake=deviceList.getDeviceModelMaster().getDeviceMakeMaster().getDeviceMake();
		this.deviceModel=deviceList.getDeviceModelMaster().getDeviceModel();
		this.deviceModelListId=deviceList.getDeviceModelMaster().getDeviceModelListId();
		this.deviceResolution=deviceList.getDeviceModelMaster().getDeviceResolution();
		this.deviceStatus=deviceList.getCommonActiveStatusMaster().getStatus();
		this.hostId=deviceList.getHostList().getHostId();
		this.hostName=deviceList.getHostList().getHostName();
		
		this.devicePlatformVersionListId=deviceList.getDevicePlatformVersionListMaster().getDevicePlatformVersionListId();
		this.devicePlatformVersion=deviceList.getDevicePlatformVersionListMaster().getDevicePlatformVersion();
		this.devicePlatformName=deviceList.getDevicePlatformVersionListMaster().getDevicePlatformMaster().getDevicePlatformName();
		this.deviceLabId=deviceList.getDeviceLab().getDevice_lab_Id();
		this.deviceLabName=deviceList.getDeviceLab().getDevice_lab_name();
	}


	
	public Integer getDeviceListId() {
		return deviceListId;
	}

	public void setDeviceListId(Integer deviceListId) {
		this.deviceListId = deviceListId;
	}

	public Integer getHostId() {
		return hostId;
	}

	public void setHostId(Integer hostId) {
		this.hostId = hostId;
	}

	public String getHostName() {
		return hostName;
	}

	public void setHostName(String hostName) {
		this.hostName = hostName;
	}

	public Integer getDeviceModelListId() {
		return deviceModelListId;
	}

	public void setDeviceModelListId(Integer deviceModelListId) {
		this.deviceModelListId = deviceModelListId;
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

	public String getDeviceMake() {
		return deviceMake;
	}

	public void setDeviceMake(String deviceMake) {
		this.deviceMake = deviceMake;
	}

	public Integer getDevicePlatformVersionListId() {
		return devicePlatformVersionListId;
	}

	public void setDevicePlatformVersionListId(Integer devicePlatformVersionListId) {
		this.devicePlatformVersionListId = devicePlatformVersionListId;
	}

	public String getDevicePlatformVersion() {
		return devicePlatformVersion;
	}

	public void setDevicePlatformVersion(String devicePlatformVersion) {
		this.devicePlatformVersion = devicePlatformVersion;
	}

	public String getDevicePlatformName() {
		return devicePlatformName;
	}

	public void setDevicePlatformName(String devicePlatformName) {
		this.devicePlatformName = devicePlatformName;
	}

	public String getDeviceStatus() {
		return deviceStatus;
	}

	public void setDeviceStatus(String deviceStatus) {
		this.deviceStatus = deviceStatus;
	}

	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	@JsonIgnore
	public DeviceList getDeviceList(){
		DeviceList deviceList = new DeviceList();
		deviceList.setDeviceId(deviceId);
		deviceList.setDeviceListId(deviceListId);
		
		HostList hostList = new HostList();
		hostList.setHostId(hostId);
		deviceList.setHostList(hostList);		
		
		DeviceModelMaster deviceModelMaster = new DeviceModelMaster();
		deviceModelMaster.setDeviceModelListId(deviceModelListId);
		deviceModelMaster.setDeviceModel(deviceModel);
		deviceModelMaster.setDeviceResolution(deviceResolution);		
		DeviceMakeMaster deviceMakeMaster = new DeviceMakeMaster();		
		deviceMakeMaster.setDeviceMake(deviceMake);
		deviceModelMaster.setDeviceMakeMaster(deviceMakeMaster);
		deviceList.setDeviceModelMaster(deviceModelMaster);
		
		CommonActiveStatusMaster deviceStatusMaster= new CommonActiveStatusMaster();
		deviceStatusMaster.setStatus(deviceStatus);
		deviceList.setCommonActiveStatusMaster(deviceStatusMaster);
		
		DevicePlatformVersionListMaster devicePlatformVersionListMaster = new DevicePlatformVersionListMaster();
		devicePlatformVersionListMaster.setDevicePlatformVersionListId(devicePlatformVersionListId);
		devicePlatformVersionListMaster.setDevicePlatformVersion(devicePlatformVersion);
		DevicePlatformMaster devicePlatformMaster = new DevicePlatformMaster();
		devicePlatformMaster.setDevicePlatformName(devicePlatformName);
		devicePlatformVersionListMaster.setDevicePlatformMaster(devicePlatformMaster);
		deviceList.setDevicePlatformVersionListMaster(devicePlatformVersionListMaster);
		
		DeviceLab deviceLab = new DeviceLab();
		deviceLab.setDevice_lab_Id(deviceLabId);
		deviceList.setDeviceLab(deviceLab);
		
		return deviceList;
	}

	public Integer getDeviceLabId() {
		return deviceLabId;
	}

	public void setDeviceLabId(Integer deviceLabId) {
		this.deviceLabId = deviceLabId;
	}

	public String getDeviceLabName() {
		return deviceLabName;
	}

	public void setDeviceLabName(String deviceLabName) {
		this.deviceLabName = deviceLabName;
	}
	
	
}
