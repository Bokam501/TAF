package com.hcl.atf.taf.schedule.data.extractor;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.hssf.usermodel.HSSFFormulaEvaluator;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.RichTextString;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFFormulaEvaluator;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import com.hcl.atf.taf.model.DataExtractorNotification;

@Service
public class DataSourceScheduleMapperExtractor {

	private static final Log log = LogFactory.getLog(DataSourceScheduleMapperExtractor.class);

	public LinkedHashMap<String, String> readExcelDataMapperTemplate(String fileName, String sheetName, String engagement, String project, String extractorType, DataExtractorNotification dataExtractorNotification) {
		LinkedHashMap<String, String> dataMapper = new LinkedHashMap<String, String>();
		LinkedHashMap<String, String> columnMapper = new LinkedHashMap<String, String>();
		FileInputStream fileInputStream = null;
		try{
			log.info("Reading excel data mapper template");
			File file = new File(fileName); 
			fileInputStream = new FileInputStream(file);
			
			Workbook workBook = null;
			if(file != null && file.getName().trim().toLowerCase().endsWith("xlsx")){
				workBook = new XSSFWorkbook(fileInputStream);
				XSSFFormulaEvaluator.evaluateAllFormulaCells((XSSFWorkbook) workBook);
			}else if(file != null && file.getName().trim().toLowerCase().endsWith("xlsm")){
				workBook = new XSSFWorkbook(fileInputStream);
				XSSFFormulaEvaluator.evaluateAllFormulaCells((XSSFWorkbook) workBook);
			}else if(file != null && file.getName().trim().toLowerCase().endsWith("xls")){
				workBook = new HSSFWorkbook(fileInputStream);
				HSSFFormulaEvaluator.evaluateAllFormulaCells((HSSFWorkbook) workBook);
			}
			
			Sheet sheet = workBook.getSheet(sheetName);
			if(sheet == null){
				dataExtractorNotification.setFileFailed(true);
				dataExtractorNotification.getMessageList().add(new Date()+" Unable to find sheet with name : "+sheetName+" in file - "+file.getAbsolutePath());
				return dataMapper;
			}
			
			Row headerRow = sheet.getRow(0);
			int totalRowsCount = sheet.getLastRowNum() + 1;
			int totalColumnsCount = sheet.getRow(0).getLastCellNum();
			
			Cell headerCell = null;
			Iterator<Cell> headerCells = headerRow.cellIterator();
			ArrayList<String> headerValueList = new ArrayList<String>();
			String headerCellValue = "";
			while (headerCells.hasNext()) {
				headerCell = (XSSFCell) headerCells.next();
				RichTextString headerValue = headerCell.getRichStringCellValue();
				headerCellValue = headerValue.getString().trim().replaceAll("[\n\r]", "");
				if(headerCellValue != null && !headerCellValue.isEmpty()){
					headerValueList.add(headerValue.getString().trim().replaceAll("[\n\r]", ""));
				}
			}
			
			Row contentRow;
			DataFormatter formatter = new DataFormatter();
			
			if (headerValueList != null && headerValueList.size() > 0) {
				String isExtractorTypeFound = "false";
				for (int rowCounter = 1; rowCounter < totalRowsCount; rowCounter++) {
					contentRow = sheet.getRow(rowCounter);
					dataMapper = new LinkedHashMap<String, String>();
					columnMapper = new LinkedHashMap<String, String>();
					for (int columnCounter = 0; columnCounter < totalColumnsCount; columnCounter++) {
						Cell contentCell = contentRow.getCell(columnCounter);
						String cellValue = formatter.formatCellValue(contentCell).trim().replaceAll("[\n\r]", "");
						if(cellValue != null && cellValue.endsWith("*")){
							cellValue = cellValue.substring(0, cellValue.length() - 1);
						}
						if(headerValueList.get(columnCounter) != null && cellValue != null && !cellValue.isEmpty()){
							dataMapper.put(headerValueList.get(columnCounter), cellValue.trim());
							if(columnCounter > 6){
								columnMapper.put(cellValue.trim(), headerValueList.get(columnCounter));
							}
						}
					}
					
					if(dataMapper.containsKey("File type") && extractorType.equalsIgnoreCase(dataMapper.get("File type"))){
						isExtractorTypeFound = "true";
					}

					if(dataMapper.containsKey("File type") && dataMapper.containsKey("Engagement") && dataMapper.get("File type") != null && dataMapper.get("Engagement") != null){
						if(dataMapper.get("File type").trim().equalsIgnoreCase(extractorType) && dataMapper.get("Engagement").trim().equalsIgnoreCase(engagement)){
							if((dataMapper.containsKey("Product") && dataMapper.get("Product") != null && dataMapper.get("Product").trim().equalsIgnoreCase(project)) || (((!dataMapper.containsKey("Product") || dataMapper.get("Product") == null) && project.trim().isEmpty()))){
								isExtractorTypeFound = "true";
								break;
							}
						}
					}
				}
				dataMapper.put("columnMapper", columnMapper.toString());
				dataMapper.put("isExtractorTypeFound", isExtractorTypeFound);
			}else{
				dataExtractorNotification.setFileFailed(true);
				dataExtractorNotification.getMessageList().add(new Date()+" No header value / no mapping found in iLCM mapper file");
			}
			log.info("Successfully completed reading ILCM excel data template mapper");
		}catch(Exception ex){
			dataExtractorNotification.setFileFailed(true);
			dataExtractorNotification.getMessageList().add(new Date()+" Unable to read file - "+fileName+" due to "+ex.getMessage());
			log.error("Error on extracting excel template ", ex);
		}finally{
			if(fileInputStream != null){
				try {
					fileInputStream.close();
				} catch (Exception ex) {
					log.error("Stream closing error - ", ex);
				}
			}
		}
		
		return dataMapper;
	}
	
}
