package com.hcl.atf.taf.mongodb.dao.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.bson.types.ObjectId;
import org.json.JSONObject;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hcl.atf.taf.mongodb.dao.DataSourceExtractorMongoDAO;
import com.hcl.atf.taf.mongodb.model.ActivityCollectionMongo;
import com.hcl.atf.taf.mongodb.model.CalculatedDefectCollectionMongo;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.util.JSON;

@Repository
public class DataSourceExtractorMongoDAOImpl implements DataSourceExtractorMongoDAO {
	
	private static final Log log = LogFactory.getLog(DataSourceExtractorMongoDAOImpl.class);

	private MongoOperations mongoOperation;	

	public DataSourceExtractorMongoDAOImpl(){
	}
	    
	public DataSourceExtractorMongoDAOImpl(MongoOperations mongoOperation){
	       this.mongoOperation=mongoOperation;
	}

	@Override
	@Transactional
	public Boolean checkMongoConnectivity() {
		boolean isMongoConnectionAvailable = true;
		log.debug("Inside checkMongoConnectivity");
		try {
			DBCollection dBCollection = this.mongoOperation.getCollection("connection_check");
			dBCollection.find().count();
			log.debug("Connection successfull");
		} catch (Exception e) {
			isMongoConnectionAvailable = false;
			log.error("Connection failure", e);
		}
		return isMongoConnectionAvailable;
	}
	
	/*
	 * This method will insert this record into MongoDB for the first time and later on, update it based on the _id field value
	 * @see com.hcl.atf.taf.mongodb.model.DashboardMongo#save(com.hcl.atf.taf.mongodb.model.DashboardMongo)
	 */
	@Override
	@Transactional
	public void save(Object saveObject, String collectionName) {

		log.debug("Saving Dasghboard to MOngo DB");
		try {
			this.mongoOperation.save(saveObject, collectionName);
			log.debug("Saved Dashboard to MongoDB");
		} catch (Exception e) {
			log.error("Unable to push Dashboard to MongoDB", e);
		}
	}
	
	
	@Override
	@Transactional
	public void save(JSONObject saveObject, String collectionName) {
		log.debug("Saving Dasghboard to MOngo DB");
		
		HashMap<String, Object> mapValues = new HashMap<String, Object>();
		try {
			
			
			Iterator<String> keysItr = saveObject.keys();
			
			while(keysItr.hasNext()){
				String key = keysItr.next();
		        Object value = saveObject.get(key);
		        mapValues.put(key, value);
		        
			}
			
			this.mongoOperation.save(mapValues, collectionName);
			log.debug("Saved Dashboard to MongoDB");
		} catch (Exception e) {
			log.error("Unable to push Dashboard to MongoDB", e);
		}
	}
	
	@Override
	@Transactional
	public void save(HashMap<String, Object> data, String collectionName) {

		log.debug("Saving Dasghboard to MOngo DB");
		try {
			DBObject dataObject = (DBObject) JSON.parse(new JSONObject().toString());

			/*HashMap<String, Object> result = new HashMap<String, Object>();
			Iterator<?> keys = data.keys();
	        while(keys.hasNext()){
	            String key = (String)keys.next();
	            String value = data.getString(key); 
	            if("_id".equalsIgnoreCase(key)){
					if(value.length() == 24){
						result.put(key, new ObjectId(value));
					}
				}else{
					result.put(key, value);
				}
	        }*/
			 if(data.containsKey("_id")){
				 String _id = (String) data.get("_id");
				if(_id != null && _id.length() == 24){
					data.put("_id", new ObjectId(_id));
				}
			}
			dataObject.putAll(data);
			this.mongoOperation.save(dataObject, collectionName);
			log.debug("Saved Dashboard to MongoDB");
		} catch (Exception e) {
			log.error("Unable to push Dashboard to MongoDB", e);
		}
	}
	
	
	@Override
	@Transactional
	public void saveDefect(HashMap<String, Object> data, String collectionName) {

		log.debug("Saving Dasghboard to MOngo DB");
		try {
			this.mongoOperation.save(data, collectionName);
			log.debug("Saved Dashboard to MongoDB");
		} catch (Exception e) {
			log.error("Unable to push Dashboard to MongoDB", e);
		}
	}
	
	@Override
	@Transactional
	public Object findDocumentById(String documentId) {
		
		Object returnObject = null;
		
		log.debug("Retriving document from Mongo DB for Id - "+documentId);
		try {
			DBCollection dBCollection = this.mongoOperation.getCollection("dummy_activity_collection");
			BasicDBObject whereQuery = new BasicDBObject();
			whereQuery.put("_id", documentId);
			DBCursor cursor = dBCollection.find(whereQuery);
			cursor = dBCollection.find().sort(new BasicDBObject("actualActivityStartDate",1)).limit(1); 
			ObjectMapper mapper = new ObjectMapper();
			mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
			
			while (cursor.hasNext()) {
				String cursorData = cursor.next().toString();
				returnObject = mapper.readValue(cursorData, ActivityCollectionMongo.class);
				
			}
			log.debug("Dashboard details retrived for id - "+documentId);
		} catch (Exception e) {
			log.error("Unable to retrive Dashboard details - "+documentId, e);
		}
		return returnObject;
	}
	
