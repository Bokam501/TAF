package com.hcl.atf.taf.model.json;


import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.hcl.atf.taf.controller.utilities.DateUtility;
import com.hcl.atf.taf.model.DecouplingCategory;
import com.hcl.atf.taf.model.Environment;
import com.hcl.atf.taf.model.EnvironmentCombination;
import com.hcl.atf.taf.model.ExecutionPriority;
import com.hcl.atf.taf.model.HostList;
import com.hcl.atf.taf.model.ProductFeature;
import com.hcl.atf.taf.model.TestCaseList;
import com.hcl.atf.taf.model.TestRunJob;
import com.hcl.atf.taf.model.TestSuiteList;
import com.hcl.atf.taf.model.UserList;
import com.hcl.atf.taf.model.WorkPackage;
import com.hcl.atf.taf.model.WorkPackageTestCaseExecutionPlan;
import com.hcl.atf.taf.model.WorkShiftMaster;
import com.hcl.atf.taf.model.WorkpackageRunConfiguration;


public class JsonWorkPackageTestCaseExecutionPlan implements java.io.Serializable{

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
	private String hostName;
	@JsonProperty	
	private String testCaseScriptQualifiedName;
	@JsonProperty	
	private String testCaseScriptFileName;

	@JsonProperty
	private Integer productModeId;
	@JsonProperty	
	private String productModeName;
	@JsonProperty	
	private String sourceType;
	@JsonProperty
	private Integer featureId;

	@JsonProperty	
	private String featureName;
	@JsonProperty	
	private String sourceTable;
	@JsonProperty	
	private String sourceData;
	@JsonProperty	
	private Integer status;
	@JsonProperty	
	private Integer testRunJobId;
	@JsonProperty	
	private Integer executionType;

	public JsonWorkPackageTestCaseExecutionPlan() {
	}	

