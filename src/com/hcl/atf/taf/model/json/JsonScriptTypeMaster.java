package com.hcl.atf.taf.model.json;



import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.hcl.atf.taf.model.ScriptTypeMaster;

public class JsonScriptTypeMaster implements java.io.Serializable {

	@JsonProperty
	private String scriptType;
	

	public JsonScriptTypeMaster() {
	}

	public JsonScriptTypeMaster(ScriptTypeMaster scriptTypeMaster) {
		this.scriptType = scriptTypeMaster.getScriptType();
	}

	public String getScriptType() {
		return scriptType;
	}

	public void setScriptType(String scriptType) {
		this.scriptType = scriptType;
	}
	
	
	@JsonIgnore
	public ScriptTypeMaster getScriptTypeMaster(){
		ScriptTypeMaster scriptTypeMaster = new ScriptTypeMaster();
		scriptTypeMaster.setScriptType(scriptType);
		return scriptTypeMaster;
	}
	
}
