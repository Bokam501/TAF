package com.hcl.atf.taf.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hcl.atf.taf.constants.IDPAConstants;
import com.hcl.atf.taf.controller.utilities.DateUtility;
import com.hcl.atf.taf.dao.ActivityDAO;
import com.hcl.atf.taf.dao.ActivityTaskDAO;
import com.hcl.atf.taf.dao.ActivityTypeDAO;
import com.hcl.atf.taf.dao.ActivityWorkPackageDAO;
import com.hcl.atf.taf.dao.CommentsTrackerDAO;
import com.hcl.atf.taf.dao.ExecutionTypeMasterDAO;
import com.hcl.atf.taf.dao.ProductUserRoleDAO;
import com.hcl.atf.taf.dao.UserListDAO;
import com.hcl.atf.taf.model.Activity;
import com.hcl.atf.taf.model.ActivityEntityMaster;
import com.hcl.atf.taf.model.ActivityTask;
import com.hcl.atf.taf.model.ActivityWorkPackage;
import com.hcl.atf.taf.model.CommentsTracker;
import com.hcl.atf.taf.model.CommentsTypeMaster;
import com.hcl.atf.taf.model.ExecutionPriority;
import com.hcl.atf.taf.model.ExecutionTypeMaster;
import com.hcl.atf.taf.model.UserList;
import com.hcl.atf.taf.model.dto.ActivityWorkPackageSummaryDTO;
import com.hcl.atf.taf.model.dto.ProductAWPSummaryDTO;
import com.hcl.atf.taf.model.json.JsonActivityWorkPackage;
import com.hcl.atf.taf.mongodb.service.MongoDBService;
import com.hcl.atf.taf.service.ActivityEntityMasterService;
import com.hcl.atf.taf.service.ActivityWorkPackageService;
import com.hcl.atf.taf.service.CommentsTypeMasterService;
import com.hcl.ilcm.workflow.service.WorkflowStatusPolicyService;

@Service
public class ActivityWorkPackageServiceImpl implements ActivityWorkPackageService{
	private static final Log log = LogFactory.getLog(ActivityWorkPackageServiceImpl.class);
	@Autowired
	ActivityWorkPackageDAO workRequestDAO;
	@Autowired
	ActivityTypeDAO activityTypeDAO;
	@Autowired
	ActivityDAO activityDAO;
	@Autowired
	private ExecutionTypeMasterDAO executionTypeMasterDAO;
	@Autowired
	private UserListDAO userListDAO;
	@Autowired
	private ActivityTaskDAO activityTaskDAO;
	@Autowired
	private CommentsTypeMasterService commentsTypeMasterService;
	@Autowired
	private CommentsTrackerDAO commentsTrackerDAO;
	@Autowired
	private ActivityEntityMasterService activityEntityMasterService;
	@Autowired
	private MongoDBService mongoDBService;	

	@Autowired
	private ProductUserRoleDAO productUserRoleDAO;
	
	@Autowired
	private WorkflowStatusPolicyService workflowStatusPolicyService;	
	
	@SuppressWarnings("unchecked")
	@Override
	@Transactional
	public List<JsonActivityWorkPackage> listActivityWorkPackagessByBuildId(Integer productId,Integer productVersionId,Integer productBuildId,int isActive, Map<String, String> searchStrings,UserList user) {
		List<ActivityWorkPackage> workRequestList = null;
		List<JsonActivityWorkPackage> jsonWorkRequest = new ArrayList<JsonActivityWorkPackage>();
		try {
			Integer testFactoryId=0;
			workRequestList = workRequestDAO.listActivityWorkPackagesByProductId(productId,productVersionId,productBuildId,isActive, user,searchStrings);
			if(workRequestList!=null && workRequestList.size()>0){				
				for(ActivityWorkPackage wr: workRequestList){
					JsonActivityWorkPackage jsonActivityWorkPackage = new JsonActivityWorkPackage(wr);
					jsonWorkRequest.add(jsonActivityWorkPackage);	
				}
				workflowStatusPolicyService.setInstanceIndicators(testFactoryId,productId,null, IDPAConstants.ENTITY_ACTIVITY_WORKPACKAGE_ID, jsonWorkRequest, IDPAConstants.ENTITY_ACTIVITY_WORKPACKAGE_ID,user, null, null, null,IDPAConstants.ENABLE_PENDING_WITH_COLUMN);
			}
			
			Collections.sort(jsonWorkRequest, new Comparator<JsonActivityWorkPackage>() {
				@Override
				public int compare(JsonActivityWorkPackage activityWp, JsonActivityWorkPackage activityWp2) {
					if(activityWp.getRemainingHours() != null && activityWp2.getRemainingHours() != null){
						return activityWp.getRemainingHours().compareTo(activityWp2.getRemainingHours());
					}else {
						return 0;
					}
				}
			});			
			jsonWorkRequest = (List<JsonActivityWorkPackage>) workflowStatusPolicyService.getPaginationListFromFullList(jsonWorkRequest, null, null);
			
		} catch (Exception e) {
			log.error("ERROR  ",e);
		}
		return jsonWorkRequest;
	}


