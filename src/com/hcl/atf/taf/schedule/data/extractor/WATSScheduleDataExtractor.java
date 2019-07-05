package com.hcl.atf.taf.schedule.data.extractor;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
public class WATSScheduleDataExtractor {

	@Autowired
	CollectionValuesScheduleMapper collectionValuesScheduleMapper;
	
	private static final Log log = LogFactory.getLog(WATSScheduleDataExtractor.class);
	
	public List<ActivityCollection> readWATSExcel(LinkedHashMap<String, String> dataTemplate, ArrayList<String> headerCellValueList, Sheet sheet, int totalColumnsCount, DataExtractorNotification dataExtractorNotification) {
		List<ActivityCollection> activityCollections = new ArrayList<ActivityCollection>();
		try{
			log.info("Inside readWATSExcel()");

			int numberOfValidRecords = 0;
			int numberOfInvalidRecords = 0;
			
			int headerColumnNumber = Integer.parseInt(dataTemplate.get("Header Column Number")) - 1;
			int contentRowNumber = Integer.parseInt(dataTemplate.get("Content Row Number")) - 1;
			int totalRowsCount = sheet.getLastRowNum() + 1;
			
			ActivityCollection activityCollection = null;
			
			DataFormatter formatter = new DataFormatter();
			Row contentRow;
			int dataColumnNumber = 0;
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MMM-yyyy");
			
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
								if(cellValue != null && !cellValue.trim().isEmpty() && !"NA".equalsIgnoreCase(cellValue) && !"N/A".equalsIgnoreCase(cellValue)){
									if(dataTemplate.containsKey("activityName") && dataTemplate.get("activityName").equalsIgnoreCase(headerCellValueList.get(columnCounter))){
										cellValue = cellValue.trim().replaceAll(" ", "_");
										activityCollection.setActivityName(cellValue);
									}else if(dataTemplate.containsKey("weekDate") && dataTemplate.get("weekDate").equalsIgnoreCase(headerCellValueList.get(columnCounter))){
										if(contentCell.getCellType() == Cell.CELL_TYPE_STRING){
											try{
												activityCollection.setWeekDate(simpleDateFormat.parse(cellValue.split("[\n\r]")[0]));											
											}catch(Exception ex){
												log.error("Date Parsing error", ex);
											}
										}else{
											activityCollection.setWeekDate(contentCell.getDateCellValue());
										}
										
									}else{
										collectionValuesScheduleMapper.setActivityCollectionValues(activityCollection, dataTemplate, headerCellValueList.get(columnCounter), cellValue, contentCell);									
									}
								}
							}
						}
						
						if(activityCollection.getProductName() != null && !activityCollection.getProductName().isEmpty() && activityCollection.getActivityName() != null && !activityCollection.getActivityName().isEmpty()){
							Float weightageUnit = 1.0F;
							Float activitySizeActual = activityCollection.getActivitySizeActual();
							if(activityCollection.getActivityType() != null && "Gap Analysis".equalsIgnoreCase(activityCollection.getActivityType().trim())){
								weightageUnit = 2.0F;
							}else if(activityCollection.getActivityType() != null && "Visual Rationale".equalsIgnoreCase(activityCollection.getActivityType().trim())){
								weightageUnit = 1.0F;
							}else if(activityCollection.getActivityType() != null && "Special Rationale".equalsIgnoreCase(activityCollection.getActivityType().trim())){
								weightageUnit = 3.0F;
							}else if(activityCollection.getActivityType() != null && "Equivalency Report".equalsIgnoreCase(activityCollection.getActivityType().trim())){
								weightageUnit = 4.0F;
							}else if(activityCollection.getActivityType() != null && ("Protocol".equalsIgnoreCase(activityCollection.getActivityType().trim()) || "Final report".equalsIgnoreCase(activityCollection.getActivityType().trim()) || "Protocol/ Final report".equalsIgnoreCase(activityCollection.getActivityType().trim()))){
								weightageUnit = 5.0F;
							}
							activityCollection.setWeightageUnit(weightageUnit);
							activityCollection.setWorkUnitActual(activitySizeActual * weightageUnit); 
							if(activityCollection.getRevisedActivityStartDate() != null){
								activityCollection.setWeekDate(activityCollection.getRevisedActivityStartDate());
							}else{
								activityCollection.setWeekDate(activityCollection.getPlannedActivityStartDate());
							}
							activityCollections.add(activityCollection);
							numberOfValidRecords++;
						}
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
		}catch(Exception ex){
			dataExtractorNotification.setFileFailed(true);
			dataExtractorNotification.getMessageList().add(new Date()+" Unable to extract data from file - "+dataTemplate.get("fileToRead")+" due to "+ex.getMessage());
			log.error("EXTRACTION ERROR", ex);
		}
		return activityCollections;
	}
}
