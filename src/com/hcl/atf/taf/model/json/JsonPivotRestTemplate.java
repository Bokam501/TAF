package com.hcl.atf.taf.model.json;

import java.text.SimpleDateFormat;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.hcl.atf.taf.model.PivotRestTemplate;
import com.hcl.atf.taf.model.dto.PivotRestTemplateDTO;

public class JsonPivotRestTemplate implements java.io.Serializable {
	private static final Log log = LogFactory.getLog(JsonPivotRestTemplate.class);
	@JsonProperty
	private Integer templateId;
	@JsonProperty
	private String templateName;
	@JsonProperty
	private String description;
	@JsonProperty
	private String factoryId;
	@JsonProperty
	private String productId;
	@JsonProperty
	private Integer collectionId;
	@JsonProperty
	private String cubeName;
	@JsonProperty
	private String configJsonValue;
	@JsonProperty
	private Integer activeStatus;
	@JsonProperty
	private String createdDate;
	@JsonProperty
	private String createdBy;
	
	@JsonProperty
	private String userName;
	@JsonProperty
	private String reportType;
	
	public JsonPivotRestTemplate() {
	}
	public JsonPivotRestTemplate(PivotRestTemplate pivotRestTemplate) {
		this.templateId = pivotRestTemplate.getTemplateId();
		this.templateName = pivotRestTemplate.getTemplateName();
		this.description = pivotRestTemplate.getDescription();
		this.factoryId = pivotRestTemplate.getFactoryId();
		this.productId = pivotRestTemplate.getProductId();
		this.collectionId = pivotRestTemplate.getCollectionId();
		this.cubeName = pivotRestTemplate.getCubeName();
		this.configJsonValue = pivotRestTemplate.getConfigJsonValue();
		this.activeStatus = pivotRestTemplate.getActiveStatus();	
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		if(pivotRestTemplate.getCreatedDate()!=null)
			this.createdDate=sdf.format(pivotRestTemplate.getCreatedDate());
		
		
	}

	public JsonPivotRestTemplate(PivotRestTemplateDTO pivotRestTemplate, String str) {
		this.templateId = pivotRestTemplate.getTemplateId();
		this.templateName = pivotRestTemplate.getTemplateName();
		this.description = pivotRestTemplate.getDescription();
		this.factoryId = pivotRestTemplate.getFactoryId();
		this.productId = pivotRestTemplate.getProductId();
		this.collectionId = pivotRestTemplate.getCollectionId();
		this.cubeName = pivotRestTemplate.getCubeName();
		this.configJsonValue = pivotRestTemplate.getConfigJsonValue();
		this.activeStatus = pivotRestTemplate.getActiveStatus();	
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		if(pivotRestTemplate.getCreatedDate()!=null)
			this.createdDate=sdf.format(pivotRestTemplate.getCreatedDate());
		this.userName=pivotRestTemplate.getUserName();
		this.reportType=pivotRestTemplate.getReportType();
		
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

	public String getFactoryId() {
		return factoryId;
	}

	public void setFactoryId(String factoryId) {
		this.factoryId = factoryId;
	}

	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

	public Integer getCollectionId() {
		return collectionId;
	}

	public void setCollectionId(Integer collectionId) {
		this.collectionId = collectionId;
	}

	public String getCubeName() {
		return cubeName;
	}

	public void setCubeName(String cubeName) {
		this.cubeName = cubeName;
	}

	public String getConfigJsonValue() {
		return configJsonValue;
	}

	public void setConfigJsonValue(String configJsonValue) {
		this.configJsonValue = configJsonValue;
	}

	public Integer getActiveStatus() {
		return activeStatus;
	}

	public void setActiveStatus(Integer activeStatus) {
		this.activeStatus = activeStatus;
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

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public String getReportType() {
		return reportType;
	}

	public void setReportType(String reportType) {
		this.reportType = reportType;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}
	

}
