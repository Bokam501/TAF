/**
 * 
 */
package com.hcl.ilcm.workflow.dao.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.hcl.atf.taf.constants.IDPAConstants;
import com.hcl.atf.taf.model.StatusCategory;
import com.hcl.ilcm.workflow.dao.WorkflowStatusPolicyDAO;
import com.hcl.ilcm.workflow.model.WorkflowMaster;
import com.hcl.ilcm.workflow.model.WorkflowStatus;
import com.hcl.ilcm.workflow.model.WorkflowStatusActor;
import com.hcl.ilcm.workflow.model.WorkflowStatusPolicy;

/**
 * @author silambarasur
 *
 */
@Repository
public class WorkflowStatusPolicyDAOImpl implements WorkflowStatusPolicyDAO {
private static final Log log = LogFactory.getLog(WorkflowStatusPolicyDAOImpl.class);
	
	@Autowired(required=true)
    private SessionFactory sessionFactory;
	
	@Override
	@Transactional
	public void addWorkflowStatusPolicy(WorkflowStatusPolicy workflowStatusPolicies,Integer workflowId) {
		try {
			WorkflowStatus workflowStatus=new WorkflowStatus();
			WorkflowMaster workflowMaster = new WorkflowMaster();
			StatusCategory statusCategory = new StatusCategory();
			workflowMaster.setWorkflowId(workflowId);
			workflowStatus=workflowStatusPolicies.getWorkflowStatus();
			if(workflowStatusPolicies.getWorkflowStatus() !=null && workflowStatusPolicies.getWorkflowStatus().getStatusCategory() != null) {
				statusCategory = workflowStatusPolicies.getWorkflowStatus().getStatusCategory();
			}
			workflowStatus.setStatusCategory(statusCategory);
			workflowStatus.setActiveStatus(1);
			workflowStatus.setWorkflow(workflowMaster);
			sessionFactory.getCurrentSession().save(workflowStatus);
			workflowStatusPolicies.setWorkflowStatus(workflowStatus);
			sessionFactory.getCurrentSession().save(workflowStatusPolicies);
		}catch(Exception e) {
			log.error("Error in save WorkflowStatusPolicies", e);
		}
		
	}
	
	@Override
	@Transactional
	public void updateWorkflowStatusPolicy(WorkflowStatusPolicy workflowStatusPolicies,Integer workflowId) {
		try {
			
			WorkflowStatus workflowStatus=workflowStatusPolicies.getWorkflowStatus();
			WorkflowMaster workflowMaster = new WorkflowMaster();
			workflowMaster.setWorkflowId(workflowId);
			workflowStatus.setWorkflow(workflowMaster);
			sessionFactory.getCurrentSession().saveOrUpdate(workflowStatus);
			sessionFactory.getCurrentSession().saveOrUpdate(workflowStatusPolicies);
		}catch(Exception e) {
			log.error("Error in update WorkflowStatusPolicies", e);
		}
	}
	
	@Override
	@Transactional
	public List<WorkflowStatusPolicy> getWorkflowStatusPolicies(Integer workflowId,Integer entityTypeId,Integer entityId, Integer entityInstanceId, Integer levelId,Integer activeStatus, String statusPolicyType) {
		log.debug("Inside getWorkflowStatusPolicies with parameters workflowId - "+workflowId+", entityTypeId - "+entityTypeId+", entityId - "+entityId+", entityInstanceId - "+entityInstanceId+", levelId - "+levelId+", activeStatus - "+activeStatus+", statusPolicyType - "+statusPolicyType);
		List<WorkflowStatusPolicy> workflowPolicyList=null;
		try {
			Criteria statusPolicyCriteria = sessionFactory.getCurrentSession().createCriteria(WorkflowStatusPolicy.class, "wfStatusPolicy");
			statusPolicyCriteria.createAlias("wfStatusPolicy.workflowStatus", "wfStatus");
			statusPolicyCriteria.createAlias("wfStatus.workflow", "workflow");
			statusPolicyCriteria.add(Restrictions.eq("workflow.workflowId", workflowId));
			statusPolicyCriteria.add(Restrictions.eq("wfStatusPolicy.activeStatus", activeStatus));
			statusPolicyCriteria.add(Restrictions.eq("wfStatusPolicy.statusPolicyType", statusPolicyType));
			if(levelId != null){
				statusPolicyCriteria.add(Restrictions.eq("wfStatusPolicy.levelId", levelId));
			}
			
			if (statusPolicyType.equalsIgnoreCase(IDPAConstants.WORKFLOW_STATUS_POLICY_TYPE_WORKFLOW)) {
				statusPolicyCriteria.addOrder(Order.asc("wfStatus.statusOrder"));
				workflowPolicyList=statusPolicyCriteria.list();
			} else if (statusPolicyType.equalsIgnoreCase(IDPAConstants.WORKFLOW_STATUS_POLICY_TYPE_ENTITY)) {
				statusPolicyCriteria.createAlias("wfStatusPolicy.entityType", "entityType");
				statusPolicyCriteria.add(Restrictions.eq("entityType.entitymasterid", entityTypeId));
				if (entityId != null && entityId > 0) {
					statusPolicyCriteria.add(Restrictions.eq("wfStatusPolicy.entityId", entityId));
				}/*else{
					statusPolicyCriteria.add(Restrictions.isNull("wfStatusPolicy.entityId"));
				}*/
				statusPolicyCriteria.add(Restrictions.isNull("wfStatusPolicy.entityInstanceId"));
				statusPolicyCriteria.addOrder(Order.asc("wfStatus.statusOrder"));
				workflowPolicyList=statusPolicyCriteria.list();
			} else if (statusPolicyType.equalsIgnoreCase(IDPAConstants.WORKFLOW_STATUS_POLICY_TYPE_INSTANCE)) {
				statusPolicyCriteria.createAlias("wfStatusPolicy.entityType", "entityType");
				statusPolicyCriteria.add(Restrictions.eq("entityType.entitymasterid", entityTypeId));
				if (entityId != null && entityId > 0) {
					statusPolicyCriteria.add(Restrictions.eq("wfStatusPolicy.entityId", entityId));
				}/*else{
					statusPolicyCriteria.add(Restrictions.isNull("wfStatusPolicy.entityId"));
				}*/
				statusPolicyCriteria.add(Restrictions.eq("wfStatusPolicy.entityInstanceId", entityInstanceId));
				statusPolicyCriteria.addOrder(Order.asc("wfStatus.statusOrder"));
				workflowPolicyList=statusPolicyCriteria.list();
			}  

			return workflowPolicyList;
			
		}catch(Exception e) {
			
		}
		return workflowPolicyList;
	}

	@Override
	@Transactional
	public void addClonedStatusPolicies(WorkflowStatusPolicy workflowStatusPolicy) {
		try {
			log.info("Inside addClonedStatusPolicies");
			sessionFactory.getCurrentSession().saveOrUpdate(workflowStatusPolicy);
		}catch(Exception e) {
			log.error("Error in addClonedStatusPolicies", e);
		}
	}

	@Override
	@Transactional
	public List<WorkflowStatusActor> getWorkflowStatusActor(Integer productId, Integer workflowStatusId, Integer entityTypeId, Integer entityId, Integer entityInstanceId, String actorType, Integer userId, Integer roleId) {
		List<WorkflowStatusActor> workflowStatusActors = new ArrayList<WorkflowStatusActor>();
		try {
			log.debug(" getWorkflowStatusActor with parameters productId - "+productId+", workflowStatusId - "+workflowStatusId+", entityTypeId - "+entityTypeId+", entityId - "+entityId+", entityInstanceId - "+entityInstanceId+", actorType - "+actorType+", userId - "+userId+", roleId - "+roleId);
			Criteria criteria = sessionFactory.getCurrentSession().createCriteria(WorkflowStatusActor.class, "workflowStatusActor");
			if(workflowStatusId != null){
				criteria.add(Restrictions.eq("workflowStatusActor.workflowStatus.workflowStatusId", workflowStatusId));
			}
			criteria.add(Restrictions.eq("workflowStatusActor.entityType.entitymasterid", entityTypeId));
			if(productId != null && productId > 0){
				criteria.add(Restrictions.eq("workflowStatusActor.product.productId", productId));
			}
			if(entityId != null){
				criteria.add(Restrictions.eq("workflowStatusActor.entityId", entityId));
			}else{
				criteria.add(Restrictions.isNull("workflowStatusActor.entityId"));
			}
			if(entityInstanceId != null){
				criteria.add(Restrictions.eq("workflowStatusActor.entityInstanceId", entityInstanceId));
			}else{
				criteria.add(Restrictions.isNull("workflowStatusActor.entityInstanceId"));
			}
			if(IDPAConstants.WORKFLOW_STATUS_POLICY_USER_ACTION_SCOPE.equalsIgnoreCase(actorType)){
				criteria.add(Restrictions.isNotNull("workflowStatusActor.user.userId"));
				if(userId != null){
					criteria.add(Restrictions.eq("workflowStatusActor.user.userId", userId));
				}
			}else if(IDPAConstants.WORKFLOW_STATUS_POLICY_ROLE_ACTION_SCOPE.equalsIgnoreCase(actorType)){
				criteria.add(Restrictions.isNotNull("workflowStatusActor.role.userRoleId"));
				if(roleId != null){
					criteria.add(Restrictions.eq("workflowStatusActor.role.userRoleId", roleId));
				}
			}
			workflowStatusActors = criteria.list();
		}catch(Exception e) {
			log.error("Error in getWorkflowStatusActor", e);
		}
		return workflowStatusActors;
	}
	
	@Override
	@Transactional
	public void addWorkflowStatusActor(WorkflowStatusActor workflowStatusActor) {
		try {
			log.info("Inside addWorkflowStatusActor");
			sessionFactory.getCurrentSession().saveOrUpdate(workflowStatusActor);
		}catch(Exception e) {
			log.error("Error in addWorkflowStatusActor", e);
		}
	}

	@Override
	@Transactional
	public boolean checkWorkflowStatusActorAlreadyExist(Integer productId, Integer workflowStatusActorId, Integer workflowStatusId, Integer entityTypeId, Integer entityId, Integer entityInstanceId, Integer userId, Integer roleId) {
		boolean isMappingAlreadyExist = false;
		log.info("Inside checkWorkflowStatusPolicyActorAlreadyExist with parameters productId - "+productId+", workflowStatusActorId - "+workflowStatusActorId+", workflowStatusId - "+workflowStatusId+", entityTypeId - "+entityTypeId+", entityId - "+entityId+", entityInstanceId - "+entityInstanceId+", userId - "+userId+", roleId - "+roleId);
		List<WorkflowStatusActor> workflowStatusActors = null; 
		try{
			Criteria criteria = sessionFactory.getCurrentSession().createCriteria(WorkflowStatusActor.class, "wfStatusActor");
			criteria.createAlias("wfStatusActor.workflowStatus", "wfStatus");
			criteria.add(Restrictions.eq("wfStatus.workflowStatusId", workflowStatusId));
			criteria.add(Restrictions.eq("wfStatusActor.entityType.entitymasterid", entityTypeId));
			if(productId != null && productId > 0){
				criteria.add(Restrictions.eq("wfStatusActor.product.productId", productId));
			}
			if(entityId != null){
				criteria.add(Restrictions.eq("wfStatusActor.entityId", entityId));
			}else{
				criteria.add(Restrictions.isNull("wfStatusActor.entityId"));
			}
			if(entityInstanceId != null){
				criteria.add(Restrictions.eq("wfStatusActor.entityInstanceId", entityInstanceId));
			}else{
				criteria.add(Restrictions.isNull("wfStatusActor.entityInstanceId"));
			}
			if(userId != null){
				criteria.createAlias("wfStatusActor.user", "user");
				criteria.add(Restrictions.eq("user.userId", userId));
			}
			if(roleId != null){
				criteria.createAlias("wfStatusActor.role", "role");
				criteria.add(Restrictions.eq("role.userRoleId", roleId));
			}
			if(workflowStatusActorId != null){
				criteria.add(Restrictions.ne("wfStatusActor.workflowStatusActorId", workflowStatusActorId));
			}
			workflowStatusActors = criteria.list();
			if(workflowStatusActors != null && workflowStatusActors.size() > 0){
				isMappingAlreadyExist = true;
			}
			
		}catch(Exception ex){
			log.error("Error inside checkWorkflowStatusPolicyActorAlreadyExist - ", ex);
		}
		return isMappingAlreadyExist;
	}

	@Override
	@Transactional
	public void deleteWorkflowStatusActor(WorkflowStatusActor workflowStatusActor) {
		try {
			log.info("Inside deleteWorkflowStatusActor");
			sessionFactory.getCurrentSession().delete(workflowStatusActor);
		}catch(Exception e) {
			log.error("Error in deleteWorkflowStatusActor", e);
		}
	}

	@Override
	@Transactional
	public WorkflowStatusPolicy getWorkflowStatusPolicieByWorkflowStatusId(Integer workflowStatusId, Integer entityTypeId, Integer entityId, Integer entityInstanceId, Integer levelId, Integer activeStatus, String statusPolicyType) {

		WorkflowStatusPolicy workflowStatusPolicy = null;
		try {
			Criteria statusPolicyCriteria = sessionFactory.getCurrentSession().createCriteria(WorkflowStatusPolicy.class, "wfStatusPolicy");
			statusPolicyCriteria.createAlias("wfStatusPolicy.workflowStatus", "wfStatus");
			statusPolicyCriteria.add(Restrictions.eq("wfStatus.workflowStatusId", workflowStatusId));
			statusPolicyCriteria.add(Restrictions.eq("wfStatusPolicy.activeStatus", activeStatus));
			statusPolicyCriteria.add(Restrictions.eq("wfStatusPolicy.statusPolicyType", statusPolicyType));
			if(levelId != null){
				statusPolicyCriteria.add(Restrictions.eq("wfStatusPolicy.levelId", levelId));
			}
			
			if (statusPolicyType.equalsIgnoreCase(IDPAConstants.WORKFLOW_STATUS_POLICY_TYPE_WORKFLOW)) {
				statusPolicyCriteria.addOrder(Order.asc("wfStatus.statusOrder"));
			} else if (statusPolicyType.equalsIgnoreCase(IDPAConstants.WORKFLOW_STATUS_POLICY_TYPE_ENTITY)) {
				statusPolicyCriteria.createAlias("wfStatusPolicy.entityType", "entityType");
				statusPolicyCriteria.add(Restrictions.eq("entityType.entitymasterid", entityTypeId));
				if (entityId != null && entityId > 0) {
					statusPolicyCriteria.add(Restrictions.eq("wfStatusPolicy.entityId", entityId));
				}
				statusPolicyCriteria.addOrder(Order.asc("wfStatus.statusOrder"));
			} else if (statusPolicyType.equalsIgnoreCase(IDPAConstants.WORKFLOW_STATUS_POLICY_TYPE_INSTANCE)) {
				statusPolicyCriteria.createAlias("wfStatusPolicy.entityType", "entityType");
				statusPolicyCriteria.add(Restrictions.eq("entityType.entitymasterid", entityTypeId));
				if (entityId != null && entityId > 0) {
					statusPolicyCriteria.add(Restrictions.eq("wfStatusPolicy.entityId", entityId));
				}
				statusPolicyCriteria.add(Restrictions.eq("wfStatusPolicy.entityInstanceId", entityInstanceId));
				statusPolicyCriteria.addOrder(Order.asc("wfStatus.statusOrder"));
				
			}  
			List<WorkflowStatusPolicy> workflowPolicyList = statusPolicyCriteria.list();
			log.info("getWorkflowStatusPolicies End");
			if(workflowPolicyList != null && workflowPolicyList.size() > 0){
				workflowStatusPolicy = workflowPolicyList.get(0);
			}
			
		}catch(Exception e) {
			log.error("Error while getting workflow status policy using workflow status id", e);
		}
		return workflowStatusPolicy;
	}

	@Override
	@Transactional
	public boolean checkMandatoryUserActionsToChangeStatus(Integer productId, Integer sourceStatusId, Integer entityTypeId, Integer entityId, Integer entityInstanceId, String workflowStatusPolicyUserActionScope, Integer userId, Integer roleId) {
		boolean isStatusChanegAllowed = false;
		try {
			log.info("Inside checkMandatoryUserActionsToChangeStatus with parameters productId - "+productId+", sourceStatusId - "+sourceStatusId+", entityTypeId - "+entityTypeId+", entityId - "+entityId+", entityInstanceId - "+entityInstanceId+", userId - "+userId+", roleId - "+roleId);
			
			Criteria criteria = sessionFactory.getCurrentSession().createCriteria(WorkflowStatusActor.class, "workflowStatusActor");
			criteria.add(Restrictions.eq("workflowStatusActor.workflowStatus.workflowStatusId", sourceStatusId));
			criteria.add(Restrictions.eq("workflowStatusActor.entityType.entitymasterid", entityTypeId));
			if(productId != null && productId > 0){
				criteria.add(Restrictions.eq("workflowStatusActor.product.productId", productId));
			}
			if(entityId != null){
				criteria.add(Restrictions.eq("workflowStatusActor.entityId", entityId));
			}else{
				criteria.add(Restrictions.isNull("workflowStatusActor.entityId"));
			}
			if(entityInstanceId != null){
				criteria.add(Restrictions.eq("workflowStatusActor.entityInstanceId", entityInstanceId));
			}else{
				criteria.add(Restrictions.isNull("workflowStatusActor.entityInstanceId"));
			}
			if(userId != null){
				criteria.add(Restrictions.eq("workflowStatusActor.user.userId", userId));
			}
			if(roleId != null){
				criteria.add(Restrictions.eq("workflowStatusActor.role.userRoleId", roleId));
			}
			criteria.add(Restrictions.eq("workflowStatusActor.actionRequirement", IDPAConstants.WORKFLOW_STATUS_USER_ACTION_MANDATORY));
			criteria.add(Restrictions.ne("workflowStatusActor.userActionStatus", IDPAConstants.WORKFLOW_STATUS_USER_ACTION_COMPELTED));
			
			Integer userCount = ((Long)criteria.setProjection(Projections.rowCount()).uniqueResult()).intValue();
			if(userCount == 0){
				isStatusChanegAllowed = true;
			}
			log.info("Is status change allowed - "+isStatusChanegAllowed);
		}catch(Exception e) {
			log.error("Error in checkMandatoryUserActionsToChangeStatus", e);
		}
		return isStatusChanegAllowed;
	
	}

	@Override
	@Transactional
	public void updateInstanceEntityMappingAndDeleteStatusPolicies(Integer entityTypeId, Integer entityId, Integer entityInstanceId, Integer workflowId) {
		log.info("Inside updateInstanceEntityMappingAndDeleteStatusPolicies with parameters : entityTypeId - "+entityTypeId+", entityId - "+entityId+", entityInstanceId - "+entityInstanceId);
		try{
			String constraints = " where entityTypeId = "+entityTypeId;
			if(entityId != null){
				constraints += " and entityId = "+entityId;
			}else{
				constraints += " and entityId IS NULL";
			}
			constraints += " and entityInstanceId = "+entityInstanceId;
			
			String tableName = "wf_workflows_entity_mapping";
			String operationQuery = "update "+tableName+" set workflowId = "+workflowId;
			
			String queryToExecute = operationQuery+constraints;
			sessionFactory.getCurrentSession().createSQLQuery(queryToExecute).executeUpdate();
			
			operationQuery = "delete from ";
			tableName = "wf_workflows_status_policies";
			queryToExecute = operationQuery+tableName+constraints;
			sessionFactory.getCurrentSession().createSQLQuery(queryToExecute).executeUpdate();
			
			tableName = "wf_entity_instance_status_actors_mapping";
			queryToExecute = operationQuery+tableName+constraints;
			sessionFactory.getCurrentSession().createSQLQuery(queryToExecute).executeUpdate();
			
			operationQuery = "delete from ";
			tableName = "wf_workflow_events";
			queryToExecute = operationQuery+tableName+constraints;
			sessionFactory.getCurrentSession().createSQLQuery(queryToExecute).executeUpdate();
			
		}catch(Exception ex){
			log.error("Error inside updateInstanceEntityMappingAndDeleteStatusPolicies - ", ex);
		}
	}
	
	@Override
	@Transactional
	public List<Object[]> listWorkflowStatusSummary(Integer engagmentId,Integer productId, Integer entityTypeId, Integer entityId, Integer parentEntityInstanceId) {
		List<Object[]> workflowstatusList=null;
		try{
		String sql="SELECT wfstatus.workflowStatusDisplayName, COUNT(DISTINCT policy.entityInstanceId),wfstatus.workflowStatusId,"+
				" policy.entityId,activityMasterName,act.activityMasterId FROM wf_workflows_status_policies policy "+
				" LEFT JOIN product_master product ON (product.productId=policy.levelId) "+
				" LEFT JOIN activity act ON (act.activityId=policy.entityInstanceId AND act.workflowStatusId=policy.workflowStatusId)"+ 
				" LEFT JOIN activity_work_package wp ON (act.activityWorkPackageId=wp.activityWorkPackageId)  "+
				" LEFT JOIN wf_workflows_status wfstatus ON (wfstatus.workflowStatusId=policy.workflowStatusId) "+
				" LEFT JOIN activity_master actType ON actType.activityMasterId=act.activityMasterId "+
				" LEFT JOIN test_factory tf ON (tf.testFactoryId=product.testFactoryId) "+ 
				" WHERE act.workflowStatusId > 0";	
		if(engagmentId != null && engagmentId != 0){
			sql += " and tf.testFactoryId = "+engagmentId;
		}
		if(productId != null && productId != 0){
			sql += " AND product.productId = "+productId;
		}
		if(entityId != null && entityId != 0){
			sql += " and policy.entityId = "+entityId;
		}
		if(parentEntityInstanceId != null && parentEntityInstanceId != 0){
			sql += " and act.activityWorkPackageId = "+parentEntityInstanceId;
		}
		sql += " GROUP BY wfstatus.workflowstatusId,act.activitymasterId,policy.entityid,acttype.activitymastername ORDER BY wfstatus.workflowStatusId,wfstatus.workflowStatusDisplayName";
		workflowstatusList=sessionFactory.getCurrentSession().createSQLQuery(sql).list();
		
		}catch(Exception e) {
			log.error("Error in listWorkflowStatusTaskSummary", e);
		}
		return workflowstatusList;
	}
	
	@Override
	@Transactional
	public Integer getWorkflowStatusSummaryPaginationCount(Integer productId, Integer entityTypeId, Integer entityId, Integer entityInstanceId) {
		log.info("Inside listWorkflowStatusSummary with parameters : productId - "+productId+", entityTypeId - "+entityTypeId+", entityId - "+entityId+", entityInstanceId - "+entityInstanceId);
		Integer paginationCount = 0;
		try{
			
			String sql = "SELECT workflowStatusDisplayName, COUNT(DISTINCT entityInstanceId) FROM wf_workflow_status_actors_users_view WHERE workflowStatusId = currentStatusId AND currentStatusId > 0 AND workflowStatusType <> 'End' AND entityTypeId = "+entityTypeId;
			if(entityId != null && entityId != 0){
				sql += " entityId = "+entityId;
			}
			if(entityInstanceId != null && entityInstanceId != 0){
				sql += " entityInstanceId = "+entityInstanceId;
			}
			sql += " AND productId = "+productId+" GROUP BY currentStatusId ORDER BY workflowStatusId";
					
			List<Object[]> workflowstatusList =sessionFactory.getCurrentSession().createSQLQuery(sql).list();
			if(workflowstatusList != null && workflowstatusList.size() > 0){
				paginationCount = workflowstatusList.size();
			}
		}catch(Exception e) {
			log.error("Error in listWorkflowStatusTaskSummary", e);
		}
		return paginationCount;
	}

