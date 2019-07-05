package com.hcl.ilcm.workflow.service;

import java.util.List;

import com.hcl.ilcm.workflow.model.WorkflowMaster;
import com.hcl.ilcm.workflow.model.WorkflowMasterEntityMapping;

public interface WorkflowMasterService {

	WorkflowMaster getWorkflowForEntityTypeOrInstance(Integer productId, Integer entityTypeId, Integer entityId, Integer entityInstanceId, Integer activeState);
	List<WorkflowMaster> getWorkflowMasterList();
	void addWorkflowMaster(WorkflowMaster workflowMaster);
	void updateWorkflowMaster(WorkflowMaster workflowMaster);
	List<WorkflowMaster> getWorkflowMasterMappedToEntityList(Integer productId, Integer entityTypeId, Integer entityId, Integer activeState);
	List<WorkflowMaster> getWorkflowMasterListByType(String workflowType);
	List<WorkflowMaster> getWorkFlowMasterListByWorkflowName(String  WorkflowName);
	List<WorkflowMasterEntityMapping> getEntitiesAndInstanceMappedWithWorkflow(Integer workflowId);
	Boolean isWorkflowAleadyExist(Integer referenceWorkflowId, String workflowName);
	List<WorkflowMaster> getWorkflowMasterListByStatus(Integer isActive);
}
