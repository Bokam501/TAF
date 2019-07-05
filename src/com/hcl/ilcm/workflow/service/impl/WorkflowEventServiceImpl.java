/**
 * 
 */
package com.hcl.ilcm.workflow.service.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.velocity.app.VelocityEngine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hcl.atf.taf.constants.IDPAConstants;
import com.hcl.atf.taf.dao.ActivityDAO;
import com.hcl.atf.taf.dao.ActivityTaskDAO;
import com.hcl.atf.taf.dao.ActivityWorkPackageDAO;
import com.hcl.atf.taf.dao.TestCaseListDAO;
import com.hcl.atf.taf.model.Activity;
import com.hcl.atf.taf.model.ActivitySecondaryStatusMaster;
import com.hcl.atf.taf.model.ActivityTask;
import com.hcl.atf.taf.model.ActivityWorkPackage;
import com.hcl.atf.taf.model.Attachment;
import com.hcl.atf.taf.model.EntityMaster;
import com.hcl.atf.taf.model.ProductFeature;
import com.hcl.atf.taf.model.ProductMaster;
import com.hcl.atf.taf.model.StatusCategory;
import com.hcl.atf.taf.model.TestCaseList;
import com.hcl.atf.taf.model.UserList;
import com.hcl.atf.taf.mongodb.service.MongoDBService;
import com.hcl.atf.taf.service.ActivityService;
import com.hcl.atf.taf.service.ActivityTaskService;
import com.hcl.atf.taf.service.ActivityWorkPackageService;
import com.hcl.atf.taf.service.CommonService;
import com.hcl.atf.taf.service.EmailService;
import com.hcl.atf.taf.service.EventsService;
import com.hcl.atf.taf.service.ProductListService;
import com.hcl.atf.taf.service.UserListService;
import com.hcl.atf.taf.service.WorkPackageService;
import com.hcl.ilcm.workflow.dao.WorkflowEventDAO;
import com.hcl.ilcm.workflow.dao.WorkflowStatusDAO;
import com.hcl.ilcm.workflow.dao.WorkflowStatusPolicyDAO;
import com.hcl.ilcm.workflow.model.WorkflowEvent;
import com.hcl.ilcm.workflow.model.WorkflowMaster;
import com.hcl.ilcm.workflow.model.WorkflowStatus;
import com.hcl.ilcm.workflow.model.WorkflowStatusActor;
import com.hcl.ilcm.workflow.model.WorkflowStatusPolicy;
import com.hcl.ilcm.workflow.service.ConfigurationWorkFlowService;
import com.hcl.ilcm.workflow.service.WorkflowEventService;

/**
 * @author silambarasur
 *
 */
@Service
public class WorkflowEventServiceImpl implements WorkflowEventService{
	
	private static final Log log = LogFactory.getLog(WorkflowEventServiceImpl.class);
	
	@Autowired
	private WorkflowEventDAO workflowEventDAO;
	
	@Autowired
	private TestCaseListDAO testCaseListDAO;
	
	@Autowired
	private ActivityWorkPackageDAO activityWorkPackageDAO;
	
	@Autowired
	private ActivityTaskDAO activityTaskDAO;

	@Autowired
	private ActivityDAO activityDAO;
	
	@Autowired
	private WorkflowStatusPolicyDAO workflowStatusPolicyDAO;
	
	@Autowired
	private WorkflowStatusDAO workflowStatusDAO;
	
	@Autowired
	private MongoDBService mongoDBService;
	
	@Autowired
	private CommonService commonService;
	
	@Autowired
	private ConfigurationWorkFlowService configurationWorkFlowService;
	@Autowired
	private UserListService userListService;
	@Autowired
	private WorkPackageService workPackageService;
	@Autowired
	private EventsService eventsService;
	@Autowired
	private EmailService emailService;
	@Autowired
    private VelocityEngine velocityEngine;
	@Autowired
	private ActivityWorkPackageService activityWorkPackageService;
	@Autowired
	private ActivityService activityService;
	@Autowired
	private ActivityTaskService activityTaskService;
	@Autowired
	private ProductListService productListService;
	
	@Value("#{ilcmProps['EMAIL_ALERT_NOTIFICATION']}")
    private String emailNotification;
	
	@Override
	@Transactional
	public List<WorkflowEvent> listWorkflowEvents() {
		return workflowEventDAO.listWorkflowEvents();
	}
	
	@Override
	@Transactional
	public WorkflowEvent getWorkflowEventById(int effortTrackerId) {
		WorkflowEvent workflowEvent = null;
		try{
			workflowEvent = workflowEventDAO.getWorkflowEventById(effortTrackerId);
			Object instanceObject = null;
			if(workflowEvent != null){
				Integer instanceId = workflowEvent.getEntityInstanceId();
				if(workflowEvent.getEntityType().getEntitymasterid() == IDPAConstants.ENTITY_TEST_CASE_ID){
					instanceObject = testCaseListDAO.getByTestCaseId(instanceId);
				}else if(workflowEvent.getEntityType().getEntitymasterid() == IDPAConstants.ENTITY_TASK_TYPE){
					instanceObject = activityTaskDAO.getActivityTaskById(instanceId, 0);
				}else if(workflowEvent.getEntityType().getEntitymasterid() == IDPAConstants.ENTITY_ACTIVITY_TYPE){
					instanceObject = activityDAO.getActivityById(instanceId, 0);
				}else if(workflowEvent.getEntityType().getEntitymasterid() == IDPAConstants.ENTITY_ACTIVITY_WORKPACKAGE_ID){
					instanceObject = activityWorkPackageDAO.getActivityWorkPackageById(instanceId, 0);
				}
				workflowEvent.setInstanceObject(instanceObject);
			}
		}catch(Exception ex){
			log.error("Error in getWorkflowEventById - ", ex);
		}
		return workflowEvent;
	}
	
	
	@Override
	@Transactional
	public String addWorkflowEvent(Integer productId, String approveAllEntityInstanceIds, UserList user, Integer entityId, Integer entityTypeId, Integer targetStatusId,Integer secondaryStatusId, Integer effort, String comments, Integer sourceStatusId, Integer entityInstanceId, String attachmentIds, List<Integer> possibleIDs, Boolean isAutoApproveStatusChangeAllowed, Date actionDate,Integer actualSize) {
		Integer entityWiseEffort=0;
		WorkflowEvent referBackWorkflowEvent=null;
		boolean referback = false;
		boolean isSameStatusStage = true;
		Integer initialTargetStatus =null;
		if((sourceStatusId != null && sourceStatusId >0 ) && (targetStatusId == null || targetStatusId ==0)) {
			targetStatusId = sourceStatusId;
		} else {
			initialTargetStatus = targetStatusId;
		}
		ActivityWorkPackage activityWorkPackage = null;
		Activity activity = null;
		ActivityTask activityTask = null;
		ProductFeature feature=null;
		try {
			String builkTaskIds[]=approveAllEntityInstanceIds.split(",");
			if(builkTaskIds.length >0){
				for(String approvalId:builkTaskIds){
					entityInstanceId = Integer.parseInt(approvalId);
					entityWiseEffort = 0;
					
					if(effort != null) {
						entityWiseEffort = effort/builkTaskIds.length;
					}
					
					boolean isStatusAlreadyChanged = checkStatusOfEntityInstance(entityTypeId, entityInstanceId, sourceStatusId, possibleIDs);
					if(isStatusAlreadyChanged && sourceStatusId != null){
						return "Instance status already changed, Please refresh and see latest updates !";
					}
					
					if(targetStatusId > 0){
						isSameStatusStage = false;
						updateUserActionStatus(productId, sourceStatusId, targetStatusId, entityTypeId, entityId, entityInstanceId, user.getUserId());
						boolean isStatusChangeAllowed = checkMandatoryUserActionsToChangeStatus(productId, sourceStatusId, entityTypeId, entityId, entityInstanceId);
						if(isAutoApproveStatusChangeAllowed == null){
							isAutoApproveStatusChangeAllowed = isStatusChangeAllowed;
						}
						
						if(isAutoApproveStatusChangeAllowed == null || !isAutoApproveStatusChangeAllowed){
							comments += " -- Waiting for other mandatory user(s) to change";
						}else{
							comments += " -- Action completed";
						}
					}
					
					WorkflowEvent workflowEvents = new WorkflowEvent();
					workflowEvents.setEntityId(entityId);
					workflowEvents.setEntityInstanceId(entityInstanceId);
					EntityMaster entityMaster = new EntityMaster();
					entityMaster.setEntitymasterid(entityTypeId);
					workflowEvents.setEntityType(entityMaster);
					
					workflowEvents.setActualSize(actualSize);
					
					WorkflowStatus workflowCurrentStatus = workflowStatusDAO.getWorkflowStatusById(sourceStatusId);
					workflowEvents.setCurrentStatus(workflowCurrentStatus);
					if(targetStatusId > 0) {
						WorkflowStatus workflowTargetStatus = workflowStatusDAO.getWorkflowStatusById(targetStatusId);
						workflowEvents.setTargetStatus(workflowTargetStatus);
						
						Integer slaDurationPlanned = 0;
						WorkflowStatusPolicy workflowStatusPolicy = workflowStatusPolicyDAO.getSLADurationForEntityOrInstance(entityTypeId, entityId, entityInstanceId, sourceStatusId);
						if(workflowStatusPolicy != null){
							slaDurationPlanned = workflowStatusPolicy.getSlaDuration();
						}
						Integer slaDurationActual = 0;
						WorkflowEvent workflowEvent = workflowEventDAO.getLastEventOfEntityOrInstanceForStatus(entityTypeId, entityId, entityInstanceId, sourceStatusId);
						if(workflowEvent != null && workflowEvent.getLastUpdatedDate() != null){
							long hoursSpent = new Date().getTime() - workflowEvent.getLastUpdatedDate().getTime() ;
							if(hoursSpent < 0){
								hoursSpent = (-1 * hoursSpent);
							}
							slaDurationActual = (int) (hoursSpent / (1000*60*60));
						}
						workflowEvents.setSlaDurationPlanned(slaDurationPlanned);
						workflowEvents.setSlaDurationActual(slaDurationActual);
						
					}else {
						workflowEvents.setTargetStatus(null);
					}
					
					if(referback){
						referBackWorkflowEvent=getLatestWorkflowEventAction(entityId, entityTypeId);
						if(referBackWorkflowEvent != null) {
							WorkflowStatus currentWorkflowStatus = new WorkflowStatus();
							currentWorkflowStatus.setWorkflowStatusId(targetStatusId);
							referBackWorkflowEvent.setCurrentStatus(currentWorkflowStatus);
						}
					}	
					
					workflowEvents.setModifiedBy(user);
					workflowEvents.setLastUpdatedDate(new Date());
					workflowEvents.setPlannedStartDate(new Date());
					workflowEvents.setPlannedEndDate(new Date());
					workflowEvents.setActualStartDate(new Date());
					workflowEvents.setActualEndDate(new Date());
					workflowEvents.setPlannedEffort(0);
					workflowEvents.setActualEffort(entityWiseEffort);
					workflowEvents.setEntityGroupId(1);
					workflowEvents.setComments(comments);
					workflowEvents.setLastUpdatedDate(actionDate);
					ProductMaster product= new ProductMaster();
					product.setProductId(productId);
					workflowEvents.setProduct(product);
					workflowEventDAO.addWorkflowEvent(workflowEvents);
					
					if(workflowEvents != null && workflowEvents.getWorkflowEventId() != null){
						eventsService.addNewEntityEvent(IDPAConstants.ENTITY_WORKFLOW_EVENTS, workflowEvents.getWorkflowEventId(), workflowEvents.getCurrentStatus().getWorkflowStatusName(), user);
					}
					if(workflowEvents.getWorkflowEventId() != null && attachmentIds != null && !attachmentIds.trim().isEmpty() && !"0".equalsIgnoreCase(attachmentIds)){
						updateAttachmentForWorkflowEvent(workflowEvents.getWorkflowEventId(), attachmentIds);
					}
					
					Object instanceObject = null;
					if(workflowEvents.getEntityType().getEntitymasterid() == IDPAConstants.ENTITY_TEST_CASE_ID){
						instanceObject = testCaseUpdateBasedOnTracker(workflowEvents, isAutoApproveStatusChangeAllowed, productId);
					}else if(workflowEvents.getEntityType().getEntitymasterid() == IDPAConstants.ENTITY_TASK_TYPE){
						instanceObject = activityTaskUpdateBasedOnTracker(workflowEvents, isAutoApproveStatusChangeAllowed, productId);
					}else if(workflowEvents.getEntityType().getEntitymasterid() == IDPAConstants.ENTITY_ACTIVITY_TYPE){
						instanceObject = activityUpdateBasedOnTracker(workflowEvents, isAutoApproveStatusChangeAllowed, productId);
						activity = activityService.getActivityById(entityInstanceId, 1);
						activity.setWorkflowStatus(workflowEvents.getTargetStatus());
						activityService.updateActivity(activity);
					}else if(workflowEvents.getEntityType().getEntitymasterid() == IDPAConstants.ENTITY_ACTIVITY_WORKPACKAGE_ID){
						instanceObject = activityWorkpackageUpdateBasedOnTracker(workflowEvents, isAutoApproveStatusChangeAllowed, productId);
					}else if(workflowEvents.getEntityType().getEntitymasterid() == IDPAConstants.PRODUCT_FEATURE_ENTITY_MASTER_ID){
						instanceObject = productFeatureUpdateBasedOnTracker(workflowEvents, isAutoApproveStatusChangeAllowed, productId);
					}
					
					if(isAutoApproveStatusChangeAllowed != null && isAutoApproveStatusChangeAllowed){
						updateInstanceStatusPolicyActualValues(productId, sourceStatusId, targetStatusId, entityTypeId, entityId, entityInstanceId);
						checkAndUpdateStatusOfInstanceParentOrChild(productId, entityTypeId, entityInstanceId, instanceObject);
						
						List<Object[]> pendingActorsDetails = workflowStatusPolicyDAO.getPendingWithActors(productId, entityTypeId, entityId, entityInstanceId, targetStatusId);						
						String emailIds = "";
						String[] emails = null;
						String entityTypeName = "";
						String instanceNames = "";
						String firstNames = "";						
						String pendingWith = "";	
						int slaDuration = 0;
						String movedFrom = "--";
						String movedTo = "--";
						String[] ccMailIds = null;
						
						if(entityTypeId.equals(IDPAConstants.ENTITY_ACTIVITY_WORKPACKAGE_ID)){
							activityWorkPackage = activityWorkPackageService.getActivityWorkPackageById(entityInstanceId, 1);
							ccMailIds = new String[]{activityWorkPackage.getOwner().getEmailId()};
						}else if(entityTypeId.equals(IDPAConstants.ENTITY_ACTIVITY_TYPE)){
						/*	activity = activityService.getActivityById(entityInstanceId, 1);
							activity.setWorkflowStatus(workflowEvents.getTargetStatus());
							activityService.updateActivity(activity);*/
							ccMailIds = new String[]{activity.getAssignee().getEmailId()};
						}else if (entityTypeId.equals(IDPAConstants.ENTITY_TASK_TYPE)) {
							activityTask = activityTaskService.getActivityTaskById(entityInstanceId, 1);
							ccMailIds = new String[]{activityTask.getAssignee().getEmailId()};
						}else if (entityTypeId.equals(IDPAConstants.PRODUCT_FEATURE_ENTITY_MASTER_ID)) {
							feature = productListService.getByProductFeatureId(entityInstanceId);
							ccMailIds = new String[]{feature.getAssignee().getEmailId()};
						}else{
							ccMailIds = IDPAConstants.defaultMailId;
						}
						
						if(workflowEvents.getCurrentStatus() != null){
							movedFrom = workflowEvents.getCurrentStatus().getWorkflowStatusName();
						}
						if(workflowEvents.getTargetStatus() != null){
							movedTo = workflowEvents.getTargetStatus().getWorkflowStatusName();
						}
						if(workflowEvents.getSlaDurationPlanned() != null){
							slaDuration = workflowEvents.getSlaDurationPlanned();
						}
						
							if(!pendingActorsDetails.isEmpty() && pendingActorsDetails.size() != 0){							
							 for(Object[] pendingActorObjArr: pendingActorsDetails){								
								if(pendingActorObjArr != null && pendingActorObjArr.length > 0){
									UserList userList = userListService.getUserListById((Integer)pendingActorObjArr[0]);									
									if(userList != null){
									emailIds += userList.getEmailId()+",";
									firstNames += userList.getFirstName()+"/"; 
									pendingWith += userList.getLoginId()+",";																	
									}									
									if(entityTypeName.equals("") && pendingActorObjArr[1] != null){										
										entityTypeName = (String)pendingActorObjArr[1];
									}
									if(instanceNames.equals("") && pendingActorObjArr[2] != null){
										instanceNames = (String)pendingActorObjArr[2];
									}									
								 }
							  }
							if(emailIds.endsWith(",")){
								emailIds = emailIds.substring(0, emailIds.length()-1);
								emails = emailIds.split(",");
							}							
							if(firstNames.endsWith("/")){
								firstNames = firstNames.substring(0, firstNames.length()-1);
							}
							if(pendingWith.endsWith(",")){
								pendingWith = pendingWith.substring(0, pendingWith.length()-1);								
							}else{
								pendingWith = "--";
							 }
							
							if(emailIds.equals("") || emailIds.isEmpty()){
								emails = IDPAConstants.defaultMailId;
							}
							if(firstNames.equals("") || firstNames.isEmpty()){
								firstNames = IDPAConstants.defaultName;
							}
							WorkflowEvent workflowEvent = getLastEventOfEntityOrInstanceForStatus(entityTypeId, entityId, entityInstanceId, targetStatusId);
							if(emailNotification.equalsIgnoreCase("YES")){
								emailService.sendEmailWhenWorkflowStatusChange(emails, ccMailIds, firstNames, entityTypeName, instanceNames, workflowEvent.getModifiedBy().getFirstName(),workflowEvent.getComments(),workflowEvent.getLastUpdatedDate(), movedFrom, movedTo, pendingWith, slaDuration,entityInstanceId);
							}
					   }
					}
					
					if(!isSameStatusStage){
						if(possibleIDs != null && !possibleIDs.contains(sourceStatusId)){
							possibleIDs.add(sourceStatusId);
						}
						updateForAutoApproveStatus(productId, initialTargetStatus, entityTypeId, entityId, entityInstanceId, user, possibleIDs, isAutoApproveStatusChangeAllowed);
					}
					
					addWorkflowEventToMongoDB(workflowEvents);
				}
			}
		}catch(Exception e) {
			log.error("Error in Workflow Events", e);
		}
		return "";
	}
	
