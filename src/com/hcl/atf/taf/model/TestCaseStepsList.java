package com.hcl.atf.taf.model;

// Generated Feb 4, 2014 4:30:16 PM by Hibernate Tools 3.4.0.CR1


import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;

import static javax.persistence.GenerationType.IDENTITY;

import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * TestCaseStepsList generated by hbm2java
 */
@Entity
@Table(name = "test_case_steps_list")
public class TestCaseStepsList implements java.io.Serializable {

	private Integer testStepId;
	private TestCaseList testCaseList;
	private String testStepName;
	private String testStepDescription;
	private String testStepInput;
	private String testStepExpectedOutput;
	private Set<TestExecutionResult> testExecutionResults = new HashSet<TestExecutionResult>(
			0);
	//Changes for TestManagement tools 
	private String testStepCode;
	private String testStepSource;
	private Date testStepCreatedDate;
	private Date testStepLastUpdatedDate;
	
	private Integer status;
	
	
	public TestCaseStepsList() {
	}

	public TestCaseStepsList(String testStepName) {
		this.testStepName = testStepName;
	}

	public TestCaseStepsList(TestCaseList testCaseList, String testStepName,
			String testStepDescription, String testStepInput,
			String testStepExpectedOutput,
			Set<TestExecutionResult> testExecutionResults) {
		this.testCaseList = testCaseList;
		this.testStepName = testStepName;
		this.testStepDescription = testStepDescription;
		this.testStepInput = testStepInput;
		this.testStepExpectedOutput = testStepExpectedOutput;
		this.testExecutionResults = testExecutionResults;
	}

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "testStepId", unique = true, nullable = false)
	public Integer getTestStepId() {
		return this.testStepId;
	}

	public void setTestStepId(Integer testStepId) {
		this.testStepId = testStepId;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "testCaseId")
	public TestCaseList getTestCaseList() {
		return this.testCaseList;
	}

	public void setTestCaseList(TestCaseList testCaseList) {
		this.testCaseList = testCaseList;
	}

	@Column(name = "testStepName", nullable = false)
	public String getTestStepName() {
		return this.testStepName;
	}

	public void setTestStepName(String testStepName) {
		this.testStepName = testStepName;
	}

	@Column(name = "testStepDescription")
	public String getTestStepDescription() {
		return this.testStepDescription;
	}

	public void setTestStepDescription(String testStepDescription) {
		this.testStepDescription = testStepDescription;
	}

	@Column(name = "testStepInput")
	public String getTestStepInput() {
		return this.testStepInput;
	}

	public void setTestStepInput(String testStepInput) {
		this.testStepInput = testStepInput;
	}

	@Column(name = "testStepExpectedOutput")
	public String getTestStepExpectedOutput() {
		return this.testStepExpectedOutput;
	}

	public void setTestStepExpectedOutput(String testStepExpectedOutput) {
		this.testStepExpectedOutput = testStepExpectedOutput;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "testCaseStepsList")
	public Set<TestExecutionResult> getTestExecutionResults() {
		return this.testExecutionResults;
	}

	public void setTestExecutionResults(
			Set<TestExecutionResult> testExecutionResults) {
		this.testExecutionResults = testExecutionResults;
	}

	@Column(name = "testStepCode", length = 100)
	public String getTestStepCode() {
		return this.testStepCode;
	}

	public void setTestStepCode(String testStepCode) {
		this.testStepCode = testStepCode;
	}
	

	@Column(name = "testStepSource", length = 100)
	public String getTestStepSource() {
		return this.testStepSource;
	}

	public void setTestStepSource(String testStepSource) {
		this.testStepSource = testStepSource;
	}
	

	@Column(name = "testStepCreatedDate", length = 100)
	public Date getTestStepCreatedDate() {
		return this.testStepCreatedDate;
	}

	public void setTestStepCreatedDate(Date testStepCreatedDate) {
		this.testStepCreatedDate = testStepCreatedDate;
	}
	

	@Column(name = "testStepLastUpdatedDate", length = 100)
	public Date getTestStepLastUpdatedDate() {
		return this.testStepLastUpdatedDate;
	}

	public void setTestStepLastUpdatedDate(Date testStepLastUpdatedDate) {
		this.testStepLastUpdatedDate = testStepLastUpdatedDate;
	}
		
	@Column(name="status")
	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	@Override
	public boolean equals(Object testCaseStepList) {	
		if (testCaseStepList == null)
			return false;
		TestCaseStepsList testStep = (TestCaseStepsList) testCaseStepList;
		if (testStep.getTestStepId().equals(this.testStepId)) {
			
			return true;
		} else {
			return false;
		}
	}
	
	@Override
	public int hashCode(){
	    return (int) testStepId;
	  }
}