package com.hcl.atf.taf.model.json;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.hcl.atf.taf.controller.utilities.DateUtility;
import com.hcl.atf.taf.model.DeviceLab;
import com.hcl.atf.taf.model.DeviceModelMaster;
import com.hcl.atf.taf.model.GenericDevices;
import com.hcl.atf.taf.model.MobileType;
import com.hcl.atf.taf.model.PlatformType;
import com.hcl.atf.taf.model.ServerType;
import com.hcl.atf.taf.model.StorageDrive;

public class JsonGenericDevice {
	@JsonProperty
	private Integer genericsDevicesId;
	@JsonProperty
	private String name;
	@JsonProperty
	private String description;
	@JsonProperty
	private Integer status;
	@JsonProperty
	private Integer deviceLabId;
	@JsonProperty
	private String createdDate;
	@JsonProperty
	private String modifiedDate;
	@JsonProperty
	private Integer availableStatus;
	@JsonProperty
	private Integer platformTypeId;
	@JsonProperty
	private String UDID;
	@JsonProperty
	private Integer deviceModelMasterId;
	@JsonProperty
	private Integer testFactoryId;
	@JsonProperty
	private Integer testFactoryLabId;
	@JsonProperty
	private Integer productId;
	@JsonProperty
	private String deviceModelMasterNmae;
	@JsonProperty
	private String deviceLabName;
	@JsonProperty
	private String platformTypeName;
	@JsonProperty
	private String platformTypeversion;
	@JsonProperty
	private Integer hostId;
	@JsonProperty
	private String hostIpAddress;
	@JsonProperty
	private String hostName;
	@JsonProperty
	private String hostStatus;
	@JsonProperty
	private Integer devType;
	
	//ServerType
	@JsonProperty
	private String server_hostName;
	@JsonProperty
	private String server_ip;
	@JsonProperty
	private String server_systemName;
	@JsonProperty
	private Integer server_systemTypeId;
	@JsonProperty	
	private Integer server_processorId;
	@JsonProperty
	private String server_processorName;
	//MobileType
	@JsonProperty
	private String kernelNumber;
	@JsonProperty
	private String buildNumber;
	@JsonProperty	
	private Integer deviceMakeId;
	@JsonProperty
	private String deviceMake;
	@JsonProperty
	private Long storageSize;
	@JsonProperty
	private String storageSizeUnit;	
	@JsonProperty
	private String firmware;
	@JsonProperty
	private String bootLoader;
	@JsonProperty	
	private String driveVersion;
	@JsonProperty
	private Integer screenResolutionX;
	@JsonProperty
	private Integer screenResolutionY;
	
