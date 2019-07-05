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
import org.hibernate.SQLQuery;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.hcl.atf.taf.dao.ProductFeatureDAO;
import com.hcl.atf.taf.dao.TestCaseListDAO;
import com.hcl.atf.taf.dao.utilities.HibernateCustomOrderByForHierarchicalEntities;
import com.hcl.atf.taf.model.ProductFeature;
import com.hcl.atf.taf.model.ProductFeatureHasTestCase;
import com.hcl.atf.taf.model.ProductFeatureProductBuildMapping;
import com.hcl.atf.taf.model.ProductMaster;
import com.hcl.atf.taf.model.TestCaseList;
import com.hcl.atf.taf.model.TestCasePriority;
import com.hcl.atf.taf.model.TestSuiteList;

@Repository
public class ProductFeatureDAOImpl implements ProductFeatureDAO {
	private static final Log log = LogFactory.getLog(ProductFeatureDAOImpl.class);
	
	private static final String ENTITY_TABLE_NAME = "product_feature";
	
	@Autowired(required=true)
    private SessionFactory sessionFactory;	
		
	@Autowired
	private TestCaseListDAO testCaseListDAO;
	
	@Override
	@Transactional
	public List<ProductFeature> list() {
		log.debug("listing all ProductFeature instance");
		List<ProductFeature> productFeatures=null;
		try {
			Criteria c = sessionFactory.getCurrentSession().createCriteria(ProductFeature.class, "pfeature");
			productFeatures = c.list();		
			
			if (!(productFeatures == null || productFeatures.isEmpty())){
				for (ProductFeature pfeature : productFeatures) {
						if(pfeature.getProductMaster() != null){
							Hibernate.initialize(pfeature.getProductMaster());
						}							
				}
			}
			log.debug("list all successful");
		} catch (RuntimeException re) {
			log.error("list all failed", re);
		}
		return productFeatures;
	}

	@Override
	@Transactional
	public List<ProductFeature> list(Integer startIndex, Integer pageSize, Date startDate,Date endDate) {
		log.debug("listing all ProductFeature instance");
		List<ProductFeature> productFeatures=null;
		try {
			
			Criteria c = sessionFactory.getCurrentSession().createCriteria(ProductFeature.class, "pfeature");
			if (startDate != null) {
				c.add(Restrictions.ge("pfeature.createDate", startDate));
			}
			if (endDate != null) {
				c.add(Restrictions.le("pfeature.createDate", endDate));
			}
			c.addOrder(Order.asc("productFeatureId"));
            c.setFirstResult(startIndex);
            c.setMaxResults(pageSize);
            productFeatures = c.list();	
			log.debug("list all successful");
			
			if (!(productFeatures == null || productFeatures.isEmpty())){
				for (ProductFeature pfeature : productFeatures) {
						if(pfeature.getProductMaster() != null){
							Hibernate.initialize(pfeature.getProductMaster());
						}							
				}
			}
			log.debug("list all successful");
		} catch (RuntimeException re) {
			log.error("list all failed", re);
		}
		return productFeatures;
	}

	@Override
	@Transactional
	public List<ProductFeature> listChildNodesInHierarchyinLayers(ProductFeature feature) {

		log.debug("listing all ching nodes in hierarchy");
		List<ProductFeature> productFeatures=null;
		try {
			
			Criteria c = sessionFactory.getCurrentSession().createCriteria(ProductFeature.class, "feature");
			c.add(Restrictions.gt("leftIndex", feature.getLeftIndex()));
			c.add(Restrictions.lt("rightIndex", feature.getRightIndex()));
			
			c.addOrder(HibernateCustomOrderByForHierarchicalEntities.sqlFormula("(rightIndex-leftIndex) desc"));
			c.addOrder(Order.asc("leftIndex"));
			productFeatures = c.list();	
			
			log.debug("listing child nodes in hierarchy successful");
		} catch (RuntimeException re) {
			log.error("listing child nodes in hierarchy failed", re);
		}
		return productFeatures;
	}

	
	@Override
	@Transactional
	public ProductFeature getByProductFeatureId(int productFeatureId) {
		log.debug("getting ProductFeature instance by id");
		ProductFeature productFeature=null;
		try {			
			List list =  sessionFactory.getCurrentSession().createQuery("from ProductFeature p where productFeatureId=:productFeatureId").setParameter("productFeatureId", productFeatureId)
					.list();
			productFeature=(list!=null && list.size()!=0)?(ProductFeature)list.get(0):null;
			if(productFeature != null){
				Hibernate.initialize(productFeature);
				Hibernate.initialize(productFeature.getProductMaster());
				Set<ProductFeature> pfSet= productFeature.getChildFeatures();
				if(pfSet.size() !=0){
					Hibernate.initialize(productFeature.getChildFeatures());
					for(ProductFeature pfeature:pfSet){
						if(pfeature!=null){
							Hibernate.initialize(pfeature);
							Set<TestCaseList> testCaseList=pfeature.getTestCaseList();
							if(testCaseList.size()!=0){
								Hibernate.initialize(testCaseList);
							}
						}
						
					}
				}
				if(productFeature.getParentFeature() != null){
					Hibernate.initialize(productFeature.getParentFeature());
					Hibernate.initialize(productFeature.getParentFeature().getProductFeatureName());
				}
				if(productFeature.getTestCaseList() != null){
					Set<TestCaseList> tcSet = productFeature.getTestCaseList();
					Hibernate.initialize(productFeature.getTestCaseList());
					for (TestCaseList testCaseList : tcSet) {				
						Hibernate.initialize(testCaseList);	
						Hibernate.initialize(testCaseList.getExecutionTypeMaster());	
						Hibernate.initialize(testCaseList.getProductMaster());
						Hibernate.initialize(testCaseList.getProductVersionList());
						Hibernate.initialize(testCaseList.getProductVersionList());
						if(testCaseList.getProductFeature() != null){
							Hibernate.initialize(testCaseList.getProductFeature());
						}	
						if(testCaseList.getDecouplingCategory() != null){
							Hibernate.initialize(testCaseList.getDecouplingCategory());
						}	
						Hibernate.initialize(testCaseList.getTestCasePriority());
					}
				}
				
			}
			log.debug("getByProductFeatureId successful");
		} catch (RuntimeException re) {
			log.error("getByProductFeatureId failed", re);
			//throw re;
		}
		return productFeature;        
	}

	@Override
	@Transactional
	public Integer getUnMappedTestCaseListCountOfFeatureByProductFeatureId(int productId, int productFeatureId) {
		log.info("Get Testcaselist unmapped to ProductFeature");
		int totUnMappedTCListCount = 0;
		try {
			totUnMappedTCListCount=((Number) sessionFactory.getCurrentSession().createSQLQuery("select distinct count(*) from test_case_list t where  t.productId=:productMasterId and t.testCaseId not in(select pfte.testCaseId from product_feature_has_test_case_list pfte where pfte.productFeatureId=:featureId)").
					setParameter("productMasterId", productId).
					setParameter("featureId", productFeatureId).uniqueResult()).intValue();
		} catch (HibernateException e) {
			log.error("Error getting UnMappedTCList Of Feature By ProductFeatureId",e);
		}		
		return totUnMappedTCListCount;
	}
	
	@Override
	@Transactional
	public Integer getUnMappedTestCaseListCountOfFeatureByProductId(int productId) {
		log.info("Get Total Testcaselist of product unmapped to ProductFeature");
		int totUnMappedTCListCount = 0;
		try {
			totUnMappedTCListCount=((Number) sessionFactory.getCurrentSession().createSQLQuery(
							"select distinct count(*) from test_case_list t where  t.productId=:productMasterId and t.testCaseId not in(select pfte.testCaseId from product_feature_has_test_case_list pfte, product_feature pf, product_master pm where pfte.productFeatureId=pf.productFeatureId and pf.productId = pm.productId and pm.productId =:pId)").
					setParameter("productMasterId", productId).
					setParameter("pId", productId).uniqueResult()).intValue();			
		} catch (HibernateException e) {
			log.error(e);
		}		
		return totUnMappedTCListCount;
	}

