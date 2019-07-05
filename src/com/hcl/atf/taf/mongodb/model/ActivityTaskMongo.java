package com.hcl.atf.taf.mongodb.model;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.hcl.atf.taf.model.ActivityTask;
@Document(collection = "activitytasks")
public class ActivityTaskMongo {

	@Id
	private String id;
	private String _class;
	private Integer activityTaskId;	
	private String activityTaskName;		
	private Integer activityId;	
	private String activityName;	
	
	private Integer activityWorkPackageId;	
	private Integer productId;		
	private Integer versionId;	
	private Integer buildId;	
	private String activityWorkPackageName;	
	private String productName;		
	private String versionName;	
	private String buildname;
	private Integer testFactoryId;	
	private String  testFactoryName;	
	private Integer testCentersId;
	private String testCentersName;

	
	private Integer customerId;	
	private String  customerName;
	private Integer activityTypeId;	
	private String activityTypeName;	
	private Integer changeRequestId;	
	private Integer categoryId;	
	private String categoryName;	
	private Integer assigneeId;	
	private String assigneeName;	
	private Integer reviewerId;	
	private String reviewerName;	
	private Integer statusId;	
	private String statusName;
	
	private Integer activityTaskTypeId;	
	private String activityTaskTypeName;	
	private Integer priorityId;	
	private String priorityName;	
	private Integer environmentCombinationId;	
	private String environmentCombinationName;	
	private Date baselineStartDate;	
	private Date baselineEndDate;
	private Date plannedStartDate;	
	private Date plannedEndDate;
	private Date actualStartDate;	
	private Date actualEndDate;
	private Integer resultId;	
	private String resultName;
	private String remark;	
	
	private Integer createdById;	
	private String createdByName;	
	 
	private String status;	

	private Integer baselineEffort;
	private Integer plannedEffort;
	private Integer actualEffort;	
	
	private Integer baselineTaskSize;
	private Integer plannedTaskSize;
    private Integer actualTaskSize;
    private Float baselineUnit;
    private Float plannedUnit;
    private Float actualUnit;

	private Integer competencyId;
	private String competencyName;
	
	private  Integer possibleSavings ;//plannedTaskSize
	private Integer estimatedSavings;//plannedEffort
	private Integer actualSavings;//actualTaskSize
	
