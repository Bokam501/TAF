/**
 * 
 */
package com.hcl.ilcm.workflow.dao.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.Subqueries;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.hcl.atf.taf.constants.IDPAConstants;
import com.hcl.ilcm.workflow.dao.WorkflowStatusDAO;
import com.hcl.ilcm.workflow.model.WorkflowStatus;
import com.hcl.ilcm.workflow.model.WorkflowStatusMapping;
import com.hcl.ilcm.workflow.model.WorkflowStatusPolicy;

/**
 * @author silambarasur
 *
 */
@Repository
public class WorkflowStatusDAOImpl implements WorkflowStatusDAO{
	
	private static final Log log = LogFactory.getLog(WorkflowStatusDAOImpl.class);
	
	@Autowired(required=true)
    private SessionFactory sessionFactory;

	
	@Override
	@Transactional
	public List<WorkflowStatus> getWorkFlowStatusList(Integer productId,Integer entityTypeId,Integer entityId,List<String> workFlowStatusRuleList){
		log.info("getWorkFlowStatusList start");
		List<WorkflowStatus> workFlowStatusList=null;
		try{
			Criteria workflowStatusCriteria = sessionFactory.getCurrentSession().createCriteria(WorkflowStatus.class, "wfStatus");
			workflowStatusCriteria.createAlias("wfStatus.entityMaster", "entityType");
			workflowStatusCriteria.createAlias("wfStatus.workflow", "workflow");
			workflowStatusCriteria.add(Restrictions.eq("wfStatus.levelId", productId));
			workflowStatusCriteria.add(Restrictions.eq("entityType.entitymasterid", entityTypeId));
			workflowStatusCriteria.add(Restrictions.eq("wfStatus.entityId", entityId));
			if(workFlowStatusRuleList!=null && workFlowStatusRuleList.size() >0) {
				workflowStatusCriteria.add(Restrictions.in("wfStatus.workflowStatusName", workFlowStatusRuleList));
			}
			workFlowStatusList=workflowStatusCriteria.list();
			log.info("getWorkFlowStatusList End");
			return workFlowStatusList;
		}catch(Exception e){
			log.error("getWorkFlowStatusList Failed", e);
		}
		return null;
	}
	
	@Override
	@Transactional
	public List<WorkflowStatus> getWorkFlowStatusList(Integer workflowId, List<String> workFlowStatusNamesFromBPMN){
		
		log.info("getWorkFlowStatusList start");
		List<WorkflowStatus> workFlowStatusList=null;
		try{
			Criteria workflowStatusCriteria = sessionFactory.getCurrentSession().createCriteria(WorkflowStatus.class, "wfStatus");
			workflowStatusCriteria.createAlias("wfStatus.workflow", "workflow");
			workflowStatusCriteria.add(Restrictions.eq("workflow.workflowId", workflowId));
			if(workFlowStatusNamesFromBPMN != null && workFlowStatusNamesFromBPMN.size() >0) {
				workflowStatusCriteria.add(Restrictions.in("wfStatus.workflowStatusName", workFlowStatusNamesFromBPMN));
			}
			workflowStatusCriteria.addOrder(Order.asc("wfStatus.statusOrder"));
			workFlowStatusList=workflowStatusCriteria.list();

			log.info("getWorkFlowStatusList End");
			return workFlowStatusList;
		}catch(Exception e){
			log.error("getWorkFlowStatusList Failed", e);
		}
		return null;
	}

	
	@Override
	@Transactional
	public WorkflowStatus getWorkflowStatusById(Integer worflowStatusId){
		log.info("getWorkflowStatusById start");
		List<WorkflowStatus> workFlowStatusList=null;
		try{
			
			Criteria workflowStatusCriteria = sessionFactory.getCurrentSession().createCriteria(WorkflowStatus.class, "wfStatus");
			workflowStatusCriteria.add(Restrictions.eq("wfStatus.workflowStatusId", worflowStatusId));
			workFlowStatusList=workflowStatusCriteria.list();
			if(workFlowStatusList !=null && workFlowStatusList.size()>0){
				log.info("getWorkflowStatusById End");
				return workFlowStatusList.get(0);
			}
			
		}catch(Exception e){
			log.error("getWorkflowStatusById failed"+e);
		}
		return null;
	}
	
