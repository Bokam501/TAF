package com.hcl.atf.taf.model.json;


import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.hcl.atf.taf.controller.utilities.DateUtility;
import com.hcl.atf.taf.controller.utilities.SpecialCharacterEncoder;
import com.hcl.atf.taf.model.EnvironmentCombination;
import com.hcl.atf.taf.model.GenericDevices;
import com.hcl.atf.taf.model.HostList;
import com.hcl.atf.taf.model.ProductBuild;
import com.hcl.atf.taf.model.ProductMaster;
import com.hcl.atf.taf.model.ProductVersionListMaster;
import com.hcl.atf.taf.model.RunConfiguration;
import com.hcl.atf.taf.model.TestCaseExecutionResult;
import com.hcl.atf.taf.model.TestCaseList;
import com.hcl.atf.taf.model.TestCasePriority;
import com.hcl.atf.taf.model.TestCaseStepsList;
import com.hcl.atf.taf.model.TestStepExecutionResult;
import com.hcl.atf.taf.model.TestSuiteList;
import com.hcl.atf.taf.model.WorkPackage;
import com.hcl.atf.taf.model.WorkPackageTestCaseExecutionPlan;
import com.hcl.atf.taf.model.WorkpackageRunConfiguration;


public class JsonWorkPackageTestCaseExecutionPlanForTerminal implements java.io.Serializable{
	
	@JsonProperty	
	private int id;
	
	@JsonProperty	
	private int testcaseId;
	
	@JsonProperty	
	private String testcaseName;
	
	@JsonProperty	
	private String testcaseDescription;
	
	@JsonProperty	
	private String testcaseCode;
	
	
	@JsonProperty	
	private int environmentId;
	
	@JsonProperty	
	private String environmentName;
	
	@JsonProperty
	private int workPackageId;
	
	@JsonProperty	
	private String workPackageName;
	
	
	@JsonProperty
	private int testLeadId;
	
	@JsonProperty	
	private String testLeadName;
	
	@JsonProperty
	private int testerId;
	
	@JsonProperty	
	private String testerName;
	
	@JsonProperty	
	private String plannedExecutionDate;
	
	@JsonProperty	
	private String actualExecutionDate;
	
	@JsonProperty	
	private String createdDate;
	
	@JsonProperty	
	private String modifiedDate;
	
	@JsonProperty	
	private Integer isExecuted;
	@JsonProperty	
	private Integer executionStatus;
	@JsonProperty
	private String runCode;
	
	@JsonProperty
	private int plannedShiftId;
	
	@JsonProperty
	private String plannedShiftName;
	
	@JsonProperty
	private int actualShiftId;
	
	@JsonProperty
	private String actualShiftName;
	@JsonProperty	
	private int localeId;
	@JsonProperty	
	private String localeName;
	@JsonProperty
	private int decouplingCategoryId;
	
	@JsonProperty	
	private String decouplingCategoryName;
	
	@JsonProperty
	private Integer executionPriorityId;
	
	@JsonProperty	
	private String executionPriorityName;
	@JsonProperty	
	private String executionPriorityDisplayName;
	@JsonProperty	
	private String executionName;
	@JsonProperty	
	private String workpackagePlannedStartDate;
	@JsonProperty	
	private String workpackagePlannedEndDate;
	
	@JsonProperty
	private int productId;
	@JsonProperty	
	private String productName;
	@JsonProperty
	private int productVersionId;
	@JsonProperty	
	private String productVersionName;
	@JsonProperty	
	private String environmentCombinationName;
	@JsonProperty	
	private Integer environmentCombinationId;
	
	@JsonProperty	
	private String deviceName;
	@JsonProperty	
	private Integer deviceId;
	
	@JsonProperty	
	private String runConfigurationName;
	@JsonProperty	
	private Integer runConfigurationId;

	@JsonProperty	
	private Integer testsuiteId;
	
	@JsonProperty	
	private String testsuiteName;
	
	@JsonProperty	
	private String testcaseType;

