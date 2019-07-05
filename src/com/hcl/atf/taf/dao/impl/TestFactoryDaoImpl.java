package com.hcl.atf.taf.dao.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.Subqueries;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.hcl.atf.taf.constants.TAFConstants;
import com.hcl.atf.taf.dao.TestFactoryDao;
import com.hcl.atf.taf.dao.TestfactoryResourcePoolDAO;
import com.hcl.atf.taf.model.EngagementTypeMaster;
import com.hcl.atf.taf.model.ProductMaster;
import com.hcl.atf.taf.model.ProductMode;
import com.hcl.atf.taf.model.ProductUserRole;
import com.hcl.atf.taf.model.TestFactory;
import com.hcl.atf.taf.model.TestFactoryManager;
import com.hcl.atf.taf.model.TestfactoryResourcePool;
import com.hcl.atf.taf.model.WorkPackage;

@Repository
public class TestFactoryDaoImpl implements TestFactoryDao {
	
private static final Log log = LogFactory.getLog(TestFactoryDaoImpl.class);
	
@Autowired(required=true)
private SessionFactory sessionFactory;
@Autowired(required=true)
private TestfactoryResourcePoolDAO TestfactoryResourcePoolDAO;
@Override
@Transactional
public List<TestFactory> getTestFactoryList(int testFactoryLabId, int status, int filter) {
	if(status == TAFConstants.ENTITY_STATUS_ALL){
		return list(testFactoryLabId);
	}
	List<TestFactory> testFactoryList=null;
	try {
		
		Criteria c = sessionFactory.getCurrentSession().createCriteria(TestFactory.class, "testFactory");
		c.createAlias("testFactory.testFactoryLab", "tfl");
		c.createAlias("testFactory.engagementTypeMaster", "etm");
		if(testFactoryLabId != 0){
			c.add(Restrictions.eq("tfl.testFactoryLabId", testFactoryLabId));
		}
		if(filter == 1){
			c.add(Restrictions.eq("etm.engagementTypeId", 1));
		}else if(filter == 2){
			c.add(Restrictions.eq("etm.engagementTypeId", 2));
		}
		c.add(Restrictions.eq("testFactory.status", status));
		testFactoryList = c.list();
		
		if (!(testFactoryList == null || testFactoryList.isEmpty())){
			for (TestFactory testFactory : testFactoryList) {
				Hibernate.initialize(testFactory.getTestFactoryLab());
				Hibernate.initialize(testFactory.getTestfactoryResourcePoolList());
				Hibernate.initialize(testFactory.getEngagementTypeMaster());
				
			}
		}
		log.debug("list all successful");
	} catch (RuntimeException re) {
		log.error("list all failed", re);
	}
	
	
	return testFactoryList;
	
	
}


@Override
@Transactional
public List<TestFactory> list(int testFactoryLabId) {
	log.debug("listing specific TestFactory instance");
	List<TestFactory> testFactoryList=null;
	try {
		testFactoryList=sessionFactory.getCurrentSession().createQuery("from TestFactory where status=1 and testFactoryLabId=:testFactoryLabId")
													.setParameter("testFactoryLabId", testFactoryLabId).list();
		if (!(testFactoryList == null || testFactoryList.isEmpty())){
			for (TestFactory testFactory : testFactoryList) {
				Hibernate.initialize(testFactory.getTestFactoryLab());
				Hibernate.initialize(testFactory.getEngagementTypeMaster());
				Hibernate.initialize(testFactory.getTestfactoryResourcePoolList());
			}
		}
		log.debug("list specific successful");
	} catch (RuntimeException re) {
		log.error("list specific failed", re);
	}
	return testFactoryList;
}

@Override
@Transactional
public List<TestFactory> getTestFactoryListByLabAndUser(int testFactoryLabId, int status, int userId, int testFactoryId,int filterstatus) {
	if(status == TAFConstants.ENTITY_STATUS_ALL){
		return list(testFactoryLabId);
	}
	List<TestFactory> testFactoryList=new ArrayList<TestFactory>();
	List<TestFactoryManager> testFactoryManagerList = null;
	try {
		Criteria c = sessionFactory.getCurrentSession().createCriteria(TestFactoryManager.class,"testFactManager");	
		c.createAlias("testFactManager.testFactory", "testFact");
		c.createAlias("testFact.testFactoryLab", "lab");
		c.createAlias("testFactManager.userList", "user");
		if(testFactoryLabId > 0){
			c.add(Restrictions.eq("lab.testFactoryLabId", testFactoryLabId));	
		}		
		if(userId > 0){
			c.add(Restrictions.eq("user.userId", userId));
		}
		
		if(testFactoryId != -1){
			c.add(Restrictions.eq("testFact.testFactoryId", testFactoryId));
			
		}
		
		c.add(Restrictions.eqProperty("testFactManager.testFactory.testFactoryId", "testFact.testFactoryId"));
		if(filterstatus == 2){
			c.add(Restrictions.in("testFact.status", Arrays.asList(0, 1)));
		}else if(filterstatus != 2){
			c.add(Restrictions.eq("testFact.status", filterstatus));
		}
		testFactoryManagerList = c.list();
		for (TestFactoryManager testFactoryManager : testFactoryManagerList) {
			Hibernate.initialize(testFactoryManager.getTestFactory());
			Hibernate.initialize(testFactoryManager.getUserList());
			TestFactory tf = testFactoryManager.getTestFactory();
			Hibernate.initialize(tf.getTestFactoryLab());
			Hibernate.initialize(tf.getEngagementTypeMaster());
			Hibernate.initialize(tf.getTestfactoryResourcePoolList());
			testFactoryList.add(tf);
		}
		
		log.debug("TestFactoryList specific successful");
	} catch (RuntimeException re) {
		log.error("TestFactoryList specific failed", re);
	}
	return testFactoryList;
	
	
}
@Override
@Transactional
public TestFactory getTestFactoryById(Integer testFactoryId) {
	TestFactory testFactory=null;
	String hql = "from TestFactory  where testFactoryId = :testFactoryId";
	List instances = sessionFactory.getCurrentSession().createQuery(hql).setParameter("testFactoryId", testFactoryId).list();
	testFactory=(instances!=null && instances.size()!=0)?(TestFactory)instances.get(0):null;
	if (!(testFactory == null )){
			Hibernate.initialize(testFactory.getTestFactoryLab());
			Hibernate.initialize(testFactory.getTestfactoryResourcePoolList());
			Hibernate.initialize(testFactory.getEngagementTypeMaster());
	}
	return testFactory;
}


@Override
@Transactional
public List<TestFactory> getTestFactoryList() {
	log.debug("listing specific TestFactory instance");
	List<TestFactory> testFactoryList=null;
	try {
		testFactoryList=sessionFactory.getCurrentSession().createQuery("from TestFactory where status=1" ).list();
		if (!(testFactoryList == null || testFactoryList.isEmpty())){
			for (TestFactory testFactory : testFactoryList) {
				Hibernate.initialize(testFactory.getTestFactoryLab());
				Hibernate.initialize(testFactory.getEngagementTypeMaster());
				Hibernate.initialize(testFactory.getTestfactoryResourcePoolList());			
			}
		}
		log.debug("list specific successful");
	} catch (RuntimeException re) {
		log.error("list specific failed", re);
	}
	return testFactoryList;
}

@Override
@Transactional
public List<TestfactoryResourcePool> getResourcePoolListbyTestFactoryId(
		int testFactoryId) {
	log.info("Obtaining ResourcePool based on Testfactory ID.");
	List<TestfactoryResourcePool> rPoolList = new ArrayList<TestfactoryResourcePool>();
	Set<TestfactoryResourcePool> rPoolListSet = new HashSet<TestfactoryResourcePool>();
	List<TestFactory> testFactoryList=new ArrayList<TestFactory>();
	try {
		Criteria c = sessionFactory.getCurrentSession().createCriteria(TestFactory.class, "tf");
		c.add(Restrictions.eq("tf.testFactoryId", testFactoryId));
		c.add(Restrictions.eq("tf.status", 1));
		testFactoryList = c.list();		
		
		if (!(testFactoryList == null || testFactoryList.isEmpty())){
			for (TestFactory testFactory : testFactoryList) {				
				Hibernate.initialize(testFactory.getTestFactoryLab());
				Hibernate.initialize(testFactory.getTestfactoryResourcePoolList());				
					rPoolListSet.addAll(testFactory.getTestfactoryResourcePoolList());			
				}	
		}
		log.info("rPoolListSet size --"+rPoolListSet.size());
		
		for (TestfactoryResourcePool rpool : rPoolListSet) {
			
			rPoolList.add(rpool);
			
			log.info(" RP Added ---"+rpool.getResourcePoolName());
		}
		
		log.debug("list specific successful");
	} catch (RuntimeException re) {
		log.error("list specific failed", re);
	}
	return rPoolList;
}

@Override
@Transactional
public List<TestFactory> getTestFactoriesByTestFactoryManagerId(
		int testFactoryManagerId) {
	List<TestFactoryManager> listOfTestFactoryManager = null;
	List<TestFactory> listOfTestFactories = new ArrayList<TestFactory>();
	try {
		Criteria c = sessionFactory.getCurrentSession().createCriteria(TestFactoryManager.class, "tfm");
		c.createAlias("tfm.userList", "testFactoryManager");
		c.createAlias("tfm.testFactory", "testFactory");
		c.add(Restrictions.eq("testFactoryManager.userId", testFactoryManagerId));
		c.add(Restrictions.eq("testFactory.status", 1));
		listOfTestFactoryManager = c.list();
		log.info("Result Set Size of Test Factory Manager: " + listOfTestFactoryManager.size());
		
		for (TestFactoryManager testFactoryManager : listOfTestFactoryManager) {
			Hibernate.initialize(testFactoryManager.getTestFactory());
			Hibernate.initialize(testFactoryManager.getTestFactory().getTestFactoryLab());
			Hibernate.initialize(testFactoryManager.getTestFactory().getEngagementTypeMaster());
			listOfTestFactories.add(testFactoryManager.getTestFactory());
		}
	} catch (Exception e) {
		log.error("Getting TestFacories with Test Factory Manager Id", e);
	}
	return listOfTestFactories;
}


@Override
@Transactional
public List<TestFactory> getTestFactoriesByProductId(int productId) {
	List<ProductMaster> listOfProductMaster = null;
	List<TestFactory> listOfTestFactories = new ArrayList<TestFactory>();
	try {
		Criteria c = sessionFactory.getCurrentSession().createCriteria(ProductMaster.class, "product");
		c.add(Restrictions.eq("product.productId", productId));
		listOfProductMaster = c.list();
		for (ProductMaster productMaster : listOfProductMaster) {
			Hibernate.initialize(productMaster.getTestFactory());
			Hibernate.initialize(productMaster.getTestFactory().getTestFactoryLab());
			Hibernate.initialize(productMaster.getTestFactory().getEngagementTypeMaster());
			listOfTestFactories.add(productMaster.getTestFactory());
		}
		log.debug("Getting the list of Test Factories with which user is associated with via Product: "+productId+ " is "+listOfTestFactories.size());
	} catch (Exception e) {
		log.error("Getting the list of Test Factories with which user is associated with via Product: "+productId, e);
	}
	return listOfTestFactories;
}


@Override
@Transactional
public TestfactoryResourcePool mapRespoolTestfactory(Integer testFactoryId,
		Integer resourcePoolId, String action) {
	
	TestfactoryResourcePool testfactoryResourcePool=null;  
	TestFactory testFactory=null;
	try{
		testfactoryResourcePool=TestfactoryResourcePoolDAO.getbyresourcePoolId(resourcePoolId);
		testFactory=getTestFactoryById(testFactoryId);
		if (testfactoryResourcePool != null && testFactory != null) {
			boolean needToUpdateOrAdd = false;
			Set<TestFactory> testFactorySet=testfactoryResourcePool.getTestFactoryList();
 if(action.equalsIgnoreCase("Exist")){
				if(testFactorySet.contains(testFactory)){
					return null;
				}
			}
 else if (action.equalsIgnoreCase("Add")) {

				if (testFactorySet.size() == 0) {
					needToUpdateOrAdd = true;
				} else {
					log.info("size > 0");
					
					TestFactory testFactoryForProcessing=testFactorySet.iterator().next();
					if (testFactoryForProcessing != null) {
						log.info("testFactoryForProcessing not null");
						int alreadyAvailableTestFcatoryId = testFactoryForProcessing.getTestFactoryId().intValue();
							
						if (alreadyAvailableTestFcatoryId != testFactoryId) {
							log.debug("alreadyAvailableTestFcatoryId---------->"
											+ alreadyAvailableTestFcatoryId);
							log.debug("testFactoryId---------->"
									+ testFactoryId);
							log.debug("values or different");
							log.debug("testfactoryResourcePool.getTestFactoryList().size()="
									+ testfactoryResourcePool.getTestFactoryList()
											.size());

							TestFactory testFactoryAvailable = getTestFactoryById(alreadyAvailableTestFcatoryId);
							for (TestfactoryResourcePool resPool : testFactoryAvailable
									.getTestfactoryResourcePoolList()) {
								log.debug("resPool.getResourcePoolId().intValue()"
										+ resPool.getResourcePoolId().intValue());

								if (resPool.getResourcePoolId().intValue() == resourcePoolId) {
									log.debug("TestFactory found in Resource Pool");
									sessionFactory.getCurrentSession()
											.saveOrUpdate(
													testFactoryAvailable);
									log.debug("testFactoryAvailable.getTestfactoryResourcePoolList().size()="
											+ testFactoryAvailable
													.getTestfactoryResourcePoolList()
													.size());
									
									break;
								}
							}

							needToUpdateOrAdd = true;
						}
					}
				}

				if (needToUpdateOrAdd) {
					
					testfactoryResourcePool.getTestFactoryList().add(testFactory);
					testFactory.getTestfactoryResourcePoolList().add(testfactoryResourcePool);
					sessionFactory.getCurrentSession().saveOrUpdate(
							testfactoryResourcePool);
					sessionFactory.getCurrentSession().saveOrUpdate(
							testFactory);
					
				}
			}
			
		else if(action.equalsIgnoreCase("Remove")){

				log.debug("Remove TestFactory  from ResourcePool");

				try {
					log.debug("testFactoryId input: "  +testFactoryId);
					testfactoryResourcePool = (TestfactoryResourcePool) sessionFactory.getCurrentSession().get(TestfactoryResourcePool.class, resourcePoolId);
					if (testfactoryResourcePool == null) {
						log.debug("resourcePool with specified id not found : " + resourcePoolId);
						return null;
					}
					testFactory = (TestFactory) sessionFactory.getCurrentSession().get(TestFactory.class, testFactoryId);
					if (testFactory == null) {
						log.debug("testFactory could not found in the database : " + testFactoryId);
						log.debug("testFactory could not found in the database : " + testFactoryId);
						return null;
					}
					Set<TestFactory> tsetFactoryList =testfactoryResourcePool.getTestFactoryList();
					log.debug("TestFactory set size before removing :"+ tsetFactoryList.size());
					tsetFactoryList.remove(testFactory);
					log.debug("testFactory set size  after removing ::"+ tsetFactoryList.size());
					
					testfactoryResourcePool.setTestFactoryList(tsetFactoryList);
					
					sessionFactory.getCurrentSession().save(testfactoryResourcePool);
					
					log.debug("Removed TestFactory from ResPool successfully");
				} catch (RuntimeException re) {
					log.error("Failed to remove TestFactory from ResPool", re);
					
				}
			
			}
		}
		
		
	} catch (RuntimeException re) {
		log.error("list specific failed", re);
	}
	
	
	return testfactoryResourcePool;
  }


@Override
@Transactional
public int add(TestFactory testFactory) {
	log.debug("adding TestFactory instance");
	try {
		testFactory.setStatus(1);			
		sessionFactory.getCurrentSession().save(testFactory);
		log.debug("TestFactory added successfully");
	}catch (RuntimeException re) {
		log.error("add failed to TestFactory", re);
	}
	return testFactory.getTestFactoryId();
 }


@Override
@Transactional
public void update(TestFactory testFactory) {
	log.debug("updating TestFactory instance");
	try {
		sessionFactory.getCurrentSession().saveOrUpdate(testFactory);
		log.debug("TestFactory updateed successfully");
	} catch (RuntimeException re) {
		log.error(" TestFactory update failed", re);
	}	
}


@Override
public TestFactory getTestFactoryByWorkPackageId(int workPackageId) {
	TestFactory testFactory=null;
	List<WorkPackage> workPackageList = null;
	try {
		Criteria c = sessionFactory.getCurrentSession().createCriteria(WorkPackage.class, "wp");
		c.createAlias("wp.productBuild", "prodBuild");
		c.createAlias("prodBuild.productVersion", "prodVersion");
		c.createAlias("prodVersion.productMaster", "product");
		c.add(Restrictions.eqProperty("wp.productBuild.productBuildId", "prodBuild.productBuildId"));
		c.add(Restrictions.eqProperty("prodBuild.productVersion.productVersionListId", "prodVersion.productVersionListId"));
		c.add(Restrictions.eqProperty("prodVersion.productMaster.productId", "product.productId"));
		c.add(Restrictions.eq("wp.workPackageId", workPackageId));
		
		workPackageList = c.list();
		
		WorkPackage wp = (workPackageList!=null && workPackageList.size()!=0)?(WorkPackage)workPackageList.get(0):null;
		if (!(wp == null )){
			Hibernate.initialize(wp.getProductBuild().getProductVersion().getProductMaster().getTestFactory());
			testFactory = wp.getProductBuild().getProductVersion().getProductMaster().getTestFactory();
		}
		
		log.debug("Getting the Test Factory to which the WP: "+workPackageId +" belongs to via Product");
	} catch (Exception e) {
		log.error("Exception while getting the Test Factory to which the WP: "+workPackageId +" belongs to via Product", e);
	}
	return testFactory;
}


@Override
@Transactional
public List<TestFactory> listByTestFactoryId(int testFactoryId) {	
	List<TestFactory> testFactoryList=new ArrayList<TestFactory>();	
	try {
		Criteria c = sessionFactory.getCurrentSession().createCriteria(TestFactory.class,"testFactory");		
		if(testFactoryId != -1){
			c.add(Restrictions.eq("testFactory.testFactoryId", testFactoryId));	
		}		
		
		testFactoryList = c.list();
		for (TestFactory testFactory : testFactoryList) {
			Hibernate.initialize(testFactory.getTestFactoryLab());
			if(testFactory.getTestfactoryResourcePoolList() != null){
				 Set<TestfactoryResourcePool> resourcePoolSet = testFactory.getTestfactoryResourcePoolList();
				 Hibernate.initialize(resourcePoolSet);
				 for (TestfactoryResourcePool testfactoryResourcePool : resourcePoolSet) {
					Hibernate.initialize(testfactoryResourcePool.getTestFactoryLab());
					Hibernate.initialize(testfactoryResourcePool.getTestFactoryLab());
				}
			}
		}		
		log.debug("TestFactoryList specific successful");
	} catch (RuntimeException re) {
		log.error("TestFactoryList specific failed", re);
	}
	return testFactoryList;	
}

@Override
public List<TestFactory> listByTestFactoryIdAndLabId(int testFactoryId,
		int testFactoryLabId) {	
	List<TestFactory> testFactoryList=new ArrayList<TestFactory>();	
	try {
		Criteria c = sessionFactory.getCurrentSession().createCriteria(TestFactory.class,"testFact");		
		c.createAlias("testFact.testFactoryLab", "lab");
		if(testFactoryId != -1){
			c.add(Restrictions.eq("testFact.testFactoryId", testFactoryId));	
		}	
		if(testFactoryLabId != -1){
			c.add(Restrictions.eq("lab.testFactoryLabId", testFactoryLabId));	
		}
		
		testFactoryList = c.list();
		for (TestFactory testFactory : testFactoryList) {
			Hibernate.initialize(testFactory.getTestFactoryLab());
			Hibernate.initialize(testFactory.getEngagementTypeMaster());
			if(testFactory.getTestfactoryResourcePoolList() != null){
				 Set<TestfactoryResourcePool> resourcePoolSet = testFactory.getTestfactoryResourcePoolList();
				 Hibernate.initialize(resourcePoolSet);
				 for (TestfactoryResourcePool testfactoryResourcePool : resourcePoolSet) {
					Hibernate.initialize(testfactoryResourcePool.getTestFactoryLab());
					Hibernate.initialize(testfactoryResourcePool.getTestFactoryLab());
				}
			}
		}		
		log.debug("TestFactoryList specific successful");
	} catch (RuntimeException re) {
		log.error("TestFactoryList specific failed", re);
	}
	return testFactoryList;	
}

@Override
@Transactional
public boolean isTestFactoryExistingByName(TestFactory testFactory) {
	log.debug("Verifying if TestFactory name already exist");
	TestFactory testFactoryObj = new TestFactory();
	List<TestFactory> testFactoryList=null;	
	try {			
		Criteria crit = sessionFactory.getCurrentSession().createCriteria(TestFactory.class, "testFact");		
		
		crit.add(Restrictions.eq("testFact.testFactoryName", testFactory.getTestFactoryName().trim()) );
		crit.add(Restrictions.eq("testFact.engagementTypeMaster.engagementTypeId",  testFactory.getEngagementTypeMaster().getEngagementTypeId()));
		testFactoryList = crit.list();
		testFactoryObj = (testFactoryList!=null && testFactoryList.size()!=0)?(TestFactory)testFactoryList.get(0):null;
		if(testFactoryObj != null){
			if(testFactoryObj.getTestFactoryId() != -1){//TestFactory with same name already exists
				return true;
			}else{
				return false;
			}
		}
		if (!(testFactoryList == null || testFactoryList.isEmpty())){				
			for (TestFactory tf : testFactoryList) {
				Hibernate.initialize(tf.getTestFactoryLab());
				Hibernate.initialize(tf.getTestfactoryResourcePoolList());
			}
		}
		log.debug("Verifying if TestFactory name already exist");		
	} catch (RuntimeException re) {
		log.error("TestFactory with same name does not exists", re);	
	}
	return false;
}

@Override
@Transactional
public boolean isTestFactoryExistingByNameForUpdate(TestFactory testFactory,
		int testFactoryId) {
	log.debug("Verifying if TestFactory name already exist For given testFactoryId");
	TestFactory testFactoryObj = new TestFactory();
	List<TestFactory> testFactoryList=null;	
	try {			
		Criteria crit = sessionFactory.getCurrentSession().createCriteria(TestFactory.class, "testFact");			
		if(testFactoryId !=-1){
			crit.add(Restrictions.ne("testFact.testFactoryId", testFactoryId));			
		}
		crit.add(Restrictions.eq("testFact.testFactoryName", testFactory.getTestFactoryName().trim()));
		testFactoryList = crit.list();
		testFactoryObj = (testFactoryList!=null && testFactoryList.size()!=0)?(TestFactory)testFactoryList.get(0):null;
		if(testFactoryObj != null){
			if(testFactoryObj.getTestFactoryId() != -1){//TestFactory with same name already exists
				return true;
			}else{
				return false;
			}
		}
		if (!(testFactoryList == null || testFactoryList.isEmpty())){				
			for (TestFactory tf : testFactoryList) {				
				Hibernate.initialize(tf.getTestFactoryLab());
				Hibernate.initialize(tf.getTestfactoryResourcePoolList());
			}
		}
		log.debug("Verifying if TestFactory name already exist For given testFactoryId");		
	} catch (RuntimeException re) {
		log.error("TestFactory with same name does not exists For given testFactoryId", re);	
	}
	return false;
}

@Override
@Transactional
public TestFactory getTestFactoryByName(String testFactoryName) {
	log.debug("listing specific TestFactory instance");
	TestFactory testFactory=null;
	List<TestFactory> testFactoryList=null;	
	try {
		Criteria crit = sessionFactory.getCurrentSession().createCriteria(TestFactory.class, "testFact");		
		crit.add(Restrictions.eq("testFact.testFactoryName", testFactoryName));
		testFactoryList = crit.list();
		testFactory=(testFactoryList!=null && testFactoryList.size()!=0)?(TestFactory)testFactoryList.get(0):null;
		log.debug("list specific successful");
	} catch (RuntimeException re) {
		log.error("list specific failed", re);
	}
	return testFactory;
}

@Override
@Transactional
public List<EngagementTypeMaster> listEngagementTypes() {
	List<EngagementTypeMaster> engagementTypeList = null;		
	try {
		Criteria c = sessionFactory.getCurrentSession().createCriteria(EngagementTypeMaster.class, "engagementType");		
		engagementTypeList = c.list();
	} catch (HibernateException e) {
		log.error("ERROR  ",e);
	}		
	return engagementTypeList;
}

@Override
@Transactional
public int  getEngagementTypeIdBytestfactoryId(int testfactoryId) {
	log.info("Fetching Engagement Id for Testfactory");
	Integer engagementTypeId = 0;
	String sql = "";	
	List<Object[]> listFromDB = new ArrayList<Object[]>();
	try {
		sql = "select tf.engagementTypeId from test_factory tf where testFactoryId =:tfid";
		engagementTypeId = ((Number)sessionFactory.getCurrentSession().createSQLQuery(sql).setParameter("tfid", testfactoryId).uniqueResult()).intValue();
	} catch (HibernateException e) {
		log.error("Unable to fetch Engagement Id ", e);
	}	
	return engagementTypeId;
}

@Override
@Transactional
public List<ProductMode> getmodelist() {
	List<ProductMode> modeList=null;
	try {
		modeList=sessionFactory.getCurrentSession().createQuery("from ProductMode where status=1" ).list();
		log.debug("list specific successful");	
		
	} catch (RuntimeException re) {
		log.error("list specific failed", re);
	}
	return modeList;
}

@Override
@Transactional
public EngagementTypeMaster getEngagementTypeById(int engagementTypeId) {
	List<EngagementTypeMaster> engagementTypeMasterList = null;		
	EngagementTypeMaster engagementTypeMaster = null;
	try {
		Criteria c = sessionFactory.getCurrentSession().createCriteria(EngagementTypeMaster.class, "engagementType");		
		c.add(Restrictions.eq("engagementType.engagementTypeId", engagementTypeId));
		engagementTypeMasterList = c.list();
		engagementTypeMaster = (engagementTypeMasterList!=null && engagementTypeMasterList.size()!=0)?(EngagementTypeMaster)engagementTypeMasterList.get(0):null;
	} catch (HibernateException e) {
		log.error("ERROR  ",e);
	}		
	return engagementTypeMaster;
}


@Override
@Transactional
public TestFactory getResourcePoolShowHideTab(Integer testFactoryId) {
	log.debug("Getting resourcePool Tab to show by testfactory id");
	TestFactory testFactoryshowHideTab = null;
	List<TestFactory> testFactoryshowHideTabList = new ArrayList<TestFactory>();
	try {
	
		Criteria c = sessionFactory.getCurrentSession().createCriteria(TestFactory.class, "testFactory");
		if (testFactoryId != 0){
			c.add(Restrictions.eq("testFactory.testFactoryId",  testFactoryId));
		}
		
		c.add(Restrictions.eq("testFactory.engagementTypeMaster.engagementTypeId",  1));
		
		testFactoryshowHideTabList = c.list();	
		
		if (testFactoryshowHideTabList == null) {
			log.debug("Resource Pool Tab with testfactory ID  : " + testFactoryId);
		}			
		testFactoryshowHideTab=(testFactoryshowHideTabList!=null && testFactoryshowHideTabList.size()!=0)?(TestFactory)testFactoryshowHideTabList.get(0):null;			
		
		log.debug("Got Product Core resource Tab");
	} catch (RuntimeException re) {
		log.error("Failed to get Product Core resource Tab", re);
	}
	
	return testFactoryshowHideTab;
}


@Override
@Transactional
public Integer countAllTestFactory(Date startDate,Date endDate) {
	
	try {
		Criteria c = sessionFactory.getCurrentSession().createCriteria(TestFactory.class,"testFactory");
		if (startDate != null) {
			c.add(Restrictions.ge("testFactory.createdDate", startDate));
		}
		if (endDate != null) {
			c.add(Restrictions.le("testFactory.createdDate", endDate));
		}
		
		c.setProjection(Projections.rowCount());
		Integer count = Integer.parseInt(c.uniqueResult().toString());
		return count;
	} catch (Exception e) {
		log.error("Unable to get count of all TestFactory", e);
		return -1;
	}
}


