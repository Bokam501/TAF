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
@DiscriminatorValue("4")
public class ServerType extends GenericDevices{

	private String hostName;
	private String ip;
	private String systemName;
	private SystemType systemType;
	private Processor processor;
	
	@Column(name = "hostName", length = 100)
	public String getHostName() {
		return hostName;
	}
	public void setHostName(String hostName) {
		this.hostName = hostName;
	}
	@Column(name = "ip", length = 100)
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	@Column(name = "systemName", length = 100)
	public String getSystemName() {
		return systemName;
	}
	public void setSystemName(String systemName) {
		this.systemName = systemName;
	}
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "systemtypeId")
	public SystemType getSystemType() {
		return systemType;
	}
	public void setSystemType(SystemType systemType) {
		this.systemType = systemType;
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
