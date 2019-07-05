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

import com.hcl.atf.taf.model.EntityMaster;

/**
 * @author silambarasur
 *
 */
@Entity
@Table(name="wf_workflows_status_policies")
public class WorkflowStatusPolicy implements Serializable, Cloneable {
	
	private static final long serialVersionUID = 1L;
	
	private Integer workflowStatusPolicyId;
	private WorkflowStatus workflowStatus;
	private String statusPolicyType;
	private EntityMaster entityType;
	private Integer entityId;
	private Integer entityInstanceId;
	private String level;
	private Integer levelId;
	private Integer activeStatus;
	private Integer weightage;
	private String actionScope;
	private Integer slaDuration;
	private String slaViolationAction;
	private String stautsTransitionPolicy;
	private Date plannedStartDate;
	private Date plannedEndDate;
	private Float plannedEffort;
	private Date actualStartDate;
	private Date actualEndDate;
	private Float actualEffort;
	
	
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "workflowStatusPolicyId",unique = true, nullable = false)
	public Integer getWorkflowStatusPolicyId() {
		return workflowStatusPolicyId;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "workflowStatusId")
	public WorkflowStatus getWorkflowStatus() {
		return workflowStatus;
	}
	@Column(name="statusPolicyType")
	public String getStatusPolicyType() {
		return statusPolicyType;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "entityTypeId", nullable = true)
	public EntityMaster getEntityType() {
		return entityType;
	}
	
	@Column(name="entityId")
	public Integer getEntityId() {
		return entityId;
	}
	
	@Column(name="entityInstanceId")
	public Integer getEntityInstanceId() {
		return entityInstanceId;
	}
	
	@Column(name="level")
	public String getLevel() {
		return level;
	}
	
	@Column(name="levelId")
	public Integer getLevelId() {
		return levelId;
	}
	
	@Column(name="activeStatus")
	public Integer getActiveStatus() {
		return activeStatus;
	}
	
	@Column(name="weightage")
	public Integer getWeightage() {
		return weightage;
	}
	
	@Column(name="actionScope")
	public String getActionScope() {
		return actionScope;
	}
	
	@Column(name="slaDuration")
	public Integer getSlaDuration() {
		return slaDuration;
	}
	
	@Column(name="slaViolationAction")
	public String getSlaViolationAction() {
		return slaViolationAction;
	}
	
	
	public void setWorkflowStatusPolicyId(Integer workflowStatusPolicyId) {
		this.workflowStatusPolicyId = workflowStatusPolicyId;
	}
	public void setWorkflowStatus(WorkflowStatus workflowStatus) {
		this.workflowStatus = workflowStatus;
	}
	public void setStatusPolicyType(String statusPolicyType) {
		this.statusPolicyType = statusPolicyType;
	}
	public void setEntityType(EntityMaster entityType) {
		this.entityType = entityType;
	}
	public void setEntityId(Integer entityId) {
		this.entityId = entityId;
	}
	public void setEntityInstanceId(Integer entityInstanceId) {
		this.entityInstanceId = entityInstanceId;
	}
	public void setLevel(String level) {
		this.level = level;
	}
	public void setLevelId(Integer levelId) {
		this.levelId = levelId;
	}
	public void setActiveStatus(Integer activeStatus) {
		this.activeStatus = activeStatus;
	}
	public void setWeightage(Integer weightage) {
		this.weightage = weightage;
	}
	public void setActionScope(String actionScope) {
		this.actionScope = actionScope;
	}
	public void setSlaDuration(Integer slaDuration) {
		this.slaDuration = slaDuration;
	}
	public void setSlaViolationAction(String slaViolationAction) {
		this.slaViolationAction = slaViolationAction;
	}
	
	public Object clone() throws CloneNotSupportedException{  
		return super.clone();  
	}

	@Column(name="stautsTransitionPolicy")
	public String getStautsTransitionPolicy() {
		return stautsTransitionPolicy;
	}

	public void setStautsTransitionPolicy(String stautsTransitionPolicy) {
		this.stautsTransitionPolicy = stautsTransitionPolicy;
	}

	@Column(name="plannedStartDate")
	public Date getPlannedStartDate() {
		return plannedStartDate;
	}

	public void setPlannedStartDate(Date plannedStartDate) {
		this.plannedStartDate = plannedStartDate;
	}

	@Column(name="plannedEndDate")
	public Date getPlannedEndDate() {
		return plannedEndDate;
	}

	public void setPlannedEndDate(Date plannedEndDate) {
		this.plannedEndDate = plannedEndDate;
	}

	@Column(name="plannedEffort")
	public Float getPlannedEffort() {
		return plannedEffort;
	}

	public void setPlannedEffort(Float plannedEffort) {
		this.plannedEffort = plannedEffort;
	}

	@Column(name="actualStartDate")
	public Date getActualStartDate() {
		return actualStartDate;
	}

	public void setActualStartDate(Date actualStartDate) {
		this.actualStartDate = actualStartDate;
	}

	@Column(name="actualEndDate")
	public Date getActualEndDate() {
		return actualEndDate;
	}

	public void setActualEndDate(Date actualEndDate) {
		this.actualEndDate = actualEndDate;
	}

	@Column(name="actualEffort")
	public Float getActualEffort() {
		return actualEffort;
	}

	public void setActualEffort(Float actualEffort) {
		this.actualEffort = actualEffort;
	}
	
	

}
