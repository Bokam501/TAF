package com.hcl.atf.taf.controller;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
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
import com.hcl.atf.taf.controller.utilities.DateUtility;
import com.hcl.atf.taf.dao.ActivityDAO;
import com.hcl.atf.taf.dao.ActivityTaskDAO;
import com.hcl.atf.taf.dao.ProductBuildDAO;
import com.hcl.atf.taf.dao.ProductUserRoleDAO;
import com.hcl.atf.taf.dao.ProductVersionListMasterDAO;
import com.hcl.atf.taf.integration.data.excel.ExcelTestDataIntegrator;
import com.hcl.atf.taf.model.Activity;
import com.hcl.atf.taf.model.ActivityMaster;
import com.hcl.atf.taf.model.ActivityResult;
import com.hcl.atf.taf.model.ActivitySecondaryStatusMaster;
import com.hcl.atf.taf.model.ActivityTask;
import com.hcl.atf.taf.model.ActivityTaskType;
import com.hcl.atf.taf.model.ActivityWorkPackage;
import com.hcl.atf.taf.model.ChangeRequest;
import com.hcl.atf.taf.model.CustomFieldValues;
import com.hcl.atf.taf.model.Customer;
import com.hcl.atf.taf.model.EntityMaster;
import com.hcl.atf.taf.model.EntityRelationship;
import com.hcl.atf.taf.model.EntityUserGroup;
import com.hcl.atf.taf.model.EnvironmentCombination;
import com.hcl.atf.taf.model.ExecutionTypeMaster;
import com.hcl.atf.taf.model.ProductBuild;
import com.hcl.atf.taf.model.ProductFeature;
import com.hcl.atf.taf.model.ProductMaster;
import com.hcl.atf.taf.model.ProductUserRole;
import com.hcl.atf.taf.model.ProductVersionListMaster;
import com.hcl.atf.taf.model.StatusCategory;
import com.hcl.atf.taf.model.TestFactory;
import com.hcl.atf.taf.model.UserList;
import com.hcl.atf.taf.model.dto.ActivitiesDTO;
import com.hcl.atf.taf.model.dto.ActivityWorkPackageSummaryDTO;
import com.hcl.atf.taf.model.dto.ProductAWPSummaryDTO;
import com.hcl.atf.taf.model.dto.ProductSummaryForActivityProcessDTO;
import com.hcl.atf.taf.model.json.JsonActivity;
import com.hcl.atf.taf.model.json.JsonActivityGantt;
import com.hcl.atf.taf.model.json.JsonActivityMaster;
import com.hcl.atf.taf.model.json.JsonActivityTask;
import com.hcl.atf.taf.model.json.JsonActivityTaskType;
import com.hcl.atf.taf.model.json.JsonActivityWorkPackage;
import com.hcl.atf.taf.model.json.JsonEntityRelationship;
import com.hcl.atf.taf.model.json.JsonEnvironmentCombination;
import com.hcl.atf.taf.model.json.JsonProductAWPSummary;
import com.hcl.atf.taf.model.json.JsonProductSummaryForActivityProcessDTO;
import com.hcl.atf.taf.model.json.jtable.JTableResponse;
import com.hcl.atf.taf.model.json.jtable.JTableResponseOptions;
import com.hcl.atf.taf.model.json.jtable.JTableSingleResponse;
import com.hcl.atf.taf.mongodb.constants.MongodbConstants;
import com.hcl.atf.taf.mongodb.service.MongoDBService;
import com.hcl.atf.taf.service.ActivityResultService;
import com.hcl.atf.taf.service.ActivitySecondaryStatusMasterService;
import com.hcl.atf.taf.service.ActivityService;
import com.hcl.atf.taf.service.ActivityStatusService;
import com.hcl.atf.taf.service.ActivityTaskService;
import com.hcl.atf.taf.service.ActivityTypeService;
import com.hcl.atf.taf.service.ActivityWorkPackageService;
import com.hcl.atf.taf.service.ChangeRequestService;
import com.hcl.atf.taf.service.CommentsTrackerService;
import com.hcl.atf.taf.service.CommonService;
import com.hcl.atf.taf.service.CustomFieldService;
import com.hcl.atf.taf.service.CustomerService;
import com.hcl.atf.taf.service.DimensionService;
import com.hcl.atf.taf.service.EntityRelationshipCommonService;
import com.hcl.atf.taf.service.EntityRelationshipService;
import com.hcl.atf.taf.service.EnvironmentService;
import com.hcl.atf.taf.service.EventsService;
import com.hcl.atf.taf.service.ExecutionTypeMasterService;
import com.hcl.atf.taf.service.NotificationService;
import com.hcl.atf.taf.service.ProductListService;
import com.hcl.atf.taf.service.StatusService;
import com.hcl.atf.taf.service.UserListService;
import com.hcl.atf.taf.service.WorkPackageService;
import com.hcl.ilcm.workflow.dao.WorkflowActivityDAO;
import com.hcl.ilcm.workflow.model.WorkflowEvent;
import com.hcl.ilcm.workflow.model.WorkflowMaster;
import com.hcl.ilcm.workflow.model.WorkflowStatus;
import com.hcl.ilcm.workflow.model.json.JsonWorkflowEvent;
import com.hcl.ilcm.workflow.service.ConfigurationWorkFlowService;
import com.hcl.ilcm.workflow.service.WorkflowEventService;
import com.hcl.ilcm.workflow.service.WorkflowMasterService;
import com.hcl.ilcm.workflow.service.WorkflowStatusPolicyService;
import com.hcl.ilcm.workflow.service.WorkflowStatusService;

@Controller
public class ActivityManagementController {
	private static final Log log = LogFactory
			.getLog(ActivityManagementController.class);

	@Autowired
	private ActivityWorkPackageService activityWorkPackageService;
	@Autowired
	private WorkflowStatusService workflowStatusService; 
	@Autowired
	private ActivityService activityService;
	@Autowired
	private ActivityTaskService activityTaskService;
	@Autowired
	private ProductListService productListService;
	@Autowired
	private ChangeRequestService changeRequestService;
	@Autowired
	private ActivityTypeService activityTypeService;
	@Autowired
	private EnvironmentService environmentService;
	@Autowired
	private CustomerService customerService;
	@Autowired
	private ActivityResultService activityResultService;
	@Autowired
	private MongoDBService mongoDBService;	
	@Autowired
	private ExcelTestDataIntegrator excelTestDataIntegrator;
	@Autowired
	private CommentsTrackerService commentsTrackerService;
	@Autowired
	private WorkPackageService workPackageService; 
	@Autowired
	private EventsService eventsService;
	@Autowired
	private StatusService statusService;
	@Autowired
	private ActivityTaskDAO activityTaskDAO;
	
	@Autowired
	private ProductUserRoleDAO productUserRoleDAO;
	
	@Autowired
	private WorkflowEventService workflowEventService;
	
	
	@Autowired
	private WorkflowActivityDAO workflowActivityDAO;
	
	
	
	@Autowired
	private ActivitySecondaryStatusMasterService activitySecondaryStatusMasterService;
	
	@Autowired
	private DimensionService dimensionService;
	
	@Autowired
	private ActivityDAO activityDao;	
	
	@Autowired
	private UserListService userListService; 
	
	@Autowired
	private WorkflowStatusPolicyService workflowStatusPolicyService;
	
	@Autowired
	private WorkflowMasterService workflowMasterService;
	
	@Autowired
	private EntityRelationshipService entityRelationshipService;
	
	@Autowired
	private ActivityStatusService activityStatusService;
	
	@Autowired
	private ConfigurationWorkFlowService configurationWorkFlowService;
	
	@Autowired
	private CommonService commonService;
	
	@Autowired
	private CustomFieldService customFieldService;
	
	@Autowired
	private ExecutionTypeMasterService executionTypeMasterService;
	
	@Autowired
	private NotificationService notificationService;
	
	@Autowired
	private ProductVersionListMasterDAO productVersionListMasterDAO;
	
	@Autowired
	private ProductBuildDAO productBuildDAO;
	
	@Autowired
	private EntityRelationshipCommonService entityRelationshipCommonService;
	
	private static String specialChars="[!@#$%^&*()+~:?;{}]";

	@RequestMapping(value = "process.workRequest.list", method = RequestMethod.POST, produces = "application/json")
	public @ResponseBody
	JTableResponse viewWorkRequestStatusSummary(HttpServletRequest request,@RequestParam int productId,@RequestParam int productVersionId,
			@RequestParam int productBuildId,@RequestParam int isActive, @RequestParam int jtStartIndex,
			@RequestParam int jtPageSize) {
		log.debug("inside process.workRequest.list");
		JTableResponse jTableResponse;
		try {
			List<JsonActivityWorkPackage> jsonActivityWorkPackage = null;
			Map<String, String> searchStrings = new HashMap<String, String>();
			UserList user = (UserList) request.getSession().getAttribute("USER");
			String searchassigneeId = request.getParameter("searchassigneeId");
			String searchpriority = request.getParameter("searchpriority");
			String searchstatusId = request.getParameter("searchstatusId");
			
			searchStrings.put("searchassigneeId", searchassigneeId);
            searchStrings.put("searchpriority", searchpriority);
            searchStrings.put("searchstatusId", searchstatusId);
            
			jsonActivityWorkPackage = activityWorkPackageService.listActivityWorkPackagessByBuildId(productId,productVersionId,productBuildId,isActive, searchStrings,user);
			Integer workpackageCount = activityWorkPackageService.listActivityWorkPackagesByProductIdCount(productId, productVersionId, productBuildId, isActive, searchStrings);
			
			jTableResponse = new JTableResponse("OK", jsonActivityWorkPackage, workpackageCount);
			log.debug("inside process fetching work request records ");
		} catch (Exception e) {
			jTableResponse = new JTableResponse("ERROR",
					"Error fetching records!");
			log.error("JSON ERROR", e);
		}
		return jTableResponse;
	}
	

	@RequestMapping(value = "activityWorkPackage.testFactory.product.level", method = RequestMethod.POST, produces = "application/json")
	public @ResponseBody JTableResponse listActivityWorkPackagesByTestFactoryProduct(
			HttpServletRequest request, @RequestParam int testFactoryId, @RequestParam int productId,
			@RequestParam int isActive, @RequestParam int jtStartIndex, @RequestParam int jtPageSize) {
		String logmsg = "";
		if(testFactoryId != -1){
			logmsg = " TestFactory Level";
		}else if(productId != -1){
			logmsg = " Product Level";
		}
		log.info("inside activityWorkPackage.testFactory.product.level "+logmsg);
		JTableResponse jTableResponse;
		List<JsonActivityWorkPackage> jsonActivityWorkPackageList = null;
		Integer activityWPTotalCount = 0;
		try {
			Map<String, String> searchStrings = new HashMap<String, String>();
			UserList user = (UserList) request.getSession().getAttribute("USER");
			
			String searchassigneeId = request.getParameter("searchassigneeId");
			String searchpriority = request.getParameter("searchpriority");
			String searchstatusId = request.getParameter("searchstatusId");
			
			searchStrings.put("searchassigneeId", searchassigneeId);
            searchStrings.put("searchpriority", searchpriority);
            searchStrings.put("searchstatusId", searchstatusId);
            
			jsonActivityWorkPackageList  = new ArrayList<JsonActivityWorkPackage>();
			jsonActivityWorkPackageList = activityWorkPackageService.listActivityWorkPackagesByTestFactoryId(testFactoryId, productId, 1, searchStrings,user, jtStartIndex, jtPageSize);			
			
			activityWPTotalCount =  activityWorkPackageService.getActivityWPCountByTestFactoryIdProductId(testFactoryId, productId);					
			
			jTableResponse = new JTableResponse("OK", jsonActivityWorkPackageList,activityWPTotalCount);
			log.debug("inside process fetching ActivityWorkPackage records ");
		} catch (Exception e) {
			jTableResponse = new JTableResponse("ERROR",
					"Error fetching ActivityWorkPackage records!");
			log.error("JSON ERROR", e);
		}
		return jTableResponse;
	}
	


	@RequestMapping(value = "process.workrequest.add", method = RequestMethod.POST, produces = "application/json")
	public @ResponseBody
	JTableSingleResponse addWorkRequest(HttpServletRequest request,
			@ModelAttribute JsonActivityWorkPackage jsonActivityWorkPackage,
			BindingResult result) {
		JTableSingleResponse jTableSingleResponse;
		 ProductBuild productBuild = null;
	        ProductVersionListMaster productVersion = null;
	        ProductMaster productMaster = null;
	        ActivityWorkPackage workRequest =null;
	        String subject = "Activity WorkPackage Creation";
		try {
			log.info("In process.workrequest.add");
			UserList user = (UserList) request.getSession()
					.getAttribute("USER");

			 workRequest = jsonActivityWorkPackage
					.getActivityWorkPackage();
			 
			 if(workRequest.getActivityWorkPackageName().matches(".*"+specialChars+".*")){
				 return jTableSingleResponse = new JTableSingleResponse("INFORMATION","ActivityWorkPackage Name consists of Invalid special characters!");
			 }
			 if (ActivityDateValidation(
						workRequest.getPlannedStartDate(),
						workRequest.getPlannedEndDate())) {
					return jTableSingleResponse = new JTableSingleResponse("INFORMATION",
							"Planned start date should be less than or equal to planned end date");
				}
			 
			 
			Date aWpPsDate=workRequest.getPlannedStartDate();
			Date aWpPeDate=workRequest.getPlannedEndDate();
			//workRequest.getOwner().getUserId();
			
			ProductBuild productbuild=productListService.getProductBuildById(workRequest.getProductBuild().getProductBuildId(), 1);
			
			workRequest.setProductBuild(productbuild);
			Boolean testingTeamUser=productListService.getTeamResourceByUserIdandProductIdandDate(productbuild.getProductVersion().getProductMaster().getProductId(),workRequest.getOwner().getUserId(),aWpPsDate,aWpPeDate);
			//log.info("testingTeamUser "+testingTeamUser);
			
			 workRequest.setCreatedDate(new Date());
			 workRequest.setModifiedDate(new Date());
			workRequest.setCreatedBy(user);
			workRequest.setIsActive(1);
			
			workRequest.setBaselineStartDate(workRequest.getPlannedStartDate());
			workRequest.setBaselineEndDate(workRequest.getPlannedEndDate());
			ActivityWorkPackage wpByName = activityWorkPackageService.getActivityWorkPackageByName(jsonActivityWorkPackage.getActivityWorkPackageName(), jsonActivityWorkPackage.getProductBuildId());
			if(wpByName !=null && wpByName.getActivityWorkPackageName() != null){
				jTableSingleResponse = new JTableSingleResponse("ERROR","WorkPackage name already exists");
			}
			else{
				WorkflowStatus workflowStatus = null;
				if(jsonActivityWorkPackage.getStatusId() ==  null || jsonActivityWorkPackage.getStatusId() == 0){
					workflowStatus = workflowStatusService.getInitialStatusForInstanceByWorkflowId(productbuild.getProductVersion().getProductMaster().getProductId(), IDPAConstants.ENTITY_ACTIVITY_WORKPACKAGE_ID, null, jsonActivityWorkPackage.getWorkflowId());
				}else{
					workflowStatus = workflowStatusService.getWorkflowStatusById(jsonActivityWorkPackage.getStatusId());
				}
				if(workflowStatus == null || workflowStatus.getWorkflowStatusId() == null || workflowStatus.getWorkflowStatusId() == 0){
					//Uncomment if WorkFlowStatus is Mandatory and  to be set while adding ActWP
					/*jTableSingleResponse = new JTableSingleResponse("ERROR","Unable to find status for activity workpackage. Please check workflow mapped for activity workpackage or mapped workflow has status");
					return jTableSingleResponse;*/
					workflowStatus = new WorkflowStatus();
					workflowStatus.setWorkflowStatusId(-1);				
				}
				workRequest.setWorkflowStatus(workflowStatus);
				
				if(workflowStatus.getWorkflowStatusId() == -1){
					workRequest.setActualStartDate(new Date());
					workRequest.setActualEndDate(new Date());
				}
				
				productMaster = productbuild.getProductVersion().getProductMaster();
				workRequest.setProductMaster(productMaster);
				
				
			activityWorkPackageService.addActivityWorkPackage(workRequest);
			if(workRequest !=null){
				List<UserList>productUserPermisionList = productUserRoleDAO.getProductResources(productMaster.getProductId());
				EntityMaster entityMaster = new EntityMaster();
				entityMaster.setEntitymasterid(IDPAConstants.ENTITY_ACTIVITY_WORKPACKAGE_ID);
				
				for(UserList userPermission: productUserPermisionList){
					EntityUserGroup entityUserGroup = new EntityUserGroup();
					entityUserGroup.setEntityInstanceId(workRequest.getActivityWorkPackageId());
					entityUserGroup.setEntityTypeId(entityMaster);
					entityUserGroup.setUser(userPermission);
					entityUserGroup.setMappedBy(user);
					entityUserGroup.setMappedDate(new Date());
					entityUserGroup.setProduct(productMaster);
					userListService.mapOrUnmapEntityUserGroup(entityUserGroup, "map");
				}
				
			}
			
			if(workRequest!=null){
				mongoDBService.addActivityWorkPackagetoMongoDB(workRequest.getActivityWorkPackageId());
			}
			
			if(workRequest != null && workRequest.getActivityWorkPackageId() != null){
				workflowStatusPolicyService.instanciateWorkflowStatusAndPoliciesForInstanceByWorkflowId(productbuild.getProductVersion().getProductMaster().getProductId(), IDPAConstants.ENTITY_ACTIVITY_WORKPACKAGE_ID, null, workRequest.getActivityWorkPackageId(), jsonActivityWorkPackage.getWorkflowId(), workRequest.getWorkflowStatus().getWorkflowStatusId(), user, workRequest.getPlannedStartDate(), workRequest);
			}			
			
			//Entity Audition History 
			eventsService.addNewEntityEvent(IDPAConstants.ENTITY_ACTIVITY_WORKPACKAGE, workRequest.getActivityWorkPackageId(), workRequest.getActivityWorkPackageName(), user);
			
			workRequest=activityWorkPackageService.getActivityWorkPackageById(workRequest.getActivityWorkPackageId(), 1);
		 	
			if(!testingTeamUser){
				jTableSingleResponse = new JTableSingleResponse("OK",new JsonActivityWorkPackage(workRequest)," The assigned owner for the created workpackage is not available for the '"+productbuild.getProductVersion().getProductMaster().getProductName()
						+"' in the given date range. ");
			}else{
				jTableSingleResponse = new JTableSingleResponse("OK",new JsonActivityWorkPackage(workRequest));
			}
			productBuild =workRequest.getProductBuild();
            productVersion = productBuild.getProductVersion();
            productMaster = productVersion.getProductMaster();			
			
            Integer productId = productMaster.getProductId();
            notificationService.processNotification(request,productId, IDPAConstants.NOTIFICATION_ACTIVITY_WORKPACKAGE_UPDATION, workRequest,subject);

			}
		} catch (Exception e) {
			jTableSingleResponse = new JTableSingleResponse("ERROR",
					"Error adding record!");
			log.error("JSON ERROR", e);
		}
		return jTableSingleResponse;

	}

	@RequestMapping(value = "process.workrequest.update", method = RequestMethod.POST, produces = "application/json")
	public @ResponseBody
	JTableResponse updateWorkRequest(HttpServletRequest request, @ModelAttribute JsonActivityWorkPackage jsonActivityWorkPackage,
			BindingResult result) {
		log.debug("process.workrequest.update");
		JTableResponse jTableResponse = null;
		ActivityWorkPackage workRequestFromUI = null;
		ActivityWorkPackage workRequestFromDB = null;
        ProductBuild productBuild = null;
        ProductVersionListMaster productVersion = null;
        ProductMaster productMaster = null;
        TestFactory testFactory = null;
        String remarks = "";
        String subject = "Activity Workpackage Updation";
        
		if (result.hasErrors()) {
			jTableResponse = new JTableResponse("ERROR", "Invalid Form!");
		}
		try {
			workRequestFromUI = jsonActivityWorkPackage
					.getActivityWorkPackage();
			if(workRequestFromUI.getActivityWorkPackageName() == ""){
				jTableResponse = new JTableResponse("INFORMATION","ActivityWorkPackage Name should not be empty!");
			}else if(workRequestFromUI.getActivityWorkPackageName().matches(".*"+specialChars+".*")){
				jTableResponse = new JTableResponse("INFORMATION","ActivityWorkPackage Name consists of Invalid special characters!");
			}else if(workRequestFromUI.getPlannedStartDate() == null && workRequestFromUI.getPlannedEndDate() != null){
				return jTableResponse = new JTableResponse("INFORMATION", "Please provide planned start date");
			}else if (ActivityDateValidation(workRequestFromUI.getPlannedStartDate(), workRequestFromUI.getPlannedEndDate())) {
				return jTableResponse = new JTableResponse("INFORMATION", "Planned start date should be less than or equal to planned end date");
			}else if(workRequestFromUI.getActualStartDate() == null && workRequestFromUI.getActualEndDate() != null){
				return jTableResponse = new JTableResponse("INFORMATION", "Please provide actual start date");
			}else if (ActivityDateValidation(workRequestFromUI.getActualStartDate(), workRequestFromUI.getActualEndDate())) {
				return jTableResponse = new JTableResponse("INFORMATION", "Actual start date should be less than or equal to actual end date");
			}else{
			
				if(workRequestFromUI.getBaselineStartDate() == null){
					workRequestFromUI.setBaselineStartDate(workRequestFromUI.getPlannedStartDate());
				}
				if(workRequestFromUI.getBaselineEndDate() == null){
					workRequestFromUI.setBaselineEndDate(workRequestFromUI.getPlannedEndDate());
				}
				
			workRequestFromDB = activityWorkPackageService.getActivityWorkPackageById(workRequestFromUI.getActivityWorkPackageId(), 1);
			ActivityWorkPackage actWPByName = activityWorkPackageService.getActivityWorkPackageByName(jsonActivityWorkPackage.getActivityWorkPackageName(), jsonActivityWorkPackage.getProductBuildId());
			
			UserList user = (UserList)request.getSession().getAttribute("USER");
			
			workRequestFromUI.setModifiedDate(new Date());	
			productBuild = workRequestFromDB.getProductBuild();
			productVersion = productBuild.getProductVersion();
			productMaster = productVersion.getProductMaster();
			testFactory = productMaster.getTestFactory();
			remarks = "TestFactory: "+testFactory.getTestFactoryName()+", Product: "+productMaster.getProductName()+", ActivityWorkpackage: "+workRequestFromUI.getActivityWorkPackageName();
									
				if(workRequestFromDB.getActivityWorkPackageName() != null && workRequestFromUI.getActivityWorkPackageName() != null){
					if(workRequestFromDB.getActivityWorkPackageName().trim().equals(workRequestFromUI.getActivityWorkPackageName().trim())){					
						activityWorkPackageService.updateActivityWorkPackage(workRequestFromUI);
						
						/*Mongodb parent status*/
						if(jsonActivityWorkPackage.getModifiedField().equalsIgnoreCase(MongodbConstants.MODIFIED_FIELD_ISACTIVE)){
							mongoDBService.updateParentStatusInChildColletions(IDPAConstants.ENTITY_ACTIVITY_WORKPACKAGE_ID,jsonActivityWorkPackage.getActivityWorkPackageId(),jsonActivityWorkPackage.getIsActive());
						}
						mongoDBService.addActivityWorkPackagetoMongoDB(workRequestFromUI.getActivityWorkPackageId());
					
						//Entity Audition History //Update							
						eventsService.addEntityChangedEvent(IDPAConstants.ENTITY_ACTIVITY_WORKPACKAGE, workRequestFromUI.getActivityWorkPackageId(), workRequestFromUI.getActivityWorkPackageName(),
								jsonActivityWorkPackage.getModifiedField(), jsonActivityWorkPackage.getModifiedFieldTitle(),
								jsonActivityWorkPackage.getOldFieldValue(), jsonActivityWorkPackage.getModifiedFieldValue(), user, remarks);
						
						List<JsonActivityWorkPackage> jsonActivityWorkPackageList =new ArrayList<JsonActivityWorkPackage>();
						jsonActivityWorkPackageList.add(jsonActivityWorkPackage);
						jTableResponse = new JTableResponse("OK",jsonActivityWorkPackageList ,1);
			 							            
						Integer productId = productMaster.getProductId();
						notificationService.processNotification(request,productId, IDPAConstants.NOTIFICATION_ACTIVITY_WORKPACKAGE_UPDATION, workRequestFromUI,subject);
					}  
					else{
						if(actWPByName != null){
							jTableResponse = new JTableResponse("INFORMATION","ActivityWorkPackage Name already Exists For Same Build!");
						}
						else{
							activityWorkPackageService.updateActivityWorkPackage(workRequestFromUI);
							/*Mongodb parent status*/
							if(jsonActivityWorkPackage.getModifiedField().equalsIgnoreCase(MongodbConstants.MODIFIED_FIELD_ISACTIVE)){
								mongoDBService.updateParentStatusInChildColletions(IDPAConstants.ENTITY_ACTIVITY_WORKPACKAGE_ID,jsonActivityWorkPackage.getActivityWorkPackageId(),jsonActivityWorkPackage.getIsActive());
							}
							
							
							if(workRequestFromUI!=null && workRequestFromUI.getActivityWorkPackageId()!=null){
								mongoDBService.addActivityWorkPackagetoMongoDB(workRequestFromUI.getActivityWorkPackageId());							
								//Entity Audition History //Update	
								eventsService.addEntityChangedEvent(IDPAConstants.ENTITY_ACTIVITY_WORKPACKAGE, workRequestFromUI.getActivityWorkPackageId(), workRequestFromUI.getActivityWorkPackageName(),
										jsonActivityWorkPackage.getModifiedField(), jsonActivityWorkPackage.getModifiedFieldTitle(),
										jsonActivityWorkPackage.getOldFieldValue(), jsonActivityWorkPackage.getModifiedFieldValue(), user, remarks);
							}
							List<JsonActivityWorkPackage> jsonActivityWorkPackageList =new ArrayList<JsonActivityWorkPackage>();
							jsonActivityWorkPackageList.add(jsonActivityWorkPackage);
							jTableResponse = new JTableResponse("OK",jsonActivityWorkPackageList ,1);
						}
				    }
				}  
			}
		} catch (Exception e) {
			jTableResponse = new JTableResponse("ERROR",
					"Unable to update the Activity Work Package!");
			log.error("JSON ERROR", e);
		}
		return jTableResponse;
	}

	@RequestMapping(value = "process.workrequest.update.modifiedfield", method = RequestMethod.POST, produces = "application/json")
	public @ResponseBody
	JTableResponse updateWorkRequestModifiedField(HttpServletRequest request, @RequestParam Integer activityWorkPackageId,
			@RequestParam String modifiedfField,@RequestParam String modifiedValue ) {
		log.debug("process.workrequest.update.modifiedfield");
		JTableResponse jTableResponse = null;
		ActivityWorkPackage workRequestFromUI = null;
		ActivityWorkPackage workRequestFromDB = null;
        ProductBuild productBuild = null;
        ProductVersionListMaster productVersion = null;
        ProductMaster productMaster = null;
        
		
		try {
			
		} catch (Exception e) {
			jTableResponse = new JTableResponse("ERROR",
					"Unable to update the Activity Work Package!");
			log.error("JSON ERROR", e);
		}
		return jTableResponse;
	}

