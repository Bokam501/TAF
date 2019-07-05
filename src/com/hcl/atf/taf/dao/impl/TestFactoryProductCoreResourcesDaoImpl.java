package com.hcl.atf.taf.dao.impl;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.hcl.atf.taf.controller.utilities.DateUtility;
import com.hcl.atf.taf.dao.TestFactoryProductCoreResourcesDao;
import com.hcl.atf.taf.model.TestFactoryProductCoreResource;
@Repository
public class TestFactoryProductCoreResourcesDaoImpl implements TestFactoryProductCoreResourcesDao{
	private static final Log log = LogFactory.getLog(TestFactoryProductCoreResourcesDaoImpl.class);
	@Autowired(required=true)
    private SessionFactory sessionFactory;
	@SuppressWarnings("unchecked")
	@Transactional
	@Override
	public List<TestFactoryProductCoreResource> getProductCoreResourcesList(
			int productId, Integer jtStartIndex, Integer jtPageSize) {
		Integer status=1;
		log.debug("listing listProductUserRole instance");
		List<TestFactoryProductCoreResource> productCoreResList = null;
		try {
						
			Criteria c = sessionFactory.getCurrentSession().createCriteria(TestFactoryProductCoreResource.class, "testFactoryCoreRes");
			c.createAlias("testFactoryCoreRes.productMaster", "prodMaster");
			c.add(Restrictions.eq("prodMaster.productId", productId));
			c.add(Restrictions.eq("testFactoryCoreRes.status", status));
			
			if(jtStartIndex != null && jtPageSize != null)
				c.setFirstResult(jtStartIndex).setMaxResults(jtPageSize);
			
			productCoreResList = c.list();
			
			if (!(productCoreResList == null || productCoreResList
					.isEmpty())) {
				for (TestFactoryProductCoreResource productCoreRes : productCoreResList) {
					Hibernate.initialize(productCoreRes.getProductMaster());
					Hibernate.initialize(productCoreRes.getUserRole());
					Hibernate.initialize(productCoreRes.getUserList());
				}
			}
			log.debug("list successful");
		} catch (RuntimeException re) {
			log.error("list failed", re);
			// throw re;
		}
		return productCoreResList;
	}
	
	@Transactional
	@Override
	public Map<Integer, Integer> getProductCoreResourcesCountByRole(int productId) {		
		log.debug("listing Product Core Resources CountByRole");		
		Map<Integer, Integer> productCoreResourcesRoleCount = new HashMap<Integer, Integer>();
		List<Object[]> listFromDb = new ArrayList<Object[]>();
		String sql="";
		Query query = null;
		try {
			sql ="select count(tfpcr.userRoleId) as userroleidcount , tfpcr.userRoleId from test_factory_product_core_resource tfpcr "+
					"where productId=:pid  GROUP BY tfpcr.userRoleId";
			query = sessionFactory.getCurrentSession().createSQLQuery(sql).setParameter("pid",productId );
			listFromDb = query.list();
			for(Object[] row:listFromDb){
				Integer  rolecount = ((BigInteger)row[0]).intValue();
				Integer  roleid= (Integer)row[1];
				productCoreResourcesRoleCount.put(roleid, rolecount);
			}						
			log.debug("list successful");
		} catch (RuntimeException re) {
			log.error("list failed", re);
			// throw re;
		}
		return productCoreResourcesRoleCount;
	
	}
	
	@Transactional
	@Override
	public void add(TestFactoryProductCoreResource coreResouce) {
		log.debug("adding ProductCore Resouce");
		try {
			coreResouce.setStatus(1);
			sessionFactory.getCurrentSession().save(coreResouce);
			log.debug("add successful");
		} catch (RuntimeException re) {
			log.error("add failed", re);
		}
		
	}
	@Transactional
	@Override
	public TestFactoryProductCoreResource getCoreResourceById(
			Integer testFactoryProductCoreResourceId) {
		TestFactoryProductCoreResource coreRes=null;
		String hql = "from TestFactoryProductCoreResource coreRes where coreRes.testFactoryProductCoreResourceId = :testFactoryProductCoreResourceId";
		List instances = sessionFactory.getCurrentSession().createQuery(hql).setParameter("testFactoryProductCoreResourceId", testFactoryProductCoreResourceId).list();
		coreRes=(instances!=null && instances.size()!=0)?(TestFactoryProductCoreResource)instances.get(0):null;
		
		return coreRes;
		
	}
	@Transactional
	@Override
	public void upadte(TestFactoryProductCoreResource coreResourceFromUI) {
		log.debug("updating Product CoreResorce instance");
		
		try {
							
			sessionFactory.getCurrentSession().update(coreResourceFromUI);
			
			log.debug("update successful");  
		} catch (RuntimeException re) {
			log.error("update failed", re);
		} 
	}
	
