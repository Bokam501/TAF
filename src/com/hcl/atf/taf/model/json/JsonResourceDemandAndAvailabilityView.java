package com.hcl.atf.taf.model.json;

import com.fasterxml.jackson.annotation.JsonProperty;

public class JsonResourceDemandAndAvailabilityView {
	
	@JsonProperty
	private Integer resourceDemandAvailabilityViewId;
	@JsonProperty
	private String date;
	@JsonProperty
	private Integer demandCount;
	@JsonProperty
	private Integer availabilityCount;
	@JsonProperty
	private Integer reservationCount;
	@JsonProperty
	private Integer shortageCount;
	@JsonProperty
	private String start;
	@JsonProperty
	private String title;
	
	public JsonResourceDemandAndAvailabilityView(){
		demandCount = 0;
		availabilityCount = 0;
		reservationCount = 0;
		shortageCount = 0;
	}
	
	public Integer getResourceDemandAvailabilityViewId() {
		return resourceDemandAvailabilityViewId;
	}
	public void setResourceDemandAvailabilityViewId(
			Integer resourceDemandAvailabilityViewId) {
		this.resourceDemandAvailabilityViewId = resourceDemandAvailabilityViewId;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public Integer getDemandCount() {
		return demandCount;
	}
	public void setDemandCount(Integer demandCount) {
		this.demandCount = demandCount;
	}
	public Integer getAvailabilityCount() {
		return availabilityCount;
	}
	public void setAvailabilityCount(Integer availabilityCount) {
		this.availabilityCount = availabilityCount;
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

	public String getStart() {
		return start;
	}

	public void setStart(String start) {
		this.start = start;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
}
