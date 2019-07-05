package com.hcl.atf.taf.schedule.data.extractor;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hcl.atf.taf.model.ActivityCollection;
import com.hcl.atf.taf.model.DataExtractorNotification;

@Service
public class PairWiseScheduleDataExtractor {

	@Autowired
	CollectionValuesScheduleMapper collectionValuesScheduleMapper;
	
	private static final Log log = LogFactory.getLog(PairWiseScheduleDataExtractor.class);
	
	public List<ActivityCollection> readPairWiseExcel(LinkedHashMap<String, String> dataTemplate, ArrayList<String> headerCellValueList, Sheet sheet, int totalColumnsCount, DataExtractorNotification dataExtractorNotification) {
		List<ActivityCollection> activityCollections = new ArrayList<ActivityCollection>();
		try{
			log.info("Inside readPairWiseExcel()");

			int numberOfValidRecords = 0;
			int numberOfInvalidRecords = 0;
			
			int headerColumnNumber = Integer.parseInt(dataTemplate.get("Header Column Number")) - 1;
			int contentRowNumber = Integer.parseInt(dataTemplate.get("Content Row Number")) - 1;
			int totalRowsCount = sheet.getLastRowNum() + 1;
			
			ActivityCollection activityCollection = null;
			HashMap<String, ActivityCollection> dateBasedActivityCollection = new HashMap<String, ActivityCollection>();
			
			DataFormatter formatter = new DataFormatter();
			Row contentRow;
			int dataColumnNumber = 0;
			for (int rowCounter = contentRowNumber; rowCounter < totalRowsCount; rowCounter++) {
				try{
					contentRow = sheet.getRow(rowCounter);
					if(contentRow != null){
						activityCollection = new ActivityCollection();	
						activityCollection.setProductName(dataTemplate.get("scheduledProduct"));
						activityCollection.setProductId(Integer.parseInt(dataTemplate.get("scheduledProductId")));
						activityCollection.setCompetency(dataTemplate.get("scheduledCompetency"));
						activityCollection.setCompetencyId(Integer.parseInt(dataTemplate.get("scheduledCompetencyId")));
						activityCollection.setProject(dataTemplate.get("scheduledProduct"));
						activityCollection.setProjectId(Integer.parseInt(dataTemplate.get("scheduledProductId")));
						activityCollection.setCustomerName(dataTemplate.get("scheduledCustomer"));
						activityCollection.setCustomerId(Integer.parseInt(dataTemplate.get("scheduledCustomerId")));
						activityCollection.setTestFactoryName(dataTemplate.get("scheduledTestFactoryName"));
						activityCollection.setTestFactoryId(Integer.parseInt(dataTemplate.get("scheduledTestFactoryId")));
						activityCollection.setTestCentersName(dataTemplate.get("scheduledTestCentersName"));
						activityCollection.setTestCentersId(Integer.parseInt(dataTemplate.get("scheduledTestCentersId")));
						
						for (int columnCounter = 0; columnCounter < headerCellValueList.size(); columnCounter++) {
							dataColumnNumber = columnCounter + headerColumnNumber;
							while(dataColumnNumber >= totalColumnsCount){
								dataColumnNumber = dataColumnNumber - totalColumnsCount + headerColumnNumber;
							}
							Cell contentCell = contentRow.getCell(dataColumnNumber);
							String cellValue = formatter.formatCellValue(contentCell);
							
							if(dataTemplate.containsValue(headerCellValueList.get(columnCounter))){
								if(cellValue != null && !cellValue.trim().isEmpty() && !"NA".equalsIgnoreCase(cellValue)){
									collectionValuesScheduleMapper.setActivityCollectionValues(activityCollection, dataTemplate, headerCellValueList.get(columnCounter), cellValue, contentCell);
								}
							}
						}
						if(activityCollection.getActivityStatus() != null && !activityCollection.getActivityStatus().isEmpty() && activityCollection.getActualActivityStartDate() != null){
							
							Calendar firstDayWeekCalendar = Calendar.getInstance();
							firstDayWeekCalendar.setTime(activityCollection.getActualActivityStartDate());
							firstDayWeekCalendar.set(Calendar.HOUR_OF_DAY, 0);
							firstDayWeekCalendar.set(Calendar.MINUTE, 0);
							firstDayWeekCalendar.set(Calendar.SECOND, 0);
							firstDayWeekCalendar.setFirstDayOfWeek(Calendar.MONDAY);
							firstDayWeekCalendar.add(Calendar.DAY_OF_WEEK, firstDayWeekCalendar.getFirstDayOfWeek() - firstDayWeekCalendar.get(Calendar.DAY_OF_WEEK));
							
							if(dateBasedActivityCollection.containsKey(firstDayWeekCalendar.getTime().toString())){
							
								ActivityCollection activityCollectionWeek = dateBasedActivityCollection.get(firstDayWeekCalendar.getTime().toString());
								activityCollectionWeek.setActivitySizeActual(activityCollectionWeek.getActivitySizeActual() + 1);
								dateBasedActivityCollection.put(firstDayWeekCalendar.getTime().toString(), activityCollectionWeek);
							
							}else{
								
								ActivityCollection activityCollectionWeek = activityCollection;
								activityCollection.setActivitySizeActual(1.0f);
								activityCollectionWeek.setActualActivityStartDate(firstDayWeekCalendar.getTime());
								dateBasedActivityCollection.put(firstDayWeekCalendar.getTime().toString(), activityCollectionWeek);
							}
							numberOfValidRecords++;
						}
					}
				}catch(Exception ex){
					numberOfInvalidRecords++;
					dataExtractorNotification.getMessageList().add(new Date()+" Unable to read data of row number - "+(rowCounter+1)+" due to "+ex.getMessage());
					log.error("Unable to read data of row number - "+(rowCounter+1)+" due to "+ex.getMessage()+"\n", ex);
				}
			}
			
			if(dateBasedActivityCollection != null && !dateBasedActivityCollection.isEmpty() && dateBasedActivityCollection.size() > 0){
				activityCollections.addAll(dateBasedActivityCollection.values());
			}
			
			dataExtractorNotification.setNumberOfValidRecords(numberOfValidRecords);
			dataExtractorNotification.setNumberOfInvalidRecords(numberOfInvalidRecords);
			dataExtractorNotification.setTotalNumberOfRecords(numberOfValidRecords + numberOfInvalidRecords);
		}catch(Exception ex){
			dataExtractorNotification.setFileFailed(true);
			dataExtractorNotification.getMessageList().add(new Date()+" Unable to extract data from file - "+dataTemplate.get("fileToRead")+" due to "+ex.getMessage());
			log.error("EXTRACTION ERROR", ex);
		}
		return activityCollections;
	}
}
