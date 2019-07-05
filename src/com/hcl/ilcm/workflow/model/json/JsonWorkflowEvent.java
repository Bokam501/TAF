/**
 * 
 */
package com.hcl.ilcm.workflow.model.json;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.hcl.atf.taf.controller.utilities.DateUtility;
import com.hcl.atf.taf.model.ActivityTask;
import com.hcl.atf.taf.model.ProductMaster;
import com.hcl.atf.taf.model.UserList;
import com.hcl.ilcm.workflow.model.WorkflowEvent;
import com.hcl.ilcm.workflow.model.WorkflowStatus;

/**
 * @author silambarasur
 *
 */
public class JsonWorkflowEvent {
	
	@JsonProperty
	private Integer workflowEventId;
	@JsonProperty	
	private Integer entityId;
	@JsonProperty	
	private Integer entityInstanceId;
	@JsonProperty	
	private String entityName;
	@JsonProperty
	private Integer entityTypeId;
	@JsonProperty
	private String entityTypeName;
	@JsonProperty
	private Integer modifiedById;
	@JsonProperty
	private String modifiedByName;
	@JsonProperty
	private Integer currentStatusId;
	@JsonProperty
	private String currentStatusName;
	@JsonProperty
	private Integer targetStatusId;
	@JsonProperty
	private String targetStatusName;
	@JsonProperty
	private String lastUpdatedDate;
	@JsonProperty
	private String plannedStartDate;
	@JsonProperty
	private String comments;
	
	@JsonProperty
	private Integer productId;
	
	@JsonProperty
	private String productName;
	
	@JsonProperty private String plannedEndDate;
	@JsonProperty private String actualStartDate;
	@JsonProperty private String actualEndDate;
	@JsonProperty private Integer plannedEffort;
	@JsonProperty private Integer actualEffort;
	@JsonProperty private Integer entityGroupId;
	
	@JsonProperty private Integer slaDurationPlanned;
	@JsonProperty private Integer slaDurationActual;
	@JsonProperty private String slaRAG;
	@JsonProperty
	private Integer attachmentCount;

	@JsonProperty
	private Integer actualSize;

	
	
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	public JsonWorkflowEvent() {
	}
	