	@RequestMapping(value = "process.workrequest.delete", method = RequestMethod.POST, produces = "application/json")
	public @ResponseBody JTableResponse deleteWorkRequest(HttpServletRequest request,@RequestParam int activityWorkPackageId) {
		log.debug("inside process.workrequest.delete");
		JTableResponse jTableResponse;
		String message = "";
		String referencedTableName = "activity_work_package";
		String referencedColumnName = "activityWorkPackageId";
		String subject = "Activity WorkPackage Deletion";
		try{
			message = activityWorkPackageService.deleteActivityWorkPackage(activityWorkPackageId, referencedTableName, referencedColumnName);
			if(message.equals("OK")){
				ActivityWorkPackage activityWorkpackage=activityWorkPackageService.getActivityWorkPackageById(activityWorkPackageId, 1);
				Integer productId=activityWorkpackage.getProductBuild().getProductVersion().getProductMaster().getProductId();
				notificationService.processNotification(request,productId, IDPAConstants.NOTIFICATION_ACTIVITY_WORKPACKAGE_DELETION, activityWorkpackage,subject);
				mongoDBService.deleteActivityWorkPackageFromMongoDb(activityWorkPackageId);
				jTableResponse = new JTableResponse(message);
				}else{
				jTableResponse = new JTableResponse("INFORMATION",message);
				}
		}catch(Exception e){
			jTableResponse = new JTableResponse("ERROR",
					"Unable to delete the Activity Work Package!");
			log.error("JSON ERROR", e);
		}
		return jTableResponse;
	}
	
	@RequestMapping(value = "process.activity.list", method = RequestMethod.POST, produces = "application/json")
	public @ResponseBody
	JTableResponse viewActivitySummary(HttpServletRequest req, int engagementId,@RequestParam int productId,@RequestParam int productVersionId,@RequestParam int productBuildId,@RequestParam int activityWorkPackageId,@RequestParam int isActive,
			@RequestParam int jtStartIndex, @RequestParam int jtPageSize) {
		log.info("Fetching Activity Summary");
		JTableResponse jTableResponse;
		try {
			List<JsonActivity> jsonActivity = null;
			Map<String, String> searchStrings = new HashMap<String, String>();
			String searchassigneeId = req.getParameter("searchassigneeId");
			String searchpriority = req.getParameter("searchpriority");
			String searchstatusId = req.getParameter("searchstatusId");
			
			 searchStrings.put("searchassigneeId", searchassigneeId);
            searchStrings.put("searchpriority", searchpriority);
            searchStrings.put("searchstatusId", searchstatusId);
			String statusType="All";
		jsonActivity = activityService.listActivities(searchStrings,engagementId,productId, productVersionId, productBuildId,activityWorkPackageId, 0,isActive,req,statusType,jtStartIndex,jtPageSize);

			 jTableResponse = new JTableResponse("OK", jsonActivity, activityService.getActivitiesCount(searchStrings,engagementId, productId, productVersionId, productBuildId, activityWorkPackageId, 0, isActive));    
		} catch (Exception e) {
			jTableResponse = new JTableResponse("ERROR",
					"Error Fetching Activity Summary records!");
			log.error("JSON ERROR Fetching Activity Summary", e);
		}
		return jTableResponse;
	}

	@RequestMapping(value = "process.activity.add", method = RequestMethod.POST, produces = "application/json")
	public @ResponseBody JTableSingleResponse addActivity( HttpServletRequest request,@ModelAttribute JsonActivity jsonActivity,BindingResult result) {	
	JTableSingleResponse jTableSingleResponse;
		try {
			
	            ProductBuild productBuild = null;
	            ProductVersionListMaster productVersion = null;
	            ProductMaster productMaster = null;
	            TestFactory testFactory = null;
	            Customer customer = null;
	        	UserList user = (UserList) request.getSession().getAttribute("USER");
	        	int userId=user.getUserId();
				UserList userObj = userListService.getUserListById(userId);
				ActivityWorkPackage activityWorkPackage = null;
				ProductFeature feature = null;
				String customFileds=jsonActivity.getCustomFields();
				List<CustomFieldValues> customFieldValuesList = new ArrayList<CustomFieldValues>();
				if(customFileds != "" && !customFileds.isEmpty()) {
					String customFieldErrorMessage = activityService.customFieldValidationProcessing(customFileds, jsonActivity.getActivityMasterId(), user, customFieldValuesList, customFieldService);
					if(customFieldErrorMessage != null && !customFieldErrorMessage.trim().isEmpty()){
						return jTableSingleResponse = new JTableSingleResponse("ERROR", customFieldErrorMessage);
					}
				}
				
				Activity activity = jsonActivity.getActivity();
				String subject = "Activity Creation Notification";
	        
			if(activity.getActivityName() =="" || activity.getActivityName().isEmpty() ) {
				
				return jTableSingleResponse = new JTableSingleResponse("INFORMATION","Activity Name should not be empty!");
			}
			if(activity.getActivityName().matches(".*"+specialChars+".*")) {
				return jTableSingleResponse = new JTableSingleResponse("INFORMATION","Activity Name consists of Invalid special Characters!");
			}	
			
			if(activity.getActivityMaster() == null ){
				return jTableSingleResponse = new JTableSingleResponse("ERROR","Activity Type is Not Available for this customer!");
			}
			
			if (ActivityDateValidation(activity.getPlannedStartDate(),
					activity.getPlannedEndDate())) {
				return jTableSingleResponse = new JTableSingleResponse("INFORMATION",
						"Planned start date should be less than or equal to planned end date");
			}
			activityWorkPackage = activityWorkPackageService.getActivityWorkPackageById(jsonActivity.getActivityWorkPackageId(),99);
			activity.setActivityWorkPackage(activityWorkPackage);
			Date wpStDate = activityWorkPackage.getPlannedStartDate();
			Date wpEndDate = activityWorkPackage.getPlannedEndDate();
			Date actStDate = activity.getPlannedStartDate();
			Date actEndDate = activity.getPlannedEndDate();						
			log.info("process.activity.add wpStDate " + wpStDate);
			log.info("process.activity.add wpEndDate " + wpEndDate);						
			Boolean testingTeamUser=productListService.getTeamResourceByUserIdandProductIdandDate(activityWorkPackage.getProductBuild().getProductVersion().getProductMaster().getProductId(),jsonActivity.getAssigneeId(),actStDate,actEndDate);
			log.info("testingTeamUser "+testingTeamUser);			
			String errmsg="Please select Start Date & End Date between WorkPackage Date "+wpStDate.toString().replaceAll("00:00:00.0", "")+"  and "+wpEndDate.toString().replaceAll("00:00:00.0", "");
		
			if(jsonActivity.getProductFeatureId()!=null){
				feature = productListService.getByProductFeatureId(jsonActivity.getProductFeatureId());
				activity.setProductFeature(feature);
			}
			activity.setCreatedBy(user);
			activity.setModifiedBy(user);
			activity.setCreatedDate(new Date());
			activity.setModifiedDate(new Date());
			activity.setIsActive(1);
			
			activity.setBaselineStartDate(activity.getPlannedStartDate());
			activity.setBaselineEndDate(activity.getPlannedEndDate());
			
			if(jsonActivity.getPlannedActivitySize() == null) {
				activity.setPlannedActivitySize(1);
			}else {
				activity.setPlannedActivitySize(jsonActivity.getPlannedActivitySize());
			}
			activity.setBaselineActivitySize(activity.getPlannedActivitySize());
			activity.setBaselineEffort(activity.getPlannedEffort());
			
			if(jsonActivity.getActualActivitySize() == null) {
				activity.setActualActivitySize(activity.getPlannedActivitySize());
			}else {
				activity.setActualActivitySize(jsonActivity.getActualActivitySize());
			}
			
			if(activity.getActivityMaster() != null && activity.getActivityMaster().getActivityMasterId() != null){
				ActivityMaster activityMaster = activityTypeService.getActivityMasterById(activity.getActivityMaster().getActivityMasterId());
				if(activityMaster != null){
					Float weightage = activityMaster.getWeightage() == null ?0:activityMaster.getWeightage();
					Float plannedUnit = activity.getPlannedActivitySize() * weightage;
					Float actualUnit = activity.getActualActivitySize() * weightage * activity.getPercentageCompletion();
					Float baselineUnit = activity.getBaselineActivitySize() * weightage;
					activity.setBaselineUnit(baselineUnit);
					activity.setPlannedUnit(plannedUnit);
					activity.setActualUnit(actualUnit);
				}else{
					activity.setBaselineUnit((float)activity.getBaselineActivitySize());
					activity.setPlannedUnit((float)activity.getPlannedActivitySize());
					activity.setActualUnit((float)activity.getActualActivitySize());
				}
			}else{
				activity.setBaselineUnit((float)activity.getBaselineActivitySize());
				activity.setPlannedUnit((float)activity.getPlannedActivitySize());
				activity.setActualUnit((float)activity.getActualActivitySize());
			}
			
			Integer entityId = 0;
			if(activity.getActivityMaster() != null){
				entityId = activity.getActivityMaster().getActivityMasterId();
			}
			boolean errorFlagActivity = activityService.getActivityByWPIdandActivityName(jsonActivity.getActivityWorkPackageId(), jsonActivity.getActivityName());
			if(errorFlagActivity){
				jTableSingleResponse = new JTableSingleResponse("INFORMATION","Activity Name Already Exists For Same WorkPackage!");
			}else{
				WorkflowStatus workflowStatus = null;
				if(jsonActivity.getStatusId() ==  null || jsonActivity.getStatusId() == 0){
					workflowStatus = workflowStatusService.getInitialStatusForInstanceByWorkflowId(activityWorkPackage.getProductBuild().getProductVersion().getProductMaster().getProductId(), IDPAConstants.ENTITY_ACTIVITY_TYPE, entityId, jsonActivity.getWorkflowId());
				}else{
					workflowStatus = workflowStatusService.getWorkflowStatusById(jsonActivity.getStatusId());
				}
				if(workflowStatus == null || workflowStatus.getWorkflowStatusId() == null || workflowStatus.getWorkflowStatusId() == 0){
					jTableSingleResponse = new JTableSingleResponse("ERROR","Unable to find status for activity. Please check workflow mapped for activity type or mapped workflow has status");
					return jTableSingleResponse;
				}
				activity.setWorkflowStatus(workflowStatus);
				if(workflowStatus.getWorkflowStatusId() == -1){
					activity.setActualStartDate(new Date());
					activity.setActualEndDate(new Date());
				}
				
				productMaster = activityWorkPackage.getProductBuild().getProductVersion().getProductMaster();
				activity.setProductMaster(productMaster);
			
				if(activity.getPlannedEffort() != null && activity.getPlannedEffort() >0) {
					/*Integer calculatedPlannedEffort= DateUtility.getCalendarHoursBetweenDates(activity.getPlannedStartDate(), activity.getPlannedEndDate());
					int plannedEffort = activity.getPlannedEffort()+calculatedPlannedEffort;
					activity.setPlannedEffort((plannedEffort/24)*9);*/
					Integer calculatedPlannedEffort= DateUtility.getWorkingHoursBetweenTwoDates(activity.getPlannedStartDate(),DateUtility.formatedDateWithMaxDateWithTime(activity.getPlannedEndDate()) , 9);
					activity.setPlannedEffort(calculatedPlannedEffort);
				}
				
				if(jsonActivity.getIsImmidiateAutoAllocation() != null && jsonActivity.getIsImmidiateAutoAllocation() == 1){
					activity = (Activity) commonService.autoAllocationOfResource(activity);
				}
				
			activityService.addActivity(activity);
			if(activity != null && activity.getActivityId() != null){
				Integer productId = activityWorkPackage.getProductBuild().getProductVersion().getProductMaster().getProductId();
				Integer engagementId = activityWorkPackage.getProductBuild().getProductVersion().getProductMaster().getTestFactory().getTestFactoryId();
				workflowStatusPolicyService.instanciateWorkflowStatusAndPoliciesForInstanceByWorkflowId(productId, IDPAConstants.ENTITY_ACTIVITY_TYPE, entityId, activity.getActivityId(), jsonActivity.getWorkflowId(), activity.getWorkflowStatus().getWorkflowStatusId(), user, activity.getPlannedStartDate(), activity);
				if(activity.getLifeCycleStage() != null){
					workflowStatusPolicyService.addWorkflowInstanceLifeCycleStage(productId, IDPAConstants.ENTITY_ACTIVITY_ID, activity.getActivityId(), activity.getLifeCycleStage().getWorkflowStatusId(), DateUtility.dateToStringInSecond(activity.getPlannedStartDate()), DateUtility.dateToStringInSecond(activity.getPlannedEndDate()), DateUtility.dateToStringInSecond(new Date()), user.getUserId());
				}
				
				if(customFieldValuesList != null && customFieldValuesList.size() > 0){
					customFieldService.saveCustomFieldsListForInstance(customFieldValuesList, activity.getActivityId());
				}
			}
			mongoDBService.addActivitytoMongoDB(activity.getActivityId());
			
			eventsService.addNewEntityEvent(IDPAConstants.ENTITY_ACTIVITY, activity.getActivityId(), activity.getActivityName(), userObj);
			
			if(activityWorkPackage!=null){
				int activityWorkpackageStatusCategoryId = activityService.getActivityWorkpackageStatusCategoryIdByActivityStatusCategory(activityWorkPackage.getActivityWorkPackageId());
				if(activityWorkpackageStatusCategoryId <= 0){
					activityWorkpackageStatusCategoryId = 1;
				}
				StatusCategory statusCategory = new StatusCategory();
				statusCategory.setStatusCategoryId(activityWorkpackageStatusCategoryId);
				activityWorkPackage.setStatusCategory(statusCategory);
				activityWorkPackageService.updateActivityWorkPackage(activityWorkPackage);
				mongoDBService.addActivityWorkPackagetoMongoDB(activityWorkPackage.getActivityWorkPackageId());
			}
			
			//added for mongoDB insertion			       		
            log.info("process.activitytask.add Mongo DB insertion");				      
            activity = activityService.getActivityById(activity.getActivityId(),1);	
            activityWorkPackage = activity.getActivityWorkPackage();
            productBuild = activityWorkPackage.getProductBuild();
            productVersion = productBuild.getProductVersion();
            productMaster = productVersion.getProductMaster();
            testFactory = productMaster.getTestFactory();
            customer = productMaster.getCustomer();			
            
            Integer productId = productMaster.getProductId();
            String productName = productMaster.getProductName();			            
			log.info("process.activitytask.add Mongo DB insertion done");
		
			List<JsonActivity> jsonActivitieslist = new ArrayList<JsonActivity>();
			jsonActivity = new JsonActivity(activity);
			jsonActivitieslist.add(jsonActivity);
			workflowStatusPolicyService.setInstanceIndicators(testFactory.getTestFactoryId(),productId,activity.getActivityWorkPackage().getActivityWorkPackageId(), IDPAConstants.ENTITY_ACTIVITY_TYPE, jsonActivitieslist, IDPAConstants.ENTITY_ACTIVITY_ID,user, activity.getModifiedDate(), activity.getActivityMaster().getActivityMasterId(), activity.getActivityId(),IDPAConstants.ENABLE_PENDING_WITH_COLUMN);

			if(!testingTeamUser){
				jTableSingleResponse = new JTableSingleResponse("OK",jsonActivity," The assignee for the created activty may not available for the '"+activityWorkPackage.getProductBuild().getProductVersion().getProductMaster().getProductName()
						+"' in the given date range. ");
			}else{
				jTableSingleResponse = new JTableSingleResponse("OK",jsonActivity);
			}
			notificationService.processNotification(request,productId, IDPAConstants.NOTIFICATION_ACTIVITY_CREATION, activity,subject);
			log.info("process.activity.add Success");
			}
		} catch (Exception e) {
			jTableSingleResponse = new JTableSingleResponse("ERROR",
					"Error adding activity record!");
			log.error("JSON ERROR", e);
		}
		return jTableSingleResponse;
	}

	@RequestMapping(value = "process.activity.update", method = RequestMethod.POST, produces = "application/json")
	public @ResponseBody
	JTableResponse updateActivity(HttpServletRequest request,
			@ModelAttribute JsonActivity jsonActivity, BindingResult result) {
		log.debug("process.activity.update");
		JTableResponse jTableResponse = null;
		Activity workRequestFromUI = null;
		Activity workRequestFromDB = null;
		ProductFeature feature = null;
		
		 ProductBuild productBuild = null;
         ProductVersionListMaster productVersion = null;
         ProductMaster productMaster = null;
         TestFactory testFactory = null;
         Customer customer = null;
         ActivityWorkPackage activityWorkPackage = null;
         workRequestFromUI = jsonActivity.getActivity();
         UserList user = (UserList) request.getSession().getAttribute("USER");
         String remarks = "";
         String subject = "Activity Updation";
         if("lifeCycleStageId".equalsIgnoreCase(jsonActivity.getModifiedField()) || "activityMasterId".equalsIgnoreCase(jsonActivity.getModifiedField())){
        	 boolean isEligibleToChange = workflowEventService.checkInstanceEligibiltyToChangeWorkflowMapping(IDPAConstants.ENTITY_ACTIVITY_TYPE, jsonActivity.getActivityMasterId(), jsonActivity.getActivityId());
        	 if(!isEligibleToChange){
        		 jTableResponse = new JTableResponse("ERROR", "Instance under gone some operation(s), cannot change "+jsonActivity.getModifiedField()+" at this point");
        		 return jTableResponse;
        	 }
         }
        if(workRequestFromUI.getPlannedStartDate() == null && workRequestFromUI.getPlannedEndDate() != null){
			return jTableResponse = new JTableResponse("ERROR", "Please provide planned start date");
		}else if (ActivityDateValidation(workRequestFromUI.getPlannedStartDate(), workRequestFromUI.getPlannedEndDate())) {
			return jTableResponse = new JTableResponse("ERROR", "Planned start date should be less than or equal to planned end date");
		}
		
        if(workRequestFromUI.getBaselineStartDate() == null){
        	workRequestFromUI.setBaselineStartDate(workRequestFromUI.getPlannedStartDate());
        }
        if(workRequestFromUI.getBaselineEndDate() == null){
        	workRequestFromUI.setBaselineEndDate(workRequestFromUI.getPlannedEndDate());
        }
        
		if(workRequestFromUI.getActualStartDate() == null && workRequestFromUI.getActualEndDate() != null){
			return jTableResponse = new JTableResponse("ERROR", "Please provide actual start date");
		}else if (ActivityDateValidation(workRequestFromUI.getActualStartDate(), workRequestFromUI.getActualEndDate())) {
			return jTableResponse = new JTableResponse("ERROR", "Actual start date should be less than or equal to actual end date");
		}
		
         activityWorkPackage = activityWorkPackageService.getActivityWorkPackageById(jsonActivity.getActivityWorkPackageId(),99);
         workRequestFromUI.setActivityWorkPackage(activityWorkPackage);
			Date wpStDate = activityWorkPackage.getPlannedStartDate();
			Date wpEndDate = activityWorkPackage.getPlannedEndDate();
			Date actStDate = workRequestFromUI.getPlannedStartDate();
			Date actEndDate = workRequestFromUI.getPlannedEndDate();
			log.info("process.activity.add wpStDate " + wpStDate);
			log.info("process.activity.add wpEndDate " + wpEndDate);
         
         String errmsg="Please select Start Date & End Date between WorkPackage Date "+wpStDate.toString().replaceAll("00:00:00.0", "")+"  and "+wpEndDate.toString().replaceAll("00:00:00.0", "");
			
			if(actStDate!=null ||actEndDate!=null)
			{Integer dateresult = DateUtility.ActivityDateValidation(actStDate, actEndDate,
							wpStDate, wpEndDate);
					if (dateresult == 1) {
						return jTableResponse = new JTableResponse("ERROR",errmsg);
					} else if (dateresult == 2) {
						return jTableResponse = new JTableResponse("ERROR",errmsg);
					} else
						jTableResponse = new JTableResponse("OK");
				}
		if (result.hasErrors()) {
			jTableResponse = new JTableResponse("ERROR", "Invalid Form!");
		}
		try {
			if(jsonActivity.getProductFeatureId()!=null){
				feature = productListService.getByProductFeatureId(jsonActivity.getProductFeatureId());
			}
			workRequestFromUI.setProductFeature(feature);
			if(jsonActivity.getBaselineActivitySize() == null) {
				workRequestFromUI.setBaselineActivitySize(jsonActivity.getPlannedActivitySize());
			}else {
				workRequestFromUI.setBaselineActivitySize(jsonActivity.getBaselineActivitySize());
			}
			
			if(jsonActivity.getActualActivitySize() == null) {
				workRequestFromUI.setActualActivitySize(0);
			}else {
				workRequestFromUI.setActualActivitySize(jsonActivity.getActualActivitySize());
			}
			
			List<ActivityTask> activityTasks = activityTaskService.getActivityTaskById(workRequestFromUI.getActivityId());
			if((activityTasks == null || activityTasks.size() == 0) && workRequestFromUI.getActivityMaster() != null && workRequestFromUI.getActivityMaster().getActivityMasterId() != null){
				ActivityMaster activityMaster = activityTypeService.getActivityMasterById(workRequestFromUI.getActivityMaster().getActivityMasterId());
				if(activityMaster != null){
					Float weightage = activityMaster.getWeightage();
					if(weightage == null){
						weightage = 1.0F;
					}
					Float plannedUnit = workRequestFromUI.getPlannedActivitySize() == null ?0:workRequestFromUI.getPlannedActivitySize() * weightage;
					Float actualUnit = workRequestFromUI.getActualActivitySize() == null ?0:workRequestFromUI.getActualActivitySize() * weightage;
					workRequestFromUI.setPlannedUnit(plannedUnit);
					workRequestFromUI.setActualUnit(actualUnit);
				}else{
					workRequestFromUI.setPlannedUnit((float)(workRequestFromUI.getPlannedActivitySize() != null?workRequestFromUI.getPlannedActivitySize():0));
					workRequestFromUI.setActualUnit((float)(workRequestFromUI.getActualActivitySize()!=null?workRequestFromUI.getActualActivitySize():0));
				}
			}else{
				workRequestFromUI.setPlannedUnit((float)(workRequestFromUI.getPlannedActivitySize() != null?workRequestFromUI.getPlannedActivitySize():0));
				workRequestFromUI.setActualUnit((float)(workRequestFromUI.getActualActivitySize()!=null?workRequestFromUI.getActualActivitySize():0));
			}
			
			workRequestFromUI.setModifiedDate(new Date());

			if(workRequestFromUI.getActivityName() == ""){
				jTableResponse = new JTableResponse("INFORMATION","Activity Name should not be empty!");
			}else if(workRequestFromUI.getActivityName().matches(".*"+specialChars+".*")) {
				jTableResponse = new JTableResponse("INFORMATION","Activity Name consists of Invalid special Characters!");
			}
			else{
			workRequestFromDB = activityService.getActivityById(workRequestFromUI.getActivityId(),1);
			boolean errorFlagActivity = activityService.getActivityByWPIdandActivityName(jsonActivity.getActivityWorkPackageId(), jsonActivity.getActivityName());						
            
			if(workRequestFromUI.getActivityName()!= null && workRequestFromDB.getActivityName()!=null){
				if(workRequestFromUI.getActivityName().trim().equals(workRequestFromDB.getActivityName().trim())){
					workRequestFromUI.setModifiedBy(user);	
			if(jsonActivity.getModifiedFieldTitle().equalsIgnoreCase("Planned start Date") || jsonActivity.getModifiedFieldTitle().equalsIgnoreCase("Planned End Date")) {
				/*Integer calculatedPlannedEffort= DateUtility.getCalendarHoursBetweenDates(workRequestFromUI.getPlannedStartDate(), workRequestFromUI.getPlannedEndDate());
				workRequestFromUI.setPlannedEffort(workRequestFromUI.getPlannedEffort()+calculatedPlannedEffort);*/
				Integer calculatedPlannedEffort= DateUtility.getWorkingHoursBetweenTwoDates(workRequestFromUI.getPlannedStartDate(),DateUtility.formatedDateWithMaxDateWithTime(workRequestFromUI.getPlannedEndDate()) , 9);
				workRequestFromUI.setPlannedEffort(calculatedPlannedEffort);
				jsonActivity.setPlannedEffort(calculatedPlannedEffort);
			}	
			activityService.updateActivity(workRequestFromUI);									
			workRequestFromUI = activityService.getActivityById(workRequestFromUI.getActivityId(),1);	
            activityWorkPackage = workRequestFromUI.getActivityWorkPackage();
            productBuild = activityWorkPackage.getProductBuild();
            productVersion = productBuild.getProductVersion();
            productMaster = productVersion.getProductMaster();
            testFactory = productMaster.getTestFactory();
            customer = productMaster.getCustomer();
			
            Integer productId = productMaster.getProductId();
			UserList assigneeUserList=null;
			UserList reviewerUserList=null;			
			if(workRequestFromUI.getAssignee()!=null){
				 assigneeUserList=userListService.getUserListById(workRequestFromUI.getAssignee().getUserId());
			}
			if(workRequestFromUI.getReviewer()!=null){
				 reviewerUserList=userListService.getUserListById(workRequestFromUI.getReviewer().getUserId());
			}
			if(jsonActivity.getModifiedFieldTitle().equalsIgnoreCase("Reviewer") ){ 
				configurationWorkFlowService.changeInstnaceActorMapping(productId,  IDPAConstants.ENTITY_ACTIVITY_TYPE, workRequestFromUI.getActivityMaster().getActivityMasterId(),  workRequestFromUI.getActivityId(), workRequestFromUI.getWorkflowStatus().getWorkflow().getWorkflowId(), reviewerUserList);
			} else if(jsonActivity.getModifiedFieldTitle().equalsIgnoreCase("Assignee")) {
				configurationWorkFlowService.changeInstnaceActorMapping(productId,  IDPAConstants.ENTITY_ACTIVITY_TYPE, workRequestFromUI.getActivityMaster().getActivityMasterId(),  workRequestFromUI.getActivityId(), workRequestFromUI.getWorkflowStatus().getWorkflow().getWorkflowId(), assigneeUserList);
			}
			if(workRequestFromUI.getLifeCycleStage() != null){
				workflowStatusPolicyService.updateWorkflowInstanceLifeCycleStage(productId, IDPAConstants.ENTITY_ACTIVITY_ID, workRequestFromUI.getActivityId(), workRequestFromUI.getLifeCycleStage().getWorkflowStatusId(), DateUtility.dateToStringInSecond(workRequestFromUI.getPlannedStartDate()), DateUtility.dateToStringInSecond(workRequestFromUI.getPlannedEndDate()), DateUtility.dateToStringInSecond(new Date()), user.getUserId());
			}
			
			activityService.updateActivityPredecessors(workRequestFromUI, workRequestFromUI.getActivityPredecessors());
			
			remarks = "TestFactory: "+testFactory.getTestFactoryName()+", Product: "+productMaster.getProductName()+", ActivityWorkpackage: "+activityWorkPackage.getActivityWorkPackageName()+", Activity: "+workRequestFromUI.getActivityName();
			eventsService.addEntityChangedEvent(IDPAConstants.ENTITY_ACTIVITY, workRequestFromUI.getActivityId(), workRequestFromUI.getActivityName(),
					jsonActivity.getModifiedField(), jsonActivity.getModifiedFieldTitle(),
					jsonActivity.getOldFieldValue(), jsonActivity.getModifiedFieldValue(), user, remarks);

			/*Mongodb parent status*/
			if(jsonActivity.getModifiedField().equalsIgnoreCase(MongodbConstants.MODIFIED_FIELD_ISACTIVE)){
				mongoDBService.updateParentStatusInChildColletions(IDPAConstants.ACTIVITY_ENTITY_MASTER_ID,jsonActivity.getActivityId(),jsonActivity.getIsActive());
			}
			
			if(workRequestFromUI!=null){
				mongoDBService.addActivitytoMongoDB(workRequestFromUI.getActivityId());
			}

			if(activityWorkPackage!=null){
				int activityWorkpackageStatusCategoryId = activityService.getActivityWorkpackageStatusCategoryIdByActivityStatusCategory(activityWorkPackage.getActivityWorkPackageId());
				if(activityWorkpackageStatusCategoryId <= 0){
					activityWorkpackageStatusCategoryId = 1;
				}
				StatusCategory statusCategory = new StatusCategory();
				statusCategory.setStatusCategoryId(activityWorkpackageStatusCategoryId);
				activityWorkPackage.setStatusCategory(statusCategory);
				activityWorkPackageService.updateActivityWorkPackage(activityWorkPackage);
				mongoDBService.addActivityWorkPackagetoMongoDB(activityWorkPackage.getActivityWorkPackageId());
			}
			
			List<JsonActivity> jsonActivityList =new ArrayList<JsonActivity>();
			jsonActivityList.add(jsonActivity);
			 jTableResponse = new JTableResponse("OK",jsonActivityList ,1);
			 
			 
	            log.info("process.activitytask.add Mongo DB insertion");				      
	            					    		
				log.info("process.activitytask.add Mongo DB insertion done");
				notificationService.processNotification(request,productId,IDPAConstants.NOTIFICATION_ACTIVITY_UPDATION, workRequestFromUI,subject);
				}else{
					if(errorFlagActivity){
						jTableResponse = new JTableResponse("INFORMATION","Activity Name Already Exists For Same WorkPackage!");
					}else{
						
						activityService.updateActivity(workRequestFromUI);
						if(jsonActivity.getModifiedField().equalsIgnoreCase(MongodbConstants.MODIFIED_FIELD_ISACTIVE)){
							mongoDBService.updateParentStatusInChildColletions(IDPAConstants.ACTIVITY_ENTITY_MASTER_ID,jsonActivity.getActivityId(),jsonActivity.getIsActive());
						}
						
						if(workRequestFromUI!=null){
							mongoDBService.addActivitytoMongoDB(workRequestFromUI.getActivityId());
										
							eventsService.addEntityChangedEvent(IDPAConstants.ENTITY_ACTIVITY, workRequestFromUI.getActivityId(), workRequestFromUI.getActivityName(),
									jsonActivity.getModifiedField(), jsonActivity.getModifiedFieldTitle(),
									jsonActivity.getOldFieldValue(), jsonActivity.getModifiedFieldValue(), user, remarks);
						}
						if(activityWorkPackage!=null){
							int activityWorkpackageStatusCategoryId = activityService.getActivityWorkpackageStatusCategoryIdByActivityStatusCategory(activityWorkPackage.getActivityWorkPackageId());
							if(activityWorkpackageStatusCategoryId <= 0){
								activityWorkpackageStatusCategoryId = 1;
							}
							StatusCategory statusCategory = new StatusCategory();
							statusCategory.setStatusCategoryId(activityWorkpackageStatusCategoryId);
							activityWorkPackage.setStatusCategory(statusCategory);
							activityWorkPackageService.updateActivityWorkPackage(activityWorkPackage);
							mongoDBService.addActivityWorkPackagetoMongoDB(activityWorkPackage.getActivityWorkPackageId());
						}
						List<JsonActivity> jsonActivityList =new ArrayList<JsonActivity>();
						jsonActivityList.add(jsonActivity);
						 jTableResponse = new JTableResponse("OK",jsonActivityList ,1);
					}
				}
			}
			}
		} catch (Exception e) {
			jTableResponse = new JTableResponse("ERROR",
					"Unable to update the Activity!");
			log.error("JSON ERROR", e);
		}
		return jTableResponse;
	}

