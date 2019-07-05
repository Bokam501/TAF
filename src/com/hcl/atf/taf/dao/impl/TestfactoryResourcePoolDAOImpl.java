package com.hcl.atf.taf.dao.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.hcl.atf.taf.dao.TestfactoryResourcePoolDAO;
import com.hcl.atf.taf.model.TestFactory;
import com.hcl.atf.taf.model.TestFactoryLab;
import com.hcl.atf.taf.model.TestfactoryResourcePool;
import com.hcl.atf.taf.model.UserList;
import com.hcl.atf.taf.model.UserResourcePoolMapping;

@Repository
public class TestfactoryResourcePoolDAOImpl implements TestfactoryResourcePoolDAO {
	private static final Log log = LogFactory.getLog(TestfactoryResourcePoolDAOImpl.class);
	
	@Autowired(required=true)
    private SessionFactory sessionFactory;

	@Override
	@Transactional
	public List<TestfactoryResourcePool> listResourcePool() {
		log.debug("listing all TestfactoryResourcePool instance");
		List<TestfactoryResourcePool> resourcePool=null;
		List<TestfactoryResourcePool> resourcePoolList = new ArrayList<TestfactoryResourcePool>();
		try {
			
			resourcePool=sessionFactory.getCurrentSession().createQuery("from TestfactoryResourcePool rp where rp.status=1").list();
			if (!(resourcePool == null || resourcePool.isEmpty())){
				for (TestfactoryResourcePool tresourcePool : resourcePool) {
					if(tresourcePool.getResourcePoolId() != -10){
						resourcePoolList.add(tresourcePool);
						Hibernate.initialize(tresourcePool.getTestFactoryLab());	
					}
				}
			}
			log.debug("list all successful");
		} catch (RuntimeException re) {
			log.error("list all failed", re);
			//throw re;
		}
		return resourcePoolList;
	}
	
	@Override
	@Transactional
	public List<TestfactoryResourcePool> listResourcePoolbyID(int resourcePoolId) {		
		return null;
	}

	@Override
	@Transactional
	public List<TestfactoryResourcePool> listResourcePoolbytestFactoryLabId(
			int testFactoryLabId) {
		log.debug("Listing TestfactoryResourcePool by LabID");
		List<TestfactoryResourcePool> resourcePool=null;
		List<TestfactoryResourcePool> resourcePoolList = new ArrayList<TestfactoryResourcePool>();
		try {
			
			resourcePool=sessionFactory.getCurrentSession().createQuery("from TestfactoryResourcePool rp where rp.testFactoryLab.testFactoryLabId=:labID and rp.status=1")
					.setParameter("labID", testFactoryLabId).list();
			if (!(resourcePool == null || resourcePool.isEmpty())){
				for (TestfactoryResourcePool tresourcePool : resourcePool) {
					if(tresourcePool.getResourcePoolId() != -10){
						resourcePoolList.add(tresourcePool);
						Hibernate.initialize(tresourcePool.getTestFactoryLab());
					}
				}
			}
			log.debug("list all successful");
		} catch (RuntimeException re) {
			log.error("list all failed", re);
		}
		return resourcePoolList;
	}

	@Override
	@Transactional
	public TestfactoryResourcePool getResourcePoolbytestFactoryLabId(
			int testFactoryLabId) {
		log.info("Getting TestfactoryResourcePoolby LabID instance");
		TestfactoryResourcePool resourcePool=null;
		
		try {			
			List list=sessionFactory.getCurrentSession().createQuery("from TestfactoryResourcePool rp where rp.testFactoryLab.testFactoryLabId=:labID and rp.status=1")
					.setParameter("labID", testFactoryLabId).list();
			resourcePool=(list!=null && list.size()!=0)?(TestfactoryResourcePool)list.get(0):null;
			log.debug("list all successful");
		} catch (RuntimeException re) {
			log.error("list all failed", re);
		}
		return resourcePool;
	}
	
	@Override
	@Transactional
	public void addResourcePool(TestfactoryResourcePool resourcePool) {
		log.info("adding ResourcePool instance");
		try {
			resourcePool.setStatus(1);
			sessionFactory.getCurrentSession().save(resourcePool);
			log.debug("adding ResourcePool successful");
		} catch (RuntimeException re) {
			log.error("adding ResourcePool failed", re);
		}
		
	}

