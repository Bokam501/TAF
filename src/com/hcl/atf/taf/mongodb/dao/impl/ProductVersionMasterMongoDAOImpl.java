package com.hcl.atf.taf.mongodb.dao.impl;

import java.text.SimpleDateFormat;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hcl.atf.taf.model.ProductMaster;
import com.hcl.atf.taf.model.ProductVersionListMaster;
import com.hcl.atf.taf.mongodb.constants.MongodbConstants;
import com.hcl.atf.taf.mongodb.dao.ProductVersionMasterMongoDAO;
import com.hcl.atf.taf.mongodb.model.ProductVersionMasterMongo;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
@Repository
public class ProductVersionMasterMongoDAOImpl implements ProductVersionMasterMongoDAO{

	//	private MongoOperations mongoOperation;
		
	  //  private static final String PRODUCTVERSION_COLLECTION = MongodbConstants.APP_NAME+"."+MongodbConstants.VERSIONS;
		private static final Log log = LogFactory.getLog(ProductVersionMasterMongoDAOImpl.class);

		@Value("#{ilcmProps['PORT_FOR_ELASTIC_SEARCH']}")
	    private String PORT_FOR_ELASTIC_SEARCH;
		
	    public ProductVersionMasterMongoDAOImpl(){
	    }
	    
	    /*public ProductVersionMasterMongoDAOImpl(MongoOperations mongoOperation){
	        this.mongoOperation=mongoOperation;
	    }*/
		
		@Override
		public void save(ProductVersionListMaster versionlist, ProductMaster product) {
			ProductVersionMasterMongo versionMasterMongo = new ProductVersionMasterMongo(product.getProductName(), versionlist.getProductVersionName(),
					versionlist.getProductVersionDescription(), versionlist.getTargetSourceLocation(), versionlist.getTargetBinaryLocation(), 
					versionlist.getWebAppURL(), versionlist.getStatus(), versionlist.getReleaseDate(),
					versionlist.getStatusChangeDate());			
		//	this.mongoOperation.save(versionMasterMongo,product.getProductName()+"."+MongodbConstants.VERSIONS);
			
			try {
				
				ObjectMapper mapper = new ObjectMapper();
				SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
		        mapper.setDateFormat(formatter);
	            String str = mapper.writeValueAsString(versionMasterMongo);
				String indexName = MongodbConstants.VERSIONS;
				String docType = "Version";
				
				responseData(str,indexName,docType, versionMasterMongo.getId());
				
				
				log.info("Saved Product Version to  Elastic Search");
			}catch(Exception e) {
				log.error("Unable to push ProductVersion to Elastic Search", e);
			}
					
	}
		
		@Override
		@Transactional
		public void save(ProductVersionMasterMongo productVersionMasterMongo) {

			log.debug("Saving ProductVersion to MOngo DB");
			try {
				//this.mongoOperation.save(productVersionMasterMongo, MongodbConstants.MONGO_VERSIONS);
				ObjectMapper mapper = new ObjectMapper();
				SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
		        mapper.setDateFormat(formatter);
	            String str = mapper.writeValueAsString(productVersionMasterMongo);
				String indexName = MongodbConstants.VERSIONS;
				String docType = "Version";
				
				responseData(str,indexName,docType, productVersionMasterMongo.getId());
				
				
				log.info("Saved Product Version to  Elastic Search");
			} catch (Exception e) {
				log.error("Unable to push ProductVersion to MongoDB", e);
			}
		}
		
		
		@Override
		@Transactional
		public void save(List<ProductVersionMasterMongo> productVersionMasterMongo) {

			log.debug("Saving ProductVersions to MOngo DB");
			try {
				//this.mongoOperation.save(productVersionMasterMongo, MongodbConstants.MONGO_VERSIONS);
				log.info("Saved ProductVersions to MongoDB");
			} catch (Exception e) {
				log.error("Unable to push ProductVersions to MongoDB", e);
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
