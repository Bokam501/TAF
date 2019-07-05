package com.hcl.atf.taf.model.json;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.hcl.atf.taf.controller.utilities.DateUtility;
import com.hcl.atf.taf.model.TestFactoryLab;

public class JsonTestFactoryLab {
	private static final Log log = LogFactory.getLog(JsonProductLocale.class);

	public JsonTestFactoryLab(){
		
	}
	
	@JsonProperty
	private Integer testFactoryLabId;
	@JsonProperty
	private String testFactoryLabName;
	@JsonProperty
	private String displayName;
	@JsonProperty
	private String city;
	@JsonProperty
	private String state;
	@JsonProperty
	private String country;
	@JsonProperty
	private Integer status;
	@JsonProperty
	private String createdDate;
	@JsonProperty
	private String modifiedDate;
	
	public JsonTestFactoryLab(TestFactoryLab testFactoryLab ){
		
		this.testFactoryLabId=testFactoryLab.getTestFactoryLabId();
		this.testFactoryLabName=testFactoryLab.getTestFactoryLabName();
		this.displayName=testFactoryLab.getDisplayName();
		this.city=testFactoryLab.getCity();
		this.state=testFactoryLab.getState();
		this.country=testFactoryLab.getCountry();
	
		if(testFactoryLab.getStatus() != null){
			this.status = testFactoryLab.getStatus();
		}
		if(testFactoryLab.getCreatedDate()!=null){
			this.createdDate = DateUtility.dateToStringInSecond(testFactoryLab.getCreatedDate());
		}
		if(testFactoryLab.getModifiedDate()!=null){
			this.modifiedDate = DateUtility.dateToStringInSecond(testFactoryLab.getModifiedDate());
			}
		
	}
	
	@JsonIgnore
	public TestFactoryLab  getTestFactoryLab(){
		
		TestFactoryLab testFactoryLab=new TestFactoryLab();
		
		if(this.testFactoryLabId!=null){
			testFactoryLab.setTestFactoryLabId(this.testFactoryLabId);
		}
		testFactoryLab.setTestFactoryLabName(this.testFactoryLabName);
	
		testFactoryLab.setDisplayName(this.displayName);
		testFactoryLab.setCity(this.city);
		testFactoryLab.setState(this.state);
		testFactoryLab.setCountry(this.country);
		
		if(this.status != null ){			
			testFactoryLab.setStatus(this.status);			
		}else{
			testFactoryLab.setStatus(0);	
		}
		if(this.createdDate == null || this.createdDate.trim().isEmpty()) {
			testFactoryLab.setCreatedDate(DateUtility.getCurrentTime());
		} else {
		
			testFactoryLab.setCreatedDate(DateUtility.dateFormatWithOutSeconds(this.createdDate));
		}
		testFactoryLab.setModifiedDate(DateUtility.getCurrentTime());
		
		return testFactoryLab;
		
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
	public String getDisplayName() {
		return displayName;
	}
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
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
}
