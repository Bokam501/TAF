package com.hcl.atf.taf.service;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.hcl.atf.taf.model.ActivityTask;
import com.hcl.atf.taf.model.ActivityTaskType;
import com.hcl.atf.taf.model.UserList;
import com.hcl.atf.taf.model.json.JsonActivityTask;

public interface ActivityTaskService {
	List<ActivityTask> activityTaskLists();
	List<ActivityTask> getActivityTaskById(Integer activityId);
	void addActivityTask(ActivityTask addActivityTask);
	void updateActivityTask(ActivityTask updateActivityTask);
	String deleteActivityTask(int activityTaskId, String referencedTableName, String referencedColumnName);
	List<JsonActivityTask> listActivityTaskByActivityId(Integer activityId,Integer initializationLevel, Integer isActive);
	ActivityTask getActivityTaskById(Integer activityTaskId,Integer initializationLevel);
	List<JsonActivityTask> listActivityTaskByActivityIdAndUserId(HttpServletRequest req,Integer productId, int activityId,UserList user,Integer jtStartIndex, Integer jtPageSize,Map<String,String> searchStrings, Integer initializationLevel);
	List<JsonActivityTask> listActivityTasks(HttpServletRequest req,Integer productId,	Integer productVersionId, Integer productBuildId,Integer activityWorkPackageId, Integer activityId, Integer initializationLevel, Integer isActive,UserList user,
			Integer jtStartIndex, Integer jtPageSize, Map<String, String> searchStrings);
	void updateActivityTaskEffortTracker(Integer taskId, String resultEntity,
			String resultPrimaryStatus, String resultsecondaryStatus,
			Integer taskWiseEffort,
			String resultModifiedBy, String sourcestatus, String resultComments,Integer dimensionId);	
	HashSet<Integer> getActivtyTaskPrimaryStatus(Integer taskId);
	Integer countAllactivityTaskService(Date startDate, Date endDate);
	List<ActivityTask> listAllactivityTask(int startIndex, int pageSize, Date startDate,Date endDate);
	boolean getActivityTaskById(Integer activityId,String activityTaskName);
	
	ActivityTaskType getActivityTaskTypeById(Integer activityTaskTypeId);
	void updateActivityBasedOnTasks(Integer activityId);
	
	int addActivityTasksBulk(List<ActivityTask> activityTaskList,Integer productId);
	
	List<ActivityTaskType> listActivityTaskTypes(Integer testFactoryId, Integer productId, Integer status, Integer jtStartIndex, Integer jtPageSize, Boolean isConsolidated);
	Integer getTotalRecordsForTaskTypesPagination(Integer testFactoryId, Integer productId, Integer status, Boolean isConsolidated);
	Boolean checkTaskTypeExistForProduct(Integer testFactoryId, Integer productId, String taskTypeName, Integer activityTaskTypeId, Boolean isConsolidated);
	void addActivityTaskType(ActivityTaskType activityTaskType);
	void updateActivityTaskType(ActivityTaskType activityTaskType);
	
	int getActivityStatusCategoryIdByTaskStatus(Integer activityId);
	Integer listActivityTasksCount(Integer productId, Integer productVersionId, Integer productBuildId, Integer activityWorkPackageId, Integer activityId, Integer initializationLevel, Integer isActive, Map<String, String> searchStrings);
} 
 