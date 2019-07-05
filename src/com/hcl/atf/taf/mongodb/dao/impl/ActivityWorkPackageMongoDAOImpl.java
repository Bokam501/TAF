package com.hcl.atf.taf.mongodb.dao.impl;

import java.text.SimpleDateFormat;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hcl.atf.taf.mongodb.constants.MongodbConstants;
import com.hcl.atf.taf.mongodb.dao.ActivityWorkPackageMongoDAO;
import com.hcl.atf.taf.mongodb.model.AcitivityWorkPackageMongo;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

public class ActivityWorkPackageMongoDAOImpl implements ActivityWorkPackageMongoDAO {

	//private MongoOperations mongoOperation;
	private static final Log log = LogFactory.getLog(ActivityWorkPackageMongoDAOImpl.class);
	
	@Value("#{ilcmProps['PORT_FOR_ELASTIC_SEARCH']}")
    private String PORT_FOR_ELASTIC_SEARCH;
	
	public ActivityWorkPackageMongoDAOImpl(){
		
	}	
	/*public ActivityWorkPackageMongoDAOImpl(MongoOperations mongoOperation) {
		
		this.mongoOperation = mongoOperation;
	}*/


	@Override
	@Transactional
	public void save(AcitivityWorkPackageMongo activityWorkPackageMongo){
		
		log.debug("Saving AcitivityWorkPackageMongo to MOngo DB");
		try {
		//	this.mongoOperation.save(activityWorkPackageMongo, MongodbConstants.ACTIVITYWORKPACKAGE);
			ObjectMapper mapper = new ObjectMapper();
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
	        mapper.setDateFormat(formatter);
            String str = mapper.writeValueAsString(activityWorkPackageMongo);
			String indexName = MongodbConstants.ACTIVITYWORKPACKAGE;
			String docType = "activityWorkpackage";
			
			responseData(str,indexName,docType, activityWorkPackageMongo.getId().toString());
			log.info("Saved AcitivityWorkPackageMongo to MongoDB");
		} catch (Exception e) {
			log.error("Unable to push AcitivityWorkPackage to MongoDB", e);
		}
		
		
		
	}
	@Override
	@Transactional
	public void deleteActivityWorkPackageFromMongoDb(Integer activityWorkPackageId) {
	try{
	log.info("Deleting Activity WP in mongoDb Id: "+activityWorkPackageId);
	/*	DBCollection dbCollection = this.mongoOperation.getCollection(MongodbConstants.ACTIVITYWORKPACKAGE);
		BasicDBObject query = new BasicDBObject();
		query.append("activityWorkPackageId", activityWorkPackageId);
		
		dbCollection.remove(query);*/
		String indexName = MongodbConstants.ACTIVITYWORKPACKAGE;
		String docType = "activityWorkpackage";
		removeDocument(indexName,docType,activityWorkPackageId.toString());
	}catch(Exception ex){
		log.error("Unable to delete ActivityWorkPackage ", ex);
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
