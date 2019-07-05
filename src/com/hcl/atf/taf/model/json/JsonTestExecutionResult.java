package com.hcl.atf.taf.model.json;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.hcl.atf.taf.controller.utilities.SpecialCharacterEncoder;
import com.hcl.atf.taf.model.TestExecutionResult;
import com.hcl.atf.taf.model.TestResultStatusMaster;
import com.hcl.atf.taf.model.TestRunList;
import com.hcl.atf.taf.model.TestSuiteList;
import com.hcl.atf.taf.model.dto.TestStepExecutionResultDTO;

public class JsonTestExecutionResult implements java.io.Serializable {

	@JsonProperty
	private Integer testExecutionResultId;
	@JsonProperty
	private String testResultStatus;
	@JsonProperty
	private Integer testRunListId;
	@JsonProperty
	private Integer testSuiteId;
	@JsonProperty
	private Integer testStepId;
	@JsonProperty
	private String testStepName;	
	@JsonProperty
	private Date testStepCreatedDate;
	@JsonProperty
	private Date testStepLastUpdatedDate;
	@JsonProperty
	private String testStep;
	@JsonProperty
	private String testStepDescription;
	@JsonProperty
	private String testStepInput;
	@JsonProperty
	private String testStepExpectedOutput;
	@JsonProperty
	private String testStepObservedOutput;
	@JsonProperty
	private Integer testCaseId;
	@JsonProperty
	private String testCase;
	@JsonProperty
	private String testCaseDescription;
	@JsonProperty
	private String testCaseScriptQualifiedName;
	@JsonProperty
	private String testCasePriority;
	@JsonProperty
	private String testCaseScriptFileName;
	@JsonProperty
	private String screenShotPath;
	@JsonProperty
	private String screenShotLabel;
	@JsonProperty
	private String startTime;
	@JsonProperty
	private String endTime;
	@JsonProperty
	private String executionRemarks;
	@JsonProperty
	private String failureReason;
	@JsonProperty
	private Integer teststepexecutionresultId;
	//Added by Rajesh. Needed for getting test case, step IDs, Code from Terminal
	//These fields will not be persisted in the database
	@JsonProperty
	private String testCaseCode;
	@JsonProperty
	private String testStepCode;


	public JsonTestExecutionResult() {
	}
	public JsonTestExecutionResult(TestExecutionResult testExecutionResult) {
		
		testExecutionResultId=testExecutionResult.getTestExecutionResultId();
		if(testExecutionResult.getTestResultStatusMaster()!=null)
			testResultStatus=testExecutionResult.getTestResultStatusMaster().getTestResultStatus();
		if(testExecutionResult.getTestRunList()!=null)
			testRunListId=testExecutionResult.getTestRunList().getTestRunListId();
		if(testExecutionResult.getTestSuiteList()!=null)
			testSuiteId=testExecutionResult.getTestSuiteList().getTestSuiteId();
		
		testStep=testExecutionResult.getTestStep();
		testStepDescription=testExecutionResult.getTestStepDescription();
		testStepInput=testExecutionResult.getTestStepInput();
		testStepExpectedOutput=testExecutionResult.getTestStepExpectedOutput();
		testStepObservedOutput=testExecutionResult.getTestStepObservedOutput();		
		testCase=testExecutionResult.getTestCase();
		testCaseDescription=testExecutionResult.getTestCaseDescription();
		
		screenShotPath=testExecutionResult.getScreenShotPath();
		screenShotLabel=testExecutionResult.getScreenShotLabel();
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		if (testExecutionResult.getStartTime() != null)
			startTime = sdf.format(testExecutionResult.getStartTime());
		if (testExecutionResult.getEndTime() != null)
			endTime = sdf.format(testExecutionResult.getEndTime());
		executionRemarks = testExecutionResult.getExecutionRemarks();
		failureReason = testExecutionResult.getFailureReason();
	}
	