	@Override
	@Transactional

	public List<Object[]> getUnMappedTestCaseListByProductFeatureId(int productId, int productFeatureId, Integer jtStartIndex, Integer jtPageSize) {
		log.info("Get UnMappedTCOfFeature with ProductFeatureId");
			List<Object[]> unMappedTCListObj = null;
		try {
			String sql="select distinct testCaseId, testCaseName, testCaseCode from test_case_list t where  productId=:productMasterId and t.testCaseId not in(select pfte.testCaseId from product_feature_has_test_case_list pfte where pfte.productFeatureId=:featureId)";
			sql = sql+" OFFSET "+jtStartIndex+" LIMIT "+jtPageSize+"";
			unMappedTCListObj=sessionFactory.getCurrentSession().createSQLQuery(sql).
					setParameter("productMasterId", productId).
					setParameter("featureId", productFeatureId).
					list();
			log.info("unmapped TestCase of Feature :"+unMappedTCListObj.size());
		
		} catch (RuntimeException re) {
			log.error("Get testcaselist Unmapped to productFeatureId", re);	
		}
		return unMappedTCListObj;
		
	}

	@Override
	@Transactional
	public void add(ProductFeature productFeature) {
		log.debug("adding ProductFeature instance");
		try {

			//Logic for creating the initial Product Feature. The main root node.
			//There will be only one root node 
			List<ProductFeature> productFeatures = list();
			ProductFeature rootPF = null;
			if (productFeatures == null) {
				//Some problem in finding out Product features size. Will lead to inconsistent state
				//Abort adding.
				log.error("Add failed as could not find PF objects");
				return;
			} else if (productFeatures.size() <= 0) {

				//There are no Features in the DB. Seed the main root node
				rootPF = new ProductFeature();
				rootPF.setProductFeatureName("Product Features");
				rootPF.setProductFeatureDescription("Root Product Feature");
				rootPF.setDisplayName("Product Features");
				rootPF.setLeftIndex(1);
				rootPF.setRightIndex(2);
				sessionFactory.getCurrentSession().save(rootPF);
			} 
			
		//	productFeature.setStatus(1);
			if (productFeature.getParentFeature() == null) {
		        //If no parent category is specified, make the root category the parent category
				rootPF = getByProductFeatureName("Product Features");
				//TODO : Get the PF without parent. This will be the root PF. There will be only one root PF,
				//one without a parent PF
				productFeature.setParentFeature(rootPF);
			}	
			
			ProductFeature parentFC = productFeature.getParentFeature();
			if(parentFC != null){
		        updateHierarchyIndexForNew(ENTITY_TABLE_NAME, parentFC.getRightIndex());
		        productFeature.setLeftIndex(parentFC.getRightIndex());
		        productFeature.setRightIndex(parentFC.getRightIndex() + 1);
		        productFeature.setProductFeaturestatus(1);
				sessionFactory.getCurrentSession().save(productFeature);
			}
			
			log.debug("add successful");
		} catch (RuntimeException re) {
			log.error("add failed", re);
			//throw re;
		}
	}
	
	@Override
	@Transactional
	public ProductFeature getRootFeature() {
		log.debug("Getting Root ProductFeature instance");
		ProductFeature rootPF = null;
		try {

			rootPF = getByProductFeatureName("Product Features");
			log.debug("add successful");
		} catch (RuntimeException re) {
			log.error("add failed", re);
		}
		return rootPF;
	}

	@Override
	@Transactional
	public void updateHierarchyIndexForNew(Integer parentRightIndex) {
		
		updateHierarchyIndexForNew(ENTITY_TABLE_NAME, parentRightIndex);
	}

	
	@Override
	@Transactional
	public void updateHierarchyIndexForNew(String tableName, int parentRightIndex) {
		
		int leftIndex = parentRightIndex;
        int rightIndex = leftIndex + 1;
        final String strRightIndex = "rightIndex";
        String jpql ="UPDATE "+ tableName +" SET rightIndex = rightIndex + 2 WHERE rightIndex >= :rightIndex and leftIndex < :rightIndex";
        SQLQuery fetchQuery = sessionFactory.getCurrentSession().createSQLQuery(jpql);
        fetchQuery.setParameter(strRightIndex, parentRightIndex);
        fetchQuery.executeUpdate();
        
        jpql ="UPDATE "+tableName+" SET leftIndex=leftIndex + 2, rightIndex = rightIndex + 2 WHERE rightIndex > :rightIndex and leftIndex > :rightIndex";
        fetchQuery = sessionFactory.getCurrentSession().createSQLQuery(jpql);
        fetchQuery.setParameter(strRightIndex, parentRightIndex);
        fetchQuery.executeUpdate();
	}
	
	@Override
	@Transactional
	public void updateHierarchyIndexForDelete(Integer leftIndex, Integer rightIndex) {
	
		updateHierarchyIndexForDelete(ENTITY_TABLE_NAME, leftIndex, rightIndex);
	}
	
	@Override
	@Transactional
	public void updateHierarchyIndexForDelete(String tableName, int leftIndex, int rightIndex) {
		
		final String strRightIndex = "rightIndex";
        String jpql ="UPDATE "+ tableName +" SET rightIndex = rightIndex - 2 WHERE rightIndex > :rightIndex and leftIndex < :rightIndex";
        SQLQuery fetchQuery = sessionFactory.getCurrentSession().createSQLQuery(jpql);
        fetchQuery.setParameter(strRightIndex, rightIndex);
        fetchQuery.executeUpdate();
        
        jpql ="UPDATE "+tableName+" SET leftIndex=leftIndex - 2, rightIndex = rightIndex - 2 WHERE rightIndex > :rightIndex and leftIndex > :rightIndex";
        fetchQuery = sessionFactory.getCurrentSession().createSQLQuery(jpql);
        fetchQuery.setParameter(strRightIndex, rightIndex);
        fetchQuery.executeUpdate();
 	}

	
	@Override
	@Transactional
	public void delete(ProductFeature productFeature) {
		log.debug("updating ProductFeature instance");
		try {			
			//Delete only if the DC has no child categories
			if (productFeature.getChildFeatures() == null || productFeature.getChildFeatures().size() <= 0) {
				
				updateHierarchyIndexForDelete(ENTITY_TABLE_NAME, productFeature.getLeftIndex(), productFeature.getRightIndex());
				sessionFactory.getCurrentSession().delete(productFeature);
			} else {				
				log.info("Delete not allowed as the PF has child PF. Delete them first");
			}
			log.debug("update successful");
		} catch (RuntimeException re) {
			log.error("update failed", re);
			//throw re;
		}		
	}

	@Override
	@Transactional
	public void update(ProductFeature feature) {
		
		log.debug("updating ProductFeature instance");
		try {
			sessionFactory.getCurrentSession().merge(feature);
			log.info("Update successful");
			return;
		} catch (RuntimeException re) {
			log.error("update failed", re);
		}		
	}

	@Override
	@Transactional
	public void updateFeatureParent(ProductFeature feature, ProductFeature oldParentFeature, ProductFeature newParentFeature) {
		
	}

	@Override
	@Transactional
	public ProductFeature getByProductFeatureName(String productFeatureName) {
		ProductFeature productFeature = null;
		List list = null;
	try{
		list = sessionFactory.getCurrentSession().createQuery("from ProductFeature c where productFeatureName = :name")
				.setParameter("name", productFeatureName).list();
		productFeature = (ProductFeature) ((list!=null && ! list.isEmpty())? list.get(0):null);			
		
	} catch(RuntimeException re){
		log.error("Error in Product Feature retrieval",re);
	}	
		return productFeature;
	}
	
