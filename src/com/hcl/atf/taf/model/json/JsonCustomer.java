package com.hcl.atf.taf.model.json;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.hcl.atf.taf.model.Customer;


public class JsonCustomer implements java.io.Serializable {
	private static final Log log = LogFactory
			.getLog(JsonCustomer.class);
	@JsonProperty
	private Integer customerId;
	@JsonProperty
	private String customerName;
	@JsonProperty
	private String description;
	@JsonProperty
	private String customerRefId;
	
	@JsonProperty
	private Integer status;
	@JsonProperty
	private String domain;
	@JsonProperty
	private String ldapURL;
	@JsonProperty
	private String imageURI;
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
	

	public JsonCustomer() {
	}

	public JsonCustomer(Customer customer) {
		this.customerId = customer.getCustomerId();
		this.customerName = customer.getCustomerName();
		this.customerRefId = customer.getCustomerRefId();
		this.description = customer.getDescription();
		
		if(customer.getStatus() != null){
			this.status = customer.getStatus();
		}
		
		if(customer.getDomain() != null){
			this.domain = customer.getDomain();
		}
		
		if(customer.getLdapURL() != null){
			this.ldapURL = customer.getLdapURL();
		}
		
		if(customer.getImageURI() != null){
			this.imageURI = customer.getImageURI();
		}
		
	}
	
	@JsonIgnore
	public Customer getCustomer(){
		Customer customer = new Customer();
		if(customerId != null){
			customer.setCustomerId(customerId);
		}
		
		customer.setCustomerName(customerName);
		customer.setCustomerRefId(customerRefId);
		customer.setDescription(description);
		if(this.status != null){
			customer.setStatus(status);
		}else{
			customer.setStatus(0);
		}
		
		if(this.domain != null){
			customer.setDomain(domain);
		}
		
		if(this.ldapURL != null){
			customer.setLdapURL(ldapURL);
		}
		
		if(this.imageURI != null){
			customer.setImageURI(imageURI);
		}
		
		log.info("Getter Id"+customerId+", Name: "+customerName+", Ref Id :"+customerRefId+", St: "+status);
		return customer;
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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getCustomerRefId() {
		return customerRefId;
	}

	public void setCustomerRefId(String customerRefId) {
		this.customerRefId = customerRefId;
	}
	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getDomain() {
		return domain;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}

	public String getLdapURL() {
		return ldapURL;
	}

	public void setLdapURL(String ldapURL) {
		this.ldapURL = ldapURL;
	}

	public String getImageURI() {
		return imageURI;
	}

	public void setImageURI(String imageURI) {
		this.imageURI = imageURI;
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