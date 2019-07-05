package com.hcl.atf.taf.mongodb.dao.impl;

import java.text.SimpleDateFormat;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hcl.atf.taf.mongodb.constants.MongodbConstants;
import com.hcl.atf.taf.mongodb.dao.ChangeRequestMongoDAO;
import com.hcl.atf.taf.mongodb.model.ChangeRequestMappingMongo;
import com.hcl.atf.taf.mongodb.model.ChangeRequestMongo;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

@Repository
public class ChangeRequestMongoDAOImpl implements ChangeRequestMongoDAO {

	private static final Log log = LogFactory.getLog(ChangeRequestMongoDAOImpl.class);

	@Value("#{ilcmProps['PORT_FOR_ELASTIC_SEARCH']}")
    private String PORT_FOR_ELASTIC_SEARCH;
	
//	private MongoOperations mongoOperation;	

	public ChangeRequestMongoDAOImpl(){
    }
    
    /*public ChangeRequestMongoDAOImpl(MongoOperations mongoOperation){
        this.mongoOperation=mongoOperation;
    }*/
    
	@Override
	@Transactional
	public void save(ChangeRequestMongo changeRequestMongo) {
		log.debug("Saving changeRequestMongo to Mngo DB");
		try {
			//this.mongoOperation.save(changeRequestMongo, MongodbConstants.CHANGE_REQUEST_MONGO);
			
			ObjectMapper mapper = new ObjectMapper();
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
	        mapper.setDateFormat(formatter);
            String str = mapper.writeValueAsString(changeRequestMongo);
			String indexName = MongodbConstants.CHANGE_REQUEST_MONGO;
			String docType = "ChangeRequest";
			
			responseData(str,indexName,docType, changeRequestMongo.getId());
			
			log.info("Saved changeRequest to MongoDB");
		} catch (Exception e) {
			log.error("Unable to push changeRequest to MongoDB", e);
		}
	}

	@Override
	@Transactional
	public void saveChangeRequestMapping(ChangeRequestMappingMongo mappingMongo) {
		log.debug("Saving changeRequestMappingMongo to Mongo DB");
		try {
			//this.mongoOperation.save(mappingMongo, MongodbConstants.CHANGE_REQUEST_MAPPING_MONGO);
			ObjectMapper mapper = new ObjectMapper();
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
	        mapper.setDateFormat(formatter);
            String str = mapper.writeValueAsString(mappingMongo);
			String indexName = MongodbConstants.CHANGE_REQUEST_MAPPING_MONGO;
			String docType = "ChangeRequestMapping";
			
			responseData(str,indexName,docType, mappingMongo.getId());
			
			log.info("changeRequestMapping is saved to MongoDB");
		} catch (Exception e) {
			log.error("Unable to push changeRequestMapping to MongoDB", e);
		}
	}
	
	
	@Override
	@Transactional
	public void removeChangeRequestMapping(Integer entityRelationshipId) {
		try{
			log.info("Deleting the entityRelationshipId: "+entityRelationshipId);
			/*DBCollection dBCollection = this.mongoOperation.getCollection(MongodbConstants.CHANGE_REQUEST_MAPPING_MONGO);
			BasicDBObject query = new BasicDBObject();
			query.append("changeRequestMappingId", entityRelationshipId);
			dBCollection.remove(query);*/
			String indexName = MongodbConstants.CHANGE_REQUEST_MONGO;
			String docType = "ChangeRequest";
			
			removeDocument(indexName,docType,entityRelationshipId.toString());
			
		}catch(Exception ex){
			log.error("Unable to delete changeRequestMapping  ", ex);
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
