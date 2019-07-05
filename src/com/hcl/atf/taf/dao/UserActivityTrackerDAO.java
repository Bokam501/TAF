/**
 * 
 */
package com.hcl.atf.taf.dao;

import java.util.List;

import com.hcl.atf.taf.model.UserActivityTracker;

/**
 * @author silambarasur
 *
 */
public interface UserActivityTrackerDAO {
	
	UserActivityTracker getUserActivityTracker(Integer userId);
	List<UserActivityTracker> getUserActivityTrakerByUserId(Integer userId);
	List<UserActivityTracker> getUserActivityTrakerList();
	void addUserActivityTracker(UserActivityTracker userActivityTracker);
	void updateUserActivityTracker(UserActivityTracker userActivityTracker);
	

}
