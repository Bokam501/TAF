package com.hcl.atf.taf.model.json;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.hcl.atf.taf.model.AttachmentType;
import com.hcl.atf.taf.model.EntityMaster;

public class JsonAttachmentType {

	@JsonProperty
	private Integer attachmentTypeId;
	@JsonProperty
	private String attachmentTypeName;
	@JsonProperty
	private String description;
	@JsonProperty
	private Integer entityTypeId;
	@JsonProperty
	private String entityTypeName;

	public JsonAttachmentType(){	
	}
	
	public JsonAttachmentType(AttachmentType attachmentType){
		this.attachmentTypeId = attachmentType.getAttachmentTypeId();
		this.attachmentTypeName = attachmentType.getAttachmentType();
		this.description = attachmentType.getDescription();
		if(attachmentType.getEntityMaster() != null && attachmentType.getEntityMaster().getEntitymasterid() != null){
			this.entityTypeId = attachmentType.getEntityMaster().getEntitymasterid();
			this.entityTypeName = attachmentType.getEntityMaster().getEntitymastername();
		}
	}
	
	public Integer getAttachmentTypeId() {
		return attachmentTypeId;
	}

	public void setAttachmentTypeId(Integer attachmentTypeId) {
		this.attachmentTypeId = attachmentTypeId;
	}

	public String getAttachmentTypeName() {
		return attachmentTypeName;
	}

	public void setAttachmentTypeName(String attachmentTypeName) {
		this.attachmentTypeName = attachmentTypeName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Integer getEntityTypeId() {
		return entityTypeId;
	}

	public void setEntityTypeId(Integer entityTypeId) {
		this.entityTypeId = entityTypeId;
	}

	public String getEntityTypeName() {
		return entityTypeName;
	}

	public void setEntityTypeName(String entityTypeName) {
		this.entityTypeName = entityTypeName;
	}

	@JsonIgnore
	public AttachmentType getAttachmentType() {
		AttachmentType attachmentType = new AttachmentType();
		attachmentType.setAttachmentTypeId(this.attachmentTypeId);
		attachmentType.setAttachmentType(this.attachmentTypeName);
		attachmentType.setDescription(this.description);
		if(this.entityTypeId != null){
			EntityMaster entityMaster = new EntityMaster();
			entityMaster.setEntitymasterid(this.entityTypeId);
			attachmentType.setEntityMaster(entityMaster);
		}
		return attachmentType;
	}

}
