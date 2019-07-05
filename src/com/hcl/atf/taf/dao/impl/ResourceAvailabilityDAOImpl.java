package com.hcl.atf.taf.dao.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.SQLQuery;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.hcl.atf.taf.constants.IDPAConstants;
import com.hcl.atf.taf.controller.utilities.DateUtility;
import com.hcl.atf.taf.dao.ResourceAvailabilityDAO;
import com.hcl.atf.taf.model.ResourceAvailability;
import com.hcl.atf.taf.model.ShiftTypeMaster;
import com.hcl.atf.taf.model.TestFactory;
import com.hcl.atf.taf.model.TestFactoryProductCoreResource;
import com.hcl.atf.taf.model.TestFactoryResourceReservation;
import com.hcl.atf.taf.model.TestfactoryResourcePool;
import com.hcl.atf.taf.model.UserList;
import com.hcl.atf.taf.model.UserRoleMaster;
import com.hcl.atf.taf.model.WorkPackageDemandProjection;
import com.hcl.atf.taf.model.WorkShiftMaster;
import com.hcl.atf.taf.model.dto.ResourceAttendanceSummaryDTO;
import com.hcl.atf.taf.model.dto.ResourceAvailabilityDTO;
import com.hcl.atf.taf.model.dto.ResourceCountDTO;
import com.hcl.atf.taf.model.dto.ResourcePoolSummaryDTO;
import com.hcl.atf.taf.model.dto.TestFactoryResourceReservationDTO;
import com.hcl.atf.taf.model.dto.WeeklyResourceDemandDTO;
import com.hcl.atf.taf.model.dto.WeeklyResourceReservationDTO;
import com.hcl.atf.taf.model.dto.WorkPackageDemandProjectionStatisticsDTO;

@Repository
public class ResourceAvailabilityDAOImpl implements ResourceAvailabilityDAO {
	
	private static final Log log = LogFactory.getLog(ResourceAvailabilityDAOImpl.class);
	
	@Autowired
	private SessionFactory sessionFactory;

	@Override
	@Transactional
	public List<ResourceAvailability> listResourceAvailability(int resourceId, Date startDate, Date endDate) {
		log.info("listing listWorkPackageDemandProjection - ByTester instance"+startDate);
		List<ResourceAvailability> resourceAvailabilityList = null;
		try {
			Criteria c = sessionFactory.getCurrentSession().createCriteria(ResourceAvailability.class);
			c.add(Restrictions.between("workDate", startDate,  endDate));
			c.add(Restrictions.eq("resource.userId", resourceId));
			
			resourceAvailabilityList = c.list();
			
			if (!resourceAvailabilityList.isEmpty()) {
				log.info("Resource Availability : "+resourceAvailabilityList.size());
				for (ResourceAvailability resourceAvailability : resourceAvailabilityList) {
					Hibernate.initialize(resourceAvailability.getResource());
					Hibernate.initialize(resourceAvailability.getWorkShiftMaster());
					Hibernate.initialize(resourceAvailability.getShiftTypeMaster());
				}
			}
			
			log.debug("list successful");
		} catch (RuntimeException re) {
			log.error("list failed", re);
			// throw re;
		}
		return resourceAvailabilityList;
	}
	
	
	@Override
	@Transactional
	public List<ResourceAvailability> updateResourceAvailability(List<ResourceAvailability> resourceAvailabilityList) {
		log.info("updating WorkPackageDemandProjection instance");
		
		List<ResourceAvailability> updatedWorkPackageDemandProjections = new ArrayList<ResourceAvailability>();
		try {
			if (resourceAvailabilityList == null || resourceAvailabilityList.isEmpty()) {
				log.info("No Resource availability available to update");
				return null;
			}

			for (ResourceAvailability resourceAvailability : resourceAvailabilityList) {

				Date demandDate = resourceAvailability.getWorkDate();
				List list = sessionFactory.getCurrentSession().createQuery("from ResourceAvailability demand where "
						+ " year(demand.workDate) = :year"
						+ " and month(demand.workDate) = :month"
						+ " and day(demand.workDate) = :day"
						+ " and demand.resource.userId = :userId"
							+ " and demand.shiftTypeMaster.shiftTypeId = :shiftTypeId")
						.setParameter("year", DateUtility.getYearOfDate(demandDate))
						.setParameter("month", DateUtility.getMonthOfDate(demandDate))
						.setParameter("day", DateUtility.getDateOfDate(demandDate))
						.setParameter("userId",resourceAvailability.getResource().getUserId().intValue())
						.setParameter("shiftTypeId", resourceAvailability.getShiftTypeMaster().getShiftTypeId().intValue())
						.list();
				
				
				int resourceCount = 0;
				if ((resourceAvailability.getIsAvailable() == null) || (resourceAvailability.getIsAvailable().intValue() == 0)) {
					resourceCount = 0;
				} else {
					resourceCount = resourceAvailability.getIsAvailable().intValue();
				}
				
				log.info("Search Date : " +  DateUtility.getDateOfDate(demandDate) + "-" + DateUtility.getMonthOfDate(demandDate) + "-"+ DateUtility.getYearOfDate(demandDate));
				log.info("Resource Count : " + resourceCount);
				log.info("List count : " + list.size() + " : List is empty : " + list.isEmpty());
				ResourceAvailability resourceAvailabilityFromDB = null;
				if (list.isEmpty()) {
					resourceAvailabilityFromDB = null;
					log.info("Did not find object : " + resourceAvailability.getResource().getUserId() + " : " + resourceAvailability.getWorkShiftMaster().getShiftId() + " : "+ resourceAvailability.getWorkDate());
				} else {
					resourceAvailabilityFromDB = (ResourceAvailability)list.get(0);
					log.info("Found demand object for : " +  resourceAvailabilityFromDB.getResourceAvailabilityId() + " : " + resourceAvailabilityFromDB.getResource().getUserId() + " : " + resourceAvailabilityFromDB.getShiftTypeMaster().getShiftTypeId() + " : "+ resourceAvailabilityFromDB.getWorkDate());
				}
				if (resourceAvailabilityFromDB == null) {
					if (resourceCount <= 0) {
						log.info("Not adding as resource count is : " + resourceCount);
					} else {
						sessionFactory.getCurrentSession().save(resourceAvailability);
						log.info("Added new demand Resource Count : " + resourceCount);
						updatedWorkPackageDemandProjections.add(resourceAvailability);
					}
				} else {
					
					resourceAvailabilityFromDB.setIsAvailable(resourceCount);
					sessionFactory.getCurrentSession().update(resourceAvailabilityFromDB);
					log.info("Updated Resource Count : " + resourceCount);
					updatedWorkPackageDemandProjections.add(resourceAvailabilityFromDB);
				}
			}
			log.info("Updated workpackage demand projection successfully");
		} catch (Exception e) {
			log.error("update failed", e);
			return null;
		}
		return updatedWorkPackageDemandProjections;
	}

	@Override
	@Transactional
	public List<WorkPackageDemandProjection> listWorkPackageDemandProjectionForResourcePlanning(
			int workPackageId, Date resourceDemandForDate) {
				log.info("inside listWorkPackageDemandProjectionForResourcePlanning in ResoureAvailabilityDAO");
				List<WorkPackageDemandProjection> workPackageDemandProjectionList = null;
				try {
					Criteria c = sessionFactory.getCurrentSession().createCriteria(WorkPackageDemandProjection.class, "wpdemand");
					c.add(Restrictions.eq("wpdemand.workDate", resourceDemandForDate));
					c.add(Restrictions.eq("wpdemand.workPackage.workPackageId", workPackageId));
					workPackageDemandProjectionList = c.list();
					if (!workPackageDemandProjectionList.isEmpty()) {
						log.info("Resource Demand : "+workPackageDemandProjectionList.size());
						for (WorkPackageDemandProjection workPackageDemandProjection : workPackageDemandProjectionList) {
							Hibernate.initialize(workPackageDemandProjection.getWorkPackage());
							Hibernate.initialize(workPackageDemandProjection.getWorkShiftMaster());
							Hibernate.initialize(workPackageDemandProjection.getWorkPackage().getProductBuild().getProductVersion().getProductMaster());
						}
					}
					
					log.debug("list successful");
				} catch (RuntimeException re) {
					log.error("list failed", re);
				}
				return workPackageDemandProjectionList;
	}

	@Override
	@Transactional
	public List<ResourceCountDTO> getBlockedResourcesOfWorkPackage(int workPackageId, Date resourceDemandForDate) {
		
		List<ResourceCountDTO> listOfRResourceCountDTO = new ArrayList<ResourceCountDTO>();
		try {
		Criteria c = sessionFactory.getCurrentSession().createCriteria(TestFactoryResourceReservation.class, "tfrr");
		c.createAlias("tfrr.workPackage", "wp");
		c.createAlias("tfrr.shift", "shift");
		c.createAlias("tfrr.blockedUser", "bu");
		c.createAlias("tfrr.reservationActionUser", "actionUser");
		c.add(Restrictions.eq("wp.workPackageId", workPackageId));
		c.add(Restrictions.eq("tfrr.reservationDate", resourceDemandForDate));
		ProjectionList projectionList = Projections.projectionList();
		projectionList = Projections.projectionList();
		projectionList.add(Property.forName("wp.workPackageId").as("workPackageId"));
		projectionList.add(Property.forName("shift.shiftId").as("shiftId"));
		projectionList.add(Projections.count("bu.userId").as("blockedUserCount"));
		projectionList.add(Projections.groupProperty("wp.workPackageId"));
		projectionList.add(Projections.groupProperty("shift.shiftId"));
		c.setProjection(projectionList);
		
		List<Object[]> list = c.list();
		log.info("Result Set Size : " + list.size());
		ResourceCountDTO resourceCountDTO = null;
		for (Object[] row : list) {
			resourceCountDTO = new ResourceCountDTO();
			resourceCountDTO.setWorkPackageId(((Integer)row[0]).intValue());
			resourceCountDTO.setShiftId(((Integer)row[1]).intValue());
			resourceCountDTO.setBlockedResourcesCount(((Double)row[2]).floatValue());
			listOfRResourceCountDTO.add(resourceCountDTO);
			log.info("Status Summary for work package Id : "  +((Integer)row[0]).intValue() + "  Shift : "+((Integer)row[1]).intValue()+"   Blocked Resource Count : " + ((Long)row[2]).intValue());
		}
		} catch (Exception e) {
			log.error("update failed", e);
			return null;
		}
		return listOfRResourceCountDTO;
	}
	
	@Override
	@Transactional
	public List<ResourceCountDTO> getProductCoreResourceOfWorkPackage(int productId) {
		log.info("Inside getProductCoreResourceOfWorkPackage()");
		List<ResourceCountDTO> listOfProductCoreResource = new ArrayList<ResourceCountDTO>();
		try {
		Criteria c = sessionFactory.getCurrentSession().createCriteria(TestFactoryProductCoreResource.class, "tfpcr");
		c.createAlias("tfpcr.productMaster", "product");
		c.createAlias("tfpcr.userList", "user");
		c.add(Restrictions.eq("product.productId", productId));
		ProjectionList projectionList = Projections.projectionList();
		projectionList.add(Property.forName("product.productId").as("productId"));
		projectionList.add(Projections.count("user.userId").as("productCoreResourceCount"));
		projectionList.add(Projections.groupProperty("product.productId"));
		c.setProjection(projectionList);
		
		List<Object[]> list = c.list();
		log.info("Result Set Size in getProductCoreResourceOfWorkPackage : " + list.size());
		ResourceCountDTO resourceCountDTO = null;
		for (Object[] row : list) {
			resourceCountDTO = new ResourceCountDTO();
			resourceCountDTO.setProductId(((Integer)row[0]).intValue());
			resourceCountDTO.setCoreResourceCount(((Long)row[1]).intValue());
			listOfProductCoreResource.add(resourceCountDTO);
			log.info("Status Summary for Product Id : "  +((Integer)row[0]).intValue() + "  core user Count  : "+((Long)row[1]).intValue());
		}
		} catch (Exception e) {
			log.error("update failed", e);
			return null;
		}
		return listOfProductCoreResource;
	}
	
	@Override
	@Transactional
	public List<UserList> listResourcesForAvailabilityPlan(Integer resourcePoolId) {
		log.info("listing listResourcesForAvailabilityPlan");
		List<UserList> userListCollection = new ArrayList<UserList>();
		log.info("Getting User List for AvailabilityPlan : " + resourcePoolId);
		try {
			List<Integer> roleIds = new ArrayList<Integer>();
			roleIds.add(3);
			roleIds.add(4);// Test Lead
			roleIds.add(5);// Tester
			Criteria c = sessionFactory.getCurrentSession().createCriteria(UserList.class, "user");
			c.createAlias("user.userRoleMaster", "role");
			c.createAlias("user.vendor", "vendor");
			c.add(Restrictions.eq("user.status", 1));
			c.add(Restrictions.eq("vendor.status", 1));
			c.add(Restrictions.eq("resourcePool.resourcePoolId", resourcePoolId));
			c.add(Restrictions.in("role.userRoleId", roleIds));
			userListCollection = c.list();
			for (UserList userList : userListCollection) {
				Hibernate.initialize(userList.getCommonActiveStatusMaster());
				Hibernate.initialize(userList.getResourcePool());
				Hibernate.initialize(userList.getUserRoleMaster());
				Hibernate.initialize(userList.getVendor());
				Hibernate.initialize(userList.getUserTypeMasterNew());
				Hibernate.initialize(userList.getUserSkills());
				
			}
			log.debug("list successful");
		} catch (RuntimeException re) {
			log.error("list failed", re);
			// throw re;
		}
		return userListCollection;
	}

	@Override
	@Transactional
	public List<UserList> listResourcesForAvailabilityPlan(Map<String, String> searchStrings, String searchResourcePoolName,String searchUserName,Integer resourcePoolId, Integer jtStartIndex, Integer jtPageSize) {
		log.info("listing listResourcesForAvailabilityPlan");
		List<UserList> userListCollection = new ArrayList<UserList>();
		log.info("Getting User List for AvailabilityPlan : " + resourcePoolId);
		try {
			Criteria c = sessionFactory.getCurrentSession().createCriteria(UserList.class, "user");
			c.createAlias("user.userRoleMaster", "role");
			c.createAlias("user.vendor", "vendor");
			c.createAlias("user.resourcePool", "resourcePool");
			c.createAlias("user.userTypeMasterNew", "usertype");
			c.add(Restrictions.eq("user.status", 1));
			c.add(Restrictions.eq("vendor.status", 1));
			c.add(Restrictions.eq("resourcePool.resourcePoolId", resourcePoolId));
			c.add(Restrictions.in("role.userRoleId", Arrays.asList(3,4,5)));
			
			
			if(searchStrings.get("searchUserName") != null){
				c.add(Restrictions.ilike("user.userDisplayName", searchStrings.get("searchUserName"),MatchMode.ANYWHERE));
			}			
			if(searchStrings.get("searchResourcePoolName") != null){
				c.add(Restrictions.ilike("resourcePool.resourcePoolName", searchStrings.get("searchResourcePoolName"),MatchMode.ANYWHERE));
			}
			if(searchStrings.get("searchUserTypeName") != null){
				c.add(Restrictions.ilike("usertype.userTypeName", searchStrings.get("searchUserTypeName"),MatchMode.ANYWHERE));
			}
			if(searchStrings.get("searchRoleName") != null){
				c.add(Restrictions.ilike("role.roleName", searchStrings.get("searchRoleName"),MatchMode.ANYWHERE));
			}
			if(searchStrings.get("searchRegisteredCompanyName") != null){
				c.add(Restrictions.ilike("vendor.registeredCompanyName", searchStrings.get("searchRegisteredCompanyName"),MatchMode.ANYWHERE));
			}
			
			if(jtStartIndex != null && jtPageSize != null)
				c.setFirstResult(jtStartIndex).setMaxResults(jtPageSize);
			userListCollection = c.list();
			for (UserList userList : userListCollection) {
				Hibernate.initialize(userList.getCommonActiveStatusMaster());
				Hibernate.initialize(userList.getResourcePool());
				Hibernate.initialize(userList.getUserRoleMaster());
				Hibernate.initialize(userList.getVendor());
				Hibernate.initialize(userList.getUserTypeMasterNew());
				Hibernate.initialize(userList.getUserSkills());
			}
			log.info("list successful"+userListCollection.size());
		} catch (RuntimeException re) {
			log.error("list failed", re);
			// throw re;
		}
		return userListCollection;
	}
	
