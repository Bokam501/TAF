
package com.hcl.atf.taf.mongodb.dao.impl;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hcl.atf.taf.mongodb.constants.MongodbConstants;
import com.hcl.atf.taf.mongodb.dao.TestCaseDefectsMongoDAO;
import com.hcl.atf.taf.mongodb.model.TestCaseDefectsMasterMongo;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

@Repository
public class TestCaseDefectsMongoDAOImpl implements TestCaseDefectsMongoDAO {

	private static final Log log = LogFactory.getLog(TestCaseDefectsMongoDAOImpl.class);
	
	//private MongoOperations mongoOperation;
	
   // private static final String TESTCASE_DEFECTS_COLLECTION = MongodbConstants.APP_NAME+"."+MongodbConstants.DEFECTS;
	
	@Value("#{ilcmProps['PORT_FOR_ELASTIC_SEARCH']}")
    private String PORT_FOR_ELASTIC_SEARCH;

    public TestCaseDefectsMongoDAOImpl(){
    }
    
    /*public TestCaseDefectsMongoDAOImpl(MongoOperations mongoOperation){
        this.mongoOperation=mongoOperation;
    }
	*/
	@Override
	@Transactional
	public void save(TestCaseDefectsMasterMongo defectMongo) {
		try {
			//this.mongoOperation.save(defectMongo, MongodbConstants.DEFECTS);
			
			ObjectMapper mapper = new ObjectMapper();
            String str = mapper.writeValueAsString(defectMongo);
			String indexName = MongodbConstants.DEFECTS;
			String docType = "Defects";
			responseData(str,indexName,docType, defectMongo.getId());
			log.info("Saved Defect to Elastic Search");
		} catch (Exception e) {
			log.error("Unable to push Defect to MongoDB", e);
		}
	}

	@Override
	@Transactional
	public void save(List<TestCaseDefectsMasterMongo> defectsMongo) {
		try {
			//this.mongoOperation.save(defectsMongo, MongodbConstants.DEFECTS);
			log.info("Saved Defect to MongoDB");
		} catch (Exception e) {
			log.error("Unable to push Defect to MongoDB", e);
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
