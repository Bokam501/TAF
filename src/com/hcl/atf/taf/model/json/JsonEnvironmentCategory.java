package com.hcl.atf.taf.model.json;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.hcl.atf.taf.controller.utilities.DateUtility;
import com.hcl.atf.taf.model.EnvironmentCategory;
import com.hcl.atf.taf.model.EnvironmentGroup;

public class JsonEnvironmentCategory implements java.io.Serializable {

	private static final Log log = LogFactory
			.getLog(JsonEnvironmentCategory.class);
	@JsonProperty
	private Integer environmentCategoryId;
	@JsonProperty
	private String environmentCategoryName;
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
	private String parentEnvironmentCategoryName;
	@JsonProperty
	private Integer parentEnvironmentCategoryId;
	@JsonProperty
	private Integer leftIndex;
	@JsonProperty
	private Integer rightIndex;
	@JsonProperty
	private Integer environmentGroupId;
	
	@JsonProperty
	private String environmentGroupName;
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
	
	public JsonEnvironmentCategory() {
	}

	public JsonEnvironmentCategory(EnvironmentCategory environmentCategory) {
		this.environmentCategoryId=environmentCategory.getEnvironmentCategoryId();
		this.environmentCategoryName=environmentCategory.getEnvironmentCategoryName();
		this.description=environmentCategory.getDescription();
		
		this.environmentGroupId=environmentCategory.getEnvironmentGroup().getEnvironmentGroupId();
		if(environmentCategory.getEnvironmentGroup().getEnvironmentGroupName()!=null ){
			this.environmentGroupName=environmentCategory.getEnvironmentGroup().getEnvironmentGroupName();
		}
		
		if(environmentCategory.getCreatedDate()!=null )
			this.createdDate=DateUtility.dateformatWithOutTime(environmentCategory.getCreatedDate());
		if(environmentCategory.getModifiedDate()!=null)
			this.modifiedDate=DateUtility.dateformatWithOutTime(environmentCategory.getModifiedDate());
		this.status=environmentCategory.getStatus();
		this.displayName=environmentCategory.getDisplayName();
		
		if(environmentCategory.getParentEnvironmentCategory()!=null){
			this.parentEnvironmentCategoryId=environmentCategory.getParentEnvironmentCategory().getEnvironmentCategoryId();
			this.parentEnvironmentCategoryName=environmentCategory.getParentEnvironmentCategory().getEnvironmentCategoryName();
		}
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

	
	public Integer getEnvironmentCategoryId() {
		return environmentCategoryId;
	}

	public void setEnvironmentCategoryId(Integer environmentCategoryId) {
		this.environmentCategoryId = environmentCategoryId;
	}

	public String getEnvironmentCategoryName() {
		return environmentCategoryName;
	}

	public void setEnvironmentCategoryName(String environmentCategoryName) {
		this.environmentCategoryName = environmentCategoryName;
	}

	public String getParentEnvironmentCategoryName() {
		return parentEnvironmentCategoryName;
	}

	public void setParentEnvironmentCategoryName(
			String parentEnvironmentCategoryName) {
		this.parentEnvironmentCategoryName = parentEnvironmentCategoryName;
	}

	public Integer getParentEnvironmentCategoryId() {
		return parentEnvironmentCategoryId;
	}

	public void setParentEnvironmentCategoryId(Integer parentEnvironmentCategoryId) {
		this.parentEnvironmentCategoryId = parentEnvironmentCategoryId;
	}

	public Integer getLeftIndex() {
		return leftIndex;
	}

	public void setLeftIndex(Integer leftIndex) {
		this.leftIndex = leftIndex;
	}

	public Integer getRightIndex() {
		return rightIndex;
	}

	public void setRightIndex(Integer rightIndex) {
		this.rightIndex = rightIndex;
	}

	@JsonIgnore
	public EnvironmentCategory getEnvironmentCategory() {
		EnvironmentCategory environmentCategory = new EnvironmentCategory();
		environmentCategory.setEnvironmentCategoryId(environmentCategoryId);
		environmentCategory.setEnvironmentCategoryName(environmentCategoryName);
		environmentCategory.setDescription(description);
		if(createdDate == null || createdDate.isEmpty()){
			environmentCategory.setCreatedDate(DateUtility.getCurrentTime());
		}else{
			environmentCategory.setCreatedDate(DateUtility.dateformatWithOutTime(createdDate));
		}
		
		if(modifiedDate == null || modifiedDate.isEmpty()){
			environmentCategory.setModifiedDate(DateUtility.getCurrentTime());
		}else{
			environmentCategory.setModifiedDate(DateUtility.getCurrentTime());
		}
		
		environmentCategory.setStatus(status);
		environmentCategory.setDisplayName(displayName);
		
		if(parentEnvironmentCategoryId!=null ){
			EnvironmentCategory parentEnvironmentCategory=  new EnvironmentCategory();
			parentEnvironmentCategory.setEnvironmentCategoryId(parentEnvironmentCategoryId);
			environmentCategory.setParentEnvironmentCategory(parentEnvironmentCategory);
		}
		
		if(environmentGroupId!=null){
			EnvironmentGroup environmentGroup = new EnvironmentGroup();
			environmentGroup.setEnvironmentGroupId(environmentGroupId);
			environmentCategory.setEnvironmentGroup(environmentGroup);
		}
		
		return environmentCategory;
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