	@Override
	@Transactional
	public boolean isProductFeatureExistingByName(String productFeatureName) {

		String hql = "from ProductFeature c where productFeatureName = :name";
		List productFeatures = sessionFactory.getCurrentSession().createQuery(hql).setParameter("name", productFeatureName).list();
		if (productFeatures == null || productFeatures.isEmpty()) 
		    return false;
		else 
			return true;
	}

	@Override
	@Transactional
	public boolean isProductFeatureExistingByName(String productFeatureName, Integer productId, Integer productFeatureId) {
		log.info("verifying if any feature existing by same name to the product");
		Criteria c = sessionFactory.getCurrentSession().createCriteria(ProductFeature.class,"pfeature");			
		c.createAlias("pfeature.productMaster", "product");
		c.add(Restrictions.eq("product.productId",productId));
		if(productFeatureId != null && productFeatureId > 0){
			c.add(Restrictions.ne("pfeature.productFeatureId", productFeatureId));
		}
		c.add(Restrictions.eq("pfeature.productFeatureName", productFeatureName));
		List productFeatures =c.list();	
		
		if (productFeatures == null || productFeatures.isEmpty()) 
		    return false;
		else 
			return true;
	}
	
	@Override
	@Transactional
	public boolean isProductFeatureExistingByName(ProductFeature productFeature) {
		String hql = "from ProductFeature c where productFeatureName = :name";
		List productFeatures = sessionFactory.getCurrentSession().createQuery(hql).setParameter("name", productFeature.getProductFeatureName()).list();
		if (productFeatures == null || productFeatures.isEmpty()) 
		    return false;
		else 
			return true;
	}

	@Override
	@Transactional
	public List<ProductFeature> getFeatureListByProductId(Integer productId, Integer featureStatus, Integer jtStartIndex, Integer jtPageSize) { 
		return getFeatureListByProductId(productId, featureStatus, jtStartIndex, jtPageSize, true);
	}

	
	@Override
	@Transactional
	public List<ProductFeature> getFeatureListByProductId(Integer productId, Integer featureStatus, Integer jtStartIndex, Integer jtPageSize, boolean initialize) {
		log.debug("listing all Features for product");
		List<ProductFeature> features = null;
		try{
			
			Criteria c = sessionFactory.getCurrentSession().createCriteria(ProductFeature.class, "productFeature");
			c.add(Restrictions.eq("productFeature.productMaster.productId", productId));
			if(featureStatus != null){
				if(featureStatus != 2){
					c.add(Restrictions.eq("productFeature.productFeaturestatus", featureStatus));
				}
			}else{
				c.add(Restrictions.eq("productFeature.productFeaturestatus", 1));
			}
			if(jtStartIndex != null && jtPageSize != null)
				c.setFirstResult(jtStartIndex).setMaxResults(jtPageSize);
			c.addOrder(Order.asc("productFeature.leftIndex"));
			features = c.list();
			
			if (initialize) {
				if(!(features == null || features.isEmpty())){
					for(ProductFeature pf : features){
						if(pf.getParentFeature() != null){
							Hibernate.initialize(pf.getParentFeature());
						}			
						Hibernate.initialize(pf.getChildFeatures());					
						if(pf.getTestCaseList() != null){
							Set<TestCaseList> tCaseSet = pf.getTestCaseList();
							Hibernate.initialize(tCaseSet);
							for (TestCaseList testCaseList : tCaseSet) {							
								Hibernate.initialize(testCaseList.getTestCaseStepsLists());
								Hibernate.initialize(testCaseList.getTestSuiteLists());
								Hibernate.initialize(testCaseList.getDecouplingCategory());				
								Hibernate.initialize(testCaseList.getTestExecutionResults());								
								Hibernate.initialize(testCaseList.getTestSuiteLists());
							}
							
						}
						Hibernate.initialize(pf.getProductMaster());
					}
				}
			}
			log.debug("list all successful");			
		}catch(RuntimeException re){
			log.error("list all failed", re);
		}
		return features;
	}
	
	@Override
	@Transactional
	public Integer getProductFeatureCount(Integer productId, Integer jtStartIndex, Integer jtPageSize) {
		log.debug("getProductFeatureCount by productId");
		Integer productFeaturesCount = 0;
		try{
			String sql="select count(*) from product_feature pf where pf.productId=:prodId and pf.productFeatureStatus=:pfstatus";
			if(jtStartIndex!=-1 && jtPageSize!=-1){
				productFeaturesCount=((Number) sessionFactory.getCurrentSession().createSQLQuery(sql)
						.setParameter("prodId", productId)
						.setParameter("pfstatus", 1)
						.setFirstResult(jtStartIndex).setMaxResults(jtPageSize)
						.uniqueResult()).intValue();				
			}else{
				productFeaturesCount=((Number) sessionFactory.getCurrentSession().createSQLQuery(sql)
						.setParameter("prodId", productId)
						.setParameter("pfstatus", 1)
						.uniqueResult()).intValue();					
			}
			log.debug("list all successful");			
		}catch(RuntimeException re){
			log.error("list all failed", re);
		}
		return productFeaturesCount;
	}

	@Override
	@Transactional
	public Integer getFeatureListSize(Integer productId) {
		log.debug("listing all Features for product");
		List<ProductFeature> features = null;
		Integer size=0;
		try{
			Criteria c = sessionFactory.getCurrentSession().createCriteria(ProductFeature.class, "productFeature");
			c.add(Restrictions.eq("productFeature.productMaster.productId", productId));
			c.add(Restrictions.eq("productFeature.productFeaturestatus", 1));
			features = c.list();
			if(features!=null && !features.isEmpty()){
				size=features.size();
			}
			
			log.debug("list all successful");			
		}catch(RuntimeException re){
			log.error("list all failed", re);
		}
		return size;
	}
	
