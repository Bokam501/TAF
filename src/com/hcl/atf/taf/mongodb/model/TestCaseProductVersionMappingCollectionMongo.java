/**
 * 
 */
package com.hcl.atf.taf.mongodb.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.hcl.atf.taf.model.TestCaseProductVersionMapping;

/**
 * @author silambarasur
 *
 */
@Document(collection = "testCase_product_versionMapping")
public class TestCaseProductVersionMappingCollectionMongo {
	
	@Id
	private String id;
	private String _class;
	private Integer testCaseVersionMappingId;
	private Integer testCaseId;
	private String testCaseName;
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
	
	
	
	public TestCaseProductVersionMappingCollectionMongo() {
		
	}
	
	public TestCaseProductVersionMappingCollectionMongo(TestCaseProductVersionMapping testCaseProductVersionMapping,String testCaseName,String versionName) {
		if(testCaseProductVersionMapping != null ) {
			this.id=testCaseProductVersionMapping.getId()+"";
			this._class = this.getClass().getName().replace("\\", ".").replace("/", ".");
			
			
			if(testCaseProductVersionMapping != null && testCaseProductVersionMapping.getProduct() != null) {
				this.productId = testCaseProductVersionMapping.getProduct().getProductId();
				this.productName = testCaseProductVersionMapping.getProduct().getProductName();
				
				if(testCaseProductVersionMapping.getProduct().getTestFactory() != null) {
					this.testFactoryId = testCaseProductVersionMapping.getProduct().getTestFactory().getTestFactoryId();
					this.testFactoryName = testCaseProductVersionMapping.getProduct().getTestFactory().getTestFactoryName();
					
					if(testCaseProductVersionMapping.getProduct().getTestFactory().getTestFactoryLab() != null) {
						this.testCentersId = testCaseProductVersionMapping.getProduct().getTestFactory().getTestFactoryLab().getTestFactoryLabId();
						this.testCentersName = testCaseProductVersionMapping.getProduct().getTestFactory().getTestFactoryLab().getTestFactoryLabName();
					}
				}
			}
		
		}
		this.createdDate = testCaseProductVersionMapping.getCreatedDate();
		this.modifiedDate = testCaseProductVersionMapping.getModifiedDate();
		this.createdBy = testCaseProductVersionMapping.getCreatedBy() != null ? testCaseProductVersionMapping.getCreatedBy().getLoginId() : "";
		this.modifiedBy = testCaseProductVersionMapping.getCreatedBy() != null ? testCaseProductVersionMapping.getModifiedBy().getLoginId() : "";
		this.testCaseId = testCaseProductVersionMapping.getTestCaseId();
		this.testCaseName = testCaseName;
		this.versionId = testCaseProductVersionMapping.getVersionId();
		this.versionName = versionName;
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

	public Integer getTestCaseVersionMappingId() {
		return testCaseVersionMappingId;
	}

	public void setTestCaseVersionMappingId(Integer testCaseVersionMappingId) {
		this.testCaseVersionMappingId = testCaseVersionMappingId;
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
	
	
	

}
