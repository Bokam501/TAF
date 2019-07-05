package com.hcl.atf.taf.dao.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.hibernate.type.StandardBasicTypes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.hcl.atf.taf.constants.IDPAConstants;
import com.hcl.atf.taf.constants.TAFConstants;
import com.hcl.atf.taf.dao.EnvironmentDAO;
import com.hcl.atf.taf.dao.ExecutionTypeMasterDAO;
import com.hcl.atf.taf.dao.ProductFeatureDAO;
import com.hcl.atf.taf.dao.TestCaseListDAO;
import com.hcl.atf.taf.dao.TestCasePriorityDAO;
import com.hcl.atf.taf.dao.TestCaseStepsListDAO;
import com.hcl.atf.taf.dao.TestSuiteListDAO;
import com.hcl.atf.taf.dao.TestcaseTypeMasterDAO;
import com.hcl.atf.taf.dao.WorkPackageDAO;
import com.hcl.atf.taf.model.DecouplingCategory;
import com.hcl.atf.taf.model.EntityRelationship;
import com.hcl.atf.taf.model.ExecutionTypeMaster;
import com.hcl.atf.taf.model.ProductFeature;
import com.hcl.atf.taf.model.ProductMaster;
import com.hcl.atf.taf.model.ProductVersionListMaster;
import com.hcl.atf.taf.model.TestCaseList;
import com.hcl.atf.taf.model.TestCasePriority;
import com.hcl.atf.taf.model.TestCaseScript;
import com.hcl.atf.taf.model.TestCaseStepsList;
import com.hcl.atf.taf.model.TestExecutionResult;
import com.hcl.atf.taf.model.TestExecutionResultBugList;
import com.hcl.atf.taf.model.TestRunJob;
import com.hcl.atf.taf.model.TestRunPlan;
import com.hcl.atf.taf.model.TestSuiteList;
import com.hcl.atf.taf.model.TestcaseTypeMaster;
import com.hcl.atf.taf.model.WorkPackageTestCaseExecutionPlan;
import com.hcl.atf.taf.model.dto.TestCaseListDTO;
import com.hcl.atf.taf.model.dto.WorkPackageTCEPResultSummaryDTO;
import com.hcl.atf.taf.model.json.terminal.JsonTestExecutionResult;
import com.hcl.atf.taf.mongodb.service.MongoDBService;
import com.hcl.atf.taf.service.EntityRelationshipService;
import com.hcl.atf.taf.service.ExecutionTypeMasterService;
import com.hcl.atf.taf.service.ProductListService;
import com.hcl.atf.taf.service.TestSuiteConfigurationService;



@Repository
public class TestCaseListDAOImpl implements TestCaseListDAO {
	private static final Log log = LogFactory.getLog(TestCaseListDAOImpl.class);
	
	@Autowired
    private SessionFactory sessionFactory;
	
	@Autowired
	private TestSuiteListDAO testSuiteListDAO;
	@Autowired
	private TestcaseTypeMasterDAO testcaseTypeMasterDAO;
	
	@Autowired
	private ProductListService productListService;
	
	@Autowired
    private TestCaseStepsListDAO testCaseStepsDAO;

	@Autowired
    private WorkPackageDAO workpackageDAO;
	@Autowired
    private ExecutionTypeMasterDAO executionTypeMasterDAO;
	@Autowired
    private TestCasePriorityDAO testCasePriorityDAO;
	@Autowired
    private TestSuiteConfigurationService testSuiteConfigurationService;
	
	@Autowired
    private ProductFeatureDAO productFeatureDAO;
	
	@Autowired
    private EnvironmentDAO environmentDAO;
	@Autowired
	private MongoDBService mongoDBService;	
	@Autowired
	private EntityRelationshipService entityRelationshipService;
	@Autowired
	private ExecutionTypeMasterService executionTypeMasterService;

	@Value("#{ilcmProps['TEST_STEP_PARSER']}")
    private String testStepParser;
	@Value("#{ilcmProps['testStep_updateFields_from_TER']}")
    private String testStepUpdateFromTER;
	
	@Override
	@Transactional
	public void delete(TestCaseList testCaseList) {
		log.debug("deleting TestCaseList instance");
		try {
			sessionFactory.getCurrentSession().delete(testCaseList);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			//throw re;
		}		
		
	}
	
	@Override
	@Transactional
	public void deleteTCWithMappings(TestCaseList testCaseList) {
		log.debug("deleting TestCaseList instance");
		try {
			
			String sql = "delete from TestCaseList where testCaseId=:tcId";
			sessionFactory.getCurrentSession().createQuery(sql).setParameter("tcId", testCaseList.getTestCaseId()).executeUpdate();
			
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
		}		
		
	}

	@Override
	@Transactional
	public int addTestCase (int testCaseId,int testSuiteId) {
		log.debug("adding TestCaseList instance");
		TestCaseList testCaseList = null;
		TestSuiteList testSuiteList = null;
		try {
		
			testSuiteList = (TestSuiteList) sessionFactory.getCurrentSession().get(TestSuiteList.class,testSuiteId);
			if (testSuiteList == null) {
				log.debug("TestSuite with specified id not found : " + testSuiteId);
			}

			testCaseList = (TestCaseList) sessionFactory.getCurrentSession().get(TestCaseList.class, testCaseId);
			if (testCaseList == null) {
				log.debug("TestCase with specified id not found : " + testCaseId);
			}
			
			Set<TestCaseList> testCaseLists = testSuiteList.getTestCaseLists();
			testCaseLists.add(testCaseList);
			testSuiteList.setTestCaseLists(testCaseLists);
			sessionFactory.getCurrentSession().save(testSuiteList);
			log.debug("Added TestCase to TestSuite");
		} catch (RuntimeException re) {
			log.error("Failed to add TestCase to TestSuite", re);
		}
		return testCaseList.getTestCaseId().intValue();
	}
	
	@Override
	@Transactional
	public int addTestCasetoProductVersion(int testcaseId, int versionId) {
		log.debug("adding TestCaseList instance");
		TestCaseList testCaseList = null;
		ProductVersionListMaster productVersion = null;
		try {
		
			productVersion = (ProductVersionListMaster) sessionFactory.getCurrentSession().get(ProductVersionListMaster.class,versionId);
			if (productVersion == null) {
				log.debug("productVersion with specified id not found : " + versionId);
			}		
					
			testCaseList = (TestCaseList) sessionFactory.getCurrentSession().get(TestCaseList.class, testcaseId);
			if (testCaseList == null) {
				log.debug("TestCase with specified id not found : " + testcaseId);
			}
			
		
			Set<TestCaseList> testCaseLists = productVersion.getTestCaseLists();			
			testCaseLists.add(testCaseList);
			productVersion.setTestCaseLists(testCaseLists);
			sessionFactory.getCurrentSession().save(productVersion);
			log.debug("Added TestCase to ProductVersion");
		} catch (RuntimeException re) {
			log.error("Failed to add TestCase to ProductVersion", re);
		}
		return testCaseList.getTestCaseId().intValue();
	}	
	
	@Override
	@Transactional
	public int add(TestCaseList testCaseList) {
		log.debug("adding TestCaseList instance");
		try {
			sessionFactory.getCurrentSession().save(testCaseList);
			log.debug("add successful");
		} catch (RuntimeException re) {
			log.error("add failed", re);
		}
		return testCaseList.getTestCaseId().intValue();
				
	}
	
	@Override
	@Transactional
	public TestCaseList update(TestCaseList testCaseList,Integer count, Integer maxLimit) {
		log.debug("updating TestCaseList instance");
		try {
			
			sessionFactory.getCurrentSession().saveOrUpdate(testCaseList);
			if (count % maxLimit == 0 ) {
				sessionFactory.getCurrentSession().flush();
				sessionFactory.getCurrentSession().clear();
		    } 
			
		} catch (RuntimeException re) {
			log.error("update failed", re);
		}
		return testCaseList;
	}
	
	
	
	
	@Override
    @Transactional
    public void updateTestCasesManually(TestCaseList testCaseList) {
           log.debug("updating Test CaseList instance");
           try {
                  
        	   TestCaseList existingTestCaseList = null;  
                  List list = sessionFactory.getCurrentSession().createQuery("from TestCaseList tcl where tcl.testCaseId=:testCaseId").setParameter("testCaseId",testCaseList.getTestCaseId())
                               .list();
                  existingTestCaseList = (list!=null && list.size()!=0)?(TestCaseList) list.get(0):null;
                  if (existingTestCaseList != null) {
                	  	 if(testCaseList.getProductFeature()!=null){
                	  		 existingTestCaseList.setProductFeature(testCaseList.getProductFeature());
                	  	 }
                	  	if(testCaseList.getDecouplingCategory()!=null){
               	  		 existingTestCaseList.setDecouplingCategory(testCaseList.getDecouplingCategory());
               	  	 	}
                	  	if(testCaseList.getProductMaster()!=null){
                	  		 existingTestCaseList.setProductMaster(testCaseList.getProductMaster());
                	  	}
                	  	existingTestCaseList.setTestCaseCode(testCaseList.getTestCaseCode());
                	  	existingTestCaseList.setTestCaseCreatedDate(testCaseList.getTestCaseCreatedDate());
                	  	existingTestCaseList.setTestCaseDescription(testCaseList.getTestCaseDescription());
                	  	existingTestCaseList.setTestCaseLastUpdatedDate(testCaseList.getTestCaseLastUpdatedDate());
                	  	existingTestCaseList.setTestCaseName(testCaseList.getTestCaseName());
                	  	existingTestCaseList.setTestCasePriority(testCaseList.getTestCasePriority());
                		existingTestCaseList.setTestCaseType(testCaseList.getTestCaseType());
                	  	existingTestCaseList.setTestCaseCode(testCaseList.getTestCaseCode());
                	  	existingTestCaseList.setTestSuiteLists(testCaseList.getTestSuiteLists());
                	  	if(testCaseList.getTestCaseStepsLists()!=null && testCaseList.getTestExecutionResults()!=null){
                	  		existingTestCaseList.setTestCaseStepsLists(testCaseList.getTestCaseStepsLists());
                	  		existingTestCaseList.setTestExecutionResults(testCaseList.getTestExecutionResults());
                	  	}
                         sessionFactory.getCurrentSession().saveOrUpdate(existingTestCaseList);
                         log.debug("update successful");
                  } else {
                        log.info("Update of testEnvironment failed");
                  }
           } catch (RuntimeException re) {
                  log.error("update failed", re);
           }
	}

	@Override
	@Transactional
	public List<TestCaseList> list() {
		log.debug("listing all TestCaseList instance");
		List<TestCaseList> testCaseList=null;
		try {
			testCaseList=sessionFactory.getCurrentSession().createQuery("from TestCase").list();
			log.debug("list all successful");
		} catch (RuntimeException re) {
			log.error("list all failed", re);
			//throw re;
		}
		return testCaseList;
	}
	
	@Override
	@Transactional
    public List<TestCaseList> list(int startIndex, int pageSize) {
		log.debug("listing TestCaseList instance");
		List<TestCaseList> testCaseList=null;
		try {
			testCaseList=sessionFactory.getCurrentSession().createQuery("from TestCase")
	                .setFirstResult(startIndex)
	                .setMaxResults(pageSize)
	                .list();
			log.debug("list successful");
		} catch (RuntimeException re) {
			log.error("list failed", re);
			//throw re;
		}
		return testCaseList;       
    }
	
