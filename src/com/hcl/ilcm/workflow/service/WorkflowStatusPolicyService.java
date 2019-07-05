/**
 * 
 */
package com.hcl.ilcm.workflow.service;

import java.util.Date;
import java.util.List;

import com.hcl.atf.taf.model.Activity;
import com.hcl.atf.taf.model.ActivityTask;
import com.hcl.atf.taf.model.ActivityWorkPackage;
import com.hcl.atf.taf.model.TestCaseList;
import com.hcl.atf.taf.model.UserList;
import com.hcl.ilcm.workflow.model.WorkflowMasterEntityMapping;
import com.hcl.ilcm.workflow.model.WorkflowStatus;
import com.hcl.ilcm.workflow.model.WorkflowStatusActor;
import com.hcl.ilcm.workflow.model.WorkflowStatusPolicy;
import com.hcl.ilcm.workflow.model.json.JsonWorkflowSLAInstance;

/**
 * @author silambarasur
 *
 */
public interface WorkflowStatusPolicyService {
	
	List<WorkflowStatusPolicy> getWorkflowStatusPolicies(Integer workflowId,Integer entityTypeId,Integer entityId, Integer entityInstanceId, Integer levelId,Integer activeStatus, String statusPolicyType);
	void addWorkflowStatusPolicy(WorkflowStatusPolicy workflowStatusPolicies,Integer workflowId);
	void updateWorkflowStatusPolicy(WorkflowStatusPolicy workflowStatusPolicies,Integer workflowId);
	void mapStatusToEntityAndInstance(WorkflowStatusPolicy workflowStatusPolicy, Integer workflowId);
	
	void setEntityStatusPoliciesFromWorkflowPolicies(Integer productId, Integer entityTypeId, Integer entityId, Integer workflowId);
	void setEntityInstanceStatusPoliciesFromWorkflowPolicies(Integer productId, Integer entityTypeId, Integer entityId, Integer entityInstanceId, Integer workflowId, Date plannedStartDate);
	void addWorkflowStatusActor(WorkflowStatusActor workflowStatusActor);
	List<WorkflowStatusActor> getWorkflowStatusActor(Integer productId, Integer workflowStatusId, Integer entityTypeId, Integer entityId, Integer entityInstanceId, String actorType);
	boolean checkWorkflowStatusActorAlreadyExist(Integer productId, Integer workflowStatusActorId, Integer workflowStatusId, Integer entityTypeId, Integer entityId, Integer entityInstanceId, Integer userId, Integer roleId);
	void deleteWorkflowStatusActor(WorkflowStatusActor workflowStatusActor);
	void instanciateActorFromEntityToInstance(Integer productId, Integer workflowStatusId, Integer entityTypeId, Integer entityId, Integer entityInstanceId, String actorType);
	void instanciateWorkflowForInstance(Integer productId, Integer workflowId, Integer entityTypeId, Integer entityId, Integer entityInstanceId);
	void instanciateWorkflowStatusAndPoliciesForInstanceByWorkflowId(Integer productId, Integer entityTypeId, Integer entityId, Integer entityInstanceId, Integer workflowId, Integer statusId, UserList user, Date plannedStartDate, Object instanceObject);
	void instanciateWorkflowStatusAndPoliciesForInstanceByEntityType(Integer productId, Integer entityTypeId, Integer entityId, Integer entityInstanceId, Integer statusId, UserList user, Date plannedStartDate, Object instanceObject);
	void updateInstanceEntityMappingAndDeleteStatusPolicies(Integer entityTypeId, Integer entityId, Integer entityInstanceId, Integer workflowId);
	List<Object[]> listWorkflowStatusSummary(Integer engagmentId,Integer productId, Integer entityTypeId, Integer entityId, Integer entityInstanceId);
	List<Object[]> listWorkflowStatusActorSummary(Integer engagementId,Integer productId, Integer entityTypeId, Integer entityId, Integer entityInstanceId, String actorType);
	Integer getWorkflowStatusActorSummaryPaginationCount(Integer productId, Integer entityTypeId, Integer entityId, Integer entityInstanceId, String actorType);
	Integer getWorkflowStatusSummaryPaginationCount(Integer productId, Integer entityTypeId, Integer entityId, Integer entityInstanceId);
	List<Object[]> getPendingWithInstanceActors(Integer productId,Integer entityTypeId,Integer entityId);
	void setInstanceIndicators(Integer testFactoryId,Integer productId,Integer workpackageId, Integer entityTypeId, List<?> instanceObjects, Integer attachemntEntityTypeId,UserList user, Date modifiedAfterDate, Integer entityId, Integer entityInstanceId,boolean enablePendingWith);
	List<Object[]> getWorkflowStatusSummaryCountByProduct(Integer engagementId,Integer productId,Integer entityTypeId,Integer entityParentInstanceId);
	List<ActivityTask> getWorkflowSLATaskDetailView(Integer productId,Integer entityTypeId,Integer lifeCycleEntityId,Integer lifeCycleStageId,String workflowType,String indicator,Integer entityParentInstanceId, Integer jtStartIndex,Integer jtPageSize);
	JsonWorkflowSLAInstance setJsonWorkflowSLAInstance(Object instanceObject, String viewType);
	List<TestCaseList> getWorkflowSLATestCaseDetailView(Integer productId,Integer entityTypeId,String indicator,Integer jtStartIndex,Integer jtPageSize);
	List<ActivityTask> listWorkflowStatusSLADetailForTask(Integer productId, Integer entityTypeId, Integer entityId,Integer workflowStatusId,Integer userId,Integer roleId,Integer workflowId,Integer jtStartIndex,Integer jtPageSize);
	List<TestCaseList> listWorkflowStatusSLADetailForTestcase(Integer productId, Integer entityTypeId, Integer entityId,Integer workflowStatusId,Integer userId,Integer roleId,Integer workflowId,Integer jtStartIndex,Integer jtPageSize);
	List<Activity> getWorkflowSLAActivityDetailView(Integer engagementId,Integer productId,Integer entityTypeId,Integer lifeCycleEntityId,Integer lifeCycleStageId,String workflowType,String indicator,Integer entityParentInstanceId,Integer jtStartIndex,Integer jtPageSize);
	List<Activity> listWorkflowStatusSLADetailForActivity(Integer engagementId,Integer productId, Integer entityTypeId, Integer entityId,Integer workflowStatusId,Integer userId,Integer roleId,Integer typeId,Integer workflowStatusCategoryId,Integer entityParentInstanceId,Integer jtStartIndex,Integer jtPageSize);
	List<ActivityWorkPackage> getWorkflowSLAActivityWorkPackageDetailView(Integer engagementId,Integer productId, Integer entityTypeId, String indicator,Integer entityParentInstanceId, Integer jtStartIndex, Integer jtPageSize);
	List<ActivityWorkPackage> listWorkflowStatusSLADetailForActivityWorkpackage(Integer engagementId,Integer productId, Integer entityTypeId, Integer entityId, Integer workflowStatusId, Integer userId, Integer roleId,Integer workflowId, Integer jtStartIndex, Integer jtPageSize);
	List<Object[]> getWorkpackageRAGViewSummaryList(Integer productId);
	void addWorkflowInstanceLifeCycleStage(Integer productId,Integer entityTypeId, Integer entityInstanceId, Integer lifeCycleStageId, String instancePlannedStartDate, String instancePlannedEndDate, String createdDate, Integer createdBy);
	
