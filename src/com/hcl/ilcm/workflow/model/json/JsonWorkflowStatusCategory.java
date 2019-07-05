/**
 * 
 */
package com.hcl.ilcm.workflow.model.json;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.hcl.atf.taf.model.WorkflowStatusCategory;
import com.hcl.ilcm.workflow.model.WorkflowMaster;

/**
 * @author silambarasur
 * 
 */
public class JsonWorkflowStatusCategory {

	@JsonProperty
	private Integer workflowStatusCategoryId;
	@JsonProperty
	private String workflowStatusCategoryName;
	@JsonProperty
	private Integer workflowStatusCategoryOrder;
	@JsonProperty
	private Double workflowStatusCategoryIndicator;
	@JsonProperty
	private Integer activeStatus;
	@JsonProperty
	private Integer workflowId;
	@JsonProperty
	private String workflowName;
	@JsonProperty
	private String description;
	
	public JsonWorkflowStatusCategory() {
		
	}
	
	public JsonWorkflowStatusCategory(WorkflowStatusCategory workflowStatusCategory) {
		this.workflowStatusCategoryId = workflowStatusCategory.getWorkflowStatusCategoryId();
		this.workflowStatusCategoryName = workflowStatusCategory.getWorkflowStatusCategoryName();
		this.workflowStatusCategoryOrder = workflowStatusCategory.getWorkflowStatusCategoryOrder();
		this.workflowStatusCategoryIndicator = workflowStatusCategory.getWorkflowStatusCategoryIndicator();
		this.activeStatus = workflowStatusCategory.getActiveStatus();
		if (workflowStatusCategory != null && workflowStatusCategory.getWorkflow() != null) {
			this.workflowId = workflowStatusCategory.getWorkflow().getWorkflowId();
			this.workflowName = workflowStatusCategory.getWorkflow().getWorkflowName();
		}
		this.description = workflowStatusCategory.getDescription();
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

	public Integer getWorkflowStatusCategoryOrder() {
		return workflowStatusCategoryOrder;
	}

	public void setWorkflowStatusCategoryOrder(Integer workflowStatusCategoryOrder) {
		this.workflowStatusCategoryOrder = workflowStatusCategoryOrder;
	}

	public Double getWorkflowStatusCategoryIndicator() {
		return workflowStatusCategoryIndicator;
	}

	public void setWorkflowStatusCategoryIndicator(
			Double workflowStatusCategoryIndicator) {
		this.workflowStatusCategoryIndicator = workflowStatusCategoryIndicator;
	}

	public Integer getActiveStatus() {
		return activeStatus;
	}

	public void setActiveStatus(Integer activeStatus) {
		this.activeStatus = activeStatus;
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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@JsonIgnore
	public WorkflowStatusCategory getWorkflowStatusCategory(){
		WorkflowStatusCategory workflowStatusCategory = new WorkflowStatusCategory();
		workflowStatusCategory.setWorkflowStatusCategoryId(this.workflowStatusCategoryId);
		workflowStatusCategory.setWorkflowStatusCategoryName(this.workflowStatusCategoryName);
		workflowStatusCategory.setWorkflowStatusCategoryOrder(this.workflowStatusCategoryOrder);
		workflowStatusCategory.setWorkflowStatusCategoryIndicator(this.workflowStatusCategoryIndicator);
		workflowStatusCategory.setActiveStatus(this.activeStatus);
		workflowStatusCategory.setDescription(this.description);
		if(this.workflowId != null) {
			WorkflowMaster workflowMaster = new WorkflowMaster();
			workflowMaster.setWorkflowId(this.workflowId);
			workflowMaster.setWorkflowName(this.workflowName);
			workflowStatusCategory.setWorkflow(workflowMaster);
		}
		return workflowStatusCategory;
	}

}
