package com.hcl.atf.taf.controller;

import java.util.ArrayList;
import java.util.Date;
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
import com.hcl.atf.taf.model.Activity;
import com.hcl.atf.taf.model.ActivityWorkPackage;
import com.hcl.atf.taf.model.ClarificationScope;
import com.hcl.atf.taf.model.ClarificationTracker;
import com.hcl.atf.taf.model.ClarificationTypeMaster;
import com.hcl.atf.taf.model.EntityMaster;
import com.hcl.atf.taf.model.EntityRelationship;
import com.hcl.atf.taf.model.EntityStatus;
import com.hcl.atf.taf.model.ProductMaster;
import com.hcl.atf.taf.model.TestFactory;
import com.hcl.atf.taf.model.TransactionClarification;
import com.hcl.atf.taf.model.UserList;
import com.hcl.atf.taf.model.json.JsonClarificationScope;
import com.hcl.atf.taf.model.json.JsonClarificationTracker;
import com.hcl.atf.taf.model.json.JsonClarificationType;
import com.hcl.atf.taf.model.json.JsonEntityStatus;
import com.hcl.atf.taf.model.json.JsonTransactionClarification;
import com.hcl.atf.taf.model.json.jtable.JTableResponse;
import com.hcl.atf.taf.model.json.jtable.JTableResponseOptions;
import com.hcl.atf.taf.model.json.jtable.JTableSingleResponse;
import com.hcl.atf.taf.mongodb.service.MongoDBService;
import com.hcl.atf.taf.service.ActivityService;
import com.hcl.atf.taf.service.ActivityStatusService;
import com.hcl.atf.taf.service.ActivityWorkPackageService;
import com.hcl.atf.taf.service.ClarificationTrackerService;
import com.hcl.atf.taf.service.EntityRelationshipService;
import com.hcl.atf.taf.service.EventsService;
import com.hcl.atf.taf.service.NotificationService;
import com.hcl.atf.taf.service.ProductListService;
import com.hcl.atf.taf.service.UserListService;
import com.hcl.atf.taf.service.WorkPackageService;

@Controller
public class ClarificationTrackerManagementController {
	private static final Log log = LogFactory.getLog(ClarificationTrackerManagementController.class);

	@Autowired
	private ClarificationTrackerService clarificationTrackerService;
	@Autowired
	private ActivityService activityService;
	@Autowired
	private ActivityStatusService activityStatusService;
	
	@Autowired
	private MongoDBService mongoDBService;
	@Autowired
	private ProductListService productListService;
	@Autowired
	private UserListService userListService;
	@Autowired
	private WorkPackageService workPackageService;
	@Autowired
	private EventsService eventsService;	
	@Autowired
	private EntityRelationshipService entityRelationshipService;
	@Autowired
	private ActivityWorkPackageService activityWorkPackageService;
	
	@Autowired
	private NotificationService notificationService;

	@RequestMapping(value = "list.clarificationtracker.by.entityTypeAndInstanceIds", method = RequestMethod.POST, produces = "application/json")
	public @ResponseBody
	JTableResponse listClarificationTrackersByActivity(@RequestParam Integer entityTypeId, @RequestParam Integer entityInstanceId) {
		log.info("inside list.clarificationtracker.by.entityTypeAndInstanceIds");
		JTableResponse jTableResponse;
		try {
			List<JsonClarificationTracker> jsonClarificationTrackerList = null;
			jsonClarificationTrackerList = clarificationTrackerService.listClarificationTrackerByActivityId(entityTypeId, entityInstanceId, 1);
			jTableResponse = new JTableResponse("OK", jsonClarificationTrackerList);
			log.debug("inside process fetching  DR records ");
		} catch (Exception e) {
			jTableResponse = new JTableResponse("ERROR","Error fetching DR records!");
			log.error("JSON ERROR", e);
		}
		return jTableResponse;
	}
	

