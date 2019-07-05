package com.hcl.atf.taf.mongodb.dao.impl;

import java.text.SimpleDateFormat;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hcl.atf.taf.mongodb.constants.MongodbConstants;
import com.hcl.atf.taf.mongodb.dao.WorkPackageMongoDAO;
import com.hcl.atf.taf.mongodb.model.WorkPackageMongo;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

@Repository
public class WorkPackageMongoDAOImpl implements WorkPackageMongoDAO {
	
	private static final Log log = LogFactory.getLog(WorkPackageMongoDAOImpl.class);

//	private MongoOperations mongoOperation;	

	@Value("#{ilcmProps['PORT_FOR_ELASTIC_SEARCH']}")
    private String PORT_FOR_ELASTIC_SEARCH;
	
	public WorkPackageMongoDAOImpl(){
    }
    
    /*public WorkPackageMongoDAOImpl(MongoOperations mongoOperation){
        this.mongoOperation=mongoOperation;
    }*/
    
	@Override
	public void save(WorkPackageMongo workPackageMongo) {

		log.debug("Saving Workpackage to MOngo DB");
		try {
			//this.mongoOperation.save(workPackageMongo, MongodbConstants.WORKPACKAGE);
			
			ObjectMapper mapper = new ObjectMapper();
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
	        mapper.setDateFormat(formatter);
            String str = mapper.writeValueAsString(workPackageMongo);
			String indexName = MongodbConstants.WORKPACKAGE;
			String docType = "workpackage";
			
			responseData(str,indexName,docType, workPackageMongo.getId());
			
			log.info("Saved workPackage to MongoDB");
		} catch (Exception e) {
			log.error("Unable to push workPackage to MongoDB", e);
		}
	}

	@Override
	public void save(List<WorkPackageMongo> workPackagesMongo) {

		log.debug("Saving Workpackage to MOngo DB");
		try {
			//this.mongoOperation.save(workPackagesMongo, MongodbConstants.WORKPACKAGE);
			log.info("Saved workPackage to MongoDB");
		} catch (Exception e) {
			log.error("Unable to push workPackage to MongoDB", e);
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


