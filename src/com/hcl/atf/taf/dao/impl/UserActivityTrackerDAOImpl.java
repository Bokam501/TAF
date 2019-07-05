/**
 * 
 */
package com.hcl.atf.taf.dao.impl;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.hcl.atf.taf.dao.UserActivityTrackerDAO;
import com.hcl.atf.taf.model.UserActivityTracker;

/**
 * @author silambarasur
 * 
 */
@Repository
public class UserActivityTrackerDAOImpl implements UserActivityTrackerDAO {

	private static final Log log = LogFactory
			.getLog(UserActivityTrackerDAOImpl.class);

	@Autowired(required = true)
	private SessionFactory sessionFactory;

	@Override
	@Transactional
	public UserActivityTracker getUserActivityTracker(Integer userId) {
		List<UserActivityTracker> userActivityTrackers = null;
		try {
			Criteria c = sessionFactory.getCurrentSession().createCriteria(UserActivityTracker.class, "userActivityTracker");	
			c.add(Restrictions.eq("userActivityTracker.user.userId", userId));
			c.addOrder(Order.desc("userActivityId"));
			userActivityTrackers=c.list();
			if(userActivityTrackers!=null && userActivityTrackers.size() > 0) {
				return userActivityTrackers.get(0);
			}
		} catch (RuntimeException re) {
			log.error("getBy UserId failed", re);
		}
		return null;
	}

	@Override
	@Transactional
	public List<UserActivityTracker> getUserActivityTrakerByUserId(
			Integer userId) {

		List<UserActivityTracker> userActivityTrackers = null;
		try {
			List list = sessionFactory
					.getCurrentSession()
					.createQuery(
							"from UserActivityTracker uat where userId=:userId")
					.setParameter("userId", userId).list();
			userActivityTrackers = list;
		} catch (RuntimeException re) {
			log.error("getUserActivityTrakerByUserId  failed", re);
		}
		return userActivityTrackers;

	}

	@Override
	@Transactional
	public List<UserActivityTracker> getUserActivityTrakerList() {
		List<UserActivityTracker> userActivityTrackers = null;
		try {
			List list = sessionFactory.getCurrentSession()
					.createQuery("from UserActivityTracker").list();
			userActivityTrackers = list;
		} catch (RuntimeException re) {
			log.error("getUserActivityTrakerList  failed", re);
		}
		return userActivityTrackers;

	}

	@Override
	@Transactional
	public void addUserActivityTracker(UserActivityTracker userActivityTracker) {
		try {
			sessionFactory.getCurrentSession().save(userActivityTracker);
		} catch (RuntimeException re) {
			log.error("addUserActivityTracker  failed", re);
		}
	}

	@Override
	@Transactional
	public void updateUserActivityTracker(UserActivityTracker userActivityTracker) {
		try {
			sessionFactory.getCurrentSession().saveOrUpdate(userActivityTracker);
		} catch (RuntimeException re) {
			log.error("updateUserActivityTracker  failed", re);
		}
	}

}
