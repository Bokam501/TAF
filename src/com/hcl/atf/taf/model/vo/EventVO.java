package com.hcl.atf.taf.model.vo;

import java.util.Date;

/**
 * Event  - ATF Event Object.
 */
public class EventVO {

	public String eventSourceComponent; // Referenced Constant
	public String eventName; // Referenced Constant
	public String eventDescription; // User provided
	public Long eventData; // Primary metric related to event
	public Long additonalEventData; // Metric related to event
	public Long additonalEventData2; // Metric related to event
	public String eventCategory; // Referenced Constant
	public String eventType; // Referenced Constant
	public String sourceEntityType; // Referenced Constant
	public Integer sourceEntityId; // Entity Reference from source System
	public String sourceEntityName; // Entity Reference from source System
	public String targetEntityType; // Referenced Constant
	public Integer targetEntityId; // Entity Reference from source System
	public String targetEntityName; // Entity Reference from source System
	public String additionalTargetEntityType; // Referenced Constant
	public Integer additionalTargetEntityId; // Entity Reference from source System
	public String additionalTargetEntityName; // Entity Reference from source System
	public Integer eventStatus; //Open(0) or closed(1) event
	public Date startTime;
	public Date endTime;

	public EventVO() {
		
	}
	
	public EventVO(String eventName) {
		
		this.eventName = eventName;
	}
	
	public EventVO(
			String eventSourceComponent,
			String eventName, String eventDescription, 
			Long eventData, Long additonalEventData, Long additonalEventData2, 
			String eventCategory, String eventType,
			String sourceEntitytype, Integer sourceEntityId, String sourceEntityName,
			String targetEntitytype, Integer targetEntityId, String targetEntityName,
			String additionalTargetEntityType, Integer additionalTargetEntityId, String additionalTargetEntityName, 
			Integer eventStatus,
			Date startTime, Date endTime) {

		this.eventSourceComponent = eventSourceComponent;
		this.eventName = eventName;
		this.eventDescription = eventDescription;
		this.eventData = eventData;
		this.additonalEventData = additonalEventData;
		this.additonalEventData2 = additonalEventData2;
		this.eventCategory = eventCategory;
		this.eventType = eventType;
		this.sourceEntityType = sourceEntityType;
		this.sourceEntityId = sourceEntityId;
		this.sourceEntityName = sourceEntityName;
		this.targetEntityType = targetEntityType;
		this.targetEntityId = targetEntityId;
		this.targetEntityName = targetEntityName;
		this.additionalTargetEntityType = additionalTargetEntityType;
		this.additionalTargetEntityId = additionalTargetEntityId;
		this.additionalTargetEntityName = additionalTargetEntityName;
		this.eventStatus = eventStatus;
		this.startTime = startTime;
		this.endTime = endTime;
	}

	public String getEventSourceComponent() {
		return eventSourceComponent;
	}
	public void setEventSourceComponent(String eventSourceComponent) {
		this.eventSourceComponent = eventSourceComponent;
	}
	public String getEventName() {
		return eventName;
	}
	public void setEventName(String eventName) {
		this.eventName = eventName;
	}
	public String getEventDescription() {
		return eventDescription;
	}
	public void setEventDescription(String eventDescription) {
		this.eventDescription = eventDescription;
	}
	public Long getEventData() {
		return eventData;
	}
	public void setEventData(Long eventData) {
		this.eventData = eventData;
	}
	public Long getAdditonalEventData() {
		return additonalEventData;
	}
	public void setAdditonalEventData(Long additonalEventData) {
		this.additonalEventData = additonalEventData;
	}
	public Long getAdditonalEventData2() {
		return additonalEventData2;
	}
	public void setAdditonalEventData2(Long additonalEventData2) {
		this.additonalEventData2 = additonalEventData2;
	}
	public String getEventCategory() {
		return eventCategory;
	}
	public void setEventCategory(String eventCategory) {
		this.eventCategory = eventCategory;
	}
	public String getEventType() {
		return eventType;
	}
	public void setEventType(String eventType) {
		this.eventType = eventType;
	}
	public String getSourceEntityType() {
		return sourceEntityType;
	}
	public void setSourceEntityType(String sourceEntityType) {
		this.sourceEntityType = sourceEntityType;
	}
	public Integer getSourceEntityId() {
		return sourceEntityId;
	}
	public void setSourceEntityId(Integer sourceEntityId) {
		this.sourceEntityId = sourceEntityId;
	}
	public String getSourceEntityName() {
		return sourceEntityName;
	}
	public void setSourceEntityName(String sourceEntityName) {
		this.sourceEntityName = sourceEntityName;
	}
	public String getTargetEntityType() {
		return targetEntityType;
	}
	public void setTargetEntityType(String targetEntityType) {
		this.targetEntityType = targetEntityType;
	}
	public Integer getTargetEntityId() {
		return targetEntityId;
	}
	public void setTargetEntityId(Integer targetEntityId) {
		this.targetEntityId = targetEntityId;
	}
	public String getTargetEntityName() {
		return targetEntityName;
	}
	public void setTargetEntityName(String targetEntityName) {
		this.targetEntityName = targetEntityName;
	}
	public String getAdditionalTargetEntityType() {
		return additionalTargetEntityType;
	}
	public void setAdditionalTargetEntityType(String additionalTargetEntityType) {
		this.additionalTargetEntityType = additionalTargetEntityType;
	}
	public Integer getAdditionalTargetEntityId() {
		return additionalTargetEntityId;
	}
	public void setAdditionalTargetEntityId(Integer additionalTargetEntityId) {
		this.additionalTargetEntityId = additionalTargetEntityId;
	}
	public String getAdditionalTargetEntityName() {
		return additionalTargetEntityName;
	}
	public void setAdditionalTargetEntityName(String additionalTargetEntityName) {
		this.additionalTargetEntityName = additionalTargetEntityName;
	}
	public Integer getEventStatus() {
		return eventStatus;
	}
	public void setEventStatus(Integer eventStatus) {
		this.eventStatus = eventStatus;
	}
	public Date getStartTime() {
		return startTime;
	}
	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}
	public Date getEndTime() {
		return endTime;
	}
	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
	
	@Override
	public boolean equals(Object obj) {
		
		if (obj == null)
			return false;
		EventVO eventVO = (EventVO) obj;
		if (this.eventName.equals(eventVO.getEventName()))
			return true;
		return false;
	}
}
