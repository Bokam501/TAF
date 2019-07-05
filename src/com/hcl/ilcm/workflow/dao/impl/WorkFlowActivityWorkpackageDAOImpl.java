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

import com.hcl.atf.taf.model.ActivityWorkPackage;
import com.hcl.ilcm.workflow.dao.WorkflowActivityWorkpackageDAO;
@Repository
public class WorkFlowActivityWorkpackageDAOImpl implements WorkflowActivityWorkpackageDAO  {
private static final Log log = LogFactory.getLog(WorkFlowActivityWorkpackageDAOImpl.class);
	
	@Autowired(required=true)
	private SessionFactory sessionFactory;
	
	
	@Override
	@Transactional
	public List<ActivityWorkPackage> listActivityWorkpackageforWorkFlowForUserOrRole(Integer productId, Integer workflowId, Integer entityTypeId, Integer entityId, Integer userId, Integer roleId, Integer jtStartIndex, Integer jtPageSize) {
		log.debug("listing all activities List instance : productId - "+productId+", workflowId - "+workflowId+", entityTypeId - "+entityTypeId+", userId - "+userId+", roleId - "+roleId);
		log.info("Listing all activities List instance : productId - "+productId);
		List<ActivityWorkPackage> activityWorkPackages = new ArrayList<ActivityWorkPackage>();
		try {
			
			String activityWorkpackageForUserOrRoleQuery = "SELECT DISTINCT entityInstanceId from wf_workflow_status_actors_users_view"
			+ " WHERE "
			+ " productId = " + productId
			+ " AND entityTypeId = " + entityTypeId;
			if(entityId != null){
				activityWorkpackageForUserOrRoleQuery += " AND entityId = " + entityId;
			}
			if(workflowId != null){
				activityWorkpackageForUserOrRoleQuery += " AND workflowId = " + workflowId;
			}
			activityWorkpackageForUserOrRoleQuery += "	AND workflowStatusId = currentStatusId"
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
			
			List<Integer> activityWorkpackageIds = sessionFactory.getCurrentSession().createSQLQuery(activityWorkpackageForUserOrRoleQuery).list();
			if (activityWorkpackageIds == null || activityWorkpackageIds.isEmpty()) {
				return null;
			}
			
			Criteria activityWorkpackageCriteria = sessionFactory.getCurrentSession().createCriteria(ActivityWorkPackage.class, "activityWorkpackage");	
			activityWorkpackageCriteria.add(Restrictions.in("activityWorkpackage.activityWorkPackageId", activityWorkpackageIds));
			activityWorkpackageCriteria.addOrder(Order.desc("activityWorkpackage.activityWorkPackageId"));
			activityWorkpackageCriteria.setFirstResult(jtStartIndex);
			activityWorkpackageCriteria.setMaxResults(jtPageSize);
			activityWorkPackages = activityWorkpackageCriteria.list();
				
			log.info("list activityWorkPackages successful - "+activityWorkPackages.size());
		} catch (RuntimeException re) {
			log.error("list activityWorkPackages failed", re);
			//throw re;
		}
		return activityWorkPackages;
	}
	
	@Override
	@Transactional
	public List<ActivityWorkPackage> listActivityWorkpackageforWorkFlowSLAIndicator(Integer productId,Integer entityTypeId,List<Integer> activityWorkpackageIds,Integer jtStartIndex, Integer jtPageSize) {
		List<ActivityWorkPackage> activityWorkPackages = new ArrayList<ActivityWorkPackage>();
		try {
			
			Criteria activityCriteria = sessionFactory.getCurrentSession().createCriteria(ActivityWorkPackage.class, "activityWorkpackage");
			activityCriteria.add(Restrictions.in("activityWorkpackage.activityWorkPackageId", activityWorkpackageIds));
			activityCriteria.addOrder(Order.desc("activityWorkpackage.activityWorkPackageId"));
			activityCriteria.setFirstResult(jtStartIndex);
			activityCriteria.setMaxResults(jtPageSize);
			activityWorkPackages = activityCriteria.list();
			log.info("list activityWorkpackage successful - "+activityWorkPackages.size());
		}catch(Exception e) {
			log.error("Error in listActivitiesforWorkFlowSLAIndicator",e);
		}
		return activityWorkPackages;
	}
	
}
