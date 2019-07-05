package com.hcl.atf.taf.model.json;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.fasterxml.jackson.annotation.JsonProperty;


public class JsonWorkPackageStatusSummary implements java.io.Serializable{
	
	private static final Log log = LogFactory.getLog(JsonWorkPackageStatusSummary.class);
	
	@JsonProperty	
	private Integer workPackageStatusSummaryId;
	@JsonProperty
	private Integer selectedEnvironmentsCount;
	@JsonProperty
	private Integer selectedLocalesCount;
	@JsonProperty
	private Integer selectedTestCasesCount;
	@JsonProperty
	private Integer totalTestCaseForExecutionCount;
	@JsonProperty
	private Integer completedTestCaseCount;
	@JsonProperty
	private Integer notCompletedTestCaseCount;
	@JsonProperty
	private Integer workPackageId;
	@JsonProperty
	private String workPackageName;
	
	
	public JsonWorkPackageStatusSummary(){
		selectedEnvironmentsCount = 0;
		selectedLocalesCount = 0;
		selectedTestCasesCount = 0;
		totalTestCaseForExecutionCount = 0;
		completedTestCaseCount = 0;
		notCompletedTestCaseCount = 0;
	}


	public Integer getWorkPackageStatusSummaryId() {
		return workPackageStatusSummaryId;
	}


	public void setWorkPackageStatusSummaryId(Integer workPackageStatusSummaryId) {
		this.workPackageStatusSummaryId = workPackageStatusSummaryId;
	}


	public Integer getSelectedEnvironmentsCount() {
		return selectedEnvironmentsCount;
	}


	public void setSelectedEnvironmentsCount(Integer selectedEnvironmentsCount) {
		this.selectedEnvironmentsCount = selectedEnvironmentsCount;
	}


	public Integer getSelectedLocalesCount() {
		return selectedLocalesCount;
	}


	public void setSelectedLocalesCount(Integer selectedLocalesCount) {
		this.selectedLocalesCount = selectedLocalesCount;
	}


	public Integer getSelectedTestCasesCount() {
		return selectedTestCasesCount;
	}


	public void setSelectedTestCasesCount(Integer selectedTestCasesCount) {
		this.selectedTestCasesCount = selectedTestCasesCount;
	}


	public Integer getCompletedTestCaseCount() {
		return completedTestCaseCount;
	}


	public void setCompletedTestCaseCount(Integer completedTestCaseCount) {
		this.completedTestCaseCount = completedTestCaseCount;
	}


	public Integer getNotCompletedTestCaseCount() {
		return notCompletedTestCaseCount;
	}


	public void setNotCompletedTestCaseCount(Integer notCompletedTestCaseCount) {
		this.notCompletedTestCaseCount = notCompletedTestCaseCount;
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


	public Integer getTotalTestCaseForExecutionCount() {
		return totalTestCaseForExecutionCount;
	}


	public void setTotalTestCaseForExecutionCount(
			Integer totalTestCaseForExecutionCount) {
		this.totalTestCaseForExecutionCount = totalTestCaseForExecutionCount;
	}

}
