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
@Table(name = "clarification_tracker")
public class ClarificationTracker {
	private Integer clarificationTrackerId;
	private String clarificationTitle;	
	private ClarificationScope clarificationScope;	
	private ClarificationTypeMaster clarificationType;
	private String clarificationDescription;
	private Date raisedDate;
	private UserList raisedBy;
	private ExecutionPriority priority;
	private UserList owner;
	private EntityStatus entityStatus;
	private Integer dependentCR;
	private Integer dependentActivityTracker;
	private EntityMaster entityType;
	private Integer entityInstanceId;
	private Date plannedStartDate;
	private Date plannedEndDate;
	private Date actualStartDate;
	private Date actualEndDate;
	private Integer resolution;	
	private TestFactory testFactory;
	private ProductMaster product;
	private Integer planExpectedValue;
	private Integer achievedValue;
	
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "clarificationTrackerId",unique = true, nullable = false)
	public Integer getClarificationTrackerId() {
		return clarificationTrackerId;
	}
	public void setClarificationTrackerId(Integer clarificationTrackerId) {
		this.clarificationTrackerId = clarificationTrackerId;
	}
	@Column(name = "clarificationTitle")
	public String getClarificationTitle() {
		return clarificationTitle;
	}
	public void setClarificationTitle(String clarificationTitle) {
		this.clarificationTitle = clarificationTitle;
	}
	@Column(name = "clarificationDescription")
	public String getClarificationDescription() {
		return clarificationDescription;
	}
	public void setClarificationDescription(String clarificationDescription) {
		this.clarificationDescription = clarificationDescription;
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
	@JoinColumn (name = "raisedBy")
	public UserList getRaisedBy() {
		return raisedBy;
	}
	public void setRaisedBy(UserList raisedBy) {
		this.raisedBy = raisedBy;
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
	@JoinColumn (name = "statusId")
	public EntityStatus getEntityStatus() {
		return entityStatus;
	}
	
	public void setEntityStatus(EntityStatus entityStatus) {
		this.entityStatus = entityStatus;
	}
	
	@Column(name = "dependentCR")
	public Integer getDependentCR() {
		return dependentCR;
	}
	
	public void setDependentCR(Integer dependentCR) {
		this.dependentCR = dependentCR;
	}
	@Column(name = "dependentActivityTracker")
	public Integer getDependentActivityTracker() {
		return dependentActivityTracker;
	}
	public void setDependentActivityTracker(Integer dependentActivityTracker) {
		this.dependentActivityTracker = dependentActivityTracker;
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
	@Column(name = "resolutionId")
	public Integer getResolution() {
		return resolution;
	}
	public void setResolution(Integer resolution) {
		this.resolution = resolution;
	}
	@ManyToOne (fetch = FetchType.LAZY)
	@JoinColumn (name = "clarificationType")
	public ClarificationTypeMaster getClarificationType() {
		return clarificationType;
	}
	public void setClarificationType(ClarificationTypeMaster clarificationType) {
		this.clarificationType = clarificationType;
	}
	
	@ManyToOne (fetch = FetchType.LAZY)
	@JoinColumn (name = "clarificationScope")
	public ClarificationScope getClarificationScope() {
		return clarificationScope;
	}
	public void setClarificationScope(ClarificationScope clarificationScope) {
		this.clarificationScope = clarificationScope;
	}
	
	@ManyToOne (fetch = FetchType.LAZY)
	@JoinColumn (name = "testFactoryId")
	public TestFactory getTestFactory() {
		return testFactory;
	}
	public void setTestFactory(TestFactory testFactory) {
		this.testFactory = testFactory;
	}
	
	@ManyToOne (fetch = FetchType.LAZY)
	@JoinColumn (name = "productId",nullable=true)
	public ProductMaster getProduct() {
		return product;
	}
	public void setProduct(ProductMaster product) {
		this.product = product;
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
}
