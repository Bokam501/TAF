/**
 * 
 */
package com.hcl.atf.taf.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import com.hcl.atf.taf.constants.IDPAConstants;
import com.hcl.atf.taf.controller.utilities.DateUtility;
import com.hcl.atf.taf.controller.utilities.RestResponseUtility;
import com.hcl.atf.taf.dao.CommonDAO;
import com.hcl.atf.taf.dao.ProductMasterDAO;
import com.hcl.atf.taf.dao.ProductTeamResourcesDao;
import com.hcl.atf.taf.model.Activity;
import com.hcl.atf.taf.model.ActivityMaster;
import com.hcl.atf.taf.model.ActivityWorkPackage;
import com.hcl.atf.taf.model.Comments;
import com.hcl.atf.taf.model.EntityMaster;
import com.hcl.atf.taf.model.ProductBuild;
import com.hcl.atf.taf.model.ProductMaster;
import com.hcl.atf.taf.model.ProductVersionListMaster;
import com.hcl.atf.taf.model.UserList;
import com.hcl.atf.taf.model.json.JsonActivity;
import com.hcl.atf.taf.model.json.jtable.JTableResponse;
import com.hcl.atf.taf.model.json.jtable.JTableSingleResponse;
import com.hcl.atf.taf.service.ActivityService;
import com.hcl.atf.taf.service.ActivityTypeService;
import com.hcl.atf.taf.service.ActivityWorkPackageService;
import com.hcl.atf.taf.service.CommonService;
import com.hcl.atf.taf.service.CustomFieldService;
import com.hcl.atf.taf.service.ProductListService;
import com.hcl.atf.taf.service.UserListService;
import com.hcl.atf.taf.service.WorkPackageService;
import com.hcl.ilcm.workflow.dao.WorkflowActivityDAO;
import com.hcl.ilcm.workflow.dao.WorkflowStatusPolicyDAO;
import com.hcl.ilcm.workflow.model.WorkflowEvent;
import com.hcl.ilcm.workflow.model.WorkflowStatus;
import com.hcl.ilcm.workflow.model.WorkflowStatusActor;
import com.hcl.ilcm.workflow.model.json.JsonWorkflowStatusActor;
import com.hcl.ilcm.workflow.service.ConfigurationWorkFlowService;
import com.hcl.ilcm.workflow.service.WorkflowEventService;
import com.hcl.ilcm.workflow.service.WorkflowStatusPolicyService;
import com.hcl.ilcm.workflow.service.WorkflowStatusService;

/**
 * @author silambarasur
 *
 */
@Path("/atlas/ilcm")
public class ActivityRestController {
	private static final Log log = LogFactory.getLog(ActivityRestController.class);
	

	@Autowired
	private UserListService userListService;
	
	@Autowired
	private HttpServletRequest request;
	
	@Autowired
	private ProductTeamResourcesDao productTeamResourcesDao;
	@Autowired
	private ProductMasterDAO productMasterDAO;
	@Autowired
	private ProductListService productListService;
	@Autowired
	private WorkflowEventService workflowEventService;
	@Autowired
	private ActivityService activityService;
	@Autowired
	private ActivityTypeService activityTypeService;
	@Autowired
	private ConfigurationWorkFlowService configurationWorkFlowService;
	@Autowired
	private ActivityWorkPackageService activityWorkPackageService;
	@Autowired
	private WorkflowActivityDAO workflowActivityDAO;
	
	@Autowired
	private WorkflowStatusService workflowStatusService;
	
	@Autowired
	private CustomFieldService customFieldService;
	
	@Autowired
	private CommonService commonService;
	
	@Autowired
	private WorkflowStatusPolicyService workflowStatusPolicyService;
	
	@Autowired
	private WorkPackageService workPackageService;
	
