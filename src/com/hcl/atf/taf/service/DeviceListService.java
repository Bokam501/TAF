package com.hcl.atf.taf.service;

import java.util.List;

import com.hcl.atf.taf.model.DeviceLab;
import com.hcl.atf.taf.model.DeviceList;
import com.hcl.atf.taf.model.DeviceMakeMaster;
import com.hcl.atf.taf.model.DeviceModelMaster;
import com.hcl.atf.taf.model.DevicePlatformMaster;
import com.hcl.atf.taf.model.DevicePlatformVersionListMaster;
import com.hcl.atf.taf.model.DeviceType;
import com.hcl.atf.taf.model.GenericDevices;
import com.hcl.atf.taf.model.PlatformType;
import com.hcl.atf.taf.model.Processor;
import com.hcl.atf.taf.model.RunConfiguration;
import com.hcl.atf.taf.model.SystemType;
import com.hcl.atf.taf.model.TestRunJob;
import com.hcl.atf.taf.model.json.JsonGenericDevice;
import com.hcl.atf.taf.model.json.JsonRunConfiguration;

public interface DeviceListService {
	void add (DeviceList deviceList);
	void update (DeviceList deviceList);
	void delete (int deviceListId);
	List<DeviceList> list();	
	List<DeviceList> list(int startIndex, int pageSize);
	List<DevicePlatformMaster> platformsList(int productId);
	List<DevicePlatformMaster> platformsList();
	List<DevicePlatformVersionListMaster> platformVersionsList(String devicePlatform);
	List<DeviceMakeMaster> makeList();
	List<DeviceModelMaster> modelList();
	List<DeviceModelMaster> modelList(String deviceMake);
	int getTotalRecords();
	List<DeviceList> listByHostId(int hostId);
	void resetDevicesStatus();
	void resetDevicesStatus(int hostId);
	void add(DevicePlatformVersionListMaster devicePlatformVersionListMaster);
	DevicePlatformVersionListMaster getDevicePlatformVersionList(DevicePlatformVersionListMaster devicePlatformVersionListMaster);
	DeviceModelMaster getDeviceModelMaster(DeviceModelMaster deviceModelMaster);
	int addDeviceModelMaster(DeviceModelMaster deviceModelMaster);
	void updateDeviceModelMaster(DeviceModelMaster deviceModelMaster);
	DeviceMakeMaster getDeviceMakeMaster(DeviceMakeMaster deviceMakeMaster);
	
	//primary key id
	DeviceList getDeviceByListId(int deviceListId);
	
	//device unique serial number
	DeviceList getDeviceById(String string);
	List<DeviceList> list(String deviceStatus);
	List<DeviceList> list(String deviceStatus,int startIndex, int pageSize);
	int totalRecordsCount();
	boolean isDeviceExistById(String deviceId);
	void resetDevicesStatus(String deviceId); 
	List<DeviceList> listHostIdByDevice(String deviceId);
	DeviceLab getDeviceLabByDeviceLabId(Integer deviceLabId);
	DeviceModelMaster getDeviceModelMasterById(Integer deviceModelMasterId);
	PlatformType getPlatFormType(Integer platformTypeId);
	String getPlatformTypeVersionByplatformId(int platformId);
	List<PlatformType> listPlatformType();
	void genericupdate(GenericDevices deviceList);
	void updateTestRunJob(TestRunJob testRunJob);
	
	List<SystemType> listSystemType();
	void addSystemType(SystemType systemType);
	SystemType getSystemTypeByName(String name);	
	
	List<Processor> listProcessor();
	void addProcessor(Processor processor);
	Processor getProcessorByProcessorName(String processorName);
	Processor getProcessorByProcessorId(Integer processorId);
	List<DeviceType> listDeviceType();
	void addDeviceType(DeviceType deviceType);
	DeviceType getDeviceTypeByName(String deviceTypeName);	
	DeviceType getDeviceTypeByTypeId(Integer deviceTypeId);
	
	DeviceModelMaster getDeviceModelMaster(String deviceModel);
	PlatformType getPlatFormType(String devicePlatformName,String devicePlatformVersion);
	DeviceMakeMaster getDeviceMakeMaster(String deviceMake);
	List<JsonGenericDevice> getUnMappedGenericDeviceOfProductfromRunConfigurationWorkPackageLevel(int productId, int ecId, int runConfigStatus, int workpackageId);
	RunConfiguration isRunConfigurationOfGenDeviceOfWPExisting(Integer environmentCombinationId, Integer workpackageId,	Integer deviceId);
	List<JsonGenericDevice> getUnMappedGenericDeviceOfProductfromRunConfigurationTestRunPlanLevel(int productId, int ecId, int runConfigStatus, int testRunPlanId);
	List<JsonRunConfiguration> getMappedGenericDeviceFromRunconfigurationTestRunPlanLevel(Integer environmentCombinationId, Integer testRunPlanId, Integer runConfigStatus);
	List<JsonRunConfiguration> getMappedGenericDeviceFromRunconfigurationWorkPackageLevel(Integer environmentCombinationId, Integer workPackageId, Integer runConfigStatus);
	int addDeviceMakeMaster(DeviceMakeMaster deviceMakeMaster);
	DeviceMakeMaster getDeviceMakeMasterById(Integer deviceMakeId);
	int addPlatformType(PlatformType platformType);
	PlatformType getPlatFormTypeById(Integer platformId);
	PlatformType validatePlatFormOrCreate(String devicePlatformName, String devicePlatformVersion);
}
