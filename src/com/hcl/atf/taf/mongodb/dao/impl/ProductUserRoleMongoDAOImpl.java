package com.hcl.atf.taf.mongodb.dao.impl;

import java.text.SimpleDateFormat;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hcl.atf.taf.mongodb.constants.MongodbConstants;
import com.hcl.atf.taf.mongodb.dao.ProductUserRoleMongoDAO;
import com.hcl.atf.taf.mongodb.model.ProductUserRoleMongo;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

@Repository
public class ProductUserRoleMongoDAOImpl implements ProductUserRoleMongoDAO {

	// private MongoOperations mongoOperation;
	private static final Log log = LogFactory
			.getLog(ProductUserRoleMongoDAOImpl.class);

	@Value("#{ilcmProps['PORT_FOR_ELASTIC_SEARCH']}")
	private String PORT_FOR_ELASTIC_SEARCH;

	public ProductUserRoleMongoDAOImpl() {
	}

	/*
	 * public ProductUserRoleMongoDAOImpl(MongoOperations mongoOperation){
	 * this.mongoOperation=mongoOperation; }
	 */

	@Override
	public void save(ProductUserRoleMongo productUserRoleMongo) {

		log.debug("Saving ProductUserRoleMongo to MOngo DB");
		try {
			// this.mongoOperation.save(productUserRoleMongo,
			// MongodbConstants.PRODUCTS_USER_ROLE);

			ObjectMapper mapper = new ObjectMapper();
			SimpleDateFormat formatter = new SimpleDateFormat(
					"yyyy-MM-dd'T'HH:mm:ss");
			mapper.setDateFormat(formatter);
			String str = mapper.writeValueAsString(productUserRoleMongo);
			String indexName = MongodbConstants.PRODUCTS_USER_ROLE;
			String docType = "productUserRole";

			responseData(str, indexName, docType, productUserRoleMongo.getId().toString());

			log.info("Saved ProductUserRoleMongo to MongoDB");
		} catch (Exception e) {
			log.error("Unable to push ProductUserRoleMongo to MongoDB", e);
		}
	}

	@Override
	public void save(List<ProductUserRoleMongo> productUserRoleMongoList) {

		log.debug("Saving ProductUserRoleMongo to MOngo DB");
		try {
			/*this.mongoOperation.save(productUserRoleMongoList,
					MongodbConstants.PRODUCTS);*/
			// this.mongoOperation.insert(productMasterMongo,
			// MongodbConstants.PRODUCTS);

			log.info("Saved ProductUserRoleMongo to MongoDB");
		} catch (Exception e) {
			log.error("Unable to push ProductUserRoleMongo to MongoDB", e);
		}
	}

	private String responseData(String str, String indexName, String docType,
			String _id) {
		String output = "";
		try {
			Client client = Client.create();
			ClientResponse response = null;
			String esUrl = PORT_FOR_ELASTIC_SEARCH;
			String url = esUrl + "/" + indexName + "/" + docType + "/" + _id;
			WebResource webResource = client.resource(url.toString());
			response = webResource.accept("application/json").post(
					ClientResponse.class, str);
			output = response.getEntity(String.class);
		} catch (Exception e) {
			log.error("Error while formating " + docType + "" + e);
		}
		return output;
	}

}
