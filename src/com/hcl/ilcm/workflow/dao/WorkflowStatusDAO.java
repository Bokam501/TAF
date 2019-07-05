/**
 * 
 */
package com.hcl.ilcm.workflow.dao;

import java.util.HashMap;
import java.util.List;

import com.hcl.ilcm.workflow.model.WorkflowStatus;
import com.hcl.ilcm.workflow.model.WorkflowStatusMapping;

/**
 * @author silambarasur
 *
 */
public interface WorkflowStatusDAO {
	List<WorkflowStatus> getWorkFlowStatusList(Integer productId,Integer entityTypeId,Integer entityId,List<String> workFlowStatusList);
	WorkflowStatus getWorkflowStatusById(Integer worflowStatusId);
	WorkflowStatus getStatusNameByEntityTypeId(String resultPrimaryStatus,Integer entityTypeId);
	List<WorkflowStatus> getSecondaryStatusByParentId(Integer productId,Integer entityTypeId,Integer wfStatusParentId);
	List<WorkflowStatus> getWorkFlowStatusList(Integer workflowId, List<String> workFlowStatusNamesFromBPMN);
	boolean isExistWorkflowStausByWorkflowIdAndWorkflowStatusName(Integer workflowId,String workflowStatusName);
	boolean isExistWorkflowStausType(Integer workflowId,String workflowStatusType);
	WorkflowStatus getInitialStatusForInstance(Integer productId, Integer entityTypeId, Integer entityId, Integer workflowId);
	Boolean isWorkflowContainsMandatoryStatusTypeStatus(Integer workflowId);
	Boolean isStatusAvailableForStatusType(Integer workflowId, String updatedStatusType, Integer exceptStatusId);
	List<String> getWorflowStatusTypes(Class className, String aliasName, HashMap<String, Object> constraints, String projection);
	WorkflowStatus getFirstWorkflowStatusOfStatusType(Integer workflowId, String statusType, String orderBy);
	List<Object[]> getWorkflowStatusAvailableForMapping(Integer workflowId,	Integer sourceStatusId, Integer jtStartIndex, Integer jtPageSize);
	Integer getWorkflowStatusAvailableForMappingCount(Integer workflowId, Integer sourceStatusId);
	List<Object[]> getWorkflowStatusAlreadyMapped(Integer workflowId, Integer sourceStatusId, Integer jtStartIndex, Integer jtPageSize);
	boolean isWorkflowStatusAlreadyMapped(Integer workflowId, Integer sourceStatusId, Integer targetStatusId);
	void workflowStatusMappingOrUnmapping(WorkflowStatusMapping workflowStatusMapping, String maporunmap);
	List<WorkflowStatus> getPossibleWorkflowStatusForAction(Integer currentStatusId);
	WorkflowStatus getWorkflowStatusByName(String worflowStatusName);
	WorkflowStatus getWorkflowStausByWorkflowIdAndWorkflowStatusName(Integer workflowId,String workflowStatusName);
}
