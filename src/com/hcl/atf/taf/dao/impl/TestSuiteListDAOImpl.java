package com.hcl.atf.taf.dao.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.hcl.atf.taf.constants.TAFConstants;
import com.hcl.atf.taf.dao.ProductMasterDAO;
import com.hcl.atf.taf.dao.TestSuiteListDAO;
import com.hcl.atf.taf.model.ProductMaster;
import com.hcl.atf.taf.model.ProductVersionListMaster;
import com.hcl.atf.taf.model.TestCaseList;
import com.hcl.atf.taf.model.TestCaseStepsList;
import com.hcl.atf.taf.model.TestManagementSystem;
import com.hcl.atf.taf.model.TestRunPlan;
import com.hcl.atf.taf.model.TestSuiteList;
import com.hcl.atf.taf.model.WorkPackage;

@Repository
public class TestSuiteListDAOImpl implements TestSuiteListDAO {
	private static final Log log = LogFactory.getLog(TestSuiteListDAOImpl.class);
	
	@Autowired(required=true)
    private SessionFactory sessionFactory;
	
	@Autowired
	private ProductMasterDAO productDAO; 
	
	@Override
	@Transactional
	public void delete(TestSuiteList testSuiteList) {
		log.debug("deleting TestSuiteList instance");
		try {		
				testSuiteList.setStatus(0);
				testSuiteList.setStatusChangeDate(new Date(System.currentTimeMillis()));
				update(testSuiteList);			
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
		}		
		
	}
	
	@Override
	@Transactional
	public void reactivate(TestSuiteList testSuiteList) {
		log.debug("deleting TestSuiteList instance");
		try {		
				testSuiteList.setStatus(TAFConstants.ENTITY_STATUS_ACTIVE);
				testSuiteList.setStatusChangeDate(new Date(System.currentTimeMillis()));
				update(testSuiteList);			
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
		}		
		
	}
	
	@Override
	@Transactional
	public void delete(Set<TestSuiteList> testSuiteLists) {
		log.debug("deleting TestSuiteList instance");
		try {
			for(TestSuiteList testSuiteList: testSuiteLists){				
				testSuiteList.setStatus(0);
				testSuiteList.setStatusChangeDate(new Date(System.currentTimeMillis()));
				update(testSuiteList);
			}
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
		}		
		
	}
	
	@Override
	@Transactional
	public void reactivate(Set<TestSuiteList> testSuiteLists) {
		log.debug("deleting TestSuiteList instance");
		try {
			for(TestSuiteList testSuiteList: testSuiteLists){				
				testSuiteList.setStatus(TAFConstants.ENTITY_STATUS_ACTIVE);
				testSuiteList.setStatusChangeDate(new Date(System.currentTimeMillis()));
				update(testSuiteList);
			}
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
		}		
		
	}


	@Override
	@Transactional
	public int add(TestSuiteList testSuiteList) {
		log.debug("adding TestSuiteList instance");
		try {
			testSuiteList.setStatus(1);
			testSuiteList.setStatusChangeDate(new Date(System.currentTimeMillis()));
			sessionFactory.getCurrentSession().save(testSuiteList);
			log.debug("add successful");
		} catch (RuntimeException re) {
			log.error("add failed", re);
		}
		return testSuiteList.getTestSuiteId();
				
	}
	
	@Override
	@Transactional
	public int addTestSuiteMapping(int testSuiteId, int testSuiteIdtobeMapped) {
		log.info("Mapping TestSuite to TestSuite instance");
		TestCaseList testCaseList = null;
		TestSuiteList testSuiteList = null;
		TestSuiteList testSuiteToBeMapped = null;
		try {
		
			testSuiteList = (TestSuiteList) sessionFactory.getCurrentSession().get(TestSuiteList.class,testSuiteId);
			if (testSuiteList == null) {
				log.debug("TestSuite with specified id not found : " + testSuiteId);
			}

			testSuiteToBeMapped = (TestSuiteList) sessionFactory.getCurrentSession().get(TestSuiteList.class, testSuiteIdtobeMapped);
			if (testCaseList == null) {
				log.debug("MappedTestSuite with specified id not found : " + testSuiteIdtobeMapped);
			}
			
			Set<TestSuiteList> testSuiteMappedList = testSuiteList.getTestSuiteList();
			testSuiteMappedList.add(testSuiteToBeMapped);
			testSuiteList.setTestSuiteList(testSuiteMappedList);
			
			sessionFactory.getCurrentSession().save(testSuiteList);
			log.error("Added TestCase to TestSuite");
		} catch (RuntimeException re) {
			log.error("Failed to add TestCase to TestSuite", re);
		}
		return testSuiteList.getTestSuiteId().intValue();
	}
	
	@Override
    @Transactional
    public void update(TestSuiteList testSuiteList) {
		log.debug("updating TestSuiteList instance");
		try {			
			sessionFactory.getCurrentSession().saveOrUpdate(testSuiteList);
			log.info("Updated Test Suite  from update : " + testSuiteList.getTestSuiteId() + " : " + testSuiteList.getTestSuiteScriptFileLocation()  + " : " + testSuiteList.getTestSuiteName() );
			log.debug("update successful");
		} catch (RuntimeException re) {
			log.error("update failed", re);
		}	
		
	}
    
	
	
	@Override
	@Transactional
	public List<TestSuiteList> list() {

		log.debug("listing all TestSuiteList instance");
		List<TestSuiteList> testSuiteList=null;
		try {
			testSuiteList=sessionFactory.getCurrentSession().createQuery("from TestSuiteList").list();
			log.debug("list all successful");
		} catch (RuntimeException re) {
			log.error("list all failed", re);
		}
		return testSuiteList;
	}
	
	@Override
	@Transactional
	public List<TestSuiteList> list(int status) {

		log.debug("listing all TestSuiteList instance");
		List<TestSuiteList> testSuiteList=null;
		if(status == TAFConstants.ENTITY_STATUS_ALL){
			return list();
		}
		try {
			testSuiteList=sessionFactory.getCurrentSession().createQuery("from TestSuiteList where status=:status")
			.setParameter("status",status)
			.list();
			log.debug("list all successful");
		} catch (RuntimeException re) {
			log.error("list all failed", re);
		}
		return testSuiteList;
	}
	