	private boolean checkStatusOfEntityInstance(Integer entityTypeId, Integer entityInstanceId, Integer sourceStatusId, List<Integer> possibleIds){
		boolean isStatusAlreadyChanged = false;
		try{
			Integer instanceCurrentStatusId = 0;
			if(entityTypeId == IDPAConstants.ENTITY_TEST_CASE_ID){
				TestCaseList testCaseList = testCaseListDAO.getByTestCaseId(entityInstanceId);
				if(testCaseList != null){
					instanceCurrentStatusId = testCaseList.getWorkflowStatus().getWorkflowStatusId();
				}
			}else if(entityTypeId == IDPAConstants.ENTITY_TASK_TYPE){
				ActivityTask activityTask = activityTaskDAO.getActivityTaskById(entityInstanceId, 0);
				if(activityTask != null){
					instanceCurrentStatusId = activityTask.getStatus().getWorkflowStatusId();
				}
			}
			if(instanceCurrentStatusId != 0 && instanceCurrentStatusId != sourceStatusId && !possibleIds.contains(instanceCurrentStatusId)){
				isStatusAlreadyChanged = true;
			}

		}catch(Exception ex){
			log.error("Error inside checking status of instance ",ex);
		}
		return isStatusAlreadyChanged;
	}
	
	private ActivityTask activityTaskUpdateBasedOnTracker(WorkflowEvent workflowEvent, Boolean isStatusChangeAllowed, Integer productId){
		ActivityTask activityTask = null;
		try{
			activityTask = activityTaskDAO.getActivityTaskById(workflowEvent.getEntityInstanceId(), 1);
			if(activityTask != null) {
				if(isStatusChangeAllowed == null || !isStatusChangeAllowed){
					activityTask.setStatus(activityTask.getStatus());
				}else{
					activityTask.setStatus(workflowEvent.getTargetStatus() == null ? workflowEvent.getCurrentStatus() : workflowEvent.getTargetStatus());
				}
			}
			
			workflowEvent.setInstanceObject(activityTask);
			
			ActivitySecondaryStatusMaster activitySecondaryStatusMaster = new ActivitySecondaryStatusMaster();
			activitySecondaryStatusMaster.setActivitySecondaryStatusId(1);
			activityTask.setSecondaryStatus(activitySecondaryStatusMaster);
			activityTask.setModifiedBy(workflowEvent.getModifiedBy());
			activityTask.setModifiedDate(workflowEvent.getLastUpdatedDate());

			Date currentDate = new Date();
			if(activityTask.getActualStartDate() == null || activityTask.getActualStartDate().after(currentDate)){
				activityTask.setActualStartDate(currentDate);
			}
			if(activityTask.getStatus() != null && activityTask.getStatus().getWorkflowStatusType() != null && "End".equalsIgnoreCase(activityTask.getStatus().getWorkflowStatusType())){
				if(activityTask.getActualEndDate() == null || activityTask.getActualEndDate().before(currentDate)){
					activityTask.setActualEndDate(currentDate);
				}
			}
			
			Integer eventEffort = workflowEvent.getActualEffort();
			if(eventEffort == null){
				eventEffort = 0;
			}
			if(activityTask.getTotalEffort() != null){
				activityTask.setTotalEffort((activityTask.getTotalEffort() + eventEffort));
			}else{
				activityTask.setTotalEffort(eventEffort);
			}
			
			if(activityTask.getStatus() != null && activityTask.getStatus().getWorkflow() != null && activityTask.getActivityTaskType() != null){
				Float percentageCompletion = getPercentageCompletionOfInstance(activityTask.getStatus().getWorkflow().getWorkflowId(), IDPAConstants.ENTITY_TASK_TYPE, activityTask.getActivityTaskType().getActivityTaskTypeId(), activityTask.getActivityTaskId(), productId, 1, "Instance", activityTask.getStatus().getWorkflowStatusName(), activityTask.getStatus().getWorkflowStatusType());
				activityTask.setPercentageCompletion(percentageCompletion);
			}else if(activityTask.getStatus() != null && activityTask.getStatus().getWorkflowStatusId() == -1){
				activityTask.setPercentageCompletion(100F);
			}else{
				activityTask.setPercentageCompletion(activityTask.getPercentageCompletion());
			}
			
			activityTaskDAO.updateActivityTask(activityTask);
			
			if(activityTask!=null && activityTask.getActivityTaskId()!=null){
				mongoDBService.addActivityTaskToMongoDB(activityTask.getActivityTaskId());
			}

			Activity activity = activityTask.getActivity();
			
			if(activity != null){
				int activityStatusCategoryId = activityTaskDAO.getActivityStatusCategoryIdByTaskStatus(activity.getActivityId());
				if(activityStatusCategoryId <= 0){
					activityStatusCategoryId = 1;
				}
				StatusCategory statusCategory = new StatusCategory();
				statusCategory.setStatusCategoryId(activityStatusCategoryId);
				activity.setStatusCategory(statusCategory);
				if(activity.getActualStartDate() == null || activity.getActualStartDate().after(currentDate)){
					activity.setActualStartDate(currentDate);
				}
				if(activity.getWorkflowStatus() != null && activity.getWorkflowStatus().getWorkflowStatusType() != null && "End".equalsIgnoreCase(activity.getWorkflowStatus().getWorkflowStatusType())){
					if(activity.getActualEndDate() == null || activity.getActualEndDate().before(currentDate)){
						activity.setActualEndDate(currentDate);
					}
				}
				
				if(activity.getTotalEffort() != null){
					activity.setTotalEffort((activity.getTotalEffort() + eventEffort));
				}else{
					activity.setTotalEffort(eventEffort);
				}
				
				activityDAO.updateActivity(activity);
				
				if(activity!=null){
					mongoDBService.addActivitytoMongoDB(activity.getActivityId());
				}

				ActivityWorkPackage activityWorkPackage = activity.getActivityWorkPackage();//activityWorkPackageService.getActivityWorkPackageById(activity.getActivityWorkPackage().getActivityWorkPackageId(), 1);
				int activityWorkpackageStatusCategoryId = activity.getActivityWorkPackage().getStatusCategory().getStatusCategoryId();//activityDAO.getActivityWorkpackageStatusCategoryIdByActivityStatusCategory(activity.getActivityWorkPackage().getActivityWorkPackageId());
				if(activityWorkpackageStatusCategoryId <= 0){
					activityWorkpackageStatusCategoryId = 1;
				}
				statusCategory.setStatusCategoryId(activityWorkpackageStatusCategoryId);
				activityWorkPackage.setStatusCategory(statusCategory);
				if(activityWorkPackage.getActualStartDate() == null || activityWorkPackage.getActualStartDate().after(currentDate)){
					activityWorkPackage.setActualStartDate(currentDate);
				}
				if(activityWorkPackage.getWorkflowStatus() != null && activityWorkPackage.getWorkflowStatus().getWorkflowStatusType() != null && "End".equalsIgnoreCase(activityWorkPackage.getWorkflowStatus().getWorkflowStatusType())){
					if(activityWorkPackage.getActualEndDate() == null || activityWorkPackage.getActualEndDate().before(currentDate)){
						activityWorkPackage.setActualEndDate(currentDate);
					}
				}
				
				if(activityWorkPackage.getTotalEffort() != null){
					activityWorkPackage.setTotalEffort((activityWorkPackage.getTotalEffort() + eventEffort));
				}else{
					activityWorkPackage.setTotalEffort(eventEffort);
				}
				
				activityWorkPackageDAO.updateActivityWorkPackage(activityWorkPackage);
				if(activityWorkPackage!=null){
					mongoDBService.addActivityWorkPackagetoMongoDB(activityWorkPackage.getActivityWorkPackageId());
				}
			}
		}catch(Exception ex){
			log.error("Error in activityTaskUpdateBasedOnTracker", ex);	   
		}
		return activityTask;
	}
	
