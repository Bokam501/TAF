package com.hcl.atf.taf.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.hcl.atf.taf.dao.TestCaseStepsListDAO;
import com.hcl.atf.taf.model.ProductMaster;
import com.hcl.atf.taf.model.TestCaseStepsList;
import com.hcl.atf.taf.model.TestExecutionResult;
import com.hcl.atf.taf.model.json.JsonTestExecutionResult;


@Repository
public class TestCaseStepsListDAOImpl implements TestCaseStepsListDAO {
	private static final Log log = LogFactory.getLog(TestCaseStepsListDAOImpl.class);
	
	@Autowired(required=true)
    private SessionFactory sessionFactory;
	
	@Override
	@Transactional
	public void delete(TestCaseStepsList testCaseStepsList) {
		log.debug("deleting TestCaseList instance");
		try {
			sessionFactory.getCurrentSession().delete(testCaseStepsList);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
		}		
		
	}

	@Override
	@Transactional
	public void add(TestCaseStepsList testCaseStepsList) {
		log.debug("adding TestCaseList instance");
		try {
			testCaseStepsList.setStatus(1);
			sessionFactory.getCurrentSession().save(testCaseStepsList);
			log.debug("add successful");
		} catch (RuntimeException re) {
			log.error("add failed", re);
		}
				
	}

	@Override
	@Transactional
	public void update(TestCaseStepsList testCaseStepsList) {
		log.debug("updating TestCaseList instance");
		try {
			sessionFactory.getCurrentSession().saveOrUpdate(testCaseStepsList);
			log.debug("update successful");
		} catch (RuntimeException re) {
			log.error("update failed", re);
		}
		
		
	}
    
@Override
@Transactional
public void updateTestCaseStepsListInImport(
		List<TestCaseStepsList> modifiedTestCaseStepsList,
		String tcAddOrUpdateAction, Integer maxBatchProcessingLimit) {
	log.info("updating TestCaseStepsList for New and Update");
	int count=0;
	for (TestCaseStepsList testCaseStepsList : modifiedTestCaseStepsList) {

		count++;
			if(tcAddOrUpdateAction.equalsIgnoreCase("Update")){
				updateTestCaseStepsList(testCaseStepsList,count,maxBatchProcessingLimit);			
				continue;
			}else if(tcAddOrUpdateAction.equalsIgnoreCase("New")){
				updateTestCaseStepsList(testCaseStepsList,count,maxBatchProcessingLimit);			
				if(count==maxBatchProcessingLimit){
					count=0;
				}
			}
	}
}

@Override
@Transactional
public TestCaseStepsList updateTestCaseStepsList(TestCaseStepsList testCaseStepsList,Integer count, Integer maxLimit) {
	log.debug("updating TestCaseStepsList instance");
	try {		
		sessionFactory.getCurrentSession().saveOrUpdate(testCaseStepsList);
		if (count % maxLimit == 0 ) {
			sessionFactory.getCurrentSession().flush();
			sessionFactory.getCurrentSession().clear();
	    } 
		
	} catch (RuntimeException re) {
		log.error("update failed", re);
	}
	return testCaseStepsList;
}

	@Override
	@Transactional
	public List<TestCaseStepsList> list() {
		log.debug("listing all TestCaseStepsList instance");
		List<TestCaseStepsList> testCaseStepsList=null;
		try {
			testCaseStepsList=sessionFactory.getCurrentSession().createQuery("from TestCaseStepsList").list();
			log.debug("list all successful");
		} catch (RuntimeException re) {
			log.error("list all failed", re);
		}
		return testCaseStepsList;
	}
	
	@Override
	@Transactional
    public List<TestCaseStepsList> list(int startIndex, int pageSize) {
		log.debug("listing TestCaseStepsList instance");
		List<TestCaseStepsList> testCaseStepsList=null;
		try {
			testCaseStepsList=sessionFactory.getCurrentSession().createQuery("from TestCaseStepsList")
	                .setFirstResult(startIndex)
	                .setMaxResults(pageSize)
	                .list();
			log.debug("list successful");
		} catch (RuntimeException re) {
			log.error("list failed", re);
		}
		return testCaseStepsList;       
    }
	
	@Override
	@Transactional
	public TestCaseStepsList getByTestCaseId(int testCaseId){
		log.debug("getting TestCaseList instance by id");
		TestCaseStepsList testCaseStepsList=null;
		try {
			List list = sessionFactory.getCurrentSession().createQuery("from TestCaseStepsList t where testCaseId=:testCaseId").setParameter("testCaseId",testCaseId)
					.list();
			testCaseStepsList=(list!=null && list.size()!=0)?(TestCaseStepsList)list.get(0):null;
			log.debug("getByTesCaseId successful");
		} catch (RuntimeException re) {
			log.error("getByTestCaseId failed", re);
		}
		return testCaseStepsList;
        
	}	
	
