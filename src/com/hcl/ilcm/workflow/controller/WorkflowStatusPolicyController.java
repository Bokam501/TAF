/**
 * 
 */
package com.hcl.ilcm.workflow.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hcl.atf.taf.constants.IDPAConstants;
import com.hcl.atf.taf.controller.utilities.ValidationUtility;
import com.hcl.atf.taf.integration.data.excel.ExcelTestDataIntegrator;
import com.hcl.atf.taf.model.Activity;
import com.hcl.atf.taf.model.ActivityTask;
import com.hcl.atf.taf.model.ActivityWorkPackage;
import com.hcl.atf.taf.model.TestCaseList;
import com.hcl.atf.taf.model.UserList;
import com.hcl.atf.taf.model.json.JsonActivity;
import com.hcl.atf.taf.model.json.JsonActivityTask;
import com.hcl.atf.taf.model.json.JsonActivityWorkPackage;
import com.hcl.atf.taf.model.json.JsonTestCaseList;
import com.hcl.atf.taf.model.json.JsonUserList;
import com.hcl.atf.taf.model.json.jtable.JTableResponse;
import com.hcl.atf.taf.model.json.jtable.JTableResponseOptions;
import com.hcl.atf.taf.model.json.jtable.JTableSingleResponse;
import com.hcl.atf.taf.service.CommonService;
import com.hcl.atf.taf.service.EventsService;
import com.hcl.atf.taf.service.UserListService;
import com.hcl.atf.taf.service.WorkPackageService;
import com.hcl.ilcm.workflow.model.WorkflowMaster;
import com.hcl.ilcm.workflow.model.WorkflowStatus;
import com.hcl.ilcm.workflow.model.WorkflowStatusActor;
import com.hcl.ilcm.workflow.model.WorkflowStatusMapping;
import com.hcl.ilcm.workflow.model.WorkflowStatusPolicy;
import com.hcl.ilcm.workflow.model.json.JsonCommonOption;
import com.hcl.ilcm.workflow.model.json.JsonWorkflowSLAInstance;
import com.hcl.ilcm.workflow.model.json.JsonWorkflowStatusActor;
import com.hcl.ilcm.workflow.model.json.JsonWorkflowStatusPolicy;
import com.hcl.ilcm.workflow.service.WorkflowEventService;
import com.hcl.ilcm.workflow.service.WorkflowMasterService;
import com.hcl.ilcm.workflow.service.WorkflowStatusPolicyService;
import com.hcl.ilcm.workflow.service.WorkflowStatusService;

/**
 * @author silambarasur
 * 
 */
@Controller
public class WorkflowStatusPolicyController {

	private static final Log log = LogFactory
			.getLog(WorkflowStatusPolicyController.class);
	@Autowired
	private WorkflowStatusPolicyService workflowStatusPoliciesService;
	
	@Autowired
	private WorkflowMasterService workflowMasterService;
	
	@Autowired
	private WorkflowStatusService workflowStatusService;
	
	@Autowired
	private UserListService userListService;
	@Autowired
	private WorkPackageService workPackageService;
	@Autowired
	private EventsService eventsService;
	
	@Autowired
	private ExcelTestDataIntegrator excelTestDataIntegrator;

	@Autowired
	private WorkflowEventService workflowEventService;
	
	@Autowired
	CommonService commonService;
	
	@RequestMapping(value = "workflow.status.policy.list", method = RequestMethod.POST, produces = "application/json")
	public @ResponseBody JTableResponse getWorkflowStatusPolicyList(HttpServletRequest req, @RequestParam Integer isActive, @RequestParam Integer productId, @RequestParam Integer workflowId, @RequestParam Integer entityTypeId, @RequestParam Integer entityId, @RequestParam Integer entityInstanceId, @RequestParam String scopeType) {
		log.info("Fetching workflow.status.policy.list");
		JTableResponse jTableResponse = null;
		List<JsonWorkflowStatusPolicy> JsonWorkflowStatusPolicies = new ArrayList<JsonWorkflowStatusPolicy>();
		List<WorkflowStatusPolicy> workflowStatusPoliciesList = null;
		try {
			Integer activeStatus=1;
			if(entityTypeId == 0){
				entityTypeId = null;
			}
			if(entityId == 0){
				entityId = null;
			}
			if(entityInstanceId == 0){
				entityInstanceId = null;
			}
			if(productId == 0){
				productId = null;
			}
			if(workflowId == 0){
				WorkflowMaster workflowMaster = workflowMasterService.getWorkflowForEntityTypeOrInstance(productId, entityTypeId,  entityId, entityInstanceId, isActive);
				if(workflowMaster != null){
					workflowId = workflowMaster.getWorkflowId();
				}
			}
			workflowStatusPoliciesList = workflowStatusPoliciesService.getWorkflowStatusPolicies(workflowId, entityTypeId, entityId, entityInstanceId, productId, activeStatus, scopeType);

			if (workflowStatusPoliciesList != null && workflowStatusPoliciesList.size() > 0) {
				List<WorkflowStatusActor> workflowStatusActors = workflowStatusPoliciesService.getWorkflowStatusActor(productId, null, entityTypeId, entityId, entityInstanceId, "Both");
				String users = "";
				String roles = "";
				for (WorkflowStatusPolicy statusPolicy : workflowStatusPoliciesList) {
					JsonWorkflowStatusPolicy jsonWorkflowStatusPolicy = new JsonWorkflowStatusPolicy(statusPolicy);
					users = "";
					roles = "";
					for(WorkflowStatusActor workflowStatusActor : workflowStatusActors){
						if((workflowStatusActor.getWorkflowStatus() != null) && (workflowStatusActor.getWorkflowStatus().getWorkflowStatusId()  == statusPolicy.getWorkflowStatus().getWorkflowStatusId())){
							if(workflowStatusActor.getUser() != null){
								users += workflowStatusActor.getUser().getLoginId()+", ";
							}
							if(workflowStatusActor.getRole() != null){
								roles += workflowStatusActor.getRole().getRoleLabel()+", ";
							}
						}else{
							continue;
						}
					}
					users = users + roles;
					if(users != null && users.trim().endsWith(",")){
						users = users.trim().substring(0, (users.trim().length() - 1));
					}
					jsonWorkflowStatusPolicy.setStatusActors(users);
					JsonWorkflowStatusPolicies.add(jsonWorkflowStatusPolicy);
				}
			}
			jTableResponse = new JTableResponse("OK",
					JsonWorkflowStatusPolicies,
					JsonWorkflowStatusPolicies.size());
			log.info("Fetched workflow status policy records ");
		} catch (Exception e) {
			jTableResponse = new JTableResponse("ERROR",
					"Error fetching records!");
			log.error("JSON ERROR", e);
		}
		return jTableResponse;

	}

