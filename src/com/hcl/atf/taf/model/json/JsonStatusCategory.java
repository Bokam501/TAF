package com.hcl.atf.taf.model.json;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.hcl.atf.taf.model.StatusCategory;

public class JsonStatusCategory {

	@JsonProperty
	private Integer statusCategoryId;
	@JsonProperty
	private String statusCategoryName;
	@JsonProperty
	private Integer statusCategoryOrder;
	@JsonProperty
	private Double statusCategoryIndicator;
	@JsonProperty
	private Integer activeStatus;
	
	public JsonStatusCategory(){
		
	}
	
	public JsonStatusCategory(StatusCategory statusCategory){
		this.statusCategoryId = statusCategory.getStatusCategoryId();
		this.statusCategoryName = statusCategory.getStatusCategoryName();
		this.statusCategoryOrder = statusCategory.getStatusCategoryOrder();
		this.statusCategoryIndicator = statusCategory.getStatusCategoryIndicator();
		this.activeStatus = statusCategory.getActiveStatus();
		
	}
	
	public Integer getStatusCategoryId() {
		return statusCategoryId;
	}
	public void setStatusCategoryId(Integer statusCategoryId) {
		this.statusCategoryId = statusCategoryId;
	}
	public String getStatusCategoryName() {
		return statusCategoryName;
	}
	public void setStatusCategoryName(String statusCategoryName) {
		this.statusCategoryName = statusCategoryName;
	}
	public Integer getStatusCategoryOrder() {
		return statusCategoryOrder;
	}
	public void setStatusCategoryOrder(Integer statusCategoryOrder) {
		this.statusCategoryOrder = statusCategoryOrder;
	}
	public Double getStatusCategoryIndicator() {
		return statusCategoryIndicator;
	}
	public void setStatusCategoryIndicator(Double statusCategoryIndicator) {
		this.statusCategoryIndicator = statusCategoryIndicator;
	}
	public Integer getActiveStatus() {
		return activeStatus;
	}
	public void setActiveStatus(Integer activeStatus) {
		this.activeStatus = activeStatus;
	}

	@JsonIgnore
	public StatusCategory getStatusCategory(){
		StatusCategory statusCategory = new StatusCategory();
		if(this.statusCategoryId != null){
			statusCategory.setStatusCategoryId(this.statusCategoryId);
			statusCategory.setStatusCategoryName(this.statusCategoryName);
			statusCategory.setStatusCategoryOrder(this.statusCategoryOrder);
			statusCategory.setStatusCategoryIndicator(this.statusCategoryIndicator);
			statusCategory.setActiveStatus(this.activeStatus);
		}
		return statusCategory;
	}
	
}
