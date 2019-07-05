package com.hcl.ilcm.workflow.dao.impl;

import java.util.ArrayList;
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
import com.hcl.ilcm.workflow.dao.WorkflowMasterDAO;
import com.hcl.ilcm.workflow.model.WorkflowMaster;
import com.hcl.ilcm.workflow.model.WorkflowMasterEntityMapping;

@Repository
public class WorkflowMasterDAOImpl implements WorkflowMasterDAO {

	private static final Log log = LogFactory.getLog(WorkflowMasterDAOImpl.class);
	
	@Autowired(required=true)
    private SessionFactory sessionFactory;
	
	@Override
	@Transactional
	public List<WorkflowMaster> getWorkflowMasterList() {
		try {
		 Criteria workflowMasterCriteria = sessionFactory.getCurrentSession().createCriteria(WorkflowMaster.class, "wfMaster");
		 return workflowMasterCriteria.list();
		} catch(Exception e) {
			log.error("Error in getWorkflowMasterList "+e);
		}
		return null;
	}
	
	@Override
	@Transactional
	public List<WorkflowMaster> getWorkflowMasterListByType(String workflowType) {
		log.debug("get WorkflowMasterList By Type");
		try {
		 Criteria workflowMasterCriteria = sessionFactory.getCurrentSession().createCriteria(WorkflowMaster.class, "wfMaster");
		 if(workflowType != null && !workflowType.trim().isEmpty()){
			 workflowMasterCriteria.add(Restrictions.eq("wfMaster.workflowType", workflowType));
		 }
		 return workflowMasterCriteria.list();
		} catch(Exception e) {
			log.error("Error in getWorkflowMasterListByType "+e);
		}
		return null;
	}
	
	@Override
	@Transactional
	public void addWorkflowMaster(WorkflowMaster workflowMaster) {
		try{
			sessionFactory.getCurrentSession().save(workflowMaster);
		}catch(Exception e) {
			log.info("Error in addWorkflowMaster"+e);
		}
		
	}
	
	@Override
	@Transactional
	public void updateWorkflowMaster(WorkflowMaster workflowMaster) {
		try{
			sessionFactory.getCurrentSession().saveOrUpdate(workflowMaster);
		}catch(Exception e) {
			log.info("Error in updateWorkflowMaster"+e);
		}
	}
	
	@Override
	@Transactional
	public WorkflowMaster getWorkflowForEntityTypeOrInstance(Integer productId, Integer entityTypeId, Integer entityId, Integer entityInstanceId, Integer activeState) {
		log.info("Fetching getWorkflowForEntityTypeOrInstance with parameters productId - "+productId);
		log.debug("Fetching getWorkflowForEntityTypeOrInstance with parameters productId - "+productId+", entityTypeId - "+entityTypeId+", entityId - "+entityId+", entityInstanceId - "+entityInstanceId+" activeState - "+activeState);
		try{
			
			List<WorkflowMaster> workflowList=null;
			List<WorkflowMasterEntityMapping> workflowMasterEntityMappings = null; 
			
			Criteria workflowMasterCriteria = sessionFactory.getCurrentSession().createCriteria(WorkflowMasterEntityMapping.class, "wfEntityMapping");
			if(productId != null && productId > 0){
				workflowMasterCriteria.createAlias("wfEntityMapping.product", "product");
				workflowMasterCriteria.add(Restrictions.eq("product.productId", productId));				
			}
			workflowMasterCriteria.createAlias("wfEntityMapping.entityType", "entityType");
			workflowMasterCriteria.add(Restrictions.eq("entityType.entitymasterid", entityTypeId));
			if(entityId !=null && entityId > 0) {
				workflowMasterCriteria.add(Restrictions.eq("wfEntityMapping.entityId", entityId));
			}/*else{
				workflowMasterCriteria.add(Restrictions.isNull("wfEntityMapping.entityId"));
			}*/
			if(entityInstanceId !=null && entityInstanceId > 0) {
				workflowMasterCriteria.add(Restrictions.eq("wfEntityMapping.entityInstanceId", entityInstanceId));
			}else{
				workflowMasterCriteria.add(Restrictions.isNull("wfEntityMapping.entityInstanceId"));
			}
			workflowMasterCriteria.add(Restrictions.eq("wfEntityMapping.isDefault", 1));
			
			workflowMasterEntityMappings = workflowMasterCriteria.list();
			
			if(workflowMasterEntityMappings!=null && workflowMasterEntityMappings.size()>0){
				workflowMasterCriteria = sessionFactory.getCurrentSession().createCriteria(WorkflowMaster.class, "wfMaster");
				Integer workflowId=(Integer)workflowMasterEntityMappings.get(0).getWorkflow().getWorkflowId();
				workflowMasterCriteria.add(Restrictions.eq("wfMaster.workflowId",workflowId));
				if(activeState != null && activeState != 2){
					workflowMasterCriteria.add(Restrictions.eq("wfMaster.activeState",activeState));
				}
				workflowList= workflowMasterCriteria.list();
				if(workflowList !=null && workflowList.size() >0) {
					return workflowList.get(0);
				}
			}
		}catch(Exception ex){
			log.error("Error inside getWorkflowForEntityTypeOrInstance - ", ex);
		}
		return null;
	}

