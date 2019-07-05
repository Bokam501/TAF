package com.hcl.atf.taf.model.json;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hcl.atf.taf.model.dto.MetricsMasterTestCaseResultDTO;


public class JsonMetricsTestCaseResult implements java.io.Serializable {
	private static final Log log = LogFactory
			.getLog(JsonMetricsTestCaseResult.class);
	
	private Integer testCaseExecutionResultId;
	private String result;
	
	private Integer productId;	
	private String productName;	
	private Integer workPackageId;
	private String workPackageName;
	
	
	private Integer testCasePassedCount;
	private Integer testCaseFailedCount;
	private Integer testCaseBlockedCount;
	private Integer testCaseNorunCount;
	private Integer totalTestCaseCount;	
	private Float productConfidenceQuality;
	
	
	
	

	public JsonMetricsTestCaseResult() {
	}

	public JsonMetricsTestCaseResult(MetricsMasterTestCaseResultDTO metricsMasterTestCaseResultDTO) {
		this.workPackageId = metricsMasterTestCaseResultDTO.getWorkPackageId();
		this.workPackageName = metricsMasterTestCaseResultDTO.getWorkPackageName();
		this.productId=metricsMasterTestCaseResultDTO.getProductId();
		this.productName=metricsMasterTestCaseResultDTO.getProductName();
		this.testCaseExecutionResultId=metricsMasterTestCaseResultDTO.getTestCaseExecutionResultId();
		this.result=metricsMasterTestCaseResultDTO.getResult();
		this.testCasePassedCount=metricsMasterTestCaseResultDTO.getTestCasePassedCount();
		this.testCaseFailedCount=metricsMasterTestCaseResultDTO.getTestCaseFailedCount();
		this.testCaseBlockedCount=metricsMasterTestCaseResultDTO.getTestCaseBlockedCount();
		this.testCaseNorunCount=metricsMasterTestCaseResultDTO.getTestCaseNorunCount();
		
		
		
	}
	
	@JsonIgnore
	public MetricsMasterTestCaseResultDTO getMetricsMasterTestCaseResultDTO(){
		MetricsMasterTestCaseResultDTO metricsMasterTestCaseResultDTO = new MetricsMasterTestCaseResultDTO();
		if(workPackageId != null){
			metricsMasterTestCaseResultDTO.setWorkPackageId(workPackageId);
		}
		
		metricsMasterTestCaseResultDTO.setWorkPackageName(workPackageName);
		metricsMasterTestCaseResultDTO.setProductId(productId);
		metricsMasterTestCaseResultDTO.setProductName(productName);
		metricsMasterTestCaseResultDTO.setTestCaseExecutionResultId(testCaseExecutionResultId);
		
		metricsMasterTestCaseResultDTO.setResult(result);
		metricsMasterTestCaseResultDTO.setTestCasePassedCount(testCasePassedCount);
		metricsMasterTestCaseResultDTO.setTestCaseFailedCount(testCaseFailedCount);
		metricsMasterTestCaseResultDTO.setTestCaseBlockedCount(testCaseBlockedCount);
		metricsMasterTestCaseResultDTO.setTestCaseNorunCount(testCaseNorunCount);
		
		return metricsMasterTestCaseResultDTO;
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

	public Integer getTestCasePassedCount() {
		return testCasePassedCount;
	}

	public void setTestCasePassedCount(Integer testCasePassedCount) {
		this.testCasePassedCount = testCasePassedCount;
	}

	public Integer getTestCaseFailedCount() {
		return testCaseFailedCount;
	}

	public void setTestCaseFailedCount(Integer testCaseFailedCount) {
		this.testCaseFailedCount = testCaseFailedCount;
	}

	public Integer getTestCaseBlockedCount() {
		return testCaseBlockedCount;
	}

	public void setTestCaseBlockedCount(Integer testCaseBlockedCount) {
		this.testCaseBlockedCount = testCaseBlockedCount;
	}

	public Integer getTestCaseNorunCount() {
		return testCaseNorunCount;
	}

	public void setTestCaseNorunCount(Integer testCaseNorunCount) {
		this.testCaseNorunCount = testCaseNorunCount;
	}

	public Integer getTotalTestCaseCount() {
		return totalTestCaseCount;
	}

	public void setTotalTestCasePassedCount(Integer totalTestCaseCount) {
		this.totalTestCaseCount = totalTestCaseCount;
	}

	public Float getProductConfidenceQuality() {
		return productConfidenceQuality;
	}

	public void setProductConfidenceQuality(Float productConfidenceQuality) {
		this.productConfidenceQuality = productConfidenceQuality;
	}

	public void setTotalTestCaseCount(Integer totalTestCaseCount) {
		this.totalTestCaseCount = totalTestCaseCount;
	}

	
	
}
	