	@Override
	@Transactional
	public List<Object[]> listWorkflowStatusActorSummary(Integer engagementId,Integer productId, Integer entityTypeId, Integer entityId, Integer parentEntityInstanceId, String actorType) {
		List<Object[]> workflowstatusList=null;
		try{
			String actorNameColumn = "userName";
			if("Role".equalsIgnoreCase(actorType)){
				actorNameColumn = "roleName";
			}
			String statusActorSummaryQuery = "SELECT workflowStatusDisplayName, "+actorNameColumn+", COUNT(DISTINCT entityInstanceId),workflowStatusId,entityId,userId,roleId,typeName FROM wf_workflow_status_actors_users_view actorView,product_master product,test_factory tf WHERE tf.testFactoryId=product.testFactoryId and product.productId=actorView.productId ";
			if(engagementId != null && engagementId != 0){
				statusActorSummaryQuery += " and tf.testFactoryId = "+engagementId;
			}
			if(productId != null && productId != 0){
				statusActorSummaryQuery += " and actorView.productId = "+productId;
			}
			if(entityTypeId != null && entityTypeId != 0){
				statusActorSummaryQuery += " and entityTypeId = "+entityTypeId;
			}
			if(entityId != null && entityId != 0){
				statusActorSummaryQuery += " and entityId = "+entityId;
			}
			if(parentEntityInstanceId != null && parentEntityInstanceId != 0){
				statusActorSummaryQuery += " and actorView.parentEntityInstanceId = "+parentEntityInstanceId;
			}
			statusActorSummaryQuery +=	" AND workflowStatusId = currentStatusId AND currentStatusId > 0 AND workflowStatusType <> 'End' AND actorMappingType = '"+actorType+"' AND userActionStatus <> 'Completed'"
					+ " GROUP BY workflowStatusName,typeId,actorview.workflowstatusdisplayname, actorview.workflowstatusid, actorview.entityid, actorview.userid, actorview.roleid, actorview.typename, "+actorNameColumn
					+ " ORDER BY workflowStatusId";
			
			workflowstatusList=sessionFactory.getCurrentSession().createSQLQuery(statusActorSummaryQuery).list();
		}catch(Exception e) {
			log.error("Error in listWorkflowStatusActorSummary", e);
		}
		return workflowstatusList;
	}
	
	@Override
	@Transactional
	public Integer getWorkflowStatusActorSummaryPaginationCount(Integer productId, Integer entityTypeId, Integer entityId, Integer entityInstanceId, String actorType) {
		log.info("Inside listWorkflowStatusActorSummary with parameters : productId - "+productId+", entityTypeId - "+entityTypeId+", entityId - "+entityId+", entityInstanceId - "+entityInstanceId+", actorType - "+actorType);
		Integer paginationCount = 0;
		try{
			String actorNameColumn = "userName";
			if("Role".equalsIgnoreCase(actorType)){
				actorNameColumn = "roleName";
			}
			String statusActorSummaryQuery = "SELECT count(*) FROM wf_workflow_status_actors_users_view"
					+ " WHERE productId = "+productId+" AND entityTypeId = "+entityTypeId;
			if(entityId != null && entityId != 0){
				statusActorSummaryQuery += " entityId = "+entityId;
			}
			if(entityInstanceId != null && entityInstanceId != 0){
				statusActorSummaryQuery += " entityInstanceId = "+entityInstanceId;
			}
			statusActorSummaryQuery +=	" AND workflowStatusId = currentStatusId AND currentStatusId > 0 AND workflowStatusType <> 'End' AND actorMappingType = '"+actorType+"' AND userActionStatus <> 'Completed'"
					+ " GROUP BY workflowStatusName, "+actorNameColumn
					+ " ORDER BY workflowStatusId";
			
			List<Object[]> workflowstatusList = sessionFactory.getCurrentSession().createSQLQuery(statusActorSummaryQuery).list();
			if(workflowstatusList != null && workflowstatusList.size() > 0){
				paginationCount = workflowstatusList.size();
			}
		}catch(Exception e) {
			log.error("Error in listWorkflowStatusActorSummary", e);
		}
		return paginationCount;
	}	
	
	@Override
	@Transactional
	public List<Object[]> getPendingWithInstanceActors(Integer productId,Integer entityTypeId,Integer entityId) {
		List<Object[]> pendingInstanceActors=null;
		try{
		String sql="SELECT entityInstanceId,GROUP_CONCAT(CASE actionRequirement WHEN 'Mandatory' THEN CONCAT(userName,'[M]') ELSE username END SEPARATOR ', '), GROUP_CONCAT(CAST(roleLabel AS text) SEPARATOR ', '), actionScope,GROUP_CONCAT(CAST(userId AS text) SEPARATOR ','),GROUP_CONCAT(CAST( roleId AS text) SEPARATOR ',') FROM `wf_workflow_status_actors_users_view` WHERE productId="+productId +" AND entityTypeId="+entityTypeId;
		if(entityId !=null && entityId != 0){
			sql+=" AND entityId="+entityId;
		}
		sql+=" AND workflowStatusId = currentStatusId AND currentStatusId > 0 AND userActionStatus <> 'Completed'	GROUP BY workflowStatusName,entityInstanceId";
		pendingInstanceActors=sessionFactory.getCurrentSession().createSQLQuery(sql).list();
		}catch(Exception e) {
			log.error("Error in getPendingWithInstanceActors"+e);
		}
		return pendingInstanceActors;
	}
	
	@Override
	@Transactional
	public List<Object[]> getPendingWithActors(Integer productId,Integer entityTypeId,Integer entityId, Integer entityInstanceId, Integer targetStatusId) {
		List<Object[]> pendingInstanceActors=null;
		try{
		String sql="SELECT distinct userId, entityTypeName, instanceName FROM wf_workflow_status_actors_users_view WHERE productId="+productId +" AND entityTypeId="
		+entityTypeId +" AND entityInstanceId="+entityInstanceId+" AND currentStatusId="+targetStatusId;
		if(entityId !=null && entityId != 0){
			sql+=" AND entityId="+entityId;
		}
		sql+=" AND workflowStatusId = currentStatusId AND currentStatusId > 0 AND userActionStatus <> 'Completed'";
		pendingInstanceActors=sessionFactory.getCurrentSession().createSQLQuery(sql).list();
		}catch(Exception e) {
			log.error("Error in getPendingWithActors"+e);
		}
		return pendingInstanceActors;
	}

	@Override
	@Transactional
	public List<Object[]> getPendingWithInstanceActorsTestFactoryLevel(Integer testFactoryId,Integer productId,Integer entityTypeId, String entityInstanceId, Integer entityId, HashMap<String, String> pendingWithQueryDetails) {
		List<Object[]> pendingInstanceActors=null;
		try{
			String joinColumn = "";
			String workflowStatusCheck = "";
			if(pendingWithQueryDetails != null){
				if(pendingWithQueryDetails.containsKey("joinColumn")){
					joinColumn = pendingWithQueryDetails.get("joinColumn");
				}
				
				if(pendingWithQueryDetails.containsKey("workflowStatusCheck")){
					workflowStatusCheck = pendingWithQueryDetails.get("workflowStatusCheck");
				}
			}
			
			String sql="SELECT actorMapping.entityInstanceId,string_agg(CASE actorMapping.actionRequirement WHEN 'Mandatory' THEN CONCAT(users.loginId,'[M]') ELSE users.loginId END, ', ') As UserList," 
					+" string_agg(CAST(role.roleLabel AS text), ', ') As RoleList, policy.actionScope,string_agg(CAST(users.userId AS text), ',') As userIds,string_agg(CAST(role.userRoleId AS text), ',') As userRoleIds" 
					+" FROM wf_entity_instance_status_actors_mapping actorMapping" 
					+joinColumn
					+" LEFT JOIN product_master product ON product.productId=actorMapping.productId"
					+" LEFT JOIN test_factory tf ON tf.testFactoryId=product.testFactoryId" 
					+" LEFT JOIN user_list users ON actorMapping.userId = users.userId"
					+" LEFT JOIN user_role_master role ON role.userRoleId = actorMapping.roleId"
					+" LEFT JOIN wf_workflows_status_policies policy ON (actorMapping.entityInstanceId = policy.entityInstanceId AND actorMapping.entityTypeId=policy.entityTypeId AND actorMapping.productId=policy.levelId AND actorMapping.workflowStatusId = policy.workflowStatusId)"
					+" WHERE actorMapping.entityInstanceId IS NOT NULL AND actorMapping.entityTypeId="+entityTypeId;
				
			if(testFactoryId !=null && testFactoryId > 0){
				sql += " AND tf.testFactoryId="+testFactoryId;
			}
			if(productId !=null && productId > 0){
				sql+=" AND actorMapping.productId="+productId;
			}
			if(entityInstanceId !=""){
				sql+=" AND actorMapping.entityInstanceId in ("+entityInstanceId+") ";
			}
			if(entityId !=null && entityId > 0){
				sql+=" AND actorMapping.entityId="+entityId;
			}	
			sql += " AND actorMapping.workflowStatusId > 0 "+workflowStatusCheck+" AND actorMapping.userActionStatus <> 'Completed'"
				+" GROUP BY actorMapping.workflowStatusId,actorMapping.entityInstanceId,policy.actionScope";
			
			pendingInstanceActors=sessionFactory.getCurrentSession().createSQLQuery(sql).list();
		}catch(Exception e) {
			log.error("Error in getPendingWithInstanceActorsTestFactoryLevel"+e);
		}
		return pendingInstanceActors;
	}
	
	@Override
	@Transactional
	public List<Object[]> getWorkflowStatusSummaryCountByProduct(Integer engagementId,Integer productId,Integer entityTypeId,Integer parentEntityInstanceId) {
		List<Object[]> statusSummaryCountList=null;
		try{
			String sql="SELECT entityTypeId,entity.entitymastername,ABS(FLOOR(extract(epoch from(lastUpdatedDate+(slaDuration * INTERVAL '1 HOUR')-NOW()))/3600 / 3600)) AS remaingHours,"+
					   " CASE WHEN ABS(FLOOR(extract(epoch from(lastUpdatedDate+(slaDuration * INTERVAL '1 HOUR')-NOW()))/3600 / 3600)) >=24 THEN 'green' WHEN ABS(FLOOR(extract(epoch from(lastUpdatedDate+(slaDuration * INTERVAL '1 HOUR')-NOW()))/3600 / 3600)) <24"+
					   " AND ABS(FLOOR(extract(epoch from(lastUpdatedDate+(slaDuration * INTERVAL '1 HOUR')-NOW()))/3600 / 3600))>=2 THEN 'orange' WHEN ABS(FLOOR(extract(epoch from(lastUpdatedDate+(slaDuration * INTERVAL '1 HOUR')-NOW()))/3600 / 3600))<2"+
					   " AND ABS(FLOOR(extract(epoch from(lastUpdatedDate+(slaDuration * INTERVAL '1 HOUR')-NOW()))/3600 / 3600))>0 THEN 'orangered' ELSE 'red' END AS indicator,COUNT(DISTINCT entityInstanceId) AS COUNT,product.productName as productName"+
					   " FROM wf_workflow_status_last_action_view statusView,entitymaster entity,product_master product,test_factory tf WHERE tf.testFactoryId=product.testFactoryId and product.productId=statusView.productId and entity.entitymasterid=statusView.entityTypeId";
			if(engagementId !=null && engagementId!=0) {
				sql+=" and tf.testFactoryId="+engagementId;
			}
			if(productId !=null && productId!=0) {
				sql+=" and statusView.productId="+productId;
			}
			if(parentEntityInstanceId !=null && parentEntityInstanceId!=0) {
				sql+=" and statusView.parentEntityInstanceId="+parentEntityInstanceId;
			}
			
			if(entityTypeId !=null && entityTypeId!=0) {
				sql+=" and entityTypeId="+entityTypeId;
			}
			
			sql+=" GROUP BY statusView.productId,entitytypeid,entity.entitymastername,product.productname,statusview.lastupdateddate,statusview.sladuration,indicator";
			statusSummaryCountList=sessionFactory.getCurrentSession().createSQLQuery(sql).list();
		}catch(Exception e) {
			log.error("Error in getWorkflowStatusSummaryCountByProduct"+e);
		}
	return statusSummaryCountList;
	}
	
	
	@Override
	@Transactional
	public List<Object[]> getWorkflowSummarySLADetailView(Integer engagmentId,Integer productId,Integer entityTypeId,String indicator,Integer parentEntityInstanceId) {
		List<Object[]> statusSummaryCountList=null;
		try{
			String sql="SELECT statusView.entityTypeId,entity.entitymastername,ABS(FLOOR(extract(epoch from(lastUpdatedDate+(slaDuration * INTERVAL '1 HOUR')-NOW()))/3600  / 3600)) AS remaingHours,"+
					   " CASE WHEN ABS(FLOOR(extract(epoch from(lastUpdatedDate+(slaDuration * INTERVAL '1 HOUR')-NOW()))/3600  / 3600)) >=24 THEN 'green' WHEN ABS(FLOOR(extract(epoch from(lastUpdatedDate+(slaDuration * INTERVAL '1 HOUR')-NOW()))/3600  / 3600)) <24"+
					   " AND ABS(FLOOR(extract(epoch from(lastUpdatedDate+(slaDuration * INTERVAL '1 HOUR')-NOW()))/3600  / 3600))>=2 THEN 'orange' WHEN ABS(FLOOR(extract(epoch from(lastUpdatedDate+(slaDuration * INTERVAL '1 HOUR')-NOW()))/3600  / 3600))<2"+
					   " AND ABS(FLOOR(extract(epoch from(lastUpdatedDate+(slaDuration * INTERVAL '1 HOUR')-NOW()))/3600  / 3600))>0 THEN 'orangered' ELSE 'red' END AS indicator,entityInstanceId FROM wf_workflow_status_last_action_view statusView,"+
					   " entitymaster entity ,product_master product,test_factory tf WHERE tf.testFactoryId=product.testFactoryId and product.productId=statusView.productId and entity.entitymasterid=statusView.entityTypeId";
			if(engagmentId !=null && engagmentId!=0) {
				sql+=" and tf.testFactoryId="+engagmentId;
			}
			if(productId !=null && productId!=0) {
				sql+=" and statusView.productId="+productId;
			}
			if(parentEntityInstanceId !=null && parentEntityInstanceId!=0) {
				sql+=" and parentEntityInstanceId="+parentEntityInstanceId;
			}
			
			if(entityTypeId !=null && entityTypeId!=0) {
				sql+=" and entityTypeId="+entityTypeId;
			}
			if(indicator !=null && !indicator.trim().isEmpty()) {
				if("red".equalsIgnoreCase(indicator)){
					sql+=" AND ABS(FLOOR(extract(epoch from(lastUpdatedDate+(slaDuration * INTERVAL '1 HOUR')-NOW()))/3600 / 3600)) <= 0";
				}else if("orange".equalsIgnoreCase(indicator)){
					sql+=" AND ABS(FLOOR(extract(epoch from(lastUpdatedDate+(slaDuration * INTERVAL '1 HOUR')-NOW()))/3600 / 3600)) BETWEEN 1 AND 24";
				}else if("green".equalsIgnoreCase(indicator)){
					sql+=" AND ABS(FLOOR(extract(epoch from(lastUpdatedDate+(slaDuration * INTERVAL '1 HOUR')-NOW()))/3600 / 3600)) >= 24";
				}
			}
			statusSummaryCountList=sessionFactory.getCurrentSession().createSQLQuery(sql).list();
		}catch(Exception e) {
			log.error("Error in getStatusSummaryCountByProduct"+e);
		}
	return statusSummaryCountList;
	}
	
	
	
	@Override
	@Transactional
	public List<Object[]> listWorkflowStatusSLADetail(Integer engagementId, Integer productId, Integer entityTypeId, Integer entityId,Integer workflowStatusId,Integer userId,Integer roleId,Integer entityParentInstanceId) {
		log.info("Inside listWorkflowStatusSummary with parameters : productId - "+productId+", entityTypeId - "+entityTypeId+", entityId - "+entityId+", workflowStatusId - "+workflowStatusId+", userId - "+userId+",roleId - "+roleId);
		List<Object[]> workflowInstanceList=null;
		try{
			
		String sql="SELECT DISTINCT policy.entityInstanceId FROM wf_workflows_status_policies policy"+ 
				" LEFT JOIN product_master product ON (product.productId=policy.levelId) "+
				" LEFT JOIN activity act ON (act.activityId=policy.entityInstanceId AND act.workflowStatusId=policy.workflowStatusId)"+
				" LEFT JOIN user_list users ON act.assigneeId = users.userId"+
				" LEFT JOIN activity_work_package wp ON (act.activityWorkPackageId=wp.activityWorkPackageId)"+
				" LEFT JOIN wf_workflows_status wfstatus ON (wfstatus.workflowStatusId=policy.workflowStatusId)"+ 
				" LEFT JOIN activity_master actType ON actType.activityMasterId=act.activityMasterId "+
				" LEFT JOIN test_factory tf ON (tf.testFactoryId=product.testFactoryId) "+ 
				" WHERE act.workflowStatusId > 0";	
		if(entityId != null && entityId != 0){
			sql += " and policy.entityId = "+entityId;
		}
		if(engagementId !=null && engagementId!=0) {
			sql+=" and tf.testFactoryId="+engagementId;
		}
		
		if(productId !=null && productId!=0) {
			sql+=" AND product.productId ="+productId;
		}
		
		if(workflowStatusId != null && workflowStatusId != 0){
			sql += " and wfstatus.workflowStatusId = "+workflowStatusId;
		}
		if(userId != null && userId != 0){
			sql += " and users.userId = "+userId;
		}
		if(roleId != null && roleId != 0){
			sql += " and users.roleId = "+roleId;
		}
		
		if(entityParentInstanceId != null && entityParentInstanceId >0) {
			sql += " and act.activityWorkPackageId = "+entityParentInstanceId;
		}
		workflowInstanceList=sessionFactory.getCurrentSession().createSQLQuery(sql).list();
		
		}catch(Exception e) {
			log.error("Error in listWorkflowStatusSLADetail", e);
		}
		return workflowInstanceList;
	}
	
	
	@Override
	@Transactional
	public List<Object[]> listWorkflowStatusCategorySLADetail(Integer engagementId, Integer productId, Integer entityTypeId, Integer entityId,Integer workflowStatusId,Integer userId,Integer roleId,Integer workflowStatusCategoryId,Integer entityParentInstanceId) {
		log.info("Inside listWorkflowStatusSummary with parameters : productId - "+productId+", entityTypeId - "+entityTypeId+", entityId - "+entityId+", workflowStatusId - "+workflowStatusId+", userId - "+userId+",roleId - "+roleId);
		List<Object[]> workflowInstanceList=null;
		try{
			
		String sql="SELECT DISTINCT policy.entityInstanceId "+
				" FROM wf_workflows_status_policies policy "+
				" LEFT JOIN product_master product ON (product.productId=policy.levelId)"+ 
				" LEFT JOIN activity act ON (act.activityId=policy.entityInstanceId AND act.workflowStatusId=policy.workflowStatusId)"+ 
				" LEFT JOIN activity_work_package wp ON (act.activityWorkPackageId=wp.activityWorkPackageId) "+
				" LEFT JOIN wf_workflows_status wfstatus ON (wfstatus.workflowStatusId=policy.workflowStatusId)"+ 
				" LEFT JOIN activity_master actType ON actType.activityMasterId=act.activityMasterId"+
				" LEFT JOIN wf_status_category statusCategory ON statusCategory.workflowStatusCategoryId=wfstatus.workflowStatusCategoryId"+
				" LEFT JOIN test_factory tf ON (tf.testFactoryId=product.testFactoryId)  WHERE act.workflowStatusId > 0";	
			
		if(entityId != null && entityId != 0){
			sql += " and policy.entityId = "+entityId;
		}
		if(engagementId !=null && engagementId!=0) {
			sql+=" and tf.testFactoryId="+engagementId;
		}
		
		if(productId !=null && productId!=0) {
			sql+=" AND product.productId ="+productId;
		}
		
		if(entityTypeId !=null && entityTypeId!=0) {
			sql+=" AND policy.entityTypeId="+entityTypeId;
		}
		if(workflowStatusId != null && workflowStatusId != 0){
			sql += " and act.workflowStatusId = "+workflowStatusId;
		}
		
		if(workflowStatusCategoryId != null && workflowStatusCategoryId!=0) {
			sql += " and statusCategory.workflowStatusCategoryId = "+workflowStatusCategoryId;
			
		}
		if(userId != null && userId != 0){
			sql += " and userId = "+userId;
		}
		if(roleId != null && roleId != 0){
			sql += " and roleId = "+roleId;
		}
		
		if(entityParentInstanceId != null && entityParentInstanceId >0) {
			sql += " and act.activityWorkPackageId = "+entityParentInstanceId;
		}
		workflowInstanceList=sessionFactory.getCurrentSession().createSQLQuery(sql).list();
		
		}catch(Exception e) {
			log.error("Error in listWorkflowStatusSLADetail", e);
		}
		return workflowInstanceList;
	}

	@Override
	@Transactional
	public WorkflowStatusPolicy getSLADurationForEntityOrInstance(Integer entityTypeId, Integer entityId, Integer entityInstanceId, Integer sourceStatusId) {
		WorkflowStatusPolicy workflowStatusPolicy = null;
		try{
			Criteria criteria = sessionFactory.getCurrentSession().createCriteria(WorkflowStatusPolicy.class, "workflowStatusPolicy");
			criteria.add(Restrictions.eq("workflowStatusPolicy.entityType.entitymasterid", entityTypeId));
			if(entityId != null){
				criteria.add(Restrictions.eq("workflowStatusPolicy.entityId", entityId));
			}else{
				criteria.add(Restrictions.isNull("workflowStatusPolicy.entityId"));
			}
			if(entityInstanceId != null){
				criteria.add(Restrictions.eq("workflowStatusPolicy.entityInstanceId", entityInstanceId));
			}else{
				criteria.add(Restrictions.isNull("workflowStatusPolicy.entityInstanceId"));
			}
			criteria.add(Restrictions.eq("workflowStatusPolicy.workflowStatus.workflowStatusId", sourceStatusId));
			List<WorkflowStatusPolicy> workflowStatusPolicies = criteria.list();
			if(workflowStatusPolicies != null && workflowStatusPolicies.size() > 0){
				workflowStatusPolicy = workflowStatusPolicies.get(0);
			}
		}catch(Exception ex){
			log.error("Error inside getSLADurationForEntityOrInstance - ", ex);
		}
		return workflowStatusPolicy;
	}

	@Override
	@Transactional
	public void addWorkflowInstanceLifeCycleStage(Integer productId,Integer entityTypeId, Integer entityInstanceId, Integer lifeCycleStageId, String instancePlannedStartDate, String instancePlannedEndDate, String createdDate, Integer createdBy) {
		try{
			String lifeCycleStageQuery = "INSERT INTO wf_instance_life_cycle_stage (productId,entityTypeId, entityInstanceId, lifeCycleStageId, instancePlannedStartDate, instancePlannedEndDate, createdDate, createdBy) "
					+ "VALUES ("
					+ productId+","+entityTypeId+", "+entityInstanceId+", "+lifeCycleStageId+", '"+instancePlannedStartDate+"', '"+instancePlannedEndDate+"', '"+createdDate+"', "+createdBy+")";
			
			log.info("Query for adding instance lifecycle stage - "+ lifeCycleStageQuery);
			
			sessionFactory.getCurrentSession().createSQLQuery(lifeCycleStageQuery).executeUpdate();
				
		}catch(Exception e) {
			log.error("Error in addWorkflowInstanceLifeCycleStage", e);
		}		
	}
	
