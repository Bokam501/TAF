package com.hcl.atf.taf.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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

import com.hcl.atf.taf.constants.IDPAConstants;
import com.hcl.atf.taf.dao.CommonDAO;
import com.hcl.atf.taf.model.Activity;
import com.hcl.atf.taf.model.ActivityTask;
import com.hcl.atf.taf.model.ActivityWorkPackage;
import com.hcl.atf.taf.model.ChangeRequest;
import com.hcl.atf.taf.model.ChangeRequestType;
import com.hcl.atf.taf.model.ClarificationTracker;
import com.hcl.atf.taf.model.EntityMaster;
import com.hcl.atf.taf.model.EntityRelationship;
import com.hcl.atf.taf.model.ProductBuild;
import com.hcl.atf.taf.model.ProductMaster;
import com.hcl.atf.taf.model.ProductVersionListMaster;
import com.hcl.atf.taf.model.StatusCategory;
import com.hcl.atf.taf.model.TestFactory;
import com.hcl.atf.taf.model.UserList;
import com.hcl.atf.taf.model.json.JsonChangeRequest;
import com.hcl.atf.taf.model.json.JsonChangeRequestType;
import com.hcl.atf.taf.model.json.jtable.JTableResponse;
import com.hcl.atf.taf.model.json.jtable.JTableResponseOptions;
import com.hcl.atf.taf.model.json.jtable.JTableSingleResponse;
import com.hcl.atf.taf.mongodb.service.MongoDBService;
import com.hcl.atf.taf.service.ActivityService;
import com.hcl.atf.taf.service.ActivityStatusService;
import com.hcl.atf.taf.service.ActivityTaskService;
import com.hcl.atf.taf.service.ActivityWorkPackageService;
import com.hcl.atf.taf.service.ChangeRequestService;
import com.hcl.atf.taf.service.ClarificationTrackerService;
import com.hcl.atf.taf.service.CommonService;
import com.hcl.atf.taf.service.EntityRelationshipService;
import com.hcl.atf.taf.service.EventsService;
import com.hcl.atf.taf.service.NotificationService;
import com.hcl.atf.taf.service.ProductListService;
import com.hcl.atf.taf.service.UserListService;
import com.hcl.atf.taf.service.WorkPackageService;

@Controller
public class ChangeManagementController {
	private static final Log log = LogFactory.getLog(ChangeManagementController.class);

	@Autowired
	private ChangeRequestService changeRequestService;
	@Autowired
	private ActivityService activityService;
	@Autowired
	private ActivityTaskService activityTaskService;
	@Autowired
	private ActivityStatusService activityStatusService;
	@Autowired
	private ProductListService productListService;
	@Autowired
	private EntityRelationshipService entityRelationshipService;
	@Autowired
	private ClarificationTrackerService clarificationTrackerService;
	@Autowired
	private UserListService userListService;
	@Autowired
	private WorkPackageService workPackageService;
	@Autowired
	private EventsService eventsService;
	@Autowired
	private CommonService commonService;
	@Autowired
	private CommonDAO commonDAO;
	@Autowired
	private MongoDBService mongoDBService;
	@Autowired
	private ActivityWorkPackageService activityWorkPackageService;

	@Autowired
	private NotificationService notificationService;
	@RequestMapping(value = "list.change.requests.by.activity", method = RequestMethod.POST, produces = "application/json")
	public @ResponseBody
	JTableResponse listChangeRequestsByActivity(@RequestParam Integer activityId) {
		log.debug("inside list.change.requests.by.activity");
		JTableResponse jTableResponse;
		try {
			List<JsonChangeRequest> jsonChangeRequestList = null;
			jsonChangeRequestList = changeRequestService.listChangeRequestsByActivityId(activityId, 1);
			jTableResponse = new JTableResponse("OK", jsonChangeRequestList);
			log.debug("inside process fetching Change Requests records ");
		} catch (Exception e) {
			jTableResponse = new JTableResponse("ERROR","Error fetching records!");
			log.error("JSON ERROR", e);
		}
		return jTableResponse;
	}


