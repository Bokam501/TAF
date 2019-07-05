package com.hcl.atf.taf.mongodb.dao.impl;

import java.text.SimpleDateFormat;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hcl.atf.taf.mongodb.constants.MongodbConstants;
import com.hcl.atf.taf.mongodb.dao.FeaturesMongoDAO;
import com.hcl.atf.taf.mongodb.model.FeatureMasterMongo;
import com.hcl.atf.taf.mongodb.model.ISEFeatureMappingMongo;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

@Repository
public class FeatureMongoDAOImpl implements FeaturesMongoDAO{
	
	private static final Log log = LogFactory.getLog(WorkPackageMongoDAOImpl.class);

	//private MongoOperations mongoOperation;
	
	@Value("#{ilcmProps['PORT_FOR_ELASTIC_SEARCH']}")
    private String PORT_FOR_ELASTIC_SEARCH;
	
    public FeatureMongoDAOImpl(){
    }
	    
    /*public FeatureMongoDAOImpl(MongoOperations mongoOperation){
        this.mongoOperation=mongoOperation;
    }*/
		
	@Override
	@Transactional
	public void save(FeatureMasterMongo featureMasterMongo) {
		
		try {
			//this.mongoOperation.save(featureMasterMongo, MongodbConstants.FEATURES_COLLECTION_NAME);
			
			ObjectMapper mapper = new ObjectMapper();
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
	        mapper.setDateFormat(formatter);
            String str = mapper.writeValueAsString(featureMasterMongo);
			log.info("str --- "+str);
			String indexName = MongodbConstants.FEATURES_COLLECTION_NAME;
			String docType = "Feature";
			
			responseData(str,indexName,docType, featureMasterMongo.getId());
			
			log.info("Saved Feature to MongoDB");
		} catch (Exception e) {
			log.error("Unable to push Feature to MongoDB", e);
		}
	}

	@Override
	@Transactional
	public void save(List<FeatureMasterMongo> featuresMongo) {
		
		try {
			//this.mongoOperation.save(featuresMongo, MongodbConstants.FEATURES_COLLECTION_NAME);
			log.info("Saved Feature to MongoDB");
		} catch (Exception e) {
			log.error("Unable to push Feature to MongoDB", e);
		}
	}

	@Override
	@Transactional
	public void saveISEFeatureMapping(ISEFeatureMappingMongo iseFeatureMappingMongo) {

		try {
			//this.mongoOperation.save(iseFeatureMappingMongo, MongodbConstants.ISE_FEATURES_COLLECTION_NAME);
			
			ObjectMapper mapper = new ObjectMapper();
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
	        mapper.setDateFormat(formatter);
            String str = mapper.writeValueAsString(iseFeatureMappingMongo);
			String indexName = MongodbConstants.ISE_FEATURES_COLLECTION_NAME;
			String docType = "Ise_Feature";
			
			responseData(str,indexName,docType, iseFeatureMappingMongo.getId());
			
			log.info("Saved ISEFeatureMapping to MongoDB");
		} catch (Exception e) {
			log.error("Unable to push ISEFeatureMappingMongo to MongoDB", e);
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
	
}