	@Autowired
	private WorkflowStatusPolicyDAO workflowStatusPolicyDAO;
	
	
	@Value("#{ilcmProps['USER_AUTHENTICATION_REQUIRED']}")
    private String atlasUserAuthenticationRequired;
	
	
	@POST
	@Path("/activityManagement/query/getActivites")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response getActivites(String activityFormData) throws JSONException {
	SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
	JSONArray arr = new JSONArray();
	try {
		
		JSONParser parser = new JSONParser();
		org.json.simple.JSONObject jsonFormatObject = (org.json.simple.JSONObject) parser.parse(activityFormData);
		if(jsonFormatObject == null) {
			log.info("Could not parse request : /activityManagement/query/getActivites");
			return RestResponseUtility.prepareErrorResponseWithoutData("No inputs provided", "No inputs provided");
		}
		String userName=jsonFormatObject.get("user") != null ? jsonFormatObject.get("user").toString():"";
		String productName=(String)jsonFormatObject.get("productName") != null?jsonFormatObject.get("productName").toString():"";
		String productVersionName=(String)jsonFormatObject.get("productVersionName") != null ?jsonFormatObject.get("productVersionName").toString():"";
		String productBuildName=(String)jsonFormatObject.get("productBuildName") !=null ?jsonFormatObject.get("productBuildName").toString():"";
		String activityWorkPackageNames=(String)jsonFormatObject.get("activityWorkPackageName") != null ?jsonFormatObject.get("activityWorkPackageName").toString():"";
		String activityWorkPackageIds=(String)jsonFormatObject.get("activityWorkPackageId") != null ?jsonFormatObject.get("activityWorkPackageId").toString():"";
		String status=(String)jsonFormatObject.get("status") != null ?jsonFormatObject.get("status").toString():"";
		String statusTypes = (String)jsonFormatObject.get("statusType") != null ?jsonFormatObject.get("statusType").toString():"";
		String activityTypes = (String)jsonFormatObject.get("activityType") != null ?jsonFormatObject.get("activityType").toString():"";
		String activityAssignees = (String)jsonFormatObject.get("activityAssignee") != null ?jsonFormatObject.get("activityAssignee").toString():"";
		String activityReviewer = (String)jsonFormatObject.get("activityReviewer") != null ?jsonFormatObject.get("activityReviewer").toString():"";
		
		String plannedStartDateParam = (String)jsonFormatObject.get("plannedStartDate") != null ?jsonFormatObject.get("plannedStartDate").toString():"";
		String plannedEndDateParam = (String)jsonFormatObject.get("plannedEndDate") != null ?jsonFormatObject.get("plannedEndDate").toString():"";
		
		String actualStartDateParam = (String)jsonFormatObject.get("actualStartDate") != null ?jsonFormatObject.get("actualStartDate").toString():"";
		String actualEndDateParam = (String)jsonFormatObject.get("actualEndDate") != null ?jsonFormatObject.get("actualEndDate").toString():"";
		
		
		
		Integer productVersionId=0;
		Integer productBuildId=0;
		ProductMaster product= null;
		Integer entityId=28;
		Integer parentEntityInstanceId=0;
		Integer active=1;
		//Authenticate the user
	
		Response authResponse = authenticateUser(userName, "/activityManagement/query/getActivites");
		if (authResponse != null)
			return authResponse;
		UserList userList = userListService.getUserByLoginId(userName);
		request.getSession().setAttribute("USER", userList);	
			if(productName != null && !productName.trim().isEmpty()) {
				product=productMasterDAO.getProductByName(productName);
				if(product == null) {
					return RestResponseUtility.prepareErrorResponseWithoutData("Invalid ProductName", "Invalid ProductName");
				}
				//Check authorization for user
				authResponse = checkUserAuthorization(product.getProductId(), userName, "/activityManagement/query/getActivites");
				if (authResponse != null)
					return authResponse;
				
				if(productVersionName != null && !productVersionName.isEmpty()) {
					ProductVersionListMaster productVersion=productListService.getProductVersionListByProductIdAndVersionName(product.getProductId(), productVersionName);
					if(productVersion != null) {
						productVersionId=productVersion.getProductVersionListId();
					} else {
						return RestResponseUtility.prepareErrorResponseWithoutData("Invalid  Version Name", "Invalid  Version Name");
					}
				}
					if(productVersionId >0) {
						ProductBuild productBuild=productListService.getproductBuildByProductIdAndBuildName(productVersionId, productBuildName);
						if(productBuild != null) {
							productBuildId=productBuild.getProductBuildId();
						}else {
							return RestResponseUtility.prepareErrorResponseWithoutData("Invalid  Build Name", "Invalid  Build Name");
						}
					}
					List<Integer> activityWorkPackageIdList=new ArrayList<>();
						if(productBuildId >0) {
							if(activityWorkPackageIds != null && !activityWorkPackageIds.trim().isEmpty()) {
								for(String workpackageId : activityWorkPackageIds.split(",")){
									ActivityWorkPackage actityWp=activityWorkPackageService.getActivityWorkPackageById(Integer.parseInt(workpackageId), productBuildId);
									if(actityWp != null) {
										activityWorkPackageIdList.add(actityWp.getActivityWorkPackageId());
									}
								}
							} else if(activityWorkPackageNames != null && !activityWorkPackageNames.trim().isEmpty()){
								
								for(String workpackageName : activityWorkPackageNames.split(",")){
									ActivityWorkPackage actityWp=activityWorkPackageService.getActivityWorkPackageByName(workpackageName, productBuildId);
									if(actityWp != null) {
										activityWorkPackageIdList.add(actityWp.getActivityWorkPackageId());
									}
								}	
							}
							
							if((activityWorkPackageIds != null && !activityWorkPackageIds.isEmpty()) || (activityWorkPackageNames != null && !activityWorkPackageNames.trim().isEmpty())){
								if(activityWorkPackageIdList == null || activityWorkPackageIdList.size() ==0) {
									return RestResponseUtility.prepareErrorResponseWithoutData("Invalid Activity Workpackage Name or WorkpackageId ", "Invalid  Activity Workpackage Name or WorkpackageId");
								}
							}
						 }
					List<String> workflowStatusList= new ArrayList<>();
					if(status != null && !status.trim().isEmpty()) {
						for(String statusName : status.split(",")){
							workflowStatusList.add(statusName);
						}
					}
					List<String> workflowStatusTypeList = new ArrayList<>();
					if(statusTypes != null && !statusTypes.trim().isEmpty()) {
						for(String workflowStatusType : statusTypes.split(",")){
							workflowStatusTypeList.add(workflowStatusType);
						}
					}
					List<String> activityTypeList= new ArrayList<>();
					if(activityTypes != null && !activityTypes.trim().isEmpty()) {
						for(String activityType : activityTypes.split(",")){
							activityTypeList.add(activityType);
						}
					}
					
					List<String> activityAssigneeList= new ArrayList<>();
					if(activityAssignees != null && !activityAssignees.trim().isEmpty()) {
						for(String assignee : activityAssignees.split(",")){
							activityAssigneeList.add(assignee);
						}
					}
					
					List<String> activityReviewerList= new ArrayList<>();
					if(activityReviewer != null && !activityReviewer.trim().isEmpty()) {
						for(String reviewer : activityReviewer.split(",")){
							activityReviewerList.add(reviewer);
						}
					}
					
					
					Date plannedStartFromDate =null;
					Date plannedStartToDate =null;
					Date plannedEndFromDate = null;
					Date plannedEndToDate = null;
					Date actualStartFromDate = null;
					Date actualStartToDate = null;
					Date actualEndFromDate = null;
					Date actualEndToDate = null;
					
					String plannedStartDateSearch="";
					String plannedEndDateSearch="";
					String actualStartDateSearch="";
					String actualEndDateSearch="";
					
					if(plannedStartDateParam != null && !plannedStartDateParam.trim().isEmpty()) {
						 String planStartDate[]=plannedStartDateParam.split(" ");
						 try {
							 if(!planStartDate[0].isEmpty() && planStartDate[0].equalsIgnoreCase("between")) {
								 plannedStartDateSearch = planStartDate[0];
								 plannedStartFromDate= DateUtility.dateformatWithOutTime(planStartDate[1]);
								 plannedStartToDate= DateUtility.dateformatWithOutTime(planStartDate[2]);
								 if (ActivityDateValidation(plannedStartFromDate,plannedStartToDate)) {
										RestResponseUtility.prepareErrorResponseWithoutData("Planned start from date should be less than or equal to planned start to date","Planned start from date should be less than or equal to planned start to date");
								 }
							 } else {
								 plannedStartDateSearch = planStartDate[0];
								 plannedStartFromDate= DateUtility.dateformatWithOutTime(planStartDate[1]);
							 }
						 } catch(Exception e) {
							 return RestResponseUtility.prepareErrorResponseWithoutData("Invalid Activity planned start date parameter", "Invalid Activity planned start date parameter");
						 }
						 
					}
					
					if(plannedEndDateParam != null && !plannedEndDateParam.trim().isEmpty()) {
						 String planEndDate[]=plannedEndDateParam.split(" ");
						 try {
							 if(!planEndDate[0].isEmpty() && planEndDate[0].equalsIgnoreCase("between")) {
								 plannedEndDateSearch = planEndDate[0];
								 plannedEndFromDate= DateUtility.dateformatWithOutTime(planEndDate[1]);
								 plannedEndToDate= DateUtility.dateformatWithOutTime(planEndDate[2]);
								 if (ActivityDateValidation(plannedEndFromDate,plannedEndToDate)) {
										RestResponseUtility.prepareErrorResponseWithoutData("Planned End from date should be less than or equal to planned start to date","Planned End from date should be less than or equal to planned start to date");
								 }
							 } else{
								 plannedEndDateSearch = planEndDate[0];
								 plannedEndFromDate= DateUtility.dateformatWithOutTime(planEndDate[1]);
							 }
						 } catch(Exception e) {
							 return RestResponseUtility.prepareErrorResponseWithoutData("Invalid Activity planned end date parameter", "Invalid Activity planned end date parameter");
						 }
						 
					}
					
					if(actualStartDateParam != null && !actualStartDateParam.trim().isEmpty()) {
						 String actualStartDate[]=actualStartDateParam.split(" ");
						 try {
							 if(!actualStartDate[0].isEmpty() && actualStartDate[0].equalsIgnoreCase("between")) {
								 actualStartDateSearch = actualStartDate[0];
								 actualStartFromDate= DateUtility.dateformatWithOutTime(actualStartDate[1]);
								 actualStartToDate= DateUtility.dateformatWithOutTime(actualStartDate[2]);
								 if (ActivityDateValidation(actualStartFromDate,actualStartToDate)) {
										RestResponseUtility.prepareErrorResponseWithoutData("Actual start from date should be less than or equal to planned start to date","Actual start from date should be less than or equal to planned start to date");
								 }
							 } else {
								 actualStartDateSearch = actualStartDate[0];
								 actualStartFromDate= DateUtility.dateformatWithOutTime(actualStartDate[1]);
							 }
						 } catch(Exception e) {
							 return RestResponseUtility.prepareErrorResponseWithoutData("Invalid Activity actual start date parameter", "Invalid Activity actual start date parameter");
						 }
						 
					}
					
					if(actualEndDateParam != null && !actualEndDateParam.trim().isEmpty()) {
						 String actutalEndDate[]=actualEndDateParam.split(",");
						 try {
							 if(!actutalEndDate[0].isEmpty() && actutalEndDate[0].equalsIgnoreCase("between")) {
								 actualEndDateSearch = actutalEndDate[0];
								 actualEndFromDate= DateUtility.dateformatWithOutTime(actutalEndDate[1]);
								 actualEndToDate= DateUtility.dateformatWithOutTime(actutalEndDate[2]);
								 if (ActivityDateValidation(actualEndFromDate,actualEndToDate)) {
										RestResponseUtility.prepareErrorResponseWithoutData("Actual End from date should be less than or equal to planned start to date","Actual End from date should be less than or equal to planned start to date");
								 }
							 } else {
								 actualEndDateSearch = actutalEndDate[0];
								 actualEndFromDate= DateUtility.dateformatWithOutTime(actutalEndDate[1]);
							 }
						 } catch(Exception e) {
							 return RestResponseUtility.prepareErrorResponseWithoutData("Invalid Activity actual end date parameter", "Invalid Activity actual end date parameter");
						 }
						 
					}
					
					List<JsonActivity> jsonActivities = activityService.listActivitiesBySerachCriteria(product.getTestFactory().getTestFactoryId(), product.getProductId(), productVersionId, productBuildId, userList, activityWorkPackageIdList, workflowStatusList, workflowStatusTypeList, activityTypeList, activityAssigneeList, activityReviewerList, 
							plannedStartFromDate,plannedStartToDate,plannedStartDateSearch,
							plannedEndFromDate,plannedEndToDate,plannedEndDateSearch, 
							actualStartFromDate,actualStartToDate,actualStartDateSearch,
							actualEndFromDate,actualEndToDate,actualEndDateSearch);
					
							
					List<HashMap<String, Object>> customFieldValueList=customFieldService.getAllCustomFieldsExistInstance(entityId, parentEntityInstanceId, product.getTestFactory().getTestFactoryId(), product.getProductId(), active, new Date());
					if(jsonActivities != null){
						for(JsonActivity jsonActivity:jsonActivities) {
							JSONObject responseJson = new JSONObject();
							responseJson=jsonActivity.getCleanJson();
							
							
							if(customFieldValueList != null && customFieldValueList.size() >0) {
								for(Map<String,Object> customField:customFieldValueList) {
									if(jsonActivity.getActivityId().equals(Integer.parseInt(customField.get("Id").toString()))) {
										responseJson.put("customField", customField);
									}
								}
							}
							arr.put(responseJson);	
						}
					}
				
			} else {
				return RestResponseUtility.prepareErrorResponseWithoutData("ProductName is required", "ProductName is required");
			}
		return RestResponseUtility.prepareSuccessResponseWithJSONArray("Listing Activity details", arr);
	
		}catch(Exception e) {
			return RestResponseUtility.prepareErrorResponseWithoutData("Error in Listing Activity details"+e,"Error in Listing Activity details"+e);
		}
	 }
	
	
	@POST
	@Path("/activityManagement/query/updateWorkflowStatus")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response updateActivityWorkflowStatus(String workflowStatusFormData) throws JSONException {
	SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
	try {
		
		JSONParser parser = new JSONParser();
		org.json.simple.JSONObject jsonFormatObject = (org.json.simple.JSONObject) parser.parse(workflowStatusFormData);
		if(jsonFormatObject == null) {
			log.info("Could not parse request : /activityManagement/query/updateWorkflowStatus");
			return RestResponseUtility.prepareErrorResponseWithoutData("No inputs provided", "No inputs provided");
		}
		String user=(String)jsonFormatObject.get("user") != null ?jsonFormatObject.get("user").toString():"";
		Response authResponse = authenticateUser(user, "/activityManagement/query/updateWorkflowStatus");
		if (authResponse != null)
			return authResponse;
		String activityId=(String)jsonFormatObject.get("activityId") != null ?jsonFormatObject.get("activityId").toString():"";
		String activityName=(String)jsonFormatObject.get("activityName") != null ?jsonFormatObject.get("activityName").toString():"";
		String activityWorkPackageId=(String)jsonFormatObject.get("activityWorkPackageId") != null ?jsonFormatObject.get("activityWorkPackageId").toString():"";
		String activityWorkPackageName=(String)jsonFormatObject.get("activityWorkPackageName") != null ?jsonFormatObject.get("activityWorkPackageName").toString():"";
		
		String productName=(String)jsonFormatObject.get("productName") != null ?jsonFormatObject.get("productName").toString():"";
		String fromStage=(String)jsonFormatObject.get("fromStage") != null ?jsonFormatObject.get("fromStage").toString():"";
		String toStage=(String)jsonFormatObject.get("toStage") != null ?jsonFormatObject.get("toStage").toString():"";
		String newEffort=(String)jsonFormatObject.get("newEffort") != null ?jsonFormatObject.get("newEffort").toString():"";
		String newEffortAction=(String)jsonFormatObject.get("newEffortAction") != null ?jsonFormatObject.get("newEffortAction").toString():"";
		String comments=(String)jsonFormatObject.get("comments") !=null ?jsonFormatObject.get("comments").toString() :"";
		
		String workflowPlannedStartDate=(String)jsonFormatObject.get("plannedStartDate") != null ?jsonFormatObject.get("plannedStartDate").toString():"";
		String workflowPlannedEndDate=(String)jsonFormatObject.get("plannedEndDate") != null ?jsonFormatObject.get("plannedEndDate").toString():"";
		String changeStageOwner=(String)jsonFormatObject.get("changeStageOwner") != null ?jsonFormatObject.get("changeStageOwner").toString():"";
		String stageOwnerChangeType=(String)jsonFormatObject.get("stageOwnerChangeType") != null ?jsonFormatObject.get("stageOwnerChangeType").toString():"";
		
		Integer effort=0;
		Integer actualSize=0;
		if((activityId ==null ||activityId.trim().isEmpty()) && (activityName == null && activityName.trim().isEmpty())) {
			return RestResponseUtility.prepareErrorResponseWithoutData("activityId or activityName is required", "activityId or activityName is required");
		}
		Integer entityInstanceId=0;
		Activity activity=null;
		if(activityId != null && !activityId.isEmpty()) {
			entityInstanceId=Integer.parseInt(activityId);
		}
		if(entityInstanceId != null && entityInstanceId >0) {
			activity=activityService.getActivityById(entityInstanceId, 1);
			if(activity == null) {
				return RestResponseUtility.prepareErrorResponseWithoutData("Invalid activityId", "Invalid activityId");
			}
		} else if(activityName !="" && activityName.trim().isEmpty()) {
			if((activityWorkPackageId ==null ||activityWorkPackageId.trim().isEmpty()) && (activityWorkPackageName == null && activityWorkPackageName.trim().isEmpty())) {
				return RestResponseUtility.prepareErrorResponseWithoutData("activityWorkPackageId or activityWorkPackageName is required", "activityWorkPackageId or activityWorkPackageName is required");
			}
			if(activityWorkPackageId != null && !activityWorkPackageId.trim().isEmpty()) {
				activity = activityService.getActivityByWorkpackageIdAndActivityName(Integer.parseInt(activityWorkPackageId), activityName);
				
			} else if(activityWorkPackageName != null && !activityWorkPackageName.trim().isEmpty()) {
				List<ActivityWorkPackage> activityWorkpackages= activityWorkPackageService.getActivityWorkPackagesByName(activityWorkPackageName);
				if(activityWorkpackages != null && activityWorkpackages.size() >0) {
					activity = activityService.getActivityByWorkpackageIdAndActivityName(activityWorkpackages.get(0).getActivityWorkPackageId(), activityName);
				} else {
					return RestResponseUtility.prepareErrorResponseWithoutData("Invalid activityWorkpackageName", "Invalid activityWorkpackageName");
				}
			}
		}
		
		if(productName ==null ||productName.trim().isEmpty()) {
			return RestResponseUtility.prepareErrorResponseWithoutData("ProductName is required", "ProductName is required");
		}
		
		
		
		if(fromStage ==null ||fromStage.trim().isEmpty()) {
			return RestResponseUtility.prepareErrorResponseWithoutData("fromStage is required", "fromStage is required");
		}
		
		if(toStage ==null ||toStage.trim().isEmpty()) {
			return RestResponseUtility.prepareErrorResponseWithoutData("toStage is required", "toStage is required");
		}
		
		ProductMaster product=productMasterDAO.getProductByName(productName);
		if(product == null) {
			return RestResponseUtility.prepareErrorResponseWithoutData("Invalid  ProductName", "Invalid  ProductName");
		}
		authResponse = checkUserAuthorization(product.getProductId(), user, "/activityManagement/query/updateWorkflowStatus");
		if (authResponse != null)
			return authResponse;
		
		Integer entityTypeId=33;
		WorkflowStatus sourceWorkflowStatus=workflowStatusService.getWorkflowStatusForInstanceByEntityType(product.getProductId(), entityTypeId, activity.getActivityMaster().getActivityMasterId(), fromStage);
		WorkflowStatus targetWorkflowStatus=workflowStatusService.getWorkflowStatusForInstanceByEntityType(product.getProductId(), entityTypeId,  activity.getActivityMaster().getActivityMasterId(), toStage);
		if(sourceWorkflowStatus == null) {
			return RestResponseUtility.prepareErrorResponseWithoutData("Invalid  fromStage WorkflowStatus", "Invalid  fromStage WorkflowStatus");
		}
		
		if(targetWorkflowStatus == null) {
			return RestResponseUtility.prepareErrorResponseWithoutData("Invalid  toStage WorkflowStatus", "Invalid  toStage WorkflowStatus");
		}
		
		
		Date plannedStartDate =null;
		Date plannedEndDate = null;
		
		if(workflowPlannedStartDate != null && !workflowPlannedStartDate.trim().isEmpty()) {
			 try {
				 plannedStartDate= DateUtility.dateformatWithOutTime(workflowPlannedStartDate);
			 } catch(Exception e) {
				 return RestResponseUtility.prepareErrorResponseWithoutData("Invalid start date parameter", "Invalid start date parameter");
			 }
			 
		}
		if(workflowPlannedEndDate != null && !workflowPlannedEndDate.trim().isEmpty()) {
			 try {
				 plannedEndDate= DateUtility.dateformatWithOutTime(workflowPlannedEndDate);
			 } catch(Exception e) {
				 return RestResponseUtility.prepareErrorResponseWithoutData("Invalid start date parameter", "Invalid start date parameter");
			 }
			 
		}
		
		if(plannedStartDate != null && plannedEndDate != null) {
			if (ActivityDateValidation(plannedStartDate, plannedEndDate)) {
				 return RestResponseUtility.prepareErrorResponseWithoutData("Planned start date should be less than or equal to planned end date","Planned start date should be less than or equal to planned end date");
			}
			
			 String errmsg="Please select Start Date & End Date between Activity Date "+activity.getPlannedStartDate().toString().replaceAll("00:00:00.0", "")+"  and "+activity.getPlannedEndDate().toString().replaceAll("00:00:00.0", "");
			 Integer dateResult = DateUtility.ActivityDateValidation(plannedStartDate, plannedEndDate,
						activity.getPlannedStartDate(), activity.getPlannedEndDate());
			if (dateResult == 1) {
				return RestResponseUtility.prepareErrorResponseWithoutData(errmsg,errmsg);
			} else if (dateResult == 2) {
				return RestResponseUtility.prepareErrorResponseWithoutData(errmsg,errmsg);
			} 
		}
		
		//Date actionDate= DateUtility.dateToStringInSecond(new Date());
		if(changeStageOwner == null || changeStageOwner.trim().isEmpty() ) {
			
			String attachmentIds="";
			 List<Integer> possibleIds=null;
			 UserList userList = userListService.getUserByLoginId(user);
			 String errorMessage = workflowEventService.updateWorkflowEvent(product.getProductId(), entityInstanceId.toString(), userList,activity.getActivityMaster().getActivityMasterId(), entityTypeId, targetWorkflowStatus.getWorkflowStatusId(), 0, effort, comments, sourceWorkflowStatus.getWorkflowStatusId(), entityInstanceId, attachmentIds, possibleIds, false, new Date(),actualSize,plannedStartDate,plannedEndDate,newEffortAction);
			 if(errorMessage!= null && !errorMessage.isEmpty()) {
				 return RestResponseUtility.prepareErrorResponseWithoutData(errorMessage, errorMessage);
			 }
		} else {
			UserList actor= userListService.getUserByLoginId(changeStageOwner);
			if(actor == null) {
				return RestResponseUtility.prepareErrorResponseWithoutData("Invalid StageOwner", "Invalid StageOwner");
			}
			WorkflowStatusActor workflowStatusActor = new WorkflowStatusActor();
			
			if(stageOwnerChangeType != null && !stageOwnerChangeType.trim().isEmpty()) {
				if(stageOwnerChangeType.equalsIgnoreCase("replace")) {
					List<WorkflowStatusActor> workflowStatusActors = workflowStatusPolicyDAO.getWorkflowStatusActor(product.getProductId(), targetWorkflowStatus.getWorkflowStatusId(), entityTypeId, activity.getActivityMaster().getActivityMasterId(), entityInstanceId, IDPAConstants.WORKFLOW_STATUS_POLICY_USER_ACTION_SCOPE, null, null);
					if(workflowStatusActors != null && workflowStatusActors.size() >0) {
						for(WorkflowStatusActor statusActor:workflowStatusActors) {
							workflowStatusPolicyDAO.deleteWorkflowStatusActor(statusActor);
						}
					}
				}
			}
			
			boolean isMappingAleadyExist = workflowStatusPolicyService.checkWorkflowStatusActorAlreadyExist(product.getProductId(), actor.getUserId(), targetWorkflowStatus.getWorkflowStatusId(), IDPAConstants.ENTITY_ACTIVITY_TYPE, activity.getActivityMaster().getActivityMasterId(),activity.getActivityId(), actor.getUserId(),null);
			if(!isMappingAleadyExist){
				if(entityInstanceId != null){
					workflowStatusActor.setMappingLevel("Instance");
				}else{
					workflowStatusActor.setMappingLevel("Entity");
				}
				workflowStatusActor.setEntityInstanceId(entityInstanceId);
				workflowStatusActor.setUser(actor);
				workflowStatusActor.setEntityId(activity.getActivityMaster().getActivityMasterId());
				workflowStatusActor.setActionRequirement("Optional");
				workflowStatusActor.setActorMappingType("User");
				workflowStatusActor.setUserActionStatus("Not Complete");
				workflowStatusActor.setWorkflowStatus(targetWorkflowStatus);
				workflowStatusActor.setProduct(product);
				EntityMaster entMas = workPackageService.getEntityMasterById(IDPAConstants.ENTITY_ACTIVITY_TYPE);		
				workflowStatusActor.setEntityType(entMas);
				workflowStatusPolicyService.addWorkflowStatusActor(workflowStatusActor);
				log.info("workflow.status.policy.user.add");
				return RestResponseUtility.prepareSuccessResponse("Updated Workflow stage actor","Updated Workflow stage actor");
			}else{
				log.info("Actor already associated with the policy");
				return RestResponseUtility.prepareErrorResponseWithoutData("Actor already associated with the policy","Actor already associated with the policy");
			}
			
		}
		return RestResponseUtility.prepareSuccessResponse("Updated Workflow status from  "+fromStage+ " to "+toStage, "Updated Workflow status from  "+fromStage+ " to "+toStage);
		}catch(Exception e) {
			return RestResponseUtility.prepareErrorResponseWithoutData("Error in update activity wokflow status"+e,"Error in update activity wokflow status"+e);
		}
	 }
	
	
	