	@Override
	@Transactional
	public List<WorkflowMasterEntityMapping> getWorkflowEntitiesForProduct(int productId) {
		log.debug("get WorkflowEntities For Product with parameters productId - "+productId);
		List<WorkflowMasterEntityMapping> workflowMasterEntityMappings = null; 
		try{
			Criteria workflowMasterCriteria = sessionFactory.getCurrentSession().createCriteria(WorkflowMasterEntityMapping.class, "wfEntityMapping");
			workflowMasterCriteria.createAlias("wfEntityMapping.product", "product");
			workflowMasterCriteria.add(Restrictions.eq("product.productId", productId));
			workflowMasterCriteria.add(Restrictions.isNull("wfEntityMapping.entityInstanceId"));
			
			workflowMasterEntityMappings = workflowMasterCriteria.list();
			
		}catch(Exception ex){
			log.error("Error inside getWorkflowMasterDetails - ", ex);
		}
		return workflowMasterEntityMappings;
	}

	@Override
	@Transactional
	public void addWorkflowProductEntityMapping(WorkflowMasterEntityMapping workflowMasterEntityMapping) {
		try{
			sessionFactory.getCurrentSession().saveOrUpdate(workflowMasterEntityMapping);
		}catch(Exception e) {
			log.error("Error in addWorkflowProductEntityMapping ", e);
		}
	}

	@Override
	@Transactional
	public boolean checkWorkflowEntityMappingAlreadyExist(Integer workflowEntityMappingId, Integer productId, Integer workflowId, Integer entityTypeId, Integer entityId) {
		boolean isMappingAlreadyExist = false;
		log.info("Inside checkWorkflowEntityMappingAlreadyExist with parameters workflowEntityMappingId - "+workflowEntityMappingId+", productId - "+productId+", workflowId - "+workflowId+", entityTypeId - "+entityTypeId+", entityId - "+entityId);
		List<WorkflowMasterEntityMapping> workflowMasterEntityMappings = null; 
		try{

			Criteria workflowMasterCriteria = sessionFactory.getCurrentSession().createCriteria(WorkflowMasterEntityMapping.class, "wfEntityMapping");
			if(productId != null && productId > 0){
				workflowMasterCriteria.createAlias("wfEntityMapping.product", "product");
				workflowMasterCriteria.add(Restrictions.eq("product.productId", productId));				
			}
			workflowMasterCriteria.createAlias("wfEntityMapping.workflow", "workflow");
			workflowMasterCriteria.add(Restrictions.eq("workflow.workflowId", workflowId));
			workflowMasterCriteria.createAlias("wfEntityMapping.entityType", "entityType");
			workflowMasterCriteria.add(Restrictions.eq("entityType.entitymasterid", entityTypeId));
			if(entityId != null){
				workflowMasterCriteria.add(Restrictions.eq("wfEntityMapping.entityId", entityId));
			}
			if(workflowEntityMappingId != null){
				workflowMasterCriteria.add(Restrictions.ne("wfEntityMapping.workflowEntityMappingId", workflowEntityMappingId));
			}
			workflowMasterCriteria.add(Restrictions.isNull("wfEntityMapping.entityInstanceId"));
			
			workflowMasterEntityMappings = workflowMasterCriteria.list();
			if(workflowMasterEntityMappings != null && workflowMasterEntityMappings.size() > 0){
				isMappingAlreadyExist = true;
			}
			
		}catch(Exception ex){
			log.error("Error inside checkWorkflowEntityMappingAlreadyExist - ", ex);
		}
		return isMappingAlreadyExist;
	}