	@Override
	@Transactional
	public void updateWorkflowInstanceLifeCycleStage(Integer productId,Integer entityTypeId, Integer entityInstanceId, Integer lifeCycleStageId, String instancePlannedStartDate, String instancePlannedEndDate, String createdDate, Integer createdBy) {
		try{
			String lifeCycleStageQuery = "UPDATE wf_instance_life_cycle_stage SET lifeCycleStageId="+lifeCycleStageId+",instancePlannedStartDate='"+instancePlannedStartDate+"',instancePlannedEndDate='"+instancePlannedEndDate+"' WHERE productId="+productId+" AND entityTypeId="+entityTypeId+" AND entityInstanceId="+entityInstanceId;
			log.info("Query for update instance lifecycle stage - "+ lifeCycleStageQuery);
			sessionFactory.getCurrentSession().createSQLQuery(lifeCycleStageQuery).executeUpdate();
		}catch(Exception e) {
			log.error("Error in addWorkflowInstanceLifeCycleStage", e);
		}		
	}
	
	@Override
	@Transactional
	public List<Object[]> getWorkpackageRAGViewSummaryList(Integer productId) {
		List<Object[]> workfPackageRAGViewList=null;
		try {
			
			String sql="SELECT productName,lifeCycleEntityId,lifeCycleEntityName,lifeCycleEntityStatusId,lifeCycleEntityStatusName,lifeCycleEntityWorkflowTemplate,entityTypeId,entityTypeName,lifeCycleStatusId,lifeCycleStatusName,lifeCycleStatusCategoryId,lifeCycleStatusCategoryName,COUNT(DISTINCT entityInstanceId) instanceCount,@remaingHours\\:=FLOOR(TIMESTAMPDIFF(HOUR, NOW(), instancePlannedEndDate)) AS remaingHours,CASE WHEN @remaingHours >=24 THEN 'green' WHEN @remaingHours <24 AND @remaingHours>=2 THEN 'orange' WHEN @remaingHours<2 AND @remaingHours>0 THEN 'orangered' ELSE 'red' END AS indicator FROM wf_life_cycle_stages_view WHERE entitytypeId <> 29 AND lifecycleentitystatusid <> -1";
			
			if(productId !=null && productId >0) {
				sql+=" AND productId="+productId;
			}
			sql+=" GROUP BY lifeCycleEntityId,entityTypeId,lifeCycleStatusId,indicator ORDER BY lifeCycleStatusId,entityTypeId ASC";
			workfPackageRAGViewList=sessionFactory.getCurrentSession().createSQLQuery(sql).list();
			
		} catch(Exception e) {
			log.error("Error in getWorkpackageRAGViewSummaryList",e);
			
		}
		return workfPackageRAGViewList;
	}
	
	@Override
	@Transactional
	public List<Object[]> getWorkflowLifeCycleSummarySLADetailView(Integer engagementId,Integer productId,Integer entityTypeId,Integer lifeCycleEntityId,Integer lifeCycleStageId,String indicator) {
		List<Object[]> statusSummaryCountList=null;
		try{
			
			String sql="SELECT entityTypeId,entityTypeName,@remaingHours\\:=FLOOR(TIMESTAMPDIFF(HOUR, NOW(), instancePlannedEndDate)) AS remaingHours, @indicator\\:=CASE WHEN @remaingHours >=24 THEN 'green' WHEN @remaingHours <24 AND @remaingHours>=2 THEN 'orange' WHEN @remaingHours<2 AND @remaingHours>0 THEN 'orangered' ELSE 'red' END AS indicator,entityInstanceId FROM wf_life_cycle_stages_view stageView where";
			if(productId !=null && productId!=0) {
				sql+=" stageView.productId="+productId;
			}
			if(entityTypeId !=null && entityTypeId!=0) {
				sql+=" and entityTypeId="+entityTypeId;
			}
			if(lifeCycleEntityId !=null && lifeCycleEntityId!=0) {
				sql+=" and lifeCycleEntityId="+lifeCycleEntityId;
			}
			if(lifeCycleStageId !=null && lifeCycleStageId!=0) {
				sql+=" and lifeCycleStatusId="+lifeCycleStageId;
			}
			if(indicator !=null && !indicator.trim().isEmpty()) {
				if("red".equalsIgnoreCase(indicator)){
					sql+=" and FLOOR(TIMESTAMPDIFF(HOUR, NOW(), instancePlannedEndDate)) <= 0";
				}else if("orangered".equalsIgnoreCase(indicator)){
					sql+=" and FLOOR(TIMESTAMPDIFF(HOUR, NOW(), instancePlannedEndDate)) BETWEEN 0 AND 2";
				}else if("orange".equalsIgnoreCase(indicator)){
					sql+=" and FLOOR(TIMESTAMPDIFF(HOUR, NOW(), instancePlannedEndDate)) BETWEEN 2 AND 24";
				}else if(!"blue".equalsIgnoreCase(indicator)){
					sql+=" and FLOOR(TIMESTAMPDIFF(HOUR, NOW(), instancePlannedEndDate)) >= 24";
				}
			}
			statusSummaryCountList=sessionFactory.getCurrentSession().createSQLQuery(sql).list();
		}catch(Exception e) {
			log.error("Error in getStatusSummaryCountByProduct"+e);
		}
	return statusSummaryCountList;
	}
	
	@Override
	@Transactional
	public List<Object[]> getWorkflowTypeSummaryByEntityType(Integer engagementId,Integer productId,Integer entityTypeId,Integer parentEntityInstanceId) {
		List<Object[]> workflowTypeSummaryList=null;
		try {
			String sql="SELECT typeName,typeId,statusView.productId,entityTypeId,COUNT(DISTINCT entityInstanceId) AS COUNT FROM wf_workflow_status_last_action_view statusView,wf_Workflows_status wfStatus,product_master product,test_factory tf WHERE tf.testFactoryId=product.testFactoryId and product.productId=statusView.productId and statusview.targetStatusId=wfStatus.workflowStatusId ";
			if(engagementId !=null && engagementId!=0) {
				sql+=" and tf.testFactoryId="+engagementId;
			}
			if(productId !=null && productId!=0) {
				sql+=" and statusView.productId="+productId;
			}
			if(entityTypeId !=null && entityTypeId!=0) {
				sql+=" and statusView.entityTypeId="+entityTypeId;
			}
			if(parentEntityInstanceId !=null && parentEntityInstanceId!=0) {
				sql+=" and statusView.parentEntityInstanceId="+parentEntityInstanceId;
			}
			sql+=" GROUP BY statusView.entityTypeId,typeId,statusView.productId,statusview.typename";
			workflowTypeSummaryList=sessionFactory.getCurrentSession().createSQLQuery(sql).list();
		}catch(Exception e) {
			log.error("Error in getWorkflowTypeSummaryByEntityType"+e);
		}
		return workflowTypeSummaryList;
	}
	
	@Override
	@Transactional
	public List<Object[]> getWorkflowTypeSummaryInstances(Integer engagementId,Integer productId,Integer typeId,Integer entityTypeId,Integer parentEntityInstanceId) {
		
		List<Object[]> workflowTypeSummaryInstanceList=null;
		try {
			String sql="SELECT DISTINCT entityInstanceId FROM wf_workflow_status_last_action_view statusView,product_master product,test_factory tf WHERE tf.testFactoryId=product.testFactoryId and product.productId=statusView.productId ";
			
			if(engagementId !=null && engagementId!=0) {
				sql+=" and tf.testFactoryId="+engagementId;
			}
			
			if(productId !=null && productId!=0) {
				sql+=" and statusView.productId="+productId;
			}
			if(entityTypeId !=null && entityTypeId!=0) {
				sql+=" and statusView.entityTypeId="+entityTypeId;
			}
			
			if(typeId !=null && typeId!=0) {
				sql+=" and statusView.typeId="+typeId;
			}
			
			if(parentEntityInstanceId !=null && parentEntityInstanceId!=0) {
				sql+=" and statusView.parentEntityInstanceId="+parentEntityInstanceId;
			}
			
			workflowTypeSummaryInstanceList=sessionFactory.getCurrentSession().createSQLQuery(sql).list();
		}catch(Exception e) {
			log.error("Error in getWorkflowTypeSummaryInstances"+e);
		}
		return workflowTypeSummaryInstanceList;
		
	}

	@Override
	@Transactional
	public List<String> getWorkFlowStatuNamebyWorkFlowId(int workflowId) {
		List<String> workFlowStatuNameList = null;
		
		try{			
			String hql = "SELECT workFlowStatus.workflowStatusName from WorkflowStatus workFlowStatus WHERE workFlowStatus.workflow.workflowId=:workflowId";
			workFlowStatuNameList=sessionFactory.getCurrentSession().createQuery(hql).setParameter("workflowId", workflowId).list();	
			
		}catch(Exception e){
			log.error("Error in getWorkFlowStatuNamebyWorkFlowId"+e);
			
		}
		
		return workFlowStatuNameList;
		
		
	}

	@Override
	@Transactional
	public List<WorkflowStatus> getWorkFlowStatus(int workflowId) {
		List<WorkflowStatus> workflowStatusList = null;
		try{
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(WorkflowStatus.class, "workflowStatus");
		criteria.add(Restrictions.eq("workflowStatus.workflow.workflowId", workflowId));
		workflowStatusList = criteria.list();
		
		}catch(Exception e){
			
			log.error("Error in getWorkFlowStatus"+e);
		}
		
		return workflowStatusList;
	}

	@Override
	@Transactional
	public WorkflowStatusPolicy getWorkflowStatusPolicyById(Integer workflowStatusPolicyId) {
		WorkflowStatusPolicy workflowStatusPolicy = null;
		try{
			Criteria criteria = sessionFactory.getCurrentSession().createCriteria(WorkflowStatusPolicy.class, "workflowStatusPolicy");
			criteria.add(Restrictions.eq("workflowStatusPolicy.workflowStatusPolicyId", workflowStatusPolicyId));
			List<WorkflowStatusPolicy> workflowStatusPolicies = criteria.list();
			if(workflowStatusPolicies != null && workflowStatusPolicies.size() > 0){
				workflowStatusPolicy = workflowStatusPolicies.get(0);
			}
		}catch(Exception ex){
			log.error("Error in getWorkflowStatusPolicyById - ", ex);
		}
		return workflowStatusPolicy;
	}

	@Override
	@Transactional
	public void deleteWorkflowStatusAndPolicy(Integer workflowStatusId) {
		try{
			String deleteQuery = "delete from wf_workflows_status where workflowStatusId = "+workflowStatusId;
			sessionFactory.getCurrentSession().createSQLQuery(deleteQuery).executeUpdate();
			
			deleteQuery = "delete from wf_workflows_status_policies where workflowStatusId = "+workflowStatusId;
			sessionFactory.getCurrentSession().createSQLQuery(deleteQuery).executeUpdate();
			
			deleteQuery = "delete from wf_entity_instance_status_actors_mapping where workflowStatusId = "+workflowStatusId;
			sessionFactory.getCurrentSession().createSQLQuery(deleteQuery).executeUpdate();
			
			deleteQuery = "delete from wf_instance_life_cycle_stage where lifeCycleStageId = "+workflowStatusId;
			sessionFactory.getCurrentSession().createSQLQuery(deleteQuery).executeUpdate();
		}catch(Exception ex){
			log.error("Error in deleteWorkflowStatusAndPolicy - ", ex);
		}
	}

	@Override
	@Transactional
	public boolean isWorkflowStatusInLifeCycleStage(Integer workflowStatusId) {
		boolean isWorkflowStatusInLifeCycleStage = false;
		try{
			String lifeCycleStageQuery = "select lifeCycleStageId from wf_instance_life_cycle_stage where lifeCycleStageId = "+workflowStatusId;
			List<Object> lifeCycleStages = sessionFactory.getCurrentSession().createSQLQuery(lifeCycleStageQuery).list();
			if(lifeCycleStages != null && lifeCycleStages.size() > 0){
				isWorkflowStatusInLifeCycleStage = true;
			}
		}catch(Exception e) {
			log.error("Error in addWorkflowInstanceLifeCycleStage", e);
		}		
		return isWorkflowStatusInLifeCycleStage;
	}
	
	
	
	@Transactional
	public List<Object[]> getWorkflowRAGSummaryCountByProductAndEntityTypeDuplicate(Integer engagementId,Integer productId,Integer entityTypeId,Integer parentEntityInstanceId) {
		List<Object[]> ragSummaryCountList=null;
		try{
			String sql="SELECT entityTypeId,entityTypeName,@totalHours\\:=FLOOR(TIMESTAMPDIFF(HOUR,ragView.plannedStartDate,ragView.plannedEndDate)) AS totalHours,@expectedEnd\\:=CASE WHEN @totalHours =0 THEN (ragView.plannedEndDate+INTERVAL 1 DAY) ELSE ragView.plannedEndDate END AS expectedEnd,@totalHours\\:=CASE WHEN @totalHours =0 THEN (@totalHours+24) ELSE @totalHours END AS exptotalHours,@remaingHours\\:=FLOOR(TIMESTAMPDIFF(HOUR,NOW(),@expectedEnd)) AS remaingHours,@remaingHoursPercentage\\:=ROUND(@remaingHours/@totalHours*100,2) AS remaingHoursPercentage,IFNULL(@remainingCompletion\\:=100-ragView.percentageCompletion,0) AS remainingCompletion,(CASE WHEN (@remaingHoursPercentage >=@remainingCompletion) THEN 'green' WHEN ((@remaingHoursPercentage < @remainingCompletion) && ((@remainingCompletion - @remaingHoursPercentage) <50)) THEN 'orange' ELSE 'red' END) AS indicator,COUNT(DISTINCT entityInstanceId) AS COUNT,ragView.productName FROM wf_workflow_policy_rag_view ragView,product_master product,activity_work_package wp,activity act,test_factory tf WHERE tf.testFactoryId=product.testFactoryId AND product.productId = ragView.productId";
			if(engagementId !=null && engagementId!=0) {
				sql+=" and tf.testFactoryId="+engagementId;
			}
			if(productId !=null && productId!=0) {
				sql+=" and ragView.productId="+productId;
			}
			if(parentEntityInstanceId !=null && parentEntityInstanceId!=0) {
				sql+=" and ragView.parentEntityInstanceId="+parentEntityInstanceId;
			}
			
			if(entityTypeId !=null && entityTypeId!=0) {
				if(entityTypeId.equals(IDPAConstants.ENTITY_ACTIVITY_TYPE)) {
					sql+=" and ragView.entityInstanceId=act.activityId and wp.activityWorkpackageId=act.activityWorkpackageId ";
					
				} else if(entityTypeId.equals(IDPAConstants.ENTITY_ACTIVITY_WORKPACKAGE_ID)){
					sql+=" and ragView.entityInstanceId=wp.activityWorkPackageId ";
				}
				sql+=" and ragView.entityTypeId="+entityTypeId;
			}
			sql+=   " AND ragView.workflowStatusId = currentStatusId AND currentStatusId > 0 AND workflowStatusType <> 'End'";
			
			sql+=" GROUP BY indicator";
			ragSummaryCountList=sessionFactory.getCurrentSession().createSQLQuery(sql).list();
		}catch(Exception e) {
			log.error("Error in getWorkflowRAGSummaryCountByProductAndEntityTypeDuplicate"+e);
		}
	return ragSummaryCountList;
	}
	
	
	@Override
	@Transactional
	public List<Object[]> getWorkflowRAGSummaryCountByProductAndEntityType(Integer engagementId,Integer productId,Integer entityTypeId,Integer parentEntityInstanceId) {
		List<Object[]> ragSummaryCountList=null;
		try{
			String sql="SELECT 33 AS entityTypeId,CAST('Activity Type' AS text), ABS(FLOOR(extract(epoch from(act.plannedStartDate-act.plannedEndDate))/3600)) AS totalHours,"+
					   " CASE WHEN ABS(FLOOR(extract(epoch from(act.plannedStartDate-act.plannedEndDate))/3600)) =0 THEN (act.plannedEndDate+INTERVAL '1 DAY') ELSE act.plannedEndDate END AS expectedEnd,"+
					   " CASE WHEN ABS(FLOOR(extract(epoch from(act.plannedStartDate-act.plannedEndDate))/3600)) =0 THEN (ABS(FLOOR(extract(epoch from(act.plannedStartDate-act.plannedEndDate))/3600))+24)"+
					   " ELSE ABS(FLOOR(extract(epoch from(act.plannedStartDate-act.plannedEndDate))/3600)) END AS exptotalHours,"+
					   " ABS(FLOOR(extract(epoch from(NOW()-CASE WHEN ABS(FLOOR(extract(epoch from(act.plannedStartDate-act.plannedEndDate))/3600)) =0 THEN (act.plannedEndDate+INTERVAL '1 DAY') ELSE act.plannedEndDate END))/3600)) AS remaingHours,"+
					   " ROUND(CAST(ABS(FLOOR(extract(epoch from(NOW()-CASE WHEN ABS(FLOOR(extract(epoch from(act.plannedStartDate-act.plannedEndDate))/3600)) =0 THEN (act.plannedEndDate+INTERVAL '1 DAY') ELSE act.plannedEndDate END))/3600))"+
					   " / CASE WHEN ABS(FLOOR(extract(epoch from(act.plannedStartDate-act.plannedEndDate))/3600)) =0 THEN (ABS(FLOOR(extract(epoch from(act.plannedStartDate-act.plannedEndDate))/3600))+24) ELSE ABS(FLOOR(extract(epoch from(act.plannedStartDate-act.plannedEndDate))/3600)) END*100 as numeric),2) AS remaingHoursPercentage,"+
					   " COALESCE(100-act.percentageCompletion,0) AS remainingCompletion,(CASE WHEN (ROUND(CAST(ABS(FLOOR(extract(epoch from(NOW()-CASE WHEN ABS(FLOOR(extract(epoch from(act.plannedStartDate-act.plannedEndDate))/3600)) =0"+
					   " THEN (act.plannedEndDate+INTERVAL '1 DAY') ELSE act.plannedEndDate END))/3600))/CASE WHEN ABS(FLOOR(extract(epoch from(act.plannedStartDate-act.plannedEndDate))/3600)) =0 THEN (ABS(FLOOR(extract(epoch from(act.plannedStartDate-act.plannedEndDate))/3600))+24)"+
					   " ELSE ABS(FLOOR(extract(epoch from(act.plannedStartDate-act.plannedEndDate))/3600)) END*100 as numeric),2) >=100-act.percentageCompletion) THEN 'green' WHEN ((ROUND(CAST(ABS(FLOOR(extract(epoch from(NOW()-CASE WHEN ABS(FLOOR(extract(epoch from(act.plannedStartDate-act.plannedEndDate))/3600)) =0"+
					   " THEN (act.plannedEndDate+INTERVAL '1 DAY') ELSE act.plannedEndDate END))/3600))/CASE WHEN ABS(FLOOR(extract(epoch from(act.plannedStartDate-act.plannedEndDate))/3600)) =0 THEN (ABS(FLOOR(extract(epoch from(act.plannedStartDate-act.plannedEndDate))/3600))+24) ELSE ABS(FLOOR(extract(epoch from(act.plannedStartDate-act.plannedEndDate))/3600)) END*100 as numeric),2) < 100-act.percentageCompletion)"+
					   " AND ((100-act.percentageCompletion - ROUND(CAST(ABS(FLOOR(extract(epoch from(NOW()-CASE WHEN ABS(FLOOR(extract(epoch from(act.plannedStartDate-act.plannedEndDate))/3600)) =0 THEN (act.plannedEndDate+INTERVAL '1 DAY') ELSE act.plannedEndDate END))/3600))/CASE WHEN ABS(FLOOR(extract(epoch from(act.plannedStartDate-act.plannedEndDate))/3600)) =0"+
					   " THEN (ABS(FLOOR(extract(epoch from(act.plannedStartDate-act.plannedEndDate))/3600))+24) ELSE ABS(FLOOR(extract(epoch from(act.plannedStartDate-act.plannedEndDate))/3600)) END*100 as numeric),2)) <50)) THEN 'orange' ELSE 'red' END) AS indicator,COUNT(DISTINCT act.activityId) AS COUNT,"+
					   " product.productName,wp.activityWorkPackageName,wp.activityWorkPackageId FROM activity_work_package wp,Activity act,test_factory tf,product_master product,product_build build,product_version_list_master pversion,wf_workflows_status wfStatus WHERE act.activityWorkPackageId=wp.activityWorkPackageId"+
					   " AND wp.productbuild=build.productBuildId AND build.productVersionId=pversion.productVersionListId AND product.productId=pversion.productId AND tf.testFactoryId=product.testFactoryId AND wfStatus.workflowStatusId=act.workflowStatusId";
			if(engagementId !=null && engagementId!=0) {
				sql+=" and tf.testFactoryId="+engagementId;
			}
			if(productId !=null && productId!=0) {
				sql+=" and product.productId="+productId;
			}
			if(parentEntityInstanceId !=null && parentEntityInstanceId!=0) {
				sql+=" and act.activityWorkPackageId="+parentEntityInstanceId;
			}
			
			sql+= " AND wfStatus.workflowStatusId = act.workflowStatusId and workflowStatusType <> 'End' AND workflowStatusType <> 'Abort'";
			
			sql+=" GROUP BY indicator,act.plannedstartdate,act.plannedenddate,act.percentagecompletion,product.productname,wp.activityworkpackagename,wp.activityworkpackageid";
			ragSummaryCountList=sessionFactory.getCurrentSession().createSQLQuery(sql).list();
		}catch(Exception e) {
			log.error("Error in getWorkflowRAGSummaryCountByProductAndEntityType"+e);
		}
	return ragSummaryCountList;
	}
	
	
	@Override
	@Transactional
	public List<Object[]> getWorkflowRAGSummaryCompleteCountByProductAndEntityType(Integer engagementId,Integer productId,Integer entityTypeId,Integer parentEntityInstanceId) {
		List<Object[]> ragSummaryCountList=null;
		try{
			String sql="SELECT 33 AS entityTypeId,CAST('Activity Type' AS text), ABS(FLOOR(extract(EPOCH FROM(act.plannedStartDate-act.plannedEndDate))/3600)) AS totalHours,"+
					   " CASE WHEN ABS(FLOOR(extract(EPOCH FROM(act.plannedStartDate-act.plannedEndDate))/3600)) =0 THEN (act.plannedEndDate+INTERVAL '1 DAY') ELSE act.plannedEndDate END AS expectedEnd,"+
					   " CASE WHEN ABS(FLOOR(extract(EPOCH FROM(act.plannedStartDate-act.plannedEndDate))/3600)) =0 THEN (ABS(FLOOR(extract(EPOCH FROM(act.plannedStartDate-act.plannedEndDate))/3600))+24) ELSE ABS(FLOOR(extract(EPOCH FROM(act.plannedStartDate-act.plannedEndDate))/3600)) END AS exptotalHours,"+
					   " ABS(FLOOR(extract(EPOCH FROM(NOW()-CASE WHEN ABS(FLOOR(extract(EPOCH FROM(act.plannedStartDate-act.plannedEndDate))/3600)) =0 THEN (act.plannedEndDate+INTERVAL '1 DAY') ELSE act.plannedEndDate END))/3600)) AS remaingHours,"+
					   " ROUND(CAST(ABS(FLOOR(extract(EPOCH FROM(NOW()-CASE WHEN ABS(FLOOR(extract(EPOCH FROM(act.plannedStartDate-act.plannedEndDate))/3600)) =0 THEN (act.plannedEndDate+INTERVAL '1 DAY') ELSE act.plannedEndDate END))/3600))"+
					   " / (CASE WHEN ABS(FLOOR(extract(EPOCH FROM(act.plannedStartDate-act.plannedEndDate))/3600)) =0 THEN (ABS(FLOOR(extract(EPOCH FROM(act.plannedStartDate-act.plannedEndDate))/3600))+24) ELSE ABS(FLOOR(extract(EPOCH FROM(act.plannedStartDate-act.plannedEndDate))/3600)) END)*100 as numeric),2) AS remaingHoursPercentage,"+
					   " COALESCE(100-act.percentageCompletion,0) AS remainingCompletion,(CASE WHEN (ROUND(CAST(ABS(FLOOR(extract(EPOCH FROM(NOW()-CASE WHEN ABS(FLOOR(extract(EPOCH FROM(act.plannedStartDate-act.plannedEndDate))/3600)) =0 THEN (act.plannedEndDate+INTERVAL '1 DAY') ELSE act.plannedEndDate END))/3600))"+
					   " / (CASE WHEN ABS(FLOOR(extract(EPOCH FROM(act.plannedStartDate-act.plannedEndDate))/3600)) =0 THEN (ABS(FLOOR(extract(EPOCH FROM(act.plannedStartDate-act.plannedEndDate))/3600))+24) ELSE ABS(FLOOR(extract(EPOCH FROM(act.plannedStartDate-act.plannedEndDate))/3600)) END)*100 as numeric),2) >=100-act.percentageCompletion)"+
					   " THEN 'green' WHEN ((ROUND(CAST(ABS(FLOOR(extract(EPOCH FROM(NOW()-CASE WHEN ABS(FLOOR(extract(EPOCH FROM(act.plannedStartDate-act.plannedEndDate))/3600)) =0 THEN (act.plannedEndDate+INTERVAL '1 DAY') ELSE act.plannedEndDate END))/3600))/(CASE WHEN ABS(FLOOR(extract(EPOCH FROM(act.plannedStartDate-act.plannedEndDate))/3600)) =0"+
					   " THEN (ABS(FLOOR(extract(EPOCH FROM(act.plannedStartDate-act.plannedEndDate))/3600))+24) ELSE ABS(FLOOR(extract(EPOCH FROM(act.plannedStartDate-act.plannedEndDate))/3600)) END)*100 as numeric),2) < 100-act.percentageCompletion) AND ((100-act.percentageCompletion - ROUND(CAST(ABS(FLOOR(extract(EPOCH FROM(NOW()-CASE WHEN ABS(FLOOR(extract(EPOCH FROM(act.plannedStartDate-act.plannedEndDate))/3600)) =0 THEN (act.plannedEndDate+INTERVAL '1 DAY') ELSE act.plannedEndDate END))/3600))"+
					   " / (CASE WHEN ABS(FLOOR(extract(EPOCH FROM(act.plannedStartDate-act.plannedEndDate))/3600)) =0 THEN (ABS(FLOOR(extract(EPOCH FROM(act.plannedStartDate-act.plannedEndDate))/3600))+24) ELSE ABS(FLOOR(extract(EPOCH FROM(act.plannedStartDate-act.plannedEndDate))/3600)) END)*100 as numeric),2)) <50)) THEN 'orange' ELSE 'red' END) AS indicator,"+
					   " COUNT(DISTINCT act.activityId) AS COUNT,product.productName,wp.activityWorkPackageName,wp.activityWorkPackageId FROM activity_work_package wp,Activity act,test_factory tf,product_master product,product_build build,product_version_list_master pversion,wf_workflows_status wfStatus WHERE act.activityWorkPackageId=wp.activityWorkPackageId"+
					   " AND wp.productbuild=build.productBuildId AND build.productVersionId=pversion.productVersionListId AND product.productId=pversion.productId AND tf.testFactoryId=product.testFactoryId AND wfStatus.workflowStatusId=act.workflowStatusId";
			if(engagementId !=null && engagementId!=0) {
				sql+=" and tf.testFactoryId="+engagementId;
			}
			
			if(productId !=null && productId!=0) {
				sql+=" and product.productId="+productId;
			}
			if(parentEntityInstanceId !=null && parentEntityInstanceId!=0) {
				sql+=" and act.activityWorkPackageId="+parentEntityInstanceId;
			}
			sql+=   " AND act.workflowStatusId = wfstatus.workflowStatusId AND workflowStatusType like 'End'";
			sql+= " GROUP BY act.plannedStartDate,act.plannedEndDate,act.percentagecompletion,product.productname,wp.activityworkpackagename,wp.activityworkpackageid";
			
			ragSummaryCountList=sessionFactory.getCurrentSession().createSQLQuery(sql).list();
		}catch(Exception e) {
			log.error("Error in getWorkflowRAGSummaryCompleteCountByProductAndEntityType"+e);
		}
	return ragSummaryCountList;
	}
	
