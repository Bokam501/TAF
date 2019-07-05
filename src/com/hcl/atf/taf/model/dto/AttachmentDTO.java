package com.hcl.atf.taf.model.dto;


public class AttachmentDTO {
	private Integer attachmentId;	
	private Integer entityMasterId;	
	private String entityMasterName;	
	private Integer entityPrimaryId;	
	private String entityName;	
	private String attachmentType;//TestData, Object Repository, ScreenShot, Log .../create Constants	
	private String attributeFileExtension;	
	private String attributeFileName;	
	private String attachmentName;
	private String attributeFileURI;
	private String attributeFileSize;	
	private String description;		
	private Integer createrId;		
	private String createrName;	
	private Integer modifierId;		
	private String modifierName;		
	private String uploadedDate;	
	private String modifiedDate;		
	private String lastModifiedDate;	
	private Integer status;
	
	private Integer isEditable;
	
	public Integer getAttachmentId() {
		return attachmentId;
	}
	public void setAttachmentId(Integer attachmentId) {
		this.attachmentId = attachmentId;
	}
	public Integer getEntityMasterId() {
		return entityMasterId;
	}
	public void setEntityMasterId(Integer entityMasterId) {
		this.entityMasterId = entityMasterId;
	}
	public String getEntityMasterName() {
		return entityMasterName;
	}
	public void setEntityMasterName(String entityMasterName) {
		this.entityMasterName = entityMasterName;
	}
	public Integer getEntityPrimaryId() {
		return entityPrimaryId;
	}
	public void setEntityPrimaryId(Integer entityPrimaryId) {
		this.entityPrimaryId = entityPrimaryId;
	}
	public String getEntityName() {
		return entityName;
	}
	public void setEntityName(String entityName) {
		this.entityName = entityName;
	}
	public String getAttachmentType() {
		return attachmentType;
	}
	public void setAttachmentType(String attachmentType) {
		this.attachmentType = attachmentType;
	}
	public String getAttributeFileExtension() {
		return attributeFileExtension;
	}
	public void setAttributeFileExtension(String attributeFileExtension) {
		this.attributeFileExtension = attributeFileExtension;
	}
	public String getAttributeFileName() {
		return attributeFileName;
	}
	public void setAttributeFileName(String attributeFileName) {
		this.attributeFileName = attributeFileName;
	}
	public String getAttributeFileURI() {
		return attributeFileURI;
	}
	public void setAttributeFileURI(String attributeFileURI) {
		this.attributeFileURI = attributeFileURI;
	}
	public String getAttributeFileSize() {
		return attributeFileSize;
	}
	public void setAttributeFileSize(String attributeFileSize) {
		this.attributeFileSize = attributeFileSize;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Integer getCreaterId() {
		return createrId;
	}
	public void setCreaterId(Integer createrId) {
		this.createrId = createrId;
	}
	public String getCreaterName() {
		return createrName;
	}
	public void setCreaterName(String createrName) {
		this.createrName = createrName;
	}
	public Integer getModifierId() {
		return modifierId;
	}
	public void setModifierId(Integer modifierId) {
		this.modifierId = modifierId;
	}
	public String getModifierName() {
		return modifierName;
	}
	public void setModifierName(String modifierName) {
		this.modifierName = modifierName;
	}
	public String getUploadedDate() {
		return uploadedDate;
	}
	public void setUploadedDate(String uploadedDate) {
		this.uploadedDate = uploadedDate;
	}
	public String getModifiedDate() {
		return modifiedDate;
	}
	public void setModifiedDate(String modifiedDate) {
		this.modifiedDate = modifiedDate;
	}
	public String getLastModifiedDate() {
		return lastModifiedDate;
	}
	public void setLastModifiedDate(String lastModifiedDate) {
		this.lastModifiedDate = lastModifiedDate;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}	
	public String getAttachmentName() {
		return attachmentName;
	}
	public void setAttachmentName(String attachmentName) {
		this.attachmentName = attachmentName;
	}
	public Integer getIsEditable() {
		return isEditable;
	}
	public void setIsEditable(Integer isEditable) {
		this.isEditable = isEditable;
	}
		
	
}