	@JsonProperty	
	private String testcasePriority;
	@JsonProperty	
	private Integer hostId;

	@JsonProperty	
	private String testCaseScriptQualifiedName;
	@JsonProperty	
	private String testCaseScriptFileName;
	
	//Test case result
	
	@JsonProperty	
	private Integer testCaseExecutionResultId;
	@JsonProperty	
	private String result;
	@JsonProperty	
	private Integer defectsCount;
	@JsonProperty	
	private String defectIds;
	@JsonProperty	
	private String comments;
	@JsonProperty	
	private Integer isApproved;
	@JsonProperty	
	private Integer isReviewed;
	@JsonProperty	
	private String observedOutput;
	@JsonProperty	
	private String evidencePath;
	@JsonProperty	
	private String evidenceLabel;
	@JsonProperty	
	private Long executionTime;

	// Teststep result
	@JsonProperty
	private Integer testStepExecutionId;
	@JsonProperty	
	private String testStepResult;
	@JsonProperty	
	private String testStepcomments;
	@JsonProperty	
	private Integer testStepIsApproved;
	@JsonProperty	
	private Integer testStepIsReviewed;
	@JsonProperty	
	private String testStepObservedOutput;
	@JsonProperty	
	private Integer testStepsId;
	@JsonProperty	
	private String testStepsName;
	@JsonProperty	
	private String description;
	@JsonProperty	
	private String input;
	@JsonProperty	
	private String expectedOutput;
	@JsonProperty	
	private String code;
	
	@JsonProperty	
	private String testStepEvidencePath;
	@JsonProperty	
	private String testStepEvidenceLabel;
	
	@JsonProperty	
	private Integer testRunJobId;

	@JsonProperty	
	private Integer productBuildId;


	@JsonProperty	
	private String udid;

	

	@JsonProperty	
	private String hostName;
	
	@JsonProperty	
	private String sourceType;
	@JsonProperty
	private int featureId;
	
	@JsonProperty	
	private String featureName;
	@JsonProperty	
	private String sourceTable;
	@JsonProperty	
	private String sourceData;

	public JsonWorkPackageTestCaseExecutionPlanForTerminal() {
	}	

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getTestcaseId() {
		return testcaseId;
	}

	public void setTestcaseId(int testcaseId) {
		this.testcaseId = testcaseId;
	}

	public String getTestcaseName() {
		return testcaseName;
	}

	public void setTestcaseName(String testcaseName) {
		this.testcaseName = testcaseName;
	}

	public String getTestcaseDescription() {
		return testcaseDescription;
	}

	public void setTestcaseDescription(String testcaseDescription) {
		this.testcaseDescription = testcaseDescription;
	}

	public String getTestcaseCode() {
		return testcaseCode;
	}

	public void setTestcaseCode(String testcaseCode) {
		this.testcaseCode = testcaseCode;
	}

	public int getEnvironmentId() {
		return environmentId;
	}

	public void setEnvironmentId(int environmentId) {
		this.environmentId = environmentId;
	}

	public String getEnvironmentName() {
		return environmentName;
	}

	public void setEnvironmentName(String environmentName) {
		this.environmentName = environmentName;
	}

	public int getWorkPackageId() {
		return workPackageId;
	}

	public void setWorkPackageId(int workPackageId) {
		this.workPackageId = workPackageId;
	}

	public String getWorkPackageName() {
		return workPackageName;
	}

	public void setWorkPackageName(String workPackageName) {
		this.workPackageName = workPackageName;
	}

	public int getTestLeadId() {
		return testLeadId;
	}

	public void setTestLeadId(int testLeadId) {
		this.testLeadId = testLeadId;
	}

	public String getTestLeadName() {
		return testLeadName;
	}

	public void setTestLeadName(String testLeadName) {
		this.testLeadName = testLeadName;
	}

	public int getTesterId() {
		return testerId;
	}

	public void setTesterId(int testerId) {
		this.testerId = testerId;
	}

	public String getTesterName() {
		return testerName;
	}

	public void setTesterName(String testerName) {
		this.testerName = testerName;
	}

