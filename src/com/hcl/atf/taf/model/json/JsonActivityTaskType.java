package com.hcl.atf.taf.model.json;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.hcl.atf.taf.model.ActivityTaskType;
import com.hcl.atf.taf.model.ProductMaster;
import com.hcl.atf.taf.model.TestFactory;

public class JsonActivityTaskType {
	
	@JsonProperty
	private Integer activityTaskTypeId;
	@JsonProperty
	private String activityTaskTypeName;
	@JsonProperty
	private String activityTaskTypeDescription;
	@JsonProperty
	private Float activityTaskTypeWeightage;
	@JsonProperty
	private Integer productId;
	@JsonProperty
	private Integer testFactoryId;
	
	public JsonActivityTaskType() {
	}

	public JsonActivityTaskType(ActivityTaskType activityTaskType) {
		this.activityTaskTypeId = activityTaskType.getActivityTaskTypeId();	
		this.activityTaskTypeName = activityTaskType.getActivityTaskTypeName();	
		this.activityTaskTypeDescription = activityTaskType.getActivityTaskTypeDescription();	
		this.activityTaskTypeWeightage = activityTaskType.getActivityTaskTypeWeightage();
		if(activityTaskType.getProduct() != null){
			this.productId = activityTaskType.getProduct().getProductId(); 
		}
		if(activityTaskType.getTestFactory() != null){
			this.testFactoryId = activityTaskType.getTestFactory().getTestFactoryId();
		}
		
}
	@JsonIgnore
	public ActivityTaskType getActivityTaskType() {
		ActivityTaskType activityTaskType= new ActivityTaskType();

		activityTaskType.setActivityTaskTypeId(activityTaskTypeId);
		activityTaskType.setActivityTaskTypeName(activityTaskTypeName);
		activityTaskType.setActivityTaskTypeDescription(activityTaskTypeDescription);
		activityTaskType.setActivityTaskTypeWeightage(activityTaskTypeWeightage);
		
		ProductMaster productMaster = null;
		if(this.productId != null && this.productId > 0){
			productMaster = new ProductMaster();
			productMaster.setProductId(this.productId);
		}
		activityTaskType.setProduct(productMaster);
		TestFactory testFactory = null;
		if(this.testFactoryId != null && this.testFactoryId > 0){
			testFactory = new TestFactory();
			testFactory.setTestFactoryId(this.testFactoryId);
		}
		activityTaskType.setTestFactory(testFactory);
		return activityTaskType;
	}

	public Integer getActivityTaskTypeId() {
		return activityTaskTypeId;
	}

	public void setActivityTaskTypeId(Integer activityTaskTypeId) {
		this.activityTaskTypeId = activityTaskTypeId;
	}

	public String getActivityTaskTypeName() {
		return activityTaskTypeName;
	}

	public void setActivityTaskTypeName(String activityTaskTypeName) {
		this.activityTaskTypeName = activityTaskTypeName;
	}

	public String getActivityTaskTypeDescription() {
		return activityTaskTypeDescription;
	}

	public void setActivityTaskTypeDescription(String activityTaskTypeDescription) {
		this.activityTaskTypeDescription = activityTaskTypeDescription;
	}

	public Float getActivityTaskTypeWeightage() {
		return activityTaskTypeWeightage;
	}

	public void setActivityTaskTypeWeightage(Float activityTaskTypeWeightage) {
		this.activityTaskTypeWeightage = activityTaskTypeWeightage;
	}

	public Integer getProductId() {
		return productId;
	}

	public void setProductId(Integer productId) {
		this.productId = productId;
	}

	public Integer getTestFactoryId() {
		return testFactoryId;
	}

	public void setTestFactoryId(Integer testFactoryId) {
		this.testFactoryId = testFactoryId;
	}


}
