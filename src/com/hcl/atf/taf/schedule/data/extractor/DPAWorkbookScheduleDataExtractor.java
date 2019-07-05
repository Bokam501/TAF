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

import com.hcl.atf.taf.model.DPAWorkbookCollection;
import com.hcl.atf.taf.model.DataExtractorNotification;

@Service
public class DPAWorkbookScheduleDataExtractor {

	@Autowired
	CollectionValuesScheduleMapper collectionValuesScheduleMapper;
	
	private static final Log log = LogFactory.getLog(DPAWorkbookScheduleDataExtractor.class);
	
	public List<DPAWorkbookCollection> readDPAWorkbookExcel(LinkedHashMap<String, String> dataTemplate, ArrayList<String> headerCellValueList, Sheet sheet, int totalColumnsCount, DataExtractorNotification dataExtractorNotification) {
		List<DPAWorkbookCollection> dpaWorkbookCollections = new ArrayList<DPAWorkbookCollection>();
		try{
			log.info("Inside readDPAWorkbookExcel()");

			int numberOfValidRecords = 0;
			int numberOfInvalidRecords = 0;
			
			int headerColumnNumber = Integer.parseInt(dataTemplate.get("Header Column Number")) - 1;
			int contentRowNumber = Integer.parseInt(dataTemplate.get("Content Row Number")) - 1;
			int totalRowsCount = sheet.getLastRowNum() + 1;

			Integer headerSize = headerCellValueList.size();
			List<Integer> groupingHeaderIndex = new ArrayList<Integer>();
			int j = 0;
			for(int i = 0; i < headerSize; i++){
				j = i + 1;
				if(headerCellValueList.get(i) != null && !headerCellValueList.get(i).trim().isEmpty() && j < headerSize && (headerCellValueList.get(j) == null || headerCellValueList.get(j).trim().isEmpty())){
					groupingHeaderIndex.add(i);
				}
			}
			
			Integer groupingIndexSize = 0;
			if(groupingHeaderIndex != null){
				groupingIndexSize = groupingHeaderIndex.size();
			}
			
			DPAWorkbookCollection dpaWorkbookCollection = null;
			
			DataFormatter formatter = new DataFormatter();
			Row contentRow;
			int dataColumnNumber = 0;
			String groupingType = "";
			for (int rowCounter = contentRowNumber; rowCounter < totalRowsCount; rowCounter++) {
				try{
					contentRow = sheet.getRow(rowCounter);
					if(contentRow != null){
						dpaWorkbookCollection = new DPAWorkbookCollection();
						dpaWorkbookCollection.setProductName(dataTemplate.get("scheduledProduct"));
						dpaWorkbookCollection.setProductId(Integer.parseInt(dataTemplate.get("scheduledProductId")));
						dpaWorkbookCollection.setCompetency(dataTemplate.get("scheduledCompetency"));
						dpaWorkbookCollection.setCompetencyId(Integer.parseInt(dataTemplate.get("scheduledCompetencyId")));
						dpaWorkbookCollection.setProject(dataTemplate.get("scheduledProduct"));
						dpaWorkbookCollection.setProjectId(Integer.parseInt(dataTemplate.get("scheduledProductId")));
						dpaWorkbookCollection.setCustomerName(dataTemplate.get("scheduledCustomer"));
						dpaWorkbookCollection.setCustomerId(Integer.parseInt(dataTemplate.get("scheduledCustomerId")));
						dpaWorkbookCollection.setTestFactoryName(dataTemplate.get("scheduledTestFactoryName"));
						dpaWorkbookCollection.setTestFactoryId(Integer.parseInt(dataTemplate.get("scheduledTestFactoryId")));
						dpaWorkbookCollection.setTestCentersName(dataTemplate.get("scheduledTestCentersName"));
						dpaWorkbookCollection.setTestCentersId(Integer.parseInt(dataTemplate.get("scheduledTestCentersId")));	
						
						for (int columnCounter = 0; columnCounter < headerCellValueList.size(); columnCounter++) {
							dataColumnNumber = columnCounter + headerColumnNumber;
							while(dataColumnNumber >= totalColumnsCount){
								dataColumnNumber = dataColumnNumber - totalColumnsCount + headerColumnNumber;
							}
							Cell contentCell = contentRow.getCell(dataColumnNumber);
							String cellValue = formatter.formatCellValue(contentCell);
							
							if(dataTemplate.containsValue(headerCellValueList.get(columnCounter))){
								if(cellValue != null && !cellValue.trim().isEmpty() && !"NA".equalsIgnoreCase(cellValue) && !"N/A".equalsIgnoreCase(cellValue)){
									if(groupingIndexSize >= 2){
										if(dataColumnNumber >= groupingHeaderIndex.get(0) && dataColumnNumber < groupingHeaderIndex.get(1)){
											groupingType = "Corrective";
										}else if(dataColumnNumber >= groupingHeaderIndex.get(1)){
											groupingType = "Preventive";
										}										
									}else{
										groupingType = "";
									}
									collectionValuesScheduleMapper.setDPAWorkbookCollectionValues(dpaWorkbookCollection, dataTemplate, headerCellValueList.get(columnCounter), cellValue, contentCell, groupingType);									
								}
							}
						}
						
						if(dpaWorkbookCollection.getDpaId() != null && !dpaWorkbookCollection.getDpaId().isEmpty()){
							dpaWorkbookCollections.add(dpaWorkbookCollection);
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
		return dpaWorkbookCollections;
	}
}