	@POST
	@Path("/activityManagement/query/updateActivity")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response updateActivity(String activityFormData) throws JSONException {
	SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
	JSONObject responseJson = new JSONObject();
	JSONArray arr = new JSONArray();
	try {
		
		JSONParser parser = new JSONParser();
		org.json.simple.JSONObject jsonFormatObject = (org.json.simple.JSONObject) parser.parse(activityFormData);
		if(jsonFormatObject == null) {
			log.info("Could not parse request : /activityManagement/query/updateActivity");
			return RestResponseUtility.prepareErrorResponseWithoutData("No inputs provided", "No inputs provided");
		}
		
		String user=(String)jsonFormatObject.get("user") !=null ?jsonFormatObject.get("user").toString():"";
		String formActivityId=(String)jsonFormatObject.get("activityId") != null ?jsonFormatObject.get("activityId").toString():"";
		String activityName=(String)jsonFormatObject.get("activityName") != null ?jsonFormatObject.get("activityName").toString():"";
		String activityMasterName=(String)jsonFormatObject.get("activityMasterName") != null ?jsonFormatObject.get("activityMasterName").toString():"";
		String formAssignee=(String)jsonFormatObject.get("assignee") != null ?jsonFormatObject.get("assignee").toString():"";
		String formReviewer=(String)jsonFormatObject.get("reviewer") != null ?jsonFormatObject.get("reviewer").toString():"";
		String priorityName=(String)jsonFormatObject.get("priorityName") != null ?jsonFormatObject.get("priorityName").toString():"";
		String formPlannedStartDate=(String)jsonFormatObject.get("plannedStartDate") != null ?jsonFormatObject.get("plannedStartDate").toString():"";
		String formPlannedEndDate=(String)jsonFormatObject.get("plannedEndDate") != null ?jsonFormatObject.get("plannedEndDate").toString():"";
		
		
		if(activityMasterName ==null ||activityMasterName.trim().isEmpty()) {
			responseJson.put("result", "ERROR");
			responseJson.put("status", "400");	
			responseJson.put("message", "EntityId is required");
			responseJson.put("Failure_Details", "EntityId is required");
			return Response.ok(responseJson.toString()).build();
		}
		if(formActivityId ==null ||formActivityId.trim().isEmpty()) {
			responseJson.put("result", "ERROR");
			responseJson.put("status", "400");	
			responseJson.put("data", "");
			responseJson.put("message", "ActivityId is required");
			responseJson.put("Failure_Details", "ActivityId is required");
			return Response.ok(responseJson.toString()).build();
		}
		
		
		if(activityName ==null ||activityName.trim().isEmpty()) {
			responseJson.put("result", "ERROR");
			responseJson.put("status", "400");	
			responseJson.put("message", "Activity name is required");
			responseJson.put("Failure_Details", "Activity name is required");
			return Response.ok(responseJson.toString()).build();
		}
		
		if(formAssignee ==null ||formAssignee.trim().isEmpty()) {
			responseJson.put("result", "ERROR");
			responseJson.put("status", "400");	
			responseJson.put("message", "assignee is required");
			responseJson.put("Failure_Details", "assignee is required");
			return Response.ok(responseJson.toString()).build();
		}
		
		if(formReviewer ==null ||formReviewer.trim().isEmpty()) {
			responseJson.put("result", "ERROR");
			responseJson.put("status", "400");	
			responseJson.put("message", "reviewer is required");
			responseJson.put("Failure_Details", "reviewer is required");
			return Response.ok(responseJson.toString()).build();
		}
		
		if(formPlannedStartDate ==null ||formPlannedStartDate.trim().isEmpty()) {
			responseJson.put("result", "ERROR");
			responseJson.put("status", "400");	
			responseJson.put("message", "plannedStartDate is required");
			responseJson.put("Failure_Details", "plannedStartDate is required");
			return Response.ok(responseJson.toString()).build();
		}
		
		if(formPlannedEndDate ==null ||formPlannedEndDate.trim().isEmpty()) {
			responseJson.put("result", "ERROR");
			responseJson.put("status", "400");	
			responseJson.put("message", "plannedStartDate is required");
			responseJson.put("Failure_Details", "plannedStartDate is required");
			return Response.ok(responseJson.toString()).build();
		}
		
		
		Integer activityId=Integer.parseInt(formActivityId);
		Activity activity=activityService.getActivityById(activityId,1);
		if(activity == null) {
			responseJson.put("result", "ERROR");
			responseJson.put("status", "400");	
			responseJson.put("message", "ActivityId is invalid");
			responseJson.put("Failure_Details", "ActivityId is invalid");
			return Response.ok(responseJson.toString()).build();
		}
		String reviewerDB=activity.getReviewer().getLoginId();
		String assigneeDB=activity.getAssignee().getLoginId();
		Date plannedStartDate= DateUtility.dateformatWithOutTime(formPlannedStartDate);
		Date plannedEndDate= DateUtility.dateformatWithOutTime(formPlannedEndDate);
		ProductMaster product=activity.getActivityWorkPackage().getProductBuild().getProductVersion().getProductMaster();
		ActivityMaster activityMaster = activityTypeService.getActivityMasterByNameAndProductId(activityMasterName, product.getProductId());
		
		UserList assignee=userListService.getUserListByUserName(formAssignee);
		UserList reviewer=userListService.getUserListByUserName(formReviewer);
		if(user ==null ||user.trim().isEmpty()) {
			responseJson.put("result", "ERROR");
			responseJson.put("status", "400");	
			responseJson.put("message", "User is required");
			responseJson.put("Failure_Details", "User is required");
			return Response.ok(responseJson.toString()).build();
		}
		if(user != null && !user.trim().isEmpty()) {
			
			UserList userList=userListService.getUserByLoginId(user);
			
			if(userList == null) {
				responseJson.put("result", "ERROR");
				responseJson.put("status", "400");	
				responseJson.put("message", "Invalid User");
				responseJson.put("Failure_Details", "Invalid User");
				return Response.ok(responseJson.toString()).build();
			}
						
			if(!productTeamResourcesDao.isExistsTeamResourceByUserIdandProductIdandUserId(product.getProductId(), userList.getUserId())) {
				responseJson.put("result", "ERROR");
				responseJson.put("status", "400");	
				responseJson.put("message", "User not mapped for the given product");
				responseJson.put("Failure_Details", "User not mapped for the given product");
				return Response.ok(responseJson.toString()).build();
			}
			
			if(activityMaster !=null) {
				if(!activityMaster.getActivityMasterId().equals(activity.getActivityMaster().getActivityMasterId())) {
					activity.setActivityMaster(activityMaster);
				}
			}
			
			ActivityWorkPackage activityWorkPackage =activity.getActivityWorkPackage();
			activity.setActivityWorkPackage(activityWorkPackage);
				Date wpStDate = activityWorkPackage.getPlannedStartDate();
				Date wpEndDate = activityWorkPackage.getPlannedEndDate();
				Date actStDate = activity.getPlannedStartDate();
				Date actEndDate = activity.getPlannedEndDate();
				log.info("process.activity.add wpStDate " + wpStDate);
				log.info("process.activity.add wpEndDate " + wpEndDate);
	         
	         String errmsg="Please select Start Date & End Date between WorkPackage Date "+wpStDate.toString().replaceAll("00:00:00.0", "")+"  and "+wpEndDate.toString().replaceAll("00:00:00.0", "");
				
				if(actStDate!=null ||actEndDate!=null){
					Integer dateResult = DateUtility.ActivityDateValidation(actStDate, actEndDate,wpStDate, wpEndDate);
					if (dateResult == 1) {
						responseJson.put("result", "ERROR");
						responseJson.put("status", "400");	
						responseJson.put("message", errmsg);
						responseJson.put("Failure_Details", errmsg);
						return Response.ok(responseJson.toString()).build();
					} else if (dateResult == 2) {
						responseJson.put("result", "ERROR");
						responseJson.put("status", "400");	
						responseJson.put("message", errmsg);
						responseJson.put("Failure_Details", errmsg);
					} 
				}
				
				activity.setAssignee(assignee);
				activity.setReviewer(reviewer);
				activity.setActivityName(activityName);
				activity.setPlannedStartDate(plannedStartDate);
				activity.setPlannedEndDate(plannedEndDate);
				activityService.updateActivity(activity);
				
				if(!formReviewer.equalsIgnoreCase(reviewerDB) ){ 
					configurationWorkFlowService.changeInstnaceActorMapping(product.getProductId(),  IDPAConstants.ENTITY_ACTIVITY_TYPE, activity.getActivityMaster().getActivityMasterId(),  activity.getActivityId(), activity.getWorkflowStatus().getWorkflow().getWorkflowId(), reviewer);
				} else if(!formAssignee.equalsIgnoreCase(assigneeDB)) {
					configurationWorkFlowService.changeInstnaceActorMapping(product.getProductId(),  IDPAConstants.ENTITY_ACTIVITY_TYPE, activity.getActivityMaster().getActivityMasterId(),  activity.getActivityId(), activity.getWorkflowStatus().getWorkflow().getWorkflowId(), assignee);
				}
				
				JsonActivity jsonActivity= new JsonActivity(activity);
				arr.put(jsonActivity.getCleanJson());
			
		}
		
		responseJson.put("result", "OK");
		responseJson.put("status", "200");	
		responseJson.put("data", arr);
		responseJson.put("message", "Updated activity successfully!");
		return Response.ok(responseJson.toString()).build();
	
		}catch(Exception e) {
			responseJson.put("result", "ERROR");
			responseJson.put("status", "400");	
			responseJson.put("data", "");
			responseJson.put("message", "Error in update activity");
			responseJson.put("Failure_Details", "Error in update activity");
			return Response.ok(responseJson.toString()).build();
		}
	 }
	
	
	@POST
	@Path("/activityManagement/query/getUserBasedActivites")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response getUserBasedActivites(String activityFormData) throws JSONException {
	SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
	JSONArray arr = new JSONArray();
	try {
		
		JSONParser parser = new JSONParser();
		org.json.simple.JSONObject jsonFormatObject = (org.json.simple.JSONObject) parser.parse(activityFormData);
		if(jsonFormatObject == null) {
			log.info("Could not parse request : /map user to Product");
			return RestResponseUtility.prepareErrorResponseWithoutData("No inputs provided", "No inputs provided");
		}
		
		
		String user=(String)jsonFormatObject.get("user") != null ?jsonFormatObject.get("user").toString():"";
		String productName=(String)jsonFormatObject.get("productName") != null?jsonFormatObject.get("productName").toString():"";
		String productVersionName=(String)jsonFormatObject.get("productVersionName") != null ?jsonFormatObject.get("productVersionName").toString():"";
		String productBuildName=(String)jsonFormatObject.get("productBuildName") !=null ?jsonFormatObject.get("productBuildName").toString():"";
		String activityWorkPackageName=(String)jsonFormatObject.get("activityWorkPackageName") != null ?jsonFormatObject.get("activityWorkPackageName").toString():"";
		String actionValue=(String)jsonFormatObject.get("actionValue") != null ?jsonFormatObject.get("actionValue").toString():"";
		Response authResponse = authenticateUser(user, "/activityManagement/query/getUserBasedActivites");
		if (authResponse != null)
			return authResponse;
		Integer isActive=1;
		Integer productId=0;
		Integer productVersionId=0;
		Integer productBuildId=0;
		Integer activityWorkPackageId=0;
		String statusType="All";
		Integer jtStartIndex=0;
		Integer jtPageSize=1000;
		Map<String, String> searchStrings=null;
		ProductMaster product= null;
		List<Activity> activities= null;
		List<JsonActivity> jsonActivities =null;
		
		
		UserList userList=userListService.getUserByLoginId(user);
		request.getSession().setAttribute("USER", userList);
			if(productName != null && !productName.trim().isEmpty()) {
				product=productMasterDAO.getProductByName(productName);
				if(product == null) {
					return RestResponseUtility.prepareErrorResponseWithoutData("Invalid  ProductName", "Invalid  ProductName");
				}
				//Check authorization for user
				authResponse = checkUserAuthorization(product.getProductId(), user, "/activityManagement/query/getUserBasedActivites");
				if (authResponse != null)
					return authResponse;
				
				if(productVersionName != null && !productVersionName.isEmpty()) {
					ProductVersionListMaster productVersion=productListService.getProductVersionListByProductIdAndVersionName(product.getProductId(), productVersionName);
					if(productVersion != null) {
						productVersionId=productVersion.getProductVersionListId();
					} else {
						return RestResponseUtility.prepareErrorResponseWithoutData("Invalid  Version Name", "Invalid  Version Name");
					}
				}
				if(productVersionId >0) {
					ProductBuild productBuild=productListService.getproductBuildByProductIdAndBuildName(productVersionId, productBuildName);
					if(productBuild != null) {
						productBuildId=productBuild.getProductBuildId();
					}
				}
					if(productBuildId >0) {
						ActivityWorkPackage activityWorkPackage=activityWorkPackageService.getActivityWorkPackageByName(activityWorkPackageName, productBuildId);
						if(activityWorkPackage != null) {
							activityWorkPackageId=activityWorkPackage.getActivityWorkPackageId();
						}
						
					}
					if(actionValue == "" || actionValue.isEmpty()) {
						jsonActivities = activityService.listActivities(searchStrings,product.getTestFactory().getTestFactoryId(),product.getProductId(), productVersionId, productBuildId,activityWorkPackageId, 0,isActive,request,statusType,jtStartIndex,jtPageSize);
					}
					if(actionValue != null && !actionValue.trim().isEmpty()) {
						if(actionValue.equals("AssignedActivities")){
							activities = activityService.listMyActivitiesByLoginIdAndProductId(userList.getUserId(),product.getTestFactory().getTestFactoryId(), productId,activityWorkPackageId);
						}else if(actionValue.equals("PendingWithMe")){
							activities= workflowActivityDAO.listActivitiesforWorkFlowForUserOrRole(product.getTestFactory().getTestFactoryId(),productId,activityWorkPackageId, null, IDPAConstants.ENTITY_ACTIVITY_TYPE, null, userList.getUserId(), null, jtStartIndex, jtPageSize);
						} else if(actionValue.equals("PassingThroughMe")){
							activities=workflowActivityDAO.listActivitiesforWorkFlowStatusPartForUserOrRole(product.getTestFactory().getTestFactoryId(),productId,activityWorkPackageId, IDPAConstants.ENTITY_ACTIVITY_TYPE, userList.getUserId(),null);
						}	
						jsonActivities = activityService.listMyActivities(request,activities,product.getTestFactory().getTestFactoryId(),productId,jtStartIndex,jtPageSize);
					}
					if(jsonActivities != null && jsonActivities.size() >0){
						for(JsonActivity jsonActivity:jsonActivities) {
							arr.put(jsonActivity.getCleanJson());
						}
					}
				
			} else {
				return RestResponseUtility.prepareErrorResponseWithoutData("ProductName is required", "ProductName is required");
			}
		return RestResponseUtility.prepareSuccessResponseWithJSONArray("Listing user based Activity details ", arr);
	
		}catch(Exception e) {
			return RestResponseUtility.prepareErrorResponseWithoutData("Error in Listing Activity details"+e, "Error in Listing Activity details"+e);
		}
	 }
	

private Response checkUserAuthorization(Integer productId, String userName, String serviceName) {
		
		boolean isProductAuthorizedForUser=false;
		UserList user = userListService.getUserByLoginId(userName);
		try {
			if (atlasUserAuthenticationRequired != null && atlasUserAuthenticationRequired.equalsIgnoreCase("yes")) {
			
				isProductAuthorizedForUser = productListService.isUserPermissionByProductIdandUserId(productId, user.getUserId(), user.getUserRoleMaster().getUserRoleId());
				if (!isProductAuthorizedForUser) {
					
					log.info("REST service : " + serviceName + " : User : " + user.getLoginId() + " is not authorized for product Id : " + productId);
					return RestResponseUtility.prepareErrorResponseWithoutData("User : " + user.getLoginId() + " is not authorized for product Id : " + productId, "User : " + user.getLoginId() + " is not authorized for product Id : " + productId);
				}
			}
		}catch(Exception e) {
			log.error("Problem while authorizing user : " + user.getLoginId(), e);
			try {
				return RestResponseUtility.prepareErrorResponseWithoutData("REST service : " + serviceName + "Problem while authorizing user : " + user.getLoginId(), "REST service : " + serviceName + "Problem while authorizing user : " + user.getLoginId());
			} catch (Exception r) {
				log.error("Problem while authorizing user : " + user.getLoginId(), e);
			}
		}
		return null;
	}
	