	@RequestMapping(value="process.activity.delete",method=RequestMethod.POST ,produces="application/json")
	public @ResponseBody JTableResponse deleteActivity(@RequestParam int activityId) {
		log.debug("inside process.activity.delete");
		JTableResponse jTableResponse = null;
		String message = "";
		String referencedTableName="activity";
		String referencedColumnName="activityId";
		try{
			message = activityService.deleteActivity(activityId, referencedTableName, referencedColumnName);
			if(message.equals("OK")){
				mongoDBService.deleteActivityFromMongodb(activityId);
			jTableResponse = new JTableResponse(message);
			}else{
			jTableResponse = new JTableResponse("INFORMATION",message);
			}
		}catch(Exception e){
			jTableResponse = new JTableResponse("ERROR","Error Deleting Records !");
			log.error("JSON ERROR", e);
		}
		return jTableResponse;
	}
	
	@RequestMapping(value = "process.activitytask.list", method = RequestMethod.POST, produces = "application/json")
	public @ResponseBody
	JTableResponse viewActivityTaskSummary(HttpServletRequest request,@RequestParam Integer productId,@RequestParam Integer productVersionId,@RequestParam Integer productBuildId,@RequestParam Integer activityWorkPackageId,
			@RequestParam Integer activityId, @RequestParam int isActive,
			@RequestParam int jtStartIndex, @RequestParam int jtPageSize) {
 		log.info("Fetching Activity Task Summary");
		JTableResponse jTableResponse;
		try {
			if(productId == null) 
				productId=0;
			if(productVersionId == null)
				productVersionId=0;
			if(productBuildId==null) 
				productBuildId=0;
			if(activityWorkPackageId == null)
				activityWorkPackageId=0;
			if(activityId == null)
				activityId=0;
			List<JsonActivityTask> jsonActivityTask = null;
			Map<String, String> searchStrings = new HashMap<String, String>();			
			UserList user = (UserList) request.getSession().getAttribute("USER");
			String searchassigneeId = request.getParameter("searchassigneeId");
			String searchpriority = request.getParameter("searchpriority");
			String searchstatusId = request.getParameter("searchstatusId");
			
			searchStrings.put("searchassigneeId", searchassigneeId);
            searchStrings.put("searchpriority", searchpriority);
            searchStrings.put("searchstatusId", searchstatusId);
			jsonActivityTask = activityTaskService.listActivityTasks(request,productId, productVersionId, productBuildId,activityWorkPackageId,activityId, 0, isActive,user,jtStartIndex,jtPageSize, searchStrings);
			Integer activityTasksCount = activityTaskService.listActivityTasksCount(productId, productVersionId, productBuildId,activityWorkPackageId,activityId, 0, isActive, searchStrings);
			jTableResponse = new JTableResponse("OK", jsonActivityTask, activityTasksCount);
		} catch (Exception e) {
			jTableResponse = new JTableResponse("ERROR",
					"Error Fetching Activity Task Summary records!");
			log.error("JSON ERROR Fetching Activity Task Summary", e);
		}
		return jTableResponse;
	}
	
	@RequestMapping(value = "process.activitytask.add", method = RequestMethod.POST, produces = "application/json")
	
	public @ResponseBody
	JTableSingleResponse addActivityTask( HttpServletRequest request,@ModelAttribute JsonActivityTask jsonActivityTask) {	
	JTableSingleResponse jTableSingleResponse;
		try {
			
		    ActivityWorkPackage activityWorkPackage = null;
            ProductBuild productBuild = null;
            ProductVersionListMaster productVersion = null;
            ProductMaster productMaster = null;
            TestFactory testFactory = null;
            Customer customer = null;
            //ActivityStatus activityStatus ;
            WorkflowStatus workflowStatus;
            UserList user = (UserList) request.getSession().getAttribute("USER");
			log.info("process.activitytask.add");			
			Activity activity = null;			
			String subject = "Activity Task Creation"; 
						
			activity = activityService.getActivityById(jsonActivityTask.getActivityId(),1);	
			if(jsonActivityTask.getPlannedStartDate()==null && jsonActivityTask.getPlannedEndDate()==null){
				jsonActivityTask.setPlannedStartDate(DateUtility.dateformatWithSlashWithOutTime(activity.getPlannedStartDate()));
				jsonActivityTask.setPlannedEndDate(DateUtility.dateformatWithSlashWithOutTime(activity.getPlannedEndDate()));
			}
				
			ActivityTask activityTask = jsonActivityTask.getActivityTask();
			
			if(activityTask.getActivityTaskName().matches(".*"+specialChars+".*")){
				return jTableSingleResponse = new JTableSingleResponse("INFORMATION","ActivityTask Name consists of Invalid special Characters!");
			}
			
			if(activityTask.getActivityTaskType() == null ){
				return jTableSingleResponse = new JTableSingleResponse("ERROR","ActivityTask Type is Not Available for this Product!");
			}
			if (ActivityDateValidation(activityTask.getPlannedStartDate(),
					activityTask.getPlannedEndDate())) {
				return jTableSingleResponse = new JTableSingleResponse("INFORMATION",
						"Planned start date should be less than or equal to planned end date");
			}
			
			Date actStDate = activity.getPlannedStartDate();
			Date actEndDate = activity.getPlannedEndDate();
			Date actTaskStDate = activityTask.getPlannedStartDate();
			Date actTaskEndDate = activityTask.getPlannedEndDate();
			//log.info("process.activity.add actStDate " + actStDate);
			//log.info("process.activity.add actEndDate " + actEndDate);
			
			
			
			
			String errmsg="Please select Start Date & End Date  between  activity  Date "+actStDate.toString().replaceAll("00:00:00.0", "")+"   and  "+actEndDate.toString().replaceAll("00:00:00.0", "");
			if(actStDate!=null ||actEndDate!=null)
			{
				Integer dateresult = DateUtility.ActivityDateValidation(actTaskStDate, actTaskEndDate,
					actStDate, actEndDate);
					if (dateresult == 1) {
						return jTableSingleResponse = new JTableSingleResponse("INFORMATION",errmsg);
					} else if (dateresult == 2) {
						return jTableSingleResponse = new JTableSingleResponse("INFORMATION",errmsg);
					} else
						 jTableSingleResponse = new JTableSingleResponse("OK");
				}
			activityTask.setActivity(activity);	
			activityTask.setCreatedBy(user);
			activityTask.setIsActive(1);
			activityTask.setModifiedBy(user);
			
			ActivityResult activityResult = activityResultService.getResultByName(IDPAConstants.STATUS_OPEN);
			ActivitySecondaryStatusMaster activitySecondaryStatusName = null;
			if(jsonActivityTask.getActivityTask() !=null && jsonActivityTask.getActivityTask().getSecondaryStatus() != null) {
				activitySecondaryStatusName = activitySecondaryStatusMasterService.getSecondaryStatusById(jsonActivityTask.getActivityTask().getSecondaryStatus().getActivitySecondaryStatusId());
				activityTask.setSecondaryStatus(activitySecondaryStatusName);	
			}
			
			UserList assigneeUser=userListService.getUserListById(jsonActivityTask.getAssigneeId());
			UserList reviewerUser=userListService.getUserListById(jsonActivityTask.getReviewerId());
			
			activityTask.setAssignee(assigneeUser);			
			activityTask.setReviewer(reviewerUser);	
			activityTask.setResult(activityResult);     
			activityTask.setCreatedDate(new Date());	
			activityTask.setModifiedDate(new Date());	
			activityTask.setCreatedBy(user);
			activityTask.setModifiedBy(user);
			
			activityTask.setBaselineStartDate(activityTask.getPlannedStartDate());
			activityTask.setBaselineEndDate(activityTask.getPlannedEndDate());
			
			if(jsonActivityTask.getPlannedTaskSize() == null) {
				activityTask.setPlannedTaskSize(0);
			}else {
				activityTask.setPlannedTaskSize(jsonActivityTask.getPlannedTaskSize());
			}
			
			activityTask.setBaselineTaskSize(activityTask.getPlannedTaskSize());
			activityTask.setBaselineEffort(activityTask.getPlannedEffort());
			
			if(jsonActivityTask.getActualTaskSize() == null) {
				activityTask.setActualTaskSize(0);
			}else {
				activityTask.setActualTaskSize(jsonActivityTask.getActualTaskSize());
			}
			
			if(activityTask.getActivityTaskType() != null && activityTask.getActivityTaskType().getActivityTaskTypeId() != null){
				ActivityTaskType activityTaskType = activityTaskService.getActivityTaskTypeById(activityTask.getActivityTaskType().getActivityTaskTypeId());
				if(activityTaskType != null){
					Float weightage = activityTaskType.getActivityTaskTypeWeightage();
					Float plannedUnit = activityTask.getPlannedTaskSize() * weightage;
					Float actualUnit = activityTask.getActualTaskSize() * weightage;
					activityTask.setPlannedUnit(plannedUnit);
					activityTask.setBaselineUnit(plannedUnit);
					activityTask.setActualUnit(actualUnit);
				}
			}
			boolean errorFlagActivityTask = activityTaskService.getActivityTaskById(jsonActivityTask.getActivityId(), jsonActivityTask.getActivityTaskName());
			if(errorFlagActivityTask){
				jTableSingleResponse = new JTableSingleResponse("INFORMATION","ActivityTask Name already Exists For Same Activity!");
			}
			else{
				
			
			//added for mongoDB insertion			       		
            log.info("process.activitytask.add Mongo DB insertion");				      
            activityWorkPackage = activity.getActivityWorkPackage();
            productBuild = activityWorkPackage.getProductBuild();
            productVersion = productBuild.getProductVersion();
            productMaster = productVersion.getProductMaster();
            testFactory = productMaster.getTestFactory();
            customer = productMaster.getCustomer();			
            
            Integer productId = productMaster.getProductId();
            String productName = productMaster.getProductName();			            
            String productVersionName=productVersion.getProductVersionName();
			String productBuildName=productBuild.getBuildname();
			String awpName=activityWorkPackage.getActivityWorkPackageName();						    		
			//mongoDBService.addActivityTaskToMongoDB(activityTask,productId,productName,productVersionId,productVersionName,productBuildId,productBuildName,awpId,awpName, testFactoryId, testFactoryName, customerId, customerName);
			Integer entityId = 0;
			if(activityTask.getActivityTaskType() != null){
				entityId = activityTask.getActivityTaskType().getActivityTaskTypeId();
			}
			if(jsonActivityTask.getStatusId() ==  null || jsonActivityTask.getStatusId() == 0){
				workflowStatus = workflowStatusService.getInitialStatusForInstanceByWorkflowId(productId, IDPAConstants.ENTITY_TASK_TYPE, entityId, jsonActivityTask.getWorkflowId());
			}else{
				workflowStatus = workflowStatusService.getWorkflowStatusById(jsonActivityTask.getStatusId());
			}
			if(workflowStatus == null || workflowStatus.getWorkflowStatusId() == null || workflowStatus.getWorkflowStatusId() == 0){
				jTableSingleResponse = new JTableSingleResponse("ERROR","Unable to find status for task. Please check workflow mapped for task type or mapped workflow has status");
				return jTableSingleResponse;
			}
			activityTask.setStatus(workflowStatus);
			if(workflowStatus.getWorkflowStatusId() == -1){
				activityTask.setActualStartDate(new Date());
				activityTask.setActualEndDate(new Date());
			}
			
			
				productMaster = activityWorkPackage.getProductBuild().getProductVersion().getProductMaster();
				activityTask.setProductMaster(productMaster); 

			
			
			activityTaskService.addActivityTask(activityTask);
			if(activityTask!=null && activityTask.getActivityTaskId()!=null){
				workflowStatusPolicyService.instanciateWorkflowStatusAndPoliciesForInstanceByWorkflowId(productId, IDPAConstants.ENTITY_TASK_TYPE, entityId, activityTask.getActivityTaskId(), jsonActivityTask.getWorkflowId(), activityTask.getStatus().getWorkflowStatusId(), user, activityTask.getPlannedStartDate(), activityTask);
				if(activityTask.getLifeCycleStage() != null){
					workflowStatusPolicyService.addWorkflowInstanceLifeCycleStage(productId,IDPAConstants.ENTITY_TASK, activityTask.getActivityTaskId(), activityTask.getLifeCycleStage().getWorkflowStatusId(), DateUtility.dateToStringInSecond(activityTask.getPlannedStartDate()), DateUtility.dateToStringInSecond(activityTask.getPlannedEndDate()), DateUtility.dateToStringInSecond(new Date()), user.getUserId());
				}
				mongoDBService.addActivityTaskToMongoDB(activityTask.getActivityTaskId());
			
				int userId=user.getUserId();
				UserList userObj = userListService.getUserListById(userId);
				EntityMaster entityMaster = workPackageService.getEntityMasterByName(IDPAConstants.ENTITY_ACTIVITY_TASK);
				//Entity Audition History 
				eventsService.addNewEntityEvent(IDPAConstants.ENTITY_ACTIVITY_TASK, activityTask.getActivityTaskId(), activityTask.getActivityTaskName(), userObj);				
			}
			
			if(activity != null){
				activityTaskService.updateActivityBasedOnTasks(activity.getActivityId());
				StatusCategory statusCategory = new StatusCategory();
				
				int activityStatusCategoryId = activityTaskService.getActivityStatusCategoryIdByTaskStatus(activity.getActivityId());
				if(activityStatusCategoryId <= 0){
					activityStatusCategoryId = 1;
				}
				statusCategory.setStatusCategoryId(activityStatusCategoryId);
				activity.setStatusCategory(statusCategory);
				activityService.updateActivity(activity);
				if(activity!=null){
					mongoDBService.addActivitytoMongoDB(activity.getActivityId());
				}
				
				int activityWorkpackageStatusCategoryId = activityService.getActivityWorkpackageStatusCategoryIdByActivityStatusCategory(activity.getActivityWorkPackage().getActivityWorkPackageId());
				if(activityWorkpackageStatusCategoryId <= 0){
					activityWorkpackageStatusCategoryId = 1;
				}
				statusCategory.setStatusCategoryId(activityWorkpackageStatusCategoryId);
				activityWorkPackage = activityWorkPackageService.getActivityWorkPackageById(activity.getActivityWorkPackage().getActivityWorkPackageId(), 1);
				activityWorkPackage.setStatusCategory(statusCategory);
				activityWorkPackageService.updateActivityWorkPackage(activityWorkPackage);
				if(activityWorkPackage!=null){
					mongoDBService.addActivityWorkPackagetoMongoDB(activityWorkPackage.getActivityWorkPackageId());
				}
			}
			log.info("process.activitytask.add Mongo DB insertion done");
			
			UserList assigneeUserList=userListService.getUserListById(activityTask.getAssignee().getUserId());
			UserList reviewerUserList=userListService.getUserListById(activityTask.getReviewer().getUserId());
				
			String assigneeMailId=assigneeUserList !=null ?assigneeUserList.getEmailId():"";
			String reviewerMailId=assigneeUserList !=null ?reviewerUserList.getEmailId():"";
			log.info("process.activitytask.add email send done");
			
			Boolean testingTeamUser=productListService.getTeamResourceByUserIdandProductIdandDate(productId,activityTask.getAssignee().getUserId(),actTaskStDate,actTaskEndDate);
			log.info("testingTeamUser "+testingTeamUser);
			if(!testingTeamUser){
				jTableSingleResponse = new JTableSingleResponse("INFORMATION",new JsonActivityTask(activityTask)," The assigned assignee for the created activity task is not available for the '"+productName
						+"' in the given date range. ");
			}else{
				jTableSingleResponse = new JTableSingleResponse("OK",new JsonActivityTask(activityTask));
			}
			
			String comments="";
			Integer primaryStatusId=workflowStatus.getWorkflowStatusId();
			String approveAllEntityInstanceIds=activityTask.getActivityTaskId().toString();
			
			notificationService.processNotification(request,productId, IDPAConstants.NOTIFICATION_ACTIVITY_TASK_CREATION, activityTask,subject);
			
			log.info("process.activitytask.add Success");
			}
		} catch (Exception e) {
			jTableSingleResponse = new JTableSingleResponse("ERROR",
					"Error adding activity task record!");
			log.error("JSON ERROR", e);
		}
		return jTableSingleResponse;
	}
	@RequestMapping(value = "process.activitytask.update", method = RequestMethod.POST, produces = "application/json")
	public @ResponseBody JTableResponse updateActivityTask(HttpServletRequest request, @ModelAttribute JsonActivityTask jsonActivityTask, BindingResult result) {
		log.info("process.activitytask.update");
		JTableResponse jTableResponse = null;
		ActivityTask requestFromUI = null;
		ActivityTask requestFromDB = null;
		String subject = "Activity Task Updation";
		if (result.hasErrors()) {
			jTableResponse = new JTableResponse("ERROR", "Invalid Form!");
		}
		try {	ActivityWorkPackage activityWorkPackage = null;
		        ProductBuild productBuild = null;
		        ProductVersionListMaster productVersion = null;
		        ProductMaster productMaster = null;
		        TestFactory testFactory = null;
		        Customer customer = null;
		        String remarks = "";
					            
		        requestFromUI = jsonActivityTask.getActivityTask();	
		        UserList user = (UserList) request.getSession().getAttribute("USER");
		        requestFromUI.getActivity().setModifiedDate(new Date());	
		        requestFromUI.setModifiedDate(new Date());
		        UserList assigneeUser=userListService.getUserListById(jsonActivityTask.getAssigneeId());
				UserList reviewerUser=userListService.getUserListById(jsonActivityTask.getReviewerId());
				requestFromUI.setAssignee(assigneeUser);			
				requestFromUI.setReviewer(reviewerUser);	
				requestFromUI.setModifiedBy(user);
				
				if("lifeCycleStageId".equalsIgnoreCase(jsonActivityTask.getModifiedField()) || "activityTaskTypeId".equalsIgnoreCase(jsonActivityTask.getModifiedField())){
		        	 boolean isEligibleToChange = workflowEventService.checkInstanceEligibiltyToChangeWorkflowMapping(IDPAConstants.ENTITY_TASK_TYPE, jsonActivityTask.getActivityTaskTypeId(), jsonActivityTask.getActivityTaskId());
		        	 if(!isEligibleToChange){
		        		 jTableResponse = new JTableResponse("ERROR", "Instance under gone some operation(s), cannot change "+jsonActivityTask.getModifiedField()+" at this point");
		        		 return jTableResponse;
		        	 }
		         }

				if(requestFromUI.getPlannedStartDate() == null && requestFromUI.getPlannedEndDate() != null){
					return jTableResponse = new JTableResponse("ERROR", "Please provide planned start date");
				}else if (ActivityDateValidation(requestFromUI.getPlannedStartDate(), requestFromUI.getPlannedEndDate())) {
					return jTableResponse = new JTableResponse("ERROR", "Planned start date should be less than or equal to planned end date");
				}
				
				if(requestFromUI.getActualStartDate() == null && requestFromUI.getActualEndDate() != null){
					return jTableResponse = new JTableResponse("ERROR", "Please provide actual start date");
				}else if (ActivityDateValidation(requestFromUI.getActualStartDate(), requestFromUI.getActualEndDate())) {
					return jTableResponse = new JTableResponse("ERROR", "Actual start date should be less than or equal to actual end date");
				}
				
				Activity activity = null;
				activity = activityService.getActivityById(jsonActivityTask.getActivityId(),1);
				activityWorkPackage = activity.getActivityWorkPackage();
	            productBuild = activityWorkPackage.getProductBuild();
	            productVersion = productBuild.getProductVersion();
	            productMaster = productVersion.getProductMaster();
	            testFactory = productMaster.getTestFactory();
	            customer = productMaster.getCustomer();
				
				if (ActivityDateValidation(requestFromUI.getPlannedStartDate(),
						requestFromUI.getPlannedEndDate())) {
					return jTableResponse = new JTableResponse("INFORMATION",
							"Planned End date should be less than or equal to Planned Start date");
				}
				Date actStDate = activity.getPlannedStartDate();
				Date actEndDate = activity.getPlannedEndDate();
				Date actTaskStDate = requestFromUI.getPlannedStartDate();
				Date actTaskEndDate = requestFromUI.getPlannedEndDate();
				
				String errmsg="Please select  End Date  between  activity  Date "+actStDate.toString().replaceAll("00:00:00.0", "")+"   and  "+actEndDate.toString().replaceAll("00:00:00.0", "");
				if(actStDate!=null ||actEndDate!=null)
				{
					Integer dateresult = DateUtility.ActivityDateValidation(actTaskStDate, actTaskEndDate,
						actStDate, actEndDate);
						if (dateresult == 1) {
							return jTableResponse = new JTableResponse("INFORMATION",errmsg);
						} else if (dateresult == 2) {
							return jTableResponse = new JTableResponse("INFORMATION",errmsg);
						} else
							jTableResponse = new JTableResponse("OK");
					}
				
				log.info("process.activitytask.add jsonActivityTask.getAssigneeId()"+requestFromUI.getAssignee().getUserId());
				if(jsonActivityTask.getPlannedTaskSize() == null) {
		        	requestFromUI.setPlannedTaskSize(0);
				}else {
					requestFromUI.setPlannedTaskSize(jsonActivityTask.getPlannedTaskSize());
				}
				
				if(jsonActivityTask.getBaselineTaskSize() == null) {
					requestFromUI.setBaselineTaskSize(requestFromUI.getPlannedTaskSize());
				}else{
					requestFromUI.setBaselineTaskSize(jsonActivityTask.getBaselineTaskSize());
				}
				
				if(requestFromUI.getBaselineEffort() == null){
					requestFromUI.setPlannedEffort(requestFromUI.getPlannedEffort());
				}
				
				if(jsonActivityTask.getActualTaskSize() == null) {
					requestFromUI.setActualTaskSize(0);
				}else {
					requestFromUI.setActualTaskSize(jsonActivityTask.getActualTaskSize());
				}
				if(requestFromUI.getActivityTaskType() != null && requestFromUI.getActivityTaskType().getActivityTaskTypeId() != null){
					ActivityTaskType activityTaskType = activityTaskService.getActivityTaskTypeById(requestFromUI.getActivityTaskType().getActivityTaskTypeId());
					if(activityTaskType != null){
						Float weightage = activityTaskType.getActivityTaskTypeWeightage();
						Float plannedUnit = requestFromUI.getBaselineTaskSize() * weightage;
						Float actualUnit = requestFromUI.getActualTaskSize() * weightage;
						requestFromUI.setPlannedUnit(plannedUnit);
						requestFromUI.setActualUnit(actualUnit);
					}
				}
				
				 Integer totalTaskEfforts= workflowEventService.getTotalEffortsByEntityInstanceIdAndEntityType(requestFromUI.getActivityTaskId(),IDPAConstants.ENTITY_TASK_TYPE);
				 requestFromUI.setTotalEffort(totalTaskEfforts);
				
				if(requestFromUI.getActivityTaskName() == ""){
					jTableResponse = new JTableResponse("INFORMATION","ActivityTask Name should not be empty!");
				}
				else if(requestFromUI.getActivityTaskName().matches(".*"+specialChars+".*")){
					jTableResponse = new JTableResponse("INFORMATION","ActivityTask Name consists of Invalid special Characters!");
				}
				else{
				requestFromDB = activityTaskService.getActivityTaskById(requestFromUI.getActivityTaskId(), 1);
				boolean errorFlagActivityTask = activityTaskService.getActivityTaskById(jsonActivityTask.getActivityId(), jsonActivityTask.getActivityTaskName());
				//Audit History - Starts				
				String modifiedField = "";
				String modifiedFieldTitle = "";
				String oldFieldID = "";
				String oldFieldValue = "";
				String modifiedFieldID = "";
				String modifiedFieldValue = "";
				
				if(jsonActivityTask.getModifiedField() != null && !jsonActivityTask.getModifiedField().equalsIgnoreCase("")){
					modifiedField = jsonActivityTask.getModifiedField();
					if(jsonActivityTask.getModifiedFieldTitle() != null && !jsonActivityTask.getModifiedFieldTitle().equalsIgnoreCase(""))
						modifiedFieldTitle = jsonActivityTask.getModifiedFieldTitle();
					if(jsonActivityTask.getOldFieldID() != null && !jsonActivityTask.getOldFieldID().equalsIgnoreCase(""))
						oldFieldID = jsonActivityTask.getOldFieldID();
					if(jsonActivityTask.getOldFieldValue() != null && !jsonActivityTask.getOldFieldValue().equalsIgnoreCase(""))
						oldFieldValue = jsonActivityTask.getOldFieldValue();
					if(jsonActivityTask.getModifiedFieldID() != null && !jsonActivityTask.getModifiedFieldID().equalsIgnoreCase(""))
						modifiedFieldID = jsonActivityTask.getModifiedFieldID();					
					if(jsonActivityTask.getModifiedFieldValue() != null && !jsonActivityTask.getModifiedFieldValue().equalsIgnoreCase(""))
						modifiedFieldValue = jsonActivityTask.getModifiedFieldValue();					
				}	
				//Audit History - Ends
				if(requestFromDB.getActivityTaskName() != null && requestFromUI.getActivityTaskName() != null){
					if(requestFromDB.getActivityTaskName().trim().equals(requestFromUI.getActivityTaskName().trim())){
				activityTaskService.updateActivityTask(requestFromUI);
				
				/*Mongodb parent status*/
				if(jsonActivityTask.getModifiedField().equalsIgnoreCase(MongodbConstants.MODIFIED_FIELD_STATUS)){
					mongoDBService.updateParentStatusInChildColletions(IDPAConstants.ENTITY_TASK,jsonActivityTask.getActivityTaskId(),jsonActivityTask.getIsActive());
				}

				if(requestFromUI!=null && requestFromUI.getActivityTaskId()!=null){
					mongoDBService.addActivityTaskToMongoDB(requestFromUI.getActivityTaskId());
					remarks = "TestFactory: "+testFactory.getTestFactoryName()+", Product: "+productMaster.getProductName()+", ActivityWorkpackage: "+activityWorkPackage.getActivityWorkPackageName()+", Activity: "+activity.getActivityName()+", ActivityTask: "+requestFromUI.getActivityTaskName();
					//Entity Audition History //Update					
					eventsService.addEntityChangedEvent(IDPAConstants.ENTITY_ACTIVITY_TASK,  requestFromUI.getActivityTaskId(), requestFromUI.getActivityTaskName(),
							jsonActivityTask.getModifiedField(), jsonActivityTask.getModifiedFieldTitle(),
							jsonActivityTask.getOldFieldValue(), jsonActivityTask.getModifiedFieldValue(), user, remarks);
				}
								
				if(activity!=null && activity.getActivityId()!=null){
					mongoDBService.addActivitytoMongoDB(activity.getActivityId());
				}
				
				if(activity != null){
					activityTaskService.updateActivityBasedOnTasks(activity.getActivityId());
					
					int activityStatusCategoryId = activityTaskService.getActivityStatusCategoryIdByTaskStatus(activity.getActivityId());
					if(activityStatusCategoryId <= 0){
						activityStatusCategoryId = 1;
					}
					StatusCategory statusCategory = new StatusCategory();
					statusCategory.setStatusCategoryId(activityStatusCategoryId);
					activity.setStatusCategory(statusCategory);
					activityService.updateActivity(activity);
					if(activity!=null){
						mongoDBService.addActivitytoMongoDB(activity.getActivityId());
					}
					
					int activityWorkpackageStatusCategoryId = activityService.getActivityWorkpackageStatusCategoryIdByActivityStatusCategory(activity.getActivityWorkPackage().getActivityWorkPackageId());
					if(activityWorkpackageStatusCategoryId <= 0){
						activityWorkpackageStatusCategoryId = 1;
					}
					statusCategory.setStatusCategoryId(activityWorkpackageStatusCategoryId);
					activityWorkPackage = activityWorkPackageService.getActivityWorkPackageById(activity.getActivityWorkPackage().getActivityWorkPackageId(), 1);
					activityWorkPackage.setStatusCategory(statusCategory);
					activityWorkPackageService.updateActivityWorkPackage(activityWorkPackage);
					if(activityWorkPackage!=null){
						mongoDBService.addActivityWorkPackagetoMongoDB(activityWorkPackage.getActivityWorkPackageId());
					}
				}
				
				//added for mongoDB insertion			
	            log.info("process.activitytask.add Mongo DB insertion");				      	            				      
	            Integer productId = productMaster.getProductId();
				
				log.info("process.activitytask.add Mongo DB insertion done");	
				List<JsonActivityTask> jsonActivityTaskList =new ArrayList<JsonActivityTask>();
				jsonActivityTaskList.add(jsonActivityTask);
				jTableResponse = new JTableResponse("OK",jsonActivityTaskList ,1);
				 
				WorkflowStatus workflowStatus=workflowStatusService.getWorkflowStatusById(requestFromUI.getStatus().getWorkflowStatusId());
				requestFromUI.getStatus().setWorkflowStatusName(workflowStatus.getWorkflowStatusName());
				notificationService.processNotification(request,productId, IDPAConstants.NOTIFICATION_ACTIVITY_TASK_CREATION, requestFromUI,subject);
				}
				else{
					if (errorFlagActivityTask) {
						jTableResponse = new JTableResponse("INFORMATION","ActivityTask Name already Exists For Same Activity!");
					} else {
						activityTaskService.updateActivityTask(requestFromUI);						
						if(requestFromUI!=null && requestFromUI.getActivityTaskId()!=null){
							mongoDBService.addActivityTaskToMongoDB(requestFromUI.getActivityTaskId());
							//Entity Audition History //Update	
							eventsService.addEntityChangedEvent(IDPAConstants.ENTITY_ACTIVITY_TASK, requestFromUI.getActivityTaskId(), requestFromUI.getActivityTaskName(),
									jsonActivityTask.getModifiedField(), jsonActivityTask.getModifiedFieldTitle(),
									jsonActivityTask.getOldFieldValue(), jsonActivityTask.getModifiedFieldValue(), user, remarks);
						}						
						List<JsonActivityTask> jsonActivityTaskList =new ArrayList<JsonActivityTask>();
						jsonActivityTaskList.add(jsonActivityTask);
						jTableResponse = new JTableResponse("OK",jsonActivityTaskList ,1);
					}
				}
			}
		  }
		} catch (Exception e) {
			jTableResponse = new JTableResponse("ERROR",
					"Unable to update the Activity Task!");
			log.error("JSON ERROR", e);
		}
		return jTableResponse;
	}
	
