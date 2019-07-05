package com.hcl.atf.taf.model.json;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.hcl.atf.taf.model.DefectTypeMaster;

public class JsonDefectType implements java.io.Serializable {
	
	@JsonProperty
	private Integer defectTypeId;
	
	@JsonProperty	
	private String defectTypeName;
	
	
	public JsonDefectType(){
		
	}
	
	public JsonDefectType(DefectTypeMaster defectType) {
		defectTypeId = defectType.getDefectTypeId();
		defectTypeName = defectType.getDefectTypeName();
	}
	
	@JsonIgnore
	public DefectTypeMaster getDefectTypeMaster() {
		DefectTypeMaster defectType = new DefectTypeMaster();
		if (defectTypeId != null) {
			defectType.setDefectTypeId(defectTypeId);
		}
		if (defectTypeName != null) {
			defectType.setDefectTypeName(defectTypeName);
		}
		return defectType;
	}

	public Integer getDefectTypeId() {
		return defectTypeId;
	}

	public void setDefectTypeId(Integer defectTypeId) {
		this.defectTypeId = defectTypeId;
	}

	public String getDefectTypeName() {
		return defectTypeName;
	}

	public void setDefectTypeName(String defectTypeName) {
		this.defectTypeName = defectTypeName;
	}

}
