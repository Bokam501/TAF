package com.hcl.atf.taf.service;

import java.util.List;

import com.hcl.atf.taf.model.ActivityMaster;
import com.hcl.atf.taf.model.ActivityType;
import com.hcl.atf.taf.model.ExecutionTypeMaster;
import com.hcl.atf.taf.model.json.JsonActivityMaster;


public interface ActivityTypeService {

	void addActivityMaster(ActivityMaster activityMaster);
	void updateActivityMaster(ActivityMaster activityMaster);
	Boolean isActivityMasterAvailable(String activityTypeName, Integer referenceActivityMasterId, Integer testFactoryId, Integer productId);
	ActivityMaster getActivityMasterById(Integer activityMasterId);
	List<JsonActivityMaster> listActivityTypes(Integer testFactoryId, Integer productId, Integer jtStartIndex, Integer jtPageSize, int initializationLevel, Boolean isConsildated);
	List<ActivityType> listActivityTypes(int entityStatusActive,Integer jtStartIndex, Integer jtPageSize,Integer initializationLevel);
	List<ActivityType> listActivityTypesByActivityGroupId(int entityStatusActive, int activityGroupId, Integer jtStartIndex, Integer jtPageSize,Integer initializationLevel);
	ActivityMaster getActivityMasterByName(String activityMasterName);
	ExecutionTypeMaster getCategoryByName(String categoryName);
	ActivityMaster getActivityMasterByNameAndProductId(String activityMasterName,Integer productId);
	List<JsonActivityMaster> listActivityTypesByEnagementIdAndProductId(Integer testFactoryId, Integer productId, Boolean isConsildated);
	ActivityMaster getActivityMasterByNameInProductAndTestFactory(String activityTypeName, Integer productId, Integer testFactoryId);
}
