package com.hcl.atf.taf.model.json;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.hcl.atf.taf.model.ActivityGroup;
import com.hcl.atf.taf.model.ActivityMaster;
import com.hcl.atf.taf.model.ActivityType;
import com.hcl.atf.taf.model.ProductMaster;
import com.hcl.atf.taf.model.TestFactory;


public class JsonActivityMaster {
	private static final Log log = LogFactory.getLog(JsonActivityMaster.class);
	
	@JsonProperty
	private Integer activityMasterId;
	@JsonProperty
	private String activityMasterName;
	@JsonProperty
	private String description;
	@JsonProperty
	private Integer testFactoryId;
	@JsonProperty
	private String testFactoryName;
	@JsonProperty
	private Integer productId;
	@JsonProperty
	private String productName;
	@JsonProperty
	private Integer activityTypeId;
	@JsonProperty
	private String activityTypeName;
	@JsonProperty
	private Integer activityGroupId;
	@JsonProperty
	private String activityGroupName;
	@JsonProperty
	private Float weightage;
	



	public JsonActivityMaster(){	
	}
	
	
	public JsonActivityMaster(ActivityMaster activityMaster){	
		
		this.activityMasterId = activityMaster.getActivityMasterId();
		this.activityMasterName = activityMaster.getActivityMasterName();
		this.description = activityMaster.getDescription();
		this.weightage = activityMaster.getWeightage();
		if(activityMaster.getTestFactory() != null){
			this.testFactoryId = activityMaster.getTestFactory().getTestFactoryId();
			this.testFactoryName = activityMaster.getTestFactory().getTestFactoryName();
		}
		if (activityMaster.getProductMaster() != null) {
			this.productId = activityMaster.getProductMaster().getProductId();
			this.productName = activityMaster.getProductMaster().getProductName();		
		}
		if (activityMaster.getActivityType() != null) {
			this.activityTypeId = activityMaster.getActivityType().getActivityTypeId();
			this.activityTypeName = activityMaster.getActivityType().getActivityTypeName();	
			
		}
		if (activityMaster.getActivityType().getActivityGroup() != null) {
			this.activityGroupId = activityMaster.getActivityType().getActivityGroup().getActivityGroupId();
			this.activityGroupName = activityMaster.getActivityType().getActivityGroup().getActivityGroupName();	
		}
	}
	
	@JsonIgnore
	public ActivityMaster getActivityMaster() {
		ActivityMaster activityMaster = new ActivityMaster();
		activityMaster.setActivityMasterId(activityMasterId);
		activityMaster.setActivityMasterName(activityMasterName);
		activityMaster.setDescription(description);
		if(this.weightage != null){
			activityMaster.setWeightage(weightage);
		}else{
			activityMaster.setWeightage(1.0f);
		}
		
		if(this.testFactoryId != null && testFactoryId > 0){
			TestFactory testFactory = new TestFactory();
			testFactory.setTestFactoryId(this.testFactoryId);
			activityMaster.setTestFactory(testFactory);
		}
		if (this.productId != null && productId > 0) {
			ProductMaster productMaster = new ProductMaster();		
			productMaster.setProductId(productId);
			activityMaster.setProductMaster(productMaster);
		}
		if (this.activityTypeId != null) {
			ActivityType activityType = new ActivityType();		
			activityType.setActivityTypeId(activityTypeId);
			activityType.setActivityTypeName(activityTypeName);
			activityMaster.setActivityType(activityType);
		}
		if (this.activityGroupId != null) {
			ActivityGroup activityGroup = new ActivityGroup();		
			activityGroup.setActivityGroupId(activityGroupId);
			activityGroup.setActivityGroupName(activityGroupName);
			activityMaster.getActivityType().setActivityGroup(activityGroup);
		}
		return activityMaster;
	}
	
	


	public Integer getActivityMasterId() {
		return activityMasterId;
	}


	public void setActivityMasterId(Integer activityMasterId) {
		this.activityMasterId = activityMasterId;
	}


	public String getActivityMasterName() {
		return activityMasterName;
	}


	public void setActivityMasterName(String activityMasterName) {
		this.activityMasterName = activityMasterName;
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


	public void setProductId(Integer productId) {
		this.productId = productId;
	}


	public String getProductName() {
		return productName;
	}


	public void setProductName(String productName) {
		this.productName = productName;
	}

	public Integer getActivityTypeId() {
		return activityTypeId;
	}

	public void setActivityTypeId(Integer activityTypeId) {
		this.activityTypeId = activityTypeId;
	}


	public String getActivityTypeName() {
		return activityTypeName;
	}


	public void setActivityTypeName(String activityTypeName) {
		this.activityTypeName = activityTypeName;
	}


	public Integer getActivityGroupId() {
		return activityGroupId;
	}


	public void setActivityGroupId(Integer activityGroupId) {
		this.activityGroupId = activityGroupId;
	}


	public String getActivityGroupName() {
		return activityGroupName;
	}


	public void setActivityGroupName(String activityGroupName) {
		this.activityGroupName = activityGroupName;
	}
	

	public Float getWeightage() {
		return weightage;
	}


	public void setWeightage(Float weightage) {
		this.weightage = weightage;
	}
	
}
