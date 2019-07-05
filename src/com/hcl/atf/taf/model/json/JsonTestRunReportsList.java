package com.hcl.atf.taf.model.json;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.hcl.atf.taf.model.custom.TestRunReportsList;

public class JsonTestRunReportsList implements java.io.Serializable {

	@JsonProperty
	private Integer testRunNo;
	@JsonProperty
	private Integer testRunListId;
	@JsonProperty
	private String productName;
	@JsonProperty
	private String productVersionName;
	@JsonProperty
	private String testRunTriggeredTime;	
	@JsonProperty
	private String testRunResultStatus;	
	@JsonProperty
	private Integer testRunConfigurationChildId;	
	@JsonProperty
	private String testRunConfigurationName;	

	public JsonTestRunReportsList(){
		
	}
	
	public Integer getTestRunNo() {
		return testRunNo;
	}

	public void setTestRunNo(Integer testRunNo) {
		this.testRunNo = testRunNo;
	}

	public Integer getTestRunListId() {
		return testRunListId;
	}

	public void setTestRunListId(Integer testRunListId) {
		this.testRunListId = testRunListId;
	}
	
	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getProductVersionName() {
		return productVersionName;
	}

	public void setProductVersionName(String productVersionName) {
		this.productVersionName = productVersionName;
	}

	public String getTestRunTriggeredTime() {
		return testRunTriggeredTime;
	}

	public void setTestRunTriggeredTime(String testRunTriggeredTime) {
		this.testRunTriggeredTime = testRunTriggeredTime;
	}

	public String getTestRunResultStatus() {
		return testRunResultStatus;
	}

	public void setTestRunResultStatus(String testRunResultStatus) {
		this.testRunResultStatus = testRunResultStatus;
	}

	public JsonTestRunReportsList(TestRunReportsList testRunReportsList){
		
		testRunNo=testRunReportsList.getTestRunNo();
		testRunListId=testRunReportsList.getTestRunListId();
		productName=testRunReportsList.getProductName();
		productVersionName=testRunReportsList.getProductVersionName();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		if(testRunReportsList.getTestRunTriggeredTime()!=null){
			testRunTriggeredTime=sdf.format(testRunReportsList.getTestRunTriggeredTime());
		} else {
			testRunTriggeredTime = "";
		}
		testRunResultStatus=testRunReportsList.getTestRunResultStatus();
		testRunConfigurationChildId=testRunReportsList.getTestRunConfigurationChildId();	
		testRunConfigurationName=testRunReportsList.getTestRunConfigurationName();
	}
	@JsonIgnore
	public TestRunReportsList getTestRunReportsList(){
		TestRunReportsList testRunReportsList = new TestRunReportsList();
		testRunReportsList.setTestRunNo(testRunNo);
		testRunReportsList.setTestRunListId(testRunListId);
		testRunReportsList.setProductName(productName);
		testRunReportsList.setProductVersionName(productVersionName);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			testRunReportsList.setTestRunTriggeredTime(sdf.parse(testRunTriggeredTime));
		} catch (ParseException e) {
		}
		testRunReportsList.setTestRunResultStatus(testRunResultStatus);
		testRunReportsList.setTestRunConfigurationChildId(testRunConfigurationChildId);
		testRunReportsList.setTestRunConfigurationName(testRunConfigurationName);
		return testRunReportsList;
	}

	public Integer getTestRunConfigurationChildId() {
		return testRunConfigurationChildId;
	}

	public void setTestRunConfigurationChildId(Integer testRunConfigurationChildId) {
		this.testRunConfigurationChildId = testRunConfigurationChildId;
	}

	public String getTestRunConfigurationName() {
		return testRunConfigurationName;
	}

	public void setTestRunConfigurationName(String testRunConfigurationName) {
		this.testRunConfigurationName = testRunConfigurationName;
	}
}
