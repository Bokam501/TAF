/**
 * 
 */
package com.hcl.ilcm.workflow.service.impl;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hcl.atf.taf.constants.IDPAConstants;
import com.hcl.atf.taf.controller.utilities.DateUtility;
import com.hcl.atf.taf.dao.CommonDAO;
import com.hcl.atf.taf.model.Activity;
import com.hcl.atf.taf.model.ActivityTask;
import com.hcl.atf.taf.model.ActivityWorkPackage;
import com.hcl.atf.taf.model.Comments;
import com.hcl.atf.taf.model.EntityMaster;
import com.hcl.atf.taf.model.ProductFeature;
import com.hcl.atf.taf.model.ProductMaster;
import com.hcl.atf.taf.model.TestCaseList;
import com.hcl.atf.taf.model.UserList;
import com.hcl.atf.taf.model.json.JsonActivity;
import com.hcl.atf.taf.model.json.JsonActivityTask;
import com.hcl.atf.taf.model.json.JsonActivityWorkPackage;
import com.hcl.atf.taf.model.json.JsonTestCaseList;
import com.hcl.atf.taf.service.ActivityService;
import com.hcl.atf.taf.service.ActivityTaskService;
import com.hcl.atf.taf.service.ActivityWorkPackageService;
import com.hcl.atf.taf.service.CommonService;
import com.hcl.atf.taf.service.CustomFieldService;
import com.hcl.atf.taf.service.ProductListService;
import com.hcl.ilcm.workflow.dao.WorkflowActivityDAO;
import com.hcl.ilcm.workflow.dao.WorkflowActivityWorkpackageDAO;
import com.hcl.ilcm.workflow.dao.WorkflowEventDAO;
import com.hcl.ilcm.workflow.dao.WorkflowMasterDAO;
import com.hcl.ilcm.workflow.dao.WorkflowStatusPolicyDAO;
import com.hcl.ilcm.workflow.dao.WorkflowTaskDAO;
import com.hcl.ilcm.workflow.dao.WorkflowTestcasesDAO;
import com.hcl.ilcm.workflow.model.WorkflowMaster;
import com.hcl.ilcm.workflow.model.WorkflowMasterEntityMapping;
import com.hcl.ilcm.workflow.model.WorkflowStatus;
import com.hcl.ilcm.workflow.model.WorkflowStatusActor;
import com.hcl.ilcm.workflow.model.WorkflowStatusPolicy;
import com.hcl.ilcm.workflow.model.json.JsonWorkflowSLAInstance;
import com.hcl.ilcm.workflow.service.ConfigurationWorkFlowService;
import com.hcl.ilcm.workflow.service.WorkflowEventService;
import com.hcl.ilcm.workflow.service.WorkflowStatusPolicyService;

/**
 * @author silambarasur
 *
 */
@Service
public class WorkflowStatusPolicyServiceImpl implements WorkflowStatusPolicyService{
	private static final Log log = LogFactory.getLog(WorkflowStatusPolicyServiceImpl.class);
	
	@Autowired
	private WorkflowStatusPolicyDAO workflowStatusPolicyDAO;
	
	@Autowired
	private WorkflowMasterDAO workflowMasterDAO;
	
	@Autowired
	private WorkflowEventDAO workflowEventDAO;
	
	@Autowired
	private WorkflowTaskDAO workflowTaskDAO;
	
	@Autowired
	private WorkflowTestcasesDAO workflowTestcasesDAO;
	
	@Autowired
	private WorkflowEventService workflowEventService;
	
	@Autowired
	private WorkflowActivityDAO workflowActivityDAO;
	
	@Autowired
	private ActivityTaskService activityTaskService;
	
	@Autowired
	private ActivityService activityService;
	
	@Autowired
	private ActivityWorkPackageService activityWorkPackageService;
	
	@Autowired
	private WorkflowActivityWorkpackageDAO workflowActivityWorkpackageDAO;
	
	@Autowired
	private CommonDAO commonDAO;
	
	@Autowired
	private CommonService commonService;
	
	@Autowired
	private ConfigurationWorkFlowService configurationWorkFlowService;
	
	@Autowired
	private CustomFieldService customFieldService;
	
	@Autowired
	private ProductListService productListService;
	
	@Autowired
	private HttpSession session;
	
	@Override
	@Transactional
	public List<WorkflowStatusPolicy> getWorkflowStatusPolicies(Integer workflowId,Integer entityTypeId,Integer entityId, Integer entityInstanceId, Integer levelId,Integer activeStatus, String statusPolicyType) {
		return workflowStatusPolicyDAO.getWorkflowStatusPolicies(workflowId, entityTypeId, entityId, entityInstanceId, levelId, activeStatus, statusPolicyType);
	}
	
	@Override
	@Transactional
	public void addWorkflowStatusPolicy(WorkflowStatusPolicy workflowStatusPolicies,Integer workflowId) {
		workflowStatusPolicyDAO.addWorkflowStatusPolicy(workflowStatusPolicies,workflowId);
	}
	
	@Override
	@Transactional
	public void mapStatusToEntityAndInstance(WorkflowStatusPolicy workflowStatusPolicy, Integer workflowId) {
		try{
			List<WorkflowMasterEntityMapping> workflowMasterEntityMappings = getEntitiesAndInstanceMappedWithWorkflow(workflowId);
			for(WorkflowMasterEntityMapping workflowMasterEntityMapping : workflowMasterEntityMappings){
				
				WorkflowStatusPolicy workflowStatusPolicyClone = (WorkflowStatusPolicy) workflowStatusPolicy.clone();
				workflowStatusPolicyClone.setWorkflowStatusPolicyId(null);
				EntityMaster entityMaster = workflowMasterEntityMapping.getEntityType();
				workflowStatusPolicyClone.setEntityType(entityMaster);
				workflowStatusPolicyClone.setEntityId(workflowMasterEntityMapping.getEntityId());
				workflowStatusPolicyClone.setLevel("Product");
				workflowStatusPolicyClone.setLevelId(workflowMasterEntityMapping.getProduct().getProductId());
				
				if("Entity".equalsIgnoreCase(workflowMasterEntityMapping.getMappingLevel())){
					workflowStatusPolicyClone.setStatusPolicyType("Entity");
				}else if("Instance".equalsIgnoreCase(workflowMasterEntityMapping.getMappingLevel())){
					workflowStatusPolicyClone.setStatusPolicyType("Instance");
					workflowStatusPolicyClone.setEntityInstanceId(workflowMasterEntityMapping.getEntityInstanceId());
				}
				
				workflowStatusPolicyDAO.addClonedStatusPolicies(workflowStatusPolicyClone);
			}
		}catch(Exception ex){
			log.error("Error in mapStatusToEntityAndInstance - ", ex);
		}
	}
	
	@Override
	@Transactional
	public List<WorkflowMasterEntityMapping> getEntitiesAndInstanceMappedWithWorkflow(Integer workflowId){
		List<WorkflowMasterEntityMapping> workflowMasterEntityMappings = new ArrayList<WorkflowMasterEntityMapping>();
		try{
			workflowMasterEntityMappings = configurationWorkFlowService.getEntitiesAndInstanceMappedWithWorkflow(workflowId);
		}catch(Exception ex){
			log.error("Error in getEntitiesAndInstanceMappedWithWorkflow - ", ex);
		}
		return workflowMasterEntityMappings;
	}
	
	@Override
	@Transactional
	public void updateWorkflowStatusPolicy(WorkflowStatusPolicy workflowStatusPolicies,Integer workflowId) {
		workflowStatusPolicyDAO.updateWorkflowStatusPolicy(workflowStatusPolicies,workflowId);
	}

	@Override
	@Transactional
	public void setEntityStatusPoliciesFromWorkflowPolicies(Integer productId, Integer entityTypeId, Integer entityId, Integer workflowId) {

		try{
			if (workflowId == null)
				return;
			
			List<WorkflowStatusPolicy> workflowStatusPolicies = workflowStatusPolicyDAO.getWorkflowStatusPolicies(workflowId, null, null, null, null, 1, "Workflow");
			if (workflowStatusPolicies == null || workflowStatusPolicies.isEmpty()) {
				return;
			}
			
			WorkflowStatusPolicy entityStatusPolicy;
			for (WorkflowStatusPolicy workflowStatusPolicy : workflowStatusPolicies) {
				entityStatusPolicy = (WorkflowStatusPolicy) workflowStatusPolicy.clone();
				entityStatusPolicy.setWorkflowStatusPolicyId(null);
				entityStatusPolicy.setStatusPolicyType("Entity");
				EntityMaster entityMaster = new EntityMaster();
				entityMaster.setEntitymasterid(entityTypeId);
				entityStatusPolicy.setEntityType(entityMaster);
				entityStatusPolicy.setEntityId(entityId);
				entityStatusPolicy.setLevel("Product");
				entityStatusPolicy.setLevelId(productId);
				workflowStatusPolicyDAO.addClonedStatusPolicies(entityStatusPolicy);
			}
		}catch(Exception ex){
			log.error("Error while processing cloning workflow policies to entity ", ex);
		}
		
	}

	@Override
	@Transactional
	public void setEntityInstanceStatusPoliciesFromWorkflowPolicies(Integer productId, Integer entityTypeId, Integer entityId, Integer entityInstanceId, Integer workflowId, Date plannedStartDate) {

		try{
			if (workflowId == null)
				return;
			if (entityTypeId == null)
				return;
			if (productId == null)
				return;
			
			List<WorkflowStatusPolicy> entityStatusPolicies = workflowStatusPolicyDAO.getWorkflowStatusPolicies(workflowId, entityTypeId, entityId, null, productId, 1, "Entity");
			if (entityStatusPolicies == null || entityStatusPolicies.isEmpty()) {
				return;
			}
			
			Integer entityTotalSLADurationHours = 0;
			for (WorkflowStatusPolicy entityStatusPolicy : entityStatusPolicies) {
				if(entityStatusPolicy != null && entityStatusPolicy.getSlaDuration() != null){
					entityTotalSLADurationHours += entityStatusPolicy.getSlaDuration();
				}
			}
			
			WorkflowStatusPolicy instanceStatusPolicy;
			for (WorkflowStatusPolicy entityStatusPolicy : entityStatusPolicies) {
				instanceStatusPolicy = (WorkflowStatusPolicy) entityStatusPolicy.clone();
				instanceStatusPolicy.setWorkflowStatusPolicyId(null);
				instanceStatusPolicy.setStatusPolicyType("Instance");
				EntityMaster entityMaster = new EntityMaster();
				entityMaster.setEntitymasterid(entityTypeId);
				entityStatusPolicy.setEntityType(entityMaster);
				instanceStatusPolicy.setEntityId(entityId);
				instanceStatusPolicy.setLevel("Product");
				instanceStatusPolicy.setLevelId(productId);
				instanceStatusPolicy.setEntityInstanceId(entityInstanceId);
				Integer instanceStatusSLADuration = getInstanceStatusSLADuration(entityStatusPolicy.getSlaDuration(), entityTotalSLADurationHours, entityTypeId, entityInstanceId);
				instanceStatusPolicy.setSlaDuration(instanceStatusSLADuration);
				setPlannedDatesAndEfforts(instanceStatusPolicy, plannedStartDate);
				plannedStartDate = instanceStatusPolicy.getPlannedEndDate();

				workflowStatusPolicyDAO.addClonedStatusPolicies(instanceStatusPolicy);
				instanciateActorFromEntityToInstance(productId, entityStatusPolicy.getWorkflowStatus().getWorkflowStatusId(), entityTypeId, entityId, entityInstanceId, "Both");
			}
			instanciateWorkflowForInstance(productId, workflowId, entityTypeId, entityId, entityInstanceId);
			
		}catch(Exception ex){
			log.error("Error while processing cloning entity policies to entity instance ", ex);
		}
	}
	
	private Integer getInstanceStatusSLADuration(Integer entityStatusSLADuration, Integer entityTotalSLADurationHours, Integer entityTypeId, Integer entityInstanceId) {
		Integer instanceStatusSLADuration = entityStatusSLADuration;
		try{
			if(entityStatusSLADuration > 0 && entityTotalSLADurationHours > 0){
				Integer hoursBetweenPlannedDates = getHoursBetweenInstancePlannedDates(entityTypeId, entityInstanceId);
				if(hoursBetweenPlannedDates > 0){
					instanceStatusSLADuration = ((hoursBetweenPlannedDates * entityStatusSLADuration) / entityTotalSLADurationHours);
				}
			}
		}catch(Exception ex){
			log.error("Exception in getInstanceStatusSLADuration - ", ex);
		}
		if(instanceStatusSLADuration == null){
			instanceStatusSLADuration = 0;
		}
		return instanceStatusSLADuration;
	}

