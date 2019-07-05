package com.hcl.atf.taf.model;

import static javax.persistence.GenerationType.IDENTITY;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.hcl.atf.taf.model.ActivityWorkPackage;
import com.hcl.atf.taf.model.StatusCategory;
import com.hcl.ilcm.workflow.model.WorkflowStatus;


@Entity
@Table(name = "activity")
public class Activity implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer activityId;	
	private String activityName;
	private ActivityWorkPackage activityWorkPackage;
	private ProductFeature productFeature;
	private ActivityMaster activityMaster;
    private ClarificationTracker clarificationTracker;
	private String activityTrackerNumber;
	private Integer drReferenceNumber;
	private ExecutionTypeMaster category;
	private UserList assignee;
	private UserList reviewer;
	private Integer autoAllocateReferenceId;
	private StatusCategory statusCategory;
	private ExecutionPriority priority;
	private String remark;
	private Date baselineStartDate;
	private Date baselineEndDate;
	private Date plannedStartDate;
	private Date plannedEndDate;
	private Date actualStartDate;
	private Date actualEndDate;
	private Date createdDate;
	private Date modifiedDate;
	private UserList createdBy;
	private UserList modifiedBy;
	private Integer isActive;
	
	private Integer baselineActivitySize;
	private Integer plannedActivitySize;
	private Integer actualActivitySize;
	
	private Set<EnvironmentCombination> environmentCombination = new HashSet<EnvironmentCombination>(0);
	private Set<ActivityTask> activityTask = new HashSet<ActivityTask>(0);
	
	private Float baselineUnit;
	private Float plannedUnit;
    private Float actualUnit;
    
    private WorkflowStatus workflowStatus;
    private Integer totalEffort;
    
    private WorkflowStatus lifeCycleStage;
    private Float percentageCompletion;
    
    private ProductMaster productMaster;
    private Integer baselineEffort;
    private Integer plannedEffort;
    private String complexity;
    
    private String activityPredecessors;
	
    @Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "activityId", unique = true, nullable = false)
	public Integer getActivityId() {
		return activityId;
	}

	public void setActivityId(Integer activityId) {
		this.activityId = activityId;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "activityWorkPackageId")
	public ActivityWorkPackage getActivityWorkPackage() {
		return activityWorkPackage;
	}

	public void setActivityWorkPackage(ActivityWorkPackage activityWorkPackage) {
		this.activityWorkPackage = activityWorkPackage;
	}
	

	@Column(name = "activityTrackerNumber")
	public String getActivityTrackerNumber() {
		return activityTrackerNumber;
	}
	public void setActivityTrackerNumber(String activityTrackerNumber) {
		this.activityTrackerNumber = activityTrackerNumber;
	}

	@Column(name = "drReferenceNumber")
	public Integer getDrReferenceNumber() {
		return drReferenceNumber;
	}

	public void setDrReferenceNumber(Integer drReferenceNumber) {
		this.drReferenceNumber = drReferenceNumber;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "assigneeId")
	public UserList getAssignee() {
		return assignee;
	}

	public void setAssignee(UserList assignee) {
		this.assignee = assignee;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "reviewerId")
	public UserList getReviewer() {
		return reviewer;
	}

	public void setReviewer(UserList reviewer) {
		this.reviewer = reviewer;
	}

	@Column(name = "autoAllocateReferenceId")
	public Integer getAutoAllocateReferenceId() {
		return autoAllocateReferenceId;
	}

	public void setAutoAllocateReferenceId(Integer autoAllocateReferenceId) {
		this.autoAllocateReferenceId = autoAllocateReferenceId;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "statusCategoryId")
	public StatusCategory getStatusCategory() {
		return statusCategory;
	}

	public void setStatusCategory(StatusCategory statusCategory) {
		this.statusCategory = statusCategory;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "priorityId")
	public ExecutionPriority getPriority() {
		return priority;
	}

	public void setPriority(ExecutionPriority priority) {
		this.priority = priority;
	}

	@Column(name = "remark")
	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	@Column(name = "baselineStartDate")
	public Date getBaselineStartDate() {
		return baselineStartDate;
	}

	public void setBaselineStartDate(Date baselineStartDate) {
		this.baselineStartDate = baselineStartDate;
	}

	@Column(name = "baselineEndDate")
	public Date getBaselineEndDate() {
		return baselineEndDate;
	}

	public void setBaselineEndDate(Date baselineEndDate) {
		this.baselineEndDate = baselineEndDate;
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

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "createdDate")
	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "modifiedDate")
	public Date getModifiedDate() {
		return modifiedDate;
	}

	public void setModifiedDate(Date modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "createdBy")
	public UserList getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(UserList createdBy) {
		this.createdBy = createdBy;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "modifiedBy")
	public UserList getModifiedBy() {
		return modifiedBy;
	}

	public void setModifiedBy(UserList modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "categoryId")
	public ExecutionTypeMaster getCategory() {
		return category;
	}

	public void setCategory(ExecutionTypeMaster category) {
		this.category = category;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "productFeatureId")
	public ProductFeature getProductFeature() {
		return productFeature;
	}

	public void setProductFeature(ProductFeature productFeature) {
		this.productFeature = productFeature;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "activityMasterId")
	public ActivityMaster getActivityMaster() {
		return activityMaster;
	}

	public void setActivityMaster(ActivityMaster activityMaster) {
		this.activityMaster = activityMaster;
	}

	@Column(name = "isActive")
	public Integer getIsActive() {
		return isActive;
	}

	public void setIsActive(Integer isActive) {
		this.isActive = isActive;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "clarificationTrackerId")
	public ClarificationTracker getClarificationTracker() {
		return clarificationTracker;
	}

	public void setClarificationTracker(
			ClarificationTracker clarificationTracker) {
		this.clarificationTracker = clarificationTracker;
	}
	@Column(name = "activityName")
	public String getActivityName() {
		return activityName;
	}

	public void setActivityName(String activityName) {
		this.activityName = activityName;
	}

	@ManyToMany(fetch = FetchType.LAZY, mappedBy = "activitySet",cascade=CascadeType.ALL)
	public Set<EnvironmentCombination> getEnvironmentCombination() {
		return environmentCombination;
	}

	public void setEnvironmentCombination(
			Set<EnvironmentCombination> environmentCombination) {
		this.environmentCombination = environmentCombination;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "activity",cascade=CascadeType.ALL)
	public Set<ActivityTask> getActivityTask() {
		return activityTask;
	}

	public void setActivityTask(Set<ActivityTask> activityTask) {
		this.activityTask = activityTask;
	}

	@Override
	public boolean equals(Object o) {
		
			Activity activity = (Activity) o;
			if (this.activityId == activity.getActivityId()){
				return true;
			}else{
				return false;
			}
		
		
	}
	
	@Override
	public int hashCode(){
	    return (int) activityId;
	 }
	
	@Column(name = "baselineActivitySize")
	public Integer getBaselineActivitySize() {
		return baselineActivitySize;
	}

	public void setBaselineActivitySize(Integer baselineActivitySize) {
		this.baselineActivitySize = baselineActivitySize;
	}
	
	@Column(name = "plannedActivitySize")
	public Integer getPlannedActivitySize() {
		return plannedActivitySize;
	}

	public void setPlannedActivitySize(Integer plannedActivitySize) {
		this.plannedActivitySize = plannedActivitySize;
	}

	@Column(name = "actualActivitySize")
	public Integer getActualActivitySize() {
		return actualActivitySize;
	}

	public void setActualActivitySize(Integer actualActivitySize) {
		this.actualActivitySize = actualActivitySize;
	}
	
	@Column(name = "baselineUnit")
	public Float getBaselineUnit() {
		return baselineUnit;
	}

	public void setBaselineUnit(Float baselineUnit) {
		this.baselineUnit = baselineUnit;
	}

	@Column(name = "plannedUnit")
	public Float getPlannedUnit() {
		return plannedUnit;
	}
	
	public void setPlannedUnit(Float plannedUnit) {
		this.plannedUnit = plannedUnit;
	}
		
	@Column(name = "actualUnit")
	public Float getActualUnit() {
		return actualUnit;
	}
	
	public void setActualUnit(Float actualUnit) {
		this.actualUnit = actualUnit;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "workflowStatusId")
	public WorkflowStatus getWorkflowStatus() {
		return workflowStatus;
	}

	public void setWorkflowStatus(WorkflowStatus workflowStatus) {
		this.workflowStatus = workflowStatus;
	}

	@Column(name = "totalEfforts")
	public Integer getTotalEffort() {
		return totalEffort;
	}

	public void setTotalEffort(Integer totalEffort) {
		this.totalEffort = totalEffort;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "lifeCycleStage", nullable = true)
	public WorkflowStatus getLifeCycleStage() {
		return lifeCycleStage;
	}

	public void setLifeCycleStage(WorkflowStatus lifeCycleStage) {
		this.lifeCycleStage = lifeCycleStage;
	}

	@Column(name = "percentageCompletion")
	public Float getPercentageCompletion() {
		return percentageCompletion;
	}

	public void setPercentageCompletion(Float percentageCompletion) {
		this.percentageCompletion = percentageCompletion;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "productId")
	public ProductMaster getProductMaster() {
		return productMaster;
	}

	public void setProductMaster(ProductMaster productMaster) {
		this.productMaster = productMaster;
	}

	
	@Column(name = "baselineEfforts")
	public Integer getBaselineEffort() {
		return baselineEffort;
	}

	public void setBaselineEffort(Integer baselineEffort) {
		this.baselineEffort = baselineEffort;
	}

	@Column(name = "plannedEfforts")
	public Integer getPlannedEffort() {
		return plannedEffort;
	}

	public void setPlannedEffort(Integer plannedEffort) {
		this.plannedEffort = plannedEffort;
	}

	@Column(name = "complexity")
	public String getComplexity() {
		return complexity;
	}

	public void setComplexity(String complexity) {
		this.complexity = complexity;
	}

	@Column(name = "predecessors")
	public String getActivityPredecessors() {
		return activityPredecessors;
	}

	public void setActivityPredecessors(String activityPredecessors) {
		this.activityPredecessors = activityPredecessors;
	}
	
	
}
