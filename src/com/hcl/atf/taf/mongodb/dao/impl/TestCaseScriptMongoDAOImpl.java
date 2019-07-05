package com.hcl.atf.taf.mongodb.dao.impl;

import java.text.SimpleDateFormat;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hcl.atf.taf.mongodb.constants.MongodbConstants;
import com.hcl.atf.taf.mongodb.dao.TestCaseScriptMongoDAO;
import com.hcl.atf.taf.mongodb.model.TestCaseScriptMappingMongo;
import com.hcl.atf.taf.mongodb.model.TestCaseScriptMongo;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

@Repository
public class TestCaseScriptMongoDAOImpl implements TestCaseScriptMongoDAO {

	private static final Log log = LogFactory.getLog(TestCaseScriptMongoDAOImpl.class);

	//private MongoOperations mongoOperation;	

	@Value("#{ilcmProps['PORT_FOR_ELASTIC_SEARCH']}")
    private String PORT_FOR_ELASTIC_SEARCH;
	
	
	public TestCaseScriptMongoDAOImpl(){
    }
    
   /* public TestCaseScriptMongoDAOImpl(MongoOperations mongoOperation){
        this.mongoOperation=mongoOperation;
    }
*/
	@Override
	@Transactional
	public void save(TestCaseScriptMongo testCaseScriptMongo) {
		log.debug("Saving TestCaseScriptMongo to Mongo DB");
		try {
			//this.mongoOperation.save(testCaseScriptMongo, MongodbConstants.TEST_CASE_SCRIPT_MONGO);
			ObjectMapper mapper = new ObjectMapper();
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
	        mapper.setDateFormat(formatter);
            String str = mapper.writeValueAsString(testCaseScriptMongo);
			String indexName = MongodbConstants.TEST_CASE_SCRIPT_MONGO;
			String docType = "Testscript";
			
			responseData(str,indexName,docType, testCaseScriptMongo.getId());
			log.info("Saved TestCaseScript to MongoDB");
		} catch (Exception e) {
			log.error("Unable to push TestCaseScriptMongo to MongoDB", e);
		}
	}

	@Override
	@Transactional
	public void saveTestCaseScriptMapping(TestCaseScriptMappingMongo testCaseScriptMappingMongo) {
		log.debug("Saving TestCaseScriptMappingMongo to Mongo DB");
		try {
			//this.mongoOperation.save(testCaseScriptMappingMongo, MongodbConstants.TEST_CASE_SCRIPT_MAPPING_MONGO);
			log.info("TestCaseScriptMapping is saved to MongoDB");
		} catch (Exception e) {
			log.error("Unable to push TestCaseScriptMappingMongo to MongoDB", e);
		}		
	}

	@Override
	@Transactional
	public void removeTestCaseScriptMapping(Integer testCaseScriptMappingMongoId) {
		try{
			log.info("Deleting the testCaseScriptMappingMongoId: "+testCaseScriptMappingMongoId);
		/*	DBCollection dBCollection = this.mongoOperation.getCollection(MongodbConstants.TEST_CASE_SCRIPT_MAPPING_MONGO);
			BasicDBObject query = new BasicDBObject();
			query.append("_id", testCaseScriptMappingMongoId);
			dBCollection.remove(query);*/
		}catch(Exception ex){
			log.error("Unable to delete testCaseScriptMappingMongoId  ", ex);
		}
	}

	@Override
	public void removeTestCaseScript(Integer testCaseScriptId) {
		try{
			log.info("Deleting the testCaseScriptMongoId: "+testCaseScriptId);
			/*DBCollection dBCollection = this.mongoOperation.getCollection(MongodbConstants.TEST_CASE_SCRIPT_MONGO);
			BasicDBObject query = new BasicDBObject();
			query.append("scriptId", testCaseScriptId);
			dBCollection.remove(query);*/
		}catch(Exception ex){
			log.error("Unable to delete testCaseScriptMappingMongoId  ", ex);
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
			log.info("Output from ES Server .... "+output);
			} catch(Exception e) {
			log.error("Error while formating "+docType+""+e);
		}
		return output;
	}
	
	
}
