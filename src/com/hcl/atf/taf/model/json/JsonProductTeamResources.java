package com.hcl.atf.taf.model.json;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.hcl.atf.taf.controller.utilities.DateUtility;
import com.hcl.atf.taf.model.DimensionMaster;
import com.hcl.atf.taf.model.ProductMaster;
import com.hcl.atf.taf.model.ProductTeamResources;
import com.hcl.atf.taf.model.UserList;
import com.hcl.atf.taf.model.UserRoleMaster;

public class JsonProductTeamResources {
	@JsonProperty
	private Integer productTeamResourceId;
	@JsonProperty
	private String fromDate;
	@JsonProperty
	private String toDate;
	@JsonProperty
	private String createdDate;
	@JsonProperty
	private String modifiedDate;
	@JsonProperty
	private Integer status;
	@JsonProperty
	private String remarks;
	@JsonProperty
	private Integer userId;
	@JsonProperty
	private String loginId;
	@JsonProperty
	private Integer productId;
	@JsonProperty
	private Integer productSpecificUserRoleId;
	@JsonProperty
	private String productSpecificUserRoleName;
	@JsonProperty
	private Integer userDefaultRoleId;
	@JsonProperty
	private String userDefaultRoleName;
	@JsonProperty
	private Integer dimensionId;
	@JsonProperty
	private String competencyName;
	@JsonProperty
	private String modifiedField;
	@JsonProperty
	private String 	modifiedFieldTitle;
	@JsonProperty
	private String	oldFieldValue;
	@JsonProperty	
	private String modifiedFieldValue;
	
	private Float percentageofallocation;
	
	
	public JsonProductTeamResources(){
		
	}
	
	public JsonProductTeamResources(ProductTeamResources productTeamResource){
		this.remarks = productTeamResource.getRemarks();
		this.percentageofallocation = productTeamResource.getPercentageofallocation();
		if(productTeamResource.getProductTeamResourceId()!=null){
			this.productTeamResourceId = productTeamResource.getProductTeamResourceId();
		}
			
		if(productTeamResource.getCreatedDate()!=null){
			this.createdDate = DateUtility.dateformatWithOutTime(productTeamResource.getCreatedDate());
		}
		if(productTeamResource.getModifiedDate()!=null){
			this.modifiedDate = DateUtility.dateformatWithOutTime(productTeamResource.getModifiedDate());
			}
		if(productTeamResource.getFromDate()!=null){
			this.fromDate = DateUtility.dateformatWithOutTime(productTeamResource.getFromDate());
		}
		if(productTeamResource.getToDate()!=null){
			this.toDate = DateUtility.dateformatWithOutTime(productTeamResource.getToDate());
			}
		if(productTeamResource.getStatus() != null){
			this.status = productTeamResource.getStatus();
		}
		if(productTeamResource.getProductMaster()!=null){
			this.productId=productTeamResource.getProductMaster().getProductId();
		}
		if(productTeamResource.getUserList()!=null){
			this.userId=productTeamResource.getUserList().getUserId();
			this.loginId=productTeamResource.getUserList().getLoginId();
		}
		
		if(productTeamResource.getUserList().getUserRoleMaster() != null){
			this.userDefaultRoleId = productTeamResource.getUserList().getUserRoleMaster().getUserRoleId();
			this.userDefaultRoleName = productTeamResource.getUserList().getUserRoleMaster().getRoleLabel();
		}
		
		if(productTeamResource.getProductSpecificUserRole()!=null){
			this.productSpecificUserRoleId=productTeamResource.getProductSpecificUserRole().getUserRoleId();
			this.productSpecificUserRoleName=productTeamResource.getProductSpecificUserRole().getRoleLabel();
		}

		if(productTeamResource.getDimensionMaster() != null){
			this.dimensionId=productTeamResource.getDimensionMaster().getDimensionId();
			this.competencyName=productTeamResource.getDimensionMaster().getName();
		}
		
	}
	
