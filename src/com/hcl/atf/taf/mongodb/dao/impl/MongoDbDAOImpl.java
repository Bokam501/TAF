package com.hcl.atf.taf.mongodb.dao.impl;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hcl.atf.taf.mongodb.constants.MongodbConstants;
import com.hcl.atf.taf.mongodb.dao.MongoDbDAO;
import com.hcl.atf.taf.mongodb.model.ISEDefectCollectionMongo;
import com.hcl.atf.taf.mongodb.model.ProductFeatureProductBuildMappingCollectionMongo;
import com.hcl.atf.taf.mongodb.model.TestCaseProductVersionMappingCollectionMongo;
import com.hcl.atf.taf.mongodb.model.TestCaseToProductFeatureMappingMongo;
import com.hcl.atf.taf.mongodb.model.TestFactoryReservedResourcesMongo;
import com.hcl.atf.taf.mongodb.model.WorkpackageDemandProjectionMongo;
import com.mongodb.BasicDBObject;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

@Repository
public class MongoDbDAOImpl implements MongoDbDAO {
	
	@Value("#{ilcmProps['PORT_FOR_ELASTIC_SEARCH']}")
    private String PORT_FOR_ELASTIC_SEARCH;
	
	//private MongoOperations mongoOperation;
	private static final Log log = LogFactory.getLog(MongoDbDAOImpl.class);

    public MongoDbDAOImpl(){
    }
    
    /*public MongoDbDAOImpl(MongoOperations mongoOperation){
        this.mongoOperation=mongoOperation;
    }*/
     
	
	@Override
	@Transactional
	public void save(WorkpackageDemandProjectionMongo workpackageDemandProjectionMongo) {

		log.debug("Saving WorkpackageDemandProjection to Elastic Search");
		try {
		//	this.mongoOperation.save(workpackageDemandProjectionMongo, MongodbConstants.WORKPACKAGAE_DEMAND);
			ObjectMapper mapper = new ObjectMapper();
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
	        mapper.setDateFormat(formatter);
            String str = mapper.writeValueAsString(workpackageDemandProjectionMongo);
			String indexName = MongodbConstants.WORKPACKAGAE_DEMAND;
			String docType = "workpackageDemandProjectionMongo";
			
			responseData(str,indexName,docType, workpackageDemandProjectionMongo.getId());
			
		 
			log.info("Saved WorkpackageDemandProjection to Elastic Search");
		} catch (Exception e) {
			log.error("Unable to push WorkpackageDemandProjection to Elastic Search", e);
		}
	}

	@Override
	@Transactional
	public void save(TestFactoryReservedResourcesMongo testFactoryReservedResourcesMongo) {

		log.debug("Saving testFactoryReservedResourcesMongo to MOngo DB");
		try {
			//this.mongoOperation.save(testFactoryReservedResourcesMongo, MongodbConstants.WORKPACKAGAE_RESOURCE_RESERVATION);
			
			ObjectMapper mapper = new ObjectMapper();
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
	        mapper.setDateFormat(formatter);
            String str = mapper.writeValueAsString(testFactoryReservedResourcesMongo);
			String indexName = MongodbConstants.WORKPACKAGAE_RESOURCE_RESERVATION;
			String docType = "workpackageResourceReservation";
			
			responseData(str,indexName,docType, testFactoryReservedResourcesMongo.getId());
			log.info("Saved testFactoryReservedResourcesMongo to MongoDB");
		} catch (Exception e) {
			log.error("Unable to push testFactoryReservedResourcesMongo to MongoDB", e);
		}
	}

	@Override
	@Transactional
	public void deleteResourceDemandFromMongoDb(Integer demandId) {
		try{
			log.info("Deleting Demand  from MongoDb Id"+ demandId);
			
		/*	DBCollection dbCollection = this.mongoOperation.getCollection(MongodbConstants.WORKPACKAGAE_DEMAND);
			BasicDBObject query = new BasicDBObject();
			query.append("wpDemandProjectionId", demandId);
			dbCollection.remove(query);
		*/	
		}catch(Exception ex){
			log.error("Unable to delete Demand from Mongodb ", ex);
		}
	}

	@Override
	@Transactional
	public void deleteReseveredResourceFromMongoDb(Integer reservedId) {
	try{
		/*DBCollection dbCollection = this.mongoOperation.getCollection(MongodbConstants.WORKPACKAGAE_RESOURCE_RESERVATION);
		BasicDBObject query = new BasicDBObject();
		query.append("resourceReservationId", reservedId);
		dbCollection.remove(query);*/
		
	}catch(Exception ex){
		log.error("Unable to delete ReseveredResource from Mongodb ", ex);
	}
	}

