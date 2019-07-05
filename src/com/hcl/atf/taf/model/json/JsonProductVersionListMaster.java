package com.hcl.atf.taf.model.json;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.hcl.atf.taf.controller.utilities.DateUtility;
import com.hcl.atf.taf.model.ProductMaster;
import com.hcl.atf.taf.model.ProductVersionListMaster;

public class JsonProductVersionListMaster implements java.io.Serializable {
	@JsonProperty
	private Integer productVersionListId;

	@JsonProperty	
	private Integer productId;
	@JsonProperty
	private String productVersionName;
	@JsonProperty
	private String productVersionDescription;
	@JsonProperty
	private String releaseDate;
	@JsonProperty
	private Integer status;
	@JsonProperty
	private String targetSourceLocation;
	@JsonProperty
	private String targetBinaryLocation;
	@JsonProperty
	private String webAppURL;
	@JsonProperty
	private String createdDate;
	@JsonProperty
	private String statusChangeDate;
	//added for mongoDB insertion
	@JsonProperty
	private String productName;
	@JsonProperty
	private String testFactory;	
	@JsonProperty
	private String modifiedField;
	@JsonProperty
	private String modifiedFieldTitle;	
	@JsonProperty
	private String oldFieldID;
	@JsonProperty
	private String oldFieldValue;
	@JsonProperty
	private String	modifiedFieldID;	
	@JsonProperty
	private String modifiedFieldValue;
	
	public String getTestFactory() {
		return testFactory;
	}


	public void setTestFactory(String testFactory) {
		this.testFactory = testFactory;
	}


	public String getProductName() {
		return productName;
	}


	public void setProductName(String productName) {
		this.productName = productName;
	}

	//end
	public JsonProductVersionListMaster(){
		
	}
	
	
	public JsonProductVersionListMaster(ProductVersionListMaster productVersionListMaster){
		 productVersionListId=productVersionListMaster.getProductVersionListId();
		 productVersionName=productVersionListMaster.getProductVersionName();
		 productVersionDescription=productVersionListMaster.getProductVersionDescription();
		 if(productVersionListMaster.getStatus() != null){
			 status=productVersionListMaster.getStatus();
			}
		
		
		 if(productVersionListMaster.getReleaseDate()!=null){
			 this.releaseDate= DateUtility.dateformatWithOutTime(productVersionListMaster.getReleaseDate());
			}
		 
		 if(productVersionListMaster.getCreatedDate()!=null){
				this.createdDate = DateUtility.dateToStringWithSeconds1(productVersionListMaster.getCreatedDate());
		}
		 
		if(productVersionListMaster.getStatusChangeDate()!=null){
				this.statusChangeDate = DateUtility.dateformatWithOutTime(productVersionListMaster.getStatusChangeDate());
		}	
			
		 if(productVersionListMaster.getProductMaster()!=null)
			 productId=productVersionListMaster.getProductMaster().getProductId();
		 targetBinaryLocation=productVersionListMaster.getTargetBinaryLocation();
		 targetSourceLocation=productVersionListMaster.getTargetSourceLocation();
		 webAppURL=productVersionListMaster.getWebAppURL();
	}

	
	
	
	
	public Integer getProductVersionListId() {
		return productVersionListId;
	}
	
	
	public void setProductVersionListId(Integer productVersionListId) {
		this.productVersionListId = productVersionListId;
	}
	
	
	public Integer getProductId() {
		return productId;
	}
	
	
	public void setProductId(Integer productId) {
		this.productId = productId;
	}
	
	
	public String getProductVersionName() {
		return productVersionName;
	}
	
	
	public void setProductVersionName(String productVersionName) {
		this.productVersionName = productVersionName;
	}
	
	
	public String getProductVersionDescription() {
		return productVersionDescription;
	}
	
	
	public void setProductVersionDescription(String productVersionDescription) {
		this.productVersionDescription = productVersionDescription;
	}
	
	
	
	@JsonIgnore
	public ProductVersionListMaster getProductVersionListMaster(){
				
		ProductVersionListMaster productVersionListMaster = new ProductVersionListMaster();
		if(productVersionListId!=null){
			productVersionListMaster.setProductVersionListId(productVersionListId);
		}
		productVersionListMaster.setProductVersionName(productVersionName);
		productVersionListMaster.setProductVersionDescription(productVersionDescription);
		
		ProductMaster productMaster = new ProductMaster();
		productMaster.setProductId(productId);		
		productVersionListMaster.setProductMaster(productMaster);
		
		if(this.createdDate == null || this.createdDate.trim().isEmpty()) {
			productVersionListMaster.setCreatedDate(DateUtility.getCurrentTime());
		} else {
			productVersionListMaster.setCreatedDate(DateUtility.toFormatDate(this.createdDate));
		}
		productVersionListMaster.setStatusChangeDate(DateUtility.getCurrentTime());
		
		
		
		
		if(this.status != null ){			
			productVersionListMaster.setStatus(status);		
		}else{
			productVersionListMaster.setStatus(0);	
		}
		
		if(this.releaseDate == null || this.releaseDate.trim().isEmpty()) {
			productVersionListMaster.setReleaseDate(DateUtility.getCurrentTime());
		}else {
			productVersionListMaster.setReleaseDate(DateUtility.dateformatWithOutTime(this.releaseDate));
		}
		
		productVersionListMaster.setTargetBinaryLocation(targetBinaryLocation);
		productVersionListMaster.setTargetSourceLocation(targetSourceLocation);
		productVersionListMaster.setWebAppURL(webAppURL);
		return productVersionListMaster;
	}

	public String getReleaseDate() {
		return releaseDate;
	}

	public void setReleaseDate(String releaseDate) {
		this.releaseDate=releaseDate;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
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


	public String getCreatedDate() {
		return createdDate;
	}


	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
	}


	public String getStatusChangeDate() {
		return statusChangeDate;
	}


	public void setStatusChangeDate(String statusChangeDate) {
		this.statusChangeDate = statusChangeDate;
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

	
}
