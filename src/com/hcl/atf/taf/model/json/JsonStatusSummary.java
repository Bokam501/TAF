package com.hcl.atf.taf.model.json;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.hcl.atf.taf.controller.utilities.DateUtility;
import com.hcl.atf.taf.model.dto.StatusSummaryDTO;

public class JsonStatusSummary {

	@JsonProperty
	private Integer dimensionId;
	@JsonProperty
	private String statusName;	
	@JsonProperty
	private String description;	
	@JsonProperty
	private Integer status;	
	@JsonProperty
	private Integer ownerId;
	@JsonProperty
	private String ownerName;
	@JsonProperty
	private String createdOn;
	@JsonProperty
	private Integer lastModifiedById;
	@JsonProperty
	private String lastModifiedByName;
	@JsonProperty
	private String lastModifiedDate;
	@JsonProperty
	private Integer numberOfPrimaryStatus;	
	@JsonProperty
	private Integer numberOfSecondaryStatus;	
	@JsonProperty
	private Integer numberOfPrimaryStatusHavingSecondaryStatus;
	@JsonProperty
	private Integer numberOfPrimaryStatusNotHavingSecondaryStatus;
	@JsonProperty
	private Integer numberOfSecondaryStatusMappedWithPrimaryStatus;
	@JsonProperty
	private Integer numberOfSecondaryStatusUnMappedWithPrimaryStatus;
	
	
	public JsonStatusSummary(){
		this.dimensionId = 0;
		this.statusName = "";	
		this.description = "";	
		this.status = 0;	
		this.ownerId = 0;
		this.ownerName = "";
		this.createdOn = "";
		this.lastModifiedById = 0;
		this.lastModifiedByName = "";
		this.lastModifiedDate = "";
		this.numberOfPrimaryStatus = 0;	
		this.numberOfSecondaryStatus = 0;	
		this.numberOfPrimaryStatusHavingSecondaryStatus = 0;
		this.numberOfPrimaryStatusNotHavingSecondaryStatus = 0;
		this.numberOfSecondaryStatusMappedWithPrimaryStatus = 0;
		this.numberOfSecondaryStatusUnMappedWithPrimaryStatus = 0;
	}
	
	public JsonStatusSummary(StatusSummaryDTO statusSummaryDTO){
		if(statusSummaryDTO.getDimensionMaster() != null){
			this.dimensionId = statusSummaryDTO.getDimensionMaster().getDimensionId();
			this.statusName = statusSummaryDTO.getDimensionMaster().getName();
			this.description = statusSummaryDTO.getDimensionMaster().getDescription();
			this.status = statusSummaryDTO.getDimensionMaster().getStatus();
			if(statusSummaryDTO.getDimensionMaster().getOwner() != null){
				this.ownerId = statusSummaryDTO.getDimensionMaster().getOwner().getUserId();
				this.ownerName = statusSummaryDTO.getDimensionMaster().getOwner().getUserDisplayName();
			}
			if(statusSummaryDTO.getDimensionMaster().getCreatedDate() != null){
				this.createdOn = DateUtility.dateToStringWithSeconds1(statusSummaryDTO.getDimensionMaster().getCreatedDate());
			}
			if(statusSummaryDTO.getDimensionMaster().getModifiedBy() != null){
				this.lastModifiedById = statusSummaryDTO.getDimensionMaster().getModifiedBy().getUserId();
				this.lastModifiedByName = statusSummaryDTO.getDimensionMaster().getModifiedBy().getUserDisplayName();
			}
			if(statusSummaryDTO.getDimensionMaster().getModifiedDate() != null){
				this.lastModifiedDate = DateUtility.dateToStringWithSeconds1(statusSummaryDTO.getDimensionMaster().getModifiedDate());
			}
		}
		
		this.numberOfPrimaryStatus = statusSummaryDTO.getNumberOfPrimaryStatus();
		this.numberOfSecondaryStatus = statusSummaryDTO.getNumberOfSecondaryStatus();
		this.numberOfPrimaryStatusHavingSecondaryStatus = statusSummaryDTO.getNumberOfPrimaryStatusHavingSecondaryStatus();
		this.numberOfPrimaryStatusNotHavingSecondaryStatus = statusSummaryDTO.getNumberOfPrimaryStatusNotHavingSecondaryStatus();
		this.numberOfSecondaryStatusMappedWithPrimaryStatus = statusSummaryDTO.getNumberOfSecondaryStatusMappedWithPrimaryStatus();
		this.numberOfSecondaryStatusUnMappedWithPrimaryStatus = statusSummaryDTO.getNumberOfSecondaryStatusUnMappedWithPrimaryStatus();
	}

	public Integer getDimensionId() {
		return dimensionId;
	}

	public void setDimensionId(Integer dimensionId) {
		this.dimensionId = dimensionId;
	}

	public String getStatusName() {
		return statusName;
	}

	public void setStatusName(String statusName) {
		this.statusName = statusName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getOwnerId() {
		return ownerId;
	}

	public void setOwnerId(Integer ownerId) {
		this.ownerId = ownerId;
	}

	public String getOwnerName() {
		return ownerName;
	}

	public void setOwnerName(String ownerName) {
		this.ownerName = ownerName;
	}

	public String getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(String createdOn) {
		this.createdOn = createdOn;
	}

	public Integer getLastModifiedById() {
		return lastModifiedById;
	}

	public void setLastModifiedById(Integer lastModifiedById) {
		this.lastModifiedById = lastModifiedById;
	}

	public String getLastModifiedByName() {
		return lastModifiedByName;
	}

	public void setLastModifiedByName(String lastModifiedByName) {
		this.lastModifiedByName = lastModifiedByName;
	}

	public String getLastModifiedDate() {
		return lastModifiedDate;
	}

	public void setLastModifiedDate(String lastModifiedDate) {
		this.lastModifiedDate = lastModifiedDate;
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
