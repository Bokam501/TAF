/**
 * 
 */
package com.hcl.atf.taf.service.impl;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hcl.atf.taf.dao.UserActivityTrackerDAO;
import com.hcl.atf.taf.model.UserActivityTracker;
import com.hcl.atf.taf.service.UserActivityTrackerService;

/**
 * @author silambarasur
 *
 */
@Service
public class UserActivityTrackerServiceImpl implements UserActivityTrackerService{
	private static final Log log = LogFactory
			.getLog(UserActivityTrackerServiceImpl.class);

	
	@Autowired
	private UserActivityTrackerDAO userActivityTrackerDAO;
	
	@Override
	@Transactional
	public UserActivityTracker getUserActivityTracker(Integer userId) {
		try {
		return userActivityTrackerDAO.getUserActivityTracker(userId);
		}catch(Exception e) {
			log.error("getUserActivityTracker service failed ",e);
		}
		return null;
	}
	@Override
	@Transactional
	public List<UserActivityTracker> getUserActivityTrakerByUserId(Integer userId) {
		try {
			userActivityTrackerDAO.getUserActivityTrakerByUserId(userId);
		}catch(Exception e) {
			log.error("getUserActivityTrakerByUserId service failed ",e);
		}
		return null;
	}
	@Override
	@Transactional
	public List<UserActivityTracker> getUserActivityTrakerList() {
		try {
			return userActivityTrackerDAO.getUserActivityTrakerList();
		}catch(Exception e) {
			log.error("getUserActivityTrakerList service failed ",e);
		}
		return null;
	}
	@Override
	@Transactional
	public void addUserActivityTracker(UserActivityTracker userActivityTracker) {
		try {
			userActivityTrackerDAO.addUserActivityTracker(userActivityTracker);
		}catch(Exception e) {
			log.error("addUserActivityTracker service failed ",e);
		}
	}
	
	@Override
	@Transactional
	public void updateUserActivityTracker(UserActivityTracker userActivityTracker) {
		try {
			userActivityTrackerDAO.updateUserActivityTracker(userActivityTracker);
		}catch(Exception e) {
			log.error("addUserActivityTracker service failed ",e);
		}
	}
	
}
