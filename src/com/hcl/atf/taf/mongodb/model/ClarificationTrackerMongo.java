package com.hcl.atf.taf.mongodb.model;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.hcl.atf.taf.model.ClarificationTracker;


@Document(collection = "activityclarificationtracker")
public class ClarificationTrackerMongo {
	
	@Id
	private String id;
	private Integer clarificationTrackerId;
	private String clarificationTitle;
	private Integer clarificationTypeId;
	private String clarificationTypeName;
	private String clarificationDescription;
	private Date raisedDate;
	private Integer raisedByUserId;
	private String raisedByUserName;
	private Integer executionPriorityId;
	private String  executionPriority;
	private Integer ownerId;
	private String ownerName;
	private Integer workflowStatusId;
	private String workflowStatusName;
	private Integer dependentCR;
	private Integer dependentActivityTracker;
	private Integer entityTypeId;
	private String entityTypeName;
	private Integer entityInstanceId;
	private String entityInstanceName;
	private Date plannedStartDate;
	private Date plannedEndDate;
	private Date actualStartDate;
	private Date actualEndDate;
	private Integer resolution;
	
	private Integer planExpectedValue;
	private Integer achievedValue;
	
	private Integer productId;
	private String productName;
	
	private Integer versionId;
	private String versionName;
	
	private Integer buildId;
	private String buildName;
	
	private Integer workPackageId;
	private String workPackageName;
	
	
	private Integer activityId;
	private String activityName;
	
	private Integer activityTaskId;
	private String activityTaskName;
	
	
	private Integer testFactoryId;
	private String testFactoryName;
	
	private Integer testCenterId;
	private String testCenterName;
	
	

	public ClarificationTrackerMongo(){
		
	}
	
	public ClarificationTrackerMongo(ClarificationTracker clarificationTracker) {
			this.id = clarificationTracker.getClarificationTrackerId()+"";
			this.clarificationTrackerId= clarificationTracker.getClarificationTrackerId();
			this.dependentCR=clarificationTracker.getDependentCR();
			
			this.planExpectedValue = clarificationTracker.getPlanExpectedValue();
			this.achievedValue = clarificationTracker.getAchievedValue();		
			
		
			this.clarificationTitle=clarificationTracker.getClarificationTitle();
			this.raisedDate=clarificationTracker.getRaisedDate();
			this.clarificationDescription=clarificationTracker.getClarificationDescription();
			this.dependentActivityTracker=clarificationTracker.getDependentActivityTracker();
			this.entityInstanceId=clarificationTracker.getEntityInstanceId();
			this.plannedStartDate=clarificationTracker.getPlannedStartDate();
	
			this.plannedEndDate=clarificationTracker.getPlannedEndDate();
			this.actualStartDate=clarificationTracker.getActualStartDate();
			this.actualEndDate=clarificationTracker.getActualEndDate();
			this.resolution=clarificationTracker.getResolution();
			
			if(clarificationTracker.getClarificationType()!=null){
				this.clarificationTypeId=clarificationTracker.getClarificationType().getClarificationId();
				this.clarificationTypeName=clarificationTracker.getClarificationType().getClarificationType();
			}
			
			if(clarificationTracker.getRaisedBy()!=null){
				this.raisedByUserId=clarificationTracker.getRaisedBy().getUserId();
				this.raisedByUserName=clarificationTracker.getRaisedBy().getLoginId();
			}
			
			if(clarificationTracker.getPriority()!=null){
				this.executionPriorityId=clarificationTracker.getPriority().getExecutionPriorityId();
				this.executionPriority=clarificationTracker.getPriority().getExecutionPriority();
			}
			
			if(clarificationTracker.getOwner()!=null){
				this.ownerId=clarificationTracker.getOwner().getUserId();
				this.ownerName=clarificationTracker.getOwner().getLoginId();
			}
			if(clarificationTracker.getEntityStatus()!=null){
				this.workflowStatusId=	clarificationTracker.getEntityStatus().getEntityStatusId();
				this.workflowStatusName=clarificationTracker.getEntityStatus().getEntityStatusName();
			}
			
			if(clarificationTracker.getEntityType()!=null){
				this.entityTypeId=clarificationTracker.getEntityType().getEntitymasterid();
				this.entityTypeName=clarificationTracker.getEntityType().getEntitymastername();
			}
			
		}

		public String getId() {
			return id;
		}
		
		public void setId(String id) {
			this.id = id;
		}
		
		public Integer getClarificationTrackerId() {
			return clarificationTrackerId;
		}
		
		public void setClarificationTrackerId(Integer clarificationTrackerId) {
			this.clarificationTrackerId = clarificationTrackerId;
		}
		
		public String getClarificationTitle() {
			return clarificationTitle;
		}
		
		public void setClarificationTitle(String clarificationTitle) {
			this.clarificationTitle = clarificationTitle;
		}
		
		public Integer getClarificationTypeId() {
			return clarificationTypeId;
		}
		
		public void setClarificationTypeId(Integer clarificationTypeId) {
			this.clarificationTypeId = clarificationTypeId;
		}
		
		public String getClarificationTypeName() {
			return clarificationTypeName;
		}
		
		public void setClarificationTypeName(String clarificationTypeName) {
			this.clarificationTypeName = clarificationTypeName;
		}
		
