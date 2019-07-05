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

import com.hcl.atf.taf.dao.CommentsTypeMasterDAO;
import com.hcl.atf.taf.model.CommentsTypeMaster;
@Repository
public class CommentsTypeMasterDAOImpl implements CommentsTypeMasterDAO{


	private static final Log log = LogFactory.getLog(ActivityStatusDAOImpl.class);

	@Autowired(required = true)
	private SessionFactory sessionFactory;
	
	@Override
	@Transactional
	public List<CommentsTypeMaster> listCommentsTypeMasters() {
		List<CommentsTypeMaster> commentsTypeMasterList = null;
		try {
			Criteria c = sessionFactory.getCurrentSession().createCriteria(CommentsTypeMaster.class, "CommentsTypeMaster");
			commentsTypeMasterList = c.list();
			log.debug("List listActivityStatuses successful");
		} catch (RuntimeException re) {
			log.error("List listActivityStatuses failed", re);
		}
		return commentsTypeMasterList;
	}

	@Override
	@Transactional
	public CommentsTypeMaster getCommentsTypeMasterById(
			Integer CommentsTypeMasterId) {
		List<CommentsTypeMaster> commentsTypeMasterList = null;
		CommentsTypeMaster commentsTypeMaster = null;
		try {
			Criteria c = sessionFactory.getCurrentSession().createCriteria(CommentsTypeMaster.class, "commentsTypeMaster");
			c.add(Restrictions.eq("commentsTypeMaster.commentsTypeId", CommentsTypeMasterId));
			commentsTypeMasterList = c.list();
			commentsTypeMaster = (commentsTypeMasterList!=null && commentsTypeMasterList.size()!=0)?(CommentsTypeMaster)commentsTypeMasterList.get(0):null;
			log.info("List CommentsTypeMaster successful. CommentsTypeMaster NAME: "+commentsTypeMaster.getCommentsTypeName());
		} catch (RuntimeException re) {
			log.error("List CommentsTypeMaster failed", re);
		}
		return commentsTypeMaster;
	}
	
	@Override
	@Transactional
	public CommentsTypeMaster getCommentsTypeMasterByName(String typeName) {
		List<CommentsTypeMaster> commentsTypeMasterList = null;
		CommentsTypeMaster commentsTypeMaster = null;
		try {
			log.info("DB query for typeName"+typeName);
			Criteria c = sessionFactory.getCurrentSession().createCriteria(CommentsTypeMaster.class, "commentsTypeMaster");
			c.add(Restrictions.eq("commentsTypeMaster.commentsTypeName",typeName));
			commentsTypeMasterList = c.list();
			if(commentsTypeMasterList!=null)			{
				commentsTypeMaster = (commentsTypeMasterList!=null && commentsTypeMasterList.size()!=0)?(CommentsTypeMaster)commentsTypeMasterList.get(0):null;
			log.info("List commentsTypeMaster successful. commentsTypeMaster: "+commentsTypeMaster.getCommentsTypeName());}
			else{
				log.info("DB query for list commentsTypeMaster failure");
			}
		} catch (RuntimeException re) {
			log.error("List ActivityResult failed", re);
		}
		return commentsTypeMaster;
	}
	

}
