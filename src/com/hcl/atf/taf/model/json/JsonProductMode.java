package com.hcl.atf.taf.model.json;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.hcl.atf.taf.model.ProductMode;

public class JsonProductMode {
	@JsonProperty
	private Integer modeId;
	@JsonProperty
	private String modeName;
	@JsonProperty
	private String modeDescription;
	@JsonProperty
	private Integer status;
	
	
	public JsonProductMode() {
	}
	public JsonProductMode(ProductMode productMode){
		
		this.modeId=productMode.getModeId();
		this.modeName=productMode.getModeName();
		this.modeDescription=productMode.getModeDescription();
		this.status=productMode.getStatus();
		
		
	}
	public Integer getModeId() {
		return modeId;
	}

	public void setModeId(Integer modeId) {
		this.modeId = modeId;
	}

	public String getModeName() {
		return modeName;
	}
	
	public void setModeName(String modeName) {
		this.modeName=modeName;
	}
	
	public String getModeDescription() {
		return modeDescription;
	}
	
}