	@Override
	@Transactional
	public TestfactoryResourcePool getbyresourcePoolId(int resourcePoolId) {
		log.debug("getting TestfactoryResourcePool instance by id");
		TestfactoryResourcePool resourcePool=null;
		try {			
			List list =  sessionFactory.getCurrentSession().createQuery("from TestfactoryResourcePool rp where rp.resourcePoolId=:resourcepool_id").setParameter("resourcepool_id", resourcePoolId)
					.list();
			resourcePool=(list!=null && list.size()!=0)?(TestfactoryResourcePool)list.get(0):null;
			if (!(resourcePool == null )){
				
					
					Hibernate.initialize(resourcePool.getTestFactoryList());
					Set<TestFactory> tfList=resourcePool.getTestFactoryList();
					for(TestFactory tf:tfList){
						Hibernate.initialize(tf.getTestfactoryResourcePoolList());
						Hibernate.initialize(tf.getEngagementTypeMaster());
					}
					Hibernate.initialize(resourcePool.getTestFactoryLab());
					
			
					
				}
			
			log.debug("getbyresourcePoolId successful");
		} catch (RuntimeException re) {
			log.error("getbyresourcePoolId failed", re);
		}
		return resourcePool;        
	}

	@Override
	@Transactional
	public void updateResourcePool(TestfactoryResourcePool resourcePool) {
		log.info("updating TestfactoryResourcePool instance");
		try {			
			log.info("ResourcePool Id in Dao--->"+resourcePool.getResourcePoolId());		
			sessionFactory.getCurrentSession().update(resourcePool);
			
			log.debug("update successful");  
		} catch (RuntimeException re) {
			log.error("update failed", re);
		}		
	}

	@Override
	@Transactional
	public List<TestfactoryResourcePool> listResourcePoolByResourceManagerId(
			int userRoleId, int userId) {
		List<UserList> listOfUser = null;
		List<TestfactoryResourcePool> listOfTestfactoryResourcePool = new ArrayList<TestfactoryResourcePool>();
		try {
			Criteria c = sessionFactory.getCurrentSession().createCriteria(UserList.class, "user");
			c.createAlias("user.userRoleMaster", "userRoleMaster");
			c.add(Restrictions.eq("user.userId", userId));
			c.add(Restrictions.eq("userRoleMaster.userRoleId", userRoleId));
			listOfUser = c.list();
			log.info("Result Set Size of Resource Manager list: " + listOfUser.size());
			
			for (UserList userList : listOfUser) {
				Hibernate.initialize(userList.getResourcePool());
				if(userList.getResourcePool().getResourcePoolId() != -10){
					listOfTestfactoryResourcePool.add(userList.getResourcePool());
				}
			}
		} catch (Exception e) {
			log.error("Getting Products with user is associated with via Work Package", e);
		}
		return listOfTestfactoryResourcePool;
	}

	@Override
	@Transactional
	public List<TestfactoryResourcePool> listResourcePoollistbyId(int testFactoryLabId, int resourcePoolId) {
		log.info("listing all ResourcePool Id instance");
		List<TestfactoryResourcePool> resourcePool=null;
		try {
			
			Criteria c = sessionFactory.getCurrentSession().createCriteria(TestfactoryResourcePool.class, "testFactory");
			c.add(Restrictions.eq("testFactory.testFactoryLab.testFactoryLabId", testFactoryLabId));
			c.add(Restrictions.eq("testFactory.status", 1));
			c.add(Restrictions.eq("testFactory.resourcePoolId", resourcePoolId));
			resourcePool = c.list();
			
			if (!(resourcePool == null || resourcePool.isEmpty())){
				for (TestfactoryResourcePool tresourcePool : resourcePool) {
					Hibernate.initialize(tresourcePool.getTestFactoryLab());				
				}
			}
			log.debug("list all successful");
		} catch (RuntimeException re) {
			log.error("list all failed", re);
		}
		return resourcePool;
	}

