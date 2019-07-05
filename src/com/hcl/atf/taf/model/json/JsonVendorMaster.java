package com.hcl.atf.taf.model.json;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.hcl.atf.taf.controller.utilities.DateUtility;
import com.hcl.atf.taf.model.VendorMaster;

public class JsonVendorMaster implements java.io.Serializable {
	
	private static final Log log = LogFactory.getLog(JsonTimeSheetEntryMaster.class);
	
	
	@JsonProperty
	private Integer vendorId;
	@JsonProperty
	private String registeredCompanyName;
	@JsonProperty
	private String domain;
	@JsonProperty
	private String address;
	@JsonProperty
	private String city;
	@JsonProperty
	private String contactEmailId;
	@JsonProperty
	private String contactNumber;
	@JsonProperty
	private String createdDate;
	@JsonProperty
	private String modifiedDate;
	@JsonProperty
	private String spocName;
	@JsonProperty
	private Integer status;
	@JsonProperty
	private String modifiedField;
	@JsonProperty
	private String 	modifiedFieldTitle;	
	@JsonProperty
	private String oldFieldID;
	@JsonProperty
	private String	oldFieldValue;
	@JsonProperty
	private String	modifiedFieldID;
	@JsonProperty	
	private String modifiedFieldValue;
	
	public JsonVendorMaster(){
		
	}
	public JsonVendorMaster(VendorMaster VendorMaster){
		
		if(VendorMaster.getVendorId()!=null){
		this.vendorId=VendorMaster.getVendorId();
		}
		this.registeredCompanyName=VendorMaster.getRegisteredCompanyName();
		this.domain=VendorMaster.getDomain();
		this.address=VendorMaster.getAddress();
		this.city=VendorMaster.getCity();
		this.contactEmailId=VendorMaster.getContactEmailId();
		this.contactNumber=VendorMaster.getContactNumber();
		this.spocName=VendorMaster.getSpocName();
		if(VendorMaster.getCreatedDate()!=null){
			this.createdDate = DateUtility.dateToStringInSecond(VendorMaster.getCreatedDate());
		}
		if(VendorMaster.getModifiedDate()!=null){
			this.modifiedDate = DateUtility.dateToStringInSecond(VendorMaster.getModifiedDate());
			}
		if(VendorMaster.getStatus() != null){
			this.status = VendorMaster.getStatus();
		}
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

	public String getSpocName() {
		return spocName;
	}

	public void setSpocName(String spocName) {
		this.spocName = spocName;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	@JsonIgnore
	public VendorMaster getVendorMaster() {
		VendorMaster VendorMaster = new VendorMaster();
		
		VendorMaster.setRegisteredCompanyName(this.registeredCompanyName);
		VendorMaster.setDomain(this.domain);
		VendorMaster.setAddress(this.address);
		VendorMaster.setCity(this.city);
		VendorMaster.setContactEmailId(this.contactEmailId);
		VendorMaster.setContactNumber(this.contactNumber);
		VendorMaster.setSpocName(this.spocName);
   if(this.vendorId!=null){
	VendorMaster.setVendorId(this.vendorId);
	  }
   if(this.status != null ){			
	   VendorMaster.setStatus(status);			
	}else{
		VendorMaster.setStatus(0);	
	}
	VendorMaster.setModifiedDate(DateUtility.getCurrentTime());
		return VendorMaster;
	
	}
	
	
	
	
	public Integer getVendorId() {
		return vendorId;
	}


	public void setVendorId(Integer vendorId) {
		this.vendorId = vendorId;
	}


	public String getRegisteredCompanyName() {
		return registeredCompanyName;
	}
	
	public void setRegisteredCompanyName(String registeredCompanyName) {
		this.registeredCompanyName = registeredCompanyName;
	}
	
	public String getDomain() {
		return domain;
	}
	
	public void setDomain(String domain) {
		this.domain = domain;
	}
	
	public String getAddress() {
		return address;
	}
	
	public void setAddress(String address) {
		this.address = address;
	}
	
	public String getCity() {
		return city;
	}
	
	public void setCity(String city) {
		this.city = city;
	}
	
	public String getContactEmailId() {
		return contactEmailId;
	}
	
	public void setContactEmailId(String contactEmailId) {
		this.contactEmailId = contactEmailId;
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
	public String getContactNumber() {
		return contactNumber;
	}
	
	public void setContactNumber(String contactNumber) {
		this.contactNumber = contactNumber;
	}
	
}
