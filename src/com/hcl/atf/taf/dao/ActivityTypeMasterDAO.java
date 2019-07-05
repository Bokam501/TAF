package com.hcl.atf.taf.dao;

import java.util.List;

import com.hcl.atf.taf.model.ActivityType;

public interface ActivityTypeMasterDAO {	
	List<ActivityType> listActivityTypeMaster();	
	
	ActivityType getActivityTypeMasterById(Integer activityTypeMasterId,
			Integer initializationLevel);
}
