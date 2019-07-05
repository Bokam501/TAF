/**
 * 
 */
package com.hcl.ilcm.workflow.dao.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projection;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.hcl.ilcm.workflow.dao.WorkflowEventDAO;
import com.hcl.ilcm.workflow.model.WorkflowEvent;

/**
 * @author silambarasur
 *
 */
@Repository
public class WorkflowEventDAOImpl implements WorkflowEventDAO{
	
private static final Log log = LogFactory.getLog(WorkflowEventDAOImpl.class);
	
	@Autowired(required=true)
    private SessionFactory sessionFactory;
	
	@Override
	@Transactional
	public List<WorkflowEvent> listWorkflowEvents(){
		log.info("listWorkflowEvents start");
		List<WorkflowEvent> workflowEventList=null;
		try{
			Criteria c = sessionFactory.getCurrentSession().createCriteria(WorkflowEvent.class, "workflowEvent");
			workflowEventList = c.list();
			log.info("listWorkflowEvents End");
		}catch(Exception e){
			log.info("listWorkflowEvents failed"+e);
		}
		return workflowEventList;
	}
	
	@Override
	@Transactional
	public WorkflowEvent getWorkflowEventById(int workflowEventId){
		List<WorkflowEvent> workflowEventList = null;
		WorkflowEvent workflowEvent = null;
		try {
			Criteria c = sessionFactory.getCurrentSession().createCriteria(WorkflowEvent.class, "workflowEvent");
			c.add(Restrictions.eq("workflowEvent.workflowEventId", workflowEventId));
			workflowEventList = c.list();
			workflowEvent = (workflowEventList!=null && workflowEventList.size()!=0)?(WorkflowEvent)workflowEventList.get(0):null;
			
		} catch (RuntimeException re) {
			log.error("List workflowEvent failed", re);
		}
		return workflowEvent;
	}
	
	@Override
	@Transactional
	public void addWorkflowEvent(WorkflowEvent workflowEvent){
		log.info("adding WorkflowEvent instance");
		try {	
			sessionFactory.getCurrentSession().save(workflowEvent);
			log.info("add WorkflowEvent successful");
		} catch (RuntimeException re) {
			log.error("add WorkflowEvent failed", re);
		}
	}
	
	
	@Override
	@Transactional
	public List<WorkflowEvent> listWorkflowEventByEntityTypeAndEnityInstanceId(Integer entityTypeId,Integer entityInstanceId, int initializationLevel){
		
		List<WorkflowEvent> workflowEventList = null;
		try {
			Criteria c = sessionFactory.getCurrentSession().createCriteria(WorkflowEvent.class, "workflowEvent");
			c.createAlias("workflowEvent.entityType", "entityTypeMaster");
			c.add(Restrictions.eq("entityTypeMaster.entitymasterid", entityTypeId));
			c.add(Restrictions.eq("workflowEvent.entityInstanceId", entityInstanceId));
			c.addOrder(Order.asc("workflowEventId"));
			workflowEventList = c.list();
			
		} catch (RuntimeException re) {
			log.error("List listworkflowEventByEntityTypeAndEnityId failed", re);
		}
		return workflowEventList;
	}
	
	@Override
	@Transactional
	public void updateWorkflowEvent(WorkflowEvent workflowEvent){
		log.info("updateWorkflowEvent instance");
		try {	
			sessionFactory.getCurrentSession().saveOrUpdate(workflowEvent);
			log.info("updateWorkflowEvent successful");
		} catch (RuntimeException re) {
			log.error("updateWorkflowEvent failed", re);
		}
	}
	
	
	@Override
	@Transactional
	public Integer getTotalEffortsByEntityInstanceIdAndEntityType(int entityInstanceId,int entityTypeId){
		log.debug("getting Total efforts");
		List<Long> workflowEventList = null;
		Integer totalEfforts = 0;
		
		try{
			if(entityInstanceId >0 && entityTypeId >0){
				Session session = sessionFactory.getCurrentSession();
				workflowEventList = session.createQuery("select sum(wfEvent.actualEffort) from WorkflowEvent wfEvent where wfEvent.entityType.entitymasterid=:entitymasterid and wfEvent.entityInstanceId=:entityInstanceId and wfEvent.actualEffort is not null").setParameter("entitymasterid", entityTypeId).setParameter("entityInstanceId", entityInstanceId)
				.list();
				
				if(workflowEventList != null && workflowEventList.size() >0){
					totalEfforts=workflowEventList.get(0) != null ?workflowEventList.get(0).intValue() :0;
					return totalEfforts;
				}
			} 
		}catch(RuntimeException re){
			log.error("list all workflow event effort failed", re);
		}
		return totalEfforts;
	}