	@Override
	@Transactional
	public List<UserList> getBlockedResources(Date resourceDemandForDate, int shiftId) {
		log.info("inside getListOfResourceBlockedForWorkpackage() of ResourceAvailability DAO Impl");
		log.info("shiftId: "+shiftId);
		log.info("resourceDemandForDate: "+resourceDemandForDate);
		List<TestFactoryResourceReservation> listOfTestFactoryResourceReserved = new ArrayList<TestFactoryResourceReservation>();
		List<UserList> listOfBlockedUsers = new ArrayList<UserList>();
		try {
			Criteria c = sessionFactory.getCurrentSession().createCriteria(TestFactoryResourceReservation.class, "tfrr");
			c.createAlias("tfrr.shift", "shift");
			c.createAlias("tfrr.blockedUser", "bu");
			c.add(Restrictions.eq("shift.shiftId", shiftId));
			c.add(Restrictions.eq("tfrr.reservationDate", resourceDemandForDate));
			
			listOfTestFactoryResourceReserved = c.list();
			log.info("Result Set Size : " + listOfTestFactoryResourceReserved.size());
			for (TestFactoryResourceReservation testFactoryResourceReservation : listOfTestFactoryResourceReserved) {
				Hibernate.initialize(testFactoryResourceReservation.getBlockedUser());
				listOfBlockedUsers.add(testFactoryResourceReservation.getBlockedUser());
			}
		} catch (Exception e) {
			log.error("update failed", e);
			return null;
		}
		return listOfBlockedUsers;
	}

	@Override
	@Transactional
	public List<UserList> getResourcePoolwithRole(int resourcePoolId) {

			log.info("DAO Impl -- By ResourcePoolIdWithRole");
			List<UserList> userList = new ArrayList<UserList>();
			try{
				log.info(", resourcePoolId="+resourcePoolId);			
				 if(resourcePoolId != 0){
					log.info("DAO Impl -- By ResourcePoolId"+resourcePoolId);
					//RP and FL available
					Criteria c = sessionFactory.getCurrentSession().createCriteria(UserList.class);
					c.add(Restrictions.eq("resourcePool.resourcePoolId", resourcePoolId));
					if(userList.size() ==0){
						userList = c.list();
						log.info("Resourpool "+userList.size());
					}else{
						userList = null;
						userList = c.list();
						log.info("Refresshed Resourpool "+userList.size());
					}				
				}		
				if (!(userList == null || userList.isEmpty())) {
					for (UserList user : userList) {
						Hibernate.initialize(user.getUserTypeMasterNew());
						Hibernate.initialize(user.getUserRoleMaster());
						Hibernate.initialize(user.getResourcePool());
						Hibernate.initialize(user.getVendor());	
						Hibernate.initialize(user.getCommonActiveStatusMaster());
					}
				}
				log.debug("list successful");
			}catch (RuntimeException re) {
				log.error("list failed", re);
				// throw re;			
			}			
			return userList;
	}


	@Override
	@Transactional
	public List<ResourcePoolSummaryDTO> getResourcePoolwithRoleDTO(int resourcePoolId, List<UserRoleMaster> roleList) {			
			List<ResourcePoolSummaryDTO> userListDTO = new ArrayList<ResourcePoolSummaryDTO>();
			try{				
				List<String> requiredRoleNames = new ArrayList<String>();
				for(UserRoleMaster urm : roleList){
					requiredRoleNames.add(urm.getRoleName());
				}
				Criteria c = sessionFactory.getCurrentSession().createCriteria(UserList.class,"userList");
				c.createAlias("userList.resourcePool", "rPool");
				c.createAlias("userList.userRoleMaster", "role");
				c.createAlias("userList.userTypeMasterNew", "utype");
				c.add(Restrictions.eq("rPool.resourcePoolId", resourcePoolId));
				c.add(Restrictions.in("role.roleName", requiredRoleNames));
				ProjectionList projectionList = Projections.projectionList();
				projectionList.add(Property.forName("rPool.resourcePoolId").as("rPoolId"));
				projectionList.add(Property.forName("rPool.resourcePoolName").as("rPoolName"));
				projectionList.add(Property.forName("utype.userTypeId").as("userTypeId"));
				projectionList.add(Property.forName("utype.userTypeLabel").as("userTypeLabel"));
				projectionList.add(Property.forName("role.userRoleId").as("userRoleId"));
				projectionList.add(Property.forName("role.roleName").as("userRoleName"));
				projectionList.add(Projections.count("userList.userId"));
				projectionList.add(Projections.groupProperty("role.userRoleId"));
				projectionList.add(Projections.groupProperty("utype.userTypeId"));
				c.setProjection(projectionList);
				
				List<Object[]> list = c.list();		
				ResourcePoolSummaryDTO resourcePoolSummaryDTO = null;
				for (Object[] row : list) {
					resourcePoolSummaryDTO = new ResourcePoolSummaryDTO();
					resourcePoolSummaryDTO.setResourcePoolId((Integer)row[0]);
					resourcePoolSummaryDTO.setResourcePoolName((String)row[1]);		
					resourcePoolSummaryDTO.setUserTypeId((Integer)row[2]);
					resourcePoolSummaryDTO.setUserTypeLabel((String)row[3]);
					resourcePoolSummaryDTO.setUserRoleId((Integer)row[4]);
					resourcePoolSummaryDTO.setRoleName((String)row[5]);
					resourcePoolSummaryDTO.setRole_Count(((Long)row[6]).intValue());
					userListDTO.add(resourcePoolSummaryDTO);
				}				
				log.debug("list successful");
			}catch (RuntimeException re) {
				log.error("list failed", re);
				// throw re;			
			}			
			return userListDTO;
	}
	
	@Override
	@Transactional
	public List<UserList> getOtherProductCoreResources(int workPackgeId) {
		return null;
	}


	@Override
	@Transactional
	public List<UserList> getOtherTestFactoryCoreResources(int workPackgeId) {
		return null;
	}


	@Override
	@Transactional
	public List<UserList> getListOfResourceBlockedForWorkpackage(int workPackageId, int shiftId, Date resourceDemandForDate) {
		log.info("inside getListOfResourceBlockedForWorkpackage() of ResourceAvailability DAO Impl");
		log.info("workPackageId: "+workPackageId);
		log.info("shiftId: "+shiftId);
		log.info("resourceDemandForDate: "+resourceDemandForDate);
		List<TestFactoryResourceReservation> listOfTestFactoryResourceReserved = new ArrayList<TestFactoryResourceReservation>();
		List<UserList> listOfBlockedUsers = new ArrayList<UserList>();
		try {
			Criteria c = sessionFactory.getCurrentSession().createCriteria(TestFactoryResourceReservation.class, "tfrr");
			c.createAlias("tfrr.workPackage", "wp");
			c.createAlias("tfrr.shift", "shift");
			c.add(Restrictions.eq("wp.workPackageId", workPackageId));
			c.add(Restrictions.eq("shift.shiftId", shiftId));
			c.add(Restrictions.eq("tfrr.reservationDate", resourceDemandForDate));
			
			listOfTestFactoryResourceReserved = c.list();
			log.info("Result Set Size : " + listOfTestFactoryResourceReserved.size());
			for (TestFactoryResourceReservation testFactoryResourceReservation : listOfTestFactoryResourceReserved) {
				Hibernate.initialize(testFactoryResourceReservation.getBlockedUser());
				listOfBlockedUsers.add(testFactoryResourceReservation.getBlockedUser());
			}
		} catch (Exception e) {
			log.error("update failed", e);
			return null;
		}
		return listOfBlockedUsers;
	}
	
	@Override
	@Transactional
	public void add(ResourceAvailability resourceAvailablity) {
		log.debug("adding Resource Availabilty instance");
		try {
			sessionFactory.getCurrentSession().save(resourceAvailablity);
			log.debug("add successful");
		} catch (RuntimeException re) {
			log.error("add failed", re);
		}
		
	}

	@Override
	@Transactional
	public void delete(ResourceAvailability resourceAvailablity) {
		log.debug("deleting Resource Availabilty instance");
		try {
			sessionFactory.getCurrentSession().delete(resourceAvailablity);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
		}	
		
	}
	
	@Override
	@Transactional
	public void updateResourceAvailability(ResourceAvailability resourceAvailablity) {
		log.debug("updating Resource Availabilty instance");
		try {
			sessionFactory.getCurrentSession().update(resourceAvailablity);
			log.debug("update successful");
		} catch (RuntimeException re) {
			log.error("update failed", re);
		}	
		
	}
	
	
	@Override
	@Transactional
	public ResourceAvailability getResourceAvailability(Integer userId, Date date, Integer shiftTypeId) {
		
		if(userId == null || date == null || shiftTypeId == null) {
			log.info("Required info for getting resource availability is missing");
			return null;
		}
		
		Criteria c = sessionFactory.getCurrentSession().createCriteria(ResourceAvailability.class, "resourceAvailability");
		c.createAlias("resourceAvailability.resource", "user");
		c.createAlias("resourceAvailability.shiftTypeMaster", "shift");
		c.add(Restrictions.eq("resourceAvailability.workDate", date));
		c.add(Restrictions.eq("user.userId", userId));
		c.add(Restrictions.eq("shift.shiftTypeId", shiftTypeId));
		
		List<ResourceAvailability> resourceAvailabilityList = c.list();
		for(ResourceAvailability resourceAvailability : resourceAvailabilityList) {
			Hibernate.initialize(resourceAvailability.getResource());
			Hibernate.initialize(resourceAvailability.getWorkShiftMaster());
			Hibernate.initialize(resourceAvailability.getShiftTypeMaster());

		}
		if (resourceAvailabilityList.size() > 0)
			return (ResourceAvailability)resourceAvailabilityList.get(0);
		else 
			return null;
	}

	@Override
	@Transactional
	public List<ResourceAvailability> listResourceAvailablityByDate(
			Date workDate) {
		List<ResourceAvailability> resourceAvailabilityList = null;
		try {
			
			Criteria c = sessionFactory.getCurrentSession().createCriteria(ResourceAvailability.class);
			c.add(Restrictions.eq("workDate", workDate));
			resourceAvailabilityList = c.list();
			
			if (resourceAvailabilityList.isEmpty()) {
				log.info("Resource Availability : "+resourceAvailabilityList.size());
				for (ResourceAvailability resourceAvailability : resourceAvailabilityList) {
					Hibernate.initialize(resourceAvailability.getResource());
					Hibernate.initialize(resourceAvailability.getWorkShiftMaster());
				}
			}
			
			log.debug("list successful Resource Availability" +resourceAvailabilityList.size());
		} catch (RuntimeException re) {
			log.error("list failed", re);
		}
		return resourceAvailabilityList;
	}

	@Override
	@Transactional
	public TestFactoryResourceReservationDTO listTestFactoryResourceReservationByWorkpackageIdDate(
			Date startDate, Date endDate, Integer testFactoryLabId, Integer testFactoryId, Integer productId, Integer workPackageId, Integer shiftId,Date workDate) {
		TestFactoryResourceReservationDTO testFactoryResourceReservationDTO = new TestFactoryResourceReservationDTO();
		try {
			Criteria c = sessionFactory.getCurrentSession().createCriteria(TestFactoryResourceReservation.class, "tfrr");
			c.createAlias("tfrr.workPackage", "workPackage");
			c.createAlias("workPackage.productBuild", "productBuild");
			c.createAlias("productBuild.productVersion", "productVersion");
			c.createAlias("productVersion.productMaster", "product");
			c.createAlias("product.testFactory", "testFactory");
			c.createAlias("testFactory.testFactoryLab", "testFactoryLab");
			ProjectionList projectionList = Projections.projectionList();

			if (shiftId != null && shiftId!=-1) {
				c.createAlias("tfrr.shift", "shift");
				c.add(Restrictions.eq("shift.shiftId", shiftId.intValue()));
			}
			
			if (workPackageId != null) {
				c.add(Restrictions.eq("workPackage.workPackageId", workPackageId));
			} 
			if (productId != null) {
				
				c.add(Restrictions.eq("product.productId", productId));
			} 
			if (testFactoryId != null) {
				c.add(Restrictions.eq("testFactory.testFactoryId", testFactoryId));
			} 
			if (testFactoryLabId != null) {
							
				c.add(Restrictions.eq("testFactoryLab.testFactoryLabId", testFactoryLabId));
			} 
			c.createAlias("tfrr.blockedUser", "b");
			c.add(Restrictions.eq("tfrr.reservationDate", workDate));
			
			projectionList.add(Projections.count("b.userId").as("blockedCount"));
			projectionList.add(Projections.groupProperty("tfrr.reservationDate"));
			
			c.setProjection(projectionList);
			
			List<Object[]> list = c.list();
			
			log.info("Result Set Size : " + list.size());
			
			Object [] row = (list != null && list.size() != 0) ? list.get(0) : null;
					
			if(row!=null) {
				testFactoryResourceReservationDTO.setBlockedCount(((BigDecimal)row[0]).floatValue());
			}
			
			log.debug("list successful");
		} catch (RuntimeException re) {
			log.error("list failed", re);
			// throw re;
		}
		
		return testFactoryResourceReservationDTO;
	}

	@Override
	@Transactional
	public ShiftTypeMaster getShiftTypeIdFromWorkShiftId(Integer shiftId) {
		log.info("inside getShiftTypeIdFromWorkShiftId "+shiftId);
		WorkShiftMaster workShiftMaster = null;
		ShiftTypeMaster shiftTypeMaster=null;
		List<WorkShiftMaster> workShiftMasterList = null;

		try {
			Criteria c = sessionFactory.getCurrentSession().createCriteria(WorkShiftMaster.class, "wsm");
			c.add(Restrictions.eq("wsm.shiftId", shiftId));

			workShiftMasterList = c.list();
			workShiftMaster = (workShiftMasterList != null && workShiftMasterList.size() != 0) ? workShiftMasterList.get(0) : null;
			if(workShiftMaster!=null){
				Hibernate.initialize(workShiftMaster.getShiftType());
				shiftTypeMaster=workShiftMaster.getShiftType();
			}
			
			log.debug("list successful");
		} catch (RuntimeException re) {
			log.error("list failed", re);
		}
		return shiftTypeMaster;
	}

	@Override
	@Transactional
	public ResourceAvailabilityDTO listResourceAvailablityByDate(Date startDate, Date endDate, Integer testFactoryLabId,
			Integer testFactoryId, Integer productId, Integer workPackageId,
			Integer shiftId,Date workDate) {
		log.info("listing listResourceAvailabilityDTO");
		ResourceAvailabilityDTO resourceAvailabilityDTO = new ResourceAvailabilityDTO();
		
		try {
			Criteria c = sessionFactory.getCurrentSession().createCriteria(ResourceAvailability.class, "ra");


			ProjectionList projectionList = Projections.projectionList();

			if (shiftId != null &&shiftId!=-1) {
				ShiftTypeMaster shiftTypeMaster= getShiftTypeIdFromWorkShiftId(shiftId);
				
				c.createAlias("ra.shiftTypeMaster", "shift");
				c.add(Restrictions.eq("shift.shiftTypeId", shiftTypeMaster.getShiftTypeId()));
			}
			c.createAlias("ra.resource", "r");
			c.add(Restrictions.eq("ra.workDate", workDate));
			c.add(Restrictions.eq("ra.isAvailable", 1));
			
			projectionList.add(Projections.count("r.userId").as("resourceAvailabilityCount"));
			projectionList.add(Projections.groupProperty("ra.workDate"));
			
			c.setProjection(projectionList);
			
			List<Object[]> list = c.list();
			
			log.info("Result Set Size : " + list.size());
			Object [] row = (list != null && list.size() != 0) ? list.get(0) : null;
			
			if(row!=null) {
				resourceAvailabilityDTO.setResourceAvailabilityCount(((BigDecimal)row[0]).floatValue());
			}
			
			log.debug("list successful");
		} catch (RuntimeException re) {
			log.error("list failed", re);
			// throw re;
		}
		return resourceAvailabilityDTO;
	}


	@Override
	@Transactional
	public void saveBlockedResource(TestFactoryResourceReservation testFactoryResourceReservation) {
		log.info("adding resource into TestFactoryResourceReservation");
		try {	
			sessionFactory.getCurrentSession().save(testFactoryResourceReservation);
		} catch (RuntimeException re) {
			log.error("add failed", re);
		}	
		
	}


	@Override
	@Transactional
	public void removeUnblockedResources(TestFactoryResourceReservation testFactoryResourceReservation) {
		log.info("remove resource from TestFactoryResourceReservation");
		try {	
			sessionFactory.getCurrentSession().delete(testFactoryResourceReservation);
		} catch (RuntimeException re) {
			log.error("delete failed", re);
		}	
	}



	@Override
	@Transactional
	public List<UserList> getAllUsersFromResourcePools(int workPackgeId, int shiftId, Date blockResourceForDate) {
		//For now, return resources from all resource pools
		List<ResourceAvailability> listOfAvailableResources = null;
		List<UserList> listOfAvailableUsers = new ArrayList<UserList>();
		try {
			List<String> requiredRoleNames = new ArrayList<String>();
			requiredRoleNames.add("TestLead");
			requiredRoleNames.add("Tester");
			
			Criteria c = sessionFactory.getCurrentSession().createCriteria(ResourceAvailability.class, "resourceAvailability");
			c.createAlias("resourceAvailability.workShiftMaster", "shift");
			c.createAlias("resourceAvailability.resource", "resource");
			c.add(Restrictions.eq("shift.shiftId", shiftId));
			c.add(Restrictions.eq("resourceAvailability.workDate", blockResourceForDate));
			c.add(Restrictions.eq("resourceAvailability.isAvailable", 1));
			
			listOfAvailableResources = c.list();
			log.info("Result Set Size : " + listOfAvailableResources.size());
			for (ResourceAvailability resourceAvailability : listOfAvailableResources) {
				Hibernate.initialize(resourceAvailability.getResource());
				listOfAvailableUsers.add(resourceAvailability.getResource());
			}
		} catch (Exception e) {
			log.error("Getting resource pool resources failed", e);
		}
		
		return listOfAvailableUsers;
	}

