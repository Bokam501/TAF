package com.hcl.atf.taf.dao;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.hcl.atf.taf.model.ResourceAvailability;
import com.hcl.atf.taf.model.ShiftTypeMaster;
import com.hcl.atf.taf.model.TestFactoryResourceReservation;
import com.hcl.atf.taf.model.TestfactoryResourcePool;
import com.hcl.atf.taf.model.UserList;
import com.hcl.atf.taf.model.UserRoleMaster;
import com.hcl.atf.taf.model.WorkPackageDemandProjection;
import com.hcl.atf.taf.model.dto.ResourceAttendanceSummaryDTO;
import com.hcl.atf.taf.model.dto.ResourceAvailabilityDTO;
import com.hcl.atf.taf.model.dto.ResourceCountDTO;
import com.hcl.atf.taf.model.dto.ResourcePoolSummaryDTO;
import com.hcl.atf.taf.model.dto.TestFactoryResourceReservationDTO;
import com.hcl.atf.taf.model.dto.WeeklyResourceDemandDTO;
import com.hcl.atf.taf.model.dto.WeeklyResourceReservationDTO;
import com.hcl.atf.taf.model.dto.WorkPackageDemandProjectionStatisticsDTO;

public interface ResourceAvailabilityDAO {

	List<ResourceAvailability> listResourceAvailability(int resourceId,	Date startDate, Date endDate);
	List<ResourceAvailability> updateResourceAvailability(List<ResourceAvailability> resourceAvailabilityList);
	List<WorkPackageDemandProjection> listWorkPackageDemandProjectionForResourcePlanning(int workPackageId, Date resourceDemandForDate);
	List<ResourceCountDTO> getBlockedResourcesOfWorkPackage(int productId, Date resourceDemandForDate);
	List<UserList> listResourcesForAvailabilityPlan(Integer resourcePoolId);
	List<UserList> listResourcesForAvailabilityPlan(Map<String, String> searchStrings, String searchResourcePoolName,String searchUserName,Integer resourcePoolId, Integer jtStartIndex, Integer jtPageSize);
	List<UserList> getListOfResourceBlockedForWorkpackage(int workPackageId,int shiftId, Date resourceDemandForDate);
	//
	List<UserList> getResourcePoolwithRole(int resourcePoolId);
	List<ResourcePoolSummaryDTO> getResourcePoolwithRoleDTO(int resourcePoolId, List<UserRoleMaster> roleList);
	List<UserList> getAllUsersFromResourcePools(int workPackgeId, int shiftId, Date blockResourceForDate);
	
	List<UserList> getBlockedResources(Date blockResourceForDate, int shiftId);
	List<UserList> getOtherProductCoreResources(int workPackgeId);
	List<UserList> getOtherTestFactoryCoreResources(int workPackgeId);
	void add(ResourceAvailability resourceAvailablity);
	void delete(ResourceAvailability resourceAvailablity);
	void updateResourceAvailability(ResourceAvailability resourceAvailablity);
	ResourceAvailability getResourceAvailability(Integer userId, Date date, Integer shiftId);
	
	List<ResourceAvailability>  listResourceAvailablityByDate(Date workDate);
	TestFactoryResourceReservationDTO listTestFactoryResourceReservationByWorkpackageIdDate(Date startDate, Date endDate, Integer testFactoryLabId, Integer testFactoryId, Integer productId, Integer workPackageId, Integer shiftId,Date workDate);
	ResourceAvailabilityDTO  listResourceAvailablityByDate(Date startDate, Date endDate, Integer testFactoryLabId, Integer testFactoryId, Integer productId, Integer workPackageId, Integer shiftId,Date workDate);
	void saveBlockedResource(TestFactoryResourceReservation testFactoryResourceReservation);
	List<ResourceCountDTO> getProductCoreResourceOfWorkPackage(int productId);
	void removeUnblockedResources(TestFactoryResourceReservation testFactoryResourceReservation);
	List<ResourceAvailability> listResourceAttendance(int resourcePoolId, Date workDate, Integer shiftId);
	List<ResourceAvailability> listResourceAttendance(int resourcePoolId, Date workDateFrom,Date workDateTo, Integer shiftTypeId, Integer userId);
	ResourceAvailability getResourceAvailabilityById(Integer resourceAvailabilityId);

	List<ResourceAvailability> listResourceAttendanceByShiftId(Integer userId, Date startDate, Date endDate, int shiftTypeId, Integer typeFilter);

	List<WorkPackageDemandProjection>  listWorkpackageDemandProjection(Integer testFactoryLabId,Integer  testFactoryId, Integer productId,Integer  workPackageId, Integer shiftId,Date workDate, Integer jtStartIndex, Integer jtPageSize);
	List<WorkPackageDemandProjectionStatisticsDTO>  listWorkpackageDemandProjectionByRole(Integer  workPackageId, Integer shiftId, Date reservationDate);
	
	
	List<ResourceAvailability>  listAvaiablitybyDate(Integer testFactoryLabId,Integer  testFactoryId, Integer productId,Integer  workPackageId, Integer shiftId,Date workDate, Integer jtStartIndex, Integer jtPageSize);
	List<TestFactoryResourceReservation> listTestFactoryResourceReservation(Integer testFactoryLabId,Integer  testFactoryId, Integer productId,Integer  workPackageId, Integer shiftId,Date workDate, Integer jtStartIndex, Integer jtPageSize);
	List<ShiftTypeMaster>  listShiftTypeMaster();
	ShiftTypeMaster getShiftTypeMasterById(Integer shiftTypeId);