	@Override
	@Transactional
	public WorkflowStatus getStatusNameByEntityTypeId(String resultPrimaryStatus,Integer entityTypeId){
		log.info("getStatusNameByEntityTypeId start");
		List<WorkflowStatus> workFlowStatusList=null;
		try{
			
			Criteria workflowStatusCriteria = sessionFactory.getCurrentSession().createCriteria(WorkflowStatus.class, "wfStatus");
			workflowStatusCriteria.createAlias("wfStatus.entityMaster", "entityType");
			workflowStatusCriteria.add(Restrictions.eq("entityType.entitymasterid", entityTypeId));
			workflowStatusCriteria.add(Restrictions.eq("wfStatus.workflowStatusName", resultPrimaryStatus));
			workFlowStatusList=workflowStatusCriteria.list();
			if(workFlowStatusList !=null && workFlowStatusList.size()>0){
				log.info("getStatusNameByEntityTypeId End");
				return workFlowStatusList.get(0);
			}
			
		}catch(Exception e){
			log.error("getStatusNameByEntityTypeId failed"+e);
		}
		return null;
	}
	
	@Override
	@Transactional
	public List<WorkflowStatus> getSecondaryStatusByParentId(Integer productId,Integer entityTypeId,Integer wfStatusParentId) {
		log.info("getSecondaryStatusByParentId Start");
		List<WorkflowStatus> workFlowStatusList=null;
		try{
			
			Criteria workflowStatusCriteria = sessionFactory.getCurrentSession().createCriteria(WorkflowStatus.class, "wfStatus");
			workflowStatusCriteria.createAlias("wfStatus.entityMaster", "entityType");
			workflowStatusCriteria.add(Restrictions.eq("entityType.entitymasterid", entityTypeId));
			workflowStatusCriteria.add(Restrictions.eq("wfStatus.levelId", productId));
			workflowStatusCriteria.add(Restrictions.eq("wfStatus.parentStatus.workflowStatusId", wfStatusParentId));
			workFlowStatusList=workflowStatusCriteria.list();
			log.info("getSecondaryStatusByParentId End");
			return workFlowStatusList;
			
		}catch(Exception e){
			log.error("getSecondaryStatusByParentId failed"+e);
		}
		return workFlowStatusList;
		
	}
	
	@Override
	@Transactional
	public boolean isExistWorkflowStausByWorkflowIdAndWorkflowStatusName(Integer workflowId,String workflowStatusName) {
		List<WorkflowStatus> workFlowStatusList=null;
		try{
			
			Criteria workflowStatusCriteria = sessionFactory.getCurrentSession().createCriteria(WorkflowStatus.class, "wfStatus");
			workflowStatusCriteria.createAlias("wfStatus.workflow", "workflow");
			workflowStatusCriteria.add(Restrictions.eq("workflow.workflowId", workflowId));
			workflowStatusCriteria.add(Restrictions.eq("wfStatus.workflowStatusName", workflowStatusName));
			workFlowStatusList=workflowStatusCriteria.list();
			if(workFlowStatusList!=null && workFlowStatusList.size() >0) {
				return true;
			}
		}catch(Exception e){
			log.error("isExistWorkflowStaus failed"+e);
		}
		return false;
	}
	
	@Override
	@Transactional
	public boolean isExistWorkflowStausType(Integer workflowId,String workflowStatusType) {
		List<WorkflowStatus> workFlowStatusList=null;
		try{
			
			Criteria workflowStatusCriteria = sessionFactory.getCurrentSession().createCriteria(WorkflowStatus.class, "wfStatus");
			workflowStatusCriteria.createAlias("wfStatus.workflow", "workflow");
			workflowStatusCriteria.add(Restrictions.eq("workflow.workflowId", workflowId));
			workflowStatusCriteria.add(Restrictions.eq("wfStatus.workflowStatusType", workflowStatusType));
			workFlowStatusList=workflowStatusCriteria.list();
			if(workFlowStatusList!=null && workFlowStatusList.size() >0) {
				return true;
			}
		}catch(Exception e){
			log.error("isExistWorkflowStaus failed"+e);
		}
		return false;
	}