	private void setPlannedDatesAndEfforts(WorkflowStatusPolicy instanceWorkflowStatusPolicy, Date plannedStartDate){
		try{
			if(plannedStartDate == null){
				plannedStartDate = new Date();
			}
			instanceWorkflowStatusPolicy.setPlannedStartDate(plannedStartDate);
			Calendar plannedEndDate = Calendar.getInstance();
			plannedEndDate.setTime(plannedStartDate);
			plannedEndDate.add(Calendar.HOUR_OF_DAY, instanceWorkflowStatusPolicy.getSlaDuration());
			instanceWorkflowStatusPolicy.setPlannedEndDate(plannedEndDate.getTime());
			instanceWorkflowStatusPolicy.setPlannedEffort(1.0F);
		}catch(Exception ex){
			log.error("Exception occured in setPlannedDatesAndEfforts - ", ex);
		}
	}
	
	private Integer getHoursBetweenInstancePlannedDates(Integer entityTypeId, Integer entityInstanceId) {
		Integer hoursBetweenPlannedDates = 0;
		try{
			Date startDate = null;
			Date endDate = null;
			if(entityTypeId == IDPAConstants.ENTITY_TASK){
				ActivityTask activityTask = activityTaskService.getActivityTaskById(entityInstanceId, 0);
				startDate = activityTask.getPlannedStartDate();
				endDate = activityTask.getPlannedEndDate();
			}else if(entityTypeId == IDPAConstants.ENTITY_ACTIVITY_ID){
				Activity activity = activityService.getActivityById(entityInstanceId, 0);
				startDate = activity.getPlannedStartDate();
				endDate = activity.getPlannedEndDate();
			}else if(entityTypeId == IDPAConstants.ENTITY_ACTIVITY_WORKPACKAGE_ID){
				ActivityWorkPackage activityWorkPackage = activityWorkPackageService.getActivityWorkPackageById(entityInstanceId, 0);
				startDate = activityWorkPackage.getPlannedStartDate();
				endDate = activityWorkPackage.getPlannedEndDate();
			}else if(entityTypeId == IDPAConstants.ENTITY_TEST_CASE_ID){
				
			}
			if(startDate != null && endDate != null){
				if(startDate.equals(endDate)){
					Calendar endDateCalendar = Calendar.getInstance();
					endDateCalendar.setTime(endDate);
					endDateCalendar.add(Calendar.HOUR_OF_DAY, 24);
				}
				hoursBetweenPlannedDates = DateUtility.getCalendarHoursBetweenDates(startDate, endDate);
			}
		}catch(Exception ex){
			log.error("Exception in getHoursBetweenInstancePlannedDates - ", ex);
		}
		return hoursBetweenPlannedDates;
	}

	@Override
	@Transactional
	public void instanciateActorFromEntityToInstance(Integer productId, Integer workflowStatusId, Integer entityTypeId, Integer entityId, Integer entityInstanceId, String actorType){
		try{
			List<WorkflowStatusActor> entityStatusActors = workflowStatusPolicyDAO.getWorkflowStatusActor(productId, workflowStatusId, entityTypeId, entityId, null, "Both", null, null);
			for(WorkflowStatusActor workflowStatusActor : entityStatusActors){
				WorkflowStatusActor instancerStatusActor = (WorkflowStatusActor) workflowStatusActor.clone();
				instancerStatusActor.setWorkflowStatusActorId(null);
				instancerStatusActor.setEntityInstanceId(entityInstanceId);
				instancerStatusActor.setMappingLevel("Instance");
				boolean isMappingAleadyExist = true;
				UserList defaultActor=null;
				if(instancerStatusActor.getUser() != null){
					if(instancerStatusActor.getUser().getUserId() != null && instancerStatusActor.getUser().getUserId() == -1){
						if(entityTypeId == IDPAConstants.ENTITY_TASK_TYPE){
							ActivityTask activityTask = activityTaskService.getActivityTaskById(entityInstanceId, 0);
							instancerStatusActor.setUser(activityTask.getAssignee());
						}else if(entityTypeId == IDPAConstants.ENTITY_ACTIVITY_TYPE){
							Activity activity = activityService.getActivityById(entityInstanceId, 0);
							instancerStatusActor.setUser(activity.getAssignee());
							defaultActor = activity.getActivityWorkPackage().getOwner();
						}else if(entityTypeId == IDPAConstants.PRODUCT_BUILD_ENTITY_MASTER_ID){
							ProductFeature productFeature = productListService.getByProductFeatureId(entityInstanceId);
							instancerStatusActor.setUser(productFeature.getAssignee());
							defaultActor = productFeature.getAssignee();
						}
					}
					if(instancerStatusActor.getUser().getUserId() != null && instancerStatusActor.getUser().getUserId() == -2){
						if(entityTypeId == IDPAConstants.ENTITY_TASK_TYPE){
							ActivityTask activityTask = activityTaskService.getActivityTaskById(entityInstanceId, 0);
							instancerStatusActor.setUser(activityTask.getReviewer());
						}else if(entityTypeId == IDPAConstants.ENTITY_ACTIVITY_TYPE){
							Activity activity = activityService.getActivityById(entityInstanceId, 0);
							instancerStatusActor.setUser(activity.getReviewer());
						}else if(entityTypeId == IDPAConstants.PRODUCT_BUILD_ENTITY_MASTER_ID){
							ProductFeature productFeature = productListService.getByProductFeatureId(entityInstanceId);
							instancerStatusActor.setUser(productFeature.getReviewer());
						}
					}
					if(instancerStatusActor.getUser() != null && instancerStatusActor.getUser().getUserId() != null){
						isMappingAleadyExist = checkWorkflowStatusActorAlreadyExist(productId, null, workflowStatusId, entityTypeId, entityId, entityInstanceId, instancerStatusActor.getUser().getUserId(), null);
					}
					
				}else if(instancerStatusActor.getRole() != null){
					if(instancerStatusActor.getRole() != null && instancerStatusActor.getRole().getUserRoleId() != null){
						isMappingAleadyExist = checkWorkflowStatusActorAlreadyExist(productId, null, workflowStatusId, entityTypeId, entityId, entityInstanceId, null, instancerStatusActor.getRole().getUserRoleId());
					}
				}
				
				if(!isMappingAleadyExist){
					workflowStatusPolicyDAO.addWorkflowStatusActor(instancerStatusActor);
				}
				
			}
		}catch(Exception ex){
			log.error("Error while instanciating user or role from entity to instance ", ex);
		}
	}
	
	@Override
	@Transactional
	public void instanciateWorkflowForInstance(Integer productId, Integer workflowId, Integer entityTypeId, Integer entityId, Integer entityInstanceId){
		try{
			WorkflowMasterEntityMapping workflowMasterEntityMapping = workflowMasterDAO.getWorkflowOfEntityInstance(productId, workflowId, entityTypeId, entityId, entityInstanceId);
			
			if(workflowMasterEntityMapping == null){
				workflowMasterEntityMapping = new WorkflowMasterEntityMapping();
				ProductMaster productMaster = new ProductMaster();
				productMaster.setProductId(productId);
				workflowMasterEntityMapping.setProduct(productMaster);
				workflowMasterEntityMapping.setMappingLevel("Instance");
				EntityMaster entityMaster = new EntityMaster();
				entityMaster.setEntitymasterid(entityTypeId);
				workflowMasterEntityMapping.setEntityType(entityMaster);
				workflowMasterEntityMapping.setEntityId(entityId);
				workflowMasterEntityMapping.setEntityInstanceId(entityInstanceId);
			}
			workflowMasterEntityMapping.setIsDefault(1);
			WorkflowMaster workflowMaster = new WorkflowMaster();
			workflowMaster.setWorkflowId(workflowId);
			workflowMasterEntityMapping.setWorkflow(workflowMaster);
			
			workflowMasterDAO.addWorkflowProductEntityMapping(workflowMasterEntityMapping);
		}catch(Exception ex){
			log.error("Error while instanciating workflow for instance ", ex);
		}
	}

	@Override
	@Transactional
	public void instanciateWorkflowStatusAndPoliciesForInstanceByWorkflowId(Integer productId, Integer entityTypeId, Integer entityId, Integer entityInstanceId, Integer workflowId, Integer statusId, UserList user, Date plannedStartDate, Object instanceObject){
		try{
			if(statusId != null && statusId > 0){
				workflowEventService.setInitialInstanceEvent(productId, entityTypeId, entityId, entityInstanceId, statusId, null, 0, "", user);
			}
			if(workflowId != null && workflowId > 0){
				setEntityInstanceStatusPoliciesFromWorkflowPolicies(productId, entityTypeId, entityId, entityInstanceId, workflowId, plannedStartDate);
			}
			workflowEventService.checkAndUpdateStatusOfInstanceParentOrChild(productId, entityTypeId, entityInstanceId, instanceObject);
		}catch(Exception ex){
			log.error("Error while instanciating workflow status and policies for instance ", ex);
		}
	}
	
	@Override
	@Transactional
	public void instanciateWorkflowStatusAndPoliciesForInstanceByEntityType(Integer productId, Integer entityTypeId, Integer entityId, Integer entityInstanceId, Integer statusId, UserList user, Date plannedStartDate, Object instanceObject){
		try{
			if(statusId != null && statusId > 0){
				workflowEventService.setInitialInstanceEvent(productId, entityTypeId, entityId, entityInstanceId, statusId, null, 0, "", user);
			}
			Integer activeState=1;
			WorkflowMaster workflowMaster = workflowMasterDAO.getWorkflowForEntityTypeOrInstance(productId, entityTypeId,  entityId, null, activeState);
			if(workflowMaster != null && workflowMaster.getWorkflowId() != null && workflowMaster.getWorkflowId() > 0) {
				setEntityInstanceStatusPoliciesFromWorkflowPolicies(productId, entityTypeId, entityId, entityInstanceId, workflowMaster.getWorkflowId(), plannedStartDate);
			}
			workflowEventService.checkAndUpdateStatusOfInstanceParentOrChild(productId, entityTypeId, entityInstanceId, instanceObject);
		}catch(Exception ex){
			log.error("Error while instanciating workflow status and policies for instance ", ex);
		}
	}
	
	@Override
	@Transactional
	public void addWorkflowStatusActor(WorkflowStatusActor workflowStatusActor) {
		workflowStatusPolicyDAO.addWorkflowStatusActor(workflowStatusActor);
	}

	@Override
	@Transactional
	public List<WorkflowStatusActor> getWorkflowStatusActor(Integer productId, Integer workflowStatusId, Integer entityTypeId, Integer entityId, Integer entityInstanceId, String actorType) {
		return workflowStatusPolicyDAO.getWorkflowStatusActor(productId, workflowStatusId, entityTypeId, entityId, entityInstanceId, actorType, null, null);
	}

	@Override
	@Transactional
	public boolean checkWorkflowStatusActorAlreadyExist(Integer productId, Integer workflowStatusActorId, Integer workflowStatusId, Integer entityTypeId, Integer entityId, Integer entityInstanceId, Integer userId, Integer roleId) {
		return workflowStatusPolicyDAO.checkWorkflowStatusActorAlreadyExist(productId, workflowStatusActorId, workflowStatusId, entityTypeId, entityId, entityInstanceId, userId, roleId);
	}

	@Override
	@Transactional
	public void deleteWorkflowStatusActor(WorkflowStatusActor workflowStatusActor) {
		workflowStatusPolicyDAO.deleteWorkflowStatusActor(workflowStatusActor);
	}

	@Override
	@Transactional
	public void updateInstanceEntityMappingAndDeleteStatusPolicies(Integer entityTypeId, Integer entityId, Integer entityInstanceId, Integer workflowId) {
		workflowStatusPolicyDAO.updateInstanceEntityMappingAndDeleteStatusPolicies(entityTypeId, entityId, entityInstanceId, workflowId);
	}
	
