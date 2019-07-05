package com.hcl.atf.taf.service;

import java.util.List;

import com.hcl.atf.taf.model.ActivityEntityMaster;

public interface ActivityEntityMasterService {

	ActivityEntityMaster getActivityEntityMasterById(
			Integer activityEntityMasterById);
	ActivityEntityMaster getActivityEntityMasterByName(
			String activityEntityMasterByName);

	List<ActivityEntityMaster> listActivityEntityMaster();

}