	public JsonWorkflowEvent(WorkflowEvent entityEffortTracker) {
		this.workflowEventId = entityEffortTracker.getWorkflowEventId();
	    this.lastUpdatedDate = DateFormat.getDateTimeInstance(DateFormat.MEDIUM,DateFormat.SHORT).format(entityEffortTracker.getLastUpdatedDate());
	    this.actualEffort = entityEffortTracker.getActualEffort();
	    this.plannedEffort = entityEffortTracker.getPlannedEffort();
	    this.comments = entityEffortTracker.getComments();
	    this.entityInstanceId=entityEffortTracker.getEntityInstanceId();
	    
	    if (entityEffortTracker.getEntityType() != null) {
			this.entityTypeId = entityEffortTracker.getEntityId();
			this.entityTypeName = entityEffortTracker.getEntityType().getEntitymastername();
		}
		if (entityEffortTracker.getEntityType() != null) {			
			this.entityId = entityEffortTracker.getEntityId();
		}
		if (entityEffortTracker.getCurrentStatus() != null) {			
			this.currentStatusId = entityEffortTracker.getCurrentStatus().getWorkflowStatusId();
			this.currentStatusName = entityEffortTracker.getCurrentStatus().getWorkflowStatusDisplayName();
		}
		if (entityEffortTracker.getCurrentStatus() == null) {		
			this.currentStatusName = "--";
		}
		if (entityEffortTracker.getTargetStatus() != null) {			
			this.targetStatusId = entityEffortTracker.getTargetStatus().getWorkflowStatusId();
			this.targetStatusName = entityEffortTracker.getTargetStatus().getWorkflowStatusDisplayName();
		}
		
		if(entityEffortTracker.getTargetStatus() == null) {
				this.targetStatusName = "--";
		}
		
		if (entityEffortTracker.getModifiedBy() != null) {			
			this.modifiedById = entityEffortTracker.getModifiedBy().getUserId();
			this.modifiedByName = entityEffortTracker.getModifiedBy().getLoginId();
		}
		
		if(entityEffortTracker.getProduct() != null) {
			this.productId = entityEffortTracker.getProduct().getProductId();
			this.productName = entityEffortTracker.getProduct().getProductName();
		}
		
		this.slaRAG = " <i class='fa fa-circle' style='color: green;' title='Ontime'></i> ";
		if(entityEffortTracker.getSlaDurationActual() != null && entityEffortTracker.getSlaDurationPlanned() != null){
			if(entityEffortTracker.getSlaDurationActual() > entityEffortTracker.getSlaDurationPlanned()){
				this.slaRAG = " <i class='fa fa-circle' style='color: red;' title='Over due'></i> ";
			}
		}
		this.slaDurationPlanned = entityEffortTracker.getSlaDurationPlanned();
		this.slaDurationActual = entityEffortTracker.getSlaDurationActual();
		this.actualSize = entityEffortTracker.getActualSize();
	}
	@JsonIgnore
	public WorkflowEvent getWorkflowEntityEffortTracker() {
		WorkflowEvent workflowEntityEffortTracker = new WorkflowEvent();
		workflowEntityEffortTracker.setWorkflowEventId(workflowEventId);
		workflowEntityEffortTracker.setPlannedEffort(plannedEffort);
		workflowEntityEffortTracker.setActualEffort(actualEffort);
		workflowEntityEffortTracker.setComments(comments);
		workflowEntityEffortTracker.setEntityInstanceId(entityInstanceId);
		
		workflowEntityEffortTracker.setActualSize(actualSize);
		
		if (this.entityId != null) {
			ActivityTask activityTask = new ActivityTask();			
			activityTask.setActivityTaskId(entityTypeId);
			activityTask.setActivityTaskName(entityTypeName);
		}
		if (this.currentStatusId != null) {
			WorkflowStatus workflowStatus=new WorkflowStatus();
			workflowStatus.setWorkflowStatusId(currentStatusId);
			workflowStatus.setWorkflowStatusName(currentStatusName);
			workflowEntityEffortTracker.setCurrentStatus(workflowStatus);
			
		}
		if (this.targetStatusId != null) {
			WorkflowStatus workflowStatus=new WorkflowStatus();
			workflowStatus.setWorkflowStatusId(targetStatusId);
			workflowStatus.setWorkflowStatusName(targetStatusName);
			workflowEntityEffortTracker.setTargetStatus(workflowStatus);
			
		}
		if (this.modifiedById != null) {
			UserList userList = new UserList();
			userList.setUserId(modifiedById);
			userList.setLoginId(modifiedByName);
			workflowEntityEffortTracker.setModifiedBy(userList);
		}
		if(this.productId != null) {
			ProductMaster product= new ProductMaster();
			product.setProductId(productId);
			workflowEntityEffortTracker.setProduct(product);
		}
		workflowEntityEffortTracker.setLastUpdatedDate(DateUtility.getCurrentTime());
		workflowEntityEffortTracker.setActualStartDate(DateUtility.getCurrentTime());
		workflowEntityEffortTracker.setActualEndDate(DateUtility.getCurrentTime());
		workflowEntityEffortTracker.setPlannedStartDate(DateUtility.getCurrentTime());
		workflowEntityEffortTracker.setPlannedEndDate(DateUtility.getCurrentTime());
		
		return workflowEntityEffortTracker;
	    }

	
	public Integer getworkflowEntityEffortTrackerId() {
		return workflowEventId;
	}
	public void setWorkflowEntityEffortTrackerId(Integer workflowEntityEffortTrackerId) {
		this.workflowEventId = workflowEntityEffortTrackerId;
	}
	public Integer getEntityId() {
		return entityId;
	}
	public void setEntityId(Integer entityId) {
		this.entityId = entityId;
	}
	public String getEntityName() {
		return entityName;
	}
	public void setEntityName(String entityName) {
		this.entityName = entityName;
	}
	public Integer getEntityTypeId() {
		return entityTypeId;
	}
	public void setEntityTypeId(Integer entityTypeId) {
		this.entityTypeId = entityTypeId;
	}
	public String getEntityTypeName() {
		return entityTypeName;
	}
	public void setEntityTypeName(String entityTypeName) {
		this.entityTypeName = entityTypeName;
	}
	
