package com.hcl.atf.taf.dao.impl;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Hibernate;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.hcl.atf.taf.dao.TestRunConfigurationParentDAO;
import com.hcl.atf.taf.model.TestRunConfigurationParent;

@Repository
public class TestRunConfigurationParentDAOImpl implements TestRunConfigurationParentDAO {
	private static final Log log = LogFactory.getLog(TestRunConfigurationParentDAOImpl.class);
	
	@Autowired(required=true)
    private SessionFactory sessionFactory;

	@Override
	@Transactional
	public void add(TestRunConfigurationParent testRunConfigurationParent) {
		log.debug("adding TestRunConfigurationParent instance");
		try {
			sessionFactory.getCurrentSession().save(testRunConfigurationParent);
			log.debug("add successful");
		} catch (RuntimeException re) {
			log.error("add failed", re);
		}
		
	}

	@Override
	@Transactional
	public void delete(TestRunConfigurationParent testRunConfigurationParent) {
		log.debug("deleting TestRunConfigurationParent instance");
		try {
			sessionFactory.getCurrentSession().delete(testRunConfigurationParent);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
		}		
		
	}

	@Override
	@Transactional
	public TestRunConfigurationParent getByTestRunConfigurationParentId(
			int testRunConfigurationParentId) {
		log.debug("getting getByTestRunConfigurationParent instance by id");
		TestRunConfigurationParent testRunConfigurationParent=null;
		try {
			List list =  sessionFactory.getCurrentSession().createQuery("from TestRunConfigurationParent t where testRunConfigurationParentId=:testRunConfigurationParentId").setParameter("testRunConfigurationParentId",testRunConfigurationParentId)
					.list();
			testRunConfigurationParent=(list!=null && list.size()!=0)?(TestRunConfigurationParent)list.get(0):null;
			if(testRunConfigurationParent != null){
				Hibernate.initialize(testRunConfigurationParent.getTestRunConfigurationChilds());				
				Hibernate.initialize(testRunConfigurationParent.getProductMaster());
			}
			log.debug("getByUserId successful");
		} catch (RuntimeException re) {
			log.error("getByUserId failed", re);
		}
		return testRunConfigurationParent;
	}
	
	@Override
	@Transactional
	public boolean getTestRunConfigurationParentByName(String testRunconfigurationName) {
		String hql = "from TestRunConfigurationParent te where testRunconfigurationName = :testRunconfigurationName";
		List list = sessionFactory.getCurrentSession().createQuery(hql).setParameter("testRunconfigurationName", testRunconfigurationName).list();
		if (list != null  && !list.isEmpty()) 
		    return true;
		else 
			return false;
	}

	@Override
	@Transactional
	public int getTotalRecords() {
		log.debug("getting TestRunConfigurationParent total records");
		int count =0;
		try {
			count=((Number) sessionFactory.getCurrentSession().createSQLQuery("select count(*) from test_run_configuration_parent").uniqueResult()).intValue();
			
			log.debug("total records fetch successful");
		} catch (RuntimeException re) {
			log.error("total records fetch failed", re);			
		}
		return count;
	
	}

	@Override
	@Transactional
	public int getTotalRecords(int userId) {
		log.debug("getting TestRunConfigurationParent total records based on UserID");
		int count =0;
		try {
			count=((Number) sessionFactory.getCurrentSession().createSQLQuery("select count(*) from test_run_configuration_parent where userId=:userId").setParameter("userId",userId).uniqueResult()).intValue();
			
			log.debug("records fetch successful");
		} catch (RuntimeException re) {
			log.error("records fetch failed", re);			
		}
		return count;
	}

	@Override
	@Transactional
	public int getTotalRecords(int userId, int productId) {
		log.debug("getting TestRunConfigurationParent total records based on UserID & ProductId");
		int count =0;
		try {
			count=((Number) sessionFactory.getCurrentSession().createSQLQuery("select count(*) from test_run_configuration_parent where userId=:userID AND prodcutId=:productId").setParameter("userId",userId).setParameter("productId",productId).uniqueResult()).intValue();
			
			log.debug("records fetch successful");
		} catch (RuntimeException re) {
			log.error("records fetch failed", re);			
		}
		return count;
	}

