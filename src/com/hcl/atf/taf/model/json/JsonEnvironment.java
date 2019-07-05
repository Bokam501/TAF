package com.hcl.atf.taf.model.json;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.hcl.atf.taf.controller.utilities.DateUtility;
import com.hcl.atf.taf.model.Environment;
import com.hcl.atf.taf.model.EnvironmentCategory;
import com.hcl.atf.taf.model.EnvironmentGroup;
import com.hcl.atf.taf.model.ProductMaster;

public class JsonEnvironment implements java.io.Serializable {

	private static final Log log = LogFactory.getLog(JsonEnvironment.class);	
	@JsonProperty
	private Integer environmentId;
	@JsonProperty
	private String environmentName;
	@JsonProperty
	private String description;
	@JsonProperty
	private Integer isStandAloneEnvironment;
	@JsonProperty
	private String createdDate;
	@JsonProperty
	private String modifiedDate;
	@JsonProperty
	private Integer productMasterId;
	@JsonProperty
	private Integer status;
	@JsonProperty
	private Integer environmentCategoryId;
	@JsonProperty
	private String environmentCategoryName;
	@JsonProperty
	private String environmentCategoryDisplayName;
	@JsonProperty
	private Integer environmentGroupId;
	@JsonProperty
	private String environmentGroupName;
	@JsonProperty
	private Integer isSelected;
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
	
	public JsonEnvironment() {
	}

	public JsonEnvironment(Environment environment) {
		
		this.environmentId = environment.getEnvironmentId();
		this.environmentName = environment.getEnvironmentName();
		this.description = environment.getDescription();
		
		if(environment.getCreatedDate()!=null){
			//this.createdDate = DateUtility.dateToStringInSecond(environment.getCreatedDate());
			this.createdDate = DateUtility.dateformatWithOutTime(environment.getCreatedDate());
		}
		
		if(environment.getModifiedDate()!=null){
			//this.modifiedDate = DateUtility.dateToStringInSecond(environment.getModifiedDate());
			this.modifiedDate = DateUtility.dateformatWithOutTime(environment.getModifiedDate());
		}
		
		if(environment.getStatus() != null){
			this.status = environment.getStatus();
		}
		if(environment.getProductMaster() !=null){
			productMasterId = environment.getProductMaster().getProductId();
		}	
		if(environment.getEnvironmentCategory()!=null){
			environmentCategoryId=environment.getEnvironmentCategory().getEnvironmentCategoryId();
			environmentCategoryName=environment.getEnvironmentCategory().getEnvironmentCategoryName();
			environmentCategoryDisplayName=environment.getEnvironmentCategory().getDisplayName();
		}
		if(environment.getEnvironmentCategory()!=null && environment.getEnvironmentCategory().getEnvironmentGroup()!=null){
			environmentGroupId=environment.getEnvironmentCategory().getEnvironmentGroup().getEnvironmentGroupId();
			environmentGroupName=environment.getEnvironmentCategory().getEnvironmentGroup().getEnvironmentGroupName();
		}
		if (environment.getIsStandAloneEnvironment() == null || environment.getIsStandAloneEnvironment() < 0)
			this.isStandAloneEnvironment = 0;
		else
			this.isStandAloneEnvironment = environment.getIsStandAloneEnvironment();
	}
	