	private Activity activityUpdateBasedOnTracker(WorkflowEvent workflowEvent, Boolean isStatusChangeAllowed, Integer productId){
		Activity activity = null;
		try{
			activity = activityDAO.getActivityById(workflowEvent.getEntityInstanceId(), 1);
			if(isStatusChangeAllowed == null || !isStatusChangeAllowed){
				activity.setWorkflowStatus(activity.getWorkflowStatus());
			}else{
				activity.setWorkflowStatus(workflowEvent.getTargetStatus() == null ? workflowEvent.getCurrentStatus() : workflowEvent.getTargetStatus());
			}
			
			workflowEvent.setInstanceObject(activity);
			
			activity.setModifiedBy(workflowEvent.getModifiedBy());
			activity.setModifiedDate(workflowEvent.getLastUpdatedDate());

			Date currentDate = new Date();
			
			if((activity.getActualStartDate() == null || activity.getActualStartDate().after(currentDate)) &&  !"Begin".equalsIgnoreCase(activity.getWorkflowStatus().getWorkflowStatusType()) ){
									activity.setActualStartDate(currentDate);
			}		
			if((activity.getWorkflowStatus() != null && activity.getWorkflowStatus().getWorkflowStatusType() != null)  && "End".equalsIgnoreCase(activity.getWorkflowStatus().getWorkflowStatusType())){
				if(activity.getActualEndDate() == null || activity.getActualEndDate().before(currentDate)){
						activity.setActualEndDate(currentDate);
				}
			}
			Integer eventEffort = workflowEvent.getActualEffort();
			if(eventEffort == null){
				eventEffort = 0;
			}
			if(activity.getTotalEffort() != null){
				activity.setTotalEffort((activity.getTotalEffort() + eventEffort));
			}else{
				activity.setTotalEffort(eventEffort);
			}
			
			Integer eventActualSize = workflowEvent.getActualSize();
			if(eventActualSize == null){
				eventActualSize = 0;
			}
			if(activity.getActualActivitySize() != null){
				activity.setActualActivitySize((activity.getActualActivitySize() + eventActualSize));
			}else{
				activity.setActualActivitySize(eventActualSize);
			}
			
			if(activity.getWorkflowStatus() != null && activity.getWorkflowStatus().getWorkflow() != null && activity.getActivityMaster() != null){
				Float percentageCompletion = getPercentageCompletionOfInstance(activity.getWorkflowStatus().getWorkflow().getWorkflowId(), IDPAConstants.ENTITY_ACTIVITY_TYPE, activity.getActivityMaster().getActivityMasterId(), activity.getActivityId(), productId, 1, "Instance", activity.getWorkflowStatus().getWorkflowStatusName(), activity.getWorkflowStatus().getWorkflowStatusType());
				activity.setPercentageCompletion(percentageCompletion);
			}else if(activity.getWorkflowStatus() != null && activity.getWorkflowStatus().getWorkflowStatusId() == -1){
				activity.setPercentageCompletion(100F);
			}else{
				activity.setPercentageCompletion(activity.getPercentageCompletion());
			}
			
			activityDAO.updateActivity(activity);
			
			if(activity!=null && activity.getActivityId()!=null){
				mongoDBService.addActivitytoMongoDB(activity.getActivityId());
			}
			ActivityWorkPackage activityWorkPackage = activity.getActivityWorkPackage();
			if(activityWorkPackage != null){
				int activityStatusCategoryId = activityDAO.getActivityWorkpackageStatusCategoryIdByActivityStatusCategory(activityWorkPackage.getActivityWorkPackageId());
				if(activityStatusCategoryId <= 0){
					activityStatusCategoryId = 1;
				}
				StatusCategory statusCategory = new StatusCategory();
				statusCategory.setStatusCategoryId(activityStatusCategoryId);
				activityWorkPackage.setStatusCategory(statusCategory);
				if(activityWorkPackage.getActualStartDate() == null || activityWorkPackage.getActualStartDate().after(currentDate)){
					activityWorkPackage.setActualStartDate(currentDate);
				}
				if(activityWorkPackage.getWorkflowStatus() != null && activityWorkPackage.getWorkflowStatus().getWorkflowStatusType() != null && "End".equalsIgnoreCase(activityWorkPackage.getWorkflowStatus().getWorkflowStatusType())){
					if(activityWorkPackage.getActualEndDate() == null || activityWorkPackage.getActualEndDate().before(currentDate)){
						activityWorkPackage.setActualEndDate(currentDate);
					}
				}
				if(activityWorkPackage.getTotalEffort() != null){
					activityWorkPackage.setTotalEffort((activityWorkPackage.getTotalEffort() + eventEffort));
				}else{
					activityWorkPackage.setTotalEffort(eventEffort);
				}
				activityWorkPackageDAO.updateActivityWorkPackage(activityWorkPackage);
				
				if(activityWorkPackage!=null){
					mongoDBService.addActivityWorkPackagetoMongoDB(activityWorkPackage);
				}
				
			}
		}catch(Exception ex){
			log.error("Error in activityUpdateBasedOnTracker", ex);	   
		}
		return activity;
	}
	
	private ActivityWorkPackage activityWorkpackageUpdateBasedOnTracker(WorkflowEvent workflowEvent, Boolean isStatusChangeAllowed, Integer productId){
		ActivityWorkPackage activityWorkPackage = null;
		try{
			activityWorkPackage = activityWorkPackageDAO.getActivityWorkPackageById(workflowEvent.getEntityInstanceId(), 1);
			if(isStatusChangeAllowed == null || !isStatusChangeAllowed){
				activityWorkPackage.setWorkflowStatus(activityWorkPackage.getWorkflowStatus());
			}else{
				activityWorkPackage.setWorkflowStatus(workflowEvent.getTargetStatus() == null ? workflowEvent.getCurrentStatus() : workflowEvent.getTargetStatus());
			}
			
			workflowEvent.setInstanceObject(activityWorkPackage);
			
			activityWorkPackage.setModifiedBy(workflowEvent.getModifiedBy());
			activityWorkPackage.setModifiedDate(workflowEvent.getLastUpdatedDate());

			Date currentDate = new Date();
			if(activityWorkPackage.getActualStartDate() == null || activityWorkPackage.getActualStartDate().after(currentDate)){
				activityWorkPackage.setActualStartDate(currentDate);
			}
			if(activityWorkPackage.getWorkflowStatus() != null && activityWorkPackage.getWorkflowStatus().getWorkflowStatusType() != null && "End".equalsIgnoreCase(activityWorkPackage.getWorkflowStatus().getWorkflowStatusType())){
				if(activityWorkPackage.getActualEndDate() == null || activityWorkPackage.getActualEndDate().before(currentDate)){
					activityWorkPackage.setActualEndDate(currentDate);
				}
			}
			Integer eventEffort = workflowEvent.getActualEffort();
			if(eventEffort == null){
				eventEffort = 0;
			}
			if(activityWorkPackage.getTotalEffort() != null){
				activityWorkPackage.setTotalEffort((activityWorkPackage.getTotalEffort() + eventEffort));
			}else{
				activityWorkPackage.setTotalEffort(eventEffort);
			}
			
			if(activityWorkPackage.getWorkflowStatus() != null && activityWorkPackage.getWorkflowStatus().getWorkflow() != null){
				Float percentageCompletion = getPercentageCompletionOfInstance(activityWorkPackage.getWorkflowStatus().getWorkflow().getWorkflowId(), IDPAConstants.ENTITY_ACTIVITY_WORKPACKAGE_ID, null, activityWorkPackage.getActivityWorkPackageId(), productId, 1, "Instance", activityWorkPackage.getWorkflowStatus().getWorkflowStatusName(), activityWorkPackage.getWorkflowStatus().getWorkflowStatusType());
				activityWorkPackage.setPercentageCompletion(percentageCompletion);
			}else if(activityWorkPackage.getWorkflowStatus() != null && activityWorkPackage.getWorkflowStatus().getWorkflowStatusId() == -1){
				activityWorkPackage.setPercentageCompletion(100F);
			}else{
				activityWorkPackage.setPercentageCompletion(activityWorkPackage.getPercentageCompletion());
			}
			
			activityWorkPackageDAO.updateActivityWorkPackage(activityWorkPackage);
			
			if(activityWorkPackage !=null && activityWorkPackage.getActivityWorkPackageId() != null){
				mongoDBService.addActivityWorkPackagetoMongoDB(activityWorkPackage.getActivityWorkPackageId());
			}

		}catch(Exception ex){
			log.error("Error in activityWorkpackageUpdateBasedOnTracker", ex);	   
		}
		return activityWorkPackage;
	}
	
	
	private ProductFeature productFeatureUpdateBasedOnTracker(WorkflowEvent workflowEvent, Boolean isStatusChangeAllowed, Integer productId){
		ProductFeature productFeature = null;
		try{
			productFeature = productListService.getByProductFeatureId(workflowEvent.getEntityInstanceId());
			if(isStatusChangeAllowed == null || !isStatusChangeAllowed){
				productFeature.setWorkflowStatus(productFeature.getWorkflowStatus());
			}else{
				productFeature.setWorkflowStatus(workflowEvent.getTargetStatus() == null ? workflowEvent.getCurrentStatus() : workflowEvent.getTargetStatus());
			}
			
			workflowEvent.setInstanceObject(productFeature);
			
			//productFeature.setModifiedBy(workflowEvent.getModifiedBy());
			productFeature.setModifiedDate(workflowEvent.getLastUpdatedDate());

			Date currentDate = new Date();
			
			Integer eventEffort = workflowEvent.getActualEffort();
			if(eventEffort == null){
				eventEffort = 0;
			}
			if(productFeature.getTotalEffort() != null){
				productFeature.setTotalEffort((productFeature.getTotalEffort() + eventEffort));
			}else{
				productFeature.setTotalEffort(eventEffort);
			}
			
			
			productListService.updateProductFeature(productFeature);
			
			if(productFeature !=null && productFeature.getProductFeatureId() != null){
				mongoDBService.addProductFeature(productFeature.getProductFeatureId());
			}

		}catch(Exception ex){
			log.error("Error in activityWorkpackageUpdateBasedOnTracker", ex);	   
		}
		return productFeature;
	}
	
