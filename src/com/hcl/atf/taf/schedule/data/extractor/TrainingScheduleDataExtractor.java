package com.hcl.atf.taf.schedule.data.extractor;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
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

import com.hcl.atf.taf.model.DataExtractorNotification;

@Service
public class TrainingScheduleDataExtractor {

	@Autowired
	CollectionValuesScheduleMapper collectionValuesScheduleMapper;
	
	private static final Log log = LogFactory.getLog(TrainingScheduleDataExtractor.class);
	
	public List<HashMap<String, Object>> readResourceTrainingExcel(LinkedHashMap<String, String> dataTemplate, ArrayList<String> headerCellValueList, Sheet sheet, int totalColumnsCount, DataExtractorNotification dataExtractorNotification) {
		List<HashMap<String, Object>> resourceTrainings = new ArrayList<HashMap<String, Object>>();
		try{
			log.info("Inside readResourceTrainingExcel()");

			int numberOfValidRecords = 0;
			int numberOfInvalidRecords = 0;
			
			int headerColumnNumber = Integer.parseInt(dataTemplate.get("Header Column Number")) - 1;
			int contentRowNumber = Integer.parseInt(dataTemplate.get("Content Row Number")) - 1;
			int totalRowsCount = sheet.getLastRowNum() + 1;

			HashMap<String, Object> resourceTraining = null;
			
			DataFormatter formatter = new DataFormatter();
			Row contentRow;
			int dataColumnNumber = 0;
			for (int rowCounter = contentRowNumber; rowCounter < totalRowsCount; rowCounter++) {
				try{
					contentRow = sheet.getRow(rowCounter);
					if(contentRow != null){
						resourceTraining = new HashMap<String, Object>();
						for (int columnCounter = 0; columnCounter < headerCellValueList.size(); columnCounter++) {
							dataColumnNumber = columnCounter + headerColumnNumber;
							while(dataColumnNumber >= totalColumnsCount){
								dataColumnNumber = dataColumnNumber - totalColumnsCount + headerColumnNumber;
							}
							Cell contentCell = contentRow.getCell(dataColumnNumber);
							String cellValue = formatter.formatCellValue(contentCell);
							
							if(dataTemplate.containsValue(headerCellValueList.get(columnCounter))){
								if(cellValue != null && !cellValue.trim().isEmpty() && !"NA".equalsIgnoreCase(cellValue) && !"N/A".equalsIgnoreCase(cellValue)){
									String headerValue = headerCellValueList.get(columnCounter).replace(".", " ");
									if(dataTemplate.containsKey("id") && dataTemplate.get("id").equalsIgnoreCase(headerCellValueList.get(columnCounter))){
										resourceTraining.put("_id", cellValue);
									}
									if(contentCell.getCellType() == Cell.CELL_TYPE_FORMULA){
										resourceTraining.put(headerValue, contentCell.getNumericCellValue());
									}else{
										if(NumberUtils.isNumber(cellValue)){
											resourceTraining.put(headerValue, Integer.parseInt(cellValue));
										}else{
											resourceTraining.put(headerValue, cellValue);
										}
									}
								}
							}
						}
						
						if(resourceTraining.size() > 0 && resourceTraining.containsKey("_id")){
							
							resourceTraining.put("productName", dataTemplate.get("scheduledProduct"));
							resourceTraining.put("productId", Integer.parseInt(dataTemplate.get("scheduledProductId")));
							resourceTraining.put("Competency", dataTemplate.get("scheduledCompetency"));
							resourceTraining.put("competencyId", Integer.parseInt(dataTemplate.get("scheduledCompetencyId")));
							resourceTraining.put("project", dataTemplate.get("scheduledProduct"));
							resourceTraining.put("projectId", Integer.parseInt(dataTemplate.get("scheduledProductId")));
							resourceTraining.put("customer", dataTemplate.get("scheduledCustomer"));
							resourceTraining.put("customerId", Integer.parseInt(dataTemplate.get("scheduledCustomerId")));
							resourceTraining.put("testFactoryName", dataTemplate.get("scheduledTestFactoryName"));
							resourceTraining.put("testFactoryId", Integer.parseInt(dataTemplate.get("scheduledTestFactoryId")));
							resourceTraining.put("testCentersName", dataTemplate.get("scheduledTestCentersName"));
							resourceTraining.put("testCentersId", Integer.parseInt(dataTemplate.get("scheduledTestCentersId")));
							
							resourceTrainings.add(resourceTraining);
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
		return resourceTrainings;
	}

	public List<HashMap<String, Object>> readTrainingTypeExcel(LinkedHashMap<String, String> dataTemplate, ArrayList<String> headerCellValueList, Sheet sheet, int totalColumnsCount, DataExtractorNotification dataExtractorNotification) {
		List<HashMap<String, Object>> trainingTypes = new ArrayList<HashMap<String, Object>>();
		try{
			log.info("Inside readTrainingTypeExcel()");

			int numberOfValidRecords = 0;
			int numberOfInvalidRecords = 0;
			
			int headerColumnNumber = Integer.parseInt(dataTemplate.get("Header Column Number")) - 1;
			int contentRowNumber = Integer.parseInt(dataTemplate.get("Content Row Number")) - 1;

			HashMap<String, Object> trainingType = null;
			
			DataFormatter formatter = new DataFormatter();
			Row contentRow;
			
			for (int columnCounter = headerColumnNumber; columnCounter < totalColumnsCount; columnCounter++) {
				try{
					String headerValue = headerCellValueList.get((columnCounter - headerColumnNumber));
					if(headerValue != null && !headerValue.isEmpty()){
						trainingType = new HashMap<String, Object>();
						trainingType.put("Type", headerValue);
						
						contentRow = sheet.getRow(contentRowNumber);
						String cellValue = "";
						if(contentRow != null){
							Cell contentCell = contentRow.getCell(columnCounter);
							cellValue = formatter.formatCellValue(contentCell);
							
							if(contentCell.getCellType() == Cell.CELL_TYPE_FORMULA){
								trainingType.put("Not Completed", contentCell.getNumericCellValue());
							}else{
								trainingType.put("Not Completed", cellValue);
							}
						}
						
						contentRow = sheet.getRow(contentRowNumber - 1);
						cellValue = "";
						if(contentRow != null){
							Cell contentCell = contentRow.getCell(columnCounter);
							cellValue = formatter.formatCellValue(contentCell);
							
							if(contentCell.getCellType() == Cell.CELL_TYPE_FORMULA){
								trainingType.put("Completed", contentCell.getNumericCellValue());
							}else{
								trainingType.put("Completed", cellValue);
							}
						}
					}
					
					if(trainingType != null && trainingType.size() > 0 && trainingType.containsKey("Type")){
						
						trainingType.put("productName", dataTemplate.get("scheduledProduct"));
						trainingType.put("productId", Integer.parseInt(dataTemplate.get("scheduledProductId")));
						trainingType.put("Competency", dataTemplate.get("scheduledCompetency"));
						trainingType.put("competencyId", Integer.parseInt(dataTemplate.get("scheduledCompetencyId")));
						trainingType.put("project", dataTemplate.get("scheduledProduct"));
						trainingType.put("projectId", Integer.parseInt(dataTemplate.get("scheduledProductId")));
						trainingType.put("customer", dataTemplate.get("scheduledCustomer"));
						trainingType.put("customerId", Integer.parseInt(dataTemplate.get("scheduledCustomerId")));
						trainingType.put("testFactoryName", dataTemplate.get("scheduledTestFactoryName"));
						trainingType.put("testFactoryId", Integer.parseInt(dataTemplate.get("scheduledTestFactoryId")));
						trainingType.put("testCentersName", dataTemplate.get("scheduledTestCentersName"));
						trainingType.put("testCentersId", Integer.parseInt(dataTemplate.get("scheduledTestCentersId")));
						
						trainingTypes.add(trainingType);
						numberOfValidRecords++;
					}
				}catch(Exception ex){
					numberOfInvalidRecords++;
					dataExtractorNotification.getMessageList().add(new Date()+" Unable to read data of column number - "+(columnCounter+1)+" due to "+ex.getMessage());
					log.error("Unable to read data of column number - "+(columnCounter+1)+" due to "+ex.getMessage()+"\n", ex);
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
		return trainingTypes;
	}
}
