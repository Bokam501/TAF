/**
 * 
 */
package com.hcl.ilcm.workflow.dao;

import java.util.HashMap;
import java.util.List;

import com.hcl.ilcm.workflow.model.WorkflowStatus;
import com.hcl.ilcm.workflow.model.WorkflowStatusActor;
import com.hcl.ilcm.workflow.model.WorkflowStatusPolicy;

/**
 * @author silambarasur
 *
 */
public interface WorkflowStatusPolicyDAO {
	List<WorkflowStatusPolicy> getWorkflowStatusPolicies(Integer workflowId,Integer entityTypeId,Integer entityId, Integer entityInstanceId, Integer levelId,Integer activeStatus, String statusPolicyType);
	void addWorkflowStatusPolicy(WorkflowStatusPolicy workflowStatusPolicies,Integer workflowId);
	void updateWorkflowStatusPolicy(WorkflowStatusPolicy workflowStatusPolicies,Integer workflowId);
	void addClonedStatusPolicies(WorkflowStatusPolicy entityStatusPolicies);
	void addWorkflowStatusActor(WorkflowStatusActor workflowStatusActor);
	List<WorkflowStatusActor> getWorkflowStatusActor(Integer productId, Integer workflowStatusId, Integer entityTypeId, Integer entityId, Integer entityInstanceId, String actorType, Integer userId, Integer roleId);
	boolean checkWorkflowStatusActorAlreadyExist(Integer productId, Integer workflowStatusActorId, Integer entityTypeId, Integer entityId, Integer workflowStatusId, Integer entityInstanceId, Integer userId, Integer roleId);
	void deleteWorkflowStatusActor(WorkflowStatusActor workflowStatusActor);
	WorkflowStatusPolicy getWorkflowStatusPolicieByWorkflowStatusId(Integer workflowStatusId, Integer entityTypeId, Integer entityId, Integer entityInstanceId, Integer levelId, Integer activeStatus, String statusPolicyType);
	boolean checkMandatoryUserActionsToChangeStatus(Integer productId, Integer sourceStatusId, Integer entityTypeId, Integer entityId, Integer entityInstanceId, String workflowStatusPolicyUserActionScope, Integer userId, Integer roleId);
	void updateInstanceEntityMappingAndDeleteStatusPolicies(Integer entityTypeId, Integer entityId, Integer entityInstanceId, Integer workflowId);
	List<Object[]> listWorkflowStatusSummary(Integer engagmentId,Integer productId, Integer entityTypeId, Integer entityId, Integer entityInstanceId);
	List<Object[]> listWorkflowStatusActorSummary(Integer engagementId,Integer productId, Integer entityTypeId, Integer entityId, Integer entityInstanceId, String actorType);
	Integer getWorkflowStatusActorSummaryPaginationCount(Integer productId, Integer entityTypeId, Integer entityId, Integer entityInstanceId, String actorType);
	Integer getWorkflowStatusSummaryPaginationCount(Integer productId, Integer entityTypeId, Integer entityId, Integer entityInstanceId);
	
	List<Object[]> getPendingWithInstanceActors(Integer productId,Integer entityTypeId,Integer entityId);
	List<Object[]> getWorkflowStatusSummaryCountByProduct(Integer engagementId,Integer productId,Integer entityTypeId,Integer entityParentInstanceId);
	List<Object[]> getWorkflowSummarySLADetailView(Integer engagementId,Integer productId,Integer entityTypeId,String indicator,Integer entityParentInstanceId);
	List<Object[]> listWorkflowStatusSLADetail(Integer engagementId,Integer productId, Integer entityTypeId, Integer entityId,Integer workflowStatusId,Integer userId,Integer roleId,Integer entityParentInstanceId);
	List<Object[]> listWorkflowStatusCategorySLADetail(Integer engagementId,Integer productId, Integer entityTypeId, Integer entityId,Integer workflowStatusId,Integer userId,Integer roleId,Integer workflowStatusCategoryId,Integer entityParentInstanceId);
	WorkflowStatusPolicy getSLADurationForEntityOrInstance(Integer entityTypeId, Integer entityId, Integer entityInstanceId, Integer sourceStatusId);
	List<Object[]> getWorkpackageRAGViewSummaryList(Integer productId);
	List<Object[]> getWorkflowLifeCycleSummarySLADetailView(Integer engagementId,Integer productId,Integer entityTypeId,Integer lifeCycleEntityId,Integer lifeCycleStageId,String indicator);
	void addWorkflowInstanceLifeCycleStage(Integer productId,Integer entityTypeId, Integer entityInstanceId, Integer lifeCycleStageId, String instancePlannedStartDate, String instancePlannedEndDate, String createdDate, Integer createdBy);
	List<Object[]> getWorkflowTypeSummaryByEntityType(Integer engagmentId,Integer productId,Integer entityTypeId,Integer entityParentInstanceId);
	
