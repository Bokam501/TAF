/**
 * 
 */
package com.hcl.atf.taf.model.json;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.hcl.atf.taf.controller.utilities.DateUtility;
import com.hcl.atf.taf.model.dto.ISERecommandedTestcases;

/**
 * @author silambarasur
 *
 */
public class JsonISERecommendationTestcases {
	
	@JsonProperty
	private Integer id;
	@JsonProperty
	private Integer buildId;
	@JsonProperty
	private String buildName;
	@JsonProperty
	private String recommendationCategory;
	@JsonProperty
	private String rag_status;
	@JsonProperty
    private String ST;
	@JsonProperty
    private String NT;
	@JsonProperty
    private String thresold_prob;
	@JsonProperty
    private String title;
	@JsonProperty
    private String type[];
	@JsonProperty
    private String GT;
	@JsonProperty
    private Integer testcaseId;
	@JsonProperty
    private String testCaseName;
	@JsonProperty
    private String ET;
	@JsonProperty
    private String standard_prob;
	@JsonProperty
    private String CT;
	@JsonProperty
    private String BT;
	@JsonProperty
    private String feature;
	@JsonProperty
    private String probability;
	@JsonProperty
    private String picked;
	@JsonProperty
    private String defect_prob;
	@JsonProperty
    private String HFT;
	@JsonProperty
    private String UST;
	@JsonProperty
    private String testbed;
	@JsonProperty
    private String LFT;
	@JsonProperty
    private String rag_value;
	@JsonProperty
	private Integer productId;
	@JsonProperty
	private String productName;
	
	@JsonProperty
	private String planUptainTime;
	
	public String getPlanUptainTime() {
		return planUptainTime;
	}
	public void setPlanUptainTime(String planUptainTime) {
		this.planUptainTime = planUptainTime;
	}
	public JsonISERecommendationTestcases() {
		
	}
	public JsonISERecommendationTestcases(ISERecommandedTestcases iseRecommandedTestcases) {
		
		if(iseRecommandedTestcases != null) {
		this.id = iseRecommandedTestcases.getId();
		this.BT = iseRecommandedTestcases.getBT();
			if(iseRecommandedTestcases.getBuild() != null) {
				this.buildId =iseRecommandedTestcases.getBuild().getProductBuildId();
				this.buildName =iseRecommandedTestcases.getBuild().getBuildname();
				if(iseRecommandedTestcases.getBuild().getProductMaster() != null) {
					this.productId = iseRecommandedTestcases.getBuild().getProductMaster().getProductId();
					this.productName = iseRecommandedTestcases.getBuild().getProductMaster().getProductName();
				}
				
			}
		this.CT =iseRecommandedTestcases.getCT();
		this.defect_prob = iseRecommandedTestcases.getDefect_prob();
		this.ET = iseRecommandedTestcases.getET();
		this.feature = iseRecommandedTestcases.getFeature();
		this.GT = iseRecommandedTestcases.getGT();
		this.HFT = iseRecommandedTestcases.getHFT();
		this.LFT = iseRecommandedTestcases.getLFT();
		this.NT = iseRecommandedTestcases.getNT();
		this.picked = iseRecommandedTestcases.getPicked();
		this.probability = iseRecommandedTestcases.getProbability();
		this.rag_status = iseRecommandedTestcases.getRag_status();
		this.rag_value = iseRecommandedTestcases.getRag_value();
		this.recommendationCategory = iseRecommandedTestcases.getRecommendationCategory();
		
		this.ST = iseRecommandedTestcases.getST();
		this.standard_prob = iseRecommandedTestcases.getStandard_prob();
		this.testbed = iseRecommandedTestcases.getTestbed();
			if(iseRecommandedTestcases.getTestcase() != null){
				this.testcaseId = iseRecommandedTestcases.getTestcase().getTestCaseId();
				this.testCaseName = iseRecommandedTestcases.getTestcase().getTestCaseName();
			}
		}
		
		this.testRunPlanId = iseRecommandedTestcases.getTestRunPlanId();
		this.thresold_prob = iseRecommandedTestcases.getThresold_prob();
		this.title = iseRecommandedTestcases.getTitle();
		this.type = iseRecommandedTestcases.getType();
		this.UST = iseRecommandedTestcases.getUST();
		
		if(iseRecommandedTestcases.getPlanObtainTime() != null) {
			this.planUptainTime = DateUtility.dateToString(iseRecommandedTestcases.getPlanObtainTime());
		} else {
			this.planUptainTime ="";
		}
		
	}
	
