package com.hcl.atf.taf.model;

// Generated Feb 4, 2014 4:30:16 PM by Hibernate Tools 3.4.0.CR1

import java.util.HashSet;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * DevicePlatformMaster generated by hbm2java
 */
@Entity
@Table(name = "device_platform_master")
public class DevicePlatformMaster implements java.io.Serializable {

	private String devicePlatformName;
	private Set<TestEnviromentMaster> testEnviromentMasters = new HashSet<TestEnviromentMaster>(
			0);
	private Set<DevicePlatformVersionListMaster> devicePlatformVersionListMasters = new HashSet<DevicePlatformVersionListMaster>(
			0);

	public DevicePlatformMaster() {
	}

	public DevicePlatformMaster(String devicePlatformName) {
		this.devicePlatformName = devicePlatformName;
	}

	public DevicePlatformMaster(
			String devicePlatformName,
			Set<TestEnviromentMaster> testEnviromentMasters,
			Set<DevicePlatformVersionListMaster> devicePlatformVersionListMasters) {
		this.devicePlatformName = devicePlatformName;
		this.testEnviromentMasters = testEnviromentMasters;
		this.devicePlatformVersionListMasters = devicePlatformVersionListMasters;
	}

	@Id
	@Column(name = "devicePlatformName", unique = true, nullable = false, length = 30)
	public String getDevicePlatformName() {
		return this.devicePlatformName;
	}

	public void setDevicePlatformName(String devicePlatformName) {
		this.devicePlatformName = devicePlatformName;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "devicePlatformMaster")
	public Set<TestEnviromentMaster> getTestEnviromentMasters() {
		return this.testEnviromentMasters;
	}

	public void setTestEnviromentMasters(
			Set<TestEnviromentMaster> testEnviromentMasters) {
		this.testEnviromentMasters = testEnviromentMasters;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "devicePlatformMaster")
	public Set<DevicePlatformVersionListMaster> getDevicePlatformVersionListMasters() {
		return this.devicePlatformVersionListMasters;
	}

	public void setDevicePlatformVersionListMasters(
			Set<DevicePlatformVersionListMaster> devicePlatformVersionListMasters) {
		this.devicePlatformVersionListMasters = devicePlatformVersionListMasters;
	}

}
