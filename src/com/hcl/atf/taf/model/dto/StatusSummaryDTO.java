package com.hcl.atf.taf.model.dto;

import com.hcl.atf.taf.model.DimensionMaster;

public class StatusSummaryDTO {
	
	private DimensionMaster dimensionMaster;
	private Integer numberOfPrimaryStatus;	
	private Integer numberOfSecondaryStatus;	
	private Integer numberOfPrimaryStatusHavingSecondaryStatus;
	private Integer numberOfPrimaryStatusNotHavingSecondaryStatus;
	private Integer numberOfSecondaryStatusMappedWithPrimaryStatus;
	private Integer numberOfSecondaryStatusUnMappedWithPrimaryStatus;
	
	public DimensionMaster getDimensionMaster() {
		return dimensionMaster;
	}
	public void setDimensionMaster(DimensionMaster dimensionMaster) {
		this.dimensionMaster = dimensionMaster;
	}
	public Integer getNumberOfPrimaryStatus() {
		return numberOfPrimaryStatus;
	}
	public void setNumberOfPrimaryStatus(Integer numberOfPrimaryStatus) {
		this.numberOfPrimaryStatus = numberOfPrimaryStatus;
	}
	public Integer getNumberOfSecondaryStatus() {
		return numberOfSecondaryStatus;
	}
	public void setNumberOfSecondaryStatus(Integer numberOfSecondaryStatus) {
		this.numberOfSecondaryStatus = numberOfSecondaryStatus;
	}
	public Integer getNumberOfPrimaryStatusHavingSecondaryStatus() {
		return numberOfPrimaryStatusHavingSecondaryStatus;
	}
	public void setNumberOfPrimaryStatusHavingSecondaryStatus(
			Integer numberOfPrimaryStatusHavingSecondaryStatus) {
		this.numberOfPrimaryStatusHavingSecondaryStatus = numberOfPrimaryStatusHavingSecondaryStatus;
	}
	public Integer getNumberOfPrimaryStatusNotHavingSecondaryStatus() {
		return numberOfPrimaryStatusNotHavingSecondaryStatus;
	}
	public void setNumberOfPrimaryStatusNotHavingSecondaryStatus(
			Integer numberOfPrimaryStatusNotHavingSecondaryStatus) {
		this.numberOfPrimaryStatusNotHavingSecondaryStatus = numberOfPrimaryStatusNotHavingSecondaryStatus;
	}
	public Integer getNumberOfSecondaryStatusMappedWithPrimaryStatus() {
		return numberOfSecondaryStatusMappedWithPrimaryStatus;
	}
	public void setNumberOfSecondaryStatusMappedWithPrimaryStatus(
			Integer numberOfSecondaryStatusMappedWithPrimaryStatus) {
		this.numberOfSecondaryStatusMappedWithPrimaryStatus = numberOfSecondaryStatusMappedWithPrimaryStatus;
	}
	public Integer getNumberOfSecondaryStatusUnMappedWithPrimaryStatus() {
		return numberOfSecondaryStatusUnMappedWithPrimaryStatus;
	}
	public void setNumberOfSecondaryStatusUnMappedWithPrimaryStatus(
			Integer numberOfSecondaryStatusUnMappedWithPrimaryStatus) {
		this.numberOfSecondaryStatusUnMappedWithPrimaryStatus = numberOfSecondaryStatusUnMappedWithPrimaryStatus;
	}
}