	@RequestMapping(value="list.changerequests.by.entityTypeAndInstanceId",method=RequestMethod.POST ,produces="application/json")
	public @ResponseBody JTableResponse getChangeRequestsMappedToActivity(@RequestParam Integer entityType1, @RequestParam Integer entityInstance1) {
		log.info("list.changerequestset.by.activity");
		JTableResponse jTableResponse;
		try {		
			if (entityInstance1 == null || ("null").equals(entityInstance1)) {		
	            jTableResponse = new JTableResponse("OK", null,0);
				return jTableResponse;
			}
			List<Object[]> attachmentCountDetails = commonDAO.getAttachmentCountOfEntity(IDPAConstants.CHANGE_REQUEST_ENTITY_MASTER_ID);
			List<ChangeRequest> ChangeRequestFromDB = changeRequestService.listChangeRequestByEntityTypeAndInstanceIds(entityType1, entityInstance1);	
			if (ChangeRequestFromDB == null) {				
				log.info("No mappings available for activityId");			
				jTableResponse = new JTableResponse("OK", null,0);
				return jTableResponse;
			}
			List<JsonChangeRequest> jsonChangeRequestList = new ArrayList<JsonChangeRequest>();
			for (ChangeRequest changeRequestList : ChangeRequestFromDB) {
				JsonChangeRequest jsonChangeRequest = new JsonChangeRequest(changeRequestList);
				jsonChangeRequest.setAttachmentCount(commonService.getCountFromAttachmentCountDetails(attachmentCountDetails, jsonChangeRequest.getChangeRequestId()));
				jsonChangeRequestList.add(jsonChangeRequest);
			}		
			jTableResponse = new JTableResponse("OK", jsonChangeRequestList,jsonChangeRequestList.size());
			ChangeRequestFromDB = null;  
		} catch (Exception e) {
	            jTableResponse = new JTableResponse("ERROR","Error fetching records!");
	            log.error("JSON ERROR", e);
	        }
	    return jTableResponse;
	}
	
	@RequestMapping(value="list.changerequestset.by.activityWP",method=RequestMethod.POST ,produces="application/json")
	public @ResponseBody JTableResponse getChangeRequestsMappedToActivityWP(@RequestParam Integer entityType1, @RequestParam Integer entityInstance1,  @RequestParam int jtStartIndex, @RequestParam int jtPageSize) {
		log.info("list.changerequestset.by.activityWP");
		JTableResponse jTableResponse;
		try {		
			if (entityInstance1 == null || ("null").equals(entityInstance1)) {		
	            jTableResponse = new JTableResponse("OK", null,0);
				return jTableResponse;
			}			
			List<JsonChangeRequest> jsonChangeRequestList = changeRequestService.listChangeRequestByActivityWorkPackageId( entityType1,  entityInstance1,
					 jtStartIndex,  jtPageSize,  -1);
			if (jsonChangeRequestList == null) {				
				log.info("No mappings available for ActivityWorkPackage");			
				jTableResponse = new JTableResponse("OK", null,0);
				return jTableResponse;
			}	
			jTableResponse = new JTableResponse("OK", jsonChangeRequestList,jsonChangeRequestList.size());
			jsonChangeRequestList = null;  
		} catch (Exception e) {
	            jTableResponse = new JTableResponse("ERROR","Error fetching records!");
	            log.error("JSON ERROR", e);
	        }
	    return jTableResponse;
	}
	