	@Override
	@Transactional
    public List<TestSuiteList> list(int startIndex, int pageSize) {
		log.debug("listing TestSuiteList instance");
		List<TestSuiteList> testSuiteList=null;
		try {
			testSuiteList=sessionFactory.getCurrentSession().createQuery("from TestSuiteList")
	                .setFirstResult(startIndex)
	                .setMaxResults(pageSize)
	                .list();
			if (!(testSuiteList == null || testSuiteList.isEmpty())){
				for(TestSuiteList tsl : testSuiteList){
					Hibernate.initialize(tsl.getProductMaster());
				}
			}
			log.debug("list successful");
		} catch (RuntimeException re) {
			log.error("list failed", re);
		}
		return testSuiteList;       
    }
	
	@Override
	@Transactional
    public List<TestSuiteList> list(int startIndex, int pageSize, int status) {
		log.debug("listing TestSuiteList instance");
		List<TestSuiteList> testSuiteList=new ArrayList<TestSuiteList>();
		if(status == TAFConstants.ENTITY_STATUS_ALL){
			return list(startIndex, pageSize);
		}
		try {
			testSuiteList=sessionFactory.getCurrentSession().createQuery("from TestSuiteList where status=:status")
					.setParameter("status",status)
	                .setFirstResult(startIndex)
	                .setMaxResults(pageSize)	                
	                .list();
			if (!(testSuiteList == null || testSuiteList.isEmpty())){
				for(TestSuiteList tsl : testSuiteList){
					Hibernate.initialize(tsl.getProductMaster());
					Hibernate.initialize(tsl.getProductVersionListMaster());					
					if (tsl.getProductVersionListMaster() != null) {
					}
					
					Hibernate.initialize(tsl.getTestCaseLists());
					if(tsl.getTestCaseLists()!= null) {
						for(TestCaseList testCaseList : tsl.getTestCaseLists()){
							Hibernate.initialize(testCaseList.getTestCaseStepsLists());							
						}
					}
				}
			}
			log.debug("list successful");
		} catch (RuntimeException re) {
			log.error("list failed", re);
		}
		return testSuiteList;       
    }
	
	
	@Override
	@Transactional
	public TestSuiteList getByTestSuiteId(int testSuiteId){
		log.info("getting TestSuiteList instance by id");
		TestSuiteList testSuiteList=null;
		try {
			Criteria c = sessionFactory.getCurrentSession().createCriteria(TestSuiteList.class,"tsuite");			
			c.add(Restrictions.eq("tsuite.testSuiteId", testSuiteId));
			
			List list = c.list();
			testSuiteList=(list!=null && list.size()!=0)?(TestSuiteList) list.get(0):null;
			if (testSuiteList != null){
				Hibernate.initialize(testSuiteList.getProductMaster());
				
				Hibernate.initialize(testSuiteList.getProductVersionListMaster());
				if (testSuiteList.getProductVersionListMaster() != null)
				{
					Hibernate.initialize(testSuiteList.getProductVersionListMaster());
				}				
				if(testSuiteList.getScriptTypeMaster()!=null){
					Hibernate.initialize(testSuiteList.getScriptTypeMaster());
				}
					if(testSuiteList.getTestCaseLists().size()!=0 ){	
					Hibernate.initialize(testSuiteList.getTestCaseLists());
					for(TestCaseList testCaseList : testSuiteList.getTestCaseLists()){
						Hibernate.initialize(testCaseList.getProductMaster());
						Hibernate.initialize(testCaseList.getTestCasePriority());
						Hibernate.initialize(testCaseList.getExecutionTypeMaster());
						Hibernate.initialize(testCaseList.getTestcaseTypeMaster());
						if(testCaseList.getTestCaseStepsLists()!=null &&! (testCaseList.getTestCaseStepsLists()).isEmpty() ){
							Set<TestCaseStepsList> tcStepSet = testCaseList.getTestCaseStepsLists();
							if(tcStepSet.size() != 0){
								Hibernate.initialize(tcStepSet);
								for (TestCaseStepsList testCaseStepsList : tcStepSet) {
									Hibernate.initialize(testCaseStepsList);
									Hibernate.initialize(testCaseStepsList.getTestExecutionResults());
									Hibernate.initialize(testCaseStepsList.getTestCaseList());
								}	
							}
						}
						if(testCaseList.getProductFeature() != null){
							Hibernate.initialize(testCaseList.getProductFeature());
						}
						if(testCaseList.getDecouplingCategory() != null){
							Hibernate.initialize(testCaseList.getDecouplingCategory());
						}
					}
				}
				if(testSuiteList.getExecutionTypeMaster() != null){
				Hibernate.initialize(testSuiteList.getExecutionTypeMaster());
				}
				if(testSuiteList.getTestSuiteList() != null){
					Hibernate.initialize(testSuiteList.getTestSuiteList());	
					if(testSuiteList.getTestSuiteList().size() != 0){
						for(TestSuiteList testsuiteobj : testSuiteList.getTestSuiteList()){
							Hibernate.initialize(testsuiteobj.getExecutionTypeMaster());
							Hibernate.initialize(testsuiteobj.getProductMaster());
							Hibernate.initialize(testsuiteobj.getProductVersionListMaster());
							if(testsuiteobj.getTestCaseLists().size()!=0 ){	
								Hibernate.initialize(testsuiteobj.getTestCaseLists());
								for(TestCaseList testCaseList : testsuiteobj.getTestCaseLists()){
									Hibernate.initialize(testCaseList.getProductMaster());
									Hibernate.initialize(testCaseList.getTestCasePriority());
									Hibernate.initialize(testCaseList.getExecutionTypeMaster());
									Hibernate.initialize(testCaseList.getTestcaseTypeMaster());
									if(testCaseList.getTestCaseStepsLists()!=null &&! (testCaseList.getTestCaseStepsLists()).isEmpty() ){
										Set<TestCaseStepsList> tcStepSet = testCaseList.getTestCaseStepsLists();
										if(tcStepSet.size() != 0){
											Hibernate.initialize(testCaseList.getTestCaseStepsLists());
											for (TestCaseStepsList testCaseStepsList : tcStepSet) {
												Hibernate.initialize(testCaseStepsList);
											}	
										}
									}
									if(testCaseList.getProductFeature() != null){
										Hibernate.initialize(testCaseList.getProductFeature());
									}
									if(testCaseList.getDecouplingCategory() != null){
										Hibernate.initialize(testCaseList.getDecouplingCategory());
									}
								}
							}
							
							if(testsuiteobj.getTestSuiteList() != null && testsuiteobj.getTestSuiteList().size()!=0 ){	
								Hibernate.initialize(testsuiteobj.getTestSuiteList());
							}
							
						}					
					}
				}
								
			}
			log.info("getByTesSuiteId successful");
		} catch (RuntimeException re) {
			log.error("getByTestSuiteId failed", re);
		}
		return testSuiteList;
        
	}	