	@Override
	@Transactional
	public WorkflowStatus getInitialStatusForInstance(Integer productId, Integer entityTypeId, Integer entityId, Integer workflowId) {
		log.info("Inside getInitialStatusForInstance with parameters productId - "+productId+", entityTypeId - "+entityTypeId+", entityId - "+entityId+", workflowId - "+workflowId);
		WorkflowStatus workflowStatus = null;
		try{
			List<Integer> workflowStatusIds = new ArrayList<Integer>();
			Criteria workflowStatusIdsCriteria = sessionFactory.getCurrentSession().createCriteria(WorkflowStatusPolicy.class, "wfStausPolicy");
			workflowStatusIdsCriteria.add(Restrictions.eq("wfStausPolicy.entityType.entitymasterid", entityTypeId));
			if(entityId != null && entityId >0){
				workflowStatusIdsCriteria.add(Restrictions.eq("wfStausPolicy.entityId", entityId));
			}
			workflowStatusIdsCriteria.add(Restrictions.isNull("wfStausPolicy.entityInstanceId"));
			workflowStatusIdsCriteria.setProjection(Projections.distinct(Projections.property("wfStausPolicy.workflowStatus.workflowStatusId")));
			workflowStatusIds = workflowStatusIdsCriteria.list();

			log.info("Workflow status ids - "+workflowStatusIds);
			
			Criteria workflowStatusCriteria = sessionFactory.getCurrentSession().createCriteria(WorkflowStatus.class, "wfStatus");
			workflowStatusCriteria.add(Restrictions.in("wfStatus.workflowStatusId", workflowStatusIds));
			if(workflowId != null && workflowId > 0) {
				workflowStatusCriteria.add(Restrictions.eq("wfStatus.workflow.workflowId", workflowId));
			}
			workflowStatusCriteria.addOrder(Order.asc("wfStatus.statusOrder"));
			List<WorkflowStatus> workFlowStatusList = workflowStatusCriteria.list();
			if(workFlowStatusList != null && workFlowStatusList.size() > 0){
				workflowStatus = workFlowStatusList.get(0);
			}
		}catch(Exception ex){
			log.error("Error while getting initial status for instance ", ex);
		}
		return workflowStatus;
	}

	@Override
	@Transactional
	public Boolean isWorkflowContainsMandatoryStatusTypeStatus(Integer workflowId) {
		log.info("Inside isWorkflowContainsMandatoryStatusTypeStatus with parameters workflowId - "+workflowId);
		Boolean isAllMandatoryStatusTypesAvailable = false;
		try{
			Criteria workflowStatusCriteria = sessionFactory.getCurrentSession().createCriteria(WorkflowStatus.class, "wfStatus");
			if(workflowId != null && workflowId > 0) {
				workflowStatusCriteria.add(Restrictions.eq("wfStatus.workflow.workflowId", workflowId));
			}
			
			workflowStatusCriteria.setProjection(Projections.distinct(Projections.property("wfStatus.workflowStatusType")));
			List statusTypesList = workflowStatusCriteria.list();
			
			if(statusTypesList != null && statusTypesList.contains(IDPAConstants.WORKFLOW_STATUS_TYPE_BEGIN) && statusTypesList.contains(IDPAConstants.WORKFLOW_STATUS_TYPE_INTERMEDIATE) && statusTypesList.contains(IDPAConstants.WORKFLOW_STATUS_TYPE_END)){
				isAllMandatoryStatusTypesAvailable = true;
			}
		}catch(Exception ex){
			log.error("Error in isWorkflowContainsMandatoryStatusTypeStatus - ", ex);
		}
		return isAllMandatoryStatusTypesAvailable;
	}

