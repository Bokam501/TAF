package com.hcl.atf.taf.mongodb.dao;

import java.util.List;

import com.hcl.atf.taf.mongodb.model.ActivityTaskMongo;

public interface ActivityTaskMongoDAO {
	
	void save(ActivityTaskMongo activityTaskMongo);
	void save(List<ActivityTaskMongo> activityTaskMongo);
	void deleteActivityTaskFromMongoDb(Integer activityTaskId);
	
}
