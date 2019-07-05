package com.hcl.atf.taf.model.dto;

import com.hcl.atf.taf.model.TestCaseList;


public class TestCaseListDTO {
	private TestCaseList testCaseList;
	private String testSteps;
	private String feature;
	
	public TestCaseList getTestCaseList() {
		return testCaseList;
	}
	public void setTestCaseList(TestCaseList testCaseList) {
		this.testCaseList = testCaseList;
	}
	public String getTestSteps() {
		return testSteps;
	}
	public void setTestSteps(String testSteps) {
		this.testSteps = testSteps;
	}
	public String getFeature() {
		return feature;
	}
	public void setFeature(String feature) {
		this.feature = feature;
	}

}
