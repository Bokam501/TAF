package com.hcl.ilcm.workflow.model.json;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.hcl.atf.taf.model.StatusCategory;
import com.hcl.atf.taf.model.WorkflowStatusCategory;
import com.hcl.ilcm.workflow.model.WorkflowMaster;
import com.hcl.ilcm.workflow.model.WorkflowStatus;


public class JsonWorkflowStatus {

	
	
	@JsonProperty
	private Integer workflowStatusId;
	@JsonProperty
	private String workflowStatusName;
	@JsonProperty
	private String workflowStatusDisplayName;
	@JsonProperty
	private String workflowStatusDescription;
	@JsonProperty
	private Integer statusCategoryId;
	@JsonProperty
	private String statusCategoryName;
	@JsonProperty
	private Integer activeStatus;
	@JsonProperty
	private Integer workflowId;
	@JsonProperty
	private String workflowName;
	@JsonProperty
	private Integer statusOrder;
	
	@JsonProperty
	private Integer resolution;
	
	@JsonProperty
	private Integer isLifeCycleStage;
	
	@JsonProperty
	private Integer workflowStatusCategoryId;
	
	@JsonProperty
	private String workflowStatusCategoryName;

	
	public JsonWorkflowStatus() {
	}

	public JsonWorkflowStatus(WorkflowStatus workflowStatus) {
		this.workflowStatusId = workflowStatus.getWorkflowStatusId();
		this.workflowStatusName = workflowStatus.getWorkflowStatusName();
		this.workflowStatusDisplayName = workflowStatus.getWorkflowStatusDisplayName();
		this.workflowStatusDescription = workflowStatus.getWorkflowStatusDescription();
		if(workflowStatus.getStatusCategory() != null){
			this.statusCategoryId = workflowStatus.getStatusCategory().getStatusCategoryId();
			this.statusCategoryName = workflowStatus.getStatusCategory().getStatusCategoryName();
		}
		if(workflowStatus.getWorkflowStatusCategory() != null){
			this.workflowStatusCategoryId = workflowStatus.getWorkflowStatusCategory().getWorkflowStatusCategoryId();
			this.workflowStatusCategoryName =workflowStatus.getWorkflowStatusCategory().getWorkflowStatusCategoryName();
		}
		
		this.activeStatus = workflowStatus.getActiveStatus();
		if(workflowStatus.getWorkflow() != null) {
			this.workflowId =workflowStatus.getWorkflow().getWorkflowId();
			this.workflowName = workflowStatus.getWorkflow().getWorkflowName();
		}
		this.statusOrder = workflowStatus.getStatusOrder();
		this.isLifeCycleStage =workflowStatus.getIsLifeCycleStage();
	}


	public String getWorkflowStatusName() {
		return workflowStatusName;
	}

	public void setWorkflowStatusName(String workflowStatusName) {
		this.workflowStatusName = workflowStatusName;
	}

	public String getWorkflowStatusDisplayName() {
		return workflowStatusDisplayName;
	}

	public void setWorkflowStatusDisplayName(String workflowStatusDisplayName) {
		this.workflowStatusDisplayName = workflowStatusDisplayName;
	}

	public String getWorkflowStatusDescription() {
		return workflowStatusDescription;
	}

	public void setWorkflowStatusDescription(String workflowStatusDescription) {
		this.workflowStatusDescription = workflowStatusDescription;
	}

	public Integer getStatusCategoryId() {
		return statusCategoryId;
	}

	public void setStatusCategoryId(Integer statusCategoryId) {
		this.statusCategoryId = statusCategoryId;
	}

	public String getStatusCategoryName() {
		return statusCategoryName;
	}

	public void setStatusCategoryName(String statusCategoryName) {
		this.statusCategoryName = statusCategoryName;
	}

	public Integer getActiveStatus() {
		return activeStatus;
	}

	public void setActiveStatus(Integer activeStatus) {
		this.activeStatus = activeStatus;
	}


	public void setWorkflowStatusId(Integer workflowStatusId) {
		this.workflowStatusId = workflowStatusId;
	}
	
	public Integer getWorkflowStatusId() {
		return workflowStatusId;
	}

	@JsonIgnore
	public WorkflowStatus getWorkflowStatus() {
		WorkflowStatus workflowStatus = new WorkflowStatus();
		if(this.workflowStatusId != null){
			workflowStatus.setWorkflowStatusId(this.workflowStatusId);
		}
		workflowStatus.setWorkflowStatusName(this.workflowStatusName);
		workflowStatus.setWorkflowStatusDisplayName(this.workflowStatusDisplayName);
		workflowStatus.setWorkflowStatusDescription(this.workflowStatusDescription);
		StatusCategory statusCategory = new StatusCategory();
		if(this.statusCategoryId != null && this.statusCategoryId > 0){
			statusCategory.setStatusCategoryId(this.statusCategoryId);
		}else{
			statusCategory.setStatusCategoryId(1);
		}
		workflowStatus.setStatusCategory(statusCategory);
		
		if(this.workflowStatusCategoryId != null) {
			WorkflowStatusCategory workflowStatusCategory = new WorkflowStatusCategory();
			workflowStatusCategory.setWorkflowStatusCategoryId(this.workflowStatusCategoryId);
			workflowStatus.setWorkflowStatusCategory(workflowStatusCategory);
		}
		
		if(this.activeStatus != null){
			workflowStatus.setActiveStatus(this.activeStatus);
		}else{
			workflowStatus.setActiveStatus(1);
		}
		workflowStatus.setStatusOrder(this.statusOrder);
		if(isLifeCycleStage !=null) {
			workflowStatus.setIsLifeCycleStage(isLifeCycleStage);
		}else {
			workflowStatus.setIsLifeCycleStage(1);
		}
		if(this.workflowId != null) {
			WorkflowMaster workflowMaster = new WorkflowMaster();
			workflowMaster.setWorkflowId(workflowId);
			workflowMaster.setWorkflowName(workflowName);
			workflowStatus.setWorkflow(workflowMaster);
		}
		return workflowStatus;
	}

	public Integer getWorkflowId() {
		return workflowId;
	}

	public void setWorkflowId(Integer workflowId) {
		this.workflowId = workflowId;
	}

	public String getWorkflowName() {
		return workflowName;
	}

	public void setWorkflowName(String workflowName) {
		this.workflowName = workflowName;
	}

	public Integer getStatusOrder() {
		return statusOrder;
	}

	public void setStatusOrder(Integer statusOrder) {
		this.statusOrder = statusOrder;
	}

	public Integer getResolution() {
		return resolution;
	}

	public void setResolution(Integer resolution) {
		this.resolution = resolution;
	}

	public Integer getIsLifeCycleStage() {
		return isLifeCycleStage;
	}

	public void setIsLifeCycleStage(Integer isLifeCycleStage) {
		this.isLifeCycleStage = isLifeCycleStage;
	}

	public Integer getWorkflowStatusCategoryId() {
		return workflowStatusCategoryId;
	}

	public void setWorkflowStatusCategoryId(Integer workflowStatusCategoryId) {
		this.workflowStatusCategoryId = workflowStatusCategoryId;
	}

	public String getWorkflowStatusCategoryName() {
		return workflowStatusCategoryName;
	}

	public void setWorkflowStatusCategoryName(String workflowStatusCategoryName) {
		this.workflowStatusCategoryName = workflowStatusCategoryName;
	}
	
}