	@Override
	@Transactional
	public WorkflowMasterEntityMapping getWorkflowOfEntityInstance(Integer productId, Integer workflowId, Integer entityTypeId, Integer entityId, Integer entityInstanceId) {
		log.info("Inside getWorkflowOfEntityInstance with parameters productId - "+productId+", workflowId - "+workflowId+", entityTypeId - "+entityTypeId+", entityId - "+entityId+", entityInstanceId - "+entityInstanceId);
		WorkflowMasterEntityMapping workflowMasterEntityMapping = null; 
		try{

			Criteria workflowMasterCriteria = sessionFactory.getCurrentSession().createCriteria(WorkflowMasterEntityMapping.class, "wfEntityMapping");
			if(productId != null && productId > 0){
				workflowMasterCriteria.createAlias("wfEntityMapping.product", "product");
				workflowMasterCriteria.add(Restrictions.eq("product.productId", productId));
			}
			workflowMasterCriteria.createAlias("wfEntityMapping.workflow", "workflow");
			workflowMasterCriteria.add(Restrictions.eq("workflow.workflowId", workflowId));
			workflowMasterCriteria.createAlias("wfEntityMapping.entityType", "entityType");
			workflowMasterCriteria.add(Restrictions.eq("entityType.entitymasterid", entityTypeId));
			if(entityId != null){
				workflowMasterCriteria.add(Restrictions.eq("wfEntityMapping.entityId", entityId));
			}else{
				workflowMasterCriteria.add(Restrictions.isNull("wfEntityMapping.entityId"));
			}
			if(entityInstanceId != null){
				workflowMasterCriteria.add(Restrictions.eq("wfEntityMapping.entityInstanceId", entityInstanceId));
			}else{
				workflowMasterCriteria.add(Restrictions.isNull("wfEntityMapping.entityInstanceId"));
			}
			
			List<WorkflowMasterEntityMapping> workflowMasterEntityMappings = workflowMasterCriteria.list();
			if(workflowMasterEntityMappings != null && workflowMasterEntityMappings.size() > 0){
				workflowMasterEntityMapping = workflowMasterEntityMappings.get(0);
			}
			
		}catch(Exception ex){
			log.error("Error inside getWorkflowMasterDetails - ", ex);
		}
		return workflowMasterEntityMapping;

	}

