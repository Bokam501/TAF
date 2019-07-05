package com.hcl.atf.taf.model.json;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.hcl.atf.taf.controller.utilities.DateUtility;
import com.hcl.atf.taf.model.CustomFieldConfigMaster;
import com.hcl.atf.taf.model.CustomFieldGroupMaster;
import com.hcl.atf.taf.model.EntityMaster;
import com.hcl.atf.taf.model.UserList;

public class JsonCustomFieldConfigMaster {

	@JsonProperty
	private Integer id;
	@JsonProperty
	private Integer parentEntityId;
	@JsonProperty
	private String parentEntityName;
	@JsonProperty
	private Integer parentEntityInstanceId;
	@JsonProperty
	private Integer entityId;
	@JsonProperty
	private String entityName;
	@JsonProperty
	private Integer entityTypeId;
	@JsonProperty
	private String fieldName;
	@JsonProperty
	private String description;
	@JsonProperty
	private String dataType;
	@JsonProperty
	private String controlType;
	@JsonProperty
	private String isMandatory;
	@JsonProperty
	private String defaultValue;
	@JsonProperty
	private String upperLimit;
	@JsonProperty
	private String lowerLimit;
	@JsonProperty
	private String fieldOptions;
	@JsonProperty
	private String fieldOptionUrl;
	@JsonProperty
	private Integer fieldGroupId;
	@JsonProperty
	private String fieldGroupName;
	@JsonProperty
	private Integer displayOrder;
	@JsonProperty
	private String frequencyType;
	@JsonProperty
	private String frequency;
	@JsonProperty
	private Integer dependsOn;
	@JsonProperty
	private Integer isActive;
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
	
	public JsonCustomFieldConfigMaster() {
	
	}

	public JsonCustomFieldConfigMaster(CustomFieldConfigMaster customFieldConfigMaster) {
		this.id = customFieldConfigMaster.getId();
		if(customFieldConfigMaster.getParentEntity() != null){
			this.parentEntityId = customFieldConfigMaster.getParentEntity().getEntitymasterid();
			this.parentEntityName = customFieldConfigMaster.getParentEntity().getEntityDisplayName();
		}
		this.parentEntityInstanceId = customFieldConfigMaster.getParentEntityInstanceId();
		if(customFieldConfigMaster.getEntity() != null){
			this.entityId = customFieldConfigMaster.getEntity().getEntitymasterid();
			this.entityName = customFieldConfigMaster.getEntity().getEntityDisplayName();
		}
		this.entityTypeId = customFieldConfigMaster.getEntityType();
		this.fieldName = customFieldConfigMaster.getFieldName();
		this.description = customFieldConfigMaster.getDescription();
		this.dataType = customFieldConfigMaster.getDataType();
		this.controlType = customFieldConfigMaster.getControlType();
		this.isMandatory = customFieldConfigMaster.getIsMandatory();
		this.defaultValue = customFieldConfigMaster.getDefaultValue();
		this.upperLimit = customFieldConfigMaster.getUpperLimit();
		this.lowerLimit = customFieldConfigMaster.getLowerLimit();
		this.fieldOptions = customFieldConfigMaster.getFieldOptions();
		this.fieldOptionUrl = customFieldConfigMaster.getFieldOptionUrl();
		if(customFieldConfigMaster.getFieldGroup() != null){
			this.fieldGroupId = customFieldConfigMaster.getFieldGroup().getId();
			this.fieldGroupName = customFieldConfigMaster.getFieldGroup().getGroupName();
		}
		this.displayOrder = customFieldConfigMaster.getDisplayOrder();
		this.frequencyType = customFieldConfigMaster.getFrequencyType();
		this.frequency = customFieldConfigMaster.getFrequency();
		this.dependsOn = customFieldConfigMaster.getDependsOn();
		this.isActive = customFieldConfigMaster.getIsActive();
		if(customFieldConfigMaster.getCreatedBy() != null){
			this.createdById = customFieldConfigMaster.getCreatedBy().getUserId();
			this.createdByName = customFieldConfigMaster.getCreatedBy().getUserDisplayName();
		}
		if(customFieldConfigMaster.getCreatedOn() != null){
			this.createdOn = DateUtility.dateToStringWithSeconds1(customFieldConfigMaster.getCreatedOn());
		}else {
			this.createdOn = "dd-mm-yy";
		}
		if(customFieldConfigMaster.getModifiedBy() != null){
			this.modifiedById = customFieldConfigMaster.getModifiedBy().getUserId();
			this.modifiedByName = customFieldConfigMaster.getModifiedBy().getUserDisplayName();
		}
		if(customFieldConfigMaster.getModifiedOn() != null){
			this.modifiedOn = DateUtility.dateToStringWithSeconds1(customFieldConfigMaster.getModifiedOn());
		}else {
			this.modifiedOn = "dd-mm-yy";
		}
	}

