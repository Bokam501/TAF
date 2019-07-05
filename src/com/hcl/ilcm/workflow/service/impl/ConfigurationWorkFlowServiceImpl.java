/**
 * 
 */
package com.hcl.ilcm.workflow.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.kie.api.runtime.manager.RuntimeManager;
import org.kie.api.runtime.process.WorkItemHandler;
import org.kie.internal.runtime.manager.cdi.qualifier.PerProcessInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hcl.atf.taf.constants.IDPAConstants;
import com.hcl.atf.taf.dao.ActivityTypeDAO;
import com.hcl.atf.taf.model.Activity;
import com.hcl.atf.taf.model.ActivityTask;
import com.hcl.atf.taf.model.ActivityWorkPackage;
import com.hcl.atf.taf.model.ProductFeature;
import com.hcl.atf.taf.model.TestCaseList;
import com.hcl.atf.taf.model.UserList;
import com.hcl.atf.taf.model.json.jtable.JTableSingleResponse;
import com.hcl.atf.taf.service.ActivityService;
import com.hcl.atf.taf.service.ActivityTaskService;
import com.hcl.atf.taf.service.ActivityTaskTypeService;
import com.hcl.atf.taf.service.ActivityWorkPackageService;
import com.hcl.atf.taf.service.ProductListService;
import com.hcl.atf.taf.service.TestCaseService;
import com.hcl.ilcm.workflow.dao.WorkflowActivityDAO;
import com.hcl.ilcm.workflow.dao.WorkflowActivityWorkpackageDAO;
import com.hcl.ilcm.workflow.dao.WorkflowMasterDAO;
import com.hcl.ilcm.workflow.dao.WorkflowTaskDAO;
import com.hcl.ilcm.workflow.dao.WorkflowTestcasesDAO;
import com.hcl.ilcm.workflow.model.WorkflowMasterEntityMapping;
import com.hcl.ilcm.workflow.model.WorkflowStatus;
import com.hcl.ilcm.workflow.model.WorkflowStatusPolicy;
import com.hcl.ilcm.workflow.service.ConfigurationWorkFlowService;
import com.hcl.ilcm.workflow.service.WorkflowEventService;
import com.hcl.ilcm.workflow.service.WorkflowMasterService;
import com.hcl.ilcm.workflow.service.WorkflowStatusPolicyService;
import com.hcl.ilcm.workflow.service.WorkflowStatusService;

/**
 * @author silambarasur
 *
 */
@Service
public class ConfigurationWorkFlowServiceImpl implements ConfigurationWorkFlowService {
	
	
	private static final Log log = LogFactory
			.getLog(ConfigurationWorkFlowServiceImpl.class);
	
	@PerProcessInstance 
	private RuntimeManager singletonManager;  
	
	@PerProcessInstance
	private WorkItemHandler workItemHandler;
	@Autowired
	private WorkflowTaskDAO workflowTaskDAO;
	
	@Autowired
	private WorkflowTestcasesDAO workflowTestcasesDAO;
	
	@Autowired
	private WorkflowActivityDAO workflowActivityDAO;
	
	@Autowired
	private WorkflowMasterService workflowMasterService;
	
	@Autowired
	private ActivityTaskTypeService activityTaskTypeService;

	@Autowired
	private WorkflowMasterDAO workflowMasterDAO;
	
	@Autowired
	private WorkflowEventService workflowEventService;
	
	@Autowired
	private WorkflowStatusPolicyService workflowStatusPolicyService;
	
	@Autowired
	private ActivityWorkPackageService activityWorkPackageService;
	
	@Autowired
	private ActivityService activityService;
	
	@Autowired
	private ActivityTaskService activityTaskService;
	
	@Autowired
	private ActivityTypeDAO activityTypeDAO;
	
	@Autowired
	private TestCaseService testCaseService;
	
	@Autowired
	private WorkflowStatusService workflowStatusService;
	
	@Autowired
	private ProductListService productListService;
	
	@Autowired
	private WorkflowActivityWorkpackageDAO workflowActivityWorkpackageDAO;
	