	@Override
	@Transactional
	public Set<TestSuiteList> getTestSuiteByProductId(int productId) {
		log.info("inside getTestSuiteByProductId");
		WorkPackage workPackage =null;
		Set<TestSuiteList> testSuiteLists = null;
		ProductMaster productMaster = new ProductMaster();
		try {
			Criteria c = sessionFactory.getCurrentSession().createCriteria(ProductMaster.class,"product");			
			c.add(Restrictions.eq("product.productId", productId));
			
			List list = c.list();
			productMaster = (list != null && list.size() != 0)?(ProductMaster) list.get(0) : null;
			
			if(productMaster != null){
				Hibernate.initialize(productMaster.getProductVersionListMasters());
				Hibernate.initialize(productMaster.getDefectManagementSystems());
				Hibernate.initialize(productMaster.getDefectManagementSystemMappings());
				Hibernate.initialize(productMaster.getProductFeatures());
				Hibernate.initialize(productMaster.getTestCaseLists());
				
				Hibernate.initialize(productMaster.getTestRunConfigurationParents());
				Hibernate.initialize(productMaster.getTestManagementSystems());
				Set<TestManagementSystem> testSystems  = productMaster.getTestManagementSystems();
				if(testSystems != null){
					for(TestManagementSystem testManagementSystem : testSystems){
						Hibernate.initialize((testManagementSystem.getTestManagementSystemMappings()));
					}
				}
				
				testSuiteLists = productMaster.getTestSuiteLists(); 
				if(testSuiteLists != null){
					Hibernate.initialize(testSuiteLists);
				}
				for(TestSuiteList tsuite : testSuiteLists){
					Hibernate.initialize(tsuite.getProductMaster());
					Hibernate.initialize(tsuite.getProductVersionListMaster());
					Hibernate.initialize(tsuite.getScriptTypeMaster());
					if(tsuite.getTestCaseLists() != null){
						Hibernate.initialize(tsuite.getTestCaseLists());
					}
					if(tsuite.getTestSuiteList() != null){
						Hibernate.initialize(tsuite.getTestSuiteList());
					}
				}				
			}		
		
			log.debug("getTestSuiteByProductId successful");
		} catch (RuntimeException re) {
			log.error("getTestSuiteByProductId failed", re);
		}
		return testSuiteLists;
	}
	
	@Override
	@Transactional
	public int getTotalRecords() {
		log.debug("getting TestSuiteList total records");
		int count =0;
		try {
			count=((Number) sessionFactory.getCurrentSession().createSQLQuery("select count(*) from test_suite_list where status=1").uniqueResult()).intValue();
			
			log.debug("total records fetch successful");
		} catch (RuntimeException re) {
			log.error("total records fetch failed", re);			
		}
		return count;
	
	}

	@Override
	@Transactional
	public List<TestSuiteList> list(int productVersionListId,String[] parameters) {
		
			log.debug("listing parameterized TestSuiteList instance");
			List<TestSuiteList> testSuiteList=null;
			try {
				StringBuffer qry=new StringBuffer("select ");
				for(int i=0;i<parameters.length;i++){
					qry.append("t."+parameters[i]+",");				
						
				}
				String query=qry.substring(0, qry.lastIndexOf(","));
				
				testSuiteList=sessionFactory.getCurrentSession().createQuery("from TestSuiteList t where productVersionListId=:productVersionListId and t.status=1")
				.setParameter("productVersionListId",productVersionListId)
		                .list();
				if (!(testSuiteList == null || testSuiteList.isEmpty())){
					for(TestSuiteList tsl : testSuiteList){
						Hibernate.initialize(tsl.getProductMaster());
						Hibernate.initialize(tsl.getTestCaseLists());
						if(tsl.getTestCaseLists() != null && !tsl.getTestCaseLists().isEmpty()){
							for(TestCaseList testCaseList : tsl.getTestCaseLists()){
								Hibernate.initialize(testCaseList.getTestCaseStepsLists());
							}
						}
					}
				}
				log.debug("list successful");
			} catch (RuntimeException re) {
				log.error("list failed", re);
			}
			return testSuiteList;       
	    
	}

	@Override
	@Transactional
	public List<TestSuiteList> filterTestSuites(int jtStartIndex,
			int jtPageSize,Integer productId) {
		log.debug("listing filtered TestSuites instance");
		
		List<TestSuiteList> testSuiteList=null;
		try {
			StringBuffer qry= new StringBuffer("from TestSuiteList");
			boolean isANDrequired=false;
			if(productId!=-1){
				
				qry.append(" e where");
				qry.append(" e.productMaster.productId=:productId");
				isANDrequired=true;
				
			}
			Query query = sessionFactory.getCurrentSession().createQuery(qry.toString());
				if(productId!=-1){
					
					query.setParameter("productId", productId);
				}
					testSuiteList = query.setFirstResult(jtStartIndex).setMaxResults(jtPageSize).list();
					if (!(testSuiteList == null || testSuiteList.isEmpty())){
						for(TestSuiteList tsl : testSuiteList){
							Hibernate.initialize(tsl.getProductMaster());
						}
					}	
			
					log.debug("list filtered successful");
				}
			catch (RuntimeException re) {
				log.error("list filtered failed", re);
			}
		
		return testSuiteList;
	}

