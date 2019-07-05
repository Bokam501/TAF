package com.hcl.ilcm.workflow.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
//import org.elasticsearch.search.aggregations.bucket.histogram.Histogram.Order;
import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.hcl.atf.taf.constants.IDPAConstants;
import com.hcl.atf.taf.model.ActivityTask;
import com.hcl.ilcm.workflow.dao.WorkflowTaskDAO;
@Repository
public class WorkFlowTaskDAOImpl implements WorkflowTaskDAO  {
private static final Log log = LogFactory.getLog(WorkFlowTaskDAOImpl.class);
	
	@Autowired(required=true)
	private SessionFactory sessionFactory;
	
	
	@Override
	@Transactional
	public List<ActivityTask> listActivityTaskforWorkFlow(Integer productId, String entityLevel, Integer entityLevelId,Integer taskTypeId, Integer userId,List<String> assigneeStatusList,List<String> leadStatusList,List<String> roleStatusList) {
		log.debug("list all Activity Task WorkFlow start");
		List<ActivityTask> activityTaskList = new ArrayList<ActivityTask>();
		List<ActivityTask> myLeadTasks =new ArrayList<ActivityTask>();
		List<ActivityTask> myAssigneeTasks = new ArrayList<ActivityTask>();
		try {
			Criteria assigneeCriteria = sessionFactory.getCurrentSession().createCriteria(ActivityTask.class, "activityTask");	
			assigneeCriteria.createAlias("activityTask.status", "status");
			assigneeCriteria.createAlias("activityTask.activityTaskType", "taskType");
			assigneeCriteria.createAlias("activityTask.assignee", "assignee");
			assigneeCriteria.createAlias("taskType.product", "product");
			
			if(assigneeStatusList!=null && assigneeStatusList.size() >0) {
				assigneeCriteria.add(Restrictions.in("status.workflowStatusName", assigneeStatusList));
				assigneeCriteria.add(Restrictions.eq("assignee.userId", userId));
				assigneeCriteria.add(Restrictions.eq("product.productId", productId));
				assigneeCriteria.add(Restrictions.eq("taskType.activityTaskTypeId", taskTypeId));
				assigneeCriteria.addOrder(Order.desc("activityTaskId"));
				myAssigneeTasks = assigneeCriteria.list();
			}
			
			Criteria leadCriteria = sessionFactory.getCurrentSession().createCriteria(ActivityTask.class, "activityTask");	
			leadCriteria.createAlias("activityTask.status", "status");
			leadCriteria.createAlias("activityTask.activityTaskType", "taskType");
			leadCriteria.createAlias("activityTask.activity", "activity");
			leadCriteria.createAlias("activityTask.reviewer", "reviewer");
			leadCriteria.createAlias("taskType.product", "product");
			
			if(leadStatusList!=null && leadStatusList.size() >0) {
				leadCriteria.add(Restrictions.in("status.workflowStatusName", leadStatusList));
				leadCriteria.add(Restrictions.eq("reviewer.userId", userId));
				leadCriteria.add(Restrictions.eq("product.productId", productId));
				leadCriteria.add(Restrictions.eq("taskType.activityTaskTypeId", taskTypeId));
				leadCriteria.addOrder(Order.desc("activityTaskId"));
				myLeadTasks = leadCriteria.list();
			}
			
			//Role Criteria
			Criteria roleCriteria = sessionFactory.getCurrentSession().createCriteria(ActivityTask.class, "activityTask");	
			roleCriteria.createAlias("activityTask.status", "status");
			roleCriteria.createAlias("activityTask.activity", "activity");
			roleCriteria.createAlias("activityTask.activityTaskType", "taskType");
			if(roleStatusList!=null && roleStatusList.size() >0) {
				roleCriteria.add(Restrictions.in("status.workflowStatusName", roleStatusList));
				
				if (entityLevel == null || entityLevel.trim().isEmpty() || entityLevel.trim().equalsIgnoreCase(IDPAConstants.ENTITY_PRODUCT)) {	
					roleCriteria.createAlias("taskType.product", "product");
					roleCriteria.add(Restrictions.eq("product.productId", productId));
					roleCriteria.add(Restrictions.eq("taskType.activityTaskTypeId", taskTypeId));
				} else if (entityLevel.trim().equalsIgnoreCase(IDPAConstants.ENTITY_PRODUCT_VERSION)) {
	
					roleCriteria.createAlias("activity.activityWorkPackage", "activityWp");
					roleCriteria.createAlias("activityWp.productBuild", "pbuild");
					roleCriteria.createAlias("pbuild.productVersion", "pVersion");
					roleCriteria.add(Restrictions.eq("pVersion.productVersionListId", entityLevelId));
				} else if (entityLevel.trim().equalsIgnoreCase(IDPAConstants.ENTITY_PRODUCT_BUILD)) {
					
					roleCriteria.createAlias("activity.activityWorkPackage", "activityWp");
					roleCriteria.createAlias("activityWp.productBuild", "pbuild");
					roleCriteria.add(Restrictions.eq("pbuild.productBuildId", entityLevelId));
				} else if (entityLevel.trim().equalsIgnoreCase(IDPAConstants.ENTITY_ACTIVITY_WORKPACKAGE)) {
					
					roleCriteria.createAlias("activity.activityWorkPackageId", "activityWp");
					roleCriteria.add(Restrictions.eq("activityWp.workpackageId", entityLevelId));
				}
				roleCriteria.addOrder(Order.desc("activityTaskId"));
				activityTaskList = roleCriteria.list();
			}
			activityTaskList.addAll(myAssigneeTasks);
			activityTaskList.addAll(myLeadTasks);
			log.debug("list all Activity Task WorkFlow successful");
		} catch (RuntimeException re) {
			log.error("list all Activity Task WorkFlow failed", re);
		}
		return activityTaskList;
	}
	
