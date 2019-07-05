package com.hcl.atf.taf.dao;

import java.util.List;

import com.hcl.atf.taf.model.DeviceLab;
import com.hcl.atf.taf.model.DeviceList;
import com.hcl.atf.taf.model.DeviceMakeMaster;
import com.hcl.atf.taf.model.DeviceModelMaster;
import com.hcl.atf.taf.model.GenericDevices;
import com.hcl.atf.taf.model.PlatformType;
import com.hcl.atf.taf.model.RunConfiguration;
import com.hcl.atf.taf.model.TestRunJob;
import com.hcl.atf.taf.model.json.JsonGenericDevice;
import com.hcl.atf.taf.model.json.JsonRunConfiguration;

public interface DeviceListDAO  {	 
	void add (DeviceList deviceList);
	void update (DeviceList deviceList);
	void delete (DeviceList deviceList);
	List<DeviceList> list();
	
	List<DeviceList> list(int startIndex, int pageSize);
	List<DeviceList> list(int devicePlatformVersionlistId,String[] parameters);
	List<DeviceList> listByHostId(int hostId);
	int getTotalRecords();
	void resetDevicesStatus();
	void resetDevicesStatus(int hostId);
	DeviceList getByDeviceListId(int deviceListId);
	DeviceList getByDeviceId(String deviceId);
	List<DeviceList> list(String deviceStatus);
	List<DeviceList> list(String deviceStatus,int startIndex, int pageSize);
	DeviceList getByTestEnvironmentDevicesId(int deviceListId);
	List<DeviceList> listTestEnvironmentDevices(int testEnvironmentDevicesId,
			int startIndex, int pageSize);
	List<DeviceList> listTestEnvironmentDevices(int startIndex, int pageSize);
	List<DeviceList> listTestEnvironmentDevices(int testEnvironmentDevicesId);
	List<DeviceList> listTestEnvironmentDevices();
	List<DeviceList> listDevicesForPlatformVersion(String platformName,String[] parameters);
	List<DeviceList> listDevicesForPlatformVersion(String devicePlatformVersion);
	void deleteDevice(int deviceListId);
	List<DeviceList> getDevicesOfTestEnvironment(int testEnvironmentDevicesId);
	int totalRecordsCount();
	boolean isDeviceExistById(String deviceId);
	void resetDevicesStatus(String deviceId); 
	List<DeviceList> listHostIdByDevice(String deviceId);
	DeviceLab getDeviceLabByDeviceLabId(Integer deviceLabId);
	DeviceModelMaster getDeviceModelMasterById(Integer deviceModelMasterId);
	PlatformType getPlatFormType(Integer platformTypeId);
	List<PlatformType> listPlatformType();
	void genericupdate(GenericDevices deviceList);
	void updateTestRunJob(TestRunJob testRunJob);
	DeviceModelMaster getDeviceModelMaster(String deviceModel);
	PlatformType getPlatFormType(String devicePlatformName,String devicePlatformVersion);
	DeviceMakeMaster getDeviceMakeMaster(String deviceMake);
	List<JsonGenericDevice> getUnMappedGenericDeviceOfProductfromRunConfigurationWorkPackageLevel(int productId, int ecId, int runConfigStatus, int workpackageId);
	RunConfiguration isRunConfigurationOfGenDeviceOfWPExisting(Integer environmentCombinationId, Integer workpackageId,	Integer deviceId);
	List<JsonGenericDevice> getUnMappedGenericDeviceOfProductfromRunConfigurationTestRunPlanLevel(int productId, int ecId, int runConfigStatus, int testRunPlanId);
	List<JsonRunConfiguration> getMappedGenericDeviceFromRunconfigurationTestRunPlanLevel(Integer environmentCombinationId, Integer testRunPlanId, Integer runConfigStatus);
	List<JsonRunConfiguration> getMappedGenericDeviceFromRunconfigurationWorkPackageLevel(Integer environmentCombinationId, Integer workPackageId, Integer runConfigStatus);
	String getPlatformTypeVersionByplatformId(int platformId);
	int addDeviceModelMaster(DeviceModelMaster deviceModelMaster);
	int addDeviceMakeMaster(DeviceMakeMaster deviceMakeMaster);
	DeviceMakeMaster getDeviceMakeMasterById(Integer deviceMakeId);
	PlatformType getPlatFormTypeById(Integer platformId);
	int addPlatformType(PlatformType platformType);
	void updateDeviceModelMaster(DeviceModelMaster deviceModelMaster);
}