	@Override
	@Transactional
	public List<ResourceAvailability> listResourceAttendance(int resourcePoolId, Date workDate, Integer shiftId) {
		List<ResourceAvailability> list = null;
		if (workDate == null || shiftId == null)
			return null;
		
		try {
			Criteria c = sessionFactory.getCurrentSession().createCriteria(ResourceAvailability.class, "ra");
			if(shiftId!=-1){
				c.createAlias("ra.shiftTypeMaster", "shift");
				c.add(Restrictions.eq("shift.shiftTypeId", shiftId.intValue()));
			}
			c.createAlias("ra.resource", "resourceid");
			c.createAlias("resourceid.resourcePool", "resourcepoolget");
			c.add(Restrictions.eq("resourcepoolget.resourcePoolId", resourcePoolId));
			c.add(Restrictions.eq("ra.workDate", workDate));
			c.add(Restrictions.disjunction().add(Restrictions.or(Restrictions.eq("ra.bookForShift", 1), Restrictions.and(Restrictions.eq("ra.isAvailable", 1),
			        Restrictions.eq("ra.bookForShift", 0)))));
			list = c.list();
			for (ResourceAvailability resourceAvailability : list) {
				Hibernate.initialize(resourceAvailability.getResource());
				Hibernate.initialize(resourceAvailability.getResource().getVendor());
				Hibernate.initialize(resourceAvailability.getResource().getUserRoleMaster());
				Hibernate.initialize(resourceAvailability.getResource().getUserTypeMasterNew());
				Hibernate.initialize(resourceAvailability.getResource().getResourcePool());
				Hibernate.initialize(resourceAvailability.getResource().getUserSkills());
				Hibernate.initialize(resourceAvailability.getWorkShiftMaster());
				Hibernate.initialize(resourceAvailability.getShiftTypeMaster());
			}
		}catch (Exception e) {
			log.error("Problem getting resource attendance data", e);
		}

		return list;
	}
	@Override
	@Transactional
	public List<ResourceAvailability> listResourceAttendance(int resourcePoolId, Date workDateFrom, Date workDateTo,Integer shiftTypeId, Integer userId) {
		List<ResourceAvailability> list = null;
		if (workDateFrom == null || shiftTypeId == null || workDateTo==null)
			return null;
		
		try {
			Criteria c = sessionFactory.getCurrentSession().createCriteria(ResourceAvailability.class, "ra");
			c.createAlias("ra.shiftTypeMaster", "shiftType");
			if(shiftTypeId !=-1){
				c.add(Restrictions.eq("shiftType.shiftTypeId", shiftTypeId.intValue()));
			}
			c.createAlias("ra.resource", "resourceid");
			c.createAlias("resourceid.resourcePool", "resourcepoolget");
			c.add(Restrictions.eq("resourceid.userId", userId));
			c.add(Restrictions.eq("resourcepoolget.resourcePoolId", resourcePoolId));
			
			c.add(Restrictions.between("ra.workDate", workDateFrom,workDateTo));
		
			c.add(Restrictions.eq("ra.isAvailable", 1));
			c.add(Restrictions.eq("ra.bookForShift", 1));
			c.addOrder(Order.asc("ra.workDate"));
			list = c.list();
			
			for (ResourceAvailability resourceAvailability : list) {
				Hibernate.initialize(resourceAvailability.getResource());
				Hibernate.initialize(resourceAvailability.getResource().getUserRoleMaster());
				Hibernate.initialize(resourceAvailability.getResource().getUserTypeMasterNew());
				Hibernate.initialize(resourceAvailability.getResource().getResourcePool());
				Hibernate.initialize(resourceAvailability.getWorkShiftMaster());
				Hibernate.initialize(resourceAvailability.getShiftTypeMaster());
			}
		}catch (Exception e) {
			log.error("Problem getting resource attendance data", e);
		}

		return list;
	}


	@Override
	@Transactional
	public ResourceAvailability getResourceAvailabilityById(Integer resourceAvailabilityId) {
		ResourceAvailability resourceAvailability = null;
		try {
			Criteria c = sessionFactory.getCurrentSession().createCriteria(ResourceAvailability.class, "ra");
			c.add(Restrictions.eq("ra.resourceAvailabilityId", resourceAvailabilityId));
			List<ResourceAvailability> list = c.list();
			resourceAvailability=(list!=null && list.size()!=0)?(ResourceAvailability)list.get(0):null;
			
			if (resourceAvailability != null) {
				Hibernate.initialize(resourceAvailability.getResource());
				Hibernate.initialize(resourceAvailability.getResource().getUserRoleMaster());
				Hibernate.initialize(resourceAvailability.getResource().getUserTypeMasterNew());
				Hibernate.initialize(resourceAvailability.getWorkShiftMaster());
				Hibernate.initialize(resourceAvailability.getShiftTypeMaster());
			}
		} catch (Exception e) {
			log.error(e);
		}
		return resourceAvailability;
	}



	@Override
	@Transactional
	public List<ResourceAvailability> listResourceAttendanceByShiftId(
			Integer userId, Date startDate, Date endDate, int shiftTypeId, Integer typeFilter) {
	
		List<ResourceAvailability> resourceAvailabilityList = null;
		try {
			Criteria c = sessionFactory.getCurrentSession().createCriteria(ResourceAvailability.class,"ra");
			c.createAlias("ra.shiftTypeMaster", "shift");
			c.createAlias("ra.resource", "resource");

			c.add(Restrictions.between("ra.workDate", startDate,  endDate));
			c.add(Restrictions.eq("resource.userId", userId));
			if(shiftTypeId!=0){
				c.add(Restrictions.eq("shift.shiftTypeId", shiftTypeId));
			}
			if(typeFilter != 0){
				c.add(Restrictions.eq("ra.isAvailable", 1));
			}
			resourceAvailabilityList = c.list();
			
			if (resourceAvailabilityList.size()!=0) {
				for (ResourceAvailability resourceAvailability : resourceAvailabilityList) {
					Hibernate.initialize(resourceAvailability.getResource());
					Hibernate.initialize(resourceAvailability.getWorkShiftMaster());
					Hibernate.initialize(resourceAvailability.getShiftTypeMaster());
				}
			}
			
			log.debug("list successful");
		} catch (RuntimeException re) {
			log.error("list failed", re);
		}
		return resourceAvailabilityList;
		
	}

	@Override
	@Transactional
	public List<WorkPackageDemandProjection> listWorkpackageDemandProjection(
			Integer testFactoryLabId, Integer testFactoryId, Integer productId,
			Integer workPackageId, Integer shiftId,Date workDate, Integer jtStartIndex, Integer jtPageSize) {
		log.info("listing listWorkpackageDemandProdjectionByWorkpackageId");
		List<WorkPackageDemandProjection> listWorkPackageDemandProjection = new ArrayList<WorkPackageDemandProjection>();
		try {
			Criteria c = sessionFactory.getCurrentSession().createCriteria(WorkPackageDemandProjection.class, "wpdp");
			c.createAlias("wpdp.workPackage", "workPackage");
			c.createAlias("workPackage.productBuild", "productbuild");
			c.createAlias("productbuild.productVersion", "productVersion");
			c.createAlias("productVersion.productMaster", "product");
			c.createAlias("product.testFactory", "tf");
			c.createAlias("tf.testFactoryLab", "tfl");
			
			ProjectionList projectionList = Projections.projectionList();
			projectionList = Projections.projectionList();
			
			if (shiftId != null && shiftId!=-1) {
				c.createAlias("wpdp.workShiftMaster", "shift");
				c.add(Restrictions.eq("shift.shiftId", shiftId.intValue()));
			}
			
			if (workPackageId != null) {
				c.add(Restrictions.eq("workPackage.workPackageId", workPackageId));
			} 
			if (productId != null) {
				c.add(Restrictions.eq("product.productId", productId));
			} 
			if (testFactoryId != null) {
				c.add(Restrictions.eq("tf.testFactoryId", testFactoryId));
			} 
			if (testFactoryLabId != null) {
				c.add(Restrictions.eq("tfl.testFactoryLabId", testFactoryLabId));
			}
			c.add(Restrictions.eq("workDate", workDate));
			
			if(jtStartIndex != null && jtPageSize != null)
				c.setFirstResult(jtStartIndex).setMaxResults(jtPageSize);
			
			listWorkPackageDemandProjection = c.list();
			if(listWorkPackageDemandProjection!=null && !listWorkPackageDemandProjection.isEmpty()){
				for(WorkPackageDemandProjection workPackageDemandProjection:listWorkPackageDemandProjection){
					Hibernate.initialize(workPackageDemandProjection.getWorkPackage());
					Hibernate.initialize(workPackageDemandProjection.getWorkPackage().getProductBuild().getProductVersion().getProductMaster());
					Hibernate.initialize(workPackageDemandProjection.getWorkShiftMaster());
					Hibernate.initialize(workPackageDemandProjection.getUserRole());
					Hibernate.initialize(workPackageDemandProjection.getDemandRaisedByUser());
					Hibernate.initialize(workPackageDemandProjection.getSkill());
				}
			}
			
			log.debug("list successful"+listWorkPackageDemandProjection.size());
		} catch (RuntimeException re) {
			log.error("list failed", re);
		}catch(Exception e){
			log.error("Error Listing WorkpackageDemandProjection : ", e);
		}	
		return listWorkPackageDemandProjection;
	}

	
	@Override
	@Transactional
	public List<WeeklyResourceDemandDTO> listWorkpackageWeeklyDemandProjection(Integer testFactoryLabId,Integer testFactoryId,Integer productId,Integer workPackageId,Integer shiftId,Integer workWeek,Integer workYear) {// TODO Auto-generated method stub
		List<WeeklyResourceDemandDTO> weeklyWorkPackageDemandProjectionDTO = new ArrayList<WeeklyResourceDemandDTO>();
		try {
			Criteria c = sessionFactory.getCurrentSession().createCriteria(WorkPackageDemandProjection.class,"workPackageDemand");
			c.createAlias("workPackageDemand.workPackage", "wp");
			c.createAlias("wp.productBuild", "pbuild");
			c.createAlias("pbuild.productVersion", "pVersion");
			c.createAlias("pVersion.productMaster", "product");
			c.createAlias("workPackageDemand.workShiftMaster", "workShift");
			c.createAlias("workPackageDemand.skill", "skill");
			c.createAlias("workPackageDemand.userRole", "userRole");
			
			c.createAlias("workPackageDemand.demandRaisedByUser", "raisedBy");
			
			c.add(Restrictions.eq("workYear", workYear));
			c.add(Restrictions.eq("wp.workPackageId", workPackageId));
			ProjectionList projectionList = Projections.projectionList();
			projectionList = Projections.projectionList();
			
			projectionList.add(Property.forName("wp.workPackageId").as("workPackageId"));
			projectionList.add(Property.forName("wp.name").as("workPackageName"));
			projectionList.add(Property.forName("workShift.shiftId").as("shiftId"));
			projectionList.add(Property.forName("workShift.shiftName").as("shiftName"));
			
			projectionList.add(Property.forName("skill.skillId").as("skillId"));
			projectionList.add(Property.forName("skill.skillName").as("skillName"));
			
			projectionList.add(Property.forName("userRole.userRoleId").as("userRoleId"));
			projectionList.add(Property.forName("userRole.roleName").as("roleName"));
			
			projectionList.add(Property.forName("workPackageDemand.workWeek").as("workWeek"));
			projectionList.add(Property.forName("workPackageDemand.workYear").as("workYear"));
			projectionList.add(Projections.sum("resourceCount").as("totalResourceCount"));
			
			projectionList.add(Property.forName("raisedBy.userId").as("raisedUserId"));
			projectionList.add(Property.forName("raisedBy.loginId").as("loginId"));
			
			projectionList.add(Property.forName("workPackageDemand.groupDemandId").as("groupDemandId"));
			
			
			
			projectionList.add(Projections.groupProperty("workPackage.workPackageId"));
			projectionList.add(Projections.groupProperty("workShift.shiftId"));
			projectionList.add(Projections.groupProperty("userRole.userRoleId"));
			projectionList.add(Projections.groupProperty("skill.skillId"));
			projectionList.add(Projections.groupProperty("workPackageDemand.workWeek"));
			
			
			c.setProjection(projectionList);

			List<Object[]> list = c.list();
			log.info("Weekly WorkPackage Demand Projection count : " + list.size());
			WeeklyResourceDemandDTO wpDemandProjectionDTO = null;
				for (Object[] row : list) {
					wpDemandProjectionDTO = new WeeklyResourceDemandDTO();
					wpDemandProjectionDTO.setWorkPackageId((Integer)row[0]);
					wpDemandProjectionDTO.setWorkPackageName((String)row[1]);
					wpDemandProjectionDTO.setShiftId((Integer)(row[2]));
					wpDemandProjectionDTO.setShiftName((String)row[3]);
					
					wpDemandProjectionDTO.setSkillId((Integer)row[4]);
					wpDemandProjectionDTO.setSkillName((String)row[5]);
					
					wpDemandProjectionDTO.setRoleId((Integer)row[6]);
					wpDemandProjectionDTO.setRoleName((String)row[7]);
					
					
					wpDemandProjectionDTO.setWorkWeek((Integer)row[8]);
					wpDemandProjectionDTO.setWorkYear((Integer)row[9]);
					wpDemandProjectionDTO.setResourceCount(((Long)row[10]).intValue());
					wpDemandProjectionDTO.setDemandRaisedByUserId((Integer)row[11]);
					wpDemandProjectionDTO.setDemandRaisedByUserName((String)row[12]);
					wpDemandProjectionDTO.setGroupDemandId((Long)row[13]);
					
					weeklyWorkPackageDemandProjectionDTO.add(wpDemandProjectionDTO);
					
				}
			} catch (RuntimeException re) {
				log.error("list failed", re);
			}
			return weeklyWorkPackageDemandProjectionDTO;
		}
	
	
	
	
	
