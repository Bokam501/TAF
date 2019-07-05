package com.hcl.atf.taf.model.json;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.hcl.atf.taf.model.dto.WorkPackageTestCaseExecutionPlanStatusDTO;


public class JsonWorkPackageTestCaseExecutionPlanStatus implements java.io.Serializable{
	
	private static final Log log = LogFactory.getLog(JsonWorkPackageTestCaseExecutionPlanStatus.class);
	
	@JsonProperty	
	private Integer workPackageStatusSummaryId;
	@JsonProperty
	private Integer totalTestCaseCount;
	@JsonProperty
	private Integer assignedTestLeadCount;
	@JsonProperty
	private String assignedTestLeadCountwithPercentage;
	@JsonProperty
	private Integer assignedTestLeadPercentage;
	@JsonProperty
	private Integer assignedTesterCount;
	@JsonProperty
	private String assignedTesterCountwithPercentage;
	@JsonProperty
	private Integer assignedTesterCountPercentage;
	@JsonProperty
	private Integer plannedExecutionDateCount;
	@JsonProperty
	private String plannedExecutionDateCountwithPercentage;
	@JsonProperty
	private Integer plannedExecutionDateCountPercentage;
	@JsonProperty
	private Integer workPackageId;
	@JsonProperty
	private String workPackageName;

	@JsonProperty
	private Integer totalTestSuiteCount;

	@JsonProperty
	private Integer totalFeatureCount;
	
	@JsonProperty
	private Integer activeTestCaseCount;
	
	@JsonProperty
	private Integer inActiveTestCaseCount;
	
	public JsonWorkPackageTestCaseExecutionPlanStatus(WorkPackageTestCaseExecutionPlanStatusDTO workPackageTestCaseExecutionPlanStatusDTO){
		this.workPackageStatusSummaryId = workPackageTestCaseExecutionPlanStatusDTO.getWorkPackageStatusSummaryId();
		this.activeTestCaseCount=workPackageTestCaseExecutionPlanStatusDTO.getActiveTestCaseCount();
		this.inActiveTestCaseCount=workPackageTestCaseExecutionPlanStatusDTO.getInActiveTestCaseCount();
		this.totalTestCaseCount=workPackageTestCaseExecutionPlanStatusDTO.getTotalTestCaseCount();
		this.assignedTesterCount=workPackageTestCaseExecutionPlanStatusDTO.getAssignedTesterCount();
		this.assignedTestLeadCount=workPackageTestCaseExecutionPlanStatusDTO.getAssignedTestLeadCount();
		this.plannedExecutionDateCount=workPackageTestCaseExecutionPlanStatusDTO.getPlannedExecutionDateCount();
		if(activeTestCaseCount !=0 ){
			this.assignedTesterCountPercentage= Math.round((assignedTesterCount*100)/activeTestCaseCount); 
			this.assignedTesterCountwithPercentage=assignedTesterCount+" ("+assignedTesterCountPercentage+" %)";
			this.assignedTestLeadPercentage= Math.round((assignedTestLeadCount*100)/activeTestCaseCount);
			this.assignedTestLeadCountwithPercentage=assignedTestLeadCount+" (" +assignedTestLeadPercentage+" %)";
			this.plannedExecutionDateCountPercentage= Math.round((plannedExecutionDateCount*100)/activeTestCaseCount);
			this.plannedExecutionDateCountwithPercentage=plannedExecutionDateCount+" (" +plannedExecutionDateCountPercentage+" %)";
		}else{
			this.assignedTesterCountwithPercentage="0";
			this.assignedTestLeadCountwithPercentage="0";
			this.plannedExecutionDateCountwithPercentage="0";
		}
		
		this.workPackageId=workPackageTestCaseExecutionPlanStatusDTO.getWorkPackageId();
		this.workPackageName=workPackageTestCaseExecutionPlanStatusDTO.getWorkPackageName();
		this.totalFeatureCount=workPackageTestCaseExecutionPlanStatusDTO.getTotalFeatureCount();
		this.totalTestSuiteCount=workPackageTestCaseExecutionPlanStatusDTO.getTotalTestSuiteCount();
	}