		public String getClarificationDescription() {
			return clarificationDescription;
		}
		
		public void setClarificationDescription(String clarificationDescription) {
			this.clarificationDescription = clarificationDescription;
		}
		
		public Date getRaisedDate() {
			return raisedDate;
		}
		
		public void setRaisedDate(Date raisedDate) {
			this.raisedDate = raisedDate;
		}
		
		public Integer getRaisedByUserId() {
			return raisedByUserId;
		}
		
		public void setRaisedByUserId(Integer raisedByUserId) {
			this.raisedByUserId = raisedByUserId;
		}
		
		public String getRaisedByUserName() {
			return raisedByUserName;
		}
		
		public void setRaisedByUserName(String raisedByUserName) {
			this.raisedByUserName = raisedByUserName;
		}
		
		public Integer getExecutionPriorityId() {
			return executionPriorityId;
		}
		
		public void setExecutionPriorityId(Integer executionPriorityId) {
			this.executionPriorityId = executionPriorityId;
		}
		
		public String getExecutionPriority() {
			return executionPriority;
		}
		
		public void setExecutionPriority(String executionPriority) {
			this.executionPriority = executionPriority;
		}
		
		public Integer getOwnerId() {
			return ownerId;
		}
		
		public void setOwnerId(Integer ownerId) {
			this.ownerId = ownerId;
		}
		
		public String getOwnerName() {
			return ownerName;
		}
		
		public void setOwnerName(String ownerName) {
			this.ownerName = ownerName;
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
		
		public Integer getDependentCR() {
			return dependentCR;
		}
		
		public void setDependentCR(Integer dependentCR) {
			this.dependentCR = dependentCR;
		}
		
		public Integer getDependentActivityTracker() {
			return dependentActivityTracker;
		}
		
		public void setDependentActivityTracker(Integer dependentActivityTracker) {
			this.dependentActivityTracker = dependentActivityTracker;
		}
		
		public Integer getEntityTypeId() {
			return entityTypeId;
		}
		
		public void setEntityTypeId(Integer entityTypeId) {
			this.entityTypeId = entityTypeId;
		}
		
		public String getEntityTypeName() {
			return entityTypeName;
		}
		
		public void setEntityTypeName(String entityTypeName) {
			this.entityTypeName = entityTypeName;
		}
		
		public Integer getEntityInstanceId() {
			return entityInstanceId;
		}
		
		public void setEntityInstanceId(Integer entityInstanceId) {
			this.entityInstanceId = entityInstanceId;
		}
		
		public Date getPlannedStartDate() {
			return plannedStartDate;
		}
		
		public void setPlannedStartDate(Date plannedStartDate) {
			this.plannedStartDate = plannedStartDate;
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
		
		public Integer getResolution() {
			return resolution;
		}
		
		public void setResolution(Integer resolution) {
			this.resolution = resolution;
		}

		public Date getPlannedEndDate() {
			return plannedEndDate;
		}

		public void setPlannedEndDate(Date plannedEndDate) {
			this.plannedEndDate = plannedEndDate;
		}

		public Integer getProductId() {
			return productId;
		}

		public void setProductId(Integer productId) {
			this.productId = productId;
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

		public Integer getTestCenterId() {
			return testCenterId;
		}

		public void setTestCenterId(Integer testCenterId) {
			this.testCenterId = testCenterId;
		}

		public String getTestCenterName() {
			return testCenterName;
		}

		public void setTestCenterName(String testCenterName) {
			this.testCenterName = testCenterName;
		}

		public String getEntityInstanceName() {
			return entityInstanceName;
		}

		public void setEntityInstanceName(String entityInstanceName) {
			this.entityInstanceName = entityInstanceName;
		}

		public Integer getWorkPackageId() {
			return workPackageId;
		}

		public void setWorkPackageId(Integer workPackageId) {
			this.workPackageId = workPackageId;
		}

		public String getWorkPackageName() {
			return workPackageName;
		}

		public void setWorkPackageName(String workPackageName) {
			this.workPackageName = workPackageName;
		}

		public Integer getBuildId() {
			return buildId;
		}

		public void setBuildId(Integer buildId) {
			this.buildId = buildId;
		}

		public String getBuildName() {
			return buildName;
		}

		public void setBuildName(String buildName) {
			this.buildName = buildName;
		}

		public Integer getVersionId() {
			return versionId;
		}

		public void setVersionId(Integer versionId) {
			this.versionId = versionId;
		}

		public String getVersionName() {
			return versionName;
		}

		public void setVersionName(String versionName) {
			this.versionName = versionName;
		}

		public Integer getActivityId() {
			return activityId;
		}

		public void setActivityId(Integer activityId) {
			this.activityId = activityId;
		}

		public String getActivityName() {
			return activityName;
		}

		public void setActivityName(String activityName) {
			this.activityName = activityName;
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

		public Integer getPlanExpectedValue() {
			return planExpectedValue;
		}

		public void setPlanExpectedValue(Integer planExpectedValue) {
			this.planExpectedValue = planExpectedValue;
		}

		public Integer getAchievedValue() {
			return achievedValue;
		}

		public void setAchievedValue(Integer achievedValue) {
			this.achievedValue = achievedValue;
		}




	
	
}
