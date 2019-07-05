/**
 * 
 */
package com.hcl.ilcm.workflow.service;

import java.util.List;

import com.hcl.atf.taf.model.WorkflowStatusCategory;

/**
 * @author silambarasur
 *
 */
public interface WorkflowStatusCategoryService {
	void addWorkflowStatusCategory(WorkflowStatusCategory workflowStatusCategory, Integer workflowId);
	void updateWorkflowStatusCategory(WorkflowStatusCategory workflowStatusCategory, Integer workflowId);
	List<WorkflowStatusCategory> getWorkflowStatusCategoryByWorkflowId(Integer workflowId,Integer isActive);
	List<WorkflowStatusCategory> getWorkflowStatusCategoryList(Integer workflowId);

}