	public Integer getWorkPackageStatusSummaryId() {
		return workPackageStatusSummaryId;
	}


	public void setWorkPackageStatusSummaryId(Integer workPackageStatusSummaryId) {
		this.workPackageStatusSummaryId = workPackageStatusSummaryId;
	}


	public Integer getTotalTestCaseCount() {
		return totalTestCaseCount;
	}


	public void setTotalTestCaseCount(Integer totalTestCaseCount) {
		this.totalTestCaseCount = totalTestCaseCount;
	}


	public Integer getAssignedTestLeadCount() {
		return assignedTestLeadCount;
	}


	public void setAssignedTestLeadCount(Integer assignedTestLeadCount) {
		this.assignedTestLeadCount = assignedTestLeadCount;
	}


	public Integer getAssignedTesterCount() {
		return assignedTesterCount;
	}


	public void setAssignedTesterCount(Integer assignedTesterCount) {
		this.assignedTesterCount = assignedTesterCount;
	}


	public Integer getPlannedExecutionDateCount() {
		return plannedExecutionDateCount;
	}


	public void setPlannedExecutionDateCount(Integer plannedExecutionDateCount) {
		this.plannedExecutionDateCount = plannedExecutionDateCount;
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


	public String getAssignedTestLeadCountwithPercentage() {
		return assignedTestLeadCountwithPercentage;
	}


	public void setAssignedTestLeadCountwithPercentage(
			String assignedTestLeadCountwithPercentage) {
		this.assignedTestLeadCountwithPercentage = assignedTestLeadCountwithPercentage;
	}


	public Integer getAssignedTestLeadPercentage() {
		return assignedTestLeadPercentage;
	}


	public void setAssignedTestLeadPercentage(Integer assignedTestLeadPercentage) {
		this.assignedTestLeadPercentage = assignedTestLeadPercentage;
	}


	public String getAssignedTesterCountwithPercentage() {
		return assignedTesterCountwithPercentage;
	}


	public void setAssignedTesterCountwithPercentage(
			String assignedTesterCountwithPercentage) {
		this.assignedTesterCountwithPercentage = assignedTesterCountwithPercentage;
	}


	public Integer getAssignedTesterCountPercentage() {
		return assignedTesterCountPercentage;
	}


	public void setAssignedTesterCountPercentage(
			Integer assignedTesterCountPercentage) {
		this.assignedTesterCountPercentage = assignedTesterCountPercentage;
	}


	public String getPlannedExecutionDateCountwithPercentage() {
		return plannedExecutionDateCountwithPercentage;
	}


	public void setPlannedExecutionDateCountwithPercentage(
			String plannedExecutionDateCountwithPercentage) {
		this.plannedExecutionDateCountwithPercentage = plannedExecutionDateCountwithPercentage;
	}


	public Integer getPlannedExecutionDateCountPercentage() {
		return plannedExecutionDateCountPercentage;
	}


	public void setPlannedExecutionDateCountPercentage(Integer plannedExecutionDateCountPercentage) {
		this.plannedExecutionDateCountPercentage = plannedExecutionDateCountPercentage;
	}


	public Integer getTotalTestSuiteCount() {
		return totalTestSuiteCount;
	}


	public void setTotalTestSuiteCount(Integer totalTestSuiteCount) {
		this.totalTestSuiteCount = totalTestSuiteCount;
	}


	public Integer getTotalFeatureCount() {
		return totalFeatureCount;
	}


	public void setTotalFeatureCount(Integer totalFeatureCount) {
		this.totalFeatureCount = totalFeatureCount;
	}


	public Integer getActiveTestCaseCount() {
		return activeTestCaseCount;
	}


	public void setActiveTestCaseCount(Integer activeTestCaseCount) {
		this.activeTestCaseCount = activeTestCaseCount;
	}


	public Integer getInActiveTestCaseCount() {
		return inActiveTestCaseCount;
	}


	public void setInActiveTestCaseCount(Integer inActiveTestCaseCount) {
		this.inActiveTestCaseCount = inActiveTestCaseCount;
	}
	

}
