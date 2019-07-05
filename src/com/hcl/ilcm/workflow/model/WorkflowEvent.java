/**
 * 
 */
package com.hcl.ilcm.workflow.model;

import static javax.persistence.GenerationType.IDENTITY;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import com.hcl.atf.taf.model.EntityMaster;
import com.hcl.atf.taf.model.ProductMaster;
import com.hcl.atf.taf.model.UserList;

/**
 * @author silambarasur
 * 
 */
@Entity
@Table(name = "wf_workflow_events")
public class WorkflowEvent {

	private Integer workflowEventId;
	private Integer entityId;
	private EntityMaster entityType;
	private WorkflowStatus currentStatus;
	private WorkflowStatus targetStatus;
	private UserList modifiedBy;
	private Date lastUpdatedDate;
	private Date plannedStartDate;
	private Date plannedEndDate;
	private Date actualStartDate;
	private Date actualEndDate;
	private Integer plannedEffort;
	private Integer actualEffort;
	private Integer entityGroupId;
	private String comments;
	private Integer entityInstanceId;
	private ProductMaster product;
	private Integer slaDurationPlanned;
	private Integer slaDurationActual;

	private Object instanceObject;

	private Integer actualSize;

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "workflowEventId", unique = true, nullable = false)
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

	@Column(name = "entityId")
	public Integer getEntityId() {
		return entityId;
	}

	public void setEntityId(Integer entityId) {
		this.entityId = entityId;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "entityTypeId")
	public EntityMaster getEntityType() {
		return entityType;
	}

	public void setEntityType(EntityMaster entityType) {
		this.entityType = entityType;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "currentStatusId", nullable = true)
	public WorkflowStatus getCurrentStatus() {
		return currentStatus;
	}

	public void setCurrentStatus(WorkflowStatus currentStatus) {
		this.currentStatus = currentStatus;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "targetStatusId", nullable = true)
	public WorkflowStatus getTargetStatus() {
		return targetStatus;
	}

	public void setTargetStatus(WorkflowStatus targetStatus) {
		this.targetStatus = targetStatus;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "modifiedBy")
	public UserList getModifiedBy() {
		return modifiedBy;
	}

	public void setModifiedBy(UserList modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "lastUpdatedDate")
	public void setLastUpdatedDate(Date lastUpdatedDate) {
		this.lastUpdatedDate = lastUpdatedDate;
	}

	public Date getLastUpdatedDate() {
		return lastUpdatedDate;
	}

	@Column(name = "plannedStartDate")
	public Date getPlannedStartDate() {
		return plannedStartDate;
	}

	public void setPlannedStartDate(Date plannedStartDate) {
		this.plannedStartDate = plannedStartDate;
	}

	@Column(name = "plannedEndDate")
	public Date getPlannedEndDate() {
		return plannedEndDate;
	}

	public void setPlannedEndDate(Date plannedEndDate) {
		this.plannedEndDate = plannedEndDate;
	}

	@Column(name = "actualStartDate")
	public Date getActualStartDate() {
		return actualStartDate;
	}

	public void setActualStartDate(Date actualStartDate) {
		this.actualStartDate = actualStartDate;
	}

	@Column(name = "actualEndDate")
	public Date getActualEndDate() {
		return actualEndDate;
	}

	public void setActualEndDate(Date actualEndDate) {
		this.actualEndDate = actualEndDate;
	}

	@Column(name = "plannedEffort")
	public Integer getPlannedEffort() {
		return plannedEffort;
	}

	public void setPlannedEffort(Integer plannedEffort) {
		this.plannedEffort = plannedEffort;
	}

	@Column(name = "actualEffort")
	public Integer getActualEffort() {
		return actualEffort;
	}

	public void setActualEffort(Integer actualEffort) {
		this.actualEffort = actualEffort;
	}

	@Column(name = "entityGroupId")
	public Integer getEntityGroupId() {
		return entityGroupId;
	}

	public void setEntityGroupId(Integer entityGroupId) {
		this.entityGroupId = entityGroupId;
	}

	@Column(name = "comments")
	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "productId", nullable = true)
	public ProductMaster getProduct() {
		return product;
	}

	public void setProduct(ProductMaster product) {
		this.product = product;
	}

	@Column(name = "slaDurationPlanned")
	public Integer getSlaDurationPlanned() {
		return slaDurationPlanned;
	}

	public void setSlaDurationPlanned(Integer slaDurationPlanned) {
		this.slaDurationPlanned = slaDurationPlanned;
	}

	@Column(name = "slaDurationActual")
	public Integer getSlaDurationActual() {
		return slaDurationActual;
	}

	public void setSlaDurationActual(Integer slaDurationActual) {
		this.slaDurationActual = slaDurationActual;
	}

	@Transient
	public Object getInstanceObject() {
		return instanceObject;
	}

	public void setInstanceObject(Object instanceObject) {
		this.instanceObject = instanceObject;
	}

	@Column(name = "actualSize")
	public Integer getActualSize() {
		return actualSize;
	}

	public void setActualSize(Integer actualSize) {
		this.actualSize = actualSize;
	}

}