	List<Object[]> getWorkflowTypeSummaryByEntityType(Integer engagmentId,Integer productId,Integer entityTypeId,Integer entityParentInstanceId);
	
	List<String> getWorkFlowStatuNamebyWorkFlowId(int workflowId);
	
	List<WorkflowStatus> getWorkFlowStatus(int workflowId);
	List<?> getPaginationListFromFullList(List<?> instanceObjects, Integer startIndex, Integer maxNumberOfInstance);
	List<WorkflowMasterEntityMapping> getEntitiesAndInstanceMappedWithWorkflow(Integer workflowId);
	WorkflowStatusPolicy getWorkflowStatusPolicyById(Integer workflowStatusPolicyId);
	void deleteWorkflowStatusAndPolicy(Integer workflowStatusId);
	boolean isWorkflowStatusInLifeCycleStage(Integer workflowStatusId);
	void updateWorkflowInstanceLifeCycleStage(Integer productId,Integer entityTypeId, Integer entityInstanceId, Integer lifeCycleStageId, String instancePlannedStartDate, String instancePlannedEndDate, String createdDate, Integer createdBy);
	List<Object[]> getWorkflowRAGSummaryCountByProductAndResouces(Integer engagementId,Integer productId,Integer entityTypeId,Integer entityParentInstanceId);
	List<Object[]> getWorkflowRAGSummaryCountByProductAndEntityType(Integer engagementId,Integer productId,Integer entityTypeId,Integer entityParentInstanceId);
	List<Object[]> getWorkflowRAGActivityGroupingSummaryCount(Integer engagementId,Integer productId,Integer entityTypeId,Integer parentEntityInstanceId);
	