	@Override
	@Transactional
	public List<TestFactory> listAllTestFactoryByLastSyncDate(int startIndex,int pageSize, Date startDate,Date endDate) {
		log.debug("listing all TestFactory");
		List<TestFactory> testFactories=null;
		try {
			Criteria c = sessionFactory.getCurrentSession().createCriteria(TestFactory.class, "testFactory");
			if (startDate != null) {
				c.add(Restrictions.ge("testFactory.createdDate", startDate));
			}
			if (endDate != null) {
				c.add(Restrictions.le("testFactory.createdDate", endDate));
			}
			
			c.addOrder(Order.asc("testFactoryId"));
	        c.setFirstResult(startIndex);
	        c.setMaxResults(pageSize);
	        testFactories = c.list();		
			
			if (!(testFactories == null || testFactories.isEmpty())){
				for (TestFactory testFactory : testFactories) {
					Hibernate.initialize(testFactory.getTestFactoryLab());
					Hibernate.initialize(testFactory.getTestfactoryResourcePoolList());
					Hibernate.initialize(testFactory.getEngagementTypeMaster());
					
				}
			}
			log.debug("list all successful");
		} catch (RuntimeException re) {
			log.error("list all failed", re);
		}
		return testFactories;
	}
	
	
	@Override
	@Transactional
	public List<TestFactory> getEngagementByUserAndCustomerProduct(Integer userId, Integer userRoleId, Integer customerId, Integer activeStatus) {
		List<TestFactory> testFactories = null;
		try{
			Criteria criteria = sessionFactory.getCurrentSession().createCriteria(TestFactory.class, "testFactory");
			DetachedCriteria detachedCriteria = null;
			if(userRoleId == 3 || userRoleId == 4 || userRoleId == 5){
				detachedCriteria = DetachedCriteria.forClass(ProductUserRole.class, "productUserRole");
				detachedCriteria.createAlias("productUserRole.user", "user");
				detachedCriteria.createAlias("productUserRole.product", "product");
				detachedCriteria.createAlias("product.customer", "customer");
				detachedCriteria.createAlias("product.testFactory", "productTestFactory");
				detachedCriteria.add(Restrictions.eq("user.userId", userId));
				detachedCriteria.add(Restrictions.eq("customer.customerId", customerId));
				detachedCriteria.setProjection(Property.forName("productTestFactory.testFactoryId"));
				
			}else{
				detachedCriteria = DetachedCriteria.forClass(ProductMaster.class, "product");
				detachedCriteria.createAlias("product.customer", "customer");
				detachedCriteria.createAlias("product.testFactory", "productTestFactory");
				detachedCriteria.add(Restrictions.eq("customer.customerId", customerId));
				detachedCriteria.setProjection(Property.forName("productTestFactory.testFactoryId"));
				
			}
			criteria.add(Subqueries.propertyIn("testFactory.testFactoryId", detachedCriteria));
			
			testFactories = criteria.list();
			
		}catch(Exception ex){
			log.error("Error in getEngagementByUserAndCustomerProduct - ", ex);
		}
		return testFactories;
	}