	@Override
	@Transactional
	public List<WeeklyResourceReservationDTO> listWorkpackageWeeklyResourceReservationProjection(Integer workPackageId,Integer reservationWeek,Integer reservationYear,Integer userId) {// TODO Auto-generated method stub
		List<WeeklyResourceReservationDTO> weeklyResourceDemandDTOList = new ArrayList<WeeklyResourceReservationDTO>();
		try {
			Criteria c = sessionFactory.getCurrentSession().createCriteria(TestFactoryResourceReservation.class,"resourceReservation");
			c.createAlias("resourceReservation.workPackage", "wp");
			c.createAlias("resourceReservation.shift", "workShift");
			c.createAlias("wp.productBuild", "pbuild");
			c.createAlias("pbuild.productVersion", "pVersion");
			c.createAlias("pVersion.productMaster", "product");
			c.createAlias("resourceReservation.blockedUser", "blockedUser");
			c.createAlias("blockedUser.userRoleMaster", "userRole");
						
			c.add(Restrictions.eq("resourceReservation.reservationYear", reservationYear));
			c.add(Restrictions.eq("wp.workPackageId", workPackageId));
			
			ProjectionList projectionList = Projections.projectionList();
			projectionList = Projections.projectionList();
			
			projectionList.add(Property.forName("wp.workPackageId").as("workPackageId"));
			projectionList.add(Property.forName("wp.name").as("workPackageName"));
			projectionList.add(Property.forName("userRole.userRoleId").as("userRoleId"));
			projectionList.add(Property.forName("userRole.roleName").as("roleName"));
			
			projectionList.add(Property.forName("workShift.shiftId").as("shiftId"));
			projectionList.add(Property.forName("workShift.shiftName").as("shiftName"));
			
			
			projectionList.add(Property.forName("resourceReservation.reservationWeek").as("reservationWeek"));
			projectionList.add(Property.forName("resourceReservation.reservationYear").as("reservationYear"));
			projectionList.add(Projections.sum("resourceReservation.reservationPercentage").as("reservationPercentage"));
			projectionList.add(Property.forName("resourceReservation.groupReservationId").as("groupReservationId"));
		
			
			projectionList.add(Projections.groupProperty("workPackage.workPackageId"));
			projectionList.add(Projections.groupProperty("resourceReservation.reservationWeek"));
			projectionList.add(Projections.groupProperty("userRole.userRoleId"));
			
			
			c.setProjection(projectionList);

			List<Object[]> list = c.list();
			WeeklyResourceReservationDTO weeklyResourceReservationDTO = null;
				for (Object[] row : list) {
					weeklyResourceReservationDTO = new WeeklyResourceReservationDTO();
					
					weeklyResourceReservationDTO.setWorkPackageId((Integer)row[0]);
					weeklyResourceReservationDTO.setWorkPackageName((String)row[1]);
					weeklyResourceReservationDTO.setUserRoleId((Integer)row[2]);
					weeklyResourceReservationDTO.setUserRoleName((String)row[3]);
					
					weeklyResourceReservationDTO.setShiftId((Integer)row[4]);
					weeklyResourceReservationDTO.setShiftName((String)row[5]);
					
					
					weeklyResourceReservationDTO.setReservationWeek((Integer)row[6]);
					weeklyResourceReservationDTO.setReservationYear((Integer)row[7]);
					weeklyResourceReservationDTO.setReservationPercentage(((Long)row[8]));
					weeklyResourceReservationDTO.setGroupReservationId((Long)row[9]);
					
					weeklyResourceDemandDTOList.add(weeklyResourceReservationDTO);
					
				}
			} catch (RuntimeException re) {
				log.error("list failed", re);
			}
			return weeklyResourceDemandDTOList;
		}
	
	
	@Override
	@Transactional
	public List<WorkPackageDemandProjectionStatisticsDTO> listWorkpackageDemandProjectionByRole(			
			Integer workPackageId, Integer shiftId, Date reservationDate) {
		log.info("listing listWorkpackageDemandProdjectionByWorkpackageId");
		List<WorkPackageDemandProjection> listWorkPackageDemandProjection = new ArrayList<WorkPackageDemandProjection>();
		List<WorkPackageDemandProjectionStatisticsDTO> workPackageDemandProjectionStatisticsDTOList = new ArrayList<WorkPackageDemandProjectionStatisticsDTO>();
		try {
			Criteria c = sessionFactory.getCurrentSession().createCriteria(WorkPackageDemandProjection.class, "wpdp");
			c.createAlias("wpdp.workPackage", "workPackage"); // workPackageId; 	name;
			c.createAlias("wpdp.skill", "skill");//		skillId displayName
			c.createAlias("wpdp.userRole", "userrole");// userRoleId roleName
			c.createAlias("wpdp.workShiftMaster", "shift"); //shiftName
			ProjectionList projectionList = Projections.projectionList();
			projectionList = Projections.projectionList();			
			if (shiftId != null && shiftId!=-1) {				
				c.add(Restrictions.eq("shift.shiftId", shiftId.intValue()));				
			}			
			if (workPackageId != null) {
				c.add(Restrictions.eq("workPackage.workPackageId", workPackageId));
			}			
			c.add(Restrictions.eq("workDate", reservationDate));		
			
			projectionList.add(Property.forName("wpdp.wpDemandProjectionId")); //0
			projectionList.add(Property.forName("workPackage.workPackageId")); //1
			projectionList.add(Property.forName("workPackage.name")); //2
			projectionList.add(Property.forName("skill.skillId")); //3
			projectionList.add(Property.forName("skill.displayName")); //4
			projectionList.add(Property.forName("userrole.userRoleId")); //5
			projectionList.add(Property.forName("userrole.roleName")); //6
			projectionList.add(Property.forName("shift.shiftId")); //7
			projectionList.add(Property.forName("shift.shiftName")); //8			
			projectionList.add(Property.forName("workDate")); //9
			projectionList.add(Projections.sum("wpdp.resourceCount").as("resourceCount")); //10
			projectionList.add(Projections.groupProperty("userrole.userRoleId")); //12
			c.setProjection(projectionList);
			List<Object[]> list = c.list();
			
			WorkPackageDemandProjectionStatisticsDTO wpdpStatisticsDTO = null;
			for (Object[] row : list) {
				wpdpStatisticsDTO = new WorkPackageDemandProjectionStatisticsDTO();
				wpdpStatisticsDTO.setWpDemandProjectionId((Integer)row[0]);
				wpdpStatisticsDTO.setWorkPackageId((Integer)row[1]);
				wpdpStatisticsDTO.setWorkPackageName((String)row[2]);
				wpdpStatisticsDTO.setSkillId((Integer)row[3]);
				wpdpStatisticsDTO.setSkillName((String)row[4]);
				wpdpStatisticsDTO.setUserRoleId((Integer)row[5]);
				wpdpStatisticsDTO.setRoleName((String)row[6]);
				wpdpStatisticsDTO.setShiftId((Integer)row[7]);
				wpdpStatisticsDTO.setShiftName((String)row[8]);
				wpdpStatisticsDTO.setWorkDate((Date)row[9]);
				wpdpStatisticsDTO.setResourceCount(((BigDecimal)row[10]).floatValue());
				workPackageDemandProjectionStatisticsDTOList.add(wpdpStatisticsDTO);
			}
			log.debug("list successful"+listWorkPackageDemandProjection.size());
		} catch (RuntimeException re) {
			log.error("list failed", re);
		}catch(Exception e){
			log.error("listing WorkpackageDemandProjectionByRole :", e);
		}	
		return workPackageDemandProjectionStatisticsDTOList;
	}
	
	@Override
	@Transactional
	public List<ResourceAvailability> listAvaiablitybyDate(
			Integer testFactoryLabId, Integer testFactoryId, Integer productId,
			Integer workPackageId, Integer shiftId, Date workDate, Integer jtStartIndex, Integer jtPageSize) {
		List<ResourceAvailability> resourceAvailabilityList = null;
				try {
					
					Criteria c = sessionFactory.getCurrentSession().createCriteria(ResourceAvailability.class,"ra");
					c.add(Restrictions.eq("ra.workDate", workDate));
					c.add(Restrictions.eq("ra.isAvailable", 1));
					if (shiftId != null && shiftId!=-1) {
						c.createAlias("ra.workShiftMaster", "shift");
						c.add(Restrictions.eq("shift.shiftId", shiftId.intValue()));
					}
					
					if(jtStartIndex != null && jtPageSize != null)
						c.setFirstResult(jtStartIndex).setMaxResults(jtPageSize);
					
					resourceAvailabilityList = c.list();
					log.info("Resource Availability : "+resourceAvailabilityList.size());
					if (resourceAvailabilityList!=null &&!resourceAvailabilityList.isEmpty()) {
						
						for (ResourceAvailability resourceAvailability : resourceAvailabilityList) {
							Hibernate.initialize(resourceAvailability.getResource());
							Hibernate.initialize(resourceAvailability.getWorkShiftMaster());
							Hibernate.initialize(resourceAvailability.getResource().getResourcePool());
							Hibernate.initialize(resourceAvailability.getResource().getUserSkills());
							Hibernate.initialize(resourceAvailability.getShiftTypeMaster());
						}
					}
					
					log.debug("list successful Resource Availability" +resourceAvailabilityList.size());
				} catch (RuntimeException re) {
					log.error("list failed", re);
					// throw re;
				}
				return resourceAvailabilityList;
	}


	@Override
	@Transactional
	public List<TestFactoryResourceReservation> listTestFactoryResourceReservation(
			Integer testFactoryLabId, Integer testFactoryId, Integer productId,
			Integer workPackageId, Integer shiftId, Date workDate, Integer jtStartIndex, Integer jtPageSize) {
		// TODO Auto-generated method stub
		List<TestFactoryResourceReservation> testFactoryResourceReservationList = new ArrayList<TestFactoryResourceReservation>();
		try {
			Criteria c = sessionFactory.getCurrentSession().createCriteria(TestFactoryResourceReservation.class, "tfrr");
			c.createAlias("tfrr.workPackage", "workPackage");
			c.createAlias("workPackage.productBuild", "productBuild");
			c.createAlias("productBuild.productVersion", "productVersion");
			c.createAlias("productVersion.productMaster", "product");
			c.createAlias("product.testFactory", "testFactory");
			c.createAlias("testFactory.testFactoryLab", "testFactoryLab");
			ProjectionList projectionList = Projections.projectionList();

			if (shiftId != null && shiftId!=-1) {
				c.createAlias("tfrr.shift", "shift");
				c.add(Restrictions.eq("shift.shiftId", shiftId.intValue()));
			}
			
			if (workPackageId != null) {
				c.add(Restrictions.eq("workPackage.workPackageId", workPackageId));
			} 
			if (productId != null) {
				
				c.add(Restrictions.eq("product.productId", productId));
			} 
			if (testFactoryId != null) {
				c.add(Restrictions.eq("testFactory.testFactoryId", testFactoryId));
			} 
			if (testFactoryLabId != null) {
							
				c.add(Restrictions.eq("testFactoryLab.testFactoryLabId", testFactoryLabId));
			} 
			c.add(Restrictions.eq("tfrr.reservationDate", workDate));
			
			if(jtStartIndex != null && jtPageSize != null)
				c.setFirstResult(jtStartIndex).setMaxResults(jtPageSize);
			
			testFactoryResourceReservationList = c.list();
			
			log.info("Result Set Size : " + testFactoryResourceReservationList.size());
			
			if (testFactoryResourceReservationList!=null &&!testFactoryResourceReservationList.isEmpty()) {
				
				for (TestFactoryResourceReservation testFactoryResourceReservation : testFactoryResourceReservationList) {
					Hibernate.initialize(testFactoryResourceReservation.getBlockedUser());
					Hibernate.initialize(testFactoryResourceReservation.getShift());
					Hibernate.initialize(testFactoryResourceReservation.getWorkPackage());
					Hibernate.initialize(testFactoryResourceReservation.getWorkPackage().getProductBuild().getProductVersion().getProductMaster());
					Hibernate.initialize(testFactoryResourceReservation.getReservationActionUser());
					Hibernate.initialize(testFactoryResourceReservation.getReservationActionUser().getUserSkills());
					Hibernate.initialize(testFactoryResourceReservation.getBlockedUser().getUserSkills());
				}
			}
			
			
			log.debug("list successful");
		} catch (RuntimeException re) {
			log.error("list failed", re);
			// throw re;
		}
		return testFactoryResourceReservationList;
	}
	
	@Override
	@Transactional
	public boolean checkResourceAvailabilityForADate(Integer userId, Date date) {
		
		boolean isResourceAvailable = false;
		
		if(userId == null || date == null) {
			log.info("Required info for getting resource availability is missing");
			return isResourceAvailable;
		}
		
		Criteria c = sessionFactory.getCurrentSession().createCriteria(ResourceAvailability.class, "resourceAvailability");
		c.createAlias("resourceAvailability.resource", "user");
		c.add(Restrictions.eq("resourceAvailability.workDate", date));
		c.add(Restrictions.eq("user.userId", userId));
		c.add(Restrictions.eq("resourceAvailability.isAvailable", 1));
		
		List<ResourceAvailability> resourceAvailabilityList = c.list();
		if (resourceAvailabilityList!= null && resourceAvailabilityList.size() > 0)
			isResourceAvailable = true;
		
		return isResourceAvailable;
	}


	@Override
	@Transactional
	public List<ShiftTypeMaster> listShiftTypeMaster() {
		log.info("inside listShiftTypeMaster ");
		List<ShiftTypeMaster> shiftTypeMasterList = null;
		try {
			Criteria c = sessionFactory.getCurrentSession().createCriteria(ShiftTypeMaster.class, "stm");
			shiftTypeMasterList = c.list();
			
			
			log.debug("list successful");
		} catch (RuntimeException re) {
			log.error("list failed", re);
		}
		return shiftTypeMasterList;
	}


	@Override
	@Transactional
	public ShiftTypeMaster getShiftTypeMasterById(Integer shiftTypeId) {
		log.info("inside listShiftTypeMaster ");
		ShiftTypeMaster shiftTypeMaster = null;
		List<ShiftTypeMaster> shiftTypeMasterList = null;

		try {
			Criteria c = sessionFactory.getCurrentSession().createCriteria(ShiftTypeMaster.class, "stm");
			c.add(Restrictions.eq("stm.shiftTypeId", shiftTypeId));

			shiftTypeMasterList = c.list();
			shiftTypeMaster = (shiftTypeMasterList != null && shiftTypeMasterList.size() != 0) ? shiftTypeMasterList.get(0) : null;

			
			log.debug("list successful");
		} catch (RuntimeException re) {
			log.error("list failed", re);
		}
		return shiftTypeMaster;
	}


	@Override
	public List<TestfactoryResourcePool> testFactoryResourcePoolListbyTFactoryId(Integer testFactoryId) {
		log.info("inside listTestFactory Resource Pool ");
		List<TestFactory> testFactoryList = new ArrayList<TestFactory>();
		List<TestfactoryResourcePool> testfactoryResourcePoolList = new ArrayList<TestfactoryResourcePool>();
		try {
			Criteria c = sessionFactory.getCurrentSession().createCriteria(TestFactory.class, "test");
			if(testFactoryId!=-1){
			c.add(Restrictions.eq("test.testFactoryId", testFactoryId));
			}
			testFactoryList = c.list();
			for (TestFactory testfactorylists : testFactoryList) {
				Hibernate.initialize(testfactorylists.getTestfactoryResourcePoolList());
				Hibernate.initialize(testfactorylists.getTestFactoryLab());
				testfactoryResourcePoolList.addAll(testfactorylists.getTestfactoryResourcePoolList());
			}
			log.debug("list successful");
		} catch (RuntimeException re) {
			log.error("list failed", re);
		}
		return testfactoryResourcePoolList;
	}


	@Override
	@Transactional
	public List<ResourceAttendanceSummaryDTO> listResourceAttendanceSummary(Integer resourcePoolId,  Date workDate, Integer shiftId) {
		log.info("listing listResourceAttendanceSummary");
		List<ResourceAttendanceSummaryDTO> resourceAttendanceSummaryDTOList = new ArrayList<ResourceAttendanceSummaryDTO>();
		try {
			Criteria c = sessionFactory.getCurrentSession().createCriteria(ResourceAvailability.class, "resAvail");
			c.createAlias("resAvail.shiftTypeMaster", "shift");
			if(shiftId!=-1){
				c.add(Restrictions.eq("shift.shiftTypeId", shiftId.intValue()));
			}
			c.createAlias("resAvail.resource", "resourceid");
			c.createAlias("resourceid.resourcePool", "resourcepoolget");
			c.createAlias("resourceid.vendor","vendor");
			c.add(Restrictions.eq("resourcepoolget.resourcePoolId", resourcePoolId));
			c.add(Restrictions.eq("resAvail.workDate", workDate));
			c.add(Restrictions.eq("resAvail.isAvailable", 1));
			c.add(Restrictions.disjunction().add(
	        Restrictions.or(Restrictions.eq("resAvail.bookForShift", 1),
	                		Restrictions.eq("resAvail.shiftBillingModeIsFull", 1),Restrictions.eq("resAvail.shiftAttendance", 1))));
			
			ProjectionList projectionList = Projections.projectionList();
			projectionList.add(Projections.count("resAvail.bookForShift"));
			projectionList.add(Projections.count("resAvail.shiftAttendance"));
			projectionList.add(Projections.count("resAvail.shiftBillingModeIsFull"));
			
			c.setProjection(projectionList);
			List<Object[]> list = c.list();
			log.info("Result Set Size - : " + list.size());
			
			ResourceAttendanceSummaryDTO resourceAttendanceSummaryDTO = null;
			for (Object[] row : list) {
				resourceAttendanceSummaryDTO = new ResourceAttendanceSummaryDTO();
				resourceAttendanceSummaryDTO.setBookedCount(((Long)row[0]).intValue());
				resourceAttendanceSummaryDTO.setPresentCount(((Long)row[1]).intValue());
				resourceAttendanceSummaryDTO.setFullBillingCount(((Long)row[2]).intValue());
				resourceAttendanceSummaryDTO.setBenchCount(resourceAttendanceSummaryDTO.getPresentCount() - resourceAttendanceSummaryDTO.getFullBillingCount());
				resourceAttendanceSummaryDTOList.add(resourceAttendanceSummaryDTO);
				log.info("Availability  bookForShift -->: " + ((Long)row[0]).intValue() + " shiftAttendance : " + ((Long)row[1]).intValue()+ " shiftBillingModeIsFull -->: " + ((Long)row[2]).intValue());
			}
			log.debug("list successful");
		} catch (RuntimeException re) {
			log.error("list failed", re);
		}catch(Exception e){
			log.error("list failed", e);
		}
		return resourceAttendanceSummaryDTOList;
	}
	
	
	@Override
	@Transactional
	public List<ResourceAvailabilityDTO> getAvailableBookedResourcesForDate(Date workDate) {
		// TODO Auto-generated method stub
		log.info("listing getAvailableBookedResourcesForDate ***************  ");
		List<ResourceAvailabilityDTO> listOfResourceAvailabilityDTO = new ArrayList<ResourceAvailabilityDTO>();
		
		try {
			Criteria c = sessionFactory.getCurrentSession().createCriteria(ResourceAvailability.class, "ra");
			ProjectionList projectionList = Projections.projectionList();
			
			c.createAlias("ra.resource", "res");
			c.createAlias("ra.shiftTypeMaster", "shiftType");
			c.add(Restrictions.eq("ra.workDate", workDate));
			c.add(Restrictions.eq("ra.bookForShift", 1));
			projectionList.add(Property.forName("shiftType.shiftTypeId").as("shiftTypeId"));
			projectionList.add(Property.forName("ra.workDate").as("workDate"));
			projectionList.add(Projections.count("res.userId").as("resourceAvailabilityCount"));
			projectionList.add(Projections.groupProperty("shiftType.shiftTypeId"));
			
			c.setProjection(projectionList);
			
			List<Object[]> list = c.list();
			
			log.info("Result Set Size : " + list.size());
			ResourceAvailabilityDTO resourceAvailabilityDTO = null;
			for (Object[] row : list) {
				resourceAvailabilityDTO = new ResourceAvailabilityDTO();
				resourceAvailabilityDTO.setShiftTypeId((Integer)(row[0]));
				resourceAvailabilityDTO.setWorkDate((Date)row[1]);
				resourceAvailabilityDTO.setResourceAvailabilityCount(((BigDecimal)(row[2])).floatValue());
				listOfResourceAvailabilityDTO.add(resourceAvailabilityDTO);
				log.info("Availability  bookForShift -->: " + (Integer)(row[0]) + " Date : " + (Date)row[1]+ " ResourceAvailabilityCount -->: " + ((Long)(row[2])).intValue());
			}
			
			if(listOfResourceAvailabilityDTO != null && listOfResourceAvailabilityDTO.size()>0){
				for (ResourceAvailabilityDTO resourceAvailDTO : listOfResourceAvailabilityDTO) {
					int sTypeId = resourceAvailDTO.getShiftTypeId();
				}
			}
			
			log.debug("list successful");
		} catch (RuntimeException re) {
			log.error("list failed", re);
		}
		return listOfResourceAvailabilityDTO;
	}

