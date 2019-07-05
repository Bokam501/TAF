package com.hcl.atf.taf.mongodb.model;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.hcl.atf.taf.model.WorkPackage;

@Document(collection = "testworkpackages")
public class WorkPackageMongo {
	@Id	
	private String id;
	
	private Integer workPackageId;
	private String workPackageName;
	private String description;
	private Integer productId;
	private String productName;
	private String testfactoryId;
	private String testfactoryName;
	private Integer testCentersId;
	private String testCentersName;
	
	
	private Integer buildId;
	private String buildName;
	private Integer versionId;
	private String versionName;
	private String status;
	private String workPackageType;
	private Date plannedStartDate;
	private Date plannedEndDate;
	private Date actualStartDate;
	private Date actualEndDate;
	
	private String runCode;
	private Integer workPackageTypeId;
	private Integer workFlowEventId;
	private Integer workpackageCloneId;
	private Integer runConfigurationId;
	private Integer  testRunPlanId;
	private Integer runCounfigurationsCount;
	private Integer environmentCombinationsCount;
	private Integer jobCount;
	private Integer plannedTestCaseExecutionsCount;
	private Integer totalFeaturesCount;
	private Integer plannedFeaturesCount;
	private Integer totalTestSuitesCount;
	private Integer plannedTestSuitesCount;
	private Integer totalTestCasesCount;
	private Integer plannedTestCasesCount;

	private String testRunPlanName;
	private String creationType;
	
	
	private String executionStatus;
	

	private Integer iterationNumber;
	
	private Integer lifeCyclePhaseId;
	private String lifeCyclePhaseName;
	
	private String parentStatus;
	
	
	private Date createDate;
	private Date modifiedDate;
	
	private String result;
	
	
	public WorkPackageMongo(){
		
	}
	
