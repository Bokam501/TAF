/**
 * 
 */
package com.hcl.atf.taf.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hcl.atf.taf.dao.ActivityTaskTypeDAO;
import com.hcl.atf.taf.model.ActivityTaskType;
import com.hcl.atf.taf.service.ActivityTaskTypeService;

/**
 * @author silambarasur
 *
 */
@Service
public class ActivityTaskTypeServiceImpl implements ActivityTaskTypeService {
	@Autowired
	public ActivityTaskTypeDAO activityTaskTypeDAO;
	
	@Override
	@Transactional
	public List<ActivityTaskType> getActivityTaskTypesForProduct(Integer productId){
		return activityTaskTypeDAO.getActivityTaskTypesForProduct(productId);
	}

}