	@Override
	@Transactional
	public List<Object[]> listWorkflowStatusSummary(Integer engagmentId,Integer productId, Integer entityTypeId, Integer entityId, Integer entityInstanceId){
		return workflowStatusPolicyDAO.listWorkflowStatusSummary(engagmentId,productId, entityTypeId, entityId, entityInstanceId);
	}
	@Override
	@Transactional
	public List<Object[]> getPendingWithInstanceActors(Integer productId,Integer entityTypeId,Integer entityId) {
		return workflowStatusPolicyDAO.getPendingWithInstanceActors(productId, entityTypeId, entityId);
	}

	@Override
	@Transactional
	public List<Object[]> listWorkflowStatusActorSummary(Integer engagementId,Integer productId, Integer entityTypeId, Integer entityId, Integer entityInstanceId, String actorType) {
		return workflowStatusPolicyDAO.listWorkflowStatusActorSummary(engagementId,productId, entityTypeId, entityId, entityInstanceId, actorType);
	}

	@Override
	@Transactional
	public Integer getWorkflowStatusActorSummaryPaginationCount(Integer productId, Integer entityTypeId, Integer entityId, Integer entityInstanceId, String actorType) {
		return workflowStatusPolicyDAO.getWorkflowStatusActorSummaryPaginationCount(productId, entityTypeId, entityId, entityInstanceId, actorType);
	}

	@Override
	@Transactional
	public Integer getWorkflowStatusSummaryPaginationCount(Integer productId, Integer entityTypeId, Integer entityId, Integer entityInstanceId) {
		return workflowStatusPolicyDAO.getWorkflowStatusSummaryPaginationCount(productId, entityTypeId, entityId, entityInstanceId);
	}
	
	@Override
	@Transactional
	public void setInstanceIndicators(Integer testFactoryId,Integer productId,Integer activityWorkPackageIds, Integer entityTypeId, List<?> instanceObjects, Integer attachemntEntityTypeId,UserList user, Date modifiedAfterDate, Integer entityId, Integer entityInstanceIdParam,boolean enablePendingWith) {
		
		try{
			HashMap<String, String> pendingWithQueryDetails = new HashMap<String, String>();
			HashMap<String, String> statusActionQueryDetails = new HashMap<String, String>();
			List<Object[]> instancePendingActorList =null;
			List<Comments> comments=null;
			if(IDPAConstants.ENTITY_ACTIVITY_TYPE == entityTypeId){
				pendingWithQueryDetails.put("joinColumn", " LEFT JOIN activity act ON actorMapping.entityInstanceId = act.activityId");
				pendingWithQueryDetails.put("workflowStatusCheck", " AND actorMapping.workflowStatusId = act.workflowStatusId ");
				statusActionQueryDetails.put("joinColumn", " LEFT JOIN activity act ON wfe.entityInstanceId = act.activityId");
			}else if(IDPAConstants.ENTITY_TASK_TYPE == entityTypeId){
				pendingWithQueryDetails.put("joinColumn", " LEFT JOIN activity_task task ON actorMapping.entityInstanceId = task.activityTaskId");
				pendingWithQueryDetails.put("workflowStatusCheck", " AND actorMapping.workflowStatusId = task.statusId");
				statusActionQueryDetails.put("joinColumn", " LEFT JOIN activity_task task ON wfe.entityInstanceId = task.activityTaskId");
			}else if(IDPAConstants.ENTITY_ACTIVITY_WORKPACKAGE_ID == entityTypeId){
				pendingWithQueryDetails.put("joinColumn", " LEFT JOIN activity_work_package actWP ON actorMapping.entityInstanceId = actWP.activityWorkPackageId");
				pendingWithQueryDetails.put("workflowStatusCheck", " AND actorMapping.workflowStatusId = actWP.workflowStatusId");
				statusActionQueryDetails.put("joinColumn", " LEFT JOIN activity_work_package actWP ON wfe.entityInstanceId = actWP.activityWorkPackageId");
			}else if(IDPAConstants.ENTITY_TEST_CASE_ID == entityTypeId){
				pendingWithQueryDetails.put("joinColumn", " LEFT JOIN test_case_list tcl ON actorMapping.entityInstanceId = tcl.testCaseId");
				pendingWithQueryDetails.put("workflowStatusCheck", " AND actorMapping.workflowStatusId = tcl.workflowStatusId");
				statusActionQueryDetails.put("joinColumn", " LEFT JOIN test_case_list tcl ON wfe.entityInstanceId = tcl.testCaseId");
			}
			String instanceIds="";
			
			Date userLoginDate = (Date) session.getAttribute("userLoginTime");
			Date userLogoutDate = (Date) session.getAttribute("userLogoutTime");
			
			if(activityWorkPackageIds != null && activityWorkPackageIds >0) {
				for(Object instance : instanceObjects){
					
					JsonActivityTask jsonActivityTask = null;
					JsonTestCaseList jsonTestCaseList = null;
					JsonActivity jsonActivity = null;
					JsonActivityWorkPackage jsonActivityWorkPackage = null;
					
					if(instance instanceof JsonActivityTask){
						jsonActivityTask = (JsonActivityTask) instance; 
						instanceIds += jsonActivityTask.getActivityTaskId()+",";
					} else if(instance instanceof JsonTestCaseList){
						jsonTestCaseList=(JsonTestCaseList)instance;
						instanceIds += jsonTestCaseList.getTestCaseId()+",";
					}else if(instance instanceof JsonActivity){
						jsonActivity=(JsonActivity)instance;
						instanceIds += jsonActivity.getActivityId()+",";
					}else if(instance instanceof JsonActivityWorkPackage){
						jsonActivityWorkPackage = (JsonActivityWorkPackage) instance;
						instanceIds += jsonActivityWorkPackage.getActivityWorkPackageId()+",";
					}
				}
				
				if(instanceIds.endsWith(",")) {
					instanceIds = instanceIds.substring(0, instanceIds.length()-1);
				}
				enablePendingWith=true;
			} else if(entityInstanceIdParam != null && entityInstanceIdParam >0){
				instanceIds = entityInstanceIdParam.toString();
			}
			if(enablePendingWith) {
				instancePendingActorList = workflowStatusPolicyDAO.getPendingWithInstanceActorsTestFactoryLevel(testFactoryId,productId, entityTypeId, instanceIds, entityId, pendingWithQueryDetails);
			}
			List<Object[]> currentStatusActionList = workflowEventDAO.getCurrentStatusActionViewTestFactoryLevel(testFactoryId,productId, entityTypeId, instanceIds, entityId, statusActionQueryDetails);
			List<Object[]> attachmentCountDetails = commonDAO.getAttachmentCountOfEntityTestFactoryLevel(attachemntEntityTypeId, entityInstanceIdParam);
			if(entityTypeId !=null && entityTypeId.equals(IDPAConstants.ENTITY_ACTIVITY_TYPE)) {
				comments=commonDAO.getLatestCommentOfByEnagementIdAndProductId(testFactoryId, productId, IDPAConstants.ENTITY_ACTIVITY_ID);
			}
			List<Integer> modifiedInstanceIds = new ArrayList<Integer>();
			if(modifiedAfterDate != null){
				modifiedInstanceIds = customFieldService.getInstanceIdsOfCustomFieldValuesModifiedAfter(entityTypeId, testFactoryId, productId, modifiedAfterDate);
				if(modifiedInstanceIds == null){
					modifiedInstanceIds = new ArrayList<Integer>();
				}
			}
			
			for(Object instanceObject : instanceObjects){
				
				Integer entityInstanceId = null;
				Integer statusId = null;
				String plannedDatesRAG = " <i class='fa fa-square' style='color: red;' title='Planned end date elapsed / not defined'></i> ";
				String progessIndicator="#E7505A";
				Date plannedStartDate = null;
				Date plannedEndDate = null;
				Date actualEndDate = null;
				Float percentageComplition=null;
				
				String lastestComment="";
				
				JsonActivityTask jsonActivityTask = null;
				JsonTestCaseList jsonTestCaseList = null;
				JsonActivity jsonActivity = null;
				JsonActivityWorkPackage jsonActivityWorkPackage = null;
				
				if(instanceObject instanceof JsonActivityTask){
					jsonActivityTask = (JsonActivityTask) instanceObject; 
					entityInstanceId = jsonActivityTask.getActivityTaskId();
					statusId = jsonActivityTask.getStatusId();
					plannedStartDate = DateUtility.getddmmyyyytoyyyymmddwithSec(jsonActivityTask.getPlannedStartDate());
					plannedEndDate = DateUtility.getddmmyyyytoyyyymmddwithSec(jsonActivityTask.getPlannedEndDate());
					percentageComplition = jsonActivityTask.getPercentageCompletion();
					if(!"dd-mm-yy".equalsIgnoreCase(jsonActivityTask.getActualEndDate())){
						actualEndDate = DateUtility.getddmmyyyytoyyyymmddwithSec(jsonActivityTask.getActualEndDate());
					}
				}else if(instanceObject instanceof JsonTestCaseList){
					jsonTestCaseList = (JsonTestCaseList) instanceObject; 
					entityInstanceId = jsonTestCaseList.getTestCaseId();
					statusId = jsonTestCaseList.getWorkflowStatusId();
				}else if(instanceObject instanceof JsonActivity){
					jsonActivity = (JsonActivity) instanceObject;
					entityInstanceId = jsonActivity.getActivityId();
					statusId = jsonActivity.getStatusId();
					plannedStartDate = DateUtility.getddmmyyyytoyyyymmddwithSec(jsonActivity.getPlannedStartDate());
					plannedEndDate = DateUtility.getddmmyyyytoyyyymmddwithSec(jsonActivity.getPlannedEndDate());
					percentageComplition = jsonActivity.getPercentageCompletion();
					if(!"dd-mm-yy".equalsIgnoreCase(jsonActivity.getActualEndDate())){
						actualEndDate = DateUtility.getddmmyyyytoyyyymmddwithSec(jsonActivity.getActualEndDate());
					}
				}else if(instanceObject instanceof JsonActivityWorkPackage){
					jsonActivityWorkPackage = (JsonActivityWorkPackage) instanceObject;
					entityInstanceId = jsonActivityWorkPackage.getActivityWorkPackageId();
					statusId = jsonActivityWorkPackage.getStatusId();
					plannedStartDate = DateUtility.getddmmyyyytoyyyymmddwithSec(jsonActivityWorkPackage.getPlannedStartDate());
					plannedEndDate = DateUtility.getddmmyyyytoyyyymmddwithSec(jsonActivityWorkPackage.getPlannedEndDate());
					percentageComplition = jsonActivityWorkPackage.getPercentageCompletion();
					if(!"dd-mm-yy".equalsIgnoreCase(jsonActivityWorkPackage.getActualEndDate())){
						actualEndDate = DateUtility.getddmmyyyytoyyyymmddwithSec(jsonActivityWorkPackage.getActualEndDate());
					}
				}else{
					return;
				}
				
				String users = "";
				String roles = "";
				String userIds="";
				String roleIds="";
				boolean visibleEventComment=false;
				String pendingWith = "--";
				String workflowIndicator = " <i class='fa fa-circle' style='color: red;' title='SLA duration elapsed'></i> ";
				Integer remainingHrs = null;
				String remainingHrsMins = "0";
				Date completeBy = null;
				String completeByFormat = "--";
				Integer attachmentCount = 0;
				String statusType = "";
				
				if(instancePendingActorList != null && instancePendingActorList.size() > 0){
					for(Object[] actors : instancePendingActorList){
						Integer pendingEntityInstanceId = (Integer)actors[0];
						if(pendingEntityInstanceId != null && entityInstanceId.equals(pendingEntityInstanceId)){
							String actionScope = null;
							if(actors[3] != null){
								actionScope = (String) actors[3];
							}
							users=(String)((actors[1] != null && actionScope != null && (IDPAConstants.WORKFLOW_STATUS_POLICY_USER_ACTION_SCOPE.equalsIgnoreCase(actionScope) || IDPAConstants.WORKFLOW_STATUS_POLICY_USERORROLE_ACTION_SCOPE.equalsIgnoreCase(actionScope))) ? actors[1] : "");
							roles=(String)((actors[2] != null && actionScope != null && (IDPAConstants.WORKFLOW_STATUS_POLICY_ROLE_ACTION_SCOPE.equalsIgnoreCase(actionScope) || IDPAConstants.WORKFLOW_STATUS_POLICY_USERORROLE_ACTION_SCOPE.equalsIgnoreCase(actionScope))) ? actors[2] : "");
							if(actors[4] != null) {
								userIds=(String)actors[4];
							}
							if(actors[5] != null) {
								roleIds=(String)actors[5];
							}
						}
					}
				
					if(users!= null && !users.trim().isEmpty() && roles!= null && !roles.trim().isEmpty()) {
						pendingWith = users+","+roles;
					}else {
						pendingWith = users+""+roles;
					}
					if(pendingWith == null || pendingWith.trim().isEmpty()) {
						pendingWith = "--";
					}
					String []userIdArr=userIds.split(",");
					if(userIdArr!= null && userIdArr.length >0 && user != null){
						for(String userId:userIdArr) {
							if(userId != "" && (user.getUserId().equals(Integer.parseInt(userId)))){
								visibleEventComment=true;
								break;
							}
						}
						
					}
					
					String []roleArr=roleIds.split(",");
					if(roleArr!= null && roleArr.length >0 && user != null){
						for(String roleId:roleArr) {
							if((roleId != "" && !visibleEventComment) && (user.getUserRoleMaster().getUserRoleId().equals(Integer.parseInt(roleId)))){
								visibleEventComment=true;
								break;
							}
						}
						
					}
					
				}
				
				if(currentStatusActionList != null && currentStatusActionList.size() > 0) {
					for(Object[] statusAction : currentStatusActionList){
						Integer pendingEntityInstanceId = (Integer)statusAction[0];
						if(pendingEntityInstanceId != null && entityInstanceId.equals(pendingEntityInstanceId)){
							statusType = (String) statusAction[5];
							if(statusType != null && !"End".equalsIgnoreCase(statusType)){
								completeBy=(Date)statusAction[3];
								remainingHrs = 0;
								Double remainingHours = ((Double)statusAction[4]);
								if(remainingHours != null && remainingHours.intValue() > 0){
									remainingHrs = remainingHours.intValue();
								}
								
								if(remainingHrs >= 24) {
									workflowIndicator = " <i class='fa fa-circle' style='color: green;' title='Availble for action'></i> ";
								}else if(remainingHrs < 24 && remainingHrs >=2 ) {
									workflowIndicator = " <i class='fa fa-circle'  style='color: orange;' title='Needs immediate attention'></i> ";
								}else if(remainingHrs < 2 && remainingHrs > 0 ) {
									workflowIndicator = " <i class='fa fa-circle'  style='color: orangered;' title='Needs immidiate action'></i> ";
								}else if(remainingHrs <= 0) {
									workflowIndicator = " <i class='fa fa-circle' style='color: red;' title='SLA duration elapsed'></i> ";
									remainingHrs=0;
								}
							}else{
								workflowIndicator = " <i class='fa fa-circle' style='color: aqua;' title='Action completed / Not available for action'></i> ";
							}
						}
					}
					
					if(statusId == null || statusId == -1){
						workflowIndicator= " <i class='fa fa-circle' style='color: aqua;' title='Action completed / Not available for action'></i> ";
					}
					
					if(remainingHrs != null && remainingHrs >0) {
						remainingHrsMins = DateUtility.displayHoursMinutesFormat(completeBy);
					}
					
					if(completeBy != null) {
						completeByFormat = DateFormat.getDateTimeInstance(DateFormat.MEDIUM,DateFormat.SHORT).format(completeBy.getTime());
					}else{
						completeByFormat = "--";
					}
				}
				
				Long remainingHrsForRAG = 0L;
				if(plannedEndDate != null){
					plannedEndDate=DateUtility.formatedDateWithMaxTime(plannedEndDate);
					remainingHrsForRAG = plannedEndDate.getTime() - new Date().getTime() ;
					remainingHrsForRAG = (remainingHrsForRAG / (1000*60*60));
					
					plannedDatesRAG=setRAGDetailViewIndicator(plannedStartDate,plannedEndDate,percentageComplition);
					if(plannedDatesRAG.contains("red")) {
						progessIndicator = "#E7505A";
					} else if(plannedDatesRAG.contains("green")) {
						progessIndicator = "#26C281";
					} else if(plannedDatesRAG.contains("orange")) {
						progessIndicator = "#E87E04";
					}
				}else{
					remainingHrsForRAG = (long) Integer.MAX_VALUE;
				}
				
				if((statusType != null && (IDPAConstants.WORKFLOW_STATUS_TYPE_END.equalsIgnoreCase(statusType) || IDPAConstants.WORKFLOW_STATUS_TYPE_ABORT.equalsIgnoreCase(statusType))) || statusId == null || statusId == -1){
					String title = "Action Completed";
					if(IDPAConstants.WORKFLOW_STATUS_TYPE_ABORT.equalsIgnoreCase(statusType)){
						title = "Aborted";
					}
					plannedDatesRAG = " <i class='fa fa-sort-asc' style='color: green;' title='"+title+"'></i> ";
					remainingHrsForRAG = (long) Integer.MAX_VALUE;
					if(statusType != null && "End".equalsIgnoreCase(statusType)){
						if(plannedEndDate != null && actualEndDate != null && (plannedEndDate.after(actualEndDate) || plannedEndDate.equals(actualEndDate))){
							plannedDatesRAG = " <i class='fa fa-sort-asc' style='color: green;' title='"+title+"'></i> ";
						}else if(plannedEndDate != null && actualEndDate == null && (plannedEndDate.after(new Date()) || plannedEndDate.equals(new Date()))){
							plannedDatesRAG = " <i class='fa fa-sort-asc' style='color: green;' title='"+title+"'></i> ";
							
						}else{
							plannedDatesRAG = " <i class='fa fa-sort-desc' style='color: red;' title='"+title+"'></i> ";
						
						}
					}
				}
				
				attachmentCount = commonService.getCountFromAttachmentCountDetails(attachmentCountDetails, entityInstanceId);
				
				if(comments != null && comments.size() >0) {
					for(Comments activityLatestComment:comments) {
						if(activityLatestComment != null && activityLatestComment.getEntityId().equals(entityInstanceId)) {
							lastestComment = activityLatestComment.getCreatedBy().getLoginId() + " On ("+activityLatestComment.getCreatedDate() +"):- "+activityLatestComment.getComments();
							break;
						}
					}
				}
				
				if(jsonActivityTask != null){
					jsonActivityTask.setActors(pendingWith);
					jsonActivityTask.setRemainingHours(remainingHrsForRAG.intValue());
					jsonActivityTask.setRemainingHrsMins(remainingHrsMins);
					jsonActivityTask.setWorkflowIndicator(workflowIndicator);
					jsonActivityTask.setCompletedBy(completeByFormat);
					jsonActivityTask.setWorkflowRAG(plannedDatesRAG);
					jsonActivityTask.setAttachmentCount(attachmentCount);
					jsonActivityTask.setVisibleEventComment(visibleEventComment);
				}else if(jsonTestCaseList != null){
					jsonTestCaseList.setActors(pendingWith);
					jsonTestCaseList.setRemainingHours(remainingHrsForRAG.intValue());
					jsonTestCaseList.setRemainingHrsMins(remainingHrsMins);
					jsonTestCaseList.setWorkflowIndicator(workflowIndicator);
					jsonTestCaseList.setCompletedBy(completeByFormat);
					jsonTestCaseList.setWorkflowIndicator(workflowIndicator);
					jsonTestCaseList.setAttachmentCount(attachmentCount);
				}else if(jsonActivity != null){
					jsonActivity.setActors(pendingWith);
					jsonActivity.setRemainingHours(remainingHrsForRAG.intValue());
					jsonActivity.setRemainingHrsMins(remainingHrsMins);
					jsonActivity.setWorkflowIndicator(workflowIndicator);
					jsonActivity.setCompletedBy(completeByFormat);
					jsonActivity.setWorkflowIndicator(workflowIndicator);
					jsonActivity.setWorkflowRAG(plannedDatesRAG);
					if(jsonActivity.getWorkflowStatusType() !=null && jsonActivity.getWorkflowStatusType().equals("End")) {
						jsonActivity.setProgressIndicator("#5C9BD1");
					} else {
						jsonActivity.setProgressIndicator(progessIndicator);
					}
					jsonActivity.setAttachmentCount(attachmentCount);
					jsonActivity.setVisibleEventComment(visibleEventComment);
					if(modifiedInstanceIds.contains(entityInstanceId)){
						jsonActivity.setUserTagActivity(-1);
					}
					jsonActivity.setLatestComment(lastestComment);
					if(userLogoutDate!= null){
						jsonActivity.setUserTagActivity(DateUtility.compareDateTimeRange(userLogoutDate,jsonActivity.getTempModifiedDate()));
					} else if(userLoginDate != null) {
						jsonActivity.setUserTagActivity(DateUtility.compareDateTimeRange(userLoginDate, jsonActivity.getTempModifiedDate()));
					}else {
						jsonActivity.setUserTagActivity(DateUtility.compareDateTimeRange(new Date(),jsonActivity.getTempModifiedDate()));
					}
					
				}else if(jsonActivityWorkPackage != null){
					jsonActivityWorkPackage.setActors(pendingWith);
					jsonActivityWorkPackage.setRemainingHours(remainingHrsForRAG.intValue());
					jsonActivityWorkPackage.setRemainingHrsMins(remainingHrsMins);
					jsonActivityWorkPackage.setWorkflowIndicator(workflowIndicator);
					jsonActivityWorkPackage.setCompletedBy(completeByFormat);
					jsonActivityWorkPackage.setWorkflowIndicator(workflowIndicator);
					jsonActivityWorkPackage.setWorkflowRAG(plannedDatesRAG);
					jsonActivityWorkPackage.setAttachmentCount(attachmentCount);
					jsonActivityWorkPackage.setVisibleEventComment(visibleEventComment);
				}
			}
			
		}catch(Exception ex){
			log.error("Error in setInstanceIndicators ", ex);
		}
	}
	