	@Override
	@Transactional
	public TestSuiteList getByTestSuiteCode(String testSuiteCode){
		log.debug("getting TestSuiteList instance by Code");
		TestSuiteList testSuiteList=null;
		try {
			List list = sessionFactory.getCurrentSession().createQuery("from TestSuiteList t where testSuiteCode=:testSuiteCode").setParameter("testSuiteCode",testSuiteCode)
					.list();
			testSuiteList=(list!=null && list.size()!=0)?(TestSuiteList) list.get(0):null;
			if (testSuiteList != null){
				Hibernate.initialize(testSuiteList.getProductMaster());
				if(testSuiteList.getProductVersionListMaster()!= null) {
				}
				Hibernate.initialize(testSuiteList.getTestCaseLists());				
			}			
			log.debug("getByTesSuiteCode successful");
		} catch (RuntimeException re) {
			log.error("getByTestSuiteCode failed", re);
			//throw re;
		}
		return testSuiteList;
        
	}
	@Override
	@Transactional
	public TestSuiteList getByProductTestSuiteCode(int productId,
			String testSuiteCode) {
		log.debug("getting TestSuiteList instance by product id and test suite Code");
		TestSuiteList testSuiteList=null;
		try {
			List list = sessionFactory.getCurrentSession().createQuery("from TestSuiteList t where productId=:productId and testSuiteCode=:testSuiteCode")
					.setParameter("productId",productId)
					.setParameter("testSuiteCode",testSuiteCode)
					.list();
			testSuiteList=(list!=null && list.size()!=0)?(TestSuiteList) list.get(0):null;
			if (testSuiteList != null){
				Hibernate.initialize(testSuiteList.getProductMaster());					
				Hibernate.initialize(testSuiteList.getProductVersionListMaster());
				if(testSuiteList.getProductVersionListMaster()!= null){
				}
				Hibernate.initialize(testSuiteList.getTestCaseLists());				
			}			
			log.debug("getByTesSuiteCode successful");
		} catch (RuntimeException re) {
			log.error("getByTestSuiteCode failed", re);
		}
		return testSuiteList;
        
	}
	
	@Override
	@Transactional
	public TestSuiteList getByProductIdAndTestSuiteNandAndTestSuiteCode(int productId,String testSuiteName,String testSuiteCode) {
		log.debug("getting TestSuiteList instance by product id and test suite Code");
		TestSuiteList testSuiteList=null;
		try {
			List list = sessionFactory.getCurrentSession().createQuery("from TestSuiteList t where productId=:productId and testSuiteCode=:testSuiteCode and testSuiteName=:testSuiteName")
					.setParameter("productId",productId)
					.setParameter("testSuiteCode",testSuiteCode)
					.setParameter("testSuiteName",testSuiteName)					
					.list();
			testSuiteList=(list!=null && list.size()!=0)?(TestSuiteList) list.get(0):null;
			if (testSuiteList != null){
				Hibernate.initialize(testSuiteList.getProductMaster());					
				Hibernate.initialize(testSuiteList.getProductVersionListMaster());
				if(testSuiteList.getProductVersionListMaster()!= null){
				}
				Hibernate.initialize(testSuiteList.getTestCaseLists());				
			}			
			log.debug("getByTesSuiteCode successful");
		} catch (RuntimeException re) {
			log.error("getByTestSuiteCode failed", re);
		}
		return testSuiteList;
        
	}

	@Override
	@Transactional
	public void updateTestSuite(TestSuiteList testSuiteList) {
		log.info("updating TestSuiteList instance");
		try {
			
			TestSuiteList existingTestSuite = null; 
			sessionFactory.getCurrentSession().update(testSuiteList);			
		} catch (RuntimeException re) {
			log.error("update failed", re);
		}
		
		
	}
	
	@Override
	@Transactional
	public List<TestSuiteList> getByProductId(Integer startIndex, Integer pageSize, int productId) {
		log.debug("getting TestSuiteList instance by product id");		
		List<TestSuiteList> testSuiteList = null;
		try {
			Criteria c = sessionFactory.getCurrentSession().createCriteria(TestSuiteList.class, "testsuite");
			c.createAlias("testsuite.productMaster", "produt");
			c.add(Restrictions.eq("produt.productId", productId));
			if(startIndex != null && pageSize != null)
				c.setFirstResult(startIndex).setMaxResults(pageSize);
			testSuiteList = c.list();			
			log.debug("list successful");
			
			for (TestSuiteList testSuite : testSuiteList) {
				Hibernate.initialize(testSuite.getProductMaster());					
				Hibernate.initialize(testSuite.getProductVersionListMaster());
				if(testSuite.getProductVersionListMaster()!= null){
				}
				Hibernate.initialize(testSuite.getTestCaseLists());
				if(testSuite.getTestCaseLists() != null){
					Set<TestCaseList> testCases = testSuite.getTestCaseLists();
					for (TestCaseList testCase : testCases) {
						Hibernate.initialize(testCase.getTestCaseStepsLists());
					}
				}
				Hibernate.initialize(testSuite.getTestCaseLists());
			}
			log.debug("getByProductId successful");
		} catch (RuntimeException re) {
			log.error("getByProductId failed", re);
		}
		return testSuiteList;    
	}
	

