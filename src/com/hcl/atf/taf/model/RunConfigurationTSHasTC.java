package com.hcl.atf.taf.model;

import static javax.persistence.GenerationType.IDENTITY;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "runconfigurationtestsuite_has_testcase")
public class RunConfigurationTSHasTC  implements java.io.Serializable{

	private static final long serialVersionUID = 1L;

	private Integer id;	
	private RunConfiguration runConfiguration;
	private TestSuiteList testSuiteList;
	private TestCaseList testCaseList;
	private Integer executionOrder;
	
	public RunConfigurationTSHasTC() {
		
	}

	public RunConfigurationTSHasTC(Integer id, RunConfiguration runConfiguration, TestSuiteList testSuiteList, TestCaseList testCaseList) {
		this.id = id;
		this.runConfiguration = runConfiguration;
		this.testSuiteList = testSuiteList;
		this.testCaseList = testCaseList;
	}
	@Id
	@GeneratedValue(strategy =IDENTITY)
	@Column(name = "id")
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "runConfigurationId")
	public RunConfiguration getRunConfiguration() {
		return runConfiguration;
	}
	
	public void setRunConfiguration(RunConfiguration runConfiguration) {
		this.runConfiguration = runConfiguration;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "testSuiteId")
	public TestSuiteList getTestSuiteList() {
		return testSuiteList;
	}
	
	public void setTestSuiteList(TestSuiteList testSuiteList) {
		this.testSuiteList = testSuiteList;
	}
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "testCaseId")
	public TestCaseList getTestCaseList() {
		return testCaseList;
	}
	public void setTestCaseList(TestCaseList testCaseList) {
		this.testCaseList = testCaseList;
	}
	@Column(name = "executionOrder")
	public Integer getExecutionOrder() {
		return executionOrder;
	}

	public void setExecutionOrder(Integer executionOrder) {
		this.executionOrder = executionOrder;
	}
}
