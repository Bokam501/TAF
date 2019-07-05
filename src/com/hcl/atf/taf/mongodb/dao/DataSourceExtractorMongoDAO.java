package com.hcl.atf.taf.mongodb.dao;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.json.JSONObject;

import com.hcl.atf.taf.mongodb.model.CalculatedDefectCollectionMongo;

public interface DataSourceExtractorMongoDAO {

	Boolean checkMongoConnectivity();
	
	void save(Object saveObject, String collectionName);
	void save(HashMap<String, Object> data, String collectionName);
	
	Object findDocumentById(String documentId);
	
	CalculatedDefectCollectionMongo getCalculatedDefectCollection(String testFactoryName, String programName, Date weekDate, String collectionName);
	JSONObject getTrainingTypeByNameAndCompetency(String testFactoryName, String trainingName, String competency, String collectionName);
	JSONObject getSingleMetricValueDetails(HashMap<String, Object> metricValue, String collectionName);

	void save(JSONObject saveObject, String collectionName);

	void saveDefect(HashMap<String, Object> data, String collectionName);


	List<JSONObject> getSlaMetricsCollectionByFillter(Date dateFillter, Float pmtId);



	
}
