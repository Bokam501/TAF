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

import com.hcl.atf.taf.dao.CommentsTrackerDAO;
import com.hcl.atf.taf.model.CommentsTracker;

@Repository
public class CommentsTrackerDAOImpl implements CommentsTrackerDAO{

	private static final Log log = LogFactory.getLog(ActivityStatusDAOImpl.class);
	@Autowired(required = true)
	private SessionFactory sessionFactory;
	
	@Override
	@Transactional
	public List<CommentsTracker> listCommentsTracker() {
		List<CommentsTracker> commentsTrackerList = null;
		try {
			Criteria c = sessionFactory.getCurrentSession().createCriteria(CommentsTracker.class, "CommentsTracker");
			commentsTrackerList = c.list();
			log.debug("List listActivityStatuses successful");
		} catch (RuntimeException re) {
			log.error("List listActivityStatuses failed", re);
		}
		return commentsTrackerList;
	}

	@Override
	@Transactional
	public CommentsTracker getCommentsTrackerById(int CommentsTrackerId) {
		List<CommentsTracker> commentsTrackerList = null;
		CommentsTracker commentsTracker = null;
		try {
			Criteria c = sessionFactory.getCurrentSession().createCriteria(CommentsTracker.class, "commentsTracker");
			c.add(Restrictions.eq("commentsTracker.commentsTrackerId", CommentsTrackerId));
			commentsTrackerList = c.list();
			commentsTracker = (commentsTrackerList!=null && commentsTrackerList.size()!=0)?(CommentsTracker)commentsTrackerList.get(0):null;
			
		} catch (RuntimeException re) {
			log.error("List commentsTracker failed", re);
		}
		return commentsTracker;
	}

	@Override
	@Transactional
	public void addCommentsTracker(CommentsTracker commentsTracker) {
		log.info("adding CommentsTracker instance");
		try {	
			sessionFactory.getCurrentSession().save(commentsTracker);
			
			log.info("add CommentsTracker successful");
		} catch (RuntimeException re) {
			log.error("add CommentsTracker failed", re);
			//throw re;
		}
				
	}

	@Override
	@Transactional
	public void updateCommentsTracker(CommentsTracker commentsTracker) {
		log.debug("updating CommentsTracker instance");
	
		try {
			
			sessionFactory.getCurrentSession().saveOrUpdate(commentsTracker);
			log.debug("update CommentsTracker successful");
		} catch (RuntimeException re) {
			log.error("update CommentsTracker failed", re);
		}
		
	}

	@Override
	@Transactional
	public void deleteCommentsTracker(CommentsTracker commentsTracker) {

		log.debug("Delete ChangeRequest instance");
		try {
			sessionFactory.getCurrentSession().delete(commentsTracker);
			log.debug("ChangeRequest deletion successful");
		} catch (RuntimeException re) {
			log.error("ChangeRequest deletion failed", re);
		}	
		
	}

	@Override
	@Transactional
	public List<CommentsTracker> listCommentsTrackersByActivityTaskId(
			Integer activityTaskId, int initializationLevel) {
		log.debug("listing all CommentsTracker instance");
		List<CommentsTracker> commentsTrackerList = null;
		try {
			commentsTrackerList = sessionFactory.getCurrentSession().createQuery("from CommentsTracker ct where ct.entityId.activityTaskId=:activityTaskId")
					.setParameter("activityTaskId", activityTaskId)
					.list();
			log.debug("list all CommentsTracker successful");
		} catch (RuntimeException re) {
			log.error("list all CommentsTracker failed", re);
		}
		return commentsTrackerList;
	}

	
}