	@Override
	@Transactional
	public List<ProductFeature> getFeaturesMappedToTestCase(Integer testCaseId) {
		log.debug(" getFeaturesMappedToTestCase");
		List<ProductFeature> featureList = new ArrayList<ProductFeature>();		
		List<TestCaseList> testCaseList=new ArrayList<TestCaseList>();		
		try {			 
			Criteria c = sessionFactory.getCurrentSession().createCriteria(TestCaseList.class,"testcase");			
			c.add(Restrictions.eq("testcase.testCaseId", testCaseId));
			testCaseList =c.list();			
			Set<TestSuiteList> testSuiteLists = null;	
			Set<ProductFeature> featureSet = null;	
			if(testCaseList != null){
				for (TestCaseList tCase : testCaseList) {
					Hibernate.initialize(tCase.getProductMaster());					
					if(tCase.getProductFeature() != null){
						featureSet = tCase.getProductFeature();
						featureList.addAll(featureSet);
						Hibernate.initialize(featureSet);	
						for (ProductFeature productFeature : featureSet) {
							Hibernate.initialize(productFeature.getParentFeature());	
							Hibernate.initialize(productFeature.getChildFeatures());
							Hibernate.initialize(productFeature.getTestCaseList());
						}						
					}
					Hibernate.initialize(tCase.getTestCaseStepsLists());
					Hibernate.initialize(tCase.getTestSuiteLists());
					Hibernate.initialize(tCase.getDecouplingCategory());				
					Hibernate.initialize(tCase.getTestExecutionResults());								
					testSuiteLists = tCase.getTestSuiteLists(); 
					Hibernate.initialize(testSuiteLists);
					for(TestSuiteList tsuite : testSuiteLists){
						Hibernate.initialize(tsuite.getProductMaster());
						Hibernate.initialize(tsuite.getProductVersionListMaster());
						Hibernate.initialize(tsuite.getScriptTypeMaster());
					}
				}
								
			}	
			log.debug("getFeaturesMappedToTestCase successful");			
			} catch (RuntimeException re) {
				log.error("list specific failed", re);
				//throw re;
			}
		return featureList;
	}
	@Override
	@Transactional
	public List<ProductFeature> getFeatureListExcludingChildByparentFeatureId(Integer productId, Integer parentFeatureId) {
		log.debug("listing all Features Excluding Child features");
		List<ProductFeature> features = null;
		try{

			ProductFeature feature = getByProductFeatureId(parentFeatureId);
			
			Criteria c = sessionFactory.getCurrentSession().createCriteria(ProductFeature.class, "feature");
			c.add(Restrictions.not(Restrictions.between("leftIndex", feature.getLeftIndex(), feature.getRightIndex())));
			c.addOrder(Order.asc("leftIndex"));
			features = c.list();	
			log.info("For Feature ID :"+parentFeatureId);
			log.info("Below are list of ancestors");
			if(!(features == null || features.isEmpty())){
				for(ProductFeature pf : features){
					if(pf.getParentFeature() != null){
						Hibernate.initialize(pf.getParentFeature());
					}				
						Hibernate.initialize(pf.getProductMaster());
						log.info(pf.getProductFeatureName());
				}
			}
			log.debug("list all successful");			
		}catch(RuntimeException re){
			log.error("list all failed", re);
			//throw re;
		}
		return features;
	}
	@Override
	@Transactional
	public TestCaseList updateProductFeatureTestCases(int testCaseId, int productFeatureId) {
		log.info("updateProductFeatureTestCases method updating ProductFeature - TestCase Association");
		TestCaseList testCaseList = null;
		ProductFeature productFeature = null;
		try{
			testCaseList = testCaseListDAO.getByTestCaseId(testCaseId);
			productFeature = getByProductFeatureId(productFeatureId);
			
			if (testCaseList != null && productFeature != null){
				boolean needToUpdateOrAdd = false;
				Set<ProductFeature> productFeatureSet = testCaseList.getProductFeature();
				// if no associated record exists need to add the new records
				if (productFeatureSet.size() == 0)
				{
					needToUpdateOrAdd = true;
				}else {
					// Associated record exists					
					ProductFeature productFeatureForProcessing = productFeatureSet.iterator().next();					
			
					if (productFeatureForProcessing != null) {
						log.info("productFeatureForProcessing not null");
						int alreadyAvailableProductFeatureId = productFeatureForProcessing.getProductFeatureId().intValue();

						// If associated record is for the same ProductFeature, don't do any addition/updation
						if (alreadyAvailableProductFeatureId != productFeatureId) {	
							
							// Now association exists for different Product Feature, so that association need to be cleared in testcase object first
							log.info("values or different");
							testCaseList.getProductFeature().clear();
							log.info("testCaseList.getProductFeature().size()="+testCaseList.getProductFeature().size());							
							
							// Now association exists for different Product Feature, 
							// so that association need to be cleared for Feature
							ProductFeature productFeatureAvailable = getByProductFeatureId(alreadyAvailableProductFeatureId);
							for (TestCaseList tc: productFeatureAvailable.getTestCaseList())
							{
								log.info("tc.getTestCaseId().intValue()"+tc.getTestCaseId().intValue());
							
								// Now association exists for different decoupling category, but for same testcase id, 
								// so that association needs to be removed from decouplecategory object
								if (tc.getTestCaseId().intValue() == testCaseId) {	
									log.info("testcase found in decouple");
									productFeatureAvailable.getTestCaseList().remove(tc);
									sessionFactory.getCurrentSession().saveOrUpdate(productFeatureAvailable);
									log.info("productFeatureAvailable.getTestCaseList().size()="+productFeatureAvailable.getTestCaseList().size());
									break;
								}	
							}								
							needToUpdateOrAdd = true;
						}	
					}
				}//ends here
				if (needToUpdateOrAdd) {
					testCaseList.getProductFeature().add(productFeature);
					productFeature.getTestCaseList().add(testCaseList);	
					sessionFactory.getCurrentSession().saveOrUpdate(testCaseList);
					sessionFactory.getCurrentSession().saveOrUpdate(productFeature);
				}
			}				
		} catch (RuntimeException re) {
			log.error("list specific failed", re);
			//throw re;
		}
		return testCaseList;		
	}

	@Override
	@Transactional
	public TestCaseList updateProductFeatureTestCasesOneToMany(int testCaseId, int productFeatureId, String maporunmap) {
		log.info("updateProductFeatureTestCases method updating ProductFeature - TestCase Association");
		TestCaseList testCaseList = null;
		ProductFeature productFeature = null;
		Set<TestCaseList> testCaseSet = null;
		try{
			testCaseList = testCaseListDAO.getByTestCaseId(testCaseId);
			productFeature = getByProductFeatureId(productFeatureId);		
			if (testCaseList != null && productFeature != null){
				boolean needToUpdateOrAdd = false;
				if(maporunmap.equalsIgnoreCase("map")){				
						ProductFeature pf = productFeature;
						pf.getTestCaseList().add(testCaseList);
						sessionFactory.getCurrentSession().saveOrUpdate(pf);					
				}else if(maporunmap.equalsIgnoreCase("unmap")){
					testCaseSet = productFeature.getTestCaseList();
					if(testCaseSet.size() != 0){
						if(testCaseSet.contains(testCaseList)){
							ProductFeature pf1 = productFeature;
							pf1.getTestCaseList().remove(testCaseList);
							sessionFactory.getCurrentSession().saveOrUpdate(pf1);
						}					
					}					
				}				
			}				
		} catch (RuntimeException re) {
			log.error("list specific failed", re);
		}
		return testCaseList;		
	}

	@Override
	@Transactional
	public List<ProductFeature> getChildFeatureListByParentfeatureId(Integer parentFeatureId) {
		log.debug("listing all Features for product");
		List<ProductFeature> features = null;
		try{
			features=sessionFactory.getCurrentSession().createQuery("from ProductFeature f where f.parentFeature.productFeatureId=:parentFeatureId and productFeatureStatus=1 order by f.leftIndex asc")
					.setParameter("parentFeatureId", parentFeatureId).list();	
			if(!(features == null || features.isEmpty())){
				for(ProductFeature pf : features){
					if(pf.getParentFeature() != null){
						Hibernate.initialize(pf.getParentFeature());
					}			
					Hibernate.initialize(pf.getChildFeatures());					
					if(pf.getTestCaseList() != null){
						Set<TestCaseList> tCaseSet = pf.getTestCaseList();
						Hibernate.initialize(tCaseSet);
						for (TestCaseList testCaseList : tCaseSet) {							
							Hibernate.initialize(testCaseList.getTestCaseStepsLists());
							Hibernate.initialize(testCaseList.getTestSuiteLists());
							Hibernate.initialize(testCaseList.getDecouplingCategory());				
							Hibernate.initialize(testCaseList.getTestExecutionResults());								
							Hibernate.initialize(testCaseList.getTestSuiteLists());
						}
						
					}
					Hibernate.initialize(pf.getProductMaster());
				}
			}
			log.debug("list all successful");			
		}catch(RuntimeException re){
			log.error("list all failed", re);
			//throw re;
		}
		return features;
	}

	@Override
	@Transactional
	public Integer geRootProductFeatureId(String rootProductFeatureDescription) {
		log.debug("getting Root Decoupling Category Id");
		Integer rootFeatureId =0;
		ProductFeature productFeature = null;
		try {
			List list =sessionFactory.getCurrentSession().createQuery(
					"from "
					+ "ProductFeature where productFeatureDescription=:rootProductFeatureDescription")
					.setParameter("rootProductFeatureDescription", rootProductFeatureDescription).list();
			
			productFeature=(list!=null && list.size()!=0)?(ProductFeature)list.get(0):null;
			if (productFeature != null) {
				rootFeatureId = productFeature.getProductFeatureId();
			}
			log.debug("Fetch Root Feature success");
		} catch (RuntimeException re) {
			log.error("Fetch Root Feature failed", re);			
		}
		return rootFeatureId;
	}
	
