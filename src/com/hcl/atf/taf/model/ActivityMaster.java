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
@Table(name = "activity_master")
public class ActivityMaster {
	
	private Integer activityMasterId;
	private String activityMasterName;
	private String description;
	private ProductMaster productMaster;
	private TestFactory testFactory;
	private ActivityType activityType;	
	private Float weightage;
	
	
	
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "activityMasterId",unique = true, nullable = false)
	public Integer getActivityMasterId() {
		return activityMasterId;
	}	
	public void setActivityMasterId(Integer activityMasterId) {
		this.activityMasterId = activityMasterId;
	}
	@Column(name = "activityMasterName")
	public String getActivityMasterName() {
		return activityMasterName;
	}
	public void setActivityMasterName(String activityMasterName) {
		this.activityMasterName = activityMasterName;
	}
	@Column(name = "description")
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}	
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "productId")
	public ProductMaster getProductMaster() {
		return productMaster;
	}
	public void setProductMaster(ProductMaster productMaster) {
		this.productMaster = productMaster;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "testFactoryId")
	public TestFactory getTestFactory() {
		return testFactory;
	}
	public void setTestFactory(TestFactory testFactory) {
		this.testFactory = testFactory;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "activityTypeId")
	public ActivityType getActivityType() {
		return activityType;
	}
	public void setActivityType(ActivityType activityType) {
		this.activityType = activityType;
	}
	
	@Column(name = "weightage")
	public Float getWeightage() {
		return weightage;
	}
	public void setWeightage(Float weightage) {
		this.weightage = weightage;
	}
	
	
}