	public JsonEnvironment(Environment environment,String result) {
		
		this.environmentId = environment.getEnvironmentId();
		this.environmentName = environment.getEnvironmentName();
		this.description = environment.getDescription();
		
		/*if(environment.getCreatedDate()!=null)
			this.createdDate = DateUtility.dateToStringInSecond(environment.getCreatedDate());
		
		if(environment.getModifiedDate()!=null)
			this.modifiedDate = DateUtility.dateToStringInSecond(environment.getModifiedDate());*/
		
		if(environment.getCreatedDate()!=null){
			//this.createdDate = DateUtility.dateToStringInSecond(environment.getCreatedDate());
			this.createdDate = DateUtility.dateformatWithOutTime(environment.getCreatedDate());
		}
		
		if(environment.getModifiedDate()!=null){
			//this.modifiedDate = DateUtility.dateToStringInSecond(environment.getModifiedDate());
			this.modifiedDate = DateUtility.dateformatWithOutTime(environment.getModifiedDate());
		}
		
		if(environment.getStatus() != null){
			this.status = environment.getStatus();
		}
		if(environment.getProductMaster() !=null){
			productMasterId = environment.getProductMaster().getProductId();
		}	
		if(environment.getEnvironmentCategory()!=null){
			environmentCategoryId=environment.getEnvironmentCategory().getEnvironmentCategoryId();
			environmentCategoryName=environment.getEnvironmentCategory().getEnvironmentCategoryName();
			environmentCategoryDisplayName=environment.getEnvironmentCategory().getDisplayName();
		}
		if(environment.getEnvironmentCategory()!=null && environment.getEnvironmentCategory().getEnvironmentGroup()!=null){
			environmentGroupId=environment.getEnvironmentCategory().getEnvironmentGroup().getEnvironmentGroupId();
			environmentGroupName=environment.getEnvironmentCategory().getEnvironmentGroup().getEnvironmentGroupName();
		}
		if(result.equalsIgnoreCase("Selected")){
			isSelected=1;
		}else if(result.equalsIgnoreCase("NotSelected")){
			isSelected=0;
		}
		if (environment.getIsStandAloneEnvironment() == null || environment.getIsStandAloneEnvironment() < 0)
			this.isStandAloneEnvironment = 0;
		else
			this.isStandAloneEnvironment = environment.getIsStandAloneEnvironment();
	}
	
	@JsonIgnore
	public Environment getEnvironment() {
		Environment environment = new Environment();
		if (environmentId != null) {
			environment.setEnvironmentId(environmentId);
		}
		
		environment.setEnvironmentName(environmentName);
		environment.setDescription(description);
		
		/*if(createdDate!=null){
			environment.setCreatedDate(DateUtility.dateFormatWithOutSeconds(createdDate));
		}else{
			environment.setCreatedDate(DateUtility.getCurrentTime());
		}*/
		if(this.createdDate == null || this.createdDate.trim().isEmpty()) {
			environment.setCreatedDate(DateUtility.getCurrentTime());
		}else{			
			environment.setCreatedDate(DateUtility.dateformatWithOutTime(this.createdDate));						
		}
		
		environment.setModifiedDate(DateUtility.getCurrentTime());
		
		if (productMasterId != null) {
			ProductMaster product = new ProductMaster();
			product.setProductId(productMasterId);
			environment.setProductMaster(product);//Product object is passed not ID.
		}	
		if(this.status != null ){			
			environment.setStatus(status);			
		}else{
			environment.setStatus(0);	
		}
		
		EnvironmentCategory environmentCategory = new EnvironmentCategory();
		environmentCategory.setEnvironmentCategoryId(environmentCategoryId);
		environmentCategory.setEnvironmentCategoryName(environmentCategoryName);
		environmentCategory.setDisplayName(environmentCategoryDisplayName);
		
		EnvironmentGroup environmentGroup = new EnvironmentGroup();
		environmentGroup.setEnvironmentGroupId(environmentGroupId);
		environmentGroup.setEnvironmentGroupName(environmentGroupName);
		
		environmentCategory.setEnvironmentGroup(environmentGroup);
		
		environment.setEnvironmentCategory(environmentCategory);
		if (isStandAloneEnvironment == null || isStandAloneEnvironment < 0)
			environment.setIsStandAloneEnvironment(0);
		else
			environment.setIsStandAloneEnvironment(isStandAloneEnvironment);
		
		return environment;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getEnvironmentId() {
		return this.environmentId;
	}

	public void setEnvironmentId(Integer environmentId) {
		this.environmentId = environmentId;
	}
	
	public String getEnvironmentName() {
		return environmentName;
	}

	public void setEnvironmentName(String environmentName) {
		this.environmentName = environmentName;
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

	public Integer getProductMasterId() {
		return productMasterId;
	}

	public void setProductMasterId(Integer productMasterId) {
		this.productMasterId = productMasterId;
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

	public String getEnvironmentCategoryDisplayName() {
		return environmentCategoryDisplayName;
	}

	public void setEnvironmentCategoryDisplayName(
			String environmentCategoryDisplayName) {
		this.environmentCategoryDisplayName = environmentCategoryDisplayName;
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

	public Integer getIsSelected() {
		return isSelected;
	}

	public void setIsSelected(Integer isSelected) {
		this.isSelected = isSelected;
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
	
	public Integer getIsStandAloneEnvironment() {
		return isStandAloneEnvironment;
	}

	public void setIsStandAloneEnvironment(Integer isStandAloneEnvironment) {
		this.isStandAloneEnvironment = isStandAloneEnvironment;
	}
}
