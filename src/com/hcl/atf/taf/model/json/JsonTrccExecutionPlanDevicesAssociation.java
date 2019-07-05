package com.hcl.atf.taf.model.json;

import java.text.SimpleDateFormat;
import java.text.ParseException;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
//import com.hcl.atf.taf.model.TestRunConfigurationChild;
import com.hcl.atf.taf.model.TrccExecutionPlan;
import com.hcl.atf.taf.model.DeviceList;
import com.hcl.atf.taf.model.TrccExecutionPlanDevicesAssociation;

public class JsonTrccExecutionPlanDevicesAssociation implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@JsonProperty
	private Integer trccEPDAssociationId;		
	
	@JsonProperty
	private Integer deviceListId;
	@JsonProperty
	private Integer trccExecutionPlanId;
	@JsonProperty
	private String isDefaultPlan;
	@JsonProperty
	private Integer status;
	@JsonProperty
	private String statusChangeDate;
	
	public JsonTrccExecutionPlanDevicesAssociation() {		
	}

	public JsonTrccExecutionPlanDevicesAssociation(TrccExecutionPlanDevicesAssociation trccExecutionPlanDevicesAssociation) {
	
		this.trccEPDAssociationId = trccExecutionPlanDevicesAssociation.getTrccEPDAssociationId();
		if(trccExecutionPlanDevicesAssociation.getDeviceList()!=null){
			this.deviceListId = trccExecutionPlanDevicesAssociation.getDeviceList().getDeviceListId();
		}
		
		if(trccExecutionPlanDevicesAssociation.getTrccExecutionPlan()!=null){
			this.trccExecutionPlanId = trccExecutionPlanDevicesAssociation.getTrccExecutionPlan().getTrccExecutionPlanId();
		}
		
		this.isDefaultPlan = trccExecutionPlanDevicesAssociation.getIsDefaultPlan();
		
		this.status = trccExecutionPlanDevicesAssociation.getStatus();
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		if (trccExecutionPlanDevicesAssociation.getStatusChangeDate() != null)
			statusChangeDate = sdf.format(trccExecutionPlanDevicesAssociation.getStatusChangeDate());
	}

	public Integer getTrccEPDAssociationId() {
		return trccEPDAssociationId;
	}

	public void setTrccEPDAssociationId(Integer trccEPDAssociationId) {
		this.trccEPDAssociationId = trccEPDAssociationId;
	}

	public Integer getDeviceListId() {
		return deviceListId;
	}

	public void setDeviceListId(Integer deviceListId) {
		this.deviceListId = deviceListId;
	}

	public Integer getTrccExecutionPlanId() {
		return trccExecutionPlanId;
	}

	public void setTrccExecutionPlanId(Integer trccExecutionPlanId) {
		this.trccExecutionPlanId = trccExecutionPlanId;
	}
	
	public String getIsDefaultPlan() {
		return isDefaultPlan;
	}

	public void setIsDefaultPlan(String isDefaultPlan) {
		this.isDefaultPlan = isDefaultPlan;
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
	public TrccExecutionPlanDevicesAssociation getTrccExecutionPlanDevicesAssociation(){
		TrccExecutionPlanDevicesAssociation trccExecutionPlanDevicesAssociation = new TrccExecutionPlanDevicesAssociation();
		trccExecutionPlanDevicesAssociation.setTrccEPDAssociationId(trccEPDAssociationId);
		DeviceList deviceList = new DeviceList();
		deviceList.setDeviceListId(deviceListId);
		trccExecutionPlanDevicesAssociation.setDeviceList(deviceList);

		TrccExecutionPlan trccExecutionPlan = new TrccExecutionPlan();
		trccExecutionPlan.setTrccExecutionPlanId(trccExecutionPlanId);
		trccExecutionPlanDevicesAssociation.setTrccExecutionPlan(trccExecutionPlan);

		trccExecutionPlanDevicesAssociation.setIsDefaultPlan(isDefaultPlan);
		
		trccExecutionPlanDevicesAssociation.setStatus(status);
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		if (statusChangeDate != null && !statusChangeDate.isEmpty())
		try {
			trccExecutionPlan.setStatusChangeDate(sdf.parse(statusChangeDate));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		return trccExecutionPlanDevicesAssociation;
	}

}