	private Float getPercentageCompletionOfInstance(Integer workflowId, Integer entityTypeId, Integer entityId, Integer entityInstanceId, Integer levelId, Integer activeStatus, String statusPolicyType, String workflowStatusName, String workflowStatusType){
		Float percentageCompletion = 0.0F;
		try{
			if(workflowStatusType != null && !"End".equalsIgnoreCase(workflowStatusType) && !"Abort".equalsIgnoreCase(workflowStatusType)){
				List<WorkflowStatusPolicy> workflowStatusPolicies = workflowStatusPolicyDAO.getWorkflowStatusPolicies(workflowId, entityTypeId, entityId, entityInstanceId, levelId, activeStatus, statusPolicyType);
				Float overAllWeightage = 0.0F;
				Float currentWeightage = 0.0F;
				Boolean isCurrentStatusReached = false;
				if(workflowStatusPolicies != null && workflowStatusPolicies.size() > 0){
					for(WorkflowStatusPolicy workflowStatusPolicy : workflowStatusPolicies){
						Integer weightage = 0;
						if(workflowStatusPolicy.getWeightage() != null){
							weightage = workflowStatusPolicy.getWeightage();
						}
						overAllWeightage += weightage;
						if(!isCurrentStatusReached){
							if(workflowStatusName.equalsIgnoreCase(workflowStatusPolicy.getWorkflowStatus().getWorkflowStatusName())){
								isCurrentStatusReached = true;
							}else{
								currentWeightage += weightage;
							}
						}
					}
				}
				
				if(overAllWeightage > 0){
					percentageCompletion = (currentWeightage / overAllWeightage) * 100;
				}
			}else if(workflowStatusType == null){
				percentageCompletion = 0F;
			}else{
				percentageCompletion = 100F;
			}
			
		}catch(Exception ex){
			log.error("Error in getPercentageCompletionOfInstance - ", ex);
		}
		return percentageCompletion;
	}
	
	private void updateInstanceStatusPolicyActualValues(Integer productId, Integer sourceStatusId, Integer targetStatusId, Integer entityTypeId, Integer entityId, Integer entityInstanceId){
		try{
			WorkflowStatusPolicy sourceWorkflowStatusPolicy = workflowStatusPolicyDAO.getSLADurationForEntityOrInstance(entityTypeId, entityId, entityInstanceId, sourceStatusId);
			WorkflowStatusPolicy targetWorkflowStatusPolicy = workflowStatusPolicyDAO.getSLADurationForEntityOrInstance(entityTypeId, entityId, entityInstanceId, targetStatusId);
		
			Date statusChangeDate = new Date();
			
			if(sourceWorkflowStatusPolicy != null){
				if(sourceWorkflowStatusPolicy.getActualEndDate() == null || sourceWorkflowStatusPolicy.getActualEndDate().before(statusChangeDate)){
					sourceWorkflowStatusPolicy.setActualEndDate(statusChangeDate);
				}
				if(sourceWorkflowStatusPolicy.getActualStartDate() == null){
					sourceWorkflowStatusPolicy.setActualStartDate(statusChangeDate);
				}
				if(sourceWorkflowStatusPolicy.getPlannedStartDate() == null){
					sourceWorkflowStatusPolicy.setPlannedStartDate(statusChangeDate);
				}
				if(sourceWorkflowStatusPolicy.getPlannedEndDate() == null){
					sourceWorkflowStatusPolicy.setPlannedEndDate(statusChangeDate);
				}
				Integer totalEffortsOfStatus = workflowEventDAO.getTotalEffortsOfEntityOrInstanceIdForStatus(productId, entityTypeId, entityId, entityInstanceId, sourceStatusId);
				if(totalEffortsOfStatus != null){
					sourceWorkflowStatusPolicy.setActualEffort(totalEffortsOfStatus.floatValue());
				}else{
					sourceWorkflowStatusPolicy.setActualEffort(0.0F);
				}
				workflowStatusPolicyDAO.updateWorkflowStatusPolicy(sourceWorkflowStatusPolicy, sourceWorkflowStatusPolicy.getWorkflowStatus().getWorkflow().getWorkflowId());
			}
			
			if(targetWorkflowStatusPolicy != null){
				if(targetWorkflowStatusPolicy.getActualStartDate() == null || targetWorkflowStatusPolicy.getActualStartDate().after(statusChangeDate)){
					targetWorkflowStatusPolicy.setActualStartDate(statusChangeDate);
				}
				if(targetWorkflowStatusPolicy.getWorkflowStatus() != null && targetWorkflowStatusPolicy.getWorkflowStatus().getWorkflowStatusType() != null && IDPAConstants.WORKFLOW_STATUS_TYPE_END.equalsIgnoreCase(targetWorkflowStatusPolicy.getWorkflowStatus().getWorkflowStatusType().trim())){
					targetWorkflowStatusPolicy.setActualEndDate(statusChangeDate);
				}
				if(targetWorkflowStatusPolicy.getPlannedStartDate() == null){
					targetWorkflowStatusPolicy.setPlannedStartDate(statusChangeDate);
				}
				if(targetWorkflowStatusPolicy.getPlannedEndDate() == null){
					targetWorkflowStatusPolicy.setPlannedEndDate(statusChangeDate);
				}
				
				workflowStatusPolicyDAO.updateWorkflowStatusPolicy(targetWorkflowStatusPolicy, targetWorkflowStatusPolicy.getWorkflowStatus().getWorkflow().getWorkflowId());
			}
		}catch(Exception ex){
			log.error("Exception in updateInstanceStatusPolicyActualValues - ", ex);
		}
	}
	
	@Override
	public void checkAndUpdateStatusOfInstanceParentOrChild(Integer productId, Integer entityTypeId, Integer entityInstanceId, Object instanceObject){
		try{
			HashMap<String, Object> contraints = new HashMap<String, Object>();
			WorkflowMaster workflowMaster = null;
			WorkflowStatus workflowStatus = null;
			String autoStatusUpdateComment = " -- Auto status update by system, based on the event occured to ";
			if(entityTypeId == IDPAConstants.ENTITY_TASK_TYPE){
				workflowStatus = null;
				ActivityTask activityTask = (ActivityTask) instanceObject;
				contraints.put("activityTask.activity.activityId", activityTask.getActivity().getActivityId());
				workflowMaster = activityTask.getActivity().getWorkflowStatus().getWorkflow();
				if(workflowMaster != null){
					workflowStatus = updateStatusForParentOrChildByStatusType(ActivityTask.class, "activityTask", contraints, "activityTask.status.workflowStatusId", productId, workflowMaster.getWorkflowId(), activityTask.getActivity().getWorkflowStatus().getWorkflowStatusType(), activityTask.getActivity(), workflowStatus, true, autoStatusUpdateComment+"task ["+activityTask.getActivityTaskId()+"] - "+activityTask.getActivityTaskName());
				}
				
				workflowStatus = null;
				contraints = new HashMap<String, Object>();
				Activity activity = activityTask.getActivity();
				contraints.put("activity.activityWorkPackage.activityWorkPackageId", activity.getActivityWorkPackage().getActivityWorkPackageId());
				if(activityTask!=null){
					if(activityTask.getActivity()!=null){
						if(activityTask.getActivity().getActivityWorkPackage()!=null){
							if(activityTask.getActivity().getActivityWorkPackage().getWorkflowStatus()!=null){
								workflowMaster = activityTask.getActivity().getActivityWorkPackage().getWorkflowStatus().getWorkflow();
							}						
						}
					}
				}
				if(workflowMaster != null){
					workflowStatus = updateStatusForParentOrChildByStatusType(Activity.class, "activity", contraints, "activity.workflowStatus.workflowStatusId", productId, workflowMaster.getWorkflowId(), activityTask.getActivity().getActivityWorkPackage().getWorkflowStatus().getWorkflowStatusType(), activityTask.getActivity().getActivityWorkPackage(), workflowStatus, true, autoStatusUpdateComment+"activity ["+activity.getActivityId()+"] - "+activity.getActivityName());
				}
				
			}else if(entityTypeId == IDPAConstants.ENTITY_ACTIVITY_TYPE){
				workflowStatus = null;
				Activity activity = (Activity) instanceObject;
				contraints.put("activity.activityWorkPackage.activityWorkPackageId", activity.getActivityWorkPackage().getActivityWorkPackageId());
				if(activity.getActivityWorkPackage() != null) {
					workflowStatus=activity.getActivityWorkPackage().getWorkflowStatus();
					if(null != workflowStatus && null != workflowStatus.getWorkflow()) {
						workflowMaster = workflowStatus.getWorkflow();
					}
				}
				if(workflowMaster != null){
					workflowStatus = updateStatusForParentOrChildByStatusType(Activity.class, "activity", contraints, "activity.workflowStatus.workflowStatusId", productId, workflowMaster.getWorkflowId(), activity.getActivityWorkPackage().getWorkflowStatus().getWorkflowStatusType(), activity.getActivityWorkPackage(), workflowStatus, true, autoStatusUpdateComment+"activity ["+activity.getActivityId()+"] - "+activity.getActivityName());
				}
				
				workflowStatus = null;
				contraints = new HashMap<String, Object>();
				List<ActivityTask> activityTasks = activityTaskDAO.getActivityTaskByActivityId(activity.getActivityId());
				for(ActivityTask activityTask : activityTasks){
					contraints.put("activity.activityId", activity.getActivityId());
					workflowMaster = activityTask.getStatus().getWorkflow();
					if(workflowMaster != null){
						workflowStatus = updateStatusForParentOrChildByStatusType(Activity.class, "activity", contraints, "activity.workflowStatus.workflowStatusId", productId, workflowMaster.getWorkflowId(), activityTask.getStatus().getWorkflowStatusType(), activityTask, workflowStatus, false, autoStatusUpdateComment+"activity ["+activity.getActivityId()+"] - "+activity.getActivityName());
					}
				}
				
			}else if(entityTypeId == IDPAConstants.ENTITY_ACTIVITY_WORKPACKAGE_ID){
				ActivityWorkPackage activityWorkPackage = (ActivityWorkPackage) instanceObject;
				workflowStatus = null;
				contraints = new HashMap<String, Object>();
				List<Activity> activities = activityDAO.listActivitiesByActivityWorkPackageId(activityWorkPackage.getActivityWorkPackageId(), 1, 2);
				for(Activity activity : activities){
					contraints.put("activityWP.activityWorkPackageId", activityWorkPackage.getActivityWorkPackageId());
					workflowMaster = activity.getWorkflowStatus().getWorkflow();
					if(workflowMaster != null){
						workflowStatus = updateStatusForParentOrChildByStatusType(ActivityWorkPackage.class, "activityWP", contraints, "activityWP.workflowStatus.workflowStatusId", productId, workflowMaster.getWorkflowId(), activity.getWorkflowStatus().getWorkflowStatusType(), activity, workflowStatus, false, autoStatusUpdateComment+"activity workpackage ["+activityWorkPackage.getActivityWorkPackageId()+"] - "+activityWorkPackage.getActivityWorkPackageName());
					}
					
					WorkflowStatus workflowStatusChild = null;
					HashMap<String, Object> contraintsChild = new HashMap<String, Object>();
					WorkflowMaster workflowMasterChild = null;
					List<ActivityTask> activityTasks = activityTaskDAO.getActivityTaskByActivityId(activity.getActivityId());
					for(ActivityTask activityTask : activityTasks){
						contraints.put("activity.activityId", activity.getActivityId());
						workflowMasterChild = activityTask.getStatus().getWorkflow();
						if(workflowMasterChild != null){
							workflowStatusChild = updateStatusForParentOrChildByStatusType(Activity.class, "activity", contraintsChild, "activity.workflowStatus.workflowStatusId", productId, workflowMasterChild.getWorkflowId(), activityTask.getStatus().getWorkflowStatusType(), activityTask, workflowStatusChild, false, autoStatusUpdateComment+"activity ["+activity.getActivityId()+"] - "+activity.getActivityName());
						}
					}
					
				}
			}
		}catch(Exception ex){
			log.error("Exception in checkAndUpdateStatusOfInstanceParentOrChild - ", ex);
		}
	}
	