	boolean checkResourceAvailabilityForADate(Integer userId, Date date);
	
	ShiftTypeMaster getShiftTypeIdFromWorkShiftId(Integer shiftTypeId) ;
	List<TestfactoryResourcePool> testFactoryResourcePoolListbyTFactoryId(Integer testFactoryId);
	List<ResourceAttendanceSummaryDTO> listResourceAttendanceSummary(Integer resourcePoolId, Date workDate, Integer shiftId);
	List<ResourceAvailabilityDTO> getAvailableBookedResourcesForDate(Date workDate);
	List<ResourceAttendanceSummaryDTO> listResourceAvailabilitySummary(Integer resourcePoolId, Date workDate, Integer shiftId);
	List<ResourceAttendanceSummaryDTO> listWorkpackageDemandSummary(Integer resourcePoolId, Date workDate, Integer shiftId);
	List<ResourceAttendanceSummaryDTO> listResourceReliable(Integer resourcePoolId, Date startDate, Date endDate, Integer userId);
	ResourceAttendanceSummaryDTO listResourceReliableTotalBooking(Integer userId, Date startDate, Date endDate);
	ResourceAvailability listShowUpandOnTime(Integer userId, Date workDate,	Integer shiftTypeId);
	List<ResourceAvailability> getResourceAvailability(int shiftTypeId, Date getAvailabilityForDate, List<Integer> resourcePoolIds);
	List<TestFactoryResourceReservation> listResourceReliableTotalBookingSummary(Integer userId, Date startDate, Date endDate);
	/*List<ReservedAndNotBookedResourceDTO> getNotAvailAndOrNotBookedResourceCount(Integer resourcePoolId, Date startDate, Date endDate);*/
	ResourceAvailability getAvailabilityAndBookingStatusOfUserByDate(Integer userId, Date reservationDate,Integer shiftTypeId);
	ResourceAvailability listResourceAVailabilityForBooking(Integer userId, Date workDate, int shiftTypeId, int availabilityStatus);
	void updateReservedResourceAvailability(ResourceAvailability resourceAvailablity, Integer availabilityStatus);
	Long listResourcesForAVailabilityCount(Integer resourcePoolId);
	ResourceCountDTO getBlockedResourcesAttendanceCount(Date reservationDate,Integer testFactoryLabId,Integer resourcePoolId, Integer shiftTypeId);
	List<ResourceAvailability> getUserAttendanceForMonth(int userId, Date dtWorkDate, Date dtMonthEndWorkDate);
	List<ResourceAvailability> listAttendaceByDate(Integer testFactoryLabId,Integer resourcePoolId, Integer shiftTypeId, Date selectedDate,List<Integer> listOfResourcePoolIds);
	boolean isAttendanceMarkedForUserByDate(int userId, Date dtWorkDate);
	List<ResourceAvailabilityDTO> getResourceAvailablityList(Date startDate,Date endDate, Integer testFactoryLabId, Integer testFactoryId,Integer productId, Integer workPackageId, Integer shiftId,String viewType);
	List<WeeklyResourceDemandDTO> listWorkpackageWeeklyDemandProjection(Integer testFactoryLabId, Integer testFactoryId, Integer productId,Integer workPackageId, Integer shiftId, Integer workWeek,
			Integer workYear);
	List<WeeklyResourceReservationDTO> listWorkpackageWeeklyResourceReservationProjection(Integer workPackageId, Integer reservationWeek,Integer reservationYear, Integer userId);
	List<WeeklyResourceDemandDTO> listWorkpackageWeeklyDemandAndReservedCountProjection(Integer workPackageId, Integer workYear);
	List<WeeklyResourceReservationDTO> listResourcePoolResourceReservationWeeklyProjection(Integer labId, Integer resourcePoolId, Integer testFactoryId, Integer productId, Integer workpackageId, Integer allocationYear, Integer userId, List<Integer> allocationWeekList);
	Integer getTotalReservationPercentageByUserId(Integer userId,Integer workWeek,Integer workYear);
	List<WorkPackageDemandProjection> getWorkpackageDemandProjection(Integer workpackageId, Integer shiftId, Integer skillId,	Integer userRoleId, Integer workWeek, Integer workYear,Integer userTypeId);
	List<TestFactoryResourceReservation> getReservedResourcesWeeklyByUserIdForUpdateOrAdd(Integer workpackageId, Integer recursiveWeek, Integer workYear,Integer shiftId, Integer skillId,Integer reservedorUnreservedUserId, Integer userRoleId, Integer userTypeId);
	List<WeeklyResourceDemandDTO> listWorkpackageWeeklyDemandForEnagementLevel(int testFactoryLabId,int testFactoryId,int productId, int workPackageId, int weekYear,List<Integer> workWeekList);
	List<WeeklyResourceDemandDTO> listWorkpackageWeeklyReservationForEnagementLevel(int testFactoryLabId,int testFactoryId, int productId, int workPackageId, int weekYear, List<Integer> workWeekList);
	List<WeeklyResourceDemandDTO> listResourcePoolResourceReservationDetailedWeeklyProjection(Integer labId, Integer resourcePoolId, Integer testFactoryId,	Integer productId, Integer workpackageId, Integer userId, List<Integer> workWeek, Integer workYear);
	Integer getTotalReservationPercentageByRoleAndSkill(Integer userId,Integer workPackageId, Integer workWeek,Integer workYear, Integer skillId, Integer userRoleId, Integer userTypeId);
	List<Object[]> getWorkpackageAndProductAndTestFactoryCombinations();
	Integer updateDemandProjectionByNonMatchedCombList(Integer workWeek, String WpIds);
}
