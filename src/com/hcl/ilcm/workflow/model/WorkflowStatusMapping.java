/**
 * 
 */
package com.hcl.ilcm.workflow.model;

import static javax.persistence.GenerationType.IDENTITY;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.hcl.atf.taf.model.UserList;

/**
 * @author silambarasur
 *
 */
@Entity
@Table(name="wf_workflows_status_mapping")
public class WorkflowStatusMapping implements Serializable{

	private static final long serialVersionUID = 8093438943336656276L;
	
	private Integer workflowStatusMappingId;
	private WorkflowMaster workflow;
	private WorkflowStatus workflowSourceStatusId;	
	private WorkflowStatus workflowTargetStatusId;	
	private Date mappedOn;
	private UserList mappedBy;
	
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "workflowStatusMappingId",unique = true, nullable = false)
	public Integer getWorkflowStatusMappingId() {
		return workflowStatusMappingId;
	}
	public void setWorkflowStatusMappingId(Integer workflowStatusMappingId) {
		this.workflowStatusMappingId = workflowStatusMappingId;
	}	
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "workflowId")
	public WorkflowMaster getWorkflow() {
		return workflow;
	}
	public void setWorkflow(WorkflowMaster workflow) {
		this.workflow = workflow;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "workflowSourceStatusId")
	public WorkflowStatus getWorkflowSourceStatusId() {
		return workflowSourceStatusId;
	}
	public void setWorkflowSourceStatusId(WorkflowStatus workflowSourceStatusId) {
		this.workflowSourceStatusId = workflowSourceStatusId;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "workflowTargetStatusId")
	public WorkflowStatus getWorkflowTargetStatusId() {
		return workflowTargetStatusId;
	}
	public void setWorkflowTargetStatusId(WorkflowStatus workflowTargetStatusId) {
		this.workflowTargetStatusId = workflowTargetStatusId;
	}
	
	@Column(name = "mappedOn")
	public Date getMappedOn() {
		return mappedOn;
	}
	public void setMappedOn(Date mappedOn) {
		this.mappedOn = mappedOn;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "mappedById")
	public UserList getMappedBy() {
		return mappedBy;
	}
	public void setMappedBy(UserList mappedBy) {
		this.mappedBy = mappedBy;
	}
	
}
