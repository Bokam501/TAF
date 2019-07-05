package com.hcl.atf.taf.model.json;


import java.text.SimpleDateFormat;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.hcl.atf.taf.model.Event;

public class JsonEvent implements java.io.Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@JsonProperty
	private Integer eventId; 
	@JsonProperty
	private String eventSourceComponent; 
	@JsonProperty
	private String eventName; 
	@JsonProperty
	private String eventDescription; 
	@JsonProperty
	private Double eventData; 
	@JsonProperty
	private Double additonalEventData; 
	@JsonProperty
	private Double additonalEventData2; 
	@JsonProperty
	private String eventCategory; 
	@JsonProperty
	private String eventType; 
	@JsonProperty
	private String sourceEntityType; 
	@JsonProperty
	private Integer sourceEntityId; 
	@JsonProperty
	private String sourceEntityName; 
	@JsonProperty
	private String targetEntityType;
	@JsonProperty
	private Integer targetEntityId; 
	@JsonProperty
	private String targetEntityName; 
	@JsonProperty
	private String additionalTargetEntityType; 
	@JsonProperty
	private Integer additionalTargetEntityId; 
	@JsonProperty
	private String additionalTargetEntityName; 
	@JsonProperty
	private Integer eventStatus; 
	@JsonProperty
	private String startTime;
	@JsonProperty
	private String endTime;
	@JsonProperty
	public Integer userId;
	@JsonProperty
	public String userName;
	@JsonProperty
	public Integer roleId;
	@JsonProperty
	public String roleLabel;
	@JsonProperty
	public String fieldName;
	@JsonProperty
	public String fieldValue;
	@JsonProperty
	public String newFieldValue;
	@JsonProperty
	private Boolean isModified;

	@JsonProperty
	public String result;
	
	public JsonEvent() {
	}
	public JsonEvent(Event event) {
		eventId=event.getEventId();
		eventSourceComponent=event.getEventSourceComponent();
		eventName=event.getEventName();
		eventDescription=event.getEventDescription();
		eventData=event.getEventData();
		additonalEventData=event.getAdditonalEventData();
		additonalEventData2=event.getAdditonalEventData2();
		eventCategory=event.getEventCategory();
		eventType=event.getEventType();
		sourceEntityType=event.getSourceEntityType();
		sourceEntityId=event.getSourceEntityId();
		sourceEntityName=event.getSourceEntityName();
		targetEntityType=event.getTargetEntityType();
		targetEntityId=event.getTargetEntityId();
		targetEntityName=event.getTargetEntityName();
		additionalTargetEntityType=event.getAdditionalTargetEntityType();
		additionalTargetEntityId=event.getAdditionalTargetEntityId();
		additionalTargetEntityName=event.getAdditionalTargetEntityName();
		eventStatus=event.getEventStatus();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		if(event.getStartTime() != null) {
			startTime=sdf.format(event.getStartTime());
		}
		if(event.getEndTime() != null) {
			endTime=sdf.format(event.getEndTime());
		}
		if(event.getUserId() != null)
			userId = event.getUserId();
		String userNameTxt = "";
		String roleLabelTxt = "";
		if(event.getUserName() != null)
			userNameTxt = event.getUserName();
		if(event.getRoleId() != null)
			roleId = event.getRoleId();
		if(event.getRoleLabel() != null)
			roleLabelTxt = event.getRoleLabel();
			roleLabel = event.getRoleLabel();
			
			userName = userNameTxt +"("+roleLabelTxt+")";
		if(event.getFieldName() != null)
			fieldName = event.getFieldName();
		if(event.getFieldValue() != null)
			fieldValue = event.getFieldValue();
		if(event.getNewFieldValue() != null)
			newFieldValue = event.getNewFieldValue();
	}
	public Integer getEventId() {
		return eventId;
	}
	public void setEventId(Integer eventId) {
		this.eventId = eventId;
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
	public Double getEventData() {
		return eventData;
	}
	public void setEventData(Double eventData) {
		this.eventData = eventData;
	}
	public Double getAdditonalEventData() {
		return additonalEventData;
	}
	public void setAdditonalEventData(Double additonalEventData) {
		this.additonalEventData = additonalEventData;
	}
	public Double getAdditonalEventData2() {
		return additonalEventData2;
	}
	public void setAdditonalEventData2(Double additonalEventData2) {
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
	public String getStartTime() {
		return startTime;
	}
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	public String getEndTime() {
		return endTime;
	}
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	public Integer getUserId() {
		return userId;
	}
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public Integer getRoleId() {
		return roleId;
	}
	public void setRoleId(Integer roleId) {
		this.roleId = roleId;
	}
	public String getRoleLabel() {
		return roleLabel;
	}
	public void setRoleLabel(String roleLabel) {
		this.roleLabel = roleLabel;
	}
	public String getFieldName() {
		return fieldName;
	}
	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}
	public String getFieldValue() {
		return fieldValue;
	}
	public void setFieldValue(String fieldValue) {
		this.fieldValue = fieldValue;
	}
	public String getNewFieldValue() {
		return newFieldValue;
	}
	public void setNewFieldValue(String newFieldValue) {
		this.newFieldValue = newFieldValue;
	}
	public Boolean getIsModified() {
		return isModified;
	}
	public void setIsModified(Boolean isModified) {
		this.isModified = isModified;
	}
	public String getResult() {
		return result;
	}
	public void setResult(String result) {
		this.result = result;
	}
	
}
