package com.hcl.atf.taf.schedule.data.extractor;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hcl.atf.taf.controller.utilities.DateUtility;
import com.hcl.atf.taf.model.ActivityCollection;
import com.hcl.atf.taf.model.DataExtractorNotification;

@Service
public class TestPlanScheduleDataExtractor {

	@Autowired
	CollectionValuesScheduleMapper collectionValuesScheduleMapper;
	
	private static final Log log = LogFactory.getLog(TestPlanScheduleDataExtractor.class);
	
	public List<ActivityCollection> readTestPlanExcel(LinkedHashMap<String, String> dataTemplate, ArrayList<String> headerCellValueList, Sheet sheet, int totalColumnsCount, DataExtractorNotification dataExtractorNotification) {
		List<ActivityCollection> activityCollections = new ArrayList<ActivityCollection>();
		try{
			log.info("Inside readTestPlanExcel()");

			int numberOfValidRecords = 0;
			int numberOfInvalidRecords = 0;
			
			int headerColumnNumber = Integer.parseInt(dataTemplate.get("Header Column Number")) - 1;
			int contentRowNumber = Integer.parseInt(dataTemplate.get("Content Row Number")) - 1;
			int totalRowsCount = sheet.getLastRowNum() + 1;
			
			int dataColumnNumber = 0;
			LinkedHashMap<Long, String> phaseMap = new LinkedHashMap<Long, String>();
			String currentPhase = "";

			ActivityCollection activityCollection = null;
			
			DataFormatter formatter = new DataFormatter();
			Row contentRow;
			ArrayList<ActivityCollection> typeActivityCollections = null;
			ArrayList<Float> previousWeekPlannedCount = new ArrayList<Float>();
			ArrayList<Float> previousWeekActualCount = new ArrayList<Float>();
			for (int rowCounter = contentRowNumber; rowCounter < totalRowsCount; rowCounter++) {
				try{
					contentRow = sheet.getRow(rowCounter);
					if(contentRow != null){
						int index = 0;
						typeActivityCollections = new ArrayList<ActivityCollection>();
						activityCollection = new ActivityCollection();
						for (int columnCounter = 0; columnCounter < headerCellValueList.size(); columnCounter++) {
							dataColumnNumber = columnCounter + headerColumnNumber;
							while(dataColumnNumber >= totalColumnsCount){
								dataColumnNumber = dataColumnNumber - totalColumnsCount + headerColumnNumber;
							}
							Cell contentCell = contentRow.getCell(dataColumnNumber);
							String cellValue = formatter.formatCellValue(contentCell);
							
							if(dataTemplate.containsValue(headerCellValueList.get(columnCounter))){
								if((cellValue != null && !cellValue.trim().isEmpty() && !"NA".equalsIgnoreCase(cellValue)) || (dataTemplate.containsKey("phase") && dataTemplate.get("phase").equalsIgnoreCase(headerCellValueList.get(columnCounter)))){
									if(dataTemplate.containsKey("cumulativeActivityPlanned") && dataTemplate.get("cumulativeActivityPlanned").equalsIgnoreCase(headerCellValueList.get(columnCounter))){
										ActivityCollection typeActivityCollection = new ActivityCollection();
										Float cumulativeActivityPlanned = 0.0f;
										Float cumulativeActivityActual = 0.0F;
										
										if(contentCell.getCellType() == Cell.CELL_TYPE_FORMULA && NumberUtils.isNumber(contentCell.getNumericCellValue()+"")){
											cumulativeActivityPlanned = Float.parseFloat(contentCell.getNumericCellValue()+"");
										}else if(NumberUtils.isNumber(cellValue)){
											cumulativeActivityPlanned = Float.parseFloat(contentCell.getNumericCellValue()+"");
										}
										
										if(contentRow.getCell(dataColumnNumber + 1) != null && contentRow.getCell(dataColumnNumber + 1).getCellType() == Cell.CELL_TYPE_FORMULA && NumberUtils.isNumber(contentRow.getCell(dataColumnNumber + 1).getNumericCellValue()+"")){
											cumulativeActivityActual = Float.parseFloat(contentRow.getCell(dataColumnNumber + 1).getNumericCellValue()+"");
										}else if(contentRow.getCell(dataColumnNumber + 1) != null && NumberUtils.isNumber(formatter.formatCellValue(contentRow.getCell(dataColumnNumber + 1)))){
											cumulativeActivityActual = Float.parseFloat(contentRow.getCell(dataColumnNumber + 1).getNumericCellValue()+"");
										}
										
										if(typeActivityCollections != null){
											Float currentWeekActivityPlanned = 0.0F;
											Float currentWeekActivityActual = 0.0F;
											
											if(index < previousWeekPlannedCount.size() && previousWeekPlannedCount.get(index) != null){
												currentWeekActivityPlanned = (cumulativeActivityPlanned - previousWeekPlannedCount.get(index));
											}else{
												currentWeekActivityPlanned = cumulativeActivityPlanned;
											}
											
											if(index < previousWeekActualCount.size() && previousWeekActualCount.get(index) != null){
												currentWeekActivityActual = (cumulativeActivityActual - previousWeekActualCount.get(index));
											}else{
												currentWeekActivityActual = cumulativeActivityActual;
											}
											 
											if(currentWeekActivityPlanned < 0){
												currentWeekActivityPlanned = 0.0F;
											}
											
											if(currentWeekActivityActual < 0){
												currentWeekActivityActual = 0.0F;
											}
											
											typeActivityCollection.setActivitySizePlanned(currentWeekActivityPlanned);
											typeActivityCollection.setActivitySizeActual(currentWeekActivityActual);
											
											typeActivityCollection.setCumulativeActivityPlanned(cumulativeActivityPlanned);
											typeActivityCollection.setCumulativeActivityActual(cumulativeActivityActual);
											
											if(index < previousWeekPlannedCount.size() && previousWeekPlannedCount.get(index) != null){
												previousWeekPlannedCount.set(index, cumulativeActivityPlanned);
											}else{
												previousWeekPlannedCount.add(cumulativeActivityPlanned);
											}
											
											if(index < previousWeekActualCount.size() && previousWeekActualCount.get(index) != null){
												previousWeekActualCount.set(index, cumulativeActivityActual);
											}else{
												previousWeekActualCount.add(cumulativeActivityActual);
											}
											
											String activityType = headerCellValueList.get(dataColumnNumber);
											if(activityType != null && !activityType.trim().isEmpty()){
												typeActivityCollection.setActivityType(headerCellValueList.get(dataColumnNumber));
												if(activityType.toLowerCase().contains("test case")){
													typeActivityCollection.setType("Test case");
												}else if(activityType.toLowerCase().contains("pairwise") && activityType.toLowerCase().contains("test job")){
													typeActivityCollection.setType("Test job / Pairwise");
												}else if(activityType.toLowerCase().contains("pairwise")){
													typeActivityCollection.setType("Pairwise");
												}else if(activityType.toLowerCase().contains("test job")){
													typeActivityCollection.setType("Test job");
												}
											}
											
											typeActivityCollections.add(typeActivityCollection);
										}
										index++;
									}else if(dataTemplate.containsKey("phase") && dataTemplate.get("phase").equalsIgnoreCase(headerCellValueList.get(columnCounter))){
										Long date = null;
										if(cellValue != null && !cellValue.trim().isEmpty() && !"NA".equalsIgnoreCase(cellValue)){
											currentPhase = cellValue.trim().replaceAll("[\n\r]", " ");
										}
										if(headerCellValueList.indexOf(dataTemplate.get("weekDate")) >= 0 && headerCellValueList.indexOf(dataTemplate.get("weekDate")) < totalColumnsCount){
											if(contentRow.getCell(headerCellValueList.indexOf(dataTemplate.get("weekDate"))) != null && contentRow.getCell(headerCellValueList.indexOf(dataTemplate.get("weekDate"))).getDateCellValue() != null){
												date = contentRow.getCell(headerCellValueList.indexOf(dataTemplate.get("weekDate"))).getDateCellValue().getTime();
											}
										}
										
										contentRow = sheet.getRow(rowCounter+1);
										if(contentRow == null){
											phaseMap.put(date, currentPhase);
										}else{
											contentCell = contentRow.getCell(dataColumnNumber);
											cellValue = formatter.formatCellValue(contentCell);
											if(cellValue != null && !cellValue.trim().isEmpty() && !"NA".equalsIgnoreCase(cellValue) && currentPhase != null && !currentPhase.trim().isEmpty() && !"NA".equalsIgnoreCase(currentPhase) && !currentPhase.equalsIgnoreCase(cellValue)){
												phaseMap.put(date, currentPhase);
											}
										}
										contentRow = sheet.getRow(rowCounter);
									}else{
										collectionValuesScheduleMapper.setActivityCollectionValues(activityCollection, dataTemplate, headerCellValueList.get(columnCounter), cellValue, contentCell);									
									}
								}
							}
						}
						
						for(ActivityCollection activityCollectionLoop : typeActivityCollections){
							if(activityCollection.getWeekDate() != null && !activityCollection.getWeekDate().toString().isEmpty() && activityCollectionLoop != null){
								activityCollectionLoop.setWeekDate(activityCollection.getWeekDate());
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
								activityCollections.add(activityCollectionLoop);
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
			
			for(ActivityCollection activityCollectionLoop : activityCollections){
				for (Map.Entry<Long, String> entry : phaseMap.entrySet()) {
					
					if(entry.getKey() != null && activityCollectionLoop.getWeekDate().getTime() <= entry.getKey()){
						activityCollectionLoop.setPhase(entry.getValue());
						break;
					}else if(entry.getKey() == null){
						activityCollectionLoop.setPhase(entry.getValue());
						break;
					}
				}
				Calendar weekDate = Calendar.getInstance();
				weekDate.setTime(DateUtility.getWeekStart(activityCollectionLoop.getWeekDate()));
				weekDate.set(Calendar.HOUR_OF_DAY, 0);
				weekDate.set(Calendar.MINUTE, 0);
				weekDate.set(Calendar.SECOND, 0);
				weekDate.set(Calendar.MILLISECOND, 0);
				weekDate.add(Calendar.MILLISECOND, weekDate.getTimeZone().getRawOffset());
				
				activityCollectionLoop.setWeekDate(weekDate.getTime());
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
