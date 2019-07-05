package com.hcl.atf.taf.model.json;

import java.util.HashMap;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.hcl.atf.taf.model.MetricsMaster;


public class JsonMetricsMaster implements java.io.Serializable {
	private static final Log log = LogFactory
			.getLog(JsonMetricsMaster.class);
	
	@JsonProperty
	private Integer metricsId;
	@JsonProperty
	private String metricsName; 
	@JsonProperty
	private String metricsType; 
	@JsonProperty
	private Integer target; 
	@JsonProperty
	private String period;
	@JsonProperty
	private String startsFrom;
	@JsonProperty
	private String indicator;
	@JsonProperty
	private String actualValue;
	@JsonProperty
	private Integer poorProgramExecutionIndexCount;
	@JsonProperty
	private boolean isTargetAvailable;
	@JsonProperty
	private String targetRange;
	@JsonProperty
	private List<HashMap<String, String>> trendMetrics;

	public JsonMetricsMaster() {
	}

	public JsonMetricsMaster(MetricsMaster metricsMaster) {
		this.metricsId = metricsMaster.getMetricsId();
		this.metricsName = metricsMaster.getMetricsName();
		this.target = metricsMaster.getTarget();
		this.period = metricsMaster.getPeriod();
		this.startsFrom = metricsMaster.getStartsFrom();
		this.poorProgramExecutionIndexCount = 0;
		
		
	}
	
	@JsonIgnore
	public MetricsMaster getMetricsMaster(){
		MetricsMaster metricsMaster = new MetricsMaster();
		if(metricsId != null){
			metricsMaster.setMetricsId(metricsId);
		}
		
		metricsMaster.setMetricsName(metricsName);
		metricsMaster.setTarget(target);
		metricsMaster.setPeriod(period);
		metricsMaster.setStartsFrom(startsFrom);
		return metricsMaster;
	}

	public Integer getMetricsId() {
		return metricsId;
	}

	public void setMetricsId(Integer metricsId) {
		this.metricsId = metricsId;
	}

	public String getMetricsName() {
		return metricsName;
	}

	public void setMetricsName(String metricsName) {
		this.metricsName = metricsName;
	}

	public Integer getTarget() {
		return target;
	}

	public void setTarget(Integer target) {
		this.target = target;
	}

	public String getPeriod() {
		return period;
	}

	public void setPeriod(String period) {
		this.period = period;
	}

	public String getStartsFrom() {
		return startsFrom;
	}

	public void setStartsFrom(String startsFrom) {
		this.startsFrom = startsFrom;
	}

	public String getActualValue() {
		return actualValue;
	}

	public void setActualValue(String actualValue) {
		this.actualValue = actualValue;
	}

	public Integer getPoorProgramExecutionIndexCount() {
		return poorProgramExecutionIndexCount;
	}

	public void setPoorProgramExecutionIndexCount(
			Integer poorProgramExecutionIndexCount) {
		this.poorProgramExecutionIndexCount = poorProgramExecutionIndexCount;
	}

	public String getIndicator() {
		return indicator;
	}

	public void setIndicator(String indicator) {
		this.indicator = indicator;
	}

	public boolean isTargetAvailable() {
		return isTargetAvailable;
	}

	public void setTargetAvailable(boolean isTargetAvailable) {
		this.isTargetAvailable = isTargetAvailable;
	}

	public String getTargetRange() {
		return targetRange;
	}

	public void setTargetRange(String targetRange) {
		this.targetRange = targetRange;
	}

	public String getMetricsType() {
		return metricsType;
	}

	public void setMetricsType(String metricsType) {
		this.metricsType = metricsType;
	}

	public List<HashMap<String, String>> getTrendMetrics() {
		return trendMetrics;
	}

	public void setTrendMetrics(List<HashMap<String, String>> trendMetrics) {
		this.trendMetrics = trendMetrics;
	}

}
	