	@SuppressWarnings("rawtypes")
	private WorkflowStatus updateStatusForParentOrChildByStatusType(Class className, String aliasName, HashMap<String, Object> constraints, String projection, Integer productId, Integer workflowId, String currentStatusType, Object instanceObject, WorkflowStatus workflowStatus, Boolean isParentUpdate, String autoStatusUpdateComment){
		try{
			if(workflowStatus == null){
				String statusType = "";
				List<String> statusTypes = workflowStatusDAO.getWorflowStatusTypes(className, aliasName, constraints, projection);
				if(statusTypes != null){
					if(statusTypes.contains(IDPAConstants.WORKFLOW_STATUS_TYPE_INTERMEDIATE)){
						statusType = IDPAConstants.WORKFLOW_STATUS_TYPE_INTERMEDIATE;
					}else if(!statusTypes.contains(IDPAConstants.WORKFLOW_STATUS_TYPE_BEGIN) && !statusTypes.contains(IDPAConstants.WORKFLOW_STATUS_TYPE_ABORT) && statusTypes.contains(IDPAConstants.WORKFLOW_STATUS_TYPE_END)){
						statusType = IDPAConstants.WORKFLOW_STATUS_TYPE_END;
					}else if(!statusTypes.contains(IDPAConstants.WORKFLOW_STATUS_TYPE_BEGIN) && statusTypes.contains(IDPAConstants.WORKFLOW_STATUS_TYPE_ABORT)){
						statusType = IDPAConstants.WORKFLOW_STATUS_TYPE_ABORT;
					}else if(statusTypes.contains(IDPAConstants.WORKFLOW_STATUS_TYPE_BEGIN) && !statusTypes.contains(IDPAConstants.WORKFLOW_STATUS_TYPE_ABORT) && !statusTypes.contains(IDPAConstants.WORKFLOW_STATUS_TYPE_END)){
						statusType = IDPAConstants.WORKFLOW_STATUS_TYPE_BEGIN;
					}else{
						statusType = IDPAConstants.WORKFLOW_STATUS_TYPE_INTERMEDIATE;
					}
				}
				
				if(statusType != null && !statusType.trim().isEmpty() && !statusType.equalsIgnoreCase(currentStatusType)){
					workflowStatus = workflowStatusDAO.getFirstWorkflowStatusOfStatusType(workflowId, statusType, "asc");
					Integer counter = 0;
					while(workflowStatus == null && counter < 4){
						if(statusType.equalsIgnoreCase(IDPAConstants.WORKFLOW_STATUS_TYPE_INTERMEDIATE)){
							statusType = IDPAConstants.WORKFLOW_STATUS_TYPE_BEGIN;
						}else if(statusType.equalsIgnoreCase(IDPAConstants.WORKFLOW_STATUS_TYPE_END)){
							statusType = IDPAConstants.WORKFLOW_STATUS_TYPE_INTERMEDIATE;
						}else if(statusType.equalsIgnoreCase(IDPAConstants.WORKFLOW_STATUS_TYPE_ABORT)){
							statusType = IDPAConstants.WORKFLOW_STATUS_TYPE_END;
						}
						workflowStatus = workflowStatusDAO.getFirstWorkflowStatusOfStatusType(workflowId, statusType, "desc");
						counter++;
					}
				}
			}
			
			if(workflowStatus != null){
				Integer entityTypeId = null;
				Integer entityId = null;
				Integer entityInstanceId = null;
				Integer sourceStatusId = null;
				Integer targetStatusId = null;
				if(instanceObject instanceof ActivityTask){
					ActivityTask activityTask = (ActivityTask) instanceObject;
					if(activityTask.getStatus() == null || (!"End".equalsIgnoreCase(activityTask.getStatus().getWorkflowStatusType()) && !"Abort".equalsIgnoreCase(activityTask.getStatus().getWorkflowStatusType()))){
						entityTypeId = IDPAConstants.ENTITY_TASK_TYPE;
						if(activityTask.getActivityTaskType() != null){
							entityId = activityTask.getActivityTaskType().getActivityTaskTypeId();
						}
						entityInstanceId = activityTask.getActivityTaskId();
						sourceStatusId = activityTask.getStatus().getWorkflowStatusId();
						targetStatusId = workflowStatus.getWorkflowStatusId();
						
						activityTask.setStatus(workflowStatus);
						if(activityTask.getStatus() != null && activityTask.getStatus().getWorkflow() != null && activityTask.getActivityTaskType() != null){
							Float percentageCompletion = getPercentageCompletionOfInstance(activityTask.getStatus().getWorkflow().getWorkflowId(), IDPAConstants.ENTITY_TASK_TYPE, activityTask.getActivityTaskType().getActivityTaskTypeId(), activityTask.getActivityTaskId(), productId, 1, "Instance", activityTask.getStatus().getWorkflowStatusName(), activityTask.getStatus().getWorkflowStatusType());
							activityTask.setPercentageCompletion(percentageCompletion);
						}else if(activityTask.getStatus() != null && activityTask.getStatus().getWorkflowStatusId() == -1){
							activityTask.setPercentageCompletion(100F);
						}else{
							activityTask.setPercentageCompletion(0F);
						}
						
						activityTaskDAO.updateActivityTask(activityTask);
					}
					
				}else if(instanceObject instanceof Activity){
					Activity activity = (Activity) instanceObject;
					if(isParentUpdate || activity.getWorkflowStatus() == null || (!"End".equalsIgnoreCase(activity.getWorkflowStatus().getWorkflowStatusType()) && !"Abort".equalsIgnoreCase(activity.getWorkflowStatus().getWorkflowStatusType()))){
						entityTypeId = IDPAConstants.ENTITY_ACTIVITY_TYPE;
						if(activity.getActivityMaster() != null){
							entityId = activity.getActivityMaster().getActivityMasterId();
						}
						entityInstanceId = activity.getActivityId();
						sourceStatusId = activity.getWorkflowStatus().getWorkflowStatusId();
						targetStatusId = workflowStatus.getWorkflowStatusId();
						if(activity.getWorkflowStatus() != null && activity.getWorkflowStatus().getWorkflow() != null && activity.getActivityMaster() != null){
							Float percentageCompletion = getPercentageCompletionOfInstance(activity.getWorkflowStatus().getWorkflow().getWorkflowId(), IDPAConstants.ENTITY_ACTIVITY_TYPE, activity.getActivityMaster().getActivityMasterId(), activity.getActivityId(), productId, 1, "Instance", activity.getWorkflowStatus().getWorkflowStatusName(), activity.getWorkflowStatus().getWorkflowStatusType());
							activity.setPercentageCompletion(percentageCompletion);
						}else if(activity.getWorkflowStatus() != null && activity.getWorkflowStatus().getWorkflowStatusId() == -1){
							activity.setPercentageCompletion(100F);
						}else{
							activity.setPercentageCompletion(0F);
						}
						
						activity.setWorkflowStatus(workflowStatus);
						activityDAO.updateActivity(activity);
					}
					
				}else if(instanceObject instanceof ActivityWorkPackage){
					ActivityWorkPackage activityWorkPackage = (ActivityWorkPackage) instanceObject;
					if(isParentUpdate || activityWorkPackage.getWorkflowStatus() == null || (!"End".equalsIgnoreCase(activityWorkPackage.getWorkflowStatus().getWorkflowStatusType()) && !"Abort".equalsIgnoreCase(activityWorkPackage.getWorkflowStatus().getWorkflowStatusType()))){
						entityTypeId = IDPAConstants.ENTITY_ACTIVITY_WORKPACKAGE_ID;
						entityInstanceId = activityWorkPackage.getActivityWorkPackageId();
						sourceStatusId = activityWorkPackage.getWorkflowStatus().getWorkflowStatusId();
						targetStatusId = workflowStatus.getWorkflowStatusId();
						if(activityWorkPackage.getWorkflowStatus() != null && activityWorkPackage.getWorkflowStatus().getWorkflow() != null){
							Float percentageCompletion = getPercentageCompletionOfInstance(activityWorkPackage.getWorkflowStatus().getWorkflow().getWorkflowId(), IDPAConstants.ENTITY_ACTIVITY_WORKPACKAGE_ID, null, activityWorkPackage.getActivityWorkPackageId(), productId, 1, "Instance", activityWorkPackage.getWorkflowStatus().getWorkflowStatusName(), activityWorkPackage.getWorkflowStatus().getWorkflowStatusType());
							activityWorkPackage.setPercentageCompletion(percentageCompletion);
						}else if(activityWorkPackage.getWorkflowStatus() != null && activityWorkPackage.getWorkflowStatus().getWorkflowStatusId() == -1){
							activityWorkPackage.setPercentageCompletion(100F);
						}else{
							activityWorkPackage.setPercentageCompletion(0F);
						}
						
						activityWorkPackage.setWorkflowStatus(workflowStatus);
						activityWorkPackageDAO.updateActivityWorkPackage(activityWorkPackage);
					}
				}
				UserList user = new UserList();
				user.setUserId(-3);
				setInstanceAutoStatusUpdateEvent(productId, entityTypeId, entityId, entityInstanceId, sourceStatusId, targetStatusId, 1, 0, autoStatusUpdateComment, user);
			}
		}catch(Exception ex){
			log.error("Exception in getStatusTypeForParentOrChild - ", ex);
		}
		return workflowStatus;
	}
	