	@Override
	@Transactional
	public List<JsonActivityWorkPackage> listActivityWorkPackagesByTestFactoryId(Integer testFactoryId, Integer productId, Integer isActive, 
			Map<String, String> searchStrings, UserList user,Integer jtStartIndex, Integer jtPageSize){
		List<JsonActivityWorkPackage> jsonActivityWPList = new ArrayList<JsonActivityWorkPackage>();
		try {
		
			
			if(productId != -1){
				jsonActivityWPList = workRequestDAO.listJsonActivityWorkPackagesByTestFactoryId(testFactoryId, productId, isActive, searchStrings, user,jtStartIndex, jtPageSize);
				if(jsonActivityWPList!=null && jsonActivityWPList.size()>0){
					workflowStatusPolicyService.setInstanceIndicators(testFactoryId,productId,null, IDPAConstants.ENTITY_ACTIVITY_WORKPACKAGE_ID, jsonActivityWPList, IDPAConstants.ENTITY_ACTIVITY_WORKPACKAGE_ID,user, null, null, null,IDPAConstants.ENABLE_PENDING_WITH_COLUMN);	
				}
			}else if(testFactoryId != -1){
				jsonActivityWPList = workRequestDAO.listJsonActivityWorkPackagesByTestFactoryId(testFactoryId, productId, isActive, searchStrings,user, jtStartIndex, jtPageSize);
				if(jsonActivityWPList!=null && jsonActivityWPList.size()>0){
					workflowStatusPolicyService.setInstanceIndicators(testFactoryId, productId,null, IDPAConstants.ENTITY_ACTIVITY_WORKPACKAGE_ID, jsonActivityWPList, IDPAConstants.ENTITY_ACTIVITY_WORKPACKAGE_ID, user, null, null, null,IDPAConstants.ENABLE_PENDING_WITH_COLUMN);
				}				
			}
			Collections.sort(jsonActivityWPList, new Comparator<JsonActivityWorkPackage>() {
				@Override
				public int compare(JsonActivityWorkPackage activityWp, JsonActivityWorkPackage activityWp2) {
					if(activityWp.getRemainingHours() != null && activityWp2.getRemainingHours() != null){
						return activityWp.getRemainingHours().compareTo(activityWp2.getRemainingHours());
					}else {
						return 0;
					}
				}
			});
		} catch (Exception e) {
			log.error("ERROR  ",e);
		}	
		return 	jsonActivityWPList;
	}	
	

	@Override
	@Transactional
	public Integer getActivityWPCountByTestFactoryIdProductId(int testFactoryId, Integer productId) {
		log.debug("listing getActivityWorkPackagesCountByTestFactoryIdProductId instance");		
		return workRequestDAO.getActivityWPCountByTestFactoryIdProductId(testFactoryId, productId);
	}
	@Override
	@Transactional
	public List<ActivityWorkPackage> listActivityWorkPackages() {
		List<ActivityWorkPackage> workRequestList = null;
		List<JsonActivityWorkPackage> jsonWorkRequest = new ArrayList<JsonActivityWorkPackage>();
		try {
			workRequestList = workRequestDAO.listActivityWorkPackages();
			for(ActivityWorkPackage wr: workRequestList){
				jsonWorkRequest.add(new JsonActivityWorkPackage(wr));	
			}
		} catch (Exception e) {
			log.error("ERROR  ",e);
		}
		return workRequestList;
	}

