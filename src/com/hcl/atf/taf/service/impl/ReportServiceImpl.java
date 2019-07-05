package com.hcl.atf.taf.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hcl.atf.taf.controller.utilities.DateUtility;
import com.hcl.atf.taf.dao.ProductUserRoleDAO;
import com.hcl.atf.taf.dao.ReportDAO;
import com.hcl.atf.taf.dao.ResourceAvailabilityDAO;
import com.hcl.atf.taf.dao.ResourcePerformanceDAO;
import com.hcl.atf.taf.dao.TimeSheetManagementDAO;
import com.hcl.atf.taf.dao.UserCustomerAccountDAO;
import com.hcl.atf.taf.dao.WorkPackageDAO;
import com.hcl.atf.taf.dao.WorkShiftMasterDAO;
import com.hcl.atf.taf.model.Activity;
import com.hcl.atf.taf.model.ActualShift;
import com.hcl.atf.taf.model.MongoCollection;
import com.hcl.atf.taf.model.PivotRestTemplate;
import com.hcl.atf.taf.model.ReportIssue;
import com.hcl.atf.taf.model.ResourceAvailability;
import com.hcl.atf.taf.model.ResourceDailyPerformance;
import com.hcl.atf.taf.model.TaskEffortTemplate;
import com.hcl.atf.taf.model.UserCustomerAccount;
import com.hcl.atf.taf.model.WorkPackageTestCaseExecutionPlan;
import com.hcl.atf.taf.model.dto.PivotRestTemplateDTO;
import com.hcl.atf.taf.model.dto.TimeSheetStatisticsDTO;
import com.hcl.atf.taf.model.dto.WorkPackageResultsStatisticsDTO;
import com.hcl.atf.taf.model.json.JsonActivityReport;
import com.hcl.atf.taf.model.json.JsonTimeSheetStatistics;
import com.hcl.atf.taf.model.json.JsonWorkPackageResultsStatistics;
import com.hcl.atf.taf.service.ReportService;

@Service
public class ReportServiceImpl implements ReportService {

	private static final Log log = LogFactory.getLog(ReportServiceImpl.class);

	@Autowired
	private ReportDAO reportDAO;
	@Autowired
	private TimeSheetManagementDAO timeSheetManagementDAO;
	@Autowired
	private ProductUserRoleDAO productUserRoleDAO;
	@Autowired
	private ResourceAvailabilityDAO resourceAvailabilityDAO;
	@Autowired
	private WorkShiftMasterDAO workShiftMasterDAO;
	@Autowired
	private WorkPackageDAO workPackageDAO;
	@Autowired
	private ResourcePerformanceDAO resourcePerformanceDAO;
	@Autowired
	UserCustomerAccountDAO userCustomerAccountDAO;
	
	@Value("#{ilcmProps['SHIFT_ATTENDANCE_GRACE_TIME_IN_MINUTES']}")
    private String shiftAttendanceGraceTimeInMinutesConfigured;
	
	@Value("#{ilcmProps['SHIFT_LUNCH_AUTHORISED_TIME_IN_MINUTES']}")
    private String shiftLunchAuthorisedTimeInMinutesConfigured;

	@Value("#{ilcmProps['SHIFT_BREAKS_AUTHORISED_TIME_IN_MINUTES']}")
    private String shiftBreaksAuthorisedTimeInMinutesConfigured;

	@Value("#{ilcmProps['WEEKLY_OVERTIME_LIMIT_IN_MINUTES']}")
    private String weeklyOverTimeLimitInMinutesConfigured;
	
	@Override
	@Transactional
	public List<WorkPackageTestCaseExecutionPlan> listWorkPackageTestCasesExecutionPlanResultsEod(Integer workPackageId, Integer shiftId, Date executionDate,
			int jtStartIndex, int jtPageSize) {

		return reportDAO.listWorkPackageTestCasesExecutionPlanResultsEod(workPackageId,	shiftId, executionDate, jtStartIndex, jtPageSize);
	}

	@Override
	@Transactional
	public List<JsonWorkPackageResultsStatistics> listWorkPackageStatisticsEOD(Integer workPackageId, Integer shiftId, Date executionDate) {
		List<WorkPackageResultsStatisticsDTO> workPackageResultsStatisticsDTOList = reportDAO.listWorkPackageStatisticsEOD(workPackageId, shiftId,	executionDate);

		List<JsonWorkPackageResultsStatistics> jsonWorkPackageResultsStatisticsList = new ArrayList<JsonWorkPackageResultsStatistics>();

		if (workPackageResultsStatisticsDTOList == null){
			return null;
		}
		for(WorkPackageResultsStatisticsDTO workPackageResultsStatisticsDTO : workPackageResultsStatisticsDTOList) {
			JsonWorkPackageResultsStatistics jsonWorkPackageResultsStatistics = new JsonWorkPackageResultsStatistics();

			jsonWorkPackageResultsStatistics.setWorkPackageId(workPackageResultsStatisticsDTO.getWorkPackageId());
			jsonWorkPackageResultsStatistics.setWorkPackageName(workPackageResultsStatisticsDTO.getWorkPackageName());

			jsonWorkPackageResultsStatistics.setTestLeadId(workPackageResultsStatisticsDTO.getTestLeadId());
			jsonWorkPackageResultsStatistics.setTestLeadName(workPackageResultsStatisticsDTO.getTestLeadName());

			jsonWorkPackageResultsStatistics.setTesterId(workPackageResultsStatisticsDTO.getTesterId());
			jsonWorkPackageResultsStatistics.setTesterName(workPackageResultsStatisticsDTO.getTesterName());

			if (workPackageResultsStatisticsDTO.getActualExecutionDate() != null) {
				jsonWorkPackageResultsStatistics.setActualExecutionDate(DateUtility.dateformatWithOutTime(workPackageResultsStatisticsDTO.getActualExecutionDate()));
			}

			jsonWorkPackageResultsStatistics.setShiftId(workPackageResultsStatisticsDTO.getShiftId());
			
			jsonWorkPackageResultsStatistics.setShiftName(workPackageResultsStatisticsDTO.getShiftName());
			
			jsonWorkPackageResultsStatistics.setDefectsCount(workPackageResultsStatisticsDTO.getDefectsCount());
			jsonWorkPackageResultsStatistics.setTestCasesCount(workPackageResultsStatisticsDTO.getTestCasesCount());

			ActualShift actualShift = workShiftMasterDAO.listshiftManage(shiftId, executionDate);
			Integer actualShiftId =0;
			if (actualShift != null){
				actualShiftId = actualShift.getActualShiftId();
				jsonWorkPackageResultsStatistics.setActualShiftId(actualShift.getActualShiftId());
				
				// Added for getting Performance rating for user on a workPackage on a Shift and on a given WorkDate
				ResourceDailyPerformance resourceDailyPerformance = resourcePerformanceDAO.getResourceDailyPerformance(workPackageResultsStatisticsDTO.getTesterId(), 
																	executionDate, actualShiftId, workPackageId);
				
				if (resourceDailyPerformance != null) {
					if (resourceDailyPerformance.getPerformanceLevel() != null)
						jsonWorkPackageResultsStatistics.setRatings(resourceDailyPerformance.getPerformanceLevel().getLevelValue());

					jsonWorkPackageResultsStatistics.setRaterComments(resourceDailyPerformance.getRaterComments());
					if (resourceDailyPerformance.getRatedOn() != null) 
						jsonWorkPackageResultsStatistics.setRatedOn(DateUtility.dateToStringInSecond(resourceDailyPerformance.getRatedOn()));
					
					if (resourceDailyPerformance.getRatedByUser() != null){
						jsonWorkPackageResultsStatistics.setRatedByUserId(resourceDailyPerformance.getRatedByUser().getUserId());
						jsonWorkPackageResultsStatistics.setRatedByUserName(resourceDailyPerformance.getRatedByUser().getUserDisplayName());
					}	
					
					jsonWorkPackageResultsStatistics.setIsRatingApproved(resourceDailyPerformance.getIsRatingApproved());
					
					jsonWorkPackageResultsStatistics.setRatingApproverComments(resourceDailyPerformance.getApproverComments());
					if (resourceDailyPerformance.getApprovedOn() != null) 
						jsonWorkPackageResultsStatistics.setRatingApprovedOn(DateUtility.dateToStringInSecond(resourceDailyPerformance.getApprovedOn()));

					if (resourceDailyPerformance.getApprovedByUser() != null){
						jsonWorkPackageResultsStatistics.setRatingApprovedByUserId(resourceDailyPerformance.getApprovedByUser().getUserId());
						jsonWorkPackageResultsStatistics.setRatingApprovedByUserName(resourceDailyPerformance.getApprovedByUser().getUserDisplayName());
					}						
				}
			}
			
			// Added for getting Performance rating for user on a workPackage on a Shift and on a given WorkDate
			ResourceDailyPerformance resourceDailyPerformance = resourcePerformanceDAO.getResourceDailyPerformance(workPackageResultsStatisticsDTO.getTesterId(), 
																executionDate, actualShiftId, workPackageId);
			//Added the below code as Actual Shift is null always - Starts
			if (resourceDailyPerformance != null) {
				if (resourceDailyPerformance.getPerformanceLevel() != null)
					jsonWorkPackageResultsStatistics.setRatings(resourceDailyPerformance.getPerformanceLevel().getLevelValue());

				jsonWorkPackageResultsStatistics.setRaterComments(resourceDailyPerformance.getRaterComments());
				if (resourceDailyPerformance.getRatedOn() != null) 
					jsonWorkPackageResultsStatistics.setRatedOn(DateUtility.dateToStringInSecond(resourceDailyPerformance.getRatedOn()));
				
				if (resourceDailyPerformance.getRatedByUser() != null){
					jsonWorkPackageResultsStatistics.setRatedByUserId(resourceDailyPerformance.getRatedByUser().getUserId());
					jsonWorkPackageResultsStatistics.setRatedByUserName(resourceDailyPerformance.getRatedByUser().getUserDisplayName());
				}	
				
				jsonWorkPackageResultsStatistics.setIsRatingApproved(resourceDailyPerformance.getIsRatingApproved());
				
				jsonWorkPackageResultsStatistics.setRatingApproverComments(resourceDailyPerformance.getApproverComments());
				if (resourceDailyPerformance.getApprovedOn() != null) 
					jsonWorkPackageResultsStatistics.setRatingApprovedOn(DateUtility.dateToStringInSecond(resourceDailyPerformance.getApprovedOn()));

				if (resourceDailyPerformance.getApprovedByUser() != null){
					jsonWorkPackageResultsStatistics.setRatingApprovedByUserId(resourceDailyPerformance.getApprovedByUser().getUserId());
					jsonWorkPackageResultsStatistics.setRatingApprovedByUserName(resourceDailyPerformance.getApprovedByUser().getUserDisplayName());
				}						
			}
			//Added the below code as Actual Shift is null always - Starts
			//Hours worked today
			HashMap<String, Integer> timeMap = new HashMap<String, Integer>();
			timeMap = timeSheetManagementDAO.getTimeSheetEntryForWorkPackageStatisticsReportEOD(
							workPackageId, shiftId, executionDate, workPackageResultsStatisticsDTO.getTesterId());
			String totalTimeConvertedInHoursMin = null;
			if (timeMap != null) {
				totalTimeConvertedInHoursMin = DateUtility.convertTimeInHoursMinutes(timeMap.get("totalHours"), timeMap.get("totalMinutes"));
			}else{
				totalTimeConvertedInHoursMin = "0";
			}

			jsonWorkPackageResultsStatistics.setHoursToday(totalTimeConvertedInHoursMin);

			jsonWorkPackageResultsStatisticsList.add(jsonWorkPackageResultsStatistics);
		}
		return jsonWorkPackageResultsStatisticsList;
	}

