package com.hcl.atf.taf.mongodb.dao.impl;

import java.text.SimpleDateFormat;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hcl.atf.taf.mongodb.constants.MongodbConstants;
import com.hcl.atf.taf.mongodb.dao.ActivityMongoDAO;
import com.hcl.atf.taf.mongodb.model.ActivityMongo;
import com.mongodb.BasicDBObject;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

@Repository
public class ActivityMongoDAOImpl implements ActivityMongoDAO {
	
	private static final Log log = LogFactory.getLog(ActivityMongoDAOImpl.class);
	//private MongoOperations mongoOperation;
	
	@Value("#{ilcmProps['PORT_FOR_ELASTIC_SEARCH']}")
    private String PORT_FOR_ELASTIC_SEARCH;
	
	@Value("#{ilcmProps['ELASTIC_SEARCH_HOST']}")
    private String hostName;
	@Value("#{ilcmProps['CLUSTER_NAME']}")
    private String clusterName;
	

    public ActivityMongoDAOImpl(){
    }
    
    /*public ActivityMongoDAOImpl(MongoOperations mongoOperation){
        this.mongoOperation=mongoOperation;
    }

*/	@Override
	@Transactional
	public void save(ActivityMongo activityMongo) {

		log.debug("Saving Product to MOngo DB");
		try {
			//this.mongoOperation.save(activityMongo, MongodbConstants.ACTIVITY);
			
			ObjectMapper mapper = new ObjectMapper();
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
	        mapper.setDateFormat(formatter);
            String str = mapper.writeValueAsString(activityMongo);
			String indexName = MongodbConstants.ACTIVITY;
			String docType = "activity";
			
			responseData(str,indexName,docType, activityMongo.getId());
			
			
			log.info("Saved Activity to  Elastic Search");
		} catch (Exception e) {
			log.error("Unable to push Activity to  Elastic Search", e);
		}
	
		
	}

	@Override
	@Transactional
	public void deleteActivityFromMongodb(Integer activityId) {
		try{
			log.info("Deleting the activity Id: "+activityId);
		//	DBCollection dBCollection = this.mongoOperation.getCollection(MongodbConstants.ACTIVITY);
			BasicDBObject query = new BasicDBObject();
			query.append("activityId", activityId);
			//dBCollection.remove(query);
			 String indexName = MongodbConstants.ACTIVITY;
			String docType = "activity";
			 removeDocument(indexName,docType,activityId.toString());
		}catch(Exception ex){
			log.error("Unable to delete activity  ", ex);
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


