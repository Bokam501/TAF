package com.hcl.atf.taf.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hcl.atf.taf.constants.IDPAConstants;
import com.hcl.atf.taf.controller.utilities.DateUtility;
import com.hcl.atf.taf.dao.ActivityEffortTrackerDAO;
import com.hcl.atf.taf.dao.ActivitySecondaryStatusMasterDAO;
import com.hcl.atf.taf.dao.ActivityStatusDAO;
import com.hcl.atf.taf.dao.ActivityTaskDAO;
import com.hcl.atf.taf.dao.UserListDAO;
import com.hcl.atf.taf.model.ActivityEffortTracker;
import com.hcl.atf.taf.model.ActivityEntityMaster;
import com.hcl.atf.taf.model.ActivitySecondaryStatusMaster;
import com.hcl.atf.taf.model.ActivityStatus;
import com.hcl.atf.taf.model.ActivityTask;
import com.hcl.atf.taf.model.ActivityTaskType;
import com.hcl.atf.taf.model.UserList;
import com.hcl.atf.taf.model.json.JsonActivityTask;
import com.hcl.atf.taf.mongodb.service.MongoDBService;
import com.hcl.atf.taf.service.ActivityEffortTrackerService;
import com.hcl.atf.taf.service.ActivityEntityMasterService;
import com.hcl.atf.taf.service.ActivityTaskService;
import com.hcl.ilcm.workflow.dao.WorkflowEventDAO;
import com.hcl.ilcm.workflow.service.WorkflowEventService;
import com.hcl.ilcm.workflow.service.WorkflowStatusPolicyService;
@Service
public class ActivityTaskServiceImpl implements ActivityTaskService {
	private static final Log log = LogFactory.getLog(ActivityWorkPackageServiceImpl.class);
	@Autowired
	ActivityTaskDAO activityTaskDAO;
	@Autowired
	ActivityStatusDAO activityStatusDAO;
	@Autowired
	UserListDAO userListDAO;
	@Autowired
	ActivityEntityMasterService activityEntityMasterService;
	@Autowired
	ActivityEffortTrackerDAO activityEffortTrackerDAO; 
	@Autowired
	ActivitySecondaryStatusMasterDAO activitySecondaryStatusMasterDAO; 
	
	@Autowired
	ActivityEffortTrackerService activityEffortTrackerService;

	@Autowired
	private MongoDBService mongoDBService;
	
	@Autowired
	private WorkflowStatusPolicyService workflowStatusPolicyService;
	
	@Autowired
	private WorkflowEventDAO workflowEventDAO;
	
	@Autowired
	private WorkflowEventService workflowEventService;
	
	@Value("#{ilcmProps['TEST_STEP_BATCH_PROCESSING_COUNT']}")
    private String activitiesMaxBatchCount;
	
	@Override
	@Transactional
	public List<ActivityTask> activityTaskLists() {
		List<ActivityTask> activityTaskList = null;
		List<JsonActivityTask> jsonactivityTask = new ArrayList<JsonActivityTask>();
		try {
			activityTaskList = activityTaskDAO.activityTasklists();
			for(ActivityTask at: activityTaskList){
				jsonactivityTask.add(new JsonActivityTask(at));	
			}
		} catch (Exception e) {
			log.error("ERROR  ",e);
		}
		return activityTaskList;
	}

	@Override
	@Transactional
	public void addActivityTask(ActivityTask addActivityTask) {
		activityTaskDAO.addActivityTask(addActivityTask);
		
	}

	@Override
	@Transactional
	public void updateActivityTask(ActivityTask updateActivityTask) {
		activityTaskDAO.updateActivityTask(updateActivityTask);
		
	}

	@Override
	@Transactional
	public String deleteActivityTask(int activityTaskId, String referencedTableName, String referencedColumnName) {
		return activityTaskDAO.deleteActivityTask(activityTaskId, referencedTableName, referencedColumnName);
	}



	@Override
	@Transactional
	public List<JsonActivityTask> listActivityTaskByActivityId(
			Integer activityId, Integer initializationLevel, Integer isActive) {
		
		List<ActivityTask> activityTaskList = null;
		List<JsonActivityTask> jsonActivityTasklist = new ArrayList<JsonActivityTask>();
		try {
			activityTaskList = activityTaskDAO.listActivityTasksByActivityId(activityId, initializationLevel, isActive);
				for(ActivityTask wr: activityTaskList){
					jsonActivityTasklist.add(new JsonActivityTask(wr));	
				}
			}
		catch (Exception e) {
			log.error("ERROR  ",e);
		}
		return jsonActivityTasklist;
	}

