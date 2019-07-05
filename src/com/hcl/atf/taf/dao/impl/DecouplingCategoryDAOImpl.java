package com.hcl.atf.taf.dao.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.SQLQuery;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.hcl.atf.taf.dao.DecouplingCategoryDAO;
import com.hcl.atf.taf.dao.TestCaseListDAO;
import com.hcl.atf.taf.dao.utilities.HibernateCustomOrderByForHierarchicalEntities;
import com.hcl.atf.taf.model.DecouplingCategory;
import com.hcl.atf.taf.model.ProductFeature;
import com.hcl.atf.taf.model.TestCaseList;
import com.hcl.atf.taf.model.TestSuiteList;
import com.hcl.atf.taf.model.WorkPackageTestCaseExecutionPlan;

@Repository
public class DecouplingCategoryDAOImpl implements DecouplingCategoryDAO {
	private static final Log log = LogFactory.getLog(DecouplingCategoryDAOImpl.class);
	
	private static final String ENTITY_TABLE_NAME = "decoupling_category";
	
	@Autowired(required=true)
    private SessionFactory sessionFactory;
	
	@Autowired
	private TestCaseListDAO testCaseListDAO;

	@Override
	@Transactional
	public List<DecouplingCategory> getDecouplingCategoryListByProductId(int productId) {
		log.debug("getDecouplingCategoryListByProductId method listing all Decoupling Categories for a productId and status=1");
		List<DecouplingCategory> decouplingCategoryList = null;
		
		try {
			decouplingCategoryList=sessionFactory.getCurrentSession().createQuery(
					"from "
					+ "DecouplingCategory dc where (dc.product.productId=:productId) and (status= 1)")
					.setParameter("productId", productId).list();
			if (!(decouplingCategoryList == null || decouplingCategoryList.isEmpty())) {
				
				for (DecouplingCategory dc : decouplingCategoryList) {
					
					if(dc.getParentCategory()!=null)
						Hibernate.initialize(dc.getParentCategory());
					Hibernate.initialize(dc.getChildCategories());
					Hibernate.initialize(dc.getProduct());
					Hibernate.initialize(dc.getUserTypeMasterNew());
					if(dc.getTestCaseList()!=null){
						Hibernate.initialize(dc.getTestCaseList());
					}
				}
			}
			log.debug("list specific successful");
		} catch (RuntimeException re) {
			log.error("list specific failed", re);
			//throw re;
		}
		return decouplingCategoryList;
	}
	
	@Override
	@Transactional
	public DecouplingCategory getDecouplingCategoryById(int decouplingCategoryId) {
		log.debug("getDecouplingCategoryById methood listing Decoupling Category by id");
		DecouplingCategory decouplingCategory = null;
		
		try {
			List list =sessionFactory.getCurrentSession().createQuery(
					"from "
					+ "DecouplingCategory where decouplingCategoryId=:decouplingCategoryId")
					.setParameter("decouplingCategoryId", decouplingCategoryId).list();
			
			decouplingCategory=(list!=null && list.size()!=0)?(DecouplingCategory)list.get(0):null;
			if (decouplingCategory != null) {
				Hibernate.initialize(decouplingCategory.getTestCaseList());
				Set<TestCaseList> tcSet = decouplingCategory.getTestCaseList();
				for (TestCaseList testCaseList : tcSet) {
					Hibernate.initialize(testCaseList.getProductMaster());
					Hibernate.initialize(testCaseList.getExecutionTypeMaster());
					Hibernate.initialize(testCaseList.getTestCasePriority());
					Hibernate.initialize(testCaseList.getTestcaseTypeMaster());
					Hibernate.initialize(testCaseList.getDecouplingCategory());
					Hibernate.initialize(testCaseList.getProductFeature());
				}
				
				Hibernate.initialize(decouplingCategory.getParentCategory());
				Hibernate.initialize(decouplingCategory.getProduct());
				Hibernate.initialize(decouplingCategory.getUserTypeMasterNew());
			}
			log.debug("list specific successful");
		} catch (RuntimeException re) {
			log.error("list specific failed", re);
		}
		return decouplingCategory;
	}
	
