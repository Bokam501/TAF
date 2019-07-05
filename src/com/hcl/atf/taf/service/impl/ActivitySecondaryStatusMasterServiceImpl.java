package com.hcl.atf.taf.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hcl.atf.taf.dao.ActivitySecondaryStatusMasterDAO;
import com.hcl.atf.taf.model.ActivitySecondaryStatusMaster;
import com.hcl.atf.taf.model.json.JsonActivitySecondaryStatusMaster;
import com.hcl.atf.taf.service.ActivitySecondaryStatusMasterService;

@Service
public class ActivitySecondaryStatusMasterServiceImpl implements ActivitySecondaryStatusMasterService{
	private static final Log log = LogFactory.getLog(ActivitySecondaryStatusMasterServiceImpl.class);
	@Autowired
	ActivitySecondaryStatusMasterDAO activitySecondaryStatusMasterDAO;
	
	@Override
	@Transactional
	public List<ActivitySecondaryStatusMaster> listActivitySecondaryStatusMaster(Integer jtStartIndex, Integer jtPageSize, Integer initializationLevel) {
		List<ActivitySecondaryStatusMaster> listActivitySecondaryStatusMaster = null;
		List<JsonActivitySecondaryStatusMaster> listJsonActivitySecondaryStatusMaster = new ArrayList<JsonActivitySecondaryStatusMaster>();
		try {
			listActivitySecondaryStatusMaster = activitySecondaryStatusMasterDAO.listActivitySecondaryStatusMaster();
			for(ActivitySecondaryStatusMaster ct: listActivitySecondaryStatusMaster){
				listJsonActivitySecondaryStatusMaster.add(new JsonActivitySecondaryStatusMaster(ct));	
			}
		} catch (Exception e) {
			log.error("ERROR listing ActivitySecondaryStatusMaster :  ",e);
		}
		return listActivitySecondaryStatusMaster;
	}

	@Override
	@Transactional
	public ActivitySecondaryStatusMaster getSecondaryStatusById(
			int secondaryStatusId) {
		return activitySecondaryStatusMasterDAO.getSecondaryStatusById(secondaryStatusId);
	}

	@Override
	@Transactional
	public List<ActivitySecondaryStatusMaster> listActivitySecondaryStatusMaster(
			Integer statusId, Integer jtStartIndex, Integer jtPageSize,
			Integer initializationLevel) {
		return activitySecondaryStatusMasterDAO.listActivitySecondaryStatusMaster(statusId,  jtStartIndex, jtPageSize,initializationLevel);
    }
	
	@Override
	@Transactional
	public List<ActivitySecondaryStatusMaster> getSecondaryStatusbyDimentionId(Integer dimentionId){
		return activitySecondaryStatusMasterDAO.getSecondaryStatusbyDimentionId(dimentionId);
	}
}