	@Override
	public ProductFeature getByProductFeatureCode(int featureCode) {
		

		log.debug("getting ProductFeature instance by Code");
		ProductFeature productFeature=null;
		String productFeatureCode = String.valueOf(featureCode);
		try {
			
			List list =  sessionFactory.getCurrentSession().createQuery("from ProductFeature p where productFeatureCode=:productFeatureCode").setParameter("productFeatureCode", productFeatureCode)
					.list();
			productFeature=(list!=null && list.size()!=0)?(ProductFeature)list.get(0):null;
			if(productFeature!= null){
				Hibernate.initialize(productFeature.getProductMaster());
				
				Hibernate.initialize(productFeature.getProductMaster().getTestSuiteLists());
				Hibernate.initialize(productFeature.getProductMaster().getTestCaseLists());
				if(productFeature.getTestCaseList() != null){
					Hibernate.initialize(productFeature.getTestCaseList());	
				}	
				if(productFeature.getTestCaseList() != null){
					Set<TestCaseList> tcSet = productFeature.getTestCaseList();
					for (TestCaseList testCaseList : tcSet) {				
						Hibernate.initialize(testCaseList);	
						
						Hibernate.initialize(testCaseList.getProductMaster());
						if(testCaseList.getProductFeature() != null){
							Hibernate.initialize(testCaseList.getProductFeature());
						}						
					}
				}
			}
			
			log.debug("getByProductFeatureCode successful");
		} catch (RuntimeException re) {
			log.error("getByProductFeatureCode failed", re);
			//throw re;
		}
		return productFeature;
        
	
	}

	
	@Override
	@Transactional
	public boolean isProductFeatureExistingByFeatureCode(String productFeatureCode, ProductMaster product) {

		String hql = "from ProductFeature pf where productFeatureCode = :code and pf.productMaster.productId = :productId";
		List productFeatures = sessionFactory.getCurrentSession().createQuery(hql)
				.setParameter("code", productFeatureCode)
				.setParameter("productId", product.getProductId()).list();
		if (productFeatures == null || productFeatures.isEmpty()) 
		    return false;
		else 
			return true;
	}

	@Override
	@Transactional
	public ProductFeature getByProductFeatureCode(String productFeatureCode, Integer productId) {
		ProductFeature productFeature = null;
		List list = null;
		try{
			list = sessionFactory.getCurrentSession().createQuery("from ProductFeature pf where productFeatureCode = :code and pf.productMaster.productId = :productId")
					.setParameter("code", productFeatureCode).
					setParameter("productId", productId).list();
			productFeature = (ProductFeature) ((list!=null && ! list.isEmpty())? list.get(0):null);			
			
		} catch(RuntimeException re){
			log.error("Error in Product Feature retrieval",re);
		}	
		return productFeature;
	}

	@Override
	@Transactional
	public ProductFeature getByProductFeatureCode(String productFeatureCode) {
		ProductFeature productFeature = null;
		List list = null;
		try{
			list = sessionFactory.getCurrentSession().createQuery("from ProductFeature pf where pf.productFeatureCode = :code")
					.setParameter("code", productFeatureCode).list();
			productFeature = (ProductFeature) ((list!=null && ! list.isEmpty())? list.get(0):null);			
			
		} catch(RuntimeException re){
			log.error("Error in Product Feature retrieval",re);
		}	
		return productFeature;
	}
	
	@Override
	@Transactional
	public ProductFeature getByProductFeatureCode(String productFeatureCode, ProductMaster product) {
		ProductFeature productFeature = null;
		List list = null;
		try{
			list = sessionFactory.getCurrentSession().createQuery("from ProductFeature pf where pf.productFeatureCode = :code and pf.productMaster.productId = :productId")
					.setParameter("code", productFeatureCode).
					setParameter("productId", product.getProductId()).list();
			productFeature = (ProductFeature) ((list!=null && ! list.isEmpty())? list.get(0):null);			
			
		} catch(RuntimeException re){
			log.error("Error in Product Feature retrieval",re);
		}	
		return productFeature;
	}

	@Override
	@Transactional
	public ProductFeature getByProductFeatureName(String productFeatureName,
			ProductMaster product) {
		ProductFeature productFeature = null;
		List list = null;
		try{
			list = sessionFactory.getCurrentSession().createQuery("from ProductFeature pf where pf.productFeatureName = :name and pf.productMaster.productId = :productId")
					.setParameter("name", productFeatureName).
					setParameter("productId", product.getProductId()).list();
			productFeature = (ProductFeature) ((list!=null && ! list.isEmpty())? list.get(0):null);			
			
		} catch(RuntimeException re){
			log.error("Error in Product Feature retrieval",re);
		}	
		return productFeature;
	}
	
	@Override
	@Transactional
	public ProductFeature bulkUpdateOfProductFeature(ProductFeature productFeature,Integer count, Integer maxLimit) {
		log.debug("updating product Feature instance");
			try {
				 String displayName = "";
				//Logic for creating the initial Product Feature. The main root node.
				//There will be only one root node 
				List<ProductFeature> productFeatures = list();
				ProductFeature rootPF = null;
				if (productFeatures == null) {
					//Some problem in finding out Product features size. Will lead to inconsistent state
					//Abort adding.
					log.error("Add failed as could not find PF objects");
					return null;
				} else if (productFeatures.size() <= 0) {
					//There are no Features in the DB. Seed the main root node
					rootPF = new ProductFeature();
					rootPF.setProductFeatureName("Product Features");
					rootPF.setProductFeatureDescription("Root Product Feature");
					rootPF.setDisplayName("Product Features");
					rootPF.setLeftIndex(1);
					rootPF.setRightIndex(2);
					sessionFactory.getCurrentSession().save(rootPF);
				} 
				
			//	productFeature.setStatus(1);
				if (productFeature.getParentFeature() == null) {
			        //If no parent category is specified, make the root category the parent category
					rootPF = getByProductFeatureName("Product Features");
					//TODO : Get the PF without parent. This will be the root PF. There will be only one root PF,
					//one without a parent PF
					productFeature.setParentFeature(rootPF);
				}	
				
				ProductFeature parentFC = productFeature.getParentFeature();
				if(parentFC!=null){
					int leftIndex = parentFC.getRightIndex();
					int rightIndex = parentFC.getRightIndex() + 1;
			        updateHierarchyIndexForNew(ENTITY_TABLE_NAME, parentFC.getRightIndex());
			        productFeature.setLeftIndex(leftIndex);
			        productFeature.setRightIndex(rightIndex);
				}
		        displayName = setDisplayNameForFeature(productFeature); 
		        productFeature.setDisplayName(displayName);
		        productFeature.setProductFeaturestatus(1);
				sessionFactory.getCurrentSession().save(productFeature);
				
				if (count % maxLimit == 0 ) {
					sessionFactory.getCurrentSession().flush();
					sessionFactory.getCurrentSession().clear();
			    } 
				
				log.debug("add successful");
			} catch (RuntimeException re) {
				log.error("add failed", re);
				//throw re;
			}
		return productFeature;
	}
	
	public String setDisplayNameForFeature(ProductFeature productFeature){
		String pfDisplayName = "";
		if(productFeature.getParentFeature() != null){
			ProductFeature parentFeature = getByProductFeatureId(productFeature.getParentFeature().getProductFeatureId());
			productFeature.setParentFeature(parentFeature);
			//Create display name from parent 
			StringBuffer displayName = new StringBuffer();
			displayName.append(productFeature.getProductFeatureName());
			boolean hasParent = true;
			while (hasParent) {
				if (parentFeature.getParentFeature() != null)
					displayName.insert(0, parentFeature.getProductFeatureName() + " | ");
				parentFeature = parentFeature.getParentFeature();
				if (parentFeature == null)
					hasParent = false;			
			}
			pfDisplayName = displayName.toString();
		}else{
			pfDisplayName = productFeature.getProductFeatureName();
		}
		return pfDisplayName;
	}

