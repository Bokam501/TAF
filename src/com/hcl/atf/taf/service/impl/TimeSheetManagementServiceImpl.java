package com.hcl.atf.taf.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hcl.atf.taf.controller.utilities.DateUtility;
import com.hcl.atf.taf.dao.ProductMasterDAO;
import com.hcl.atf.taf.dao.ResourceShiftCheckinDAO;
import com.hcl.atf.taf.dao.TimeSheetManagementDAO;
import com.hcl.atf.taf.dao.UserListDAO;
import com.hcl.atf.taf.dao.WorkPackageDAO;
import com.hcl.atf.taf.dao.WorkShiftMasterDAO;
import com.hcl.atf.taf.model.ActualShift;
import com.hcl.atf.taf.model.ProductMaster;
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
import com.hcl.atf.taf.service.TimeSheetManagementService;

@Service
public class TimeSheetManagementServiceImpl implements TimeSheetManagementService {
	
	private static final Log log = LogFactory.getLog(TimeSheetManagementServiceImpl.class);

	@Autowired
    private TimeSheetManagementDAO timeSheetManagementDAO;
	@Autowired
    private ProductMasterDAO productMasterDAO;
	@Autowired
    private WorkPackageDAO workPackageDAO;
	@Autowired
    private UserListDAO userListDAO;
	@Autowired
   private ResourceShiftCheckinDAO resourceShiftCheckinDAO;
	@Autowired
	   private WorkShiftMasterDAO workShiftMasterDAO;
	
	@Override
	@Transactional
	public List<TimeSheetEntryMaster> listTimeSheetEntries(int userId) {
		return timeSheetManagementDAO.listTimeSheetEntries(userId);
	}	
	
	@Override
	@Transactional
	public TimeSheetResourceCheckinDTO listTimeSheetEntriesResourceCheckin(int resourceId, int workShiftId, Date timeSheetDate) {
		List<TimeSheetEntryMaster> timeSheetEntryMasterResourCheckin = new ArrayList<TimeSheetEntryMaster>();
		timeSheetEntryMasterResourCheckin = timeSheetManagementDAO.listTimeSheetEntries(resourceId);		
		TimeSheetResourceCheckinDTO  tsdto = timeSheetManagementDAO.listTimeSheetForResourceShiftCheckin(resourceId, workShiftId, timeSheetDate);
		return tsdto;
	}
	
	@Override
	@Transactional
	public void addTimeSheetEntry(TimeSheetEntryMaster timeSheetEntryMaster) {
		timeSheetManagementDAO.addTimeSheetEntry(timeSheetEntryMaster);
	}

	@Override
	@Transactional
	public int getTotalRecordsOfTimeSheetEntryByUserId(int userId) {
		return timeSheetManagementDAO.getTotalRecords(userId);
	}

	@Override
	@Transactional
	public TimeSheetEntryMaster addTimeSheetEntry(TimeSheetEntryMaster timeSheetEntryMaster, UserList user, WorkShiftMaster shift) {
		
		//Get the workPackage
		WorkPackage workPakcage = workPackageDAO.getWorkPackageById(timeSheetEntryMaster.getWorkPackage().getWorkPackageId());
		timeSheetEntryMaster.setUser(user);
		timeSheetEntryMaster.setShift(shift);
		return timeSheetManagementDAO.addTimeSheetEntry(timeSheetEntryMaster);
		
	}
	
	@Override
	@Transactional
	public void updateTimeSheetEntry(TimeSheetEntryMaster timeSheetEntryMaster, UserList user, WorkShiftMaster shift) {
		
		//Get the workPackage
		WorkPackage workPakcage = workPackageDAO.getWorkPackageById(timeSheetEntryMaster.getWorkPackage().getWorkPackageId());
		timeSheetEntryMaster.setUser(user);
		timeSheetEntryMaster.setShift(shift);
		timeSheetManagementDAO.updateTimeSheetEntry(timeSheetEntryMaster);
		
	}

