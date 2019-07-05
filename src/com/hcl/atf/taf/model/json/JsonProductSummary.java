package com.hcl.atf.taf.model.json;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.hcl.atf.taf.model.dto.ProductSummaryDTO;

public class JsonProductSummary {

	@JsonProperty
	private Integer productId;
	@JsonProperty
	private String productName;	
	@JsonProperty
	private String description;
	@JsonProperty
	private Integer status;
	@JsonProperty
	private Integer customerId;
	@JsonProperty
	private String customerName;
	@JsonProperty
	private Integer engagementId;
	@JsonProperty
	private String engagementName;
	@JsonProperty
	private String productMode;
	@JsonProperty
	private Integer pVersionCount;
	@JsonProperty
	private Integer pBuildCount;
	@JsonProperty
	private Integer featuresCount;
	@JsonProperty
	private Integer testCaseCount;
	@JsonProperty
	private Integer testSuiteCount;
	@JsonProperty
	private Integer dcCount;
	@JsonProperty
	private Integer wpCount;
	@JsonProperty
	private Integer productEnvironmentsCount;
	@JsonProperty
	private Integer productEnviCombinationCount;
	@JsonProperty
	private Integer testRunPlanCount;
	@JsonProperty
	private Integer testCaseStepsCount;
	
	
	@JsonProperty
	private Integer unMappedTCFeatureCount;
	@JsonProperty
	private Integer mappedTCFeatureCount;
	@JsonProperty
	private Integer activeWpCount;
	
	@JsonProperty
	private Integer testManagersCount;
	@JsonProperty
	private Integer testLeadsCount;
	@JsonProperty
	private Integer testersCount;
	
	@JsonProperty
	private Integer pcTestManagersCount;
	@JsonProperty
	private Integer pcTestLeadsCount;
	@JsonProperty
	private Integer pcTestersCount;
	
	@JsonProperty
	private Integer mappedResourcePoolCount;
	@JsonProperty
	private Integer mappedResourcePoolsName;
	@JsonProperty
	private Integer productCoreResourceCount;
	@JsonProperty
	private Integer unMappingPercentage;

	@JsonProperty
	private String featureTestCoverage;
	@JsonProperty
	private String testCaseAutomationCoverage;
	@JsonProperty
	private String orphanTestCases;
	@JsonProperty
	private Integer defectCount;
	
	@JsonProperty
	private Integer testScriptCount;
	
	public JsonProductSummary(){
		this.pVersionCount = 0;
		this.pBuildCount = 0;
		this.featuresCount = 0;
		this.testCaseCount = 0;
		this.testSuiteCount = 0;
		this.dcCount = 0;
		this.wpCount = 0;
		this.wpCount = 0;
		this.activeWpCount = 0;
		this.wpCount = 0;
		this.testManagersCount = 0;
		this.testLeadsCount = 0;
		this.testersCount = 0;
		this.mappedResourcePoolCount = 0;
		this.productCoreResourceCount=0;
		this.mappedTCFeatureCount = 0;
		this.unMappedTCFeatureCount = 0;
		this.productEnvironmentsCount=0;
		this.productEnviCombinationCount=0;
		this.testRunPlanCount=0;
		this.testCaseStepsCount=0;
		this.unMappingPercentage=0;
		this.pcTestManagersCount = 0;
		this.pcTestLeadsCount = 0;
		this.pcTestersCount = 0;
		this.testScriptCount =0; 
		
		this.defectCount=0;
	}
	
