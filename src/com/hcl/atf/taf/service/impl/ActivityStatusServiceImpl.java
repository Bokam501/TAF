package com.hcl.atf.taf.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hcl.atf.taf.dao.ActivityStatusDAO;
import com.hcl.atf.taf.model.ActivityStatus;
import com.hcl.atf.taf.service.ActivityStatusService;


@Service
public class ActivityStatusServiceImpl implements ActivityStatusService {

	
	@Autowired
	ActivityStatusDAO activityStatusDAO;

	
	@Override
	public ActivityStatus getStatusById(Integer resultId) {
		return activityStatusDAO.getStatusById(resultId);		
	}

	@Override
	public ActivityStatus getStatusByName(String resultName) {
		return activityStatusDAO.getStatusByName(resultName);
	}

	@Override
	public List<ActivityStatus> listStatusByWorkItemId(Integer workItemId) {
		return null;
	}

	@Override
	public List<ActivityStatus> listActivityStatus() {
		return activityStatusDAO.listActivityStatuses();

	}
	
	public List<ActivityStatus> listActivityStatusByDimensionId(Integer dimensionId){
		return activityStatusDAO.listActivityStatusByDimensionId(dimensionId);
	}
	
	public ActivityStatus getStatusNameByDimensionId(String statusName,Integer dimensionId){
		return activityStatusDAO.getStatusNameByDimensionId(statusName, dimensionId);
	}

}
