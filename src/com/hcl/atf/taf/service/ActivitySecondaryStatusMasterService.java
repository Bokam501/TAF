package com.hcl.atf.taf.service;

import java.util.List;

import com.hcl.atf.taf.model.ActivitySecondaryStatusMaster;

public interface ActivitySecondaryStatusMasterService {
	
	List<ActivitySecondaryStatusMaster> listActivitySecondaryStatusMaster(Integer jtStartIndex, Integer jtPageSize, Integer initializationLevel);
	ActivitySecondaryStatusMaster getSecondaryStatusById(int secondaryStatusId);
	List<ActivitySecondaryStatusMaster> listActivitySecondaryStatusMaster(Integer statusId, Integer jtStartIndex, Integer jtPageSize, Integer initializationLevel);
	List<ActivitySecondaryStatusMaster> getSecondaryStatusbyDimentionId(Integer dimentionId);
	


}