	@Override
	@Transactional
	public List<ProductFeature> getRootFeatureListByProductId(
			Integer productId, Integer jtStartIndex, Integer jtPageSize, Integer rootFeatureId, boolean isInitializationReq) {
		log.debug("listing all Root Features for product");
		List<ProductFeature> features = null;
		try{
			
			Criteria c = sessionFactory.getCurrentSession().createCriteria(ProductFeature.class, "productFeature");
			c.add(Restrictions.eq("productFeature.productMaster.productId", productId));
			c.add(Restrictions.eq("productFeature.parentFeature.productFeatureId",rootFeatureId));
			c.add(Restrictions.eq("productFeature.productFeaturestatus", 1));
			if(jtStartIndex != null && jtPageSize != null && jtStartIndex != -1 && jtPageSize != -1)
			c.setFirstResult(jtStartIndex).setMaxResults(jtPageSize);
			c.addOrder(Order.asc("productFeature.leftIndex"));
			features = c.list();
			
			if (isInitializationReq) {
				if(!(features == null || features.isEmpty())){
					for(ProductFeature pf : features){
						if(pf.getParentFeature() != null){
							Hibernate.initialize(pf.getParentFeature());
						}			
						Hibernate.initialize(pf.getChildFeatures());					
						if(pf.getTestCaseList() != null){
							Set<TestCaseList> tCaseSet = pf.getTestCaseList();
							Hibernate.initialize(tCaseSet);
							for (TestCaseList testCaseList : tCaseSet) {							
								Hibernate.initialize(testCaseList.getTestCaseStepsLists());
								Hibernate.initialize(testCaseList.getTestSuiteLists());
								Hibernate.initialize(testCaseList.getDecouplingCategory());				
								Hibernate.initialize(testCaseList.getTestExecutionResults());								
								Hibernate.initialize(testCaseList.getTestSuiteLists());
							}
							
						}
						Hibernate.initialize(pf.getProductMaster());
					}
				}
			}
			log.debug("list all successful");			
		}catch(RuntimeException re){
			log.error("list all failed", re);
			//throw re;
		}
		return features;
	}

	@Override
	@Transactional
	public List<Object[]> getMappedTestCaseListByProductFeatureId(int productId, int productFeatureId, Integer jtStartIndex,
			Integer jtPageSize) {
		log.info("Get MappedTCOfFeature with ProductFeatureId");
		List<Object[]> mappedTCListObj = null;
		try {
			String sql="select distinct testCaseId, testCaseName, testCaseCode from test_case_list t where  t.testCaseId in(select pfte.testCaseId from product_feature_has_test_case_list pfte where pfte.productFeatureId=:productFeatureId) limit ";
			sql = sql+jtStartIndex+" ,"+jtPageSize+"";
			mappedTCListObj=sessionFactory.getCurrentSession().createSQLQuery(sql).
					setParameter("productFeatureId", productFeatureId).
					list();
			log.info("mapped list :"+mappedTCListObj.size());
		
		} catch (RuntimeException re) {
			log.error("Get testcaselist mapped to productFeatureId", re);	
		}
		return mappedTCListObj;
	}
	
	@Override
	@Transactional
	public Integer countProductFeatures(Date startDate,Date endDate) {
		
		try {
			Criteria c = sessionFactory.getCurrentSession().createCriteria(ProductFeature.class,"feature");
			if (startDate != null) {
				c.add(Restrictions.ge("feature.createdDate", startDate));
			}
			if (endDate != null) {
				c.add(Restrictions.le("feature.createdDate", endDate));
			}
			c.setProjection(Projections.rowCount());
			Integer count = Integer.parseInt(c.uniqueResult().toString());
			return count;
		} catch (Exception e) {
			log.error("Unable to get count of all Product Features", e);
			return -1;
		}
	}
	
	
	
	@Override
	@Transactional
	public ProductFeature addProductFeature(ProductFeature productFeature) {
		log.debug("adding ProductFeature instance");
		try {

			//Logic for creating the initial Product Feature. The main root node.
			//There will be only one root node 
			List<ProductFeature> productFeatures = list();
			ProductFeature rootPF = null;
			if (productFeatures == null) {
				//Some problem in finding out Product features size. Will lead to inconsistent state
				//Abort adding.
				log.error("Add failed as could not find PF objects");
				return null;
			} else if (productFeatures.size() <= 0) {

				//There are no Features in the DB. Seed the main root node
				rootPF = new ProductFeature();
				rootPF.setProductFeatureName("Product Features");
				rootPF.setProductFeatureDescription("Root Product Feature");
				rootPF.setDisplayName("Product Features");
				rootPF.setLeftIndex(1);
				rootPF.setRightIndex(2);
				sessionFactory.getCurrentSession().save(rootPF);
			} 
			
		//	productFeature.setStatus(1);
			if (productFeature.getParentFeature() == null) {
		        //If no parent category is specified, make the root category the parent category
				rootPF = getByProductFeatureName("Product Features");
				//TODO : Get the PF without parent. This will be the root PF. There will be only one root PF,
				//one without a parent PF
				productFeature.setParentFeature(rootPF);
			}	
			
			if(productFeature.getParentFeature() != null){
				ProductFeature parentFC = productFeature.getParentFeature();
				int leftIndex = parentFC.getRightIndex();
				int rightIndex = parentFC.getRightIndex() + 1;
				updateHierarchyIndexForNew(ENTITY_TABLE_NAME, parentFC.getRightIndex());
				productFeature.setLeftIndex(leftIndex);
				productFeature.setRightIndex(rightIndex);
			}
			productFeature.setProductFeaturestatus(1);
			sessionFactory.getCurrentSession().save(productFeature);
			log.debug("add successful");
			
		} catch (RuntimeException re) {
			log.error("add failed", re);
			//throw re;
		}
		return productFeature;
	}
	
	@Override
	@Transactional
	public ProductFeature getProductFeatureParentById(int productFeatureParentId) {
		log.debug("getting ProductFeature parent instance by parent id");
		ProductFeature productFeature=null;
		try {			
			List list =  sessionFactory.getCurrentSession().createQuery("from ProductFeature p where p.productFeatureId=:productFeatureId").setParameter("productFeatureId", productFeatureParentId).list();
			productFeature=(list!=null && list.size()!=0)?(ProductFeature)list.get(0):null;
			if(productFeature != null){
				Hibernate.initialize(productFeature.getParentFeature());
			}
			log.debug("getByProductFeatureIdAndParentId successful");
		} catch (RuntimeException re) {
			log.error("getByProductFeatureIdAndParentId failed", re);
			//throw re;
		}
		return productFeature;
	}
	
	
	@Override
	@Transactional
	public List<ProductFeature> getFeatureListByEnagementId(Integer engementId, Integer featureStatus, Integer jtStartIndex, Integer jtPageSize, boolean initialize) {
		log.debug("listing all Features for engagementId");
		List<ProductFeature> features = null;
		try{
			
			Criteria c = sessionFactory.getCurrentSession().createCriteria(ProductFeature.class, "productFeature");
			c.createAlias("productFeature.productMaster", "product");
			c.createAlias("product.testFactory", "testFactory");
			c.add(Restrictions.eq("testFactory.testFactoryId", engementId));
			
			if(featureStatus != null){
				if(featureStatus != 2){
					c.add(Restrictions.eq("productFeature.productFeaturestatus", featureStatus));
				}
			}else{
				c.add(Restrictions.eq("productFeature.productFeaturestatus", 1));
			}
			if(jtStartIndex != null && jtPageSize != null)
				c.setFirstResult(jtStartIndex).setMaxResults(jtPageSize);
			c.addOrder(Order.asc("productFeature.leftIndex"));
			features = c.list();
			log.debug("list all successful");			
		}catch(RuntimeException re){
			log.error("list all failed", re);
			//throw re;
		}
		return features;
	}
	