	@RequestMapping(value = "workflow.status.policy.add", method = RequestMethod.POST, produces = "application/json")
	public @ResponseBody JTableSingleResponse addWorkFlowStatusForWorkflow(HttpServletRequest request, @RequestParam Integer workflowId,@ModelAttribute JsonWorkflowStatusPolicy jsonWorkflowStatusPolicy, BindingResult result) {
		JTableSingleResponse jTableSingleResponse = null;
		try {

			if(result.hasErrors()){
				return jTableSingleResponse = new JTableSingleResponse("ERROR", "Workflow status adding faild, please all mandatory fields and their values are valid !");
			}
			boolean isExistWorkflowStatus=workflowStatusService.isExistWorkflowStausByWorkflowIdAndWorkflowStatusName(workflowId, jsonWorkflowStatusPolicy.getWorkflowStatusName());
			if(isExistWorkflowStatus) {
				return jTableSingleResponse = new JTableSingleResponse("INFORMATION", "Workflow status already exists!");
			}
			boolean statusTypeValidation=workflowStatusService.isExistWorkflowStausType(workflowId, IDPAConstants.WORKFLOW_STATUS_TYPE_BEGIN);
			if(!statusTypeValidation && !jsonWorkflowStatusPolicy.getWorkflowStatusType().equalsIgnoreCase(IDPAConstants.WORKFLOW_STATUS_TYPE_BEGIN)) {
				return jTableSingleResponse = new JTableSingleResponse("INFORMATION", "Atleast one begin status type required, there is no begin status type available! ");
			}
			WorkflowStatusPolicy workflowStatusPolicy = jsonWorkflowStatusPolicy.getWorkflowStatusPolicy();
			workflowStatusPolicy.setStatusPolicyType(IDPAConstants.WORKFLOW_STATUS_POLICY_TYPE_WORKFLOW);
			workflowStatusPolicy.setActiveStatus(1);
			if(workflowStatusPolicy.getWeightage() == null){
				workflowStatusPolicy.setWeightage(1);
			}
			workflowStatusPoliciesService.addWorkflowStatusPolicy(workflowStatusPolicy, workflowId);
			
			workflowStatusPoliciesService.mapStatusToEntityAndInstance(workflowStatusPolicy, workflowId);
			
			UserList user = (UserList) request.getSession().getAttribute("USER");
			if(workflowStatusPolicy != null && workflowStatusPolicy.getWorkflowStatusPolicyId() != null){
				if(workflowStatusPolicy.getWorkflowStatus() != null){
					eventsService.addNewEntityEvent(IDPAConstants.ENTITY_WORKFLOW_TEMPLATE_STATUS, workflowStatusPolicy.getWorkflowStatus().getWorkflowStatusId(), workflowStatusPolicy.getWorkflowStatus().getWorkflowStatusName(), user);
				}
				eventsService.addNewEntityEvent(IDPAConstants.ENTITY_WORKFLOW_POLICY, workflowStatusPolicy.getWorkflowStatusPolicyId(), workflowStatusPolicy.getStatusPolicyType(), user);
			}
			jTableSingleResponse = new JTableSingleResponse("OK",new JsonWorkflowStatusPolicy(workflowStatusPolicy));
			log.info("workflow.status.policy.add Success");
		} catch (Exception e) {
			jTableSingleResponse = new JTableSingleResponse("ERROR",
					"Error adding workflow status policy data record!");
			log.error("JSON ERROR", e);
		}
		return jTableSingleResponse;
	}

	@RequestMapping(value = "workflow.status.policy.update", method = RequestMethod.POST, produces = "application/json")
	public @ResponseBody JTableSingleResponse updateWorkFlowStatusForWorkflow(HttpServletRequest request, @RequestParam int workflowId,@ModelAttribute JsonWorkflowStatusPolicy jsonWorkflowStatusPolicy, BindingResult result) {
		JTableSingleResponse jTableSingleResponse = null;
		String remarks = "";
		try {
			
			if(workflowId == 0){
				WorkflowMaster workflowMaster = workflowMasterService.getWorkflowForEntityTypeOrInstance(jsonWorkflowStatusPolicy.getLevelId(), jsonWorkflowStatusPolicy.getEntityTypeId(),  jsonWorkflowStatusPolicy.getEntityId(), jsonWorkflowStatusPolicy.getEntityInstanceId(), jsonWorkflowStatusPolicy.getActiveStatus());
				if(workflowMaster != null){
					workflowId = workflowMaster.getWorkflowId();
				}
			}
			WorkflowStatusPolicy workflowStatusPolicy = jsonWorkflowStatusPolicy.getWorkflowStatusPolicy();
			if(jsonWorkflowStatusPolicy.getModifiedField() != null && "workflowStatusType".equalsIgnoreCase(jsonWorkflowStatusPolicy.getModifiedField())){
				Boolean isStatusTypeChangeAllowed = workflowStatusService.isStatusAvailableForStatusType(workflowId, jsonWorkflowStatusPolicy.getWorkflowStatusType(), workflowStatusPolicy.getWorkflowStatus().getWorkflowStatusId());
				if(!isStatusTypeChangeAllowed){
					return new JTableSingleResponse("ERROR", "Please check workflow contains atleast on status in Begin, Intermediate and End status types");
				}
			}
			if(jsonWorkflowStatusPolicy.getModifiedField() != null && ("plannedStartDate".equalsIgnoreCase(jsonWorkflowStatusPolicy.getModifiedField()) || "plannedEndDate".equalsIgnoreCase(jsonWorkflowStatusPolicy.getModifiedField()))){
				if(workflowStatusPolicy.getPlannedStartDate() != null && workflowStatusPolicy.getPlannedEndDate() != null){
					if(workflowStatusPolicy.getPlannedStartDate().after(workflowStatusPolicy.getPlannedEndDate())){
						return jTableSingleResponse = new JTableSingleResponse("ERROR", "Planned start date should be lesser or equal to planned end date");
					}else{
						Long slaHours = workflowStatusPolicy.getPlannedEndDate().getTime() - workflowStatusPolicy.getPlannedStartDate().getTime() ;
						slaHours = (slaHours / (1000*60*60));
						workflowStatusPolicy.setSlaDuration(slaHours.intValue());
					}
				}else if(workflowStatusPolicy.getPlannedStartDate() == null && workflowStatusPolicy.getPlannedEndDate() != null){
					return jTableSingleResponse = new JTableSingleResponse("ERROR", "Please enter planned start date");
				}
			}
			if(workflowStatusPolicy.getActualStartDate() != null && workflowStatusPolicy.getActualEndDate() != null && workflowStatusPolicy.getActualStartDate().after(workflowStatusPolicy.getActualEndDate())){
				return jTableSingleResponse = new JTableSingleResponse("ERROR", "Actual start date should be lesser or equal to planned end date");
			}else if(workflowStatusPolicy.getActualStartDate() == null && workflowStatusPolicy.getActualEndDate() != null){
				return jTableSingleResponse = new JTableSingleResponse("ERROR", "Please enter actual start date");
			}			
			if(workflowStatusPolicy.getWeightage() == null){				
				return jTableSingleResponse = new JTableSingleResponse("ERROR", "Please enter the numeric value");	
			}		
			if(workflowStatusPolicy.getWorkflowStatus().getStatusOrder() == null){
				return jTableSingleResponse = new JTableSingleResponse("ERROR", "Please enter the numeric value");
				
			}
			
			
			workflowStatusPoliciesService.updateWorkflowStatusPolicy(workflowStatusPolicy,workflowId);
			UserList user = (UserList) request.getSession().getAttribute("USER");
			if(workflowStatusPolicy != null && workflowStatusPolicy.getWorkflowStatusPolicyId() != null){
				remarks = "WorkflowStatus :"+workflowStatusPolicy.getWorkflowStatus().getWorkflowStatusName();
				if(workflowStatusPolicy.getWorkflowStatus() != null){
					eventsService.addEntityChangedEvent(IDPAConstants.ENTITY_WORKFLOW_TEMPLATE_STATUS,  workflowStatusPolicy.getWorkflowStatus().getWorkflowStatusId(), workflowStatusPolicy.getWorkflowStatus().getWorkflowStatusName(),
							jsonWorkflowStatusPolicy.getModifiedField(), jsonWorkflowStatusPolicy.getModifiedFieldTitle(),
							jsonWorkflowStatusPolicy.getOldFieldValue(), jsonWorkflowStatusPolicy.getModifiedFieldValue(), user, remarks);
				}
				eventsService.addEntityChangedEvent(IDPAConstants.ENTITY_WORKFLOW_POLICY, workflowStatusPolicy.getWorkflowStatusPolicyId(), workflowStatusPolicy.getStatusPolicyType(),
						jsonWorkflowStatusPolicy.getModifiedField(), jsonWorkflowStatusPolicy.getModifiedFieldTitle(),
						jsonWorkflowStatusPolicy.getOldFieldValue(), jsonWorkflowStatusPolicy.getModifiedFieldValue(), user, remarks);
				
			}
			
			jTableSingleResponse = new JTableSingleResponse("OK",new JsonWorkflowStatusPolicy(workflowStatusPolicy));
			log.info("workflow.status.policy.update Success");
		} catch (Exception e) {
			jTableSingleResponse = new JTableSingleResponse("ERROR", "Error in updating workflow status policy data record!");
			log.error("JSON ERROR", e);
		}
		return jTableSingleResponse;
	}
	
