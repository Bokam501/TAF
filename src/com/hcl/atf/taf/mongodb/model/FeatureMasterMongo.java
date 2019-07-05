package com.hcl.atf.taf.mongodb.model;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.hcl.atf.taf.model.ProductFeature;

@Document(collection = "features")
public class FeatureMasterMongo {
	
	@Id
	private String id;
	private Integer featureId;
	private String featureName;
	private String parentFeature;
	private String displayName;
	private String productFeatureDescription;
	private String productFeatureCode;
	private Integer productFeaturestatus;
	private String featureType;
	private String featureHierarchyType;
	private Integer childFeaturesCount;
	private Integer featureTestCasesCount;
	private String  executionPriority;
	private Integer productId;
	private String productName;
	private String productType;
	private Integer testFactoryId;
	private String testFactoryName;
	private Integer testCentersId;
	private String testCentersName;
	
	private String parentStatus;
	
	private Integer customerId;
	private String customerName;
	private String keywords;
	private String updated_by;
	private Date createdDate;
	private Date modifiedDate;
	
	public FeatureMasterMongo(){
			
	}
	
	public FeatureMasterMongo(ProductFeature feature) {
		
		this.id = feature.getProductFeatureId()+"";
		this.featureId = feature.getProductFeatureId();
		this.productFeatureCode = feature.getProductFeatureCode();
		this.featureName = feature.getProductFeatureName();
		this.displayName = feature.getDisplayName();
		this.productFeatureDescription = feature.getProductFeatureDescription();
		this.productFeaturestatus = feature.getProductFeaturestatus();
		if(feature.getExecutionPriority()!=null){
			this.executionPriority = feature.getExecutionPriority().getPriorityName();
		}
		
		if(feature.getParentFeature()!=null){
			this. parentFeature = feature.getParentFeature().getProductFeatureName();
		}
		
		if (feature.getParentFeature() == null) {
			featureHierarchyType = "Root Feature";
		} else {
			featureHierarchyType = "Non-Root Feature";
		}
		
		if(feature.getRightIndex()!=null && feature.getLeftIndex()!=null){
			if (feature.getRightIndex() - feature.getLeftIndex() > 1) {
				this.childFeaturesCount = (feature.getRightIndex() - feature.getLeftIndex()) / 2;
			} else if (feature.getRightIndex() - feature.getLeftIndex() == 1) {
				this.childFeaturesCount = 0; 
			}  
		}
		
		
		if (feature.getTestCaseList() != null) {
			this.featureTestCasesCount = feature.getTestCaseList().size();
		}
		
		this.createdDate = feature.getCreatedDate();
		this.modifiedDate = feature.getModifiedDate();
		if(feature.getProductMaster()!=null){
			this.productId = feature.getProductMaster().getProductId();
			this.productName = feature.getProductMaster().getProductName();
			if(feature.getProductMaster().getStatus() == 1 && feature.getProductMaster().getStatus()!=null){
				this.parentStatus = "Active";
			}else{
				this.parentStatus = "InActive";
			}
			
			if(feature.getProductMaster().getProductType()!=null){
				this.productType = feature.getProductMaster().getProductType().getTypeName();
			}
			if(feature.getProductMaster().getCustomer()!=null){
				this.customerId = feature.getProductMaster().getCustomer().getCustomerId();
				this.customerName = feature.getProductMaster().getCustomer().getCustomerName();
			}
			
			if(feature.getProductMaster().getTestFactory()!=null){
				this.testFactoryId = feature.getProductMaster().getTestFactory().getTestFactoryId();
				this.testFactoryName = feature.getProductMaster().getTestFactory().getTestFactoryName();
				if(feature.getProductMaster().getTestFactory().getTestFactoryLab()!=null){
					this.testCentersId=feature.getProductMaster().getTestFactory().getTestFactoryLab().getTestFactoryLabId();
					this.testCentersName=feature.getProductMaster().getTestFactory().getTestFactoryLab().getTestFactoryLabName();
					
				}
				
			}
		}
	}


	public FeatureMasterMongo(String productFeatureId,
			String feature, String parentFeature,
			Date createddate, String keywords, Date modifiedDate,
			String loginId, String productName, String displayName,
			String productFeatureDescription, String productFeatureCode,
			Integer productFeaturestatus2, Integer leftIndex,
			Integer rightIndex) {
		
		this. id=id;
		this. featureName=feature;
		this. parentFeature= parentFeature;
		this. createdDate=createddate;
		this.keywords=keywords;
		this.modifiedDate=modifiedDate;
		this.updated_by=updated_by;
		this.displayName= displayName;
		this.productFeatureDescription=productFeatureDescription;
		this.productFeatureCode= productFeatureCode;
		this.productFeaturestatus=productFeaturestatus;
		
		this. executionPriority=executionPriority;
	}


	public String getParentFeature() {
		return parentFeature;
	}

	public void setParentFeature(String parentFeature) {
		this.parentFeature = parentFeature;
	}




	public String getKeywords() {
		return keywords;
	}



	public void setKeywords(String keywords) {
		this.keywords = keywords;
	}



	



	public String getUpdated_by() {
		return updated_by;
	}



	public void setUpdated_by(String updated_by) {
		this.updated_by = updated_by;
	}



	public String getDisplayName() {
		return displayName;
	}



	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}



	public String getProductFeatureDescription() {
		return productFeatureDescription;
	}



	public void setProductFeatureDescription(String productFeatureDescription) {
		this.productFeatureDescription = productFeatureDescription;
	}



	public String getProductFeatureCode() {
		return productFeatureCode;
	}



	public void setProductFeatureCode(String productFeatureCode) {
		this.productFeatureCode = productFeatureCode;
	}



	public Integer getProductFeaturestatus() {
		return productFeaturestatus;
	}



	public void setProductFeaturestatus(Integer productFeaturestatus) {
		this.productFeaturestatus = productFeaturestatus;
	}





	public String getExecutionPriority() {
		return executionPriority;
	}



	public void setExecutionPriority(String executionPriority) {
		this.executionPriority = executionPriority;
	}

	public String getFeatureType() {
		return featureType;
	}

	public void setFeatureType(String featureType) {
		this.featureType = featureType;
	}

	public String getFeatureHierarchyType() {
		return featureHierarchyType;
	}

	public void setFeatureHierarchyType(String featureHierarchyType) {
		this.featureHierarchyType = featureHierarchyType;
	}

	public Integer getChildFeaturesCount() {
		return childFeaturesCount;
	}

	public void setChildFeaturesCount(Integer childFeaturesCount) {
		this.childFeaturesCount = childFeaturesCount;
	}

	public Integer getFeatureTestCasesCount() {
		return featureTestCasesCount;
	}

	public void setFeatureTestCasesCount(Integer featureTestCasesCount) {
		this.featureTestCasesCount = featureTestCasesCount;
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

	public String getProductType() {
		return productType;
	}

	public void setProductType(String productType) {
		this.productType = productType;
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

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public Date getModifiedDate() {
		return modifiedDate;
	}

	public void setModifiedDate(Date modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

	

	public String getId() {
		return id;
	}

	public void setId(String _id) {
		this.id = _id;
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

	public String getParentStatus() {
		return parentStatus;
	}

	public void setParentStatus(String parentStatus) {
		this.parentStatus = parentStatus;
	}

}