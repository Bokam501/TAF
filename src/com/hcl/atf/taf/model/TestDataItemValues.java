package com.hcl.atf.taf.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "testdata_item_value")
public class TestDataItemValues implements Serializable{

	private Integer testDataValueId;
	private String values;
	private TestDataItems testDataItems;
	private Date createdDate;
	private TestDataPlan testDataPlan;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "testDataItemValueId", unique = true, nullable = false)
	public Integer getTestDataValueId() {
		return testDataValueId;
	}
	public void setTestDataValueId(Integer testDataValueId) {
		this.testDataValueId = testDataValueId;
	}
	
	@Column(name = "tesDataValues")
	public String getValues() {
		return values;
	}
	public void setValues(String values) {
		this.values = values;
	}
	
	@ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="testDataItemId")
	public TestDataItems getTestDataItems() {
		return testDataItems;
	}
	public void setTestDataItems(TestDataItems testDataItems) {
		this.testDataItems = testDataItems;
	}
	@Column(name = "createdDate")
	public Date getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}
	
	@ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="testDataPlanId")
	public TestDataPlan getTestDataPlan() {
		return testDataPlan;
	}
	public void setTestDataPlan(TestDataPlan testDataPlan) {
		this.testDataPlan = testDataPlan;
	}
	
	
}
