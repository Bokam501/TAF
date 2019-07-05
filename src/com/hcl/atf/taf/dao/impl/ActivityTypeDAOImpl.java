package com.hcl.atf.taf.dao.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.hcl.atf.taf.dao.ActivityTypeDAO;
import com.hcl.atf.taf.model.ActivityMaster;
import com.hcl.atf.taf.model.ActivityType;
import com.hcl.atf.taf.model.ExecutionTypeMaster;
import com.hcl.ilcm.workflow.model.WorkflowMasterEntityMapping;



@Repository
public class ActivityTypeDAOImpl implements ActivityTypeDAO  {
	private static final Log log = LogFactory.getLog(ActivityTypeDAOImpl.class);

	@Autowired(required = true)
	private SessionFactory sessionFactory;
	
	@Override
	@Transactional
	public ActivityMaster getActivityMasterById(Integer activityMasterId) {
		ActivityMaster activityMaster = null;
		try {
			Criteria c = sessionFactory.getCurrentSession().createCriteria(ActivityMaster.class, "activityType");
			c.add(Restrictions.eq("activityType.activityMasterId", activityMasterId));
			List<ActivityMaster> activityTypeLists = c.list();
			if(activityTypeLists != null && activityTypeLists.size() > 0){
				activityMaster = activityTypeLists.get(0);
			}
			log.debug("List ActivityTypes successful");
		} catch (RuntimeException re) {
			log.error("List ActivityTypes failed", re);
		}
		return activityMaster;
	}

	@Override
	@Transactional
	public List<ActivityMaster> listActivityTypes(Integer testFactoryId, Integer productId, Integer jtStartIndex, Integer jtPageSize, int initializationLevel, Boolean isConsildated) {
		List<ActivityMaster> activityTypeLists = null;
		try {
			Criteria c = sessionFactory.getCurrentSession().createCriteria(ActivityMaster.class, "activityType");
			if(testFactoryId != null && testFactoryId > 0 && productId != null && productId > 0){
				if(isConsildated){
					c.add(Restrictions.disjunction().add(
							Restrictions.or(
									Restrictions.conjunction()
										.add(Restrictions.eq("activityType.testFactory.testFactoryId", testFactoryId)).add(Restrictions.isNull("activityType.productMaster.productId")), 
									Restrictions.eq("activityType.productMaster.productId", productId)
					)));					
				}else{
					c.add(Restrictions.eq("activityType.testFactory.testFactoryId", testFactoryId));
					c.add(Restrictions.eq("activityType.productMaster.productId", productId));
				}
			}else if(testFactoryId != null && testFactoryId > 0){
				c.add(Restrictions.eq("activityType.testFactory.testFactoryId", testFactoryId));
				c.add(Restrictions.isNull("activityType.productMaster.productId"));
			}else if(productId != null && productId > 0){
				c.add(Restrictions.eq("activityType.productMaster.productId", productId));
			}else{
				c.add(Restrictions.isNull("activityType.testFactory.testFactoryId"));
				c.add(Restrictions.isNull("activityType.productMaster.productId"));
			}
			activityTypeLists = c.list();
			
			log.debug("List ActivityTypes successful");
		} catch (RuntimeException re) {
			log.error("List ActivityTypes failed", re);
		}
		return activityTypeLists;
	}

	@Override
	@Transactional
	public List<ActivityType> listActivityTypes(
			int entityStatusActive, Integer jtStartIndex, Integer jtPageSize,Integer initializationLevel) {
		log.info("Activity Type listing");
		List<ActivityType> activityTypeList = null;
		Criteria c = sessionFactory.getCurrentSession().createCriteria(ActivityType.class, "atm");
		activityTypeList = c.list();
		if(initializationLevel==1)
		{
			if(activityTypeList!=null && activityTypeList.size()>0)
			{
				for (ActivityType activityType : activityTypeList) {
					Hibernate.initialize(activityType.getActivityGroup());
				}
			}
		}
		if(jtStartIndex != null && jtPageSize != null)
		c.setFirstResult(jtStartIndex).setMaxResults(jtPageSize);
		return activityTypeList;
	}

	@Override
	@Transactional
	public void addActivityMaster(ActivityMaster activityMaster) {
		log.info("adding activity master instance");
		try {	
			sessionFactory.getCurrentSession().save(activityMaster);
			log.info("add activity master successful");
		} catch (RuntimeException re) {
			log.error("add Activity Master failed", re);
		}	
	}

	@Override
	@Transactional
	public void updateActivityMaster(ActivityMaster activityMaster) {
		log.debug("updating WorkRequest instance");
		try {
			sessionFactory.getCurrentSession().saveOrUpdate(activityMaster);
			log.debug("update ActivityWorkPackage successful");
		} catch (RuntimeException re) {
			log.error("update ActivityWorkPackage failed", re);
			//throw re;
		}
	}

