package com.hcl.atf.taf.dao;

import java.util.List;

import com.hcl.atf.taf.model.ActivityMaster;
import com.hcl.atf.taf.model.ActivityType;
import com.hcl.atf.taf.model.ExecutionTypeMaster;
import com.hcl.ilcm.workflow.model.WorkflowMasterEntityMapping;


public interface ActivityTypeDAO {
	
	List<ActivityMaster> listActivityTypes(Integer testFactoryId, Integer productId, Integer jtStartIndex, Integer jtPageSize, int initializationLevel, Boolean isConsildated);
    List<ActivityType> listActivityTypes(int entityStatusActive,Integer jtStartIndex, Integer jtPageSize,Integer initializationLevel);
    void addActivityMaster(ActivityMaster activityMaster);
	void updateActivityMaster(ActivityMaster activityMaster);
	ActivityMaster getActivityMasterById(Integer activityMasterId);
	List<ActivityType> listActivityTypesByActivityGroupId(int entityStatusActive, int activityGroupId, Integer jtStartIndex,
			Integer jtPageSize, Integer initializationLevel);
	ActivityMaster getActivityMasterByName(String activityMasterName);
	ExecutionTypeMaster getCategoryByName(String categoryName);
	ActivityMaster getActivityMasterByNameAndProductId(String activityMasterName,Integer productId);
	ActivityMaster uploadActivityMaster(ActivityMaster activityMaster);
	Boolean isActivityMasterAvailable(String activityTypeName, Integer referenceActivityMasterId, Integer testFactoryId, Integer productId);
	List<ActivityMaster> listActivityTypesByEnagementIdAndProductId(Integer testFactoryId, Integer productId, Boolean isConsildated);
	ActivityMaster getActivityMasterByNameInProductAndTestFactory(String activityTypeName, Integer productId, Integer testFactoryId);
	List<WorkflowMasterEntityMapping> listActivityTypeByProductAndEntityAndMappingLevel(Integer productId, Integer entityTypeId, Integer entityId, String mappingLevel);
}