	@Override
	@Transactional
	public List<Object[]>  getWorkflowRAGSummaryAbortCountByProductAndEntityType(Integer engagementId,Integer productId,Integer entityTypeId,Integer parentEntityInstanceId) {
		List<Object[]> ragSummaryCountList=null;
		try{
			String sql="SELECT 33 AS entityTypeId,CAST('Activity Type' AS text), ABS(FLOOR(extract(epoch from(act.plannedStartDate-act.plannedEndDate))/3600)) AS totalHours,"+
					   " CASE WHEN ABS(FLOOR(extract(epoch from(act.plannedStartDate-act.plannedEndDate))/3600)) =0 THEN (act.plannedEndDate+INTERVAL '1 DAY') ELSE act.plannedEndDate END AS expectedEnd,"+
					   " CASE WHEN ABS(FLOOR(extract(epoch from(act.plannedStartDate-act.plannedEndDate))/3600)) =0 THEN (ABS(FLOOR(extract(epoch from(act.plannedStartDate-act.plannedEndDate))/3600))+24)"+
					   " ELSE ABS(FLOOR(extract(epoch from(act.plannedStartDate-act.plannedEndDate))/3600)) END AS exptotalHours,ABS(FLOOR(extract(epoch from(NOW()-CASE WHEN ABS(FLOOR(extract(epoch from(act.plannedStartDate-act.plannedEndDate))/3600)) =0"+
					   " THEN (act.plannedEndDate+INTERVAL '1 DAY') ELSE act.plannedEndDate END))/3600)) AS remaingHours,ROUND(CAST(ABS(FLOOR(extract(epoch from(NOW()-CASE WHEN ABS(FLOOR(extract(epoch from(act.plannedStartDate-act.plannedEndDate))/3600)) =0 THEN (act.plannedEndDate+INTERVAL '1 DAY') ELSE act.plannedEndDate END))/3600))"+
					   " / CASE WHEN ABS(FLOOR(extract(epoch from(act.plannedStartDate-act.plannedEndDate))/3600)) =0 THEN (ABS(FLOOR(extract(epoch from(act.plannedStartDate-act.plannedEndDate))/3600))+24) ELSE ABS(FLOOR(extract(epoch from(act.plannedStartDate-act.plannedEndDate))/3600)) END*100 as numeric),2) AS remaingHoursPercentage,"+
					   " COALESCE(100-act.percentageCompletion,0) AS remainingCompletion,(CASE WHEN (ROUND(CAST(ABS(FLOOR(extract(epoch from(NOW()-CASE WHEN ABS(FLOOR(extract(epoch from(act.plannedStartDate-act.plannedEndDate))/3600)) =0 THEN (act.plannedEndDate+INTERVAL '1 DAY')"+
					   " ELSE act.plannedEndDate END))/3600))/CASE WHEN ABS(FLOOR(extract(epoch from(act.plannedStartDate-act.plannedEndDate))/3600)) =0 THEN (ABS(FLOOR(extract(epoch from(act.plannedStartDate-act.plannedEndDate))/3600))+24) ELSE ABS(FLOOR(extract(epoch from(act.plannedStartDate-act.plannedEndDate))/3600)) END*100 as numeric),2) >=100-act.percentageCompletion)"+
					   " THEN 'green' WHEN ((ROUND(CAST(ABS(FLOOR(extract(epoch from(NOW()-CASE WHEN ABS(FLOOR(extract(epoch from(act.plannedStartDate-act.plannedEndDate))/3600)) =0 THEN (act.plannedEndDate+INTERVAL '1 DAY') ELSE act.plannedEndDate END))/3600))"+
					   " / CASE WHEN ABS(FLOOR(extract(epoch from(act.plannedStartDate-act.plannedEndDate))/3600)) =0 THEN (ABS(FLOOR(extract(epoch from(act.plannedStartDate-act.plannedEndDate))/3600))+24) ELSE ABS(FLOOR(extract(epoch from(act.plannedStartDate-act.plannedEndDate))/3600)) END*100 as numeric),2) < 100-act.percentageCompletion)"+
					   " AND ((100-act.percentageCompletion - ROUND(CAST(ABS(FLOOR(extract(epoch from(NOW()-CASE WHEN ABS(FLOOR(extract(epoch from(act.plannedStartDate-act.plannedEndDate))/3600)) =0 THEN (act.plannedEndDate+INTERVAL '1 DAY') ELSE act.plannedEndDate END))/3600))"+
					   " / CASE WHEN ABS(FLOOR(extract(epoch from(act.plannedStartDate-act.plannedEndDate))/3600)) =0 THEN (ABS(FLOOR(extract(epoch from(act.plannedStartDate-act.plannedEndDate))/3600))+24) ELSE ABS(FLOOR(extract(epoch from(act.plannedStartDate-act.plannedEndDate))/3600)) END*100 as numeric),2)) <50))"+
					   " THEN 'orange' ELSE 'red' END) AS indicator,COUNT(DISTINCT act.activityId) AS COUNT,product.productName,wp.activityWorkPackageName,wp.activityWorkPackageId FROM activity_work_package wp,Activity act,test_factory tf,product_master product,product_build build,product_version_list_master pversion,wf_workflows_status wfStatus"+
					   " WHERE act.activityWorkPackageId=wp.activityWorkPackageId AND wp.productbuild=build.productBuildId AND build.productVersionId=pversion.productVersionListId AND product.productId=pversion.productId AND tf.testFactoryId=product.testFactoryId AND wfStatus.workflowStatusId=act.workflowStatusId";
			if(engagementId !=null && engagementId!=0) {
				sql+=" and tf.testFactoryId="+engagementId;
			}
			
			if(productId !=null && productId!=0) {
				sql+=" and product.productId="+productId;
			}
			if(parentEntityInstanceId !=null && parentEntityInstanceId!=0) {
				sql+=" and act.activityWorkPackageId="+parentEntityInstanceId;
			}
			sql+=   " AND act.workflowStatusId = wfstatus.workflowStatusId AND workflowStatusType like 'Abort'";
			sql+= " Group by act.plannedstartdate,act.plannedenddate,act.percentagecompletion,product.productname,wp.activityworkpackagename,wp.activityworkpackageid";
			
			ragSummaryCountList=sessionFactory.getCurrentSession().createSQLQuery(sql).list();
		}catch(Exception e) {
			log.error("Error in getWorkflowRAGSummaryAbortCountByProductAndEntityType"+e);
		}
	return ragSummaryCountList;
	}
	
	//@Override
	@Transactional
	public List<Object[]> getWorkflowRAGSummaryAbortCountByProductAndEntityTypeDuplicate(Integer engagementId,Integer productId,Integer entityTypeId,Integer parentEntityInstanceId) {
		List<Object[]> ragSummaryCountList=null;
		try{
			String sql="SELECT COUNT(DISTINCT entityInstanceId) AS COUNT FROM wf_workflow_policy_rag_view ragView,product_master product,activity_work_package wp,Activity act,test_factory tf WHERE tf.testFactoryId=product.testFactoryId AND ragView.entityInstanceId=act.activityId AND act.activityWorkpackageId=wp.activityWorkpackageId AND product.productId = ragView.productId";
			if(engagementId !=null && engagementId!=0) {
				sql+=" and tf.testFactoryId="+engagementId;
			}
			if(productId !=null && productId!=0) {
				sql+=" and ragView.productId="+productId;
			}
			if(parentEntityInstanceId !=null && parentEntityInstanceId!=0) {
				sql+=" and ragView.parentEntityInstanceId="+parentEntityInstanceId;
			}
			if(entityTypeId !=null && entityTypeId!=0) {
				sql+=" and ragView.entityTypeId="+entityTypeId;
			}
			sql+=   " AND ragView.workflowStatusId = currentStatusId AND currentStatusId > 0 AND workflowStatusType like 'Abort'";
			
			ragSummaryCountList=sessionFactory.getCurrentSession().createSQLQuery(sql).list();
		}catch(Exception e) {
			log.error("Error in getWorkflowRAGSummaryAbortCountByProductAndEntityTypeDuplicate"+e);
		}
	return ragSummaryCountList;
	}
	
	
	@Override
	@Transactional
	public List<Object[]> getWorkflowRAGSummaryCountByProductAndResouces(Integer engagementId,Integer productId,Integer entityTypeId,Integer parentEntityInstanceId) {
		List<Object[]> ragSummaryCountList=null;
		try{
			String sql="SELECT 33 AS entityTypeId,CAST('Activity Type' AS text),ABS(FLOOR(extract(epoch from(act.plannedStartDate-act.plannedEndDate))/3600)) AS totalHours,"+
					   " CASE WHEN ABS(FLOOR(extract(epoch from(act.plannedStartDate-act.plannedEndDate))/3600)) =0 THEN (act.plannedEndDate+INTERVAL '1 DAY') ELSE act.plannedEndDate END AS expectedEnd,"+
					   " CASE WHEN ABS(FLOOR(extract(epoch from(act.plannedStartDate-act.plannedEndDate))/3600)) =0 THEN (ABS(FLOOR(extract(epoch from(act.plannedStartDate-act.plannedEndDate))/3600))+24) ELSE ABS(FLOOR(extract(epoch from(act.plannedStartDate-act.plannedEndDate))/3600)) END AS exptotalHours,"+
					   " ABS(FLOOR(extract(epoch from(NOW()-CASE WHEN ABS(FLOOR(extract(epoch from(act.plannedStartDate-act.plannedEndDate))/3600)) =0 THEN (act.plannedEndDate+INTERVAL '1 DAY') ELSE act.plannedEndDate END))/3600)) AS remaingHours,"+
					   " ROUND(CAST(ABS(FLOOR(extract(epoch from(NOW()-CASE WHEN ABS(FLOOR(extract(epoch from(act.plannedStartDate-act.plannedEndDate))/3600)) =0 THEN (act.plannedEndDate+INTERVAL '1 DAY') ELSE act.plannedEndDate END))/3600))/(CASE WHEN ABS(FLOOR(extract(epoch from(act.plannedStartDate-act.plannedEndDate))/3600)) =0 THEN (ABS(FLOOR(extract(epoch from(act.plannedStartDate-act.plannedEndDate))/3600))+24) ELSE ABS(FLOOR(extract(epoch from(act.plannedStartDate-act.plannedEndDate))/3600)) END)*100 as numeric),2) AS remaingHoursPercentage,"+ 
					   " COALESCE(100-act.percentageCompletion,0) AS remainingCompletion,"+
					   " (CASE WHEN (ROUND(CAST(ABS(FLOOR(extract(epoch from(NOW()-CASE WHEN ABS(FLOOR(extract(epoch from(act.plannedStartDate-act.plannedEndDate))/3600)) =0 THEN (act.plannedEndDate+INTERVAL '1 DAY') ELSE act.plannedEndDate END))/3600))"+
					   " / CASE WHEN ABS(FLOOR(extract(epoch from(act.plannedStartDate-act.plannedEndDate))/3600)) =0 THEN (ABS(FLOOR(extract(epoch from(act.plannedStartDate-act.plannedEndDate))/3600))+24) ELSE ABS(FLOOR(extract(epoch from(act.plannedStartDate-act.plannedEndDate))/3600)) END*100 as numeric),2) >=100-act.percentageCompletion) THEN 'green' WHEN  ((ROUND(CAST(ABS(FLOOR(extract(epoch from(NOW()-CASE WHEN ABS(FLOOR(extract(epoch from(act.plannedStartDate-act.plannedEndDate))/3600)) =0 THEN (act.plannedEndDate+INTERVAL '1 DAY') ELSE act.plannedEndDate END))/3600))"+
					   " / CASE WHEN ABS(FLOOR(extract(epoch from(act.plannedStartDate-act.plannedEndDate))/3600)) =0 THEN (ABS(FLOOR(extract(epoch from(act.plannedStartDate-act.plannedEndDate))/3600))+24) ELSE ABS(FLOOR(extract(epoch from(act.plannedStartDate-act.plannedEndDate))/3600)) END*100 as numeric),2) < 100-act.percentageCompletion) AND ((100-act.percentageCompletion - ROUND(CAST(ABS(FLOOR(extract(epoch from(NOW()-CASE WHEN ABS(FLOOR(extract(epoch from(act.plannedStartDate-act.plannedEndDate))/3600)) =0 THEN (act.plannedEndDate+INTERVAL '1 DAY') ELSE act.plannedEndDate END))/3600))/CASE WHEN ABS(FLOOR(extract(epoch from(act.plannedStartDate-act.plannedEndDate))/3600)) =0 THEN (ABS(FLOOR(extract(epoch from(act.plannedStartDate-act.plannedEndDate))/3600))+24) ELSE ABS(FLOOR(extract(epoch from(act.plannedStartDate-act.plannedEndDate))/3600)) END*100 as numeric),2)) <50)) THEN 'orange' ELSE 'red' END) AS indicator,"+ 
					   " COUNT(DISTINCT (act.activityId)) AS COUNT,product.productName, users.loginId AS actor,act.assigneeId,product.productId,tf.testFactoryId FROM wf_entity_instance_status_actors_mapping actorMapping"+ 
					   " LEFT JOIN Activity act ON act.activityId=actorMapping.entityInstanceId"+ 
					   " LEFT JOIN Activity_work_package wp ON wp.activityWorkPackageId=act.activityWorkPackageId"+ 
					   " LEFT JOIN product_master product ON product.productId=actorMapping.productId"+ 
					   " LEFT JOIN test_factory tf ON tf.testFactoryId=product.testFactoryId"+ 
					   " LEFT JOIN wf_workflows_status wfStatus ON wfStatus.workflowStatusId=act.workflowStatusId"+ 
					   " LEFT JOIN user_list users ON users.userId=act.assigneeId"+ 
					   " LEFT JOIN wf_workflows_status_policies policy ON (actorMapping.entityInstanceId = policy.entityInstanceId"+ 
					   " AND actorMapping.entityTypeId=policy.entityTypeId AND actorMapping.productId=policy.levelId"+ 
					   " AND wfstatus.workflowStatusId = policy.workflowStatusId AND policy.statusPolicyType = 'Instance') WHERE act.workflowStatusId > 0"+ 
					   " AND  workflowStatusType <> 'End' AND  workflowStatusType <> 'Abort' AND act.assigneeId>0 and tf.testFactoryId="+engagementId;
			
						if(productId !=null && productId!=0) {
							sql+=" and product.productId="+productId;
						}
			
						if(parentEntityInstanceId !=null && parentEntityInstanceId!=0) {
							sql+=" and wp.activityWorkPackageId="+parentEntityInstanceId;
						}
						
						sql+="  GROUP BY act.assigneeId,totalHours,expectedEnd,remainingCompletion,product.productName,users.loginId,product.productId,tf.testFactoryId,indicator ORDER BY users.loginId ASC";
			
			ragSummaryCountList=sessionFactory.getCurrentSession().createSQLQuery(sql).list();
		}catch(Exception e) {
			log.error("Error in getWorkflowRAGSummaryCountByProductAndResouces",e);
		}
	return ragSummaryCountList;
	}
	
	
	@Override
	@Transactional
	public List<Object[]> getRAGSummaryCompletedCountByProductAndResouces(Integer engagementId,Integer productId,Integer entityTypeId,Integer entityParentInstanceId) {
		List<Object[]> ragSummaryCountList=null;
		try{
			String sql="SELECT COUNT(act.activityId) AS COUNT,product.productName,"+
					" users.loginId AS actor,act.assigneeId,product.productId,tf.testFactoryId FROM Activity act"+
					" LEFT JOIN activity_work_package wp ON wp.activityWorkPackageId= act.activityWorkPackageId"+
					" LEFT JOIN product_build build ON wp.productbuild=build.productBuildId "+
					" LEFT JOIN product_version_list_master pversion ON build.productVersionId=pversion.productVersionListId"+
					" LEFT JOIN product_master product ON product.productId=pversion.productId "+
					" LEFT JOIN test_factory tf ON tf.testFactoryId=product.testFactoryId "+
					" LEFT JOIN wf_workflows_status wfStatus ON wfStatus.workflowStatusId=act.workflowStatusId"+ 	
					" LEFT JOIN user_list users ON users.userId=act.assigneeId"+
					" WHERE act.workflowStatusId > 0 AND workflowStatusType like 'End' and act.assigneeId >0 and tf.testFactoryId="+engagementId;
		
					if(productId !=null && productId!=0) {
						sql+=" and product.productId="+productId;
					}
		
					if(entityParentInstanceId !=null && entityParentInstanceId!=0) {
						sql+=" and wp.activityWorkPackageId="+entityParentInstanceId;
					}
					
					sql+="  GROUP BY act.assigneeId,product.productName,users.loginId,product.productId,tf.testFactoryId ORDER BY users.loginId ASC";
					
			ragSummaryCountList=sessionFactory.getCurrentSession().createSQLQuery(sql).list();
		}catch(Exception e) {
			log.error("Error in getRAGSummaryCompletedCountByProductAndResouces",e);
		}
	return ragSummaryCountList;
	}
	
	
	@Override
	@Transactional
	public List<Object[]> getRAGSummaryAbortedCountByProductAndResouces(Integer engagementId,Integer productId,Integer entityTypeId,Integer parentEntityInstanceId) {
		List<Object[]> ragSummaryCountList=null;
		try{
			String sql="SELECT COUNT(act.activityId) AS COUNT,product.productName,"+
					" users.loginId AS actor,act.assigneeId,product.productId,tf.testFactoryId FROM Activity act"+
					" LEFT JOIN activity_work_package wp ON wp.activityWorkPackageId= act.activityWorkPackageId"+
					" LEFT JOIN product_build build ON wp.productbuild=build.productBuildId "+
					" LEFT JOIN product_version_list_master pversion ON build.productVersionId=pversion.productVersionListId"+
					" LEFT JOIN product_master product ON product.productId=pversion.productId "+
					" LEFT JOIN test_factory tf ON tf.testFactoryId=product.testFactoryId "+
					" LEFT JOIN wf_workflows_status wfStatus ON wfStatus.workflowStatusId=act.workflowStatusId"+ 	
					" LEFT JOIN user_list users ON users.userId=act.assigneeId"+
					" WHERE act.workflowStatusId > 0 AND workflowStatusType like 'Abort' and act.assigneeId >0 and tf.testFactoryId="+engagementId;
		
					if(productId !=null && productId!=0) {
						sql+=" and product.productId="+productId;
					}
		
					if(parentEntityInstanceId !=null && parentEntityInstanceId!=0) {
						sql+=" and wp.activityWorkPackageId="+parentEntityInstanceId;
					}
					
					sql+="  GROUP BY act.assigneeId,product.productName,users.loginId,product.productId,tf.testFactoryId ORDER BY users.loginId ASC";
			ragSummaryCountList=sessionFactory.getCurrentSession().createSQLQuery(sql).list();
		}catch(Exception e) {
			log.error("Error in getRAGSummaryAbortedCountByProductAndResouces",e);
		}
	return ragSummaryCountList;
	}
	
