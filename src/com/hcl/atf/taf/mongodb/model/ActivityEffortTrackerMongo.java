package com.hcl.atf.taf.mongodb.model;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.hcl.atf.taf.model.ActivityEffortTracker;


@Document(collection = "activityEffortTracker")
public class ActivityEffortTrackerMongo {
	
	@Id
	private Integer _id;
	private Integer activityEffortTrackerId;
	private Integer activityTaskId;
	private String  activityTaskName;
	private Integer  activityTaskTypeId;
	private String  activityTaskTypeName;
	private Integer currentStatusId;
	private String currentStatusName;
	
	private Integer activityId;	
	private String activityName;	
	private Integer activityWorkPackageId;
	private String activityWorkPackageName;
	
	private Integer targetStatusId;
	private String targetStatusName;
	private String modifiedBy;
	private Date plannedStartDate;
	private Date plannedEndDate;
	private Date actualStartDate;
	private Date actualEndDate;
	private Integer plannedEffort;
	private Integer actualEffort;
	private Integer entityGroupId;
	private String comments;
	
	private Integer productId;
	private String productName;
	private Integer buildId;
	private String buildname;
	
	private Integer versionId;
	private String versionName;	
	private Integer testFactoryId;
	private String testFactoryName;
	private Integer testCentersId;
	private String testCentersName;
	
	private Integer customerId;
	private String customerName;
	
	private Date createdDate;
	private Date modifiedDate;
	
	