	@Override
	@Transactional
	public List<ResourceAttendanceSummaryDTO> listResourceAvailabilitySummary(Integer resourcePoolId,  Date workDate, Integer shiftId) {
		log.info("listing listResourceAvailabilitySummary");
		List<ResourceAttendanceSummaryDTO> resourceAttendanceSummaryDTOList = new ArrayList<ResourceAttendanceSummaryDTO>();
		ResourceAttendanceSummaryDTO resourceAttendanceSummaryDTO = new ResourceAttendanceSummaryDTO();
		try {
			
			Criteria cr = sessionFactory.getCurrentSession().createCriteria(ResourceAvailability.class, "resAvail");
			cr.createAlias("resAvail.shiftTypeMaster", "shift");
			if(shiftId!=-1){
				cr.add(Restrictions.eq("shift.shiftTypeId", shiftId.intValue()));
			}
			cr.createAlias("resAvail.resource", "resourceid");
			cr.createAlias("resourceid.resourcePool", "resourcepoolget");
			cr.createAlias("resourceid.vendor","vendor");
			cr.add(Restrictions.eq("resourcepoolget.resourcePoolId", resourcePoolId));
			cr.add(Restrictions.eq("resAvail.workDate", workDate));
			cr.add(Restrictions.eq("resAvail.bookForShift", 1));
			
			ProjectionList projectionList1 = Projections.projectionList();
			projectionList1.add(Projections.count("resAvail.bookForShift"));
			projectionList1.add(Property.forName("shift.shiftTypeId").as("shiftTypeId"));
			projectionList1.add(Property.forName("resAvail.workDate").as("workDate"));
			
			cr.setProjection(projectionList1);
			
			List<Object[]> list = cr.list();
			log.info("Result Set Size LIST : " + list.size());
			
			for (Object[] row : list) {
				
				if ((Long)row[0] != null){
					resourceAttendanceSummaryDTO.setBookedCount(((Long)row[0]).intValue());
				}
				if ((Integer)row[1] != null){
					resourceAttendanceSummaryDTO.setShiftTypeId(((Integer)row[1]).intValue());
				}
				if ((Date)row[2] != null){
					resourceAttendanceSummaryDTO.setWorkDate((Date)row[2]);
				}
				log.info("Availability  bookForShift -->: " + ((Long)row[0]).intValue() + " shiftAttendance : " + (Integer)row[1]);
			}
			Criteria c = sessionFactory.getCurrentSession().createCriteria(ResourceAvailability.class, "resAvail");
			c.createAlias("resAvail.shiftTypeMaster", "shift");
			if(shiftId!=-1){
				c.add(Restrictions.eq("shift.shiftTypeId", shiftId.intValue()));
			}
			c.createAlias("resAvail.resource", "resourceid");
			c.createAlias("resourceid.resourcePool", "resourcepoolget");
			c.createAlias("resourceid.vendor","vendor");
			c.add(Restrictions.eq("resourcepoolget.resourcePoolId", resourcePoolId));
			c.add(Restrictions.eq("resAvail.workDate", workDate));
			c.add(Restrictions.eq("resAvail.isAvailable", 1));
			
			ProjectionList projectionList = Projections.projectionList();
			projectionList.add(Projections.count("resAvail.isAvailable"));
			
			c.setProjection(projectionList);
			
			List list1 = c.list();
			log.info("Result Set Size LIST1 : " + list1.size());
			
			Object row2 = list1.get(0);
			if ((Long)row2 != null){
				resourceAttendanceSummaryDTO.setAvailableCount(((Long)row2).intValue());
			}
			resourceAttendanceSummaryDTOList.add(resourceAttendanceSummaryDTO);
			
			log.debug("list successful");
		} catch (RuntimeException re) {
			log.error("list failed", re);
		}catch(Exception e){
			log.error("list failed", e);
		}
		return resourceAttendanceSummaryDTOList;
	}
	
	
	@Override
	@Transactional
	public List<ResourceAttendanceSummaryDTO> listWorkpackageDemandSummary(Integer resourcePoolId,  Date workDate, Integer shiftId) {
		log.info("listing listWorkpackageDemandSummary");
		List<ResourceAttendanceSummaryDTO> resourceAvailabilitySummaryDTOList = new ArrayList<ResourceAttendanceSummaryDTO>();
		try {
		Criteria cr = sessionFactory.getCurrentSession().createCriteria(WorkPackageDemandProjection.class, "wpdemand");
		cr.createAlias("wpdemand.workShiftMaster", "workShiftMaster");
		cr.createAlias("workShiftMaster.shiftType", "shiftType");
		if(shiftId!=-1){
			cr.add(Restrictions.eq("shiftType.shiftTypeId", shiftId.intValue()));
		}
		cr.add(Restrictions.eq("wpdemand.workDate", workDate));
		
		ProjectionList projectionList = Projections.projectionList();
		projectionList.add(Property.forName("shiftType.shiftTypeId").as("shiftTypeId"));
		projectionList.add(Property.forName("wpdemand.workDate").as("workDate"));
		projectionList.add(Projections.sum("wpdemand.resourceCount").as("resourceCount"));
		cr.setProjection(projectionList);
				
		List<Object[]> list = cr.list();
		log.info("Result Set Size - : " + list.size());
		
		ResourceAttendanceSummaryDTO resourceAttendanceSummaryDTO = null;
		for (Object[] row : list) {
			resourceAttendanceSummaryDTO = new ResourceAttendanceSummaryDTO();
			if ((Integer)row[0] != null){
				resourceAttendanceSummaryDTO.setShiftTypeId(((Integer)row[0]).intValue());
			}
			if ((Date)row[1] != null){
				resourceAttendanceSummaryDTO.setWorkDate((Date)row[1]);
			}
			if ((Long)row[2] != null){
				resourceAttendanceSummaryDTO.setDemandCount(((Long)row[2]).intValue());
			}
			resourceAvailabilitySummaryDTOList.add(resourceAttendanceSummaryDTO);
		}
		
		log.debug("list successful");
		} catch (RuntimeException re) {
			log.error("list failed", re);
		}catch(Exception e){
			log.error("list failed", e);
		}
		return resourceAvailabilitySummaryDTOList;
	}
	
	@Override
	@Transactional
	public List<ResourceAttendanceSummaryDTO> listResourceReliable(Integer resourcePoolId, Date startDate, Date endDate, Integer userId) {
		List<ResourceAttendanceSummaryDTO> resourceReliablelist = new ArrayList<ResourceAttendanceSummaryDTO>();
		if (startDate == null || endDate == null)
			return null;
		
		try {
			Criteria c = sessionFactory.getCurrentSession().createCriteria(ResourceAvailability.class, "rAvail");
			c.createAlias("rAvail.resource", "resourceid");
			c.createAlias("resourceid.resourcePool", "resourcepoolget");
			c.createAlias("resourceid.vendor","vendor");
			c.createAlias("resourceid.userRoleMaster","userRol");
			c.createAlias("rAvail.shiftTypeMaster", "shiftmaster");
			if (resourcePoolId != null){
				c.add(Restrictions.eq("resourcepoolget.resourcePoolId", resourcePoolId));
			}
			if (userId != null){
				c.add(Restrictions.eq("resourceid.userId", userId));
			}
			c.add(Restrictions.between("rAvail.workDate", startDate, endDate));
			c.add(Restrictions.eq("rAvail.isAvailable", 1));
			c.add(Restrictions.eq("rAvail.bookForShift", 1));
			
			ProjectionList projectionList = Projections.projectionList();
			projectionList.add(Property.forName("rAvail.workDate").as("WorkDate"));
			projectionList.add(Property.forName("resourceid.loginId").as("UserName"));
			projectionList.add(Property.forName("resourcepoolget.resourcePoolName").as("resourcePoolName"));
			projectionList.add(Property.forName("vendor.registeredCompanyName").as("vendorName"));
			projectionList.add(Property.forName("userRol.roleName").as("roleName"));
			projectionList.add(Projections.groupProperty("resourceid.userId"));
			c.setProjection(projectionList);
			
			List<Object[]> list = c.list();
			
			ResourceAttendanceSummaryDTO resourceAttendanceSummaryDTO = null;
			for (Object[] row : list) {
				resourceAttendanceSummaryDTO = new ResourceAttendanceSummaryDTO();
				if ((Date)row[0] != null){
					resourceAttendanceSummaryDTO.setWorkDate((Date)row[0]);
				}
				if ((String)row[1] != null){
					resourceAttendanceSummaryDTO.setUserName((String)row[1]);
				}
				if ((String)row[2] != null){
					resourceAttendanceSummaryDTO.setResourcePoolName((String)row[2]);
				}
				if ((String)row[3] != null){
					resourceAttendanceSummaryDTO.setVendorName((String)row[3]);
				}
				if ((String)row[4] != null){
					resourceAttendanceSummaryDTO.setRoleName((String)row[4]);
				}
				if ((Integer)row[5] != null){
					resourceAttendanceSummaryDTO.setUserId((Integer)row[5]);
				}
				resourceReliablelist.add(resourceAttendanceSummaryDTO);
			}
			log.debug("list successful");
		}catch (Exception e) {
			log.error("Problem getting resource attendance data", e);
		}

			return resourceReliablelist;
		}
	
	
	@Override
	@Transactional
	public ResourceAttendanceSummaryDTO listResourceReliableTotalBooking(Integer userId, Date startDate, Date endDate) {
		ResourceAttendanceSummaryDTO resourceTotalBookings = null;

		if (startDate == null || endDate == null)
			return null;
		
		try {
			Criteria c1 = sessionFactory.getCurrentSession().createCriteria(TestFactoryResourceReservation.class, "testRes");
			c1.createAlias("testRes.blockedUser", "blockUser");
			c1.createAlias("testRes.shift", "workShift");
			c1.add(Restrictions.between("testRes.reservationDate", startDate, endDate));
			c1.add(Restrictions.eq("blockUser.userId", userId));
			
			ProjectionList projectionList = Projections.projectionList();
			projectionList.add(Projections.count("blockUser.userId").as("totalBookings"));
			projectionList.add(Projections.groupProperty("blockUser.userId"));
			c1.setProjection(projectionList);
			
			List<Object[]> lists = c1.list();
			for (Object[] row : lists) {
				resourceTotalBookings = new ResourceAttendanceSummaryDTO();
				if ((Long)row[0] != null){
					resourceTotalBookings.setTotalBookings(((Long)row[0]).intValue());
				}
				if ((Integer)row[1] != null){
					resourceTotalBookings.setUserId((Integer)row[1]);
				}
			} 
			log.debug("list successful");
		} catch (RuntimeException re) {
			log.error("list failed", re);
		}catch(Exception e){
			log.error("list failed", e);
		}
			return resourceTotalBookings; 
		}


	@Override
	@Transactional
	public ResourceAvailability listShowUpandOnTime(Integer userId, Date workDate, Integer shiftTypeId) {
		ResourceAvailability resourceAvailability = null;
		if (workDate == null)
			return null;
		try {
			Criteria c2 = sessionFactory.getCurrentSession().createCriteria(ResourceAvailability.class, "ra");
			c2.createAlias("ra.resource", "resource");
			
			c2.add(Restrictions.eq("ra.workDate", workDate));
			c2.add(Restrictions.eq("ra.shiftTypeMaster.shiftTypeId", shiftTypeId));
			c2.add(Restrictions.eq("ra.isAvailable", 1));
			c2.add(Restrictions.eq("ra.bookForShift", 1));
			c2.add(Restrictions.eq("ra.shiftAttendance", 1));
			c2.add(Restrictions.isNotNull("ra.attendanceCheckInTime"));
			c2.add(Restrictions.eq("resource.userId", userId));
			
			List<ResourceAvailability> listSum  = c2.list();
			if(listSum != null  && listSum.size()>0){
				resourceAvailability=(listSum!=null && listSum.size()!=0)?(ResourceAvailability)listSum.get(0):null;
			}
			log.debug("list successful");
		} catch (RuntimeException re) {
			log.error("list failed", re);
		}catch(Exception e){
			log.error("list failed", e);
		}
		return resourceAvailability;
	}


	@Override
	@Transactional
	public List<ResourceAvailability> getResourceAvailability(int shiftTypeId, Date getAvailabilityForDate, List<Integer> resourcePoolIds) {
		log.info("listing getResourceAvailabilityForWorkPacakge - ByTester instance"+getAvailabilityForDate);
		List<ResourceAvailability> resourceAvailabilityList = null;
		try {
			Criteria c = sessionFactory.getCurrentSession().createCriteria(ResourceAvailability.class,"resAvail");
			c.createAlias("resAvail.resource", "user");
			c.add(Restrictions.eq("resAvail.workDate", getAvailabilityForDate));
			c.add(Restrictions.eq("resAvail.shiftTypeMaster.shiftTypeId", shiftTypeId));
			c.add(Restrictions.eq("resAvail.bookForShift", 1));
			if(resourcePoolIds != null && resourcePoolIds.size()>0){
				c.add(Restrictions.in("user.resourcePool.resourcePoolId", resourcePoolIds));
			}
			resourceAvailabilityList = c.list();
			
			if (!resourceAvailabilityList.isEmpty()) {
				log.info("Resource Availability : "+resourceAvailabilityList.size());
				for (ResourceAvailability resourceAvailability : resourceAvailabilityList) {
					Hibernate.initialize(resourceAvailability.getResource());
					Hibernate.initialize(resourceAvailability.getResource().getUserRoleMaster());
					Hibernate.initialize(resourceAvailability.getWorkShiftMaster());
					Hibernate.initialize(resourceAvailability.getShiftTypeMaster());
				}
			}
			log.debug("list successful");
		} catch (Exception re) {
			log.error("list failed", re);
		}
		return resourceAvailabilityList;
	}


	@Override
	@Transactional
	public List<TestFactoryResourceReservation> listResourceReliableTotalBookingSummary(Integer userId, Date startDate, Date endDate) {
		List<TestFactoryResourceReservation> testFactoryResourceReservationList = null;
		try {
				if (startDate == null || endDate == null)
					return null;
				
				Criteria crit = sessionFactory.getCurrentSession().createCriteria(TestFactoryResourceReservation.class, "testRes");
				crit.createAlias("testRes.blockedUser", "blockUser");
				crit.add(Restrictions.between("testRes.reservationDate", startDate, endDate));
				crit.add(Restrictions.eq("blockUser.userId", userId));
				
				testFactoryResourceReservationList = crit.list();
				
				if (testFactoryResourceReservationList!=null &&!testFactoryResourceReservationList.isEmpty()) {
				for (TestFactoryResourceReservation testFactoryResourceReservation : testFactoryResourceReservationList) {
					Hibernate.initialize(testFactoryResourceReservation.getBlockedUser());
					Hibernate.initialize(testFactoryResourceReservation.getBlockedUser().getUserSkills());
					Hibernate.initialize(testFactoryResourceReservation.getReservationActionUser());
					Hibernate.initialize(testFactoryResourceReservation.getShift());
					Hibernate.initialize(testFactoryResourceReservation.getWorkPackage());
					Hibernate.initialize(testFactoryResourceReservation.getWorkPackage().getProductBuild().getProductVersion().getProductMaster());
					}
				}

				log.debug("list successful");
		} catch (RuntimeException re) {
			log.error("list failed", re);
		}catch(Exception e){
			log.error("list failed", e);
		}
			return testFactoryResourceReservationList; 
		}


