package com.hcl.atf.taf.model.json;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hcl.atf.taf.model.dto.MetricsMasterDefectsDTO;


public class JsonMetricsMasterProgramDefects implements java.io.Serializable {
	private static final Log log = LogFactory
			.getLog(JsonMetricsMasterProgramDefects.class);
	
	private Integer testExecutionResultBugId;
	private Integer bugFilingStatus;
	private Integer productId;	
	private String productName;	
	private Integer severityId;
	
	private Integer testCaseDefectsPassedCount;
	private Integer totalTestCaseDefectsCount;
	private Integer totalTestCaseInvalidDefectsCount;
	private Integer completedTestCaseDefectsPercentage;
	private Integer variance;
	private Integer closedPercentage;
	private Float defectsQualityPercentageSpecific;
	private Float lowQualityDefetcsPercentage;
	
	private Integer normalDefectsCount;
	private Integer blockerDefectsCount;
	
	
	private Integer defectsBugfilingStatusNewCount;
	private Integer defectsBugfilingStatusreferbackCount;
	private Integer defectsBugfilingStatusreviewedCount;
	private Integer defectsBugfilingStatusapprovedCount;
	
	private Integer defectsBugfilingStatusClosedCount;
	private Integer defectsBugfilingStatusDuplicateCount;
	private Integer defectsBugfilingStatusFixedCount;
	private Integer defectsBugfilingStatusVerifiedCount;
	private Integer defectsBugfilingStatusIntendedBehaviourCount;
	private Integer defectsBugfilingStatusNotReproducibleCount;

	public JsonMetricsMasterProgramDefects() {
	}

	public JsonMetricsMasterProgramDefects(MetricsMasterDefectsDTO metricsMasterDefectsDTO) {
		this.testExecutionResultBugId = metricsMasterDefectsDTO.getTestExecutionResultBugId();
		this.bugFilingStatus = metricsMasterDefectsDTO.getBugFilingStatus();
		this.productId=metricsMasterDefectsDTO.getProductId();
		this.productName=metricsMasterDefectsDTO.getProductName();
		this.severityId = metricsMasterDefectsDTO.getSeverityId();
		this.testCaseDefectsPassedCount = metricsMasterDefectsDTO.getTestCaseDefectsPassedCount();
		this.totalTestCaseDefectsCount = metricsMasterDefectsDTO.getTotalTestCaseDefectsCount();
		this.completedTestCaseDefectsPercentage = metricsMasterDefectsDTO.getCompletedTestCaseDefectsPercentage();
		this.totalTestCaseInvalidDefectsCount=metricsMasterDefectsDTO.getTotalTestCaseInvalidDefectsCount();
	}
	
	@JsonIgnore
	public MetricsMasterDefectsDTO getMetricsMasterDefectsDTO(){
		MetricsMasterDefectsDTO metricsMasterDefectsDTO = new MetricsMasterDefectsDTO();
		if(testExecutionResultBugId != null){
			metricsMasterDefectsDTO.setTestExecutionResultBugId(testExecutionResultBugId);
		}
		metricsMasterDefectsDTO.setBugFilingStatus(bugFilingStatus);
		metricsMasterDefectsDTO.setProductId(productId);
		metricsMasterDefectsDTO.setProductName(productName);
		metricsMasterDefectsDTO.setSeverityId(severityId);
		metricsMasterDefectsDTO.setTestCaseDefectsPassedCount(testCaseDefectsPassedCount);
		metricsMasterDefectsDTO.setTotalTestCaseDefectsCount(totalTestCaseDefectsCount);
		metricsMasterDefectsDTO.setCompletedTestCaseDefectsPercentage(completedTestCaseDefectsPercentage);
		metricsMasterDefectsDTO.setTotalTestCaseInvalidDefectsCount(totalTestCaseInvalidDefectsCount);
		return metricsMasterDefectsDTO;
	}

	public Integer getTestExecutionResultBugId() {
		return testExecutionResultBugId;
	}

	public void setTestExecutionResultBugId(Integer testExecutionResultBugId) {
		this.testExecutionResultBugId = testExecutionResultBugId;
	}

	public Integer getBugFilingStatus() {
		return bugFilingStatus;
	}

