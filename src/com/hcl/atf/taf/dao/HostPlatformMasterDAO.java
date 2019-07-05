package com.hcl.atf.taf.dao;

import java.util.List;

import com.hcl.atf.taf.model.DeviceList;
import com.hcl.atf.taf.model.HostPlatformMaster;
import com.hcl.atf.taf.model.UserList;

public interface HostPlatformMasterDAO  {	 

	List<HostPlatformMaster> list();
	
}