	List<Object[]> getWorkflowRAGWorkpackageSummaryCount(Integer productId,Integer entityTypeId,Integer entityParentInstanceId);
	List<Object[]> getWorkflowRAGActivitySummaryCount(Integer productId,Integer entityTypeId);
	List<ActivityWorkPackage> getWorkflowSLAWorkpackageDetailRAGView(Integer engagementId,Integer productId,Integer entityTypeId,String indicator,Integer jtStartIndex,Integer jtPageSize);
	List<Object[]> getWorkflowRAGSummarySLADetailView(Integer productId,Integer entityTypeId,String indicator);
	List<ActivityTask> getWorkflowSLATaskDetailRAGView(Integer engagementId,Integer productId,Integer entityTypeId,String indicator,Integer jtStartIndex,Integer jtPageSize);
	List<Activity> getWorkflowSLAActivityDetailRAGView(Integer engagementId,Integer productId,Integer entityTypeId,String indicator,Integer categoryId,Integer jtStartIndex,Integer jtPageSize);
	List<ActivityTask> listWorkflowRAGStatusDetailForTask(Integer productId, Integer entityTypeId, Integer entityId,Integer entityTypeInstanceId,Integer userId,Integer roleId,String indicator,Integer jtStartIndex,Integer jtPageSize);
	List<Activity> listWorkflowRAGStatusDetailForActivity(Integer engagementId,Integer productId, Integer entityTypeId, Integer entityId,Integer entityTypeInstanceId,Integer userId,Integer roleId,String indicator,Integer categoryId,Integer jtStartIndex,Integer jtPageSize);
	List<ActivityWorkPackage> listWorkflowRAGStatusDetailForActivityWorkpackage(Integer engagementId,Integer productId, Integer entityTypeId, Integer entityId,Integer entityTypeInstanceId,Integer userId,Integer roleId,String indicator,Integer jtStartIndex,Integer jtPageSize);
	
	List<Object[]> getWorkflowRAGSummaryCompleteCountByProductAndEntityType(Integer engagementId,Integer productId,Integer entityTypeId,Integer entityParentInstanceId);
	List<Object[]> getWorkflowRAGSummaryAbortCountByProductAndEntityType(Integer engagementId,Integer productId,Integer entityTypeId,Integer entityParentInstanceId);
	
	List<Object[]> getRAGviewWorkpackageSummaryCompletedCount(Integer productId,Integer entityTypeId,Integer entityParentInstanceId);
	List<Object[]> getRAGviewWorkpackageSummaryAbortedCount(Integer productId,Integer entityTypeId,Integer entityParentInstanceId);
	
	List<Object[]> getRAGSummaryCompletedCountByProductAndResouces(Integer engagementId,Integer productId,Integer entityTypeId,Integer entityParentInstanceId);
	List<Object[]> getRAGSummaryAbortedCountByProductAndResouces(Integer engagementId,Integer productId,Integer entityTypeId,Integer entityParentInstanceId);
	
	List<Object[]> getRAGViewActivityGroupingSummaryCompletedCount(Integer engagementId,Integer productId,Integer entityTypeId,Integer entityParentInstanceId);
	List<Object[]> getRAGViewActivityGroupingSummaryAbortCount(Integer engagementId,Integer productId,Integer entityTypeId,Integer entityParentInstanceId);
	
	List<Object[]> listWorkflowStatusCategorySummaryCount(Integer engagmentId,Integer productId, Integer entityTypeId, Integer entityId, Integer entityInstanceId);
	List<Object[]> getWorkflowRAGEngagementSummaryCount(Integer engagementId,Integer productId,Integer entityTypeId,Integer entityParentInstanceId);
	List<Object[]> getRAGViewEngagementSummaryCompletedCount(Integer engagementId,Integer productId,Integer entityTypeId,Integer entityParentInstanceId);
	List<Object[]> getRAGViewEngagementSummaryAbortCount(Integer engagementId,Integer productId,Integer entityTypeId,Integer entityParentInstanceId);
	
	List<Object[]> getWorkflowRAGActivityGroupingSummaryTotalCount(Integer engagementId,Integer productId,Integer entityTypeId,Integer parentEntityInstanceId);
	
	List<Object[]> getWorkflowRAGActivityCategoriesSummaryCount(Integer engagementId,Integer productId, Integer entityTypeId,Integer entityParentInstanceId);
	List<Object[]> getWorkflowRAGActivityCategoriesSummaryCompletedCount(Integer engagementId,Integer productId, Integer entityTypeId,Integer entityParentInstanceId);
	List<Object[]> getWorkflowRAGActivityCategoriesSummaryAbortCount(Integer engagementId,Integer productId, Integer entityTypeId,Integer entityParentInstanceId);
	
	List<Activity> listActivitiesforWorkFlowStatusPartForUserOrRole(Integer productId,Integer entityTypeId,Integer userId, Integer roleId);
	
	List<Activity> listActivitiesforProductIdAndActivityIds(Integer productId, Integer entityTypeId, List<Integer> entityInstanceIds,Integer jtStartIndex,Integer jtPageSize);
	
	void removeInstanceFromEntityWorkflowStatusActorsMapping(Integer entityTypeId, Integer entityId, Integer entityInstanceId);
}
