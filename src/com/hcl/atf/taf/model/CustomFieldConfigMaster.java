package com.hcl.atf.taf.model;

import static javax.persistence.GenerationType.IDENTITY;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "custom_field_config_master")
public class CustomFieldConfigMaster implements Serializable, Cloneable {

	private static final long serialVersionUID = 1L;

	private Integer id;
	private EntityMaster parentEntity;
	private Integer parentEntityInstanceId;
	private EntityMaster entity;
	private Integer entityType;
	private String fieldName;
	private String description;
	private String dataType;
	private String controlType;
	private String isMandatory;
	private String defaultValue;
	private String upperLimit;
	private String lowerLimit;
	private String fieldOptions;
	private String fieldOptionUrl;
	private CustomFieldGroupMaster fieldGroup;
	private Integer displayOrder;
	private String frequencyType;
	private String frequency;
	private Integer dependsOn;
	private Integer isActive;
	private UserList createdBy;
	private Date createdOn;
	private UserList modifiedBy;
	private Date modifiedOn;
	
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "parentEntity")
	public EntityMaster getParentEntity() {
		return parentEntity;
	}
	public void setParentEntity(EntityMaster parentEntity) {
		this.parentEntity = parentEntity;
	}
	
	@Column(name = "parentEntityInstanceId")
	public Integer getParentEntityInstanceId() {
		return parentEntityInstanceId;
	}
	public void setParentEntityInstanceId(Integer parentEntityInstanceId) {
		this.parentEntityInstanceId = parentEntityInstanceId;
	}
		
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "entity")
	public EntityMaster getEntity() {
		return entity;
	}
	public void setEntity(EntityMaster entity) {
		this.entity = entity;
	}
	
	@Column(name = "entityType")
	public Integer getEntityType() {
		return entityType;
	}
	public void setEntityType(Integer entityType) {
		this.entityType = entityType;
	}
	
	@Column(name = "fieldName")
	public String getFieldName() {
		return fieldName;
	}
	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}
	
	@Column(name = "description")
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
	@Column(name = "dataType")
	public String getDataType() {
		return dataType;
	}
	public void setDataType(String dataType) {
		this.dataType = dataType;
	}
	
	@Column(name = "controlType")
	public String getControlType() {
		return controlType;
	}
	public void setControlType(String controlType) {
		this.controlType = controlType;
	}
	
	@Column(name = "isMandatory")
	public String getIsMandatory() {
		return isMandatory;
	}
	public void setIsMandatory(String isMandatory) {
		this.isMandatory = isMandatory;
	}
	
	@Column(name = "defaultValue")
	public String getDefaultValue() {
		return defaultValue;
	}
	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}
	
	@Column(name = "upperLimit")
	public String getUpperLimit() {
		return upperLimit;
	}
	public void setUpperLimit(String upperLimit) {
		this.upperLimit = upperLimit;
	}
	
	@Column(name = "lowerLimit")
	public String getLowerLimit() {
		return lowerLimit;
	}
	public void setLowerLimit(String lowerLimit) {
		this.lowerLimit = lowerLimit;
	}
	
	@Column(name = "fieldOptions")
	public String getFieldOptions() {
		return fieldOptions;
	}
	public void setFieldOptions(String fieldOptions) {
		this.fieldOptions = fieldOptions;
	}
	
	@Column(name = "fieldOptionUrl")
	public String getFieldOptionUrl() {
		return fieldOptionUrl;
	}
	public void setFieldOptionUrl(String fieldOptionUrl) {
		this.fieldOptionUrl = fieldOptionUrl;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "fieldGroup")
	public CustomFieldGroupMaster getFieldGroup() {
		return fieldGroup;
	}
	public void setFieldGroup(CustomFieldGroupMaster fieldGroup) {
		this.fieldGroup = fieldGroup;
	}
	
	@Column(name = "displayOrder")
	public Integer getDisplayOrder() {
		return displayOrder;
	}
	public void setDisplayOrder(Integer displayOrder) {
		this.displayOrder = displayOrder;
	}
	
	@Column(name = "frequencyType")
	public String getFrequencyType() {
		return frequencyType;
	}
	public void setFrequencyType(String frequencyType) {
		this.frequencyType = frequencyType;
	}
	
	@Column(name = "frequency")
	public String getFrequency() {
		return frequency;
	}
	public void setFrequency(String frequency) {
		this.frequency = frequency;
	}
	
	@Column(name = "dependsOn")
	public Integer getDependsOn() {
		return dependsOn;
	}
	public void setDependsOn(Integer dependsOn) {
		this.dependsOn = dependsOn;
	}
	
	@Column(name = "isActive")
	public Integer getIsActive() {
		return isActive;
	}
	public void setIsActive(Integer isActive) {
		this.isActive = isActive;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "createdBy")
	public UserList getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(UserList createdBy) {
		this.createdBy = createdBy;
	}
	
	@Column(name = "createdOn")
	public Date getCreatedOn() {
		return createdOn;
	}
	public void setCreatedOn(Date createdOn) {
		this.createdOn = createdOn;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "modifiedBy")
	public UserList getModifiedBy() {
		return modifiedBy;
	}
	public void setModifiedBy(UserList modifiedBy) {
		this.modifiedBy = modifiedBy;
	}
	
	@Column(name = "modifiedOn")
	public Date getModifiedOn() {
		return modifiedOn;
	}
	public void setModifiedOn(Date modifiedOn) {
		this.modifiedOn = modifiedOn;
	}
	
	public Object clone()throws CloneNotSupportedException{  
		return super.clone();  
	} 
}
