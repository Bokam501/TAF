package com.hcl.ilcm.workflow.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
//import org.elasticsearch.search.aggregations.bucket.histogram.Histogram.Order;
import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.hcl.atf.taf.model.Activity;
import com.hcl.ilcm.workflow.dao.WorkflowActivityDAO;
@Repository
public class WorkFlowActivityDAOImpl implements WorkflowActivityDAO  {
private static final Log log = LogFactory.getLog(WorkFlowActivityDAOImpl.class);
	
	@Autowired(required=true)
	private SessionFactory sessionFactory;
	
	
	@Override
	@Transactional
	public List<Activity> listActivitiesforWorkFlowForUserOrRole(Integer testFactoryId, Integer productId,Integer activityWorkPackageId, Integer workflowId, Integer entityTypeId, Integer entityId, Integer userId, Integer roleId, Integer jtStartIndex, Integer jtPageSize) {
		log.debug("listing all activities List instance : productId - "+productId+", workflowId - "+workflowId+", entityTypeId - "+entityTypeId+", userId - "+userId+", roleId - "+roleId);
		List<Activity> activities = new ArrayList<Activity>();
		try {
			
			String activitiesForUserOrRoleQuery ="SELECT DISTINCT actorMapping.entityInstanceId FROM wf_entity_instance_status_actors_mapping actorMapping"+
												 " LEFT JOIN user_list users ON (users.userId=actorMapping.userId AND actorMapping.actorMappingType = 'User')"+
												 " LEFT JOIN user_role_master role ON (role.userRoleId=actorMapping.roleId AND actorMapping.actorMappingType = 'Role')"+
												 " LEFT JOIN wf_workflows_status wfstatus ON (wfstatus.workflowstatusId=actorMapping.workflowstatusId AND wfstatus.workflowStatusType <>'End')"+ 
												 " LEFT JOIN wf_workflows_status_policies policy ON (actorMapping.entityInstanceId = policy.entityInstanceId AND actorMapping.entityTypeId=policy.entityTypeId AND actorMapping.productId=policy.levelId AND wfstatus.workflowStatusId = policy.workflowStatusId AND policy.statusPolicyType = 'Instance')"+ 
												 " LEFT JOIN activity act ON (act.activityId=actorMapping.entityInstanceId AND wfstatus.workflowStatusId=act.workflowStatusId)"+ 
												 " LEFT JOIN product_master product ON (product.productId=actorMapping.productId)"+
												 " LEFT JOIN test_factory testFactory ON (product.testFactoryId=testFactory.testFactoryId)"+
												 " WHERE actorMapping.entityTypeId=33 AND workflowStatusType <> 'End'"+ 
												 " AND act.workflowStatusId>0  AND userActionStatus <> 'Completed'";
			
			if(testFactoryId != null && testFactoryId >0) {
				activitiesForUserOrRoleQuery += " AND testFactory.testFactoryId ="+testFactoryId;
			}
			
			if(productId != null && productId >0) {
				activitiesForUserOrRoleQuery += " AND actorMapping.productId ="+productId;
			}
			if(userId != null && userId>0) {
				activitiesForUserOrRoleQuery += " AND actorMapping.userId ="+userId;
			}
			
			if(activityWorkPackageId != null && activityWorkPackageId >0) {
				activitiesForUserOrRoleQuery += " AND act.activityWorkPackageId ="+activityWorkPackageId;
			}
			
			List<Integer> activityIds = sessionFactory.getCurrentSession().createSQLQuery(activitiesForUserOrRoleQuery).list();
			if (activityIds == null || activityIds.isEmpty()) {
				return null;
			}
			
			Criteria activityCriteria = sessionFactory.getCurrentSession().createCriteria(Activity.class, "activity");	
			activityCriteria.add(Restrictions.in("activity.activityId", activityIds));
			activities = activityCriteria.list();
				
			log.info("list activities successful - "+activities.size());
		} catch (RuntimeException re) {
			log.error("list activities failed", re);
		}
		return activities;
	}
	
