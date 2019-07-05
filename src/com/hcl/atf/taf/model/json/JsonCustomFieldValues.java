package com.hcl.atf.taf.model.json;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.hcl.atf.taf.controller.utilities.DateUtility;
import com.hcl.atf.taf.model.CustomFieldConfigMaster;
import com.hcl.atf.taf.model.CustomFieldValues;
import com.hcl.atf.taf.model.UserList;

public class JsonCustomFieldValues {

	@JsonProperty
	private Integer id;
	@JsonProperty
	private Integer customFieldId;
	@JsonProperty
	private String customFieldName;
	@JsonProperty
	private Integer entityInstanceId;
	@JsonProperty
	private String fieldValue;
	@JsonProperty
	private Integer frequencyOrder;
	@JsonProperty
	private Integer frequencyMonth;
	@JsonProperty
	private Integer frequencyYear;
	@JsonProperty
	private Integer createdById;
	@JsonProperty
	private String createdByName;
	@JsonProperty
	private String createdOn;
	@JsonProperty
	private Integer modifiedById;
	@JsonProperty
	private String modifiedByName;
	@JsonProperty
	private String modifiedOn;
	@JsonProperty
	private String modifiedField;
	@JsonProperty
	private String 	modifiedFieldTitle;	
	@JsonProperty
	private String oldFieldID;
	@JsonProperty
	private String	oldFieldValue;
	@JsonProperty
	private String	modifiedFieldID;
	@JsonProperty	
	private String modifiedFieldValue;
	
	public JsonCustomFieldValues() {
	
	}

	public JsonCustomFieldValues(CustomFieldValues customFieldValues) {
		this.id = customFieldValues.getId();
		if(customFieldValues != null){
			this.customFieldId = customFieldValues.getCustomFieldId().getId();
			this.customFieldName = customFieldValues.getCustomFieldId().getFieldName();
		}
		this.entityInstanceId = customFieldValues.getEntityInstanceId();
		this.fieldValue = customFieldValues.getFieldValue();
		this.frequencyOrder = customFieldValues.getFrequencyOrder();
		this.frequencyMonth = customFieldValues.getFrequencyMonth();
		this.frequencyYear = customFieldValues.getFrequencyYear();
		if(customFieldValues.getCreatedBy() != null){
			this.createdById = customFieldValues.getCreatedBy().getUserId();
			this.createdByName = customFieldValues.getCreatedBy().getUserDisplayName();
		}
		if(customFieldValues.getCreatedOn() != null){
			this.createdOn = DateUtility.dateToStringWithSeconds1(customFieldValues.getCreatedOn());
		}else {
			this.createdOn = "dd-mm-yy";
		}
		if(customFieldValues.getModifiedBy() != null){
			this.modifiedById = customFieldValues.getModifiedBy().getUserId();
			this.modifiedByName = customFieldValues.getModifiedBy().getUserDisplayName();
		}
		if(customFieldValues.getModifiedOn() != null){
			this.modifiedOn = DateUtility.dateToStringWithSeconds1(customFieldValues.getModifiedOn());
		}else {
			this.modifiedOn = "dd-mm-yy";
		}
	}

