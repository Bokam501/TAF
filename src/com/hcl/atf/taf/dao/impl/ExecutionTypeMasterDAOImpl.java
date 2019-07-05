package com.hcl.atf.taf.dao.impl;

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

import com.hcl.atf.taf.dao.ExecutionTypeMasterDAO;
import com.hcl.atf.taf.model.ExecutionTypeMaster;


@Repository
public class ExecutionTypeMasterDAOImpl implements ExecutionTypeMasterDAO {
	private static final Log log = LogFactory.getLog(ExecutionTypeMasterDAOImpl.class);
	
	@Autowired(required=true)
    private SessionFactory sessionFactory;	
	
	@Override
	@Transactional
	public List<ExecutionTypeMaster> list() {
		List<ExecutionTypeMaster> executionTypeMasterList = null;		
		try {
			Criteria c = sessionFactory.getCurrentSession().createCriteria(ExecutionTypeMaster.class, "executiontm");		
			executionTypeMasterList = c.list();
			for (ExecutionTypeMaster executionTypeMaster : executionTypeMasterList) {
				Hibernate.initialize(executionTypeMaster.getEntitymaster());
			}
		} catch (HibernateException e) {
			log.error("Error",e);
		}		
		return executionTypeMasterList;
	}
	
	@Override
	@Transactional
	public ExecutionTypeMaster getExecutionTypeByExecutionTypeId(int executionTypeId) {
		List<ExecutionTypeMaster> executionTypeMasterList = null;		
		ExecutionTypeMaster executionTypeMaster = null;
		try {
			Criteria c = sessionFactory.getCurrentSession().createCriteria(ExecutionTypeMaster.class, "executiontm");		
			c.add(Restrictions.eq("executiontm.executionTypeId", executionTypeId));
			executionTypeMasterList = c.list();
			for (ExecutionTypeMaster executionTypeMasterobj : executionTypeMasterList) {
				Hibernate.initialize(executionTypeMasterobj.getEntitymaster());
			}
			executionTypeMaster = (executionTypeMasterList!=null && executionTypeMasterList.size()!=0)?(ExecutionTypeMaster)executionTypeMasterList.get(0):null;
		} catch (HibernateException e) {
			log.error("Error getting ExecutionTypeByExecutionTypeId ",e);
		}		
		return executionTypeMaster;
	}
	
	@Override
	@Transactional
	public List<ExecutionTypeMaster> listbyEntityMasterId(int entitymasterid) {
		List<ExecutionTypeMaster> executionTypeMasterList = null;		
		try {
			Criteria c = sessionFactory.getCurrentSession().createCriteria(ExecutionTypeMaster.class, "executiontm");		
			c.createAlias("executiontm.entitymaster", "etm");
			c.add(Restrictions.eq("etm.entitymasterid", entitymasterid));
			executionTypeMasterList = c.addOrder(Order.desc("executiontm.name")).list();
			for (ExecutionTypeMaster executionTypeMaster : executionTypeMasterList) {
				Hibernate.initialize(executionTypeMaster.getEntitymaster());
			}
		} catch (HibernateException e) {
			log.error("Error listing ExecutionTypeMaster byEntityMasterId ",e);
		}		
		return executionTypeMasterList;
	}

	@Override
	@Transactional
	public ExecutionTypeMaster getExecutionTypeByExecutionTypeName(
			String executionTypeName) {
		List<ExecutionTypeMaster> executionTypeMasterList = null;		
		ExecutionTypeMaster executionTypeMaster = null;
		try {
			Criteria c = sessionFactory.getCurrentSession().createCriteria(ExecutionTypeMaster.class, "executionTypeMaster");		
			c.add(Restrictions.eq("executionTypeMaster.name", executionTypeName).ignoreCase());
			executionTypeMasterList = c.list();
			
			executionTypeMaster = (executionTypeMasterList!=null && executionTypeMasterList.size()!=0)?(ExecutionTypeMaster)executionTypeMasterList.get(0):null;
		} catch (HibernateException e) {
			log.error("Error geting ExecutionTypeMaster ByExecutionTypeName ",e);
		}		
		return executionTypeMaster;
	}
}
