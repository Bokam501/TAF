package com.hcl.atf.taf.dao;

import java.util.List;

import com.hcl.atf.taf.model.DeviceMakeMaster;
import com.hcl.atf.taf.model.DeviceModelMaster;

public interface DeviceModelMasterDAO  {	 

	List<DeviceModelMaster> list();
	List<DeviceModelMaster> list(String deviceMake);
	DeviceModelMaster getDeviceModelByName(String model);
	void add(DeviceModelMaster deviceModelMaster);
	
}
