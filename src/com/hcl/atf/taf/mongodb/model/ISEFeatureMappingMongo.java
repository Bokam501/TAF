package com.hcl.atf.taf.mongodb.model;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.hcl.atf.taf.model.ProductFeature;

@Document(collection = "features_mapping")
public class ISEFeatureMappingMongo {

	@Id
	private String id;
	private String feature;
	private String parentFeature;
	private Date createdDate;
	private Date lastUpdatedDate;
	private Integer productId;
	private String productName;
	
	public ISEFeatureMappingMongo() {
		
	}
	
	public ISEFeatureMappingMongo(ProductFeature feature) {
		this.id = feature.getProductFeatureId()+"";
		this.feature = feature.getProductFeatureName();
		this.createdDate = feature.getCreatedDate();
		this.lastUpdatedDate = feature.getModifiedDate();
		if(feature.getParentFeature()!=null){
			this. parentFeature = feature.getParentFeature().getProductFeatureName();
		}
		if(feature.getProductMaster()!=null){
			this.productId=feature.getProductMaster().getProductId();
			this.productName=feature.getProductMaster().getProductName();
		}	
	}
	
	
	public String getId() {
		return id;
	}
	
	public void setId(String _id) {
		this.id = _id;
	}
	
	
	public String getFeature() {
		return feature;
	}
	
	public void setFeature(String feature) {
		this.feature = feature;
	}
	
	
	public String getParentFeature() {
		return parentFeature;
	}
	
	public void setParentFeature(String parentFeature) {
		this.parentFeature = parentFeature;
	}
	
	
	public Date getCreatedDate() {
		return createdDate;
	}
	
	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}
	
	
	public Date getLastUpdatedDate() {
		return lastUpdatedDate;
	}
	
	public void setLastUpdatedDate(Date lastUpdatedDate) {
		this.lastUpdatedDate = lastUpdatedDate;
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
