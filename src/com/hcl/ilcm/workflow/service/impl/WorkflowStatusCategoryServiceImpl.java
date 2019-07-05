/**
 * 
 */
package com.hcl.ilcm.workflow.service.impl;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hcl.atf.taf.model.WorkflowStatusCategory;
import com.hcl.ilcm.workflow.dao.WorkflowStatusCategoryDAO;
import com.hcl.ilcm.workflow.service.WorkflowStatusCategoryService;

/**
 * @author silambarasur
 *
 */
@Service
public class WorkflowStatusCategoryServiceImpl implements WorkflowStatusCategoryService {
	private static final Log log = LogFactory.getLog(WorkflowStatusCategoryServiceImpl.class);
	
	@Autowired
	WorkflowStatusCategoryDAO workflowStatusCategoryDAO;
	
	@Override
	@Transactional
	public void addWorkflowStatusCategory(WorkflowStatusCategory workflowStatusCategory, Integer workflowId) {
		workflowStatusCategoryDAO.addWorkflowStatusCategory(workflowStatusCategory, workflowId);
	}
	
	@Override
	@Transactional
	public void updateWorkflowStatusCategory(WorkflowStatusCategory workflowStatusCategory,Integer workflowId) {
		workflowStatusCategoryDAO.updateWorkflowStatusCategory(workflowStatusCategory, workflowId);
		
	}
	@Override
	@Transactional
	public List<WorkflowStatusCategory> getWorkflowStatusCategoryList(Integer workflowId) {
		try {
		return workflowStatusCategoryDAO.getWorkflowStatusCategoryList(workflowId);
		}catch(Exception e) {
			log.error("Error in Service method for getWorkflowStatusCategoryList",e);
		}
		return null;
	}
	@Override
	@Transactional
	public List<WorkflowStatusCategory> getWorkflowStatusCategoryByWorkflowId(Integer workflowId,Integer isActive) {
		try {
			return workflowStatusCategoryDAO.getWorkflowStatusCategoryByWorkflowId(workflowId,isActive);
		}catch(Exception e) {
			
		}
		return null;
	}

}
