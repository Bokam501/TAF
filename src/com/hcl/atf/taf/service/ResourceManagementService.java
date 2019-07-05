package com.hcl.atf.taf.service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import com.hcl.atf.taf.model.ResourceAvailability;
import com.hcl.atf.taf.model.ShiftTypeMaster;
import com.hcl.atf.taf.model.TestFactoryResourceReservation;
import com.hcl.atf.taf.model.TestfactoryResourcePool;
import com.hcl.atf.taf.model.UserList;
import com.hcl.atf.taf.model.WorkPackage;
import com.hcl.atf.taf.model.WorkPackageDemandProjection;
import com.hcl.atf.taf.model.WorkShiftMaster;
import com.hcl.atf.taf.model.dto.ResourceAttendanceSummaryDTO;
import com.hcl.atf.taf.model.dto.ResourceAvailabilityDTO;
import com.hcl.atf.taf.model.dto.TestFactoryResourceReservationDTO;
import com.hcl.atf.taf.model.dto.UserWeekUtilisedTimeDTO;
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
import com.hcl.atf.taf.model.json.JsonWorkPackageResourceReservation;
import com.hcl.atf.taf.model.json.JsonWorkPackageResultsStatistics;
import com.hcl.atf.taf.model.json.JsonWorkPackageWeeklyDemandProjection;
import com.hcl.atf.taf.model.json.JsonWorkPackageWeeklyResourceReservation;

public interface ResourceManagementService {

	List<JsonResourceAvailability> listWorkPackageDemandProjection(int resourceId, int weekNo);
	JsonResourceAvailability updateWorkPackageDemandProjection(JsonResourceAvailability jsonResourceAvailability);
	public List<TestfactoryResourcePool> getTestfactoryResourcePoolList();
	public List<TestfactoryResourcePool> getTestfactoryResourcePoolListbyLabId(int testFactoryLabId);
	TestfactoryResourcePool getResourcePoolListbyLabId(int testFactoryLabId);
	TestfactoryResourcePool getTestfactoryResourcePoolListbyId(int resourcePoolId);
	//
	List<UserList> getResourcePoolwithRole(int resourcePoolId);
	List<JsonResourcePoolSummary> getResourcePoolwithRoleJson(int resourcePoolId);
	List<JsonResourcePoolSummary> getResourcePoolwithRoleJson(List<Integer> resourcePoolId);
	void addResourcePool(TestfactoryResourcePool resourcePool);
	void updateResourcePool(TestfactoryResourcePool resourcePool);
	List<JsonWorkPackageResourceReservation> listWorkPackageDemandProjectionForResourcePlanning(int productId, Date resourceDemandForDate);
	List<JsonUserList> getBlockedResourcesOfWorkPackage(int workPackageId, int shiftId, Date resourceDemandForDate);
	List<UserList> getExistingBlockedResourcesOfWorkPackage(int workPackageId, int shiftId, Date resourceDemandForDate);
	public List<JsonResourceAvailabilityPlan> listResourcesForAvailabilityPlan(Integer resourcePoolId, Date workDate);
	List<JsonUserList> getResourcesForReservation(int workPackgeId, int shiftId, Date blockResourceForDate, String availabilityTypeFilter);
	List<JsonUserList> getAllUnReservedResourcesForReservation(int workPackageId, int shiftId, Date blockResourceForDate, String availabilityTypeFilter);
	List<ResourceAvailability>  listResourceAvailablityByDate(Date workDate);
	TestFactoryResourceReservationDTO listTestFactoryResourceReservationByWorkpackageIdDate(Date startDate, Date endDate, Integer testFactoryLabId, Integer testFactoryId, Integer productId, Integer workPackageId, Integer shiftId,Date workDate);
	ResourceAvailabilityDTO  listResourceAvailablityByDate(Date startDate, Date endDate, Integer testFactoryLabId, Integer testFactoryId, Integer productId, Integer workPackageId, Integer shiftId,Date workDate);
	void saveBlockedResource(List<UserList> listOfResourcesToAdd, Date dtResourceBlockedForDate, String workPackageId, String shiftId, String loggedInUserId);

	public 	List<TestFactoryResourceReservation> getTestFactoryResourceReservation(int workPackageId, int shiftId, Date tfreservationDate);

