package com.hcl.atf.taf.schedule.data.extractor;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hcl.atf.taf.model.DataExtractorNotification;
import com.hcl.atf.taf.model.DefectCollection;

@Service
public class CQDumpScheduleDataExtractor {

	@Autowired
	CollectionValuesScheduleMapper collectionValuesScheduleMapper;
	
	private static final Log log = LogFactory.getLog(CQDumpScheduleDataExtractor.class);
	
	public List<DefectCollection> readCQDumpExcel(LinkedHashMap<String, String> dataTemplate, ArrayList<String> headerCellValueList, Sheet sheet, int totalColumnsCount, DataExtractorNotification dataExtractorNotification) {
		List<DefectCollection> defectCollections = new ArrayList<DefectCollection>();
		try{
			log.info("Inside readCQDumpExcel()");

			int numberOfValidRecords = 0;
			int numberOfInvalidRecords = 0;
			
			int headerColumnNumber = Integer.parseInt(dataTemplate.get("Header Column Number")) - 1;
			int contentRowNumber = Integer.parseInt(dataTemplate.get("Content Row Number")) - 1;
			int totalRowsCount = sheet.getLastRowNum() + 1;
						
			DefectCollection defectCollection = null;

			DataFormatter formatter = new DataFormatter();
			SimpleDateFormat cqDumpDateFormat = new SimpleDateFormat("MMMM dd, yyyy hh:mm:ss a 'GMT'XXX", Locale.ENGLISH);
			Row contentRow;
			int dataColumnNumber = 0;
			List<String> programsAffected = new ArrayList<String>();
			for (int rowCounter = contentRowNumber; rowCounter < totalRowsCount; rowCounter++) {
				try{
					contentRow = sheet.getRow(rowCounter);
					if(contentRow != null){
						defectCollection = new DefectCollection();
						defectCollection.setProductName(dataTemplate.get("scheduledProduct"));
						defectCollection.setProductId(Integer.parseInt(dataTemplate.get("scheduledProductId")));
						defectCollection.setCompetency(dataTemplate.get("scheduledCompetency"));
						defectCollection.setCompetencyId(Integer.parseInt(dataTemplate.get("scheduledCompetencyId")));
						defectCollection.setProject(dataTemplate.get("scheduledProduct"));
						defectCollection.setProjectId(Integer.parseInt(dataTemplate.get("scheduledProductId")));
						defectCollection.setCustomerName(dataTemplate.get("scheduledCustomer"));
						defectCollection.setCustomerId(Integer.parseInt(dataTemplate.get("scheduledCustomerId")));
						defectCollection.setTestFactoryName(dataTemplate.get("scheduledTestFactoryName"));
						defectCollection.setTestFactoryId(Integer.parseInt(dataTemplate.get("scheduledTestFactoryId")));
						defectCollection.setTestCentersName(dataTemplate.get("scheduledTestCentersName"));
						defectCollection.setTestCentersId(Integer.parseInt(dataTemplate.get("scheduledTestCentersId")));

						programsAffected = new ArrayList<String>();
						
						for (int columnCounter = 0; columnCounter < headerCellValueList.size(); columnCounter++) {
							dataColumnNumber = columnCounter + headerColumnNumber;
							while(dataColumnNumber >= totalColumnsCount){
								dataColumnNumber = dataColumnNumber - totalColumnsCount + headerColumnNumber;
							}
							Cell contentCell = contentRow.getCell(dataColumnNumber);
							String cellValue = formatter.formatCellValue(contentCell);
							
							if(dataTemplate.containsValue(headerCellValueList.get(columnCounter))){
								if(cellValue != null && !cellValue.trim().isEmpty() && !"NA".equalsIgnoreCase(cellValue)){
									if(dataTemplate.containsKey("competency") && dataTemplate.get("competency").equalsIgnoreCase(headerCellValueList.get(columnCounter))){
										if(cellValue.contains("-DRV-")){
											cellValue = "Driver";
										}
										defectCollection.setCompetency(cellValue);
									}else if(dataTemplate.containsKey("programName") && dataTemplate.get("programName").equalsIgnoreCase(headerCellValueList.get(columnCounter))){
										String[] programNames = cellValue.trim().split("[\n\r]");
										if(programNames != null && programNames.length > 0){
											programsAffected.addAll(Arrays.asList(programNames));
										}
										cellValue = cellValue.trim().replaceAll("-", "_").replaceAll(" ", "_");
										cellValue = cellValue.trim().replaceAll("[\n\r]", "-");
										defectCollection.setProject(cellValue);
									}else if(dataTemplate.containsKey("title") && dataTemplate.get("title").equalsIgnoreCase(headerCellValueList.get(columnCounter))){
										if(cellValue.length() > 75){
											cellValue = cellValue.substring(0, 75);
										}
										defectCollection.setTitle(cellValue);
									}else if(dataTemplate.containsKey("raisedDate") && dataTemplate.get("raisedDate").equalsIgnoreCase(headerCellValueList.get(columnCounter))){
										Date raisedDate = cqDumpDateFormat.parse(cellValue);
										defectCollection.setRaisedDate(raisedDate);
									}else{
										collectionValuesScheduleMapper.setDefectCollectionValues(defectCollection, dataTemplate, headerCellValueList.get(columnCounter), cellValue, contentCell);									
									}
								}
							}
						}
						
						if(defectCollection.getDefectId() != null && !defectCollection.getDefectId().isEmpty()){
							for(String programAffected : programsAffected){
								DefectCollection defectCollectionPrograms = (DefectCollection) defectCollection.clone();
								defectCollectionPrograms.setProductName(programAffected);
								defectCollections.add(defectCollectionPrograms);
							}
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
		return defectCollections;
	}
}