	@Override
	@Transactional
	public Integer getTotalEffortsOfEntityOrInstanceIdForStatus(Integer productId, Integer entityTypeId, Integer entityId, Integer entityInstanceId, Integer statusId){
		log.debug("Inside getTotalEffortsOfEntityOrInstanceIdForStatus");
		List<Long> workflowEventList = null;
		Integer totalEfforts = 0;
		try{
			String projection = "select sum(wfEvent.actualEffort) from WorkflowEvent wfEvent where wfEvent.actualEffort IS NOT NULL AND wfEvent.currentStatus.workflowStatusId = "+statusId;
			String condition = "";
			if(productId != null && productId > 0){
				condition += " AND wfEvent.product.productId = "+productId;
			}
			if(entityTypeId != null && entityTypeId > 0){
				condition += " AND wfEvent.entityType.entitymasterid = "+entityTypeId;
			}else{
				condition += " AND wfEvent.entityType.entitymasterid IS NULL";
			}
			if(entityId != null && entityId > 0){
				condition += " AND wfEvent.entityId = "+entityId;
			}else{
				condition += " AND wfEvent.entityId IS NULL";
			}
			if(entityInstanceId != null && entityInstanceId > 0){
				condition += " AND wfEvent.entityInstanceId = "+entityInstanceId;
			}else{
				condition += " AND wfEvent.entityInstanceId IS NULL";
			}
			
			if(condition != null && !condition.trim().isEmpty()){
				projection += condition;
			}
			
			Session session = sessionFactory.getCurrentSession();
			workflowEventList = session.createQuery(projection).list();
			
			if(workflowEventList != null && workflowEventList.size() >0){
				totalEfforts = workflowEventList.get(0) != null ? workflowEventList.get(0).intValue() : 0;
			}
		}catch(Exception ex){
			log.error("Exception in getTotalEffortsOfEntityOrInstanceIdForStatus - ", ex);
		}
		return totalEfforts;
	}
	
	@Override
	@Transactional
	public Integer countAllEvents(Date startDate, Date endDate) {
		try {
			Criteria c = sessionFactory.getCurrentSession().createCriteria(WorkflowEvent.class,"workflowEvent");
			if (startDate != null) {
				c.add(Restrictions.ge("workflowEvent.lastUpdatedDate", startDate));
			}
			if (endDate != null) {
				c.add(Restrictions.le("workflowEvent.lastUpdatedDate", endDate));
			}
			
			c.setProjection(Projections.rowCount());
			Integer count = Integer.parseInt(c.uniqueResult().toString());
			return count;
		} catch (Exception e) {
			log.error("Unable to get count of all WorkflowEvent", e);
			return -1;
		}
	}
	
	@Override
	@Transactional
	public List<WorkflowEvent> listAllEvents(int startIndex, int pageSize, Date startDate,Date endDate) {
		log.debug("listing all Events");
		List<WorkflowEvent> workflowEventList=null;
		try {
			Criteria c = sessionFactory.getCurrentSession().createCriteria(WorkflowEvent.class, "workflowEvent");
			if (startDate != null) {
				c.add(Restrictions.ge("workflowEvent.lastUpdatedDate", startDate));
			}
			if (endDate != null) {
				c.add(Restrictions.le("workflowEvent.lastUpdatedDate", endDate));
			}
			c.addOrder(Order.asc("workflowEventId"));
	        c.setFirstResult(startIndex);
	        c.setMaxResults(pageSize);
	        workflowEventList = c.list();		
			

			log.debug("list all successful");
		} catch (RuntimeException re) {
			log.error("list all failed", re);
		}
		return workflowEventList;
	}
	
	@Override
	@Transactional
	public WorkflowEvent getLatestWorkflowEventAction(Integer entityId,Integer entityTypeId) {
		List<WorkflowEvent> workflowEventList=null;
		WorkflowEvent workflowEvent=null;
		try {
			Criteria c = sessionFactory.getCurrentSession().createCriteria(WorkflowEvent.class, "workflowEvent");
			c.createAlias("workflowEvent.entityType", "entityTypeMaster");
			c.add(Restrictions.eq("entityTypeMaster.entitymasterid", entityTypeId));
			c.add(Restrictions.eq("workflowEvent.entityId", entityId));
			c.add(Restrictions.isNotNull("workflowEvent.targetStatus.workflowStatusId"));
			c.addOrder(Order.desc("workflowEventId"));
			workflowEventList = c.list();
			if(workflowEventList!= null && workflowEventList.size() >0) {
				workflowEvent= workflowEventList.get(0);
				if(workflowEvent!= null) {
					Hibernate.initialize(workflowEvent.getTargetStatus());
					Hibernate.initialize(workflowEvent.getCurrentStatus());
				}
				return workflowEvent;
			}
		}catch(Exception e) {
			
		}
		
		return null;
	}