	@Override
	public List<?> getPaginationListFromFullList(List<?> instanceObjects, Integer startIndex, Integer maxNumberOfInstance){
		List<Object> returnList = new ArrayList<>();
		try{
			if(startIndex != null && maxNumberOfInstance != null && instanceObjects != null && instanceObjects.size() > maxNumberOfInstance){
				int iterationCount = (startIndex + maxNumberOfInstance);
				int availableSize = instanceObjects.size();
				for(int i = startIndex; i < iterationCount; i++){
					if(i < availableSize){
						returnList.add(instanceObjects.get(i));
					}else{
						break;
					}
				}
			}else{
				returnList.addAll(instanceObjects);
			}
		}catch(Exception ex){
			log.error("Error in getPaginationListFromFullList - ", ex);
		}
		return returnList;
	}
	
	
	@Override
	@Transactional
	public List<Object[]> getWorkflowStatusSummaryCountByProduct(Integer engagementId,Integer productId,Integer entityTypeId,Integer entityParentInstanceId) {
		try {
			return workflowStatusPolicyDAO.getWorkflowStatusSummaryCountByProduct(engagementId,productId,entityTypeId,entityParentInstanceId);
		}catch(Exception e) {
			log.error("Error in getStatusSummaryCountByProduct",e);
		}
		return null;
	}
	
