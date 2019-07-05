package com.hcl.atf.taf.mongodb.dao;

import com.hcl.atf.taf.model.RunConfiguration;

public interface TestBedsMongoDAO {
	void save(RunConfiguration runConfiguration);
}
