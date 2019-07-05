package com.hcl.atf.taf.mongodb.dao.impl;


import java.text.SimpleDateFormat;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hcl.atf.taf.model.ProductBuild;
import com.hcl.atf.taf.model.UserList;
import com.hcl.atf.taf.mongodb.constants.MongodbConstants;
import com.hcl.atf.taf.mongodb.dao.BuildMongoDAO;
import com.hcl.atf.taf.mongodb.model.ISEProductBuildMongo;
import com.hcl.atf.taf.mongodb.model.ProductBuildMongo;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
@Repository
public class BuildMongoDAOImpl implements BuildMongoDAO  {
	/*DB db = null;		
	DBCollection collection = null;
	DBObject document=null;*/
	//private MongoOperations mongoOperation;
	private static final Log log = LogFactory.getLog(BuildMongoDAOImpl.class);
	
	@Value("#{ilcmProps['PORT_FOR_ELASTIC_SEARCH']}")
    private String PORT_FOR_ELASTIC_SEARCH;
	
	public BuildMongoDAOImpl(){
		
	}
	/*public BuildMongoDAOImpl(MongoOperations mongoOperation){
		this.mongoOperation = mongoOperation;
	}*/
	
	@Override
	public void save(ProductBuild productBuild, String productName, UserList user) {
		// TODO Auto-generated method stub		
	 ProductBuildMongo productBuildMongo = new ProductBuildMongo();	 
	 
	 //this.mongoOperation.save(productBuildMongo, productName+"."+MongodbConstants.BUILD);	
		
	}
	
	@Override
	@Transactional
	public void save(ProductBuildMongo productBuildMongo) {

		log.debug("Saving ProductBuild to MOngo DB");
		try {
			//this.mongoOperation.save(productBuildMongo, MongodbConstants.BUILD);
			ObjectMapper mapper = new ObjectMapper();
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
	        mapper.setDateFormat(formatter);
            String str = mapper.writeValueAsString(productBuildMongo);
			String indexName = MongodbConstants.PRODCTBUILD;
			String docType = "build";
			
			responseData(str,indexName,docType, productBuildMongo.getId());
			
			log.info("Saved ProductBuild to MongoDB");
		} catch (Exception e) {
			log.error("Unable to push ProductBuild to MongoDB", e);
		}
	}
	
	@Override
	@Transactional
	public void save(List<ProductBuildMongo> productBuildMongo) {

		log.debug("Saving ProductBuild to MOngo DB");
		try {
			//this.mongoOperation.save(productBuildMongo, MongodbConstants.BUILD);
			log.info("Saved ProductBuild to MongoDB");
		} catch (Exception e) {
			log.error("Unable to push ProductBuild to MongoDB", e);
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

	@Override
	public void saveISE(ISEProductBuildMongo iseProductBuildMongo) {
		log.debug("Saving ProductBuild to MOngo DB");
		try {
			//this.mongoOperation.save(productBuildMongo, MongodbConstants.BUILD);
			ObjectMapper mapper = new ObjectMapper();
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
	        mapper.setDateFormat(formatter);
            String str = mapper.writeValueAsString(iseProductBuildMongo);
			String indexName = MongodbConstants.BUILD;
			String docType = "build";
			
			responseData(str,indexName,docType, iseProductBuildMongo.getId());
			
			log.info("Saved ISE ProductBuild to MongoDB");
		} catch (Exception e) {
			log.error("Unable to push ISE ProductBuild to MongoDB", e);
		}
	}
}