	public JsonGenericDevice(){
		
	}
	
	
	public JsonGenericDevice(GenericDevices genericDevices) {
		this.genericsDevicesId=genericDevices.getGenericsDevicesId();
		this.name=genericDevices.getName();
		this.description=genericDevices.getDescription();
		this.status=genericDevices.getStatus();
		if(genericDevices.getDeviceLab()!=null){
		this.deviceLabId=genericDevices.getDeviceLab().getDevice_lab_Id();
		this.deviceLabName=genericDevices.getDeviceLab().getDevice_lab_name();
		}
		if(genericDevices.getDeviceModelMaster()!=null){
		this.deviceModelMasterId=genericDevices.getDeviceModelMaster().getDeviceModelListId();
		this.deviceModelMasterNmae=genericDevices.getDeviceModelMaster().getDeviceModel();
		}
		if(genericDevices.getProductMaster()!=null)
			this.productId=genericDevices.getProductMaster().getProductId();
		this.UDID=genericDevices.getUDID();
		this.availableStatus=genericDevices.getAvailableStatus();
		if(genericDevices.getPlatformType()!=null){
			this.platformTypeId=genericDevices.getPlatformType().getPlatformId();
			this.platformTypeName=genericDevices.getPlatformType().getName();
			if(genericDevices.getPlatformType().getVersion() != null){
				this.platformTypeversion =	genericDevices.getPlatformType().getVersion();
			}			 
		}
		if(genericDevices instanceof ServerType){
			this.devType = 4;
			this.server_hostName = ((ServerType) genericDevices).getHostName();
			this.server_ip= ((ServerType) genericDevices).getIp();			
			if(((ServerType) genericDevices).getSystemType() != null){
				this.server_systemTypeId = ((ServerType) genericDevices).getSystemType().getSystemTypeId();	
				this.server_systemName =((ServerType) genericDevices).getSystemType().getName();
			}
			if(((ServerType) genericDevices).getProcessor() != null){
				//private Processor processor;
				this.server_processorId = ((ServerType) genericDevices).getProcessor().getProcessorId();	
				this.server_processorName = ((ServerType) genericDevices).getProcessor().getProcessorName();
			}
		}else if(genericDevices instanceof MobileType){
			this.devType = 5;
			this.kernelNumber = ((MobileType) genericDevices).getKernelNumber();
			this.buildNumber = ((MobileType) genericDevices).getBuildNumber();
			if(((MobileType) genericDevices).getDeviceMakeMaster() != null){
				//private DeviceMakeMaster deviceMakeMaster;
				this.deviceMakeId = ((MobileType) genericDevices).getDeviceMakeMaster().getDeviceMakeId();					
				this.deviceMake = ((MobileType) genericDevices).getDeviceMakeMaster().getDeviceMake();
			}
		}else if(genericDevices instanceof StorageDrive){
			this.devType = 6;
			this.storageSize = ((StorageDrive) genericDevices).getStorageSize();
			this.storageSizeUnit = ((StorageDrive) genericDevices).getStorageSizeUnit();
			this.firmware = ((StorageDrive) genericDevices).getFirmware();
			this.bootLoader = ((StorageDrive) genericDevices).getBootLoader();
			this.driveVersion = ((StorageDrive) genericDevices).getDriveVersion();			
			
			if(((StorageDrive) genericDevices).getDeviceMakeMaster() != null){
				this.deviceMakeId = ((StorageDrive) genericDevices).getDeviceMakeMaster().getDeviceMakeId();					
				this.deviceMake = ((StorageDrive) genericDevices).getDeviceMakeMaster().getDeviceMake();
			}
			if(((StorageDrive) genericDevices).getProcessor() != null){
				//private Processor processor;
				this.server_processorId = ((StorageDrive) genericDevices).getProcessor().getProcessorId();	
				this.server_processorName = ((StorageDrive) genericDevices).getProcessor().getProcessorName();
			}
		}
	
		
		if(genericDevices.getHostList()!=null){
			this.hostId=genericDevices.getHostList().getHostId();
			this.hostName=genericDevices.getHostList().getHostName();
			this.hostIpAddress=genericDevices.getHostList().getHostIpAddress();
			this.hostStatus=genericDevices.getHostList().getCommonActiveStatusMaster().getStatus();
		}		
		this.screenResolutionX=genericDevices.getScreenResolutionX();
		this.screenResolutionY=genericDevices.getScreenResolutionY();
	}
	@JsonIgnore
	public GenericDevices  getGenericDevices() {
		GenericDevices genericDevice=new GenericDevices();	
		genericDevice.setGenericsDevicesId(genericsDevicesId);
		genericDevice.setAvailableStatus(availableStatus);
		genericDevice.setDescription(description);
		genericDevice.setName(name);
		genericDevice.setStatus(status);
		genericDevice.setUDID(UDID);
		if(genericDevice.getDeviceLab()!=null){
			DeviceLab deviceLab=new DeviceLab();
			deviceLab.setDevice_lab_Id(deviceLabId);
		genericDevice.setDeviceLab(deviceLab);
		}

		if(genericDevice.getDeviceModelMaster()!=null){
			DeviceModelMaster deviceModelMaster=new DeviceModelMaster();
			deviceModelMaster.setDeviceModelListId(deviceModelMasterId);
		genericDevice.setDeviceModelMaster(deviceModelMaster);
		}
		if(genericDevice.getPlatformType()!=null){
			
			PlatformType platformType=new PlatformType();
			platformType.setPlatformId(platformTypeId);
			if(platformTypeversion != null){
				platformType.setVersion(platformTypeversion);
			}
		genericDevice.setPlatformType(platformType);
		}		
		
		if(this.createdDate == null || this.createdDate.trim().isEmpty()) {
			genericDevice.setCreatedDate(DateUtility.getCurrentTime());
		} else {
		
			genericDevice.setCreatedDate(DateUtility.dateFormatWithOutSeconds(createdDate));
		}
		if(this.modifiedDate == null || this.modifiedDate.trim().isEmpty()) {
			genericDevice.setModifiedDate(DateUtility.getCurrentTime());
		} else {
		
			genericDevice.setModifiedDate(DateUtility.dateFormatWithOutSeconds(modifiedDate));
		}
		
		genericDevice.setScreenResolutionX(screenResolutionX);
		genericDevice.setScreenResolutionY(screenResolutionY);
		return genericDevice;
	}
	public Integer getGenericsDevicesId() {
		return genericsDevicesId;
	}
	public void setGenericsDevicesId(Integer genericsDevicesId) {
		this.genericsDevicesId = genericsDevicesId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public Integer getDeviceLabId() {
		return deviceLabId;
	}
	public void setDeviceLabId(Integer deviceLabId) {
		this.deviceLabId = deviceLabId;
	}
	public String getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
	}
	public String getModifiedDate() {
		return modifiedDate;
	}
	public void setModifiedDate(String modifiedDate) {
		this.modifiedDate = modifiedDate;
	}
	public Integer getAvailableStatus() {
		return availableStatus;
	}
	public void setAvailableStatus(Integer availableStatus) {
		this.availableStatus = availableStatus;
	}
	public Integer getPlatformTypeId() {
		return platformTypeId;
	}
	public void setPlatformTypeId(Integer platformTypeId) {
		this.platformTypeId = platformTypeId;
	}
	public String getUDID() {
		return UDID;
	}
	public void setUDID(String uDID) {
		UDID = uDID;
	}
	public Integer getDeviceModelMasterId() {
		return deviceModelMasterId;
	}
	public void setDeviceModelMasterId(Integer deviceModelMasterId) {
		this.deviceModelMasterId = deviceModelMasterId;
	}
	public Integer getTestFactoryId() {
		return testFactoryId;
	}
	public void setTestFactoryId(Integer testFactoryId) {
		this.testFactoryId = testFactoryId;
	}
	public Integer getTestFactoryLabId() {
		return testFactoryLabId;
	}
	public void setTestFactoryLabId(Integer testFactoryLabId) {
		this.testFactoryLabId = testFactoryLabId;
	}
	public Integer getProductId() {
		return productId;
	}
	public void setProductId(Integer productId) {
		this.productId = productId;
	}

