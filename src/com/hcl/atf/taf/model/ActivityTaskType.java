package com.hcl.atf.taf.model;

import static javax.persistence.GenerationType.IDENTITY;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "activity_task_type")
public class ActivityTaskType {

	private Integer activityTaskTypeId;
	private String activityTaskTypeName;	
	private String activityTaskTypeDescription;
	private Float activityTaskTypeWeightage;
	
	private ProductMaster product;
	private TestFactory testFactory;
	
	
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "activityTaskTypeId",unique = true, nullable = false)
	public Integer getActivityTaskTypeId() {
		return activityTaskTypeId;
	}
	public void setActivityTaskTypeId(Integer activityTaskTypeId) {
		this.activityTaskTypeId = activityTaskTypeId;
	}
	
	@Column(name = "activityTaskTypeName")
	public String getActivityTaskTypeName() {
		return activityTaskTypeName;
	}
	public void setActivityTaskTypeName(String activityTaskTypeName) {
		this.activityTaskTypeName = activityTaskTypeName;
	}
	
	@Column(name = "activityTaskTypeDescription")
	public String getActivityTaskTypeDescription() {
		return activityTaskTypeDescription;
	}
	public void setActivityTaskTypeDescription(String activityTaskTypeDescription) {
		this.activityTaskTypeDescription = activityTaskTypeDescription;
	}
	

	@Column(name = "activityTaskTypeWeightage")
	public Float getActivityTaskTypeWeightage() {
		return activityTaskTypeWeightage;
	}
	public void setActivityTaskTypeWeightage(Float activityTaskTypeWeightage) {
		this.activityTaskTypeWeightage = activityTaskTypeWeightage;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "productId") 
	public ProductMaster getProduct() {
		return product;
	}
	public void setProduct(ProductMaster product) {
		this.product = product;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "testFactoryId") 
	public TestFactory getTestFactory() {
		return testFactory;
	}
	public void setTestFactory(TestFactory testFactory) {
		this.testFactory = testFactory;
	}
	
	
}
