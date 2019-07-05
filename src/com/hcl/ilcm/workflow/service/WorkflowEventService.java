/**
 * 
 */
package com.hcl.ilcm.workflow.service;

import java.util.Date;
import java.util.List;

import com.hcl.atf.taf.model.UserList;
import com.hcl.ilcm.workflow.model.WorkflowEvent;

/**
 * @author silambarasur
 *
 */
public interface WorkflowEventService {
	

	List<WorkflowEvent> listWorkflowEvents();
	WorkflowEvent getWorkflowEventById(int effortTrackerId);
	List<WorkflowEvent> listWorkflowEventByEntityTypeAndEnityInstanceId(Integer entityTypeId,Integer entityInstanceId, int initializationLevel);
	void updateWorkflowEvent(WorkflowEvent workflowEntityEffortTracker);
	Integer getTotalEffortsByEntityInstanceIdAndEntityType(int entityId,int entityTypeId);
	Integer countAllEvents(Date startDate, Date endDate);
	List<WorkflowEvent> listAllEvents(int startIndex, int pageSize, Date startDate,Date endDate);
	WorkflowEvent getLatestWorkflowEventAction(Integer entityId,Integer entityTypeId);
	String addWorkflowEvent(Integer productId, String approveAllEntityInstanceIds,UserList user, Integer entityId,Integer entityTypeId, Integer primaryStatusId,Integer secondaryStatusId, Integer effort, String comments,Integer sourceStatusId,Integer entityInstanceId, String attachmentIds, List<Integer> possibleIds, Boolean isStatusChangeAllowed, Date actionDate,Integer actualSize);
	boolean checkInstanceEligibiltyToChangeWorkflowMapping(Integer entityTypeId, Integer entityId, Integer entityInstanceId);
	void setInitialInstanceEvent(Integer productId, Integer entityTypeId, Integer entityId, Integer entityInstanceId, Integer statusId, Integer secondaryStatusId, Integer effort, String comments, UserList user);
	List<WorkflowEvent> listAllWorkflowEvents(Integer startIndex, Integer numberOfRecords, Date startDate, Date endDate);
	Integer countAllWorkflowEvents(Date startDate, Date endDate);

	List<Object[]> getCurrentStatusActionView(Integer productId,Integer entityTypeId,Integer entityId);
	WorkflowEvent getLastEventOfEntityOrInstanceForStatus(Integer entityTypeId, Integer entityId, Integer entityInstanceId, Integer statusId);
	List<WorkflowEvent> getEventsOfEntityOrInstanceForStatus(Integer entityTypeId, Integer entityId, Integer entityInstanceId, Integer statusId);
	Integer getTotalEffortsOfEntityOrInstanceIdForStatus(Integer productId, Integer entityTypeId, Integer entityId, Integer entityInstanceId, Integer statusId);
	void checkAndUpdateStatusOfInstanceParentOrChild(Integer productId, Integer entityTypeId, Integer entityInstanceId, Object instanceObject);
	boolean isWorkflowStatusInEventAction(Integer workflowStatusId);
	WorkflowEvent getLastEventInstanceByEntityInstanceId(Integer entityTypeId,Integer entityInstanceId);
	
	String updateWorkflowEvent(Integer productId, String approveAllEntityInstanceIds,UserList user, Integer entityId,Integer entityTypeId, Integer primaryStatusId,Integer secondaryStatusId, Integer effort, String comments,Integer sourceStatusId,Integer entityInstanceId, String attachmentIds, List<Integer> possibleIds, Boolean isStatusChangeAllowed, Date actionDate,Integer actualSize,Date workflowPlannedStartDate,Date workflowPlannedEndDate,String newEffortAction);
}
