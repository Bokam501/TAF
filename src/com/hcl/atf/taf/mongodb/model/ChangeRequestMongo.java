package com.hcl.atf.taf.mongodb.model;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.hcl.atf.taf.model.ChangeRequest;

@Document(collection = "changerequest")
public class ChangeRequestMongo {
	
	@Id
	private String id;
	private Integer changeRequestId;
	private String changeRequestName;
	private String description;
	private Date raisedDate;
	private Integer executionPriorityId;
	private String  executionPriority;
	private Integer ownerId;
	private String ownerName;
	private Integer statusCategoryId;
	private String statusCategoryName;
	private Integer productId;
	private String productName;
	
	private String status;
	private Integer crTypeId;
	private String crTypeName;
	private Integer planExpectedValue;
	private Integer achievedValue;
	private Integer entityTypeId;
	private String entityTypeName;
	private Integer entityInstanceId;
	private String entityInstanceName;
	
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
	
	private Date modifiedDate;
	
	public ChangeRequestMongo(){
		
	}
	
	public ChangeRequestMongo(ChangeRequest changeRequest) {
		
			this.id = changeRequest.getChangeRequestId()+"";
			this.changeRequestId= changeRequest.getChangeRequestId();
			this.changeRequestName=changeRequest.getChangeRequestName();
			this.description = changeRequest.getDescription();
			this.raisedDate = changeRequest.getRaisedDate();
			this.planExpectedValue = changeRequest.getPlanExpectedValue();
			this.achievedValue = changeRequest.getAchievedValue();		
			this.modifiedDate = changeRequest.getModifiedDate();
			if(changeRequest.getIsActive() == 1){
				this.status = "Active";
			}else{
				this.status = "InActive";
			}
			this.entityInstanceId = changeRequest.getEntityInstanceId();
			
			if(changeRequest.getPriority() != null){
				this.executionPriorityId = changeRequest.getPriority().getExecutionPriorityId();
				this.executionPriority = changeRequest.getPriority().getExecutionPriorityName();
			}
			
			if(changeRequest.getOwner() != null){
				this.ownerId=changeRequest.getOwner().getUserId();
				this.ownerName=changeRequest.getOwner().getLoginId();
			}
			
			if(changeRequest.getStatusCategory() != null){
				this.statusCategoryId=changeRequest.getStatusCategory().getStatusCategoryId();
				this.statusCategoryName=changeRequest.getStatusCategory().getStatusCategoryName();
			}
			
			if(changeRequest.getProduct() != null){
				this.productId=changeRequest.getProduct().getProductId();
				this.productName=changeRequest.getProduct().getProductName();
			}
			
			if(changeRequest.getCrType() != null){
				this.crTypeId=changeRequest.getCrType().getChangeRequestTypeId();
				this.crTypeName=changeRequest.getCrType().getChangeRequestTypeName();
			}			
			
			if(changeRequest.getEntityType() != null){
				this.entityTypeId=changeRequest.getEntityType().getEntitymasterid();
				this.entityTypeName=changeRequest.getEntityType().getEntitymastername();
			}
			
		}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public Integer getChangeRequestId() {
		return changeRequestId;
	}
	public void setChangeRequestId(Integer changeRequestId) {
		this.changeRequestId = changeRequestId;
	}
	public String getChangeRequestName() {
		return changeRequestName;
	}
	public void setChangeRequestName(String changeRequestName) {
		this.changeRequestName = changeRequestName;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Date getRaisedDate() {
		return raisedDate;
	}
	public void setRaisedDate(Date raisedDate) {
		this.raisedDate = raisedDate;
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
	public Integer getStatusCategoryId() {
		return statusCategoryId;
	}
	public void setStatusCategoryId(Integer statusCategoryId) {
		this.statusCategoryId = statusCategoryId;
	}
	public String getStatusCategoryName() {
		return statusCategoryName;
	}
	public void setStatusCategoryName(String statusCategoryName) {
		this.statusCategoryName = statusCategoryName;
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
	public Date getModifiedDate() {
		return modifiedDate;
	}
	public void setModifiedDate(Date modifiedDate) {
		this.modifiedDate = modifiedDate;
	}
	
	public Integer getCrTypeId() {
		return crTypeId;
	}
	public void setCrTypeId(Integer crTypeId) {
		this.crTypeId = crTypeId;
	}
	public String getCrTypeName() {
		return crTypeName;
	}
	public void setCrTypeName(String crTypeName) {
		this.crTypeName = crTypeName;
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

	String getStatus() {
		return status;
	}

	void setStatus(String status) {
		this.status = status;
	}

	
	
	
}
