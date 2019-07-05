package com.hcl.atf.taf.mongodb.dao;

import java.util.List;

import com.hcl.atf.taf.mongodb.model.ISETestExecutionCollectionMongo;
import com.hcl.atf.taf.mongodb.model.TestCaseExecutionResultMongo;

public interface TestCaseExecutionResultMongoDAO {

	void save(TestCaseExecutionResultMongo testCaseExecutionResultMongo);
	void save(List<TestCaseExecutionResultMongo> testCaseExecutionResultsMongo);
	
	void saveISE(ISETestExecutionCollectionMongo iseTestExecutionCollectionMongo);
	void saveISE(List<ISETestExecutionCollectionMongo> ISETestExecutionCollections);
	
}