	@Override
	@Transactional
	public Boolean isStatusAvailableForStatusType(Integer workflowId, String updatedStatusType, Integer exceptStatusId) {
		log.info("Inside isStatusAvailableForStatusType with parameters workflowId - "+workflowId+", updatedStatusType - "+updatedStatusType+", exceptStatusId - "+exceptStatusId);
		Boolean isStatusAvailable = false;
		try{
			Criteria workflowStatusCriteria = sessionFactory.getCurrentSession().createCriteria(WorkflowStatus.class, "wfStatus");
			if(workflowId != null && workflowId > 0) {
				workflowStatusCriteria.add(Restrictions.eq("wfStatus.workflow.workflowId", workflowId));
			}
			if(exceptStatusId != null){
				workflowStatusCriteria.add(Restrictions.eq("wfStatus.workflowStatusId", exceptStatusId));
			}
			List<WorkflowStatus> workflowStatusList = workflowStatusCriteria.list();
			if(workflowStatusList != null && workflowStatusList.size() > 0){
				String oldStatusType = workflowStatusList.get(0).getWorkflowStatusType();
				
				if(oldStatusType == null || oldStatusType.equalsIgnoreCase(IDPAConstants.WORKFLOW_STATUS_TYPE_ABORT) || (oldStatusType != null && oldStatusType.equalsIgnoreCase(updatedStatusType))){
					isStatusAvailable = true;
					return isStatusAvailable;
				}else{
					updatedStatusType = oldStatusType;
				}
			}
			
			workflowStatusCriteria = sessionFactory.getCurrentSession().createCriteria(WorkflowStatus.class, "wfStatus");
			if(workflowId != null && workflowId > 0) {
				workflowStatusCriteria.add(Restrictions.eq("wfStatus.workflow.workflowId", workflowId));
			}
			if(updatedStatusType != null && !updatedStatusType.trim().isEmpty()){
				workflowStatusCriteria.add(Restrictions.eq("wfStatus.workflowStatusType", updatedStatusType));
			}
			if(exceptStatusId != null){
				workflowStatusCriteria.add(Restrictions.ne("wfStatus.workflowStatusId", exceptStatusId));
			}
			workflowStatusList = workflowStatusCriteria.list();
			if(workflowStatusList != null && workflowStatusList.size() > 0){
				isStatusAvailable = true;
			}
		}catch(Exception ex){
			log.error("Error in isStatusAvailableForStatusType - ", ex);
		}
		return isStatusAvailable;
	}

	@Override
	@Transactional
	public List<String> getWorflowStatusTypes(Class className, String aliasName, HashMap<String, Object> constraints, String projection) {
		log.info("Inside getWorflowStatusTypes with parameters Class - "+className+", aliasName - "+aliasName+", constraints - "+constraints+", projection - "+projection);
		List<String> statusTypesList = null;
		try{
			Criteria workflowStatusCriteria = sessionFactory.getCurrentSession().createCriteria(WorkflowStatus.class, "wfStatus");
			
			DetachedCriteria detachedCriteria = DetachedCriteria.forClass(className, aliasName);
			for(Entry<String, Object>  entry : constraints.entrySet()){
				detachedCriteria.add(Restrictions.eq(entry.getKey(), entry.getValue()));
			}
			detachedCriteria.setProjection(Property.forName(projection));
			
			workflowStatusCriteria.add(Subqueries.propertyIn("wfStatus.workflowStatusId", detachedCriteria));
			
			workflowStatusCriteria.setProjection(Projections.distinct(Projections.property("wfStatus.workflowStatusType")));
			statusTypesList = workflowStatusCriteria.list();
		}catch(Exception ex){
			log.error("Error in getWorflowStatusTypes - ", ex);
		}
		return statusTypesList;
	}

