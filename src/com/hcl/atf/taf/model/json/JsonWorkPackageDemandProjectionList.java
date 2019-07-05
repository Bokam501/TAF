package com.hcl.atf.taf.model.json;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.hcl.atf.taf.controller.utilities.DateUtility;
import com.hcl.atf.taf.model.WorkPackageDemandProjection;


public class JsonWorkPackageDemandProjectionList implements java.io.Serializable{
	
	private static final long serialVersionUID = 1L;
	
	@JsonProperty	
	private Integer wpDemandProjectionId;
	@JsonProperty
	private int workPackageId;
	@JsonProperty	
	private String workPackageName;
	@JsonProperty
	private Integer shiftId;
	@JsonProperty	
	private String shiftName;
	@JsonProperty	
	private String productName;
	@JsonProperty	
	private Integer productId;
	
	@JsonProperty	
	private String workDate;
	@JsonProperty	
	private Float resourceCount;
	@JsonProperty	
	private String demandRaisedUser;
	@JsonProperty
	private Integer skillId;
	@JsonProperty	
	private String skillName;
	@JsonProperty
	private Integer userRoleId;
	@JsonProperty	
	private String userRoleName;
	
	public JsonWorkPackageDemandProjectionList() {
	}	

	public JsonWorkPackageDemandProjectionList(WorkPackageDemandProjection workPackageDemandProjection) {
		this.wpDemandProjectionId=workPackageDemandProjection.getWpDemandProjectionId();

		this.workPackageId=workPackageDemandProjection.getWorkPackage().getWorkPackageId();
		this.workPackageName=workPackageDemandProjection.getWorkPackage().getName();
		this.shiftId=workPackageDemandProjection.getWorkShiftMaster().getShiftId();
		this.shiftName=workPackageDemandProjection.getWorkShiftMaster().getShiftName();
		this.productId=workPackageDemandProjection.getWorkPackage().getProductBuild().getProductVersion().getProductMaster().getProductId();
		this.productName=workPackageDemandProjection.getWorkPackage().getProductBuild().getProductVersion().getProductMaster().getProductName();
		this.workDate=DateUtility.dateformatWithOutTime(workPackageDemandProjection.getWorkDate());
		this.resourceCount=workPackageDemandProjection.getResourceCount();
		this.demandRaisedUser=workPackageDemandProjection.getDemandRaisedByUser().getUserDisplayName();
		if (workPackageDemandProjection.getSkill() != null){
			this.skillId=workPackageDemandProjection.getSkill().getSkillId();
			this.skillName=workPackageDemandProjection.getSkill().getSkillName();
		}
		if(workPackageDemandProjection.getUserRole() != null){
		this.userRoleId=workPackageDemandProjection.getUserRole().getUserRoleId();
		this.userRoleName=workPackageDemandProjection.getUserRole().getRoleName();
		}
	}

	public int getWpDemandProjectionId() {
		return wpDemandProjectionId;
	}

	public void setWpDemandProjectionId(int wpDemandProjectionId) {
		this.wpDemandProjectionId = wpDemandProjectionId;
	}
	
	public int getWorkPackageId() {
		return workPackageId;
	}

	public void setWorkPackageId(int workPackageId) {
		this.workPackageId = workPackageId;
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

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public Integer getProductId() {
		return productId;
	}

	public void setProductId(Integer productId) {
		this.productId = productId;
	}

	public String getWorkDate() {
		return workDate;
	}

	public void setWorkDate(String workDate) {
		this.workDate = workDate;
	}

	public Float getResourceCount() {
		return resourceCount;
	}

	public void setResourceCount(Float resourceCount) {
		this.resourceCount = resourceCount;
	}

	public void setWpDemandProjectionId(Integer wpDemandProjectionId) {
		this.wpDemandProjectionId = wpDemandProjectionId;
	}

	public String getDemandRaisedUser() {
		return demandRaisedUser;
	}

	public void setDemandRaisedUser(String demandRaisedUser) {
		this.demandRaisedUser = demandRaisedUser;
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

}
