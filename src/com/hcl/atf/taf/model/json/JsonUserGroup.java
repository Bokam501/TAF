package com.hcl.atf.taf.model.json;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.hcl.atf.taf.controller.utilities.DateUtility;
import com.hcl.atf.taf.model.ProductMaster;
import com.hcl.atf.taf.model.TestFactory;
import com.hcl.atf.taf.model.UserGroup;
import com.hcl.atf.taf.model.UserList;

public class JsonUserGroup {
	
	@JsonProperty
	private Integer userGroupId;
	@JsonProperty
	private String userGroupName;
	@JsonProperty
	private String description;
	@JsonProperty
	private Integer productId;
	@JsonProperty
	private String productName;
	@JsonProperty
	private Integer testFactoryId;
	@JsonProperty
	private String testFactoryName;
	@JsonProperty
	private String createdDate;
	@JsonProperty
	private Integer createdById;
	@JsonProperty
	private String createdByName;
	@JsonProperty
	private String modifiedDate;
	@JsonProperty
	private Integer modifiedById;
	@JsonProperty
	private String modifiedByName;
	@JsonProperty
	private String modifiedField;
	@JsonProperty
	private String 	modifiedFieldTitle;	
	@JsonProperty
	private String oldFieldID;
	@JsonProperty
	private String	oldFieldValue;
	@JsonProperty
	private String	modifiedFieldID;
	@JsonProperty	
	private String modifiedFieldValue;
	
	public JsonUserGroup() {
		
	}

	public JsonUserGroup(UserGroup userGroup) {
		this.userGroupId = userGroup.getUserGroupId();
		this.userGroupName = userGroup.getUserGroupName();
		this.description = userGroup.getDescription();
		if(userGroup.getProduct() != null){
			this.productId = userGroup.getProduct().getProductId();
			this.productName = userGroup.getProduct().getProductName();
		}
		if(userGroup.getTestFactory() != null){
			this.testFactoryId = userGroup.getTestFactory().getTestFactoryId();
			this.testFactoryName = userGroup.getTestFactory().getTestFactoryName();
		}
		if(userGroup.getCreatedBy() != null){
			this.createdById = userGroup.getCreatedBy().getUserId();
			this.createdByName = userGroup.getCreatedBy().getLoginId();
		}
		if(userGroup.getCreatedDate() != null){
			this.createdDate = DateUtility.dateToStringWithSeconds1(userGroup.getCreatedDate());
		}
		if(userGroup.getModifiedBy() != null){
			this.modifiedById = userGroup.getModifiedBy().getUserId();
			this.modifiedByName = userGroup.getModifiedBy().getLoginId();
		}
		if(userGroup.getModifiedDate() != null){
			this.modifiedDate = DateUtility.dateToStringWithSeconds1(userGroup.getModifiedDate());
		}
				
	}
	
	@JsonIgnore
	public UserGroup getUserGroup() {
		UserGroup userGroup = new UserGroup();
		userGroup.setUserGroupId(this.userGroupId);
		userGroup.setUserGroupName(this.userGroupName);
		userGroup.setDescription(this.description);
		if(this.testFactoryId != null && this.testFactoryId != 0){
			TestFactory testFactory = new TestFactory();
			testFactory.setTestFactoryId(this.testFactoryId);
			userGroup.setTestFactory(testFactory);
		}
		if(this.productId != null && this.productId != 0){
			ProductMaster product = new ProductMaster();
			product.setProductId(this.productId);
			userGroup.setProduct(product);
		}
		if(this.createdById != null && this.createdById != 0){
			UserList createdBy = new UserList();
			createdBy.setUserId(this.createdById);
			userGroup.setCreatedBy(createdBy);
		}
		if(this.createdDate == null || this.createdDate.trim().isEmpty()) {
			userGroup.setCreatedDate(DateUtility.getCurrentTime());
		} else {		
			userGroup.setCreatedDate(DateUtility.toFormatDate(this.createdDate));
		}
		if(this.modifiedById != null && this.modifiedById != 0){
			UserList modifiedBy = new UserList();
			modifiedBy.setUserId(this.modifiedById);
			userGroup.setModifiedBy(modifiedBy);
		}
		userGroup.setModifiedDate(DateUtility.getCurrentTime());
		
		return userGroup;
	}

	public Integer getUserGroupId() {
		return userGroupId;
	}

	public void setUserGroupId(Integer userGroupId) {
		this.userGroupId = userGroupId;
	}

	public String getUserGroupName() {
		return userGroupName;
	}

	public void setUserGroupName(String userGroupName) {
		this.userGroupName = userGroupName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
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

	public String getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
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

	public String getModifiedDate() {
		return modifiedDate;
	}

	public void setModifiedDate(String modifiedDate) {
		this.modifiedDate = modifiedDate;
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

	public String getOldFieldID() {
		return oldFieldID;
	}

	public void setOldFieldID(String oldFieldID) {
		this.oldFieldID = oldFieldID;
	}

	public String getOldFieldValue() {
		return oldFieldValue;
	}

	public void setOldFieldValue(String oldFieldValue) {
		this.oldFieldValue = oldFieldValue;
	}

	public String getModifiedFieldID() {
		return modifiedFieldID;
	}

	public void setModifiedFieldID(String modifiedFieldID) {
		this.modifiedFieldID = modifiedFieldID;
	}

	public String getModifiedFieldValue() {
		return modifiedFieldValue;
	}

	public void setModifiedFieldValue(String modifiedFieldValue) {
		this.modifiedFieldValue = modifiedFieldValue;
	}

}