	@Override
	@Transactional
	public TestCaseList getByTestCaseId(int testCaseId){
		log.info("getting TestCaseList instance by id");
		TestCaseList testCaseList=null;
		try {
			List list =  sessionFactory.getCurrentSession().createQuery("from TestCaseList t where testCaseId=:testCaseId").setParameter("testCaseId", testCaseId)
					.list();
			testCaseList=(list!=null && list.size()!=0)?(TestCaseList)list.get(0):null;
			if (testCaseList != null) {
				if(testCaseList.getDecouplingCategory() != null){
					Hibernate.initialize(testCaseList.getDecouplingCategory());
					for (DecouplingCategory decoup : testCaseList.getDecouplingCategory()) {
						Hibernate.initialize(decoup.getProduct());
						Hibernate.initialize(decoup.getParentCategory());
						Hibernate.initialize(decoup.getUserTypeMasterNew());
					}
				}
				
				Hibernate.initialize(testCaseList.getProductMaster());
				if(testCaseList.getProductVersionList() != null && testCaseList.getProductVersionList().size() >0){
					Hibernate.initialize(testCaseList.getProductVersionList());
				}
				
				Hibernate.initialize(testCaseList.getProductFeature());
				Hibernate.initialize(testCaseList.getTestCaseStepsLists());
				Hibernate.initialize(testCaseList.getTestSuiteLists());
				Hibernate.initialize(testCaseList.getDecouplingCategory());
				Hibernate.initialize(testCaseList.getExecutionTypeMaster());
				Hibernate.initialize(testCaseList.getTestCasePriority());
				Hibernate.initialize(testCaseList.getTestcaseTypeMaster());	
				Hibernate.initialize(testCaseList.getTestRunPlanList());
				
				if(testCaseList.getTestRunPlanList() != null && testCaseList.getTestRunPlanList().size() >0) {
					for(TestRunPlan testRun:testCaseList.getTestRunPlanList()) {
						Hibernate.initialize(testRun.getProductBuild());
					}
				}
				
				if(testCaseList.getWorkPackageTestCaseExecutionPlan()!= null){
					Hibernate.initialize(testCaseList.getWorkPackageTestCaseExecutionPlan());
					Set<WorkPackageTestCaseExecutionPlan> wtcep = testCaseList.getWorkPackageTestCaseExecutionPlan();
					for (WorkPackageTestCaseExecutionPlan wptcepobj : wtcep) {
						if(wptcepobj.getWorkPackage() != null){
							Hibernate.initialize(wptcepobj.getWorkPackage());
							if(wptcepobj.getWorkPackage().getProductBuild() != null){
								Hibernate.initialize(wptcepobj.getWorkPackage().getProductBuild());
								if(wptcepobj.getWorkPackage().getProductBuild().getProductVersion() != null){
									Hibernate.initialize(wptcepobj.getWorkPackage().getProductBuild().getProductVersion());
								}
							}
						}
						if(wptcepobj.getTestCaseExecutionResult() != null){
							Hibernate.initialize(wptcepobj.getTestCaseExecutionResult());
							if(wptcepobj.getTestCaseExecutionResult().getTestExecutionResultBugListSet() != null){
								Hibernate.initialize(wptcepobj.getTestCaseExecutionResult().getTestExecutionResultBugListSet());
								Set<TestExecutionResultBugList> terbSet = wptcepobj.getTestCaseExecutionResult().getTestExecutionResultBugListSet();
								for (TestExecutionResultBugList terbObj : terbSet) {
									if(terbObj.getBugFilingStatus() != null){
										Hibernate.initialize(terbObj.getBugFilingStatus());										
										Hibernate.initialize(terbObj.getBugFilingStatus().getWorkFlowId());
									}
									if(terbObj.getDefectSeverity() != null){
										Hibernate.initialize(terbObj.getDefectSeverity());
									}
								}
							}
							
						}
						
					}
				}
			}
			log.debug("getByTesCaseId successful");
		} catch (RuntimeException re) {
			log.error("getByTestCaseId failed", re);
		}
		return testCaseList;
        
	}


	@Override
	@Transactional
	public TestCaseList reloadTestCase(TestCaseList testCase){
		log.debug("getting TestCaseList instance by id");
		TestCaseList testCaseList=null;
		try {
			sessionFactory.getCurrentSession().evict(testCase);
			List list =  sessionFactory.getCurrentSession().createQuery("from TestCaseList t where testCaseId=:testCaseId").setParameter("testCaseId", testCase.getTestCaseId())
					.list();
			testCaseList=(list!=null && list.size()!=0)?(TestCaseList)list.get(0):null;
			
			log.debug("getByTesCaseId successful");
		} catch (RuntimeException re) {
			log.error("getByTestCaseId failed", re);
		}
		return testCaseList;
        
	}

	@Override
	@Transactional
	public TestCaseList getByTestCaseIdBare(int testCaseId){
		log.debug("getting TestCaseList instance by id");
		TestCaseList testCaseList=null;
		try {
			List list =  sessionFactory.getCurrentSession().createQuery("from TestCaseList t where testCaseId=:testCaseId").setParameter("testCaseId", testCaseId)
					.list();
			testCaseList=(list!=null && list.size()!=0)?(TestCaseList)list.get(0):null;
			if (testCaseList != null) {
				if(testCaseList.getDecouplingCategory() != null){
					Hibernate.initialize(testCaseList.getDecouplingCategory());
					for (DecouplingCategory decoup : testCaseList.getDecouplingCategory()) {
						Hibernate.initialize(decoup.getProduct());
						Hibernate.initialize(decoup.getParentCategory());
						Hibernate.initialize(decoup.getUserTypeMasterNew());
					}
				}
				
				Hibernate.initialize(testCaseList.getProductMaster());
				Hibernate.initialize(testCaseList.getProductFeature());
				Hibernate.initialize(testCaseList.getTestCaseStepsLists());
				Hibernate.initialize(testCaseList.getTestSuiteLists());
				Hibernate.initialize(testCaseList.getDecouplingCategory());
				Hibernate.initialize(testCaseList.getExecutionTypeMaster());
				Hibernate.initialize(testCaseList.getTestCasePriority());
				Hibernate.initialize(testCaseList.getTestcaseTypeMaster());		
			}
			log.debug("getByTesCaseId successful");
		} catch (RuntimeException re) {
			log.error("getByTestCaseId failed", re);
		}
		return testCaseList;
        
	}

	
	@Override
	@Transactional
	public TestCaseList getByTestCaseCode(String testCaseCode, ProductMaster product){
		log.debug("getting TestCaseList instance by id");
		TestCaseList testCaseList=null;
		try {

			List list = sessionFactory.getCurrentSession()
					.createCriteria(TestCaseList.class, "testCase")
					.createCriteria("productMaster", "product")
					.add(Restrictions.eq("testCase.testCaseCode", testCaseCode))
					.add(Restrictions.eq("testCase.productMaster", product))
					.list();
			
			testCaseList=(list!=null && list.size()!=0)?(TestCaseList)list.get(0):null;
			log.debug("getByTesCaseId successful");
		} catch (RuntimeException re) {
			log.error("getByTestCaseId failed", re);
		}
		return testCaseList;
	}

	@Override
	@Transactional
	public int getTotalRecords() {
		log.debug("getting TestCaseList total records");
		int count =0;
		try {
			count=((Number) sessionFactory.getCurrentSession().createSQLQuery("select count(*) from test_case_list").uniqueResult()).intValue();
			
			log.debug("total records fetch successful");
		} catch (RuntimeException re) {
			log.error("total records fetch failed", re);			
		}
		return count;
	
	}

	@Override
	@Transactional
	public List<TestCaseList> list(int testSuiteId) {

		log.debug("listing specific ProductVersionListMaster instance");
		//
		List<TestCaseList> testCaseList=null;
		try {
			testCaseList=sessionFactory.getCurrentSession().createQuery("from TestCaseList where testSuiteId=:testSuiteId")
														.setParameter("testSuiteId", testSuiteId).list();
			log.debug("list specific successful");
		//	
		} catch (RuntimeException re) {
			log.error("list specific failed", re);
			//throw re;
		}
		return testCaseList;
	}

	@Override
	@Transactional
	public List<TestCaseList> list(int testSuiteId, int startIndex, int pageSize) {

		log.debug("listing specific ProductVersionListMaster instance");
		List<TestCaseList> testCaseList=null;
		try {
			if(startIndex!=-1 && pageSize!=-1){
			testCaseList=sessionFactory.getCurrentSession().createQuery("from TestCase where testSuiteId=:testSuiteId")
														.setParameter("testSuiteId", testSuiteId).setFirstResult(startIndex)
														.setMaxResults(pageSize).list();
			}else{
				testCaseList=sessionFactory.getCurrentSession().createQuery("from TestCase where testSuiteId=:testSuiteId")
						.setParameter("testSuiteId", testSuiteId).list();
			}
			log.debug("list specific successful");
		} catch (RuntimeException re) {
			log.error("list specific failed", re);
		}
		return testCaseList;
	}
	
		
	@Override
	@Transactional
	public List<TestCaseList> listAllTestCases(int productId) {
		log.debug("listing all Test Case List instance");
		List<TestCaseList> testCaseList=null;
		try {
			testCaseList=sessionFactory.getCurrentSession().createQuery("from TestCaseList tcl where tcl.productMaster.productId=:productId").setParameter("productId", productId).list();
			log.debug("list specific successful");
		} catch (RuntimeException re) {
			log.error("list specific failed", re);
		}
		return testCaseList;
	}

	@Override
	@Transactional
	public List<TestCaseList> listTestCases(int testSuiteId) {

		log.debug("listing specific TestCaseList instance");
		List<TestCaseList> testCaseLists=null;
		TestSuiteList testSuiteList = null;
		try {
			testSuiteList = (TestSuiteList) sessionFactory.getCurrentSession().get(TestSuiteList.class, testSuiteId);
			if (testSuiteList == null) {
				log.debug("TestSuite with specified id not found : " + testSuiteId);
				return null;
			}
			Set<TestCaseList> testCases = testSuiteList.getTestCaseLists();
			for(TestCaseList testCaseList:testCases)
			{
				Hibernate.initialize(testCaseList);
			}
			testCaseLists=new ArrayList<TestCaseList>();
			testCaseLists.addAll(testCases);
			log.debug("Found TestSuite by ID successfully");
			} catch (RuntimeException re) {
				log.error("Failed to find TestSuite by ID", re);
				//throw re;
			}
		return testCaseLists;
	}
	
	@Override
	@Transactional
	public Set<TestSuiteList> getTestSuiteSetByTestCaseId(int testCaseId) {
		log.info("inside getTestSuiteSetByTestCaseId");
		
		Set<TestSuiteList> testSuiteLists = null;		
		TestCaseList testcaseList = new TestCaseList();
		try {

			Criteria c = sessionFactory.getCurrentSession().createCriteria(TestCaseList.class,"tcase");			
			c.add(Restrictions.eq("tcase.testCaseId", testCaseId));
			
			List list = c.list();
			testcaseList = (list != null && list.size() != 0)?(TestCaseList) list.get(0) : null;
			
			if(testcaseList != null){
				Hibernate.initialize(testcaseList.getProductMaster());
				Hibernate.initialize(testcaseList.getProductFeature());
				Hibernate.initialize(testcaseList.getTestCaseStepsLists());
				Hibernate.initialize(testcaseList.getTestSuiteLists());
				Hibernate.initialize(testcaseList.getDecouplingCategory());				
				Hibernate.initialize(testcaseList.getTestExecutionResults());
							
				testSuiteLists = testcaseList.getTestSuiteLists(); 
				Hibernate.initialize(testSuiteLists);
				for(TestSuiteList tsuite : testSuiteLists){
					Hibernate.initialize(tsuite.getProductMaster());
					Hibernate.initialize(tsuite.getProductVersionListMaster());
					Hibernate.initialize(tsuite.getScriptTypeMaster());
				}				
			}		
			log.debug("getTestSuiteSetByTestCaseId successful");
		} catch (RuntimeException re) {
			log.error("getTestSuiteSetByTestCaseId failed", re);
			// throw re;
		}
		return testSuiteLists;		
	}
	
	
	@Override
	@Transactional
	public TestCaseList getTestCaseByName(String testCaseName, int productId) {
		List<TestCaseList> list=null;
		TestCaseList tc=null;
		try {

		Criteria c = sessionFactory.getCurrentSession().createCriteria(TestCaseList.class, "tcl");
		c.createAlias("tcl.productMaster", "pm");
		c.add(Restrictions.eq("tcl.testCaseName",testCaseName).ignoreCase());		
		c.add(Restrictions.eq("pm.productId", productId));
		c.add(Restrictions.eq("tcl.status", 1));

		list=c.list();
		tc = (list != null && list.size() != 0) ? (TestCaseList) list.get(0) : null;
		
		} catch (RuntimeException re) {
			log.error("Get Testcase by name failed", re);
		}
		return tc;
	}
	
	@Override
	@Transactional
	public TestCaseList getTestCaseByNameAndCode(String testCaseName,String testCaseCode, int productId) {
		List<TestCaseList> list=null;
		TestCaseList tc=null;
		try {

		Criteria c = sessionFactory.getCurrentSession().createCriteria(TestCaseList.class, "tcl");
		c.createAlias("tcl.productMaster", "pm");
		c.add(Restrictions.eq("tcl.testCaseName",testCaseName).ignoreCase());
		c.add(Restrictions.eq("tcl.testCaseCode",testCaseCode).ignoreCase());
		c.add(Restrictions.eq("pm.productId", productId));

		list=c.list();
		tc = (list != null && list.size() != 0) ? (TestCaseList) list.get(0) : null;
		
		} catch (RuntimeException re) {
			log.error("Get Testcase by name failed", re);
		}
		return tc;
	}

