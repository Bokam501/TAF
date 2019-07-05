package com.hcl.atf.taf.dao;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.hcl.atf.taf.model.ActivityTask;
import com.hcl.atf.taf.model.ActivityTaskType;

public interface ActivityTaskDAO {
	List<ActivityTask> activityTasklists();
	void addActivityTask(ActivityTask addTask);
	void updateActivityTask(ActivityTask updateTask);
	String deleteActivityTask(int activityTaskId, String referencedTableName, String referencedColumnName);
	List<ActivityTask> listActivityTasksByActivityId(Integer activityId,Integer initializationLevel, Integer isActive);
	ActivityTask getActivityTaskById(Integer activityTaskId, Integer initializationLevel);
	List<ActivityTask> listActivityTaskforSelfReview(int activityId, Integer userId,Integer userRoleId, Integer jtStartIndex,Integer jtPageSize, Integer initializationLevel,Map<String, String> searchString);
	List<ActivityTask> listActivityTasks(Integer productId,	Integer productVersionId, Integer productBuildId,Integer activityWorkPackageId, Integer activityId,
			Integer initializationLevel, Integer isActive,Integer jtStartIndex, Integer jtPageSize, Map<String, String> searchStrings);
	List<ActivityTask> listActivityTaskforPeerReview(int activityId,
			Integer userId, Integer jtStartIndex, Integer jtPageSize,
			Integer initializationLevel, int filter);
	List<ActivityTask> listActivityTaskforPQAReview(int activityId,
			Integer userId, Integer jtStartIndex, Integer jtPageSize,
			Integer initializationLevel, int filter);
	List<ActivityTask> getActivityTaskByActivityId(Integer activityId);
	Integer countAllactivityTaskService(Date startDate, Date endDate);
	List<ActivityTask> listAllactivityTask(int startIndex,int pageIndex,Date startDate, Date endDate);
	boolean getActivityTaskById(Integer activityId,String activityTaskName);
	
	ActivityTaskType getActivityTaskTypeById(Integer activityTaskTypeId);
	void updateActivityBasedOnTasks(Integer activityId);
	Integer getActivitytaskCountByActivityId(Integer activityId);
	Integer getActivitytaskCountByStatus(Integer activityId,
			Integer userId, Integer statusFilterCode);
	Integer getActivitytaskCountByActivityIdAndUserId(Integer activityId,
			Integer userId);
	List<ActivityTaskType> listActivityTaskTypes(Integer testFactoryId, Integer productId, Integer status, Integer jtStartIndex, Integer jtPageSize, Boolean isConsolidated);
	Integer getTotalRecordsForTaskTypesPagination(Integer testFactoryId, Integer productId, Integer status, Boolean isConsolidated);
	Boolean checkTaskTypeExistForProduct(Integer testFactoryId, Integer productId, String taskTypeName, Integer activityTaskTypeId, Boolean isConsolidated);
	void addActivityTaskType(ActivityTaskType activityTaskType);
	void updateActivityTaskType(ActivityTaskType activityTaskType);
	int addBulk(List<ActivityTask> activityTaskList, int parseInt,int productId);	
	int getActivityStatusCategoryIdByTaskStatus(Integer activityId);
	Integer listActivityTasksCount(Integer productId, Integer productVersionId, Integer productBuildId, Integer activityWorkPackageId, Integer activityId, Integer initializationLevel, Integer isActive, Map<String, String> searchString);
}