	public Integer getCurrentStatusId() {
		return currentStatusId;
	}
	public void setCurrentStatusId(Integer currentStatusId) {
		this.currentStatusId = currentStatusId;
	}
	public String getCurrentStatusName() {
		return currentStatusName;
	}
	public void setCurrentStatusName(String currentStatusName) {
		this.currentStatusName = currentStatusName;
	}
	public Integer getTargetStatusId() {
		return targetStatusId;
	}
	public void setTargetStatusId(Integer targetStatusId) {
		this.targetStatusId = targetStatusId;
	}
	public String getTargetStatusName() {
		return targetStatusName;
	}
	public void setTargetStatusName(String targetStatusName) {
		this.targetStatusName = targetStatusName;
	}
	public String getLastUpdatedDate() {
		return lastUpdatedDate;
	}
	public void setLastUpdatedDate(String lastUpdatedDate) {
		this.lastUpdatedDate = lastUpdatedDate;
	}

	public Integer getModifiedById() {
		return modifiedById;
	}

	public void setModifiedById(Integer modifiedById) {
		this.modifiedById = modifiedById;
	}

	public String getModifiedByName() {
		return modifiedByName;
	}

	public void setModifiedByName(String modifiedByName) {
		this.modifiedByName = modifiedByName;
	}

	public String getPlannedStartDate() {
		return plannedStartDate;
	}

	public void setPlannedStartDate(String plannedStartDate) {
		this.plannedStartDate = plannedStartDate;
	}

	public String getPlannedEndDate() {
		return plannedEndDate;
	}

	public void setPlannedEndDate(String plannedEndDate) {
		this.plannedEndDate = plannedEndDate;
	}

	public String getActualStartDate() {
		return actualStartDate;
	}

	public void setActualStartDate(String actualStartDate) {
		this.actualStartDate = actualStartDate;
	}

	public String getActualEndDate() {
		return actualEndDate;
	}

	public void setActualEndDate(String actualEndDate) {
		this.actualEndDate = actualEndDate;
	}

	public Integer getPlannedEffort() {
		return plannedEffort;
	}

	public void setPlannedEffort(Integer plannedEffort) {
		this.plannedEffort = plannedEffort;
	}

	public Integer getActualEffort() {
		return actualEffort;
	}

	public void setActualEffort(Integer actualEffort) {
		this.actualEffort = actualEffort;
	}

	public Integer getEntityGroupId() {
		return entityGroupId;
	}

	public void setEntityGroupId(Integer entityGroupId) {
		this.entityGroupId = entityGroupId;
	}	

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public Integer getWorkflowEventId() {
		return workflowEventId;
	}

	public void setWorkflowEventId(Integer workflowEventId) {
		this.workflowEventId = workflowEventId;
	}

	public Integer getEntityInstanceId() {
		return entityInstanceId;
	}

	public void setEntityInstanceId(Integer entityInstanceId) {
		this.entityInstanceId = entityInstanceId;
	}

	public Integer getProductId() {
		return productId;
	}

	public void setProductId(Integer productId) {
		this.productId = productId;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public Integer getSlaDurationPlanned() {
		return slaDurationPlanned;
	}

	public void setSlaDurationPlanned(Integer slaDurationPlanned) {
		this.slaDurationPlanned = slaDurationPlanned;
	}

	public Integer getSlaDurationActual() {
		return slaDurationActual;
	}

	public void setSlaDurationActual(Integer slaDurationActual) {
		this.slaDurationActual = slaDurationActual;
	}

	public String getSlaRAG() {
		return slaRAG;
	}

	public void setSlaRAG(String slaRAG) {
		this.slaRAG = slaRAG;
	}

	public Integer getAttachmentCount() {
		return attachmentCount;
	}

	public void setAttachmentCount(Integer attachmentCount) {
		this.attachmentCount = attachmentCount;
	}

	public Integer getActualSize() {
		return actualSize;
	}

	public void setActualSize(Integer actualSize) {
		this.actualSize = actualSize;
	}
	
	
	
}