	@Override
	@Transactional
	public boolean checkEligibilityForDefault(Integer workflowEntityMappingId, Integer productId, Integer entityTypeId, Integer entityId) {
		boolean isEligibleForDefault = true;
		log.info("Inside checkEligibilityForDefault with parameters workflowEntityMappingId - "+workflowEntityMappingId+", productId - "+productId+", entityTypeId - "+entityTypeId+", entityId - "+entityId);
		try{
			Criteria criteria = sessionFactory.getCurrentSession().createCriteria(WorkflowMasterEntityMapping.class, "wfEntityMapping");
			if(productId != null && productId > 0){
				criteria.createAlias("wfEntityMapping.product", "product");
				criteria.add(Restrictions.eq("product.productId", productId));				
			}
			criteria.createAlias("wfEntityMapping.entityType", "entityType");
			criteria.add(Restrictions.eq("entityType.entitymasterid", entityTypeId));
			if(entityId != null){
				criteria.add(Restrictions.eq("wfEntityMapping.entityId", entityId));
			}else{
				criteria.add(Restrictions.isNull("wfEntityMapping.entityId"));
			}
			if(workflowEntityMappingId != null){
				criteria.add(Restrictions.ne("wfEntityMapping.workflowEntityMappingId", workflowEntityMappingId));
			}
			criteria.add(Restrictions.isNull("wfEntityMapping.entityInstanceId"));
			criteria.add(Restrictions.eq("wfEntityMapping.isDefault", 1));
			Integer userCount = ((Long)criteria.setProjection(Projections.rowCount()).uniqueResult()).intValue();
			if(userCount != null && userCount > 0){
				isEligibleForDefault = false;
			}
		}catch(Exception ex){
			log.error("Error inside checkEligibilityForDefault - ", ex);
		}
		return isEligibleForDefault;
	}

	@Override
	@Transactional
	public void updateIsDefaultOfEntityMapping(Integer workflowEntityMappingId, Integer productId, Integer entityTypeId, Integer entityId) {
		log.info("Inside updateIsDefaultOfEntityMapping with parameters workflowEntityMappingId - "+workflowEntityMappingId+", productId - "+productId+", entityTypeId - "+entityTypeId+", entityId - "+entityId);
		try{
			String updateIsDefaultQuery = "update wf_workflows_entity_mapping set isDefault = 0 where productId = "+productId+" and entityTypeId = "+entityTypeId;
			if(entityId != null){
				updateIsDefaultQuery += " and entityId = "+entityId;
			}
			
			int recordUpdatedCount = sessionFactory.getCurrentSession().createSQLQuery(updateIsDefaultQuery).executeUpdate();
			log.info("Number of records updated = "+recordUpdatedCount);
		}catch(Exception ex){
			log.error("Error inside updateIsDefaultOfEntityMapping - ", ex);
		}
	}

