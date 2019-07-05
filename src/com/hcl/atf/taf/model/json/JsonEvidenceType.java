package com.hcl.atf.taf.model.json;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.hcl.atf.taf.model.EvidenceType;

public class JsonEvidenceType {
	
	@JsonProperty
	private Integer evidenceTypeId;
	@JsonProperty
	private String evidenceTypeName;
	@JsonProperty
	private String description;
	
	public JsonEvidenceType() {
	}


	

	public JsonEvidenceType(EvidenceType evidenceType) {
		this.evidenceTypeId = evidenceType.getEvidenceTypeId();
		this.evidenceTypeName = evidenceType.getEvidenceTypeName();
		this.description = evidenceType.getDescription();
	}
	
	@JsonIgnore
	public EvidenceType getEvidenceType(){
		
		EvidenceType evidenceType = new EvidenceType();
		evidenceType.setEvidenceTypeId(evidenceTypeId);
		evidenceType.setEvidenceTypeName(evidenceTypeName);
		evidenceType.setDescription(description);
		
		return evidenceType;
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
	
		
}
