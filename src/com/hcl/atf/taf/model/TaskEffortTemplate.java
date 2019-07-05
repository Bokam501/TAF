
package com.hcl.atf.taf.model;

import static javax.persistence.GenerationType.IDENTITY;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "task_effort_templates")
public class TaskEffortTemplate {

	private Integer templateId;
	private String templateName;
	private String mappedFields;
	private Date fromDateValue;
	private Date toDateValue;
	private String statusValue;
	private String productValue;
	private String resourceValue;
	private Integer createdBy;
	private Date createdOn;
	private Integer isActive;
	public TaskEffortTemplate(){
		
	}
	public TaskEffortTemplate(String templateName, String mappedFields, String fromDate,String toDate, String statusValue,String productValue, String resourceValue){
		this.templateName=templateName;
		this.mappedFields=mappedFields;
		this.fromDateValue=new Date();
		this.toDateValue=new Date();
		this.statusValue=statusValue;
		this.productValue=productValue;
		this.resourceValue=resourceValue;
		
	}
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "templateId",unique = true, nullable = false)	
	public Integer getTemplateId() {
		return templateId;
	}
	public void setTemplateId(Integer templateId) {
		this.templateId = templateId;
	}
	@Column(name = "templateName")
	public String getTemplateName() {
		return templateName;
	}
	public void setTemplateName(String templateName) {
		this.templateName = templateName;
	}
	@Column(name = "mappedFields")
	public String getMappedFields() {
		return mappedFields;
	}
	public void setMappedFields(String mappedFields) {
		this.mappedFields = mappedFields;
	}
	@Column(name = "fromDateValue")
	public Date getFromDateValue() {
		return fromDateValue;
	}
	public void setFromDateValue(Date fromDateValue) {
		this.fromDateValue = fromDateValue;
	}
	@Column(name = "toDateValue")
	public Date getToDateValue() {
		return toDateValue;
	}
	public void setToDateValue(Date toDateValue) {
		this.toDateValue = toDateValue;
	}
	@Column(name = "statusValue")
	public String getStatusValue() {
		return statusValue;
	}
	public void setStatusValue(String statusValue) {
		this.statusValue = statusValue;
	}
	@Column(name = "productValue")
	public String getProductValue() {
		return productValue;
	}
	public void setProductValue(String productValue) {
		this.productValue = productValue;
	}
	@Column(name = "resourceValue")
	public String getResourceValue() {
		return resourceValue;
	}
	public void setResourceValue(String resourceValue) {
		this.resourceValue = resourceValue;
	}
	@Column(name = "createdBy")
	public Integer getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(Integer createdBy) {
		this.createdBy = createdBy;
	}
	@Column(name = "createdOn")
	public Date getCreatedOn() {
		return createdOn;
	}
	public void setCreatedOn(Date createdOn) {
		this.createdOn = createdOn;
	}	
	
	@Column(name = "isActive")
	public Integer getIsActive() {
		return isActive;
	}
	public void setIsActive(Integer isActive) {
		this.isActive = isActive;
	}	
}