	public WorkPackageMongo(WorkPackage workPackage){
		
		this.id = workPackage.getWorkPackageId()+"";
		this.workPackageId = workPackage.getWorkPackageId();
		
		this.workPackageName = workPackage.getName();
		this.description = workPackage.getDescription();
		
		if (workPackage.getStatus().equals("1"))
			this.status = "Active";
		else
			this.status = "InActive";
		
		this.plannedStartDate = workPackage.getPlannedStartDate();
		this.plannedEndDate = workPackage.getPlannedEndDate();
		this.actualStartDate = workPackage.getActualStartDate();
		this.actualEndDate = workPackage.getActualEndDate();
		this.createDate = workPackage.getCreateDate();
		this.modifiedDate = workPackage.getModifiedDate();
		
		if(workPackage.getRunConfigurationList()!=null){
			this.runCounfigurationsCount = workPackage.getRunConfigurationList().size();
		}
		if(workPackage.getEnvironmentCombinationList()!=null){
			this.environmentCombinationsCount = workPackage.getEnvironmentCombinationList().size();
		}
		if(workPackage.getTestRunJobSet()!=null){
			this.jobCount = workPackage.getTestRunJobSet().size();
		}
		if(workPackage.getWorkPackageType()!=null){
			this.workPackageType = workPackage.getWorkPackageType().getName();
		}

		this.totalFeaturesCount = workPackage.getProductFeature().size();
		this.plannedFeaturesCount = workPackage.getWorkPackageFeature().size();
		this.totalTestSuitesCount = workPackage.getTestSuiteList().size();
		this.plannedTestSuitesCount = workPackage.getWorkPackageTestSuites().size();
		this.totalTestCasesCount = workPackage.getTestcaseList().size();
		this.plannedTestCasesCount = workPackage.getWorkPackageTestCases().size();
		
		this.plannedTestCaseExecutionsCount = workPackage.getWorkPackageTestCaseExecutionPlan().size();
		if(testRunPlanId!=null){
			this.testRunPlanId = workPackage.getTestRunPlan().getTestRunPlanId();
			this.testRunPlanName = workPackage.getTestRunPlan().getTestRunPlanName();
		}
		
		if (workPackage.getWorkpackageCloneId() != null)
			this.creationType = "Cloned";
		else if (workPackage.getTestRunPlanGroup() != null)
			this.creationType = "From Test Run Plan Group";
		else if (workPackage.getTestRunPlan() != null)
			this.creationType = "From Test Run Plan";
		else 
			this.creationType = "Direct";
		this.versionId=workPackage.getProductBuild().getProductVersion().getProductVersionListId();
		this.versionName=workPackage.getProductBuild().getProductVersion().getProductVersionName();
		this.buildId = workPackage.getProductBuild().getProductBuildId();
		this.buildName=workPackage.getProductBuild().getBuildname();
		this.runCode = runCode;
		if(workPackage.getWorkFlowEvent()!=null){
			this.workFlowEventId = workPackage.getWorkFlowEvent().getWorkfloweventId();
		}
		if(workPackage.getWorkpackageCloneId()!=null){
			this.workpackageCloneId = workPackage.getWorkpackageCloneId();
		}
		this.runConfigurationId = null;
		if(workPackage.getProductBuild() != null){
			if(workPackage.getProductBuild().getStatus() != null && workPackage.getProductBuild().getStatus() == 1){
				this.parentStatus = "Active";
			}else{
				this.parentStatus = "InActive";
			}
		}
			
			
			
		this.productId=workPackage.getProductBuild().getProductVersion().getProductMaster().getProductId();
		this.productName=workPackage.getProductBuild().getProductVersion().getProductMaster().getProductName();
		this.testfactoryId=workPackage.getProductBuild().getProductVersion().getProductMaster().getTestFactory().getTestFactoryId()+"";
		this.testfactoryName=workPackage.getProductBuild().getProductVersion().getProductMaster().getTestFactory().getTestFactoryName();
		this.testCentersId=workPackage.getProductBuild().getProductVersion().getProductMaster().getTestFactory().getTestFactoryLab().getTestFactoryLabId();
		this.testCentersName=workPackage.getProductBuild().getProductVersion().getProductMaster().getTestFactory().getTestFactoryLab().getTestFactoryLabName();
		
		if(workPackage.getWorkFlowEvent().getWorkFlow()!=null){
			this.executionStatus=workPackage.getWorkFlowEvent().getWorkFlow().getStageName();
		}
		
		if(workPackage.getIterationNumber()!=null){
			this.iterationNumber=workPackage.getIterationNumber();
		}
			
		if(workPackage.getLifeCyclePhase()!=null){
			this.lifeCyclePhaseId=workPackage.getLifeCyclePhase().getLifeCyclePhaseId();
			this.lifeCyclePhaseName=workPackage.getLifeCyclePhase().getName();
		}
	
		
		
	}

	
	public WorkPackageMongo(Integer _id, String name, String description, String status, Date plannedStartDate, Date plannedEndDate,
			Date actualStartDate, Date actualEndDate, Date createDate, Date modifiedDate, Integer productBuildId, 
			String runCode, Integer isActive, Integer workPackageTypeId, Integer workFlowEventId, Integer workpackageCloneId,
			Integer runConfigurationId,Integer  testRunPlanId){
		this.id = _id+"";
		this.workPackageName = workPackageName;
		this.description = description;
		this.status = status;
		this.plannedStartDate = plannedStartDate;
		this.plannedEndDate = plannedEndDate;
		this.actualStartDate = actualStartDate;
		this.actualEndDate = actualEndDate;
		this.createDate = createDate;
		this.modifiedDate = modifiedDate;
		this.buildId = productBuildId;
		this.runCode = runCode;
		this.workPackageTypeId = workPackageTypeId;	
		this.workFlowEventId = workFlowEventId;
		this.workpackageCloneId = workpackageCloneId;
		this.runConfigurationId = runConfigurationId;
		this.testRunPlanId = testRunPlanId;
	}

	public String getId() {
		return id;
	}

