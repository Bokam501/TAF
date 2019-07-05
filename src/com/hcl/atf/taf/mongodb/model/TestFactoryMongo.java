package com.hcl.atf.taf.mongodb.model;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.hcl.atf.taf.model.TestFactory;

@Document(collection = "testFactory")
public class TestFactoryMongo {	
	@Id
	private String id;
	private Integer testFactoryId;	
	private String testFactoryName;	
	private String description;	
	private String displayName;	
	private Integer testCentersId;
	private String testCentersName;	
	private String city;	
	private String state;	
	private String country;	
	private Integer status;	
	private String parentStatus;
	private Date createdDate;	
	private Date modifiedDate;	

	public TestFactoryMongo(){			
		
	}
	public TestFactoryMongo(Integer testFactoryId,
			String testFactoryName, String displayName, String city, String state,
			String country, Integer status, Date createdDate, Date modifiedDate,
			String description, Integer testFactoryLabId) {
		this. id=id;	
		this.testFactoryId= testFactoryId;
		this. testFactoryName=testFactoryName;
		this.displayName=displayName;
		this.city=city;
		this.state=state;
		this.country=country;
		this.status= status;
		this.createdDate=createdDate;
		this.modifiedDate= modifiedDate;
		this.description=description;		
		this.testCentersId=testFactoryLabId;
	}
	public TestFactoryMongo(TestFactory testFactory) {
		
		this. id=testFactory.getTestFactoryId()+"";	
		this.testFactoryId= testFactory.getTestFactoryId();	;
		this. testFactoryName=testFactory.getTestFactoryName();
		this.displayName=testFactory.getDisplayName();
		this.city=testFactory.getCity();
		this.state=testFactory.getState();
		this.country=testFactory.getCountry();
		this.status= testFactory.getStatus();
		if(testFactory.getTestFactoryLab().getStatus() == 1){
			this.parentStatus = "Active";	
		}else{
			this.parentStatus = "InActive";
		}
		this.createdDate=testFactory.getCreatedDate();
		this.modifiedDate= testFactory.getModifiedDate();
		this.description=testFactory.getDescription();		
		this.testCentersId=testFactory.getTestFactoryLab().getTestFactoryLabId();
		this.testCentersName=testFactory.getTestFactoryLab().getTestFactoryLabName();
		
	}
	
	public String getId() {
		return id;
	}
	public void setId(String _id) {
		this.id = _id;
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
	public Date getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}
	public Date getModifiedDate() {
		return modifiedDate;
	}
	public void setModifiedDate(Date modifiedDate) {
		this.modifiedDate = modifiedDate;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
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