	public void setInstanceAutoStatusUpdateEvent(Integer productId, Integer entityTypeId, Integer entityId, Integer entityInstanceId, Integer sourceStatusId, Integer targetStatusId, Integer secondaryStatusId, Integer effort, String comments, UserList user){
		try{
			WorkflowEvent workflowEvent = new WorkflowEvent();
			workflowEvent.setEntityId(entityId);
			workflowEvent.setEntityInstanceId(entityInstanceId);
			EntityMaster entityMaster = new EntityMaster();
			entityMaster.setEntitymasterid(entityTypeId);
			workflowEvent.setEntityType(entityMaster);
			WorkflowStatus workflowSourceStatus = new WorkflowStatus();
			workflowSourceStatus.setWorkflowStatusId(sourceStatusId);
			workflowEvent.setCurrentStatus(workflowSourceStatus);
			WorkflowStatus workflowTargetStatus = new WorkflowStatus();
			workflowTargetStatus.setWorkflowStatusId(targetStatusId);
			workflowEvent.setTargetStatus(workflowTargetStatus);
			workflowEvent.setModifiedBy(user);
			workflowEvent.setLastUpdatedDate(new Date());
			workflowEvent.setPlannedStartDate(new Date());
			workflowEvent.setPlannedEndDate(new Date());
			workflowEvent.setActualStartDate(new Date());
			workflowEvent.setActualEndDate(new Date());
			workflowEvent.setPlannedEffort(0);
			workflowEvent.setActualEffort(0);
			workflowEvent.setEntityGroupId(1);
			workflowEvent.setSlaDurationPlanned(0);
			workflowEvent.setSlaDurationActual(0);
			workflowEvent.setComments(comments);
			ProductMaster product= new ProductMaster();
			product.setProductId(productId);
			workflowEvent.setProduct(product);
			workflowEventDAO.addWorkflowEvent(workflowEvent);
		}catch(Exception e) {
			log.error("Error in setInstanceAutoStatusUpdateEvent ", e );
		}
	}
	
	private void updateUserActionStatus(Integer productId, Integer sourceStatusId, Integer targetStatusId, Integer entityTypeId, Integer entityId, Integer entityInstanceId, Integer userId){
		try{
			Integer activeStatus = 1;
			WorkflowStatusPolicy workflowStatusPolicy = workflowStatusPolicyDAO.getWorkflowStatusPolicieByWorkflowStatusId(sourceStatusId, entityTypeId, entityId, entityInstanceId, productId, activeStatus, IDPAConstants.WORKFLOW_STATUS_POLICY_TYPE_INSTANCE);
			if(workflowStatusPolicy != null && IDPAConstants.WORKFLOW_STATUS_POLICY_USER_ACTION_SCOPE.equalsIgnoreCase(workflowStatusPolicy.getActionScope())){
				List<WorkflowStatusActor> workflowStatusActors = workflowStatusPolicyDAO.getWorkflowStatusActor(productId, sourceStatusId, entityTypeId, entityId, entityInstanceId, IDPAConstants.WORKFLOW_STATUS_POLICY_USER_ACTION_SCOPE, userId, null);
				if(workflowStatusActors != null && workflowStatusActors.size() > 0){
					for(WorkflowStatusActor workflowStatusActor : workflowStatusActors){
						workflowStatusActor.setUserActionStatus(IDPAConstants.WORKFLOW_STATUS_USER_ACTION_COMPELTED);
						workflowStatusPolicyDAO.addWorkflowStatusActor(workflowStatusActor);
					}
				}
			}
			
			workflowStatusPolicy = workflowStatusPolicyDAO.getWorkflowStatusPolicieByWorkflowStatusId(targetStatusId, entityTypeId, entityId, entityInstanceId, productId, activeStatus, IDPAConstants.WORKFLOW_STATUS_POLICY_TYPE_INSTANCE);
			if(workflowStatusPolicy != null && IDPAConstants.WORKFLOW_STATUS_POLICY_USER_ACTION_SCOPE.equalsIgnoreCase(workflowStatusPolicy.getActionScope())){
				List<WorkflowStatusActor> workflowStatusActors = workflowStatusPolicyDAO.getWorkflowStatusActor(productId, targetStatusId, entityTypeId, entityId, entityInstanceId, IDPAConstants.WORKFLOW_STATUS_POLICY_USER_ACTION_SCOPE, null, null);
				if(workflowStatusActors != null && workflowStatusActors.size() > 0){
					for(WorkflowStatusActor workflowStatusActor : workflowStatusActors){
						workflowStatusActor.setUserActionStatus(IDPAConstants.WORKFLOW_STATUS_USER_ACTION_NOT_COMPELTE);
						workflowStatusPolicyDAO.addWorkflowStatusActor(workflowStatusActor);
					}
				}
			}
		}catch(Exception ex){
			log.error("Error while updating user action status ", ex);
		}
	}
	
	private void updateForAutoApproveStatus(Integer productId, Integer sourceStatusId, Integer entityTypeId, Integer entityId, Integer entityInstanceId, UserList user, List<Integer> possibleIds, Boolean isAutoApproveStatusChangeAllowed){
		Integer activeStatus = 1;
		WorkflowStatusPolicy workflowStatusPolicy = workflowStatusPolicyDAO.getWorkflowStatusPolicieByWorkflowStatusId(sourceStatusId, entityTypeId, entityId, entityInstanceId, productId, activeStatus, IDPAConstants.WORKFLOW_STATUS_POLICY_TYPE_INSTANCE);
		if(workflowStatusPolicy != null && "Auto Approve".equalsIgnoreCase(workflowStatusPolicy.getStautsTransitionPolicy()) && workflowStatusPolicy.getWorkflowStatus() != null && workflowStatusPolicy.getWorkflowStatus().getWorkflowStatusName() != null && !workflowStatusPolicy.getWorkflowStatus().getWorkflowStatusName().trim().isEmpty()){
			WorkflowStatus currentStatus = workflowStatusPolicy.getWorkflowStatus();
			List<WorkflowStatus> possibleWorkflowStatuses = workflowStatusDAO.getPossibleWorkflowStatusForAction(currentStatus.getWorkflowStatusId());
			for(WorkflowStatus workflowStatus : possibleWorkflowStatuses){
				if(workflowStatus != null && workflowStatus.getWorkflowStatusId() != currentStatus.getWorkflowStatusId() && workflowStatus.getStatusOrder() >= currentStatus.getStatusOrder()){
					Integer targetStatusId = workflowStatus.getWorkflowStatusId();
					addWorkflowEvent(productId, entityInstanceId+"", user, entityId, entityTypeId, targetStatusId, targetStatusId, 0, " -- Auto Approved to next status", sourceStatusId, entityInstanceId, "", possibleIds, isAutoApproveStatusChangeAllowed, null,0);
					break;
				}
			}
				
		}
	}
	
	private boolean checkMandatoryUserActionsToChangeStatus(Integer productId, Integer sourceStatusId, Integer entityTypeId, Integer entityId, Integer entityInstanceId){
		boolean isStatusChangeAllowed = false;
		try{
			Integer activeStatus = 1;
			WorkflowStatusPolicy workflowStatusPolicy = workflowStatusPolicyDAO.getWorkflowStatusPolicieByWorkflowStatusId(sourceStatusId, entityTypeId, entityId, entityInstanceId, productId, activeStatus, IDPAConstants.WORKFLOW_STATUS_POLICY_TYPE_INSTANCE);
			if(workflowStatusPolicy != null && IDPAConstants.WORKFLOW_STATUS_POLICY_USER_ACTION_SCOPE.equalsIgnoreCase(workflowStatusPolicy.getActionScope())){
				isStatusChangeAllowed = workflowStatusPolicyDAO.checkMandatoryUserActionsToChangeStatus(productId, sourceStatusId, entityTypeId, entityId, entityInstanceId, IDPAConstants.WORKFLOW_STATUS_POLICY_USER_ACTION_SCOPE, null, null);
			}else{
				isStatusChangeAllowed = true;
			}
			
		}catch(Exception ex){
			log.error("Error while checking mandatory user actions for status change ", ex);
		}
		return isStatusChangeAllowed;
	}
	
	private void updateAttachmentForWorkflowEvent(Integer workflowEventId, String attachmentIds){
		try{
			String[] attachmentIdSplits = attachmentIds.split(",");
			for(String attachmentId : attachmentIdSplits){
				if(!NumberUtils.isNumber(attachmentId)){
					continue;
				}
				Attachment attachment = commonService.getAttachmentById(Integer.parseInt(attachmentId));
				if(attachment != null){
					attachment.setEntityId(workflowEventId);
					
					String sourceAttachmentLocation = attachment.getAttributeFileURI();
					String destinationAttachmentLocation = sourceAttachmentLocation.substring(0, sourceAttachmentLocation.indexOf(IDPAConstants.ENTITY_WORKFLOW_EVENT_ID+""));
					destinationAttachmentLocation += IDPAConstants.ENTITY_WORKFLOW_EVENT_ID+"\\"+workflowEventId;
					
					File sourceAttachment = new File(sourceAttachmentLocation);
					if(sourceAttachment != null && sourceAttachment.exists() && sourceAttachment.isFile()){
						FileInputStream sourceAttachmentFileStream = new FileInputStream(sourceAttachment);
						
						File destinationAttachment = new File(destinationAttachmentLocation);
						if(!destinationAttachment.exists() || !destinationAttachment.isDirectory()){
							FileUtils.forceMkdir(destinationAttachment);
						}
						destinationAttachmentLocation += "\\"+attachment.getAttributeFileName()+attachment.getAttributeFileExtension();
						attachment.setAttributeFileURI(destinationAttachmentLocation);
						
						FileOutputStream targetAttachmentFileStream = new FileOutputStream(destinationAttachmentLocation);
						
						byte[] buffer = new byte[1024];
			    	    int length;
			    	    while ((length = sourceAttachmentFileStream.read(buffer)) > 0){
			    	    	targetAttachmentFileStream.write(buffer, 0, length);
			    	    }

			    	    targetAttachmentFileStream.flush();
			    	    sourceAttachmentFileStream.close();
			    	    targetAttachmentFileStream.close();
			    	    
			    	    sourceAttachment.delete();
			    	    commonService.updateAttachment(attachment);
					}else{
						commonService.deleteAttachment(attachment);
					}
				}
			}
		}catch(Exception ex){
			log.error("Error in updateAttachmentForWorkflowEvent ", ex);
		}
	}
	
	private void addWorkflowEventToMongoDB(WorkflowEvent workflowEvent){
		if(workflowEvent != null && workflowEvent.getWorkflowEventId() != null){
			try{
				mongoDBService.addWorkflowEventToMongoDB(workflowEvent.getWorkflowEventId());
			}catch(Exception ex){
				log.error("Exception while adding workflowEvent to mongo DB - ", ex);
			}
		}
	}
	
	@Override
	@Transactional
	public List<WorkflowEvent> listWorkflowEventByEntityTypeAndEnityInstanceId(Integer entityTypeId,Integer entityInstanceId, int initializationLevel){
		return workflowEventDAO.listWorkflowEventByEntityTypeAndEnityInstanceId(entityTypeId, entityInstanceId, initializationLevel);
	}
	
