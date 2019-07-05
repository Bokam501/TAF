package com.hcl.atf.taf.dao;

import java.util.List;

import com.hcl.atf.taf.model.DeviceType;

public interface DeviceTypeDAO  {
	List<DeviceType> list();
	void add(DeviceType deviceType);
	DeviceType getDeviceTypeByName(String deviceTypeName);
	DeviceType getDeviceTypeByTypeId(Integer deviceTypeId);
}