	//TODO Need to update custom field values
	@Override
	@Transactional
	public void addOrUpdateCustomField(Integer entityInstanceId, String fieldName, Object fieldValue, String collectionName) {
		try{
		//	DBCollection dbCollection = this.mongoOperation.getCollection(collectionName);
			BasicDBObject updateFieldValue = new BasicDBObject();
			updateFieldValue.append("$set", new BasicDBObject().append(fieldName, fieldValue));

			BasicDBObject availableDocument = new BasicDBObject().append("_id", entityInstanceId+"");
			//dbCollection.update(availableDocument, updateFieldValue, true, true);
			
			ObjectMapper mapper = new ObjectMapper();
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
	        mapper.setDateFormat(formatter);
            String str = mapper.writeValueAsString(availableDocument);
			String indexName = collectionName;
			String docType = collectionName;
			
			responseData(str,indexName,docType,entityInstanceId+"");
			
		}catch(Exception ex){
			log.error("Error in addOrUpdateCustomField - ", ex);
		}
	}

	@Override
	@Transactional
	public void addOrUpdateCustomField(Integer entityInstanceId, HashMap<String, Object> customFields, String collectionName) {
		log.debug("Custom Fileds --> "+customFields);
		try{
			//DBCollection dbCollection = this.mongoOperation.getCollection(collectionName);
			BasicDBObject updateFieldValue = new BasicDBObject();
			BasicDBObject customFieldAndValues = new BasicDBObject();
			if(customFields != null && customFields.size() > 0){
				for(Map.Entry<String, Object> customField : customFields.entrySet()){
					customFieldAndValues.append(customField.getKey(), customField.getValue());
				}
			}
			updateFieldValue.append("$set", customFieldAndValues);

			BasicDBObject availableDocument = new BasicDBObject().append("_id", entityInstanceId+"");
			//dbCollection.update(availableDocument, updateFieldValue, true, true);*/
			
			ObjectMapper mapper = new ObjectMapper();
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
	        mapper.setDateFormat(formatter);
            String str = mapper.writeValueAsString(availableDocument);
			String indexName = collectionName;
			String docType = collectionName;
			
			responseData(str,indexName,docType,entityInstanceId+"");
			
		}catch(Exception ex){
			log.error("Error in addOrUpdateCustomField - ", ex);
		}
	}

	
	
	@Override
	@Transactional
	public void saveProductFeatureBuildMapping(ProductFeatureProductBuildMappingCollectionMongo productBuildMappingCollectionMongo) {

		log.debug("Saving saveProductFeatureBuildMapping to MOngo DB");
		try {
			//this.mongoOperation.save(productBuildMappingCollectionMongo, MongodbConstants.PRODUCT_FEATURE_BUILD_MAPPING_MONGO);
			
			ObjectMapper mapper = new ObjectMapper();
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
	        mapper.setDateFormat(formatter);
            String str = mapper.writeValueAsString(productBuildMappingCollectionMongo);
			String indexName = MongodbConstants.PRODUCT_FEATURE_BUILD_MAPPING_MONGO;
			String docType = "productBuildMapping";
			
			responseData(str,indexName,docType, productBuildMappingCollectionMongo.getId());
			
			log.info("Saved saveProductFeatureBuildMapping to MongoDB");
		} catch (Exception e) {
			log.error("Unable to push saveProductFeatureBuildMapping to MongoDB", e);
		}
	}
	
	
	@Override
	@Transactional
	public void saveTestCaseProductVersionMapping(TestCaseProductVersionMappingCollectionMongo testCaseProductVersionMappingCollectionMongo) {

		log.debug("Saving saveTestCaseProductVersionMapping to MOngo DB");
		try {
			//this.mongoOperation.save(testCaseProductVersionMappingCollectionMongo, MongodbConstants.TESTCASE_PRODUCT_VERSION_MAPPING);
			
			ObjectMapper mapper = new ObjectMapper();
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
	        mapper.setDateFormat(formatter);
            String str = mapper.writeValueAsString(testCaseProductVersionMappingCollectionMongo);
			String indexName = MongodbConstants.TESTCASE_PRODUCT_VERSION_MAPPING;
			String docType = "testCaseProductVersionMapping";
			
			responseData(str,indexName,docType, testCaseProductVersionMappingCollectionMongo.getId());
			
			log.info("Saved saveTestCaseProductVersionMapping to MongoDB");
		} catch (Exception e) {
			log.error("Unable to push saveTestCaseProductVersionMapping to MongoDB", e);
		}
	}

