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
public class PMOScheduleDataExtractor {

	@Autowired
	CollectionValuesScheduleMapper collectionValuesScheduleMapper;
	
	private static final Log log = LogFactory.getLog(PMOScheduleDataExtractor.class);
	
	public List<HashMap<String, Object>> readPMOMeticExcel(LinkedHashMap<String, String> dataTemplate, ArrayList<String> headerCellValueList, Sheet sheet, int totalColumnsCount, DataExtractorNotification dataExtractorNotification) {
		List<HashMap<String, Object>> pmoValues = new ArrayList<HashMap<String, Object>>();
		try{
			log.info("Inside readPMOMeticExcel()");

			int numberOfValidRecords = 0;
			int numberOfInvalidRecords = 0;
			
			int headerColumnNumber = Integer.parseInt(dataTemplate.get("Header Column Number")) - 1;
			int contentRowNumber = Integer.parseInt(dataTemplate.get("Content Row Number")) - 1;
			int totalRowsCount = sheet.getLastRowNum() + 1;

			HashMap<String, Object> pmoValue = null;
			
			DataFormatter formatter = new DataFormatter();
			Row contentRow;
			int dataColumnNumber = 0;
			for (int rowCounter = contentRowNumber; rowCounter < totalRowsCount; rowCounter++) {
				try{
					contentRow = sheet.getRow(rowCounter);
					if(contentRow != null){
						pmoValue = new HashMap<String, Object>();
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
										pmoValue.put(headerValue, contentCell.getNumericCellValue());
									}else{
										
										if(headerValue.equalsIgnoreCase("Project ID") || headerValue.equalsIgnoreCase("SID") || headerValue.equalsIgnoreCase("Link to Parent") ){
											pmoValue.put(headerValue, cellValue);
										}
										else if(headerValue.equalsIgnoreCase("M9 Date")){
											Date mDate = DateUtility.toConvertDate(cellValue);
																						
											pmoValue.put(headerValue, cellValue);
											
											 /* Extra Column */
											Calendar calendar = Calendar.getInstance();
										    calendar.setTime(mDate);
										    String dateRange[] = cellValue.split("/");
										    Integer  year = 0;
										    
										    if(dateRange[2].length() == 4){
										    	year = Integer.parseInt(dateRange[2]);
										    }else{
										    	year = 2000+Integer.parseInt(dateRange[2]);
										    }
										    int month = calendar.get(Calendar.MONTH);
										    log.info(" month "+month);
										    calendar.set(year, month, 1);
										    pmoValue.put("M9 Period", setDateForMongoDB(calendar.getTime()));

											
										}
										else if(NumberUtils.isNumber(cellValue)){
											pmoValue.put(headerValue, Float.parseFloat(cellValue));
										}else{
											pmoValue.put(headerValue, cellValue);
										}
									}
								}
							}
						}
						
						if(pmoValue.size() > 0){
							pmoValue.put("_id", pmoValue.get("PMT").toString());
							pmoValue.put("productName", dataTemplate.get("scheduledProduct"));
							pmoValue.put("productId", Integer.parseInt(dataTemplate.get("scheduledProductId")));
							pmoValue.put("Competency", dataTemplate.get("scheduledCompetency"));
							pmoValue.put("competencyId", Integer.parseInt(dataTemplate.get("scheduledCompetencyId")));
							pmoValue.put("project", dataTemplate.get("scheduledProduct"));
							pmoValue.put("projectId", Integer.parseInt(dataTemplate.get("scheduledProductId")));
							pmoValue.put("customer", dataTemplate.get("scheduledCustomer"));
							pmoValue.put("customerId", Integer.parseInt(dataTemplate.get("scheduledCustomerId")));
							pmoValue.put("testFactoryName", dataTemplate.get("scheduledTestFactoryName"));
							pmoValue.put("testFactoryId", Integer.parseInt(dataTemplate.get("scheduledTestFactoryId")));
							pmoValue.put("testCentersName", dataTemplate.get("scheduledTestCentersName"));
							pmoValue.put("testCentersId", Integer.parseInt(dataTemplate.get("scheduledTestCentersId")));
							pmoValue.put("createdDate", setDateForMongoDB(new Date()));
							pmoValue.put("updatedDate", setDateForMongoDB(new Date()));
							pmoValues.add(pmoValue);
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
		return pmoValues;
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
