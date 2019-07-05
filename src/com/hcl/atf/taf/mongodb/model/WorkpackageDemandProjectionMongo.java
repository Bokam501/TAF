package com.hcl.atf.taf.mongodb.model;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.hcl.atf.taf.model.WorkPackageDemandProjection;

@Document(collection = "workpackagedemandprojection")
public class WorkpackageDemandProjectionMongo {
	
	@Id
	private String id;
	private String _class;
	private Integer wpDemandProjectionId;
	private Float resourceCount;
	private Date workDate;
	
	private Integer workpackageId;
	private String workPackageName;
	
	private Integer shiftId;
	private String shiftName;
	
	private Integer skillId;
	private String skillName;
	
	private Integer userRoleId;
	private String userRoleName;
	private Date demandRaisedOn;
	
	private Integer userTypeId;
	private String userTypeName;
	
	private Integer demandRaisedByUserId;
	private String demandRaisedByUserName;
	
	private Integer workWeek;
	private Integer workYear;
	
	private String demandMode;
	private Long groupDemandId;
	
	
	private Integer productId;
	private String productName;
	private Integer versionId;
	private String versionName;
	private Integer buildId;
	private String buildname;
	
	private Integer testFactoryId;
	private String testFactoryName;
	private Integer testCentersId;
	private String testCentersName;
	private Integer customerId;
	private String customerName;
	
	public WorkpackageDemandProjectionMongo(){
			
	}
	
