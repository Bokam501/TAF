/**
 * 
 */
package com.hcl.ilcm.workflow.dao;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import com.hcl.ilcm.workflow.model.WorkflowEvent;

/**
 * @author silambarasur
 *
 */
public interface WorkflowEventDAO {
	
	List<WorkflowEvent> listWorkflowEvents();
	WorkflowEvent getWorkflowEventById(int effortTrackerId);
	List<WorkflowEvent> listWorkflowEventByEntityTypeAndEnityInstanceId(Integer entityTypeId,Integer entityInstanceId, int initializationLevel);
	void updateWorkflowEvent(WorkflowEvent workflowEntityEffortTracker);
	Integer getTotalEffortsByEntityInstanceIdAndEntityType(int entityId,int entityTypeId);
	Integer countAllEvents(Date startDate, Date endDate);
	List<WorkflowEvent> listAllEvents(int startIndex, int pageSize, Date startDate,Date endDate);
	WorkflowEvent getLatestWorkflowEventAction(Integer entityId,Integer entityTypeId);
	void addWorkflowEvent(WorkflowEvent workflowEvent);
	boolean checkInstanceEligibiltyToChangeWorkflowMapping(Integer entityTypeId, Integer entityId, Integer entityInstanceId);
	List<Object[]> getCurrentStatusActionView(Integer productId,Integer entityTypeId,Integer entityId);
	WorkflowEvent getLastEventOfEntityOrInstanceForStatus(Integer entityTypeId, Integer entityId, Integer entityInstanceId, Integer statusId);
	List<WorkflowEvent> getEventsOfEntityOrInstanceForStatus(Integer entityTypeId, Integer entityId, Integer entityInstanceId, Integer statusId);
	List<WorkflowEvent> listAllWorkflowEvents(Integer startIndex, Integer numberOfRecords, Date startDate, Date endDate);
	Integer countAllWorkflowEvents(Date startDate, Date endDate);
	Integer getTotalEffortsOfEntityOrInstanceIdForStatus(Integer productId, Integer entityTypeId, Integer entityId, Integer entityInstanceId, Integer statusId);
	boolean isWorkflowStatusInEventAction(Integer workflowStatusId);
	List<Object[]> getCurrentStatusActionViewTestFactoryLevel(Integer testFactoryId,Integer productId, Integer entityTypeId, String entityInstanceId, Integer entityId, HashMap<String, String> statusActionQueryDetails);
	WorkflowEvent getLastEventInstanceByEntityInstanceId(Integer entityTypeId,Integer entityInstanceId);
}