	@Override
	@Transactional
	public List<JsonTimeSheetStatistics> listTimeSheetStatistics(Date executionDateFrom, Date executionDateTo) {

		// If the report is requested from in between weekdays, get the report from weekstart day (Monday)
		if (DateUtility.getDayOfWeek(executionDateFrom) != 2) {  // 2 means Monday - The start day of a week as per the application
			executionDateFrom = DateUtility.getWeekStart(executionDateFrom);
		}	

		List<TimeSheetStatisticsDTO> timeSheetStatisticsDTOList =  reportDAO.listCompleteTimeSheetStatistics(executionDateFrom, executionDateTo);

		if (timeSheetStatisticsDTOList == null)
		{
			return null;
		}	
		
		HashMap<Integer,String> currentHighestRatingUserWise = new HashMap<Integer,String>(); // This HashMap won't get resetted on change of Date/Week
		HashMap<String,String> customerCodeCustomerWiseUserWise = new HashMap<String,String>(); // This HashMap won't get resetted on change of Date/Week
		
		HashMap<Integer,Integer> timeMapWeekUserWisePrevRecord = new HashMap<Integer,Integer>();
		HashMap<Integer,Integer> timeMapDayUserWisePrevRecord = new HashMap<Integer,Integer>();
		HashMap<Integer,Integer> timeMapShiftUserWisePrevRecord = new HashMap<Integer,Integer>();
		
		HashMap<String,Integer> timeMapWeekProductWiseUserWisePrevRecord = new HashMap<String,Integer>();
		
		HashMap<Integer,Integer> timeMapBreaksOfTheShiftUserWisePrevRecord = new HashMap<Integer,Integer>();
		HashMap<Integer,Integer> timeMapLunchOfTheShiftUserWisePrevRecord = new HashMap<Integer,Integer>();
		
		int previousRecordWeekNumber = 0;
		int currentRecordWeekNumber = 0;

		int previousRecordDayNumber = 0;
		int currentRecordDayNumber = 0;

		String previousRecordShift = null;
		String currentRecordShift = null;
		
		List<JsonTimeSheetStatistics> jsonTimeSheetStatisticsList =  new ArrayList<JsonTimeSheetStatistics>();
			
		for(TimeSheetStatisticsDTO timeSheetStatisticsDTO: timeSheetStatisticsDTOList)
		{
			
			// Logic to reset 'Weekwise' user data
			currentRecordWeekNumber = DateUtility.getWeekNumberOfDateOnAYear(timeSheetStatisticsDTO.getActualExecutionDate());

			if (currentRecordWeekNumber != previousRecordWeekNumber) {  // If they are different weeks, reset the HashMap values  
				timeMapWeekUserWisePrevRecord = new HashMap<Integer,Integer>();   //Weekwise Timesheet data
				timeMapWeekProductWiseUserWisePrevRecord = new HashMap<String,Integer>(); //Weekwise Timesheet data on a productId
				previousRecordWeekNumber = currentRecordWeekNumber;
			}

			// Logic to reset 'Daywise' user data
			currentRecordDayNumber = DateUtility.getDayNumberOfDateOnAYear(timeSheetStatisticsDTO.getActualExecutionDate());

			if (currentRecordDayNumber != previousRecordDayNumber) {  // If they are different days, reset the HashMap values  
				timeMapDayUserWisePrevRecord = new HashMap<Integer,Integer>();
				previousRecordDayNumber = currentRecordDayNumber;
			}

			// Logic to reset 'Shiftwise' user data (A shift a combination of Date and Shift i.e Shift on a day)
			currentRecordShift = timeSheetStatisticsDTO.getActualExecutionDate()+"-"+ timeSheetStatisticsDTO.getShiftId();
			if (!currentRecordShift.equals(previousRecordShift)) {  // If they are different days, reset the HashMap values
				timeMapLunchOfTheShiftUserWisePrevRecord = new HashMap<Integer,Integer>(); // Added for Lunch calculation
				timeMapBreaksOfTheShiftUserWisePrevRecord = new HashMap<Integer,Integer>(); // Added for Breaks calculation
				
				timeMapShiftUserWisePrevRecord = new HashMap<Integer,Integer>(); // Added for Shiftwise Timesheet Work Hours
				previousRecordShift = currentRecordShift;
			}

			
			JsonTimeSheetStatistics jsonTimeSheetStatistics =  new JsonTimeSheetStatistics();

			//Field Name: 'ActualExecutionDate'
			if(timeSheetStatisticsDTO.getActualExecutionDate()!=null){
				jsonTimeSheetStatistics.setActualExecutionDate(DateUtility.sdfDateformatWithOutTime(timeSheetStatisticsDTO.getActualExecutionDate()));
			}

			//Field Name: 'ShiftId'
			// AND few more fields
			jsonTimeSheetStatistics.setShiftId(timeSheetStatisticsDTO.getShiftId());
			jsonTimeSheetStatistics.setShiftName(timeSheetStatisticsDTO.getShiftName());

			jsonTimeSheetStatistics.setTesterId(timeSheetStatisticsDTO.getTesterId());
			jsonTimeSheetStatistics.setTesterName(timeSheetStatisticsDTO.getTesterName());

			
			if(timeSheetStatisticsDTO.getUserLastName()!=null && timeSheetStatisticsDTO.getUserFirstName()!=null)
				jsonTimeSheetStatistics.setFullNameLF(timeSheetStatisticsDTO.getUserLastName()+","+timeSheetStatisticsDTO.getUserFirstName());
			else if(timeSheetStatisticsDTO.getUserLastName()!=null && timeSheetStatisticsDTO.getUserFirstName()==null)
				jsonTimeSheetStatistics.setFullNameLF(timeSheetStatisticsDTO.getUserLastName());
			else if(timeSheetStatisticsDTO.getUserLastName()==null && timeSheetStatisticsDTO.getUserFirstName()!=null)
				jsonTimeSheetStatistics.setFullNameLF(timeSheetStatisticsDTO.getUserFirstName());
			else
				jsonTimeSheetStatistics.setFullNameLF("");
			
			jsonTimeSheetStatistics.setVendorName(timeSheetStatisticsDTO.getVendorName());

			jsonTimeSheetStatistics.setTestFactoryName(timeSheetStatisticsDTO.getTestFactoryName());
			jsonTimeSheetStatistics.setProductName(timeSheetStatisticsDTO.getProductName());
			jsonTimeSheetStatistics.setProjectCode(timeSheetStatisticsDTO.getProjectCode());
			jsonTimeSheetStatistics.setWorkPackageId(timeSheetStatisticsDTO.getWorkPackageId());
			jsonTimeSheetStatistics.setWorkPackageName(timeSheetStatisticsDTO.getWorkPackageName());
			
			jsonTimeSheetStatistics.setRoleName(timeSheetStatisticsDTO.getUserRoleName());
			
			jsonTimeSheetStatistics.setProductResourceShiftCheckInDateTime(DateUtility.dateToStringInSecond(timeSheetStatisticsDTO.getProductResourceShiftCheckIn()));
			if (timeSheetStatisticsDTO.getProductResourceShiftCheckOut() != null)
				jsonTimeSheetStatistics.setProductResourceShiftCheckOutDateTime(DateUtility.dateToStringInSecond(timeSheetStatisticsDTO.getProductResourceShiftCheckOut()));
			
			String totalTimeConvertedInHoursMinutes = null;
			Integer timeStatus  = 0;
			
			//Field Name: 'Project WorkDuration'  // Based on difference between 'Project Resource Checkin' and 'Project Resource Checkout' timings
			if (timeSheetStatisticsDTO.getProductResourceShiftCheckIn() != null && timeSheetStatisticsDTO.getProductResourceShiftCheckOut() != null){
				
				timeStatus = timeSheetStatisticsDTO.getProductResourceShiftCheckIn().compareTo(timeSheetStatisticsDTO.getProductResourceShiftCheckOut());
				if 	(timeStatus.intValue() < 0){
					long projectWorkDurationInMinutes = DateUtility.DateDifferenceInMinutes(
							timeSheetStatisticsDTO.getProductResourceShiftCheckIn(), timeSheetStatisticsDTO.getProductResourceShiftCheckOut());
					totalTimeConvertedInHoursMinutes = DateUtility.convertTimeInHoursMinutes(0, new Integer((int)projectWorkDurationInMinutes));
					log.info("totalTimeConvertedInHoursMinutes for Project Work Duration=" + totalTimeConvertedInHoursMinutes);
					jsonTimeSheetStatistics.setProductResourceShiftWorkDuration(totalTimeConvertedInHoursMinutes);	
				}
			}

			//***********************************************************************************************************

			
			//***********************************************************************************************************

			//Field Name: 'Customer Id'  (Code given by a customer to the user for their products/application)
			// If Hashmap value is available re-use the value otherwise get from DB and set the value for further re-use.
			String testerCustomerKey = timeSheetStatisticsDTO.getTesterId() + "-" + timeSheetStatisticsDTO.getCustomerId();
			log.info("testerCustomerKey="+testerCustomerKey);
			if (customerCodeCustomerWiseUserWise.get(testerCustomerKey) == null){
				log.info("Customer Code not stored earlier");
				UserCustomerAccount userCustomerAccount = userCustomerAccountDAO.getUserCustomerAccountByUserIdCustomerId(
											timeSheetStatisticsDTO.getTesterId(), timeSheetStatisticsDTO.getCustomerId());
				
				if (userCustomerAccount != null && userCustomerAccount.getUserCustomerCode() != null){
					log.info("Customer Code is being stored now");
					log.info("userCustomer Code="+userCustomerAccount.getUserCustomerCode());
					customerCodeCustomerWiseUserWise.put(testerCustomerKey, userCustomerAccount.getUserCustomerCode());
				}
			}
			
			if (customerCodeCustomerWiseUserWise.containsKey(testerCustomerKey))
				jsonTimeSheetStatistics.setCustomerCode(customerCodeCustomerWiseUserWise.get(testerCustomerKey));
					
			//***********************************************************************************************************
			
			
			//***********************************************************************************************************
			//Field Name: 'Lead'
			
			//Logic: Get LeadName from WorkPackageTestCaseExecutionPlan for 'workPackageId, shiftId, executionDate, testerId'
			//if not available get LeadName from WorkPackageTestCaseExecutionPlan for 'workPackageId, shiftId, executionDate'
			
			String testLeadSpocName = workPackageDAO.getWptcepTestLeadSpocName(timeSheetStatisticsDTO.getWorkPackageId(),
					timeSheetStatisticsDTO.getShiftTypeId(), timeSheetStatisticsDTO.getActualExecutionDate(), timeSheetStatisticsDTO.getTesterId());

			if (testLeadSpocName == null){
				log.info("testLeadSpocName - not available from WorkPackageTestCaseExecutionPlan for 'workPackageId, shiftId, executionDate, testerId");
				log.info("testLeadSpocName - gettting from WorkPackageTestCaseExecutionPlan for 'workPackageId, shiftId, executionDate");
				testLeadSpocName = workPackageDAO.getWptcepTestLeadSpocName(timeSheetStatisticsDTO.getWorkPackageId(),
						timeSheetStatisticsDTO.getShiftTypeId(), timeSheetStatisticsDTO.getActualExecutionDate());
			}	
			
			log.info("testLeadSpocName="+testLeadSpocName);
			jsonTimeSheetStatistics.setTestLeadName(testLeadSpocName);
			//***********************************************************************************************************

			
			//***********************************************************************************************************
			///Field Name: 'NextDay Confirmation'
			
			boolean isResourceAvailableNextDay = resourceAvailabilityDAO.checkResourceAvailabilityForADate(
					timeSheetStatisticsDTO.getTesterId(), DateUtility.getNextDate(timeSheetStatisticsDTO.getActualExecutionDate()));
			
			if (isResourceAvailableNextDay)	
				jsonTimeSheetStatistics.setNextDayConfirmation("Confirmed in Lab");
			else
				jsonTimeSheetStatistics.setNextDayConfirmation("Not Confirmed in Lab");
			//***********************************************************************************************************

			
			//***********************************************************************************************************
			//Field Name: 'ShiftStart' AND Field Name: 'ShiftEnd' AND Field Name: 'ShiftDuration'
			
			Date shiftStartDateTime = null;
			Date shiftEndDateTime = null;
			
			String shiftEndTime = null;
			String actualExecutionDate  = null;
			
			shiftStartDateTime = timeSheetStatisticsDTO.getActualShiftStartDateTime();
			if (shiftStartDateTime != null) 
					jsonTimeSheetStatistics.setShiftStartDateTime(DateUtility.dateToStringInSecond(shiftStartDateTime));
			
			shiftEndDateTime = timeSheetStatisticsDTO.getActualShiftEndDateTime();
			if (shiftEndDateTime == null){
				// If data not available in ActualShift, use EndTime data available in WorkShiftMaster
				if (timeSheetStatisticsDTO.getWorkShiftEndDateTime() != null && timeSheetStatisticsDTO.getActualExecutionDate() != null) {
					log.info("timeSheetStatisticsDTO.getShiftEnd()="+timeSheetStatisticsDTO.getWorkShiftEndDateTime());
					
					shiftEndTime = DateUtility.getTimeStampinHHmmss(timeSheetStatisticsDTO.getWorkShiftEndDateTime());
					actualExecutionDate = DateUtility.sdfDateformatWithOutTime(timeSheetStatisticsDTO.getActualExecutionDate());
					log.info("ShiftEndDateTime from WorkShift=" + actualExecutionDate+ " " + shiftEndTime);
					shiftEndDateTime = DateUtility.toDateInSec(actualExecutionDate+ " " + shiftEndTime);
				}
			}
				
			if (shiftEndDateTime == null){
				shiftEndDateTime = DateUtility.addHoursToDate(shiftStartDateTime, 8);  // If end time not available add 8 hours default duration
			}
			jsonTimeSheetStatistics.setShiftEndDateTime(DateUtility.dateToStringInSecond(shiftEndDateTime));
			
			
			totalTimeConvertedInHoursMinutes = null;
			
			//Field Name: 'ShiftDuration'
			if (shiftStartDateTime != null && shiftEndDateTime != null){

				timeStatus = shiftStartDateTime.compareTo(shiftEndDateTime);
				if 	(timeStatus.intValue() < 0){
					long shiftDurationInMinutes = DateUtility.DateDifferenceInMinutes(shiftStartDateTime, shiftEndDateTime);
					totalTimeConvertedInHoursMinutes = DateUtility.convertTimeInHoursMinutes(0, new Integer((int)shiftDurationInMinutes));
					log.info("totalTimeConvertedInHoursMinutes for Shift Duration=" + totalTimeConvertedInHoursMinutes);
					jsonTimeSheetStatistics.setShiftDuration(totalTimeConvertedInHoursMinutes);	
				}
			}
			//***********************************************************************************************************
			
			
			//***********************************************************************************************************
			//Field Names:
			//Field Name: '(Attendance) Status' AND Field Name: '(Attendance) Start Time' AND
			//Field Name: '(Attendance) End Time' AND Field Name: '(Attendance) Duration'  
			
			ResourceAvailability resourceAvailability = resourceAvailabilityDAO.getResourceAvailability(
					timeSheetStatisticsDTO.getTesterId(), timeSheetStatisticsDTO.getActualExecutionDate(),timeSheetStatisticsDTO.getShiftTypeId());
			
			log.info("resourceAvailability="+resourceAvailability);
			if (resourceAvailability != null){	

				//Field Name: Attendance
				if (resourceAvailability.getAttendanceCheckInTime() == null){	
					jsonTimeSheetStatistics.setAttendanceStatus("Not Entered");

					//continue;  // This continue statement is commented so as to accomodate TestManager in reports. If made continue TestManager data won't reflect.
				}	
				else if (shiftStartDateTime != null) {	
					
					Integer shiftAttendanceGraceTimeInMinutes = 0;// Default value   
					
					if (timeSheetStatisticsDTO.getShiftAttendanceGraceTime() != null){
						shiftAttendanceGraceTimeInMinutes = timeSheetStatisticsDTO.getShiftAttendanceGraceTime();
					}else{
						if (shiftAttendanceGraceTimeInMinutesConfigured != null){  // If the value given in property is not null
							try{
								shiftAttendanceGraceTimeInMinutes = new Integer(shiftAttendanceGraceTimeInMinutesConfigured);
							}
							catch (Exception e){
								shiftAttendanceGraceTimeInMinutes = 0;  // Setting to Default value
							}
						}
					}
					
					Date shiftStartDateTimeWithGraceTime = null;
					
					if (shiftAttendanceGraceTimeInMinutes.intValue() == 0){
						shiftStartDateTimeWithGraceTime = shiftStartDateTime;
					}else{
						shiftStartDateTimeWithGraceTime =  DateUtility.addMinutesToDate(shiftStartDateTime, shiftAttendanceGraceTimeInMinutes.intValue());
					}
					
					int checkInTimeStatus = 0;
					
					if (shiftStartDateTimeWithGraceTime != null){
						checkInTimeStatus = shiftStartDateTimeWithGraceTime.compareTo(resourceAvailability.getAttendanceCheckInTime());
						log.info("shiftStartDateTime="+shiftStartDateTime);
						log.info("shiftAttendanceGraceTimeInMinutesIntValue="+shiftAttendanceGraceTimeInMinutes);
						log.info("shiftStartDateTimeWithGraceTime="+shiftStartDateTimeWithGraceTime);
						log.info("resourceAvailability.getAttendanceCheckInTime()="+resourceAvailability.getAttendanceCheckInTime());
						log.info("checkInTimeStatus="+checkInTimeStatus);
						
						if 	(checkInTimeStatus < 0)
							jsonTimeSheetStatistics.setAttendanceStatus("Delayed");
						else if (checkInTimeStatus >= 0)
							jsonTimeSheetStatistics.setAttendanceStatus("On Time");
					}
				}

				//Field Name: AttendanceStartDateTime
				log.info("resourceAvailability.getAttendanceCheckInTime()="+resourceAvailability.getAttendanceCheckInTime());
				if (resourceAvailability.getAttendanceCheckInTime() != null)
					jsonTimeSheetStatistics.setAttendanceStartDateTime(DateUtility.dateToStringInSecond(resourceAvailability.getAttendanceCheckInTime()));
				
				//Field Name: AttendanceEndDateTime
				log.info("resourceAvailability.getAttendanceCheckOutTime()="+resourceAvailability.getAttendanceCheckOutTime());
				if (resourceAvailability.getAttendanceCheckOutTime() != null)
					jsonTimeSheetStatistics.setAttendanceEndDateTime(DateUtility.dateToStringInSecond(resourceAvailability.getAttendanceCheckOutTime()));
				
				totalTimeConvertedInHoursMinutes = null;
				
				//Field Name: AttendanceDuration
				if (resourceAvailability.getAttendanceCheckInTime() != null && resourceAvailability.getAttendanceCheckOutTime() != null){

					timeStatus = resourceAvailability.getAttendanceCheckInTime().compareTo(resourceAvailability.getAttendanceCheckOutTime());
					if 	(timeStatus.intValue() < 0){
						long attendanceDurationInMinutes = DateUtility.DateDifferenceInMinutes(
								resourceAvailability.getAttendanceCheckInTime(), resourceAvailability.getAttendanceCheckOutTime());
						totalTimeConvertedInHoursMinutes = DateUtility.convertTimeInHoursMinutes(0, new Integer((int)attendanceDurationInMinutes));
						log.info("totalTimeConvertedInHoursMinutes for Attendance Duration=" + totalTimeConvertedInHoursMinutes);
						jsonTimeSheetStatistics.setAttendanceDuration(totalTimeConvertedInHoursMinutes);	
					}
				}
				
			}else{
				log.info("resourceAvailability is NULL");
				
				log.info("Breaking the loop for resourceAvailability is NULL WorkPackageId=" +timeSheetStatisticsDTO.getWorkPackageId() + " Tester="+timeSheetStatisticsDTO.getTesterId()+
						" Date="+timeSheetStatisticsDTO.getActualExecutionDate() +" Shift="+timeSheetStatisticsDTO.getShiftTypeId());
				continue;
			}	
			//***********************************************************************************************************
			
			HashMap<String,Integer> timeMap =  new HashMap<String,Integer>();
			totalTimeConvertedInHoursMinutes = null;

			//***********************************************************************************************************
			//Field Name:  'LunchHours'
			Integer currentLunchTimeInMinutes = null;
			timeMap = timeSheetManagementDAO.getTimeSheetEntryForLunchAndBreaks(
					timeSheetStatisticsDTO.getWorkPackageId(), timeSheetStatisticsDTO.getShiftId(),
					timeSheetStatisticsDTO.getActualExecutionDate(), timeSheetStatisticsDTO.getTesterId(), 
					1, timeSheetStatisticsDTO.getProductResourceShiftCheckInId(), timeSheetStatisticsDTO.getUserRoleId());  // activityType 1 means Lunch
			
			log.info("timeMap for LunchBreak calculation="+timeMap);
			if (timeMap != null) {	
				log.info("timeMap Hours"+timeMap.get("totalHours"));
				log.info("timeMap Minutes"+timeMap.get("totalMinutes"));
				totalTimeConvertedInHoursMinutes = DateUtility.convertTimeInHoursMinutes(timeMap.get("totalHours"),timeMap.get("totalMinutes"));
				log.info("totalTimeConvertedInHoursMinutes for Lunch=" + totalTimeConvertedInHoursMinutes);
				
				currentLunchTimeInMinutes = DateUtility.convertTimeInMinutes(timeMap.get("totalHours"),timeMap.get("totalMinutes"));
			}
			
			if (currentLunchTimeInMinutes == null)
				currentLunchTimeInMinutes = 0;
			if (totalTimeConvertedInHoursMinutes == null)
				totalTimeConvertedInHoursMinutes = "00.00";
			jsonTimeSheetStatistics.setLunchHours(totalTimeConvertedInHoursMinutes); // This is timesheet entered Hours for lunch
			//***********************************************************************************************************
			
			
			//***********************************************************************************************************
			//Field Name: 'BreaksHours' (Field Added, was not there in initial requirement)
			totalTimeConvertedInHoursMinutes = null;
			Integer currentBreaksTimeInMinutes = null;
			
			timeMap = timeSheetManagementDAO.getTimeSheetEntryForLunchAndBreaks(
					timeSheetStatisticsDTO.getWorkPackageId(), timeSheetStatisticsDTO.getShiftId(),
					timeSheetStatisticsDTO.getActualExecutionDate(), timeSheetStatisticsDTO.getTesterId(), 
					3, timeSheetStatisticsDTO.getProductResourceShiftCheckInId(), timeSheetStatisticsDTO.getUserRoleId());  // activityType 3 means Breaks
			
			log.info("timeMap for OtherBreaks calculation="+timeMap);
			if (timeMap != null) {	
				log.info("timeMap Hours"+timeMap.get("totalHours"));
				log.info("timeMap Minutes"+timeMap.get("totalMinutes"));
				totalTimeConvertedInHoursMinutes = DateUtility.convertTimeInHoursMinutes(timeMap.get("totalHours"),timeMap.get("totalMinutes"));
				log.info("totalTimeConvertedInHoursMinutes for OtherBreaks=" + totalTimeConvertedInHoursMinutes);
				
				currentBreaksTimeInMinutes = DateUtility.convertTimeInMinutes(timeMap.get("totalHours"),timeMap.get("totalMinutes"));
			}
			
			if (currentBreaksTimeInMinutes == null)
				currentBreaksTimeInMinutes = 0;
			
			if (totalTimeConvertedInHoursMinutes == null)
				totalTimeConvertedInHoursMinutes = "00.00";
			
			jsonTimeSheetStatistics.setBreaksHours(totalTimeConvertedInHoursMinutes); // This is timesheet entered Hours for breaks
			//CUMULATIVE SUMMATION OF HOURS - LOGIC
			//***********************************************************************************************************
			//Field Name: 'UnAuthorised Lunch Hours'

			Integer cumulativeLunchOfTheShiftTimeInMinutes  = null;
			Integer previousCumulativeLunchOfTheShiftTimeInMinutes = null;
			Integer unAuthorisedLunchTimeInMinutes = null;
			
			if (timeMapLunchOfTheShiftUserWisePrevRecord != null && timeMapLunchOfTheShiftUserWisePrevRecord.containsKey(timeSheetStatisticsDTO.getTesterId())){
				log.info("Total Lunch Duration timeMapLunchOfTheShiftUserWisePrevRecord.containsKey If condition");
				log.info("timeMapLunchOfTheShiftUserWisePrevRecord="+timeMapLunchOfTheShiftUserWisePrevRecord.get(timeSheetStatisticsDTO.getTesterId()));
				previousCumulativeLunchOfTheShiftTimeInMinutes = timeMapLunchOfTheShiftUserWisePrevRecord.get(timeSheetStatisticsDTO.getTesterId());
				
				cumulativeLunchOfTheShiftTimeInMinutes = timeMapLunchOfTheShiftUserWisePrevRecord.get(timeSheetStatisticsDTO.getTesterId())+currentLunchTimeInMinutes;
										  
				log.info("cumulativeLunchOfTheShiftTimeInMinutes="+cumulativeLunchOfTheShiftTimeInMinutes);
			}	
			else{
				log.info("Total Lunch Duration timeMapLunchOfTheShiftUserWisePrevRecord.containsKey Else condition");
				cumulativeLunchOfTheShiftTimeInMinutes = currentLunchTimeInMinutes;
				log.info("cumulativeLunchOfTheShiftTimeInMinutes="+cumulativeLunchOfTheShiftTimeInMinutes);
			}
			
			if (previousCumulativeLunchOfTheShiftTimeInMinutes == null)
				previousCumulativeLunchOfTheShiftTimeInMinutes = 0;
			
			Integer shiftLunchAuthorisedTimeInMinutes = 45;// Default value   
			
			if (timeSheetStatisticsDTO.getShiftLunchAuthorisedTime() != null){
				shiftLunchAuthorisedTimeInMinutes = timeSheetStatisticsDTO.getShiftLunchAuthorisedTime();
			}else{
				if (shiftLunchAuthorisedTimeInMinutesConfigured != null){  // If the value given in property is not null
					try{
						shiftLunchAuthorisedTimeInMinutes = new Integer(shiftLunchAuthorisedTimeInMinutesConfigured);
					}
					catch (Exception e){
						shiftLunchAuthorisedTimeInMinutes = 45;  // Setting to Default value
					}
				}
			}
			
			if (cumulativeLunchOfTheShiftTimeInMinutes.intValue() <= shiftLunchAuthorisedTimeInMinutes){
				unAuthorisedLunchTimeInMinutes = 0;
			}else{
				if (previousCumulativeLunchOfTheShiftTimeInMinutes.intValue() > shiftLunchAuthorisedTimeInMinutes){  // If already exceeded limits
					unAuthorisedLunchTimeInMinutes = currentLunchTimeInMinutes;
				}else{
					unAuthorisedLunchTimeInMinutes = currentLunchTimeInMinutes - (shiftLunchAuthorisedTimeInMinutes-previousCumulativeLunchOfTheShiftTimeInMinutes);
				}
			}
				
			timeMapLunchOfTheShiftUserWisePrevRecord.put(timeSheetStatisticsDTO.getTesterId(),cumulativeLunchOfTheShiftTimeInMinutes);
			
			totalTimeConvertedInHoursMinutes = DateUtility.convertTimeInHoursMinutes(0, unAuthorisedLunchTimeInMinutes);
			log.info("totalTimeConvertedInHoursMinutes for Un Authorised Lunch Duration=" + totalTimeConvertedInHoursMinutes);
			jsonTimeSheetStatistics.setUnAuthorisedLunchHours(totalTimeConvertedInHoursMinutes);
			
			//***********************************************************************************************************
			//Field Name: 'UnAuthorised Breaks Hours'

			Integer cumulativeBreaksOfTheShiftTimeInMinutes  = null;
			Integer previousCumulativeBreaksOfTheShiftTimeInMinutes = null;
			Integer unAuthorisedBreakTimeInMinutes = null;
			
			if (timeMapBreaksOfTheShiftUserWisePrevRecord != null && timeMapBreaksOfTheShiftUserWisePrevRecord.containsKey(timeSheetStatisticsDTO.getTesterId())){
				
				log.info("Total Breaks Duration timeMapBreaksOfTheShiftUserWisePrevRecord.containsKey If condition");
				log.info("timeMapBreaksOfTheShiftUserWisePrevRecord="+timeMapBreaksOfTheShiftUserWisePrevRecord.get(timeSheetStatisticsDTO.getTesterId()));
				previousCumulativeBreaksOfTheShiftTimeInMinutes = timeMapBreaksOfTheShiftUserWisePrevRecord.get(timeSheetStatisticsDTO.getTesterId());
				
				cumulativeBreaksOfTheShiftTimeInMinutes = timeMapBreaksOfTheShiftUserWisePrevRecord.get(timeSheetStatisticsDTO.getTesterId())+currentBreaksTimeInMinutes;
										  
				log.info("cumulativeBreaksOfTheShiftTimeInMinutes="+cumulativeBreaksOfTheShiftTimeInMinutes);
			}	
			else{
				log.info("Total Breaks Duration timeMapBreaksOfTheShiftUserWisePrevRecord.containsKey Else condition");
				cumulativeBreaksOfTheShiftTimeInMinutes = currentBreaksTimeInMinutes;
				log.info("cumulativeBreaksOfTheShiftTimeInMinutes="+cumulativeBreaksOfTheShiftTimeInMinutes);
			}
			
			if (previousCumulativeBreaksOfTheShiftTimeInMinutes == null)
				previousCumulativeBreaksOfTheShiftTimeInMinutes = 0;
			
			Integer shiftBreaksAuthorisedTimeInMinutes = 45;// Default value   
			
			if (timeSheetStatisticsDTO.getShiftBreaksAuthorisedTime() != null){
				shiftBreaksAuthorisedTimeInMinutes = timeSheetStatisticsDTO.getShiftBreaksAuthorisedTime();
			}else{
				if (shiftBreaksAuthorisedTimeInMinutesConfigured != null){  // If the value given in property is not null
					try{
						shiftBreaksAuthorisedTimeInMinutes = new Integer(shiftBreaksAuthorisedTimeInMinutesConfigured);
					}
					catch (Exception e){
						shiftBreaksAuthorisedTimeInMinutes = 45;  // Setting to Default value
					}
				}
			}
			
			if (cumulativeBreaksOfTheShiftTimeInMinutes.intValue() <= shiftBreaksAuthorisedTimeInMinutes.intValue()){
				unAuthorisedBreakTimeInMinutes = 0;
			}else{
				if (previousCumulativeBreaksOfTheShiftTimeInMinutes.intValue() > shiftBreaksAuthorisedTimeInMinutes.intValue()){  // If already exceeded limits
					unAuthorisedBreakTimeInMinutes = currentBreaksTimeInMinutes;
				}else{
					unAuthorisedBreakTimeInMinutes = currentBreaksTimeInMinutes - (shiftBreaksAuthorisedTimeInMinutes-previousCumulativeBreaksOfTheShiftTimeInMinutes);
				}	
			}
			
			timeMapBreaksOfTheShiftUserWisePrevRecord.put(timeSheetStatisticsDTO.getTesterId(),cumulativeBreaksOfTheShiftTimeInMinutes);
			
			totalTimeConvertedInHoursMinutes = DateUtility.convertTimeInHoursMinutes(0, unAuthorisedBreakTimeInMinutes);
			log.info("totalTimeConvertedInHoursMinutes for Un Authorised Breaks Duration=" + totalTimeConvertedInHoursMinutes);
			jsonTimeSheetStatistics.setUnAuthorisedBreaksHours(totalTimeConvertedInHoursMinutes);
			//***********************************************************************************************************
			//Field Name: 'WorkPackage Shift Hours' (Field Added, was not there in initial requirement)
			totalTimeConvertedInHoursMinutes = null;
			Integer currentWorkPackageShiftTimeInMinutes = null;
			
			log.info("Hours Worked from DB"+timeSheetStatisticsDTO.getHoursWorked());
			log.info("Minutes Worked from DB"+timeSheetStatisticsDTO.getMinutesWorked());
			
			currentWorkPackageShiftTimeInMinutes = DateUtility.convertTimeInMinutes(timeSheetStatisticsDTO.getHoursWorked(),timeSheetStatisticsDTO.getMinutesWorked());
			
			totalTimeConvertedInHoursMinutes = DateUtility.convertTimeInHoursMinutes(0, currentWorkPackageShiftTimeInMinutes);
			log.info("totalTimeConvertedInHoursMinutes for WorkPackageShiftHours=" + totalTimeConvertedInHoursMinutes);
		
			
			if (currentWorkPackageShiftTimeInMinutes == null)
				currentWorkPackageShiftTimeInMinutes = 0;
			
			if (currentWorkPackageShiftTimeInMinutes == 0)
				jsonTimeSheetStatistics.setWorkPackageShiftHours("0.00");
			else
				jsonTimeSheetStatistics.setWorkPackageShiftHours(totalTimeConvertedInHoursMinutes); // This is timesheet entered Hours for a workPackageId on a shift
			//***********************************************************************************************************
			//Field Names:
			//Field Name: 'WorkPackage Shift Net Hours' (Field Added, was not there in initial requirement)
			totalTimeConvertedInHoursMinutes = null;
			
			Integer currentWorkPackageShiftNetTimeInMinutes = currentWorkPackageShiftTimeInMinutes;
			
			currentWorkPackageShiftNetTimeInMinutes  = currentWorkPackageShiftNetTimeInMinutes - unAuthorisedLunchTimeInMinutes;
			
			currentWorkPackageShiftNetTimeInMinutes  = currentWorkPackageShiftNetTimeInMinutes - unAuthorisedBreakTimeInMinutes;
			
			totalTimeConvertedInHoursMinutes = DateUtility.convertTimeInHoursMinutes(0, currentWorkPackageShiftNetTimeInMinutes);
			jsonTimeSheetStatistics.setWorkPackageShiftNetHours(totalTimeConvertedInHoursMinutes); // This is timesheet entered Hours for a workPackageId on a shift
			//***********************************************************************************************************
			//New Logic
			//Field Names:
			//Field Name: '(Shift) Total Work Hours' OR 'TotalShiftDuration'
			
			totalTimeConvertedInHoursMinutes = null;
			Integer cumulativeShiftTimeInMinutes  = null;

			if (timeMapShiftUserWisePrevRecord != null && timeMapShiftUserWisePrevRecord.containsKey(timeSheetStatisticsDTO.getTesterId())){
				
				log.info("TotalShiftDuration timeMapShiftUserWisePrevRecord.containsKey If condition");
				log.info("timeMapShiftUserWisePrevRecord="+timeMapShiftUserWisePrevRecord.get(timeSheetStatisticsDTO.getTesterId()));
				
				cumulativeShiftTimeInMinutes = timeMapShiftUserWisePrevRecord.get(timeSheetStatisticsDTO.getTesterId())+currentWorkPackageShiftNetTimeInMinutes;
										  
				log.info("cumulativeShiftTimeInMinutes="+cumulativeShiftTimeInMinutes);
			}	
			else{
				log.info("TotalShiftDuration timeMapShiftUserWisePrevRecord.containsKey Else condition");
				cumulativeShiftTimeInMinutes = currentWorkPackageShiftNetTimeInMinutes;
				log.info("cumulativeShiftTimeInMinutes="+cumulativeShiftTimeInMinutes);
			}
			
			totalTimeConvertedInHoursMinutes = DateUtility.convertTimeInHoursMinutes(null, cumulativeShiftTimeInMinutes);
			jsonTimeSheetStatistics.setShiftTotalWorkHours(totalTimeConvertedInHoursMinutes);
			
			timeMapShiftUserWisePrevRecord.put(timeSheetStatisticsDTO.getTesterId(),cumulativeShiftTimeInMinutes);
			//***********************************************************************************************************
			//Field Names:
			//Field Name: '(Daily) Total Work Hours' OR 'TotalDailyDuration'
			//AND Field Name: '(Daily) Payroll Total Work Hours' OR 'PayrollTotalDailyDuration'
			
			totalTimeConvertedInHoursMinutes = null;
			String totalTimeConvertedInHoursDecimalFormatMinutes = null;
			
			Integer cumulativeDayTimeInMinutes  = null; 
			if (timeMapDayUserWisePrevRecord != null && timeMapDayUserWisePrevRecord.containsKey(timeSheetStatisticsDTO.getTesterId())){
				
				log.info("TotalDailyDuration timeMapDayUserWisePrevRecord.containsKey If condition");
				log.info("timeMapDayUserWisePrevRecord="+timeMapDayUserWisePrevRecord.get(timeSheetStatisticsDTO.getTesterId()));
				
				cumulativeDayTimeInMinutes = timeMapDayUserWisePrevRecord.get(timeSheetStatisticsDTO.getTesterId())+currentWorkPackageShiftNetTimeInMinutes;
										  
				log.info("cumulativeDayTimeInMinutes="+cumulativeDayTimeInMinutes);
			}	
			else{
				log.info("TotalDailyDuration timeMapDayUserWisePrevRecord.containsKey Else condition");
				cumulativeDayTimeInMinutes = currentWorkPackageShiftNetTimeInMinutes;
				log.info("cumulativeDayTimeInMinutes="+cumulativeDayTimeInMinutes);
			}
			
			totalTimeConvertedInHoursMinutes = DateUtility.convertTimeInHoursMinutes(null, cumulativeDayTimeInMinutes);
			jsonTimeSheetStatistics.setDailyTotalWorkHours(totalTimeConvertedInHoursMinutes);
			
			totalTimeConvertedInHoursDecimalFormatMinutes = DateUtility.convertTimeInHoursDecimalFormatMinutes(null, cumulativeDayTimeInMinutes);
			jsonTimeSheetStatistics.setPayrollDailyTotalWorkHours(totalTimeConvertedInHoursDecimalFormatMinutes);
			
			timeMapDayUserWisePrevRecord.put(timeSheetStatisticsDTO.getTesterId(),cumulativeDayTimeInMinutes);
			//***********************************************************************************************************
			//Field Names:
			//Field Name: '(Weekly) Hours on Project' OR 'ProjectTimePerWeek'
			
			totalTimeConvertedInHoursMinutes = null;
			Integer cumulativeWeekProductTimeInMinutes  = null;
			
			String testerProductKey = timeSheetStatisticsDTO.getTesterId() + "-" + timeSheetStatisticsDTO.getProductId();
			if (timeMapWeekProductWiseUserWisePrevRecord != null && timeMapWeekProductWiseUserWisePrevRecord.containsKey(testerProductKey)){
				log.info("ProjectTimePerWeek timeMapWeekProductWiseUserWisePrevRecord.containsKey If condition");
				log.info("timeMapWeekProductWiseUserWisePrevRecord="+timeMapWeekProductWiseUserWisePrevRecord.get(testerProductKey));
				
				cumulativeWeekProductTimeInMinutes = timeMapWeekProductWiseUserWisePrevRecord.get(testerProductKey)+
													 currentWorkPackageShiftNetTimeInMinutes;
										  
				log.info("cumulativeWeekProductTimeInMinutes="+cumulativeWeekProductTimeInMinutes);
			}	
			else{
				log.info("ProjectTimePerWeek timeMapWeekProductWiseUserWisePrevRecord.containsKey Else condition");
				cumulativeWeekProductTimeInMinutes = currentWorkPackageShiftNetTimeInMinutes;
				log.info("cumulativeWeekProductTimeInMinutes="+cumulativeWeekProductTimeInMinutes);
			}
			
			totalTimeConvertedInHoursMinutes = DateUtility.convertTimeInHoursMinutes(null, cumulativeWeekProductTimeInMinutes);
			jsonTimeSheetStatistics.setProjectTimePerWeek(totalTimeConvertedInHoursMinutes);
			
			timeMapWeekProductWiseUserWisePrevRecord.put(testerProductKey,cumulativeWeekProductTimeInMinutes); 
			//***********************************************************************************************************
			//Field Names:
			//Field Name: '(Weekly) Total Work Hours' OR 'OverallTimeCalculations'
			//AND Field Name: '(Weekly) Payroll Total Work Hours' OR 'PayrollTotalWeekHours'
			
			totalTimeConvertedInHoursMinutes = null;
			totalTimeConvertedInHoursDecimalFormatMinutes  = null;
			Integer cumulativeWeekTimeInMinutes  = null;
			
			log.info("Value of cumulativeWeekTimeInMinutes="+cumulativeWeekTimeInMinutes);
			log.info("Value of currentWorkPackageShiftNetTimeInMinutes="+currentWorkPackageShiftNetTimeInMinutes);
			
			if (timeMapWeekUserWisePrevRecord != null && timeMapWeekUserWisePrevRecord.containsKey(timeSheetStatisticsDTO.getTesterId())){
				log.info("OverallTimeCalculations timeMapWeekUserWisePrevRecord.containsKey If condition");
				
				log.info("timeMapWeekUserWisePrevRecord="+timeMapWeekUserWisePrevRecord.get(timeSheetStatisticsDTO.getTesterId()));								
				
				cumulativeWeekTimeInMinutes = timeMapWeekUserWisePrevRecord.get(timeSheetStatisticsDTO.getTesterId())+currentWorkPackageShiftNetTimeInMinutes;
										  
				log.info("cumulativeWeekTimeInMinutes="+cumulativeWeekTimeInMinutes);
			}	
			else{
				log.info("OverallTimeCalculations timeMapWeekUserWisePrevRecord.containsKey Else condition");
				cumulativeWeekTimeInMinutes = currentWorkPackageShiftNetTimeInMinutes;
				log.info("cumulativeWeekTimeInMinutes="+cumulativeWeekTimeInMinutes);
			}
			
			totalTimeConvertedInHoursMinutes = DateUtility.convertTimeInHoursMinutes(0, cumulativeWeekTimeInMinutes);
			jsonTimeSheetStatistics.setWeeklyTotalWorkHours(totalTimeConvertedInHoursMinutes);
			
			totalTimeConvertedInHoursDecimalFormatMinutes = DateUtility.convertTimeInHoursDecimalFormatMinutes(null, cumulativeWeekTimeInMinutes);
			jsonTimeSheetStatistics.setPayrollWeeklyTotalWorkHours(totalTimeConvertedInHoursDecimalFormatMinutes);
			
			timeMapWeekUserWisePrevRecord.put(timeSheetStatisticsDTO.getTesterId(),cumulativeWeekTimeInMinutes);
			//***********************************************************************************************************	
			//Field Names:
			//Field Name: '(Weekly) Standard Time' OR 'NetWeekHours'  (Logic is like: DailyStandardTime, but for a week until a particular execution date)
			// AND Field Name: '(Weekly) Payroll Standard Time' OR 'PayrollNetWeekHours' 
			// AND Field Name: '(Weekly) Payroll Hours Remaining' OR 'PayrollWeeklyHoursRemaining'
			// AND Field Name: '(Weekly) Over Time OR 'OverTime'
			// AND Field Name: '(Weekly) Payroll Over Time OR 'PayrollOverTime'
			
			
			//totalTimeConvertedInHoursMinutes = null;  // IMPORTANT: Don't uncomment these lines, the values are re-used below in different code blocks 
			//totalTimeConvertedInHoursDecimalFormatMinutes = null; // IMPORTANT: Don't uncomment these lines, the values are re-used below in different code blocks
			Integer cumulativeWeekOverTimeInMinutes  = 0;
			
			log.info("cumulativeWeekTimeInMinutes for Week NetWeekHours and other fields=" +cumulativeWeekTimeInMinutes);
			if (cumulativeWeekTimeInMinutes != null){
			
				Integer weeklyOverTimeLimitInMinutes = 2400; // Default value Eg. 40 Hours equals 2400 minutes  
				
				if (timeSheetStatisticsDTO.getWeeklyOverTimeLimit() != null){
					weeklyOverTimeLimitInMinutes = timeSheetStatisticsDTO.getWeeklyOverTimeLimit();
				}else{
					if (weeklyOverTimeLimitInMinutesConfigured != null){  // If the value given in property is not null
						try{
							weeklyOverTimeLimitInMinutes = new Integer(weeklyOverTimeLimitInMinutesConfigured);
						}
						catch (Exception e){ // If could not store data
							weeklyOverTimeLimitInMinutes = 2400;  // Default value Eg. 40 Hours equals 2400 minutes
						}
					}
				}
				
				if (cumulativeWeekTimeInMinutes.intValue() > weeklyOverTimeLimitInMinutes.intValue()){  // Eg. 40 Hours equals 2400 minutes				
					log.info("Inside if condition");
					
					totalTimeConvertedInHoursMinutes = DateUtility.convertTimeInHoursMinutes(null, weeklyOverTimeLimitInMinutes);
					jsonTimeSheetStatistics.setWeeklyStandardHours(totalTimeConvertedInHoursMinutes);
					
					totalTimeConvertedInHoursDecimalFormatMinutes = DateUtility.convertTimeInHoursDecimalFormatMinutes(null,weeklyOverTimeLimitInMinutes);
					jsonTimeSheetStatistics.setPayrollWeeklyStandardHours(totalTimeConvertedInHoursDecimalFormatMinutes);
					
					jsonTimeSheetStatistics.setPayrollWeeklyHoursRemaining("00.00");
					
					totalTimeConvertedInHoursMinutes = DateUtility.convertTimeInHoursMinutes(null, cumulativeWeekTimeInMinutes-weeklyOverTimeLimitInMinutes);
					jsonTimeSheetStatistics.setWeeklyOverTimeHours(totalTimeConvertedInHoursMinutes);
					cumulativeWeekOverTimeInMinutes = cumulativeWeekTimeInMinutes-weeklyOverTimeLimitInMinutes;
					
					totalTimeConvertedInHoursDecimalFormatMinutes = DateUtility.convertTimeInHoursDecimalFormatMinutes(null,cumulativeWeekTimeInMinutes-weeklyOverTimeLimitInMinutes);
					jsonTimeSheetStatistics.setPayrollWeeklyOverTimeHours(totalTimeConvertedInHoursDecimalFormatMinutes);
				}else{
					log.info("Inside else");
					jsonTimeSheetStatistics.setWeeklyStandardHours(totalTimeConvertedInHoursMinutes);  // This is from already available value (From overallTimeCalculations field)
					
					jsonTimeSheetStatistics.setPayrollWeeklyStandardHours(totalTimeConvertedInHoursDecimalFormatMinutes);
					
					totalTimeConvertedInHoursDecimalFormatMinutes = DateUtility.convertTimeInHoursDecimalFormatMinutes(null, weeklyOverTimeLimitInMinutes-cumulativeWeekTimeInMinutes);
					jsonTimeSheetStatistics.setPayrollWeeklyHoursRemaining(totalTimeConvertedInHoursDecimalFormatMinutes);
					
					jsonTimeSheetStatistics.setWeeklyOverTimeHours("00.00");
					jsonTimeSheetStatistics.setPayrollWeeklyOverTimeHours("00.00");
				}
			}	
			//***********************************************************************************************************	
			//Logic for WorkPackage Shift time calculations depends on currentWorkPackageShiftNetTimeInMinutes and cumulativeWeekOverTimeInMinutes
			//***********************************************************************************************************
			//Field Names: 
			//Field Name: '(WorkPackage Shift) Standard Hours' AND Field Name: '(WorkPackage Shift) OverTime Hours'
			
			totalTimeConvertedInHoursMinutes = null;
			
			if (cumulativeWeekOverTimeInMinutes == 0){ // This means tester is not in week over time mode
				jsonTimeSheetStatistics.setWorkPackageShiftOverTimeHours("00.00");
				totalTimeConvertedInHoursMinutes = DateUtility.convertTimeInHoursMinutes(null, currentWorkPackageShiftNetTimeInMinutes);
				jsonTimeSheetStatistics.setWorkPackageShiftStandardHours(totalTimeConvertedInHoursMinutes);
			}else{ // This means already tester in weekly overtime
				if (cumulativeWeekOverTimeInMinutes >= currentWorkPackageShiftNetTimeInMinutes){
					jsonTimeSheetStatistics.setWorkPackageShiftStandardHours("00.00");
					
					totalTimeConvertedInHoursMinutes = DateUtility.convertTimeInHoursMinutes(null, currentWorkPackageShiftNetTimeInMinutes);
					jsonTimeSheetStatistics.setWorkPackageShiftOverTimeHours(totalTimeConvertedInHoursMinutes);
				}else{
					totalTimeConvertedInHoursMinutes = DateUtility.convertTimeInHoursMinutes(null, cumulativeWeekOverTimeInMinutes);
					jsonTimeSheetStatistics.setWorkPackageShiftOverTimeHours(totalTimeConvertedInHoursMinutes);
					
					totalTimeConvertedInHoursMinutes = DateUtility.convertTimeInHoursMinutes(null, currentWorkPackageShiftNetTimeInMinutes-cumulativeWeekOverTimeInMinutes);
					jsonTimeSheetStatistics.setWorkPackageShiftStandardHours(totalTimeConvertedInHoursMinutes);
				}
			}
			//***********************************************************************************************************	
			//Logic for Shift time calculations depends on cumulativeShiftTimeInMinutes and cumulativeWeekOverTimeInMinutes
			//***********************************************************************************************************
			//New Logic
			//Field Names: 
			//Field Name: '(Shift) Standard Time' AND Field Name: '(Shift) Overtime'
			
			totalTimeConvertedInHoursMinutes = null;
			
			if (cumulativeWeekOverTimeInMinutes == 0){ // This means tester is not in week over time mode
				jsonTimeSheetStatistics.setShiftOverTimeHours("00.00");
				totalTimeConvertedInHoursMinutes = DateUtility.convertTimeInHoursMinutes(null, cumulativeShiftTimeInMinutes);
				jsonTimeSheetStatistics.setShiftStandardHours(totalTimeConvertedInHoursMinutes);
			}else{ // This means already tester in weekly overtime
				if (cumulativeWeekOverTimeInMinutes >= cumulativeShiftTimeInMinutes){
					jsonTimeSheetStatistics.setShiftStandardHours("00.00");
					
					totalTimeConvertedInHoursMinutes = DateUtility.convertTimeInHoursMinutes(null, cumulativeShiftTimeInMinutes);
					jsonTimeSheetStatistics.setShiftOverTimeHours(totalTimeConvertedInHoursMinutes);
				}else{
					totalTimeConvertedInHoursMinutes = DateUtility.convertTimeInHoursMinutes(null, cumulativeWeekOverTimeInMinutes);
					jsonTimeSheetStatistics.setShiftOverTimeHours(totalTimeConvertedInHoursMinutes);
					
					totalTimeConvertedInHoursMinutes = DateUtility.convertTimeInHoursMinutes(null, cumulativeShiftTimeInMinutes-cumulativeWeekOverTimeInMinutes);
					jsonTimeSheetStatistics.setShiftStandardHours(totalTimeConvertedInHoursMinutes);
				}
			}
			//Logic for Daily time calculations depends on cumulativeDayTimeInMinutes and cumulativeWeekOverTimeInMinutes
			//***********************************************************************************************************
			//Field Names: 
			//Field Name: '(Daily) Standard Time' AND Field Name: '(Daily) Overtime'
			
			totalTimeConvertedInHoursMinutes  = null;
			
			if (cumulativeWeekOverTimeInMinutes == 0){ // This means tester is not in week over time mode
				jsonTimeSheetStatistics.setDailyOverTimeHours("00.00");
				totalTimeConvertedInHoursMinutes = DateUtility.convertTimeInHoursMinutes(null, cumulativeDayTimeInMinutes);
				jsonTimeSheetStatistics.setDailyStandardHours(totalTimeConvertedInHoursMinutes);
			}else{ // This means already tester in weekly overtime
				if (cumulativeWeekOverTimeInMinutes >= cumulativeDayTimeInMinutes){
					jsonTimeSheetStatistics.setDailyStandardHours("00.00");
					
					totalTimeConvertedInHoursMinutes = DateUtility.convertTimeInHoursMinutes(null, cumulativeDayTimeInMinutes);
					jsonTimeSheetStatistics.setDailyOverTimeHours(totalTimeConvertedInHoursMinutes);
				}else{
					totalTimeConvertedInHoursMinutes = DateUtility.convertTimeInHoursMinutes(null, cumulativeWeekOverTimeInMinutes);
					jsonTimeSheetStatistics.setDailyOverTimeHours(totalTimeConvertedInHoursMinutes);
					
					totalTimeConvertedInHoursMinutes = DateUtility.convertTimeInHoursMinutes(null, cumulativeDayTimeInMinutes-cumulativeWeekOverTimeInMinutes);
					jsonTimeSheetStatistics.setDailyStandardHours(totalTimeConvertedInHoursMinutes);
				}
			}
			//***********************************************************************************************************
			//Field Name: 'Week StartDate'
			jsonTimeSheetStatistics.setWeekStartDate(DateUtility.sdfDateformatWithOutTime(DateUtility.getWeekStart(timeSheetStatisticsDTO.getActualExecutionDate())));
			
			jsonTimeSheetStatistics.setDayOfWeek(DateUtility.getDayNameForDayNumber(DateUtility.getDayOfWeek(timeSheetStatisticsDTO.getActualExecutionDate())));
	
			//Field Name: 'Month Half'
			if (DateUtility.getDateOfDate(timeSheetStatisticsDTO.getActualExecutionDate()) < 16)
				jsonTimeSheetStatistics.setMonthHalf("FIRST");
			else
				jsonTimeSheetStatistics.setMonthHalf("SECOND");
			//***********************************************************************************************************
			//Field Name: 'Current Highest Rank'
			// If Hashmap value is available re-use the value otherwise get from DB and set the value for further re-use.
			if (currentHighestRatingUserWise.get(timeSheetStatisticsDTO.getTesterId()) == null)	{
				log.info("Performance level for the user not stored already");
				ResourceDailyPerformance resourceDailyPerformance = resourcePerformanceDAO.getResourceDailyPerformanceWithHighRating(timeSheetStatisticsDTO.getTesterId(), executionDateFrom, executionDateTo);
				if (resourceDailyPerformance != null && resourceDailyPerformance.getPerformanceLevel() != null){
					log.info("Performance level being stored="+resourceDailyPerformance.getPerformanceLevel());
					currentHighestRatingUserWise.put(timeSheetStatisticsDTO.getTesterId(), resourceDailyPerformance.getPerformanceLevel().getLevelName());
				}	
			}

			if (currentHighestRatingUserWise.containsKey(timeSheetStatisticsDTO.getTesterId()))
				jsonTimeSheetStatistics.setCurrentHighestRank(currentHighestRatingUserWise.get(timeSheetStatisticsDTO.getTesterId()));
			//NOW FINALLY Add the json object entry to the json list object
			jsonTimeSheetStatisticsList.add(jsonTimeSheetStatistics);
			
		}	
		return jsonTimeSheetStatisticsList;
	}

