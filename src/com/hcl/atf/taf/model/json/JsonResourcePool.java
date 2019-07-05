package com.hcl.atf.taf.model.json;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.hcl.atf.taf.controller.utilities.DateUtility;
import com.hcl.atf.taf.model.TestFactoryLab;
import com.hcl.atf.taf.model.TestfactoryResourcePool;


public class JsonResourcePool implements java.io.Serializable {
	private static final Log log = LogFactory
			.getLog(JsonResourcePool.class);
	@JsonProperty
	private Integer resourcePoolId;	
	@JsonProperty
	private String resourcePoolName;
	@JsonProperty
	private String displayName;
	@JsonProperty
	private Integer status;
	@JsonProperty
	private String city;
	@JsonProperty
	private String state;
	@JsonProperty
	private String country;
	@JsonProperty
	private String createdDate;
	@JsonProperty
	private String modifiedDate;
	@JsonProperty	
	private Integer testFactoryLabId;
	@JsonProperty	
	private String testFactoryLabName;
	@JsonProperty
	private String modifiedField;
	@JsonProperty
	private String 	modifiedFieldTitle;
	@JsonProperty
	private String	oldFieldValue;
	@JsonProperty	
	private String modifiedFieldValue;
	
	public JsonResourcePool() {
	}

	public JsonResourcePool(TestfactoryResourcePool resourcePool) {		
		this.resourcePoolId = resourcePool.getResourcePoolId();
		this.resourcePoolName = resourcePool.getResourcePoolName();
		this.displayName = resourcePool.getDisplayName();		
		this.city = resourcePool.getCity();
		this.state = resourcePool.getState();
		this.country = resourcePool.getCountry();
		if(resourcePool.getStatus() != null){
			this.status = resourcePool.getStatus();
		}			
		if(resourcePool.getCreatedDate()!=null){
			this.createdDate = DateUtility.dateformatWithOutTime(resourcePool.getCreatedDate());
		}
		if(resourcePool.getModifiedDate()!=null){
			this.modifiedDate = DateUtility.dateformatWithOutTime(resourcePool.getModifiedDate());
		}
		
		TestFactoryLab testFactoryLab= resourcePool.getTestFactoryLab();
		if(testFactoryLab != null){
			this.testFactoryLabId=testFactoryLab.getTestFactoryLabId();
		}else{
			this.testFactoryLabId=0;
			this.testFactoryLabName=null;
		}
	}

	@JsonIgnore
	public TestfactoryResourcePool getResourcePool(){
		TestfactoryResourcePool resourcePool = new TestfactoryResourcePool();
		resourcePool.setResourcePoolId(resourcePoolId);
		resourcePool.setResourcePoolName(resourcePoolName);
		resourcePool.setDisplayName(displayName);	
		resourcePool.setStatus(status);
		resourcePool.setCity(city);
		resourcePool.setState(state);
		resourcePool.setCountry(country);
		
		if(this.createdDate == null || this.createdDate.trim().isEmpty()) {
			resourcePool.setCreatedDate(DateUtility.getCurrentTime());
		}else{			
			resourcePool.setCreatedDate(DateUtility.dateFormatWithOutSecondsddMMyyyy(this.createdDate));						
		}		
		resourcePool.setModifiedDate(DateUtility.getCurrentTime());
	
		TestFactoryLab testFactoryLab = new TestFactoryLab();
		testFactoryLab.setTestFactoryLabId(testFactoryLabId);
		return resourcePool;		
	}

	public Integer getResourcePoolId() {
		return resourcePoolId;
	}

	public void setResourcePoolId(Integer resourcePoolId) {
		this.resourcePoolId = resourcePoolId;
	}

	public Integer getTestFactoryLabId() {
		return testFactoryLabId;
	}

	public void setTestFactoryLabId(Integer testFactoryLabId) {
		this.testFactoryLabId = testFactoryLabId;
	}

	public String getTestFactoryLabName() {
		return testFactoryLabName;
	}

	public void setTestFactoryLabName(String testFactoryLabName) {
		this.testFactoryLabName = testFactoryLabName;
	}

	public String getResourcePoolName() {
		return resourcePoolName;
	}

	public void setResourcePoolName(String resourcePoolName) {
		this.resourcePoolName = resourcePoolName;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
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

	public String getOldFieldValue() {
		return oldFieldValue;
	}

	public void setOldFieldValue(String oldFieldValue) {
		this.oldFieldValue = oldFieldValue;
	}

	public String getModifiedFieldValue() {
		return modifiedFieldValue;
	}

	public void setModifiedFieldValue(String modifiedFieldValue) {
		this.modifiedFieldValue = modifiedFieldValue;
	}

}