	List<Object[]> getWorkflowTypeSummaryInstances(Integer engagementId,Integer productId,Integer workflowId,Integer entityTypeId,Integer entityParentInstanceId);
	List<String> getWorkFlowStatuNamebyWorkFlowId(int workflowId);
	List<WorkflowStatus>getWorkFlowStatus(int workflowId);
	WorkflowStatusPolicy getWorkflowStatusPolicyById(Integer workflowStatusPolicyId);
	void deleteWorkflowStatusAndPolicy(Integer workflowStatusId);
	boolean isWorkflowStatusInLifeCycleStage(Integer workflowStatusId);
	
	void updateWorkflowInstanceLifeCycleStage(Integer productId,Integer entityTypeId, Integer entityInstanceId, Integer lifeCycleStageId, String instancePlannedStartDate, String instancePlannedEndDate, String createdDate, Integer createdBy);
	List<Object[]> getWorkflowRAGSummaryCountByProductAndResouces(Integer engagementId,Integer productId,Integer entityTypeId,Integer entityParentInstanceId);
	List<Object[]> getWorkflowRAGSummaryCountByProductAndEntityType(Integer engamentId,Integer productId,Integer entityTypeId,Integer entityParentInstanceId);
	List<Object[]> getWorkflowRAGWorkpackageSummaryCount(Integer productId,Integer entityTypeId,Integer entityParentInstanceId);
	List<Object[]> getWorkflowRAGActivityGroupingSummaryCount(Integer engagementId,Integer productId,Integer entityTypeId,Integer parentEntityInstanceId);
	List<Object[]> getWorkflowRAGActivitySummaryCount(Integer productId,Integer entityTypeId);
	List<Object[]> getWorkflowRAGSummarySLADetailView(Integer engagementId,Integer productId,Integer entityTypeId,String indicator,Integer categoryId);
	List<Object[]> listWorkflowRAGStatusInstanceDetail(Integer engagementId,Integer productId, Integer entityTypeId,Integer entityId,Integer parentInstanceId,String indicator,Integer categoryId);
	List<Object[]> getWorkflowRAGByProductAndResoucesDetail(Integer engagementId,Integer productId,Integer entityTypeId,Integer userId,Integer roleId,Integer parentInstanceId,String indicator);
	List<Object[]> getWorkflowRAGSummaryCompleteCountByProductAndEntityType(Integer engagementId,Integer productId,Integer entityTypeId,Integer entityParentInstanceId);
	List<Object[]> getWorkflowRAGSummaryAbortCountByProductAndEntityType(Integer engagementId,Integer productId,Integer entityTypeId,Integer entityParentInstanceId);
	

	List<Object[]> getWorkflowRAGSummarySLADetailAbortStageView(Integer engagementId,Integer productId,Integer entityTypeId,String indicator,Integer categoryId);
	List<Object[]> getWorkflowRAGSummarySLADetailCompletedStageView(Integer engagementId,Integer productId,Integer entityTypeId,String indicator,Integer categoryId);
	