	@Override
	@Transactional
	public void addActivityWorkPackage(ActivityWorkPackage activityWorkPackage) {
		workRequestDAO.addActivityWorkPackage(activityWorkPackage);
		
	}

	@Override
	@Transactional
	public void updateActivityWorkPackage(ActivityWorkPackage activityWorkPackage) {
		workRequestDAO.updateActivityWorkPackage(activityWorkPackage);
				
	}

	@Override
	@Transactional
	public ActivityWorkPackage getActivityWorkPackageById(Integer activityWorkPackageId,Integer initializationLevel) {
		return workRequestDAO.getActivityWorkPackageById(activityWorkPackageId,initializationLevel);
	}

	@Override
	@Transactional	
	public void updateActivity(String[] activityBulkLists,
			Integer categoryId, Integer assigneeId, String plannedEndDate,
			Integer priorityId, Integer reviewerId) {
		Activity activityBulkData=null;
		ExecutionPriority executionPriority =null;

		try
		{
	    	for(String Id : activityBulkLists){
	    	    activityBulkData=activityDAO.getActivityById(Integer.parseInt(Id),1);
	    		if(categoryId!=null && categoryId !=-1){
	    			ExecutionTypeMaster category = executionTypeMasterDAO.getExecutionTypeByExecutionTypeId(categoryId);
	    			activityBulkData.setCategory(category);
	    		}
	    		if(assigneeId!=null && assigneeId!=-1){
	    			UserList assignee =userListDAO.getByUserId(assigneeId);
	    			activityBulkData.setAssignee(assignee);
	    		}
	    		if(reviewerId!=null && reviewerId!=-1){
	    			UserList reviewer =userListDAO.getByUserId(reviewerId);
	    			activityBulkData.setReviewer(reviewer);
	    		}
	    		if(plannedEndDate!=null && !plannedEndDate.equals("")){
	    			activityBulkData.setPlannedEndDate(DateUtility.dateformatWithOutTime(plannedEndDate));
	    		}
	    		if(plannedEndDate!=null && !plannedEndDate.equals("")){
	    			activityBulkData.setPlannedEndDate(DateUtility.dateformatWithOutTime(plannedEndDate));
	    		}
	    		if(priorityId!=-1){
	    			executionPriority = activityDAO.getExecutionPriorityById(priorityId);
	    			activityBulkData.setPriority(executionPriority);
	    		}
	    		activityBulkData.setModifiedDate(new Date(System.currentTimeMillis()));
	    		activityDAO.updateActivity(activityBulkData);
	    		if(activityBulkData!=null && activityBulkData.getActivityId()!=null){
	    			mongoDBService.addActivitytoMongoDB(activityBulkData.getActivityId());
	    		}
	    	}
	    	
		}catch (Exception e) {
			return;
		}
	}

	@Override
	@Transactional
	public ActivityWorkPackage getActivityWorkPackageByName(
			String activityWpName, Integer productBuildId) {
		return workRequestDAO.getActivityWorkPackageByName(activityWpName, productBuildId);
	}

