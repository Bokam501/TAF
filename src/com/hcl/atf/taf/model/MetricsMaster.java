package com.hcl.atf.taf.model;



import static javax.persistence.GenerationType.IDENTITY;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Metrics Master for overall metrics.
 */
@Entity
@Table(name = "metrics_master")
public class MetricsMaster implements java.io.Serializable {

	
	private Integer metricsId;
	private String metricsName; 
	private Integer target; 
	private String period;
	private String startsFrom;

	public MetricsMaster(){
		
	}

	public MetricsMaster(Integer metricsId, String metricsName, Integer target,String period,String startsFrom){
		this.metricsId = metricsId;
		this.metricsName = metricsName;
		this.target = target;
		this.period = period;
		this.startsFrom = startsFrom;
	}

	
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "metricsId", unique = true, nullable = false)
	public Integer getMetricsId() {
		return metricsId;
	}

	public void setMetricsId(Integer metricsId) {
		this.metricsId = metricsId;
	}

	
	@Column(name = "metricsName")
	public String getMetricsName() {
		return metricsName;
	}

	public void setMetricsName(String metricsName) {
		this.metricsName = metricsName;
	}

	
	@Column(name = "target")
	public Integer getTarget() {
		return target;
	}

	public void setTarget(Integer target) {
		this.target = target;
	}

	@Column(name = "period")
	public String getPeriod() {
		return period;
	}

	public void setPeriod(String period) {
		this.period = period;
	}

	@Column(name = "startsFrom")
	public String getStartsFrom() {
		return startsFrom;
	}

	public void setStartsFrom(String startsFrom) {
		this.startsFrom = startsFrom;
	}

	

	
	
}
