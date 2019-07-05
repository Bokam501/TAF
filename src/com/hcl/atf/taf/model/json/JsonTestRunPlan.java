package com.hcl.atf.taf.model.json;

import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.hcl.atf.taf.controller.utilities.DateUtility;
import com.hcl.atf.taf.model.ExecutionTypeMaster;
import com.hcl.atf.taf.model.ProductBuild;
import com.hcl.atf.taf.model.ProductVersionListMaster;
import com.hcl.atf.taf.model.RunConfiguration;
import com.hcl.atf.taf.model.ScriptTypeMaster;
import com.hcl.atf.taf.model.TestCaseList;
import com.hcl.atf.taf.model.TestRunPlan;
import com.hcl.atf.taf.model.TestSuiteList;
import com.hcl.atf.taf.model.TestToolMaster;

	public class JsonTestRunPlan {
		@JsonProperty
		private Integer testRunPlanId;
		@JsonProperty
		private String testRunPlanName;
		@JsonProperty
		private String description;
		@JsonProperty
		private Integer executionTypeId;
		@JsonProperty
		private Integer testSuiteId;
		@JsonProperty
		private Integer productVersionId;
		@JsonProperty
		private Integer runConfigurationId;
		@JsonProperty
		private String testRunScheduledStartTime;
		@JsonProperty
		private Integer testRunScheduledIntervalInHour;
		@JsonProperty
		private Integer testRunRecurrenceLimit;
		@JsonProperty
		private String testRunCronSchedule;
		@JsonProperty
		private String testRunScheduledEndTime;
		@JsonProperty
		private String notifyByMail;
		@JsonProperty
		private Integer status;
		@JsonProperty
		private String createdDate;
		@JsonProperty
		private String executionTypeName;
		@JsonProperty
		private Integer testToolMasterId;
		@JsonProperty
		private String testToolMasterName;
		@JsonProperty
		private String testSuiteName;
		@JsonProperty
		private Integer testCaseId;
		@JsonProperty
		private String testCaseName;
		@JsonProperty
		private String environmentCombinationName;
		@JsonProperty
		private Integer environmentCombinationId;
		@JsonProperty
		private String deviceName;
		@JsonProperty
		private Integer deviceId;
		@JsonProperty
		private String hostName;
		@JsonProperty
		private String hostIpAddress;
		@JsonProperty
		private Integer autoPostBugs;
		@JsonProperty
		private Integer autoPostResults;
		@JsonProperty
		private String productVersionName;
		@JsonProperty	
		private Integer productId;
		@JsonProperty
		private String productName;
		@JsonProperty	
		private Integer testDataAttachmentId;
		@JsonProperty
		private String testDataAttributeFileName;
		@JsonProperty	
		private Integer objectRepoAttachmentId;
		@JsonProperty
		private String objectRepoAttributeFileName;	
		@JsonProperty
	    private String testScriptSource; 
		@JsonProperty
	    private String testSuiteScriptFileLocation;
		@JsonProperty
	    private String multipleTestSuites;
		@JsonProperty
	    private String testScriptsLevel;
		@JsonProperty
	    private String testScriptType;
		@JsonProperty
	    private String useIntelligentTestPlan;
		@JsonProperty
	    private Integer productBuildId;
		@JsonProperty
	    private String productBuildName;
		@JsonProperty	
		private Integer runConfigurationCount;
		@JsonProperty
		private String automationMode;			
		@JsonProperty
	    private String resultsReportingMode;
		@JsonProperty
		private Integer combinedResultsRunConfigurationId;
		@JsonProperty
		private String totalTestCases;
		@JsonProperty
		private String avgExecutionTime;
		@JsonProperty
		private String testPlanRunConfigs;
		@JsonProperty
		private String isReady;
		@JsonProperty
		private String message;
		
		public JsonTestRunPlan(){
			
		}
		
		public JsonTestRunPlan(TestRunPlan testRunPlan){
			testRunPlanId = testRunPlan.getTestRunPlanId();
			testRunPlanName=testRunPlan.getTestRunPlanName();
			description=testRunPlan.getDescription();
			if(testRunPlan.getProductVersionListMaster() != null ){
				productVersionId = testRunPlan.getProductVersionListMaster().getProductVersionListId();
				productVersionName = testRunPlan.getProductVersionListMaster().getProductVersionName();
				if(testRunPlan.getProductVersionListMaster().getProductMaster() != null ){
					productId = testRunPlan.getProductVersionListMaster().getProductMaster().getProductId();
					productName = testRunPlan.getProductVersionListMaster().getProductMaster().getProductName();
				}
			}
			
			if(testRunPlan.getTestRunScheduledStartTime()!=null)
				testRunScheduledStartTime=DateUtility.dateformatWithOutTime(testRunPlan.getTestRunScheduledStartTime());
			if(testRunPlan.getTestRunScheduledEndTime()!=null)
				testRunScheduledEndTime=DateUtility.dateformatWithOutTime(testRunPlan.getTestRunScheduledEndTime());
			createdDate=DateUtility.dateformatWithOutTime(testRunPlan.getCreatedDate());
			testRunRecurrenceLimit=testRunPlan.getTestRunRecurrenceLimit();
			testRunCronSchedule=testRunPlan.getTestRunCronSchedule();
			testRunScheduledIntervalInHour=testRunPlan.getTestRunScheduledIntervalInHour();
			notifyByMail=testRunPlan.getNotifyByMail();
			if(testRunPlan.getExecutionType()!=null){
				executionTypeId=testRunPlan.getExecutionType().getExecutionTypeId();
			executionTypeName=testRunPlan.getExecutionType().getName();
			}
			if(testRunPlan.getTestToolMaster()!=null){
				testToolMasterId=testRunPlan.getTestToolMaster().getTestToolId();
				testToolMasterName=testRunPlan.getTestToolMaster().getTestToolName();
			}
			if(testRunPlan.getAutoPostBugs() != null){
				autoPostBugs = testRunPlan.getAutoPostBugs();
			}
			if(testRunPlan.getAutoPostResults() != null){
				autoPostResults = testRunPlan.getAutoPostResults();
			}
			if(testRunPlan.getTestData() != null){
				testDataAttachmentId = testRunPlan.getTestData().getAttachmentId();
				testDataAttributeFileName = testRunPlan.getTestData().getAttributeFileName();
			}
			if(testRunPlan.getObjectRepository() != null){
				objectRepoAttachmentId = testRunPlan.getObjectRepository().getAttachmentId();
				objectRepoAttributeFileName = testRunPlan.getObjectRepository().getAttributeFileName();
			}	
			if(testRunPlan.getTestScriptSource() != null){
				testScriptSource = testRunPlan.getTestScriptSource();
			}
			if(testRunPlan.getTestSuiteScriptFileLocation() != null){
				testSuiteScriptFileLocation = testRunPlan.getTestSuiteScriptFileLocation();
			}
			if(testRunPlan.getMultipleTestSuites() != null){
				multipleTestSuites = testRunPlan.getMultipleTestSuites();
			}
			if(testRunPlan.getTestScriptsLevel() != null){
				testScriptsLevel = testRunPlan.getTestScriptsLevel();
			}
			if(testRunPlan.getScriptTypeMaster() != null){
				testScriptType=testRunPlan.getScriptTypeMaster().getScriptType();
			}			
			if(testRunPlan.getUseIntelligentTestPlan() != null){
				if(testRunPlan.getUseIntelligentTestPlan().equalsIgnoreCase("Yes"))
					useIntelligentTestPlan = "1";
				else
				useIntelligentTestPlan= "0";
			} else {
				useIntelligentTestPlan = "0";
			}
			
			if(testRunPlan.getProductBuild() != null){
				productBuildId=testRunPlan.getProductBuild().getProductBuildId();
				productBuildName = testRunPlan.getProductBuild().getBuildname();
			}
			if(testRunPlan.getRunConfigurationList() != null && !testRunPlan.getRunConfigurationList().isEmpty()){
				runConfigurationCount = testRunPlan.getRunConfigurationList().size();
			}else{
				runConfigurationCount = 0;
			}
			
			if(testRunPlan.getAutomationMode() != null){
				automationMode=testRunPlan.getAutomationMode();
			} 
			if(testRunPlan.getResultsReportingMode() != null){
				resultsReportingMode=testRunPlan.getResultsReportingMode();
			} 
			if(testRunPlan.getCombinedResultsRunConfiguration() != null){
				combinedResultsRunConfigurationId=testRunPlan.getCombinedResultsRunConfiguration().getRunconfigId();
			} 
			
			if(testRunPlan.getRunConfigurationList() != null ){
				testPlanRunConfigs = "";
				for(RunConfiguration rc : testRunPlan.getRunConfigurationList()){
					if(rc.getRunconfigId() !=null && rc.getRunconfigName() !=null)
						testPlanRunConfigs = testPlanRunConfigs + rc.getRunconfigId() + "^"+rc.getRunconfigName().trim()+",";					
				}
			}
			if(testPlanRunConfigs != null && !testPlanRunConfigs.isEmpty())
				testPlanRunConfigs = testPlanRunConfigs.substring(0, testPlanRunConfigs.lastIndexOf(","));		
			
			if(testRunPlan.getTestSuiteLists() != null && !testRunPlan.getTestSuiteLists().isEmpty()) {
				Integer count = 0;
				for(TestSuiteList ts : testRunPlan.getTestSuiteLists()){
					if(ts.getTestCaseLists() != null && ts.getTestCaseLists().size() > 0)
						count += ts.getTestCaseLists().size();
				}
				this.totalTestCases = String.valueOf(count);
			}
			
	}
	
	public JsonTestRunPlan(TestRunPlan testRunPlan,String type){
		boolean flag=false;
		
		if(type.equalsIgnoreCase("TestBed")){
			if(testRunPlan.getProductVersionListMaster()!=null && testRunPlan.getProductVersionListMaster().getProductMaster()!=null &&
					testRunPlan.getProductVersionListMaster().getProductMaster().getProductType()!=null &&
					(testRunPlan.getProductVersionListMaster().getProductMaster().getProductType().getProductTypeId()==2)){
				flag=true;
			}
			
			if(testRunPlan.getTestSuiteLists()!=null && !testRunPlan.getTestSuiteLists().isEmpty()){
				Set<TestSuiteList> testSuiteLists= testRunPlan.getTestSuiteLists();
				if(testSuiteLists.size()!=0){
					TestSuiteList testSuiteList= testSuiteLists.iterator().next();
					if(testSuiteList!=null){
						testSuiteId=testSuiteList.getTestSuiteId();
						testSuiteName=testSuiteList.getTestSuiteName();
						if(testSuiteList.getTestCaseLists()!=null && !testSuiteList.getTestCaseLists().isEmpty()){
							Set<TestCaseList> testCaseLists= testSuiteList.getTestCaseLists();
							if(testCaseLists.size()!=0){
								TestCaseList testCaseList= testCaseLists.iterator().next();
								if(testCaseList!=null){
									testCaseName=testCaseList.getTestCaseName();
									testCaseId=testCaseList.getTestCaseId();
									
								}
							}
						}
					}
				}
			}
			if(testRunPlan.getRunConfigurationList()!=null && !testRunPlan.getRunConfigurationList().isEmpty()){
				Set<RunConfiguration> runConfigurationSet =testRunPlan.getRunConfigurationList();
				if (runConfigurationSet.size() != 0)
				{
					RunConfiguration runConfiguration = runConfigurationSet.iterator().next();
					if (runConfiguration != null)
					{
						if(runConfiguration.getEnvironmentcombination()!=null){
							environmentCombinationName = runConfiguration.getEnvironmentcombination().getEnvironmentCombinationName();
							environmentCombinationId=runConfiguration.getEnvironmentcombination().getEnvironment_combination_id();
						}
						if(flag){
							if(runConfiguration.getHostList()!=null){
								hostName = runConfiguration.getHostList().getHostName();
								hostIpAddress=runConfiguration.getHostList().getHostIpAddress();
							}
						}else{
							if(runConfiguration.getGenericDevice()!=null){
								deviceId = runConfiguration.getGenericDevice().getGenericsDevicesId();
								deviceName=runConfiguration.getGenericDevice().getUDID();
							}
							if(runConfiguration.getHostList()!=null){
								hostName = runConfiguration.getHostList().getHostName();
								hostIpAddress=runConfiguration.getHostList().getHostIpAddress();
							}
						}
					}
				}
			}
		}
	}
			
	@JsonIgnore
	public TestRunPlan getTestRunPlan() {
		
		TestRunPlan testRunPlan = new TestRunPlan();
		testRunPlan.setTestRunPlanId(testRunPlanId);
		testRunPlan.setTestRunPlanName(testRunPlanName);
		if(executionTypeId != null){
			ExecutionTypeMaster executionTypeMaster = new ExecutionTypeMaster();
			executionTypeMaster.setExecutionTypeId(executionTypeId);
			testRunPlan.setExecutionType(executionTypeMaster);
		}
		if(productVersionId != null){
			ProductVersionListMaster productVersionListMaster = new ProductVersionListMaster();
			productVersionListMaster.setProductVersionListId(productVersionId);
			testRunPlan.setProductVersionListMaster(productVersionListMaster);
		}
		if (testToolMasterId != null) {
			TestToolMaster testTool = new TestToolMaster();
			testTool.setTestToolId(testToolMasterId);
			testTool.setTestToolName(testToolMasterName);
			testRunPlan.setTestToolMaster(testTool);
		}
		testRunPlan.setDescription(description);
		
		testRunPlan.setNotifyByMail(notifyByMail);
		testRunPlan.setTestRunRecurrenceLimit(testRunRecurrenceLimit);
		testRunPlan.setTestRunScheduledStartTime(DateUtility.dateformatWithHyphnWithOutTime(testRunScheduledStartTime));
		testRunPlan.setTestRunScheduledEndTime(DateUtility.dateformatWithHyphnWithOutTime(testRunScheduledEndTime));
		testRunPlan.setTestRunScheduledIntervalInHour(testRunScheduledIntervalInHour);
		if(autoPostBugs != null){
			testRunPlan.setAutoPostBugs(autoPostBugs);	
		}
		if(autoPostResults != null){
			testRunPlan.setAutoPostResults(autoPostResults);	
		}	
		if(this.status != null ){
			testRunPlan.setStatus(status);			
		}else{
			testRunPlan.setStatus(0);	
		}
		if(this.createdDate == null || this.createdDate.trim().isEmpty()) {
			testRunPlan.setCreatedDate(DateUtility.getCurrentDate());
		} else {
		
			testRunPlan.setCreatedDate(DateUtility.dateformatWithHyphnWithOutTime(createdDate));
		}
		testRunPlan.setTestScriptSource(testScriptSource);
		testRunPlan.setTestSuiteScriptFileLocation(testSuiteScriptFileLocation);
		testRunPlan.setMultipleTestSuites(multipleTestSuites);
		testRunPlan.setTestScriptsLevel(testScriptsLevel);
		testRunPlan.setUseIntelligentTestPlan(useIntelligentTestPlan);
		if (productBuildId != null) {
			ProductBuild productBuild = new ProductBuild();
			productBuild.setProductBuildId(productBuildId);
			productBuild.setBuildname(productBuildName);
			testRunPlan.setProductBuild(productBuild);
		}
		if(testRunCronSchedule !=null ){
			testRunPlan.setTestRunCronSchedule(testRunCronSchedule);
		}
		testRunPlan.setUseIntelligentTestPlan(useIntelligentTestPlan);
		if(testScriptType != null){
			ScriptTypeMaster scriptTypeMaster = new ScriptTypeMaster();
			scriptTypeMaster.setScriptType(testScriptType);
			testRunPlan.setScriptTypeMaster(scriptTypeMaster);
		}
		if(automationMode != null && !automationMode.isEmpty()){
			testRunPlan.setAutomationMode(automationMode);
		} 		
		if(resultsReportingMode != null && !resultsReportingMode.isEmpty()){
			testRunPlan.setResultsReportingMode(resultsReportingMode);
		} 		
		if (combinedResultsRunConfigurationId != null) {
			RunConfiguration combinedResultsRunConfiguration = new RunConfiguration();
			combinedResultsRunConfiguration.setRunconfigId(combinedResultsRunConfigurationId);
			testRunPlan.setCombinedResultsRunConfiguration(combinedResultsRunConfiguration);
		}
		return testRunPlan;
	}
	
	public Integer getTestRunPlanId() {
		return testRunPlanId;
	}
	
	public void setTestRunPlanId(Integer testRunPlanId) {
		this.testRunPlanId = testRunPlanId;
	}
	
	public String getTestRunPlanName() {
		return testRunPlanName;
	}
	
	public void setTestRunPlanName(String testRunPlanName) {
		this.testRunPlanName = testRunPlanName;
	}
	
	public String getDescription() {
		return description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
	public Integer getExecutionTypeId() {
		return executionTypeId;
	}
	
	public void setExecutionTypeId(Integer executionTypeId) {
		this.executionTypeId = executionTypeId;
	}
	
	public Integer getTestSuiteId() {
		return testSuiteId;
	}
	
	public void setTestSuiteId(Integer testSuiteId) {
		this.testSuiteId = testSuiteId;
	}
	
	public Integer getProductVersionId() {
		return productVersionId;
	}
	
	public void setProductVersionId(Integer productVersionId) {
		this.productVersionId = productVersionId;
	}
	
	public Integer getRunConfigurationId() {
		return runConfigurationId;
	}
	
	public void setRunConfigurationId(Integer runConfigurationId) {
		this.runConfigurationId = runConfigurationId;
	}
	
	public String getTestRunScheduledStartTime() {
		return testRunScheduledStartTime;
	}
	
	public void setTestRunScheduledStartTime(String testRunScheduledStartTime) {
		this.testRunScheduledStartTime = testRunScheduledStartTime;
	}
	
	public Integer getTestRunScheduledIntervalInHour() {
		return testRunScheduledIntervalInHour;
	}
	
	public void setTestRunScheduledIntervalInHour(
			Integer testRunScheduledIntervalInHour) {
		this.testRunScheduledIntervalInHour = testRunScheduledIntervalInHour;
	}
	
	public Integer getTestRunRecurrenceLimit() {
		return testRunRecurrenceLimit;
	}
	
	public void setTestRunRecurrenceLimit(Integer testRunRecurrenceLimit) {
		this.testRunRecurrenceLimit = testRunRecurrenceLimit;
	}
	
	public String getTestRunCronSchedule() {
		return testRunCronSchedule;
	}
	
	public void setTestRunCronSchedule(String testRunCronSchedule) {
		this.testRunCronSchedule = testRunCronSchedule;
	}
	
	public String getTestRunScheduledEndTime() {
		return testRunScheduledEndTime;
	}
	
	public void setTestRunScheduledEndTime(String testRunScheduledEndTime) {
		this.testRunScheduledEndTime = testRunScheduledEndTime;
	}
	
	public String getNotifyByMail() {
		return notifyByMail;
	}
	
	public void setNotifyByMail(String notifyByMail) {
		this.notifyByMail = notifyByMail;
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
	
	public String getExecutionTypeName() {
		return executionTypeName;
	}
	
	public void setExecutionTypeName(String executionTypeName) {
		this.executionTypeName = executionTypeName;
	}
	
	public Integer getTestToolMasterId() {
		return testToolMasterId;
	}
	
	public void setTestToolMasterId(Integer testToolMasterId) {
		this.testToolMasterId = testToolMasterId;
	}
	
	public String getTestToolMasterName() {
		return testToolMasterName;
	}
	
	public void setTestToolMasterName(String testToolMasterName) {
		this.testToolMasterName = testToolMasterName;
	}
	
	public Integer getAutoPostBugs() {
		return autoPostBugs;
	}
	
	public void setAutoPostBugs(Integer autoPostBugs) {
		this.autoPostBugs = autoPostBugs;
	}
	
	public Integer getAutoPostResults() {
		return autoPostResults;
	}
	
	public void setAutoPostResults(Integer autoPostResults) {
		this.autoPostResults = autoPostResults;
	}
	
	
	public Integer getTestDataAttachmentId() {
		return testDataAttachmentId;
	}
	
	public void setTestDataAttachmentId(Integer testDataAttachmentId) {
		this.testDataAttachmentId = testDataAttachmentId;
	}
	
	public String getTestDataAttributeFileName() {
		return testDataAttributeFileName;
	}
	
	public void setTestDataAttributeFileName(String testDataAttributeFileName) {
		this.testDataAttributeFileName = testDataAttributeFileName;
	}
	
	public Integer getObjectRepoAttachmentId() {
		return objectRepoAttachmentId;
	}
	
	public void setObjectRepoAttachmentId(Integer objectRepoAttachmentId) {
		this.objectRepoAttachmentId = objectRepoAttachmentId;
	}
	
	public String getObjectRepoAttributeFileName() {
		return objectRepoAttributeFileName;
	}
	
	public void setObjectRepoAttributeFileName(String objectRepoAttributeFileName) {
		this.objectRepoAttributeFileName = objectRepoAttributeFileName;
	}
	
	public String getTestScriptSource() {
		return testScriptSource;
	}
	public void setTestScriptSource(String testScriptSource) {
		this.testScriptSource = testScriptSource;
	}
	
	public String getTestSuiteScriptFileLocation() {
		return testSuiteScriptFileLocation;
	}
	public void setTestSuiteScriptFileLocation(String testSuiteScriptFileLocation) {
		this.testSuiteScriptFileLocation = testSuiteScriptFileLocation;
	}
	
	public String getMultipleTestSuites() {
		return multipleTestSuites;
	}
	public void setMultipleTestSuites(String multipleTestSuites) {
		this.multipleTestSuites = multipleTestSuites;
	}
	
	public String getTestScriptsLevel() {
		return testScriptsLevel;
	}
	public void setTestScriptsLevel(String testScriptsLevel) {
		this.testScriptsLevel = testScriptsLevel;
	}

	public String getTestScriptType() {
		return testScriptType;
	}

	public void setTestScriptType(String testScriptType) {
		this.testScriptType = testScriptType;
	}

	public Integer getProductBuildId() {
		return productBuildId;
	}

	public void setProductBuildId(Integer productBuildId) {
		this.productBuildId = productBuildId;
	}

	public String getProductBuildName() {
		return productBuildName;
	}

	public void setProductBuildName(String productBuildName) {
		this.productBuildName = productBuildName;
	}
	
	public String getAutomationMode() {
		return automationMode;
	}

	public void setAutomationMode(String automationMode) {
		this.automationMode = automationMode;
	}

	public String getResultsReportingMode() {
		return resultsReportingMode;
	}

	public void setResultsReportingMode(String resultsReportingMode) {
		this.resultsReportingMode = resultsReportingMode;
	}

	public Integer getCombinedResultsRunConfigurationId() {
		return combinedResultsRunConfigurationId;
	}

	public void setCombinedResultsRunConfigurationId(Integer combinedResultsRunConfigurationId) {
		this.combinedResultsRunConfigurationId = combinedResultsRunConfigurationId;
	}

	public String getUseIntelligentTestPlan() {
		return useIntelligentTestPlan;
	}

	public void setUseIntelligentTestPlan(String useIntelligentTestPlan) {
		this.useIntelligentTestPlan = useIntelligentTestPlan;
	}

	public String getTotalTestCases() {
		return totalTestCases;
	}

	public void setTotalTestCases(String totalTestCases) {
		this.totalTestCases = totalTestCases;
	}

	public String getAvgExecutionTime() {
		return avgExecutionTime;
	}

	public void setAvgExecutionTime(String avgExecutionTime) {
		this.avgExecutionTime = avgExecutionTime;
	}

	public String getIsReady() {
		return isReady;
	}

	public void setIsReady(String isReady) {
		this.isReady = isReady;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}