	@Override
	@Transactional
	public boolean checkInstanceEligibiltyToChangeWorkflowMapping(Integer entityTypeId, Integer entityId, Integer entityInstanceId) {
		boolean isEligibleToChange = false;
		log.info("Inside checkInstanceEligibiltyToChangeWorkflowMapping with parameters : entityTypeId - "+entityTypeId+", entityId - "+entityId+", entityInstanceId - "+entityInstanceId);
		try{
			Criteria c = sessionFactory.getCurrentSession().createCriteria(WorkflowEvent.class, "workflowEvent");
			c.createAlias("workflowEvent.entityType", "entityTypeMaster");
			c.add(Restrictions.eq("entityTypeMaster.entitymasterid", entityTypeId));
			if(entityId != null){
				c.add(Restrictions.eq("workflowEvent.entityId", entityId));
			}else{
				c.add(Restrictions.isNull("workflowEvent.entityId"));
			}
			c.add(Restrictions.eq("workflowEvent.entityInstanceId", entityInstanceId));
			c.add(Restrictions.isNotNull("workflowEvent.currentStatus.workflowStatusId"));

			Integer eventCount = ((Long)c.setProjection(Projections.rowCount()).uniqueResult()).intValue();
			if(eventCount == null || eventCount == 0){
				isEligibleToChange = true;
			}
		}catch(Exception ex){
			log.error("Error in checkInstanceEligibiltyToChangeWorkflowMapping ", ex);
		}
		return isEligibleToChange;
	}
	
	
	@Override
	@Transactional
	public List<Object[]> getCurrentStatusActionView(Integer productId,Integer entityTypeId,Integer entityId) {
		List<Object[]> currentStatusActionList=null;
		try{
		String sql="SELECT entityInstanceId,targetStatusId,lastUpdatedDate,(lastUpdatedDate+INTERVAL slaDuration HOUR) AS completeBy,FLOOR(TIME_TO_SEC(TIMEDIFF(lastUpdatedDate+INTERVAL slaDuration HOUR, NOW())) / 3600)AS remaingHours, statusType FROM wf_workflow_status_last_action_view where ";
		if(entityTypeId != null && entityTypeId!=0) {
			sql+=" entityTypeId="+entityTypeId;
		}
		if(productId != null && productId!=0) {
			sql+=" and productId="+productId;
		}
		if(entityId != null && entityId!=0) {
			sql+=" and entityId="+entityId;
		}
		sql+=" order by remaingHours";
		currentStatusActionList=sessionFactory.getCurrentSession().createSQLQuery(sql).list();
		}catch(Exception e) {
			log.error("Error in getCurrentStatusActionView"+e);
		}
		return currentStatusActionList;
	}
	