	@Override
	@Transactional
	public List<ActivityTask> getWorkflowSLATaskDetailView(Integer productId,Integer entityTypeId,Integer lifeCycleEntityId,Integer lifeCycleStageId,String workflowType,String indicator,Integer entityParentInstanceId,Integer jtStartIndex,Integer jtPageSize) {
		List<Object[]> slaDetailList=null;
		Integer engagementId=0;
		try {
			if(workflowType.equalsIgnoreCase(IDPAConstants.WORKFLOW_STATUS_TYPE)) {
				slaDetailList= workflowStatusPolicyDAO.getWorkflowSummarySLADetailView(engagementId,productId, entityTypeId, indicator,entityParentInstanceId);
			} if(workflowType.equalsIgnoreCase(IDPAConstants.WORKFLOW_LIFE_CYCLE_TYPE)) {
				slaDetailList= workflowStatusPolicyDAO.getWorkflowLifeCycleSummarySLADetailView(engagementId,productId, entityTypeId,lifeCycleEntityId,lifeCycleStageId, indicator);
			}
			List<Integer> entityInstanceIds = new ArrayList<Integer>();
			if(slaDetailList != null) {
				for(Object[] slqIndicatorDetail:slaDetailList) {
					entityInstanceIds.add((Integer)slqIndicatorDetail[4]);
				}
			}
			
			if(entityInstanceIds != null && entityInstanceIds.size() >0) {
				return workflowTaskDAO.listActivityTasksforWorkFlowSLAIndicator(productId, entityTypeId, entityInstanceIds, jtStartIndex, jtPageSize);
			} 
			
		}catch(Exception e) {
			log.error("Error in getWorkflowSummarySLADetailView", e);
		}
		return null;
	}
	
	
	@Override
	@Transactional
	public List<TestCaseList> getWorkflowSLATestCaseDetailView(Integer productId,Integer entityTypeId,String indicator,Integer jtStartIndex,Integer jtPageSize) {
		Integer entityParentInstanceId=0;
		Integer engagementId=0;
		try {
			List<Object[]> slaDetailList= workflowStatusPolicyDAO.getWorkflowSummarySLADetailView(engagementId,productId, entityTypeId, indicator,entityParentInstanceId);
			List<Integer> entityInstanceIds = new ArrayList<Integer>();
			if(slaDetailList != null) {
				for(Object[] slqIndicatorDetail:slaDetailList) {
					entityInstanceIds.add((Integer)slqIndicatorDetail[4]);
				}
			}
			if(entityInstanceIds != null && entityInstanceIds.size() >0) {
				return workflowTestcasesDAO.listTescaseforWorkFlowSLAIndicator(productId, entityTypeId, entityInstanceIds, jtStartIndex, jtPageSize);
			}
			
		}catch(Exception e) {
			log.error("Error in getWorkflowSummarySLADetailView" , e);
		}
		return null;
	}
	
	@Override
	@Transactional
	public JsonWorkflowSLAInstance setJsonWorkflowSLAInstance(Object instanceObject, String viewType) {
		JsonWorkflowSLAInstance jsonWorkflowSLAInstance= new JsonWorkflowSLAInstance();
		if(instanceObject instanceof JsonActivityTask){
			JsonActivityTask jsonActivityTask = (JsonActivityTask) instanceObject;
			jsonWorkflowSLAInstance.setInstanceId(jsonActivityTask.getActivityTaskId());
			jsonWorkflowSLAInstance.setInstanceName(jsonActivityTask.getActivityTaskName());
			jsonWorkflowSLAInstance.setActors(jsonActivityTask.getActors());
			jsonWorkflowSLAInstance.setCompletedBy(jsonActivityTask.getCompletedBy());
			jsonWorkflowSLAInstance.setRemainingHours(jsonActivityTask.getRemainingHours());
			jsonWorkflowSLAInstance.setTotalEffort(jsonActivityTask.getTotalEffort());
			if(viewType != null && IDPAConstants.WORKFLOW_STATUS_TYPE.equalsIgnoreCase(viewType)){
				jsonWorkflowSLAInstance.setWorkflowIndicator(jsonActivityTask.getWorkflowIndicator());
			}else if(viewType != null && IDPAConstants.WORKFLOW_LIFE_CYCLE_TYPE.equalsIgnoreCase(viewType)){
				jsonWorkflowSLAInstance.setWorkflowIndicator(jsonActivityTask.getWorkflowRAG());
			} 
			jsonWorkflowSLAInstance.setWorkflowStatusId(jsonActivityTask.getStatusId());
			jsonWorkflowSLAInstance.setWorkflowStatusName(jsonActivityTask.getStatusDisplayName());
			jsonWorkflowSLAInstance.setWorkflowStatusCategoryName(jsonActivityTask.getWorkflowStatusCategoryName());
			jsonWorkflowSLAInstance.setRemainingHrsMins(jsonActivityTask.getRemainingHrsMins());
			
			jsonWorkflowSLAInstance.setPlannedSize(jsonActivityTask.getPlannedTaskSize());
			jsonWorkflowSLAInstance.setActualSize(jsonActivityTask.getActualTaskSize());
			jsonWorkflowSLAInstance.setPlannedEffort(jsonActivityTask.getPlannedEffort());
			
		}else if(instanceObject instanceof JsonTestCaseList){
			JsonTestCaseList jsonTestCaseList = (JsonTestCaseList) instanceObject;
			jsonWorkflowSLAInstance.setInstanceId(jsonTestCaseList.getTestCaseId());
			jsonWorkflowSLAInstance.setInstanceName(jsonTestCaseList.getTestCaseName());
			jsonWorkflowSLAInstance.setActors(jsonTestCaseList.getActors());
			jsonWorkflowSLAInstance.setCompletedBy(jsonTestCaseList.getCompletedBy());
			jsonWorkflowSLAInstance.setRemainingHours(jsonTestCaseList.getRemainingHours());
			jsonWorkflowSLAInstance.setTotalEffort(jsonTestCaseList.getTotalEffort());
			if(viewType != null && IDPAConstants.WORKFLOW_STATUS_TYPE.equalsIgnoreCase(viewType)){
				jsonWorkflowSLAInstance.setWorkflowIndicator(jsonTestCaseList.getWorkflowIndicator());
			}
			jsonWorkflowSLAInstance.setWorkflowStatusId(jsonTestCaseList.getWorkflowStatusId());
			jsonWorkflowSLAInstance.setWorkflowStatusName(jsonTestCaseList.getWorkflowStatusDisplayName());
			jsonWorkflowSLAInstance.setWorkflowStatusCategoryName(jsonTestCaseList.getWorkflowStatusCategoryName());
			jsonWorkflowSLAInstance.setRemainingHrsMins(jsonTestCaseList.getRemainingHrsMins());
		}else if(instanceObject instanceof JsonActivity){
			JsonActivity jsonActivity = (JsonActivity) instanceObject;
			jsonWorkflowSLAInstance.setInstanceId(jsonActivity.getActivityId());
			jsonWorkflowSLAInstance.setInstanceName(jsonActivity.getActivityName());
			jsonWorkflowSLAInstance.setActors(jsonActivity.getActors());
			jsonWorkflowSLAInstance.setCompletedBy(jsonActivity.getCompletedBy());
			jsonWorkflowSLAInstance.setRemainingHours(jsonActivity.getRemainingHours());
			jsonWorkflowSLAInstance.setTotalEffort(jsonActivity.getTotalEffort());
			if(viewType != null && IDPAConstants.WORKFLOW_STATUS_TYPE.equalsIgnoreCase(viewType)){
				jsonWorkflowSLAInstance.setWorkflowIndicator(jsonActivity.getWorkflowIndicator());
			}else if(viewType != null && IDPAConstants.WORKFLOW_LIFE_CYCLE_TYPE.equalsIgnoreCase(viewType)){
				jsonWorkflowSLAInstance.setWorkflowIndicator(jsonActivity.getWorkflowRAG());
			} 
			jsonWorkflowSLAInstance.setWorkflowStatusId(jsonActivity.getStatusId());
			jsonWorkflowSLAInstance.setWorkflowStatusName(jsonActivity.getStatusDisplayName());
			jsonWorkflowSLAInstance.setWorkflowStatusCategoryName(jsonActivity.getWorkflowStatusCategoryName());
			jsonWorkflowSLAInstance.setRemainingHrsMins(jsonActivity.getRemainingHrsMins());
			jsonWorkflowSLAInstance.setPlannedSize(jsonActivity.getPlannedActivitySize());
			jsonWorkflowSLAInstance.setActualSize(jsonActivity.getActualActivitySize());
			jsonWorkflowSLAInstance.setPlannedEffort(jsonActivity.getPlannedEffort());
			//jsonWorkflowSLAInstance.setActualEffort(jsonActivity.);
		}else if(instanceObject instanceof JsonActivityWorkPackage){
			JsonActivityWorkPackage jsonActivityWorkPackage = (JsonActivityWorkPackage) instanceObject;
			jsonWorkflowSLAInstance.setInstanceId(jsonActivityWorkPackage.getActivityWorkPackageId());
			jsonWorkflowSLAInstance.setInstanceName(jsonActivityWorkPackage.getActivityWorkPackageName());
			jsonWorkflowSLAInstance.setActors(jsonActivityWorkPackage.getActors());
			jsonWorkflowSLAInstance.setCompletedBy(jsonActivityWorkPackage.getCompletedBy());
			jsonWorkflowSLAInstance.setRemainingHours(jsonActivityWorkPackage.getRemainingHours());
			jsonWorkflowSLAInstance.setTotalEffort(jsonActivityWorkPackage.getTotalEffort());
			if(viewType != null && IDPAConstants.WORKFLOW_STATUS_TYPE.equalsIgnoreCase(viewType)){
				jsonWorkflowSLAInstance.setWorkflowIndicator(jsonActivityWorkPackage.getWorkflowIndicator());
			}else if(viewType != null && IDPAConstants.WORKFLOW_LIFE_CYCLE_TYPE.equalsIgnoreCase(viewType)){
				jsonWorkflowSLAInstance.setWorkflowIndicator(jsonActivityWorkPackage.getWorkflowRAG());
			} 
			jsonWorkflowSLAInstance.setWorkflowStatusId(jsonActivityWorkPackage.getStatusId());
			jsonWorkflowSLAInstance.setWorkflowStatusName(jsonActivityWorkPackage.getStatusDisplayName());
			jsonWorkflowSLAInstance.setWorkflowStatusCategoryName(jsonActivityWorkPackage.getWorkflowStatusCategoryName());
			jsonWorkflowSLAInstance.setRemainingHrsMins(jsonActivityWorkPackage.getRemainingHrsMins());
			
		}
		return jsonWorkflowSLAInstance;
	}
	
	@Override
	@Transactional
	public List<ActivityTask> listWorkflowStatusSLADetailForTask(Integer productId, Integer entityTypeId, Integer entityId,Integer workflowStatusId,Integer userId,Integer roleId,Integer workflowId,Integer jtStartIndex,Integer jtPageSize) {
		
		List<Object[]> slaDetailList=null;
		Integer engagementId=0;
		Integer entityParentInstanceId=0;
		try{
			if(workflowId != null && workflowId >0) {
				slaDetailList= workflowStatusPolicyDAO.getWorkflowTypeSummaryInstances(engagementId,productId, workflowId, entityTypeId,entityParentInstanceId);
			}else {
				slaDetailList= workflowStatusPolicyDAO.listWorkflowStatusSLADetail(engagementId,productId, entityTypeId, entityId, workflowStatusId, userId, roleId,entityParentInstanceId);
			}
			List<Integer> entityInstanceIds = new ArrayList<Integer>();
			if(slaDetailList != null) {
				for(Object taskIds:slaDetailList) {
					entityInstanceIds.add(Integer.parseInt(taskIds.toString()));
				}
			}
			if(entityInstanceIds != null && entityInstanceIds.size() >0) {
				return workflowTaskDAO.listActivityTasksforWorkFlowSLAIndicator(productId, entityTypeId, entityInstanceIds, jtStartIndex, jtPageSize);
			}
		}catch(Exception e){
			log.error("Error in listWorkflowStatusSLADetailForTask ",e);
		}
		return null;
	}
	
	@Override
	@Transactional
	public List<TestCaseList> listWorkflowStatusSLADetailForTestcase(Integer productId, Integer entityTypeId, Integer entityId,Integer workflowStatusId,Integer userId,Integer roleId,Integer workflowId,Integer jtStartIndex,Integer jtPageSize) {
		List<Object[]> slaDetailList=null;
		Integer engagementId=0;
		Integer entityParentInstanceId=0;
		try{
			if(workflowId != null && workflowId >0) {
				slaDetailList= workflowStatusPolicyDAO.getWorkflowTypeSummaryInstances(engagementId,productId, workflowId, entityTypeId,entityParentInstanceId);
			}else {
				slaDetailList= workflowStatusPolicyDAO.listWorkflowStatusSLADetail(engagementId,productId, entityTypeId, entityId, workflowStatusId, userId, roleId,entityParentInstanceId);
			}
			List<Integer> entityInstanceIds = new ArrayList<Integer>();
			if(slaDetailList != null) {
				for(Object testcaseIds:slaDetailList) {
					entityInstanceIds.add(Integer.parseInt(testcaseIds.toString()));
				}
			}
			if(entityInstanceIds != null && entityInstanceIds.size() >0) {
				return workflowTestcasesDAO.listTescaseforWorkFlowSLAIndicator(productId, entityTypeId, entityInstanceIds, jtStartIndex, jtPageSize);
			}
		}catch(Exception e){
			log.error("Error in listWorkflowStatusSLADetailForTestcase ",e);
		}
		return null;
	}
	
