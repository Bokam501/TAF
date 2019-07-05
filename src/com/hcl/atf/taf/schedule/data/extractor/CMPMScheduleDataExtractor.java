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
public class CMPMScheduleDataExtractor {

	@Autowired
	CollectionValuesScheduleMapper collectionValuesScheduleMapper;
	
	private static final Log log = LogFactory.getLog(CMPMScheduleDataExtractor.class);
	
	public List<HashMap<String, Object>> readCMPMMeticExcel(LinkedHashMap<String, String> dataTemplate, ArrayList<String> headerCellValueList, Sheet sheet, int totalColumnsCount, DataExtractorNotification dataExtractorNotification) {
		List<HashMap<String, Object>> cmpmValues = new ArrayList<HashMap<String, Object>>();
		try{
			log.info("Inside readCMPMMeticExcel()");

			int numberOfValidRecords = 0;
			int numberOfInvalidRecords = 0;
			
			int headerColumnNumber = Integer.parseInt(dataTemplate.get("Header Column Number")) - 1;
			int contentRowNumber = Integer.parseInt(dataTemplate.get("Content Row Number")) - 1;
			int totalRowsCount = sheet.getLastRowNum() + 1;

			HashMap<String, Object> cmpmValue = null;
			
			DataFormatter formatter = new DataFormatter();
			Row contentRow;
			int dataColumnNumber = 0;
			for (int rowCounter = contentRowNumber; rowCounter < totalRowsCount; rowCounter++) {
				try{
					contentRow = sheet.getRow(rowCounter);
					if(contentRow != null){
						cmpmValue = new HashMap<String, Object>();
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
									if(contentCell.getCellType() == Cell.CELL_TYPE_FORMULA){
										cmpmValue.put(headerValue, contentCell.getNumericCellValue());
									}else{
										if(headerValue.equalsIgnoreCase("PRISM ID")){
											cmpmValue.put(headerValue, cellValue);
										}else if(headerValue.equalsIgnoreCase("Date")){
											Calendar cal = Calendar.getInstance();
											
											String[] yearMonth = cellValue.split("/");
											Integer year = Integer.parseInt(yearMonth[0]);
											Integer month = Integer.parseInt(yearMonth[1]);
											cal.set(year, month-1, 1);
											cal.set(Calendar.HOUR_OF_DAY, 0);
											cal.set(Calendar.MINUTE, 0);
											cal.set(Calendar.SECOND, 0);
											cal.set(Calendar.MILLISECOND, 0);
											cmpmValue.put(headerValue, setDateForMongoDB(cal.getTime()));
											
										}
										else if(NumberUtils.isNumber(cellValue)){
											cmpmValue.put(headerValue, Float.parseFloat(cellValue));
										}else if(headerValue.equalsIgnoreCase("PMT ID")){
											cmpmValue.put("PMT", cellValue);
										}else{
											cmpmValue.put(headerValue, cellValue);
										}
									}
								}
							}
						}
						
						if(cmpmValue.size() > 0){
							cmpmValue.put("productName", dataTemplate.get("scheduledProduct"));
							cmpmValue.put("productId", Integer.parseInt(dataTemplate.get("scheduledProductId")));
							cmpmValue.put("Competency", dataTemplate.get("scheduledCompetency"));
							cmpmValue.put("competencyId", Integer.parseInt(dataTemplate.get("scheduledCompetencyId")));
							cmpmValue.put("project", dataTemplate.get("scheduledProduct"));
							cmpmValue.put("projectId", Integer.parseInt(dataTemplate.get("scheduledProductId")));
							cmpmValue.put("customer", dataTemplate.get("scheduledCustomer"));
							cmpmValue.put("customerId", Integer.parseInt(dataTemplate.get("scheduledCustomerId")));
							cmpmValue.put("testFactoryName", dataTemplate.get("scheduledTestFactoryName"));
							cmpmValue.put("testFactoryId", Integer.parseInt(dataTemplate.get("scheduledTestFactoryId")));
							cmpmValue.put("testCentersName", dataTemplate.get("scheduledTestCentersName"));
							cmpmValue.put("testCentersId", Integer.parseInt(dataTemplate.get("scheduledTestCentersId")));
							cmpmValue.put("createdDate", setDateForMongoDB(new Date()));
							cmpmValue.put("updatedDate", setDateForMongoDB(new Date()));
							cmpmValues.add(cmpmValue);
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
		return cmpmValues;
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
