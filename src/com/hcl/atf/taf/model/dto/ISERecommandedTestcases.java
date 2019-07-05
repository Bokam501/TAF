/**
 * 
 */
package com.hcl.atf.taf.model.dto;

import static javax.persistence.GenerationType.IDENTITY;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.hcl.atf.taf.model.ProductBuild;
import com.hcl.atf.taf.model.TestCaseList;

/**
 * @author silambarasur
 *
 */
@Entity
@Table(name="ise_recommended_testcases")
public class ISERecommandedTestcases implements Serializable{


	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	 private Integer id;
	 private ProductBuild build;
	 private String recommendationCategory;
	 private String rag_status;
     private String ST;
     private String NT;
     private String thresold_prob;
     private String title;
     private String type[];
     private String GT;
     private TestCaseList testcase;
     private String ET;
     private String standard_prob;
     private String CT;
     private String BT;
     private String feature;
     private String probability;
     private String picked;
     private String defect_prob;
     private String HFT;
     private String UST;
     private String testbed;
     private String LFT;
     private String rag_value;
     private String testRunPlanId;
     private Date planObtainTime;
     
     
    
     @Id
  	@GeneratedValue(strategy = IDENTITY)
  	@Column(name = "id", unique = true, nullable = false)
     public Integer getId() {
  		return id;
  	}
  	
 	@ManyToOne(fetch = FetchType.LAZY)
 	@JoinColumn(name = "buildId")
  	public ProductBuild getBuild() {
  		return build;
  	}

 	@ManyToOne(fetch = FetchType.LAZY)
  	@JoinColumn(name = "testcaseId")
  	public TestCaseList getTestcase() {
  		return testcase;
  	}

  	@Column(name="recommendationCategory")
  	 public String getRecommendationCategory() {
  		return recommendationCategory;
  	}

  	@Column(name="rag_status")
 	public String getRag_status() {
 		return rag_status;
 	}
	
  	@Column(name="ST")
	public String getST() {
		return ST;
	}
  	
  	@Column(name="NT")
	public String getNT() {
		return NT;
	}
  	
  	@Column(name="thresold_prob")
	public String getThresold_prob() {
		return thresold_prob;
	}
  	
  	@Column(name="title")
	public String getTitle() {
		return title;
	}
  	
  	@Column(name="type")
	public String[] getType() {
		return type;
	}
  	
  	@Column(name="GT")
	public String getGT() {
		return GT;
	}
  	
  	@Column(name="ET")
	public String getET() {
		return ET;
	}
  	
  	@Column(name="standard_prob")
	public String getStandard_prob() {
		return standard_prob;
	}
  	
  	@Column(name="CT")
	public String getCT() {
		return CT;
	}
  	
  	@Column(name="BT")
	public String getBT() {
		return BT;
	}
  	
  	@Column(name="feature")
	public String getFeature() {
		return feature;
	}
  	
  	@Column(name="probability")
	public String getProbability() {
		return probability;
	}
  	
  	@Column(name="picked")
	public String getPicked() {
		return picked;
	}
  	
  	@Column(name="defect_prob")
	public String getDefect_prob() {
		return defect_prob;
	}
  	
  	@Column(name="HFT")
	public String getHFT() {
		return HFT;
	}
  	
  	
  	@Column(name="UST")
	public String getUST() {
		return UST;
	}
  	
  	@Column(name="testbed")
	public String getTestbed() {
		return testbed;
	}
  	
  	@Column(name="LFT")
	public String getLFT() {
		return LFT;
	}
  	
  	@Column(name="rag_value")
	public String getRag_value() {
		return rag_value;
	}
  	
  	@Column(name="testRunPlanId")
	public String getTestRunPlanId() {
		return testRunPlanId;
	}
  	
  	
	public void setId(Integer id) {
		this.id = id;
	}
	public void setBuild(ProductBuild build) {
		this.build = build;
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
	public void setTestcase(TestCaseList testcase) {
		this.testcase = testcase;
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

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "planObtainTime")
	public Date getPlanObtainTime() {
		return planObtainTime;
	}

	public void setPlanObtainTime(Date planObtainTime) {
		this.planObtainTime = planObtainTime;
	}
  	
}
