package com.hcl.atf.taf.model;

// Generated July 26, 2015 12:30:28 PM by Hibernate Tools 3.4.0.CR1

import static javax.persistence.GenerationType.IDENTITY;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * Attachments generated by hbm2java
 */
@Entity
@Table(name = "attachment")
public class Attachment implements java.io.Serializable {
	private Integer attachmentId;
	private ProductMaster product;
	private EntityMaster entityMaster;
	private Integer entityId;//Based on Entity, ProductId, VersionId, TestCaseId
	private String attachmentType;//TestData, Object Repository, ScreenShot, Log, Photo, Document,  .../create Constants
	private String attributeFileExtension;
	private String attributeFileName;
	private String attachmentName;
	private String attributeFileURI;
	private String attributeFileSize;
	private String description;
	private UserList createdBy;
	private UserList modifiedBy;
	private Date uploadedDate;

	private Date modifiedDate;
	private Date lastModifiedDate;
	private Integer status;
	private String attachmentPrefixName;
	
	private  Integer isEditable;
	
	@Column(name = "attachmentPrefixName")
	public String getAttachmentPrefixName() {
		return attachmentPrefixName;
	}
	public void setAttachmentPrefixName(String attachmentPrefixName) {
		this.attachmentPrefixName = attachmentPrefixName;
	}
	
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "attachmentId", unique = true, nullable = false)
	public Integer getAttachmentId() {
		return attachmentId;
	}
	public void setAttachmentId(Integer attachmentId) {
		this.attachmentId = attachmentId;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "productId", nullable = true)
	public ProductMaster getProduct() {
		return product;
	}
	public void setProduct(ProductMaster product) {
		this.product = product;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "entityMasterId")
	public EntityMaster getEntityMaster() {
		return entityMaster;
	}
	public void setEntityMaster(EntityMaster entityMaster) {
		this.entityMaster = entityMaster;
	}
	
	@Column(name ="entityId")
	public Integer getEntityId() {
		return entityId;
	}
	public void setEntityId(Integer entityId) {
		this.entityId = entityId;
	}
	
	@Column(name = "attachmentType")
	public String getAttachmentType() {
		return attachmentType;
	}
	public void setAttachmentType(String attachmentType) {
		this.attachmentType = attachmentType;
	}
	
	@Column(name = "attributeFileExtension")
	public String getAttributeFileExtension() {
		return attributeFileExtension;
	}
	public void setAttributeFileExtension(String attributeFileExtension) {
		this.attributeFileExtension = attributeFileExtension;
	}
	
	@Column(name = "attributeFileName")
	public String getAttributeFileName() {
		return attributeFileName;
	}
	public void setAttributeFileName(String attributeFileName) {
		this.attributeFileName = attributeFileName;
	}
	
	@Column(name = "attributeFileURI")
	public String getAttributeFileURI() {
		return attributeFileURI;
	}
	public void setAttributeFileURI(String attributeFileURI) {
		this.attributeFileURI = attributeFileURI;
	}
	
	@Column(name = "attributeFileSize")
	public String getAttributeFileSize() {
		return attributeFileSize;
	}
	public void setAttributeFileSize(String attributeFileSize) {
		this.attributeFileSize = attributeFileSize;
	}
	
	@Column(name = "description")
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}	
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "createdBy")	
	public UserList getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(UserList createdBy) {
		this.createdBy = createdBy;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "modifiedBy")	
	public UserList getModifiedBy() {
		return modifiedBy;
	}
	public void setModifiedBy(UserList modifiedBy) {
		this.modifiedBy = modifiedBy;
	}	
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "uploadedDate")
	public Date getUploadedDate() {
		return uploadedDate;
	}
	public void setUploadedDate(Date uploadedDate) {
		this.uploadedDate = uploadedDate;
	}
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "modifiedDate")
	public Date getModifiedDate() {
		return modifiedDate;
	}
	public void setModifiedDate(Date modifiedDate) {
		this.modifiedDate = modifiedDate;
	}
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "lastModifiedDate")
	public Date getLastModifiedDate() {
		return lastModifiedDate;
	}
	public void setLastModifiedDate(Date lastModifiedDate) {
		this.lastModifiedDate = lastModifiedDate;
	}
	@Column(name ="status")
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	
	
	@Column(name = "attachmentName")
	public String getAttachmentName() {
		return attachmentName;
	}
	public void setAttachmentName(String attachmentName) {
		this.attachmentName = attachmentName;
	}
	
	@Column(name = "isEditable")
	public Integer getIsEditable() {
		return isEditable;
	}
	public void setIsEditable(Integer isEditable) {
		this.isEditable = isEditable;
	}
	
	@Override
	public boolean equals(Object attachmentObj) {
	
		if (attachmentObj == null)
			return false;
		Attachment attach = (Attachment) attachmentObj;
		if (attach.getAttachmentId().equals(this.attachmentId)) {
			return true;
		} else {
			return false;
		}
	}
}