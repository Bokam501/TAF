package com.hcl.atf.taf.dao;

import java.util.List;

import com.hcl.atf.taf.model.ActivitySecondaryStatusMaster;

public interface ActivitySecondaryStatusMasterDAO {
	List<ActivitySecondaryStatusMaster> listActivitySecondaryStatusMaster();
	ActivitySecondaryStatusMaster getSecondaryStatusById(int secondaryStatusId);
	List<ActivitySecondaryStatusMaster> listActivitySecondaryStatusMaster(
			Integer statusId, Integer jtStartIndex, Integer jtPageSize, Integer initializationLevel);
	List<ActivitySecondaryStatusMaster> listActivitySecondaryStatusListForEffort(
			Integer statusId, Integer jtStartIndex, Integer jtPageSize,
			Integer initializationLevel);
	ActivitySecondaryStatusMaster getSecondaryStatusByName(String statusName);
	ActivitySecondaryStatusMaster getSecondaryStatusByNameAndDimentionId(String statusName,Integer dimentionId);
	List<ActivitySecondaryStatusMaster> getSecondaryStatusbyDimentionId(Integer dimentionId);
}
