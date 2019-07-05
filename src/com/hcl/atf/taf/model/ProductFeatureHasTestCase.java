package com.hcl.atf.taf.model;

import static javax.persistence.GenerationType.IDENTITY;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="product_feature_has_test_case_list")
public class ProductFeatureHasTestCase {
	
	private Integer id;
	private Integer productFeatureId;
	private Integer testCaseId;
	

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	public Integer getId() {
		return id;
	}
	
	public void setId(Integer id) {
		this.id = id;
	}
	
	
	@Column(name="productFeatureId")
	public Integer getProductFeatureId() {
		return productFeatureId;
	}

	public void setProductFeatureId(Integer productFeatureId) {
		this.productFeatureId = productFeatureId;
	}
	
	
	@Column(name="testCaseId")
	public Integer getTestCaseId() {
		return testCaseId;
	}

	public void setTestCaseId(Integer testCaseId) {
		this.testCaseId = testCaseId;
	}
	
		
}
