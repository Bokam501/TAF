package com.hcl.atf.taf.model;

import static javax.persistence.GenerationType.IDENTITY;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "testcase_type_master")
public class TestcaseTypeMaster implements java.io.Serializable{

	private Integer testcaseTypeId;	
	private String name;	
	private String description;
	
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "testcaseTypeId", unique = true, nullable = false)
	public Integer getTestcaseTypeId() {
		return testcaseTypeId;
	}
	public void setTestcaseTypeId(Integer testcaseTypeId) {
		this.testcaseTypeId = testcaseTypeId;
	}
	
	@Column(name = "name", length = 1000)
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	@Column(name = "description", length = 45)
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
}
