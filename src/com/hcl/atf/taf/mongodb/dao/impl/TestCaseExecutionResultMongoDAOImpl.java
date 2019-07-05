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
import com.hcl.atf.taf.mongodb.dao.TestCaseExecutionResultMongoDAO;
import com.hcl.atf.taf.mongodb.model.ISETestExecutionCollectionMongo;
import com.hcl.atf.taf.mongodb.model.TestCaseExecutionResultMongo;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

@Repository
public class TestCaseExecutionResultMongoDAOImpl implements TestCaseExecutionResultMongoDAO {

	private static final Log log = LogFactory.getLog(TestCaseExecutionResultMongoDAOImpl.class);

	//private MongoOperations mongoOperation;	

	@Value("#{ilcmProps['PORT_FOR_ELASTIC_SEARCH']}")
    private String PORT_FOR_ELASTIC_SEARCH;
	
	public TestCaseExecutionResultMongoDAOImpl(){
	}
	    
	/*public TestCaseExecutionResultMongoDAOImpl(MongoOperations mongoOperation){
	       this.mongoOperation=mongoOperation;
	}*/
	
	/*
	 * This method will insert this record into MongoDB for the first time and later on, update it based on the _id field value
	 * @see com.hcl.atf.taf.mongodb.dao.TestCaseExecutionResultMongoDAO#save(com.hcl.atf.taf.mongodb.model.TestCaseExecutionResultMongo)
	 */
	@Override
	@Transactional
	public void save(TestCaseExecutionResultMongo testCaseExecutionResultMongo) {

		log.debug("Saving TestcaseResult to MOngo DB");
		try {
			//this.mongoOperation.save(testCaseExecutionResultMongo, MongodbConstants.TESTCASEEXECUTION);
			ObjectMapper mapper = new ObjectMapper();
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
	        mapper.setDateFormat(formatter);
            String str = mapper.writeValueAsString(testCaseExecutionResultMongo);
			String indexName = MongodbConstants.TESTCASEEXECUTION;
			String docType = "TestcaseExecution";
			
			responseData(str,indexName,docType, testCaseExecutionResultMongo.getId());
			log.info("Saved TestCaseResult to MongoDB");
		} catch (Exception e) {
			log.error("Unable to push TestCaseResult to MongoDB", e);
		}
	}

	@Override
	@Transactional
	public void save(List<TestCaseExecutionResultMongo> testCaseExecutionResultsMongo) {

		log.debug("Saving TestcaseResult to MOngo DB");
		try {
		//	this.mongoOperation.save(testCaseExecutionResultsMongo, MongodbConstants.TESTCASEEXECUTION);
			log.info("Saved TestCaseResult to MongoDB");
		} catch (Exception e) {
			log.error("Unable to push TestCaseResult to MongoDB", e);
		}
	}

	@Override
	public void saveISE(
			ISETestExecutionCollectionMongo iseTestExecutionCollectionMongo) {
		log.debug("Saving ISE TestcaseResult to MOngo DB");
		try {
			//this.mongoOperation.save(iseTestExecutionCollectionMongo, MongodbConstants.ISETESTCASEEXECUTION);
			ObjectMapper mapper = new ObjectMapper();
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
	        mapper.setDateFormat(formatter);
            String str = mapper.writeValueAsString(iseTestExecutionCollectionMongo);
			String indexName = MongodbConstants.ISETESTCASEEXECUTION;
			String docType = "TestcaseExecution";
			
			responseData(str,indexName,docType, iseTestExecutionCollectionMongo.getId());
			log.info("Saved ISE TestCaseResult to MongoDB");
		} catch (Exception e) {
			log.error("Unable to push ISE TestCaseResult to MongoDB", e);
		}
		
	}

	@Override
	public void saveISE(
			List<ISETestExecutionCollectionMongo> ISETestExecutionCollections) {
		log.debug("Saving ISE TestcaseResult to MOngo DB");
		try {
		//	this.mongoOperation.save(ISETestExecutionCollections, MongodbConstants.ISETESTCASEEXECUTION);
			log.info("Saved ISE TestCaseResult to MongoDB");
		} catch (Exception e) {
			log.error("Unable to push ISE TestCaseResult to MongoDB", e);
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