	@Override
	@Transactional
	public List<TestFactory> getEngagementListByUserId(Integer userId) {
		List<TestFactory> testFactories = null;
		try{
			Criteria criteria = sessionFactory.getCurrentSession().createCriteria(TestFactory.class, "testFactory");
			if(userId != null){
				DetachedCriteria detachedCriteria = DetachedCriteria.forClass(ProductUserRole.class, "productUserRole");
				detachedCriteria.createAlias("productUserRole.user", "user");
				detachedCriteria.createAlias("productUserRole.product", "product");
				detachedCriteria.createAlias("product.testFactory", "productTestFactory");
				detachedCriteria.add(Restrictions.eq("user.userId", userId));
				detachedCriteria.setProjection(Property.forName("productTestFactory.testFactoryId"));
				criteria.add(Subqueries.propertyIn("testFactory.testFactoryId", detachedCriteria));
			}			
			testFactories = criteria.list();
			
		}catch(Exception ex){
			log.error("Error in getCustomFieldEngagementList - ", ex);
		}
		return testFactories;
	}

	@Override
	@Transactional
	public int addEngagement(EngagementTypeMaster engagementTypeMaster) {
		log.debug("adding Engagement");
		try {
			sessionFactory.getCurrentSession().save(engagementTypeMaster);
			log.debug("add successful");
		} catch (RuntimeException re) {
			log.error("add failed", re);
		}
		return engagementTypeMaster.getEngagementTypeId();
	}
	
}