package com.hcl.atf.taf.schedule.data.extractor;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;

import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hcl.atf.taf.model.DataExtractorNotification;
import com.hcl.atf.taf.model.UtilizationCollection;

@Service
public class UtilizationIndexScheduleDataExtractor {

	@Autowired
	CollectionValuesScheduleMapper collectionValuesScheduleMapper;
	
	private static final Log log = LogFactory.getLog(UtilizationIndexScheduleDataExtractor.class);

	public List<UtilizationCollection> readUtilizationIndexCSV(LinkedHashMap<String, String> dataTemplate, List<String> headerCellValueList, List<String[]> contentRows, String utilizationPeriod, DataExtractorNotification dataExtractorNotification) {

		List<UtilizationCollection> utilizationCollections = new ArrayList<UtilizationCollection>();
		try {
			log.info("Inside readUtilizationIndexCSV()");

			int numberOfValidRecords = 0;
			int numberOfInvalidRecords = 0;
			
			SimpleDateFormat utilizationIndexDataDateFormat = new SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH);
			String resourceName = null;
			String moduleName = null;
			String activityName = null;
			String task = null;
			Float actualEffort = 0.0F;
			UtilizationCollection utilizationCollection = null;
			for (int rowCounter = 0; rowCounter < contentRows.size(); rowCounter++) {
				try{
					List<String> contentValues = Arrays.asList(contentRows.get(rowCounter));
					activityName = null;
					task = null;
					actualEffort = 0.0F;
					if (contentValues.contains("Grand Total")) {
						break;
					}
					for (int columnCounter = 0; columnCounter < headerCellValueList.size(); columnCounter++) {
						String cellValue = contentValues.get(columnCounter);
						
						if(dataTemplate.containsKey("resourceName") && dataTemplate.get("resourceName").equalsIgnoreCase(headerCellValueList.get(columnCounter)) && cellValue != null && !cellValue.trim().isEmpty()){
							resourceName = cellValue;
						}else if(dataTemplate.containsKey("moduleName") && dataTemplate.get("moduleName").equalsIgnoreCase(headerCellValueList.get(columnCounter)) && cellValue != null && !cellValue.trim().isEmpty()){
							moduleName = cellValue;
						}else if(dataTemplate.containsKey("activityName") && dataTemplate.get("activityName").equalsIgnoreCase(headerCellValueList.get(columnCounter)) && cellValue != null && !cellValue.trim().isEmpty()){
							activityName = cellValue;
						}else if(dataTemplate.containsKey("activityType") && dataTemplate.get("activityType").equalsIgnoreCase(headerCellValueList.get(columnCounter)) && cellValue != null && !cellValue.trim().isEmpty()){
							task = cellValue;
						}else if(dataTemplate.containsKey("activityEffort") && dataTemplate.get("activityEffort").equalsIgnoreCase(headerCellValueList.get(columnCounter)) && cellValue != null && !cellValue.trim().isEmpty()){
							if(NumberUtils.isNumber(cellValue)){
								actualEffort = Float.parseFloat(cellValue);
							}
						}
					}
					if (activityName != null && !activityName.trim().isEmpty() && task != null && !task.trim().isEmpty()) {
						utilizationCollection = new UtilizationCollection();
						utilizationCollection.setProductName(dataTemplate.get("scheduledProduct"));
						utilizationCollection.setProductId(Integer.parseInt(dataTemplate.get("scheduledProductId")));
						utilizationCollection.setCompetency(dataTemplate.get("scheduledCompetency"));
						utilizationCollection.setCompetencyId(Integer.parseInt(dataTemplate.get("scheduledCompetencyId")));
						utilizationCollection.setProject(dataTemplate.get("scheduledProduct"));
						utilizationCollection.setProjectId(Integer.parseInt(dataTemplate.get("scheduledProductId")));
						utilizationCollection.setCustomerName(dataTemplate.get("scheduledCustomer"));
						utilizationCollection.setCustomerId(Integer.parseInt(dataTemplate.get("scheduledCustomerId")));
						utilizationCollection.setTestFactoryName(dataTemplate.get("scheduledTestFactoryName"));
						utilizationCollection.setTestFactoryId(Integer.parseInt(dataTemplate.get("scheduledTestFactoryId")));
						utilizationCollection.setTestCentersName(dataTemplate.get("scheduledTestCentersName"));
						utilizationCollection.setTestCentersId(Integer.parseInt(dataTemplate.get("scheduledTestCentersId")));
						utilizationCollection.setResourceName(resourceName);
						utilizationCollection.setModuleName(moduleName);
						utilizationCollection.setActivityName(activityName);
						utilizationCollection.setActivityType(task);
						utilizationCollection.setActivityEffort(actualEffort);
						try{
							utilizationCollection.setStartDate(utilizationIndexDataDateFormat.parse(utilizationPeriod.split(" ")[1]));
							utilizationCollection.setEndDate(utilizationIndexDataDateFormat.parse(utilizationPeriod.split(" ")[3]));
						}catch(Exception ex){
							log.error("Date parsing error - ", ex);
						}
						utilizationCollections.add(utilizationCollection);
						numberOfValidRecords++;
					}
				}catch(Exception ex){
					numberOfInvalidRecords++;
					dataExtractorNotification.getMessageList().add(new Date()+" Unable to read data of row number - "+(rowCounter+1)+" due to "+ex.getMessage());
					log.error("Unable to read data of row number - "+(rowCounter+1)+" due to "+ex.getMessage()+"\n", ex);
				}
			}

			dataExtractorNotification.setNumberOfValidRecords(numberOfValidRecords);
			dataExtractorNotification.setNumberOfInvalidRecords(numberOfInvalidRecords);
			dataExtractorNotification.setTotalNumberOfRecords(numberOfValidRecords + numberOfInvalidRecords);
		} catch (Exception ex) {
			dataExtractorNotification.setFileFailed(true);
			dataExtractorNotification.getMessageList().add(new Date()+" Unable to extract data from file - "+dataTemplate.get("fileToRead")+" due to "+ex.getMessage());
			log.error("ERROR  ",ex);
		}
		return utilizationCollections;
	}

}
