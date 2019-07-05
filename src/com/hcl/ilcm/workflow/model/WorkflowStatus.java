/**
 * 
 */
package com.hcl.ilcm.workflow.model;

import static javax.persistence.GenerationType.IDENTITY;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.hcl.atf.taf.model.StatusCategory;
import com.hcl.atf.taf.model.WorkflowStatusCategory;

/**
 * @author silambarasur
 *
 */
@Entity
@Table(name="wf_workflows_status")
public class WorkflowStatus implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 8093438943336656276L;
	
	private Integer workflowStatusId;
	private String workflowStatusName;
	private String workflowStatusDisplayName;
	private String workflowStatusDescription;
	private StatusCategory statusCategory;
	private Integer activeStatus;
	private WorkflowMaster workflow;
	private WorkflowStatus parentStatus;
	private Integer statusOrder;
	private String workflowStatusType;
	private Integer isLifeCycleStage;	
	private WorkflowStatusCategory workflowStatusCategory;
	
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "workflowStatusId",unique = true, nullable = false)
	public Integer getWorkflowStatusId() {
		return workflowStatusId;
	}
	public void setWorkflowStatusId(Integer workflowStatusId) {
		this.workflowStatusId = workflowStatusId;
	}
	
	@Column(name = "workflowStatusName")
	public String getWorkflowStatusName() {
		return workflowStatusName;
	}
	public void setWorkflowStatusName(String workflowStatusName) {
		this.workflowStatusName = workflowStatusName;
	}
	
	@Column(name = "workflowStatusDisplayName")
	public String getWorkflowStatusDisplayName() {
		return workflowStatusDisplayName;
	}
	public void setWorkflowStatusDisplayName(String workflowStatusDisplayName) {
		this.workflowStatusDisplayName = workflowStatusDisplayName;
	}
	
	@Column(name = "description")
	public String getWorkflowStatusDescription() {
		return workflowStatusDescription;
	}
	public void setWorkflowStatusDescription(String workflowStatusDescription) {
		this.workflowStatusDescription = workflowStatusDescription;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "statusCategoryId")
	public StatusCategory getStatusCategory() {
		return statusCategory;
	}
	
	public void setStatusCategory(StatusCategory statusCategory) {
		this.statusCategory = statusCategory;
	}
	
	@Column(name = "activeStatus")
	public Integer getActiveStatus() {
		return activeStatus;
	}
	public void setActiveStatus(Integer activeStatus) {
		this.activeStatus = activeStatus;
	}
	
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "parentStatusId")
	public WorkflowStatus getParentStatus() {
		return parentStatus;
	}
	public void setParentStatus(WorkflowStatus parentStatus) {
		this.parentStatus = parentStatus;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "workflowId")
	public WorkflowMaster getWorkflow() {
		return workflow;
	}
	public void setWorkflow(WorkflowMaster workflow) {
		this.workflow = workflow;
	}
	
	@Column(name="statusOrder")
	public Integer getStatusOrder() {
		return statusOrder;
	}
	public void setStatusOrder(Integer statusOrder) {
		this.statusOrder = statusOrder;
	}
	
	@Column(name="workflowStatusType")
	public String getWorkflowStatusType() {
		return workflowStatusType;
	}
	public void setWorkflowStatusType(String workflowStatusType) {
		this.workflowStatusType = workflowStatusType;
	}
	
	@Column(name="isLifeCycleStage")
	public Integer getIsLifeCycleStage() {
		return isLifeCycleStage;
	}
	public void setIsLifeCycleStage(Integer isLifeCycleStage) {
		this.isLifeCycleStage = isLifeCycleStage;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "workflowStatusCategoryId",nullable=true)
	public WorkflowStatusCategory getWorkflowStatusCategory() {
		return workflowStatusCategory;
	}
	public void setWorkflowStatusCategory(WorkflowStatusCategory workflowStatusCategory) {
		this.workflowStatusCategory = workflowStatusCategory;
	}
	
}
