package com.hcl.atf.taf.model.json;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.hcl.atf.taf.controller.utilities.DateUtility;
import com.hcl.atf.taf.model.EntityConfigurationProperties;
import com.hcl.atf.taf.model.EntityConfigurationPropertiesMaster;
import com.hcl.atf.taf.model.EntityMaster;

public class JsonEntityConfigurationProperties {
	@JsonProperty
	private Integer entityConfigPropertyId;
	@JsonProperty
	private String value;
	@JsonProperty
	private Integer status;
	@JsonProperty
	private String createdDate;
	@JsonProperty
	private String modifiedDate;
	@JsonProperty
	private Integer entityId;
	@JsonProperty
	private Integer entityMasterId;
	@JsonProperty
	private Integer testRunPlanId;
	@JsonProperty
	private Integer entityConfigPropertiesMasterId;
	@JsonProperty
	private String options;
	@JsonProperty
	private Integer runconfigId;
	@JsonProperty
	private String runConfigName;
	@JsonProperty
	private String envCombName;
	@JsonProperty
	private String deviceName;
	@JsonProperty
	private String entityConfigPropertiesMasterName;
	
	@JsonProperty
	private Integer runConfigurationId;
	@JsonProperty
	private String runConfigurationName;
	@JsonProperty
	private String property;
	
	public JsonEntityConfigurationProperties(){
	}
	public JsonEntityConfigurationProperties(EntityConfigurationProperties entityConfigurationProperties){
		this.entityConfigPropertyId=entityConfigurationProperties.getEntityConfigPropertyId();
		this.value=entityConfigurationProperties.getValue();
		this.property = entityConfigurationProperties.getProperty();
		this.status=entityConfigurationProperties.getStatus();
		this.createdDate=DateUtility.dateToStringInSecond(entityConfigurationProperties.getCreatedDate());
		this.modifiedDate=DateUtility.dateToStringInSecond(entityConfigurationProperties.getModifiedDate());
		this.entityId=entityConfigurationProperties.getEntityId();
        this.entityConfigPropertiesMasterId=entityConfigurationProperties.getEntityConfigurationPropertiesMaster().getEntityConfigPropertiesMasterId();
        this.entityConfigPropertiesMasterName=entityConfigurationProperties.getEntityConfigurationPropertiesMaster().getName();
        this.options=entityConfigurationProperties.getValue();
        this.entityMasterId=entityConfigurationProperties.getEntityConfigurationPropertiesMaster().getEntityMaster().getEntitymasterid();
	}
	
		
	@JsonIgnore
	public EntityConfigurationProperties getEntityConfigurationProperties() {
		EntityConfigurationProperties entityConfigurationProperties=new EntityConfigurationProperties();
		entityConfigurationProperties.setEntityConfigPropertyId(entityConfigPropertyId);
		entityConfigurationProperties.setValue(value);
		entityConfigurationProperties.setProperty(property);
		entityConfigurationProperties.setStatus(status);
		entityConfigurationProperties.setEntityId(entityId);
		entityConfigurationProperties.setValue(options);
		if(createdDate!=null){
			entityConfigurationProperties.setCreatedDate(DateUtility.toDateInSec(createdDate));
		}else{
			entityConfigurationProperties.setCreatedDate(new Date());
		}
		
		entityConfigurationProperties.setModifiedDate(new Date());
		EntityConfigurationPropertiesMaster entityConfigurationPropertiesMaster=new EntityConfigurationPropertiesMaster();
		entityConfigurationPropertiesMaster.setEntityConfigPropertiesMasterId(entityConfigPropertiesMasterId);
		EntityMaster entityMaster=new EntityMaster();
		entityMaster.setEntitymasterid(entityMasterId);
		entityConfigurationPropertiesMaster.setEntityMaster(entityMaster);
		entityConfigurationProperties.setEntityConfigurationPropertiesMaster(entityConfigurationPropertiesMaster);
		return entityConfigurationProperties;
	}
	
	public Integer getEntityConfigPropertyId() {
		return entityConfigPropertyId;
	}
	public void setEntityConfigPropertyId(Integer entityConfigPropertyId) {
		this.entityConfigPropertyId = entityConfigPropertyId;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public String getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
	}
	public String getModifiedDate() {
		return modifiedDate;
	}
	public void setModifiedDate(String modifiedDate) {
		this.modifiedDate = modifiedDate;
	}
	public Integer getEntityId() {
		return entityId;
	}
	public void setEntityId(Integer entityId) {
		this.entityId = entityId;
	}
	public Integer getTestRunPlanId() {
		return testRunPlanId;
	}
	public void setTestRunPlanId(Integer testRunPlanId) {
		this.testRunPlanId = testRunPlanId;
	}
	public Integer getEntityConfigPropertiesMasterId() {
		return entityConfigPropertiesMasterId;
	}
	public void setEntityConfigPropertiesMasterId(
			Integer entityConfigPropertiesMasterId) {
		this.entityConfigPropertiesMasterId = entityConfigPropertiesMasterId;
	}
	public String getOptions() {
		return options;
	}
	public void setOptions(String options) {
		this.options = options;
	}
	public Integer getEntityMasterId() {
		return entityMasterId;
	}
	public void setEntityMasterId(Integer entityMasterId) {
		this.entityMasterId = entityMasterId;
	}
	public Integer getRunconfigId() {
		return runconfigId;
	}
	public void setRunconfigId(Integer runconfigId) {
		this.runconfigId = runconfigId;
	}
	public String getRunConfigName() {
		return runConfigName;
	}
	public void setRunConfigName(String runConfigName) {
		this.runConfigName = runConfigName;
	}
	public String getEnvCombName() {
		return envCombName;
	}
	public void setEnvCombName(String envCombName) {
		this.envCombName = envCombName;
	}
	public String getDeviceName() {
		return deviceName;
	}
	public void setDeviceName(String deviceName) {
		this.deviceName = deviceName;
	}
	public String getEntityConfigPropertiesMasterName() {
		return entityConfigPropertiesMasterName;
	}
	public void setEntityConfigPropertiesMasterName(
			String entityConfigPropertiesMasterName) {
		this.entityConfigPropertiesMasterName = entityConfigPropertiesMasterName;
	}
	
	public String getProperty() {
		return property;
	}
	public void setProperty(String property) {
		this.property = property;
	}
	
}