	@Override
	@Transactional
	public List<TestSuiteList> getTestSuitesMappedToTestCaseByTestCaseId(int testCaseId, Integer startIndex, Integer pageSize) {
		log.debug("getting TestSuiteList instance by product id");		
		List<TestSuiteList> testSuiteList = null;
		try {
			Criteria c = sessionFactory.getCurrentSession().createCriteria(TestSuiteList.class, "testsuite");
			c.createAlias("testsuite.testCaseLists", "tc");
			c.add(Restrictions.eq("tc.testCaseId", testCaseId));
			if(startIndex != null && pageSize != null)
				c.setFirstResult(startIndex).setMaxResults(pageSize);
			testSuiteList = c.list();			
			log.debug("list successful");
			
			for (TestSuiteList testSuite : testSuiteList) {
				Hibernate.initialize(testSuite.getProductMaster());					
				Hibernate.initialize(testSuite.getProductVersionListMaster());
				if(testSuite.getProductVersionListMaster()!= null){
				}
				Hibernate.initialize(testSuite.getTestCaseLists());
				if(testSuite.getTestCaseLists() != null){
					Set<TestCaseList> testCases = testSuite.getTestCaseLists();
					for (TestCaseList testCase : testCases) {
						Hibernate.initialize(testCase.getTestCaseStepsLists());
					}
				}
				Hibernate.initialize(testSuite.getTestCaseLists());
			}
			log.debug("getByProductId successful");
		} catch (RuntimeException re) {
			log.error("getByProductId failed", re);
		}
		return testSuiteList;    
	}
	@Override
	@Transactional
	public List<TestSuiteList> getByProductId(int productId) {
		log.debug("getting TestSuiteList instance by product id");		
		List<TestSuiteList> testSuiteList = null;
		try {
			Criteria c = sessionFactory.getCurrentSession().createCriteria(TestSuiteList.class, "testsuite");
			c.createAlias("testsuite.productMaster", "produt");
			c.add(Restrictions.eq("produt.productId", productId));			
			testSuiteList = c.list();			
			log.debug("list successful");
			
			for (TestSuiteList testSuite : testSuiteList) {
				Hibernate.initialize(testSuite.getProductMaster());					
				Hibernate.initialize(testSuite.getProductVersionListMaster());
				if(testSuite.getProductVersionListMaster()!= null){
				}
				Hibernate.initialize(testSuite.getTestCaseLists());
				if(testSuite.getTestCaseLists() != null){
					Set<TestCaseList> testCases = testSuite.getTestCaseLists();
					for (TestCaseList testCase : testCases) {
						Hibernate.initialize(testCase.getTestCaseStepsLists());
						Hibernate.initialize(testCase.getProductMaster());
					}
				}
				
			}
			log.debug("getByProductId successful");
		} catch (RuntimeException re) {
			log.error("getByProductId failed", re);
		}
		return testSuiteList;    
	}

	@Override
	@Transactional
	public List<TestSuiteList> getByProductVersionId(int startIndex,
			int pageSize, int productVersionListId) {
		log.debug("getting TestSuiteList instance by productVersionList id");		
		List<TestSuiteList> testSuiteList = null;
		try {
			Criteria c = sessionFactory.getCurrentSession().createCriteria(TestSuiteList.class, "testsuite");
			c.createAlias("testsuite.productVersionListMaster", "productversion");
			c.add(Restrictions.eq("productversion.productVersionListId", productVersionListId));
			if(startIndex!=-1 && pageSize!=-1){
				c.setFirstResult(startIndex);
				c.setMaxResults(pageSize);
			}			
			testSuiteList = c.list();			
			log.debug("list successful");
			
			for (TestSuiteList testSuite : testSuiteList) {
				Hibernate.initialize(testSuite.getProductMaster());					
				Hibernate.initialize(testSuite.getProductVersionListMaster());
				if(testSuite.getProductVersionListMaster()!= null){
				}
				Hibernate.initialize(testSuite.getTestCaseLists());
				if(testSuite.getTestCaseLists() != null){
					Set<TestCaseList> testCases = testSuite.getTestCaseLists();
					for (TestCaseList testCase : testCases) {
						Hibernate.initialize(testCase.getTestCaseStepsLists());
					}
				}
				Hibernate.initialize(testSuite.getTestCaseLists());
			}
			log.debug("getByProductVersionId successful");
		} catch (RuntimeException re) {
			log.error("getByProductVersionId failed", re);
		}
		return testSuiteList;    
	}

	@Override
	@Transactional
	public List<TestSuiteList> listMappedTestSuite(int testSuiteId) {
		log.debug("getting TestSuiteList instance by id");
		List<TestSuiteList> testSuiteList = new ArrayList<TestSuiteList>();
		TestSuiteList testSuite=null;
		try {
			Criteria c = sessionFactory.getCurrentSession().createCriteria(TestSuiteList.class,"tsuite");			
			c.add(Restrictions.eq("tsuite.testSuiteId", testSuiteId));
			
			List list = c.list();
			testSuite=(list!=null && list.size()!=0)?(TestSuiteList) list.get(0):null;
			if (testSuite != null){
				Hibernate.initialize(testSuite.getProductMaster());				
				if (testSuite.getProductVersionListMaster() != null)
				{
					Hibernate.initialize(testSuite.getProductVersionListMaster());
				}				
				Hibernate.initialize(testSuite.getExecutionTypeMaster());
				Hibernate.initialize(testSuite.getScriptTypeMaster());
				Hibernate.initialize(testSuite.getTestExecutionResults());
				Hibernate.initialize(testSuite.getTestCaseLists());
				if(testSuite.getTestCaseLists()!= null && !testSuite.getTestCaseLists().isEmpty()){
					for(TestCaseList testCaseList : testSuite.getTestCaseLists()){
						Hibernate.initialize(testCaseList.getTestCaseStepsLists());
						Hibernate.initialize(testCaseList.getProductMaster());
					}
				}
				Set<TestSuiteList> testsuiteMappedSet = new HashSet<TestSuiteList>();
				if(testSuite.getTestSuiteList() != null){
					testsuiteMappedSet = testSuite.getTestSuiteList();
					for (TestSuiteList testSuiteList2 : testsuiteMappedSet) {
						Hibernate.initialize(testSuiteList2);
						Hibernate.initialize(testSuiteList2.getExecutionTypeMaster());
						Hibernate.initialize(testSuiteList2.getProductMaster());
						Hibernate.initialize(testSuiteList2.getScriptTypeMaster());
						Hibernate.initialize(testSuiteList2.getTestCaseLists());	
						testSuiteList.add(testSuiteList2);
					}
				}
			}
			log.debug("getByTesSuiteId successful");
		} catch (RuntimeException re) {
			log.error("getByTestSuiteId failed", re);
		}
		return testSuiteList;
        
	}