	@Override
	@Transactional
	public TestCaseList getTestCaseByNameInTestSuite(String testCaseName, int productId, Integer testSuiteId) {
		List<TestCaseList> list=null;
		TestCaseList tc=null;
		try {
			Criteria c = sessionFactory.getCurrentSession().createCriteria(TestCaseList.class, "tcl");
			c.createAlias("tcl.productMaster", "pm");	
			c.add(Restrictions.eq("tcl.testCaseName",testCaseName));
			c.add(Restrictions.eq("pm.productId", productId));	
			list=c.list();
			if (list.size() < 0) {
				tc = null;
			} else if (list.size() == 1) {
				tc = list.get(0);
			} else if (testSuiteId == null && list.size() == 1) {
				tc = list.get(0);
			} else {
				boolean testCasePartOfTestSuite = false;
				for (TestCaseList testCase : list) {
					Set<TestSuiteList> testCaseTestSuites = testCase.getTestSuiteLists();
					if (testCaseTestSuites.size() <= 0) {
					} else {
						for (TestSuiteList testSuite : testCaseTestSuites) {
							if (testSuite.getTestSuiteId() == testSuiteId) {
								tc = testCase;
								testCasePartOfTestSuite = true;
								break;
							}
						}
						if (testCasePartOfTestSuite && tc != null) {
							break;
						} 	
					}
				}
				if (!testCasePartOfTestSuite && list.size() == 1) {
					tc = list.get(0);
				}
			} 
		} catch (RuntimeException re) {
			log.error("Get Testcase by name in a test suite failed", re);
		}
		return tc;
	}

	
	@Override
	@Transactional
	public List<TestCaseList> getTestCasesByName(String testCaseName) {
		
		if (testCaseName == null || testCaseName.trim().equals("")) {
			log.debug("Testcase Name is null. Cannot find it");
			return null;
		}
		List<TestCaseList> list=null;
		String hql = "from TestCaseList c where testCaseName = :name or testCaseCode = :code";

		//TODO : Look for the test case name within a product and not across the entire set of products
		//Testcase needs reference to Product
		try {
			list = sessionFactory.getCurrentSession().createQuery(hql).setParameter("name", testCaseName).setParameter("code", testCaseName).list();
			log.debug("Get by name successful");
		} catch (RuntimeException re) {
			log.error("Get Testcase by name failed", re);
		}
		if (list != null && !list.isEmpty())
			return list;
		else 
			return null;
	}

	@Override
	@Transactional
	public TestCaseStepsList getTestCaseStepByName(String testCaseStepName, int productId) {
		
		List<TestCaseStepsList> list=null;
		TestCaseStepsList tc=null;
		try {

		Criteria c = sessionFactory.getCurrentSession().createCriteria(TestCaseStepsList.class, "tsl");
		c.createAlias("tsl.testCaseList", "tcl");
		c.createAlias("tcl.productMaster", "pm");

		c.add(Restrictions.eq("tsl.testStepName",testCaseStepName));
		c.add(Restrictions.eq("pm.productId", productId));

		list=c.list();
		tc = (list != null && list.size() != 0) ? (TestCaseStepsList) list.get(0) : null;
		
		} catch (RuntimeException re) {
			log.error("Get Testcase by name failed", re);
		}
		return tc;
		
	}

	@Override
	@Transactional
	public TestCaseStepsList getTestCaseStepByName(String testCaseStepName, int productId, int testCaseId) {
		
		List<TestCaseStepsList> list=null;
		TestCaseStepsList tc=null;
		try {

		Criteria c = sessionFactory.getCurrentSession().createCriteria(TestCaseStepsList.class, "tsl");
		c.createAlias("tsl.testCaseList", "tcl");
		c.createAlias("tcl.productMaster", "pm");

		c.add(Restrictions.eq("tsl.testStepName",testCaseStepName));
		c.add(Restrictions.eq("tcl.testCaseId", testCaseId));
		c.add(Restrictions.eq("pm.productId", productId));

		list=c.list();
		tc = (list != null && list.size() != 0) ? (TestCaseStepsList) list.get(0) : null;
		
		} catch (RuntimeException re) {
			log.error("Get Testcase by name failed", re);
		}
		return tc;
		
	}

	
	@Override
	@Transactional
	public List<TestCaseStepsList> getTestCaseStepsByName(String testCaseStepName) {
		
		if (testCaseStepName == null || testCaseStepName.trim().equals("")) {
			log.debug("Teststep Name is null. Cannot find it");
			return null;
		}
		List<TestCaseStepsList> list=null;
		String hql = "from TestCaseStepsList c where testStepName = :name";
		try {
			list = sessionFactory.getCurrentSession().createQuery(hql).setParameter("name", testCaseStepName).list();
			log.debug("Get by name successful");
		} catch (RuntimeException re) {
			log.error("Get Teststep by name failed", re);
		}
		if (list != null && !list.isEmpty())
			return list;
		else 
			return null;
	}

	@Override
	@Transactional
	public int addTestCaseStep(TestCaseStepsList testCaseStep) {
		
		log.debug("adding TestCaseStepsList instance");
		try {
			sessionFactory.getCurrentSession().save(testCaseStep);
			log.debug("add successful");
		} catch (RuntimeException re) {
			log.error("add failed", re);
		}
		return testCaseStep.getTestStepId().intValue();
				
	}

	//Changes for Integration of Test Management tools - Priya.B

	@Override
	@Transactional
	public List<TestCaseList> getTestCaseListByProductId(int productId, Integer jtStartIndex, Integer jtPageSize) {
		
		List<TestCaseList> list=null;
	
		try {
			LinkedList<Integer> resList = new LinkedList<>();
			resList.add(0);
			resList.add(1);
			Criteria c = sessionFactory.getCurrentSession().createCriteria(TestCaseList.class, "testcase");
			c.add(Restrictions.eq("testcase.productMaster.productId", productId));
			c.add(Restrictions.in("testcase.status", resList));
			if(jtStartIndex != null && jtPageSize != null)
				c.setFirstResult(jtStartIndex).setMaxResults(jtPageSize);
			c.addOrder(Order.asc("testcase.testCaseId"));
			list = c.list();
			if(list!= null && !list.isEmpty()){
				for(TestCaseList testCase : list){
					Hibernate.initialize(testCase.getProductMaster());
					if(testCase.getProductMaster().getProductType()!=null){
						Hibernate.initialize(testCase.getProductMaster().getProductType());
					}
					if(testCase.getProductFeature() != null){
						Hibernate.initialize(testCase.getProductFeature());
					}
					if(testCase.getProductVersionList() != null){
						Set<ProductVersionListMaster> pvmset = testCase.getProductVersionList();
						for (ProductVersionListMaster productVersionListMaster : pvmset) {
							Hibernate.initialize(productVersionListMaster);
							Hibernate.initialize(productVersionListMaster.getTestCaseLists());	
						}											
					}
					Hibernate.initialize(testCase.getTestCaseStepsLists());
					Hibernate.initialize(testCase.getTestSuiteLists());
					Hibernate.initialize(testCase.getDecouplingCategory());
					Hibernate.initialize(testCase.getExecutionTypeMaster());
					Hibernate.initialize(testCase.getTestcaseTypeMaster());
				}
			}
			
			log.debug("Get testcaselist by ProductId successful");
		} catch (RuntimeException re) {
			log.error("Get testcaselist by ProductId failed", re);	
		}
		if (list != null && !list.isEmpty())
			return list;
		else 
			return null;
	}
	
	
	@Override
	@Transactional
	public TestCaseList getTestCaseByCode(String testCaseCode) {
		
		List<TestCaseList> list=null;
		TestCaseList testCase = null;
		String hql = "from TestCaseList c where testCaseCode = :code";

		try {
			list = sessionFactory.getCurrentSession().createQuery(hql).setParameter("code", testCaseCode).list();
			
			testCase = ((list!= null && !list.isEmpty())?list.get(0):null);
			
			if(testCase!= null){
					Hibernate.initialize(testCase.getProductMaster());
					Hibernate.initialize(testCase.getTestSuiteLists());
					Hibernate.initialize(testCase.getTestCaseStepsLists());					
			}		
			log.debug("Get TestCase by Code successful");
		} catch (RuntimeException re) {
			log.error("Get Testcase by Code failed", re);		
			
		}
		return testCase;
	}

	@Override
	@Transactional
	public Boolean IsTestCaseExistsByCode(String testCaseCode) {
		
		List<TestCaseList> list=null;
		String hql = "from TestCaseList c where testCaseCode = :code";

		try {
			list = sessionFactory.getCurrentSession().createQuery(hql).setParameter("code", testCaseCode).list();
			log.debug("Get TestCase by Code successful");
		} catch (RuntimeException re) {
			log.error("Get Testcase by Code failed", re);
			
		}
		if (list != null && !list.isEmpty())
			return true;
		else 
			return false;
	}

	@Override
	@Transactional
	public TestCaseList getTestCaseByCodeProduct(String testCaseCode,
			int productId) {

		List<TestCaseList> testCases = null;
		TestCaseList testCaseList = null;
		String hql = "from TestCaseList tc where testCaseCode = :testCaseCode and productId = :productId";
		try {
			testCases = sessionFactory.getCurrentSession().createQuery(hql)
					.setParameter("testCaseCode", testCaseCode)
					.setParameter("productId", productId).list();
			testCaseList = ((testCases!=null && !testCases.isEmpty())?testCases.get(0):null);
			if(testCaseList!= null){
				Hibernate.initialize(testCaseList.getProductMaster());
				Hibernate.initialize(testCaseList.getTestSuiteLists());
				Hibernate.initialize(testCaseList.getTestCaseStepsLists());
				Hibernate.initialize(testCaseList.getTestExecutionResults());
				Hibernate.initialize(testCaseList.getProductFeature());
			}
			
		} catch (RuntimeException re) {
			log.error("Error in TestCaseList retrieval by Code and Product", re);
		}
		if (testCases != null && !testCases.isEmpty()) {
			return testCases.get(0);
		} else
			return null;
	}

	@Override
	public TestCaseList getTestCaseByNameProduct(String testCaseName, int productId) {
		
		List<TestCaseList> testCases = null;
		String hql = "from TestCaseList tc where testCaseName = :testCaseName and productId = :productId";
		try {
			testCases = sessionFactory.getCurrentSession().createQuery(hql)
					.setParameter("testCaseName", testCaseName)
					.setParameter("productId", productId).list();
		} catch (RuntimeException re) {
			log.error("Error in TestCaseList retrieval by Name and Product", re);
		}
		if (testCases != null && !testCases.isEmpty()) {
			return testCases.get(0);
		} else
			return null;	
		
	}
	
	@Override
	@Transactional
	public List<TestCaseList> getTestCaseByCodeNameProduct(String testCaseCode, String testCaseName, int productId) {
		
		List<TestCaseList> testCases = null;
		String hql = "from TestCaseList tc where testCaseCode = :testCaseCode or testCaseName = :testCaseName and productId = :productId";
		try {
			testCases = sessionFactory.getCurrentSession().createQuery(hql)
					.setParameter("testCaseCode", testCaseCode)
					.setParameter("testCaseName", testCaseName)
					.setParameter("productId", productId).list();
		} catch (RuntimeException re) {
			log.error("Error in TestCaseList retrieval by Code, Name and Product", re);
		}
		if (testCases != null && !testCases.isEmpty()) {
			return testCases;
		} else
			return null;	
		
	}
	
	@Override
	@Transactional
	public TestCaseList getTestCaseByTestCaseCodeTestCaseId(String testCaseCode, Integer testCaseId, int productId) {
		
		List<TestCaseList> testCases = null;
		String hql = "from TestCaseList tc where (testCaseCode = :testCaseCode and productId = :productId) or testCaseId = :testCaseId";
		try {
			testCases = sessionFactory.getCurrentSession().createQuery(hql)
					.setParameter("testCaseCode", testCaseCode)
					.setParameter("testCaseId", testCaseId)
					.setParameter("productId", productId).list();
		} catch (RuntimeException re) {
			log.error("Error in TestCase retrieval by Code, Id", re);
		}
		if (testCases != null && !testCases.isEmpty()) {
			return testCases.get(0);
		} else
			return null;	
	}

