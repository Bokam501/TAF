package com.hcl.atf.taf.model.json;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.hcl.atf.taf.model.CustomFieldGroupMaster;

public class JsonCustomFieldGroupMaster {

	@JsonProperty
	private Integer id;
	@JsonProperty
	private String groupName;
	@JsonProperty
	private String description;
	@JsonProperty
	private Integer displayOrder;
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
	
	public JsonCustomFieldGroupMaster() {
	
	}

	public JsonCustomFieldGroupMaster(CustomFieldGroupMaster customFieldGroupMaster) {
		this.id = customFieldGroupMaster.getId();
		this.groupName = customFieldGroupMaster.getGroupName();
		this.description = customFieldGroupMaster.getDescription();
		this.displayOrder = customFieldGroupMaster.getDisplayOrder();
	}

	@JsonIgnore
	public CustomFieldGroupMaster getCustomFieldGroupMaster() {
		CustomFieldGroupMaster customFieldGroupMaster = new CustomFieldGroupMaster();
		
		customFieldGroupMaster.setId(this.id);
		customFieldGroupMaster.setGroupName(this.groupName);
		customFieldGroupMaster.setDescription(this.description);		
		customFieldGroupMaster.setDisplayOrder(this.displayOrder);
		
		return customFieldGroupMaster;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Integer getDisplayOrder() {
		return displayOrder;
	}

	public void setDisplayOrder(Integer displayOrder) {
		this.displayOrder = displayOrder;
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