	@Override
	@Transactional
	public List<TestSuiteList> getTestSuiteByProductId(Integer productId,
			Integer testSuiteExecutionId) {
				List<TestSuiteList> testSuiteLists = null;
				
				try {
					Criteria c = sessionFactory.getCurrentSession().createCriteria(TestSuiteList.class,"tsl");	
					c.createAlias("tsl.productMaster", "pm");
					
					c.createAlias("tsl.executionTypeMaster", "etm");

					c.add(Restrictions.eq("pm.productId", productId));
					if(testSuiteExecutionId!=-1)
					c.add(Restrictions.eq("etm.executionTypeId", testSuiteExecutionId));
					
					testSuiteLists = c.list();
						
					if(testSuiteLists!=null && !testSuiteLists.isEmpty()){
						for(TestSuiteList tsuite : testSuiteLists){
							Hibernate.initialize(tsuite.getProductMaster());
							Hibernate.initialize(tsuite.getProductVersionListMaster());
							Hibernate.initialize(tsuite.getScriptTypeMaster());
						}				
					}		
				
					log.debug("getTestSuiteByProductId successful");
				} catch (RuntimeException re) {
					log.error("getTestSuiteByProductId failed", re);
				}
				return testSuiteLists;
	}

	@Override
	@Transactional
	public List<TestSuiteList> getByProductVersionId(Integer jtStartIndex,
			Integer jtPageSize, Integer versionId, Integer testRunPlanId) {
			log.debug("getting TestSuiteList instance by productVersionList id");		
			List<TestSuiteList> testSuiteList = null;
			try {
				Integer executionType=-1;
				if(testRunPlanId!=null && testRunPlanId!=-1){
					TestRunPlan testRunPlan = productDAO.getTestRunPlanBytestRunPlanId(testRunPlanId);
					executionType=testRunPlan.getExecutionType().getExecutionTypeId();
							
				}
				
				Criteria c = sessionFactory.getCurrentSession().createCriteria(TestSuiteList.class, "testsuite");
				c.createAlias("testsuite.productVersionListMaster", "productversion");
				c.createAlias("testsuite.executionTypeMaster", "executionTypeMaster");
				c.add(Restrictions.eq("productversion.productVersionListId", versionId));
				if(jtStartIndex!=-1 && jtPageSize!=-1){
					c.setFirstResult(jtStartIndex);
					c.setMaxResults(jtPageSize);
				}	
				if(executionType!=-1){
					if(executionType==3){
						c.add(Restrictions.eq("executionTypeMaster.executionTypeId", executionType));
					}
				}
				testSuiteList = c.list();			
				log.debug("list successful");
				
				for (TestSuiteList testSuite : testSuiteList) {
					Hibernate.initialize(testSuite.getProductMaster());					
					Hibernate.initialize(testSuite.getProductVersionListMaster());
					if(testSuite.getProductVersionListMaster()!= null){
					}
					Hibernate.initialize(testSuite.getTestCaseLists());
					if(testSuite.getTestCaseLists() != null){
						Set<TestCaseList> testCases = testSuite.getTestCaseLists();
						for (TestCaseList testCase : testCases) {
							Hibernate.initialize(testCase.getTestCaseStepsLists());
						}
					}
					Hibernate.initialize(testSuite.getTestCaseLists());
				}
				log.debug("getByProductVersionId successful");
			} catch (RuntimeException re) {
				log.error("getByProductVersionId failed", re);
			}
			return testSuiteList;   	}
	
	@Override
	@Transactional
	public boolean isVersionTestSuiteExistingByName(String testSuiteName, Integer versionId) {
		log.info("verifying if any feature existing by same name to the product");
		Criteria c = sessionFactory.getCurrentSession().createCriteria(TestSuiteList.class,"suite");			
		c.createAlias("suite.productVersionListMaster", "version");
		c.add(Restrictions.eq("version.productVersionListId",versionId));
		
		c.add(Restrictions.eq("suite.testSuiteName", testSuiteName));
		List TestSuiteList =c.list();	
		
		if (TestSuiteList == null || TestSuiteList.isEmpty()) 
		    return false;
		else 
			return true;
	}

	@Override
	@Transactional
	public ProductVersionListMaster getLatestProductVersion(
			ProductMaster productMaster) {
				log.info("inside getLatestProductVersion");
				List<ProductVersionListMaster> productVersionListMasters = null;
				ProductVersionListMaster productVersionListMaster = null;

				try {
					Criteria c = sessionFactory.getCurrentSession().createCriteria(ProductVersionListMaster.class,"tsl");	
					c.createAlias("tsl.productMaster", "pm");

					c.add(Restrictions.eq("pm.productId", productMaster.getProductId()));
				
					
					productVersionListMasters = c.addOrder(Order.asc("tsl.statusChangeDate")).list();
						
					
					if(productVersionListMasters!=null && !productVersionListMasters.isEmpty()){
						if(productVersionListMasters.size()>=1){
							productVersionListMaster=productVersionListMasters.get(0);
							Hibernate.initialize(productVersionListMaster.getProductMaster());

						}
									
					}		
				
					log.debug("getTestSuiteByProductId successful");
				} catch (RuntimeException re) {
					log.error("getTestSuiteByProductId failed", re);
				}
				return productVersionListMaster;
	
	}