	@Override
	@Transactional
	public List<Activity> listWorkflowStatusSLADetailForActivity(Integer engagementId,Integer productId, Integer entityTypeId, Integer entityId,Integer workflowStatusId,Integer userId,Integer roleId,Integer typeId,Integer workflowStatusCategoryId,Integer entityParentInstanceId,Integer jtStartIndex,Integer jtPageSize) {
		List<Object[]> slaDetailList=null;
		try{
			if(typeId != null && typeId >0) {
				slaDetailList= workflowStatusPolicyDAO.getWorkflowTypeSummaryInstances(engagementId,productId, typeId, entityTypeId,entityParentInstanceId);
			} else if(workflowStatusCategoryId != null && workflowStatusCategoryId >0){
				slaDetailList= workflowStatusPolicyDAO.listWorkflowStatusCategorySLADetail(engagementId,productId, entityTypeId, entityId, workflowStatusId, userId, roleId,workflowStatusCategoryId,entityParentInstanceId);
			}else {
				slaDetailList= workflowStatusPolicyDAO.listWorkflowStatusSLADetail(engagementId,productId, entityTypeId, entityId, workflowStatusId, userId, roleId,entityParentInstanceId);
			}
			List<Integer> entityInstanceIds = new ArrayList<Integer>();
			if(slaDetailList != null) {
				for(Object activityIds:slaDetailList) {
					entityInstanceIds.add(Integer.parseInt(activityIds.toString()));
				}
			}
			if(entityInstanceIds != null && entityInstanceIds.size() >0) {
				return workflowActivityDAO.listActivitiesforWorkFlowSLAIndicator(productId, entityTypeId, entityInstanceIds, jtStartIndex, jtPageSize);
			}
		}catch(Exception e){
			log.error("Error in listWorkflowStatusSLADetailForActivity ",e);
		}
		return null;
	}
	
	@Override
	@Transactional
	public List<ActivityWorkPackage> listWorkflowStatusSLADetailForActivityWorkpackage(Integer engagementId,Integer productId, Integer entityTypeId, Integer entityId,Integer workflowStatusId,Integer userId,Integer roleId,Integer workflowId,Integer jtStartIndex,Integer jtPageSize) {
		List<Object[]> slaDetailList=null;
		Integer entityParentInstanceId=0;
		try{
			if(workflowId != null && workflowId >0) {
				slaDetailList= workflowStatusPolicyDAO.getWorkflowTypeSummaryInstances(engagementId,productId, workflowId, entityTypeId,entityParentInstanceId);
			}else {
				slaDetailList= workflowStatusPolicyDAO.listWorkflowStatusSLADetail(engagementId,productId, entityTypeId, entityId, workflowStatusId, userId, roleId,entityParentInstanceId);
			}
			List<Integer> entityInstanceIds = new ArrayList<Integer>();
			if(slaDetailList != null) {
				for(Object taskIds : slaDetailList) {
					entityInstanceIds.add(Integer.parseInt(taskIds.toString()));
				}
			}
			if(entityInstanceIds != null && entityInstanceIds.size() >0) {
				return workflowActivityWorkpackageDAO.listActivityWorkpackageforWorkFlowSLAIndicator(productId, entityTypeId, entityInstanceIds, jtStartIndex, jtPageSize);
			}
		}catch(Exception e){
			log.error("Error in listWorkflowStatusSLADetailForActivityWorkpackage ",e);
		}
		return null;
	}
	
	@Override
	@Transactional
	public List<Activity> getWorkflowSLAActivityDetailView(Integer engagementId,Integer productId,Integer entityTypeId,Integer lifeCycleEntityId,Integer lifeCycleStageId,String workflowType,String indicator,Integer entityParentInstanceId,Integer jtStartIndex,Integer jtPageSize) {
		List<Object[]> slaDetailList=null;
		try {
			if(workflowType.equalsIgnoreCase(IDPAConstants.WORKFLOW_STATUS_TYPE)) {
				slaDetailList= workflowStatusPolicyDAO.getWorkflowSummarySLADetailView(engagementId,productId, entityTypeId, indicator,entityParentInstanceId);
			} if(workflowType.equalsIgnoreCase(IDPAConstants.WORKFLOW_LIFE_CYCLE_TYPE)) {
				slaDetailList= workflowStatusPolicyDAO.getWorkflowLifeCycleSummarySLADetailView(engagementId,productId, entityTypeId,lifeCycleEntityId,lifeCycleStageId,indicator);
			}
			
			List<Integer> entityInstanceIds = new ArrayList<Integer>();
			if(slaDetailList != null) {
				for(Object[] slqIndicatorDetail:slaDetailList) {
					entityInstanceIds.add((Integer)slqIndicatorDetail[4]);
				}
			}
			
			if(entityInstanceIds != null && entityInstanceIds.size() >0) {
				return workflowActivityDAO.listActivitiesforWorkFlowSLAIndicator(productId, entityTypeId, entityInstanceIds, jtStartIndex, jtPageSize);
			} 
			
		}catch(Exception e) {
			log.error("Error in getWorkflowSLAActivityDetailView", e);
		}
		return null;
	}

	@Override
	@Transactional
	public List<ActivityWorkPackage> getWorkflowSLAActivityWorkPackageDetailView(Integer engagementId,Integer productId, Integer entityTypeId, String indicator,Integer entityParentInstanceId,Integer jtStartIndex, Integer jtPageSize) {
		try {
			List<Object[]> slaDetailList= workflowStatusPolicyDAO.getWorkflowSummarySLADetailView(engagementId,productId, entityTypeId, indicator,entityParentInstanceId);
			List<Integer> entityInstanceIds = new ArrayList<Integer>();
			if(slaDetailList != null) {
				for(Object[] slqIndicatorDetail:slaDetailList) {
					entityInstanceIds.add((Integer)slqIndicatorDetail[4]);
				}
			}
			
			if(entityInstanceIds != null && entityInstanceIds.size() >0) {
				return workflowActivityWorkpackageDAO.listActivityWorkpackageforWorkFlowSLAIndicator(productId, entityTypeId, entityInstanceIds, jtStartIndex, jtPageSize);
			} 
			
		}catch(Exception e) {
			log.error("Error in getWorkflowSLAActivityWorkPackageDetailView", e);
		}
		return null;
	}
	
	@Override
	@Transactional
	public List<Object[]> getWorkpackageRAGViewSummaryList(Integer productId){
		try {
			return workflowStatusPolicyDAO.getWorkpackageRAGViewSummaryList(productId);
			
		}catch(Exception e) {
			log.error("Error in Services for getWorkpackageRAGViewSummaryList",e);
		}
		return null;
	}

	@Override
	@Transactional
	public void addWorkflowInstanceLifeCycleStage(Integer productId,  Integer entityTypeId, Integer entityInstanceId, Integer lifeCycleStageId, String instancePlannedStartDate, String instancePlannedEndDate, String createdDate, Integer createdBy) {
		workflowStatusPolicyDAO.addWorkflowInstanceLifeCycleStage(productId,entityTypeId, entityInstanceId, lifeCycleStageId, instancePlannedStartDate, instancePlannedEndDate, createdDate, createdBy);
	}
	
	@Override
	@Transactional
	public List<Object[]> getWorkflowTypeSummaryByEntityType(Integer engagmentId,Integer productId,Integer entityTypeId,Integer entityParentInstanceId) {
		try{
			return workflowStatusPolicyDAO.getWorkflowTypeSummaryByEntityType(engagmentId,productId, entityTypeId,entityParentInstanceId);
		}catch(Exception e) {
			log.error("Error service in getWorkflowTypeSummaryByEntityType",e);
		}
		return null;
	}

	@Override
	@Transactional
	public List<String> getWorkFlowStatuNamebyWorkFlowId(int workflowId) {
		return workflowStatusPolicyDAO.getWorkFlowStatuNamebyWorkFlowId(workflowId);
		
	}

	@Override
	@Transactional
	public List<WorkflowStatus> getWorkFlowStatus(int workflowId) {
		
		return workflowStatusPolicyDAO.getWorkFlowStatus(workflowId);
	}

	@Override
	@Transactional
	public WorkflowStatusPolicy getWorkflowStatusPolicyById(Integer workflowStatusPolicyId) {
		return workflowStatusPolicyDAO.getWorkflowStatusPolicyById(workflowStatusPolicyId);
	}

	@Override
	@Transactional
	public void deleteWorkflowStatusAndPolicy(Integer workflowStatusId) {
		try{
			workflowStatusPolicyDAO.deleteWorkflowStatusAndPolicy(workflowStatusId);
		}catch(Exception ex){
			log.error("Error in deleteWorkflowStatusAndPolicy - ", ex);
		}
	}

	@Override
	@Transactional
	public boolean isWorkflowStatusInLifeCycleStage(Integer workflowStatusId) {
		return workflowStatusPolicyDAO.isWorkflowStatusInLifeCycleStage(workflowStatusId);
	}
	
	
	@Override
	@Transactional
	public List<Object[]> getWorkflowRAGSummaryCountByProductAndResouces(Integer engagementId,Integer productId,Integer entityTypeId,Integer entityParentInstanceId){
		try {
		return workflowStatusPolicyDAO.getWorkflowRAGSummaryCountByProductAndResouces(engagementId,productId, entityTypeId,entityParentInstanceId);
		} catch(Exception e) {
			log.error("Error service in getWorkflowRAGSummaryCountByProductAndResouces",e);
		}
		return null;
	}
	
		@Override
	@Transactional
	public List<Object[]> getWorkflowRAGSummaryCountByProductAndEntityType(Integer engagementId,Integer productId,Integer entityTypeId,Integer entityParentInstanceId){
		try {
		return workflowStatusPolicyDAO.getWorkflowRAGSummaryCountByProductAndEntityType(engagementId,productId, entityTypeId,entityParentInstanceId);
		} catch(Exception e) {
			log.error("Error service in getWorkflowRAGSummaryCountByProductAndEntityType",e);
		}
		return null;
	}
	
	
	@Override
	@Transactional
	public List<Object[]> getWorkflowRAGActivityGroupingSummaryCount(Integer engagementId,Integer productId,Integer entityTypeId,Integer parentEntityInstanceId){
		try {
		return workflowStatusPolicyDAO.getWorkflowRAGActivityGroupingSummaryCount(engagementId,productId, entityTypeId,parentEntityInstanceId);
		} catch(Exception e) {
			log.error("Error service in getWorkflowRAGBuildSummaryCount",e);
		}
		return null;
	}
	
	@Override
	@Transactional
	public List<Object[]> getWorkflowRAGWorkpackageSummaryCount(Integer productId,Integer entityTypeId,Integer entityParentInstanceId){
		try {
		return workflowStatusPolicyDAO.getWorkflowRAGWorkpackageSummaryCount(productId, entityTypeId,entityParentInstanceId);
		} catch(Exception e) {
			log.error("Error service in getWorkflowRAGWorkpackageSummaryCount",e);
		}
		return null;
	}
	
	@Override
	@Transactional
	public List<Object[]> getWorkflowRAGActivitySummaryCount(Integer productId,Integer entityTypeId){
		try {
		return workflowStatusPolicyDAO.getWorkflowRAGActivitySummaryCount(productId, entityTypeId);
		} catch(Exception e) {
			log.error("Error service in getWorkflowRAGActivitySummaryCount",e);
		}
		return null;
	}
	
	@Override
	@Transactional
	public List<Object[]> getWorkflowRAGSummarySLADetailView(Integer productId,Integer entityTypeId,String indicator){
		//TODO	
		Integer engagementId=0;
		Integer categoryId=0;
		try {
		return workflowStatusPolicyDAO.getWorkflowRAGSummarySLADetailView(engagementId,productId, entityTypeId,indicator,categoryId);
		} catch(Exception e) {
			log.error("Error service in getWorkflowRAGSummarySLADetailView",e);
		}
		return null;
	}
	
	
	@Override
	@Transactional
	public List<ActivityTask> getWorkflowSLATaskDetailRAGView(Integer engagementId,Integer productId,Integer entityTypeId,String indicator,Integer jtStartIndex,Integer jtPageSize) {
		List<Object[]> slaDetailList=null;
		Integer categoryId=0;
		try {
			slaDetailList= workflowStatusPolicyDAO.getWorkflowRAGSummarySLADetailView(engagementId,productId, entityTypeId, indicator,categoryId);
				
			List<Integer> entityInstanceIds = new ArrayList<Integer>();
			if(slaDetailList != null) {
				
				for(Object[] slqIndicatorDetail:slaDetailList) {
					String dbIndicator = (String)slqIndicatorDetail[8];
					if(dbIndicator.equals(indicator)){
					entityInstanceIds.add((Integer)slqIndicatorDetail[4]);
					}
				}
			}
			
			if(entityInstanceIds != null && entityInstanceIds.size() >0) {
				return workflowTaskDAO.listActivityTasksforWorkFlowSLAIndicator(productId, entityTypeId, entityInstanceIds, jtStartIndex, jtPageSize);
			} 
			
		}catch(Exception e) {
			log.error("Error in getWorkflowSLATaskDetailRAGView", e);
		}
		return null;
	}
	
