package com.hcl.atf.taf.model;

// Generated Feb 4, 2014 4:30:16 PM by Hibernate Tools 3.4.0.CR1

import java.util.HashSet;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * TestResultStatusMaster generated by hbm2java
 */
@Entity
@Table(name = "test_result_status_master")
public class TestResultStatusMaster implements java.io.Serializable {

	private String testResultStatus;
	private Set<TestRunList> testRunLists = new HashSet<TestRunList>(0);
	private Set<TestExecutionResult> testExecutionResults = new HashSet<TestExecutionResult>(
			0);

	public TestResultStatusMaster() {
	}

	public TestResultStatusMaster(String testResultStatus) {
		this.testResultStatus = testResultStatus;
	}

	public TestResultStatusMaster(String testResultStatus,
			Set<TestRunList> testRunLists,
			Set<TestExecutionResult> testExecutionResults) {
		this.testResultStatus = testResultStatus;
		this.testRunLists = testRunLists;
		this.testExecutionResults = testExecutionResults;
	}

	@Id
	@Column(name = "testResultStatus", unique = true, nullable = false, length = 10)
	public String getTestResultStatus() {
		if (this.testResultStatus.equalsIgnoreCase("FAILED"))
			return "FAIL";
		return this.testResultStatus;
	}

	public void setTestResultStatus(String testResultStatus) {
		this.testResultStatus = testResultStatus;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "testResultStatusMaster")
	public Set<TestRunList> getTestRunLists() {
		return this.testRunLists;
	}

	public void setTestRunLists(Set<TestRunList> testRunLists) {
		this.testRunLists = testRunLists;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "testResultStatusMaster")
	public Set<TestExecutionResult> getTestExecutionResults() {
		return this.testExecutionResults;
	}

	public void setTestExecutionResults(
			Set<TestExecutionResult> testExecutionResults) {
		this.testExecutionResults = testExecutionResults;
	}

}