	@RequestMapping(value="process.activitytask.delete",method=RequestMethod.POST ,produces="application/json")
	public @ResponseBody JTableResponse deleteActivityTask(HttpServletRequest request,@RequestParam int activityTaskId) {
		log.debug("inside process.activitytask.delete");
		JTableResponse jTableResponse = null;
		String referencedTableName = "activity_task";
		String referencedColumnName = "activityTaskId";
		String message = "";
		String subject = "Activity Task Deletion";
		try{
			message = activityTaskService.deleteActivityTask(activityTaskId, referencedTableName, referencedColumnName);
			if(message.equals("OK")){
				mongoDBService.deleteActivityTaskFromMongoDb(activityTaskId);
				ActivityTask task=activityTaskService.getActivityTaskById(activityTaskId,0);
				Integer productId =task.getActivity().getActivityWorkPackage().getProductBuild().getProductVersion().getProductMaster().getProductId();
				notificationService.processNotification(request,productId, IDPAConstants.NOTIFICATION_ACTIVITY_TASK_CREATION, task,subject);
				jTableResponse = new JTableResponse(message);
				}else{
				jTableResponse = new JTableResponse("INFORMATION",message);
				}
		}catch(Exception e){
			jTableResponse = new JTableResponse("ERROR","Error Deleting Records !");
			log.error("JSON ERROR", e);
		}
		return jTableResponse;
	}
	
	@RequestMapping(value = "activity.type.listing", method = RequestMethod.POST, produces = "application/json")
	public @ResponseBody JTableResponse listActivityTypes(@RequestParam Integer testFactoryId, @RequestParam Integer productId, @RequestParam int jtStartIndex, @RequestParam int jtPageSize) {
		log.info("inside activity.type.listing");
		JTableResponse jTableResponse;
		try {
			List<JsonActivityMaster> activityMaster = null;
			activityMaster = activityTypeService.listActivityTypes(testFactoryId, productId, jtStartIndex, jtPageSize,0, false);
			jTableResponse = new JTableResponse("OK", activityMaster);
			log.info("inside process fetching activity type records ");
		} catch (Exception e) {
			jTableResponse = new JTableResponse("ERROR",
					"Error fetching records!");
			log.error("JSON ERROR", e);
		}
		return jTableResponse;
	}
	
	@RequestMapping(value="list.environment.combinations.by.activity",method=RequestMethod.POST ,produces="application/json")
	public @ResponseBody JTableResponse getChangeRequestsMappedToActivity(@RequestParam Integer activityId) {
		log.info("list.environment.combinations.by.activity");
		JTableResponse jTableResponse;
		try {		
			if (activityId == null || ("null").equals(activityId)) {		
	            jTableResponse = new JTableResponse("OK", null,0);
				return jTableResponse;
			}
			List<EnvironmentCombination> environmentCombinationFromDB = environmentService.getEnvironmentsCombinationsMappedToActivity(activityId,0);	
			if (environmentCombinationFromDB == null) {				
				log.info("No mappings available for activityId: "+activityId);			
				jTableResponse = new JTableResponse("OK", null,0);
				return jTableResponse;
			}
			List<JsonEnvironmentCombination> jsonEnvironmentCombinationList = new ArrayList<JsonEnvironmentCombination>();
			for (EnvironmentCombination ec : environmentCombinationFromDB) {
				jsonEnvironmentCombinationList.add(new JsonEnvironmentCombination(ec));
			}		
			jTableResponse = new JTableResponse("OK", jsonEnvironmentCombinationList,jsonEnvironmentCombinationList.size());
			environmentCombinationFromDB = null;  
		} catch (Exception e) {
	            jTableResponse = new JTableResponse("ERROR","Error fetching records!");
	            log.error("JSON ERROR", e);
	        }
	    return jTableResponse;
	}
	

	
	@RequestMapping(value = "activity.type.add", method = RequestMethod.POST, produces = "application/json")
	public @ResponseBody JTableSingleResponse addActivityMaster(@ModelAttribute JsonActivityMaster jsonActivityMaster,BindingResult result, HttpServletRequest request) {
		JTableSingleResponse jTableSingleResponse;
		try {
			log.info("In activity.type.add");
			ActivityMaster activityMaster = jsonActivityMaster.getActivityMaster();
			boolean isAlreadyExsist = activityTypeService.isActivityMasterAvailable(jsonActivityMaster.getActivityMasterName(), null, jsonActivityMaster.getTestFactoryId(), jsonActivityMaster.getProductId());
			if(isAlreadyExsist){
				jTableSingleResponse = new JTableSingleResponse("ERROR", jsonActivityMaster.getActivityMasterName()+" - Activity type already available for this product / testfactory level");
			}else{
				activityTypeService.addActivityMaster(activityMaster);
				jTableSingleResponse = new JTableSingleResponse("OK",new JsonActivityMaster(activityMaster));
			}
		} catch (Exception e) {
			jTableSingleResponse = new JTableSingleResponse("ERROR","Error adding record!");
			log.error("JSON ERROR", e);
		}
		return jTableSingleResponse;
	}
	
	@RequestMapping(value = "activity.type.update", method = RequestMethod.POST, produces = "application/json")
	public @ResponseBody JTableSingleResponse updateActivityMaster(HttpServletRequest request,@ModelAttribute JsonActivityMaster jsonActivityMaster,BindingResult result) {
		JTableSingleResponse jTableSingleResponse;
		try {
			log.info("In activity.type.update");
			ActivityMaster activityMaster = jsonActivityMaster.getActivityMaster();
			boolean isAlreadyExsist = activityTypeService.isActivityMasterAvailable(jsonActivityMaster.getActivityMasterName(), jsonActivityMaster.getActivityMasterId(), jsonActivityMaster.getTestFactoryId(), jsonActivityMaster.getProductId());
			if(isAlreadyExsist){
				jTableSingleResponse = new JTableSingleResponse("ERROR", jsonActivityMaster.getActivityMasterName()+" - Activity type already available for this product / testfactory level");
			}else{
				activityTypeService.updateActivityMaster(activityMaster);
				jTableSingleResponse = new JTableSingleResponse("OK",new JsonActivityMaster(activityMaster));
			}
		} catch (Exception e) {
			jTableSingleResponse = new JTableSingleResponse("ERROR","Error adding record!");
			log.error("JSON ERROR", e);
		}
		return jTableSingleResponse;
	}
	
	@RequestMapping(value = "activitytask.list.for.review", method = RequestMethod.POST, produces = "application/json")
	public @ResponseBody JTableResponse getActivityTaskForReviewOrAssignation(HttpServletRequest request,@RequestParam Integer activityId, @RequestParam Integer productId) {
		log.info("inside activitytask.list.for.review");
		JTableResponse jTableResponse;
	
		try {
			List<JsonActivityTask> jsonActivityTask = null;			
			int jtStartIndex=0;
			int jtPageSize=20;			
			UserList user = (UserList)request.getSession().getAttribute("USER");
			Map<String, String> searchStrings = new HashMap<String, String>();			
			Integer initalizationLevel =1;
			String searchassigneeId = request.getParameter("searchassigneeId");
			String searchpriority = request.getParameter("searchpriority");
			String searchstatusId = request.getParameter("searchstatusId");
			
			searchStrings.put("searchassigneeId", searchassigneeId);
            searchStrings.put("searchpriority", searchpriority);
            searchStrings.put("searchstatusId", searchstatusId);
			jsonActivityTask = activityTaskService.listActivityTaskByActivityIdAndUserId(request,productId, activityId,user,jtStartIndex,jtPageSize,searchStrings,initalizationLevel);
		    jTableResponse = new JTableResponse("OK", jsonActivityTask,jsonActivityTask.size());
			log.info("Fetching activity task records ");
		} catch (Exception e) {
			jTableResponse = new JTableResponse("ERROR",
					"Error fetching records!");
			log.error("JSON ERROR", e);
		}
		return jTableResponse;
	}
	
		@RequestMapping(value="assigned.activitytask.selfreview.update",method=RequestMethod.POST ,produces="application/json")
		public @ResponseBody JTableResponse selfReviewStatusUpdateOfTask(HttpServletRequest req,@ModelAttribute JsonActivityTask jsonActivityTask, BindingResult result) {
				JTableResponse jTableResponse=null;
				if(result.hasErrors()){
					jTableResponse = new JTableResponse("ERROR","Invalid Form!"); 
				}			
				try {		
				        ActivityWorkPackage activityWorkPackage = null;
			            ProductBuild productBuild = null;
			            ProductVersionListMaster productVersion = null;
			            ProductMaster productMaster = null;
			            TestFactory testFactory = null;
			            Customer customer = null;
			            
						UserList user = (UserList)req.getSession().getAttribute("USER");
						ActivityTask activityTask = jsonActivityTask.getActivityTask();						
						Activity activity = null;	
						activity = activityService.getActivityById(jsonActivityTask.getActivityId(),1);	
			            log.info("process.activitytask.add Mongo DB insertion");				      
			            activityWorkPackage = activity.getActivityWorkPackage();
			            productBuild = activityWorkPackage.getProductBuild();
			            productVersion = productBuild.getProductVersion();
			            productMaster = productVersion.getProductMaster();
			            testFactory = productMaster.getTestFactory();
			            customer = productMaster.getCustomer();			
			            
			            Integer productId = productMaster.getProductId();
			            String productName = productMaster.getProductName();			            
			            Integer testFactoryId = testFactory.getTestFactoryId();
			            String testFactoryName = testFactory.getTestFactoryName();            
			            Integer customerId = customer.getCustomerId();
			            String customerName = customer.getCustomerName();	            
			            Integer productVersionId=productVersion.getProductVersionListId();
			            String productVersionName=productVersion.getProductVersionName();
						Integer productBuildId=productBuild.getProductBuildId();
						String productBuildName=productBuild.getBuildname();
						Integer awpId=activityWorkPackage.getActivityWorkPackageId();
						String awpName=activityWorkPackage.getActivityWorkPackageName();
									    		
						mongoDBService.addActivityTaskToMongoDB(activityTask,productId,productName,productVersionId,productVersionName,productBuildId,productBuildName,awpId,awpName, testFactoryId, testFactoryName, customerId, customerName);
						log.info("process.activitytask.add Mongo DB insertion done");	
						List<JsonActivityTask> jsonActivityTaskList =new ArrayList<JsonActivityTask>();
						jsonActivityTaskList.add(jsonActivityTask);
						 jTableResponse = new JTableResponse("OK",jsonActivityTaskList ,1);
			        		
			        } catch (Exception e) {
			            jTableResponse = new JTableResponse("ERROR","Error updating or approving time sheet entry");
			            log.error("JSON ERROR updating or approving time sheet entry", e);
			        }       
		        return jTableResponse;
		    }
		
		@RequestMapping(value="activitytask.peerReview.update",method=RequestMethod.POST ,produces="application/json")
		public @ResponseBody JTableResponse peerReviewStatusUpdateOfTask(HttpServletRequest req,@ModelAttribute JsonActivityTask jsonActivityTask, BindingResult result) {
				JTableResponse jTableResponse=null;
			if(result.hasErrors()){
				jTableResponse = new JTableResponse("ERROR","Invalid Form!"); 
			}			
			try {	
			        ActivityWorkPackage activityWorkPackage = null;
		            ProductBuild productBuild = null;
		            ProductVersionListMaster productVersion = null;
		            ProductMaster productMaster = null;
		            TestFactory testFactory = null;
		            Customer customer = null;		            
		            
					UserList user = (UserList)req.getSession().getAttribute("USER");
					ActivityTask activityTask = jsonActivityTask.getActivityTask();						
					Activity activity = null;	
					activity = activityService.getActivityById(jsonActivityTask.getActivityId(),1);					      
		           
		            log.info("process.activitytask.add Mongo DB insertion");				      
		            activityWorkPackage = activity.getActivityWorkPackage();
		            productBuild = activityWorkPackage.getProductBuild();
		            productVersion = productBuild.getProductVersion();
		            productMaster = productVersion.getProductMaster();
		            testFactory = productMaster.getTestFactory();
		            customer = productMaster.getCustomer();			
		            
		            Integer productId = productMaster.getProductId();
		            String productName = productMaster.getProductName();			            
		            Integer testFactoryId = testFactory.getTestFactoryId();
		            String testFactoryName = testFactory.getTestFactoryName();            
		            Integer customerId = customer.getCustomerId();
		            String customerName = customer.getCustomerName();	            
		            Integer productVersionId=productVersion.getProductVersionListId();
		            String productVersionName=productVersion.getProductVersionName();
					Integer productBuildId=productBuild.getProductBuildId();
					String productBuildName=productBuild.getBuildname();
					Integer awpId=activityWorkPackage.getActivityWorkPackageId();
					String awpName=activityWorkPackage.getActivityWorkPackageName();
								    		
					mongoDBService.addActivityTaskToMongoDB(activityTask,productId,productName,productVersionId,productVersionName,productBuildId,productBuildName,awpId,awpName, testFactoryId, testFactoryName, customerId, customerName);
					log.info("process.activitytask.add Mongo DB insertion done");	
					
					List<JsonActivityTask> jsonActivityTaskList =new ArrayList<JsonActivityTask>();
					jsonActivityTaskList.add(jsonActivityTask);
					 jTableResponse = new JTableResponse("OK",jsonActivityTaskList ,1);		
					 
		        } catch (Exception e) {
		            jTableResponse = new JTableResponse("ERROR","Error updating or approving time sheet entry");
		            log.error("JSON ERROR updating or approving time sheet entry", e);
		        }       
	        return jTableResponse;
	    }
		
		@RequestMapping(value="activitytask.pqaReview.update",method=RequestMethod.POST ,produces="application/json")
		public @ResponseBody JTableResponse pqaReviewStatusUpdateOfTask(HttpServletRequest req,@ModelAttribute JsonActivityTask jsonActivityTask, BindingResult result) {
				JTableResponse jTableResponse=null;
			if(result.hasErrors()){
				jTableResponse = new JTableResponse("ERROR","Invalid Form!"); 
			}			
			try {		
				    ActivityWorkPackage activityWorkPackage = null;
		            ProductBuild productBuild = null;
		            ProductVersionListMaster productVersion = null;
		            ProductMaster productMaster = null;
		            TestFactory testFactory = null;
		            Customer customer = null;	
		            
					UserList user = (UserList)req.getSession().getAttribute("USER");
					ActivityTask activityTask = jsonActivityTask.getActivityTask();						
					Activity activity = null;	
					activity = activityService.getActivityById(jsonActivityTask.getActivityId(),1);	
				
		            log.info("process.activitytask.add Mongo DB insertion");				      
		            activityWorkPackage = activity.getActivityWorkPackage();
		            productBuild = activityWorkPackage.getProductBuild();
		            productVersion = productBuild.getProductVersion();
		            productMaster = productVersion.getProductMaster();
		            testFactory = productMaster.getTestFactory();
		            customer = productMaster.getCustomer();			
		            
		            Integer productId = productMaster.getProductId();
		            String productName = productMaster.getProductName();			            
		            Integer testFactoryId = testFactory.getTestFactoryId();
		            String testFactoryName = testFactory.getTestFactoryName();            
		            Integer customerId = customer.getCustomerId();
		            String customerName = customer.getCustomerName();	            
		            Integer productVersionId=productVersion.getProductVersionListId();
		            String productVersionName=productVersion.getProductVersionName();
					Integer productBuildId=productBuild.getProductBuildId();
					String productBuildName=productBuild.getBuildname();
					Integer awpId=activityWorkPackage.getActivityWorkPackageId();
					String awpName=activityWorkPackage.getActivityWorkPackageName();
								    		
					mongoDBService.addActivityTaskToMongoDB(activityTask,productId,productName,productVersionId,productVersionName,productBuildId,productBuildName,awpId,awpName, testFactoryId, testFactoryName, customerId, customerName);
					log.info("process.activitytask.add Mongo DB insertion done");	
					
					List<JsonActivityTask> jsonActivityTaskList =new ArrayList<JsonActivityTask>();
					jsonActivityTaskList.add(jsonActivityTask);
					 jTableResponse = new JTableResponse("OK",jsonActivityTaskList ,1);	
					 
		        } catch (Exception e) {
		            jTableResponse = new JTableResponse("ERROR","Error updating or approving time sheet entry");
		            log.error("JSON ERROR updating or approving time sheet entry", e);
		        }       
	        return jTableResponse;
	    }
		
		@RequestMapping(value="activitywp.activity.bulk.update",method=RequestMethod.POST ,produces="application/json")
		public @ResponseBody JTableSingleResponse updateActivityBulk(
	    		@RequestParam Integer categoryId, @RequestParam Integer assigneeId, @RequestParam String activitywpListsFromUI, @RequestParam String plannedEndDate,@RequestParam Integer priorityId,@RequestParam Integer reviewerId) {
			
			JTableSingleResponse jTableSingleResponse = null;
			
			try {
				String[] activityBulkLists = activitywpListsFromUI.split(",");
				activityWorkPackageService.updateActivity(activityBulkLists,categoryId,assigneeId,plannedEndDate,priorityId,reviewerId);
	
				jTableSingleResponse = new JTableSingleResponse("OK");
		    } 
			catch (Exception e) {
	            jTableSingleResponse = new JTableSingleResponse("ERROR","Unable to perform Activity bulk allocation!");
	            log.error("JSON ERROR", e);	            
	        }
		    return jTableSingleResponse;
	    }
		

		@RequestMapping(value="activities.import", method=RequestMethod.POST ,produces="text/plain" )
		public @ResponseBody JTableResponse activitiesDataimport(HttpServletRequest request,@RequestParam Integer testFactoryId,@RequestParam Integer productId) {
			log.debug("activities.import");
			JTableResponse jTableResponse;
			try {
				log.info("testFactoryId input:" + testFactoryId);
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
				isImportComplete = excelTestDataIntegrator.importActivities(request,fileName, testFactoryId, productId, is);
				fileName = isImportComplete.split(";")[1];
				isImportComplete = isImportComplete.split(";")[0];
				
				if(isImportComplete != null){
					log.info("Import Activities Completed.");
					jTableResponse = new JTableResponse("Ok","Import Activities Completed."+" "+isImportComplete,fileName);
				} else{
					log.info("Import completed");
					jTableResponse = new JTableResponse("Ok","Import completed"+" "+isImportComplete,fileName);
				}
			} catch (Exception e) {
				jTableResponse = new JTableResponse("Error importing activitiesData");
				log.error("JSON ERROR importing activitiesData", e);
			}
			return jTableResponse;
		}
		
		@RequestMapping(value="activitytask.referback.comments.add",method=RequestMethod.POST ,produces="application/json")
		public @ResponseBody JTableSingleResponse updateActivityTaskBulkComments(
				@RequestParam String activitywpListsFromUI,
				@RequestParam String resultComments, 
				@RequestParam String resultEntityValue, 
				@RequestParam String resultRaisedByValue,
				@RequestParam String resultCommentsTypeValue,
				@RequestParam String resultRaisedDateValue)  {
              
			JTableSingleResponse jTableSingleResponse = null;
			
			try {
				 log.error("activitytask.referback.comments.add");	    
				String[] activityTaskBulkLists = activitywpListsFromUI.split(",");
				activityWorkPackageService.updateActivityTaskBulkComments(resultComments,resultEntityValue,activityTaskBulkLists,resultRaisedByValue,resultCommentsTypeValue,resultRaisedDateValue);
	
				jTableSingleResponse = new JTableSingleResponse("OK");
		    } 
			catch (Exception e) {
	            jTableSingleResponse = new JTableSingleResponse("ERROR","Unable to perform Activity bulk allocation!");
	            log.error("JSON ERROR", e);	            
	        }
		    return jTableSingleResponse;
	    }
		
		@RequestMapping(value = "activitytask.effort.tracker.list", method = RequestMethod.POST, produces = "application/json")
		public @ResponseBody
		JTableResponse listCommentsTrackerByActivityTaskId(	@RequestParam Integer taskId) {
			log.debug("inside activitytask.referback.comments.list");
			JTableResponse jTableResponse;
			
			try {
			
				List<JsonWorkflowEvent> jsonWorkflowEntityEffortTracker = new ArrayList<JsonWorkflowEvent>();
				List<WorkflowEvent> listWorkflowEntityEffortTracker = workflowEventService.listWorkflowEventByEntityTypeAndEnityInstanceId(IDPAConstants.ENTITY_TASK_TYPE,taskId,1);
				
				
				if(listWorkflowEntityEffortTracker != null && listWorkflowEntityEffortTracker.size()>0){
					for (WorkflowEvent workflowEntityEffortTracker : listWorkflowEntityEffortTracker) {
						JsonWorkflowEvent jsonEntityEffortTracker = new JsonWorkflowEvent(workflowEntityEffortTracker);
						jsonWorkflowEntityEffortTracker.add(jsonEntityEffortTracker);
					}
				}
				jTableResponse = new JTableResponse("OK", jsonWorkflowEntityEffortTracker);
				log.debug("inside process fetching jsonCommentsTrackerList records ");
			} catch (Exception e) {
				jTableResponse = new JTableResponse("ERROR","Error fetching CommentsTrackerrecords!");
				log.error("JSON ERROR", e);
			}
			return jTableResponse;
		}
	
		
		@RequestMapping(value="activitytask.effort.tracker.add",method=RequestMethod.POST ,produces="application/json")
		public @ResponseBody JTableSingleResponse updateActivityTaskEffortTracker(	HttpServletRequest request,					
				@RequestParam Integer taskId, 
				@RequestParam String resultEntity,				
				@RequestParam String resultPrimaryStatus,
				@RequestParam String resultsecondaryStatus,
				@RequestParam Integer resultEffort,	
				@RequestParam String resultComments,				
				@RequestParam String sourceStatus,@RequestParam String approveAllTaskIds)  

