package com.hcl.atf.taf.model.json;

import java.util.HashSet;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.simple.JSONObject;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.hcl.atf.taf.controller.utilities.DateUtility;
import com.hcl.atf.taf.model.EngagementTypeMaster;
import com.hcl.atf.taf.model.TestFactory;
import com.hcl.atf.taf.model.TestFactoryLab;
import com.hcl.atf.taf.model.TestfactoryResourcePool;

public class JsonTestFactory {
	private static final Log log = LogFactory.getLog(JsonTestFactory.class);
	

    @JsonProperty
	private Integer testFactoryId;
    @JsonProperty
	private String testFactoryName;
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
    @JsonProperty
	private String description;
    @JsonProperty
	private Integer testFactoryLabId;
    @JsonProperty
   	private Integer resourcePoolId;
    @JsonProperty
   	private Integer engagementTypeId;
    @JsonProperty
   	private String engagementTypeName;
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
    
public JsonTestFactory(){
		
	}
    
    
public Integer getResourcePoolId() {
		return resourcePoolId;
	}
	public void setResourcePoolId(Integer resourcePoolId) {
		this.resourcePoolId = resourcePoolId;
	}

    public JsonTestFactory(TestFactory  testFactory){
    	
    	this.testFactoryId=testFactory.getTestFactoryId();
    	this.testFactoryName=testFactory.getTestFactoryName();
    	
    	Set<TestfactoryResourcePool> testfactoryResourcePoolSet = testFactory.getTestfactoryResourcePoolList();
		if (testfactoryResourcePoolSet.size() != 0)
		{
			
			TestfactoryResourcePool testfactoryResourcePool = testfactoryResourcePoolSet.iterator().next();
			if (testfactoryResourcePool != null)
			{
				resourcePoolId = testfactoryResourcePool.getResourcePoolId();
			}
		}
		
    			
    	this.displayName=testFactory.getDisplayName();
    	this.city=testFactory.getCity();
    	this.state=testFactory.getState();
    	this.country=testFactory.getCountry();
    	this.description=testFactory.getDescription();
    	
    	if(testFactory.getStatus() != null){
    		this.status = testFactory.getStatus();
    	}
    	if(testFactory.getTestFactoryLab()!=null){
    		this.testFactoryLabId=testFactory.getTestFactoryLab().getTestFactoryLabId();
    	}
    	if(testFactory.getEngagementTypeMaster()!=null){
    		this.engagementTypeId = testFactory.getEngagementTypeMaster().getEngagementTypeId();
    		this.engagementTypeName = testFactory.getEngagementTypeMaster().getEngagementTypeName();
    	}
    	if(testFactory.getCreatedDate()!=null){
    		this.createdDate = DateUtility.dateToStringWithSeconds1(testFactory.getCreatedDate());
    	}
    	if(testFactory.getModifiedDate()!=null){
    		this.modifiedDate = DateUtility.dateformatWithOutTime(testFactory.getModifiedDate());
    		}
    	
    }
    
    @JsonIgnore
    public TestFactory  getTestFactory(){
    	
    	log.info("testFactoryId:-->"+this.testFactoryId);
    	log.info("testFactoryName:-->"+this.testFactoryName);
    	log.info("this.resourcePoolId-->"+this.resourcePoolId);
    	TestFactory testFactory=new TestFactory();
    	if(this.testFactoryId!=null){
    		testFactory.setTestFactoryId(this.testFactoryId);
    	}
    	testFactory.setTestFactoryName(testFactoryName);
    	testFactory.setDisplayName(displayName);
    	testFactory.setCity(city);
    	testFactory.setState(state);
    	testFactory.setCountry(country);
    	testFactory.setDescription(description);
    	testFactory.setDescription(description);
    	
    	if(this.status != null ){			
    		testFactory.setStatus(status);			
    	}else{
    		testFactory.setStatus(0);	
    	}
    	
    	if(this.createdDate == null || this.createdDate.trim().isEmpty()) {
    		testFactory.setCreatedDate(DateUtility.getCurrentTime());
		} else {
			testFactory.setCreatedDate(DateUtility.toFormatDate(this.createdDate));
		}
    	testFactory.setModifiedDate(DateUtility.getCurrentTime());
		
    	
    
    	if(this.testFactoryLabId!=null){
        	TestFactoryLab tfLab=new TestFactoryLab();
        	tfLab.setTestFactoryLabId(this.testFactoryLabId);
        	testFactory.setTestFactoryLab(tfLab);
    	
    	}
    	
    	if(this.engagementTypeId != null){
    		EngagementTypeMaster engmntType = new EngagementTypeMaster();
    		engmntType.setEngagementTypeId(engagementTypeId);
    		testFactory.setEngagementTypeMaster(engmntType);
    	}
    if(resourcePoolId != null){
    	TestfactoryResourcePool testfactoryResourcePool=new TestfactoryResourcePool();
    	testfactoryResourcePool.setResourcePoolId(this.resourcePoolId);
    	Set<TestfactoryResourcePool> testfactoryResourcePoolSet = new HashSet<TestfactoryResourcePool> ();
    	testfactoryResourcePoolSet.add(testfactoryResourcePool);
    	
    	testFactory.setTestfactoryResourcePoolList(testfactoryResourcePoolSet);
    }
    	
   	
    	return testFactory;
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
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Integer getTestFactoryLabId() {
		return testFactoryLabId;
	}
	public void setTestFactoryLabId(Integer testFactoryLabId) {
		this.testFactoryLabId = testFactoryLabId;
	}


	public Integer getEngagementTypeId() {
		return engagementTypeId;
	}


	public void setEngagementTypeId(Integer engagementTypeId) {
		this.engagementTypeId = engagementTypeId;
	}


	public String getEngagementTypeName() {
		return engagementTypeName;
	}


	public void setEngagementTypeName(String engagementTypeName) {
		this.engagementTypeName = engagementTypeName;
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
    
	
	@JsonIgnore
	public JSONObject getCleanJson() {
		JSONObject jsonTestCaseObject= new JSONObject();
		try {
			jsonTestCaseObject.put("testFactoryId", testFactoryId);
			jsonTestCaseObject.put("testFactoryName", testFactoryName);
			jsonTestCaseObject.put("description", description);
			if(status == 1) {
				jsonTestCaseObject.put("status", "ACTIVE");
			} else {
				jsonTestCaseObject.put("status", "INACTIVE");
			}
			jsonTestCaseObject.put("createdDate", createdDate);
			jsonTestCaseObject.put("createdDate", modifiedDate);
			jsonTestCaseObject.put("testFactoryLabId", testFactoryLabId);
			jsonTestCaseObject.put("displayName", displayName);
			jsonTestCaseObject.put("engagementTypeId", engagementTypeId);
	    }catch(Exception e) {
	    	log.error("Error while json formatting Engagement Object");
	    }
		return jsonTestCaseObject;
	}
}
