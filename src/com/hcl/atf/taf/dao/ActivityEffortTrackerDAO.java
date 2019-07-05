package com.hcl.atf.taf.dao;

import java.util.Date;
import java.util.List;

import com.hcl.atf.taf.model.ActivityEffortTracker;

public interface ActivityEffortTrackerDAO {
	List<ActivityEffortTracker> listActivityEffortTracker();
	ActivityEffortTracker getActivityEffortTrackerById(int effortTrackerId);
	void addActivityEffortTracker(ActivityEffortTracker activityEffortTracker);	
	List<ActivityEffortTracker> listActivityEffortTrackerByActivityTaskId(
			Integer activityTaskId, int initializationLevel);
	void updateActivityEffortTracker(ActivityEffortTracker activityEffortTracker);
	Integer getTotalEffortsByTaskId(int activityTaskId);
	Integer countAllEffortTracker(Date startDate, Date endDate);
	List<ActivityEffortTracker> listAllEffortTracker(int startIndex, int pageSize, Date startDate,Date endDate);

}
