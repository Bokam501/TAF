package com.hcl.ilcm.workflow.controller;

import java.io.InputStream;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

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
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import com.hcl.atf.taf.constants.IDPAConstants;
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
import com.hcl.atf.taf.model.json.jtable.JTableResponse;
import com.hcl.atf.taf.model.json.jtable.JTableResponseOptions;
import com.hcl.atf.taf.model.json.jtable.JTableSingleResponse;
import com.hcl.atf.taf.service.ActivityService;
import com.hcl.atf.taf.service.CommonService;
import com.hcl.atf.taf.service.TestCaseService;
import com.hcl.ilcm.workflow.model.WorkflowEvent;
import com.hcl.ilcm.workflow.model.WorkflowMaster;
import com.hcl.ilcm.workflow.model.WorkflowMasterEntityMapping;
import com.hcl.ilcm.workflow.model.WorkflowStatus;
import com.hcl.ilcm.workflow.model.json.JsonWorkflowEvent;
import com.hcl.ilcm.workflow.model.json.JsonWorkflowMasterEntityMapping;
import com.hcl.ilcm.workflow.model.json.JsonWorkflowSLAInstance;
import com.hcl.ilcm.workflow.model.json.JsonWorkflowStatus;
import com.hcl.ilcm.workflow.service.ConfigurationWorkFlowService;
import com.hcl.ilcm.workflow.service.WorkflowEventService;
import com.hcl.ilcm.workflow.service.WorkflowMasterService;
import com.hcl.ilcm.workflow.service.WorkflowStatusPolicyService;
import com.hcl.ilcm.workflow.service.WorkflowStatusService;

/**
 * @author silambarasur
 *
 */
@Controller
public class ConfigurationWorkFlowController {
	private static final Log log = LogFactory
			.getLog(ConfigurationWorkFlowController.class);
	
	@Autowired
	private ConfigurationWorkFlowService configurationWorkFlowService;
	
	@Autowired
	private WorkflowStatusService workflowStatusService;
	
	@Autowired
	private ActivityService activityService;
	
	@Autowired
	private WorkflowEventService workflowEventService;
	
	@Autowired
	private TestCaseService testCaseService;
	
	@Autowired
	private WorkflowStatusPolicyService workflowStatusPolicyService;
	
	@Autowired
	private WorkflowMasterService workflowMasterService;
	
	@Autowired
	private CommonService commonService;
	
	@Autowired
	private ExcelTestDataIntegrator excelTestDataIntegrator;	
	
	private static Integer defaultEngagementId=0;

