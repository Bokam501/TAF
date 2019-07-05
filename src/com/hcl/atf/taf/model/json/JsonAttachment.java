package com.hcl.atf.taf.model.json;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.hcl.atf.taf.controller.utilities.DateUtility;
import com.hcl.atf.taf.model.Attachment;
import com.hcl.atf.taf.model.EntityMaster;
import com.hcl.atf.taf.model.ProductMaster;
import com.hcl.atf.taf.model.UserList;
import com.hcl.atf.taf.model.dto.AttachmentDTO;

public class JsonAttachment implements java.io.Serializable {
	private static final Log log = LogFactory
			.getLog(JsonAttachment.class);
	
	@JsonProperty	
	private Integer attachmentId;
	@JsonProperty
	private Integer productId;
	@JsonProperty
	private String productName;
	@JsonProperty
	private Integer entityMasterId;	
	@JsonProperty
	private String entityMasterName;
	
	@JsonProperty
	private Integer entityPrimaryId;
	@JsonProperty
	private String entityName;
	@JsonProperty
	private String attachmentType;//TestData, Object Repository, ScreenShot, Log .../create Constants
	@JsonProperty
	private String attributeFileExtension;
	@JsonProperty
	private String attributeFileName;
	@JsonProperty
	private String 	attachmentName;
	@JsonProperty
	private String attributeFileURI;
	@JsonProperty
	private String attributeFileSize;
	@JsonProperty
	private String description;	
	
	@JsonProperty
	private Integer createrId;	
	@JsonProperty	
	private String createrName;	
	
	@JsonProperty
	private Integer modifierId;	
	@JsonProperty	
	private String modifierName;
	
	@JsonProperty	
	private String uploadedDate;
	@JsonProperty
	private String modifiedDate;
	@JsonProperty	
	private String lastModifiedDate;
	@JsonProperty
	private Integer status;
	
	@JsonProperty
	private Integer isEditable;
	

	public Integer getAttachmentId() {
		return attachmentId;
	}

	public void setAttachmentId(Integer attachmentId) {
		this.attachmentId = attachmentId;
	}

	public Integer getProductId() {
		return productId;
	}

