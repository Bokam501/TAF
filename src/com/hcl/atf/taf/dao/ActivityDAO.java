package com.hcl.atf.taf.dao;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.hcl.atf.taf.model.Activity;
import com.hcl.atf.taf.model.ActivityWorkPackage;
import com.hcl.atf.taf.model.ChangeRequest;
import com.hcl.atf.taf.model.EnvironmentCombination;
import com.hcl.atf.taf.model.ExecutionPriority;
import com.hcl.atf.taf.model.ProductBuild;
import com.hcl.atf.taf.model.ProductMaster;
import com.hcl.atf.taf.model.ProductVersionListMaster;
import com.hcl.atf.taf.model.UserList;

public interface ActivityDAO {

	List<Activity> activitylists();
	List<Activity> listActiveActivities();
	void addActivity(Activity addActivity);
	void updateActivity(Activity updateActivity);
	String deleteActivity(int activityId, String referencedTableName, String referencedColumnName);
	Activity getActivityById(Integer activityId, Integer initializationLevel);
	List<ProductMaster> getUserAssociatedProducts(int userId, String filter);
	List<ProductVersionListMaster> getUserAssociatedProductVariants(int userId, String filter);
	List<ProductBuild> getUserAssociatedProductBuilds(int userId, String filter);
	List<ActivityWorkPackage> getUserAssociatedActivityWorkPackages(int userId, String filter);
	List<Activity> getUserAssociatedActivities(int userId, String filter);
	List<Activity> listActivities(Map<String, String> searchString,Integer enagementId, Integer productId,Integer productVersionId, Integer productBuildId,Integer activityWorkPackageId, Integer initializationLevel,Integer isActive,UserList user,String statusType,Integer startIndex, Integer pageSize);
	List<Activity> listActivitiesByActivityWorkPackageId(
			Integer activityWorkPackageId, Integer initializationLevel,
			Integer isActive);
	int getUnMappedEnvironmentCombiListCountOfActivityByActivityId(
			int productId, int activityId, int initialisationLevel);
	List<Object[]> getUnMappedEnvironmentCombiListByActivityId(int productId,
			int activityId, int jtStartIndex, int jtPageSize);	
	EnvironmentCombination updateEnvironmentCombiToActivityOneToMany(
			Integer enviCombiId, Integer activityId, String maporunmap);
	Activity getByActivityId(int activityId);
	List<Object[]> getECListByProductFeatureId(int productId,
			int activityId,int jtStartIndex, int jtPageSize, int initialisationLevel);
	EnvironmentCombination getEnvironmentCombinationByEnvCombId(Integer enviCombiId);


	int getUnMappedRcnByActivityId(int activityId, int i, int productId);
	List<Object[]> getMappedRcnByActivityId(int intactivityId, int i, int j,
			int k);
	ChangeRequest updateChangeRequestToActivityOneToMany(Integer changeRequestId,
			Integer activityId, String maporunmap);
	ExecutionPriority getExecutionPriorityById(int executionPriorityId);
	int addBulk(List<Activity> activitiesList, int batchSize);
	List<String> getExistingActiviesName(ActivityWorkPackage activityWorkPackage,ProductBuild productBuild);
	List<Object[]> getUnMappedRcnByActivityId(int productId, int activityId,
			int jtStartIndex, int jtPageSize);
	List<Activity> listActivitiesByActivityWorkPackageIdAndUserId(Integer activityWorkPackageId,Integer initializationLevel,Integer isActive,Integer userId,String filter);
	List<Object[]> getActivityWorkPackageCountByProductId(Integer productId);
	List<Object[]> getActivityCountByProductId(Integer productId);
	List<Activity> getActivityListByProductId(Integer productId);
	Integer countAllActivity(Date startDate, Date endDate);
	List<Activity> list(int startIndex, int pageSize, Date startDate, Date endDate);
	
	boolean getActivityByWPIdandActivityName(Integer activityWorkPackageId,String activityName);
	Integer countAllActivityWorkpackage(Date startDate, Date endDate);
	List<ActivityWorkPackage> listActivityWorkpackage(int i, int pageSize,Date startDate, Date endDate);
	List<Activity> listActivitiesByActivityWorkPackageIdAndLoginId(
			Integer activityWorkPackageId, Integer initializationLevel,
			Integer isActive, Integer userId,  boolean isTaskLevelAssignee);	
	ActivityWorkPackage getActivityWorkPackageByActivityId(Integer activityId,
			Integer initializationLevel);
	Activity getActivityByWorkpackageIdAndActivityName(Integer activityWorkPackageId, String activityName);
	
	int getActivityWorkpackageStatusCategoryIdByActivityStatusCategory(Integer activityWorkPackageId);
	Activity getActivityByName(String activityName);
	List<Activity> listAllActivities(Map<String, String> searchString,
			Integer productId, Integer productVersionId,
			Integer productBuildId, Integer activityWorkPackageId,
			Integer initializationLevel, Integer isActive);
	Integer getActivitiesCount(Map<String, String> searchString,Integer engagmentId, Integer productId, Integer productVersionId, Integer productBuildId, Integer activityWorkPackageId, Integer initializationLevel, Integer isActive);
	List<Activity> getActivityByActivtyWorkpackageId(Integer awpId);
	int getProductIdOfActivity(Integer activityId);
	List<Integer> getActivitiesofAWPIDS(Integer activityWorkPackageID, List<Integer> activityWorkPackageIdList, boolean paramIsList);
	List<Activity> getActivityByParents(Integer parentInstanceId, String parentName);
	List<Activity> getActivitiesForAutoAllocation(Integer parentEntityId, Integer parentEntityInstanceId);
	List<Activity> getActivitiesForAutoAllocationByReferenceId(Integer referenceId);
	int getActivityPresentCountBasedOnCategoryName(int activityId, String categoryName);
	List<Activity> getActivitiesForSetOfActivityIds(List<Integer> activityIds);
	List<Activity> getActivitiesBasedOnTestFactoryIdProductIdandAwpId(Integer testFactoryId, Integer productId, Integer awpId);
	List<Activity> listMyActivitiesByLoginIdAndProductId(Integer userId,Integer testFactoryId,Integer productId,Integer activityWorkPackageId);
	List<Activity> listActivitiesByPassingThrough(Integer productId,Integer userId,Integer activityWorkPackageId);
	List<Activity> listActivitiesByProductIdAndWorkpackageId(Integer enagementId,Integer productId,Integer activityWorkPackageId, Integer initializationLevel,
			Integer isActive,Integer startIndex,Integer pageSize );	
	List<Activity> getActivityByActivtyWorkpackageIdNotCompletedStage(Integer awpId);
	List<Object[]> getUnMappedRcnByActivityWPId(int productId, int activityWPId, int activityId,
			int jtStartIndex, int jtPageSize);
	int getUnMappedRcnCountByActivityWPId(int activityId, int productId,int activityWPId);
	List<Activity> listActivitiesBySerachCriteria(Integer enagementId,Integer productId, Integer productVersionId, Integer productBuildId,UserList user,List<Integer> activityWorkPackageIds,List<String> workflowStatusList,List<String> workflowStatusTypeList, List<String> activityTypeList,List<String> assigneeList,List<String> reviewerList,Date plannedStartFromDate,Date plannedStartToDate,Date plannedEndFromDate,Date plannedEndToDate,Date actualStartFromDate,Date actualStartToDate,Date actualEndFromDate,Date actualEndToDate);
	}