	@Override
	@Transactional
	public List<ProductFeatureProductBuildMapping> getProductFeatureAndProductBuildMappingList(Integer productId) {
		List<ProductFeatureProductBuildMapping> productFeatureAndBuildMappingList=null;
		try {
			
			Criteria c = sessionFactory.getCurrentSession().createCriteria(ProductFeatureProductBuildMapping.class, "productFeatureMapping");
			c.createAlias("productFeatureMapping.product", "product");
			c.add(Restrictions.eq("product.productId", productId));
			productFeatureAndBuildMappingList=c.list();
		}catch(RuntimeException re) {
			log.error("Error in getProductFeatureAndProductBuildMappingList",re);
		}
		return productFeatureAndBuildMappingList;
	}
	
	@Override
	@Transactional
	public void mappingProductFeatureToProductBuild(ProductFeatureProductBuildMapping productFeatureProductBuildMapping) {
		try {
			sessionFactory.getCurrentSession().saveOrUpdate(productFeatureProductBuildMapping);
		}catch(RuntimeException re) {
			log.error("Error in mappingProductFeatureToProductBuild",re);
		}
	}
	
	@Override
	@Transactional
	public void unMappingProductFeatureToProductBuild(ProductFeatureProductBuildMapping productFeatureProductBuildMapping) {
		try {
			String sql= "delete from productFeature_productBuild_mapping where productId="+productFeatureProductBuildMapping.getProduct().getProductId()+" and buildId="+productFeatureProductBuildMapping.getBuildId()+" and featureId="+productFeatureProductBuildMapping.getFeatureId();
			sessionFactory.getCurrentSession().createSQLQuery(sql).executeUpdate();
		}catch(RuntimeException re) {
			log.error("Error in mappingProductFeatureToProductBuild",re);
		}
	}

	@Override
	@Transactional
	public List<ProductFeature> getFeatureListByProductIdAndVersionIdAndBuild(
			Integer productId, Integer versionId, Integer buildId,
			Integer featureStatus, Integer jtStartIndex, Integer jtPageSize) {
		List<Object[]> mappingFeatureIds=null;
		List<ProductFeature> featureList= null;
		try {
			
			String sql="SELECT DISTINCT featureId FROM productfeature_productbuild_mapping mappingFeatureAndBuild "+
					" LEFT JOIN product_build build ON (build.productBuildId = mappingFeatureAndBuild.buildId) "+
					" LEFT JOIN product_version_list_master versions ON (versions.productVersionListId = build.productVersionId) "+
					" LEFT JOIN product_master product ON (product.productId = mappingFeatureAndBuild.productId) "+
					" WHERE product.productId="+productId;
			if( versionId !=null && versionId >0 ) {
				sql+=" AND versions.productVersionListId ="+versionId;
			}
			
			if(buildId != null && buildId >0) {
				sql+=" AND build.productBuildId  ="+buildId;
			}
			 sql+=" ORDER BY featureId ASC";
			 mappingFeatureIds=sessionFactory.getCurrentSession().createSQLQuery(sql).list();
			 
			 List<Integer> featureIds = new ArrayList<Integer>();
			 if(mappingFeatureIds != null && mappingFeatureIds.size() >0) {
				for(Object feature:mappingFeatureIds) {
					featureIds.add(Integer.parseInt(feature.toString()));
				}
			 }
			 
			 if(featureIds != null && featureIds.size() > 0){
				 Criteria featureCriteria = sessionFactory.getCurrentSession().createCriteria(ProductFeature.class, "feature");
				 featureCriteria.add(Restrictions.in("feature.productFeatureId", featureIds));
				 featureCriteria.setFirstResult(jtStartIndex);
				 featureCriteria.setMaxResults(jtPageSize);
				 featureList = featureCriteria.list();
			 }
		}catch(RuntimeException re) {
			log.error("Error in getFeatureListByProductIdAndVersionIdAndBuild",re);
		}
		return featureList;
	}

	@Override
	@Transactional
	public List<ProductFeatureProductBuildMapping> getFeatureListByProductIdAndVersionIdAndBuild(Integer productId, Integer versionId, Integer buildId) {
		List<ProductFeatureProductBuildMapping> mappingList = null;
		List<Object[]> mappingIdList= null;
		try {
			
			String sql="SELECT DISTINCT id FROM productfeature_productbuild_mapping mappingFeatureAndBuild "+
					" LEFT JOIN product_build build ON (build.productBuildId = mappingFeatureAndBuild.buildId) "+
					" LEFT JOIN product_version_list_master versions ON (versions.productVersionListId = build.productVersionId) "+
					" LEFT JOIN product_master product ON (product.productId = mappingFeatureAndBuild.productId) "+
					" WHERE product.productId="+productId;
			if( versionId !=null && versionId >0 ) {
				sql+=" AND versions.productVersionListId ="+versionId;
			}
			
			if(buildId != null && buildId >0) {
				sql+=" AND build.productBuildId  ="+buildId;
			}
			 sql+=" ORDER BY id ASC";
			 mappingIdList=sessionFactory.getCurrentSession().createSQLQuery(sql).list();
			 
			 List<Integer> mappingids = new ArrayList<Integer>();
			 if(mappingIdList != null && mappingIdList.size() >0) {
				for(Object id:mappingIdList) {
					mappingids.add(Integer.parseInt(id.toString()));
				}
			 }
			 
			 Criteria mappingCriteria = sessionFactory.getCurrentSession().createCriteria(ProductFeatureProductBuildMapping.class, "mappingFeatureAndBuild");
			 mappingCriteria.add(Restrictions.in("mappingFeatureAndBuild.id", mappingids));
			 mappingList = mappingCriteria.list();
			
		}catch(RuntimeException re) {
			
		}
		return mappingList;
	}

	@Override
	@Transactional
	public List<ProductFeatureProductBuildMapping> getProductFeatureAndProductBuildMappingId(Integer mappingId) {
		List<ProductFeatureProductBuildMapping> mappingList=null;
		try {
			
			 Criteria mappingCriteria = sessionFactory.getCurrentSession().createCriteria(ProductFeatureProductBuildMapping.class, "mappingFeatureAndBuild");
			 mappingCriteria.add(Restrictions.eq("mappingFeatureAndBuild.id", mappingId));
			 mappingList = mappingCriteria.list();
			
		}catch(RuntimeException re) {
			
		}
		return mappingList;
	}

	@Override
	@Transactional
	public List<ProductFeatureProductBuildMapping> getProductFeatureAndProductBuildMappingList() {
		List<ProductFeatureProductBuildMapping> mappingList=null;
		try {
			
			 Criteria mappingCriteria = sessionFactory.getCurrentSession().createCriteria(ProductFeatureProductBuildMapping.class, "mappingFeatureAndBuild");
			 mappingList = mappingCriteria.list();
			
		}catch(RuntimeException re) {
			
		}
		return mappingList;
	}

	@Override
	@Transactional
	public List<Integer> getFeatureTotestCaseMappingByFeatureId(Integer productFeatureId) {
		List<Integer> featureTotestCaseMappingList=null;
		try {
			String sql="SELECT testCaseId FROM product_feature_has_test_case_list WHERE productFeatureId="+productFeatureId;
			featureTotestCaseMappingList=sessionFactory.getCurrentSession().createSQLQuery(sql).list();
		}catch(RuntimeException re) {
			
		}
		return featureTotestCaseMappingList;
	}

	@Override
	@Transactional
	public List<ProductFeatureHasTestCase> getProductFeatureHasTestCaseList() {
		List<ProductFeatureHasTestCase> featureHasTestCaseList = null;
 		try {
 			featureHasTestCaseList  = sessionFactory.getCurrentSession().createCriteria(ProductFeatureHasTestCase.class).list();
		} catch (RuntimeException re) {
			log.error("Unable to fetch productFeatureHasTestCaseList", re);
		}
 		return featureHasTestCaseList;
	}

	@Override
	public List<TestCasePriority> listFeatureExecutionPriority() {
		List<TestCasePriority> executionPriorityList=null;
		try {
		Criteria c=sessionFactory.getCurrentSession().createCriteria(TestCasePriority.class,"testCasePriority");
		executionPriorityList = c.list();
		}catch(RuntimeException re) {
			log.error("Error while listFeatureExecutionPriority",re);
		}
		
		return executionPriorityList;
	}
	