	@Override
	@Transactional
	public List<Object[]> getCurrentStatusActionViewTestFactoryLevel(Integer testFactoryId,Integer productId,Integer entityTypeId, String entityInstanceId, Integer entityId, HashMap<String, String> statusActionQueryDetails) {
		List<Object[]> currentStatusActionList=null;
		try{
			
			String joinColumn = "";
			if(statusActionQueryDetails != null){
				if(statusActionQueryDetails.containsKey("joinColumn")){
					joinColumn = statusActionQueryDetails.get("joinColumn");
				}
			}
			
			String sql = "(SELECT  MAX(wfevent.workflowEventId)  FROM wf_workflow_events wfevent WHERE ((wfevent.targetStatusId IS NOT NULL) AND (wfevent.entityTypeId = "+entityTypeId+")) GROUP BY wfevent.entityInstanceId,wfevent.entityTypeId)";
			List<Integer> workflowEventIdList = sessionFactory.getCurrentSession().createSQLQuery(sql).list();
			String workflowEventIds = "0";
			if(workflowEventIdList != null && workflowEventIdList.size() > 0){
				for(Integer workflowEventId : workflowEventIdList){
					workflowEventIds += ","+workflowEventId;
				}
			}
			
			sql = "SELECT wfe.entityInstanceId,wfe.targetStatusId, wfe.lastUpdatedDate,(wfe.lastUpdatedDate+wsp.slaDuration * INTERVAL '1HOUR') AS completeBy,"
				+" FLOOR(extract(epoch from((wfe.lastUpdatedDate)  + wsp.slaDuration * interval '1 hour' - NOW()))/3600) as remainingHours, wfs.workflowStatusType"
				+" FROM wf_workflow_events wfe"
				+joinColumn
				+" LEFT JOIN wf_workflows_status_policies wsp ON (wfe.targetStatusId = wsp.workflowStatusId AND wfe.entityInstanceId = wsp.entityInstanceId AND wfe.entityTypeId=wsp.entityTypeId AND wfe.productId=wsp.levelId AND wfe.targetStatusId = wsp.workflowStatusId)"
				+" LEFT JOIN wf_workflows_status wfs ON wfe.targetStatusId = wfs.workflowStatusId"
				+" LEFT JOIN product_master product ON wfe.productId = product.productId"
				+" LEFT JOIN test_factory tf ON product.testFactoryId = tf.testFactoryId"
				+" WHERE (wfe.workflowEventId IN("+workflowEventIds+")) AND wfe.targetStatusId IS NOT NULL";

			if(entityTypeId != null && entityTypeId > 0) {
				sql+=" AND wfe.entityTypeId="+entityTypeId;
			}
			if(testFactoryId != null && testFactoryId > 0) {
				sql+=" AND tf.testFactoryId="+testFactoryId;
			}
			if(productId != null && productId > 0) {
				sql+=" AND wfe.productId="+productId;
			}
			if(entityInstanceId != "" ) {
				sql+=" AND wfe.entityInstanceId in ("+entityInstanceId+") ";
			}
			if(entityId != null && entityId > 0) {
				sql+=" AND wfe.entityId="+entityId;
			}
			sql+=" GROUP BY wfe.entityInstanceId, wfe.targetStatusId,wfe.lastUpdatedDate, wsp.slaDuration, wfs.workflowStatusType";
			
			currentStatusActionList=sessionFactory.getCurrentSession().createSQLQuery(sql).list();
		}catch(Exception e) {
			log.error("Error in getCurrentStatusActionViewTestFactoryLevel "+ e);
		}
		return currentStatusActionList;
	}

	@Override
	@Transactional
	public WorkflowEvent getLastEventOfEntityOrInstanceForStatus(Integer entityTypeId, Integer entityId, Integer entityInstanceId, Integer statusId) {
		WorkflowEvent workflowEvent = null;
		try {
			Criteria c = sessionFactory.getCurrentSession().createCriteria(WorkflowEvent.class, "workflowEvent");
			c.createAlias("workflowEvent.entityType", "entityTypeMaster");
			c.add(Restrictions.eq("entityTypeMaster.entitymasterid", entityTypeId));
			if(entityId != null && entityId != 0) {
				c.add(Restrictions.eq("workflowEvent.entityId", entityId));
			}else{
				c.add(Restrictions.isNull("workflowEvent.entityId"));
			}
			if(entityInstanceId != null && entityInstanceId != 0) {
				c.add(Restrictions.eq("workflowEvent.entityInstanceId", entityInstanceId));
			}else{
				c.add(Restrictions.isNotNull("workflowEvent.entityInstanceId"));
			}
			c.add(Restrictions.eq("workflowEvent.targetStatus.workflowStatusId", statusId));
			c.addOrder(Order.desc("workflowEventId"));
			List<WorkflowEvent> workflowEventList = c.list();
			if(workflowEventList!= null && workflowEventList.size() >0) {
				workflowEvent= workflowEventList.get(0);
			}
		}catch(Exception e) {
			log.error("Error in getLastEventOfEntityOrInstanceForStatus - ", e);
		}
		return workflowEvent;
	}