	@Override
	@Transactional
	public TestCaseStepsList getByTestStepId(int testStepId){
		log.debug("getting TestCaseList instance by id");
		TestCaseStepsList testCaseStepsList=null;
		try {
			List list = sessionFactory.getCurrentSession().createQuery("from TestCaseStepsList t where testStepId=:testCaseStepId").setParameter("testCaseStepId",testStepId)
					.list();
			testCaseStepsList=(list!=null && list.size()!=0)?(TestCaseStepsList)list.get(0):null;
			log.debug("getByTestStepId successful");
		} catch (RuntimeException re) {
			log.error("getByTestStepId failed", re);
		}
		return testCaseStepsList;
        
	}
	
	@Override
	@Transactional
	public TestCaseStepsList getByTestStepCode(String testStepCode, ProductMaster product){
		log.debug("getting TestCaseList instance by id");
		TestCaseStepsList testStep=null;
		try {
			Criteria criteria = sessionFactory.getCurrentSession().createCriteria(TestCaseStepsList.class, "testStep");
			criteria.createAlias("testStep.testCaseList", "testCase");
			criteria.createAlias("testCase.productMaster", "product");
			criteria.add(Restrictions.eq("testStep.testStepCode", testStepCode));
			List list=criteria.add(Restrictions.eq("product.productId", product.getProductId())).list();
			
			testStep=(list!=null && list.size()!=0)?(TestCaseStepsList)list.get(0):null;
			log.debug("getByTesCaseId successful");
		} catch (RuntimeException re) {
			log.error("getByTestCaseId failed", re);
		}
		return testStep;
	}


	@Override
	@Transactional
	public TestCaseStepsList getTestCaseStepByCodeProduct(String testStepCode,
			int productId) {		
		List<TestCaseStepsList> testStepList = null;
		TestCaseStepsList testStep = null;
		String hql = "from TestCaseStepsList tStep where testStepCode = :tstepCode and tStep.testCaseList.productMaster.productId = :productId";
		try {
			testStepList = sessionFactory.getCurrentSession().createQuery(hql)
					.setParameter("tstepCode", testStepCode)
					.setParameter("productId", productId).list();
			testStep = ((testStepList!=null && !testStepList.isEmpty())?testStepList.get(0):null);
			if(testStep!= null){
				if(testStep.getTestCaseList() != null){
					Hibernate.initialize(testStep.getTestCaseList());
					if(testStep.getTestCaseList().getProductMaster() != null)
						Hibernate.initialize(testStep.getTestCaseList().getProductMaster());
				}					
				
				if(testStep.getTestExecutionResults() != null)
				Hibernate.initialize(testStep.getTestExecutionResults());				
			}
			
		} catch (RuntimeException re) {
			log.error("Error in TestCaseList retrieval by Code and Product", re);
		}
		if (testStepList != null && !testStepList.isEmpty()) {
			return testStepList.get(0);
		} else
			return null;
	}

	@Override
	@Transactional
	public int getTotalRecords() {
		log.debug("getting TestCaseList total records");
		int count =0;
		try {
			count=((Number) sessionFactory.getCurrentSession().createSQLQuery("select count(*) from test_case_steps_list").uniqueResult()).intValue();
			
			log.debug("total records fetch successful");
		} catch (RuntimeException re) {
			log.error("total records fetch failed", re);			
		}
		return count;
	
	}

	@Override
	@Transactional
	public List<TestCaseStepsList> list(int testCaseId) {
		log.debug("listing specific test step instance");
		//
		List<TestCaseStepsList> testCaseStepsList=new ArrayList<TestCaseStepsList>();
		try {
			
			Criteria c = sessionFactory.getCurrentSession().createCriteria(TestCaseStepsList.class, "testCaseStep");
			c.createAlias("testCaseStep.testCaseList", "testCase");
			c.add(Restrictions.eq("testCase.testCaseId", testCaseId));
			c.addOrder(Order.asc("testCaseStep.testStepId"));
			testCaseStepsList = c.list();		

			if(testCaseStepsList!= null && !testCaseStepsList.isEmpty()){
				for(TestCaseStepsList testCaseStep : testCaseStepsList){
					Hibernate.initialize(testCaseStep.getTestCaseList());
				}
			}
			log.debug("list specific successful");
		//	
		} catch (RuntimeException re) {
			log.error("list specific failed", re);
		}
		return testCaseStepsList;
	}

