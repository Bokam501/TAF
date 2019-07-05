package com.hcl.atf.taf.mongodb.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hcl.atf.taf.controller.utilities.DateUtility;
import com.hcl.atf.taf.mongodb.dao.DataSourceExtractorMongoDAO;
import com.hcl.atf.taf.mongodb.model.CalculatedDefectCollectionMongo;
import com.hcl.atf.taf.mongodb.service.DataSourceExtractorMongoService;

@Service
public class DataSourceExtractorMongoServiceImpl implements DataSourceExtractorMongoService {
	
	@Autowired
	private DataSourceExtractorMongoDAO dataSourceExtractorMongoDAO;
	
	@Override
	@Transactional
	public Boolean checkMongoConnectivity() {
		return dataSourceExtractorMongoDAO.checkMongoConnectivity();
	}
	
	@Override
	@Transactional
	public void save(Object saveObject, String collectionName) {
		dataSourceExtractorMongoDAO.save(saveObject, collectionName);
	}
	
	
	
	@Override
	@Transactional
	public void save(JSONObject saveObject, String collectionName) {
		dataSourceExtractorMongoDAO.save(saveObject, collectionName);
	}

	@Override
	@Transactional
	public void save(HashMap<String, Object> data, String collectionName) {
		dataSourceExtractorMongoDAO.save(data, collectionName);
	}
	
	@Override
	@Transactional
	public Object findDocumentById(String documentId) {
		return dataSourceExtractorMongoDAO.findDocumentById(documentId);
	}
	
	@Override
	@Transactional
	public CalculatedDefectCollectionMongo getCalculatedDefectCollection(String testFactoryName, String programName, Date weekDate, String collectionName) {
		return dataSourceExtractorMongoDAO.getCalculatedDefectCollection(testFactoryName, programName, weekDate, collectionName);
	}

	@Override
	@Transactional
	public JSONObject getTrainingTypeByNameAndCompetency(String testFactoryName, String trainingName, String competency, String collectionName) {
		return dataSourceExtractorMongoDAO.getTrainingTypeByNameAndCompetency(testFactoryName, trainingName, competency, collectionName);
	}

	@Override
	@Transactional
	public JSONObject getSingleMetricValueDetails(HashMap<String, Object> metricValue, String collectionName) {
		return dataSourceExtractorMongoDAO.getSingleMetricValueDetails(metricValue, collectionName);
	}

	@Override
	@Transactional
	public void saveDefect(HashMap<String, Object> data, String collectionName) {
		dataSourceExtractorMongoDAO.saveDefect(data, collectionName);
		
	}

	@Override
	@Transactional
	public List<HashMap<String, Object>> getCumulativeSlaMetricsCollectionFromCmpmAndDefectExtracter(HashMap<String, Object> metricDetails, String metricsParam) {
		List<HashMap<String, Object>> slaCollectionAllData = new ArrayList<HashMap<String, Object>>();
		
		Float metricValue = 0f;
		for(Map.Entry<String, Object> entry : metricDetails.entrySet()){
			HashMap<String, Object> slaCollection = new HashMap<String, Object>();
			String cmpmData[] =  entry.getKey().split("~");
			metricValue = (Float) entry.getValue();
			
			Date mesaurementPeriod=DateUtility.dateFromISTFormatString(cmpmData[0].toString());
			Calendar cal = Calendar.getInstance();
			cal.setTime(mesaurementPeriod);
			cal.set(Calendar.HOUR_OF_DAY, 0);
			cal.set(Calendar.MINUTE, 0);
			cal.set(Calendar.SECOND, 0);
			cal.set(Calendar.MILLISECOND, 0);
			
			
			String pmtId = cmpmData[1];
			List<JSONObject> metricsList = dataSourceExtractorMongoDAO.getSlaMetricsCollectionByFillter(setDateForMongoDB(cal.getTime()),Float.parseFloat(pmtId));
			if(metricsList != null && metricsList.size()>0){
				for(JSONObject metrics : metricsList){
					try {
						if(metrics.has(metricsParam)){
							metricValue = metricValue + ((Double) metrics.get(metricsParam)).floatValue();
							
						}
						metrics.put(metricsParam, metricValue);
						String date = metrics.get("Measurement Period").toString();
						
						metrics.put("Measurement Period",convertMongoDBStringToDate(date) );
						Float defectsCounts =0F;
						Float efforts = 0F;
						if(metrics.has("Total Production Defects")){
							 defectsCounts = Float.parseFloat(metrics.get("Total Production Defects").toString());
						}
						if(metrics.has("Total Construction Effort")){
							efforts = Float.parseFloat(metrics.get("Total Construction Effort").toString());
						}
						
						if(efforts > 0){
							Float applicationQuality  = defectsCounts/efforts;
							metrics.put("Application Quality", applicationQuality);
						}
						
						
						Iterator<String> keysItr = metrics.keys();
						
						while(keysItr.hasNext()){
							String keyValue = keysItr.next();
					        Object value = metrics.get(keyValue);
					        slaCollection.put(keyValue, value);
						}
						
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
			}else{
				slaCollection.put("Measurement Period", setDateForMongoDB(cal.getTime()));
				slaCollection.put("PMT", Float.parseFloat(pmtId));
				slaCollection.put(metricsParam, metricValue);//("Total Construction Effort", efforts);
				slaCollection.put("_id", cmpmData[1]+"_"+setDateForMongoDB(cal.getTime()));
				
			}
			
			slaCollectionAllData.add(slaCollection);
			
		}
		
		return slaCollectionAllData;
	}

	private Date convertMongoDBStringToDate(String mongoDBDateString){
		Date convertedDate = null;
		if(mongoDBDateString != null && mongoDBDateString.contains("$date")){
			try {
				SimpleDateFormat dateFormatForMongoDB = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.ENGLISH);
				convertedDate = dateFormatForMongoDB.parse(mongoDBDateString.split(":\"")[1].split("\"}")[0]);
				convertedDate = setDateForMongoDB(convertedDate);
			} catch (Exception e) {
			}
		}
		return convertedDate;
	}

	private Date setDateForMongoDB(Date dateToMongoDB){
		if(dateToMongoDB != null){
			Calendar dateToMongoDBCalendar = Calendar.getInstance();
			dateToMongoDBCalendar.setTime(dateToMongoDB);
			dateToMongoDBCalendar.add(Calendar.MILLISECOND, dateToMongoDBCalendar.getTimeZone().getRawOffset());
			dateToMongoDB = dateToMongoDBCalendar.getTime();
		}
		return dateToMongoDB;
	}
}