	@Override
	@Transactional
	public ActivityTask getActivityTaskById(Integer activityTaskId,
			Integer initializationLevel) {
		return activityTaskDAO.getActivityTaskById(activityTaskId,initializationLevel);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	@Transactional
	public List<JsonActivityTask> listActivityTaskByActivityIdAndUserId(HttpServletRequest req, Integer productId,
			int activityId, UserList user,Integer jtStartIndex, Integer jtPageSize,
			 Map<String,String> searchStrings,Integer initializationLevel) {
		List<ActivityTask> activityTaskList = null;
		List<JsonActivityTask> jsonActivityTaskResultList = new ArrayList<JsonActivityTask>();
		List<JsonActivityTask> jsonActivityTasklist = new ArrayList<JsonActivityTask>();
		try {
			Date userLoginDate = (Date) req.getSession().getAttribute("userLoginTime");
			Date userLogoutDate = (Date) req.getSession().getAttribute("userLogoutTime");
			Integer engagementId=0;
			activityTaskList = activityTaskDAO.listActivityTaskforSelfReview(activityId, user.getUserId(),user.getUserRoleMaster().getUserRoleId(), jtStartIndex,jtPageSize, initializationLevel, searchStrings);
			if(activityTaskList != null && activityTaskList.size()>0){
				for(ActivityTask activityTask : activityTaskList){
					JsonActivityTask jsonActivityTask = new JsonActivityTask(activityTask);
					if(userLogoutDate!= null){
						jsonActivityTask.setUserTagActivity(DateUtility.compareDateTimeRange(userLogoutDate,activityTask.getModifiedDate()));
					} else if(userLoginDate != null) {
						jsonActivityTask.setUserTagActivity(DateUtility.compareDateTimeRange(userLoginDate, activityTask.getModifiedDate()));
					}else {
						jsonActivityTask.setUserTagActivity(DateUtility.compareDateTimeRange(new Date(), activityTask.getModifiedDate()));
					}
					if(jsonActivityTask.getUserTagActivity() <0) {
						jsonActivityTask.setIsModified("Yes");
					} else {
						jsonActivityTask.setIsModified("No");
					}
					
					jsonActivityTasklist.add(jsonActivityTask);	
				}
				workflowStatusPolicyService.setInstanceIndicators(engagementId,productId,null, IDPAConstants.ENTITY_TASK_TYPE, jsonActivityTasklist, IDPAConstants.ENTITY_TASK,user, null, null, null,IDPAConstants.ENABLE_PENDING_WITH_COLUMN);
			}
			
			jsonActivityTaskResultList = (List<JsonActivityTask>) workflowStatusPolicyService.getPaginationListFromFullList(jsonActivityTasklist, jtStartIndex, jtPageSize);
		}catch (Exception e) {
			log.error("ERROR  ",e);
		}
		return jsonActivityTaskResultList;
	}
	
	@Override
	@Transactional
	public List<ActivityTask> getActivityTaskById(Integer activityId){
		List<ActivityTask> activityTasks = null;
		activityTasks = activityTaskDAO.getActivityTaskByActivityId(activityId);
		return activityTasks;
	}

	@SuppressWarnings("unchecked")
	@Override
	@Transactional
	public List<JsonActivityTask> listActivityTasks(HttpServletRequest req,Integer productId,
			Integer productVersionId, Integer productBuildId,
			Integer activityWorkPackageId, Integer activityId,
			Integer initializationLevel, Integer isActive,UserList user,
			Integer jtStartIndex, Integer jtPageSize, Map<String, String> searchStrings) {
		List<ActivityTask> activityTaskList = null;
		List<JsonActivityTask> jsonActivityTasklist = new ArrayList<JsonActivityTask>();
		List<JsonActivityTask> jsonActivityTaskReturnList = new ArrayList<JsonActivityTask>();
		try {
			Date userLoginDate = (Date) req.getSession().getAttribute("userLoginTime");
			Date userLogoutDate = (Date) req.getSession().getAttribute("userLogoutTime");
			Integer engagementId=0;
			activityTaskList = activityTaskDAO.listActivityTasks(productId, productVersionId, productBuildId,activityWorkPackageId,activityId, initializationLevel, isActive,jtStartIndex,jtPageSize, searchStrings);
			for(ActivityTask activityTask: activityTaskList){
					
					Integer activityTaskEffort= activityEffortTrackerService.getTotalEffortsByTaskId(activityTask.getActivityTaskId());
					if(activityTaskEffort != null && activityTaskEffort>0){
						activityTask.setTotalEffort(activityTaskEffort);
					}
					JsonActivityTask jsonActivityTask=new JsonActivityTask(activityTask);
					
					if(userLogoutDate!= null){
						jsonActivityTask.setUserTagActivity(DateUtility.compareDateTimeRange(userLogoutDate,activityTask.getModifiedDate()));
					} else if(userLoginDate != null) {
						jsonActivityTask.setUserTagActivity(DateUtility.compareDateTimeRange(userLoginDate, activityTask.getModifiedDate()));
					}else {
						jsonActivityTask.setUserTagActivity(DateUtility.compareDateTimeRange(new Date(), activityTask.getModifiedDate()));
					}
					if(jsonActivityTask.getUserTagActivity() <0) {
						jsonActivityTask.setIsModified("Yes");
					} else {
						jsonActivityTask.setIsModified("No");
					}
					
					
					
					jsonActivityTasklist.add(jsonActivityTask);	
				}
			
			
			workflowStatusPolicyService.setInstanceIndicators(engagementId,productId,null, IDPAConstants.ENTITY_TASK_TYPE, jsonActivityTasklist, IDPAConstants.ENTITY_TASK,user, null, null, null,IDPAConstants.ENABLE_PENDING_WITH_COLUMN);
			Collections.sort(jsonActivityTasklist, new Comparator<JsonActivityTask>() {
				@Override
				public int compare(JsonActivityTask task, JsonActivityTask task2) {
					if(task.getRemainingHours() != null && task2.getRemainingHours() != null){
						return task.getRemainingHours().compareTo(task2.getRemainingHours());
					}else {
						return 0;
					}
				}
			});
			
			jsonActivityTaskReturnList = (List<JsonActivityTask>) workflowStatusPolicyService.getPaginationListFromFullList(jsonActivityTasklist, jtStartIndex, jtPageSize);
		}catch (Exception e) {
			log.error("Error in listActivityTasks"+e);
		}
		return jsonActivityTaskReturnList;
	}

	@Override
	@Transactional
	public void updateActivityTaskEffortTracker(Integer taskId,
			String resultEntity, String resultPrimaryStatus,
			String resultSecondaryStatus, Integer taskWiseEffort,
			String resultModifiedBy, String sourcestatus, String resultComments,Integer compentancyId) {

		try
		{      
			ActivityTask activityTask=null;		
			ActivityStatus activitySourceStatus = null;
			ActivityStatus activityTargetStatus= null;
			ActivitySecondaryStatusMaster activitySecondaryStatus = null;
			ActivityEntityMaster activityEntityMaster = null;	
			Integer totalTaskEffort=0;
			
			    activityTask=activityTaskDAO.getActivityTaskById(taskId,1);
	    	    ActivityEffortTracker activityEffortTracker = new ActivityEffortTracker();
	    		activityEffortTracker.setEntity(activityTask);    		   
	    		
	    		activityTargetStatus=activityStatusDAO.getStatusNameByDimensionId(resultPrimaryStatus,compentancyId);
	    		
	    	
	    		activityEffortTracker.setTargetStatus(activityTargetStatus);
	    		
	    		activityTask.setTotalEffort(totalTaskEffort);
	    		activitySourceStatus=activityStatusDAO.getStatusNameByDimensionId(sourcestatus,compentancyId);
	    		
	    		activityEffortTracker.setCurrentStatus(activitySourceStatus);	
	    		
	    		activitySecondaryStatus=activitySecondaryStatusMasterDAO.getSecondaryStatusByNameAndDimentionId(resultSecondaryStatus, compentancyId);
	    		activityTask.setSecondaryStatus(activitySecondaryStatus);  
	    		
	    		activityEffortTracker.setLastUpdatedDate(new Date(System.currentTimeMillis()));		    	
	    		activityEffortTracker.setModifiedBy(userListDAO.getByUserName(resultModifiedBy));
	    		activityTask.setModifiedBy(userListDAO.getByUserName(resultModifiedBy));
	    		    		
	    		activityEntityMaster = activityEntityMasterService.getActivityEntityMasterByName(resultEntity);
		    	activityEffortTracker.setEntityType(activityEntityMaster);		
		    	activityEffortTracker.setActualEffort(taskWiseEffort);
		    	activityEffortTracker.setComments(resultComments);	
		    
		    	activityEffortTrackerDAO.updateActivityEffortTracker(activityEffortTracker);
		    	if(activityEffortTracker!=null){
		    	}
		    	
		        activityTask.setModifiedDate(new Date(System.currentTimeMillis()));
		       
		        Integer totalTaskEfforts= activityEffortTrackerService.getTotalEffortsByTaskId(taskId);
		        activityTask.setTotalEffort(totalTaskEfforts);
	    		activityTaskDAO.updateActivityTask(activityTask);
	    		if(activityTask!=null && activityTask.getActivityTaskId()!=null){
	    			mongoDBService.addActivityTaskToMongoDB(activityTask.getActivityTaskId());
	    		}
	    		
	     	
		}catch (Exception e) {
			return;
		}
	
		
	}

	public HashSet<Integer> getActivtyTaskPrimaryStatus(Integer taskId){
		
			int intilizer=1;
			int isActive=2;
			ActivityTask activityTask =activityTaskDAO.getActivityTaskById(taskId,intilizer);
			List<ActivityTask> activityList=activityTaskDAO.listActivityTasksByActivityId(activityTask.getActivity().getActivityId(), intilizer, isActive);
			HashSet<Integer> activityTaskstatusIds=new HashSet<Integer>();
			if(activityList!=null&& activityList.size()>0){
				for(ActivityTask activityTasks :activityList){
					
					activityTaskstatusIds.add(activityTasks.getStatus().getWorkflowStatusId());
				}
				
			}
			
			return activityTaskstatusIds;	
		
	}

	@Override
	@Transactional
	public Integer countAllactivityTaskService(Date startDate, Date endDate) {
		return activityTaskDAO.countAllactivityTaskService(startDate, endDate);
	}

	@Override
	@Transactional
	public List<ActivityTask> listAllactivityTask(int startIndex, int pageSize,Date startDate, Date endDate) {
		return activityTaskDAO.listAllactivityTask(startIndex,pageSize,startDate, endDate);
	}

	@Override
	@Transactional
	public boolean getActivityTaskById(Integer activityId,String activityTaskName){
		return activityTaskDAO.getActivityTaskById(activityId, activityTaskName);
	}

	@Override
	@Transactional
	public ActivityTaskType getActivityTaskTypeById(Integer activityTaskTypeId) {
		return activityTaskDAO.getActivityTaskTypeById(activityTaskTypeId);
	}

	@Override
	@Transactional
	public void updateActivityBasedOnTasks(Integer activityId) {
		activityTaskDAO.updateActivityBasedOnTasks(activityId);
	}
	
	@Override
	@Transactional
	public int addActivityTasksBulk(List<ActivityTask> activityTaskList,Integer productId){
		Integer numberOfTasks = activityTaskDAO.addBulk(activityTaskList,Integer.parseInt(activitiesMaxBatchCount),productId);
		return numberOfTasks;
	}


	@Override
	@Transactional
	public List<ActivityTaskType> listActivityTaskTypes(Integer testFactoryId, Integer productId, Integer status, Integer jtStartIndex, Integer jtPageSize, Boolean isConsolidated) {
		return activityTaskDAO.listActivityTaskTypes(testFactoryId, productId, status, jtStartIndex, jtPageSize, isConsolidated);
	}

	@Override
	@Transactional
	public Integer getTotalRecordsForTaskTypesPagination(Integer testFactoryId, Integer productId, Integer status, Boolean isConsolidated) {
		return activityTaskDAO.getTotalRecordsForTaskTypesPagination(testFactoryId, productId, status, isConsolidated);
	}

	@Override
	public Boolean checkTaskTypeExistForProduct(Integer testFactoryId, Integer productId, String taskTypeName, Integer activityTaskTypeId, Boolean isConsolidated) {
		return activityTaskDAO.checkTaskTypeExistForProduct(testFactoryId, productId, taskTypeName, activityTaskTypeId, isConsolidated);
	}

	@Override
	public void addActivityTaskType(ActivityTaskType activityTaskType) {
		activityTaskDAO.addActivityTaskType(activityTaskType);
	}

	@Override
	public void updateActivityTaskType(ActivityTaskType activityTaskType) {
		activityTaskDAO.updateActivityTaskType(activityTaskType);
		
	}
	
	@Override
	@Transactional
	public int getActivityStatusCategoryIdByTaskStatus(Integer activityId) {
		return activityTaskDAO.getActivityStatusCategoryIdByTaskStatus(activityId);
	}

	@Override
	@Transactional
	public Integer listActivityTasksCount(Integer productId, Integer productVersionId, Integer productBuildId, Integer activityWorkPackageId, Integer activityId, Integer initializationLevel, Integer isActive, Map<String, String> searchStrings) {
		return activityTaskDAO.listActivityTasksCount(productId, productVersionId, productBuildId, activityWorkPackageId, activityId, initializationLevel, isActive, searchStrings);
	}
}