	private Integer workflowStatusId;
	private String workflowStatusName;
	private Integer secondaryStatusId;
	private String secondaryStatusName;
	private Integer totalEffort;
	private Integer lifeCycleStageId;
	private String lifeCycleStageName;
	private Float percentageCompletion;
	private String parentStatus;
	private Date createdDate;	
	private Date modifiedDate;	
	
	
public ActivityTaskMongo(){
		
	}	
public ActivityTaskMongo(ActivityTask activityTask, Integer productId, String productName, Integer productBuildVersionId, String productBuildVersionName, Integer productBuildId, String productBuildName, Integer activityWorkPackageId, String activityWorkPackageName,Integer testFactoryId, String testFactoryName, Integer customerId, String customerName) {
	this.id=activityTask.getActivityTaskId()+"";
	this._class=this.getClass().getCanonicalName().replace("\\", ".").replace("/", ".");
	this.activityTaskId=activityTask.getActivityTaskId();	
	this.activityTaskName=activityTask.getActivityTaskName();
	this.activityId=activityTask.getActivity().getActivityId();	
	if(activityTask.getCategory()!=null){
	this.categoryId=activityTask.getCategory().getExecutionTypeId();
	this.categoryName=activityTask.getCategory().getName();
	}
	this.assigneeId=activityTask.getAssignee().getUserId();
	this.assigneeName=activityTask.getAssignee().getLoginId();
	this.reviewerId=activityTask.getReviewer().getUserId();
	this.reviewerName=activityTask.getReviewer().getLoginId();
	if(activityTask.getStatus()!=null){
		this.statusId=activityTask.getStatus().getWorkflowStatusId();
		this.statusName=activityTask.getStatus().getWorkflowStatusDisplayName();
	}
	if(activityTask.getActivityTaskType()!=null){
		this.activityTaskTypeId=activityTask.getActivityTaskType().getActivityTaskTypeId();
		this.activityTaskTypeName=activityTask.getActivityTaskType().getActivityTaskTypeName();
	}
	if(activityTask.getPriority()!=null){
		this.priorityId=activityTask.getPriority().getExecutionPriorityId();
		this.priorityName=activityTask.getPriority().getExecutionPriorityName();
	}
	this.plannedStartDate=activityTask.getPlannedStartDate();
	this.plannedEndDate=activityTask.getPlannedEndDate();
	this.baselineStartDate=activityTask.getBaselineStartDate();
	this.baselineEndDate=activityTask.getBaselineEndDate();
	this.actualStartDate=activityTask.getActualStartDate();
	this.actualEndDate=activityTask.getActualEndDate();
	if(activityTask.getResult()!=null){
		this.resultId=activityTask.getResult().getActivityResultId();
		this.resultName=activityTask.getResult().getActivityResultName();
	}
	this.remark=activityTask.getRemark();
	this.createdDate=activityTask.getCreatedDate();
	this.modifiedDate=activityTask.getModifiedDate();
	if(activityTask.getIsActive()==1){
		this.status="Active";
	}else{
		this.status="InActive";
	}
	this.baselineEffort = activityTask.getBaselineEffort();
	this.plannedEffort=activityTask.getPlannedEffort();
	this.actualEffort=activityTask.getActualEffort();
	
	if(activityTask.getActivity().getActivityWorkPackage()!=null){
			this.activityWorkPackageId=activityTask.getActivity().getActivityWorkPackage().getActivityWorkPackageId();
			this.activityWorkPackageName=activityTask.getActivity().getActivityWorkPackage().getActivityWorkPackageName();
			
			if(activityTask.getActivity().getActivityWorkPackage().getProductBuild()!=null && activityTask.getActivity().getActivityWorkPackage().getProductBuild().getProductVersion()!=null && activityTask.getActivity().getActivityWorkPackage().getProductBuild().getProductVersion().getProductMaster()!=null){
				this.productId=activityTask.getActivity().getActivityWorkPackage().getProductBuild().getProductVersion().getProductMaster().getProductId();
				this.productName=activityTask.getActivity().getActivityWorkPackage().getProductBuild().getProductVersion().getProductMaster().getProductName();
			
				if(activityTask.getActivity().getActivityWorkPackage().getProductBuild().getProductVersion().getProductMaster().getTestFactory()!=null){
					this.testFactoryId=activityTask.getActivity().getActivityWorkPackage().getProductBuild().getProductVersion().getProductMaster().getTestFactory().getTestFactoryId();
					this.testFactoryName=activityTask.getActivity().getActivityWorkPackage().getProductBuild().getProductVersion().getProductMaster().getTestFactory().getTestFactoryName();
					this.testCentersId=activityTask.getActivity().getActivityWorkPackage().getProductBuild().getProductVersion().getProductMaster().getTestFactory().getTestFactoryLab().getTestFactoryLabId();
					this.testCentersName=activityTask.getActivity().getActivityWorkPackage().getProductBuild().getProductVersion().getProductMaster().getTestFactory().getTestFactoryLab().getTestFactoryLabName();
					
				}
				
					if(activityTask.getActivity().getActivityWorkPackage().getProductBuild().getProductVersion().getProductMaster().getCustomer()!=null){
						this.customerId=activityTask.getActivity().getActivityWorkPackage().getProductBuild().getProductVersion().getProductMaster().getCustomer().getCustomerId();
						this.customerName=activityTask.getActivity().getActivityWorkPackage().getProductBuild().getProductVersion().getProductMaster().getCustomer().getCustomerName();
					}
			}
			
			if(activityTask.getActivity().getActivityWorkPackage().getProductBuild()!=null){
				this.buildId=activityTask.getActivity().getActivityWorkPackage().getProductBuild().getProductBuildId();
				this.buildname=activityTask.getActivity().getActivityWorkPackage().getProductBuild().getBuildname();
				if(activityTask.getActivity().getActivityWorkPackage().getProductBuild().getProductVersion()!=null){
					this.versionId=activityTask.getActivity().getActivityWorkPackage().getProductBuild().getProductVersion().getProductVersionListId();
					this.versionName=activityTask.getActivity().getActivityWorkPackage().getProductBuild().getProductVersion().getProductVersionName();
					
				}
			}
			
			if(activityTask.getActivity().getActivityWorkPackage().getCompetency()!=null && activityTask.getActivity().getActivityWorkPackage().getCompetency().getDimensionId() > 1){
				this.competencyId = activityTask.getActivity().getActivityWorkPackage().getCompetency().getDimensionId();
				this.competencyName = activityTask.getActivity().getActivityWorkPackage().getCompetency().getName();
			}
		
	}
	
	this.baselineTaskSize = activityTask.getBaselineTaskSize();
	this.plannedTaskSize = activityTask.getPlannedTaskSize();
	this.actualTaskSize = activityTask.getActualTaskSize();
	this.baselineUnit = activityTask.getBaselineUnit();
	this.plannedUnit = activityTask.getPlannedUnit();
	this.actualUnit = activityTask.getActualUnit();
	
	
	
	this.possibleSavings = activityTask.getPlannedTaskSize();
	this.actualSavings = activityTask.getActualTaskSize();
	this.estimatedSavings=activityTask.getPlannedEffort();
	
	
	if(activityTask.getLifeCycleStage() != null){
		this.lifeCycleStageId = activityTask.getLifeCycleStage().getWorkflowStatusId();
		this.lifeCycleStageName = activityTask.getLifeCycleStage().getWorkflowStatusName();
	}	
	
	
}

public ActivityTaskMongo(ActivityTask activityTask) {
	this.id=activityTask.getActivityTaskId()+"";
	this._class=this.getClass().getCanonicalName().replace("\\", ".").replace("/", ".");
	this.activityTaskId=activityTask.getActivityTaskId();	
	this.activityTaskName=activityTask.getActivityTaskName();
	this.totalEffort=activityTask.getTotalEffort();
	
	if(activityTask.getActivity()!=null){
		this.activityId=activityTask.getActivity().getActivityId();
		this.activityName=activityTask.getActivity().getActivityName();
		if(activityTask.getActivity().getIsActive() == 1){
			this.parentStatus = "Active";
		}else{
			this.parentStatus = "InActive";
		}
	}
	
	if(activityTask.getChangeRequest()!=null){
		this.changeRequestId=activityTask.getChangeRequest().getChangeRequestId();
	}
	
	if(activityTask.getCategory()!=null){
	this.categoryId=activityTask.getCategory().getExecutionTypeId();
	this.categoryName=activityTask.getCategory().getName();
	}
	
	if(activityTask.getAssignee()!=null){
		this.assigneeId=activityTask.getAssignee().getUserId();
		this.assigneeName=activityTask.getAssignee().getLoginId();
	}
	if(activityTask.getReviewer()!=null){
		this.reviewerId=activityTask.getReviewer().getUserId();
		this.reviewerName=activityTask.getReviewer().getLoginId();
	}
	
	if(activityTask.getSecondaryStatus()!=null){
		this.secondaryStatusId=activityTask.getSecondaryStatus().getActivitySecondaryStatusId();
		this.secondaryStatusName=activityTask.getSecondaryStatus().getActivitySecondaryStatusName();
	}
	
	if(activityTask.getStatus()!=null){
		this.statusId=activityTask.getStatus().getWorkflowStatusId();
		this.statusName=activityTask.getStatus().getWorkflowStatusDisplayName();
	}
	if(activityTask.getActivityTaskType()!=null){
		this.activityTaskTypeId=activityTask.getActivityTaskType().getActivityTaskTypeId();
		this.activityTaskTypeName=activityTask.getActivityTaskType().getActivityTaskTypeName();
	}
	if(activityTask.getPriority()!=null){
		this.priorityId=activityTask.getPriority().getExecutionPriorityId();
		this.priorityName=activityTask.getPriority().getExecutionPriorityName();
	}
	if(activityTask.getEnvironmentCombination()!=null){
		this.environmentCombinationId=activityTask.getEnvironmentCombination().getEnvironment_combination_id();
		this.environmentCombinationName=activityTask.getEnvironmentCombination().getEnvironmentCombinationName();
	}
    
	this.percentageCompletion=activityTask.getPercentageCompletion();
	this.baselineStartDate=activityTask.getBaselineStartDate();
	this.baselineEndDate=activityTask.getBaselineEndDate();
	this.plannedStartDate=activityTask.getPlannedStartDate();
	this.plannedEndDate=activityTask.getPlannedEndDate();
	this.actualStartDate=activityTask.getActualStartDate();
	this.actualEndDate=activityTask.getActualEndDate();
	if(activityTask.getResult()!=null){
		this.resultId=activityTask.getResult().getActivityResultId();
		this.resultName=activityTask.getResult().getActivityResultName();
	}
	this.remark=activityTask.getRemark();
	this.createdDate=activityTask.getCreatedDate();
	this.modifiedDate=activityTask.getModifiedDate();
	
	if(activityTask.getCreatedBy()!=null){
		this.createdById=activityTask.getCreatedBy().getUserId();
		this.createdByName=activityTask.getCreatedBy().getLoginId();
	}
	
	
	if(activityTask.getIsActive()==1){
		this.status="Active";
	}else{
		this.status="InActive";
	}
	this.baselineEffort=activityTask.getBaselineEffort();
	this.plannedEffort=activityTask.getPlannedEffort();
	this.actualEffort=activityTask.getActualEffort();
	if(activityTask.getActivity()!=null){
		if(activityTask.getActivity().getActivityWorkPackage()!=null){
			this.activityWorkPackageId=activityTask.getActivity().getActivityWorkPackage().getActivityWorkPackageId();
			this.activityWorkPackageName=activityTask.getActivity().getActivityWorkPackage().getActivityWorkPackageName();
			
			if(activityTask.getActivity().getActivityWorkPackage().getProductBuild()!=null && activityTask.getActivity().getActivityWorkPackage().getProductBuild().getProductVersion()!=null && activityTask.getActivity().getActivityWorkPackage().getProductBuild().getProductVersion().getProductMaster()!=null){
				this.productId=activityTask.getActivity().getActivityWorkPackage().getProductBuild().getProductVersion().getProductMaster().getProductId();
				this.productName=activityTask.getActivity().getActivityWorkPackage().getProductBuild().getProductVersion().getProductMaster().getProductName();
			
				if(activityTask.getActivity().getActivityWorkPackage().getProductBuild().getProductVersion().getProductMaster().getTestFactory()!=null){
					this.testFactoryId=activityTask.getActivity().getActivityWorkPackage().getProductBuild().getProductVersion().getProductMaster().getTestFactory().getTestFactoryId();
					this.testFactoryName=activityTask.getActivity().getActivityWorkPackage().getProductBuild().getProductVersion().getProductMaster().getTestFactory().getTestFactoryName();
					
				}
				
					if(activityTask.getActivity().getActivityWorkPackage().getProductBuild().getProductVersion().getProductMaster().getCustomer()!=null){
						this.customerId=activityTask.getActivity().getActivityWorkPackage().getProductBuild().getProductVersion().getProductMaster().getCustomer().getCustomerId();
						this.customerName=activityTask.getActivity().getActivityWorkPackage().getProductBuild().getProductVersion().getProductMaster().getCustomer().getCustomerName();
					}
			}
			
			if(activityTask.getActivity().getActivityWorkPackage().getProductBuild()!=null){
				this.buildId=activityTask.getActivity().getActivityWorkPackage().getProductBuild().getProductBuildId();
				this.buildname=activityTask.getActivity().getActivityWorkPackage().getProductBuild().getBuildname();
				if(activityTask.getActivity().getActivityWorkPackage().getProductBuild().getProductVersion()!=null){
					this.versionId=activityTask.getActivity().getActivityWorkPackage().getProductBuild().getProductVersion().getProductVersionListId();
					this.versionName=activityTask.getActivity().getActivityWorkPackage().getProductBuild().getProductVersion().getProductVersionName();
					
				}
			}
		
			if(activityTask.getActivity().getActivityWorkPackage().getCompetency()!=null){
				this.competencyId = activityTask.getActivity().getActivityWorkPackage().getCompetency().getDimensionId();
				this.competencyName = activityTask.getActivity().getActivityWorkPackage().getCompetency().getName();
			}
			
			if(activityTask.getActivity().getWorkflowStatus()!=null){
				this.workflowStatusId=activityTask.getActivity().getWorkflowStatus().getWorkflowStatusId();
				this.workflowStatusName=activityTask.getActivity().getWorkflowStatus().getWorkflowStatusName();
			}
		}
	
	}
	if(activityTask.getLifeCycleStage() != null){
		this.lifeCycleStageId = activityTask.getLifeCycleStage().getWorkflowStatusId();
		this.lifeCycleStageName = activityTask.getLifeCycleStage().getWorkflowStatusName();
	}	
	
	this.baselineTaskSize = activityTask.getBaselineTaskSize();
	this.plannedTaskSize = activityTask.getPlannedTaskSize();
	this.actualTaskSize = activityTask.getActualTaskSize();
	this.baselineUnit = activityTask.getBaselineUnit();
	this.plannedUnit = activityTask.getPlannedUnit();
	this.actualUnit = activityTask.getActualUnit();

	this.possibleSavings = activityTask.getPlannedTaskSize();
	this.actualSavings = activityTask.getActualTaskSize();
	this.estimatedSavings=activityTask.getPlannedEffort();
	
	
}

public Integer getActivityTaskId() {
	return activityTaskId;
}

public void setActivityTaskId(Integer activityTaskId) {
	this.activityTaskId = activityTaskId;
}

public String getActivityTaskName() {
	return activityTaskName;
}

public void setActivityTaskName(String activityTaskName) {
	this.activityTaskName = activityTaskName;
}

public Integer getActivityId() {
	return activityId;
}

public void setActivityId(Integer activityId) {
	this.activityId = activityId;
}

public Integer getActivityTypeId() {
	return activityTypeId;
}

public void setActivityTypeId(Integer activityTypeId) {
	this.activityTypeId = activityTypeId;
}

public String getActivityTypeName() {
	return activityTypeName;
}

public void setActivityTypeName(String activityTypeName) {
	this.activityTypeName = activityTypeName;
}

public Integer getChangeRequestId() {
	return changeRequestId;
}

public void setChangeRequestId(Integer changeRequestId) {
	this.changeRequestId = changeRequestId;
}

public Integer getCategoryId() {
	return categoryId;
}

public void setCategoryId(Integer categoryId) {
	this.categoryId = categoryId;
}

public String getCategoryName() {
	return categoryName;
}

public void setCategoryName(String categoryName) {
	this.categoryName = categoryName;
}

public Integer getAssigneeId() {
	return assigneeId;
}

public void setAssigneeId(Integer assigneeId) {
	this.assigneeId = assigneeId;
}

public String getAssigneeName() {
	return assigneeName;
}

public void setAssigneeName(String assigneeName) {
	this.assigneeName = assigneeName;
}

public Integer getReviewerId() {
	return reviewerId;
}

public void setReviewerId(Integer reviewerId) {
	this.reviewerId = reviewerId;
}

public String getReviewerName() {
	return reviewerName;
}

public void setReviewerName(String reviewerName) {
	this.reviewerName = reviewerName;
}

public Integer getStatusId() {
	return statusId;
}

public void setStatusId(Integer statusId) {
	this.statusId = statusId;
}

public String getStatusName() {
	return statusName;
}

public void setStatusName(String statusName) {
	this.statusName = statusName;
}

public Integer getActivityTaskTypeId() {
	return activityTaskTypeId;
}

public void setActivityTaskTypeId(Integer activityTaskTypeId) {
	this.activityTaskTypeId = activityTaskTypeId;
}

public String getActivityTaskTypeName() {
	return activityTaskTypeName;
}

public void setActivityTaskTypeName(String activityTaskTypeName) {
	this.activityTaskTypeName = activityTaskTypeName;
}

public Integer getPriorityId() {
	return priorityId;
}

public void setPriorityId(Integer priorityId) {
	this.priorityId = priorityId;
}

public String getPriorityName() {
	return priorityName;
}

public void setPriorityName(String priorityName) {
	this.priorityName = priorityName;
}

public Integer getEnvironmentCombinationId() {
	return environmentCombinationId;
}

public void setEnviromentCombinationId(Integer environmentCombinationId) {
	this.environmentCombinationId = environmentCombinationId;
}

public String getEnvironmentCombinationName() {
	return environmentCombinationName;
}

public void setEnvironmentCombinationName(String enviromentCombinationName) {
	this.environmentCombinationName = enviromentCombinationName;
}

public Date getPlannedStartDate() {
	return plannedStartDate;
}

public void setPlannedStartDate(Date plannedStartDate) {
	this.plannedStartDate = plannedStartDate;
}

public Date getPlannedEndDate() {
	return plannedEndDate;
}

public void setPlannedEndDate(Date plannedEndDate) {
	this.plannedEndDate = plannedEndDate;
}

public Date getBaselineStartDate() {
	return baselineStartDate;
}
public void setBaselineStartDate(Date baselineStartDate) {
	this.baselineStartDate = baselineStartDate;
}
public Date getBaselineEndDate() {
	return baselineEndDate;
}
public void setBaselineEndDate(Date baselineEndDate) {
	this.baselineEndDate = baselineEndDate;
}
public Date getActualStartDate() {
	return actualStartDate;
}

public void setActualStartDate(Date actualStartDate) {
	this.actualStartDate = actualStartDate;
}

public Date getActualEndDate() {
	return actualEndDate;
}

public void setActualEndDate(Date actualEndDate) {
	this.actualEndDate = actualEndDate;
}

public Integer getResultId() {
	return resultId;
}

public void setResultId(Integer resultId) {
	this.resultId = resultId;
}

public String getResultName() {
	return resultName;
}

public void setResultName(String resultName) {
	this.resultName = resultName;
}

public String getRemark() {
	return remark;
}

public void setRemark(String remark) {
	this.remark = remark;
}

public Date getCreatedDate() {
	return createdDate;
}

public void setCreatedDate(Date createdDate) {
	this.createdDate = createdDate;
}

public Date getModifiedDate() {
	return modifiedDate;
}

public void setModifiedDate(Date modifiedDate) {
	this.modifiedDate = modifiedDate;
}

public Integer getCreatedById() {
	return createdById;
}

public void setCreatedById(Integer createdById) {
	this.createdById = createdById;
}

public String getCreatedByName() {
	return createdByName;
}

public void setCreatedByName(String createdByName) {
	this.createdByName = createdByName;
}


public Integer getPlannedEffort() {
	return plannedEffort;
}

public void setPlannedEffort(Integer plannedEffort) {
	this.plannedEffort = plannedEffort;
}

public Integer getActualEffort() {
	return actualEffort;
}

public Integer getBaselineEffort() {
	return baselineEffort;
}
public void setBaselineEffort(Integer baselineEffort) {
	this.baselineEffort = baselineEffort;
}
public void setActualEffort(Integer actualEffort) {
	this.actualEffort = actualEffort;
}

public Integer getActivityWorkPackageId() {
	return activityWorkPackageId;
}

public void setActivityWorkPackageId(Integer activityWorkPackageId) {
	this.activityWorkPackageId = activityWorkPackageId;
}

public Integer getProductId() {
	return productId;
}

public void setProductId(Integer productId) {
	this.productId = productId;
}


public String getActivityWorkPackageName() {
	return activityWorkPackageName;
}

public void setActivityWorkPackageName(String activityWorkPackageName) {
	this.activityWorkPackageName = activityWorkPackageName;
}

public String getProductName() {
	return productName;
}

public void setProductName(String productName) {
	this.productName = productName;
}


public Integer getTestFactoryId() {
	return testFactoryId;
}

public void setTestFactoryId(Integer testFactoryId) {
	this.testFactoryId = testFactoryId;
}

public String getTestFactoryName() {
	return testFactoryName;
}

public void setTestFactoryName(String testFactoryName) {
	this.testFactoryName = testFactoryName;
}

public Integer getCustomerId() {
	return customerId;
}

public void setCustomerId(Integer customerId) {
	this.customerId = customerId;
}

public String getCustomerName() {
	return customerName;
}

public void setCustomerName(String customerName) {
	this.customerName = customerName;
}



public String get_class() {
	return _class;
}

public void set_class(String _class) {
	this._class = _class;
}

public String getId() {
	return id;
}

public void setId(String id) {
	this.id = id;
}


public Integer getBaselineTaskSize() {
	return baselineTaskSize;
}
public void setBaselineTaskSize(Integer baselineTaskSize) {
	this.baselineTaskSize = baselineTaskSize;
}
public Integer getPlannedTaskSize() {
	return plannedTaskSize;
}

public void setPlannedTaskSize(Integer plannedTaskSize) {
	this.plannedTaskSize = plannedTaskSize;
}

public Integer getActualTaskSize() {
	return actualTaskSize;
}

public void setActualTaskSize(Integer actualTaskSize) {
	this.actualTaskSize = actualTaskSize;
}


public Float getBaselineUnit() {
	return baselineUnit;
}
public void setBaselineUnit(Float baselineUnit) {
	this.baselineUnit = baselineUnit;
}
public Float getPlannedUnit() {
	return plannedUnit;
}

public void setPlannedUnit(Float plannedUnit) {
	this.plannedUnit = plannedUnit;
}

public Float getActualUnit() {
	return actualUnit;
}

public void setActualUnit(Float actualUnit) {
	this.actualUnit = actualUnit;
}



public Integer getCompetencyId() {
	return competencyId;
}



public void setCompetencyId(Integer competencyId) {
	this.competencyId = competencyId;
}



public String getCompetencyName() {
	return competencyName;
}



public void setCompetencyName(String competencyName) {
	this.competencyName = competencyName;
}






Integer getTestCentersId() {
	return testCentersId;
}



void setTestCentersId(Integer testCentersId) {
	this.testCentersId = testCentersId;
}



String getTestCentersName() {
	return testCentersName;
}



void setTestCentersName(String testCentersName) {
	this.testCentersName = testCentersName;
}



public String getActivityName() {
	return activityName;
}



public void setActivityName(String activityName) {
	this.activityName = activityName;
}



public Integer getPossibleSavings() {
	return possibleSavings;
}



public void setPossibleSavings(Integer possibleSavings) {
	this.possibleSavings = possibleSavings;
}



public Integer getEstimatedSavings() {
	return estimatedSavings;
}



public void setEstimatedSavings(Integer estimatedSavings) {
	this.estimatedSavings = estimatedSavings;
}



public Integer getActualSavings() {
	return actualSavings;
}



public void setActualSavings(Integer actualSavings) {
	this.actualSavings = actualSavings;
}



public Integer getVersionId() {
	return versionId;
}



public void setVersionId(Integer versionId) {
	this.versionId = versionId;
}



public Integer getBuildId() {
	return buildId;
}



public void setBuildId(Integer buildId) {
	this.buildId = buildId;
}



public String getVersionName() {
	return versionName;
}



public void setVersionName(String versionName) {
	this.versionName = versionName;
}



public String getBuildname() {
	return buildname;
}



public void setBuildname(String buildname) {
	this.buildname = buildname;
}



public Integer getWorkflowStatusId() {
	return workflowStatusId;
}



public void setWorkflowStatusId(Integer workflowStatusId) {
	this.workflowStatusId = workflowStatusId;
}



public String getWorkflowStatusName() {
	return workflowStatusName;
}



public void setWorkflowStatusName(String workflowStatusName) {
	this.workflowStatusName = workflowStatusName;
}



public Integer getTotalEffort() {
	return totalEffort;
}



public void setTotalEffort(Integer totalEffort) {
	this.totalEffort = totalEffort;
}
public Integer getSecondaryStatusId() {
	return secondaryStatusId;
}
public void setSecondaryStatusId(Integer secondaryStatusId) {
	this.secondaryStatusId = secondaryStatusId;
}
public String getSecondaryStatusName() {
	return secondaryStatusName;
}
public void setSecondaryStatusName(String secondaryStatusName) {
	this.secondaryStatusName = secondaryStatusName;
}
public Integer getLifeCycleStageId() {
	return lifeCycleStageId;
}
public void setLifeCycleStageId(Integer lifeCycleStageId) {
	this.lifeCycleStageId = lifeCycleStageId;
}
public String getLifeCycleStageName() {
	return lifeCycleStageName;
}
public void setLifeCycleStageName(String lifeCycleStageName) {
	this.lifeCycleStageName = lifeCycleStageName;
}
public Float getPercentageCompletion() {
	return percentageCompletion;
}
public void setPercentageCompletion(Float percentageCompletion) {
	this.percentageCompletion = percentageCompletion;
}

void setEnvironmentCombinationId(Integer environmentCombinationId) {
	this.environmentCombinationId = environmentCombinationId;
}
public String getParentStatus() {
	return parentStatus;
}
public void setParentStatus(String parentStatus) {
	this.parentStatus = parentStatus;
}
public String getStatus() {
	return status;
}
public void setStatus(String status) {
	this.status = status;
}


}
