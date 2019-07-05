package com.hcl.atf.taf.model.json;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.hcl.atf.taf.controller.utilities.DateUtility;
import com.hcl.atf.taf.model.ProductLocale;
import com.hcl.atf.taf.model.ProductMaster;

public class JsonProductLocale {
	private static final Log log = LogFactory.getLog(JsonProductLocale.class);
	@JsonProperty
	private Integer productLocaleId;
	@JsonProperty
	private String localeName;
	@JsonProperty
	private String description;
	@JsonProperty
	private String createdDate;
	@JsonProperty
	private String modifiedDate;
	@JsonProperty
	private Integer status;
	@JsonProperty
	private Integer productMasterId;
	
	public JsonProductLocale(){
		
	}
	
public JsonProductLocale(ProductLocale locale){
	this.productLocaleId=locale.getProductLocaleId();
	this.localeName=locale.getLocaleName();
	this.description=locale.getDescription();
	if(locale.getStatus() != null){
		this.status = locale.getStatus();
	}
	if(locale.getProductMaster()!=null){
		this.productMasterId=locale.getProductMaster().getProductId();
	}
	if(locale.getCreatedDate()!=null){
		this.createdDate = DateUtility.dateToStringInSecond(locale.getCreatedDate());
	}
	if(locale.getModifiedDate()!=null){
		this.modifiedDate = DateUtility.dateToStringInSecond(locale.getModifiedDate());
		}
	
}
@JsonIgnore
public ProductLocale  getProductLocale(){
	ProductLocale locale=new ProductLocale();
	if(productLocaleId!=null){
		locale.setProductLocaleId(productLocaleId);
	}
	locale.setLocaleName(localeName);
	locale.setDescription(description);
	
	if(this.status != null ){			
		locale.setStatus(status);			
	}else{
		locale.setStatus(0);	
	}
	if(this.createdDate == null || this.createdDate.trim().isEmpty()) {
		locale.setCreatedDate(DateUtility.getCurrentTime());
	} else {
	
			locale.setCreatedDate(DateUtility.dateFormatWithOutSeconds(this.createdDate));
	}
		locale.setModifiedDate(DateUtility.getCurrentTime());
	
	
	
    if(this.productMasterId!=null){
	ProductMaster product = new ProductMaster();
	product.setProductId(productMasterId);
	locale.setProductMaster(product);
	}
	
	return locale;
	
}


	public Integer getProductLocaleId() {
		return productLocaleId;
	}
	public void setProductLocaleId(Integer productLocaleId) {
		this.productLocaleId = productLocaleId;
	}
	public String getLocaleName() {
		return localeName;
	}
	public void setLocaleName(String localeName) {
		this.localeName = localeName;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
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
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public Integer getProductMasterId() {
		return productMasterId;
	}
	public void setProductMasterId(Integer productMasterId) {
		this.productMasterId = productMasterId;
	}
	
}
