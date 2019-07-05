package com.hcl.atf.taf.mongodb.dao.impl;

import java.text.SimpleDateFormat;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hcl.atf.taf.mongodb.constants.MongodbConstants;
import com.hcl.atf.taf.mongodb.dao.WorkflowEventMongoDAO;
import com.hcl.atf.taf.mongodb.model.WorkflowEventMongo;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

@Repository
public class WorkflowEventMongoDAOImpl implements WorkflowEventMongoDAO {

	// private MongoOperations mongoOperation;
	private static final Log log = LogFactory
			.getLog(WorkflowEventMongoDAOImpl.class);

	@Value("#{ilcmProps['PORT_FOR_ELASTIC_SEARCH']}")
    private String PORT_FOR_ELASTIC_SEARCH;
	
	public WorkflowEventMongoDAOImpl() {
	}

	/*public WorkflowEventMongoDAOImpl(MongoOperations mongoOperation) {
		this.mongoOperation = mongoOperation;
	}
*/
	@Override
	@Transactional
	public void save(WorkflowEventMongo workflowEventMongo) {

		log.debug("Saving Product to MOngo DB");
		try {
			/*this.mongoOperation.save(workflowEventMongo,
					MongodbConstants.WORK_FLOW_EVENTS);*/
			
			
			ObjectMapper mapper = new ObjectMapper();
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
	        mapper.setDateFormat(formatter);
            String str = mapper.writeValueAsString(workflowEventMongo);
			String indexName = MongodbConstants.WORK_FLOW_EVENTS;
			String docType = "workflowEvent";
			
			responseData(str,indexName,docType, workflowEventMongo.getId());
			
			log.info("Saved ActivityEffortTracker to MongoDB");
		} catch (Exception e) {
			log.error("Unable to push ActivityEffortTracker to MongoDB", e);
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
