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
@Table(name = "testrunjontestsuite_has_testcase")
public class TestRunJobTestSuiteHasTestCase implements java.io.Serializable {
private static final long serialVersionUID = 1L;

private Integer id;
private TestRunJob testRunJob;
private TestSuiteList testSuiteList;
private TestCaseList testCaseList;


public TestRunJobTestSuiteHasTestCase () {
}

@Id
@GeneratedValue(strategy =IDENTITY)
@Column(name = "Id")
public Integer getId() {
	return id;
}


public void setId(Integer id) {
	this.id = id;
}

@ManyToOne(fetch = FetchType.LAZY)
@JoinColumn(name = "testRunJobId")
public TestRunJob getTestRunJob() {
	return testRunJob;
}


public void setTestRunJob(TestRunJob testRunJob) {
	this.testRunJob = testRunJob;
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


}

