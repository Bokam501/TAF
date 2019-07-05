package com.hcl.atf.taf.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "status_category")
public class StatusCategory {

	private Integer statusCategoryId;
	private String statusCategoryName;
	private Integer statusCategoryOrder;
	private Double statusCategoryIndicator;
	private Integer activeStatus;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "statusCategoryId", unique=true, nullable=false)
	public Integer getStatusCategoryId() {
		return statusCategoryId;
	}
	public void setStatusCategoryId(Integer statusCategoryId) {
		this.statusCategoryId = statusCategoryId;
	}
	
	@Column(name = "statusCategoryName")
	public String getStatusCategoryName() {
		return statusCategoryName;
	}
	public void setStatusCategoryName(String statusCategoryName) {
		this.statusCategoryName = statusCategoryName;
	}
	
	@Column(name = "statusCategoryOrder")
	public Integer getStatusCategoryOrder() {
		return statusCategoryOrder;
	}
	public void setStatusCategoryOrder(Integer statusCategoryOrder) {
		this.statusCategoryOrder = statusCategoryOrder;
	}
	
	@Column(name = "statusCategoryIndicatory")
	public Double getStatusCategoryIndicator() {
		return statusCategoryIndicator;
	}
	public void setStatusCategoryIndicator(Double statusCategoryIndicator) {
		this.statusCategoryIndicator = statusCategoryIndicator;
	}
	
	@Column(name = "activeStatus")
	public Integer getActiveStatus() {
		return activeStatus;
	}
	public void setActiveStatus(Integer activeStatus) {
		this.activeStatus = activeStatus;
	}
	
	@Override
	public boolean equals(Object o){
		StatusCategory statusCategory = (StatusCategory) o;
		if(this.statusCategoryId == statusCategory.getStatusCategoryId()){
			return true;
		}else{
			return false;
		}
		
	}
	
	@Override
	public int hashCode(){
		return (int) statusCategoryId;
	}
}