	@Override
	@Transactional
	public List<TestCaseList> listTestCaseByTestSuite(int testSuiteId,
			int jtStartIndex, int jtPageSize) {
		log.debug("listing all TestCaseList instance");
		
		List<TestCaseList> testCaseList=new LinkedList<TestCaseList>();
		try {
			Criteria c = sessionFactory.getCurrentSession().createCriteria(TestCaseList.class, "tcl");
			c.createAlias("tcl.testSuiteLists", "tsl");

			c.add(Restrictions.eq("tsl.testSuiteId",testSuiteId));

			if(jtStartIndex!=-1 && jtPageSize!=-1){
				testCaseList = c.setFirstResult(jtStartIndex).setMaxResults(jtPageSize).list();
			}else{
				testCaseList = c.list();
			}
			for (TestCaseList tcl : testCaseList) {
				Hibernate.initialize(tcl.getProductFeature());
				Hibernate.initialize(tcl.getDecouplingCategory());
				Hibernate.initialize(tcl.getTestSuiteLists());
			}
			log.debug("list all successful");
		} catch (RuntimeException re) {
			log.error("list all failed", re);		
		}
		return testCaseList;
	}

	@Override
	@Transactional
	public Integer countAllProductTestCaseSteps(Date startDate, Date endDate) {
		
		try {
			Criteria c = sessionFactory.getCurrentSession().createCriteria(TestCaseStepsList.class,"testCaseStepsList");
			
			if (startDate != null) {
				c.add(Restrictions.ge("testCaseStepsList.testStepCreatedDate", startDate));
			}
			if (endDate != null) {
				c.add(Restrictions.le("testCaseStepsList.testStepCreatedDate", endDate));
			}
			
			c.setProjection(Projections.rowCount());
			Integer count = Integer.parseInt(c.uniqueResult().toString());
			return count;
			
		} catch (Exception e) {
			log.error("Unable to get count of all TestCaseStepsList", e);
			return -1;
		}
	}

	@Override
	@Transactional
	public List<TestCaseStepsList> listAllProductTestCaseStepsByLastSyncDate(int startIndex, int pageSize, Date startDate,Date endDate) {
		log.debug("listing all ProductBuild");
			List<TestCaseStepsList> testCaseStepsList=null;
			try {
				Criteria c = sessionFactory.getCurrentSession().createCriteria(TestCaseStepsList.class, "testCaseSteps");
				
				if (startDate != null) {
					c.add(Restrictions.ge("testCaseSteps.testStepCreatedDate", startDate));
				}
				if (endDate != null) {
					c.add(Restrictions.le("testCaseSteps.testStepCreatedDate", endDate));
				}
				
				c.addOrder(Order.asc("testStepId"));
	            c.setFirstResult(startIndex);
	            c.setMaxResults(pageSize);
	            testCaseStepsList = c.list();		
				
				if (!(testCaseStepsList == null || testCaseStepsList.isEmpty())){
					for (TestCaseStepsList testStep : testCaseStepsList) {
						Hibernate.initialize(testStep);
						Hibernate.initialize(testStep.getTestExecutionResults());
						Hibernate.initialize(testStep.getTestCaseList());
					}
				}
				log.debug("list all successful");
			} catch (RuntimeException re) {
				log.error("list all failed", re);
			}
			return testCaseStepsList;}

	@Override
	@Transactional
	public List<TestSuiteList> returnVersionTestSuiteId(String testSuiteName, Integer versionId) {
		log.info("verifying if any feature existing by same name to the product");
		Criteria c = sessionFactory.getCurrentSession().createCriteria(TestSuiteList.class,"suite");			
		c.createAlias("suite.productVersionListMaster", "version");
		c.add(Restrictions.eq("version.productVersionListId",versionId));
		
		c.add(Restrictions.eq("suite.testSuiteName", testSuiteName));
		List TestSuiteList =c.list();	
		
		if (TestSuiteList == null || TestSuiteList.isEmpty()) 
		    return null;
		else 
			return TestSuiteList;
	}

	@Override
	@Transactional
	public Integer countAllTestSuites(Date startDate, Date endDate) {
		try {
			Criteria c = sessionFactory.getCurrentSession().createCriteria(TestSuiteList.class,"testSuiteList");
			if (endDate != null) {
				c.add(Restrictions.le("testSuiteList.statusChangeDate", endDate));
			}
			
			c.setProjection(Projections.rowCount());
			Integer count = Integer.parseInt(c.uniqueResult().toString());
			return count;
			
		} catch (Exception e) {
			log.error("Unable to get count of all TestSuiteList", e);
			return -1;
		}
	}

	@Override
	@Transactional
	public List<TestSuiteList> listAllTestSuites(int startIndex,int pageSize, Date startDate, Date endDate) {
		log.debug("listing all EffortTracker");
		List<TestSuiteList> testSuiteList=null;
		try {
			Criteria c = sessionFactory.getCurrentSession().createCriteria(TestSuiteList.class, "testSuiteList");
			if (endDate != null) {
				c.add(Restrictions.le("testSuiteList.statusChangeDate", endDate));
			}
			c.addOrder(Order.asc("testSuiteId"));
	        c.setFirstResult(startIndex);
	        c.setMaxResults(pageSize);
	        testSuiteList = c.list();		
			

			log.debug("list all successful");
		} catch (RuntimeException re) {
			log.error("list all failed", re);
		}
		return testSuiteList;
	}

