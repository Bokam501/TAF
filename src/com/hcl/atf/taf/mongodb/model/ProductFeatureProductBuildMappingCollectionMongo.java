/**
 * 
 */
package com.hcl.atf.taf.mongodb.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.hcl.atf.taf.model.ProductFeatureProductBuildMapping;

/**
 * @author silambarasur
 *
 */
@Document(collection = "product_feature_build_Mapping")
public class ProductFeatureProductBuildMappingCollectionMongo {
	

	@Id
	private String id;
	private String _class;
	private Integer featureId;
	private String featureName;
	private Integer versionId;
	private String versionName;
	private Integer productId;
	private String productName;
	private Object createdDate;
	private Object modifiedDate;
	private String createdBy;
	private String modifiedBy;
	
	
	private Integer testFactoryId;
	private String testFactoryName;
	private Integer testCentersId;
	private String testCentersName;
	private Integer buildId;
	private String buildName;
	private Integer customerId;
	private String customerName;
	
	
	public ProductFeatureProductBuildMappingCollectionMongo() {
		
	}
	
	public ProductFeatureProductBuildMappingCollectionMongo(ProductFeatureProductBuildMapping productFeatureProductBuildMapping,String featureName,String buildName,Integer versionId,String versionName){
	
		if(productFeatureProductBuildMapping != null ) {
			
			this.id=productFeatureProductBuildMapping.getId()+"";
			this._class = this.getClass().getName().replace("\\", ".").replace("/", ".");
			
		
			if(productFeatureProductBuildMapping != null && productFeatureProductBuildMapping.getProduct() != null) {
				this.productId = productFeatureProductBuildMapping.getProduct().getProductId();
				this.productName = productFeatureProductBuildMapping.getProduct().getProductName();
				
				if(productFeatureProductBuildMapping.getProduct().getTestFactory() != null) {
					this.testFactoryId = productFeatureProductBuildMapping.getProduct().getTestFactory().getTestFactoryId();
					this.testFactoryName = productFeatureProductBuildMapping.getProduct().getTestFactory().getTestFactoryName();
					
					if(productFeatureProductBuildMapping.getProduct().getTestFactory().getTestFactoryLab() != null) {
						this.testCentersId = productFeatureProductBuildMapping.getProduct().getTestFactory().getTestFactoryLab().getTestFactoryLabId();
						this.testCentersName = productFeatureProductBuildMapping.getProduct().getTestFactory().getTestFactoryLab().getTestFactoryLabName();
					}
				}
			}
			
			this.featureId = productFeatureProductBuildMapping.getFeatureId();
			this.featureName = featureName;
			this.buildId = productFeatureProductBuildMapping.getBuildId();
			this.buildName = buildName;
			this.versionId = versionId;
			this.versionName = versionName;
			this.createdDate = productFeatureProductBuildMapping.getCreatedDate();
			this.modifiedDate = productFeatureProductBuildMapping.getModifiedDate();
			this.createdBy = productFeatureProductBuildMapping.getCreatedBy() != null ? productFeatureProductBuildMapping.getCreatedBy().getLoginId() : "";
			this.modifiedBy = productFeatureProductBuildMapping.getCreatedBy() != null ? productFeatureProductBuildMapping.getModifiedBy().getLoginId() : "";
		
		}
	}
	public String getId() {
		return id;
	}
	public void setId(String _id) {
		this.id = _id;
	}
	public String get_class() {
		return _class;
	}
	public void set_class(String _class) {
		this._class = _class;
	}
	
	public Integer getVersionId() {
		return versionId;
	}
	public void setVersionId(Integer versionId) {
		this.versionId = versionId;
	}
	public String getVersionName() {
		return versionName;
	}
	public void setVersionName(String versionName) {
		this.versionName = versionName;
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
	public Object getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(Object createdDate) {
		this.createdDate = createdDate;
	}
	public Object getModifiedDate() {
		return modifiedDate;
	}
	public void setModifiedDate(Object modifiedDate) {
		this.modifiedDate = modifiedDate;
	}
	public String getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
	public String getModifiedBy() {
		return modifiedBy;
	}
	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}
	public Integer getTestFactoryId() {
		return testFactoryId;
	}
	public void setTestFactoryId(Integer testFactoryId) {
		this.testFactoryId = testFactoryId;
	}
	public String getTestFactoryName() {
		return testFactoryName;
	}
	public void setTestFactoryName(String testFactoryName) {
		this.testFactoryName = testFactoryName;
	}
	public Integer getTestCentersId() {
		return testCentersId;
	}
	public void setTestCentersId(Integer testCentersId) {
		this.testCentersId = testCentersId;
	}
	public String getTestCentersName() {
		return testCentersName;
	}
	public void setTestCentersName(String testCentersName) {
		this.testCentersName = testCentersName;
	}
	public Integer getBuildId() {
		return buildId;
	}
	public void setBuildId(Integer buildId) {
		this.buildId = buildId;
	}
	public String getBuildName() {
		return buildName;
	}
	public void setBuildName(String buildName) {
		this.buildName = buildName;
	}
	public Integer getCustomerId() {
		return customerId;
	}
	public void setCustomerId(Integer customerId) {
		this.customerId = customerId;
	}
	public String getCustomerName() {
		return customerName;
	}
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public Integer getFeatureId() {
		return featureId;
	}

	public void setFeatureId(Integer featureId) {
		this.featureId = featureId;
	}

	public String getFeatureName() {
		return featureName;
	}

	public void setFeatureName(String featureName) {
		this.featureName = featureName;
	}
	
}
