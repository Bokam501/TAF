package com.hcl.atf.taf.dao;

import java.util.List;

import com.hcl.atf.taf.model.DeviceList;
import com.hcl.atf.taf.model.HostHeartbeat;
import com.hcl.atf.taf.model.HostList;
import com.hcl.atf.taf.model.UserList;

public interface HostHeartbeatDAO  {	 
	void add (HostHeartbeat hostHeartbeat);
	void update (HostHeartbeat hostHeartbeat);
	void delete (HostHeartbeat hostHeartbeat);	
	List<HostHeartbeat> list();
	HostHeartbeat getByHostId(int hostId);
	void clearAll();
	List<HostHeartbeat> listExpiredHost(long timeInMillisecs);	
}
