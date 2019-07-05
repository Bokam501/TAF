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

import com.hcl.atf.taf.dao.TestFactoryCoreResoucesDao;
import com.hcl.atf.taf.model.TestFactoryCoreResource;
@Repository
public class TestFactoryCoreResoucesDaoImpl implements TestFactoryCoreResoucesDao {

private static final Log log = LogFactory.getLog(TestFactoryCoreResoucesDaoImpl.class);
	
	@Autowired(required=true)
    private SessionFactory sessionFactory;

	@SuppressWarnings("unchecked")
	@Override
	@Transactional
	public List<TestFactoryCoreResource> getCoreResourcesList(
			int testFactoryId, Integer jtStartIndex, Integer jtPageSize) {
		log.debug("listing listProductUserRole instance");
		List<TestFactoryCoreResource> tfCoreResList = null;
		try {
			Criteria c = sessionFactory.getCurrentSession().createCriteria(TestFactoryCoreResource.class, "testFactoryCoreRes");
			c.createAlias("testFactoryCoreRes.testFactory", "testFactory");
			c.add(Restrictions.eq("testFactory.testFactoryId", testFactoryId));
			if(jtStartIndex != null && jtPageSize != null)
				c.setFirstResult(jtStartIndex).setMaxResults(jtPageSize);
			
			tfCoreResList = c.list();
			
			if (!(tfCoreResList == null || tfCoreResList
					.isEmpty())) {
				for (TestFactoryCoreResource tfCoreRes : tfCoreResList) {
					Hibernate.initialize(tfCoreRes.getTestFactory());
					Hibernate.initialize(tfCoreRes.getUserRoleMaster());
					Hibernate.initialize(tfCoreRes.getUserList());
				}
			}
			log.debug("list successful");
		} catch (RuntimeException re) {
			log.error("list failed", re);
			// throw re;
		}
		return tfCoreResList;
	}

	@Override
	@Transactional
	public void addCoreResource(TestFactoryCoreResource coreResouce) {
		log.debug("adding Test FactoryCore Resouce");
		try {
			coreResouce.setStatus(1);
			sessionFactory.getCurrentSession().save(coreResouce);
			log.debug("add successful");
		} catch (RuntimeException re) {
			log.error("add failed", re);
		}
		
	}


	@Override
	@Transactional
	public TestFactoryCoreResource getCoreResourceById(
			Integer testFactoryCoreResourceId) {
		TestFactoryCoreResource coreRes=null;
		String hql = "from TestFactoryCoreResource coreRes where coreRes.testFactoryCoreResourceId = :testFactoryCoreResourceId";
		List instances = sessionFactory.getCurrentSession().createQuery(hql).setParameter("testFactoryCoreResourceId", testFactoryCoreResourceId).list();
		coreRes=(instances!=null && instances.size()!=0)?(TestFactoryCoreResource)instances.get(0):null;
		
		return coreRes;
	}

	@Override
	@Transactional
	public void updateCoreRes(TestFactoryCoreResource coreResourceFromUI) {
		log.debug("updating CoreResorce instance");
		
		try {
			sessionFactory.getCurrentSession().update(coreResourceFromUI);
			log.debug("update successful");  
		} catch (RuntimeException re) {
			log.error("update failed", re);
		} 
		
	}

	@Override
	@Transactional
	public Boolean isUserExisted(Integer testFactoryId, Integer userId) {
		String hql = "from TestFactoryCoreResource coreRes where coreRes.testFactory.testFactoryId = :testFactoryId and coreRes.userList.userId=:userId";
		List coreResUser = sessionFactory.getCurrentSession().createQuery(hql).setParameter("testFactoryId", testFactoryId).setParameter("userId", userId).list();
		if (coreResUser == null || coreResUser.isEmpty()) 
		    return false;
		else 
			return true;
	}

	@Override
	@Transactional
	public List<TestFactoryCoreResource> list() {
		log.info("listing TestFactoryCoreResource instance");
		List<TestFactoryCoreResource> tfCoreResList = null;
		try {
			tfCoreResList = sessionFactory
					.getCurrentSession()
					.createQuery(
							"from TestFactoryCoreResource coreRes where status=1")
					.list();
			if (!(tfCoreResList == null || tfCoreResList
					.isEmpty())) {
				for (TestFactoryCoreResource tfCoreRes : tfCoreResList) {
					Hibernate.initialize(tfCoreRes.getTestFactory());
					Hibernate.initialize(tfCoreRes.getUserRoleMaster());
					Hibernate.initialize(tfCoreRes.getUserList());
				}
			}
			log.info("list successful");
		} catch (RuntimeException re) {
			log.error("list failed", re);
		}
		return tfCoreResList;
	}	
}