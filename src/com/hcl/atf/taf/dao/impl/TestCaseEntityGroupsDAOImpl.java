/**
 * 
 */
package com.hcl.atf.taf.dao.impl;

import java.util.List;

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

import com.hcl.atf.taf.dao.TestCaseEntityGroupsDAO;
import com.hcl.atf.taf.model.TestCaseEntityGroup;
import com.hcl.atf.taf.model.TestCaseEntityGroupHasTestCase;

/**
 * @author silambarasur
 *
 */
@Repository
public class TestCaseEntityGroupsDAOImpl implements TestCaseEntityGroupsDAO{
	
	private static final Log log = LogFactory.getLog(TestCaseEntityGroupsDAOImpl.class);
	
	@Autowired
	private SessionFactory sessionFactory;
	
	@Override
	@Transactional
	public void addTestCaseEntityGroup(TestCaseEntityGroup testCaseEntityGroups) {
		try {
			
			
			//There will be only one root node 
			List<TestCaseEntityGroup> testCaseEntityGroupList =getAllTestCaseEntityGroups(testCaseEntityGroups.getProduct().getProductId());
			TestCaseEntityGroup rootTestCaseEntityGroup = null;
			if(testCaseEntityGroupList == null){
				//Some problem in finding out Test Case Entity Group size. Will lead to inconsistent state
				//Abort adding.
				log.error("Add failed as could not find Test case Entity Group objects");
				return;
			}else if(testCaseEntityGroupList.size() <= 0){

				//There are no TestcaseEntityGroup in the DB. Seed the main root node
				rootTestCaseEntityGroup = new TestCaseEntityGroup();
				rootTestCaseEntityGroup.setTestCaseEntityGroupName("---");
				rootTestCaseEntityGroup.setDescription("Root Test Entity Senario");
				
				sessionFactory.getCurrentSession().saveOrUpdate(rootTestCaseEntityGroup);
			}
			
			if(testCaseEntityGroups.getParentEntityGroupId() == null || testCaseEntityGroups.getParentEntityGroupId().getTestCaseEntityGroupId() == 0){
				//If no parent category is specified, make the root category the parent category
				rootTestCaseEntityGroup = getTestCaseEntityGroupByName("---");
				//TODO : Get the SK without parent. This will be the root SK. There will be only one root SK,
				//one without a parent SK
				testCaseEntityGroups.setParentEntityGroupId(rootTestCaseEntityGroup);
			}
			
			TestCaseEntityGroup parentTestCaseEntityGroups = testCaseEntityGroups.getParentEntityGroupId();
			if(parentTestCaseEntityGroups != null){
				if(parentTestCaseEntityGroups.getTestCaseEntityGroupId() != null){
					parentTestCaseEntityGroups = getTestCaseEntityGrouById(parentTestCaseEntityGroups.getTestCaseEntityGroupId());
					testCaseEntityGroups.setParentEntityGroupId(parentTestCaseEntityGroups);
				}
				sessionFactory.getCurrentSession().saveOrUpdate(testCaseEntityGroups);
					
			}
			log.debug("add successful");
		}catch(RuntimeException re) {
			log.error("Error in Adding test case entity Groups",re);
		}
		
	}
	
	@Override
	@Transactional
	public void updateTestCaseEntityGroup(TestCaseEntityGroup testCaseEntityGroups) {
		try {
			sessionFactory.getCurrentSession().saveOrUpdate(testCaseEntityGroups);
		}catch(RuntimeException re) {
			log.error("Error in Adding test case entity Groups");
		}
		
	}
	
	@Override
	@Transactional
	public List<TestCaseEntityGroup> getAllTestCaseEntityGroups(Integer productId) {
		List<TestCaseEntityGroup> testCaseEntityGroupList=null;
		try {
			Criteria c=sessionFactory.getCurrentSession().createCriteria(TestCaseEntityGroup.class,"testCaseEntityGroup");
			c.createAlias("testCaseEntityGroup.product", "product");
			c.add(Restrictions.eq("product.productId", productId));
			testCaseEntityGroupList=c.list();
			for (TestCaseEntityGroup testCaseEntityGroup : testCaseEntityGroupList) {
				Hibernate.initialize(testCaseEntityGroup.getParentEntityGroupId());			
			}
			
		}catch(RuntimeException re) {
			
		}
		return testCaseEntityGroupList;
	}

