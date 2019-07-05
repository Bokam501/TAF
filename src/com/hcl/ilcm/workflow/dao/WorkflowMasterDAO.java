package com.hcl.ilcm.workflow.dao;

import java.util.List;

import com.hcl.ilcm.workflow.model.WorkflowMaster;
import com.hcl.ilcm.workflow.model.WorkflowMasterEntityMapping;

public interface WorkflowMasterDAO {

	WorkflowMaster getWorkflowForEntityTypeOrInstance(Integer productId, Integer entityTypeId, Integer entityId, Integer entityInstanceId, Integer activeState);
	List<WorkflowMasterEntityMapping> getWorkflowEntitiesForProduct(int productId);
	List<WorkflowMaster> getWorkflowMasterList();
	void addWorkflowMaster(WorkflowMaster workflowMaster);
	void updateWorkflowMaster(WorkflowMaster workflowMaster);
	void addWorkflowProductEntityMapping(WorkflowMasterEntityMapping workflowMasterEntityMapping);
	boolean checkWorkflowEntityMappingAlreadyExist(Integer workflowEntityMappingId, Integer productId, Integer workflowId, Integer entityTypeId, Integer entityId);
	WorkflowMasterEntityMapping getWorkflowOfEntityInstance(Integer productId, Integer workflowId, Integer entityTypeId, Integer entityId, Integer entityInstanceId);
	boolean checkEligibilityForDefault(Integer workflowEntityMappingId, Integer productId, Integer entityTypeId, Integer entityId);
	void updateIsDefaultOfEntityMapping(Integer workflowEntityMappingId, Integer productId, Integer entityTypeId, Integer entityId);
	List<Object[]> getWorkflowMyActionCounts(Integer productId, Integer userId, Integer userRoleId,Integer entityTypeId);
	List<WorkflowMaster> getWorkflowMasterMappedToEntityList(Integer productId, Integer entityTypeId, Integer entityId, Integer activeState);
	List<WorkflowMaster> getWorkflowMasterListByType(String workflowType);
	List<WorkflowMaster> getWorkFlowMasterListByWorkflowName(String WorkflowName);
	List<WorkflowMasterEntityMapping> getEntitiesAndInstanceMappedWithWorkflow(Integer workflowId);
	Boolean isWorkflowAleadyExist(Integer referenceWorkflowId, String workflowName);
	List<WorkflowMaster> getWorkflowMasterListByStatus(Integer isActive);
}
