package com.hcl.atf.taf.mongodb.dao;

import java.util.List;

import com.hcl.atf.taf.mongodb.model.TestRunJobMongo;

public interface TestRunJobMongoDAO {

	void save(TestRunJobMongo testRunJobMongo);

	void save(List<TestRunJobMongo> testRunJobsMongo);
	
}
