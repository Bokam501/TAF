package com.hcl.atf.taf.service;

import com.hcl.atf.taf.model.json.JsonSchedule;

public interface SchedulerService {

	JsonSchedule viewEntitySchedule(int entityId, String entityType);

	JsonSchedule addUpdateSchedule(JsonSchedule jsonSchedule);

	JsonSchedule viewUpcomingFireTimes(int entityId, String entityType, int numberTimes);

	JsonSchedule viewUpcomingFireTimes(JsonSchedule jsonSchedule, int numberTimes);

	JsonSchedule runJobNow(Integer entityId, String entityType);
	JsonSchedule deleteSchedule(JsonSchedule jsonSchedule);
	
}