	@Override
	@Transactional
	public List<ActivityWorkPackage> getWorkflowSLAWorkpackageDetailRAGView(Integer engagementId,Integer productId,Integer entityTypeId,String indicator,Integer jtStartIndex,Integer jtPageSize) {
		List<Object[]> slaDetailList=null;
		Integer categoryId=0;
		try {
				slaDetailList= workflowStatusPolicyDAO.getWorkflowRAGSummarySLADetailView(engagementId,productId, entityTypeId, indicator,categoryId);
			
			List<Integer> entityInstanceIds = new ArrayList<Integer>();
			if(slaDetailList != null) {
				for(Object[] slqIndicatorDetail:slaDetailList) {
					String dbIndicator = (String)slqIndicatorDetail[8];
					if(dbIndicator.equals(indicator)){
						entityInstanceIds.add((Integer)slqIndicatorDetail[9]);
					}else if(indicator.equals(IDPAConstants.RAG_STATUS_VIEW_COMPLETED_INDICATOR)) {
						entityInstanceIds.add((Integer)slqIndicatorDetail[9]);
					} else if(indicator.equals(IDPAConstants.RAG_STATUS_VIEW_ABORT_INDICATOR)) {
						entityInstanceIds.add((Integer)slqIndicatorDetail[9]);
					} else {
						entityInstanceIds.add((Integer)slqIndicatorDetail[9]);
					}
					
				}
			}
			
			if(entityInstanceIds != null && entityInstanceIds.size() >0) {
				return workflowActivityWorkpackageDAO.listActivityWorkpackageforWorkFlowSLAIndicator(productId, entityTypeId, entityInstanceIds, jtStartIndex, jtPageSize);
			} 
			
		}catch(Exception e) {
			log.error("Error in getWorkflowSLAWorkpackageDetailRAGView", e);
		}
		return null;
	}
	
	
	@Override
	@Transactional
	public List<Activity> getWorkflowSLAActivityDetailRAGView(Integer engagementId,Integer productId,Integer entityTypeId,String indicator,Integer categoryId,Integer jtStartIndex,Integer jtPageSize) {
		List<Object[]> slaDetailList=null;
		try {
			List<Integer> entityInstanceIds = new ArrayList<Integer>();
				if(indicator.equalsIgnoreCase(IDPAConstants.RAG_STATUS_VIEW_COMPLETED_INDICATOR)) {
					slaDetailList= workflowStatusPolicyDAO.getWorkflowRAGSummarySLADetailCompletedStageView(engagementId,productId, entityTypeId, indicator,categoryId);
					if(slaDetailList != null) {
						for(Object[] slqIndicatorDetail:slaDetailList) {
							String dbIndicator = (String)slqIndicatorDetail[2];
							if(dbIndicator.equalsIgnoreCase(indicator)){
								entityInstanceIds.add((Integer)slqIndicatorDetail[3]);
							}
						}
					}
				}else if(indicator.equalsIgnoreCase(IDPAConstants.RAG_STATUS_VIEW_ABORT_INDICATOR)) {
					slaDetailList= workflowStatusPolicyDAO.getWorkflowRAGSummarySLADetailAbortStageView(engagementId,productId, entityTypeId, indicator,categoryId);
					if(slaDetailList != null) {
						for(Object[] slqIndicatorDetail:slaDetailList) {
							String dbIndicator = (String)slqIndicatorDetail[2];
							if(dbIndicator.equalsIgnoreCase(indicator)){
								entityInstanceIds.add((Integer)slqIndicatorDetail[3]);
							}
						}
					}
				} else {
					slaDetailList= workflowStatusPolicyDAO.getWorkflowRAGSummarySLADetailView(engagementId,productId, entityTypeId, indicator,categoryId);
					if(slaDetailList != null) {
						for(Object[] slqIndicatorDetail:slaDetailList) {
							String dbIndicator = (String)slqIndicatorDetail[8];
							if(dbIndicator.equalsIgnoreCase(indicator)){
								entityInstanceIds.add((Integer)slqIndicatorDetail[9]);
							}
							
							if(indicator.equalsIgnoreCase(IDPAConstants.RAG_STATUS_VIEW_TOTAL_INDICATOR)){
								entityInstanceIds.add((Integer)slqIndicatorDetail[9]);
							}
						}
					}
				}
			
			
			
			if(entityInstanceIds != null && entityInstanceIds.size() >0) {
				return workflowActivityDAO.listActivitiesforWorkFlowSLAIndicator(productId, entityTypeId, entityInstanceIds, jtStartIndex, jtPageSize);
			} 
			
		}catch(Exception e) {
			log.error("Error in getWorkflowSLAActivityDetailRAGView", e);
		}
		return null;
	}
	
	
	@Override
	@Transactional
	public List<ActivityTask> listWorkflowRAGStatusDetailForTask(Integer productId, Integer entityTypeId, Integer entityId,Integer parentInstanceId,Integer userId,Integer roleId,String indicator,Integer jtStartIndex,Integer jtPageSize) {
		//TODO
		Integer engagementId=0;
		List<Object[]> slaDetailList=null;
		Integer categoryId=0;
		try{
			if(userId >0 || roleId >0) {
				slaDetailList= workflowStatusPolicyDAO.getWorkflowRAGByProductAndResoucesDetail(engagementId,productId, entityTypeId, userId, roleId,parentInstanceId, indicator);
			} else {
				slaDetailList= workflowStatusPolicyDAO.listWorkflowRAGStatusInstanceDetail(engagementId,productId, entityTypeId,entityId,parentInstanceId,indicator,categoryId);
			}
			List<Integer> entityInstanceIds = new ArrayList<Integer>();
			if(slaDetailList != null && indicator.equals(IDPAConstants.RAG_STATUS_VIEW_COMPLETED_INDICATOR) || indicator.equals(IDPAConstants.RAG_STATUS_VIEW_ABORT_INDICATOR) ||indicator.equals(IDPAConstants.RAG_STATUS_VIEW_TOTAL_INDICATOR) ) {
				for(Object[] slqIndicatorDetail:slaDetailList) {
					entityInstanceIds.add((Integer)slqIndicatorDetail[0]);
				}
			} else {
				for(Object[] slqIndicatorDetail:slaDetailList) {
					String dbIndicator = (String)slqIndicatorDetail[7];
					if(dbIndicator.equals(indicator)){
						entityInstanceIds.add((Integer)slqIndicatorDetail[0]);
					}
				}
			}
			if(entityInstanceIds != null && entityInstanceIds.size() >0) {
				return workflowTaskDAO.listActivityTasksforWorkFlowSLAIndicator(productId, entityTypeId, entityInstanceIds, jtStartIndex, jtPageSize);
			}
		}catch(Exception e){
			log.error("Error in listWorkflowRAGStatusInstanceDetailForTask ",e);
		}
		return null;
	}
	
	
	@Override
	@Transactional
	public List<Activity> listWorkflowRAGStatusDetailForActivity(Integer engagementId,Integer productId, Integer entityTypeId, Integer entityId,Integer parentInstanceId,Integer userId,Integer roleId,String indicator,Integer categoryId,Integer jtStartIndex,Integer jtPageSize) {
		List<Object[]> slaDetailList=null;
		//TODO
		try{
			
			if(userId >0 || roleId >0) {
				slaDetailList= workflowStatusPolicyDAO.getWorkflowRAGByProductAndResoucesDetail(engagementId,productId, entityTypeId, userId, roleId,parentInstanceId, indicator);
			} else {
				slaDetailList= workflowStatusPolicyDAO.listWorkflowRAGStatusInstanceDetail(engagementId,productId, entityTypeId,entityId,parentInstanceId,indicator,categoryId);
			}
			List<Integer> entityInstanceIds = new ArrayList<Integer>();
			if(slaDetailList != null && indicator.equals(IDPAConstants.RAG_STATUS_VIEW_COMPLETED_INDICATOR) || indicator.equals(IDPAConstants.RAG_STATUS_VIEW_ABORT_INDICATOR) || indicator.equals(IDPAConstants.RAG_STATUS_VIEW_TOTAL_INDICATOR) ) {
				for(Object[] slqIndicatorDetail:slaDetailList) {
					entityInstanceIds.add((Integer)slqIndicatorDetail[0]);
				}
			} else {
				for(Object[] slqIndicatorDetail:slaDetailList) {
					String dbIndicator = (String)slqIndicatorDetail[7];
					if(dbIndicator.equals(indicator)){
						entityInstanceIds.add((Integer)slqIndicatorDetail[0]);
					}
				}
			}
			if(entityInstanceIds != null && entityInstanceIds.size() >0) {
				return workflowActivityDAO.listActivitiesforWorkFlowSLAIndicator(productId, entityTypeId, entityInstanceIds, jtStartIndex, jtPageSize);
			}
		}catch(Exception e){
			log.error("Error in listWorkflowRAGStatusDetailForActivity ",e);
		}
		return null;
	}
	
	
	@Override
	@Transactional
	public List<ActivityWorkPackage> listWorkflowRAGStatusDetailForActivityWorkpackage(Integer engagementId,Integer productId, Integer entityTypeId, Integer entityId,Integer parentInstanceId,Integer userId,Integer roleId,String indicator,Integer jtStartIndex,Integer jtPageSize) {
		List<Object[]> slaDetailList=null;
		Integer categoryId=0;
		try{
			if(userId >0 || roleId >0) {
				slaDetailList= workflowStatusPolicyDAO.getWorkflowRAGByProductAndResoucesDetail(engagementId,productId, entityTypeId, userId, roleId,parentInstanceId, indicator);
			} else {
				slaDetailList= workflowStatusPolicyDAO.listWorkflowRAGStatusInstanceDetail(engagementId,productId, entityTypeId,entityId,parentInstanceId,indicator,categoryId);
			}
			List<Integer> entityInstanceIds = new ArrayList<Integer>();
				if(slaDetailList != null && indicator.equals(IDPAConstants.RAG_STATUS_VIEW_COMPLETED_INDICATOR) || indicator.equals(IDPAConstants.RAG_STATUS_VIEW_ABORT_INDICATOR) || indicator.equals(IDPAConstants.RAG_STATUS_VIEW_ABORT_INDICATOR) ) {
					for(Object[] slqIndicatorDetail:slaDetailList) {
						entityInstanceIds.add((Integer)slqIndicatorDetail[0]);
					}
				} else {
					for(Object[] slqIndicatorDetail:slaDetailList) {
						String dbIndicator = (String)slqIndicatorDetail[7];
						if(dbIndicator.equals(indicator)){
							entityInstanceIds.add((Integer)slqIndicatorDetail[0]);
						}
					}
				}
			if(entityInstanceIds != null && entityInstanceIds.size() >0) {
				return workflowActivityWorkpackageDAO.listActivityWorkpackageforWorkFlowSLAIndicator(productId, entityTypeId, entityInstanceIds, jtStartIndex, jtPageSize);
			}
		}catch(Exception e){
			log.error("Error in listWorkflowStatusSLADetailForActivityWorkpackage ",e);
		}
		return null;
	}
	
	@Override
	@Transactional
	public void updateWorkflowInstanceLifeCycleStage(Integer productId,Integer entityTypeId, Integer entityInstanceId, Integer lifeCycleStageId, String instancePlannedStartDate, String instancePlannedEndDate, String createdDate, Integer createdBy) {
		try {
			workflowStatusPolicyDAO.updateWorkflowInstanceLifeCycleStage(productId, entityTypeId, entityInstanceId, lifeCycleStageId, instancePlannedStartDate, instancePlannedEndDate, createdDate, createdBy);
		}catch(Exception e) {
			log.error("Error in updateWorkflowInstanceLifeCycleStage ",e);
		}
	}
	
