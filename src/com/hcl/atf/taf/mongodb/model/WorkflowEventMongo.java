package com.hcl.atf.taf.mongodb.model;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.hcl.atf.taf.model.Activity;
import com.hcl.atf.taf.model.ActivityMaster;
import com.hcl.atf.taf.model.ActivityTask;
import com.hcl.atf.taf.model.ActivityTaskType;
import com.hcl.atf.taf.model.ActivityWorkPackage;
import com.hcl.atf.taf.model.Customer;
import com.hcl.atf.taf.model.ProductBuild;
import com.hcl.atf.taf.model.ProductMaster;
import com.hcl.atf.taf.model.ProductVersionListMaster;
import com.hcl.atf.taf.model.TestCaseList;
import com.hcl.atf.taf.model.TestFactory;
import com.hcl.atf.taf.model.TestFactoryLab;
import com.hcl.ilcm.workflow.model.WorkflowEvent;
import com.hcl.ilcm.workflow.model.WorkflowStatus;


@Document(collection = "workflowevents")
public class WorkflowEventMongo {
	
	@Id
	private String id;
	private Integer entityId;
	private String entityName;
	private Integer entityTypeId;
	private String entityTypeName;
	private Integer entityInstanceId;
	private String entityInstanceName;
	private Integer currentStatusId;
	private String currentStatusName;
	private Integer currentStatusCategoryId;
	private String currentStatusCategoryName;
	private Integer targetStatusId;
	private String targetStatusName;
	private Integer targetStatusCategoryId;
	private String targetStatusCategoryName;
	private Integer modifiedById;
	private String modifiedByName;
	private Date lastUpdatedDate;
	private Date plannedStartDate;
	private Date plannedEndDate;
	private Date actualStartDate;
	private Date actualEndDate;
	private Integer plannedEffort;
	private Integer actualEffort;
	private Integer entityGroupId;
	private String comments;
	private Integer slaDurationPlanned;
	private Integer slaDurationActual;
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
	
	private String parentStatus;
	
	private Date createdDate;
	private Date modifiedDate;

	public WorkflowEventMongo(){
		
	}
	
	public WorkflowEventMongo(WorkflowEvent workflowEvent) {
		ProductBuild productBuild = null;
		ProductVersionListMaster productVersionListMaster = null;
		ProductMaster productMaster = null;
		Customer customer = null;
		TestFactory testFactory = null;
		TestFactoryLab testFactoryLab = null;
		
		this.id = workflowEvent.getWorkflowEventId()+"";
		if(workflowEvent.getEntityType() != null){
			this.entityTypeId = workflowEvent.getEntityType().getEntitymasterid();
			this.entityTypeName = workflowEvent.getEntityType().getEntitymastername();
		}

		
		
		if(workflowEvent.getInstanceObject() != null){
			Object instanceObject = workflowEvent.getInstanceObject();
			if(instanceObject instanceof ActivityWorkPackage){
				ActivityWorkPackage activityWorkPackage = (ActivityWorkPackage) instanceObject;
				this.entityInstanceId = activityWorkPackage.getActivityWorkPackageId();
				this.entityInstanceName = activityWorkPackage.getActivityWorkPackageName();
				productBuild = activityWorkPackage.getProductBuild();
			}else if(instanceObject instanceof Activity){
				Activity activity = (Activity) instanceObject;
				this.entityInstanceId = activity.getActivityId();
				this.entityInstanceName = activity.getActivityName();
				if(activity.getActivityWorkPackage() != null){
					productBuild = activity.getActivityWorkPackage().getProductBuild();
				}
				ActivityMaster activityMaster = activity.getActivityMaster();
				if(activityMaster != null && workflowEvent.getEntityId() != null && activityMaster.getActivityMasterId() == workflowEvent.getEntityId()){
					this.entityId = activityMaster.getActivityMasterId();
					this.entityName = activityMaster.getActivityMasterName();
				}
			}else if(instanceObject instanceof ActivityTask){
				ActivityTask activityTask = (ActivityTask) instanceObject;
				this.entityInstanceId = activityTask.getActivityTaskId();
				this.entityInstanceName = activityTask.getActivityTaskName();
				if(activityTask.getActivity() != null && activityTask.getActivity().getActivityWorkPackage() != null){
					productBuild = activityTask.getActivity().getActivityWorkPackage().getProductBuild();
				}
				ActivityTaskType activityTaskType = activityTask.getActivityTaskType();
				if(activityTaskType != null && workflowEvent.getEntityId() != null && activityTaskType.getActivityTaskTypeId() == workflowEvent.getEntityId()){
					this.entityId = activityTaskType.getActivityTaskTypeId();
					this.entityName = activityTaskType.getActivityTaskTypeName();
				}
			}if(instanceObject instanceof TestCaseList){
				TestCaseList testCaseList = (TestCaseList) instanceObject;
				this.entityInstanceId = testCaseList.getTestCaseId();
				this.entityInstanceName = testCaseList.getTestCaseName();
				productMaster = testCaseList.getProductMaster();
			}
		}
		
		if(workflowEvent.getCurrentStatus() != null){
			WorkflowStatus workflowStatus = workflowEvent.getCurrentStatus();
			this.currentStatusId = workflowStatus.getWorkflowStatusId();
			this.currentStatusName = workflowStatus.getWorkflowStatusName();
			if(workflowStatus.getWorkflowStatusCategory() != null){
				this.currentStatusCategoryId = workflowStatus.getWorkflowStatusCategory().getWorkflowStatusCategoryId();
				this.currentStatusCategoryName = workflowStatus.getWorkflowStatusCategory().getWorkflowStatusCategoryName();
			}
		}
		
		if(workflowEvent.getTargetStatus() != null){
			WorkflowStatus workflowStatus = workflowEvent.getTargetStatus();
			this.targetStatusId = workflowStatus.getWorkflowStatusId();
			this.targetStatusName = workflowStatus.getWorkflowStatusName();
			if(workflowStatus.getWorkflowStatusCategory() != null){
				this.targetStatusCategoryId = workflowStatus.getWorkflowStatusCategory().getWorkflowStatusCategoryId();
				this.targetStatusCategoryName = workflowStatus.getWorkflowStatusCategory().getWorkflowStatusCategoryName();
			}
		}
		
		if(workflowEvent.getModifiedBy() != null){
			this.modifiedById = workflowEvent.getModifiedBy().getUserId();
			this.modifiedByName = workflowEvent.getModifiedBy().getLoginId();
		}
		
		this.lastUpdatedDate = workflowEvent.getLastUpdatedDate();
		this.plannedStartDate = workflowEvent.getPlannedStartDate();
		this.plannedEndDate = workflowEvent.getPlannedEndDate();
		this.actualStartDate = workflowEvent.getActualStartDate();
		this.actualEndDate = workflowEvent.getActualEndDate();
		this.plannedEffort = workflowEvent.getPlannedEffort();
		this.actualEffort = workflowEvent.getActualEffort();
		this.entityGroupId = workflowEvent.getEntityGroupId();
		this.comments = workflowEvent.getComments();
		this.slaDurationPlanned = workflowEvent.getSlaDurationPlanned();
		this.slaDurationActual = workflowEvent.getSlaDurationActual();
		
		if(productBuild != null){
			this.buildId = productBuild.getProductBuildId();
			this.buildname = productBuild.getBuildname();
			productVersionListMaster = productBuild.getProductVersion();
		}
		
		if(productVersionListMaster != null){
			this.versionId = productVersionListMaster.getProductVersionListId();
			this.versionName = productVersionListMaster.getProductVersionName();
			productMaster = productVersionListMaster.getProductMaster();
		}
		
		if(productMaster != null){
			this.productId = productMaster.getProductId();
			this.productName = productMaster.getProductName();
			customer = productMaster.getCustomer();
			testFactory = productMaster.getTestFactory();
		}
		
		if(customer != null){
			this.customerId = customer.getCustomerId();
			this.customerName = customer.getCustomerName();
		}
		
		if(testFactory != null){
			this.testFactoryId = testFactory.getTestFactoryId();
			this.testFactoryName = testFactory.getTestFactoryName();
			testFactoryLab = testFactory.getTestFactoryLab();
		}
		
		if(testFactoryLab != null){
			this.testCentersId = testFactoryLab.getTestFactoryLabId();
			this.testCentersName = testFactoryLab.getTestFactoryLabName();
		}
		
		this.createdDate = new Date();
		this.modifiedDate = new Date();
		
	}

