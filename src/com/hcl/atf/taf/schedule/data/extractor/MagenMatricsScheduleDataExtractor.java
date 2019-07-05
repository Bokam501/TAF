package com.hcl.atf.taf.schedule.data.extractor;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
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
import com.opencsv.CSVReader;

@Service
public class MagenMatricsScheduleDataExtractor {

	@Autowired
	CollectionValuesScheduleMapper collectionValuesScheduleMapper;
	
	private static final Log log = LogFactory.getLog(MagenMatricsScheduleDataExtractor.class);
	
	public List<ActivityCollection> readMagenMatrixExcel(LinkedHashMap<String, String> dataTemplate, ArrayList<String> headerCellValueList, Sheet sheet, int totalColumnsCount, DataExtractorNotification dataExtractorNotification) {
		List<ActivityCollection> activityCollections = new ArrayList<ActivityCollection>();
		try{
			log.info("Inside readMagenMatrixExcel()");

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
						
						Boolean isRequiredRow = true;
						
						dataColumnNumber = headerCellValueList.indexOf(dataTemplate.get("actualActivityStartDate")) + headerColumnNumber;
						while(dataColumnNumber >= totalColumnsCount){
							dataColumnNumber = dataColumnNumber - totalColumnsCount + headerColumnNumber;
						}
						
						String weekDate = formatter.formatCellValue(contentRow.getCell(dataColumnNumber));
						if(weekDate == null || weekDate.trim().isEmpty()){
							isRequiredRow = false;
						}
						
						if(isRequiredRow){
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
							
							if(activityCollection.getActualActivityStartDate() != null && !activityCollection.getActualActivityStartDate().toString().isEmpty()){
								
								Calendar firstDayWeekCalendar = Calendar.getInstance();
								firstDayWeekCalendar.setTime(activityCollection.getActualActivityStartDate());
								firstDayWeekCalendar.set(Calendar.HOUR_OF_DAY, 0);
								firstDayWeekCalendar.set(Calendar.MINUTE, 0);
								firstDayWeekCalendar.set(Calendar.SECOND, 0);
								firstDayWeekCalendar.setFirstDayOfWeek(Calendar.MONDAY);
								firstDayWeekCalendar.add(Calendar.DAY_OF_WEEK, firstDayWeekCalendar.getFirstDayOfWeek() - firstDayWeekCalendar.get(Calendar.DAY_OF_WEEK));
								
								if(dateBasedActivityCollection.containsKey(firstDayWeekCalendar.getTime().toString())){
								
									ActivityCollection activityCollectionWeek = dateBasedActivityCollection.get(firstDayWeekCalendar.getTime().toString());
									activityCollectionWeek.setActivitySizeActual(activityCollectionWeek.getActivitySizeActual() + activityCollection.getActivitySizeActual());
									dateBasedActivityCollection.put(firstDayWeekCalendar.getTime().toString(), activityCollectionWeek);
								
								}else{
									
									ActivityCollection activityCollectionWeek = activityCollection;
									activityCollectionWeek.setActualActivityStartDate(firstDayWeekCalendar.getTime());
									dateBasedActivityCollection.put(firstDayWeekCalendar.getTime().toString(), activityCollectionWeek);
								}
								numberOfValidRecords++;
							}
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
	
	public List<ActivityCollection> readMagenMatrixCSV(LinkedHashMap<String, String> dataTemplate, DataExtractorNotification dataExtractorNotification) {
		List<ActivityCollection> activityCollections = new ArrayList<ActivityCollection>();
		CSVReader csvReader = null;
		try{
			log.info("Inside readMagenMatrixExcel()");
			
			csvReader = new CSVReader(new FileReader(dataTemplate.get("File Location")));
			List<String[]> contents = csvReader.readAll();
			boolean isFirstRow = true;
			List<String> headerCellValueList = null;
			List<String[]> contentRows = new ArrayList<String[]>();
			for (String[] content : contents) {
				if(isFirstRow){
					headerCellValueList = Arrays.asList(content);
					isFirstRow = false;
				}else{
					contentRows.add((String[]) content);
				}
			}
			
			ActivityCollection activityCollection = null;
			if (headerCellValueList != null && headerCellValueList.size() > 0) {
				for (int rowCounter = 0; rowCounter < contentRows.size(); rowCounter++) {
					try{
						activityCollection = new ActivityCollection();		
						String[] contentValues = contentRows.get(rowCounter);
						for (int columnCounter = 0; columnCounter < contentValues.length; columnCounter++) {
							String value = contentValues[columnCounter];
							if(dataTemplate.containsValue(headerCellValueList.get(columnCounter))){
								if(value != null && !value.trim().isEmpty() && !"NA".equalsIgnoreCase(value)){
									collectionValuesScheduleMapper.setActivityCollectionValues(activityCollection, dataTemplate, headerCellValueList.get(columnCounter), value, null);
								}
							}
						}
						if(activityCollection.getActivityName() != null && !activityCollection.getActivityName().isEmpty()){
							activityCollections.add(activityCollection);
						}
					}catch(Exception ex){
						dataExtractorNotification.getMessageList().add(new Date()+" Unable to read data of row number - "+(rowCounter+2)+" due to "+ex.getMessage());
						log.error("Unable to read data of row number - "+(rowCounter+2)+" due to "+ex.getMessage()+"\n", ex);
					}
				}
			}
			
		}catch(Exception ex){
			dataExtractorNotification.setFileFailed(true);
			dataExtractorNotification.getMessageList().add(new Date()+" Unable to extract data from file - "+dataTemplate.get("fileToRead")+" due to "+ex.getMessage());
			log.error("EXTRACTION ERROR", ex);
		}finally{
			if(csvReader != null){
				try {
					csvReader.close();
				} catch (Exception ex) {
					log.error("Stream closing error - ", ex);
				}
			}
		}
		return activityCollections;
	}
	
}
