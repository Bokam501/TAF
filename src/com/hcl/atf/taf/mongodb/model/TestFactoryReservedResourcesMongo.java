package com.hcl.atf.taf.mongodb.model;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.hcl.atf.taf.model.TestFactoryResourceReservation;

@Document(collection = "workpackageresourcereservation")
public class TestFactoryReservedResourcesMongo {
	
	@Id
	private String id;
	private String _class;
	private Integer resourceReservationId;
	
	private Integer workpackageId;
	private String workPackageName;
	private Date reservationDate;
	
	private Integer shiftId;
	private String shiftName;
	
	private Integer blockedUserId;
	private String blockedUserName;
	
	
	private Integer reservationActionUserId;
	private String reservationActionUserName;
	private Date reservationActionDate;
	private Integer userRoleId;
	private String userRoleName;
	private Integer skillId;
	private String skillName;
	private Integer reservationPercentage;
	private Integer reservationWeek;
	private Integer reservationYear;
	private String reservationMode;
	
	private Integer userTypeId;
	private String userTypeName;
	private Long groupReservationId;
	
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
	
	public TestFactoryReservedResourcesMongo(){
			
	}
	
	public TestFactoryReservedResourcesMongo(TestFactoryResourceReservation testFactoryResourceReservation) {
		
		if(testFactoryResourceReservation!=null){
			
			this.id = testFactoryResourceReservation.getResourceReservationId()+"";
			this.resourceReservationId=testFactoryResourceReservation.getResourceReservationId();
			this.reservationActionDate = testFactoryResourceReservation.getReservationActionDate();
			this.reservationWeek = testFactoryResourceReservation.getReservationWeek();
			this.reservationYear = testFactoryResourceReservation.getReservationYear();
			this.reservationMode = testFactoryResourceReservation.getReservationMode();
			this.groupReservationId = testFactoryResourceReservation.getGroupReservationId();
			this.reservationPercentage=testFactoryResourceReservation.getReservationPercentage();
			this.reservationDate=testFactoryResourceReservation.getReservationDate();
			
			if(testFactoryResourceReservation.getBlockedUser() != null){
				this.blockedUserId = testFactoryResourceReservation.getBlockedUser().getUserId();
				this.blockedUserName = testFactoryResourceReservation.getBlockedUser().getLoginId();
					
			}
			
			
			if(testFactoryResourceReservation.getShift() != null){
				this.shiftId = testFactoryResourceReservation.getShift().getShiftId();
				this.shiftName = testFactoryResourceReservation.getShift().getShiftName();
			}
			
			if(testFactoryResourceReservation.getSkill() != null){
				this.skillId = testFactoryResourceReservation.getSkill().getSkillId();
				this.skillName = testFactoryResourceReservation.getSkill().getSkillName();
			}
			
			if(testFactoryResourceReservation.getUserRole() != null){
				this.userRoleId= testFactoryResourceReservation.getUserRole().getUserRoleId();
				this.userRoleName = testFactoryResourceReservation.getUserRole().getRoleLabel();
			}
			
			
			if(testFactoryResourceReservation.getUserType()!=null){
				this.userTypeId = testFactoryResourceReservation.getUserType().getUserTypeId();
				this.userTypeName = testFactoryResourceReservation.getUserType().getUserTypeLabel();
			}
			
			if(testFactoryResourceReservation.getReservationActionUser() !=null){
				this.reservationActionUserId = testFactoryResourceReservation.getReservationActionUser().getUserId();
				this.reservationActionUserName = testFactoryResourceReservation.getReservationActionUser().getLoginId();
			}
			
			
			if(testFactoryResourceReservation.getWorkPackage()!=null){
				this.workpackageId = testFactoryResourceReservation.getWorkPackage().getWorkPackageId();
				this.workPackageName = testFactoryResourceReservation.getWorkPackage().getName();
				
				if(testFactoryResourceReservation.getWorkPackage().getProductBuild()!=null && testFactoryResourceReservation.getWorkPackage().getProductBuild().getProductVersion()!=null && testFactoryResourceReservation.getWorkPackage().getProductBuild().getProductVersion().getProductMaster()!=null){
				this.productId = testFactoryResourceReservation.getWorkPackage().getProductBuild().getProductVersion().getProductMaster().getProductId();
				this.productName = testFactoryResourceReservation.getWorkPackage().getProductBuild().getProductVersion().getProductMaster().getProductName();
				}
				if(testFactoryResourceReservation.getWorkPackage().getProductBuild()!=null){
					this.buildId = testFactoryResourceReservation.getWorkPackage().getProductBuild().getProductBuildId();
					this.buildname = testFactoryResourceReservation.getWorkPackage().getProductBuild().getBuildname();
				}
				if(testFactoryResourceReservation.getWorkPackage().getProductBuild()!=null && testFactoryResourceReservation.getWorkPackage().getProductBuild().getProductVersion()!=null && testFactoryResourceReservation.getWorkPackage().getProductBuild().getProductVersion()!=null){
				this.versionId = testFactoryResourceReservation.getWorkPackage().getProductBuild().getProductVersion().getProductVersionListId();
				this.versionName = testFactoryResourceReservation.getWorkPackage().getProductBuild().getProductVersion().getProductVersionName();
				}
				if(testFactoryResourceReservation.getWorkPackage().getProductBuild()!=null && testFactoryResourceReservation.getWorkPackage().getProductBuild().getProductVersion()!=null && testFactoryResourceReservation.getWorkPackage().getProductBuild().getProductVersion().getProductMaster().getTestFactory()!=null){
				this.testFactoryId = testFactoryResourceReservation.getWorkPackage().getProductBuild().getProductVersion().getProductMaster().getTestFactory().getTestFactoryId();
				this.testFactoryName = testFactoryResourceReservation.getWorkPackage().getProductBuild().getProductVersion().getProductMaster().getTestFactory().getTestFactoryName();
				this.testCentersId=testFactoryResourceReservation.getWorkPackage().getProductBuild().getProductVersion().getProductMaster().getTestFactory().getTestFactoryLab().getTestFactoryLabId();
				this.testCentersName=testFactoryResourceReservation.getWorkPackage().getProductBuild().getProductVersion().getProductMaster().getTestFactory().getTestFactoryLab().getTestFactoryLabName();
				
				}
				if(testFactoryResourceReservation.getWorkPackage().getProductBuild()!=null && testFactoryResourceReservation.getWorkPackage().getProductBuild().getProductVersion()!=null && testFactoryResourceReservation.getWorkPackage().getProductBuild().getProductVersion().getProductMaster().getCustomer()!=null){
				this.customerId = testFactoryResourceReservation.getWorkPackage().getProductBuild().getProductVersion().getProductMaster().getCustomer().getCustomerId();
				this.customerName = testFactoryResourceReservation.getWorkPackage().getProductBuild().getProductVersion().getProductMaster().getCustomer().getCustomerName();
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

	public Integer getResourceReservationId() {
		return resourceReservationId;
	}

	public void setResourceReservationId(Integer resourceReservationId) {
		this.resourceReservationId = resourceReservationId;
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

	public Date getReservationDate() {
		return reservationDate;
	}

	public void setReservationDate(Date reservationDate) {
		this.reservationDate = reservationDate;
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

	public Integer getBlockedUserId() {
		return blockedUserId;
	}

	public void setBlockedUserId(Integer blockedUserId) {
		this.blockedUserId = blockedUserId;
	}

	public String getBlockedUserName() {
		return blockedUserName;
	}

	public void setBlockedUserName(String blockedUserName) {
		this.blockedUserName = blockedUserName;
	}

	public Integer getReservationActionUserId() {
		return reservationActionUserId;
	}

	public void setReservationActionUserId(Integer reservationActionUserId) {
		this.reservationActionUserId = reservationActionUserId;
	}

	public String getReservationActionUserName() {
		return reservationActionUserName;
	}

	public void setReservationActionUserName(String reservationActionUserName) {
		this.reservationActionUserName = reservationActionUserName;
	}

	public Date getReservationActionDate() {
		return reservationActionDate;
	}

	public void setReservationActionDate(Date reservationActionDate) {
		this.reservationActionDate = reservationActionDate;
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

	public Integer getReservationPercentage() {
		return reservationPercentage;
	}

	public void setReservationPercentage(Integer reservationPercentage) {
		this.reservationPercentage = reservationPercentage;
	}

	public Integer getReservationWeek() {
		return reservationWeek;
	}

	public void setReservationWeek(Integer reservationWeek) {
		this.reservationWeek = reservationWeek;
	}

	public Integer getReservationYear() {
		return reservationYear;
	}

	public void setReservationYear(Integer reservationYear) {
		this.reservationYear = reservationYear;
	}

	public String getReservationMode() {
		return reservationMode;
	}

	public void setReservationMode(String reservationMode) {
		this.reservationMode = reservationMode;
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

	public Long getGroupReservationId() {
		return groupReservationId;
	}

	public void setGroupReservationId(Long groupReservationId) {
		this.groupReservationId = groupReservationId;
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

	


	

}
