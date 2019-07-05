package com.hcl.atf.taf.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.hcl.atf.taf.dao.ActivityGroupDAO;
import com.hcl.atf.taf.model.ActivityGroup;
import com.hcl.atf.taf.service.ActivityGroupService;
@Repository
public class ActivityGroupServiceImpl implements ActivityGroupService{
	@Autowired
	ActivityGroupDAO activityGroupDAO;

	@Override
	@Transactional
	public List<ActivityGroup> listActivityGroups(int status,
			Integer jtStartIndex, Integer jtPageSize) {
		return activityGroupDAO.listActivityGroups(status, jtStartIndex, jtPageSize);
	}

	

	
}