	@Override
	@Transactional
	public List<Object[]> getMappedTestScriptListByTestcaseId(int productId, int testcaseId, Integer jtStartIndex,
			Integer jtPageSize) {
		log.info("Get Mapped Test script with testCaseId");
		List<Object[]> mappedTestScriptListObj = null;
		try {
			String sql="select distinct scriptId, scriptName from test_case_scripts t where  t.scriptId in(select tsts.scriptId from testcase_script_has_test_case_list tsts where tsts.testcaseId=:testcaseId) limit ";
			sql = sql+jtStartIndex+" ,"+jtPageSize+"";
			mappedTestScriptListObj=sessionFactory.getCurrentSession().createSQLQuery(sql).
					setParameter("testcaseId", testcaseId).
					list();
			log.info("mapped list :"+mappedTestScriptListObj.size());
		
		} catch (RuntimeException re) {
			log.error("Get testscripts mapped to testcaseId", re);	
		}
		return mappedTestScriptListObj;
	}
	
	@Override
	@Transactional
	public List<ProductFeature> getProductFeatureHasTestCaseId(Integer testCaseId) {
		List<ProductFeatureHasTestCase> featureHasTestCaseList = null;
		List<ProductFeature> featureList = null;
		List<Integer> featureIds= new ArrayList<Integer>();
 		try {
 			Criteria c  = sessionFactory.getCurrentSession().createCriteria(ProductFeatureHasTestCase.class,"featureMappingTestCase");
 			c.add(Restrictions.eq("featureMappingTestCase.testCaseId", testCaseId));
 			featureHasTestCaseList=c.list();
 			if(featureHasTestCaseList!= null && featureHasTestCaseList.size() >0) {
 				for(ProductFeatureHasTestCase feature:featureHasTestCaseList) {
 					featureIds.add(feature.getTestCaseId());
 				}
 			}
 			
 			Criteria feature  = sessionFactory.getCurrentSession().createCriteria(ProductFeature.class,"feature");
 			feature.add(Restrictions.in("feature.productFeatureId", featureIds));
 			featureList= feature.list();
		} catch (RuntimeException re) {
			log.error("Unable to fetch productFeatureHasTestCaseList", re);
		}
 		return featureList;
	}
	
	
	@Override
	@Transactional
	public Integer getMappedFeatureCountOfTestCasesByProductId(int productId) {
		log.info("Get Total mapped feature count of testcases");
		int totMappedTCListCount = 0;
		try {
			totMappedTCListCount=((Number) sessionFactory.getCurrentSession().createSQLQuery(
					"SELECT COUNT(DISTINCT pfm.productFeatureId) FROM product_feature_has_test_case_list pfm, product_feature pf WHERE pfm.productFeatureId = pf.productFeatureId AND pf.productId = :pId").
			setParameter("pId", productId).uniqueResult()).intValue();
			
		} catch (HibernateException e) {
			log.error(e);
		}		
		return totMappedTCListCount;
	}
	
	@Override
	@Transactional
	public Integer getMappedFeatureCountByTestcaseId(int testCaseId) {
		log.info("Get  MappedFeatureCountByTestcaseId");
		int totMappedTCListCount = 0;
		try {
			totMappedTCListCount=((Number) sessionFactory.getCurrentSession().createSQLQuery(
							"select count(pfte.testCaseId) from product_feature_has_test_case_list pfte where pfte.testCaseId=:testCaseId").
					setParameter("testCaseId", testCaseId).uniqueResult()).intValue();			
			
		} catch (HibernateException e) {
			log.error(e);
		}		
		return totMappedTCListCount;
	}

	@Override
	public Integer getMappedTestcasecountByFeatureId(int productFeatureId) {
		int totMappedTestCaseCount = 0;
		try {
			totMappedTestCaseCount=((Number) sessionFactory.getCurrentSession().createSQLQuery(
							"select count(pfte.productFeatureId) from product_feature_has_test_case_list pfte where pfte.productFeatureId=:productFeatureId").
					setParameter("productFeatureId", productFeatureId).uniqueResult()).intValue();			
			
		} catch (HibernateException e) {
			log.error(e);
		}		
		return totMappedTestCaseCount;
	}
	
	@Override
	@Transactional
	public String updateProductFeatureTestCase(int testCaseId, int productFeatureId, String maporunmap) {
		log.info("updateProductFeatureTestCases method updating ProductFeature - TestCase Association");
		TestCaseList testCaseList = null;
		ProductFeature productFeature = null;
		Set<TestCaseList> testCaseSet = null;
		try{
			testCaseList = testCaseListDAO.getByTestCaseId(testCaseId);
			productFeature = getByProductFeatureId(productFeatureId);		
			if (testCaseList != null && productFeature != null){
				if(maporunmap.equalsIgnoreCase("map")){
					if(isMappingExistsTestcaseIdandFeatureId(productFeatureId,testCaseId)){
						ProductFeature pf = productFeature;
						pf.getTestCaseList().add(testCaseList);
						sessionFactory.getCurrentSession().saveOrUpdate(pf);
						return "Testcase : " + testCaseId + " mapped to feature : " + productFeatureId;
					} else {
						return "Testcase : " + testCaseId + " Already mapped to feature : " + productFeatureId;
					}
				}else if(maporunmap.equalsIgnoreCase("unmap")){
					testCaseSet = productFeature.getTestCaseList();
					if(testCaseSet.size() != 0){
						if(testCaseSet.contains(testCaseList)){
							ProductFeature pf1 = productFeature;
							pf1.getTestCaseList().remove(testCaseList);
							sessionFactory.getCurrentSession().saveOrUpdate(pf1);
							return "Testcase : " + testCaseId + " unmapped from feature : " + productFeatureId; 
						} else {
							return "Testcase : " + testCaseId + " not mapped to feature : " + productFeatureId; 
						}
					} else {
						return "Testcase : " + testCaseId + " not mapped to feature : " + productFeatureId; 
					}
				} else {
					return "Action not valid for mapping/unmapping test case : " + testCaseId + " and feature : " + productFeatureId + " are invalid";
				}
			} else {
				return "Either test case : " + testCaseId + " or feature : " + productFeatureId + " are invalid";
			}
		} catch (RuntimeException re) {
			log.error("list specific failed", re);
			return "Problem while mapping/unmapping test case : " + testCaseId + " and feature : " + productFeatureId + " : " + re.getMessage();		
		}
	}


	
	private boolean isMappingExistsTestcaseIdandFeatureId(int productFeatureId,int testCaseId) {
		boolean isMapped=true;
		Integer totMappedTestCaseCount=0;
		try {
			totMappedTestCaseCount=((Number) sessionFactory.getCurrentSession().createSQLQuery(
							"select count(pfte.productFeatureId) from product_feature_has_test_case_list pfte where pfte.productFeatureId=:productFeatureId and pfte.testcaseid=:testCaseId").
					setParameter("productFeatureId", productFeatureId).setParameter("testCaseId", testCaseId).uniqueResult()).intValue();
			if(totMappedTestCaseCount == 0) {
				return isMapped;
			} else {
				return !isMapped;
			}
			
		} catch (HibernateException e) {
			log.error(e);
		}		
		return isMapped;
	}
	
	@Override
	@Transactional
	public boolean isFeatureExistingByFeatureNameAndFeatureCode(String featureName,String productFeatureCode, ProductMaster product) {

		String hql = "from ProductFeature pf where productFeatureName = :name and productFeatureCode = :code and pf.productMaster.productId = :productId";
		List productFeatures = sessionFactory.getCurrentSession().createQuery(hql)
				.setParameter("code", productFeatureCode)
				.setParameter("name", featureName)
				.setParameter("productId", product.getProductId()).list();
		if (productFeatures == null || productFeatures.isEmpty()) 
		    return false;
		else 
			return true;
	}
	
}