	@Override
	@Transactional
	public List<Object[]> getWorkflowRAGActivityGroupingSummaryCount(Integer engagementId,Integer productId,Integer entityTypeId,Integer parentEntityInstanceId) {
		List<Object[]> ragSummaryCountList=null;
		try{
			String sql="SELECT 33 AS entityTypeId,CAST('Activity Type' AS text), @totalHours\\:=FLOOR(TIMESTAMPDIFF(HOUR,act.plannedStartDate,act.plannedEndDate)) AS totalHours,"
					+"@expectedEnd\\:=CASE WHEN @totalHours =0 THEN (act.plannedEndDate+INTERVAL 1 DAY) ELSE act.plannedEndDate END AS expectedEnd,"
					+ "@totalHours\\:=CASE WHEN @totalHours =0 THEN (@totalHours+24) ELSE @totalHours END AS exptotalHours,"
					+ "@remaingHours\\:=FLOOR(TIMESTAMPDIFF(HOUR,NOW(),@expectedEnd)) AS remaingHours,"
					+ "@remaingHoursPercentage\\:=ROUND(@remaingHours/@totalHours*100,2) AS remaingHoursPercentage,"
					+ "IFNULL(@remainingCompletion\\:=100-act.percentageCompletion,0) AS remainingCompletion,"
					+ "(CASE WHEN (@remaingHoursPercentage >=@remainingCompletion) THEN 'green' WHEN ((@remaingHoursPercentage < @remainingCompletion) && ((@remainingCompletion - @remaingHoursPercentage) <50)) THEN 'orange' ELSE 'red' END) AS indicator,COUNT(DISTINCT act.activityId) AS COUNT,product.productName,"
					+ "wp.activityWorkPackageName,wp.activityWorkPackageId FROM activity_work_package wp,Activity act,test_factory tf,product_master product,product_build build,product_version_list_master pversion,wf_workflows_status wfStatus"
					+ " WHERE act.activityWorkPackageId=wp.activityWorkPackageId AND wp.productbuild=build.productBuildId AND build.productVersionId=pversion.productVersionListId AND product.productId=pversion.productId AND tf.testFactoryId=product.testFactoryId AND wfStatus.workflowStatusId=act.workflowStatusId";
		
			if(engagementId !=null && engagementId!=0) {
				sql+=" and tf.testFactoryId="+engagementId;
			}
			if(productId !=null && productId!=0) {
				sql+=" and product.productId="+productId;
			}
			if(parentEntityInstanceId !=null && parentEntityInstanceId!=0) {
				sql+=" and wp.activityWorkPackageId="+parentEntityInstanceId;
			}
			sql+= "	AND wfstatus.workflowStatusId = act.workflowStatusId AND workflowStatusType <> 'End' AND workflowStatusType <> 'Abort'";
			sql+=" GROUP BY product.productId,wp.activityWorkPackageId,indicator";
			ragSummaryCountList=sessionFactory.getCurrentSession().createSQLQuery(sql).list();
		}catch(Exception e) {
			log.error("Error in getWorkflowRAGActivityGroupingSummaryCount"+e);
		}
	return ragSummaryCountList;
	}
	
	
	@Override
	@Transactional
	public List<Object[]> getRAGViewActivityGroupingSummaryCompletedCount(Integer engagementId,Integer productId,Integer entityTypeId,Integer parentEntityInstanceId) {
		List<Object[]> ragSummaryCountList=null;
		try{
			String sql="SELECT COUNT(DISTINCT entityInstanceId) AS COUNT,wp.activityWorkPackageId,product.productId,product.productName,wp.activityWorkPackageName FROM wf_workflow_policy_rag_view ragView,activity_work_package wp,Activity act,product_master product,test_factory tf WHERE tf.testFactoryId=product.testFactoryId and ragView.entityInstanceId=act.activityId and act.activityWorkpackageId=wp.activityWorkpackageId ";
			if(engagementId !=null && engagementId!=0) {
				sql+=" and tf.testFactoryId="+engagementId;
			}
			if(productId !=null && productId!=0) {
				sql+=" and ragView.productId="+productId;
			}
			if(entityTypeId !=null && entityTypeId!=0) {
				sql+=" and ragView.entityTypeId="+entityTypeId;
			}
			
			if(parentEntityInstanceId !=null && parentEntityInstanceId!=0) {
				sql+=" and ragView.parentEntityInstanceId="+parentEntityInstanceId;
			}
			sql+= "	AND ragView.workflowStatusId = currentStatusId"
					+ "	AND currentStatusId > 0"
					+ " AND workflowStatusType like 'End'";
			sql+=" GROUP BY ragView.productId,wp.activityWorkPackageId";
			ragSummaryCountList=sessionFactory.getCurrentSession().createSQLQuery(sql).list();
		}catch(Exception e) {
			log.error("Error in getRAGViewActivityGroupingSummaryCompletedCount"+e);
		}
	return ragSummaryCountList;
	}
	
	
	@Override
	@Transactional
	public List<Object[]> getRAGViewActivityGroupingSummaryAbortCount(Integer engagementId,Integer productId,Integer entityTypeId,Integer parentEntityInstanceId) {
		List<Object[]> ragSummaryCountList=null;
		try{
			String sql="SELECT COUNT(DISTINCT entityInstanceId) AS COUNT,wp.activityWorkPackageId,product.productId,product.productName,wp.activityWorkPackageName FROM wf_workflow_policy_rag_view ragView,activity_work_package wp ,Activity act,product_master product,test_factory tf WHERE tf.testFactoryId=product.testFactoryId and ragView.entityInstanceId=act.activityId and act.activityWorkpackageId=wp.activityWorkpackageId ";
			if(productId !=null && productId!=0) {
				sql+=" and ragView.productId="+productId;
			}
			if(entityTypeId !=null && entityTypeId!=0) {
				sql+=" and ragView.entityTypeId="+entityTypeId;
			}
			if(parentEntityInstanceId !=null && parentEntityInstanceId!=0) {
				sql+=" and ragView.parentEntityInstanceId="+parentEntityInstanceId;
			}
			sql+= "	AND ragView.workflowStatusId = currentStatusId"
					+ "	AND currentStatusId > 0"
					+ " AND workflowStatusType like 'Abort'";
			sql+=" GROUP BY ragView.productId,wp.activityWorkPackageId";
			ragSummaryCountList=sessionFactory.getCurrentSession().createSQLQuery(sql).list();
		}catch(Exception e) {
			log.error("Error in getRAGViewActivityGroupingSummaryAbortCount"+e);
		}
	return ragSummaryCountList;
	}
	
	@Override
	@Transactional
	public List<Object[]> getWorkflowRAGWorkpackageSummaryCount(Integer productId,Integer entityTypeId,Integer entityParentInstanceId) {
		List<Object[]> ragSummaryCountList=null;
		try{
			String sql="SELECT entityTypeId,entityTypeName,@totalHours\\:=FLOOR(TIMESTAMPDIFF(HOUR,ragView.plannedStartDate,ragView.plannedEndDate)) AS totalHours,@expectedEnd\\:=CASE WHEN @totalHours =0 THEN (ragView.plannedEndDate+INTERVAL 1 DAY) ELSE ragView.plannedEndDate END AS expectedEnd,@totalHours\\:=CASE WHEN @totalHours =0 THEN (@totalHours+24) ELSE @totalHours END AS exptotalHours,@remaingHours\\:=FLOOR(TIMESTAMPDIFF(HOUR,NOW(),@expectedEnd)) AS remaingHours,@remaingHoursPercentage\\:=ROUND(@remaingHours/@totalHours*100,2) AS remaingHoursPercentage,IFNULL(@remainingCompletion\\:=100-act.percentageCompletion,0) AS remainingCompletion,(CASE WHEN (@remaingHoursPercentage >=@remainingCompletion) THEN 'green' WHEN ((@remaingHoursPercentage < @remainingCompletion) && ((@remainingCompletion - @remaingHoursPercentage) <50)) THEN 'orange' ELSE 'red' END) AS indicator,COUNT(DISTINCT entityInstanceId) AS COUNT,productName,wp.activityWorkPackageName,wp.activityWorkPackageId FROM wf_workflow_policy_rag_view ragView,activity_work_package wp,Activity act WHERE ragView.entityInstanceId=act.activityId AND act.activityWorkPackageId=wp.activityWorkPackageId ";
			if(productId !=null && productId!=0) {
				sql+=" and ragView.productId="+productId;
			}
			if(entityTypeId !=null && entityTypeId!=0) {
				sql+=" and ragView.entityTypeId="+entityTypeId;
			}
			if(entityParentInstanceId !=null && entityParentInstanceId!=0) {
				sql+=" and ragview.parentEntityInstanceId="+entityParentInstanceId;
			}
			sql+= "	AND ragView.workflowStatusId = currentStatusId"
					+ "	AND currentStatusId > 0"
					+ " AND workflowStatusType <> 'End' AND workflowStatusType <> 'Abort' ";
			
			sql+=" GROUP BY ragview.parentEntityInstanceId,indicator";
			ragSummaryCountList=sessionFactory.getCurrentSession().createSQLQuery(sql).list();
		}catch(Exception e) {
			log.error("Error in getWorkflowRAGWorkpackageSummaryCount"+e);
		}
	return ragSummaryCountList;
	}
	
	@Override
	@Transactional
	public List<Object[]> getRAGviewWorkpackageSummaryCompletedCount(Integer productId,Integer entityTypeId,Integer parentEntityInstanceId) {
		List<Object[]> ragSummaryCountList=null;
		try{
			String sql="SELECT COUNT(DISTINCT entityInstanceId) AS COUNT,parententityInstanceId FROM wf_workflow_policy_rag_view ragView where ragView.workflowStatusId = currentStatusId and currentStatusId > 0 AND workflowStatusType like 'End'";
			if(productId !=null && productId!=0) {
				sql+=" and ragView.productId="+productId;
			}
			if(entityTypeId !=null && entityTypeId!=0) {
				sql+=" and ragView.entityTypeId="+entityTypeId;
			}
			
			if(parentEntityInstanceId != null && parentEntityInstanceId!=0) {
				sql+=" and ragView.parentEntityInstanceId="+parentEntityInstanceId;
			}
			
			ragSummaryCountList=sessionFactory.getCurrentSession().createSQLQuery(sql).list();
		}catch(Exception e) {
			log.error("Error in geRAGviewWorkpackageSummaryCompletedCount"+e);
		}
	return ragSummaryCountList;
	}
	
	
	
	@Override
	@Transactional
	public List<Object[]> getRAGviewWorkpackageSummaryAbortedCount(Integer productId,Integer entityTypeId,Integer parentEntityInstanceId) {
		List<Object[]> ragSummaryCountList=null;
		try{
			String sql="SELECT COUNT(DISTINCT entityInstanceId) AS COUNT,parententityInstanceId FROM wf_workflow_policy_rag_view ragView where ragView.workflowStatusId = currentStatusId and currentStatusId > 0 AND workflowStatusType like 'Abort'";
			if(productId !=null && productId!=0) {
				sql+=" and ragView.productId="+productId;
			}
			if(entityTypeId !=null && entityTypeId!=0) {
				sql+=" and ragView.entityTypeId="+entityTypeId;
			}
			
			if(parentEntityInstanceId != null && parentEntityInstanceId!=0) {
				sql+=" and ragView.parentEntityInstanceId="+parentEntityInstanceId;
			}
			
			ragSummaryCountList=sessionFactory.getCurrentSession().createSQLQuery(sql).list();
		}catch(Exception e) {
			log.error("Error in getRAGviewWorkpackageSummaryAbortedCount"+e);
		}
	return ragSummaryCountList;
	}
	
	@Override
	@Transactional
	public List<Object[]> getWorkflowRAGActivitySummaryCount(Integer productId,Integer entityTypeId) {
		List<Object[]> ragSummaryCountList=null;
		try{
			String sql="SELECT entityTypeId,entityTypeName,@totalHours\\:=FLOOR(TIMESTAMPDIFF(HOUR,ragView.plannedStartDate,ragView.plannedEndDate)) AS totalHours,@expectedEnd\\:=CASE WHEN @totalHours =0 THEN (ragView.plannedEndDate+INTERVAL 1 DAY) ELSE ragView.plannedEndDate END AS expectedEnd,@totalHours\\:=CASE WHEN @totalHours =0 THEN (@totalHours+24) ELSE @totalHours END AS exptotalHours,@remaingHours\\:=FLOOR(TIMESTAMPDIFF(HOUR,NOW(),@expectedEnd)) AS remaingHours,@remaingHoursPercentage\\:=ROUND(@remaingHours/@totalHours*100,2) AS remaingHoursPercentage,IFNULL(@remainingCompletion\\:=100-task.percentageCompletion,0) AS remainingCompletion,(CASE WHEN (@remaingHoursPercentage >=@remainingCompletion) THEN 'green' WHEN ((@remaingHoursPercentage < @remainingCompletion) && ((@remainingCompletion - @remaingHoursPercentage) <50)) THEN 'orange' ELSE 'red' END) AS indicator,COUNT(DISTINCT entityInstanceId) AS COUNT,productName,act.activityName,act.activityId FROM wf_workflow_policy_rag_view ragView,activity act,activity_task task WHERE ragView.entityInstanceId=task.activityTaskId AND task.activityId=act.activityId ";
			if(productId !=null && productId!=0) {
				sql+=" and ragView.productId="+productId;
			}
			if(entityTypeId !=null && entityTypeId!=0) {
				sql+=" and ragView.entityTypeId="+entityTypeId;
			}
			sql+= "	AND ragView.workflowStatusId = currentStatusId"
					+ "	AND currentStatusId > 0"
					+ " AND workflowStatusType <> 'End' AND workflowStatusType <> 'Abort'";
			
			sql+=" GROUP BY act.activityId,indicator";
			ragSummaryCountList=sessionFactory.getCurrentSession().createSQLQuery(sql).list();
		}catch(Exception e) {
			log.error("Error in getWorkflowRAGActivitySummaryCount"+e);
		}
	return ragSummaryCountList;
	}
	
	
	@Override
	@Transactional
	 public List<Object[]> getWorkflowRAGSummarySLADetailView(Integer engagementId,Integer productId,Integer entityTypeId,String indicator,Integer categoryId) {
		List<Object[]> ragInstanceList=null;
		try{
			
			String sql="SELECT 33 AS entityTypeId,CAST('Activity Type' AS text),ABS(FLOOR(extract(epoch from(act.plannedStartDate-act.plannedEndDate))/3600)) AS totalHours,"+
					   " CASE WHEN ABS(FLOOR(extract(epoch from(act.plannedStartDate-act.plannedEndDate))/3600)) =0 THEN (act.plannedEndDate+INTERVAL '1 DAY') ELSE act.plannedEndDate END AS expectedEnd,CASE WHEN ABS(FLOOR(extract(epoch from(act.plannedStartDate-act.plannedEndDate))/3600)) =0"+
					   " THEN (ABS(FLOOR(extract(epoch from(act.plannedStartDate-act.plannedEndDate))/3600))+24) ELSE ABS(FLOOR(extract(epoch from(act.plannedStartDate-act.plannedEndDate))/3600)) END AS exptotalHours,ABS(FLOOR(extract(epoch from(NOW()-CASE WHEN ABS(FLOOR(extract(epoch from(act.plannedStartDate-act.plannedEndDate))/3600)) =0"+
					   " THEN (act.plannedEndDate+INTERVAL '1 DAY') ELSE act.plannedEndDate END))/3600)) AS remaingHours,ROUND(CAST(ABS(FLOOR(extract(epoch from(NOW()-CASE WHEN ABS(FLOOR(extract(epoch from(act.plannedStartDate-act.plannedEndDate))/3600)) =0 THEN (act.plannedEndDate+INTERVAL '1 DAY') ELSE act.plannedEndDate END))/3600))"+
					   " / CASE WHEN ABS(FLOOR(extract(epoch from(act.plannedStartDate-act.plannedEndDate))/3600)) =0 THEN (ABS(FLOOR(extract(epoch from(act.plannedStartDate-act.plannedEndDate))/3600))+24) ELSE ABS(FLOOR(extract(epoch from(act.plannedStartDate-act.plannedEndDate))/3600)) END*100 as numeric),2) AS remaingHoursPercentage,"+
					   " COALESCE(100-act.percentageCompletion,0) AS remainingCompletion,(CASE WHEN (ROUND(CAST(ABS(FLOOR(extract(epoch from(NOW()-CASE WHEN ABS(FLOOR(extract(epoch from(act.plannedStartDate-act.plannedEndDate))/3600)) =0 THEN (act.plannedEndDate+INTERVAL '1 DAY') ELSE act.plannedEndDate END))/3600))"+
					   " / CASE WHEN ABS(FLOOR(extract(epoch from(act.plannedStartDate-act.plannedEndDate))/3600)) =0 THEN (ABS(FLOOR(extract(epoch from(act.plannedStartDate-act.plannedEndDate))/3600))+24) ELSE ABS(FLOOR(extract(epoch from(act.plannedStartDate-act.plannedEndDate))/3600)) END*100 as numeric),2) >=100-act.percentageCompletion)"+
					   " THEN 'green' WHEN ((ROUND(CAST(ABS(FLOOR(extract(epoch from(NOW()-CASE WHEN ABS(FLOOR(extract(epoch from(act.plannedStartDate-act.plannedEndDate))/3600)) =0 THEN (act.plannedEndDate+INTERVAL '1 DAY') ELSE act.plannedEndDate END))/3600))/CASE WHEN ABS(FLOOR(extract(epoch from(act.plannedStartDate-act.plannedEndDate))/3600)) =0"+
					   " THEN (ABS(FLOOR(extract(epoch from(act.plannedStartDate-act.plannedEndDate))/3600))+24) ELSE ABS(FLOOR(extract(epoch from(act.plannedStartDate-act.plannedEndDate))/3600)) END*100 as numeric),2) < 100-act.percentageCompletion) AND ((100-act.percentageCompletion - ROUND(CAST(ABS(FLOOR(extract(epoch from(NOW()-CASE WHEN ABS(FLOOR(extract(epoch from(act.plannedStartDate-act.plannedEndDate))/3600)) =0"+
					   " THEN (act.plannedEndDate+INTERVAL '1 DAY') ELSE act.plannedEndDate END))/3600))/CASE WHEN ABS(FLOOR(extract(epoch from(act.plannedStartDate-act.plannedEndDate))/3600)) =0 THEN (ABS(FLOOR(extract(epoch from(act.plannedStartDate-act.plannedEndDate))/3600))+24) ELSE ABS(FLOOR(extract(epoch from(act.plannedStartDate-act.plannedEndDate))/3600)) END*100 as numeric),2)) <50)) THEN 'orange' ELSE 'red' END) AS indicator,"+
					   " act.activityId FROM activity act  LEFT JOIN activity_work_package wp ON wp.activityWorkPackageId= act.activityWorkPackageId LEFT JOIN product_build build ON wp.productbuild=build.productBuildId LEFT JOIN product_version_list_master pversion ON build.productVersionId=pversion.productVersionListId LEFT JOIN product_master product ON product.productId=pversion.productId  LEFT JOIN test_factory tf ON tf.testFactoryId=product.testFactoryId"+
					   " LEFT JOIN wf_workflows_status wfStatus ON wfStatus.workflowStatusId=act.workflowStatusId WHERE tf.testFactoryId=product.testFactoryId AND wfStatus.workflowStatusId > 0";
			if(engagementId !=null && engagementId!=0) {
				sql+=" and tf.testFactoryId="+engagementId;
			}
			if(productId !=null && productId!=0) {
				sql+=" and product.productId="+productId;
			}
			if(categoryId !=null && categoryId!=0) {
				sql+=" and act.activityMasterId="+categoryId;
			}
			
			
			if(indicator.equals(IDPAConstants.RAG_STATUS_VIEW_COMPLETED_INDICATOR)) {
				sql+=" AND workflowStatusType like 'End'";
			} else if(indicator.equals(IDPAConstants.RAG_STATUS_VIEW_ABORT_INDICATOR)) {
				sql+=" AND workflowStatusType like 'Abort'";
			} else if(!indicator.equals(IDPAConstants.RAG_STATUS_VIEW_TOTAL_INDICATOR) && !indicator.equals(IDPAConstants.RAG_STATUS_VIEW_COMPLETED_INDICATOR)&& !indicator.equals(IDPAConstants.RAG_STATUS_VIEW_ABORT_INDICATOR)) {
				sql+=" AND workflowStatusType <> 'End' AND workflowStatusType <> 'Abort' ";
			}
			
			ragInstanceList=sessionFactory.getCurrentSession().createSQLQuery(sql).list();
		}catch(Exception e) {
			log.error("Error in getWorkflowRAGSummarySLADetailView"+e);
		}
	return ragInstanceList;
	}
	
	
	@Override
	@Transactional
	public List<Object[]> getWorkflowRAGSummarySLADetailCompletedStageView(Integer engagementId,Integer productId,Integer entityTypeId,String indicator,Integer categoryId) {
		List<Object[]> ragInstanceList=null;
		try{
			
			String sql="SELECT 33 AS entityTypeId,CAST('Activity Type' AS text),CAST('darkgreen' AS character varying),act.activityId FROM Activity act  LEFT JOIN activity_work_package wp ON wp.activityWorkPackageId= act.activityWorkPackageId"+
					" LEFT JOIN product_build build ON wp.productbuild=build.productBuildId"+ 
					" LEFT JOIN product_version_list_master pversion ON build.productVersionId=pversion.productVersionListId"+
					" LEFT JOIN product_master product ON product.productId=pversion.productId "+
					" LEFT JOIN test_factory tf ON tf.testFactoryId=product.testFactoryId "+
					" LEFT JOIN wf_workflows_status wfStatus ON wfStatus.workflowStatusId=act.workflowStatusId WHERE tf.testFactoryId=product.testFactoryId"+
					" AND wfStatus.workflowStatusId > 0";
			if(engagementId !=null && engagementId!=0) {
				sql+=" and tf.testFactoryId="+engagementId;
			}
			if(productId !=null && productId!=0) {
				sql+=" and product.productId="+productId;
			}
			if(categoryId !=null && categoryId!=0) {
				sql+=" and act.activityMasterId="+categoryId;
			}
			
			sql+= "	and workflowStatusType like 'End'";
			
			ragInstanceList=sessionFactory.getCurrentSession().createSQLQuery(sql).list();
		}catch(Exception e) {
			log.error("Error in getWorkflowRAGSummarySLADetailCompletedStageView"+e);
		}
	return ragInstanceList;
	}
	
	
	@Override
	@Transactional
	public List<Object[]> getWorkflowRAGSummarySLADetailAbortStageView(Integer engagementId,Integer productId,Integer entityTypeId,String indicator,Integer categoryId) {
		List<Object[]> ragInstanceList=null;
		try{
			
			String sql="SELECT 33 AS entityTypeId,CAST('Activity Type' AS text),CAST('darkRed' AS character varying),act.activityId FROM Activity act  LEFT JOIN activity_work_package wp ON wp.activityWorkPackageId= act.activityWorkPackageId"+
					" LEFT JOIN product_build build ON wp.productbuild=build.productBuildId"+ 
					" LEFT JOIN product_version_list_master pversion ON build.productVersionId=pversion.productVersionListId"+
					" LEFT JOIN product_master product ON product.productId=pversion.productId "+
					" LEFT JOIN test_factory tf ON tf.testFactoryId=product.testFactoryId "+
					" LEFT JOIN wf_workflows_status wfStatus ON wfStatus.workflowStatusId=act.workflowStatusId WHERE tf.testFactoryId=product.testFactoryId"+
					" AND wfStatus.workflowStatusId > 0";
			if(engagementId !=null && engagementId!=0) {
				sql+=" and tf.testFactoryId="+engagementId;
			}
			if(productId !=null && productId!=0) {
				sql+=" and procuct.productId="+productId;
			}
			if(categoryId !=null && categoryId!=0) {
				sql+=" and act.activityMasterId="+categoryId;
			}
			sql+= "	AND workflowStatusType like 'Abort'";
			
			ragInstanceList=sessionFactory.getCurrentSession().createSQLQuery(sql).list();
		}catch(Exception e) {
			log.error("Error in getWorkflowRAGSummarySLADetailAbortStageView"+e);
		}
	return ragInstanceList;
	}
	
	
	@Override
	@Transactional
	public List<Object[]> listWorkflowRAGStatusInstanceDetail(Integer engagementId,Integer productId, Integer entityTypeId,Integer entityId, Integer parentInstanceId,String indicator,Integer categoryId) {
		log.info("Inside listWorkflowRAGStatusInstanceDetail with parameters : productId - "+productId+", entityTypeId - "+entityTypeId+", entityId - "+entityId);
		List<Object[]> workflowInstanceList=null;
		try{
		String sql=" SELECT DISTINCT entityInstanceId,ABS(FLOOR(extract(epoch from(act.plannedStartDate-act.plannedEndDate))/3600)) AS totalHours,"+
				   " CASE WHEN ABS(FLOOR(extract(epoch from(act.plannedStartDate-act.plannedEndDate))/3600)) =0 THEN (act.plannedEndDate+INTERVAL '1 DAY') ELSE act.plannedEndDate END AS expectedEnd,"+
				   " CASE WHEN ABS(FLOOR(extract(epoch from(act.plannedStartDate-act.plannedEndDate))/3600)) =0 THEN (ABS(FLOOR(extract(epoch from(act.plannedStartDate-act.plannedEndDate))/3600))+24)"+
				   " ELSE ABS(FLOOR(extract(epoch from(act.plannedStartDate-act.plannedEndDate))/3600)) END AS exptotalHours, ABS(FLOOR(extract(epoch from(NOW()-CASE WHEN ABS(FLOOR(extract(epoch from(act.plannedStartDate-act.plannedEndDate))/3600)) =0"+
				   " THEN (act.plannedEndDate+INTERVAL '1 DAY') ELSE act.plannedEndDate END))/3600)) AS remaingHours, ROUND(CAST(ABS(FLOOR(extract(epoch from(NOW()-CASE WHEN ABS(FLOOR(extract(epoch from(act.plannedStartDate-act.plannedEndDate))/3600)) =0 THEN (act.plannedEndDate+INTERVAL '1 DAY') ELSE act.plannedEndDate END))/3600))"+
				   " / CASE WHEN ABS(FLOOR(extract(epoch from(act.plannedStartDate-act.plannedEndDate))/3600)) =0 THEN (ABS(FLOOR(extract(epoch from(act.plannedStartDate-act.plannedEndDate))/3600))+24) ELSE ABS(FLOOR(extract(epoch from(act.plannedStartDate-act.plannedEndDate))/3600)) END*100 as numeric),2) AS remaingHoursPercentage,"+
				   " COALESCE(100-act.percentageCompletion,0) AS remainingCompletion, (CASE WHEN (ROUND(CAST(ABS(FLOOR(extract(epoch from(NOW()-CASE WHEN ABS(FLOOR(extract(epoch from(act.plannedStartDate-act.plannedEndDate))/3600)) =0 THEN (act.plannedEndDate+INTERVAL '1 DAY') ELSE act.plannedEndDate END))/3600))"+
				   " / CASE WHEN ABS(FLOOR(extract(epoch from(act.plannedStartDate-act.plannedEndDate))/3600)) =0 THEN (ABS(FLOOR(extract(epoch from(act.plannedStartDate-act.plannedEndDate))/3600))+24) ELSE ABS(FLOOR(extract(epoch from(act.plannedStartDate-act.plannedEndDate))/3600)) END*100 as numeric),2) >=100-act.percentageCompletion)"+
				   " THEN 'green' WHEN ((ROUND(CAST(ABS(FLOOR(extract(epoch from(NOW()-CASE WHEN ABS(FLOOR(extract(epoch from(act.plannedStartDate-act.plannedEndDate))/3600)) =0 THEN (act.plannedEndDate+INTERVAL '1 DAY') ELSE act.plannedEndDate END))/3600))/CASE WHEN ABS(FLOOR(extract(epoch from(act.plannedStartDate-act.plannedEndDate))/3600)) =0"+
				   " THEN (ABS(FLOOR(extract(epoch from(act.plannedStartDate-act.plannedEndDate))/3600))+24) ELSE ABS(FLOOR(extract(epoch from(act.plannedStartDate-act.plannedEndDate))/3600)) END*100 as numeric),2) < 100-act.percentageCompletion)"+
				   " AND ((100-act.percentageCompletion - ROUND(CAST(ABS(FLOOR(extract(epoch from(NOW()-CASE WHEN ABS(FLOOR(extract(epoch from(act.plannedStartDate-act.plannedEndDate))/3600)) =0 THEN (act.plannedEndDate+INTERVAL '1 DAY') ELSE act.plannedEndDate END))/3600))"+
				   " / CASE WHEN ABS(FLOOR(extract(epoch from(act.plannedStartDate-act.plannedEndDate))/3600)) =0 THEN (ABS(FLOOR(extract(epoch from(act.plannedStartDate-act.plannedEndDate))/3600))+24) ELSE ABS(FLOOR(extract(epoch from(act.plannedStartDate-act.plannedEndDate))/3600)) END*100 as numeric),2)) <50)) THEN 'orange' ELSE 'red' END) AS indicator"+
				   " FROM wf_workflows_status_policies  policy LEFT JOIN product_master product ON (product.productId=policy.levelId) LEFT JOIN activity act ON (act.activityId=policy.entityInstanceId AND act.workflowStatusId=policy.workflowStatusId) LEFT JOIN activity_work_package wp ON (act.activityWorkPackageId=wp.activityWorkPackageId)"+
				   " LEFT JOIN wf_workflows_status wfstatus ON (wfstatus.workflowStatusId=policy.workflowStatusId) LEFT JOIN test_factory tf ON (tf.testFactoryId=product.testFactoryId) WHERE act.workflowStatusId > 0";
		
		if(engagementId != null && engagementId != 0){
			sql += " AND tf.testFactoryId= "+engagementId;
		}
		
		if(productId != null && productId != 0){
			sql += " AND policy.levelId = "+productId;
		}
		
		if(entityTypeId != null && entityTypeId != 0){
			sql += " AND entityTypeId="+entityTypeId;
		}
		if(parentInstanceId != null && parentInstanceId != 0){
				sql += " AND act.activityWorkPackageId= "+parentInstanceId;
		}
		
		if(categoryId != null && categoryId != 0){
			sql += " AND policy.entityId= "+categoryId;
		}
		
		if(indicator.equals(IDPAConstants.RAG_STATUS_VIEW_COMPLETED_INDICATOR)) {
			sql+= " AND workflowStatusType like 'End'";
		} else if(indicator.equals(IDPAConstants.RAG_STATUS_VIEW_ABORT_INDICATOR)) {
			sql+= " AND workflowStatusType like 'Abort'";

		} else if(!indicator.equals(IDPAConstants.RAG_STATUS_VIEW_TOTAL_INDICATOR) && !indicator.equals(IDPAConstants.RAG_STATUS_VIEW_COMPLETED_INDICATOR)&& !indicator.equals(IDPAConstants.RAG_STATUS_VIEW_ABORT_INDICATOR)) {
			sql+=" AND workflowStatusType <> 'End' AND workflowStatusType <> 'Abort'";
		}
		workflowInstanceList=sessionFactory.getCurrentSession().createSQLQuery(sql).list();
		
		}catch(Exception e) {
			log.error("Error in listWorkflowRAGStatusInstanceDetail", e);
		}
		return workflowInstanceList;
	}
	
	
	
