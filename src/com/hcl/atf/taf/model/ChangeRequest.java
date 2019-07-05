package com.hcl.atf.taf.model;

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


@Entity
@Table(name = "change_request")
public class ChangeRequest {
	private Integer changeRequestId;
	private String changeRequestName;
	private String description;
	private Date raisedDate;
	private ExecutionPriority priority;
	private UserList owner;
	private StatusCategory statusCategory;
	private ProductMaster product;	
	private Date modifiedDate;
	private Integer isActive;
	private ChangeRequestType crType;
	private Integer planExpectedValue;
	private Integer achievedValue;
	private EntityMaster entityType;
	private Integer entityInstanceId;
	
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "changeRequestId",unique = true, nullable = false)
	public Integer getChangeRequestId() {
		return changeRequestId;
	}
	
	public void setChangeRequestId(Integer changeRequestId) {
		this.changeRequestId = changeRequestId;
	}
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "raisedDate")
	public Date getRaisedDate() {
		return raisedDate;
	}
	public void setRaisedDate(Date raisedDate) {
		this.raisedDate = raisedDate;
	}
	@ManyToOne (fetch = FetchType.LAZY)
	@JoinColumn (name = "priorityId")
	public ExecutionPriority getPriority() {
		return priority;
	}
	public void setPriority(ExecutionPriority priority) {
		this.priority = priority;
	}
	@ManyToOne (fetch = FetchType.LAZY)
	@JoinColumn (name = "ownerId")
	public UserList getOwner() {
		return owner;
	}
	public void setOwner(UserList owner) {
		
		this.owner = owner;
	}
	
	@ManyToOne (fetch = FetchType.LAZY)
	@JoinColumn (name = "statusCategoryId")
	public StatusCategory getStatusCategory() {
		return statusCategory;
	}

	public void setStatusCategory(StatusCategory statusCategory) {
		this.statusCategory = statusCategory;
	}	
	
	@Column(name = "changeRequestName")
	public String getChangeRequestName() {
		return changeRequestName;
	}
	public void setChangeRequestName(String changeRequestName) {
		this.changeRequestName = changeRequestName;
	}
	@Column(name = "description")
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}

	@ManyToOne (fetch = FetchType.LAZY)
	@JoinColumn (name = "productId")
	public ProductMaster getProduct() {
		return product;
	}

	public void setProduct(ProductMaster product) {
		this.product = product;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "modifiedDate")
	public Date getModifiedDate() {
		return modifiedDate;
	}

	public void setModifiedDate(Date modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

	@Column(name = "isActive")
	public Integer getIsActive() {
		return isActive;
	}

	public void setIsActive(Integer isActive) {
		this.isActive = isActive;
	}
	
	@ManyToOne (fetch = FetchType.LAZY)
	@JoinColumn (name = "changeRequestType")
	public ChangeRequestType getCrType() {
		return crType;
	}

	public void setCrType(ChangeRequestType crType) {
		this.crType = crType;
	}
	
	@Column(name = "planExpectedValue")
	public Integer getPlanExpectedValue() {
		return planExpectedValue;
	}
	public void setPlanExpectedValue(Integer planExpectedValue) {
		this.planExpectedValue = planExpectedValue;
	}
	
	@Column(name = "achievedValue")
	public Integer getAchievedValue() {
		return achievedValue;
	}
	public void setAchievedValue(Integer achievedValue) {
		this.achievedValue = achievedValue;
	}
	
	@ManyToOne (fetch = FetchType.LAZY)
	@JoinColumn (name = "entityTypeId")
	public EntityMaster getEntityType() {
		return entityType;
	}
	public void setEntityType(EntityMaster entityType) {
		this.entityType = entityType;
	}
	@Column(name = "entityInstanceId")
	public Integer getEntityInstanceId() {
		return entityInstanceId;
	}
	public void setEntityInstanceId(Integer entityInstanceId) {
		this.entityInstanceId = entityInstanceId;
	}
}
