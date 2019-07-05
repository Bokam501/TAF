package com.hcl.atf.taf.model.dto;

import com.hcl.atf.taf.model.TestCaseStepsList;


public class TestStepDTO {
	private TestCaseStepsList testStep;
	
	public TestCaseStepsList getTestCaseStepsList() {
		return testStep;
	}
	public void setTestCaseStepsList(TestCaseStepsList testStep) {
		this.testStep = testStep;
	}
}
