package com.hcl.atf.taf.model;

// Generated Nov 25, 2014 3:21:16 PM by Hibernate Tools 3.4.0.CR1

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;

import static javax.persistence.GenerationType.IDENTITY;

import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;

/**
 * TestCaseList generated by hbm2java
 */
@Entity
@Table(name = "trcc_execution_plan_devices_association", uniqueConstraints = @UniqueConstraint(columnNames = "trccEPDAssociationId"))
public class TrccExecutionPlanDevicesAssociation implements java.io.Serializable {

	private Integer trccEPDAssociationId;
	
	private DeviceList deviceList;
	
	private TrccExecutionPlan trccExecutionPlan;
	
	private String isDefaultPlan;
	
	private Integer status;
	
	private Date statusChangeDate;

	public TrccExecutionPlanDevicesAssociation() {
	}
	
	public TrccExecutionPlanDevicesAssociation(Integer trccEPDAssociationId) {
		this.trccEPDAssociationId = trccEPDAssociationId;
	}
	
	
	public TrccExecutionPlanDevicesAssociation(DeviceList deviceList,
			 TrccExecutionPlan trccExecutionPlan, String isDefaultPlan) {
		this.deviceList = deviceList;
		this.trccExecutionPlan = trccExecutionPlan;
		this.isDefaultPlan = isDefaultPlan;
	}
	
	public TrccExecutionPlanDevicesAssociation(DeviceList deviceList,
			 TrccExecutionPlan trccExecutionPlan, String isDefaultPlan, 
			 Integer status, Date statusChangeDate) {
		this.deviceList = deviceList;
		this.trccExecutionPlan = trccExecutionPlan;
		this.isDefaultPlan = isDefaultPlan;
		this.status = status;
		this.statusChangeDate = statusChangeDate;
	}

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "trccEPDAssociationId", unique = true, nullable = false)
	public Integer getTrccEPDAssociationId() {
		return this.trccEPDAssociationId;
	}

	public void setTrccEPDAssociationId(Integer trccEPDAssociationId) {
		this.trccEPDAssociationId = trccEPDAssociationId;
	}
	
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "deviceListId")
	public DeviceList getDeviceList() {
		return this.deviceList;
	}

	public void setDeviceList(DeviceList deviceList) {
		this.deviceList = deviceList;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "trccExecutionPlanId")
	public TrccExecutionPlan getTrccExecutionPlan() {
		return this.trccExecutionPlan;
	}

	public void setTrccExecutionPlan(TrccExecutionPlan trccExecutionPlan) {
		this.trccExecutionPlan = trccExecutionPlan;
	}

	@Column(name = "isDefaultPlan")
	public String getIsDefaultPlan() {
		return this.isDefaultPlan;
	}

	public void setIsDefaultPlan(String isDefaultPlan) {
		this.isDefaultPlan = isDefaultPlan;
	}
	
	@Column(name = "status")
	public Integer getStatus() {
		return this.status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "statusChangeDate")
	public Date getStatusChangeDate() {
		return statusChangeDate;
	}

	public void setStatusChangeDate(Date statusChangeDate) {
		this.statusChangeDate = statusChangeDate;
	}
}
