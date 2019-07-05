package com.hcl.atf.taf.model.dto;

import java.util.Date;


public class ProductAWPSummaryDTO {
	private Integer productId;
	private String productName;	
	private String description;	
	
	private Integer testFactoryId;	
	private String testFactoryName;
	
	private Integer productVersionListId;	
	private String productVersionName;
	
	private Integer productBuildId;	
	private String buildname;

	private Integer versionCount;
	private Integer buildCount;
	private Integer clarificationCount;
	private Integer changeRequestCount;
	private Integer attachmentsCount;
	private Integer customerId;
	private String customerName;
	private String projectCode;
	private String projectName;
	private Integer productTypeId;
	private String typeName;	
	private Integer modeId;
	private String modeName;
	private Date createdDate;
	private String createdBy;
	private String lastModifiedBy;
	private Date lastModifiedDate;
	
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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Integer getTestFactoryId() {
		return testFactoryId;
	}

	public String getTestFactoryName() {
		return testFactoryName;
	}

	public Integer getProductVersionListId() {
		return productVersionListId;
	}

	public String getProductVersionName() {
		return productVersionName;
	}

	public Integer getProductBuildId() {
		return productBuildId;
	}

	public String getBuildname() {
		return buildname;
	}

	public void setTestFactoryId(Integer testFactoryId) {
		this.testFactoryId = testFactoryId;
	}

	public void setTestFactoryName(String testFactoryName) {
		this.testFactoryName = testFactoryName;
	}

	public void setProductVersionListId(Integer productVersionListId) {
		this.productVersionListId = productVersionListId;
	}

	public void setProductVersionName(String productVersionName) {
		this.productVersionName = productVersionName;
	}

	public void setProductBuildId(Integer productBuildId) {
		this.productBuildId = productBuildId;
	}

	public void setBuildname(String buildname) {
		this.buildname = buildname;
	}



	public Integer getVersionCount() {
		return versionCount;
	}

	public Integer getBuildCount() {
		return buildCount;
	}

	public Integer getClarificationCount() {
		return clarificationCount;
	}

	public Integer getChangeRequestCount() {
		return changeRequestCount;
	}

	public Integer getAttachmentsCount() {
		return attachmentsCount;
	}

	public void setVersionCount(Integer versionCount) {
		this.versionCount = versionCount;
	}

	public void setBuildCount(Integer buildCount) {
		this.buildCount = buildCount;
	}

	public void setClarificationCount(Integer clarificationCount) {
		this.clarificationCount = clarificationCount;
	}

	public void setChangeRequestCount(Integer changeRequestCount) {
		this.changeRequestCount = changeRequestCount;
	}

	public void setAttachmentsCount(Integer attachmentsCount) {
		this.attachmentsCount = attachmentsCount;
	}

	public Integer getCustomerId() {
		return customerId;
	}

	public String getCustomerName() {
		return customerName;
	}

	public String getProjectCode() {
		return projectCode;
	}

	public String getProjectName() {
		return projectName;
	}

	public Integer getProductTypeId() {
		return productTypeId;
	}

	public String getTypeName() {
		return typeName;
	}

	public Integer getModeId() {
		return modeId;
	}

	public String getModeName() {
		return modeName;
	}

	public void setCustomerId(Integer customerId) {
		this.customerId = customerId;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public void setProjectCode(String projectCode) {
		this.projectCode = projectCode;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public void setProductTypeId(Integer productTypeId) {
		this.productTypeId = productTypeId;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	public void setModeId(Integer modeId) {
		this.modeId = modeId;
	}

	public void setModeName(String modeName) {
		this.modeName = modeName;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
	public String getLastModifiedBy() {
		return lastModifiedBy;
	}

	public void setLastModifiedBy(String lastModifiedBy) {
		this.lastModifiedBy = lastModifiedBy;
	}

	public Date getLastModifiedDate() {
		return lastModifiedDate;
	}

	public void setLastModifiedDate(Date lastModifiedDate) {
		this.lastModifiedDate = lastModifiedDate;
	}
	
}
