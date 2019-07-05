package com.hcl.atf.taf.model.json;


import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.hcl.atf.taf.model.ProductFeature;
import com.hcl.atf.taf.model.TestCaseList;

public class JsonFeatureTestCase implements java.io.Serializable{
	private static final Log log = LogFactory
			.getLog(JsonFeatureTestCase.class);
	@JsonProperty	
	private int testcaseId;
	
	@JsonProperty	
	private String testcaseName;
	
	@JsonProperty	
	private String testcaseDescription;
	
	@JsonProperty	
	private String testcaseCode;
	
	@JsonProperty
	private int productFeatureId;
	
	@JsonProperty	
	private String productFeatureName;
	
	
	public JsonFeatureTestCase() {
	}	
	
	public JsonFeatureTestCase(TestCaseList testCaseList) {
		this.testcaseId=testCaseList.getTestCaseId();
		this.testcaseName=testCaseList.getTestCaseName();
		this.testcaseDescription=testCaseList.getTestCaseDescription();
		this.testcaseCode=testCaseList.getTestCaseCode();		
		
		Set<ProductFeature> productFeatureSet = testCaseList.getProductFeature();
		if (productFeatureSet.size() != 0)
		{
			ProductFeature productFeature = productFeatureSet.iterator().next();
			if (productFeature != null)
			{
				productFeatureId = productFeature.getProductFeatureId();
				productFeatureName = productFeature.getProductFeatureName();				
			}
		}		
	}

	public int getTestcaseId() {
		return testcaseId;
	}

	public void setTestcaseId(int testcaseId) {
		this.testcaseId = testcaseId;
	}

	public String getTestcaseName() {
		return testcaseName;
	}

	public void setTestcaseName(String testcaseName) {
		this.testcaseName = testcaseName;
	}

	public String getTestcaseDescription() {
		return testcaseDescription;
	}

	public void setTestcaseDescription(String testcaseDescription) {
		this.testcaseDescription = testcaseDescription;
	}

	public String getTestcaseCode() {
		return testcaseCode;
	}

	public void setTestcaseCode(String testcaseCode) {
		this.testcaseCode = testcaseCode;
	}

	public int getProductFeatureId() {
		return productFeatureId;
	}

	public void setProductFeatureId(int productFeatureId) {
		this.productFeatureId = productFeatureId;
	}

	public String getProductFeatureName() {
		return productFeatureName;
	}

	public void setProductFeatureName(String productFeatureName) {
		this.productFeatureName = productFeatureName;
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
	

	
}
