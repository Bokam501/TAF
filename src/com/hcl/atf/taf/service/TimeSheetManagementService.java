package com.hcl.atf.taf.service;

import java.util.Date;
import java.util.List;

import org.springframework.web.bind.annotation.RequestParam;







import com.hcl.atf.taf.model.ProductMaster;
import com.hcl.atf.taf.model.ResourceShiftCheckIn;
import com.hcl.atf.taf.model.ActualShift;
import com.hcl.atf.taf.model.ResourceShiftCheckIn;
import com.hcl.atf.taf.model.TimeSheetActivityType;
import com.hcl.atf.taf.model.TimeSheetEntryMaster;
import com.hcl.atf.taf.model.UserList;
import com.hcl.atf.taf.model.WorkPackage;
import com.hcl.atf.taf.model.WorkShiftMaster;
import com.hcl.atf.taf.model.dto.TimeSheetDaySummaryDTO;
import com.hcl.atf.taf.model.dto.TimeSheetResourceCheckinDTO;
import com.hcl.atf.taf.model.json.JsonResourceShiftCheckin;
import com.hcl.atf.taf.model.json.JsonTimeSheetDaySummary;
import com.hcl.atf.taf.model.json.JsonTimeSheetEntryMaster;

public interface TimeSheetManagementService {
	TimeSheetEntryMaster getTimeSheetEntryById(int timeSheetEntryId);
	List<TimeSheetEntryMaster> listTimeSheetEntries(int userId);
	
	List<TimeSheetEntryMaster> getTimeSheetEntriesOfWorkPackageForApproval(int workPackageId);
	List<TimeSheetEntryMaster> getTimeSheetEntriesOfWorkPackageBasedonStatus(int workPackageId,int statusID);	
	void addTimeSheetEntry (TimeSheetEntryMaster timeSheetEntryMaster);
	int getTotalRecordsOfTimeSheetEntryByUserId(int userId);
	TimeSheetEntryMaster addTimeSheetEntry(TimeSheetEntryMaster timeSheetEntryMaster, UserList user, WorkShiftMaster shift);
	void updateTimeSheetEntry(TimeSheetEntryMaster timeSheetEntryMaster, UserList user, WorkShiftMaster shift);
	void updateAndApproveTimeSheetEntry(TimeSheetEntryMaster timeSheetEntryMaster, UserList user, WorkShiftMaster shift, int isApproved);
	List<JsonTimeSheetDaySummary> listTimeSheetSummaryForWeek(int resourceId, int weekNo);
	List<TimeSheetActivityType> listGenericTimeSheetActivityTypes();
	List<TimeSheetActivityType> listProductSpecificTimeSheetActivityTypes(int productId);
	TimeSheetActivityType getTimeSheetActivityTypeById(int activityTypeId);
	List<TimeSheetDaySummaryDTO> getTotalWorkedHoursbyDatenShift(UserList user, Date currentDate, int timeSheetEntryId, int shiftId);
	List<TimeSheetDaySummaryDTO> mandatoryHoursofShifts(int shiftId);
	List<ResourceShiftCheckIn> listResourceShiftCkeckInByproductId(int productId, Date date,int userId,ActualShift actualShift,int isApproved, int shiftTypeId);
	void addResourceShiftCkeckIn(int productId, int userId, Date workDate,ActualShift actualShift, String shiftTime, String shiftRemarks,String shiftRemarksValue);
	List<ResourceShiftCheckIn> listResourceShiftCkeckInByUserId(int userId);
	List<TimeSheetDaySummaryDTO> mandatoryHoursofActualShift(int shiftId, Date currentDate);
	ActualShift listActualShiftbyshiftId(int shiftId, Date date);
	void updateResourceShiftCkeckIn(ResourceShiftCheckIn resourceShiftCheckIn);
	List<ResourceShiftCheckIn> getResourceShiftCheckInByCheckinDate(int userId, String checkInDate);
	TimeSheetResourceCheckinDTO listTimeSheetEntriesResourceCheckin(int resourceId, int workShiftId, Date timeSheetDate);
	ResourceShiftCheckIn getByresourceShiftCheckInId(int resourceShiftCheckInId);
	void updateAndApproveResourceShiftTimeSheet(JsonResourceShiftCheckin jsonResourceShiftCheckin,Integer approverId);
	ActualShift listActualShiftbyActualShiftId(int actualshiftId);
	List<ProductMaster> getProductByResourceShiftCheckInId(int resourceShiftCheckInId);
	void deleteResorceShiftCheckIn(ResourceShiftCheckIn resourceShiftCheckIn);
	List<ResourceShiftCheckIn> getResourceShiftCheckInByDateAndShift(Date createdDate, int actualShiftId);
	List<JsonTimeSheetEntryMaster> getTimeSheetEntriesOfResourceShiftCheckInId(int resourceShiftCheckInId);
	List<TimeSheetEntryMaster> getTimeSheetEntriesOfResourceShiftCheckIn(int resourceShiftCheckInId);
	List<ResourceShiftCheckIn> getResourceShiftCheckInByDate(Date createdDate,
			Integer userId);
	
}