		{
			ActivityTask activityTask=null;
			Activity activity =null;
			ActivityWorkPackage activityWorkPackage =null;
			//ActivityStatus activityStatus =null;
			StatusCategory statusCategory;
			Integer taskWiseEffort=0;
			JTableSingleResponse jTableSingleResponse = null;
			try {
				
				String builkTaskIds[]=approveAllTaskIds.split(",");
				log.debug("activitytask.effort.tracker.add");
				if(builkTaskIds.length >0){
					for(String approvalId:builkTaskIds){
						taskId=	Integer.parseInt(approvalId);
						activityTask = activityTaskDAO.getActivityTaskById(taskId, 1);
						activity = activityService.getActivityById(activityTask.getActivity().getActivityId(),1);
						//activityStatus = activityStatusService.getStatusNameByDimensionId(resultPrimaryStatus,activity.getStatus().getDimension().getDimensionId());	
						UserList user = (UserList) request.getSession().getAttribute("USER");
						String resultModifiedBy=user.getLoginId();
						log.info("resultPrimaryStatus"+resultPrimaryStatus);
						if(resultEffort != null) {
							taskWiseEffort=resultEffort/builkTaskIds.length;
						}
						activityTaskService.updateActivityTaskEffortTracker(taskId,resultEntity,resultPrimaryStatus,resultsecondaryStatus,taskWiseEffort,resultModifiedBy,sourceStatus,resultComments,IDPAConstants.ENTITY_TASK_TYPE);
					}
				}
				
				if(activity != null){
					int activityStatusCategoryId = activityTaskService.getActivityStatusCategoryIdByTaskStatus(activity.getActivityId());
					if(activityStatusCategoryId <= 0){
						activityStatusCategoryId = 1;
					}
					statusCategory = new StatusCategory();
					statusCategory.setStatusCategoryId(activityStatusCategoryId);
					activity.setStatusCategory(statusCategory);
					activityService.updateActivity(activity);
					if(activity!=null){
						mongoDBService.addActivitytoMongoDB(activity.getActivityId());
					}
					
					int activityWorkpackageStatusCategoryId = activityService.getActivityWorkpackageStatusCategoryIdByActivityStatusCategory(activity.getActivityWorkPackage().getActivityWorkPackageId());
					if(activityWorkpackageStatusCategoryId <= 0){
						activityWorkpackageStatusCategoryId = 1;
					}
					statusCategory.setStatusCategoryId(activityWorkpackageStatusCategoryId);
					activityWorkPackage = activityWorkPackageService.getActivityWorkPackageById(activity.getActivityWorkPackage().getActivityWorkPackageId(), 1);
					activityWorkPackage.setStatusCategory(statusCategory);
					activityWorkPackageService.updateActivityWorkPackage(activityWorkPackage);
					if(activityWorkPackage!=null){
						mongoDBService.addActivityWorkPackagetoMongoDB(activityWorkPackage.getActivityWorkPackageId());
					}
				}
			
				jTableSingleResponse = new JTableSingleResponse("OK");
		    } 
			catch (Exception e) {
	            jTableSingleResponse = new JTableSingleResponse("ERROR","Unable to perform Activity effort Tracker!");
	            log.error("JSON ERROR", e);	            
	        }
		    return jTableSingleResponse;
	    }
		
		@RequestMapping(value = "activitytask.referback.comments.list", method = RequestMethod.POST, produces = "application/json")
		public @ResponseBody
		JTableResponse listCommentsTrackerBulkActivityTaskIds(@RequestParam String taskIds) {
			log.debug("inside activitytask.referback.comments.list");
			JTableResponse jTableResponse;
			
			try {
				
				List<JsonWorkflowEvent> temp = null;
                String[] activityTaskBulkLists = taskIds.split(",");
                List<JsonWorkflowEvent> jsonActivityEffortTrackerList = new ArrayList<JsonWorkflowEvent>();
                for(String Id:activityTaskBulkLists)
                {
                	Integer taskId=Integer.parseInt(Id);
                	log.debug("inside process fetching jsonCommentsTrackerList records ");
                	if(jsonActivityEffortTrackerList.size() == 0){
                		jsonActivityEffortTrackerList = temp;
                    }else{
                    	jsonActivityEffortTrackerList.addAll(temp);
                    }
                }
                jTableResponse = new JTableResponse("OK", jsonActivityEffortTrackerList);
			} catch (Exception e) {
				jTableResponse = new JTableResponse("ERROR","Error fetching CommentsTrackerrecords!");
				log.error("JSON ERROR", e);
			}
			return jTableResponse;
		}
		
		@RequestMapping(value = "product.activity.processSummary", method = RequestMethod.POST, produces = "application/json")
		public @ResponseBody JTableResponse productActivityProcessSummary(@RequestParam Integer productId) {
			log.debug("inside product.activity.processSummary");
			JTableResponse jTableResponse;
			
			try {
				ProductSummaryForActivityProcessDTO productDetails=null;
				productDetails=activityService.getProductActivitySummaryDetails(productId);
				
				List<JsonProductSummaryForActivityProcessDTO> jsonProductSummaryActivity=new ArrayList<JsonProductSummaryForActivityProcessDTO>();
               
				jsonProductSummaryActivity.add(new JsonProductSummaryForActivityProcessDTO(productDetails));
				
                jTableResponse = new JTableResponse("OK", jsonProductSummaryActivity);
			} catch (Exception e) {
				jTableResponse = new JTableResponse("ERROR","Error fetching CommentsTrackerrecords!");
				log.error("JSON ERROR", e);
			}
			return jTableResponse;
		}
		
		
		@RequestMapping(value = "productList.based.user.loged.in", method = RequestMethod.POST, produces = "application/json") 
		public @ResponseBody JTableResponse productListBasedOnUser(HttpServletRequest req) {
			log.debug("inside product.activity.processSummary");
			JTableResponse jTableResponse;
			
			try {
				    UserList user = new UserList();
					user = (UserList)req.getSession().getAttribute("USER");
					
					List<Integer> productIds=new ArrayList<Integer>();
					
				if(user.getUserRoleMaster().getUserRoleId() == IDPAConstants.ROLE_ID_ADMIN){
					List<ProductMaster>productList=new ArrayList<ProductMaster>();
					productList=productListService.listProduct();
					
					for(ProductMaster product:productList){
						productIds.add(product.getProductId());
					}
					
				}else{
					Set<ProductUserRole> users = user.getProductUserRoleSet();
					for(ProductUserRole userrole:users){
						if(userrole.getProduct().getStatus()==1 && userrole.getStatus()==1){
							productIds.add(userrole.getProduct().getProductId());
						}
					}	
				}
				
                jTableResponse = new JTableResponse("OK", productIds);
			} catch (Exception e) {
				jTableResponse = new JTableResponse("ERROR","Error fetching Prodcuts!");
				log.error("JSON ERROR", e);
			}
			return jTableResponse;
		}
		
		

		private Boolean ActivityDateValidation(Date PlannedStartDate, Date PlannedEndDate) {
			Boolean dateResult = false;
			if(PlannedStartDate != null && PlannedEndDate != null){
				dateResult = PlannedEndDate.before(PlannedStartDate);
			}
			return dateResult;

		}

		
		@RequestMapping(value="activityTasks.import", method=RequestMethod.POST ,produces="text/plain" )
		public @ResponseBody JTableResponse activityTasksDataimport(HttpServletRequest request,@RequestParam Integer activityWorkPackageId) {
			log.debug("Activitytasks.import");
			JTableResponse jTableResponse;
			try {
				log.info("activityWorkPackageId input:" + activityWorkPackageId);
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
				isImportComplete = excelTestDataIntegrator.importActivityTasks(request,fileName, activityWorkPackageId, is);
				
				if(isImportComplete != null){
					log.info("Import Activity tasks Completed.");
					jTableResponse = new JTableResponse("Ok","Import Activity tasks Completed."+"  "+isImportComplete);
				} else{
					log.info("Import completed");
					jTableResponse = new JTableResponse("Ok","Import completed."+" "+isImportComplete);
				}
			} catch (Exception e) {
				jTableResponse = new JTableResponse("Error Importing activityTasks" );
				log.error("JSON ERROR Importing activityTasks", e);
			}
			return jTableResponse;
		}
		@RequestMapping(value = "process.activity.folder.list", method = RequestMethod.POST, produces = "application/json")
		public @ResponseBody
		JTableResponse viewTotalActivityList(HttpServletRequest req) {
			log.info("inside process.activity.list");
			JTableResponse jTableResponse;
			try {
				List<JsonActivity> jsonActivity = null;
				Map<String, String> searchStrings = new HashMap<String, String>();
				UserList user = (UserList)req.getSession().getAttribute("USER");
			jsonActivity = activityService.listTotalActivitiesByUserId(user.getUserId(),user.getUserRoleMaster().getUserRoleId(),0,50);

				 jTableResponse = new JTableResponse("OK", jsonActivity);    
				log.info("inside process fetching activity records ");
			} catch (Exception e) {
				jTableResponse = new JTableResponse("ERROR",
						"Error fetching records!");
				log.error("JSON ERROR", e);
			}
			return jTableResponse;
		}
		
		@RequestMapping(value="my.Activity.status.summary",method=RequestMethod.POST ,produces="application/json")
	    public @ResponseBody JTableResponse getMyActiivtySummary(HttpServletRequest request,@RequestParam Integer activityId){	
			log.debug("status.summary");
			JTableResponse jTableResponse = null;
			try {
				Integer testFactoryId=0;
				UserList user = (UserList) request.getSession().getAttribute("USER");
				ActivitiesDTO activitiesDTO = activityService.getMyActivitySummary(activityId,user.getUserId());
				
				List<JsonActivity> jsonMyActivityList = new ArrayList<JsonActivity>();
				if (activitiesDTO == null ) {
					jTableResponse = new JTableResponse("OK", jsonMyActivityList, 0);
				} else {
					JsonActivity jsonActivity = new JsonActivity(activitiesDTO); 
					jsonMyActivityList.add(jsonActivity);
					Date modifiedAfterDate = commonService.getModifiedAfterDate((Date)request.getSession().getAttribute("userLoginTime"), (Date)request.getSession().getAttribute("userLogoutTime"));
					workflowStatusPolicyService.setInstanceIndicators(testFactoryId,jsonActivity.getProductId(),activitiesDTO.getActivityWorkPackage().getActivityWorkPackageId(), IDPAConstants.ENTITY_ACTIVITY_TYPE, jsonMyActivityList, IDPAConstants.ENTITY_ACTIVITY_ID,user, modifiedAfterDate, jsonActivity.getActivityMasterId(), jsonActivity.getActivityId(),IDPAConstants.ENABLE_PENDING_WITH_COLUMN);
					jTableResponse = new JTableResponse("OK", jsonMyActivityList, jsonMyActivityList.size());
					activitiesDTO = null;
				}
	        } catch (Exception e) {
	            jTableResponse = new JTableResponse("ERROR","Error fetching records!");
	            log.error("JSON ERROR", e);	            
	        }
	        return jTableResponse;
	    }	
		
