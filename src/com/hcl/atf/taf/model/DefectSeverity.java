package com.hcl.atf.taf.model;

// Generated Feb 4, 2014 4:30:16 PM by Hibernate Tools 3.4.0.CR1

import static javax.persistence.GenerationType.IDENTITY;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * TestExecutionResultBugList generated by hbm2java
 */
@Entity
@Table(name = "defect_severity")
public class DefectSeverity implements java.io.Serializable {

	private Integer severityId;
	private String severityName;
	
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "severityId", unique = true, nullable = false)
	public Integer getSeverityId() {
		return severityId;
	}

	public void setSeverityId(Integer severityId) {
		this.severityId = severityId;
	}

	@Column(name = "severityName", length = 45)
	public String getSeverityName() {
		return severityName;
	}

	public void setSeverityName(String severityName) {
		this.severityName = severityName;
	}
	
	public DefectSeverity() {
	}	
}