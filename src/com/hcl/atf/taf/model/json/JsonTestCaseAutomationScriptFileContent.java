package com.hcl.atf.taf.model.json;

import com.fasterxml.jackson.annotation.JsonProperty;

public class JsonTestCaseAutomationScriptFileContent {
	
	@JsonProperty
	public  String elementName;
	@JsonProperty
	public  String description;
	@JsonProperty
	public  String id;
	@JsonProperty
	public String label ;
	@JsonProperty
	public String chromeXpath ;
	@JsonProperty
	public String firefoxXpath;
	@JsonProperty
	public String ieXpath;
	@JsonProperty
	public String safariXpath;
	@JsonProperty
	public String chromeCssLocator ;
	@JsonProperty
	public String firefoxCssLocator ;
	@JsonProperty
	public String ieCssLocator;
	@JsonProperty
	public String safariCssLocator;
	@JsonProperty
	public String zone ;
	@JsonProperty
	public String index ;
	
	@JsonProperty
	public  String testData;
	@JsonProperty
	public  String testDataValue;
	@JsonProperty
	public  String testDataType;
	@JsonProperty
	public  String testDataCounter;
	@JsonProperty
	public  String testDataRemarks;
	
	public JsonTestCaseAutomationScriptFileContent(){
		
	}

	public String getElementName() {
		return elementName;
	}

	public void setElementName(String elementName) {
		this.elementName = elementName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getChromeXpath() {
		return chromeXpath;
	}

	public void setChromeXpath(String chromeXpath) {
		this.chromeXpath = chromeXpath;
	}

	public String getFirefoxXpath() {
		return firefoxXpath;
	}

	public void setFirefoxXpath(String firefoxXpath) {
		this.firefoxXpath = firefoxXpath;
	}

	public String getIeXpath() {
		return ieXpath;
	}

	public void setIeXpath(String ieXpath) {
		this.ieXpath = ieXpath;
	}

	public String getSafariXpath() {
		return safariXpath;
	}

	public void setSafariXpath(String safariXpath) {
		this.safariXpath = safariXpath;
	}

	public String getChromeCssLocator() {
		return chromeCssLocator;
	}

	public void setChromeCssLocator(String chromeCssLocator) {
		this.chromeCssLocator = chromeCssLocator;
	}

	public String getFirefoxCssLocator() {
		return firefoxCssLocator;
	}

	public void setFirefoxCssLocator(String firefoxCssLocator) {
		this.firefoxCssLocator = firefoxCssLocator;
	}

	public String getIeCssLocator() {
		return ieCssLocator;
	}

	public void setIeCssLocator(String ieCssLocator) {
		this.ieCssLocator = ieCssLocator;
	}

	public String getSafariCssLocator() {
		return safariCssLocator;
	}

	public void setSafariCssLocator(String safariCssLocator) {
		this.safariCssLocator = safariCssLocator;
	}

	public String getZone() {
		return zone;
	}

	public void setZone(String zone) {
		this.zone = zone;
	}

	public String getIndex() {
		return index;
	}

	public void setIndex(String index) {
		this.index = index;
	}

	public String getTestData() {
		return testData;
	}

	public void setTestData(String testData) {
		this.testData = testData;
	}

	public String getTestDataValue() {
		return testDataValue;
	}

	public void setTestDataValue(String testDataValue) {
		this.testDataValue = testDataValue;
	}

	public String getTestDataType() {
		return testDataType;
	}

	public void setTestDataType(String testDataType) {
		this.testDataType = testDataType;
	}

	public String getTestDataCounter() {
		return testDataCounter;
	}

	public void setTestDataCounter(String testDataCounter) {
		this.testDataCounter = testDataCounter;
	}

	public String getTestDataRemarks() {
		return testDataRemarks;
	}

	public void setTestDataRemarks(String testDataRemarks) {
		this.testDataRemarks = testDataRemarks;
	}
	
}
