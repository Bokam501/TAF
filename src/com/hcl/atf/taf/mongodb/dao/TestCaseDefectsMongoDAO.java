package com.hcl.atf.taf.mongodb.dao;

import java.util.List;

import com.hcl.atf.taf.mongodb.model.TestCaseDefectsMasterMongo;

public interface  TestCaseDefectsMongoDAO {

	void save(TestCaseDefectsMasterMongo defectMogo);
	void save(List<TestCaseDefectsMasterMongo> defectsMongo);
}
