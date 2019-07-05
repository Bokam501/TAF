package com.hcl.atf.taf.model;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="generic_devices")
@DiscriminatorValue("6")
public class StorageDrive extends GenericDevices{
	private Long storageSize;
	private String storageSizeUnit;	
	private String firmware;
	private String bootLoader;
	private String driveVersion;
	private DeviceMakeMaster deviceMakeMaster;
	private Processor processor;
	
	@Column(name = "storageSize", nullable = false)
	public Long getStorageSize() {
		return storageSize;
	}
	public void setStorageSize(Long storageSize) {
		this.storageSize = storageSize;
	}
	@Column(name = "storageSizeUnit", nullable = false)
	public String getStorageSizeUnit() {
		return storageSizeUnit;
	}
	public void setStorageSizeUnit(String storageSizeUnit) {
		this.storageSizeUnit = storageSizeUnit;
	}
	@Column(name = "firmware", length = 100)
	public String getFirmware() {
		return firmware;
	}
	public void setFirmware(String firmware) {
		this.firmware = firmware;
	}
	@Column(name = "bootLoader", length = 100)
	public String getBootLoader() {
		return bootLoader;
	}
	public void setBootLoader(String bootLoader) {
		this.bootLoader = bootLoader;
	}
	
	@Column(name = "driveVersion", length = 100)
	public String getDriveVersion() {
		return driveVersion;
	}
	public void setDriveVersion(String driveVersion) {
		this.driveVersion = driveVersion;
	}
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "deviceMakeId")
	public DeviceMakeMaster getDeviceMakeMaster() {
		return deviceMakeMaster;
	}
	public void setDeviceMakeMaster(DeviceMakeMaster deviceMakeMaster) {
		this.deviceMakeMaster = deviceMakeMaster;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "processorId")
	public Processor getProcessor() {
		return processor;
	}
	public void setProcessor(Processor processor) {
		this.processor = processor;
	}
}
