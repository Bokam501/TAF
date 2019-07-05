package com.hcl.atf.taf.mongodb.dao;

import com.hcl.atf.taf.mongodb.model.TestCaseScriptMappingMongo;
import com.hcl.atf.taf.mongodb.model.TestCaseScriptMongo;

public interface TestCaseScriptMongoDAO {

	void save(TestCaseScriptMongo testCaseScriptMongo);
	void saveTestCaseScriptMapping(TestCaseScriptMappingMongo testCaseScriptMappingMongo);
	void removeTestCaseScriptMapping(Integer testCaseScriptMappingMongoId);
	void removeTestCaseScript(Integer testCaseScriptId);
}