	@Override
	@Transactional
	public List<Object[]> getWorkflowRAGSummaryCompleteCountByProductAndEntityType(Integer engagementId,Integer productId,Integer entityTypeId,Integer entityParentInstanceId) {
		try {
			return workflowStatusPolicyDAO.getWorkflowRAGSummaryCompleteCountByProductAndEntityType(engagementId,productId, entityTypeId,entityParentInstanceId);
		}catch(Exception e) {
			log.error("Error in getWorkflowRAGSummaryCompleteCountByProductAndEntityType ",e);
		}
		return null;
	}
	
	@Override
	@Transactional
	public List<Object[]> getWorkflowRAGSummaryAbortCountByProductAndEntityType(Integer engagementId,Integer productId,Integer entityTypeId,Integer entityParentInstanceId) {
		
		try {
			return workflowStatusPolicyDAO.getWorkflowRAGSummaryAbortCountByProductAndEntityType(engagementId,productId, entityTypeId,entityParentInstanceId);
		}catch(Exception e) {
			log.error("Error in getWorkflowRAGSummaryAbortCountByProductAndEntityType ",e);
		}
		return null;
	}
	
	private String setRAGDetailViewIndicator(Date planStartDate,Date planEndDate,Float instancePercentageComplition) {
		Integer remainghrs=null;
		Integer totalHours=null;
		Integer remainingComplition=null;
		Integer remainingHrsPercentage=null;
		Date startDate=new Date();
		String ragViewindicator="<i class='fa fa-square' style='color: red;' title='Delayed'></i>";
		try {
			if(planEndDate != null){
				Long timeDifferenceInMilliSecond = 0L;
				if( startDate.getTime()> planEndDate.getTime()){
					timeDifferenceInMilliSecond = startDate.getTime() - planEndDate.getTime();
				}else{
					timeDifferenceInMilliSecond = planEndDate.getTime() - startDate.getTime();
				}
				remainghrs = (int) (timeDifferenceInMilliSecond / (60 * 60 * 1000));
			}
			
				if(planStartDate != null && planEndDate != null){
					Long timeDifferenceInMilliSecond = 0L;
					if(planStartDate.getTime() > planEndDate.getTime()){
						timeDifferenceInMilliSecond = planStartDate.getTime() - planEndDate.getTime();
					}else{
						timeDifferenceInMilliSecond = planEndDate.getTime() - planStartDate.getTime();
					}
					totalHours = (int) (timeDifferenceInMilliSecond / (60 * 60 * 1000));
				}
			
			
			if(totalHours != null && totalHours == 0) {
				totalHours=24;
			}
			
			remainingComplition = 100- instancePercentageComplition.intValue();
			
			remainingHrsPercentage = (remainghrs/totalHours*100);
			if(planEndDate != null && planEndDate.getTime() < startDate.getTime()) {
				ragViewindicator="<i class='fa fa-square' style='color: red;' title='Delayed'></i>";
			} else {
				if(remainingComplition >= remainingHrsPercentage) {
					ragViewindicator="<i class='fa fa-square' style='color: green;' title='In Progress'></i>";
				}else if((remainingHrsPercentage <remainingComplition)&&((remainingComplition -remainingHrsPercentage ) <50)) {
					ragViewindicator="<i class='fa fa-square' style='color: orange;' title='Nearing End date'></i>";
				} else {
					ragViewindicator="<i class='fa fa-square' style='color: red;' title='Delayed'></i>";
				}
			}
		} catch(Exception e) {
			
		}
		return ragViewindicator;
	}
	
	
	@Override
	@Transactional
	public List<Object[]> getRAGviewWorkpackageSummaryCompletedCount(Integer productId,Integer entityTypeId,Integer entityParentInstanceId) {
		try {
			return workflowStatusPolicyDAO.getRAGviewWorkpackageSummaryCompletedCount(productId, entityTypeId,entityParentInstanceId);
		}catch(Exception e) {
			log.error("Error in geRAGviewWorkpackageSummaryCompletedCount ",e);
		}
		return null;
	}
	
	@Override
	@Transactional
	public List<Object[]> getRAGviewWorkpackageSummaryAbortedCount(Integer productId,Integer entityTypeId,Integer entityParentInstanceId) {
		try {
			return workflowStatusPolicyDAO.getRAGviewWorkpackageSummaryAbortedCount(productId, entityTypeId,entityParentInstanceId);
		}catch(Exception e) {
			log.error("Error in getRAGviewWorkpackageSummaryAbortedCount ",e);
		}
		return null;
	}
	
	@Override
	@Transactional
	public List<Object[]> getRAGSummaryCompletedCountByProductAndResouces(Integer engagementId,Integer productId,Integer entityTypeId,Integer entityParentInstanceId) {
		try {
			return workflowStatusPolicyDAO.getRAGSummaryCompletedCountByProductAndResouces(engagementId,productId, entityTypeId,entityParentInstanceId);
		}catch(Exception e) {
			log.error("Error in getRAGSummaryCompletedCountByProductAndResouces ",e);
		}
		return null;
	}
	
	
	@Override
	@Transactional
	public List<Object[]> getRAGSummaryAbortedCountByProductAndResouces(Integer engagementId,Integer productId,Integer entityTypeId,Integer entityParentInstanceId) {
		try {
			return workflowStatusPolicyDAO.getRAGSummaryAbortedCountByProductAndResouces(engagementId,productId, entityTypeId,entityParentInstanceId);
		}catch(Exception e) {
			log.error("Error in getRAGSummaryAbortedCountByProductAndResouces ",e);
		}
		return null;
	}
	
	@Override
	@Transactional
	public List<Object[]> getRAGViewActivityGroupingSummaryCompletedCount(Integer engagementId,Integer productId,Integer entityTypeId,Integer entityParentInstanceId) {
		try {
			return workflowStatusPolicyDAO.getRAGViewActivityGroupingSummaryCompletedCount(engagementId,productId, entityTypeId,entityParentInstanceId);
		}catch(Exception e) {
			log.error("Error in getRAGViewActivityGroupingSummaryCompletedCount ",e);
		}
		return null;
	}
	
	@Override
	@Transactional
	public List<Object[]> getRAGViewActivityGroupingSummaryAbortCount(Integer engagementId,Integer productId,Integer entityTypeId,Integer entityParentInstanceId) {
		try {
			return workflowStatusPolicyDAO.getRAGViewActivityGroupingSummaryAbortCount(engagementId,productId, entityTypeId,entityParentInstanceId);
		}catch(Exception e) {
			log.error("Error in getRAGViewActivityGroupingSummaryAbortCount ",e);
		}
		return null;
	}
	
	@Override
	@Transactional
	public List<Object[]> listWorkflowStatusCategorySummaryCount(Integer engagmentId,Integer productId, Integer entityTypeId, Integer entityId, Integer entityInstanceId) {
		try {
			return workflowStatusPolicyDAO.listWorkflowStatusCategorySummaryCount(engagmentId,productId, entityTypeId, entityId, entityInstanceId);
		}catch(Exception e) {
			log.error("Error in listWorkflowStatusCategorySummaryCount ",e);
		}
		return null;
	}
	
	@Override
	@Transactional
	public List<Object[]> getWorkflowRAGEngagementSummaryCount(Integer engagementId,Integer productId,Integer entityTypeId,Integer entityParentInstanceId) {
		try {
			return workflowStatusPolicyDAO.getWorkflowRAGEngagementSummaryCount(engagementId,productId, entityTypeId,entityParentInstanceId);
		}catch(Exception e) {
			log.error("Error in getWorkflowRAGEngagementSummaryCount ",e);
		}
		return null;
	}
	
	@Override
	@Transactional
	public List<Object[]> getRAGViewEngagementSummaryCompletedCount(Integer engagementId,Integer productId,Integer entityTypeId,Integer entityParentInstanceId) {
		try {
			return workflowStatusPolicyDAO.getRAGViewEngagementSummaryCompletedCount(engagementId,productId, entityTypeId,entityParentInstanceId);
		}catch(Exception e) {
			log.error("Error in getRAGViewEngagementSummaryCompletedCount ",e);
		}
		return null;
	}
	
	@Override
	@Transactional
	public List<Object[]> getRAGViewEngagementSummaryAbortCount(Integer engagementId,Integer productId,Integer entityTypeId,Integer entityParentInstanceId) {
		try {
			return workflowStatusPolicyDAO.getRAGViewEngagementSummaryAbortCount(engagementId,productId, entityTypeId,entityParentInstanceId);
		}catch(Exception e) {
			log.error("Error in getRAGViewEngagementSummaryAbortCount ",e);
		}
		return null;
	}
	
	
	
	@Override
	@Transactional
	public List<Object[]> getWorkflowRAGActivityGroupingSummaryTotalCount(Integer engagementId,Integer productId,Integer entityTypeId,Integer parentEntityInstanceId) {
		try {
			return workflowStatusPolicyDAO.getWorkflowRAGActivityGroupingSummaryTotalCount(engagementId,productId, entityTypeId,parentEntityInstanceId);
		}catch(Exception e) {
			log.error("Error in getWorkflowRAGActivityGroupingSummaryTotalCount ",e);
		}
		return null;
	}
	
	@Override
	@Transactional
	public List<Object[]> getWorkflowRAGActivityCategoriesSummaryCount(Integer engagementId,Integer productId, Integer entityTypeId,Integer entityParentInstanceId){
		try {
			return workflowStatusPolicyDAO.getWorkflowRAGActivityCategoriesSummaryCount(engagementId, productId, entityTypeId, entityParentInstanceId);
		}catch(Exception e) {
			log.error("Error Service in getWorkflowRAGActivityCategoriesSummaryCount ",e);
		}
		return null;
	}

	
	@Override
	@Transactional
	public List<Object[]> getWorkflowRAGActivityCategoriesSummaryAbortCount(Integer engagementId,Integer productId, Integer entityTypeId,Integer entityParentInstanceId){
		try {
			return workflowStatusPolicyDAO.getWorkflowRAGActivityCategoriesSummaryAbortCount(engagementId, productId, entityTypeId, entityParentInstanceId);
		}catch(Exception e) {
			log.error("Error Service in getWorkflowRAGActivityCategoriesSummaryAbortCount ",e);
		}
		return null;
	}
	
	@Override
	@Transactional
	public List<Object[]> getWorkflowRAGActivityCategoriesSummaryCompletedCount(Integer engagementId,Integer productId, Integer entityTypeId,Integer entityParentInstanceId){
		try {
			return workflowStatusPolicyDAO.getWorkflowRAGActivityCategoriesSummaryCompletedCount(engagementId, productId, entityTypeId, entityParentInstanceId);
		}catch(Exception e) {
			log.error("Error Service in getWorkflowRAGActivityCategoriesSummaryCompletedCount ",e);
		}
		return null;
	}

	@Override
	@Transactional
	public List<Activity> listActivitiesforWorkFlowStatusPartForUserOrRole(Integer productId, Integer entityTypeId, Integer userId,Integer roleId) {
		Integer activityWorkPackageId=0;
		Integer testFactoryId=0;
		try {
			return workflowActivityDAO.listActivitiesforWorkFlowStatusPartForUserOrRole(testFactoryId,productId, activityWorkPackageId,entityTypeId, userId, roleId);
		}catch(Exception e) {
			log.error("Error Service in listActivitiesforWorkFlowStatusPartForUserOrRole ",e);
		}
		return null;
	}
	
	@Override
	@Transactional
	public List<Activity> listActivitiesforProductIdAndActivityIds(Integer productId, Integer entityTypeId, List<Integer> entityInstanceIds,Integer jtStartIndex,Integer jtPageSize) {
		try {
			return workflowActivityDAO.listActivitiesforWorkFlowSLAIndicator(productId, entityTypeId, entityInstanceIds, jtStartIndex, jtPageSize);
		}catch(Exception e) {
			log.error("Error Service in listActivitiesforProductIdAndActivityIds ",e);
		}
		return null;
	}

	@Override
	public void removeInstanceFromEntityWorkflowStatusActorsMapping(Integer entityTypeId, Integer entityId, Integer entityInstanceId) {
		try{
			workflowActivityDAO.removeInstanceFromEntityWorkflowStatusActorsMapping(entityTypeId,entityId,entityInstanceId);
		}catch(Exception e) {
			log.error("Error Service in removeInstanceFromEntityWorkflowStatusActorsMapping ",e);
		}
		
	}
}