	@Override
	@Transactional
	public ResourceAvailability getAvailabilityAndBookingStatusOfUserByDate(Integer userId, Date reservedDate, Integer shiftTypeId) {
		if (userId == 0 || reservedDate == null )
			return null;
		ResourceAvailability resourceAvailability = null;
		try {
			Criteria c3 = sessionFactory.getCurrentSession().createCriteria(ResourceAvailability.class, "resAvaila");
			c3.createAlias("resAvaila.resource", "resource");
			c3.add(Restrictions.eq("resAvaila.workDate", reservedDate));
			c3.add(Restrictions.eq("resAvaila.shiftTypeMaster.shiftTypeId", shiftTypeId));
			c3.add(Restrictions.eq("resource.userId", userId));
			
			List<ResourceAvailability> listSum  = c3.list();
			if(listSum != null  && listSum.size()>0){
				resourceAvailability=(listSum!=null && listSum.size()!=0)?(ResourceAvailability)listSum.get(0):null;
			}
			log.debug("list successful");
		} catch (RuntimeException re) {
			log.error("list failed", re);
		}catch(Exception e){
			log.error("list failed", e);
		}
		return resourceAvailability;
	}


	
	@Override
	@Transactional
	public ResourceAvailability listResourceAVailabilityForBooking(Integer userId, Date workDate, int shiftTypeId, int availabilityStatus) {
	
		ResourceAvailability resourceAvailability = null;
		List<ResourceAvailability> resourceAvailabilityList = null;
		try {
			Criteria c = sessionFactory.getCurrentSession().createCriteria(ResourceAvailability.class,"ra");
			c.createAlias("ra.shiftTypeMaster", "shiftType");
			c.createAlias("ra.resource", "resource");
			c.add(Restrictions.eq("ra.workDate", workDate));
			c.add(Restrictions.eq("resource.userId", userId));
			if(shiftTypeId != 0){
				c.add(Restrictions.eq("shiftType.shiftTypeId", shiftTypeId));
			}
			if(availabilityStatus == 1){
				c.add(Restrictions.eq("ra.isAvailable", 1));
				c.add(Restrictions.eq("ra.bookForShift", 0));
			}else if(availabilityStatus == 2){
				c.add(Restrictions.eq("ra.isAvailable", 0));
				c.add(Restrictions.eq("ra.bookForShift", 0));
			}
			resourceAvailabilityList = c.list();
			
			if (resourceAvailabilityList != null && resourceAvailabilityList.size() > 0) {
				resourceAvailability = (ResourceAvailability)resourceAvailabilityList.get(0);
				Hibernate.initialize(resourceAvailability.getResource());
				Hibernate.initialize(resourceAvailability.getWorkShiftMaster());
				Hibernate.initialize(resourceAvailability.getShiftTypeMaster());
			}
			log.debug("list successful");
		} catch (RuntimeException re) {
			log.error("list failed", re);
		}
		return resourceAvailability;
		
	}
	
	@Override
	@Transactional
	public void updateReservedResourceAvailability(ResourceAvailability resourceAvailablity, Integer availabilityStatus) {
		log.debug("updating Resource Availabilty instance");
		try {
			
			if(availabilityStatus.equals(1)){
				resourceAvailablity.setBookForShift(new Integer(1));
			}else if (availabilityStatus.equals(2)){
				resourceAvailablity.setIsAvailable(new Integer(1));
				resourceAvailablity.setBookForShift(new Integer(1));
			}
			sessionFactory.getCurrentSession().update(resourceAvailablity);
			log.debug("update successful");
		} catch (RuntimeException re) {
			log.error("update failed", re);
		}	
		
	}


	@Override
	@Transactional
	public Long listResourcesForAVailabilityCount(Integer resourcePoolId) {
		Long count = null;
		try {
			Criteria c = sessionFactory.getCurrentSession().createCriteria(UserList.class, "user");
			c.createAlias("user.userRoleMaster", "role");
			c.createAlias("user.vendor", "vendor");
			c.add(Restrictions.eq("user.status", 1));
			c.add(Restrictions.eq("vendor.status", 1));
			c.add(Restrictions.eq("resourcePool.resourcePoolId", resourcePoolId));
			c.add(Restrictions.in("role.userRoleId", Arrays.asList(3,4,5)));
			c.setProjection(Projections.rowCount());
			count = (Long) c.uniqueResult();
			log.debug("Count successful");
		} catch (RuntimeException re) {
			log.error("Count failed", re);
		}
		return count;
	}
	
	@Override
	@Transactional
	public ResourceCountDTO getBlockedResourcesAttendanceCount(Date reservationDate, Integer testFactoryLabId, Integer resourcePoolId, Integer shiftTypeId) {
		log.info("getBlockedResourcesCount :::: reservationDate: "+reservationDate +" and shift Type Id: "+shiftTypeId);
		ResourceCountDTO resourceCountDTO = new ResourceCountDTO();
		try {
		Criteria c = sessionFactory.getCurrentSession().createCriteria(ResourceAvailability.class, "ra");
		c.createAlias("ra.shiftTypeMaster", "st");
		c.createAlias("ra.resource", "user");
		c.createAlias("user.resourcePool", "rp");
		c.add(Restrictions.eq("ra.workDate", reservationDate));
		c.add(Restrictions.eq("st.shiftTypeId", shiftTypeId));
		c.add(Restrictions.eq("rp.resourcePoolId", resourcePoolId));
		c.add(Restrictions.eq("ra.shiftAttendance", 1));
		ProjectionList projectionList = Projections.projectionList();
		projectionList.add(Projections.count("user.userId").as("attendedUserCount"));
		c.setProjection(projectionList);
		
		List<Object> list = c.list();
		log.info("Result Set Size ****: " + list.size());
		
		Object row = (list != null && list.size() != 0) ? list.get(0) : null;
		
		if(row!=null) {
			log.info("Resource Count : "+((Long)row).intValue());
			resourceCountDTO.setBlockedResourcesCount(((BigDecimal)row).floatValue());
		}
		
		} catch (Exception e) {
			log.error("listing  failed", e);
			return null;
		}
		return resourceCountDTO;
	}


	@Override
	@Transactional
	public List<ResourceAvailability> getUserAttendanceForMonth(int userId,	Date dtMonthStartWorkDate, Date dtMonthEndWorkDate) {
		List<ResourceAvailability> list = null;
		if (dtMonthStartWorkDate == null || dtMonthEndWorkDate == null)
			return null;
		
		try {
			Criteria c = sessionFactory.getCurrentSession().createCriteria(ResourceAvailability.class, "ra");
			c.createAlias("ra.resource", "resourceid");
			c.add(Restrictions.eq("resourceid.userId", userId));
			c.add(Restrictions.between("ra.workDate", dtMonthStartWorkDate,dtMonthEndWorkDate));
			c.add(Restrictions.eq("ra.shiftAttendance", 1));
			list = c.list();
		}catch (Exception e) {
			log.error("Problem getting resource attendance data", e);
		}

		return list;
	}


	@Override
	public List<ResourceAvailability> listAttendaceByDate(Integer testFactoryLabId, Integer resourcePoolId,
			Integer shiftTypeId, Date selectedDate,List<Integer> listOfResourcePoolIds) {
		List<ResourceAvailability> resourceAttendanceList = null;
		try {
			Criteria c = sessionFactory.getCurrentSession().createCriteria(ResourceAvailability.class,"ra");
			c.createAlias("ra.shiftTypeMaster", "shiftType");
			c.createAlias("ra.resource", "resource");
			c.createAlias("resource.resourcePool", "rp");

			c.add(Restrictions.eq("ra.workDate", selectedDate));
			c.add(Restrictions.eq("ra.shiftAttendance", 1));
			if(shiftTypeId != null && shiftTypeId != 0){
				c.add(Restrictions.eq("shiftType.shiftTypeId", shiftTypeId));
			}
			
			if (resourcePoolId != null) {
				if(resourcePoolId != -1){
					c.add(Restrictions.eq("rp.resourcePoolId", resourcePoolId));
				}else{
					c.add(Restrictions.in("rp.resourcePoolId", listOfResourcePoolIds));
				}
			} 
			resourceAttendanceList = c.list();
			
			if (resourceAttendanceList.size()!=0) {
				log.info("Resource Attendance : "+resourceAttendanceList.size());
				for (ResourceAvailability resourceAvailability : resourceAttendanceList) {
					log.info("Resource Attendance for : "+resourceAvailability.getResource().getLoginId());
					Hibernate.initialize(resourceAvailability.getResource());
					Hibernate.initialize(resourceAvailability.getWorkShiftMaster());
					Hibernate.initialize(resourceAvailability.getShiftTypeMaster());
					Hibernate.initialize(resourceAvailability.getResource().getUserSkills());
				}
			}
			
			log.debug("list successful");
		} catch (RuntimeException re) {
			log.error("list failed", re);
		}
		return resourceAttendanceList;
	}


	@Override
	@Transactional
	public boolean isAttendanceMarkedForUserByDate(int userId, Date dtWorkDate) {
		
		boolean isAttendanceMarked = false;
		List<ResourceAvailability> list = null;
		if (dtWorkDate == null)
			return false;
		
		try {
			Criteria c = sessionFactory.getCurrentSession().createCriteria(ResourceAvailability.class, "ra");
			c.createAlias("ra.resource", "resourceid");
			c.add(Restrictions.eq("resourceid.userId", userId));
			c.add(Restrictions.eq("ra.workDate", dtWorkDate));
			c.add(Restrictions.eq("ra.shiftAttendance", 1));
			list = c.list();
			if(list != null && list.size()>0){
				isAttendanceMarked = true;
			}
		}catch (Exception e) {
			log.error("Problem getting resource attendance data", e);
		}

		return isAttendanceMarked;
	}
	
	@Override
	@Transactional
	public List<ResourceAvailabilityDTO> getResourceAvailablityList(Date startDate, Date endDate, Integer testFactoryLabId,
			Integer testFactoryId, Integer productId, Integer workPackageId,
			Integer shiftId,String viewType) {
		log.info("listing listResourceAvailabilityDTO");
		List<ResourceAvailabilityDTO> resourceAvailabilityDTOs = new ArrayList<ResourceAvailabilityDTO>();
		
		try {
			Criteria c = sessionFactory.getCurrentSession().createCriteria(ResourceAvailability.class, "ra");


			ProjectionList projectionList = Projections.projectionList();

			if (shiftId != null &&shiftId!=-1) {
				ShiftTypeMaster shiftTypeMaster= getShiftTypeIdFromWorkShiftId(shiftId);
				
				c.createAlias("ra.shiftTypeMaster", "shift");
				c.add(Restrictions.eq("shift.shiftTypeId", shiftTypeMaster.getShiftTypeId()));
			}
			
			if(viewType != null){
				if(viewType.equalsIgnoreCase(IDPAConstants.MONTHLY_VIEW_TYPE)){
					c.add(Restrictions.ge("ra.workDate", startDate));
					c.add(Restrictions.le("ra.workDate", endDate));
				}
			}
			
			c.createAlias("ra.resource", "r");
			c.add(Restrictions.eq("ra.isAvailable", 0));
			
			projectionList.add(Property.forName("workDate"));
			projectionList.add(Projections.count("r.userId").as("resourceAvailabilityCount"));
			projectionList.add(Projections.groupProperty("ra.workDate"));
			
			c.setProjection(projectionList);
			
			List<Object[]> list = c.list();
			
			log.info("Result Set Size : " + list.size());
			
			
			ResourceAvailabilityDTO resourceAvailabilityDTO = null;
			for (Object[] row : list) {
				resourceAvailabilityDTO = new ResourceAvailabilityDTO();
				
				resourceAvailabilityDTO.setWorkDate(((Date)row[0]));
				resourceAvailabilityDTO.setResourceAvailabilityCount(((BigDecimal)row[1]).floatValue());
				resourceAvailabilityDTOs.add(resourceAvailabilityDTO);
			}
			
			log.debug("list successful");
		} catch (RuntimeException re) {
			log.error("list failed", re);
			// throw re;
		}
		return resourceAvailabilityDTOs;
	}


	@Override
	@Transactional
	public List<WeeklyResourceDemandDTO> listWorkpackageWeeklyDemandAndReservedCountProjection(Integer workPackageId,Integer workYear) {
		List <WeeklyResourceDemandDTO> weeklyResourceDemandDTOs =new ArrayList<WeeklyResourceDemandDTO>();
		try{
			
			
			String sql = "SELECT wp.workPackageId,wp.name, shift.shiftId,shift.shiftName, skill.skillId, skill.skillName, wdp.workWeek, wdp.workYear,wdp.groupDemandId,role.userRoleId,role.roleLabel,SUM(wdp.resourceCount),userType.userTypeId,userType.userTypeLabel FROM workpackage_demand_projection wdp" 
					     +" LEFT JOIN workpackage wp ON (wp.workpackageId = wdp.workpackageId)"
						 +" LEFT JOIN   skill skill ON (skill.skillId =  wdp.skillId)"
						 +" LEFT JOIN user_role_master role ON (role.userRoleId = wdp.userRoleId)"
						 +" LEFT JOIN work_shifts_master shift ON (shift.shiftId = wdp.shiftId)"
						 +" LEFT JOIN user_type_master_new userType ON (userType.userTypeId = wdp.userTypeId)"
						 
						 +"WHERE wdp.workPackageId = "+workPackageId+" AND wdp.workWeek IS NOT NULL AND wdp.workYear="+workYear+" AND wdp.workYear IS NOT NULL GROUP BY wdp.shiftId, wdp.skillId, wdp.userRoleId, wdp.workWeek, wdp.workYear,wdp.userTypeId";
			
			List <Object[]> demandList = sessionFactory.getCurrentSession().createSQLQuery(sql).list();
			
			for(Object[] row : demandList){
				
				WeeklyResourceDemandDTO wpDemandProjectionDTO = new WeeklyResourceDemandDTO();
				wpDemandProjectionDTO.setWorkPackageId((Integer)row[0]);
				wpDemandProjectionDTO.setWorkPackageName((String)row[1]);
				wpDemandProjectionDTO.setShiftId((Integer)(row[2]));
				wpDemandProjectionDTO.setShiftName((String)row[3]);
				wpDemandProjectionDTO.setSkillId((Integer)row[4]);
				wpDemandProjectionDTO.setSkillName((String)row[5]);
				wpDemandProjectionDTO.setWorkWeek((Integer)row[6]);
				wpDemandProjectionDTO.setWorkYear((Integer)row[7]);
				wpDemandProjectionDTO.setGroupDemandId(((BigDecimal)row[8]).longValue());
				wpDemandProjectionDTO.setRoleId((Integer)row[9]);
				wpDemandProjectionDTO.setRoleName((String)row[10]);
				wpDemandProjectionDTO.setResourceCount(((BigDecimal)row[11]).floatValue());
				wpDemandProjectionDTO.setUserTypeId(((Integer)row[12]));
				wpDemandProjectionDTO.setUserTypeName(((String)row[13]));
				
				weeklyResourceDemandDTOs.add(wpDemandProjectionDTO);
			}
			
			sql = "SELECT wp.workPackageId,wp.name, shift.shiftId,shift.shiftName, skill.skillId, skill.skillName, tfrr.reservationWeek, tfrr.reservationYear,tfrr.groupReservationId,role.userRoleId,role.roleLabel, SUM(tfrr.reservationPercentage),userType.userTypeId,userType.userTypeLabel  FROM test_factory_resource_reservation tfrr" 
					+" LEFT JOIN workpackage wp ON (wp.workpackageId = tfrr.workpackageId)"
					+" LEFT JOIN   skill skill ON (skill.skillId =  tfrr.skillId)"
					+" LEFT JOIN user_role_master role ON (role.userRoleId = tfrr.userRoleId)"
					+" LEFT JOIN work_shifts_master shift ON (shift.shiftId = tfrr.shiftId)"
					+" LEFT JOIN user_type_master_new userType ON (userType.userTypeId = tfrr.userTypeId)"
					+" WHERE tfrr.workPackageId = "+workPackageId+" AND tfrr.reservationWeek IS NOT NULL AND tfrr.reservationYear="+workYear+" AND tfrr.reservationYear IS NOT NULL GROUP BY tfrr.shiftId, tfrr.skillId, tfrr.userRoleId, tfrr.reservationWeek, tfrr.reservationYear, tfrr.userId,tfrr.userTypeId";		
			
			List <Object[]> reserveList = sessionFactory.getCurrentSession().createSQLQuery(sql).list();
			
			for(Object[] row : reserveList){
				Boolean isDemandFound = false;
				for(WeeklyResourceDemandDTO weeklyResourceDemandDTO : weeklyResourceDemandDTOs){
					if(weeklyResourceDemandDTO.getWorkPackageId() == (Integer)row[0] && weeklyResourceDemandDTO.getShiftId() == (Integer)(row[2]) && weeklyResourceDemandDTO.getSkillId() == (Integer)row[4] && weeklyResourceDemandDTO.getWorkWeek() == (Integer)row[6] && weeklyResourceDemandDTO.getWorkYear() == (Integer)row[7] && weeklyResourceDemandDTO.getRoleId() == (Integer)row[9]){
						isDemandFound = true;
						weeklyResourceDemandDTO.setReservedResourceCount(weeklyResourceDemandDTO.getReservedResourceCount() + ((BigDecimal)row[11]).floatValue());
						break;
					}
				}
				if(!isDemandFound){
					WeeklyResourceDemandDTO wpDemandProjectionDTO = new WeeklyResourceDemandDTO();
					wpDemandProjectionDTO.setWorkPackageId((Integer)row[0]);
					wpDemandProjectionDTO.setWorkPackageName((String)row[1]);
					wpDemandProjectionDTO.setShiftId((Integer)(row[2]));
					wpDemandProjectionDTO.setShiftName((String)row[3]);
					wpDemandProjectionDTO.setSkillId((Integer)row[4]);
					wpDemandProjectionDTO.setSkillName((String)row[5]);
					wpDemandProjectionDTO.setWorkWeek((Integer)row[6]);
					wpDemandProjectionDTO.setWorkYear((Integer)row[7]);
					wpDemandProjectionDTO.setGroupDemandId(((BigDecimal)row[8]).longValue());
					wpDemandProjectionDTO.setRoleId((Integer)row[9]);
					wpDemandProjectionDTO.setRoleName((String)row[10]);
					wpDemandProjectionDTO.setReservedResourceCount(((BigDecimal)row[11]).floatValue());
					wpDemandProjectionDTO.setUserTypeId(((Integer)row[12]));
					wpDemandProjectionDTO.setUserTypeName(((String)row[13]));
					weeklyResourceDemandDTOs.add(wpDemandProjectionDTO);
				}
			}
			
		}catch(Exception e){
			log.error("Unable to retrive ", e);
		}
		log.info("weeklyResourceDemandDTOs ===  "+weeklyResourceDemandDTOs.size());
		return weeklyResourceDemandDTOs;
	}