	@Override
	@Transactional
	public List<JsonTimeSheetDaySummary> listTimeSheetSummaryForWeek(int resourceId, int weekNo) {
		Date startDate = DateUtility.getDateForDayOfWeek(weekNo,0);
		log.info("startDate="+startDate);
		Date endDate = DateUtility.getDateForDayOfWeek(weekNo,6);
		log.info("endDate="+endDate);
		List<TimeSheetDaySummaryDTO> listOfTimeSheetDaySummaryDTO = timeSheetManagementDAO.listTimeSheetSummaryForWeek(resourceId, startDate, endDate);
		int counter = 0;
		List<Date> datesOfWeek = DateUtility.getDatesOfWeek(weekNo);
		List<JsonTimeSheetDaySummary> listOfJsonTimeSheetDaySummary = new ArrayList<JsonTimeSheetDaySummary>();
		JsonTimeSheetDaySummary jsonTimeSheetDaySummary = null;
		Map<Integer,JsonTimeSheetDaySummary> summaryMap = new HashMap<Integer,JsonTimeSheetDaySummary>();
		int dayCounter = 1;
		//Create the Jsons for the UI. There will be a total of 7 jsons, one for each day of the week
		for (Date date : datesOfWeek) {
//			log.info("Map Date : " + date);
			jsonTimeSheetDaySummary = new JsonTimeSheetDaySummary();
			jsonTimeSheetDaySummary.setTimeSheetEntryDate(DateUtility.dateToStringWithoutSeconds(date));
			jsonTimeSheetDaySummary.setId(dayCounter++);
			summaryMap.put(date.getDate(), jsonTimeSheetDaySummary);
		}
		
		//Transform and load the summary data coming from DB to the Json. The data from DB is per day and shift.
		//There will be upto 3 entities per day, one for each shift. These need to be loaded into the json for the date
		for (TimeSheetDaySummaryDTO timeSheetDaySummaryDTO : listOfTimeSheetDaySummaryDTO) {
			log.info("Summary DTO Date : " + timeSheetDaySummaryDTO.getTimeSheetEntryDate());
			jsonTimeSheetDaySummary = summaryMap.get(timeSheetDaySummaryDTO.getTimeSheetEntryDate().getDate());
			if (jsonTimeSheetDaySummary == null) {
				
				log.info("Summary DTO Date : " + timeSheetDaySummaryDTO.getTimeSheetEntryDate() + " . Unable to find json object in map. Skipping to next DTO");
				continue;
			}
			log.info("Summary DTO Date : " + timeSheetDaySummaryDTO.getTimeSheetEntryDate() + " Found object in json map");
			jsonTimeSheetDaySummary.setResourceId(timeSheetDaySummaryDTO.getResourceId());
			log.info("timeSheetDaySummaryDTO.getShiftTypeId() ::: ********* "+timeSheetDaySummaryDTO.getShiftTypeId());
			if(timeSheetDaySummaryDTO.getShiftTypeId() == 1){
				log.info("Morning shift");
				jsonTimeSheetDaySummary.setMorningShiftHours(jsonTimeSheetDaySummary.getMorningShiftHours()+timeSheetDaySummaryDTO.getTotalHours());
				jsonTimeSheetDaySummary.setMorningShiftMins(jsonTimeSheetDaySummary.getMorningShiftMins()+timeSheetDaySummaryDTO.getTotalMins());
			}
			if(timeSheetDaySummaryDTO.getShiftTypeId() == 2){
				log.info("Night shift");
				jsonTimeSheetDaySummary.setNightShiftHours(jsonTimeSheetDaySummary.getNightShiftHours()+timeSheetDaySummaryDTO.getTotalHours());
				jsonTimeSheetDaySummary.setNightShiftMins(jsonTimeSheetDaySummary.getNightShiftMins()+timeSheetDaySummaryDTO.getTotalMins());
			}
			if(timeSheetDaySummaryDTO.getShiftTypeId() == 3){
				log.info("Graveyard shift");
				jsonTimeSheetDaySummary.setGraveyardShiftHours(jsonTimeSheetDaySummary.getGraveyardShiftHours()+timeSheetDaySummaryDTO.getTotalHours());
				jsonTimeSheetDaySummary.setGraveyardShiftMins(jsonTimeSheetDaySummary.getGraveyardShiftMins()+timeSheetDaySummaryDTO.getTotalMins());
			}
			summaryMap.put(timeSheetDaySummaryDTO.getTimeSheetEntryDate().getDate(), jsonTimeSheetDaySummary);
		}
		
		for (Map.Entry<Integer, JsonTimeSheetDaySummary> entry : summaryMap.entrySet()) {
			
			JsonTimeSheetDaySummary json = entry.getValue();
			if(json != null){
				json = calculateTime(json);
				listOfJsonTimeSheetDaySummary.add(json);
			}
		}
		return listOfJsonTimeSheetDaySummary;
	}
	
