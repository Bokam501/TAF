package com.hcl.atf.taf.model.json;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.hcl.atf.taf.model.dto.MetricsMasterDTO;


public class JsonMetricsProgramExecutionProcess implements java.io.Serializable {
	private static final Log log = LogFactory
			.getLog(JsonMetricsProgramExecutionProcess.class);
	
	private Integer workPackageId;
	private String workPackageName;
	private Integer userId;
	private String userName;
	private Integer productId;	
	private String productName;	
	private Integer testCaseAllocatedCount;
	private Integer testCaseCompletedCount;
	private Integer testCaseNotStartedCount;
	private Float completedTestCasePercentage;
	private Integer executionStatus;
	private Integer totalPlannedTestcaseCount;
	
	
	@JsonProperty
	private Integer variancePercentage;
	
	

	public JsonMetricsProgramExecutionProcess() {
	}

	public JsonMetricsProgramExecutionProcess(MetricsMasterDTO metricsMasterDTO) {
		this.workPackageId = metricsMasterDTO.getWorkPackageId();
		this.workPackageName = metricsMasterDTO.getWorkPackageName();
		this.productId=metricsMasterDTO.getProductId();
		this.productName=metricsMasterDTO.getProductName();
		this.testCaseAllocatedCount = metricsMasterDTO.getTestCaseAllocatedCount();
		this.testCaseCompletedCount = metricsMasterDTO.getTestCaseCompletedCount();
		this.testCaseNotStartedCount = metricsMasterDTO.getTestCaseNotStartedCount();
		this.executionStatus = metricsMasterDTO.getExecutionStatus();
		
		
	}
	
	@JsonIgnore
	public MetricsMasterDTO getMetricsMasterDTO(){
		MetricsMasterDTO metricsMasterDTO = new MetricsMasterDTO();
		if(workPackageId != null){
			metricsMasterDTO.setWorkPackageId(workPackageId);
		}
		
		metricsMasterDTO.setWorkPackageName(workPackageName);
		metricsMasterDTO.setExecutionStatus(executionStatus);
		metricsMasterDTO.setTestCaseAllocatedCount(testCaseAllocatedCount);
		metricsMasterDTO.setTestCaseCompletedCount(testCaseCompletedCount);
		metricsMasterDTO.setTestCaseNotStartedCount(testCaseNotStartedCount);
		
		return metricsMasterDTO;
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

	public Integer getTestCaseAllocatedCount() {
		return testCaseAllocatedCount;
	}

	public void setTestCaseAllocatedCount(Integer testCaseAllocatedCount) {
		this.testCaseAllocatedCount = testCaseAllocatedCount;
	}

	public Integer getTestCaseCompletedCount() {
		return testCaseCompletedCount;
	}

	public void setTestCaseCompletedCount(Integer testCaseCompletedCount) {
		this.testCaseCompletedCount = testCaseCompletedCount;
	}

	public Integer getTestCaseNotStartedCount() {
		return testCaseNotStartedCount;
	}

	public void setTestCaseNotStartedCount(Integer testCaseNotStartedCount) {
		this.testCaseNotStartedCount = testCaseNotStartedCount;
	}

	public Float getCompletedTestCasePercentage() {
		return completedTestCasePercentage;
	}

	public void setCompletedTestCasePercentage(Float completedTestCasePercentage) {
		this.completedTestCasePercentage = completedTestCasePercentage;
	}

	public Integer getExecutionStatus() {
		return executionStatus;
	}

	public void setExecutionStatus(Integer executionStatus) {
		this.executionStatus = executionStatus;
	}

	public Integer getVariancePercentage() {
		return variancePercentage;
	}

	public void setVariancePercentage(Integer poorProgamExecutionPercentageForIndividual) {
		this.variancePercentage = poorProgamExecutionPercentageForIndividual;
	}

	public Integer getTotalPlannedTestcaseCount() {
		return totalPlannedTestcaseCount;
	}

	public void setTotalPlannedTestcaseCount(Integer totalPlannedTestcaseCount) {
		this.totalPlannedTestcaseCount = totalPlannedTestcaseCount;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	
}
	