	public String getId() {
		return id;
	}

	public void setId(String _id) {
		this.id = _id;
	}

	public String getParentStatus() {
		return parentStatus;
	}

	public void setParentStatus(String parentStatus) {
		this.parentStatus = parentStatus;
	}

	public Integer getEntityId() {
		return entityId;
	}

	public void setEntityId(Integer entityId) {
		this.entityId = entityId;
	}

	public String getEntityName() {
		return entityName;
	}

	public void setEntityName(String entityName) {
		this.entityName = entityName;
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

	public String getEntityInstanceName() {
		return entityInstanceName;
	}

	public void setEntityInstanceName(String entityInstanceName) {
		this.entityInstanceName = entityInstanceName;
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

	public Integer getCurrentStatusCategoryId() {
		return currentStatusCategoryId;
	}

	public void setCurrentStatusCategoryId(Integer currentStatusCategoryId) {
		this.currentStatusCategoryId = currentStatusCategoryId;
	}

	public String getCurrentStatusCategoryName() {
		return currentStatusCategoryName;
	}

	public void setCurrentStatusCategoryName(String currentStatusCategoryName) {
		this.currentStatusCategoryName = currentStatusCategoryName;
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

	public Integer getTargetStatusCategoryId() {
		return targetStatusCategoryId;
	}

	public void setTargetStatusCategoryId(Integer targetStatusCategoryId) {
		this.targetStatusCategoryId = targetStatusCategoryId;
	}

	public String getTargetStatusCategoryName() {
		return targetStatusCategoryName;
	}

	public void setTargetStatusCategoryName(String targetStatusCategoryName) {
		this.targetStatusCategoryName = targetStatusCategoryName;
	}

	public Integer getModifiedById() {
		return modifiedById;
	}

	public void setModifiedById(Integer modifiedById) {
		this.modifiedById = modifiedById;
	}

	public String getModifiedByName() {
		return modifiedByName;
	}

	public void setModifiedByName(String modifiedByName) {
		this.modifiedByName = modifiedByName;
	}

	public Date getLastUpdatedDate() {
		return lastUpdatedDate;
	}

	public void setLastUpdatedDate(Date lastUpdatedDate) {
		this.lastUpdatedDate = lastUpdatedDate;
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

	public Integer getSlaDurationPlanned() {
		return slaDurationPlanned;
	}

	public void setSlaDurationPlanned(Integer slaDurationPlanned) {
		this.slaDurationPlanned = slaDurationPlanned;
	}

	public Integer getSlaDurationActual() {
		return slaDurationActual;
	}

	public void setSlaDurationActual(Integer slaDurationActual) {
		this.slaDurationActual = slaDurationActual;
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

	public Integer getTestCentersId() {
		return testCentersId;
	}

	public void setTestCentersId(Integer testCentersId) {
		this.testCentersId = testCentersId;
	}

	public String getTestCentersName() {
		return testCentersName;
	}

	public void setTestCentersName(String testCentersName) {
		this.testCentersName = testCentersName;
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

	
}
