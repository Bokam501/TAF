package com.hcl.atf.taf.model.json;


import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.hcl.atf.taf.model.DevicePlatformMaster;
import com.hcl.atf.taf.model.DevicePlatformVersionListMaster;
import com.hcl.atf.taf.model.ExecutionTypeMaster;
import com.hcl.atf.taf.model.ProductMaster;
import com.hcl.atf.taf.model.ProductVersionListMaster;
import com.hcl.atf.taf.model.SCMSystem;
import com.hcl.atf.taf.model.ScriptTypeMaster;
import com.hcl.atf.taf.model.TestCaseList;
import com.hcl.atf.taf.model.TestSuiteList;

public class JsonTestSuiteList implements java.io.Serializable {

	@JsonProperty
	private Integer testSuiteId;		
	@JsonProperty
	private Integer productId;
	@JsonProperty
	private String productName;
	
	@JsonProperty
	private Integer productVersionListId;
	@JsonProperty
	private String productVersionListName;
	@JsonProperty 
	private Integer devicePlatformVersionListId;
	@JsonProperty
	private String devicePlatformVersion;
	@JsonProperty
	private String devicePlatformName;
	
	@JsonProperty
	private String testSuiteScriptFileLocation;
	
	@JsonProperty
	private String testSuiteName;
	
	@JsonProperty
	private String testScriptType;
	
	@JsonProperty
	private String testSuiteCode;
	//Begin - Added by Rajesh to show testsuite size information
	@JsonProperty
	private Integer testCaseCount;
	@JsonProperty
	private Integer testStepsCount;
	//End - Added by Rajesh to show testsuite size information
	@JsonProperty
	private String testScriptSource;
	//Added to show TestSuite & Testcase Many to Many mapping
	@JsonProperty
	private Integer testCaseId;
	@JsonProperty
	private String testCaseName;
	@JsonProperty
	private Integer testCaseIdMapping;
	@JsonProperty
	private Integer executionTypeId;
	@JsonProperty
	private Integer isSelected;
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
	private String scriptPlatformLocation;
	@JsonProperty	
	private Integer scmSystemId;
	@JsonProperty
	private String scmSystemName;
	
	public JsonTestSuiteList() {		
	}

	public JsonTestSuiteList(TestSuiteList testSuiteList) {
	
		this.testSuiteId = testSuiteList.getTestSuiteId();
		if(testSuiteList.getProductMaster()!=null){
			this.productId = testSuiteList.getProductMaster().getProductId();
			this.productName = testSuiteList.getProductMaster().getProductName();
		}
		
		if(testSuiteList.getProductVersionListMaster()!=null){
		this.productVersionListId = testSuiteList.getProductVersionListMaster().getProductVersionListId();
		this.productVersionListName = testSuiteList.getProductVersionListMaster().getProductVersionName();		
		}
		
		
		this.testSuiteScriptFileLocation = testSuiteList.getTestSuiteScriptFileLocation();
		this.testSuiteName = testSuiteList.getTestSuiteName();
		this.scriptPlatformLocation = testSuiteList.getScriptPlatformLocation();
		
		if(testSuiteList.getScriptTypeMaster()!=null)
			this.testScriptType = testSuiteList.getScriptTypeMaster().getScriptType();
		
		this.testSuiteCode = testSuiteList.getTestSuiteCode();
		
		if (testSuiteList.getTestCaseLists() != null) {
			Set<TestCaseList> testCases = testSuiteList.getTestCaseLists();
			testCaseCount = testCases.size();
			testStepsCount = 0;
			for (TestCaseList testCase : testCases) {
				if (testCase.getTestCaseStepsLists() != null)
				testStepsCount = testStepsCount + testCase.getTestCaseStepsLists().size();
			}
		}
		
		if(testSuiteList.getTestCaseLists()!=null) {
			if(testSuiteList.getTestCaseLists().iterator().hasNext()){
				this.testCaseId = testSuiteList.getTestCaseLists().iterator().next().getTestCaseId();	
				this.testCaseName= testSuiteList.getTestCaseLists().iterator().next().getTestCaseName();
				this.testCaseIdMapping = testSuiteList.getTestCaseLists().iterator().next().getTestCaseId();
			}
		}
		else{
			this.testCaseId=0;
			this.testCaseName=null;
			this.testCaseIdMapping = 0;
		}
		this.testScriptSource = testSuiteList.getTestScriptSource();
		if(testSuiteList.getExecutionTypeMaster() != null){
			this.executionTypeId = testSuiteList.getExecutionTypeMaster().getExecutionTypeId();
		}
		
		if(testSuiteList.getScmSystem() != null){
			this.scmSystemId = testSuiteList.getScmSystem().getId();
			this.scmSystemName = testSuiteList.getScmSystem().getTitle();
		}
	}

