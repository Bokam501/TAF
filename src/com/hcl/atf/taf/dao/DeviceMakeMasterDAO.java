package com.hcl.atf.taf.dao;

import java.util.List;

import com.hcl.atf.taf.model.DeviceMakeMaster;

public interface DeviceMakeMasterDAO  {	 

	List<DeviceMakeMaster> list();
	void add(DeviceMakeMaster deviceMakeMaster);
	DeviceMakeMaster getDeviceMakeByName(String make);
	
}