	@RequestMapping(value = "workflow.status.policy.delete", method = RequestMethod.POST, produces = "application/json")
	public @ResponseBody JTableSingleResponse deleteWorkFlowStatusForWorkflow(HttpServletRequest request, @ModelAttribute JsonWorkflowStatusPolicy jsonWorkflowStatusPolicy, BindingResult result) {
		JTableSingleResponse jTableSingleResponse = null;
		try {
			WorkflowStatusPolicy workflowStatusPolicy = workflowStatusPoliciesService.getWorkflowStatusPolicyById(jsonWorkflowStatusPolicy.getWorkflowStatusPolicyId());
			if(workflowStatusPolicy == null){
				jTableSingleResponse = new JTableSingleResponse("ERROR", "Unable to delete workflow status");
			}
			boolean isWorkflowStatusInEventAction = workflowEventService.isWorkflowStatusInEventAction(workflowStatusPolicy.getWorkflowStatus().getWorkflowStatusId());
			if(isWorkflowStatusInEventAction) {
				return jTableSingleResponse = new JTableSingleResponse("ERROR", "Workflow status deletion failed, status in action for some instance");
			}
			boolean isWorkflowStatusInLifeCycleStage = workflowStatusPoliciesService.isWorkflowStatusInLifeCycleStage(workflowStatusPolicy.getWorkflowStatus().getWorkflowStatusId());
			if(isWorkflowStatusInLifeCycleStage) {
				return jTableSingleResponse = new JTableSingleResponse("ERROR", "Workflow status deletion failed, status in life cycle stage of an activity or task");
			}
			workflowStatusPoliciesService.deleteWorkflowStatusAndPolicy(workflowStatusPolicy.getWorkflowStatus().getWorkflowStatusId());
			
			jTableSingleResponse = new JTableSingleResponse("OK", "Workflow status deleted successfully");
			log.info("workflow.status.policy.delete Success");
		} catch (Exception e) {
			jTableSingleResponse = new JTableSingleResponse("ERROR", "Error deleting workflow status policy data record!");
			log.error("JSON ERROR", e);
		}
		return jTableSingleResponse;
	}
	
