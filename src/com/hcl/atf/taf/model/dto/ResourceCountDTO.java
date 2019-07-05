package com.hcl.atf.taf.model.dto;

import java.util.Date;

public class ResourceCountDTO {
	private Integer workPackageId;
	private Integer productId;
	private Date dateOfCount;
	private Integer shiftId;
	private Integer coreResourceCount;
	private Float blockedResourcesCount;
	
	public Integer getWorkPackageId() {
		return workPackageId;
	}
	public void setWorkPackageId(Integer workPackageId) {
		this.workPackageId = workPackageId;
	}
	public Date getDateOfCount() {
		return dateOfCount;
	}
	public void setDateOfCount(Date dateOfCount) {
		this.dateOfCount = dateOfCount;
	}
	public Integer getShiftId() {
		return shiftId;
	}
	public void setShiftId(Integer shiftId) {
		this.shiftId = shiftId;
	}
	public Integer getCoreResourceCount() {
		return coreResourceCount;
	}
	public void setCoreResourceCount(Integer coreResourceCount) {
		this.coreResourceCount = coreResourceCount;
	}
	public Float getBlockedResourcesCount() {
		return blockedResourcesCount;
	}
	public void setBlockedResourcesCount(Float blockedResourcesCount) {
		this.blockedResourcesCount = blockedResourcesCount;
	}
	public Integer getProductId() {
		return productId;
	}
	public void setProductId(Integer productId) {
		this.productId = productId;
	}
}
