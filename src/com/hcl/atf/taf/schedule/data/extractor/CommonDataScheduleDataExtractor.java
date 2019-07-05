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

import com.hcl.atf.taf.model.DataExtractorNotification;

@Service
public class CommonDataScheduleDataExtractor {

	@Autowired
	CollectionValuesScheduleMapper collectionValuesScheduleMapper;
	
	private static final Log log = LogFactory.getLog(CommonDataScheduleDataExtractor.class);
	
	public List<HashMap<String, Object>> readCommonDataExcel(LinkedHashMap<String, String> dataTemplate, ArrayList<String> headerCellValueList, Sheet sheet, int totalColumnsCount, DataExtractorNotification dataExtractorNotification) {
		List<HashMap<String, Object>> commonDataValues = new ArrayList<HashMap<String, Object>>();
		try{
			log.info("Inside readCommonDataExcel()");

			int numberOfValidRecords = 0;
			int numberOfInvalidRecords = 0;
			
			int headerColumnNumber = Integer.parseInt(dataTemplate.get("Header Column Number")) - 1;
			int contentRowNumber = Integer.parseInt(dataTemplate.get("Content Row Number")) - 1;
			int totalRowsCount = sheet.getLastRowNum() + 1;

			HashMap<String, Object> commonDataValue = null;
			
			DataFormatter formatter = new DataFormatter();
			Row contentRow;
			int dataColumnNumber = 0;
			String columnMapperString = dataTemplate.get("columnMapper");
			LinkedHashMap<String, String> columnMapper = new LinkedHashMap<String, String>();
			if(columnMapperString != null && !columnMapperString.trim().isEmpty()){
				columnMapperString = columnMapperString.substring(1, columnMapperString.length() - 1);
				if(columnMapperString != null && !columnMapperString.trim().isEmpty()){
					String[] keyValuePairs = columnMapperString.split(",");
					for(String pair : keyValuePairs) {
					    String[] entry = pair.split("="); 
					    columnMapper.put(entry[0].trim(), entry[1].trim());
					}
				}
			}
			
			for (int rowCounter = contentRowNumber; rowCounter < totalRowsCount; rowCounter++) {
				try{
					contentRow = sheet.getRow(rowCounter);
					if(contentRow != null){
						commonDataValue = new HashMap<String, Object>();
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
									if(columnMapper.containsKey(headerValue)){
										if(contentCell.getCellType() == Cell.CELL_TYPE_FORMULA){
											switch(contentCell.getCachedFormulaResultType()) {
									            case Cell.CELL_TYPE_NUMERIC:
									            	commonDataValue.put(columnMapper.get(headerValue), contentCell.getNumericCellValue());
									                break;
									            default:
									            	commonDataValue.put(columnMapper.get(headerValue), contentCell.getRichStringCellValue().toString()+"");
									                break;
											}
											
										}else{
											commonDataValue.put(columnMapper.get(headerValue), cellValue);
										}
									}
								}
							}
						}
						
						if(commonDataValue.size() > 0){
							commonDataValues.add(commonDataValue);
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
		return commonDataValues;
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
