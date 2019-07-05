package com.hcl.ilcm.workflow.service;


import java.util.List;

import com.hcl.atf.taf.model.Activity;
import com.hcl.atf.taf.model.ActivityTask;
import com.hcl.atf.taf.model.ActivityWorkPackage;
import com.hcl.atf.taf.model.TestCaseList;
import com.hcl.atf.taf.model.UserList;
import com.hcl.atf.taf.model.json.jtable.JTableSingleResponse;
import com.hcl.ilcm.workflow.model.WorkflowMasterEntityMapping;
/**
 * @author silambarasur
 *
 */
public interface ConfigurationWorkFlowService {
	List<ActivityTask> listActivityTasksForUser(Integer productId, String entityLevel, Integer entityLevelId, Integer userId, Integer roleId, Integer jtStartIndex, Integer jtPageSize);
	List<TestCaseList> listTestcasesForUser(Integer productId, String entityLevel, Integer entityLevelId, Integer userId, Integer roleId,Integer jtStartIndex, Integer jtPageSize);
	List<WorkflowMasterEntityMapping> getWorkflowEntitiesForProduct(int productId);
	void addWorkflowProductEntityMapping(WorkflowMasterEntityMapping workflowMasterEntityMapping);
	boolean checkWorkflowEntityMappingAlreadyExist(Integer workflowEntityMappingId, Integer productId, Integer workflowId, Integer entityTypeId, Integer entityId);
	boolean checkEligibilityForDefault(Integer workflowEntityMappingId, Integer productId, Integer entityTypeId, Integer entityId);
	void updateIsDefaultOfEntityMapping(Integer workflowEntityMappingId, Integer productId, Integer entityTypeId, Integer entityId);
	List<Object[]> getWorkflowMyActionCounts(Integer productId, Integer userId, Integer userRoleId,Integer entityTypeId);
	JTableSingleResponse changeInstnaceWorkflowMapping(Integer productId, Integer entityTypeId, Integer entityId, Integer entityInstanceId, Integer workflowId, UserList user);
	List<Activity> listActivitiesForUser(Integer productId, String entityLevel, Integer entityLevelId, Integer userId, Integer userRoleId, Integer jtStartIndex, Integer jtPageSize);
	List<ActivityWorkPackage> listActivityWorkpackagesForUser(Integer productId, String entityLevel, Integer entityLevelId, Integer userId, Integer roleId, Integer jtStartIndex, Integer jtPageSize);
	List<WorkflowMasterEntityMapping> getEntitiesAndInstanceMappedWithWorkflow(Integer workflowId);
	JTableSingleResponse changeInstnaceActorMapping(Integer productId, Integer entityTypeId, Integer entityId, Integer entityInstanceId, Integer workflowId, UserList user);
}
