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
import com.hcl.atf.taf.mongodb.dao.TestCaseStepsListMongoDAO;
import com.hcl.atf.taf.mongodb.model.TestCaseStepsListMongo;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

@Repository
public class TestCaseStepsListMongoDAOImpl implements TestCaseStepsListMongoDAO {
	private static final Log log = LogFactory
			.getLog(TestCaseStepsListMongoDAOImpl.class);

	// private MongoOperations mongoOperation;

	@Value("#{ilcmProps['PORT_FOR_ELASTIC_SEARCH']}")
    private String PORT_FOR_ELASTIC_SEARCH;
	public TestCaseStepsListMongoDAOImpl() {
	}

	/*
	 * public TestCaseStepsListMongoDAOImpl(MongoOperations mongoOperation){
	 * this.mongoOperation=mongoOperation; }
	 */
	@Override
	@Transactional
	public void save(TestCaseStepsListMongo testCaseStepsListMongo) {
		try {
			// this.mongoOperation.save(testCaseStepsListMongo,
			// MongodbConstants.TEST_STEPS);

			ObjectMapper mapper = new ObjectMapper();
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
	        mapper.setDateFormat(formatter);
            String str = mapper.writeValueAsString(testCaseStepsListMongo);
			String indexName = MongodbConstants.TEST_STEPS;
			String docType = "TestSteps";
			
			responseData(str,indexName,docType, testCaseStepsListMongo.getId());
			
			log.info("Saved testCaseSteps to MongoDB");
		} catch (Exception e) {
			log.error("Unable to push testCaseSteps to MongoDB", e);
		}
	}

	@Override
	@Transactional
	public void save(List<TestCaseStepsListMongo> testCaseStepsListMongo) {

		log.debug("Saving TestCaseStepsListMongo to MOngo DB");
		try {
			//this.mongoOperation.save(testCaseStepsListMongo,MongodbConstants.TEST_STEPS);
			
			
			// mongoOperation.insert(testCaseStepsListMongo,
			// MongodbConstants.TEST_STEPS);
			log.info("Saved TestCaseStepsListMongo to MongoDB");
		} catch (Exception e) {
			log.error("Unable to push TestCaseStepsListMongo to MongoDB", e);
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
