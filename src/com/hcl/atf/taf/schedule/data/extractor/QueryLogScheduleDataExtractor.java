package com.hcl.atf.taf.schedule.data.extractor;

import java.util.ArrayList;
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

import com.hcl.atf.taf.model.DataExtractorNotification;
import com.hcl.atf.taf.model.DefectCollection;

@Service
public class QueryLogScheduleDataExtractor {

	private static final Log log = LogFactory.getLog(QueryLogScheduleDataExtractor.class);

	@Autowired
	CollectionValuesScheduleMapper collectionValuesScheduleMapper;
	
	public List<DefectCollection> readQueryLogExcel(LinkedHashMap<String, String> dataTemplate, ArrayList<String> headerCellValueList, Sheet sheet, int totalColumnsCount, DataExtractorNotification dataExtractorNotification) {
		List<DefectCollection> defectCollections = new ArrayList<DefectCollection>();
		try{
			log.info("Inside readQueryLogExcel()");

			int numberOfValidRecords = 0;
			int numberOfInvalidRecords = 0;
			
			int headerColumnNumber = Integer.parseInt(dataTemplate.get("Header Column Number")) - 1;
			int contentRowNumber = Integer.parseInt(dataTemplate.get("Content Row Number")) - 1;
			int totalRowsCount = sheet.getLastRowNum() + 1;

			DefectCollection defectCollection = null;
			
			DataFormatter formatter = new DataFormatter();
			Row contentRow;
			int dataColumnNumber = 0;
			String programName = "";
			String activity = "";
			String onshoreLead = "";
			boolean isPlant = false;
			boolean isActivity = false;
			boolean isOnshoreLead = false;
			boolean isOffShoreLead = false;
			
			String[] headerRowNumbers = dataTemplate.get("Header Row Number").split(",");
			for(int rowCounter = 0; rowCounter < (Integer.parseInt(headerRowNumbers[0]) - 1); rowCounter++){
				contentRow = sheet.getRow(rowCounter);
				if(contentRow != null){
					for(int columnCounter = 0; columnCounter < headerCellValueList.size(); columnCounter++){
						Cell contentCell = contentRow.getCell(columnCounter);
						String cellValue = formatter.formatCellValue(contentCell);
						if("Plant Name".equalsIgnoreCase(cellValue.trim())){
							isPlant = true;
							continue;
						}
						if(isPlant && cellValue != null && !cellValue.trim().isEmpty()){
							programName = cellValue;
							isPlant = false;
							continue;
						}
						if("Activity".equalsIgnoreCase(cellValue.trim())){
							isActivity = true;
							continue;
						}
						if(isActivity && cellValue != null && !cellValue.trim().isEmpty()){
							activity = cellValue;
							isActivity = false;
							continue;
						}
						if("Onshore Lead".equalsIgnoreCase(cellValue.trim())){
							isOnshoreLead = true;
							continue;
						}
						if(isOnshoreLead && cellValue != null && !cellValue.trim().isEmpty()){
							onshoreLead = cellValue;
							isOnshoreLead = false;
							continue;
						}
						if("Offshore Lead".equalsIgnoreCase(cellValue.trim())){
							isOffShoreLead = true;
							continue;
						}
						if(isOffShoreLead && cellValue != null && !cellValue.trim().isEmpty()){
							isOffShoreLead = false;
							continue;
						}
					}
				}
			}

			for (int rowCounter = contentRowNumber; rowCounter < totalRowsCount; rowCounter++) {
				try{
					contentRow = sheet.getRow(rowCounter);
					if(contentRow != null){
						defectCollection = new DefectCollection();
						defectCollection.setProductName(programName);
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
						defectCollection.setFeature(activity);
						defectCollection.setOwner(onshoreLead);
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
										defectCollection.setActivityName(cellValue);
										defectCollection.setActivityComponent(cellValue);
									}else if(dataTemplate.containsKey("assignedDate") && dataTemplate.get("assignedDate").equalsIgnoreCase(headerCellValueList.get(columnCounter))){
										defectCollection.setRaisedDate(contentCell.getDateCellValue());
										defectCollection.setAssignedDate(contentCell.getDateCellValue());
									}else if(dataTemplate.containsKey("expectedEffort") && dataTemplate.get("expectedEffort").equalsIgnoreCase(headerCellValueList.get(columnCounter))){
										float expectedEffort = 0.0F;
										if(NumberUtils.isNumber(cellValue)){
											expectedEffort = Float.parseFloat(cellValue);
										}
										defectCollection.setExpectedEffort(expectedEffort);
										if(defectCollection.getAssignedDate() != null){
											defectCollection.setPlannedClosureDate(new Date((defectCollection.getAssignedDate()).getTime() + Math.round(expectedEffort) * 1000 * 60 * 60 * 24));										
										}
									}else{
										collectionValuesScheduleMapper.setDefectCollectionValues(defectCollection, dataTemplate, headerCellValueList.get(columnCounter), cellValue, contentCell);
									}
																		
								}
							}
						}

						if(defectCollection.getProductName() != null && !defectCollection.getProductName().isEmpty() && defectCollection.getRaisedDate() != null){
							defectCollections.add(defectCollection);
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
