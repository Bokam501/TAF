package com.hcl.atf.taf.mongodb.dao;

import com.hcl.atf.taf.mongodb.model.ChangeRequestMappingMongo;
import com.hcl.atf.taf.mongodb.model.ChangeRequestMongo;

public interface ChangeRequestMongoDAO {

	void save(ChangeRequestMongo changeRequestMongo);
	void saveChangeRequestMapping(ChangeRequestMappingMongo mappingMongo);
	void removeChangeRequestMapping(Integer entityRelationshipId);
}