	@Override
	@Transactional
	public Boolean isUserAlreadyCoreResource(Integer productId, Integer userId, Date fromDate, Date toDate,TestFactoryProductCoreResource coreResourceFromDB) {
		
		log.debug("isUserAlreadyCoreResource");
		log.info("From Date -> "+fromDate+" and ToDate --> "+toDate);
		List<TestFactoryProductCoreResource> coreResources=null;	
		try {			
			Criteria crit = sessionFactory.getCurrentSession().createCriteria(TestFactoryProductCoreResource.class, "productCoreResource");	
			crit.createAlias("productCoreResource.productMaster", "productMaster");
			crit.createAlias("productCoreResource.userList", "userList");
			if(productId >= 1){
				if(coreResourceFromDB != null){
					crit.add(Restrictions.ne("productCoreResource.testFactoryProductCoreResourceId", coreResourceFromDB.getTestFactoryProductCoreResourceId()));
				}
				crit.add(Restrictions.eq("userList.userId", userId));
				crit.add(Restrictions.eq("productCoreResource.status", 1));
				crit.add(Restrictions.disjunction().add(Restrictions.or( Restrictions.le("productCoreResource.fromDate", fromDate ),Restrictions.le("productCoreResource.fromDate", toDate ), Restrictions.eq("productCoreResource.fromDate", fromDate),Restrictions.eq("productCoreResource.fromDate", toDate))));
				crit.add(Restrictions.disjunction().add(Restrictions.or( Restrictions.ge("productCoreResource.toDate", toDate ), Restrictions.ge("productCoreResource.toDate", fromDate ) ,Restrictions.eq("productCoreResource.toDate", fromDate),Restrictions.eq("productCoreResource.fromDate", toDate))));
			}
			coreResources = crit.list();
			log.info("Overlapping Core Resource Assignments -->"+coreResources.size());
			for (TestFactoryProductCoreResource testFactoryProductCoreResource : coreResources) {
				log.info("Overlapping Core Resource Assignments --> "+testFactoryProductCoreResource.getProductMaster().getProductName()+" === " +testFactoryProductCoreResource.getFromDate() +"  -  " + testFactoryProductCoreResource.getToDate() );
			}
			if (coreResources == null || coreResources.isEmpty()) 
			    return false;
			else 
				return true;
		} catch(Exception e) {
			log.error("Problem finding if the resource is already a core resource", e);
			return true;
		}
	}

	@Override
	@Transactional
	public boolean isUserAProductCoreResource(int userId, int productId, String allocationDate) {
		List<TestFactoryProductCoreResource> listOfProductCoreResources = null;
		boolean isProductCoreResource = false;
		try {
			Criteria c = sessionFactory.getCurrentSession().createCriteria(TestFactoryProductCoreResource.class, "pcr");
			c.createAlias("pcr.userList", "user");
			c.createAlias("pcr.productMaster", "product");
			c.add(Restrictions.eq("user.userId", userId));
			c.add(Restrictions.eq("product.productId", productId));
			if(allocationDate!=null && !allocationDate.equals("") && allocationDate.contains("/")){
				c.add(Restrictions.le("pcr.fromDate",DateUtility.dateformatWithOutTime(allocationDate)));
				c.add(Restrictions.ge("pcr.toDate",DateUtility.dateformatWithOutTime(allocationDate)));
			}else{
				if(allocationDate!=null && !allocationDate.equals("")){
					c.add(Restrictions.le("pcr.fromDate",DateUtility.dateFormatWithOutSeconds(allocationDate)));
					c.add(Restrictions.ge("pcr.toDate",DateUtility.dateFormatWithOutSeconds(allocationDate)));
				}
			}
			c.add(Restrictions.eq("user.status", 1));
			listOfProductCoreResources = c.list();
			log.info("Result Set Size : " + listOfProductCoreResources.size());
			if(listOfProductCoreResources != null && !(listOfProductCoreResources.isEmpty())){
				TestFactoryProductCoreResource tfProductCoreRes = (TestFactoryProductCoreResource)listOfProductCoreResources.get(0);
				if(tfProductCoreRes != null){
					isProductCoreResource = true;
				}
			}
		} catch (Exception e) {
			log.error("update failed", e);
			return false;
		}
		return isProductCoreResource;
	}
	
	