	public JsonWorkPackageTestCaseExecutionPlan(WorkPackageTestCaseExecutionPlan workPackageTestCaseExecutionPlan) {
		try{
			this.id=workPackageTestCaseExecutionPlan.getId();

			this.testcaseId=workPackageTestCaseExecutionPlan.getTestCase().getTestCaseId();
			this.testcaseName=workPackageTestCaseExecutionPlan.getTestCase().getTestCaseName();
			this.testcaseDescription=workPackageTestCaseExecutionPlan.getTestCase().getTestCaseDescription();
			this.testcaseCode=workPackageTestCaseExecutionPlan.getTestCase().getTestCaseCode();
			this.testcaseType=workPackageTestCaseExecutionPlan.getTestCase().getTestCaseType();

			if(workPackageTestCaseExecutionPlan.getTestCase().getTestCasePriority()!=null){
				this.testcasePriority=workPackageTestCaseExecutionPlan.getTestCase().getTestCasePriority().getPriorityName();
			}
			this.testCaseScriptFileName=workPackageTestCaseExecutionPlan.getTestCase().getTestCaseScriptFileName();
			this.testCaseScriptQualifiedName=workPackageTestCaseExecutionPlan.getTestCase().getTestCaseScriptQualifiedName();

			this.workPackageId=workPackageTestCaseExecutionPlan.getWorkPackage().getWorkPackageId();
			this.workPackageName=workPackageTestCaseExecutionPlan.getWorkPackage().getName();

			this.workpackagePlannedStartDate=DateUtility.sdfDateformatWithOutTime(workPackageTestCaseExecutionPlan.getWorkPackage().getPlannedStartDate());
			this.workpackagePlannedEndDate=DateUtility.sdfDateformatWithOutTime(workPackageTestCaseExecutionPlan.getWorkPackage().getPlannedEndDate());
			if(workPackageTestCaseExecutionPlan.getEnvironmentList()!=null){
				Set<Environment> environments=workPackageTestCaseExecutionPlan.getEnvironmentList();
				for (Environment environment : environments) {
					if(environmentName==null || environmentName.equals(""))
						environmentName=environment.getEnvironmentName();
					else
						environmentName=environmentName+"-"+environment.getEnvironmentName();
				}
			}


			if(workPackageTestCaseExecutionPlan.getRunConfiguration()!=null){
				if(workPackageTestCaseExecutionPlan.getRunConfiguration().getRunconfiguration()!=null){

					runConfigurationName=workPackageTestCaseExecutionPlan.getRunConfiguration().getRunconfiguration().getRunconfigName();
					runConfigurationId=workPackageTestCaseExecutionPlan.getRunConfiguration().getWorkpackageRunConfigurationId();

					if(runConfigurationName.contains(":")){
						environmentCombinationName=runConfigurationName.substring(0,runConfigurationName.indexOf(":"));
						deviceName=runConfigurationName.substring(runConfigurationName.indexOf(":")+1);
					}else{
						environmentCombinationName=runConfigurationName;
					}
				}
			}


			this.executionStatus = workPackageTestCaseExecutionPlan.getExecutionStatus();
			if(workPackageTestCaseExecutionPlan.getIsExecuted()!=null){
				this.isExecuted = workPackageTestCaseExecutionPlan.getIsExecuted();
			}else{
				this.isExecuted=0;
			}

			if(workPackageTestCaseExecutionPlan.getTestLead()!=null){
				this.testLeadId =workPackageTestCaseExecutionPlan.getTestLead().getUserId();
				this.testLeadName =workPackageTestCaseExecutionPlan.getTestLead().getLoginId();
			}else{
				this.testLeadId=0;
				this.testLeadName=null;
			}
			if(workPackageTestCaseExecutionPlan.getTester()!=null){
				this.testerId =workPackageTestCaseExecutionPlan.getTester().getUserId();
				this.testerName =workPackageTestCaseExecutionPlan.getTester().getLoginId();
			}else{
				this.testerId=0;
				this.testerName=null;
			}

			if(workPackageTestCaseExecutionPlan.getPlannedExecutionDate()!=null){
				this.plannedExecutionDate=DateUtility.sdfDateformatWithOutTime(workPackageTestCaseExecutionPlan.getPlannedExecutionDate());
			}
			if(workPackageTestCaseExecutionPlan.getActualExecutionDate()!=null){
				this.actualExecutionDate=DateUtility.sdfDateformatWithOutTime(workPackageTestCaseExecutionPlan.getActualExecutionDate());
			}

			if(workPackageTestCaseExecutionPlan.getCreatedDate()!=null)
				this.createdDate=DateUtility.dateformatWithOutTime(workPackageTestCaseExecutionPlan.getCreatedDate());
			if(workPackageTestCaseExecutionPlan.getModifiedDate()!=null)
				this.modifiedDate=DateUtility.dateformatWithOutTime(workPackageTestCaseExecutionPlan.getModifiedDate());
			if(workPackageTestCaseExecutionPlan.getExecutionStatus()!=null )
				this.executionStatus=workPackageTestCaseExecutionPlan.getExecutionStatus();

			if(workPackageTestCaseExecutionPlan.getPlannedWorkShiftMaster()!=null){
				this.plannedShiftId=workPackageTestCaseExecutionPlan.getPlannedWorkShiftMaster().getShiftId();
				this.plannedShiftName=workPackageTestCaseExecutionPlan.getPlannedWorkShiftMaster().getShiftName();
			}

			if(workPackageTestCaseExecutionPlan.getActualWorkShiftMaster()!=null){
				this.actualShiftId=workPackageTestCaseExecutionPlan.getActualWorkShiftMaster().getShiftId();
				this.actualShiftName=workPackageTestCaseExecutionPlan.getActualWorkShiftMaster().getShiftName();
			}
			if(workPackageTestCaseExecutionPlan.getTestCase().getDecouplingCategory()!=null && workPackageTestCaseExecutionPlan.getTestCase().getDecouplingCategory().size()!=0){
				Set<DecouplingCategory> decouplingCategorySet =workPackageTestCaseExecutionPlan.getTestCase().getDecouplingCategory();
				if (decouplingCategorySet.size() != 0)
				{
					DecouplingCategory decouplingCategory = decouplingCategorySet.iterator().next();
					if (decouplingCategory != null)
					{
						decouplingCategoryId = decouplingCategory.getDecouplingCategoryId();
						decouplingCategoryName = decouplingCategory.getDecouplingCategoryName();				
					}
				}
			}

			if(workPackageTestCaseExecutionPlan.getTestCase().getProductFeature()!=null && workPackageTestCaseExecutionPlan.getTestCase().getProductFeature().size()!=0){
				Set<ProductFeature> featureSet =workPackageTestCaseExecutionPlan.getTestCase().getProductFeature();
				String temp="";
				if (featureSet.size() != 0)
				{
					for(ProductFeature ft:featureSet){
						if (ft != null)
						{
							if(temp!=null && temp.length()<=0){
								temp = ft.getProductFeatureName();
							}
							else{
								temp =temp+","+ ft.getProductFeatureName();
							}
						}
					}

				}
				featureName=temp;
			}
			if(workPackageTestCaseExecutionPlan.getExecutionPriority()!=null){
				this.executionPriorityId=workPackageTestCaseExecutionPlan.getExecutionPriority().getExecutionPriorityId();
				this.executionPriorityName=workPackageTestCaseExecutionPlan.getExecutionPriority().getExecutionPriority();
				this.executionPriorityDisplayName=workPackageTestCaseExecutionPlan.getExecutionPriority().getDisplayName();
				this.executionName=workPackageTestCaseExecutionPlan.getExecutionPriority().getExecutionPriorityName();
			}

			if(workPackageTestCaseExecutionPlan.getWorkPackage().getProductBuild().getProductVersion() != null){
				this.productVersionId = workPackageTestCaseExecutionPlan.getWorkPackage().getProductBuild().getProductVersion().getProductVersionListId();
				this.productVersionName = workPackageTestCaseExecutionPlan.getWorkPackage().getProductBuild().getProductVersion().getProductVersionName();
			}

			if(workPackageTestCaseExecutionPlan.getWorkPackage().getProductBuild().getProductVersion().getProductMaster() != null){
				this.productId = workPackageTestCaseExecutionPlan.getWorkPackage().getProductBuild().getProductVersion().getProductMaster().getProductId();
				this.productName = workPackageTestCaseExecutionPlan.getWorkPackage().getProductBuild().getProductVersion().getProductMaster().getProductName();
			}

			if(workPackageTestCaseExecutionPlan.getWorkPackage().getProductBuild().getProductVersion().getProductMaster().getProductMode() != null){
				this.productModeId = workPackageTestCaseExecutionPlan.getWorkPackage().getProductBuild().getProductVersion().getProductMaster().getProductMode().getModeId();
				this.productModeName = workPackageTestCaseExecutionPlan.getWorkPackage().getProductBuild().getProductVersion().getProductMaster().getProductMode().getModeName();
			}

			if(workPackageTestCaseExecutionPlan.getTestSuiteList()!=null){
				this.testsuiteId=workPackageTestCaseExecutionPlan.getTestSuiteList().getTestSuiteId();
				this.testsuiteName=workPackageTestCaseExecutionPlan.getTestSuiteList().getTestSuiteName();
			}
			if(workPackageTestCaseExecutionPlan.getFeature()!=null){
				this.featureId=workPackageTestCaseExecutionPlan.getFeature().getProductFeatureId();
			}
			if(workPackageTestCaseExecutionPlan.getHostList()!=null){
				this.hostId=workPackageTestCaseExecutionPlan.getHostList().getHostId();
				this.hostName=workPackageTestCaseExecutionPlan.getHostList().getHostName();
			}

			this.sourceType=workPackageTestCaseExecutionPlan.getSourceType();
			if(sourceType!=null && !sourceType.equals("")){
				if(this.sourceType.equalsIgnoreCase("Feature")){
					this.sourceType=this.sourceType+":"+workPackageTestCaseExecutionPlan.getFeature().getProductFeatureName();
				}else if(this.sourceType.equalsIgnoreCase("TestSuite")){
					this.sourceType=this.sourceType+":"+workPackageTestCaseExecutionPlan.getTestSuiteList().getTestSuiteName();
				}
			}

			this.sourceTable="TestCase";
			this.status=workPackageTestCaseExecutionPlan.getStatus();
			if(workPackageTestCaseExecutionPlan.getTestRunJob()!=null){
				this.testRunJobId=workPackageTestCaseExecutionPlan.getTestRunJob().getTestRunJobId();
			}

			if(workPackageTestCaseExecutionPlan.getEnvironmentCombination() != null ){
				this.environmentCombinationId = workPackageTestCaseExecutionPlan.getEnvironmentCombination().getEnvironment_combination_id();
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}



	@JsonIgnore
	public WorkPackageTestCaseExecutionPlan getWorkPackageTestCaseExecutionPlan() {


		WorkPackageTestCaseExecutionPlan workPackageTestCaseExecutionPlan = new WorkPackageTestCaseExecutionPlan();
		workPackageTestCaseExecutionPlan.setId(this.getId());

		TestCaseList testCase = new TestCaseList();
		testCase.setTestCaseId(this.testcaseId);
		workPackageTestCaseExecutionPlan.setTestCase(testCase);


		WorkPackage workPackage = new WorkPackage();
		workPackage.setWorkPackageId(this.workPackageId);
		workPackageTestCaseExecutionPlan.setWorkPackage(workPackage);

		WorkpackageRunConfiguration wprc= new WorkpackageRunConfiguration();
		wprc.setWorkpackageRunConfigurationId(runConfigurationId);
		workPackageTestCaseExecutionPlan.setRunConfiguration(wprc);

		UserList testLead = new UserList();
		if(this.testLeadName!=null && !this.testLeadName.equals("")){
			if(isInteger(this.testLeadName)){
				testLead= new UserList();
				testLead.setUserId(new Integer(this.testLeadName));
				workPackageTestCaseExecutionPlan.setTestLead(testLead);
			}else{
				testLead= new UserList();
				testLead.setFirstName(this.testLeadName);	
				workPackageTestCaseExecutionPlan.setTestLead(testLead);
			}
		}



		UserList tester =  new UserList();
		if(this.testerName!=null && !this.testerName.equals("")){
			if(isInteger(this.testerName)){
				tester = new UserList();
				tester.setUserId(new Integer(this.testerName));
				workPackageTestCaseExecutionPlan.setTester(tester);
			}else{
				tester = new UserList();
				tester.setFirstName(this.testerName);
				workPackageTestCaseExecutionPlan.setTester(tester);
			}
		}


		if(this.plannedExecutionDate == null || this.plannedExecutionDate.trim().isEmpty()) {
			workPackageTestCaseExecutionPlan.setPlannedExecutionDate(DateUtility.getCurrentTime());
		} else {

			workPackageTestCaseExecutionPlan.setPlannedExecutionDate(DateUtility.dateformatWithOutTime(plannedExecutionDate));
		}


		if(this.actualExecutionDate == null || this.actualExecutionDate.trim().isEmpty()) {
			workPackageTestCaseExecutionPlan.setActualExecutionDate(DateUtility.getCurrentTime());
		} else {
			workPackageTestCaseExecutionPlan.setActualExecutionDate(DateUtility.dateformatWithOutTime(this.actualExecutionDate));
		}

		if(this.createdDate == null || this.createdDate.trim().isEmpty()) {
			workPackageTestCaseExecutionPlan.setCreatedDate(DateUtility.getCurrentTime());
		} else {
			workPackageTestCaseExecutionPlan.setCreatedDate(DateUtility.dateformatWithOutTime(this.createdDate));

		}
		workPackageTestCaseExecutionPlan.setModifiedDate(DateUtility.getCurrentTime());
		workPackageTestCaseExecutionPlan.setExecutionStatus(executionStatus);
		workPackageTestCaseExecutionPlan.setIsExecuted(this.isExecuted);


		WorkShiftMaster plannedWorkShiftMaster=new WorkShiftMaster();
		if(this.plannedShiftName!=null && !this.plannedShiftName.equals("")){
			if(isInteger(this.plannedShiftName)){
				plannedWorkShiftMaster= new WorkShiftMaster();
				plannedWorkShiftMaster.setShiftId(new Integer(this.plannedShiftName));
				workPackageTestCaseExecutionPlan.setPlannedWorkShiftMaster(plannedWorkShiftMaster);
			}else{
				plannedWorkShiftMaster= new WorkShiftMaster();
				plannedWorkShiftMaster.setShiftName(this.plannedShiftName);
				workPackageTestCaseExecutionPlan.setPlannedWorkShiftMaster(plannedWorkShiftMaster);
			}
		}


		WorkShiftMaster actualWorkShiftMaster=new WorkShiftMaster();
		if(this.actualShiftName!=null && !this.actualShiftName.equals("")){
			if(isInteger(this.actualShiftName)){
				actualWorkShiftMaster= new WorkShiftMaster();
				actualWorkShiftMaster.setShiftId(new Integer(this.actualShiftName));
				workPackageTestCaseExecutionPlan.setActualWorkShiftMaster(actualWorkShiftMaster);
			}else{
				actualWorkShiftMaster= new WorkShiftMaster();
				actualWorkShiftMaster.setShiftName(this.actualShiftName);
				workPackageTestCaseExecutionPlan.setActualWorkShiftMaster(actualWorkShiftMaster);
			}
		}

			
		if(executionPriorityId!=null){
			ExecutionPriority executionPriority = new ExecutionPriority();
			executionPriority.setExecutionPriorityId(executionPriorityId);
			executionPriority.setExecutionPriority(executionPriorityName);
			executionPriority.setExecutionPriorityName(executionName);
			workPackageTestCaseExecutionPlan.setExecutionPriority(executionPriority);
		}
		TestSuiteList testSuiteList = new TestSuiteList();
		if(testsuiteId!=null){
			testSuiteList.setTestSuiteId(testsuiteId);
			workPackageTestCaseExecutionPlan.setTestSuiteList(testSuiteList);
		}

		ProductFeature productFeature = new ProductFeature();
		if(featureId!=null){
			productFeature.setProductFeatureId(featureId);
			workPackageTestCaseExecutionPlan.setFeature(productFeature);
		}
		HostList hostList = new HostList();
		if(hostId!=null){
			hostList.setHostId(hostId);
			workPackageTestCaseExecutionPlan.setHostList(hostList);
		}
		workPackageTestCaseExecutionPlan.setSourceType(sourceType);
		workPackageTestCaseExecutionPlan.setStatus(status);
		TestRunJob testRunJob = new TestRunJob();
		if(testRunJobId!=null){
			testRunJob.setTestRunJobId(testRunJobId);
			workPackageTestCaseExecutionPlan.setTestRunJob(testRunJob);	
		}

		if( this.environmentCombinationId != null) {
			EnvironmentCombination environmentCombination = new EnvironmentCombination();
			environmentCombination.setEnvironment_combination_id(environmentCombinationId);
			workPackageTestCaseExecutionPlan.setEnvironmentCombination(environmentCombination);
		}
		return workPackageTestCaseExecutionPlan;
	}


	public static boolean isInteger(String s) {
		try { 
			Integer.parseInt(s); 
		} catch(NumberFormatException e) { 
			return false; 
		}
		return true;
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

	public String getHostName() {
		return hostName;
	}

	public void setHostName(String hostName) {
		this.hostName = hostName;
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



	public String getProductModeName() {
		return productModeName;
	}

	public void setProductModeName(String productModeName) {
		this.productModeName = productModeName;
	}

	public String getSourceType() {
		return sourceType;
	}

	public void setSourceType(String sourceType) {
		this.sourceType = sourceType;
	}

	public Integer getFeatureId() {
		return featureId;
	}

	public void setFeatureId(Integer featureId) {
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

	public Integer getProductModeId() {
		return productModeId;
	}

	public void setProductModeId(Integer productModeId) {
		this.productModeId = productModeId;
	}

	public String getSourceData() {
		return sourceData;
	}

	public void setSourceData(String sourceData) {
		this.sourceData = sourceData;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getTestRunJobId() {
		return testRunJobId;
	}

	public void setTestRunJobId(Integer testRunJobId) {
		this.testRunJobId = testRunJobId;
	}

	public Integer getExecutionType() {
		return executionType;
	}

	public void setExecutionType(Integer executionType) {
		this.executionType = executionType;
	}
}
