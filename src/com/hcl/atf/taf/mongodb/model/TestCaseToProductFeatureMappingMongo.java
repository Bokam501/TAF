package com.hcl.atf.taf.mongodb.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.hcl.atf.taf.model.ProductFeature;
import com.hcl.atf.taf.model.TestCaseList;

@Document(collection = "productFeature_testCase_mapping")
public class TestCaseToProductFeatureMappingMongo {

	@Id
	private String id;
	private Integer productFeatureId;
	private String productFeatureName;
	private String parentFeature;
	private Integer testCaseId;
	private String testCaseName;
	private Integer productId;
	private String productName;
	
	public TestCaseToProductFeatureMappingMongo() {
		
	}
	
	public TestCaseToProductFeatureMappingMongo(ProductFeature productFeature,TestCaseList testCaseList) {
		this.id = productFeature.getProductFeatureId()+"_"+testCaseList.getTestCaseId();
		this.productFeatureId = productFeature.getProductFeatureId();
		this.productFeatureName = productFeature.getProductFeatureName();
		if(productFeature.getParentFeature() != null){			
			this.parentFeature = productFeature.getParentFeature().getProductFeatureName();
		}
		if(productFeature.getProductMaster() != null) {
			this.productId = productFeature.getProductMaster().getProductId();
			this.productName = productFeature.getProductMaster().getProductName();
		}
		this.testCaseId = testCaseList.getTestCaseId();
		this.testCaseName = testCaseList.getTestCaseName();
	}
	
	
	public String getId() {
		return id;
	}

	public void setId(String _id) {
		this.id = _id;
	}

	
	public Integer getProductFeatureId() {
		return productFeatureId;
	}
	
	public void setProductFeatureId(Integer productFeatureId) {
		this.productFeatureId = productFeatureId;
	}
	
	
	public String getProductFeatureName() {
		return productFeatureName;
	}
	
	public void setProductFeatureName(String productFeatureName) {
		this.productFeatureName = productFeatureName;
	}
	
	
	public String getParentFeature() {
		return parentFeature;
	}
	
	public void setParentFeature(String parentFeature) {
		this.parentFeature = parentFeature;
	}
	
	
	public Integer getTestCaseId() {
		return testCaseId;
	}
	
	public void setTestCaseId(Integer testCaseId) {
		this.testCaseId = testCaseId;
	}
	
	
	public String getTestCaseName() {
		return testCaseName;
	}
	
	public void setTestCaseName(String testCaseName) {
		this.testCaseName = testCaseName;
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
	
}