	@Override
	@Transactional
	public List<TestCaseStepsList> list(int testCaseId, int startIndex, int pageSize) {
		log.debug("listing specific TestCaseStepsList instance");
		List<TestCaseStepsList> testCaseStepsList=null;
		try {
			testCaseStepsList=sessionFactory.getCurrentSession().createQuery("from TestCaseStepsList where testCaseId=:testCaseId")
														.setParameter("testCaseId", testCaseId).setFirstResult(startIndex)
														.setMaxResults(pageSize).list();
			log.debug("list specific successful");
		} catch (RuntimeException re) {
			log.error("list specific failed", re);
		}
		return testCaseStepsList;
	}

	@Override
	@Transactional
	public TestCaseStepsList getTestCaseStepsByCodeAndProduct(String testStepCode, int productId) {
		
		List<TestCaseStepsList> testCaseSteps = null;
		TestCaseStepsList testCaseStep = null;
		
		String hql = "from TestCaseStepsList c where testStepCode = :testStepCode and c.testCaseList.productMaster.productId=:productId";
		
		try {
			testCaseSteps = sessionFactory.getCurrentSession().createQuery(hql).setParameter("testStepCode", testStepCode).setParameter("productId", productId).list();
			testCaseStep = ((testCaseSteps!= null && !testCaseSteps.isEmpty())?testCaseSteps.get(0):null);
			if(testCaseStep!= null){
				Hibernate.initialize(testCaseStep.getTestCaseList());
				Hibernate.initialize(testCaseStep.getTestExecutionResults());
			}
		} catch (RuntimeException re) {
			log.error("Error in TestCaseList retrieval by Code, Name and Product", re);
		}
		
		return testCaseStep;
		
	}

	@Override
	@Transactional
	public TestCaseStepsList getTestCaseStepByTestStepCodeAndTestStepId(String testStepCode, Integer testStepId, int productId) {
		
		List<TestCaseStepsList> testCaseSteps = null;
		TestCaseStepsList testCaseStep = null;
		
		String hql = "from TestCaseStepsList c where (testStepCode = :testStepCode and c.testCaseList.productMaster.productId=:productId) or testStepId=:testStepId";
		
		try {
			testCaseSteps = sessionFactory.getCurrentSession().createQuery(hql)
									.setParameter("testStepCode", testStepCode)
									.setParameter("productId", productId)
									.setParameter("testStepId", testStepId).list();
			testCaseStep = ((testCaseSteps!= null && !testCaseSteps.isEmpty())?testCaseSteps.get(0):null);
			if(testCaseStep!= null){
				Hibernate.initialize(testCaseStep.getTestCaseList());
				Hibernate.initialize(testCaseStep.getTestExecutionResults());
			}
		} catch (RuntimeException re) {
			log.error("Error in TestCaseList retrieval by Code, Name and Product", re);
		}
		return testCaseStep;
	}

	@Override
	@Transactional
	public TestCaseStepsList getExistingTestStepForTER(TestExecutionResult testExecutionResult, JsonTestExecutionResult jsonTestExecutionResult, ProductMaster product) {
	
		List<TestCaseStepsList> testSteps = null;
		TestCaseStepsList testStep = null;
		String testStepCode = null;
		Integer testStepId = null;
		String testStepName = null;
		String testCaseName = null;
		Integer productId = null;
		
		testStepCode = (jsonTestExecutionResult.getTestStepCode() == null) ? "" : jsonTestExecutionResult.getTestStepCode();
		testCaseName = (testExecutionResult.getTestCase() == null) ? "" : testExecutionResult.getTestCase();
		testStepId = (jsonTestExecutionResult.getTestStepId() == null) ? -1 : jsonTestExecutionResult.getTestStepId();
		testStepName = (testExecutionResult.getTestStep() == null) ? "" : testExecutionResult.getTestStep();
		
		String hql = "from TestCaseStepsList ts where (testStepCode = :testStepCode or testStepId = :testStepId or testStepName = :testStepName) and productId = :productId and tc.testCaseList.testCaseName = :testCaseName";
		try {
			testSteps = sessionFactory.getCurrentSession().createQuery(hql)
					.setParameter("testStepCode", testStepCode)
					.setParameter("testStepId", testStepId)
					.setParameter("testStepName", testStepName)
					.setParameter("testCaseName", testCaseName)
					.setParameter("productId", product.getProductId()).list();
		} catch (RuntimeException re) {
			log.error("Error in TestCase retrieval for TER", re);
		}
		if (testSteps != null && !testSteps.isEmpty()) {
			return testSteps.get(0);
		} else
			return null;	
	
	}

