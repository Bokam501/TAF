package com.hcl.atf.taf.dao.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.Subqueries;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.hcl.atf.taf.constants.IDPAConstants;
import com.hcl.atf.taf.dao.TestFactoryResourceReservationDao;
import com.hcl.atf.taf.model.ResourceAvailability;
import com.hcl.atf.taf.model.TestFactoryCoreResource;
import com.hcl.atf.taf.model.TestFactoryProductCoreResource;
import com.hcl.atf.taf.model.TestFactoryResourceReservation;
import com.hcl.atf.taf.model.UserList;
import com.hcl.atf.taf.model.UserRoleMaster;
import com.hcl.atf.taf.model.WorkPackage;
import com.hcl.atf.taf.model.WorkPackageDemandProjection;
import com.hcl.atf.taf.model.dto.ResourceCountDTO;
import com.hcl.atf.taf.model.dto.TestFactoryResourceReservationDTO;
import com.hcl.atf.taf.model.dto.WorkPackageDemandProjectionDTO;
@Repository
public class TestFactoryResourceReservationDaoImpl implements TestFactoryResourceReservationDao{
private static final Log log = LogFactory.getLog(TestFactoryCoreResoucesDaoImpl.class);
	
	@Autowired(required=true)
    private SessionFactory sessionFactory;

	@Override
	@Transactional
	public List<TestFactoryResourceReservation> getTestFactoryResourceReservation(
			int workPackageId, int shiftId,Date tfreservationDate) {
		log.debug("listing getTestFactoryResourceReservation instance");
		List<TestFactoryResourceReservation> tfResReservationList = null;
		try {
			if(workPackageId!=0){
				if(shiftId != -1){
					tfResReservationList=	sessionFactory.getCurrentSession().createQuery("from TestFactoryResourceReservation resReservation where resReservation.workPackage.workPackageId=:workPackageId and resReservation.shift.shiftId=:shiftId and resReservation.reservationDate=:reservationDate")
							.setParameter("workPackageId", workPackageId)
							.setParameter("shiftId", shiftId)
							.setParameter("reservationDate", tfreservationDate)
							.list();
				}else{
					tfResReservationList=	sessionFactory.getCurrentSession().createQuery("from TestFactoryResourceReservation resReservation where resReservation.workPackage.workPackageId=:workPackageId and resReservation.reservationDate=:reservationDate")
							.setParameter("workPackageId", workPackageId)
							.setParameter("reservationDate", tfreservationDate)
							.list();
				}
			}else{
				tfResReservationList=	sessionFactory.getCurrentSession().createQuery("from TestFactoryResourceReservation resReservation where resReservation.reservationDate=:reservationDate and resReservation.shift.shiftId=:shiftId")
						.setParameter("reservationDate", tfreservationDate)
						.setParameter("shiftId", shiftId)
						.list();
			}
		
			if (!(tfResReservationList == null || tfResReservationList
					.isEmpty())) {
				for (TestFactoryResourceReservation tfResReservation : tfResReservationList) {
					Hibernate.initialize(tfResReservation.getBlockedUser());
					Hibernate.initialize(tfResReservation.getBlockedUser().getUserSkills());
					Hibernate.initialize(tfResReservation.getReservationActionUser());
					Hibernate.initialize(tfResReservation.getShift());
					Hibernate.initialize(tfResReservation.getWorkPackage());
					Hibernate.initialize(tfResReservation.getWorkPackage().getProductBuild().getProductVersion().getProductMaster());
				}
				log.debug("list successful"+tfResReservationList.size());
			}
			
		} catch (RuntimeException re) {
			log.error("list failed", re);
		}
		return tfResReservationList;
	}
	
