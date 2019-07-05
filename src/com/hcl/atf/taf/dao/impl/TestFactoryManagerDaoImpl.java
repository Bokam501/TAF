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

import com.hcl.atf.taf.dao.TestFactoryManagerDao;
import com.hcl.atf.taf.model.TestFactoryManager;
@Repository
public class TestFactoryManagerDaoImpl implements TestFactoryManagerDao {
private static final Log log = LogFactory.getLog(TestFactoryManagerDaoImpl.class);
	
	@Autowired(required=true)
    private SessionFactory sessionFactory;

	@SuppressWarnings("unchecked")
	@Override
	@Transactional
	public List<TestFactoryManager> listTestFactoryManager(int testFactoryId,
			int entityStatusActive) {
		log.debug("listing all TestFactoryManager");		
		List<TestFactoryManager> managerList=null;

		try {			
			managerList=sessionFactory.getCurrentSession().createQuery("from TestFactoryManager manager where (manager.testFactory.testFactoryId=:testFactoryId) and (status=1 )")
			.setParameter("testFactoryId", testFactoryId).list();		
			if(managerList!=null && !managerList.isEmpty()){
				for (TestFactoryManager manager : managerList) {
					Hibernate.initialize(manager.getTestFactory());
					Hibernate.initialize(manager.getUserList());
							
					}
			}
			log.debug("list all successful");
			
		} catch (RuntimeException re) {
			log.error("list all failed", re);
		}
		return managerList;
	}

	
	
	
	
	
	
	
	
	@Override
	@Transactional
	public void addTestFactoryManager(TestFactoryManager tfManager) {
		log.debug("adding TestFactoryManager instance");
		try {
			tfManager.setStatus(1);
			
			sessionFactory.getCurrentSession().save(tfManager);
			log.debug("add successful");
		} catch (RuntimeException re) {
			log.error("add failed", re);
		}
	}

	@Override
	@Transactional
	public void delete(TestFactoryManager manager) {
		log.debug("deleting TestFactoryManager instance");
		try {
			
			sessionFactory.getCurrentSession().delete(manager);
			
			log.debug("deleted ");
		} catch (RuntimeException re) {
			log.error("add failed", re);
		}
	}	
	
	@Override
	@Transactional
	public TestFactoryManager getTestFactoryManagerByTestCatoryIdUserId(int userId, int testFactoryId) {
		List<TestFactoryManager> testFactoryManagersList = null;
		TestFactoryManager testFactoryManger=null;
		try {
			Criteria c = sessionFactory.getCurrentSession().createCriteria(TestFactoryManager.class, "tfm");
			c.createAlias("tfm.testFactory", "testFactory");
			c.createAlias("tfm.userList", "userList");
			c.add(Restrictions.eq("testFactory.testFactoryId", testFactoryId));
			c.add(Restrictions.eq("userList.userId", userId));
			testFactoryManagersList = c.list();
			 testFactoryManger = (testFactoryManagersList != null && testFactoryManagersList.size() != 0) ? (TestFactoryManager) testFactoryManagersList
					.get(0) : null;
			log.info("Result Set Size : " + testFactoryManagersList.size());
			for (TestFactoryManager testFactoryManager : testFactoryManagersList) {
				Hibernate.initialize(testFactoryManager.getUserList());
			}
		} catch (Exception e) {
			log.error("update failed", e);
			return null;
		}
		return testFactoryManger;
	}

	@Override
	@Transactional
	public void deleteTestFactoryMappedUsersByTestFactoryIdAndUserId(Integer testFactoryId, List<Integer> userIds) {
		log.debug("deleteTestFactoryMappedUsersByTestFactoryIdAndUserId");
		try {
			 
		 Criteria c =  sessionFactory.getCurrentSession().createCriteria(TestFactoryManager.class, "tfm");
		 c.createAlias("tfm.testFactory", "testFactory");
		 c.add(Restrictions.eq("testFactory.testFactoryId", testFactoryId));
		 c.add(Restrictions.in("tfm.userList.userId",userIds));		
		 List<TestFactoryManager> tfMS =  c.list();
		 for(TestFactoryManager tfM : tfMS){
			 sessionFactory.getCurrentSession().delete(tfM);			 
		 }		
			log.debug("deleted ");
		} catch (RuntimeException re) {
			log.error("deleted failed", re);
		}
	}

	
}