	private Response authenticateUser(String userName, String serviceName) {
		try {
			UserList userList = null;
			if (atlasUserAuthenticationRequired != null && atlasUserAuthenticationRequired.equalsIgnoreCase("yes")) {
				if(userName == null || userName.trim().isEmpty()) {
					log.info("REST service : " + serviceName + " : Registered user name is missing. It is mandatory");
					return RestResponseUtility.prepareErrorResponseWithoutData("User is required", "Registered user name is missing. It is mandatory");
				} else {
					userList = userListService.getUserByLoginId(userName);
					if (userList == null) {
						log.info("REST service : " + serviceName + " : Username is not a registered user : " + userName);
						return RestResponseUtility.prepareErrorResponseWithoutData( "User is invalid", "User name specified : "  + userName + "  is invalid");
					}
				}
				log.info("REST service : " + serviceName + " : Username is a valid user : " + userName);
				return null;
			}
		} catch (Exception e) {
			log.error("Problem while validating user : " + userName, e);
			try {
				return RestResponseUtility.prepareErrorResponseWithoutData("REST service : " + serviceName + "Problem while validating user : " + userName, "REST service : " + serviceName + "Problem while validating user : " + userName);
			} catch (Exception r) {
				log.error("Problem while validating user : " + userName, e);
			}
		}
		return null;
	}
	
