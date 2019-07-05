package com.hcl.atf.taf.schedule.data.extractor;

import java.util.ArrayList;
import java.util.Calendar;
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
public class SingleValueMeticScheduleDataExtractor {

	@Autowired
	CollectionValuesScheduleMapper collectionValuesScheduleMapper;
	
	private static final Log log = LogFactory.getLog(SingleValueMeticScheduleDataExtractor.class);
	
	public List<HashMap<String, Object>> readSingleValueMeticExcel(LinkedHashMap<String, String> dataTemplate, ArrayList<String> headerCellValueList, Sheet sheet, int totalColumnsCount, DataExtractorNotification dataExtractorNotification) {
		List<HashMap<String, Object>> meticValues = new ArrayList<HashMap<String, Object>>();
		try{
			log.info("Inside readSingleValueMeticExcel()");

			int numberOfValidRecords = 0;
			int numberOfInvalidRecords = 0;
			
			int headerColumnNumber = Integer.parseInt(dataTemplate.get("Header Column Number")) - 1;
			int contentRowNumber = Integer.parseInt(dataTemplate.get("Content Row Number")) - 1;
			int totalRowsCount = sheet.getLastRowNum() + 1;

			HashMap<String, Object> metricValue = null;
			
			DataFormatter formatter = new DataFormatter();
			Row contentRow;
			int dataColumnNumber = 0;
			for (int rowCounter = contentRowNumber; rowCounter < totalRowsCount; rowCounter++) {
				try{
					contentRow = sheet.getRow(rowCounter);
					if(contentRow != null){
						metricValue = new HashMap<String, Object>();
						for (int columnCounter = 0; columnCounter < headerCellValueList.size(); columnCounter++) {
							dataColumnNumber = columnCounter + headerColumnNumber;
							while(dataColumnNumber >= totalColumnsCount){
								dataColumnNumber = dataColumnNumber - totalColumnsCount + headerColumnNumber;
							}
							Cell contentCell = contentRow.getCell(dataColumnNumber);
							String cellValue = formatter.formatCellValue(contentCell);
							
							if(cellValue != null && !cellValue.trim().isEmpty() && !"NA".equalsIgnoreCase(cellValue) && !"N/A".equalsIgnoreCase(cellValue)){
								String headerValue = headerCellValueList.get(columnCounter).replace(".", " ");
								if(headerValue != null && !headerValue.isEmpty()){
									if("Date".equalsIgnoreCase(headerValue)){
										metricValue.put(headerValue, setDateForMongoDB(contentCell.getDateCellValue()));
									}else{
										if(contentCell.getCellType() == Cell.CELL_TYPE_FORMULA){
											metricValue.put(headerValue, contentCell.getNumericCellValue());
										}else{
											if(NumberUtils.isNumber(cellValue)){
												metricValue.put(headerValue, Float.parseFloat(cellValue));
											}else{
												metricValue.put(headerValue, cellValue);
											}
										}
									}
								}
							}
						}
						
						if(metricValue.size() > 0){
							metricValue.put("productName", dataTemplate.get("scheduledProduct"));
							metricValue.put("productId", Integer.parseInt(dataTemplate.get("scheduledProductId")));
							metricValue.put("Competency", dataTemplate.get("scheduledCompetency"));
							metricValue.put("competencyId", Integer.parseInt(dataTemplate.get("scheduledCompetencyId")));
							metricValue.put("project", dataTemplate.get("scheduledProduct"));
							metricValue.put("projectId", Integer.parseInt(dataTemplate.get("scheduledProductId")));
							metricValue.put("customer", dataTemplate.get("scheduledCustomer"));
							metricValue.put("customerId", Integer.parseInt(dataTemplate.get("scheduledCustomerId")));
							metricValue.put("testFactoryName", dataTemplate.get("scheduledTestFactoryName"));
							metricValue.put("testFactoryId", Integer.parseInt(dataTemplate.get("scheduledTestFactoryId")));
							metricValue.put("testCentersName", dataTemplate.get("scheduledTestCentersName"));
							metricValue.put("testCentersId", Integer.parseInt(dataTemplate.get("scheduledTestCentersId")));
							metricValue.put("createdDate", setDateForMongoDB(new Date()));
							metricValue.put("updatedDate", setDateForMongoDB(new Date()));
							meticValues.add(metricValue);
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
		return meticValues;
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
