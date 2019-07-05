package com.hcl.atf.taf.model.dto;

import java.util.Date;

public class WorkPackageDemandProjectionDTO {
	
	private Integer productId;
	private String productName;
	private Integer workPackageId;
	private String workPackageName;
	private Integer shiftId;
	private String shiftName;
	private Integer shiftTypeId;
	private String shiftTypeName;
	private Date workDate;
	private Float resourceCount;
	private Integer demandCount;
	private Integer reservationCount;
	private Integer shortageCount;
	private Integer availabilityCount;
	
	public Integer getProductId() {
		return productId;
	}
	public void setProductId(Integer productId) {
		this.productId = productId;
	}
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public Integer getWorkPackageId() {
		return workPackageId;
	}
	public void setWorkPackageId(Integer workPackageId) {
		this.workPackageId = workPackageId;
	}
	public String getWorkPackageName() {
		return workPackageName;
	}
	public void setWorkPackageName(String workPackageName) {
		this.workPackageName = workPackageName;
	}
	public Integer getShiftId() {
		return shiftId;
	}
	public void setShiftId(Integer shiftId) {
		this.shiftId = shiftId;
	}
	public String getShiftName() {
		return shiftName;
	}
	public void setShiftName(String shiftName) {
		this.shiftName = shiftName;
	}
	public Integer getShiftTypeId() {
		return shiftTypeId;
	}
	public void setShiftTypeId(Integer shiftTypeId) {
		this.shiftTypeId = shiftTypeId;
	}
	public String getShiftTypeName() {
		return shiftTypeName;
	}
	public void setShiftTypeName(String shiftTypeName) {
		this.shiftTypeName = shiftTypeName;
	}
	public Date getWorkDate() {
		return workDate;
	}
	public void setWorkDate(Date workDate) {
		this.workDate = workDate;
	}
	public Float getResourceCount() {
		return resourceCount;
	}
	public void setResourceCount(Float resourceCount) {
		this.resourceCount = resourceCount;
	}
	public Integer getDemandCount() {
		return demandCount;
	}
	public void setDemandCount(Integer demandCount) {
		this.demandCount = demandCount;
	}
	public Integer getReservationCount() {
		return reservationCount;
	}
	public void setReservationCount(Integer reservationCount) {
		this.reservationCount = reservationCount;
	}
	public Integer getShortageCount() {
		return shortageCount;
	}
	public void setShortageCount(Integer shortageCount) {
		this.shortageCount = shortageCount;
	}
	public Integer getAvailabilityCount() {
		return availabilityCount;
	}
	public void setAvailabilityCount(Integer availabilityCount) {
		this.availabilityCount = availabilityCount;
	}
	
	
}
