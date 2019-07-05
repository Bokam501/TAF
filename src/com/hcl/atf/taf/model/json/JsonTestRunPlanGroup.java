package com.hcl.atf.taf.model.json;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.hcl.atf.taf.controller.utilities.DateUtility;
import com.hcl.atf.taf.model.ExecutionTypeMaster;
import com.hcl.atf.taf.model.ProductMaster;
import com.hcl.atf.taf.model.ProductVersionListMaster;
import com.hcl.atf.taf.model.TestRunPlanGroup;

public class JsonTestRunPlanGroup {
	@JsonProperty
	private Integer testRunPlanGroupId;
	@JsonProperty
	private String name;
	@JsonProperty
	private String description;
	@JsonProperty
	private String createdDate;
	@JsonProperty
	private String modifiedDate;
	@JsonProperty
	private Integer executionType;
	@JsonProperty
	private Integer productVersionId;
	@JsonProperty
	private String productVersionName;
	@JsonProperty
	private String modifiedField;
	@JsonProperty
	private String 	modifiedFieldTitle;
	@JsonProperty
	private String	oldFieldValue;
	@JsonProperty	
	private String modifiedFieldValue;
	@JsonProperty
	private Integer productId;
	@JsonProperty
	private String productName;
	public JsonTestRunPlanGroup(){
		
	}
	
	public JsonTestRunPlanGroup(TestRunPlanGroup testRunPlanGroup){
		this.name = testRunPlanGroup.getName();
		this.description=testRunPlanGroup.getDescription();
	
		if(testRunPlanGroup.getTestRunPlanGroupId()!=null){
			this.testRunPlanGroupId = testRunPlanGroup.getTestRunPlanGroupId();
		}
			
		if(testRunPlanGroup.getCreatedDate()!=null){
			this.createdDate = DateUtility.dateformatWithOutTime(testRunPlanGroup.getCreatedDate());
		}
		if(testRunPlanGroup.getModifiedDate()!=null){
			this.modifiedDate = DateUtility.dateformatWithOutTime(testRunPlanGroup.getModifiedDate());
			}
		if(testRunPlanGroup.getExecutionTypeMaster() != null){
			this.executionType = testRunPlanGroup.getExecutionTypeMaster().getExecutionTypeId();
		}
		if(testRunPlanGroup.getProductVersionListMaster()!=null){
			this.productVersionName=testRunPlanGroup.getProductVersionListMaster().getProductVersionName();
			this.productVersionId=testRunPlanGroup.getProductVersionListMaster().getProductVersionListId();
		}
		if(testRunPlanGroup.getProduct()!=null){
			this.productName=testRunPlanGroup.getProduct().getProductName();
			this.productId=testRunPlanGroup.getProduct().getProductId();
		}
	}

	@JsonIgnore
	public TestRunPlanGroup gettestRunPlanGroup(){
		TestRunPlanGroup testRunPlanGroup = new TestRunPlanGroup();
		
		if(testRunPlanGroupId != null){
			testRunPlanGroup.setTestRunPlanGroupId(testRunPlanGroupId);
		}
		testRunPlanGroup.setName(name);
		testRunPlanGroup.setDescription(description);
		
		if(this.createdDate == null || this.createdDate.trim().isEmpty()) {
			testRunPlanGroup.setCreatedDate(DateUtility.getCurrentTime());
		} else {
		
			testRunPlanGroup.setCreatedDate(DateUtility.dateformatWithHyphnWithOutTime(this.createdDate));
		}
		if(this.modifiedDate == null || this.modifiedDate.trim().isEmpty()) {
			testRunPlanGroup.setModifiedDate(DateUtility.getCurrentTime());
		} else {
		
			testRunPlanGroup.setModifiedDate(DateUtility.dateformatWithHyphnWithOutTime(this.modifiedDate));
		}
		if(executionType != null){
			ExecutionTypeMaster etm = new ExecutionTypeMaster();
			etm.setExecutionTypeId(executionType);
			testRunPlanGroup.setExecutionTypeMaster(etm);
		}
		
		if(productVersionId != null){
			ProductVersionListMaster productVersionListMaster = new ProductVersionListMaster();
			productVersionListMaster.setProductVersionListId(productVersionId);
			testRunPlanGroup.setProductVersionListMaster(productVersionListMaster);
		}
		if(productId != null){
			ProductMaster product = new ProductMaster();
			product.setProductId(productId);
			testRunPlanGroup.setProduct(product);
		}
		return testRunPlanGroup;
	}
	
	
	
	
	public Integer getTestRunPlanGroupId() {
		return testRunPlanGroupId;
	}

	public void setTestRunPlanGroupId(Integer testRunPlanGroupId) {
		this.testRunPlanGroupId = testRunPlanGroupId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
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
	
	public Integer getExecutionType() {
		return executionType;
	}

	public void setExecutionType(Integer executionTypeId) {
		this.executionType = executionTypeId;
	}

	public Integer getProductVersionId() {
		return productVersionId;
	}

	public void setProductVersionId(Integer productVersionId) {
		this.productVersionId = productVersionId;
	}

	public String getProductVersionName() {
		return productVersionName;
	}

	public void setProductVersionName(String productVersionName) {
		this.productVersionName = productVersionName;
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

}