	@Override
	@Transactional
	public List<ActivityTask> listActivityTasksForUser(Integer productId, String entityLevel, Integer entityLevelId, Integer userId, Integer roleId, Integer jtStartIndex, Integer jtPageSize){
		try {
			List<ActivityTask> userTasks = new ArrayList<ActivityTask>();
			List<ActivityTask> activityTypeTasks = workflowTaskDAO.listActivityTasksforWorkFlowForUserOrRole(productId, null, IDPAConstants.ENTITY_TASK_TYPE, null, userId, roleId, jtStartIndex, jtPageSize);
		    if (activityTypeTasks != null) {
		    	userTasks.addAll(activityTypeTasks);
			}
			return userTasks;
		} catch (Throwable t) {
			log.error(t);
		} 
		return null;
	}

	@Override
	@Transactional
	public List<TestCaseList> listTestcasesForUser(Integer productId, String entityLevel, Integer entityLevelId, Integer userId, Integer roleId,Integer jtStartIndex, Integer jtPageSize ){
		try {
			List<TestCaseList> userTestcases = new ArrayList<TestCaseList>();
			userTestcases=workflowTestcasesDAO.listActivityTestcasesforWorkFlowForUserOrRole(productId, null, IDPAConstants.ENTITY_TEST_CASE_ID, null, userId, roleId, jtStartIndex, jtPageSize);
			return userTestcases;
		} catch (Throwable t) {
			log.error(t);
		} 
		return null;
	}
	
	@Override
	public List<WorkflowMasterEntityMapping> getWorkflowEntitiesForProduct(int productId) {
		return workflowMasterDAO.getWorkflowEntitiesForProduct(productId);
	}

	@Override
	@Transactional
	public void addWorkflowProductEntityMapping(WorkflowMasterEntityMapping workflowMasterEntityMapping) {
		workflowMasterDAO.addWorkflowProductEntityMapping(workflowMasterEntityMapping);		
	}

	@Override
	@Transactional
	public boolean checkWorkflowEntityMappingAlreadyExist(Integer workflowEntityMappingId, Integer productId, Integer workflowId, Integer entityTypeId, Integer entityId) {
		if(entityTypeId != null && entityTypeId == 0){
			entityTypeId = null;
		}
		if(entityId != null && entityId == 0){
			entityId = null;
		}
		return workflowMasterDAO.checkWorkflowEntityMappingAlreadyExist(workflowEntityMappingId, productId, workflowId, entityTypeId, entityId);
	}

	@Override
	@Transactional
	public boolean checkEligibilityForDefault(Integer workflowEntityMappingId, Integer productId, Integer entityTypeId, Integer entityId) {
		if(entityTypeId != null && entityTypeId == 0){
			entityTypeId = null;
		}
		if(entityId != null && entityId == 0){
			entityId = null;
		}
		return workflowMasterDAO.checkEligibilityForDefault(workflowEntityMappingId, productId, entityTypeId, entityId);
	}

	@Override
	@Transactional
	public void updateIsDefaultOfEntityMapping(Integer workflowEntityMappingId, Integer productId, Integer entityTypeId, Integer entityId) {
		if(entityTypeId != null && entityTypeId == 0){
			entityTypeId = null;
		}
		if(entityId != null && entityId == 0){
			entityId = null;
		}
		workflowMasterDAO.updateIsDefaultOfEntityMapping(workflowEntityMappingId, productId, entityTypeId, entityId);
	}

	@Override
	@Transactional
	public List<Object[]> getWorkflowMyActionCounts(Integer productId, Integer userId, Integer userRoleId,Integer entityTypeId) {
		List<Object[]> workflowEntityTypeCountList=new ArrayList<Object[]>();
		workflowEntityTypeCountList.addAll(workflowMasterDAO.getWorkflowMyActionCounts(productId, userId, userRoleId,entityTypeId));
		return workflowEntityTypeCountList;
	}

