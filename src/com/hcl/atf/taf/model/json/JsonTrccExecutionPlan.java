package com.hcl.atf.taf.model.json;


import java.text.SimpleDateFormat;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.hcl.atf.taf.model.TestRunConfigurationChild;
import com.hcl.atf.taf.model.TrccExecutionPlan;

public class JsonTrccExecutionPlan implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@JsonProperty
	private Integer trccExecutionPlanId;
	@JsonProperty
	private String planName;
	@JsonProperty
	private String description;
	@JsonProperty
	private String createdDate;
	@JsonProperty
	private String lastModifiedDate;
	@JsonProperty
	private Integer status;
	@JsonProperty
	private String statusChangeDate;
	@JsonProperty
	private Integer testRunConfigurationChildId;
	@JsonProperty
	private String isDefaultPlan;

	public JsonTrccExecutionPlan() {
	
	}
	public JsonTrccExecutionPlan(TrccExecutionPlan trccExecutionPlan) {
		
		trccExecutionPlanId=trccExecutionPlan.getTrccExecutionPlanId();
		if(trccExecutionPlan.getTestRunConfigurationChild()!=null)
			testRunConfigurationChildId = trccExecutionPlan.getTestRunConfigurationChild().getTestRunConfigurationChildId();
	
		planName=trccExecutionPlan.getPlanName();
		description=trccExecutionPlan.getDescription();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		if (trccExecutionPlan.getCreatedDate() != null)
			createdDate = sdf.format(trccExecutionPlan.getCreatedDate());
		if (trccExecutionPlan.getLastModifiedDate() != null)
			lastModifiedDate = sdf.format(trccExecutionPlan.getLastModifiedDate());
		status=trccExecutionPlan.getStatus();
		if (trccExecutionPlan.getStatusChangeDate() != null)
			statusChangeDate = sdf.format(trccExecutionPlan.getStatusChangeDate());
		
		this.isDefaultPlan = trccExecutionPlan.getIsDefaultPlan();
	}
	public Integer getTrccExecutionPlanId() {
		return trccExecutionPlanId;
	}
	public void setTrccExecutionPlanId(Integer trccExecutionPlanId) {
		this.trccExecutionPlanId = trccExecutionPlanId;
	}
	public String getPlanName() {
		return planName;
	}
	public void setPlanName(String planName) {
		this.planName = planName;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
	}
	public String getLastModifiedDate() {
		return lastModifiedDate;
	}
	public void setLastModifiedDate(String lastModifiedDate) {
		this.lastModifiedDate = lastModifiedDate;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public String getStatusChangeDate() {
		return statusChangeDate;
	}
	public void setStatusChangeDate(String statusChangeDate) {
		this.statusChangeDate = statusChangeDate;
	}
	public Integer getTestRunConfigurationChildId() {
		return testRunConfigurationChildId;
	}
	public void setTestRunConfigurationChildId(Integer testRunConfigurationChildId) {
		this.testRunConfigurationChildId = testRunConfigurationChildId;
	}
	
	public String getIsDefaultPlan() {
		return isDefaultPlan;
	}

	public void setIsDefaultPlan(String isDefaultPlan) {
		this.isDefaultPlan = isDefaultPlan;
	}
	
	@JsonIgnore
	public TrccExecutionPlan getTrccExecutionPlan(){
		TrccExecutionPlan trccExecutionPlan = new TrccExecutionPlan();
		trccExecutionPlan.setTrccExecutionPlanId(trccExecutionPlanId);
		trccExecutionPlan.setPlanName(planName);
		trccExecutionPlan.setDescription(description);
		trccExecutionPlan.setStatus(status);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		TestRunConfigurationChild testRunConfigurationChild = new TestRunConfigurationChild();
		testRunConfigurationChild.setTestRunConfigurationChildId(testRunConfigurationChildId);
		trccExecutionPlan.setTestRunConfigurationChild(testRunConfigurationChild);
		trccExecutionPlan.setIsDefaultPlan(isDefaultPlan);
		
		return trccExecutionPlan;
	}

}