	public JsonProductSummary(ProductSummaryDTO productSummaryDTO){
		this.productId = productSummaryDTO.getProductId();
		this.productName = productSummaryDTO.getProductName();
		this.description = productSummaryDTO.getDescription();
		this.customerId = productSummaryDTO.getCustomerId();
		this.customerName = productSummaryDTO.getCustomerName();
		this.engagementId = productSummaryDTO.getEngagementId();
		this.engagementName = productSummaryDTO.getEngagementName();
		this.productMode = productSummaryDTO.getProductMode();
		
		if(productSummaryDTO.getpVersionCount() != null){
			this.pVersionCount = productSummaryDTO.getpVersionCount();
		}else{
			this.pVersionCount = 0;
		}
		
		if(productSummaryDTO.getpBuildCount() != null){
			this.pBuildCount = productSummaryDTO.getpBuildCount();
		}else{
			this.pBuildCount = 0;
		}
		
		if(productSummaryDTO.getFeaturesCount()!= null){
			this.featuresCount = productSummaryDTO.getFeaturesCount();
		}else{
			this.featuresCount = 0;
		}
		
		
		if(productSummaryDTO.getTestCaseCount() != null){
			this.testCaseCount = productSummaryDTO.getTestCaseCount();
		}else{
			this.testCaseCount = 0;
		}
		
		if(productSummaryDTO.getTestSuiteCount() != null){
			this.testSuiteCount = productSummaryDTO.getTestSuiteCount();
		}else{
			this.testSuiteCount = 0;
		}
		
		
		if(productSummaryDTO.getDcCount() != null){
			this.dcCount = productSummaryDTO.getDcCount();
		}else{
			this.dcCount = 0;
		}
		
		if(productSummaryDTO.getWpCount() != null){
			this.wpCount = productSummaryDTO.getWpCount();
		}else{
			this.wpCount = 0;
		}
		
		if(productSummaryDTO.getActiveWpCount() != null){
			this.activeWpCount = productSummaryDTO.getActiveWpCount();
		}else{
			this.activeWpCount = 0;
		}
		
		if(productSummaryDTO.getTestManagersCount() != null){
			this.testManagersCount = productSummaryDTO.getTestManagersCount();
		}else{
			this.testManagersCount = 0;
		}
		
		if(productSummaryDTO.getTestLeadsCount() != null){
			this.testLeadsCount = productSummaryDTO.getTestLeadsCount();
		}else{
			this.testLeadsCount = 0;
		}
		
		if(productSummaryDTO.getTestersCount() != null){
			this.testersCount = productSummaryDTO.getTestersCount();
		}else{
			this.testersCount = 0;
		}
		
		if(productSummaryDTO.getMappedResourcePoolCount() != null){
			this.mappedResourcePoolCount = productSummaryDTO.getMappedResourcePoolCount();
		}else{
			this.mappedResourcePoolCount = 0;
		}
		
		if(productSummaryDTO.getProductCoreResourceCount() != null){
			this.productCoreResourceCount = productSummaryDTO.getProductCoreResourceCount();
		}else{
			this.productCoreResourceCount = 0;
		}
		
		if(productSummaryDTO.getMappedTCFeatureCount() != null){
			this.mappedTCFeatureCount = productSummaryDTO.getMappedTCFeatureCount();
		}else{
			this.mappedTCFeatureCount = 0;
		}
		
		if(productSummaryDTO.getUnMappedTCFeatureCount() != null){
			this.unMappedTCFeatureCount = productSummaryDTO.getUnMappedTCFeatureCount();
		}else{
			this.unMappedTCFeatureCount = 0;
		}
		
		
		if(productSummaryDTO.getProductEnvironmentsCount() != null){
			this.productEnvironmentsCount = productSummaryDTO.getProductEnvironmentsCount();
		}else{
			this.productEnvironmentsCount = 0;
		}
		
		if(productSummaryDTO.getProductEnviCombinationCount() != null){
			this.productEnviCombinationCount = productSummaryDTO.getProductEnviCombinationCount();
		}else{
			this.productEnviCombinationCount = 0;
		}
		
		if(productSummaryDTO.getTestRunPlanCount() != null){
			this.testRunPlanCount = productSummaryDTO.getTestRunPlanCount();
		}else{
			this.testRunPlanCount = 0;
		}
		
		if(productSummaryDTO.getTestCaseStepsCount() != null){
			this.testCaseStepsCount = productSummaryDTO.getTestCaseStepsCount();
		}else{
			this.testCaseStepsCount = 0;
		}
		
		if(productSummaryDTO.getUnMappingPercentage() != null){
			this.unMappingPercentage = productSummaryDTO.getUnMappingPercentage() ;
		}else{
			this.unMappingPercentage = 0;
		}
		
		if(productSummaryDTO.getPcTestManagersCount() != null){
			this.pcTestManagersCount = productSummaryDTO.getPcTestManagersCount();
		}else{
			this.pcTestManagersCount = 0;
		}
		
		if(productSummaryDTO.getPcTestLeadsCount() != null){
			this.pcTestLeadsCount = productSummaryDTO.getPcTestLeadsCount();
		}else{
			this.pcTestLeadsCount = 0;
		}
		
		if(productSummaryDTO.getPcTestersCount() != null){
			this.pcTestersCount = productSummaryDTO.getPcTestersCount();
		}else{
			this.pcTestersCount = 0;
		}		
	
		if(productSummaryDTO.getTestScriptCount() != null) {
			this.testScriptCount =productSummaryDTO.getTestScriptCount(); 
		}else {
			this.testScriptCount =0;
		}
		
		if(productSummaryDTO.getFeatureTestCoverage() !=null && !productSummaryDTO.getFeatureTestCoverage().isEmpty()) {
			this.featureTestCoverage = productSummaryDTO.getFeatureTestCoverage();
		} else {
			this.featureTestCoverage ="";
		}
		
		if(productSummaryDTO.getTestCaseAutomationCoverage() != null && !productSummaryDTO.getTestCaseAutomationCoverage().isEmpty()){ 
			this.testCaseAutomationCoverage = productSummaryDTO.getTestCaseAutomationCoverage();
		} else {
			this.testCaseAutomationCoverage = "";
		}
		
		if(productSummaryDTO.getOrphanTestCases() != null && !productSummaryDTO.getOrphanTestCases().isEmpty()) {
			this.orphanTestCases = productSummaryDTO.getOrphanTestCases();
		} else {
			this.orphanTestCases = "";
		}
		
		if(productSummaryDTO.getDefectCount() != null) {
			this.defectCount = productSummaryDTO.getDefectCount();
		}
	}

	public Integer getTestScriptCount() {
		return testScriptCount;
	}

	public void setTestScriptCount(Integer testScriptCount) {
		this.testScriptCount = testScriptCount;
	}
}
