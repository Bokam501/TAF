package com.hcl.atf.taf.dao;

import java.util.Date;
import java.util.List;

import com.hcl.atf.taf.model.ResourceAvailability;
import com.hcl.atf.taf.model.TestFactoryResourceReservation;
import com.hcl.atf.taf.model.UserList;
import com.hcl.atf.taf.model.UserRoleMaster;
import com.hcl.atf.taf.model.WorkPackage;
import com.hcl.atf.taf.model.dto.ResourceCountDTO;
import com.hcl.atf.taf.model.dto.TestFactoryResourceReservationDTO;
import com.hcl.atf.taf.model.dto.WorkPackageDemandProjectionDTO;

public interface TestFactoryResourceReservationDao {

	public List<TestFactoryResourceReservation> getTestFactoryResourceReservation(int workPackageId, int shiftId, Date tfreservationDate);
	Integer saveBlockedResource(TestFactoryResourceReservation testFactoryResourceReservation);
	List<ResourceCountDTO> getProductCoreResourceOfWorkPackage(int productId);
	void removeUnblockedResources(TestFactoryResourceReservation testFactoryResourceReservation);
	List<WorkPackageDemandProjectionDTO> listWorkPackageDemandProjectionForResourcePlanning(int workPackageId, Date resourceDemandForDate);
	List<ResourceCountDTO> getBlockedResourcesOfWorkPackage(int productId, Date resourceDemandForDate);
	List<UserList> getBlockedResources(Date blockResourceForDate, int shiftId);
	List<UserList> getOtherTestFactoryCoreResources(int workPackgeId, Date date, boolean isOtherTFCoreResource);
	List<UserList> getListOfResourceBlockedForWorkpackage(int workPackageId,int shiftId, Date resourceDemandForDate);
	ResourceAvailability getResourceAvailability(Integer userId, Date date, Integer shiftId);
	TestFactoryResourceReservation getTestFactoryResourceReservation(Integer workPackageId, Integer shiftId, Date blockedDate, Integer blockedUserId, Integer actionUserId, int filter);
	boolean isResourceBlockedAlready(Integer workPackageId, Integer shiftId, Date blockedDate, Integer blockedUserId, int filter);
	List<UserList> getResourcesAvailability(int shiftId, Date blockResourceForDate, String availabilityTypeFilter, Integer resourcePoolId);
	public List<TestFactoryResourceReservation> getTestFactoryResourceReservation(int workPackageId, Date startDate, Date endDate);
	List<TestFactoryResourceReservation> getReservedResourcesOfWorkpackage(int workPackageId,int shiftId, Date resourceDemandForDate);
	public List<TestFactoryResourceReservation> listTestFactoryResourceReservationByUserIdStartDateEndDate(int resourceId, Date startDate, Date endDate);
	List<ResourceCountDTO> getBlockedResourcesForReservationDate(Date reservationDate);
	List<TestFactoryResourceReservation> listResourceReservationsByDates(Integer resourcePoolId, Date startDate, Date endDate, Integer shiftTypeId);
	List<TestFactoryResourceReservation> listResourceReservationsByDatesForResourcePool(Integer resourcePoolId, Date startDate, Date endDate, Integer shiftTypeId,Integer jtStartIndex, Integer jtPageSize);
	ResourceCountDTO getBlockedResourcesCount(Date reservationDate,Integer testFactoryLabId, Integer resourcePoolId, Integer shiftTypeId);
	List<TestFactoryResourceReservation> listTestFactoryResourceReservationByDate(Integer testFactoryLabId, Integer resourcePoolId,	Integer shiftTypeId, Date workDate,List<Integer> listOfResourcePoolIds);
	List<UserList> getResourcesResourcesOfSameWpAndDateInDifferentShift(int shiftId, Date blockResourceForDate);
	public List<TestFactoryResourceReservation> getTestFactoryResourceReservationByDateShiftAndUser(int userId, int shiftId, Date tfreservationDate);
	List<UserList> getOtherProductCoreResources(int productId, Date date, boolean isOtherProductCoreResource);
	List<WorkPackage> getWorkPackageFromTestFactoryResourceReservationByserId(int userId, int shiftId, Date createdDate);
	UserRoleMaster getProductCoreResourcesProductRole(int userId, int productId, Date date);
	List<TestFactoryResourceReservationDTO> getTestFactoryResourceReservationList(Date startDate, Date endDate, Integer testFactoryLabId, Integer testFactoryId, Integer productId, Integer workPackageId, Integer shiftId, String viewType);
	public List<UserList> getBlockedResourcesWeeklyByRoleAndSkill(Integer workpackageId,Integer workWeek, Integer workYear, Integer shiftId, Integer userRoleId, Integer skillId,Integer userTypeId);
	public List<TestFactoryResourceReservation> getReservedResourcesWeeklyByRoleAndSkill(Integer workpackageId, Integer workWeek, Integer workYear, Integer shiftId, Integer userRoleId, Integer skillId,Integer userTypeId);
	public List<UserList> getListOfResourceBlockedForWorkpackageWeekly(Integer workpackageId, Integer shiftId, Integer workWeek,Integer workYear);
	public boolean isResourceBlockedAlreadyForWeekly(Integer workpackageId,Integer shiftId, Integer workWeek, Integer workYear,
			Integer userId, int filter);
	public List<TestFactoryResourceReservation> getTestFactoryResourceReservationByUsingWeek(Integer workpackageId, Integer shiftId, Integer workWeek,Integer workYear, Integer userId, Integer loggedInUserId, int filter,Long groupDemandId,Integer userTypeId);
	public void updateReservedResourcePercentage(Integer workpackageId,Integer recursiveWeek, Integer workYear, Integer reserveShiftId,Integer reserveSkillId, Integer reservationPercentage,Integer userId, Integer reserveUserRoleId,Integer userTypeId);
	
}