	@Override
	@Transactional
	public List<TestRunConfigurationParent> listAll(int startIndex, int pageSize) {
		log.debug("listing TestRunConfigurationParent instance");
		List<TestRunConfigurationParent> testRunConfigurationParent=null;
		try {
			testRunConfigurationParent=sessionFactory.getCurrentSession().createQuery("from TestRunConfigurationParent")
	                .setFirstResult(startIndex)
	                .setMaxResults(pageSize)
	                .list();
			if (!(testRunConfigurationParent == null || testRunConfigurationParent.isEmpty())) {
				for (TestRunConfigurationParent trcp : testRunConfigurationParent) {
					Hibernate.initialize(trcp.getProductMaster());
					Hibernate.initialize(trcp.getUserList());
				}
			}
			log.debug("list successful");
			
			
		} catch (RuntimeException re) {
			log.error("list failed", re);
		}
		return testRunConfigurationParent;     
	}

	@Override
	@Transactional
	public List<TestRunConfigurationParent> list(int userId,
			int startIndex, int pageSize) {
		log.debug("listing TestRunConfigurationParent instance");
		List<TestRunConfigurationParent> testRunConfigurationParent=null;
		try {
			testRunConfigurationParent=sessionFactory.getCurrentSession().createQuery("from TestRunConfigurationParent where userId=:userId").setParameter("userId",testRunConfigurationParent)
	                .setFirstResult(startIndex)
	                .setMaxResults(pageSize)
	                .list();
			log.debug("list successful");
		} catch (RuntimeException re) {
			log.error("list failed", re);
		}
		return testRunConfigurationParent; 
	}

	@Override
	@Transactional
	public List<TestRunConfigurationParent> list(int userId, int productId,
			int startIndex, int pageSize) {
		log.debug("listing TestRunConfigurationParent instance");
		List<TestRunConfigurationParent> testRunConfigurationParent=null;
		try {
			testRunConfigurationParent=sessionFactory.getCurrentSession().createQuery("from TestRunConfigurationParent where userId=:userId AND productId=:productId")
					.setParameter("userId",testRunConfigurationParent)
					.setParameter("productId",productId)
	                .setFirstResult(startIndex)
	                .setMaxResults(pageSize)
	                .list();
			log.debug("list successful");
		} catch (RuntimeException re) {
			log.error("list failed", re);
		}
		return testRunConfigurationParent; 
	}

	@Override
	@Transactional
	public List<TestRunConfigurationParent> listAll() {
		log.debug("listing TestRunConfigurationParent instance");
		List<TestRunConfigurationParent> testRunConfigurationParent=null;
		try {
			testRunConfigurationParent=sessionFactory.getCurrentSession().createQuery("from TestRunConfigurationParent")
										.list();
			log.debug("list successful");
		} catch (RuntimeException re) {
			log.error("list failed", re);
		}
		return testRunConfigurationParent;     
	}



	@Override
	@Transactional
	public void update(TestRunConfigurationParent testRunConfigurationParent) {
		log.debug("updating TestRunConfigurationParent instance");
		try {
			sessionFactory.getCurrentSession().saveOrUpdate(testRunConfigurationParent);
			log.debug("add successful");
		} catch (RuntimeException re) {
			log.error("add failed", re);
		}
		
	}

	@Override
	@Transactional
	public List<TestRunConfigurationParent> list(int userId) {
		log.debug("listing TestRunConfigurationParent instance");
		List<TestRunConfigurationParent> testRunConfigurationParent=null;
		try {
			testRunConfigurationParent=sessionFactory.getCurrentSession().createQuery("from TestRunConfigurationParent where userId=:userId").setParameter("userId",userId)	               
	                .list();
			log.debug("list successful");
		} catch (RuntimeException re) {
			log.error("list failed", re);
		}
		return testRunConfigurationParent; 
	}

	@Override
	@Transactional
	public List<TestRunConfigurationParent> list(int userId, int productId) {		
			log.debug("listing TestRunConfigurationParent instance");
			List<TestRunConfigurationParent> testRunConfigurationParent=null;
			try {
				testRunConfigurationParent=sessionFactory.getCurrentSession().createQuery("from TestRunConfigurationParent where userId=:userId AND productId=:productId")
						.setParameter("userId",testRunConfigurationParent)
						.setParameter("productId",productId)
		                .list();
				log.debug("list successful");
			} catch (RuntimeException re) {
				log.error("list failed", re);
			}
			return testRunConfigurationParent; 
	}
	

}
