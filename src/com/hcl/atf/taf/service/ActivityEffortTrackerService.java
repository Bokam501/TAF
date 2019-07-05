package com.hcl.atf.taf.service;

import java.util.Date;
import java.util.List;

import com.hcl.atf.taf.model.ActivityEffortTracker;
import com.hcl.atf.taf.model.json.JsonActivityEffortTracker;

public interface ActivityEffortTrackerService {

	List<ActivityEffortTracker> listActivityEffortTracker();
	ActivityEffortTracker getActivityEffortTrackerById(int effortTrackerId);
	void addActivityEffortTracker(ActivityEffortTracker activityEffortTracker);	
	List<JsonActivityEffortTracker> listActivityEffortTrackerByActivityTaskId(
			Integer activityTaskId, int initializationLevel);
	void updateActivityEffortTracker(ActivityEffortTracker activityEffortTracker);
	Integer getTotalEffortsByTaskId(int taskId);
	Integer countAllEffortTracker(Date startDate, Date endDate);
	List<ActivityEffortTracker> listAllEffortTracker(int startIndex, int pageSize,Date startDate, Date endDate);
}
