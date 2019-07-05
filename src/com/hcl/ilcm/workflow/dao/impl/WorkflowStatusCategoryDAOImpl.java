/**
 * 
 */
package com.hcl.ilcm.workflow.dao.impl;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.hcl.atf.taf.model.WorkflowStatusCategory;
import com.hcl.ilcm.workflow.dao.WorkflowStatusCategoryDAO;
import com.hcl.ilcm.workflow.model.WorkflowMaster;

/**
 * @author silambarasur
 * 
 */
@Repository
public class WorkflowStatusCategoryDAOImpl implements WorkflowStatusCategoryDAO{

	private static final Log log = LogFactory.getLog(WorkflowStatusCategoryDAOImpl.class);

	@Autowired(required = true)
	private SessionFactory sessionFactory;

	@Override
	@Transactional
	public void addWorkflowStatusCategory(WorkflowStatusCategory workflowStatusCategory, Integer workflowId) {
		try {
			WorkflowMaster workflowMaster = new WorkflowMaster();
			workflowMaster.setWorkflowId(workflowId);
			workflowStatusCategory.setWorkflow(workflowMaster);
			sessionFactory.getCurrentSession().save(workflowStatusCategory);
		} catch (Exception e) {
			log.error("Error in save addWorkflowStatusCategory", e);
		}

	}

	@Override
	@Transactional
	public void updateWorkflowStatusCategory(WorkflowStatusCategory workflowStatusCategory,Integer workflowId) {
		try {
			WorkflowMaster workflowMaster = new WorkflowMaster();
			workflowMaster.setWorkflowId(workflowId);
			workflowStatusCategory.setWorkflow(workflowMaster);
			sessionFactory.getCurrentSession().saveOrUpdate(workflowStatusCategory);
		} catch (Exception e) {
			log.error("Error in save updateWorkflowStatusCategory", e);
		}
	}
	
	@Override
	@Transactional
	public List<WorkflowStatusCategory> getWorkflowStatusCategoryList(Integer workflowId) {
		List<WorkflowStatusCategory> workflowStatusCategories = null;
		try {
			
			 Criteria workflowStatusCategoryCriteria = sessionFactory.getCurrentSession().createCriteria(WorkflowStatusCategory.class, "workflowStatusCategory");
			 workflowStatusCategoryCriteria.createAlias("workflowStatusCategory.workflow", "workflow");
			 workflowStatusCategoryCriteria.add(Restrictions.eq("workflow.workflowId", workflowId));
			 workflowStatusCategories = workflowStatusCategoryCriteria.list();
		}catch(Exception e) {
			log.error("Error in fetch getWorkflowStatusCategoryList records", e);
		}
		return workflowStatusCategories;
	}
	
	@Override
	@Transactional
	public List<WorkflowStatusCategory> getWorkflowStatusCategoryByWorkflowId(Integer workflowId, Integer isActive) {
		List<WorkflowStatusCategory> workflowStatusCategories = null;
		try {
			 Criteria workflowStatusCategoryCriteria = sessionFactory.getCurrentSession().createCriteria(WorkflowStatusCategory.class, "workflowStatusCategory");
			 workflowStatusCategoryCriteria.createAlias("workflowStatusCategory.workflow", "workflow");
			 workflowStatusCategoryCriteria.add(Restrictions.eq("workflow.workflowId", workflowId));
			 workflowStatusCategoryCriteria.add(Restrictions.eq("workflowStatusCategory.activeStatus", isActive));
			 workflowStatusCategories = workflowStatusCategoryCriteria.list();
		}catch(Exception e) {
			log.error("Error in fetch getWorkflowStatusCategoryByWorkflowId records", e);
		}
		
		return workflowStatusCategories;
	}

}