	@Override
	@Transactional
	public List<Object[]> getWorkflowMyActionCounts(Integer productId, Integer userId, Integer userRoleId,Integer entityTypeId) {
		log.debug("Inside getWorkflowMyActionCounts with parameters : productId - "+productId+", userId - "+userId+", roleId - "+userRoleId);
		log.info("Getting WorkflowMyActionCounts with parameters : productId - "+productId);
		List<Object[]> myPendingActionCounts = new ArrayList<Object[]>();
		try {
			
			String entityTypeMapping="";
			String workflowStatusCheckingCondition="";
			if(entityTypeId != null && entityTypeId ==IDPAConstants.ENTITY_ACTIVITY_TYPE){
				entityTypeMapping=" LEFT JOIN activity act ON (act.activityId=actorMapping.entityInstanceId AND wfstatus.workflowStatusId=act.workflowStatusId)";
				workflowStatusCheckingCondition=" act.workflowStatusId >0 ";
			} else if(entityTypeId != null && entityTypeId == IDPAConstants.ENTITY_ACTIVITY_WORKPACKAGE_ID) {
				entityTypeMapping=" LEFT JOIN activity_work_package wp ON (wp.activityWorkPackageId=actorMapping.entityInstanceId AND wfstatus.workflowStatusId=wp.workflowStatusId)";
				workflowStatusCheckingCondition=" wp.workflowStatusId >0 ";
			} else if(entityTypeId != null && entityTypeId == IDPAConstants.ENTITY_TASK_TYPE) {
				entityTypeMapping=" LEFT JOIN activity_task task ON (task.activityTaskId=actorMapping.entityInstanceId AND wfstatus.workflowStatusId=task.statusId)";
				workflowStatusCheckingCondition=" task.statusId >0 ";
			} else if(entityTypeId != null && entityTypeId == IDPAConstants.ENTITY_TEST_CASE_ID) {
				entityTypeMapping=" LEFT JOIN test_case_list testcase ON (testcase.testCaseId=actorMapping.entityInstanceId AND wfstatus.workflowStatusId=testcase.workflowStatusId)";
				workflowStatusCheckingCondition=" testcase.workflowStatusId >0 ";
			}
			String myActionCountQuery ="SELECT COUNT(DISTINCT actorMapping.entityInstanceId),entity.entityMasterName FROM wf_entity_instance_status_actors_mapping actorMapping"+
										" LEFT JOIN user_list users ON (users.userId=actorMapping.userId AND actorMapping.actorMappingType = 'User')"+
										" LEFT JOIN user_role_master role ON (role.userRoleId=actorMapping.roleId AND actorMapping.actorMappingType = 'Role')"+
										" LEFT JOIN entitymaster entity ON(actorMapping.entityTypeId = entity.entitymasterid)"+
										" LEFT JOIN wf_workflows_status wfstatus ON (wfstatus.workflowstatusId=actorMapping.workflowstatusId AND wfstatus.workflowStatusType <>'End')"+entityTypeMapping+ 
										" WHERE actorMapping.productId ="+productId+" AND actorMapping.entityTypeId="+entityTypeId+" AND workflowStatusType <> 'End' "+
										" AND ((actorMapping.userId ="+userId+") OR (actorMapping.roleId ="+userRoleId+") )  AND userActionStatus <> 'Completed' AND "+workflowStatusCheckingCondition+" GROUP BY actorMapping.entityTypeId";
			
			
			myPendingActionCounts = sessionFactory.getCurrentSession().createSQLQuery(myActionCountQuery).list();
			
		} catch (RuntimeException re) {
			log.error("Error inside getWorkflowMyActionCounts ", re);
		}
		return myPendingActionCounts;
	}

	@Override
	@Transactional
	public List<WorkflowMaster> getWorkflowMasterMappedToEntityList(Integer productId, Integer entityTypeId, Integer entityId, Integer activeState) {
		log.debug("Fetching WorkflowMasterMappedToEntityList with parameters productId - "+productId+", entityTypeId - "+entityTypeId+", entityId - "+entityId+", activeState - "+activeState);
		List<WorkflowMaster> workflowList = null;
		List<WorkflowMasterEntityMapping> workflowMasterEntityMappings = null; 
		try{
			Criteria workflowMasterCriteria = sessionFactory.getCurrentSession().createCriteria(WorkflowMasterEntityMapping.class, "wfEntityMapping");
			if(productId != null && productId > 0){
				workflowMasterCriteria.createAlias("wfEntityMapping.product", "product");
				workflowMasterCriteria.add(Restrictions.eq("product.productId", productId));
			}
			workflowMasterCriteria.createAlias("wfEntityMapping.entityType", "entityType");
			workflowMasterCriteria.add(Restrictions.eq("entityType.entitymasterid", entityTypeId));
			if(entityId != null && entityId > 0) {
				workflowMasterCriteria.add(Restrictions.eq("wfEntityMapping.entityId", entityId));
			}else{
				workflowMasterCriteria.add(Restrictions.isNull("wfEntityMapping.entityId"));
			}
			workflowMasterCriteria.add(Restrictions.isNull("wfEntityMapping.entityInstanceId"));
			workflowMasterCriteria.addOrder(Order.desc("wfEntityMapping.isDefault"));
			
			workflowMasterEntityMappings = workflowMasterCriteria.list();
			
			if(workflowMasterEntityMappings != null && workflowMasterEntityMappings.size() > 0){
				workflowList = new ArrayList<WorkflowMaster>();
				for(WorkflowMasterEntityMapping workflowMasterEntityMapping : workflowMasterEntityMappings){
					if(workflowMasterEntityMapping.getWorkflow() != null && workflowMasterEntityMapping.getWorkflow().getActiveState() == activeState && workflowMasterEntityMapping.getWorkflow().getReadyState() == 1){
						workflowList.add(workflowMasterEntityMapping.getWorkflow());
					}
				}
			}
		}catch(Exception ex){
			log.error("Error inside getWorkflowMasterMappedToEntityList - ", ex);
		}
		return workflowList;
	}