	@RequestMapping(value="list.changerequestset.by.product",method=RequestMethod.POST ,produces="application/json")
	public @ResponseBody JTableResponse getChangeRequestsMappedToProduct(@RequestParam Integer entityType1, @RequestParam Integer entityInstance1,  @RequestParam int jtStartIndex, @RequestParam int jtPageSize) {
		log.info("list.changerequestset.by.product");
		JTableResponse jTableResponse;
		try {		
			if (entityInstance1 == null || ("null").equals(entityInstance1)) {		
	            jTableResponse = new JTableResponse("OK", null,0);
				return jTableResponse;
			}			
			List<JsonChangeRequest> jsonChangeRequestList = changeRequestService.listChangeRequestByProductId( entityType1,  entityInstance1,
					 jtStartIndex,  jtPageSize,  -1);
			if (jsonChangeRequestList == null) {				
				log.info("No mappings available for ActivityWorkPackage");			
				jTableResponse = new JTableResponse("OK", null,0);
				return jTableResponse;
			}	
			jTableResponse = new JTableResponse("OK", jsonChangeRequestList,jsonChangeRequestList.size());
			jsonChangeRequestList = null;  
		} catch (Exception e) {
	            jTableResponse = new JTableResponse("ERROR","Error fetching records!");
	            log.error("JSON ERROR", e);
	        }
	    return jTableResponse;
	}
	@RequestMapping(value = "changerequests.add.by.entityTypeAndInstanceId", method = RequestMethod.POST, produces = "application/json")
	public @ResponseBody JTableSingleResponse addActivityTask(HttpServletRequest req, @ModelAttribute JsonChangeRequest jsonChangeRequest,@RequestParam Integer entityInstanceId) {	
	JTableSingleResponse jTableSingleResponse;
		try {
			log.info("changerequests.add.by.entityTypeAndInstanceId");					
			ChangeRequest changeRequest = jsonChangeRequest.getChangeRequest();	
			if(changeRequest.getChangeRequestName() == null || changeRequest.getChangeRequestName() == "" ) {
				return jTableSingleResponse = new JTableSingleResponse("ERROR","Change requst name should not empty!");
			}
			
			StatusCategory statusCategory = new StatusCategory();
			statusCategory.setStatusCategoryId(1);
			changeRequest.setStatusCategory(statusCategory);
			
			ActivityWorkPackage activityWorkPackage = null;
			Activity activity=null;
			ActivityTask activityTask = null;
			ProductMaster productMaster=null;
			String subject = "ChangeRequest creation for an Entity";
			
			productMaster = productListService.getProductById(jsonChangeRequest.getProductId());
			
			Integer productId =0;     	            
	            if(productMaster != null){
	            	 productId=productMaster.getProductId();					    
					changeRequest.setProduct(productMaster);
					log.info("changerequests.add.by.entityTypeAndInstanceId ProductId"+changeRequest.getProduct().getProductId());
				}
	            changeRequest.setIsActive(1);
	            changeRequest.setEntityInstanceId(entityInstanceId);
	            if(changeRequest.getEntityType() == null){
	            	EntityMaster entityMaster = new EntityMaster();
	            	entityMaster.setEntitymasterid(jsonChangeRequest.getEntityType1());
	            	changeRequest.setEntityType(entityMaster);
	            }
	            	if(changeRequest.getPlanExpectedValue() == null){
	            		if(activity.getPlannedActivitySize() != null){
	            		changeRequest.setPlanExpectedValue(activity.getPlannedActivitySize());
	            		}
	            	}
	         
			Integer changeRequestId = changeRequestService.addChangeRequest(changeRequest);
			ChangeRequest changeReq = changeRequestService.getChangeRequestById(changeRequestId);
			if(changeRequest != null && changeRequest.getChangeRequestId() != null){
				mongoDBService.addChangeRequestToMongoDB(changeReq);
				UserList user=(UserList)req.getSession().getAttribute("USER");
				eventsService.addNewEntityEvent(IDPAConstants.ENTITY_CHANGE_REQUEST, changeRequest.getChangeRequestId(), changeRequest.getChangeRequestName(), user);
			}
		    
			if(changeRequestId != null){
				EntityRelationship entityRelationship = new EntityRelationship();
				if(jsonChangeRequest.getEntityType1() != null){
					entityRelationship.setEntityTypeId1(jsonChangeRequest.getEntityType1());
				}
				if(jsonChangeRequest.getEntityType2() != null){
					entityRelationship.setEntityTypeId2(jsonChangeRequest.getEntityType2());
				}
				if(jsonChangeRequest.getEntityInstance1() != null){
					entityRelationship.setEntityInstanceId1(jsonChangeRequest.getEntityInstance1());
				}				
				entityRelationship.setEntityInstanceId2(changeRequestId);				
				entityRelationship.setIsActive(1);
				entityRelationship.setRelationshipType(IDPAConstants.ENTITY_CHANGE_REQUEST);
				entityRelationshipService.addEntityRelationship(entityRelationship);
			}
									
			jTableSingleResponse = new JTableSingleResponse("OK",
					new JsonChangeRequest(changeReq));
			notificationService.processNotification(req, productId, IDPAConstants.NOTIFICATION_CHANGE_REQUEST_CREATION, changeRequest,subject);
			log.info("changerequests.add.by.entityTypeAndInstanceId Success");
		} catch (Exception e) {
			jTableSingleResponse = new JTableSingleResponse("ERROR",
					"Error adding ChangeRequest record!");
			log.error("JSON ERROR", e);
		}
		return jTableSingleResponse;
	}
	
