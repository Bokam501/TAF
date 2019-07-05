package com.hcl.atf.taf.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hcl.atf.taf.dao.ActivityEffortTrackerDAO;
import com.hcl.atf.taf.model.ActivityEffortTracker;
import com.hcl.atf.taf.model.json.JsonActivityEffortTracker;
import com.hcl.atf.taf.service.ActivityEffortTrackerService;
@Service
public class ActivityEffortTrackerServiceImpl implements ActivityEffortTrackerService {
	private static final Log log = LogFactory.getLog(ActivityEffortTrackerServiceImpl.class);
	@Autowired
	ActivityEffortTrackerDAO activityEffortTrackerDAO;
	 
	@Override
	@Transactional
	public List<ActivityEffortTracker> listActivityEffortTracker() {
		List<ActivityEffortTracker> listActivityEffortTracker = null;
		List<JsonActivityEffortTracker> listJsonActivityEffortTracker = new ArrayList<JsonActivityEffortTracker>();
		try {
			listActivityEffortTracker = activityEffortTrackerDAO.listActivityEffortTracker();
			for(ActivityEffortTracker aet: listActivityEffortTracker){
				listJsonActivityEffortTracker.add(new JsonActivityEffortTracker(aet));	
			}
		} catch (Exception e) {
			log.error("ERROR  ",e);
		}
		return listActivityEffortTracker;
	}
	
	@Override
	public ActivityEffortTracker getActivityEffortTrackerById(
			int effortTrackerId) {
		return activityEffortTrackerDAO.getActivityEffortTrackerById(effortTrackerId);
	}

	@Override
	public void addActivityEffortTracker(
			ActivityEffortTracker activityEffortTracker) {
		activityEffortTrackerDAO.addActivityEffortTracker(activityEffortTracker);
	}
	
	@Override
	@Transactional
	public void updateActivityEffortTracker(ActivityEffortTracker activityEffortTracker) {
		activityEffortTrackerDAO.updateActivityEffortTracker(activityEffortTracker);
	}

	@Override
	@Transactional
	public List<JsonActivityEffortTracker> listActivityEffortTrackerByActivityTaskId(
			Integer activityTaskId, int initializationLevel) {
		List<JsonActivityEffortTracker> listOfJsonActivityEffortTrackers = new ArrayList<JsonActivityEffortTracker>();
		List<ActivityEffortTracker> listOfActivityEffortTrackers =activityEffortTrackerDAO.listActivityEffortTrackerByActivityTaskId(activityTaskId, initializationLevel);
		if(listOfActivityEffortTrackers != null && listOfActivityEffortTrackers.size()>0){
			for (ActivityEffortTracker activityEffortTracker : listOfActivityEffortTrackers) {
				JsonActivityEffortTracker jsonActivityEffortTracker = new JsonActivityEffortTracker(activityEffortTracker);
				listOfJsonActivityEffortTrackers.add(jsonActivityEffortTracker);
			}
		}
		return listOfJsonActivityEffortTrackers;
	}
	
	@Override
	@Transactional
	public Integer getTotalEffortsByTaskId(int taskId){
		 
		return activityEffortTrackerDAO.getTotalEffortsByTaskId(taskId);
	}

	@Override
	@Transactional
	public Integer countAllEffortTracker(Date startDate, Date endDate) {
		return activityEffortTrackerDAO.countAllEffortTracker(startDate,endDate);
	}

	@Override
	public List<ActivityEffortTracker> listAllEffortTracker(int startIndex,int pageSize, Date startDate, Date endDate) {
		return activityEffortTrackerDAO.listAllEffortTracker(startIndex,pageSize,startDate,endDate);
	}
}
