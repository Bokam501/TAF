package com.hcl.atf.taf.mongodb.model;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.hcl.atf.taf.model.ActivityWorkPackage;

@Document(collection = "activityworkPackages")
public class AcitivityWorkPackageMongo {	
	@Id
	private String id;	
	private Integer activityWorkPackageId;
	private String activityWorkPackageName;
	private String description;
	private  Date baselineStartDate; 
	private  Date baselineEndDate;
	private  Date plannedStartDate; 
	private  Date plannedEndDate;
	private  Date actualStartDate;
	private  Date actualEndDate;
	/*private  Integer statusId;
	private String statusName;*/
	private  Integer statusCategoryId;
	private String statusCategoryName;
	private  Integer priorityId;
	private String priorityName;	
	private String ownerId;
	private String ownerName;
	private String activityWorkPackageTag;
	private String activityWorkPackageType;
	private String remark;
	private  Integer createdById;
	private  String createdByName;
	private String status;
	private  Integer modifiedById;
	private String modifiedByName;
	private Integer productId;
	private String productName;
	private Integer customerId;
	private String customerName;
	private Integer buildId;
	private String buildname;
	private Integer versionId;
	private String versionName;		
	private Integer testFactoryId;
	private String testFactoryName;
	private Integer testCentersId;
	private String testCentersName;
	
	private Integer workflowStatusId;
	private String workflowStatusName;
    private Integer totalEffort;
    private Float percentageCompletion;
	
	private Integer competencyId;
	private String competencyName;
	
	private String parentStatus;
	
	private  Date createdDate;
	private  Date modifiedDate;

	public AcitivityWorkPackageMongo() {
		
	}

