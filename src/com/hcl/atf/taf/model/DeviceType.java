package com.hcl.atf.taf.model;

import static javax.persistence.GenerationType.IDENTITY;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

@Entity
@Table(name = "devicetype")
public class DeviceType {
	private Integer deviceTypeId;
	private String deviceTypeName;
	private DeviceType deviceType_parentId;
	
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "deviceTypeId", unique = true, nullable = false)
	public Integer getDeviceTypeId() {
		return deviceTypeId;
	}
	public void setDeviceTypeId(Integer deviceTypeId) {
		this.deviceTypeId = deviceTypeId;
	}
	@Column(name = "deviceTypeName", length = 45)
	public String getDeviceTypeName() {
		return deviceTypeName;
	}
	public void setDeviceTypeName(String deviceTypeName) {
		this.deviceTypeName = deviceTypeName;
	}
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "deviceType_parentId" )
	@NotFound(action=NotFoundAction.IGNORE)
	public DeviceType getDeviceType_parentId() {
		return deviceType_parentId;
	}
	public void setDeviceType_parentId(DeviceType deviceType_parentId) {
		this.deviceType_parentId = deviceType_parentId;
	}
	
}
