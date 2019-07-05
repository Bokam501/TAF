package com.hcl.atf.taf.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.hcl.atf.taf.model.Activity;
import com.hcl.atf.taf.model.ActivityWorkPackage;
import com.hcl.atf.taf.model.ChangeRequest;
import com.hcl.atf.taf.model.CustomFieldValues;
import com.hcl.atf.taf.model.EnvironmentCombination;
import com.hcl.atf.taf.model.ExecutionTypeMaster;
import com.hcl.atf.taf.model.ProductBuild;
import com.hcl.atf.taf.model.UserList;
import com.hcl.atf.taf.model.dto.ActivitiesDTO;
import com.hcl.atf.taf.model.dto.ProductSummaryForActivityProcessDTO;
import com.hcl.atf.taf.model.json.JsonActivity;

public interface ActivityService {
	List<Activity> activityLists();
	void addActivity(Activity addActivity);
	void updateActivity(Activity updateActivity);
	String deleteActivity(int activityId, String referencedTableName, String referencedColumnName);	
	Activity getActivityById(Integer activityId,Integer initializationLevel);
	List<JsonActivity> listActivities(Map<String, String> searchStrings,Integer engagementId, Integer productId,Integer productVersionId, Integer productBuildId,Integer activityWorkPackageId, Integer initializationLevel,Integer isActive,HttpServletRequest req,String statusType,Integer startIndex,Integer pageSize );
	List<JsonActivity> listActivitiesByActivityWorkPackageId(Integer activityWorkPackageId, Integer initializationLevel,
			Integer isActive);
	int getUnMappedEnvironmentCombiListCountOfActivityByActivityId(
			int productId, int activityId,int initialisationLevel);
	List<Object[]> getUnMappedEnvironmentCombiListByActivityId(int productId,
			int activityId, int jtStartIndex, int jtPageSize, int initialisationLevel);	
	EnvironmentCombination updateEnvironmentCombiToActivityOneToMany(
			Integer enviCombiId, Integer activityId, String maporunmap);
	List<Object[]> getMappedEnvCombiByActivityId(int productId,
			int intactivityId, int i, int j,int initialisationLevel);
	int getUnMappedRcnByActivityId(int activityId, int i, int productId);
	List<Object[]> getUnMappedRcnByActivityId(int productId,int activityId, int jtStartIndex,
			int jtPageSize, int i);
    List<Object[]> getMappedRcnByActivityId(int intactivityId, int i, int j,int k);
	ChangeRequest updateChangeRequestToActivityOneToMany(Integer changeRequestId,
			Integer activityId, String maporunmap);
	int addActivitiesBulk(List<Activity> listOfActivitiesToAdd);
	List<String> getExistingActiviesName(ActivityWorkPackage activityWorkPackage, ProductBuild productBuild);
	ExecutionTypeMaster getExecutionTypeByExecutionTypeName(String executionTypeName);
	
	ProductSummaryForActivityProcessDTO getProductActivitySummaryDetails(Integer productId);
	float getScheduleVarianceforProduct(Integer productId);
	Integer countAllActivity(Date startDate, Date endDate);
	boolean getActivityByWPIdandActivityName(Integer activityWorkPackageId,String activityName);
	Integer countAllActivityWorkpackage(Date startDate, Date endDate);
	List<JsonActivity> listTotalActivitiesByUserId(int userId,Integer userRoleId,
			int jtStartIndex, int jtPageSize);
	Activity getActivityByWorkpackageIdAndActivityName(Integer activityWorkPackageId,String activityName);
	ActivitiesDTO getMyActivitySummary(Integer activityId,Integer userId);
	ActivityWorkPackage getActivityWorkPackageByActivityId(Integer activityId, Integer initializationLevel);
	
