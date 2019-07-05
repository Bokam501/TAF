package com.hcl.atf.taf.service;

import java.util.List;

import com.hcl.atf.taf.model.HostList;
import com.hcl.atf.taf.model.HostPlatformMaster;
import com.hcl.atf.taf.model.RunConfiguration;
import com.hcl.atf.taf.model.json.JsonHostList;

public interface HostListService {
	void add (HostList hostList);
	void update (HostList hostList);
	void delete (int hostId);
	List<HostList> list();	
	List<HostList> list(int startIndex, int pageSize);
	List<HostList> listByHostIp(String hostIP);
	List<HostPlatformMaster> platformsList();
	int getTotalRecords();
	HostList getHostById(int hostId);
	List<HostList> listByHostName(String hostName);
	void CheckIfHostActive();
	void CheckIfHostActive(int hostId);
	void resetHostsStatus();
	boolean isHostExistingByName(String hostName);	
	void resetHostsStatus(int hostId);
	void isHostActive(int hostId);
	List<JsonHostList> getUnMappedHostListOfProductfromRunConfigurationWorkPackageLevel(int productId, int ecId, int runConfigStatus, int workpackageId);
	List<JsonHostList> getUnMappedHostListOfProductfromRunConfigurationTestRunPlanLevel(int productId, int ecId, int runConfigStatus, int testRunPlanId);
	RunConfiguration isRunConfigurationOfHostOfWorkPackageExisting(Integer environmentCombinationId, Integer workpackageId,	Integer hostId);
	RunConfiguration isRunConfigurationOfHostOfTestRunPlanExisting(Integer environmentCombinationId, Integer testRunPlanId,	Integer hostId);	
	List<HostList> listHostIdByStatus(String status);
	void deleteWPReportFolder();
}
