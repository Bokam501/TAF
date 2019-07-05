package com.hcl.atf.taf.model.json;


import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.hcl.atf.taf.model.DecouplingCategory;
import com.hcl.atf.taf.model.TestCaseList;

public class JsonDecouplingCategoryTestCase implements java.io.Serializable{
	private static final Log log = LogFactory
			.getLog(JsonDecouplingCategoryTestCase.class);
	@JsonProperty	
	private int testcaseId;
	
	@JsonProperty	
	private String testcaseName;
	
	@JsonProperty	
	private String testcaseDescription;
	
	@JsonProperty	
	private String testcaseCode;
	
	@JsonProperty
	private int decouplingCategoryId;
	
	@JsonProperty	
	private String decouplingCategoryName;
	
	
	public JsonDecouplingCategoryTestCase() {
	}	
	
	public JsonDecouplingCategoryTestCase(TestCaseList testCaseList) {
		this.testcaseId=testCaseList.getTestCaseId();
		this.testcaseName=testCaseList.getTestCaseName();
		this.testcaseDescription=testCaseList.getTestCaseDescription();
		this.testcaseCode=testCaseList.getTestCaseCode();		
		
		Set<DecouplingCategory> decouplingCategorySet = testCaseList.getDecouplingCategory();
		if (decouplingCategorySet.size() != 0)
		{
			DecouplingCategory decouplingCategory = decouplingCategorySet.iterator().next();
			if (decouplingCategory != null)
			{
				decouplingCategoryId = decouplingCategory.getDecouplingCategoryId();
				decouplingCategoryName = decouplingCategory.getDecouplingCategoryName();				
			}
		}
	}

	public static boolean isInteger(String s) {
		try {
			Integer.parseInt(s);
		} catch (NumberFormatException e) {
			return false;
		}
		// only got here if we didn't return false
		return true;
	}
	public int getTestCaseId() {
		return testcaseId;
	}

	public void setTestCaseId(int testcaseId) {
		this.testcaseId = testcaseId;
	}

	public String getTestCaseName() {
		return testcaseName;
	}

	public void setTestCaseName(String testcaseName) {
		this.testcaseName = testcaseName;
	}

	public String getTestCaseDescription() {
		return testcaseDescription;
	}

	public void setTestCaseDescription(String testcaseDescription) {
		this.testcaseDescription = testcaseDescription;
	}

	public String getTestcaseCode() {
		return testcaseCode;
	}

	public void setTestcaseCode(String testcaseCode) {
		this.testcaseCode = testcaseCode;
	}

	public int getDecouplingCategoryId() {
		return decouplingCategoryId;
	}

	public void setDecouplingCategoryId(int decouplingCategoryId) {
		this.decouplingCategoryId = decouplingCategoryId;
	}

	public String getDecouplingCategoryName() {
		return decouplingCategoryName;
	}

	public void setDecouplingCategoryName(String decouplingCategoryName) {
		this.decouplingCategoryName = decouplingCategoryName;
	}
}