	@Override
	@Transactional
	public List<ActivityType> listActivityTypesByActivityGroupId(
			int entityStatusActive, int activityGroupId, Integer jtStartIndex,
			Integer jtPageSize, Integer initializationLevel) {
		log.info("Activity Type listing");
		List<ActivityType> activityTypeList = null;
		Criteria c = sessionFactory.getCurrentSession().createCriteria(ActivityType.class, "activityType");
		c.add(Restrictions.eq("activityType.activityGroup.activityGroupId", activityGroupId));
		if(jtStartIndex != null && jtPageSize != null)
			c.setFirstResult(jtStartIndex).setMaxResults(jtPageSize);
		activityTypeList = c.list();
		if(initializationLevel==1)
		{
			if(activityTypeList!=null && activityTypeList.size()>0)
			{
				for (ActivityType activityType : activityTypeList) {
					Hibernate.initialize(activityType.getActivityGroup());
				}
			}
		}
		return activityTypeList;
	}

	@Override
	@Transactional
	public ActivityMaster getActivityMasterByName(String activityMasterName) {
		List<ActivityMaster> activityMasterList = null;		
		ActivityMaster activityMaster = null;
		try {
			Criteria c = sessionFactory.getCurrentSession().createCriteria(ActivityMaster.class, "am");		
			c.add(Restrictions.eq("am.activityMasterName", activityMasterName).ignoreCase());
			activityMasterList = c.list();
			
			activityMaster = (activityMasterList!=null && activityMasterList.size()!=0)?(ActivityMaster)activityMasterList.get(0):null;
		} catch (HibernateException e) {
			log.error("ERROR  ",e);
		}		
		return activityMaster;
	}

	@Override
	@Transactional
	public ExecutionTypeMaster getCategoryByName(String categoryName) {
		List<ExecutionTypeMaster> executionTypeMasterList = null;		
		ExecutionTypeMaster executionTypeMaster = null;
		try {
			Criteria c = sessionFactory.getCurrentSession().createCriteria(ExecutionTypeMaster.class, "etm");		
			c.add(Restrictions.eq("etm.name", categoryName).ignoreCase());
			c.add(Restrictions.eq("etm.entitymaster.entitymasterid", 1));
			executionTypeMasterList = c.list();
			
			executionTypeMaster = (executionTypeMasterList!=null && executionTypeMasterList.size()!=0)?(ExecutionTypeMaster)executionTypeMasterList.get(0):null;
		} catch (HibernateException e) {
			log.error("ERROR  ",e);
		}		
		return executionTypeMaster;
	}
	
	@Override
	@Transactional
	public ActivityMaster getActivityMasterByNameAndProductId(String activityMasterName,Integer productId) {
		List<ActivityMaster> activityMasterList = null;		
		ActivityMaster activityMaster = null;
		try {
			Criteria c = sessionFactory.getCurrentSession().createCriteria(ActivityMaster.class, "am");		
			c.add(Restrictions.eq("am.activityMasterName", activityMasterName).ignoreCase());
			c.add(Restrictions.eq("am.productMaster.productId", productId));
			activityMasterList = c.list();
			
			activityMaster = (activityMasterList!=null && activityMasterList.size()!=0)?(ActivityMaster)activityMasterList.get(0):null;
		} catch (HibernateException e) {
			log.error("ERROR  ",e);
		}		
		return activityMaster;
	}
	
	@Override
	@Transactional
	public ActivityMaster uploadActivityMaster(ActivityMaster activityMaster) {
		log.info("upload activity master instance");
		try {	
			sessionFactory.getCurrentSession().save(activityMaster);
			log.info("upload activity master successful");
			return activityMaster;
		} catch (RuntimeException re) {
			log.error("add Activity Master failed", re);
			//throw re;
		}
		return null;
	}

