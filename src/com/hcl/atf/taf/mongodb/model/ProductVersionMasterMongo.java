package com.hcl.atf.taf.mongodb.model;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.hcl.atf.taf.model.ProductVersionListMaster;

@Document(collection = "versions")
public class ProductVersionMasterMongo {
	
	@Id
	private String id;
	private Integer versionId;
	private String versionName;//ISE Filed
	private String productVersionDescription;
	private Integer productId;
	private String productName;
	private Integer testFactoryId;
	private String testFactoryName;
	private String targetSourceLocation;
	private String targetBinaryLocation;
	private String webAppURL;
	private String status;
	private Date releaseDate;
	private Integer  customerId;
	private String customerName;
	private Integer testCentersId;
	private String testCentersName;
	
	private String parentStatus;
	
	private Date createdDate;
	private Date modifiedDate;
	
	
	@Override
    public String toString(){
	return versionName+":"+productName+":"+":"+productVersionDescription+":"+ targetSourceLocation+":"+
			targetBinaryLocation+":"+webAppURL+":"+status+":"+releaseDate+":"+modifiedDate/*+":"+testRunConfigurationChildId+":"+testSuiteName+":"+testCaseName*/;
    }
	
	public ProductVersionMasterMongo(){
		
	}

	public ProductVersionMasterMongo(String productName, String productVersionName, String productVersionDescription, String targetSourceLocation,
			String targetBinaryLocation, String webAppURL, String status, Date releaseDate, Date modifiedDate) {
	}
	
public ProductVersionMasterMongo(ProductVersionListMaster productVersion) {
		this.id=productVersion.getProductVersionListId()+"";
		if(productVersion.getProductMaster()!=null){
			this.productName = productVersion.getProductMaster().getProductName();
			this.productId=productVersion.getProductMaster().getProductId();
			if(productVersion.getProductMaster().getStatus()!=null && productVersion.getProductMaster().getStatus() == 1){
				this.parentStatus = "Active";
			}else{
				this.parentStatus = "InActive";
			}
		}
		
		this.productVersionDescription = productVersion.getProductVersionDescription();
		this.targetSourceLocation = productVersion.getTargetSourceLocation();
		this.targetBinaryLocation = productVersion.getTargetBinaryLocation();
		this.webAppURL = productVersion.getWebAppURL();
		if (productVersion.getStatus() == 1){
			this.status = "Active";
		}else{
			this.status = "InActive";
		}
		this.releaseDate = productVersion.getReleaseDate();
		this.modifiedDate = productVersion.getStatusChangeDate();
		this.versionId=productVersion.getProductVersionListId();
		this.versionName = productVersion.getProductVersionName();
		this.testFactoryName = productVersion.getProductMaster().getTestFactory().getTestFactoryName();
		this.customerName = productVersion.getProductMaster().getCustomer().getCustomerName();
		this.productName=productVersion.getProductMaster().getProductName();
	
		this.testFactoryId=productVersion.getProductMaster().getTestFactory().getTestFactoryId();
		this.customerId=productVersion.getProductMaster().getCustomer().getCustomerId();
		this.createdDate=productVersion.getCreatedDate();
		
		this.testCentersId=productVersion.getProductMaster().getTestFactory().getTestFactoryLab().getTestFactoryLabId();
		this.testCentersName=productVersion.getProductMaster().getTestFactory().getTestFactoryLab().getTestFactoryLabName();
	
		
	}
	

	public ProductVersionMasterMongo(String productName2,
		String productVersionName, String productVersionDescription2,
		String targetSourceLocation2, String targetBinaryLocation2,
		String webAppURL2, Integer status2, Date releaseDate2,
		Date statusChangeDate) {
		this.productName = productName;
		this.productVersionDescription = productVersionDescription;
		this.targetSourceLocation = targetSourceLocation;
		this.targetBinaryLocation = targetBinaryLocation;
		this.webAppURL = webAppURL;
		this.status = status;
		this.releaseDate = releaseDate;
		this.modifiedDate = modifiedDate;
}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	

	public String getProductVersionDescription() {
		return productVersionDescription;
	}

	public void setProductVersionDescription(String productVersionDescription) {
		this.productVersionDescription = productVersionDescription;
	}

	public String getTargetSourceLocation() {
		return targetSourceLocation;
	}

	public void setTargetSourceLocation(String targetSourceLocation) {
		this.targetSourceLocation = targetSourceLocation;
	}

	public String getTargetBinaryLocation() {
		return targetBinaryLocation;
	}

	public void setTargetBinaryLocation(String targetBinaryLocation) {
		this.targetBinaryLocation = targetBinaryLocation;
	}

	public String getWebAppURL() {
		return webAppURL;
	}

	public void setWebAppURL(String webAppURL) {
		this.webAppURL = webAppURL;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Date getReleaseDate() {
		return releaseDate;
	}

	public void setReleaseDate(Date releaseDate) {
		this.releaseDate = releaseDate;
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

	public Integer getProductId() {
		return productId;
	}

	public void setProductId(Integer productId) {
		this.productId = productId;
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
	

	public String getId() {
		return id;
	}

	public void setId(String _id) {
		this.id = _id;
	}

	public String getVersionName() {
		return versionName;
	}

	public void setVersionName(String versionName) {
		this.versionName = versionName;
	}

	public Integer getVersionId() {
		return versionId;
	}

	public void setVersionId(Integer versionId) {
		this.versionId = versionId;
	}

	public Date getModifiedDate() {
		return modifiedDate;
	}

	public void setModifiedDate(Date modifiedDate) {
		this.modifiedDate = modifiedDate;
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
