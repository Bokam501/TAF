package com.hcl.atf.taf.mongodb.dao.impl;

import java.text.SimpleDateFormat;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hcl.atf.taf.mongodb.constants.MongodbConstants;
import com.hcl.atf.taf.mongodb.dao.ProductTeamResourceMongoDAO;
import com.hcl.atf.taf.mongodb.model.ProductTeamResourcesMongo;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

@Repository
public class ProductTeamResourceMongoDAOImpl implements ProductTeamResourceMongoDAO {
	
	@Value("#{ilcmProps['PORT_FOR_ELASTIC_SEARCH']}")
    private String PORT_FOR_ELASTIC_SEARCH;
	
	//private MongoOperations mongoOperation;
	private static final Log log = LogFactory.getLog(ProductTeamResourceMongoDAOImpl.class);

    public ProductTeamResourceMongoDAOImpl(){
    }
    
    /*public ProductTeamResourceMongoDAOImpl(MongoOperations mongoOperation){
        this.mongoOperation=mongoOperation;
    }*/

	@Override
	public void save(ProductTeamResourcesMongo productTeamResourcesMongo) {

		log.debug("Saving productTeamResourcesMongo to MOngo DB");
		try {
		//	this.mongoOperation.save(productTeamResourcesMongo, MongodbConstants.PRODUCT_TEAM_RESOURCES);
			ObjectMapper mapper = new ObjectMapper();
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
	        mapper.setDateFormat(formatter);
            String str = mapper.writeValueAsString(productTeamResourcesMongo);
			String indexName = MongodbConstants.PRODUCT_TEAM_RESOURCES;
			String docType = "productTeamResource";
			
			responseData(str,indexName,docType, productTeamResourcesMongo.getId());
			
			log.info("Saved productTeamResourcesMongo to MongoDB");
		} catch (Exception e) {
			log.error("Unable to push productTeamResourcesMongo to MongoDB", e);
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