	/*@POST
	@Path("/activityManagement/query/updateAssigneedWorkflowStatus")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response updateAssignedUserWorkflowStatus(String workflowStatusFormData) throws JSONException {
	SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
	try {
		
		JSONParser parser = new JSONParser();
		org.json.simple.JSONObject jsonFormatObject = (org.json.simple.JSONObject) parser.parse(workflowStatusFormData);
		if(jsonFormatObject == null) {
			log.info("Could not parse request : /activityManagement/query/updateAssigneedWorkflowStatus");
			return RestResponseUtility.prepareErrorResponseWithoutData("No inputs provided", "No inputs provided");
		}
		String user=(String)jsonFormatObject.get("user") != null ?jsonFormatObject.get("user").toString():"";
		Response authResponse = authenticateUser(user, "/activityManagement/query/updateAssigneedWorkflowStatus");
		if (authResponse != null)
			return authResponse;
		String productName=(String)jsonFormatObject.get("productName") != null ?jsonFormatObject.get("productName").toString():"";
		String entity=(String)jsonFormatObject.get("activityType") != null ?jsonFormatObject.get("activityType").toString():"";
		String workflowStatus=(String)jsonFormatObject.get("workflowStatus") != null ?jsonFormatObject.get("workflowStatus").toString():"";
		String activityId=(String)jsonFormatObject.get("activityId") != null ?jsonFormatObject.get("activityId").toString():"";
		String sourceUser=(String)jsonFormatObject.get("sourceUser") != null ?jsonFormatObject.get("sourceUser").toString():"";
		String destinationUser=(String)jsonFormatObject.get("destinationUser") != null ?jsonFormatObject.get("destinationUser").toString():"";
		
		
		if(productName ==null ||productName.trim().isEmpty()) {
			return RestResponseUtility.prepareErrorResponseWithoutData("ProductName is required", "ProductName is required");
		}
		
		if(entity ==null ||entity.trim().isEmpty()) {
			return RestResponseUtility.prepareErrorResponseWithoutData("activityType is required", "activityType is required");
		}
		
		if(workflowStatus ==null ||workflowStatus.trim().isEmpty()) {
			return RestResponseUtility.prepareErrorResponseWithoutData("targetStatus is required", "targetStatus is required");
		}
		
		if(activityId ==null ||activityId.trim().isEmpty()) {
			return RestResponseUtility.prepareErrorResponseWithoutData("activityId is required", "activityId is required");
		}
		ProductMaster product=productMasterDAO.getProductByName(productName);
		if(product == null) {
			return RestResponseUtility.prepareErrorResponseWithoutData("Invalid  ProductName", "Invalid  ProductName");
		}
		authResponse = checkUserAuthorization(product.getProductId(), user, "/activityManagement/query/updateWorkflowStatus");
		if (authResponse != null)
			return authResponse;
		ActivityMaster activityType=activityTypeService.getActivityMasterByNameInProductAndTestFactory(entity, product.getProductId(), product.getTestFactory().getTestFactoryId());
		if(activityType == null) {
			return RestResponseUtility.prepareErrorResponseWithoutData("Invalid  activityType", "Invalid  activityType");
		}
		Integer entityTypeId=33;
		WorkflowStatus workflowStatusObj=workflowStatusService.getWorkflowStatusForInstanceByEntityType(product.getProductId(), entityTypeId,  activityType.getActivityMasterId(), workflowStatus);
		if(workflowStatusObj == null) {
			return RestResponseUtility.prepareErrorResponseWithoutData("Invalid  sourceWorkflowStatus", "Invalid  sourceWorkflowStatus");
		}
		
		Integer entityInstanceId=Integer.parseInt(activityId);
		return RestResponseUtility.prepareSuccessResponse("Updated Workflow status from  "+sourceStatus+ " to "+targetStatus, "Updated Workflow status from  "+sourceStatus+ " to "+targetStatus);
		}catch(Exception e) {
			return RestResponseUtility.prepareErrorResponseWithoutData("Error in update activity wokflow status"+e,"Error in update activity wokflow status"+e);
		}

	 }*/
	
