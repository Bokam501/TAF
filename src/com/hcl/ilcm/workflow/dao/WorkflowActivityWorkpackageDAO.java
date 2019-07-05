package com.hcl.ilcm.workflow.dao;

import java.util.List;

import com.hcl.atf.taf.model.ActivityWorkPackage;

public interface WorkflowActivityWorkpackageDAO {
	List<ActivityWorkPackage> listActivityWorkpackageforWorkFlowForUserOrRole(Integer productId, Integer workflowId, Integer entityActivityType, Integer activityMasterId, Integer userId, Integer userRoleId, Integer jtStartIndex, Integer jtPageSize);
	List<ActivityWorkPackage> listActivityWorkpackageforWorkFlowSLAIndicator(Integer productId,Integer entityTypeId,List<Integer> activityIds,Integer jtStartIndex, Integer jtPageSize);
}