	@Override
	@Transactional
	public void updateWorkflowEvent(WorkflowEvent workflowEvents){
		workflowEventDAO.updateWorkflowEvent(workflowEvents);
	}
	
	@Override
	@Transactional
	public Integer getTotalEffortsByEntityInstanceIdAndEntityType(int entityId,int entityTypeId) {
		return workflowEventDAO.getTotalEffortsByEntityInstanceIdAndEntityType(entityId, entityTypeId);
	}
	
	@Override
	@Transactional
	public Integer countAllEvents(Date startDate, Date endDate) {
		return workflowEventDAO.countAllEvents(startDate, endDate);
	}
	
	@Override
	@Transactional
	public List<WorkflowEvent> listAllEvents(int startIndex, int pageSize, Date startDate,Date endDate) {
		return workflowEventDAO.listAllEvents(startIndex, pageSize, startDate, endDate);
	}

	@Override
	@Transactional
	public WorkflowEvent getLatestWorkflowEventAction(Integer entityInstanceId,Integer entityTypeId) {
		WorkflowEvent referBackWorkflowEvent=  workflowEventDAO.getLatestWorkflowEventAction(entityInstanceId, entityTypeId);
		
		if(referBackWorkflowEvent != null && referBackWorkflowEvent.getCurrentStatus() != null) {
			WorkflowStatus referbackWorkflowStatus =new WorkflowStatus();
			Integer referBackWorkflowStatusId=referBackWorkflowEvent.getCurrentStatus().getWorkflowStatusId();
			log.info("ReferBack Stage:"+referBackWorkflowStatusId);
			referbackWorkflowStatus.setWorkflowStatusId(referBackWorkflowStatusId);
			referBackWorkflowEvent.setTargetStatus(referbackWorkflowStatus);
			return referBackWorkflowEvent;
		}
		return null;
	}
	
	
	private TestCaseList testCaseUpdateBasedOnTracker(WorkflowEvent workflowEvent, Boolean isStatusChangeAllowed, Integer productId){
		TestCaseList testcase = null;
		try{
			testcase = testCaseListDAO.getByTestCaseId(workflowEvent.getEntityInstanceId());
			if(isStatusChangeAllowed == null || !isStatusChangeAllowed){
				testcase.setWorkflowStatus(testcase.getWorkflowStatus());
			}else{
				testcase.setWorkflowStatus(workflowEvent.getTargetStatus() == null ? workflowEvent.getCurrentStatus() : workflowEvent.getTargetStatus());
			}
			
			workflowEvent.setInstanceObject(testcase);
			Integer totalTaskEfforts = getTotalEffortsByEntityInstanceIdAndEntityType(workflowEvent.getEntityInstanceId(), IDPAConstants.ENTITY_TEST_CASE_ID);
			testcase.setTotalEffort(totalTaskEfforts);
			testCaseListDAO.update(testcase);
		
		}catch(Exception ex){
			log.error("Error in activityTaskUpdateBasedOnTracker", ex);	   
		}
		return testcase;
	}

	@Override
	@Transactional
	public boolean checkInstanceEligibiltyToChangeWorkflowMapping(Integer entityTypeId, Integer entityId, Integer entityInstanceId) {
		return workflowEventDAO.checkInstanceEligibiltyToChangeWorkflowMapping(entityTypeId, entityId, entityInstanceId);
	}
	
	@Override
	@Transactional
	public void setInitialInstanceEvent(Integer productId, Integer entityTypeId, Integer entityId, Integer entityInstanceId, Integer statusId, Integer secondaryStatusId, Integer effort, String comments, UserList user){
		try{
		WorkflowEvent workflowEvent = new WorkflowEvent();
		workflowEvent.setEntityId(entityId);
		workflowEvent.setEntityInstanceId(entityInstanceId);
		EntityMaster entityMaster = new EntityMaster();
		entityMaster.setEntitymasterid(entityTypeId);
		workflowEvent.setEntityType(entityMaster);
		WorkflowStatus workflowTargetStatus = new WorkflowStatus();
		workflowTargetStatus.setWorkflowStatusId(statusId);
		workflowEvent.setTargetStatus(workflowTargetStatus);
		workflowEvent.setModifiedBy(user);
		workflowEvent.setLastUpdatedDate(new Date());
		workflowEvent.setPlannedStartDate(new Date());
		workflowEvent.setPlannedEndDate(new Date());
		workflowEvent.setActualStartDate(new Date());
		workflowEvent.setActualEndDate(new Date());
		workflowEvent.setPlannedEffort(0);
		workflowEvent.setActualEffort(0);
		workflowEvent.setEntityGroupId(1);
		workflowEvent.setSlaDurationPlanned(0);
		workflowEvent.setSlaDurationActual(0);
		workflowEvent.setComments(comments);
		ProductMaster product= new ProductMaster();
		product.setProductId(productId);
		workflowEvent.setProduct(product);
		workflowEventDAO.addWorkflowEvent(workflowEvent);
		}catch(Exception e) {
			log.error("Error in setInitialInstanceEvent ", e);
		}
	}

	@Override
	@Transactional
	public List<WorkflowEvent> listAllWorkflowEvents(Integer startIndex, Integer numberOfRecords, Date startDate, Date endDate) {
		List<WorkflowEvent> workflowEvents = null;
		try{
			workflowEvents = workflowEventDAO.listAllWorkflowEvents(startIndex, numberOfRecords, startDate, endDate);
			for(WorkflowEvent workflowEvent : workflowEvents){
				Object instanceObject = null;
				if(workflowEvent != null){
					Integer instanceId = workflowEvent.getEntityInstanceId();
					if(workflowEvent.getEntityType().getEntitymasterid() == IDPAConstants.ENTITY_TEST_CASE_ID){
						instanceObject = testCaseListDAO.getByTestCaseId(instanceId);
					}else if(workflowEvent.getEntityType().getEntitymasterid() == IDPAConstants.ENTITY_TASK_TYPE){
						instanceObject = activityTaskDAO.getActivityTaskById(instanceId, 0);
					}else if(workflowEvent.getEntityType().getEntitymasterid() == IDPAConstants.ENTITY_ACTIVITY_TYPE){
						instanceObject = activityDAO.getActivityById(instanceId, 0);
					}else if(workflowEvent.getEntityType().getEntitymasterid() == IDPAConstants.ENTITY_ACTIVITY_WORKPACKAGE_ID){
						instanceObject = activityWorkPackageDAO.getActivityWorkPackageById(instanceId, 0);
					}
					workflowEvent.setInstanceObject(instanceObject);
				}
			}
		}catch(Exception ex){
			log.error("Error in listAllWorkflowEvents - ", ex);
		}
		return workflowEvents;
	}

	@Override
	@Transactional
	public Integer countAllWorkflowEvents(Date startDate, Date endDate) {
		Integer countOfWorkflowEvents = 0;
		try{
			countOfWorkflowEvents = workflowEventDAO.countAllWorkflowEvents(startDate, endDate);
		}catch(Exception ex){
			log.error("Error in countAllWorkflowEvents - ", ex);
		}
		return countOfWorkflowEvents;
	}

	@Override
	@Transactional
	public List<Object[]> getCurrentStatusActionView(Integer productId,Integer entityTypeId, Integer entityId) {
		return workflowEventDAO.getCurrentStatusActionView(productId,entityTypeId, entityId);
	}

	@Override
	@Transactional
	public WorkflowEvent getLastEventOfEntityOrInstanceForStatus(Integer entityTypeId, Integer entityId, Integer entityInstanceId, Integer statusId) {
		return workflowEventDAO.getLastEventOfEntityOrInstanceForStatus(entityTypeId, entityId, entityInstanceId, statusId);
	}

	@Override
	@Transactional
	public List<WorkflowEvent> getEventsOfEntityOrInstanceForStatus(Integer entityTypeId, Integer entityId, Integer entityInstanceId, Integer statusId) {
		return workflowEventDAO.getEventsOfEntityOrInstanceForStatus(entityTypeId, entityId, entityInstanceId, statusId);
	}

	@Override
	@Transactional
	public Integer getTotalEffortsOfEntityOrInstanceIdForStatus(Integer productId, Integer entityTypeId, Integer entityId, Integer entityInstanceId, Integer statusId) {
		return workflowEventDAO.getTotalEffortsOfEntityOrInstanceIdForStatus(productId, entityTypeId, entityId, entityInstanceId, statusId);
	}

	@Override
	@Transactional
	public boolean isWorkflowStatusInEventAction(Integer workflowStatusId) {
		return workflowEventDAO.isWorkflowStatusInEventAction(workflowStatusId);
	}