	@Override
	@Transactional
	public List<TestFactoryProductCoreResource> getProductCoreResourcesByRole(
			int productId, Integer roleId) {
		Integer status=1;
		log.debug("listing listProductUserRole instance");
		List<TestFactoryProductCoreResource> productCoreResList = null;
		try {
			Criteria c = sessionFactory.getCurrentSession().createCriteria(TestFactoryProductCoreResource.class, "testFactoryCoreRes");
			c.createAlias("testFactoryCoreRes.productMaster", "prodMaster");
			c.createAlias("testFactoryCoreRes.userRole", "userRole");
			c.add(Restrictions.eq("prodMaster.productId", productId));
			c.add(Restrictions.eq("testFactoryCoreRes.status", status));
			c.add(Restrictions.eq("userRole.userRoleId",roleId));
			productCoreResList = c.list();
			
			if (!(productCoreResList == null || productCoreResList
					.isEmpty())) {
				for (TestFactoryProductCoreResource productCoreRes : productCoreResList) {
					Hibernate.initialize(productCoreRes.getProductMaster());
					Hibernate.initialize(productCoreRes.getUserRole());
					Hibernate.initialize(productCoreRes.getUserList());
				}
			}
			log.debug("list successful");
		} catch (RuntimeException re) {
			log.error("list failed", re);
		}
		return productCoreResList;
	}
	
	@Override
	@Transactional
	public int getProductRoleOfProductCoreResource(int userId, int productId, String allocationDate) {
		List<TestFactoryProductCoreResource> listOfProductCoreResources = null;
		int  productSpecificRoleId = 0;
		try {
			Criteria c = sessionFactory.getCurrentSession().createCriteria(TestFactoryProductCoreResource.class, "pcr");
			c.createAlias("pcr.userList", "user");
			c.createAlias("pcr.productMaster", "product");
			c.add(Restrictions.eq("pcr.status", 1));
			c.add(Restrictions.eq("user.userId", userId));
			c.add(Restrictions.eq("product.productId", productId));
			if(allocationDate!=null && !allocationDate.equals("") && allocationDate.contains("/")){
				c.add(Restrictions.le("pcr.fromDate",DateUtility.dateformatWithOutTime(allocationDate)));
				c.add(Restrictions.ge("pcr.toDate",DateUtility.dateformatWithOutTime(allocationDate)));
			}else{
				if(allocationDate!=null && !allocationDate.equals("")){
					c.add(Restrictions.le("pcr.fromDate",DateUtility.dateFormatWithOutSeconds(allocationDate)));
					c.add(Restrictions.ge("pcr.toDate",DateUtility.dateFormatWithOutSeconds(allocationDate)));
				}
			}
			c.add(Restrictions.eq("user.status", 1));
			listOfProductCoreResources = c.list();
			log.info("Result Set Size : " + listOfProductCoreResources.size());
			if(listOfProductCoreResources != null && !(listOfProductCoreResources.isEmpty())){
				TestFactoryProductCoreResource tfProductCoreRes = (TestFactoryProductCoreResource)listOfProductCoreResources.get(0);
				if(tfProductCoreRes != null){
					productSpecificRoleId = tfProductCoreRes.getUserRole().getUserRoleId();
				}
			}
		} catch (Exception e) {
			log.error("update failed", e);
			return productSpecificRoleId;
		}
		return productSpecificRoleId;
	}
	
	@Override
	@Transactional
	public Integer getTestFactoryResourcePoolHasTestFactorySizebyTestFactoryId(Integer testFcatoryId){
		Integer testFactoryResPoolCount=0;
		String sql ="SELECT COUNT(*) FROM test_factory_resource_pool_has_test_factory tfrspool WHERE tfrspool.testFactoryId="+testFcatoryId;
		testFactoryResPoolCount = ((Number) sessionFactory.getCurrentSession().createSQLQuery(sql).uniqueResult()).intValue();
		return testFactoryResPoolCount;
	}
	
}