	@Override
	@Transactional
	public List<JsonActivityReport> listActivitiesByDate(Date dataFromDate, Date dataToDate,Integer productId) {
		List<JsonActivityReport> jsonActivityReportList = new ArrayList<JsonActivityReport>();
		List<Activity> activitys =  reportDAO.listActivitiesByDate(dataFromDate, dataToDate,productId);
		if(activitys != null && activitys.size()>0){
			for (Activity activity : activitys) {
				JsonActivityReport jsonActivityReport = new JsonActivityReport(activity);
				jsonActivityReportList.add(jsonActivityReport);
			}
		}
		return jsonActivityReportList;
	}
	
	@Override
	@Transactional
	public List<Activity> getActivitiesByDate(Date dataFromDate, Date dataToDate,Integer productId) {
		List<Activity> activitys =  null;
		activitys = reportDAO.listActivitiesByDate(dataFromDate, dataToDate,productId);
		return activitys;
	}
	@Override
	@Transactional
	public List<Object[]> getActivityTaskEffortReport(Integer productId) {
		List<Object[]> activityTaskEffortList =  null;
		activityTaskEffortList = reportDAO.getActivityTaskEffortReport(productId);
		return activityTaskEffortList;
	}
	
	@Override
	@Transactional
	public List<Object[]> getResourceEffortReport(Integer productId) {
		List<Object[]> activityTaskEffortList =  null;
		activityTaskEffortList = reportDAO.getResourceEffortReport(productId);
		return activityTaskEffortList;
	}
	@Override
	@Transactional
	public void addTaskEffortTemplate(TaskEffortTemplate taskEffortTemplate) {
		reportDAO.addTaskEffortTemplate(taskEffortTemplate);
		
	}
	@Override
	@Transactional
	public List<TaskEffortTemplate> getTaskEffortTemplateList() {
		List<TaskEffortTemplate> taskEffortTemplate =  null;
		taskEffortTemplate = reportDAO.getTaskEffortTemplateList();
		return taskEffortTemplate;
	}
	@Override
	@Transactional
	public List<Object[]> getActivityTaskEffortReportBasedOnFilter(String productId, String statusVal, Date fromDate, Date toDate, String resourceVal) {
		List<Object[]> activityTaskEffortList =  null;
		activityTaskEffortList = reportDAO.getActivityTaskEffortReportBasedOnFilter(productId,statusVal,fromDate,toDate,resourceVal);
		return activityTaskEffortList;
	}
	@Override
	@Transactional
	public TaskEffortTemplate getTaskEffortTemplateById(Integer templateId){
		TaskEffortTemplate taskEffortTemplate=null;
		taskEffortTemplate=reportDAO.getTaskEffortTemplateById(templateId);
		return taskEffortTemplate;
	}
	@Override
	@Transactional
	public List<MongoCollection> getMongoCollectionList() {
		List<MongoCollection> mongoCollection =  null;
		mongoCollection = reportDAO.getMongoCollectionList();
		return mongoCollection;
	}
	