	@RequestMapping(value = "workflow.tasks.pending.my.action", method = RequestMethod.POST, produces = "application/json")
	public @ResponseBody JTableResponse getActivityTasksForUser(HttpServletRequest request, @RequestParam int productId, @RequestParam String entityLevel, @RequestParam int entityLevelId, @RequestParam Integer jtStartIndex,@RequestParam Integer jtPageSize) {	
		log.info("Fetching pending ActivityTasks For User ");
		JTableResponse jTableResponse;
		try {
	        
			List<JsonActivityTask> jsonActivityTaskList = new ArrayList<>();
			List<ActivityTask> activityTaskList=null;
		
			UserList user = (UserList) request.getSession().getAttribute("USER");
			activityTaskList=configurationWorkFlowService.listActivityTasksForUser(productId, entityLevel, entityLevelId, user.getUserId(), user.getUserRoleMaster().getUserRoleId(), jtStartIndex, jtPageSize);
			
			if(activityTaskList!=null && activityTaskList.size()>0) {
				for(ActivityTask activityTask: activityTaskList){
					JsonActivityTask jsonActivityTask=new JsonActivityTask(activityTask);
					jsonActivityTaskList.add(jsonActivityTask);	
				}
				workflowStatusPolicyService.setInstanceIndicators(defaultEngagementId,productId,null, IDPAConstants.ENTITY_TASK_TYPE, jsonActivityTaskList, IDPAConstants.ENTITY_TASK,user, null, null, null,IDPAConstants.ENABLE_PENDING_WITH_COLUMN);
			}
			Collections.sort(jsonActivityTaskList, new Comparator<JsonActivityTask>() {
				@Override
				public int compare(JsonActivityTask task, JsonActivityTask task2) {
					if(task.getRemainingHours() != null && task2.getRemainingHours() != null){
						return task.getRemainingHours().compareTo(task2.getRemainingHours());
					}else {
						return 0;
					}
				}
			});
			
			@SuppressWarnings("unchecked")
			List<JsonActivityTask> jsonActivityTaskReturnList = (List<JsonActivityTask>) workflowStatusPolicyService.getPaginationListFromFullList(jsonActivityTaskList, jtStartIndex, jtPageSize);
			
			jTableResponse = new JTableResponse("OK", jsonActivityTaskReturnList, jsonActivityTaskList.size());
		} catch (Exception e) {
			jTableResponse = new JTableResponse("ERROR",
					"Error fetching pending ActivityTasks For User!");
			log.error("JSON ERROR Fetching pending ActivityTasks For User", e);
		}
		return jTableResponse;
	}
	
	
	@RequestMapping(value="workflow.status.master.option.list",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableResponse getWorkFlowUserActionStatusOptions(@RequestParam Integer productId,@RequestParam Integer entityTypeId, @RequestParam Integer entityId, @RequestParam Integer currentStatusId) {

		log.debug("workflow.status.master.option.list start");
		JTableResponse jTableResponse;		 
		List<Map<String, String>> workflowStatusResponseOptions = new ArrayList<Map<String, String>>();
		try {	   
			List<WorkflowStatus> workflowStatusOptions = workflowStatusService.getPossibleWorkflowStatusForAction(currentStatusId);
			if (workflowStatusOptions != null) {
				Map<String, String> workflowCurrentStatusDetails = new LinkedHashMap<String, String>();
				for(WorkflowStatus workflowStatus : workflowStatusOptions){
					if(currentStatusId == workflowStatus.getWorkflowStatusId()){
						if(workflowStatus.getWorkflowStatusCategory() != null){
							workflowCurrentStatusDetails.put("Category", workflowStatus.getWorkflowStatusCategory().getWorkflowStatusCategoryName());
						}else{
							workflowCurrentStatusDetails.put("Category", "");
						}
						continue;
					}
					Map<String, String> workflowStatusDetails = new LinkedHashMap<String, String>();
					workflowStatusDetails.put("Id", workflowStatus.getWorkflowStatusId()+"");
					workflowStatusDetails.put("Value", workflowStatus.getWorkflowStatusName());
					workflowStatusDetails.put("DisplayText", workflowStatus.getWorkflowStatusDisplayName());
					if(workflowStatus.getWorkflowStatusCategory() != null){
						workflowStatusDetails.put("Category", workflowStatus.getWorkflowStatusCategory().getWorkflowStatusCategoryName());
					}else{
						workflowStatusDetails.put("Category", "");
					}
					workflowStatusResponseOptions.add(workflowStatusDetails);
				}
				
				if(!workflowCurrentStatusDetails.isEmpty()){
					workflowCurrentStatusDetails.put("Id", "0");
					workflowCurrentStatusDetails.put("Value", "---");
					workflowCurrentStatusDetails.put("DisplayText", "---");
					workflowStatusResponseOptions.add(workflowCurrentStatusDetails);
				}
				
			}
			jTableResponse = new JTableResponse("OK", workflowStatusResponseOptions);
			return jTableResponse;
        } catch (Exception e) {
        	jTableResponse = new JTableResponse("ERROR","Please map status to the product and Entity Type.");
            log.error("JSON ERROR", e);
        }	
		jTableResponse = new JTableResponse("ERROR","Please map status to the product and Entity Type.");
		log.info("jTableResponseOptions success");
		return jTableResponse; 
    }
	
	@RequestMapping(value="workflow.getAllStatus.master.option.list",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableResponseOptions getWorkFlowAllStatusOptions(@RequestParam Integer productId, Integer entityTypeId, @RequestParam Integer entityId) {

		log.debug("workflow.getAllStatus.master.option.list");
		JTableResponseOptions jTableResponseOptions;		 
		List<JsonWorkflowStatus> jsonStatusList = new ArrayList<JsonWorkflowStatus>();
		try {	  
			Integer activeState=1;
			Integer entityInstanceId = null;
			WorkflowMaster workflowMaster = workflowMasterService.getWorkflowForEntityTypeOrInstance(productId, entityTypeId, entityId, entityInstanceId, activeState);
			if(workflowMaster != null) {
				List<WorkflowStatus> workflowStatusOptions = workflowStatusService.getWorkFlowStatusList(workflowMaster.getWorkflowId(), null);
				if (workflowStatusOptions != null) {
					JsonWorkflowStatus jsonStatus =  null;
					for (WorkflowStatus status : workflowStatusOptions) {
						jsonStatus = new JsonWorkflowStatus(status);
						jsonStatusList.add(jsonStatus);
					}
				}
			}
			jTableResponseOptions = new JTableResponseOptions("OK", jsonStatusList, true);
			
			return jTableResponseOptions;
        } catch (Exception e) {
        	jTableResponseOptions = new JTableResponseOptions("ERROR","Please map status to the product and Entity Type.");
            log.error("JSON ERROR", e);
        }	
		jTableResponseOptions = new JTableResponseOptions("ERROR","Please map status to the product and Entity Type.");
		log.info("jTableResponseOptions success");
		return jTableResponseOptions; 
    }

	@RequestMapping(value="workflow.life.cycle.stages.options",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableResponseOptions getWorkFlowLifeCycleStagesOptions(@RequestParam Integer entityTypeId, @RequestParam Integer entityId, @RequestParam Integer entityInstanceId) {
		log.debug("workflow.life.cycle.stages.options");
		JTableResponseOptions jTableResponseOptions;		 
		List<JsonWorkflowStatus> jsonStatusList = new ArrayList<JsonWorkflowStatus>();
		try {	   
			Integer activeState = 1;
			Integer productId = null;
			WorkflowMaster workflowMaster = workflowMasterService.getWorkflowForEntityTypeOrInstance(productId, entityTypeId, entityId, entityInstanceId, activeState);
			if(workflowMaster != null) {
				List<WorkflowStatus> workflowStatusList = workflowStatusService.getWorkFlowStatusList(workflowMaster.getWorkflowId(), null);
				if (workflowStatusList != null) {
					for (WorkflowStatus status : workflowStatusList) {
						if(status.getIsLifeCycleStage() != null && status.getIsLifeCycleStage() == 1){
							JsonWorkflowStatus jsonStatus = new JsonWorkflowStatus(status);
							jsonStatusList.add(jsonStatus);
						}
					}
				}
			}
			if(jsonStatusList ==null || jsonStatusList.size() == 0) {
				JsonWorkflowStatus jsonStatus = new JsonWorkflowStatus();
				jsonStatus.setWorkflowStatusId(0);
				jsonStatus.setWorkflowStatusName("--");
				jsonStatus.setWorkflowStatusDisplayName("--");
				jsonStatusList.add(jsonStatus);
			}
			jTableResponseOptions = new JTableResponseOptions("OK", jsonStatusList, true);
			
			return jTableResponseOptions;
        } catch (Exception e) {
        	jTableResponseOptions = new JTableResponseOptions("ERROR","Error fetching life cycle stages");
            log.error("JSON ERROR", e);
        }	
		jTableResponseOptions = new JTableResponseOptions("ERROR","Unable to fetch life cycle stages");
		log.info("jTableResponseOptions success");
		return jTableResponseOptions; 
    }

	@RequestMapping(value="workflow.event.tracker.add",method=RequestMethod.POST ,produces="application/json")
	public @ResponseBody JTableSingleResponse updateWorkflowEventTracker(HttpServletRequest request,					
			@RequestParam Integer productId,
			@RequestParam Integer entityId, 
			@RequestParam Integer entityTypeId,				
			@RequestParam Integer primaryStatusId,
			@RequestParam Integer secondaryStatusId,
			@RequestParam Integer effort,	
			@RequestParam String comments,				
			@RequestParam Integer sourceStatusId,
			@RequestParam String approveAllEntityInstanceIds,
			@RequestParam Integer entityInstanceId,
			@RequestParam String attachmentIds,
			@RequestParam String actionDate,@RequestParam Integer actualSize){
		
		JTableSingleResponse jTableSingleResponse = null;
		try {
				UserList user = (UserList) request.getSession().getAttribute("USER");
				if(entityInstanceId != null && entityInstanceId == 0){
					entityInstanceId = null;
				}
				if(entityId != null && entityId == 0){
					entityId = null;
				}
				Date date = null;
				if(actionDate != null){
					SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
					date = simpleDateFormat.parse(actionDate);
				}
				List<Integer> possibleIds = new ArrayList<Integer>(); 
				String errorMessage = workflowEventService.addWorkflowEvent(productId, approveAllEntityInstanceIds, user, entityId, entityTypeId, primaryStatusId, secondaryStatusId, effort, comments, sourceStatusId, entityInstanceId, attachmentIds, possibleIds, null, date,actualSize);
				
				
				if(errorMessage != null && !errorMessage.isEmpty()){
					jTableSingleResponse = new JTableSingleResponse("ERROR", errorMessage);
				}else{
					jTableSingleResponse = new JTableSingleResponse("OK");
				}
	    } 
		catch (Exception e) {
            jTableSingleResponse = new JTableSingleResponse("ERROR","Unable to perform Activity effort Tracker!");
            log.error("JSON ERROR", e);	            
        }
	    return jTableSingleResponse;
    }
	
	
	@RequestMapping(value = "workflow.testcases.pending.my.action", method = RequestMethod.POST, produces = "application/json")
	public @ResponseBody JTableResponse getTestCasesForUser(HttpServletRequest request,@RequestParam Integer productId, @RequestParam String entityLevel, @RequestParam int entityLevelId, @RequestParam Integer jtStartIndex,@RequestParam Integer jtPageSize) {	
		log.info("Fetching TestCases For User ");
		JTableResponse jTableResponse;
		try {
			List<JsonTestCaseList> jsonTestCases = new ArrayList<>();
			List<TestCaseList> testCaseList=null;
		
			UserList user = (UserList) request.getSession().getAttribute("USER");
			testCaseList=configurationWorkFlowService.listTestcasesForUser(productId, entityLevel, entityLevelId, user.getUserId(),user.getUserRoleMaster().getUserRoleId(),jtStartIndex,jtPageSize);
			
			if(testCaseList!=null && testCaseList.size()>0) {
				for(TestCaseList testcase: testCaseList){
					JsonTestCaseList jsonTestCaseList=new JsonTestCaseList(testcase);
					jsonTestCases.add(jsonTestCaseList);
				}
				workflowStatusPolicyService.setInstanceIndicators(defaultEngagementId,productId,null, IDPAConstants.ENTITY_TEST_CASE_ID, jsonTestCases, IDPAConstants.ENTITY_TEST_CASE_ID,user, null, null, null,IDPAConstants.ENABLE_PENDING_WITH_COLUMN);
			}
			Collections.sort(jsonTestCases, new Comparator<JsonTestCaseList>() {
				@Override
				public int compare(JsonTestCaseList testCase, JsonTestCaseList testCase2) {
					if(testCase.getRemainingHours() != null && testCase2.getRemainingHours() != null){
						//return testCase.getRemainingHours() - testCase2.getRemainingHours();
						return testCase.getRemainingHours().compareTo(testCase2.getRemainingHours());
					}else {
						return 0;
					}
				}
			});
			@SuppressWarnings("unchecked")
			List<JsonTestCaseList> jsonTestCaseReturnList = (List<JsonTestCaseList>) workflowStatusPolicyService.getPaginationListFromFullList(jsonTestCases, jtStartIndex, jtPageSize);
			
			jTableResponse = new JTableResponse("OK", jsonTestCaseReturnList, jsonTestCases.size());
		} catch (Exception e) {
			jTableResponse = new JTableResponse("ERROR",
					"Error fetching TestCases For User!");
			log.error("JSON ERROR Fetching TestCases For User", e);
		}
		return jTableResponse;
	}
	
	@RequestMapping(value = "workflow.event.tracker.list", method = RequestMethod.POST, produces = "application/json")
	public @ResponseBody JTableResponse getWorkflowEventTrackerList(@RequestParam Integer entityTypeId,@RequestParam Integer entityInstanceId) {
		log.debug("inside workflow.event.tracker.list");
		JTableResponse jTableResponse;
		
		try {
			int initializationLevel=1;
			List<JsonWorkflowEvent> jsonWorkflowEvent = new ArrayList<JsonWorkflowEvent>();
			List<WorkflowEvent> listWorkflowEvents = workflowEventService.listWorkflowEventByEntityTypeAndEnityInstanceId(entityTypeId,entityInstanceId,initializationLevel);
			
			if(listWorkflowEvents != null && listWorkflowEvents.size()>0){
				List<Object[]> attachmentCountDetails = commonService.getAttachmentCountOfEntity(IDPAConstants.ENTITY_WORKFLOW_EVENT_ID);
				for (WorkflowEvent workflowEvent : listWorkflowEvents) {
					JsonWorkflowEvent jsonEntityEffortTracker = new JsonWorkflowEvent(workflowEvent);
					Integer attachmentCount = commonService.getCountFromAttachmentCountDetails(attachmentCountDetails, jsonEntityEffortTracker.getworkflowEntityEffortTrackerId());
					jsonEntityEffortTracker.setAttachmentCount(attachmentCount);
					jsonWorkflowEvent.add(jsonEntityEffortTracker);
				}
			}
			jTableResponse = new JTableResponse("OK", jsonWorkflowEvent);
			log.debug("inside process fetching jsonCommentsTrackerList records ");
		} catch (Exception e) {
			jTableResponse = new JTableResponse("ERROR","Error fetching CommentsTrackerrecords!");
			log.error("JSON ERROR", e);
		}
		return jTableResponse;
	}
	
	@RequestMapping(value = "workflow.activities.pending.my.action", method = RequestMethod.POST, produces = "application/json")
	public @ResponseBody JTableResponse getActivitiesForUser(HttpServletRequest request, @RequestParam int productId, @RequestParam String entityLevel, @RequestParam int entityLevelId, @RequestParam Integer jtStartIndex,@RequestParam Integer jtPageSize) {	
		log.info("Fetching Activities For User");
		JTableResponse jTableResponse;
		try {	        
			List<JsonActivity> jsonActivityList = new ArrayList<>();
			UserList user = (UserList) request.getSession().getAttribute("USER");
			List<Activity> activityList = configurationWorkFlowService.listActivitiesForUser(productId, entityLevel, entityLevelId, user.getUserId(), user.getUserRoleMaster().getUserRoleId(), jtStartIndex, jtPageSize);
			
			if(activityList!=null && activityList.size()>0) {
				for(Activity activity: activityList){
					JsonActivity jsonActivity = new JsonActivity(activity);
					jsonActivityList.add(jsonActivity);	
				}
				Date modifiedAfterDate = commonService.getModifiedAfterDate((Date)request.getSession().getAttribute("userLoginTime"), (Date)request.getSession().getAttribute("userLogoutTime"));
				workflowStatusPolicyService.setInstanceIndicators(defaultEngagementId,productId,null, IDPAConstants.ENTITY_ACTIVITY_TYPE, jsonActivityList, IDPAConstants.ENTITY_ACTIVITY_ID,user, modifiedAfterDate, null, null,IDPAConstants.ENABLE_PENDING_WITH_COLUMN);
			}
			Collections.sort(jsonActivityList, new Comparator<JsonActivity>() {
				@Override
				public int compare(JsonActivity activity1, JsonActivity activity2) {
					if(activity1.getRemainingHours() != null && activity2.getRemainingHours() != null){
						return activity1.getRemainingHours().compareTo(activity2.getRemainingHours());
					}else {
						return 0;
					}
				}
			});
			
			@SuppressWarnings("unchecked")
			List<JsonActivity> jsonActivityReturnList = (List<JsonActivity>) workflowStatusPolicyService.getPaginationListFromFullList(jsonActivityList, jtStartIndex, jtPageSize);
			
			jTableResponse = new JTableResponse("OK", jsonActivityReturnList, jsonActivityList.size());
		} catch (Exception e) {
			jTableResponse = new JTableResponse("ERROR",
					"Error fetching Activities For User!");
			log.error("JSON ERROR Fetching Activities For User", e);
		}
		return jTableResponse;
	}
	
	@RequestMapping(value = "workflow.activity.workpackages.pending.my.action", method = RequestMethod.POST, produces = "application/json")
	public @ResponseBody JTableResponse getActivityWorkpackagesForUser(HttpServletRequest request, @RequestParam int productId, @RequestParam String entityLevel, @RequestParam int entityLevelId, @RequestParam int jtStartIndex,@RequestParam int jtPageSize) {	
		log.info("workflow.activities.pending.my.action start");
		JTableResponse jTableResponse;
		try {
	        
			List<JsonActivityWorkPackage> jsonActivityWorkPackages = new ArrayList<JsonActivityWorkPackage>();
			List<ActivityWorkPackage> activityWorkPackages = null;
		
			UserList user = (UserList) request.getSession().getAttribute("USER");
			activityWorkPackages = configurationWorkFlowService.listActivityWorkpackagesForUser(productId, entityLevel, entityLevelId, user.getUserId(), user.getUserRoleMaster().getUserRoleId(), jtStartIndex, jtPageSize);
			
			if(activityWorkPackages != null && activityWorkPackages.size() > 0) {
				for(ActivityWorkPackage activityWorkPackage : activityWorkPackages){
					JsonActivityWorkPackage jsonActivityWorkPackage = new JsonActivityWorkPackage(activityWorkPackage);
					jsonActivityWorkPackages.add(jsonActivityWorkPackage);	
				}
				workflowStatusPolicyService.setInstanceIndicators(defaultEngagementId,productId,null, IDPAConstants.ENTITY_ACTIVITY_WORKPACKAGE_ID, jsonActivityWorkPackages, IDPAConstants.ENTITY_ACTIVITY_WORKPACKAGE_ID,user, null, null, null,IDPAConstants.ENABLE_PENDING_WITH_COLUMN);
			}
			Collections.sort(jsonActivityWorkPackages, new Comparator<JsonActivityWorkPackage>() {
				@Override
				public int compare(JsonActivityWorkPackage activityWorkPackage1, JsonActivityWorkPackage activityWorkPackage2) {
					if(activityWorkPackage1.getRemainingHours() != null && activityWorkPackage2.getRemainingHours() != null){
						return activityWorkPackage1.getRemainingHours().compareTo(activityWorkPackage2.getRemainingHours());
					}else {
						return 0;
					}
				}
			});
			
			@SuppressWarnings("unchecked")
			List<JsonActivityWorkPackage> jsonActivityWorkpackageReturnList = (List<JsonActivityWorkPackage>) workflowStatusPolicyService.getPaginationListFromFullList(jsonActivityWorkPackages, jtStartIndex, jtPageSize);
			
			jTableResponse = new JTableResponse("OK", jsonActivityWorkpackageReturnList, jsonActivityWorkPackages.size());
			log.info("workflow.activity.workpackages.pending.my.action Completed");
		} catch (Exception e) {
			jTableResponse = new JTableResponse("ERROR",
					"Error fetching records!");
			log.error("JSON ERROR", e);
		}
		return jTableResponse;
	}
	
	@RequestMapping(value="workflow.entity.secondary.status.master.option.list",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableResponseOptions listWorkflowEntityBasedSecondaryStatusMaster(@RequestParam Integer productId,@RequestParam Integer entityTypeId,@RequestParam Integer statusId) {
		log.info("inside workflow.entity.secondary.status.master.option.list "+statusId);
		JTableResponseOptions jTableResponseOptions;
		try {			
			
			List<WorkflowStatus>  workflowSecondayStatusList=workflowStatusService.getSecondaryStatusByParentId(productId, entityTypeId, statusId);
			List<JsonWorkflowStatus> jsonStatusList = new ArrayList<JsonWorkflowStatus>();
			if (workflowSecondayStatusList != null && workflowSecondayStatusList.size()>0) {
				JsonWorkflowStatus jsonStatus =  null;
				for (WorkflowStatus status : workflowSecondayStatusList) {
					jsonStatus = new JsonWorkflowStatus(status);
					jsonStatusList.add(jsonStatus);
				}
			}
			jTableResponseOptions = new JTableResponseOptions("OK", jsonStatusList, true);
			
			return jTableResponseOptions;
	        } catch (Exception e) {
	        	jTableResponseOptions = new JTableResponseOptions("ERROR","Secondary Status Not Mapped!");
	            log.error("JSON ERROR", e);
	        }
        return jTableResponseOptions;
    }
	
	@RequestMapping(value = "workflow.product.entity.mapping.list", method = RequestMethod.POST, produces = "application/json")
	public @ResponseBody JTableResponse getWorkflowProductEntityMappingList(HttpServletRequest request, @RequestParam int productId) {	
		log.info("Inside workflow.product.entity.mapping.list with product id - "+productId);
		JTableResponse jTableResponse;
		try {
			List<WorkflowMasterEntityMapping> workflowMasterEntityMappings = null;
			List<JsonWorkflowMasterEntityMapping> jsonWorkflowMasterEntityMappings = new ArrayList<JsonWorkflowMasterEntityMapping>();
			workflowMasterEntityMappings = configurationWorkFlowService.getWorkflowEntitiesForProduct(productId);
			if(workflowMasterEntityMappings != null && workflowMasterEntityMappings.size() > 0) {
				for(WorkflowMasterEntityMapping workflowMasterEntityMapping : workflowMasterEntityMappings){
					JsonWorkflowMasterEntityMapping jsonWorkflowMasterEntityMapping = new JsonWorkflowMasterEntityMapping(workflowMasterEntityMapping);
					jsonWorkflowMasterEntityMappings.add(jsonWorkflowMasterEntityMapping);
				}
			}
			jTableResponse = new JTableResponse("OK", jsonWorkflowMasterEntityMappings);
			log.info("workflow.product.entity.mapping.list Completed");
		} catch (Exception e) {
			jTableResponse = new JTableResponse("ERROR", "Error fetching records!");
			log.error("JSON ERROR", e);
		}
		return jTableResponse;
	}
	
	@RequestMapping(value = "workflow.product.entity.mapping.add", method = RequestMethod.POST, produces = "application/json")
	public @ResponseBody JTableSingleResponse addWorkflowProductEntityMapping(@ModelAttribute JsonWorkflowMasterEntityMapping jsonWorkflowMasterEntityMapping, BindingResult result, HttpServletRequest request){	
		log.info("Inside workflow.product.entity.mapping.add");
		JTableSingleResponse jTableSingleResponse;
		try {
			
			if(jsonWorkflowMasterEntityMapping.getProductId() == null || jsonWorkflowMasterEntityMapping.getProductId() == 0){
				jTableSingleResponse = new JTableSingleResponse("ERROR","Please choose a product");
				return jTableSingleResponse;	
			}
			if(jsonWorkflowMasterEntityMapping.getWorkflowId() == null || jsonWorkflowMasterEntityMapping.getWorkflowId() == 0){
				jTableSingleResponse = new JTableSingleResponse("ERROR","No workflow is chosen or available to map with entity");
				return jTableSingleResponse;
			}
			
			WorkflowMasterEntityMapping workflowMasterEntityMapping = jsonWorkflowMasterEntityMapping.getWorkflowMasterEntityMapping();
			
			boolean isMappingAleadyExist = configurationWorkFlowService.checkWorkflowEntityMappingAlreadyExist(jsonWorkflowMasterEntityMapping.getWorkflowEntityMappingId(), jsonWorkflowMasterEntityMapping.getProductId(), jsonWorkflowMasterEntityMapping.getWorkflowId(), jsonWorkflowMasterEntityMapping.getEntityTypeId(), jsonWorkflowMasterEntityMapping.getEntityId());
			if(!isMappingAleadyExist){
				boolean isEligibleForDefault = configurationWorkFlowService.checkEligibilityForDefault(jsonWorkflowMasterEntityMapping.getWorkflowEntityMappingId(), jsonWorkflowMasterEntityMapping.getProductId(), jsonWorkflowMasterEntityMapping.getEntityTypeId(), jsonWorkflowMasterEntityMapping.getEntityId());
				if(isEligibleForDefault){
					workflowMasterEntityMapping.setIsDefault(1);
				}else{
					workflowMasterEntityMapping.setIsDefault(0);
				}
				configurationWorkFlowService.addWorkflowProductEntityMapping(workflowMasterEntityMapping);
				if(workflowMasterEntityMapping.getWorkflow() != null && workflowMasterEntityMapping.getWorkflow().getWorkflowId() != null){
					workflowStatusPolicyService.setEntityStatusPoliciesFromWorkflowPolicies(workflowMasterEntityMapping.getProduct().getProductId(), workflowMasterEntityMapping.getEntityType().getEntitymasterid(), workflowMasterEntityMapping.getEntityId(), workflowMasterEntityMapping.getWorkflow().getWorkflowId());
				}
				jTableSingleResponse = new JTableSingleResponse("OK", new JsonWorkflowMasterEntityMapping(workflowMasterEntityMapping));
				log.info("workflow.product.entity.mapping.add Completed");
			}else{
				jTableSingleResponse = new JTableSingleResponse("ERROR", "Workflow entity mapping already exists.");
				log.info("Workflow entity mapping already exists.");
			}
			
		} catch (Exception e) {
			jTableSingleResponse = new JTableSingleResponse("ERROR", "Unable to add workflow entity mapping ");
			log.error("Unable to add workflow entity mapping in getWorkflowProductEntityMappingAdd ", e);
		}
		return jTableSingleResponse;
	}
	
	@RequestMapping(value = "workflow.product.entity.mapping.update", method = RequestMethod.POST, produces = "application/json")
	public @ResponseBody JTableSingleResponse updateWorkflowProductEntityMapping(@ModelAttribute JsonWorkflowMasterEntityMapping jsonWorkflowMasterEntityMapping, BindingResult result, HttpServletRequest request){	
		log.info("Inside workflow.product.entity.mapping.update");
		JTableSingleResponse jTableSingleResponse;
		try {
			if(result.hasErrors()){
				jTableSingleResponse = new JTableSingleResponse("ERROR","Invalid form!");
				return jTableSingleResponse;
			}
			WorkflowMasterEntityMapping workflowMasterEntityMapping = jsonWorkflowMasterEntityMapping.getWorkflowMasterEntityMapping();
			boolean isMappingAleadyExist = configurationWorkFlowService.checkWorkflowEntityMappingAlreadyExist(jsonWorkflowMasterEntityMapping.getWorkflowEntityMappingId(), jsonWorkflowMasterEntityMapping.getProductId(), jsonWorkflowMasterEntityMapping.getWorkflowId(), jsonWorkflowMasterEntityMapping.getEntityTypeId(), jsonWorkflowMasterEntityMapping.getEntityId());
			if(!isMappingAleadyExist){
				if(jsonWorkflowMasterEntityMapping.getIsDefault() != null && jsonWorkflowMasterEntityMapping.getIsDefault() == 1){
					configurationWorkFlowService.updateIsDefaultOfEntityMapping(jsonWorkflowMasterEntityMapping.getWorkflowEntityMappingId(), jsonWorkflowMasterEntityMapping.getProductId(), jsonWorkflowMasterEntityMapping.getEntityTypeId(), jsonWorkflowMasterEntityMapping.getEntityId());
				}else{
					boolean isEligibleForDefault = configurationWorkFlowService.checkEligibilityForDefault(jsonWorkflowMasterEntityMapping.getWorkflowEntityMappingId(), jsonWorkflowMasterEntityMapping.getProductId(), jsonWorkflowMasterEntityMapping.getEntityTypeId(), jsonWorkflowMasterEntityMapping.getEntityId());
					if(isEligibleForDefault){
						jTableSingleResponse = new JTableSingleResponse("ERROR", "Unable to update, no default mapping exist for the entity");
						return jTableSingleResponse;
					}
				}
				configurationWorkFlowService.addWorkflowProductEntityMapping(workflowMasterEntityMapping);
				
				jTableSingleResponse = new JTableSingleResponse("OK", new JsonWorkflowMasterEntityMapping(workflowMasterEntityMapping));
				log.info("workflow.product.entity.mapping.update Completed");
			}else{
				jTableSingleResponse = new JTableSingleResponse("ERROR", "Workflow entity mapping already exists.");
				log.info("Workflow entity mapping already exists.");
			}
			
		} catch (Exception e) {
			jTableSingleResponse = new JTableSingleResponse("ERROR", "Unable to add workflow entity mapping ");
			log.error("Unable to update workflow entity mapping in updateWorkflowProductEntityMapping ", e);
		}
		return jTableSingleResponse;
	}
	
	@RequestMapping(value = "workflow.change.instance.workflow.mapping", method = RequestMethod.POST, produces = "application/json")
	public @ResponseBody JTableSingleResponse changeInstanceWorkflowMapping(
			@RequestParam Integer productId,
			@RequestParam Integer entityTypeId,
			@RequestParam Integer entityId,
			@RequestParam Integer entityInstanceId,
			@RequestParam Integer workflowId,
			HttpServletRequest request){	
		log.info("Inside workflow.change.instance.workflow.mapping");
		JTableSingleResponse jTableSingleResponse;
		try {
			UserList user = (UserList) request.getSession().getAttribute("USER");
			jTableSingleResponse = configurationWorkFlowService.changeInstnaceWorkflowMapping(productId, entityTypeId, entityId, entityInstanceId, workflowId, user);
		} catch (Exception e) {
			jTableSingleResponse = new JTableSingleResponse("ERROR", "Unable to change instance workflow mapping ");
			log.error("Unable to update instance workflow mapping in changeInstanceWorkflowMapping ", e);
		}
		return jTableSingleResponse;
	}
	
	@RequestMapping(value = "workflow.pending.my.action.count", method = RequestMethod.POST, produces = "application/json")
	public @ResponseBody JTableResponse getWorkflowMyActionCounts(HttpServletRequest request, @RequestParam Integer productId,@RequestParam Integer entityTypeId) {	
		log.info("workflow.pending.my.action.count start");
		JTableResponse jTableResponse;
		try {
			UserList user = (UserList) request.getSession().getAttribute("USER");
			List<Object[]> myPendingCountDetailObjects = configurationWorkFlowService.getWorkflowMyActionCounts(productId, user.getUserId(), user.getUserRoleMaster().getUserRoleId(),entityTypeId);
			List<HashMap<String, Object>> myPendingCountDetails = new ArrayList<HashMap<String, Object>>();
			if(myPendingCountDetailObjects != null && myPendingCountDetailObjects.size() > 0){
				for(Object[] myPendingCountDetailObj : myPendingCountDetailObjects){
					HashMap<String, Object> myPendingCountDetail =new HashMap<String, Object>();
					myPendingCountDetail.put((String)myPendingCountDetailObj[1], ((BigInteger)myPendingCountDetailObj[0]).intValue());
					myPendingCountDetails.add(myPendingCountDetail);	
				}
			}
			jTableResponse = new JTableResponse("OK", myPendingCountDetails);
		} catch (Exception e) {
			jTableResponse = new JTableResponse("ERROR", "Error fetching records!");
			log.error("JSON ERROR", e);
		}
		return jTableResponse;
	}
	
	@RequestMapping(value = "workflow.entity.instance.status.summary", method = RequestMethod.POST, produces = "application/json")
	public @ResponseBody JTableResponse listWorkflowEntityInstanceStatusSummary(HttpServletRequest request, @RequestParam Integer engagementId,@RequestParam Integer productId,@RequestParam Integer entityTypeId,@RequestParam Integer entityId,@RequestParam Integer entityParentInstanceId) {
		JTableResponse jTableResponse=null;
		try{
			if(entityId != null && entityId == 0){
				entityId = null;
			}
			List<Object[]> workflowStatusSummaryList = workflowStatusPolicyService.listWorkflowStatusSummary(engagementId,productId, entityTypeId, entityId, entityParentInstanceId);
			ArrayList<HashMap<String, Object>> workflowStatusSummarys = new ArrayList<HashMap<String, Object>>();
			for (Object[] row : workflowStatusSummaryList) {
				HashMap<String, Object> workflowStatusSummary =new HashMap<String, Object>();
				workflowStatusSummary.put("workflowStatus", (String)row[0]);
				workflowStatusSummary.put("workflowStatusCount", (BigInteger)row[1]);
				workflowStatusSummary.put("workflowStatusId", (Integer)row[2]);
				workflowStatusSummary.put("entityId", (Integer)row[3]);
				workflowStatusSummary.put("typeName", (String)row[4]);
				workflowStatusSummarys.add(workflowStatusSummary);					
			}				
			jTableResponse = new JTableResponse("OK", workflowStatusSummarys, workflowStatusSummaryList.size());
		}catch(Exception e) {
			log.error("Error in listWorkflowTaskStatusSummary"+e);
		}
		return jTableResponse;
	}
	
	@RequestMapping(value = "workflow.entity.instance.status.actor.summary", method = RequestMethod.POST, produces = "application/json")
	public @ResponseBody JTableResponse listWorkflowEntityInstanceStatusActorSummary(HttpServletRequest request,@RequestParam Integer engagementId, @RequestParam Integer productId,@RequestParam Integer entityTypeId,@RequestParam Integer entityId,@RequestParam Integer entityInstanceId,@RequestParam String actorType) {
		JTableResponse jTableResponse=null;
		try{
			if(entityId != null && entityId == 0){
				entityId = null;
			}
			if(entityInstanceId != null && entityInstanceId == 0){
				entityInstanceId = null;
			}
			List<Object[]> workflowStatusSummaryList = workflowStatusPolicyService.listWorkflowStatusActorSummary(engagementId,productId, entityTypeId, entityId, entityInstanceId, actorType);
			ArrayList<HashMap<String, Object>> workflowStatusSummarys = new ArrayList<HashMap<String, Object>>();
			for (Object[] row : workflowStatusSummaryList) {
				HashMap<String, Object> workflowStatusSummary =new HashMap<String, Object>();
				workflowStatusSummary.put("workflowStatus", (String)row[0]);
				workflowStatusSummary.put("actorName", (String)row[1]);
				workflowStatusSummary.put("instanceCount", (BigInteger)row[2]);
				workflowStatusSummary.put("workflowStatusId", (Integer)row[3]);
				workflowStatusSummary.put("entityId", (Integer)row[4]);
				workflowStatusSummary.put("userId", (Integer)row[5]);
				workflowStatusSummary.put("roleId", (Integer)row[6]);
				workflowStatusSummary.put("typeName", (String)row[7]);
				workflowStatusSummarys.add(workflowStatusSummary);					
			}				
			jTableResponse = new JTableResponse("OK", workflowStatusSummarys, workflowStatusSummaryList.size());
		}catch(Exception e) {
			log.error("Error in listWorkflowEntityInstanceStatusActorSummary", e);
		}
		return jTableResponse;
	}
	
	@RequestMapping(value = "workflow.status.indicator.product.summary", method = RequestMethod.POST, produces = "application/json")
	public @ResponseBody JTableResponse listWorkflowStatusIndicatorProductSummary(HttpServletRequest request,@RequestParam Integer engagementId, @RequestParam Integer productId, @RequestParam Integer entityTypeId, @RequestParam Integer entityParentInstanceId) {
		JTableResponse jTableResponse=null;
		try{
			if(entityTypeId == 0){
				entityTypeId = null;
			}
			ArrayList<HashMap<String, Object>> productWorkflowStatusIndicatorSummary = new ArrayList<HashMap<String, Object>>();
			List<Object[]> workflowStatusProductSummaryCount = workflowStatusPolicyService.getWorkflowStatusSummaryCountByProduct(engagementId,productId,entityTypeId,entityParentInstanceId);
			HashMap<String, HashMap<String, Object>> statusSummaryMap = new HashMap<String, HashMap<String, Object>>(); 
			Integer totalWorkflowStatusCount=0;
			for (Object[] row : workflowStatusProductSummaryCount) {
				
				String entityTypeName = (String)row[1];
				String indicator=(String)row[3];
				String productName =(String)row[5];
				totalWorkflowStatusCount=totalWorkflowStatusCount+((BigInteger)row[4]).intValue();
				if(statusSummaryMap.containsKey(entityTypeName)){
					Integer indicatorValue = 0;
					if(statusSummaryMap.get(entityTypeName).containsKey(indicator)) {
						indicatorValue = (Integer)statusSummaryMap.get(entityTypeName).get(indicator) + ((BigInteger)row[4]).intValue();
					}else{
						indicatorValue = ((BigInteger)row[4]).intValue();
					}
					statusSummaryMap.get(entityTypeName).put("entityTypeId", (Integer)row[0]);
					statusSummaryMap.get(entityTypeName).put(indicator, indicatorValue);
					statusSummaryMap.get(entityTypeName).put("productName", productName);
				}else{
					HashMap<String, Object> newIndicator = new HashMap<String, Object>();
					Integer indicatorValue = ((BigInteger)row[4]).intValue();
					newIndicator.put(indicator, indicatorValue);
					newIndicator.put("entityTypeId", (Integer)row[0]);
					newIndicator.put("productName", productName);
					statusSummaryMap.put(entityTypeName, newIndicator);
				}
				
			}
			for(Entry<String, HashMap<String, Object>> statusSummarys : statusSummaryMap.entrySet()){
				String entityType = statusSummarys.getKey();
				if("ActivityWorkpackage".equalsIgnoreCase(entityType)){
					entityType = "Activity Workpackages";
				}else if("TaskType".equalsIgnoreCase(entityType)){
					entityType = "Tasks";
				}else if("ActivityType".equalsIgnoreCase(entityType)){
					entityType = "Activities";
				}else if("TestCase".equalsIgnoreCase(entityType)){
					entityType = "Test cases";
				}
				statusSummaryMap.get(statusSummarys.getKey()).put("entityType", entityType);
				if(!statusSummaryMap.get(statusSummarys.getKey()).containsKey("red")){
					statusSummaryMap.get(statusSummarys.getKey()).put("red", 0);
				}
				if(!statusSummaryMap.get(statusSummarys.getKey()).containsKey("orangered")){
					statusSummaryMap.get(statusSummarys.getKey()).put("orangered", 0);
				}
				if(!statusSummaryMap.get(statusSummarys.getKey()).containsKey("orange")){
					statusSummaryMap.get(statusSummarys.getKey()).put("orange", 0);
				}
				if(!statusSummaryMap.get(statusSummarys.getKey()).containsKey("green")){
					statusSummaryMap.get(statusSummarys.getKey()).put("green", 0);
				}
				statusSummaryMap.get(statusSummarys.getKey()).put("totalWorkflowStatusCount", totalWorkflowStatusCount);
				productWorkflowStatusIndicatorSummary.add(statusSummaryMap.get(statusSummarys.getKey()));
			}
			
			jTableResponse = new JTableResponse("OK", productWorkflowStatusIndicatorSummary, productWorkflowStatusIndicatorSummary.size());
		}catch(Exception e) {
			log.error("Error in"+e);
		}
		return jTableResponse;
	}
	
	
	
	@RequestMapping(value = "workflow.summary.SLA.indicator.detail", method = RequestMethod.POST, produces = "application/json")
	public @ResponseBody JTableResponse listWorkflowSummarySLADetail(HttpServletRequest request, @RequestParam Integer engagementId, @RequestParam Integer productId, @RequestParam Integer entityTypeId,@RequestParam Integer lifeCycleEntityId,@RequestParam Integer lifeCycleStageId,@RequestParam String indicator,@RequestParam String workflowType,@RequestParam Integer entityParentInstanceId,Integer jtStartIndex,Integer jtPageSize) {
		JTableResponse jTableResponse=null;
		try{
			List<JsonWorkflowSLAInstance> jsonWorkflowSLAInstanceList = new ArrayList<JsonWorkflowSLAInstance>();
			UserList user = (UserList) request.getSession().getAttribute("USER");
			if(entityTypeId == IDPAConstants.ENTITY_TASK_TYPE || entityTypeId == IDPAConstants.ENTITY_TASK) {
				List<ActivityTask> listSLAIndicatorTaskList=workflowStatusPolicyService.getWorkflowSLATaskDetailView(productId, entityTypeId,lifeCycleEntityId,lifeCycleStageId,workflowType,indicator,entityParentInstanceId,jtStartIndex, jtPageSize);
				if(listSLAIndicatorTaskList!=null && listSLAIndicatorTaskList.size()>0) {
					List<JsonActivityTask> jsonActivityTasks = new ArrayList<JsonActivityTask>(); 
					HashMap<Integer, ActivityTask> activityTaskDeatils = new HashMap<Integer, ActivityTask>();
					for(ActivityTask activityTask: listSLAIndicatorTaskList){
						JsonActivityTask jsonActivityTask=new JsonActivityTask(activityTask);
						activityTaskDeatils.put(activityTask.getActivityTaskId(), activityTask);
						jsonActivityTasks.add(jsonActivityTask);
					}
					workflowStatusPolicyService.setInstanceIndicators(defaultEngagementId,productId,entityParentInstanceId, IDPAConstants.ENTITY_TASK_TYPE, jsonActivityTasks, IDPAConstants.ENTITY_TASK,user, null, null, null,IDPAConstants.ENABLE_PENDING_WITH_COLUMN);
					for(JsonActivityTask jsonActivityTask : jsonActivityTasks){
						ActivityTask activityTask = activityTaskDeatils.get(jsonActivityTask.getActivityTaskId());
						JsonWorkflowSLAInstance jsonWorkflowSLAInstance=workflowStatusPolicyService.setJsonWorkflowSLAInstance(jsonActivityTask, workflowType);
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
				List<TestCaseList> listSLAIndicatorTestcaseList=workflowStatusPolicyService.getWorkflowSLATestCaseDetailView(productId, entityTypeId, indicator, jtStartIndex, jtPageSize);
				if(listSLAIndicatorTestcaseList!=null && listSLAIndicatorTestcaseList.size()>0) {
					List<JsonTestCaseList> jsonTestCaseLists = new ArrayList<JsonTestCaseList>(); 
					HashMap<Integer, TestCaseList> testCaseDeatils = new HashMap<Integer, TestCaseList>();
					for(TestCaseList testCase: listSLAIndicatorTestcaseList){
						JsonTestCaseList jsonTestCase = new JsonTestCaseList(testCase);
						testCaseDeatils.put(testCase.getTestCaseId(), testCase);
						jsonTestCaseLists.add(jsonTestCase);
					}
					workflowStatusPolicyService.setInstanceIndicators(defaultEngagementId,productId,entityParentInstanceId, IDPAConstants.ENTITY_TEST_CASE_ID, jsonTestCaseLists, IDPAConstants.ENTITY_TEST_CASE_ID,user, null, null, null,IDPAConstants.ENABLE_PENDING_WITH_COLUMN);
					for(JsonTestCaseList jsonTestCase : jsonTestCaseLists){
						TestCaseList testCase = testCaseDeatils.get(jsonTestCase.getTestCaseId());
						JsonWorkflowSLAInstance jsonWorkflowSLAInstance=workflowStatusPolicyService.setJsonWorkflowSLAInstance(jsonTestCase, workflowType);
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
				
			} else if(entityTypeId == IDPAConstants.ENTITY_ACTIVITY_TYPE || entityTypeId == IDPAConstants.ENTITY_ACTIVITY_ID) {
				List<Activity> listSLAIndicatorActivityList=workflowStatusPolicyService.getWorkflowSLAActivityDetailView(engagementId,productId, entityTypeId,lifeCycleEntityId,lifeCycleStageId,workflowType, indicator,entityParentInstanceId, jtStartIndex, jtPageSize);
				if(listSLAIndicatorActivityList!=null && listSLAIndicatorActivityList.size()>0) {
					List<JsonActivity> jsonActivities = new ArrayList<JsonActivity>(); 
					HashMap<Integer, Activity> activityDetails = new HashMap<Integer, Activity>();
					for(Activity activity: listSLAIndicatorActivityList){
						JsonActivity jsonActivity=new JsonActivity(activity);
						activityDetails.put(activity.getActivityId(), activity);
						jsonActivities.add(jsonActivity);
					}
					Date modifiedAfterDate = commonService.getModifiedAfterDate((Date)request.getSession().getAttribute("userLoginTime"), (Date)request.getSession().getAttribute("userLogoutTime"));
					workflowStatusPolicyService.setInstanceIndicators(defaultEngagementId,productId,entityParentInstanceId, IDPAConstants.ENTITY_ACTIVITY_TYPE, jsonActivities, IDPAConstants.ENTITY_ACTIVITY_ID,user, modifiedAfterDate, null, null,IDPAConstants.ENABLE_PENDING_WITH_COLUMN);
					for(JsonActivity jsonActivity : jsonActivities){
						Activity activity = activityDetails.get(jsonActivity.getActivityId());
						JsonWorkflowSLAInstance jsonWorkflowSLAInstance=workflowStatusPolicyService.setJsonWorkflowSLAInstance(jsonActivity, workflowType);
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
				
			} else if(entityTypeId == IDPAConstants.ENTITY_ACTIVITY_WORKPACKAGE_ID) {
				List<ActivityWorkPackage> listSLAIndicatorActivityWorkpackageList=workflowStatusPolicyService.getWorkflowSLAActivityWorkPackageDetailView(engagementId,productId, entityTypeId, indicator,entityParentInstanceId,jtStartIndex, jtPageSize);
				if(listSLAIndicatorActivityWorkpackageList!=null && listSLAIndicatorActivityWorkpackageList.size()>0) {
					List<JsonActivityWorkPackage> jsonActivityWorkPackages = new ArrayList<JsonActivityWorkPackage>();
					HashMap<Integer, ActivityWorkPackage> activityWorkpackageDetails = new HashMap<Integer, ActivityWorkPackage>();
					for(ActivityWorkPackage activityWorkPackage : listSLAIndicatorActivityWorkpackageList){
						JsonActivityWorkPackage jsonActivityWorkPackage = new JsonActivityWorkPackage(activityWorkPackage);
						activityWorkpackageDetails.put(activityWorkPackage.getActivityWorkPackageId(), activityWorkPackage);
						jsonActivityWorkPackages.add(jsonActivityWorkPackage);
					}
					workflowStatusPolicyService.setInstanceIndicators(defaultEngagementId,productId,entityParentInstanceId, IDPAConstants.ENTITY_ACTIVITY_WORKPACKAGE_ID, jsonActivityWorkPackages, IDPAConstants.ENTITY_ACTIVITY_WORKPACKAGE_ID,user, null, null, null,IDPAConstants.ENABLE_PENDING_WITH_COLUMN);
					for(JsonActivityWorkPackage jsonActivityWorkPackage : jsonActivityWorkPackages){
						ActivityWorkPackage activityWorkPackage = activityWorkpackageDetails.get(jsonActivityWorkPackage.getActivityWorkPackageId());
						JsonWorkflowSLAInstance jsonWorkflowSLAInstance = workflowStatusPolicyService.setJsonWorkflowSLAInstance(jsonActivityWorkPackage, workflowType);
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
			log.info("workflow.summary.SLA.indicator.detail Completed");
		}catch(Exception e) {
			log.error("Error in listWorkflowSummarySLADetails",e);
		}
		return jTableResponse;
		}
	
	

	@RequestMapping(value = "workpackageRAG.View.summary", method = RequestMethod.POST, produces = "application/json")
	public @ResponseBody JTableResponse getWorkpackageRAGViewSummaryList(HttpServletRequest request, @RequestParam Integer productId) {log.info("workpackageRAG.View.summary");
	JTableResponse jTableResponse;
	
	try {
		List<HashMap<Integer, ArrayList<HashMap<String, Object>>>> workpackageRAGViewSummaryList = new ArrayList<HashMap<Integer, ArrayList<HashMap<String, Object>>>>();
		List<Object[]> lifeCycleInstnaceList=workflowStatusPolicyService.getWorkpackageRAGViewSummaryList(productId);
		HashMap<Integer,ArrayList<HashMap<String, Object>>> ragSummaryDetails= new HashMap<Integer,ArrayList<HashMap<String, Object>>>();
		
		HashMap<String, Integer> workpackageIndex= new HashMap<String,Integer>();
		if(lifeCycleInstnaceList != null && lifeCycleInstnaceList.size() >0) {
			String indicator="";
			String workpackageName="";
			int index=-1;
			
			HashMap<String, HashMap<String, Object>> wpRAGViewMap =new HashMap<String, HashMap<String, Object>>();
			for (Object[] row : lifeCycleInstnaceList) {
				HashMap<String, Object> wpRAGViewSummary = null;

				if(wpRAGViewMap.containsKey((BigInteger)row[1]+"-"+(BigInteger)row[3])){
					wpRAGViewSummary = wpRAGViewMap.get((BigInteger)row[1]+"-"+(BigInteger)row[3]);
				}else{
					wpRAGViewSummary = new HashMap<String, Object>();
					wpRAGViewSummary.put("productName", (String)row[0]);
					wpRAGViewSummary.put("lifeCycleEntityId", (BigInteger)row[1]);
					wpRAGViewSummary.put("lifeCycleEntityName", (String)row[2]);
					wpRAGViewSummary.put("lifeCycleEntityStatusId", (BigInteger)row[3]);
					wpRAGViewSummary.put("lifeCycleEntityStatusName", (String)row[4]);
					wpRAGViewSummary.put("lifeCycleEntityWorkflowTemplate", (String)row[5]);
					List<HashMap<String, Object>> wpDetailsList = new ArrayList<HashMap<String, Object>>();
					wpRAGViewSummary.put("entityDetails", wpDetailsList);
				}
				HashMap<String, Object> entityDetails = new HashMap<String, Object>();
				entityDetails.put("entityTypeId", (Integer)row[6]);
				entityDetails.put("entityTypeName", (String)row[7]);
				entityDetails.put("lifeCycleStatusId", (Integer)row[8]);
				entityDetails.put("lifeCycleStatusName", (String)row[9]);
				entityDetails.put("workflowStatusCategoryId", (Integer)row[10]);
				entityDetails.put("workflowStatusCategoryName", (String)row[11] == null ?"--" :(String)row[11]);
				entityDetails.put("instanceCount", (BigInteger)row[12]);
				entityDetails.put("indicator", (String)row[14]);
				indicator=(String)row[14];
			
				if(indicator.equalsIgnoreCase("red")) {
					entityDetails.put("redCount", (BigInteger)row[12]);
					entityDetails.put("orangeredCount",0);
					entityDetails.put("orangeCount", 0);
					entityDetails.put("greenCount", 0);
				}else if(indicator.equalsIgnoreCase("orangered")) {
					entityDetails.put("orangeredCount", (BigInteger)row[12]);
					entityDetails.put("redCount", 0);						
					entityDetails.put("orangeCount", 0);
					entityDetails.put("greenCount", 0);
				}else if(indicator.equalsIgnoreCase("orange")) {
					entityDetails.put("orangeCount", (BigInteger)row[12]);
					entityDetails.put("redCount", 0);
					entityDetails.put("orangeredCount",0);
					entityDetails.put("greenCount", 0);
				}else if(indicator.equalsIgnoreCase("green")) {
					entityDetails.put("greenCount", (BigInteger)row[12]);
					entityDetails.put("orangeCount", 0);
					entityDetails.put("redCount", 0);
					entityDetails.put("orangeredCount",0);						
				}
				((ArrayList<HashMap<String, Object>>)wpRAGViewSummary.get("entityDetails")).add(entityDetails);
				wpRAGViewMap.put((BigInteger)row[1]+"-"+(BigInteger)row[3], wpRAGViewSummary);
			}
			
			for(Entry<String, HashMap<String, Object>> mapDetails : wpRAGViewMap.entrySet()){
				ArrayList<HashMap<String, Object>> detailsList = new ArrayList<HashMap<String, Object>>();
				detailsList.add(mapDetails.getValue());
				index++;
				ragSummaryDetails.put(index, detailsList);
			}
			if(!ragSummaryDetails.isEmpty() && ragSummaryDetails.size() >0) {
				workpackageRAGViewSummaryList.add(ragSummaryDetails);
			}
		}
		jTableResponse = new JTableResponse("OK", workpackageRAGViewSummaryList);
		log.info("workpackageRAG.View.summary Completed");
	} catch (Exception e) {
		jTableResponse = new JTableResponse("ERROR", "Error in workpackageRAG.View.summary!");
		log.error("JSON ERROR", e);
	}
	return jTableResponse;
	}
	
	
	@RequestMapping(value = "workflow.entity.instance.type.summary", method = RequestMethod.POST, produces = "application/json")
	public @ResponseBody JTableResponse listWorkflowEntityInstanceTypeSummary(HttpServletRequest request, @RequestParam Integer engagementId,@RequestParam Integer productId,@RequestParam Integer entityTypeId,@RequestParam Integer entityParentInstanceId) {
		JTableResponse jTableResponse=null;
		try{
			
			List<Object[]> workflowStatusSummaryList = workflowStatusPolicyService.getWorkflowTypeSummaryByEntityType(engagementId,productId, entityTypeId,entityParentInstanceId);
			ArrayList<HashMap<String, Object>> workflowStatusSummarys = new ArrayList<HashMap<String, Object>>();
			for (Object[] row : workflowStatusSummaryList) {
				HashMap<String, Object> workflowStatusSummary =new HashMap<String, Object>();
				workflowStatusSummary.put("typeName", (String)row[0]);
				workflowStatusSummary.put("typeId", (Integer)row[1]);
				workflowStatusSummary.put("productId", (Integer)row[2]);
				workflowStatusSummary.put("entityTypeId", (Integer)row[3]);
				workflowStatusSummary.put("typeCount", (BigInteger)row[4]);
				workflowStatusSummarys.add(workflowStatusSummary);					
			}				
			jTableResponse = new JTableResponse("OK", workflowStatusSummarys, workflowStatusSummaryList.size());
		}catch(Exception e) {
			log.error("Error in listWorkflowEntityInstanceTypeSummary",e);
		}
		return jTableResponse;
	}
	
	
	@RequestMapping(value="workflow.template.status.import", method=RequestMethod.POST ,produces="text/plain" )
	public @ResponseBody JTableResponse workflowTemplateStatusImport(HttpServletRequest request) {
		log.debug("workflow.template.status.import");
		JTableResponse jTableResponse;
		try {
			
			MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
			CommonsMultipartFile multipartFile = null;
			String fileName = "";
			InputStream is=null;
			Iterator<String> iterator = multipartRequest.getFileNames();
			while (iterator.hasNext()) {
				String key = (String) iterator.next();
				multipartFile = (CommonsMultipartFile) multipartRequest.getFile(key);
				fileName = multipartFile.getOriginalFilename();
				is=multipartFile.getInputStream();
			}
			
			String isImportComplete = " ";
			
			isImportComplete = excelTestDataIntegrator.importWorkflowTemplateStatus(request,fileName,is);
			
			if(isImportComplete != null){
				log.info("Import workflow template status Completed.");
				jTableResponse = new JTableResponse("Ok","workflow template status Import Completed."+" "+isImportComplete);
			} else{
				log.info("Import completed");
				jTableResponse = new JTableResponse("Ok","Import completed"+" "+isImportComplete);
			}
		} catch (Exception e) {
			jTableResponse = new JTableResponse("Error in Import");
			log.error("JSON ERROR importing workflowTemplateStatus : " , e);
		}
		return jTableResponse;
	}
	
	@RequestMapping(value="entity.workflow.status.policy.import", method=RequestMethod.POST ,produces="text/plain" )
	public @ResponseBody JTableResponse entityWorkflowMappingStatusPolicyForUserandRoleImport(HttpServletRequest request,@RequestParam Integer productId) {
		log.debug("entity.workflow.status.policy.import");
		JTableResponse jTableResponse;
		try {
			
			MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
			CommonsMultipartFile multipartFile = null;
			String fileName = "";
			InputStream is=null;
			Iterator<String> iterator = multipartRequest.getFileNames();
			while (iterator.hasNext()) {
				String key = (String) iterator.next();
				multipartFile = (CommonsMultipartFile) multipartRequest.getFile(key);
				fileName = multipartFile.getOriginalFilename();
				is=multipartFile.getInputStream();
			}
			
			
			String isImportComplete = " ";
			
			isImportComplete = excelTestDataIntegrator.importEntityWorkflowMappingStatusPolicyForUserandRole(request,fileName,is,productId);
			
			if(isImportComplete != null){
				log.info("Import  entity workflow status policy for user and role Completed.");
				jTableResponse = new JTableResponse("Ok","Import  entity workflow status policy for user and role Completed."+" "+isImportComplete);
			} else{
				log.info("Import completed");
				jTableResponse = new JTableResponse("Ok","Import completed."+" "+isImportComplete);
			}
		} catch (Exception e) {
			jTableResponse = new JTableResponse("Error in Import");
			log.error("JSON ERROR Importing entityWorkflowMappingStatusPolicy For UserandRoleImport : " , e);
		}
		return jTableResponse;
	}
	
	@RequestMapping(value = "workflow.RAG.indicator.product.SLA.summary", method = RequestMethod.POST, produces = "application/json")
	public @ResponseBody JTableResponse listWorkflowRAGIndicatorProductSLASummary(HttpServletRequest request, @RequestParam Integer engagementId,@RequestParam Integer productId, @RequestParam Integer entityTypeId,@RequestParam Integer entityParentInstanceId) {
		JTableResponse jTableResponse=null;
		try{
			if(entityTypeId == 0){
				entityTypeId = null;
			}
			Integer totalRAGSummaryCount=0;
			ArrayList<HashMap<String, Object>> productWorkflowRAGIndicatorSummary = new ArrayList<HashMap<String, Object>>();
			//Total Count service
			List<Object[]> workflowRAGWorkpackageSummaryTotalCount = workflowStatusPolicyService.getWorkflowRAGActivityGroupingSummaryTotalCount(engagementId,productId,entityTypeId,entityParentInstanceId);
			List<Object[]> workflowRAGProductSummaryCount = workflowStatusPolicyService.getWorkflowRAGSummaryCountByProductAndEntityType(engagementId,productId,entityTypeId,entityParentInstanceId);
			List<Object[]> workflowRAGProductSummaryCompleteCount = workflowStatusPolicyService.getWorkflowRAGSummaryCompleteCountByProductAndEntityType(engagementId,productId,entityTypeId,entityParentInstanceId);
			List<Object[]> workflowRAGProductSummaryAbortCount = workflowStatusPolicyService.getWorkflowRAGSummaryAbortCountByProductAndEntityType(engagementId,productId,entityTypeId,entityParentInstanceId);
			HashMap<String, HashMap<String, Object>> statusSummaryMap = new HashMap<String, HashMap<String, Object>>();
			
			if(workflowRAGWorkpackageSummaryTotalCount != null){
				for(Object totalRAGCount : workflowRAGWorkpackageSummaryTotalCount) {
					totalRAGSummaryCount=Integer.parseInt(totalRAGCount.toString());
				}			
			}
			
			for (Object[] row : workflowRAGProductSummaryCount) {
				
				String entityTypeName = (String)row[1];
				String indicator=(String)row[8];
				String productName =(String)row[10];
				
				if(statusSummaryMap.containsKey(entityTypeName)){
					Integer indicatorValue = 0;
					if(statusSummaryMap.get(entityTypeName).containsKey(indicator)) {
						indicatorValue = (Integer)statusSummaryMap.get(entityTypeName).get(indicator) + ((BigInteger)row[9]).intValue();
					}else{
						indicatorValue = ((BigInteger)row[9]).intValue();
					}
					statusSummaryMap.get(entityTypeName).put("entityTypeId", (Integer)row[0]);
					statusSummaryMap.get(entityTypeName).put(indicator, indicatorValue);
					statusSummaryMap.get(entityTypeName).put("productName", productName);
				}else{
					HashMap<String, Object> newIndicator = new HashMap<String, Object>();
					Integer indicatorValue = ((BigInteger)row[9]).intValue();
					newIndicator.put(indicator, indicatorValue);
					newIndicator.put("entityTypeId", (Integer)row[0]);
					newIndicator.put("productName", productName);
					statusSummaryMap.put(entityTypeName, newIndicator);
				}
				
			}
			
			
			for (Object[] row : workflowRAGProductSummaryCompleteCount) {
				
				String entityTypeName = (String)row[1];
				String indicator="completedCount";
				String productName =(String)row[10];
				
				if(statusSummaryMap.containsKey(entityTypeName)){
					Integer indicatorValue = 0;
					if(statusSummaryMap.get(entityTypeName).containsKey(indicator)) {
						indicatorValue = (Integer)statusSummaryMap.get(entityTypeName).get(indicator) + ((BigInteger)row[9]).intValue();
					}else{
						indicatorValue = ((BigInteger)row[9]).intValue();
					}
					statusSummaryMap.get(entityTypeName).put("entityTypeId", (Integer)row[0]);
					statusSummaryMap.get(entityTypeName).put(indicator, indicatorValue);
					statusSummaryMap.get(entityTypeName).put("productName", productName);
				}else{
					HashMap<String, Object> newIndicator = new HashMap<String, Object>();
					Integer indicatorValue = ((BigInteger)row[9]).intValue();
					newIndicator.put(indicator, indicatorValue);
					newIndicator.put("entityTypeId", (Integer)row[0]);
					newIndicator.put("productName", productName);
					statusSummaryMap.put(entityTypeName, newIndicator);
				}
				
			}


			for (Object[] row : workflowRAGProductSummaryAbortCount) {
				
				String entityTypeName = (String)row[1];
				String indicator="abortCount";
				String productName =(String)row[10];
				
				if(statusSummaryMap.containsKey(entityTypeName)){
					Integer indicatorValue = 0;
					if(statusSummaryMap.get(entityTypeName).containsKey(indicator)) {
						indicatorValue = (Integer)statusSummaryMap.get(entityTypeName).get(indicator) + ((BigInteger)row[9]).intValue();
					}else{
						indicatorValue = ((BigInteger)row[9]).intValue();
					}
					statusSummaryMap.get(entityTypeName).put("entityTypeId", (Integer)row[0]);
					statusSummaryMap.get(entityTypeName).put(indicator, indicatorValue);
					statusSummaryMap.get(entityTypeName).put("productName", productName);
				}else{
					HashMap<String, Object> newIndicator = new HashMap<String, Object>();
					Integer indicatorValue = ((BigInteger)row[9]).intValue();
					newIndicator.put(indicator, indicatorValue);
					newIndicator.put("entityTypeId", (Integer)row[0]);
					newIndicator.put("productName", productName);
					statusSummaryMap.put(entityTypeName, newIndicator);
				}
				
			}
			for(Entry<String, HashMap<String, Object>> statusSummarys : statusSummaryMap.entrySet()){
				String entityType = statusSummarys.getKey();
				if("ActivityWorkpackage".equalsIgnoreCase(entityType)){
					entityType = "Activity Workpackages";
				}else if("TaskType".equalsIgnoreCase(entityType)){
					entityType = "Tasks";
				}else if("ActivityType".equalsIgnoreCase(entityType)){
					entityType = "Activities";
				}else if("TestCase".equalsIgnoreCase(entityType)){
					entityType = "Test cases";
				}
				statusSummaryMap.get(statusSummarys.getKey()).put("entityType", entityType);
				if(!statusSummaryMap.get(statusSummarys.getKey()).containsKey("red")){
					statusSummaryMap.get(statusSummarys.getKey()).put("red", 0);
				}
				if(!statusSummaryMap.get(statusSummarys.getKey()).containsKey("orange")){
					statusSummaryMap.get(statusSummarys.getKey()).put("orange", 0);
				}
				if(!statusSummaryMap.get(statusSummarys.getKey()).containsKey("green")){
					statusSummaryMap.get(statusSummarys.getKey()).put("green", 0);
				}
				
				if(!statusSummaryMap.get(statusSummarys.getKey()).containsKey("completedCount")){
					statusSummaryMap.get(statusSummarys.getKey()).put("completedCount", 0);
				}
				
				if(!statusSummaryMap.get(statusSummarys.getKey()).containsKey("abortCount")){
					statusSummaryMap.get(statusSummarys.getKey()).put("abortCount", 0);
				}
				
				statusSummaryMap.get(statusSummarys.getKey()).put("totalRAGCount", totalRAGSummaryCount);
				
				
				
				productWorkflowRAGIndicatorSummary.add(statusSummaryMap.get(statusSummarys.getKey()));
			}
			jTableResponse = new JTableResponse("OK", productWorkflowRAGIndicatorSummary, productWorkflowRAGIndicatorSummary.size());
			log.info("workflow.RAG.indicator.product.SLA.summary completed");
		}catch(Exception e) {
			log.error("Error in"+e);
		}
		return jTableResponse;
	}
	
	@RequestMapping(value = "workflow.RAG.indicator.product.resource.summary", method = RequestMethod.POST, produces = "application/json")
	public @ResponseBody JTableResponse listWorkflowRAGIndicatorProductResourceBasedSummary(HttpServletRequest request,@RequestParam Integer engagementId, @RequestParam Integer productId, @RequestParam Integer entityTypeId, @RequestParam Integer entityParentInstanceId) {
		JTableResponse jTableResponse=null;
		try{
			if(entityTypeId == 0){
				entityTypeId = null;
			}
			ArrayList<HashMap<String, Object>> productWorkflowRAGIndicatorSummary = new ArrayList<HashMap<String, Object>>();
			List<Object[]> workflowRAGProductSummaryCount = workflowStatusPolicyService.getWorkflowRAGSummaryCountByProductAndResouces(engagementId,productId,entityTypeId,entityParentInstanceId);
			List<Object[]> workflowRAGProductSummaryCompletedCount = workflowStatusPolicyService.getRAGSummaryCompletedCountByProductAndResouces(engagementId,productId,entityTypeId,entityParentInstanceId);
			List<Object[]> workflowRAGProductSummaryAbortedCount = workflowStatusPolicyService.getRAGSummaryAbortedCountByProductAndResouces(engagementId,productId,entityTypeId,entityParentInstanceId);
			HashMap<String, HashMap<String, Object>> statusSummaryMap = new HashMap<String, HashMap<String, Object>>(); 
			for (Object[] row : workflowRAGProductSummaryCount) {
				
				String indicator=(String)row[8];
				String productName =(String)row[10];
				String userName =(String)row[11];
				Integer userId =(Integer)row[12];
				Integer prodId =(Integer)row[13];
				Integer engmentId =(Integer)row[14];
				
				if(statusSummaryMap.containsKey(userName)){
					Integer indicatorValue = 0;
					if(statusSummaryMap.get(userName).containsKey(indicator)) {
						indicatorValue = (Integer)statusSummaryMap.get(userName).get(indicator) + ((BigInteger)row[9]).intValue();
					}else{
						indicatorValue = ((BigInteger)row[9]).intValue();
					}
					statusSummaryMap.get(userName).put("entityTypeId", (Integer)row[0]);
					statusSummaryMap.get(userName).put(indicator, indicatorValue);
					statusSummaryMap.get(userName).put("productName", productName);
					statusSummaryMap.get(userName).put("userId", userId);
					statusSummaryMap.get(userName).put("userName", userName);
					statusSummaryMap.get(userName).put("productId", prodId);
					statusSummaryMap.get(userName).put("engagementId", engmentId);
				}else{
					HashMap<String, Object> newIndicator = new HashMap<String, Object>();
					Integer indicatorValue = ((BigInteger)row[9]).intValue();
					newIndicator.put(indicator, indicatorValue);
					newIndicator.put("entityTypeId", (Integer)row[0]);
					newIndicator.put("productName", productName);
					newIndicator.put("userName", userName);
					newIndicator.put("userId", userId);
					newIndicator.put("productId", prodId);
					newIndicator.put("engagementId", engmentId);
					statusSummaryMap.put(userName, newIndicator);
				}
				
			}
			
			
			
			
			for (Object[] row : workflowRAGProductSummaryCompletedCount) {
				
						String indicator="completedCount";
						String productName =(String)row[1];
						String userName =(String)row[2];
						Integer userId =(Integer)row[3];
						Integer prodId =(Integer)row[4];
						Integer engmentId =(Integer)row[5];
						
						if(statusSummaryMap.containsKey(userName)){
							Integer indicatorValue = 0;
							if(statusSummaryMap.get(userName).containsKey(indicator)) {
								indicatorValue = (Integer)statusSummaryMap.get(userName).get(indicator) + ((BigInteger)row[0]).intValue();
							}else{
								indicatorValue = ((BigInteger)row[0]).intValue();
							}
							statusSummaryMap.get(userName).put(indicator, indicatorValue);
							statusSummaryMap.get(userName).put("productName", productName);
							statusSummaryMap.get(userName).put("userId", userId);
							statusSummaryMap.get(userName).put("userName", userName);
							statusSummaryMap.get(userName).put("productId", prodId);
							statusSummaryMap.get(userName).put("engagementId", engmentId);
						}else{
							HashMap<String, Object> newIndicator = new HashMap<String, Object>();
							Integer indicatorValue = ((BigInteger)row[0]).intValue();
							newIndicator.put(indicator, indicatorValue);
							newIndicator.put("productName", productName);
							newIndicator.put("userName", userName);
							newIndicator.put("userId", userId);
							newIndicator.put("productId", prodId);
							newIndicator.put("engagementId", engmentId);
							statusSummaryMap.put(userName, newIndicator);
						}
						
					}
			
			
			
			for (Object[] row : workflowRAGProductSummaryAbortedCount) {
				
						String indicator="abortCount";
						String productName =(String)row[1];
						String userName =(String)row[2];
						Integer userId =(Integer)row[3];
						Integer prodId =(Integer)row[4];
						Integer engmentId =(Integer)row[5];
						
						if(statusSummaryMap.containsKey(userName)){
							Integer indicatorValue = 0;
							if(statusSummaryMap.get(userName).containsKey(indicator)) {
								indicatorValue = (Integer)statusSummaryMap.get(userName).get(indicator) + ((BigInteger)row[0]).intValue();
							}else{
								indicatorValue = ((BigInteger)row[0]).intValue();
							}
							statusSummaryMap.get(userName).put(indicator, indicatorValue);
							statusSummaryMap.get(userName).put("productName", productName);
							statusSummaryMap.get(userName).put("userId", userId);
							statusSummaryMap.get(userName).put("userName", userName);
							statusSummaryMap.get(userName).put("productId", prodId);
							statusSummaryMap.get(userName).put("engagementId", engmentId);
						}else{
							HashMap<String, Object> newIndicator = new HashMap<String, Object>();
							Integer indicatorValue = ((BigInteger)row[0]).intValue();
							newIndicator.put(indicator, indicatorValue);
							newIndicator.put("productName", productName);
							newIndicator.put("userName", userName);
							newIndicator.put("userId", userId);
							newIndicator.put("productId", prodId);
							newIndicator.put("engagementId", engmentId);
							statusSummaryMap.put(userName, newIndicator);
						}
						
					}
			
			for(Entry<String, HashMap<String, Object>> statusSummarys : statusSummaryMap.entrySet()){
				
				if(!statusSummaryMap.get(statusSummarys.getKey()).containsKey("red")){
					statusSummaryMap.get(statusSummarys.getKey()).put("red", 0);
				}
				if(!statusSummaryMap.get(statusSummarys.getKey()).containsKey("orange")){
					statusSummaryMap.get(statusSummarys.getKey()).put("orange", 0);
				}
				if(!statusSummaryMap.get(statusSummarys.getKey()).containsKey("green")){
					statusSummaryMap.get(statusSummarys.getKey()).put("green", 0);
				}
				if(!statusSummaryMap.get(statusSummarys.getKey()).containsKey("completedCount")){
					statusSummaryMap.get(statusSummarys.getKey()).put("completedCount", 0);
				}
				if(!statusSummaryMap.get(statusSummarys.getKey()).containsKey("abortCount")){
					statusSummaryMap.get(statusSummarys.getKey()).put("abortCount", 0);
				}
				productWorkflowRAGIndicatorSummary.add(statusSummaryMap.get(statusSummarys.getKey()));
			}
			jTableResponse = new JTableResponse("OK", productWorkflowRAGIndicatorSummary, productWorkflowRAGIndicatorSummary.size());
		}catch(Exception e) {
			log.error("Error in"+e);
		}
		return jTableResponse;
	}
	
	
	@RequestMapping(value = "workflow.RAG.indicator.activity.grouping.summary", method = RequestMethod.POST, produces = "application/json")
	public @ResponseBody JTableResponse listWorkflowRAGIndicatorProductActivityGroupingBasedSummary(HttpServletRequest request, @RequestParam Integer engagementId,@RequestParam Integer productId, @RequestParam Integer entityTypeId, @RequestParam Integer entityParentInstanceId) {
		JTableResponse jTableResponse=null;
		try{
			if(entityTypeId == 0){
				entityTypeId = null;
			}
			ArrayList<HashMap<String, Object>> productWorkflowRAGIndicatorWorkpackageSummary = new ArrayList<HashMap<String, Object>>();
			//Total Count service
			List<Object[]> workflowRAGWorkpackageSummaryCount = workflowStatusPolicyService.getWorkflowRAGActivityGroupingSummaryCount(engagementId,productId,entityTypeId,entityParentInstanceId);
			List<Object[]> workflowRAGWorkpackageSummaryCompletedCount = workflowStatusPolicyService.getRAGViewActivityGroupingSummaryCompletedCount(engagementId,productId,entityTypeId,entityParentInstanceId);
			List<Object[]> workflowRAGWorkpackageSummaryAbortedCount = workflowStatusPolicyService.getRAGViewActivityGroupingSummaryAbortCount(engagementId,productId,entityTypeId,entityParentInstanceId);
			HashMap<String, HashMap<String, Object>> ragSummaryMap = new HashMap<String, HashMap<String, Object>>(); 
			
			for (Object[] row : workflowRAGWorkpackageSummaryCount) {
				
				String indicator=(String)row[8];
				Integer prodId =(Integer)row[10];
				String productName =(String)row[11];
				Integer workpackageId =(Integer)row[12];
				String workpackageName =(String)row[13];
				
				if(ragSummaryMap.containsKey(prodId+""+workpackageId)){
					Integer indicatorValue = 0;
					if(ragSummaryMap.get(prodId+""+workpackageId).containsKey(indicator)) {
						indicatorValue = (Integer)ragSummaryMap.get(prodId+""+workpackageId).get(indicator) + ((BigInteger)row[9]).intValue();
					}else{
						indicatorValue = ((BigInteger)row[9]).intValue();
					}
					ragSummaryMap.get(prodId+""+workpackageId).put("entityTypeId", (Integer)row[0]);
					ragSummaryMap.get(prodId+""+workpackageId).put(indicator, indicatorValue);
					ragSummaryMap.get(prodId+""+workpackageId).put("productId", prodId);
					ragSummaryMap.get(prodId+""+workpackageId).put("productName", productName);
					ragSummaryMap.get(prodId+""+workpackageId).put("workpackageId", workpackageId);
					ragSummaryMap.get(prodId+""+workpackageId).put("workpackageName", workpackageName);
					
				}else{
					HashMap<String, Object> newIndicator = new HashMap<String, Object>();
					Integer indicatorValue = ((BigInteger)row[9]).intValue();
					newIndicator.put(indicator, indicatorValue);
					newIndicator.put("entityTypeId", (Integer)row[0]);
					newIndicator.put("productName", productName);
					newIndicator.put("productId", prodId);
					newIndicator.put("workpackageId", workpackageId);
					newIndicator.put("workpackageName", workpackageName);
					
						if(workflowRAGWorkpackageSummaryCompletedCount !=null) {
							for (Object[] completedStageList : workflowRAGWorkpackageSummaryCompletedCount){
								Integer completedWpId=(Integer)completedStageList[1];
								Integer completedProductId=(Integer)completedStageList[2];
							if(completedWpId != null && completedWpId.equals(workpackageId)) {
								newIndicator.put("completedCount", (BigInteger) completedStageList[0]);
							}
						   }
						}
						if(workflowRAGWorkpackageSummaryAbortedCount !=null) {
							for (Object[] abortedStageList : workflowRAGWorkpackageSummaryAbortedCount){
								Integer abortedWpId=(Integer)abortedStageList[1];
								Integer abourtProductId=(Integer)abortedStageList[2];
								BigInteger abortCount=(BigInteger) abortedStageList[0];
							if(abortedWpId != null && abortedWpId.equals(workpackageId)) {
								newIndicator.put("abortCount",abortCount);
							}
							}
						}
					
					ragSummaryMap.put(prodId+""+workpackageId, newIndicator);
				}
				
			}
					
			for(Entry<String, HashMap<String, Object>> statusSummarys : ragSummaryMap.entrySet()){
			
				if(!ragSummaryMap.get(statusSummarys.getKey()).containsKey("red")){
					ragSummaryMap.get(statusSummarys.getKey()).put("red", 0);
				}
				
				if(!ragSummaryMap.get(statusSummarys.getKey()).containsKey("orange")){
					ragSummaryMap.get(statusSummarys.getKey()).put("orange", 0);
				}
				if(!ragSummaryMap.get(statusSummarys.getKey()).containsKey("green")){
					ragSummaryMap.get(statusSummarys.getKey()).put("green", 0);
				}
				if(!ragSummaryMap.get(statusSummarys.getKey()).containsKey("completedCount")){
					ragSummaryMap.get(statusSummarys.getKey()).put("completedCount", 0);
				}
				if(!ragSummaryMap.get(statusSummarys.getKey()).containsKey("abortCount")){
					ragSummaryMap.get(statusSummarys.getKey()).put("abortCount", 0);
				}
				productWorkflowRAGIndicatorWorkpackageSummary.add(ragSummaryMap.get(statusSummarys.getKey()));
			}
			jTableResponse = new JTableResponse("OK", productWorkflowRAGIndicatorWorkpackageSummary, productWorkflowRAGIndicatorWorkpackageSummary.size());
			log.info("workflow.RAG.indicator.activity.grouping.summary completed");
		}catch(Exception e) {
			log.error("Error listWorkflowRAGIndicatorProductActivityGroupingBasedSummary in"+e);
		}
		return jTableResponse;
	}
	
	
	@RequestMapping(value = "workflow.RAG.indicator.product.workpackage.summary", method = RequestMethod.POST, produces = "application/json")
	public @ResponseBody JTableResponse listWorkflowRAGIndicatorProductWorkPackageBasedSummary(HttpServletRequest request, @RequestParam Integer productId, @RequestParam Integer entityTypeId,@RequestParam Integer entityParentInstanceId) {
		JTableResponse jTableResponse=null;
		try{
			if(entityTypeId == 0){
				entityTypeId = null;
			}
			Integer engagementId=0;
			Integer totalRAGSummaryCount=0;
			ArrayList<HashMap<String, Object>> productWorkflowRAGIndicatorWorkpackageSummary = new ArrayList<HashMap<String, Object>>();
			List<Object[]> workflowRAGWorkpackageSummaryTotalCount = workflowStatusPolicyService.getWorkflowRAGActivityGroupingSummaryTotalCount(engagementId,productId,entityTypeId,entityParentInstanceId);
			List<Object[]> workflowRAGWorkpackageSummaryCount = workflowStatusPolicyService.getWorkflowRAGWorkpackageSummaryCount(productId,entityTypeId,entityParentInstanceId);
			List<Object[]> workflowRAGWorkpackageSummaryCompletedCount = workflowStatusPolicyService.getRAGviewWorkpackageSummaryCompletedCount(productId,entityTypeId,entityParentInstanceId);
			List<Object[]> workflowRAGWorkpackageSummaryAbortedCount = workflowStatusPolicyService.getRAGviewWorkpackageSummaryAbortedCount(productId,entityTypeId,entityParentInstanceId);
			
			HashMap<String, HashMap<String, Object>> ragSummaryMap = new HashMap<String, HashMap<String, Object>>(); 
			if(workflowRAGWorkpackageSummaryTotalCount != null){
				for(Object totalRAGCount : workflowRAGWorkpackageSummaryTotalCount) {
					totalRAGSummaryCount=Integer.parseInt(totalRAGCount.toString());
				}			
			}
			for (Object[] row : workflowRAGWorkpackageSummaryCount) {
				
				String indicator=(String)row[8];
				String productName =(String)row[10];
				String workpackageName =(String)row[11];
				Integer workpackageId =(Integer)row[12];
				
				if(ragSummaryMap.containsKey(workpackageId+"")){
					Integer indicatorValue = 0;
					if(ragSummaryMap.get(workpackageId+"").containsKey(indicator)) {
						indicatorValue = (Integer)ragSummaryMap.get(workpackageId+"").get(indicator) + ((BigInteger)row[9]).intValue();
					}else{
						indicatorValue = ((BigInteger)row[9]).intValue();
					}
					ragSummaryMap.get(workpackageId+"").put("entityTypeId", (Integer)row[0]);
					ragSummaryMap.get(workpackageId+"").put(indicator, indicatorValue);
					ragSummaryMap.get(workpackageId+"").put("productName", productName);
					ragSummaryMap.get(workpackageId+"").put("workpackageId", workpackageId);
					ragSummaryMap.get(workpackageId+"").put("workpackageName", workpackageName);
					
				}else{
					HashMap<String, Object> newIndicator = new HashMap<String, Object>();
					Integer indicatorValue = ((BigInteger)row[9]).intValue();
					newIndicator.put(indicator, indicatorValue);
					newIndicator.put("entityTypeId", (Integer)row[0]);
					newIndicator.put("productName", productName);
					newIndicator.put("workpackageId", workpackageId);
					newIndicator.put("workpackageName", workpackageName);
					
					
					if(workflowRAGWorkpackageSummaryCompletedCount !=null) {
						for (Object[] completedStageList : workflowRAGWorkpackageSummaryCompletedCount){
							BigInteger completedWorkpackageId=(BigInteger)completedStageList[1];
							Integer wpId=completedWorkpackageId.intValue();
							if(wpId.equals(workpackageId)) {
								newIndicator.put("completedCount", (BigInteger) completedStageList[0]);
							}
							
						}
					}
					if(workflowRAGWorkpackageSummaryAbortedCount !=null) {
						for (Object[] abortedStageList : workflowRAGWorkpackageSummaryAbortedCount){
							BigInteger abourtWorkpackageId=(BigInteger)abortedStageList[1];
							Integer wpId=abourtWorkpackageId.intValue();
							if(wpId.equals(workpackageId)) {
								newIndicator.put("abortCount",(BigInteger) abortedStageList[0]);
							}
							
						}
					}
					newIndicator.put("totalRAGCount", totalRAGSummaryCount);
					ragSummaryMap.put(workpackageId+"", newIndicator);
				}
				
			}
			for(Entry<String, HashMap<String, Object>> statusSummarys : ragSummaryMap.entrySet()){
			
				if(!ragSummaryMap.get(statusSummarys.getKey()).containsKey("red")){
					ragSummaryMap.get(statusSummarys.getKey()).put("red", 0);
				}
				
				if(!ragSummaryMap.get(statusSummarys.getKey()).containsKey("orange")){
					ragSummaryMap.get(statusSummarys.getKey()).put("orange", 0);
				}
				if(!ragSummaryMap.get(statusSummarys.getKey()).containsKey("green")){
					ragSummaryMap.get(statusSummarys.getKey()).put("green", 0);
				}
				if(!ragSummaryMap.get(statusSummarys.getKey()).containsKey("completedCount")){
					ragSummaryMap.get(statusSummarys.getKey()).put("completedCount", 0);
				}
				if(!ragSummaryMap.get(statusSummarys.getKey()).containsKey("abortCount")){
					ragSummaryMap.get(statusSummarys.getKey()).put("abortCount", 0);
				}
				productWorkflowRAGIndicatorWorkpackageSummary.add(ragSummaryMap.get(statusSummarys.getKey()));
			}
			jTableResponse = new JTableResponse("OK", productWorkflowRAGIndicatorWorkpackageSummary, productWorkflowRAGIndicatorWorkpackageSummary.size());
			log.info("workflow.RAG.indicator.product.workpackage.summary completed");
		}catch(Exception e) {
			log.error("Error in"+e);
		}
		return jTableResponse;
	}
	
	
	@RequestMapping(value = "workflow.RAG.indicator.product.activity.summary", method = RequestMethod.POST, produces = "application/json")
	public @ResponseBody JTableResponse listWorkflowRAGIndicatorProductActivityBasedSummary(HttpServletRequest request, @RequestParam Integer productId, @RequestParam Integer entityTypeId) {
		JTableResponse jTableResponse=null;
		try{
			if(entityTypeId == 0){
				entityTypeId = null;
			}
			ArrayList<HashMap<String, Object>> productWorkflowRAGIndicatorActivitySummary = new ArrayList<HashMap<String, Object>>();
			List<Object[]> workflowRAGActivitySummaryCount = workflowStatusPolicyService.getWorkflowRAGActivitySummaryCount(productId,entityTypeId);
			HashMap<String, HashMap<String, Object>> ragSummaryMap = new HashMap<String, HashMap<String, Object>>(); 
			for (Object[] row : workflowRAGActivitySummaryCount) {
				
				String indicator=(String)row[8];
				String productName =(String)row[10];
				String activityName =(String)row[11];
				Integer activityId =(Integer)row[12];
				
				if(ragSummaryMap.containsKey(activityId+"")){
					Integer indicatorValue = 0;
					if(ragSummaryMap.get(activityId+"").containsKey(indicator)) {
						indicatorValue = (Integer)ragSummaryMap.get(activityId+"").get(indicator) + ((BigInteger)row[9]).intValue();
					}else{
						indicatorValue = ((BigInteger)row[9]).intValue();
					}
					ragSummaryMap.get(activityId+"").put("entityTypeId", (Integer)row[0]);
					ragSummaryMap.get(activityId+"").put(indicator, indicatorValue);
					ragSummaryMap.get(activityId+"").put("productName", productName);
					ragSummaryMap.get(activityId+"").put("activityId", activityId);
					ragSummaryMap.get(activityId+"").put("activityName", activityName);
				}else{
					HashMap<String, Object> newIndicator = new HashMap<String, Object>();
					Integer indicatorValue = ((BigInteger)row[9]).intValue();
					newIndicator.put(indicator, indicatorValue);
					newIndicator.put("entityTypeId", (Integer)row[0]);
					newIndicator.put("productName", productName);
					newIndicator.put("activityId", activityId);
					newIndicator.put("activityName", activityName);
					ragSummaryMap.put(activityId+"", newIndicator);
				}
				
			}
			for(Entry<String, HashMap<String, Object>> statusSummarys : ragSummaryMap.entrySet()){
			
				if(!ragSummaryMap.get(statusSummarys.getKey()).containsKey("red")){
					ragSummaryMap.get(statusSummarys.getKey()).put("red", 0);
				}
				
				if(!ragSummaryMap.get(statusSummarys.getKey()).containsKey("orange")){
					ragSummaryMap.get(statusSummarys.getKey()).put("orange", 0);
				}
				if(!ragSummaryMap.get(statusSummarys.getKey()).containsKey("green")){
					ragSummaryMap.get(statusSummarys.getKey()).put("green", 0);
				}
				productWorkflowRAGIndicatorActivitySummary.add(ragSummaryMap.get(statusSummarys.getKey()));
			}
			jTableResponse = new JTableResponse("OK", productWorkflowRAGIndicatorActivitySummary, productWorkflowRAGIndicatorActivitySummary.size());
			log.info("workflow.RAG.indicator.product.activity.summary completed");
		}catch(Exception e) {
			log.error("Error in"+e);
		}
		return jTableResponse;
	}
	
	
	@RequestMapping(value = "workflow.RAG.summary.SLA.indicator.detail", method = RequestMethod.POST, produces = "application/json")
	public @ResponseBody JTableResponse listRAViewSLADetail(HttpServletRequest request, @RequestParam Integer engagementId, @RequestParam Integer productId, @RequestParam Integer entityTypeId,@RequestParam String indicator,@RequestParam Integer categoryId,Integer jtStartIndex,Integer jtPageSize) {
		JTableResponse jTableResponse=null;
		try{
			List<JsonWorkflowSLAInstance> jsonWorkflowSLAInstanceList = new ArrayList<JsonWorkflowSLAInstance>();
			UserList user = (UserList) request.getSession().getAttribute("USER");
			if(entityTypeId == IDPAConstants.ENTITY_TASK_TYPE || entityTypeId == IDPAConstants.ENTITY_TASK) {
				List<ActivityTask> listSLAIndicatorTaskList=workflowStatusPolicyService.getWorkflowSLATaskDetailRAGView(engagementId,productId, entityTypeId,indicator,jtStartIndex, jtPageSize);
				if(listSLAIndicatorTaskList!=null && listSLAIndicatorTaskList.size()>0) {
					List<JsonActivityTask> jsonActivityTasks = new ArrayList<JsonActivityTask>(); 
					HashMap<Integer, ActivityTask> activityTaskDeatils = new HashMap<Integer, ActivityTask>();
					for(ActivityTask activityTask: listSLAIndicatorTaskList){
						JsonActivityTask jsonActivityTask=new JsonActivityTask(activityTask);
						activityTaskDeatils.put(activityTask.getActivityTaskId(), activityTask);
						jsonActivityTasks.add(jsonActivityTask);
					}
					workflowStatusPolicyService.setInstanceIndicators(defaultEngagementId,productId,null, IDPAConstants.ENTITY_TASK_TYPE, jsonActivityTasks, IDPAConstants.ENTITY_TASK,user, null, null, null,IDPAConstants.ENABLE_PENDING_WITH_COLUMN);
					for(JsonActivityTask jsonActivityTask : jsonActivityTasks){
						ActivityTask activityTask = activityTaskDeatils.get(jsonActivityTask.getActivityTaskId());
						JsonWorkflowSLAInstance jsonWorkflowSLAInstance=workflowStatusPolicyService.setJsonWorkflowSLAInstance(jsonActivityTask, IDPAConstants.WORKFLOW_LIFE_CYCLE_TYPE);
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
			} else if(entityTypeId == IDPAConstants.ENTITY_ACTIVITY_TYPE || entityTypeId == IDPAConstants.ENTITY_ACTIVITY_ID) {
				List<Activity> listSLAIndicatorActivityList=workflowStatusPolicyService.getWorkflowSLAActivityDetailRAGView(engagementId,productId, entityTypeId,indicator,categoryId, jtStartIndex, jtPageSize);
				if(listSLAIndicatorActivityList!=null && listSLAIndicatorActivityList.size()>0) {
					List<JsonActivity> jsonActivities = new ArrayList<JsonActivity>(); 
					HashMap<Integer, Activity> activityDetails = new HashMap<Integer, Activity>();
					for(Activity activity: listSLAIndicatorActivityList){
						JsonActivity jsonActivity=new JsonActivity(activity);
						activityDetails.put(activity.getActivityId(), activity);
						jsonActivities.add(jsonActivity);
					}
					Date modifiedAfterDate = commonService.getModifiedAfterDate((Date)request.getSession().getAttribute("userLoginTime"), (Date)request.getSession().getAttribute("userLogoutTime"));
					workflowStatusPolicyService.setInstanceIndicators(defaultEngagementId,productId,null, IDPAConstants.ENTITY_ACTIVITY_TYPE, jsonActivities, IDPAConstants.ENTITY_ACTIVITY_ID,user, modifiedAfterDate, null, null,IDPAConstants.ENABLE_PENDING_WITH_COLUMN);
					for(JsonActivity jsonActivity : jsonActivities){
						Activity activity = activityDetails.get(jsonActivity.getActivityId());
						JsonWorkflowSLAInstance jsonWorkflowSLAInstance=workflowStatusPolicyService.setJsonWorkflowSLAInstance(jsonActivity, IDPAConstants.WORKFLOW_LIFE_CYCLE_TYPE);
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
				List<ActivityWorkPackage> listSLAIndicatorActivityWorkpackageList=workflowStatusPolicyService.getWorkflowSLAWorkpackageDetailRAGView(engagementId,productId, entityTypeId, indicator, jtStartIndex, jtPageSize);
				if(listSLAIndicatorActivityWorkpackageList!=null && listSLAIndicatorActivityWorkpackageList.size()>0) {
					List<JsonActivityWorkPackage> jsonActivityWorkPackages = new ArrayList<JsonActivityWorkPackage>();
					HashMap<Integer, ActivityWorkPackage> activityWorkpackageDetails = new HashMap<Integer, ActivityWorkPackage>();
					for(ActivityWorkPackage activityWorkPackage : listSLAIndicatorActivityWorkpackageList){
						JsonActivityWorkPackage jsonActivityWorkPackage = new JsonActivityWorkPackage(activityWorkPackage);
						activityWorkpackageDetails.put(activityWorkPackage.getActivityWorkPackageId(), activityWorkPackage);
						jsonActivityWorkPackages.add(jsonActivityWorkPackage);
					}
					workflowStatusPolicyService.setInstanceIndicators(defaultEngagementId,productId,null, IDPAConstants.ENTITY_ACTIVITY_WORKPACKAGE_ID, jsonActivityWorkPackages, IDPAConstants.ENTITY_ACTIVITY_WORKPACKAGE_ID,user, null, null, null,IDPAConstants.ENABLE_PENDING_WITH_COLUMN);
					for(JsonActivityWorkPackage jsonActivityWorkPackage : jsonActivityWorkPackages){
						ActivityWorkPackage activityWorkPackage = activityWorkpackageDetails.get(jsonActivityWorkPackage.getActivityWorkPackageId());
						JsonWorkflowSLAInstance jsonWorkflowSLAInstance = workflowStatusPolicyService.setJsonWorkflowSLAInstance(jsonActivityWorkPackage, IDPAConstants.WORKFLOW_LIFE_CYCLE_TYPE);
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
			log.info("workflow.RAG.summary.SLA.indicator.detail Completed");
		}catch(Exception e) {
			log.error("Error in listWorkflowRAGSummarySLADetail",e);
		}
		return jTableResponse;
		}
	
	
	@RequestMapping(value = "workflow.entity.instance.status.category.summary", method = RequestMethod.POST, produces = "application/json")
	public @ResponseBody JTableResponse listWorkflowEntityInstanceStatusCategorySummary(HttpServletRequest request, @RequestParam Integer engagementId,@RequestParam Integer productId,@RequestParam Integer entityTypeId,@RequestParam Integer entityId,@RequestParam Integer entityParentInstanceId) {
		JTableResponse jTableResponse=null;
		try{
			if(entityId != null && entityId == 0){
				entityId = null;
			}
			List<Object[]> workflowStatusSummaryList = workflowStatusPolicyService.listWorkflowStatusCategorySummaryCount(engagementId,productId, entityTypeId, entityId, entityParentInstanceId);
			ArrayList<HashMap<String, Object>> workflowStatusSummarys = new ArrayList<HashMap<String, Object>>();
			for (Object[] row : workflowStatusSummaryList) {
				HashMap<String, Object> workflowStatusSummary =new HashMap<String, Object>();
				workflowStatusSummary.put("workflowStatusCategoryName", (String)row[0]);
				workflowStatusSummary.put("workflowStatusCount", (BigInteger)row[1]);
				workflowStatusSummary.put("workflowStatusCategoryId", (Integer)row[2]);
				workflowStatusSummary.put("entityId", (Integer)row[3]);
				workflowStatusSummary.put("typeName", (String)row[4]);
				workflowStatusSummarys.add(workflowStatusSummary);					
			}				
			jTableResponse = new JTableResponse("OK", workflowStatusSummarys, workflowStatusSummaryList.size());
		}catch(Exception e) {
			log.error("Error in listWorkflowEntityInstanceStatusCategorySummary"+e);
		}
		return jTableResponse;
	}
	
	
	@RequestMapping(value = "workflow.RAG.indicator.engagement.summary", method = RequestMethod.POST, produces = "application/json")
	public @ResponseBody JTableResponse listWorkflowRAGIndicatorEngagementBasedSummary(HttpServletRequest request,@RequestParam Integer engagementId, @RequestParam Integer productId, @RequestParam Integer entityTypeId, @RequestParam Integer entityParentInstanceId) {
		JTableResponse jTableResponse=null;
		try{
			if(entityTypeId == 0){
				entityTypeId = null;
			}
			ArrayList<HashMap<String, Object>> productWorkflowRAGIndicatorWorkpackageSummary = new ArrayList<HashMap<String, Object>>();
			List<Object[]> workflowRAGWorkpackageSummaryCount = workflowStatusPolicyService.getWorkflowRAGEngagementSummaryCount(engagementId,productId,entityTypeId,entityParentInstanceId);
			List<Object[]> workflowRAGWorkpackageSummaryCompletedCount = workflowStatusPolicyService.getRAGViewEngagementSummaryCompletedCount(engagementId,productId,entityTypeId,entityParentInstanceId);
			List<Object[]> workflowRAGWorkpackageSummaryAbortedCount = workflowStatusPolicyService.getRAGViewEngagementSummaryAbortCount(engagementId,productId,entityTypeId,entityParentInstanceId);
			HashMap<String, HashMap<String, Object>> ragSummaryMap = new HashMap<String, HashMap<String, Object>>(); 
			for (Object[] row : workflowRAGWorkpackageSummaryCount) {
				
				String indicator=(String)row[8];
				Integer prodId =(Integer)row[10];
				String productName =(String)row[11];
				Integer engmentId =(Integer)row[12];
				String engagementName =(String)row[13];
				
				Integer workpackageId =(Integer)row[14];
				String workpackageName =(String)row[15];
				
				if(ragSummaryMap.containsKey(prodId+""+workpackageId)){
					Integer indicatorValue = 0;
					if(ragSummaryMap.get(prodId+""+workpackageId).containsKey(indicator)) {
						indicatorValue = (Integer)ragSummaryMap.get(prodId+""+workpackageId).get(indicator) + ((BigInteger)row[9]).intValue();
					}else{
						indicatorValue = ((BigInteger)row[9]).intValue();
					}
					ragSummaryMap.get(prodId+""+workpackageId).put("entityTypeId", (Integer)row[0]);
					ragSummaryMap.get(prodId+""+workpackageId).put(indicator, indicatorValue);
					ragSummaryMap.get(prodId+""+workpackageId).put("productId", prodId);
					ragSummaryMap.get(prodId+""+workpackageId).put("productName", productName);
					ragSummaryMap.get(prodId+""+workpackageId).put("engagementId", engmentId);
					ragSummaryMap.get(prodId+""+workpackageId).put("engagementName", engagementName);
					ragSummaryMap.get(prodId+""+workpackageId).put("workpackageId", workpackageId);
					ragSummaryMap.get(prodId+""+workpackageId).put("workpackageName", workpackageName);
					
				}else{
					HashMap<String, Object> newIndicator = new HashMap<String, Object>();
					Integer indicatorValue = ((BigInteger)row[9]).intValue();
					newIndicator.put(indicator, indicatorValue);
					newIndicator.put("entityTypeId", (Integer)row[0]);
					newIndicator.put("productName", productName);
					newIndicator.put("productId", prodId);
					newIndicator.put("engagementId", engmentId);
					newIndicator.put("engagementName", engagementName);
					newIndicator.put("workpackageId", workpackageId);
					newIndicator.put("workpackageName", workpackageName);
					ragSummaryMap.put(prodId+""+workpackageId, newIndicator);
				}
				
			}
			
			for (Object[] row : workflowRAGWorkpackageSummaryCompletedCount) {
				
					String indicator="completedCount";
				
					Integer prodId =(Integer)row[10];
					String productName =(String)row[11];
					Integer engmentId =(Integer)row[12];
					String engagementName =(String)row[13];
					
					Integer workpackageId =(Integer)row[14];
					String workpackageName =(String)row[15];
					
					if(ragSummaryMap.containsKey(prodId+""+workpackageId)){
						Integer indicatorValue = 0;
						if(ragSummaryMap.get(prodId+""+workpackageId).containsKey(indicator)) {
							indicatorValue = (Integer)ragSummaryMap.get(prodId+""+workpackageId).get(indicator) + ((BigInteger)row[9]).intValue();
						}else{
							indicatorValue = ((BigInteger)row[9]).intValue();
						}
						ragSummaryMap.get(prodId+""+workpackageId).put("entityTypeId", (Integer)row[0]);
						ragSummaryMap.get(prodId+""+workpackageId).put(indicator, indicatorValue);
						ragSummaryMap.get(prodId+""+workpackageId).put("productId", prodId);
						ragSummaryMap.get(prodId+""+workpackageId).put("productName", productName);
						ragSummaryMap.get(prodId+""+workpackageId).put("engagementId", engmentId);
						ragSummaryMap.get(prodId+""+workpackageId).put("engagementName", engagementName);
						ragSummaryMap.get(prodId+""+workpackageId).put("workpackageId", workpackageId);
						ragSummaryMap.get(prodId+""+workpackageId).put("workpackageName", workpackageName);
						
					}else{
						HashMap<String, Object> newIndicator = new HashMap<String, Object>();
						Integer indicatorValue = ((BigInteger)row[9]).intValue();
						newIndicator.put(indicator, indicatorValue);
						newIndicator.put("entityTypeId", (Integer)row[0]);
						newIndicator.put("productName", productName);
						newIndicator.put("productId", prodId);
						newIndicator.put("engagementId", engmentId);
						newIndicator.put("engagementName", engagementName);
						newIndicator.put("workpackageId", workpackageId);
						newIndicator.put("workpackageName", workpackageName);
						
						ragSummaryMap.put(prodId+""+workpackageId, newIndicator);
					}
					
				}
			
			for (Object[] row : workflowRAGWorkpackageSummaryAbortedCount) {
				
				String indicator="abortCount";
					Integer prodId =(Integer)row[10];
					String productName =(String)row[11];
					Integer engmentId =(Integer)row[12];
					String engagementName =(String)row[13];
					
					Integer workpackageId =(Integer)row[14];
					String workpackageName =(String)row[15];
					
					if(ragSummaryMap.containsKey(prodId+""+workpackageId)){
						Integer indicatorValue = 0;
						if(ragSummaryMap.get(prodId+""+workpackageId).containsKey(indicator)) {
							indicatorValue = (Integer)ragSummaryMap.get(prodId+""+workpackageId).get(indicator) + ((BigInteger)row[9]).intValue();
						}else{
							indicatorValue = ((BigInteger)row[9]).intValue();
						}
						ragSummaryMap.get(prodId+""+workpackageId).put("entityTypeId", (Integer)row[0]);
						ragSummaryMap.get(prodId+""+workpackageId).put(indicator, indicatorValue);
						ragSummaryMap.get(prodId+""+workpackageId).put("productId", prodId);
						ragSummaryMap.get(prodId+""+workpackageId).put("productName", productName);
						ragSummaryMap.get(prodId+""+workpackageId).put("engagementId", engmentId);
						ragSummaryMap.get(prodId+""+workpackageId).put("engagementName", engagementName);
						ragSummaryMap.get(prodId+""+workpackageId).put("workpackageId", workpackageId);
						ragSummaryMap.get(prodId+""+workpackageId).put("workpackageName", workpackageName);
						
					}else{
						HashMap<String, Object> newIndicator = new HashMap<String, Object>();
						Integer indicatorValue = ((BigInteger)row[9]).intValue();
						newIndicator.put(indicator, indicatorValue);
						newIndicator.put("entityTypeId",(Integer)row[0]);
						newIndicator.put("productName", productName);
						newIndicator.put("productId", prodId);
						newIndicator.put("engagementId", engmentId);
						newIndicator.put("engagementName", engagementName);
						newIndicator.put("workpackageId", workpackageId);
						newIndicator.put("workpackageName", workpackageName);
						
						
						
						ragSummaryMap.put(prodId+""+workpackageId, newIndicator);
					}
					
				}
			for(Entry<String, HashMap<String, Object>> statusSummarys : ragSummaryMap.entrySet()){
			
				if(!ragSummaryMap.get(statusSummarys.getKey()).containsKey("red")){
					ragSummaryMap.get(statusSummarys.getKey()).put("red", 0);
				}
				
				if(!ragSummaryMap.get(statusSummarys.getKey()).containsKey("orange")){
					ragSummaryMap.get(statusSummarys.getKey()).put("orange", 0);
				}
				if(!ragSummaryMap.get(statusSummarys.getKey()).containsKey("green")){
					ragSummaryMap.get(statusSummarys.getKey()).put("green", 0);
				}
				if(!ragSummaryMap.get(statusSummarys.getKey()).containsKey("completedCount")){
					ragSummaryMap.get(statusSummarys.getKey()).put("completedCount", 0);
				}
				if(!ragSummaryMap.get(statusSummarys.getKey()).containsKey("abortCount")){
					ragSummaryMap.get(statusSummarys.getKey()).put("abortCount", 0);
				}
				productWorkflowRAGIndicatorWorkpackageSummary.add(ragSummaryMap.get(statusSummarys.getKey()));
			}
			jTableResponse = new JTableResponse("OK", productWorkflowRAGIndicatorWorkpackageSummary, productWorkflowRAGIndicatorWorkpackageSummary.size());
		}catch(Exception e) {
			log.error("Error in"+e);
		}
		return jTableResponse;
	}
	
	
	@RequestMapping(value = "workflow.RAG.indicator.activity.category.summary", method = RequestMethod.POST, produces = "application/json")
	public @ResponseBody JTableResponse listWorkflowRAGIndicatorActivityCategoryBasedSummary(HttpServletRequest request,@RequestParam Integer engagementId, @RequestParam Integer productId, @RequestParam Integer entityTypeId, @RequestParam Integer entityParentInstanceId) {
		JTableResponse jTableResponse=null;
		try{
			if(entityTypeId == 0){
				entityTypeId = null;
			}
			ArrayList<HashMap<String, Object>> productWorkflowRAGIndicatorWorkpackageSummary = new ArrayList<HashMap<String, Object>>();
			List<Object[]> workflowRAGActivityCategorySummaryCount = workflowStatusPolicyService.getWorkflowRAGActivityCategoriesSummaryCount(engagementId,productId,entityTypeId,entityParentInstanceId);
			List<Object[]> workflowRAGActivityCategorySummaryCompletedCount = workflowStatusPolicyService.getWorkflowRAGActivityCategoriesSummaryCompletedCount(engagementId,productId,entityTypeId,entityParentInstanceId);
			List<Object[]> workflowRAGActivityCategorySummaryAbortedCount = workflowStatusPolicyService.getWorkflowRAGActivityCategoriesSummaryAbortCount(engagementId,productId,entityTypeId,entityParentInstanceId);
			HashMap<String, HashMap<String, Object>> ragSummaryMap = new HashMap<String, HashMap<String, Object>>(); 
			for (Object[] row : workflowRAGActivityCategorySummaryCount) {
				
				String indicator=(String)row[8];
				Integer prodId =(Integer)row[10];
				String productName =(String)row[11];
				Integer engmentId =(Integer)row[12];
				String engagementName =(String)row[13];
				
				Integer workpackageId =(Integer)row[14];
				String workpackageName =(String)row[15];
				
				Integer activityCategoryId =(Integer)row[16];
				String activityCategoryName =(String)row[17];
				
				if(ragSummaryMap.containsKey(prodId+""+workpackageId+""+activityCategoryId)){
					Integer indicatorValue = 0;
					if(ragSummaryMap.get(prodId+""+workpackageId+""+activityCategoryId).containsKey(indicator)) {
						indicatorValue = (Integer)ragSummaryMap.get(prodId+""+workpackageId+""+activityCategoryId).get(indicator) + ((BigInteger)row[9]).intValue();
					}else{
						indicatorValue = ((BigInteger)row[9]).intValue();
					}
					ragSummaryMap.get(prodId+""+workpackageId+""+activityCategoryId).put("entityTypeId", (Integer)row[0]);
					ragSummaryMap.get(prodId+""+workpackageId+""+activityCategoryId).put(indicator, indicatorValue);
					ragSummaryMap.get(prodId+""+workpackageId+""+activityCategoryId).put("productId", prodId);
					ragSummaryMap.get(prodId+""+workpackageId+""+activityCategoryId).put("productName", productName);
					ragSummaryMap.get(prodId+""+workpackageId+""+activityCategoryId).put("engagementId", engmentId);
					ragSummaryMap.get(prodId+""+workpackageId+""+activityCategoryId).put("engagementName", engagementName);
					ragSummaryMap.get(prodId+""+workpackageId+""+activityCategoryId).put("workpackageId", workpackageId);
					ragSummaryMap.get(prodId+""+workpackageId+""+activityCategoryId).put("workpackageName", workpackageName);
					ragSummaryMap.get(prodId+""+workpackageId+""+activityCategoryId).put("activityCategoryId", activityCategoryId);
					ragSummaryMap.get(prodId+""+workpackageId+""+activityCategoryId).put("activityCategoryName", activityCategoryName);
					
				}else{
					HashMap<String, Object> newIndicator = new HashMap<String, Object>();
					Integer indicatorValue = ((BigInteger)row[9]).intValue();
					newIndicator.put(indicator, indicatorValue);
					newIndicator.put("entityTypeId", (Integer)row[0]);
					newIndicator.put("productName", productName);
					newIndicator.put("productId", prodId);
					newIndicator.put("engagementId", engmentId);
					newIndicator.put("engagementName", engagementName);
					newIndicator.put("workpackageId", workpackageId);
					newIndicator.put("workpackageName", workpackageName);
					newIndicator.put("activityCategoryId", activityCategoryId);
					newIndicator.put("activityCategoryName", activityCategoryName);
					
					ragSummaryMap.put(prodId+""+workpackageId+""+activityCategoryId, newIndicator);
				}
				
			}
			
			
			for (Object[] row : workflowRAGActivityCategorySummaryCompletedCount) {
				
					String indicator ="completedCount";
					Integer prodId =(Integer)row[10];
					String productName =(String)row[11];
					Integer engmentId =(Integer)row[12];
					String engagementName =(String)row[13];
					
					Integer workpackageId =(Integer)row[14];
					String workpackageName =(String)row[15];
					
					Integer activityCategoryId =(Integer)row[16];
					String activityCategoryName =(String)row[17];
					
					if(ragSummaryMap.containsKey(prodId+""+workpackageId+""+activityCategoryId)){
						Integer indicatorValue = 0;
						if(ragSummaryMap.get(prodId+""+workpackageId+""+activityCategoryId).containsKey(indicator)) {
							indicatorValue = (Integer)ragSummaryMap.get(prodId+""+workpackageId+""+activityCategoryId).get(indicator) + ((BigInteger)row[9]).intValue();
						}else{
							indicatorValue = ((BigInteger)row[9]).intValue();
						}
						ragSummaryMap.get(prodId+""+workpackageId+""+activityCategoryId).put("entityTypeId", (Integer)row[0]);
						ragSummaryMap.get(prodId+""+workpackageId+""+activityCategoryId).put(indicator, indicatorValue);
						ragSummaryMap.get(prodId+""+workpackageId+""+activityCategoryId).put("productId", prodId);
						ragSummaryMap.get(prodId+""+workpackageId+""+activityCategoryId).put("productName", productName);
						ragSummaryMap.get(prodId+""+workpackageId+""+activityCategoryId).put("engagementId", engmentId);
						ragSummaryMap.get(prodId+""+workpackageId+""+activityCategoryId).put("engagementName", engagementName);
						ragSummaryMap.get(prodId+""+workpackageId+""+activityCategoryId).put("workpackageId", workpackageId);
						ragSummaryMap.get(prodId+""+workpackageId+""+activityCategoryId).put("workpackageName", workpackageName);
						ragSummaryMap.get(prodId+""+workpackageId+""+activityCategoryId).put("activityCategoryId", activityCategoryId);
						ragSummaryMap.get(prodId+""+workpackageId+""+activityCategoryId).put("activityCategoryName", activityCategoryName);
						
					}else{
						HashMap<String, Object> newIndicator = new HashMap<String, Object>();
						Integer indicatorValue = ((BigInteger)row[9]).intValue();
						newIndicator.put(indicator, indicatorValue);
						newIndicator.put("entityTypeId", (Integer)row[0]);
						newIndicator.put("productName", productName);
						newIndicator.put("productId", prodId);
						newIndicator.put("engagementId", engmentId);
						newIndicator.put("engagementName", engagementName);
						newIndicator.put("workpackageId", workpackageId);
						newIndicator.put("workpackageName", workpackageName);
						newIndicator.put("activityCategoryId", activityCategoryId);
						newIndicator.put("activityCategoryName", activityCategoryName);
						
						ragSummaryMap.put(prodId+""+workpackageId+""+activityCategoryId, newIndicator);
					}
					
				}
			
			
			for (Object[] row : workflowRAGActivityCategorySummaryAbortedCount) {
				
				String indicator="abortCount";
					Integer prodId =(Integer)row[10];
					String productName =(String)row[11];
					Integer engmentId =(Integer)row[12];
					String engagementName =(String)row[13];
					
					Integer workpackageId =(Integer)row[14];
					String workpackageName =(String)row[15];
					
					Integer activityCategoryId =(Integer)row[16];
					String activityCategoryName =(String)row[17];
					
					if(ragSummaryMap.containsKey(prodId+""+workpackageId+""+activityCategoryId)){
						Integer indicatorValue = 0;
						if(ragSummaryMap.get(prodId+""+workpackageId+""+activityCategoryId).containsKey(indicator)) {
							indicatorValue = (Integer)ragSummaryMap.get(prodId+""+workpackageId+""+activityCategoryId).get(indicator) + ((BigInteger)row[9]).intValue();
						}else{
							indicatorValue = ((BigInteger)row[9]).intValue();
						}
						ragSummaryMap.get(prodId+""+workpackageId+""+activityCategoryId).put("entityTypeId", (Integer)row[0]);
						ragSummaryMap.get(prodId+""+workpackageId+""+activityCategoryId).put(indicator, indicatorValue);
						ragSummaryMap.get(prodId+""+workpackageId+""+activityCategoryId).put("productId", prodId);
						ragSummaryMap.get(prodId+""+workpackageId+""+activityCategoryId).put("productName", productName);
						ragSummaryMap.get(prodId+""+workpackageId+""+activityCategoryId).put("engagementId", engmentId);
						ragSummaryMap.get(prodId+""+workpackageId+""+activityCategoryId).put("engagementName", engagementName);
						ragSummaryMap.get(prodId+""+workpackageId+""+activityCategoryId).put("workpackageId", workpackageId);
						ragSummaryMap.get(prodId+""+workpackageId+""+activityCategoryId).put("workpackageName", workpackageName);
						ragSummaryMap.get(prodId+""+workpackageId+""+activityCategoryId).put("activityCategoryId", activityCategoryId);
						ragSummaryMap.get(prodId+""+workpackageId+""+activityCategoryId).put("activityCategoryName", activityCategoryName);
						
					}else{
						HashMap<String, Object> newIndicator = new HashMap<String, Object>();
						Integer indicatorValue = ((BigInteger)row[9]).intValue();
						newIndicator.put(indicator, indicatorValue);
						newIndicator.put("entityTypeId", (Integer)row[0]);
						newIndicator.put("productName", productName);
						newIndicator.put("productId", prodId);
						newIndicator.put("engagementId", engmentId);
						newIndicator.put("engagementName", engagementName);
						newIndicator.put("workpackageId", workpackageId);
						newIndicator.put("workpackageName", workpackageName);
						newIndicator.put("activityCategoryId", activityCategoryId);
						newIndicator.put("activityCategoryName", activityCategoryName);
						
						
						ragSummaryMap.put(prodId+""+workpackageId+""+activityCategoryId, newIndicator);
					}
					
				}
			
			
			for(Entry<String, HashMap<String, Object>> statusSummarys : ragSummaryMap.entrySet()){
			
				if(!ragSummaryMap.get(statusSummarys.getKey()).containsKey("red")){
					ragSummaryMap.get(statusSummarys.getKey()).put("red", 0);
				}
				
				if(!ragSummaryMap.get(statusSummarys.getKey()).containsKey("orange")){
					ragSummaryMap.get(statusSummarys.getKey()).put("orange", 0);
				}
				if(!ragSummaryMap.get(statusSummarys.getKey()).containsKey("green")){
					ragSummaryMap.get(statusSummarys.getKey()).put("green", 0);
				}
				if(!ragSummaryMap.get(statusSummarys.getKey()).containsKey("completedCount")){
					ragSummaryMap.get(statusSummarys.getKey()).put("completedCount", 0);
				}
				if(!ragSummaryMap.get(statusSummarys.getKey()).containsKey("abortCount")){
					ragSummaryMap.get(statusSummarys.getKey()).put("abortCount", 0);
				}
				productWorkflowRAGIndicatorWorkpackageSummary.add(ragSummaryMap.get(statusSummarys.getKey()));
			}
			jTableResponse = new JTableResponse("OK", productWorkflowRAGIndicatorWorkpackageSummary, productWorkflowRAGIndicatorWorkpackageSummary.size());
		}catch(Exception e) {
			log.error("Error in"+e);
		}
		return jTableResponse;
	}
	
	@RequestMapping(value="workflow.event.tracker.update.by.eventId",method=RequestMethod.POST ,produces="application/json")
	public @ResponseBody JTableSingleResponse updateWorkflowEventTracker(HttpServletRequest request,
			@RequestParam Integer entityInstanceId,
			@RequestParam Integer entityTypeId,
			@RequestParam Integer effort){
		
		JTableSingleResponse jTableSingleResponse = null;
		try {
				if(entityInstanceId != null && entityInstanceId == 0){
					entityInstanceId = null;
				}
				
				WorkflowEvent workflowEvent= workflowEventService.getLastEventInstanceByEntityInstanceId(entityTypeId, entityInstanceId);
				
				if(workflowEvent != null){
					Integer totalEffort=workflowEvent.getActualEffort()+effort;
					workflowEvent.setActualEffort(totalEffort);
					workflowEvent.setLastUpdatedDate(new Date());
					workflowEventService.updateWorkflowEvent(workflowEvent);
					jTableSingleResponse = new JTableSingleResponse("OK");
				}
	    } 
		catch (Exception e) {
            jTableSingleResponse = new JTableSingleResponse("ERROR","Unable to perform update effort Tracker!");
            log.error("JSON ERROR", e);	            
        }
	    return jTableSingleResponse;
    }
	
}
