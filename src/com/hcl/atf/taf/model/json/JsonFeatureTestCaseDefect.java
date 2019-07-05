package com.hcl.atf.taf.model.json;


import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.hcl.atf.taf.model.ProductFeature;
import com.hcl.atf.taf.model.ProductVersionListMaster;
import com.hcl.atf.taf.model.TestCaseList;

public class JsonFeatureTestCaseDefect implements java.io.Serializable{
	private static final Log log = LogFactory
			.getLog(JsonFeatureTestCaseDefect.class);
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
	@JsonProperty	
	private String productFeatureCode;
	@JsonProperty
	private String openDefects;
	@JsonProperty
	private String referBackDefects;
	@JsonProperty
	private String reviewedDefects;
	@JsonProperty
	private String approvedDefects;	
	@JsonProperty
	private String closedDefects;	
	@JsonProperty
	private String totalDefects;
	@JsonProperty
	private String testExecutionCount;
	
	@JsonProperty
	private int productVersionId;	
	@JsonProperty	
	private String productVersionName;
	
	public JsonFeatureTestCaseDefect() {
	}	
	
	public JsonFeatureTestCaseDefect(TestCaseList testCaseList) {
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
				productFeatureCode =  productFeature.getProductFeatureCode();
			}
		}	
		Set<ProductVersionListMaster> productVerList = testCaseList.getProductVersionList();
		if (productVerList.size() != 0)
		{
			ProductVersionListMaster productVer = productVerList.iterator().next();
			if (productVer != null)
			{
				productVersionId = productVer.getProductVersionListId();
				productFeatureName = productVer.getProductVersionName();	
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

	

	public String getProductFeatureCode() {
		return productFeatureCode;
	}

	public void setProductFeatureCode(String productFeatureCode) {
		this.productFeatureCode = productFeatureCode;
	}

	public String getOpenDefects() {
		return openDefects;
	}

	public void setOpenDefects(String openDefects) {
		this.openDefects = openDefects;
	}

	public String getReferBackDefects() {
		return referBackDefects;
	}

	public void setReferBackDefects(String referBackDefects) {
		this.referBackDefects = referBackDefects;
	}

	public String getReviewedDefects() {
		return reviewedDefects;
	}

	public void setReviewedDefects(String reviewedDefects) {
		this.reviewedDefects = reviewedDefects;
	}

	public String getApprovedDefects() {
		return approvedDefects;
	}

	public void setApprovedDefects(String approvedDefects) {
		this.approvedDefects = approvedDefects;
	}

	public String getClosedDefects() {
		return closedDefects;
	}

	public void setClosedDefects(String closedDefects) {
		this.closedDefects = closedDefects;
	}

	public String getTotalDefects() {
		return totalDefects;
	}

	public void setTotalDefects(String totalDefects) {
		this.totalDefects = totalDefects;
	}

	public String getTestExecutionCount() {
		return testExecutionCount;
	}

	public void setTestExecutionCount(String testExecutionCount) {
		this.testExecutionCount = testExecutionCount;
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

	public int getProductVersionId() {
		return productVersionId;
	}

	public void setProductVersionId(int productVersionId) {
		this.productVersionId = productVersionId;
	}

	public String getProductVersionName() {
		return productVersionName;
	}

	public void setProductVersionName(String productVersionName) {
		this.productVersionName = productVersionName;
	}
	
}