	@Override
	public void updateActivityTaskBulkComments(String resultComments,
			String resultEntityValue, String[] activityTaskBulkLists,
			String raisedByValue, String commentsTypeValue,String resultRaisedDateValue
			) {

		ActivityTask activityTaskBulkData=null;		
		
		try
		{
	    	for(String Id : activityTaskBulkLists){
	    		log.info("activityTask  status"+Id);
	    		String[] activityIdvalue = Id.split(":");
	    		activityTaskBulkData=activityTaskDAO.getActivityTaskById(Integer.parseInt(activityIdvalue[0]),1);
	    	
	    		CommentsTracker commentsTracker = new CommentsTracker();
		    	commentsTracker.setComment(resultComments);	    
		    	commentsTracker.setEntityId(activityTaskBulkData);   
		    	commentsTracker.setLastUpdatedDate(new Date());
		        CommentsTypeMaster commentsTypeMaster = commentsTypeMasterService.getCommentsTypeMasterByName(commentsTypeValue);
				commentsTracker.setCommentType(commentsTypeMaster);
		        commentsTracker.setRaisedBy(userListDAO.getByUserName(raisedByValue));
		    	ActivityEntityMaster activityEntityMaster = activityEntityMasterService.getActivityEntityMasterByName(resultEntityValue);
		    	commentsTracker.setEntityType(activityEntityMaster);
				commentsTracker.setRaisedDate(DateUtility.dateformatWithOutTime(resultRaisedDateValue));
				commentsTracker.setComment(resultComments);	
			    commentsTrackerDAO.addCommentsTracker(commentsTracker);				 
	            activityTaskBulkData.setModifiedDate(new Date(System.currentTimeMillis()));
	    		activityTaskDAO.updateActivityTask(activityTaskBulkData);
	    		
	    		if(activityTaskBulkData!=null && activityTaskBulkData.getActivityTaskId()!=null){
	    			mongoDBService.addActivitytoMongoDB(activityTaskBulkData.getActivityTaskId());
	    		}
	    	}
	    	
	 
	    	
		}catch (Exception e) {
			return;
		}
	
		
	}
	
	@Override
	@Transactional
	public String deleteActivityWorkPackage(int activityWorkPackageId, String referencedTableName, String referencedColumnName) {
		return workRequestDAO.deleteActivityWorkPackage(activityWorkPackageId, referencedTableName, referencedColumnName);
	}

	@Override
	@Transactional
	public Integer listActivityWorkPackagesByProductIdCount(Integer productId, Integer productVersionId, Integer productBuildId, Integer isActive, Map<String, String> searchString) {
		return workRequestDAO.listActivityWorkPackagesByProductIdCount(productId, productVersionId, productBuildId, isActive, searchString);
	}

	@Override
	@Transactional
	public List<ActivityWorkPackage> getActivityWorkPackageByBuildId(Integer buildId) {
		return workRequestDAO.getActivityWorkPackageByBuildId(buildId);
	}

	@Override
	@Transactional
	public ActivityWorkPackageSummaryDTO listActivityWorkPackageSummaryDetails(Integer activityWorkPackageId) {		
		return workRequestDAO.listActivityWorkPackageSummaryDetails(activityWorkPackageId);
	}	

	@Override
	@Transactional
	public ProductAWPSummaryDTO listProductAWPSummaryDetails(Integer productId) {		
		return workRequestDAO.listProductAWPSummaryDetails(productId);
	}	

	@Override
	@Transactional
	public List<Integer> getActivityWorkPackagesofProductIDS(int productId, List<Integer> productIdList, boolean paramIsList){				
		return workRequestDAO.getActivityWorkPackagesofProductIDS(productId, productIdList, paramIsList);
	}
	
	@Override
	@Transactional
	public ActivityWorkPackage getLatestActivityWPByProductId(Integer productId){
		return workRequestDAO.getLatestActivityWPByProductId(productId);
	}


	@Override
	@Transactional
	public ActivityWorkPackage getLastestActivityWorkPackageByNameInProduct(String workpackageName, Integer productId) {
		return workRequestDAO.getLastestActivityWorkPackageByNameInProduct(workpackageName, productId);
	}


	@Override
	@Transactional
	public List<ActivityWorkPackage> getActivityWorkPackageListByProductId(Integer productId) {
		return workRequestDAO.getActivityWorkPackageListByProductId(productId);
	}
	
	@Override
	@Transactional
	public List<ActivityWorkPackage> getActivityWorkPackagesByName(String activityWpName) {
		return workRequestDAO.getActivityWorkPackagesByName(activityWpName);
	}


	@Override
	public List<Object[]> getActivityWorkpackageByTestFactoryIdProductId(
			Integer testFactoryId, Integer productId) {
		try{
			return workRequestDAO.getActivityWorkpackageByTestFactoryIdProductId(testFactoryId, productId);
		}catch(Exception e) {
			log.error("Error in getActivityWorkpackageByTestFactoryIdProductId",e);
		}
		return null;
	}
}
