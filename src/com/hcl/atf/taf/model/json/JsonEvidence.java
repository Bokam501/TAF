package com.hcl.atf.taf.model.json;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.hcl.atf.taf.controller.utilities.CommonUtility;
import com.hcl.atf.taf.model.Evidence;

public class JsonEvidence {
	@JsonProperty
	private Integer evidenceid;
	@JsonProperty
	private Integer entityvalue;
	
	@JsonProperty
	private String fileuri;
	@JsonProperty
	private String filetype;
	@JsonProperty
	private Long size;
	@JsonProperty
	private Integer entityId;
	@JsonProperty
	private String evidencename;
	@JsonProperty
	private String description;
	@JsonProperty
	private Integer evidenceTypeId;
	@JsonProperty
	private String evidenceTypeName;
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
	
	
	
	public JsonEvidence() {
	}

	

	public Integer getEntityvalue() {
		return entityvalue;
	}



	public void setEntityvalue(Integer entityvalue) {
		this.entityvalue = entityvalue;
	}



	public JsonEvidence(Evidence evidence) {
		this.evidenceid = evidence.getEvidenceid();
		this.fileuri = evidence.getFileuri();
		if(evidence.getFileuri()!=null){
			this.fileuri = CommonUtility.getCatalinaPath()+evidence.getFileuri();
			
		}
		this.filetype = evidence.getFiletype();
		this.size=evidence.getSize();
		if(evidence.getEvidencename() != null) {
			this.evidencename = evidence.getEvidencename();
		} else { 
			this.evidencename= "N/A";
		}
		this.description=evidence.getDescription();
		if(evidence.getEntityMaster()!=null){
			this.entityId=evidence.getEntityMaster().getEntitymasterid();
		}
		this.entityvalue=evidence.getEntityvalue();
		if(evidence.getEvidenceType()!=null){
			this.evidenceTypeId=evidence.getEvidenceType().getEvidenceTypeId();
			this.evidenceTypeName=evidence.getEvidenceType().getEvidenceTypeName();
		}
	}
	
	@JsonIgnore
	public Evidence getEvidence(){
		
		Evidence evidence = new Evidence();
		
		evidence.setEvidenceid(evidenceid);
		evidence.setFileuri(fileuri);
		evidence.setFiletype(filetype);
		evidence.setSize(size);
		return evidence;
	}
	public Integer getEvidenceid() {
		return evidenceid;
	}
	public void setEvidenceid(Integer evidenceid) {
		this.evidenceid = evidenceid;
	}
	
	public String getFileuri() {
		return fileuri;
	}
	public void setFileuri(String fileuri) {
		this.fileuri = fileuri;
	}
	public String getFiletype() {
		return filetype;
	}
	public void setFiletype(String filetype) {
		this.filetype = filetype;
	}
	public Long getSize() {
		return size;
	}
	public void setSize(Long size) {
		this.size = size;
	}

	public Integer getEntityId() {
		return entityId;
	}

	public void setEntityId(Integer entityId) {
		this.entityId = entityId;
	}

	public String getEvidencename() {
		return evidencename;
	}

	public void setEvidencename(String evidencename) {
		this.evidencename = evidencename;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}



	public Integer getEvidenceTypeId() {
		return evidenceTypeId;
	}



	public void setEvidenceTypeId(Integer evidenceTypeId) {
		this.evidenceTypeId = evidenceTypeId;
	}



	public String getEvidenceTypeName() {
		return evidenceTypeName;
	}



	public void setEvidenceTypeName(String evidenceTypeName) {
		this.evidenceTypeName = evidenceTypeName;
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
