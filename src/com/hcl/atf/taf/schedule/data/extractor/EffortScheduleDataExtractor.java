package com.hcl.atf.taf.schedule.data.extractor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;

import org.apache.commons.lang.math.NumberUtils;
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
public class EffortScheduleDataExtractor {

	@Autowired
	CollectionValuesScheduleMapper collectionValuesScheduleMapper;
	
	private static final Log log = LogFactory.getLog(EffortScheduleDataExtractor.class);
	
	public List<ActivityCollection> readPMSmartEffortsExcel(LinkedHashMap<String, String> dataTemplate, ArrayList<String> headerCellValueList, Sheet sheet, int totalColumnsCount, DataExtractorNotification dataExtractorNotification) {
		List<ActivityCollection> activityCollections = new ArrayList<ActivityCollection>();
		try{
			log.info("Inside readPMSmartEffortsExcel()");

			int numberOfValidRecords = 0;
			int numberOfInvalidRecords = 0;
			
			int headerColumnNumber = Integer.parseInt(dataTemplate.get("Header Column Number")) - 1;
			int contentRowNumber = Integer.parseInt(dataTemplate.get("Content Row Number")) - 1;
			int totalRowsCount = sheet.getLastRowNum() + 1;
			
			ActivityCollection activityCollection = null;
			
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
								if(cellValue != null && !cellValue.trim().isEmpty() && !"NA".equalsIgnoreCase(cellValue) && !"N/A".equalsIgnoreCase(cellValue)){
									if(dataTemplate.containsKey("activityName") && dataTemplate.get("activityName").equalsIgnoreCase(headerCellValueList.get(columnCounter))){
										cellValue = cellValue.trim().replaceAll(" ", "_");
										activityCollection.setActivityName(cellValue);
									}else if(dataTemplate.containsKey("actualActivityEffort") && dataTemplate.get("actualActivityEffort").equalsIgnoreCase(headerCellValueList.get(columnCounter))){
										String[] activityEffortDetails = cellValue.split(",");
										Float authorEffort = 0.0F;
										Float reviewEffort = 0.0F;
										int reviewerNumber = 1;
										for(String activityEffortDetail : activityEffortDetails){
											String[] activityEffort = activityEffortDetail.split("="); 
											if(activityEffort != null && activityEffort.length == 2){
												String effortValue = activityEffort[1].trim();
												if(activityEffort[0].trim().toLowerCase().contains("review")){
													if(NumberUtils.isNumber(effortValue)){
														reviewEffort += Float.parseFloat(effortValue);
														if(reviewerNumber == 1){
															activityCollection.setActivityReviewEffort1(Float.parseFloat(effortValue));
														}else if(reviewerNumber == 2){
															activityCollection.setActivityReviewEffort2(Float.parseFloat(effortValue));
														}else if(reviewerNumber == 3){
															activityCollection.setActivityReviewEffort3(Float.parseFloat(effortValue));
														}else if(reviewerNumber == 4){
															activityCollection.setActivityReviewEffort4(Float.parseFloat(effortValue));
														}else if(reviewerNumber == 5){
															activityCollection.setActivityReviewEffort5(Float.parseFloat(effortValue));
														}
													}
													reviewerNumber++;
												}else{
													if(NumberUtils.isNumber(effortValue)){
														authorEffort += Float.parseFloat(effortValue);
													}
												}
											}
										}
										activityCollection.setActivityExecutionEffort(authorEffort);
										
									}else{
										collectionValuesScheduleMapper.setActivityCollectionValues(activityCollection, dataTemplate, headerCellValueList.get(columnCounter), cellValue, contentCell);									
									}
								}
							}
						}
						
						if(activityCollection.getActivityName() != null && !activityCollection.getActivityName().isEmpty()){
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
	
	
	public List<ActivityCollection> readPMSmartEffortsCSV(LinkedHashMap<String, String> dataTemplate, List<String> headerCellValueList, List<String[]> contentRows, DataExtractorNotification dataExtractorNotification) {
		List<ActivityCollection> activityCollections = new ArrayList<ActivityCollection>();
		try{
			log.info("Inside readPMSmartEffortsCSV()");

			int numberOfValidRecords = 0;
			int numberOfInvalidRecords = 0;
			
			int totalRowsCount = contentRows.size();
			int headerColumnCounter = headerCellValueList.size();
			
			ActivityCollection activityCollection = null;
			for (int rowCounter = 0; rowCounter < totalRowsCount; rowCounter++) {
				try{
					List<String> contentValues = Arrays.asList(contentRows.get(rowCounter));
					if(contentValues != null && contentValues.size() == headerColumnCounter){
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
						for (int columnCounter = 0; columnCounter < headerColumnCounter; columnCounter++) {
							String cellValue = contentValues.get(columnCounter);
							if(dataTemplate.containsValue(headerCellValueList.get(columnCounter))){
								if(cellValue != null && !cellValue.trim().isEmpty() && !"NA".equalsIgnoreCase(cellValue) && !"N/A".equalsIgnoreCase(cellValue)){
									if(dataTemplate.containsKey("activityName") && dataTemplate.get("activityName").equalsIgnoreCase(headerCellValueList.get(columnCounter))){
										cellValue = cellValue.trim().replaceAll(" ", "_");
										activityCollection.setActivityName(cellValue);
									}else if(dataTemplate.containsKey("actualActivityEffort") && dataTemplate.get("actualActivityEffort").equalsIgnoreCase(headerCellValueList.get(columnCounter))){
										String[] activityEffortDetails = cellValue.split(",");
										Float authorEffort = 0.0F;
										Float reviewEffort = 0.0F;
										int reviewerNumber = 1;
										for(String activityEffortDetail : activityEffortDetails){
											String[] activityEffort = activityEffortDetail.split("="); 
											if(activityEffort != null && activityEffort.length == 2){
												String effortValue = activityEffort[1].trim();
												if(activityEffort[0].trim().toLowerCase().contains("review")){
													if(NumberUtils.isNumber(effortValue)){
														reviewEffort += Float.parseFloat(effortValue);
														if(reviewerNumber == 1){
															activityCollection.setActivityReviewEffort1(Float.parseFloat(effortValue));
														}else if(reviewerNumber == 2){
															activityCollection.setActivityReviewEffort2(Float.parseFloat(effortValue));
														}else if(reviewerNumber == 3){
															activityCollection.setActivityReviewEffort3(Float.parseFloat(effortValue));
														}else if(reviewerNumber == 4){
															activityCollection.setActivityReviewEffort4(Float.parseFloat(effortValue));
														}else if(reviewerNumber == 5){
															activityCollection.setActivityReviewEffort5(Float.parseFloat(effortValue));
														}
													}
													reviewerNumber++;
												}else{
													if(NumberUtils.isNumber(effortValue)){
														authorEffort += Float.parseFloat(effortValue);
													}
												}
											}
										}
										activityCollection.setActivityExecutionEffort(authorEffort);
										
									}
								}
							}
						}
						
						if(activityCollection.getActivityName() != null && !activityCollection.getActivityName().isEmpty()){
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
