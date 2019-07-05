/**
 * 
 */
package com.hcl.ilcm.workflow.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.hcl.atf.taf.model.TestCaseList;
import com.hcl.ilcm.workflow.dao.WorkflowTestcasesDAO;

/**
 * @author silambarasur
 *
 */
@Repository
public class WorkflowTestcasesDAOImpl implements WorkflowTestcasesDAO {
	
	private static final Log log = LogFactory.getLog(WorkflowTestcasesDAOImpl.class);
	
	@Autowired(required=true)
	private SessionFactory sessionFactory;
	
	@Override
	@Transactional
	public List<TestCaseList> listActivityTestcasesforWorkFlow(Integer productId,List<String> testCaseStatusList,Integer jtStartIndex, Integer jtPageSize) {
			log.debug("listing all Test Case List instance");
			List<TestCaseList> testCaseList = new ArrayList<TestCaseList>();
			try {
				Criteria testcaseCriteria = sessionFactory.getCurrentSession().createCriteria(TestCaseList.class, "testCase");	
				testcaseCriteria.createAlias("testCase.workflowStatus", "status");
				testcaseCriteria.createAlias("testCase.productMaster", "product");
				
				
				if(testCaseStatusList!=null && testCaseStatusList.size() >0) {
					testcaseCriteria.add(Restrictions.in("status.workflowStatusName", testCaseStatusList));
					testcaseCriteria.add(Restrictions.eq("product.productId", productId));
					testcaseCriteria.addOrder(Order.desc("testCaseId"));
					testcaseCriteria.setFirstResult(jtStartIndex);
					testcaseCriteria.setMaxResults(jtPageSize);
					testCaseList = testcaseCriteria.list();
				}
				
			log.debug("list testcase successful");
		} catch (RuntimeException re) {
			log.error("list testcase failed", re);
			//throw re;
		}
		return testCaseList;
	}

	@Override
	@Transactional
	public List<TestCaseList> listActivityTestcasesforWorkFlowForUserOrRole(Integer productId, Integer workflowId, Integer entityTypeId, Integer entityId, Integer userId, Integer roleId, Integer jtStartIndex, Integer jtPageSize) {

		log.info("listing all test case List instance : productId - "+productId+", workflowId - "+workflowId+", entityTypeId - "+entityTypeId+", userId - "+userId+", roleId - "+roleId);
		List<TestCaseList> testCaseList = new ArrayList<TestCaseList>();
		try {
			
			String testcasesForUserSQL = "SELECT DISTINCT entityInstanceId from wf_workflow_status_actors_users_view"
			+ " WHERE "
			+ " productId = " + productId
			+ " AND entityTypeId = " + entityTypeId;
			if(entityId != null){
				testcasesForUserSQL += " AND entityId = " + entityId;
			}
			if(workflowId != null){
				testcasesForUserSQL += " AND workflowId = " + workflowId;
			}
			testcasesForUserSQL += "	AND workflowStatusId = currentStatusId"
			+ "	AND currentStatusId > 0"
			+ " AND workflowStatusType <> 'End'"
			+ "	AND ("
			+ 	" (userId = " + userId 
			+	" AND (actionScope = 'User' OR actionScope = 'Role or User')" 
			+ 	" AND userActionStatus <> 'Completed')" 
			+ 	" OR"
			+   " (roleId = " + roleId
			+ 	" AND (actionScope = 'Role' OR actionScope = 'Role or User'))"
			+ 	")";
			
			List<Integer> testCaseIds = sessionFactory.getCurrentSession().createSQLQuery(testcasesForUserSQL).list();
			if (testCaseIds == null || testCaseIds.isEmpty()) {
				return null;
			}
			
			Criteria testcaseCriteria = sessionFactory.getCurrentSession().createCriteria(TestCaseList.class, "testCase");	
			testcaseCriteria.add(Restrictions.in("testCase.testCaseId", testCaseIds));
			testcaseCriteria.addOrder(Order.desc("testCase.testCaseId"));
			testcaseCriteria.setFirstResult(jtStartIndex);
			testcaseCriteria.setMaxResults(jtPageSize);
			testCaseList = testcaseCriteria.list();
				
			log.debug("list testcase successful");
		} catch (RuntimeException re) {
			log.error("list testcase failed", re);
		}
		return testCaseList;
	}
	
	@Override
	@Transactional
	public List<TestCaseList> listTescaseforWorkFlowSLAIndicator(Integer productId,Integer entityTypeId,List<Integer> testCaseIds,Integer jtStartIndex, Integer jtPageSize) {
		List<TestCaseList> testCaseList = new ArrayList<TestCaseList>();
		try {
			Criteria testcaseCriteria = sessionFactory.getCurrentSession().createCriteria(TestCaseList.class, "testCase");	
			testcaseCriteria.add(Restrictions.in("testCase.testCaseId", testCaseIds));
			testcaseCriteria.addOrder(Order.desc("testCase.testCaseId"));
			testcaseCriteria.setFirstResult(jtStartIndex);
			testcaseCriteria.setMaxResults(jtPageSize);
			testCaseList = testcaseCriteria.list();
		}catch(Exception e) {
			log.error("Error in listTescaseforWorkFlowSLAIndicator",e);
		}
		return testCaseList;
	}

}