	@Override
	@Transactional
	public List<WorkflowMaster> getWorkFlowMasterListByWorkflowName(String WorkflowName) {

		List<WorkflowMaster> workFlowMasterList = null;
		try{				
		Criteria workflowMasterCriteria = sessionFactory.getCurrentSession().createCriteria(WorkflowMaster.class, "WorkflowMaster");		
		
		workflowMasterCriteria.add(Restrictions.eq("WorkflowMaster.workflowName",WorkflowName));
		
		workFlowMasterList = workflowMasterCriteria.list();		
		return workFlowMasterList;
		}catch(Exception ex){			
			log.error("Error inside getWorkflowMasterMappedToEntityList - ", ex);			
		}
				return workFlowMasterList;	

		}

	@Override
	@Transactional
	public List<WorkflowMasterEntityMapping> getEntitiesAndInstanceMappedWithWorkflow(Integer workflowId) {
		log.info("Inside getEntitiesAndInstanceMappedWithWorkflow with parameters workflowId - "+workflowId);
		List<WorkflowMasterEntityMapping> workflowMasterEntityMappings = null; 
		try{
			Criteria workflowMasterCriteria = sessionFactory.getCurrentSession().createCriteria(WorkflowMasterEntityMapping.class, "wfEntityMapping");
			workflowMasterCriteria.createAlias("wfEntityMapping.workflow", "workflow");
			workflowMasterCriteria.add(Restrictions.eq("workflow.workflowId", workflowId));
			
			workflowMasterEntityMappings = workflowMasterCriteria.list();
		}catch(Exception ex){
			log.error("Error inside getEntitiesAndInstanceMappedWithWorkflow - ", ex);
		}
		return workflowMasterEntityMappings;
	}

	@Override
	@Transactional
	public Boolean isWorkflowAleadyExist(Integer referenceWorkflowId, String workflowName) {
		Boolean isWorkflowExist = false;
		try{
			Criteria criteria = sessionFactory.getCurrentSession().createCriteria(WorkflowMaster.class, "workflowMaster")
			.add(Restrictions.eq("workflowMaster.workflowName", workflowName));
			if(referenceWorkflowId != null && referenceWorkflowId > 0){
				criteria.add(Restrictions.ne("workflowMaster.workflowId", referenceWorkflowId));
			}
			List<WorkflowMaster> workflowMasters = criteria.list();
			if(workflowMasters != null && workflowMasters.size() > 0){
				isWorkflowExist = true;
			}
		}catch(Exception ex){
			log.error("Error in isWorkflowAleadyExist - ", ex);
		}
		return isWorkflowExist;
	}

	@Override
	@Transactional
	public List<WorkflowMaster> getWorkflowMasterListByStatus(Integer isActive) {
		try {
			 Criteria workflowMasterCriteria = sessionFactory.getCurrentSession().createCriteria(WorkflowMaster.class, "wfMaster");
			 if(isActive !=2) {
				 workflowMasterCriteria.add(Restrictions.eq("wfMaster.activeState", isActive));
			 }
			 return workflowMasterCriteria.list();
			} catch(RuntimeException re) {
				log.error("Error in getWorkflowMasterList "+re);
			}
			return null;
	}
}