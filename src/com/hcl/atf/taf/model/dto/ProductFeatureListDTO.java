package com.hcl.atf.taf.model.dto;

import com.hcl.atf.taf.model.ProductFeature;


public class ProductFeatureListDTO {
	private ProductFeature productFeature;
	private String parentFeatureCode;
	private Integer parentFeatureId;
	
	public ProductFeature getProductFeature() {
		return productFeature;
	}
	public void setProductFeature(ProductFeature productFeature) {
		this.productFeature = productFeature;
	}
	public String getParentFeatureCode() {
		return parentFeatureCode;
	}
	public void setParentFeatureCode(String parentFeatureCode) {
		this.parentFeatureCode = parentFeatureCode;
	}
	public Integer getParentFeatureId() {
		return parentFeatureId;
	}
	public void setParentFeatureId(Integer parentFeatureId) {
		this.parentFeatureId = parentFeatureId;
	}
	
	
}
