package com.hcl.atf.taf.mongodb.dao;

import java.util.HashMap;

import com.hcl.atf.taf.mongodb.model.ISEDefectCollectionMongo;
import com.hcl.atf.taf.mongodb.model.ProductFeatureProductBuildMappingCollectionMongo;
import com.hcl.atf.taf.mongodb.model.TestCaseProductVersionMappingCollectionMongo;
import com.hcl.atf.taf.mongodb.model.TestCaseToProductFeatureMappingMongo;
import com.hcl.atf.taf.mongodb.model.TestFactoryReservedResourcesMongo;
import com.hcl.atf.taf.mongodb.model.WorkpackageDemandProjectionMongo;

public interface  MongoDbDAO {
		
	void save(WorkpackageDemandProjectionMongo workpackageDemandProjectionMongo);

	void save(TestFactoryReservedResourcesMongo testFactoryReservedResourcesMongo);

	void deleteResourceDemandFromMongoDb(Integer demandId);

	void deleteReseveredResourceFromMongoDb(Integer reservedId);

	void addOrUpdateCustomField(Integer entityInstanceId, String fieldName, Object fieldValue, String collectionName);

	void addOrUpdateCustomField(Integer entityInstanceId, HashMap<String, Object> customFields, String collectionName);
	
	void saveProductFeatureBuildMapping(ProductFeatureProductBuildMappingCollectionMongo productBuildMappingCollectionMongo);
	void saveTestCaseProductVersionMapping(TestCaseProductVersionMappingCollectionMongo testCaseProductVersionMappingCollectionMongo);
	void saveISEDefectCollection(ISEDefectCollectionMongo iseDefectCollectionMongo);
	void removeDefectCollection(Integer defectCollecionId);
	
	void saveFeatureTestCaseMapping(TestCaseToProductFeatureMappingMongo mappingMongo);
	void removeFeatureTestCaseMapping(Integer productFeatureId, Integer testCaseId);
}