	@Override
	public void update(TestCaseStepsList testCaseStepsList, Integer count) {
		log.debug("updating TestCaseList instance");
		try {
			sessionFactory.getCurrentSession().saveOrUpdate(testCaseStepsList);
			if (count % 20 == 0 ) {
				sessionFactory.getCurrentSession().flush();
				sessionFactory.getCurrentSession().clear();
		    }
			log.debug("update successful");
		} catch (RuntimeException re) {
			log.error("update failed", re);
		}
	}
		
	@Override
	public int addBulk(List<TestCaseStepsList> testCaseSteps, Integer batchSize) {
		log.info("Adding Test case steps in bulk");
		int count = 0;
		try {
			//Get the session. Do not get the stateless session as relationship to testcase needs to be added for the test step.
			Session session = sessionFactory.openSession();
			Transaction tx = session.beginTransaction();
			if (batchSize <= 0)
				batchSize = 50;
			for (TestCaseStepsList testStep : testCaseSteps ) {
				testStep.setStatus(1);
				session.save(testStep);
				log.info("Test Step data saved successfully");
				if (count++ % batchSize == 0 ) {
					session.flush();
					session.clear();
			    }
			}
			tx.commit();
			session.close();
			log.info("Bulk Add Successful");
		} catch (RuntimeException re) {
			log.error("Bulk Add failed", re);
		}
		return count;
	}

	@Override
	public List<TestCaseStepsList> getTestCaseStepsByProductId(
			int productId) {
		List<TestCaseStepsList> testCaseSteps = null;
		try {
			testCaseSteps = sessionFactory
					.getCurrentSession()
					.createQuery(
							"from TestCaseStepsList c where c.testCaseList.productMaster.productId=:productId")
					.setParameter("productId", productId).list();
		
			
		} catch (RuntimeException re) {
			log.error("Error in TestCaseList retrieval by Code, Name and Product", re);
		}
		
		return testCaseSteps;
	}

	@Override
	@Transactional
	public TestCaseStepsList getByTestStepName(String testStepName) {
		log.debug("getting Test step instance by Name");
		TestCaseStepsList testCaseStepsList=null;
		try {
			List list = sessionFactory.getCurrentSession().createQuery("from TestCaseStepsList t where testStepName=:testCaseStepName").setParameter("testCaseStepName",testStepName)
					.list();
			testCaseStepsList=(list!=null && list.size()!=0)?(TestCaseStepsList)list.get(0):null;
			log.debug("getByTestStepName successful");
		} catch (RuntimeException re) {
			log.error("getByTestStepName failed", re);
		}
		return testCaseStepsList;
	}
	
	@Override
	@Transactional
	public List<TestCaseStepsList> getTestStepListByStatus(int testCaseId,Integer status) {
		log.debug("listing specific test step instance");
		//
		List<TestCaseStepsList> testCaseStepsList=new ArrayList<TestCaseStepsList>();
		try {
			
			Criteria c = sessionFactory.getCurrentSession().createCriteria(TestCaseStepsList.class, "testCaseStep");
			c.createAlias("testCaseStep.testCaseList", "testCase");
			c.add(Restrictions.eq("testCase.testCaseId", testCaseId));
			if( status != null && status != 2) {
				c.add(Restrictions.eq("testCaseStep.status", status));
			}
			c.addOrder(Order.asc("testCaseStep.testStepId"));
			testCaseStepsList = c.list();		

			if(testCaseStepsList!= null && !testCaseStepsList.isEmpty()){
				for(TestCaseStepsList testCaseStep : testCaseStepsList){
					Hibernate.initialize(testCaseStep.getTestCaseList());
				}
			}
			log.debug("list specific successful");
		//	
		} catch (RuntimeException re) {
			log.error("list specific failed", re);
		}
		return testCaseStepsList;
	}

	@Override
	@Transactional
	public int getTotalRecordsOfTestCaseStepsByTestCaseId(int testCaseId) {
		log.info("getting TestStepList total records");
		int count =0;
		try {
			List list= sessionFactory.getCurrentSession().createSQLQuery("select * from test_case_steps_list where testCaseId=:testCaseId").setParameter("testCaseId",testCaseId).list();
			count = (list!=null && list.size()!=0)? list.size() : 0;
			log.info("total records fetch successful");
		} catch (RuntimeException re) {
			log.error("total records fetch failed", re);			
		}
		return count;
	}
}