	private Boolean ActivityDateValidation(Date PlannedStartDate, Date PlannedEndDate) {
		Boolean dateResult = false;
		if(PlannedStartDate != null && PlannedEndDate != null){
			dateResult = PlannedEndDate.before(PlannedStartDate);
		}
		return dateResult;

	}
	
	
	@POST
	@Path("/activityManagement/query/getActivitiesWithWorkflowEventDetail")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response getActivitiesAndWorkflowEventDetails(String activityFormData) throws JSONException {
	SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
	JSONArray arr = new JSONArray();
	try {
		
		JSONParser parser = new JSONParser();
		org.json.simple.JSONObject jsonFormatObject = (org.json.simple.JSONObject) parser.parse(activityFormData);
		if(jsonFormatObject == null) {
			log.info("Could not parse request : /activityManagement/query/getActivites");
			return RestResponseUtility.prepareErrorResponseWithoutData("No inputs provided", "No inputs provided");
		}
		String userName=jsonFormatObject.get("user") != null ? jsonFormatObject.get("user").toString():"";
		String productName=(String)jsonFormatObject.get("productName") != null?jsonFormatObject.get("productName").toString():"";
		String productVersionName=(String)jsonFormatObject.get("productVersionName") != null ?jsonFormatObject.get("productVersionName").toString():"";
		String productBuildName=(String)jsonFormatObject.get("productBuildName") !=null ?jsonFormatObject.get("productBuildName").toString():"";
		String activityWorkPackageNames=(String)jsonFormatObject.get("activityWorkPackageName") != null ?jsonFormatObject.get("activityWorkPackageName").toString():"";
		String activityWorkPackageIds=(String)jsonFormatObject.get("activityWorkPackageId") != null ?jsonFormatObject.get("activityWorkPackageId").toString():"";
		String status=(String)jsonFormatObject.get("status") != null ?jsonFormatObject.get("status").toString():"";
		String statusTypes = (String)jsonFormatObject.get("statusType") != null ?jsonFormatObject.get("statusType").toString():"";
		String activityTypes = (String)jsonFormatObject.get("activityType") != null ?jsonFormatObject.get("activityType").toString():"";
		String activityAssignees = (String)jsonFormatObject.get("activityAssignee") != null ?jsonFormatObject.get("activityAssignee").toString():"";
		String activityReviewer = (String)jsonFormatObject.get("activityReviewer") != null ?jsonFormatObject.get("activityReviewer").toString():"";
		
		String plannedStartDateParam = (String)jsonFormatObject.get("plannedStartDate") != null ?jsonFormatObject.get("plannedStartDate").toString():"";
		String plannedEndDateParam = (String)jsonFormatObject.get("plannedEndDate") != null ?jsonFormatObject.get("plannedEndDate").toString():"";
		
		String actualStartDateParam = (String)jsonFormatObject.get("actualStartDate") != null ?jsonFormatObject.get("actualStartDate").toString():"";
		String actualEndDateParam = (String)jsonFormatObject.get("actualEndDate") != null ?jsonFormatObject.get("actualEndDate").toString():"";
		
		
		
		Integer productVersionId=0;
		Integer productBuildId=0;
		ProductMaster product= null;
		Integer entityId=28;
		Integer parentEntityInstanceId=0;
		Integer active=1;
		//Authenticate the user
	
		Response authResponse = authenticateUser(userName, "/activityManagement/query/getActivites");
		if (authResponse != null)
			return authResponse;
		UserList userList = userListService.getUserByLoginId(userName);
		request.getSession().setAttribute("USER", userList);	
			if(productName != null && !productName.trim().isEmpty()) {
				product=productMasterDAO.getProductByName(productName);
				if(product == null) {
					return RestResponseUtility.prepareErrorResponseWithoutData("Invalid ProductName", "Invalid ProductName");
				}
				//Check authorization for user
				authResponse = checkUserAuthorization(product.getProductId(), userName, "/activityManagement/query/getActivites");
				if (authResponse != null)
					return authResponse;
				
				if(productVersionName != null && !productVersionName.isEmpty()) {
					ProductVersionListMaster productVersion=productListService.getProductVersionListByProductIdAndVersionName(product.getProductId(), productVersionName);
					if(productVersion != null) {
						productVersionId=productVersion.getProductVersionListId();
					} else {
						return RestResponseUtility.prepareErrorResponseWithoutData("Invalid  Version Name", "Invalid  Version Name");
					}
				}
					if(productVersionId >0) {
						ProductBuild productBuild=productListService.getproductBuildByProductIdAndBuildName(productVersionId, productBuildName);
						if(productBuild != null) {
							productBuildId=productBuild.getProductBuildId();
						}else {
							return RestResponseUtility.prepareErrorResponseWithoutData("Invalid  Build Name", "Invalid  Build Name");
						}
					}
					List<Integer> activityWorkPackageIdList=new ArrayList<>();
						if(productBuildId >0) {
							if(activityWorkPackageIds != null && !activityWorkPackageIds.trim().isEmpty()) {
								for(String workpackageId : activityWorkPackageIds.split(",")){
									ActivityWorkPackage actityWp=activityWorkPackageService.getActivityWorkPackageById(Integer.parseInt(workpackageId), productBuildId);
									if(actityWp != null) {
										activityWorkPackageIdList.add(actityWp.getActivityWorkPackageId());
									}
								}
							} else if(activityWorkPackageNames != null && !activityWorkPackageNames.trim().isEmpty()){
								
								for(String workpackageName : activityWorkPackageNames.split(",")){
									ActivityWorkPackage actityWp=activityWorkPackageService.getActivityWorkPackageByName(workpackageName, productBuildId);
									if(actityWp != null) {
										activityWorkPackageIdList.add(actityWp.getActivityWorkPackageId());
									}
								}	
							}
							
							if((activityWorkPackageIds != null && !activityWorkPackageIds.isEmpty()) || (activityWorkPackageNames != null && !activityWorkPackageNames.trim().isEmpty())){
								if(activityWorkPackageIdList == null || activityWorkPackageIdList.size() ==0) {
									return RestResponseUtility.prepareErrorResponseWithoutData("Invalid Activity Workpackage Name or WorkpackageId ", "Invalid  Activity Workpackage Name or WorkpackageId");
								}
							}
						 }
					List<String> workflowStatusList= new ArrayList<>();
					if(status != null && !status.trim().isEmpty()) {
						for(String statusName : status.split(",")){
							workflowStatusList.add(statusName);
						}
					}
					List<String> workflowStatusTypeList = new ArrayList<>();
					if(statusTypes != null && !statusTypes.trim().isEmpty()) {
						for(String workflowStatusType : statusTypes.split(",")){
							workflowStatusTypeList.add(workflowStatusType);
						}
					}
					List<String> activityTypeList= new ArrayList<>();
					if(activityTypes != null && !activityTypes.trim().isEmpty()) {
						for(String activityType : activityTypes.split(",")){
							activityTypeList.add(activityType);
						}
					}
					
					List<String> activityAssigneeList= new ArrayList<>();
					if(activityAssignees != null && !activityAssignees.trim().isEmpty()) {
						for(String assignee : activityAssignees.split(",")){
							activityAssigneeList.add(assignee);
						}
					}
					
					List<String> activityReviewerList= new ArrayList<>();
					if(activityReviewer != null && !activityReviewer.trim().isEmpty()) {
						for(String reviewer : activityReviewer.split(",")){
							activityReviewerList.add(reviewer);
						}
					}
					
					
					Date plannedStartFromDate =null;
					Date plannedStartToDate =null;
					Date plannedEndFromDate = null;
					Date plannedEndToDate = null;
					Date actualStartFromDate = null;
					Date actualStartToDate = null;
					Date actualEndFromDate = null;
					Date actualEndToDate = null;
					
					String plannedStartDateSearch="";
					String plannedEndDateSearch="";
					String actualStartDateSearch="";
					String actualEndDateSearch="";
					
					if(plannedStartDateParam != null && !plannedStartDateParam.trim().isEmpty()) {
						 String planStartDate[]=plannedStartDateParam.split(" ");
						 try {
							 if(!planStartDate[0].isEmpty() && planStartDate[0].equalsIgnoreCase("between")) {
								 plannedStartDateSearch = planStartDate[0];
								 plannedStartFromDate= DateUtility.dateformatWithOutTime(planStartDate[1]);
								 plannedStartToDate= DateUtility.dateformatWithOutTime(planStartDate[2]);
								 if (ActivityDateValidation(plannedStartFromDate,plannedStartToDate)) {
										RestResponseUtility.prepareErrorResponseWithoutData("Planned start from date should be less than or equal to planned start to date","Planned start from date should be less than or equal to planned start to date");
								 }
							 } else {
								 plannedStartDateSearch = planStartDate[0];
								 plannedStartFromDate= DateUtility.dateformatWithOutTime(planStartDate[1]);
							 }
						 } catch(Exception e) {
							 return RestResponseUtility.prepareErrorResponseWithoutData("Invalid Activity planned start date parameter", "Invalid Activity planned start date parameter");
						 }
						 
					}
					
					if(plannedEndDateParam != null && !plannedEndDateParam.trim().isEmpty()) {
						 String planEndDate[]=plannedEndDateParam.split(" ");
						 try {
							 if(!planEndDate[0].isEmpty() && planEndDate[0].equalsIgnoreCase("between")) {
								 plannedEndDateSearch = planEndDate[0];
								 plannedEndFromDate= DateUtility.dateformatWithOutTime(planEndDate[1]);
								 plannedEndToDate= DateUtility.dateformatWithOutTime(planEndDate[2]);
								 if (ActivityDateValidation(plannedEndFromDate,plannedEndToDate)) {
										RestResponseUtility.prepareErrorResponseWithoutData("Planned End from date should be less than or equal to planned start to date","Planned End from date should be less than or equal to planned start to date");
								 }
							 } else{
								 plannedEndDateSearch = planEndDate[0];
								 plannedEndFromDate= DateUtility.dateformatWithOutTime(planEndDate[1]);
							 }
						 } catch(Exception e) {
							 return RestResponseUtility.prepareErrorResponseWithoutData("Invalid Activity planned end date parameter", "Invalid Activity planned end date parameter");
						 }
						 
					}
					
					if(actualStartDateParam != null && !actualStartDateParam.trim().isEmpty()) {
						 String actualStartDate[]=actualStartDateParam.split(" ");
						 try {
							 if(!actualStartDate[0].isEmpty() && actualStartDate[0].equalsIgnoreCase("between")) {
								 actualStartDateSearch = actualStartDate[0];
								 actualStartFromDate= DateUtility.dateformatWithOutTime(actualStartDate[1]);
								 actualStartToDate= DateUtility.dateformatWithOutTime(actualStartDate[2]);
								 if (ActivityDateValidation(actualStartFromDate,actualStartToDate)) {
										RestResponseUtility.prepareErrorResponseWithoutData("Actual start from date should be less than or equal to planned start to date","Actual start from date should be less than or equal to planned start to date");
								 }
							 } else {
								 actualStartDateSearch = actualStartDate[0];
								 actualStartFromDate= DateUtility.dateformatWithOutTime(actualStartDate[1]);
							 }
						 } catch(Exception e) {
							 return RestResponseUtility.prepareErrorResponseWithoutData("Invalid Activity actual start date parameter", "Invalid Activity actual start date parameter");
						 }
						 
					}
					
					if(actualEndDateParam != null && !actualEndDateParam.trim().isEmpty()) {
						 String actutalEndDate[]=actualEndDateParam.split(",");
						 try {
							 if(!actutalEndDate[0].isEmpty() && actutalEndDate[0].equalsIgnoreCase("between")) {
								 actualEndDateSearch = actutalEndDate[0];
								 actualEndFromDate= DateUtility.dateformatWithOutTime(actutalEndDate[1]);
								 actualEndToDate= DateUtility.dateformatWithOutTime(actutalEndDate[2]);
								 if (ActivityDateValidation(actualEndFromDate,actualEndToDate)) {
										RestResponseUtility.prepareErrorResponseWithoutData("Actual End from date should be less than or equal to planned start to date","Actual End from date should be less than or equal to planned start to date");
								 }
							 } else {
								 actualEndDateSearch = actutalEndDate[0];
								 actualEndFromDate= DateUtility.dateformatWithOutTime(actutalEndDate[1]);
							 }
						 } catch(Exception e) {
							 return RestResponseUtility.prepareErrorResponseWithoutData("Invalid Activity actual end date parameter", "Invalid Activity actual end date parameter");
						 }
						 
					}
					
					List<JsonActivity> jsonActivities = activityService.listActivitiesBySerachCriteria(product.getTestFactory().getTestFactoryId(), product.getProductId(), productVersionId, productBuildId, userList, activityWorkPackageIdList, workflowStatusList, workflowStatusTypeList, activityTypeList, activityAssigneeList, activityReviewerList, 
							plannedStartFromDate,plannedStartToDate,plannedStartDateSearch,
							plannedEndFromDate,plannedEndToDate,plannedEndDateSearch, 
							actualStartFromDate,actualStartToDate,actualStartDateSearch,
							actualEndFromDate,actualEndToDate,actualEndDateSearch);
					
							
					List<HashMap<String, Object>> customFieldValueList=customFieldService.getAllCustomFieldsExistInstance(entityId, parentEntityInstanceId, product.getTestFactory().getTestFactoryId(), product.getProductId(), active, new Date());
					if(jsonActivities != null && jsonActivities.size() >0){
						for(JsonActivity jsonActivity:jsonActivities) {
							JSONObject responseJson = new JSONObject();
							responseJson=jsonActivity.getCleanJson();
							List<WorkflowEvent> workflowEvents= workflowEventService.listWorkflowEventByEntityTypeAndEnityInstanceId(IDPAConstants.ENTITY_ACTIVITY_TYPE, jsonActivity.getActivityId(), 1);
							
							if(workflowEvents != null && workflowEvents.size() >0) {
								
								String eventDetails="";
								for (WorkflowEvent event:workflowEvents) {
									if(event.getCurrentStatus() != null){
										 String targetStatus=event.getTargetStatus() != null ?event.getTargetStatus().getWorkflowStatusName():" ";
										eventDetails += jsonActivity.getActivityName()+": [" +jsonActivity.getActivityId() +"] " + event.getCurrentStatus().getWorkflowStatusName() + " - " +targetStatus+" - " + event.getModifiedBy().getLoginId() +" - "+event.getLastUpdatedDate() +" - "+ event.getActualEffort()+" Completed \n";  
									}
									
								}
								responseJson.put("eventDetail",eventDetails);
							}
							
							if(customFieldValueList != null && customFieldValueList.size() >0) {
								for(Map<String,Object> customField:customFieldValueList) {
									String instanceId= customField.get("Id") != null ?customField.get("Id").toString():"0";
									if(jsonActivity.getActivityId().equals(Integer.parseInt(instanceId))) {
										responseJson.put("customField", customField);
									}
								}
							}
							arr.put(responseJson);	
						}
					}
				
			} else {
				return RestResponseUtility.prepareErrorResponseWithoutData("ProductName is required", "ProductName is required");
			}
		return RestResponseUtility.prepareSuccessResponseWithJSONArray("Listing Activity details", arr);
	
		}catch(Exception e) {
			return RestResponseUtility.prepareErrorResponseWithoutData("Error in Listing Activity details"+e,"Error in Listing Activity details"+e);
		}
	 }