	@Override
	@Transactional
	public List<WorkflowEvent> getEventsOfEntityOrInstanceForStatus(Integer entityTypeId, Integer entityId, Integer entityInstanceId, Integer statusId) {
		List<WorkflowEvent> workflowEventList = null;
		try {
			Criteria c = sessionFactory.getCurrentSession().createCriteria(WorkflowEvent.class, "workflowEvent");
			c.createAlias("workflowEvent.entityType", "entityTypeMaster");
			c.add(Restrictions.eq("entityTypeMaster.entitymasterid", entityTypeId));
			if(entityId != null && entityId != 0) {
				c.add(Restrictions.eq("workflowEvent.entityId", entityId));
			}else{
				c.add(Restrictions.isNotNull("workflowEvent.entityId"));
			}
			if(entityInstanceId != null && entityInstanceId != 0) {
				c.add(Restrictions.eq("workflowEvent.entityInstanceId", entityInstanceId));
			}else{
				c.add(Restrictions.isNotNull("workflowEvent.entityInstanceId"));
			}
			c.add(Restrictions.eq("workflowEvent.currentStatus.workflowStatusId", statusId));
			c.addOrder(Order.desc("workflowEventId"));
			workflowEventList = c.list();
		}catch(Exception e) {
			log.error("Error in getEventsOfEntityOrInstanceForStatus - ", e);
		}
		return workflowEventList;
	}
	
	@Override
	@Transactional
	public List<WorkflowEvent> listAllWorkflowEvents(Integer startIndex, Integer numberOfRecords, Date startDate, Date endDate) {
		List<WorkflowEvent> workflowEvents = null;
		try{
			Criteria c = sessionFactory.getCurrentSession().createCriteria(WorkflowEvent.class, "workflowEvent");
			if (startDate != null) {
				c.add(Restrictions.ge("workflowEvent.lastUpdatedDate", startDate));
			}
			if (endDate != null) {
				c.add(Restrictions.le("workflowEvent.lastUpdatedDate", endDate));
			}
			if(startIndex != null && numberOfRecords != null){
				c.setFirstResult(startIndex);
				c.setMaxResults(numberOfRecords);
			}
			workflowEvents = c.list();
		}catch(Exception ex){
			log.error("Exception in listAllWorkflowEvents - ", ex);
		}
		return workflowEvents;
	}

	@Override
	@Transactional
	public Integer countAllWorkflowEvents(Date startDate, Date endDate) {
		Integer countOfWorkflowEvents = 0;
		try{
			Criteria c = sessionFactory.getCurrentSession().createCriteria(WorkflowEvent.class, "workflowEvent");
			if (startDate != null) {
				c.add(Restrictions.ge("workflowEvent.lastUpdatedDate", startDate));
			}
			if (endDate != null) {
				c.add(Restrictions.le("workflowEvent.lastUpdatedDate", endDate));
			}
			countOfWorkflowEvents = ((Long)c.setProjection(Projections.rowCount()).uniqueResult()).intValue();
		}catch(Exception ex){
			log.error("Exception in countAllWorkflowEvents - ", ex);
		}
		return countOfWorkflowEvents;
	}

	@Override
	@Transactional
	public boolean isWorkflowStatusInEventAction(Integer workflowStatusId) {
		boolean isWorkflowStatusInEventAction = false;
		try{
			Criteria c = sessionFactory.getCurrentSession().createCriteria(WorkflowEvent.class, "workflowEvent");
			c.add(Restrictions.disjunction().add(Restrictions.or(Restrictions.eq("workflowEvent.currentStatus.workflowStatusId", workflowStatusId ), Restrictions.eq("workflowEvent.targetStatus.workflowStatusId", workflowStatusId))));
			List<WorkflowEvent> workflowEvents = c.list();
			if(workflowEvents != null && workflowEvents.size() > 0){
				isWorkflowStatusInEventAction = true;
			}
		}catch(Exception ex){
			log.error("Error in isWorkflowStatusInEventAction - ", ex);
		}
		return isWorkflowStatusInEventAction;
	}

	@Override
	@Transactional
	public WorkflowEvent getLastEventInstanceByEntityInstanceId(Integer entityTypeId,Integer entityInstanceId) {
		List<WorkflowEvent> workflowEventList = null;
		try {
			Criteria c = sessionFactory.getCurrentSession().createCriteria(WorkflowEvent.class, "workflowEvent");
			c.createAlias("workflowEvent.entityType", "entityTypeMaster");
			c.add(Restrictions.eq("entityTypeMaster.entitymasterid", entityTypeId));
			c.add(Restrictions.eq("workflowEvent.entityInstanceId", entityInstanceId));
			c.addOrder(Order.desc("workflowEventId"));
			workflowEventList = c.list();
			if(workflowEventList != null && workflowEventList.size() >0) {
				return workflowEventList.get(0);
			}
				
		} catch (RuntimeException re) {
			log.error("List getLastEventInstanceByEntityInstanceId failed", re);
		}
		return null;
	}
	
	
}