	public JsonTestExecutionResult(TestExecutionResult testExecutionResult, boolean encode) {
		
		testExecutionResultId=testExecutionResult.getTestExecutionResultId();
		if(testExecutionResult.getTestResultStatusMaster()!=null)
			testResultStatus=testExecutionResult.getTestResultStatusMaster().getTestResultStatus();
		if(testExecutionResult.getTestRunList()!=null)
			testRunListId=testExecutionResult.getTestRunList().getTestRunListId();
		if(testExecutionResult.getTestSuiteList()!=null)
			testSuiteId=testExecutionResult.getTestSuiteList().getTestSuiteId();
		
		testStep=SpecialCharacterEncoder.encodeReservedCharacters(testExecutionResult.getTestStep());
		testStepDescription=SpecialCharacterEncoder.encodeReservedCharacters(testExecutionResult.getTestStepDescription());
		testStepInput=SpecialCharacterEncoder.encodeReservedCharacters(testExecutionResult.getTestStepInput());
		testStepExpectedOutput=SpecialCharacterEncoder.encodeReservedCharacters(testExecutionResult.getTestStepExpectedOutput());
		testStepObservedOutput=SpecialCharacterEncoder.encodeReservedCharacters(testExecutionResult.getTestStepObservedOutput());		
		testCase=SpecialCharacterEncoder.encodeReservedCharacters(testExecutionResult.getTestCase());
		testCaseDescription=SpecialCharacterEncoder.encodeReservedCharacters(testExecutionResult.getTestCaseDescription());
		
		screenShotPath=SpecialCharacterEncoder.encodeReservedCharacters(testExecutionResult.getScreenShotPath());
		screenShotLabel=SpecialCharacterEncoder.encodeReservedCharacters(testExecutionResult.getScreenShotLabel());
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		if (testExecutionResult.getStartTime() != null)
			startTime = sdf.format(testExecutionResult.getStartTime());
		if (testExecutionResult.getEndTime() != null)
			endTime = sdf.format(testExecutionResult.getEndTime());
		executionRemarks = SpecialCharacterEncoder.encodeReservedCharacters(testExecutionResult.getExecutionRemarks());
		failureReason = SpecialCharacterEncoder.encodeReservedCharacters(testExecutionResult.getFailureReason());
	}

	public JsonTestExecutionResult(TestStepExecutionResultDTO tStepExeDTOObj) {
		if(tStepExeDTOObj.getTestcaseId() != null){
			this.testCaseId =tStepExeDTOObj.getTestcaseId();	
		}else{
			this.testCaseId = 0;
		}
		if(tStepExeDTOObj.getTestStepId() != null){
			this.testStepId =tStepExeDTOObj.getTestStepId();	
		}else{
			this.testStepId = 0;
		}
		if(tStepExeDTOObj.getTestStepName() != null){
			this.testStepName =tStepExeDTOObj.getTestStepName();	
		}else{
			this.testStepName = "";
		}
		if(tStepExeDTOObj.getTestStepCode() != null){
			this.testStepCode =tStepExeDTOObj.getTestStepCode();	
		}else{
			this.testStepCode = "";
		}
		if(tStepExeDTOObj.getTestStepDescription() != null){
			this.testStepDescription =tStepExeDTOObj.getTestStepDescription();	
		}else{
			this.testStepDescription = "";
		}
		if(tStepExeDTOObj.getTestStepExpectedOutput() != null){
			this.testStepExpectedOutput =tStepExeDTOObj.getTestStepExpectedOutput();	
		}else{
			this.testStepExpectedOutput = "";
		}
		if(tStepExeDTOObj.getObservedOutput() != null){
			this.testStepObservedOutput =tStepExeDTOObj.getObservedOutput();	
		}else{
			this.testStepObservedOutput = "";
		}
		if(tStepExeDTOObj.getTeststepexecutionresultId() != null){
			this.teststepexecutionresultId = tStepExeDTOObj.getTeststepexecutionresultId();
		}else{
			this.teststepexecutionresultId = 0;
		}
		if(tStepExeDTOObj.getResult() != null){
			this.testResultStatus =tStepExeDTOObj.getResult();	
		}else{
			this.testResultStatus = "";
		}
		if(tStepExeDTOObj.getTestStepStarttime() != null){
			String dt= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(tStepExeDTOObj.getTestStepStarttime());
			if(dt != null)
			this.startTime =dt;	
		}

		if(tStepExeDTOObj.getTestStepEndtime() != null){
			String dt= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(tStepExeDTOObj.getTestStepEndtime());
			if(dt != null)
			this.endTime =dt;	
		}
		
		if(tStepExeDTOObj.getComments() != null){
			this.executionRemarks =tStepExeDTOObj.getComments();	
		}else{
			this.executionRemarks = "";
		}	
		if(tStepExeDTOObj.getTestStepInput() != null){
			this.testStepInput =tStepExeDTOObj.getTestStepInput();	
		}else{
			this.testStepInput = "";
		}
		 
	}
	public Integer getTestExecutionResultId() {
		return testExecutionResultId;
	}
	public void setTestExecutionResultId(Integer testExecutionResultId) {
		this.testExecutionResultId = testExecutionResultId;
	}
	public String getTestResultStatus() {
		return testResultStatus;
	}
	public void setTestResultStatus(String testResultStatus) {
		this.testResultStatus = testResultStatus;
	}
	public Integer getTestRunListId() {
		return testRunListId;
	}
	public void setTestRunListId(Integer testRunListId) {
		this.testRunListId = testRunListId;
	}
	
