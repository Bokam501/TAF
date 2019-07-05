package com.hcl.atf.taf.dao.impl;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.hcl.atf.taf.dao.ActivityStatusDAO;
import com.hcl.atf.taf.model.ActivityStatus;



@Repository
public class ActivityStatusDAOImpl implements ActivityStatusDAO {
	private static final Log log = LogFactory.getLog(ActivityStatusDAOImpl.class);

	@Autowired(required = true)
	private SessionFactory sessionFactory;
	
	@Override
	@Transactional
	public List<ActivityStatus> listActivityStatuses() {
		List<ActivityStatus> activityStatusList = null;
		try {
			Criteria c = sessionFactory.getCurrentSession().createCriteria(ActivityStatus.class, "ActivityStatus");
			activityStatusList = c.list();
			log.debug("List listActivityStatuses successful");
		} catch (RuntimeException re) {
			log.error("List listActivityStatuses failed", re);
		}
		return activityStatusList;
	}

	



	@Override
	@Transactional
	public ActivityStatus getStatusByName(String statusName) {
		List<ActivityStatus> activityStatusList = null;
		ActivityStatus activityStatus = null;
		try {
			Criteria c = sessionFactory.getCurrentSession().createCriteria(ActivityStatus.class, "activityStatus");
			c.add(Restrictions.eq("activityStatus.activityStatusName", statusName));
			activityStatusList = c.list();
			activityStatus = (activityStatusList!=null && activityStatusList.size()!=0)?(ActivityStatus)activityStatusList.get(0):null;
		} catch (RuntimeException re) {
			log.error("List ActivityStatus failed", re);
		}
		return activityStatus;
	}

	@Override
	@Transactional
	public ActivityStatus getStatusById(Integer statusId) {
		List<ActivityStatus> activityStatusList = null;
		ActivityStatus activityStatus = null;
		try {
			Criteria c = sessionFactory.getCurrentSession().createCriteria(ActivityStatus.class, "activityStatus");
			c.add(Restrictions.eq("activityStatus.activityStatusId", statusId));
			activityStatusList = c.list();
			activityStatus = (activityStatusList!=null && activityStatusList.size()!=0)?(ActivityStatus)activityStatusList.get(0):null;
			log.info("List ActivityStatus successful. ActivityStatus: "+activityStatus.getActivityStatusName());
		} catch (RuntimeException re) {
			log.error("List ActivityStatus failed", re);
		}
		return activityStatus;
	}

	
	@Override
	@Transactional
	public List<ActivityStatus> listActivityStatusByDimensionId(Integer dimensionId){
		
		List<ActivityStatus> activityStatusList = null;
		try {
			Criteria c = sessionFactory.getCurrentSession().createCriteria(ActivityStatus.class, "activityStatus");
			c.createAlias("activityStatus.dimension", "dimension");
			c.add(Restrictions.eq("dimension.dimensionId", dimensionId));
			activityStatusList = c.list();
			log.debug("List listActivityStatuses successful");
		} catch (RuntimeException re) {
			log.error("List listActivityStatuses failed", re);
		}
		return activityStatusList;
		
	}
	@Override
	@Transactional
	public ActivityStatus getStatusNameByDimensionId(String activityStatusName,Integer dimensionId){
		
		List<ActivityStatus> activityStatusList = null;
		ActivityStatus activityStatus = null;
		try {
			Query query = null;
			query = sessionFactory.getCurrentSession().createQuery("from ActivityStatus activityStatus where activityStatus.dimension.dimensionId=:dimensionId and activityStatus.activityStatusName=:activityStatusName")
					.setParameter("activityStatusName",activityStatusName)
					.setParameter("dimensionId",dimensionId);
			
			activityStatusList = query.list();
			activityStatus = (activityStatusList!=null && activityStatusList.size()!=0)?(ActivityStatus)activityStatusList.get(0):null;
		} catch (RuntimeException re) {
			log.error("List ActivityStatus failed", re);
		}
		return activityStatus;
		
	}

}