	@Override
	@Transactional
	public List<Object[]> getPivotRestTemplateList(Integer templateId) {
		List<Object[]> pivotRestTemplate =  null;
		pivotRestTemplate = reportDAO.getPivotRestTemplateList(templateId);
		return pivotRestTemplate;
	}
	
	@Override
	@Transactional
	public void addPivotRestTemplate(PivotRestTemplate pivotRestTemplate){
		reportDAO.addPivotRestTemplate(pivotRestTemplate);
	}
	
	@Override
	@Transactional
	public List<PivotRestTemplate> getPivotRestTemplateReportByParams(Integer factoryId, Integer productId, Integer collectionId){
		List<PivotRestTemplate> pivotReportTemplateList =  null;
		pivotReportTemplateList = reportDAO.getPivotRestTemplateReportByParams(factoryId,productId,collectionId);
		return pivotReportTemplateList;
	}
	@Override
	@Transactional
	public List<PivotRestTemplate> getPivotRestTemplateList() {
		List<PivotRestTemplate> pivotRestTemplate =  null;
		pivotRestTemplate = reportDAO.getPivotRestTemplateList();
		return pivotRestTemplate;
	}
	
	@Override
	@Transactional
	public List<PivotRestTemplateDTO> getPivotRestTemplateList(Integer collectionId, Integer jtStartIndex, Integer jtPageSize) {

		return reportDAO.getPivotRestTemplateList(collectionId, jtStartIndex, jtPageSize);
	}
	@Override
	@Transactional
	public String deletePivotRestReportById(int templateId){
		return reportDAO.deletePivotRestReportById(templateId);
	}
	
	@Override
	@Transactional
	public List<Object[]> getProductAndEngagementNameList(String productIds){
		List<Object[]> productAndEngagementNameList =  null;
		productAndEngagementNameList = reportDAO.getProductAndEngagementNameList(productIds);
		return productAndEngagementNameList;
	}

	@Override
	@Transactional
	public List<ReportIssue> getReportIssueList() {
		return reportDAO.getReportIssueList();
	}
	
	@Override
	@Transactional
	public void  updateReportIssue(ReportIssue roportIssue){
		 reportDAO.updateReportIssue(roportIssue);
	}

	@Override
	public void addReportIssue(ReportIssue reportIssue) {
		reportDAO.addReportIssue(reportIssue);
	}
}
