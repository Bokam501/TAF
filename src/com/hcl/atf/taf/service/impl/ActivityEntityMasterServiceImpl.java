package com.hcl.atf.taf.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hcl.atf.taf.dao.ActivityEntityMasterDAO;
import com.hcl.atf.taf.model.ActivityEntityMaster;
import com.hcl.atf.taf.service.ActivityEntityMasterService;

@Service
public class ActivityEntityMasterServiceImpl implements ActivityEntityMasterService{
	

		@Autowired
		ActivityEntityMasterDAO activityEntityMasterDAO;


		@Override
		public ActivityEntityMaster getActivityEntityMasterById(
				Integer activityEntityMasterById) {
			return activityEntityMasterDAO.getActivityEntityMasterById(activityEntityMasterById);
		}

		@Override
		public List<ActivityEntityMaster> listActivityEntityMaster() {
			return activityEntityMasterDAO.listActivityEntityMasters();
		}

		@Override
		public ActivityEntityMaster getActivityEntityMasterByName(
				String activityEntityMasterByName) {
			return activityEntityMasterDAO.getActivityEntityMasterByName(activityEntityMasterByName);
		}
}