	int getActivityWorkpackageStatusCategoryIdByActivityStatusCategory(Integer activityWorkPackageId);
	Activity getActivityByName(String activityName);
	List<Activity> listAllActivities(Map<String, String> searchString,
			Integer productId, Integer productVersionId,
			Integer productBuildId, Integer activityWorkPackageId,
			Integer initializationLevel, Integer isActive);
	List<Activity> listAllActivitiesByActivityWorkPackageId(
			Integer activityWorkPackageId, Integer initializationLevel,
			Integer isActive);
	Integer getActivitiesCount(Map<String, String> searchString, Integer engagementId, Integer productId, Integer productVersionId, Integer productBuildId, Integer activityWorkPackageId, Integer initializationLevel, Integer isActive);
	List<Activity> getActivityByActivtyWorkpackageId(Integer awpId);
	int getProductIdOfActivity(Integer activityId);
	Activity getByActivityId(int activityId);
	List<Integer> getActivitiesofAWPIDS(int activityWorkPackageID, List<Integer> activityWorkPackageIdList, boolean paramIsList);
	List<Activity> getActivityByParents(Integer parentInstanceId, String parentName);
	List<Activity> getActivitiesForAutoAllocation(Integer parentEntityId, Integer parentEntityInstanceId);
	List<Activity> getActivitiesForAutoAllocationByReferenceId(Integer referenceId);
	int getActivityPresentCountBasedOnCategoryName(int activityId, String categoryName);
	void processActivityPokeNotification(Integer activityId,UserList user,String message,String messageType,List<String> ccMailList,List<String> toMailList);
	List<Activity> getActivitiesForSetOfActivityIds(List<Integer> activityIds);
	List<Activity> getActivitiesBasedOnTestFactoryIdProductIdandAwpId(Integer testFactoryId, Integer productId, Integer awpId);
	List<JsonActivity> listMyActivities(HttpServletRequest request,List<Activity> activitieslist, Integer testFactoryId,Integer productId, Integer startIndex, Integer pageSize);
	List<Activity> listMyActivitiesByLoginIdAndProductId(Integer userId,Integer testFactoryId,Integer productId,Integer activityWorkPackageId);
	List<Activity> listActivitiesByPassingThrough(Integer productId,Integer userId,Integer activityWorkPackageId);
	void getPendingStatusActivities(Integer productId, Integer entityTypeId,String botName);
	List<Activity> listActivitiesByProductIdAndWorkpackageId(Integer enagementId,Integer productId,Integer activityWorkPackageId, Integer initializationLevel,
			Integer isActive,Integer startIndex,Integer pageSize );	
	List<Activity> getActivityByActivtyWorkpackageIdNotCompletedStage(Integer awpId);
	
	void getWeeklyReportsForSLUActivities(Integer productId, Integer entityTypeId);
	String customFieldValidationProcessing(String customFileds, Integer customFieldMappedTo, UserList user, List<CustomFieldValues> customFieldValuesList, CustomFieldService customFieldService);
	void getMonthlyReportsForSLUActivities(Integer productId, Integer entityTypeId);
	
	void updateActivityPredecessors(Activity activity, String predecessorsString);
	List<Object[]> getUnMappedRcnByActivityWPId(int productId, int activityWPId, int activityId, int jtStartIndex,
			int jtPageSize);
	int getUnMappedRcnCountByActivityWPId(int activityId, int productId,int activityWPId);
	List<JsonActivity> listActivitiesBySerachCriteria(Integer enagementId,Integer productId, Integer productVersionId, Integer productBuildId,UserList user,List<Integer> activityWorkPackageIds,List<String> workflowStatusList,List<String> workflowStatusTypeList, List<String> activityTypeList,List<String> assigneeList,List<String> reviewerList,
			Date plannedStartFromDate,Date plannedStartToDate,String plannedStartDateSearch,
			Date plannedEndFromDate,Date plannedEndToDate,String plannedEndDateSearch,
			Date actualStartFromDate,Date actualStartToDate,String actualStartDateSearch,
			Date actualEndFromDate,Date actualEndToDate, String actualEndDateSearch);
}
