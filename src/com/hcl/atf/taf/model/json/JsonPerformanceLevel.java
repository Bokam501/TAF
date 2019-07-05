package com.hcl.atf.taf.model.json;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.hcl.atf.taf.model.PerformanceLevel;

public class JsonPerformanceLevel implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	private static final Log log = LogFactory.getLog(JsonPerformanceLevel.class);
	
	@JsonProperty
	private Integer performanceLevelId;
	@JsonProperty
	private String levelName;
	@JsonProperty
	private String description;
	@JsonProperty
	private Integer levelValue;
	
	public JsonPerformanceLevel() {
	}
	
	
	public JsonPerformanceLevel(PerformanceLevel performanceLevel){
		this.performanceLevelId = performanceLevel.getPerformanceLevelId();
		this.levelName = performanceLevel.getLevelName();
		this.description = performanceLevel.getDescription();
		this.levelValue = performanceLevel.getLevelValue();
	}
	
	
	@JsonIgnore
	public PerformanceLevel getPerformanceLevel() {
		PerformanceLevel performanceLevel = new PerformanceLevel();
		performanceLevel.setPerformanceLevelId(this.performanceLevelId);
		performanceLevel.setLevelName(this.levelName);
		performanceLevel.setDescription(this.description);
		performanceLevel.setLevelValue(this.levelValue);
				
		return performanceLevel;
	}


	public Integer getPerformanceLevelId() {
		return performanceLevelId;
	}


	public void setPerformanceLevelId(Integer performanceLevelId) {
		this.performanceLevelId = performanceLevelId;
	}


	public String getLevelName() {
		return levelName;
	}


	public void setLevelName(String levelName) {
		this.levelName = levelName;
	}


	public String getDescription() {
		return description;
	}


	public void setDescription(String description) {
		this.description = description;
	}


	public Integer getLevelValue() {
		return levelValue;
	}


	public void setLevelValue(Integer levelValue) {
		this.levelValue = levelValue;
	}

	
}