	@RequestMapping(value = "list.clarificationtracker.by.activityworkpackage", method = RequestMethod.POST, produces = "application/json")
	public @ResponseBody
	JTableResponse listClarificationTrackersByAWPId(@RequestParam Integer entityTypeId, @RequestParam Integer entityInstanceId, @RequestParam int jtStartIndex, @RequestParam int jtPageSize) {
		log.info("inside list.clarificationtracker.by.activityworkpackage");
		JTableResponse jTableResponse;
		try {
			List<JsonClarificationTracker> jsonClarificationTrackerList = null;
			jsonClarificationTrackerList = clarificationTrackerService.listClarificationsByAWPId(entityTypeId, entityInstanceId, jtStartIndex, jtPageSize, 1);
			//jsonClarificationTrackerList = clarificationTrackerService.listClarificationTrackerByActivityId(entityTypeId, entityInstanceId, 1);
			jTableResponse = new JTableResponse("OK", jsonClarificationTrackerList);
			log.debug("inside process fetching  DR records ");
		} catch (Exception e) {
			jTableResponse = new JTableResponse("ERROR","Error fetching DR records!");
			log.error("JSON ERROR", e);
		}
		return jTableResponse;
	}

	@RequestMapping(value = "list.clarificationtracker.by.product", method = RequestMethod.POST, produces = "application/json")
	public @ResponseBody
	JTableResponse listClarificationTrackersByProductId(@RequestParam Integer entityTypeId, @RequestParam Integer entityInstanceId, @RequestParam int jtStartIndex, @RequestParam int jtPageSize) {
		log.info("inside list.clarificationtracker.by.product");
		JTableResponse jTableResponse;
		try {
			List<JsonClarificationTracker> jsonClarificationTrackerList = null;
			jsonClarificationTrackerList = clarificationTrackerService.listClarificationsByProductId(entityTypeId, entityInstanceId, jtStartIndex, jtPageSize, 1);
			jTableResponse = new JTableResponse("OK", jsonClarificationTrackerList);
			log.debug("inside process fetching  CR records ");
		} catch (Exception e) {
			jTableResponse = new JTableResponse("ERROR","Error fetching CR records!");
			log.error("JSON ERROR", e);
		}
		return jTableResponse;
	}

	@RequestMapping(value = "update.clarificationtracker.by.activity", method = RequestMethod.POST, produces = "application/json")
	public @ResponseBody
	JTableResponse updateClarificationTracker(HttpServletRequest request,
			@ModelAttribute JsonClarificationTracker jsonClarificationTracker, BindingResult result) {
		log.info("update.clarificationtracker.by.activity");
		JTableResponse jTableResponse = null;
		ClarificationTracker RequestFromUI = null;
		Activity activity = null;
		String remarks = "";
		if (result.hasErrors()) {
			jTableResponse = new JTableResponse("ERROR", "Invalid Form!");
		}
		try {
			RequestFromUI = jsonClarificationTracker.getClarificationTracker();
			EntityMaster entityMaster = new EntityMaster();
			entityMaster.setEntitymasterid(IDPAConstants.ACTIVITY_ENTITY_MASTER_ID);
			RequestFromUI.setEntityType(entityMaster);		
			clarificationTrackerService.updateClarificationTracker(RequestFromUI);
			
			if(RequestFromUI.getEntityType().getEntitymasterid() == IDPAConstants.TESTFACTORY_ENTITY_MASTER_ID){
				remarks = "TestFactory :"+RequestFromUI.getTestFactory() != null ? RequestFromUI.getTestFactory().getTestFactoryName():""+", ClarificationTracker :"+RequestFromUI.getClarificationTitle();
			}else if (RequestFromUI.getEntityType().getEntitymasterid() == IDPAConstants.PRODUCT_ENTITY_MASTER_ID) {
				remarks = "TestFactory :"+RequestFromUI.getTestFactory() != null ? RequestFromUI.getTestFactory().getTestFactoryName():""+", Product :"+RequestFromUI.getProduct() != null ?RequestFromUI.getProduct().getProductName():""+", ClarificationTracker :"+RequestFromUI.getClarificationTitle();
			}else if (RequestFromUI.getEntityType().getEntitymasterid() == IDPAConstants.ACTIVITY_ENTITY_MASTER_ID) {
				activity = activityService.getActivityById(RequestFromUI.getEntityInstanceId(), 1);
				remarks = "TestFactory :"+RequestFromUI.getTestFactory() != null ? RequestFromUI.getTestFactory().getTestFactoryName():""+", Product :"+RequestFromUI.getProduct() != null ?RequestFromUI.getProduct().getProductName():""+", Activity :"+activity != null ?activity.getActivityName():""+", ClarificationTracker :"+RequestFromUI.getClarificationTitle();
			}
			
			if(RequestFromUI != null && RequestFromUI.getClarificationTrackerId() != null){
				UserList user=(UserList)request.getSession().getAttribute("USER");
				eventsService.addEntityChangedEvent(IDPAConstants.ENTITY_CLARIFICATION_TRACKER,  RequestFromUI.getClarificationTrackerId(),
						RequestFromUI.getClarificationTitle(),
						jsonClarificationTracker.getModifiedField(), jsonClarificationTracker.getModifiedFieldTitle(),
						jsonClarificationTracker.getOldFieldValue(), jsonClarificationTracker.getModifiedFieldValue(), user, remarks);
			}
			List<JsonClarificationTracker> jsonClarificationTrackerList = new ArrayList();
			jsonClarificationTrackerList.add(jsonClarificationTracker);
			 jTableResponse = new JTableResponse("OK",jsonClarificationTrackerList ,1);
		} catch (Exception e) {
			jTableResponse = new JTableResponse("ERROR",
					"Unable to update the ClarificationTracker!");
			log.error("JSON ERROR", e);
		}
		return jTableResponse;
	}
	
