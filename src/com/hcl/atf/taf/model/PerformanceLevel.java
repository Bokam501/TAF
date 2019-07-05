package com.hcl.atf.taf.model;

import static javax.persistence.GenerationType.IDENTITY;

import java.io.Serializable;
import java.lang.Integer;
import java.lang.String;

import javax.persistence.*;

/**
 * Entity implementation class for Entity: PerformanceLevel
 *
 */
@Entity
@Table(name="performance_level_rating")
public class PerformanceLevel implements Serializable {

	   
	private Integer performanceLevelId;
	private String levelName;
	private String description;
	private Integer levelValue;
	private static final long serialVersionUID = 1L;

	public PerformanceLevel() {
		super();
	}
	
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "performanceLevelId", unique = true, nullable = false)
	public Integer getPerformanceLevelId() {
		return this.performanceLevelId;
	}

	public void setPerformanceLevelId(Integer performanceLevelId) {
		this.performanceLevelId = performanceLevelId;
	}
	
	@Column(name = "levelName")
	public String getLevelName() {
		return this.levelName;
	}

	public void setLevelName(String levelName) {
		this.levelName = levelName;
	}
	
	@Column(name = "description")
	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	@Column(name = "levelValue")
	public Integer getLevelValue() {
		return this.levelValue;
	}

	public void setLevelValue(Integer levelValue) {
		this.levelValue = levelValue;
	}
   
}