	@Override
	@Transactional
	public List<TestfactoryResourcePool> listResourcePoolByTestFactoryLabId(int testFactoryLabId) {
		log.info("listing all ResourcePool Id instance");
		List<TestfactoryResourcePool> resourcePool=null;
		try {
			
			Criteria c = sessionFactory.getCurrentSession().createCriteria(TestfactoryResourcePool.class, "testFactory");
			c.add(Restrictions.eq("testFactory.testFactoryLab.testFactoryLabId", testFactoryLabId));
			c.add(Restrictions.eq("testFactory.status", 1));
			resourcePool = c.list();
			
			if (!(resourcePool == null || resourcePool.isEmpty())){
				for (TestfactoryResourcePool tresourcePool : resourcePool) {
					Hibernate.initialize(tresourcePool.getTestFactoryLab());				
				}
			}
			log.debug("list all successful");
		} catch (RuntimeException re) {
			log.error("list all failed", re);
		}
		return resourcePool;
	}

	@Override
	@Transactional
	public TestFactoryLab getTestFactoryLabIdByUserResourcePool(Integer userId) {
		log.info("listing all ResourcePool Id instance");
		UserList user = null;
		List<UserList> userList = null;
		TestFactoryLab testFactoryLab = null;
		try {	
			Criteria c = sessionFactory.getCurrentSession().createCriteria(UserList.class, "user");
			c.add(Restrictions.eq("user.userId", userId));
			userList = c.list();
			user=(userList!=null && userList.size()!=0)?(UserList)userList.get(0):null;
			if(user != null){
				Hibernate.initialize(user.getResourcePool().getTestFactoryLab());	
				testFactoryLab = user.getResourcePool().getTestFactoryLab();
			}
			log.debug("list all successful");
		} catch (RuntimeException re) {
			log.error("list all failed", re);
		}
		return testFactoryLab;
		
	}

	@Override
	@Transactional
	public TestfactoryResourcePool getTestFactoryResourcePoolByName(String testFactoryResourcePoolName) {
		log.info("Getting TestfactoryResourcePoolby LabID instance");
		TestfactoryResourcePool resourcePool=null;
		
		try {			
			List list=sessionFactory.getCurrentSession().createQuery("from TestfactoryResourcePool rp where rp.resourcePoolName=:rpName and rp.status=1")
					.setParameter("rpName", testFactoryResourcePoolName).list();
			resourcePool=(list!=null && list.size()!=0)?(TestfactoryResourcePool)list.get(0):null;
			log.debug("list all successful");
		} catch (RuntimeException re) {
			log.error("listing TestFactoryResourcePool By Name failed", re);
		}
		return resourcePool;
	}

	@Override
	@Transactional
	public List<Integer> getTestfactoryResourcePoolListbyTestFactoryId(
			Integer testFactoryId) {
		List<Integer> testFactoryResPoolIds=null;
		try {
			String sql ="SELECT resourcePoolId FROM test_factory_resource_pool_has_test_factory tfrspool WHERE tfrspool.testFactoryId="+testFactoryId;
			testFactoryResPoolIds = (sessionFactory.getCurrentSession().createSQLQuery(sql).list());
		} catch (HibernateException e) {
			log.error("Getting TestfactoryResourcePoolList by TestFactoryId failed", e);
		}		
		return testFactoryResPoolIds;
	}

	@Override
	@Transactional
	public boolean checkResourceAssignedToRPforWeek(Integer userId,Integer resourcePoolId, Date weekDate) {
		boolean available = false;
		try{
			
			List<UserResourcePoolMapping> list = new ArrayList<UserResourcePoolMapping>();
			
			Criteria c = sessionFactory.getCurrentSession().createCriteria(UserResourcePoolMapping.class, "urpm")
			  .add(Restrictions.eq("urpm.resourcepoolId.resourcePoolId", resourcePoolId))
			  .add(Restrictions.le("urpm.fromDate",weekDate))
			  .add(Restrictions.ge("urpm.toDate",weekDate))
			 .add(Restrictions.ge("urpm.userId.userId",userId));
			list = c.list();
			if(list != null && list.size()>0){
				available = true;
			}
			
		}catch(Exception ex){
			log.error("unable to fetch ", ex);
		}
		return available;
	}
	
	
}