	void removeUnblockedResources(List<UserList> listOfResourcesToDelete,Date dtResourceBlockedForDate, String workPackageId, String shiftId, String loggedInUserId);
	List<JsonWorkPackageResourceReservation> listWorkPackageDemandProjectionForResourcePlanningByWorkpackage(int workpackageId, Date resourceDemandForDate);
	List<JsonResourceAttendance> listResourcesAttendance(int resourcePoolId, Date workDate, Integer shiftId);
	List<JsonResourceAttendance> listResourcesAttendance(int resourcePoolId, Date workDateFrom,Date workDateTo, Integer shiftId, Integer userId);
	ResourceAvailability updateResourceAttendanceInline(Integer resourceAvailabilityId, String modifiedField, String modifiedFieldValue,HttpServletRequest req);
	List<TestfactoryResourcePool> getResourcePoolListById(int testFactoryLabId, int resourcePoolId);
	
	TestfactoryResourcePool mapRespoolTestfactory(Integer testFactoryId,
			Integer resourcePoolId, String action);
	List<JsonResourceAvailabilityPlan> listResourcesForAvailabilityPlanByShifId(Map<String, String> searchStrings, String searchResourcePoolName,String searchUserName,
			int resourcePoolId, Date workDate, int shiftTypeId, Integer jtStartIndex, Integer jtPageSize);

	List<WorkPackageDemandProjection>  listWorkpackageDemandProjection(Integer testFactoryLabId,Integer  testFactoryId, Integer productId,Integer  workPackageId, Integer shiftId,Date workDate, Integer jtStartIndex, Integer jtPageSize);
	List<WorkPackageDemandProjectionStatisticsDTO>  listWorkpackageDemandProjectionByRole(Integer workPackageId, Integer shiftId, Date reservationDate);

	List<ResourceAvailability>  listAvaiablitybyDate(Integer testFactoryLabId,Integer  testFactoryId, Integer productId,Integer  workPackageId, Integer shiftId,Date workDate, Integer jtStartIndex, Integer jtPageSize);
	List<TestFactoryResourceReservation> listTestFactoryResourceReservation(Integer testFactoryLabId,Integer  testFactoryId, Integer productId,Integer  workPackageId, Integer shiftId,Date workDate, Integer jtStartIndex, Integer jtPageSize);
	List<TestFactoryResourceReservation> getTestFactoryResourceReservation(int workPackageId, int weekNo);
	List<ShiftTypeMaster>  listShiftTypeMaster();

	ShiftTypeMaster getShiftTypeMasterById(Integer shiftTypeId);
	ResourceAvailability  updateResourceAvailabilityInline(UserList userList,ShiftTypeMaster shiftTypeMaster, Integer shiftTypeAvailability,String workDate);

	ResourceAvailability  updateResourceBookingInline(UserList userList,ShiftTypeMaster shiftTypeMaster,Integer shiftTypeBooking,String workDate);
	
	List<TestfactoryResourcePool> testFactoryResourcePoolListbyTFactoryId(Integer testFactoryId);
	List<ResourceAttendanceSummaryDTO> listResourceAttendanceSummary(Integer resourcePoolId, Date workDate, Integer shiftId);
	TestFactoryResourceReservation saveReservedResource(Integer reservedUserId, Date dtResourceBlockedForDate, String workPackageId, String shiftId, String loggedInUserId);
	TestFactoryResourceReservation removeReservationForResource(Integer unBlockedUserId, Date dtResourceBlockedForDate, String workPackageId, String shiftId, String loggedInUserId);
	
	List<JsonResourceAvailability> listForBookingAndReservedStatus(int resourceId, int weekNo);
	List<ResourceAttendanceSummaryDTO> listResourceAvailabiltySummary(Integer resourcePoolId, Date workDate, Integer shiftId);
	List<JsonResourceAttendance> listResourcesReliable(Integer resourcePoolId, Date startDate, Date endDate, Integer userId);
	List<JsonResourceAvailabilityDetails> getResourceAvailability(int workPackageId, int shiftId, int shiftTypeId, Date getAvailabilityForDate);
	List<JsonTestFactoryResourceReservation> listResourcesReliableTotalBookingSummary(Integer userId, Date startDate, Date endDate);
	List<JsonTestFactoryResourceReservation> listResourcesReliableShowUpSummary(Integer userId, Date startDate, Date endDate);
	List<JsonTestFactoryResourceReservation> listResourcesReliableOnTimeSummary(Integer userId, Date startDate, Date endDate);
	
