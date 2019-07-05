/**
 * 
 */
package com.hcl.atf.taf.scheduler.jobs;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.stereotype.Service;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import com.hcl.atf.taf.controller.utilities.DateUtility;
import com.hcl.ers.optimus.textprocessing.TextProcessing;

/**
 * @author silambarasur
 *
 */
@Service
public class PushElasticSearchToMongoDBJob implements Job{

	private static final Log log = LogFactory.getLog(PushElasticSearchToMongoDBJob.class);

	
	@SuppressWarnings("unchecked")
	@Override
	public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
		SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
		List<String> collectionNames = new ArrayList<String>();
		collectionNames.add("builds");
		collectionNames.add("testcases");
		collectionNames.add("test_executions");
		collectionNames.add("features_mapping");
		collectionNames.add("productfeature_testcase_mapping");
		  
		  Map<String,String>mongoDBMap= new HashMap<String,String>();
		  
		  
			try{
				String mongoDBAvailable = jobExecutionContext.getJobDetail().getJobDataMap().getString("mongoDBAvailable");
				String elasticSearchAvailable = jobExecutionContext.getJobDetail().getJobDataMap().getString("elasticSearchAvailable");
				
				if(mongoDBAvailable !="" &&mongoDBAvailable.trim().equalsIgnoreCase("NO")) {
					return;
				}
				
				if(elasticSearchAvailable !="" &&elasticSearchAvailable.trim().equalsIgnoreCase("NO")) {
					return;
				}
			  
			  String fromDate="";
			  String toDate=DateUtility.sdfDateformatWithOutTime(new Date());
			  
			  Map<String,String>outPutMap=TextProcessing.getCollectionDataFromES("http://localhost:9200","9200",collectionNames,"createdDate",fromDate,toDate);
			  if(outPutMap != null && outPutMap.size() >0) {
				  for (Map.Entry<String,String> entry : outPutMap.entrySet()) {
					  String entityCollection=entry.getValue().toString();
					  JSONParser jsonParser = new JSONParser();
					  JSONArray entityArray= (JSONArray) jsonParser.parse(entityCollection);
					  for(int i=0;i<entityArray.size();i++) {
						 JSONObject entity=(JSONObject)entityArray.get(i);	
						 String productName=entity.get("productName") != null ?entity.get("productName").toString():"";
						 String _id=entity.get("id") != null ?entity.get("id").toString():"";
						 entity.put("_id", _id);
						 entity.put("synTime",DateUtility.dateToStringInSecond(new Date()));
						 if(productName == null || productName == "") {
							 productName="EmptyProduct";
						 } else {
							 String key = productName+"."+entry.getKey();
							 if(mongoDBMap.containsKey(key)) {
								 JSONArray jsonArray = (JSONArray) jsonParser.parse(mongoDBMap.get(key).toString());
								 jsonArray.add(entity);
								 mongoDBMap.put(key, jsonArray.toJSONString());
							 } else {
								 JSONArray jsonArray = new JSONArray();
								 jsonArray.add(entity);
								 mongoDBMap.put(key, jsonArray.toJSONString());
							 }
						 }
						 
					  }
				  }
				  
				  
			  }
			 
			  TextProcessing.insertDataFromMongo("localhost",27017,"ise",mongoDBMap);
		  }catch(Exception e ) {
			  log.error("Error in Mongo db Push",e);
		  }
		
	}

}