	@RequestMapping(value = "changerequests.add", method = RequestMethod.POST, produces = "application/json")
	public @ResponseBody JTableSingleResponse addChangeRequest(HttpServletRequest req, @ModelAttribute JsonChangeRequest jsonChangeRequest) {	
	JTableSingleResponse jTableSingleResponse = null;
		try {
			log.info("changerequests.add");					
			ChangeRequest changeRequest = jsonChangeRequest.getChangeRequest();	
			Integer productId = jsonChangeRequest.getProductId();
			ProductMaster product = null;
			ClarificationTracker clarificationTracker = null;
			String subject ="Change Request Adding";
			if(productId != null){
				int entityTypeId = 18;
				product = productListService.getProductDetailsById(productId);
				changeRequest.setProduct(product);
				
				changeRequest.setEntityInstanceId(productId);
				EntityMaster entityMaster = new EntityMaster();
				if(jsonChangeRequest.getEntityType1() != null){					
					entityMaster.setEntitymasterid(jsonChangeRequest.getEntityType1());
					changeRequest.setEntityType(entityMaster);
				}else{					
					entityMaster.setEntitymasterid(entityTypeId);
					changeRequest.setEntityType(entityMaster);
				}
			}
			if(product != null){
				StatusCategory statusCategory = new StatusCategory();
				if(jsonChangeRequest.getStatusCategoryId() != null){
					statusCategory.setStatusCategoryId(jsonChangeRequest.getStatusCategoryId());
				}else{
					statusCategory.setStatusCategoryId(1);
				}
				changeRequest.setStatusCategory(statusCategory);
				
				changeRequest.setRaisedDate(new Date());
				changeRequest.setIsActive(1);
				Integer clarificationTrackerId = jsonChangeRequest.getEntityInstance1();
				if(clarificationTrackerId != null){
				 clarificationTracker = clarificationTrackerService.getClarificationTrackersById(clarificationTrackerId, 1);
						changeRequest.setEntityInstanceId(clarificationTrackerId);
						if(jsonChangeRequest.getEntityType1() != null){
							EntityMaster entityMaster = new EntityMaster();
							entityMaster.setEntitymasterid(jsonChangeRequest.getEntityType1());
							changeRequest.setEntityType(entityMaster);
						}
						if(changeRequest.getPlanExpectedValue() == null){							
							if(clarificationTracker.getPlanExpectedValue() != null){
								changeRequest.setPlanExpectedValue(clarificationTracker.getPlanExpectedValue());
							}							
						}
				}
				if(changeRequest.getPlanExpectedValue() == null){
					changeRequest.setPlanExpectedValue(1);
				}
				Integer entityInstance2 = changeRequestService.addChangeRequest(changeRequest);
				
				if(changeRequest != null && entityInstance2 != null){
					mongoDBService.addChangeRequestToMongoDB(entityInstance2);
					UserList user=(UserList)req.getSession().getAttribute("USER");
					eventsService.addNewEntityEvent(IDPAConstants.ENTITY_CHANGE_REQUEST, changeRequest.getChangeRequestId(), changeRequest.getChangeRequestName(), user);
				}
				if(entityInstance2 != null){
					EntityRelationship entityRelationship = new EntityRelationship();
					if(jsonChangeRequest.getEntityType1() != null){
					entityRelationship.setEntityTypeId1(jsonChangeRequest.getEntityType1());
					}
					if(jsonChangeRequest.getEntityType2() != null){
					entityRelationship.setEntityTypeId2(jsonChangeRequest.getEntityType2());
					}
					if(jsonChangeRequest.getEntityInstance1() != null){
					entityRelationship.setEntityInstanceId1(jsonChangeRequest.getEntityInstance1());
					}				
					entityRelationship.setEntityInstanceId2(entityInstance2);			
					entityRelationship.setIsActive(1);
					entityRelationship.setRelationshipType(IDPAConstants.ENTITY_CHANGE_REQUEST);
					entityRelationshipService.addEntityRelationship(entityRelationship);
				}
				
				jTableSingleResponse = new JTableSingleResponse("OK",new JsonChangeRequest(changeRequest));
				notificationService.processNotification(req, jsonChangeRequest.getProductId(), IDPAConstants.NOTIFICATION_CHANGE_REQUEST_CREATION, changeRequest,subject);
				log.info("changerequests.add successful");
			}
			
		} catch (Exception e) {
			jTableSingleResponse = new JTableSingleResponse("ERROR",
					"Error adding ChangeRequest record!");
			log.error("JSON ERROR", e);
		}
		return jTableSingleResponse;
	}
	
	
	@RequestMapping(value = "changerequests.update", method = RequestMethod.POST, produces = "application/json")
	public @ResponseBody JTableResponse updateWorkRequest(HttpServletRequest req, @ModelAttribute JsonChangeRequest jsonChangeRequest,
			BindingResult result) {
		log.info("changerequests.update");
		JTableResponse jTableResponse = null;
		ChangeRequest changeRequestUI = null;
		ProductMaster productMaster = null;
		TestFactory testFactory = null;
		String remarks = "";
		String subject = "Change Request Updation";
		if (result.hasErrors()) {
			jTableResponse = new JTableResponse("ERROR", "Invalid Form!");
		}
		try {
			changeRequestUI = jsonChangeRequest.getChangeRequest();						
			if(changeRequestUI.getIsActive() != null){
				changeRequestUI.setIsActive(changeRequestUI.getIsActive());
			}else{
				changeRequestUI.setIsActive(1);
			}
			
			Integer productId = jsonChangeRequest.getProductId();
			if(productId != null){
				int entityTypeId = 18;
				productMaster = productListService.getProductDetailsById(productId);
				testFactory = productMaster.getTestFactory();
				changeRequestUI.setProduct(productMaster);
				
				changeRequestUI.setEntityInstanceId(productId);
				EntityMaster entityMaster = new EntityMaster();
				if(jsonChangeRequest.getEntityType1() != null){					
					entityMaster.setEntitymasterid(jsonChangeRequest.getEntityType1());
					changeRequestUI.setEntityType(entityMaster);
				}else{					
					entityMaster.setEntitymasterid(entityTypeId);
					changeRequestUI.setEntityType(entityMaster);
				}
			}
			
			changeRequestService.updateChangeRequest(changeRequestUI);
			if(changeRequestUI.getChangeRequestId() != null){
			mongoDBService.addChangeRequestToMongoDB(changeRequestUI.getChangeRequestId());
			}			
			if(changeRequestUI != null && changeRequestUI.getChangeRequestId() != null){
				UserList user=(UserList)req.getSession().getAttribute("USER");	
				remarks = "TestFactory :"+testFactory.getTestFactoryName()+", Product :"+productMaster.getProductName()+", ChangeRequest :"+changeRequestUI.getChangeRequestName();
				eventsService.addEntityChangedEvent(IDPAConstants.ENTITY_CHANGE_REQUEST, changeRequestUI.getChangeRequestId(), changeRequestUI.getChangeRequestName(),
						jsonChangeRequest.getModifiedField(), jsonChangeRequest.getModifiedFieldTitle(),
						jsonChangeRequest.getOldFieldValue(), jsonChangeRequest.getModifiedFieldValue(), user, remarks);
			}
			
			List<JsonChangeRequest> jsonChangeRequestList = new ArrayList();
			jsonChangeRequestList.add(jsonChangeRequest);
			 jTableResponse = new JTableResponse("OK",jsonChangeRequestList ,1);
			 notificationService.processNotification(req, jsonChangeRequest.getProductId(), IDPAConstants.NOTIFICATION_CHANGE_REQUEST_CREATION, changeRequestUI,subject);
			 log.info("changerequests.update successful");
		} catch (Exception e) {
			jTableResponse = new JTableResponse("ERROR",
					"Unable to update the Change Request!");
			log.error("JSON ERROR", e);
		}
		return jTableResponse;
	}



@RequestMapping(value="unmapped.changeRequest.count",method=RequestMethod.POST ,produces="application/json")
public @ResponseBody JTableSingleResponse getUnMappedTestCaseListCountOfFeatureByProductFeatureId(@RequestParam int activityId) {
	JTableSingleResponse jTableSingleResponse;
	int unMappedRcn = 0;
	JSONObject unMappedRcnCountObj =new JSONObject();
	try {	
		ActivityWorkPackage activityWorkPackage = null;
		Activity activity=null;
		ProductBuild productBuild = null;
		ProductMaster productMaster=null;
		ProductVersionListMaster productVersion=null;
		   activity = activityService.getActivityById(activityId,1);	
		 activityWorkPackage = activity.getActivityWorkPackage();
            productBuild = activityWorkPackage.getProductBuild();
            productVersion = productBuild.getProductVersion();
            productMaster = productVersion.getProductMaster();	            
            Integer productId = productMaster.getProductId();		
	        unMappedRcn = activityService.getUnMappedRcnByActivityId(activityId,0,productId);			
	
		unMappedRcnCountObj.put("unMappedTCCount", unMappedRcn);						
		jTableSingleResponse = new JTableSingleResponse("OK",unMappedRcnCountObj);
        } catch (Exception e) {
            jTableSingleResponse = new JTableSingleResponse("ERROR","Unable to update the ChangeRequest  & Activty association!");
            log.error("JSON ERROR updating the ChangeRequest  & Activty association", e);	 
        }
        
    return jTableSingleResponse;
}	
@RequestMapping(value="unmapped.changerequest.list",method=RequestMethod.POST ,produces="application/json")
public @ResponseBody JTableResponse listUnMappedRcnWithActivityId(@RequestParam int productId,@RequestParam int activityId, @RequestParam int jtStartIndex, @RequestParam int jtPageSize) {
	log.debug("unmapped.changerequest.list");
	JTableResponse jTableResponse=null;
		try {	
			List<Object[]> unMappedRcnListObj = activityService.getUnMappedRcnByActivityId(productId,activityId, jtStartIndex, jtPageSize,0);
		
			JSONArray unMappedJsonArray = new JSONArray();
			for (Object[] row : unMappedRcnListObj) {
				JSONObject jsobj =new JSONObject();
				jsobj.put(IDPAConstants.ITEM_ID, (Integer)row[0]);
				jsobj.put(IDPAConstants.ITEM_NAME, (String)row[1]);	
				unMappedJsonArray.add(jsobj);					
			}				
			jTableResponse = new JTableResponse("OK", unMappedJsonArray,unMappedJsonArray.size());
			unMappedRcnListObj = null;					 
		
		} catch (Exception e) {
            jTableResponse = new JTableResponse("ERROR","Error fetching UnMappedRCNList for Activity!");
            log.error("JSON ERROR", e);
        }
    return jTableResponse;
}
@RequestMapping(value="changerequest.mapped.to.activity.list",method=RequestMethod.POST ,produces="application/json")
public @ResponseBody JTableResponse listRcnsOfActivity(@RequestParam String activityId) {
		int intactivityId = Integer.parseInt(activityId);
	JTableResponse jTableResponse;		
		try {	
				List<Object[]> mappedECListObj = activityService.getMappedRcnByActivityId(intactivityId, -1, -1,0);
				JSONArray mappedJsonArray = new JSONArray();
				for (Object[] row : mappedECListObj) {
					JSONObject jsobj =new JSONObject();
					jsobj.put(IDPAConstants.ITEM_ID, (Integer)row[0]);
					jsobj.put(IDPAConstants.ITEM_NAME, (String)row[1]);		
					mappedJsonArray.add(jsobj);					
				}				
				jTableResponse = new JTableResponse("OK", mappedJsonArray,mappedJsonArray.size());
				mappedECListObj = null;
        } catch (Exception e) {
            jTableResponse = new JTableResponse("ERROR","Error fetching RCN mapped to Activity!");
            log.error("JSON ERROR", e);
        }	        
    return jTableResponse;
}
@RequestMapping(value="activity.changerequest.mapping",method=RequestMethod.POST ,produces="application/json")
public @ResponseBody JTableSingleResponse addChangeRequestToActivity(@ModelAttribute JsonChangeRequest jsonChangeRequest,@RequestParam Integer activityId,@RequestParam Integer changeRequestId, @RequestParam String maporunmap) {
	JTableSingleResponse jTableSingleResponse;
	try {
			ChangeRequest changeRequest = activityService.updateChangeRequestToActivityOneToMany(changeRequestId, activityId, maporunmap);
			
			List<JsonChangeRequest> jsonChangeRequestToUI=new ArrayList<JsonChangeRequest>();
			if(changeRequest != null){
				jsonChangeRequestToUI.add(new JsonChangeRequest(changeRequest));
			}				
			jTableSingleResponse = new JTableSingleResponse("OK",jsonChangeRequestToUI);	
		} catch (Exception e) {
            jTableSingleResponse = new JTableSingleResponse("ERROR","Unable to update the Activity  & Environment association!");
            log.error("JSON ERROR adding ChangeRequest To Activity", e);	 
        }
	
    return jTableSingleResponse;
}

