package com.hcl.atf.taf.mongodb.dao;

import java.util.List;

import com.hcl.atf.taf.mongodb.model.TestCaseStepsListMongo;

public interface TestCaseStepsListMongoDAO {

	void save(TestCaseStepsListMongo testCaseStepsListMongo);

	void save(List<TestCaseStepsListMongo> testCaseStepsListMongo);
	
}