	private JsonTimeSheetDaySummary calculateTime(JsonTimeSheetDaySummary json) {
		
		if(json == null){
			return json;
		}
		else{
			int totalMorningTime = json.getMorningShiftHours()*60 + json.getMorningShiftMins();
			json.setMorningShiftHours(totalMorningTime/60);
			json.setMorningShiftMins(totalMorningTime%60);
			json.setMorningShiftTime(totalMorningTime/60 + ":" + totalMorningTime%60);

			int totalNightTime = json.getNightShiftHours()*60 + json.getNightShiftMins();
			json.setNightShiftHours(totalNightTime/60);
			json.setNightShiftMins(totalNightTime%60);
			json.setNightShiftTime((totalNightTime/60) + ":" + (totalNightTime%60));

			int totalGraveyardTime = json.getGraveyardShiftHours()*60 + json.getGraveyardShiftMins();
			json.setGraveyardShiftHours(totalGraveyardTime/60);
			json.setGraveyardShiftMins(totalGraveyardTime%60);
			json.setGraveyardShiftTime((totalGraveyardTime/60) + ":" + (totalGraveyardTime%60));

			int totalDayTime = totalMorningTime +  totalNightTime + totalGraveyardTime;
			json.setTotalHours(totalDayTime/60);
			json.setTotalMins(totalDayTime % 60);
			json.setTotalTime((totalDayTime/60) + ":" + (totalDayTime % 60));
			return json;
		}
	}

	@Override
	@Transactional
	public List<TimeSheetEntryMaster> getTimeSheetEntriesOfWorkPackageForApproval(int workPackageId) {
		return timeSheetManagementDAO.getTimeSheetEntriesOfWorkPackageForApproval(workPackageId);
	}

	@Override
	@Transactional
	public List<TimeSheetEntryMaster> getTimeSheetEntriesOfWorkPackageBasedonStatus(int workPackageId, int statusID) {
		return timeSheetManagementDAO.getTimeSheetEntriesOfWorkPackageBasedonStatus(workPackageId, statusID);
	}
	
	@Override
	@Transactional
	public void updateAndApproveTimeSheetEntry(TimeSheetEntryMaster timeSheetEntryMaster, UserList user,WorkShiftMaster shift, int isApproved) {
			log.info("Inside updateAndApproveTimeSheetEntry() of Service Imp.....");
			timeSheetEntryMaster.setShift(shift);
			if(isApproved == 1){
				timeSheetEntryMaster.setApprover(user);
				timeSheetEntryMaster.setIsApproved(1);
				timeSheetEntryMaster.setApprovedDate(DateUtility.getCurrentTime());
			}
			timeSheetManagementDAO.updateAndApproveTimeSheetEntry(timeSheetEntryMaster);
	}

	@Override
	@Transactional
	public TimeSheetEntryMaster getTimeSheetEntryById(int timeSheetEntryId) {
		return timeSheetManagementDAO.getTimeSheetEntryById(timeSheetEntryId);
	}

	@Override
	@Transactional
	public List<TimeSheetActivityType> listGenericTimeSheetActivityTypes() {
		return timeSheetManagementDAO.listGenericTimeSheetActivityTypes();
	}

