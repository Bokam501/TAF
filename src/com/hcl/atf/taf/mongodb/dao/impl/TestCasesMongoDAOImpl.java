package com.hcl.atf.taf.mongodb.dao.impl;

import java.text.SimpleDateFormat;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hcl.atf.taf.model.TestCaseList;
import com.hcl.atf.taf.model.UserList;
import com.hcl.atf.taf.mongodb.constants.MongodbConstants;
import com.hcl.atf.taf.mongodb.dao.TestCasesMongoDAO;
import com.hcl.atf.taf.mongodb.model.ISETestCaseCollectionMongo;
import com.hcl.atf.taf.mongodb.model.TestCaseMasterMongo;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

@Repository
public class TestCasesMongoDAOImpl implements TestCasesMongoDAO {

	private static final Log log = LogFactory
			.getLog(TestCasesMongoDAOImpl.class);

	// private MongoOperations mongoOperation;

	@Value("#{ilcmProps['PORT_FOR_ELASTIC_SEARCH']}")
	private String PORT_FOR_ELASTIC_SEARCH;

	public TestCasesMongoDAOImpl() {
	}

	/*
	 * public TestCasesMongoDAOImpl(MongoOperations mongoOperation){
	 * this.mongoOperation=mongoOperation; }
	 */

	@Override
	@Transactional
	public void save(TestCaseMasterMongo testCaseMasterMongo) {
		try {
			// this.mongoOperation.save(testCaseMasterMongo,
			// MongodbConstants.TESTCASES);
			ObjectMapper mapper = new ObjectMapper();
			SimpleDateFormat formatter = new SimpleDateFormat(
					"yyyy-MM-dd'T'HH:mm:ss");
			mapper.setDateFormat(formatter);
			String str = mapper.writeValueAsString(testCaseMasterMongo);
			String indexName = MongodbConstants.TESTCASES;
			String docType = "Testcase";

			responseData(str, indexName, docType, testCaseMasterMongo.getId());

			log.info("Saved TestCase to MongoDB TC Id "
					+ testCaseMasterMongo.getTestcaseid());
		} catch (Exception e) {
			log.error("Unable to push TestCase to MongoDB", e);
		}
	}

	@Override
	public void save(TestCaseList testCaseListFromUI, String productName,
			UserList user) {

	}

	@Override
	@Transactional
	public void save(List<TestCaseMasterMongo> testCasesMasterMongo) {

		log.debug("Saving TestCase to MOngo DB");
		try {
			// this.mongoOperation.save(testCasesMasterMongo,
			// MongodbConstants.TESTCASES);
			
			
			ObjectMapper mapper = new ObjectMapper();
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
			mapper.setDateFormat(formatter);
			String str = mapper.writeValueAsString(testCasesMasterMongo);
			bulkPush(str);
			log.info("Saved TestCase to MongoDB");
		} catch (Exception e) {
			log.error("Unable to push TestCase to MongoDB", e);
		}
	}

	@Override
	@Transactional
	public void saveISE(ISETestCaseCollectionMongo iseTestCaseMasterMongo) {
		try {
			// this.mongoOperation.save(iseTestCaseMasterMongo,
			// MongodbConstants.ISETESTCASES);
			ObjectMapper mapper = new ObjectMapper();
			SimpleDateFormat formatter = new SimpleDateFormat(
					"yyyy-MM-dd'T'HH:mm:ss");
			mapper.setDateFormat(formatter);
			String str = mapper.writeValueAsString(iseTestCaseMasterMongo);
			String indexName = MongodbConstants.ISETESTCASES;
			String docType = "ISE_Testcase";

			responseData(str, indexName, docType,
					iseTestCaseMasterMongo.getId());
			log.info("Saved ISE TestCase to MongoDB");
		} catch (Exception e) {
			log.error("Unable to push ISE TestCase to MongoDB", e);
		}
	}

	@Override
	@Transactional
	public void saveISE(List<ISETestCaseCollectionMongo> iseTestCasesMasterMongoList) {

		log.debug("Saving ISE TestCase to MOngo DB");
		try {
			// this.mongoOperation.save(iseTestCasesMasterMongoList,
			// MongodbConstants.ISETESTCASES);
			ObjectMapper mapper = new ObjectMapper();
			SimpleDateFormat formatter = new SimpleDateFormat(
					"yyyy-MM-dd'T'HH:mm:ss");
			mapper.setDateFormat(formatter);
			String str = mapper.writeValueAsString(iseTestCasesMasterMongoList);
			bulkPush(str);
		} catch (Exception e) {
			log.error("Unable to push ISE TestCase to MongoDB", e);
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
	
	private String bulkPush(String str)
	{
		String output="";
		try {
		Client client = Client.create();
		ClientResponse response = null;
		String esUrl = PORT_FOR_ELASTIC_SEARCH ;
		String indexName="testcases";
	    //String url = esUrl + "/" + indexName;
		String url = esUrl + "/" + indexName + "/" + "Testcase";
		WebResource webResource = client.resource(url.toString());
	    response = webResource.accept("application/json").post(ClientResponse.class, str);
		log.info("Bulk testcase push"+response);
		output = response.getEntity(String.class);	
		log.info("Output from ES Server .... "+output);
		}catch(Exception e) {
			log.error("Bulk push failed",e);
		}
		return output;
		
	}

}
