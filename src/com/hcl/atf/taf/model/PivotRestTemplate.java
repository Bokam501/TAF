package com.hcl.atf.taf.model;

import static javax.persistence.GenerationType.IDENTITY;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "pivot_rest_template")
public class PivotRestTemplate {

	private Integer templateId;
	private String templateName;
	private String factoryId;
	private String productId;
	private Integer collectionId;
	private String cubeName;
	private String configJsonValue;
	private Integer activeStatus;
	private Integer createdBy;
	private Integer updatedBy;
	private Date createdDate;
	private Date updatedDate;
	private String description;
	
	public PivotRestTemplate(){
		
	}
	public PivotRestTemplate(String templateName,Integer collectionId,String factoryId,String productId,String cubeName,String configJsonValue,int activeStatus, String description){
		this.templateName=templateName;
		this.collectionId=collectionId;
		this.factoryId=factoryId;
		this.productId=productId;
		this.cubeName=cubeName;
		this.configJsonValue=configJsonValue;
		this.activeStatus=activeStatus;	
		this.description=description;
		
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
	@Column(name = "collectionId")
	public Integer getCollectionId() {
		return collectionId;
	}
	public void setCollectionId(Integer collectionId) {
		this.collectionId = collectionId;
	}
	
	@Column(name = "activeStatus")
	public Integer getActiveStatus() {
		return activeStatus;
	}
	public void setActiveStatus(Integer activeStatus) {
		this.activeStatus = activeStatus;
	}
	@Column(name = "cubeName")
	public String getCubeName() {
		return cubeName;
	}
	public void setCubeName(String cubeName) {
		this.cubeName = cubeName;
	}
	@Column(name = "templateName")
	public String getTemplateName() {
		return templateName;
	}
	public void setTemplateName(String templateName) {
		this.templateName = templateName;
	}
	@Column(name = "factoryId")
	public String getFactoryId() {
		return factoryId;
	}
	public void setFactoryId(String factoryId) {
		this.factoryId = factoryId;
	}
	@Column(name = "productId")
	public String getProductId() {
		return productId;
	}
	public void setProductId(String productId) {
		this.productId = productId;
	}
	@Column(name = "configJsonValue")
	public String getConfigJsonValue() {
		return configJsonValue;
	}
	public void setConfigJsonValue(String configJsonValue) {
		this.configJsonValue = configJsonValue;
	}
	@Column(name = "createdBy")
	public Integer getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(Integer createdBy) {
		this.createdBy = createdBy;
	}
	@Column(name = "updatedBy")
	public Integer getUpdatedBy() {
		return updatedBy;
	}
	public void setUpdatedBy(Integer updatedBy) {
		this.updatedBy = updatedBy;
	}
	@Column(name = "createdDate")
	public Date getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}
	@Column(name = "updatedDate")
	public Date getUpdatedDate() {
		return updatedDate;
	}
	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
	}
	@Column(name = "description")
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
}
