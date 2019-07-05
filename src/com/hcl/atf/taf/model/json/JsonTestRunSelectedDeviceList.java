package com.hcl.atf.taf.model.json;



import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.hcl.atf.taf.model.CommonActiveStatusMaster;
import com.hcl.atf.taf.model.DeviceList;
import com.hcl.atf.taf.model.DeviceMakeMaster;
import com.hcl.atf.taf.model.DeviceModelMaster;
import com.hcl.atf.taf.model.DevicePlatformMaster;
import com.hcl.atf.taf.model.DevicePlatformVersionListMaster;
import com.hcl.atf.taf.model.HostList;
import com.hcl.atf.taf.model.TestRunConfigurationChild;
import com.hcl.atf.taf.model.TestRunSelectedDeviceList;


public class JsonTestRunSelectedDeviceList implements java.io.Serializable {

	@JsonProperty
	private Integer selectedDeviceListId;
	
	@JsonProperty
	private Integer deviceListId;
	@JsonProperty
	private String deviceId;
	@JsonProperty
	private Integer deviceModelListId;
	@JsonProperty
	private String deviceModel;
	@JsonProperty
	private String deviceMake;
	@JsonProperty
	private Integer devicePlatformVersionListId;
	@JsonProperty
	private String devicePlatformVersion;
	@JsonProperty
	private String devicePlatformName;
	@JsonProperty
	private String deviceStatus;
	@JsonProperty
	private Integer hostId;
	@JsonProperty
	private String hostName;
	
	@JsonProperty
	private Integer testRunConfigurationChildId;

	public JsonTestRunSelectedDeviceList() {
	}

	public JsonTestRunSelectedDeviceList(TestRunSelectedDeviceList testRunSelectedDeviceList) {
		
		selectedDeviceListId=testRunSelectedDeviceList.getSelectedDeviceListId();
		
		if(testRunSelectedDeviceList.getDeviceList()!=null){
			this.deviceListId=testRunSelectedDeviceList.getDeviceList().getDeviceListId();
			this.deviceId=testRunSelectedDeviceList.getDeviceList().getDeviceId();
			if(testRunSelectedDeviceList.getDeviceList().getDeviceModelMaster()!=null){
				this.deviceModelListId=testRunSelectedDeviceList.getDeviceList().getDeviceModelMaster().getDeviceModelListId();
				this.deviceModel=testRunSelectedDeviceList.getDeviceList().getDeviceModelMaster().getDeviceModel();
				if(testRunSelectedDeviceList.getDeviceList().getDeviceModelMaster().getDeviceMakeMaster()!=null){
					this.deviceMake=testRunSelectedDeviceList.getDeviceList().getDeviceModelMaster().getDeviceMakeMaster().getDeviceMake();
				}
			}
			if(testRunSelectedDeviceList.getDeviceList().getDevicePlatformVersionListMaster()!=null){
				this.devicePlatformVersionListId=testRunSelectedDeviceList.getDeviceList().getDevicePlatformVersionListMaster().getDevicePlatformVersionListId();
				this.devicePlatformVersion=testRunSelectedDeviceList.getDeviceList().getDevicePlatformVersionListMaster().getDevicePlatformVersion();
				if(testRunSelectedDeviceList.getDeviceList().getDevicePlatformVersionListMaster().getDevicePlatformMaster()!=null){
					this.devicePlatformName=testRunSelectedDeviceList.getDeviceList().getDevicePlatformVersionListMaster().getDevicePlatformMaster().getDevicePlatformName();
				}
				if(testRunSelectedDeviceList.getDeviceList().getCommonActiveStatusMaster()!=null){
					this.deviceStatus=testRunSelectedDeviceList.getDeviceList().getCommonActiveStatusMaster().getStatus();
				}
			}
			if(testRunSelectedDeviceList.getDeviceList().getHostList()!=null){
				this.hostId=testRunSelectedDeviceList.getDeviceList().getHostList().getHostId();
				this.hostName=testRunSelectedDeviceList.getDeviceList().getHostList().getHostName();
			}
		}
				
		if(testRunSelectedDeviceList.getTestRunConfigurationChild()!=null){
			this.testRunConfigurationChildId=testRunSelectedDeviceList.getTestRunConfigurationChild().getTestRunConfigurationChildId();
		}
	}

	public Integer getSelectedDeviceListId() {
		return selectedDeviceListId;
	}

	public void setSelectedDeviceListId(Integer selectedDeviceListId) {
		this.selectedDeviceListId = selectedDeviceListId;
	}

	public Integer getDeviceListId() {
		return deviceListId;
	}

	public void setDeviceListId(Integer deviceListId) {
		this.deviceListId = deviceListId;
	}

	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
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

	public Integer getTestRunConfigurationChildId() {
		return testRunConfigurationChildId;
	}

	public void setTestRunConfigurationChildId(Integer testRunConfigurationChildId) {
		this.testRunConfigurationChildId = testRunConfigurationChildId;
	}

	@JsonIgnore
	public TestRunSelectedDeviceList getTestRunSelectedDeviceList(){
		TestRunSelectedDeviceList testRunSelectedDeviceList = new TestRunSelectedDeviceList();
		if(selectedDeviceListId!=null)
		testRunSelectedDeviceList.setSelectedDeviceListId(selectedDeviceListId);
		
		DeviceList deviceList = new DeviceList();
		deviceList.setDeviceListId(deviceListId);
		if(deviceId!=null)
		deviceList.setDeviceId(deviceId);
		DeviceModelMaster deviceModelMaster = new DeviceModelMaster();
		if(deviceModelListId!=null)
		deviceModelMaster.setDeviceModelListId(deviceModelListId);		
		DeviceMakeMaster deviceMakeMaster = new DeviceMakeMaster();
		if(deviceMake!=null)
		deviceMakeMaster.setDeviceMake(deviceMake);
		deviceModelMaster.setDeviceMakeMaster(deviceMakeMaster);
		deviceList.setDeviceModelMaster(deviceModelMaster);
		
		DevicePlatformVersionListMaster devicePlatformVersionListMaster = new DevicePlatformVersionListMaster();
		if(devicePlatformVersionListId!=null)
		devicePlatformVersionListMaster.setDevicePlatformVersionListId(devicePlatformVersionListId);
		DevicePlatformMaster devicePlatformMaster = new DevicePlatformMaster();
		if(devicePlatformName!=null)
		devicePlatformMaster.setDevicePlatformName(devicePlatformName);
		devicePlatformVersionListMaster.setDevicePlatformMaster(devicePlatformMaster);
		deviceList.setDevicePlatformVersionListMaster(devicePlatformVersionListMaster);
		
		HostList hostList = new HostList();
		hostList.setHostId(hostId);
		deviceList.setHostList(hostList);
		
		CommonActiveStatusMaster deviceStatusMaster = new CommonActiveStatusMaster();
		deviceStatusMaster.setStatus(deviceStatus);
		deviceList.setCommonActiveStatusMaster(deviceStatusMaster);
		
		testRunSelectedDeviceList.setDeviceList(deviceList);
		
		
		TestRunConfigurationChild testRunConfigurationChild = new TestRunConfigurationChild();
		testRunConfigurationChild.setTestRunConfigurationChildId(testRunConfigurationChildId);
		testRunSelectedDeviceList.setTestRunConfigurationChild(testRunConfigurationChild);
		
		return testRunSelectedDeviceList;
	}
	
}
