package com.hcl.atf.taf.model.json;


import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.hcl.atf.taf.model.TestCaseList;
import com.hcl.atf.taf.model.TestCaseStepsList;

public class JsonTestCaseStepsList implements java.io.Serializable {

	@JsonProperty
	private Integer testStepId;		
	@JsonProperty
	private String testStepName;
	@JsonProperty
	private String testStepDescription;
	@JsonProperty
	private String testStepInput;
	@JsonProperty
	private String testStepExpectedOutput;
	
	@JsonProperty
	private Integer testCaseId;
	//Changes for TestManagement tools
	@JsonProperty
	private String testStepCode;
	@JsonProperty
	private String testStepSource;
	@JsonProperty
	private Date testStepCreatedDate;
	@JsonProperty
	private Date testStepLastUpdatedDate;
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
	
	@JsonProperty	
	private Integer status;
	
	
	public JsonTestCaseStepsList() {		
		this.testCaseId = new Integer(0);
	}

	public JsonTestCaseStepsList(TestCaseStepsList testCaseStepsList) {
	
		if(testCaseStepsList.getTestStepId()!=null)
			this.testStepId = testCaseStepsList.getTestStepId();
		if(testCaseStepsList.getTestStepName()!=null)
			this.testStepName = testCaseStepsList.getTestStepName();
		if(testCaseStepsList.getTestStepDescription()!=null)
			this.testStepDescription = testCaseStepsList.getTestStepDescription();
		if(testCaseStepsList.getTestStepInput()!=null)
			this.testStepInput = testCaseStepsList.getTestStepInput();
		if(testCaseStepsList.getTestStepExpectedOutput()!=null)
			this.testStepExpectedOutput = testCaseStepsList.getTestStepExpectedOutput();
		if(testCaseStepsList.getTestCaseList().getTestCaseId()!=null)
			this.testCaseId = testCaseStepsList.getTestCaseList().getTestCaseId();
		
		if(testCaseStepsList.getTestStepCode()!=null)
			this.testStepCode = testCaseStepsList.getTestStepCode();
		if(testCaseStepsList.getTestStepSource()!=null)
			this.testStepSource = testCaseStepsList.getTestStepSource();
		if(testCaseStepsList.getTestStepCreatedDate()!=null)
			this.testStepCreatedDate = testCaseStepsList.getTestStepCreatedDate();
		if(testCaseStepsList.getTestStepLastUpdatedDate()!=null)
			this.testStepLastUpdatedDate = testCaseStepsList.getTestStepLastUpdatedDate();
		if(testCaseStepsList.getStatus() != null)
			this.status = testCaseStepsList.getStatus();
		
	}

	

	@JsonIgnore
	public TestCaseStepsList getTestCaseList(){
		
		TestCaseStepsList testCaseStepsList = new TestCaseStepsList();
		testCaseStepsList.setTestStepId(testStepId);
		testCaseStepsList.setTestStepName(testStepName);
		testCaseStepsList.setTestStepDescription(testStepDescription);
		testCaseStepsList.setTestStepExpectedOutput(testStepExpectedOutput);
		testCaseStepsList.setTestStepInput(testStepInput);
		TestCaseList testCaseList = new TestCaseList();
		testCaseList.setTestCaseId(testCaseId);
		testCaseStepsList.setTestCaseList(testCaseList);
		testCaseStepsList.setTestStepCode(testStepCode);
		testCaseStepsList.setTestStepSource(testStepSource);
		testCaseStepsList.setTestStepCreatedDate(testStepCreatedDate);
		testCaseStepsList.setTestStepLastUpdatedDate(testStepLastUpdatedDate);
		testCaseStepsList.setStatus(status);
		
		return testCaseStepsList;
	}

	public Integer getTestCaseId() {
		return testCaseId;
	}

	public void setTestCaseId(Integer testCaseId) {
		this.testCaseId = testCaseId;
	}
	
	public Integer getTestStepId() {
		return testStepId;
	}

	public void setTestStepId(Integer testStepId) {
		this.testStepId = testStepId;
	}

	public String getTestStepName() {
		return testStepName;
	}

	public void setTestStepName(String testStepName) {
		this.testStepName = testStepName;
	}

	public String getTestStepDescription() {
		return testStepDescription;
	}

	public void setTestStepDescription(String testStepDescription) {
		this.testStepDescription = testStepDescription;
	}

	public String getTestStepInput() {
		return testStepInput;
	}

	public void setTestStepInput(String testStepInput) {
		this.testStepInput = testStepInput;
	}

	public String getTestStepExpectedOutput() {
		return testStepExpectedOutput;
	}

	public void setTestStepExpectedOutput(String testStepExpectedOutput) {
		this.testStepExpectedOutput = testStepExpectedOutput;
	}

	//Changes for TestManagement Tools
	public String getTestStepCode() {
		return testStepCode;
	}

	public void setTestStepCode(String testStepCode) {
		this.testStepCode = testStepCode;
	}

	public String getTestStepSource() {
		return testStepSource;
	}

	public void setTestStepSource(String testStepSource) {
		this.testStepSource = testStepSource;
	}

	public Date getTestStepCreatedDate() {
		return testStepCreatedDate;
	}

	public void setTestStepCreatedDate(Date testStepCreatedDate) {
		this.testStepCreatedDate = testStepCreatedDate;
	}

	public Date getTestStepLastUpdatedDate() {
		return testStepLastUpdatedDate;
	}

	public void setTestStepLastUpdatedDate(Date testStepLastUpdatedDate) {
		this.testStepLastUpdatedDate = testStepLastUpdatedDate;
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

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}
	
	

}