	@Override
	@Transactional
	public List<WorkPackageDemandProjectionDTO> listWorkPackageDemandProjectionForResourcePlanning(
			int workPackageId, Date resourceDemandForDate) {
				log.info("inside listWorkPackageDemandProjectionForResourcePlanning in ResoureAvailabilityDAO");
				List<WorkPackageDemandProjectionDTO> workPackageDemandProjectionDTOList = new ArrayList<WorkPackageDemandProjectionDTO>();
				try {
					Criteria c = sessionFactory.getCurrentSession().createCriteria(WorkPackageDemandProjection.class, "wpdemand");
					c.createAlias("wpdemand.workPackage", "wp");
					c.createAlias("wpdemand.workShiftMaster", "ws");
					c.createAlias("ws.shiftType", "st");
					c.createAlias("wp.productBuild", "pBuild");
					c.createAlias("pBuild.productVersion", "pVersion");
					c.createAlias("pVersion.productMaster", "product");
					c.add(Restrictions.eq("wpdemand.workDate", resourceDemandForDate));
					c.add(Restrictions.eq("wp.workPackageId", workPackageId));
					
					ProjectionList projectionList = Projections.projectionList();
					projectionList.add(Property.forName("wp.workPackageId").as("workPackageId"));
					projectionList.add(Property.forName("wp.name").as("workPackageName"));
					projectionList.add(Property.forName("wpdemand.workDate").as("workDate"));
					projectionList.add(Property.forName("ws.shiftId").as("shiftId"));
					projectionList.add(Property.forName("ws.shiftName").as("shiftName"));
					projectionList.add(Property.forName("st.shiftTypeId").as("shiftTypeId"));
					projectionList.add(Property.forName("st.shiftName").as("shiftTypeName"));
					projectionList.add(Property.forName("product.productId").as("productId"));
					projectionList.add(Property.forName("product.productName").as("productName"));
					projectionList.add(Projections.sum("wpdemand.resourceCount").as("totalResourceDemandCount"));
					projectionList.add(Projections.groupProperty("ws.shiftId"));
					c.setProjection(projectionList);
					
					List<Object[]> list = c.list();
					log.info("Daily WorkPackage Demand Projection count : " + list.size());
					WorkPackageDemandProjectionDTO wpDemandProjectionDTO = null;
						for (Object[] row : list) {
							wpDemandProjectionDTO = new WorkPackageDemandProjectionDTO();
							wpDemandProjectionDTO.setWorkPackageId((Integer)row[0]);
							wpDemandProjectionDTO.setWorkPackageName((String)row[1]);
							wpDemandProjectionDTO.setWorkDate((Date)row[2]);
							wpDemandProjectionDTO.setShiftId((Integer)(row[3]));
							wpDemandProjectionDTO.setShiftName((String)(row[4]));
							wpDemandProjectionDTO.setShiftTypeId((Integer)(row[5]));
							wpDemandProjectionDTO.setShiftTypeName((String)(row[6]));
							wpDemandProjectionDTO.setProductId((Integer)(row[7]));
							wpDemandProjectionDTO.setProductName((String)(row[8]));
							wpDemandProjectionDTO.setResourceCount(((Double)row[9]).floatValue());
							workPackageDemandProjectionDTOList.add(wpDemandProjectionDTO);
							
						}					
					
					log.debug("list successful");
				} catch (RuntimeException re) {
					log.error("list failed", re);
				}
				return workPackageDemandProjectionDTOList;
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
			resourceCountDTO.setBlockedResourcesCount(((Long)row[2]).floatValue());
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
	public List<UserList> getOtherProductCoreResources(int productId, Date date, boolean isOtherProductCoreResource) {
		List<TestFactoryProductCoreResource> listOfOthersProductCoreResources = new ArrayList<TestFactoryProductCoreResource>();
		List<UserList> OtherproductCoreUsers = new ArrayList<UserList>();
		try {
			Criteria c = sessionFactory.getCurrentSession().createCriteria(TestFactoryProductCoreResource.class, "pcr");
			c.createAlias("pcr.userList", "user");
			if(isOtherProductCoreResource){
				c.add(Restrictions.ne("pcr.productMaster.productId", productId));
			}else{
				c.add(Restrictions.eq("pcr.productMaster.productId", productId));
			}
			if(date != null){
				c.add(Restrictions.le("pcr.fromDate", date));
				c.add(Restrictions.ge("pcr.toDate", date));
			}
			c.add(Restrictions.eq("user.status", 1));
			c.add(Restrictions.eq("pcr.status", 1));
			listOfOthersProductCoreResources = c.list();
			log.info("Result Set Size : " + listOfOthersProductCoreResources.size());
			for (TestFactoryProductCoreResource productCoreResource : listOfOthersProductCoreResources) {
				log.info("productCoreResource userId: "+productCoreResource.getUserList().getLoginId()+ "  User Id: "+productCoreResource.getUserList().getUserId());
				Hibernate.initialize(productCoreResource.getUserList());
				Hibernate.initialize(productCoreResource.getUserRole());
				OtherproductCoreUsers.add(productCoreResource.getUserList());
			}
		} catch (Exception e) {
			log.error("update failed", e);
			return null;
		}
		return OtherproductCoreUsers;
	}


	@Override
	@Transactional
	public List<UserList> getOtherTestFactoryCoreResources(int testFactoryId, Date date, boolean isOtherTFCoreResource) {
		List<TestFactoryCoreResource> listOfOtherTestFactoryCoreResources = new ArrayList<TestFactoryCoreResource>();
		List<UserList> OtherTfCoreResources = new ArrayList<UserList>();
		try {
			Criteria c = sessionFactory.getCurrentSession().createCriteria(TestFactoryCoreResource.class, "tfcr");
			c.createAlias("tfcr.userList", "user");
			if(isOtherTFCoreResource){
				c.add(Restrictions.ne("tfcr.testFactory.testFactoryId", testFactoryId));
			}else{
				c.add(Restrictions.eq("tfcr.testFactory.testFactoryId", testFactoryId));
			}
			if(date != null){
				c.add(Restrictions.le("tfcr.fromDate", date));
				c.add(Restrictions.ge("tfcr.toDate", date));
			}
			c.add(Restrictions.eq("user.status", 1));
			listOfOtherTestFactoryCoreResources = c.list();
			log.info("Result Set Size : " + listOfOtherTestFactoryCoreResources.size());
			for (TestFactoryCoreResource otherTfCoreResource : listOfOtherTestFactoryCoreResources) {
				Hibernate.initialize(otherTfCoreResource.getUserList());
				Hibernate.initialize(otherTfCoreResource.getUserList().getUserRoleMaster());
				OtherTfCoreResources.add(otherTfCoreResource.getUserList());
			}
		} catch (Exception e) {
			log.error("update failed", e);
			return null;
		}
		return OtherTfCoreResources;
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
	public ResourceAvailability getResourceAvailability(Integer userId, Date date, Integer shiftId) {
		
		if(userId == null || date == null || shiftId == null) {
			log.info("Required info for getting resource availability is missing");
			return null;
		}
		
		Criteria c = sessionFactory.getCurrentSession().createCriteria(ResourceAvailability.class, "resourceAvailability");
		c.createAlias("resourceAvailability.resource", "user");
		c.createAlias("resourceAvailability.workShiftMaster", "shift");
		c.add(Restrictions.eq("resourceAvailability.workDate", date));
		c.add(Restrictions.eq("user.userId", userId));
		c.add(Restrictions.eq("shift.shiftId", shiftId));
		
		List<ResourceAvailability> resourceAvailabilityList = c.list();
		for(ResourceAvailability resourceAvailability : resourceAvailabilityList) {
			Hibernate.initialize(resourceAvailability.getResource());
			Hibernate.initialize(resourceAvailability.getWorkShiftMaster());

		}
		if (resourceAvailabilityList.size() > 0)
			return (ResourceAvailability)resourceAvailabilityList.get(0);
		else 
			return null;
	}

	
	@Override
	@Transactional
	public Integer saveBlockedResource(TestFactoryResourceReservation testFactoryResourceReservation) {
		log.info("adding resource into TestFactoryResourceReservation");
		Integer isRecordAdded = 0;
		try {	
			sessionFactory.getCurrentSession().save(testFactoryResourceReservation);
			isRecordAdded = testFactoryResourceReservation.getResourceReservationId();
			log.info("add successful");
		} catch (RuntimeException re) {
			log.error("add failed", re);
		}	
		return isRecordAdded;
	}


	@Override
	@Transactional
	public void removeUnblockedResources(TestFactoryResourceReservation testFactoryResourceReservation) {
		log.info("remove resource from TestFactoryResourceReservation");
		try {	
			sessionFactory.getCurrentSession().delete(testFactoryResourceReservation);
			log.info("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
		}	catch (Exception e) {
			log.error("Exception occured delete failed", e);
		}	
	}

	@Override
	@Transactional
	public TestFactoryResourceReservation getTestFactoryResourceReservation(
			Integer workPackageId, Integer shiftId, Date blockedDate,
			Integer blockedUserId, Integer actionUserId, int filter) {
		
		if(workPackageId == null || shiftId == null || blockedDate == null ||  blockedUserId == null ||  actionUserId == null) {
			log.info("Required info for getting resource reservation is missing");
			return null;
		}
		
		Criteria c = sessionFactory.getCurrentSession().createCriteria(TestFactoryResourceReservation.class, "resourceReservation");
		c.createAlias("resourceReservation.workPackage", "wp");
		c.createAlias("resourceReservation.shift", "shift");
		c.createAlias("resourceReservation.blockedUser", "blockedUser");
		c.createAlias("resourceReservation.reservationActionUser", "actionUser");
		c.add(Restrictions.eq("resourceReservation.reservationDate", blockedDate));
		c.add(Restrictions.eq("wp.workPackageId", workPackageId));
		c.add(Restrictions.eq("shift.shiftId", shiftId));
		c.add(Restrictions.eq("blockedUser.userId", blockedUserId));
		if(filter == 1){
			c.add(Restrictions.eq("actionUser.userId", actionUserId));
		}
		
		List<TestFactoryResourceReservation> resourceReservationList = c.list();
		if (resourceReservationList.size() > 0)
			return (TestFactoryResourceReservation)resourceReservationList.get(0);
		else {
			log.info("Reservation entry does not exist for " + blockedUserId + " : for date " + blockedDate + " : for shift " + shiftId + " : for workpackage " + workPackageId);
			return null;
		}
	}

	@Override
	@Transactional
	public List<UserList> getResourcesAvailability(int shiftTypeId, Date blockResourceForDate, String availabilityTypeFilter, Integer resourcePoolId) {
		// TODO : Process based on resourcePool Ids. 
		//For now, return resources from all resource pools
		List<ResourceAvailability> listOfAvailableResources = null;
		List<UserList> listOfAvailableUsers = new ArrayList<UserList>();
		try {
			List<Integer> requiredRoleIds = new ArrayList<Integer>();
			requiredRoleIds.add(new Integer(3));
			requiredRoleIds.add(new Integer(4));
			requiredRoleIds.add(new Integer(5));
			
			log.info("shiftTypeId:: "+shiftTypeId);
			
			Criteria c = sessionFactory.getCurrentSession().createCriteria(ResourceAvailability.class, "resourceAvailability");
			c.createAlias("resourceAvailability.shiftTypeMaster", "shiftType");
			c.createAlias("resourceAvailability.resource", "user");
			c.createAlias("user.userRoleMaster", "userRole");
			c.add(Restrictions.eq("shiftType.shiftTypeId", shiftTypeId));
			c.add(Restrictions.eq("resourceAvailability.workDate", blockResourceForDate));
			c.add(Restrictions.in("userRole.userRoleId",requiredRoleIds));
			if(availabilityTypeFilter.equalsIgnoreCase("Booked")){
				c.add(Restrictions.eq("resourceAvailability.bookForShift", 1));
				if(resourcePoolId != 0){
					c.add(Restrictions.eq("user.resourcePool.resourcePoolId", resourcePoolId));
				}
			}else if(availabilityTypeFilter.equalsIgnoreCase("BookedOrAvailable")){
				c.add(Restrictions.disjunction().add(Restrictions.or(Restrictions.eq("resourceAvailability.bookForShift", 1), Restrictions.and(Restrictions.eq("resourceAvailability.isAvailable", 1),
				        Restrictions.eq("resourceAvailability.bookForShift", 0)))));
				if(resourcePoolId != 0){
					c.add(Restrictions.eq("user.resourcePool.resourcePoolId", resourcePoolId));
				}
			}
			listOfAvailableResources = c.list();
			
			log.info("Result Set Size of Available user******: " + listOfAvailableResources.size());
			for (ResourceAvailability resourceAvailability : listOfAvailableResources) {
				Hibernate.initialize(resourceAvailability.getResource());
				Hibernate.initialize(resourceAvailability.getResource().getUserSkills());
				listOfAvailableUsers.add(resourceAvailability.getResource());
			}
		} catch (Exception e) {
			log.error("Getting resource pool resources failed", e);
		}
		return listOfAvailableUsers;
	}

	@Override
	public boolean isResourceBlockedAlready(Integer workPackageId,
			Integer shiftId, Date blockedDate, Integer blockedUserId, int filter) {
		//If filter = 1, checks if the user is already reserved by another manager for the same work package
		//If filter = 0, checks if the user is already reserved by another manager for the different work package
		if(workPackageId == null || shiftId == null || blockedDate == null ||  blockedUserId == null) {
			log.info("Required info for getting resource reservation is missing");
			return false;
		}
		
		Criteria c = sessionFactory.getCurrentSession().createCriteria(TestFactoryResourceReservation.class, "resourceReservation");
		c.createAlias("resourceReservation.workPackage", "wp");
		c.createAlias("resourceReservation.shift", "shift");
		c.createAlias("resourceReservation.blockedUser", "blockedUser");
		c.createAlias("resourceReservation.reservationActionUser", "actionUser");
		c.add(Restrictions.eq("resourceReservation.reservationDate", blockedDate));
		if(filter == 1){
			c.add(Restrictions.eq("wp.workPackageId", workPackageId));
		}
		c.add(Restrictions.eq("shift.shiftId", shiftId));
		c.add(Restrictions.eq("blockedUser.userId", blockedUserId));
		
		List<TestFactoryResourceReservation> resourceReservationList = c.list();
		if (resourceReservationList.size() > 0)
			return true;
		else 
			return false;
	}

	
	
	
	
	@Override
	@Transactional
	public List<TestFactoryResourceReservation> getTestFactoryResourceReservation(
			int workPackageId, Date startDate, Date endDate) {
		log.info("inside getTestFactoryResourceReservation");
		List<TestFactoryResourceReservation> resourceReservationList = null;
		try {
			Criteria c = sessionFactory.getCurrentSession().createCriteria(TestFactoryResourceReservation.class, "tfrr");
			c.add(Restrictions.between("tfrr.reservationDate", startDate,  endDate));
			c.add(Restrictions.eq("tfrr.workPackage.workPackageId", workPackageId));
			resourceReservationList = c.list();
			if (!resourceReservationList.isEmpty()) {
				log.info("Resource Reservation : "+resourceReservationList.size());
				for (TestFactoryResourceReservation tfResReservation : resourceReservationList) {
					Hibernate.initialize(tfResReservation.getBlockedUser());
					Hibernate.initialize(tfResReservation.getReservationActionUser());
					Hibernate.initialize(tfResReservation.getShift());
					Hibernate.initialize(tfResReservation.getWorkPackage());
					Hibernate.initialize(tfResReservation.getWorkPackage().getProductBuild().getProductVersion().getProductMaster());
					Hibernate.initialize(tfResReservation.getBlockedUser().getUserSkills());
				}
			}
			
			log.debug("list successful");
		} catch (RuntimeException re) {
			log.error("list failed", re);
		}
		return resourceReservationList;
	}

	@Override
	@Transactional
	public List<TestFactoryResourceReservation> getReservedResourcesOfWorkpackage(int workPackageId, int shiftId, Date resourceDemandForDate){
		log.info("inside getListOfResourceBlockedForWorkpackage() of ResourceAvailability DAO Impl");
		log.info("workPackageId: "+workPackageId);
		log.info("shiftId: "+shiftId);
		log.info("resourceDemandForDate: "+resourceDemandForDate);
		List<TestFactoryResourceReservation> listOfTestFactoryResourceReserved = new ArrayList<TestFactoryResourceReservation>();
		try {
			Criteria c = sessionFactory.getCurrentSession().createCriteria(TestFactoryResourceReservation.class, "tfrr");
			c.createAlias("tfrr.workPackage", "wp");
			c.createAlias("tfrr.shift", "shift");
			if(workPackageId != 0 && workPackageId !=-1){
				c.add(Restrictions.eq("wp.workPackageId", workPackageId));
			}
			c.add(Restrictions.eq("shift.shiftId", shiftId));
			c.add(Restrictions.eq("tfrr.reservationDate", resourceDemandForDate));
			
			listOfTestFactoryResourceReserved = c.list();
			log.info("Result Set Size : " + listOfTestFactoryResourceReserved.size());
			for (TestFactoryResourceReservation testFactoryResourceReservation : listOfTestFactoryResourceReserved) {
				Hibernate.initialize(testFactoryResourceReservation.getBlockedUser());
			}
		} catch (Exception e) {
			log.error("update failed", e);
			return null;
		}
		return listOfTestFactoryResourceReserved;
	}
	
	@Override
	@Transactional
	public List<TestFactoryResourceReservation> listTestFactoryResourceReservationByUserIdStartDateEndDate(
			int resourceId, Date startDate, Date endDate) {
		log.info("listing listTestFactoryResourceReservationByUserIdStartDateEndDate - ByTester instance"+startDate);
		List<TestFactoryResourceReservation> testFactoryResourceReservationList = null;
		try {
			
			Criteria c = sessionFactory.getCurrentSession().createCriteria(TestFactoryResourceReservation.class,"tfrr");
			c.add(Restrictions.between("tfrr.reservationDate", startDate,  endDate));
			c.createAlias("tfrr.blockedUser", "blockedUser");
			c.add(Restrictions.eq("blockedUser.userId", resourceId));
			testFactoryResourceReservationList = c.list();
			
			
			if (!testFactoryResourceReservationList.isEmpty()) {
				log.info("TestFactoryResourceReservation : "+testFactoryResourceReservationList.size());
				for (TestFactoryResourceReservation testFactoryResourceReservation : testFactoryResourceReservationList) {
					
					Hibernate.initialize(testFactoryResourceReservation.getBlockedUser());
					Hibernate.initialize(testFactoryResourceReservation.getReservationActionUser());
					Hibernate.initialize(testFactoryResourceReservation.getShift());
					Hibernate.initialize(testFactoryResourceReservation.getWorkPackage());
					
					
				}
			}
			
			log.debug("list successful");
		} catch (RuntimeException re) {
			log.error("list failed", re);
		}
		return testFactoryResourceReservationList;
	}

	@Override
	@Transactional
	public List<ResourceCountDTO> getBlockedResourcesForReservationDate(Date reservationDate) {
		log.info("getBlockedResourcesForReservationDate :::: reservationDate: "+reservationDate);
		List<ResourceCountDTO> listOfRResourceCountDTO = new ArrayList<ResourceCountDTO>();
		try {
		Criteria c = sessionFactory.getCurrentSession().createCriteria(TestFactoryResourceReservation.class, "tfrr");
		c.createAlias("tfrr.workPackage", "wp");
		c.createAlias("tfrr.shift", "shift");
		c.createAlias("tfrr.blockedUser", "bu");
		c.createAlias("tfrr.reservationActionUser", "actionUser");
		c.add(Restrictions.eq("tfrr.reservationDate", reservationDate));
		ProjectionList projectionList = Projections.projectionList();
		projectionList = Projections.projectionList();
		projectionList.add(Property.forName("tfrr.reservationDate").as("reservationDate"));
		projectionList.add(Property.forName("shift.shiftId").as("shiftId"));
		projectionList.add(Projections.count("bu.userId").as("blockedUserCount"));
		projectionList.add(Projections.groupProperty("shift.shiftId"));
		c.setProjection(projectionList);
		
		List<Object[]> list = c.list();
		log.info("Result Set Size ****: " + list.size());
		ResourceCountDTO resourceCountDTO = null;
		for (Object[] row : list) {
			resourceCountDTO = new ResourceCountDTO();
			resourceCountDTO.setDateOfCount((Date)row[0]);
			resourceCountDTO.setShiftId(((Integer)row[1]).intValue());
			resourceCountDTO.setBlockedResourcesCount(((Long)row[2]).floatValue());
			listOfRResourceCountDTO.add(resourceCountDTO);
			log.info("Status Summary for Date : "  +(Date)row[0] +"  Shift : "+((Integer)row[1]).intValue()+"   Blocked Resource Count : " + ((Long)row[2]).intValue());
		}
		} catch (Exception e) {
			log.error("update failed", e);
			return null;
		}
		return listOfRResourceCountDTO;
	}
	
	@Override
	@Transactional
	public List<TestFactoryResourceReservation> listResourceReservationsByDates(Integer resourcePoolId, Date startDate, Date endDate, Integer shiftTypeId) {
		List<TestFactoryResourceReservation> listOfTestFactoryResourceReservations = null;

		if (startDate == null)
			return null;
		
		try {
			Criteria c1 = sessionFactory.getCurrentSession().createCriteria(TestFactoryResourceReservation.class, "testRes");
			c1.createAlias("testRes.blockedUser", "blockUser");
			c1.createAlias("testRes.shift", "workShift");
			c1.createAlias("workShift.shiftType", "shiftType");
			if(endDate == null){
				c1.add(Restrictions.eq("testRes.reservationDate", startDate));
			}else{
				c1.add(Restrictions.between("testRes.reservationDate", startDate, endDate));
			}
			if(shiftTypeId != 0){
				c1.add(Restrictions.eq("shiftType.shiftTypeId", shiftTypeId));
			}
			c1.add(Restrictions.eq("blockUser.resourcePool.resourcePoolId", resourcePoolId));
			
			listOfTestFactoryResourceReservations = c1.list();
			if(listOfTestFactoryResourceReservations != null && listOfTestFactoryResourceReservations.size()>0){
				log.info("Result Set Size : " + listOfTestFactoryResourceReservations.size());
				for (TestFactoryResourceReservation testFactoryResourceReservation : listOfTestFactoryResourceReservations) {
					Hibernate.initialize(testFactoryResourceReservation.getBlockedUser());
					Hibernate.initialize(testFactoryResourceReservation.getBlockedUser().getResourcePool());
					Hibernate.initialize(testFactoryResourceReservation.getBlockedUser().getUserRoleMaster());
					Hibernate.initialize(testFactoryResourceReservation.getBlockedUser().getVendor());
					Hibernate.initialize(testFactoryResourceReservation.getBlockedUser().getUserTypeMasterNew());
					Hibernate.initialize(testFactoryResourceReservation.getBlockedUser().getUserSkills());
				}
			}
			log.debug("list successful");
		} catch (RuntimeException re) {
			log.error("list failed", re);
		}catch(Exception e){
			log.error("ERROR  ",e);
		}
			return listOfTestFactoryResourceReservations; 
		}
	
	@Override
	@Transactional
	public ResourceCountDTO getBlockedResourcesCount(Date reservationDate, Integer testFactoryLabId, Integer resourcePoolId,Integer shiftTypeId) {
		log.info("getBlockedResourcesCount :::: reservationDate: "+reservationDate +" and shift Type Id: "+shiftTypeId);
		ResourceCountDTO resourceCountDTO = new ResourceCountDTO();
		try {
		Criteria c = sessionFactory.getCurrentSession().createCriteria(TestFactoryResourceReservation.class, "tfrr");
		c.createAlias("tfrr.workPackage", "wp");
		c.createAlias("tfrr.shift", "shift");
		c.createAlias("shift.shiftType", "st");
		c.createAlias("tfrr.blockedUser", "bu");
		c.createAlias("bu.resourcePool", "rp");
		c.createAlias("tfrr.reservationActionUser", "actionUser");
		c.add(Restrictions.eq("tfrr.reservationDate", reservationDate));
		c.add(Restrictions.eq("st.shiftTypeId", shiftTypeId));
		c.add(Restrictions.eq("rp.resourcePoolId", resourcePoolId));
		ProjectionList projectionList = Projections.projectionList();
		projectionList.add(Projections.count("bu.userId").as("blockedUserCount"));
		c.setProjection(projectionList);
		
		List<Object> list = c.list();
		log.info("Result Set Size ****: " + list.size());
		
		Object row = (list != null && list.size() != 0) ? list.get(0) : null;
		
		if(row!=null) {
			log.info("Resource Count : "+((Long)row).intValue());
			resourceCountDTO.setBlockedResourcesCount(((Long)row).floatValue());
		}
		
		} catch (Exception e) {
			log.error("listing  failed", e);
			return null;
		}
		return resourceCountDTO;
	}

	@Override
	@Transactional
	public List<TestFactoryResourceReservation> listTestFactoryResourceReservationByDate(Integer testFactoryLabId, Integer resourcePoolId, Integer shiftTypeId, Date workDate, List<Integer> listOfResourcePoolIds) {
		List<TestFactoryResourceReservation> testFactoryResourceReservationList = new ArrayList<TestFactoryResourceReservation>();
		try {
			Criteria c = sessionFactory.getCurrentSession().createCriteria(TestFactoryResourceReservation.class, "tfrr");
			c.createAlias("tfrr.workPackage", "workPackage");
			c.createAlias("workPackage.productBuild", "productBuild");
			c.createAlias("productBuild.productVersion", "productVersion");
			c.createAlias("productVersion.productMaster", "product");
			c.createAlias("product.testFactory", "testFactory");
			c.createAlias("testFactory.testFactoryLab", "testFactoryLab");
			c.createAlias("tfrr.blockedUser", "user");
			c.createAlias("user.resourcePool", "rp");
			c.createAlias("tfrr.shift", "ws");
			c.createAlias("ws.shiftType", "st");
			ProjectionList projectionList = Projections.projectionList();

			if (testFactoryLabId != null) {
				c.add(Restrictions.eq("testFactoryLab.testFactoryLabId", testFactoryLabId));
			} 
			
			if (resourcePoolId != null) {
				if(resourcePoolId != -1){
					c.add(Restrictions.eq("rp.resourcePoolId", resourcePoolId));
				}else{
					c.add(Restrictions.in("rp.resourcePoolId", listOfResourcePoolIds));
				}
			} 
			if (shiftTypeId != null) {
				c.add(Restrictions.eq("st.shiftTypeId", shiftTypeId));
			}
			c.add(Restrictions.eq("tfrr.reservationDate", workDate));

			testFactoryResourceReservationList = c.list();
			
			log.info("Result Set Size : " + testFactoryResourceReservationList.size());
			
			if (testFactoryResourceReservationList!=null &&!testFactoryResourceReservationList.isEmpty()) {
				
				for (TestFactoryResourceReservation testFactoryResourceReservation : testFactoryResourceReservationList) {
					log.info("Reserved User: "+testFactoryResourceReservation.getBlockedUser().getLoginId());
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
		}
		return testFactoryResourceReservationList;
	}
	
	
	@Override
	@Transactional
	public List<TestFactoryResourceReservation> listResourceReservationsByDatesForResourcePool(Integer resourcePoolId, Date startDate, Date endDate, Integer shiftTypeId,Integer jtStartIndex, Integer jtPageSize) {
		List<TestFactoryResourceReservation> listOfTestFactoryResourceReservations = null;

		if (startDate == null)
			return null;
		
		try {
			Criteria c1 = sessionFactory.getCurrentSession().createCriteria(TestFactoryResourceReservation.class, "testRes");
			c1.createAlias("testRes.blockedUser", "blockUser");
			c1.createAlias("testRes.shift", "workShift");
			c1.createAlias("workShift.shiftType", "shiftType");
			if(endDate == null){
				c1.add(Restrictions.eq("testRes.reservationDate", startDate));
			}else{
				c1.add(Restrictions.between("testRes.reservationDate", startDate, endDate));
			}
			if(shiftTypeId != 0){
				c1.add(Restrictions.eq("shiftType.shiftTypeId", shiftTypeId));
			}
			c1.add(Restrictions.eq("blockUser.resourcePool.resourcePoolId", resourcePoolId));
			if(jtStartIndex != null && jtPageSize != null)
				c1.setFirstResult(jtStartIndex).setMaxResults(jtPageSize);
			
			listOfTestFactoryResourceReservations = c1.list();
			if(listOfTestFactoryResourceReservations != null && listOfTestFactoryResourceReservations.size()>0){
				log.info("Result Set Size : " + listOfTestFactoryResourceReservations.size());
				for (TestFactoryResourceReservation testFactoryResourceReservation : listOfTestFactoryResourceReservations) {
					Hibernate.initialize(testFactoryResourceReservation.getBlockedUser());
					Hibernate.initialize(testFactoryResourceReservation.getBlockedUser().getResourcePool());
					Hibernate.initialize(testFactoryResourceReservation.getBlockedUser().getUserRoleMaster());
					Hibernate.initialize(testFactoryResourceReservation.getBlockedUser().getVendor());
					Hibernate.initialize(testFactoryResourceReservation.getBlockedUser().getUserTypeMasterNew());
					Hibernate.initialize(testFactoryResourceReservation.getBlockedUser().getUserSkills());
				}
			}
			log.debug("list successful");
		} catch (RuntimeException re) {
			log.error("list failed", re);
		}catch(Exception e){
			log.error("ERROR  ",e);
		}
			return listOfTestFactoryResourceReservations; 
		}
	
	@Override
	@Transactional
	public List<UserList> getResourcesResourcesOfSameWpAndDateInDifferentShift(int shiftId, Date blockResourceForDate) {
		log.info("inside getListOfResourceBlockedForWorkpackage() of ResourceAvailability DAO Impl");
		log.info("shiftId: "+shiftId);
		log.info("resourceDemandForDate: "+blockResourceForDate);
		List<TestFactoryResourceReservation> listOfTestFactoryResourceReserved = new ArrayList<TestFactoryResourceReservation>();
		List<UserList> listOfBlockedUsers = new ArrayList<UserList>();
		try {
			Criteria c = sessionFactory.getCurrentSession().createCriteria(TestFactoryResourceReservation.class, "tfrr");
			c.createAlias("tfrr.shift", "shift");
			c.createAlias("tfrr.blockedUser", "bu");
			c.add(Restrictions.ne("shift.shiftId", shiftId));
			c.add(Restrictions.eq("tfrr.reservationDate", blockResourceForDate));
			
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
	public List<TestFactoryResourceReservation> getTestFactoryResourceReservationByDateShiftAndUser(int userId, int shiftId,Date tfreservationDate) {
		log.debug("listing getTestFactoryResourceReservation instance");
		List<TestFactoryResourceReservation> tfResReservationList = null;
		try {
			Criteria c = sessionFactory.getCurrentSession().createCriteria(TestFactoryResourceReservation.class, "tfrr");
			c.createAlias("tfrr.shift", "shift");
			c.createAlias("tfrr.blockedUser", "bu");
			c.add(Restrictions.eq("shift.shiftId", shiftId));
			if(tfreservationDate != null){
				c.add(Restrictions.eq("tfrr.reservationDate", tfreservationDate));
			}			
			c.add(Restrictions.eq("bu.userId", userId));
			tfResReservationList=c.list();
			if (!(tfResReservationList == null || tfResReservationList
					.isEmpty())) {
				for (TestFactoryResourceReservation tfResReservation : tfResReservationList) {
					Hibernate.initialize(tfResReservation.getBlockedUser());
					Hibernate.initialize(tfResReservation.getBlockedUser().getUserSkills());
					Hibernate.initialize(tfResReservation.getReservationActionUser());
					Hibernate.initialize(tfResReservation.getShift());
					Hibernate.initialize(tfResReservation.getWorkPackage());
					Hibernate.initialize(tfResReservation.getWorkPackage().getProductBuild().getProductVersion().getProductMaster());
				}
				log.debug("list successful"+tfResReservationList.size());
			}
			
		} catch (RuntimeException re) {
			log.error("list failed", re);
		}
		return tfResReservationList;
	}
	
	@Override
	@Transactional
	public List<WorkPackage> getWorkPackageFromTestFactoryResourceReservationByserId(int userId, int shiftId, Date tfreservationDate) {
		log.debug("listing getWorkPackageFromTestFactoryResourceReservationByserId instance");
		List<TestFactoryResourceReservation> tfResReservationList = null;
		List<WorkPackage> workPackageList = new ArrayList<WorkPackage>();
		WorkPackage wkpkg = new WorkPackage();
		try {
			Criteria c = sessionFactory.getCurrentSession().createCriteria(TestFactoryResourceReservation.class, "tfrr");
			c.createAlias("tfrr.blockedUser", "bu");
			c.add(Restrictions.eq("tfrr.reservationDate", tfreservationDate));
			c.add(Restrictions.eq("bu.userId", userId));
			tfResReservationList=c.list();
			if (!(tfResReservationList == null || tfResReservationList
					.isEmpty())) {
				for (TestFactoryResourceReservation tfResReservation : tfResReservationList) {
					Hibernate.initialize(tfResReservation.getBlockedUser());
					Hibernate.initialize(tfResReservation.getBlockedUser().getUserSkills());
					Hibernate.initialize(tfResReservation.getReservationActionUser());
					Hibernate.initialize(tfResReservation.getShift());
					Hibernate.initialize(tfResReservation.getWorkPackage());
					wkpkg = tfResReservation.getWorkPackage();
					if(wkpkg != null){
						Hibernate.initialize(wkpkg);						
						Hibernate.initialize(wkpkg.getProductBuild().getProductVersion().getProductMaster());
						Hibernate.initialize(wkpkg.getWorkFlowEvent().getWorkFlow());
						workPackageList.add(wkpkg);
					}					
				}
				log.debug("WorkPackage list size : "+workPackageList.size());
			}
			
		} catch (RuntimeException re) {
			log.error("list failed", re);
		}
		return workPackageList;
	}
	
	@Override
	@Transactional
	public UserRoleMaster getProductCoreResourcesProductRole(int userId, int productId, Date date) {
		List<TestFactoryProductCoreResource> listOfProductCoreResources = new ArrayList<TestFactoryProductCoreResource>();
		UserRoleMaster productUserRole = null;
		try {
			Criteria c = sessionFactory.getCurrentSession().createCriteria(TestFactoryProductCoreResource.class, "pcr");
			c.createAlias("pcr.userList", "user");
			c.createAlias("pcr.productMaster", "product");
			c.add(Restrictions.eq("user.userId", userId));
			c.add(Restrictions.eq("product.productId", productId));
			if(date != null){
				c.add(Restrictions.le("pcr.fromDate", date));
				c.add(Restrictions.ge("pcr.toDate", date));
			}
			c.add(Restrictions.eq("user.status", 1));
			listOfProductCoreResources = c.list();
			log.info("Result Set Size : " + listOfProductCoreResources.size());
			if (listOfProductCoreResources != null && listOfProductCoreResources.size() > 0)
				return productUserRole = (UserRoleMaster)listOfProductCoreResources.get(0).getUserRole();
		} catch (Exception e) {
			log.error("update failed", e);
			return null;
		}
		return productUserRole;
	}

	@Override
	@Transactional
	public List<TestFactoryResourceReservationDTO> getTestFactoryResourceReservationList(
			Date startDate, Date endDate, Integer testFactoryLabId,
			Integer testFactoryId, Integer productId, Integer workPackageId,
			Integer shiftId, String viewType) {

		List<TestFactoryResourceReservationDTO> testFactoryResourceReservationDTOList = new ArrayList<TestFactoryResourceReservationDTO>();
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
			
			if(viewType != null){
				if(viewType.equalsIgnoreCase(IDPAConstants.MONTHLY_VIEW_TYPE)){
					c.add(Restrictions.ge("tfrr.reservationDate", startDate));
					c.add(Restrictions.le("tfrr.reservationDate", endDate));
				}
			}
			
			c.createAlias("tfrr.blockedUser", "b");
			
			projectionList.add(Property.forName("reservationDate"));
			projectionList.add(Projections.count("b.userId").as("blockedCount"));
			projectionList.add(Projections.groupProperty("tfrr.reservationDate"));
			
			c.setProjection(projectionList);
			
			List<Object[]> list = c.list();
			
			log.info("Result Set Size : " + list.size());
			
			TestFactoryResourceReservationDTO testFactoryResourceReservationDTO = null;
			for (Object[] row : list) {
				testFactoryResourceReservationDTO = new TestFactoryResourceReservationDTO();
				
				testFactoryResourceReservationDTO.setWorkDate(((Date)row[0]));
				testFactoryResourceReservationDTO.setBlockedCount(((Long)row[1]).floatValue());
				testFactoryResourceReservationDTOList.add(testFactoryResourceReservationDTO);
			}
			log.debug("list successful");
		} catch (RuntimeException re) {
			log.error("list failed", re);
		}
		
		return testFactoryResourceReservationDTOList;
	}

	@Override
	@Transactional
	public List<UserList> getBlockedResourcesWeeklyByRoleAndSkill(Integer workpackageId, Integer workWeek, Integer workYear, Integer shiftId,Integer userRoleId, Integer skillId,Integer userTypeId) {
		List<UserList> listOfBlockedUsers = new ArrayList<UserList>();
		try {
			
			Criteria c = sessionFactory.getCurrentSession().createCriteria(UserList.class, "user");
			c.add(Restrictions.disjunction().add(Subqueries.propertyIn("user.userId", DetachedCriteria.forClass(TestFactoryResourceReservation.class, "tfrr")
				.add(Restrictions.eq("tfrr.shift.shiftId", shiftId))
				.add(Restrictions.eq("tfrr.reservationWeek", workWeek))
				.add(Restrictions.eq("tfrr.reservationYear", workYear))
				.add(Restrictions.eq("tfrr.userRole.userRoleId", userRoleId))
				.add(Restrictions.eq("tfrr.skill.skillId", skillId))
				.add(Restrictions.eq("tfrr.workPackage.workPackageId", workpackageId))
				.add(Restrictions.eq("tfrr.userType.userTypeId", userTypeId))
				.setProjection(Projections.property("tfrr.blockedUser.userId"))		
			)));
			c.add(Restrictions.eq("user.status", 1));
			
			listOfBlockedUsers = c.list();
			log.info("Result Set Size : " + listOfBlockedUsers.size());
			
		} catch (Exception e) {
			log.error("update failed", e);
			return null;
		}
		return listOfBlockedUsers;
	}

	@Override
	@Transactional
	public List<TestFactoryResourceReservation> getReservedResourcesWeeklyByRoleAndSkill(Integer workpackageId, Integer workWeek, Integer workYear, Integer shiftId, Integer userRoleId, Integer skillId,Integer userTypeId) {
		
		List<TestFactoryResourceReservation> listOfTestFactoryResourceReserved = new ArrayList<TestFactoryResourceReservation>();
		try {
			Criteria c = sessionFactory.getCurrentSession().createCriteria(TestFactoryResourceReservation.class, "tfrr");
			c.createAlias("tfrr.shift", "shift");
			c.createAlias("tfrr.blockedUser", "bu");
			c.createAlias("tfrr.userRole", "userRole");
			c.createAlias("tfrr.skill", "skill");
			c.createAlias("tfrr.userType", "userType");
			
			
			c.add(Restrictions.eq("shift.shiftId", shiftId));
			c.add(Restrictions.eq("tfrr.reservationWeek", workWeek));
			c.add(Restrictions.eq("tfrr.reservationYear", workYear));
			c.add(Restrictions.eq("userRole.userRoleId", userRoleId));
			c.add(Restrictions.eq("skill.skillId", skillId));
			c.add(Restrictions.eq("tfrr.workPackage.workPackageId", workpackageId));
			c.add(Restrictions.eq("userType.userTypeId", userTypeId));
			c.add(Restrictions.eq("bu.status", 1));
			
			listOfTestFactoryResourceReserved = c.list();
			
		} catch (Exception e) {
			log.error("update failed", e);
			return null;
		}
		return listOfTestFactoryResourceReserved;
	}

	@Override
	public List<UserList> getListOfResourceBlockedForWorkpackageWeekly(Integer workpackageId, Integer shiftId, Integer workWeek,Integer workYear) {
		
		List<TestFactoryResourceReservation> listOfTestFactoryResourceReserved = new ArrayList<TestFactoryResourceReservation>();
		List<UserList> listOfBlockedUsers = new ArrayList<UserList>();
		try {
			Criteria c = sessionFactory.getCurrentSession().createCriteria(UserList.class, "user");
			c.add(Restrictions.disjunction().add(Subqueries.propertyIn("user.userId", DetachedCriteria.forClass(TestFactoryResourceReservation.class, "tfrr")
				.add(Restrictions.eq("tfrr.shift.shiftId", shiftId))
				.add(Restrictions.eq("tfrr.reservationWeek", workWeek))
				.add(Restrictions.eq("tfrr.reservationYear", workYear))
				.add(Restrictions.eq("tfrr.workPackage.workPackageId", workpackageId))
				.setProjection(Projections.property("tfrr.blockedUser.userId"))		
			)));
			c.add(Restrictions.eq("user.status", 1));
			listOfBlockedUsers = c.list();
		} catch (Exception e) {
			log.error("update failed", e);
			return null;
		}
		return listOfBlockedUsers;
	}

	@Override
	@Transactional
	public boolean isResourceBlockedAlreadyForWeekly(Integer workpackageId,
			Integer shiftId, Integer workWeek, Integer workYear,
			Integer userId, int filter) {
		//If filter = 1, checks if the user is already reserved by another manager for the same work package
		//If filter = 0, checks if the user is already reserved by another manager for the different work package
		if(workpackageId == null || shiftId == null || workWeek == null ||  userId == null) {
			log.info("Required info for getting resource reservation is missing");
			return false;
		}
		
		Criteria c = sessionFactory.getCurrentSession().createCriteria(TestFactoryResourceReservation.class, "resourceReservation");
		c.createAlias("resourceReservation.workPackage", "wp");
		c.createAlias("resourceReservation.shift", "shift");
		c.createAlias("resourceReservation.blockedUser", "blockedUser");
		c.createAlias("resourceReservation.reservationActionUser", "actionUser");
		c.add(Restrictions.eq("resourceReservation.reservationWeek", workWeek));
		c.add(Restrictions.eq("resourceReservation.reservationYear", workYear));
		if(filter == 1){
			c.add(Restrictions.eq("wp.workPackageId", workpackageId));
		}
		c.add(Restrictions.eq("shift.shiftId", shiftId));
		c.add(Restrictions.eq("blockedUser.userId", userId));
		
		List<TestFactoryResourceReservation> resourceReservationList = c.list();
		if (resourceReservationList.size() > 0)
			return true;
		else 
			return false;
	}

	@Override
	@Transactional
	public List<TestFactoryResourceReservation> getTestFactoryResourceReservationByUsingWeek(Integer workpackageId, Integer shiftId, Integer workWeek,
			Integer workYear, Integer blockedUserId, Integer loggedInUserId, int filter,Long groupDemandId,Integer userTypeId) {
		
		if(workpackageId == null || shiftId == null || workWeek == null ||  blockedUserId == null ||  loggedInUserId == null) {
			log.info("Required info for getting resource reservation is missing");
			return null;
		}
		
		Criteria c = sessionFactory.getCurrentSession().createCriteria(TestFactoryResourceReservation.class, "resourceReservation");
		c.createAlias("resourceReservation.workPackage", "wp");
		c.createAlias("resourceReservation.shift", "shift");
		c.createAlias("resourceReservation.blockedUser", "blockedUser");
		c.createAlias("resourceReservation.userType", "userType");
		c.createAlias("resourceReservation.reservationActionUser", "actionUser");
		
		c.add(Restrictions.eq("resourceReservation.reservationWeek", workWeek));
		c.add(Restrictions.eq("resourceReservation.reservationYear", workYear));
		
		c.add(Restrictions.eq("wp.workPackageId", workpackageId));
		c.add(Restrictions.eq("shift.shiftId", shiftId));
		c.add(Restrictions.eq("blockedUser.userId", blockedUserId));
		
		c.add(Restrictions.eq("userType.userTypeId", userTypeId));
		
		if(filter == 1){
			c.add(Restrictions.eq("actionUser.userId", loggedInUserId));
		}
		
		List<TestFactoryResourceReservation> resourceReservationList = c.list();
		
		if (resourceReservationList.size() > 0)
			return resourceReservationList;
		else {
			return null;
		}
	}

	@Override
	@Transactional
	public void updateReservedResourcePercentage(Integer workpackageId,Integer recursiveWeek, Integer workYear, Integer reserveShiftId,Integer reserveSkillId, Integer reservationPercentage,Integer userId, Integer reserveUserRoleId,Integer userTypeId) {
		try{
			String sql = "UPDATE test_factory_resource_reservation SET reservationPercentage ="+reservationPercentage + " WHERE workPackageId = "+workpackageId+" AND reservationWeek = "+recursiveWeek+" AND reservationYear = "+workYear+" AND shiftId = "+reserveShiftId+" AND skillId = "+reserveSkillId+" AND userId = "+userId+" AND userRoleId = "+reserveUserRoleId +" AND userTypeId = "+userTypeId ;
			sessionFactory.getCurrentSession().createSQLQuery(sql).executeUpdate();
			
		}catch(Exception ex){
			log.error("Unable to update ", ex);
		}
	}

}
