package com.hcl.atf.taf.model.json;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.hcl.atf.taf.model.ExtractorTypeMaster;

public class JsonExtractorTypeMaster implements Serializable {

	private static final long serialVersionUID = 378236606538876680L;

	@JsonProperty
	private Integer id;
	
	@JsonProperty
	private String extarctorName;
	
	public JsonExtractorTypeMaster(ExtractorTypeMaster extractorTypeMaster){
		this.id = extractorTypeMaster.getId();
		this.extarctorName = extractorTypeMaster.getExtarctorName();
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getExtarctorName() {
		return extarctorName;
	}

	public void setExtarctorName(String extarctorName) {
		this.extarctorName = extarctorName;
	}
	
	public ExtractorTypeMaster getExtractorTypeMaster(){
		ExtractorTypeMaster extractorTypeMaster = new ExtractorTypeMaster();
		
		extractorTypeMaster.setId(this.id);
		extractorTypeMaster.setExtarctorName(this.extarctorName);
		
		return extractorTypeMaster;
	}
}
