package com.hcl.atf.help.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.hcl.atf.help.dao.HelpItemDAO;
import com.hcl.atf.help.model.HelpItem;


@Repository
public class HelpItemDAOImpl implements HelpItemDAO {
	private static final Log log = LogFactory.getLog(HelpItemDAOImpl.class);
	
	
	@Autowired(required=true)
    private SessionFactory sessionFactory;
	
	@Override
	@Transactional
	public List<HelpItem> list(int status) {		
		Criteria c = sessionFactory.getCurrentSession().createCriteria(HelpItem.class, "helpItem");
		if(status <2){
			c.add(Restrictions.eq("helpItem.status", status));	
		} else {
			c.add(Restrictions.eq("helpItem.status", 1));	
		}
		List<HelpItem> helpItems = c.list();		
		return helpItems;
	}

	@Override
	@Transactional
	public HelpItem getHelpItemById(int helpItemId) {

		log.debug("getting HelpItem instance by id");
		HelpItem helpItem=null;
		List<HelpItem> helpItemList = new ArrayList<HelpItem>();
		
		try {
			Criteria c = sessionFactory.getCurrentSession().createCriteria(HelpItem.class, "helpItem");
			c.add(Restrictions.eq("helpItem.helpItemId", helpItemId));
			
			helpItemList = c.list();
			helpItem=(helpItemList!=null && helpItemList.size()!=0)?(HelpItem)helpItemList.get(0):null;
			
			log.debug("get By helpItemId successful");
		} catch (RuntimeException re) {
			log.error("get By helpItemId failed", re);
			//throw re;
		}
		return helpItem;
	}

	@Override
	@Transactional
	public HelpItem getHelpItemByName(String name) {

		log.debug("getting HelpItem instance by name");
		HelpItem helpItem=null;
		List<HelpItem> helpItemList = new ArrayList<HelpItem>();
		
		try {
			Criteria c = sessionFactory.getCurrentSession().createCriteria(HelpItem.class, "helpItem");
			c.add(Restrictions.eq("helpItem.name", name));
			
			helpItemList = c.list();
			helpItem=(helpItemList!=null && helpItemList.size()!=0)?(HelpItem)helpItemList.get(0):null;
			
			log.debug("get By helpItemname successful");
		} catch (RuntimeException re) {
			log.error("get By helpItemname failed", re);
			//throw re;
		}
		return helpItem;
	}

	@Override
	@Transactional
	public void add(HelpItem helpItem) {
		log.debug("adding HelpItem instance");
		try {
			sessionFactory.getCurrentSession().save(helpItem);	
			log.debug("add successful");
		} catch (RuntimeException re) {
			log.error("add failed", re);
			//throw re;
		}
	}
	
	@Override
	@Transactional
	public void delete(HelpItem helpItem) {
		log.debug("Deleting HelpItem instance");
		try {			
			sessionFactory.getCurrentSession().delete(helpItem);
			log.debug("Deleting successful");
		} catch (RuntimeException re) {
			log.error("Deleting failed", re);
			//throw re;
		}		
	}
	
	@Override
	@Transactional
	public void delete(Integer helpItemId) {
		log.debug("Deleting HelpItem instance");
		try {			
			sessionFactory.getCurrentSession().delete(getHelpItemById(helpItemId));
			log.debug("Deleting successful");
		} catch (RuntimeException re) {
			log.error("Deleting failed", re);
			//throw re;
		}		
	}

	@Override
	@Transactional
	public HelpItem update(HelpItem helpItem) {
		log.debug("updating helpItem instance");
		try {
			sessionFactory.getCurrentSession().saveOrUpdate(helpItem);
			log.debug("update successful");
		} catch (RuntimeException re) {
			log.error("update failed", re);
			//throw re;
		}	
		return helpItem;
	}
	
	@Override
	@Transactional
	public int getTotalCount() {
		
		int count = 0;
		try {
			Criteria c = sessionFactory.getCurrentSession().createCriteria(HelpItem.class, "helpItem");
			c.setProjection(Projections.rowCount());
			count = Integer.parseInt("" + c.uniqueResult());
		} catch (Exception e) {
			log.error("Unable to get helpItems count", e);
		}
		return count;
	}

	@Override
	@Transactional
	public List<HelpItem> list() {		
		try {
		Criteria c = sessionFactory.getCurrentSession().createCriteria(HelpItem.class, "helpItem");
		List<HelpItem> helpItems = c.list();		
		return helpItems;
		}catch(RuntimeException re) {
			log.error("Error while get list of helpItem",re);
		}
		return null;
	}	
}
