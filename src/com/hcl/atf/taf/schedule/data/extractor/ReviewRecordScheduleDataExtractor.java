package com.hcl.atf.taf.schedule.data.extractor;

import java.util.ArrayList;
import java.util.Date;
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
import com.hcl.atf.taf.model.ReviewRecordCollection;

@Service
public class ReviewRecordScheduleDataExtractor {

	@Autowired
	CollectionValuesScheduleMapper collectionValuesScheduleMapper;
	
	private static final Log log = LogFactory.getLog(ReviewRecordScheduleDataExtractor.class);
	
	public List<ReviewRecordCollection> readReviewRecordExcel(LinkedHashMap<String, String> dataTemplate, ArrayList<String> headerCellValueList, Sheet sheet, int totalColumnsCount, DataExtractorNotification dataExtractorNotification) {
		List<ReviewRecordCollection> reviewRecordCollections = new ArrayList<ReviewRecordCollection>();
		try{
			log.info("Inside readReviewRecordExcel()");

			int numberOfValidRecords = 0;
			int numberOfInvalidRecords = 0;
			
			int headerColumnNumber = Integer.parseInt(dataTemplate.get("Header Column Number")) - 1;
			int contentRowNumber = Integer.parseInt(dataTemplate.get("Content Row Number")) - 1;
			int totalRowsCount = sheet.getLastRowNum() + 1;

			ReviewRecordCollection reviewRecordCollection = null;
			
			DataFormatter formatter = new DataFormatter();
			Row contentRow;
			int dataColumnNumber = 0;
			for (int rowCounter = contentRowNumber; rowCounter < totalRowsCount; rowCounter++) {
				try{
					contentRow = sheet.getRow(rowCounter);
					if(contentRow != null){
						reviewRecordCollection = new ReviewRecordCollection();
						reviewRecordCollection.setProductName(dataTemplate.get("scheduledProduct"));
						reviewRecordCollection.setProductId(Integer.parseInt(dataTemplate.get("scheduledProductId")));
						reviewRecordCollection.setCompetency(dataTemplate.get("scheduledCompetency"));
						reviewRecordCollection.setCompetencyId(Integer.parseInt(dataTemplate.get("scheduledCompetencyId")));
						reviewRecordCollection.setProject(dataTemplate.get("scheduledProduct"));
						reviewRecordCollection.setProjectId(Integer.parseInt(dataTemplate.get("scheduledProductId")));
						reviewRecordCollection.setCustomerName(dataTemplate.get("scheduledCustomer"));
						reviewRecordCollection.setCustomerId(Integer.parseInt(dataTemplate.get("scheduledCustomerId")));
						reviewRecordCollection.setTestFactoryName(dataTemplate.get("scheduledTestFactoryName"));
						reviewRecordCollection.setTestFactoryId(Integer.parseInt(dataTemplate.get("scheduledTestFactoryId")));
						reviewRecordCollection.setTestCentersName(dataTemplate.get("scheduledTestCentersName"));
						reviewRecordCollection.setTestCentersId(Integer.parseInt(dataTemplate.get("scheduledTestCentersId")));	
						
						for (int columnCounter = 0; columnCounter < headerCellValueList.size(); columnCounter++) {
							dataColumnNumber = columnCounter + headerColumnNumber;
							while(dataColumnNumber >= totalColumnsCount){
								dataColumnNumber = dataColumnNumber - totalColumnsCount + headerColumnNumber;
							}
							Cell contentCell = contentRow.getCell(dataColumnNumber);
							String cellValue = formatter.formatCellValue(contentCell);
							
							if(dataTemplate.containsValue(headerCellValueList.get(columnCounter))){
								if(cellValue != null && !cellValue.trim().isEmpty() && !"NA".equalsIgnoreCase(cellValue) && !"N/A".equalsIgnoreCase(cellValue)){
									if((dataTemplate.containsKey("project") && dataTemplate.get("project").equalsIgnoreCase(headerCellValueList.get(columnCounter)))){
										cellValue = cellValue.trim().replaceAll(" ", "_");
										reviewRecordCollection.setProject(cellValue);
										reviewRecordCollection.setActivityName(cellValue);
										reviewRecordCollection.setActivityComponent(cellValue);
									}
									collectionValuesScheduleMapper.setReviewRecordCollectionValues(reviewRecordCollection, dataTemplate, headerCellValueList.get(columnCounter), cellValue, contentCell);									
								}
							}
						}
						
						if(reviewRecordCollection.getProductName() != null && !reviewRecordCollection.getProductName().isEmpty()){
							reviewRecordCollections.add(reviewRecordCollection);
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
		return reviewRecordCollections;
	}
}
