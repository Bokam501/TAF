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
import com.hcl.atf.taf.mongodb.dao.TestRunJobMongoDAO;
import com.hcl.atf.taf.mongodb.model.TestRunJobMongo;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

@Repository
public class TestRunJobMongoDAOImpl implements TestRunJobMongoDAO {

//	private MongoOperations mongoOperation;
	private static final Log log = LogFactory
			.getLog(TestRunJobMongoDAOImpl.class);
	@Value("#{ilcmProps['PORT_FOR_ELASTIC_SEARCH']}")
    private String PORT_FOR_ELASTIC_SEARCH;
	public TestRunJobMongoDAOImpl() {

	}

	/*public TestRunJobMongoDAOImpl(MongoOperations mongoOperation) {
		this.mongoOperation = mongoOperation;
	}*/

	@Override
	@Transactional
	public void save(TestRunJobMongo testRunJobMongo) {

		log.debug("Saving TestRunJob to MOngo DB");
		try {
			//this.mongoOperation.save(testRunJobMongo,MongodbConstants.TEST_RUN_JOB);
			
			ObjectMapper mapper = new ObjectMapper();
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
	        mapper.setDateFormat(formatter);
            String str = mapper.writeValueAsString(testRunJobMongo);
			String indexName = MongodbConstants.TEST_RUN_JOB;
			String docType = "testRunJob";
			responseData(str,indexName,docType, testRunJobMongo.getId());
			
			log.debug("Saved TestRunJob to MongoDB ID "+testRunJobMongo.getTestRunJobId());
		} catch (Exception e) {
			log.error("Unable to push TestRunJob to MongoDB", e);
		}
	}

	@Override
	@Transactional
	public void save(List<TestRunJobMongo> testRunJobsMongo) {

		log.debug("Saving TestRunJobMongo to MOngo DB");
		try {
			//this.mongoOperation.save(testRunJobsMongo,
				//	MongodbConstants.TEST_RUN_JOB);
			log.info("Saved TestRunJobMongo to MongoDB");
		} catch (Exception e) {
			log.error("Unable to push TestRunJobMongo to MongoDB", e);
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
