package com.hcl.atf.taf.mongodb.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.hcl.atf.taf.model.Activity;
import com.hcl.atf.taf.model.EntityRelationship;

@Document(collection = "changeRequestMapping")
public class ChangeRequestMappingMongo {

	@Id
	private String id;
	private String _class;
	private Integer changeRequestMappingId;
	private Integer activityTypeId;
	private Integer activityId;
	private Integer changeRequestTypeId;
	private Integer changeRequestId;
	private String activityName;
	private String changeRequestName;
	private Integer activityWorkpackageId;
	private String activityWorkPackageName;
	
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
	private String status;
	
	
	
	
	public ChangeRequestMappingMongo(EntityRelationship entityRelationship, String activityName, String changeRequestName, Integer actWPId, String actWPName, Activity activity) {
		this.id=entityRelationship.getId()+"";
		this.changeRequestMappingId = entityRelationship.getId();
		this._class = this.getClass().getName().replace("\\", ".").replace("/", ".");
		this.activityTypeId = entityRelationship.getEntityTypeId1();
		this.activityId = entityRelationship.getEntityInstanceId1();
		this.changeRequestTypeId = entityRelationship.getEntityTypeId2();
		this.changeRequestId = entityRelationship.getEntityInstanceId2();
		this.activityName = activityName;
		this.changeRequestName = changeRequestName;
		this.activityWorkpackageId = actWPId;
		this.activityWorkPackageName = actWPName;
		this.parentStatus = "Active";
		this.status = "Active";
		
		
		if(activity != null){
			if(activity.getActivityWorkPackage() != null){
					if(activity.getActivityWorkPackage().getProductBuild()!=null && activity.getActivityWorkPackage().getProductBuild().getProductVersion()!=null && activity.getActivityWorkPackage().getProductBuild().getProductVersion().getProductMaster()!=null){
					this.productId = activity.getActivityWorkPackage().getProductBuild().getProductVersion().getProductMaster().getProductId();
					this.productName = activity.getActivityWorkPackage().getProductBuild().getProductVersion().getProductMaster().getProductName();
					}
					if(activity.getActivityWorkPackage().getProductBuild()!=null){
						this.buildId = activity.getActivityWorkPackage().getProductBuild().getProductBuildId();
						this.buildname = activity.getActivityWorkPackage().getProductBuild().getBuildname();
					}
					if(activity.getActivityWorkPackage().getProductBuild()!=null && activity.getActivityWorkPackage().getProductBuild().getProductVersion()!=null && activity.getActivityWorkPackage().getProductBuild().getProductVersion()!=null){
					this.versionId = activity.getActivityWorkPackage().getProductBuild().getProductVersion().getProductVersionListId();
					this.versionName = activity.getActivityWorkPackage().getProductBuild().getProductVersion().getProductVersionName();
					}
					if(activity.getActivityWorkPackage().getProductBuild()!=null && activity.getActivityWorkPackage().getProductBuild().getProductVersion()!=null && activity.getActivityWorkPackage().getProductBuild().getProductVersion().getProductMaster().getTestFactory()!=null){
					this.testFactoryId = activity.getActivityWorkPackage().getProductBuild().getProductVersion().getProductMaster().getTestFactory().getTestFactoryId();
					this.testFactoryName = activity.getActivityWorkPackage().getProductBuild().getProductVersion().getProductMaster().getTestFactory().getTestFactoryName();
					this.testCentersId=activity.getActivityWorkPackage().getProductBuild().getProductVersion().getProductMaster().getTestFactory().getTestFactoryLab().getTestFactoryLabId();
					this.testCentersName=activity.getActivityWorkPackage().getProductBuild().getProductVersion().getProductMaster().getTestFactory().getTestFactoryLab().getTestFactoryLabName();
					}
					if(activity.getActivityWorkPackage().getProductBuild()!=null && activity.getActivityWorkPackage().getProductBuild().getProductVersion()!=null && activity.getActivityWorkPackage().getProductBuild().getProductVersion().getProductMaster().getCustomer()!=null){
					this.customerId = activity.getActivityWorkPackage().getProductBuild().getProductVersion().getProductMaster().getCustomer().getCustomerId();
					this.customerName = activity.getActivityWorkPackage().getProductBuild().getProductVersion().getProductMaster().getCustomer().getCustomerName();
					}
			}
		}
	}

	public String getId() {
		return id;
	}

	public void setId(String _id) {
		this.id = _id;
	}


	public String get_class() {
		return _class;
	}

	public void set_class(String _class) {
		this._class = _class;
	}

	
	public Integer getActivityTypeId() {
		return activityTypeId;
	}

	public void setActivityTypeId(Integer activityTypeId) {
		this.activityTypeId = activityTypeId;
	}

	
	public Integer getActivityId() {
		return activityId;
	}

	public void setActivityId(Integer activityId) {
		this.activityId = activityId;
	}

	
	public Integer getChangeRequestTypeId() {
		return changeRequestTypeId;
	}

	public void setChangeRequestTypeId(Integer changeRequestTypeId) {
		this.changeRequestTypeId = changeRequestTypeId;
	}

	
	public Integer getChangeRequestId() {
		return changeRequestId;
	}

	public void setChangeRequestId(Integer changeRequestId) {
		this.changeRequestId = changeRequestId;
	}

	public Integer getChangeRequestMappingId() {
		return changeRequestMappingId;
	}

	public void setChangeRequestMappingId(Integer changeRequestMappingId) {
		this.changeRequestMappingId = changeRequestMappingId;
	}

	public String getActivityName() {
		return activityName;
	}

	public void setActivityName(String activityName) {
		this.activityName = activityName;
	}

	public String getChangeRequestName() {
		return changeRequestName;
	}

	public void setChangeRequestName(String changeRequestName) {
		this.changeRequestName = changeRequestName;
	}

	public Integer getActivityWorkpackageId() {
		return activityWorkpackageId;
	}

	public void setActivityWorkpackageId(Integer activityWorkpackageId) {
		this.activityWorkpackageId = activityWorkpackageId;
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

	public String getActivityWorkPackageName() {
		return activityWorkPackageName;
	}

	public void setActivityWorkPackageName(String activityWorkPackageName) {
		this.activityWorkPackageName = activityWorkPackageName;
	}
	
	
	
}
