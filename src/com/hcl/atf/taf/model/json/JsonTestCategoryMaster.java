package com.hcl.atf.taf.model.json;



import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.hcl.atf.taf.model.TestCategoryMaster;


public class JsonTestCategoryMaster implements java.io.Serializable {

	@JsonProperty
	private String testCategory;
	

	public JsonTestCategoryMaster() {
	}

	public JsonTestCategoryMaster(TestCategoryMaster testCategoryMaster) {
		this.testCategory = testCategoryMaster.getTestCategory();
	}

	public String getTestCategory() {
		return testCategory;
	}

	public void setTestCategory(String testCategory) {
		this.testCategory = testCategory;
	}

	@JsonIgnore
	public TestCategoryMaster getTestCategoryMaster(){
		TestCategoryMaster testCategoryMaster = new TestCategoryMaster();
		testCategoryMaster.setTestCategory(testCategory);		
		return testCategoryMaster;
	}

}