	@Override
	@Transactional
	public List<Object[]> getWorkflowRAGByProductAndResoucesDetail(Integer engagementId,Integer productId,Integer entityTypeId,Integer userId,Integer roleId,Integer parentInstanceId,String indicator) {
		List<Object[]> ragSummaryCountList=null;
		try{
			String sql="SELECT DISTINCT act.activityId,ABS(FLOOR(extract(epoch from(act.plannedStartDate-act.plannedEndDate))/3600)) AS totalHours, CASE WHEN ABS(FLOOR(extract(epoch from(act.plannedStartDate-act.plannedEndDate))/3600)) =0 THEN (act.plannedEndDate+INTERVAL '1 DAY')"+
					   " ELSE act.plannedEndDate END AS expectedEnd, CASE WHEN ABS(FLOOR(extract(epoch from(act.plannedStartDate-act.plannedEndDate))/3600)) =0 THEN (ABS(FLOOR(extract(epoch from(act.plannedStartDate-act.plannedEndDate))/3600))+24) ELSE ABS(FLOOR(extract(epoch from(act.plannedStartDate-act.plannedEndDate))/3600)) END AS exptotalHours,"+
					   " ABS(FLOOR(extract(epoch from(NOW()-CASE WHEN ABS(FLOOR(extract(epoch from(act.plannedStartDate-act.plannedEndDate))/3600)) =0 THEN (act.plannedEndDate+INTERVAL '1 DAY') ELSE act.plannedEndDate END))/3600)) AS remaingHours, ROUND(CAST(ABS(FLOOR(extract(epoch from(NOW()-CASE WHEN ABS(FLOOR(extract(epoch from(act.plannedStartDate-act.plannedEndDate))/3600)) =0"+
					   " THEN (act.plannedEndDate+INTERVAL '1 DAY') ELSE act.plannedEndDate END))/3600))/CASE WHEN ABS(FLOOR(extract(epoch from(act.plannedStartDate-act.plannedEndDate))/3600)) =0 THEN (ABS(FLOOR(extract(epoch from(act.plannedStartDate-act.plannedEndDate))/3600))+24) ELSE ABS(FLOOR(extract(epoch from(act.plannedStartDate-act.plannedEndDate))/3600)) END*100 as numeric),2) AS remaingHoursPercentage,"+
					   " COALESCE(100-act.percentageCompletion,0) AS remainingCompletion, (CASE WHEN (ROUND(CAST(ABS(FLOOR(extract(epoch from(NOW()-CASE WHEN ABS(FLOOR(extract(epoch from(act.plannedStartDate-act.plannedEndDate))/3600)) =0 THEN (act.plannedEndDate+INTERVAL '1 DAY') ELSE act.plannedEndDate END))/3600))/CASE WHEN ABS(FLOOR(extract(epoch from(act.plannedStartDate-act.plannedEndDate))/3600)) =0 THEN (ABS(FLOOR(extract(epoch from(act.plannedStartDate-act.plannedEndDate))/3600))+24)"+
					   " ELSE ABS(FLOOR(extract(epoch from(act.plannedStartDate-act.plannedEndDate))/3600)) END*100 as numeric),2) >=100-act.percentageCompletion) THEN 'green' WHEN ((ROUND(CAST(ABS(FLOOR(extract(epoch from(NOW()-CASE WHEN ABS(FLOOR(extract(epoch from(act.plannedStartDate-act.plannedEndDate))/3600)) =0 THEN (act.plannedEndDate+INTERVAL '1 DAY') ELSE act.plannedEndDate END))/3600))"+
					   " / CASE WHEN ABS(FLOOR(extract(epoch from(act.plannedStartDate-act.plannedEndDate))/3600)) =0 THEN (ABS(FLOOR(extract(epoch from(act.plannedStartDate-act.plannedEndDate))/3600))+24) ELSE ABS(FLOOR(extract(epoch from(act.plannedStartDate-act.plannedEndDate))/3600)) END*100 as numeric),2) < 100-act.percentageCompletion) AND ((100-act.percentageCompletion - ROUND(CAST(ABS(FLOOR(extract(epoch from(NOW()-CASE WHEN ABS(FLOOR(extract(epoch from(act.plannedStartDate-act.plannedEndDate))/3600)) =0"+
					   " THEN (act.plannedEndDate+INTERVAL '1 DAY') ELSE act.plannedEndDate END))/3600))/CASE WHEN ABS(FLOOR(extract(epoch from(act.plannedStartDate-act.plannedEndDate))/3600)) =0 THEN (ABS(FLOOR(extract(epoch from(act.plannedStartDate-act.plannedEndDate))/3600))+24) ELSE ABS(FLOOR(extract(epoch from(act.plannedStartDate-act.plannedEndDate))/3600)) END*100 as numeric),2)) <50)) THEN 'orange' ELSE 'red' END) AS indicator FROM wf_entity_instance_status_actors_mapping actorMapping"+
					   " LEFT JOIN Activity act ON act.activityId=actorMapping.entityInstanceId  LEFT JOIN product_master product ON product.productId=actorMapping.productId  LEFT JOIN test_factory tf ON tf.testFactoryId=product.testFactoryId  LEFT JOIN wf_workflows_status wfStatus ON wfStatus.workflowStatusId=act.workflowStatusId LEFT JOIN user_list users ON users.userId=actorMapping.userId"+
					   " LEFT JOIN wf_workflows_status_policies policy ON (actorMapping.entityInstanceId = policy.entityInstanceId AND actorMapping.entityTypeId=policy.entityTypeId AND actorMapping.productId=policy.levelId AND wfstatus.workflowStatusId = policy.workflowStatusId AND policy.statusPolicyType = 'Instance') WHERE act.workflowStatusId > 0";
			
			 
			if(engagementId !=null && engagementId!=0) {
				sql+=" and tf.testFactoryId="+engagementId;
			}
			if(productId !=null && productId!=0) {
				sql+=" and product.productId="+productId;
			}
			if(parentInstanceId !=null && parentInstanceId!=0) {
				sql+=" AND act.activityWorkPackageId="+parentInstanceId;
			}
			if(entityTypeId !=null && entityTypeId!=0) {
				sql+=" and actorMapping.entityTypeId="+entityTypeId;
			}
			
			if(indicator.equals(IDPAConstants.RAG_STATUS_VIEW_COMPLETED_INDICATOR)) {
				sql+=" AND workflowStatusType like 'End'";
			} else if(indicator.equals(IDPAConstants.RAG_STATUS_VIEW_ABORT_INDICATOR)) {
				sql+=" AND workflowStatusType like 'Abort'";
			} else  {
				sql+=" AND workflowStatusType <> 'End' AND workflowStatusType <> 'Abort'";	
			}
			
			
			if(userId != null && userId != 0){
				sql += " and act.assigneeId = "+userId;
			}
			
			ragSummaryCountList=sessionFactory.getCurrentSession().createSQLQuery(sql).list();
		}catch(Exception e) {
			log.error("Error in getWorkflowRAGByProductAndResoucesDetail",e);
		}
	return ragSummaryCountList;
	}
	
	
	@Override
	@Transactional
	public List<Object[]> listWorkflowStatusCategorySummaryCount(Integer engagmentId,Integer productId, Integer entityTypeId, Integer entityId, Integer parentEntityInstanceId) {
		List<Object[]> workflowstatusList=null;
		try{
		String sql="SELECT workflowStatusCategoryName, COUNT(DISTINCT policy.entityInstanceId),wfstatus.workflowStatusCategoryId,policy.entityId,actType.activitymastername "+
				" FROM wf_workflows_status_policies policy "+
				" LEFT JOIN product_master product ON (product.productId=policy.levelId)"+ 
				" LEFT JOIN activity act ON (act.activityId=policy.entityInstanceId AND act.workflowStatusId=policy.workflowStatusId)"+ 
				" LEFT JOIN activity_work_package wp ON (act.activityWorkPackageId=wp.activityWorkPackageId) "+
				" LEFT JOIN wf_workflows_status wfstatus ON (wfstatus.workflowStatusId=policy.workflowStatusId)"+ 
				" LEFT JOIN activity_master actType ON actType.activityMasterId=act.activityMasterId"+
				" LEFT JOIN wf_status_category statusCategory ON statusCategory.workflowStatusCategoryId=wfstatus.workflowStatusCategoryId"+
				" LEFT JOIN test_factory tf ON (tf.testFactoryId=product.testFactoryId)  WHERE act.workflowStatusId > 0";	
		if(engagmentId != null && engagmentId != 0){
			sql += " and tf.testFactoryId = "+engagmentId;
		}
		if(productId != null && productId != 0){
			sql += " AND product.productId = "+productId;
		}
		
		if(entityId != null && entityId != 0){
			sql += " and policy.entityId = "+entityId;
		}
		if(parentEntityInstanceId != null && parentEntityInstanceId != 0){
			sql += " and act.activityWorkPackageId = "+parentEntityInstanceId;
		}
		sql += " GROUP BY wfStatus.workflowStatusCategoryId,act.activitymasterId,statuscategory.workflowstatuscategoryname,policy.entityid,acttype.activitymastername ORDER BY wfStatus.workflowStatusCategoryId";
			
		workflowstatusList=sessionFactory.getCurrentSession().createSQLQuery(sql).list();
		
		}catch(Exception e) {
			log.error("Error in listWorkflowStatusCategorySummaryCount", e);
		}
		return workflowstatusList;
	}
	
	@Override
	@Transactional
	 public List<Object[]> getWorkflowRAGEngagementSummaryCount(Integer engagementId,Integer productId, Integer entityTypeId,Integer entityParentInstanceId) {
		List<Object[]> ragSummaryCountList=null;
		try{
			String sql="SELECT 33 AS entityTypeId,CAST('Activity Type' AS text), ABS(FLOOR(extract(epoch from(act.plannedStartDate-act.plannedEndDate))/3600)) AS totalHours,"+
					   " CASE WHEN ABS(FLOOR(extract(epoch from(act.plannedStartDate-act.plannedEndDate))/3600)) =0 THEN (act.plannedEndDate+INTERVAL '1 DAY') ELSE act.plannedEndDate END AS expectedEnd,"+
					   " CASE WHEN ABS(FLOOR(extract(epoch from(act.plannedStartDate-act.plannedEndDate))/3600)) =0 THEN (ABS(FLOOR(extract(epoch from(act.plannedStartDate-act.plannedEndDate))/3600))+24) ELSE ABS(FLOOR(extract(epoch from(act.plannedStartDate-act.plannedEndDate))/3600)) END AS exptotalHours,"+
					   " ABS(FLOOR(extract(epoch from(NOW()-CASE WHEN ABS(FLOOR(extract(epoch from(act.plannedStartDate-act.plannedEndDate))/3600)) =0 THEN (act.plannedEndDate+INTERVAL '1 DAY') ELSE act.plannedEndDate END))/3600)) AS remaingHours,"+
					   " ROUND(CAST(ABS(FLOOR(extract(epoch from(NOW()-CASE WHEN ABS(FLOOR(extract(epoch from(act.plannedStartDate-act.plannedEndDate))/3600)) =0 THEN (act.plannedEndDate+INTERVAL '1 DAY') ELSE act.plannedEndDate END))/3600))"+
					   " / (CASE WHEN ABS(FLOOR(extract(epoch from(act.plannedStartDate-act.plannedEndDate))/3600)) =0 THEN (ABS(FLOOR(extract(epoch from(act.plannedStartDate-act.plannedEndDate))/3600))+24) ELSE ABS(FLOOR(extract(epoch from(act.plannedStartDate-act.plannedEndDate))/3600)) END)*100 as numeric),2) AS remaingHoursPercentage,"+
					   " COALESCE(100-act.percentageCompletion,0) AS remainingCompletion,(CASE WHEN (ROUND(CAST(ABS(FLOOR(extract(epoch from(NOW()-CASE WHEN ABS(FLOOR(extract(epoch from(act.plannedStartDate-act.plannedEndDate))/3600)) =0 THEN (act.plannedEndDate+INTERVAL '1 DAY') ELSE act.plannedEndDate END))/3600))"+
					   " / (CASE WHEN ABS(FLOOR(extract(epoch from(act.plannedStartDate-act.plannedEndDate))/3600)) =0 THEN (ABS(FLOOR(extract(epoch from(act.plannedStartDate-act.plannedEndDate))/3600))+24) ELSE ABS(FLOOR(extract(epoch from(act.plannedStartDate-act.plannedEndDate))/3600)) END)*100 as numeric),2) >=100-act.percentageCompletion)"+
					   " THEN 'green' WHEN ((ROUND(CAST(ABS(FLOOR(extract(epoch from(NOW()-CASE WHEN ABS(FLOOR(extract(epoch from(act.plannedStartDate-act.plannedEndDate))/3600)) =0 THEN (act.plannedEndDate+INTERVAL '1 DAY') ELSE act.plannedEndDate END))/3600))"+
					   " / (CASE WHEN ABS(FLOOR(extract(epoch from(act.plannedStartDate-act.plannedEndDate))/3600)) =0 THEN (ABS(FLOOR(extract(epoch from(act.plannedStartDate-act.plannedEndDate))/3600))+24) ELSE ABS(FLOOR(extract(epoch from(act.plannedStartDate-act.plannedEndDate))/3600)) END)*100 as numeric),2) < 100-act.percentageCompletion)"+
					   " AND ((100-act.percentageCompletion - ROUND(CAST(ABS(FLOOR(extract(epoch from(NOW()-CASE WHEN ABS(FLOOR(extract(epoch from(act.plannedStartDate-act.plannedEndDate))/3600)) =0 THEN (act.plannedEndDate+INTERVAL '1 DAY') ELSE act.plannedEndDate END))/3600))/(CASE WHEN ABS(FLOOR(extract(epoch from(act.plannedStartDate-act.plannedEndDate))/3600)) =0"+
					   " THEN (ABS(FLOOR(extract(epoch from(act.plannedStartDate-act.plannedEndDate))/3600))+24) ELSE ABS(FLOOR(extract(epoch from(act.plannedStartDate-act.plannedEndDate))/3600)) END)*100 as numeric),2)) <50)) THEN 'orange' ELSE 'red' END) AS indicator,COUNT(DISTINCT act.activityId) AS COUNT,"+
					   " product.productId,product.productName,tf.testFactoryId,tf.testFactoryName,wp.activityWorkPackageId,wp.activityWorkPackageName FROM activity_work_package wp,Activity act,test_factory tf,product_master product,product_build build,product_version_list_master pversion,wf_workflows_status wfStatus"+
					   " WHERE act.activityWorkPackageId=wp.activityWorkPackageId AND wp.productbuild=build.productBuildId AND build.productVersionId=pversion.productVersionListId AND product.productId=pversion.productId AND tf.testFactoryId=product.testFactoryId AND wfStatus.workflowStatusId=act.workflowStatusId";
			
			if(engagementId != null && engagementId!=0) {
				sql+=" and tf.testFactoryId="+engagementId;
			}
			if(productId !=null && productId!=0) {
				sql+=" and product.productId="+productId;
			}
			if(entityParentInstanceId !=null && entityParentInstanceId!=0) {
				sql+=" and wp.activityWorkPackageId="+entityParentInstanceId;
			}
			
			sql+= "	AND wfStatus.workflowStatusId = act.WorkflowStatusId AND workflowStatusType <> 'End' AND workflowStatusType <> 'Abort'";
			sql+=" GROUP BY tf.testFactoryId,product.productId,wp.activityWorkPackageId,act.plannedstartdate,act.plannedenddate,act.percentagecompletion,indicator order by product.productId,wp.activityWorkPackageId asc";
			ragSummaryCountList=sessionFactory.getCurrentSession().createSQLQuery(sql).list();
		}catch(Exception e) {
			log.error("Error in getWorkflowRAGEngagementSummaryCount"+e);
		}
	return ragSummaryCountList;
	}
	