	@Override
	@Transactional
	public WorkflowEvent getLastEventInstanceByEntityInstanceId(Integer entityTypeId,Integer entityInstanceId) {
		try {
			return workflowEventDAO.getLastEventInstanceByEntityInstanceId(entityTypeId,entityInstanceId);
		}catch(Exception e) {
			log.error("getLastEventInstanceByEntityInstanceId ",e);
		}
		return null;
	}

	
	@Override
	@Transactional
	public String updateWorkflowEvent(Integer productId, String approveAllEntityInstanceIds, UserList user, Integer entityId, Integer entityTypeId, Integer targetStatusId,Integer secondaryStatusId, Integer effort, String comments, Integer sourceStatusId, Integer entityInstanceId, String attachmentIds, List<Integer> possibleIDs, Boolean isAutoApproveStatusChangeAllowed, Date actionDate,Integer actualSize,Date workflowPlannedStartDate,Date workflowPlannedEndDate,String newEffortAction) {
		Integer entityWiseEffort=0;
		WorkflowEvent referBackWorkflowEvent=null;
		boolean referback = false;
		boolean isSameStatusStage = true;
		Integer initialTargetStatus =null;
		if((sourceStatusId != null && sourceStatusId >0 ) && (targetStatusId == null || targetStatusId ==0)) {
			targetStatusId = sourceStatusId;
		} else {
			initialTargetStatus = targetStatusId;
		}
		ActivityWorkPackage activityWorkPackage = null;
		Activity activity = null;
		ActivityTask activityTask = null;
		ProductFeature feature=null;
		try {
			String builkTaskIds[]=approveAllEntityInstanceIds.split(",");
			if(builkTaskIds.length >0){
				for(String approvalId:builkTaskIds){
					entityInstanceId = Integer.parseInt(approvalId);
					entityWiseEffort = 0;
					
					if(effort != null) {
						entityWiseEffort = effort/builkTaskIds.length;
					}
					
					boolean isStatusAlreadyChanged = checkStatusOfEntityInstance(entityTypeId, entityInstanceId, sourceStatusId, possibleIDs);
					if(isStatusAlreadyChanged && sourceStatusId != null){
						return "Instance status already changed, Please refresh and see latest updates !";
					}
					
					if(targetStatusId > 0){
						isSameStatusStage = false;
						updateUserActionStatus(productId, sourceStatusId, targetStatusId, entityTypeId, entityId, entityInstanceId, user.getUserId());
						boolean isStatusChangeAllowed = checkMandatoryUserActionsToChangeStatus(productId, sourceStatusId, entityTypeId, entityId, entityInstanceId);
						if(isAutoApproveStatusChangeAllowed == null){
							isAutoApproveStatusChangeAllowed = isStatusChangeAllowed;
						}
						
						if(isAutoApproveStatusChangeAllowed == null || !isAutoApproveStatusChangeAllowed){
							comments += " -- Waiting for other mandatory user(s) to change";
						}else{
							comments += " -- Action completed";
						}
					}
					
					WorkflowEvent workflowEvents = new WorkflowEvent();
					workflowEvents.setEntityId(entityId);
					workflowEvents.setEntityInstanceId(entityInstanceId);
					EntityMaster entityMaster = new EntityMaster();
					entityMaster.setEntitymasterid(entityTypeId);
					workflowEvents.setEntityType(entityMaster);
					
					workflowEvents.setActualSize(actualSize);
					
					if(workflowPlannedStartDate != null) {
						workflowEvents.setPlannedStartDate(workflowPlannedStartDate);
					}
					
					if(workflowPlannedEndDate != null) {
						workflowEvents.setPlannedEndDate(workflowPlannedEndDate);
					}
					
					WorkflowStatus workflowCurrentStatus = workflowStatusDAO.getWorkflowStatusById(sourceStatusId);
					workflowEvents.setCurrentStatus(workflowCurrentStatus);
					if(targetStatusId > 0) {
						WorkflowStatus workflowTargetStatus = workflowStatusDAO.getWorkflowStatusById(targetStatusId);
						workflowEvents.setTargetStatus(workflowTargetStatus);
						
						Integer slaDurationPlanned = 0;
						WorkflowStatusPolicy workflowStatusPolicy = workflowStatusPolicyDAO.getSLADurationForEntityOrInstance(entityTypeId, entityId, entityInstanceId, sourceStatusId);
						if(workflowStatusPolicy != null){
							slaDurationPlanned = workflowStatusPolicy.getSlaDuration();
						}
						Integer slaDurationActual = 0;
						WorkflowEvent workflowEvent = workflowEventDAO.getLastEventOfEntityOrInstanceForStatus(entityTypeId, entityId, entityInstanceId, sourceStatusId);
						if(workflowEvent != null && workflowEvent.getLastUpdatedDate() != null){
							long hoursSpent = new Date().getTime() - workflowEvent.getLastUpdatedDate().getTime() ;
							if(hoursSpent < 0){
								hoursSpent = (-1 * hoursSpent);
							}
							slaDurationActual = (int) (hoursSpent / (1000*60*60));
						}
						workflowEvents.setSlaDurationPlanned(slaDurationPlanned);
						workflowEvents.setSlaDurationActual(slaDurationActual);
						
					}else {
						workflowEvents.setTargetStatus(null);
					}
					
					if(referback){
						referBackWorkflowEvent=getLatestWorkflowEventAction(entityId, entityTypeId);
						if(referBackWorkflowEvent != null) {
							WorkflowStatus currentWorkflowStatus = new WorkflowStatus();
							currentWorkflowStatus.setWorkflowStatusId(targetStatusId);
							referBackWorkflowEvent.setCurrentStatus(currentWorkflowStatus);
						}
					}	
					
					workflowEvents.setModifiedBy(user);
					workflowEvents.setLastUpdatedDate(new Date());
					workflowEvents.setPlannedStartDate(new Date());
					workflowEvents.setPlannedEndDate(new Date());
					workflowEvents.setActualStartDate(new Date());
					workflowEvents.setActualEndDate(new Date());
					workflowEvents.setPlannedEffort(0);
					workflowEvents.setActualEffort(entityWiseEffort);
					workflowEvents.setEntityGroupId(1);
					workflowEvents.setComments(comments);
					workflowEvents.setLastUpdatedDate(actionDate);
					ProductMaster product= new ProductMaster();
					product.setProductId(productId);
					workflowEvents.setProduct(product);
					if(newEffortAction != null && !newEffortAction.trim().isEmpty()) {
						
						if(newEffortAction.equalsIgnoreCase("replace")) {
							WorkflowEvent workflowEvent= workflowEventDAO.getLastEventInstanceByEntityInstanceId(entityTypeId, entityInstanceId);
							if(workflowEvent != null){
								workflowEvent.setActualEffort(workflowEvents.getActualEffort());
								workflowEvent.setLastUpdatedDate(new Date());
								workflowEventDAO.updateWorkflowEvent(workflowEvent);
							} else {
								workflowEventDAO.addWorkflowEvent(workflowEvents);
							}
						} 
					} else {
						workflowEventDAO.addWorkflowEvent(workflowEvents);
					}
					
					if(workflowEvents != null && workflowEvents.getWorkflowEventId() != null){
						eventsService.addNewEntityEvent(IDPAConstants.ENTITY_WORKFLOW_EVENTS, workflowEvents.getWorkflowEventId(), workflowEvents.getCurrentStatus().getWorkflowStatusName(), user);
					}
					if(workflowEvents.getWorkflowEventId() != null && attachmentIds != null && !attachmentIds.trim().isEmpty() && !"0".equalsIgnoreCase(attachmentIds)){
						updateAttachmentForWorkflowEvent(workflowEvents.getWorkflowEventId(), attachmentIds);
					}
					
					Object instanceObject = null;
					if(workflowEvents.getEntityType().getEntitymasterid() == IDPAConstants.ENTITY_TEST_CASE_ID){
						instanceObject = testCaseUpdateBasedOnTracker(workflowEvents, isAutoApproveStatusChangeAllowed, productId);
					}else if(workflowEvents.getEntityType().getEntitymasterid() == IDPAConstants.ENTITY_TASK_TYPE){
						instanceObject = activityTaskUpdateBasedOnTracker(workflowEvents, isAutoApproveStatusChangeAllowed, productId);
					}else if(workflowEvents.getEntityType().getEntitymasterid() == IDPAConstants.ENTITY_ACTIVITY_TYPE){
						instanceObject = activityUpdateBasedOnTracker(workflowEvents, isAutoApproveStatusChangeAllowed, productId);
						activity = activityService.getActivityById(entityInstanceId, 1);
						activity.setWorkflowStatus(workflowEvents.getTargetStatus());
						activityService.updateActivity(activity);
					}else if(workflowEvents.getEntityType().getEntitymasterid() == IDPAConstants.ENTITY_ACTIVITY_WORKPACKAGE_ID){
						instanceObject = activityWorkpackageUpdateBasedOnTracker(workflowEvents, isAutoApproveStatusChangeAllowed, productId);
					}else if(workflowEvents.getEntityType().getEntitymasterid() == IDPAConstants.PRODUCT_FEATURE_ENTITY_MASTER_ID){
						instanceObject = productFeatureUpdateBasedOnTracker(workflowEvents, isAutoApproveStatusChangeAllowed, productId);
					}
					
					if(isAutoApproveStatusChangeAllowed != null && isAutoApproveStatusChangeAllowed){
						updateInstanceStatusPolicyActualValues(productId, sourceStatusId, targetStatusId, entityTypeId, entityId, entityInstanceId);
						checkAndUpdateStatusOfInstanceParentOrChild(productId, entityTypeId, entityInstanceId, instanceObject);
						
						List<Object[]> pendingActorsDetails = workflowStatusPolicyDAO.getPendingWithActors(productId, entityTypeId, entityId, entityInstanceId, targetStatusId);						
						String emailIds = "";
						String[] emails = null;
						String entityTypeName = "";
						String instanceNames = "";
						String firstNames = "";						
						String pendingWith = "";	
						int slaDuration = 0;
						String movedFrom = "--";
						String movedTo = "--";
						String[] ccMailIds = null;
						
						if(entityTypeId.equals(IDPAConstants.ENTITY_ACTIVITY_WORKPACKAGE_ID)){
							activityWorkPackage = activityWorkPackageService.getActivityWorkPackageById(entityInstanceId, 1);
							ccMailIds = new String[]{activityWorkPackage.getOwner().getEmailId()};
						}else if(entityTypeId.equals(IDPAConstants.ENTITY_ACTIVITY_TYPE)){
						/*	activity = activityService.getActivityById(entityInstanceId, 1);
							activity.setWorkflowStatus(workflowEvents.getTargetStatus());
							activityService.updateActivity(activity);*/
							ccMailIds = new String[]{activity.getAssignee().getEmailId()};
						}else if (entityTypeId.equals(IDPAConstants.ENTITY_TASK_TYPE)) {
							activityTask = activityTaskService.getActivityTaskById(entityInstanceId, 1);
							ccMailIds = new String[]{activityTask.getAssignee().getEmailId()};
						}else if (entityTypeId.equals(IDPAConstants.PRODUCT_FEATURE_ENTITY_MASTER_ID)) {
							feature = productListService.getByProductFeatureId(entityInstanceId);
							ccMailIds = new String[]{feature.getAssignee().getEmailId()};
						}else{
							ccMailIds = IDPAConstants.defaultMailId;
						}
						
						if(workflowEvents.getCurrentStatus() != null){
							movedFrom = workflowEvents.getCurrentStatus().getWorkflowStatusName();
						}
						if(workflowEvents.getTargetStatus() != null){
							movedTo = workflowEvents.getTargetStatus().getWorkflowStatusName();
						}
						if(workflowEvents.getSlaDurationPlanned() != null){
							slaDuration = workflowEvents.getSlaDurationPlanned();
						}
						
							if(!pendingActorsDetails.isEmpty() && pendingActorsDetails.size() != 0){							
							 for(Object[] pendingActorObjArr: pendingActorsDetails){								
								if(pendingActorObjArr != null && pendingActorObjArr.length > 0){
									UserList userList = userListService.getUserListById((Integer)pendingActorObjArr[0]);									
									if(userList != null){
									emailIds += userList.getEmailId()+",";
									firstNames += userList.getFirstName()+"/"; 
									pendingWith += userList.getLoginId()+",";																	
									}									
									if(entityTypeName.equals("") && pendingActorObjArr[1] != null){										
										entityTypeName = (String)pendingActorObjArr[1];
									}
									if(instanceNames.equals("") && pendingActorObjArr[2] != null){
										instanceNames = (String)pendingActorObjArr[2];
									}									
								 }
							  }
							if(emailIds.endsWith(",")){
								emailIds = emailIds.substring(0, emailIds.length()-1);
								emails = emailIds.split(",");
							}							
							if(firstNames.endsWith("/")){
								firstNames = firstNames.substring(0, firstNames.length()-1);
							}
							if(pendingWith.endsWith(",")){
								pendingWith = pendingWith.substring(0, pendingWith.length()-1);								
							}else{
								pendingWith = "--";
							 }
							
							if(emailIds.equals("") || emailIds.isEmpty()){
								emails = IDPAConstants.defaultMailId;
							}
							if(firstNames.equals("") || firstNames.isEmpty()){
								firstNames = IDPAConstants.defaultName;
							}
							WorkflowEvent workflowEvent = getLastEventOfEntityOrInstanceForStatus(entityTypeId, entityId, entityInstanceId, targetStatusId);
							if(emailNotification.equalsIgnoreCase("YES")){
								emailService.sendEmailWhenWorkflowStatusChange(emails, ccMailIds, firstNames, entityTypeName, instanceNames, workflowEvent.getModifiedBy().getFirstName(),workflowEvent.getComments(),workflowEvent.getLastUpdatedDate(), movedFrom, movedTo, pendingWith, slaDuration,entityInstanceId);
							}
					   }
					}
					
					if(!isSameStatusStage){
						if(possibleIDs != null && !possibleIDs.contains(sourceStatusId)){
							possibleIDs.add(sourceStatusId);
						}
						updateForAutoApproveStatus(productId, initialTargetStatus, entityTypeId, entityId, entityInstanceId, user, possibleIDs, isAutoApproveStatusChangeAllowed);
					}
					
					addWorkflowEventToMongoDB(workflowEvents);
				}
			}
		}catch(Exception e) {
			log.error("Error in Workflow Events", e);
		}
		return "";
	}

}
