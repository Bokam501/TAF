package com.hcl.atf.taf.model.json;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.hcl.atf.taf.controller.utilities.DateUtility;
import com.hcl.atf.taf.model.DefectIdentificationStageMaster;
import com.hcl.atf.taf.model.ProductBuild;
import com.hcl.atf.taf.model.ProductMaster;
import com.hcl.atf.taf.model.ProductVersionListMaster;

public class JsonProductBuild implements java.io.Serializable {
	
	@JsonProperty
	private Integer productVersionListId;
	@JsonProperty	
	private Integer productBuildId;
	
	@JsonProperty	
	private String productBuildNo;
	
	@JsonProperty
	private String productBuildName;
	@JsonProperty
	private String productBuildDescription;
	@JsonProperty
	private String productBuildDate;
	@JsonProperty
	private Integer status;
	@JsonProperty
	private Integer buildTypeId;
	@JsonProperty
	private String buildTypeName;
	@JsonProperty	
	private Integer clonedProductBuildId;
	@JsonProperty	
	private String clonedProductBuildName;
	@JsonProperty
	private String createdDate;
	@JsonProperty
	private String modifiedDate;	
	@JsonProperty
	private String modifiedField;
	@JsonProperty
	private String modifiedFieldTitle;	
	@JsonProperty
	private String oldFieldID;
	@JsonProperty
	private String oldFieldValue;
	@JsonProperty
	private String modifiedFieldID;
	@JsonProperty
	private String modifiedFieldValue;
	
	@JsonProperty
	private Integer productId;
	
	@JsonProperty
	private String productName;
	
	public JsonProductBuild(){
		
	}
	
	public JsonProductBuild(ProductBuild productBuild) {
		productBuildId = productBuild.getProductBuildId();
		productBuildNo = productBuild.getBuildNo();
		productBuildName = productBuild.getBuildname();
		productBuildDescription = productBuild.getBuildDescription();
		if(productBuild.getStatus() != null){
			status = productBuild.getStatus();
		}
		
		if (productBuild.getProductVersion() != null) {
			productVersionListId = productBuild.getProductVersion()
					.getProductVersionListId();
		}
		if(productBuild.getBuildDate()!=null){
			
			this.productBuildDate=DateUtility.dateformatWithOutTime(productBuild.getBuildDate());
		}
		
		if(productBuild.getCreatedDate()!=null){
			this.createdDate = DateUtility.dateToStringWithSeconds1(productBuild.getCreatedDate());
		}
		if(productBuild.getModifiedDate()!=null){
			this.modifiedDate = DateUtility.dateformatWithOutTime(productBuild.getModifiedDate());
			}
		
		if(productBuild.getBuildType() != null){
			this.buildTypeId = productBuild.getBuildType().getStageId();
			this.buildTypeName = productBuild.getBuildType().getStageName();
		}else{
			this.buildTypeId = 0;
			this.buildTypeName = null;
		}
		
		if(productBuild.getClonedBuild() != null){
			this.clonedProductBuildId = productBuild.getClonedBuild().getProductBuildId();
			this.clonedProductBuildName = productBuild.getClonedBuild().getBuildname();
		}
		
		if(productBuild.getProductMaster() != null){
			
			this.productId = productBuild.getProductMaster().getProductId();
			this.productName = productBuild.getProductMaster().getProductName();
		}
	}
	
