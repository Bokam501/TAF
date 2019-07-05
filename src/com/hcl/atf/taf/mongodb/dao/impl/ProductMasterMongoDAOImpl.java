package com.hcl.atf.taf.mongodb.dao.impl;

import java.text.SimpleDateFormat;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hcl.atf.taf.model.json.JsonProductMaster;
import com.hcl.atf.taf.mongodb.constants.MongodbConstants;
import com.hcl.atf.taf.mongodb.dao.ProductMasterMongoDAO;
import com.hcl.atf.taf.mongodb.model.ProductMasterMongo;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

@Repository
public class ProductMasterMongoDAOImpl implements ProductMasterMongoDAO {
	
	//private MongoOperations mongoOperation;
	private static final Log log = LogFactory.getLog(ProductMasterMongoDAOImpl.class);
	
	@Value("#{ilcmProps['PORT_FOR_ELASTIC_SEARCH']}")
    private String PORT_FOR_ELASTIC_SEARCH;
	
    public ProductMasterMongoDAOImpl(){
    }
    
  /*  public ProductMasterMongoDAOImpl(MongoOperations mongoOperation){
        this.mongoOperation=mongoOperation;
    }*/
     
	@Override
	@Transactional
	public void save(JsonProductMaster product) {
		
		ProductMasterMongo productMasterMongo= new ProductMasterMongo();
	
	//	this.mongoOperation.save(productMasterMongo,PRODUCT_COLLECTION);
		
		
	}
	
	@Override
	@Transactional
	public void save(ProductMasterMongo productMasterMongo) {

		log.debug("Saving Product to MOngo DB");
		try {
			//this.mongoOperation.save(productMasterMongo, MongodbConstants.PRODUCTS);
			
				
				ObjectMapper mapper = new ObjectMapper();
				SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
		        mapper.setDateFormat(formatter);
	            String str = mapper.writeValueAsString(productMasterMongo);
				String indexName = MongodbConstants.PRODUCTS;
				String docType = "product";
				
				responseData(str,indexName,docType, productMasterMongo.getId());
				
				
				log.info("Saved Product to  Elastic Search");
			
			log.info("Saved Product to MongoDB");
		} catch (Exception e) {
			log.error("Unable to push Product to MongoDB", e);
		}
	}

	@Override
	@Transactional
	public void save(List<ProductMasterMongo> productMasterMongo) {

		log.debug("Saving Product to MOngo DB");
		try {
		//	this.mongoOperation.save(productMasterMongo, MongodbConstants.PRODUCTS);
//			this.mongoOperation.insert(productMasterMongo, MongodbConstants.PRODUCTS);
			
			log.info("Saved Product to MongoDB");
		} catch (Exception e) {
			log.error("Unable to push Product to MongoDB", e);
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