	@Override
	@Transactional
	public DecouplingCategory getDecouplingCategoryByName(String decouplingCategoryName) {
		log.debug("getDecouplingCategoryByName");
		DecouplingCategory decouplingCategory = null;		
		try{
			List list = sessionFactory.getCurrentSession().createQuery("from DecouplingCategory dc where dc.decouplingCategoryName=:categoryname")
					.setParameter("categoryname", decouplingCategoryName).list();
			decouplingCategory =(list!=null && list.size()!=0)?(DecouplingCategory) list.get(0):null;
			if(decouplingCategory !=null){
				Hibernate.initialize(decouplingCategory.getTestCaseList());
				Hibernate.initialize(decouplingCategory.getParentCategory());
				Hibernate.initialize(decouplingCategory.getProduct());
				Hibernate.initialize(decouplingCategory.getUserTypeMasterNew());
			}
		}catch(IndexOutOfBoundsException ae){
			
		}catch(RuntimeException re){
			log.error("getDecouplingCategoryByName",re);
			return decouplingCategory;
		}
		return decouplingCategory;		
	}
	
	
	@Override
	@Transactional
	public TestCaseList updateDecouplingCategoriesTestCases(int testCaseId, int decouplingCategoryId) {
		log.info("updateDecouplingCategoriesTestCases method updating Decoupling Category - TestCase Association");
		//TestCases Decoupling Category have many to many relationship. Here we are mimicking oneToOne relationship on
		//manyToMany relationship. i.e We are preventing manyToMany data updation.
		
		//boolean updationstatus = false;
		TestCaseList testCaseList = null;
		DecouplingCategory decouplingCategory = null;
		
		try {
			testCaseList = testCaseListDAO.getByTestCaseId(testCaseId);
			decouplingCategory = getDecouplingCategoryById(decouplingCategoryId);
		
			if (testCaseList != null && decouplingCategory != null)
			{
				boolean needToUpdateOrAdd = false;
				
				Set<DecouplingCategory> decouplingCategorySet = testCaseList.getDecouplingCategory();
				// if no associated record exists need to add the new records
				if (decouplingCategorySet.size() == 0)
				{
					needToUpdateOrAdd = true;
				}	
				else {
					// Associated record exists
					log.info("size > 0");
					DecouplingCategory decouplingCategoryForProcessing = decouplingCategorySet.iterator().next();
					if (decouplingCategoryForProcessing != null) {
						log.info("decouplingCategoryForProcessing not null");
						int alreadyAvailableDecouplingCategoryId = decouplingCategoryForProcessing.getDecouplingCategoryId().intValue();

						// If associated record is for the same decoupling category, don't do any addition/updation
						if (alreadyAvailableDecouplingCategoryId != decouplingCategoryId) {	
							
							// Now association exists for different decoupling category, so that association need to be cleared in testcase object first
							log.info("values or different");
							testCaseList.getDecouplingCategory().clear();
							log.info("testCaseList.getDecouplingCategory().size()="+testCaseList.getDecouplingCategory().size());
							//sessionFactory.getCurrentSession().saveOrUpdate(testCaseList);
							
							
							// Now association exists for different decoupling category, 
							// so that association need to be cleared for decouplecategory
							DecouplingCategory decouplingCategoryAvailable = getDecouplingCategoryById(alreadyAvailableDecouplingCategoryId);
							for (TestCaseList tc: decouplingCategoryAvailable.getTestCaseList())
							{
								log.info("tc.getTestCaseId().intValue()"+tc.getTestCaseId().intValue());
							
								// Now association exists for different decoupling category, but for same testcase id, 
								// so that association needs to be removed from decouplecategory object
								if (tc.getTestCaseId().intValue() == testCaseId) {	
									log.info("testcase found in decouple");
									decouplingCategoryAvailable.getTestCaseList().remove(tc);
									sessionFactory.getCurrentSession().saveOrUpdate(decouplingCategoryAvailable);
									log.info("decouplingCategoryAvailable.getTestCaseList().size()="+decouplingCategoryAvailable.getTestCaseList().size());
									break;
								}	
							}
								
							needToUpdateOrAdd = true;
						}	
					}
				}	
				
				if (needToUpdateOrAdd) {
					testCaseList.getDecouplingCategory().add(decouplingCategory);
					decouplingCategory.getTestCaseList().add(testCaseList);	
					sessionFactory.getCurrentSession().saveOrUpdate(testCaseList);
					sessionFactory.getCurrentSession().saveOrUpdate(decouplingCategory);
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
	public TestCaseList updateDecouplingCategoriesTestCasesOneToMany(Integer testCaseId, int decouplingCategoryId, String maporunmap) {
		log.info("updateDecouplingCategoriesTestCasesOneToMany method updating DecouplingCategories - TestCase Association");
		
		TestCaseList testCaseList = null;
		DecouplingCategory decouplingCategory = null;
		Set<TestCaseList> testCaseSet = null;
		try{
			testCaseList = testCaseListDAO.getByTestCaseId(testCaseId);
			decouplingCategory = getDecouplingCategoryById(decouplingCategoryId);
			
			if (testCaseList != null && decouplingCategory != null){
				boolean needToUpdateOrAdd = false;
				if(maporunmap.equalsIgnoreCase("map")){				
					DecouplingCategory dc = decouplingCategory;
					dc.getTestCaseList().add(testCaseList);
					sessionFactory.getCurrentSession().saveOrUpdate(dc);					
				}else if(maporunmap.equalsIgnoreCase("unmap")){
					testCaseSet = decouplingCategory.getTestCaseList();
					//testCaseSet.remove(testCaseList);
					if(testCaseSet.size() != 0){
						if(testCaseSet.contains(testCaseList)){
							DecouplingCategory dc1 = decouplingCategory;
							dc1.getTestCaseList().remove(testCaseList);
							sessionFactory.getCurrentSession().saveOrUpdate(dc1);
						}					
					}					
				}				
			}				
		} catch (RuntimeException re) {
			log.error("updateDecouplingCategoriesTestCasesOneToMany failed", re);
			re.printStackTrace();
		}
		return testCaseList;		
	}
	
	@Override
	@Transactional
	public List<DecouplingCategory> getDecouplingCategoriesMappedToTestCase(Integer testCaseId) {
		log.debug(" getDecouplingCategoriesMappedToTestCase");
		List<DecouplingCategory> decouplingList = new ArrayList<DecouplingCategory>();
		
		List<ProductFeature> featureList = new ArrayList<ProductFeature>();		
		List<TestCaseList> testCaseList=new ArrayList<TestCaseList>();		
		try {			 
			Criteria c = sessionFactory.getCurrentSession().createCriteria(TestCaseList.class,"testcase");			
			c.add(Restrictions.eq("testcase.testCaseId", testCaseId));
			testCaseList =c.list();			
			Set<TestSuiteList> testSuiteLists = null;	
			Set<ProductFeature> featureSet = null;	
			Set<DecouplingCategory> decouplingSet = null;
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
					if(tCase.getDecouplingCategory() != null){
						decouplingSet = tCase.getDecouplingCategory();						
						decouplingList.addAll(decouplingSet);
						Hibernate.initialize(decouplingSet);	
						for (DecouplingCategory decouplingCategory : decouplingSet) {
							Hibernate.initialize(decouplingCategory.getParentCategory());	
							Hibernate.initialize(decouplingCategory.getChildCategories());
							Hibernate.initialize(decouplingCategory.getTestCaseList());
							Hibernate.initialize(decouplingCategory.getUserTypeMasterNew());
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
			log.debug("getDecouplingCategoriesMappedToTestCase successful");			
			} catch (RuntimeException re) {
				log.error("getDecouplingCategoriesMappedToTestCase failed", re);
				//throw re;
			}
		return decouplingList;
	}
	@Override
	@Transactional
	public List<DecouplingCategory> list() {
		log.debug("listing all Product Decoupling Category instance");
		List<DecouplingCategory> decouplingCategory = null;
		try{
			decouplingCategory = sessionFactory.getCurrentSession().createQuery("from DecouplingCategory").list();
			if (!(decouplingCategory == null || decouplingCategory.isEmpty())) {
				
				for (DecouplingCategory dc : decouplingCategory) {
					
					if(dc.getParentCategory()!=null)
						Hibernate.initialize(dc.getParentCategory());
					if(dc.getProduct()!=null && dc.getProduct().getProductId()!=null)
						Hibernate.initialize(dc.getProduct());
						Hibernate.initialize(dc.getUserTypeMasterNew());
						Hibernate.initialize(dc.getTestCaseList());
						Hibernate.initialize(dc.getUserTypeMasterNew());
				}
			}
			log.debug("list all successful");
		}catch(RuntimeException re){
			log.error("list all failed", re);
		}		
		return decouplingCategory;
	}

	@Override
	@Transactional
	public boolean isProductDecouplingCategoryExistingByName(DecouplingCategory decouplingCategory) {
		String hql = "from DecouplingCategory dc where dc.decouplingCategoryName = :name";
		List instances = sessionFactory.getCurrentSession().createQuery(hql).setParameter("name", decouplingCategory.getDecouplingCategoryName().trim()).list();
		if (instances == null || instances.isEmpty()) 
		    return false;
		else 
			return true;
	}
	
	@Override
	@Transactional
	public boolean isProductDecouplingCategoryExistingByName(DecouplingCategory decouplingCategory, Integer productId) {
		log.info("verifying if any Decoupling Category existing by same name to the product");
		Criteria c = sessionFactory.getCurrentSession().createCriteria(DecouplingCategory.class,"dcoupling");			
		c.createAlias("dcoupling.product", "prod");
		c.add(Restrictions.eq("prod.productId",productId));
		
		c.add(Restrictions.eq("dcoupling.decouplingCategoryName", decouplingCategory.getDecouplingCategoryName()));
		List decouplingObj =c.list();	
		
		if (decouplingObj == null || decouplingObj.isEmpty()) 
		    return false;
		else 
			return true;
	}
	
	//@Override 
	public List<Integer> getAncestors(int decouplingCategoryId) {
	
		String ANCESTORS_HIERARCHY_QUERY = "SELECT id from decoupling_category where leftIndex < (select leftIndex from decoupling_category where id=:"+ decouplingCategoryId + ") " + " and rightIndex > (select rightIndex from decoupling_category where id = :" + decouplingCategoryId + " )";

        SQLQuery fetchQuery = sessionFactory.getCurrentSession().createSQLQuery(ANCESTORS_HIERARCHY_QUERY);
		List<Integer> ancestorIds = fetchQuery.list();
		if (ancestorIds == null) {
			ancestorIds = new ArrayList<Integer>();
		}
		return ancestorIds;
	}

	//@Override 
	public List<Integer> getDescendants(int decouplingCategoryId) {

		String DESCENDANTS_HIERARCHY_QUERY = "SELECT id from decoupling_category where leftIndex > (select leftIndex from decoupling_category where id=:" + decouplingCategoryId + ") "+ " and rightIndex < (select rightIndex from decoupling_category where id = :" + decouplingCategoryId + " )";
        SQLQuery fetchQuery = sessionFactory.getCurrentSession().createSQLQuery(DESCENDANTS_HIERARCHY_QUERY);
		List<Integer> descendentIds = fetchQuery.list();
		if (descendentIds == null) {
			descendentIds = new ArrayList<Integer>();
		}
		return descendentIds;
	}

	@Override
	@Transactional
	public void add(DecouplingCategory decouplingCategory) {
		log.debug("adding ProductDecoupling Category instance");
		try {

			//Logic for creating the initial Decoupling category. The main root node.
			//There will be only one root node 
			List<DecouplingCategory> decouplingCategories = list();
			DecouplingCategory rootDC = null;
			if (decouplingCategories == null) {
				//Some problem in finding out decoupling categories size. Will lead to inconsistent state
				//Abort adding.
				log.error("Add failed as could not find DC objects");
				return;
			} else if (decouplingCategories.size() <= 0) {

				//There are no categories in the DB. Seed the main root node
				rootDC = new DecouplingCategory();
				rootDC.setDecouplingCategoryName("Decoupling Categories");
				rootDC.setDescription("Root Decoupling Category");
				rootDC.setDisplayName("Decoupling Categories");
				rootDC.setLeftIndex(1);
				rootDC.setRightIndex(2);
				rootDC.setStatus(1);
				rootDC.setCreatedDate(new Date(System.currentTimeMillis()));
				rootDC.setModifiedDate(new Date(System.currentTimeMillis()));
				sessionFactory.getCurrentSession().save(rootDC);
			} 
			
			decouplingCategory.setStatus(1);
			if (decouplingCategory.getParentCategory() == null) {
		        //If no parent category is specified, make the root category the parent category
				rootDC = getDecouplingCategoryByName("Decoupling Categories");
				//TODO : Get the DC without parent. This will be the root DC. There will be only one root DC,
				//one without a parent DC
				decouplingCategory.setParentCategory(rootDC);
			}	
			
	        DecouplingCategory parentDC = decouplingCategory.getParentCategory();
			int leftIndex = parentDC.getRightIndex();
			int rightIndex = parentDC.getRightIndex() + 1;
	        updateHierarchyIndexForNew(ENTITY_TABLE_NAME, parentDC.getRightIndex());
			decouplingCategory.setLeftIndex(leftIndex);
			decouplingCategory.setRightIndex(rightIndex);
			sessionFactory.getCurrentSession().save(decouplingCategory);
			log.debug("add successful");
		} catch (RuntimeException re) {
			log.error("add failed", re);
			//throw re;
		}
	}

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
	public void delete(DecouplingCategory decouplingCategory) {
		log.debug("updating ProductDecoupling instance");
		try {
			
			//Delete only if the DC has no child categories
			if (decouplingCategory.getChildCategories() == null || decouplingCategory.getChildCategories().size() <= 0) {
				
				updateHierarchyIndexForDelete(ENTITY_TABLE_NAME, decouplingCategory.getLeftIndex(), decouplingCategory.getRightIndex());
				sessionFactory.getCurrentSession().delete(decouplingCategory);
			} else {
				
				log.info("Delete not allowed as the DC has child DC. Delete them first");
			}
			log.debug("update successful");
		} catch (RuntimeException re) {
			log.error("update failed", re);
			//throw re;
		}		
	}

	
	@Override
	@Transactional
	public void update(DecouplingCategory decouplingCategoryFromUI) {
		log.debug("updating ProductDecoupling instance");
		try {
			sessionFactory.getCurrentSession().saveOrUpdate(decouplingCategoryFromUI);
			log.debug("update successful");
		} catch (RuntimeException re) {
			log.error("update failed", re);
		}		
	}

	@Override
	@Transactional
	public List<DecouplingCategory> getDCByWorkpackage(List<WorkPackageTestCaseExecutionPlan> workPackageTestCaseExecutionPlanList,int workPackageId) {
		// TODO Auto-generated method stub
		log.debug("getDCByWorkpackage method listing all Decoupling Categories for a workPackageId and status=1");
		List<DecouplingCategory> decouplingCategoryList = new ArrayList<DecouplingCategory>();
		DecouplingCategory decouplingCategory=null;
		try {
			
			for(WorkPackageTestCaseExecutionPlan workPackageTestCaseExecutionPlan:workPackageTestCaseExecutionPlanList){
				if(workPackageTestCaseExecutionPlan.getTestCase()!=null && workPackageTestCaseExecutionPlan.getTestCase().getDecouplingCategory()!=null && !workPackageTestCaseExecutionPlan.getTestCase().getDecouplingCategory().isEmpty()){
					decouplingCategory=workPackageTestCaseExecutionPlan.getTestCase().getDecouplingCategory().iterator().next();
					if(decouplingCategory!=null){
						if(decouplingCategoryList!=null && !decouplingCategoryList.isEmpty() && !decouplingCategoryList.contains(decouplingCategory)){
							decouplingCategoryList.add(decouplingCategory);
						}else if(decouplingCategoryList==null || decouplingCategoryList.isEmpty()){
							decouplingCategoryList.add(decouplingCategory);
						}
					}
				}
			}
			if (!(decouplingCategoryList == null || decouplingCategoryList.isEmpty())) {
				
				for (DecouplingCategory dc : decouplingCategoryList) {
					
					if(dc.getParentCategory()!=null)
						Hibernate.initialize(dc.getParentCategory());
						Hibernate.initialize(dc.getProduct());
						Hibernate.initialize(dc.getUserTypeMasterNew());
						Hibernate.initialize(dc.getTestCaseList());
						Hibernate.initialize(dc.getUserTypeMasterNew());
				}
			}
			log.debug("list specific successful"+decouplingCategoryList.size());
		} catch (RuntimeException re) {
			log.error("list specific failed", re);
			//throw re;
		}
		return decouplingCategoryList;
	}

	@Override
	@Transactional
	public int getRootDecouplingCategoryId() {
		Integer rootDecouplingCategoryId = 0;
		return rootDecouplingCategoryId;
	}
	
	@Override
	@Transactional
	public List<DecouplingCategory> getChildCategoriesListByParentCategoryId(Integer parentCategoryId) {
		log.debug("listing all child DCs for parent DC");
		List<DecouplingCategory> childDCList = null;
		try{
			childDCList=sessionFactory.getCurrentSession().createQuery("from DecouplingCategory dc where dc.parentCategory.decouplingCategoryId=:parentCategoryId and status=1 order by dc.leftIndex asc")
					.setParameter("parentCategoryId", parentCategoryId).list();	
			if(!(childDCList == null || childDCList.isEmpty())){
				for(DecouplingCategory childDc : childDCList){
					if(childDc.getParentCategory() != null){
						Hibernate.initialize(childDc.getParentCategory());
					}			
					Hibernate.initialize(childDc.getChildCategories());					
					if(childDc.getTestCaseList() != null){
						Set<TestCaseList> tCaseSet = childDc.getTestCaseList();
						Hibernate.initialize(tCaseSet);
						for (TestCaseList testCaseList : tCaseSet) {							
							Hibernate.initialize(testCaseList.getTestCaseStepsLists());
							Hibernate.initialize(testCaseList.getTestSuiteLists());
							Hibernate.initialize(testCaseList.getDecouplingCategory());				
							Hibernate.initialize(testCaseList.getTestExecutionResults());								
							Hibernate.initialize(testCaseList.getTestSuiteLists());
						}
						
					}
					Hibernate.initialize(childDc.getProduct());
				}
			}
			log.debug("list all successful");			
		}catch(RuntimeException re){
			log.error("list all failed", re);
		}
		return childDCList;
	}

	@Override
	@Transactional
	public Integer geRootDecouplingCategoryId(String rootDecouplingCategoryDescription) {
		log.debug("getting Root Decoupling Category Id");
		Integer rootDecouplingCategoryId =0;
		DecouplingCategory decouplingCategory = null;
		
		try {
			List list =sessionFactory.getCurrentSession().createQuery(
					"from "
					+ "DecouplingCategory where description=:rootDecouplingCategoryDescription")
					.setParameter("rootDecouplingCategoryDescription", rootDecouplingCategoryDescription).list();
			
			decouplingCategory=(list!=null && list.size()!=0)?(DecouplingCategory)list.get(0):null;
			if (decouplingCategory != null) {
				rootDecouplingCategoryId = decouplingCategory.getDecouplingCategoryId();
			}
			log.debug("Fetch Root Decoupling Category success");
		} catch (RuntimeException re) {
			log.error("Fetch Root Decoupling Category failed", re);			
		}
		return rootDecouplingCategoryId;		
	}

	@Override
	public DecouplingCategory getRootDecouplingCategory() {
		log.debug("getting RootDecouplingCategory instance");
		DecouplingCategory decouplingCategory = null;
		try {
			 decouplingCategory = getDecouplingCategoryByName("Decoupling Categories");
		} catch (RuntimeException re) {
			log.error("Fetching Root Decoupling Category failed", re);
		}
		return decouplingCategory;
	}
	
	@Override
	@Transactional
	public List<DecouplingCategory> listChildNodesInHierarchyinLayers(DecouplingCategory decouplingCategory) {
		log.debug("listing all child nodes in hierarchy");
		List<DecouplingCategory> decouplingCategories=null;
		try {
			
			Criteria c = sessionFactory.getCurrentSession().createCriteria(DecouplingCategory.class);
			c.add(Restrictions.gt("leftIndex", decouplingCategory.getLeftIndex()));
			c.add(Restrictions.lt("rightIndex", decouplingCategory.getRightIndex()));
			
			c.addOrder(HibernateCustomOrderByForHierarchicalEntities.sqlFormula("(rightIndex-leftIndex) desc"));
			c.addOrder(Order.asc("leftIndex"));
			decouplingCategories = c.list();	
			
			log.debug("listing child nodes in hierarchy successful");
		} catch (RuntimeException re) {
			log.error("listing child nodes in hierarchy failed", re);
		}
		return decouplingCategories;
	}
	
}
