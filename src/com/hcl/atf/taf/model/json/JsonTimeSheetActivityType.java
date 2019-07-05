package com.hcl.atf.taf.model.json;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.hcl.atf.taf.controller.utilities.DateUtility;
import com.hcl.atf.taf.model.ProductMaster;
import com.hcl.atf.taf.model.TimeSheetActivityType;

public class JsonTimeSheetActivityType implements java.io.Serializable {
	
	@JsonProperty
	private Integer activityTypeId;
	@JsonProperty
	private String activityName;
	@JsonProperty
	private String description;
	@JsonProperty
	private Integer status;
	@JsonProperty
	private Integer productId;
	@JsonProperty
	private String productName;
	@JsonProperty
	private String createdDate;
	@JsonProperty
	private String modifiedDate;
	
	
	public JsonTimeSheetActivityType() {
	}

	public JsonTimeSheetActivityType(TimeSheetActivityType activityType) {
		
		this.activityTypeId = activityType.getActivityTypeId();
		this.activityName = activityType.getActivityName();
		this.description = activityType.getDescription();
		if(activityType.getCreatedDate()!=null)
		this.createdDate = DateUtility.dateToStringInSecond(activityType.getCreatedDate());
		if(activityType.getModifiedDate()!=null)
		this.modifiedDate = DateUtility.dateToStringInSecond(activityType.getModifiedDate());
		
		if(activityType.getStatus() != null){
			this.status = activityType.getStatus();
		}
		if(activityType.getProduct() !=null){
			productId = activityType.getProduct().getProductId();
			productName = activityType.getProduct().getProductName();
		}	
	}
	
	@JsonIgnore
	public TimeSheetActivityType getTimeSheetActivityType() {
		TimeSheetActivityType activityType = new TimeSheetActivityType();
		if (activityTypeId != null) {
			activityType.setActivityTypeId(activityTypeId);
		}
		activityType.setActivityName(activityName);
		activityType.setDescription(description);
		
		if(createdDate!=null){
			activityType.setCreatedDate(DateUtility.dateFormatWithOutSeconds(createdDate));
		}else{
			activityType.setCreatedDate(DateUtility.getCurrentTime());
		}
		activityType.setModifiedDate(DateUtility.getCurrentTime());
		
		if (productId != null) {
			ProductMaster product = new ProductMaster();
			product.setProductId(productId);
			activityType.setProduct(product);//Product object is passed not ID.
		}	
		if(this.status != null ){			
			activityType.setStatus(status);			
		}else{
			activityType.setStatus(0);	
		}
		
		return activityType;
	}
	
	public Integer getActivityTypeId() {
		return activityTypeId;
	}
	public void setActivityTypeId(Integer activityTypeId) {
		this.activityTypeId = activityTypeId;
	}
	public String getActivityName() {
		return activityName;
	}
	public void setActivityName(String activityName) {
		this.activityName = activityName;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
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
}