	@JsonIgnore
	public CustomFieldConfigMaster getCustomFieldConfigMaster() {
		CustomFieldConfigMaster customFieldConfigMaster = new CustomFieldConfigMaster();
		if(this.id != null && this.id > 0){
			customFieldConfigMaster.setId(this.id);
		}
		if(this.parentEntityId != null && this.parentEntityId > 0){
			EntityMaster entityMaster = new EntityMaster();
			entityMaster.setEntitymasterid(this.parentEntityId);
			customFieldConfigMaster.setParentEntity(entityMaster);
		}
		customFieldConfigMaster.setParentEntityInstanceId(this.parentEntityInstanceId);
		if(this.entityId != null && this.entityId > 0){
			EntityMaster entityMaster = new EntityMaster();
			entityMaster.setEntitymasterid(this.entityId);
			customFieldConfigMaster.setEntity(entityMaster);
		}
		customFieldConfigMaster.setEntityType(this.entityTypeId);
		customFieldConfigMaster.setFieldName(this.fieldName);
		customFieldConfigMaster.setDescription(this.description);		
		customFieldConfigMaster.setDataType(this.dataType);
		customFieldConfigMaster.setControlType(this.controlType);
		if(this.isMandatory == null){
			customFieldConfigMaster.setIsMandatory("No");
		}else{
			customFieldConfigMaster.setIsMandatory(this.isMandatory);
		}
		customFieldConfigMaster.setDefaultValue(this.defaultValue);
		customFieldConfigMaster.setUpperLimit(this.upperLimit);
		customFieldConfigMaster.setLowerLimit(this.lowerLimit);
		customFieldConfigMaster.setFieldOptions(this.fieldOptions);
		customFieldConfigMaster.setFieldOptionUrl(this.fieldOptionUrl);
		if(this.fieldGroupId != null && this.fieldGroupId > 0){
			CustomFieldGroupMaster customFieldGroupMaster = new CustomFieldGroupMaster();
			customFieldGroupMaster.setId(this.fieldGroupId);
			customFieldConfigMaster.setFieldGroup(customFieldGroupMaster);
		}
		customFieldConfigMaster.setDisplayOrder(this.displayOrder);
		customFieldConfigMaster.setFrequencyType(this.frequencyType);
		customFieldConfigMaster.setFrequency(this.frequency);
		customFieldConfigMaster.setDependsOn(this.dependsOn);
		if(this.isActive != null){
			customFieldConfigMaster.setIsActive(this.isActive);
		}else{
			customFieldConfigMaster.setIsActive(1);
		}
		if(this.createdById != null && this.createdById > 0){
			UserList userList = new UserList();
			userList.setUserId(this.createdById);
			customFieldConfigMaster.setCreatedBy(userList);
		}
		if(this.createdOn != null && !this.createdOn.trim().isEmpty()){
			if(this.createdOn.equals("dd-mm-yy")){
				customFieldConfigMaster.setCreatedOn(new Date());				
			}else{ 
				customFieldConfigMaster.setCreatedOn(DateUtility.toFormatDate(this.createdOn));
			}
		}
		if(this.modifiedById != null && this.modifiedById > 0){
			UserList userList = new UserList();
			userList.setUserId(this.modifiedById);
			customFieldConfigMaster.setModifiedBy(userList);
		}
		if(this.modifiedOn != null && !this.modifiedOn.trim().isEmpty()){
			if(this.modifiedOn.equals("dd-mm-yy")){
				customFieldConfigMaster.setModifiedOn(new Date());				
			}else{ 
				customFieldConfigMaster.setModifiedOn(DateUtility.toFormatDate(this.modifiedOn));
			}
		}
		
		return customFieldConfigMaster;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getParentEntityId() {
		return parentEntityId;
	}

	public void setParentEntityId(Integer parentEntityId) {
		this.parentEntityId = parentEntityId;
	}

	public String getParentEntityName() {
		return parentEntityName;
	}

	public void setParentEntityName(String parentEntityName) {
		this.parentEntityName = parentEntityName;
	}

	public Integer getParentEntityInstanceId() {
		return parentEntityInstanceId;
	}

	public void setParentEntityInstanceId(Integer parentEntityInstanceId) {
		this.parentEntityInstanceId = parentEntityInstanceId;
	}

	public Integer getEntityId() {
		return entityId;
	}

	public void setEntityId(Integer entityId) {
		this.entityId = entityId;
	}

	public String getEntityName() {
		return entityName;
	}

	public void setEntityName(String entityName) {
		this.entityName = entityName;
	}

	public Integer getEntityTypeId() {
		return entityTypeId;
	}

	public void setEntityTypeId(Integer entityTypeId) {
		this.entityTypeId = entityTypeId;
	}

	public String getFieldName() {
		return fieldName;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getDataType() {
		return dataType;
	}

	public void setDataType(String dataType) {
		this.dataType = dataType;
	}

	public String getControlType() {
		return controlType;
	}

	public void setControlType(String controlType) {
		this.controlType = controlType;
	}

	public String getIsMandatory() {
		return isMandatory;
	}

	public void setIsMandatory(String isMandatory) {
		this.isMandatory = isMandatory;
	}

	public String getDefaultValue() {
		return defaultValue;
	}

	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}

	public String getUpperLimit() {
		return upperLimit;
	}

	public void setUpperLimit(String upperLimit) {
		this.upperLimit = upperLimit;
	}

	public String getLowerLimit() {
		return lowerLimit;
	}

	public void setLowerLimit(String lowerLimit) {
		this.lowerLimit = lowerLimit;
	}

	public String getFieldOptions() {
		return fieldOptions;
	}

	public void setFieldOptions(String fieldOptions) {
		this.fieldOptions = fieldOptions;
	}

	public String getFieldOptionUrl() {
		return fieldOptionUrl;
	}

	public void setFieldOptionUrl(String fieldOptionUrl) {
		this.fieldOptionUrl = fieldOptionUrl;
	}

	public Integer getFieldGroupId() {
		return fieldGroupId;
	}

	public void setFieldGroupId(Integer fieldGroupId) {
		this.fieldGroupId = fieldGroupId;
	}

	public String getFieldGroupName() {
		return fieldGroupName;
	}

	public void setFieldGroupName(String fieldGroupName) {
		this.fieldGroupName = fieldGroupName;
	}

	public Integer getDisplayOrder() {
		return displayOrder;
	}

	public void setDisplayOrder(Integer displayOrder) {
		this.displayOrder = displayOrder;
	}

	public String getFrequencyType() {
		return frequencyType;
	}

	public void setFrequencyType(String frequencyType) {
		this.frequencyType = frequencyType;
	}

	public String getFrequency() {
		return frequency;
	}

	public void setFrequency(String frequency) {
		this.frequency = frequency;
	}

	public Integer getDependsOn() {
		return dependsOn;
	}

	public void setDependsOn(Integer dependsOn) {
		this.dependsOn = dependsOn;
	}

	public Integer getIsActive() {
		return isActive;
	}

	public void setIsActive(Integer isActive) {
		this.isActive = isActive;
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