	public String getPlannedExecutionDate() {
		return plannedExecutionDate;
	}

	public void setPlannedExecutionDate(String plannedExecutionDate) {
		this.plannedExecutionDate = plannedExecutionDate;
	}

	public String getActualExecutionDate() {
		return actualExecutionDate;
	}

	public void setActualExecutionDate(String actualExecutionDate) {
		this.actualExecutionDate = actualExecutionDate;
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

	public Integer getIsExecuted() {
		return isExecuted;
	}

	public void setIsExecuted(Integer isExecuted) {
		this.isExecuted = isExecuted;
	}

	public Integer getExecutionStatus() {
		return executionStatus;
	}

	public void setExecutionStatus(Integer executionStatus) {
		this.executionStatus = executionStatus;
	}

	public String getRunCode() {
		return runCode;
	}

	public void setRunCode(String runCode) {
		this.runCode = runCode;
	}

	public int getPlannedShiftId() {
		return plannedShiftId;
	}

	public void setPlannedShiftId(int plannedShiftId) {
		this.plannedShiftId = plannedShiftId;
	}

	public String getPlannedShiftName() {
		return plannedShiftName;
	}

	public void setPlannedShiftName(String plannedShiftName) {
		this.plannedShiftName = plannedShiftName;
	}

	public int getActualShiftId() {
		return actualShiftId;
	}

	public void setActualShiftId(int actualShiftId) {
		this.actualShiftId = actualShiftId;
	}

	public String getActualShiftName() {
		return actualShiftName;
	}

	public void setActualShiftName(String actualShiftName) {
		this.actualShiftName = actualShiftName;
	}

	public int getLocaleId() {
		return localeId;
	}

	public void setLocaleId(int localeId) {
		this.localeId = localeId;
	}

	public String getLocaleName() {
		return localeName;
	}

	public void setLocaleName(String localeName) {
		this.localeName = localeName;
	}

	public int getDecouplingCategoryId() {
		return decouplingCategoryId;
	}

	public void setDecouplingCategoryId(int decouplingCategoryId) {
		this.decouplingCategoryId = decouplingCategoryId;
	}

	public String getDecouplingCategoryName() {
		return decouplingCategoryName;
	}

	public void setDecouplingCategoryName(String decouplingCategoryName) {
		this.decouplingCategoryName = decouplingCategoryName;
	}

	public Integer getExecutionPriorityId() {
		return executionPriorityId;
	}

	public void setExecutionPriorityId(Integer executionPriorityId) {
		this.executionPriorityId = executionPriorityId;
	}

	public String getExecutionPriorityName() {
		return executionPriorityName;
	}

	public void setExecutionPriorityName(String executionPriorityName) {
		this.executionPriorityName = executionPriorityName;
	}

	public String getExecutionPriorityDisplayName() {
		return executionPriorityDisplayName;
	}

	public void setExecutionPriorityDisplayName(String executionPriorityDisplayName) {
		this.executionPriorityDisplayName = executionPriorityDisplayName;
	}

	public String getExecutionName() {
		return executionName;
	}

	public void setExecutionName(String executionName) {
		this.executionName = executionName;
	}

	public String getWorkpackagePlannedStartDate() {
		return workpackagePlannedStartDate;
	}

	public void setWorkpackagePlannedStartDate(String workpackagePlannedStartDate) {
		this.workpackagePlannedStartDate = workpackagePlannedStartDate;
	}

	public String getWorkpackagePlannedEndDate() {
		return workpackagePlannedEndDate;
	}

	public void setWorkpackagePlannedEndDate(String workpackagePlannedEndDate) {
		this.workpackagePlannedEndDate = workpackagePlannedEndDate;
	}

	public int getProductId() {
		return productId;
	}

	public void setProductId(int productId) {
		this.productId = productId;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public int getProductVersionId() {
		return productVersionId;
	}

	public void setProductVersionId(int productVersionId) {
		this.productVersionId = productVersionId;
	}

	public String getProductVersionName() {
		return productVersionName;
	}

	public void setProductVersionName(String productVersionName) {
		this.productVersionName = productVersionName;
	}

	public String getEnvironmentCombinationName() {
		return environmentCombinationName;
	}

	public void setEnvironmentCombinationName(String environmentCombinationName) {
		this.environmentCombinationName = environmentCombinationName;
	}

	public Integer getEnvironmentCombinationId() {
		return environmentCombinationId;
	}

	public void setEnvironmentCombinationId(Integer environmentCombinationId) {
		this.environmentCombinationId = environmentCombinationId;
	}

	public String getRunConfigurationName() {
		return runConfigurationName;
	}

	public void setRunConfigurationName(String runConfigurationName) {
		this.runConfigurationName = runConfigurationName;
	}

	public Integer getRunConfigurationId() {
		return runConfigurationId;
	}

	public void setRunConfigurationId(Integer runConfigurationId) {
		this.runConfigurationId = runConfigurationId;
	}

	public Integer getTestsuiteId() {
		return testsuiteId;
	}

	public void setTestsuiteId(Integer testsuiteId) {
		this.testsuiteId = testsuiteId;
	}

	public String getTestsuiteName() {
		return testsuiteName;
	}

	public void setTestsuiteName(String testsuiteName) {
		this.testsuiteName = testsuiteName;
	}

	public String getDeviceName() {
		return deviceName;
	}

	public void setDeviceName(String deviceName) {
		this.deviceName = deviceName;
	}

	public Integer getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(Integer deviceId) {
		this.deviceId = deviceId;
	}

	public String getTestcaseType() {
		return testcaseType;
	}

	public void setTestcaseType(String testcaseType) {
		this.testcaseType = testcaseType;
	}

	public String getTestcasePriority() {
		return testcasePriority;
	}

	public void setTestcasePriority(String testcasePriority) {
		this.testcasePriority = testcasePriority;
	}

	public Integer getHostId() {
		return hostId;
	}

	public void setHostId(Integer hostId) {
		this.hostId = hostId;
	}

	public String getTestCaseScriptQualifiedName() {
		return testCaseScriptQualifiedName;
	}

	public void setTestCaseScriptQualifiedName(String testCaseScriptQualifiedName) {
		this.testCaseScriptQualifiedName = testCaseScriptQualifiedName;
	}

	public String getTestCaseScriptFileName() {
		return testCaseScriptFileName;
	}

	public void setTestCaseScriptFileName(String testCaseScriptFileName) {
		this.testCaseScriptFileName = testCaseScriptFileName;
	}

	public Long getExecutionTime() {
		return executionTime;
	}

	public void setExecutionTime(Long executionTime) {
		this.executionTime = executionTime;
	}

	
	public Integer getTestCaseExecutionResultId() {
		return testCaseExecutionResultId;
	}
	public void setTestCaseExecutionResultId(Integer testCaseExecutionResultId) {
		this.testCaseExecutionResultId = testCaseExecutionResultId;
	}
	public String getResult() {
		return result;
	}
	public void setResult(String result) {
		this.result = result;
	}
	public Integer getDefectsCount() {
		return defectsCount;
	}
	public void setDefectsCount(Integer defectsCount) {
		this.defectsCount = defectsCount;
	}
	public String getDefectIds() {
		return defectIds;
	}
	public void setDefectIds(String defectIds) {
		this.defectIds = defectIds;
	}
	public String getComments() {
		return comments;
	}
	public void setComments(String comments) {
		this.comments = comments;
	}
	public Integer getIsApproved() {
		return isApproved;
	}
	public void setIsApproved(Integer isApproved) {
		this.isApproved = isApproved;
	}
	public Integer getIsReviewed() {
		return isReviewed;
	}
	public void setIsReviewed(Integer isReviewed) {
		this.isReviewed = isReviewed;
	}

	public String getObservedOutput() {
		return observedOutput;
	}

	public void setObservedOutput(String observedOutput) {
		this.observedOutput = observedOutput;
	}

	public String getEvidencePath() {
		return evidencePath;
	}

	public void setEvidencePath(String evidencePath) {
		this.evidencePath = evidencePath;
	}

	public String getEvidenceLabel() {
		return evidenceLabel;
	}

	public void setEvidenceLabel(String evidenceLabel) {
		this.evidenceLabel = evidenceLabel;
	}

	public String getTestStepResult() {
		return testStepResult;
	}

	public void setTestStepResult(String testStepResult) {
		this.testStepResult = testStepResult;
	}

	public String getTestStepcomments() {
		return testStepcomments;
	}

	public void setTestStepcomments(String testStepcomments) {
		this.testStepcomments = testStepcomments;
	}

	public Integer getTestStepIsApproved() {
		return testStepIsApproved;
	}

	public void setTestStepIsApproved(Integer testStepIsApproved) {
		this.testStepIsApproved = testStepIsApproved;
	}

	public Integer getTestStepIsReviewed() {
		return testStepIsReviewed;
	}

	public void setTestStepIsReviewed(Integer testStepIsReviewed) {
		this.testStepIsReviewed = testStepIsReviewed;
	}

	public String getTestStepObservedOutput() {
		return testStepObservedOutput;
	}

	public void setTestStepObservedOutput(String testStepObservedOutput) {
		this.testStepObservedOutput = testStepObservedOutput;
	}

	public Integer getTestStepsId() {
		return testStepsId;
	}

	public void setTestStepsId(Integer testStepsId) {
		this.testStepsId = testStepsId;
	}

	public String getTestStepsName() {
		return testStepsName;
	}

	public void setTestStepsName(String testStepsName) {
		this.testStepsName = testStepsName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getInput() {
		return input;
	}

	public void setInput(String input) {
		this.input = input;
	}

	public String getExpectedOutput() {
		return expectedOutput;
	}

	public void setExpectedOutput(String expectedOutput) {
		this.expectedOutput = expectedOutput;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getTestStepEvidencePath() {
		return testStepEvidencePath;
	}

	public void setTestStepEvidencePath(String testStepEvidencePath) {
		this.testStepEvidencePath = testStepEvidencePath;
	}

	public String getTestStepEvidenceLabel() {
		return testStepEvidenceLabel;
	}

	public void setTestStepEvidenceLabel(String testStepEvidenceLabel) {
		this.testStepEvidenceLabel = testStepEvidenceLabel;
	}

	public Integer getTestRunJobId() {
		return testRunJobId;
	}

	public void setTestRunJobId(Integer testRunJobId) {
		this.testRunJobId = testRunJobId;
	}
	
	public Integer getTestStepExecutionId() {
		return testStepExecutionId;
	}


	
	@JsonIgnore
	public WorkPackageTestCaseExecutionPlan getDecodedTestExecutionResult(){
	
		
		WorkPackageTestCaseExecutionPlan workPackageTestCaseExecutionPlan = new WorkPackageTestCaseExecutionPlan();
		workPackageTestCaseExecutionPlan.setId(this.getId());
		
		TestCaseList testCase = new TestCaseList();
		testCase.setTestCaseId(Integer.parseInt(SpecialCharacterEncoder.decodeReservedCharacters(testcaseId+"")));
		testCase.setTestCaseName(SpecialCharacterEncoder.decodeReservedCharacters(testcaseName));
		testCase.setTestCaseDescription(SpecialCharacterEncoder.decodeReservedCharacters(testcaseDescription));
		testCase.setTestCaseCode(SpecialCharacterEncoder.decodeReservedCharacters(testcaseCode));
		testCase.setTestCaseType(SpecialCharacterEncoder.decodeReservedCharacters(testcaseType));
		testCase.setTestCaseScriptFileName(SpecialCharacterEncoder.decodeReservedCharacters(testCaseScriptFileName));
		testCase.setTestCaseScriptQualifiedName(SpecialCharacterEncoder.decodeReservedCharacters(testCaseScriptQualifiedName));
		TestCasePriority testCasePriority= new TestCasePriority();
		testCasePriority.setPriorityName(testcasePriority);
		testCase.setTestCasePriority(testCasePriority);
		
		workPackageTestCaseExecutionPlan.setTestCase(testCase);
		
		ProductMaster productMaster= new ProductMaster();
		productMaster.setProductName(SpecialCharacterEncoder.decodeReservedCharacters(productName));
		productMaster.setProductId(Integer.parseInt(SpecialCharacterEncoder.decodeReservedCharacters(productId+"")));
		
		WorkPackage workPackage = new WorkPackage();
		workPackage.setWorkPackageId(Integer.parseInt(SpecialCharacterEncoder.decodeReservedCharacters(workPackageId+"")));
		ProductVersionListMaster productVersionListMaster = new ProductVersionListMaster();
		productVersionListMaster.setProductVersionListId(Integer.parseInt(SpecialCharacterEncoder.decodeReservedCharacters(productVersionId+"")));
		
		ProductBuild productBuild = new ProductBuild();
		productBuild.setProductBuildId(Integer.parseInt(SpecialCharacterEncoder.decodeReservedCharacters(productBuildId+"")));
		productBuild.setProductVersion(productVersionListMaster);
		
		workPackage.setProductBuild(productBuild);
		
		workPackageTestCaseExecutionPlan.setWorkPackage(workPackage);
		
		WorkpackageRunConfiguration  workpackageRunConfiguration =new WorkpackageRunConfiguration();
		RunConfiguration runconfiguration = new RunConfiguration();

		EnvironmentCombination environmentCombination = new EnvironmentCombination();
		if(environmentCombinationId!=null){
		environmentCombination.setEnvironment_combination_id(Integer.parseInt(SpecialCharacterEncoder.decodeReservedCharacters(environmentCombinationId+"")));
		environmentCombination.setEnvironmentCombinationName(SpecialCharacterEncoder.decodeReservedCharacters(environmentCombinationName));
		runconfiguration.setEnvironmentcombination(environmentCombination);
		}
		GenericDevices genericDevices = new GenericDevices();
		if(deviceId!=null){
			genericDevices.setGenericsDevicesId(Integer.parseInt(SpecialCharacterEncoder.decodeReservedCharacters(deviceId+"")));
			genericDevices.setName(SpecialCharacterEncoder.decodeReservedCharacters(deviceName));
			genericDevices.setUDID(SpecialCharacterEncoder.decodeReservedCharacters(udid));
			runconfiguration.setGenericDevice(genericDevices);
		}
		HostList hostList = new HostList();
		hostList.setHostId(Integer.parseInt(SpecialCharacterEncoder.decodeReservedCharacters(hostId+"")));
		runconfiguration.setHostList(hostList);
		
		if(runConfigurationId!=null){
			runconfiguration.setRunconfigId(Integer.parseInt(SpecialCharacterEncoder.decodeReservedCharacters(runConfigurationId+"")));
			runconfiguration.setRunconfigName(SpecialCharacterEncoder.decodeReservedCharacters(runConfigurationName));
		}
		workpackageRunConfiguration.setRunconfiguration(runconfiguration);
		workpackageRunConfiguration.setType("testsuite");
		
		
		workPackageTestCaseExecutionPlan.setPlannedExecutionDate(DateUtility.getCurrentTime());
		
		workPackageTestCaseExecutionPlan.setActualExecutionDate(DateUtility.getCurrentTime());
		workPackageTestCaseExecutionPlan.setCreatedDate(DateUtility.getCurrentTime());

		workPackageTestCaseExecutionPlan.setModifiedDate(DateUtility.getCurrentTime());

		workPackageTestCaseExecutionPlan.setExecutionStatus(Integer.parseInt(SpecialCharacterEncoder.decodeReservedCharacters(executionStatus+"")));
		workPackageTestCaseExecutionPlan.setIsExecuted(Integer.parseInt(SpecialCharacterEncoder.decodeReservedCharacters(this.isExecuted+"")));
		
		
		TestSuiteList testSuiteList= new TestSuiteList();
		testSuiteList.setTestSuiteId(Integer.parseInt(SpecialCharacterEncoder.decodeReservedCharacters(this.testsuiteId+"")));
		testSuiteList.setTestSuiteName(SpecialCharacterEncoder.decodeReservedCharacters(testsuiteName));
		
		workPackageTestCaseExecutionPlan.setTestSuiteList(testSuiteList);
		
		TestCaseExecutionResult testCaseExecutionResult = new TestCaseExecutionResult();
		testCaseExecutionResult.setComments(SpecialCharacterEncoder.decodeReservedCharacters(comments));
		testCaseExecutionResult.setResult(SpecialCharacterEncoder.decodeReservedCharacters(result));
		testCaseExecutionResult.setObservedOutput(SpecialCharacterEncoder.decodeReservedCharacters(observedOutput));
		testCaseExecutionResult.setExecutionTime(Long.parseLong(SpecialCharacterEncoder.decodeReservedCharacters(executionTime+"")));
		workPackageTestCaseExecutionPlan.setTestCaseExecutionResult(testCaseExecutionResult);

		workPackageTestCaseExecutionPlan.setExecutionStatus(executionStatus);

		workPackageTestCaseExecutionPlan.setIsExecuted(this.isExecuted);
		
		TestCaseStepsList testCaseStepsList= new TestCaseStepsList();
		testCaseStepsList.setTestStepName(SpecialCharacterEncoder.decodeReservedCharacters(testStepsName));
		testCaseStepsList.setTestStepDescription(SpecialCharacterEncoder.decodeReservedCharacters(description));
		testCaseStepsList.setTestStepInput(SpecialCharacterEncoder.decodeReservedCharacters(input));
		testCaseStepsList.setTestStepExpectedOutput(SpecialCharacterEncoder.decodeReservedCharacters(expectedOutput));
		testCaseStepsList.setTestStepCode(SpecialCharacterEncoder.decodeReservedCharacters(code));
		
		TestStepExecutionResult testStepExecutionResult= new TestStepExecutionResult();
		testStepExecutionResult.setObservedOutput(SpecialCharacterEncoder.decodeReservedCharacters(testStepObservedOutput));
		testStepExecutionResult.setComments(SpecialCharacterEncoder.decodeReservedCharacters(testStepcomments));
		testStepExecutionResult.setResult(SpecialCharacterEncoder.decodeReservedCharacters(testStepResult));
		testStepExecutionResult.setTestSteps(testCaseStepsList);
		Set<TestStepExecutionResult> testStepExecutionResultSet= new HashSet<TestStepExecutionResult>();
		testStepExecutionResultSet.add(testStepExecutionResult);
		testCaseExecutionResult.setTestStepExecutionResultSet(testStepExecutionResultSet);
			
		return workPackageTestCaseExecutionPlan;
	}

	public Integer getProductBuildId() {
		return productBuildId;
	}

	public void setProductBuildId(Integer productBuildId) {
		this.productBuildId = productBuildId;
	}

	public void setTestStepExecutionId(Integer testStepExecutionId) {
		this.testStepExecutionId = testStepExecutionId;
	}

	public String getUdid() {
		return udid;
	}

	public void setUdid(String udid) {
		this.udid = udid;
	}

	public String getHostName() {
		return hostName;
	}

	public void setHostName(String hostName) {
		this.hostName = hostName;
	}

	

	public String getSourceType() {
		return sourceType;
	}

	public void setSourceType(String sourceType) {
		this.sourceType = sourceType;
	}

	public int getFeatureId() {
		return featureId;
	}

	public void setFeatureId(int featureId) {
		this.featureId = featureId;
	}

	public String getFeatureName() {
		return featureName;
	}

	public void setFeatureName(String featureName) {
		this.featureName = featureName;
	}

	public String getSourceTable() {
		return sourceTable;
	}

	public void setSourceTable(String sourceTable) {
		this.sourceTable = sourceTable;
	}

	public String getSourceData() {
		return sourceData;
	}

	public void setSourceData(String sourceData) {
		this.sourceData = sourceData;
	}



	
}