	public Integer getTestSuiteId() {
		return testSuiteId;
	}

	public void setTestSuiteId(Integer testSuiteId) {
		this.testSuiteId = testSuiteId;
	}

	

	public Integer getProductId() {
		return productId;
	}

	public void setProductId(Integer productId) {
		this.productId = productId;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	

	public Integer getProductVersionListId() {
		return productVersionListId;
	}

	public void setProductVersionListId(Integer productVersionListId) {
		this.productVersionListId = productVersionListId;
	}

	public String getProductVersionListName() {
		return productVersionListName;
	}

	public void setProductVersionListName(String productVersionListName) {
		this.productVersionListName = productVersionListName;
	}

	public Integer getDevicePlatformVersionListId() {
		return devicePlatformVersionListId;
	}

	public void setDevicePlatformVersionListId(Integer devicePlatformVersionListId) {
		this.devicePlatformVersionListId = devicePlatformVersionListId;
	}

	public String getDevicePlatformVersion() {
		return devicePlatformVersion;
	}

	public void setDevicePlatformVersion(String devicePlatformVersion) {
		this.devicePlatformVersion = devicePlatformVersion;
	}

	

	public String getDevicePlatformName() {
		return devicePlatformName;
	}

	public void setDevicePlatformName(String devicePlatformName) {
		this.devicePlatformName = devicePlatformName;
	}

	public String getTestSuiteScriptFileLocation() {
		return testSuiteScriptFileLocation;
	}

	public void setTestSuiteScriptFileLocation(String testSuiteScriptFileLocation) {
		this.testSuiteScriptFileLocation = testSuiteScriptFileLocation;
	}

	public String getTestSuiteName() {
		return testSuiteName;
	}

	public void setTestSuiteName(String testSuiteName) {
		this.testSuiteName = testSuiteName;
	}
	
	public String getTestScriptType() {
		return testScriptType;
	}

	public void setTestScriptType(String testScriptType) {
		this.testScriptType = testScriptType;
	}
	
	//Added for test management tool integration changes
	public String getTestSuiteCode() {
		return testSuiteCode;
	}


	public void setTestSuiteCode(String testSuiteCode) {
		this.testSuiteCode = testSuiteCode;
	}
	
	public Integer getTestCaseCount() {
		return testCaseCount;
	}

	public void setTestCaseCount(Integer testCaseCount) {
		this.testCaseCount = testCaseCount;
	}

	public Integer getTestStepsCount() {
		return testStepsCount;
	}

	public void setTestStepsCount(Integer testStepsCount) {
		this.testStepsCount = testStepsCount;
	}
	
	public String getTestScriptSource() {
		return testScriptSource;
	}

	public void setTestScriptSource(String testScriptSource) {
		this.testScriptSource = testScriptSource;
	}


	public Integer getTestCaseId() {
		return testCaseId;
	}

	public void setTestCaseId(Integer testCaseId) {
		this.testCaseId = testCaseId;
	}

	public String getTestCaseName() {
		return testCaseName;
	}

	public void setTestCaseName(String testCaseName) {
		this.testCaseName = testCaseName;
	}

	public Integer getTestCaseIdMapping() {
		return testCaseIdMapping;
	}

	public void setTestCaseIdMapping(Integer testCaseIdMapping) {
		this.testCaseIdMapping = testCaseIdMapping;
	}

	public Integer getExecutionTypeId() {
		return executionTypeId;
	}

	public void setExecutionTypeId(Integer executionTypeId) {
		this.executionTypeId = executionTypeId;
	}
	

	@JsonIgnore
	public TestSuiteList getTestSuiteList(){
		TestSuiteList testSuiteList = new TestSuiteList();
		testSuiteList.setTestSuiteId(testSuiteId);
		
		ProductMaster productMaster= new ProductMaster();
		productMaster.setProductId(productId);
		ProductVersionListMaster productVersionListMaster=new ProductVersionListMaster();
		productVersionListMaster.setProductVersionListId(productVersionListId);
		productVersionListMaster.setProductMaster(productMaster);
		
		DevicePlatformMaster devicePlatformMaster = new DevicePlatformMaster();
		devicePlatformMaster.setDevicePlatformName(devicePlatformName);
		DevicePlatformVersionListMaster devicePlatformVersionListMaster= new DevicePlatformVersionListMaster();
		devicePlatformVersionListMaster.setDevicePlatformVersionListId(devicePlatformVersionListId);
		devicePlatformVersionListMaster.setDevicePlatformMaster(devicePlatformMaster);
		
		testSuiteList.setProductVersionListMaster(productVersionListMaster);
		testSuiteList.setProductMaster(productMaster);
		
		testSuiteList.setTestSuiteName(testSuiteName);
		testSuiteList.setTestSuiteScriptFileLocation(testSuiteScriptFileLocation);
		
		ScriptTypeMaster scriptTypeMaster = new ScriptTypeMaster();
		scriptTypeMaster.setScriptType(testScriptType);
		testSuiteList.setScriptTypeMaster(scriptTypeMaster);
		
		testSuiteList.setTestSuiteCode(testSuiteCode);
		testSuiteList.setTestScriptSource(testScriptSource);
		testSuiteList.setScriptPlatformLocation(scriptPlatformLocation);
		if((testCaseId !=null) && (testCaseId !=0)){ 
			TestCaseList testcaseList = new TestCaseList();
			testcaseList.setTestCaseId(testCaseId);
			  Set<TestCaseList> tcSet = new HashSet<TestCaseList>();
			  tcSet.add(testcaseList);
			  testSuiteList.setTestCaseLists(tcSet);
		} 
		if(executionTypeId != null){
			ExecutionTypeMaster etm = new ExecutionTypeMaster();
			etm.setExecutionTypeId(executionTypeId);
			testSuiteList.setExecutionTypeMaster(etm);
		}
		
		if(scmSystemId != null){
			SCMSystem scmSystem = new SCMSystem();
			scmSystem.setId(scmSystemId);
			testSuiteList.setScmSystem(scmSystem);
		}
		
		return testSuiteList;
	}

	public Integer getIsSelected() {
		return isSelected;
	}

	public void setIsSelected(Integer isSelected) {
		this.isSelected = isSelected;
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

	public String getScriptPlatformLocation() {
		return scriptPlatformLocation;
	}

	public void setScriptPlatformLocation(String scriptPlatformLocation) {
		this.scriptPlatformLocation = scriptPlatformLocation;
	}
	
	public Integer getScmSystemId() {
		return scmSystemId;
	}

	public void setScmSystemId(Integer scmSystemId) {
		this.scmSystemId = scmSystemId;
	}

	public String getScmSystemName() {
		return scmSystemName;
	}

	public void setScmSystemName(String scmSystemName) {
		this.scmSystemName = scmSystemName;
	}
}
