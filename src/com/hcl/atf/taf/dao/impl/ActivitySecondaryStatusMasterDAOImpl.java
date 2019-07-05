package com.hcl.atf.taf.dao.impl;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.hcl.atf.taf.dao.ActivitySecondaryStatusMasterDAO;
import com.hcl.atf.taf.model.ActivitySecondaryStatusMaster;

@Repository
public class ActivitySecondaryStatusMasterDAOImpl implements ActivitySecondaryStatusMasterDAO {

	private static final Log log = LogFactory.getLog(ActivityStatusDAOImpl.class);
	@Autowired(required = true)
	private SessionFactory sessionFactory;
	

	
	@Override
	@Transactional
	public List<ActivitySecondaryStatusMaster> listActivitySecondaryStatusMaster() {
		List<ActivitySecondaryStatusMaster> secondaryStatusMasterList = null;
		try {
			Criteria c = sessionFactory.getCurrentSession().createCriteria(ActivitySecondaryStatusMaster.class, "ActivitySecondaryStatusMaster");
			secondaryStatusMasterList = c.list();
			log.debug("List ActivitySecondaryStatusMaster successful");
		} catch (RuntimeException re) {
			log.error("List ActivitySecondaryStatusMaster failed", re);
		}
		return secondaryStatusMasterList;
	}

	
	@Override
	@Transactional
	public ActivitySecondaryStatusMaster getSecondaryStatusById(
			int secondaryStatusId) {
		List<ActivitySecondaryStatusMaster> secondaryStatusMasterList = null;
		ActivitySecondaryStatusMaster secondaryStatusMaster = null;
		try {
			Criteria c = sessionFactory.getCurrentSession().createCriteria(ActivitySecondaryStatusMaster.class, "activitySecondaryStatusMaster");
			c.add(Restrictions.eq("activitySecondaryStatusMaster.activitySecondaryStatusId", secondaryStatusId));
			secondaryStatusMasterList = c.list();
			secondaryStatusMaster = (secondaryStatusMasterList!=null && secondaryStatusMasterList.size()!=0)?(ActivitySecondaryStatusMaster)secondaryStatusMasterList.get(0):null;
			
		} catch (RuntimeException re) {
			log.error("List commentsTracker failed", re);
		}
		return secondaryStatusMaster;
	}
	
	
	
	
	@Override
	@Transactional
	public List<ActivitySecondaryStatusMaster> listActivitySecondaryStatusMaster(
			Integer statusId, Integer jtStartIndex, Integer jtPageSize,
			Integer initializationLevel) {
		log.info("Activity ActivitySecondaryStatusMaster listing");
		List<ActivitySecondaryStatusMaster> activitySecondaryStatusMasterList = null;		
		List<Object> primaryHasSecondaryStatusList = null;
		try
		{
		 String sql="SELECT ace.activitySecondaryStatusId FROM activitystatus_has_secondarystatus ace WHERE ace.activityStatusId=:statusId";
		 primaryHasSecondaryStatusList=sessionFactory.getCurrentSession().createSQLQuery(sql).setParameter("statusId",statusId).list();
		  
		 if(primaryHasSecondaryStatusList!=null && primaryHasSecondaryStatusList.size()>0)  
		 {
		 Criteria c = sessionFactory.getCurrentSession().createCriteria(ActivitySecondaryStatusMaster.class, "assm");
		    c.add(Restrictions.in("assm.activitySecondaryStatusId",primaryHasSecondaryStatusList));

		
		if(jtStartIndex != null && jtPageSize != null)		
			{c.setFirstResult(jtStartIndex).setMaxResults(jtPageSize);	
			}	
		activitySecondaryStatusMasterList = c.list();			
		if(initializationLevel==1)
		{
			if(activitySecondaryStatusMasterList!=null  && activitySecondaryStatusMasterList.size()>0)
			{
				for (ActivitySecondaryStatusMaster activitySecondaryStatusMaster : activitySecondaryStatusMasterList) {
					Hibernate.initialize(activitySecondaryStatusMaster.getActivityStatus());
				}
			}
		}
		}
		 
		
		}
		catch (RuntimeException re) {
			log.error("ERROR getting EnvCombinationList Unmapped to ActivityId", re);	
		}
		return activitySecondaryStatusMasterList;
	}


	@Override
	public List<ActivitySecondaryStatusMaster> listActivitySecondaryStatusListForEffort(
			Integer statusId, Integer jtStartIndex, Integer jtPageSize,
			Integer initializationLevel) {
		return null;
	}

	@Override
	@Transactional
	public ActivitySecondaryStatusMaster getSecondaryStatusByName(String statusName) {
		List<ActivitySecondaryStatusMaster> activitySecondaryStatusMasterList = null;
		ActivitySecondaryStatusMaster activitySecondaryStatusMaster = null;
		try {
			Criteria c = sessionFactory.getCurrentSession().createCriteria(ActivitySecondaryStatusMaster.class, "activitySecondaryStatusMaster");
			c.add(Restrictions.eq("activitySecondaryStatusMaster.activitySecondaryStatusName", statusName));
			activitySecondaryStatusMasterList = c.list();
			activitySecondaryStatusMaster = (activitySecondaryStatusMasterList!=null && activitySecondaryStatusMasterList.size()!=0)?(ActivitySecondaryStatusMaster)activitySecondaryStatusMasterList.get(0):null;
		} catch (RuntimeException re) {
			log.error("List ActivityStatus failed", re);
		}
		return activitySecondaryStatusMaster;
	}


	@Override
	@Transactional
	public ActivitySecondaryStatusMaster getSecondaryStatusByNameAndDimentionId(String statusName,Integer dimentionId){
		
		List<ActivitySecondaryStatusMaster> activitySecondaryStatusMasterList = null;
		ActivitySecondaryStatusMaster activitySecondaryStatusMaster = null;
		try {
			Criteria c = sessionFactory.getCurrentSession().createCriteria(ActivitySecondaryStatusMaster.class, "activitySecondaryStatusMaster");
			c.add(Restrictions.eq("activitySecondaryStatusMaster.activitySecondaryStatusName", statusName));
			c.add(Restrictions.eq("activitySecondaryStatusMaster.dimensionId.dimensionId", dimentionId));
			activitySecondaryStatusMasterList = c.list();
			activitySecondaryStatusMaster = (activitySecondaryStatusMasterList!=null && activitySecondaryStatusMasterList.size()!=0)?(ActivitySecondaryStatusMaster)activitySecondaryStatusMasterList.get(0):null;
		} catch (RuntimeException re) {
			log.error("List ActivityStatus secondary failed", re);
		}
		return activitySecondaryStatusMaster;
		
	}
	
	
	@Override
	@Transactional
	public List<ActivitySecondaryStatusMaster> getSecondaryStatusbyDimentionId(Integer dimentionId){
		
		List<ActivitySecondaryStatusMaster> activitySecondaryStatusMasterList = null;
		ActivitySecondaryStatusMaster activitySecondaryStatusMaster = null;
		try {
			Criteria c = sessionFactory.getCurrentSession().createCriteria(ActivitySecondaryStatusMaster.class, "activitySecondaryStatusMaster");
			c.add(Restrictions.eq("activitySecondaryStatusMaster.dimensionId.dimensionId", dimentionId));
			activitySecondaryStatusMasterList = c.list();
		} catch (RuntimeException re) {
			log.error("List Activity secondary status failed", re);
		}
		return activitySecondaryStatusMasterList;
	}
}
