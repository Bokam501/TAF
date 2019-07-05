package com.hcl.atf.taf.model.json;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.hcl.atf.taf.model.TestCaseScript;
import com.hcl.atf.taf.model.TestCaseScriptVersion;

public class JsonAutomationScriptsVersion implements java.io.Serializable{
	
	private static final Log log = LogFactory.getLog(JsonAutomationScriptsVersion.class);
	
	@JsonProperty
	private Integer scriptVersionId;
	@JsonProperty
	private Integer scriptversion;
	@JsonProperty
	private Integer revision;
	@JsonProperty
	private String uri;
	@JsonProperty
	private Integer status;
	@JsonProperty
	private Integer isSelected;
	@JsonProperty
	private Integer scriptId;
	@JsonProperty
	private Integer testCaseId;
	
	public JsonAutomationScriptsVersion(){
		
	}
	
	public JsonAutomationScriptsVersion(TestCaseScriptVersion testcasescriptversion){
		
		if(testcasescriptversion.getScriptVersionId() != null){
			this.scriptVersionId = testcasescriptversion.getScriptVersionId();
		}
		if(testcasescriptversion.getScriptversion() != null){
			this.scriptversion = testcasescriptversion.getScriptversion();
		}
		if(testcasescriptversion.getRevision() != null){
			this.revision = testcasescriptversion.getRevision();
		}
		if(testcasescriptversion.getUri() != null){
			this.uri = testcasescriptversion.getUri();
		}
		if(testcasescriptversion.getStatus() != null){
			this.status = testcasescriptversion.getStatus();
		}
		if(testcasescriptversion.getIsSelected() != null){
			this.isSelected = testcasescriptversion.getIsSelected();
		}
	}
	
	
	
	@JsonIgnore
	public TestCaseScriptVersion getTestCaseScriptVersion(){
		
		TestCaseScriptVersion testscriptVersion = new TestCaseScriptVersion();
		TestCaseScript tcscript=new TestCaseScript();
		tcscript.setScriptId(scriptId);
		testscriptVersion.setTestcasescript(tcscript);
		testscriptVersion.setScriptversion(scriptversion);
		testscriptVersion.setRevision(revision);
		testscriptVersion.setUri(uri);
		testscriptVersion.setStatus(status);
		testscriptVersion.setIsSelected(isSelected);
	
		
		return testscriptVersion;
		
	}
	
	
	public Integer getScriptVersionId() {
		return scriptVersionId;
	}
	public void setScriptVersionId(Integer scriptVersionId) {
		this.scriptVersionId = scriptVersionId;
	}
	public Integer getScriptversion() {
		return scriptversion;
	}
	public void setScriptversion(Integer scriptversion) {
		this.scriptversion = scriptversion;
	}
	public Integer getRevision() {
		return revision;
	}
	public void setRevision(Integer revision) {
		this.revision = revision;
	}
	public String getUri() {
		return uri;
	}
	public void setUri(String uri) {
		this.uri = uri;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public Integer getIsSelected() {
		return isSelected;
	}
	public void setIsSelected(Integer isSelected) {
		this.isSelected = isSelected;
	}

	public Integer getScriptId() {
		return scriptId;
	}

	public void setScriptId(Integer scriptId) {
		this.scriptId = scriptId;
	}

	public Integer getTestCaseId() {
		return testCaseId;
	}

	public void setTestCaseId(Integer testCaseId) {
		this.testCaseId = testCaseId;
	}
	

}