	@Override
	@Transactional
	public TestCaseList getExistingTestCaseForTER(TestExecutionResult testExecutionResult, JsonTestExecutionResult jsonTestExecutionResult, ProductMaster product) {
	
		List<TestCaseList> testCases = null;
		TestCaseList testCase = null;
		String testCaseCode = null;
		Integer testCaseId = null;
		String testCaseName = null;
		Integer productId = null;
		
		testCaseCode = (jsonTestExecutionResult.getTestCaseCode() == null) ? "" : jsonTestExecutionResult.getTestCaseCode();
		testCaseName = (testExecutionResult.getTestCase() == null) ? "" : testExecutionResult.getTestCase();
		testCaseId = (jsonTestExecutionResult.getTestCaseId() == null) ? -1 : jsonTestExecutionResult.getTestCaseId();
		
		String hql = "from TestCaseList tc where (testCaseCode = :testCaseCode or testCaseId = :testCaseId or testCaseName = :testCaseName) and productId = :productId";
		try {
			testCases = sessionFactory.getCurrentSession().createQuery(hql)
					.setParameter("testCaseCode", testCaseCode)
					.setParameter("testCaseId", testCaseId)
					.setParameter("testCaseName", testCaseName)
					.setParameter("productId", product.getProductId()).list();
		} catch (RuntimeException re) {
			log.error("Error in TestCase retrieval for TER", re);
		}
		if (testCases != null && !testCases.isEmpty()) {
			return testCases.get(0);
		} else
			return null;	
	
	}

	@Override
	@Transactional
	public TestSuiteList updateTestCasesTestSuites(int testSuiteId,
			int testCaseId) {
		TestSuiteList testsuite = null;
		TestCaseList testcase = null;
		try{
			testsuite = testSuiteListDAO.getByTestSuiteId(testSuiteId);
			testcase = getByTestCaseId(testCaseId);
			if(testsuite != null && testcase != null){
				boolean needToUpdateOrAdd = false;
				Set<TestCaseList> testCaseSet = testsuite.getTestCaseLists();
				// if no associated record exists need to add the new records
				if(testCaseSet.size() ==0)
				{
					needToUpdateOrAdd = true;
				}else{
					// Associated record exists
					log.debug("size > 0");
					TestCaseList testCaseForProcessing = testCaseSet.iterator().next();
					if (testCaseForProcessing != null) {
						log.info("testCaseForProcessing not null");
						int alreadyAvailableTestCaseId = testCaseForProcessing.getTestCaseId().intValue();

						// If associated record is for the same TestCaseList, don't do any addition/updation
						if (alreadyAvailableTestCaseId != testCaseId) {	
							
							// Now association exists for different TestCaseList, so that association need to be cleared in testSuite object first
							log.debug("values or different");
							testsuite.getTestCaseLists().clear();						
							log.debug("testsuite.getTestCaseLists().size()="+testsuite.getTestCaseLists().size());
							//sessionFactory.getCurrentSession().saveOrUpdate(testsuite);						
							
							// Now association exists for different Testcaselist, 
							// so that association need to be cleared for Testcaselist
							TestCaseList testCaseListAvailable = getByTestCaseId(alreadyAvailableTestCaseId);		
									
							for (TestSuiteList tsuite: testCaseListAvailable.getTestSuiteLists())
							{
								log.debug("tsuite.getTestSuiteId().intValue()"+tsuite.getTestSuiteId().intValue());
							
								// Now association exists for different TestcaseList, but for same testsuite id, 
								// so that association needs to be removed from Testcase object
								if (tsuite.getTestSuiteId().intValue() == testSuiteId) {	
									log.debug("Testsuite found in decouple");
									testCaseListAvailable.getTestSuiteLists().remove(tsuite);
									sessionFactory.getCurrentSession().saveOrUpdate(testCaseListAvailable);
									log.debug("testCaseListAvailable.getTestSuiteLists().size()="+testCaseListAvailable.getTestSuiteLists().size());
									break;
								}	
							}
								
							needToUpdateOrAdd = true;
						}	
					}
				
				}
				if (needToUpdateOrAdd) {
					testsuite.getTestCaseLists().add(testcase);
					testcase.getTestSuiteLists().add(testsuite);						
					sessionFactory.getCurrentSession().saveOrUpdate(testsuite);
					sessionFactory.getCurrentSession().saveOrUpdate(testcase);
				}
			}
		}catch (RuntimeException re) {
			log.error("list specific failed", re);
			//throw re;
		}
		return testsuite;
	}