	JsonWorkPackageResultsStatistics updateResourceDailyPerformance(JsonWorkPackageResultsStatistics jsonWorkPackageResultsStatistics, UserList rater);
	JsonWorkPackageResultsStatistics resourceDailyPerformanceApproveUpdate(JsonWorkPackageResultsStatistics jsonWorkPackageResultsStatistics, UserList approver);
	List<JsonReservedResourcesForBooking> getNotAvailAndOrNotBookedResourceCount(Integer resourcePoolId, Date startDate, Date endDate);
	List<JsonResourceAvailabilityPlan> listReservedResourcesForBooking(int resourcePoolId, Date workDate, int shiftTypeId, int availabilityStatus,Integer jtStartIndex, Integer jtPageSize);
	void updateAvailabilityForReservedResources(String[] resourceLists,	String workDate, Integer shiftTypeId, Integer availabilityStatus,int filterType);
	boolean isAvailabilityExistForUser(Integer userId,	Date workDate, Integer shiftTypeId);
	void addEntryForUserInResourceAvailability(Integer userId,	Date workDate, ShiftTypeMaster shiftType);
	WorkShiftMaster  listWorkShiftsByshiftId(int shiftId);
	List<UserWeekUtilisedTimeDTO>  getTimesheetEntriesForWorkPackage(int workPackgeId, WorkShiftMaster shift, Date blockResourceForDate);
	List<UserWeekUtilisedTimeDTO>  getReservedShiftsOfUser(int workPackageId, int shiftId,Date blockResourceForDate);
	Long listResourcesForAVailabilityCount(Integer resourcePoolId);
	void confirmBookingForAvailableResource(Integer userId, Date reservationDate,Integer shiftTypeId);
	
	Float getBlockedResourcesCount(Date reservationDate, Integer testFactoryLabId, Integer resourcePoolId,Integer shiftTypeId);
	Float getBlockedResourcesAttendanceCount(Date reservationDate, Integer testFactoryLabId, Integer resourcePoolId,Integer shiftTypeId);
	List<JsonResourceAvailability> listResourceWorkTime(int resourceId,	int weekNo);
	HashMap<Integer,Integer> getUserAttendanceForMonth(int userId, String workDate);
	List<JsonResourceShiftCheckinDetailsForWeek> listUserTimeManagementSummary(int resourceId, int weekNo);
	List<JsonResourceShiftCheckin> getResourceShiftCheckInForWeek(int weekNo, int resourceId, List<ShiftTypeMaster> listOfShiftTypes);
	List<TestFactoryResourceReservation> listTestFactoryResourceReservationByDate(Integer testFactoryLabId, Integer resourcePoolId,Integer shiftTypeId, Date workDate);
	List<ResourceAvailability> listAttendanceByDate(Integer testFactoryLabId,Integer resourcePoolId, Integer shiftTypeId, Date selectedDate);
	
	List<JsonUserList> listUserbySpecificRoleByTestFactoryId(int testFactoryId, int productId);
	List<TestFactoryResourceReservation> getTestFactoryResourceReservationByDateShiftAndUser(int userId, int shiftId,Date tfreservationDate);
	void updateAvailabilityForResourcesBulk(String[] resourceLists,	String workDate, Integer shiftTypeId, Integer availabilityStatus);
	String updateBookingForResourcesBulk(String[] resourceLists,	String workDate, Integer shiftTypeId, Integer availabilityStatus);	
	List<WorkPackage> getWorkPackageFromTestFactoryResourceReservationByserId(int userId, int shiftId, Date createdDate);
	List<JsonResourceAvailabilityPlan> listResourcesForBookingPlanByShifId(Map<String, String> searchStrings, String searchResourcePoolName, String searchUserName, Integer resourcePoolId, Date workDate, Integer shiftTypeId);
	List<ResourceAvailabilityDTO> getResourceAvailablityList(Date startDate,Date endDate, Integer testFactoryLabId, Integer testFactoryId,Integer productId, Integer workPackageId, Integer shiftId,String viewType);
	List<TestFactoryResourceReservationDTO> getTestFactoryResourceReservationList(Date startDate, Date endDate, Integer testFactoryLabId, Integer testFactoryId, Integer productId, Integer workPackageId, Integer shiftId,String viewType);
	
