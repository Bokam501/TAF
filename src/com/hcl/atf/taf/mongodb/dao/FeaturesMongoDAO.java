package com.hcl.atf.taf.mongodb.dao;

import java.util.List;

import com.hcl.atf.taf.mongodb.model.FeatureMasterMongo;
import com.hcl.atf.taf.mongodb.model.ISEFeatureMappingMongo;

public interface FeaturesMongoDAO {

	void save(FeatureMasterMongo featureMasterMongo);
	void save(List<FeatureMasterMongo> featuresMongo);
	void saveISEFeatureMapping(ISEFeatureMappingMongo iseFeatureMappingMongo);
}
