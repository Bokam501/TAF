package com.hcl.atf.taf.model;

// Generated Feb 4, 2014 4:30:16 PM by Hibernate Tools 3.4.0.CR1

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * LifeCyclePhase generated by hbm2java
 */
@Entity
@Table(name = "life_cycle_phase_master")
public class LifeCyclePhase implements java.io.Serializable {

	private Integer lifeCyclePhaseId;
	private String name;
	private String description;

	public LifeCyclePhase() {
	}
	
	@Id
	@Column(name = "lifeCyclePhaseId", unique = true, nullable = false, length = 20)
	public Integer getLifeCyclePhaseId() {
		return lifeCyclePhaseId;
	}

	public void setLifeCyclePhaseId(Integer lifeCyclePhaseId) {
		this.lifeCyclePhaseId = lifeCyclePhaseId;
	}
	
	@Column(name = "name", nullable = false)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	@Column(name = "description", nullable = false)
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}