	public Integer getTestSuiteId() {
		return testSuiteId;
	}
	public void setTestSuiteId(Integer testSuiteId) {
		this.testSuiteId = testSuiteId;
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
	public String getTestStep() {
		return testStep;
	}
	public void setTestStep(String testStep) {
		this.testStep = testStep;
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
	public String getTestStepObservedOutput() {
		return testStepObservedOutput;
	}
	public void setTestStepObservedOutput(String testStepObservedOutput) {
		this.testStepObservedOutput = testStepObservedOutput;
	}
	public Integer getTestCaseId() {
		return testCaseId;
	}
	public void setTestCaseId(Integer testCaseId) {
		this.testCaseId = testCaseId;
	}
	public String getTestCase() {
		return testCase;
	}
	public void setTestCase(String testCase) {
		this.testCase = testCase;
	}
	public String getTestCaseDescription() {
		return testCaseDescription;
	}
	public void setTestCaseDescription(String testCaseDescription) {
		this.testCaseDescription = testCaseDescription;
	}
	public String getScreenShotPath() {
		return screenShotPath;
	}
	public void setScreenShotPath(String screenShotPath) {
		this.screenShotPath = screenShotPath;
	}
	public String getScreenShotLabel() {
		return screenShotLabel;
	}
	public void setScreenShotLabel(String screenShotLabel) {
		this.screenShotLabel = screenShotLabel;
	}
	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public String getExecutionRemarks() {
		return executionRemarks;
	}

	public void setExecutionRemarks(String executionRemarks) {
		this.executionRemarks = executionRemarks;
	}

	public String getFailureReason() {
		return failureReason;
	}

	public void setFailureReason(String failureReason) {
		this.failureReason = failureReason;
	}
	
	public String getTestCaseScriptQualifiedName() {
		return this.testCaseScriptQualifiedName;
	}

	public void setTestCaseScriptQualifiedName(String testCaseScriptQualifiedName) {
		this.testCaseScriptQualifiedName = testCaseScriptQualifiedName;
	}

	public String getTestCaseScriptFileName() {
		return this.testCaseScriptFileName;
	}

	public void setTestCaseScriptFileName(String testCaseScriptFileName) {
		this.testCaseScriptFileName = testCaseScriptFileName;
	}

	public String getTestCasePriority() {
		return this.testCasePriority;
	}

	public void setTestCasePriority(String testCasePriority) {
		this.testCasePriority = testCasePriority;
	}
	
	@JsonIgnore
	public TestExecutionResult getTestExecutionResult(){
		TestExecutionResult testExecutionResult = new TestExecutionResult();
		testExecutionResult.setTestExecutionResultId(testExecutionResultId);
		
		TestRunList testRunList = new TestRunList();
		testRunList.setTestRunListId(testRunListId);
		testExecutionResult.setTestRunList(testRunList);
		
		TestSuiteList testSuiteList= new TestSuiteList();
		testSuiteList.setTestSuiteId(testSuiteId);
		testExecutionResult.setTestSuiteList(testSuiteList);
		
		testExecutionResult.setTestStep(testStep);
		testExecutionResult.setTestStepDescription(testStepDescription);
		testExecutionResult.setTestStepInput(testStepInput);
		testExecutionResult.setTestStepExpectedOutput(testStepExpectedOutput);
		testExecutionResult.setTestStepObservedOutput(testStepObservedOutput);
		testExecutionResult.setTestCase(testCase);
		testExecutionResult.setTestCaseDescription(testCaseDescription);
	
		testExecutionResult.setScreenShotPath(screenShotPath);
		testExecutionResult.setScreenShotLabel(screenShotLabel);
		
		TestResultStatusMaster testResultStatusMaster = new TestResultStatusMaster();
		testResultStatusMaster.setTestResultStatus(testResultStatus);
		testExecutionResult.setTestResultStatusMaster(testResultStatusMaster);
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		if (startTime != null)
			try {
				testExecutionResult.setStartTime(sdf.parse(startTime));
			} catch (ParseException e) {
				e.printStackTrace();
			}
		if (endTime != null)
			try {
				testExecutionResult.setEndTime(sdf.parse(endTime));
			} catch (ParseException e) {
				e.printStackTrace();
			}
		testExecutionResult.setExecutionRemarks(executionRemarks);
		testExecutionResult.setFailureReason(failureReason);
		return testExecutionResult;
	}

	@JsonIgnore
	public TestExecutionResult getDecodedTestExecutionResult(){
		TestExecutionResult testExecutionResult = new TestExecutionResult();
		testExecutionResult.setTestExecutionResultId(testExecutionResultId);
		
		TestRunList testRunList = new TestRunList();
		testRunList.setTestRunListId(testRunListId);
		testExecutionResult.setTestRunList(testRunList);
		
		TestSuiteList testSuiteList= new TestSuiteList();
		testSuiteList.setTestSuiteId(testSuiteId);
		testExecutionResult.setTestSuiteList(testSuiteList);
		
		testExecutionResult.setTestStep(SpecialCharacterEncoder.decodeReservedCharacters(testStep));
		testExecutionResult.setTestStepDescription(SpecialCharacterEncoder.decodeReservedCharacters(testStepDescription));
		testExecutionResult.setTestStepInput(SpecialCharacterEncoder.decodeReservedCharacters(testStepInput));
		testExecutionResult.setTestStepExpectedOutput(SpecialCharacterEncoder.decodeReservedCharacters(testStepExpectedOutput));
		testExecutionResult.setTestStepObservedOutput(SpecialCharacterEncoder.decodeReservedCharacters(testStepObservedOutput));
		testExecutionResult.setTestCase(SpecialCharacterEncoder.decodeReservedCharacters(testCase));
		testExecutionResult.setTestCaseDescription(SpecialCharacterEncoder.decodeReservedCharacters(testCaseDescription));
	
		testExecutionResult.setScreenShotPath(SpecialCharacterEncoder.decodeReservedCharacters(screenShotPath));
		testExecutionResult.setScreenShotLabel(SpecialCharacterEncoder.decodeReservedCharacters(screenShotLabel));
		
		TestResultStatusMaster testResultStatusMaster = new TestResultStatusMaster();
		testResultStatusMaster.setTestResultStatus(testResultStatus);
		testExecutionResult.setTestResultStatusMaster(testResultStatusMaster);
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		if (startTime != null)
			try {
				testExecutionResult.setStartTime(sdf.parse(startTime));
			} catch (ParseException e) {
				e.printStackTrace();
			}
		if (endTime != null)
			try {
				testExecutionResult.setEndTime(sdf.parse(endTime));
			} catch (ParseException e) {
				e.printStackTrace();
			}
		testExecutionResult.setExecutionRemarks(SpecialCharacterEncoder.decodeReservedCharacters(executionRemarks));
		testExecutionResult.setFailureReason(SpecialCharacterEncoder.decodeReservedCharacters(failureReason));
		return testExecutionResult;
	}

	//Added by Rajesh. Needed for getting test case, step IDs, Code from Terminal.
	//They will help in identifyng the Testcase and Teststep of the result easily
	//These fields will not be persisted in the database.
	public String getTestCaseCode() {
		return testCaseCode;
	}
	public void setTestCaseCode(String testCaseCode) {
		this.testCaseCode = testCaseCode;
	}
	public String getTestStepCode() {
		return testStepCode;
	}
	public void setTestStepCode(String testStepCode) {
		this.testStepCode = testStepCode;
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
	public Integer getTeststepexecutionresultId() {
		return teststepexecutionresultId;
	}
	public void setTeststepexecutionresultId(Integer teststepexecutionresultId) {
		this.teststepexecutionresultId = teststepexecutionresultId;
	}
}