	@Override
	@Transactional
	public WorkflowStatus getFirstWorkflowStatusOfStatusType(Integer workflowId, String statusType, String orderBy) {
		log.info("Inside getFirstWorkflowStatusOfStatusType with parameters workflowId - "+workflowId+", statusType - "+statusType);
		WorkflowStatus workflowStatus = null;
		try{
			Criteria workflowStatusCriteria = sessionFactory.getCurrentSession().createCriteria(WorkflowStatus.class, "wfStatus");
			workflowStatusCriteria.add(Restrictions.eq("wfStatus.workflow.workflowId", workflowId));
			workflowStatusCriteria.add(Restrictions.eq("wfStatus.workflowStatusType", statusType));
			if("desc".equalsIgnoreCase(orderBy)){
				workflowStatusCriteria.addOrder(Order.desc("wfStatus.statusOrder"));
			}else{
				workflowStatusCriteria.addOrder(Order.asc("wfStatus.statusOrder"));
			}
			List<WorkflowStatus> workflowStatuses = workflowStatusCriteria.list();
			if(workflowStatuses != null && workflowStatuses.size() > 0){
				workflowStatus = workflowStatuses.get(0);
			}
		}catch(Exception ex){
			log.error("Error in getWorflowStatusTypes - ", ex);
		}
		return workflowStatus;
	}

	@Override
	@Transactional
	public List<Object[]> getWorkflowStatusAvailableForMapping(Integer workflowId, Integer sourceStatusId, Integer jtStartIndex, Integer jtPageSize) {
		log.info("Inside getWorkflowStatusAvailableForMapping with parameters workflowId - "+workflowId+", sourceStatusId - "+sourceStatusId+", jtStartIndex - "+jtStartIndex+", jtPageSize - "+jtPageSize);
		List<Object[]> availableStatuses = null;
		try {
			Criteria criteria = sessionFactory.getCurrentSession().createCriteria(WorkflowStatus.class, "workflowStatus")
			.add(Restrictions.eq("workflowStatus.workflow.workflowId", workflowId))
			.add(Restrictions.ne("workflowStatus.workflowStatusId", sourceStatusId))
		    .add(Subqueries.propertyNotIn("workflowStatus.workflowStatusId", DetachedCriteria.forClass(WorkflowStatusMapping.class, "workflowStatusMapping")
		    		.add(Restrictions.eq("workflowStatusMapping.workflow.workflowId", workflowId))
		    		.add(Restrictions.eq("workflowStatusMapping.workflowSourceStatusId.workflowStatusId", sourceStatusId))
		            .setProjection(Projections.property("workflowStatusMapping.workflowTargetStatusId.workflowStatusId"))		
		    ))
		    .setProjection(Projections.projectionList()
		    	.add(Projections.property("workflowStatus.workflowStatusId"))
		    	.add(Projections.property("workflowStatus.workflowStatusDisplayName"))
		    	.add(Projections.property("workflowStatus.statusOrder"))
		    );
			
			if(jtStartIndex != null && jtPageSize != null){
				criteria.setFirstResult(jtStartIndex).setMaxResults(jtPageSize);
			}
			
			availableStatuses = criteria.list();
		} catch (Exception ex) {
			log.error("Error in getWorkflowStatusAvailableForMapping - ", ex);	
		}
		return availableStatuses;
	}


	@Override
	@Transactional
	public Integer getWorkflowStatusAvailableForMappingCount(Integer workflowId, Integer sourceStatusId) {
		log.info("Inside getWorkflowStatusAvailableForMappingCount with parameters workflowId - "+workflowId+", sourceStatusId - "+sourceStatusId);
		Integer availableStatusCount = null;
		try {
			Criteria criteria = sessionFactory.getCurrentSession().createCriteria(WorkflowStatus.class, "workflowStatus")
			.add(Restrictions.eq("workflowStatus.workflow.workflowId", workflowId))
			.add(Restrictions.ne("workflowStatus.workflowStatusId", sourceStatusId))
		    .add(Subqueries.propertyNotIn("workflowStatus.workflowStatusId", DetachedCriteria.forClass(WorkflowStatusMapping.class, "workflowStatusMapping")
		    		.add(Restrictions.eq("workflowStatusMapping.workflow.workflowId", workflowId))
		    		.add(Restrictions.eq("workflowStatusMapping.workflowSourceStatusId.workflowStatusId", sourceStatusId))
		            .setProjection(Projections.property("workflowStatusMapping.workflowTargetStatusId.workflowStatusId"))		
		    ))
		    .setProjection(Projections.rowCount());
			availableStatusCount = ((Long) criteria.uniqueResult()).intValue(); 
			
		} catch (Exception ex) {
			log.error("Error in getWorkflowStatusAvailableForMapping - ", ex);	
		}
		return availableStatusCount;
	}
	