	public String getDeviceModelMasterNmae() {
		return deviceModelMasterNmae;
	}

	public void setDeviceModelMasterNmae(String deviceModelMasterNmae) {
		this.deviceModelMasterNmae = deviceModelMasterNmae;
	}

	public String getDeviceLabName() {
		return deviceLabName;
	}
	
	public void setDeviceLabName(String deviceLabName) {
		this.deviceLabName = deviceLabName;
	}

	public String getPlatformTypeName() {
		return platformTypeName;
	}

	public void setPlatformTypeName(String platformTypeName) {
		this.platformTypeName = platformTypeName;
	}

	public String getPlatformTypeversion() {
		return platformTypeversion;
	}

	public void setPlatformTypeversion(String platformTypeversion) {
		this.platformTypeversion = platformTypeversion;
	}

	public String getHostIpAddress() {
		return hostIpAddress;
	}

	public void setHostIpAddress(String hostIpAddress) {
		this.hostIpAddress = hostIpAddress;
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

	public String getHostStatus() {
		return hostStatus;
	}

	public void setHostStatus(String hostStatus) {
		this.hostStatus = hostStatus;
	}

	public Integer getDevType() {
		return devType;
	}


	public void setDevType(Integer devType) {
		this.devType = devType;
	}

	public String getServer_hostName() {
		return server_hostName;
	}

	public void setServer_hostName(String server_hostName) {
		this.server_hostName = server_hostName;
	}

	public String getServer_ip() {
		return server_ip;
	}

	public void setServer_ip(String server_ip) {
		this.server_ip = server_ip;
	}
	
	public String getServer_systemName() {
		return server_systemName;
	}

	public void setServer_systemName(String server_systemName) {
		this.server_systemName = server_systemName;
	}

	public Integer getServer_systemTypeId() {
		return server_systemTypeId;
	}

	public void setServer_systemTypeId(Integer server_systemTypeId) {
		this.server_systemTypeId = server_systemTypeId;
	}

	public Integer getServer_processorId() {
		return server_processorId;
	}

	public void setServer_processorId(Integer server_processorId) {
		this.server_processorId = server_processorId;
	}

	public String getServer_processorName() {
		return server_processorName;
	}

	public void setServer_processorName(String server_processorName) {
		this.server_processorName = server_processorName;
	}

	public String getKernelNumber() {
		return kernelNumber;
	}

	public void setKernelNumber(String kernelNumber) {
		this.kernelNumber = kernelNumber;
	}

	public String getBuildNumber() {
		return buildNumber;
	}

	public void setBuildNumber(String buildNumber) {
		this.buildNumber = buildNumber;
	}

	public Integer getDeviceMakeId() {
		return deviceMakeId;
	}

	public String getDeviceMake() {
		return deviceMake;
	}

	public void setDeviceMake(String deviceMake) {
		this.deviceMake = deviceMake;
	}

	public void setDeviceMakeId(Integer deviceMakeId) {
		this.deviceMakeId = deviceMakeId;
	}

	public Long getStorageSize() {
		return storageSize;
	}

	public void setStorageSize(Long storageSize) {
		this.storageSize = storageSize;
	}

	public String getStorageSizeUnit() {
		return storageSizeUnit;
	}

	public void setStorageSizeUnit(String storageSizeUnit) {
		this.storageSizeUnit = storageSizeUnit;
	}

	public String getFirmware() {
		return firmware;
	}

	public void setFirmware(String firmware) {
		this.firmware = firmware;
	}

	public String getBootLoader() {
		return bootLoader;
	}

	public void setBootLoader(String bootLoader) {
		this.bootLoader = bootLoader;
	}

	public String getDriveVersion() {
		return driveVersion;
	}
	public void setDriveVersion(String driveVersion) {
		this.driveVersion = driveVersion;
	}


	public Integer getScreenResolutionX() {
		return screenResolutionX;
	}


	public void setScreenResolutionX(Integer screenResolutionX) {
		this.screenResolutionX = screenResolutionX;
	}


	public Integer getScreenResolutionY() {
		return screenResolutionY;
	}


	public void setScreenResolutionY(Integer screenResolutionY) {
		this.screenResolutionY = screenResolutionY;
	}
}