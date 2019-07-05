package com.hcl.atf.taf.model.json;

import com.fasterxml.jackson.annotation.JsonProperty;

public class JsonDashBoard implements java.io.Serializable {
	
	@JsonProperty
	private Float scheduleVariance ;
	@JsonProperty
	private Float productQualityIndex ;
	@JsonProperty
	private Float riskIndex ;
	@JsonProperty
	private Float utlizationIndex ;
	@JsonProperty
	private Float healthIndex ;
	@JsonProperty
	private Float scheduleVarianceTarget ;
	@JsonProperty
	private Float productQualityIndexTarget ;
	@JsonProperty
	private Float riskIndexTarget ;
	@JsonProperty
	private Float utlizationIndexTarget ;
	@JsonProperty
	private Float healthIndexTarget ;
	
	
	public JsonDashBoard(){
		scheduleVariance=-5F;
		productQualityIndex=90F;
		riskIndex=0.9f;
		utlizationIndex=80F;
		healthIndex=99F;
		
		// To be read from property file and set for the following fields
		scheduleVarianceTarget = 0F;
		productQualityIndexTarget = 96F;
		riskIndexTarget = 1F ;
		utlizationIndexTarget = 85F ;
		healthIndexTarget = 98F;
		
	}

	

	public JsonDashBoard(Float scheduleVariance, Float productQualityIndex,
			Float riskIndex, Float utlizationIndex, Float healthIndex,
			Float scheduleVarianceTarget, Float productQualityIndexTarget,
			Float riskIndexTarget, Float utlizationIndexTarget,
			Float healthIndexTarget) {

		this.scheduleVariance = scheduleVariance;
		this.productQualityIndex = productQualityIndex;
		this.riskIndex = riskIndex;
		this.utlizationIndex = utlizationIndex;
		this.healthIndex = healthIndex;
		this.scheduleVarianceTarget = scheduleVarianceTarget;
		this.productQualityIndexTarget = productQualityIndexTarget;
		this.riskIndexTarget = riskIndexTarget;
		this.utlizationIndexTarget = utlizationIndexTarget;
		this.healthIndexTarget = healthIndexTarget;
	}



	public Float getScheduleVariance() {
		return scheduleVariance;
	}

	public void setScheduleVariance(Float scheduleVariance) {
		this.scheduleVariance = scheduleVariance;
	}

	public Float getProductQualityIndex() {
		return productQualityIndex;
	}

	public void setProductQualityIndex(Float productQualityIndex) {
		this.productQualityIndex = productQualityIndex;
	}

	public Float getRiskIndex() {
		return riskIndex;
	}

	public void setRiskIndex(Float riskIndex) {
		this.riskIndex = riskIndex;
	}

	public Float getUtlizationIndex() {
		return utlizationIndex;
	}

	public void setUtlizationIndex(Float utlizationIndex) {
		this.utlizationIndex = utlizationIndex;
	}

	public Float getHealthIndex() {
		return healthIndex;
	}

	public void setHealthIndex(Float healthIndex) {
		this.healthIndex = healthIndex;
	}

	public Float getScheduleVarianceTarget() {
		return scheduleVarianceTarget;
	}

	public void setScheduleVarianceTarget(Float scheduleVarianceTarget) {
		this.scheduleVarianceTarget = scheduleVarianceTarget;
	}

	public Float getProductQualityIndexTarget() {
		return productQualityIndexTarget;
	}

	public void setProductQualityIndexTarget(Float productQualityIndexTarget) {
		this.productQualityIndexTarget = productQualityIndexTarget;
	}

	public Float getRiskIndexTarget() {
		return riskIndexTarget;
	}

	public void setRiskIndexTarget(Float riskIndexTarget) {
		this.riskIndexTarget = riskIndexTarget;
	}

	public Float getUtlizationIndexTarget() {
		return utlizationIndexTarget;
	}

	public void setUtlizationIndexTarget(Float utlizationIndexTarget) {
		this.utlizationIndexTarget = utlizationIndexTarget;
	}

	public Float getHealthIndexTarget() {
		return healthIndexTarget;
	}

	public void setHealthIndexTarget(Float healthIndexTarget) {
		this.healthIndexTarget = healthIndexTarget;
	}
}