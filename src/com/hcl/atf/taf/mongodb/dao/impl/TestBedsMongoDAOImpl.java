package com.hcl.atf.taf.mongodb.dao.impl;

import java.text.SimpleDateFormat;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hcl.atf.taf.controller.utilities.DateUtility;
import com.hcl.atf.taf.model.RunConfiguration;
import com.hcl.atf.taf.mongodb.constants.MongodbConstants;
import com.hcl.atf.taf.mongodb.dao.TestBedsMongoDAO;
import com.hcl.atf.taf.mongodb.model.TestBedsMongo;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

@Repository
public class TestBedsMongoDAOImpl implements TestBedsMongoDAO {
	//DB db = null;		
	//DBCollection collection = null;
	//DBObject document=null;
	//private MongoOperations mongoOperation;	
    //private static final String TESTSTEP_COLLECTION="";
	private static final Log log = LogFactory.getLog(TestBedsMongoDAOImpl.class);
	
	@Value("#{ilcmProps['PORT_FOR_ELASTIC_SEARCH']}")
    private String PORT_FOR_ELASTIC_SEARCH;
	
    public TestBedsMongoDAOImpl(){
    }
    
    /*public TestBedsMongoDAOImpl(MongoOperations mongoOperation){
        this.mongoOperation=mongoOperation;
    }*/
    
	@Override
	public void save(RunConfiguration runConfiguration) {
		// TODO Auto-generated method stub
		/*public TestBedsMongo(String env_name, String description, String distribution, String env_type, String created_date, 
				Integer genericDeviceId, Integer productId, Integer productVersionId, Integer runconfigNameId, 
				Integer workPackageId, String testRunPlanSet, String hostListId){*/
			
		try {
			int wpId = runConfiguration.getWorkPackage().getWorkPackageId();
			int verId = runConfiguration.getWorkPackage().getProductBuild().getProductVersion().getProductVersionListId();
			int runconfigId = runConfiguration.getRunconfigId();
			int deviceId = 0;
			if(runConfiguration.getGenericDevice() != null){
				 deviceId = runConfiguration.getGenericDevice().getGenericsDevicesId();	
			}
			String envtype = "";
			if(runConfiguration.getEnvironmentcombination() != null){
				 envtype =runConfiguration.getEnvironmentcombination().getEnvironmentCombinationName();	
			}
			
			TestBedsMongo testBedsMongo = new TestBedsMongo(runConfiguration.getEnvironmentcombination().getEnvironmentCombinationName(), 
					"", "", envtype, ""+DateUtility.getCurrentTime(), deviceId, runConfiguration.getProduct().getProductId(), verId, runconfigId, wpId, "", "");
			// save
			//this.mongoOperation.save(testBedsMongo, MongodbConstants.TESTBEDS);
			ObjectMapper mapper = new ObjectMapper();
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
	        mapper.setDateFormat(formatter);
            String str = mapper.writeValueAsString(testBedsMongo);
			String indexName = MongodbConstants.TESTBEDS;
			String docType = "Environment";
			
			responseData(str,indexName,docType, testBedsMongo.getEnv_name());
			
			
		} catch (Exception e) {
			log.error("ERROR  ",e);
		}
		
		/*db =  ConnectionPool.getInstance().getMongoDB();
		collection=db.getCollection(runConfiguration.getProduct().getProductName()+"."+MongodbConstants.TESTBEDS);
		
	
		document = new BasicDBObject();
		//document.put(MongodbConstants.TB_ID,runConfiguration.getRunconfigId());
		document.put(MongodbConstants.TB_ENVIRONMENT_NAME,runConfiguration.getEnvironmentcombination().getEnvironmentCombinationName());
		document.put(MongodbConstants.TB_DESCRIPTION,"");
		document.put(MongodbConstants.TB_DISTRIBUTION,"");		
		document.put(MongodbConstants.TB_ENVIRONMENT_TYPE,runConfiguration.getEnvironmentcombination().getEnvironmentCombinationName());
		document.put(MongodbConstants.TB_CREATEDDATE,"");
		
		collection.insert(document);*/
		
	
	}
	
	private String responseData(String str,String indexName,String docType, String _id)	{
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


