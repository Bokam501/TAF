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
import com.hcl.atf.taf.mongodb.dao.ActivityTaskMongoDAO;
import com.hcl.atf.taf.mongodb.model.ActivityTaskMongo;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
@Repository
public class ActivityTaskMongoDAOImpl implements ActivityTaskMongoDAO{
		//private MongoOperations mongoOperation;
		private static final Log log = LogFactory.getLog(ActivityTaskMongoDAOImpl.class);
		
		@Value("#{ilcmProps['PORT_FOR_ELASTIC_SEARCH']}")
	    private String PORT_FOR_ELASTIC_SEARCH;
		
		public ActivityTaskMongoDAOImpl(){
			
		}
		/*public ActivityTaskMongoDAOImpl(MongoOperations mongoOperation){
			this.mongoOperation = mongoOperation;
		}*/
		
		
		@Override
		@Transactional
		public void save(ActivityTaskMongo activityTaskMongo) {

			log.info("Saving activityTaskMongo to MOngo DB");
			try {
				log.info("Saving activityTaskMongo to MOngo DB activityTaskMongo.getActivityTaskId()"+activityTaskMongo.getActivityTaskId());
			
				//this.mongoOperation.save(activityTaskMongo, MongodbConstants.ACTIVITYTASK);

				ObjectMapper mapper = new ObjectMapper();
				SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
		        mapper.setDateFormat(formatter);
	            String str = mapper.writeValueAsString(activityTaskMongo);
				String indexName = MongodbConstants.ACTIVITYTASK;
				String docType = "activityTask";
				
				responseData(str,indexName,docType, activityTaskMongo.getId().toString());
				
				log.info("Saved activityTaskMongo to elastic serach ");
			} catch (Exception e) {
				log.error("Unable to push activityTaskMongo to MongoDB", e);
			}
		}
		
		@Override
		@Transactional
		public void save(List<ActivityTaskMongo> activityTaskMongo) {

			/*log.debug("Saving activityTaskMongo to MOngo DB");
			try {
				this.mongoOperation.save(activityTaskMongo, MongodbConstants.ACTIVITYTASK);
				log.info("Saved activityTaskMongo to MongoDB");
			} catch (Exception e) {
				log.error("Unable to push activityTaskMongo to MongoDB", e);
			}*/
		}
		
		@Override
		@Transactional
		public void deleteActivityTaskFromMongoDb(Integer activityTaskId) {
			try{
				log.info("Deleting Activity Task from MongoDb Id"+ activityTaskId);
				
			/*	DBCollection dbCollection = this.mongoOperation.getCollection(MongodbConstants.ACTIVITYTASK);
				BasicDBObject query = new BasicDBObject();
				query.append("activityTaskId", activityTaskId);
				dbCollection.remove(query);*/
				String indexName = MongodbConstants.ACTIVITYTASK;
				String docType = "activityTask";
				 removeDocument(indexName,docType,activityTaskId.toString());
			}catch(Exception ex){
				log.error("Unable to delete ActivityTask in Mongodb ", ex);
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