	@JsonProperty
    private String testRunPlanId;
	public Integer getId() {
		return id;
	}
	public Integer getBuildId() {
		return buildId;
	}
	public String getBuildName() {
		return buildName;
	}
	public String getRecommendationCategory() {
		return recommendationCategory;
	}
	public String getRag_status() {
		return rag_status;
	}
	public String getST() {
		return ST;
	}
	public String getNT() {
		return NT;
	}
	public String getThresold_prob() {
		return thresold_prob;
	}
	public String getTitle() {
		return title;
	}
	public String[] getType() {
		return type;
	}
	public String getGT() {
		return GT;
	}
	public Integer getTestcaseId() {
		return testcaseId;
	}
	public String getTestCaseName() {
		return testCaseName;
	}
	public String getET() {
		return ET;
	}
	public String getStandard_prob() {
		return standard_prob;
	}
	public String getCT() {
		return CT;
	}
	public String getBT() {
		return BT;
	}
	public String getFeature() {
		return feature;
	}
	public String getProbability() {
		return probability;
	}
	public String getPicked() {
		return picked;
	}
	public String getDefect_prob() {
		return defect_prob;
	}
	public String getHFT() {
		return HFT;
	}
	public String getUST() {
		return UST;
	}
	public String getTestbed() {
		return testbed;
	}
	public String getLFT() {
		return LFT;
	}
	public String getRag_value() {
		return rag_value;
	}
	public String getTestRunPlanId() {
		return testRunPlanId;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public void setBuildId(Integer buildId) {
		this.buildId = buildId;
	}
	public void setBuildName(String buildName) {
		this.buildName = buildName;
	}
	public void setRecommendationCategory(String recommendationCategory) {
		this.recommendationCategory = recommendationCategory;
	}
	public void setRag_status(String rag_status) {
		this.rag_status = rag_status;
	}
	public void setST(String sT) {
		ST = sT;
	}
	public void setNT(String nT) {
		NT = nT;
	}
	public void setThresold_prob(String thresold_prob) {
		this.thresold_prob = thresold_prob;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public void setType(String[] type) {
		this.type = type;
	}
	public void setGT(String gT) {
		GT = gT;
	}
	public void setTestcaseId(Integer testcaseId) {
		this.testcaseId = testcaseId;
	}
	public void setTestCaseName(String testCaseName) {
		this.testCaseName = testCaseName;
	}
	public void setET(String eT) {
		ET = eT;
	}
	public void setStandard_prob(String standard_prob) {
		this.standard_prob = standard_prob;
	}
	public void setCT(String cT) {
		CT = cT;
	}
	public void setBT(String bT) {
		BT = bT;
	}
	public void setFeature(String feature) {
		this.feature = feature;
	}
	public void setProbability(String probability) {
		this.probability = probability;
	}
	public void setPicked(String picked) {
		this.picked = picked;
	}
	public void setDefect_prob(String defect_prob) {
		this.defect_prob = defect_prob;
	}
	public void setHFT(String hFT) {
		HFT = hFT;
	}
	public void setUST(String uST) {
		UST = uST;
	}
	public void setTestbed(String testbed) {
		this.testbed = testbed;
	}
	public void setLFT(String lFT) {
		LFT = lFT;
	}
	public void setRag_value(String rag_value) {
		this.rag_value = rag_value;
	}
	public void setTestRunPlanId(String testRunPlanId) {
		this.testRunPlanId = testRunPlanId;
	}
	public Integer getProductId() {
		return productId;
	}
	public String getProductName() {
		return productName;
	}
	public void setProductId(Integer productId) {
		this.productId = productId;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}

}
