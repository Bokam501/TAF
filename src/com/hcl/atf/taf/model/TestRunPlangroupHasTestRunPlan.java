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
@Table(name = "testrunplangroup_has_testrunplan")
public class TestRunPlangroupHasTestRunPlan implements java.io.Serializable {
private static final long serialVersionUID = 1L;

private Integer testRunGroupId;	
private TestRunPlan testrunplan;
private TestRunPlanGroup testRunPlanGroup;
private Integer executionOrder;

public TestRunPlangroupHasTestRunPlan () {
}

@Id
@GeneratedValue(strategy =IDENTITY)
@Column(name = "testRunGroupId")
public Integer getTestRunGroupId() {
	return testRunGroupId;
}

public void setTestRunGroupId(Integer testRunGroupId) {
	this.testRunGroupId = testRunGroupId;
}

@ManyToOne(fetch = FetchType.LAZY)
@JoinColumn(name = "testRunPlanId")
public TestRunPlan getTestrunplan() {
	return testrunplan;
}

public void setTestrunplan(TestRunPlan testRunPlanId) {
	this.testrunplan = testRunPlanId;
}

@ManyToOne(fetch = FetchType.LAZY)
@JoinColumn(name = "testRunPlanGroupId")
public TestRunPlanGroup getTestRunPlanGroup() {
	return testRunPlanGroup;
}

public void setTestRunPlanGroup(TestRunPlanGroup testRunPlanGroup) {
	this.testRunPlanGroup = testRunPlanGroup;
}




@Column(name = "executionOrder")
public Integer getExecutionOrder() {
	return executionOrder;
}

public void setExecutionOrder(Integer executionOrder) {
	this.executionOrder = executionOrder;
}

}