		@RequestMapping(value = "process.activityworkpackage.clone", method = RequestMethod.POST, produces = "application/json")
		public @ResponseBody
		JTableSingleResponse cloneActivityWorkpackage(HttpServletRequest request, @RequestParam Integer srcProductBuildId, @RequestParam Integer destProductBuildId, @RequestParam Integer  workpackageId,@RequestParam String newWorkpackageName, @RequestParam String planStartDate, @RequestParam String planEndDate, @RequestParam String description) {
			JTableSingleResponse jTableSingleResponse=null;
		    ActivityWorkPackage activityWorkPackage =null;
		    ProductBuild productBuild = null;
		    List<Activity> activitieslist = null;
		    Integer productId = 0;
			try{
				activitieslist = activityService.listAllActivitiesByActivityWorkPackageId(workpackageId,0,1);
				UserList user = (UserList) request.getSession().getAttribute("USER");
				productBuild=productListService.getProductBuildById(destProductBuildId, 0);
				activityWorkPackage=activityWorkPackageService.getActivityWorkPackageById(workpackageId,0);
				
				productId = activityWorkPackage.getProductBuild().getProductVersion().getProductMaster().getProductId();
				
				activityWorkPackage.setActivityWorkPackageId(null);
				activityWorkPackage.setActivityWorkPackageName(newWorkpackageName);
				activityWorkPackage.setPlannedStartDate(new Date(planStartDate));
				activityWorkPackage.setPlannedEndDate(new Date(planEndDate));
				activityWorkPackage.setProductBuild(productBuild);
				activityWorkPackage.setCreatedDate(new Date());
				activityWorkPackage.setModifiedDate(new Date());
				activityWorkPackage.setCreatedBy(user);
				
				UserList owner = activityWorkPackage.getOwner();
				
				activityWorkPackage.setIsActive(1);		
				
				activityWorkPackage.setBaselineStartDate(activityWorkPackage.getPlannedStartDate());
				activityWorkPackage.setBaselineEndDate(activityWorkPackage.getPlannedEndDate());
								
				ActivityWorkPackage wpByName = activityWorkPackageService.getActivityWorkPackageByName(newWorkpackageName, destProductBuildId);
				if(wpByName !=null && wpByName.getActivityWorkPackageName() != null){
					jTableSingleResponse = new JTableSingleResponse("ERROR","WorkPackage name already exists");
				}
				else{
					WorkflowStatus workflowStatus = null;
					if(activityWorkPackage.getWorkflowStatus() != null && activityWorkPackage.getWorkflowStatus().getWorkflowStatusId() != null && activityWorkPackage.getWorkflowStatus().getWorkflowStatusId() > 0){
						workflowStatus = workflowStatusService.getInitialStatusForInstanceByWorkflowId(productId, IDPAConstants.ENTITY_ACTIVITY_WORKPACKAGE_ID, null, activityWorkPackage.getWorkflowStatus().getWorkflow().getWorkflowId());						
					}else{
						workflowStatus = workflowStatusService.getWorkflowStatusById(-1);
					}
					activityWorkPackage.setWorkflowStatus(workflowStatus);
					activityWorkPackage.setOwner(owner);
					
					if(workflowStatus.getWorkflowStatusId() == -1){
						activityWorkPackage.setActualStartDate(new Date());
						activityWorkPackage.setActualEndDate(new Date());
					}
					activityWorkPackageService.addActivityWorkPackage(activityWorkPackage);//Adding activity workpackage
					jTableSingleResponse = new JTableSingleResponse("OK",new JsonActivityWorkPackage(activityWorkPackage));
					if(activityWorkPackage!=null && activityWorkPackage.getActivityWorkPackageId() != null)
					{
						Integer workflowId = 0;
						if(activityWorkPackage.getWorkflowStatus() != null && activityWorkPackage.getWorkflowStatus().getWorkflow() != null){
							workflowId = activityWorkPackage.getWorkflowStatus().getWorkflow().getWorkflowId();
						}
						workflowStatusPolicyService.instanciateWorkflowStatusAndPoliciesForInstanceByWorkflowId(productId, IDPAConstants.ENTITY_ACTIVITY_WORKPACKAGE_ID, null, activityWorkPackage.getActivityWorkPackageId(), workflowId, activityWorkPackage.getWorkflowStatus().getWorkflowStatusId(), user, activityWorkPackage.getPlannedStartDate(), activityWorkPackage);
						mongoDBService.addActivityWorkPackagetoMongoDB(activityWorkPackage.getActivityWorkPackageId());
						
						int userId=user.getUserId();
						UserList userObj = userListService.getUserListById(userId);
						EntityMaster entityMaster = workPackageService.getEntityMasterByName(IDPAConstants.ENTITY_ACTIVITY_WORKPACKAGE);
						eventsService.addNewEntityEvent(IDPAConstants.ENTITY_ACTIVITY_WORKPACKAGE, activityWorkPackage.getActivityWorkPackageId(), activityWorkPackage.getActivityWorkPackageName(), userObj);
						try {
							activitieslist = activityService.listAllActivitiesByActivityWorkPackageId(workpackageId,0,1);
							if(activitieslist != null && activitieslist.size()>0){
								ActivityWorkPackage actWorkPackage=activityWorkPackageService.getActivityWorkPackageById(activityWorkPackage.getActivityWorkPackageId(),0);
								for(Activity activity: activitieslist){
									Activity newActivityObj=activity;
									int activityId=activity.getActivityId();
									newActivityObj.setActivityId(null);
									newActivityObj.setActivityWorkPackage(actWorkPackage);
									newActivityObj.setCreatedDate(new Date());
									newActivityObj.setModifiedDate(new Date());
									newActivityObj.setCreatedBy(user);
									newActivityObj.setIsActive(1);
									
									newActivityObj.setBaselineStartDate(newActivityObj.getPlannedStartDate());
									newActivityObj.setBaselineEndDate(newActivityObj.getPlannedEndDate());
									
									WorkflowStatus activityWorkflowStatus = null;
									Integer entityId = 0;
									if(newActivityObj.getActivityMaster() != null){
										entityId = newActivityObj.getActivityMaster().getActivityMasterId();
									}
									if(newActivityObj.getWorkflowStatus() != null && newActivityObj.getWorkflowStatus().getWorkflowStatusId() != null && newActivityObj.getWorkflowStatus().getWorkflowStatusId() > 0){
										if(newActivityObj.getActivityMaster() != null){
											activityWorkflowStatus = workflowStatusService.getInitialStatusForInstanceByWorkflowId(productId, IDPAConstants.ENTITY_ACTIVITY_TYPE, entityId, newActivityObj.getWorkflowStatus().getWorkflow().getWorkflowId());						
										}else{
											activityWorkflowStatus = workflowStatusService.getWorkflowStatusById(-1);
										}
									}else{
										activityWorkflowStatus = workflowStatusService.getWorkflowStatusById(-1);
									}
									newActivityObj.setWorkflowStatus(activityWorkflowStatus);
									if(workflowStatus.getWorkflowStatusId() == -1){
										newActivityObj.setActualStartDate(new Date());
										newActivityObj.setActualEndDate(new Date());
									}
									activityService.addActivity(newActivityObj);// Adding activity 
									
									if(newActivityObj != null && newActivityObj.getActivityId() != null){
										Integer activityWorkflowId = 0;
										if(newActivityObj.getWorkflowStatus() != null && newActivityObj.getWorkflowStatus().getWorkflow() != null){
											activityWorkflowId = newActivityObj.getWorkflowStatus().getWorkflow().getWorkflowId();
										}
										if(newActivityObj.getActivityMaster() != null){
											workflowStatusPolicyService.instanciateWorkflowStatusAndPoliciesForInstanceByWorkflowId(productId, IDPAConstants.ENTITY_ACTIVITY_TYPE, entityId, newActivityObj.getActivityId(), activityWorkflowId, newActivityObj.getWorkflowStatus().getWorkflowStatusId(), user, newActivityObj.getPlannedStartDate(), newActivityObj);
										}
										if(newActivityObj.getLifeCycleStage() != null){
											workflowStatusPolicyService.addWorkflowInstanceLifeCycleStage(productId,IDPAConstants.ENTITY_ACTIVITY_ID, newActivityObj.getActivityId(), newActivityObj.getLifeCycleStage().getWorkflowStatusId(), DateUtility.dateToStringInSecond(newActivityObj.getPlannedStartDate()), DateUtility.dateToStringInSecond(newActivityObj.getPlannedEndDate()), DateUtility.dateToStringInSecond(new Date()), user.getUserId());
										}
										customFieldService.cloneCustomFieldValuesOfInstance(IDPAConstants.ENTITY_ACTIVITY_ID, activity.getActivityId(), newActivityObj.getActivityId());
									}
									
									mongoDBService.addActivitytoMongoDB(newActivityObj.getActivityId());
									entityMaster=null;
									entityMaster = workPackageService.getEntityMasterByName(IDPAConstants.ENTITY_ACTIVITY);
									//Entity Audition History 
									eventsService.addNewEntityEvent(IDPAConstants.ENTITY_ACTIVITY, newActivityObj.getActivityId(), newActivityObj.getActivityName(), userObj);
									
									int newActivityId=newActivityObj.getActivityId();
									Activity activities=activityService.getActivityById(newActivityId, 1);
									try {
										List<ActivityTask> activityTaskList = null;																
										activityTaskList = activityTaskService.getActivityTaskById(activityId);
										if(activityTaskList!=null && activityTaskList.size()!=0){
											for(ActivityTask activityTask: activityTaskList){
												
												Integer activityTaskEffort= workflowEventService.getTotalEffortsByEntityInstanceIdAndEntityType(activityTask.getActivityTaskId(), IDPAConstants.ENTITY_TASK_TYPE);
												if(activityTaskEffort != null && activityTaskEffort>0){
													activityTask.setTotalEffort(activityTaskEffort);
												}
												ActivityTask newActivityTaskObj=activityTask;
												int activityTaskId=activityTask.getActivityTaskId();
												newActivityTaskObj.setActivityTaskId(null);
												newActivityTaskObj.setActivity(activities);
												newActivityTaskObj.setCreatedDate(new Date());
												newActivityTaskObj.setModifiedDate(new Date());
												newActivityTaskObj.setCreatedBy(user);
												newActivityTaskObj.setIsActive(1);
												
												newActivityTaskObj.setBaselineStartDate(newActivityTaskObj.getPlannedStartDate());
												newActivityTaskObj.setBaselineEndDate(newActivityTaskObj.getPlannedEndDate());
												
												entityId = 0;
												if(newActivityTaskObj.getActivityTaskType() != null){
													entityId = newActivityTaskObj.getActivityTaskType().getActivityTaskTypeId();
												}
												WorkflowStatus taskWorkflowStatus = null;
												if(newActivityTaskObj.getStatus() != null && newActivityTaskObj.getStatus().getWorkflowStatusId() != null && newActivityTaskObj.getStatus().getWorkflowStatusId() > 0){
													if(newActivityTaskObj.getActivityTaskType() != null){
														taskWorkflowStatus = workflowStatusService.getInitialStatusForInstanceByWorkflowId(productId, IDPAConstants.ENTITY_TASK_TYPE, entityId, newActivityTaskObj.getStatus().getWorkflow().getWorkflowId());						
													}else{
														taskWorkflowStatus = workflowStatusService.getWorkflowStatusById(-1);
													}
												}else{
													taskWorkflowStatus = workflowStatusService.getWorkflowStatusById(-1);
												}
												newActivityTaskObj.setStatus(taskWorkflowStatus);
												if(workflowStatus.getWorkflowStatusId() == -1){
													newActivityTaskObj.setActualStartDate(new Date());
													newActivityTaskObj.setActualEndDate(new Date());
												}
												activityTaskService.addActivityTask(newActivityTaskObj);// Adding activity task
												if(newActivityTaskObj!=null && newActivityTaskObj.getActivityTaskId() != null){
													Integer taskWorkflowId = 0;
													if(newActivityTaskObj.getStatus() != null && newActivityTaskObj.getStatus().getWorkflow() != null){
														taskWorkflowId = newActivityTaskObj.getStatus().getWorkflow().getWorkflowId();
													}
													if(newActivityTaskObj.getActivityTaskType() != null){
														workflowStatusPolicyService.instanciateWorkflowStatusAndPoliciesForInstanceByWorkflowId(productId, IDPAConstants.ENTITY_TASK_TYPE, entityId, newActivityTaskObj.getActivityTaskId(), taskWorkflowId, newActivityTaskObj.getStatus().getWorkflowStatusId(), user, newActivityTaskObj.getPlannedStartDate(), newActivityTaskObj);
													}
													if(newActivityTaskObj.getLifeCycleStage() != null){
														workflowStatusPolicyService.addWorkflowInstanceLifeCycleStage(productId, IDPAConstants.ENTITY_TASK, newActivityTaskObj.getActivityTaskId(), newActivityTaskObj.getLifeCycleStage().getWorkflowStatusId(), DateUtility.dateToStringInSecond(newActivityTaskObj.getPlannedStartDate()), DateUtility.dateToStringInSecond(newActivityTaskObj.getPlannedEndDate()), DateUtility.dateToStringInSecond(new Date()), user.getUserId());
													}
													mongoDBService.addActivityTaskToMongoDB(newActivityTaskObj.getActivityTaskId());
													entityMaster=null;
													entityMaster = workPackageService.getEntityMasterByName(IDPAConstants.ENTITY_ACTIVITY_TASK);
													//Entity Audition History 
													eventsService.addNewEntityEvent(IDPAConstants.ENTITY_ACTIVITY_TASK, newActivityTaskObj.getActivityTaskId(), newActivityTaskObj.getActivityTaskName(), userObj);
												}
											}//Ending Activity Task for loop
										}
									} catch (Exception e) {
										log.error("Error:::"+e.getMessage());
									}
									
								}//Ending Activity for loop
							}
						} catch (Exception e) {
							log.error("Error:::"+e.getMessage());
						}
						
					}
				}
				
			}catch(Exception e){
				log.error("Error...",e);
			}
		    return jTableSingleResponse;

		}
		@RequestMapping(value = "process.activity.clone", method = RequestMethod.POST, produces = "application/json")
		public @ResponseBody
		JTableSingleResponse cloneActivity(HttpServletRequest request, @RequestParam Integer srcActWorkpackageId, @RequestParam Integer destActWorkpackageId, @RequestParam Integer  cloneActivityId,@RequestParam String newActivityName, String planStartDate, String planEndDate, String description) {
		JTableSingleResponse jTableSingleResponse=null;
		log.info("cloneActivityId "+cloneActivityId);
		log.info("newActivityName "+newActivityName);
	    ActivityWorkPackage activityWorkPackage =null;
	    List<Activity> activitieslist = null;
	    Integer productId = 0;
		try{
			UserList user = (UserList) request.getSession().getAttribute("USER");
			try {
				
				if (ActivityDateValidation(DateUtility.dateformatWithOutTime(planStartDate),DateUtility.dateformatWithOutTime(planEndDate))) {
					return jTableSingleResponse = new JTableSingleResponse("INFORMATION",
							"Planned End date should be less than or equal to Planned Start date");
				}
				
				boolean errorFlagActivity = activityService.getActivityByWPIdandActivityName(destActWorkpackageId, newActivityName);
				if(errorFlagActivity){
					return jTableSingleResponse = new JTableSingleResponse("INFORMATION","Activity Name Already Exists For Same WorkPackage!");
				}
				
				activityWorkPackage=activityWorkPackageService.getActivityWorkPackageById(destActWorkpackageId,0);
				Date wpStDate = activityWorkPackage.getPlannedStartDate();
				Date wpEndDate = activityWorkPackage.getPlannedEndDate();
				Date actStDate = DateUtility.dateformatWithOutTime(planStartDate);
				Date actEndDate = DateUtility.dateformatWithOutTime(planEndDate);
				
				String errmsg="Please select Start Date & End Date between WorkPackage Date "+activityWorkPackage.getPlannedStartDate().toString().replaceAll("00:00:00.0", "")+"  and "+wpEndDate.toString().replaceAll("00:00:00.0", "");
				
				if(actStDate!=null || actEndDate!=null)
				{
				 Integer dateresult = DateUtility.ActivityDateValidation(actStDate, actEndDate,wpStDate, wpEndDate);
						if (dateresult == 1) {
							return jTableSingleResponse = new JTableSingleResponse("INFORMATION",errmsg);
						} else if (dateresult == 2) {
							return jTableSingleResponse = new JTableSingleResponse("INFORMATION",errmsg);
						} else
							 jTableSingleResponse = new JTableSingleResponse("OK");
					}
				
				Activity activity=activityService.getActivityById(cloneActivityId, 1);
				if(activity!=null){
					Activity newActivityObj=activity;
					int activityId=activity.getActivityId();
					productId = activity.getActivityWorkPackage().getProductBuild().getProductVersion().getProductMaster().getProductId();
					newActivityObj.setActivityId(null);
					newActivityObj.setActivityName(newActivityName);
					newActivityObj.setActivityWorkPackage(activityWorkPackage);
					newActivityObj.setPlannedStartDate(new Date(planStartDate));
					newActivityObj.setPlannedEndDate(new Date(planEndDate));
					newActivityObj.setCreatedDate(new Date());
					newActivityObj.setModifiedDate(new Date());
					newActivityObj.setCreatedBy(user);
					newActivityObj.setIsActive(1);
					
					newActivityObj.setBaselineStartDate(newActivityObj.getPlannedStartDate());
					newActivityObj.setBaselineEndDate(newActivityObj.getPlannedEndDate());
					
					Integer entityId = 0;
					if(newActivityObj.getActivityMaster() != null){
						entityId = newActivityObj.getActivityMaster().getActivityMasterId();
					}
					WorkflowStatus activityWorkflowStatus = null;
					if(newActivityObj.getWorkflowStatus() != null && newActivityObj.getWorkflowStatus().getWorkflowStatusId() != null && newActivityObj.getWorkflowStatus().getWorkflowStatusId() > 0){
						if(newActivityObj.getActivityMaster() != null){
							activityWorkflowStatus = workflowStatusService.getInitialStatusForInstanceByWorkflowId(productId, IDPAConstants.ENTITY_ACTIVITY_TYPE, entityId, newActivityObj.getWorkflowStatus().getWorkflow().getWorkflowId());						
						}else{
							activityWorkflowStatus = workflowStatusService.getWorkflowStatusById(-1);
						}
					}else{
						activityWorkflowStatus = workflowStatusService.getWorkflowStatusById(-1);
					}
					newActivityObj.setWorkflowStatus(activityWorkflowStatus);
					if(activityWorkflowStatus.getWorkflowStatusId() == -1){
						newActivityObj.setActualStartDate(new Date());
						newActivityObj.setActualEndDate(new Date());
					}
					activityService.addActivity(newActivityObj);// Adding activity
					
					if(newActivityObj.getActivityId() != null) {
						EntityRelationship entityRelationship = new EntityRelationship();
						entityRelationship.setEntityTypeId1(IDPAConstants.ENTITY_ACTIVITY_ID);
						entityRelationship.setEntityTypeId2(IDPAConstants.ENTITY_ACTIVITY_ID);
						entityRelationship.setEntityInstanceId1(cloneActivityId);
						entityRelationship.setEntityInstanceId2(newActivityObj.getActivityId());
						entityRelationship.setIsActive(1);
						entityRelationship.setRelationshipType(IDPAConstants.CLONED);
						entityRelationshipService.addEntityRelationship(entityRelationship);
					}
					
					if(newActivityObj != null && newActivityObj.getActivityId() != null){
						Integer activityWorkflowId = 0;
						if(newActivityObj.getWorkflowStatus() != null && newActivityObj.getWorkflowStatus().getWorkflow() != null){
							activityWorkflowId = newActivityObj.getWorkflowStatus().getWorkflow().getWorkflowId();
						}
						if(newActivityObj.getActivityMaster() != null){
							workflowStatusPolicyService.instanciateWorkflowStatusAndPoliciesForInstanceByWorkflowId(productId, IDPAConstants.ENTITY_ACTIVITY_TYPE, entityId, newActivityObj.getActivityId(), activityWorkflowId, newActivityObj.getWorkflowStatus().getWorkflowStatusId(), user, newActivityObj.getPlannedStartDate(), newActivityObj);
						}
						if(newActivityObj.getLifeCycleStage() != null){
							workflowStatusPolicyService.addWorkflowInstanceLifeCycleStage(productId,IDPAConstants.ENTITY_ACTIVITY_ID, newActivityObj.getActivityId(), newActivityObj.getLifeCycleStage().getWorkflowStatusId(), DateUtility.dateToStringInSecond(newActivityObj.getPlannedStartDate()), DateUtility.dateToStringInSecond(newActivityObj.getPlannedEndDate()), DateUtility.dateToStringInSecond(new Date()), user.getUserId());
						}
						customFieldService.cloneCustomFieldValuesOfInstance(IDPAConstants.ENTITY_ACTIVITY_ID, cloneActivityId, newActivityObj.getActivityId());
					}
					
					jTableSingleResponse = new JTableSingleResponse("OK",new JsonActivity(activity));
					int newActivityId=newActivityObj.getActivityId();
					Activity activities=activityService.getActivityById(newActivityId, 1);
					mongoDBService.addActivitytoMongoDB(newActivityObj.getActivityId());
					int userId=user.getUserId();
					UserList userObj = userListService.getUserListById(userId);
					EntityMaster entityMaster = workPackageService.getEntityMasterByName(IDPAConstants.ENTITY_ACTIVITY);
					//Entity Audition History 
					eventsService.addNewEntityEvent(IDPAConstants.ENTITY_ACTIVITY, newActivityObj.getActivityId(), newActivityObj.getActivityName(), userObj);
					
					try {
						List<ActivityTask> activityTaskList = null;																
						activityTaskList = activityTaskService.getActivityTaskById(activityId);
						if(activityTaskList!=null && activityTaskList.size()!=0){
							for(ActivityTask activityTask: activityTaskList){
								
								Integer activityTaskEffort= workflowEventService.getTotalEffortsByEntityInstanceIdAndEntityType(activityTask.getActivityTaskId(), IDPAConstants.ENTITY_TASK_TYPE);
								if(activityTaskEffort != null && activityTaskEffort>0){
									activityTask.setTotalEffort(activityTaskEffort);
								}
								ActivityTask newActivityTaskObj=activityTask;
								//int activityTaskId=activityTask.getActivityTaskId();
								newActivityTaskObj.setActivityTaskId(null);
								newActivityTaskObj.setActivity(activities);
								newActivityTaskObj.setCreatedDate(new Date());
								newActivityTaskObj.setModifiedDate(new Date());
								newActivityTaskObj.setCreatedBy(user);
								newActivityTaskObj.setIsActive(1);
								
								newActivityTaskObj.setBaselineStartDate(newActivityTaskObj.getPlannedStartDate());
								newActivityTaskObj.setBaselineEndDate(newActivityTaskObj.getPlannedEndDate());
								
								entityId = 0;
								if(newActivityTaskObj.getActivityTaskType() != null){
									entityId = newActivityTaskObj.getActivityTaskType().getActivityTaskTypeId();
								}
								WorkflowStatus taskWorkflowStatus = null;
								if(newActivityTaskObj.getStatus() != null && newActivityTaskObj.getStatus().getWorkflowStatusId() != null && newActivityTaskObj.getStatus().getWorkflowStatusId() > 0){
									if(newActivityTaskObj.getActivityTaskType() != null){
										taskWorkflowStatus = workflowStatusService.getInitialStatusForInstanceByWorkflowId(productId, IDPAConstants.ENTITY_TASK_TYPE, entityId, newActivityTaskObj.getStatus().getWorkflow().getWorkflowId());						
									}else{
										taskWorkflowStatus = workflowStatusService.getWorkflowStatusById(-1);
									}
								}else{
									taskWorkflowStatus = workflowStatusService.getWorkflowStatusById(-1);
								}
								newActivityTaskObj.setStatus(taskWorkflowStatus);
								if(taskWorkflowStatus.getWorkflowStatusId() == -1){
									newActivityTaskObj.setActualStartDate(new Date());
									newActivityTaskObj.setActualEndDate(new Date());
								}
								activityTaskService.addActivityTask(newActivityTaskObj);// Adding activity task
								if(newActivityTaskObj!=null && newActivityTaskObj.getActivityTaskId() != null){
									Integer taskWorkflowId = 0;
									if(newActivityTaskObj.getStatus() != null && newActivityTaskObj.getStatus().getWorkflow() != null){
										taskWorkflowId = newActivityTaskObj.getStatus().getWorkflow().getWorkflowId();
									}
									if(newActivityTaskObj.getActivityTaskType() != null){
										workflowStatusPolicyService.instanciateWorkflowStatusAndPoliciesForInstanceByWorkflowId(productId, IDPAConstants.ENTITY_TASK_TYPE, entityId, newActivityTaskObj.getActivityTaskId(), taskWorkflowId, newActivityTaskObj.getStatus().getWorkflowStatusId(), user, newActivityTaskObj.getPlannedStartDate(), newActivityTaskObj);
									}
									if(newActivityTaskObj.getLifeCycleStage() != null){
										workflowStatusPolicyService.addWorkflowInstanceLifeCycleStage(productId, IDPAConstants.ENTITY_TASK, newActivityTaskObj.getActivityTaskId(), newActivityTaskObj.getLifeCycleStage().getWorkflowStatusId(), DateUtility.dateToStringInSecond(newActivityTaskObj.getPlannedStartDate()), DateUtility.dateToStringInSecond(newActivityTaskObj.getPlannedEndDate()), DateUtility.dateToStringInSecond(new Date()), user.getUserId());
									}
									mongoDBService.addActivityTaskToMongoDB(newActivityTaskObj.getActivityTaskId());
									entityMaster=null;
									entityMaster = workPackageService.getEntityMasterByName(IDPAConstants.ENTITY_ACTIVITY_TASK);
									//Entity Audition History 
									eventsService.addNewEntityEvent(IDPAConstants.ENTITY_ACTIVITY_TASK, newActivityTaskObj.getActivityTaskId(), newActivityTaskObj.getActivityTaskName(), userObj);
								}
							}//Ending Activity Task for loop
						}
					} catch (Exception e) {
						log.error("Error:::"+e.getMessage());
					}
				}
				
			} catch (Exception e) {
				log.error("Error:::"+e.getMessage());
			}
					
		
		}catch(Exception e){
			log.error("Error...",e);
		}
	    return jTableSingleResponse;

	}
		@RequestMapping(value = "process.activitytask.clone", method = RequestMethod.POST, produces = "application/json")
		public @ResponseBody
		JTableSingleResponse cloneActivityTask(HttpServletRequest request, @RequestParam Integer srcActivityId, @RequestParam Integer destActivityId, @RequestParam Integer  cloneActivityTaskId,@RequestParam String newActivityTaskName, String planStartDate, String planEndDate, String description) {
		JTableSingleResponse jTableSingleResponse=null;
		log.info("cloneActivityTaskId "+cloneActivityTaskId);
		log.info("newActivityTaskName "+newActivityTaskName);
	    ActivityWorkPackage activityWorkPackage =null;
	    List<Activity> activitieslist = null;
	    Integer productId = 0;
		try{
			UserList user = (UserList) request.getSession().getAttribute("USER");
			//code to add activities under this workpackage
			
			try {
				/* Plan start date should be less than Plan End date */
				if (ActivityDateValidation(DateUtility.dateformatWithOutTime(planStartDate),DateUtility.dateformatWithOutTime(planEndDate))) {
					return jTableSingleResponse = new JTableSingleResponse("INFORMATION",
							"Planned End date should be less than or equal to Planned Start date");
				}
				
				Activity activities=activityService.getActivityById(destActivityId,1);
				
				Date actStDate = activities.getPlannedStartDate();
				Date actEndDate = activities.getPlannedEndDate();
				Date actTaskStDate = DateUtility.dateformatWithOutTime(planStartDate);
				Date actTaskEndDate = DateUtility.dateformatWithOutTime(planEndDate);
				String errmsg="Please select Start Date & End Date between Activity Date "+actStDate.toString().replaceAll("00:00:00.0", "")+"  and "+actEndDate.toString().replaceAll("00:00:00.0", "");
				
				if(actStDate!=null || actEndDate!=null)
				{
				 Integer dateresult = DateUtility.ActivityDateValidation(actTaskStDate, actTaskEndDate,actStDate, actEndDate);
						if (dateresult == 1) {
							return jTableSingleResponse = new JTableSingleResponse("INFORMATION",errmsg);
						} else if (dateresult == 2) {
							return jTableSingleResponse = new JTableSingleResponse("INFORMATION",errmsg);
						} else
							 jTableSingleResponse = new JTableSingleResponse("OK");
					}
				
				
				/* Duplicate Name Checking */
				boolean errorFlagActivityTask = activityTaskService.getActivityTaskById(destActivityId, newActivityTaskName);
				if(errorFlagActivityTask){
					return jTableSingleResponse = new JTableSingleResponse("INFORMATION","ActivityTask Name already Exists For Same Activity!");
				}
				
				ActivityTask activityTask=activityTaskService.getActivityTaskById(cloneActivityTaskId, 1);
				try {		
					ActivityTask actTaskObj=activityTask;
					productId = activityTask.getActivity().getActivityWorkPackage().getProductBuild().getProductVersion().getProductMaster().getProductId();
					actTaskObj.setActivityTaskId(null);
					actTaskObj.setActivityTaskName(newActivityTaskName);
					actTaskObj.setActivity(activities);
					actTaskObj.setPlannedStartDate(new Date(planStartDate));
					actTaskObj.setPlannedEndDate(new Date(planEndDate));
					actTaskObj.setCreatedDate(new Date());
					actTaskObj.setModifiedDate(new Date());
					actTaskObj.setCreatedBy(user);
					actTaskObj.setIsActive(1);
					
					actTaskObj.setBaselineStartDate(actTaskObj.getPlannedStartDate());
					actTaskObj.setBaselineEndDate(actTaskObj.getPlannedEndDate());
					
					Integer entityId = 0;
					if(actTaskObj.getActivityTaskType() != null){
						entityId = actTaskObj.getActivityTaskType().getActivityTaskTypeId();
					}
					WorkflowStatus taskWorkflowStatus = null;
					if(actTaskObj.getStatus() != null && actTaskObj.getStatus().getWorkflowStatusId() != null && actTaskObj.getStatus().getWorkflowStatusId() > 0){
						if(actTaskObj.getActivityTaskType() != null){
							taskWorkflowStatus = workflowStatusService.getInitialStatusForInstanceByWorkflowId(productId, IDPAConstants.ENTITY_TASK_TYPE, entityId, actTaskObj.getStatus().getWorkflow().getWorkflowId());						
						}else{
							taskWorkflowStatus = workflowStatusService.getWorkflowStatusById(-1);
						}
					}else{
						taskWorkflowStatus = workflowStatusService.getWorkflowStatusById(-1);
					}
					actTaskObj.setStatus(taskWorkflowStatus);
					if(taskWorkflowStatus.getWorkflowStatusId() == -1){
						actTaskObj.setActualStartDate(new Date());
						actTaskObj.setActualEndDate(new Date());
					}
					activityTaskService.addActivityTask(actTaskObj);// Adding activity task		
					if(actTaskObj!=null && actTaskObj.getActivityTaskId() != null){
						Integer taskWorkflowId = 0;
						if(actTaskObj.getStatus() != null && actTaskObj.getStatus().getWorkflow() != null){
							taskWorkflowId = actTaskObj.getStatus().getWorkflow().getWorkflowId();
						}
						if(actTaskObj.getActivityTaskType() != null){
							workflowStatusPolicyService.instanciateWorkflowStatusAndPoliciesForInstanceByWorkflowId(productId, IDPAConstants.ENTITY_TASK_TYPE, entityId, actTaskObj.getActivityTaskId(), taskWorkflowId, actTaskObj.getStatus().getWorkflowStatusId(), user, actTaskObj.getPlannedStartDate(), actTaskObj);
						}
						if(actTaskObj.getLifeCycleStage() != null){
							workflowStatusPolicyService.addWorkflowInstanceLifeCycleStage(productId, IDPAConstants.ENTITY_TASK, actTaskObj.getActivityTaskId(), actTaskObj.getLifeCycleStage().getWorkflowStatusId(), DateUtility.dateToStringInSecond(actTaskObj.getPlannedStartDate()), DateUtility.dateToStringInSecond(actTaskObj.getPlannedEndDate()), DateUtility.dateToStringInSecond(new Date()), user.getUserId());
						}
						mongoDBService.addActivityTaskToMongoDB(actTaskObj.getActivityTaskId());
						
						int userId=user.getUserId();
						UserList userObj = userListService.getUserListById(userId);
						EntityMaster entityMaster = workPackageService.getEntityMasterByName(IDPAConstants.ENTITY_ACTIVITY_TASK);
						//Entity Audition History 
						eventsService.addNewEntityEvent(IDPAConstants.ENTITY_ACTIVITY_TASK, actTaskObj.getActivityTaskId(), actTaskObj.getActivityTaskName(), userObj);	
						jTableSingleResponse = new JTableSingleResponse("OK",new JsonActivityTask(actTaskObj));
					}
				} catch (Exception e) {
					log.error("Error:::"+e.getMessage());
				}
						
					
			} catch (Exception e) {
				log.error("Error:::"+e.getMessage());
			}
					
		
		}catch(Exception e){
			log.error("Error...",e);
		}
	    return jTableSingleResponse;

	}	
		
		@RequestMapping(value="add.activity.by.changerequest",method=RequestMethod.POST ,produces="application/json")
	    public @ResponseBody JTableResponse addActivitybyChangeRequest(HttpServletRequest request, @RequestParam String changeRequestIdFromUI,@RequestParam Integer prodBuildId,@RequestParam Integer actWorkPackageId, @RequestParam Integer productId){	
			log.debug("add.activity.by.changerequest");
			JTableResponse jTableResponse=null;
			int entityType1=42;
			int entityType2=28;
			List<String> activityNameList = null;			
			ActivityWorkPackage activityWorkPackage = activityWorkPackageService.getActivityWorkPackageById(actWorkPackageId, 1);
			ProductBuild productBuild = productListService.getProductBuildIdWithCompleteInitialization(prodBuildId);
			activityNameList = activityService.getExistingActiviesName(activityWorkPackage, productBuild);			
			Integer testFactoryId = productBuild.getProductVersion().getProductMaster().getTestFactory().getTestFactoryId();
			String[] crIds = changeRequestIdFromUI.split(",");
			String duplicateActivityNames = "";
			ActivityMaster activityType = null;
			List<JsonActivityMaster> jsonActivityMasters = activityTypeService.listActivityTypes(testFactoryId, productId, null, null,0, true);
			if(jsonActivityMasters != null && jsonActivityMasters.size() > 0){
				activityType = jsonActivityMasters.get(0).getActivityMaster();
			}
			for(String changeRequestId : crIds){
				ChangeRequest changeRequest = changeRequestService.getChangeRequestById(Integer.parseInt(changeRequestId));
				String crName = changeRequest.getChangeRequestName();
				if(crName != null && crName != ""){
					if(!activityNameList.contains(crName)){
						Activity activity = new Activity();
						activity.setActivityName(changeRequest.getChangeRequestName());
						activity.setActivityWorkPackage(activityWorkPackage);
						activity.setPlannedStartDate(activityWorkPackage.getPlannedStartDate());
						activity.setPlannedEndDate(activityWorkPackage.getPlannedEndDate());
						activity.setIsActive(1);
						activity.setActualActivitySize(0);						
						activity.setCreatedBy(changeRequest.getOwner());
						activity.setModifiedBy(changeRequest.getOwner());
						activity.setCreatedDate(new Date());
						activity.setModifiedDate(new Date());
						activity.setActivityMaster(activityType);
						activity.setAssignee(changeRequest.getOwner());
						activity.setReviewer(changeRequest.getOwner());
						activity.setPriority(changeRequest.getPriority());
						
						activity.setBaselineStartDate(activity.getPlannedStartDate());
						activity.setBaselineEndDate(activity.getPlannedEndDate());
						activity.setComplexity("LOW");
												
						if(activity.getCategory() == null){
						List<ExecutionTypeMaster> executionTypeMasterFromDB =	executionTypeMasterService.listbyEntityMasterId(1);
						ExecutionTypeMaster executionTypeMaster =		executionTypeMasterFromDB.get(0);
						activity.setCategory(executionTypeMaster);
						}				
												
						
						WorkflowStatus workflowStatus = null;
						if(activityType != null){
							workflowStatus = workflowStatusService.getInitialStatusForInstanceByEntityType(productId, IDPAConstants.ENTITY_ACTIVITY_TYPE, activityType.getActivityMasterId());
						}
						if(workflowStatus == null || workflowStatus.getWorkflowStatusId() == null || workflowStatus.getWorkflowStatusId() == 0){
							workflowStatus = workflowStatusService.getWorkflowStatusById(-1);
						}
						activity.setWorkflowStatus(workflowStatus);
						if(workflowStatus.getWorkflowStatusId() == -1){
							activity.setActualStartDate(new Date());
							activity.setActualEndDate(new Date());
						}
						if(changeRequest.getPlanExpectedValue() != null){
							activity.setPlannedActivitySize(changeRequest.getPlanExpectedValue());
						}else{
							activity.setPlannedActivitySize(1);
						}
						
					
						WorkflowStatus workflowStatusLifeCycleStatus = null;							
						if(activityWorkPackage.getWorkflowStatus() != null && activityWorkPackage.getWorkflowStatus().getWorkflowStatusId() != -1){
							WorkflowMaster workflowMaster = activityWorkPackage.getWorkflowStatus().getWorkflow();
							if(workflowMaster != null) {
								List<WorkflowStatus> workflowStatusList = workflowStatusService.getWorkFlowStatusList(workflowMaster.getWorkflowId(), null);
								if (workflowStatusList != null) {
									workflowStatusLifeCycleStatus = workflowStatusList.get(0);
								}
							}
							activity.setLifeCycleStage(workflowStatusLifeCycleStatus);
						}
						
						activityService.addActivity(activity);
						if(activity != null && activity.getActivityId()!=null){
							mongoDBService.addActivitytoMongoDB(activity.getActivityId());
						}
							
						if(activity != null){
							Integer activityTypeId=activity.getActivityMaster() != null ?activity.getActivityMaster().getActivityMasterId():0;
							workflowStatusPolicyService.instanciateWorkflowStatusAndPoliciesForInstanceByEntityType(productId, IDPAConstants.ENTITY_ACTIVITY_TYPE, activityTypeId, activity.getActivityId(), activity.getWorkflowStatus().getWorkflowStatusId(), activity.getCreatedBy(), activity.getPlannedStartDate(), activity);
							if(activity.getLifeCycleStage() != null){
								UserList user = (UserList) request.getSession().getAttribute("USER");
								workflowStatusPolicyService.addWorkflowInstanceLifeCycleStage(productId, IDPAConstants.ENTITY_ACTIVITY_ID, activity.getActivityId(), activity.getLifeCycleStage().getWorkflowStatusId(), DateUtility.dateToStringInSecond(activity.getPlannedStartDate()), DateUtility.dateToStringInSecond(activity.getPlannedEndDate()), DateUtility.dateToStringInSecond(new Date()), user.getUserId());
							}
						}
						
						Activity act = activityService.getActivityByName(activity.getActivityName());
						
						EntityRelationship entityRelationship = new EntityRelationship();
						entityRelationship.setEntityTypeId1(entityType1);
						entityRelationship.setEntityTypeId2(entityType2);
						entityRelationship.setEntityInstanceId1(Integer.parseInt(changeRequestId));
						entityRelationship.setEntityInstanceId2(act.getActivityId());
						entityRelationship.setIsActive(1);
						entityRelationshipService.addEntityRelationship(entityRelationship);
						
						activityNameList.add(crName);
						
					}else{
						duplicateActivityNames += crName+", ";
						jTableResponse = new JTableResponse("ERROR",duplicateActivityNames);
					}
				}
				
			}			
			return  jTableResponse;
		}
		
		@RequestMapping(value="activity.task.type.list",method=RequestMethod.POST ,produces="application/json")
	    public @ResponseBody JTableResponse activityTaskTypeList(@RequestParam Integer testFactoryId, @RequestParam Integer productId, @RequestParam Integer jtStartIndex, @RequestParam Integer jtPageSize) {
			log.info("inside activity.task.type.list");
			JTableResponse jTableResponse;
			try {
				Integer status = 1;
				List<ActivityTaskType> activityTaskTypes = activityTaskService.listActivityTaskTypes(testFactoryId, productId, status, jtStartIndex, jtPageSize, false);
				List<JsonActivityTaskType> jsonActivityTaskTypes = new ArrayList<JsonActivityTaskType>();
				Integer totalRecordsAvailable = activityTaskService.getTotalRecordsForTaskTypesPagination(testFactoryId, productId, status, false);
				
				for(ActivityTaskType activityTaskType : activityTaskTypes){
					jsonActivityTaskTypes.add(new JsonActivityTaskType(activityTaskType));
				}
		        jTableResponse = new JTableResponse("OK", jsonActivityTaskTypes, totalRecordsAvailable);
		           
	        } catch (Exception e) {
	            jTableResponse = new JTableResponse("ERROR","Error fetching records!");
	            log.error("JSON ERROR", e);
	        }
		        
	        return jTableResponse;
	    }
		
		@RequestMapping(value="activity.task.type.add",method=RequestMethod.POST ,produces="application/json")
	    public @ResponseBody JTableSingleResponse activityTaskTypeAdd(@RequestParam Integer testFactoryId, @RequestParam Integer productId, @ModelAttribute JsonActivityTaskType jsonActivityTaskType) {
			log.info("inside activity.task.type.add");
			JTableSingleResponse jTableSingleResponse;
			try {
				ActivityTaskType activityTaskType = jsonActivityTaskType.getActivityTaskType();
				Boolean isAlreadyExists = activityTaskService.checkTaskTypeExistForProduct(testFactoryId, productId, activityTaskType.getActivityTaskTypeName(), activityTaskType.getActivityTaskTypeId(), true);
				if(isAlreadyExists){
					jTableSingleResponse = new JTableSingleResponse("ERROR", activityTaskType.getActivityTaskTypeName()+" is already exist for this product / testfactory level");
				}else{
					activityTaskService.addActivityTaskType(activityTaskType);
					jTableSingleResponse = new JTableSingleResponse("OK", activityTaskType, "Activity task type added successfully");
				}
			} catch (Exception e) {
				jTableSingleResponse = new JTableSingleResponse("ERROR","Error adding records!");
	            log.error("JSON ERROR", e);
	        }
		        
	        return jTableSingleResponse;
	    }
		
