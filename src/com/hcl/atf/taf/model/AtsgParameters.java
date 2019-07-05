package com.hcl.atf.taf.model;

import static javax.persistence.GenerationType.IDENTITY;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "atsg_parameters")
public class AtsgParameters {
	  private Integer atsgId;	
	private Integer testCaseId;
	private Integer objectRepositoryId;
	private Integer testDataId;
	private Integer productVersionId;
	private String testEngine;
	
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "atsgId", unique = true, nullable = false)
	public Integer getAtsgId() {
		return atsgId;
	}
	public void setAtsgId(Integer atsgId) {
		this.atsgId = atsgId;
	}
	
	
	@Column(name = "testCaseId")
	public Integer getTestCaseId() {
		return testCaseId;
	}
	public void setTestCaseId(Integer testCaseId) {
		this.testCaseId = testCaseId;
	}
	@Column(name = "objectRepositoryId")
	public Integer getObjectRepositoryId() {
		return objectRepositoryId;
	}
	public void setObjectRepositoryId(Integer objectRepositoryId) {
		this.objectRepositoryId = objectRepositoryId;
	}
	@Column(name = "testDataId")
	public Integer getTestDataId() {
		return testDataId;
	}
	public void setTestDataId(Integer testDataId) {
		this.testDataId = testDataId;
	}
	@Column(name = "productVersionId")
	public Integer getProductVersionId() {
		return productVersionId;
	}
	public void setProductVersionId(Integer productVersionId) {
		this.productVersionId = productVersionId;
	}
	@Column(name = "testEngine")
	public String getTestEngine() {
		return testEngine;
	}
	public void setTestEngine(String testEngine) {
		this.testEngine = testEngine;
	}
	
	
}
