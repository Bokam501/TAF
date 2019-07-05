package com.hcl.atf.taf.mongodb.dao.impl;

import java.text.SimpleDateFormat;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hcl.atf.taf.mongodb.constants.MongodbConstants;
import com.hcl.atf.taf.mongodb.dao.TestSuiteListMongoDAO;
import com.hcl.atf.taf.mongodb.model.TestSuiteListMongo;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

@Repository
public class TestSuiteListMongoDAOImpl implements TestSuiteListMongoDAO {

	// private MongoOperations mongoOperation;
	private static final Log log = LogFactory
			.getLog(TestSuiteListMongoDAOImpl.class);
	@Value("#{ilcmProps['PORT_FOR_ELASTIC_SEARCH']}")
    private String PORT_FOR_ELASTIC_SEARCH;
	
	public TestSuiteListMongoDAOImpl() {
	}

	/*
	 * public TestSuiteListMongoDAOImpl(MongoOperations mongoOperation){
	 * this.mongoOperation=mongoOperation; }
	 */

	@Override
	@Transactional
	public void save(TestSuiteListMongo testSuiteListMongo) {

		log.debug("Saving Product to MOngo DB");
		try {
			// this.mongoOperation.save(testSuiteListMongo,
			// MongodbConstants.TEST_SUITE);
			ObjectMapper mapper = new ObjectMapper();
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
	        mapper.setDateFormat(formatter);
            String str = mapper.writeValueAsString(testSuiteListMongo);
			String indexName = MongodbConstants.TEST_SUITE;
			String docType = "testsuite";
			
			responseData(str,indexName,docType, testSuiteListMongo.getId());
			
			log.info("Saved TestSuiteList to MongoDB");
		} catch (Exception e) {
			log.error("Unable to push TestSuiteList to MongoDB", e);
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