	@JsonIgnore
	public CustomFieldValues getCustomFieldValues() {
		CustomFieldValues customFieldValues = new CustomFieldValues();
		if(this.id != null && this.id > 0){
			customFieldValues.setId(this.id);
		}
		if(this.customFieldId != null && this.customFieldId > 0){
			CustomFieldConfigMaster customFieldConfigMaster = new  CustomFieldConfigMaster();
			customFieldConfigMaster.setId(this.customFieldId);
			customFieldValues.setCustomFieldId(customFieldConfigMaster);
		}
		customFieldValues.setEntityInstanceId(this.entityInstanceId);
		customFieldValues.setFieldValue(this.fieldValue);
		customFieldValues.setFrequencyOrder(this.frequencyOrder);
		customFieldValues.setFrequencyMonth(this.frequencyMonth);
		customFieldValues.setFrequencyYear(this.frequencyYear);
		if(this.createdById != null && this.createdById > 0){
			UserList userList = new UserList();
			userList.setUserId(this.createdById);
			customFieldValues.setCreatedBy(userList);
		}
		if(this.createdOn != null && !this.createdOn.trim().isEmpty()){
			if(this.createdOn.equals("mm/dd/yy")){
				customFieldValues.setCreatedOn(new Date());				
			}else{ 
				customFieldValues.setCreatedOn(DateUtility.toFormatDate(this.createdOn));
			}
		}
		if(this.modifiedById != null && this.modifiedById > 0){
			UserList userList = new UserList();
			userList.setUserId(this.modifiedById);
			customFieldValues.setModifiedBy(userList);
		}
		if(this.modifiedOn != null && !this.modifiedOn.trim().isEmpty()){
			if(this.modifiedOn.equals("mm/dd/yy")){
				customFieldValues.setModifiedOn(new Date());				
			}else{ 
				customFieldValues.setModifiedOn(DateUtility.toFormatDate(this.modifiedOn));
			}
		}
		
		return customFieldValues;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getCustomFieldId() {
		return customFieldId;
	}

	public void setCustomFieldId(Integer customFieldId) {
		this.customFieldId = customFieldId;
	}

	public String getCustomFieldName() {
		return customFieldName;
	}

	public void setCustomFieldName(String customFieldName) {
		this.customFieldName = customFieldName;
	}

	public Integer getEntityInstanceId() {
		return entityInstanceId;
	}

	public void setEntityInstanceId(Integer entityInstanceId) {
		this.entityInstanceId = entityInstanceId;
	}

	public String getFieldValue() {
		return fieldValue;
	}

	public void setFieldValue(String fieldValue) {
		this.fieldValue = fieldValue;
	}

	public Integer getFrequencyOrder() {
		return frequencyOrder;
	}

	public void setFrequencyOrder(Integer frequencyOrder) {
		this.frequencyOrder = frequencyOrder;
	}

	public Integer getFrequencyMonth() {
		return frequencyMonth;
	}

	public void setFrequencyMonth(Integer frequencyMonth) {
		this.frequencyMonth = frequencyMonth;
	}

	public Integer getFrequencyYear() {
		return frequencyYear;
	}

	public void setFrequencyYear(Integer frequencyYear) {
		this.frequencyYear = frequencyYear;
	}

	public Integer getCreatedById() {
		return createdById;
	}

	public void setCreatedById(Integer createdById) {
		this.createdById = createdById;
	}

	public String getCreatedByName() {
		return createdByName;
	}

	public void setCreatedByName(String createdByName) {
		this.createdByName = createdByName;
	}

	public String getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(String createdOn) {
		this.createdOn = createdOn;
	}

	public Integer getModifiedById() {
		return modifiedById;
	}

	public void setModifiedById(Integer modifiedById) {
		this.modifiedById = modifiedById;
	}

	public String getModifiedByName() {
		return modifiedByName;
	}

	public void setModifiedByName(String modifiedByName) {
		this.modifiedByName = modifiedByName;
	}

	public String getModifiedOn() {
		return modifiedOn;
	}

	public void setModifiedOn(String modifiedOn) {
		this.modifiedOn = modifiedOn;
	}

	public String getModifiedField() {
		return modifiedField;
	}

	public void setModifiedField(String modifiedField) {
		this.modifiedField = modifiedField;
	}

	public String getModifiedFieldTitle() {
		return modifiedFieldTitle;
	}

	public void setModifiedFieldTitle(String modifiedFieldTitle) {
		this.modifiedFieldTitle = modifiedFieldTitle;
	}

	public String getOldFieldID() {
		return oldFieldID;
	}

	public void setOldFieldID(String oldFieldID) {
		this.oldFieldID = oldFieldID;
	}

	public String getOldFieldValue() {
		return oldFieldValue;
	}

	public void setOldFieldValue(String oldFieldValue) {
		this.oldFieldValue = oldFieldValue;
	}

	public String getModifiedFieldID() {
		return modifiedFieldID;
	}

	public void setModifiedFieldID(String modifiedFieldID) {
		this.modifiedFieldID = modifiedFieldID;
	}

	public String getModifiedFieldValue() {
		return modifiedFieldValue;
	}

	public void setModifiedFieldValue(String modifiedFieldValue) {
		this.modifiedFieldValue = modifiedFieldValue;
	}

}
