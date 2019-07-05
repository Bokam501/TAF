package com.hcl.atf.taf.model.json;

import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.hcl.atf.taf.controller.utilities.DateUtility;
import com.hcl.atf.taf.model.TaskEffortTemplate;



public class JsonTaskEffortTemplate implements java.io.Serializable {
	private static final Log log = LogFactory.getLog(JsonTaskEffortTemplate.class);
	@JsonProperty
	private Integer templateId;
	@JsonProperty
	private String templateName;
	@JsonProperty
	private String mappedFields;
	@JsonProperty
	private String fromDateValue;
	@JsonProperty
	private String toDateValue;
	@JsonProperty
	private String statusValue;
	@JsonProperty
	private String productValue;
	@JsonProperty
	private String resourceValue;
	@JsonProperty
	private Integer createdBy;
	@JsonProperty
	private Date createdOn;
	@JsonProperty
	private Integer isActive;
	public JsonTaskEffortTemplate() {
	}

	public JsonTaskEffortTemplate(TaskEffortTemplate taskEffortTemplate) {
		this.templateId = taskEffortTemplate.getTemplateId();
		this.templateName = taskEffortTemplate.getTemplateName();
		this.mappedFields = taskEffortTemplate.getMappedFields();	
		
		
		if(taskEffortTemplate.getFromDateValue()!=null){
			this.fromDateValue = DateUtility.dateToStringWithSeconds1(taskEffortTemplate.getFromDateValue());
		}
		if(taskEffortTemplate.getToDateValue()!=null){
			this.toDateValue = DateUtility.dateformatWithOutTime(taskEffortTemplate.getToDateValue());
		}
		this.statusValue = taskEffortTemplate.getStatusValue();
		this.productValue = taskEffortTemplate.getProductValue();
		this.resourceValue = taskEffortTemplate.getResourceValue();
		this.createdBy = taskEffortTemplate.getCreatedBy();
		this.createdOn = taskEffortTemplate.getCreatedOn();
		
		this.isActive = taskEffortTemplate.getIsActive();	
	}

	public Integer getTemplateId() {
		return templateId;
	}

	public void setTemplateId(Integer templateId) {
		this.templateId = templateId;
	}

	public String getTemplateName() {
		return templateName;
	}

	public void setTemplateName(String templateName) {
		this.templateName = templateName;
	}

	public String getMappedFields() {
		return mappedFields;
	}

	public void setMappedFields(String mappedFields) {
		this.mappedFields = mappedFields;
	}

	public String getFromDateValue() {
		return fromDateValue;
	}

	public void setFromDateValue(String fromDateValue) {
		this.fromDateValue = fromDateValue;
	}

	public String getToDateValue() {
		return toDateValue;
	}

	public void setToDateValue(String toDateValue) {
		this.toDateValue = toDateValue;
	}

	public String getStatusValue() {
		return statusValue;
	}

	public void setStatusValue(String statusValue) {
		this.statusValue = statusValue;
	}

	public String getProductValue() {
		return productValue;
	}

	public void setProductValue(String productValue) {
		this.productValue = productValue;
	}

	public String getResourceValue() {
		return resourceValue;
	}

	public void setResourceValue(String resourceValue) {
		this.resourceValue = resourceValue;
	}

	public Integer getIsActive() {
		return isActive;
	}

	public void setIsActive(Integer isActive) {
		this.isActive = isActive;
	}

	public Integer getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(Integer createdBy) {
		this.createdBy = createdBy;
	}

	public Date getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(Date createdOn) {
		this.createdOn = createdOn;
	}

}