	@Override
	@Transactional
	public List<ActivityTask> listActivityTasksforWorkFlowForUserOrRole(Integer productId, Integer workflowId, Integer entityTypeId, Integer entityId, Integer userId, Integer roleId, Integer jtStartIndex, Integer jtPageSize) {

		log.info("listing all task List instance : productId - "+productId+", workflowId - "+workflowId+", entityTypeId - "+entityTypeId+", userId - "+userId+", roleId - "+roleId);
		List<ActivityTask> activityTasks = new ArrayList<ActivityTask>();
		try {
			
			String tasksForUserOrRoleQuery = "SELECT DISTINCT entityInstanceId from wf_workflow_status_actors_users_view"
			+ " WHERE "
			+ " productId = " + productId
			+ " AND entityTypeId = " + entityTypeId;
			if(entityId != null){
				tasksForUserOrRoleQuery += " AND entityId = " + entityId;
			}
			if(workflowId != null){
				tasksForUserOrRoleQuery += " AND workflowId = " + workflowId;
			}
			tasksForUserOrRoleQuery += "	AND workflowStatusId = currentStatusId"
			+ "	AND currentStatusId > 0"
			+ " AND workflowStatusType <> 'End'"
			+ "	AND ("
			+ 	" (userId = " + userId 
			+	" AND (actionScope = 'User' OR actionScope = 'Role or User')" 
			+ 	" AND userActionStatus <> 'Completed')" 
			+ 	" OR"
			+   " (roleId = " + roleId
			+ 	" AND (actionScope = 'Role' OR actionScope = 'Role or User'))"
			+ 	")";
			
			List<Integer> taskIds = sessionFactory.getCurrentSession().createSQLQuery(tasksForUserOrRoleQuery).list();
			log.info("Task Ids - "+taskIds);
			if (taskIds == null || taskIds.isEmpty()) {
				return null;
			}
			
			Criteria taskcaseCriteria = sessionFactory.getCurrentSession().createCriteria(ActivityTask.class, "activityTask");	
			taskcaseCriteria.add(Restrictions.in("activityTask.activityTaskId", taskIds));
			taskcaseCriteria.addOrder(Order.desc("activityTask.activityTaskId"));
			activityTasks = taskcaseCriteria.list();
				
			log.info("list tasks successful - "+activityTasks.size());
		} catch (RuntimeException re) {
			log.error("list tasks failed", re);
			//throw re;
		}
		return activityTasks;
	}
	
	@Override
	@Transactional
	public List<ActivityTask> listActivityTasksforWorkFlowSLAIndicator(Integer productId,Integer entityTypeId,List<Integer> taskIds,Integer jtStartIndex, Integer jtPageSize) {
		List<ActivityTask> activityTasks = new ArrayList<ActivityTask>();
		try {
			
			Criteria taskcaseCriteria = sessionFactory.getCurrentSession().createCriteria(ActivityTask.class, "activityTask");
			taskcaseCriteria.add(Restrictions.in("activityTask.activityTaskId", taskIds));
			taskcaseCriteria.addOrder(Order.desc("activityTask.activityTaskId"));
			taskcaseCriteria.setFirstResult(jtStartIndex);
			taskcaseCriteria.setMaxResults(jtPageSize);
			activityTasks = taskcaseCriteria.list();
			log.info("list tasks successful - "+activityTasks.size());
		}catch(Exception e) {
			log.error("Error in listActivityTasksforWorkFlowSLAIndicator",e);
		}
		return activityTasks;
	}
	
}
