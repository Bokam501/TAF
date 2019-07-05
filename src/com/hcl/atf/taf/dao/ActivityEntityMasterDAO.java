package com.hcl.atf.taf.dao;

import java.util.List;

import com.hcl.atf.taf.model.ActivityEntityMaster;
import com.hcl.atf.taf.model.ActivityMaster;

public interface ActivityEntityMasterDAO {
	List<ActivityEntityMaster> listActivityEntityMasters();
	ActivityEntityMaster getActivityEntityMasterById(Integer activityEntityMaster);
	ActivityEntityMaster getActivityEntityMasterByName(String typeName);
	List<ActivityMaster> getActivityTypesByEnagementAndProductId(Integer testFactoryId, Integer productId);
}