	@Override
	@Transactional
	public JTableSingleResponse changeInstnaceWorkflowMapping(Integer productId, Integer entityTypeId, Integer entityId, Integer entityInstanceId, Integer workflowId, UserList user) {
		JTableSingleResponse jTableSingleResponse = null;
		try{
			if(entityId == 0){
				entityId = null;
			}
			if(workflowId == null || workflowId == 0){
				jTableSingleResponse = new JTableSingleResponse("ERROR", "Workflow is not selected");
				return jTableSingleResponse;
			}
			boolean isEligibleToChange = workflowEventService.checkInstanceEligibiltyToChangeWorkflowMapping(entityTypeId, entityId, entityInstanceId);
			if(isEligibleToChange){
				ActivityWorkPackage activityWorkPackage = null;
				Activity activity = null;
				ActivityTask activityTask = null;
				TestCaseList testCaseList = null;
				Date plannedStartDate = null;
				
				if(entityTypeId == IDPAConstants.ENTITY_ACTIVITY_WORKPACKAGE_ID){
					activityWorkPackage = activityWorkPackageService.getActivityWorkPackageById(entityInstanceId, 0);
					plannedStartDate = activityWorkPackage.getPlannedStartDate();
				}else if(entityTypeId == IDPAConstants.ENTITY_ACTIVITY_TYPE){
					activity = activityService.getActivityById(entityInstanceId, 0);
					plannedStartDate = activity.getPlannedStartDate();
				}else if(entityTypeId == IDPAConstants.ENTITY_TASK_TYPE){
					activityTask = activityTaskService.getActivityTaskById(entityInstanceId, 0);
					plannedStartDate = activityTask.getPlannedStartDate();
				}else if(entityTypeId == IDPAConstants.ENTITY_TEST_CASE_ID){
					testCaseList = testCaseService.getTestCaseById(entityInstanceId);
				}
				
				workflowStatusPolicyService.updateInstanceEntityMappingAndDeleteStatusPolicies(entityTypeId, entityId, entityInstanceId, workflowId);
				workflowStatusPolicyService.setEntityInstanceStatusPoliciesFromWorkflowPolicies(productId, entityTypeId, entityId, entityInstanceId, workflowId, plannedStartDate);
				List<WorkflowStatusPolicy> workflowStatusPolicies = workflowStatusPolicyService.getWorkflowStatusPolicies(workflowId, entityTypeId, entityId, entityInstanceId, productId, 1, "Instance");
				WorkflowStatus workflowStatus = null;
				if(workflowStatusPolicies != null && workflowStatusPolicies.size() > 0){
					workflowStatus = workflowStatusPolicies.get(0).getWorkflowStatus();
				}else{
					Integer defultWorkflowId = -1;
					workflowStatus = workflowStatusService.getWorkflowStatusById(defultWorkflowId);
				}
				if(workflowStatus != null){
					if(activityWorkPackage != null){
						activityWorkPackage.setWorkflowStatus(workflowStatus);
						activityWorkPackageService.updateActivityWorkPackage(activityWorkPackage);
					}else if(activity != null){
						activity.setWorkflowStatus(workflowStatus);
						activityService.updateActivity(activity);
					}else if(activityTask != null){
						activityTask.setStatus(workflowStatus);
						activityTaskService.updateActivityTask(activityTask);
					}else if(testCaseList != null){
						testCaseList.setWorkflowStatus(workflowStatus);
						testCaseService.update(testCaseList);
					}
					workflowEventService.setInitialInstanceEvent(productId, entityTypeId, entityId, entityInstanceId, workflowStatus.getWorkflowStatusId(), null, 0, "", user);
				}else{
					jTableSingleResponse = new JTableSingleResponse("ERROR", "No status found for mapping");
					return jTableSingleResponse;
				}
				jTableSingleResponse = new JTableSingleResponse("OK", "Workflow mapping for instance changed successfully");
			}else{
				jTableSingleResponse = new JTableSingleResponse("ERROR", "Instance under gone some operation(s), cannot change workflow mapping at this point");
				log.info("Instance operation is in process cannot change workflow mapping in between");
			}
			
		}catch(Exception ex){
			jTableSingleResponse = new JTableSingleResponse("ERROR", "Unable to change instance workflow mapping ");
			log.error("Error in changeInstnaceWorkflowMapping - ", ex);
		}
		return jTableSingleResponse;
	}

	@Override
	public List<Activity> listActivitiesForUser(Integer productId, String entityLevel, Integer entityLevelId, Integer userId, Integer userRoleId, Integer jtStartIndex, Integer jtPageSize) {
		Integer activityWorkPackageId=0;
		Integer testFactoryId=0;
		try {
			return workflowActivityDAO.listActivitiesforWorkFlowForUserOrRole(testFactoryId,productId,activityWorkPackageId, null, IDPAConstants.ENTITY_ACTIVITY_TYPE, null, userId, userRoleId, jtStartIndex, jtPageSize);
		} catch (Throwable t) {
			log.error(t);
		} 
		return null;
	}
	
