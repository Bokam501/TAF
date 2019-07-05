package com.hcl.atf.taf.model.json;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.hcl.atf.taf.controller.utilities.DateUtility;
import com.hcl.atf.taf.model.EnvironmentGroup;

public class JsonEnvironmentGroup implements java.io.Serializable {

	private static final Log log = LogFactory
			.getLog(JsonEnvironmentGroup.class);

	
	@JsonProperty
	private Integer environmentGroupId;
	@JsonProperty
	private String environmentGroupName;
	@JsonProperty
	private String description;
	@JsonProperty
	private String createdDate;
	@JsonProperty
	private String modifiedDate;
	@JsonProperty
	private Integer status;
	@JsonProperty
	private String displayName;
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
	
	

	public JsonEnvironmentGroup() {
	}

	public JsonEnvironmentGroup(EnvironmentGroup environmentGroup) {
		this.environmentGroupId=environmentGroup.getEnvironmentGroupId();
		this.environmentGroupName=environmentGroup.getEnvironmentGroupName();
		this.description=environmentGroup.getDescription();
		if(environmentGroup.getCreatedDate()!=null )
			this.createdDate=DateUtility.dateformatWithOutTime(environmentGroup.getCreatedDate());
		if(environmentGroup.getModifiedDate()!=null)
			this.modifiedDate=DateUtility.dateformatWithOutTime(environmentGroup.getModifiedDate());
		this.status=environmentGroup.getStatus();
		this.displayName=environmentGroup.getDisplayName();
	}


	

	public Integer getEnvironmentGroupId() {
		return environmentGroupId;
	}

	public void setEnvironmentGroupId(Integer environmentGroupId) {
		this.environmentGroupId = environmentGroupId;
	}

	public String getEnvironmentGroupName() {
		return environmentGroupName;
	}

	public void setEnvironmentGroupName(String environmentGroupName) {
		this.environmentGroupName = environmentGroupName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
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

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	@JsonIgnore
	public EnvironmentGroup getEnvironmentGroup() {
		EnvironmentGroup environmentGroup = new EnvironmentGroup();
		environmentGroup.setEnvironmentGroupId(environmentGroupId);
		environmentGroup.setEnvironmentGroupName(environmentGroupName);
		environmentGroup.setDescription(description);
		if(createdDate == null || createdDate.isEmpty()){
			environmentGroup.setCreatedDate(DateUtility.getCurrentTime());
		}else{
			environmentGroup.setCreatedDate(DateUtility.dateformatWithOutTime(createdDate));
		}
		
		if(modifiedDate == null || modifiedDate.isEmpty()){
			environmentGroup.setModifiedDate(DateUtility.getCurrentTime());
		}else{
			environmentGroup.setModifiedDate(DateUtility.getCurrentTime());
		}
		
		environmentGroup.setStatus(status);
		environmentGroup.setDisplayName(displayName);
		return environmentGroup;
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