	@Override
	@Transactional
	public TestSuiteList getByTestSuiteName(String testSuiteName) {
		log.debug("getting TestSuiteList instance by id");
		TestSuiteList testSuiteList=null;
		try {
			Criteria c = sessionFactory.getCurrentSession().createCriteria(TestSuiteList.class,"tsuite");			
			c.add(Restrictions.eq("tsuite.testSuiteName", testSuiteName));
			
			List list = c.list();
			testSuiteList=(list!=null && list.size()!=0)?(TestSuiteList) list.get(0):null;
			if (testSuiteList != null){
				Hibernate.initialize(testSuiteList.getProductMaster());
				
				Hibernate.initialize(testSuiteList.getProductVersionListMaster());
				if (testSuiteList.getProductVersionListMaster() != null)
				{
					Hibernate.initialize(testSuiteList.getProductVersionListMaster());
				}				
				if(testSuiteList.getScriptTypeMaster()!=null){
					Hibernate.initialize(testSuiteList.getScriptTypeMaster());
				}
					if(testSuiteList.getTestCaseLists().size()!=0 ){	
					Hibernate.initialize(testSuiteList.getTestCaseLists());
					for(TestCaseList testCaseList : testSuiteList.getTestCaseLists()){
						Hibernate.initialize(testCaseList.getProductMaster());
						Hibernate.initialize(testCaseList.getTestCasePriority());
						Hibernate.initialize(testCaseList.getExecutionTypeMaster());
						Hibernate.initialize(testCaseList.getTestcaseTypeMaster());
						if(testCaseList.getTestCaseStepsLists()!=null &&! (testCaseList.getTestCaseStepsLists()).isEmpty() ){
							Set<TestCaseStepsList> tcStepSet = testCaseList.getTestCaseStepsLists();
							if(tcStepSet.size() != 0){
								Hibernate.initialize(tcStepSet);
								for (TestCaseStepsList testCaseStepsList : tcStepSet) {
									Hibernate.initialize(testCaseStepsList);
									Hibernate.initialize(testCaseStepsList.getTestExecutionResults());
									Hibernate.initialize(testCaseStepsList.getTestCaseList());
								}	
							}
						}
						if(testCaseList.getProductFeature() != null){
							Hibernate.initialize(testCaseList.getProductFeature());
						}
						if(testCaseList.getDecouplingCategory() != null){
							Hibernate.initialize(testCaseList.getDecouplingCategory());
						}
					}
				}
				if(testSuiteList.getExecutionTypeMaster() != null){
				Hibernate.initialize(testSuiteList.getExecutionTypeMaster());
				}
				if(testSuiteList.getTestSuiteList() != null){
					Hibernate.initialize(testSuiteList.getTestSuiteList());	
					if(testSuiteList.getTestSuiteList().size() != 0){
						for(TestSuiteList testsuiteobj : testSuiteList.getTestSuiteList()){
							Hibernate.initialize(testsuiteobj.getExecutionTypeMaster());
							Hibernate.initialize(testsuiteobj.getProductMaster());
							Hibernate.initialize(testsuiteobj.getProductVersionListMaster());
							if(testsuiteobj.getTestCaseLists().size()!=0 ){	
								Hibernate.initialize(testsuiteobj.getTestCaseLists());
								for(TestCaseList testCaseList : testsuiteobj.getTestCaseLists()){
									Hibernate.initialize(testCaseList.getProductMaster());
									Hibernate.initialize(testCaseList.getTestCasePriority());
									Hibernate.initialize(testCaseList.getExecutionTypeMaster());
									Hibernate.initialize(testCaseList.getTestcaseTypeMaster());
									if(testCaseList.getTestCaseStepsLists()!=null &&! (testCaseList.getTestCaseStepsLists()).isEmpty() ){
										Set<TestCaseStepsList> tcStepSet = testCaseList.getTestCaseStepsLists();
										if(tcStepSet.size() != 0){
											Hibernate.initialize(testCaseList.getTestCaseStepsLists());
											for (TestCaseStepsList testCaseStepsList : tcStepSet) {
												Hibernate.initialize(testCaseStepsList);
											}	
										}
									}
									
									if(testCaseList.getProductFeature() != null){
										Hibernate.initialize(testCaseList.getProductFeature());
									}
									if(testCaseList.getDecouplingCategory() != null){
										Hibernate.initialize(testCaseList.getDecouplingCategory());
									}
								}
							}
							
							if(testsuiteobj.getTestSuiteList() != null && testsuiteobj.getTestSuiteList().size()!=0 ){	
								Hibernate.initialize(testsuiteobj.getTestSuiteList());
							}						
						}					
					}
				}								
			}
			log.info("getByTesSuiteId successful");
		} catch (RuntimeException re) {
			log.error("getByTestSuiteId failed", re);			
		}
		return testSuiteList;
	}
	
	@Override
	@Transactional
	public TestSuiteList getTestSuiteByProductIdOrTestSuiteNandOrTestSuiteCode(int productId,String testSuiteName,String testSuiteCode) {
		log.debug("getting TestSuiteList instance by product id and test suite Code");
		TestSuiteList testSuiteList=null;
		List list =null;
		try {
			if(productId !=0 && (testSuiteName !=null && !testSuiteName.trim().isEmpty()) &&(testSuiteCode != null && !testSuiteCode.trim().isEmpty())) {
			list = sessionFactory.getCurrentSession().createQuery("from TestSuiteList t where productId=:productId and testSuiteCode=:testSuiteCode and testSuiteName=:testSuiteName")
					.setParameter("productId",productId)
					.setParameter("testSuiteCode",testSuiteCode)
					.setParameter("testSuiteName",testSuiteName)					
					.list();
			} else if(productId !=0 && (testSuiteName !=null && !testSuiteName.trim().isEmpty()) && (testSuiteCode == null || testSuiteCode.isEmpty())){
				list = sessionFactory.getCurrentSession().createQuery("from TestSuiteList t where productId=:productId and testSuiteName=:testSuiteName")
						.setParameter("productId",productId)
						.setParameter("testSuiteName",testSuiteName)					
						.list();
			} else if(productId !=0 && (testSuiteName ==null || testSuiteName.isEmpty()) && (testSuiteCode != null && testSuiteCode.trim().isEmpty())) {
				list = sessionFactory.getCurrentSession().createQuery("from TestSuiteList t where productId=:productId and testSuiteCode=:testSuiteCode")
						.setParameter("productId",productId)
						.setParameter("testSuiteCode",testSuiteCode)
						.list();
			}
			testSuiteList=(list!=null && list.size()!=0)?(TestSuiteList) list.get(0):null;
			if (testSuiteList != null){
				Hibernate.initialize(testSuiteList.getProductMaster());					
				Hibernate.initialize(testSuiteList.getProductVersionListMaster());
				if(testSuiteList.getProductVersionListMaster()!= null){
				}
				Hibernate.initialize(testSuiteList.getTestCaseLists());				
			}			
			log.debug("getByTesSuiteCode successful");
		} catch (RuntimeException re) {
			log.error("getByTestSuiteCode failed", re);
		}
		return testSuiteList;
        
	}
}