	@JsonIgnore
	public ProductTeamResources  getProductTeamResources(){
		ProductTeamResources productTeamRes = new ProductTeamResources();
		if(productTeamResourceId != null){
			productTeamRes.setProductTeamResourceId(productTeamResourceId);
		}
		productTeamRes.setRemarks(remarks);
		productTeamRes.setPercentageofallocation(percentageofallocation);
		
		if(this.status != null ){			
			productTeamRes.setStatus(status);			
		}else{
			productTeamRes.setStatus(0);	
		}
		
		if(this.createdDate == null || this.createdDate.trim().isEmpty()) {
			productTeamRes.setCreatedDate(DateUtility.getCurrentTime());
		} else {
		
			productTeamRes.setCreatedDate(DateUtility.dateformatWithOutTime(this.createdDate));
		}
		productTeamRes.setModifiedDate(DateUtility.getCurrentTime());
		
		if(this.fromDate == null || this.fromDate.trim().isEmpty()) {
			productTeamRes.setFromDate(DateUtility.getCurrentTime());
		} else {
		
			productTeamRes.setFromDate(DateUtility.dateformatWithOutTime(this.fromDate));
		}
		if(this.toDate == null || this.toDate.trim().isEmpty()) {
			productTeamRes.setToDate(DateUtility.getCurrentTime());
		} else {
		
			productTeamRes.setToDate(DateUtility.dateformatWithOutTime(this.toDate));
		}
		
		
	    if(this.productId!=null){
			ProductMaster product = new ProductMaster();
			product.setProductId(productId);
			productTeamRes.setProductMaster(product);
		}

	    if(this.userId!=null){
			UserList user = new UserList();
			user.setUserId(userId);
			productTeamRes.setUserList(user);
		}

	    if(this.productSpecificUserRoleId != null){
			UserRoleMaster userRole = new UserRoleMaster();
			userRole.setUserRoleId(productSpecificUserRoleId);
			productTeamRes.setProductSpecificUserRole(userRole);
		}
	    
	    if(this.dimensionId != null){
			DimensionMaster dimensionMaster = new DimensionMaster();
			dimensionMaster.setDimensionId(dimensionId);
			productTeamRes.setDimensionMaster(dimensionMaster);
		}
		return productTeamRes;
		
	}
	
	public Integer getProductTeamResourceId() {
		return productTeamResourceId;
	}
	public void setProductTeamResourceId(
			Integer productTeamResourceId) {
		this.productTeamResourceId = productTeamResourceId;
	}
	public String getFromDate() {
		return fromDate;
	}
	public void setFromDate(String fromDate) {
		this.fromDate = fromDate;
	}
	public String getToDate() {
		return toDate;
	}
	public void setToDate(String toDate) {
		this.toDate = toDate;
	}
	public String getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
	}
	public String getModifiedDate() {
		return modifiedDate;
	}
	public void setModifiedDate(String modifiedDate) {
		this.modifiedDate = modifiedDate;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	public Integer getUserId() {
		return userId;
	}
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	public Integer getProductId() {
		return productId;
	}
	public void setProductId(Integer productId) {
		this.productId = productId;
	}
	
	public String getLoginId() {
		return loginId;
	}
	public void setLoginId(String loginId) {
		this.loginId = loginId;
	}

	public Integer getProductSpecificUserRoleId() {
		return productSpecificUserRoleId;
	}

	public void setProductSpecificUserRoleId(Integer productSpecificUserRoleId) {
		this.productSpecificUserRoleId = productSpecificUserRoleId;
	}

	public String getProductSpecificUserRoleName() {
		return productSpecificUserRoleName;
	}

	public void setProductSpecificUserRoleName(String productSpecificUserRoleName) {
		this.productSpecificUserRoleName = productSpecificUserRoleName;
	}

	public Integer getUserDefaultRoleId() {
		return userDefaultRoleId;
	}

	public void setUserDefaultRoleId(Integer userDefaultRoleId) {
		this.userDefaultRoleId = userDefaultRoleId;
	}

	public String getUserDefaultRoleName() {
		return userDefaultRoleName;
	}

	public void setUserDefaultRoleName(String userDefaultRoleName) {
		this.userDefaultRoleName = userDefaultRoleName;
	}

	public Integer getDimensionId() {
		return dimensionId;
	}

	public void setDimensionId(Integer dimensionId) {
		this.dimensionId = dimensionId;
	}

	public String getCompetencyName() {
		return competencyName;
	}

	public void setCompetencyName(String competencyName) {
		this.competencyName = competencyName;
	}

	public Float getPercentageofallocation() {
		return percentageofallocation;
	}

	public void setPercentageofallocation(Float percentageofallocation) {
		this.percentageofallocation = percentageofallocation;
	}

	public String getModifiedField() {
		return modifiedField;
	}

	public void setModifiedField(String modifiedField) {
		this.modifiedField = modifiedField;
	}

	public String getModifiedFieldTitle() {
		return modifiedFieldTitle;
	}

	public void setModifiedFieldTitle(String modifiedFieldTitle) {
		this.modifiedFieldTitle = modifiedFieldTitle;
	}

	public String getOldFieldValue() {
		return oldFieldValue;
	}

	public void setOldFieldValue(String oldFieldValue) {
		this.oldFieldValue = oldFieldValue;
	}

	public String getModifiedFieldValue() {
		return modifiedFieldValue;
	}

	public void setModifiedFieldValue(String modifiedFieldValue) {
		this.modifiedFieldValue = modifiedFieldValue;
	}
	
}
