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

import com.hcl.atf.taf.controller.utilities.DateUtility;
import com.hcl.atf.taf.model.DataExtractorNotification;

@Service
public class DefectDumpScheduleDataExtractor {

	@Autowired
	CollectionValuesScheduleMapper collectionValuesScheduleMapper;
	
	private static final Log log = LogFactory.getLog(DefectDumpScheduleDataExtractor.class);
	
	public List<HashMap<String, Object>> readDefectDumpMeticExcel(LinkedHashMap<String, String> dataTemplate, ArrayList<String> headerCellValueList, Sheet sheet, int totalColumnsCount, DataExtractorNotification dataExtractorNotification) {
		List<HashMap<String, Object>> defectDumpValues = new ArrayList<HashMap<String, Object>>();
		try{
			log.info("Inside readDefectDumpMeticExcel()");

			int numberOfValidRecords = 0;
			int numberOfInvalidRecords = 0;
			
			int headerColumnNumber = Integer.parseInt(dataTemplate.get("Header Column Number")) - 1;
			int contentRowNumber = Integer.parseInt(dataTemplate.get("Content Row Number")) - 1;
			int totalRowsCount = sheet.getLastRowNum() + 1;

			HashMap<String, Object> defectDumpValue = null;
			
			DataFormatter formatter = new DataFormatter();
			Row contentRow;
			int dataColumnNumber = 0;
			for (int rowCounter = contentRowNumber; rowCounter < totalRowsCount; rowCounter++) {
				try{
					contentRow = sheet.getRow(rowCounter);
					if(contentRow != null){
						defectDumpValue = new HashMap<String, Object>();
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
										defectDumpValue.put(headerValue, contentCell.getNumericCellValue());
									}else{
										if(headerValue.equalsIgnoreCase("Test Case #") || headerValue.equalsIgnoreCase("Detected in Release") || headerValue.equalsIgnoreCase("Fixed In Version")){
											defectDumpValue.put(headerValue, cellValue);
										}else if(NumberUtils.isNumber(cellValue) && !headerValue.equalsIgnoreCase("Defect ID") ){
											defectDumpValue.put(headerValue, Float.parseFloat(cellValue));
										}else if(headerValue.equalsIgnoreCase("Defect Creation Date/Time (PT)")){
											Date defectDate = DateUtility.dateformatWithOutTime(cellValue);
											
											Calendar cal = Calendar.getInstance();
											cal.setTime(defectDate);
											cal.set(Calendar.HOUR_OF_DAY, 0);
											cal.set(Calendar.MINUTE, 0);
											cal.set(Calendar.SECOND, 0);
											cal.set(Calendar.MILLISECOND, 0);
											
											defectDumpValue.put(headerValue, setDateForMongoDB(cal.getTime()));

											Calendar calendar = Calendar.getInstance();
										    calendar.setTime(defectDate);
										    
										    calendar.set(Calendar.DAY_OF_MONTH, 1);
										    calendar.set(Calendar.HOUR_OF_DAY, 0);
										    calendar.set(Calendar.MINUTE, 0);
										    calendar.set(Calendar.SECOND, 0);
										    calendar.set(Calendar.MILLISECOND, 0);
										    defectDumpValue.put("Defect Period", setDateForMongoDB(calendar.getTime()));
											
										}else if(headerValue.equalsIgnoreCase("Modified Date - not used")){
											Date modifiedDate = DateUtility.dateformatWithOutTime(cellValue);
											
											Calendar calendar = Calendar.getInstance();
										    calendar.setTime(modifiedDate);
										    String dateRange[] = cellValue.split("/");
										    Integer  year = 0;
										    
										    if(dateRange[2].length() == 4){
										    	year = Integer.parseInt(dateRange[2]);
										    }else{
										    	String[] date = dateRange[2].split(" ");
										    	year = 2000+Integer.parseInt(date[0]);
										    }
										    int month = calendar.get(Calendar.MONTH);
										    calendar.set(year, month, 1);
										    defectDumpValue.put("Modified Date - not used", setDateForMongoDB(calendar.getTime()));
											
										}else{
											defectDumpValue.put(headerValue, cellValue);
										}
									}
								}
							}
						}
						
						if(defectDumpValue.size() > 0){
							defectDumpValue.put("_id", defectDumpValue.get("Defect ID").toString());
							defectDumpValue.put("productName", dataTemplate.get("scheduledProduct"));
							defectDumpValue.put("productId", Integer.parseInt(dataTemplate.get("scheduledProductId")));
							defectDumpValue.put("Competency", dataTemplate.get("scheduledCompetency"));
							defectDumpValue.put("competencyId", Integer.parseInt(dataTemplate.get("scheduledCompetencyId")));
							defectDumpValue.put("project", dataTemplate.get("scheduledProduct"));
							defectDumpValue.put("projectId", Integer.parseInt(dataTemplate.get("scheduledProductId")));
							defectDumpValue.put("customer", dataTemplate.get("scheduledCustomer"));
							defectDumpValue.put("customerId", Integer.parseInt(dataTemplate.get("scheduledCustomerId")));
							defectDumpValue.put("testFactoryName", dataTemplate.get("scheduledTestFactoryName"));
							defectDumpValue.put("testFactoryId", Integer.parseInt(dataTemplate.get("scheduledTestFactoryId")));
							defectDumpValue.put("testCentersName", dataTemplate.get("scheduledTestCentersName"));
							defectDumpValue.put("testCentersId", Integer.parseInt(dataTemplate.get("scheduledTestCentersId")));
							defectDumpValue.put("createdDate", setDateForMongoDB(new Date()));
							defectDumpValue.put("updatedDate", setDateForMongoDB(new Date()));
							defectDumpValues.add(defectDumpValue);
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
		return defectDumpValues;
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