	@RequestMapping(value = "list.change.requests.by.product", method = RequestMethod.POST, produces = "application/json")
	public @ResponseBody JTableResponse listChangeRequestsByProduct(@RequestParam Integer productId,@RequestParam Integer status,  @RequestParam int jtStartIndex,
			@RequestParam int jtPageSize) {
		log.debug("inside list.change.requests.by.product");
		JTableResponse jTableResponse;
		try {
			List<JsonChangeRequest> jsonChangeRequestList = null;
			jsonChangeRequestList = changeRequestService.listChangeRequestsByProductId(productId,status,0,jtStartIndex,jtPageSize);
			jTableResponse = new JTableResponse("OK", jsonChangeRequestList);
			log.debug("inside process fetching Change Requests records ");
		} catch (Exception e) {
			jTableResponse = new JTableResponse("ERROR","Error fetching records!");
			log.error("JSON ERROR", e);
		}
		return jTableResponse;
	}
	
	
	@SuppressWarnings("unused")
	@RequestMapping(value="list.change.requests.by.activity.update",method=RequestMethod.POST ,produces="application/json")
    public @ResponseBody JTableSingleResponse updateTestCases(HttpServletRequest req,@RequestParam Integer activityId,@ModelAttribute JsonChangeRequest jsonChangeRequest, BindingResult result) {
		JTableSingleResponse jTableSingleResponse = null;
		ChangeRequest changeRequestList=null;
		Activity activity = null;
		ActivityWorkPackage activityWorkPackage = null;
		ProductMaster productMaster = null;		
		TestFactory testFactory = null;
		
		String remarks = "";
		String subject = "Updation of ChangeRequest for an Activity";
		log.info("list.change.requests.by.activity.update");
		if(result.hasErrors()){
			jTableSingleResponse = new JTableSingleResponse("ERROR","Invalid form!"); 
		}
		try {	
			changeRequestList=jsonChangeRequest.getChangeRequest();
				if(activityId !=null){
					activity = activityService.getActivityById(activityId,0);
					activityWorkPackage = activity.getActivityWorkPackage();
					productMaster = activityWorkPackage.getProductBuild().getProductVersion().getProductMaster();
					testFactory = productMaster.getTestFactory();
					remarks = "TestFactory :"+testFactory.getTestFactoryName()+", Product :"+productMaster.getProductName()+", ActivityWorkPackage :"+activityWorkPackage.getActivityWorkPackageName()+", Activity :"+activity.getActivityName()+", ChangeRequest :"+changeRequestList.getChangeRequestName();
				
			}
			
			changeRequestService.updateChangeRequesttoActivity(changeRequestList);
			if(changeRequestList != null && changeRequestList.getChangeRequestId() != null){
					mongoDBService.addChangeRequestToMongoDB(changeRequestList.getChangeRequestId());
				UserList user=(UserList)req.getSession().getAttribute("USER");
				
				eventsService.addEntityChangedEvent(IDPAConstants.ENTITY_CHANGE_REQUEST, changeRequestList.getChangeRequestId(), changeRequestList.getChangeRequestName(),
						jsonChangeRequest.getModifiedField(), jsonChangeRequest.getModifiedFieldTitle(),
						jsonChangeRequest.getOldFieldValue(), jsonChangeRequest.getModifiedFieldValue(), user, remarks);
			}
			List<JsonChangeRequest> jsnCRList=new ArrayList<JsonChangeRequest>();
			jsnCRList.add(jsonChangeRequest);
			jTableSingleResponse = new JTableSingleResponse("OK",jsnCRList);
			notificationService.processNotification(req, jsonChangeRequest.getProductId(), IDPAConstants.NOTIFICATION_CHANGE_REQUEST_UPDATION, changeRequestList,subject);
		
		}
		catch (Exception e) {
            jTableSingleResponse = new JTableSingleResponse("ERROR","Error adding record!");
            log.error("JSON ERROR", e);	 
        }
		 return jTableSingleResponse;
	}
	