	public WorkpackageDemandProjectionMongo(WorkPackageDemandProjection workPackageDemandProjection) {
		
		if(workPackageDemandProjection!=null){
			
			this.id = workPackageDemandProjection.getWpDemandProjectionId()+"";
			this.wpDemandProjectionId=workPackageDemandProjection.getWpDemandProjectionId();
			this.demandRaisedOn = workPackageDemandProjection.getDemandRaisedOn();
			this.workWeek = workPackageDemandProjection.getWorkWeek();
			this.workYear = workPackageDemandProjection.getWorkYear();
			this.demandMode = workPackageDemandProjection.getDemandMode();
			this.groupDemandId = workPackageDemandProjection.getGroupDemandId();
			this.resourceCount=workPackageDemandProjection.getResourceCount();
			this.workDate=workPackageDemandProjection.getWorkDate();
			
			if(workPackageDemandProjection.getWorkShiftMaster() != null){
				this.shiftId = workPackageDemandProjection.getWorkShiftMaster().getShiftId();
				this.shiftName = workPackageDemandProjection.getWorkShiftMaster().getShiftName();
			}
			
			if(workPackageDemandProjection.getSkill() != null){
				this.skillId = workPackageDemandProjection.getSkill().getSkillId();
				this.skillName = workPackageDemandProjection.getSkill().getSkillName();
			}
			
			if(workPackageDemandProjection.getUserRole() != null){
				this.userRoleId= workPackageDemandProjection.getUserRole().getUserRoleId();
				this.userRoleName = workPackageDemandProjection.getUserRole().getRoleLabel();
			}
			
			
			if(workPackageDemandProjection.getUserTypeMasterNew()!=null){
				this.userTypeId = workPackageDemandProjection.getUserTypeMasterNew().getUserTypeId();
				this.userTypeName = workPackageDemandProjection.getUserTypeMasterNew().getUserTypeLabel();
			}
			
			if(workPackageDemandProjection.getDemandRaisedByUser() !=null){
				this.demandRaisedByUserId = workPackageDemandProjection.getDemandRaisedByUser().getUserId();
				this.demandRaisedByUserName = workPackageDemandProjection.getDemandRaisedByUser().getLoginId();
			}
			
			
			if(workPackageDemandProjection.getWorkPackage()!=null){
				this.workpackageId = workPackageDemandProjection.getWorkPackage().getWorkPackageId();
				this.workPackageName = workPackageDemandProjection.getWorkPackage().getName();
				
				if(workPackageDemandProjection.getWorkPackage().getProductBuild()!=null && workPackageDemandProjection.getWorkPackage().getProductBuild().getProductVersion()!=null && workPackageDemandProjection.getWorkPackage().getProductBuild().getProductVersion().getProductMaster()!=null){
				this.productId = workPackageDemandProjection.getWorkPackage().getProductBuild().getProductVersion().getProductMaster().getProductId();
				this.productName = workPackageDemandProjection.getWorkPackage().getProductBuild().getProductVersion().getProductMaster().getProductName();
				}
				if(workPackageDemandProjection.getWorkPackage().getProductBuild()!=null){
					this.buildId = workPackageDemandProjection.getWorkPackage().getProductBuild().getProductBuildId();
					this.buildname = workPackageDemandProjection.getWorkPackage().getProductBuild().getBuildname();
				}
				if(workPackageDemandProjection.getWorkPackage().getProductBuild()!=null && workPackageDemandProjection.getWorkPackage().getProductBuild().getProductVersion()!=null && workPackageDemandProjection.getWorkPackage().getProductBuild().getProductVersion()!=null){
				this.versionId = workPackageDemandProjection.getWorkPackage().getProductBuild().getProductVersion().getProductVersionListId();
				this.versionName = workPackageDemandProjection.getWorkPackage().getProductBuild().getProductVersion().getProductVersionName();
				}
				if(workPackageDemandProjection.getWorkPackage().getProductBuild()!=null && workPackageDemandProjection.getWorkPackage().getProductBuild().getProductVersion()!=null && workPackageDemandProjection.getWorkPackage().getProductBuild().getProductVersion().getProductMaster().getTestFactory()!=null){
				this.testFactoryId = workPackageDemandProjection.getWorkPackage().getProductBuild().getProductVersion().getProductMaster().getTestFactory().getTestFactoryId();
				this.testFactoryName = workPackageDemandProjection.getWorkPackage().getProductBuild().getProductVersion().getProductMaster().getTestFactory().getTestFactoryName();
				this.testCentersId=workPackageDemandProjection.getWorkPackage().getProductBuild().getProductVersion().getProductMaster().getTestFactory().getTestFactoryLab().getTestFactoryLabId();
				this.testCentersName=workPackageDemandProjection.getWorkPackage().getProductBuild().getProductVersion().getProductMaster().getTestFactory().getTestFactoryLab().getTestFactoryLabName();
				
				}
				if(workPackageDemandProjection.getWorkPackage().getProductBuild()!=null && workPackageDemandProjection.getWorkPackage().getProductBuild().getProductVersion()!=null && workPackageDemandProjection.getWorkPackage().getProductBuild().getProductVersion().getProductMaster().getCustomer()!=null){
				this.customerId = workPackageDemandProjection.getWorkPackage().getProductBuild().getProductVersion().getProductMaster().getCustomer().getCustomerId();
				this.customerName = workPackageDemandProjection.getWorkPackage().getProductBuild().getProductVersion().getProductMaster().getCustomer().getCustomerName();
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

	public Integer getWpDemandProjectionId() {
		return wpDemandProjectionId;
	}

	public void setWpDemandProjectionId(Integer wpDemandProjectionId) {
		this.wpDemandProjectionId = wpDemandProjectionId;
	}
	

	public Float getResourceCount() {
		return resourceCount;
	}

	public void setResourceCount(Float resourceCount) {
		this.resourceCount = resourceCount;
	}

	public Date getWorkDate() {
		return workDate;
	}

	public void setWorkDate(Date workDate) {
		this.workDate = workDate;
	}

	public Integer getWorkpackageId() {
		return workpackageId;
	}

	public void setWorkpackageId(Integer workpackageId) {
		this.workpackageId = workpackageId;
	}

	public String getWorkPackageName() {
		return workPackageName;
	}

	public void setWorkPackageName(String workPackageName) {
		this.workPackageName = workPackageName;
	}

	public Integer getShiftId() {
		return shiftId;
	}

	public void setShiftId(Integer shiftId) {
		this.shiftId = shiftId;
	}

	public String getShiftName() {
		return shiftName;
	}

	public void setShiftName(String shiftName) {
		this.shiftName = shiftName;
	}

	public Integer getSkillId() {
		return skillId;
	}

	public void setSkillId(Integer skillId) {
		this.skillId = skillId;
	}

	public String getSkillName() {
		return skillName;
	}

	public void setSkillName(String skillName) {
		this.skillName = skillName;
	}

	public Integer getUserRoleId() {
		return userRoleId;
	}

	public void setUserRoleId(Integer userRoleId) {
		this.userRoleId = userRoleId;
	}

	public String getUserRoleName() {
		return userRoleName;
	}

	public void setUserRoleName(String userRoleName) {
		this.userRoleName = userRoleName;
	}

	public Date getDemandRaisedOn() {
		return demandRaisedOn;
	}

	public void setDemandRaisedOn(Date demandRaisedOn) {
		this.demandRaisedOn = demandRaisedOn;
	}

	public Integer getUserTypeId() {
		return userTypeId;
	}

	public void setUserTypeId(Integer userTypeId) {
		this.userTypeId = userTypeId;
	}

	public String getUserTypeName() {
		return userTypeName;
	}

	public void setUserTypeName(String userTypeName) {
		this.userTypeName = userTypeName;
	}

	public Integer getDemandRaisedByUserId() {
		return demandRaisedByUserId;
	}

	public void setDemandRaisedByUserId(Integer demandRaisedByUserId) {
		this.demandRaisedByUserId = demandRaisedByUserId;
	}

	public String getDemandRaisedByUserName() {
		return demandRaisedByUserName;
	}

	public void setDemandRaisedByUserName(String demandRaisedByUserName) {
		this.demandRaisedByUserName = demandRaisedByUserName;
	}

	public Integer getWorkWeek() {
		return workWeek;
	}

	public void setWorkWeek(Integer workWeek) {
		this.workWeek = workWeek;
	}

	public Integer getWorkYear() {
		return workYear;
	}

	public void setWorkYear(Integer workYear) {
		this.workYear = workYear;
	}

	public String getDemandMode() {
		return demandMode;
	}

	public void setDemandMode(String demandMode) {
		this.demandMode = demandMode;
	}

	public Long getGroupDemandId() {
		return groupDemandId;
	}

	public void setGroupDemandId(Long groupDemandId) {
		this.groupDemandId = groupDemandId;
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


	

}