	@RequestMapping(value = "add.clarificationtracker.by.activity", method = RequestMethod.POST, produces = "application/json")
	public @ResponseBody JTableSingleResponse addClarificationTracker(HttpServletRequest request,@ModelAttribute JsonClarificationTracker jsonClarificationTracker,BindingResult result) {	
		JTableSingleResponse jTableSingleResponse;		
		try {
			log.info("add.clarificationtracker.by.activity");
			int entityTypeId = 31;
			String subject = "Adding ClarificationTracker by Activity";
			ClarificationTracker clarificationTracker = jsonClarificationTracker.getClarificationTracker();	
			if(clarificationTracker != null && (clarificationTracker.getEntityStatus() == null || clarificationTracker.getEntityStatus().getEntityStatusId() == null)){
				EntityStatus entityStatus = new EntityStatus();
				List<EntityStatus> clarificationstatusTypes = clarificationTrackerService.listClarificationStatus(entityTypeId);
				if(clarificationstatusTypes != null && clarificationstatusTypes.size() > 0){
					entityStatus.setEntityStatusId(clarificationstatusTypes.get(0).getEntityStatusId());
					clarificationTracker.setEntityStatus(entityStatus);
				}
			}
			
			if(jsonClarificationTracker.getEntityTypeId() != null && jsonClarificationTracker.getEntityTypeId() == IDPAConstants.ENTITY_ACTIVITY_WORKPACKAGE_ID){
				if(jsonClarificationTracker.getEntityInstanceId() != null){
					ActivityWorkPackage activityWorkPackage = activityWorkPackageService.getActivityWorkPackageById(jsonClarificationTracker.getEntityInstanceId(), 1);
					if(activityWorkPackage != null) {
						TestFactory testFactory = activityWorkPackage.getProductBuild().getProductVersion().getProductMaster().getTestFactory();
						clarificationTracker.setTestFactory(testFactory);
					}
				}
			}
			
			
			if(jsonClarificationTracker.getProductId() != null){
				ProductMaster productMaster = productListService.getProductById(jsonClarificationTracker.getProductId());
				TestFactory testFactory = new TestFactory();
				testFactory.setTestFactoryId(productMaster.getTestFactory().getTestFactoryId());
				clarificationTracker.setTestFactory(testFactory);
	    	}
			
			if(jsonClarificationTracker.getActivityId() != null){
				Activity activity = activityService.getActivityById(jsonClarificationTracker.getActivityId(), 1);		
				if(clarificationTracker.getPlanExpectedValue() == null){
					if(activity.getPlannedActivitySize() != null){
					clarificationTracker.setPlanExpectedValue(activity.getPlannedActivitySize());
					}
				}			
			}
			
			if(clarificationTracker.getEntityType() == null){
			EntityMaster entityMaster = new EntityMaster();
			entityMaster.setEntitymasterid(jsonClarificationTracker.getEntityTypeId());
			clarificationTracker.setEntityType(entityMaster);
			} else {
				EntityMaster entityMaster = new EntityMaster();
				entityMaster.setEntitymasterid(jsonClarificationTracker.getEntityTypeId());
				clarificationTracker.setEntityType(entityMaster);
			}
			clarificationTracker.setPlannedStartDate(new Date());
			Integer clarificationTrackerId = clarificationTrackerService.addClarificationTracker(clarificationTracker);
			if(clarificationTracker!=null && clarificationTracker.getClarificationTrackerId()!=null){
				mongoDBService.addClarificationToMongoDB(clarificationTracker.getClarificationTrackerId());
				if(clarificationTracker != null && clarificationTracker.getClarificationTrackerId() != null){
					UserList user=(UserList)request.getSession().getAttribute("USER");
					eventsService.addNewEntityEvent(IDPAConstants.ENTITY_CLARIFICATION_TRACKER, clarificationTracker.getClarificationTrackerId(), clarificationTracker.getClarificationTitle(), user);
				}
			}
			if(clarificationTrackerId != null){
				EntityRelationship entityRelationship = new EntityRelationship();
				entityRelationship.setEntityTypeId1(jsonClarificationTracker.getEntityTypeId());
				entityRelationship.setEntityTypeId2(jsonClarificationTracker.getEntityTypeId2());
				entityRelationship.setEntityInstanceId1(jsonClarificationTracker.getEntityInstanceId());
				entityRelationship.setEntityInstanceId2(clarificationTrackerId);
				entityRelationship.setIsActive(1);
				entityRelationship.setRelationshipType(IDPAConstants.ENTITY_CLARIFICATION_TRACKER);
				entityRelationshipService.addEntityRelationship(entityRelationship);
			}
			jTableSingleResponse = new JTableSingleResponse("OK", new JsonClarificationTracker(clarificationTracker));
			notificationService.processNotification(request, jsonClarificationTracker.getProductId(), IDPAConstants.NOTIFICATION_CLARIFICATION_TRACKER_CREATION, clarificationTracker,subject);
			log.info("add.clarificationtracker.by.activity Success");
		} catch (Exception e) {
			jTableSingleResponse = new JTableSingleResponse("ERROR", "Error adding clarificationTracker record!");
			log.error("JSON ERROR", e);
		}
		return jTableSingleResponse;
	}

@RequestMapping(value="list.clarification.type.option",method=RequestMethod.POST ,produces="application/json")
public @ResponseBody JTableResponseOptions getClarificationType() {
	log.info("list.clarification.type.option");
	JTableResponseOptions jTableResponseOptions;		 
	try {	
		List<ClarificationTypeMaster> clarificationTypes = clarificationTrackerService.listClarificationType();
		List<JsonClarificationType> jsonClarificationTypes = new ArrayList<JsonClarificationType>();
		for(ClarificationTypeMaster clarificatinType : clarificationTypes){
			jsonClarificationTypes.add(new JsonClarificationType(clarificatinType));
		}
		jTableResponseOptions = new JTableResponseOptions("OK", jsonClarificationTypes, true);			
        } catch (Exception e) {
        	jTableResponseOptions = new JTableResponseOptions("ERROR","Error obtaining clarifications Types!");
            log.error("JSON ERROR", e);
        }	        
    return jTableResponseOptions;
}



@RequestMapping(value="clarification.status.option.list",method=RequestMethod.POST ,produces="application/json")
public @ResponseBody JTableResponseOptions getClarificationStatus(@RequestParam int entityTypeId) {
	JTableResponseOptions jTableResponseOptions;		 
	try {	
		List<EntityStatus> clarificationstatusTypes = clarificationTrackerService.listClarificationStatus(entityTypeId);
		List<JsonEntityStatus> jsonClarificationStatusTypes = new ArrayList<JsonEntityStatus>();
		for(EntityStatus clarificationStatusType : clarificationstatusTypes){
			jsonClarificationStatusTypes.add(new JsonEntityStatus(clarificationStatusType));
		}
		jTableResponseOptions = new JTableResponseOptions("OK", jsonClarificationStatusTypes, true);			
        } catch (Exception e) {
        	jTableResponseOptions = new JTableResponseOptions("ERROR","Error obtaining clarifications status Types!");
            log.error("JSON ERROR", e);
        }	        
    return jTableResponseOptions;
}
@RequestMapping(value="clarification.resolution.option.list",method=RequestMethod.POST ,produces="application/json")
public @ResponseBody JTableResponseOptions getClarificationStatusResolution(@RequestParam int parentStatusId) {
	JTableResponseOptions jTableResponseOptions;		 
	try {	
		List<EntityStatus> clarificationstatusTypes = clarificationTrackerService.listClarificationResolutionStatus(parentStatusId);
		List<JsonEntityStatus> jsonClarificationStatusTypes = new ArrayList<JsonEntityStatus>();
		for(EntityStatus clarificationStatusType : clarificationstatusTypes){
			jsonClarificationStatusTypes.add(new JsonEntityStatus(clarificationStatusType));
		}
		jTableResponseOptions = new JTableResponseOptions("OK", jsonClarificationStatusTypes, true);			
        } catch (Exception e) {
        	jTableResponseOptions = new JTableResponseOptions("ERROR","Error obtaining clarifications status Types!");
            log.error("JSON ERROR", e);
        }	        
    return jTableResponseOptions;
	}
 
@RequestMapping(value="list.clarification.scope.option",method=RequestMethod.POST ,produces="application/json")
public @ResponseBody JTableResponseOptions getClarificationScope() {
	JTableResponseOptions jTableResponseOptions=null;		 
	try {	
		List<ClarificationScope> listClarificationScope= clarificationTrackerService.getClarificationScope();
		List<JsonClarificationScope> jsonClarificationScope = new ArrayList<JsonClarificationScope>();
		for(ClarificationScope clarificationScope : listClarificationScope){
			jsonClarificationScope.add(new JsonClarificationScope(clarificationScope));	
		}
		jTableResponseOptions = new JTableResponseOptions("OK", jsonClarificationScope, true);		
        } catch (Exception e) {
        	jTableResponseOptions = new JTableResponseOptions("ERROR","Error obtaining clarifications scope!");
            log.error("JSON ERROR", e);
        }	       
    return jTableResponseOptions;
	}

@RequestMapping(value="list.clarificationtracker.by.engagementAndProductId",method=RequestMethod.POST,produces="application/json")
public @ResponseBody
JTableResponse listClarificationTrackerByEngagementAndProductIds(@RequestParam Integer testFactoryId, @RequestParam Integer productId) {
	log.info("inside list.clarificationtracker.by.engagementAndProductId");
	JTableResponse jTableResponse = null;
	try{
		List<JsonClarificationTracker> jsonClarificationTrackerList = null;
		jsonClarificationTrackerList = clarificationTrackerService.listClarificationsByEngagementAndProductIds(testFactoryId, productId);
		jTableResponse = new JTableResponse("OK", jsonClarificationTrackerList);
	}catch(Exception e) {
		jTableResponse = new JTableResponse("ERROR", "Error fetching DR records by EngagementId and ProductId");
		log.error("Error occured in listClarificationTrackerByEngagementAndProductIds - ", e);
	}
	return jTableResponse;
}


@RequestMapping(value="transaction.clarification.add",method=RequestMethod.POST ,produces="application/json")
public @ResponseBody JTableSingleResponse addTransactionClarification(HttpServletRequest req,@RequestParam Integer clarificationTrackerId,@ModelAttribute JsonTransactionClarification jsonTransactionClarification, BindingResult result) {
	log.info("Inside transaction.clarification.add ");
	JTableSingleResponse jTableSingleResponse=null;
	if(result.hasErrors()){
		jTableSingleResponse  = new JTableSingleResponse("ERROR","Invalid form!"); 
		return jTableSingleResponse;
	}		
	try {   	
		TransactionClarification transactionClarification= jsonTransactionClarification.getTransactionClarification();
		UserList user = new UserList();
		user = (UserList)req.getSession().getAttribute("USER");
		transactionClarification.setCommentedBy(user);
		transactionClarification.setCommentedDate(new Date());
		clarificationTrackerService.addTransactionClarification(transactionClarification);
			jTableSingleResponse = new JTableSingleResponse("OK",new JsonTransactionClarification(transactionClarification));		
        } catch (Exception e) {
            jTableSingleResponse = new JTableSingleResponse("ERROR","Error adding TransactionClarification record!");
            log.error("JSON ERROR adding TransactionClarification ", e);
        }
        
    return jTableSingleResponse;
}

@RequestMapping(value="transaction.clarification.list",method=RequestMethod.POST ,produces="application/json")
public @ResponseBody JTableResponse listTransactionClarification(@RequestParam Integer clarificationTrackerId) {
	log.debug("inside administration.transaction.clarification.list");
	JTableResponse jTableResponse;
	 try{
		List<TransactionClarification> transactionClarificationList=clarificationTrackerService.listTransactionClarification(clarificationTrackerId);
		
		List<JsonTransactionClarification> jsonTransactionClarification=new ArrayList<JsonTransactionClarification>();
		for(TransactionClarification transaction: transactionClarificationList){
			jsonTransactionClarification.add(new JsonTransactionClarification(transaction));
		}
        jTableResponse = new JTableResponse("OK", jsonTransactionClarification,jsonTransactionClarification.size());     
        transactionClarificationList = null;
        } catch (Exception e) {
            jTableResponse = new JTableResponse("ERROR","Unable to show transactionClarification List!");
            log.error("JSON ERROR", e);
        }
        
    return jTableResponse;
}


@RequestMapping(value="transaction.clarification.update",method=RequestMethod.POST ,produces="application/json")
public @ResponseBody JTableResponse updateTransactionClarification(@ModelAttribute JsonTransactionClarification jsonTransactionClarification,BindingResult result) {
	JTableResponse jTableResponse = null;
	if(result.hasErrors()){
		jTableResponse = new JTableResponse("ERROR","Invalid Form!"); 
	}	
	TransactionClarification transactionClarificationUI = null;
	try {
		transactionClarificationUI= jsonTransactionClarification.getTransactionClarification();
		clarificationTrackerService.updateTransactionClarification(transactionClarificationUI);	
		 jTableResponse = new JTableResponse("OK");  
	    } catch (Exception e) {
            jTableResponse = new JTableResponse("ERROR","Unable to update the transactionClarification!");
            log.error("JSON ERROR", e);
        }	        
        
    return jTableResponse;

}


@RequestMapping(value="transaction.clarification.option.list",method=RequestMethod.POST ,produces="application/json")
public @ResponseBody JTableResponseOptions getTransactionClarificationOptions(@RequestParam Integer clarificationTrackerId) {
	log.info("transaction.clarification.option.list");
	JTableResponseOptions jTableResponseOptions;		 
	try {	
		List<TransactionClarification> transactionClarifications = clarificationTrackerService.listTransactionClarification(clarificationTrackerId);
		List<JsonTransactionClarification> jsonTransactionClarification = new ArrayList<JsonTransactionClarification>();
		JsonTransactionClarification jtc = new JsonTransactionClarification();
		jtc.setTransactionId(0);
		jtc.setTransactionName("--");
		jsonTransactionClarification.add(0,jtc);
		
		for(TransactionClarification clarification : transactionClarifications){
			jsonTransactionClarification.add(new JsonTransactionClarification(clarification));
		}
		jTableResponseOptions = new JTableResponseOptions("OK", jsonTransactionClarification, true);			
        } catch (Exception e) {
        	jTableResponseOptions = new JTableResponseOptions("ERROR","Error obtaining clarifications !");
            log.error("JSON ERROR", e);
        }	        
    return jTableResponseOptions;
	}

@RequestMapping(value="delete.clarificationtracker",method=RequestMethod.POST ,produces="application/json")
public @ResponseBody JTableResponse deleteActivityTask(@RequestParam Integer clarificationTrackerId) {
	log.debug("inside delete.clarificationtracker");
	JTableResponse jTableResponse = null;
	
	String message = "";
	try{
		message = clarificationTrackerService.deleteClarificationTracker(clarificationTrackerId);
		
		if(message.equals("OK")){
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

@RequestMapping(value = "update.clarificationtracker.by.entityType", method = RequestMethod.POST, produces = "application/json")
public @ResponseBody
JTableResponse updateClarificationTrackerByEntityType(HttpServletRequest request,
		@ModelAttribute JsonClarificationTracker jsonClarificationTracker, BindingResult result) {
	log.info("update.clarificationtracker.by.entityType");
	JTableResponse jTableResponse = null;
	ClarificationTracker RequestFromUI = null;
	ActivityWorkPackage activityWorkPackage = null;
	ProductMaster productMaster = null;
	TestFactory testFactory = null;
	String remarks = "";
	String subject = "Updation of Clarification by EntityType";
	if (result.hasErrors()) {
		jTableResponse = new JTableResponse("ERROR", "Invalid Form!");
	}
	try {
		RequestFromUI = jsonClarificationTracker.getClarificationTracker();
		if(jsonClarificationTracker.getEntityTypeId() != 0 && jsonClarificationTracker.getEntityTypeId() == IDPAConstants.ENTITY_ACTIVITY_WORKPACKAGE_ID){
			EntityMaster entityMaster = new EntityMaster();
			entityMaster.setEntitymasterid(IDPAConstants.ENTITY_ACTIVITY_WORKPACKAGE_ID);
			RequestFromUI.setEntityType(entityMaster);
			
			activityWorkPackage = activityWorkPackageService.getActivityWorkPackageById(RequestFromUI.getEntityInstanceId(), 1);
			productMaster = activityWorkPackage.getProductMaster();
			testFactory = productMaster.getTestFactory();
			remarks = "TestFactory :"+testFactory.getTestFactoryName()+", Product :"+productMaster.getProductName()+", ActivityWorkpackage :"+activityWorkPackage.getActivityWorkPackageName()+", ClarificationTracker :"+RequestFromUI.getClarificationTitle();
		}
				
		clarificationTrackerService.updateClarificationTracker(RequestFromUI);
		
		if(jsonClarificationTracker.getEntityTypeId() != 0 && jsonClarificationTracker.getEntityTypeId() == IDPAConstants.PRODUCT_ENTITY_MASTER_ID){
			EntityMaster entityMaster = new EntityMaster();
			entityMaster.setEntitymasterid(IDPAConstants.PRODUCT_ENTITY_MASTER_ID);
			RequestFromUI.setEntityType(entityMaster);
			
			productMaster = productListService.getProductById(RequestFromUI.getEntityInstanceId());
			testFactory = productMaster.getTestFactory();
			remarks = "TestFactory :"+testFactory.getTestFactoryName()+", Product :"+productMaster.getProductName()+", ClarificationTracker :"+RequestFromUI.getClarificationTitle();
		}
		
		if(RequestFromUI != null && RequestFromUI.getClarificationTrackerId() != null){
			UserList user=(UserList)request.getSession().getAttribute("USER");
			eventsService.addEntityChangedEvent(IDPAConstants.ENTITY_CLARIFICATION_TRACKER,  RequestFromUI.getClarificationTrackerId(),
					RequestFromUI.getClarificationTitle(),
					jsonClarificationTracker.getModifiedField(), jsonClarificationTracker.getModifiedFieldTitle(),
					jsonClarificationTracker.getOldFieldValue(), jsonClarificationTracker.getModifiedFieldValue(), user, remarks);
		}
		List<JsonClarificationTracker> jsonClarificationTrackerList = new ArrayList();
		jsonClarificationTrackerList.add(jsonClarificationTracker);
		 jTableResponse = new JTableResponse("OK",jsonClarificationTrackerList ,1);
		 notificationService.processNotification(request, jsonClarificationTracker.getProductId(), IDPAConstants.NOTIFICATION_CLARIFICATION_TRACKER_UPDATION, RequestFromUI,subject);
	} catch (Exception e) {
		jTableResponse = new JTableResponse("ERROR",
				"Unable to update the ClarificationTracker!");
		log.error("JSON ERROR", e);
	}
	return jTableResponse;
}


}