		@RequestMapping(value="activity.task.type.update",method=RequestMethod.POST ,produces="application/json")
	    public @ResponseBody JTableSingleResponse activityTaskTypeUpdate(@ModelAttribute JsonActivityTaskType jsonActivityTaskType) {
			log.info("inside activity.task.type.add");
			JTableSingleResponse jTableSingleResponse;
			try {
				ActivityTaskType activityTaskType = jsonActivityTaskType.getActivityTaskType();
				Boolean isAlreadyExists = activityTaskService.checkTaskTypeExistForProduct(jsonActivityTaskType.getTestFactoryId(), jsonActivityTaskType.getProductId(), activityTaskType.getActivityTaskTypeName(), activityTaskType.getActivityTaskTypeId(), true);
				if(isAlreadyExists){
					jTableSingleResponse = new JTableSingleResponse("ERROR", activityTaskType.getActivityTaskTypeName()+" is already exist for this product / testfactory level");
				}else{
					activityTaskService.updateActivityTaskType(activityTaskType);
					jTableSingleResponse = new JTableSingleResponse("OK", activityTaskType, "Activity task type updated successfully");
				}
			} catch (Exception e) {
				jTableSingleResponse = new JTableSingleResponse("ERROR","Error updating records!");
	            log.error("JSON ERROR", e);
	        }
		        
	        return jTableSingleResponse;
	    }		

		@RequestMapping(value="get.productId.of.activity",method=RequestMethod.POST ,produces="application/json")
	    public @ResponseBody String getProductIdOfActivity(@RequestParam int activityId) {
			log.info("Inside getProductIdOfActivity ");
			String ffinalResult="";
			JSONArray jproductId = new JSONArray();
			int productId = 0;
			try {   
				if(activityId != -1){
					productId = activityService.getProductIdOfActivity(activityId);	
				}else{
				}
								
				JSONObject jobj = new JSONObject();
				jobj.put("productId", productId);
				jproductId.add(jobj);				
						ffinalResult=jproductId.toString();						
						if (ffinalResult == null) {		
							log.info("No Product Id");							
							return "No Data";
						}
		        } catch (Exception e) {
		            log.error("JSON ERROR", e);
		            e.printStackTrace();
		        }		        
	        return  ffinalResult;
	    }	
	
		@RequestMapping(value="activity.workpackage.summary.list",method=RequestMethod.POST ,produces="application/json")
	    public @ResponseBody JTableResponse getActivityWorkPackageSummary(@RequestParam int activityWorkPackageId, HttpServletRequest req) {
			log.debug("activity.workpackage.summary.list");
			UserList user=(UserList)req.getSession().getAttribute("USER");
			int tcerId = 0;
			JTableResponse jTableResponse = null;
				try {
					ActivityWorkPackageSummaryDTO activityWorkPackageSummaryDTO = activityWorkPackageService.listActivityWorkPackageSummaryDetails(activityWorkPackageId);
					List<JsonActivityWorkPackage> jsonActivityWorkPackageList = new ArrayList<JsonActivityWorkPackage>();
					
					if (activityWorkPackageSummaryDTO == null ) {
						
						jTableResponse = new JTableResponse("OK", jsonActivityWorkPackageList, 0);
					} else {
						jsonActivityWorkPackageList.add(new JsonActivityWorkPackage(activityWorkPackageSummaryDTO));
						jTableResponse = new JTableResponse("OK", jsonActivityWorkPackageList,jsonActivityWorkPackageList.size() );
						activityWorkPackageSummaryDTO = null;
					}					
		        } catch (Exception e) {
		            jTableResponse = new JTableResponse("ERROR","Error fetching records!");
		            log.error("JSON ERROR", e);	            
		        }
	        return jTableResponse;
	    }

		@RequestMapping(value="product.awp.summary",method=RequestMethod.POST ,produces="application/json")
	    public @ResponseBody JTableResponse getProductAWPSummary(@RequestParam int productId, HttpServletRequest req) {
			log.debug("product.awp.summary");
			UserList user=(UserList)req.getSession().getAttribute("USER");
			int tcerId = 0;
			JTableResponse jTableResponse = null;
				try {
					ProductAWPSummaryDTO productAWPSummaryDTO = activityWorkPackageService.listProductAWPSummaryDetails(productId);
					List<JsonProductAWPSummary> jsonProductAWPSummaryList = new ArrayList<JsonProductAWPSummary>();
					if (productAWPSummaryDTO == null ) {						
						jTableResponse = new JTableResponse("OK", jsonProductAWPSummaryList, 0);
					} else {
						jsonProductAWPSummaryList.add(new JsonProductAWPSummary(productAWPSummaryDTO));
						jTableResponse = new JTableResponse("OK", jsonProductAWPSummaryList,jsonProductAWPSummaryList.size() );
						productAWPSummaryDTO = null;
					}					
		        } catch (Exception e) {
		            jTableResponse = new JTableResponse("ERROR","Error fetching records!");
		            log.error("JSON ERROR", e);	            
		        }
	        return jTableResponse;
	    }
		
		@RequestMapping(value = "process.activity.modifiedField.update", method = RequestMethod.POST, produces = "application/json")
		public @ResponseBody
		JTableResponse updateActivityModifiedField(HttpServletRequest request, @RequestParam Integer activityId,
				@RequestParam String modifiedField,@RequestParam String modifiedValue ) {			
			JTableResponse jTableResponse = null;
			Activity activityFromDB = null;
			ActivityWorkPackage activityWorkPackage = null;
			ProductFeature feature = null;
			ProductBuild productBuild = null;
	        ProductVersionListMaster productVersion = null;
	        ProductMaster productMaster = null;
	        TestFactory testFactory = null;
	        Customer customer = null;
	        String oldFieldValue=null;
	        String modifiedFieldTitle=null;
	        String remarks = "";
	        String subject = "Activity Updation";
			 try{ 				 
				 UserList user = (UserList) request.getSession().getAttribute("USER");				 
				 activityFromDB = activityService.getActivityById(activityId, 1);
				 activityWorkPackage = activityWorkPackageService.getActivityWorkPackageById(activityFromDB.getActivityWorkPackage().getActivityWorkPackageId(),99);
				 activityFromDB.setActivityWorkPackage(activityWorkPackage);
					Date wpStDate = activityWorkPackage.getPlannedStartDate();
					Date wpEndDate = activityWorkPackage.getPlannedEndDate();
					Date actStDate = activityFromDB.getPlannedStartDate();
					Date actEndDate = activityFromDB.getPlannedEndDate();
					String errmsg="Please select Start Date & End Date between WorkPackage Date "+wpStDate.toString().replaceAll("00:00:00.0", "")+"  and "+wpEndDate.toString().replaceAll("00:00:00.0", "");
					if(actStDate!=null ||actEndDate!=null){
						Integer dateresult = DateUtility.ActivityDateValidation(actStDate, actEndDate, wpStDate, wpEndDate);
							if (dateresult == 1) {
								return jTableResponse = new JTableResponse("INFORMATION",errmsg);
							} else if (dateresult == 2) {
								return jTableResponse = new JTableResponse("INFORMATION",errmsg);
							} else
								jTableResponse = new JTableResponse("OK");
						}
					if(activityFromDB.getProductFeature() != null){
						feature = productListService.getByProductFeatureId(activityFromDB.getProductFeature().getProductFeatureId());
					}
					activityFromDB.setProductFeature(feature);
					if(activityFromDB.getBaselineActivitySize() == null) {
						activityFromDB.setBaselineActivitySize(activityFromDB.getPlannedActivitySize());
					}else {
						activityFromDB.setBaselineActivitySize(activityFromDB.getBaselineActivitySize());
					}
					
					if(activityFromDB.getActualActivitySize() == null) {
						activityFromDB.setActualActivitySize(0);
					}else {
						activityFromDB.setActualActivitySize(activityFromDB.getActualActivitySize());
					}
					
					List<ActivityTask> activityTasks = activityTaskService.getActivityTaskById(activityId);
					if((activityTasks == null || activityTasks.size() == 0) && activityFromDB.getActivityMaster() != null && activityFromDB.getActivityMaster().getActivityMasterId() != null){
						ActivityMaster activityMaster = activityTypeService.getActivityMasterById(activityFromDB.getActivityMaster().getActivityMasterId());
						if(activityMaster != null){
							Float weightage = activityMaster.getWeightage();
							if(weightage == null){
								weightage = 1.0F;
							}
							Float plannedUnit = activityFromDB.getPlannedActivitySize() * weightage;
							Float actualUnit = activityFromDB.getActualActivitySize() * weightage;
							activityFromDB.setPlannedUnit(plannedUnit);
							activityFromDB.setActualUnit(actualUnit);
						}else{
							activityFromDB.setPlannedUnit((float)activityFromDB.getPlannedActivitySize());
							activityFromDB.setActualUnit((float)activityFromDB.getActualActivitySize());
						}
					  }else{
						activityFromDB.setPlannedUnit((float)activityFromDB.getPlannedActivitySize());
						activityFromDB.setActualUnit((float)activityFromDB.getActualActivitySize());
					  }					
					activityFromDB.setModifiedDate(new Date());					
					activityFromDB.setModifiedBy(user);
					
					boolean errorFlagActivity = activityService.getActivityByWPIdandActivityName(activityFromDB.getActivityWorkPackage().getActivityWorkPackageId(),
							modifiedValue);					 			
					if(errorFlagActivity){
					jTableResponse = new JTableResponse("INFORMATION","Activity Name Already Exists For Same WorkPackage!");
					}else{
						if(modifiedField.equalsIgnoreCase("activityName")){					 					 	
							if(activityFromDB != null && modifiedValue != null){
								if(modifiedValue == ""){
									jTableResponse = new JTableResponse("INFORMATION","Activity Name should not be empty!");
								}
								else if(modifiedValue.matches(".*"+specialChars+".*")) {
									jTableResponse = new JTableResponse("INFORMATION","Activity Name consists of Invalid special Characters!");
								}
								else if(!activityFromDB.getActivityName().equals(modifiedValue)){
								 oldFieldValue=activityFromDB.getActivityName();
								 modifiedFieldTitle="Activity Name";
								 activityFromDB.setActivityName(modifiedValue);
								 }
							}
						}
					}
					
					if(modifiedField.equalsIgnoreCase("baselineActivitySize")){
						oldFieldValue = ""+activityFromDB.getBaselineActivitySize();
						modifiedFieldTitle="Base line Activity Size";
						activityFromDB.setBaselineActivitySize(Integer.parseInt(modifiedValue));
					}
					if(modifiedField.equalsIgnoreCase("plannedActivitySize")){
						oldFieldValue = ""+activityFromDB.getPlannedActivitySize();
						modifiedFieldTitle="Planned Activity Size";
						activityFromDB.setPlannedActivitySize(Integer.parseInt(modifiedValue));
					}
					if(modifiedField.equalsIgnoreCase("actualActivitySize")){
						oldFieldValue = ""+activityFromDB.getActualActivitySize();
						modifiedFieldTitle="Actual Activity Size";
						activityFromDB.setActualActivitySize(Integer.parseInt(modifiedValue));
					}
					if(modifiedField.equalsIgnoreCase("baselineEffort")){
						oldFieldValue = ""+activityFromDB.getBaselineEffort();
						modifiedFieldTitle="Base line Effort";
						activityFromDB.setBaselineEffort(Integer.parseInt(modifiedValue));
					}
					if(modifiedField.equalsIgnoreCase("plannedEffort")){
						oldFieldValue = ""+activityFromDB.getPlannedEffort();
						modifiedFieldTitle="Planned Effort";
						Integer calculatedPlannedEffort= DateUtility.getCalendarHoursBetweenDates(activityFromDB.getPlannedStartDate(), activityFromDB.getPlannedEndDate());
						activityFromDB.setPlannedEffort(activityFromDB.getPlannedEffort()+calculatedPlannedEffort);
					}
					if(modifiedField.equalsIgnoreCase("actualEffort")){
						oldFieldValue = ""+activityFromDB.getTotalEffort();
						modifiedFieldTitle="Actual Effort";
						activityFromDB.setTotalEffort(Integer.parseInt(modifiedValue));
					}
					if(modifiedField.equalsIgnoreCase("remark")){
						oldFieldValue = ""+activityFromDB.getRemark();
						modifiedFieldTitle="Remarks";
						activityFromDB.setRemark(modifiedValue);
					}
					if(modifiedField.equalsIgnoreCase("percentageCompletion")){
						oldFieldValue = ""+activityFromDB.getPercentageCompletion();
						modifiedFieldTitle="percentageCompletion";
						activityFromDB.setPercentageCompletion(Float.parseFloat(modifiedValue));
					}					
					
					activityService.updateActivity(activityFromDB);			
					activityWorkPackage = activityFromDB.getActivityWorkPackage();
		            productBuild = activityWorkPackage.getProductBuild();
		            productVersion = productBuild.getProductVersion();
		            productMaster = productVersion.getProductMaster();
		            testFactory = productMaster.getTestFactory();
		            customer = productMaster.getCustomer();			
		            
		            Integer productId = productMaster.getProductId();
		            String productName = productMaster.getProductName();			            
		            String productVersionName=productVersion.getProductVersionName();
					String productBuildName=productBuild.getBuildname();
					String awpName=activityWorkPackage.getActivityWorkPackageName();
					
					if(activityFromDB.getLifeCycleStage() != null){
						workflowStatusPolicyService.updateWorkflowInstanceLifeCycleStage(productId, IDPAConstants.ENTITY_ACTIVITY_ID, activityFromDB.getActivityId(), activityFromDB.getLifeCycleStage().getWorkflowStatusId(), DateUtility.dateToStringInSecond(activityFromDB.getPlannedStartDate()), DateUtility.dateToStringInSecond(activityFromDB.getPlannedEndDate()), DateUtility.dateToStringInSecond(new Date()), user.getUserId());
					}
					
					remarks = "TestFactory: "+testFactory.getTestFactoryName()+", Product: "+productName+", ActivityWorkpackage: "+awpName+", Activity: "+activityFromDB.getActivityName();
					//Entity Audition History //Update		
					eventsService.addEntityChangedEvent(IDPAConstants.ENTITY_ACTIVITY, activityFromDB.getActivityId(), activityFromDB.getActivityName(),
							modifiedField, modifiedFieldTitle,
							oldFieldValue, modifiedValue, user, remarks);
					
					if(activityFromDB!=null){
						mongoDBService.addActivitytoMongoDB(activityFromDB.getActivityId());
					}

					if(activityWorkPackage!=null){
						int activityWorkpackageStatusCategoryId = activityService.getActivityWorkpackageStatusCategoryIdByActivityStatusCategory(activityWorkPackage.getActivityWorkPackageId());
						if(activityWorkpackageStatusCategoryId <= 0){
							activityWorkpackageStatusCategoryId = 1;
						}
						StatusCategory statusCategory = new StatusCategory();
						statusCategory.setStatusCategoryId(activityWorkpackageStatusCategoryId);
						activityWorkPackage.setStatusCategory(statusCategory);
						activityWorkPackageService.updateActivityWorkPackage(activityWorkPackage);
						mongoDBService.addActivityWorkPackagetoMongoDB(activityWorkPackage.getActivityWorkPackageId());
					}
					
					UserList assigneeUserList=null;
					UserList reviewerUserList=null;
					if(activityFromDB.getAssignee()!=null){
						 assigneeUserList=userListService.getUserListById(activityFromDB.getAssignee().getUserId());
					}
					if(activityFromDB.getReviewer()!=null){
						 reviewerUserList=userListService.getUserListById(activityFromDB.getReviewer().getUserId());
					}
					
					notificationService.processNotification(request, productId, IDPAConstants.NOTIFICATION_ACTIVITY_UPDATION, activityFromDB,subject);
					
					jTableResponse = new JTableResponse("OK","successfully updated Activity record...");
			}catch (Exception e) {
				jTableResponse = new JTableResponse("ERROR","Error updating Activity record...");
				log.info("JSON Error", e);
			 }
			return jTableResponse;
		}
		
		
		@RequestMapping(value = "process.activity.engagement.list", method = RequestMethod.POST, produces = "application/json")
		public @ResponseBody
		JTableResponse viewActivitySummaryByEnagementLevel(HttpServletRequest req,@RequestParam int engagementId,@RequestParam int productId,@RequestParam int productVersionId,@RequestParam int productBuildId,@RequestParam int activityWorkPackageId,@RequestParam int isActive,
				@RequestParam String statusType, @RequestParam int jtStartIndex, @RequestParam int jtPageSize) {
			log.info("Fetching Activity Summary");
			JTableResponse jTableResponse;
			try {
				List<JsonActivity> jsonActivity = null;
				Map<String, String> searchStrings = new HashMap<String, String>();
				String searchassigneeId = req.getParameter("searchassigneeId");
				String searchpriority = req.getParameter("searchpriority");
				String searchstatusId = req.getParameter("searchstatusId");
				
				 searchStrings.put("searchassigneeId", searchassigneeId);
	            searchStrings.put("searchpriority", searchpriority);
	            searchStrings.put("searchstatusId", searchstatusId);
	           
				
			jsonActivity = activityService.listActivities(searchStrings,engagementId,productId, productVersionId, productBuildId,activityWorkPackageId, 0,isActive,req,statusType,jtStartIndex,jtPageSize);
			jTableResponse = new JTableResponse("OK", jsonActivity, activityService.getActivitiesCount(searchStrings,engagementId, productId, productVersionId, productBuildId, activityWorkPackageId, 0, isActive));
			
			} catch (Exception e) {
				jTableResponse = new JTableResponse("ERROR",
						"Error Fetching Activity Summary records!");
				log.error("JSON ERROR Fetching Activity Summary", e);
			}
			return jTableResponse;
		}
		
		
		
		@RequestMapping(value = "process.activity.move", method = RequestMethod.POST, produces = "application/json")
		public @ResponseBody
		JTableSingleResponse activityMove(HttpServletRequest request, @RequestParam Integer srcActWorkpackageId, @RequestParam Integer destActWorkpackageId, @RequestParam Integer  movingActivityId,@RequestParam String newActivityName, String planStartDate, String planEndDate, String description) {
		JTableSingleResponse jTableSingleResponse=null;
		log.info("movingActivityId "+movingActivityId);
		log.info("newActivityName "+newActivityName);
	    ActivityWorkPackage activityWorkPackage =null;
	    Integer productId = 0;
	    Integer destProductId=0;
		try{
			UserList user = (UserList) request.getSession().getAttribute("USER");
			
			try {
				
				if (ActivityDateValidation(DateUtility.dateformatWithOutTime(planStartDate),DateUtility.dateformatWithOutTime(planEndDate))) {
					return jTableSingleResponse = new JTableSingleResponse("INFORMATION",
							"Planned End date should be less than or equal to Planned Start date");
				}
				
				boolean errorFlagActivity = activityService.getActivityByWPIdandActivityName(destActWorkpackageId, newActivityName);
				if(errorFlagActivity){
					return jTableSingleResponse = new JTableSingleResponse("INFORMATION","Activity Name Already Exists For Same WorkPackage!");
				}
				
				activityWorkPackage=activityWorkPackageService.getActivityWorkPackageById(destActWorkpackageId,0);
				
				Date wpStDate = activityWorkPackage.getPlannedStartDate();
				Date wpEndDate = activityWorkPackage.getPlannedEndDate();
				Date actStDate = DateUtility.dateformatWithOutTime(planStartDate);
				Date actEndDate = DateUtility.dateformatWithOutTime(planEndDate);
				
				String errmsg="Please select Start Date & End Date between WorkPackage Date "+activityWorkPackage.getPlannedStartDate().toString().replaceAll("00:00:00.0", "")+"  and "+wpEndDate.toString().replaceAll("00:00:00.0", "");
				
				if(actStDate!=null || actEndDate!=null)
				{
				 Integer dateresult = DateUtility.ActivityDateValidation(actStDate, actEndDate,wpStDate, wpEndDate);
						if (dateresult == 1) {
							return jTableSingleResponse = new JTableSingleResponse("INFORMATION",errmsg);
						} else if (dateresult == 2) {
							return jTableSingleResponse = new JTableSingleResponse("INFORMATION",errmsg);
						} else
							 jTableSingleResponse = new JTableSingleResponse("OK");
				}
				if(activityWorkPackage != null ) {
					destProductId=activityWorkPackage.getProductBuild().getProductVersion().getProductMaster().getProductId();
				}
				
				Activity activity=activityService.getActivityById(movingActivityId, 1);
				productId = activity.getActivityWorkPackage().getProductBuild().getProductVersion().getProductMaster().getProductId();
				
				String comments= "Activity Move to "+activity.getActivityWorkPackage().getActivityWorkPackageName()+" workpackage to "+activityWorkPackage.getActivityWorkPackageName();
				comments =comments+" "+description;
				if(!destProductId.equals(productId)) {
					return jTableSingleResponse = new JTableSingleResponse("INFORMATION","Please move Same Product!");
				}
				if(activity!=null){
					Activity newActivityObj=activity;
					newActivityObj.setActivityName(newActivityName);
					newActivityObj.setActivityWorkPackage(activityWorkPackage);
					newActivityObj.setCreatedBy(user);
					newActivityObj.setIsActive(1);
					
					newActivityObj.setBaselineStartDate(newActivityObj.getPlannedStartDate());
					newActivityObj.setBaselineEndDate(newActivityObj.getPlannedEndDate());
				
					activityService.updateActivity(newActivityObj);// update activity
					//Update Workflow event information
					workflowEventService.setInitialInstanceEvent(destProductId, 33, newActivityObj.getActivityMaster().getActivityMasterId(), newActivityObj.getActivityId(), newActivityObj.getWorkflowStatus().getWorkflowStatusId(), null, 0, comments, user);
					jTableSingleResponse = new JTableSingleResponse("OK",new JsonActivity(activity));
					mongoDBService.addActivitytoMongoDB(newActivityObj.getActivityId());
					int userId=user.getUserId();
					UserList userObj = userListService.getUserListById(userId);
					eventsService.addNewEntityEvent(IDPAConstants.ENTITY_ACTIVITY, newActivityObj.getActivityId(), newActivityObj.getActivityName(), userObj);
					
					
					
				}
				
			} catch (Exception e) {
				log.error("Error:::"+e.getMessage());
			}
					
		
		}catch(Exception e){
			log.error("Error...",e);
		}
	    return jTableSingleResponse;

	}
		
		
		
