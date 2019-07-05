package com.hcl.ilcm.workflow.dao;

import java.util.List;

import com.hcl.atf.taf.model.ActivityTask;

public interface WorkflowTaskDAO {
	
	List<ActivityTask> listActivityTaskforWorkFlow(Integer productId,String entityLevel, Integer entityLevelId,Integer taskTypeId, Integer userId,List<String> assigneeStatusList, List<String> leadStatusList,List<String> roleStatusList);
	List<ActivityTask> listActivityTasksforWorkFlowForUserOrRole(Integer productId, Integer workflowId, Integer entityTypeId, Integer entityId, Integer userId, Integer roleId, Integer jtStartIndex, Integer jtPageSize);
	List<ActivityTask> listActivityTasksforWorkFlowSLAIndicator(Integer productId,Integer entityTypeId,List<Integer> taskIds,Integer jtStartIndex, Integer jtPageSize);
}
