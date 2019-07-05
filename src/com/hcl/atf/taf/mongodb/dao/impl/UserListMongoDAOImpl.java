package com.hcl.atf.taf.mongodb.dao.impl;

import java.text.SimpleDateFormat;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hcl.atf.taf.mongodb.constants.MongodbConstants;
import com.hcl.atf.taf.mongodb.dao.UserListMongoDAO;
import com.hcl.atf.taf.mongodb.model.UserListMongo;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

@Repository
public class UserListMongoDAOImpl implements UserListMongoDAO {

	// private MongoOperations mongoOperation;

	private static final Log log = LogFactory
			.getLog(UserListMongoDAOImpl.class);

	@Value("#{ilcmProps['PORT_FOR_ELASTIC_SEARCH']}")
    private String PORT_FOR_ELASTIC_SEARCH;
	
	public UserListMongoDAOImpl() {
	}

	/*
	 * public UserListMongoDAOImpl(MongoOperations mongoOperation){
	 * this.mongoOperation=mongoOperation; }
	 */

	@Override
	@Transactional
	public void save(UserListMongo userListMongo) {

		log.debug("Saving userList to MOngo DB");
		try {
			/*this.mongoOperation.save(userListMongo,
					MongodbConstants.USER_LIST_MONGO);*/
			
			
			ObjectMapper mapper = new ObjectMapper();
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
	        mapper.setDateFormat(formatter);
            String str = mapper.writeValueAsString(userListMongo);
			String indexName = MongodbConstants.USER_LIST_MONGO;
			String docType = "user";
			responseData(str,indexName,docType, userListMongo.getId());
		} catch (Exception e) {
			log.error("Unable to push userList to MongoDB", e);
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
		//log.info("Output from ES Server .... "+output);
		} catch(Exception e) {
			log.error("Error while formating "+docType+""+e);
		}
		return output;
	}

}