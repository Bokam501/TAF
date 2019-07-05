package com.hcl.atf.taf.model.json;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.hcl.atf.taf.controller.utilities.DateUtility;
import com.hcl.atf.taf.model.ProductMaster;
import com.hcl.atf.taf.model.TestDataPlan;

public class JsonTestDataPlan implements Serializable {
	@JsonProperty
	private Integer testDataPlanId;
	@JsonProperty
	private String testDataPlanName;
	@JsonProperty
	private String testDataPlanDescription;
	@JsonProperty
	private String createdOn;
	@JsonProperty
	private String userName;
	@JsonProperty
	private Integer createdBy;
	@JsonProperty
	private Integer productId;
	

	public JsonTestDataPlan() {

	}
	public JsonTestDataPlan(TestDataPlan testDataPlan) {
		if(testDataPlan != null){
			this.testDataPlanId = testDataPlan.getTestDataPlanId();
			this.testDataPlanName = testDataPlan.getTestDataPlanName();
			this.testDataPlanDescription = testDataPlan.getTestDataPlanDescription();
			
			if(testDataPlan.getCreatedOn() != null)
				this.createdOn = DateUtility.sdfDateformatWithOutTime(testDataPlan.getCreatedOn());
			
			if(testDataPlan.getUserlist() != null){
				this.createdBy = testDataPlan.getUserlist().getUserId();
				userName = testDataPlan.getUserlist().getLoginId();
			}

		
			if(testDataPlan.getProductMaster() != null){
				this.productId = testDataPlan.getProductMaster().getProductId();	
			}
		}	
	}
	
	@JsonIgnore
	public TestDataPlan getTestDataPlan() {
		TestDataPlan testDataPlan  = new TestDataPlan();
			testDataPlan.setTestDataPlanName(testDataPlanName);
		testDataPlan.setTestDataPlanDescription(testDataPlanDescription);
		testDataPlan.setTestDataPlanId(testDataPlanId);
		
		ProductMaster productMaster = new ProductMaster();
		productMaster.setProductId(productId);
		testDataPlan.setProductMaster(productMaster);
		if(createdOn == null || createdOn.trim().isEmpty()){
			testDataPlan.setCreatedOn(new Date());
		}else{
			testDataPlan.setCreatedOn(DateUtility.dateFormatWithOutSeconds(createdOn));
		}
		
		return testDataPlan;
		
}

	public Integer getTestDataPlanId() {
		return testDataPlanId;
	}
	public void setTestDataPlanId(Integer testDataPlanId) {
		this.testDataPlanId = testDataPlanId;
	}
	public String getTestDataPlanName() {
		return testDataPlanName;
	}
	public void setTestDataPlanName(String testDataPlanName) {
		this.testDataPlanName = testDataPlanName;
	}
	public String getTestDataPlanDescription() {
		return testDataPlanDescription;
	}
	public void setTestDataPlanDescription(String testDataPlanDescription) {
		this.testDataPlanDescription = testDataPlanDescription;
	}
	
	public Integer getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(Integer createdBy) {
		this.createdBy = createdBy;
	}
	public String getCreatedOn() {
		return createdOn;
	}
	public void setCreatedOn(String createdOn) {
		this.createdOn = createdOn;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public Integer getProductId() {
		return productId;
	}
	public void setProductId(Integer productId) {
		this.productId = productId;
	}
	

}