	@JsonIgnore
	public ProductBuild getProductBuild() {
			
		ProductBuild productBuild = new ProductBuild();
		if (productBuildId != null) {
			productBuild.setProductBuildId(productBuildId);
		}
		productBuild.setBuildNo(productBuildNo);
		productBuild.setBuildname(productBuildName);
		productBuild.setBuildDescription(productBuildDescription);

		if(this.productId != null){
						
			ProductMaster productMaster = new ProductMaster();
			productMaster.setProductId(this.productId);
			productMaster.setProductName(this.productName);
			productBuild.setProductMaster(productMaster);
			
			
		}
		
		if(productBuild.getProductVersion()!=null){
		ProductVersionListMaster productVersionListMaster = new ProductVersionListMaster();
		productVersionListMaster.setProductVersionListId(productVersionListId);
		productBuild.setProductVersion(productVersionListMaster);//ProductVersionList object is passed not ID.
		}
		
		if(productBuild.getClonedBuild() != null){
			ProductBuild clonedProductBuild = new ProductBuild();
			clonedProductBuild.setProductBuildId(clonedProductBuildId);
			productBuild.setClonedBuild(clonedProductBuild);
		}
		
		if(this.buildTypeId != null){
			DefectIdentificationStageMaster buildType = new DefectIdentificationStageMaster();
			buildType.setStageId(buildTypeId);
			buildType.setStageName(buildTypeName);
			productBuild.setBuildType(buildType);
		}
		
		if(this.createdDate == null || this.createdDate.trim().isEmpty()) {
			productBuild.setCreatedDate(DateUtility.getCurrentTime());
		} else {
		
			productBuild.setCreatedDate(DateUtility.toFormatDate(this.createdDate));
		}
		productBuild.setModifiedDate(DateUtility.getCurrentTime());
		
		if(this.status != null ){
			
			productBuild.setStatus(status);			
		}else{
			productBuild.setStatus(0);	
		}
		if(this.productBuildDate == null || this.productBuildDate.trim().isEmpty()) {
			productBuild.setBuildDate(DateUtility.getCurrentTime());
		} else {
		
			productBuild.setBuildDate(DateUtility.dateformatWithOutTime(this.productBuildDate));
		}

		return productBuild;
	}
	public Integer getProductVersionListId() {
		return productVersionListId;
	}
	
	
	public void setProductVersionListId(Integer productVersionListId) {
		this.productVersionListId = productVersionListId;
	}

	public Integer getProductBuildId() {
		return productBuildId;
	}

	public void setProductBuildId(Integer productBuildId) {
		this.productBuildId = productBuildId;
	}

	public String getProductBuildNo() {
		return productBuildNo;
	}

	public void setProductBuildNo(String productBuildNo) {
		this.productBuildNo = productBuildNo;
	}

	public String getProductBuildName() {
		return productBuildName;
	}

	public void setProductBuildName(String productBuildName) {
		this.productBuildName = productBuildName;
	}

	public String getProductBuildDescription() {
		return productBuildDescription;
	}

	public void setProductBuildDescription(String productBuildDescription) {
		this.productBuildDescription = productBuildDescription;
	}

	public String getProductBuildDate() {
		return productBuildDate;
	}

	public void setProductBuildDate(String productBuildDate) {
		this.productBuildDate = productBuildDate;
	}
	
	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
	}

	public String getModifiedDate() {
		return modifiedDate;
	}

	public void setModifiedDate(String modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

	public Integer getBuildTypeId() {
		return buildTypeId;
	}

	public void setBuildTypeId(Integer buildTypeId) {
		this.buildTypeId = buildTypeId;
	}

	public String getBuildTypeName() {
		return buildTypeName;
	}

	public void setBuildTypeName(String buildTypeName) {
		this.buildTypeName = buildTypeName;
	}

	public Integer getClonedProductBuildId() {
		return clonedProductBuildId;
	}

	public void setClonedProductBuildId(Integer clonedProductBuildId) {
		this.clonedProductBuildId = clonedProductBuildId;
	}

	public String getClonedProductBuildName() {
		return clonedProductBuildName;
	}

	public void setClonedProductBuildName(String clonedProductBuildName) {
		this.clonedProductBuildName = clonedProductBuildName;
	}

	public String getModifiedField() {
		return modifiedField;
	}

	public void setModifiedField(String modifiedField) {
		this.modifiedField = modifiedField;
	}

	public String getModifiedFieldTitle() {
		return modifiedFieldTitle;
	}

	public void setModifiedFieldTitle(String modifiedFieldTitle) {
		this.modifiedFieldTitle = modifiedFieldTitle;
	}

	public String getOldFieldID() {
		return oldFieldID;
	}

	public void setOldFieldID(String oldFieldID) {
		this.oldFieldID = oldFieldID;
	}

	public String getOldFieldValue() {
		return oldFieldValue;
	}

	public void setOldFieldValue(String oldFieldValue) {
		this.oldFieldValue = oldFieldValue;
	}

	public String getModifiedFieldID() {
		return modifiedFieldID;
	}

	public void setModifiedFieldID(String modifiedFieldID) {
		this.modifiedFieldID = modifiedFieldID;
	}

	public String getModifiedFieldValue() {
		return modifiedFieldValue;
	}

	public void setModifiedFieldValue(String modifiedFieldValue) {
		this.modifiedFieldValue = modifiedFieldValue;
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