	@Override
	@Transactional
	public List<Activity> listActivitiesforWorkFlowSLAIndicator(Integer productId,Integer entityTypeId,List<Integer> activityIds,Integer jtStartIndex, Integer jtPageSize) {
		List<Activity> activities = new ArrayList<Activity>();
		try {
			
			Criteria activityCriteria = sessionFactory.getCurrentSession().createCriteria(Activity.class, "activity");
			activityCriteria.add(Restrictions.in("activity.activityId", activityIds));
			activityCriteria.setFirstResult(jtStartIndex);
			activityCriteria.setMaxResults(jtPageSize);
			activities = activityCriteria.list();
			log.info("list activities successful - "+activities.size());
		}catch(Exception e) {
			log.error("Error in listActivitiesforWorkFlowSLAIndicator",e);
		}
		return activities;
	}
	
	
	@Override
	@Transactional
	public List<Activity> listActivitiesforWorkFlowStatusPartForUserOrRole(Integer testFactoryId,Integer productId,Integer activityWorkPackageId,Integer entityTypeId,Integer userId, Integer roleId) {
		log.debug("list Activities for WorkFlowStatus Part UserOrRole : productId - "+productId+", entityTypeId - "+entityTypeId+", userId - "+userId+", roleId - "+roleId);
		List<Activity> activities = new ArrayList<Activity>();
		try {
			
			String activitiesForUserOrRoleQuery ="SELECT DISTINCT policy.entityInstanceId FROM wf_workflows_status_policies policy"+ 
												" LEFT JOIN wf_entity_instance_status_actors_mapping actorMapping ON (actorMapping.entityInstanceId = policy.entityInstanceId AND actorMapping.entityTypeId=policy.entityTypeId AND actorMapping.productId=policy.levelId AND actorMapping.workflowStatusId = policy.workflowStatusId AND policy.statusPolicyType = 'Instance')"+
												" LEFT JOIN product_master product ON (product.productId=actorMapping.productId)"+
												" LEFT JOIN test_factory testFactory ON (product.testFactoryId=testFactory.testFactoryId)"+
												" WHERE actorMapping.entityTypeId="+entityTypeId+" AND actorMapping.userId="+userId;
			
			if(testFactoryId != null && testFactoryId >0) {
				activitiesForUserOrRoleQuery += " AND testFactory.testFactoryId ="+testFactoryId; 
			}
			
			if(productId !=null && productId >0) {
				activitiesForUserOrRoleQuery += " AND actorMapping.productId ="+productId; 
			}
			
			List<Integer> activityIds = sessionFactory.getCurrentSession().createSQLQuery(activitiesForUserOrRoleQuery).list();
			if (activityIds == null || activityIds.isEmpty()) {
				return null;
			}
			
			Criteria activityCriteria = sessionFactory.getCurrentSession().createCriteria(Activity.class, "activity");
			activityCriteria.createAlias("activity.activityWorkPackage", "awp");
			activityCriteria.add(Restrictions.in("activity.activityId", activityIds));
			if(activityWorkPackageId != null && activityWorkPackageId >0) {
				activityCriteria.add(Restrictions.eq("awp.activityWorkPackageId", activityWorkPackageId));
			}
			activities = activityCriteria.list();
				
			log.info("listActivitiesforWorkFlowStatusPartForUserOrRole - "+activities.size());
		} catch (RuntimeException re) {
			log.error("listActivitiesforWorkFlowStatusPartForUserOrRole failed", re);
			//throw re;
		}
		return activities;
	}

	@Override
	public void removeInstanceFromEntityWorkflowStatusActorsMapping(Integer entityTypeId, Integer entityId, Integer entityInstanceId) {
		try{
			String constraints = " where entityTypeId = "+entityTypeId;
			if(entityId != null){
				constraints += " and entityId = "+entityId;
			}else{
				constraints += " and entityId IS NULL";
			}
			constraints += " and entityInstanceId = "+entityInstanceId;
			
			String tableName = "delete from wf_entity_instance_status_actors_mapping";
			String queryToExecute = tableName+constraints;
			sessionFactory.getCurrentSession().createSQLQuery(queryToExecute).executeUpdate();
			
		}catch(RuntimeException re) {
			log.error("removeInstanceFromEntityWorkflowStatusActorsMapping failed", re);
		}
		
	}
	
}
