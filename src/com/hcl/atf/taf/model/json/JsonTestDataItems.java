package com.hcl.atf.taf.model.json;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.hcl.atf.taf.controller.utilities.DateUtility;
import com.hcl.atf.taf.model.ProductMaster;
import com.hcl.atf.taf.model.TestDataItemValues;
import com.hcl.atf.taf.model.TestDataItems;
import com.hcl.atf.taf.model.TestDataPlan;


public class JsonTestDataItems implements Serializable{
	@JsonProperty
	private Integer testDataItemId;	
	@JsonProperty
	private String dataName;
	@JsonProperty
	private String type;
	@JsonProperty
	private String remarks;
	@JsonProperty
	private String createdDate;
	@JsonProperty
	private String modifiedDate;
	@JsonProperty
	private String createdBy;
	@JsonProperty
	private String productName;
	@JsonProperty
	private Integer testDataItemValueId;
	@JsonProperty
	private String values;
	@JsonProperty
	private Integer productId;
	@JsonProperty
	private Integer userId;
	@JsonProperty
	private Integer testDataPlanId;
	@JsonProperty
	private String handle;
	@JsonProperty
	private Integer isShare = 0;
	public JsonTestDataItems() {

	}

	public JsonTestDataItems(TestDataItems testDataItems){

		if(testDataItems != null){
			this.testDataItemId = testDataItems.getTestDataItemId();
			this.dataName = testDataItems.getDataName();
			this.type = testDataItems.getType();
			this.remarks = testDataItems.getRemarks();

			if (testDataItems.getCreatedDate() != null) {
				this.createdDate = DateUtility.sdfDateformatWithOutTime(testDataItems.getCreatedDate());

			}
			if (testDataItems.getModifiedDate() != null) {
				this.modifiedDate = DateUtility.sdfDateformatWithOutTime(testDataItems.getModifiedDate());
			}
		
			if(testDataItems.getProductMaster() !=  null){
				this.productId = testDataItems.getProductMaster().getProductId();
				this.productName = testDataItems.getProductMaster().getProductName();
			} 
			if(testDataItems.getUserlist() != null){
				this.createdBy = testDataItems.getUserlist().getLoginId();
			}
					
			if(testDataItems.getGroupName() != null && testDataItems.getGroupName() != ""){
				handle = testDataItems.getGroupName();
			}
			if(testDataItems.getIsShare() != null){

				isShare = testDataItems.getIsShare();
			}
		}
	}

	public JsonTestDataItems(TestDataItems testDataItems,boolean flag){

		if(testDataItems != null){
			this.testDataItemId = testDataItems.getTestDataItemId();
			this.dataName = testDataItems.getDataName();
			this.type = testDataItems.getType();
			this.remarks = testDataItems.getRemarks();
			if(testDataItems.getGroupName() != null && testDataItems.getGroupName() != ""){
				handle = testDataItems.getGroupName();
			}
			int count = 0 ;
			values = "";
			Set<TestDataItemValues> testDataItemValSet = testDataItems.getTestDataItemsValSet();
			if(testDataItemValSet != null && !testDataItemValSet.isEmpty()){

				if(testDataItemValSet .size()>0){
					for(TestDataItemValues testDataItemVal :testDataItemValSet){
						if(testDataItemVal.getValues() != null && testDataItemVal.getValues() != ""){
							if(count++ >0){
								values = values +",";
							}
							values = values + testDataItemVal.getValues();
						}

					}
				}

			}
		}
	}

	public JsonTestDataItems(TestDataItemValues testDataItemvalues){

		if(testDataItemvalues != null){
			this.testDataItemValueId = testDataItemvalues.getTestDataValueId();
			this.values = testDataItemvalues.getValues();

		}
		if (testDataItemvalues.getCreatedDate() != null) {
			this.createdDate = DateUtility.sdfDateformatWithOutTime(testDataItemvalues.getCreatedDate());

		}

		if (testDataItemvalues.getTestDataItems() != null) {
			this.testDataItemId = testDataItemvalues.getTestDataItems().getTestDataItemId();

		}

		if(testDataItemvalues.getTestDataPlan() != null){
			testDataPlanId = testDataItemvalues.getTestDataPlan().getTestDataPlanId();
		}
	}


	@JsonIgnore
	public TestDataItems getTestDataItems(){
		TestDataItems testDataItems = new TestDataItems();
		if(testDataItemId != null){
			testDataItems.setTestDataItemId(testDataItemId);
		}
		if(createdDate != null ){
			testDataItems.setCreatedDate(DateUtility.dateFormatWithOutSeconds(createdDate));
		}else{
			testDataItems.setCreatedDate(new Date());
		}

		if(modifiedDate != null && modifiedDate != "" ){
			testDataItems.setModifiedDate(DateUtility.dateFormatWithOutSeconds(modifiedDate));	
		}else{
			testDataItems.setModifiedDate(new Date());
		}

	
		if(productId != null){
			ProductMaster productMaster = new ProductMaster();
			productMaster.setProductId(productId);
			testDataItems.setProductMaster(productMaster);
		}
		testDataItems.setDataName(dataName);
		testDataItems.setRemarks(remarks);
		testDataItems.setType(type);
		if(handle == null || handle == ""){
			handle = "UITestdata";
		}
		testDataItems.setGroupName(handle);
		if(isShare == null  ){
			isShare = 0 ;

		}
		testDataItems.setIsShare(isShare);

		return testDataItems;
	}
	@JsonIgnore
	public TestDataItemValues getTestDataItemValues(){
		TestDataItemValues testDataItemValues = new TestDataItemValues();
		TestDataItems testDataItem = new TestDataItems();
		if(testDataItemId != null){
			testDataItem.setTestDataItemId(testDataItemId);
			testDataItemValues.setTestDataItems(testDataItem);
		}
		if(createdDate != null && createdDate != ""){
			testDataItemValues.setCreatedDate(DateUtility.dateFormatWithOutSeconds(createdDate));
		}else{
			testDataItemValues.setCreatedDate(new Date());
		}
		testDataItemValues.setValues(values);
		testDataItemValues.setTestDataValueId(testDataItemValueId);
		TestDataPlan  tdPlan = new TestDataPlan();
		tdPlan.setTestDataPlanId(testDataPlanId);
		testDataItemValues.setTestDataPlan(tdPlan);
		return testDataItemValues;
	}
	public Integer getTestDataItemId() {
		return testDataItemId;
	}

	public void setTestDataItemId(Integer testDataItemId) {
		this.testDataItemId = testDataItemId;
	}

	public String getDataName() {
		return dataName;
	}

	public void setDataName(String dataName) {
		this.dataName = dataName;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
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



	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}



	public Integer getTestDataItemValueId() {
		return testDataItemValueId;
	}

	public void setTestDataItemValueId(Integer testDataItemValueId) {
		this.testDataItemValueId = testDataItemValueId;
	}

	public String getValues() {
		return values;
	}

	public void setValues(String values) {
		this.values = values;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public Integer getTestDataPlanId() {
		return testDataPlanId;
	}

	public void setTestDataPlanId(Integer testDataPlanId) {
		this.testDataPlanId = testDataPlanId;
	}

	public Integer getIsShare() {
		return isShare;
	}

	public void setIsShare(Integer isShare) {
		this.isShare = isShare;
	}

	public String getHandle() {
		return handle;
	}

	public void setHandle(String handle) {
		this.handle = handle;
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

}