	@Override
	@Transactional
	public CalculatedDefectCollectionMongo getCalculatedDefectCollection(String testFactoryName, String programName, Date weekDate, String collectionName) {
		CalculatedDefectCollectionMongo calculatedDefectCollectionMongo = null;
		log.debug("Retrive calculated defect collection");
		try {
			DBCollection dBCollection = this.mongoOperation.getCollection((collectionName).toLowerCase());
			
			BasicDBObject andQuery = new BasicDBObject();
			List<BasicDBObject> constraintsList = new ArrayList<BasicDBObject>();
			constraintsList.add(new BasicDBObject("testFactoryName", testFactoryName));
			constraintsList.add(new BasicDBObject("productName", programName));
			BasicDBObject lteWeekDateQuery = new BasicDBObject();
			lteWeekDateQuery.put("$lte", weekDate);
			constraintsList.add(new BasicDBObject("weekDate", lteWeekDateQuery));
			andQuery.put("$and", constraintsList);
			
			DBCursor cursor = dBCollection.find(andQuery).sort(new BasicDBObject("weekDate", -1)).limit(1); 
			
			ObjectMapper mapper = new ObjectMapper();
			mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
			
			while (cursor.hasNext()) {
				String cursorData = cursor.next().toString();
				calculatedDefectCollectionMongo = mapper.readValue(cursorData, CalculatedDefectCollectionMongo.class);
				
			}
		} catch (Exception e) {
			log.error("Unable to get calculated defect collection from MongoDB", e);
		}
		return calculatedDefectCollectionMongo;
	}

	@Override
	@Transactional
	public JSONObject getTrainingTypeByNameAndCompetency(String testFactoryName, String trainingName, String competency, String collectionName) {
		JSONObject trainingType = null;
		log.debug("Retrive Training details of name - "+trainingName);
		try {
			DBCollection dBCollection = this.mongoOperation.getCollection((collectionName).toLowerCase());
			
			BasicDBObject andQuery = new BasicDBObject();
			List<BasicDBObject> constraintsList = new ArrayList<BasicDBObject>();
			constraintsList.add(new BasicDBObject("testFactoryName", testFactoryName));
			constraintsList.add(new BasicDBObject("Type", trainingName));
			constraintsList.add(new BasicDBObject("Competency", competency));
			andQuery.put("$and", constraintsList);
			
			DBCursor cursor = dBCollection.find(andQuery).limit(1); 
			
			while (cursor.hasNext()) {
				trainingType = new JSONObject(cursor.next().toString());
			}
		} catch (Exception e) {
			log.error("Unable to get training information from MongoDB", e);
		}
		return trainingType;
	}

	@Override
	@Transactional
	public JSONObject getSingleMetricValueDetails(HashMap<String, Object> metricValue, String collectionName) {
		JSONObject singleValueMetric = null;
		try {
			log.debug("Retrive Single metric value details for date - "+metricValue.get("Date")+" and month - "+metricValue.get("Month"));
			DBCollection dBCollection = this.mongoOperation.getCollection((collectionName).toLowerCase());
			
			BasicDBObject andQuery = new BasicDBObject();
			List<BasicDBObject> constraintsList = new ArrayList<BasicDBObject>();
			constraintsList.add(new BasicDBObject("Date", metricValue.get("Date")));
			constraintsList.add(new BasicDBObject("Month", metricValue.get("Month")));
			constraintsList.add(new BasicDBObject("testFactoryName", metricValue.get("testFactoryName")));
			constraintsList.add(new BasicDBObject("Competency", metricValue.get("Competency")));
			andQuery.put("$and", constraintsList);
			
			DBCursor cursor = dBCollection.find(andQuery).limit(1); 
			
			while (cursor.hasNext()) {
				singleValueMetric = new JSONObject(cursor.next().toString());
			}
		} catch (Exception e) {
			log.error("Unable to get single metric value information from MongoDB", e);
		}
		return singleValueMetric;
	}


	@Override
	@Transactional
	public List<JSONObject> getSlaMetricsCollectionByFillter(Date dateFillter,Float pmtId) {
		List<JSONObject>metricsList=new ArrayList<JSONObject>();
		try{
		
			List<BasicDBObject> obj = new ArrayList<BasicDBObject>();

			DBCollection dBCollection = this.mongoOperation.getCollection("sla_metrics_collection");
			BasicDBObject allQuery=new BasicDBObject();
	
			obj.add(new BasicDBObject("Measurement Period",dateFillter));
			obj.add(new BasicDBObject("PMT",pmtId));
			
			allQuery.put("$and", obj);
	
			DBCursor cursor = dBCollection.find(allQuery);
				
	         while (cursor.hasNext()) { 
	        	 metricsList.add(new JSONObject(cursor.next().toString()));
	         }			
			
		}catch(Exception e){
			log.error("Unable to retrive ", e);
		}		
		return metricsList;
	}

	

	

}