	public AcitivityWorkPackageMongo(ActivityWorkPackage actvityWorkPackage) {
		this.id=actvityWorkPackage.getActivityWorkPackageId()+"";
		this.activityWorkPackageId = actvityWorkPackage.getActivityWorkPackageId();
		this.activityWorkPackageName = actvityWorkPackage.getActivityWorkPackageName();
		this.description = actvityWorkPackage.getDescription();
				
		this.baselineStartDate = actvityWorkPackage.getBaselineStartDate();
		this.baselineEndDate = actvityWorkPackage.getBaselineEndDate();
		this.plannedStartDate = actvityWorkPackage.getPlannedStartDate();
		this.plannedEndDate = actvityWorkPackage.getPlannedEndDate();
		this.actualStartDate = actvityWorkPackage.getActualStartDate();
		this.actualEndDate = actvityWorkPackage.getActualEndDate();
		/*if(actvityWorkPackage.getStatus()!=null){
			this.statusId = actvityWorkPackage.getStatus().getActivityStatusId();
			this.statusName = actvityWorkPackage.getStatus().getActivityStatusName();
		}*/
	    if(actvityWorkPackage.getWorkflowStatus()!=null){
	    	this.workflowStatusId=actvityWorkPackage.getWorkflowStatus().getWorkflowStatusId();
	    	this.workflowStatusName=actvityWorkPackage.getWorkflowStatus().getWorkflowStatusName();
	    }
		this.totalEffort=actvityWorkPackage.getTotalEffort();
		this.percentageCompletion=actvityWorkPackage.getPercentageCompletion();
		
		if(actvityWorkPackage.getStatusCategory()!=null){
			this.statusCategoryId = actvityWorkPackage.getStatusCategory().getStatusCategoryId();
			this.statusCategoryName = actvityWorkPackage.getStatusCategory().getStatusCategoryName();
		}
		
		if(actvityWorkPackage.getPriority()!=null){
			this.priorityId = actvityWorkPackage.getPriority().getExecutionPriorityId();
			this.priorityName = actvityWorkPackage.getPriority().getExecutionPriorityName();
		}
		if(actvityWorkPackage.getOwner()!=null){
			this.ownerId = actvityWorkPackage.getOwner().getLoginId();
			this.ownerName = actvityWorkPackage.getOwner().getUserDisplayName();
		}
		
		this.activityWorkPackageTag = actvityWorkPackage.getActivityWorkPackageTag();
		this.activityWorkPackageType = actvityWorkPackage.getActivityWorkPackageType();
		this.remark = actvityWorkPackage.getRemark();
		this.createdDate = actvityWorkPackage.getCreatedDate();
		this.modifiedDate = actvityWorkPackage.getModifiedDate();
		if(actvityWorkPackage.getCreatedBy()!=null){
			this.createdById = actvityWorkPackage.getCreatedBy().getUserId();
			this.createdByName = actvityWorkPackage.getCreatedBy().getUserDisplayName();
		}
		
		
		
		
		if(actvityWorkPackage.getModifiedBy()!=null){
			this.modifiedById = actvityWorkPackage.getModifiedBy().getUserId();
			this.modifiedByName = actvityWorkPackage.getModifiedBy().getUserDisplayName();
		}
		
		 
		if(actvityWorkPackage.getIsActive() == 1){
			this.status = "Active";
		}else{
			this.status = "InActive";
		}
		
		
		this.customerId = actvityWorkPackage.getProductBuild().getProductVersion().getProductMaster().getCustomer().getCustomerId();
		this.customerName = actvityWorkPackage.getProductBuild().getProductVersion().getProductMaster().getCustomer().getCustomerName();
		
		if(actvityWorkPackage.getProductBuild()!=null){
			this.buildId = actvityWorkPackage.getProductBuild().getProductBuildId();
			this.buildname = actvityWorkPackage.getProductBuild().getBuildname();
			
			if(actvityWorkPackage.getProductBuild()!= null && actvityWorkPackage.getProductBuild().getStatus() == 1){
				this.parentStatus = "Active";
			}else{
				this.parentStatus = "InActive";
			}
			
			if(actvityWorkPackage.getProductBuild().getProductVersion()!=null){
				this.versionId = actvityWorkPackage.getProductBuild().getProductVersion().getProductVersionListId();	
				this.versionName = actvityWorkPackage.getProductBuild().getProductVersion().getProductVersionName();
				
				if(actvityWorkPackage.getProductBuild().getProductVersion().getProductMaster()!=null){
					this.productId=actvityWorkPackage.getProductBuild().getProductVersion().getProductMaster().getProductId();
					this.productName=actvityWorkPackage.getProductBuild().getProductVersion().getProductMaster().getProductName();
					
					if(actvityWorkPackage.getProductBuild().getProductVersion().getProductMaster().getTestFactory()!=null){
						this.testFactoryId = actvityWorkPackage.getProductBuild().getProductVersion().getProductMaster().getTestFactory().getTestFactoryId();
						this.testFactoryName = actvityWorkPackage.getProductBuild().getProductVersion().getProductMaster().getTestFactory().getTestFactoryName();
						
						if(actvityWorkPackage.getProductBuild().getProductVersion().getProductMaster().getTestFactory().getTestFactoryLab()!=null){
							this.testCentersId=actvityWorkPackage.getProductBuild().getProductVersion().getProductMaster().getTestFactory().getTestFactoryLab().getTestFactoryLabId();
							this.testCentersName=actvityWorkPackage.getProductBuild().getProductVersion().getProductMaster().getTestFactory().getTestFactoryLab().getTestFactoryLabName();
						}
					}
				}
			}
		}
		
		if(actvityWorkPackage.getCompetency() != null && actvityWorkPackage.getCompetency().getDimensionId() > 1){
			this.competencyId = actvityWorkPackage.getCompetency().getDimensionId();
			this.competencyName = actvityWorkPackage.getCompetency().getName();
		}
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Integer getActivityWorkPackageId() {
		return activityWorkPackageId;
	}

	public void setActivityWorkPackageId(Integer activityWorkPackageId) {
		this.activityWorkPackageId = activityWorkPackageId;
	}

	public String getActivityWorkPackageName() {
		return activityWorkPackageName;
	}

	public void setActivityWorkPackageName(String activityWorkPackageName) {
		this.activityWorkPackageName = activityWorkPackageName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
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

	/*public Integer getStatusId() {
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
	}*/
	
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

	public String getOwnerId() {
		return ownerId;
	}

	public void setOwnerId(String ownerId) {
		this.ownerId = ownerId;
	}

	public String getOwnerName() {
		return ownerName;
	}

	public void setOwnerName(String ownerName) {
		this.ownerName = ownerName;
	}

	public String getActivityWorkPackageTag() {
		return activityWorkPackageTag;
	}

	public void setActivityWorkPackageTag(String activityWorkPackageTag) {
		this.activityWorkPackageTag = activityWorkPackageTag;
	}

	public String getActivityWorkPackageType() {
		return activityWorkPackageType;
	}

	public void setActivityWorkPackageType(String activityWorkPackageType) {
		this.activityWorkPackageType = activityWorkPackageType;
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

	public Date getPlannedStartDate() {
		return plannedStartDate;
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

	public Float getPercentageCompletion() {
		return percentageCompletion;
	}

	public void setPercentageCompletion(Float percentageCompletion) {
		this.percentageCompletion = percentageCompletion;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getParentStatus() {
		return parentStatus;
	}

	public void setParentStatus(String parentStatus) {
		this.parentStatus = parentStatus;
	}
	
	
}