	@Override
	@Transactional
	public List<Object[]> getWorkflowStatusAlreadyMapped(Integer workflowId, Integer sourceStatusId, Integer jtStartIndex, Integer jtPageSize) {
		log.info("Inside getWorkflowStatusAlreadyMapped with parameters workflowId - "+workflowId+", sourceStatusId - "+sourceStatusId+", jtStartIndex - "+jtStartIndex+", jtPageSize - "+jtPageSize);
		List<Object[]> mappedStatuses = null;
		try {
			Criteria criteria = sessionFactory.getCurrentSession().createCriteria(WorkflowStatus.class, "workflowStatus")
			.add(Restrictions.eq("workflowStatus.workflow.workflowId", workflowId))
			.add(Restrictions.ne("workflowStatus.workflowStatusId", sourceStatusId))
		    .add(Subqueries.propertyIn("workflowStatus.workflowStatusId", DetachedCriteria.forClass(WorkflowStatusMapping.class, "workflowStatusMapping")
		    		.add(Restrictions.eq("workflowStatusMapping.workflow.workflowId", workflowId))
		    		.add(Restrictions.eq("workflowStatusMapping.workflowSourceStatusId.workflowStatusId", sourceStatusId))
		            .setProjection(Projections.property("workflowStatusMapping.workflowTargetStatusId.workflowStatusId"))		
		    ))
		    .setProjection(Projections.projectionList()
		    	.add(Projections.property("workflowStatus.workflowStatusId"))
		    	.add(Projections.property("workflowStatus.workflowStatusDisplayName"))
		    	.add(Projections.property("workflowStatus.statusOrder"))
		    );
			
			if(jtStartIndex != null && jtPageSize != null){
				criteria.setFirstResult(jtStartIndex).setMaxResults(jtPageSize);
			}
			
			mappedStatuses = criteria.list();
		} catch (Exception ex) {
			log.error("Error in getWorkflowStatusAlreadyMapped - ", ex);	
		}
		return mappedStatuses;
	}

	@Override
	@Transactional
	public boolean isWorkflowStatusAlreadyMapped(Integer workflowId, Integer sourceStatusId, Integer targetStatusId) {
		log.info("Inside getWorkflowStatusAlreadyMapped with parameters workflowId - "+workflowId+", sourceStatusId - "+sourceStatusId+", targetStatusId - "+targetStatusId);
		boolean isAlreadyMapped = false;
		try{
			Criteria criteria = sessionFactory.getCurrentSession().createCriteria(WorkflowStatusMapping.class, "workflowStatusMapping")
			.add(Restrictions.eq("workflowStatusMapping.workflowSourceStatusId.workflowStatusId", sourceStatusId))
			.add(Restrictions.eq("workflowStatusMapping.workflowTargetStatusId.workflowStatusId", targetStatusId))
			.add(Restrictions.eq("workflowStatusMapping.workflow.workflowId", workflowId));
			
			List<WorkflowStatusMapping> workflowStatusMappings = criteria.list();
			
			if(workflowStatusMappings != null && workflowStatusMappings.size() > 0){
				isAlreadyMapped = true;
			}
			
		}catch(Exception ex){
			log.error("Error in isWorkflowStatusAlreadyMapped - ", ex);
		}
		return isAlreadyMapped;
	}