	public void setId(String _id) {
		this.id = _id;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Date getPlannedStartDate() {
		return plannedStartDate;
	}

	public void setPlannedStartDate(Date plannedStartDate) {
		this.plannedStartDate = plannedStartDate;
	}

	public Date getPlannedEndDate() {
		return plannedEndDate;
	}

	public void setPlannedEndDate(Date plannedEndDate) {
		this.plannedEndDate = plannedEndDate;
	}

	public Date getActualStartDate() {
		return actualStartDate;
	}

	public void setActualStartDate(Date actualStartDate) {
		this.actualStartDate = actualStartDate;
	}

	public Date getActualEndDate() {
		return actualEndDate;
	}

	public void setActualEndDate(Date actualEndDate) {
		this.actualEndDate = actualEndDate;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public Date getModifiedDate() {
		return modifiedDate;
	}

	public void setModifiedDate(Date modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

	

	public String getRunCode() {
		return runCode;
	}

	public void setRunCode(String runCode) {
		this.runCode = runCode;
	}

	public Integer getWorkPackageTypeId() {
		return workPackageTypeId;
	}

	public void setWorkPackageTypeId(Integer workPackageTypeId) {
		this.workPackageTypeId = workPackageTypeId;
	}

	public Integer getWorkFlowEventId() {
		return workFlowEventId;
	}

	public void setWorkFlowEventId(Integer workFlowEventId) {
		this.workFlowEventId = workFlowEventId;
	}

	public Integer getWorkpackageCloneId() {
		return workpackageCloneId;
	}

	public void setWorkpackageCloneId(Integer workpackageCloneId) {
		this.workpackageCloneId = workpackageCloneId;
	}

	public Integer getRunConfigurationId() {
		return runConfigurationId;
	}

	public void setRunConfigurationId(Integer runConfigurationId) {
		this.runConfigurationId = runConfigurationId;
	}

	public Integer getTestRunPlanId() {
		return testRunPlanId;
	}

	public void setTestRunPlanId(Integer testRunPlanId) {
		this.testRunPlanId = testRunPlanId;
	}

	@Override
    public String toString(){
	return workPackageName+":"+description+":"+status+":"+plannedStartDate+":"+ plannedEndDate+":"+
			actualStartDate+":"+actualEndDate+":"+createDate+":"+modifiedDate+":"+buildId+":"+runCode+":";
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

	public String getTestfactoryId() {
		return testfactoryId;
	}

	public void setTestfactoryId(String testfactoryId) {
		this.testfactoryId = testfactoryId;
	}

	public String getTestfactoryName() {
		return testfactoryName;
	}

	public void setTestfactoryName(String testfactoryName) {
		this.testfactoryName = testfactoryName;
	}

	

	public Integer getWorkPackageId() {
		return workPackageId;
	}

	public void setWorkPackageId(Integer workPackageId) {
		this.workPackageId = workPackageId;
	}

	
	public String getWorkPackageType() {
		return workPackageType;
	}

	public void setWorkPackageType(String workPackageType) {
		this.workPackageType = workPackageType;
	}

	public Integer getTotalFeaturesCount() {
		return totalFeaturesCount;
	}

	public void setTotalFeaturesCount(Integer totalFeaturesCount) {
		this.totalFeaturesCount = totalFeaturesCount;
	}

	public Integer getTotalTestSuitesCount() {
		return totalTestSuitesCount;
	}

	public void setTotalTestSuitesCount(Integer totalTestSuitesCount) {
		this.totalTestSuitesCount = totalTestSuitesCount;
	}

	public Integer getTotalTestCasesCount() {
		return totalTestCasesCount;
	}

	public void setTotalTestCasesCount(Integer totalTestCasesCount) {
		this.totalTestCasesCount = totalTestCasesCount;
	}

	public Integer getPlannedTestCasesCount() {
		return plannedTestCasesCount;
	}

	public void setPlannedTestCasesCount(Integer plannedTestCasesCount) {
		this.plannedTestCasesCount = plannedTestCasesCount;
	}

	public Integer getVersionId() {
		return versionId;
	}

	public void setVersionId(Integer versionId) {
		this.versionId = versionId;
	}

	public String getVersionName() {
		return versionName;
	}

	public void setVersionName(String versionName) {
		this.versionName = versionName;
	}

	public Integer getRunCounfigurationsCount() {
		return runCounfigurationsCount;
	}

	public void setRunCounfigurationsCount(Integer runCounfigurationsCount) {
		this.runCounfigurationsCount = runCounfigurationsCount;
	}

	public Integer getPlannedTestCaseExecutionsCount() {
		return plannedTestCaseExecutionsCount;
	}

	public void setPlannedTestCaseExecutionsCount(
			Integer plannedTestCaseExecutionsCount) {
		this.plannedTestCaseExecutionsCount = plannedTestCaseExecutionsCount;
	}

	public Integer getPlannedFeaturesCount() {
		return plannedFeaturesCount;
	}

	public void setPlannedFeaturesCount(Integer plannedFeaturesCount) {
		this.plannedFeaturesCount = plannedFeaturesCount;
	}

	public String getTestRunPlanName() {
		return testRunPlanName;
	}

	public void setTestRunPlanName(String testRunPlanName) {
		this.testRunPlanName = testRunPlanName;
	}

	public String getWorkPackageName() {
		return workPackageName;
	}

	public void setWorkPackageName(String workPackageName) {
		this.workPackageName = workPackageName;
	}

	public Integer getBuildId() {
		return buildId;
	}

	public void setBuildId(Integer buildId) {
		this.buildId = buildId;
	}

	public String getBuildName() {
		return buildName;
	}

	public void setBuildName(String buildName) {
		this.buildName = buildName;
	}

	public String getExecutionStatus() {
		return executionStatus;
	}

	public void setExecutionStatus(String executionStatus) {
		this.executionStatus = executionStatus;
	}

	public Integer getEnvironmentCombinationsCount() {
		return environmentCombinationsCount;
	}

	public void setEnvironmentCombinationsCount(Integer environmentCombinationsCount) {
		this.environmentCombinationsCount = environmentCombinationsCount;
	}

	public Integer getJobCount() {
		return jobCount;
	}

	public void setJobCount(Integer jobCount) {
		this.jobCount = jobCount;
	}

	public Integer getPlannedTestSuitesCount() {
		return plannedTestSuitesCount;
	}

	public void setPlannedTestSuitesCount(Integer plannedTestSuitesCount) {
		this.plannedTestSuitesCount = plannedTestSuitesCount;
	}

	public String getCreationType() {
		return creationType;
	}

	public void setCreationType(String creationType) {
		this.creationType = creationType;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	Integer getTestCentersId() {
		return testCentersId;
	}

	void setTestCentersId(Integer testCentersId) {
		this.testCentersId = testCentersId;
	}

	String getTestCentersName() {
		return testCentersName;
	}

	void setTestCentersName(String testCentersName) {
		this.testCentersName = testCentersName;
	}

	public Integer getIterationNumber() {
		return iterationNumber;
	}

	public void setIterationNumber(Integer iterationNumber) {
		this.iterationNumber = iterationNumber;
	}

	public Integer getLifeCyclePhaseId() {
		return lifeCyclePhaseId;
	}

	public void setLifeCyclePhaseId(Integer lifeCyclePhaseId) {
		this.lifeCyclePhaseId = lifeCyclePhaseId;
	}

	public String getLifeCyclePhaseName() {
		return lifeCyclePhaseName;
	}

	public void setLifeCyclePhaseName(String lifeCyclePhaseName) {
		this.lifeCyclePhaseName = lifeCyclePhaseName;
	}

	public String getParentStatus() {
		return parentStatus;
	}

	public void setParentStatus(String parentStatus) {
		this.parentStatus = parentStatus;
	}	
	
}