	public ActivityEffortTrackerMongo(){
		
	}
	
	
	
public ActivityEffortTrackerMongo(ActivityEffortTracker activityEffortTracker) {
	
	
	this._id=activityEffortTracker.getActivityEffortTrackerId();
	this.activityEffortTrackerId=activityEffortTracker.getActivityEffortTrackerId();
	
	
	
	
	
	if(activityEffortTracker.getEntity()!=null){
		this.activityTaskId=activityEffortTracker.getEntity().getActivityTaskId();
		this.activityTaskName=activityEffortTracker.getEntity().getActivityTaskName();
		
		if(activityEffortTracker.getEntity().getActivity()!=null){
			this.activityId = activityEffortTracker.getEntity().getActivity().getActivityId();
			this.activityName = activityEffortTracker.getEntity().getActivity().getActivityName();
			if(activityEffortTracker.getEntity().getActivity().getActivityWorkPackage()!=null){
				this.activityWorkPackageId = activityEffortTracker.getEntity().getActivity().getActivityWorkPackage().getActivityWorkPackageId();
				this.activityWorkPackageName = activityEffortTracker.getEntity().getActivity().getActivityWorkPackage().getActivityWorkPackageName();
			}
		}
		
		if(activityEffortTracker.getEntity().getActivityTaskType()!=null){
			this.activityTaskTypeId=activityEffortTracker.getEntity().getActivityTaskType().getActivityTaskTypeId();
			this.activityTaskTypeName=activityEffortTracker.getEntity().getActivityTaskType().getActivityTaskTypeName();
		}
	}
	
	if(activityEffortTracker.getCurrentStatus()!=null){
		this.currentStatusId=activityEffortTracker.getCurrentStatus().getActivityStatusId();
		this.currentStatusName=activityEffortTracker.getCurrentStatus().getActivityStatusName();
	}
	
	if(activityEffortTracker.getTargetStatus()!=null){
		this.targetStatusId=activityEffortTracker.getTargetStatus().getActivityStatusId();
		this.targetStatusName=activityEffortTracker.getTargetStatus().getActivityStatusName();
	}
	
	
	if(activityEffortTracker.getModifiedBy()!=null){
		this.modifiedBy=activityEffortTracker.getModifiedBy().getLoginId();
	}
	
	this.plannedStartDate=activityEffortTracker.getPlannedStartDate();
	
	this.plannedEndDate=activityEffortTracker.getPlannedEndDate();
	
	this.actualStartDate=activityEffortTracker.getActualStartDate();
	
	this.actualEndDate=activityEffortTracker.getActualEndDate();
	
	this.plannedEffort=activityEffortTracker.getPlannedEffort();
	
	this.actualEffort=activityEffortTracker.getActualEffort();
	
	this.entityGroupId=activityEffortTracker.getEntityGroupId();
	
	this.comments=activityEffortTracker.getComments();
			
	
	if(activityEffortTracker.getEntity().getActivity().getActivityWorkPackage()!=null){
		if(activityEffortTracker.getEntity().getActivity().getActivityWorkPackage().getProductBuild()!=null && activityEffortTracker.getEntity().getActivity().getActivityWorkPackage().getProductBuild().getProductVersion()!=null && activityEffortTracker.getEntity().getActivity().getActivityWorkPackage().getProductBuild().getProductVersion().getProductMaster()!=null){
		this.productId = activityEffortTracker.getEntity().getActivity().getActivityWorkPackage().getProductBuild().getProductVersion().getProductMaster().getProductId();
		this.productName = activityEffortTracker.getEntity().getActivity().getActivityWorkPackage().getProductBuild().getProductVersion().getProductMaster().getProductName();
		}
		if(activityEffortTracker.getEntity().getActivity().getActivityWorkPackage().getProductBuild()!=null){
			this.buildId = activityEffortTracker.getEntity().getActivity().getActivityWorkPackage().getProductBuild().getProductBuildId();
			this.buildname = activityEffortTracker.getEntity().getActivity().getActivityWorkPackage().getProductBuild().getBuildname();
		}
		if(activityEffortTracker.getEntity().getActivity().getActivityWorkPackage().getProductBuild()!=null && activityEffortTracker.getEntity().getActivity().getActivityWorkPackage().getProductBuild().getProductVersion()!=null && activityEffortTracker.getEntity().getActivity().getActivityWorkPackage().getProductBuild().getProductVersion()!=null){
		this.versionId = activityEffortTracker.getEntity().getActivity().getActivityWorkPackage().getProductBuild().getProductVersion().getProductVersionListId();
		this.versionName = activityEffortTracker.getEntity().getActivity().getActivityWorkPackage().getProductBuild().getProductVersion().getProductVersionName();
		}
		if(activityEffortTracker.getEntity().getActivity().getActivityWorkPackage().getProductBuild()!=null && activityEffortTracker.getEntity().getActivity().getActivityWorkPackage().getProductBuild().getProductVersion()!=null && activityEffortTracker.getEntity().getActivity().getActivityWorkPackage().getProductBuild().getProductVersion().getProductMaster().getTestFactory()!=null){
		this.testFactoryId = activityEffortTracker.getEntity().getActivity().getActivityWorkPackage().getProductBuild().getProductVersion().getProductMaster().getTestFactory().getTestFactoryId();
		this.testFactoryName = activityEffortTracker.getEntity().getActivity().getActivityWorkPackage().getProductBuild().getProductVersion().getProductMaster().getTestFactory().getTestFactoryName();
		this.testCentersId=activityEffortTracker.getEntity().getActivity().getActivityWorkPackage().getProductBuild().getProductVersion().getProductMaster().getTestFactory().getTestFactoryLab().getTestFactoryLabId();
		this.testCentersName=activityEffortTracker.getEntity().getActivity().getActivityWorkPackage().getProductBuild().getProductVersion().getProductMaster().getTestFactory().getTestFactoryLab().getTestFactoryLabName();
		
		
		}
		if(activityEffortTracker.getEntity().getActivity().getActivityWorkPackage().getProductBuild()!=null && activityEffortTracker.getEntity().getActivity().getActivityWorkPackage().getProductBuild().getProductVersion()!=null && activityEffortTracker.getEntity().getActivity().getActivityWorkPackage().getProductBuild().getProductVersion().getProductMaster().getCustomer()!=null){
		this.customerId = activityEffortTracker.getEntity().getActivity().getActivityWorkPackage().getProductBuild().getProductVersion().getProductMaster().getCustomer().getCustomerId();
		this.customerName = activityEffortTracker.getEntity().getActivity().getActivityWorkPackage().getProductBuild().getProductVersion().getProductMaster().getCustomer().getCustomerName();
		}
	}
	
	this.createdDate=new Date();
	this.modifiedDate=activityEffortTracker.getLastUpdatedDate();

	
	
}



public Integer get_id() {
	return _id;
}



public void set_id(Integer _id) {
	this._id = _id;
}



public Integer getActivityEffortTrackerId() {
	return activityEffortTrackerId;
}



public void setActivityEffortTrackerId(Integer activityEffortTrackerId) {
	this.activityEffortTrackerId = activityEffortTrackerId;
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



public Integer getPlannedEffort() {
	return plannedEffort;
}



public void setPlannedEffort(Integer plannedEffort) {
	this.plannedEffort = plannedEffort;
}



public Integer getActualEffort() {
	return actualEffort;
}



public void setActualEffort(Integer actualEffort) {
	this.actualEffort = actualEffort;
}



public Integer getEntityGroupId() {
	return entityGroupId;
}



public void setEntityGroupId(Integer entityGroupId) {
	this.entityGroupId = entityGroupId;
}



public String getComments() {
	return comments;
}



public void setComments(String comments) {
	this.comments = comments;
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



public Integer getBuildId() {
	return buildId;
}



public void setBuildId(Integer buildId) {
	this.buildId = buildId;
}



public String getBuildname() {
	return buildname;
}



public void setBuildname(String buildname) {
	this.buildname = buildname;
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




public Integer getCurrentStatusId() {
	return currentStatusId;
}



public void setCurrentStatusId(Integer currentStatusId) {
	this.currentStatusId = currentStatusId;
}









public String getCurrentStatusName() {
	return currentStatusName;
}



public void setCurrentStatusName(String currentStatusName) {
	this.currentStatusName = currentStatusName;
}



public Integer getTargetStatusId() {
	return targetStatusId;
}



public void setTargetStatusId(Integer targetStatusId) {
	this.targetStatusId = targetStatusId;
}



public String getTargetStatusName() {
	return targetStatusName;
}



public void setTargetStatusName(String targetStatusName) {
	this.targetStatusName = targetStatusName;
}



public String getModifiedBy() {
	return modifiedBy;
}



public void setModifiedBy(String modifiedBy) {
	this.modifiedBy = modifiedBy;
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



Integer getActivityTaskId() {
	return activityTaskId;
}



void setActivityTaskId(Integer activityTaskId) {
	this.activityTaskId = activityTaskId;
}



String getActivityTaskName() {
	return activityTaskName;
}



void setActivityTaskName(String activityTaskName) {
	this.activityTaskName = activityTaskName;
}



Integer getActivityTaskTypeId() {
	return activityTaskTypeId;
}



void setActivityTaskTypeId(Integer activityTaskTypeId) {
	this.activityTaskTypeId = activityTaskTypeId;
}



String getActivityTaskTypeName() {
	return activityTaskTypeName;
}



void setActivityTaskTypeName(String activityTaskTypeName) {
	this.activityTaskTypeName = activityTaskTypeName;
}



Integer getActivityId() {
	return activityId;
}



void setActivityId(Integer activityId) {
	this.activityId = activityId;
}



String getActivityName() {
	return activityName;
}



void setActivityName(String activityName) {
	this.activityName = activityName;
}



Integer getActivityWorkPackageId() {
	return activityWorkPackageId;
}



void setActivityWorkPackageId(Integer activityWorkPackageId) {
	this.activityWorkPackageId = activityWorkPackageId;
}



String getActivityWorkPackageName() {
	return activityWorkPackageName;
}



void setActivityWorkPackageName(String activityWorkPackageName) {
	this.activityWorkPackageName = activityWorkPackageName;
}
	
	
}
