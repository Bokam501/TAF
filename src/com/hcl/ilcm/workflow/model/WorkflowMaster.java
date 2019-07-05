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

@Entity
@Table(name = "wf_workflows_master")
public class WorkflowMaster implements Serializable{

	private static final long serialVersionUID = 1L;


	private Integer workflowId;
	
	private String workflowName;
	
	private String workflowStatusRulesFileId;
	
	private String workflowStatusRulesFileName;
	
	private String description;
	
	private Integer activeState;
	
	private UserList createdBy;
	
	private Date createdDate;
	
	private UserList modifiedBy;
	
	private Date modifiedDate;
	
	private Integer readyState;

	private String workflowType;
	
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	public Integer getWorkflowId() {
		return workflowId;
	}

	public void setWorkflowId(Integer workflowId) {
		this.workflowId = workflowId;
	}

	
	@Column(name = "workflowName")
	public String getWorkflowName() {
		return workflowName;
	}

	public void setWorkflowName(String workflowName) {
		this.workflowName = workflowName;
	}

	@Column(name = "workflowStatusRulesFileId")
	public String getWorkflowStatusRulesFileId() {
		return workflowStatusRulesFileId;
	}

	public void setWorkflowStatusRulesFileId(String workflowStatusRulesFileId) {
		this.workflowStatusRulesFileId = workflowStatusRulesFileId;
	}

	@Column(name = "workflowStatusRulesFileName")
	public String getWorkflowStatusRulesFileName() {
		return workflowStatusRulesFileName;
	}

	public void setWorkflowStatusRulesFileName(String workflowStatusRulesFileName) {
		this.workflowStatusRulesFileName = workflowStatusRulesFileName;
	}
	
	@Column(name = "description")
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	@Column(name = "activeState")
	public Integer getActiveState() {
		return activeState;
	}

	public void setActiveState(Integer activeState) {
		this.activeState = activeState;
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

	@Column(name = "readyState")
	public Integer getReadyState() {
		return readyState;
	}

	public void setReadyState(Integer readyState) {
		this.readyState = readyState;
	}

	@Column(name="workflowType")
	public String getWorkflowType() {
		return workflowType;
	}

	public void setWorkflowType(String workflowType) {
		this.workflowType = workflowType;
	}
	
	
	
}
