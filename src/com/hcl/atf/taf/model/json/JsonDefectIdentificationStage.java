package com.hcl.atf.taf.model.json;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.hcl.atf.taf.model.DefectIdentificationStageMaster;

public class JsonDefectIdentificationStage implements java.io.Serializable {
	
	@JsonProperty
	private Integer stageId;
	
	@JsonProperty	
	private String stageName;
	
	@JsonProperty	
	private String description;

	
	public JsonDefectIdentificationStage(){
		
	}
	
	public JsonDefectIdentificationStage(DefectIdentificationStageMaster defectIdentificationStage) {
		stageId = defectIdentificationStage.getStageId();
		stageName = defectIdentificationStage.getStageName();
		description = defectIdentificationStage.getDescription();
	}
	
	@JsonIgnore
	public DefectIdentificationStageMaster getDefectIdentificationStageMaster() {
		DefectIdentificationStageMaster defectStage = new DefectIdentificationStageMaster();
		if (stageId != null) {
			defectStage.setStageId(stageId);
		}
		if (stageName != null) {
			defectStage.setStageName(stageName);
		}
		if (description != null) {
			defectStage.setDescription(description);
		}
		return defectStage;
	}

	public Integer getStageId() {
		return stageId;
	}

	public void setStageId(Integer stageId) {
		this.stageId = stageId;
	}

	public String getStageName() {
		return stageName;
	}

	public void setStageName(String stageName) {
		this.stageName = stageName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}
