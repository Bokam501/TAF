package com.hcl.atf.taf.dao.impl;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.hcl.atf.taf.dao.ActivityResultDAO;
import com.hcl.atf.taf.model.ActivityResult;

@Repository
public class ActivityResultDAOImpl implements ActivityResultDAO {
	private static final Log log = LogFactory.getLog(ActivityStatusDAOImpl.class);

	@Autowired(required = true)
	private SessionFactory sessionFactory;
	
	@Override
	@Transactional
	public List<ActivityResult> listActivityResults() {
		List<ActivityResult> activityResultList = null;
		try {
			Criteria c = sessionFactory.getCurrentSession().createCriteria(ActivityResult.class, "ActivityResult");
			activityResultList = c.list();
			log.debug("List ActivityResults successful");
		} catch (RuntimeException re) {
			log.error("List ActivityResults failed", re);
		}
		return activityResultList;
	}

	

	@Override
	@Transactional
	public ActivityResult getResultsByName(String resultName) {
		List<ActivityResult> sctivityResultsList = null;
		ActivityResult activityResult = null;
		try {
			Criteria c = sessionFactory.getCurrentSession().createCriteria(ActivityResult.class, "activityResult");
			c.add(Restrictions.eq("activityResult.activityResultName", resultName));
			sctivityResultsList = c.list();
			activityResult = (sctivityResultsList!=null && sctivityResultsList.size()!=0)?(ActivityResult)sctivityResultsList.get(0):null;
		} catch (RuntimeException re) {
			log.error("List ActivityResult failed", re);
		}
		return activityResult;
	}

	@Override
	public ActivityResult getResultsById(Integer resultId) {
		// TODO Auto-generated method stub
		return null;
	}
}
