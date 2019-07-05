package com.hcl.atf.taf.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hcl.atf.taf.controller.utilities.DateUtility;
import com.hcl.atf.taf.dao.ProductMasterDAO;
import com.hcl.atf.taf.dao.ProductUserRoleDAO;
import com.hcl.atf.taf.dao.ResourceAvailabilityDAO;
import com.hcl.atf.taf.dao.ResourcePerformanceDAO;
import com.hcl.atf.taf.dao.ResourceShiftCheckinDAO;
import com.hcl.atf.taf.dao.SkillDAO;
import com.hcl.atf.taf.dao.TestFactoryDao;
import com.hcl.atf.taf.dao.TestFactoryResourceReservationDao;
import com.hcl.atf.taf.dao.TestfactoryResourcePoolDAO;
import com.hcl.atf.taf.dao.TimeSheetManagementDAO;
import com.hcl.atf.taf.dao.UserListDAO;
import com.hcl.atf.taf.dao.WorkPackageDAO;
import com.hcl.atf.taf.dao.WorkShiftMasterDAO;
import com.hcl.atf.taf.model.ActualShift;
import com.hcl.atf.taf.model.PerformanceLevel;
import com.hcl.atf.taf.model.ProductMaster;
import com.hcl.atf.taf.model.ResourceAvailability;
import com.hcl.atf.taf.model.ResourceDailyPerformance;
import com.hcl.atf.taf.model.ResourceShiftCheckIn;
import com.hcl.atf.taf.model.ShiftTypeMaster;
import com.hcl.atf.taf.model.Skill;
import com.hcl.atf.taf.model.TestFactory;
import com.hcl.atf.taf.model.TestFactoryResourceReservation;
import com.hcl.atf.taf.model.TestfactoryResourcePool;
import com.hcl.atf.taf.model.TimeSheetEntryMaster;
import com.hcl.atf.taf.model.UserList;
import com.hcl.atf.taf.model.UserResourcePoolMapping;
import com.hcl.atf.taf.model.UserRoleMaster;
import com.hcl.atf.taf.model.UserSkills;
import com.hcl.atf.taf.model.UserTypeMasterNew;
import com.hcl.atf.taf.model.WorkPackage;
import com.hcl.atf.taf.model.WorkPackageDemandProjection;
import com.hcl.atf.taf.model.WorkShiftMaster;
import com.hcl.atf.taf.model.dto.ResourceAttendanceSummaryDTO;
import com.hcl.atf.taf.model.dto.ResourceAvailabilityDTO;
import com.hcl.atf.taf.model.dto.ResourceCountDTO;
import com.hcl.atf.taf.model.dto.ResourcePoolSummaryDTO;
import com.hcl.atf.taf.model.dto.TestFactoryResourceReservationDTO;
import com.hcl.atf.taf.model.dto.UserWeekUtilisedTimeDTO;
import com.hcl.atf.taf.model.dto.WeeklyResourceDemandDTO;
import com.hcl.atf.taf.model.dto.WeeklyResourceReservationDTO;
import com.hcl.atf.taf.model.dto.WorkPackageDemandProjectionDTO;
import com.hcl.atf.taf.model.dto.WorkPackageDemandProjectionStatisticsDTO;
import com.hcl.atf.taf.model.json.JsonReservedResourcesForBooking;
import com.hcl.atf.taf.model.json.JsonResourceAttendance;
import com.hcl.atf.taf.model.json.JsonResourceAvailability;
import com.hcl.atf.taf.model.json.JsonResourceAvailabilityDetails;
import com.hcl.atf.taf.model.json.JsonResourceAvailabilityPlan;
import com.hcl.atf.taf.model.json.JsonResourcePoolSummary;
import com.hcl.atf.taf.model.json.JsonResourceShiftCheckin;
import com.hcl.atf.taf.model.json.JsonResourceShiftCheckinDetailsForWeek;
import com.hcl.atf.taf.model.json.JsonTestFactoryResourceReservation;
import com.hcl.atf.taf.model.json.JsonUserList;
import com.hcl.atf.taf.model.json.JsonWorkPackageDemandProjection;
import com.hcl.atf.taf.model.json.JsonWorkPackageResourceReservation;
import com.hcl.atf.taf.model.json.JsonWorkPackageResultsStatistics;
import com.hcl.atf.taf.model.json.JsonWorkPackageWeeklyDemandProjection;
import com.hcl.atf.taf.model.json.JsonWorkPackageWeeklyResourceReservation;
import com.hcl.atf.taf.mongodb.service.MongoDBService;
import com.hcl.atf.taf.service.ResourceManagementService;

@Service
public class ResourceManagementServiceImpl implements ResourceManagementService {

	private static final Log log = LogFactory.getLog(ResourceManagementServiceImpl.class);

	@Autowired
	private ResourceAvailabilityDAO resourceAvailabilityDAO;
	@Autowired
	private UserListDAO userListDAO;
	
	@Autowired
	private SkillDAO skillDAO;
	@Autowired
	private WorkPackageDAO workPackageDAO;
	@Autowired
	private TestfactoryResourcePoolDAO testfactoryResourcePoolDAO;
	@Autowired
	private TestFactoryDao testFactoryDao;

	@Autowired
	private TestFactoryResourceReservationDao testFactoryResourceReservationDao;

	
	@Autowired
	private MongoDBService  mongoDBService;

	
	@Autowired
	private ProductMasterDAO productMasterDAO;
	
	@Autowired
	private WorkShiftMasterDAO workShiftMasterDAO;
	
	@Autowired
	private TimeSheetManagementDAO timeSheetManagementDAO;
	
	@Autowired
	private ResourcePerformanceDAO resourcePerformanceDAO;
	
	@Autowired
	private ResourceShiftCheckinDAO resourceShiftCheckinDAO;
	
	@Autowired
	private ProductUserRoleDAO productUserRoleDAO;
	

	@Override
	@Transactional
	public List<JsonResourceAvailability> listWorkPackageDemandProjection(int resourceId, int weekNo) {
		Date startDate = DateUtility.getDateForDayOfWeek(weekNo,0);
		log.info("startDate="+startDate);
		Date endDate = DateUtility.getDateForDayOfWeek(weekNo,6);
		log.info("endDate="+endDate);
		UserList user=userListDAO.getByUserId(resourceId);
		Set<TestFactory> testFactoryList= user.getResourcePool().getTestFactoryList();
		List<ShiftTypeMaster> shiftTypeMasterList =  resourceAvailabilityDAO.listShiftTypeMaster();
		Map<String, JsonResourceAvailability> jsonResourceAvailabilityShiftMap = new HashMap<String, JsonResourceAvailability>();
		JsonResourceAvailability jsonResourceAvailabilityShift = null;
		int counter = 1;
		for(ShiftTypeMaster shiftType : shiftTypeMasterList){

			jsonResourceAvailabilityShift = new JsonResourceAvailability();
			jsonResourceAvailabilityShift.setResourceAvailabilityId(counter++);
			jsonResourceAvailabilityShift.setResourceId(resourceId);
			jsonResourceAvailabilityShift.setShiftId(shiftType.getShiftTypeId());
			jsonResourceAvailabilityShift.setShiftName(shiftType.getShiftName());
			jsonResourceAvailabilityShift.setWeekNo(weekNo);
			
			jsonResourceAvailabilityShiftMap.put(jsonResourceAvailabilityShift.getShiftName(), jsonResourceAvailabilityShift);
			
		}
		
		List<ResourceAvailability> resourceAvailabilityList =  resourceAvailabilityDAO.listResourceAvailability(resourceId, startDate, endDate);
		List<JsonResourceAvailability> jsonWorkPackageDemandProjections = new ArrayList<JsonResourceAvailability>();
	
		if(resourceAvailabilityList== null || resourceAvailabilityList.isEmpty()){
			
		} else {
			for (ResourceAvailability resourceAvailability : resourceAvailabilityList) {
			
				Integer availabilityStatus = 0;
				if (resourceAvailability.getIsAvailable() == null || resourceAvailability.getIsAvailable().intValue()==0) {
					availabilityStatus = 0;
				} else {
					availabilityStatus = 1;
				}
				JsonResourceAvailability shiftJsonResourceAvailability = jsonResourceAvailabilityShiftMap.get(resourceAvailability.getShiftTypeMaster().getShiftName());
				if (shiftJsonResourceAvailability != null) {
					int dayOfWeek = DateUtility.getDayOfWeek(resourceAvailability.getWorkDate());
					shiftJsonResourceAvailability = loadResourceDemand(shiftJsonResourceAvailability, availabilityStatus, dayOfWeek);
					jsonResourceAvailabilityShiftMap.put(shiftJsonResourceAvailability.getShiftName(), shiftJsonResourceAvailability);
				}
			}
		}
		
		jsonWorkPackageDemandProjections = new ArrayList<JsonResourceAvailability>(jsonResourceAvailabilityShiftMap.values());
		return jsonWorkPackageDemandProjections;
	}

	private JsonWorkPackageDemandProjection setDateNames(JsonWorkPackageDemandProjection jsonWorkPackageDemandProjectionShift, List<String> dateNames) {
	
		log.info("Ist Date : " + dateNames.get(0));
		return jsonWorkPackageDemandProjectionShift;
	}

	private JsonResourceAvailability loadResourceDemand(JsonResourceAvailability jsonResourceAvailabilityShift, int resourceCount, int dayOfWeek) {
		log.info("dayOfWeek="+dayOfWeek);
		log.info("day of the week"+DateUtility.getDateNamesOfWeek(dayOfWeek));
		log.info("resourceCount="+resourceCount);
		switch(dayOfWeek) {
			// Please dont change this ordering of case numbers
			case 1: // Sunday
				jsonResourceAvailabilityShift.setDay7ResourceCount(resourceCount);
				break;
				
			case 2:  // Monday
				jsonResourceAvailabilityShift.setDay1ResourceCount(resourceCount);
				break;
			case 3:
				jsonResourceAvailabilityShift.setDay2ResourceCount(resourceCount);
				break;
			case 4:
				jsonResourceAvailabilityShift.setDay3ResourceCount(resourceCount);
				break;
			case 5:
				jsonResourceAvailabilityShift.setDay4ResourceCount(resourceCount);
				break;
			case 6:
				jsonResourceAvailabilityShift.setDay5ResourceCount(resourceCount);
				break;
			case 7:
				jsonResourceAvailabilityShift.setDay6ResourceCount(resourceCount);
				break;
		}
	
		return jsonResourceAvailabilityShift;
	}

	
	@Override
	@Transactional
	public JsonResourceAvailability updateWorkPackageDemandProjection(JsonResourceAvailability jsonResourceAvailability) {
		
		log.info("updateWorkPackageDemandProjection in serviceImpl");

		log.info("ShiftId : "+jsonResourceAvailability.getShiftId());
		log.info("ShiftId : "+jsonResourceAvailability.getShiftName());
		log.info("Resource Id : "+jsonResourceAvailability.getResourceId());
		log.info("ResourceCount : "+jsonResourceAvailability.getDay1ResourceCount());

		int weekNo = jsonResourceAvailability.getWeekNo();
		Date workDate = DateUtility.getDateForDayOfWeek(weekNo,1);
		log.info("Date"+workDate);
		
		WorkShiftMaster workShiftMaster = new WorkShiftMaster();
		workShiftMaster.setShiftId(jsonResourceAvailability.getShiftId());
		
		ShiftTypeMaster shiftTypeMaster = new ShiftTypeMaster();
		shiftTypeMaster.setShiftTypeId(jsonResourceAvailability.getShiftId());
		
		UserList resource = new UserList();
		resource.setUserId(jsonResourceAvailability.getResourceId());

		ResourceAvailability resourceAvailability = null;
		List<ResourceAvailability> resourceAvailabilityList = new ArrayList<ResourceAvailability>();
			
		//Monday
		resourceAvailability = new ResourceAvailability();
		resourceAvailability.setIsAvailable(jsonResourceAvailability.getDay1ResourceCount());
		workDate = DateUtility.getDateForDayOfWeek(weekNo,0);
		resourceAvailability.setWorkDate(workDate);
		resourceAvailability.setWorkShiftMaster(workShiftMaster);
		resourceAvailability.setShiftTypeMaster(shiftTypeMaster);
		resourceAvailability.setResource(resource);
		resourceAvailability.setBookForShift(0);
		resourceAvailabilityList.add(resourceAvailability);
		//Tuesday
		resourceAvailability = new ResourceAvailability();
		resourceAvailability.setIsAvailable(jsonResourceAvailability.getDay2ResourceCount());
		workDate = DateUtility.getDateForDayOfWeek(weekNo,1);
		resourceAvailability.setWorkDate(workDate);
		resourceAvailability.setWorkShiftMaster(workShiftMaster);
		resourceAvailability.setShiftTypeMaster(shiftTypeMaster);
		resourceAvailability.setResource(resource);
		resourceAvailability.setBookForShift(0);
		resourceAvailabilityList.add(resourceAvailability);
		//Wednesday
		resourceAvailability = new ResourceAvailability();
		resourceAvailability.setIsAvailable(jsonResourceAvailability.getDay3ResourceCount());
		workDate = DateUtility.getDateForDayOfWeek(weekNo,2);
		resourceAvailability.setWorkDate(workDate);
		resourceAvailability.setWorkShiftMaster(workShiftMaster);
		resourceAvailability.setShiftTypeMaster(shiftTypeMaster);
		resourceAvailability.setResource(resource);
		resourceAvailability.setBookForShift(0);
		resourceAvailabilityList.add(resourceAvailability);
		//Thursday
		resourceAvailability = new ResourceAvailability();
		resourceAvailability.setIsAvailable(jsonResourceAvailability.getDay4ResourceCount());
		workDate = DateUtility.getDateForDayOfWeek(weekNo,3);
		resourceAvailability.setWorkDate(workDate);
		resourceAvailability.setWorkShiftMaster(workShiftMaster);
		resourceAvailability.setShiftTypeMaster(shiftTypeMaster);
		resourceAvailability.setResource(resource);
		resourceAvailability.setBookForShift(0);
		resourceAvailabilityList.add(resourceAvailability);
		//Friday
		resourceAvailability = new ResourceAvailability();
		resourceAvailability.setIsAvailable(jsonResourceAvailability.getDay5ResourceCount());
		workDate = DateUtility.getDateForDayOfWeek(weekNo,4);
		resourceAvailability.setWorkDate(workDate);
		resourceAvailability.setWorkShiftMaster(workShiftMaster);
		resourceAvailability.setShiftTypeMaster(shiftTypeMaster);
		resourceAvailability.setResource(resource);
		resourceAvailability.setBookForShift(0);
		resourceAvailabilityList.add(resourceAvailability);
		//Saturday
		resourceAvailability = new ResourceAvailability();
		resourceAvailability.setIsAvailable(jsonResourceAvailability.getDay6ResourceCount());
		workDate = DateUtility.getDateForDayOfWeek(weekNo,5);
		resourceAvailability.setWorkDate(workDate);
		resourceAvailability.setWorkShiftMaster(workShiftMaster);
		resourceAvailability.setShiftTypeMaster(shiftTypeMaster);
		resourceAvailability.setResource(resource);
		resourceAvailability.setBookForShift(0);
		resourceAvailabilityList.add(resourceAvailability);
		//Sunday
		resourceAvailability = new ResourceAvailability();
		resourceAvailability.setIsAvailable(jsonResourceAvailability.getDay7ResourceCount());
		workDate = DateUtility.getDateForDayOfWeek(weekNo,6);
		resourceAvailability.setWorkDate(workDate);
		resourceAvailability.setWorkShiftMaster(workShiftMaster);
		resourceAvailability.setShiftTypeMaster(shiftTypeMaster);
		resourceAvailability.setResource(resource);
		resourceAvailability.setBookForShift(0);
		resourceAvailabilityList.add(resourceAvailability);
		
		// Update in DB
		List<ResourceAvailability> updatedResourceAvailabilityList = resourceAvailabilityDAO.updateResourceAvailability(resourceAvailabilityList);
		
		return jsonResourceAvailability;
	}

	@Override
	@Transactional
	public List<TestfactoryResourcePool> getTestfactoryResourcePoolList() {		
		return testfactoryResourcePoolDAO.listResourcePool();
	}

	@Override
	@Transactional
	public List<TestfactoryResourcePool> getTestfactoryResourcePoolListbyLabId(
			int testFactoryLabId) {
		return testfactoryResourcePoolDAO.listResourcePoolbytestFactoryLabId(testFactoryLabId);
	}

	@Override
	@Transactional
	public void addResourcePool(TestfactoryResourcePool resourcePool) {
		 testfactoryResourcePoolDAO.addResourcePool(resourcePool);		
	}

	@Override
	@Transactional
	public TestfactoryResourcePool getTestfactoryResourcePoolListbyId(
			int resourcePoolId) {
		return testfactoryResourcePoolDAO.getbyresourcePoolId(resourcePoolId);				
				
	}

	@Override
	@Transactional
	public TestfactoryResourcePool getResourcePoolListbyLabId(
			int testFactoryLabId) {
		return testfactoryResourcePoolDAO.getResourcePoolbytestFactoryLabId(testFactoryLabId);
	}

	@Override
	@Transactional
	public void updateResourcePool(TestfactoryResourcePool resourcePool) {
		testfactoryResourcePoolDAO.updateResourcePool(resourcePool);
		
	}
	@Override
	@Transactional
	public List<UserList> getResourcePoolwithRole(int resourcePoolId) {
		
		List<UserRoleMaster> roleList = new ArrayList<UserRoleMaster>();
		roleList = productMasterDAO.getAllRoles();
		List<ResourcePoolSummaryDTO> userbyRPDTO = resourceAvailabilityDAO.getResourcePoolwithRoleDTO(resourcePoolId, roleList);
		return resourceAvailabilityDAO.getResourcePoolwithRole(resourcePoolId);
	}
	
	
	@Override
	@Transactional
	public List<JsonResourcePoolSummary> getResourcePoolwithRoleJson(int resourcePoolId) {
		
		List<UserRoleMaster> roleList = new ArrayList<UserRoleMaster>();
		List<JsonResourcePoolSummary> jsonResourcePoolSummaryList = new ArrayList<JsonResourcePoolSummary>();		
		roleList = productMasterDAO.getAllRoles();
		List<ResourcePoolSummaryDTO> userListbyRPDTO = resourceAvailabilityDAO.getResourcePoolwithRoleDTO(resourcePoolId, roleList);
		JsonResourcePoolSummary jsonResourcePoolSummaryCore = new JsonResourcePoolSummary();
		jsonResourcePoolSummaryCore.setJsonResourcePoolSummaryId(1);
		jsonResourcePoolSummaryCore.setResourcePoolId(resourcePoolId);
		jsonResourcePoolSummaryCore.setUserTypeId(1);

		JsonResourcePoolSummary jsonResourcePoolSummaryFlexi = new JsonResourcePoolSummary();
		jsonResourcePoolSummaryFlexi.setJsonResourcePoolSummaryId(2);
		jsonResourcePoolSummaryFlexi.setResourcePoolId(resourcePoolId);
		jsonResourcePoolSummaryFlexi.setUserTypeId(2);
		
		JsonResourcePoolSummary jsonResourcePoolSummaryContract = new JsonResourcePoolSummary();
		jsonResourcePoolSummaryContract.setJsonResourcePoolSummaryId(3);
		jsonResourcePoolSummaryContract.setResourcePoolId(resourcePoolId);
		jsonResourcePoolSummaryContract.setUserTypeId(3);
		
		JsonResourcePoolSummary jsonResourcePoolSummaryOnsite = new JsonResourcePoolSummary();
		jsonResourcePoolSummaryOnsite.setJsonResourcePoolSummaryId(4);
		jsonResourcePoolSummaryOnsite.setResourcePoolId(resourcePoolId);
		jsonResourcePoolSummaryOnsite.setUserTypeId(4);
		
		JsonResourcePoolSummary jsonResourcePoolSummaryOffshore = new JsonResourcePoolSummary();
		jsonResourcePoolSummaryOffshore.setJsonResourcePoolSummaryId(5);
		jsonResourcePoolSummaryOffshore.setResourcePoolId(resourcePoolId);
		jsonResourcePoolSummaryOffshore.setUserTypeId(5);
		
		
		if(userListbyRPDTO != null && userListbyRPDTO.size() !=0){
			for(ResourcePoolSummaryDTO rpdto : userListbyRPDTO){
				
				if (rpdto.getUserTypeId() == 1) {
					jsonResourcePoolSummaryCore.setResourcePoolName(rpdto.getResourcePoolName());
					jsonResourcePoolSummaryCore.setUserTypeLabel(rpdto.getUserTypeLabel());
					int userRoleId = rpdto.getUserRoleId();
					if(userRoleId == 2){
						if(jsonResourcePoolSummaryCore.getEngagementManager_Count() != null && jsonResourcePoolSummaryCore.getEngagementManager_Count() !=0){
							jsonResourcePoolSummaryCore.setEngagementManager_Count( jsonResourcePoolSummaryCore.getEngagementManager_Count()+ rpdto.getRole_Count());
						}else{
							jsonResourcePoolSummaryCore.setEngagementManager_Count( rpdto.getRole_Count());
						}						
					}else if(userRoleId == 3){
						if(jsonResourcePoolSummaryCore.getResourceManager_Count()  != null && jsonResourcePoolSummaryCore.getResourceManager_Count()  !=0){
							jsonResourcePoolSummaryCore.setTestManager_Count(jsonResourcePoolSummaryCore.getResourceManager_Count() + rpdto.getRole_Count());	
						}else{
							jsonResourcePoolSummaryCore.setTestManager_Count( rpdto.getRole_Count());
						}
											
					}else if(userRoleId == 4){
						if(jsonResourcePoolSummaryCore.getTestLead_Count() != null && jsonResourcePoolSummaryCore.getTestLead_Count()  !=0){
							jsonResourcePoolSummaryCore.setTestLead_Count(jsonResourcePoolSummaryCore.getTestLead_Count() + rpdto.getRole_Count());
						}else{
							jsonResourcePoolSummaryCore.setTestLead_Count(rpdto.getRole_Count());
						}
											
					}else if(userRoleId == 5){
						if(jsonResourcePoolSummaryCore.getTester_Count() != null && jsonResourcePoolSummaryCore.getTester_Count() !=0){
							jsonResourcePoolSummaryCore.setTester_Count(jsonResourcePoolSummaryCore.getTester_Count() + rpdto.getRole_Count());
						}else{
							jsonResourcePoolSummaryCore.setTester_Count(rpdto.getRole_Count());
						}											
					}else if(userRoleId == 6){
						if(jsonResourcePoolSummaryCore.getProgramManager_Count() != null && jsonResourcePoolSummaryCore.getProgramManager_Count() !=0){
							jsonResourcePoolSummaryCore.setProgramManager_Count(jsonResourcePoolSummaryCore.getProgramManager_Count() + rpdto.getRole_Count());
						}else{
							jsonResourcePoolSummaryCore.setProgramManager_Count(rpdto.getRole_Count());
						}											
					}else if(userRoleId == 7){
						if(jsonResourcePoolSummaryCore.getResourceManager_Count() != null && jsonResourcePoolSummaryCore.getResourceManager_Count() != 0){
							jsonResourcePoolSummaryCore.setResourceManager_Count(jsonResourcePoolSummaryCore.getResourceManager_Count() + rpdto.getRole_Count());	
						}else{
							jsonResourcePoolSummaryCore.setResourceManager_Count( rpdto.getRole_Count());
						}
											
					}
				} 
				else if (rpdto.getUserTypeId() == 2) {
					jsonResourcePoolSummaryContract.setResourcePoolName(rpdto.getResourcePoolName());
					jsonResourcePoolSummaryContract.setUserTypeLabel(rpdto.getUserTypeLabel());
					int userRoleId = rpdto.getUserRoleId();
					if(userRoleId == 2){
						if(jsonResourcePoolSummaryContract.getEngagementManager_Count() != null && jsonResourcePoolSummaryContract.getEngagementManager_Count() !=0){
							jsonResourcePoolSummaryContract.setEngagementManager_Count( jsonResourcePoolSummaryContract.getEngagementManager_Count()+ rpdto.getRole_Count());
						}else{
							jsonResourcePoolSummaryContract.setEngagementManager_Count( rpdto.getRole_Count());
						}						
					}else if(userRoleId == 3){
						if(jsonResourcePoolSummaryContract.getResourceManager_Count()  != null && jsonResourcePoolSummaryContract.getResourceManager_Count()  !=0){
							jsonResourcePoolSummaryContract.setTestManager_Count(jsonResourcePoolSummaryContract.getResourceManager_Count() + rpdto.getRole_Count());	
						}else{
							jsonResourcePoolSummaryContract.setTestManager_Count( rpdto.getRole_Count());
						}						
											
					}else if(userRoleId == 4){
						if(jsonResourcePoolSummaryContract.getTestLead_Count() != null && jsonResourcePoolSummaryContract.getTestLead_Count()  !=0){
							jsonResourcePoolSummaryContract.setTestLead_Count(jsonResourcePoolSummaryContract.getTestLead_Count() + rpdto.getRole_Count());
						}else{
							jsonResourcePoolSummaryContract.setTestLead_Count(rpdto.getRole_Count());
						}											
					}else if(userRoleId == 5){
						if(jsonResourcePoolSummaryContract.getTester_Count() != null && jsonResourcePoolSummaryContract.getTester_Count() !=0){
							jsonResourcePoolSummaryContract.setTester_Count(jsonResourcePoolSummaryContract.getTester_Count() + rpdto.getRole_Count());
						}else{
							jsonResourcePoolSummaryContract.setTester_Count(rpdto.getRole_Count());
						}										
					}else if(userRoleId == 6){
						if(jsonResourcePoolSummaryContract.getProgramManager_Count() != null && jsonResourcePoolSummaryContract.getProgramManager_Count() !=0){
							jsonResourcePoolSummaryContract.setProgramManager_Count(jsonResourcePoolSummaryContract.getProgramManager_Count() + rpdto.getRole_Count());
						}else{
							jsonResourcePoolSummaryContract.setProgramManager_Count(rpdto.getRole_Count());
						}											
					}else if(userRoleId == 7){
						if(jsonResourcePoolSummaryContract.getResourceManager_Count() != null && jsonResourcePoolSummaryContract.getResourceManager_Count() != 0){
							jsonResourcePoolSummaryContract.setResourceManager_Count(jsonResourcePoolSummaryContract.getResourceManager_Count() + rpdto.getRole_Count());	
						}else{
							jsonResourcePoolSummaryContract.setResourceManager_Count( rpdto.getRole_Count());
						}											
					}
				}
				else if (rpdto.getUserTypeId() == 3) {
					jsonResourcePoolSummaryFlexi.setResourcePoolName(rpdto.getResourcePoolName());
					jsonResourcePoolSummaryFlexi.setUserTypeLabel(rpdto.getUserTypeLabel());
					int userRoleId = rpdto.getUserRoleId();
					if(userRoleId == 2){
						if(jsonResourcePoolSummaryFlexi.getEngagementManager_Count() != null && jsonResourcePoolSummaryFlexi.getEngagementManager_Count() !=0){
							jsonResourcePoolSummaryFlexi.setEngagementManager_Count( jsonResourcePoolSummaryFlexi.getEngagementManager_Count()+ rpdto.getRole_Count());
						}else{
							jsonResourcePoolSummaryFlexi.setEngagementManager_Count( rpdto.getRole_Count());
						}					
					}else if(userRoleId == 3){
						if(jsonResourcePoolSummaryFlexi.getResourceManager_Count()  != null && jsonResourcePoolSummaryFlexi.getResourceManager_Count()  !=0){
							jsonResourcePoolSummaryFlexi.setTestManager_Count(jsonResourcePoolSummaryFlexi.getResourceManager_Count() + rpdto.getRole_Count());	
						}else{
							jsonResourcePoolSummaryFlexi.setTestManager_Count( rpdto.getRole_Count());
						}										
					}else if(userRoleId == 4){
						if(jsonResourcePoolSummaryFlexi.getTestLead_Count() != null && jsonResourcePoolSummaryFlexi.getTestLead_Count()  !=0){
							jsonResourcePoolSummaryFlexi.setTestLead_Count(jsonResourcePoolSummaryFlexi.getTestLead_Count() + rpdto.getRole_Count());
						}else{
							jsonResourcePoolSummaryFlexi.setTestLead_Count(rpdto.getRole_Count());
						}											
					}else if(userRoleId == 5){
						if(jsonResourcePoolSummaryFlexi.getTester_Count() != null && jsonResourcePoolSummaryFlexi.getTester_Count() !=0){
							jsonResourcePoolSummaryFlexi.setTester_Count(jsonResourcePoolSummaryFlexi.getTester_Count() + rpdto.getRole_Count());
						}else{
							jsonResourcePoolSummaryFlexi.setTester_Count(rpdto.getRole_Count());
						}											
					}else if(userRoleId == 6){
						if(jsonResourcePoolSummaryFlexi.getProgramManager_Count() != null && jsonResourcePoolSummaryFlexi.getProgramManager_Count() !=0){
							jsonResourcePoolSummaryFlexi.setProgramManager_Count(jsonResourcePoolSummaryFlexi.getProgramManager_Count() + rpdto.getRole_Count());
						}else{
							jsonResourcePoolSummaryFlexi.setProgramManager_Count(rpdto.getRole_Count());
						}					
					}else if(userRoleId == 7){
						if(jsonResourcePoolSummaryFlexi.getResourceManager_Count() != null && jsonResourcePoolSummaryFlexi.getResourceManager_Count() != 0){
							jsonResourcePoolSummaryFlexi.setResourceManager_Count(jsonResourcePoolSummaryFlexi.getResourceManager_Count() + rpdto.getRole_Count());	
						}else{
							jsonResourcePoolSummaryFlexi.setResourceManager_Count( rpdto.getRole_Count());
						}											
					}
				}
				else if (rpdto.getUserTypeId() == 4) {
					jsonResourcePoolSummaryOnsite.setResourcePoolName(rpdto.getResourcePoolName());
					jsonResourcePoolSummaryOnsite.setUserTypeLabel(rpdto.getUserTypeLabel());
					int userRoleId = rpdto.getUserRoleId();
					if(userRoleId == 2){
						if(jsonResourcePoolSummaryOnsite.getEngagementManager_Count() != null && jsonResourcePoolSummaryContract.getEngagementManager_Count() !=0){
							jsonResourcePoolSummaryOnsite.setEngagementManager_Count( jsonResourcePoolSummaryContract.getEngagementManager_Count()+ rpdto.getRole_Count());
						}else{
							jsonResourcePoolSummaryOnsite.setEngagementManager_Count( rpdto.getRole_Count());
						}						
					}else if(userRoleId == 3){
						if(jsonResourcePoolSummaryOnsite.getResourceManager_Count()  != null && jsonResourcePoolSummaryContract.getResourceManager_Count()  !=0){
							jsonResourcePoolSummaryOnsite.setTestManager_Count(jsonResourcePoolSummaryContract.getResourceManager_Count() + rpdto.getRole_Count());	
						}else{
							jsonResourcePoolSummaryOnsite.setTestManager_Count( rpdto.getRole_Count());
						}						
											
					}else if(userRoleId == 4){
						if(jsonResourcePoolSummaryOnsite.getTestLead_Count() != null && jsonResourcePoolSummaryContract.getTestLead_Count()  !=0){
							jsonResourcePoolSummaryOnsite.setTestLead_Count(jsonResourcePoolSummaryContract.getTestLead_Count() + rpdto.getRole_Count());
						}else{
							jsonResourcePoolSummaryOnsite.setTestLead_Count(rpdto.getRole_Count());
						}											
					}else if(userRoleId == 5){
						if(jsonResourcePoolSummaryOnsite.getTester_Count() != null && jsonResourcePoolSummaryContract.getTester_Count() !=0){
							jsonResourcePoolSummaryOnsite.setTester_Count(jsonResourcePoolSummaryContract.getTester_Count() + rpdto.getRole_Count());
						}else{
							jsonResourcePoolSummaryOnsite.setTester_Count(rpdto.getRole_Count());
						}										
					}else if(userRoleId == 6){
						if(jsonResourcePoolSummaryOnsite.getProgramManager_Count() != null && jsonResourcePoolSummaryContract.getProgramManager_Count() !=0){
							jsonResourcePoolSummaryOnsite.setProgramManager_Count(jsonResourcePoolSummaryContract.getProgramManager_Count() + rpdto.getRole_Count());
						}else{
							jsonResourcePoolSummaryOnsite.setProgramManager_Count(rpdto.getRole_Count());
						}											
					}else if(userRoleId == 7){
						if(jsonResourcePoolSummaryOnsite.getResourceManager_Count() != null && jsonResourcePoolSummaryContract.getResourceManager_Count() != 0){
							jsonResourcePoolSummaryOnsite.setResourceManager_Count(jsonResourcePoolSummaryContract.getResourceManager_Count() + rpdto.getRole_Count());	
						}else{
							jsonResourcePoolSummaryOnsite.setResourceManager_Count( rpdto.getRole_Count());
						}											
					}
				}
				else if (rpdto.getUserTypeId() == 5) {
					jsonResourcePoolSummaryOffshore.setResourcePoolName(rpdto.getResourcePoolName());
					jsonResourcePoolSummaryOffshore.setUserTypeLabel(rpdto.getUserTypeLabel());
					int userRoleId = rpdto.getUserRoleId();
					if(userRoleId == 2){
						if(jsonResourcePoolSummaryOffshore.getEngagementManager_Count() != null && jsonResourcePoolSummaryContract.getEngagementManager_Count() !=0){
							jsonResourcePoolSummaryOffshore.setEngagementManager_Count( jsonResourcePoolSummaryContract.getEngagementManager_Count()+ rpdto.getRole_Count());
						}else{
							jsonResourcePoolSummaryOffshore.setEngagementManager_Count( rpdto.getRole_Count());
						}						
					}else if(userRoleId == 3){
						if(jsonResourcePoolSummaryOffshore.getResourceManager_Count()  != null && jsonResourcePoolSummaryContract.getResourceManager_Count()  !=0){
							jsonResourcePoolSummaryOffshore.setTestManager_Count(jsonResourcePoolSummaryContract.getResourceManager_Count() + rpdto.getRole_Count());	
						}else{
							jsonResourcePoolSummaryOffshore.setTestManager_Count( rpdto.getRole_Count());
						}						
											
					}else if(userRoleId == 4){
						if(jsonResourcePoolSummaryOffshore.getTestLead_Count() != null && jsonResourcePoolSummaryContract.getTestLead_Count()  !=0){
							jsonResourcePoolSummaryOffshore.setTestLead_Count(jsonResourcePoolSummaryContract.getTestLead_Count() + rpdto.getRole_Count());
						}else{
							jsonResourcePoolSummaryOffshore.setTestLead_Count(rpdto.getRole_Count());
						}											
					}else if(userRoleId == 5){
						if(jsonResourcePoolSummaryOffshore.getTester_Count() != null && jsonResourcePoolSummaryContract.getTester_Count() !=0){
							jsonResourcePoolSummaryOffshore.setTester_Count(jsonResourcePoolSummaryContract.getTester_Count() + rpdto.getRole_Count());
						}else{
							jsonResourcePoolSummaryOffshore.setTester_Count(rpdto.getRole_Count());
						}										
					}else if(userRoleId == 6){
						if(jsonResourcePoolSummaryOffshore.getProgramManager_Count() != null && jsonResourcePoolSummaryContract.getProgramManager_Count() !=0){
							jsonResourcePoolSummaryOffshore.setProgramManager_Count(jsonResourcePoolSummaryContract.getProgramManager_Count() + rpdto.getRole_Count());
						}else{
							jsonResourcePoolSummaryOffshore.setProgramManager_Count(rpdto.getRole_Count());
						}											
					}else if(userRoleId == 7){
						if(jsonResourcePoolSummaryOffshore.getResourceManager_Count() != null && jsonResourcePoolSummaryContract.getResourceManager_Count() != 0){
							jsonResourcePoolSummaryOffshore.setResourceManager_Count(jsonResourcePoolSummaryContract.getResourceManager_Count() + rpdto.getRole_Count());	
						}else{
							jsonResourcePoolSummaryOffshore.setResourceManager_Count( rpdto.getRole_Count());
						}											
					}
				}
								
			}	
		jsonResourcePoolSummaryList.add(jsonResourcePoolSummaryCore);
		jsonResourcePoolSummaryList.add(jsonResourcePoolSummaryFlexi);
		jsonResourcePoolSummaryList.add(jsonResourcePoolSummaryContract);
		jsonResourcePoolSummaryList.add(jsonResourcePoolSummaryOnsite);
		jsonResourcePoolSummaryList.add(jsonResourcePoolSummaryOffshore);
	}return jsonResourcePoolSummaryList;	
	}
	@Override
	@Transactional
	public List<JsonResourcePoolSummary> getResourcePoolwithRoleJson(
			List<Integer> resourcePoolIdList) {
		List<UserRoleMaster> roleList = new ArrayList<UserRoleMaster>();
		List<JsonResourcePoolSummary> jsonResourcePoolSummaryList = new ArrayList<JsonResourcePoolSummary>();		
		roleList = productMasterDAO.getAllRoles();
		
		
		
		for(Integer resourcePoolId : resourcePoolIdList){
			List<ResourcePoolSummaryDTO> userListbyRPDTO = resourceAvailabilityDAO.getResourcePoolwithRoleDTO(resourcePoolId, roleList);
			JsonResourcePoolSummary jsonResourcePoolSummaryCore = new JsonResourcePoolSummary();
			jsonResourcePoolSummaryCore.setJsonResourcePoolSummaryId(1);
			jsonResourcePoolSummaryCore.setResourcePoolId(resourcePoolId);
			jsonResourcePoolSummaryCore.setUserTypeId(1);

			JsonResourcePoolSummary jsonResourcePoolSummaryFlexi = new JsonResourcePoolSummary();
			jsonResourcePoolSummaryFlexi.setJsonResourcePoolSummaryId(2);
			jsonResourcePoolSummaryFlexi.setResourcePoolId(resourcePoolId);
			jsonResourcePoolSummaryFlexi.setUserTypeId(2);
			
			JsonResourcePoolSummary jsonResourcePoolSummaryContract = new JsonResourcePoolSummary();
			jsonResourcePoolSummaryContract.setJsonResourcePoolSummaryId(3);
			jsonResourcePoolSummaryContract.setResourcePoolId(resourcePoolId);
			jsonResourcePoolSummaryContract.setUserTypeId(3);
			
			if(userListbyRPDTO != null && userListbyRPDTO.size() !=0){
				for(ResourcePoolSummaryDTO rpdto : userListbyRPDTO){
					
					if (rpdto.getUserTypeId() == 1) {
						jsonResourcePoolSummaryCore.setResourcePoolName(rpdto.getResourcePoolName());
						jsonResourcePoolSummaryCore.setUserTypeLabel(rpdto.getUserTypeLabel());
						int userRoleId = rpdto.getUserRoleId();
						if(userRoleId == 2){
							if(jsonResourcePoolSummaryCore.getEngagementManager_Count() != null && jsonResourcePoolSummaryCore.getEngagementManager_Count() !=0){
								jsonResourcePoolSummaryCore.setEngagementManager_Count( jsonResourcePoolSummaryCore.getEngagementManager_Count()+ rpdto.getRole_Count());
							}else{
								jsonResourcePoolSummaryCore.setEngagementManager_Count( rpdto.getRole_Count());
							}
						}else if(userRoleId == 3){
							if(jsonResourcePoolSummaryCore.getResourceManager_Count()  != null && jsonResourcePoolSummaryCore.getResourceManager_Count()  !=0){
								jsonResourcePoolSummaryCore.setTestManager_Count(jsonResourcePoolSummaryCore.getResourceManager_Count() + rpdto.getRole_Count());	
							}else{
								jsonResourcePoolSummaryCore.setTestManager_Count( rpdto.getRole_Count());
							}
												
						}else if(userRoleId == 4){
							if(jsonResourcePoolSummaryCore.getTestLead_Count() != null && jsonResourcePoolSummaryCore.getTestLead_Count()  !=0){
								jsonResourcePoolSummaryCore.setTestLead_Count(jsonResourcePoolSummaryCore.getTestLead_Count() + rpdto.getRole_Count());
							}else{
								jsonResourcePoolSummaryCore.setTestLead_Count(rpdto.getRole_Count());
							}
												
						}else if(userRoleId == 5){
							if(jsonResourcePoolSummaryCore.getTester_Count() != null && jsonResourcePoolSummaryCore.getTester_Count() !=0){
								jsonResourcePoolSummaryCore.setTester_Count(jsonResourcePoolSummaryCore.getTester_Count() + rpdto.getRole_Count());
							}else{
								jsonResourcePoolSummaryCore.setTester_Count(rpdto.getRole_Count());
							}											
						}else if(userRoleId == 6){
							if(jsonResourcePoolSummaryCore.getProgramManager_Count() != null && jsonResourcePoolSummaryCore.getProgramManager_Count() !=0){
								jsonResourcePoolSummaryCore.setProgramManager_Count(jsonResourcePoolSummaryCore.getProgramManager_Count() + rpdto.getRole_Count());
							}else{
								jsonResourcePoolSummaryCore.setProgramManager_Count(rpdto.getRole_Count());
							}											
						}else if(userRoleId == 7){
							if(jsonResourcePoolSummaryCore.getResourceManager_Count() != null && jsonResourcePoolSummaryCore.getResourceManager_Count() != 0){
								jsonResourcePoolSummaryCore.setResourceManager_Count(jsonResourcePoolSummaryCore.getResourceManager_Count() + rpdto.getRole_Count());	
							}else{
								jsonResourcePoolSummaryCore.setResourceManager_Count( rpdto.getRole_Count());
							}
												
						}
					} 
					else if (rpdto.getUserTypeId() == 2) {
						jsonResourcePoolSummaryContract.setResourcePoolName(rpdto.getResourcePoolName());
						jsonResourcePoolSummaryContract.setUserTypeLabel(rpdto.getUserTypeLabel());
						int userRoleId = rpdto.getUserRoleId();
						if(userRoleId == 2){
							if(jsonResourcePoolSummaryContract.getEngagementManager_Count() != null && jsonResourcePoolSummaryContract.getEngagementManager_Count() !=0){
								jsonResourcePoolSummaryContract.setEngagementManager_Count( jsonResourcePoolSummaryContract.getEngagementManager_Count()+ rpdto.getRole_Count());
							}else{
								jsonResourcePoolSummaryContract.setEngagementManager_Count( rpdto.getRole_Count());
							}						
						}else if(userRoleId == 3){
							if(jsonResourcePoolSummaryContract.getResourceManager_Count()  != null && jsonResourcePoolSummaryContract.getResourceManager_Count()  !=0){
								jsonResourcePoolSummaryContract.setTestManager_Count(jsonResourcePoolSummaryContract.getResourceManager_Count() + rpdto.getRole_Count());	
							}else{
								jsonResourcePoolSummaryContract.setTestManager_Count( rpdto.getRole_Count());
							}						
												
						}else if(userRoleId == 4){
							if(jsonResourcePoolSummaryContract.getTestLead_Count() != null && jsonResourcePoolSummaryContract.getTestLead_Count()  !=0){
								jsonResourcePoolSummaryContract.setTestLead_Count(jsonResourcePoolSummaryContract.getTestLead_Count() + rpdto.getRole_Count());
							}else{
								jsonResourcePoolSummaryContract.setTestLead_Count(rpdto.getRole_Count());
							}											
						}else if(userRoleId == 5){
							if(jsonResourcePoolSummaryContract.getTester_Count() != null && jsonResourcePoolSummaryContract.getTester_Count() !=0){
								jsonResourcePoolSummaryContract.setTester_Count(jsonResourcePoolSummaryContract.getTester_Count() + rpdto.getRole_Count());
							}else{
								jsonResourcePoolSummaryContract.setTester_Count(rpdto.getRole_Count());
							}										
						}else if(userRoleId == 6){
							if(jsonResourcePoolSummaryContract.getProgramManager_Count() != null && jsonResourcePoolSummaryContract.getProgramManager_Count() !=0){
								jsonResourcePoolSummaryContract.setProgramManager_Count(jsonResourcePoolSummaryContract.getProgramManager_Count() + rpdto.getRole_Count());
							}else{
								jsonResourcePoolSummaryContract.setProgramManager_Count(rpdto.getRole_Count());
							}											
						}else if(userRoleId == 7){
							if(jsonResourcePoolSummaryContract.getResourceManager_Count() != null && jsonResourcePoolSummaryContract.getResourceManager_Count() != 0){
								jsonResourcePoolSummaryContract.setResourceManager_Count(jsonResourcePoolSummaryContract.getResourceManager_Count() + rpdto.getRole_Count());	
							}else{
								jsonResourcePoolSummaryContract.setResourceManager_Count( rpdto.getRole_Count());
							}											
						}
					}
					else if (rpdto.getUserTypeId() == 3) {
						jsonResourcePoolSummaryFlexi.setResourcePoolName(rpdto.getResourcePoolName());
						jsonResourcePoolSummaryFlexi.setUserTypeLabel(rpdto.getUserTypeLabel());
						int userRoleId = rpdto.getUserRoleId();
						if(userRoleId == 2){
							if(jsonResourcePoolSummaryFlexi.getEngagementManager_Count() != null && jsonResourcePoolSummaryFlexi.getEngagementManager_Count() !=0){
								jsonResourcePoolSummaryFlexi.setEngagementManager_Count( jsonResourcePoolSummaryFlexi.getEngagementManager_Count()+ rpdto.getRole_Count());
							}else{
								jsonResourcePoolSummaryFlexi.setEngagementManager_Count( rpdto.getRole_Count());
							}					
						}else if(userRoleId == 3){
							if(jsonResourcePoolSummaryFlexi.getResourceManager_Count()  != null && jsonResourcePoolSummaryFlexi.getResourceManager_Count()  !=0){
								jsonResourcePoolSummaryFlexi.setTestManager_Count(jsonResourcePoolSummaryFlexi.getResourceManager_Count() + rpdto.getRole_Count());	
							}else{
								jsonResourcePoolSummaryFlexi.setTestManager_Count( rpdto.getRole_Count());
							}										
						}else if(userRoleId == 4){
							if(jsonResourcePoolSummaryFlexi.getTestLead_Count() != null && jsonResourcePoolSummaryFlexi.getTestLead_Count()  !=0){
								jsonResourcePoolSummaryFlexi.setTestLead_Count(jsonResourcePoolSummaryFlexi.getTestLead_Count() + rpdto.getRole_Count());
							}else{
								jsonResourcePoolSummaryFlexi.setTestLead_Count(rpdto.getRole_Count());
							}											
						}else if(userRoleId == 5){
							if(jsonResourcePoolSummaryFlexi.getTester_Count() != null && jsonResourcePoolSummaryFlexi.getTester_Count() !=0){
								jsonResourcePoolSummaryFlexi.setTester_Count(jsonResourcePoolSummaryFlexi.getTester_Count() + rpdto.getRole_Count());
							}else{
								jsonResourcePoolSummaryFlexi.setTester_Count(rpdto.getRole_Count());
							}											
						}else if(userRoleId == 6){
							if(jsonResourcePoolSummaryFlexi.getProgramManager_Count() != null && jsonResourcePoolSummaryFlexi.getProgramManager_Count() !=0){
								jsonResourcePoolSummaryFlexi.setProgramManager_Count(jsonResourcePoolSummaryFlexi.getProgramManager_Count() + rpdto.getRole_Count());
							}else{
								jsonResourcePoolSummaryFlexi.setProgramManager_Count(rpdto.getRole_Count());
							}					
						}else if(userRoleId == 7){
							if(jsonResourcePoolSummaryFlexi.getResourceManager_Count() != null && jsonResourcePoolSummaryFlexi.getResourceManager_Count() != 0){
								jsonResourcePoolSummaryFlexi.setResourceManager_Count(jsonResourcePoolSummaryFlexi.getResourceManager_Count() + rpdto.getRole_Count());	
							}else{
								jsonResourcePoolSummaryFlexi.setResourceManager_Count( rpdto.getRole_Count());
							}											
						}
					}
				}	

			jsonResourcePoolSummaryList.add(jsonResourcePoolSummaryCore);
			jsonResourcePoolSummaryList.add(jsonResourcePoolSummaryFlexi);
			jsonResourcePoolSummaryList.add(jsonResourcePoolSummaryContract);
		}
		}
		
				
		return jsonResourcePoolSummaryList;
	}
	@Override
	@Transactional
	public List<JsonWorkPackageResourceReservation> listWorkPackageDemandProjectionForResourcePlanning(int productId, Date resourceDemandForDate) {
		log.info("listWorkPackageDemandProjectionForResourcePlanning() in ResourceManagementServiceImpl");
		List<WorkPackageDemandProjectionDTO> workPackageDemandProjectionListForResourcePlanning = new ArrayList<WorkPackageDemandProjectionDTO>();
		List<ResourceCountDTO> blockedResourceCountDTOList = new ArrayList<ResourceCountDTO>();
		List<ResourceAvailabilityDTO> resourceAvailabilityDTOList = new ArrayList<ResourceAvailabilityDTO>();
		List<ResourceCountDTO> reservedResCountDTOListToGetActualAvailableResCount = new ArrayList<ResourceCountDTO>();
		List<WorkPackage> listOfWorkPackages = workPackageDAO.getWorkPackagesByProductId(productId);
		
		if(listOfWorkPackages != null && listOfWorkPackages.size()>0){
			for (WorkPackage workPackage : listOfWorkPackages) {
				List<WorkPackageDemandProjectionDTO> subListOfWorkPackageDemandProjectionForResourcePlanning = new ArrayList<WorkPackageDemandProjectionDTO>();
				subListOfWorkPackageDemandProjectionForResourcePlanning = testFactoryResourceReservationDao.listWorkPackageDemandProjectionForResourcePlanning(workPackage.getWorkPackageId(), resourceDemandForDate);
				if(workPackageDemandProjectionListForResourcePlanning.size() == 0){
					workPackageDemandProjectionListForResourcePlanning = subListOfWorkPackageDemandProjectionForResourcePlanning;
				}else{
					workPackageDemandProjectionListForResourcePlanning.addAll(subListOfWorkPackageDemandProjectionForResourcePlanning);
				}
				
				List<ResourceCountDTO> subListOfblockedResourceCountDTO = new ArrayList<ResourceCountDTO>();
				subListOfblockedResourceCountDTO = testFactoryResourceReservationDao.getBlockedResourcesOfWorkPackage(workPackage.getWorkPackageId(), resourceDemandForDate);
				if(blockedResourceCountDTOList.size() == 0){
					blockedResourceCountDTOList = subListOfblockedResourceCountDTO;
				}else{
					blockedResourceCountDTOList.addAll(subListOfblockedResourceCountDTO);
				}
			}
		}
		
		List<JsonWorkPackageResourceReservation> jsonWorkPackageResourceReservationList = new ArrayList<JsonWorkPackageResourceReservation>();
		if(workPackageDemandProjectionListForResourcePlanning != null && workPackageDemandProjectionListForResourcePlanning.size()>0){
			JsonWorkPackageResourceReservation jsonWorkPackageResourceReservation = null;
			int index = 1;
			for (WorkPackageDemandProjectionDTO workPackageDemandProjection : workPackageDemandProjectionListForResourcePlanning) {
				jsonWorkPackageResourceReservation = new JsonWorkPackageResourceReservation();
				jsonWorkPackageResourceReservation.setWorkPackageResourceReservationId(index++);
				jsonWorkPackageResourceReservation.setWorkPackageId(workPackageDemandProjection.getWorkPackageId());
				jsonWorkPackageResourceReservation.setProductId(workPackageDemandProjection.getProductId());
				jsonWorkPackageResourceReservation.setProductName(workPackageDemandProjection.getProductName());
				jsonWorkPackageResourceReservation.setWorkPackageName(workPackageDemandProjection.getWorkPackageName());
				jsonWorkPackageResourceReservation.setDateOfCount(DateUtility.sdfDateformatWithOutTime(workPackageDemandProjection.getWorkDate()));
				jsonWorkPackageResourceReservation.setShiftId(workPackageDemandProjection.getShiftId());
				jsonWorkPackageResourceReservation.setShiftName(workPackageDemandProjection.getShiftName());
				jsonWorkPackageResourceReservation.setShiftTypeId(workPackageDemandProjection.getShiftTypeId());
				jsonWorkPackageResourceReservation.setShiftTypeName(workPackageDemandProjection.getShiftTypeName());
				jsonWorkPackageResourceReservation.setResourceDemandCount(workPackageDemandProjection.getResourceCount());
				jsonWorkPackageResourceReservation.setAvailableCoreResourceCount(0f);
				jsonWorkPackageResourceReservation.setBlockedResourceCount(0f);
				jsonWorkPackageResourceReservation.setGapInResourceCount(workPackageDemandProjection.getResourceCount());
				jsonWorkPackageResourceReservationList.add(jsonWorkPackageResourceReservation);
			}
		}
		if(blockedResourceCountDTOList != null && blockedResourceCountDTOList.size()>0){
			for (JsonWorkPackageResourceReservation jsonWorkPackageResourceReservation : jsonWorkPackageResourceReservationList) {
				for (ResourceCountDTO resourceCountDTO : blockedResourceCountDTOList) {
					if(jsonWorkPackageResourceReservation.getWorkPackageId().equals(resourceCountDTO.getWorkPackageId())){
						if(jsonWorkPackageResourceReservation.getShiftId().equals(resourceCountDTO.getShiftId())){
							jsonWorkPackageResourceReservation.setBlockedResourceCount(resourceCountDTO.getBlockedResourcesCount());
							jsonWorkPackageResourceReservation.setGapInResourceCount(jsonWorkPackageResourceReservation.getResourceDemandCount()-jsonWorkPackageResourceReservation.getBlockedResourceCount());
						}
					}
				}
			}
		}
		
		resourceAvailabilityDTOList = resourceAvailabilityDAO.getAvailableBookedResourcesForDate(resourceDemandForDate);
		if(resourceAvailabilityDTOList != null && resourceAvailabilityDTOList.size()>0){
			for (JsonWorkPackageResourceReservation jsonWorkPackageResourceReservation : jsonWorkPackageResourceReservationList) {
				log.info("jsonWorkPackageResourceReservation wp: "+jsonWorkPackageResourceReservation.getWorkPackageId()+" shiftType: "+jsonWorkPackageResourceReservation.getShiftTypeId());
				for (ResourceAvailabilityDTO resourceAvailabilityDTO : resourceAvailabilityDTOList) {
					if(jsonWorkPackageResourceReservation.getShiftTypeId() == resourceAvailabilityDTO.getShiftTypeId()){
						log.info("ShiftTypeId are equal $$$$$$$$$$$");
						log.info("set Resource Availability Count "+resourceAvailabilityDTO.getResourceAvailabilityCount());
						jsonWorkPackageResourceReservation.setAvailableCoreResourceCount(resourceAvailabilityDTO.getResourceAvailabilityCount());
					}
				}
			}
		}
		
		reservedResCountDTOListToGetActualAvailableResCount = testFactoryResourceReservationDao.getBlockedResourcesForReservationDate(resourceDemandForDate);
		if(reservedResCountDTOListToGetActualAvailableResCount != null && reservedResCountDTOListToGetActualAvailableResCount.size()>0){
			for (JsonWorkPackageResourceReservation jsonWorkPackageResourceReservation : jsonWorkPackageResourceReservationList) {
				log.info("jsonWorkPackageResourceReservation. ShiftId: "+jsonWorkPackageResourceReservation.getShiftId());
				for (ResourceCountDTO resourceCountDTO : reservedResCountDTOListToGetActualAvailableResCount) {
						if(jsonWorkPackageResourceReservation.getShiftId() == resourceCountDTO.getShiftId()){
							Float exisitingResAvailabilityCount = jsonWorkPackageResourceReservation.getAvailableCoreResourceCount();
							log.info("exisitingResAvailabilityCount:  "+exisitingResAvailabilityCount);
							log.info("resourceCountDTO.getBlockedResourcesCount():  "+resourceCountDTO.getBlockedResourcesCount());
							jsonWorkPackageResourceReservation.setAvailableCoreResourceCount(exisitingResAvailabilityCount-resourceCountDTO.getBlockedResourcesCount());
							if(jsonWorkPackageResourceReservation.getAvailableCoreResourceCount() < 0){
								jsonWorkPackageResourceReservation.setAvailableCoreResourceCount(0f);
							}
							log.info("RrC>>"+jsonWorkPackageResourceReservation.getAvailableCoreResourceCount());
						}
				}
			}
		}
		return jsonWorkPackageResourceReservationList;
	}

		
	@Override
	@Transactional
	public List<JsonResourceAvailabilityPlan> listResourcesForAvailabilityPlan(Integer resourcePoolId, Date workDate) {
			
		log.info("listing listResourcesForAvailabilityPlan service method");
		log.info("Getting AvailabilityPlan for : " + resourcePoolId + " : " + workDate);
				
		List<UserList> userListCollection =  resourceAvailabilityDAO.listResourcesForAvailabilityPlan(resourcePoolId);
		
		List<JsonResourceAvailabilityPlan> jsonResourceAvailabilityPlanList = new ArrayList<JsonResourceAvailabilityPlan>();
		
		for(UserList userList : userListCollection) {
			
			JsonResourceAvailabilityPlan jsonResourceAvailabilityPlan = new JsonResourceAvailabilityPlan();
			
			jsonResourceAvailabilityPlan.setResourcePoolId(userList.getResourcePool().getResourcePoolId());
			jsonResourceAvailabilityPlan.setResourcePoolName(userList.getResourcePool().getResourcePoolName());
			jsonResourceAvailabilityPlan.setUserId(userList.getUserId());
			jsonResourceAvailabilityPlan.setUserName(userList.getUserDisplayName());
			jsonResourceAvailabilityPlan.setWorkDate(DateUtility.sdfDateformatWithOutTime(workDate));
			jsonResourceAvailabilityPlan.setRoleName(userList.getUserRoleMaster().getRoleLabel());
			jsonResourceAvailabilityPlan.setUserTypeName(userList.getUserTypeMasterNew().getUserTypeLabel());
			jsonResourceAvailabilityPlan.setVendorId(userList.getVendor().getVendorId());
			jsonResourceAvailabilityPlan.setRegisteredCompanyName(userList.getVendor().getRegisteredCompanyName());
			
			
			//TODO : Optimize database calls
			List<ResourceAvailability> resourceAvailabilityList =  resourceAvailabilityDAO.listResourceAvailability(userList.getUserId(), workDate, workDate);
			
			Integer availabilityStatus = null;
			Integer bookingStatus = null;
			for(ResourceAvailability resourceAvailability : resourceAvailabilityList) {
				log.info("Resource availability : " + resourceAvailability.getIsAvailable());
				
				if (resourceAvailability.getIsAvailable() == null || resourceAvailability.getIsAvailable().intValue()==0) {
					availabilityStatus = 0;
				} else {
					availabilityStatus = 1;
				}
				if (resourceAvailability.getBookForShift() == null || resourceAvailability.getBookForShift().intValue()==0) {
					bookingStatus = 0;
				} else {
					bookingStatus = 1;
				}
				if (resourceAvailability.getWorkShiftMaster().getShiftId() == 1) {
					jsonResourceAvailabilityPlan.setDayShiftAvailibility(availabilityStatus);
					jsonResourceAvailabilityPlan.setDayShiftBooking(bookingStatus);;
				} else if (resourceAvailability.getWorkShiftMaster().getShiftId() == 2) {
					jsonResourceAvailabilityPlan.setNightShiftAvailibility(availabilityStatus);
					jsonResourceAvailabilityPlan.setNightShiftBooking(bookingStatus);;
				}
				if (resourceAvailability.getWorkShiftMaster().getShiftId() == 3) {
					jsonResourceAvailabilityPlan.setGraveyardShiftAvailibility(availabilityStatus);
					jsonResourceAvailabilityPlan.setGraveyardShiftBooking(bookingStatus);;
				}
			
								
			}	
			jsonResourceAvailabilityPlanList.add(jsonResourceAvailabilityPlan);
		}
					
		return jsonResourceAvailabilityPlanList;
	}

	
	@Override
	@Transactional
	public List<JsonUserList> getBlockedResourcesOfWorkPackage(
			int workPackageId, int shiftId, Date resourceDemandForDate) {
		List<JsonUserList> jsonBlockedUserList = null;
		List<UserList> productCoreResources = null;
		Integer productId = 0;
		List<TestFactoryResourceReservation> listOfResourceReserved = testFactoryResourceReservationDao.getReservedResourcesOfWorkpackage(workPackageId, shiftId,resourceDemandForDate);
		WorkShiftMaster workShift = workShiftMasterDAO.listWorkShiftsByshiftId(shiftId);
		ShiftTypeMaster shiftTypeOfWorkShift = resourceAvailabilityDAO.getShiftTypeIdFromWorkShiftId(shiftId);
		List<UserWeekUtilisedTimeDTO> listOfTimeSheetEntries = timeSheetManagementDAO.getTimesheetEntriesForWorkPackage(workPackageId, workShift, resourceDemandForDate);
		List<UserWeekUtilisedTimeDTO> listOfReservedShifts = timeSheetManagementDAO.getReservedShiftsOfUser(workPackageId, shiftId, resourceDemandForDate);
		if(listOfResourceReserved != null && listOfResourceReserved.size()>0){
			jsonBlockedUserList = new ArrayList<JsonUserList>();
			for (TestFactoryResourceReservation reservation : listOfResourceReserved) {
				JsonUserList jsonUser = new JsonUserList();
				jsonUser.setLoginId(reservation.getBlockedUser().getLoginId());
				jsonUser.setUserId(reservation.getBlockedUser().getUserId());
				jsonUser.setUserRoleId(reservation.getBlockedUser().getUserRoleMaster().getUserRoleId());
				jsonUser.setUserRoleLabel(reservation.getBlockedUser().getUserRoleMaster().getRoleLabel());
				jsonUser.setImageURI(reservation.getBlockedUser().getImageURI());
				Set<UserSkills> setOfUserSkills = reservation.getBlockedUser().getUserSkills();
				StringBuffer sb = jsonUser.getSkillsOfUser(setOfUserSkills);
				if(sb != null){
					jsonUser.setSkillName(sb);
				}
				jsonUser.setReserve(new Integer(1));
				jsonUser.setReservationDetails(reservation.getReservationActionUser().getLoginId()+" / "+ reservation.getReservationActionDate()+" / "+reservation.getWorkPackage().getName());
				jsonBlockedUserList.add(jsonUser);
			}
			log.info("Available jsonUserList size: "+jsonBlockedUserList.size());
		}
		
		if(jsonBlockedUserList != null && jsonBlockedUserList.size()>0){
			//compare and set DTO's time sheet hours of user
			for (JsonUserList jsonUser : jsonBlockedUserList) {
				for (UserWeekUtilisedTimeDTO userWeekUtilisedTimeDTO : listOfTimeSheetEntries) {
					if(userWeekUtilisedTimeDTO.getUserId() == jsonUser.getUserId()){
						Integer totalTimeInMinutes = DateUtility.convertTimeInMinutes(userWeekUtilisedTimeDTO.getTimeSheetTotalHours(),userWeekUtilisedTimeDTO.getTimeSheetTotalMins());
						Integer remainingTimeSheetInMinutes = 2400-totalTimeInMinutes;
						userWeekUtilisedTimeDTO.setTimeSheetDuration(DateUtility.convertTimeInHoursMinutes(0,remainingTimeSheetInMinutes));
						jsonUser.setTimeSheetHours(userWeekUtilisedTimeDTO.getTimeSheetDuration());
						break;
					}else{
						continue;
					}
				}
			}
		}
		
		if(jsonBlockedUserList != null && jsonBlockedUserList.size()>0){
			//compare and set DTO's time sheet hours of user
			for (JsonUserList jsonUser : jsonBlockedUserList) {
				for (UserWeekUtilisedTimeDTO userWeekUtilisedTimeDTO : listOfReservedShifts) {
					if(userWeekUtilisedTimeDTO.getUserId() == jsonUser.getUserId()){
						long totalTime = 40-(userWeekUtilisedTimeDTO.getShiftBookingCount()*8);
						userWeekUtilisedTimeDTO.setShiftBookingDuration(String.valueOf(totalTime));
						jsonUser.setBookedHrs(totalTime);
						break;
					}else{
						continue;
					}
				}
			}
		}
		
		if(jsonBlockedUserList != null && jsonBlockedUserList.size()>0){
			for (JsonUserList jsonUser : jsonBlockedUserList) {
				 ResourceAvailability resAvailability = resourceAvailabilityDAO.getResourceAvailability(jsonUser.getUserId(), resourceDemandForDate, shiftTypeOfWorkShift.getShiftTypeId());
				 if(resAvailability != null){
					 if(resAvailability.getBookForShift() == 1){
						 jsonUser.setBooked("B");
					 }else{
						 jsonUser.setBooked("NB");
					 }
					 
					 if(resAvailability.getIsAvailable() == 1){
						 jsonUser.setAvailable("A");
					 }else{
						 jsonUser.setAvailable("NA");
					 }
				 }
			}
		}
		
		if(workPackageId != 0){
			WorkPackage wPackage = workPackageDAO.getWorkPackageByIdWithMinimalnitialization(workPackageId);
			if(wPackage != null){
				if(wPackage.getProductBuild().getProductVersion().getProductMaster() != null){
					productId = wPackage.getProductBuild().getProductVersion().getProductMaster().getProductId();
				}
			}
		}
		if(productId != 0){
			log.info("productId: "+productId + "resourceDemandForDate: "+resourceDemandForDate);
			productCoreResources = testFactoryResourceReservationDao.getOtherProductCoreResources(productId,resourceDemandForDate,false);
		}
		
		if(productCoreResources != null && productCoreResources.size() > 0){
			if(jsonBlockedUserList != null && jsonBlockedUserList.size()>0){
				for (JsonUserList jsonUser : jsonBlockedUserList) {
					for (UserList productCoreUser : productCoreResources) {
						if(productCoreUser.getUserId() == jsonUser.getUserId()){
							jsonUser.setProductCoreResource("Core");
							UserRoleMaster productCoreUserRole = testFactoryResourceReservationDao.getProductCoreResourcesProductRole(productCoreUser.getUserId(),productId,resourceDemandForDate);
							if(productCoreUserRole != null){
								jsonUser.setUserRoleId(productCoreUserRole.getUserRoleId());
								jsonUser.setUserRoleLabel(productCoreUserRole.getRoleLabel());
							}
							break;
						}else{
							continue;
						}
					}
				}
			}
		}
		
		return jsonBlockedUserList;
	}

	
	@Override
	@Transactional
	public  ResourceAvailability updateResourceAvailabilityInline(UserList userList,ShiftTypeMaster shiftTypeMaster,Integer shiftTypeAvailability,String workDate) {
		String date[]=workDate.split(",");
		workDate=date[0];
		ResourceAvailability resourceAvailability = resourceAvailabilityDAO.getResourceAvailability(userList.getUserId(),DateUtility.dateformatWithOutTime(workDate), shiftTypeMaster.getShiftTypeId());
		log.info("resourceAvailability --->"+resourceAvailability);
		if (resourceAvailability == null) {
			if(shiftTypeAvailability==null || shiftTypeAvailability ==0){
				log.info("no action");
			}else{
				
				ResourceAvailability newResourceAvailability = new ResourceAvailability();
				newResourceAvailability.setWorkDate(DateUtility.dateformatWithOutTime(workDate));
				newResourceAvailability.setShiftTypeMaster(shiftTypeMaster);
				newResourceAvailability.setIsAvailable(1);
				newResourceAvailability.setResource(userList);
				resourceAvailabilityDAO.add(newResourceAvailability);
				log.info("Add action");
				resourceAvailability = newResourceAvailability;
			}
		}else{
			if(shiftTypeAvailability==null || shiftTypeAvailability ==0){
				log.info("delete action");
				resourceAvailabilityDAO.delete(resourceAvailability);
			}else {
				log.info("Update avaialability action");
				resourceAvailability.setIsAvailable(1);
				resourceAvailabilityDAO.updateResourceAvailability(resourceAvailability);
			}
			log.info("Update avaialability end action");
		}
		
		return resourceAvailability;
	}
	
	@Override
	@Transactional
	public  ResourceAvailability updateResourceBookingInline(UserList userList,ShiftTypeMaster shiftTypeMaster,Integer shiftTypeBooking,String workDate) {
		String date[]=workDate.split(",");
		workDate=date[0];
		ResourceAvailability resourceAvailability = resourceAvailabilityDAO.getResourceAvailability(userList.getUserId(),DateUtility.dateformatWithOutTime(workDate), shiftTypeMaster.getShiftTypeId());
		log.info("resourceBooking --->"+resourceAvailability);
		if (resourceAvailability != null) {
			if(shiftTypeBooking == null || shiftTypeBooking ==0){
				resourceAvailability.setBookForShift(0);
				resourceAvailabilityDAO.updateResourceAvailability(resourceAvailability);
			}else {
				resourceAvailability.setBookForShift(1);
				resourceAvailabilityDAO.updateResourceAvailability(resourceAvailability);
			}
			log.info("Update Booking end action");
		}
		
		return resourceAvailability;
	}
	
	@Override
	@Transactional
	public List<ResourceAvailability> listResourceAvailablityByDate(
			Date workDate) {
		return resourceAvailabilityDAO.listResourceAvailablityByDate(workDate);
	}

	@Override
	@Transactional
	public TestFactoryResourceReservationDTO listTestFactoryResourceReservationByWorkpackageIdDate(
			Date startDate, Date endDate, Integer testFactoryLabId, Integer testFactoryId, Integer productId, Integer workPackageId, Integer shiftId,Date workDate) {
		return resourceAvailabilityDAO.listTestFactoryResourceReservationByWorkpackageIdDate(startDate, endDate, testFactoryLabId, testFactoryId, productId, workPackageId, shiftId, workDate);
	}

	@Override
	@Transactional
	public ResourceAvailabilityDTO listResourceAvailablityByDate(
			Date startDate, Date endDate, Integer testFactoryLabId,
			Integer testFactoryId, Integer productId, Integer workPackageId,
			Integer shiftId,Date workDate) {
		return resourceAvailabilityDAO.listResourceAvailablityByDate(startDate, endDate, testFactoryLabId, testFactoryId, productId, workPackageId, shiftId,workDate);
	}
	
	@Override
	@Transactional
	public void saveBlockedResource(List<UserList> listOfResourcesToAdd, Date resourceBlockedForDate, String workPackageId, String shiftId, String loggedInUserId) {
		try {
			log.info("Reserve resources for work Package: "+workPackageId+" for shift: "+shiftId+"   for date: "+resourceBlockedForDate);
			TestFactoryResourceReservation testFactoryResourceReservation = null;
			WorkPackage workPackage = workPackageDAO.getWorkPackageByIdWithMinimalnitialization(Integer.parseInt(workPackageId));
			WorkShiftMaster workShiftMaster = workPackageDAO.getWorkShiftById(Integer.parseInt(shiftId));
			UserList loggedInUser = userListDAO.getByUserId(Integer.parseInt(loggedInUserId));
			log.info("listOfResourcesToAdd size: "+listOfResourcesToAdd.size());
			for (UserList userList : listOfResourcesToAdd) {
				
				boolean isResourceAlreadyBlockedByAnotherManagerForSameWp = testFactoryResourceReservationDao.isResourceBlockedAlready(Integer.parseInt(workPackageId), Integer.parseInt(shiftId), resourceBlockedForDate, userList.getUserId(),1);
				boolean isResourceAlreadyBlockedByAnotherManagerForDiffWp = testFactoryResourceReservationDao.isResourceBlockedAlready(Integer.parseInt(workPackageId), Integer.parseInt(shiftId), resourceBlockedForDate, userList.getUserId(),0);
				if(isResourceAlreadyBlockedByAnotherManagerForSameWp || isResourceAlreadyBlockedByAnotherManagerForDiffWp){
					log.info("Resource "+ userList.getLoginId() +" already blocked by another manager for date: "+resourceBlockedForDate+"  shift: "+shiftId);
				}else{
					testFactoryResourceReservation = new TestFactoryResourceReservation();
					testFactoryResourceReservation.setBlockedUser(userList);
					testFactoryResourceReservation.setWorkPackage(workPackage);
					testFactoryResourceReservation.setShift(workShiftMaster);
					testFactoryResourceReservation.setReservationDate(resourceBlockedForDate);
					testFactoryResourceReservation.setReservationActionUser(loggedInUser);
					testFactoryResourceReservation.setReservationActionDate(new Date(System.currentTimeMillis()));
					testFactoryResourceReservationDao.saveBlockedResource(testFactoryResourceReservation);
				}
			}
		} catch (Exception e) {
			log.error("error occured while saving resource reservation",e);
		}
	}
	

	@Override
	@Transactional
	public List<TestFactoryResourceReservation> getTestFactoryResourceReservation(
			int workPackageId, int shiftId,Date tfreservationDate) {
		return testFactoryResourceReservationDao.getTestFactoryResourceReservation(workPackageId,shiftId,tfreservationDate);
	}


	@Override
	@Transactional
	public void removeUnblockedResources(List<UserList> listOfResourcesToDelete,
			Date dtResourceBlockedForDate, String workPackageId,String shiftId, String loggedInUserId) {
		try {
			TestFactoryResourceReservation testFactoryResourceReservation = null;
			log.info("listOfResourcesToDelete size: "+listOfResourcesToDelete.size());
			for (UserList userList : listOfResourcesToDelete) {
				log.info("User to unblock: "+userList.getLoginId());
				testFactoryResourceReservation = testFactoryResourceReservationDao.getTestFactoryResourceReservation(Integer.parseInt(workPackageId), Integer.parseInt(shiftId), dtResourceBlockedForDate, userList.getUserId(), Integer.parseInt(loggedInUserId),0);
				if(testFactoryResourceReservation != null){
					testFactoryResourceReservationDao.removeUnblockedResources(testFactoryResourceReservation);
				}else{
					log.info("Entry does not exist for user : "+userList.getLoginId()+" for shift : "+shiftId+" for workPackage Id: "+workPackageId+"  for Date: "+dtResourceBlockedForDate);
				}
			}
		} catch (Exception e) {
			log.error("error occured while saving resource reservation",e);
		}
	}

	@Override
	@Transactional
	public List<JsonWorkPackageResourceReservation> listWorkPackageDemandProjectionForResourcePlanningByWorkpackage(
			int workpackageId, Date resourceDemandForDate) {
		log.info("listWorkPackageDemandProjectionForResourcePlanning() in ResourceManagementServiceImpl");
		List<WorkPackageDemandProjectionDTO> workPackageDemandProjectionListForResourcePlanning = new ArrayList<WorkPackageDemandProjectionDTO>();
		List<ResourceCountDTO> blockedResourceCountDTOList = new ArrayList<ResourceCountDTO>();
		List<ResourceCountDTO> reservedResCountDTOListToGetActualAvailableResCount = new ArrayList<ResourceCountDTO>();
		List<JsonWorkPackageResourceReservation> jsonWorkPackageResourceReservationList = new ArrayList<JsonWorkPackageResourceReservation>();
		List<ResourceAvailabilityDTO> resourceAvailabilityDTOList = null;

				List<WorkPackageDemandProjectionDTO> subListOfWorkPackageDemandProjectionForResourcePlanning = new ArrayList<WorkPackageDemandProjectionDTO>();
				subListOfWorkPackageDemandProjectionForResourcePlanning = testFactoryResourceReservationDao.listWorkPackageDemandProjectionForResourcePlanning(workpackageId, resourceDemandForDate);
				if(workPackageDemandProjectionListForResourcePlanning.size() == 0){
					workPackageDemandProjectionListForResourcePlanning = subListOfWorkPackageDemandProjectionForResourcePlanning;
				}else{
					workPackageDemandProjectionListForResourcePlanning.addAll(subListOfWorkPackageDemandProjectionForResourcePlanning);
				}
				
				List<ResourceCountDTO> subListOfblockedResourceCountDTO = new ArrayList<ResourceCountDTO>();
				subListOfblockedResourceCountDTO = testFactoryResourceReservationDao.getBlockedResourcesOfWorkPackage(workpackageId, resourceDemandForDate);
				if(blockedResourceCountDTOList.size() == 0){
					blockedResourceCountDTOList = subListOfblockedResourceCountDTO;
				}else{
					blockedResourceCountDTOList.addAll(subListOfblockedResourceCountDTO);
				}
				


		
		if(workPackageDemandProjectionListForResourcePlanning != null && workPackageDemandProjectionListForResourcePlanning.size()>0){
			JsonWorkPackageResourceReservation jsonWorkPackageResourceReservation = null;
			int index = 1;
			for (WorkPackageDemandProjectionDTO workPackageDemandProjection : workPackageDemandProjectionListForResourcePlanning) {
				jsonWorkPackageResourceReservation = new JsonWorkPackageResourceReservation();
				jsonWorkPackageResourceReservation.setWorkPackageResourceReservationId(index++);
				jsonWorkPackageResourceReservation.setWorkPackageId(workPackageDemandProjection.getWorkPackageId());
				jsonWorkPackageResourceReservation.setProductId(workPackageDemandProjection.getProductId());
				jsonWorkPackageResourceReservation.setProductName(workPackageDemandProjection.getProductName());
				jsonWorkPackageResourceReservation.setWorkPackageName(workPackageDemandProjection.getWorkPackageName());
				jsonWorkPackageResourceReservation.setShiftId(workPackageDemandProjection.getShiftId());
				jsonWorkPackageResourceReservation.setShiftName(workPackageDemandProjection.getShiftName());
				jsonWorkPackageResourceReservation.setShiftTypeId(workPackageDemandProjection.getShiftTypeId());
				jsonWorkPackageResourceReservation.setShiftTypeName(workPackageDemandProjection.getShiftTypeName());
				jsonWorkPackageResourceReservation.setDateOfCount(DateUtility.sdfDateformatWithOutTime(workPackageDemandProjection.getWorkDate()));
				jsonWorkPackageResourceReservation.setResourceDemandCount(workPackageDemandProjection.getResourceCount());
				jsonWorkPackageResourceReservation.setAvailableCoreResourceCount(0f);
				jsonWorkPackageResourceReservation.setBlockedResourceCount(0f);
				jsonWorkPackageResourceReservation.setGapInResourceCount(workPackageDemandProjection.getResourceCount());
				jsonWorkPackageResourceReservationList.add(jsonWorkPackageResourceReservation);
			}
		}
		if(blockedResourceCountDTOList != null && blockedResourceCountDTOList.size()>0){
			for (JsonWorkPackageResourceReservation jsonWorkPackageResourceReservation : jsonWorkPackageResourceReservationList) {
				for (ResourceCountDTO resourceCountDTO : blockedResourceCountDTOList) {
					if(jsonWorkPackageResourceReservation.getWorkPackageId().equals(resourceCountDTO.getWorkPackageId())){
						if(jsonWorkPackageResourceReservation.getShiftId().equals(resourceCountDTO.getShiftId())){
							jsonWorkPackageResourceReservation.setBlockedResourceCount(resourceCountDTO.getBlockedResourcesCount());
							jsonWorkPackageResourceReservation.setAvailableCoreResourceCount(0f);
							jsonWorkPackageResourceReservation.setGapInResourceCount(jsonWorkPackageResourceReservation.getResourceDemandCount()-jsonWorkPackageResourceReservation.getBlockedResourceCount());
						}
					}
				}
			}
		}
		
		resourceAvailabilityDTOList = resourceAvailabilityDAO.getAvailableBookedResourcesForDate(resourceDemandForDate);
		if(resourceAvailabilityDTOList != null && resourceAvailabilityDTOList.size()>0){
			for (JsonWorkPackageResourceReservation jsonWorkPackageResourceReservation : jsonWorkPackageResourceReservationList) {
				log.info("jsonWorkPackageResourceReservation wp: "+jsonWorkPackageResourceReservation.getWorkPackageId()+" shiftType: "+jsonWorkPackageResourceReservation.getShiftTypeId());
				for (ResourceAvailabilityDTO resourceAvailabilityDTO : resourceAvailabilityDTOList) {
					if(jsonWorkPackageResourceReservation.getShiftTypeId() == resourceAvailabilityDTO.getShiftTypeId()){
						log.info("ShiftTypeId are equal $$$$$$$$$$$");
						log.info("set Resource Availability Count "+resourceAvailabilityDTO.getResourceAvailabilityCount());
						jsonWorkPackageResourceReservation.setAvailableCoreResourceCount(resourceAvailabilityDTO.getResourceAvailabilityCount());
					}
				}
			}
		}
		
		reservedResCountDTOListToGetActualAvailableResCount = testFactoryResourceReservationDao.getBlockedResourcesForReservationDate(resourceDemandForDate);
		if(reservedResCountDTOListToGetActualAvailableResCount != null && reservedResCountDTOListToGetActualAvailableResCount.size()>0){
			for (JsonWorkPackageResourceReservation jsonWorkPackageResourceReservation : jsonWorkPackageResourceReservationList) {
				log.info("jsonWorkPackageResourceReservation Date: &&&&& "+jsonWorkPackageResourceReservation.getDateOfCount()+" shiftType: "+jsonWorkPackageResourceReservation.getShiftTypeId());
				for (ResourceCountDTO resourceCountDTO : reservedResCountDTOListToGetActualAvailableResCount) {
						if(jsonWorkPackageResourceReservation.getShiftId() == resourceCountDTO.getShiftId()){
							Float exisitingResAvailabilityCount = jsonWorkPackageResourceReservation.getAvailableCoreResourceCount();
							log.info("exisitingResAvailabilityCount:  "+exisitingResAvailabilityCount);
							log.info("resourceCountDTO.getBlockedResourcesCount():  "+resourceCountDTO.getBlockedResourcesCount());
							jsonWorkPackageResourceReservation.setAvailableCoreResourceCount(exisitingResAvailabilityCount-resourceCountDTO.getBlockedResourcesCount());
							if(jsonWorkPackageResourceReservation.getAvailableCoreResourceCount() < 0){
								jsonWorkPackageResourceReservation.setAvailableCoreResourceCount(0f);
							}
							log.info("RrC>>"+jsonWorkPackageResourceReservation.getAvailableCoreResourceCount());
						}
				}
			}
		}
		
		return jsonWorkPackageResourceReservationList;
	
	}

	@Override
	@Transactional
	public List<JsonResourceAttendance> listResourcesAttendance(int resourcePoolId, Date workDate, Integer shiftId) {
		log.info("listResourcesAttendance ()   >>>>  "+"resourcePoolId::: "+resourcePoolId+"    workDate: "+workDate+"      shiftId: "+shiftId );
		List<ResourceAvailability> resourceAvailabilityList = resourceAvailabilityDAO.listResourceAttendance(resourcePoolId, workDate, shiftId);
		List<JsonResourceAttendance> jsonResourceAttendanceList =  new ArrayList<JsonResourceAttendance>();
		JsonResourceAttendance jsonResourceAttendance = null;
		for (ResourceAvailability resourceAvailability : resourceAvailabilityList) {
		
			StringBuffer skillarray = new StringBuffer();
		
			Set<UserSkills> userSkillsSet = resourceAvailability.getResource().getUserSkills();
			int blocked_UserId = resourceAvailability.getResource().getUserId();
				if(userSkillsSet != null && userSkillsSet.size()>0){
				skillarray= getSkillsOfUsers(userSkillsSet, blocked_UserId);			
			}		
			
			jsonResourceAttendance = new JsonResourceAttendance(resourceAvailability);
			if(skillarray.length() != 0){
				
				jsonResourceAttendance.setSkillName(skillarray);
				jsonResourceAttendance.setSkillNameCharacterlength(skillarray.length());
				
			}else{
				jsonResourceAttendance.setSkillName(new StringBuffer(""));
				jsonResourceAttendance.setSkillNameCharacterlength(0);
				
			}
			jsonResourceAttendanceList.add(jsonResourceAttendance);
		}
		
		return jsonResourceAttendanceList;
	}
	
	@Override
	@Transactional
	public List<JsonResourceAttendance> listResourcesAttendance(int resourcePoolId, Date workDateFrom,  Date workDateTo, Integer shiftId, Integer userId) {
		log.info("listResourcesAttendance ()   >>>>  "+"resourcePoolId::: "+resourcePoolId+"    workDate: "+workDateFrom+"      shiftId: "+shiftId );
		WorkShiftMaster workShiftMaster = workPackageDAO.getWorkShiftById(shiftId);
		Integer shiftTypeId = null;
		if(shiftId != -1){
			shiftTypeId = workShiftMaster.getShiftType().getShiftTypeId();
		}else{
			shiftTypeId = shiftId;
		}
		List<ResourceAvailability> resourceAvailabilityList = resourceAvailabilityDAO.listResourceAttendance(resourcePoolId, workDateFrom, workDateTo,shiftTypeId, userId);
		List<JsonResourceAttendance> jsonResourceAttendanceList =  new ArrayList<JsonResourceAttendance>();
		JsonResourceAttendance jsonResourceAttendance = null;
		for (ResourceAvailability resourceAvailability : resourceAvailabilityList) {
			jsonResourceAttendance = new JsonResourceAttendance(resourceAvailability);
			jsonResourceAttendanceList.add(jsonResourceAttendance);
		}
		return jsonResourceAttendanceList;
	}

	@Override
	@Transactional
	public ResourceAvailability updateResourceAttendanceInline(Integer resourceAvailabilityId, String modifiedField, String modifiedFieldValue,HttpServletRequest req) {
		// TODO Auto-generated method stub
		ResourceAvailability resourceAvailability = resourceAvailabilityDAO.getResourceAvailabilityById(resourceAvailabilityId);
		
		
		
		Locale locale=(Locale)req.getSession().getAttribute("locale");
		String localeId=(String)req.getSession().getAttribute("localeId");
		
		
		
		if (modifiedField.equalsIgnoreCase("shiftAttendance")) {
			
			if (modifiedFieldValue == null || modifiedFieldValue.equals("0")) {
				resourceAvailability.setShiftAttendance(0);
				resourceAvailability.setAttendanceCheckInTime(null);
			} else {
				resourceAvailability.setShiftAttendance(1);
				resourceAvailability.setAttendanceCheckInTime(new Date(System.currentTimeMillis()));
			}
		}else if (modifiedField.equalsIgnoreCase("shiftBillingModeIsFull")) {
			if (modifiedFieldValue == null || modifiedFieldValue.equals("0")) {
				resourceAvailability.setShiftBillingModeIsFull(null);
			} else {
				resourceAvailability.setShiftBillingModeIsFull(1);
			}
			
		}
		else if (modifiedField.equalsIgnoreCase("attendanceCheckInTime")) {
			resourceAvailability.setAttendanceCheckInTime(DateUtility.toDateInSec(modifiedFieldValue));
			resourceAvailability.setShiftAttendance(1);
			
		}else if (modifiedField.equalsIgnoreCase("attendanceCheckOutTime")) {
		
			if(resourceAvailability.getAttendanceCheckInTime() != null)
			{
				resourceAvailability.setAttendanceCheckOutTime(DateUtility.toDateInSec(modifiedFieldValue));
			}
			
		}
		
		resourceAvailabilityDAO.updateResourceAvailability(resourceAvailability);
		return resourceAvailability;
	}
	
	@Override
	@Transactional
	public List<TestfactoryResourcePool> getResourcePoolListById(
			int testFactoryLabId, int resourcePoolId) {
		return testfactoryResourcePoolDAO.listResourcePoollistbyId(testFactoryLabId, resourcePoolId);
	}

	@Override
	@Transactional
	public TestfactoryResourcePool mapRespoolTestfactory(Integer testFactoryId,
			Integer resourcePoolId, String action) {
		return testFactoryDao.mapRespoolTestfactory(testFactoryId,resourcePoolId,action);
	}

	public StringBuffer getSkillsOfUsers(Set<UserSkills> userSkillsSet, int blocked_UserId){
		StringBuffer skillarray = new StringBuffer();
		if(userSkillsSet != null && userSkillsSet.size()>0){
			for (UserSkills userSkillsets : userSkillsSet) {
				int user_id = userSkillsets.getUser().getUserId();
				if(user_id == blocked_UserId){
					int isPrimary = userSkillsets.getSelfIsPrimary();
					if(isPrimary == 1){
						if(skillarray.length() == 0){
							skillarray.append(userSkillsets.getSkill().getDisplayName());
						}else if(skillarray.length() >0){
							skillarray.append(",");
							skillarray.append(userSkillsets.getSkill().getDisplayName());
						}
					}					
				}				
			}
		}
		return skillarray;
	}
	static int flagger=0;
	@Override
	public List<JsonResourceAvailabilityPlan> listResourcesForAvailabilityPlanByShifId(Map<String, String> searchStrings, String searchResourcePoolName,String searchUserName,
			int resourcePoolId, Date workDate, int shiftTypeId,Integer jtStartIndex, Integer jtPageSize) {
		
		log.info("Getting AvailabilityPlan for : " + resourcePoolId + " : " + workDate);
		int count=0;	
		List<JsonResourceAvailabilityPlan> jsonResourceAvailabilityPlanList = new ArrayList<JsonResourceAvailabilityPlan>();
		List<JsonResourceAvailabilityPlan> jsonResourceAvailabilityCompleteList = new ArrayList<JsonResourceAvailabilityPlan>();
		List<JsonResourceAvailabilityPlan> jsonResourceMarkedAvailableList = new ArrayList<JsonResourceAvailabilityPlan>();
		List<JsonResourceAvailabilityPlan> jsonResourceNotMarkedAvailableList = new ArrayList<JsonResourceAvailabilityPlan>();
		try {
			List<UserList> userListCollection =  resourceAvailabilityDAO.listResourcesForAvailabilityPlan(searchStrings, searchResourcePoolName,searchUserName,resourcePoolId, jtStartIndex, jtPageSize);
			
			for(UserList userList : userListCollection) {
				JsonResourceAvailabilityPlan jsonResourceAvailabilityPlan = new JsonResourceAvailabilityPlan();
				jsonResourceAvailabilityPlan.setResourcePoolId(userList.getResourcePool().getResourcePoolId());
				jsonResourceAvailabilityPlan.setResourcePoolName(userList.getResourcePool().getResourcePoolName());
				jsonResourceAvailabilityPlan.setUserId(userList.getUserId());
				jsonResourceAvailabilityPlan.setUserName(userList.getUserDisplayName());
				jsonResourceAvailabilityPlan.setWorkDate(DateUtility.sdfDateformatWithOutTime(workDate));
				jsonResourceAvailabilityPlan.setRoleId(userList.getUserRoleMaster().getUserRoleId());
				jsonResourceAvailabilityPlan.setRoleName(userList.getUserRoleMaster().getRoleLabel());
				jsonResourceAvailabilityPlan.setUserTypeName(userList.getUserTypeMasterNew().getUserTypeLabel());
				jsonResourceAvailabilityPlan.setVendorId(userList.getVendor().getVendorId());
				jsonResourceAvailabilityPlan.setRegisteredCompanyName(userList.getVendor().getRegisteredCompanyName());
				
				
				StringBuffer skillarray = new StringBuffer();
				Set<UserSkills> userSkillsSet = userList.getUserSkills();
				int blocked_UserId = userList.getUserId();
					if(userSkillsSet != null && userSkillsSet.size()>0){
					skillarray= getSkillsOfUsers(userSkillsSet, blocked_UserId);			
				}
				
				if(skillarray.length() != 0){
					jsonResourceAvailabilityPlan.setSkillName(skillarray);
					jsonResourceAvailabilityPlan.setSkillNameCharacterlength(skillarray.length());
					
				}else{
					jsonResourceAvailabilityPlan.setSkillName(new StringBuffer(""));
					jsonResourceAvailabilityPlan.setSkillNameCharacterlength(0);
					
				}
				
				
				List<ResourceAvailability> resourceAvailabilityList =  resourceAvailabilityDAO.listResourceAttendanceByShiftId(userList.getUserId(), workDate,workDate, shiftTypeId, 0);
				Integer availabilityStatus = null;
				Integer bookingStatus = null;
				if(resourceAvailabilityList!=null && !resourceAvailabilityList.isEmpty()){
					for(ResourceAvailability resourceAvailability : resourceAvailabilityList) {
						if (resourceAvailability.getIsAvailable() == null || resourceAvailability.getIsAvailable().intValue()==0) {
							availabilityStatus = 0;
						} else {
							availabilityStatus = 1;
						}
						if (resourceAvailability.getBookForShift() == null || resourceAvailability.getBookForShift().intValue()==0) {
							bookingStatus = 0;
						} else {
							bookingStatus = 1;
						}
						if(resourceAvailability.getShiftTypeMaster().getShiftTypeId()!=null){
							jsonResourceAvailabilityPlan.setShiftTypeAvailability(availabilityStatus);
							jsonResourceAvailabilityPlan.setShiftTypeBooking(bookingStatus);
							jsonResourceAvailabilityPlan.setShiftTypeId(shiftTypeId);
								
						}else{
							availabilityStatus = 0;
							bookingStatus = 0;
							jsonResourceAvailabilityPlan.setShiftTypeAvailability(availabilityStatus);
							jsonResourceAvailabilityPlan.setShiftTypeBooking(bookingStatus);
							jsonResourceAvailabilityPlan.setShiftTypeId(shiftTypeId);
						}
						
					}
				}else{
					availabilityStatus = 0;
					bookingStatus = 0;
					jsonResourceAvailabilityPlan.setShiftTypeAvailability(availabilityStatus);
					jsonResourceAvailabilityPlan.setShiftTypeBooking(bookingStatus);
					jsonResourceAvailabilityPlan.setShiftTypeId(shiftTypeId);
				}
				
				List<UserWeekUtilisedTimeDTO> listOfTimeSheetEntriesforUser = timeSheetManagementDAO.getTimesheetEntriesForUser(0, shiftTypeId, workDate, userList.getUserId());
						if(listOfTimeSheetEntriesforUser != null && !listOfTimeSheetEntriesforUser.isEmpty()){
							for (UserWeekUtilisedTimeDTO userWeekUtilisedTimeDTO : listOfTimeSheetEntriesforUser) {
								int userId = userWeekUtilisedTimeDTO.getUserId();
								if(userId == userList.getUserId()){
									Integer totalTimeInMinutes = DateUtility.convertTimeInMinutes(userWeekUtilisedTimeDTO.getTimeSheetTotalHours(),userWeekUtilisedTimeDTO.getTimeSheetTotalMins());
									Integer remainingTimeSheetInMinutes = 2400-totalTimeInMinutes;
									userWeekUtilisedTimeDTO.setTimeSheetDuration(DateUtility.convertTimeInHoursMinutes(0,remainingTimeSheetInMinutes));
									jsonResourceAvailabilityPlan.setTimeSheetHours(String.valueOf(userWeekUtilisedTimeDTO.getTimeSheetDuration()));
								}
							}
						}	
					
			   List<UserWeekUtilisedTimeDTO> listOfReservedShifts = timeSheetManagementDAO.getReservedShiftsforUserId(userList.getUserId(), workDate) ;					
			   for (UserWeekUtilisedTimeDTO userWeekUtilisedTimeDTO : listOfReservedShifts) {
					long totalTime = 40-(userWeekUtilisedTimeDTO.getShiftBookingCount()*8);
					userWeekUtilisedTimeDTO.setShiftBookingDuration(String.valueOf(totalTime));
					jsonResourceAvailabilityPlan.setBookedHrs(totalTime);
				}		
			  
			   String searchShiftTypeAvailability =searchStrings.get("searchShiftTypeAvailability");
			   String searchShiftTypeBooking =searchStrings.get("searchShiftTypeBooking");
			   String searchTimeSheetHours =searchStrings.get("searchTimeSheetHours");
			   String searchBookedHrs =searchStrings.get("searchBookedHrs");
			   
			   if(searchShiftTypeAvailability != null && searchShiftTypeAvailability != "" ){
				   flagger = flagger+1;
				   if(jsonResourceAvailabilityPlan.getShiftTypeAvailability() == Integer.parseInt(searchShiftTypeAvailability)){
					   jsonResourceAvailabilityPlanList.add(jsonResourceAvailabilityPlan);   
				   }				   
			   }
			   if(searchShiftTypeBooking != null && searchShiftTypeBooking != "" ){
				   flagger = flagger+1;
				   if(jsonResourceAvailabilityPlan.getShiftTypeBooking() == Integer.parseInt(searchShiftTypeBooking)){
					   jsonResourceAvailabilityPlanList.add(jsonResourceAvailabilityPlan);   
				   }				   
			   }
			   if(searchTimeSheetHours != null && searchTimeSheetHours != ""){
				   flagger = flagger+1;
				   if( jsonResourceAvailabilityPlan.getTimeSheetHours().equals(searchTimeSheetHours)){
					   jsonResourceAvailabilityPlanList.add(jsonResourceAvailabilityPlan);   
				   }
			   }
			   if(searchBookedHrs != null && searchBookedHrs != ""){
				   flagger =flagger+1;
				   if(jsonResourceAvailabilityPlan.getBookedHrs() == Integer.parseInt(searchBookedHrs)){
					   jsonResourceAvailabilityPlanList.add(jsonResourceAvailabilityPlan);   
				   }
			   }
			   if(flagger <= 0){
				    
			   }
			   jsonResourceAvailabilityPlanList.add(jsonResourceAvailabilityPlan);
			}
			 if(jsonResourceAvailabilityPlanList != null && jsonResourceAvailabilityPlanList.size()>0){
				   for (JsonResourceAvailabilityPlan jsonResourceAvailPlan : jsonResourceAvailabilityPlanList) {
					   if(jsonResourceAvailPlan.getShiftTypeAvailability() == 1){
						   jsonResourceMarkedAvailableList.add(jsonResourceAvailPlan);
					   }else{
						   jsonResourceNotMarkedAvailableList.add(jsonResourceAvailPlan);
					   }
				   }
			   }
			   
			   if(jsonResourceMarkedAvailableList != null && jsonResourceMarkedAvailableList.size()>0){
				   jsonResourceAvailabilityCompleteList.addAll(jsonResourceMarkedAvailableList);
			   }
			   
			   if(jsonResourceNotMarkedAvailableList != null && jsonResourceNotMarkedAvailableList.size()>0){
				   jsonResourceAvailabilityCompleteList.addAll(jsonResourceNotMarkedAvailableList);
			   }
		} catch (Exception e) {
			log.error("ERROR  ",e);
		}
		flagger = 0;
		
		return jsonResourceAvailabilityCompleteList;
	}
	
	@Override
	public List<JsonResourceAvailabilityPlan> listResourcesForBookingPlanByShifId(Map<String, String> searchStrings, String searchResourcePoolName,String searchUserName,
			Integer resourcePoolId, Date workDate, Integer shiftTypeId) {
		
		log.info("Getting AvailabilityPlan for : " + resourcePoolId + " : " + workDate);
		int count=0;	
		List<JsonResourceAvailabilityPlan> jsonResourceAvailabilityPlanList = new ArrayList<JsonResourceAvailabilityPlan>();
		List<JsonResourceAvailabilityPlan> jsonResourceAvailabilityCompleteList = new ArrayList<JsonResourceAvailabilityPlan>();
		List<JsonResourceAvailabilityPlan> jsonResourceMarkedAvailableList = new ArrayList<JsonResourceAvailabilityPlan>();
		try {
			List<UserList> userListCollection =  resourceAvailabilityDAO.listResourcesForAvailabilityPlan(searchStrings, searchResourcePoolName,searchUserName,resourcePoolId, null, null);
			
			
			for(UserList userList : userListCollection) {
				JsonResourceAvailabilityPlan jsonResourceAvailabilityPlan = new JsonResourceAvailabilityPlan();
				jsonResourceAvailabilityPlan.setResourcePoolId(userList.getResourcePool().getResourcePoolId());
				jsonResourceAvailabilityPlan.setResourcePoolName(userList.getResourcePool().getResourcePoolName());
				jsonResourceAvailabilityPlan.setUserId(userList.getUserId());
				jsonResourceAvailabilityPlan.setUserName(userList.getUserDisplayName());
				jsonResourceAvailabilityPlan.setWorkDate(DateUtility.sdfDateformatWithOutTime(workDate));
				jsonResourceAvailabilityPlan.setRoleId(userList.getUserRoleMaster().getUserRoleId());
				jsonResourceAvailabilityPlan.setRoleName(userList.getUserRoleMaster().getRoleLabel());
				jsonResourceAvailabilityPlan.setUserTypeName(userList.getUserTypeMasterNew().getUserTypeLabel());
				jsonResourceAvailabilityPlan.setVendorId(userList.getVendor().getVendorId());
				jsonResourceAvailabilityPlan.setRegisteredCompanyName(userList.getVendor().getRegisteredCompanyName());
				
				
				StringBuffer skillarray = new StringBuffer();
				Set<UserSkills> userSkillsSet = userList.getUserSkills();
				int blocked_UserId = userList.getUserId();
					if(userSkillsSet != null && userSkillsSet.size()>0){
					skillarray= getSkillsOfUsers(userSkillsSet, blocked_UserId);			
				}
				
				if(skillarray.length() != 0){
					jsonResourceAvailabilityPlan.setSkillName(skillarray);
					jsonResourceAvailabilityPlan.setSkillNameCharacterlength(skillarray.length());
					
				}else{
					jsonResourceAvailabilityPlan.setSkillName(new StringBuffer(""));
					jsonResourceAvailabilityPlan.setSkillNameCharacterlength(0);
					
				}
				
				List<ResourceAvailability> resourceAvailabilityList =  resourceAvailabilityDAO.listResourceAttendanceByShiftId(userList.getUserId(), workDate,workDate, shiftTypeId, 1);
				Integer availabilityStatus = null;
				Integer bookingStatus = null;
				if(resourceAvailabilityList!=null && !resourceAvailabilityList.isEmpty()){
					for(ResourceAvailability resourceAvailability : resourceAvailabilityList) {
					
						if (resourceAvailability.getIsAvailable() == null || resourceAvailability.getIsAvailable().intValue()==0) {
							availabilityStatus = 0;
						} else {
							availabilityStatus = 1;
						}
						if (resourceAvailability.getBookForShift() == null || resourceAvailability.getBookForShift().intValue()==0) {
							bookingStatus = 0;
						} else {
							bookingStatus = 1;
						}
						if(resourceAvailability.getShiftTypeMaster().getShiftTypeId()!=null){
							jsonResourceAvailabilityPlan.setShiftTypeAvailability(availabilityStatus);
							jsonResourceAvailabilityPlan.setShiftTypeBooking(bookingStatus);
							jsonResourceAvailabilityPlan.setShiftTypeId(shiftTypeId);
								
						}else{
							availabilityStatus = 0;
							bookingStatus = 0;
							jsonResourceAvailabilityPlan.setShiftTypeAvailability(availabilityStatus);
							jsonResourceAvailabilityPlan.setShiftTypeBooking(bookingStatus);
							jsonResourceAvailabilityPlan.setShiftTypeId(shiftTypeId);
						}
						
					}
				}else{
					availabilityStatus = 0;
					bookingStatus = 0;
					jsonResourceAvailabilityPlan.setShiftTypeAvailability(availabilityStatus);
					jsonResourceAvailabilityPlan.setShiftTypeBooking(bookingStatus);
					jsonResourceAvailabilityPlan.setShiftTypeId(shiftTypeId);
				}
				
				List<UserWeekUtilisedTimeDTO> listOfTimeSheetEntriesforUser = timeSheetManagementDAO.getTimesheetEntriesForUser(0, shiftTypeId, workDate, userList.getUserId());
						if(listOfTimeSheetEntriesforUser != null && !listOfTimeSheetEntriesforUser.isEmpty()){
							for (UserWeekUtilisedTimeDTO userWeekUtilisedTimeDTO : listOfTimeSheetEntriesforUser) {
								int userId = userWeekUtilisedTimeDTO.getUserId();
								if(userId == userList.getUserId()){
									Integer totalTimeInMinutes = DateUtility.convertTimeInMinutes(userWeekUtilisedTimeDTO.getTimeSheetTotalHours(),userWeekUtilisedTimeDTO.getTimeSheetTotalMins());
									Integer remainingTimeSheetInMinutes = 2400-totalTimeInMinutes;
									userWeekUtilisedTimeDTO.setTimeSheetDuration(DateUtility.convertTimeInHoursMinutes(0,remainingTimeSheetInMinutes));
									jsonResourceAvailabilityPlan.setTimeSheetHours(String.valueOf(userWeekUtilisedTimeDTO.getTimeSheetDuration()));
								}
							}
						}	
					
			   List<UserWeekUtilisedTimeDTO> listOfReservedShifts = timeSheetManagementDAO.getReservedShiftsforUserId(userList.getUserId(), workDate) ;					
			   for (UserWeekUtilisedTimeDTO userWeekUtilisedTimeDTO : listOfReservedShifts) {
					long totalTime = 40-(userWeekUtilisedTimeDTO.getShiftBookingCount()*8);
					userWeekUtilisedTimeDTO.setShiftBookingDuration(String.valueOf(totalTime));
					jsonResourceAvailabilityPlan.setBookedHrs(totalTime);
				}		
			   
			   String searchShiftTypeAvailability =searchStrings.get("searchShiftTypeAvailability");
			   String searchShiftTypeBooking =searchStrings.get("searchShiftTypeBooking");
			   String searchTimeSheetHours =searchStrings.get("searchTimeSheetHours");
			   String searchBookedHrs =searchStrings.get("searchBookedHrs");
			   
			   if(searchShiftTypeAvailability != null && searchShiftTypeAvailability != "" ){
				   flagger = flagger+1;
				   if(jsonResourceAvailabilityPlan.getShiftTypeAvailability() == Integer.parseInt(searchShiftTypeAvailability)){
					   jsonResourceAvailabilityPlanList.add(jsonResourceAvailabilityPlan);   
				   }				   
			   }
			   if(searchShiftTypeBooking != null && searchShiftTypeBooking != "" ){
				   flagger = flagger+1;
				   if(jsonResourceAvailabilityPlan.getShiftTypeBooking() == Integer.parseInt(searchShiftTypeBooking)){
					   jsonResourceAvailabilityPlanList.add(jsonResourceAvailabilityPlan);   
				   }				   
			   }
			   if(searchTimeSheetHours != null && searchTimeSheetHours != ""){
				   flagger = flagger+1;
				   if( jsonResourceAvailabilityPlan.getTimeSheetHours().equals(searchTimeSheetHours)){
					   jsonResourceAvailabilityPlanList.add(jsonResourceAvailabilityPlan);   
				   }
			   }
			   if(searchBookedHrs != null && searchBookedHrs != ""){
				   flagger =flagger+1;
				   if(jsonResourceAvailabilityPlan.getBookedHrs() == Integer.parseInt(searchBookedHrs)){
					   jsonResourceAvailabilityPlanList.add(jsonResourceAvailabilityPlan);   
				   }
			   }
			   if(flagger <= 0){
				    
			   }
			   jsonResourceAvailabilityPlanList.add(jsonResourceAvailabilityPlan);
			}
			 if(jsonResourceAvailabilityPlanList != null && jsonResourceAvailabilityPlanList.size()>0){
				   for (JsonResourceAvailabilityPlan jsonResourceAvailPlan : jsonResourceAvailabilityPlanList) {
					   if(jsonResourceAvailPlan.getShiftTypeAvailability() == 1){
						   jsonResourceMarkedAvailableList.add(jsonResourceAvailPlan);
					   }else{
						   jsonResourceMarkedAvailableList.remove(jsonResourceAvailPlan);
					   }
				   }
			   }
			   
			   if(jsonResourceMarkedAvailableList != null && jsonResourceMarkedAvailableList.size()>0){
				   jsonResourceAvailabilityCompleteList.addAll(jsonResourceMarkedAvailableList);
			   }
			   
		} catch (Exception e) {
			log.error("ERROR  ",e);
		}
		flagger = 0;
		return jsonResourceAvailabilityCompleteList;
	}
	
	@Override
	public List<WorkPackageDemandProjection> listWorkpackageDemandProjection(
			Integer testFactoryLabId, Integer testFactoryId, Integer productId,
			Integer workPackageId, Integer shiftId,Date workDate, Integer jtStartIndex, Integer jtPageSize) {
		return resourceAvailabilityDAO.listWorkpackageDemandProjection(testFactoryLabId, testFactoryId, productId, workPackageId, shiftId,workDate, jtStartIndex, jtPageSize);
	}

	@Override
	@Transactional
	public List<JsonWorkPackageWeeklyDemandProjection> listWorkpackageWeeklyDemandProjection(
			Integer testFactoryLabId, Integer testFactoryId, Integer productId,
			Integer workPackageId, Integer shiftId, Integer workWeek, Integer workYear) {
		
		List<WeeklyResourceDemandDTO> weeklyResourceDemandDTOs = resourceAvailabilityDAO.listWorkpackageWeeklyDemandAndReservedCountProjection(workPackageId,workYear);
		
		
		
		
		List<JsonWorkPackageWeeklyDemandProjection> jsonWorkPackageWeeklyDemandProjections = new ArrayList<JsonWorkPackageWeeklyDemandProjection>();
		
		WorkPackage workPackage = workPackageDAO.getWorkPackageById(workPackageId);
		
		List<WorkShiftMaster> workShifts = workShiftMasterDAO.listWorkShiftsByTestFactoryId(testFactoryId);
		log.info("testFactoryId ========== "+testFactoryId);
		if(weeklyResourceDemandDTOs == null || weeklyResourceDemandDTOs.size() == 0){
			
			
			
			for(WorkShiftMaster shift:workShifts){
				JsonWorkPackageWeeklyDemandProjection jsonWorkPackageWeeklyDemandProjection= new JsonWorkPackageWeeklyDemandProjection();
				jsonWorkPackageWeeklyDemandProjection.setWorkPackageId(workPackageId);
				jsonWorkPackageWeeklyDemandProjection.setWorkYear(workYear);
				jsonWorkPackageWeeklyDemandProjection.setWorkPackageName(workPackage.getName());
				jsonWorkPackageWeeklyDemandProjection.setShiftId(shift.getShiftId());
				jsonWorkPackageWeeklyDemandProjection.setShiftName(shift.getShiftName());
				jsonWorkPackageWeeklyDemandProjections.add(jsonWorkPackageWeeklyDemandProjection);
				
			}
			
			
		}else{
			for (WeeklyResourceDemandDTO weeklyResourceDemandDTO : weeklyResourceDemandDTOs) {
				
				//Check if the json object for this demand object is already present. If yes, update it, else create a new one.
				JsonWorkPackageWeeklyDemandProjection jsonWorkPackageWeeklyDemandProjection = getJsonWorkPackageWeeklyDemandProjection(jsonWorkPackageWeeklyDemandProjections, weeklyResourceDemandDTO); 
				if (jsonWorkPackageWeeklyDemandProjection == null) {
					
					jsonWorkPackageWeeklyDemandProjection = new JsonWorkPackageWeeklyDemandProjection();
					
					jsonWorkPackageWeeklyDemandProjection.setWorkPackageId(weeklyResourceDemandDTO.getWorkPackageId());
					jsonWorkPackageWeeklyDemandProjection.setWorkPackageName(weeklyResourceDemandDTO.getWorkPackageName());
					jsonWorkPackageWeeklyDemandProjection.setShiftId(weeklyResourceDemandDTO.getShiftId());
					jsonWorkPackageWeeklyDemandProjection.setShiftName(weeklyResourceDemandDTO.getShiftName());
					jsonWorkPackageWeeklyDemandProjection.setUserRoleId(weeklyResourceDemandDTO.getRoleId());
					jsonWorkPackageWeeklyDemandProjection.setUserRoleName(weeklyResourceDemandDTO.getRoleName());
					jsonWorkPackageWeeklyDemandProjection.setSkillId(weeklyResourceDemandDTO.getSkillId());
					jsonWorkPackageWeeklyDemandProjection.setSkillName(weeklyResourceDemandDTO.getSkillName());
					jsonWorkPackageWeeklyDemandProjection.setWorkWeek(weeklyResourceDemandDTO.getWorkWeek());
					jsonWorkPackageWeeklyDemandProjection.setWorkYear(weeklyResourceDemandDTO.getWorkYear());
					jsonWorkPackageWeeklyDemandProjection.setDemandRaisedByUserId(weeklyResourceDemandDTO.getDemandRaisedByUserId());
					jsonWorkPackageWeeklyDemandProjection.setDemandRaisedByUserName(weeklyResourceDemandDTO.getDemandRaisedByUserName());
					jsonWorkPackageWeeklyDemandProjection.setUserTypeId(weeklyResourceDemandDTO.getUserTypeId());
					jsonWorkPackageWeeklyDemandProjection.setUserTypeName(weeklyResourceDemandDTO.getUserTypeName());
					jsonWorkPackageWeeklyDemandProjections.add(jsonWorkPackageWeeklyDemandProjection);
				}
				jsonWorkPackageWeeklyDemandProjection.setGroupDemandId(weeklyResourceDemandDTO.getGroupDemandId());
				
				Integer workWeekNo = weeklyResourceDemandDTO.getWorkWeek();
				if(workWeekNo == null){
					workWeekNo = 0;
				}
				
				float weeklyResourceCount = weeklyResourceDemandDTO.getResourceCount();
				float reservedResourceCount = weeklyResourceDemandDTO.getReservedResourceCount();
				reservedResourceCount = reservedResourceCount/5;
				reservedResourceCount = reservedResourceCount/100;
				weeklyResourceCount= weeklyResourceCount/5;
				
				
				switch (workWeekNo) {
					case 1:
						if(jsonWorkPackageWeeklyDemandProjection.getWeek1() != null){
							weeklyResourceCount = weeklyResourceCount + jsonWorkPackageWeeklyDemandProjection.getWeek1();
						}
						if(jsonWorkPackageWeeklyDemandProjection.getReservedResourceCountWk1() != 0){
							reservedResourceCount = reservedResourceCount + jsonWorkPackageWeeklyDemandProjection.getReservedResourceCountWk1();
						}
						jsonWorkPackageWeeklyDemandProjection.setWeek1(weeklyResourceCount);
						jsonWorkPackageWeeklyDemandProjection.setReservedResourceCountWk1(reservedResourceCount);
						break;
					case 2:
						if(jsonWorkPackageWeeklyDemandProjection.getWeek2() != null){
							weeklyResourceCount = weeklyResourceCount + jsonWorkPackageWeeklyDemandProjection.getWeek2();
						}
						if(jsonWorkPackageWeeklyDemandProjection.getReservedResourceCountWk2() != 0){
							reservedResourceCount = reservedResourceCount + jsonWorkPackageWeeklyDemandProjection.getReservedResourceCountWk2();
						}
						jsonWorkPackageWeeklyDemandProjection.setWeek2(weeklyResourceCount);	
						jsonWorkPackageWeeklyDemandProjection.setReservedResourceCountWk2(reservedResourceCount);
						break;
					case 3:
						if(jsonWorkPackageWeeklyDemandProjection.getWeek3() != null){
							weeklyResourceCount = weeklyResourceCount + jsonWorkPackageWeeklyDemandProjection.getWeek3();
						}
						if(jsonWorkPackageWeeklyDemandProjection.getReservedResourceCountWk3() != 0){
							reservedResourceCount = reservedResourceCount + jsonWorkPackageWeeklyDemandProjection.getReservedResourceCountWk3();
						}
						jsonWorkPackageWeeklyDemandProjection.setWeek3(weeklyResourceCount);
						jsonWorkPackageWeeklyDemandProjection.setReservedResourceCountWk3(reservedResourceCount);
						break;
					case 4:
						if(jsonWorkPackageWeeklyDemandProjection.getWeek4() != null){
							weeklyResourceCount = weeklyResourceCount + jsonWorkPackageWeeklyDemandProjection.getWeek4();
						}
						if(jsonWorkPackageWeeklyDemandProjection.getReservedResourceCountWk4() != 0){
							reservedResourceCount = reservedResourceCount + jsonWorkPackageWeeklyDemandProjection.getReservedResourceCountWk4();
						}
						
						jsonWorkPackageWeeklyDemandProjection.setWeek4(weeklyResourceCount);	
						jsonWorkPackageWeeklyDemandProjection.setReservedResourceCountWk4(reservedResourceCount);
						break;
					case 5:
						if(jsonWorkPackageWeeklyDemandProjection.getWeek5() != null){
							weeklyResourceCount = weeklyResourceCount + jsonWorkPackageWeeklyDemandProjection.getWeek5();
						}
						if(jsonWorkPackageWeeklyDemandProjection.getReservedResourceCountWk5() != 0){
							reservedResourceCount = reservedResourceCount + jsonWorkPackageWeeklyDemandProjection.getReservedResourceCountWk5();
						}
						jsonWorkPackageWeeklyDemandProjection.setWeek5(weeklyResourceCount);	
						jsonWorkPackageWeeklyDemandProjection.setReservedResourceCountWk5(reservedResourceCount);
						break;
					case 6:
						if(jsonWorkPackageWeeklyDemandProjection.getWeek6() != null){
							weeklyResourceCount = weeklyResourceCount + jsonWorkPackageWeeklyDemandProjection.getWeek6();
						}
						if(jsonWorkPackageWeeklyDemandProjection.getReservedResourceCountWk6() != 0){
							reservedResourceCount = reservedResourceCount + jsonWorkPackageWeeklyDemandProjection.getReservedResourceCountWk6();
						}
						
						jsonWorkPackageWeeklyDemandProjection.setWeek6(weeklyResourceCount);	
						jsonWorkPackageWeeklyDemandProjection.setReservedResourceCountWk6(reservedResourceCount);
						break;
					case 7:
						if(jsonWorkPackageWeeklyDemandProjection.getWeek7() != null){
							weeklyResourceCount = weeklyResourceCount + jsonWorkPackageWeeklyDemandProjection.getWeek7();
						}
						if(jsonWorkPackageWeeklyDemandProjection.getReservedResourceCountWk4() != 0){
							reservedResourceCount = reservedResourceCount + jsonWorkPackageWeeklyDemandProjection.getReservedResourceCountWk7();
						}
						
						jsonWorkPackageWeeklyDemandProjection.setWeek7(weeklyResourceCount);	
						jsonWorkPackageWeeklyDemandProjection.setReservedResourceCountWk7(reservedResourceCount);
						break;
					case 8:
						if(jsonWorkPackageWeeklyDemandProjection.getWeek8() != null){
							weeklyResourceCount = weeklyResourceCount + jsonWorkPackageWeeklyDemandProjection.getWeek8();
						}
						if(jsonWorkPackageWeeklyDemandProjection.getReservedResourceCountWk8() != 0){
							reservedResourceCount = reservedResourceCount + jsonWorkPackageWeeklyDemandProjection.getReservedResourceCountWk8();
						}
						
						jsonWorkPackageWeeklyDemandProjection.setWeek8(weeklyResourceCount);	
						jsonWorkPackageWeeklyDemandProjection.setReservedResourceCountWk8(reservedResourceCount);
						break;
					case 9:
						if(jsonWorkPackageWeeklyDemandProjection.getWeek9() != null){
							weeklyResourceCount = weeklyResourceCount + jsonWorkPackageWeeklyDemandProjection.getWeek9();
						}
						if(jsonWorkPackageWeeklyDemandProjection.getReservedResourceCountWk9() != 0){
							reservedResourceCount = reservedResourceCount + jsonWorkPackageWeeklyDemandProjection.getReservedResourceCountWk9();
						}
						
						jsonWorkPackageWeeklyDemandProjection.setWeek9(weeklyResourceCount);	
						jsonWorkPackageWeeklyDemandProjection.setReservedResourceCountWk9(reservedResourceCount);
						break;
					case 10:
						if(jsonWorkPackageWeeklyDemandProjection.getWeek10() != null){
							weeklyResourceCount = weeklyResourceCount + jsonWorkPackageWeeklyDemandProjection.getWeek10();
						}
						if(jsonWorkPackageWeeklyDemandProjection.getReservedResourceCountWk10() != 0){
							reservedResourceCount = reservedResourceCount + jsonWorkPackageWeeklyDemandProjection.getReservedResourceCountWk10();
						}
						
						jsonWorkPackageWeeklyDemandProjection.setWeek10(weeklyResourceCount);	
						jsonWorkPackageWeeklyDemandProjection.setReservedResourceCountWk10(reservedResourceCount);
						break;
					case 11:
						if(jsonWorkPackageWeeklyDemandProjection.getWeek11() != null){
							weeklyResourceCount = weeklyResourceCount + jsonWorkPackageWeeklyDemandProjection.getWeek11();
						}
						if(jsonWorkPackageWeeklyDemandProjection.getReservedResourceCountWk11() != 0){
							reservedResourceCount = reservedResourceCount + jsonWorkPackageWeeklyDemandProjection.getReservedResourceCountWk11();
						}
						
						jsonWorkPackageWeeklyDemandProjection.setWeek11(weeklyResourceCount);	
						jsonWorkPackageWeeklyDemandProjection.setReservedResourceCountWk11(reservedResourceCount);
						break;
					case 12:
						if(jsonWorkPackageWeeklyDemandProjection.getWeek12() != null){
							weeklyResourceCount = weeklyResourceCount + jsonWorkPackageWeeklyDemandProjection.getWeek12();
						}
						if(jsonWorkPackageWeeklyDemandProjection.getReservedResourceCountWk12() != 0){
							reservedResourceCount = reservedResourceCount + jsonWorkPackageWeeklyDemandProjection.getReservedResourceCountWk12();
						}
						
						jsonWorkPackageWeeklyDemandProjection.setWeek12(weeklyResourceCount);	
						jsonWorkPackageWeeklyDemandProjection.setReservedResourceCountWk12(reservedResourceCount);
						break;
					case 13:
						if(jsonWorkPackageWeeklyDemandProjection.getWeek13() != null){
							weeklyResourceCount = weeklyResourceCount + jsonWorkPackageWeeklyDemandProjection.getWeek13();
						}
						if(jsonWorkPackageWeeklyDemandProjection.getReservedResourceCountWk13() != 0){
							reservedResourceCount = reservedResourceCount + jsonWorkPackageWeeklyDemandProjection.getReservedResourceCountWk13();
						}
						
						jsonWorkPackageWeeklyDemandProjection.setWeek13(weeklyResourceCount);	
						jsonWorkPackageWeeklyDemandProjection.setReservedResourceCountWk13(reservedResourceCount);
						break;
					case 14:
						if(jsonWorkPackageWeeklyDemandProjection.getWeek14() != null){
							weeklyResourceCount = weeklyResourceCount + jsonWorkPackageWeeklyDemandProjection.getWeek14();
						}
						if(jsonWorkPackageWeeklyDemandProjection.getReservedResourceCountWk14() != 0){
							reservedResourceCount = reservedResourceCount + jsonWorkPackageWeeklyDemandProjection.getReservedResourceCountWk14();
						}
						
						jsonWorkPackageWeeklyDemandProjection.setWeek14(weeklyResourceCount);
						jsonWorkPackageWeeklyDemandProjection.setReservedResourceCountWk14(reservedResourceCount);
						break;
					case 15:
						if(jsonWorkPackageWeeklyDemandProjection.getWeek15() != null){
							weeklyResourceCount = weeklyResourceCount + jsonWorkPackageWeeklyDemandProjection.getWeek15();
						}
						if(jsonWorkPackageWeeklyDemandProjection.getReservedResourceCountWk15() != 0){
							reservedResourceCount = reservedResourceCount + jsonWorkPackageWeeklyDemandProjection.getReservedResourceCountWk15();
						}
						
						jsonWorkPackageWeeklyDemandProjection.setWeek15(weeklyResourceCount);
						jsonWorkPackageWeeklyDemandProjection.setReservedResourceCountWk15(reservedResourceCount);	
						break;
					case 16:
						if(jsonWorkPackageWeeklyDemandProjection.getWeek16() != null){
							weeklyResourceCount = weeklyResourceCount + jsonWorkPackageWeeklyDemandProjection.getWeek16();
						}
						if(jsonWorkPackageWeeklyDemandProjection.getReservedResourceCountWk16() != 0){
							reservedResourceCount = reservedResourceCount + jsonWorkPackageWeeklyDemandProjection.getReservedResourceCountWk16();
						}
						
						jsonWorkPackageWeeklyDemandProjection.setWeek16(weeklyResourceCount);	
						jsonWorkPackageWeeklyDemandProjection.setReservedResourceCountWk16(reservedResourceCount);
						break;
					case 17:
						if(jsonWorkPackageWeeklyDemandProjection.getWeek17() != null){
							weeklyResourceCount = weeklyResourceCount + jsonWorkPackageWeeklyDemandProjection.getWeek17();
						}
						if(jsonWorkPackageWeeklyDemandProjection.getReservedResourceCountWk17() != 0){
							reservedResourceCount = reservedResourceCount + jsonWorkPackageWeeklyDemandProjection.getReservedResourceCountWk17();
						}
						
						jsonWorkPackageWeeklyDemandProjection.setWeek17(weeklyResourceCount);
						jsonWorkPackageWeeklyDemandProjection.setReservedResourceCountWk17(reservedResourceCount);
						break;
					case 18:
						if(jsonWorkPackageWeeklyDemandProjection.getWeek18() != null){
							weeklyResourceCount = weeklyResourceCount + jsonWorkPackageWeeklyDemandProjection.getWeek18();
						}
						if(jsonWorkPackageWeeklyDemandProjection.getReservedResourceCountWk18() != 0){
							reservedResourceCount = reservedResourceCount + jsonWorkPackageWeeklyDemandProjection.getReservedResourceCountWk18();
						}
						
						jsonWorkPackageWeeklyDemandProjection.setWeek18(weeklyResourceCount);
						jsonWorkPackageWeeklyDemandProjection.setReservedResourceCountWk18(reservedResourceCount);
						
						break;
					case 19:
						if(jsonWorkPackageWeeklyDemandProjection.getWeek19() != null){
							weeklyResourceCount = weeklyResourceCount + jsonWorkPackageWeeklyDemandProjection.getWeek19();
						}
						if(jsonWorkPackageWeeklyDemandProjection.getReservedResourceCountWk19() != 0){
							reservedResourceCount = reservedResourceCount + jsonWorkPackageWeeklyDemandProjection.getReservedResourceCountWk19();
						}
						
						jsonWorkPackageWeeklyDemandProjection.setWeek19(weeklyResourceCount);
						jsonWorkPackageWeeklyDemandProjection.setReservedResourceCountWk19(reservedResourceCount);
						break;
					case 20:
						if(jsonWorkPackageWeeklyDemandProjection.getWeek20() != null){
							weeklyResourceCount = weeklyResourceCount + jsonWorkPackageWeeklyDemandProjection.getWeek20();
						}
						if(jsonWorkPackageWeeklyDemandProjection.getReservedResourceCountWk20() != 0){
							reservedResourceCount = reservedResourceCount + jsonWorkPackageWeeklyDemandProjection.getReservedResourceCountWk20();
						}
						
						jsonWorkPackageWeeklyDemandProjection.setWeek20(weeklyResourceCount);
						jsonWorkPackageWeeklyDemandProjection.setReservedResourceCountWk20(reservedResourceCount);
						break;
					case 21:
						if(jsonWorkPackageWeeklyDemandProjection.getWeek21() != null){
							weeklyResourceCount = weeklyResourceCount + jsonWorkPackageWeeklyDemandProjection.getWeek21();
						}
						if(jsonWorkPackageWeeklyDemandProjection.getReservedResourceCountWk21() != 0){
							reservedResourceCount = reservedResourceCount + jsonWorkPackageWeeklyDemandProjection.getReservedResourceCountWk21();
						}
						
						jsonWorkPackageWeeklyDemandProjection.setWeek21(weeklyResourceCount);	
						jsonWorkPackageWeeklyDemandProjection.setReservedResourceCountWk21(reservedResourceCount);
						break;
					case 22:
						if(jsonWorkPackageWeeklyDemandProjection.getWeek22() != null){
							weeklyResourceCount = weeklyResourceCount + jsonWorkPackageWeeklyDemandProjection.getWeek22();
						}
						if(jsonWorkPackageWeeklyDemandProjection.getReservedResourceCountWk22() != 0){
							reservedResourceCount = reservedResourceCount + jsonWorkPackageWeeklyDemandProjection.getReservedResourceCountWk22();
						}
						
						jsonWorkPackageWeeklyDemandProjection.setWeek22(weeklyResourceCount);	
						jsonWorkPackageWeeklyDemandProjection.setReservedResourceCountWk22(reservedResourceCount);
						break;
					case 23:
						if(jsonWorkPackageWeeklyDemandProjection.getWeek23() != null){
							weeklyResourceCount = weeklyResourceCount + jsonWorkPackageWeeklyDemandProjection.getWeek23();
						}
						if(jsonWorkPackageWeeklyDemandProjection.getReservedResourceCountWk23() != 0){
							reservedResourceCount = reservedResourceCount + jsonWorkPackageWeeklyDemandProjection.getReservedResourceCountWk23();
						}
						
						jsonWorkPackageWeeklyDemandProjection.setWeek23(weeklyResourceCount);
						jsonWorkPackageWeeklyDemandProjection.setReservedResourceCountWk23(reservedResourceCount);
						break;
					case 24:
						if(jsonWorkPackageWeeklyDemandProjection.getWeek24() != null){
							weeklyResourceCount = weeklyResourceCount + jsonWorkPackageWeeklyDemandProjection.getWeek24();
						}
						if(jsonWorkPackageWeeklyDemandProjection.getReservedResourceCountWk24() != 0){
							reservedResourceCount = reservedResourceCount + jsonWorkPackageWeeklyDemandProjection.getReservedResourceCountWk24();
						}
						
						jsonWorkPackageWeeklyDemandProjection.setWeek24(weeklyResourceCount);
						jsonWorkPackageWeeklyDemandProjection.setReservedResourceCountWk24(reservedResourceCount);
						break;
					case 25:
						if(jsonWorkPackageWeeklyDemandProjection.getWeek25() != null){
							weeklyResourceCount = weeklyResourceCount + jsonWorkPackageWeeklyDemandProjection.getWeek25();
						}
						if(jsonWorkPackageWeeklyDemandProjection.getReservedResourceCountWk25() != 0){
							reservedResourceCount = reservedResourceCount + jsonWorkPackageWeeklyDemandProjection.getReservedResourceCountWk25();
						}
						
						jsonWorkPackageWeeklyDemandProjection.setWeek25(weeklyResourceCount);	
						jsonWorkPackageWeeklyDemandProjection.setReservedResourceCountWk25(reservedResourceCount);
						break;
					case 26:
						if(jsonWorkPackageWeeklyDemandProjection.getWeek26() != null){
							weeklyResourceCount = weeklyResourceCount + jsonWorkPackageWeeklyDemandProjection.getWeek26();
						}
						if(jsonWorkPackageWeeklyDemandProjection.getReservedResourceCountWk26() != 0){
							reservedResourceCount = reservedResourceCount + jsonWorkPackageWeeklyDemandProjection.getReservedResourceCountWk26();
						}
						
						jsonWorkPackageWeeklyDemandProjection.setWeek26(weeklyResourceCount);
						jsonWorkPackageWeeklyDemandProjection.setReservedResourceCountWk26(reservedResourceCount);
						break;
					case 27:
						if(jsonWorkPackageWeeklyDemandProjection.getWeek27() != null){
							weeklyResourceCount = weeklyResourceCount + jsonWorkPackageWeeklyDemandProjection.getWeek27();
						}
						if(jsonWorkPackageWeeklyDemandProjection.getReservedResourceCountWk27() != 0){
							reservedResourceCount = reservedResourceCount + jsonWorkPackageWeeklyDemandProjection.getReservedResourceCountWk27();
						}
						
						jsonWorkPackageWeeklyDemandProjection.setWeek27(weeklyResourceCount);
						jsonWorkPackageWeeklyDemandProjection.setReservedResourceCountWk27(reservedResourceCount);
						break;
					case 28:
						if(jsonWorkPackageWeeklyDemandProjection.getWeek28() != null){
							weeklyResourceCount = weeklyResourceCount + jsonWorkPackageWeeklyDemandProjection.getWeek28();
						}
						if(jsonWorkPackageWeeklyDemandProjection.getReservedResourceCountWk28() != 0){
							reservedResourceCount = reservedResourceCount + jsonWorkPackageWeeklyDemandProjection.getReservedResourceCountWk28();
						}
						
						jsonWorkPackageWeeklyDemandProjection.setWeek28(weeklyResourceCount);
						jsonWorkPackageWeeklyDemandProjection.setReservedResourceCountWk28(reservedResourceCount);
						break;
					case 29:
						if(jsonWorkPackageWeeklyDemandProjection.getWeek29() != null){
							weeklyResourceCount = weeklyResourceCount + jsonWorkPackageWeeklyDemandProjection.getWeek29();
						}
						if(jsonWorkPackageWeeklyDemandProjection.getReservedResourceCountWk29() != 0){
							reservedResourceCount = reservedResourceCount + jsonWorkPackageWeeklyDemandProjection.getReservedResourceCountWk29();
						}
						
						jsonWorkPackageWeeklyDemandProjection.setWeek29(weeklyResourceCount);
						jsonWorkPackageWeeklyDemandProjection.setReservedResourceCountWk29(reservedResourceCount);
						break;
					case 30:
						if(jsonWorkPackageWeeklyDemandProjection.getWeek30() != null){
							weeklyResourceCount = weeklyResourceCount + jsonWorkPackageWeeklyDemandProjection.getWeek30();
						}
						if(jsonWorkPackageWeeklyDemandProjection.getReservedResourceCountWk30() != 0){
							reservedResourceCount = reservedResourceCount + jsonWorkPackageWeeklyDemandProjection.getReservedResourceCountWk30();
						}
						
						jsonWorkPackageWeeklyDemandProjection.setWeek30(weeklyResourceCount);
						jsonWorkPackageWeeklyDemandProjection.setReservedResourceCountWk30(reservedResourceCount);
						break;
					case 31:
						if(jsonWorkPackageWeeklyDemandProjection.getWeek31() != null){
							weeklyResourceCount = weeklyResourceCount + jsonWorkPackageWeeklyDemandProjection.getWeek31();
						}
						if(jsonWorkPackageWeeklyDemandProjection.getReservedResourceCountWk31() != 0){
							reservedResourceCount = reservedResourceCount + jsonWorkPackageWeeklyDemandProjection.getReservedResourceCountWk31();
						}
						
						jsonWorkPackageWeeklyDemandProjection.setWeek31(weeklyResourceCount);	
						jsonWorkPackageWeeklyDemandProjection.setReservedResourceCountWk31(reservedResourceCount);
						break;
					case 32:
						if(jsonWorkPackageWeeklyDemandProjection.getWeek32() != null){
							weeklyResourceCount = weeklyResourceCount + jsonWorkPackageWeeklyDemandProjection.getWeek32();
						}
						if(jsonWorkPackageWeeklyDemandProjection.getReservedResourceCountWk32() != 0){
							reservedResourceCount = reservedResourceCount + jsonWorkPackageWeeklyDemandProjection.getReservedResourceCountWk32();
						}
						
						jsonWorkPackageWeeklyDemandProjection.setWeek32(weeklyResourceCount);
						jsonWorkPackageWeeklyDemandProjection.setReservedResourceCountWk32(reservedResourceCount);
						break;
					case 33:
						if(jsonWorkPackageWeeklyDemandProjection.getWeek33() != null){
							weeklyResourceCount = weeklyResourceCount + jsonWorkPackageWeeklyDemandProjection.getWeek33();
						}
						if(jsonWorkPackageWeeklyDemandProjection.getReservedResourceCountWk33() != 0){
							reservedResourceCount = reservedResourceCount + jsonWorkPackageWeeklyDemandProjection.getReservedResourceCountWk33();
						}
						
						jsonWorkPackageWeeklyDemandProjection.setWeek33(weeklyResourceCount);
						jsonWorkPackageWeeklyDemandProjection.setReservedResourceCountWk33(reservedResourceCount);
						break;
					case 34:
						if(jsonWorkPackageWeeklyDemandProjection.getWeek34() != null){
							weeklyResourceCount = weeklyResourceCount + jsonWorkPackageWeeklyDemandProjection.getWeek34();
						}
						if(jsonWorkPackageWeeklyDemandProjection.getReservedResourceCountWk34() != 0){
							reservedResourceCount = reservedResourceCount + jsonWorkPackageWeeklyDemandProjection.getReservedResourceCountWk34();
						}
						
						jsonWorkPackageWeeklyDemandProjection.setWeek34(weeklyResourceCount);	
						jsonWorkPackageWeeklyDemandProjection.setReservedResourceCountWk34(reservedResourceCount);
						break;
					case 35:
						if(jsonWorkPackageWeeklyDemandProjection.getWeek35() != null){
							weeklyResourceCount = weeklyResourceCount + jsonWorkPackageWeeklyDemandProjection.getWeek35();
						}
						if(jsonWorkPackageWeeklyDemandProjection.getReservedResourceCountWk35() != 0){
							reservedResourceCount = reservedResourceCount + jsonWorkPackageWeeklyDemandProjection.getReservedResourceCountWk35();
						}
						
						jsonWorkPackageWeeklyDemandProjection.setWeek35(weeklyResourceCount);	
						jsonWorkPackageWeeklyDemandProjection.setReservedResourceCountWk35(reservedResourceCount);
						break;
					case 36:
						if(jsonWorkPackageWeeklyDemandProjection.getWeek36() != null){
							weeklyResourceCount = weeklyResourceCount + jsonWorkPackageWeeklyDemandProjection.getWeek36();
						}
						if(jsonWorkPackageWeeklyDemandProjection.getReservedResourceCountWk36() != 0){
							reservedResourceCount = reservedResourceCount + jsonWorkPackageWeeklyDemandProjection.getReservedResourceCountWk36();
						}
						
						jsonWorkPackageWeeklyDemandProjection.setWeek36(weeklyResourceCount);	
						jsonWorkPackageWeeklyDemandProjection.setReservedResourceCountWk36(reservedResourceCount);
						break;
					case 37:
						if(jsonWorkPackageWeeklyDemandProjection.getWeek37() != null){
							weeklyResourceCount = weeklyResourceCount + jsonWorkPackageWeeklyDemandProjection.getWeek37();
						}
						if(jsonWorkPackageWeeklyDemandProjection.getReservedResourceCountWk37() != 0){
							reservedResourceCount = reservedResourceCount + jsonWorkPackageWeeklyDemandProjection.getReservedResourceCountWk37();
						}
						
						jsonWorkPackageWeeklyDemandProjection.setWeek37(weeklyResourceCount);	
						jsonWorkPackageWeeklyDemandProjection.setReservedResourceCountWk37(reservedResourceCount);
						break;
					case 38:
						if(jsonWorkPackageWeeklyDemandProjection.getWeek38() != null){
							weeklyResourceCount = weeklyResourceCount + jsonWorkPackageWeeklyDemandProjection.getWeek38();
						}
						if(jsonWorkPackageWeeklyDemandProjection.getReservedResourceCountWk38() != 0){
							reservedResourceCount = reservedResourceCount + jsonWorkPackageWeeklyDemandProjection.getReservedResourceCountWk38();
						}
						
						jsonWorkPackageWeeklyDemandProjection.setWeek38(weeklyResourceCount);	
						jsonWorkPackageWeeklyDemandProjection.setReservedResourceCountWk38(reservedResourceCount);
						break;
					case 39:
						if(jsonWorkPackageWeeklyDemandProjection.getWeek39() != null){
							weeklyResourceCount = weeklyResourceCount + jsonWorkPackageWeeklyDemandProjection.getWeek39();
						}
						if(jsonWorkPackageWeeklyDemandProjection.getReservedResourceCountWk39() != 0){
							reservedResourceCount = reservedResourceCount + jsonWorkPackageWeeklyDemandProjection.getReservedResourceCountWk39();
						}
						
						jsonWorkPackageWeeklyDemandProjection.setWeek39(weeklyResourceCount);
						jsonWorkPackageWeeklyDemandProjection.setReservedResourceCountWk39(reservedResourceCount);
						break;
					case 40:
						if(jsonWorkPackageWeeklyDemandProjection.getWeek40() != null){
							weeklyResourceCount = weeklyResourceCount + jsonWorkPackageWeeklyDemandProjection.getWeek40();
						}
						if(jsonWorkPackageWeeklyDemandProjection.getReservedResourceCountWk40() != 0){
							reservedResourceCount = reservedResourceCount + jsonWorkPackageWeeklyDemandProjection.getReservedResourceCountWk40();
						}
						
						jsonWorkPackageWeeklyDemandProjection.setWeek40(weeklyResourceCount);
						jsonWorkPackageWeeklyDemandProjection.setReservedResourceCountWk40(reservedResourceCount);
						break;
					case 41:
						if(jsonWorkPackageWeeklyDemandProjection.getWeek41() != null){
							weeklyResourceCount = weeklyResourceCount + jsonWorkPackageWeeklyDemandProjection.getWeek41();
						}
						if(jsonWorkPackageWeeklyDemandProjection.getReservedResourceCountWk41() != 0){
							reservedResourceCount = reservedResourceCount + jsonWorkPackageWeeklyDemandProjection.getReservedResourceCountWk41();
						}
						
						jsonWorkPackageWeeklyDemandProjection.setWeek41(weeklyResourceCount);
						jsonWorkPackageWeeklyDemandProjection.setReservedResourceCountWk41(reservedResourceCount);
						break;
					case 42:
						if(jsonWorkPackageWeeklyDemandProjection.getWeek42() != null){
							weeklyResourceCount = weeklyResourceCount + jsonWorkPackageWeeklyDemandProjection.getWeek42();
						}
						if(jsonWorkPackageWeeklyDemandProjection.getReservedResourceCountWk42() != 0){
							reservedResourceCount = reservedResourceCount + jsonWorkPackageWeeklyDemandProjection.getReservedResourceCountWk42();
						}
						
						jsonWorkPackageWeeklyDemandProjection.setWeek42(weeklyResourceCount);
						jsonWorkPackageWeeklyDemandProjection.setReservedResourceCountWk42(reservedResourceCount);
						break;
					case 43:
						if(jsonWorkPackageWeeklyDemandProjection.getWeek43() != null){
							weeklyResourceCount = weeklyResourceCount + jsonWorkPackageWeeklyDemandProjection.getWeek43();
						}
						if(jsonWorkPackageWeeklyDemandProjection.getReservedResourceCountWk43() != 0){
							reservedResourceCount = reservedResourceCount + jsonWorkPackageWeeklyDemandProjection.getReservedResourceCountWk43();
						}
						
						jsonWorkPackageWeeklyDemandProjection.setWeek43(weeklyResourceCount);	
						jsonWorkPackageWeeklyDemandProjection.setReservedResourceCountWk43(reservedResourceCount);
						break;
					case 44:
						if(jsonWorkPackageWeeklyDemandProjection.getWeek44() != null){
							weeklyResourceCount = weeklyResourceCount + jsonWorkPackageWeeklyDemandProjection.getWeek44();
						}
						if(jsonWorkPackageWeeklyDemandProjection.getReservedResourceCountWk44() != 0){
							reservedResourceCount = reservedResourceCount + jsonWorkPackageWeeklyDemandProjection.getReservedResourceCountWk44();
						}
						
						jsonWorkPackageWeeklyDemandProjection.setWeek44(weeklyResourceCount);	
						jsonWorkPackageWeeklyDemandProjection.setReservedResourceCountWk44(reservedResourceCount);
						break;
					case 45:
						if(jsonWorkPackageWeeklyDemandProjection.getWeek45() != null){
							weeklyResourceCount = weeklyResourceCount + jsonWorkPackageWeeklyDemandProjection.getWeek45();
						}
						if(jsonWorkPackageWeeklyDemandProjection.getReservedResourceCountWk45() != 0){
							reservedResourceCount = reservedResourceCount + jsonWorkPackageWeeklyDemandProjection.getReservedResourceCountWk45();
						}
						
						jsonWorkPackageWeeklyDemandProjection.setWeek45(weeklyResourceCount);	
						jsonWorkPackageWeeklyDemandProjection.setReservedResourceCountWk45(reservedResourceCount);
						break;
					case 46:
						if(jsonWorkPackageWeeklyDemandProjection.getWeek46() != null){
							weeklyResourceCount = weeklyResourceCount + jsonWorkPackageWeeklyDemandProjection.getWeek46();
						}
						if(jsonWorkPackageWeeklyDemandProjection.getReservedResourceCountWk46() != 0){
							reservedResourceCount = reservedResourceCount + jsonWorkPackageWeeklyDemandProjection.getReservedResourceCountWk46();
						}
						
						jsonWorkPackageWeeklyDemandProjection.setWeek46(weeklyResourceCount);
						jsonWorkPackageWeeklyDemandProjection.setReservedResourceCountWk46(reservedResourceCount);
						break;
					case 47:
						if(jsonWorkPackageWeeklyDemandProjection.getWeek47() != null){
							weeklyResourceCount = weeklyResourceCount + jsonWorkPackageWeeklyDemandProjection.getWeek47();
						}
						if(jsonWorkPackageWeeklyDemandProjection.getReservedResourceCountWk47() != 0){
							reservedResourceCount = reservedResourceCount + jsonWorkPackageWeeklyDemandProjection.getReservedResourceCountWk47();
						}
						
						jsonWorkPackageWeeklyDemandProjection.setWeek47(weeklyResourceCount);
						jsonWorkPackageWeeklyDemandProjection.setReservedResourceCountWk47(reservedResourceCount);
						break;
					case 48:
						if(jsonWorkPackageWeeklyDemandProjection.getWeek48() != null){
							weeklyResourceCount = weeklyResourceCount + jsonWorkPackageWeeklyDemandProjection.getWeek48();
						}
						if(jsonWorkPackageWeeklyDemandProjection.getReservedResourceCountWk48() != 0){
							reservedResourceCount = reservedResourceCount + jsonWorkPackageWeeklyDemandProjection.getReservedResourceCountWk48();
						}
						
						jsonWorkPackageWeeklyDemandProjection.setWeek48(weeklyResourceCount);	
						jsonWorkPackageWeeklyDemandProjection.setReservedResourceCountWk48(reservedResourceCount);
						break;
					case 49:
						if(jsonWorkPackageWeeklyDemandProjection.getWeek49() != null){
							weeklyResourceCount = weeklyResourceCount + jsonWorkPackageWeeklyDemandProjection.getWeek49();
						}
						if(jsonWorkPackageWeeklyDemandProjection.getReservedResourceCountWk49() != 0){
							reservedResourceCount = reservedResourceCount + jsonWorkPackageWeeklyDemandProjection.getReservedResourceCountWk49();
						}
						
						jsonWorkPackageWeeklyDemandProjection.setWeek49(weeklyResourceCount);	
						jsonWorkPackageWeeklyDemandProjection.setReservedResourceCountWk49(reservedResourceCount);
						break;
					case 50:
						if(jsonWorkPackageWeeklyDemandProjection.getWeek50() != null){
							weeklyResourceCount = weeklyResourceCount + jsonWorkPackageWeeklyDemandProjection.getWeek50();
						}
						if(jsonWorkPackageWeeklyDemandProjection.getReservedResourceCountWk50() != 0){
							reservedResourceCount = reservedResourceCount + jsonWorkPackageWeeklyDemandProjection.getReservedResourceCountWk50();
						}
						
						jsonWorkPackageWeeklyDemandProjection.setWeek50(weeklyResourceCount);
						jsonWorkPackageWeeklyDemandProjection.setReservedResourceCountWk50(reservedResourceCount);
						break;
					case 51:
						if(jsonWorkPackageWeeklyDemandProjection.getWeek51() != null){
							weeklyResourceCount = weeklyResourceCount + jsonWorkPackageWeeklyDemandProjection.getWeek51();
						}
						if(jsonWorkPackageWeeklyDemandProjection.getReservedResourceCountWk51() != 0){
							reservedResourceCount = reservedResourceCount + jsonWorkPackageWeeklyDemandProjection.getReservedResourceCountWk51();
						}
						
						jsonWorkPackageWeeklyDemandProjection.setWeek51(weeklyResourceCount);
						jsonWorkPackageWeeklyDemandProjection.setReservedResourceCountWk51(reservedResourceCount);
						break;
										
					case 52:
						if(jsonWorkPackageWeeklyDemandProjection.getWeek52() != null){
							weeklyResourceCount = weeklyResourceCount + jsonWorkPackageWeeklyDemandProjection.getWeek52();
						}
						if(jsonWorkPackageWeeklyDemandProjection.getReservedResourceCountWk52() != 0){
							reservedResourceCount = reservedResourceCount + jsonWorkPackageWeeklyDemandProjection.getReservedResourceCountWk52();
						}
						
						jsonWorkPackageWeeklyDemandProjection.setWeek52(weeklyResourceCount);	
						jsonWorkPackageWeeklyDemandProjection.setReservedResourceCountWk52(reservedResourceCount);
						break;
				}
			}
		}
		
		
		return jsonWorkPackageWeeklyDemandProjections;
	}

	
	@Override
	@Transactional
	public List<JsonWorkPackageWeeklyResourceReservation> listWorkpackageWeeklyResourceReservationProjection(Integer workPackageId,Integer reservationWeek,Integer reservationYear,Integer userId) {
		
		List<WeeklyResourceReservationDTO> weeklyResourceReservationDTOs = resourceAvailabilityDAO.listWorkpackageWeeklyResourceReservationProjection( workPackageId, reservationWeek, reservationYear, userId);
		
		
		
		List<JsonWorkPackageWeeklyResourceReservation> jsonWorkPackageWeeklyResourceReservations = new ArrayList<JsonWorkPackageWeeklyResourceReservation>();
		
		WorkPackage workPackage = workPackageDAO.getWorkPackageById(workPackageId);
		
		List<UserList> userList = userListDAO.getUserListByResourcePoolId(3);
		
		
		if(weeklyResourceReservationDTOs == null || weeklyResourceReservationDTOs.size() == 0){
			
			JsonWorkPackageWeeklyResourceReservation jsonWorkPackageWeeklyResourceReservation= new JsonWorkPackageWeeklyResourceReservation();
			
			for(UserList user:userList){
				jsonWorkPackageWeeklyResourceReservation.setWorkPackageId(workPackageId);
				jsonWorkPackageWeeklyResourceReservation.setReservationYear(reservationYear);
				jsonWorkPackageWeeklyResourceReservation.setWorkPackageName(workPackage.getName());
				jsonWorkPackageWeeklyResourceReservation.setUserId(user.getUserId());
				jsonWorkPackageWeeklyResourceReservation.setUserName(user.getLoginId());
				jsonWorkPackageWeeklyResourceReservation.setUserRoleId(user.getUserRoleMaster().getUserRoleId());
				jsonWorkPackageWeeklyResourceReservation.setUserRoleName(user.getUserRoleMaster().getRoleName());
				jsonWorkPackageWeeklyResourceReservations.add(jsonWorkPackageWeeklyResourceReservation);
				
			}
			
			
		}else{
			for (WeeklyResourceReservationDTO weeklyResourceReservationDTO : weeklyResourceReservationDTOs) {
				
				//Check if the json object for this demand object is already present. If yes, update it, else create a new one.
				JsonWorkPackageWeeklyResourceReservation jsonWorkPackageWeeklyResourceReservation = getJsonWorkPackageWeeklyResourceReservation(jsonWorkPackageWeeklyResourceReservations, weeklyResourceReservationDTO); 
				if (jsonWorkPackageWeeklyResourceReservation == null) {
					
					jsonWorkPackageWeeklyResourceReservation = new JsonWorkPackageWeeklyResourceReservation();
					
					jsonWorkPackageWeeklyResourceReservation.setWorkPackageId(weeklyResourceReservationDTO.getWorkPackageId());
					jsonWorkPackageWeeklyResourceReservation.setWorkPackageName(weeklyResourceReservationDTO.getWorkPackageName());
					
					jsonWorkPackageWeeklyResourceReservation.setUserId(weeklyResourceReservationDTO.getUserId());
					jsonWorkPackageWeeklyResourceReservation.setUserName(weeklyResourceReservationDTO.getUserName());
					
					jsonWorkPackageWeeklyResourceReservation.setUserRoleId(weeklyResourceReservationDTO.getUserRoleId());
					jsonWorkPackageWeeklyResourceReservation.setUserRoleName(weeklyResourceReservationDTO.getUserRoleName());
					
					jsonWorkPackageWeeklyResourceReservation.setReservationWeek(weeklyResourceReservationDTO.getReservationWeek());
					jsonWorkPackageWeeklyResourceReservation.setReservationYear(weeklyResourceReservationDTO.getReservationYear());
					
					jsonWorkPackageWeeklyResourceReservations.add(jsonWorkPackageWeeklyResourceReservation);
				}
				
				Integer reservationWeekNo = weeklyResourceReservationDTO.getReservationWeek();
				if(reservationWeekNo == null){
					reservationWeekNo = 0;
				}
				
				float weeklyResourceReservationPercentage = weeklyResourceReservationDTO.getReservationPercentage();
				
				weeklyResourceReservationPercentage= weeklyResourceReservationPercentage/7;
				
				switch (reservationWeekNo) {
					case 1:
						jsonWorkPackageWeeklyResourceReservation.setWeek1(weeklyResourceReservationPercentage);	
						break;
					case 2:
						jsonWorkPackageWeeklyResourceReservation.setWeek2(weeklyResourceReservationPercentage);	
						break;
					case 3:
						jsonWorkPackageWeeklyResourceReservation.setWeek3(weeklyResourceReservationPercentage);	
						break;
					case 4:
						jsonWorkPackageWeeklyResourceReservation.setWeek4(weeklyResourceReservationPercentage);	
						break;
					case 5:
						jsonWorkPackageWeeklyResourceReservation.setWeek5(weeklyResourceReservationPercentage);	
						break;
					case 6:
						jsonWorkPackageWeeklyResourceReservation.setWeek6(weeklyResourceReservationPercentage);	
						break;
					case 7:
						jsonWorkPackageWeeklyResourceReservation.setWeek7(weeklyResourceReservationPercentage);	
						break;
					case 8:
						jsonWorkPackageWeeklyResourceReservation.setWeek8(weeklyResourceReservationPercentage);	
						break;
					case 9:
						jsonWorkPackageWeeklyResourceReservation.setWeek9(weeklyResourceReservationPercentage);	
						break;
					case 10:
						jsonWorkPackageWeeklyResourceReservation.setWeek10(weeklyResourceReservationPercentage);	
						break;
					case 11:
						jsonWorkPackageWeeklyResourceReservation.setWeek11(weeklyResourceReservationPercentage);	
						break;
					case 12:
						jsonWorkPackageWeeklyResourceReservation.setWeek12(weeklyResourceReservationPercentage);	
						break;
					case 13:
						jsonWorkPackageWeeklyResourceReservation.setWeek13(weeklyResourceReservationPercentage);	
						break;
					case 14:
						jsonWorkPackageWeeklyResourceReservation.setWeek14(weeklyResourceReservationPercentage);	
						break;
					case 15:
						jsonWorkPackageWeeklyResourceReservation.setWeek15(weeklyResourceReservationPercentage);	
						break;
					case 16:
						jsonWorkPackageWeeklyResourceReservation.setWeek16(weeklyResourceReservationPercentage);	
						break;
					case 17:
						jsonWorkPackageWeeklyResourceReservation.setWeek17(weeklyResourceReservationPercentage);	
						break;
					case 18:
						jsonWorkPackageWeeklyResourceReservation.setWeek18(weeklyResourceReservationPercentage);	
						break;
					case 19:
						jsonWorkPackageWeeklyResourceReservation.setWeek19(weeklyResourceReservationPercentage);	
						break;
					case 20:
						jsonWorkPackageWeeklyResourceReservation.setWeek20(weeklyResourceReservationPercentage);	
						break;
					case 21:
						jsonWorkPackageWeeklyResourceReservation.setWeek21(weeklyResourceReservationPercentage);	
						break;
					case 22:
						jsonWorkPackageWeeklyResourceReservation.setWeek22(weeklyResourceReservationPercentage);	
						break;
					case 23:
						jsonWorkPackageWeeklyResourceReservation.setWeek23(weeklyResourceReservationPercentage);	
						break;
					case 24:
						jsonWorkPackageWeeklyResourceReservation.setWeek24(weeklyResourceReservationPercentage);	
						break;
					case 25:
						jsonWorkPackageWeeklyResourceReservation.setWeek25(weeklyResourceReservationPercentage);	
						break;
					case 26:
						jsonWorkPackageWeeklyResourceReservation.setWeek26(weeklyResourceReservationPercentage);	
						break;
					case 27:
						jsonWorkPackageWeeklyResourceReservation.setWeek27(weeklyResourceReservationPercentage);	
						break;
					case 28:
						jsonWorkPackageWeeklyResourceReservation.setWeek28(weeklyResourceReservationPercentage);	
						break;
					case 29:
						jsonWorkPackageWeeklyResourceReservation.setWeek29(weeklyResourceReservationPercentage);	
						break;
					case 30:
						jsonWorkPackageWeeklyResourceReservation.setWeek30(weeklyResourceReservationPercentage);	
						break;
					case 31:
						jsonWorkPackageWeeklyResourceReservation.setWeek31(weeklyResourceReservationPercentage);	
						break;
					case 32:
						jsonWorkPackageWeeklyResourceReservation.setWeek32(weeklyResourceReservationPercentage);	
						break;
					case 33:
						jsonWorkPackageWeeklyResourceReservation.setWeek33(weeklyResourceReservationPercentage);	
						break;
					case 34:
						jsonWorkPackageWeeklyResourceReservation.setWeek34(weeklyResourceReservationPercentage);	
						break;
					case 35:
						jsonWorkPackageWeeklyResourceReservation.setWeek35(weeklyResourceReservationPercentage);	
						break;
					case 36:
						jsonWorkPackageWeeklyResourceReservation.setWeek36(weeklyResourceReservationPercentage);	
						break;
					case 37:
						jsonWorkPackageWeeklyResourceReservation.setWeek37(weeklyResourceReservationPercentage);	
						break;
					case 38:
						jsonWorkPackageWeeklyResourceReservation.setWeek38(weeklyResourceReservationPercentage);	
						break;
					case 39:
						jsonWorkPackageWeeklyResourceReservation.setWeek39(weeklyResourceReservationPercentage);	
						break;
					case 40:
						jsonWorkPackageWeeklyResourceReservation.setWeek40(weeklyResourceReservationPercentage);	
						break;
					case 41:
						jsonWorkPackageWeeklyResourceReservation.setWeek41(weeklyResourceReservationPercentage);	
						break;
					case 42:
						jsonWorkPackageWeeklyResourceReservation.setWeek42(weeklyResourceReservationPercentage);	
						break;
					case 43:
						jsonWorkPackageWeeklyResourceReservation.setWeek43(weeklyResourceReservationPercentage);	
						break;
					case 44:
						jsonWorkPackageWeeklyResourceReservation.setWeek44(weeklyResourceReservationPercentage);	
						break;
					case 45:
						jsonWorkPackageWeeklyResourceReservation.setWeek45(weeklyResourceReservationPercentage);	
						break;
					case 46:
						jsonWorkPackageWeeklyResourceReservation.setWeek46(weeklyResourceReservationPercentage);	
						break;
					case 47:
						jsonWorkPackageWeeklyResourceReservation.setWeek47(weeklyResourceReservationPercentage);	
						break;
					case 48:
						jsonWorkPackageWeeklyResourceReservation.setWeek48(weeklyResourceReservationPercentage);	
						break;
					case 49:
						jsonWorkPackageWeeklyResourceReservation.setWeek49(weeklyResourceReservationPercentage);	
						break;
					case 50:
						jsonWorkPackageWeeklyResourceReservation.setWeek50(weeklyResourceReservationPercentage);	
						break;
					case 51:
						jsonWorkPackageWeeklyResourceReservation.setWeek51(weeklyResourceReservationPercentage);	
						break;
										
					case 52:
						jsonWorkPackageWeeklyResourceReservation.setWeek52(weeklyResourceReservationPercentage);	
						break;
				}
			}
		}
		
		
		return jsonWorkPackageWeeklyResourceReservations;
	}
	
	private JsonWorkPackageWeeklyDemandProjection getJsonWorkPackageWeeklyDemandProjection(List<JsonWorkPackageWeeklyDemandProjection> jsonWorkPackageWeeklyDemandProjections, WeeklyResourceDemandDTO weeklyResourceDemandDTO) {
		
		for (JsonWorkPackageWeeklyDemandProjection jsonWorkPackageWeeklyDemandProjection : jsonWorkPackageWeeklyDemandProjections) {
			
			if ((jsonWorkPackageWeeklyDemandProjection.getWorkPackageId() == weeklyResourceDemandDTO.getWorkPackageId()) &&
					(jsonWorkPackageWeeklyDemandProjection.getShiftId() == weeklyResourceDemandDTO.getShiftId()) &&
					(jsonWorkPackageWeeklyDemandProjection.getUserRoleId() == weeklyResourceDemandDTO.getRoleId()) &&
					(jsonWorkPackageWeeklyDemandProjection.getSkillId() == weeklyResourceDemandDTO.getSkillId()) &&
					(jsonWorkPackageWeeklyDemandProjection.getUserTypeId() == weeklyResourceDemandDTO.getUserTypeId())
					) {
				return jsonWorkPackageWeeklyDemandProjection;
			}
		}
		return null;
	}

private JsonWorkPackageWeeklyDemandProjection getJsonWorkPackageWeeklyDemandProjectionTestFactoryLabLevel(List<JsonWorkPackageWeeklyDemandProjection> jsonWorkPackageWeeklyDemandProjections, WeeklyResourceDemandDTO weeklyResourceDemandDTO) {
		
		for (JsonWorkPackageWeeklyDemandProjection jsonWorkPackageWeeklyDemandProjection : jsonWorkPackageWeeklyDemandProjections) {
			
			if ((jsonWorkPackageWeeklyDemandProjection.getWorkPackageId() == weeklyResourceDemandDTO.getWorkPackageId()) &&
					(jsonWorkPackageWeeklyDemandProjection.getShiftId() == weeklyResourceDemandDTO.getShiftId()) &&
					(jsonWorkPackageWeeklyDemandProjection.getUserRoleId() == weeklyResourceDemandDTO.getRoleId()) &&
					(jsonWorkPackageWeeklyDemandProjection.getSkillId() == weeklyResourceDemandDTO.getSkillId()) &&
					(jsonWorkPackageWeeklyDemandProjection.getTestFactoryId() == weeklyResourceDemandDTO.getTestFactoryId()) &&
					(jsonWorkPackageWeeklyDemandProjection.getUserTypeId() == weeklyResourceDemandDTO.getUserTypeId())
					) {
				return jsonWorkPackageWeeklyDemandProjection;
			}
		}
		return null;
	}

	
private JsonWorkPackageWeeklyResourceReservation getJsonWorkPackageWeeklyResourceReservation(List<JsonWorkPackageWeeklyResourceReservation> jsonWorkPackageWeeklyResourceReservations, WeeklyResourceReservationDTO weeklyResourceReservationDTO) {
		
		for (JsonWorkPackageWeeklyResourceReservation jsonWorkPackageWeeklyResourceReservation : jsonWorkPackageWeeklyResourceReservations) {
			
			if ((jsonWorkPackageWeeklyResourceReservation.getWorkPackageId() == weeklyResourceReservationDTO.getWorkPackageId()) &&
					(jsonWorkPackageWeeklyResourceReservation.getUserRoleId() == weeklyResourceReservationDTO.getUserRoleId())
					) {
				return jsonWorkPackageWeeklyResourceReservation;
			}
		}
		return null;
	}

private JsonWorkPackageWeeklyResourceReservation getJsonWorkPackageWeeklyResourceReservationForResourcePoolId(List<JsonWorkPackageWeeklyResourceReservation> jsonWorkPackageWeeklyResourceReservations, WeeklyResourceReservationDTO weeklyResourceReservationDTO) {
	
	for (JsonWorkPackageWeeklyResourceReservation jsonWorkPackageWeeklyResourceReservation : jsonWorkPackageWeeklyResourceReservations) {
		
		if (jsonWorkPackageWeeklyResourceReservation.getUserId().equals(weeklyResourceReservationDTO.getUserId()) && jsonWorkPackageWeeklyResourceReservation.getResourcePoolId().equals(weeklyResourceReservationDTO.getResourcePoolId()) ) {
			return jsonWorkPackageWeeklyResourceReservation;
		}
	}
	return null;
}

	@Override
	public List<WorkPackageDemandProjectionStatisticsDTO> listWorkpackageDemandProjectionByRole(
			Integer workPackageId, Integer shiftId, Date reservationDate) {
		return resourceAvailabilityDAO.listWorkpackageDemandProjectionByRole(workPackageId, shiftId,reservationDate);
		
	}
	
	
	@Override
	@Transactional
	public List<ResourceAvailability> listAvaiablitybyDate(
			Integer testFactoryLabId, Integer testFactoryId, Integer productId,
			Integer workPackageId, Integer shiftId, Date workDate, Integer jtStartIndex, Integer jtPageSize) {
		return resourceAvailabilityDAO.listAvaiablitybyDate(testFactoryLabId, testFactoryId, productId, workPackageId, shiftId, workDate, jtStartIndex, jtPageSize);
	}

	@Override
	@Transactional
	public List<TestFactoryResourceReservation> listTestFactoryResourceReservation(
			Integer testFactoryLabId, Integer testFactoryId, Integer productId,
			Integer workPackageId, Integer shiftId, Date workDate, Integer jtStartIndex, Integer jtPageSize) {
		return resourceAvailabilityDAO.listTestFactoryResourceReservation(testFactoryLabId, testFactoryId, productId, workPackageId, shiftId, workDate, jtStartIndex, jtPageSize);
	}

	@Override
	public List<TestFactoryResourceReservation> getTestFactoryResourceReservation(
			int workPackageId, int weekNo) {
		Date startDate = DateUtility.getDateForDayOfWeek(weekNo,0);
		Date endDate = DateUtility.getDateForDayOfWeek(weekNo,6);
		return testFactoryResourceReservationDao.getTestFactoryResourceReservation(workPackageId,startDate,endDate);
	}

	
	@Override
	@Transactional
	public List<ShiftTypeMaster> listShiftTypeMaster() {
		return resourceAvailabilityDAO.listShiftTypeMaster();
	}

	@Override
	public ShiftTypeMaster getShiftTypeMasterById(Integer shiftTypeId) {
		return resourceAvailabilityDAO.getShiftTypeMasterById(shiftTypeId);
	}

	

	@Override
	@Transactional
	public List<TestfactoryResourcePool> testFactoryResourcePoolListbyTFactoryId(Integer testFactoryId) {	
		return resourceAvailabilityDAO.testFactoryResourcePoolListbyTFactoryId(testFactoryId);
	}

	@Override
	public List<UserList> getExistingBlockedResourcesOfWorkPackage(
			int workPackageId, int shiftId, Date resourceDemandForDate) {
		return testFactoryResourceReservationDao.getListOfResourceBlockedForWorkpackage(workPackageId, shiftId,resourceDemandForDate);
	}

	@Override
	@Transactional
	public List<UserList> getExistingBlockedResourcesOfWorkPackageForWeek(Integer workpackageId, Integer shiftId,Integer  workWeek,Integer workYear) {
		return testFactoryResourceReservationDao.getListOfResourceBlockedForWorkpackageWeekly(workpackageId, shiftId,workWeek,workYear);
	}
	
	
	@Override
	@Transactional
	public List<ResourceAttendanceSummaryDTO> listResourceAttendanceSummary(Integer resourcePoolId, Date workDate, Integer shiftId) {
		return resourceAvailabilityDAO.listResourceAttendanceSummary(resourcePoolId,workDate,shiftId);
	}

	@Override
	@Transactional
	public TestFactoryResourceReservation saveReservedResource(Integer reservedUserId,Date dtResourceBlockedForDate, String workPackageId, String shiftId, String loggedInUserId) {
		TestFactoryResourceReservation testFactoryResourceReservation = null;
		String result = "";
		try {
			WorkPackage workPackage = workPackageDAO.getWorkPackageByIdWithMinimalnitialization(Integer.parseInt(workPackageId));
			WorkShiftMaster workShiftMaster = workPackageDAO.getWorkShiftById(Integer.parseInt(shiftId));
			UserList reservedUser = userListDAO.getByUserId(reservedUserId);
			UserList loggedInUser = userListDAO.getByUserId(Integer.parseInt(loggedInUserId));
			boolean isResourceAlreadyBlockedByAnotherManagerForSameWp = testFactoryResourceReservationDao.isResourceBlockedAlready(Integer.parseInt(workPackageId), Integer.parseInt(shiftId), dtResourceBlockedForDate, reservedUser.getUserId(),1);
			boolean isResourceAlreadyBlockedByAnotherManagerForDiffWp = testFactoryResourceReservationDao.isResourceBlockedAlready(Integer.parseInt(workPackageId), Integer.parseInt(shiftId), dtResourceBlockedForDate, reservedUser.getUserId(),0);
			if(isResourceAlreadyBlockedByAnotherManagerForSameWp || isResourceAlreadyBlockedByAnotherManagerForDiffWp){
				result = "Resource "+ reservedUser.getLoginId() +" is already blocked by another manager for date: "+dtResourceBlockedForDate;
			}else{
				
				//TODO : Check for race conditions to prevent duplicate reservation addition
				testFactoryResourceReservation = new TestFactoryResourceReservation();
				testFactoryResourceReservation.setBlockedUser(reservedUser);
				testFactoryResourceReservation.setWorkPackage(workPackage);
				testFactoryResourceReservation.setShift(workShiftMaster);
				testFactoryResourceReservation.setReservationDate(dtResourceBlockedForDate);
				testFactoryResourceReservation.setReservationActionUser(loggedInUser);
				testFactoryResourceReservation.setReservationActionDate(new Date(System.currentTimeMillis()));
				Integer isRecordAdded = testFactoryResourceReservationDao.saveBlockedResource(testFactoryResourceReservation);
				if(!isRecordAdded.equals(0)){
					// Record added successfully. Notify users through mail.
					result="";
					return testFactoryResourceReservation;
				}else{
					result="Reservation Failed";
					testFactoryResourceReservation = null;
					return null;
				}
			}
		} catch (Exception e) {
			log.error("error occured while saving resource reservation",e);
			result = e.getMessage();
		}
		return testFactoryResourceReservation;
		
	}

	@Override
	@Transactional
	public TestFactoryResourceReservation removeReservationForResource(
			Integer unBlockedUserId, Date dtResourceBlockedForDate,
			String workPackageId, String shiftId, String loggedInUserId) {
		TestFactoryResourceReservation testFactoryResourceReservation = null;
		try {
			UserList unReservedUser = userListDAO.getByUserId(unBlockedUserId);
				testFactoryResourceReservation = testFactoryResourceReservationDao.getTestFactoryResourceReservation(Integer.parseInt(workPackageId), Integer.parseInt(shiftId), dtResourceBlockedForDate, unReservedUser.getUserId(), Integer.parseInt(loggedInUserId), 0);
				if(testFactoryResourceReservation != null){
					testFactoryResourceReservationDao.removeUnblockedResources(testFactoryResourceReservation);
					log.info("Cancelled Reservation for user : "+unReservedUser.getLoginId()+" for shift : "+shiftId+" for workPackage Id: "+workPackageId+"  for Date: "+dtResourceBlockedForDate);
				}else{
					log.info("Reservation cancellation not needed (as no prior reservation exists) for user : "+unReservedUser.getLoginId()+" for shift : "+shiftId+" for workPackage Id: "+workPackageId+"  for Date: "+dtResourceBlockedForDate);
				}
		} catch (Exception e) {
			log.error("error occured while removing resource reservation",e);
		}
		return testFactoryResourceReservation;
	}
	
	@Override
	public List<JsonResourceAvailability> listForBookingAndReservedStatus(int resourceId, int weekNo) {

		Date startDate = DateUtility.getDateForDayOfWeek(weekNo,0);
		log.info("startDate="+startDate);
		Date endDate = DateUtility.getDateForDayOfWeek(weekNo,6);
		log.info("endDate="+endDate);
		UserList user=userListDAO.getByUserId(resourceId);
		List<ShiftTypeMaster> shiftTypeMasterList =  resourceAvailabilityDAO.listShiftTypeMaster();
		Map<String, JsonResourceAvailability> jsonResourceAvailabilityShiftMap = new HashMap<String, JsonResourceAvailability>();
		JsonResourceAvailability jsonResourceAvailabilityShift = null;
		int counter = 1;
		for(ShiftTypeMaster shiftType : shiftTypeMasterList){

			jsonResourceAvailabilityShift = new JsonResourceAvailability();
			jsonResourceAvailabilityShift.setResourceAvailabilityId(counter++);
			jsonResourceAvailabilityShift.setResourceId(resourceId);
			jsonResourceAvailabilityShift.setShiftId(shiftType.getShiftTypeId());
			jsonResourceAvailabilityShift.setShiftName(shiftType.getShiftName());
			jsonResourceAvailabilityShift.setWeekNo(weekNo);
			
			jsonResourceAvailabilityShiftMap.put(jsonResourceAvailabilityShift.getShiftName(), jsonResourceAvailabilityShift);
			
		}
		
		List<ResourceAvailability> resourceAvailabilityList =  resourceAvailabilityDAO.listResourceAvailability(resourceId, startDate, endDate);
		List<TestFactoryResourceReservation> testFactoryResourceReservationList =  testFactoryResourceReservationDao.listTestFactoryResourceReservationByUserIdStartDateEndDate(resourceId, startDate, endDate);
		
		List<JsonResourceAvailability> jsonWorkPackageDemandProjections = new ArrayList<JsonResourceAvailability>();
	
		if(resourceAvailabilityList== null || resourceAvailabilityList.isEmpty()){
			
		} 
		else if(testFactoryResourceReservationList==null && testFactoryResourceReservationList.isEmpty()){
			
		}
		else{
			String status = "";
			String valueforToolTip="";
			for (ResourceAvailability resourceAvailability : resourceAvailabilityList) {
			for(TestFactoryResourceReservation testFactoryResourceReservation:testFactoryResourceReservationList){
			log.info("Date:"+DateUtility.sdfDateformatWithOutTime(resourceAvailability.getWorkDate())+"Date2:"+DateUtility.sdfDateformatWithOutTime(testFactoryResourceReservation.getReservationDate()));
			if(resourceAvailability.getWorkDate().compareTo(testFactoryResourceReservation.getReservationDate())==0){
				if(resourceAvailability.getShiftTypeMaster().getShiftTypeId()==testFactoryResourceReservation.getShift().getShiftType().getShiftTypeId()){
					if(resourceAvailability.getBookForShift()!=null && resourceAvailability.getBookForShift()!=0){
							 valueforToolTip = testFactoryResourceReservation.getWorkPackage().getName()+" / "+testFactoryResourceReservation.getShift().getShiftName();
							
							status="B,R";
						}else{
							status="B";
						}
				}
				
			}
		}
			if(status.equals("")){
				if(resourceAvailability.getWorkDate()!=null){
					if(resourceAvailability.getBookForShift()!=null && resourceAvailability.getBookForShift()!=0){
						status="B";
					}else{
						status="-";
					}
				}
			}
			JsonResourceAvailability shiftJsonResourceAvailability = jsonResourceAvailabilityShiftMap.get(resourceAvailability.getShiftTypeMaster().getShiftName());
			log.info("ShiftName()-->"+resourceAvailability.getShiftTypeMaster().getShiftName()+"+Date()-->"+resourceAvailability.getWorkDate()+"+status-->"+status+" + tootip-->"+valueforToolTip);
			if (shiftJsonResourceAvailability != null) {
				int dayOfWeek = DateUtility.getDayOfWeek(resourceAvailability.getWorkDate());
				shiftJsonResourceAvailability = loadReservedResource(shiftJsonResourceAvailability, status, dayOfWeek,valueforToolTip);
			
				jsonResourceAvailabilityShiftMap.put(shiftJsonResourceAvailability.getShiftName(), shiftJsonResourceAvailability);
			}
			 status = "";
			 valueforToolTip="";
			}
		}
		
		
		jsonWorkPackageDemandProjections = new ArrayList<JsonResourceAvailability>(jsonResourceAvailabilityShiftMap.values());
		return jsonWorkPackageDemandProjections;
	}
	
	private JsonResourceAvailability loadReservedResource(JsonResourceAvailability jsonResourceAvailabilityShift, String status, int dayOfWeek,String valueforToolTip) {
		log.info("dayOfWeek="+dayOfWeek);
		log.info("day of the week"+DateUtility.getDateNamesOfWeek(dayOfWeek));
		log.info("status="+status);
		log.info("valueforToolTip="+valueforToolTip);
		
		switch(dayOfWeek) {
			// Please dont change this ordering of case numbers
			case 1:
				jsonResourceAvailabilityShift.setDay7(status);
				if(valueforToolTip!=""){
					jsonResourceAvailabilityShift.setDay7ValueforToolTip(valueforToolTip);
				}
				break;
				
			case 2:  // Monday
				jsonResourceAvailabilityShift.setDay1(status);
				if(valueforToolTip!=""){
					jsonResourceAvailabilityShift.setDay1ValueforToolTip(valueforToolTip);
				}
				break;
			case 3:
				jsonResourceAvailabilityShift.setDay2(status);
				if(valueforToolTip!=""){
					jsonResourceAvailabilityShift.setDay2ValueforToolTip(valueforToolTip);
				}
				break;
			case 4:
				jsonResourceAvailabilityShift.setDay3(status);
				if(valueforToolTip!=""){
					jsonResourceAvailabilityShift.setDay3ValueforToolTip(valueforToolTip);
				}
				break;
			case 5:
				jsonResourceAvailabilityShift.setDay4(status);
				if(valueforToolTip!=""){
					jsonResourceAvailabilityShift.setDay4ValueforToolTip(valueforToolTip);
				}
				break;
			case 6:
				jsonResourceAvailabilityShift.setDay5(status);
				if(valueforToolTip!=""){
					jsonResourceAvailabilityShift.setDay5ValueforToolTip(valueforToolTip);
				}
				break;
			case 7:
				jsonResourceAvailabilityShift.setDay6(status);
				if(valueforToolTip!=""){
					jsonResourceAvailabilityShift.setDay6ValueforToolTip(valueforToolTip);
				}
				break;
		}
	
		return jsonResourceAvailabilityShift;
	}

	@Override
	@Transactional
	public List<ResourceAttendanceSummaryDTO> listResourceAvailabiltySummary(Integer resourcePoolId, Date workDate, Integer shiftId) {
		List<ResourceAttendanceSummaryDTO> resourceAvailabilityDTO = resourceAvailabilityDAO.listResourceAvailabilitySummary(resourcePoolId,workDate,shiftId);
		List<ResourceAttendanceSummaryDTO> resourceDemandDTO = resourceAvailabilityDAO.listWorkpackageDemandSummary(resourcePoolId,workDate,shiftId);
		
		for (ResourceAttendanceSummaryDTO resourceAvailability : resourceAvailabilityDTO) {
			for (ResourceAttendanceSummaryDTO resourceDemand : resourceDemandDTO) {
				if(resourceDemand.getWorkDate() != null && resourceAvailability.getWorkDate() != null && resourceAvailability.getWorkDate().equals(resourceDemand.getWorkDate()))
				{
					if (resourceAvailability.getShiftTypeId() == resourceDemand.getShiftTypeId())
					{
						resourceAvailability.setDemandCount(resourceDemand.getDemandCount());
						resourceAvailability.setGapCount(resourceAvailability.getAvailableCount() - resourceAvailability.getDemandCount());
					}
				}
				
			}
			
		}
		return resourceAvailabilityDTO;
	}

	@Override
	@Transactional
	public List<JsonResourceAttendance> listResourcesReliable(Integer resourcePoolId, Date startDate, Date endDate, Integer userId) {
		log.info("listResourcesReliable ()   >>>>  "+"resourcePoolId::: "+resourcePoolId+"    startDate: "+ startDate+"      endDate: "+endDate );
		List<JsonResourceAttendance> jsonResourceReliableList = new ArrayList<JsonResourceAttendance>();
		
		List<ResourceAttendanceSummaryDTO> resourceReliableList = resourceAvailabilityDAO.listResourceReliable(resourcePoolId, startDate, endDate, userId);
		List<ResourceAttendanceSummaryDTO> resourceReliablilityList = new ArrayList<ResourceAttendanceSummaryDTO>();
		
		for (ResourceAttendanceSummaryDTO resourceAttendanceSummaryDTO : resourceReliableList) {
			ResourceAttendanceSummaryDTO resourceTotalBookings =  resourceAvailabilityDAO.listResourceReliableTotalBooking(resourceAttendanceSummaryDTO.getUserId(), startDate, endDate);
			if(resourceTotalBookings != null){
				if(resourceAttendanceSummaryDTO.getUserId().equals(resourceTotalBookings.getUserId())){
					resourceAttendanceSummaryDTO.setTotalBookings(resourceTotalBookings.getTotalBookings());
				}
			}
			List<TestFactoryResourceReservation> testFactoryResourceReservationList = resourceAvailabilityDAO.listResourceReliableTotalBookingSummary(resourceAttendanceSummaryDTO.getUserId(), startDate, endDate);
			if (testFactoryResourceReservationList!=null && !testFactoryResourceReservationList.isEmpty()) {
				int counterForShowUp = 0;
				int counterForOnTime=0;
				for (TestFactoryResourceReservation testFactoryResourceReservation : testFactoryResourceReservationList) {
						ShiftTypeMaster shiftType = workShiftMasterDAO.getShiftTypeByShiftId(testFactoryResourceReservation.getShift().getShiftId());
						ResourceAvailability resourceAvailablity =  resourceAvailabilityDAO.listShowUpandOnTime(resourceAttendanceSummaryDTO.getUserId(), testFactoryResourceReservation.getReservationDate(), shiftType.getShiftTypeId());
						
						if(resourceAvailablity != null){
							if(resourceAvailablity.getShiftAttendance() != null && resourceAvailablity.getShiftAttendance() == 1){
								counterForShowUp++;
							}
						ActualShift actualShift = workShiftMasterDAO.listActualShiftbyshiftId(testFactoryResourceReservation.getShift().getShiftId(), testFactoryResourceReservation.getReservationDate());
						if(actualShift != null){
							if(actualShift.getStartTime() != null){
								Long timeDiff = Math.abs(resourceAvailablity.getAttendanceCheckInTime().getTime() - actualShift.getStartTime().getTime());
								if(timeDiff < 300000){
									counterForOnTime++;
								}
							}
						}
					}
				}
				resourceAttendanceSummaryDTO.setShowUp(counterForShowUp);
				resourceAttendanceSummaryDTO.setOnTime(counterForOnTime);
			}
			resourceReliablilityList.add(resourceAttendanceSummaryDTO);
		}
		
		if(resourceReliablilityList != null && resourceReliablilityList.size()>0){
			for (ResourceAttendanceSummaryDTO resourceAttendanceSummaryDTO : resourceReliablilityList) {
				JsonResourceAttendance jsonResourceAttendance = new JsonResourceAttendance(resourceAttendanceSummaryDTO);
				jsonResourceAttendance.setUserId(resourceAttendanceSummaryDTO.getUserId());
				jsonResourceAttendance.setUserName(resourceAttendanceSummaryDTO.getUserName());
				jsonResourceAttendance.setResourcePoolName(resourceAttendanceSummaryDTO.getResourcePoolName());
				jsonResourceAttendance.setRegisteredCompanyName(resourceAttendanceSummaryDTO.getVendorName());
				jsonResourceAttendance.setRoleName(resourceAttendanceSummaryDTO.getRoleName());
				jsonResourceReliableList.add(jsonResourceAttendance);
			}
		}
		return jsonResourceReliableList;
	}
	
	@Override
	@Transactional
	public List<JsonResourceAvailabilityDetails> getResourceAvailability(int workPackageId,int shiftId, int shiftTypeId, Date getAvailabilityForDate) {
		List<ResourceAvailability> listResourceAvailability = null;
		List<TestFactoryResourceReservation> listOfResourceReserved = null;
		List<JsonResourceAvailabilityDetails> listJsonResourceAvailability = new ArrayList<JsonResourceAvailabilityDetails>();
		List<TestfactoryResourcePool> listOfResourcePools = null;
		TestFactory testFactory = null;
		List<Integer> resourcePoolIds = new ArrayList<Integer>();
		try {
			if(workPackageId != 0){
				WorkPackage workPackage = workPackageDAO.getWorkPackageById(workPackageId);
				testFactory= testFactoryDao.getTestFactoryById(workPackage.getProductBuild().getProductVersion().getProductMaster().getTestFactory().getTestFactoryId());
				listOfResourcePools = testFactoryDao.getResourcePoolListbyTestFactoryId(testFactory.getTestFactoryId());
			}
			if(listOfResourcePools != null && listOfResourcePools.size()>0){
				for (TestfactoryResourcePool testfactoryResourcePool : listOfResourcePools) {
					resourcePoolIds.add(testfactoryResourcePool.getResourcePoolId());
				}
				listResourceAvailability = resourceAvailabilityDAO.getResourceAvailability(shiftTypeId, getAvailabilityForDate, resourcePoolIds);
				log.info("listResourceAvailability: "+listResourceAvailability.size());
				listOfResourceReserved = testFactoryResourceReservationDao.getReservedResourcesOfWorkpackage(0, shiftId,getAvailabilityForDate);
				log.info("listOfResourceReserved size:::::: "+listOfResourceReserved.size());
				   if(listResourceAvailability != null && listResourceAvailability.size() > 0) {
						if(listOfResourceReserved != null && listOfResourceReserved.size()>0){
							for (TestFactoryResourceReservation testFactoryResourceReservation : listOfResourceReserved) {
								for (Iterator<ResourceAvailability> iter = listResourceAvailability.iterator(); iter.hasNext();) {
									ResourceAvailability resAvail = iter.next();
								      if (resAvail.getResource().getUserId().equals(testFactoryResourceReservation.getBlockedUser().getUserId())) {
								    	  log.info("Removed user Id : "+resAvail.getResource().getUserId() + "  user Login Id: "+resAvail.getResource().getLoginId());
								    	  iter.remove();
								      }
								  }
							  }
						 }
				   }
				   
				   log.info("After Removal listResourceAvailability: "+listResourceAvailability.size());
				   for (ResourceAvailability resAvailability : listResourceAvailability) {
						JsonResourceAvailabilityDetails jsonResourceAvailability = new JsonResourceAvailabilityDetails(resAvailability);
						listJsonResourceAvailability.add(jsonResourceAvailability);
					}
			}
		} catch (Exception e) {
			log.error("Error occured while getting the available resources details for  date: "+getAvailabilityForDate+ " and shift Type Id: "+shiftTypeId,e);
		}
		return listJsonResourceAvailability;
	}

	@Override
	@Transactional
	public List<JsonUserList> getResourcesForReservation(int workPackageId,
			int shiftId, Date blockResourceForDate,
			String availabilityTypeFilter) {
		log.info("in getAvailableUsersForBlocking() ***"+shiftId);
		List<JsonUserList> jsonUserList = null;
		List<UserList> otherProductCoreResources = null;
		List<UserList> otherTestFactoryCoreResources = null;
		List<UserList> productCoreResources = null;
		List<UserList> testFactoryCoreResources = null;
		Integer productId = 0;
		
		ShiftTypeMaster shiftTypeOfWorkShift = resourceAvailabilityDAO.getShiftTypeIdFromWorkShiftId(shiftId);
		List<UserList> availableUserList = new ArrayList<UserList>();
		List<UserList> workPacakgeReservedResources = new ArrayList<UserList>();
		WorkShiftMaster workShift = workShiftMasterDAO.listWorkShiftsByshiftId(shiftId);
		
		
		TestFactory testFactory = null;
		if(workPackageId!=-1){
			WorkPackage workPackage = workPackageDAO.getWorkPackageById(workPackageId);
			testFactory= testFactoryDao.getTestFactoryById(workPackage.getProductBuild().getProductVersion().getProductMaster().getTestFactory().getTestFactoryId());
			productId = workPackageDAO.getProductIdByWorkpackage(workPackageId);
		}else{
			testFactory=workShift.getTestFactory();
		}
		
		List<TestfactoryResourcePool> listOfResourcePools = testFactoryDao.getResourcePoolListbyTestFactoryId(testFactory.getTestFactoryId());
		if(listOfResourcePools == null){
			
		}else if(listOfResourcePools != null && listOfResourcePools.size()>0){
			for (TestfactoryResourcePool testfactoryResourcePool : listOfResourcePools) {
				List<UserList> subListOfavailableUnReservedUsers = null;
				subListOfavailableUnReservedUsers = testFactoryResourceReservationDao.getResourcesAvailability(shiftTypeOfWorkShift.getShiftTypeId(), blockResourceForDate, availabilityTypeFilter,testfactoryResourcePool.getResourcePoolId());
				if(availableUserList.size() == 0){
					availableUserList = subListOfavailableUnReservedUsers;
				}else{
					availableUserList.addAll(subListOfavailableUnReservedUsers);
				}
			}
		
		}
		
		
		
		List<UserWeekUtilisedTimeDTO> listOfTimeSheetEntries = timeSheetManagementDAO.getTimesheetEntriesForWorkPackage(workPackageId, workShift, blockResourceForDate);
		List<UserWeekUtilisedTimeDTO> listOfReservedShifts = timeSheetManagementDAO.getReservedShiftsOfUser(workPackageId, shiftId, blockResourceForDate);
		log.info("listOfTimeSheetEntries size: "+listOfTimeSheetEntries.size());
		
		List<UserList> blockedUsers = testFactoryResourceReservationDao.getBlockedResources(blockResourceForDate, shiftId);
		if(productId != 0){
			otherProductCoreResources = testFactoryResourceReservationDao.getOtherProductCoreResources(productId,blockResourceForDate,true);
			productCoreResources = testFactoryResourceReservationDao.getOtherProductCoreResources(productId,blockResourceForDate,false);
		}
		
		
		if(testFactory != null){
			otherTestFactoryCoreResources = testFactoryResourceReservationDao.getOtherTestFactoryCoreResources(testFactory.getTestFactoryId(),blockResourceForDate,true);
			testFactoryCoreResources = testFactoryResourceReservationDao.getOtherTestFactoryCoreResources(testFactory.getTestFactoryId(),blockResourceForDate,false);
		}
		
		
		int index = -1;
		for (UserList blockedUser : blockedUsers) {
			index = availableUserList.indexOf(blockedUser);
			if (index >= 0) {
				availableUserList.remove(index);
			}
		}
		
		// Remove other product core resources
		if(otherProductCoreResources != null && otherProductCoreResources.size()>0){
			for (UserList otherProductCoreResource : otherProductCoreResources) {
				index = availableUserList.indexOf(otherProductCoreResource);
				if (index >= 0) {
					availableUserList.remove(index);
				}
			}
		}
		
		
		// Remove other Test Factory core resources
		if(otherTestFactoryCoreResources != null && otherTestFactoryCoreResources.size()>0){
			for (UserList otherTestFactoryCoreResource : otherTestFactoryCoreResources) {
				index = availableUserList.indexOf(otherTestFactoryCoreResource);
				if (index >= 0) {
					availableUserList.remove(index);
				}
			}
		}
		
		if(availableUserList != null && availableUserList.size()>0){
			jsonUserList = new ArrayList<JsonUserList>();
			for (UserList user : availableUserList) {
				JsonUserList jsonUser = new JsonUserList();
				jsonUser.setLoginId(user.getLoginId());
				jsonUser.setUserId(user.getUserId());
				jsonUser.setUserRoleId(user.getUserRoleMaster().getUserRoleId());
				jsonUser.setUserRoleLabel(user.getUserRoleMaster().getRoleLabel());
				jsonUser.setImageURI(user.getImageURI());
				jsonUser.setReserve(new Integer(0));
				Set<UserSkills> setOfUserSkills = user.getUserSkills();
				StringBuffer sb = jsonUser.getSkillsOfUser(setOfUserSkills);
				if(sb != null){
					jsonUser.setSkillName(sb);
				}
				jsonUserList.add(jsonUser);
			}
			log.info("Available jsonUserList size: "+jsonUserList.size());
			if(jsonUserList != null && jsonUserList.size()>0){
				//compare and set DTO's time sheet hours of user
				for (JsonUserList jsonUser : jsonUserList) {
					for (UserWeekUtilisedTimeDTO userWeekUtilisedTimeDTO : listOfTimeSheetEntries) {
						if(userWeekUtilisedTimeDTO.getUserId() == jsonUser.getUserId()){
							Integer totalTimeInMinutes = DateUtility.convertTimeInMinutes(userWeekUtilisedTimeDTO.getTimeSheetTotalHours(),userWeekUtilisedTimeDTO.getTimeSheetTotalMins());
							Integer remainingTimeSheetInMinutes = 2400-totalTimeInMinutes;
							userWeekUtilisedTimeDTO.setTimeSheetDuration(DateUtility.convertTimeInHoursMinutes(0,remainingTimeSheetInMinutes));
							jsonUser.setTimeSheetHours(userWeekUtilisedTimeDTO.getTimeSheetDuration());
							break;
						}else{
							continue;
						}
					}
				}
			}
			
			if(jsonUserList != null && jsonUserList.size()>0){
				//compare and set DTO's time sheet hours of user
				for (JsonUserList jsonUser : jsonUserList) {
					for (UserWeekUtilisedTimeDTO userWeekUtilisedTimeDTO : listOfReservedShifts) {
						if(userWeekUtilisedTimeDTO.getUserId() == jsonUser.getUserId()){
							long totalTime = 40-(userWeekUtilisedTimeDTO.getShiftBookingCount()*8);
							userWeekUtilisedTimeDTO.setShiftBookingDuration(String.valueOf(totalTime));
							jsonUser.setBookedHrs(totalTime);
							break;
						}else{
							continue;
						}
					}
				}
			}
		}
		
		if(jsonUserList != null && jsonUserList.size()>0){
			for (JsonUserList jsonUser : jsonUserList) {
				 ResourceAvailability resAvailability = resourceAvailabilityDAO.getResourceAvailability(jsonUser.getUserId(), blockResourceForDate, shiftTypeOfWorkShift.getShiftTypeId());
				 if(resAvailability != null){
					 if(resAvailability.getBookForShift() == 1){
						 jsonUser.setBooked("B");
					 }else{
						 jsonUser.setBooked("NB");
					 }
					 if(resAvailability.getIsAvailable() == 1){
						 jsonUser.setAvailable("A");
					 }else{
						 jsonUser.setAvailable("NA");
					 }
				 }
			}
		}
		
		
		if(productCoreResources != null && productCoreResources.size() > 0){
			if(jsonUserList != null && jsonUserList.size()>0){
				for (JsonUserList jsonUser : jsonUserList) {
					for (UserList productCoreUser : productCoreResources) {
						if(productCoreUser.getUserId() == jsonUser.getUserId()){
							jsonUser.setProductCoreResource("Core");
							UserRoleMaster productCoreUserRole = testFactoryResourceReservationDao.getProductCoreResourcesProductRole(productCoreUser.getUserId(),productId,blockResourceForDate);
							if(productCoreUserRole != null){
								jsonUser.setUserRoleId(productCoreUserRole.getUserRoleId());
								jsonUser.setUserRoleLabel(productCoreUserRole.getRoleLabel());
							}
							break;
						}else{
							continue;
						}
					}
				}
			}
		}
		
		if(testFactoryCoreResources != null && testFactoryCoreResources.size() > 0){
			if(jsonUserList != null && jsonUserList.size()>0){
				for (JsonUserList jsonUser : jsonUserList) {
					for (UserList tfCoreUser : testFactoryCoreResources) {
						if(tfCoreUser.getUserId() == jsonUser.getUserId()){
							jsonUser.setTfCoreResource("Core");
							break;
						}else{
							continue;
						}
					}
				}
			}
		}
		
		
 		return jsonUserList ;
	}

	@Override
	@Transactional
	public List<JsonUserList> getAllUnReservedResourcesForReservation(
			int workPackageId, int shiftId, Date blockResourceForDate,
			String availabilityTypeFilter) {
		
		log.debug("in getAllUnReservedResourcesForReservation() ***");
		List<JsonUserList> jsonUserList = null;
		List<UserList> otherProductCoreResources = null;
		List<UserList> otherTestFactoryCoreResources = null;
		List<UserList> productCoreResources = null;
		List<UserList> testFactoryCoreResources = null;
		List<UserList> listOfUsersOfResourcePool = new ArrayList<UserList>();
		WorkShiftMaster workShift = workShiftMasterDAO.listWorkShiftsByshiftId(shiftId);

		TestFactory testFactory = null;
		Integer productId = 0;
		if(workPackageId!=-1){
			WorkPackage workPackage = workPackageDAO.getWorkPackageById(workPackageId);
			testFactory= testFactoryDao.getTestFactoryById(workPackage.getProductBuild().getProductVersion().getProductMaster().getTestFactory().getTestFactoryId());
			productId = workPackageDAO.getProductIdByWorkpackage(workPackageId);
		}else{
			testFactory=workShift.getTestFactory();
		}
		
		//Step 1 : Get the master list of all resources for the product, from the mapped resource pools of the test factory.
		List<TestfactoryResourcePool> listOfResourcePools = testFactoryDao.getResourcePoolListbyTestFactoryId(testFactory.getTestFactoryId());
		if((listOfResourcePools == null) || (listOfResourcePools.size() <= 0)){
			//There are no resource pools for the test factory. Return null.
			log.info("There are no resource pools mapped to this Test Factory. Hence cannot show resources for reservation : " + testFactory.getDisplayName());
			return null;
		}else if(listOfResourcePools != null && listOfResourcePools.size()>0){
			for (TestfactoryResourcePool testfactoryResourcePool : listOfResourcePools) {
				List<UserList> subListOfavailableUnReservedUsers = null;
				subListOfavailableUnReservedUsers = userListDAO.getUserListByRoleFromResourcePoolId(testfactoryResourcePool.getResourcePoolId());
				if(listOfUsersOfResourcePool.size() == 0){
					listOfUsersOfResourcePool = subListOfavailableUnReservedUsers;
				}else{
					listOfUsersOfResourcePool.addAll(subListOfavailableUnReservedUsers);
				}
			}
		}
		log.info("Total Resources for Test Factory : " + testFactory.getTestFactoryName() + " are : " + listOfUsersOfResourcePool.size());
		for (UserList user : listOfUsersOfResourcePool) {
			log.info("User : " + user.getLoginId());
		}
		
		List<UserWeekUtilisedTimeDTO> listOfTimeSheetEntries = timeSheetManagementDAO.getTimesheetEntriesForWorkPackage(workPackageId, workShift, blockResourceForDate);
		List<UserWeekUtilisedTimeDTO> listOfReservedShifts = timeSheetManagementDAO.getReservedShiftsOfUser(workPackageId, shiftId, blockResourceForDate);
		
		List<UserList> blockedUsers = testFactoryResourceReservationDao.getBlockedResources(blockResourceForDate, shiftId);
		log.info("Blocked Users for Date : " + blockResourceForDate + "  : Shift : " + workShift.getShiftName() + " are : " + blockedUsers.size());
		for (UserList user : blockedUsers) {
			log.info("User : " + user.getLoginId());
		}
		List<UserList> bookedOrAvailableResourceList = new ArrayList<UserList>();
		for (TestfactoryResourcePool testfactoryResourcePool : listOfResourcePools) {
			List<UserList> subListOfBOrAvailableUsers = null;
			subListOfBOrAvailableUsers = testFactoryResourceReservationDao.getResourcesAvailability(workShift.getShiftType().getShiftTypeId(), blockResourceForDate, "BookedOrAvailable",testfactoryResourcePool.getResourcePoolId());
			if(listOfUsersOfResourcePool.size() == 0){
				bookedOrAvailableResourceList = subListOfBOrAvailableUsers;
			}else{
				bookedOrAvailableResourceList.addAll(subListOfBOrAvailableUsers);
			}
		}
		log.info("Booked or Available Users for Date : " + blockResourceForDate + "  : Shift : " + workShift.getShiftName() + " are : " + bookedOrAvailableResourceList.size());
		for (UserList user : bookedOrAvailableResourceList) {
			log.info("User : " + user.getLoginId());
		}
		
		if(productId != 0){
			otherProductCoreResources = testFactoryResourceReservationDao.getOtherProductCoreResources(productId,blockResourceForDate,true);
			productCoreResources = testFactoryResourceReservationDao.getOtherProductCoreResources(productId,blockResourceForDate,false);
		} 
		for (UserList user : otherProductCoreResources) {
			log.info("User : " + user.getLoginId());
		}
		log.info("Current Product Core Resources are : " + productCoreResources.size());
		for (UserList user : productCoreResources) {
			log.info("User : " + user.getLoginId());
		}
		
		if(testFactory != null){
			otherTestFactoryCoreResources = testFactoryResourceReservationDao.getOtherTestFactoryCoreResources(testFactory.getTestFactoryId(),blockResourceForDate,true);
			testFactoryCoreResources = testFactoryResourceReservationDao.getOtherTestFactoryCoreResources(testFactory.getTestFactoryId(),blockResourceForDate,false);
		}
		log.info("Other Test Factory Core Resources are : " + otherTestFactoryCoreResources.size());
		for (UserList user : otherTestFactoryCoreResources) {
			log.info("User : " + user.getLoginId());
		}
		log.info("Current Test Factory Core Resources are : " + testFactoryCoreResources.size());
		for (UserList user : testFactoryCoreResources) {
			log.info("User : " + user.getLoginId());
		}
		
		//Remove already blocked users from the list of available users
		int index = -1;
		int counter = 0;
		if(blockedUsers != null && blockedUsers.size()>0){
			for (UserList blockedUser : blockedUsers) {
				index = listOfUsersOfResourcePool.indexOf(blockedUser);
				if (index >= 0) {
					listOfUsersOfResourcePool.remove(index);
					counter++;
				}
			}
		}
		log.info("Removed " + counter + " blocked users from available resources. Total available now are : " + listOfUsersOfResourcePool.size());
		
		//Remove already blocked users from the list of available users
		counter = 0;
		if(bookedOrAvailableResourceList != null && bookedOrAvailableResourceList.size()>0){
			for (UserList bookedOrAvailableUser : bookedOrAvailableResourceList) {
				index = listOfUsersOfResourcePool.indexOf(bookedOrAvailableUser);
				if (index >= 0) {
					listOfUsersOfResourcePool.remove(index);
					counter++;
				}
			}
		}
		log.info("Removed " + counter + " Booked or available users from available resources. Total available now are : " + listOfUsersOfResourcePool.size());
		
		// Remove other product core resources
		counter = 0;
		if(otherProductCoreResources != null && otherProductCoreResources.size()>0){
			for (UserList otherProductCoreResource : otherProductCoreResources) {
				index = listOfUsersOfResourcePool.indexOf(otherProductCoreResource);
				if (index >= 0) {
					listOfUsersOfResourcePool.remove(index);
					counter++;
				}
			}
		}
		log.info("Removed " + counter + " Other product core resources from available resources. Total available now are : " + listOfUsersOfResourcePool.size());
		
		
		// Remove other Test Factory core resources
		counter = 0;
		if(otherTestFactoryCoreResources != null && otherTestFactoryCoreResources.size()>0){
			for (UserList otherTestFactoryCoreResource : otherTestFactoryCoreResources) {
				index = listOfUsersOfResourcePool.indexOf(otherTestFactoryCoreResource);
				if (index >= 0) {
					listOfUsersOfResourcePool.remove(index);
					counter++;
				}
			}
		}
		log.info("Removed " + counter + " Other Test Factory core resources from available resources. Total available now are : " + listOfUsersOfResourcePool.size());
	
		
		if(listOfUsersOfResourcePool != null && listOfUsersOfResourcePool.size()>0){
			jsonUserList = new ArrayList<JsonUserList>();
			for (UserList user : listOfUsersOfResourcePool) {
				JsonUserList jsonUser = new JsonUserList();
				jsonUser.setLoginId(user.getLoginId());
				jsonUser.setUserId(user.getUserId());
				jsonUser.setUserRoleId(user.getUserRoleMaster().getUserRoleId());
				jsonUser.setUserRoleLabel(user.getUserRoleMaster().getRoleLabel());
				jsonUser.setImageURI(user.getImageURI());
				jsonUser.setReserve(new Integer(0));
				Set<UserSkills> setOfUserSkills = user.getUserSkills();
				StringBuffer sb = jsonUser.getSkillsOfUser(setOfUserSkills);
				if(sb != null){
					jsonUser.setSkillName(sb);
				}
				jsonUser.setBooked("NB");
				jsonUser.setAvailable("NA");
				jsonUserList.add(jsonUser);
			}
			log.info("Available jsonUserList size: "+jsonUserList.size());

			if(jsonUserList != null && jsonUserList.size()>0){
				//compare and set DTO's time sheet hours of user
				for (JsonUserList jsonUser : jsonUserList) {
					for (UserWeekUtilisedTimeDTO userWeekUtilisedTimeDTO : listOfTimeSheetEntries) {
						if(userWeekUtilisedTimeDTO.getUserId() == jsonUser.getUserId()){
							Integer totalTimeInMinutes = DateUtility.convertTimeInMinutes(userWeekUtilisedTimeDTO.getTimeSheetTotalHours(),userWeekUtilisedTimeDTO.getTimeSheetTotalMins());
							Integer remainingTimeSheetInMinutes = 2400-totalTimeInMinutes;
							userWeekUtilisedTimeDTO.setTimeSheetDuration(DateUtility.convertTimeInHoursMinutes(0,remainingTimeSheetInMinutes));
							jsonUser.setTimeSheetHours(userWeekUtilisedTimeDTO.getTimeSheetDuration());
							break;
						}else{
							continue;
						}
					}
				}
			}
			
			if(jsonUserList != null && jsonUserList.size()>0){
				//compare and set DTO's time sheet hours of user
				for (JsonUserList jsonUser : jsonUserList) {
					for (UserWeekUtilisedTimeDTO userWeekUtilisedTimeDTO : listOfReservedShifts) {
						if(userWeekUtilisedTimeDTO.getUserId() == jsonUser.getUserId()){
							long totalTime = 40-(userWeekUtilisedTimeDTO.getShiftBookingCount()*8);
							userWeekUtilisedTimeDTO.setShiftBookingDuration(String.valueOf(totalTime));
							jsonUser.setBookedHrs(totalTime);
							break;
						}else{
							continue;
						}
					}
				}
			}
			
			if(productCoreResources != null && productCoreResources.size() > 0){
				if(jsonUserList != null && jsonUserList.size()>0){
					for (JsonUserList jsonUser : jsonUserList) {
						for (UserList productCoreUser : productCoreResources) {
							if(productCoreUser.getUserId() == jsonUser.getUserId()){
								jsonUser.setProductCoreResource("Core");
								UserRoleMaster productCoreUserRole = testFactoryResourceReservationDao.getProductCoreResourcesProductRole(productCoreUser.getUserId(),productId,blockResourceForDate);
								if(productCoreUserRole != null){
									jsonUser.setUserRoleId(productCoreUserRole.getUserRoleId());
									jsonUser.setUserRoleLabel(productCoreUserRole.getRoleLabel());
								}
								break;
							}else{
								continue;
							}
						}
					}
				}
			}
			
			if(testFactoryCoreResources != null && testFactoryCoreResources.size() > 0){
				if(jsonUserList != null && jsonUserList.size()>0){
					for (JsonUserList jsonUser : jsonUserList) {
						for (UserList tfCoreUser : testFactoryCoreResources) {
							if(tfCoreUser.getUserId() == jsonUser.getUserId()){
								jsonUser.setTfCoreResource("Core");
								break;
							}else{
								continue;
							}
						}
					}
				}
			}
		}
 		return jsonUserList ;
	}
	
	
	@Override
	@Transactional
	public List<JsonUserList> getAllUnReservedResourcesForReservationWeekly(Integer workPackageId, Integer shiftId, Integer workWeek,Integer workYear, Integer userRoleId, Integer skillId, String filter,Integer userTypeId) {
		
		log.debug("in getAllUnReservedResourcesForReservation() ***");
		List<JsonUserList> jsonUserList = null;
		List<UserList> listOfUsersOfResourcePool = new ArrayList<UserList>();
		
		WorkShiftMaster workShift = workShiftMasterDAO.listWorkShiftsByshiftId(shiftId);
		
		Date startDate = DateUtility.getWeekStartDate(workWeek,workYear);

		TestFactory testFactory = null;
		if(workPackageId!=-1){
			WorkPackage workPackage = workPackageDAO.getWorkPackageById(workPackageId);
			testFactory= testFactoryDao.getTestFactoryById(workPackage.getProductBuild().getProductVersion().getProductMaster().getTestFactory().getTestFactoryId());
		}else{
			testFactory=workShift.getTestFactory();
		}
		
		List<TestfactoryResourcePool> listOfResourcePools = testFactoryDao.getResourcePoolListbyTestFactoryId(testFactory.getTestFactoryId());
		if((listOfResourcePools == null) || (listOfResourcePools.size() <= 0)){
			log.info("There are no resource pools mapped to this Test Factory. Hence cannot show resources for reservation : " + testFactory.getDisplayName());
			return null;
		}else if(listOfResourcePools != null && listOfResourcePools.size()>0){
			for (TestfactoryResourcePool testfactoryResourcePool : listOfResourcePools) {
				List<UserList> subListOfavailableUnReservedUsers = null;
				Skill skill = skillDAO.getBySkillId(skillId);
				
				if(filter.equalsIgnoreCase("All")){
					
					subListOfavailableUnReservedUsers = userListDAO.getUserListByRoleFromResourcePoolIdByRoleAndSkill(testfactoryResourcePool.getResourcePoolId(), null, null,null,startDate,null);
				}else{
					subListOfavailableUnReservedUsers = userListDAO.getUserListByRoleFromResourcePoolIdByRoleAndSkill(testfactoryResourcePool.getResourcePoolId(), userRoleId, skillId,userTypeId,startDate,skill);
				}
				
				if(listOfUsersOfResourcePool.size() == 0){
					listOfUsersOfResourcePool = subListOfavailableUnReservedUsers;
				}else{
					listOfUsersOfResourcePool.addAll(subListOfavailableUnReservedUsers);
				}
			}
			
			
			
		}
		
		Set<UserList> hs = new HashSet<>();
		hs.addAll(listOfUsersOfResourcePool);
		listOfUsersOfResourcePool.clear();
		listOfUsersOfResourcePool.addAll(hs);
		
		
		List<UserList> blockedUsers = testFactoryResourceReservationDao.getBlockedResourcesWeeklyByRoleAndSkill(workPackageId,workWeek, workYear, shiftId, userRoleId, skillId,userTypeId);
		int index = -1;
		int counter = 0;
		if(blockedUsers != null && blockedUsers.size()>0){
			for (UserList blockedUser : blockedUsers) {
				index = listOfUsersOfResourcePool.indexOf(blockedUser);
				if (index >= 0) {
					listOfUsersOfResourcePool.remove(index);
					counter++;
				}
			}
		}
		log.info("Removed " + counter + " blocked users from available resources. Total available now are : " + listOfUsersOfResourcePool.size());
		Integer totalPercentage = 0;
		if(listOfUsersOfResourcePool != null && listOfUsersOfResourcePool.size()>0){
			jsonUserList = new ArrayList<JsonUserList>();
			for (UserList user : listOfUsersOfResourcePool) {
				totalPercentage = getTotalReservationPercentageByUserId(user.getUserId(),workWeek,workYear);
				JsonUserList jsonUser = new JsonUserList();
				jsonUser.setLoginId(user.getLoginId());
				jsonUser.setUserId(user.getUserId());
				jsonUser.setUserRoleId(user.getUserRoleMaster().getUserRoleId());
				jsonUser.setUserRoleLabel(user.getUserRoleMaster().getRoleLabel());
				jsonUser.setImageURI(user.getImageURI());
				jsonUser.setTotalReservationPercentage(totalPercentage);
				jsonUser.setUserTypeLabel(user.getUserTypeMasterNew().getUserTypeLabel());
				jsonUser.setUserCode(user.getUserCode());
				jsonUser.setReserve(new Integer(0));
				Set<UserSkills> setOfUserSkills = user.getUserSkills();
				StringBuffer sb = jsonUser.getSkillsOfUser(setOfUserSkills);
				if(sb != null){
					jsonUser.setSkillName(sb);
				}
				jsonUser.setBooked("NB");
				jsonUser.setAvailable("NA");
				jsonUserList.add(jsonUser);
			}
			log.info("Available jsonUserList size: "+jsonUserList.size());
		}
 		return jsonUserList ;
	}

	@Override
	@Transactional
	public List<JsonTestFactoryResourceReservation> listResourcesReliableTotalBookingSummary(Integer userId, Date startDate, Date endDate) {
		log.info("listResourcesReliable Total Bookings   >>>>  "+"userId::: "+userId+"    startDate: "+ startDate+"      endDate: "+endDate );
		List<JsonTestFactoryResourceReservation> jsonResourceReliableList = new ArrayList<JsonTestFactoryResourceReservation>();

		List<TestFactoryResourceReservation> testFactoryResourceReservationList = resourceAvailabilityDAO.listResourceReliableTotalBookingSummary(userId, startDate, endDate);
		if (testFactoryResourceReservationList!=null && !testFactoryResourceReservationList.isEmpty()) {
			for (TestFactoryResourceReservation testFactoryResourceReservation : testFactoryResourceReservationList) {
				jsonResourceReliableList.add(new JsonTestFactoryResourceReservation(testFactoryResourceReservation));
				}
			}
		return jsonResourceReliableList;
	}
	
	@Override
	@Transactional
	public List<JsonTestFactoryResourceReservation> listResourcesReliableShowUpSummary(Integer userId, Date startDate, Date endDate) {
		log.info("listResourcesReliableNoShowSummary No Show  >>>>  "+"userId::: "+userId+"    startDate: "+ startDate+"      endDate: "+endDate );
		List<JsonTestFactoryResourceReservation> jsonResourceReliableList = new ArrayList<JsonTestFactoryResourceReservation>();
		List<TestFactoryResourceReservation> testFactoryResourceReservationList = resourceAvailabilityDAO.listResourceReliableTotalBookingSummary(userId, startDate, endDate);
		if (testFactoryResourceReservationList!=null && !testFactoryResourceReservationList.isEmpty()) {
			for (TestFactoryResourceReservation testFactoryResourceReservation : testFactoryResourceReservationList) {
					ShiftTypeMaster shiftType = workShiftMasterDAO.getShiftTypeByShiftId(testFactoryResourceReservation.getShift().getShiftId());
					ResourceAvailability resourceAvail =  resourceAvailabilityDAO.listShowUpandOnTime(testFactoryResourceReservation.getBlockedUser().getUserId(), testFactoryResourceReservation.getReservationDate(), shiftType.getShiftTypeId());
					if(resourceAvail != null){
						if(resourceAvail.getShiftAttendance() != null && resourceAvail.getShiftAttendance() == 1){
							jsonResourceReliableList.add(new JsonTestFactoryResourceReservation(testFactoryResourceReservation));
						}
					}
				}
			}
		return jsonResourceReliableList;
	}
	
	
	@Override
	@Transactional
	public List<JsonTestFactoryResourceReservation> listResourcesReliableOnTimeSummary(Integer userId, Date startDate, Date endDate) {
		log.info("listResourcesReliableLateSummary Late   >>>>  "+"userId::: "+userId+"    startDate: "+ startDate+"      endDate: "+endDate );
		List<JsonTestFactoryResourceReservation> jsonResourceReliableList = new ArrayList<JsonTestFactoryResourceReservation>();
		List<TestFactoryResourceReservation> testFactoryResourceReservationList = resourceAvailabilityDAO.listResourceReliableTotalBookingSummary(userId, startDate, endDate);
		if (testFactoryResourceReservationList!=null && !testFactoryResourceReservationList.isEmpty()) {
			for (TestFactoryResourceReservation testFactoryResourceReservation : testFactoryResourceReservationList) {
				ShiftTypeMaster shiftType = workShiftMasterDAO.getShiftTypeByShiftId(testFactoryResourceReservation.getShift().getShiftId());
				ResourceAvailability resourceAvailabliity =  resourceAvailabilityDAO.listShowUpandOnTime(testFactoryResourceReservation.getBlockedUser().getUserId(), testFactoryResourceReservation.getReservationDate(),  shiftType.getShiftTypeId());
				if(resourceAvailabliity != null){
					ActualShift actualShift = workShiftMasterDAO.listActualShiftbyshiftId(testFactoryResourceReservation.getShift().getShiftId(), testFactoryResourceReservation.getReservationDate());
					if(actualShift != null){
						if(actualShift.getStartTime() != null){
							Long timeDiff = Math.abs(resourceAvailabliity.getAttendanceCheckInTime().getTime() - actualShift.getStartTime().getTime());
								if(timeDiff < 300000){
									jsonResourceReliableList.add(new JsonTestFactoryResourceReservation(testFactoryResourceReservation));
								}
							}
						}
					}
				}
			}
		return jsonResourceReliableList;
	}
	
	@Override
	@Transactional
	public JsonWorkPackageResultsStatistics updateResourceDailyPerformance(JsonWorkPackageResultsStatistics jsonWorkPackageResultsStatistics, UserList rater){
		log.info("inside updateResourceDailyPerformance");
		String actualDateStr = jsonWorkPackageResultsStatistics.getActualExecutionDate();
		ResourceDailyPerformance resourceDailyPerformance = resourcePerformanceDAO.getResourceDailyPerformance(
				jsonWorkPackageResultsStatistics.getTesterId(), DateUtility.dateformatWithOutTime(jsonWorkPackageResultsStatistics.getActualExecutionDate()), 
				jsonWorkPackageResultsStatistics.getActualShiftId(), jsonWorkPackageResultsStatistics.getWorkPackageId());
		
		if (resourceDailyPerformance == null){
			log.info("resource performance object is null");
			resourceDailyPerformance = new ResourceDailyPerformance();
			resourceDailyPerformance.setWorkDate(DateUtility.dateformatWithOutTime(jsonWorkPackageResultsStatistics.getActualExecutionDate()));
			if(jsonWorkPackageResultsStatistics.getActualShiftId() != null && jsonWorkPackageResultsStatistics.getActualShiftId() != 0){
				ActualShift actualShift = new ActualShift();
				actualShift.setActualShiftId(jsonWorkPackageResultsStatistics.getActualShiftId());
				resourceDailyPerformance.setActualShift(actualShift);
			}
			
			
			WorkPackage workPackage = new WorkPackage();
			workPackage.setWorkPackageId(jsonWorkPackageResultsStatistics.getWorkPackageId());
			resourceDailyPerformance.setWorkPackage(workPackage);
			
			UserList userList = new UserList();
			userList.setUserId(jsonWorkPackageResultsStatistics.getTesterId());
			resourceDailyPerformance.setUser(userList);
		}	
		
		PerformanceLevel performanceLevel = new PerformanceLevel();
		performanceLevel.setPerformanceLevelId(jsonWorkPackageResultsStatistics.getRatings());
		resourceDailyPerformance.setPerformanceLevel(performanceLevel);
		jsonWorkPackageResultsStatistics.setRatings(resourceDailyPerformance.getPerformanceLevel().getPerformanceLevelId());
		resourceDailyPerformance.setRaterComments(jsonWorkPackageResultsStatistics.getRaterComments());
		
		resourceDailyPerformance.setRatedByUser(rater);
		jsonWorkPackageResultsStatistics.setRatedByUserId(rater.getUserId());
		jsonWorkPackageResultsStatistics.setRatedByUserName(rater.getUserDisplayName());
		
		resourceDailyPerformance.setRatedOn(DateUtility.getCurrentTime());
		jsonWorkPackageResultsStatistics.setRatedOn(DateUtility.dateToStringInSecond(resourceDailyPerformance.getRatedOn()));
		
		resourcePerformanceDAO.updateResourceDailyPerformance(resourceDailyPerformance);
		return jsonWorkPackageResultsStatistics;
	}

	@Override
	@Transactional
	public JsonWorkPackageResultsStatistics resourceDailyPerformanceApproveUpdate(JsonWorkPackageResultsStatistics jsonWorkPackageResultsStatistics, UserList approver){
		log.info("inside resourceDailyPerformanceApproveUpdate");
		ResourceDailyPerformance resourceDailyPerformance = resourcePerformanceDAO.getResourceDailyPerformance(
				jsonWorkPackageResultsStatistics.getTesterId(), DateUtility.toDateInSec(jsonWorkPackageResultsStatistics.getActualExecutionDate()), 
				jsonWorkPackageResultsStatistics.getActualShiftId(), jsonWorkPackageResultsStatistics.getWorkPackageId());
		
		if (resourceDailyPerformance == null){
			log.info("resource performance object is null");
			resourceDailyPerformance = new ResourceDailyPerformance();
			resourceDailyPerformance.setWorkDate(DateUtility.toDateInSec(jsonWorkPackageResultsStatistics.getActualExecutionDate()));
			
			ActualShift actualShift = new ActualShift();
			actualShift.setActualShiftId(jsonWorkPackageResultsStatistics.getActualShiftId());
			resourceDailyPerformance.setActualShift(actualShift);
			
			WorkPackage workPackage = new WorkPackage();
			workPackage.setWorkPackageId(jsonWorkPackageResultsStatistics.getWorkPackageId());
			resourceDailyPerformance.setWorkPackage(workPackage);
			
			UserList userList = new UserList();
			userList.setUserId(jsonWorkPackageResultsStatistics.getTesterId());
			resourceDailyPerformance.setUser(userList);
		}	
	
		PerformanceLevel performanceLevel = new PerformanceLevel();
		performanceLevel.setPerformanceLevelId(jsonWorkPackageResultsStatistics.getRatings());
		resourceDailyPerformance.setPerformanceLevel(performanceLevel);
		
		boolean ratingDataChanged = false;
		if (jsonWorkPackageResultsStatistics.getRaterComments() != resourceDailyPerformance.getRaterComments())
			ratingDataChanged = true;
		
		if (resourceDailyPerformance.getPerformanceLevel() == null && jsonWorkPackageResultsStatistics.getRatings() != null)
					ratingDataChanged = true;
		if (resourceDailyPerformance.getPerformanceLevel() != null && jsonWorkPackageResultsStatistics.getRatings() == null)
			ratingDataChanged = true;

		if (jsonWorkPackageResultsStatistics.getRatings().intValue() != resourceDailyPerformance.getPerformanceLevel().getPerformanceLevelId().intValue())
				ratingDataChanged = true;
		 
		resourceDailyPerformance.setRaterComments(jsonWorkPackageResultsStatistics.getRaterComments());
		
		resourceDailyPerformance.setRatedByUser(approver);
		jsonWorkPackageResultsStatistics.setRatedByUserId(approver.getUserId());
		jsonWorkPackageResultsStatistics.setRatedByUserName(approver.getUserDisplayName());
		
		resourceDailyPerformance.setRatedOn(DateUtility.getCurrentTime());
		jsonWorkPackageResultsStatistics.setRatedOn(DateUtility.dateToStringInSecond(resourceDailyPerformance.getRatedOn()));
		
		resourceDailyPerformance.setIsRatingApproved(jsonWorkPackageResultsStatistics.getIsRatingApproved());
		resourceDailyPerformance.setApproverComments(jsonWorkPackageResultsStatistics.getRatingApproverComments());
		
		resourceDailyPerformance.setApprovedByUser(approver);
		jsonWorkPackageResultsStatistics.setRatingApprovedByUserId(approver.getUserId());
		jsonWorkPackageResultsStatistics.setRatingApprovedByUserName(approver.getUserDisplayName());
		
		resourceDailyPerformance.setApprovedOn(DateUtility.getCurrentTime());
		jsonWorkPackageResultsStatistics.setRatingApprovedOn(DateUtility.dateToStringInSecond(resourceDailyPerformance.getApprovedOn()));
		
		resourcePerformanceDAO.updateResourceDailyPerformance(resourceDailyPerformance);
		return jsonWorkPackageResultsStatistics;
	}
	
	@Override
	@Transactional
	public List<JsonReservedResourcesForBooking> getNotAvailAndOrNotBookedResourceCount(Integer resourcePoolId, Date startDate, Date endDate) {
		log.info("getCountOfReservedResourcedForBooking ()   >>>>  "+"resourcePoolId::: "+resourcePoolId+"    startDate: "+ startDate+"      endDate: "+endDate );
		List<JsonReservedResourcesForBooking> jsonReservedResourcesForBookingList = new ArrayList<JsonReservedResourcesForBooking>();
		HashMap<String, List<TestFactoryResourceReservation>> map = null;
		List<TestFactoryResourceReservation> listOfTestFactoryResourceReservations = testFactoryResourceReservationDao.listResourceReservationsByDates(resourcePoolId, startDate, endDate,0);
		if(listOfTestFactoryResourceReservations != null && listOfTestFactoryResourceReservations.size()>0){
			map = new HashMap<String, List<TestFactoryResourceReservation>>();
			List<TestFactoryResourceReservation> trReservationList  = null;
			for (TestFactoryResourceReservation testFactoryResourceReservation : listOfTestFactoryResourceReservations) {
				String key = testFactoryResourceReservation.getReservationDate() +"~" +testFactoryResourceReservation.getShift().getShiftId();
				if(map.containsKey(key)){
					trReservationList  = map.get(key);
					trReservationList.add(testFactoryResourceReservation);
					map.put(key, trReservationList);
				}else{
					trReservationList = new ArrayList<TestFactoryResourceReservation>();
					trReservationList.add(testFactoryResourceReservation);
					map.put(key, trReservationList);
				}
			}
			
			if(map != null && map.size()>0){
				for (String key : map.keySet()) {
					int notAvailAndNotBookedCounter = 0;
			    	int availableAndNotBookedCounter = 0;
			    	JsonReservedResourcesForBooking jsonReservedResourcesForBooking = null;
					List<TestFactoryResourceReservation> tfReservedList = map.get(key);
				   // 
				    if(tfReservedList != null && tfReservedList.size()>0){
				    	int iCounter = 0;
				    	for (TestFactoryResourceReservation testFactoryResourceReservation : tfReservedList) {
				    		iCounter++;
				    		ShiftTypeMaster shiftType = workShiftMasterDAO.getShiftTypeByShiftId(testFactoryResourceReservation.getShift().getShiftId());
				    		ResourceAvailability resourceAvailability = resourceAvailabilityDAO.getAvailabilityAndBookingStatusOfUserByDate(testFactoryResourceReservation.getBlockedUser().getUserId(),testFactoryResourceReservation.getReservationDate(), shiftType.getShiftTypeId());
				    		if(resourceAvailability != null){
				    			 jsonReservedResourcesForBooking = new JsonReservedResourcesForBooking();
				    			 jsonReservedResourcesForBooking.setReservedResourceBookingId(iCounter);
				    			 jsonReservedResourcesForBooking.setResourcePoolId(testFactoryResourceReservation.getBlockedUser().getResourcePool().getResourcePoolId());
				    			 jsonReservedResourcesForBooking.setResourcePoolName(testFactoryResourceReservation.getBlockedUser().getResourcePool().getResourcePoolName());
				    			 jsonReservedResourcesForBooking.setReservationDate(DateUtility.sdfDateformatWithOutTime(resourceAvailability.getWorkDate()));
				    			 jsonReservedResourcesForBooking.setShiftTypeId(resourceAvailability.getShiftTypeMaster().getShiftTypeId());
				    			 jsonReservedResourcesForBooking.setShiftTypeName(resourceAvailability.getShiftTypeMaster().getShiftName());
				    			if(resourceAvailability.getIsAvailable() == null){
				    				notAvailAndNotBookedCounter++;
				    			}
				    			if(resourceAvailability.getIsAvailable() != null && resourceAvailability.getBookForShift() == null){
				    				if(resourceAvailability.getIsAvailable() == 0){
				    					notAvailAndNotBookedCounter++;
				    				}
				    			}else{
				    				if(resourceAvailability.getIsAvailable().equals(0) && resourceAvailability.getBookForShift().equals(0)){
				    					log.info("Date: "+testFactoryResourceReservation.getReservationDate()+ "  User Id : "+resourceAvailability.getResource().getUserId()+" shift Type:  "+resourceAvailability.getShiftTypeMaster().getShiftName()+" Availability>>>>>> "+resourceAvailability.getIsAvailable() +"    Booking >>>>>> "+resourceAvailability.getBookForShift());
					    				notAvailAndNotBookedCounter++;
					    			}else if(resourceAvailability.getIsAvailable() == 1 && resourceAvailability.getBookForShift() == 0){
					    				availableAndNotBookedCounter++;
					    			}
				    			}
				    		}
						}
				    }
				    if(notAvailAndNotBookedCounter == 0){
				    	// Both values are zero. then don't add the object to the list
				    }else{
				    	 jsonReservedResourcesForBooking.setNotAvailableAndNotBookedCount(notAvailAndNotBookedCounter);
						 jsonReservedResourcesForBooking.setAvailableNotBookedCount(availableAndNotBookedCounter);
						 jsonReservedResourcesForBookingList.add(jsonReservedResourcesForBooking);
				    }
				}
			}
		}
		return jsonReservedResourcesForBookingList;
	}

	@Override
	public List<JsonResourceAvailabilityPlan> listReservedResourcesForBooking(int resourcePoolId, Date workDate, int shiftTypeId, int availabilityStatusIdentifier,Integer jtStartIndex, Integer jtPageSize) {
		log.info("Getting Reserved Resources of : " + resourcePoolId + " for Work Date:  " + workDate);
		List<JsonResourceAvailabilityPlan> jsonResourceAvailabilityPlanList = new ArrayList<JsonResourceAvailabilityPlan>();
		List<TestFactoryResourceReservation> listOfTestFactoryResourceReservations = testFactoryResourceReservationDao.listResourceReservationsByDatesForResourcePool(resourcePoolId, workDate, null,shiftTypeId,jtStartIndex, jtPageSize);
		List<UserList> userListCollection = new ArrayList<UserList>();
		if(listOfTestFactoryResourceReservations != null && listOfTestFactoryResourceReservations.size()>0){
			for (TestFactoryResourceReservation testFactoryResourceReservation : listOfTestFactoryResourceReservations) {
				if(testFactoryResourceReservation != null){
					userListCollection.add(testFactoryResourceReservation.getBlockedUser());
				}
			}
		}
		
		
		if(userListCollection != null && userListCollection.size() > 0){
			for(UserList userList : userListCollection) {
				JsonResourceAvailabilityPlan jsonResourceAvailabilityPlan = new JsonResourceAvailabilityPlan();
				jsonResourceAvailabilityPlan.setResourcePoolId(userList.getResourcePool().getResourcePoolId());
				jsonResourceAvailabilityPlan.setResourcePoolName(userList.getResourcePool().getResourcePoolName());
				jsonResourceAvailabilityPlan.setUserId(userList.getUserId());
				jsonResourceAvailabilityPlan.setUserName(userList.getUserDisplayName());
				jsonResourceAvailabilityPlan.setWorkDate(DateUtility.sdfDateformatWithOutTime(workDate));
				jsonResourceAvailabilityPlan.setRoleName(userList.getUserRoleMaster().getRoleLabel());
				jsonResourceAvailabilityPlan.setUserTypeName(userList.getUserTypeMasterNew().getUserTypeLabel());
				jsonResourceAvailabilityPlan.setVendorId(userList.getVendor().getVendorId());
				jsonResourceAvailabilityPlan.setRegisteredCompanyName(userList.getVendor().getRegisteredCompanyName());
				StringBuffer skillarray = new StringBuffer();
				Set<UserSkills> userSkillsSet = userList.getUserSkills();
				int blocked_UserId = userList.getUserId();
					if(userSkillsSet != null && userSkillsSet.size()>0){
					skillarray= getSkillsOfUsers(userSkillsSet, blocked_UserId);			
				}
				if(skillarray.length() != 0){
					jsonResourceAvailabilityPlan.setSkillName(skillarray);
					jsonResourceAvailabilityPlan.setSkillNameCharacterlength(skillarray.length());
					
				}else{
					jsonResourceAvailabilityPlan.setSkillName(new StringBuffer(""));
					jsonResourceAvailabilityPlan.setSkillNameCharacterlength(0);
					
				}
				ResourceAvailability resourceAvailability =  resourceAvailabilityDAO.listResourceAVailabilityForBooking(userList.getUserId(), workDate, shiftTypeId, availabilityStatusIdentifier);
				Integer availabilityStatus = null;
				Integer bookingStatus = null;
				if(resourceAvailability != null){
						jsonResourceAvailabilityPlan.setResourceAvailabilityId(resourceAvailability.getResourceAvailabilityId());
						if(resourceAvailability.getIsAvailable() != null){
							availabilityStatus = resourceAvailability.getIsAvailable();
							jsonResourceAvailabilityPlan.setShiftTypeAvailability(availabilityStatus);
						}
						if(resourceAvailability.getBookForShift() != null){
							bookingStatus = resourceAvailability.getBookForShift();
							jsonResourceAvailabilityPlan.setShiftTypeBooking(bookingStatus);
						}
						log.info("Resource availability : " + resourceAvailability.getIsAvailable());
						log.info("Resource Book for shift : " + resourceAvailability.getBookForShift());
						List<UserWeekUtilisedTimeDTO> listOfTimeSheetEntriesforUser = timeSheetManagementDAO.getTimesheetEntriesForUser(0, shiftTypeId, workDate, userList.getUserId());
						log.info("Resource available list for use "+userList.getUserId()+" is "+listOfTimeSheetEntriesforUser.size());
						if(listOfTimeSheetEntriesforUser != null && !listOfTimeSheetEntriesforUser.isEmpty()){
							for (UserWeekUtilisedTimeDTO userWeekUtilisedTimeDTO : listOfTimeSheetEntriesforUser) {
								int userId = userWeekUtilisedTimeDTO.getUserId();
								if(userId == userList.getUserId()){
									Integer totalTimeInMinutes = DateUtility.convertTimeInMinutes(userWeekUtilisedTimeDTO.getTimeSheetTotalHours(),userWeekUtilisedTimeDTO.getTimeSheetTotalMins());
									Integer remainingTimeSheetInMinutes = 2400-totalTimeInMinutes;
									userWeekUtilisedTimeDTO.setTimeSheetDuration(DateUtility.convertTimeInHoursMinutes(0,remainingTimeSheetInMinutes));
									jsonResourceAvailabilityPlan.setTimeSheetHours(String.valueOf(userWeekUtilisedTimeDTO.getTimeSheetDuration()));
									log.info("userWeekUtilisedTimeDTO.getTimeSheetDuration(): "+userWeekUtilisedTimeDTO.getTimeSheetDuration());
								}
							}
						}	
							
			           List<UserWeekUtilisedTimeDTO> listOfReservedShifts = timeSheetManagementDAO.getReservedShiftsforUserId(userList.getUserId(), workDate) ;					
			           for (UserWeekUtilisedTimeDTO userWeekUtilisedTimeDTO : listOfReservedShifts) {
							log.info("userWeekUtilisedTimeDTO User Id: "+userWeekUtilisedTimeDTO.getUserId());
							long totalTime = 40-(userWeekUtilisedTimeDTO.getShiftBookingCount()*8);
							userWeekUtilisedTimeDTO.setShiftBookingDuration(String.valueOf(totalTime));
							log.info("userWeekUtilisedTimeDTO.getTimeSheetDuration(): "+userWeekUtilisedTimeDTO.getShiftBookingDuration());
							jsonResourceAvailabilityPlan.setBookedHrs(totalTime);
							
						}		
						jsonResourceAvailabilityPlanList.add(jsonResourceAvailabilityPlan);
				}
			}
			
			return jsonResourceAvailabilityPlanList;
		}else {
			return null;
		}
	}

	@Override
	@Transactional
	public void updateAvailabilityForReservedResources(String[] resourceAvailabilityIdsList, String workDate, Integer shiftTypeId, Integer availabilityStatus, int filterType) {
		log.info("availabilityStatus : "+availabilityStatus);
		ResourceAvailability resourceAvailability=null;
		TestFactoryResourceReservation testFactoryResourceReservation=null;
		try
		{
	    	if(filterType == 1){
	    		/// Confirm Booking of unbooked reserved resources
	    		for(String resourceAvailabilityId : resourceAvailabilityIdsList){
		    		log.info("resourceAvailabilityId="+resourceAvailabilityId);
		    		resourceAvailability = resourceAvailabilityDAO.getResourceAvailabilityById(Integer.parseInt(resourceAvailabilityId));
		    		if(resourceAvailability != null){
		    			resourceAvailabilityDAO.updateReservedResourceAvailability(resourceAvailability,availabilityStatus);
		    		}
		    	}
	    	}else{
	    		// If 0 - Remove Reservations as there resource will not be available
	    		for(String resourceAvailabilityId : resourceAvailabilityIdsList){
		    		log.info("resourceAvailabilityId="+resourceAvailabilityId);
		    		if(testFactoryResourceReservation != null){
		    			testFactoryResourceReservationDao.removeUnblockedResources(testFactoryResourceReservation);
		    		}
		    	}
	    	}
	    	
		}catch (Exception e) {
			log.info("Problem in adding Plan details records", e);
			return;
		}
	}

	@Override
	public boolean isAvailabilityExistForUser(Integer userId, Date workDate,Integer shiftTypeId) {
		boolean isAvailabilityExistForUser = false;
		ResourceAvailability resourceAvailability = resourceAvailabilityDAO.getAvailabilityAndBookingStatusOfUserByDate(userId, workDate, shiftTypeId);
		if(resourceAvailability != null){
			isAvailabilityExistForUser = true;
		}
		return isAvailabilityExistForUser;
	}

	@Override
	@Transactional
	public void addEntryForUserInResourceAvailability(Integer userId, Date workDate, ShiftTypeMaster shiftType) {
		ResourceAvailability newResourceAvailability = new ResourceAvailability();
		newResourceAvailability.setWorkDate(workDate);
		newResourceAvailability.setShiftTypeMaster(shiftType);
		newResourceAvailability.setIsAvailable(0);
		UserList user = userListDAO.getByUserId(userId);
		newResourceAvailability.setResource(user);
		newResourceAvailability.setBookForShift(0);
		resourceAvailabilityDAO.add(newResourceAvailability);
		log.info("Add an entry with Availability as 0 and booking for shift as 0 for Resource, when Test manager reserves unaviable user");
	}

	@Override
	public WorkShiftMaster listWorkShiftsByshiftId(int shiftId) {
		return workShiftMasterDAO.listWorkShiftsByshiftId(shiftId);
	}

	@Override
	public List<UserWeekUtilisedTimeDTO> getTimesheetEntriesForWorkPackage(
			int workPackgeId, WorkShiftMaster shift, Date blockResourceForDate) {
		return timeSheetManagementDAO.getTimesheetEntriesForWorkPackage(workPackgeId, shift, blockResourceForDate);
	}

	@Override
	public List<UserWeekUtilisedTimeDTO> getReservedShiftsOfUser(
			int workPackageId, int shiftId,
			Date blockResourceForDate) {
		return timeSheetManagementDAO.getReservedShiftsOfUser(workPackageId, shiftId, blockResourceForDate);
	}
	
	@Override
	@Transactional
	public Long listResourcesForAVailabilityCount(Integer resourcePoolId) {
		return  resourceAvailabilityDAO.listResourcesForAVailabilityCount(resourcePoolId);
	}
	
	@Override
	@Transactional
	public void confirmBookingForAvailableResource(Integer userId,Date reservationDate, Integer shiftTypeId) {
		ResourceAvailability resourceAvailability = resourceAvailabilityDAO.getAvailabilityAndBookingStatusOfUserByDate(userId, reservationDate, shiftTypeId);
		int availabilityStatus = 1 ; // Confirm Booking for resource booked from BookedorAvailable type pool
		if(resourceAvailability != null){
			resourceAvailabilityDAO.updateReservedResourceAvailability(resourceAvailability, availabilityStatus);
		}
	}
	
	@Override
	@Transactional
	public Float getBlockedResourcesCount(Date reservationDate,Integer testFactoryLabId,
			Integer resourcePoolId, Integer shiftTypeId) {
		Float blockedResourceCount = 0f;
		if(resourcePoolId == -1){
			List<TestfactoryResourcePool> listTestfactoryResourcePools  = testfactoryResourcePoolDAO.listResourcePoolByTestFactoryLabId(testFactoryLabId);
			if(listTestfactoryResourcePools != null && listTestfactoryResourcePools.size()>0){
				for (TestfactoryResourcePool testfactoryResourcePool : listTestfactoryResourcePools) {
					Float subBlockedResourceCount = 0f;
					ResourceCountDTO resourceCountDTO = testFactoryResourceReservationDao.getBlockedResourcesCount(reservationDate,testFactoryLabId, testfactoryResourcePool.getResourcePoolId(), shiftTypeId);
					if(resourceCountDTO != null){
						subBlockedResourceCount = resourceCountDTO.getBlockedResourcesCount();
						blockedResourceCount = blockedResourceCount+subBlockedResourceCount;
					}
				}
			}
		}else{
			ResourceCountDTO resourceCountDTO = testFactoryResourceReservationDao.getBlockedResourcesCount(reservationDate,testFactoryLabId, resourcePoolId, shiftTypeId);
			if(resourceCountDTO != null){
				blockedResourceCount = resourceCountDTO.getBlockedResourcesCount();
			}
		}
		return blockedResourceCount;
	}

	@Override
	@Transactional
	public Float getBlockedResourcesAttendanceCount(Date reservationDate,Integer testFactoryLabId,
			Integer resourcePoolId, Integer shiftTypeId) {
		Float blockedResourceCount = 0f;
		if(resourcePoolId == -1){
			List<TestfactoryResourcePool> listTestfactoryResourcePools  = testfactoryResourcePoolDAO.listResourcePoolByTestFactoryLabId(testFactoryLabId);
			if(listTestfactoryResourcePools != null && listTestfactoryResourcePools.size()>0){
				for (TestfactoryResourcePool testfactoryResourcePool : listTestfactoryResourcePools) {
					Float subBlockedResourceCount = 0f;
					ResourceCountDTO resourceCountDTO = resourceAvailabilityDAO.getBlockedResourcesAttendanceCount(reservationDate,testFactoryLabId, testfactoryResourcePool.getResourcePoolId(), shiftTypeId);
					if(resourceCountDTO != null){
						subBlockedResourceCount = resourceCountDTO.getBlockedResourcesCount();
						blockedResourceCount = blockedResourceCount+subBlockedResourceCount;
					}
				}
			}
		}else{
			ResourceCountDTO resourceCountDTO = resourceAvailabilityDAO.getBlockedResourcesAttendanceCount(reservationDate,testFactoryLabId, resourcePoolId, shiftTypeId);
			if(resourceCountDTO != null){
				blockedResourceCount = resourceCountDTO.getBlockedResourcesCount();
			}
		}
		return blockedResourceCount;
	}
	
	
	
	@Override
	@Transactional
	public List<JsonResourceAvailability> listResourceWorkTime(int resourceId, int weekNo) {
		Date startDate = DateUtility.getDateForDayOfWeek(weekNo,0);
		log.info("startDate="+startDate);
		Date endDate = DateUtility.getDateForDayOfWeek(weekNo,6);
		log.info("endDate="+endDate);
		UserList user=userListDAO.getByUserId(resourceId);
		Set<TestFactory> testFactoryList= user.getResourcePool().getTestFactoryList();
		List<ShiftTypeMaster> shiftTypeMasterList =  resourceAvailabilityDAO.listShiftTypeMaster();
		Map<String, JsonResourceAvailability> jsonResourceAvailabilityShiftMap = new HashMap<String, JsonResourceAvailability>();
		JsonResourceAvailability jsonResourceAvailabilityShift = null;
		int counter = 1;
		for(ShiftTypeMaster shiftType : shiftTypeMasterList){

			jsonResourceAvailabilityShift = new JsonResourceAvailability();
			jsonResourceAvailabilityShift.setResourceAvailabilityId(counter++);
			jsonResourceAvailabilityShift.setResourceId(resourceId);
			jsonResourceAvailabilityShift.setShiftId(shiftType.getShiftTypeId());
			jsonResourceAvailabilityShift.setShiftName(shiftType.getShiftName());
			jsonResourceAvailabilityShift.setWeekNo(weekNo);
			
			jsonResourceAvailabilityShiftMap.put(jsonResourceAvailabilityShift.getShiftName(), jsonResourceAvailabilityShift);
			
		}
		
		List<ResourceAvailability> resourceAvailabilityList =  resourceAvailabilityDAO.listResourceAvailability(resourceId, startDate, endDate);
		List<JsonResourceAvailability> jsonWorkPackageDemandProjections = new ArrayList<JsonResourceAvailability>();
	
		if(resourceAvailabilityList== null || resourceAvailabilityList.isEmpty()){
			
		} else {
			for (ResourceAvailability resourceAvailability : resourceAvailabilityList) {
			
				Integer availabilityStatus = 0;
				if (resourceAvailability.getIsAvailable() == null || resourceAvailability.getIsAvailable().intValue()==0) {
					availabilityStatus = 0;
				} else {
					availabilityStatus = 1;
				}
				JsonResourceAvailability shiftJsonResourceAvailability = jsonResourceAvailabilityShiftMap.get(resourceAvailability.getShiftTypeMaster().getShiftName());
				if (shiftJsonResourceAvailability != null) {
					int dayOfWeek = DateUtility.getDayOfWeek(resourceAvailability.getWorkDate());
					shiftJsonResourceAvailability = loadResourceDemand(shiftJsonResourceAvailability, availabilityStatus, dayOfWeek);
					jsonResourceAvailabilityShiftMap.put(shiftJsonResourceAvailability.getShiftName(), shiftJsonResourceAvailability);
				}
			}
		}
		
		jsonWorkPackageDemandProjections = new ArrayList<JsonResourceAvailability>(jsonResourceAvailabilityShiftMap.values());
		return jsonWorkPackageDemandProjections;
	}

	@Override
	@Transactional
	public HashMap<Integer,Integer> getUserAttendanceForMonth(int userId, String workDate) {
		log.info("getUserAttendanceForMonth ()   >>>>  "+"resource Id::: "+userId+"    workDate: "+workDate+" lenght: "+workDate.length());
		HashMap<Integer,Integer> mapOfUserAttendanceForAMonth= null;
		List<ResourceShiftCheckIn> resourceShiftCheckinList = null;
		try {
			Date dtMonthStartWorkDate = DateUtility.getFirstDateOfMonth(workDate);
			Date dtMonthEndWorkDate = DateUtility.getLastDateOfMonth(workDate);
			log.info("dtMonthStartWorkDate >>>>>> "+dtMonthStartWorkDate+"    dtMonthEndWorkDate>>>>>>: "+dtMonthEndWorkDate);
			
			List<TimeSheetEntryMaster> listOfTimeSheetEntriesOfShiftChInId = null;
			List<ResourceAvailability> resourceAvailabilityList = resourceAvailabilityDAO.getUserAttendanceForMonth(userId,dtMonthStartWorkDate,dtMonthEndWorkDate);
			if(resourceAvailabilityList != null && resourceAvailabilityList.size()>0){
				mapOfUserAttendanceForAMonth = new HashMap<Integer, Integer>();
				for (ResourceAvailability resourceAvailability : resourceAvailabilityList) {
					String strWorkDate = DateUtility.sdfDateformatWithOutTime(resourceAvailability.getWorkDate());
					log.info("strWorkDate: "+strWorkDate);
					Integer dateKey = new Integer(strWorkDate.substring(8, 10));
					if(resourceAvailability.getShiftAttendance()==1){
						mapOfUserAttendanceForAMonth.put(dateKey, 1);
						resourceShiftCheckinList = resourceShiftCheckinDAO.getResourceShiftCheckInByDate(resourceAvailability.getWorkDate(), userId);
						if(resourceShiftCheckinList != null && resourceShiftCheckinList.size()>0){
							long checkInCheckOutDurationInMinutes = 0;
							for (ResourceShiftCheckIn resShiftCheckIn : resourceShiftCheckinList) {
								if(resShiftCheckIn.getCheckOut() != null){
									checkInCheckOutDurationInMinutes = DateUtility.DateDifferenceInMinutes(resShiftCheckIn.getCheckIn(), resShiftCheckIn.getCheckOut());
									log.info("totalTimeConvertedInHoursMinutes for Shift Duration=" + checkInCheckOutDurationInMinutes);
									listOfTimeSheetEntriesOfShiftChInId = timeSheetManagementDAO.getTimeSheetEntriesOfResourceShiftCheckInId(resShiftCheckIn.getResourceShiftCheckInId());
									Integer totalTimesheetDuration = 0;
									if(listOfTimeSheetEntriesOfShiftChInId != null && listOfTimeSheetEntriesOfShiftChInId.size()>0){
										for (TimeSheetEntryMaster timeSheetEntryMaster : listOfTimeSheetEntriesOfShiftChInId) {
											Integer timeSheetDurationInHrsMins = DateUtility.convertTimeInMinutes(timeSheetEntryMaster.getHours(),timeSheetEntryMaster.getMins());
											totalTimesheetDuration = totalTimesheetDuration+timeSheetDurationInHrsMins;
										}
										if(checkInCheckOutDurationInMinutes == totalTimesheetDuration){
											mapOfUserAttendanceForAMonth.put(dateKey,3);
										}else{
											mapOfUserAttendanceForAMonth.put(dateKey,2);
										}
									}else{
										/// No time sheet Entries...
										mapOfUserAttendanceForAMonth.put(dateKey,2);
									}
								}else{
									checkInCheckOutDurationInMinutes = 0;
									mapOfUserAttendanceForAMonth.put(dateKey,2);
								}
							}
						}else{
							/// Only attendace marked. No resource shift check-ins
							mapOfUserAttendanceForAMonth.put(dateKey,1);
						}
					}else{
						////No attendance
						mapOfUserAttendanceForAMonth.put(dateKey, 0);
					}			
					
				}
			}
			
		} catch (Exception e) {
			log.error("ERROR  ",e);
		}
		log.debug("Result: "+mapOfUserAttendanceForAMonth);
		return mapOfUserAttendanceForAMonth;
	}
	
	@Override
	@Transactional
	public List<JsonResourceShiftCheckinDetailsForWeek> listUserTimeManagementSummary(int resourceId, int weekNo) {
		List<ShiftTypeMaster> shiftTypeMasterList =  resourceAvailabilityDAO.listShiftTypeMaster();
		Map<String, JsonResourceShiftCheckinDetailsForWeek> jsonResourceAvailabilityShiftMap = new HashMap<String, JsonResourceShiftCheckinDetailsForWeek>();
		JsonResourceShiftCheckinDetailsForWeek jsonResourceShiftCheckinDetailsForWeek = null;
		int counter = 1;
		for(ShiftTypeMaster shiftType : shiftTypeMasterList){
			jsonResourceShiftCheckinDetailsForWeek = new JsonResourceShiftCheckinDetailsForWeek();
			jsonResourceShiftCheckinDetailsForWeek.setResourceShiftCheckInId(counter++);
			jsonResourceShiftCheckinDetailsForWeek.setResourceId(resourceId);
			jsonResourceShiftCheckinDetailsForWeek.setShiftTypeId(shiftType.getShiftTypeId());
			jsonResourceShiftCheckinDetailsForWeek.setShiftTypeName(shiftType.getShiftName());
			
			jsonResourceAvailabilityShiftMap.put(jsonResourceShiftCheckinDetailsForWeek.getShiftTypeName(), jsonResourceShiftCheckinDetailsForWeek);
		}
		
		List<JsonResourceShiftCheckin> jsonResourceShiftCheckinList =  getResourceShiftCheckInForWeek(weekNo, resourceId, shiftTypeMasterList);
		
		List<JsonResourceShiftCheckinDetailsForWeek> jsonResourceShiftCheckinDetailsForWeekList = new ArrayList<JsonResourceShiftCheckinDetailsForWeek>();
	
		if(jsonResourceShiftCheckinList== null || jsonResourceShiftCheckinList.isEmpty()){
			
		} else {
			for (JsonResourceShiftCheckin jsonResourceCheckin : jsonResourceShiftCheckinList) {
				String shiftTypeName = jsonResourceCheckin.getShiftTypeName();
				JsonResourceShiftCheckinDetailsForWeek shiftJsonResourceAvailability = jsonResourceAvailabilityShiftMap.get(shiftTypeName);
				if (shiftJsonResourceAvailability != null) {
					int dayOfWeek = DateUtility.getDayOfWeek(DateUtility.dateFormatWithOutSeconds(jsonResourceCheckin.getCreatedDate()));
					boolean isAttendanceMarked = resourceAvailabilityDAO.isAttendanceMarkedForUserByDate(resourceId, DateUtility.dateFormatWithOutSeconds(jsonResourceCheckin.getCreatedDate()));
					shiftJsonResourceAvailability = loadResourceCheckIns(shiftJsonResourceAvailability, jsonResourceCheckin.getWorkDuration(), jsonResourceCheckin.getTimeSheetHours(), dayOfWeek,isAttendanceMarked);
					jsonResourceAvailabilityShiftMap.put(shiftTypeName, shiftJsonResourceAvailability);
				}
			}
		}
		jsonResourceShiftCheckinDetailsForWeekList = new ArrayList<JsonResourceShiftCheckinDetailsForWeek>(jsonResourceAvailabilityShiftMap.values());
		log.info("jsonResourceAvailabilityShiftMap >>>>>>>>>>>>>>>> "+jsonResourceAvailabilityShiftMap);
		log.info("JsonResourceShiftCheckinDetailsForWeek size>>>>>>>>>>>>>>>> "+jsonResourceShiftCheckinDetailsForWeekList.size());
		return jsonResourceShiftCheckinDetailsForWeekList;
	}
	
	public List<JsonResourceShiftCheckin> getResourceShiftCheckInForWeek(int weekNo, int resourceId,List<ShiftTypeMaster> shiftTypeMasterList){
		JsonResourceShiftCheckin jsonResourceShiftCheckin = null;
		List<JsonResourceShiftCheckin> jsonResourceShiftCheckinList = new ArrayList<JsonResourceShiftCheckin>();
		List<ResourceAvailability> listOfResourceAvailabilityForAWeek = new ArrayList<ResourceAvailability>();
		for(ShiftTypeMaster shiftType : shiftTypeMasterList){
			for(int dayCounter = 0; dayCounter <= 6 ; dayCounter++){
				Date workDate = DateUtility.getDateForDayOfWeek(weekNo,dayCounter);
				jsonResourceShiftCheckin = resourceShiftCheckinDAO.getResourceShiftCheckInsForAWeekDay(workDate, resourceId, shiftType.getShiftTypeId());
				List<ResourceShiftCheckIn> listOfResourceShiftCheckIns = resourceShiftCheckinDAO.getResourceShiftCheckInsForADay(workDate, resourceId, shiftType.getShiftTypeId());
				
				Integer totalTimesheetDuration = 0;
				if(listOfResourceShiftCheckIns != null && listOfResourceShiftCheckIns.size() >0){
					for (ResourceShiftCheckIn resourceShiftCheckIn : listOfResourceShiftCheckIns) {
						List<TimeSheetEntryMaster> listTimeSheetEntries = timeSheetManagementDAO.getTimeSheetEntriesOfResourceShiftCheckInId(resourceShiftCheckIn.getResourceShiftCheckInId());
						for (TimeSheetEntryMaster timeSheetEntryMaster : listTimeSheetEntries) {
							Integer timeSheetDurationInHrsMins = DateUtility.convertTimeInMinutes(timeSheetEntryMaster.getHours(),timeSheetEntryMaster.getMins());
							totalTimesheetDuration = totalTimesheetDuration+timeSheetDurationInHrsMins;
						}
					}
				} 
				if(jsonResourceShiftCheckin != null){
					String totalTimesheetHours = "00:00";
					totalTimesheetHours = DateUtility.convertTimeInHoursMinutes(0,totalTimesheetDuration);
					jsonResourceShiftCheckin.setTimeSheetHours(totalTimesheetHours);
					jsonResourceShiftCheckinList.add(jsonResourceShiftCheckin);
				}
			}
		}
		return jsonResourceShiftCheckinList;
	}
	
	private JsonResourceShiftCheckinDetailsForWeek loadResourceCheckIns(JsonResourceShiftCheckinDetailsForWeek jsonResourceShiftCheckinDetailsForWeek, String dayCheckInhours, String timeSheetHours, int dayOfWeek, boolean isAttendanceMarked) {
		log.info("dayOfWeek="+dayOfWeek);
		log.info("dayCheckInHours= "+dayCheckInhours+"  timeSheetHours  = "+timeSheetHours +"isAttendanceMarked: "+isAttendanceMarked);
		
		switch(dayOfWeek) {
			// Please dont change this ordering of case numbers
			case 1: // Sunday
				if(isAttendanceMarked){
					jsonResourceShiftCheckinDetailsForWeek.setDay7CheckInAndTimeSheetHours(dayCheckInhours+" / "+timeSheetHours);
				}else{
					jsonResourceShiftCheckinDetailsForWeek.setDay7CheckInAndTimeSheetHours("-");
				}
				break;
				
			case 2:  // Monday
				if(isAttendanceMarked){
					jsonResourceShiftCheckinDetailsForWeek.setDay1CheckInAndTimeSheetHours(dayCheckInhours+" / "+timeSheetHours);
				}else{
					jsonResourceShiftCheckinDetailsForWeek.setDay1CheckInAndTimeSheetHours("-");
				}
				break;
			case 3:
				jsonResourceShiftCheckinDetailsForWeek.setDay2CheckInAndTimeSheetHours(dayCheckInhours+" / "+timeSheetHours);
				break;
			case 4:
				jsonResourceShiftCheckinDetailsForWeek.setDay3CheckInAndTimeSheetHours(dayCheckInhours+" / "+timeSheetHours);
				break;
			case 5:
				jsonResourceShiftCheckinDetailsForWeek.setDay4CheckInAndTimeSheetHours(dayCheckInhours+" / "+timeSheetHours);
				break;
			case 6:
				jsonResourceShiftCheckinDetailsForWeek.setDay5CheckInAndTimeSheetHours(dayCheckInhours+" / "+timeSheetHours);
				break;
			case 7:
				jsonResourceShiftCheckinDetailsForWeek.setDay6CheckInAndTimeSheetHours(dayCheckInhours+" / "+timeSheetHours);
				break;
		}
	
		return jsonResourceShiftCheckinDetailsForWeek;
	}

	@Override
	@Transactional
	public List<TestFactoryResourceReservation> listTestFactoryResourceReservationByDate(
			Integer testFactoryLabId, Integer resourcePoolId,
			Integer shiftTypeId, Date workDate) {
		List<TestFactoryResourceReservation>  listOfTestFactoryResourceReservation = null;
		if(resourcePoolId == -1){
			List<TestfactoryResourcePool> resourcePoolList = testfactoryResourcePoolDAO.listResourcePoolbytestFactoryLabId(testFactoryLabId);
			if(resourcePoolList != null && resourcePoolList.size()>0){
				List<Integer> resourcePoolIdList = new ArrayList<Integer>();
				if (!(resourcePoolList == null || resourcePoolList.isEmpty())) {
					for (TestfactoryResourcePool rp : resourcePoolList) {
						resourcePoolIdList.add(rp.getResourcePoolId());
					}
				}	
				listOfTestFactoryResourceReservation = testFactoryResourceReservationDao.listTestFactoryResourceReservationByDate(testFactoryLabId,resourcePoolId,shiftTypeId,workDate,resourcePoolIdList);
			}
		}else{
			listOfTestFactoryResourceReservation = testFactoryResourceReservationDao.listTestFactoryResourceReservationByDate(testFactoryLabId,resourcePoolId,shiftTypeId,workDate,null);
		}
		return listOfTestFactoryResourceReservation;
	}

	@Override
	@Transactional
	public List<ResourceAvailability> listAttendanceByDate(
			Integer testFactoryLabId, Integer resourcePoolId,
			Integer shiftTypeId, Date selectedDate) {
		List<ResourceAvailability> listOfResourceAttendance = null;
		if(resourcePoolId == -1){
			List<TestfactoryResourcePool> resourcePoolList = testfactoryResourcePoolDAO.listResourcePoolbytestFactoryLabId(testFactoryLabId);
			if(resourcePoolList != null && resourcePoolList.size()>0){
				List<Integer> resourcePoolIdList = new ArrayList<Integer>();
				if (!(resourcePoolList == null || resourcePoolList.isEmpty())) {
					for (TestfactoryResourcePool rp : resourcePoolList) {
						resourcePoolIdList.add(rp.getResourcePoolId());
					}
				}	
				listOfResourceAttendance = resourceAvailabilityDAO.listAttendaceByDate(testFactoryLabId, resourcePoolId, shiftTypeId, selectedDate,resourcePoolIdList);
			}
		}else{
			listOfResourceAttendance = resourceAvailabilityDAO.listAttendaceByDate(testFactoryLabId, resourcePoolId, shiftTypeId, selectedDate,null);
		}
		return listOfResourceAttendance;
	}

	@Override
	@Transactional
	public List<JsonUserList> listUserbySpecificRoleByTestFactoryId(int testFactoryId, int productId) {
		
		List<UserList> listOfUsers = new ArrayList<UserList>();
		List<JsonUserList> jsonUserList = new ArrayList<JsonUserList>(0);
		List<TestfactoryResourcePool> listOfResourcePools = testFactoryDao.getResourcePoolListbyTestFactoryId(testFactoryId);
		if(listOfResourcePools.size() == 0){
			log.info("inside"+jsonUserList.size());
			return jsonUserList;
		}else if(listOfResourcePools != null && listOfResourcePools.size()>0){
			for (TestfactoryResourcePool testfactoryResourcePool : listOfResourcePools) {
				List<UserList> subListOfavailableUnReservedUsers = null;
				subListOfavailableUnReservedUsers = userListDAO.getUserListByRoleFromResourcePoolId(testfactoryResourcePool.getResourcePoolId());
				if(listOfUsers.size() == 0){
					listOfUsers = subListOfavailableUnReservedUsers;
				}else{
					listOfUsers.addAll(subListOfavailableUnReservedUsers);
				}
			}
		}
		
		if(listOfUsers.size()>0){
			for (UserList user : listOfUsers) {
				jsonUserList.add(new JsonUserList(user));
			}
		}else{
			jsonUserList=null;
			return jsonUserList;
		}
		return jsonUserList;
	}
	@Override
	public List<TestFactoryResourceReservation> getTestFactoryResourceReservationByDateShiftAndUser(int userId, int shiftId,Date tfreservationDate){
		return testFactoryResourceReservationDao.getTestFactoryResourceReservationByDateShiftAndUser(userId, shiftId,tfreservationDate);
	}

	@Override
	@Transactional
	public void updateAvailabilityForResourcesBulk(String[] resourceLists,
			String workDate, Integer shiftTypeId, Integer availabilityStatus) {
		ResourceAvailability resourceAvailability=null;
		try
		{
	    	
			if(availabilityStatus == 1){
				// Mark availability for user
				for(String resourceId : resourceLists){
		    		resourceAvailability = resourceAvailabilityDAO.getResourceAvailability(Integer.parseInt(resourceId), DateUtility.dateformatWithOutTime(workDate), shiftTypeId);
		    		if(resourceAvailability != null){
		    			if(resourceAvailability.getIsAvailable() == 0){
		    				resourceAvailability.setIsAvailable(1);
		    			}
		    			resourceAvailabilityDAO.updateResourceAvailability(resourceAvailability);
		    		}else{
		    			UserList user = userListDAO.getByUserId(Integer.parseInt(resourceId));
		    			ShiftTypeMaster shiftType = getShiftTypeMasterById(shiftTypeId);
		    			ResourceAvailability newResourceAvailability = new ResourceAvailability();
		    			newResourceAvailability.setWorkDate(DateUtility.dateformatWithOutTime(workDate));
		    			newResourceAvailability.setIsAvailable(1);
		    			newResourceAvailability.setBookForShift(0);
		    			newResourceAvailability.setResource(user);
		    			newResourceAvailability.setShiftTypeMaster(shiftType);
		    			resourceAvailabilityDAO.add(newResourceAvailability);
		    		}
		    	}
	    	}else if(availabilityStatus == 0){
	    		// If 0 - Remove Availability for the user.
	    		for(String resourceId : resourceLists){
		    		resourceAvailability = resourceAvailabilityDAO.getResourceAvailability(Integer.parseInt(resourceId), DateUtility.dateformatWithOutTime(workDate), shiftTypeId);
		    		if(resourceAvailability != null){
		    			if(resourceAvailability.getIsAvailable() == 1 && resourceAvailability.getBookForShift() == 1){
		    				resourceAvailability.setBookForShift(0);
		    				resourceAvailability.setIsAvailable(0);
		    			}else if(resourceAvailability.getIsAvailable() == 1 && resourceAvailability.getBookForShift() == 0){
		    				resourceAvailability.setIsAvailable(0);
		    			}
		    			resourceAvailabilityDAO.updateResourceAvailability(resourceAvailability);
		    		}else{
		    			// Do nothing
		    		}
		    	}
	    	}
	    	
		}catch (Exception e) {
			log.info("Problem in adding Plan details records", e);
			return;
		}
		
	}

	@Override
	@Transactional
	public String updateBookingForResourcesBulk(String[] resourceLists,
			String workDate, Integer shiftTypeId, Integer availabilityStatus) {
			log.info("availabilityStatus : "+availabilityStatus);
			ResourceAvailability resourceAvailability=null;
			List<UserList> unAvailableUserList = new ArrayList<UserList>();
			String message = "";
			try
			{
				if(availabilityStatus == 1){
					// Mark availability for user
					for(String resourceId : resourceLists){
			    		log.info("resourceId="+resourceId);
			    		resourceAvailability = resourceAvailabilityDAO.getResourceAvailability(Integer.parseInt(resourceId), DateUtility.dateformatWithOutTime(workDate), shiftTypeId);
			    		if(resourceAvailability != null){
			    			if(resourceAvailability.getIsAvailable() == 1 && (resourceAvailability.getBookForShift() == null || resourceAvailability.getBookForShift() == 0)){
			    				resourceAvailability.setBookForShift(1);
			    			}
			    			else if(resourceAvailability.getIsAvailable() == 0 && resourceAvailability.getBookForShift() == null || resourceAvailability.getBookForShift() == 0){
			    				///No action. Provided this conditions only for understanding purpose
			    				UserList user = userListDAO.getByUserId(Integer.parseInt(resourceId));
			    				unAvailableUserList.add(user);
			    			}
			    			resourceAvailabilityDAO.updateResourceAvailability(resourceAvailability);
			    		}else{
			    			// No action
			    		}
			    	}
		    	}else if(availabilityStatus == 0){
		    		// If 0 - Remove Availability for the user.
		    		for(String resourceId : resourceLists){
			    		log.info("resourceId="+resourceId);
			    		resourceAvailability = resourceAvailabilityDAO.getResourceAvailability(Integer.parseInt(resourceId), DateUtility.dateformatWithOutTime(workDate), shiftTypeId);
			    		if(resourceAvailability != null){
			    			if(resourceAvailability.getIsAvailable() == 1 && resourceAvailability.getBookForShift() == 1){
			    				resourceAvailability.setBookForShift(0);
			    			}else if(resourceAvailability.getIsAvailable() == 1 && resourceAvailability.getBookForShift() == 0){
			    				// No action
			    			}else {
			    				// No action
			    				
			    			}
			    			resourceAvailabilityDAO.updateResourceAvailability(resourceAvailability);
			    		}else{
			    			// Do nothing
			    		}
			    	}
		    	}
				if(unAvailableUserList != null && unAvailableUserList.size() > 0){
					String userNameList = "";
					for (UserList userList : unAvailableUserList) {
						if(userList != null){
							if(userNameList.length() == 0){
								userNameList = userList.getLoginId();
							}else{
								userNameList = userNameList+","+userList.getLoginId();
							}
						}
					}
					if(userNameList != null && userNameList.length()>0){
						message = "Not able to book the following resource(s), as they are not available "+userNameList;
					}
				}
		    	
			}catch (Exception e) {
				log.info("Problem in adding Plan details records", e);
			}
			return message;
	}

	@Override
	@Transactional
	public List<WorkPackage> getWorkPackageFromTestFactoryResourceReservationByserId(
			int userId, int shiftId, Date tfreservationDate) {
		return testFactoryResourceReservationDao.getWorkPackageFromTestFactoryResourceReservationByserId(userId, shiftId, tfreservationDate);
	}

	@Override
	@Transactional
	public List<ResourceAvailabilityDTO> getResourceAvailablityList(Date startDate, Date endDate, Integer testFactoryLabId,	Integer testFactoryId, Integer productId, Integer workPackageId,
			Integer shiftId, String viewType) {
		return resourceAvailabilityDAO.getResourceAvailablityList(startDate, endDate, testFactoryLabId, testFactoryId, productId, workPackageId, shiftId, viewType);
	}

	@Override
	@Transactional
	public List<TestFactoryResourceReservationDTO> getTestFactoryResourceReservationList(Date startDate, Date endDate, Integer testFactoryLabId,
			Integer testFactoryId, Integer productId, Integer workPackageId,Integer shiftId, String viewType) {
		return testFactoryResourceReservationDao.getTestFactoryResourceReservationList(startDate, endDate, testFactoryLabId, testFactoryId, productId, workPackageId,shiftId, viewType) ;
	}

	@Override
	@Transactional
	public TestfactoryResourcePool getTestFactoryResourcePoolByName(String testFactoryResourcePoolName) {
		return testfactoryResourcePoolDAO.getTestFactoryResourcePoolByName(testFactoryResourcePoolName);
	}

	@Override
	@Transactional
	public List<JsonUserList> getReservedResourcesWeeklyByRoleAndSkill(Integer workpackageId, Integer workWeek, Integer workYear, Integer shiftId, Integer userRoleId, Integer skillId,Integer userTypeId){
		List<JsonUserList> jsonBlockedUserList = null;
		List<UserList> productCoreResources = null;
		try{
			Integer productId = 0;
			Integer totalReservationPercentage = 0;
			List<TestFactoryResourceReservation> listOfResourceReserved = testFactoryResourceReservationDao.getReservedResourcesWeeklyByRoleAndSkill(workpackageId, workWeek, workYear, shiftId, userRoleId, skillId,userTypeId);
			if(listOfResourceReserved != null && listOfResourceReserved.size()>0){
				jsonBlockedUserList = new ArrayList<JsonUserList>();
				List<Integer> userIds = new ArrayList<Integer>();
				for (TestFactoryResourceReservation reservation : listOfResourceReserved) {
					if(userIds.contains(reservation.getBlockedUser().getUserId())){
						continue;
					}else{
						userIds.add(reservation.getBlockedUser().getUserId());
					}
					
					totalReservationPercentage = getTotalReservationPercentageByUserId(reservation.getBlockedUser().getUserId(),workWeek,workYear);
					JsonUserList jsonUser = new JsonUserList();
					jsonUser.setLoginId(reservation.getBlockedUser().getLoginId());
					jsonUser.setUserId(reservation.getBlockedUser().getUserId());
					jsonUser.setUserRoleId(reservation.getBlockedUser().getUserRoleMaster().getUserRoleId());
					jsonUser.setUserRoleLabel(reservation.getBlockedUser().getUserRoleMaster().getRoleLabel());
					jsonUser.setUserTypeLabel(reservation.getBlockedUser().getUserTypeMasterNew().getUserTypeLabel());
					jsonUser.setUserCode(reservation.getBlockedUser().getUserCode());
					jsonUser.setImageURI(reservation.getBlockedUser().getImageURI());
					Set<UserSkills> setOfUserSkills = reservation.getBlockedUser().getUserSkills();
					StringBuffer sb = jsonUser.getSkillsOfUser(setOfUserSkills);
					if(sb != null){
						jsonUser.setSkillName(sb);
					}
					jsonUser.setBooked("B");
					jsonUser.setAvailable("A");
					jsonUser.setReserve(new Integer(1));
					jsonUser.setReservationPercentage(reservation.getReservationPercentage());
					jsonUser.setTotalReservationPercentage(totalReservationPercentage);
					jsonUser.setReservationDetails(reservation.getReservationActionUser().getLoginId()+" / "+ reservation.getReservationActionDate()+" / "+reservation.getWorkPackage().getName());
					jsonBlockedUserList.add(jsonUser);
				}
				log.info("Available jsonUserList size: "+jsonBlockedUserList.size());
			}
			
		}catch(Exception ex){
			log.info("error fetching.. ",ex);
		}
		return jsonBlockedUserList;
	}

	
	
	
	@Override
	@Transactional
	public Integer getTotalReservationPercentageByUserId(Integer userId,Integer workWeek,Integer workYear){
		Integer totalPercentage = 0;
		try{
			totalPercentage = resourceAvailabilityDAO.getTotalReservationPercentageByUserId(userId,workWeek,workYear);
			totalPercentage = totalPercentage/5;
			
		}catch(Exception ex){
			log.error("Unable to fetch the data ", ex);
		}
		
		return totalPercentage;
	}
	
	
	
	@Override
	public TestFactoryResourceReservation saveReservedResourceForWeekly(Integer reservedorUnreservedUserId, Integer workWeek,Integer workYear, Integer workpackageId, Integer shiftId,Integer skillId,Integer userRoleId,
			Integer loggedInUserId,Long groupDemandId,Integer reservationPercentage,Integer userTypeId) {
		
		TestFactoryResourceReservation testFactoryResourceReservation = null;
		String result = "";
		try {
			log.info("Reserve resources for work Package: "+workpackageId+" for shift: "+shiftId+"   for week: "+workWeek);
			WorkPackage workPackage = workPackageDAO.getWorkPackageByIdWithMinimalnitialization(workpackageId);
			WorkShiftMaster workShiftMaster = workPackageDAO.getWorkShiftById(shiftId);
			UserList reservedUser = userListDAO.getByUserId(reservedorUnreservedUserId);
			UserList loggedInUser = userListDAO.getByUserId(loggedInUserId);
			Skill skill = new Skill();
			skill.setSkillId(skillId);
			
			UserRoleMaster userRole = new UserRoleMaster();
			userRole.setUserRoleId(userRoleId);
			
			UserTypeMasterNew userType = new UserTypeMasterNew();
			userType.setUserTypeId(userTypeId);
			/* */
			TestFactory testFactory = null;
			
			testFactory= testFactoryDao.getTestFactoryById(workPackage.getProductBuild().getProductVersion().getProductMaster().getTestFactory().getTestFactoryId());
			
			
			List<TestfactoryResourcePool> listOfResourcePools = testFactoryDao.getResourcePoolListbyTestFactoryId(testFactory.getTestFactoryId());
			if((listOfResourcePools == null) || (listOfResourcePools.size() <= 0)){
				log.info("There are no resource pools mapped to this Test Factory. Hence cannot show resources for reservation : " + testFactory.getDisplayName());
				return null;
			}else if(listOfResourcePools != null && listOfResourcePools.size()>0){
				for (TestfactoryResourcePool testfactoryResourcePool : listOfResourcePools) {
					
					boolean value = checkResourceAssignedToRPforWeek(reservedorUnreservedUserId,workWeek,workYear,testfactoryResourcePool.getResourcePoolId());
					if(value == false){
						continue;
					}else{
						
						List<WorkPackageDemandProjection> workPackageDemandProjections = resourceAvailabilityDAO.getWorkpackageDemandProjection(workpackageId, shiftId, skillId, userRoleId, workWeek, workYear,userTypeId);	
						
						if(workPackageDemandProjections != null && workPackageDemandProjections.size() > 0){
							groupDemandId = workPackageDemandProjections.get(0).getGroupDemandId();
						}
						
							testFactoryResourceReservation = new TestFactoryResourceReservation();
							testFactoryResourceReservation.setBlockedUser(reservedUser);
							testFactoryResourceReservation.setWorkPackage(workPackage);
							testFactoryResourceReservation.setShift(workShiftMaster);
							testFactoryResourceReservation.setReservationWeek(workWeek);
							testFactoryResourceReservation.setReservationYear(workYear);
							testFactoryResourceReservation.setReservationActionUser(loggedInUser);
							testFactoryResourceReservation.setSkill(skill);
							testFactoryResourceReservation.setUserRole(userRole);
							testFactoryResourceReservation.setReservationActionDate(new Date(System.currentTimeMillis()));
							testFactoryResourceReservation.setReservationMode("Weekly");
							testFactoryResourceReservation.setGroupReservationId(groupDemandId);
							testFactoryResourceReservation.setReservationPercentage(reservationPercentage);
							testFactoryResourceReservation.setUserType(userType);
							testFactoryResourceReservation.setResourcePool(testfactoryResourcePool);
							
							Date startDate = DateUtility.getWeekStartDateFromJanMonday(workWeek,workYear);
							Calendar startCalendar = Calendar.getInstance();
							startCalendar.setTime(startDate);
							
							Calendar endCalendar = Calendar.getInstance();
							endCalendar.setTime(startDate);
							endCalendar.add(Calendar.DAY_OF_MONTH, 4);
							endCalendar.set(Calendar.HOUR_OF_DAY, 23);
							endCalendar.set(Calendar.MINUTE, 59);
							endCalendar.set(Calendar.SECOND, 59);
							
							Date endDate = endCalendar.getTime();
							Integer isRecordAdded = 0;
							while(startDate.before(endDate)){
								testFactoryResourceReservation.setReservationDate(startDate);
								testFactoryResourceReservation.setResourceReservationId(null);
								isRecordAdded = testFactoryResourceReservationDao.saveBlockedResource(testFactoryResourceReservation);
								if(testFactoryResourceReservation != null){
									mongoDBService.addReseveredResourceToMongoDB(testFactoryResourceReservation.getResourceReservationId());
								}
								
								startCalendar.add(Calendar.DAY_OF_MONTH, 1);
								startDate = startCalendar.getTime();
							}
						break;
					}
					
					
					
				}
				
			}
			
			
		
		} catch (Exception e) {
			log.error("error occured while saving resource reservation",e);
			result = e.getMessage();
		}
		return testFactoryResourceReservation;
		
	}

	

	@Override
	@Transactional
	public List<TestFactoryResourceReservation> removeReservationForResourceForWeekly(Integer reservedorUnreservedUserId, Integer workWeek,Integer workYear, Integer workpackageId, Integer shiftId,
			Integer loggedInUserId,Long groupDemandId,Integer userTypeId) {
		List<TestFactoryResourceReservation> testFactoryResourceReservations = null;
		
		try {
			log.info("unBlockedUserId: "+reservedorUnreservedUserId);
			UserList unReservedUser = userListDAO.getByUserId(reservedorUnreservedUserId);
				
			testFactoryResourceReservations = testFactoryResourceReservationDao.getTestFactoryResourceReservationByUsingWeek(workpackageId, shiftId, workWeek,workYear, unReservedUser.getUserId(), loggedInUserId, 0,groupDemandId,userTypeId);
				
				if(testFactoryResourceReservations != null){
					for(TestFactoryResourceReservation testFactoryResourceReservation : testFactoryResourceReservations ){
						testFactoryResourceReservationDao.removeUnblockedResources(testFactoryResourceReservation);
						
						if(testFactoryResourceReservation != null && testFactoryResourceReservation.getResourceReservationId() != null){
							mongoDBService.deleteReseveredResourceFromMongoDb(testFactoryResourceReservation.getResourceReservationId());
						}
						
					}
					log.info("Cancelled Reservation for user : "+unReservedUser.getLoginId()+" for shift : "+shiftId+" for workPackage Id: "+workpackageId+"  for week: "+workWeek);
				}else{
					log.info("Reservation cancellation not needed (as no prior reservation exists) for user : "+unReservedUser.getLoginId()+" for shift : "+shiftId+" for workPackage Id: "+workpackageId+"  for week: "+workWeek);
				}
		} catch (Exception e) {
			log.error("error occured while removing resource reservation",e);
		}
		return testFactoryResourceReservations;
	}

	@Override
	@Transactional
	public void updateReservedResourcePercentage(Integer workpackageId,Integer recursiveWeek, Integer workYear, Integer reserveShiftId,Integer reserveSkillId, Integer reservationPercentage,Integer userId, Integer reserveUserRoleId,Integer userTypeId) {
		testFactoryResourceReservationDao.updateReservedResourcePercentage(workpackageId,recursiveWeek, workYear, reserveShiftId, reserveSkillId,reservationPercentage,userId,reserveUserRoleId,userTypeId);
		
	}

	@Override
	public List<JsonWorkPackageWeeklyResourceReservation> getResourceAllocationWeekly(Integer labId, Integer resourcePoolId, Integer testFactoryId, Integer productId, Integer workpackageId, Integer allocationYear,Integer userId,Integer utilizationRange, List<Integer> allocationWeekList) {
		List<JsonWorkPackageWeeklyResourceReservation> jsonWorkPackageWeeklyResourceReservations = new ArrayList<JsonWorkPackageWeeklyResourceReservation>();
		List<JsonWorkPackageWeeklyResourceReservation> jsonWorkPackageWeeklyResourceReservationsFiltered = new ArrayList<JsonWorkPackageWeeklyResourceReservation>();
		try{
			List<WeeklyResourceReservationDTO> weeklyResourceReservationDTOs = resourceAvailabilityDAO.listResourcePoolResourceReservationWeeklyProjection(labId, resourcePoolId, testFactoryId, productId, workpackageId, allocationYear, userId, allocationWeekList);
			List<UserList> userList = new ArrayList<UserList>();
			
			WorkPackage workPackage = null;
			ProductMaster productMaster = null;
			TestFactory testFactory = null;
			
			Set<Integer> resourcePoolIds = new HashSet<Integer>();
			if(resourcePoolId != null && resourcePoolId != 0){
				resourcePoolIds.add(resourcePoolId);
			}
			
			if(workpackageId != null && workpackageId != 0){
				workPackage = workPackageDAO.getWorkPackageById(workpackageId);
				productMaster = workPackage.getProductBuild().getProductVersion().getProductMaster();
			}
			
			if(productId != null && productId != 0 && productMaster == null){
				productMaster = productMasterDAO.getProductDetailsById(productId);
			}
			if(productMaster != null){
				testFactory = productMaster.getTestFactory();
			}

			if(testFactoryId != null && testFactoryId != 0 && testFactory == null){
				testFactory = testFactoryDao.getTestFactoryById(testFactoryId);
			}
			
			if(testFactory != null){
				Set<TestfactoryResourcePool> testfactoryResourcePools = testFactory.getTestfactoryResourcePoolList();
				for(TestfactoryResourcePool loopTestfactoryResourcePool : testfactoryResourcePools){
					resourcePoolIds.add(loopTestfactoryResourcePool.getResourcePoolId());
				}
			}
			
			if(labId != null && labId != 0){
				List<TestfactoryResourcePool> testfactoryResourcePools = testfactoryResourcePoolDAO.listResourcePoolByTestFactoryLabId(labId);
				if(testfactoryResourcePools != null && testfactoryResourcePools.size() > 0){
					for(TestfactoryResourcePool testfactoryResourcePool : testfactoryResourcePools){
						resourcePoolIds.add(testfactoryResourcePool.getResourcePoolId());
					}
				}
			}
			
			Calendar startDate = Calendar.getInstance();
			startDate.set(Calendar.DAY_OF_MONTH, 1);
			startDate.set(Calendar.MONTH, Calendar.JANUARY);
			startDate.set(Calendar.YEAR, allocationYear);
			startDate.set(Calendar.HOUR_OF_DAY, 0);
			startDate.set(Calendar.MINUTE, 0);
			startDate.set(Calendar.SECOND, 0);
			
			Calendar endDate = (Calendar) startDate.clone();
			endDate.add(Calendar.YEAR, 1);
			
			List<UserList> users = null;
			
			if(userId == null || userId <= 0){
				if(resourcePoolIds != null && resourcePoolIds.size() > 0){
					for(Integer loopResourcePoolId : resourcePoolIds){
						users = userListDAO.getUserListMappedToResourcePool(loopResourcePoolId, startDate.getTime(), endDate.getTime());
						if(users != null && users.size() > 0){
							for(UserList user : users){
								if(user.getResourcePool().getResourcePoolId() != loopResourcePoolId){
									TestfactoryResourcePool testfactoryResourcePool = testfactoryResourcePoolDAO.getbyresourcePoolId(loopResourcePoolId);
									user.setResourcePool(testfactoryResourcePool);
								}
							}
							userList.addAll(users);
						}
					}
				}
			}else{
				List<UserResourcePoolMapping> userResourcePoolMappings = userListDAO.getUserResourcePoolMappingByFilter(userId, null,startDate.getTime(), endDate.getTime());
				List<String> userAndRPIds = new ArrayList<String>();
				for(UserResourcePoolMapping userResourcePoolMapping :userResourcePoolMappings ){
					if(!userAndRPIds.contains(userResourcePoolMapping.getUserId().getUserId()+"--"+userResourcePoolMapping.getResourcepoolId().getResourcePoolId())){
						UserList user = new UserList(userResourcePoolMapping.getUserId());
						user.setResourcePool(userResourcePoolMapping.getResourcepoolId());
						userList.add(user);
						userAndRPIds.add(userResourcePoolMapping.getUserId().getUserId()+"--"+userResourcePoolMapping.getResourcepoolId().getResourcePoolId());
					}
				}
			}
			
			if(userList ==  null || userList.size() == 0){
				return jsonWorkPackageWeeklyResourceReservations;
			}
			
			JsonWorkPackageWeeklyResourceReservation jsonWorkPackageWeeklyResourceReservationTotal = new JsonWorkPackageWeeklyResourceReservation();
			for(UserList user : userList){
				JsonWorkPackageWeeklyResourceReservation jsonWorkPackageWeeklyResourceReservation= new JsonWorkPackageWeeklyResourceReservation();
				List<UserResourcePoolMapping> userResourcePoolMappings = userListDAO.getUserResourcePoolMappingByFilter(user.getUserId(), user.getResourcePool().getResourcePoolId(),startDate.getTime(), endDate.getTime());
				if(userResourcePoolMappings != null && userResourcePoolMappings.size() > 0){
					for(UserResourcePoolMapping userResourcePoolMapping : userResourcePoolMappings){
						Calendar weekCalendar = Calendar.getInstance();
						weekCalendar.setFirstDayOfWeek(Calendar.MONDAY);
						weekCalendar.setTime(userResourcePoolMapping.getFromDate());
						if(weekCalendar.get(Calendar.DAY_OF_WEEK) < Calendar.MONDAY){
							weekCalendar.add(Calendar.DATE, 1);
						}else if(weekCalendar.get(Calendar.DAY_OF_WEEK) > Calendar.MONDAY){
							weekCalendar.add(Calendar.DATE, ((Calendar.SATURDAY - weekCalendar.get(Calendar.DAY_OF_WEEK) + 2)));
						}
						
						while(weekCalendar.getTime().before(userResourcePoolMapping.getToDate()) || weekCalendar.getTime().equals(userResourcePoolMapping.getToDate())){
							if(weekCalendar.get(Calendar.DAY_OF_WEEK) == Calendar.MONDAY){
								Integer weekNoOfDate = DateUtility.getWeekNumberOfDate(weekCalendar.getTime());
								if(!jsonWorkPackageWeeklyResourceReservation.getReservedWeeks().contains(weekNoOfDate)){
									jsonWorkPackageWeeklyResourceReservation.getReservedWeeks().add(weekNoOfDate);
								}
							}
							weekCalendar.add(Calendar.WEEK_OF_YEAR, 1);
							
						}
					}
				}
				
				jsonWorkPackageWeeklyResourceReservation.setUserId(user.getUserId());
				jsonWorkPackageWeeklyResourceReservation.setUserName(user.getLoginId());
				jsonWorkPackageWeeklyResourceReservation.setUserRoleId(user.getUserRoleMaster().getUserRoleId());
				jsonWorkPackageWeeklyResourceReservation.setUserRoleName(user.getUserRoleMaster().getRoleLabel());
				jsonWorkPackageWeeklyResourceReservation.setReservationYear(allocationYear);
				jsonWorkPackageWeeklyResourceReservation.setResourcePoolId(user.getResourcePool().getResourcePoolId());
				jsonWorkPackageWeeklyResourceReservation.setResourcePoolName(user.getResourcePool().getResourcePoolName());
				jsonWorkPackageWeeklyResourceReservation.setUserCode(user.getUserCode());
				jsonWorkPackageWeeklyResourceReservation.setUserTypeId(user.getUserTypeMasterNew().getUserTypeId());
				jsonWorkPackageWeeklyResourceReservation.setUserTypeName(user.getUserTypeMasterNew().getUserTypeLabel());
				
				jsonWorkPackageWeeklyResourceReservations.add(jsonWorkPackageWeeklyResourceReservation);
			}
			
			for (WeeklyResourceReservationDTO weeklyResourceReservationDTO : weeklyResourceReservationDTOs) {
				
				JsonWorkPackageWeeklyResourceReservation jsonWorkPackageWeeklyResourceReservation = getJsonWorkPackageWeeklyResourceReservationForResourcePoolId(jsonWorkPackageWeeklyResourceReservations, weeklyResourceReservationDTO); 
				if (jsonWorkPackageWeeklyResourceReservation == null) {
					
					jsonWorkPackageWeeklyResourceReservation = new JsonWorkPackageWeeklyResourceReservation();
					
					jsonWorkPackageWeeklyResourceReservation.setWorkPackageId(weeklyResourceReservationDTO.getWorkPackageId());
					jsonWorkPackageWeeklyResourceReservation.setWorkPackageName(weeklyResourceReservationDTO.getWorkPackageName());
					
					jsonWorkPackageWeeklyResourceReservation.setUserId(weeklyResourceReservationDTO.getUserId());
					jsonWorkPackageWeeklyResourceReservation.setUserName(weeklyResourceReservationDTO.getUserName());
					
					jsonWorkPackageWeeklyResourceReservation.setUserRoleId(weeklyResourceReservationDTO.getUserRoleId());
					jsonWorkPackageWeeklyResourceReservation.setUserRoleName(weeklyResourceReservationDTO.getUserRoleName());
					
					jsonWorkPackageWeeklyResourceReservation.setReservationWeek(weeklyResourceReservationDTO.getReservationWeek());
					jsonWorkPackageWeeklyResourceReservation.setReservationYear(weeklyResourceReservationDTO.getReservationYear());
					
					jsonWorkPackageWeeklyResourceReservation.setUserCode(weeklyResourceReservationDTO.getUserCode());
					
					jsonWorkPackageWeeklyResourceReservation.setUserTypeId(weeklyResourceReservationDTO.getUserTypeId());
					jsonWorkPackageWeeklyResourceReservation.setUserTypeName(weeklyResourceReservationDTO.getUserTypeName());
					
					jsonWorkPackageWeeklyResourceReservation.setResourcePoolId(weeklyResourceReservationDTO.getResourcePoolId());
					jsonWorkPackageWeeklyResourceReservation.setResourcePoolName(weeklyResourceReservationDTO.getResourcePoolName());
					
					jsonWorkPackageWeeklyResourceReservations.add(jsonWorkPackageWeeklyResourceReservation);
				}
				
				Integer reservationWeekNo = weeklyResourceReservationDTO.getReservationWeek();
				if(reservationWeekNo == null){
					reservationWeekNo = 0;
				}
				
				float weeklyResourceReservationPercentage = weeklyResourceReservationDTO.getReservationPercentage();
				weeklyResourceReservationPercentage = weeklyResourceReservationPercentage / 100;
				if(jsonWorkPackageWeeklyResourceReservation.getAllWeeks() != null){
					jsonWorkPackageWeeklyResourceReservation.setAllWeeks(weeklyResourceReservationPercentage + jsonWorkPackageWeeklyResourceReservation.getAllWeeks());
				}
				
				if(!jsonWorkPackageWeeklyResourceReservation.getReservedWeeks().contains(reservationWeekNo)){
					jsonWorkPackageWeeklyResourceReservation.getReservedWeeks().add(reservationWeekNo);
				}
				
				switch (reservationWeekNo) {
					case 1:
						if(jsonWorkPackageWeeklyResourceReservation.getWeek1() != null){
							weeklyResourceReservationPercentage = weeklyResourceReservationPercentage + jsonWorkPackageWeeklyResourceReservation.getWeek1();
						}
						jsonWorkPackageWeeklyResourceReservation.setWeek1(weeklyResourceReservationPercentage);	
						jsonWorkPackageWeeklyResourceReservationTotal.setWeek1(jsonWorkPackageWeeklyResourceReservationTotal.getWeek1() + weeklyResourceReservationPercentage);
						break;
					case 2:
						if(jsonWorkPackageWeeklyResourceReservation.getWeek2() != null){
							weeklyResourceReservationPercentage = weeklyResourceReservationPercentage + jsonWorkPackageWeeklyResourceReservation.getWeek2();
						}
						jsonWorkPackageWeeklyResourceReservation.setWeek2(weeklyResourceReservationPercentage);
						jsonWorkPackageWeeklyResourceReservationTotal.setWeek2(jsonWorkPackageWeeklyResourceReservationTotal.getWeek2() + weeklyResourceReservationPercentage);
						break;
					case 3:
						if(jsonWorkPackageWeeklyResourceReservation.getWeek3() != null){
							weeklyResourceReservationPercentage = weeklyResourceReservationPercentage + jsonWorkPackageWeeklyResourceReservation.getWeek3();
						}
						jsonWorkPackageWeeklyResourceReservation.setWeek3(weeklyResourceReservationPercentage);	
						jsonWorkPackageWeeklyResourceReservationTotal.setWeek3(jsonWorkPackageWeeklyResourceReservationTotal.getWeek3() + weeklyResourceReservationPercentage);
						break;
					case 4:
						if(jsonWorkPackageWeeklyResourceReservation.getWeek4() != null){
							weeklyResourceReservationPercentage = weeklyResourceReservationPercentage + jsonWorkPackageWeeklyResourceReservation.getWeek4();
						}
						jsonWorkPackageWeeklyResourceReservation.setWeek4(weeklyResourceReservationPercentage);	
						jsonWorkPackageWeeklyResourceReservationTotal.setWeek4(jsonWorkPackageWeeklyResourceReservationTotal.getWeek4() + weeklyResourceReservationPercentage);
						break;
					case 5:
						if(jsonWorkPackageWeeklyResourceReservation.getWeek5() != null){
							weeklyResourceReservationPercentage = weeklyResourceReservationPercentage + jsonWorkPackageWeeklyResourceReservation.getWeek5();
						}
						jsonWorkPackageWeeklyResourceReservation.setWeek5(weeklyResourceReservationPercentage);	
						jsonWorkPackageWeeklyResourceReservationTotal.setWeek5(jsonWorkPackageWeeklyResourceReservationTotal.getWeek5() + weeklyResourceReservationPercentage);
						break;
					case 6:
						if(jsonWorkPackageWeeklyResourceReservation.getWeek6() != null){
							weeklyResourceReservationPercentage = weeklyResourceReservationPercentage + jsonWorkPackageWeeklyResourceReservation.getWeek6();
						}
						jsonWorkPackageWeeklyResourceReservation.setWeek6(weeklyResourceReservationPercentage);	
						jsonWorkPackageWeeklyResourceReservationTotal.setWeek6(jsonWorkPackageWeeklyResourceReservationTotal.getWeek6() + weeklyResourceReservationPercentage);
						break;
					case 7:
						if(jsonWorkPackageWeeklyResourceReservation.getWeek7() != null){
							weeklyResourceReservationPercentage = weeklyResourceReservationPercentage + jsonWorkPackageWeeklyResourceReservation.getWeek7();
						}
						jsonWorkPackageWeeklyResourceReservation.setWeek7(weeklyResourceReservationPercentage);	
						jsonWorkPackageWeeklyResourceReservationTotal.setWeek7(jsonWorkPackageWeeklyResourceReservationTotal.getWeek7() + weeklyResourceReservationPercentage);
						break;
					case 8:
						if(jsonWorkPackageWeeklyResourceReservation.getWeek8() != null){
							weeklyResourceReservationPercentage = weeklyResourceReservationPercentage + jsonWorkPackageWeeklyResourceReservation.getWeek8();
						}
						jsonWorkPackageWeeklyResourceReservation.setWeek8(weeklyResourceReservationPercentage);	
						jsonWorkPackageWeeklyResourceReservationTotal.setWeek8(jsonWorkPackageWeeklyResourceReservationTotal.getWeek8() + weeklyResourceReservationPercentage);
						break;
					case 9:
						if(jsonWorkPackageWeeklyResourceReservation.getWeek9() != null){
							weeklyResourceReservationPercentage = weeklyResourceReservationPercentage + jsonWorkPackageWeeklyResourceReservation.getWeek9();
						}
						jsonWorkPackageWeeklyResourceReservation.setWeek9(weeklyResourceReservationPercentage);	
						jsonWorkPackageWeeklyResourceReservationTotal.setWeek9(jsonWorkPackageWeeklyResourceReservationTotal.getWeek9() + weeklyResourceReservationPercentage);
						break;
					case 10:
						if(jsonWorkPackageWeeklyResourceReservation.getWeek10() != null){
							weeklyResourceReservationPercentage = weeklyResourceReservationPercentage + jsonWorkPackageWeeklyResourceReservation.getWeek10();
						}
						jsonWorkPackageWeeklyResourceReservation.setWeek10(weeklyResourceReservationPercentage);	
						jsonWorkPackageWeeklyResourceReservationTotal.setWeek10(jsonWorkPackageWeeklyResourceReservationTotal.getWeek10() + weeklyResourceReservationPercentage);
						break;
					case 11:
						if(jsonWorkPackageWeeklyResourceReservation.getWeek11() != null){
							weeklyResourceReservationPercentage = weeklyResourceReservationPercentage + jsonWorkPackageWeeklyResourceReservation.getWeek11();
						}
						jsonWorkPackageWeeklyResourceReservation.setWeek11(weeklyResourceReservationPercentage);	
						jsonWorkPackageWeeklyResourceReservationTotal.setWeek11(jsonWorkPackageWeeklyResourceReservationTotal.getWeek11() + weeklyResourceReservationPercentage);
						break;
					case 12:
						if(jsonWorkPackageWeeklyResourceReservation.getWeek12() != null){
							weeklyResourceReservationPercentage = weeklyResourceReservationPercentage + jsonWorkPackageWeeklyResourceReservation.getWeek12();
						}
						jsonWorkPackageWeeklyResourceReservation.setWeek12(weeklyResourceReservationPercentage);	
						jsonWorkPackageWeeklyResourceReservationTotal.setWeek12(jsonWorkPackageWeeklyResourceReservationTotal.getWeek12() + weeklyResourceReservationPercentage);
						break;
					case 13:
						if(jsonWorkPackageWeeklyResourceReservation.getWeek13() != null){
							weeklyResourceReservationPercentage = weeklyResourceReservationPercentage + jsonWorkPackageWeeklyResourceReservation.getWeek13();
						}
						jsonWorkPackageWeeklyResourceReservation.setWeek13(weeklyResourceReservationPercentage);	
						jsonWorkPackageWeeklyResourceReservationTotal.setWeek13(jsonWorkPackageWeeklyResourceReservationTotal.getWeek13() + weeklyResourceReservationPercentage);
						break;
					case 14:
						if(jsonWorkPackageWeeklyResourceReservation.getWeek14() != null){
							weeklyResourceReservationPercentage = weeklyResourceReservationPercentage + jsonWorkPackageWeeklyResourceReservation.getWeek14();
						}
						jsonWorkPackageWeeklyResourceReservation.setWeek14(weeklyResourceReservationPercentage);	
						jsonWorkPackageWeeklyResourceReservationTotal.setWeek14(jsonWorkPackageWeeklyResourceReservationTotal.getWeek14() + weeklyResourceReservationPercentage);
						break;
					case 15:
						if(jsonWorkPackageWeeklyResourceReservation.getWeek15() != null){
							weeklyResourceReservationPercentage = weeklyResourceReservationPercentage + jsonWorkPackageWeeklyResourceReservation.getWeek15();
						}
						jsonWorkPackageWeeklyResourceReservation.setWeek15(weeklyResourceReservationPercentage);	
						jsonWorkPackageWeeklyResourceReservationTotal.setWeek15(jsonWorkPackageWeeklyResourceReservationTotal.getWeek15() + weeklyResourceReservationPercentage);
						break;
					case 16:
						if(jsonWorkPackageWeeklyResourceReservation.getWeek16() != null){
							weeklyResourceReservationPercentage = weeklyResourceReservationPercentage + jsonWorkPackageWeeklyResourceReservation.getWeek16();
						}
						jsonWorkPackageWeeklyResourceReservation.setWeek16(weeklyResourceReservationPercentage);	
						jsonWorkPackageWeeklyResourceReservationTotal.setWeek16(jsonWorkPackageWeeklyResourceReservationTotal.getWeek16() + weeklyResourceReservationPercentage);
						break;
					case 17:
						if(jsonWorkPackageWeeklyResourceReservation.getWeek17() != null){
							weeklyResourceReservationPercentage = weeklyResourceReservationPercentage + jsonWorkPackageWeeklyResourceReservation.getWeek17();
						}
						jsonWorkPackageWeeklyResourceReservation.setWeek17(weeklyResourceReservationPercentage);	
						jsonWorkPackageWeeklyResourceReservationTotal.setWeek17(jsonWorkPackageWeeklyResourceReservationTotal.getWeek17() + weeklyResourceReservationPercentage);
						break;
					case 18:
						if(jsonWorkPackageWeeklyResourceReservation.getWeek18() != null){
							weeklyResourceReservationPercentage = weeklyResourceReservationPercentage + jsonWorkPackageWeeklyResourceReservation.getWeek18();
						}
						jsonWorkPackageWeeklyResourceReservation.setWeek18(weeklyResourceReservationPercentage);	
						jsonWorkPackageWeeklyResourceReservationTotal.setWeek18(jsonWorkPackageWeeklyResourceReservationTotal.getWeek18() + weeklyResourceReservationPercentage);
						break;
					case 19:
						if(jsonWorkPackageWeeklyResourceReservation.getWeek19() != null){
							weeklyResourceReservationPercentage = weeklyResourceReservationPercentage + jsonWorkPackageWeeklyResourceReservation.getWeek19();
						}
						jsonWorkPackageWeeklyResourceReservation.setWeek19(weeklyResourceReservationPercentage);	
						jsonWorkPackageWeeklyResourceReservationTotal.setWeek19(jsonWorkPackageWeeklyResourceReservationTotal.getWeek19() + weeklyResourceReservationPercentage);
						break;
					case 20:
						if(jsonWorkPackageWeeklyResourceReservation.getWeek20() != null){
							weeklyResourceReservationPercentage = weeklyResourceReservationPercentage + jsonWorkPackageWeeklyResourceReservation.getWeek20();
						}
						jsonWorkPackageWeeklyResourceReservation.setWeek20(weeklyResourceReservationPercentage);	
						jsonWorkPackageWeeklyResourceReservationTotal.setWeek20(jsonWorkPackageWeeklyResourceReservationTotal.getWeek20() + weeklyResourceReservationPercentage);
						break;
					case 21:
						if(jsonWorkPackageWeeklyResourceReservation.getWeek21() != null){
							weeklyResourceReservationPercentage = weeklyResourceReservationPercentage + jsonWorkPackageWeeklyResourceReservation.getWeek21();
						}
						jsonWorkPackageWeeklyResourceReservation.setWeek21(weeklyResourceReservationPercentage);	
						jsonWorkPackageWeeklyResourceReservationTotal.setWeek21(jsonWorkPackageWeeklyResourceReservationTotal.getWeek21() + weeklyResourceReservationPercentage);
						break;
					case 22:
						if(jsonWorkPackageWeeklyResourceReservation.getWeek22() != null){
							weeklyResourceReservationPercentage = weeklyResourceReservationPercentage + jsonWorkPackageWeeklyResourceReservation.getWeek22();
						}
						jsonWorkPackageWeeklyResourceReservation.setWeek22(weeklyResourceReservationPercentage);	
						jsonWorkPackageWeeklyResourceReservationTotal.setWeek22(jsonWorkPackageWeeklyResourceReservationTotal.getWeek22() + weeklyResourceReservationPercentage);
						break;
					case 23:
						if(jsonWorkPackageWeeklyResourceReservation.getWeek23() != null){
							weeklyResourceReservationPercentage = weeklyResourceReservationPercentage + jsonWorkPackageWeeklyResourceReservation.getWeek23();
						}
						jsonWorkPackageWeeklyResourceReservation.setWeek23(weeklyResourceReservationPercentage);	
						jsonWorkPackageWeeklyResourceReservationTotal.setWeek23(jsonWorkPackageWeeklyResourceReservationTotal.getWeek23() + weeklyResourceReservationPercentage);
						break;
					case 24:
						if(jsonWorkPackageWeeklyResourceReservation.getWeek24() != null){
							weeklyResourceReservationPercentage = weeklyResourceReservationPercentage + jsonWorkPackageWeeklyResourceReservation.getWeek24();
						}
						jsonWorkPackageWeeklyResourceReservation.setWeek24(weeklyResourceReservationPercentage);	
						jsonWorkPackageWeeklyResourceReservationTotal.setWeek24(jsonWorkPackageWeeklyResourceReservationTotal.getWeek24() + weeklyResourceReservationPercentage);
						break;
					case 25:
						if(jsonWorkPackageWeeklyResourceReservation.getWeek25() != null){
							weeklyResourceReservationPercentage = weeklyResourceReservationPercentage + jsonWorkPackageWeeklyResourceReservation.getWeek25();
						}
						jsonWorkPackageWeeklyResourceReservation.setWeek25(weeklyResourceReservationPercentage);	
						jsonWorkPackageWeeklyResourceReservationTotal.setWeek25(jsonWorkPackageWeeklyResourceReservationTotal.getWeek25() + weeklyResourceReservationPercentage);
						break;
					case 26:
						if(jsonWorkPackageWeeklyResourceReservation.getWeek26() != null){
							weeklyResourceReservationPercentage = weeklyResourceReservationPercentage + jsonWorkPackageWeeklyResourceReservation.getWeek26();
						}
						jsonWorkPackageWeeklyResourceReservation.setWeek26(weeklyResourceReservationPercentage);	
						jsonWorkPackageWeeklyResourceReservationTotal.setWeek26(jsonWorkPackageWeeklyResourceReservationTotal.getWeek26() + weeklyResourceReservationPercentage);
						break;
					case 27:
						if(jsonWorkPackageWeeklyResourceReservation.getWeek27() != null){
							weeklyResourceReservationPercentage = weeklyResourceReservationPercentage + jsonWorkPackageWeeklyResourceReservation.getWeek27();
						}
						jsonWorkPackageWeeklyResourceReservation.setWeek27(weeklyResourceReservationPercentage);	
						jsonWorkPackageWeeklyResourceReservationTotal.setWeek27(jsonWorkPackageWeeklyResourceReservationTotal.getWeek27() + weeklyResourceReservationPercentage);
						break;
					case 28:
						if(jsonWorkPackageWeeklyResourceReservation.getWeek28() != null){
							weeklyResourceReservationPercentage = weeklyResourceReservationPercentage + jsonWorkPackageWeeklyResourceReservation.getWeek28();
						}
						jsonWorkPackageWeeklyResourceReservation.setWeek28(weeklyResourceReservationPercentage);	
						jsonWorkPackageWeeklyResourceReservationTotal.setWeek28(jsonWorkPackageWeeklyResourceReservationTotal.getWeek28() + weeklyResourceReservationPercentage);
						break;
					case 29:
						if(jsonWorkPackageWeeklyResourceReservation.getWeek29() != null){
							weeklyResourceReservationPercentage = weeklyResourceReservationPercentage + jsonWorkPackageWeeklyResourceReservation.getWeek29();
						}
						jsonWorkPackageWeeklyResourceReservation.setWeek29(weeklyResourceReservationPercentage);	
						jsonWorkPackageWeeklyResourceReservationTotal.setWeek29(jsonWorkPackageWeeklyResourceReservationTotal.getWeek29() + weeklyResourceReservationPercentage);
						break;
					case 30:
						if(jsonWorkPackageWeeklyResourceReservation.getWeek30() != null){
							weeklyResourceReservationPercentage = weeklyResourceReservationPercentage + jsonWorkPackageWeeklyResourceReservation.getWeek30();
						}
						jsonWorkPackageWeeklyResourceReservation.setWeek30(weeklyResourceReservationPercentage);
						jsonWorkPackageWeeklyResourceReservationTotal.setWeek30(jsonWorkPackageWeeklyResourceReservationTotal.getWeek30() + weeklyResourceReservationPercentage);
						break;
					case 31:
						if(jsonWorkPackageWeeklyResourceReservation.getWeek31() != null){
							weeklyResourceReservationPercentage = weeklyResourceReservationPercentage + jsonWorkPackageWeeklyResourceReservation.getWeek31();
						}
						jsonWorkPackageWeeklyResourceReservation.setWeek31(weeklyResourceReservationPercentage);
						jsonWorkPackageWeeklyResourceReservationTotal.setWeek31(jsonWorkPackageWeeklyResourceReservationTotal.getWeek31() + weeklyResourceReservationPercentage);
						break;
					case 32:
						if(jsonWorkPackageWeeklyResourceReservation.getWeek32() != null){
							weeklyResourceReservationPercentage = weeklyResourceReservationPercentage + jsonWorkPackageWeeklyResourceReservation.getWeek32();
						}
						jsonWorkPackageWeeklyResourceReservation.setWeek32(weeklyResourceReservationPercentage);	
						jsonWorkPackageWeeklyResourceReservationTotal.setWeek32(jsonWorkPackageWeeklyResourceReservationTotal.getWeek32() + weeklyResourceReservationPercentage);
						break;
					case 33:
						if(jsonWorkPackageWeeklyResourceReservation.getWeek33() != null){
							weeklyResourceReservationPercentage = weeklyResourceReservationPercentage + jsonWorkPackageWeeklyResourceReservation.getWeek33();
						}
						jsonWorkPackageWeeklyResourceReservation.setWeek33(weeklyResourceReservationPercentage);
						jsonWorkPackageWeeklyResourceReservationTotal.setWeek33(jsonWorkPackageWeeklyResourceReservationTotal.getWeek33() + weeklyResourceReservationPercentage);
						break;
					case 34:
						if(jsonWorkPackageWeeklyResourceReservation.getWeek34() != null){
							weeklyResourceReservationPercentage = weeklyResourceReservationPercentage + jsonWorkPackageWeeklyResourceReservation.getWeek34();
						}
						jsonWorkPackageWeeklyResourceReservation.setWeek34(weeklyResourceReservationPercentage);	
						jsonWorkPackageWeeklyResourceReservationTotal.setWeek34(jsonWorkPackageWeeklyResourceReservationTotal.getWeek34() + weeklyResourceReservationPercentage);
						break;
					case 35:
						if(jsonWorkPackageWeeklyResourceReservation.getWeek35() != null){
							weeklyResourceReservationPercentage = weeklyResourceReservationPercentage + jsonWorkPackageWeeklyResourceReservation.getWeek35();
						}
						jsonWorkPackageWeeklyResourceReservation.setWeek35(weeklyResourceReservationPercentage);
						jsonWorkPackageWeeklyResourceReservationTotal.setWeek35(jsonWorkPackageWeeklyResourceReservationTotal.getWeek35() + weeklyResourceReservationPercentage);
						break;
					case 36:
						if(jsonWorkPackageWeeklyResourceReservation.getWeek36() != null){
							weeklyResourceReservationPercentage = weeklyResourceReservationPercentage + jsonWorkPackageWeeklyResourceReservation.getWeek36();
						}
						jsonWorkPackageWeeklyResourceReservation.setWeek36(weeklyResourceReservationPercentage);	
						jsonWorkPackageWeeklyResourceReservationTotal.setWeek36(jsonWorkPackageWeeklyResourceReservationTotal.getWeek36() + weeklyResourceReservationPercentage);
						break;
					case 37:
						if(jsonWorkPackageWeeklyResourceReservation.getWeek37() != null){
							weeklyResourceReservationPercentage = weeklyResourceReservationPercentage + jsonWorkPackageWeeklyResourceReservation.getWeek37();
						}
						jsonWorkPackageWeeklyResourceReservation.setWeek37(weeklyResourceReservationPercentage);	
						jsonWorkPackageWeeklyResourceReservationTotal.setWeek37(jsonWorkPackageWeeklyResourceReservationTotal.getWeek37() + weeklyResourceReservationPercentage);
						break;
					case 38:
						if(jsonWorkPackageWeeklyResourceReservation.getWeek38() != null){
							weeklyResourceReservationPercentage = weeklyResourceReservationPercentage + jsonWorkPackageWeeklyResourceReservation.getWeek38();
						}
						jsonWorkPackageWeeklyResourceReservation.setWeek38(weeklyResourceReservationPercentage);
						jsonWorkPackageWeeklyResourceReservationTotal.setWeek38(jsonWorkPackageWeeklyResourceReservationTotal.getWeek38() + weeklyResourceReservationPercentage);
						break;
					case 39:
						if(jsonWorkPackageWeeklyResourceReservation.getWeek39() != null){
							weeklyResourceReservationPercentage = weeklyResourceReservationPercentage + jsonWorkPackageWeeklyResourceReservation.getWeek39();
						}
						jsonWorkPackageWeeklyResourceReservation.setWeek39(weeklyResourceReservationPercentage);	
						jsonWorkPackageWeeklyResourceReservationTotal.setWeek39(jsonWorkPackageWeeklyResourceReservationTotal.getWeek39() + weeklyResourceReservationPercentage);
						break;
					case 40:
						if(jsonWorkPackageWeeklyResourceReservation.getWeek40() != null){
							weeklyResourceReservationPercentage = weeklyResourceReservationPercentage + jsonWorkPackageWeeklyResourceReservation.getWeek40();
						}
						jsonWorkPackageWeeklyResourceReservation.setWeek40(weeklyResourceReservationPercentage);
						jsonWorkPackageWeeklyResourceReservationTotal.setWeek40(jsonWorkPackageWeeklyResourceReservationTotal.getWeek40() + weeklyResourceReservationPercentage);
						break;
					case 41:
						if(jsonWorkPackageWeeklyResourceReservation.getWeek41() != null){
							weeklyResourceReservationPercentage = weeklyResourceReservationPercentage + jsonWorkPackageWeeklyResourceReservation.getWeek41();
						}
						jsonWorkPackageWeeklyResourceReservation.setWeek41(weeklyResourceReservationPercentage);	
						jsonWorkPackageWeeklyResourceReservationTotal.setWeek41(jsonWorkPackageWeeklyResourceReservationTotal.getWeek41() + weeklyResourceReservationPercentage);
						break;
					case 42:
						if(jsonWorkPackageWeeklyResourceReservation.getWeek42() != null){
							weeklyResourceReservationPercentage = weeklyResourceReservationPercentage + jsonWorkPackageWeeklyResourceReservation.getWeek42();
						}
						jsonWorkPackageWeeklyResourceReservation.setWeek42(weeklyResourceReservationPercentage);	
						jsonWorkPackageWeeklyResourceReservationTotal.setWeek42(jsonWorkPackageWeeklyResourceReservationTotal.getWeek42() + weeklyResourceReservationPercentage);
						break;
					case 43:
						if(jsonWorkPackageWeeklyResourceReservation.getWeek43() != null){
							weeklyResourceReservationPercentage = weeklyResourceReservationPercentage + jsonWorkPackageWeeklyResourceReservation.getWeek43();
						}
						jsonWorkPackageWeeklyResourceReservation.setWeek43(weeklyResourceReservationPercentage);
						jsonWorkPackageWeeklyResourceReservationTotal.setWeek43(jsonWorkPackageWeeklyResourceReservationTotal.getWeek43() + weeklyResourceReservationPercentage);
						break;
					case 44:
						if(jsonWorkPackageWeeklyResourceReservation.getWeek44() != null){
							weeklyResourceReservationPercentage = weeklyResourceReservationPercentage + jsonWorkPackageWeeklyResourceReservation.getWeek44();
						}
						jsonWorkPackageWeeklyResourceReservation.setWeek44(weeklyResourceReservationPercentage);
						jsonWorkPackageWeeklyResourceReservationTotal.setWeek44(jsonWorkPackageWeeklyResourceReservationTotal.getWeek44() + weeklyResourceReservationPercentage);
						break;
					case 45:
						if(jsonWorkPackageWeeklyResourceReservation.getWeek45() != null){
							weeklyResourceReservationPercentage = weeklyResourceReservationPercentage + jsonWorkPackageWeeklyResourceReservation.getWeek45();
						}
						jsonWorkPackageWeeklyResourceReservation.setWeek45(weeklyResourceReservationPercentage);	
						jsonWorkPackageWeeklyResourceReservationTotal.setWeek45(jsonWorkPackageWeeklyResourceReservationTotal.getWeek45() + weeklyResourceReservationPercentage);
						break;
					case 46:
						if(jsonWorkPackageWeeklyResourceReservation.getWeek46() != null){
							weeklyResourceReservationPercentage = weeklyResourceReservationPercentage + jsonWorkPackageWeeklyResourceReservation.getWeek46();
						}
						jsonWorkPackageWeeklyResourceReservation.setWeek46(weeklyResourceReservationPercentage);
						jsonWorkPackageWeeklyResourceReservationTotal.setWeek46(jsonWorkPackageWeeklyResourceReservationTotal.getWeek46() + weeklyResourceReservationPercentage);
						break;
					case 47:
						if(jsonWorkPackageWeeklyResourceReservation.getWeek47() != null){
							weeklyResourceReservationPercentage = weeklyResourceReservationPercentage + jsonWorkPackageWeeklyResourceReservation.getWeek47();
						}
						jsonWorkPackageWeeklyResourceReservation.setWeek47(weeklyResourceReservationPercentage);	
						jsonWorkPackageWeeklyResourceReservationTotal.setWeek47(jsonWorkPackageWeeklyResourceReservationTotal.getWeek47() + weeklyResourceReservationPercentage);
						break;
					case 48:
						if(jsonWorkPackageWeeklyResourceReservation.getWeek48() != null){
							weeklyResourceReservationPercentage = weeklyResourceReservationPercentage + jsonWorkPackageWeeklyResourceReservation.getWeek48();
						}
						jsonWorkPackageWeeklyResourceReservation.setWeek48(weeklyResourceReservationPercentage);
						jsonWorkPackageWeeklyResourceReservationTotal.setWeek48(jsonWorkPackageWeeklyResourceReservationTotal.getWeek48() + weeklyResourceReservationPercentage);
						break;
					case 49:
						if(jsonWorkPackageWeeklyResourceReservation.getWeek49() != null){
							weeklyResourceReservationPercentage = weeklyResourceReservationPercentage + jsonWorkPackageWeeklyResourceReservation.getWeek49();
						}
						jsonWorkPackageWeeklyResourceReservation.setWeek49(weeklyResourceReservationPercentage);
						jsonWorkPackageWeeklyResourceReservationTotal.setWeek49(jsonWorkPackageWeeklyResourceReservationTotal.getWeek49() + weeklyResourceReservationPercentage);
						break;
					case 50:
						if(jsonWorkPackageWeeklyResourceReservation.getWeek50() != null){
							weeklyResourceReservationPercentage = weeklyResourceReservationPercentage + jsonWorkPackageWeeklyResourceReservation.getWeek50();
						}
						jsonWorkPackageWeeklyResourceReservation.setWeek50(weeklyResourceReservationPercentage);	
						jsonWorkPackageWeeklyResourceReservationTotal.setWeek50(jsonWorkPackageWeeklyResourceReservationTotal.getWeek50() + weeklyResourceReservationPercentage);
						break;
					case 51:
						if(jsonWorkPackageWeeklyResourceReservation.getWeek51() != null){
							weeklyResourceReservationPercentage = weeklyResourceReservationPercentage + jsonWorkPackageWeeklyResourceReservation.getWeek51();
						}
						jsonWorkPackageWeeklyResourceReservation.setWeek51(weeklyResourceReservationPercentage);
						jsonWorkPackageWeeklyResourceReservationTotal.setWeek51(jsonWorkPackageWeeklyResourceReservationTotal.getWeek51() + weeklyResourceReservationPercentage);
						break;
										
					case 52:
						if(jsonWorkPackageWeeklyResourceReservation.getWeek52() != null){
							weeklyResourceReservationPercentage = weeklyResourceReservationPercentage + jsonWorkPackageWeeklyResourceReservation.getWeek52();
						}
						jsonWorkPackageWeeklyResourceReservation.setWeek52(weeklyResourceReservationPercentage);	
						jsonWorkPackageWeeklyResourceReservationTotal.setWeek52(jsonWorkPackageWeeklyResourceReservationTotal.getWeek52() + weeklyResourceReservationPercentage);
						break;
				}
				
			}
			
			if(utilizationRange != 0){
				jsonWorkPackageWeeklyResourceReservationsFiltered = getJsonWorkPackageWeeklyResourceReservationsFilteredData(jsonWorkPackageWeeklyResourceReservations,utilizationRange);
			}else{
				return jsonWorkPackageWeeklyResourceReservations;
			}
			
			
		
			
		}catch(Exception ex){
			log.error("Exception in getResourceAllocationWeeklyFromResourcePool - ", ex);
		}
		return jsonWorkPackageWeeklyResourceReservationsFiltered;
		
	}
	
	
	
	private List<JsonWorkPackageWeeklyResourceReservation> getJsonWorkPackageWeeklyResourceReservationsFilteredData(List<JsonWorkPackageWeeklyResourceReservation> jsonWorkPackageWeeklyResourceReservations,Integer utilizationRange) {
		List<JsonWorkPackageWeeklyResourceReservation> filteredJsonData = new ArrayList<JsonWorkPackageWeeklyResourceReservation>();
		//2 -over 1-under, 0-all
		Integer  utilized =utilizationRange;
		boolean isUserToShow = false;
		for(JsonWorkPackageWeeklyResourceReservation jsonWorkPackageWeeklyResourceReservation : jsonWorkPackageWeeklyResourceReservations){
			isUserToShow = false;
			
			for(int i=1;i<=52;i++){
				
			switch(i){

			case 1:
				if(jsonWorkPackageWeeklyResourceReservation.getWeek1() != null){
					
					if(utilized.equals(2)){
						if(jsonWorkPackageWeeklyResourceReservation.getWeek1() > 1){
							isUserToShow = true;
						}
					}else if(utilized.equals(1)){
						if(jsonWorkPackageWeeklyResourceReservation.getWeek1() < 1 ){
							if(jsonWorkPackageWeeklyResourceReservation.getReservedWeeks().contains(i)){
								isUserToShow = true;
							}
							
						}
					}
					
				}
				
				break;
			case 2:
				
				if(jsonWorkPackageWeeklyResourceReservation.getWeek2() != null){
					
					
					if(utilized.equals(2)){
						if(jsonWorkPackageWeeklyResourceReservation.getWeek2() > 1){
							isUserToShow = true;
						}
					}else if(utilized.equals(1)){
						if(jsonWorkPackageWeeklyResourceReservation.getWeek2() < 1){
							if(jsonWorkPackageWeeklyResourceReservation.getReservedWeeks().contains(i)){
								isUserToShow = true;
							}
							
						}
					}
					
				}
				break;
			case 3:

				if(jsonWorkPackageWeeklyResourceReservation.getWeek3() != null){
					
					if(utilized.equals(2)){
						if(jsonWorkPackageWeeklyResourceReservation.getWeek3() > 1){
							isUserToShow = true;
						}
					}else if(utilized.equals(1)){
						if(jsonWorkPackageWeeklyResourceReservation.getWeek3() < 1){
							if(jsonWorkPackageWeeklyResourceReservation.getReservedWeeks().contains(i)){
								isUserToShow = true;
							}
						}
					}
					
				}

				break;
			case 4:
				if(jsonWorkPackageWeeklyResourceReservation.getWeek4() != null){
					
					if(utilized.equals(2)){
						if(jsonWorkPackageWeeklyResourceReservation.getWeek4() > 1){
							isUserToShow = true;
						}
					}else if(utilized.equals(1)){
						if(jsonWorkPackageWeeklyResourceReservation.getWeek4() < 1){
							if(jsonWorkPackageWeeklyResourceReservation.getReservedWeeks().contains(i)){
								isUserToShow = true;
							}
						}
					}
					
				}
				
				break;
			case 5:
				if(jsonWorkPackageWeeklyResourceReservation.getWeek5() != null){
					
					if(utilized.equals(2)){
						if(jsonWorkPackageWeeklyResourceReservation.getWeek5() > 1){
							isUserToShow = true;
						}
					}else if(utilized.equals(1)){
						if(jsonWorkPackageWeeklyResourceReservation.getWeek5() < 1){
							if(jsonWorkPackageWeeklyResourceReservation.getReservedWeeks().contains(i)){
								isUserToShow = true;
							}
						}
					}
					
					
				}
				break;
			case 6:
				if(jsonWorkPackageWeeklyResourceReservation.getWeek6() != null){
					
					if(utilized.equals(2)){
						if(jsonWorkPackageWeeklyResourceReservation.getWeek6() > 1){
							isUserToShow = true;
						}
					}else if(utilized.equals(1)){
						if(jsonWorkPackageWeeklyResourceReservation.getWeek6() < 1 ){
							if(jsonWorkPackageWeeklyResourceReservation.getReservedWeeks().contains(i)){
								isUserToShow = true;
							}
						}
					}
					
				}
				break;
			case 7:
				if(jsonWorkPackageWeeklyResourceReservation.getWeek7() != null){
					

					if(utilized.equals(2)){
						if(jsonWorkPackageWeeklyResourceReservation.getWeek7() > 1){
							isUserToShow = true;
						}
					}else if(utilized.equals(1)){
						if(jsonWorkPackageWeeklyResourceReservation.getWeek7() < 1){
							if(jsonWorkPackageWeeklyResourceReservation.getReservedWeeks().contains(i)){
								isUserToShow = true;
							}
						}
					}
					
				}
				break;
			case 8:
				if(jsonWorkPackageWeeklyResourceReservation.getWeek8() != null){
					
					if(utilized.equals(2)){
						if(jsonWorkPackageWeeklyResourceReservation.getWeek8() > 1){
							isUserToShow = true;
						}
					}else if(utilized.equals(1)){
						if(jsonWorkPackageWeeklyResourceReservation.getWeek8() < 1 ){
							if(jsonWorkPackageWeeklyResourceReservation.getReservedWeeks().contains(i)){
								isUserToShow = true;
							}
						}
					}
					
				}
				break;
			case 9:
				if(jsonWorkPackageWeeklyResourceReservation.getWeek9() != null){
					
					if(utilized.equals(2)){
						if(jsonWorkPackageWeeklyResourceReservation.getWeek9() > 1){
							isUserToShow = true;
						}
					}else if(utilized.equals(1)){
						if(jsonWorkPackageWeeklyResourceReservation.getWeek9() < 1){
							if(jsonWorkPackageWeeklyResourceReservation.getReservedWeeks().contains(i)){
								isUserToShow = true;
							}
						}
					}
					
					
				}
				break;
			case 10:
				if(jsonWorkPackageWeeklyResourceReservation.getWeek10() != null){
					
					
					if(utilized.equals(2)){
						if(jsonWorkPackageWeeklyResourceReservation.getWeek10() > 1){
							isUserToShow = true;
						}
					}else if(utilized.equals(1)){
						if(jsonWorkPackageWeeklyResourceReservation.getWeek10() < 1 ){
							if(jsonWorkPackageWeeklyResourceReservation.getReservedWeeks().contains(i)){
								isUserToShow = true;
							}
						}
					}
					
					
					
				}
				break;
			case 11:
				if(jsonWorkPackageWeeklyResourceReservation.getWeek11() != null){
					if(utilized.equals(2)){
						if(jsonWorkPackageWeeklyResourceReservation.getWeek11() > 1){
							isUserToShow = true;
						}
					}else if(utilized.equals(1)){
						if(jsonWorkPackageWeeklyResourceReservation.getWeek11() < 1 ){
							if(jsonWorkPackageWeeklyResourceReservation.getReservedWeeks().contains(i)){
								isUserToShow = true;
							}
						}
					}
				}
				break;
			case 12:
				if(jsonWorkPackageWeeklyResourceReservation.getWeek12() != null){
					
					if(utilized.equals(2)){
						if(jsonWorkPackageWeeklyResourceReservation.getWeek12() > 1){
							isUserToShow = true;
						}
					}else if(utilized.equals(1)){
						if(jsonWorkPackageWeeklyResourceReservation.getWeek12() < 1 ){
							if(jsonWorkPackageWeeklyResourceReservation.getReservedWeeks().contains(i)){
								isUserToShow = true;
							}
						}
					}
					
				}	
				break;
			case 13:
				if(jsonWorkPackageWeeklyResourceReservation.getWeek13() != null){
					
					if(utilized.equals(2)){
						if(jsonWorkPackageWeeklyResourceReservation.getWeek13() > 1){
							isUserToShow = true;
						}
					}else if(utilized.equals(1)){
						if(jsonWorkPackageWeeklyResourceReservation.getWeek13() < 1){
							if(jsonWorkPackageWeeklyResourceReservation.getReservedWeeks().contains(i)){
								isUserToShow = true;
							}
						}
					}
					
				}
				break;
			case 14:
				if(jsonWorkPackageWeeklyResourceReservation.getWeek14() != null){
					if(utilized.equals(2)){
						if(jsonWorkPackageWeeklyResourceReservation.getWeek14() > 1){
							isUserToShow = true;
						}
					}else if(utilized.equals(1)){
						if(jsonWorkPackageWeeklyResourceReservation.getWeek14() < 1 ){
							if(jsonWorkPackageWeeklyResourceReservation.getReservedWeeks().contains(i)){
								isUserToShow = true;
							}
						}
					}
					
				}			
				break;
			case 15:
				if(jsonWorkPackageWeeklyResourceReservation.getWeek15() != null){
					
					
					if(utilized.equals(2)){
						if(jsonWorkPackageWeeklyResourceReservation.getWeek15() > 1){
							isUserToShow = true;
						}
					}else if(utilized.equals(1)){
						if(jsonWorkPackageWeeklyResourceReservation.getWeek15() < 1 ){
							if(jsonWorkPackageWeeklyResourceReservation.getReservedWeeks().contains(i)){
								isUserToShow = true;
							}
						}
					}
					
				}
				break;
			case 16:
				if(jsonWorkPackageWeeklyResourceReservation.getWeek16() != null){
					
					if(utilized.equals(2)){
						if(jsonWorkPackageWeeklyResourceReservation.getWeek16() > 1){
							isUserToShow = true;
						}
					}else if(utilized.equals(1)){
						if(jsonWorkPackageWeeklyResourceReservation.getWeek16() < 1){
							if(jsonWorkPackageWeeklyResourceReservation.getReservedWeeks().contains(i)){
								isUserToShow = true;
							}
						}
					}
					
					
				}
				break;
			case 17:
				if(jsonWorkPackageWeeklyResourceReservation.getWeek17() != null){
					
					
					if(utilized.equals(2)){
						if(jsonWorkPackageWeeklyResourceReservation.getWeek17() > 1){
							isUserToShow = true;
						}
					}else if(utilized.equals(1)){
						if(jsonWorkPackageWeeklyResourceReservation.getWeek17() < 1 ){
							if(jsonWorkPackageWeeklyResourceReservation.getReservedWeeks().contains(i)){
								isUserToShow = true;
							}
						}
					}
					
					
					
				}
				break;
			case 18:
				if(jsonWorkPackageWeeklyResourceReservation.getWeek18() != null){
					
					if(utilized.equals(2)){
						if(jsonWorkPackageWeeklyResourceReservation.getWeek18() > 1){
							isUserToShow = true;
						}
					}else if(utilized.equals(1)){
						if(jsonWorkPackageWeeklyResourceReservation.getWeek18() < 1){
							if(jsonWorkPackageWeeklyResourceReservation.getReservedWeeks().contains(i)){
								isUserToShow = true;
							}
						}
					}
					
				}
				break;
			case 19:
				if(jsonWorkPackageWeeklyResourceReservation.getWeek19() != null){
					
					
					if(utilized.equals(2)){
						if(jsonWorkPackageWeeklyResourceReservation.getWeek19() > 1){
							isUserToShow = true;
						}
					}else if(utilized.equals(1)){
						if(jsonWorkPackageWeeklyResourceReservation.getWeek19() < 1 ){
							if(jsonWorkPackageWeeklyResourceReservation.getReservedWeeks().contains(i)){
								isUserToShow = true;
							}
						}
					}
					
					
				}
				break;
			case 20:
				if(jsonWorkPackageWeeklyResourceReservation.getWeek20() != null){
					
					
					if(utilized.equals(2)){
						if(jsonWorkPackageWeeklyResourceReservation.getWeek20() > 1){
							isUserToShow = true;
						}
					}else if(utilized.equals(1)){
						if(jsonWorkPackageWeeklyResourceReservation.getWeek20() < 1 ){
							if(jsonWorkPackageWeeklyResourceReservation.getReservedWeeks().contains(i)){
								isUserToShow = true;
							}
						}
					}
					
				}
				break;
			case 21:
				if(jsonWorkPackageWeeklyResourceReservation.getWeek21() != null){
					
					
					if(utilized.equals(2)){
						if(jsonWorkPackageWeeklyResourceReservation.getWeek21() > 1){
							isUserToShow = true;
						}
					}else if(utilized.equals(1)){
						if(jsonWorkPackageWeeklyResourceReservation.getWeek21() < 1 ){
							if(jsonWorkPackageWeeklyResourceReservation.getReservedWeeks().contains(i)){
								isUserToShow = true;
							}
						}
					}
					
				}
				break;
			case 22:
				if(jsonWorkPackageWeeklyResourceReservation.getWeek22() != null){
					
					
					if(utilized.equals(2)){
						if(jsonWorkPackageWeeklyResourceReservation.getWeek22() > 1){
							isUserToShow = true;
						}
					}else if(utilized.equals(1)){
						if(jsonWorkPackageWeeklyResourceReservation.getWeek22() < 1){
							if(jsonWorkPackageWeeklyResourceReservation.getReservedWeeks().contains(i)){
								isUserToShow = true;
							}
						}
					}
					
					
				}
				break;
			case 23:
				if(jsonWorkPackageWeeklyResourceReservation.getWeek23() != null){
					
					
					if(utilized.equals(2)){
						if(jsonWorkPackageWeeklyResourceReservation.getWeek23() > 1){
							isUserToShow = true;
						}
					}else if(utilized.equals(1)){
						if(jsonWorkPackageWeeklyResourceReservation.getWeek23() < 1){
							if(jsonWorkPackageWeeklyResourceReservation.getReservedWeeks().contains(i)){
								isUserToShow = true;
							}
						}
					}
					
					
				}
				break;
			case 24:
				if(jsonWorkPackageWeeklyResourceReservation.getWeek24() != null){
					
					if(utilized.equals(2)){
						if(jsonWorkPackageWeeklyResourceReservation.getWeek24() > 1){
							isUserToShow = true;
						}
					}else if(utilized.equals(1)){
						if(jsonWorkPackageWeeklyResourceReservation.getWeek24() < 1 ){
							if(jsonWorkPackageWeeklyResourceReservation.getReservedWeeks().contains(i)){
								isUserToShow = true;
							}
						}
					}
					
					
					
				}
				break;
			case 25:
				if(jsonWorkPackageWeeklyResourceReservation.getWeek25() != null){
					
					
					if(utilized.equals(2)){
						if(jsonWorkPackageWeeklyResourceReservation.getWeek25() > 1){
							isUserToShow = true;
						}
					}else if(utilized.equals(1)){
						if(jsonWorkPackageWeeklyResourceReservation.getWeek25() < 1 ){
							if(jsonWorkPackageWeeklyResourceReservation.getReservedWeeks().contains(i)){
								isUserToShow = true;
							}
						}
					}
					
				}
				break;
			case 26:
				if(jsonWorkPackageWeeklyResourceReservation.getWeek26() != null){
					
					
					if(utilized.equals(2)){
						if(jsonWorkPackageWeeklyResourceReservation.getWeek26() > 1){
							isUserToShow = true;
						}
					}else if(utilized.equals(1)){
						if(jsonWorkPackageWeeklyResourceReservation.getWeek26() < 1 ){
							if(jsonWorkPackageWeeklyResourceReservation.getReservedWeeks().contains(i)){
								isUserToShow = true;
							}
						}
					}
					
				}
				break;
			case 27:
				if(jsonWorkPackageWeeklyResourceReservation.getWeek27() != null){
					
					
					if(utilized.equals(2)){
						if(jsonWorkPackageWeeklyResourceReservation.getWeek27() > 1){
							isUserToShow = true;
						}
					}else if(utilized.equals(1)){
						if(jsonWorkPackageWeeklyResourceReservation.getWeek27() < 1 ){
							if(jsonWorkPackageWeeklyResourceReservation.getReservedWeeks().contains(i)){
								isUserToShow = true;
							}
						}
					}
					
					
				}
				break;
			case 28:
				if(jsonWorkPackageWeeklyResourceReservation.getWeek28() != null){
					
					
					if(utilized.equals(2)){
						if(jsonWorkPackageWeeklyResourceReservation.getWeek28() > 1){
							isUserToShow = true;
						}
					}else if(utilized.equals(1)){
						if(jsonWorkPackageWeeklyResourceReservation.getWeek28() < 1 ){
							if(jsonWorkPackageWeeklyResourceReservation.getReservedWeeks().contains(i)){
								isUserToShow = true;
							}
						}
					}
					
				}
				break;
			case 29:
				if(jsonWorkPackageWeeklyResourceReservation.getWeek29() != null){
					
					if(utilized.equals(2)){
						if(jsonWorkPackageWeeklyResourceReservation.getWeek29() > 1){
							isUserToShow = true;
						}
					}else if(utilized.equals(1)){
						if(jsonWorkPackageWeeklyResourceReservation.getWeek29() < 1 ){
							if(jsonWorkPackageWeeklyResourceReservation.getReservedWeeks().contains(i)){
								isUserToShow = true;
							}
						}
					}
					
				}
				break;
			case 30:
				if(jsonWorkPackageWeeklyResourceReservation.getWeek30() != null){
					
					if(utilized.equals(2)){
						if(jsonWorkPackageWeeklyResourceReservation.getWeek30() > 1){
							isUserToShow = true;
						}
					}else if(utilized.equals(1)){
						if(jsonWorkPackageWeeklyResourceReservation.getWeek30() < 1){
							if(jsonWorkPackageWeeklyResourceReservation.getReservedWeeks().contains(i)){
								isUserToShow = true;
							}
						}
					}
				}
				break;
			case 31:
				if(jsonWorkPackageWeeklyResourceReservation.getWeek31() != null){
					
					
					if(utilized.equals(2)){
						if(jsonWorkPackageWeeklyResourceReservation.getWeek31() > 1){
							isUserToShow = true;
						}
					}else if(utilized.equals(1)){
						if(jsonWorkPackageWeeklyResourceReservation.getWeek31() < 1 ){
							if(jsonWorkPackageWeeklyResourceReservation.getReservedWeeks().contains(i)){
								isUserToShow = true;
							}
						}
					}
				}
				break;
			case 32:
				if(jsonWorkPackageWeeklyResourceReservation.getWeek32() != null){
					
					if(utilized.equals(2)){
						if(jsonWorkPackageWeeklyResourceReservation.getWeek32() > 1){
							isUserToShow = true;
						}
					}else if(utilized.equals(1)){
						if(jsonWorkPackageWeeklyResourceReservation.getWeek32() < 1 ){
							if(jsonWorkPackageWeeklyResourceReservation.getReservedWeeks().contains(i)){
								isUserToShow = true;
							}
						}
					}
				}
				break;
			case 33:
				if(jsonWorkPackageWeeklyResourceReservation.getWeek33() != null){
					
					
					if(utilized.equals(2)){
						if(jsonWorkPackageWeeklyResourceReservation.getWeek33() > 1){
							isUserToShow = true;
						}
					}else if(utilized.equals(1)){
						if(jsonWorkPackageWeeklyResourceReservation.getWeek33() < 1){
							if(jsonWorkPackageWeeklyResourceReservation.getReservedWeeks().contains(i)){
								isUserToShow = true;
							}
						}
					}
					
					
				}
				break;
			case 34:
				if(jsonWorkPackageWeeklyResourceReservation.getWeek34() != null){
					
					
					if(utilized.equals(2)){
						if(jsonWorkPackageWeeklyResourceReservation.getWeek34() > 1){
							isUserToShow = true;
						}
					}else if(utilized.equals(1)){
						if(jsonWorkPackageWeeklyResourceReservation.getWeek34() < 1){
							if(jsonWorkPackageWeeklyResourceReservation.getReservedWeeks().contains(i)){
								isUserToShow = true;
							}
						}
					}
					
				}
				break;
			case 35:
				if(jsonWorkPackageWeeklyResourceReservation.getWeek35() != null){
					
					if(utilized.equals(2)){
						if(jsonWorkPackageWeeklyResourceReservation.getWeek35() > 1){
							isUserToShow = true;
						}
					}else if(utilized.equals(1)){
						if(jsonWorkPackageWeeklyResourceReservation.getWeek35() < 1){
							if(jsonWorkPackageWeeklyResourceReservation.getReservedWeeks().contains(i)){
								isUserToShow = true;
							}
						}
					}
					
					
				}
				break;
			case 36:
				if(jsonWorkPackageWeeklyResourceReservation.getWeek36() != null){
					
					
					if(utilized.equals(2)){
						if(jsonWorkPackageWeeklyResourceReservation.getWeek36() > 1){
							isUserToShow = true;
						}
					}else if(utilized.equals(1)){
						if(jsonWorkPackageWeeklyResourceReservation.getWeek36() < 1){
							if(jsonWorkPackageWeeklyResourceReservation.getReservedWeeks().contains(i)){
								isUserToShow = true;
							}
						}
					}
					
					
				}
				break;
			case 37:
				if(jsonWorkPackageWeeklyResourceReservation.getWeek37() != null){
					
					
					if(utilized.equals(2)){
						if(jsonWorkPackageWeeklyResourceReservation.getWeek37() > 1){
							isUserToShow = true;
					}else if(utilized.equals(1)){
						if(jsonWorkPackageWeeklyResourceReservation.getWeek37() < 1 ){
							if(jsonWorkPackageWeeklyResourceReservation.getReservedWeeks().contains(i)){
								isUserToShow = true;
							}
						}
					}
					
				  }	
				}
				break;
			case 38:
				if(jsonWorkPackageWeeklyResourceReservation.getWeek38() != null){
					
					if(utilized.equals(2)){
						if(jsonWorkPackageWeeklyResourceReservation.getWeek38() > 1){
							isUserToShow = true;
						}
					}else if(utilized.equals(1)){
						if(jsonWorkPackageWeeklyResourceReservation.getWeek38() < 1 ){
							if(jsonWorkPackageWeeklyResourceReservation.getReservedWeeks().contains(i)){
								isUserToShow = true;
							}
						}
					}
					
					
				}
				break;
			case 39:
				if(jsonWorkPackageWeeklyResourceReservation.getWeek39() != null){
					
					if(utilized.equals(2)){
						if(jsonWorkPackageWeeklyResourceReservation.getWeek39() > 1){
							isUserToShow = true;
						}
					}else if(utilized.equals(1)){
						if(jsonWorkPackageWeeklyResourceReservation.getWeek39() < 1){
							if(jsonWorkPackageWeeklyResourceReservation.getReservedWeeks().contains(i)){
								isUserToShow = true;
							}
						}
					}
					
					
				}
				break;
			case 40:
				if(jsonWorkPackageWeeklyResourceReservation.getWeek40() != null){
					
					
					if(utilized.equals(2)){
						if(jsonWorkPackageWeeklyResourceReservation.getWeek40() > 1){
							isUserToShow = true;
						}
					}else if(utilized.equals(1)){
						if(jsonWorkPackageWeeklyResourceReservation.getWeek40() < 1){
							if(jsonWorkPackageWeeklyResourceReservation.getReservedWeeks().contains(i)){
								isUserToShow = true;
							}
						}
					}
					
					
				}
				break;
			case 41:
				if(jsonWorkPackageWeeklyResourceReservation.getWeek41() != null){
					
					if(utilized.equals(2)){
						if(jsonWorkPackageWeeklyResourceReservation.getWeek41() > 1){
							isUserToShow = true;
						}
					}else if(utilized.equals(1)){
						if(jsonWorkPackageWeeklyResourceReservation.getWeek41() < 1){
							if(jsonWorkPackageWeeklyResourceReservation.getReservedWeeks().contains(i)){
								isUserToShow = true;
							}
						}
					}
					
				}
				break;
			case 42:
				if(jsonWorkPackageWeeklyResourceReservation.getWeek42() != null){
					
					
					if(utilized.equals(2)){
						if(jsonWorkPackageWeeklyResourceReservation.getWeek42() > 1){
							isUserToShow = true;
						}
					}else if(utilized.equals(1)){
						if(jsonWorkPackageWeeklyResourceReservation.getWeek42() < 1 ){
							if(jsonWorkPackageWeeklyResourceReservation.getReservedWeeks().contains(i)){
								isUserToShow = true;
							}
						}
					}
					
					
				}
				break;
			case 43:
				if(jsonWorkPackageWeeklyResourceReservation.getWeek43() != null){
					
					
					if(utilized.equals(2)){
						if(jsonWorkPackageWeeklyResourceReservation.getWeek43() > 1){
							isUserToShow = true;
						}
					}else if(utilized.equals(1)){
						if(jsonWorkPackageWeeklyResourceReservation.getWeek43() < 1){
							if(jsonWorkPackageWeeklyResourceReservation.getReservedWeeks().contains(i)){
								isUserToShow = true;
							}
						}
					}
					
					
				}
				break;
			case 44:
				if(jsonWorkPackageWeeklyResourceReservation.getWeek44() != null){
					
					if(utilized.equals(2)){
						if(jsonWorkPackageWeeklyResourceReservation.getWeek44() > 1){
							isUserToShow = true;
						}
					}else if(utilized.equals(1)){
						if(jsonWorkPackageWeeklyResourceReservation.getWeek44() < 1){
							if(jsonWorkPackageWeeklyResourceReservation.getReservedWeeks().contains(i)){
								isUserToShow = true;
							}
						}
					}
					
				}
				break;
			case 45:
				if(jsonWorkPackageWeeklyResourceReservation.getWeek45() != null){
					
					
					if(utilized.equals(2)){
						if(jsonWorkPackageWeeklyResourceReservation.getWeek45() > 1){
							isUserToShow = true;
						}
					}else if(utilized.equals(1)){
						if(jsonWorkPackageWeeklyResourceReservation.getWeek45() < 1){
							if(jsonWorkPackageWeeklyResourceReservation.getReservedWeeks().contains(i)){
								isUserToShow = true;
							}
						}
					}
					
				}
				break;
			case 46:
				if(jsonWorkPackageWeeklyResourceReservation.getWeek46() != null){
					
					if(utilized.equals(2)){
						if(jsonWorkPackageWeeklyResourceReservation.getWeek46() > 1){
							isUserToShow = true;
						}
					}else if(utilized.equals(1)){
						if(jsonWorkPackageWeeklyResourceReservation.getWeek46() < 1){
							if(jsonWorkPackageWeeklyResourceReservation.getReservedWeeks().contains(i)){
								isUserToShow = true;
							}
						}
					}
					
				}
				break;
			case 47:
				if(jsonWorkPackageWeeklyResourceReservation.getWeek47() != null){
					
					
					if(utilized.equals(2)){
						if(jsonWorkPackageWeeklyResourceReservation.getWeek47() > 1){
							isUserToShow = true;
						}
					}else if(utilized.equals(1)){
						if(jsonWorkPackageWeeklyResourceReservation.getWeek47() < 1){
							if(jsonWorkPackageWeeklyResourceReservation.getReservedWeeks().contains(i)){
								isUserToShow = true;
							}
						}
					}
					
				}
				break;
			case 48:
				if(jsonWorkPackageWeeklyResourceReservation.getWeek48() != null){
					
					
					if(utilized.equals(2)){
						if(jsonWorkPackageWeeklyResourceReservation.getWeek48() > 1){
							isUserToShow = true;
						}
					}else if(utilized.equals(1)){
						if(jsonWorkPackageWeeklyResourceReservation.getWeek48() < 1){
							if(jsonWorkPackageWeeklyResourceReservation.getReservedWeeks().contains(i)){
								isUserToShow = true;
							}
						}
					}
					
					
				}
				break;
			case 49:
				if(jsonWorkPackageWeeklyResourceReservation.getWeek49() != null){
					
					if(utilized.equals(2)){
						if(jsonWorkPackageWeeklyResourceReservation.getWeek49() > 1){
							isUserToShow = true;
						}
					}else if(utilized.equals(1)){
						if(jsonWorkPackageWeeklyResourceReservation.getWeek49() < 1){
							if(jsonWorkPackageWeeklyResourceReservation.getReservedWeeks().contains(i)){
								isUserToShow = true;
							}
						}
					}
					
					
				}
				break;
			case 50:
				if(jsonWorkPackageWeeklyResourceReservation.getWeek50() != null){
					
					
					if(utilized.equals(2)){
						if(jsonWorkPackageWeeklyResourceReservation.getWeek50() > 1){
							isUserToShow = true;
						}
					}else if(utilized.equals(1)){
						if(jsonWorkPackageWeeklyResourceReservation.getWeek50() < 1){
							if(jsonWorkPackageWeeklyResourceReservation.getReservedWeeks().contains(i)){
								isUserToShow = true;
							}
						}
					}
					
				}
				break;
			case 51:
				if(jsonWorkPackageWeeklyResourceReservation.getWeek51() != null){
					
					if(utilized.equals(2)){
						if(jsonWorkPackageWeeklyResourceReservation.getWeek51() > 1){
							isUserToShow = true;
						}
					}else if(utilized.equals(1)){
						if(jsonWorkPackageWeeklyResourceReservation.getWeek51() < 1){
							if(jsonWorkPackageWeeklyResourceReservation.getReservedWeeks().contains(i)){
								isUserToShow = true;
							}
						}
					}
					
					
				}
				break;
								
			case 52:
				if(jsonWorkPackageWeeklyResourceReservation.getWeek52() != null){
					
					if(utilized.equals(2)){
						if(jsonWorkPackageWeeklyResourceReservation.getWeek52() > 1){
							isUserToShow = true;
						}
					}else if(utilized.equals(1)){
						if(jsonWorkPackageWeeklyResourceReservation.getWeek52() < 1){
							if(jsonWorkPackageWeeklyResourceReservation.getReservedWeeks().contains(i)){
								isUserToShow = true;
							}
						}
					}
					
				}
				break;
		
			}
		}
			if(isUserToShow){
				filteredJsonData.add(jsonWorkPackageWeeklyResourceReservation);
			}
			
		}
		
		return filteredJsonData;
	}
	
	

	@Override
	@Transactional
	public List<WorkPackageDemandProjection> getWorkpackageDemandProjection(Integer workPackageId, Integer shiftId, Integer skillId, Integer userRoleId, Integer workWeek, Integer workYear,Integer userTypeId) {
		return resourceAvailabilityDAO.getWorkpackageDemandProjection(workPackageId, shiftId, skillId, userRoleId, workWeek, workYear,userTypeId);
	}

	@Override
	@Transactional
	public List<TestFactoryResourceReservation> getReservedResourcesWeeklyByUserIdForUpdateOrAdd(Integer workpackageId, Integer recursiveWeek, Integer workYear,Integer shiftId, Integer skillId,
			Integer reservedorUnreservedUserId, Integer userRoleId,Integer userTypeId) {
		return resourceAvailabilityDAO.getReservedResourcesWeeklyByUserIdForUpdateOrAdd(workpackageId,recursiveWeek,workYear,shiftId,skillId,reservedorUnreservedUserId,userRoleId,userTypeId);
	}

	@Override
	@Transactional
	public List<JsonWorkPackageWeeklyDemandProjection> listWorkpackageWeeklyDemandProjectionEnagementLevel(int testFactoryLabId,
			int testFactoryId, int productId, int workPackageId, int weekYear,int selectedTab, List<Integer> workWeekList) {
		
		List<JsonWorkPackageWeeklyDemandProjection> jsonWorkPackageWeeklyDemandProjections = new ArrayList<JsonWorkPackageWeeklyDemandProjection>();
		List<WeeklyResourceDemandDTO> weeklyResourceDemandDTOs = new ArrayList<WeeklyResourceDemandDTO>();
		List<WeeklyResourceDemandDTO> demandAndReserveList = new ArrayList<WeeklyResourceDemandDTO>();
		
		demandAndReserveList = resourceAvailabilityDAO.listWorkpackageWeeklyDemandForEnagementLevel(testFactoryLabId,testFactoryId,productId,workPackageId,weekYear,workWeekList);
		if(demandAndReserveList != null){
			weeklyResourceDemandDTOs.addAll(demandAndReserveList);
		}
		demandAndReserveList = resourceAvailabilityDAO.listWorkpackageWeeklyReservationForEnagementLevel(testFactoryLabId,testFactoryId,productId,workPackageId,weekYear,workWeekList);
		if(demandAndReserveList != null){
			weeklyResourceDemandDTOs.addAll(demandAndReserveList);
		}
		
		for(WeeklyResourceDemandDTO weeklyResourceDemandDTO:weeklyResourceDemandDTOs){
			JsonWorkPackageWeeklyDemandProjection jsonWorkPackageWeeklyDemandProjection = getJsonWorkPackageWeeklyDemandProjectionTestFactoryLabLevel(jsonWorkPackageWeeklyDemandProjections, weeklyResourceDemandDTO);
			if (jsonWorkPackageWeeklyDemandProjection == null) {
				jsonWorkPackageWeeklyDemandProjection = new JsonWorkPackageWeeklyDemandProjection();
				jsonWorkPackageWeeklyDemandProjection.setWorkPackageId(weeklyResourceDemandDTO.getWorkPackageId());
				jsonWorkPackageWeeklyDemandProjection.setWorkPackageName(weeklyResourceDemandDTO.getWorkPackageName());
				jsonWorkPackageWeeklyDemandProjection.setShiftId(weeklyResourceDemandDTO.getShiftId());
				jsonWorkPackageWeeklyDemandProjection.setShiftName(weeklyResourceDemandDTO.getShiftName());
				jsonWorkPackageWeeklyDemandProjection.setUserRoleId(weeklyResourceDemandDTO.getRoleId());
				jsonWorkPackageWeeklyDemandProjection.setUserRoleName(weeklyResourceDemandDTO.getRoleName());
				jsonWorkPackageWeeklyDemandProjection.setSkillId(weeklyResourceDemandDTO.getSkillId());
				jsonWorkPackageWeeklyDemandProjection.setSkillName(weeklyResourceDemandDTO.getSkillName());
				jsonWorkPackageWeeklyDemandProjection.setWorkWeek(weeklyResourceDemandDTO.getWorkWeek());
				jsonWorkPackageWeeklyDemandProjection.setWorkYear(weeklyResourceDemandDTO.getWorkYear());
				jsonWorkPackageWeeklyDemandProjection.setProductId(weeklyResourceDemandDTO.getProductId());
				jsonWorkPackageWeeklyDemandProjection.setProductName(weeklyResourceDemandDTO.getProductName());
				jsonWorkPackageWeeklyDemandProjection.setUserTypeId(weeklyResourceDemandDTO.getUserTypeId());
				jsonWorkPackageWeeklyDemandProjection.setUserTypeName(weeklyResourceDemandDTO.getUserTypeName());
				jsonWorkPackageWeeklyDemandProjection.setTestFactoryId(weeklyResourceDemandDTO.getTestFactoryId());
				jsonWorkPackageWeeklyDemandProjection.setTestFactoryName(weeklyResourceDemandDTO.getTestFactoryName());
				jsonWorkPackageWeeklyDemandProjection.setGroupDemandId(weeklyResourceDemandDTO.getGroupDemandId());
				
				
				jsonWorkPackageWeeklyDemandProjections.add(jsonWorkPackageWeeklyDemandProjection);
			}
			Integer workWeekNo = weeklyResourceDemandDTO.getWorkWeek();
			if(workWeekNo == null){
				workWeekNo = 0;
			}
			
			float weeklyResourceCount = weeklyResourceDemandDTO.getResourceCount();
			weeklyResourceCount= weeklyResourceCount/5;
			
			float weeklyReservedResourceCount = weeklyResourceDemandDTO.getReservedResourceCount();
			weeklyReservedResourceCount = weeklyReservedResourceCount / 5;
			weeklyReservedResourceCount= weeklyReservedResourceCount/100;
			
			
			switch (workWeekNo) {
				case 1:
					if(jsonWorkPackageWeeklyDemandProjection.getWeek1() != null){
						weeklyResourceCount = weeklyResourceCount + jsonWorkPackageWeeklyDemandProjection.getWeek1();
					}
					weeklyReservedResourceCount = weeklyReservedResourceCount + jsonWorkPackageWeeklyDemandProjection.getReservedResourceCountWk1();
					jsonWorkPackageWeeklyDemandProjection.setReservedResourceCountWk1(weeklyReservedResourceCount);
					
					jsonWorkPackageWeeklyDemandProjection.setWeek1(weeklyResourceCount);
					
					break;
				case 2:
					if(jsonWorkPackageWeeklyDemandProjection.getWeek2() != null){
						weeklyResourceCount = weeklyResourceCount + jsonWorkPackageWeeklyDemandProjection.getWeek2();
					}
					weeklyReservedResourceCount = weeklyReservedResourceCount + jsonWorkPackageWeeklyDemandProjection.getReservedResourceCountWk2();
					jsonWorkPackageWeeklyDemandProjection.setReservedResourceCountWk2(weeklyReservedResourceCount);
					
					jsonWorkPackageWeeklyDemandProjection.setWeek2(weeklyResourceCount);	
					break;
				case 3:
					if(jsonWorkPackageWeeklyDemandProjection.getWeek3() != null){
						weeklyResourceCount = weeklyResourceCount + jsonWorkPackageWeeklyDemandProjection.getWeek3();
					}
					weeklyReservedResourceCount = weeklyReservedResourceCount + jsonWorkPackageWeeklyDemandProjection.getReservedResourceCountWk3();
					jsonWorkPackageWeeklyDemandProjection.setReservedResourceCountWk3(weeklyReservedResourceCount);
					
					jsonWorkPackageWeeklyDemandProjection.setWeek3(weeklyResourceCount);
					break;
				case 4:
					if(jsonWorkPackageWeeklyDemandProjection.getWeek4() != null){
						weeklyResourceCount = weeklyResourceCount + jsonWorkPackageWeeklyDemandProjection.getWeek4();
					}
					weeklyReservedResourceCount = weeklyReservedResourceCount + jsonWorkPackageWeeklyDemandProjection.getReservedResourceCountWk4();
					jsonWorkPackageWeeklyDemandProjection.setReservedResourceCountWk4(weeklyReservedResourceCount);
					
					jsonWorkPackageWeeklyDemandProjection.setWeek4(weeklyResourceCount);	
					break;
				case 5:
					if(jsonWorkPackageWeeklyDemandProjection.getWeek5() != null){
						weeklyResourceCount = weeklyResourceCount + jsonWorkPackageWeeklyDemandProjection.getWeek5();
					}
					weeklyReservedResourceCount = weeklyReservedResourceCount + jsonWorkPackageWeeklyDemandProjection.getReservedResourceCountWk5();
					jsonWorkPackageWeeklyDemandProjection.setReservedResourceCountWk5(weeklyReservedResourceCount);
					
					jsonWorkPackageWeeklyDemandProjection.setWeek5(weeklyResourceCount);	
					break;
				case 6:
					if(jsonWorkPackageWeeklyDemandProjection.getWeek6() != null){
						weeklyResourceCount = weeklyResourceCount + jsonWorkPackageWeeklyDemandProjection.getWeek6();
					}
					weeklyReservedResourceCount = weeklyReservedResourceCount + jsonWorkPackageWeeklyDemandProjection.getReservedResourceCountWk6();
					jsonWorkPackageWeeklyDemandProjection.setReservedResourceCountWk6(weeklyReservedResourceCount);
					
					jsonWorkPackageWeeklyDemandProjection.setWeek6(weeklyResourceCount);	
					break;
				case 7:
					if(jsonWorkPackageWeeklyDemandProjection.getWeek7() != null){
						weeklyResourceCount = weeklyResourceCount + jsonWorkPackageWeeklyDemandProjection.getWeek7();
					}
					weeklyReservedResourceCount = weeklyReservedResourceCount + jsonWorkPackageWeeklyDemandProjection.getReservedResourceCountWk7();
					jsonWorkPackageWeeklyDemandProjection.setReservedResourceCountWk7(weeklyReservedResourceCount);
					
					jsonWorkPackageWeeklyDemandProjection.setWeek7(weeklyResourceCount);	
					break;
				case 8:
					if(jsonWorkPackageWeeklyDemandProjection.getWeek8() != null){
						weeklyResourceCount = weeklyResourceCount + jsonWorkPackageWeeklyDemandProjection.getWeek8();
					}
					weeklyReservedResourceCount = weeklyReservedResourceCount + jsonWorkPackageWeeklyDemandProjection.getReservedResourceCountWk8();
					jsonWorkPackageWeeklyDemandProjection.setReservedResourceCountWk8(weeklyReservedResourceCount);
					
					jsonWorkPackageWeeklyDemandProjection.setWeek8(weeklyResourceCount);	
					break;
				case 9:
					if(jsonWorkPackageWeeklyDemandProjection.getWeek9() != null){
						weeklyResourceCount = weeklyResourceCount + jsonWorkPackageWeeklyDemandProjection.getWeek9();
					}
					weeklyReservedResourceCount = weeklyReservedResourceCount + jsonWorkPackageWeeklyDemandProjection.getReservedResourceCountWk9();
					jsonWorkPackageWeeklyDemandProjection.setReservedResourceCountWk9(weeklyReservedResourceCount);
					
					jsonWorkPackageWeeklyDemandProjection.setWeek9(weeklyResourceCount);	
					break;
				case 10:
					if(jsonWorkPackageWeeklyDemandProjection.getWeek10() != null){
						weeklyResourceCount = weeklyResourceCount + jsonWorkPackageWeeklyDemandProjection.getWeek10();
					}
					weeklyReservedResourceCount = weeklyReservedResourceCount + jsonWorkPackageWeeklyDemandProjection.getReservedResourceCountWk10();
					jsonWorkPackageWeeklyDemandProjection.setReservedResourceCountWk10(weeklyReservedResourceCount);
					
					jsonWorkPackageWeeklyDemandProjection.setWeek10(weeklyResourceCount);	
					break;
				case 11:
					if(jsonWorkPackageWeeklyDemandProjection.getWeek11() != null){
						weeklyResourceCount = weeklyResourceCount + jsonWorkPackageWeeklyDemandProjection.getWeek11();
					}
					weeklyReservedResourceCount = weeklyReservedResourceCount + jsonWorkPackageWeeklyDemandProjection.getReservedResourceCountWk11();
					jsonWorkPackageWeeklyDemandProjection.setReservedResourceCountWk11(weeklyReservedResourceCount);
					
					jsonWorkPackageWeeklyDemandProjection.setWeek11(weeklyResourceCount);	
					break;
				case 12:
					if(jsonWorkPackageWeeklyDemandProjection.getWeek12() != null){
						weeklyResourceCount = weeklyResourceCount + jsonWorkPackageWeeklyDemandProjection.getWeek12();
					}
					weeklyReservedResourceCount = weeklyReservedResourceCount + jsonWorkPackageWeeklyDemandProjection.getReservedResourceCountWk12();
					jsonWorkPackageWeeklyDemandProjection.setReservedResourceCountWk12(weeklyReservedResourceCount);
					
					jsonWorkPackageWeeklyDemandProjection.setWeek12(weeklyResourceCount);	
					break;
				case 13:
					if(jsonWorkPackageWeeklyDemandProjection.getWeek13() != null){
						weeklyResourceCount = weeklyResourceCount + jsonWorkPackageWeeklyDemandProjection.getWeek13();
					}
					weeklyReservedResourceCount = weeklyReservedResourceCount + jsonWorkPackageWeeklyDemandProjection.getReservedResourceCountWk13();
					jsonWorkPackageWeeklyDemandProjection.setReservedResourceCountWk13(weeklyReservedResourceCount);
					
					jsonWorkPackageWeeklyDemandProjection.setWeek13(weeklyResourceCount);	
					break;
				case 14:
					if(jsonWorkPackageWeeklyDemandProjection.getWeek14() != null){
						weeklyResourceCount = weeklyResourceCount + jsonWorkPackageWeeklyDemandProjection.getWeek14();
					}
					weeklyReservedResourceCount = weeklyReservedResourceCount + jsonWorkPackageWeeklyDemandProjection.getReservedResourceCountWk14();
					jsonWorkPackageWeeklyDemandProjection.setReservedResourceCountWk14(weeklyReservedResourceCount);
					
					jsonWorkPackageWeeklyDemandProjection.setWeek14(weeklyResourceCount);
					break;
				case 15:
					if(jsonWorkPackageWeeklyDemandProjection.getWeek15() != null){
						weeklyResourceCount = weeklyResourceCount + jsonWorkPackageWeeklyDemandProjection.getWeek15();
					}
					weeklyReservedResourceCount = weeklyReservedResourceCount + jsonWorkPackageWeeklyDemandProjection.getReservedResourceCountWk15();
					jsonWorkPackageWeeklyDemandProjection.setReservedResourceCountWk15(weeklyReservedResourceCount);
					
					jsonWorkPackageWeeklyDemandProjection.setWeek15(weeklyResourceCount);
					break;
				case 16:
					if(jsonWorkPackageWeeklyDemandProjection.getWeek16() != null){
						weeklyResourceCount = weeklyResourceCount + jsonWorkPackageWeeklyDemandProjection.getWeek16();
					}
					weeklyReservedResourceCount = weeklyReservedResourceCount + jsonWorkPackageWeeklyDemandProjection.getReservedResourceCountWk16();
					jsonWorkPackageWeeklyDemandProjection.setReservedResourceCountWk16(weeklyReservedResourceCount);
					
					jsonWorkPackageWeeklyDemandProjection.setWeek16(weeklyResourceCount);	
					break;
				case 17:
					if(jsonWorkPackageWeeklyDemandProjection.getWeek17() != null){
						weeklyResourceCount = weeklyResourceCount + jsonWorkPackageWeeklyDemandProjection.getWeek17();
					}
					weeklyReservedResourceCount = weeklyReservedResourceCount + jsonWorkPackageWeeklyDemandProjection.getReservedResourceCountWk17();
					jsonWorkPackageWeeklyDemandProjection.setReservedResourceCountWk17(weeklyReservedResourceCount);
					
					jsonWorkPackageWeeklyDemandProjection.setWeek17(weeklyResourceCount);
					break;
				case 18:
					if(jsonWorkPackageWeeklyDemandProjection.getWeek18() != null){
						weeklyResourceCount = weeklyResourceCount + jsonWorkPackageWeeklyDemandProjection.getWeek18();
					}
					weeklyReservedResourceCount = weeklyReservedResourceCount + jsonWorkPackageWeeklyDemandProjection.getReservedResourceCountWk18();
					jsonWorkPackageWeeklyDemandProjection.setReservedResourceCountWk18(weeklyReservedResourceCount);
					
					jsonWorkPackageWeeklyDemandProjection.setWeek18(weeklyResourceCount);
					
					break;
				case 19:
					if(jsonWorkPackageWeeklyDemandProjection.getWeek19() != null){
						weeklyResourceCount = weeklyResourceCount + jsonWorkPackageWeeklyDemandProjection.getWeek19();
					}
					weeklyReservedResourceCount = weeklyReservedResourceCount + jsonWorkPackageWeeklyDemandProjection.getReservedResourceCountWk19();
					jsonWorkPackageWeeklyDemandProjection.setReservedResourceCountWk19(weeklyReservedResourceCount);
					
					jsonWorkPackageWeeklyDemandProjection.setWeek19(weeklyResourceCount);
					break;
				case 20:
					if(jsonWorkPackageWeeklyDemandProjection.getWeek20() != null){
						weeklyResourceCount = weeklyResourceCount + jsonWorkPackageWeeklyDemandProjection.getWeek20();
					}
					weeklyReservedResourceCount = weeklyReservedResourceCount + jsonWorkPackageWeeklyDemandProjection.getReservedResourceCountWk20();
					jsonWorkPackageWeeklyDemandProjection.setReservedResourceCountWk20(weeklyReservedResourceCount);
					
					jsonWorkPackageWeeklyDemandProjection.setWeek20(weeklyResourceCount);
					break;
				case 21:
					if(jsonWorkPackageWeeklyDemandProjection.getWeek21() != null){
						weeklyResourceCount = weeklyResourceCount + jsonWorkPackageWeeklyDemandProjection.getWeek21();
					}
					weeklyReservedResourceCount = weeklyReservedResourceCount + jsonWorkPackageWeeklyDemandProjection.getReservedResourceCountWk21();
					jsonWorkPackageWeeklyDemandProjection.setReservedResourceCountWk21(weeklyReservedResourceCount);
					
					jsonWorkPackageWeeklyDemandProjection.setWeek21(weeklyResourceCount);	
					break;
				case 22:
					if(jsonWorkPackageWeeklyDemandProjection.getWeek22() != null){
						weeklyResourceCount = weeklyResourceCount + jsonWorkPackageWeeklyDemandProjection.getWeek22();
					}
					weeklyReservedResourceCount = weeklyReservedResourceCount + jsonWorkPackageWeeklyDemandProjection.getReservedResourceCountWk22();
					jsonWorkPackageWeeklyDemandProjection.setReservedResourceCountWk22(weeklyReservedResourceCount);
					
					jsonWorkPackageWeeklyDemandProjection.setWeek22(weeklyResourceCount);	
					break;
				case 23:
					if(jsonWorkPackageWeeklyDemandProjection.getWeek23() != null){
						weeklyResourceCount = weeklyResourceCount + jsonWorkPackageWeeklyDemandProjection.getWeek23();
					}
					weeklyReservedResourceCount = weeklyReservedResourceCount + jsonWorkPackageWeeklyDemandProjection.getReservedResourceCountWk23();
					jsonWorkPackageWeeklyDemandProjection.setReservedResourceCountWk23(weeklyReservedResourceCount);
					
					jsonWorkPackageWeeklyDemandProjection.setWeek23(weeklyResourceCount);
					break;
				case 24:
					if(jsonWorkPackageWeeklyDemandProjection.getWeek24() != null){
						weeklyResourceCount = weeklyResourceCount + jsonWorkPackageWeeklyDemandProjection.getWeek24();
					}
					weeklyReservedResourceCount = weeklyReservedResourceCount + jsonWorkPackageWeeklyDemandProjection.getReservedResourceCountWk24();
					jsonWorkPackageWeeklyDemandProjection.setReservedResourceCountWk24(weeklyReservedResourceCount);
					
					jsonWorkPackageWeeklyDemandProjection.setWeek24(weeklyResourceCount);
					break;
				case 25:
					if(jsonWorkPackageWeeklyDemandProjection.getWeek25() != null){
						weeklyResourceCount = weeklyResourceCount + jsonWorkPackageWeeklyDemandProjection.getWeek25();
					}
					weeklyReservedResourceCount = weeklyReservedResourceCount + jsonWorkPackageWeeklyDemandProjection.getReservedResourceCountWk25();
					jsonWorkPackageWeeklyDemandProjection.setReservedResourceCountWk25(weeklyReservedResourceCount);
					
					jsonWorkPackageWeeklyDemandProjection.setWeek25(weeklyResourceCount);	
					break;
				case 26:
					if(jsonWorkPackageWeeklyDemandProjection.getWeek26() != null){
						weeklyResourceCount = weeklyResourceCount + jsonWorkPackageWeeklyDemandProjection.getWeek26();
					}
					weeklyReservedResourceCount = weeklyReservedResourceCount + jsonWorkPackageWeeklyDemandProjection.getReservedResourceCountWk26();
					jsonWorkPackageWeeklyDemandProjection.setReservedResourceCountWk26(weeklyReservedResourceCount);
					
					jsonWorkPackageWeeklyDemandProjection.setWeek26(weeklyResourceCount);
					break;
				case 27:
					if(jsonWorkPackageWeeklyDemandProjection.getWeek27() != null){
						weeklyResourceCount = weeklyResourceCount + jsonWorkPackageWeeklyDemandProjection.getWeek27();
					}
					weeklyReservedResourceCount = weeklyReservedResourceCount + jsonWorkPackageWeeklyDemandProjection.getReservedResourceCountWk27();
					jsonWorkPackageWeeklyDemandProjection.setReservedResourceCountWk27(weeklyReservedResourceCount);
					
					jsonWorkPackageWeeklyDemandProjection.setWeek27(weeklyResourceCount);
					break;
				case 28:
					if(jsonWorkPackageWeeklyDemandProjection.getWeek28() != null){
						weeklyResourceCount = weeklyResourceCount + jsonWorkPackageWeeklyDemandProjection.getWeek28();
					}
					weeklyReservedResourceCount = weeklyReservedResourceCount + jsonWorkPackageWeeklyDemandProjection.getReservedResourceCountWk28();
					jsonWorkPackageWeeklyDemandProjection.setReservedResourceCountWk28(weeklyReservedResourceCount);
					
					jsonWorkPackageWeeklyDemandProjection.setWeek28(weeklyResourceCount);
					break;
				case 29:
					if(jsonWorkPackageWeeklyDemandProjection.getWeek29() != null){
						weeklyResourceCount = weeklyResourceCount + jsonWorkPackageWeeklyDemandProjection.getWeek29();
					}
					weeklyReservedResourceCount = weeklyReservedResourceCount + jsonWorkPackageWeeklyDemandProjection.getReservedResourceCountWk29();
					jsonWorkPackageWeeklyDemandProjection.setReservedResourceCountWk29(weeklyReservedResourceCount);
					
					jsonWorkPackageWeeklyDemandProjection.setWeek29(weeklyResourceCount);
					break;
				case 30:
					if(jsonWorkPackageWeeklyDemandProjection.getWeek30() != null){
						weeklyResourceCount = weeklyResourceCount + jsonWorkPackageWeeklyDemandProjection.getWeek30();
					}
					weeklyReservedResourceCount = weeklyReservedResourceCount + jsonWorkPackageWeeklyDemandProjection.getReservedResourceCountWk30();
					jsonWorkPackageWeeklyDemandProjection.setReservedResourceCountWk30(weeklyReservedResourceCount);
					
					jsonWorkPackageWeeklyDemandProjection.setWeek30(weeklyResourceCount);
					break;
				case 31:
					if(jsonWorkPackageWeeklyDemandProjection.getWeek31() != null){
						weeklyResourceCount = weeklyResourceCount + jsonWorkPackageWeeklyDemandProjection.getWeek31();
					}
					weeklyReservedResourceCount = weeklyReservedResourceCount + jsonWorkPackageWeeklyDemandProjection.getReservedResourceCountWk31();
					jsonWorkPackageWeeklyDemandProjection.setReservedResourceCountWk31(weeklyReservedResourceCount);
					
					jsonWorkPackageWeeklyDemandProjection.setWeek31(weeklyResourceCount);	
					break;
				case 32:
					if(jsonWorkPackageWeeklyDemandProjection.getWeek32() != null){
						weeklyResourceCount = weeklyResourceCount + jsonWorkPackageWeeklyDemandProjection.getWeek32();
					}
					weeklyReservedResourceCount = weeklyReservedResourceCount + jsonWorkPackageWeeklyDemandProjection.getReservedResourceCountWk32();
					jsonWorkPackageWeeklyDemandProjection.setReservedResourceCountWk32(weeklyReservedResourceCount);
					
					jsonWorkPackageWeeklyDemandProjection.setWeek32(weeklyResourceCount);
					break;
				case 33:
					if(jsonWorkPackageWeeklyDemandProjection.getWeek33() != null){
						weeklyResourceCount = weeklyResourceCount + jsonWorkPackageWeeklyDemandProjection.getWeek33();
					}
					weeklyReservedResourceCount = weeklyReservedResourceCount + jsonWorkPackageWeeklyDemandProjection.getReservedResourceCountWk33();
					jsonWorkPackageWeeklyDemandProjection.setReservedResourceCountWk33(weeklyReservedResourceCount);
					
					jsonWorkPackageWeeklyDemandProjection.setWeek33(weeklyResourceCount);
					break;
				case 34:
					if(jsonWorkPackageWeeklyDemandProjection.getWeek34() != null){
						weeklyResourceCount = weeklyResourceCount + jsonWorkPackageWeeklyDemandProjection.getWeek34();
					}
					weeklyReservedResourceCount = weeklyReservedResourceCount + jsonWorkPackageWeeklyDemandProjection.getReservedResourceCountWk34();
					jsonWorkPackageWeeklyDemandProjection.setReservedResourceCountWk34(weeklyReservedResourceCount);
					
					jsonWorkPackageWeeklyDemandProjection.setWeek34(weeklyResourceCount);	
					break;
				case 35:
					if(jsonWorkPackageWeeklyDemandProjection.getWeek35() != null){
						weeklyResourceCount = weeklyResourceCount + jsonWorkPackageWeeklyDemandProjection.getWeek35();
					}
					weeklyReservedResourceCount = weeklyReservedResourceCount + jsonWorkPackageWeeklyDemandProjection.getReservedResourceCountWk35();
					jsonWorkPackageWeeklyDemandProjection.setReservedResourceCountWk35(weeklyReservedResourceCount);
					
					jsonWorkPackageWeeklyDemandProjection.setWeek35(weeklyResourceCount);	
					break;
				case 36:
					if(jsonWorkPackageWeeklyDemandProjection.getWeek36() != null){
						weeklyResourceCount = weeklyResourceCount + jsonWorkPackageWeeklyDemandProjection.getWeek36();
					}
					weeklyReservedResourceCount = weeklyReservedResourceCount + jsonWorkPackageWeeklyDemandProjection.getReservedResourceCountWk36();
					jsonWorkPackageWeeklyDemandProjection.setReservedResourceCountWk36(weeklyReservedResourceCount);
					
					jsonWorkPackageWeeklyDemandProjection.setWeek36(weeklyResourceCount);	
					break;
				case 37:
					if(jsonWorkPackageWeeklyDemandProjection.getWeek37() != null){
						weeklyResourceCount = weeklyResourceCount + jsonWorkPackageWeeklyDemandProjection.getWeek37();
					}
					weeklyReservedResourceCount = weeklyReservedResourceCount + jsonWorkPackageWeeklyDemandProjection.getReservedResourceCountWk37();
					jsonWorkPackageWeeklyDemandProjection.setReservedResourceCountWk37(weeklyReservedResourceCount);
					
					jsonWorkPackageWeeklyDemandProjection.setWeek37(weeklyResourceCount);	
					break;
				case 38:
					if(jsonWorkPackageWeeklyDemandProjection.getWeek38() != null){
						weeklyResourceCount = weeklyResourceCount + jsonWorkPackageWeeklyDemandProjection.getWeek38();
					}
					weeklyReservedResourceCount = weeklyReservedResourceCount + jsonWorkPackageWeeklyDemandProjection.getReservedResourceCountWk38();
					jsonWorkPackageWeeklyDemandProjection.setReservedResourceCountWk38(weeklyReservedResourceCount);
					
					jsonWorkPackageWeeklyDemandProjection.setWeek38(weeklyResourceCount);	
					break;
				case 39:
					if(jsonWorkPackageWeeklyDemandProjection.getWeek39() != null){
						weeklyResourceCount = weeklyResourceCount + jsonWorkPackageWeeklyDemandProjection.getWeek39();
					}
					weeklyReservedResourceCount = weeklyReservedResourceCount + jsonWorkPackageWeeklyDemandProjection.getReservedResourceCountWk39();
					jsonWorkPackageWeeklyDemandProjection.setReservedResourceCountWk39(weeklyReservedResourceCount);
					
					jsonWorkPackageWeeklyDemandProjection.setWeek39(weeklyResourceCount);
					break;
				case 40:
					if(jsonWorkPackageWeeklyDemandProjection.getWeek40() != null){
						weeklyResourceCount = weeklyResourceCount + jsonWorkPackageWeeklyDemandProjection.getWeek40();
					}
					weeklyReservedResourceCount = weeklyReservedResourceCount + jsonWorkPackageWeeklyDemandProjection.getReservedResourceCountWk40();
					jsonWorkPackageWeeklyDemandProjection.setReservedResourceCountWk40(weeklyReservedResourceCount);
					
					jsonWorkPackageWeeklyDemandProjection.setWeek40(weeklyResourceCount);
					break;
				case 41:
					if(jsonWorkPackageWeeklyDemandProjection.getWeek41() != null){
						weeklyResourceCount = weeklyResourceCount + jsonWorkPackageWeeklyDemandProjection.getWeek41();
					}
					weeklyReservedResourceCount = weeklyReservedResourceCount + jsonWorkPackageWeeklyDemandProjection.getReservedResourceCountWk41();
					jsonWorkPackageWeeklyDemandProjection.setReservedResourceCountWk41(weeklyReservedResourceCount);
					
					jsonWorkPackageWeeklyDemandProjection.setWeek41(weeklyResourceCount);
					break;
				case 42:
					if(jsonWorkPackageWeeklyDemandProjection.getWeek42() != null){
						weeklyResourceCount = weeklyResourceCount + jsonWorkPackageWeeklyDemandProjection.getWeek42();
					}
					weeklyReservedResourceCount = weeklyReservedResourceCount + jsonWorkPackageWeeklyDemandProjection.getReservedResourceCountWk42();
					jsonWorkPackageWeeklyDemandProjection.setReservedResourceCountWk42(weeklyReservedResourceCount);
					
					jsonWorkPackageWeeklyDemandProjection.setWeek42(weeklyResourceCount);
					break;
				case 43:
					if(jsonWorkPackageWeeklyDemandProjection.getWeek43() != null){
						weeklyResourceCount = weeklyResourceCount + jsonWorkPackageWeeklyDemandProjection.getWeek43();
					}
					weeklyReservedResourceCount = weeklyReservedResourceCount + jsonWorkPackageWeeklyDemandProjection.getReservedResourceCountWk43();
					jsonWorkPackageWeeklyDemandProjection.setReservedResourceCountWk43(weeklyReservedResourceCount);
					
					jsonWorkPackageWeeklyDemandProjection.setWeek43(weeklyResourceCount);	
					break;
				case 44:
					if(jsonWorkPackageWeeklyDemandProjection.getWeek44() != null){
						weeklyResourceCount = weeklyResourceCount + jsonWorkPackageWeeklyDemandProjection.getWeek44();
					}
					weeklyReservedResourceCount = weeklyReservedResourceCount + jsonWorkPackageWeeklyDemandProjection.getReservedResourceCountWk44();
					jsonWorkPackageWeeklyDemandProjection.setReservedResourceCountWk44(weeklyReservedResourceCount);
					
					jsonWorkPackageWeeklyDemandProjection.setWeek44(weeklyResourceCount);	
					break;
				case 45:
					if(jsonWorkPackageWeeklyDemandProjection.getWeek45() != null){
						weeklyResourceCount = weeklyResourceCount + jsonWorkPackageWeeklyDemandProjection.getWeek45();
					}
					weeklyReservedResourceCount = weeklyReservedResourceCount + jsonWorkPackageWeeklyDemandProjection.getReservedResourceCountWk45();
					jsonWorkPackageWeeklyDemandProjection.setReservedResourceCountWk45(weeklyReservedResourceCount);
					
					jsonWorkPackageWeeklyDemandProjection.setWeek45(weeklyResourceCount);	
					break;
				case 46:
					if(jsonWorkPackageWeeklyDemandProjection.getWeek46() != null){
						weeklyResourceCount = weeklyResourceCount + jsonWorkPackageWeeklyDemandProjection.getWeek46();
					}
					weeklyReservedResourceCount = weeklyReservedResourceCount + jsonWorkPackageWeeklyDemandProjection.getReservedResourceCountWk46();
					jsonWorkPackageWeeklyDemandProjection.setReservedResourceCountWk46(weeklyReservedResourceCount);
					
					jsonWorkPackageWeeklyDemandProjection.setWeek46(weeklyResourceCount);
					break;
				case 47:
					if(jsonWorkPackageWeeklyDemandProjection.getWeek47() != null){
						weeklyResourceCount = weeklyResourceCount + jsonWorkPackageWeeklyDemandProjection.getWeek47();
					}
					weeklyReservedResourceCount = weeklyReservedResourceCount + jsonWorkPackageWeeklyDemandProjection.getReservedResourceCountWk47();
					jsonWorkPackageWeeklyDemandProjection.setReservedResourceCountWk47(weeklyReservedResourceCount);
					
					jsonWorkPackageWeeklyDemandProjection.setWeek47(weeklyResourceCount);
					break;
				case 48:
					if(jsonWorkPackageWeeklyDemandProjection.getWeek48() != null){
						weeklyResourceCount = weeklyResourceCount + jsonWorkPackageWeeklyDemandProjection.getWeek48();
					}
					weeklyReservedResourceCount = weeklyReservedResourceCount + jsonWorkPackageWeeklyDemandProjection.getReservedResourceCountWk48();
					jsonWorkPackageWeeklyDemandProjection.setReservedResourceCountWk48(weeklyReservedResourceCount);
					
					jsonWorkPackageWeeklyDemandProjection.setWeek48(weeklyResourceCount);	
					break;
				case 49:
					if(jsonWorkPackageWeeklyDemandProjection.getWeek49() != null){
						weeklyResourceCount = weeklyResourceCount + jsonWorkPackageWeeklyDemandProjection.getWeek49();
					}
					weeklyReservedResourceCount = weeklyReservedResourceCount + jsonWorkPackageWeeklyDemandProjection.getReservedResourceCountWk49();
					jsonWorkPackageWeeklyDemandProjection.setReservedResourceCountWk49(weeklyReservedResourceCount);
					
					jsonWorkPackageWeeklyDemandProjection.setWeek49(weeklyResourceCount);	
					break;
				case 50:
					if(jsonWorkPackageWeeklyDemandProjection.getWeek50() != null){
						weeklyResourceCount = weeklyResourceCount + jsonWorkPackageWeeklyDemandProjection.getWeek50();
					}
					weeklyReservedResourceCount = weeklyReservedResourceCount + jsonWorkPackageWeeklyDemandProjection.getReservedResourceCountWk50();
					jsonWorkPackageWeeklyDemandProjection.setReservedResourceCountWk50(weeklyReservedResourceCount);
					
					jsonWorkPackageWeeklyDemandProjection.setWeek50(weeklyResourceCount);
					break;
				case 51:
					if(jsonWorkPackageWeeklyDemandProjection.getWeek51() != null){
						weeklyResourceCount = weeklyResourceCount + jsonWorkPackageWeeklyDemandProjection.getWeek51();
					}
					weeklyReservedResourceCount = weeklyReservedResourceCount + jsonWorkPackageWeeklyDemandProjection.getReservedResourceCountWk51();
					jsonWorkPackageWeeklyDemandProjection.setReservedResourceCountWk51(weeklyReservedResourceCount);
					
					jsonWorkPackageWeeklyDemandProjection.setWeek51(weeklyResourceCount);
					break;
									
				case 52:
					if(jsonWorkPackageWeeklyDemandProjection.getWeek52() != null){
						weeklyResourceCount = weeklyResourceCount + jsonWorkPackageWeeklyDemandProjection.getWeek52();
					}
					weeklyReservedResourceCount = weeklyReservedResourceCount + jsonWorkPackageWeeklyDemandProjection.getReservedResourceCountWk52();
					jsonWorkPackageWeeklyDemandProjection.setReservedResourceCountWk52(weeklyReservedResourceCount);
					
					jsonWorkPackageWeeklyDemandProjection.setWeek52(weeklyResourceCount);	
					break;
			}
		
		}
		return jsonWorkPackageWeeklyDemandProjections;
	}

	
	private JsonWorkPackageWeeklyDemandProjection getJsonWorkPackageWeeklyDemandForUserId(List<JsonWorkPackageWeeklyDemandProjection> jsonWorkPackageWeeklyDemandProjections, WeeklyResourceDemandDTO weeklyResourceDemandDTO) {
		
		for (JsonWorkPackageWeeklyDemandProjection jsonWorkPackageWeeklyDemandProjection : jsonWorkPackageWeeklyDemandProjections) {
			
			if ( jsonWorkPackageWeeklyDemandProjection.getResourcePoolId().equals(weeklyResourceDemandDTO.getResourcePoolId())
					&& jsonWorkPackageWeeklyDemandProjection.getWorkPackageId() == weeklyResourceDemandDTO.getWorkPackageId()
					&& jsonWorkPackageWeeklyDemandProjection.getProductId().equals(weeklyResourceDemandDTO.getProductId())
					&& jsonWorkPackageWeeklyDemandProjection.getShiftId().equals(weeklyResourceDemandDTO.getShiftId()) 
					&& jsonWorkPackageWeeklyDemandProjection.getWorkYear().equals(weeklyResourceDemandDTO.getWorkYear() ) 
					&& jsonWorkPackageWeeklyDemandProjection.getUserRoleId().equals(weeklyResourceDemandDTO.getRoleId())
					&& jsonWorkPackageWeeklyDemandProjection.getSkillId().equals(weeklyResourceDemandDTO.getSkillId())
					&& jsonWorkPackageWeeklyDemandProjection.getUserTypeId().equals(weeklyResourceDemandDTO.getUserTypeId())
					&& jsonWorkPackageWeeklyDemandProjection.getUserId().equals(weeklyResourceDemandDTO.getUserId())
			) {
				return jsonWorkPackageWeeklyDemandProjection;
			}
		}
		return null;
	}
	
	@Override
	@Transactional
	public List<JsonWorkPackageWeeklyDemandProjection> getResourceAllocationDetailedWeekly(Integer labId, Integer resourcePoolId, Integer testFactoryId, Integer productId, Integer workpackageId, Integer userId, List<Integer> workWeek, Integer workYear) {
		List<JsonWorkPackageWeeklyDemandProjection> jsonWorkPackageWeeklyDemandProjections = new ArrayList<JsonWorkPackageWeeklyDemandProjection>();
		try{
			List<WeeklyResourceDemandDTO> weeklyResourceDemandDTOs = resourceAvailabilityDAO.listResourcePoolResourceReservationDetailedWeeklyProjection(labId, resourcePoolId, testFactoryId, productId, workpackageId, userId, workWeek, workYear);
			Calendar startDate = Calendar.getInstance();
			startDate.set(Calendar.DAY_OF_MONTH, 1);
			startDate.set(Calendar.MONTH, Calendar.JANUARY);
			if(workYear != null && workYear != 0){
				startDate.set(Calendar.YEAR, workYear);
			}
			startDate.set(Calendar.HOUR_OF_DAY, 0);
			startDate.set(Calendar.MINUTE, 0);
			startDate.set(Calendar.SECOND, 0);
			
			Calendar endDate = (Calendar) startDate.clone();
			endDate.add(Calendar.YEAR, 1);
			
			for (WeeklyResourceDemandDTO weeklyResourceDemandDTO : weeklyResourceDemandDTOs) {
				
				JsonWorkPackageWeeklyDemandProjection jsonWorkPackageWeeklyDemandProjection = getJsonWorkPackageWeeklyDemandForUserId(jsonWorkPackageWeeklyDemandProjections, weeklyResourceDemandDTO);
				
				if(jsonWorkPackageWeeklyDemandProjection == null ){
					jsonWorkPackageWeeklyDemandProjection = new JsonWorkPackageWeeklyDemandProjection();
					
					jsonWorkPackageWeeklyDemandProjection.setTestFactoryId(weeklyResourceDemandDTO.getTestFactoryId());
					jsonWorkPackageWeeklyDemandProjection.setTestFactoryName(weeklyResourceDemandDTO.getTestFactoryName());
					
					jsonWorkPackageWeeklyDemandProjection.setProductId(weeklyResourceDemandDTO.getProductId());
					jsonWorkPackageWeeklyDemandProjection.setProductName(weeklyResourceDemandDTO.getProductName());
					
					jsonWorkPackageWeeklyDemandProjection.setWorkPackageId(weeklyResourceDemandDTO.getWorkPackageId());
					jsonWorkPackageWeeklyDemandProjection.setWorkPackageName(weeklyResourceDemandDTO.getWorkPackageName());
									
					jsonWorkPackageWeeklyDemandProjection.setSkillId(weeklyResourceDemandDTO.getSkillId());
					jsonWorkPackageWeeklyDemandProjection.setSkillName(weeklyResourceDemandDTO.getSkillName());
					
					jsonWorkPackageWeeklyDemandProjection.setShiftId(weeklyResourceDemandDTO.getShiftId());
					jsonWorkPackageWeeklyDemandProjection.setShiftName(weeklyResourceDemandDTO.getShiftName());
					
					jsonWorkPackageWeeklyDemandProjection.setUserRoleId(weeklyResourceDemandDTO.getRoleId());
					jsonWorkPackageWeeklyDemandProjection.setUserRoleName(weeklyResourceDemandDTO.getRoleName());
					
					jsonWorkPackageWeeklyDemandProjection.setUserTypeId(weeklyResourceDemandDTO.getUserTypeId());
					jsonWorkPackageWeeklyDemandProjection.setUserTypeName(weeklyResourceDemandDTO.getUserTypeName());
					
					jsonWorkPackageWeeklyDemandProjection.setResourcePoolId(weeklyResourceDemandDTO.getResourcePoolId());
					jsonWorkPackageWeeklyDemandProjection.setResourcePoolName(weeklyResourceDemandDTO.getResourcePoolName());
					
					jsonWorkPackageWeeklyDemandProjection.setWorkYear(weeklyResourceDemandDTO.getWorkYear());
					
					jsonWorkPackageWeeklyDemandProjection.setUserId(weeklyResourceDemandDTO.getUserId());
					jsonWorkPackageWeeklyDemandProjection.setUserName(weeklyResourceDemandDTO.getUserName());
					jsonWorkPackageWeeklyDemandProjection.setUserCode(weeklyResourceDemandDTO.getUserCode());
					
					jsonWorkPackageWeeklyDemandProjections.add(jsonWorkPackageWeeklyDemandProjection);
				}
				
				
				List<UserResourcePoolMapping> userResourcePoolMappings = userListDAO.getUserResourcePoolMappingByFilter(userId, null,startDate.getTime(), endDate.getTime());
				if(userResourcePoolMappings != null && userResourcePoolMappings.size() > 0){
					for(UserResourcePoolMapping userResourcePoolMapping : userResourcePoolMappings){
						Calendar weekCalendar = Calendar.getInstance();
						weekCalendar.setFirstDayOfWeek(Calendar.MONDAY);
						weekCalendar.setTime(userResourcePoolMapping.getFromDate());
						if(weekCalendar.get(Calendar.DAY_OF_WEEK) < Calendar.MONDAY){
							weekCalendar.add(Calendar.DATE, 1);
						}else if(weekCalendar.get(Calendar.DAY_OF_WEEK) > Calendar.MONDAY){
							weekCalendar.add(Calendar.DATE, ((Calendar.SATURDAY - weekCalendar.get(Calendar.DAY_OF_WEEK) + 2)));
						}
						
						while(weekCalendar.getTime().before(userResourcePoolMapping.getToDate()) || weekCalendar.getTime().equals(userResourcePoolMapping.getToDate())){
							if(weekCalendar.get(Calendar.DAY_OF_WEEK) == Calendar.MONDAY){
								Integer weekNoOfDate = DateUtility.getWeekNumberOfDate(weekCalendar.getTime());
								if(!jsonWorkPackageWeeklyDemandProjection.getReservedWeeks().contains(weekNoOfDate)){
									jsonWorkPackageWeeklyDemandProjection.getReservedWeeks().add(weekNoOfDate);
								}
							}
							weekCalendar.add(Calendar.WEEK_OF_YEAR, 1);
						}
					}
				}
				setWeeksCount(jsonWorkPackageWeeklyDemandProjection, weeklyResourceDemandDTO);
				
			}
			
		}catch(Exception ex){
			log.error("Exception in getResourceAllocationDetailedWeekly - ", ex);
		}
		return jsonWorkPackageWeeklyDemandProjections;
		
	}

	
	
	
	
	private void setWeeksCount(JsonWorkPackageWeeklyDemandProjection jsonWorkPackageWeeklyDemandProjection, WeeklyResourceDemandDTO weeklyResourceDemandDTO){
		Integer workWeekNo = weeklyResourceDemandDTO.getWorkWeek();
		if(workWeekNo == null){
			workWeekNo = 0;
		}
		
		float weeklyResourceCount = weeklyResourceDemandDTO.getResourceCount();
		
		float weeklyReservedResourceCount = weeklyResourceDemandDTO.getReservedResourceCount();
		weeklyReservedResourceCount= weeklyReservedResourceCount/100;
		
		jsonWorkPackageWeeklyDemandProjection.setReservedResourceCount(jsonWorkPackageWeeklyDemandProjection.getReservedResourceCount() + weeklyReservedResourceCount);
		if(!jsonWorkPackageWeeklyDemandProjection.getReservedWeeks().contains(workWeekNo)){
			jsonWorkPackageWeeklyDemandProjection.getReservedWeeks().add(workWeekNo);
		}
		
		switch (workWeekNo) {
			case 1:
				if(jsonWorkPackageWeeklyDemandProjection.getWeek1() != null){
					weeklyResourceCount = weeklyResourceCount + jsonWorkPackageWeeklyDemandProjection.getWeek1();
				}
				weeklyReservedResourceCount = weeklyReservedResourceCount + jsonWorkPackageWeeklyDemandProjection.getReservedResourceCountWk1();
				jsonWorkPackageWeeklyDemandProjection.setReservedResourceCountWk1(weeklyReservedResourceCount);
				
				jsonWorkPackageWeeklyDemandProjection.setWeek1(weeklyResourceCount);
				
				break;
			case 2:
				if(jsonWorkPackageWeeklyDemandProjection.getWeek2() != null){
					weeklyResourceCount = weeklyResourceCount + jsonWorkPackageWeeklyDemandProjection.getWeek2();
				}
				weeklyReservedResourceCount = weeklyReservedResourceCount + jsonWorkPackageWeeklyDemandProjection.getReservedResourceCountWk2();
				jsonWorkPackageWeeklyDemandProjection.setReservedResourceCountWk2(weeklyReservedResourceCount);
				
				jsonWorkPackageWeeklyDemandProjection.setWeek2(weeklyResourceCount);	
				break;
			case 3:
				if(jsonWorkPackageWeeklyDemandProjection.getWeek3() != null){
					weeklyResourceCount = weeklyResourceCount + jsonWorkPackageWeeklyDemandProjection.getWeek3();
				}
				weeklyReservedResourceCount = weeklyReservedResourceCount + jsonWorkPackageWeeklyDemandProjection.getReservedResourceCountWk3();
				jsonWorkPackageWeeklyDemandProjection.setReservedResourceCountWk3(weeklyReservedResourceCount);
				
				jsonWorkPackageWeeklyDemandProjection.setWeek3(weeklyResourceCount);
				break;
			case 4:
				if(jsonWorkPackageWeeklyDemandProjection.getWeek4() != null){
					weeklyResourceCount = weeklyResourceCount + jsonWorkPackageWeeklyDemandProjection.getWeek4();
				}
				weeklyReservedResourceCount = weeklyReservedResourceCount + jsonWorkPackageWeeklyDemandProjection.getReservedResourceCountWk4();
				jsonWorkPackageWeeklyDemandProjection.setReservedResourceCountWk4(weeklyReservedResourceCount);
				
				jsonWorkPackageWeeklyDemandProjection.setWeek4(weeklyResourceCount);	
				break;
			case 5:
				if(jsonWorkPackageWeeklyDemandProjection.getWeek5() != null){
					weeklyResourceCount = weeklyResourceCount + jsonWorkPackageWeeklyDemandProjection.getWeek5();
				}
				weeklyReservedResourceCount = weeklyReservedResourceCount + jsonWorkPackageWeeklyDemandProjection.getReservedResourceCountWk5();
				jsonWorkPackageWeeklyDemandProjection.setReservedResourceCountWk5(weeklyReservedResourceCount);
				
				jsonWorkPackageWeeklyDemandProjection.setWeek5(weeklyResourceCount);	
				break;
			case 6:
				if(jsonWorkPackageWeeklyDemandProjection.getWeek6() != null){
					weeklyResourceCount = weeklyResourceCount + jsonWorkPackageWeeklyDemandProjection.getWeek6();
				}
				weeklyReservedResourceCount = weeklyReservedResourceCount + jsonWorkPackageWeeklyDemandProjection.getReservedResourceCountWk6();
				jsonWorkPackageWeeklyDemandProjection.setReservedResourceCountWk6(weeklyReservedResourceCount);
				
				jsonWorkPackageWeeklyDemandProjection.setWeek6(weeklyResourceCount);	
				break;
			case 7:
				if(jsonWorkPackageWeeklyDemandProjection.getWeek7() != null){
					weeklyResourceCount = weeklyResourceCount + jsonWorkPackageWeeklyDemandProjection.getWeek7();
				}
				weeklyReservedResourceCount = weeklyReservedResourceCount + jsonWorkPackageWeeklyDemandProjection.getReservedResourceCountWk7();
				jsonWorkPackageWeeklyDemandProjection.setReservedResourceCountWk7(weeklyReservedResourceCount);
				
				jsonWorkPackageWeeklyDemandProjection.setWeek7(weeklyResourceCount);	
				break;
			case 8:
				if(jsonWorkPackageWeeklyDemandProjection.getWeek8() != null){
					weeklyResourceCount = weeklyResourceCount + jsonWorkPackageWeeklyDemandProjection.getWeek8();
				}
				weeklyReservedResourceCount = weeklyReservedResourceCount + jsonWorkPackageWeeklyDemandProjection.getReservedResourceCountWk8();
				jsonWorkPackageWeeklyDemandProjection.setReservedResourceCountWk8(weeklyReservedResourceCount);
				
				jsonWorkPackageWeeklyDemandProjection.setWeek8(weeklyResourceCount);	
				break;
			case 9:
				if(jsonWorkPackageWeeklyDemandProjection.getWeek9() != null){
					weeklyResourceCount = weeklyResourceCount + jsonWorkPackageWeeklyDemandProjection.getWeek9();
				}
				weeklyReservedResourceCount = weeklyReservedResourceCount + jsonWorkPackageWeeklyDemandProjection.getReservedResourceCountWk9();
				jsonWorkPackageWeeklyDemandProjection.setReservedResourceCountWk9(weeklyReservedResourceCount);
				
				jsonWorkPackageWeeklyDemandProjection.setWeek9(weeklyResourceCount);	
				break;
			case 10:
				if(jsonWorkPackageWeeklyDemandProjection.getWeek10() != null){
					weeklyResourceCount = weeklyResourceCount + jsonWorkPackageWeeklyDemandProjection.getWeek10();
				}
				weeklyReservedResourceCount = weeklyReservedResourceCount + jsonWorkPackageWeeklyDemandProjection.getReservedResourceCountWk10();
				jsonWorkPackageWeeklyDemandProjection.setReservedResourceCountWk10(weeklyReservedResourceCount);
				
				jsonWorkPackageWeeklyDemandProjection.setWeek10(weeklyResourceCount);	
				break;
			case 11:
				if(jsonWorkPackageWeeklyDemandProjection.getWeek11() != null){
					weeklyResourceCount = weeklyResourceCount + jsonWorkPackageWeeklyDemandProjection.getWeek11();
				}
				weeklyReservedResourceCount = weeklyReservedResourceCount + jsonWorkPackageWeeklyDemandProjection.getReservedResourceCountWk11();
				jsonWorkPackageWeeklyDemandProjection.setReservedResourceCountWk11(weeklyReservedResourceCount);
				
				jsonWorkPackageWeeklyDemandProjection.setWeek11(weeklyResourceCount);	
				break;
			case 12:
				if(jsonWorkPackageWeeklyDemandProjection.getWeek12() != null){
					weeklyResourceCount = weeklyResourceCount + jsonWorkPackageWeeklyDemandProjection.getWeek12();
				}
				weeklyReservedResourceCount = weeklyReservedResourceCount + jsonWorkPackageWeeklyDemandProjection.getReservedResourceCountWk12();
				jsonWorkPackageWeeklyDemandProjection.setReservedResourceCountWk12(weeklyReservedResourceCount);
				
				jsonWorkPackageWeeklyDemandProjection.setWeek12(weeklyResourceCount);	
				break;
			case 13:
				if(jsonWorkPackageWeeklyDemandProjection.getWeek13() != null){
					weeklyResourceCount = weeklyResourceCount + jsonWorkPackageWeeklyDemandProjection.getWeek13();
				}
				weeklyReservedResourceCount = weeklyReservedResourceCount + jsonWorkPackageWeeklyDemandProjection.getReservedResourceCountWk13();
				jsonWorkPackageWeeklyDemandProjection.setReservedResourceCountWk13(weeklyReservedResourceCount);
				
				jsonWorkPackageWeeklyDemandProjection.setWeek13(weeklyResourceCount);	
				break;
			case 14:
				if(jsonWorkPackageWeeklyDemandProjection.getWeek14() != null){
					weeklyResourceCount = weeklyResourceCount + jsonWorkPackageWeeklyDemandProjection.getWeek14();
				}
				weeklyReservedResourceCount = weeklyReservedResourceCount + jsonWorkPackageWeeklyDemandProjection.getReservedResourceCountWk14();
				jsonWorkPackageWeeklyDemandProjection.setReservedResourceCountWk14(weeklyReservedResourceCount);
				
				jsonWorkPackageWeeklyDemandProjection.setWeek14(weeklyResourceCount);
				break;
			case 15:
				if(jsonWorkPackageWeeklyDemandProjection.getWeek15() != null){
					weeklyResourceCount = weeklyResourceCount + jsonWorkPackageWeeklyDemandProjection.getWeek15();
				}
				weeklyReservedResourceCount = weeklyReservedResourceCount + jsonWorkPackageWeeklyDemandProjection.getReservedResourceCountWk15();
				jsonWorkPackageWeeklyDemandProjection.setReservedResourceCountWk15(weeklyReservedResourceCount);
				
				jsonWorkPackageWeeklyDemandProjection.setWeek15(weeklyResourceCount);
				break;
			case 16:
				if(jsonWorkPackageWeeklyDemandProjection.getWeek16() != null){
					weeklyResourceCount = weeklyResourceCount + jsonWorkPackageWeeklyDemandProjection.getWeek16();
				}
				weeklyReservedResourceCount = weeklyReservedResourceCount + jsonWorkPackageWeeklyDemandProjection.getReservedResourceCountWk16();
				jsonWorkPackageWeeklyDemandProjection.setReservedResourceCountWk16(weeklyReservedResourceCount);
				
				jsonWorkPackageWeeklyDemandProjection.setWeek16(weeklyResourceCount);	
				break;
			case 17:
				if(jsonWorkPackageWeeklyDemandProjection.getWeek17() != null){
					weeklyResourceCount = weeklyResourceCount + jsonWorkPackageWeeklyDemandProjection.getWeek17();
				}
				weeklyReservedResourceCount = weeklyReservedResourceCount + jsonWorkPackageWeeklyDemandProjection.getReservedResourceCountWk17();
				jsonWorkPackageWeeklyDemandProjection.setReservedResourceCountWk17(weeklyReservedResourceCount);
				
				jsonWorkPackageWeeklyDemandProjection.setWeek17(weeklyResourceCount);
				break;
			case 18:
				if(jsonWorkPackageWeeklyDemandProjection.getWeek18() != null){
					weeklyResourceCount = weeklyResourceCount + jsonWorkPackageWeeklyDemandProjection.getWeek18();
				}
				weeklyReservedResourceCount = weeklyReservedResourceCount + jsonWorkPackageWeeklyDemandProjection.getReservedResourceCountWk18();
				jsonWorkPackageWeeklyDemandProjection.setReservedResourceCountWk18(weeklyReservedResourceCount);
				
				jsonWorkPackageWeeklyDemandProjection.setWeek18(weeklyResourceCount);
				
				break;
			case 19:
				if(jsonWorkPackageWeeklyDemandProjection.getWeek19() != null){
					weeklyResourceCount = weeklyResourceCount + jsonWorkPackageWeeklyDemandProjection.getWeek19();
				}
				weeklyReservedResourceCount = weeklyReservedResourceCount + jsonWorkPackageWeeklyDemandProjection.getReservedResourceCountWk19();
				jsonWorkPackageWeeklyDemandProjection.setReservedResourceCountWk19(weeklyReservedResourceCount);
				
				jsonWorkPackageWeeklyDemandProjection.setWeek19(weeklyResourceCount);
				break;
			case 20:
				if(jsonWorkPackageWeeklyDemandProjection.getWeek20() != null){
					weeklyResourceCount = weeklyResourceCount + jsonWorkPackageWeeklyDemandProjection.getWeek20();
				}
				weeklyReservedResourceCount = weeklyReservedResourceCount + jsonWorkPackageWeeklyDemandProjection.getReservedResourceCountWk20();
				jsonWorkPackageWeeklyDemandProjection.setReservedResourceCountWk20(weeklyReservedResourceCount);
				
				jsonWorkPackageWeeklyDemandProjection.setWeek20(weeklyResourceCount);
				break;
			case 21:
				if(jsonWorkPackageWeeklyDemandProjection.getWeek21() != null){
					weeklyResourceCount = weeklyResourceCount + jsonWorkPackageWeeklyDemandProjection.getWeek21();
				}
				weeklyReservedResourceCount = weeklyReservedResourceCount + jsonWorkPackageWeeklyDemandProjection.getReservedResourceCountWk21();
				jsonWorkPackageWeeklyDemandProjection.setReservedResourceCountWk21(weeklyReservedResourceCount);
				
				jsonWorkPackageWeeklyDemandProjection.setWeek21(weeklyResourceCount);	
				break;
			case 22:
				if(jsonWorkPackageWeeklyDemandProjection.getWeek22() != null){
					weeklyResourceCount = weeklyResourceCount + jsonWorkPackageWeeklyDemandProjection.getWeek22();
				}
				weeklyReservedResourceCount = weeklyReservedResourceCount + jsonWorkPackageWeeklyDemandProjection.getReservedResourceCountWk22();
				jsonWorkPackageWeeklyDemandProjection.setReservedResourceCountWk22(weeklyReservedResourceCount);
				
				jsonWorkPackageWeeklyDemandProjection.setWeek22(weeklyResourceCount);	
				break;
			case 23:
				if(jsonWorkPackageWeeklyDemandProjection.getWeek23() != null){
					weeklyResourceCount = weeklyResourceCount + jsonWorkPackageWeeklyDemandProjection.getWeek23();
				}
				weeklyReservedResourceCount = weeklyReservedResourceCount + jsonWorkPackageWeeklyDemandProjection.getReservedResourceCountWk23();
				jsonWorkPackageWeeklyDemandProjection.setReservedResourceCountWk23(weeklyReservedResourceCount);
				
				jsonWorkPackageWeeklyDemandProjection.setWeek23(weeklyResourceCount);
				break;
			case 24:
				if(jsonWorkPackageWeeklyDemandProjection.getWeek24() != null){
					weeklyResourceCount = weeklyResourceCount + jsonWorkPackageWeeklyDemandProjection.getWeek24();
				}
				weeklyReservedResourceCount = weeklyReservedResourceCount + jsonWorkPackageWeeklyDemandProjection.getReservedResourceCountWk24();
				jsonWorkPackageWeeklyDemandProjection.setReservedResourceCountWk24(weeklyReservedResourceCount);
				
				jsonWorkPackageWeeklyDemandProjection.setWeek24(weeklyResourceCount);
				break;
			case 25:
				if(jsonWorkPackageWeeklyDemandProjection.getWeek25() != null){
					weeklyResourceCount = weeklyResourceCount + jsonWorkPackageWeeklyDemandProjection.getWeek25();
				}
				weeklyReservedResourceCount = weeklyReservedResourceCount + jsonWorkPackageWeeklyDemandProjection.getReservedResourceCountWk25();
				jsonWorkPackageWeeklyDemandProjection.setReservedResourceCountWk25(weeklyReservedResourceCount);
				
				jsonWorkPackageWeeklyDemandProjection.setWeek25(weeklyResourceCount);	
				break;
			case 26:
				if(jsonWorkPackageWeeklyDemandProjection.getWeek26() != null){
					weeklyResourceCount = weeklyResourceCount + jsonWorkPackageWeeklyDemandProjection.getWeek26();
				}
				weeklyReservedResourceCount = weeklyReservedResourceCount + jsonWorkPackageWeeklyDemandProjection.getReservedResourceCountWk26();
				jsonWorkPackageWeeklyDemandProjection.setReservedResourceCountWk26(weeklyReservedResourceCount);
				
				jsonWorkPackageWeeklyDemandProjection.setWeek26(weeklyResourceCount);
				break;
			case 27:
				if(jsonWorkPackageWeeklyDemandProjection.getWeek27() != null){
					weeklyResourceCount = weeklyResourceCount + jsonWorkPackageWeeklyDemandProjection.getWeek27();
				}
				weeklyReservedResourceCount = weeklyReservedResourceCount + jsonWorkPackageWeeklyDemandProjection.getReservedResourceCountWk27();
				jsonWorkPackageWeeklyDemandProjection.setReservedResourceCountWk27(weeklyReservedResourceCount);
				
				jsonWorkPackageWeeklyDemandProjection.setWeek27(weeklyResourceCount);
				break;
			case 28:
				if(jsonWorkPackageWeeklyDemandProjection.getWeek28() != null){
					weeklyResourceCount = weeklyResourceCount + jsonWorkPackageWeeklyDemandProjection.getWeek28();
				}
				weeklyReservedResourceCount = weeklyReservedResourceCount + jsonWorkPackageWeeklyDemandProjection.getReservedResourceCountWk28();
				jsonWorkPackageWeeklyDemandProjection.setReservedResourceCountWk28(weeklyReservedResourceCount);
				
				jsonWorkPackageWeeklyDemandProjection.setWeek28(weeklyResourceCount);
				break;
			case 29:
				if(jsonWorkPackageWeeklyDemandProjection.getWeek29() != null){
					weeklyResourceCount = weeklyResourceCount + jsonWorkPackageWeeklyDemandProjection.getWeek29();
				}
				weeklyReservedResourceCount = weeklyReservedResourceCount + jsonWorkPackageWeeklyDemandProjection.getReservedResourceCountWk29();
				jsonWorkPackageWeeklyDemandProjection.setReservedResourceCountWk29(weeklyReservedResourceCount);
				
				jsonWorkPackageWeeklyDemandProjection.setWeek29(weeklyResourceCount);
				break;
			case 30:
				if(jsonWorkPackageWeeklyDemandProjection.getWeek30() != null){
					weeklyResourceCount = weeklyResourceCount + jsonWorkPackageWeeklyDemandProjection.getWeek30();
				}
				weeklyReservedResourceCount = weeklyReservedResourceCount + jsonWorkPackageWeeklyDemandProjection.getReservedResourceCountWk30();
				jsonWorkPackageWeeklyDemandProjection.setReservedResourceCountWk30(weeklyReservedResourceCount);
				
				jsonWorkPackageWeeklyDemandProjection.setWeek30(weeklyResourceCount);
				break;
			case 31:
				if(jsonWorkPackageWeeklyDemandProjection.getWeek31() != null){
					weeklyResourceCount = weeklyResourceCount + jsonWorkPackageWeeklyDemandProjection.getWeek31();
				}
				weeklyReservedResourceCount = weeklyReservedResourceCount + jsonWorkPackageWeeklyDemandProjection.getReservedResourceCountWk31();
				jsonWorkPackageWeeklyDemandProjection.setReservedResourceCountWk31(weeklyReservedResourceCount);
				
				jsonWorkPackageWeeklyDemandProjection.setWeek31(weeklyResourceCount);	
				break;
			case 32:
				if(jsonWorkPackageWeeklyDemandProjection.getWeek32() != null){
					weeklyResourceCount = weeklyResourceCount + jsonWorkPackageWeeklyDemandProjection.getWeek32();
				}
				weeklyReservedResourceCount = weeklyReservedResourceCount + jsonWorkPackageWeeklyDemandProjection.getReservedResourceCountWk32();
				jsonWorkPackageWeeklyDemandProjection.setReservedResourceCountWk32(weeklyReservedResourceCount);
				
				jsonWorkPackageWeeklyDemandProjection.setWeek32(weeklyResourceCount);
				break;
			case 33:
				if(jsonWorkPackageWeeklyDemandProjection.getWeek33() != null){
					weeklyResourceCount = weeklyResourceCount + jsonWorkPackageWeeklyDemandProjection.getWeek33();
				}
				weeklyReservedResourceCount = weeklyReservedResourceCount + jsonWorkPackageWeeklyDemandProjection.getReservedResourceCountWk33();
				jsonWorkPackageWeeklyDemandProjection.setReservedResourceCountWk33(weeklyReservedResourceCount);
				
				jsonWorkPackageWeeklyDemandProjection.setWeek33(weeklyResourceCount);
				break;
			case 34:
				if(jsonWorkPackageWeeklyDemandProjection.getWeek34() != null){
					weeklyResourceCount = weeklyResourceCount + jsonWorkPackageWeeklyDemandProjection.getWeek34();
				}
				weeklyReservedResourceCount = weeklyReservedResourceCount + jsonWorkPackageWeeklyDemandProjection.getReservedResourceCountWk34();
				jsonWorkPackageWeeklyDemandProjection.setReservedResourceCountWk34(weeklyReservedResourceCount);
				
				jsonWorkPackageWeeklyDemandProjection.setWeek34(weeklyResourceCount);	
				break;
			case 35:
				if(jsonWorkPackageWeeklyDemandProjection.getWeek35() != null){
					weeklyResourceCount = weeklyResourceCount + jsonWorkPackageWeeklyDemandProjection.getWeek35();
				}
				weeklyReservedResourceCount = weeklyReservedResourceCount + jsonWorkPackageWeeklyDemandProjection.getReservedResourceCountWk35();
				jsonWorkPackageWeeklyDemandProjection.setReservedResourceCountWk35(weeklyReservedResourceCount);
				
				jsonWorkPackageWeeklyDemandProjection.setWeek35(weeklyResourceCount);	
				break;
			case 36:
				if(jsonWorkPackageWeeklyDemandProjection.getWeek36() != null){
					weeklyResourceCount = weeklyResourceCount + jsonWorkPackageWeeklyDemandProjection.getWeek36();
				}
				weeklyReservedResourceCount = weeklyReservedResourceCount + jsonWorkPackageWeeklyDemandProjection.getReservedResourceCountWk36();
				jsonWorkPackageWeeklyDemandProjection.setReservedResourceCountWk36(weeklyReservedResourceCount);
				
				jsonWorkPackageWeeklyDemandProjection.setWeek36(weeklyResourceCount);	
				break;
			case 37:
				if(jsonWorkPackageWeeklyDemandProjection.getWeek37() != null){
					weeklyResourceCount = weeklyResourceCount + jsonWorkPackageWeeklyDemandProjection.getWeek37();
				}
				weeklyReservedResourceCount = weeklyReservedResourceCount + jsonWorkPackageWeeklyDemandProjection.getReservedResourceCountWk37();
				jsonWorkPackageWeeklyDemandProjection.setReservedResourceCountWk37(weeklyReservedResourceCount);
				
				jsonWorkPackageWeeklyDemandProjection.setWeek37(weeklyResourceCount);	
				break;
			case 38:
				if(jsonWorkPackageWeeklyDemandProjection.getWeek38() != null){
					weeklyResourceCount = weeklyResourceCount + jsonWorkPackageWeeklyDemandProjection.getWeek38();
				}
				weeklyReservedResourceCount = weeklyReservedResourceCount + jsonWorkPackageWeeklyDemandProjection.getReservedResourceCountWk38();
				jsonWorkPackageWeeklyDemandProjection.setReservedResourceCountWk38(weeklyReservedResourceCount);
				
				jsonWorkPackageWeeklyDemandProjection.setWeek38(weeklyResourceCount);	
				break;
			case 39:
				if(jsonWorkPackageWeeklyDemandProjection.getWeek39() != null){
					weeklyResourceCount = weeklyResourceCount + jsonWorkPackageWeeklyDemandProjection.getWeek39();
				}
				weeklyReservedResourceCount = weeklyReservedResourceCount + jsonWorkPackageWeeklyDemandProjection.getReservedResourceCountWk39();
				jsonWorkPackageWeeklyDemandProjection.setReservedResourceCountWk39(weeklyReservedResourceCount);
				
				jsonWorkPackageWeeklyDemandProjection.setWeek39(weeklyResourceCount);
				break;
			case 40:
				if(jsonWorkPackageWeeklyDemandProjection.getWeek40() != null){
					weeklyResourceCount = weeklyResourceCount + jsonWorkPackageWeeklyDemandProjection.getWeek40();
				}
				weeklyReservedResourceCount = weeklyReservedResourceCount + jsonWorkPackageWeeklyDemandProjection.getReservedResourceCountWk40();
				jsonWorkPackageWeeklyDemandProjection.setReservedResourceCountWk40(weeklyReservedResourceCount);
				
				jsonWorkPackageWeeklyDemandProjection.setWeek40(weeklyResourceCount);
				break;
			case 41:
				if(jsonWorkPackageWeeklyDemandProjection.getWeek41() != null){
					weeklyResourceCount = weeklyResourceCount + jsonWorkPackageWeeklyDemandProjection.getWeek41();
				}
				weeklyReservedResourceCount = weeklyReservedResourceCount + jsonWorkPackageWeeklyDemandProjection.getReservedResourceCountWk41();
				jsonWorkPackageWeeklyDemandProjection.setReservedResourceCountWk41(weeklyReservedResourceCount);
				
				jsonWorkPackageWeeklyDemandProjection.setWeek41(weeklyResourceCount);
				break;
			case 42:
				if(jsonWorkPackageWeeklyDemandProjection.getWeek42() != null){
					weeklyResourceCount = weeklyResourceCount + jsonWorkPackageWeeklyDemandProjection.getWeek42();
				}
				weeklyReservedResourceCount = weeklyReservedResourceCount + jsonWorkPackageWeeklyDemandProjection.getReservedResourceCountWk42();
				jsonWorkPackageWeeklyDemandProjection.setReservedResourceCountWk42(weeklyReservedResourceCount);
				
				jsonWorkPackageWeeklyDemandProjection.setWeek42(weeklyResourceCount);
				break;
			case 43:
				if(jsonWorkPackageWeeklyDemandProjection.getWeek43() != null){
					weeklyResourceCount = weeklyResourceCount + jsonWorkPackageWeeklyDemandProjection.getWeek43();
				}
				weeklyReservedResourceCount = weeklyReservedResourceCount + jsonWorkPackageWeeklyDemandProjection.getReservedResourceCountWk43();
				jsonWorkPackageWeeklyDemandProjection.setReservedResourceCountWk43(weeklyReservedResourceCount);
				
				jsonWorkPackageWeeklyDemandProjection.setWeek43(weeklyResourceCount);	
				break;
			case 44:
				if(jsonWorkPackageWeeklyDemandProjection.getWeek44() != null){
					weeklyResourceCount = weeklyResourceCount + jsonWorkPackageWeeklyDemandProjection.getWeek44();
				}
				weeklyReservedResourceCount = weeklyReservedResourceCount + jsonWorkPackageWeeklyDemandProjection.getReservedResourceCountWk44();
				jsonWorkPackageWeeklyDemandProjection.setReservedResourceCountWk44(weeklyReservedResourceCount);
				
				jsonWorkPackageWeeklyDemandProjection.setWeek44(weeklyResourceCount);	
				break;
			case 45:
				if(jsonWorkPackageWeeklyDemandProjection.getWeek45() != null){
					weeklyResourceCount = weeklyResourceCount + jsonWorkPackageWeeklyDemandProjection.getWeek45();
				}
				weeklyReservedResourceCount = weeklyReservedResourceCount + jsonWorkPackageWeeklyDemandProjection.getReservedResourceCountWk45();
				jsonWorkPackageWeeklyDemandProjection.setReservedResourceCountWk45(weeklyReservedResourceCount);
				
				jsonWorkPackageWeeklyDemandProjection.setWeek45(weeklyResourceCount);	
				break;
			case 46:
				if(jsonWorkPackageWeeklyDemandProjection.getWeek46() != null){
					weeklyResourceCount = weeklyResourceCount + jsonWorkPackageWeeklyDemandProjection.getWeek46();
				}
				weeklyReservedResourceCount = weeklyReservedResourceCount + jsonWorkPackageWeeklyDemandProjection.getReservedResourceCountWk46();
				jsonWorkPackageWeeklyDemandProjection.setReservedResourceCountWk46(weeklyReservedResourceCount);
				
				jsonWorkPackageWeeklyDemandProjection.setWeek46(weeklyResourceCount);
				break;
			case 47:
				if(jsonWorkPackageWeeklyDemandProjection.getWeek47() != null){
					weeklyResourceCount = weeklyResourceCount + jsonWorkPackageWeeklyDemandProjection.getWeek47();
				}
				weeklyReservedResourceCount = weeklyReservedResourceCount + jsonWorkPackageWeeklyDemandProjection.getReservedResourceCountWk47();
				jsonWorkPackageWeeklyDemandProjection.setReservedResourceCountWk47(weeklyReservedResourceCount);
				
				jsonWorkPackageWeeklyDemandProjection.setWeek47(weeklyResourceCount);
				break;
			case 48:
				if(jsonWorkPackageWeeklyDemandProjection.getWeek48() != null){
					weeklyResourceCount = weeklyResourceCount + jsonWorkPackageWeeklyDemandProjection.getWeek48();
				}
				weeklyReservedResourceCount = weeklyReservedResourceCount + jsonWorkPackageWeeklyDemandProjection.getReservedResourceCountWk48();
				jsonWorkPackageWeeklyDemandProjection.setReservedResourceCountWk48(weeklyReservedResourceCount);
				
				jsonWorkPackageWeeklyDemandProjection.setWeek48(weeklyResourceCount);	
				break;
			case 49:
				if(jsonWorkPackageWeeklyDemandProjection.getWeek49() != null){
					weeklyResourceCount = weeklyResourceCount + jsonWorkPackageWeeklyDemandProjection.getWeek49();
				}
				weeklyReservedResourceCount = weeklyReservedResourceCount + jsonWorkPackageWeeklyDemandProjection.getReservedResourceCountWk49();
				jsonWorkPackageWeeklyDemandProjection.setReservedResourceCountWk49(weeklyReservedResourceCount);
				
				jsonWorkPackageWeeklyDemandProjection.setWeek49(weeklyResourceCount);	
				break;
			case 50:
				if(jsonWorkPackageWeeklyDemandProjection.getWeek50() != null){
					weeklyResourceCount = weeklyResourceCount + jsonWorkPackageWeeklyDemandProjection.getWeek50();
				}
				weeklyReservedResourceCount = weeklyReservedResourceCount + jsonWorkPackageWeeklyDemandProjection.getReservedResourceCountWk50();
				jsonWorkPackageWeeklyDemandProjection.setReservedResourceCountWk50(weeklyReservedResourceCount);
				
				jsonWorkPackageWeeklyDemandProjection.setWeek50(weeklyResourceCount);
				break;
			case 51:
				if(jsonWorkPackageWeeklyDemandProjection.getWeek51() != null){
					weeklyResourceCount = weeklyResourceCount + jsonWorkPackageWeeklyDemandProjection.getWeek51();
				}
				weeklyReservedResourceCount = weeklyReservedResourceCount + jsonWorkPackageWeeklyDemandProjection.getReservedResourceCountWk51();
				jsonWorkPackageWeeklyDemandProjection.setReservedResourceCountWk51(weeklyReservedResourceCount);
				
				jsonWorkPackageWeeklyDemandProjection.setWeek51(weeklyResourceCount);
				break;
								
			case 52:
				if(jsonWorkPackageWeeklyDemandProjection.getWeek52() != null){
					weeklyResourceCount = weeklyResourceCount + jsonWorkPackageWeeklyDemandProjection.getWeek52();
				}
				weeklyReservedResourceCount = weeklyReservedResourceCount + jsonWorkPackageWeeklyDemandProjection.getReservedResourceCountWk52();
				jsonWorkPackageWeeklyDemandProjection.setReservedResourceCountWk52(weeklyReservedResourceCount);
				
				jsonWorkPackageWeeklyDemandProjection.setWeek52(weeklyResourceCount);	
				break;
		}
	}
	
	@Override
	@Transactional
	public List<Integer> getTestfactoryResourcePoolListbyTestFactoryId(
			Integer testFactoryId) {
		return testfactoryResourcePoolDAO.getTestfactoryResourcePoolListbyTestFactoryId(testFactoryId);				
		
	}
	
	@Override
	public Set<Integer> getRecursiveWeeks(String recursiveWeeksPattern, Set<Integer> recursiveWeeks){
		try{
			if(recursiveWeeksPattern != null && !recursiveWeeksPattern.trim().isEmpty()){
				String[] recursiveWeeksPatternSplit = recursiveWeeksPattern.split(",");
				for(String recursiveWeek : recursiveWeeksPatternSplit){
					if(recursiveWeek.contains("-")){
						String[] seriesWeeks = recursiveWeek.split("-");
						int[] weeks = new int[seriesWeeks.length];
						for (int i = 0; i < seriesWeeks.length; i++) {
							String numberAsString = seriesWeeks[i];
							try{
								weeks[i] = Integer.parseInt(numberAsString);
							}catch(Exception ex){
								log.error("Error occured in getRecursiveWeeks while convering to Integer - ", ex);
							}
						}

						Arrays.sort(weeks);
						for(int i = (weeks[0]); i <= (weeks[weeks.length - 1]); i++){
							recursiveWeeks.add(i);
						}
					}else if(NumberUtils.isNumber(recursiveWeek)){
						recursiveWeeks.add(Integer.parseInt(recursiveWeek));
					}
				}
			}
		}catch(Exception ex){
			log.error("Error in getRecursiveWeeks - ", ex);
		}
		return recursiveWeeks;
	}
	

	private boolean checkResourceAssignedToRPforWeek(Integer userId, Integer workWeek,Integer workYear, Integer resourcePoolId) {
		
		try{
			
			Date startDate = DateUtility.getWeekStartDate(workWeek,workYear);
			return testfactoryResourcePoolDAO.checkResourceAssignedToRPforWeek(userId,resourcePoolId,startDate);		
			
		}catch(Exception ex){
			log.error("error ", ex);
		}
		return false;
	}
	
	@Override
	@Transactional
	public Integer getTotalReservationPercentageByRoleAndSkill(Integer userId, Integer workPackageId, Integer workWeek, Integer workYear, Integer skillId, Integer userRoleId, Integer userTypeId){
		Integer totalPercentage = 0;
		try{
			totalPercentage = resourceAvailabilityDAO.getTotalReservationPercentageByRoleAndSkill(userId, workPackageId, workWeek, workYear, skillId, userRoleId, userTypeId);
			totalPercentage = totalPercentage/5;
			
		}catch(Exception ex){
			log.error("Unable to fetch the data ", ex);
		}
		
		return totalPercentage;
	}
	
	@Override
	@Transactional
	public List<Object[]> getWorkpackageAndProductAndTestFactoryCombinations(){
		List<Object[]> listCombns = null;
	try {
		listCombns = resourceAvailabilityDAO.getWorkpackageAndProductAndTestFactoryCombinations();	
	} catch (Exception e) {
		log.error("Unable to fetch Workpackage,Product&TestFactory combinations",e);
	}
	return listCombns;
	}
	
	@Override
	@Transactional
	public Integer updateDemandProjectionByNonMatchedCombList(Integer workWeek,String workpackageIds) {
		try {
			return resourceAvailabilityDAO.updateDemandProjectionByNonMatchedCombList(workWeek,workpackageIds);
		} catch (Exception e) {
			log.error("Unable to update DemandProjection",e);
		}
		return 0;
	}
	
}
