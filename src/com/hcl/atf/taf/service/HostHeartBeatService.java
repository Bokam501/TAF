package com.hcl.atf.taf.service;

import java.util.List;

import com.hcl.atf.taf.model.HostHeartbeat;

public interface HostHeartBeatService {
	void add (HostHeartbeat hostHeartbeat);
	void update (HostHeartbeat hostHeartbeat);
	void delete (int hostId);	
	List<HostHeartbeat> list();
	HostHeartbeat getByHostId(int hostId);
	void clearAll();
	List<HostHeartbeat> listExpiredHost(long timeInMillisecs);
	boolean setHostResponseToHeartbeatAsJobsAvailable(Integer hostId);
	boolean setHostResponseToHeartbeat(Integer hostId, boolean response, Short responseCode);	

}
