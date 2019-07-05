package com.hcl.atf.taf.mongodb.dao;

import java.util.List;

import com.hcl.atf.taf.model.TestCaseList;
import com.hcl.atf.taf.model.UserList;
import com.hcl.atf.taf.mongodb.model.ISETestCaseCollectionMongo;
import com.hcl.atf.taf.mongodb.model.TestCaseMasterMongo;

public interface  TestCasesMongoDAO {

	void save(TestCaseList testCaseListFromUI, String productName, UserList user);

	void save(TestCaseMasterMongo testCaseMasterMongo);

	void save(List<TestCaseMasterMongo> testCasesMasterMongo);
	
	void saveISE(ISETestCaseCollectionMongo iseTestCaseMasterMongo);

	void saveISE(List<ISETestCaseCollectionMongo> iseTestCasesMasterMongoList);

}
