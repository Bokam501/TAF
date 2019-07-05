package com.hcl.atf.taf.model.json;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.hcl.atf.taf.controller.utilities.DateUtility;
import com.hcl.atf.taf.model.ProductMaster;
import com.hcl.atf.taf.model.TestFactoryProductCoreResource;
import com.hcl.atf.taf.model.UserList;
import com.hcl.atf.taf.model.UserRoleMaster;

public class JsonTestFactoryProductCoreResource {
	@JsonProperty
	private Integer testFactoryProductCoreResourceId;
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
	public String getLoginId() {
		return loginId;
	}
	public void setLoginId(String loginId) {
		this.loginId = loginId;
	}
	@JsonProperty
	private Integer productId;
	@JsonProperty
	private Integer userRoleId;
	@JsonProperty
	private Integer userDefaultRoleId;
	@JsonProperty
	private String userDefaultRoleName;
	
	
	public JsonTestFactoryProductCoreResource(){
		
	}
	public JsonTestFactoryProductCoreResource(TestFactoryProductCoreResource productCoreResource){

		
		this.remarks=productCoreResource.getRemarks();
		if(productCoreResource.getTestFactoryProductCoreResourceId()!=null){
		this.testFactoryProductCoreResourceId=productCoreResource.getTestFactoryProductCoreResourceId();
		}
		
		
	if(productCoreResource.getCreatedDate()!=null){
		this.createdDate = DateUtility.dateformatWithOutTime(productCoreResource.getCreatedDate());
	}
	if(productCoreResource.getModifiedDate()!=null){
		this.modifiedDate = DateUtility.dateformatWithOutTime(productCoreResource.getModifiedDate());
		}
	if(productCoreResource.getFromDate()!=null){
		this.fromDate = DateUtility.dateformatWithOutTime(productCoreResource.getFromDate());
	}
	if(productCoreResource.getToDate()!=null){
		this.toDate = DateUtility.dateformatWithOutTime(productCoreResource.getToDate());
		}
	if(productCoreResource.getStatus() != null){
		this.status = productCoreResource.getStatus();
	}
	if(productCoreResource.getProductMaster()!=null){
		this.productId=productCoreResource.getProductMaster().getProductId();
	}
	if(productCoreResource.getUserList()!=null){
		this.userId=productCoreResource.getUserList().getUserId();
		this.loginId=productCoreResource.getUserList().getLoginId();
	}
	
	if(productCoreResource.getUserList().getUserRoleMaster() != null){
		this.userDefaultRoleId = productCoreResource.getUserList().getUserRoleMaster().getUserRoleId();
		this.userDefaultRoleName = productCoreResource.getUserList().getUserRoleMaster().getRoleName();
	}
	
	if(productCoreResource.getUserRole()!=null){
		this.userRoleId=productCoreResource.getUserRole().getUserRoleId();
	}

//
	}
	@JsonIgnore
	public TestFactoryProductCoreResource  getTestFactoryProductCoreResource(){
		TestFactoryProductCoreResource productCoreRes=new TestFactoryProductCoreResource();
		if(testFactoryProductCoreResourceId!=null){
			productCoreRes.setTestFactoryProductCoreResourceId(testFactoryProductCoreResourceId);
		}
		productCoreRes.setRemarks(remarks);
		
		
		if(this.status != null ){			
			productCoreRes.setStatus(status);			
		}else{
			productCoreRes.setStatus(0);	
		}
		
		if(this.createdDate == null || this.createdDate.trim().isEmpty()) {
			productCoreRes.setCreatedDate(DateUtility.getCurrentTime());
		} else {
		
			productCoreRes.setCreatedDate(DateUtility.dateformatWithOutTime(this.createdDate));
		}
		productCoreRes.setModifiedDate(DateUtility.getCurrentTime());
		
		if(this.fromDate == null || this.fromDate.trim().isEmpty()) {
			productCoreRes.setFromDate(DateUtility.getCurrentTime());
		} else {
		
			productCoreRes.setFromDate(DateUtility.dateformatWithOutTime(this.fromDate));
		}
		if(this.toDate == null || this.toDate.trim().isEmpty()) {
			productCoreRes.setToDate(DateUtility.getCurrentTime());
		} else {
		
			productCoreRes.setToDate(DateUtility.dateformatWithOutTime(this.toDate));
		}
		
		
	    if(this.productId!=null){
		ProductMaster product = new ProductMaster();
		product.setProductId(productId);
		productCoreRes.setProductMaster(product);
		}

	    if(this.userId!=null){
		UserList user = new UserList();
		user.setUserId(userId);
		productCoreRes.setUserList(user);
		}

	    if(this.userRoleId!=null){
		UserRoleMaster userRole = new UserRoleMaster();
		userRole.setUserRoleId(userRoleId);
		productCoreRes.setUserRole(userRole);
		}
		
		return productCoreRes;
		
	}
	
	public Integer getTestFactoryProductCoreResourceId() {
		return testFactoryProductCoreResourceId;
	}
	public void setTestFactoryProductCoreResourceId(
			Integer testFactoryProductCoreResourceId) {
		this.testFactoryProductCoreResourceId = testFactoryProductCoreResourceId;
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
	public Integer getUserRoleId() {
		return userRoleId;
	}
	public void setUserRoleId(Integer userRoleId) {
		this.userRoleId = userRoleId;
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

}
