package com.hcl.atf.taf.report.xml.beans;

// Generated Feb 4, 2014 4:30:16 PM by Hibernate Tools 3.4.0.CR1



public class DeviceListBean implements java.io.Serializable {

	
	private static final long serialVersionUID = 1L;
	private Integer deviceListId;
	//private HostListBean hostList;
	//private DeviceModelMasterBean deviceModelMaster;
	//private DevicePlatformVersionListMasterBean devicePlatformVersionListMaster;
	//private CommonActiveStatusMaster commonActiveStatusMaster;
	private String deviceId;
	/*private Set<TestRunSelectedDeviceListBean> testRunSelectedDeviceLists = new HashSet<TestRunSelectedDeviceListBean>(
			0);
	private Set<TestEnvironmentDevicesBean> testEnvironmentDevices = new HashSet<TestEnvironmentDevicesBean>(
			0);
	private Set<TestRunListBean> testRunLists = new HashSet<TestRunListBean>(0);*/
	
	//private Set<TrccExecutionPlanDetails> trccExecutionPlanDetails = new HashSet<TrccExecutionPlanDetails>(0);
	//private DeviceLabBean deviceLab;

	public Integer getDeviceListId() {
		return deviceListId;
	}

	public void setDeviceListId(Integer deviceListId) {
		this.deviceListId = deviceListId;
	}
/*
	public HostListBean getHostList() {
		return hostList;
	}

	public void setHostList(HostListBean hostList) {
		this.hostList = hostList;
	}

	public DeviceModelMasterBean getDeviceModelMaster() {
		return deviceModelMaster;
	}

	public void setDeviceModelMaster(DeviceModelMasterBean deviceModelMaster) {
		this.deviceModelMaster = deviceModelMaster;
	}
*/
	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	/*public Set<TestRunSelectedDeviceListBean> getTestRunSelectedDeviceLists() {
		return testRunSelectedDeviceLists;
	}

	public void setTestRunSelectedDeviceLists(
			Set<TestRunSelectedDeviceListBean> testRunSelectedDeviceLists) {
		this.testRunSelectedDeviceLists = testRunSelectedDeviceLists;
	}

	public Set<TestEnvironmentDevicesBean> getTestEnvironmentDevices() {
		return testEnvironmentDevices;
	}

	public void setTestEnvironmentDevices(
			Set<TestEnvironmentDevicesBean> testEnvironmentDevices) {
		this.testEnvironmentDevices = testEnvironmentDevices;
	}

	public Set<TestRunListBean> getTestRunLists() {
		return testRunLists;
	}

	public void setTestRunLists(Set<TestRunListBean> testRunLists) {
		this.testRunLists = testRunLists;
	}

	public DeviceLabBean getDeviceLab() {
		return deviceLab;
	}

	public void setDeviceLab(DeviceLabBean deviceLab) {
		this.deviceLab = deviceLab;
	}*/
	
	/*public DeviceList() {
	}

	public DeviceList(HostList hostList, DeviceModelMaster deviceModelMaster,
			DevicePlatformVersionListMaster devicePlatformVersionListMaster,
			CommonActiveStatusMaster commonActiveStatusMaster, String deviceId) {
		this.hostList = hostList;
		this.deviceModelMaster = deviceModelMaster;
		this.devicePlatformVersionListMaster = devicePlatformVersionListMaster;
		this.commonActiveStatusMaster = commonActiveStatusMaster;
		this.deviceId = deviceId;
	}

	public DeviceList(HostList hostList, DeviceModelMaster deviceModelMaster,
			DevicePlatformVersionListMaster devicePlatformVersionListMaster,
			CommonActiveStatusMaster commonActiveStatusMaster, String deviceId,
			Set<TestRunSelectedDeviceList> testRunSelectedDeviceLists,
			Set<TestEnvironmentDevices> testEnvironmentDevices,
			Set<TestRunList> testRunLists) {
		this.hostList = hostList;
		this.deviceModelMaster = deviceModelMaster;
		this.devicePlatformVersionListMaster = devicePlatformVersionListMaster;
		this.commonActiveStatusMaster = commonActiveStatusMaster;
		this.deviceId = deviceId;
		this.testRunSelectedDeviceLists = testRunSelectedDeviceLists;
		this.testEnvironmentDevices = testEnvironmentDevices;
		this.testRunLists = testRunLists;
	}*/

	/*
	public DeviceList(HostList hostList, DeviceModelMaster deviceModelMaster,
			DevicePlatformVersionListMaster devicePlatformVersionListMaster,
			CommonActiveStatusMaster commonActiveStatusMaster, String deviceId,
			Set<TestRunSelectedDeviceList> testRunSelectedDeviceLists,
			Set<TestEnvironmentDevices> testEnvironmentDevices,
			Set<TestRunList> testRunLists,Set<TrccExecutionPlanDetails> trccExecutionPlanDetails) {
		this.hostList = hostList;
		this.deviceModelMaster = deviceModelMaster;
		this.devicePlatformVersionListMaster = devicePlatformVersionListMaster;
		this.commonActiveStatusMaster = commonActiveStatusMaster;
		this.deviceId = deviceId;
		this.testRunSelectedDeviceLists = testRunSelectedDeviceLists;
		this.testEnvironmentDevices = testEnvironmentDevices;
		this.testRunLists = testRunLists;
		this.trccExecutionPlanDetails = trccExecutionPlanDetails;
	}
	*/
	
	
	
}