	@RequestMapping(value = "list.changerequests.by.id", method = RequestMethod.POST, produces = "application/json")
	public @ResponseBody
	JTableResponse listChangeRequestsById(@RequestParam Integer entityType1,@RequestParam Integer entityType2,@RequestParam Integer entityInstanceId1) {
		log.debug("inside list.changerequests.by.id");
		JTableResponse jTableResponse;
		try {
			List<JsonChangeRequest> jsonChangeRequestList = null;
			jsonChangeRequestList = changeRequestService.listChangeRequestsByentityInstanceId(entityType1, entityType2, entityInstanceId1);
			jTableResponse = new JTableResponse("OK", jsonChangeRequestList);
			log.debug("inside process fetching Change Requests records ");
		} catch (Exception e) {
			jTableResponse = new JTableResponse("ERROR","Error fetching records!");
			log.error("JSON ERROR", e);
		}
		return jTableResponse;
	}
	@RequestMapping(value="changerequest.type.option",method=RequestMethod.POST ,produces="application/json")
	public @ResponseBody JTableResponseOptions getChangeRequestType() {
		log.info("changerequest.type.option");
		JTableResponseOptions jTableResponseOptions;		 
		try {	
			List<ChangeRequestType> changeRequestTypeList = changeRequestService.getChangeRequestType();
			List<JsonChangeRequestType> jsonchangeRequestTypeList = new ArrayList<JsonChangeRequestType>();
			for(ChangeRequestType changeRequestType : changeRequestTypeList){
				jsonchangeRequestTypeList.add(new JsonChangeRequestType(changeRequestType));
			}
			jTableResponseOptions = new JTableResponseOptions("OK", jsonchangeRequestTypeList, true);			
	        } catch (Exception e) {
	        	jTableResponseOptions = new JTableResponseOptions("ERROR","Error obtaining clarifications Types!");
	            log.error("JSON ERROR", e);
	        }	        
	    return jTableResponseOptions;
	}
	