	@Override
	@Transactional
	public List<Object[]> getRAGViewEngagementSummaryCompletedCount(Integer engagementId,Integer productId,Integer entityTypeId,Integer entityInstanceParentId) {
		List<Object[]> ragSummaryCountList=null;
		try{
			String sql="SELECT 33 AS entityTypeId,CAST('Activity Type' AS text), ABS(FLOOR(extract(epoch from(act.plannedStartDate-act.plannedEndDate))/3600)) AS totalHours,"+
					   " CASE WHEN ABS(FLOOR(extract(epoch from(act.plannedStartDate-act.plannedEndDate))/3600)) =0 THEN (act.plannedEndDate+INTERVAL '1 DAY') ELSE act.plannedEndDate END AS expectedEnd,"+
					   " CASE WHEN ABS(FLOOR(extract(epoch from(act.plannedStartDate-act.plannedEndDate))/3600)) =0 THEN (ABS(FLOOR(extract(epoch from(act.plannedStartDate-act.plannedEndDate))/3600))+24) ELSE ABS(FLOOR(extract(epoch from(act.plannedStartDate-act.plannedEndDate))/3600)) END AS exptotalHours,"+
					   " ABS(FLOOR(extract(epoch from(NOW()-CASE WHEN ABS(FLOOR(extract(epoch from(act.plannedStartDate-act.plannedEndDate))/3600)) =0 THEN (act.plannedEndDate+INTERVAL '1 DAY') ELSE act.plannedEndDate END))/3600)) AS remaingHours,"+
					   " ROUND(CAST(ABS(FLOOR(extract(epoch from(NOW()-CASE WHEN ABS(FLOOR(extract(epoch from(act.plannedStartDate-act.plannedEndDate))/3600)) =0 THEN (act.plannedEndDate+INTERVAL '1 DAY') ELSE act.plannedEndDate END))/3600))"+
					   " / CASE WHEN ABS(FLOOR(extract(epoch from(act.plannedStartDate-act.plannedEndDate))/3600)) =0 THEN (ABS(FLOOR(extract(epoch from(act.plannedStartDate-act.plannedEndDate))/3600))+24) ELSE ABS(FLOOR(extract(epoch from(act.plannedStartDate-act.plannedEndDate))/3600)) END*100 as numeric),2) AS remaingHoursPercentage,"+
					   " COALESCE(100-act.percentageCompletion,0) AS remainingCompletion,(CASE WHEN (ROUND(CAST(ABS(FLOOR(extract(epoch from(NOW()-CASE WHEN ABS(FLOOR(extract(epoch from(act.plannedStartDate-act.plannedEndDate))/3600)) =0"+
					   " THEN (act.plannedEndDate+INTERVAL '1 DAY') ELSE act.plannedEndDate END))/3600))/CASE WHEN ABS(FLOOR(extract(epoch from(act.plannedStartDate-act.plannedEndDate))/3600)) =0 THEN (ABS(FLOOR(extract(epoch from(act.plannedStartDate-act.plannedEndDate))/3600))+24)"+
					   " ELSE ABS(FLOOR(extract(epoch from(act.plannedStartDate-act.plannedEndDate))/3600)) END*100 as numeric),2) >=100-act.percentageCompletion) THEN 'green' WHEN ((ROUND(CAST(ABS(FLOOR(extract(epoch from(NOW()-CASE WHEN ABS(FLOOR(extract(epoch from(act.plannedStartDate-act.plannedEndDate))/3600)) =0"+
					   " THEN (act.plannedEndDate+INTERVAL '1 DAY') ELSE act.plannedEndDate END))/3600))/CASE WHEN ABS(FLOOR(extract(epoch from(act.plannedStartDate-act.plannedEndDate))/3600)) =0 THEN (ABS(FLOOR(extract(epoch from(act.plannedStartDate-act.plannedEndDate))/3600))+24)"+
					   " ELSE ABS(FLOOR(extract(epoch from(act.plannedStartDate-act.plannedEndDate))/3600)) END*100 as numeric),2) < 100-act.percentageCompletion) AND ((100-act.percentageCompletion - ROUND(CAST(ABS(FLOOR(extract(epoch from(NOW()-CASE WHEN ABS(FLOOR(extract(epoch from(act.plannedStartDate-act.plannedEndDate))/3600)) =0"+
					   " THEN (act.plannedEndDate+INTERVAL '1 DAY') ELSE act.plannedEndDate END))/3600))/CASE WHEN ABS(FLOOR(extract(epoch from(act.plannedStartDate-act.plannedEndDate))/3600)) =0 THEN (ABS(FLOOR(extract(epoch from(act.plannedStartDate-act.plannedEndDate))/3600))+24)"+
					   " ELSE ABS(FLOOR(extract(epoch from(act.plannedStartDate-act.plannedEndDate))/3600)) END*100 as numeric),2)) <50)) THEN 'orange' ELSE 'red' END) AS indicator,COUNT(DISTINCT act.activityId) AS COUNT,product.productId,product.productName,tf.testFactoryId,tf.testFactoryName,wp.activityWorkPackageId,wp.activityWorkPackageName"+
					   " FROM activity_work_package wp,Activity act,test_factory tf,product_master product,product_build build,product_version_list_master pversion,wf_workflows_status wfStatus WHERE act.activityWorkPackageId=wp.activityWorkPackageId AND wp.productbuild=build.productBuildId AND"+
					   " build.productVersionId=pversion.productVersionListId AND product.productId=pversion.productId AND tf.testFactoryId=product.testFactoryId AND wfStatus.workflowStatusId=act.workflowStatusId";
			if(engagementId != null && engagementId!=0) {
				sql+=" and tf.testFactoryId="+engagementId;
			}
			if(productId !=null && productId!=0) {
				sql+=" and product.productId="+productId;
			}
			if(entityInstanceParentId !=null && entityInstanceParentId!=0) {
				sql+=" and wp.activityWorkPackageId="+entityInstanceParentId;
			}
			sql+= "	AND wfStatus.workflowStatusId = act.WorkflowStatusId AND workflowStatusType like 'End' GROUP BY wp.activityWorkPackageId,act.plannedstartdate,act.plannedenddate,act.percentagecompletion,product.productid,tf.testfactoryid";
			ragSummaryCountList=sessionFactory.getCurrentSession().createSQLQuery(sql).list();
		}catch(Exception e) {
			log.error("Error in getRAGViewEngagementSummaryCompletedCount"+e);
		}
		
		return ragSummaryCountList;
		
	}
	
	
	@Override
	@Transactional
	public List<Object[]> getRAGViewEngagementSummaryAbortCount(Integer engagementId,Integer productId,Integer entityTypeId,Integer entityInstanceParentId) {
		List<Object[]> ragSummaryCountList=null;
		try{
			String sql="SELECT 33 AS entityTypeId,CAST('Activity Type' AS text), ABS(FLOOR(extract(epoch from(act.plannedStartDate-act.plannedEndDate))/3600)) AS totalHours,"+
					   " CASE WHEN ABS(FLOOR(extract(epoch from(act.plannedStartDate-act.plannedEndDate))/3600)) =0 THEN (act.plannedEndDate+INTERVAL '1 DAY') ELSE act.plannedEndDate END AS expectedEnd,"+
					   " CASE WHEN ABS(FLOOR(extract(epoch from(act.plannedStartDate-act.plannedEndDate))/3600)) =0 THEN (ABS(FLOOR(extract(epoch from(act.plannedStartDate-act.plannedEndDate))/3600))+24) ELSE ABS(FLOOR(extract(epoch from(act.plannedStartDate-act.plannedEndDate))/3600)) END AS exptotalHours,"+
					   " ABS(FLOOR(extract(epoch from(NOW()-CASE WHEN ABS(FLOOR(extract(epoch from(act.plannedStartDate-act.plannedEndDate))/3600)) =0 THEN (act.plannedEndDate+INTERVAL '1 DAY') ELSE act.plannedEndDate END))/3600)) AS remaingHours,"+
					   " ROUND(CAST(ABS(FLOOR(extract(epoch from(NOW()-CASE WHEN ABS(FLOOR(extract(epoch from(act.plannedStartDate-act.plannedEndDate))/3600)) =0 THEN (act.plannedEndDate+INTERVAL '1 DAY') ELSE act.plannedEndDate END))/3600))"+
					   " / CASE WHEN ABS(FLOOR(extract(epoch from(act.plannedStartDate-act.plannedEndDate))/3600)) =0 THEN (ABS(FLOOR(extract(epoch from(act.plannedStartDate-act.plannedEndDate))/3600))+24) ELSE ABS(FLOOR(extract(epoch from(act.plannedStartDate-act.plannedEndDate))/3600)) END*100 as numeric),2) AS remaingHoursPercentage,"+
					   " COALESCE(100-act.percentageCompletion,0) AS remainingCompletion,(CASE WHEN (ROUND(CAST(ABS(FLOOR(extract(epoch from(NOW()-CASE WHEN ABS(FLOOR(extract(epoch from(act.plannedStartDate-act.plannedEndDate))/3600)) =0"+
					   " THEN (act.plannedEndDate+INTERVAL '1 DAY') ELSE act.plannedEndDate END))/3600))/CASE WHEN ABS(FLOOR(extract(epoch from(act.plannedStartDate-act.plannedEndDate))/3600)) =0 THEN (ABS(FLOOR(extract(epoch from(act.plannedStartDate-act.plannedEndDate))/3600))+24)"+
					   " ELSE ABS(FLOOR(extract(epoch from(act.plannedStartDate-act.plannedEndDate))/3600)) END*100 as numeric),2) >=100-act.percentageCompletion) THEN 'green' WHEN ((ROUND(CAST(ABS(FLOOR(extract(epoch from(NOW()-CASE WHEN ABS(FLOOR(extract(epoch from(act.plannedStartDate-act.plannedEndDate))/3600)) =0"+
					   " THEN (act.plannedEndDate+INTERVAL '1 DAY') ELSE act.plannedEndDate END))/3600))/CASE WHEN ABS(FLOOR(extract(epoch from(act.plannedStartDate-act.plannedEndDate))/3600)) =0 THEN (ABS(FLOOR(extract(epoch from(act.plannedStartDate-act.plannedEndDate))/3600))+24)"+
					   " ELSE ABS(FLOOR(extract(epoch from(act.plannedStartDate-act.plannedEndDate))/3600)) END*100 as numeric),2) < 100-act.percentageCompletion) AND ((100-act.percentageCompletion - ROUND(CAST(ABS(FLOOR(extract(epoch from(NOW()-CASE WHEN ABS(FLOOR(extract(epoch from(act.plannedStartDate-act.plannedEndDate))/3600)) =0"+
					   " THEN (act.plannedEndDate+INTERVAL '1 DAY') ELSE act.plannedEndDate END))/3600))/CASE WHEN ABS(FLOOR(extract(epoch from(act.plannedStartDate-act.plannedEndDate))/3600)) =0 THEN (ABS(FLOOR(extract(epoch from(act.plannedStartDate-act.plannedEndDate))/3600))+24)"+
					   " ELSE ABS(FLOOR(extract(epoch from(act.plannedStartDate-act.plannedEndDate))/3600)) END*100 as numeric),2)) <50)) THEN 'orange' ELSE 'red' END) AS indicator,COUNT(DISTINCT act.activityId) AS COUNT,product.productId,product.productName,tf.testFactoryId,tf.testFactoryName,wp.activityWorkPackageId,wp.activityWorkPackageName"+
					   " FROM activity_work_package wp,Activity act,test_factory tf,product_master product,product_build build,product_version_list_master pversion,wf_workflows_status wfStatus WHERE act.activityWorkPackageId=wp.activityWorkPackageId AND wp.productbuild=build.productBuildId AND"+
					   " build.productVersionId=pversion.productVersionListId AND product.productId=pversion.productId AND tf.testFactoryId=product.testFactoryId AND wfStatus.workflowStatusId=act.workflowStatusId";
			if(engagementId != null && engagementId!=0) {
				sql+=" and tf.testFactoryId="+engagementId;
			}
			if(productId !=null && productId!=0) {
				sql+=" and product.productId="+productId;
			}
			if(entityInstanceParentId !=null && entityInstanceParentId!=0) {
				sql+=" and wp.activityWorkPackageId="+entityInstanceParentId;
			}
			
			sql+= "	AND wfStatus.workflowStatusId = act.WorkflowStatusId AND workflowStatusType like 'Abort' GROUP BY wp.activityWorkPackageId,act.plannedstartdate,act.plannedenddate,act.percentagecompletion,product.productid,tf.testfactoryid";
			ragSummaryCountList=sessionFactory.getCurrentSession().createSQLQuery(sql).list();
		}catch(Exception e) {
			log.error("Error in getRAGViewEngagementSummaryCompletedCount"+e);
		}
		
		return ragSummaryCountList;
		
	}
	
	
	@Transactional
	public List<Object[]> getWorkflowRAGActivityGroupingSummaryTotalCount(Integer engagementId,Integer productId,Integer entityTypeId,Integer parentEntityInstanceId) {
		List<Object[]> ragSummaryCountList=null;
		try{
			String sql="SELECT COUNT(DISTINCT act.activityId) AS COUNT FROM activity_work_package wp,Activity act,test_factory tf,product_master product,product_build build,product_version_list_master pversion,wf_workflows_status wfStatus"
					+ " WHERE act.activityWorkPackageId=wp.activityWorkPackageId AND wp.productbuild=build.productBuildId AND build.productVersionId=pversion.productVersionListId AND product.productId=pversion.productId AND tf.testFactoryId=product.testFactoryId AND wfStatus.workflowStatusId=act.workflowStatusId";
			if(engagementId !=null && engagementId!=0) {
				sql+=" and tf.testFactoryId="+engagementId;
			}
			if(productId !=null && productId!=0) {
				sql+=" and product.productId="+productId;
			}
			
			if(parentEntityInstanceId !=null && parentEntityInstanceId!=0) {
				sql+=" and wp.activityWorkPackageId="+parentEntityInstanceId;
			}
			sql+= "	AND wfStatus.workflowStatusId = act.workflowStatusId AND wfStatus.workflowStatusId >0";
			
			
			ragSummaryCountList=sessionFactory.getCurrentSession().createSQLQuery(sql).list();
		}catch(Exception e) {
			log.error("Error in getWorkflowRAGActivityGroupingSummaryTotalCount"+e);
		}
	return ragSummaryCountList;
	}
	
	
	@Override
	@Transactional
	 public List<Object[]> getWorkflowRAGActivityCategoriesSummaryCount(Integer engagementId,Integer productId, Integer entityTypeId,Integer entityParentInstanceId) {
		List<Object[]> ragSummaryCountList=null;
		try{
			String sql="SELECT 33 AS entityTypeId,CAST('Activity Type' AS text) as ActivityType, ABS(FLOOR(extract(epoch from(act.plannedStartDate-act.plannedEndDate))/3600)) AS totalHours,"+
					   " CASE WHEN ABS(FLOOR(extract(epoch from(act.plannedStartDate-act.plannedEndDate))/3600)) =0 THEN (act.plannedEndDate+INTERVAL '1 DAY') ELSE act.plannedEndDate END AS expectedEnd,"+
					   " CASE WHEN ABS(FLOOR(extract(epoch from(act.plannedStartDate-act.plannedEndDate))/3600)) =0 THEN (ABS(FLOOR(extract(epoch from(act.plannedStartDate-act.plannedEndDate))/3600))+24) ELSE ABS(FLOOR(extract(epoch from(act.plannedStartDate-act.plannedEndDate))/3600)) END AS exptotalHours,"+
					   " ABS(FLOOR(extract(epoch from(NOW()-CASE WHEN ABS(FLOOR(extract(epoch from(act.plannedStartDate-act.plannedEndDate))/3600)) =0 THEN (act.plannedEndDate+INTERVAL '1 DAY') ELSE act.plannedEndDate END))/3600)) AS remaingHours,"+
					   " ROUND(CAST(ABS(FLOOR(extract(epoch from(NOW()-CASE WHEN ABS(FLOOR(extract(epoch from(act.plannedStartDate-act.plannedEndDate))/3600)) =0 THEN (act.plannedEndDate+INTERVAL '1 DAY') ELSE act.plannedEndDate END))/3600))"+
					   " / (CASE WHEN ABS(FLOOR(extract(epoch from(act.plannedStartDate-act.plannedEndDate))/3600)) =0 THEN (ABS(FLOOR(extract(epoch from(act.plannedStartDate-act.plannedEndDate))/3600))+24) ELSE ABS(FLOOR(extract(epoch from(act.plannedStartDate-act.plannedEndDate))/3600)) END)*100 as numeric),2) AS remaingHoursPercentage,"+
					   " COALESCE(100-act.percentageCompletion,0) AS remainingCompletion,(CASE WHEN (ROUND(CAST(ABS(FLOOR(extract(epoch from(NOW()-CASE WHEN ABS(FLOOR(extract(epoch from(act.plannedStartDate-act.plannedEndDate))/3600)) =0 THEN (act.plannedEndDate+INTERVAL '1 DAY') ELSE act.plannedEndDate END))/3600))"+
					   " / (CASE WHEN ABS(FLOOR(extract(epoch from(act.plannedStartDate-act.plannedEndDate))/3600)) =0 THEN (ABS(FLOOR(extract(epoch from(act.plannedStartDate-act.plannedEndDate))/3600))+24) ELSE ABS(FLOOR(extract(epoch from(act.plannedStartDate-act.plannedEndDate))/3600)) END)*100 as numeric),2) >=100-act.percentageCompletion)"+
					   " THEN 'green' WHEN ((ROUND(CAST(ABS(FLOOR(extract(epoch from(NOW()-CASE WHEN ABS(FLOOR(extract(epoch from(act.plannedStartDate-act.plannedEndDate))/3600)) =0 THEN (act.plannedEndDate+INTERVAL '1 DAY') ELSE act.plannedEndDate END))/3600))"+
					   " / (CASE WHEN ABS(FLOOR(extract(epoch from(act.plannedStartDate-act.plannedEndDate))/3600)) =0 THEN (ABS(FLOOR(extract(epoch from(act.plannedStartDate-act.plannedEndDate))/3600))+24) ELSE ABS(FLOOR(extract(epoch from(act.plannedStartDate-act.plannedEndDate))/3600)) END)*100 as numeric),2) < 100-act.percentageCompletion)"+
					   " AND ((100-act.percentageCompletion - ROUND(CAST(ABS(FLOOR(extract(epoch from(NOW()-CASE WHEN ABS(FLOOR(extract(epoch from(act.plannedStartDate-act.plannedEndDate))/3600)) =0 THEN (act.plannedEndDate+INTERVAL '1 DAY') ELSE act.plannedEndDate END))/3600))"+
					   " / (CASE WHEN ABS(FLOOR(extract(epoch from(act.plannedStartDate-act.plannedEndDate))/3600)) =0 THEN (ABS(FLOOR(extract(epoch from(act.plannedStartDate-act.plannedEndDate))/3600))+24) ELSE ABS(FLOOR(extract(epoch from(act.plannedStartDate-act.plannedEndDate))/3600)) END)*100 as numeric),2)) <50)) THEN 'orange' ELSE 'red' END) AS indicator,"+
					   " COUNT(DISTINCT act.activityId) AS COUNT,product.productId,product.productName,tf.testFactoryId,tf.testFactoryName,wp.activityWorkPackageId,wp.activityWorkPackageName,actCtgy.activityMasterId,actCtgy.activityMasterName FROM activity_work_package wp,Activity act,activity_master actCtgy,test_factory tf,product_master product,product_build build,product_version_list_master pversion,wf_workflows_status wfStatus"+
					   " WHERE act.activityWorkPackageId=wp.activityWorkPackageId AND wp.productbuild=build.productBuildId AND build.productVersionId=pversion.productVersionListId AND product.productId=pversion.productId AND tf.testFactoryId=product.testFactoryId AND wfStatus.workflowStatusId=act.workflowStatusId and  actCtgy.activityMasterId=act.activityMasterId";
			
			if(engagementId != null && engagementId!=0) {
				sql+=" and tf.testFactoryId="+engagementId;
			}
			if(productId !=null && productId!=0) {
				sql+=" and product.productId="+productId;
			}
			if(entityParentInstanceId !=null && entityParentInstanceId!=0) {
				sql+=" and wp.activityWorkPackageId="+entityParentInstanceId;
			}
			
			sql+= "	AND wfStatus.workflowStatusId = act.WorkflowStatusId AND workflowStatusType <> 'End' AND workflowStatusType <> 'Abort'";
			sql+=" GROUP BY tf.testFactoryId,product.productId,wp.activityWorkPackageId,act.plannedStartDate,act.plannedEndDate,act.percentagecompletion,indicator,actCtgy.activityMasterId order by product.productId,wp.activityWorkPackageId asc";
			ragSummaryCountList=sessionFactory.getCurrentSession().createSQLQuery(sql).list();
		}catch(Exception e) {
			log.error("Error in getWorkflowRAGActivityCategoriesSummaryCount"+e);
		}
	return ragSummaryCountList;
	}
	 
	 
	@Override
	@Transactional
	public List<Object[]> getWorkflowRAGActivityCategoriesSummaryCompletedCount(Integer engagementId,Integer productId, Integer entityTypeId,Integer entityParentInstanceId) {
			List<Object[]> ragSummaryCountList=null;
			try{
				String sql="SELECT 33 AS entityTypeId,CAST('Activity Type' AS text), ABS(FLOOR(extract(epoch from(act.plannedStartDate-act.plannedEndDate))/3600)) AS totalHours,"+
						   " CASE WHEN ABS(FLOOR(extract(epoch from(act.plannedStartDate-act.plannedEndDate))/3600)) =0 THEN (act.plannedEndDate+INTERVAL '1 DAY') ELSE act.plannedEndDate END AS expectedEnd,"+
						   " CASE WHEN ABS(FLOOR(extract(epoch from(act.plannedStartDate-act.plannedEndDate))/3600)) =0 THEN (ABS(FLOOR(extract(epoch from(act.plannedStartDate-act.plannedEndDate))/3600))+24)"+
						   " ELSE ABS(FLOOR(extract(epoch from(act.plannedStartDate-act.plannedEndDate))/3600)) END AS exptotalHours,"+
						   " ABS(FLOOR(extract(epoch from(NOW()-CASE WHEN ABS(FLOOR(extract(epoch from(act.plannedStartDate-act.plannedEndDate))/3600)) =0 THEN (act.plannedEndDate+INTERVAL '1 DAY') ELSE act.plannedEndDate END))/3600)) AS remaingHours,"+
						   " ROUND(CAST(ABS(FLOOR(extract(epoch from(NOW()-CASE WHEN ABS(FLOOR(extract(epoch from(act.plannedStartDate-act.plannedEndDate))/3600)) =0 THEN (act.plannedEndDate+INTERVAL '1 DAY') ELSE act.plannedEndDate END))/3600))"+
						   " / CASE WHEN ABS(FLOOR(extract(epoch from(act.plannedStartDate-act.plannedEndDate))/3600)) =0 THEN (ABS(FLOOR(extract(epoch from(act.plannedStartDate-act.plannedEndDate))/3600))+24)"+
						   " ELSE ABS(FLOOR(extract(epoch from(act.plannedStartDate-act.plannedEndDate))/3600)) END*100 as numeric),2) AS remaingHoursPercentage,"+
						   " COALESCE(100-act.percentageCompletion,0) AS remainingCompletion,(CASE WHEN (ROUND(CAST(ABS(FLOOR(extract(epoch from(NOW()-CASE WHEN ABS(FLOOR(extract(epoch from(act.plannedStartDate-act.plannedEndDate))/3600)) =0"+
						   " THEN (act.plannedEndDate+INTERVAL '1 DAY') ELSE act.plannedEndDate END))/3600))/CASE WHEN ABS(FLOOR(extract(epoch from(act.plannedStartDate-act.plannedEndDate))/3600)) =0"+
						   " THEN (ABS(FLOOR(extract(epoch from(act.plannedStartDate-act.plannedEndDate))/3600))+24) ELSE ABS(FLOOR(extract(epoch from(act.plannedStartDate-act.plannedEndDate))/3600)) END*100 as numeric),2) >=100-act.percentageCompletion)"+
						   " THEN 'green' WHEN ((ROUND(CAST(ABS(FLOOR(extract(epoch from(NOW()-CASE WHEN ABS(FLOOR(extract(epoch from(act.plannedStartDate-act.plannedEndDate))/3600)) =0 THEN (act.plannedEndDate+INTERVAL '1 DAY') ELSE act.plannedEndDate END))/3600))"+
						   " / CASE WHEN ABS(FLOOR(extract(epoch from(act.plannedStartDate-act.plannedEndDate))/3600)) =0 THEN (ABS(FLOOR(extract(epoch from(act.plannedStartDate-act.plannedEndDate))/3600))+24)"+
						   " ELSE ABS(FLOOR(extract(epoch from(act.plannedStartDate-act.plannedEndDate))/3600)) END*100 as numeric),2) < 100-act.percentageCompletion) AND ((100-act.percentageCompletion - "+
						   " ROUND(CAST(ABS(FLOOR(extract(epoch from(NOW()-CASE WHEN ABS(FLOOR(extract(epoch from(act.plannedStartDate-act.plannedEndDate))/3600)) =0 THEN (act.plannedEndDate+INTERVAL '1 DAY') ELSE act.plannedEndDate END))/3600))"+
						   " / CASE WHEN ABS(FLOOR(extract(epoch from(act.plannedStartDate-act.plannedEndDate))/3600)) =0 THEN (ABS(FLOOR(extract(epoch from(act.plannedStartDate-act.plannedEndDate))/3600))+24)"+
						   " ELSE ABS(FLOOR(extract(epoch from(act.plannedStartDate-act.plannedEndDate))/3600)) END*100 as numeric),2)) <50)) THEN 'orange' ELSE 'red' END) AS indicator,"+
						   " COUNT(DISTINCT act.activityId) AS COUNT,product.productId,product.productName,tf.testFactoryId,tf.testFactoryName,wp.activityWorkPackageId,wp.activityWorkPackageName,actCtgy.activityMasterId,actCtgy.activityMasterName"+
						   " FROM activity_work_package wp,Activity act,activity_master actCtgy,test_factory tf,product_master product,product_build build,product_version_list_master pversion,wf_workflows_status wfStatus WHERE act.activityWorkPackageId=wp.activityWorkPackageId"+
						   " AND wp.productbuild=build.productBuildId AND build.productVersionId=pversion.productVersionListId AND product.productId=pversion.productId AND tf.testFactoryId=product.testFactoryId AND wfStatus.workflowStatusId=act.workflowStatusId and  actCtgy.activityMasterId=act.activityMasterId";
				
				
				if(engagementId != null && engagementId!=0) {
					sql+=" and tf.testFactoryId="+engagementId;
				}
				if(productId !=null && productId!=0) {
					sql+=" and product.productId="+productId;
				}
				if(entityParentInstanceId !=null && entityParentInstanceId!=0) {
					sql+=" and wp.activityWorkPackageId="+entityParentInstanceId;
				}
				
				sql+= "	AND wfStatus.workflowStatusId = act.WorkflowStatusId AND workflowStatusType like 'End'";
				sql+=" GROUP BY product.productId,wp.activityWorkPackageId,actCtgy.activityMasterId,act.plannedstartdate,act.plannedenddate,act.percentagecompletion,tf.testfactoryid order by product.productId,wp.activityWorkPackageId asc";
				ragSummaryCountList=sessionFactory.getCurrentSession().createSQLQuery(sql).list();
			}catch(Exception e) {
				log.error("Error in getWorkflowRAGActivityCategoriesSummaryCompletedCount"+e);
			}
		return ragSummaryCountList;
		}
		 
		 
		 @Override
		 @Transactional
		 public List<Object[]> getWorkflowRAGActivityCategoriesSummaryAbortCount(Integer engagementId,Integer productId, Integer entityTypeId,Integer entityParentInstanceId) {
				List<Object[]> ragSummaryCountList=null;
				try{
					String sql="SELECT 33 AS entityTypeId,CAST('Activity Type' AS text), ABS(FLOOR(extract(epoch from(act.plannedStartDate-act.plannedEndDate))/3600)) AS totalHours,CASE WHEN ABS(FLOOR(extract(epoch from(act.plannedStartDate-act.plannedEndDate))/3600)) =0"+
							   " THEN (act.plannedEndDate+INTERVAL '1 DAY') ELSE act.plannedEndDate END AS expectedEnd,CASE WHEN ABS(FLOOR(extract(epoch from(act.plannedStartDate-act.plannedEndDate))/3600)) =0 THEN (ABS(FLOOR(extract(epoch from(act.plannedStartDate-act.plannedEndDate))/3600))+24)"+
							   " ELSE ABS(FLOOR(extract(epoch from(act.plannedStartDate-act.plannedEndDate))/3600)) END AS exptotalHours,ABS(FLOOR(extract(epoch from(NOW()-CASE WHEN ABS(FLOOR(extract(epoch from(act.plannedStartDate-act.plannedEndDate))/3600)) =0 THEN (act.plannedEndDate+INTERVAL '1 DAY') ELSE act.plannedEndDate END))/3600)) AS remaingHours,"+
							   " ROUND(CAST(ABS(FLOOR(extract(epoch from(NOW()-CASE WHEN ABS(FLOOR(extract(epoch from(act.plannedStartDate-act.plannedEndDate))/3600)) =0 THEN (act.plannedEndDate+INTERVAL '1 DAY') ELSE act.plannedEndDate END))/3600))/CASE WHEN ABS(FLOOR(extract(epoch from(act.plannedStartDate-act.plannedEndDate))/3600)) =0 THEN"+
							   " (ABS(FLOOR(extract(epoch from(act.plannedStartDate-act.plannedEndDate))/3600))+24) ELSE ABS(FLOOR(extract(epoch from(act.plannedStartDate-act.plannedEndDate))/3600)) END*100 as numeric),2) AS remaingHoursPercentage,COALESCE(100-act.percentageCompletion,0) AS remainingCompletion,(CASE WHEN (ROUND(CAST(ABS(FLOOR(extract(epoch from(NOW()-CASE"+
							   " WHEN ABS(FLOOR(extract(epoch from(act.plannedStartDate-act.plannedEndDate))/3600)) =0 THEN (act.plannedEndDate+INTERVAL '1 DAY') ELSE act.plannedEndDate END))/3600))/CASE WHEN ABS(FLOOR(extract(epoch from(act.plannedStartDate-act.plannedEndDate))/3600)) =0 THEN (ABS(FLOOR(extract(epoch from(act.plannedStartDate-act.plannedEndDate))/3600))+24)"+
							   " ELSE ABS(FLOOR(extract(epoch from(act.plannedStartDate-act.plannedEndDate))/3600)) END*100 as numeric),2) >=100-act.percentageCompletion) THEN 'green' WHEN ((ROUND(CAST(ABS(FLOOR(extract(epoch from(NOW()-CASE WHEN ABS(FLOOR(extract(epoch from(act.plannedStartDate-act.plannedEndDate))/3600)) =0 THEN (act.plannedEndDate+INTERVAL '1 DAY') ELSE act.plannedEndDate END))/3600))"+
							   " / CASE WHEN ABS(FLOOR(extract(epoch from(act.plannedStartDate-act.plannedEndDate))/3600)) =0 THEN (ABS(FLOOR(extract(epoch from(act.plannedStartDate-act.plannedEndDate))/3600))+24) ELSE ABS(FLOOR(extract(epoch from(act.plannedStartDate-act.plannedEndDate))/3600)) END*100 as numeric),2) < 100-act.percentageCompletion) AND ((100-act.percentageCompletion -"+
							   " ROUND(CAST(ABS(FLOOR(extract(epoch from(NOW()-CASE WHEN ABS(FLOOR(extract(epoch from(act.plannedStartDate-act.plannedEndDate))/3600)) =0 THEN (act.plannedEndDate+INTERVAL '1 DAY') ELSE act.plannedEndDate END))/3600))/CASE WHEN ABS(FLOOR(extract(epoch from(act.plannedStartDate-act.plannedEndDate))/3600)) =0 THEN (ABS(FLOOR(extract(epoch from(act.plannedStartDate-act.plannedEndDate))/3600))+24)"+
							   " ELSE ABS(FLOOR(extract(epoch from(act.plannedStartDate-act.plannedEndDate))/3600)) END*100 as numeric),2)) <50)) THEN 'orange' ELSE 'red' END) AS indicator,COUNT(DISTINCT act.activityId) AS COUNT,product.productId,product.productName,tf.testFactoryId,tf.testFactoryName,wp.activityWorkPackageId,wp.activityWorkPackageName,actCtgy.activityMasterId,actCtgy.activityMasterName"+
							   " FROM activity_work_package wp,Activity act,activity_master actCtgy,test_factory tf,product_master product,product_build build,product_version_list_master pversion,wf_workflows_status wfStatus WHERE act.activityWorkPackageId=wp.activityWorkPackageId AND wp.productbuild=build.productBuildId AND build.productVersionId=pversion.productVersionListId AND product.productId=pversion.productId"+
							   " AND tf.testFactoryId=product.testFactoryId AND wfStatus.workflowStatusId=act.workflowStatusId and  actCtgy.activityMasterId=act.activityMasterId";
					
					
					if(engagementId != null && engagementId!=0) {
						sql+=" and tf.testFactoryId="+engagementId;
					}
					if(productId !=null && productId!=0) {
						sql+=" and product.productId="+productId;
					}
					if(entityParentInstanceId !=null && entityParentInstanceId!=0) {
						sql+=" and wp.activityWorkPackageId="+entityParentInstanceId;
					}
					
					sql+= "	AND wfStatus.workflowStatusId = act.WorkflowStatusId AND workflowStatusType like 'Abort'";
					sql+=" GROUP BY product.productId,wp.activityWorkPackageId,actCtgy.activityMasterId,act.plannedstartdate,act.plannedenddate,act.percentagecompletion,tf.testfactoryid order by product.productId,wp.activityWorkPackageId asc";
					ragSummaryCountList=sessionFactory.getCurrentSession().createSQLQuery(sql).list();
				}catch(Exception e) {
					log.error("Error in getWorkflowRAGActivityCategoriesSummaryAbortCount"+e);
				}
			return ragSummaryCountList;
			}
		 
		 
		 @Override
		 @Transactional
		 public List<Object[]> getBeginStatusActivities(Integer productId, Integer entityTypeId) {
				List<Object[]> beginStageList=null;
				try {
					String sql="SELECT act.activityId,act.activityName,wfStatus.workflowStatusName,actor.userId,users.loginId,users.emailId,act.reviewerId,product.productName,wp.activityWorkPackageName FROM wf_entity_instance_status_actors_mapping actor"+
								" LEFT JOIN activity act ON (actor.entityInstanceId=act.activityId)"+
								" LEFT JOIN activity_work_package wp ON act.activityWorkPackageId=wp.activityWorkPackageId"+
								" LEFT JOIN product_master product ON product.productId=actor.productId"+
								" LEFT JOIN wf_workflows_status wfStatus ON (act.workflowStatusId=wfStatus.workflowstatusId)"+
								" LEFT JOIN user_list users ON users.userId=actor.userId  WHERE act.workflowStatusId >0 AND (wfStatus.workflowStatusType LIKE '"+IDPAConstants.WORKFLOW_STATUS_TYPE_BEGIN+"' OR wfStatus.workflowStatusType LIKE '"+IDPAConstants.WORKFLOW_STATUS_TYPE_INTERMEDIATE+"')AND users.userId IS NOT NULL AND users.userId >0 ";
					if(productId != null && productId >0) {
						sql+=" and actor.productId="+productId;
					}
					if(entityTypeId != null && entityTypeId >0) {
						sql+=" and actor.entityTypeId="+entityTypeId;
					}
					sql+=" GROUP BY act.activityId,act.activityName ORDER BY users.userId ASC";
					beginStageList=sessionFactory.getCurrentSession().createSQLQuery(sql).list();
				}catch(Exception e) {
					log.error("Error in Service getBeginStatusActivities",e);
				}
				return beginStageList;
		 }

