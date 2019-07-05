package com.hcl.atf.taf.dao.impl;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.hcl.atf.taf.dao.ActivityTypeMasterDAO;
import com.hcl.atf.taf.model.ActivityType;

@Repository
public class ActivityTypeMasterDAOImpl implements ActivityTypeMasterDAO {
	private static final Log log = LogFactory.getLog(ActivityTypeDAOImpl.class);

	@Autowired(required = true)
	private SessionFactory sessionFactory;
	
	

	@Override
	@Transactional
	public List<ActivityType> listActivityTypeMaster() {
		List<ActivityType> activityTypeMasterLists = null;
		try {
			Criteria c = sessionFactory.getCurrentSession().createCriteria(ActivityType.class, "activityTypeMaster");
			activityTypeMasterLists = c.list();
			log.debug("List ActivityTypeMaster successful");
		} catch (RuntimeException re) {
			log.error("List ActivityTypeMaster failed", re);
		}
		return activityTypeMasterLists;
	}

	@Override
	@Transactional
	public ActivityType getActivityTypeMasterById(Integer activityTypeMasterId,Integer initializationLevel) {

		ActivityType activityType=null;
		try {
			List list =  sessionFactory.getCurrentSession().createQuery("from ActivityTypeMaster where activityTypeMasterId=:activityTypeMasterId")
					.setParameter("activityTypeMasterId", activityTypeMasterId)
					.list();
			activityType=(list!=null && list.size()!=0)?(ActivityType)list.get(0):null;
		}			
		catch (RuntimeException re) {
			log.error("getActivityById failed", re);
		}
		return activityType;
	}
	
	
}
