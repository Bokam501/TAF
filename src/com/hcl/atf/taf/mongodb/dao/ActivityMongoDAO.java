package com.hcl.atf.taf.mongodb.dao;

import com.hcl.atf.taf.mongodb.model.ActivityMongo;

public interface  ActivityMongoDAO {
		
	void save(ActivityMongo activityMongo);

	void deleteActivityFromMongodb(Integer activityId);
	
	


}
