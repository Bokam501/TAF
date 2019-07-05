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

import com.hcl.atf.taf.dao.SystemTypeDAO;
import com.hcl.atf.taf.model.SystemType;


@Repository
public class SystemTypeDAOImpl implements SystemTypeDAO {
	private static final Log log = LogFactory.getLog(SystemTypeDAOImpl.class);
	
	@Autowired(required=true)
    private SessionFactory sessionFactory;	
	
	@Override
	@Transactional
	public List<SystemType> list() {
		List<SystemType> systemTypeList = null;
		try{
			Criteria c = sessionFactory.getCurrentSession().createCriteria(SystemType.class, "systemtype");			
			systemTypeList = c.list();			
			log.debug("list SystemType successful");
		}catch (RuntimeException re) {
			log.error("list SystemType failed", re);
		}
		return systemTypeList;	
	}
	
	@Override
	public void add(SystemType systemType) {
		log.debug("adding SystemType instance");
		try {
			sessionFactory.getCurrentSession().save(systemType);
			log.debug("SystemType - add successful");
		} catch (RuntimeException re) {
			log.error("SystemType - add failed", re);
		}				
	}

	@Override
	public SystemType getSystemTypeByName(String name) {
		SystemType systemType = null;
		try {
			Criteria c = sessionFactory.getCurrentSession().createCriteria(SystemType.class, "system");
			c.add(Restrictions.eq("system.name", name));
			List list = c.list();		
			systemType=(list!=null && list.size()!=0)?(SystemType)list.get(0):null;
			
			log.debug("SystemType by Name successful");
		} catch (RuntimeException re) {
			log.error("SystemType by Name failed", re);
		}
		return systemType;
	}

}
