package com.hcl.atf.taf.dao;

import java.util.List;

import com.hcl.atf.taf.model.DevicePlatformMaster;

public interface DevicePlatformMasterDAO  {	 

	List<DevicePlatformMaster> list(int productId);

	List<DevicePlatformMaster> list();
	
}
