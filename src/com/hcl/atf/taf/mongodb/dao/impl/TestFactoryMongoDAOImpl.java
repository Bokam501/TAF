package com.hcl.atf.taf.mongodb.dao.impl;

import java.text.SimpleDateFormat;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hcl.atf.taf.model.TestFactory;
import com.hcl.atf.taf.mongodb.constants.MongodbConstants;
import com.hcl.atf.taf.mongodb.dao.TestFactoryMongoDAO;
import com.hcl.atf.taf.mongodb.model.TestFactoryMongo;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
@Repository
public class TestFactoryMongoDAOImpl  implements TestFactoryMongoDAO{
	
	private static final Log log = LogFactory.getLog(TestFactoryMongoDAOImpl.class);
//	private MongoOperations mongoOperation;
	
	@Value("#{ilcmProps['PORT_FOR_ELASTIC_SEARCH']}")
    private String PORT_FOR_ELASTIC_SEARCH;
	
	public TestFactoryMongoDAOImpl(){
		
	}
	/*public TestFactoryMongoDAOImpl(MongoOperations mongoOperation){
		this.mongoOperation = mongoOperation;
	}*/

	@Override
	@Transactional
	public void save(TestFactory testFact) {
			
		 String keywords="";
		 TestFactoryMongo testFactoryMongo = new TestFactoryMongo(testFact.getTestFactoryId(),testFact.getTestFactoryName(), testFact.getDisplayName(), testFact.getCity(),
				 testFact.getState(), testFact.getCountry(), testFact.getStatus(), testFact.getCreatedDate(), testFact.getModifiedDate(), 
				 testFact.getDescription(), testFact.getTestFactoryLab().getTestFactoryLabId());			
				
		//this.mongoOperation.save(testFactoryMongo,MongodbConstants.TESTFACTORIES);
		 try {
		 ObjectMapper mapper = new ObjectMapper();
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
	        mapper.setDateFormat(formatter);
         String str = mapper.writeValueAsString(testFactoryMongo);
			String indexName = MongodbConstants.TESTFACTORIES;
			String docType = "testfactories";
			
			responseData(str,indexName,docType, testFactoryMongo.getId());
		 }catch(Exception e) {
			 
		 }
			
	}
	@Override
	@Transactional
	public void save(TestFactoryMongo testFactoryMongo) {
		

		try {
			//this.mongoOperation.save(testFactoryMongo, MongodbConstants.TESTFACTORIES);
			 
			 ObjectMapper mapper = new ObjectMapper();
				SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
		        mapper.setDateFormat(formatter);
	         String str = mapper.writeValueAsString(testFactoryMongo);
				String indexName = MongodbConstants.TESTFACTORIES;
				String docType = "Testfactories";
				
				responseData(str,indexName,docType, testFactoryMongo.getId());
			log.info("Saved TestFactory to MongoDB TC Id "+ testFactoryMongo.getTestFactoryId());
		} catch (Exception e) {
			log.error("Unable to push TestFactory to MongoDB", e);
		}
	
		
	}
	@Override
	@Transactional
	public void save(List<TestFactoryMongo> testFactoriesMongo) {

		log.debug("Saving TestFactory to MOngo DB");
		try {
			//this.mongoOperation.save(testFactoriesMongo, MongodbConstants.TESTFACTORIES);
			log.info("Saved TestFactory to MongoDB");
		} catch (Exception e) {
			log.error("Unable to push TestFactory to MongoDB", e);
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
