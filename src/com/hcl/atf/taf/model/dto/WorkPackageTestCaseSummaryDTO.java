package com.hcl.atf.taf.model.dto;

import java.util.Date;
import java.util.HashMap;
import java.util.Set;

import com.hcl.atf.taf.model.Environment;
import com.hcl.atf.taf.model.EnvironmentCombination;
import com.hcl.atf.taf.model.RunConfiguration;

public class WorkPackageTestCaseSummaryDTO {
	
	private Integer WorkPackageTestCaseSummaryId;
	private Integer workPackageId;
	private String workPackageName;
	private String workPackageType;
	private Integer productId;	
	private String productName;		
	private Integer pBuildId;	
	private String pBuildName;	
	private String description;	
	private Integer status;	
	private Date plannedStartDate;	
	private Date plannedEndDate;	
	private Date actualStartDate;	
	private Date actualEndDate;	
	private Date actualExecutionDate;	
	private Integer testCasesCount;
	private Integer defectsCount;
	private Integer approvedTestCaseCount;
	private Integer rejectedTestCaseCount;
	private Integer totalTestCaseForExecutionCount;
	private Integer completedTestCaseCount;
	private Integer completedTestCasePercentage;	
	private Integer notCompletedTestCaseCount;	
	private Integer selectedTestCasesCount;	
	private Integer selectedTestSuitesCount;	
	private Integer selectedFeaturesCount;	
	private Integer totalTestCaseCount;
	private Integer plannedExecutionDateCount;
	private String workFlowstageName;
	private HashMap workFlowstageNameList;
	private Set<Environment> environmentList;
	private Integer ApprovedDefectsCount;
	private Integer totalDefectsCount;
	private String ShiftName;
	private Integer workpackageStatus;
	private Set<EnvironmentCombination> environmentCombination;
	private Set<RunConfiguration> runConfigurations;
	private String productType;
	private Integer iterationNo;
	private Integer lifecyclePhaseId;
	private HashMap lifecyclePhaseList;
	private String wpcreatedUser;
	private Integer jobsCount;
	private Integer testRunJobsCompleted;
	public Integer getWorkPackageTestCaseSummaryId() {
		return WorkPackageTestCaseSummaryId;
	}
	public void setWorkPackageTestCaseSummaryId(Integer workPackageTestCaseSummaryId) {
		WorkPackageTestCaseSummaryId = workPackageTestCaseSummaryId;
	}
	public Integer getWorkPackageId() {
		return workPackageId;
	}
	public void setWorkPackageId(Integer workPackageId) {
		this.workPackageId = workPackageId;
	}
	public String getWorkPackageName() {
		return workPackageName;
	}
	public void setWorkPackageName(String workPackageName) {
		this.workPackageName = workPackageName;
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
	public Integer getpBuildId() {
		return pBuildId;
	}
	public void setpBuildId(Integer pBuildId) {
		this.pBuildId = pBuildId;
	}
	public String getpBuildName() {
		return pBuildName;
	}
	public void setpBuildName(String pBuildName) {
		this.pBuildName = pBuildName;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
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
	public Date getActualExecutionDate() {
		return actualExecutionDate;
	}
	public void setActualExecutionDate(Date actualExecutionDate) {
		this.actualExecutionDate = actualExecutionDate;
	}
	public Integer getTestCasesCount() {
		return testCasesCount;
	}
	public void setTestCasesCount(Integer testCasesCount) {
		this.testCasesCount = testCasesCount;
	}
	public Integer getDefectsCount() {
		return defectsCount;
	}
	public void setDefectsCount(Integer defectsCount) {
		this.defectsCount = defectsCount;
	}
	public Integer getApprovedTestCaseCount() {
		return approvedTestCaseCount;
	}
	public void setApprovedTestCaseCount(Integer approvedTestCaseCount) {
		this.approvedTestCaseCount = approvedTestCaseCount;
	}
	public Integer getRejectedTestCaseCount() {
		return rejectedTestCaseCount;
	}
	public void setRejectedTestCaseCount(Integer rejectedTestCaseCount) {
		this.rejectedTestCaseCount = rejectedTestCaseCount;
	}
	public Integer getTotalTestCaseForExecutionCount() {
		return totalTestCaseForExecutionCount;
	}
	public void setTotalTestCaseForExecutionCount(
			Integer totalTestCaseForExecutionCount) {
		this.totalTestCaseForExecutionCount = totalTestCaseForExecutionCount;
	}
	public Integer getCompletedTestCaseCount() {
		return completedTestCaseCount;
	}
	public void setCompletedTestCaseCount(Integer completedTestCaseCount) {
		this.completedTestCaseCount = completedTestCaseCount;
	}
	public Integer getCompletedTestCasePercentage() {
		return completedTestCasePercentage;
	}
	public void setCompletedTestCasePercentage(Integer completedTestCasePercentage) {
		this.completedTestCasePercentage = completedTestCasePercentage;
	}
	public Integer getNotCompletedTestCaseCount() {
		return notCompletedTestCaseCount;
	}
	public void setNotCompletedTestCaseCount(Integer notCompletedTestCaseCount) {
		this.notCompletedTestCaseCount = notCompletedTestCaseCount;
	}
	public Integer getSelectedTestCasesCount() {
		return selectedTestCasesCount;
	}
	public void setSelectedTestCasesCount(Integer selectedTestCasesCount) {
		this.selectedTestCasesCount = selectedTestCasesCount;
	}
	public Integer getTotalTestCaseCount() {
		return totalTestCaseCount;
	}
	public void setTotalTestCaseCount(Integer totalTestCaseCount) {
		this.totalTestCaseCount = totalTestCaseCount;
	}
	public Integer getPlannedExecutionDateCount() {
		return plannedExecutionDateCount;
	}
	public void setPlannedExecutionDateCount(Integer plannedExecutionDateCount) {
		this.plannedExecutionDateCount = plannedExecutionDateCount;
	}
	public String getWorkFlowstageName() {
		return workFlowstageName;
	}
	public void setWorkFlowstageName(String workFlowstageName) {
		this.workFlowstageName = workFlowstageName;
	}
	public HashMap getWorkFlowstageNameList() {
		return workFlowstageNameList;
	}
	public void setWorkFlowstageNameList(HashMap workFlowstageNameList) {
		this.workFlowstageNameList = workFlowstageNameList;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public Set<Environment> getEnvironmentList() {
		return environmentList;
	}
	public void setEnvironmentList(Set<Environment> environmentList) {
		this.environmentList = environmentList;
	}
	public Integer getApprovedDefectsCount() {
		return ApprovedDefectsCount;
	}
	public void setApprovedDefectsCount(Integer approvedDefectsCount) {
		ApprovedDefectsCount = approvedDefectsCount;
	}
	public Integer getTotalDefectsCount() {
		return totalDefectsCount;
	}
	public void setTotalDefectsCount(Integer totalDefectsCount) {
		this.totalDefectsCount = totalDefectsCount;
	}
	public String getShiftName() {
		return ShiftName;
	}
	public void setShiftName(String shiftName) {
		ShiftName = shiftName;
	}
	public Integer getWorkpackageStatus() {
		return workpackageStatus;
	}
	public void setWorkpackageStatus(Integer workpackageStatus) {
		this.workpackageStatus = workpackageStatus;
	}
	public Set<EnvironmentCombination> getEnvironmentCombination() {
		return environmentCombination;
	}
	public void setEnvironmentCombination(
			Set<EnvironmentCombination> environmentCombination) {
		this.environmentCombination = environmentCombination;
	}
	public Set<RunConfiguration> getRunConfigurations() {
		return runConfigurations;
	}
	public void setRunConfigurations(Set<RunConfiguration> runConfigurations) {
		this.runConfigurations = runConfigurations;
	}
	
	public String getWorkPackageType() {
		return workPackageType;
	}
	
	public void setWorkPackageType(String workPackageType) {
		this.workPackageType=workPackageType;
		
	}
	public Integer getSelectedTestSuitesCount() {
		return selectedTestSuitesCount;
	}
	public void setSelectedTestSuitesCount(Integer selectedTestSuitesCount) {
		this.selectedTestSuitesCount = selectedTestSuitesCount;
	}
	public Integer getSelectedFeaturesCount() {
		return selectedFeaturesCount;
	}
	public void setSelectedFeaturesCount(Integer selectedFeaturesCount) {
		this.selectedFeaturesCount = selectedFeaturesCount;
	}
	public String getProductType() {
		return productType;
	}
	public void setProductType(String productType) {
		this.productType = productType;
	}
	public Integer getIterationNo() {
		return iterationNo;
	}
	public void setIterationNo(Integer iterationNo) {
		this.iterationNo = iterationNo;
	}
	public Integer getLifecyclePhaseId() {
		return lifecyclePhaseId;
	}
	public void setLifecyclePhaseId(Integer lifecyclePhaseId) {
		this.lifecyclePhaseId = lifecyclePhaseId;
	}
	public HashMap getLifecyclePhaseList() {
		return lifecyclePhaseList;
	}
	public void setLifecyclePhaseList(HashMap lifecyclePhaseList) {
		this.lifecyclePhaseList = lifecyclePhaseList;
	}
	public String getWpcreatedUser() {
		return wpcreatedUser;
	}
	public void setWpcreatedUser(String wpcreatedUser) {
		this.wpcreatedUser = wpcreatedUser;
	}
	public Integer getJobsCount() {
		return jobsCount;
	}
	public void setJobsCount(Integer jobsCount) {
		this.jobsCount = jobsCount;
	}
	public Integer getTestRunJobsCompleted() {
		return testRunJobsCompleted;
	}
	public void setTestRunJobsCompleted(Integer testRunJobsCompleted) {
		this.testRunJobsCompleted = testRunJobsCompleted;
	}
	

}
