package com.hcl.atf.taf.mongodb.dao;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.hcl.atf.taf.model.json.JsonProductMaster;
import com.hcl.atf.taf.mongodb.constants.MongodbConstants;
import com.hcl.atf.taf.mongodb.model.ActivityEffortTrackerMongo;
import com.hcl.atf.taf.mongodb.model.ProductMasterMongo;
import com.hcl.atf.taf.mongodb.model.TestCaseExecutionResultMongo;
import com.hcl.atf.taf.mongodb.model.TestSuiteListMongo;

public interface  TestSuiteListMongoDAO {
		
	void save(TestSuiteListMongo testSuiteListMongo);
}