	@Override
	@Transactional
	public TestCaseList updateTestSuiteTestCasesOneToMany(int testCaseId, int testSuiteId, String maporunmap) {
		log.info("updateProductFeatureTestCases method updating ProductFeature - TestCase Association");
		TestCaseList testCaseList = null;
		TestSuiteList testSuiteList = null;
		Set<TestCaseList> testCaseSet = null;
		try{
			testCaseList = getByTestCaseId(testCaseId);
			testSuiteList = testSuiteListDAO.getByTestSuiteId(testSuiteId);
			if (testCaseList != null && testSuiteList != null){
				boolean needToUpdateOrAdd = false;
				if(maporunmap.equalsIgnoreCase("map")){				
						TestSuiteList ts = testSuiteList;
						ts.getTestCaseLists().add(testCaseList);
						sessionFactory.getCurrentSession().saveOrUpdate(ts);					
				}else if(maporunmap.equalsIgnoreCase("unmap")){
					testCaseSet =  testSuiteList.getTestCaseLists();
					if(testCaseSet.size() != 0){
						if(testCaseSet.contains(testCaseList)){
							TestSuiteList ts1 = testSuiteList;
							ts1.getTestCaseLists().remove(testCaseList);
							sessionFactory.getCurrentSession().saveOrUpdate(ts1);
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
	public TestCaseList listBytestSuiteId(int testSuiteId){
		log.debug(" list Testcase By testSuiteId instance");
		//
		TestCaseList testCaseList=null;
		try {			 
			Criteria c = sessionFactory.getCurrentSession().createCriteria(TestCaseList.class,"testcase");
			c.createAlias("testcase.testSuiteLists", "tsuite");
			c.add(Restrictions.eq("tsuite.testSuiteId", testSuiteId));
			List list=c.list();
			
			testCaseList = (list!=null && list.size()!=0)?(TestCaseList)list.get(0):null;
			Set<TestSuiteList> testSuiteLists = null;	
			if(testCaseList != null){
				Hibernate.initialize(testCaseList.getProductMaster());
				Hibernate.initialize(testCaseList.getProductFeature());
				Hibernate.initialize(testCaseList.getTestCaseStepsLists());
				Hibernate.initialize(testCaseList.getTestSuiteLists());
				Hibernate.initialize(testCaseList.getDecouplingCategory());				
				Hibernate.initialize(testCaseList.getTestExecutionResults());
							
				testSuiteLists = testCaseList.getTestSuiteLists(); 
				Hibernate.initialize(testSuiteLists);
				for(TestSuiteList tsuite : testSuiteLists){
					Hibernate.initialize(tsuite.getProductMaster());
					Hibernate.initialize(tsuite.getProductVersionListMaster());
					Hibernate.initialize(tsuite.getScriptTypeMaster());
				}				
			}	
			log.debug("list Testcase By testSuiteId instance successful");			
			} catch (RuntimeException re) {
				log.error("list specific failed", re);
			}
		return testCaseList;
	}

	@Override
	@Transactional
	public List<TestCaseList> getTestCaseByProductVersionId(int startIndex,
			int pageSize, int productVersionListId) {
		log.debug("getting TestSuiteList instance by productVersionList id");		
		List<TestSuiteList> testSuiteList = null;
		List<TestCaseList> testCaseList = new ArrayList<TestCaseList>();
		List<ProductVersionListMaster> productVerList = null;
		try {
			Criteria c = sessionFactory.getCurrentSession().createCriteria(ProductVersionListMaster.class, "prodVersion");			
			c.add(Restrictions.eq("prodVersion.productVersionListId", productVersionListId));
			if(startIndex!=-1 && pageSize!=-1){
				c.setFirstResult(startIndex);
				c.setMaxResults(pageSize);
			}
			productVerList = c.list();			
			log.debug("list successful");
			
			for (ProductVersionListMaster prodversion : productVerList) {
				Hibernate.initialize(prodversion.getProductMaster());
				
				if(prodversion.getTestCaseLists() != null){
					Hibernate.initialize(prodversion.getTestCaseLists());
					Set<TestCaseList> testCases = prodversion.getTestCaseLists();					
					for (TestCaseList testCase : testCases) {
						testCaseList.add(testCase);
						Hibernate.initialize(testCase.getTestSuiteLists());
						Hibernate.initialize(testCase.getTestCaseStepsLists());
						if(testCase.getProductFeature() != null){
							Hibernate.initialize(testCase.getProductFeature());
						}
						if(testCase.getDecouplingCategory() != null){
							Hibernate.initialize(testCase.getDecouplingCategory());
						}
					}
					
				}
				
				if(prodversion.getTestSuiteLists() != null){
					Hibernate.initialize(prodversion.getTestSuiteLists());
					Set<TestSuiteList> testsuite = prodversion.getTestSuiteLists();
					for (TestSuiteList testSuiteobj : testsuite) {						
						Hibernate.initialize(testSuiteobj.getProductMaster());
						Hibernate.initialize(testSuiteobj.getProductVersionListMaster());
						Hibernate.initialize(testSuiteobj.getTestCaseLists());
						Hibernate.initialize(testSuiteobj.getScriptTypeMaster());
					}
				}		
				
			}
			log.debug("getByProductVersionId successful");
		} catch (RuntimeException re) {
			log.error("getByProductVersionId failed", re);
		}
		return testCaseList;    
	}

	@Override
	@Transactional
	public List<TestCaseList> getTestCasesMappedToFeature(Integer productFeatureId) {
		log.debug(" getTestCasesMappedToFeature");
		List<TestCaseList> testCaseList=new ArrayList<TestCaseList>();		
		try {			 
			Criteria c = sessionFactory.getCurrentSession().createCriteria(TestCaseList.class,"testcase");	
			c.createAlias("testcase.productFeature", "feature");
			c.add(Restrictions.eq("feature.productFeatureId", productFeatureId));
			testCaseList =c.list();			
			Set<TestSuiteList> testSuiteLists = null;	
			Set<ProductFeature> featureSet = null;	
			Set<DecouplingCategory> decouplingSet = null;
			if(testCaseList != null){
				for (TestCaseList tCase : testCaseList) {
					Hibernate.initialize(tCase.getProductMaster());					
					if(tCase.getProductFeature() != null){
						featureSet = tCase.getProductFeature();
						Hibernate.initialize(featureSet);	
						for (ProductFeature productFeature : featureSet) {
							Hibernate.initialize(productFeature.getParentFeature());	
							Hibernate.initialize(productFeature.getChildFeatures());
							Hibernate.initialize(productFeature.getTestCaseList());
						}						
					}
					Hibernate.initialize(tCase.getTestCaseStepsLists());
					Hibernate.initialize(tCase.getTestSuiteLists());					
					if(tCase.getDecouplingCategory() != null){
						decouplingSet = tCase.getDecouplingCategory();
						Hibernate.initialize(decouplingSet);	
						for (DecouplingCategory decoupling : decouplingSet) {
							Hibernate.initialize(decoupling.getParentCategory());	
							Hibernate.initialize(decoupling.getChildCategories());
							Hibernate.initialize(decoupling.getTestCaseList());
							Hibernate.initialize(decoupling.getUserTypeMasterNew());
						}						
					}
					Hibernate.initialize(tCase.getTestExecutionResults());								
					testSuiteLists = tCase.getTestSuiteLists(); 
					Hibernate.initialize(testSuiteLists);
					for(TestSuiteList tsuite : testSuiteLists){
						Hibernate.initialize(tsuite.getProductMaster());
						Hibernate.initialize(tsuite.getProductVersionListMaster());
						Hibernate.initialize(tsuite.getScriptTypeMaster());
					}
					
					if(tCase.getExecutionTypeMaster() != null) {
						Hibernate.initialize(tCase.getExecutionTypeMaster());	
					}
					
					if(tCase.getTestcaseExecutionType() != null) {
						Hibernate.initialize(tCase.getTestcaseExecutionType());	
					}
				}
								
			}	
			log.debug("getTestCasesMappedToFeature successful");			
			} catch (RuntimeException re) {
				log.error("list specific failed", re);
			}
		return testCaseList;
	}
	@Override
	@Transactional
	public List<TestCaseList> getTestCaseListByProductIdByType(int productId,
			int executionType) {
		List<TestCaseList> list=null;
		try {
			String hql="";
		if(executionType!=-1){
		 hql = "from TestCaseList c where productId = :productId and executionTypeId=:executionTypeId order by c.testCaseId asc";
		
			list = sessionFactory.getCurrentSession().createQuery(hql).setParameter("productId", productId).setParameter("executionTypeId", executionType).list();
		}else{
			 hql = "from TestCaseList c where productId = :productId order by c.testCaseId asc";
				
				list = sessionFactory.getCurrentSession().createQuery(hql).setParameter("productId", productId).list();
				
		}
		if(list!= null && !list.isEmpty()){
				for(TestCaseList testCase : list){
					Hibernate.initialize(testCase.getProductMaster());
					Hibernate.initialize(testCase.getProductFeature());
					Hibernate.initialize(testCase.getTestCaseStepsLists());
					Hibernate.initialize(testCase.getTestSuiteLists());
					Hibernate.initialize(testCase.getDecouplingCategory());
					Hibernate.initialize(testCase.getExecutionTypeMaster());
					Hibernate.initialize(testCase.getTestcaseTypeMaster());
				}
			}
			
			log.debug("Get testcaselist by ProductId successful");
		} catch (RuntimeException re) {
			log.error("Get testcaselist by ProductId failed", re);	
		}
		if (list != null && !list.isEmpty())
			return list;
		else 
			return null;
	}

	@Override
	@Transactional
	public List<ProductVersionListMaster> getTestCasesVersions(int productId,
			int testCaseId) {
		
		// TODO Auto-generated method stub
		log.info("Getting Selected Version in DAO  for Product ID : " + productId + " : TestCase ID : " + testCaseId);
		log.debug("listing getTestCasesVersions");
		
		List<ProductVersionListMaster> productVersionListMastersList = null;
		try {
			productVersionListMastersList = sessionFactory.getCurrentSession().createQuery("select distinct e"
									+ " from TestCaseList as tc join tc.productVersionList as pv"
									+ " where tc.productMaster.productId = :productId and tc.testCaseId = :testCaseId"
									+ " order by pv.productVersionListId asc")
									.setParameter("productId", productId)
									.setParameter("testCaseId", testCaseId)
									.list();

			productVersionListMastersList = sessionFactory.getCurrentSession().createQuery("select distinct e"
			+ " from ProductVersionListMaster as pv, TestCaseList as tc"
			+ " where pv.productVersionListId = tc.productVersionList.productVersionListId"
			+ " and tc.productMaster.productId = :productId and tc.testCaseId = :testCaseId"
			+ " order by pv.productVersionListId asc")
			.setParameter("productId", productId)
			.setParameter("testCaseId", testCaseId)
			.list();		
				log.debug("list successful");
			log.info("Selected Version in DAO : " + productVersionListMastersList.size() + " : Product ID : " + productId + " : TestCase ID : " + testCaseId);
		} catch (RuntimeException re) {
			log.error("list failed", re);
		}
		return productVersionListMastersList;
	}

	@Override
	@Transactional
	public TestCaseList processTestExecutionResult(TestCaseList testCaseList, JsonTestExecutionResult jsonWorkPackageTestCaseExecutionPlanForTerminal) {
		
		return processTestExecutionResult(testCaseList, jsonWorkPackageTestCaseExecutionPlanForTerminal, null);
	}

	private TestCaseStepsList updateTestStepWithDetailsFromTER(JsonTestExecutionResult jsonWorkPackageTestCaseExecutionPlanForTerminal, TestCaseStepsList testStep) {
		if (!(jsonWorkPackageTestCaseExecutionPlanForTerminal.getTestStepDescription() == null || jsonWorkPackageTestCaseExecutionPlanForTerminal.getTestStepDescription().trim().isEmpty()))
			testStep.setTestStepDescription(jsonWorkPackageTestCaseExecutionPlanForTerminal.getTestStepDescription());
		if (!(jsonWorkPackageTestCaseExecutionPlanForTerminal.getTestStepExpectedOutput() == null || jsonWorkPackageTestCaseExecutionPlanForTerminal.getTestStepExpectedOutput().trim().isEmpty()))
			testStep.setTestStepExpectedOutput(jsonWorkPackageTestCaseExecutionPlanForTerminal.getTestStepExpectedOutput());
		if (!(jsonWorkPackageTestCaseExecutionPlanForTerminal.getTestStepInput() == null || jsonWorkPackageTestCaseExecutionPlanForTerminal.getTestStepInput().trim().isEmpty()))
			testStep.setTestStepInput(jsonWorkPackageTestCaseExecutionPlanForTerminal.getTestStepInput());
		if (!(jsonWorkPackageTestCaseExecutionPlanForTerminal.getTestStepCode() == null || jsonWorkPackageTestCaseExecutionPlanForTerminal.getTestStepCode().trim().isEmpty()))
			testStep.setTestStepCode(jsonWorkPackageTestCaseExecutionPlanForTerminal.getTestStepCode());
		
		try {
			testStep.setStatus(1);
			sessionFactory.getCurrentSession().saveOrUpdate(testStep);
			log.info("Updated Test step with details from TER : " + jsonWorkPackageTestCaseExecutionPlanForTerminal.getTestStepName());
		} catch (RuntimeException r) {
			log.error("Creating testStep failed", r);
		}
		
		testStep = testCaseStepsDAO.getByTestStepId(testStep.getTestStepId());
		return testStep;

		
	}
	
	@Override
	@Transactional
	public TestCaseList processTestExecutionResult(TestCaseList testCaseList, JsonTestExecutionResult jsonWorkPackageTestCaseExecutionPlanForTerminal, Integer testSuiteId) {
		log.info("Processing processTestExecutionResult instance");
		TestCaseList testCase = null;
		TestSuiteList testSuite=null;
		String testCaseName = jsonWorkPackageTestCaseExecutionPlanForTerminal.getTestCase().trim();
		try {
			testCaseName = testCaseName.replaceAll("[^A-Za-z0-9]", "");
			log.info("After removing special characters from testcase : " + testCaseName);
			//testCaseName = testCaseName.replaceAll("_", " ");
			//log.info("After removing space characters from testcase : " + testCaseName);
			TestRunJob testRunJob= environmentDAO.getTestRunJobById(jsonWorkPackageTestCaseExecutionPlanForTerminal.getTestRunListId());
			ProductMaster product = testRunJob.getWorkPackage().getProductBuild().getProductVersion().getProductMaster();		
			Set<TestCaseStepsList> testCaseStepsLists=new HashSet<TestCaseStepsList>();
			TestCaseStepsList testStep = null;
			if(testStepUpdateFromTER == null){
				testStepUpdateFromTER = "Yes";
			} else if (!(testStepUpdateFromTER == null || testStepUpdateFromTER.equalsIgnoreCase("Yes"))){
				testStepUpdateFromTER = "No";
			}
			
			//If the property is set then update all the test step details related to test case each and every time			
			if (testStepUpdateFromTER != null && testStepUpdateFromTER.equalsIgnoreCase("Yes")) {
				if (jsonWorkPackageTestCaseExecutionPlanForTerminal.getTestStepId() != null){
					log.info("Test Step ID is available : " + jsonWorkPackageTestCaseExecutionPlanForTerminal.getTestStepId());
					testStep = testCaseStepsDAO.getByTestStepId(jsonWorkPackageTestCaseExecutionPlanForTerminal.getTestStepId());					
				} else if(!(jsonWorkPackageTestCaseExecutionPlanForTerminal.getTestStepName() == null || jsonWorkPackageTestCaseExecutionPlanForTerminal.getTestStepName().isEmpty())){
					testStep = testCaseStepsDAO.getByTestStepName(jsonWorkPackageTestCaseExecutionPlanForTerminal.getTestStepName());
				}
				
				if (testStep != null) {
					log.info("Finding Test Step and Test Case");
					testCase = testStep.getTestCaseList();	
					testCase.setStatus(1);
					//Update the test step with details from the TER object
					testStep = updateTestStepWithDetailsFromTER(jsonWorkPackageTestCaseExecutionPlanForTerminal, testStep);	
					log.info("Found Test Step and Test Case");
					return testCase;
				} else {
					log.debug("The Teststep ID / Name provided is not correct or not obtained.");
				}
			}
			
			
			//Since teststep could not be obtained with ID or code, Try to get it with teststep name. TestStep name will be unique within a test case
			//For this, first get the test case
			//Get testcase by ID
			if(jsonWorkPackageTestCaseExecutionPlanForTerminal.getTestCaseId() != null){
				if (!(jsonWorkPackageTestCaseExecutionPlanForTerminal.getTestCaseId() <= 0)) {
					log.debug("Test Case Id is available : " + jsonWorkPackageTestCaseExecutionPlanForTerminal.getTestCaseId());
					testCase = getByTestCaseId(jsonWorkPackageTestCaseExecutionPlanForTerminal.getTestCaseId());
				} else {
					log.debug("Test Case Id is not avaiable");
				}
			}else {
				log.debug("Test Case Id is not avaiable");
			}
			//If not obtained through ID, try to get it with testcase code
			if (testCase == null) {
				if (!(jsonWorkPackageTestCaseExecutionPlanForTerminal.getTestCaseCode()== null || jsonWorkPackageTestCaseExecutionPlanForTerminal.getTestCaseCode().trim().isEmpty())) {
					log.info("Test Case Code is available : " + jsonWorkPackageTestCaseExecutionPlanForTerminal.getTestCaseCode());
					testCase = getByTestCaseCode(jsonWorkPackageTestCaseExecutionPlanForTerminal.getTestCaseCode(), product);
				} else {
					log.info("Test Case code is not avaiable");
				}
			} else {
				log.debug("Found Test case based on Code provided");
			}
			log.info("testcase : " +jsonWorkPackageTestCaseExecutionPlanForTerminal.getTestCase());
			log.info("testcase : " +jsonWorkPackageTestCaseExecutionPlanForTerminal.getTestCase().trim());
			//If not available by ID and code, try to get by name
			if (testCase == null) {
				log.info("testcase : " +jsonWorkPackageTestCaseExecutionPlanForTerminal.getTestCase());
				log.info("testcase : " +jsonWorkPackageTestCaseExecutionPlanForTerminal.getTestCase().trim());
				testCase = getSimilarTestCasesByName(testCaseName, product.getProductId());
				/*if (testSuiteId == null) {
					testCase = getTestCaseByName(testCaseName, product.getProductId());
				} else {
					testCase = getTestCaseByNameInTestSuite(testCaseName, product.getProductId(), testSuiteId);
				}*/
			} else {
				log.info("Found Test case based on Code provided");
			}
			//If not available by name also, create a new testcase
			if (testCase == null) {

				log.info("Test case was not found with given name. Creating new one : " + jsonWorkPackageTestCaseExecutionPlanForTerminal.getTestCaseId());
				testCase = new TestCaseList(jsonWorkPackageTestCaseExecutionPlanForTerminal.getTestCase().trim());
				// Commented the following methods when removing 
				// the "testCaseScriptQualifiedName" and "testCaseScriptFileName" from Test_case_list table.
				if(jsonWorkPackageTestCaseExecutionPlanForTerminal.getTestCaseScriptQualifiedName() != null && !jsonWorkPackageTestCaseExecutionPlanForTerminal.getTestCaseScriptQualifiedName().isEmpty())
					testCase.setTestCaseScriptQualifiedName(jsonWorkPackageTestCaseExecutionPlanForTerminal.getTestCaseScriptQualifiedName());
				else 
					testCase.setTestCaseScriptQualifiedName("com.hcl.atf.taf.testcase");
				testCase.setTestCaseScriptFileName(jsonWorkPackageTestCaseExecutionPlanForTerminal.getTestCaseScriptFileName());
				TestCasePriority tcp = new TestCasePriority();
				tcp=testCasePriorityDAO.getTestCasePriorityBytestcasePriorityId(IDPAConstants.TESTCASE_PRIORITY);
				testCase.setTestCasePriority(tcp);
				ExecutionTypeMaster executionTypeMaster=executionTypeMasterDAO.getExecutionTypeByExecutionTypeId(IDPAConstants.TESTCASE_EXECUTION);
				testCase.setExecutionTypeMaster(executionTypeMaster);				
				testCase.setTestCaseCreatedDate(new Date(System.currentTimeMillis()));
				testCase.setTestCaseSource(TAFConstants.TESTCASE_SOURCE_TAF);
				testCase.setTestCaseType(TAFConstants.TESTCASE_AUTOMATED);
				testCase.setStatus(1);
				TestcaseTypeMaster testcaseTypeMaster=testcaseTypeMasterDAO.getTestcaseTypeMasterBytestcaseTypeId(IDPAConstants.TESTCASE_EXECUTION);
				testCase.setTestcaseTypeMaster(testcaseTypeMaster);
								
				//testCase.setProductType(product.getProductType());
				//set test case code,desc and testcasetype for testcases created from zip file
				testCase.setTestCaseCode(jsonWorkPackageTestCaseExecutionPlanForTerminal.getTestCaseDescription());
				if(jsonWorkPackageTestCaseExecutionPlanForTerminal.getTestCaseDescription() != null)
					testCase.setTestCaseDescription(jsonWorkPackageTestCaseExecutionPlanForTerminal.getTestCaseDescription());
				else
					testCase.setTestCaseDescription(jsonWorkPackageTestCaseExecutionPlanForTerminal.getTestCase());
				if(testRunJob.getRunConfiguration() != null && testRunJob.getRunConfiguration().getProductType() != null)
					testCase.setProductType(testRunJob.getRunConfiguration().getProductType());
				
				testCase.setProductMaster(product);
				//Associate Test case to test suite. New m-n relationship
				sessionFactory.getCurrentSession().save(testCase);
				testSuite = testSuiteListDAO.getByTestSuiteId(jsonWorkPackageTestCaseExecutionPlanForTerminal.getTestSuiteId());				
				testSuite.getTestCaseLists().add(testCase);
				testCase.getTestSuiteLists().add(testSuite);
				//Associate Test Case mapping to the Test Configuration if the testcase is not created
				if(testRunJob.getRunConfiguration() != null && testRunJob.getRunConfiguration().getRunconfigId() != null){
					Integer testConfigId = testRunJob.getRunConfiguration().getRunconfigId();
					if(! productListService.isTestConfigurationTestCaseAlreadyMapped(testConfigId, testSuiteId, testCase.getTestCaseId()))
						productListService.mapTestSuiteTestCasesRunConfiguration(testConfigId, testSuiteId, testCase.getTestCaseId(), "Add");
				}
				try {
					sessionFactory.getCurrentSession().save(testCase);
					sessionFactory.getCurrentSession().saveOrUpdate(testSuite);
					log.debug("Created new testcase : " + testCase.getTestCaseName());
				} catch (RuntimeException r) {
					log.debug("Scenario 3 : Unable to create new testcase", r);
					return testCase;
				}
			} else {
				log.debug("Found Test case using name : " + testCase.getTestCaseName() + " : " + testCase.getTestCaseId());
				// Commented the following methods when removing the "testCaseScriptQualifiedName" and "testCaseScriptFileName" from Test_case_list table. By: Logeswari, On: Feb-11-2015
				// The following logic is implemented to update ScriptQualifiedName, ScriptFileName, testCasePriority if they don't exist in DB.
				if (testCase.getTestCaseScriptQualifiedName() == null && jsonWorkPackageTestCaseExecutionPlanForTerminal.getTestCaseScriptQualifiedName() != null){
					if(!jsonWorkPackageTestCaseExecutionPlanForTerminal.getTestCaseScriptQualifiedName().isEmpty())
						testCase.setTestCaseScriptQualifiedName(jsonWorkPackageTestCaseExecutionPlanForTerminal.getTestCaseScriptQualifiedName());
					else 
						testCase.setTestCaseScriptQualifiedName("com.hcl.atf.taf.testcase");
				} else if (testCase.getTestCaseScriptQualifiedName() != null && testCase.getTestCaseScriptQualifiedName().isEmpty()){
					testCase.setTestCaseScriptQualifiedName("com.hcl.atf.taf.testcase");
				}						
				testCase.setProductMaster(product);
				if (testCase.getTestCaseScriptFileName() == null  && jsonWorkPackageTestCaseExecutionPlanForTerminal.getTestCaseScriptFileName() != null)
					testCase.setTestCaseScriptFileName(jsonWorkPackageTestCaseExecutionPlanForTerminal.getTestCaseScriptFileName());
				testCase.setStatus(1);
				//set test case code,desc and testcasetype for testcases created from zip file
				//Fix for Test Case Code update
				if(testCase.getTestCaseCode() != null && !testCase.getTestCaseCode().trim().isEmpty())
					testCase.setTestCaseCode(testCase.getTestCaseCode());
				else
					testCase.setTestCaseCode(jsonWorkPackageTestCaseExecutionPlanForTerminal.getTestCase());
				if(jsonWorkPackageTestCaseExecutionPlanForTerminal.getTestCaseDescription() != null)
					testCase.setTestCaseDescription(jsonWorkPackageTestCaseExecutionPlanForTerminal.getTestCaseDescription());
				else
					testCase.setTestCaseDescription(jsonWorkPackageTestCaseExecutionPlanForTerminal.getTestCase());
				if(testRunJob.getRunConfiguration() != null && testRunJob.getRunConfiguration().getProductType() != null)
					testCase.setProductType(testRunJob.getRunConfiguration().getProductType());
				try {
					sessionFactory.getCurrentSession().saveOrUpdate(testCase);
					log.debug("Updated existing testcase : " + testCase.getTestCaseName() + "with few details") ;
				} catch (RuntimeException r) {
					log.error("Unable to update testcase details", r);
				}
				
				// The following logic is implemented to fix the issue with TestCase Testsuite association
				//Associate Test case to test suite. New m-n relationship
				testSuite = testSuiteListDAO.getByTestSuiteId(jsonWorkPackageTestCaseExecutionPlanForTerminal.getTestSuiteId());			
				Set<TestCaseList> testCaseListInTestSuite = testSuite.getTestCaseLists();
				if (testCaseListInTestSuite == null || ! testCaseListInTestSuite.contains(testCase)) {
					testSuite.getTestCaseLists().add(testCase);
					try {
						sessionFactory.getCurrentSession().saveOrUpdate(testSuite);
						log.debug("Updated testcase - testsuite association");
					} catch (RuntimeException r) {
						log.error("Scenario 4 : Unable to create association between testcase and testsuite", r);
					}
				} else {
					log.info("Already testcase-testSuite Association Exists") ;
				}
			}
			testSuiteConfigurationService.updateTCtoPVMapping(testCase.getTestCaseId(),testRunJob.getWorkPackage().getProductBuild().getProductVersion().getProductVersionListId(),"1");
			testSuiteConfigurationService.addTestCase(testCase.getTestCaseId(),testSuite.getTestSuiteId());
			//Associate Test Case mapping to the Test Configuration if the testcase is already available but not mapped
			if(testRunJob.getRunConfiguration() != null && testRunJob.getRunConfiguration().getRunconfigId() != null){
				Integer testConfigId = testRunJob.getRunConfiguration().getRunconfigId();
				if(! productListService.isTestConfigurationTestCaseAlreadyMapped(testConfigId, testSuiteId, testCase.getTestCaseId()))
					productListService.mapTestSuiteTestCasesRunConfiguration(testConfigId, testSuiteId, testCase.getTestCaseId(), "Add");
			}
			//Get the test step with test step name. 
			testStep = getTestCaseStepByName(jsonWorkPackageTestCaseExecutionPlanForTerminal.getTestStep(), testCase);
			//If not found by name, create a test step
			if (testStep == null) {				
				testStep = new TestCaseStepsList(jsonWorkPackageTestCaseExecutionPlanForTerminal.getTestStep().trim());
				testStep.setTestStepDescription(jsonWorkPackageTestCaseExecutionPlanForTerminal.getTestStepDescription());
				testStep.setTestStepInput(jsonWorkPackageTestCaseExecutionPlanForTerminal.getTestStepInput());
				testStep.setTestStepExpectedOutput(jsonWorkPackageTestCaseExecutionPlanForTerminal.getTestStepExpectedOutput());
				testStep.setTestStepCreatedDate(new Date(System.currentTimeMillis()));
				testStep.setTestStepSource(TAFConstants.TESTCASE_SOURCE_TAF);
				testStep.setTestStepCode(jsonWorkPackageTestCaseExecutionPlanForTerminal.getTestStepCode());				
				testStep.setTestCaseList(testCase);
				testStep.setStatus(1);
				try {
					sessionFactory.getCurrentSession().save(testStep);
					sessionFactory.getCurrentSession().saveOrUpdate(testCase);
					log.info("Created new test step : " + jsonWorkPackageTestCaseExecutionPlanForTerminal.getTestStep());
				} catch (RuntimeException r) {
					log.error("Creating testStep failed", r);
					return testCase;
				}
			} else {
				testStep.setStatus(1);
				sessionFactory.getCurrentSession().saveOrUpdate(testStep);
				log.info("Found Test step using name : " + testStep.getTestStepName() + " : " + testStep.getTestStepId());
				log.info("Updated Test Step Status");
			}

			//Associate TER to the teststep and test case
			try {
				testCaseStepsLists.add(testStep);
				testCase.setTestCaseStepsLists(testCaseStepsLists);
				sessionFactory.getCurrentSession().saveOrUpdate(testCase);
				log.debug("Successfully associated test case and created a new TestStep");
				return testCase;
			} catch (RuntimeException r) {
				log.error("Associating result to Testcase, test step Scenario 2 : Failed", r);
			}
			return testCase;
		} catch (RuntimeException re) {
			log.error("Unable to associate test case and test step to execution", re);
		}		
		return testCase;
	}	
	
	private TestCaseStepsList getTestCaseStepByName(String testCaseStepName, TestCaseList testCase) {		
		if (testCaseStepName == null || testCaseStepName.trim().equals("")) {
			log.debug("Teststep Name is null. Cannot find it");
			return null;
		}
		
		List<TestCaseStepsList> list=null;
		String hql = "from TestCaseStepsList c where testStepName = :name and c.testCaseList.testCaseId = :testCaseId";
		try {
			list = sessionFactory.getCurrentSession().createQuery(hql).
					setParameter("name", testCaseStepName).
					setParameter("testCaseId", testCase.getTestCaseId()).
					list();
			log.debug("Get by name successful");
		} catch (RuntimeException re) {
			log.error("Get Teststep by name failed", re);
		}
		if (list != null && !list.isEmpty())
			return list.get(0);
		else 
			return null;
	}

@Override
@Transactional
public void updateTestCasesInImport(
		List<TestCaseListDTO> modifiedTestCasesListDTO,
		String tcAddOrUpdateAction, Integer maxBatchProcessingLimit) {
	// TODO Auto-generated method stub
	int count=0;
	for(TestCaseListDTO testCaseDTO : modifiedTestCasesListDTO){
		count++;
			if(tcAddOrUpdateAction.equalsIgnoreCase("Update")){
				update(testCaseDTO.getTestCaseList(),count,maxBatchProcessingLimit);
				
				mongoDBService.addProductTestCaseToMongoDB(testCaseDTO.getTestCaseList().getTestCaseId());
				
				continue;
			}else if(tcAddOrUpdateAction.equalsIgnoreCase("New")){
				TestCaseList testCaseList  = update(testCaseDTO.getTestCaseList(),count,maxBatchProcessingLimit);
				
				mongoDBService.addProductTestCaseToMongoDB(testCaseList.getTestCaseId());
				
				if(testCaseDTO.getFeature() != null && testCaseDTO.getFeature().trim().length()>0){
					mapTestcaseToFeature(testCaseList,testCaseDTO.getFeature());
				}
				if(count==maxBatchProcessingLimit){
					count=0;
				}
			}		
	}

}
private List<String> getListOfTestSteps(String testSteps){
	List<String> listOfTestSteps = null;
	String[] testStepsArray = null;
	if(testSteps != null &&  !testSteps.equals("")){
		testStepsArray = testSteps.split(testStepParser);
		if(testStepsArray != null && testStepsArray.length>0){
			listOfTestSteps = new ArrayList<String>();
			for(String testStep: testStepsArray){
				listOfTestSteps.add(testStep.trim());
			}
		}
	}
	return listOfTestSteps;
}

@Override
@Transactional
public Integer update(TestCaseList testCaseList) {
	log.debug("updating TestCaseList instance");
	try {
			sessionFactory.getCurrentSession().saveOrUpdate(testCaseList);
	} catch (RuntimeException re) {
		log.error("update failed", re);
	}
	return testCaseList.getTestCaseId();
}

	@Override
	@Transactional
	public Integer getTestCaseListSize(Integer productId) {
		Integer size=0;
		try {
			Criteria c = sessionFactory.getCurrentSession().createCriteria(TestCaseList.class, "testcase");
			c.add(Restrictions.eq("testcase.productMaster.productId", productId));
			size = ((Number)c.setProjection(Projections.rowCount()).uniqueResult()).intValue();		
		} catch (RuntimeException re) {
			log.error("Get testcaselist by ProductId failed", re);	
		}
		return size;
	}

	@Override
	@Transactional
	public Integer getUnMappedFeatureCountOfTestCaseByTestCaseId(int productId, int testCaseId) {
		log.info("Get ProductFeature unmapped to Testcaselist");
		int totUnMappedPFListCount = 0;
		try {
			totUnMappedPFListCount=((Number) sessionFactory.getCurrentSession().createSQLQuery("select distinct count(*) from product_feature pf where  pf.productId=:productMasterId and pf.productFeatureId not in(select pfte.productFeatureId from product_feature_has_test_case_list pfte where pfte.testCaseId=:testcaseId)").
					setParameter("productMasterId", productId).
					setParameter("testcaseId", testCaseId).uniqueResult()).intValue();
		} catch (HibernateException e) {
			log.error(e);
		}		
		return totUnMappedPFListCount;
	}
	
	@Override
	@Transactional
	public List<Object[]> getUnMappedFeatureByTestCaseId(int productId, int testCaseId, Integer jtStartIndex, Integer jtPageSize) {
		log.info("Get UnMappedFeatureOfTC with testCaseId");
			List<Object[]> unMappedFeatureListObj = null;
		try {		
			String sql="select distinct productFeatureId, productFeatureName from product_feature pf where  productId=:productMasterId and pf.productFeatureId not in(select pfte.productFeatureId from product_feature_has_test_case_list pfte where pfte.testCaseId=:tCId) ";			
			sql = sql+"OFFSET "+jtStartIndex+" LIMIT "+jtPageSize+"";			
			unMappedFeatureListObj=sessionFactory.getCurrentSession().createSQLQuery(sql).
					setParameter("productMasterId", productId).
					setParameter("tCId", testCaseId).
					list();
			log.debug("unmapped list :"+unMappedFeatureListObj.size());
		
		} catch (RuntimeException re) {
			log.error("Get Feature Unmapped to testCaseId", re);	
		}
		return unMappedFeatureListObj;
		
	}
	
	@Override
	@Transactional
	public List<Object[]> getMappedTestCaseListByProductFeatureId(int productId, int productFeatureId, Integer jtStartIndex, Integer jtPageSize) {
		log.info("Get MappedTCOfFeaturewith ProductFeatureId");
			List<Object[]> unMappedTCListObj = null;
		try {
			String sql="select distinct testCaseId, testCaseName, testCaseCode from test_case_list t where  productId=:productMasterId and t.testCaseId in(select pfte.testCaseId from product_feature_has_test_case_list pfte where pfte.productFeatureId=:featureId)";
			unMappedTCListObj=sessionFactory.getCurrentSession().createSQLQuery(sql).
					setParameter("productMasterId", productId).
					setParameter("featureId", productFeatureId).
					list();
			log.debug("mapped list :"+unMappedTCListObj.size());
		
		} catch (RuntimeException re) {
			log.error("Get testcaselist Mapped to productFeatureId", re);	
		}
		return unMappedTCListObj;
		
	}
	
	@Override
	@Transactional
	public List<Object[]> getMappedFeatureBytestCaseId(int productId, int testCaseId, Integer jtStartIndex, Integer jtPageSize) {
		log.info("Get MappedFeaturesOfTCwith ProductFeatureId");
			List<Object[]> mappedTCListObj = null;
		try {
			String sql="select distinct productFeatureId, productFeatureName from product_feature pf where  productId=:productMasterId and pf.productFeatureId in(select pfte.productFeatureId from product_feature_has_test_case_list pfte where pfte.testCaseId=:tcId)";
			mappedTCListObj=sessionFactory.getCurrentSession().createSQLQuery(sql).
					setParameter("productMasterId", productId).
					setParameter("tcId", testCaseId).
					list();
			log.debug("mapped list :"+mappedTCListObj.size());
		
		} catch (RuntimeException re) {
			log.error("Get Features Mapped to testcaselist failed", re);	
		}
		return mappedTCListObj;
		
	}

	@Override
	@Transactional
	public List<String> getExistingTestStepsCodes(TestCaseList testCase, ProductMaster product) {
		List<String> testStepCodesList = new ArrayList<String>();
		String hql = "SELECT TS.testStepCode from TestCaseStepsList TS WHERE TS.testCaseList.testCaseId=:testCaseId and TS.testCaseList.productMaster.productId=:productId";
		testStepCodesList = sessionFactory.getCurrentSession().createQuery(hql).
				setParameter("testCaseId", testCase.getTestCaseId().intValue()).
				setParameter("productId",product.getProductId().intValue()).
				list();
		return testStepCodesList;
	}

	@Override
	@Transactional
	public List<String> getExistingTestStepsNames(TestCaseList testCase, ProductMaster product) {

		List<String> testStepNamesList = null;
		String hql = "SELECT TS.testStepName from TestCaseStepsList TS WHERE TS.testCaseList.testCaseId=:testCaseId and TS.testCaseList.productMaster.productId=:productId";
		testStepNamesList=sessionFactory.getCurrentSession().createQuery(hql).
				setParameter("testCaseId", testCase.getTestCaseId().intValue()).
				setParameter("productId",product.getProductId().intValue()).
				list();
		return testStepNamesList;
	}
	
	public void mapTestcaseToFeature(TestCaseList testCase, String productFeatureCode){
		Integer testCaseId = testCase.getTestCaseId();
		ProductFeature pf = productFeatureDAO.getByProductFeatureCode(productFeatureCode, testCase.getProductMaster().getProductId());
		Integer pfId = pf.getProductFeatureId();
		log.debug("testcaseId: "+testCaseId+" -------> pfId: "+pfId);
	try {
			int	count = (sessionFactory
					.getCurrentSession()
					.createSQLQuery(
							"insert into product_feature_has_test_case_list (productFeatureId,testCaseId) values ("+pfId+","+testCaseId+")").executeUpdate());
			log.debug("Include Mapping for Test case and product Feature successfully");
		} catch (Exception re) {
			log.error("Failed to Include Mapping for Test case and product Feature ", re);
			
		}
	}
	@Override
	@Transactional
	public int unmapTestcaseFromFeatureMapping(Integer testCaseId){		
		log.debug(" unmapTestcaseFromFeatureMapping testcaseId: "+testCaseId);
		int	count = 0;
		try {
			count = (sessionFactory
					.getCurrentSession()
					.createSQLQuery(
							"delete from product_feature_has_test_case_list where testCaseId=:tcId").setParameter("tcId", testCaseId).executeUpdate());
			log.debug("unmapTestcaseFromFeatureMapping successfully testcaseId: "+testCaseId);
		} catch (Exception re) {
			log.error("unmapTestcaseFromFeatureMapping successfully testcaseId: "+testCaseId+"", re);
			
		}
		return count;
	}
	
	@Override
	@Transactional
	public int unmapTestcaseFromVersionMapping(Integer testCaseId){		
		log.debug(" unmapTestcaseFromVersionMapping testcaseId: "+testCaseId);
		int	count = 0;
		try {
			count = (sessionFactory
					.getCurrentSession()
					.createSQLQuery(
							"delete from test_case_list_has_product_version_list where testCaseId=:tcId").setParameter("tcId", testCaseId).executeUpdate());
			log.debug("unmapTestcaseFromVersionMapping successfully testcaseId: "+testCaseId);
		} catch (Exception re) {
			log.error("unmapTestcaseFromVersionMapping successfully testcaseId: "+testCaseId+"", re);
			
		}
		return count;
	}
	
	@Override
	@Transactional
	public int unmapTestcaseFromTestSuiteMapping(Integer testCaseId){		
		log.debug(" unmapTestcaseFromTestSuiteMapping testcaseId: "+testCaseId);
		int	count = 0;
		try {
			count = (sessionFactory
					.getCurrentSession()
					.createSQLQuery(
							"delete from test_suite_list_has_test_case_list where testCaseId=:tcId").setParameter("tcId", testCaseId).executeUpdate());
			log.debug("unmapTestcaseFromTestSuiteMapping successfully testcaseId: "+testCaseId);
		} catch (Exception re) {
			log.error("unmapTestcaseFromTestSuiteMapping successfully testcaseId: "+testCaseId+"", re);
			
		}
		return count;
	}
	
	@Override
	@Transactional
	public int unmapTestcaseFromDecouplingCategoryMapping(Integer testCaseId){		
		log.debug(" unmapTestcaseFromDecouplingCategoryMapping testcaseId: "+testCaseId);
		int	count = 0;
		try {
			count = (sessionFactory
					.getCurrentSession()
					.createSQLQuery(
							"delete from decouplingcategory_has_test_case_list where testCaseId=:tcId").setParameter("tcId", testCaseId).executeUpdate());
			log.debug("unmapTestcaseFromDecouplingCategoryMapping successfully testcaseId: "+testCaseId);
		} catch (Exception re) {
			log.error("unmapTestcaseFromDecouplingCategoryMapping successfully testcaseId: "+testCaseId+"", re);
			
		}
		return count;
	}
	@Override
	@Transactional
	public List<String> getExistingTestCaseCodes(ProductMaster product) {
		List<String> testCaseCodesList = null;
		String hql = "SELECT TC.testCaseCode from TestCaseList TC WHERE TC.productMaster.productId=:productId";
		testCaseCodesList=sessionFactory.getCurrentSession().createQuery(hql).
				setParameter("productId",product.getProductId().intValue()).
				list();
		return testCaseCodesList;
	}

	@Override
	@Transactional
	public List<String> getExistingTestCaseNames(ProductMaster product) {
		List<String> testCaseNamesList = null;
		String hql = "SELECT TC.testCaseName from TestCaseList TC WHERE TC.productMaster.productId=:productId";
		testCaseNamesList=sessionFactory.getCurrentSession().createQuery(hql).
				setParameter("productId",product.getProductId().intValue()).
				list();
		return testCaseNamesList;
	}

	
	@Override
	@Transactional
	public List<TestCaseList> listAllProductTestCasesByLastSyncDate(int startIndex, int pageSize, Date startDate,Date endDate) {
		log.debug("listing all TestCases");
		List<TestCaseList> testCasesList=null;
		try {
			Criteria c = sessionFactory.getCurrentSession().createCriteria(TestCaseList.class, "testCaseList");
			if (startDate != null) {
				c.add(Restrictions.ge("testCaseList.testCaseCreatedDate", startDate));
			}
			if (endDate != null) {
				c.add(Restrictions.le("testCaseList.testCaseCreatedDate", endDate));
			}
			c.addOrder(Order.asc("testCaseId"));
            c.setFirstResult(startIndex);
            c.setMaxResults(pageSize);
            testCasesList = c.list();		
			
			if (!(testCasesList == null || testCasesList.isEmpty())){
				for (TestCaseList testCase : testCasesList) {
				Hibernate.initialize(testCase.getProductMaster());
				Hibernate.initialize(testCase.getProductFeature());
				Hibernate.initialize(testCase.getTestCaseStepsLists());
				Hibernate.initialize(testCase.getTestSuiteLists());
				Hibernate.initialize(testCase.getDecouplingCategory());
				Hibernate.initialize(testCase.getExecutionTypeMaster());
				Hibernate.initialize(testCase.getTestCasePriority());}
			}
			log.debug("list all successful");
		} catch (RuntimeException re) {
			log.error("list all failed", re);
			//throw re;
		}
		return testCasesList;
	}
	
	@Override
	@Transactional
	public List<WorkPackageTCEPResultSummaryDTO> getWPTestCaseExecutionResultSummary(Integer testCaseId, Integer workPackageId, Integer productId, String dataLevel) {
		log.debug("getWPTestCaseExecutionResultSummary testCaseId and workPackageId");
			List<WorkPackageTCEPResultSummaryDTO> wptcepResultList = null;
		try {
			
			String sql="SELECT pvlm.productVersionListId as productVersionId, "
					+ " pvlm.productVersionName as productVersionName,pb.productBuildId as productBuildId, pb.buildName as productBuildName,COUNT(tcres.result) AS totalResultCount,"
					+ "tcres.result AS result FROM workpackage_testcase_execution_plan AS wptcPlan "
					+ "INNER JOIN testcase_execution_result AS tcres ON wptcPlan.wptcepId=tcres.testCaseExecutionResultId "
					+ "INNER JOIN workpackage wp ON wptcPlan.workPackageId = wp.workPackageId "
					+ "INNER JOIN product_build pb ON pb.productBuildId=wp.productBuildId "
					+ "INNER JOIN product_version_list_master pvlm ON pb.productVersionId= pvlm.productVersionListId ";
			
			if(dataLevel.equals(IDPAConstants.EXECUTION_HISTORY_PRODUCT_LEVEL)){
				sql = sql +  "WHERE wptcPlan.testcaseId=:tcId  GROUP BY pvlm.productVersionListId ,pb.productBuildId, tcres.result";
				
				wptcepResultList=((SQLQuery) sessionFactory.getCurrentSession().createSQLQuery(sql).
						setParameter("tcId", testCaseId)).addScalar("productVersionId",StandardBasicTypes.INTEGER).addScalar("productBuildId",StandardBasicTypes.INTEGER).addScalar("totalResultCount", StandardBasicTypes.INTEGER).addScalar("result",StandardBasicTypes.STRING).addScalar("productVersionName",StandardBasicTypes.STRING).
						addScalar("productBuildName",StandardBasicTypes.STRING).setResultTransformer(Transformers.aliasToBean(WorkPackageTCEPResultSummaryDTO.class)).list();
			}else if(dataLevel.equalsIgnoreCase(IDPAConstants.EXECUTION_HISTORY_WORKPACKAGE_LEVEL)){
				sql = sql +  "WHERE wptcPlan.testcaseId=:tcId and wp.workPackageId=:wpId GROUP BY pvlm.productVersionListId ,pb.productBuildId, tcres.result";
				
				wptcepResultList=((SQLQuery) sessionFactory.getCurrentSession().createSQLQuery(sql).
						setParameter("tcId", testCaseId)).addScalar("productVersionId",StandardBasicTypes.INTEGER).addScalar("productBuildId",StandardBasicTypes.INTEGER).addScalar("totalResultCount", StandardBasicTypes.INTEGER).addScalar("result",StandardBasicTypes.STRING).addScalar("productVersionName",StandardBasicTypes.STRING).addScalar("productBuildName",StandardBasicTypes.STRING).
						setParameter("wpId", workPackageId).
						setResultTransformer(Transformers.aliasToBean(WorkPackageTCEPResultSummaryDTO.class)).list();
			}
			log.debug("WPTestCaseExecutionResult list :"+wptcepResultList.size());		
		} catch (RuntimeException re) {
			log.error("getWPTestCaseExecutionResultSummary testCaseId and workPackageId failed", re);	
		}
		return wptcepResultList;		
	}

	@Override
	@Transactional
	public List<Object[]> getUnMappedTestScriptsByProductId(int productId, int testCaseId, int jtStartIndex, int jtPageSize) {
		List<Object[]> unMappedTestScriptListObj = null;
		try {
	        String sql="select distinct scriptId, scriptName from test_case_scripts tcs where tcs.productId=:productId AND tcs.scriptId NOT IN(SELECT testScriptAssn.scriptId FROM testcase_script_has_test_case_list testScriptAssn WHERE testScriptAssn.testCaseId=:testCaseId)";
	        unMappedTestScriptListObj = sessionFactory.getCurrentSession().createSQLQuery(sql).
				setParameter("productId", productId).setParameter("testCaseId", testCaseId).list();
	    } catch (RuntimeException re) {
			log.error("ERROR getting TestScripts Unmapped to TestCases", re);	
		}
		return unMappedTestScriptListObj;
	}

	@Override
	@Transactional
	public List<Object[]> getMappedTestScriptsByTestCaseId(int testCaseId) {
		List<Object[]> mappedTestScriptListObj = null;
		try {
			
			String sql="select distinct scriptId, scriptName from test_case_scripts tcs where tcs.scriptId in(SELECT testScriptAssn.scriptId FROM testcase_script_has_test_case_list testScriptAssn WHERE testScriptAssn.testCaseId=:testCaseId)";
			mappedTestScriptListObj=sessionFactory.getCurrentSession().createSQLQuery(sql).
				setParameter("testCaseId", testCaseId).
					list();
		} catch (RuntimeException re) {
			log.error("ERROR getting Mapped TestScripts for TestCaseList", re);	
		}
		return mappedTestScriptListObj;
	}

	@Override
	public int getUnMappedTestScriptsCountByProductId(int productId, int testCaseId) {
		int totUnMappedTestScriptsCount = 0;
		try {
			totUnMappedTestScriptsCount=((Number) sessionFactory.getCurrentSession().createSQLQuery("select distinct count(*) from test_case_scripts tcs where tcs.productId=:productId AND tcs.scriptId NOT IN(SELECT testScriptAssn.scriptId FROM testcase_script_has_test_case_list testScriptAssn WHERE testScriptAssn.testCaseId=:testCaseId)").
				setParameter("testCaseId", testCaseId).
			    setParameter("productId", productId).uniqueResult()).intValue();
		} catch (RuntimeException e) {			
			log.error(e);
		}		
		return totUnMappedTestScriptsCount;
	}

	@Override
	public void updateTestCaseScriptToTestCase(Integer testCaseId, Integer scriptId, String maporunmap) {
		log.info("TestCase-TestCaseScript Association");
		EntityRelationship entityRelationship = null;
		try{
			
			if (scriptId!= null && testCaseId!= null){
				if(maporunmap.equalsIgnoreCase("map")){										
					entityRelationship = new EntityRelationship();
					entityRelationship.setEntityTypeId1(IDPAConstants.ENTITY_TEST_CASE_ID);
					entityRelationship.setEntityTypeId2(IDPAConstants.ENTITY_TEST_CASE_SCRIPT_ID);
					entityRelationship.setEntityInstanceId1(testCaseId);
					entityRelationship.setEntityInstanceId2(scriptId);
					entityRelationship.setIsActive(1);
					entityRelationshipService.addEntityRelationship(entityRelationship);
					
				}else if(maporunmap.equalsIgnoreCase("unmap")){	
					entityRelationship = entityRelationshipService.getEntityRelationshipByTypeAndInstanceIds(IDPAConstants.ENTITY_TEST_CASE_ID,IDPAConstants.ENTITY_TEST_CASE_SCRIPT_ID, testCaseId, scriptId);
					entityRelationshipService.deleteEntityRelationship(testCaseId, scriptId,IDPAConstants.ENTITY_TEST_CASE_ID,IDPAConstants.ENTITY_TEST_CASE_SCRIPT_ID);						
				}				
			}				
		} catch (RuntimeException re) {
			log.error("TestCase-TestCaseScript Association failed", re);				
		}
	}

	@Override
	@Transactional
	public int addBulkTestCases(List<TestCaseList> testCaseList, int batchSize) {
		log.info("Adding Test case steps in bulk");
		int count = 0;
		try {
			Session session = sessionFactory.getCurrentSession();
			if (batchSize <= 0)
				batchSize = 50;
			for (TestCaseList testCase : testCaseList ) {
				testCase.setStatus(1);
				session.save(testCase);
				log.info("TestCase data saved successfully");
				if (count++ % batchSize == 0 ) {
					session.flush();
					session.clear();
			    }
			}
			log.info("Bulk Add Successful");
		} catch (RuntimeException re) {
			log.error("Bulk Add failed", re);
		}
		return count;
	}
	
	@Override
	@Transactional
	public List<TestCaseScript> getMappedTestScriptsByProductIdAndTestcaseId(int productId, int testCaseId) {
		List<Object[]>  mappedTestScriptIds = null;
		List<TestCaseScript> testCaseScriptList=null;
		try {
			String sql="select distinct scriptId from test_case_scripts tcs where tcs.productId="+productId +" AND tcs.scriptId IN(SELECT testScriptAssn.scriptId FROM testcase_script_has_test_case_list testScriptAssn WHERE testScriptAssn.testCaseId="+testCaseId+")";
			mappedTestScriptIds=sessionFactory.getCurrentSession().createSQLQuery(sql).list();
			
			 List<Integer> testScriptIds = new ArrayList<Integer>();
			 if(mappedTestScriptIds != null && mappedTestScriptIds.size() >0) {
				for(Object testCaseScript:mappedTestScriptIds) {
					testScriptIds.add(Integer.parseInt(testCaseScript.toString()));
				}
			 }
			 if(testScriptIds != null && testScriptIds.size() >0) {
				 Criteria testCaseScriptCriteria = sessionFactory.getCurrentSession().createCriteria(TestCaseScript.class, "testCaseScript");
				 testCaseScriptCriteria.add(Restrictions.in("testCaseScript.scriptId", testScriptIds));
				 testCaseScriptList = testCaseScriptCriteria.list();
			 }
		} catch (RuntimeException e) {			
			log.error("Error while getMappedTestScriptsByProductIdAndTestcaseId",e);
		}		
		return testCaseScriptList;
	}
	
	
	@Override
	@Transactional
	public List<TestCaseList> listTescaseforPredecessors(Set<Integer> testCaseIds) {
		List<TestCaseList> testCaseList = new ArrayList<TestCaseList>();
		try {
			Criteria testcaseCriteria = sessionFactory.getCurrentSession().createCriteria(TestCaseList.class, "testCase");	
			testcaseCriteria.add(Restrictions.in("testCase.testCaseId", testCaseIds));
			testcaseCriteria.addOrder(Order.desc("testCase.testCaseId"));
			testCaseList = testcaseCriteria.list();
		}catch(Exception e) {
			log.error("Error in listTescaseforWorkFlowSLAIndicator",e);
		}
		return testCaseList;
	}

	@Override
	@Transactional
	public Date getLatestTestcaseCreatedDate(String tableName,String dateField) {
		try {
			
			String sql="select max("+dateField+") from "+tableName;
			List<Date> dateList=sessionFactory.getCurrentSession().createSQLQuery(sql).list();
			if(dateList != null && dateList.size() >0) {
				Date createdDate = dateList.get(0);
				return createdDate;
			}
		}catch(RuntimeException re) {
			
		}
		return null;
	}
	
	
	public TestCaseList getSimilarTestCasesByName(String testCaseName, int productId) {
		List<TestCaseList> list=null;
		TestCaseList tc=null;
		try {

			Criteria c = sessionFactory.getCurrentSession().createCriteria(TestCaseList.class, "tcl");
			c.createAlias("tcl.productMaster", "pm");
			//c.add(Restrictions.eq("tcl.testCaseName",testCaseName).ignoreCase());		
			c.add(Restrictions.eq("pm.productId", productId));
	
			list=c.list();
			for(TestCaseList tcl : list) {
				if(tcl.getTestCaseName().replaceAll("[^A-Za-z0-9]", "").equalsIgnoreCase(testCaseName)) {
					tc = tcl;
					break;
				}
			}		
		
		} catch (RuntimeException re) {
			log.error("Get Testcase by name failed", re);
		}
		return tc;
	}
	@Override
	@Transactional
	public TestCaseList getByTestCaseIdwithoutInitialization(int testCaseId){
		log.info("getByTestCaseIdwithoutInitialization : getting TestCaseList instance by id");
		TestCaseList testCaseList=null;
		try {
			List list =  sessionFactory.getCurrentSession().createQuery("from TestCaseList t where testCaseId=:testCaseId").setParameter("testCaseId", testCaseId)
					.list();
			testCaseList=(list!=null && list.size()!=0)?(TestCaseList)list.get(0):null;
			if (testCaseList != null) {
				if(testCaseList.getDecouplingCategory() != null){
					Hibernate.initialize(testCaseList.getDecouplingCategory());
					for (DecouplingCategory decoup : testCaseList.getDecouplingCategory()) {
						Hibernate.initialize(decoup.getProduct());
						Hibernate.initialize(decoup.getParentCategory());
						Hibernate.initialize(decoup.getUserTypeMasterNew());
					}
				}
				
				Hibernate.initialize(testCaseList.getProductMaster());
				if(testCaseList.getProductVersionList() != null && testCaseList.getProductVersionList().size() >0){
					Hibernate.initialize(testCaseList.getProductVersionList());
				}
				
				Hibernate.initialize(testCaseList.getProductFeature());
				Hibernate.initialize(testCaseList.getTestCaseStepsLists());
				Hibernate.initialize(testCaseList.getTestSuiteLists());
				Hibernate.initialize(testCaseList.getDecouplingCategory());
				Hibernate.initialize(testCaseList.getExecutionTypeMaster());
				Hibernate.initialize(testCaseList.getTestCasePriority());
				Hibernate.initialize(testCaseList.getTestcaseTypeMaster());	
				Hibernate.initialize(testCaseList.getTestRunPlanList());
				
				if(testCaseList.getTestRunPlanList() != null && testCaseList.getTestRunPlanList().size() >0) {
					for(TestRunPlan testRun:testCaseList.getTestRunPlanList()) {
						Hibernate.initialize(testRun.getProductBuild());
					}
				}
			}
			log.debug("getByTestCaseIdwithoutInitialization : getByTesCaseId successful");
		} catch (RuntimeException re) {
			log.error("getByTestCaseIdwithoutInitialization : getByTestCaseId failed", re);
		}
		return testCaseList;
        
	}

	
}	