		@Override
		@Transactional
		public List<Object[]> getBeginStageActivitiesForWeeklyReport(Integer productId, Integer entityTypeId) {
			List<Object[]> beginStageList=null;
			try {
				String sql="SELECT wp.activityWorkPackageName,act.activityname,wstatus.workflowStatusName,act.plannedEndDate,act.activityId FROM activity act "+ 
							" LEFT JOIN activity_work_package wp ON act.activityWorkPackageId=wp.activityWorkPackageId"+
							" LEFT JOIN wf_workflows_status wstatus ON wstatus.workflowStatusId=act.workflowStatusId"+
							" LEFT JOIN wf_status_category category ON category.workflowStatusCategoryId = wstatus.workflowStatusCategoryId"+
							" WHERE act.activityid IN (SELECT DISTINCT entityInstanceId FROM wf_workflow_events event"+
							" LEFT JOIN wf_workflows_status wfstatus ON wfstatus.workflowStatusId=event.targetStatusId"+
							" WHERE lastUpdatedDate >= CURDATE() - INTERVAL DAYOFWEEK(CURDATE())+6 DAY"+
							" AND lastUpdatedDate < CURDATE() - INTERVAL DAYOFWEEK(CURDATE())-1 DAY AND category.workflowStatusCategoryName LIKE 'Pipeline' AND event.productId="+productId+" and event.entityTypeId="+entityTypeId+") ORDER BY wp.activityWorkPackageId";
				beginStageList=sessionFactory.getCurrentSession().createSQLQuery(sql).list();
			}catch(RuntimeException re) {
				log.error(" Error in getBeginStageActivitiesForWeeklyReport",re);
			}
			return beginStageList;
		}

		@Override
		@Transactional
		public List<Object[]> getIntermediateStageActivitiesForWeeklyReport(Integer productId, Integer entityTypeId) {
			
			List<Object[]> intermediateStageList=null;
			try {
				String sql="SELECT wp.activityWorkPackageName,act.activityname,wstatus.workflowStatusName,act.plannedEndDate,act.activityId  FROM activity act "+ 
							" LEFT JOIN activity_work_package wp ON act.activityWorkPackageId=wp.activityWorkPackageId"+
							" LEFT JOIN wf_workflows_status wstatus ON wstatus.workflowStatusId=act.workflowStatusId"+
							" LEFT JOIN wf_status_category category ON category.workflowStatusCategoryId = wstatus.workflowStatusCategoryId"+
							" WHERE act.activityid IN (SELECT DISTINCT entityInstanceId FROM wf_workflow_events event"+
							" LEFT JOIN wf_workflows_status wfstatus ON wfstatus.workflowStatusId=event.targetStatusId"+
							" WHERE lastUpdatedDate >= CURDATE() - INTERVAL DAYOFWEEK(CURDATE())+6 DAY"+
							" AND lastUpdatedDate < CURDATE() - INTERVAL DAYOFWEEK(CURDATE())-1 DAY AND category.workflowStatusCategoryName LIKE 'WIP' AND event.productId="+productId+" and event.entityTypeId="+entityTypeId+") ORDER BY wp.activityWorkPackageId";
				intermediateStageList=sessionFactory.getCurrentSession().createSQLQuery(sql).list();
			}catch(RuntimeException re) {
				log.error(" Error in getIntermediateStageActivitiesForWeeklyReport",re);
			}
			return intermediateStageList;
		}

		@Override
		@Transactional
		public List<Object[]> getEndStageActivitiesForWeeklyReport(Integer productId, Integer entityTypeId) {
			
			List<Object[]> endStageList=null;
			try {
				String sql="SELECT wp.activityWorkPackageName,act.activityname,wstatus.workflowStatusName,act.plannedEndDate,act.activityId  FROM activity act "+ 
							" LEFT JOIN activity_work_package wp ON act.activityWorkPackageId=wp.activityWorkPackageId"+
							" LEFT JOIN wf_workflows_status wstatus ON wstatus.workflowStatusId=act.workflowStatusId"+
							" LEFT JOIN wf_status_category category ON category.workflowStatusCategoryId = wstatus.workflowStatusCategoryId"+
							" WHERE act.activityid IN (SELECT DISTINCT entityInstanceId FROM wf_workflow_events event"+
							" LEFT JOIN wf_workflows_status wfstatus ON wfstatus.workflowStatusId=event.targetStatusId"+
							" WHERE lastUpdatedDate >= CURDATE() - INTERVAL DAYOFWEEK(CURDATE())+6 DAY"+
							" AND lastUpdatedDate < CURDATE() - INTERVAL DAYOFWEEK(CURDATE())-1 DAY AND category.workflowStatusCategoryName LIKE 'Completed' AND event.productId="+productId+" and event.entityTypeId="+entityTypeId+") ORDER BY wp.activityWorkPackageId";
				endStageList=sessionFactory.getCurrentSession().createSQLQuery(sql).list();
			}catch(RuntimeException re) {
				log.error(" Error in getEndStageActivitiesForWeeklyReport",re);
			}
			return endStageList;
		}

		@Override
		@Transactional
		public List<Object[]> getWorkpackageLevelActivitiesCountForWeekly(Integer productId, Integer entityTypeId) {
			List<Object[]> workpackageLevelCountForWeeklyReportList=null;
			try {
				String sql="SELECT wp.activityWorkPackageName,wcategory.workflowStatusCategoryName,COUNT(act.activityId) FROM activity act"+ 
						   " LEFT JOIN activity_work_package wp ON act.activityWorkPackageId=wp.activityWorkPackageId"+
						   " LEFT JOIN product_build build ON wp.productbuild=build.productBuildId"+
						   " LEFT JOIN product_version_list_master pversion ON build.productVersionId=pversion.productVersionListId"+
						   " LEFT JOIN product_master product ON product.productId=pversion.productId"+ 
						   " LEFT JOIN wf_workflows_status wstatus ON wstatus.workflowStatusId=act.workflowStatusId"+
						   " LEFT JOIN wf_status_category wcategory ON wcategory.workflowStatusCategoryId=wstatus.workflowStatusCategoryId"+
						   " WHERE product.productId="+productId+" GROUP BY wp.activityWorkPackageId,wstatus.workflowStatusCategoryId ORDER BY  wp.activityWorkPackageId ASC";
				workpackageLevelCountForWeeklyReportList=sessionFactory.getCurrentSession().createSQLQuery(sql).list();
			}catch(RuntimeException re) {
				log.error("Error in getWorkpackageLevelActivitiesCountForWeekly",re);
			}
			return workpackageLevelCountForWeeklyReportList;
		}
		
		
		@Override
		@Transactional
		public List<Object[]> getAttentionActivitiesForWeeklyReport(Integer productId, Integer entityTypeId) {
			
			List<Object[]> endStageList=null;
			try {
				String sql="SELECT wp.activityWorkPackageName,act.activityname,wstatus.workflowStatusName,act.plannedEndDate,ul.firstName FROM activity act"+  
						" LEFT JOIN activity_work_package wp ON act.activityWorkPackageId=wp.activityWorkPackageId"+
						" LEFT JOIN wf_workflows_status wstatus ON wstatus.workflowStatusId=act.workflowStatusId"+
						" LEFT JOIN product_build build ON wp.productbuild=build.productBuildId"+
						" LEFT JOIN product_version_list_master pversion ON build.productVersionId=pversion.productVersionListId"+
						" LEFT JOIN product_master product ON product.productId=pversion.productId"+
						" LEFT JOIN user_list ul ON act.assigneeId=ul.userId"+
						" WHERE product.productId="+productId+" AND wstatus.workflowStatusName NOT LIKE 'OnHold' AND (act.plannedEndDate < CURDATE()) AND (wstatus.workflowStatusType LIKE 'Intermediate')  ORDER BY wp.activityWorkPackageId";
				endStageList=sessionFactory.getCurrentSession().createSQLQuery(sql).list();
			}catch(RuntimeException re) {
				log.error(" Error in getEndStageActivitiesForWeeklyReport",re);
			}
			return endStageList;
		}
		
		@Override
		@Transactional
		public List<Object[]> getBeginStageActivitiesForMonthlyReport(Integer productId, Integer entityTypeId) {
			List<Object[]> beginStageList=null;
			try {
				String sql="SELECT wp.activityWorkPackageName,act.activityname AS projectName,act.activityId,act.activityMasterId FROM activity act"+ 
						" LEFT JOIN activity_work_package wp ON act.activityWorkPackageId=wp.activityWorkPackageId"+
						" LEFT JOIN wf_workflows_status wstatus ON wstatus.workflowStatusId=act.workflowStatusId"+
						" LEFT JOIN wf_status_category category ON category.workflowStatusCategoryId = wstatus.workflowStatusCategoryId"+
						" WHERE category.workflowStatusCategoryName LIKE 'Pipeline'"+
						" AND act.activityid IN (SELECT DISTINCT entityInstanceId FROM wf_workflow_events event"+
						" LEFT JOIN wf_workflows_status wfstatus ON wfstatus.workflowStatusId=event.targetStatusId"+
						" WHERE lastUpdatedDate BETWEEN (CURRENT_DATE() - INTERVAL 1 MONTH) AND (CURRENT_DATE()+INTERVAL 1 DAY)"+
						" AND event.productId="+productId+" and event.entityTypeId="+entityTypeId+") ORDER BY wp.activityWorkPackageId";
				beginStageList=sessionFactory.getCurrentSession().createSQLQuery(sql).list();
			}catch(RuntimeException re) {
				log.error(" Error in getBeginStageActivitiesForWeeklyReport",re);
			}
			return beginStageList;
		}

		@Override
		@Transactional
		public List<Object[]> getIntermediateStageActivitiesForMonthlyReport(Integer productId, Integer entityTypeId) {
			
			List<Object[]> intermediateStageList=null;
			try {
				String sql="SELECT wp.activityWorkPackageName,act.activityname AS projectName,act.activityId,act.activityMasterId FROM activity act"+ 
						" LEFT JOIN activity_work_package wp ON act.activityWorkPackageId=wp.activityWorkPackageId"+
						" LEFT JOIN wf_workflows_status wstatus ON wstatus.workflowStatusId=act.workflowStatusId"+
						" LEFT JOIN wf_status_category category ON category.workflowStatusCategoryId = wstatus.workflowStatusCategoryId"+
						" WHERE category.workflowStatusCategoryName LIKE 'WIP'"+
						" AND act.activityid IN (SELECT DISTINCT entityInstanceId FROM wf_workflow_events event"+						
						" LEFT JOIN wf_workflows_status wfstatus ON wfstatus.workflowStatusId=event.targetStatusId"+
						" WHERE lastUpdatedDate BETWEEN (CURRENT_DATE() - INTERVAL 1 MONTH) AND (CURRENT_DATE()+INTERVAL 1 DAY)"+
						" AND event.productId="+productId+" and event.entityTypeId="+entityTypeId+") ORDER BY wp.activityWorkPackageId";
				intermediateStageList=sessionFactory.getCurrentSession().createSQLQuery(sql).list();
			}catch(RuntimeException re) {
				log.error(" Error in getIntermediateStageActivitiesForWeeklyReport",re);
			}
			return intermediateStageList;
		}

		@Override
		@Transactional
		public List<Object[]> getEndStageActivitiesForMonthlyReport(Integer productId, Integer entityTypeId) {
			List<Object[]> endStageList=null;
			try {
				String sql="SELECT wp.activityWorkPackageName,act.activityname AS projectName,act.activityId,act.activityMasterId FROM activity act"+ 
						" LEFT JOIN activity_work_package wp ON act.activityWorkPackageId=wp.activityWorkPackageId"+
						" LEFT JOIN wf_workflows_status wstatus ON wstatus.workflowStatusId=act.workflowStatusId"+
						" LEFT JOIN wf_status_category category ON category.workflowStatusCategoryId = wstatus.workflowStatusCategoryId"+
						" WHERE category.workflowStatusCategoryName LIKE 'Completed'"+
						" AND act.activityid IN (SELECT DISTINCT entityInstanceId FROM wf_workflow_events event"+
						" LEFT JOIN wf_workflows_status wfstatus ON wfstatus.workflowStatusId=event.targetStatusId"+
						" WHERE lastUpdatedDate BETWEEN (CURRENT_DATE() - INTERVAL 1 MONTH) AND (CURRENT_DATE()+INTERVAL 1 DAY)"+					
						" AND event.productId="+productId+" and event.entityTypeId="+entityTypeId+") ORDER BY wp.activityWorkPackageId";
				endStageList=sessionFactory.getCurrentSession().createSQLQuery(sql).list();
			}catch(RuntimeException re) {
				log.error(" Error in getEndStageActivitiesForWeeklyReport",re);
			}
			return endStageList;
		}
		
		@Override
		@Transactional
		public List<Object[]> getCustomFieldValuesByentityTypeAndInstanceIds(Integer entityId, Integer activityTypeId, Integer entityInstanceId) {
			List<Object[]> customFieldValList=null;
			try {
				String sql="SELECT  cfm.fieldName, cfv.fieldValue FROM custom_field_values cfv"+
						   " LEFT JOIN  custom_field_config_master cfm ON(cfv.customFieldId = cfm.id  AND (cfm.fieldName='Account' OR cfm.fieldName='Project Code'))"+
						   " WHERE cfm.entity="+entityId+" AND cfm.entityType="+activityTypeId+" AND cfv.entityInstanceId="+entityInstanceId;
				customFieldValList=sessionFactory.getCurrentSession().createSQLQuery(sql).list();
			} catch (RuntimeException re) {
				log.error(" Error in getCustomFieldValuesByentityTypeAndInstanceIds",re);
			}
			return customFieldValList;
		}
}
