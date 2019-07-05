package com.hcl.atf.taf.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hcl.atf.taf.dao.ActivityResultDAO;
import com.hcl.atf.taf.model.ActivityResult;
import com.hcl.atf.taf.service.ActivityResultService;


@Service
public class ActivityResultServiceImpl implements ActivityResultService{
	@Autowired
	ActivityResultDAO activityResultDAO;

	@Override
	public ActivityResult getResultById(Integer resultId) {
		return activityResultDAO.getResultsById(resultId);		
	}

	@Override
	public ActivityResult getResultByName(String statusName) {
		return activityResultDAO.getResultsByName(statusName);
	}

	@Override
	public List<ActivityResult> listResultByActivityId(Integer activityId) {
		return null;
	}

	@Override
	public List<ActivityResult> listActivityResult() {
		return activityResultDAO.listActivityResults();
	}

}
