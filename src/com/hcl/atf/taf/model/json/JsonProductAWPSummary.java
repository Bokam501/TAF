package com.hcl.atf.taf.model.json;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.hcl.atf.taf.model.dto.ProductAWPSummaryDTO;

public class JsonProductAWPSummary {

	@JsonProperty
	private Integer productId;
	@JsonProperty
	private String productName;	
	@JsonProperty
	private String description;
	@JsonProperty
	private Integer testFactoryId;
	@JsonProperty
	private String testFactoryName;
	@JsonProperty
	private Integer productVersionListId;
	@JsonProperty
	private String productVersionName;
	@JsonProperty
	private Integer productBuildId;	
	@JsonProperty
	private String buildname;
	@JsonProperty
	private Integer versionCount;
	@JsonProperty
	private Integer buildCount;
	@JsonProperty
	private Integer clarificationCount;
	@JsonProperty
	private Integer changeRequestCount;
	@JsonProperty
	private Integer attachmentsCount;
	@JsonProperty
	private Integer customerId;
	@JsonProperty
	private String customerName;
	@JsonProperty
	private String projectCode;
	@JsonProperty
	private String projectName;
	@JsonProperty
	private Integer productTypeId;
	@JsonProperty
	private String typeName;	
	@JsonProperty
	private Integer modeId;
	@JsonProperty
	private String modeName;
	@JsonProperty
	private String createdDate;
	@JsonProperty
	private String createdBy;
	@JsonProperty
	private String lastModifiedBy;
	@JsonProperty
	private String lastModifiedDate;
	
	public JsonProductAWPSummary(){
		this.productId = 0;
		this.productName = "";	
		this.description = "";	
		this.testFactoryId = 0;
		this.testFactoryName = "";	
		this.productVersionListId = 0;
		this.productVersionName = "";	
		this.productBuildId = 0;	
		this.buildname = "";	
		this.versionCount = 0;
		this.buildCount = 0;
		this.clarificationCount = 0;
		this.changeRequestCount = 0;
		this.attachmentsCount = 0;
		this.customerId = 0;
		this.customerName = "";
		this.projectCode = "";
		this.projectName = "";
		this.productTypeId = 0;
		this.typeName = "";
		this.modeId = 0;
		this.modeName = "";
		this.createdDate = "";
		this.createdBy = "";
		this.lastModifiedBy = "";
		this.lastModifiedDate = "";		
		
	}
	
	public JsonProductAWPSummary(ProductAWPSummaryDTO productAWPSummaryDTO){
		this.productId = productAWPSummaryDTO.getProductId();
		this.productName = productAWPSummaryDTO.getProductName();
		this.description = productAWPSummaryDTO.getDescription();
		this.testFactoryId = productAWPSummaryDTO.getTestFactoryId();
		this.testFactoryName = productAWPSummaryDTO.getTestFactoryName();
		this.productVersionListId = productAWPSummaryDTO.getProductVersionListId();
		this.productVersionName = productAWPSummaryDTO.getProductVersionName();
		this.productBuildId = productAWPSummaryDTO.getProductBuildId();
		this.buildname = productAWPSummaryDTO.getBuildname();
		this.versionCount = productAWPSummaryDTO.getVersionCount();
		this.buildCount = productAWPSummaryDTO.getBuildCount();
		this.clarificationCount = productAWPSummaryDTO.getClarificationCount();
		this.changeRequestCount = productAWPSummaryDTO.getChangeRequestCount();
		this.attachmentsCount = productAWPSummaryDTO.getAttachmentsCount();
		if(productAWPSummaryDTO.getCustomerId() != null)
			this.customerId = productAWPSummaryDTO.getCustomerId();
		if(productAWPSummaryDTO.getCustomerName() != null)
			this.customerName = productAWPSummaryDTO.getCustomerName();
		
		this.projectCode = productAWPSummaryDTO.getProjectCode();
		this.projectName = productAWPSummaryDTO.getProjectName();
		if(productAWPSummaryDTO.getProductTypeId() != null)
			this.productTypeId = productAWPSummaryDTO.getProductTypeId();
		this.typeName = productAWPSummaryDTO.getTypeName();
		if(productAWPSummaryDTO.getModeId() != null)
			this.modeId = productAWPSummaryDTO.getModeId();
		if(productAWPSummaryDTO.getModeName() != null)
			this.modeName = productAWPSummaryDTO.getModeName();			
		if(productAWPSummaryDTO.getCreatedDate() != null)
			this.createdDate = productAWPSummaryDTO.getCreatedDate().toString();
		if(productAWPSummaryDTO.getCreatedBy() != null)
			this.createdBy = productAWPSummaryDTO.getCreatedBy();
		if(productAWPSummaryDTO.getLastModifiedBy() != null)
			this.lastModifiedBy = productAWPSummaryDTO.getLastModifiedBy();
		if(productAWPSummaryDTO.getLastModifiedDate() != null)
			this.lastModifiedDate = productAWPSummaryDTO.getLastModifiedDate().toString();
	}

	public Integer getProductId() {
		return productId;
	}

	public String getProductName() {
		return productName;
	}

	public String getDescription() {
		return description;
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

	public void setProductId(Integer productId) {
		this.productId = productId;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public void setDescription(String description) {
		this.description = description;
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

	public void setVersionCount(Integer versionCount) {
		this.versionCount = versionCount;
	}

	public Integer getBuildCount() {
		return buildCount;
	}

	public void setBuildCount(Integer buildCount) {
		this.buildCount = buildCount;
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

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public String getCreatedDate() {
		return createdDate;
	}

	public String getLastModifiedBy() {
		return lastModifiedBy;
	}	

	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
	}

	public void setLastModifiedBy(String lastModifiedBy) {
		this.lastModifiedBy = lastModifiedBy;
	}

	public String getLastModifiedDate() {
		return lastModifiedDate;
	}

	public void setLastModifiedDate(String lastModifiedDate) {
		this.lastModifiedDate = lastModifiedDate;
	}

}
