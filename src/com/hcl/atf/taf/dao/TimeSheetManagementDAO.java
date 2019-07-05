package com.hcl.atf.taf.dao;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import com.hcl.atf.taf.model.ResourceShiftCheckIn;
import com.hcl.atf.taf.model.TimeSheetActivityType;
import com.hcl.atf.taf.model.TimeSheetEntryMaster;
import com.hcl.atf.taf.model.UserList;
import com.hcl.atf.taf.model.WorkShiftMaster;
import com.hcl.atf.taf.model.dto.TimeSheetDaySummaryDTO;
import com.hcl.atf.taf.model.dto.TimeSheetResourceCheckinDTO;
import com.hcl.atf.taf.model.dto.UserWeekUtilisedTimeDTO;

public interface TimeSheetManagementDAO {
	List<TimeSheetEntryMaster> listTimeSheetEntries(int userId);
	TimeSheetEntryMaster addTimeSheetEntry(TimeSheetEntryMaster timeSheetEntryMaster);
	void updateTimeSheetEntry(TimeSheetEntryMaster timeSheetEntryMaster);
	int getTotalRecords(int userId);
	List<TimeSheetDaySummaryDTO> listTimeSheetSummaryForWeek(int resourceId, Date startDate, Date endDate);
	TimeSheetResourceCheckinDTO listTimeSheetForResourceShiftCheckin(int resourceId, int workShiftId, Date timeSheetDate);
	List<TimeSheetEntryMaster> getTimeSheetEntriesOfWorkPackageForApproval(int workPackageId);
	List<TimeSheetEntryMaster> getTimeSheetEntriesOfWorkPackageBasedonStatus(int workPackageId, int statusID);
	
	
	
	void updateAndApproveTimeSheetEntry(TimeSheetEntryMaster timeSheetEntryMaster);
	TimeSheetEntryMaster getTimeSheetEntryById(int timeSheetEntryId);

	List<TimeSheetActivityType> listGenericTimeSheetActivityTypes();
	List<TimeSheetActivityType> listProductSpecificTimeSheetActivityTypes(int productId);
	TimeSheetActivityType getTimeSheetActivityTypeById(int activityTypeId);
	List<TimeSheetDaySummaryDTO> getTotalWorkedHoursbyDatenShift(UserList user, Date currentDate, int timeSheetEntryId, int shiftId);	
	List<TimeSheetDaySummaryDTO> mandatoryHoursofShifts(int shiftId);
	List<TimeSheetDaySummaryDTO> mandatoryHoursofActualShift(int shiftId, Date currentDate);
	
	HashMap<String,Integer> getTimeSheetEntryForWorkPackageStatisticsReportEOD(Integer workPackageId, Integer shiftId, Date executionDate, Integer testerId);
	HashMap<String,Integer> getTimeSheetEntryForADate(Date executionDate, Integer testerId);
	HashMap<String,Integer> getTimeSheetEntryForADateFromWeekStart(Date executionDate, Integer testerId);
	HashMap<String,Integer> getTimeSheetEntryForADateFromWeekStartOnAProduct(Date executionDate, Integer testerId, Integer productId);
	
	HashMap<String,Integer> getTimeSheetEntryForLunchAndBreaks(Integer workPackageId, Integer shiftId, Date executionDate, 
			   												  Integer testerId, Integer activityTypeId, Integer resourceShiftCheckInId, Integer userRoleId);
	HashMap<Integer,Integer> getTimeSheetEntryForADateFromWeekStart(Date executionDate);
	List<UserWeekUtilisedTimeDTO> getTimesheetEntriesForWorkPackage(int workPackgeId, WorkShiftMaster shift, Date resourceBlockedDate);
	List<UserWeekUtilisedTimeDTO> getTimesheetEntriesForUser(int workPackgeId, int shiftTypeId, Date resourceBlockedDate, int userId);
	List<UserWeekUtilisedTimeDTO> getReservedShiftsOfUser(int workPackgeId,int shiftId, Date blockResourceForDate);
	List<UserWeekUtilisedTimeDTO> getReservedShiftsforUserId(int userId, Date blockResourceForDate);
	
	String getRoleOfUserOnWorkPackage(Integer workPackageId, Integer shiftId, Date executionDate, Integer testerId);
	
	List<ResourceShiftCheckIn> getResourceShiftCheckInByCheckinDate(int userId, String checkinDate);
	List<TimeSheetEntryMaster> getTimeSheetEntriesOfResourceShiftCheckInId(int resourceShiftCheckInId);
}
