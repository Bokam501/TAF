package com.hcl.atf.taf.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.hcl.ilcm.workflow.model.WorkflowMaster;

@Entity
@Table(name = "wf_status_category")
public class WorkflowStatusCategory {

	private Integer workflowStatusCategoryId;
	private String  workflowStatusCategoryName;
	private Integer  workflowStatusCategoryOrder;
	private Double  workflowStatusCategoryIndicator;
	private Integer activeStatus;
	private WorkflowMaster workflow;
	private String description;
	private UserList createdBy;
	private Date createdDate;
	private UserList modifiedBy;
	private Date modifiedDate;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "workflowStatusCategoryId", unique = true, nullable = false)
	public Integer getWorkflowStatusCategoryId() {
		return workflowStatusCategoryId;
	}

	public void setWorkflowStatusCategoryId(Integer workflowStatusCategoryId) {
		this.workflowStatusCategoryId = workflowStatusCategoryId;
	}

	@Column(name = "workflowStatusCategoryName")
	public String getWorkflowStatusCategoryName() {
		return workflowStatusCategoryName;
	}

	public void setWorkflowStatusCategoryName(String workflowStatusCategoryName) {
		this.workflowStatusCategoryName = workflowStatusCategoryName;
	}

	@Column(name = "workflowStatusCategoryOrder")
	public Integer getWorkflowStatusCategoryOrder() {
		return workflowStatusCategoryOrder;
	}

	public void setWorkflowStatusCategoryOrder(Integer workflowStatusCategoryOrder) {
		this.workflowStatusCategoryOrder = workflowStatusCategoryOrder;
	}

	@Column(name = "workflowStatusCategoryIndicator")
	public Double getWorkflowStatusCategoryIndicator() {
		return workflowStatusCategoryIndicator;
	}

	public void setWorkflowStatusCategoryIndicator(
			Double workflowStatusCategoryIndicator) {
		this.workflowStatusCategoryIndicator = workflowStatusCategoryIndicator;
	}

	@Column(name = "activeStatus")
	public Integer getActiveStatus() {
		return activeStatus;
	}

	public void setActiveStatus(Integer activeStatus) {
		this.activeStatus = activeStatus;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "workflowId")
	public WorkflowMaster getWorkflow() {
		return workflow;
	}
	public void setWorkflow(WorkflowMaster workflow) {
		this.workflow = workflow;
	}
	
	@Column(name="description")
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "createdBy")
	public UserList getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(UserList createdBy) {
		this.createdBy = createdBy;
	}

	@Column(name = "createdDate")
	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "modifiedBy")
	public UserList getModifiedBy() {
		return modifiedBy;
	}

	public void setModifiedBy(UserList modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	@Column(name = "modifiedDate")
	public Date getModifiedDate() {
		return modifiedDate;
	}

	public void setModifiedDate(Date modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

}
