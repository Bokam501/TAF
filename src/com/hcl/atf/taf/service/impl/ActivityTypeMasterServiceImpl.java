package com.hcl.atf.taf.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hcl.atf.taf.dao.ActivityTypeMasterDAO;
import com.hcl.atf.taf.model.ActivityType;
import com.hcl.atf.taf.service.ActivityTypeMasterService;
@Service
public class ActivityTypeMasterServiceImpl implements ActivityTypeMasterService{
	@Autowired
	ActivityTypeMasterDAO activityTypeMasterDAO;

	@Override
	@Transactional
	public ActivityType getActivityTypeMasterById(Integer activityTypeMasterId,
			Integer initializationLevel) {
		return activityTypeMasterDAO.getActivityTypeMasterById(activityTypeMasterId, initializationLevel);
	}

	@Override
	@Transactional
	public List<ActivityType> listActivityTypeMaster() {
		return activityTypeMasterDAO.listActivityTypeMaster();
	}
}