	public void setProductId(Integer productId) {
		this.productId = productId;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
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

	public void setCreaterId(int createrId) {
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

	public void setModifierId(int modifierId) {
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

	public void setCreaterId(Integer createrId) {
		this.createrId = createrId;
	}

	public void setModifierId(Integer modifierId) {
		this.modifierId = modifierId;
	}

	public JsonAttachment() {
	}

	public JsonAttachment(Attachment attachments) {		
		if(attachments.getAttachmentId() != null){
			this.attachmentId = attachments.getAttachmentId();
		}
		
		if(attachments.getProduct() != null){
			this.productId = attachments.getProduct().getProductId();
			this.productName = attachments.getProduct().getProductName();
		}
		
		if(attachments.getEntityMaster() != null){
			this.entityMasterId = attachments.getEntityMaster().getEntitymasterid();
			this.entityMasterName = attachments.getEntityMaster().getEntitymastername();
	
		}
		if(attachments.getEntityId() != null){
			this.entityPrimaryId = attachments.getEntityId();
		}
		if(attachments.getAttachmentType() != null){
			this.attachmentType = attachments.getAttachmentType();
		}
		if(attachments.getAttributeFileExtension() != null){
			this.attributeFileExtension = attachments.getAttributeFileExtension();
		}		
		
		if(attachments.getAttributeFileName() != null){
			this.attributeFileName = attachments.getAttributeFileName();
		}
		
		if(attachments.getAttachmentName() != null){
			this.attachmentName = attachments.getAttachmentName();
		}
		
		if(attachments.getAttributeFileURI() != null){
			this.attributeFileURI = attachments.getAttributeFileURI();
		}
		
		if(attachments.getAttributeFileSize() != null){
			this.attributeFileSize = attachments.getAttributeFileSize();
		}
		
		if(attachments.getDescription() != null && !attachments.getDescription().trim().isEmpty()){
			this.description = attachments.getDescription();
		}
		
		if(attachments.getCreatedBy() != null){
			this.createrId = attachments.getCreatedBy().getUserId();
			this.createrName = attachments.getCreatedBy().getLoginId();
		}
		
		if(attachments.getModifiedBy() != null){
			this.modifierId = attachments.getModifiedBy().getUserId();
			this.modifierName = attachments.getModifiedBy().getLoginId();
		}
		
		if(attachments.getAttributeFileExtension() != null){
			this.attributeFileExtension = attachments.getAttributeFileExtension();
		}
		
		if(attachments.getUploadedDate() != null){
			this.uploadedDate = DateUtility.dateToStringWithSeconds1(attachments.getUploadedDate());
		}
		if(attachments.getModifiedDate() != null){
			this.modifiedDate = DateUtility.dateToStringWithSeconds1(attachments.getModifiedDate());
		}
		if(attachments.getLastModifiedDate() != null){
			this.lastModifiedDate = DateUtility.dateToStringWithSeconds1(attachments.getLastModifiedDate());
		}
		
		if(attachments.getStatus() != null){
			this.status = attachments.getStatus();			
		}
		
		if(attachments.getIsEditable() != null){
			this.isEditable = attachments.getIsEditable();			
		}
	}
	public JsonAttachment(AttachmentDTO attachmentDTO) {		
		if(attachmentDTO.getAttachmentId() != null){
			this.attachmentId = attachmentDTO.getAttachmentId();
		}
		if(attachmentDTO.getEntityMasterId() != null)
			this.entityMasterId = attachmentDTO.getEntityMasterId();
		if(attachmentDTO.getEntityMasterName() != null)
			this.entityMasterName = attachmentDTO.getEntityMasterName();


		if(attachmentDTO.getEntityPrimaryId() != null){
			this.entityPrimaryId = attachmentDTO.getEntityPrimaryId();
			this.entityName = attachmentDTO.getEntityName();
		}
		if(attachmentDTO.getAttachmentType() != null){
			this.attachmentType = attachmentDTO.getAttachmentType();
		}
		if(attachmentDTO.getAttributeFileExtension() != null){
			this.attributeFileExtension = attachmentDTO.getAttributeFileExtension();
		}		
		
		if(attachmentDTO.getAttributeFileName() != null){
			this.attributeFileName = attachmentDTO.getAttributeFileName();
		}
		if(attachmentDTO.getAttachmentName() != null){
			
			this.attachmentName = attachmentDTO.getAttachmentName();
		}
		
		if(attachmentDTO.getAttributeFileURI() != null){
			this.attributeFileURI = attachmentDTO.getAttributeFileURI();
		}
		
		if(attachmentDTO.getAttributeFileSize() != null){
			this.attributeFileSize = attachmentDTO.getAttributeFileSize();
		}
		
		if(attachmentDTO.getDescription() != null){
			this.description = attachmentDTO.getDescription();
		}
		
		if(attachmentDTO.getCreaterId() != null)
			this.createrId = attachmentDTO.getCreaterId();
		if(attachmentDTO.getCreaterName() != null)
			this.createrName = attachmentDTO.getCreaterName();		
		
		if(attachmentDTO.getModifierId() != null)
			this.modifierId = attachmentDTO.getModifierId();
		if(attachmentDTO.getModifierName() != null)
			this.modifierName = attachmentDTO.getModifierName();
		
		if(attachmentDTO.getAttributeFileExtension() != null){
			this.attributeFileExtension = attachmentDTO.getAttributeFileExtension();
		}
		
		if(attachmentDTO.getUploadedDate() != null){
			this.uploadedDate = attachmentDTO.getUploadedDate();
		}
		if(attachmentDTO.getModifiedDate() != null){
			this.modifiedDate = attachmentDTO.getModifiedDate();
		}
		if(attachmentDTO.getLastModifiedDate() != null){
			this.lastModifiedDate = attachmentDTO.getLastModifiedDate();
		}
		
		if(attachmentDTO.getStatus() != null){
			this.status = attachmentDTO.getStatus();			
		}
		
		if(attachmentDTO.getIsEditable() != null){
			this.isEditable = attachmentDTO.getIsEditable();			
		}
	}

	@JsonIgnore
	public Attachment getAttachments() {
		Attachment attachmentsObj = new Attachment();
		if(this.attachmentId != null)
		attachmentsObj.setAttachmentId(attachmentId);
		
		if(this.productId != null && this.productId > 0){
			ProductMaster productMaster = new ProductMaster();
			productMaster.setProductId(this.productId);
			attachmentsObj.setProduct(productMaster);
		}
		
		if(this.entityMasterId != null){//this.entityMasterName
			EntityMaster etm = new EntityMaster();
			etm.setEntitymasterid(entityMasterId);			
			attachmentsObj.setEntityMaster(etm);
		}
		if(this.entityPrimaryId != null)
			attachmentsObj.setEntityId(entityPrimaryId);
		
		if(this.attachmentType  != null)
			attachmentsObj.setAttachmentType(attachmentType);
		
		if(this.attributeFileExtension != null)
			attachmentsObj.setAttributeFileExtension(attributeFileExtension);
		
		if(this.attributeFileName != null){
			attachmentsObj.setAttributeFileName(attributeFileName);
		}
		if(this.attachmentName != null){
			attachmentsObj.setAttachmentName(attachmentName);
		}

		if(this.attributeFileURI != null){
			attachmentsObj.setAttributeFileURI(attributeFileURI);
		}
	
		if(this.attributeFileSize != null){
			attachmentsObj.setAttributeFileSize(attributeFileSize);
		}
		
		if(this.description != null){
			attachmentsObj.setDescription(description);
		}
		
		if(this.createrId != null){
			UserList creater = new UserList();
			creater.setUserId(createrId);		//this.createrName 				
			attachmentsObj.setCreatedBy(creater);
		}
		
		if(this.modifierId != null){
			UserList modifier = new UserList();
			modifier.setUserId(modifierId);		//this.modifierName 				
			attachmentsObj.setModifiedBy(modifier);
		}
		
		if(this.attributeFileExtension != null){
			attachmentsObj.setAttributeFileExtension(attributeFileExtension);
		}
		
		if(this.uploadedDate == null || this.uploadedDate.trim().isEmpty()) {
			attachmentsObj.setUploadedDate(DateUtility.getCurrentTime());
		}else{		
			attachmentsObj.setUploadedDate(DateUtility.toFormatDate(this.uploadedDate));
		}

		if(this.uploadedDate == null || this.uploadedDate.trim().isEmpty()) {
			attachmentsObj.setUploadedDate(DateUtility.getCurrentTime());
		}else{		
			attachmentsObj.setUploadedDate(DateUtility.toFormatDate(this.uploadedDate));
		}
		attachmentsObj.setModifiedDate(DateUtility.getCurrentTime());

		if(this.lastModifiedDate == null || this.lastModifiedDate.trim().isEmpty()) {
			attachmentsObj.setLastModifiedDate(DateUtility.getCurrentTime());
		} else {
		
			attachmentsObj.setLastModifiedDate(DateUtility.dateformatWithOutTime(this.lastModifiedDate));
		}	
		attachmentsObj.setStatus(status);	
		
		attachmentsObj.setIsEditable(isEditable);
		return attachmentsObj;
	}

}