	public void setBugFilingStatus(Integer bugFilingStatus) {
		this.bugFilingStatus = bugFilingStatus;
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

	public Integer getSeverityId() {
		return severityId;
	}

	public void setSeverityId(Integer severityId) {
		this.severityId = severityId;
	}

	public Integer getTestCaseDefectsPassedCount() {
		return testCaseDefectsPassedCount;
	}

	public void setTestCaseDefectsPassedCount(Integer testCaseDefectsPassedCount) {
		this.testCaseDefectsPassedCount = testCaseDefectsPassedCount;
	}

	public Integer getTotalTestCaseDefectsCount() {
		return totalTestCaseDefectsCount;
	}

	public void setTotalTestCaseDefectsCount(Integer totalTestCaseDefectsCount) {
		this.totalTestCaseDefectsCount = totalTestCaseDefectsCount;
	}

	public Integer getCompletedTestCaseDefectsPercentage() {
		return completedTestCaseDefectsPercentage;
	}

	public void setCompletedTestCaseDefectsPercentage(
			Integer completedTestCaseDefectsPercentage) {
		this.completedTestCaseDefectsPercentage = completedTestCaseDefectsPercentage;
	}

	public Integer getTotalTestCaseInvalidDefectsCount() {
		return totalTestCaseInvalidDefectsCount;
	}

	public void setTotalTestCaseInvalidDefectsCount(
			Integer totalTestCaseInvalidDefectsCount) {
		this.totalTestCaseInvalidDefectsCount = totalTestCaseInvalidDefectsCount;
	}

	public Integer getDefectsBugfilingStatusNewCount() {
		return defectsBugfilingStatusNewCount;
	}

	public void setDefectsBugfilingStatusNewCount(
			Integer defectsBugfilingStatusNewCount) {
		this.defectsBugfilingStatusNewCount = defectsBugfilingStatusNewCount;
	}

	public Integer getDefectsBugfilingStatusreferbackCount() {
		return defectsBugfilingStatusreferbackCount;
	}

	public void setDefectsBugfilingStatusreferbackCount(
			Integer defectsBugfilingStatusreferbackCount) {
		this.defectsBugfilingStatusreferbackCount = defectsBugfilingStatusreferbackCount;
	}

	public Integer getDefectsBugfilingStatusreviewedCount() {
		return defectsBugfilingStatusreviewedCount;
	}

	public void setDefectsBugfilingStatusreviewedCount(
			Integer defectsBugfilingStatusreviewedCount) {
		this.defectsBugfilingStatusreviewedCount = defectsBugfilingStatusreviewedCount;
	}

	public Integer getDefectsBugfilingStatusapprovedCount() {
		return defectsBugfilingStatusapprovedCount;
	}

	public void setDefectsBugfilingStatusapprovedCount(
			Integer defectsBugfilingStatusapprovedCount) {
		this.defectsBugfilingStatusapprovedCount = defectsBugfilingStatusapprovedCount;
	}

	public Integer getDefectsBugfilingStatusClosedCount() {
		return defectsBugfilingStatusClosedCount;
	}

	public void setDefectsBugfilingStatusClosedCount(
			Integer defectsBugfilingStatusClosedCount) {
		this.defectsBugfilingStatusClosedCount = defectsBugfilingStatusClosedCount;
	}

	public Integer getDefectsBugfilingStatusDuplicateCount() {
		return defectsBugfilingStatusDuplicateCount;
	}

	public void setDefectsBugfilingStatusDuplicateCount(
			Integer defectsBugfilingStatusDuplicateCount) {
		this.defectsBugfilingStatusDuplicateCount = defectsBugfilingStatusDuplicateCount;
	}

	public Integer getDefectsBugfilingStatusFixedCount() {
		return defectsBugfilingStatusFixedCount;
	}

	public void setDefectsBugfilingStatusFixedCount(
			Integer defectsBugfilingStatusFixedCount) {
		this.defectsBugfilingStatusFixedCount = defectsBugfilingStatusFixedCount;
	}

	public Integer getDefectsBugfilingStatusVerifiedCount() {
		return defectsBugfilingStatusVerifiedCount;
	}

	public void setDefectsBugfilingStatusVerifiedCount(
			Integer defectsBugfilingStatusVerifiedCount) {
		this.defectsBugfilingStatusVerifiedCount = defectsBugfilingStatusVerifiedCount;
	}

	public Integer getDefectsBugfilingStatusIntendedBehaviourCount() {
		return defectsBugfilingStatusIntendedBehaviourCount;
	}

	public void setDefectsBugfilingStatusIntendedBehaviourCount(
			Integer defectsBugfilingStatusIntendedBehaviourCount) {
		this.defectsBugfilingStatusIntendedBehaviourCount = defectsBugfilingStatusIntendedBehaviourCount;
	}

	public Integer getDefectsBugfilingStatusNotReproducibleCount() {
		return defectsBugfilingStatusNotReproducibleCount;
	}

	public void setDefectsBugfilingStatusNotReproducibleCount(
			Integer defectsBugfilingStatusNotReproducibleCount) {
		this.defectsBugfilingStatusNotReproducibleCount = defectsBugfilingStatusNotReproducibleCount;
	}

	public Integer getVariance() {
		return variance;
	}

	public void setVariance(Integer variance) {
		this.variance = variance;
	}

	public Integer getClosedPercentage() {
		return closedPercentage;
	}

	public void setClosedPercentage(Integer closedPercentage) {
		this.closedPercentage = closedPercentage;
	}

	public Integer getNormalDefectsCount() {
		return normalDefectsCount;
	}

	public void setNormalDefectsCount(Integer normalDefectsCount) {
		this.normalDefectsCount = normalDefectsCount;
	}

	public Integer getBlockerDefectsCount() {
		return blockerDefectsCount;
	}

	public void setBlockerDefectsCount(Integer blockerDefectsCount) {
		this.blockerDefectsCount = blockerDefectsCount;
	}

	public Float getDefectsQualityPercentageSpecific() {
		return defectsQualityPercentageSpecific;
	}

	public void setDefectsQualityPercentageSpecific(
			Float defectsQualityPercentageSpecific) {
		this.defectsQualityPercentageSpecific = defectsQualityPercentageSpecific;
	}

	public Float getLowQualityDefetcsPercentage() {
		return lowQualityDefetcsPercentage;
	}

	public void setLowQualityDefetcsPercentage(Float lowQualityDefetcsPercentage) {
		this.lowQualityDefetcsPercentage = lowQualityDefetcsPercentage;
	}

	
}
	