		@RequestMapping(value = "process.activityworkpackage.move", method = RequestMethod.POST, produces = "application/json")
		public @ResponseBody
		JTableSingleResponse movingActivityWorkpackage(HttpServletRequest request, @RequestParam Integer srcProductBuildId, @RequestParam Integer destProductBuildId, @RequestParam Integer  workpackageId,@RequestParam String newWorkpackageName, @RequestParam String planStartDate, @RequestParam String planEndDate, @RequestParam String description) {
			JTableSingleResponse jTableSingleResponse=null;
		    ActivityWorkPackage activityWorkPackage =null;
		    ProductBuild productBuild = null;
		    List<Activity> activitieslist = null;
		    Integer productId = 0;
		    Integer destProductId=0;
			try{
				activitieslist = activityService.listAllActivitiesByActivityWorkPackageId(workpackageId,0,1);
				UserList user = (UserList) request.getSession().getAttribute("USER");
				productBuild=productListService.getProductBuildById(destProductBuildId, 0);
				activityWorkPackage=activityWorkPackageService.getActivityWorkPackageById(workpackageId,0);
				destProductId=productBuild.getProductVersion().getProductMaster().getProductId();
				productId = activityWorkPackage.getProductBuild().getProductVersion().getProductMaster().getProductId();
				
				String sourceWorkpackageName=activityWorkPackage.getActivityWorkPackageName();
				
				String comments= "Activity Move to "+sourceWorkpackageName+" workpackage to "+newWorkpackageName;
				
				if(!destProductId.equals(productId)) {
					return jTableSingleResponse = new JTableSingleResponse("INFORMATION","Please move Same Product!");
				}
				
				activityWorkPackage.setActivityWorkPackageId(null);
				activityWorkPackage.setActivityWorkPackageName(newWorkpackageName);
				activityWorkPackage.setPlannedStartDate(new Date(planStartDate));
				activityWorkPackage.setPlannedEndDate(new Date(planEndDate));
				activityWorkPackage.setProductBuild(productBuild);
				activityWorkPackage.setCreatedDate(new Date());
				activityWorkPackage.setModifiedDate(new Date());
				activityWorkPackage.setCreatedBy(user);
				
				UserList owner = activityWorkPackage.getOwner();
				
				activityWorkPackage.setIsActive(1);		
				
				activityWorkPackage.setBaselineStartDate(activityWorkPackage.getPlannedStartDate());
				activityWorkPackage.setBaselineEndDate(activityWorkPackage.getPlannedEndDate());
								
				ActivityWorkPackage wpByName = activityWorkPackageService.getActivityWorkPackageByName(newWorkpackageName, destProductBuildId);
				if(wpByName !=null && wpByName.getActivityWorkPackageName() != null){
					jTableSingleResponse = new JTableSingleResponse("ERROR","WorkPackage name already exists");
				}
				else{
					WorkflowStatus workflowStatus = null;
					if(activityWorkPackage.getWorkflowStatus() != null && activityWorkPackage.getWorkflowStatus().getWorkflowStatusId() != null && activityWorkPackage.getWorkflowStatus().getWorkflowStatusId() > 0){
						workflowStatus = workflowStatusService.getInitialStatusForInstanceByWorkflowId(productId, IDPAConstants.ENTITY_ACTIVITY_WORKPACKAGE_ID, null, activityWorkPackage.getWorkflowStatus().getWorkflow().getWorkflowId());						
					}else{
						workflowStatus = workflowStatusService.getWorkflowStatusById(-1);
					}
					activityWorkPackage.setWorkflowStatus(workflowStatus);
					activityWorkPackage.setOwner(owner);
				//	activityWorkPackage.setOwner(wpByName.getOwner());
					
					if(workflowStatus.getWorkflowStatusId() == -1){
						activityWorkPackage.setActualStartDate(new Date());
						activityWorkPackage.setActualEndDate(new Date());
					}
					activityWorkPackageService.addActivityWorkPackage(activityWorkPackage);//Adding activity workpackage
					jTableSingleResponse = new JTableSingleResponse("OK",new JsonActivityWorkPackage(activityWorkPackage));
					if(activityWorkPackage!=null && activityWorkPackage.getActivityWorkPackageId() != null)
					{
						Integer workflowId = 0;
						if(activityWorkPackage.getWorkflowStatus() != null && activityWorkPackage.getWorkflowStatus().getWorkflow() != null){
							workflowId = activityWorkPackage.getWorkflowStatus().getWorkflow().getWorkflowId();
						}
						workflowStatusPolicyService.instanciateWorkflowStatusAndPoliciesForInstanceByWorkflowId(productId, IDPAConstants.ENTITY_ACTIVITY_WORKPACKAGE_ID, null, activityWorkPackage.getActivityWorkPackageId(), workflowId, activityWorkPackage.getWorkflowStatus().getWorkflowStatusId(), user, activityWorkPackage.getPlannedStartDate(), activityWorkPackage);
						mongoDBService.addActivityWorkPackagetoMongoDB(activityWorkPackage.getActivityWorkPackageId());
						
						int userId=user.getUserId();
						UserList userObj = userListService.getUserListById(userId);
						//Entity Audition History 
						eventsService.addNewEntityEvent(IDPAConstants.ENTITY_ACTIVITY_WORKPACKAGE, activityWorkPackage.getActivityWorkPackageId(), activityWorkPackage.getActivityWorkPackageName(), userObj);
						
						//code to move activities under this workpackage
						
						try {
							activitieslist = activityService.listAllActivitiesByActivityWorkPackageId(workpackageId,0,1);
							if(activitieslist != null && activitieslist.size()>0){
								ActivityWorkPackage actWorkPackage=activityWorkPackageService.getActivityWorkPackageById(activityWorkPackage.getActivityWorkPackageId(),0);
								for(Activity activity: activitieslist){
									Activity newActivityObj=activity;
									newActivityObj.setActivityWorkPackage(actWorkPackage);
									newActivityObj.setIsActive(1);
									
									newActivityObj.setBaselineStartDate(newActivityObj.getPlannedStartDate());
									newActivityObj.setBaselineEndDate(newActivityObj.getPlannedEndDate());
									
						
									activityService.updateActivity(newActivityObj);// moving activity 
									
									//Update Workflow event information
									workflowEventService.setInitialInstanceEvent(destProductId, 33, newActivityObj.getActivityMaster().getActivityMasterId(), newActivityObj.getActivityId(), newActivityObj.getWorkflowStatus().getWorkflowStatusId(), null, 0, comments, user);
									mongoDBService.addActivitytoMongoDB(newActivityObj.getActivityId());
									eventsService.addNewEntityEvent(IDPAConstants.ENTITY_ACTIVITY, newActivityObj.getActivityId(), newActivityObj.getActivityName(), userObj);
									
								}//Ending Activity for loop
								comments =comments+" "+description;
							}
						} catch (Exception e) {
							log.error("Error:::"+e.getMessage());
						}
						
					}
				}
				
			}catch(Exception e){
				log.error("Error...",e);
			}
		    return jTableSingleResponse;

		}
		
		
		@RequestMapping(value = "process.activity.poke.notification", method = RequestMethod.POST, produces = "application/json")
		public @ResponseBody JTableSingleResponse activityPokeNotification(HttpServletRequest request, @RequestParam Integer activityId, @RequestParam String message, @RequestParam String messageType, @RequestParam String ccMailIds, @RequestParam String toMailIds) {
			JTableSingleResponse jTableSingleResponse=null;
			String EMAIL_REGEX = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$";
			List<String> incorrectCCMailList = new ArrayList<String>();
			List<String> correctCCMailList = new ArrayList<String>();
			List<String> incorrectToMailList = new ArrayList<String>();
			List<String> correctToMailList = new ArrayList<String>();
			String ccMails[]=null;
			String toMails[]=null;
			String invalidMails="";
			try {
				if(ccMailIds != null && ccMailIds !=""){					
					ccMails=ccMailIds.trim().split(";");
						for(String mail:ccMails){
							if(mail.matches(EMAIL_REGEX)){
								correctCCMailList.add(mail);
							}else{
								incorrectCCMailList.add(mail);
							}							
						}
						if(!incorrectCCMailList.isEmpty() && incorrectCCMailList.size()>0){
							for(String incorrectMail:incorrectCCMailList){
								invalidMails+=incorrectMail+",";
							}
							invalidMails=invalidMails.substring(0, invalidMails.length()-1);
							return jTableSingleResponse = new JTableSingleResponse("ERROR","Invalid Mail Id(s): "+invalidMails+".Please enter valid Mail Id's");
						}
				}
				
				if(toMailIds != null && toMailIds !=""){					
					toMails=toMailIds.trim().split(";");
						for(String mail:toMails){
							if(mail.equals(IDPAConstants.ASSIGNEE) || mail.equals(IDPAConstants.REVIEWER)){
								correctToMailList.add(mail);
							}
							else if(mail.matches(EMAIL_REGEX)){
								correctToMailList.add(mail);
							}else{
								incorrectToMailList.add(mail);
							}							
						}
						if(!incorrectToMailList.isEmpty() && incorrectToMailList.size()>0){
							for(String incorrectMail:incorrectToMailList){
								invalidMails+=incorrectMail+",";
							}
							invalidMails=invalidMails.substring(0, invalidMails.length()-1);
							return jTableSingleResponse = new JTableSingleResponse("ERROR","Invalid Mail Id(s): "+invalidMails+".Please enter valid Mail Id's");
						}
				}
				if(message.trim() == null || message.trim().equals("")){
					return jTableSingleResponse = new JTableSingleResponse("ERROR","Please enter a message before submitting notification");
				}
				UserList user = (UserList) request.getSession().getAttribute("USER");
				activityService.processActivityPokeNotification(activityId,user,message,messageType,correctCCMailList,correctToMailList);
				jTableSingleResponse = new JTableSingleResponse("SUCCESS", "Poke Notification successfully sent");
			}catch(Exception e) {
				jTableSingleResponse = new JTableSingleResponse("ERROR","Error while sending poke notification");
	            log.error("JSON ERROR", e);
			}
			return jTableSingleResponse;
			
		}		
		@RequestMapping(value="process.get.activity.by.id",method=RequestMethod.POST ,produces="application/json")
	    public @ResponseBody JTableSingleResponse getActivityFromDB(HttpServletRequest request, @RequestParam Integer activityId) {
			log.info("inside process.get.activity.by.id");
			JTableSingleResponse jTableSingleResponse=null;			
			try {
				Activity activityFromDB = activityService.getByActivityId(activityId);			
				if(activityFromDB != null ){
					List<JsonActivity> jsonActivities = new ArrayList<JsonActivity>();
					JsonActivity jsonActivity = new JsonActivity(activityFromDB);
					jsonActivities.add(jsonActivity);
					TestFactory testFactory = activityFromDB.getActivityWorkPackage().getProductBuild().getProductVersion().getProductMaster().getTestFactory();
					ProductMaster productMaster = activityFromDB.getActivityWorkPackage().getProductBuild().getProductVersion().getProductMaster();
					UserList user = (UserList) request.getSession().getAttribute("USER");
					workflowStatusPolicyService.setInstanceIndicators(testFactory.getTestFactoryId(),productMaster.getProductId(),activityFromDB.getActivityWorkPackage().getActivityWorkPackageId(), IDPAConstants.ENTITY_ACTIVITY_TYPE, jsonActivities, IDPAConstants.ENTITY_ACTIVITY_ID,user, activityFromDB.getModifiedDate(), activityFromDB.getActivityMaster().getActivityMasterId(), activityFromDB.getActivityId(),IDPAConstants.ENABLE_PENDING_WITH_COLUMN);
					jTableSingleResponse = new JTableSingleResponse("OK", jsonActivities.get(0));
				}else{
						return jTableSingleResponse = new JTableSingleResponse("INFORMATION","Unable to get the activity !");
				}
			} catch (Exception e) {
				jTableSingleResponse = new JTableSingleResponse("ERROR","Error for getting Activity!");
	            log.error("JSON ERROR", e);
	        }
		        
	        return jTableSingleResponse;
	    }		
		@RequestMapping(value = "my.activity.list", method = RequestMethod.POST, produces = "application/json")
		public @ResponseBody JTableResponse myActivityList(HttpServletRequest request, @RequestParam Integer testFactoryId, @RequestParam Integer productId,@RequestParam Integer activityWorkPackageId, @RequestParam Integer tabValue,@RequestParam Integer jtStartIndex,@RequestParam Integer jtPageSize) {
			JTableResponse jTableResponse=null;
			try {
				UserList user = (UserList) request.getSession().getAttribute("USER");
				List<Activity> activities = new ArrayList<Activity>();
				List<JsonActivity> jsonActivities =null;
				/*Tab value 0- Assignee,1-Pending With*/
				
				if(user != null && user.getUserId() == 1) {
					
					return jTableResponse = new JTableResponse("OK", jsonActivities, jsonActivities.size());
					
					
				}
				if(tabValue.equals(0)){
					activities = activityService.listMyActivitiesByLoginIdAndProductId(user.getUserId(),testFactoryId, productId,activityWorkPackageId);
				}else if(tabValue.equals(1)){
					activities= workflowActivityDAO.listActivitiesforWorkFlowForUserOrRole(testFactoryId,productId,activityWorkPackageId, null, IDPAConstants.ENTITY_ACTIVITY_TYPE, null, user.getUserId(), null, jtStartIndex, jtPageSize);
				} else if(tabValue.equals(2)){
					activities=workflowActivityDAO.listActivitiesforWorkFlowStatusPartForUserOrRole(testFactoryId,productId,activityWorkPackageId, IDPAConstants.ENTITY_ACTIVITY_TYPE, user.getUserId(),null);
				}
				
				jsonActivities = activityService.listMyActivities(request,activities,testFactoryId,productId,jtStartIndex,jtPageSize);
				
				jTableResponse = new JTableResponse("OK", jsonActivities, jsonActivities.size());
				
			}catch(Exception e) {
				jTableResponse = new JTableResponse("ERROR",
						"Error Fetching Activity Summary records!");
				log.error("JSON ERROR Fetching Activity Summary", e);
			}
			return jTableResponse;
			
		}
		
		@RequestMapping(value="get.activityworkpackage.by.engagement.product.list",method=RequestMethod.POST ,produces="application/json")
		public @ResponseBody JTableResponse getAllWorkpackagesForEngamentIdAndProductId(@RequestParam Integer testFactoryId,@RequestParam Integer productId) {
			log.debug("get.activityworkpackage.by.engagement.product.list");
			JTableResponse jTableResponse=null;
				try {	
					List<Object[]> workpackages=activityWorkPackageService.getActivityWorkpackageByTestFactoryIdProductId(testFactoryId, productId);
					JSONArray unMappedJsonArray = new JSONArray();
					if(workpackages != null && workpackages.size()>0) {
						for (Object[] row : workpackages) {
							JSONObject jsobj =new JSONObject();
							jsobj.put(IDPAConstants.ITEM_ID, (Integer)row[0]);
							jsobj.put(IDPAConstants.ITEM_NAME, (String)row[1]);	
							unMappedJsonArray.add(jsobj);					
						}				
					}
					jTableResponse = new JTableResponse("OK", unMappedJsonArray,unMappedJsonArray.size());
				
				} catch (Exception e) {
		            jTableResponse = new JTableResponse("ERROR","Error fetching UnMappedRCNList for Activity!");
		            log.error("JSON ERROR", e);
		        }
		    return jTableResponse;
		}
		
		@RequestMapping(value = "process.multiselection.activity.move", method = RequestMethod.POST, produces = "application/json")
		public @ResponseBody
		JTableSingleResponse multiSelectionActivityMove(HttpServletRequest request,Integer activityId, @RequestParam Integer destActWorkpackageId) {
		JTableSingleResponse jTableSingleResponse=null;
		log.info("movingActivityId "+activityId);
		log.info("destination WorkpackageId: "+destActWorkpackageId);
	    ActivityWorkPackage activityWorkPackage =null;
	    Integer productId = 0;
	    Integer destProductId=0;
		try{
			UserList user = (UserList) request.getSession().getAttribute("USER");
			
			try {
				
				activityWorkPackage=activityWorkPackageService.getActivityWorkPackageById(destActWorkpackageId,0);
				
				if(activityWorkPackage != null ) {
					destProductId=activityWorkPackage.getProductBuild().getProductVersion().getProductMaster().getProductId();
				}
				
				Activity activity=activityService.getActivityById(activityId, 0);
				productId = activity.getActivityWorkPackage().getProductBuild().getProductVersion().getProductMaster().getProductId();
				
				String comments= "Activity Move to "+activity.getActivityWorkPackage().getActivityWorkPackageName()+" workpackage to "+activityWorkPackage.getActivityWorkPackageName();
				if(!destProductId.equals(productId)) {
					return jTableSingleResponse = new JTableSingleResponse("INFORMATION","Please move Same Product!");
				}
				if(activity!=null){
					Activity newActivityObj=activity;
					newActivityObj.setActivityWorkPackage(activityWorkPackage);
					newActivityObj.setPlannedStartDate(activityWorkPackage.getPlannedStartDate());
					newActivityObj.setPlannedEndDate(activityWorkPackage.getPlannedEndDate());
					newActivityObj.setModifiedDate(new Date());
					newActivityObj.setCreatedBy(user);
					newActivityObj.setIsActive(1);
					
					newActivityObj.setBaselineStartDate(newActivityObj.getPlannedStartDate());
					newActivityObj.setBaselineEndDate(newActivityObj.getPlannedEndDate());
				
					activityService.updateActivity(newActivityObj);// update activity
					//Update Workflow event information
					workflowEventService.setInitialInstanceEvent(destProductId, IDPAConstants.ENTITY_ACTIVITY_TYPE, newActivityObj.getActivityMaster().getActivityMasterId(), newActivityObj.getActivityId(), newActivityObj.getWorkflowStatus().getWorkflowStatusId(), null, 0, comments, user);
					jTableSingleResponse = new JTableSingleResponse("OK",new JsonActivity(activity));
					mongoDBService.addActivitytoMongoDB(newActivityObj.getActivityId());
					int userId=user.getUserId();
					UserList userObj = userListService.getUserListById(userId);
					eventsService.addNewEntityEvent(IDPAConstants.ENTITY_ACTIVITY, newActivityObj.getActivityId(), newActivityObj.getActivityName(), userObj);
					
				}
				
			} catch (Exception e) {
				log.error("Error in multiSelectionActivityMove",e);
			}
					
		
		}catch(Exception e){
			log.error("Error...",e);
		}
	    return jTableSingleResponse;

	}
		
		@RequestMapping(value="map.activities.by.activityWorkPackageId",method=RequestMethod.POST ,produces="application/json")
		public @ResponseBody JTableResponse mapActivitiesByWorkpackageId(@RequestParam Integer activityWorkPackageId) {
			log.debug("map.activity.by.activityWorkPackageId");
			JTableResponse jTableResponse=null;
				try {	
					List<Activity> activities=activityService.getActivityByActivtyWorkpackageIdNotCompletedStage(activityWorkPackageId);
					JSONArray unMappedJsonArray = new JSONArray();
					String workflowStatus="";
					if(activities != null && activities.size() >0) {
						for (Activity row : activities) {
							JSONObject jsobj =new JSONObject();
							workflowStatus=row.getWorkflowStatus() != null? row.getWorkflowStatus().getWorkflowStatusName():"No Status ";
							jsobj.put(IDPAConstants.ITEM_ID, row.getActivityId());
							jsobj.put(IDPAConstants.ITEM_NAME, row.getActivityName()+" ~ ["+workflowStatus+"]");	
							unMappedJsonArray.add(jsobj);					
						}				
					}
					jTableResponse = new JTableResponse("OK", unMappedJsonArray,unMappedJsonArray.size());
				
				} catch (Exception e) {
		            jTableResponse = new JTableResponse("ERROR","Error fetching UnMappedRCNList for Activity!");
		            log.error("JSON ERROR", e);
		        }
		    return jTableResponse;
		}	
		
		@RequestMapping(value="unmap.activities.by.productIdandworkpackageId",method=RequestMethod.POST ,produces="application/json")
		public @ResponseBody JTableResponse listActivitiesWithProductIdAndWorkpackageId(@RequestParam Integer engagementId,@RequestParam Integer productId,@RequestParam Integer activityWorkPackageId,Integer startIndex,Integer pageSize) {
			log.debug("unmap.activities.by.productIdandworkpackageId");
			JTableResponse jTableResponse=null;
				try {	
					
					List<Activity> activities = activityService.listActivitiesByProductIdAndWorkpackageId(engagementId, productId, activityWorkPackageId, 0, 1, startIndex, pageSize);
					JSONArray unMappedJsonArray = new JSONArray();
					if(activities != null && activities.size() >0) {
						String workflowStatus="";
						for (Activity row : activities) {
							workflowStatus=row.getWorkflowStatus() != null? row.getWorkflowStatus().getWorkflowStatusName():"No Status ";
							JSONObject jsobj =new JSONObject();
							jsobj.put(IDPAConstants.ITEM_ID, row.getActivityId());
							jsobj.put(IDPAConstants.ITEM_NAME, row.getActivityName()+" ~ ["+workflowStatus+"]");	
							unMappedJsonArray.add(jsobj);					
						}				
					}
					jTableResponse = new JTableResponse("OK", unMappedJsonArray,unMappedJsonArray.size());
				
				} catch (Exception e) {
		            jTableResponse = new JTableResponse("ERROR","Error fetching UnMappedRCNList for Activity!");
		            log.error("JSON ERROR", e);
		        }
		    return jTableResponse;
		}
		
		
		@RequestMapping(value="unmap.activities.by.productIdandworkpackageId.count",method=RequestMethod.POST ,produces="application/json")
		public @ResponseBody JTableSingleResponse listActivitiesWithProductIdAndWorkpackageIdCount(@RequestParam Integer engagementId,@RequestParam Integer productId,@RequestParam Integer activityWorkPackageId,Integer startIndex,Integer pageSize) {
			
			JTableSingleResponse jTableSingleResponse;
			int unMappedActivityCount = 0;
			JSONObject unMappedActivityCountObj =new JSONObject();
			try {	
				List<Activity> activities = activityService.listActivitiesByProductIdAndWorkpackageId(engagementId, productId, activityWorkPackageId, 0, 1, startIndex, pageSize);
				if(activities!=null & activities.size() >0) {
					unMappedActivityCount=activities.size();
				} else {
					unMappedActivityCount=0;
				}
				
				unMappedActivityCountObj.put("unMappedTCCount", unMappedActivityCount);						
				jTableSingleResponse = new JTableSingleResponse("OK",unMappedActivityCountObj);
		        } catch (Exception e) {
		            jTableSingleResponse = new JTableSingleResponse("ERROR","Unable to update the ChangeRequest  & Activty association!");
		            log.error("JSON ERROR updating the ChangeRequest  & Activty association", e);	 
		        }
		        
		    return jTableSingleResponse;
		}
		
		@RequestMapping(value="workpackage.type.option.byProductId",method=RequestMethod.POST ,produces="application/json")
		public @ResponseBody JTableResponseOptions getWorkpackageByProductId(@RequestParam Integer productId) {
			log.info("workpackage.type.option.byProductId");
			JTableResponseOptions jTableResponseOptions;
			List<JsonActivityWorkPackage> jsonWorkPackageList = new ArrayList<JsonActivityWorkPackage>();
			try {	
				List<ProductBuild> productBuildList=null;
				List<ActivityWorkPackage> activityWorkPackageList=null;
				List<ProductVersionListMaster> productVersionList=productVersionListMasterDAO.list(productId);
				if(productVersionList != null && productVersionList.size() >0) {
					for(ProductVersionListMaster productVersion:productVersionList) {
						
							productBuildList=productBuildDAO.list(productVersion.getProductVersionListId());
							
							if(productBuildList != null && productBuildList.size() >0) {
								for(ProductBuild build:productBuildList) {
									
									activityWorkPackageList =activityWorkPackageService.getActivityWorkPackageByBuildId(build.getProductBuildId());
									
									if(activityWorkPackageList != null && activityWorkPackageList.size() >0) {
										for(ActivityWorkPackage activityWorkPackage:activityWorkPackageList) {
											
											jsonWorkPackageList.add(new JsonActivityWorkPackage(activityWorkPackage));
											
										}
									}
								
								}
							}
						
					}
				}
				
				jTableResponseOptions = new JTableResponseOptions("OK", jsonWorkPackageList, true);			
		        } catch (Exception e) {
		        	jTableResponseOptions = new JTableResponseOptions("ERROR","Error obtaining WorkPackages !");
		            log.error("JSON ERROR", e);
		        }	        
		    return jTableResponseOptions;
		}
		
		
		
		@RequestMapping(value="activity.mapping.by.entity.relation",method=RequestMethod.POST ,produces="application/json")
		public @ResponseBody JTableResponse getActivitesByEntityMappingInActivityMapping(@RequestParam Integer activityId) {
			log.debug("activity.mapping.by.entity.relation");
			JTableResponse jTableResponse=null;
			Integer entityInstanceId1 = null;
			List<EntityRelationship> entityRelationShipList = null;
			List<JsonEntityRelationship> jsonEntityRelationShipList = new ArrayList<JsonEntityRelationship>();
				try {	
					entityInstanceId1 = activityId;					
					entityRelationShipList =	entityRelationshipService.getEntityRelationShipByEntityInstanceId(entityInstanceId1);
					if(entityRelationShipList != null && entityRelationShipList.size() != 0 ){
						JsonEntityRelationship jsonEntityRelationShip = new JsonEntityRelationship(entityRelationShipList.get(0));
						jsonEntityRelationShipList.add(jsonEntityRelationShip);
					}
					
					jTableResponse = new JTableResponse("OK",jsonEntityRelationShipList,jsonEntityRelationShipList.size());
				
				} catch (Exception e) {
		            jTableResponse = new JTableResponse("ERROR","Error In getActivitesByEntityMappingInActivityMapping!");
		            log.error("JSON ERROR", e);
		        }
		    return jTableResponse;
		}	
		
		
		@RequestMapping(value="get.activity.by.activityid",method=RequestMethod.POST ,produces="application/json")
		public @ResponseBody JTableResponse getAcitivityById(@RequestParam Integer activityId) {
			log.debug("get.activity.by.activityid");
			JTableResponse jTableResponse=null;
			Activity activity = null;
			List<JsonActivity> josnActivityList = new ArrayList<JsonActivity>();
			JsonActivity jsonActivity = null;
			
				try {	
						activity = activityService.getActivityById(activityId,0);
						if(activity != null){
							jsonActivity = new JsonActivity(activity);
							josnActivityList.add(jsonActivity);
					}
					
					jTableResponse = new JTableResponse("OK",josnActivityList,josnActivityList.size());
				
				} catch (Exception e) {
		            jTableResponse = new JTableResponse("ERROR","Error In getting Activity!");
		            log.error("JSON ERROR", e);
		        }
		    return jTableResponse;
		}	

		@RequestMapping(value="get.Activities.for.gantt.by.activityWorkPackageId",method=RequestMethod.POST ,produces="application/json")
		public @ResponseBody JTableResponse getAcitivitiesForGanttByActivityWorkpackageId(HttpServletRequest req,@RequestParam Integer activityWorkPackageId) {
			JTableResponse jTableResponse=null;
			Map<String, String> searchStrings = new HashMap<String, String>();
			List<Map<String,Object>> activityGanttList = new ArrayList<Map<String,Object>>();
			Map<String,Object> ganttTasks = new HashMap<String,Object>();
			List<JsonActivityGantt> tasks = new ArrayList<JsonActivityGantt>();
			List<String> childrens = new ArrayList<String>();
			
			Map<String,String> progress = null;
			Map<String,String> dependency = null;
			
			
			
			try {
				List<JsonActivity> jsonActivityList = activityService.listActivities(searchStrings,0,0, 0, 0,activityWorkPackageId, 0,1,req,"All",0,1000);
				if(jsonActivityList != null && jsonActivityList.size() >0) {
					
					for(JsonActivity jsonActivity :jsonActivityList ) {
						
						JsonActivityGantt jsonActivityGantt = new JsonActivityGantt(jsonActivity);
						progress = new HashMap<String,String>();
						progress.put("percent", jsonActivity.getPercentageCompletion().toString());
						progress.put("color", jsonActivity.getProgressIndicator());
						jsonActivityGantt.setProgress(progress);
						tasks.add(jsonActivityGantt);
						childrens.add(jsonActivity.getActivityName());
						if( jsonActivity.getActivityPredecessors() != null && !jsonActivity.getActivityPredecessors().trim().isEmpty()) {
							List<Map<String,String>> dependencies = new ArrayList<Map<String,String>>();
							String [] predecessors= jsonActivity.getActivityPredecessors().split(",");
							for(String activityId : predecessors) {
								dependency = new HashMap<String,String>();
								Activity activity=activityService.getActivityById(Integer.parseInt(activityId), 1);
								if(activity != null) {
									dependency.put("from", activity.getActivityId().toString());
									dependencies.add(dependency);
								}
							}
							jsonActivityGantt.setDependencies(dependencies);
						}
					}
					
					if(jsonActivityList != null && jsonActivityList.size() >0) { 
						String activityWorkpackageName= jsonActivityList.get(0).getActivityWorkPackageName();
						ganttTasks.put("name", activityWorkpackageName);
						ganttTasks.put("tasks",tasks);
						ganttTasks.put("children",childrens);
						activityGanttList.add(ganttTasks);
					}
				}
				jTableResponse = new JTableResponse("OK",activityGanttList,activityGanttList.size());
			}catch(Exception e) {
				log.error("Error in getAcitivitiesForGanttByActivityWorkpackageId",e);
			}
			   return jTableResponse;
		}
		
		@RequestMapping(value = "process.activity.gantt.update.planDates", method = RequestMethod.POST, produces = "application/json")
		public @ResponseBody
		JTableResponse updateActivityPlanDates(HttpServletRequest request,@RequestParam Integer activityId,@RequestParam String startDate,@RequestParam String endDate) {
			JTableResponse jTableResponse=null;
			try {
				Activity activity = activityService.getActivityById(activityId, 1);
				if(activity != null) {
					if( startDate != "" && !startDate.isEmpty()) {
						activity.setPlannedStartDate(DateUtility
							.dateformatWithOutTime(startDate));
					}
					if(endDate != "" && !endDate.isEmpty()) {
						activity.setPlannedEndDate(DateUtility
							.dateformatWithOutTime(endDate));
					}
					activityService.updateActivity(activity);
					jTableResponse = new JTableResponse("OK","PlanDates Updated Successfully!");
				} else {
					jTableResponse = new JTableResponse("ERROR","Activity is not available");
				}
			}catch(Exception e) {
				log.error("Error in updateActivityPlanDates",e);
			}
			return jTableResponse;
		}
		
		@RequestMapping(value="get.wpOwner.mailId.by.actWPId",method=RequestMethod.POST ,produces="application/json")
		public @ResponseBody JTableResponse getWorkpackageOwnerByActivityId(@RequestParam Integer activityWPId) {
			JTableResponse jTableResponse = null;
			ActivityWorkPackage activityWorkPackage=null;
			List<JsonActivityWorkPackage> jsonActivityWorkPackageList = new ArrayList<JsonActivityWorkPackage>(); 
			try {
				activityWorkPackage = activityWorkPackageService.getActivityWorkPackageById(activityWPId, 1);
				if(activityWorkPackage != null){					
					jsonActivityWorkPackageList.add(new JsonActivityWorkPackage(activityWorkPackage));
					jTableResponse = new JTableResponse("OK", jsonActivityWorkPackageList);
				}
			} catch (Exception e) {
				jTableResponse = new JTableResponse("ERROR","Error in getWorkpackageOwnerByActivityId");
				log.error("JSON ERROR", e);
			}
			return jTableResponse;
		}
		
		
		@RequestMapping(value = "process.activity.list.count", method = RequestMethod.POST, produces = "application/json")
		public @ResponseBody
		JTableResponse getActivityListCount(HttpServletRequest req,@RequestParam int engagementId,@RequestParam int productId,@RequestParam int productVersionId,@RequestParam int productBuildId,@RequestParam int activityWorkPackageId,@RequestParam int isActive,
				@RequestParam int jtStartIndex, @RequestParam int jtPageSize) {
			log.info("Fetching getActivityListCount");
			JTableResponse jTableResponse;
			try {
				List<JsonActivity> jsonActivity = null;
				Map<String, String> searchStrings = new HashMap<String, String>();
				String searchassigneeId = req.getParameter("searchassigneeId");
				String searchpriority = req.getParameter("searchpriority");
				String searchstatusId = req.getParameter("searchstatusId");
				
				 searchStrings.put("searchassigneeId", searchassigneeId);
	            searchStrings.put("searchpriority", searchpriority);
	            searchStrings.put("searchstatusId", searchstatusId);

				 jTableResponse = new JTableResponse("OK", jsonActivity, activityService.getActivitiesCount(searchStrings,engagementId, productId, productVersionId, productBuildId, activityWorkPackageId, 0, isActive));    
			} catch (Exception e) {
				jTableResponse = new JTableResponse("ERROR",
						"Error Fetching Activity Summary records!");
				log.error("JSON ERROR Fetching Activity Summary", e);
			}
			return jTableResponse;
		}
		
}