	@Override
	@Transactional
	public void saveISEDefectCollection(ISEDefectCollectionMongo iseDefectCollectionMongo) {
		
		log.debug("Saving DefectCollectionMongo to Mongo DB");
		try {
		//	this.mongoOperation.save(iseDefectCollectionMongo, MongodbConstants.ISE_DEFECTS);
			
			ObjectMapper mapper = new ObjectMapper();
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
	        mapper.setDateFormat(formatter);
            String str = mapper.writeValueAsString(iseDefectCollectionMongo);
			String indexName = MongodbConstants.ISE_DEFECTS;
			String docType = "ISE_Defect";
			
			responseData(str,indexName,docType, iseDefectCollectionMongo.getId().toString());
			
			log.info("Saved DefectCollection to MongoDB");
		} catch (Exception e) {
			log.error("Unable to push DefectCollectionMongo to MongoDB", e);
		}
	}
	
	
	@Override
	@Transactional
	public void removeDefectCollection(Integer defectCollecionId) {
		try {
			/*log.debug("Deleting DefectCollectionMongo from Mongo DB");
			DBCollection dbCollection = this.mongoOperation.getCollection(MongodbConstants.ISE_DEFECTS);
			BasicDBObject query = new BasicDBObject();
			query.append("_id", defectCollecionId);
			dbCollection.remove(query);*/
			
			String indexName = MongodbConstants.ISE_DEFECTS;
			String docType = "ISE_Defect";
			 removeDocument(indexName,docType,defectCollecionId.toString());
			
		} catch (Exception e) {
			log.error("Unable to remove DefectCollection", e);
		}
	}
	
	
	@Override
	@Transactional
	public void saveFeatureTestCaseMapping(TestCaseToProductFeatureMappingMongo productFeatureMapping) {
		try {
			//this.mongoOperation.save(mappingMongo, MongodbConstants.TESTCASE_TO_FEATURES_MAPPING_COLLECTION_NAME);
			ObjectMapper mapper = new ObjectMapper();
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
	        mapper.setDateFormat(formatter);
            String str = mapper.writeValueAsString(productFeatureMapping);
			String indexName = MongodbConstants.TESTCASE_TO_FEATURES_MAPPING_COLLECTION_NAME;
			String docType = "ProductFeatureTestcaseMapping";
			
			responseData(str,indexName,docType, productFeatureMapping.getId().toString());
			
			log.info("saveFeatureTestCaseMapping successful");
		} catch (Exception e) {
			log.error("saveFeatureTestCaseMapping failed");
		}
	}

	@Override
	@Transactional
	public void removeFeatureTestCaseMapping(Integer productFeatureId,Integer testCaseId) {
		try {
			/*log.debug("Deleting FeatureTestCaseMappingMongo from Mongo DB");
			DBCollection dbCollection = this.mongoOperation.getCollection(MongodbConstants.TESTCASE_TO_FEATURES_MAPPING_COLLECTION_NAME);
			BasicDBObject query = new BasicDBObject();
			query.append("productFeatureId", productFeatureId);
			query.append("testCaseId", testCaseId);
			dbCollection.remove(query);*/
		} catch (Exception e) {
			log.error("Unable to remove FeatureTestCaseMappingMongo", e);
		}
		
	}
	
	private String responseData(String str,String indexName,String docType, String _id)
	{
		String output="";
		try {
		Client client = Client.create();
		ClientResponse response = null;
		String esUrl = PORT_FOR_ELASTIC_SEARCH ;
	    String url = esUrl + "/" + indexName + "/" + docType+ "/" + _id;
		WebResource webResource = client.resource(url.toString());
	    response = webResource.accept("application/json").post(ClientResponse.class, str);
		output = response.getEntity(String.class);	
		} catch(Exception e) {
			log.error("Error while formating "+docType+""+e);
		}
		return output;
	}
	
	private String removeDocument(String indexName,String docType, String _id)
	{
		String output="";
		try {
		Client client = Client.create();
		ClientResponse response = null;
		String esUrl = PORT_FOR_ELASTIC_SEARCH ;
	    String url = esUrl + "/" + indexName + "/" + docType+ "/" + _id;
		WebResource webResource = client.resource(url.toString());
	    response = webResource.accept("application/json").delete(ClientResponse.class);
		output = response.getEntity(String.class);	
		} catch(Exception e) {
			log.error("Error while formating "+docType+""+e);
		}
		return output;
	}
	
	
}