	@Override
	@Transactional
	public TimeSheetActivityType getTimeSheetActivityTypeById(int activityTypeId) {
		return timeSheetManagementDAO.getTimeSheetActivityTypeById(activityTypeId);
	}

	@Override
	@Transactional
	public List<TimeSheetDaySummaryDTO> getTotalWorkedHoursbyDatenShift(UserList user, Date currentDate, int timeSheetEntryId, int shiftId) {
		List<TimeSheetDaySummaryDTO> timeSheetDSDTO = new ArrayList<TimeSheetDaySummaryDTO>();
		timeSheetDSDTO = timeSheetManagementDAO.getTotalWorkedHoursbyDatenShift(user, currentDate, timeSheetEntryId, shiftId);		
		return timeSheetDSDTO;
	}
	
	
	@Override
	@Transactional
	public List<TimeSheetActivityType> listProductSpecificTimeSheetActivityTypes(
			int productId) {
		return timeSheetManagementDAO.listProductSpecificTimeSheetActivityTypes(productId);
	}
	
	@Override
	@Transactional
	public List<TimeSheetDaySummaryDTO> mandatoryHoursofShifts(int shiftId){		
		return timeSheetManagementDAO.mandatoryHoursofShifts(shiftId);
	}
	
	@Override
	@Transactional
	public List<TimeSheetDaySummaryDTO> mandatoryHoursofActualShift(int shiftId, Date currentDate){		
		return timeSheetManagementDAO.mandatoryHoursofActualShift(shiftId, currentDate);
	}

	@Override
	@Transactional
	public List<ResourceShiftCheckIn> getResourceShiftCheckInByCheckinDate(
			int userId, String checkInDate) {		
		return timeSheetManagementDAO.getResourceShiftCheckInByCheckinDate(userId, checkInDate);
	}
	@Override
	@Transactional
	public ResourceShiftCheckIn getByresourceShiftCheckInId(
			int resourceShiftCheckInId) {		
		return resourceShiftCheckinDAO.getByresourceShiftCheckInId(resourceShiftCheckInId);
	}
	
	
	@Override
	public List<ResourceShiftCheckIn> listResourceShiftCkeckInByproductId(int productId, Date date,int userId,ActualShift actualShift,int isApproved, int shiftTypeId) {
		log.info("listResourceShiftCkeckInByproductId");
		return resourceShiftCheckinDAO.listResourceShiftCkeckIn(productId,date,userId,actualShift,isApproved,shiftTypeId);
		
	}

	@Override
	public void addResourceShiftCkeckIn(int productId, int userId, Date workDate,ActualShift actualShift, String shiftTime, String shiftRemarks,String shiftRemarksValue) {
		
		Date checkIn=DateUtility.toDateInSec(shiftTime);
		if(checkIn.compareTo(actualShift.getStartTime())<0){
			checkIn=actualShift.getStartTime();
		}
		ResourceShiftCheckIn resourceShiftCheckin=new ResourceShiftCheckIn();
		ProductMaster productMaster=new ProductMaster();
		productMaster.setProductId(productId);
    	resourceShiftCheckin.setProductMaster(productMaster);
		UserList userList=new UserList();
		userList.setUserId(userId);
		resourceShiftCheckin.setUserList(userList);
		resourceShiftCheckin.setActualShift(actualShift);
		resourceShiftCheckin.setCreatedDate(workDate);
		resourceShiftCheckin.setIsApproved(0);
		if (shiftRemarks.equalsIgnoreCase("startRemarks")) {
			resourceShiftCheckin.setCheckIn(checkIn);
			resourceShiftCheckin.setStartTimeRemarks(shiftRemarksValue);
		}
		
		resourceShiftCheckinDAO.addResourceShiftCkeckIn(resourceShiftCheckin);
		
	}

	@Override
	public List<ResourceShiftCheckIn> listResourceShiftCkeckInByUserId(int userId) {
		return resourceShiftCheckinDAO.listResourceShiftCkeckInByUserId(userId);
	}

	@Override
	public ActualShift listActualShiftbyshiftId(int shiftId, Date date) {
		return workShiftMasterDAO.listActualShiftbyshiftId(shiftId,date);
	}

