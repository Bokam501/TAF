package com.hcl.atf.taf.dao;

import java.util.List;

import com.hcl.atf.taf.model.DeviceList;
import com.hcl.atf.taf.model.HostList;
import com.hcl.atf.taf.model.RunConfiguration;
import com.hcl.atf.taf.model.UserList;
import com.hcl.atf.taf.model.json.JsonHostList;

public interface HostListDAO  {	 
	void add (HostList hostList);
	void update (HostList hostList);
	void delete (HostList hostList);
	List<HostList> list();
	HostList getByHostId(int hostId);
	List<HostList> list(int startIndex, int pageSize);
	List<HostList> listByHostIP(String hostIP);  
	List<HostList> listByHostName(String hostName);
	List<HostList> listHostIdByStatus(String status);
	int getTotalRecords();
	void resetHostsStatus();
	boolean isHostExistingByName(String hostName);
	void resetHostsStatus(int hostId);
	List<JsonHostList> getUnMappedHostListOfProductfromRunConfigurationWorkPackageLevel(int productId, int ecId, int runConfigStatus, int workpackageId);
	RunConfiguration isRunConfigurationOfHostOfWorkPackageExisting(Integer environmentCombinationId, Integer workpackageId,	Integer hostId);
	List<JsonHostList> getUnMappedHostListOfProductfromRunConfigurationTestRunPlanLevel(
			int productId, int ecId, int runConfigStatus, int testRunPlanId);
	RunConfiguration isRunConfigurationOfHostOfTestRunPlanExisting(Integer environmentCombinationId, Integer testRunPlanId,	Integer hostId);
}
