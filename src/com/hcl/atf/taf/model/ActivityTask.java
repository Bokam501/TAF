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

import com.hcl.ilcm.workflow.model.WorkflowStatus;

@Entity
@Table(name = "activity_task")
public class ActivityTask {

	private Integer activityTaskId;
	private String activityTaskName;	
	private Activity activity;
	private ChangeRequest changeRequest;
	private ExecutionTypeMaster category;
	private UserList assignee;
	private UserList reviewer;
	private EnvironmentCombination environmentCombination;
	private Date baselineStartDate;
	private Date baselineEndDate;
	private Date plannedStartDate;
	private Date plannedEndDate;
	private Date actualStartDate;
	private Date actualEndDate;
	private WorkflowStatus status;
	private ActivityTaskType activityTaskType;
	private ActivityResult result;
	private ExecutionPriority priority;
	private String remark;
	private Date createdDate;
	private Date modifiedDate;
	private UserList createdBy;
	private UserList modifiedBy;
	private Integer isActive;
	private Integer baselineEffort;
	private Integer plannedEffort;
	private Integer actualEffort;
    private Integer commentsTracker;
    private ActivitySecondaryStatusMaster secondaryStatus;
    private Integer isReferBack;
    private ActivityEffortTracker activityEffortTracker;
    private Integer baselineTaskSize;
    private Integer plannedTaskSize;
    private Integer actualTaskSize;
    private Integer totalEffort;
    private Float baselineUnit;
    private Float plannedUnit;
    private Float actualUnit;
    private Float percentageCompletion;
    
    private WorkflowStatus lifeCycleStage;
    private ProductMaster productMaster;
    private String complexity;
    
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "activityTaskId",unique = true, nullable = false)
	public Integer getActivityTaskId() {
		return activityTaskId;
	}
	public void setActivityTaskId(Integer activityTaskId) {
		this.activityTaskId = activityTaskId;
	}
	@Column(name = "activityTaskName")
	public String getActivityTaskName() {
		return activityTaskName;
	}
	public void setActivityTaskName(String activityTaskName) {
		this.activityTaskName = activityTaskName;
	}
	@ManyToOne (fetch = FetchType.LAZY)
	@JoinColumn (name = "activityId")
	public Activity getActivity() {
		return activity;
	}
	public void setActivity(Activity activity) {
		this.activity = activity;
	}

	@ManyToOne (fetch = FetchType.LAZY)
	@JoinColumn (name = "changeRequestId")
	public ChangeRequest getChangeRequest() {
		return changeRequest;
	}

	public void setChangeRequest(ChangeRequest changeRequest) {
		this.changeRequest = changeRequest;
	}
	@ManyToOne (fetch = FetchType.LAZY)
	@JoinColumn (name = "categoryId")
	public ExecutionTypeMaster getCategory() {
		return category;
	}
	public void setCategory(ExecutionTypeMaster category) {
		this.category = category;
	}
	@ManyToOne (fetch = FetchType.LAZY)
	@JoinColumn (name = "assigneeId")
	public UserList getAssignee() {
		return assignee;
	}
	public void setAssignee(UserList assignee) {
		this.assignee = assignee;
	}
	@ManyToOne (fetch = FetchType.LAZY)
	@JoinColumn (name = "reviewerId")
	public UserList getReviewer() {
		return reviewer;
	}
	public void setReviewer(UserList reviewer) {
		this.reviewer = reviewer;
		}	
	
	@ManyToOne (fetch = FetchType.LAZY)
	@JoinColumn (name = "environmentCombinationId")
	public EnvironmentCombination getEnvironmentCombination() {
		return environmentCombination;
	}
	public void setEnvironmentCombination(
			EnvironmentCombination environmentCombination) {
		this.environmentCombination = environmentCombination;
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
	@ManyToOne (fetch = FetchType.LAZY)
	@JoinColumn (name = "statusId")
	public WorkflowStatus getStatus() {
		return status;
	}
	public void setStatus(WorkflowStatus status) {
		this.status = status;
	}
	
	@ManyToOne (fetch = FetchType.LAZY)
	@JoinColumn (name = "activityTaskTypeId")
	public ActivityTaskType getActivityTaskType() {
		return activityTaskType;
	}
	public void setActivityTaskType(ActivityTaskType activityTaskType) {
		this.activityTaskType = activityTaskType;
	}
	
	
	@ManyToOne (fetch = FetchType.LAZY)
	@JoinColumn (name = "resultId")
	public ActivityResult getResult() {
		return result;
	}
	public void setResult(ActivityResult result) {
		this.result = result;
	}
	@ManyToOne (fetch = FetchType.LAZY)
	@JoinColumn (name = "priorityId")
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
	@ManyToOne (fetch = FetchType.LAZY)
	@JoinColumn (name = "createdBy")
	public UserList getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(UserList createdBy) {
		this.createdBy = createdBy;
	}
	@ManyToOne (fetch = FetchType.LAZY)
	@JoinColumn (name = "modifiedBy")
	public UserList getModifiedBy() {
		return modifiedBy;
	}
	public void setModifiedBy(UserList modifiedBy) {
		this.modifiedBy = modifiedBy;
	}
	
	@Column(name = "isActive")
	public Integer getIsActive() {
		return isActive;
	}
	public void setIsActive(Integer isActive) {
		this.isActive = isActive;
	}
	
	@Column(name = "baselineEffort")
	public Integer getBaselineEffort() {
		return baselineEffort;
	}
	public void setBaselineEffort(Integer baselineEffort) {
		this.baselineEffort = baselineEffort;
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
	
	@ManyToOne (fetch = FetchType.LAZY)
	@JoinColumn (name = "secondaryStatusId")
	public ActivitySecondaryStatusMaster getSecondaryStatus() {
		return secondaryStatus;
	}
	public void setSecondaryStatus(ActivitySecondaryStatusMaster secondaryStatus) {
		this.secondaryStatus = secondaryStatus;
	}

	@Column(name = "totalEffort")
	public Integer getTotalEffort() {
		return totalEffort;
	}
	public void setTotalEffort(Integer totalEffort) {
		this.totalEffort = totalEffort;
	}

	@Column(name = "baselineTaskSize")
	public Integer getBaselineTaskSize() {
		return baselineTaskSize;
	}
	public void setBaselineTaskSize(Integer baselineTaskSize) {
		this.baselineTaskSize = baselineTaskSize;
	}
	
	@Column(name = "plannedTaskSize")
	public Integer getPlannedTaskSize() {
		return plannedTaskSize;
	}
	public void setPlannedTaskSize(Integer plannedTaskSize) {
		this.plannedTaskSize = plannedTaskSize;
	}
	
	@Column(name = "actualTaskSize")
	public Integer getActualTaskSize() {
		return actualTaskSize;
	}
	public void setActualTaskSize(Integer actualTaskSize) {
		this.actualTaskSize = actualTaskSize;
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
		
	@Column(name = "complexity")
	public String getComplexity() {
		return complexity;
	}

	public void setComplexity(String complexity) {
		this.complexity = complexity;
	}
}