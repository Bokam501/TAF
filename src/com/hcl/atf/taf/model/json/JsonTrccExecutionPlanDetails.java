package com.hcl.atf.taf.model.json;

import java.text.SimpleDateFormat;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import com.hcl.atf.taf.model.TrccExecutionPlanDetails;
import com.hcl.atf.taf.model.TrccExecutionPlan;
import com.hcl.atf.taf.model.DeviceList;
import com.hcl.atf.taf.model.TestCaseList;

public class JsonTrccExecutionPlanDetails implements java.io.Serializable {

	@JsonProperty
	private Integer trccExecutionPlanDetailsId;		
	@JsonProperty
	private Integer trccExecutionPlanId;
	@JsonProperty
	private Integer deviceListId;
	@JsonProperty
	private Integer testCaseId;
	@JsonProperty
	private Integer status;
	@JsonProperty
	private String statusChangeDate;

	
	public JsonTrccExecutionPlanDetails() {		
	}

	public JsonTrccExecutionPlanDetails(TrccExecutionPlanDetails trccExecutionPlanDetails) {
	
		this.trccExecutionPlanDetailsId = trccExecutionPlanDetails.getTrccExecutionPlanDetailsId();
		
		if(trccExecutionPlanDetails.getTrccExecutionPlan()!=null){
			this.trccExecutionPlanId = trccExecutionPlanDetails.getTrccExecutionPlan().getTrccExecutionPlanId();
		}
		
		if(trccExecutionPlanDetails.getDeviceList()!=null){
			this.deviceListId = trccExecutionPlanDetails.getDeviceList().getDeviceListId();
		}
		
		if(trccExecutionPlanDetails.getTestCaseList()!=null){
			this.testCaseId = trccExecutionPlanDetails.getTestCaseList().getTestCaseId();
		}

		status=trccExecutionPlanDetails.getStatus();
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		if (trccExecutionPlanDetails.getStatusChangeDate() != null)
			statusChangeDate = sdf.format(trccExecutionPlanDetails.getStatusChangeDate());

	}

	public Integer getTrccExecutionPlanDetailsId() {
		return trccExecutionPlanDetailsId;
	}

	public void setTrccExecutionPlanDetailsId(Integer trccExecutionPlanDetailsId) {
		this.trccExecutionPlanDetailsId = trccExecutionPlanDetailsId;
	}

	public Integer getTrccExecutionPlanId() {
		return trccExecutionPlanId;
	}

	public void setTrccExecutionPlanId(Integer trccExecutionPlanId) {
		this.trccExecutionPlanId = trccExecutionPlanId;
	}

	public Integer getDeviceListId() {
		return deviceListId;
	}

	public void setDeviceListId(Integer deviceListId) {
		this.deviceListId = deviceListId;
	}
	
	public Integer getTestCaseId() {
		return testCaseId;
	}

	public void setTestCaseId(Integer testCaseId) {
		this.testCaseId = testCaseId;
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

	@JsonIgnore
	public TrccExecutionPlanDetails getTrccExecutionPlanDetails(){
		TrccExecutionPlanDetails trccExecutionPlanDetails = new TrccExecutionPlanDetails();
		trccExecutionPlanDetails.setTrccExecutionPlanDetailsId(trccExecutionPlanDetailsId);
		
		if(trccExecutionPlanDetails.getTrccExecutionPlan()!=null){
			TrccExecutionPlan trccExecutionPlan = new TrccExecutionPlan();
			trccExecutionPlan.setTrccExecutionPlanId(trccExecutionPlanId);
			trccExecutionPlanDetails.setTrccExecutionPlan(trccExecutionPlan);
		}
		if(trccExecutionPlanDetails.getDeviceList()!=null){
			DeviceList deviceList = new DeviceList();
			deviceList.setDeviceListId(deviceListId);
			trccExecutionPlanDetails.setDeviceList(deviceList);
		}
		
		TestCaseList testCaseList = new TestCaseList();
		testCaseList.setTestCaseId(testCaseId);
		trccExecutionPlanDetails.setTestCaseList(testCaseList);
		
		trccExecutionPlanDetails.setStatus(status);
				
		return trccExecutionPlanDetails;
	}

}
