package com.hcl.atf.taf.service;

import java.util.List;

import com.hcl.atf.taf.model.ActivityType;

public interface ActivityTypeMasterService {
	ActivityType getActivityTypeMasterById(Integer activityTypeMasterId,Integer initializationLevel);	
	List<ActivityType> listActivityTypeMaster();

}
