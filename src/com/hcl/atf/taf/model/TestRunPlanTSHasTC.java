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
@Table(name = "testrunplantestsuite_has_testcase")
public class TestRunPlanTSHasTC  implements java.io.Serializable{
	private static final long serialVersionUID = 1L;

	private Integer id;	
	private TestRunPlan testrunplan;
	private TestSuiteList testSuiteList;
	private TestCaseList testCaseList;
	private Integer executionOrder;
	
	
	public TestRunPlanTSHasTC() {
		
	}

	public TestRunPlanTSHasTC(Integer id, TestRunPlan testrunplan,
			TestSuiteList testSuiteList, TestCaseList testCaseList) {
		this.id = id;
		this.testrunplan = testrunplan;
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
	@JoinColumn(name = "testRunPlanId")
	public TestRunPlan getTestrunplan() {
		return testrunplan;
	}
	public void setTestrunplan(TestRunPlan testrunplan) {
		this.testrunplan = testrunplan;
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