	@Override
	@Transactional
	public List<ActivityWorkPackage> listActivityWorkpackagesForUser(Integer productId, String entityLevel, Integer entityLevelId, Integer userId, Integer roleId, Integer jtStartIndex, Integer jtPageSize) {
		try {
			List<ActivityWorkPackage> activityWorkPackages = new ArrayList<ActivityWorkPackage>();
			activityWorkPackages = workflowActivityWorkpackageDAO.listActivityWorkpackageforWorkFlowForUserOrRole(productId, null, IDPAConstants.ENTITY_ACTIVITY_WORKPACKAGE_ID, null, userId, roleId, jtStartIndex, jtPageSize);
			return activityWorkPackages;
		} catch (Throwable t) {
			log.error(t);
		} 
		return null;
	}

	@Override
	@Transactional
	public List<WorkflowMasterEntityMapping> getEntitiesAndInstanceMappedWithWorkflow(Integer workflowId) {
		List<WorkflowMasterEntityMapping> workflowMasterEntityMappings = new ArrayList<WorkflowMasterEntityMapping>();
		try{
			workflowMasterEntityMappings = workflowMasterDAO.getEntitiesAndInstanceMappedWithWorkflow(workflowId);
		}catch(Exception ex){
			log.error("Error in getEntitiesAndInstanceMappedWithWorkflow - ", ex);
		}
		return workflowMasterEntityMappings;
	}
	
	
	@Override
	@Transactional
	public JTableSingleResponse changeInstnaceActorMapping(Integer productId, Integer entityTypeId, Integer entityId, Integer entityInstanceId, Integer workflowId, UserList user) {
		JTableSingleResponse jTableSingleResponse = null;
		try{
			if(entityId == 0){
				entityId = null;
			}
			if(workflowId == null || workflowId == 0){
				jTableSingleResponse = new JTableSingleResponse("ERROR", "Workflow is not selected");
				return jTableSingleResponse;
			}
				ActivityWorkPackage activityWorkPackage = null;
				Activity activity = null;
				ActivityTask activityTask = null;
				TestCaseList testCaseList = null;
				ProductFeature productFeature=null;
				Date plannedStartDate = null;
				
				if(entityTypeId == IDPAConstants.ENTITY_ACTIVITY_WORKPACKAGE_ID){
					activityWorkPackage = activityWorkPackageService.getActivityWorkPackageById(entityInstanceId, 0);
					plannedStartDate = activityWorkPackage.getPlannedStartDate();
				}else if(entityTypeId == IDPAConstants.ENTITY_ACTIVITY_TYPE){
					activity = activityService.getActivityById(entityInstanceId, 0);
					plannedStartDate = activity.getPlannedStartDate();
				}else if(entityTypeId == IDPAConstants.ENTITY_TASK_TYPE){
					activityTask = activityTaskService.getActivityTaskById(entityInstanceId, 0);
					plannedStartDate = activityTask.getPlannedStartDate();
				}else if(entityTypeId == IDPAConstants.ENTITY_TEST_CASE_ID){
					testCaseList = testCaseService.getTestCaseById(entityInstanceId);
				}else if(entityTypeId == IDPAConstants.PRODUCT_FEATURE_ENTITY_MASTER_ID){
					productFeature = productListService.getByProductFeatureId(entityInstanceId);
				}
				
				workflowStatusPolicyService.removeInstanceFromEntityWorkflowStatusActorsMapping(entityTypeId, entityId, entityInstanceId);
				
				List<WorkflowStatusPolicy> workflowStatusPolicies = workflowStatusPolicyService.getWorkflowStatusPolicies(workflowId, entityTypeId, entityId, entityInstanceId, productId, 1, "Instance");
				if(workflowStatusPolicies != null && workflowStatusPolicies.size() >0) {
					for(WorkflowStatusPolicy workflowStatusPolicy:workflowStatusPolicies) {
						workflowStatusPolicyService.instanciateActorFromEntityToInstance(productId, workflowStatusPolicy.getWorkflowStatus().getWorkflowStatusId(), entityTypeId, entityId, entityInstanceId, "Both");
					}
				} else {
					jTableSingleResponse = new JTableSingleResponse("ERROR", "No status found for mapping");
					return jTableSingleResponse;
				}
				
				jTableSingleResponse = new JTableSingleResponse("OK", "Workflow mapping for instance changed successfully");
		}catch(Exception ex){
			jTableSingleResponse = new JTableSingleResponse("ERROR", "Unable to change instance actor mapping ");
			log.error("Error in changeInstnaceActorMapping - ", ex);
		}
		return jTableSingleResponse;
	}
}