	@Override
	@Transactional
	public void workflowStatusMappingOrUnmapping(WorkflowStatusMapping workflowStatusMapping, String maporunmap) {
		try{
			if(maporunmap == null || maporunmap.isEmpty() || maporunmap.equalsIgnoreCase("map")){
				sessionFactory.getCurrentSession().save(workflowStatusMapping);
			}else{
				workflowStatusMapping = (WorkflowStatusMapping) sessionFactory.getCurrentSession().createCriteria(WorkflowStatusMapping.class, "workflowStatusMapping")
						.add(Restrictions.eq("workflowStatusMapping.workflowSourceStatusId.workflowStatusId", workflowStatusMapping.getWorkflowSourceStatusId().getWorkflowStatusId()))
						.add(Restrictions.eq("workflowStatusMapping.workflowTargetStatusId.workflowStatusId", workflowStatusMapping.getWorkflowTargetStatusId().getWorkflowStatusId()))
						.add(Restrictions.eq("workflowStatusMapping.workflow.workflowId", workflowStatusMapping.getWorkflow().getWorkflowId()))
	                    .uniqueResult();
				sessionFactory.getCurrentSession().delete(workflowStatusMapping);
			}
			
			log.info(maporunmap+" successful");
		}catch(Exception ex){
			log.error("Error in workflowStatusMappingOrUnmapping - ", ex);
		}
	}

	@Override
	@Transactional
	public List<WorkflowStatus> getPossibleWorkflowStatusForAction(Integer currentStatusId) {
		List<WorkflowStatus> workflowStatus = null;
		try{
			Criteria criteria = sessionFactory.getCurrentSession().createCriteria(WorkflowStatus.class, "workflowStatus")
			.add(Restrictions.disjunction().add(Restrictions.or(Subqueries.propertyIn("workflowStatus.workflowStatusId", DetachedCriteria.forClass(WorkflowStatusMapping.class, "workflowStatusMapping")
		    	.add(Restrictions.eq("workflowStatusMapping.workflowSourceStatusId.workflowStatusId", currentStatusId))
		        .setProjection(Projections.property("workflowStatusMapping.workflowTargetStatusId.workflowStatusId"))		
				), Restrictions.eq("workflowStatus.workflowStatusId", currentStatusId))));
			
			workflowStatus = criteria.list();
		}catch(Exception ex){
			log.error("Error in getPossibleWorkflowStatusForAction - ", ex);
		}
		return workflowStatus;
	}
	
	@Override
	@Transactional
	public WorkflowStatus getWorkflowStatusByName(String worflowStatusName) {
		List<WorkflowStatus> workFlowStatusList=null;
		try {
			Criteria workflowStatusCriteria = sessionFactory.getCurrentSession().createCriteria(WorkflowStatus.class, "wfStatus");
			workflowStatusCriteria.add(Restrictions.eq("wfStatus.workflowStatusName", worflowStatusName));
			workFlowStatusList=workflowStatusCriteria.list();
			if(workFlowStatusList !=null && workFlowStatusList.size()>0){
				log.info("getWorkflowStatusByName End");
				return workFlowStatusList.get(0);
			}
		}catch(RuntimeException re) {
			log.error("Error in DAO getWorkflowStatusByName",re);
		}
		return null;
	}
	
	@Override
	@Transactional
	public WorkflowStatus getWorkflowStausByWorkflowIdAndWorkflowStatusName(Integer workflowId,String workflowStatusName) {
		List<WorkflowStatus> workFlowStatusList=null;
		try{
			
			Criteria workflowStatusCriteria = sessionFactory.getCurrentSession().createCriteria(WorkflowStatus.class, "wfStatus");
			workflowStatusCriteria.createAlias("wfStatus.workflow", "workflow");
			workflowStatusCriteria.add(Restrictions.eq("workflow.workflowId", workflowId));
			workflowStatusCriteria.add(Restrictions.ilike("wfStatus.workflowStatusName", workflowStatusName));
			workFlowStatusList=workflowStatusCriteria.list();
			if(workFlowStatusList!=null && workFlowStatusList.size() >0) {
				return workFlowStatusList.get(0);
			}
		}catch(Exception e){
			log.error("isExistWorkflowStaus failed"+e);
		}
		return null;
	}
	
}
