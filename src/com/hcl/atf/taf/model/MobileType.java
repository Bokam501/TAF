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
@DiscriminatorValue("5")
public class MobileType extends GenericDevices{

	private String kernelNumber;
	private String buildNumber;
	private DeviceMakeMaster deviceMakeMaster;

	@Column(name = "kernelNumber", length = 45)
	public String getKernelNumber() {
		return kernelNumber;
	}
	public void setKernelNumber(String kernelNumber) {
		this.kernelNumber = kernelNumber;
	}
	@Column(name = "buildNumber", length = 45)
	public String getBuildNumber() {
		return buildNumber;
	}
	public void setBuildNumber(String buildNumber) {
		this.buildNumber = buildNumber;
	}
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "deviceMakeId")
	public DeviceMakeMaster getDeviceMakeMaster() {
		return deviceMakeMaster;
	}
	public void setDeviceMakeMaster(DeviceMakeMaster deviceMakeMaster) {
		this.deviceMakeMaster = deviceMakeMaster;
	}
}