	@Override
	@Transactional
	public List<TestCaseEntityGroup> getTestCaseEntityGroupsByParentEntityId(Integer parentEntityId) {
		List<TestCaseEntityGroup> testCaseEntityGroupList=null;
		try {
			Criteria c=sessionFactory.getCurrentSession().createCriteria(TestCaseEntityGroup.class,"testCaseEntityGroup");
			c.createAlias("testCaseEntityGroup.parentEntityGroupId", "parentEntityGroup");
			c.add(Restrictions.eq("parentEntityGroup.parentEntityGroupId", parentEntityId));
			testCaseEntityGroupList=c.list();
		}catch(RuntimeException re) {
			
		}
		return testCaseEntityGroupList;
	}
	
	@Override
	@Transactional
	public TestCaseEntityGroup getTestCaseEntityGrouById(Integer testCaseEntityGroupId) {
		List<TestCaseEntityGroup> testCaseEntityGroupList=null;
		TestCaseEntityGroup testCaseEntityGroups= null;
		try {
			Criteria c=sessionFactory.getCurrentSession().createCriteria(TestCaseEntityGroup.class,"testCaseEntityGroup");
			c.add(Restrictions.eq("testCaseEntityGroup.testCaseEntityGroupId", testCaseEntityGroupId));
			testCaseEntityGroupList=c.list();
			if(testCaseEntityGroupList !=null && testCaseEntityGroupList.size() >0) {
				testCaseEntityGroups=testCaseEntityGroupList.get(0);
			}
		}catch(RuntimeException re) {
			log.error("Error in ");
		}
		return testCaseEntityGroups;
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
	

	@Override
	@Transactional
	public TestCaseEntityGroup getTestCaseEntityGroupByName(String testCaseEntityGroupName) {
		log.debug("listing specific Test case Entity Group instance by Name");
		TestCaseEntityGroup testCaseEntityGroup=null;
		try {
			Criteria c = sessionFactory.getCurrentSession().createCriteria(TestCaseEntityGroup.class, "testCaseEntityGroup");
			c.add(Restrictions.like("testCaseEntityGroup.testCaseEntityGroupName", testCaseEntityGroupName));
			
			List<TestCaseEntityGroup> testCaseEntityGroupList = c.list();	
			for (TestCaseEntityGroup testCaseEntityGroups : testCaseEntityGroupList) {
				Hibernate.initialize(testCaseEntityGroups.getParentEntityGroupId());			
			}
			testCaseEntityGroup=(testCaseEntityGroupList!=null && testCaseEntityGroupList.size()!=0)?(TestCaseEntityGroup)testCaseEntityGroupList.get(0):null;
			
			log.debug("list specific successful");
		} catch (RuntimeException re) {
			log.error("list specific failed", re);
		}
		return testCaseEntityGroup;
	}

	@Override
	public List<TestCaseEntityGroup> getTestCaseEntityGroupListExcludingChildByparentTestCaseEntityGroupId(Integer productId,Integer testCaseEntityGroupId) {

		log.debug("listing all TestCase Entity Excluding Child testCase Entity Groups");
		List<TestCaseEntityGroup> testCaseEntityGroupList = null;
		try{

			TestCaseEntityGroup testCaseEntityGroup = getTestCaseEntityGrouById(testCaseEntityGroupId);
			
			Criteria c = sessionFactory.getCurrentSession().createCriteria(TestCaseEntityGroup.class, "testCaseEntityGroup");
			c.add(Restrictions.not(Restrictions.between("leftIndex", testCaseEntityGroup.getLeftIndex(), testCaseEntityGroup.getRightIndex())));
			c.addOrder(Order.asc("leftIndex"));
			testCaseEntityGroupList = c.list();	
			log.info("For TestCase Entity Group ID :"+testCaseEntityGroupId);
			log.info("Below are list of ancestors");
			if(!(testCaseEntityGroupList == null || testCaseEntityGroupList.isEmpty())){
				for(TestCaseEntityGroup testCaseEntityGp : testCaseEntityGroupList){
					if(testCaseEntityGp.getParentEntityGroupId() != null){
						Hibernate.initialize(testCaseEntityGp.getParentEntityGroupId());
					}				
						log.info(testCaseEntityGp.getTestCaseEntityGroupName());
				}
			}
			log.debug("list all successful");			
		}catch(RuntimeException re){
			log.error("list all failed", re);
		}
		return testCaseEntityGroupList;
	
	}
	
	@Override
	@Transactional
	public TestCaseEntityGroup getRootTestCaseEntityGroup() {
		log.debug("Getting Root TestCaseEntityGroup instance");
		TestCaseEntityGroup rootPF = null;
		try {

			rootPF = getTestCaseEntityGroupByName("TestCaseEntityGroups");
		} catch (RuntimeException re) {
			log.error("getRootTestCaseEntityGroup failed", re);
		}
		return rootPF;
	}

	@Override
	@Transactional
	public List<Object[]> getUnMappedTestCasesByProductId(int productId, int testScenarioId, int jtStartIndex, int jtPageSize) {
		List<Object[]> unMappedTestCaseListObj = null;
		try {
	        String sql="select distinct testCaseId, testCaseName from test_case_list tcl where tcl.productId=:productId AND tcl.testCaseId NOT IN(SELECT testCaseGroupAssn.testCaseId FROM testcase_entitygroup_has_test_case_list testCaseGroupAssn WHERE testCaseGroupAssn.testCaseEntityGroupId=:testCaseEntityGroupId)";
	        unMappedTestCaseListObj = sessionFactory.getCurrentSession().createSQLQuery(sql).
				setParameter("productId", productId).setParameter("testCaseEntityGroupId", testScenarioId).list();
	    } catch (RuntimeException re) {
			log.error("ERROR getting Unmapped TestCases for TestScenario", re);	
		}
		return unMappedTestCaseListObj;
	}

	@Override
	@Transactional
	public List<Object[]> getMappedTestCasesByTestScenarioId(int testScenarioId) {
		List<Object[]> mappedTestCaseListObj = null;
		try {
			
			String sql="select distinct testCaseId, testCaseName from test_case_list tcl where tcl.testCaseId in(SELECT testCaseGroupAssn.testCaseId FROM testcase_entitygroup_has_test_case_list testCaseGroupAssn WHERE testCaseGroupAssn.testCaseEntityGroupId=:testCaseEntityGroupId)";
			mappedTestCaseListObj=sessionFactory.getCurrentSession().createSQLQuery(sql).
				setParameter("testCaseEntityGroupId", testScenarioId).
					list();
		} catch (RuntimeException re) {
			log.error("ERROR getting Mapped TestCases for TestScenario", re);	
		}
		return mappedTestCaseListObj;
	}

	@Override
	@Transactional
	public int getUnMappedTestCasesCountByProductId(int productId, int testScenarioId) {
		int totUnMappedTestCasesCount = 0;
		try {
			totUnMappedTestCasesCount=((Number) sessionFactory.getCurrentSession().createSQLQuery("select distinct count(*) from test_case_list tcl where tcl.productId=:productId AND tcl.testCaseId NOT IN(SELECT testCaseGroupAssn.testCaseId FROM testcase_entitygroup_has_test_case_list testCaseGroupAssn WHERE testCaseGroupAssn.testCaseEntityGroupId=:testCaseEntityGroupId)").
				setParameter("testCaseEntityGroupId", testScenarioId).
			    setParameter("productId", productId).uniqueResult()).intValue();
		} catch (RuntimeException e) {			
			log.error(e);
		}		
		return totUnMappedTestCasesCount;
	}

	@Override
	@Transactional
	public void updateTestCaseToTestScenario(TestCaseEntityGroupHasTestCase testCaseEntityGroupHasTestCase, String maporunmap) {
		log.info("TestCase-TestScenario Association");
		try{			
				if(maporunmap.equalsIgnoreCase("map")){										
					addTestScenarioAssociation(testCaseEntityGroupHasTestCase);					
				}else if(maporunmap.equalsIgnoreCase("unmap")){	
					deleteTestScenarioAssociation(testCaseEntityGroupHasTestCase);
				}				
		} catch (RuntimeException re) {
			log.error("TestCase-TestScenario Association failed", re);				
		}
	}

	@Override
	@Transactional
	public TestCaseEntityGroupHasTestCase getTestCaseScriptAssociationByIds(Integer testScenarioId, Integer testCaseId) {
		log.info("getTestCaseScriptAssociationByIds");
		TestCaseEntityGroupHasTestCase testCaseEntityGroupHasTestCase = null;
		try {
			Criteria criteria = sessionFactory.getCurrentSession().createCriteria(TestCaseEntityGroupHasTestCase.class,"testScenarioAssociation");
			List list = criteria.add(Restrictions.eq("testScenarioAssociation.testCaseEntityGroupId", testScenarioId)).
							add(Restrictions.eq("testScenarioAssociation.testCaseId", testCaseId)).list();
			testCaseEntityGroupHasTestCase =  (list.size() != 0 && list != null)?(TestCaseEntityGroupHasTestCase)list.get(0):null;
		} catch (RuntimeException re) {
			log.error("Error in getTestCaseScriptAssociationByIds", re);
		}
		return testCaseEntityGroupHasTestCase;
	}
	
	@Override
	@Transactional
	public Integer addTestScenarioAssociation(TestCaseEntityGroupHasTestCase testCaseEntityGroupHasTestCase) {
		log.info("adding TestScenarioAssociation");
		try {	
			sessionFactory.getCurrentSession().save(testCaseEntityGroupHasTestCase);
			log.info("add TestScenarioAssociation successful");
		} catch (RuntimeException re) {
			log.error("Error in addTestScenarioAssociation", re);
		}
		return testCaseEntityGroupHasTestCase.getTestCaseEntityGroupId();
	}	
	
	@Override
	@Transactional
	public void deleteTestScenarioAssociation(TestCaseEntityGroupHasTestCase testCaseEntityGroupHasTestCase) {
		try {
			String query = "Delete from TestCaseEntityGroupHasTestCase where testCaseEntityGroupId=:testCaseEntityGroupId and testCaseId=:testCaseId";
			sessionFactory.getCurrentSession().createQuery(query).setInteger("testCaseEntityGroupId", testCaseEntityGroupHasTestCase.getTestCaseEntityGroupId()).
			setInteger("testCaseId", testCaseEntityGroupHasTestCase.getTestCaseId()).executeUpdate();
			log.debug("deleteTestScenarioAssociation successful");
		} catch (RuntimeException re) {
			log.error("Error in TestScenarioAssociation", re);
		}
	}
	
	
	@Override
	@Transactional
	public List<TestCaseEntityGroupHasTestCase> getMappedTestCaseAssociationByTestcaseScenarioId(Integer testcaseSenarioId) {
		 List<TestCaseEntityGroupHasTestCase> testcaseScenarioAssociationList= null;
		try {	
			Criteria criteria = sessionFactory.getCurrentSession().createCriteria(TestCaseEntityGroupHasTestCase.class,"testScenarioAssociation");
			criteria.add(Restrictions.eq("testScenarioAssociation.testCaseEntityGroupId", testcaseSenarioId));
			testcaseScenarioAssociationList=criteria.list();
		} catch (RuntimeException re) {
			log.error("Error in addTestScenarioAssociation", re);
			
		}
		return testcaseScenarioAssociationList;
	}	
	
}