	List<Object[]> getRAGviewWorkpackageSummaryCompletedCount(Integer productId,Integer entityTypeId,Integer entityParentInstanceId);
	List<Object[]> getRAGviewWorkpackageSummaryAbortedCount(Integer productId,Integer entityTypeId,Integer entityParentInstanceId);
	
	List<Object[]> getRAGSummaryCompletedCountByProductAndResouces(Integer engagementId,Integer productId,Integer entityTypeId,Integer entityParentInstanceId);
	List<Object[]> getRAGSummaryAbortedCountByProductAndResouces(Integer engagementId,Integer productId,Integer entityTypeId,Integer entityParentInstanceId);
	
	List<Object[]> getRAGViewActivityGroupingSummaryCompletedCount(Integer engagmentId,Integer productId,Integer entityTypeId,Integer entityParentInstanceId);
	List<Object[]> getRAGViewActivityGroupingSummaryAbortCount(Integer engagmentId,Integer productId,Integer entityTypeId,Integer entityParentInstanceId);
	
	List<Object[]> listWorkflowStatusCategorySummaryCount(Integer engagmentId,Integer productId, Integer entityTypeId, Integer entityId, Integer entityInstanceId);
	List<Object[]> getWorkflowRAGEngagementSummaryCount(Integer engagementId,Integer productId, Integer entityTypeId,Integer entityParentInstanceId);
	
	List<Object[]> getRAGViewEngagementSummaryCompletedCount(Integer engagementId,Integer productId,Integer entityTypeId,Integer entityParentInstanceId);
	List<Object[]> getRAGViewEngagementSummaryAbortCount(Integer engagementId,Integer productId,Integer entityTypeId,Integer entityParentInstanceId);
	
	List<Object[]> getWorkflowRAGActivityGroupingSummaryTotalCount(Integer engagementId, Integer productId, Integer entityTypeId,Integer parentEntityInstanceId);
	
	List<Object[]> getWorkflowRAGActivityCategoriesSummaryCount(Integer engagementId,Integer productId, Integer entityTypeId,Integer entityParentInstanceId);
	List<Object[]> getWorkflowRAGActivityCategoriesSummaryAbortCount(Integer engagementId,Integer productId, Integer entityTypeId,Integer entityParentInstanceId);
	List<Object[]> getWorkflowRAGActivityCategoriesSummaryCompletedCount(Integer engagementId,Integer productId, Integer entityTypeId,Integer entityParentInstanceId);
	List<Object[]> getPendingWithInstanceActorsTestFactoryLevel(Integer testFactoryId,Integer productId, Integer entityTypeId, String entityInstanceId, Integer entityId, HashMap<String, String> pendingWithQueryDetails);
	List<Object[]> getPendingWithActors(Integer productId, Integer entityTypeId, Integer entityId, Integer entityInstanceId, Integer targetStatusId);
	
	List<Object[]> getBeginStatusActivities(Integer productId, Integer entityTypeId);
	
	List<Object[]> getBeginStageActivitiesForWeeklyReport(Integer productId, Integer entityTypeId);
	
	List<Object[]> getIntermediateStageActivitiesForWeeklyReport(Integer productId, Integer entityTypeId);
	
	List<Object[]> getEndStageActivitiesForWeeklyReport(Integer productId, Integer entityTypeId);
	
	List<Object[]> getWorkpackageLevelActivitiesCountForWeekly(Integer productId, Integer entityTypeId);
	
	List<Object[]> getAttentionActivitiesForWeeklyReport(Integer productId, Integer entityTypeId);
	
	List<Object[]> getBeginStageActivitiesForMonthlyReport(Integer productId, Integer entityTypeId);
	
	List<Object[]> getIntermediateStageActivitiesForMonthlyReport(Integer productId, Integer entityTypeId);
	
	List<Object[]> getEndStageActivitiesForMonthlyReport(Integer productId, Integer entityTypeId);
	
	List<Object[]> getCustomFieldValuesByentityTypeAndInstanceIds(Integer entityId, Integer activityTypeId, Integer entityInstanceId);
 
}
