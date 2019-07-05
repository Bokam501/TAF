package com.hcl.ilcm.workflow.dao;

import java.util.List;

import com.hcl.atf.taf.model.Activity;

public interface WorkflowActivityDAO {
	List<Activity> listActivitiesforWorkFlowForUserOrRole(Integer testFactoryId,Integer productId,Integer activityWorkpackageId, Integer workflowId, Integer entityActivityType, Integer activityMasterId, Integer userId, Integer userRoleId, Integer jtStartIndex, Integer jtPageSize);
	List<Activity> listActivitiesforWorkFlowSLAIndicator(Integer productId,Integer entityTypeId,List<Integer> activityIds,Integer jtStartIndex, Integer jtPageSize);
	List<Activity> listActivitiesforWorkFlowStatusPartForUserOrRole(Integer testFactoryId,Integer productId,Integer activityWorkpackageId,Integer entityTypeId,Integer userId, Integer roleId);
	
	void removeInstanceFromEntityWorkflowStatusActorsMapping(Integer entityTypeId, Integer entityId, Integer entityInstanceId);
}
