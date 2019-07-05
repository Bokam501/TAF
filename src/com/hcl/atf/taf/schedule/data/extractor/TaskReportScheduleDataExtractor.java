package com.hcl.atf.taf.schedule.data.extractor;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hcl.atf.taf.model.ActivityCollection;
import com.hcl.atf.taf.model.DataExtractorNotification;

@Service
public class TaskReportScheduleDataExtractor {

	@Autowired
	CollectionValuesScheduleMapper collectionValuesScheduleMapper;
	
	private static final Log log = LogFactory.getLog(TaskReportScheduleDataExtractor.class);
	
	public List<ActivityCollection> readTaskReportExcel(LinkedHashMap<String, String> dataTemplate, List<String> headerCellValueList, Sheet sheet, int totalColumnsCount, DataExtractorNotification dataExtractorNotification) {
		List<ActivityCollection> activityCollections = new ArrayList<ActivityCollection>();
		try{
			log.info("Inside readTaskReportExcel()");
			
			int numberOfValidRecords = 0;
			int numberOfInvalidRecords = 0;
			
			int headerColumnNumber = Integer.parseInt(dataTemplate.get("Header Column Number")) - 1;
			int contentRowNumber = Integer.parseInt(dataTemplate.get("Content Row Number")) - 1;
			int totalRowsCount = sheet.getLastRowNum() + 1;
						
			ActivityCollection activityCollection = null;
			
			DataFormatter formatter = new DataFormatter();
			Row contentRow;
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MMM-yyyy");
			int dataColumnNumber = 0;
			for (int rowCounter = contentRowNumber; rowCounter < totalRowsCount; rowCounter++) {
				try{
					contentRow = sheet.getRow(rowCounter);
					if(contentRow != null){
						activityCollection = new ActivityCollection();
						activityCollection.setProductName(dataTemplate.get("scheduledProduct"));
						activityCollection.setProductId(Integer.parseInt(dataTemplate.get("scheduledProductId")));
						activityCollection.setCompetency(dataTemplate.get("scheduledCompetency"));
						activityCollection.setCompetencyId(Integer.parseInt(dataTemplate.get("scheduledCompetencyId")));
						activityCollection.setProject(dataTemplate.get("scheduledProduct"));
						activityCollection.setProjectId(Integer.parseInt(dataTemplate.get("scheduledProductId")));
						activityCollection.setCustomerName(dataTemplate.get("scheduledCustomer"));
						activityCollection.setCustomerId(Integer.parseInt(dataTemplate.get("scheduledCustomerId")));
						activityCollection.setTestFactoryName(dataTemplate.get("scheduledTestFactoryName"));
						activityCollection.setTestFactoryId(Integer.parseInt(dataTemplate.get("scheduledTestFactoryId")));
						activityCollection.setTestCentersName(dataTemplate.get("scheduledTestCentersName"));
						activityCollection.setTestCentersId(Integer.parseInt(dataTemplate.get("scheduledTestCentersId")));
						
						for (int columnCounter = 0; columnCounter < headerCellValueList.size(); columnCounter++) {
							dataColumnNumber = columnCounter + headerColumnNumber;
							while(dataColumnNumber >= totalColumnsCount){
								dataColumnNumber = dataColumnNumber - totalColumnsCount + headerColumnNumber;
							}
							Cell contentCell = contentRow.getCell(dataColumnNumber);
							String cellValue = formatter.formatCellValue(contentCell);
							
							if(dataTemplate.containsValue(headerCellValueList.get(columnCounter))){
								if(cellValue != null && !cellValue.trim().isEmpty() && !"NA".equalsIgnoreCase(cellValue) && !"N/A".equalsIgnoreCase(cellValue)){
									if(dataTemplate.containsKey("activityName") && dataTemplate.get("activityName").equalsIgnoreCase(headerCellValueList.get(columnCounter))){ // headerCellValueList.get(columnCounter).toLowerCase().startsWith(dataTemplate.get("activityName").toLowerCase())
										cellValue = cellValue.trim().replaceAll(" ", "_");
										activityCollection.setActivityName(cellValue);
									}else if(dataTemplate.containsKey("activityOwner") && dataTemplate.get("activityOwner").equalsIgnoreCase(headerCellValueList.get(columnCounter))){
										activityCollection.setActivityOwner(getRequiredResources(cellValue));
									}else if(dataTemplate.containsKey("activityReviewer1") && dataTemplate.get("activityReviewer1").equalsIgnoreCase(headerCellValueList.get(columnCounter))){
										activityCollection.setActivityReviewer1(getRequiredResources(cellValue));
									}else if(dataTemplate.containsKey("activityReviewer2") && dataTemplate.get("activityReviewer2").equalsIgnoreCase(headerCellValueList.get(columnCounter))){
										activityCollection.setActivityReviewer2(getRequiredResources(cellValue));
									}else if(dataTemplate.containsKey("activityReviewer3") && dataTemplate.get("activityReviewer3").equalsIgnoreCase(headerCellValueList.get(columnCounter))){
										activityCollection.setActivityReviewer3(getRequiredResources(cellValue));
									}else if(dataTemplate.containsKey("activityReviewer4") && dataTemplate.get("activityReviewer4").equalsIgnoreCase(headerCellValueList.get(columnCounter))){
										activityCollection.setActivityReviewer4(getRequiredResources(cellValue));
									}else if(dataTemplate.containsKey("activityReviewer5") && dataTemplate.get("activityReviewer5").equalsIgnoreCase(headerCellValueList.get(columnCounter))){
										activityCollection.setActivityReviewer5(getRequiredResources(cellValue));
									}else if(dataTemplate.containsKey("actualActivityStartDate") && dataTemplate.get("actualActivityStartDate").equalsIgnoreCase(headerCellValueList.get(columnCounter))){
										activityCollection.setActualActivityStartDate(simpleDateFormat.parse(cellValue));
									}else if(dataTemplate.containsKey("actualActivityEndDate") && dataTemplate.get("actualActivityEndDate").equalsIgnoreCase(headerCellValueList.get(columnCounter))){
										activityCollection.setActualActivityEndDate(simpleDateFormat.parse(cellValue));
									}else if(dataTemplate.containsKey("plannedActivityStartDate") && dataTemplate.get("plannedActivityStartDate").equalsIgnoreCase(headerCellValueList.get(columnCounter))){ // headerCellValueList.get(columnCounter).toLowerCase().startsWith(dataTemplate.get("plannedActivityStartDate").toLowerCase())
										activityCollection.setPlannedActivityStartDate(simpleDateFormat.parse(cellValue));
										activityCollection.setWeekDate(simpleDateFormat.parse(cellValue));
									}else if(dataTemplate.containsKey("plannedActivityEndDate") && dataTemplate.get("plannedActivityEndDate").equalsIgnoreCase(headerCellValueList.get(columnCounter))){ // headerCellValueList.get(columnCounter).toLowerCase().startsWith(dataTemplate.get("plannedActivityEndDate").toLowerCase())
										activityCollection.setPlannedActivityEndDate(simpleDateFormat.parse(cellValue));
									}else if(dataTemplate.containsKey("revisedActivityStartDate") && dataTemplate.get("revisedActivityStartDate").equalsIgnoreCase(headerCellValueList.get(columnCounter))){
										activityCollection.setRevisedActivityStartDate(simpleDateFormat.parse(cellValue));
									}else if(dataTemplate.containsKey("revisedActivityEndDate") && dataTemplate.get("revisedActivityEndDate").equalsIgnoreCase(headerCellValueList.get(columnCounter))){
										activityCollection.setRevisedActivityEndDate(simpleDateFormat.parse(cellValue));
									}else{
										collectionValuesScheduleMapper.setActivityCollectionValues(activityCollection, dataTemplate, headerCellValueList.get(columnCounter), cellValue, contentCell);									
									}
								}
							}
						}
						
						if(activityCollection.getProductName() != null && !activityCollection.getProductName().isEmpty() && activityCollection.getActivityName() != null && !activityCollection.getActivityName().isEmpty()){
							Float sva = 0.0F;
							Float eva = 0.0F;
							
							Integer numberOfWorkingDaysBetweenEnds = 0;
							Integer numberOfWorkingDaysBetweenPlannedStartAndEnd = 0;
							
							Date plannedOrRevisedStartDate = activityCollection.getPlannedActivityStartDate();
							Date plannedOrRevisedEndDate = activityCollection.getPlannedActivityEndDate();
							if(activityCollection.getRevisedActivityStartDate() != null){
								activityCollection.setWeekDate(activityCollection.getRevisedActivityStartDate());
								plannedOrRevisedStartDate = activityCollection.getRevisedActivityStartDate();
							}
							if(activityCollection.getRevisedActivityEndDate() != null){
								plannedOrRevisedEndDate = activityCollection.getRevisedActivityEndDate();
							}
							if(activityCollection.getActualActivityEndDate() != null && plannedOrRevisedEndDate != null){
								numberOfWorkingDaysBetweenEnds = getNumberOfWorkingDays(plannedOrRevisedEndDate, activityCollection.getActualActivityEndDate());
							}
							if(plannedOrRevisedStartDate != null && plannedOrRevisedEndDate != null){
								numberOfWorkingDaysBetweenPlannedStartAndEnd = getNumberOfWorkingDays(plannedOrRevisedStartDate, plannedOrRevisedEndDate);
							}
							
							if(plannedOrRevisedEndDate != null && activityCollection.getActualActivityEndDate() != null && activityCollection.getActualActivityEndDate().before(plannedOrRevisedEndDate)){
								sva = (((float)numberOfWorkingDaysBetweenEnds + 1) / (float)numberOfWorkingDaysBetweenPlannedStartAndEnd) * 100;
							}else if(plannedOrRevisedEndDate != null && activityCollection.getActualActivityEndDate() != null && activityCollection.getActualActivityEndDate().after(plannedOrRevisedEndDate)){
								sva = (((float)numberOfWorkingDaysBetweenEnds - 1) / (float)numberOfWorkingDaysBetweenPlannedStartAndEnd) * 100;
							}
							if(sva == Float.NEGATIVE_INFINITY || sva == Float.POSITIVE_INFINITY || sva == Float.NaN || Float.isNaN(sva) || Float.isInfinite(sva)){
								sva = 0.0F;
							}
							activityCollection.setsVa(sva);
							if(activityCollection.getRevisedActivityEffort() == 0.0F){
								eva = ((activityCollection.getActualActivityEffort() - activityCollection.getPlannedActivityEffort())/activityCollection.getPlannedActivityEffort())*100;
							}else{
								eva = ((activityCollection.getActualActivityEffort() - activityCollection.getRevisedActivityEffort())/activityCollection.getRevisedActivityEffort())*100;
							}
							if(eva == Float.NEGATIVE_INFINITY || eva == Float.POSITIVE_INFINITY || eva == Float.NaN || Float.isNaN(eva) || Float.isInfinite(eva)){
								eva = 0.0F;
							}
							activityCollection.seteVa(eva);
							activityCollections.add(activityCollection);
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
		return activityCollections;
	}
	
	private String getRequiredResources(String resources){
		String requiredResources = "";
		if(!"Skip".equalsIgnoreCase(resources)){
			String[] activityResources = resources.split(",");
			for(String resource : activityResources){
				if(resource.toLowerCase().contains("(Required)".toLowerCase())){
					resource = resource.split(Pattern.quote("(Required)"))[0];
					if(requiredResources == null || requiredResources.trim().isEmpty()){
						requiredResources = resource.trim();
					}else{
						requiredResources += ", "+resource.trim();
					}
				}
			}
		}
		return requiredResources.trim();
	}
	
	private Integer getNumberOfWorkingDays(Date startDate, Date endDate){
		Integer numberOfWorkingDays = 0;
		Calendar startDateCalendar = Calendar.getInstance();
		Calendar endDateCalendar = Calendar.getInstance();

		boolean isNegative = false;
		
		if(startDate.before(endDate)){
			startDateCalendar.setTime(startDate);
			endDateCalendar.setTime(endDate);
		}else{
			startDateCalendar.setTime(endDate);
			endDateCalendar.setTime(startDate);
			isNegative = true;
		}
		
		if(startDateCalendar.equals(endDateCalendar)){
			if(startDateCalendar.get(Calendar.DAY_OF_WEEK) != Calendar.SATURDAY && startDateCalendar.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY){
				numberOfWorkingDays = 1;
			}
		}else{
			while(startDateCalendar.before(endDateCalendar) || startDateCalendar.equals(endDateCalendar)){
				if(startDateCalendar.get(Calendar.DAY_OF_WEEK) != Calendar.SATURDAY && startDateCalendar.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY){
					numberOfWorkingDays = numberOfWorkingDays+1;
				}
				startDateCalendar.add(Calendar.DAY_OF_MONTH, 1);
			}
		}
		if(isNegative){
			numberOfWorkingDays = numberOfWorkingDays * -1;
		}
		return numberOfWorkingDays;
	}
	
}