	@RequestMapping(value="workflow.status.policy.action.mode.option.list",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableResponseOptions listWorkflowStatusPolicyActionMode() {
		log.info("Fetching workflow.status.policy.action.mode.option.list ");
		JTableResponseOptions jTableResponseOptions;
		try {			
			
			List<JsonCommonOption> jsonCommonOptions= new ArrayList<JsonCommonOption>();
			JsonCommonOption commonOption=new JsonCommonOption();
			commonOption.setValue(IDPAConstants.WORKFLOW_STATUS_POLICY_USER_ACTION_SCOPE);
			jsonCommonOptions.add(commonOption);
			commonOption=new JsonCommonOption();
			commonOption.setValue(IDPAConstants.WORKFLOW_STATUS_POLICY_ROLE_ACTION_SCOPE);
			jsonCommonOptions.add(commonOption);
			commonOption=new JsonCommonOption();
			commonOption.setValue(IDPAConstants.WORKFLOW_STATUS_POLICY_USERORROLE_ACTION_SCOPE);
			jsonCommonOptions.add(commonOption);

			jTableResponseOptions = new JTableResponseOptions("OK", jsonCommonOptions, false);
			
			return jTableResponseOptions;
	        } catch (Exception e) {
	        	jTableResponseOptions = new JTableResponseOptions("ERROR","Error in Workflow status policy Action mode ");
	            log.error("JSON ERROR", e);
	        }
        return jTableResponseOptions;
    }
	
	@RequestMapping(value="workflow.user.list.by.product.id.options",method=RequestMethod.POST ,produces="application/json")
	public @ResponseBody JTableResponseOptions workflowUserListByProductIdOptions(@RequestParam Integer productId, @RequestParam Boolean isAddonUsersAllowed) {
		log.debug("inside workflow.user.list.by.product.id.options");
		JTableResponseOptions jTableResponseOptions = null;	
		try {			
			List<UserList> userLists = userListService.getActivityUserListBasedRoleIdAndProductId(-10, productId);
			List<JsonUserList> jsonUserLists = new ArrayList<JsonUserList>();
			for(UserList userList: userLists){
				jsonUserLists.add(new JsonUserList(userList));
			}
			if(isAddonUsersAllowed != null && isAddonUsersAllowed && jsonUserLists != null){
				UserList userList = userListService.getUserListById(-1);
				jsonUserLists.add(new JsonUserList(userList));
				
				userList = userListService.getUserListById(-2);
				jsonUserLists.add(new JsonUserList(userList));
			}
			jTableResponseOptions = new JTableResponseOptions("OK", jsonUserLists,true);
	           
        } catch (Exception e) {
        	jTableResponseOptions = new JTableResponseOptions("ERROR","Error fetching records!");
            log.error("JSON ERROR", e);
        }
	        
        return jTableResponseOptions;
    }
	
	@RequestMapping(value = "workflow.status.policy.actor.list", method = RequestMethod.POST, produces = "application/json")
	public @ResponseBody JTableResponse getWorkFlowStatusPolicyActorList(HttpServletRequest req, @RequestParam Integer productId, @RequestParam Integer workflowStatusId, @RequestParam Integer entityTypeId, @RequestParam Integer entityId, @RequestParam Integer entityInstanceId, @RequestParam String actorType) {
		log.info("Getting WorkFlowStatusPolicyActorList");
		JTableResponse jTableResponse = null;
		List<JsonWorkflowStatusActor> jsonWorkflowStatusActors = new ArrayList<JsonWorkflowStatusActor>();
		List<WorkflowStatusActor> workflowStatusActors = null;
		try {
			if(entityInstanceId != null && entityInstanceId == 0){
				entityInstanceId = null;
			}
			if(entityId != null && entityId == 0){
				entityId = null;
			}
			workflowStatusActors = workflowStatusPoliciesService.getWorkflowStatusActor(productId, workflowStatusId, entityTypeId, entityId, entityInstanceId, actorType);
			if (workflowStatusActors != null && workflowStatusActors.size() > 0) {
				for (WorkflowStatusActor workflowStatusActor : workflowStatusActors) {
					jsonWorkflowStatusActors.add(new JsonWorkflowStatusActor(workflowStatusActor));
				}
			}

			jTableResponse = new JTableResponse("OK", jsonWorkflowStatusActors, jsonWorkflowStatusActors.size());
			log.debug("Fetched workflow status actor records ");
		} catch (Exception e) {
			jTableResponse = new JTableResponse("ERROR", "Error fetching workflow status actor records!");
			log.error("JSON ERROR  fetching workflow status actor records", e);
		}
		return jTableResponse;

	}
	
	@RequestMapping(value = "workflow.status.policy.actor.add", method = RequestMethod.POST, produces = "application/json")
	public @ResponseBody JTableSingleResponse addWorkFlowStatusPolicyActor(HttpServletRequest request, @RequestParam Integer productId, @RequestParam Integer entityTypeId, @RequestParam Integer entityId, @RequestParam Integer entityInstanceId, @RequestParam String actorMappingType, @ModelAttribute JsonWorkflowStatusActor jsonWorkflowStatusActor, BindingResult result) {
		JTableSingleResponse jTableSingleResponse = null;
		try {
			if(entityInstanceId != null && entityInstanceId == 0){
				entityInstanceId = null;
			}
			if(entityId != null && entityId == 0){
				entityId = null;
			}
			jsonWorkflowStatusActor.setEntityInstanceId(entityInstanceId);
			jsonWorkflowStatusActor.setEntityId(entityId);
			WorkflowStatusActor workflowStatusActor = jsonWorkflowStatusActor.getWorkflowStatusActor();
			boolean isMappingAleadyExist = workflowStatusPoliciesService.checkWorkflowStatusActorAlreadyExist(productId, jsonWorkflowStatusActor.getWorkflowStatusActorId(), jsonWorkflowStatusActor.getWorkflowStatusId(), jsonWorkflowStatusActor.getEntityTypeId(), jsonWorkflowStatusActor.getEntityId(), jsonWorkflowStatusActor.getEntityInstanceId(), jsonWorkflowStatusActor.getUserId(), jsonWorkflowStatusActor.getRoleId());
			if(!isMappingAleadyExist){
				if(entityInstanceId != null){
					workflowStatusActor.setMappingLevel("Instance");
				}else{
					workflowStatusActor.setMappingLevel("Entity");
				}
				workflowStatusPoliciesService.addWorkflowStatusActor(workflowStatusActor);
				jTableSingleResponse = new JTableSingleResponse("OK", new JsonWorkflowStatusActor(workflowStatusActor));
				log.info("workflow.status.policy.user.add");
			}else{
				jTableSingleResponse = new JTableSingleResponse("ERROR", "Actor already associated with the policy");
				log.info("Actor already associated with the policy");
			}
		} catch (Exception e) {
			jTableSingleResponse = new JTableSingleResponse("ERROR", "Error adding workflow status actor!");
			log.error("JSON ERROR", e);
		}
		return jTableSingleResponse;
	}
	
	@RequestMapping(value = "workflow.status.policy.actor.update", method = RequestMethod.POST, produces = "application/json")
	public @ResponseBody JTableSingleResponse updateWorkFlowStatusPolicyActor(HttpServletRequest request, @RequestParam Integer productId, @RequestParam Integer entityTypeId, @RequestParam Integer entityId, @RequestParam Integer entityInstanceId, @RequestParam String actorMappingType, @ModelAttribute JsonWorkflowStatusActor jsonWorkflowStatusActor, BindingResult result) {
		JTableSingleResponse jTableSingleResponse = null;
		try {
			if(entityInstanceId != null && entityInstanceId == 0){
				entityInstanceId = null;
			}
			if(entityId != null && entityId == 0){
				entityId = null;
			}
			jsonWorkflowStatusActor.setEntityInstanceId(entityInstanceId);
			WorkflowStatusActor workflowStatusActor = jsonWorkflowStatusActor.getWorkflowStatusActor();
			boolean isMappingAleadyExist = workflowStatusPoliciesService.checkWorkflowStatusActorAlreadyExist(productId, jsonWorkflowStatusActor.getWorkflowStatusActorId(), jsonWorkflowStatusActor.getWorkflowStatusId(), jsonWorkflowStatusActor.getEntityTypeId(), jsonWorkflowStatusActor.getEntityId(), jsonWorkflowStatusActor.getEntityInstanceId(), jsonWorkflowStatusActor.getUserId(), jsonWorkflowStatusActor.getRoleId());
			if(!isMappingAleadyExist){
				workflowStatusPoliciesService.addWorkflowStatusActor(workflowStatusActor);
				jTableSingleResponse = new JTableSingleResponse("OK", new JsonWorkflowStatusActor(workflowStatusActor));
				log.info("workflow.status.policy.actor.update");
			}else{
				jTableSingleResponse = new JTableSingleResponse("ERROR", "Actor already associated with the policy");
				log.info("Actor already associated with the policy");
			}
		} catch (Exception e) {
			jTableSingleResponse = new JTableSingleResponse("ERROR", "Error updaing workflow status actor!");
			log.error("JSON ERROR", e);
		}
		return jTableSingleResponse;
	}
	
	@RequestMapping(value = "workflow.status.policy.actor.delete", method = RequestMethod.POST, produces = "application/json")
	public @ResponseBody JTableSingleResponse deleteWorkFlowStatusPolicyActor(HttpServletRequest request, @ModelAttribute JsonWorkflowStatusActor jsonWorkflowStatusActor, BindingResult result) {
		JTableSingleResponse jTableSingleResponse = null;
		log.info("Inside workflow.status.policy.actor.delete");
		try {
			WorkflowStatusActor workflowStatusActor = jsonWorkflowStatusActor.getWorkflowStatusActor();
			workflowStatusPoliciesService.deleteWorkflowStatusActor(workflowStatusActor);
			jTableSingleResponse = new JTableSingleResponse("OK", "Actor deleted successfully");
		} catch (Exception e) {
			jTableSingleResponse = new JTableSingleResponse("ERROR", "Error deleting workflow status actor!");
			log.error("JSON ERROR", e);
		}
		return jTableSingleResponse;
	}
	
	@RequestMapping(value="workflow.status.policy.SLA.Violation.action.option.list",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableResponseOptions listWorkflowStatusPolicySLAViolationAction() {
		log.info("inside workflow.status.policy.SLA.Violation.action.option.list ");
		JTableResponseOptions jTableResponseOptions;
		try {			
			
			List<JsonCommonOption> jsonCommonOptions= new ArrayList<JsonCommonOption>();
			
			JsonCommonOption commonOption=new JsonCommonOption();
			commonOption.setValue(IDPAConstants.WORKFLOW_STATUS_POLICY_SLA_VIOLATION_NO_ACTION);
			jsonCommonOptions.add(commonOption);
			
			commonOption=new JsonCommonOption();
			commonOption.setValue(IDPAConstants.WORKFLOW_STATUS_POLICY_SLA_VIOLATION_AUTO_APPROVE);
			jsonCommonOptions.add(commonOption);
			
			commonOption=new JsonCommonOption();
			commonOption.setValue(IDPAConstants.WORKFLOW_STATUS_POLICY_SLA_VIOLATION_AUTO_REJECT);
			jsonCommonOptions.add(commonOption);
			commonOption=new JsonCommonOption();
			commonOption.setValue(IDPAConstants.WORKFLOW_STATUS_POLICY_SLA_VIOLATION_AUTO_REFERBACK);
			jsonCommonOptions.add(commonOption);

			jTableResponseOptions = new JTableResponseOptions("OK", jsonCommonOptions, false);
			
			return jTableResponseOptions;
	        } catch (Exception e) {
	        	jTableResponseOptions = new JTableResponseOptions("ERROR","Error in Workflow status policy SLA Violation Action ");
	            log.error("JSON ERROR", e);
	        }
        return jTableResponseOptions;
    }
	
	@RequestMapping(value="workflow.status.type.option.list",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableResponseOptions listWorkflowStatusType() {
		log.info("Fetching workflow.status.type.option.list ");
		JTableResponseOptions jTableResponseOptions;
		try {			
			
			List<JsonCommonOption> jsonStatusTypeOptions= new ArrayList<JsonCommonOption>();
			
			JsonCommonOption statusTypeOption=new JsonCommonOption();
			statusTypeOption.setValue(IDPAConstants.WORKFLOW_STATUS_TYPE_BEGIN);
			jsonStatusTypeOptions.add(statusTypeOption);
			
			statusTypeOption=new JsonCommonOption();
			statusTypeOption.setValue(IDPAConstants.WORKFLOW_STATUS_TYPE_INTERMEDIATE);
			jsonStatusTypeOptions.add(statusTypeOption);
			
			statusTypeOption=new JsonCommonOption();
			statusTypeOption.setValue(IDPAConstants.WORKFLOW_STATUS_TYPE_END);
			jsonStatusTypeOptions.add(statusTypeOption);

			statusTypeOption=new JsonCommonOption();
			statusTypeOption.setValue(IDPAConstants.WORKFLOW_STATUS_TYPE_ABORT);
			jsonStatusTypeOptions.add(statusTypeOption);
			
			jTableResponseOptions = new JTableResponseOptions("OK", jsonStatusTypeOptions, false);
			
			return jTableResponseOptions;
	        } catch (Exception e) {
	        	jTableResponseOptions = new JTableResponseOptions("ERROR","Error in Workflow status type");
	            log.error("JSON ERROR Fetching Workflow status type", e);
	        }
        return jTableResponseOptions;
    }
	
	
	@RequestMapping(value="workflow.status.transition.policy.option.list",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableResponseOptions listWorkflowStatusTransistionPolicy(@RequestParam String actionScope) {
		log.info("Fetching workflow.status.transition.policy.option.list ");
		JTableResponseOptions jTableResponseOptions;
		try {			
			
			List<JsonCommonOption> jsonStatusTransitionPolicies= new ArrayList<JsonCommonOption>();
			JsonCommonOption statusTransistionPolicyOption=new JsonCommonOption();
			
			if(actionScope.equalsIgnoreCase(IDPAConstants.WORKFLOW_STATUS_POLICY_USER_ACTION_SCOPE)) {

				statusTransistionPolicyOption.setValue(IDPAConstants.WORKFLOW_STATUS_TRANSITION_POLICY_FIRST_ACTION);
				jsonStatusTransitionPolicies.add(statusTransistionPolicyOption);
				statusTransistionPolicyOption=new JsonCommonOption();
				statusTransistionPolicyOption.setValue(IDPAConstants.WORKFLOW_STATUS_TRANSITION_POLICY_MANDATORY_CHECK);
				jsonStatusTransitionPolicies.add(statusTransistionPolicyOption);
				statusTransistionPolicyOption=new JsonCommonOption();
				statusTransistionPolicyOption.setValue(IDPAConstants.WORKFLOW_STATUS_TRANSITION_POLICY_POLLING);
				jsonStatusTransitionPolicies.add(statusTransistionPolicyOption);
				statusTransistionPolicyOption=new JsonCommonOption();
				statusTransistionPolicyOption.setValue(IDPAConstants.WORKFLOW_STATUS_TRANSITION_POLICY_AUTO_APPROVE);
				jsonStatusTransitionPolicies.add(statusTransistionPolicyOption);
			
			}else if(actionScope.equalsIgnoreCase(IDPAConstants.WORKFLOW_STATUS_POLICY_ROLE_ACTION_SCOPE)) {
				
				statusTransistionPolicyOption.setValue(IDPAConstants.WORKFLOW_STATUS_TRANSITION_POLICY_FIRST_ACTION);
				jsonStatusTransitionPolicies.add(statusTransistionPolicyOption);
				statusTransistionPolicyOption=new JsonCommonOption();
				statusTransistionPolicyOption.setValue(IDPAConstants.WORKFLOW_STATUS_TRANSITION_POLICY_POLLING);
				jsonStatusTransitionPolicies.add(statusTransistionPolicyOption);
				statusTransistionPolicyOption=new JsonCommonOption();
				statusTransistionPolicyOption.setValue(IDPAConstants.WORKFLOW_STATUS_TRANSITION_POLICY_AUTO_APPROVE);
				jsonStatusTransitionPolicies.add(statusTransistionPolicyOption);
				
			}else if(actionScope.equalsIgnoreCase(IDPAConstants.WORKFLOW_STATUS_POLICY_USERORROLE_ACTION_SCOPE)) {
				
				statusTransistionPolicyOption.setValue(IDPAConstants.WORKFLOW_STATUS_TRANSITION_POLICY_FIRST_ACTION);
				jsonStatusTransitionPolicies.add(statusTransistionPolicyOption);
				statusTransistionPolicyOption=new JsonCommonOption();
				statusTransistionPolicyOption.setValue(IDPAConstants.WORKFLOW_STATUS_TRANSITION_POLICY_POLLING);
				jsonStatusTransitionPolicies.add(statusTransistionPolicyOption);
				statusTransistionPolicyOption=new JsonCommonOption();
				statusTransistionPolicyOption.setValue(IDPAConstants.WORKFLOW_STATUS_TRANSITION_POLICY_AUTO_APPROVE);
				jsonStatusTransitionPolicies.add(statusTransistionPolicyOption);
			}

			jTableResponseOptions = new JTableResponseOptions("OK", jsonStatusTransitionPolicies, false);
			
			return jTableResponseOptions;
	        } catch (Exception e) {
	        	jTableResponseOptions = new JTableResponseOptions("ERROR","Error in Workflow status Transition Policy");
	            log.error("JSON ERROR", e);
	        }
        return jTableResponseOptions;
    }
	
	@RequestMapping(value = "workflow.status.summary.instance.detail", method = RequestMethod.POST, produces = "application/json")
	public @ResponseBody JTableResponse listWorkflowSummarySLAInstanceDetail(HttpServletRequest request,@RequestParam Integer engagementId, @RequestParam Integer productId, @RequestParam Integer entityTypeId,@RequestParam Integer entityId,@RequestParam Integer workflowStatusId,@RequestParam Integer userId,@RequestParam Integer roleId,@RequestParam Integer typeId,@RequestParam Integer entityParentInstanceId,@RequestParam Integer workflowStatusCategoryId,Integer jtStartIndex,Integer jtPageSize) {
		JTableResponse jTableResponse=null;
		try{
			List<JsonWorkflowSLAInstance> jsonWorkflowSLAInstanceList = new ArrayList<JsonWorkflowSLAInstance>();
			UserList user = (UserList) request.getSession().getAttribute("USER");
			if(entityTypeId == IDPAConstants.ENTITY_TASK_TYPE) {
				List<ActivityTask> listSLAIndicatorTaskList=workflowStatusPoliciesService.listWorkflowStatusSLADetailForTask(productId, entityTypeId, entityId, workflowStatusId, userId, roleId,typeId,jtStartIndex,jtPageSize);
				if(listSLAIndicatorTaskList!=null && listSLAIndicatorTaskList.size()>0) {
					List<JsonActivityTask> jsonActivityTasks = new ArrayList<JsonActivityTask>(); 
					HashMap<Integer, ActivityTask> activityTaskDeatils = new HashMap<Integer, ActivityTask>();
					for(ActivityTask activityTask: listSLAIndicatorTaskList){
						JsonActivityTask jsonActivityTask=new JsonActivityTask(activityTask);
						activityTaskDeatils.put(activityTask.getActivityTaskId(), activityTask);
						jsonActivityTasks.add(jsonActivityTask);
					}
					workflowStatusPoliciesService.setInstanceIndicators(engagementId,productId,null, IDPAConstants.ENTITY_TASK_TYPE, jsonActivityTasks, IDPAConstants.ENTITY_TASK,user, null, null, null,IDPAConstants.ENABLE_PENDING_WITH_COLUMN);
					for(JsonActivityTask jsonActivityTask : jsonActivityTasks){
						ActivityTask activityTask = activityTaskDeatils.get(jsonActivityTask.getActivityTaskId());
						JsonWorkflowSLAInstance jsonWorkflowSLAInstance=workflowStatusPoliciesService.setJsonWorkflowSLAInstance(jsonActivityTask, IDPAConstants.WORKFLOW_STATUS_TYPE);
						if(activityTask.getActivity() != null && activityTask.getActivity().getActivityWorkPackage() != null) {
							jsonWorkflowSLAInstance.setProductId(activityTask.getActivity().getActivityWorkPackage().getProductBuild().getProductVersion().getProductMaster().getProductId());
							jsonWorkflowSLAInstance.setProductName(activityTask.getActivity().getActivityWorkPackage().getProductBuild().getProductVersion().getProductMaster().getProductName());
						}	
						jsonWorkflowSLAInstance.setEntityTypeId(entityTypeId);
						jsonWorkflowSLAInstance.setEntityTypeName("TaskType");
						jsonWorkflowSLAInstance.setEntityId(activityTask.getActivityTaskType().getActivityTaskTypeId());
						jsonWorkflowSLAInstance.setEntityName(activityTask.getActivityTaskType().getActivityTaskTypeName());
						jsonWorkflowSLAInstanceList.add(jsonWorkflowSLAInstance);
					}
					activityTaskDeatils = null;
					jsonActivityTasks = null;
				}
			}else if(entityTypeId == IDPAConstants.ENTITY_TEST_CASE_ID) {
				List<TestCaseList> listSLAIndicatorTestcaseList = workflowStatusPoliciesService.listWorkflowStatusSLADetailForTestcase(productId, entityTypeId, entityId, workflowStatusId, userId, roleId,typeId,jtStartIndex,jtPageSize);
				if(listSLAIndicatorTestcaseList != null && listSLAIndicatorTestcaseList.size()>0) {
					List<JsonTestCaseList> jsonTestCaseLists = new ArrayList<JsonTestCaseList>(); 
					HashMap<Integer, TestCaseList> testCaseDeatils = new HashMap<Integer, TestCaseList>();
					for(TestCaseList testCase: listSLAIndicatorTestcaseList){
						JsonTestCaseList jsonTestCase = new JsonTestCaseList(testCase);
						testCaseDeatils.put(testCase.getTestCaseId(), testCase);
						jsonTestCaseLists.add(jsonTestCase);
					}
					workflowStatusPoliciesService.setInstanceIndicators(engagementId,productId,null, IDPAConstants.ENTITY_TEST_CASE_ID, jsonTestCaseLists, IDPAConstants.ENTITY_TEST_CASE_ID,user, null, null, null,IDPAConstants.ENABLE_PENDING_WITH_COLUMN);
					for(JsonTestCaseList jsonTestCase : jsonTestCaseLists){
						TestCaseList testCase = testCaseDeatils.get(jsonTestCase.getTestCaseId());
						JsonWorkflowSLAInstance jsonWorkflowSLAInstance=workflowStatusPoliciesService.setJsonWorkflowSLAInstance(jsonTestCase, IDPAConstants.WORKFLOW_STATUS_TYPE);
						if(testCase.getProductMaster() != null) {
							jsonWorkflowSLAInstance.setProductId(testCase.getProductMaster().getProductId());
							jsonWorkflowSLAInstance.setProductName(testCase.getProductMaster().getProductName());
						}	
						jsonWorkflowSLAInstance.setEntityTypeId(entityTypeId);
						jsonWorkflowSLAInstance.setEntityTypeName("TestCase");
						jsonWorkflowSLAInstance.setEntityId(0);
						jsonWorkflowSLAInstance.setEntityName("");
						jsonWorkflowSLAInstanceList.add(jsonWorkflowSLAInstance);
					}
					testCaseDeatils = null;
					jsonTestCaseLists = null;
				}
			}else if(entityTypeId == IDPAConstants.ENTITY_ACTIVITY_TYPE) {
				List<Activity> listSLAIndicatorActivityList=workflowStatusPoliciesService.listWorkflowStatusSLADetailForActivity(engagementId,productId, entityTypeId, entityId, workflowStatusId, userId, roleId,typeId,workflowStatusCategoryId,entityParentInstanceId,jtStartIndex,jtPageSize);
				if(listSLAIndicatorActivityList != null && listSLAIndicatorActivityList.size() > 0) {
					List<JsonActivity> jsonActivities = new ArrayList<JsonActivity>(); 
					HashMap<Integer, Activity> activityDetails = new HashMap<Integer, Activity>();
					for(Activity activity: listSLAIndicatorActivityList){
						JsonActivity jsonActivity=new JsonActivity(activity);
						activityDetails.put(activity.getActivityId(), activity);
						jsonActivities.add(jsonActivity);
					}
					Integer activityWorkpackageId=999999;//For Performance purpose passing dummy workpackageID
					Date modifiedAfterDate = commonService.getModifiedAfterDate((Date)request.getSession().getAttribute("userLoginTime"), (Date)request.getSession().getAttribute("userLogoutTime"));
					workflowStatusPoliciesService.setInstanceIndicators(engagementId,productId,activityWorkpackageId, IDPAConstants.ENTITY_ACTIVITY_TYPE, jsonActivities, IDPAConstants.ENTITY_ACTIVITY_ID,user, modifiedAfterDate, null, null,IDPAConstants.ENABLE_PENDING_WITH_COLUMN);
					for(JsonActivity jsonActivity : jsonActivities){
						Activity activity = activityDetails.get(jsonActivity.getActivityId());
						JsonWorkflowSLAInstance jsonWorkflowSLAInstance=workflowStatusPoliciesService.setJsonWorkflowSLAInstance(jsonActivity, IDPAConstants.WORKFLOW_STATUS_TYPE);
						if(activity.getActivityWorkPackage() != null) {
							jsonWorkflowSLAInstance.setProductId(activity.getActivityWorkPackage().getProductBuild().getProductVersion().getProductMaster().getProductId());
							jsonWorkflowSLAInstance.setProductName(activity.getActivityWorkPackage().getProductBuild().getProductVersion().getProductMaster().getProductName());
						}	
						jsonWorkflowSLAInstance.setEntityTypeId(entityTypeId);
						jsonWorkflowSLAInstance.setEntityTypeName("Activities");
						jsonWorkflowSLAInstance.setEntityId(jsonActivity.getActivityMasterId());
						jsonWorkflowSLAInstance.setEntityName(jsonActivity.getActivityMasterName());
						jsonWorkflowSLAInstanceList.add(jsonWorkflowSLAInstance);
					}
					activityDetails = null;
					jsonActivities = null;
				}
			}else if(entityTypeId == IDPAConstants.ENTITY_ACTIVITY_WORKPACKAGE_ID) {
				List<ActivityWorkPackage> listSLAIndicatorActivityWorkPackages = workflowStatusPoliciesService.listWorkflowStatusSLADetailForActivityWorkpackage(engagementId,productId, entityTypeId, entityId, workflowStatusId, userId, roleId,typeId,jtStartIndex,jtPageSize);
				if(listSLAIndicatorActivityWorkPackages != null && listSLAIndicatorActivityWorkPackages.size() > 0) {
					List<JsonActivityWorkPackage> jsonActivityWorkPackages = new ArrayList<JsonActivityWorkPackage>();
					HashMap<Integer, ActivityWorkPackage> activityWorkpackageDetails = new HashMap<Integer, ActivityWorkPackage>();
					for(ActivityWorkPackage activityWorkPackage : listSLAIndicatorActivityWorkPackages){
						JsonActivityWorkPackage jsonActivityWorkPackage = new JsonActivityWorkPackage(activityWorkPackage);
						activityWorkpackageDetails.put(activityWorkPackage.getActivityWorkPackageId(), activityWorkPackage);
						jsonActivityWorkPackages.add(jsonActivityWorkPackage);
					}
					workflowStatusPoliciesService.setInstanceIndicators(engagementId,productId,null, IDPAConstants.ENTITY_ACTIVITY_WORKPACKAGE_ID, jsonActivityWorkPackages, IDPAConstants.ENTITY_ACTIVITY_WORKPACKAGE_ID,user, null, null, null,IDPAConstants.ENABLE_PENDING_WITH_COLUMN);
					for(JsonActivityWorkPackage jsonActivityWorkPackage : jsonActivityWorkPackages){
						ActivityWorkPackage activityWorkPackage = activityWorkpackageDetails.get(jsonActivityWorkPackage.getActivityWorkPackageId());
						JsonWorkflowSLAInstance jsonWorkflowSLAInstance = workflowStatusPoliciesService.setJsonWorkflowSLAInstance(jsonActivityWorkPackage, IDPAConstants.WORKFLOW_STATUS_TYPE);
						if(activityWorkPackage.getProductBuild() != null) {
							jsonWorkflowSLAInstance.setProductId(activityWorkPackage.getProductBuild().getProductVersion().getProductMaster().getProductId());
							jsonWorkflowSLAInstance.setProductName(activityWorkPackage.getProductBuild().getProductVersion().getProductMaster().getProductName());
						}	
						jsonWorkflowSLAInstance.setEntityTypeId(entityTypeId);
						jsonWorkflowSLAInstance.setEntityTypeName("Activity Workpackages");
						jsonWorkflowSLAInstance.setEntityId(null);
						jsonWorkflowSLAInstance.setEntityName(null);
						jsonWorkflowSLAInstanceList.add(jsonWorkflowSLAInstance);
					}
					activityWorkpackageDetails = null;
					jsonActivityWorkPackages = null;
				}
			}
			jTableResponse = new JTableResponse("OK", jsonWorkflowSLAInstanceList,jsonWorkflowSLAInstanceList.size());
			log.info("workflow.status.summary.instance.detail Completed");
		}catch(Exception e) {
			log.error("Error in listWorkflowSummarySLAInstanceDetail",e);
		}
		return jTableResponse;
		}
	
		@RequestMapping(value="workflow.type.option.list",method=RequestMethod.POST ,produces="application/json")
	    public @ResponseBody JTableResponseOptions listWorkflowType() {
			log.info("inside workflow.type.option.list ");
			JTableResponseOptions jTableResponseOptions;
			try {			
				
				List<JsonCommonOption> jsonWorkflowTypeOptions= new ArrayList<JsonCommonOption>();
				
				JsonCommonOption typeOption=new JsonCommonOption();
				typeOption.setValue(IDPAConstants.WORKFLOW_STATUS_TYPE);
				jsonWorkflowTypeOptions.add(typeOption);
				
				typeOption=new JsonCommonOption();
				typeOption.setValue(IDPAConstants.WORKFLOW_LIFE_CYCLE_TYPE);
				jsonWorkflowTypeOptions.add(typeOption);
				
				jTableResponseOptions = new JTableResponseOptions("OK", jsonWorkflowTypeOptions, false);
				
				return jTableResponseOptions;
		        } catch (Exception e) {
		        	jTableResponseOptions = new JTableResponseOptions("ERROR","Error in Workflow type");
		            log.error("JSON ERROR", e);
		        }
	        return jTableResponseOptions;
	    }
		
		@RequestMapping(value = "workflow.RAG.status.instance.detail", method = RequestMethod.POST, produces = "application/json")
		public @ResponseBody JTableResponse listWorkflowRAGStatusInstanceDetail(HttpServletRequest request,@RequestParam Integer engagementId,  @RequestParam Integer productId, @RequestParam Integer entityTypeId,@RequestParam Integer entityId,@RequestParam Integer entityTypeInstanceId,@RequestParam Integer userId,@RequestParam Integer roleId,@RequestParam String indicator,@RequestParam Integer categoryId,Integer jtStartIndex,Integer jtPageSize) {
			JTableResponse jTableResponse=null;
			Integer activityWorkpackageId=999999;//For Performance purpose passing dummy workpackageID
			try{
				List<JsonWorkflowSLAInstance> jsonWorkflowSLAInstanceList = new ArrayList<JsonWorkflowSLAInstance>();
				UserList user = (UserList) request.getSession().getAttribute("USER");
				if(entityTypeId == IDPAConstants.ENTITY_TASK_TYPE) {
					
					List<ActivityTask> listSLAIndicatorTaskList=workflowStatusPoliciesService.listWorkflowRAGStatusDetailForTask(productId, entityTypeId, entityId,entityTypeInstanceId,userId, roleId,indicator,jtStartIndex,jtPageSize);
					if(listSLAIndicatorTaskList!=null && listSLAIndicatorTaskList.size()>0) {
						List<JsonActivityTask> jsonActivityTasks = new ArrayList<JsonActivityTask>(); 
						HashMap<Integer, ActivityTask> activityTaskDeatils = new HashMap<Integer, ActivityTask>();
						for(ActivityTask activityTask: listSLAIndicatorTaskList){
							JsonActivityTask jsonActivityTask=new JsonActivityTask(activityTask);
							activityTaskDeatils.put(activityTask.getActivityTaskId(), activityTask);
							jsonActivityTasks.add(jsonActivityTask);
						}
						workflowStatusPoliciesService.setInstanceIndicators(engagementId,productId,activityWorkpackageId, IDPAConstants.ENTITY_TASK_TYPE, jsonActivityTasks, IDPAConstants.ENTITY_TASK,user, null, null, null,IDPAConstants.ENABLE_PENDING_WITH_COLUMN);
						for(JsonActivityTask jsonActivityTask : jsonActivityTasks){
							ActivityTask activityTask = activityTaskDeatils.get(jsonActivityTask.getActivityTaskId());
							JsonWorkflowSLAInstance jsonWorkflowSLAInstance=workflowStatusPoliciesService.setJsonWorkflowSLAInstance(jsonActivityTask, IDPAConstants.WORKFLOW_LIFE_CYCLE_TYPE);
							if(activityTask.getActivity() != null && activityTask.getActivity().getActivityWorkPackage() != null) {
								jsonWorkflowSLAInstance.setProductId(activityTask.getActivity().getActivityWorkPackage().getProductBuild().getProductVersion().getProductMaster().getProductId());
								jsonWorkflowSLAInstance.setProductName(activityTask.getActivity().getActivityWorkPackage().getProductBuild().getProductVersion().getProductMaster().getProductName());
							}	
							jsonWorkflowSLAInstance.setEntityTypeId(entityTypeId);
							jsonWorkflowSLAInstance.setEntityTypeName("TaskType");
							jsonWorkflowSLAInstance.setEntityId(activityTask.getActivityTaskType().getActivityTaskTypeId());
							jsonWorkflowSLAInstance.setEntityName(activityTask.getActivityTaskType().getActivityTaskTypeName());
							jsonWorkflowSLAInstanceList.add(jsonWorkflowSLAInstance);
						}
						activityTaskDeatils = null;
						jsonActivityTasks = null;
					}
				}else if(entityTypeId == IDPAConstants.ENTITY_ACTIVITY_TYPE) {
					List<Activity> listSLAIndicatorActivityList=workflowStatusPoliciesService.listWorkflowRAGStatusDetailForActivity(engagementId,productId, entityTypeId, entityId,entityTypeInstanceId,userId, roleId,indicator,categoryId,jtStartIndex,jtPageSize);
					if(listSLAIndicatorActivityList != null && listSLAIndicatorActivityList.size() > 0) {
						List<JsonActivity> jsonActivities = new ArrayList<JsonActivity>(); 
						HashMap<Integer, Activity> activityDetails = new HashMap<Integer, Activity>();
						for(Activity activity: listSLAIndicatorActivityList){
							JsonActivity jsonActivity=new JsonActivity(activity);
							activityDetails.put(activity.getActivityId(), activity);
							jsonActivities.add(jsonActivity);
						}
						 
						Date modifiedAfterDate = commonService.getModifiedAfterDate((Date)request.getSession().getAttribute("userLoginTime"), (Date)request.getSession().getAttribute("userLogoutTime"));
						workflowStatusPoliciesService.setInstanceIndicators(engagementId,productId,activityWorkpackageId, IDPAConstants.ENTITY_ACTIVITY_TYPE, jsonActivities, IDPAConstants.ENTITY_ACTIVITY_ID,user, modifiedAfterDate, null, null,IDPAConstants.ENABLE_PENDING_WITH_COLUMN);
						for(JsonActivity jsonActivity : jsonActivities){
							Activity activity = activityDetails.get(jsonActivity.getActivityId());
							JsonWorkflowSLAInstance jsonWorkflowSLAInstance=workflowStatusPoliciesService.setJsonWorkflowSLAInstance(jsonActivity, IDPAConstants.WORKFLOW_LIFE_CYCLE_TYPE);
							if(activity.getActivityWorkPackage() != null) {
								jsonWorkflowSLAInstance.setProductId(activity.getActivityWorkPackage().getProductBuild().getProductVersion().getProductMaster().getProductId());
								jsonWorkflowSLAInstance.setProductName(activity.getActivityWorkPackage().getProductBuild().getProductVersion().getProductMaster().getProductName());
							}	
							jsonWorkflowSLAInstance.setEntityTypeId(entityTypeId);
							jsonWorkflowSLAInstance.setEntityTypeName("Activities");
							jsonWorkflowSLAInstance.setEntityId(jsonActivity.getActivityMasterId());
							jsonWorkflowSLAInstance.setEntityName(jsonActivity.getActivityMasterName());
							jsonWorkflowSLAInstanceList.add(jsonWorkflowSLAInstance);
						}
						activityDetails = null;
						jsonActivities = null;
					}
				}else if(entityTypeId == IDPAConstants.ENTITY_ACTIVITY_WORKPACKAGE_ID) {
					List<ActivityWorkPackage> listSLAIndicatorActivityWorkPackages = workflowStatusPoliciesService.listWorkflowRAGStatusDetailForActivityWorkpackage(engagementId,productId, entityTypeId, entityId,entityTypeInstanceId,userId, roleId,indicator,jtStartIndex,jtPageSize);
					if(listSLAIndicatorActivityWorkPackages != null && listSLAIndicatorActivityWorkPackages.size() > 0) {
						List<JsonActivityWorkPackage> jsonActivityWorkPackages = new ArrayList<JsonActivityWorkPackage>();
						HashMap<Integer, ActivityWorkPackage> activityWorkpackageDetails = new HashMap<Integer, ActivityWorkPackage>();
						for(ActivityWorkPackage activityWorkPackage : listSLAIndicatorActivityWorkPackages){
							JsonActivityWorkPackage jsonActivityWorkPackage = new JsonActivityWorkPackage(activityWorkPackage);
							activityWorkpackageDetails.put(activityWorkPackage.getActivityWorkPackageId(), activityWorkPackage);
							jsonActivityWorkPackages.add(jsonActivityWorkPackage);
						}
						workflowStatusPoliciesService.setInstanceIndicators(engagementId,productId,null, IDPAConstants.ENTITY_ACTIVITY_WORKPACKAGE_ID, jsonActivityWorkPackages, IDPAConstants.ENTITY_ACTIVITY_WORKPACKAGE_ID,user, null, null, null,IDPAConstants.ENABLE_PENDING_WITH_COLUMN);
						for(JsonActivityWorkPackage jsonActivityWorkPackage : jsonActivityWorkPackages){
							ActivityWorkPackage activityWorkPackage = activityWorkpackageDetails.get(jsonActivityWorkPackage.getActivityWorkPackageId());
							JsonWorkflowSLAInstance jsonWorkflowSLAInstance = workflowStatusPoliciesService.setJsonWorkflowSLAInstance(jsonActivityWorkPackage, IDPAConstants.WORKFLOW_LIFE_CYCLE_TYPE);
							if(activityWorkPackage.getProductBuild() != null) {
								jsonWorkflowSLAInstance.setProductId(activityWorkPackage.getProductBuild().getProductVersion().getProductMaster().getProductId());
								jsonWorkflowSLAInstance.setProductName(activityWorkPackage.getProductBuild().getProductVersion().getProductMaster().getProductName());
							}	
							jsonWorkflowSLAInstance.setEntityTypeId(entityTypeId);
							jsonWorkflowSLAInstance.setEntityTypeName("Activity Workpackages");
							jsonWorkflowSLAInstance.setEntityId(null);
							jsonWorkflowSLAInstance.setEntityName(null);
							jsonWorkflowSLAInstanceList.add(jsonWorkflowSLAInstance);
						}
						activityWorkpackageDetails = null;
						jsonActivityWorkPackages = null;
					}
				}
				jTableResponse = new JTableResponse("OK", jsonWorkflowSLAInstanceList,jsonWorkflowSLAInstanceList.size());
				log.info("workflow.RAG.summary.instance.detail Completed");
			}catch(Exception e) {
				log.error("Error in listWorkflowRAGSummaryInstanceDetail",e);
			}
			return jTableResponse;
			}
		
		
	@RequestMapping(value="workflow.status.available.for.mapping",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableResponse listWorkflowStatusAvailableForMapping(@RequestParam Integer workflowId, @RequestParam Integer sourceStatusId, @RequestParam Integer jtStartIndex, @RequestParam Integer jtPageSize) {  
		log.debug("  ------------workflow.status.available.for.mapping-----------");
		JTableResponse jTableResponse;
		try {
			List<Object[]> workflowStatuses = workflowStatusService.getWorkflowStatusAvailableForMapping(workflowId, sourceStatusId, jtStartIndex, jtPageSize);
			ArrayList<HashMap<String, Object>> availableStatuses = new ArrayList<HashMap<String, Object>>();
			for (Object[] row : workflowStatuses) {
				HashMap<String, Object> sourceStatusDetail =new HashMap<String, Object>();
				sourceStatusDetail.put(IDPAConstants.ITEM_ID, (Integer)row[0]);
				sourceStatusDetail.put(IDPAConstants.ITEM_NAME, (String)row[1]);
				sourceStatusDetail.put("statusOrder", (Integer)row[2]);
				availableStatuses.add(sourceStatusDetail);					
			}				
			jTableResponse = new JTableResponse("OK", availableStatuses, availableStatuses.size());
		} catch (Exception e) {
	       	jTableResponse = new JTableResponse("ERROR","Error listing un mapped status!");
	       	log.error("JSON ERROR", e);
	    }	        
		return jTableResponse;
    }
	
	@RequestMapping(value="workflow.status.available.for.mapping.count",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableSingleResponse listWorkflowStatusAvailableForMappingCount(@RequestParam Integer workflowId, @RequestParam Integer sourceStatusId) {  
		log.debug("  ------------workflow.status.available.for.mapping.count-----------");
		JTableSingleResponse jTableSingleResponse;
		
		Integer availableWorkflowStatusCount = 0;		
		HashMap<String, Integer> availableWorkflowStatusCountMap =new HashMap<String, Integer>();
		try {
			availableWorkflowStatusCount = workflowStatusService.getWorkflowStatusAvailableForMappingCount(workflowId, sourceStatusId);
			availableWorkflowStatusCountMap.put("unMappedTCCount", availableWorkflowStatusCount);						
			jTableSingleResponse = new JTableSingleResponse("OK", availableWorkflowStatusCountMap);
        } catch (Exception e) {
            jTableSingleResponse = new JTableSingleResponse("ERROR","Unable to fetch available workflow status count!");
            log.error("Error in listWorkflowStatusAvailableForMappingCount - ", e);	 
        }
	        
        return jTableSingleResponse;
    }
	
	@RequestMapping(value="workflow.status.already.mapped",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableResponse listWorkflowStatusAlreadyMapped(@RequestParam Integer workflowId, @RequestParam Integer sourceStatusId, @RequestParam Integer jtStartIndex, @RequestParam Integer jtPageSize) {
		log.debug("  ------------workflow.status.already.for.mapped-----------");
		JTableResponse jTableResponse;
		try {
			List<Object[]> workflowStatuses = workflowStatusService.getWorkflowStatusAlreadyMapped(workflowId, sourceStatusId, jtStartIndex, jtPageSize);
			Integer totalRecordsAvailable = 0;//workflowStatusService.getTotalRecordsForDimensionOfProductPagination(productId, 1, dimensionTypeId, DimensionProduct.class);
			ArrayList<HashMap<String, Object>> availableStatuses = new ArrayList<HashMap<String, Object>>();
			for (Object[] row : workflowStatuses) {
				HashMap<String, Object> sourceStatusDetail =new HashMap<String, Object>();
				sourceStatusDetail.put(IDPAConstants.ITEM_ID, (Integer)row[0]);
				sourceStatusDetail.put(IDPAConstants.ITEM_NAME, (String)row[1]);	
				sourceStatusDetail.put("statusOrder", (Integer)row[2]);
				availableStatuses.add(sourceStatusDetail);					
			}			
			jTableResponse = new JTableResponse("OK", availableStatuses, totalRecordsAvailable);
	           
        } catch (Exception e) {
            jTableResponse = new JTableResponse("ERROR","Error fetching records!");
            log.error("JSON ERROR", e);
        }
	        
        return jTableResponse;
	}
	
	@RequestMapping(value="workflow.status.map.or.unmap",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableSingleResponse workflowStatusMappingOrUnmapping(@RequestParam Integer workflowId, @RequestParam Integer sourceStatusId, @RequestParam Integer targetStatusId, @RequestParam String maporunmap, HttpServletRequest request) {  
		log.debug("  ------------workflow.status.map.or.unmap-----------");
		JTableSingleResponse jTableSingleResponse;
		
		try {
			if(maporunmap == null || maporunmap.isEmpty() || maporunmap.equalsIgnoreCase("map")){
				String errorMessage = ValidationUtility.validateForWorkflowStatusMapping(workflowId, sourceStatusId, targetStatusId, workflowStatusService);			
				if (errorMessage != null && !errorMessage.trim().isEmpty()) {
					jTableSingleResponse = new JTableSingleResponse("ERROR",errorMessage);
					return jTableSingleResponse;
				}
			}
			
			WorkflowStatusMapping workflowStatusMapping = new WorkflowStatusMapping();
			
			WorkflowMaster workflowMaster = new WorkflowMaster();
			workflowMaster.setWorkflowId(workflowId);
			workflowStatusMapping.setWorkflow(workflowMaster);
			
			WorkflowStatus workflowSourceStatus = new WorkflowStatus();
			workflowSourceStatus.setWorkflowStatusId(sourceStatusId);
			workflowStatusMapping.setWorkflowSourceStatusId(workflowSourceStatus);
			
			WorkflowStatus workflowTargetStatus = new WorkflowStatus();
			workflowTargetStatus.setWorkflowStatusId(targetStatusId);
			workflowStatusMapping.setWorkflowTargetStatusId(workflowTargetStatus);

			workflowStatusMapping.setMappedOn(new Date());
			
			UserList mappedBy = (UserList) request.getSession().getAttribute("USER");
			workflowStatusMapping.setMappedBy(mappedBy);
			
			workflowStatusService.workflowStatusMappingOrUnmapping(workflowStatusMapping, maporunmap);			
	        jTableSingleResponse = new JTableSingleResponse("OK", "Operation successfull");
	    } catch (Exception e) {
	        jTableSingleResponse = new JTableSingleResponse("ERROR","Error while adding dimension to product!");
	        log.error("JSON ERROR", e);
	    }	        
        return jTableSingleResponse;		
    }
	
}