	@Override
	@Transactional
	public List<WeeklyResourceReservationDTO> listResourcePoolResourceReservationWeeklyProjection(Integer labId, Integer resourcePoolId, Integer testFactoryId, Integer productId, Integer workpackageId, Integer allocationYear, Integer userId, List<Integer> allocationWeekList) {// TODO Auto-generated method stub
		List<WeeklyResourceReservationDTO> weeklyResourceDemandDTOList = new ArrayList<WeeklyResourceReservationDTO>();
		try {
			Criteria c = sessionFactory.getCurrentSession().createCriteria(TestFactoryResourceReservation.class,"resourceReservation");
			c.createAlias("resourceReservation.workPackage", "wp");
			c.createAlias("resourceReservation.shift", "workShift");
			c.createAlias("wp.productBuild", "pbuild");
			c.createAlias("pbuild.productVersion", "pVersion");
			c.createAlias("pVersion.productMaster", "product");
			c.createAlias("resourceReservation.blockedUser", "blockedUser");
			c.createAlias("blockedUser.userRoleMaster", "userRole");
			c.createAlias("blockedUser.userTypeMasterNew", "userType");
			c.createAlias("resourceReservation.resourcePool", "resourcePool");
			c.createAlias("resourcePool.testFactoryLab", "testFactoryLab");
			
			c.add(Restrictions.eq("resourceReservation.reservationYear", allocationYear));
			if (allocationWeekList != null && allocationWeekList.size() > 0) {
				c.add(Restrictions.in("resourceReservation.reservationWeek", allocationWeekList));
			}
			if(resourcePoolId != null && resourcePoolId != 0){
				c.add(Restrictions.eq("resourcePool.resourcePoolId", resourcePoolId));
			}
			
			if(labId != null && labId != 0){
				c.add(Restrictions.eq("testFactoryLab.testFactoryLabId", labId));
			}
			
			if(productId != null && productId != 0){
				c.add(Restrictions.eq("product.productId", productId));
			}
			
			if(workpackageId != null && workpackageId != 0){
				c.add(Restrictions.eq("wp.workPackageId", workpackageId));
			}
			
			if(userId != null && userId != 0){
				c.add(Restrictions.eq("blockedUser.userId", userId));
			}
			
			ProjectionList projectionList = Projections.projectionList();
			projectionList = Projections.projectionList();
			
			projectionList.add(Property.forName("wp.workPackageId").as("workPackageId"));
			projectionList.add(Property.forName("wp.name").as("workPackageName"));
			projectionList.add(Property.forName("userRole.userRoleId").as("userRoleId"));
			projectionList.add(Property.forName("userRole.roleLabel").as("roleName"));
			
			projectionList.add(Property.forName("workShift.shiftId").as("shiftId"));
			projectionList.add(Property.forName("workShift.shiftName").as("shiftName"));
			
			
			projectionList.add(Property.forName("resourceReservation.reservationWeek").as("reservationWeek"));
			projectionList.add(Property.forName("resourceReservation.reservationYear").as("reservationYear"));
			projectionList.add(Projections.avg("resourceReservation.reservationPercentage").as("reservationPercentage"));
			projectionList.add(Property.forName("resourceReservation.groupReservationId").as("groupReservationId"));
			
			projectionList.add(Property.forName("blockedUser.userId").as("userId"));
			projectionList.add(Property.forName("blockedUser.loginId").as("userName"));
			
			projectionList.add(Property.forName("blockedUser.userCode").as("userCode"));
			
			projectionList.add(Property.forName("userType.userTypeId").as("userTypeId"));
			projectionList.add(Property.forName("userType.userTypeLabel").as("userTypeLabel"));
			
			projectionList.add(Property.forName("resourcePool.resourcePoolId").as("resourcePoolId"));
			projectionList.add(Property.forName("resourcePool.resourcePoolName").as("resourcePoolName"));
			
			projectionList.add(Projections.groupProperty("resourceReservation.reservationWeek"));
			projectionList.add(Projections.groupProperty("resourceReservation.groupReservationId"));
			projectionList.add(Projections.groupProperty("resourceReservation.reservationYear"));
			projectionList.add(Projections.groupProperty("blockedUser.userId"));
			projectionList.add(Projections.groupProperty("resourcePool.resourcePoolId"));
			
			projectionList.add(Projections.groupProperty("wp.workPackageId"));
			projectionList.add(Projections.groupProperty("wp.name"));
			projectionList.add(Projections.groupProperty("userRole.userRoleId"));
			projectionList.add(Projections.groupProperty("userRole.roleLabel"));
			projectionList.add(Projections.groupProperty("workShift.shiftId"));
			projectionList.add(Projections.groupProperty("workShift.shiftName"));
			projectionList.add(Projections.groupProperty("blockedUser.loginId"));
			projectionList.add(Projections.groupProperty("blockedUser.userCode"));
			projectionList.add(Projections.groupProperty("userType.userTypeId"));
			projectionList.add(Projections.groupProperty("userType.userTypeLabel"));
			projectionList.add(Projections.groupProperty("resourcePool.resourcePoolName"));
			
			c.setProjection(projectionList);

			List<Object[]> list = c.list();
			WeeklyResourceReservationDTO weeklyResourceReservationDTO = null;
				for (Object[] row : list) {
					weeklyResourceReservationDTO = new WeeklyResourceReservationDTO();
					
					weeklyResourceReservationDTO.setWorkPackageId((Integer)row[0]);
					weeklyResourceReservationDTO.setWorkPackageName((String)row[1]);
					weeklyResourceReservationDTO.setUserRoleId((Integer)row[2]);
					weeklyResourceReservationDTO.setUserRoleName((String)row[3]);
					
					weeklyResourceReservationDTO.setShiftId((Integer)row[4]);
					weeklyResourceReservationDTO.setShiftName((String)row[5]);
					
					
					weeklyResourceReservationDTO.setReservationWeek((Integer)row[6]);
					weeklyResourceReservationDTO.setReservationYear((Integer)row[7]);
					weeklyResourceReservationDTO.setReservationPercentage(((Double)row[8]).intValue());
					weeklyResourceReservationDTO.setGroupReservationId((Long)row[9]);
					
					weeklyResourceReservationDTO.setUserId((Integer)row[10]);
					weeklyResourceReservationDTO.setUserName((String)row[11]);
					weeklyResourceReservationDTO.setUserCode((String)row[12]);
					weeklyResourceReservationDTO.setUserTypeId((Integer)row[13]);
					weeklyResourceReservationDTO.setUserTypeName((String)row[14]);
					weeklyResourceReservationDTO.setResourcePoolId((Integer)row[15]);
					weeklyResourceReservationDTO.setResourcePoolName((String)row[16]);
					
					weeklyResourceDemandDTOList.add(weeklyResourceReservationDTO);
					
				}
			} catch (RuntimeException re) {
				log.error("Error in listResourcePoolResourceReservationProjection - ", re);
			}
			return weeklyResourceDemandDTOList;
		}


	@Override
	@Transactional
	public Integer getTotalReservationPercentageByUserId(Integer userId,Integer workWeek,Integer workYear) {
		
		Integer totalPercentage = 0;
		try{
			
			Criteria c = sessionFactory.getCurrentSession().createCriteria(TestFactoryResourceReservation.class,"tfrr");
			        c.createAlias("tfrr.blockedUser", "user");
			        c.add(Restrictions.eq("user.userId", userId));
			        c.add(Restrictions.eq("tfrr.reservationWeek", workWeek));
			        c.add(Restrictions.eq("tfrr.reservationYear", workYear));
			        
			        ProjectionList projectionList = Projections.projectionList();
					projectionList = Projections.projectionList();
					
					
					projectionList.add(Projections.sum("tfrr.reservationPercentage").as("total"));
					
					projectionList.add(Projections.groupProperty("user.userId"));
					projectionList.add(Projections.groupProperty("tfrr.reservationWeek"));
					projectionList.add(Projections.groupProperty("tfrr.reservationYear"));
				
					c.setProjection(projectionList);
					List<Object[]> list = c.list();
					
					for (Object percentage : list ){
						 Object[] projectionPercentage = (Object[]) percentage;
						totalPercentage = (((Long)projectionPercentage[0]).intValue());
					}
					
			
		}catch(Exception ex){
			log.error("Unable to get data ", ex);
		}
		return totalPercentage;
	}


	@Override
	@Transactional
	public List<WorkPackageDemandProjection> getWorkpackageDemandProjection(Integer workpackageId, Integer shiftId, Integer skillId, Integer userRoleId, Integer workWeek, Integer workYear,Integer userTypeId) {
		List<WorkPackageDemandProjection> workPackageDemandProjections = null;
		try{
			Criteria c = sessionFactory.getCurrentSession().createCriteria(WorkPackageDemandProjection.class,"wdp");
	        c.add(Restrictions.eq("wdp.workPackage.workPackageId", workpackageId));
	        c.add(Restrictions.eq("wdp.workShiftMaster.shiftId", shiftId));
	        c.add(Restrictions.eq("wdp.skill.skillId", skillId));
	        c.add(Restrictions.eq("wdp.userRole.userRoleId", userRoleId));
	        c.add(Restrictions.eq("wdp.workWeek", workWeek));
	        c.add(Restrictions.eq("wdp.workYear", workYear));
	        c.add(Restrictions.eq("wdp.userTypeMasterNew.userTypeId", userTypeId));
	        
	        workPackageDemandProjections = c.list();
	        	        
		}catch(Exception ex){
			log.error("Error in getWorkpackageDemandProjection - ", ex);
		}
		return workPackageDemandProjections;
	}


	@Override
	@Transactional
	public List<TestFactoryResourceReservation> getReservedResourcesWeeklyByUserIdForUpdateOrAdd(Integer workpackageId, Integer recursiveWeek, Integer workYear,Integer shiftId, Integer skillId,
			Integer reservedorUnreservedUserId, Integer userRoleId,Integer userTypeId) {
		
		List<TestFactoryResourceReservation> testFactoryResourceReservations = new ArrayList<TestFactoryResourceReservation>();
		try{
			Criteria c = sessionFactory.getCurrentSession().createCriteria(TestFactoryResourceReservation.class, "trr");
			c.createAlias("trr.workPackage", "workPackage");
			c.createAlias("trr.shift", "shift");
			c.createAlias("trr.skill", "skill");
			c.createAlias("trr.blockedUser", "blockedUser");
			c.createAlias("trr.userRole", "userRole");
			c.createAlias("trr.userType", "userType");
			
			
			c.add(Restrictions.eq("workPackage.workPackageId", workpackageId));
			c.add(Restrictions.eq("trr.reservationWeek", recursiveWeek));
			c.add(Restrictions.eq("trr.reservationYear", workYear));
			c.add(Restrictions.eq("shift.shiftId", shiftId));
			c.add(Restrictions.eq("skill.skillId", skillId));
			c.add(Restrictions.eq("blockedUser.userId", reservedorUnreservedUserId));
			c.add(Restrictions.eq("userRole.userRoleId", userRoleId));
			c.add(Restrictions.eq("userType.userTypeId", userTypeId));
			
			
			testFactoryResourceReservations = c.list();
			
			
			
		}catch(Exception ex){
			log.error("Unable to get data ", ex);
		}
		return testFactoryResourceReservations;
	}


	@Override
	@Transactional
	public List<WeeklyResourceDemandDTO> listWorkpackageWeeklyDemandForEnagementLevel(int testFactoryLabId,int testFactoryId,int productId, int workPackageId, int weekYear, List<Integer> workWeekList) {
		
		
		List<WeeklyResourceDemandDTO> weeklyResourceDemandDTOList = new ArrayList<WeeklyResourceDemandDTO>();
		try{
			
		
		Criteria c = sessionFactory.getCurrentSession().createCriteria(WorkPackageDemandProjection.class, "wpdp");
		
		c.createAlias("wpdp.workPackage", "workPackage");
		c.createAlias("workPackage.productBuild", "productbuild");
		c.createAlias("productbuild.productVersion", "productVersion");
		c.createAlias("productVersion.productMaster", "productMaster");
		c.createAlias("productMaster.testFactory", "testFactory");
		
		c.createAlias("testFactory.testFactoryLab", "testFactoryLab");
		
		c.createAlias("wpdp.skill", "skill");
		c.createAlias("wpdp.userRole", "userRole");
		c.createAlias("wpdp.workShiftMaster", "shift");
		c.createAlias("wpdp.userTypeMasterNew", "userType");
		
		if (workPackageId != 0) {
			c.add(Restrictions.eq("workPackage.workPackageId", workPackageId));
		} 
		if (productId != 0) {
			c.add(Restrictions.eq("productMaster.productId", productId));
		} 
		if (testFactoryId != 0) {
			c.add(Restrictions.eq("testFactory.testFactoryId", testFactoryId));
		} 
		if (weekYear != 0) {
			c.add(Restrictions.eq("wpdp.workYear", weekYear));
		}
		
		if (workWeekList != null && workWeekList.size() > 0) {
			c.add(Restrictions.in("wpdp.workWeek", workWeekList));
		}
		
		if(testFactoryLabId != 0){
			c.add(Restrictions.eq("testFactoryLab.testFactoryLabId", testFactoryLabId));
		}
		 ProjectionList p1=Projections.projectionList();
		 p1 = Projections.projectionList();
		
		p1.add(Projections.property("workPackage.workPackageId"));
        p1.add(Projections.property("workPackage.name"));
        p1.add(Projections.property("productMaster.productId"));
        p1.add(Projections.property("productMaster.productName"));
        p1.add(Projections.property("testFactory.testFactoryId"));
        p1.add(Projections.property("testFactory.testFactoryName"));
        p1.add(Projections.property("shift.shiftId"));	
        p1.add(Projections.property("shift.shiftName"));	
        p1.add(Projections.property("userRole.userRoleId"));	
        p1.add(Projections.property("userRole.roleLabel"));
        p1.add(Projections.property("workWeek"));	
        p1.add(Projections.property("workYear"));	
        p1.add(Projections.sum("resourceCount"));	
        p1.add(Projections.property("skill.skillId"));
        p1.add(Projections.property("skill.skillName"));	
        p1.add(Projections.property("userType.userTypeId"));	
        p1.add(Projections.property("userType.userTypeLabel"));	
        p1.add(Projections.property("wpdp.groupDemandId"));	
        
        
        if(workPackageId != 0){
			 p1.add(Projections.groupProperty("workPackage.workPackageId"));
		 }
		 
		 if (productId != 0) {
			 p1.add(Projections.groupProperty("productMaster.productId"));
		} 
		 
		if (testFactoryId != 0) {
				p1.add(Projections.groupProperty("testFactory.testFactoryId"));
		} 
			
		
        
        p1.add(Projections.groupProperty("shift.shiftId"));
		p1.add(Projections.groupProperty("userRole.userRoleId"));
		p1.add(Projections.groupProperty("skill.skillId"));
		p1.add(Projections.groupProperty("wpdp.workWeek"));
		p1.add(Projections.groupProperty("userType.userTypeId"));
		p1.add(Projections.groupProperty("productMaster.productId"));
		p1.add(Projections.groupProperty("workPackage.workPackageId"));
		p1.add(Projections.groupProperty("testFactory.testFactoryId"));
		p1.add(Projections.groupProperty("workYear"));
		p1.add(Projections.groupProperty("wpdp.groupDemandId"));
		
		

        c.setProjection(p1); 	
		
        List<Object[]> list = c.list();
        WeeklyResourceDemandDTO weeklyResourceDemandDTO = null;
			for (Object[] row : list) {
				weeklyResourceDemandDTO = new WeeklyResourceDemandDTO();

				weeklyResourceDemandDTO.setWorkPackageId((Integer)row[0]);
				weeklyResourceDemandDTO.setWorkPackageName((String)row[1]);
				weeklyResourceDemandDTO.setProductId((Integer)row[2]);
				weeklyResourceDemandDTO.setProductName((String)row[3]);
				weeklyResourceDemandDTO.setTestFactoryId((Integer)row[4]);
				weeklyResourceDemandDTO.setTestFactoryName((String)row[5]);
				weeklyResourceDemandDTO.setShiftId((Integer)row[6]);
				weeklyResourceDemandDTO.setShiftName((String)row[7]);
				weeklyResourceDemandDTO.setRoleId((Integer)row[8]);
				weeklyResourceDemandDTO.setRoleName((String)row[9]);
				weeklyResourceDemandDTO.setWorkWeek((Integer)row[10]);
				weeklyResourceDemandDTO.setWorkYear((Integer)row[11]);
				weeklyResourceDemandDTO.setResourceCount(((Double)row[12]).floatValue());
				weeklyResourceDemandDTO.setSkillId(((Integer)row[13]));
				weeklyResourceDemandDTO.setSkillName(((String)row[14]));
				weeklyResourceDemandDTO.setUserTypeId(((Integer)row[15]));
				weeklyResourceDemandDTO.setUserTypeName(((String)row[16]));
				weeklyResourceDemandDTO.setGroupDemandId((Long)row[17]);
				
				
				weeklyResourceDemandDTOList.add(weeklyResourceDemandDTO);
				
			}
			
			
		}catch(Exception ex){
			log.error("Unable to fetch ", ex);
		}
		log.info("weeklyResourceDemandDTOList "+weeklyResourceDemandDTOList.size());
		return weeklyResourceDemandDTOList;
	}