	@POST
	@Path("/activityManagement/query/getActivitiesWithComments")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response getActivitiesWithComments(String activityFormData) throws JSONException {
	SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
	JSONArray arr = new JSONArray();
	try {
		
		JSONParser parser = new JSONParser();
		org.json.simple.JSONObject jsonFormatObject = (org.json.simple.JSONObject) parser.parse(activityFormData);
		if(jsonFormatObject == null) {
			log.info("Could not parse request : /activityManagement/query/getActivites");
			return RestResponseUtility.prepareErrorResponseWithoutData("No inputs provided", "No inputs provided");
		}
		String userName=jsonFormatObject.get("user") != null ? jsonFormatObject.get("user").toString():"";
		String productName=(String)jsonFormatObject.get("productName") != null?jsonFormatObject.get("productName").toString():"";
		String activityWorkPackageNames=(String)jsonFormatObject.get("activityWorkPackageName") != null ?jsonFormatObject.get("activityWorkPackageName").toString():"";
		String activityWorkPackageIds=(String)jsonFormatObject.get("activityWorkPackageId") != null ?jsonFormatObject.get("activityWorkPackageId").toString():"";
		
		String startDateParam = (String)jsonFormatObject.get("startDate") != null ?jsonFormatObject.get("startDate").toString():"";
		String endDateParam = (String)jsonFormatObject.get("endDate") != null ?jsonFormatObject.get("endDate").toString():"";
		String comment = (String)jsonFormatObject.get("comment") != null ?jsonFormatObject.get("comment").toString():"";
		
		
		ProductMaster product= null;
		Integer entityId=28;
		Integer parentEntityInstanceId=0;
		Integer active=1;
		//Authenticate the user
	
		Response authResponse = authenticateUser(userName, "/activityManagement/query/getActivites");
		if (authResponse != null)
			return authResponse;
		UserList userList = userListService.getUserByLoginId(userName);
		request.getSession().setAttribute("USER", userList);	
			if(productName != null && !productName.trim().isEmpty()) {
				product=productMasterDAO.getProductByName(productName);
				if(product == null) {
					return RestResponseUtility.prepareErrorResponseWithoutData("Invalid ProductName", "Invalid ProductName");
				}
				//Check authorization for user
				authResponse = checkUserAuthorization(product.getProductId(), userName, "/activityManagement/query/getActivites");
				if (authResponse != null)
					return authResponse;
				
				
					List<Integer> activityWorkPackageIdList=new ArrayList<>();
							if(activityWorkPackageIds != null && !activityWorkPackageIds.trim().isEmpty()) {
								for(String workpackageId : activityWorkPackageIds.split(",")){
									ActivityWorkPackage actityWp=activityWorkPackageService.getActivityWorkPackageById(Integer.parseInt(workpackageId),1);
									if(actityWp != null) {
										activityWorkPackageIdList.add(actityWp.getActivityWorkPackageId());
									}
							}
							
							if(activityWorkPackageNames != null && !activityWorkPackageNames.trim().isEmpty()){
								
								for(String workpackageName : activityWorkPackageNames.split(",")){
									ActivityWorkPackage actityWp=activityWorkPackageService.getLastestActivityWorkPackageByNameInProduct(workpackageName,product.getProductId());
									if(actityWp != null) {
										activityWorkPackageIdList.add(actityWp.getActivityWorkPackageId());
									}
								}	
							}
							
							if((activityWorkPackageIds != null && !activityWorkPackageIds.isEmpty()) || (activityWorkPackageNames != null && !activityWorkPackageNames.trim().isEmpty())){
								if(activityWorkPackageIdList == null || activityWorkPackageIdList.size() ==0) {
									return RestResponseUtility.prepareErrorResponseWithoutData("Invalid Activity Workpackage Name or WorkpackageId ", "Invalid  Activity Workpackage Name or WorkpackageId");
								}
							}
						 }
					
					
					
					Date startDate =null;
					Date endDate = null;
					
					if(startDateParam != null && !startDateParam.trim().isEmpty()) {
						 try {
							 startDate= DateUtility.dateformatWithOutTime(startDateParam);
						 } catch(Exception e) {
							 return RestResponseUtility.prepareErrorResponseWithoutData("Invalid start date parameter", "Invalid start date parameter");
						 }
						 
					}
					if(endDateParam != null && !endDateParam.trim().isEmpty()) {
						 try {
							 endDate= DateUtility.dateformatWithOutTime(endDateParam);
						 } catch(Exception e) {
							 return RestResponseUtility.prepareErrorResponseWithoutData("Invalid start date parameter", "Invalid start date parameter");
						 }
						 
					}
							
					List<Comments> comments = commonService.getCommentsListBasedOnDateFillterAndComment(product.getProductId(),IDPAConstants.ACTIVITY_ENTITY_MASTER_ID, startDate, endDate, comment);
					
					if(comments != null && comments.size() >0) {
						for(Comments cmt:comments) {
							JSONObject responseJson = new JSONObject();
							Activity activity=activityService.getActivityById(cmt.getEntityId(),1);
							if(activity != null) {
								 if(activityWorkPackageIdList != null && activityWorkPackageIdList.size() >0) {
									 for(Integer wpId:activityWorkPackageIdList){
										 if(wpId.equals(activity.getActivityWorkPackage().getActivityWorkPackageId())) {
											responseJson.put("activityId", activity.getActivityId());
											responseJson.put("activityName", activity.getActivityName());
											responseJson.put("activityWorkpackageId", activity.getActivityWorkPackage().getActivityWorkPackageId());
											responseJson.put("activityWorkpackageName", activity.getActivityWorkPackage().getActivityWorkPackageName());
											responseJson.put("productId", activity.getActivityWorkPackage().getProductBuild().getProductVersion().getProductMaster().getProductId());
											responseJson.put("productName", activity.getActivityWorkPackage().getProductBuild().getProductVersion().getProductMaster().getProductName());
											responseJson.put("workflowStatusId", activity.getWorkflowStatus().getWorkflowStatusId());
											responseJson.put("workflowStatusName", activity.getWorkflowStatus().getWorkflowStatusName());
											responseJson.put("activityTypeId", activity.getActivityMaster().getActivityMasterId());
											responseJson.put("activityTypeName", activity.getActivityMaster().getActivityMasterName());
											responseJson.put("assignee", activity.getAssignee().getLoginId());
											responseJson.put("reviewer", activity.getReviewer().getLoginId());
											responseJson.put("commentDate", cmt.getCreatedDate());
											responseJson.put("commentOwner", cmt.getCreatedBy().getLoginId());
											responseJson.put("comment", cmt.getComments());
										 }
									 }
								 } else {
									 	responseJson.put("activityId", activity.getActivityId());
										responseJson.put("activityName", activity.getActivityName());
										responseJson.put("activityWorkpackageId", activity.getActivityWorkPackage().getActivityWorkPackageId());
										responseJson.put("activityWorkpackageName", activity.getActivityWorkPackage().getActivityWorkPackageName());
										responseJson.put("productId", activity.getActivityWorkPackage().getProductBuild().getProductVersion().getProductMaster().getProductId());
										responseJson.put("productName", activity.getActivityWorkPackage().getProductBuild().getProductVersion().getProductMaster().getProductName());
										responseJson.put("workflowStatusId", activity.getWorkflowStatus().getWorkflowStatusId());
										responseJson.put("workflowStatusName", activity.getWorkflowStatus().getWorkflowStatusName());
										responseJson.put("activityTypeId", activity.getActivityMaster().getActivityMasterId());
										responseJson.put("activityTypeName", activity.getActivityMaster().getActivityMasterName());
										responseJson.put("assignee", activity.getAssignee().getLoginId());
										responseJson.put("reviewer", activity.getReviewer().getLoginId());
										responseJson.put("commentDate", cmt.getCreatedDate());
										responseJson.put("commentOwner", cmt.getCreatedBy().getLoginId());
										responseJson.put("comment", cmt.getComments());
								 }
							}
							if(responseJson != null && responseJson.length() >0) {
								arr.put(responseJson);		
							}
						}
					}
				
			} else {
				return RestResponseUtility.prepareErrorResponseWithoutData("ProductName is required", "ProductName is required");
			}
		return RestResponseUtility.prepareSuccessResponseWithJSONArray("Listing Activity comments", arr);
	
		}catch(Exception e) {
			return RestResponseUtility.prepareErrorResponseWithoutData("Error in Listing Activity details"+e,"Error in Listing Activity details"+e);
		}
	 }
	
	
	
	
}