	TestfactoryResourcePool getTestFactoryResourcePoolByName(String testFactoryResourcePoolName);
	List<JsonWorkPackageWeeklyDemandProjection> listWorkpackageWeeklyDemandProjection(Integer testFactoryLabId, Integer testFactoryId, Integer productId,Integer workPackageId, Integer shiftId, Integer workWeek,Integer workYear);
	List<JsonWorkPackageWeeklyResourceReservation> listWorkpackageWeeklyResourceReservationProjection(Integer workPackageId, Integer reservationWeek,Integer reservationYear, Integer userId);
	List<JsonUserList> getAllUnReservedResourcesForReservationWeekly(Integer workPackageId,Integer shiftId, Integer workWeek,Integer workYear, Integer userRoleId, Integer skillId, String filter, Integer userTypeId);
	List<JsonUserList> getReservedResourcesWeeklyByRoleAndSkill(Integer workpackageId, Integer workWeek, Integer workYear, Integer shiftId, Integer userRoleId, Integer skillId,Integer userTypeId);
	List<UserList> getExistingBlockedResourcesOfWorkPackageForWeek(Integer workpackageId, Integer shiftId, Integer workWeek,Integer workYear);
	TestFactoryResourceReservation saveReservedResourceForWeekly(Integer reservedorUnreservedUserId, Integer workWeek,Integer workYear, Integer workpackageId, Integer shiftId,Integer skillId, Integer userRoleId, Integer loggedInUserId, Long groupDemandId,Integer reservationPercentage, Integer userTypeId);
	List<TestFactoryResourceReservation> removeReservationForResourceForWeekly(Integer reservedorUnreservedUserId, Integer workWeek,Integer workYear, Integer workpackageId, Integer shiftId,Integer loggedInUserId, Long groupDemandId,Integer userTypeId);
	List<JsonWorkPackageWeeklyResourceReservation> getResourceAllocationWeekly(Integer labId, Integer resourcePoolId, Integer testFactoryId, Integer productId, Integer workpackageId, Integer allocationYear, Integer userId,Integer utilizationRange,List<Integer> allocationWeekList);
	Integer getTotalReservationPercentageByUserId(Integer userId,Integer workWeek,Integer workYear);
	List<WorkPackageDemandProjection> getWorkpackageDemandProjection(Integer workPackageId, Integer shiftId, Integer skillId, Integer userRoleId, Integer recursiveWeek, Integer workYear,Integer userTypeId);
	void updateReservedResourcePercentage(Integer workpackageId,Integer recursiveWeek, Integer workYear, Integer reserveShiftId,Integer reserveSkillId, Integer reservationPercentage,Integer userId, Integer reserveUserRoleId,Integer userTypeId);
	List<TestFactoryResourceReservation> getReservedResourcesWeeklyByUserIdForUpdateOrAdd(Integer workpackageId, Integer recursiveWeek, Integer workYear,Integer shiftId, Integer skillId,Integer reservedorUnreservedUserId, Integer userRoleId, Integer userTypeId);
	List<JsonWorkPackageWeeklyDemandProjection> listWorkpackageWeeklyDemandProjectionEnagementLevel(int testFactoryLabId,int testFactoryId, int productId, int workPackageId, int weekYear,int selectedTab,List<Integer> recursiveWeekList);
	List<JsonWorkPackageWeeklyDemandProjection> getResourceAllocationDetailedWeekly(Integer labId, Integer resourcePoolId, Integer testFactoryId, Integer productId, Integer workpackageId, Integer userId, List<Integer> workWeek, Integer workYear);
	List<Integer> getTestfactoryResourcePoolListbyTestFactoryId(Integer testFactoryId);
	Set<Integer> getRecursiveWeeks(String recursiveWeeksPattern, Set<Integer> recursiveWeeks);
	Integer getTotalReservationPercentageByRoleAndSkill(Integer userId, Integer workPackageId, Integer workWeek,Integer workYear, Integer skillId, Integer userRoleId, Integer userTypeId);
	List<Object[]> getWorkpackageAndProductAndTestFactoryCombinations();
	Integer updateDemandProjectionByNonMatchedCombList(Integer workWeek, String WpIds);
}