	@Override
	@Transactional
	public List<WeeklyResourceDemandDTO> listWorkpackageWeeklyReservationForEnagementLevel(int testFactoryLabId,int testFactoryId, int productId, int workPackageId, int weekYear, List<Integer> workWeekList) {
		
		
		List<WeeklyResourceDemandDTO> weeklyResourceDemandDTOList = new ArrayList<WeeklyResourceDemandDTO>();
		try{
			
		
		Criteria c = sessionFactory.getCurrentSession().createCriteria(TestFactoryResourceReservation.class, "tfrr");
		
		c.createAlias("tfrr.workPackage", "workPackage");
		c.createAlias("workPackage.productBuild", "productbuild");
		c.createAlias("productbuild.productVersion", "productVersion");
		c.createAlias("productVersion.productMaster", "productMaster");
		c.createAlias("productMaster.testFactory", "testFactory");
		c.createAlias("testFactory.testFactoryLab", "testFactoryLab");
		c.createAlias("tfrr.skill", "skill");
		c.createAlias("tfrr.userRole", "userRole");
		c.createAlias("tfrr.shift", "shift");
		c.createAlias("tfrr.userType", "userType");
		
		if (workPackageId != 0) {
			c.add(Restrictions.eq("workPackage.workPackageId", workPackageId));
		} 
		if (productId != 0) {
			c.add(Restrictions.eq("productMaster.productId", productId));
		} 
		if (testFactoryId != 0) {
			c.add(Restrictions.eq("testFactory.testFactoryId", testFactoryId));
		} 
		if (weekYear != 0) {
			c.add(Restrictions.eq("tfrr.reservationYear", weekYear));
		}
		if (workWeekList != null && workWeekList.size() > 0) {
			c.add(Restrictions.in("tfrr.reservationWeek", workWeekList));
		}
		
		 ProjectionList p1=Projections.projectionList();
		 p1 = Projections.projectionList();
		
		p1.add(Projections.property("workPackage.workPackageId"));
        p1.add(Projections.property("workPackage.name"));
        p1.add(Projections.property("productMaster.productId"));
        p1.add(Projections.property("productMaster.productName"));
        p1.add(Projections.property("testFactory.testFactoryId"));
        p1.add(Projections.property("testFactory.testFactoryName"));
        p1.add(Projections.property("shift.shiftId"));	
        p1.add(Projections.property("shift.shiftName"));	
        p1.add(Projections.property("userRole.userRoleId"));	
        p1.add(Projections.property("userRole.roleLabel"));
        p1.add(Projections.property("reservationWeek"));	
        p1.add(Projections.property("reservationYear"));	
        p1.add(Projections.sum("reservationPercentage"));	
        p1.add(Projections.property("skill.skillId"));
        p1.add(Projections.property("skill.skillName"));	
        p1.add(Projections.property("userType.userTypeId"));	
        p1.add(Projections.property("userType.userTypeLabel"));	
		p1.add(Projections.property("tfrr.groupReservationId"));   
		
        if(workPackageId != 0){
			 p1.add(Projections.groupProperty("workPackage.workPackageId"));
		 }
		 
		 if (productId != 0) {
			 p1.add(Projections.groupProperty("productMaster.productId"));
		} 
		
		 if (testFactoryId != 0) {
			 p1.add(Projections.groupProperty("testFactory.testFactoryId"));
		} 
		 
        p1.add(Projections.groupProperty("shift.shiftId"));
		p1.add(Projections.groupProperty("userRole.userRoleId"));
		p1.add(Projections.groupProperty("skill.skillId"));
		p1.add(Projections.groupProperty("tfrr.reservationWeek"));
		p1.add(Projections.groupProperty("userType.userTypeId"));
		p1.add(Projections.groupProperty("productMaster.productId"));
		p1.add(Projections.groupProperty("workPackage.workPackageId"));
		p1.add(Projections.groupProperty("testFactory.testFactoryId"));
		p1.add(Projections.groupProperty("reservationYear"));	
		p1.add(Projections.groupProperty("tfrr.groupReservationId")); 

        c.setProjection(p1); 	
		
        List<Object[]> list = c.list();
        WeeklyResourceDemandDTO weeklyResourceDemandDTO = null;
			for (Object[] row : list) {
				weeklyResourceDemandDTO = new WeeklyResourceDemandDTO();

				weeklyResourceDemandDTO.setWorkPackageId((Integer)row[0]);
				weeklyResourceDemandDTO.setWorkPackageName((String)row[1]);
				weeklyResourceDemandDTO.setProductId((Integer)row[2]);
				weeklyResourceDemandDTO.setProductName((String)row[3]);
				weeklyResourceDemandDTO.setTestFactoryId((Integer)row[4]);
				weeklyResourceDemandDTO.setTestFactoryName((String)row[5]);
				weeklyResourceDemandDTO.setShiftId((Integer)row[6]);
				weeklyResourceDemandDTO.setShiftName((String)row[7]);
				weeklyResourceDemandDTO.setRoleId((Integer)row[8]);
				weeklyResourceDemandDTO.setRoleName((String)row[9]);
				weeklyResourceDemandDTO.setWorkWeek((Integer)row[10]);
				weeklyResourceDemandDTO.setWorkYear((Integer)row[11]);
				weeklyResourceDemandDTO.setReservedResourceCount(((Long)row[12]).floatValue());
				weeklyResourceDemandDTO.setSkillId(((Integer)row[13]));
				weeklyResourceDemandDTO.setSkillName(((String)row[14]));
				weeklyResourceDemandDTO.setUserTypeId(((Integer)row[15]));
				weeklyResourceDemandDTO.setUserTypeName(((String)row[16]));
				weeklyResourceDemandDTO.setGroupDemandId((Long)row[17]);
				
				
				weeklyResourceDemandDTOList.add(weeklyResourceDemandDTO);
				
			}
			
			
		}catch(Exception ex){
			log.error("Unable to fetch ", ex);
		}
		log.info("weeklyResourceDemandDTOList "+weeklyResourceDemandDTOList.size());
		return weeklyResourceDemandDTOList;
	}


	@Override
	@Transactional
	public List<WeeklyResourceDemandDTO> listResourcePoolResourceReservationDetailedWeeklyProjection(Integer labId, Integer resourcePoolId, Integer testFactoryId, Integer productId, Integer workpackageId, Integer userId, List<Integer> workWeekList, Integer workYear) {
		
		
		List<WeeklyResourceDemandDTO> weeklyResourceDemandDTOList = new ArrayList<WeeklyResourceDemandDTO>();
		try{

			Criteria c = sessionFactory.getCurrentSession().createCriteria(TestFactoryResourceReservation.class, "tfrr");

			c.createAlias("tfrr.workPackage", "workPackage");
			c.createAlias("workPackage.productBuild", "productbuild");
			c.createAlias("productbuild.productVersion", "productVersion");
			c.createAlias("productVersion.productMaster", "productMaster");
			c.createAlias("productMaster.testFactory", "testFactory");
			c.createAlias("tfrr.skill", "skill");
			c.createAlias("tfrr.userRole", "userRole");
			c.createAlias("tfrr.shift", "shift");
			c.createAlias("tfrr.userType", "userType");
			c.createAlias("tfrr.blockedUser", "user");
			c.createAlias("tfrr.resourcePool", "resourcePool");
			

			if (labId != 0) {
				c.createAlias("testFactory.testFactoryLab", "testFactoryLab");
				c.add(Restrictions.eq("testFactoryLab.testFactoryLabId", labId));
			}
			if (resourcePoolId != 0) {
				c.add(Restrictions.eq("tfrr.resourcePool.resourcePoolId", resourcePoolId));
			}
			if (testFactoryId != 0) {
				c.add(Restrictions.eq("testFactory.testFactoryId", testFactoryId));
			} 
			if (productId != 0) {
				c.add(Restrictions.eq("productMaster.productId", productId));
			} 
			if (workpackageId != 0) {
				c.add(Restrictions.eq("workPackage.workPackageId", workpackageId));
			} 
			if(workWeekList != null && workWeekList.size() > 0){
				c.add(Restrictions.in("tfrr.reservationWeek", workWeekList));
			}
			if (workYear != 0) {
				c.add(Restrictions.eq("tfrr.reservationYear", workYear));
			}
			if(userId != 0){
				c.add(Restrictions.eq("user.userId", userId));
			}
			
			ProjectionList p1=Projections.projectionList();
			p1 = Projections.projectionList();

			p1.add(Projections.property("workPackage.workPackageId"));
			p1.add(Projections.property("workPackage.name"));
			p1.add(Projections.property("productMaster.productId"));
			p1.add(Projections.property("productMaster.productName"));
			p1.add(Projections.property("testFactory.testFactoryId"));
			p1.add(Projections.property("testFactory.testFactoryName"));
			p1.add(Projections.property("shift.shiftId"));	
			p1.add(Projections.property("shift.shiftName"));	
			p1.add(Projections.property("userRole.userRoleId"));	
			p1.add(Projections.property("userRole.roleLabel"));
			p1.add(Projections.property("reservationWeek"));	
			p1.add(Projections.property("reservationYear"));	
			p1.add(Projections.sum("reservationPercentage"));	
			p1.add(Projections.property("skill.skillId"));
			p1.add(Projections.property("skill.skillName"));	
			p1.add(Projections.property("userType.userTypeId"));	
			p1.add(Projections.property("userType.userTypeLabel"));
			p1.add(Projections.property("resourcePool.resourcePoolId"));
			p1.add(Projections.property("resourcePool.resourcePoolName"));
			p1.add(Projections.property("user.userId"));
			p1.add(Projections.property("user.loginId"));
			p1.add(Projections.property("user.userCode"));


			p1.add(Projections.groupProperty("workPackage.workPackageId"));
			p1.add(Projections.groupProperty("productMaster.productId"));
			p1.add(Projections.groupProperty("shift.shiftId"));
			p1.add(Projections.groupProperty("userRole.userRoleId"));
			p1.add(Projections.groupProperty("skill.skillId"));
			p1.add(Projections.groupProperty("tfrr.reservationWeek"));
			p1.add(Projections.groupProperty("tfrr.reservationYear"));
			p1.add(Projections.groupProperty("userType.userTypeId"));
			p1.add(Projections.groupProperty("user.userId"));
			p1.add(Projections.groupProperty("resourcePool.resourcePoolId"));
			p1.add(Projections.groupProperty("testFactory.testFactoryId"));
			

			c.setProjection(p1); 	

			List<Object[]> list = c.list();
			
			WeeklyResourceDemandDTO weeklyResourceDemandDTO = null;
			for (Object[] row : list) {
				weeklyResourceDemandDTO = new WeeklyResourceDemandDTO();

				weeklyResourceDemandDTO.setWorkPackageId((Integer)row[0]);
				weeklyResourceDemandDTO.setWorkPackageName((String)row[1]);
				weeklyResourceDemandDTO.setProductId((Integer)row[2]);
				weeklyResourceDemandDTO.setProductName((String)row[3]);
				weeklyResourceDemandDTO.setTestFactoryId((Integer)row[4]);
				weeklyResourceDemandDTO.setTestFactoryName((String)row[5]);
				weeklyResourceDemandDTO.setShiftId((Integer)row[6]);
				weeklyResourceDemandDTO.setShiftName((String)row[7]);
				weeklyResourceDemandDTO.setRoleId((Integer)row[8]);
				weeklyResourceDemandDTO.setRoleName((String)row[9]);
				weeklyResourceDemandDTO.setWorkWeek((Integer)row[10]);
				weeklyResourceDemandDTO.setWorkYear((Integer)row[11]);
				weeklyResourceDemandDTO.setReservedResourceCount(((Long)row[12]) / 5);
				weeklyResourceDemandDTO.setSkillId(((Integer)row[13]));
				weeklyResourceDemandDTO.setSkillName(((String)row[14]));
				weeklyResourceDemandDTO.setUserTypeId(((Integer)row[15]));
				weeklyResourceDemandDTO.setUserTypeName(((String)row[16]));
				weeklyResourceDemandDTO.setResourcePoolId(((Integer)row[17]));
				weeklyResourceDemandDTO.setResourcePoolName(((String)row[18]));
				weeklyResourceDemandDTO.setUserId(((Integer)row[19]));
				weeklyResourceDemandDTO.setUserName(((String)row[20]));
				weeklyResourceDemandDTO.setUserCode((String)row[21]);
				
				weeklyResourceDemandDTOList.add(weeklyResourceDemandDTO);

			}

		}catch(Exception ex){
			log.error("Error in listResourcePoolResourceReservationDetailedWeeklyProjection - ", ex);
		}
		return weeklyResourceDemandDTOList;
	}
	
	@Override
	@Transactional
	public Integer getTotalReservationPercentageByRoleAndSkill(Integer userId, Integer workPackageId, Integer workWeek,Integer workYear, Integer skillId, Integer userRoleId, Integer userTypeId) {
		
		Integer totalPercentage = 0;
		try{
			
			Criteria c = sessionFactory.getCurrentSession().createCriteria(TestFactoryResourceReservation.class,"tfrr");
			        c.createAlias("tfrr.blockedUser", "user");
			        c.add(Restrictions.eq("user.userId", userId));
			        c.add(Restrictions.eq("tfrr.workPackage.workPackageId", workPackageId));
			        c.add(Restrictions.eq("tfrr.skill.skillId", skillId));
			        c.add(Restrictions.eq("tfrr.userRole.userRoleId", userRoleId));
			        c.add(Restrictions.eq("tfrr.userType.userTypeId", userTypeId));
			        c.add(Restrictions.eq("tfrr.reservationWeek", workWeek));
			        c.add(Restrictions.eq("tfrr.reservationYear", workYear));
			        
			        ProjectionList projectionList = Projections.projectionList();
					projectionList = Projections.projectionList();
					
					
					projectionList.add(Projections.sum("tfrr.reservationPercentage").as("total"));
					
					projectionList.add(Projections.groupProperty("user.userId"));
					projectionList.add(Projections.groupProperty("tfrr.workPackage.workPackageId"));
					projectionList.add(Projections.groupProperty("tfrr.reservationWeek"));
					projectionList.add(Projections.groupProperty("tfrr.reservationYear"));
				
					c.setProjection(projectionList);
					List<Object[]> list = c.list();
					
					for (Object percentage : list ){
						 Object[] projectionPercentage = (Object[]) percentage;
						totalPercentage = (((Long)projectionPercentage[0]).intValue());
					}
					
			
		}catch(Exception ex){
			log.error("Unable to get data ", ex);
		}
		return totalPercentage;
	}
	
	@Override
	@Transactional
	public List<Object[]> getWorkpackageAndProductAndTestFactoryCombinations(){
		List<Object[]> availableCombns = null;
		try{
			String sqlQuery = "SELECT w.name,p.productName,t.testFactoryName,w.workPackageId FROM workpackage w JOIN product_master p ON p.productId = w.productId "
					+ "JOIN test_factory t ON t.testFactoryId = p.testFactoryId";
			availableCombns = sessionFactory.getCurrentSession().createSQLQuery(sqlQuery).list();			
		}catch(Exception e){
			log.error("Unable to get data ", e);
		}
		return availableCombns;
	}
	
	@Override
	@Transactional
	public Integer updateDemandProjectionByNonMatchedCombList(Integer workWeek, String wpIds) {
		Integer updatedRecordCount=0;
		try {
			if(wpIds !="" && !wpIds.isEmpty()) {
				String query = "UPDATE workpackage_demand_projection set resourceCount=0 where workWeek >="+workWeek+" and workPackageId in("+wpIds+")";
				SQLQuery sqlQuery = sessionFactory.getCurrentSession().createSQLQuery(query);
				updatedRecordCount = sqlQuery.executeUpdate();
			}
		} catch (Exception e) {
			log.error("Unable to update DemandProjection ", e);
		}
		return updatedRecordCount;
	}
	
}