	@Override
	public void updateResourceShiftCkeckIn(
			ResourceShiftCheckIn resourceShiftCheckIn) {
		 resourceShiftCheckinDAO.updateResourceShiftCkeckIn(resourceShiftCheckIn);
		
	}

	@Override
	public void updateAndApproveResourceShiftTimeSheet(JsonResourceShiftCheckin jsonResourceShiftCheckin,Integer approverId) {
		
		ResourceShiftCheckIn resourceShiftCheckIn=resourceShiftCheckinDAO.getByresourceShiftCheckInId(jsonResourceShiftCheckin.getResourceShiftCheckInId());
		if(jsonResourceShiftCheckin.getIsApproved()!=null){
			resourceShiftCheckIn.setIsApproved(jsonResourceShiftCheckin.getIsApproved());
		}
		if(jsonResourceShiftCheckin.getApprovalRemarks()!=""){
			resourceShiftCheckIn.setApprovalRemarks(jsonResourceShiftCheckin.getApprovalRemarks());
		}
		
		resourceShiftCheckIn.setApprovedDate(new Date(System.currentTimeMillis()));
	
		UserList approverUser=new UserList();
		approverUser.setUserId(approverId);
		resourceShiftCheckIn.setApproverUser(approverUser);
		resourceShiftCheckinDAO.updateResourceShiftCkeckIn(resourceShiftCheckIn);
		
		
	}

	@Override
	public ActualShift listActualShiftbyActualShiftId(int actualshiftId) {
		return workShiftMasterDAO.listActualShiftbyActualShiftId(actualshiftId);
	}

	@Override
	public List<ProductMaster> getProductByResourceShiftCheckInId(
			int resourceShiftCheckInId) {
		return resourceShiftCheckinDAO.getProductByResourceShiftCheckInId(resourceShiftCheckInId);
	}
	
	@Override
	public void deleteResorceShiftCheckIn(ResourceShiftCheckIn resourceShiftCheckIn) {
		resourceShiftCheckinDAO.deleteResorceShiftCheckIn(resourceShiftCheckIn);
	}

	@Override
	public List<ResourceShiftCheckIn> getResourceShiftCheckInByDateAndShift(Date createdDate,int actualShiftId) {
		return resourceShiftCheckinDAO.getResourceShiftCheckInByDateAndShift(createdDate,actualShiftId);
	}

	@Override
	public List<JsonTimeSheetEntryMaster> getTimeSheetEntriesOfResourceShiftCheckInId(
			int resourceShiftCheckInId) {
		List<TimeSheetEntryMaster> listOfTimeEntriesForShiftCheckIn = null;
		List<JsonTimeSheetEntryMaster> jsonTimeSheetEntryMaster = new ArrayList<JsonTimeSheetEntryMaster>();	
		listOfTimeEntriesForShiftCheckIn = timeSheetManagementDAO.getTimeSheetEntriesOfResourceShiftCheckInId(resourceShiftCheckInId);
		for(TimeSheetEntryMaster timeSheetEntry: listOfTimeEntriesForShiftCheckIn){						
			jsonTimeSheetEntryMaster.add(new JsonTimeSheetEntryMaster(timeSheetEntry));
		}
		return jsonTimeSheetEntryMaster;
	}
	
	@Override
	@Transactional
	public List<TimeSheetEntryMaster> getTimeSheetEntriesOfResourceShiftCheckIn(int resourceShiftCheckInId) {
		List<TimeSheetEntryMaster> listOfTimeEntriesForShiftCheckIn = null;
		listOfTimeEntriesForShiftCheckIn = timeSheetManagementDAO.getTimeSheetEntriesOfResourceShiftCheckInId(resourceShiftCheckInId);
		return listOfTimeEntriesForShiftCheckIn;
	}

	@Override
	public List<ResourceShiftCheckIn> getResourceShiftCheckInByDate(
			Date createdDate, Integer userId) {
		return resourceShiftCheckinDAO.getResourceShiftCheckInByDate(createdDate,userId);
	}

}