	@Override
	public Boolean isActivityMasterAvailable(String activityTypeName, Integer referenceActivityMasterId, Integer testFactoryId, Integer productId) {
		Boolean isAlreadyExsist = false;
		try{
			Criteria c = sessionFactory.getCurrentSession().createCriteria(ActivityMaster.class, "am");		
			c.add(Restrictions.eq("am.activityMasterName", activityTypeName));
			if(testFactoryId != null && testFactoryId > 0 && productId != null && productId > 0){
				c.add(Restrictions.disjunction().add(
						Restrictions.or(
								Restrictions.conjunction()
									.add(Restrictions.eq("am.testFactory.testFactoryId", testFactoryId)).add(Restrictions.isNull("am.productMaster.productId")), 
								Restrictions.eq("am.productMaster.productId", productId)
				)));
			}else if(testFactoryId != null && testFactoryId > 0){
				c.add(Restrictions.eq("am.testFactory.testFactoryId", testFactoryId));
			}else if(productId != null && productId > 0){
				c.add(Restrictions.eq("am.productMaster.productId", productId));
			}else{
				c.add(Restrictions.isNull("am.testFactory.testFactoryId"));
				c.add(Restrictions.isNull("am.productMaster.productId"));
			}
			
			if(referenceActivityMasterId != null){
				c.add(Restrictions.ne("am.activityMasterId", referenceActivityMasterId));
			}
			List<ActivityMaster> activityMasterList = c.list();
			if(activityMasterList != null && activityMasterList.size() > 0){
				isAlreadyExsist = true;
			}
		}catch(Exception ex){
			log.error("Error in isActivityMasterAvailable - ", ex);
		}
		return isAlreadyExsist;
	}
	
	
	@Override
	@Transactional
	public List<ActivityMaster> listActivityTypesByEnagementIdAndProductId(Integer testFactoryId, Integer productId, Boolean isConsildated) {
		List<ActivityMaster> activityTypeLists = null;
	
		try {
			Criteria c = sessionFactory.getCurrentSession().createCriteria(ActivityMaster.class, "activityType");
			if(testFactoryId != null && testFactoryId > 0 && productId != null && productId > 0){
				if(isConsildated){
					c.add(Restrictions.disjunction().add(
							Restrictions.or(
									Restrictions.conjunction()
										.add(Restrictions.eq("activityType.testFactory.testFactoryId", testFactoryId)).add(Restrictions.isNull("activityType.productMaster.productId")), 
									Restrictions.eq("activityType.productMaster.productId", productId)
					)));					
				}else{
					c.add(Restrictions.eq("activityType.testFactory.testFactoryId", testFactoryId));
					c.add(Restrictions.eq("activityType.productMaster.productId", productId));
				}
				
			}else if(testFactoryId != null && testFactoryId > 0){
				c.add(Restrictions.eq("activityType.testFactory.testFactoryId", testFactoryId));
			}else if(productId != null && productId > 0){
				c.add(Restrictions.eq("activityType.productMaster.productId", productId));
			}
			activityTypeLists = c.list();
			activityTypeLists = new ArrayList<ActivityMaster>(new HashSet<ActivityMaster>(activityTypeLists));
			log.debug("List listActivityTypesByEnagementIdAndProductId successful");
		} catch (RuntimeException re) {
			log.error("List listActivityTypesByEnagementIdAndProductId failed", re);
		}
		return activityTypeLists;
	}

	@Override
	@Transactional
	public ActivityMaster getActivityMasterByNameInProductAndTestFactory(String activityTypeName, Integer productId, Integer testFactoryId) {
		ActivityMaster activityMaster = null;
		try{
			Criteria c = sessionFactory.getCurrentSession().createCriteria(ActivityMaster.class, "am");		
			c.add(Restrictions.eq("am.activityMasterName", activityTypeName));
			if(testFactoryId != null && testFactoryId > 0 && productId != null && productId > 0){
				c.add(Restrictions.disjunction().add(
						Restrictions.or(
								Restrictions.conjunction()
									.add(Restrictions.eq("am.testFactory.testFactoryId", testFactoryId)).add(Restrictions.isNull("am.productMaster.productId")), 
								Restrictions.eq("am.productMaster.productId", productId)
				)));
			}else if(testFactoryId != null && testFactoryId > 0){
				c.add(Restrictions.eq("am.testFactory.testFactoryId", testFactoryId));
			}else if(productId != null && productId > 0){
				c.add(Restrictions.eq("am.productMaster.productId", productId));
			}else{
				c.add(Restrictions.isNull("am.testFactory.testFactoryId"));
				c.add(Restrictions.isNull("am.productMaster.productId"));
			}
			c.addOrder(Order.desc("am.activityMasterId"));
			
			List<ActivityMaster> activityMasterList = c.list();
			if(activityMasterList != null && activityMasterList.size() > 0){
				activityMaster = activityMasterList.get(0);
			}
		}catch(Exception ex){
			log.error("Error occured in getActivityMasterByNameInProductAndTestFactory - ", ex);
		}
		return activityMaster;
	}
	
	public List<WorkflowMasterEntityMapping> listActivityTypeByProductAndEntityAndMappingLevel(Integer productId, Integer entityTypeId, Integer entityId, String mappingLevel){
		List<WorkflowMasterEntityMapping> listOfWorkflowEntity = null;
		try{
			Criteria c = sessionFactory.getCurrentSession().createCriteria(WorkflowMasterEntityMapping.class,"wm");
			c.add(Restrictions.eq("wm.product.productId", productId));
			c.add(Restrictions.eq("wm.entityType.entitymasterid", entityTypeId));
			c.add(Restrictions.eq("wm.entityId", entityId));
			c.add(Restrictions.eq("wm.mappingLevel", mappingLevel));
			listOfWorkflowEntity = c.list();
			
		}catch(Exception ex){
			log.error("Error occured in listActivityTypeByProductAndEntityAndMappingLevel - ", ex);
		}
		return listOfWorkflowEntity;
	}
	
}