	@RequestMapping(value="unmapped.changerequest.list.by.productId",method=RequestMethod.POST ,produces="application/json")
	public @ResponseBody JTableResponse listUnMappedRcn(@RequestParam int productId, @RequestParam int jtStartIndex, @RequestParam int jtPageSize) {
		log.debug("unmapped.changerequest.list.by.productId");
		JTableResponse jTableResponse=null;
			try {	
				List<Object[]> unMappedRcnListObj = changeRequestService.getRcnByProductId(productId, jtStartIndex, jtPageSize,0);
			
				JSONArray unMappedJsonArray = new JSONArray();
				for (Object[] row : unMappedRcnListObj) {
					JSONObject jsobj =new JSONObject();
					jsobj.put(IDPAConstants.ITEM_ID, (Integer)row[0]);
					jsobj.put(IDPAConstants.ITEM_NAME, (String)row[1]);	
					unMappedJsonArray.add(jsobj);					
				}				
				jTableResponse = new JTableResponse("OK", unMappedJsonArray,unMappedJsonArray.size());
				unMappedRcnListObj = null;					 
			
			} catch (Exception e) {
	            jTableResponse = new JTableResponse("ERROR","Error fetching UnMappedRCNList!");
	            log.error("JSON ERROR", e);
	        }
	    return jTableResponse;
	}
	
	@RequestMapping(value="unmapped.changeRequest.count.by.productId",method=RequestMethod.POST ,produces="application/json")
	public @ResponseBody JTableSingleResponse getUnMappedChangeRequestListCountByProductId(@RequestParam int productId,@RequestParam int activityId) {
		JTableSingleResponse jTableSingleResponse;
		int unMappedRcn = 0;
		JSONObject unMappedRcnCountObj =new JSONObject();
		try {	
		    unMappedRcn = activityService.getUnMappedRcnByActivityId(activityId,0,productId);		
			unMappedRcnCountObj.put("unMappedTCCount", unMappedRcn);						
			jTableSingleResponse = new JTableSingleResponse("OK",unMappedRcnCountObj);
	        } catch (Exception e) {
	            jTableSingleResponse = new JTableSingleResponse("ERROR","Unable to get the count of UnMapped CRlist!");
	            log.error("JSON ERROR getting UnMapped CRlist!", e);	 
	        }
	    return jTableSingleResponse;
	}
	
	@RequestMapping(value="list.changeRequest.by.engagementAndProductId",method=RequestMethod.POST,produces="application/json")
	public @ResponseBody
	JTableResponse listClarificationTrackerByEngagementAndProductIds(@RequestParam Integer testFactoryId, @RequestParam Integer productId) {
		log.info("inside list.changeRequest.by.engagementAndProductId");
		JTableResponse jTableResponse = null;
		try{
			List<JsonChangeRequest> jsonChangeRequestList = null;
			jsonChangeRequestList = changeRequestService.listChangeRequestsByEngagementAndProductIds(testFactoryId, productId);
			jTableResponse = new JTableResponse("OK", jsonChangeRequestList);
		}catch(Exception e) {
			jTableResponse = new JTableResponse("ERROR", "Error fetching CR records by EngagementId and ProductId");
		}
		return jTableResponse;
	}
	
