package com.hcl.atf.taf.mongodb.dao;

import java.util.List;

import com.hcl.atf.taf.model.TestFactory;
import com.hcl.atf.taf.mongodb.model.TestFactoryMongo;

public interface TestFactoryMongoDAO {
	void save(TestFactory testdata);

	void save(TestFactoryMongo testFactoryMongo);

	void save(List<TestFactoryMongo> testFactoriesMongo);
}