	@RequestMapping(value="unmapped.changerequest.list.by.wpId",method=RequestMethod.POST ,produces="application/json")
	public @ResponseBody JTableResponse listUnMappedRcnWithActivityWPId(@RequestParam int productId,@RequestParam int activityWPId,@RequestParam int activityId, @RequestParam int jtStartIndex, @RequestParam int jtPageSize) {
		log.debug("unmapped.changerequest.list.by.wpId");
		JTableResponse jTableResponse=null;
			try {	
				List<Object[]> unMappedRcnListObj = activityService.getUnMappedRcnByActivityWPId(productId,activityWPId,activityId, jtStartIndex, jtPageSize);
			
				JSONArray unMappedJsonArray = new JSONArray();
				for (Object[] row : unMappedRcnListObj) {
					JSONObject jsobj =new JSONObject();
					jsobj.put(IDPAConstants.ITEM_ID, (Integer)row[0]);
					jsobj.put(IDPAConstants.ITEM_NAME, (String)row[1]);	
					unMappedJsonArray.add(jsobj);					
				}				
				jTableResponse = new JTableResponse("OK", unMappedJsonArray,unMappedJsonArray.size());
				unMappedRcnListObj = null;					 
			
			} catch (Exception e) {
	            jTableResponse = new JTableResponse("ERROR","Error fetching UnMappedRCNList!");
	            log.error("JSON ERROR", e);
	        }
	    return jTableResponse;
	}
	
	
	
	@RequestMapping(value="unmapped.changeRequest.count.by.wpId",method=RequestMethod.POST ,produces="application/json")
	public @ResponseBody JTableSingleResponse getUnMappedRcnCountByActivityWPId(@RequestParam int activityId,@RequestParam int activityWPId) {
	JTableSingleResponse jTableSingleResponse;
	int unMappedRcn = 0;
	JSONObject unMappedRcnCountObj =new JSONObject();
	try {	
		ActivityWorkPackage activityWorkPackage = null;
		Activity activity=null;
		ProductBuild productBuild = null;
		ProductMaster productMaster=null;
		ProductVersionListMaster productVersion=null;
		   activity = activityService.getActivityById(activityId,1);	
		 activityWorkPackage = activity.getActivityWorkPackage();
            productBuild = activityWorkPackage.getProductBuild();
            productVersion = productBuild.getProductVersion();
            productMaster = productVersion.getProductMaster();	            
            Integer productId = productMaster.getProductId();		
	        unMappedRcn = activityService.getUnMappedRcnCountByActivityWPId(activityId,productId,activityWPId);			
	
		unMappedRcnCountObj.put("unMappedTCCount", unMappedRcn);						
		jTableSingleResponse = new JTableSingleResponse("OK",unMappedRcnCountObj);
        } catch (Exception e) {
            jTableSingleResponse = new JTableSingleResponse("ERROR","Error getting unmappedRcnCount!");
            log.error("JSON ERROR getting unmappedRcnCount", e);	 
        }
        
    return jTableSingleResponse;
	}
	
	@RequestMapping(value="list.CR.by.entityTypeIds.and.instanceId", method=RequestMethod.POST, produces="application/json")
	public @ResponseBody JTableResponse listCRsbyEntityTypeAndInstanceIds(@RequestParam Integer entityType1, @RequestParam Integer entityType2, @RequestParam Integer entityInstance1) {
		JTableResponse jTableResponse = null;
		List<ChangeRequest> listOfMappedCRs = null;
		try {
			log.debug("list.CR.by.entityTypeIds.and.instanceId");
			listOfMappedCRs = changeRequestService.listCRByEntityTypeAndInstanceIds(entityType1, entityType2, entityInstance1);
			if(listOfMappedCRs.size()>0 && listOfMappedCRs != null){
				List<JsonChangeRequest> jsonChangeRequestList = new ArrayList<JsonChangeRequest>();
				List<Object[]> attachmentCountDetails = commonDAO.getAttachmentCountOfEntity(IDPAConstants.CHANGE_REQUEST_ENTITY_MASTER_ID);
				for (ChangeRequest changeRequest : listOfMappedCRs) {
					JsonChangeRequest jsonChangeRequest = new JsonChangeRequest(changeRequest);
					jsonChangeRequest.setAttachmentCount(commonService.getCountFromAttachmentCountDetails(attachmentCountDetails, jsonChangeRequest.getChangeRequestId()));
					jsonChangeRequestList.add(jsonChangeRequest);
				}
				jTableResponse = new JTableResponse("OK", jsonChangeRequestList, jsonChangeRequestList.size());
			}
		} catch (Exception e) {
			log.error("JSON ERROR list.CR.by.entityTypeIds.and.instanceId", e);
		}
		return jTableResponse;
	}
	
}
