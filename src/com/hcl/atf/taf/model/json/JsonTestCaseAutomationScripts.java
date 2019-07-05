package com.hcl.atf.taf.model.json;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.hcl.atf.taf.model.ProductMaster;
import com.hcl.atf.taf.model.TestCaseScript;

public class JsonTestCaseAutomationScripts implements java.io.Serializable {
	private static final Log log = LogFactory.getLog(JsonTestCaseAutomationScripts.class);
	
	@JsonProperty
	private Integer scriptId;
	@JsonProperty
	private String scriptName;
	@JsonProperty
	private String scriptQualifiedName;
	@JsonProperty
	private String language;
	@JsonProperty
	private String source;
	
	private String scriptVersion;
	private Integer revision;
	private String uri;
	
	private Integer isLatest;
	private Integer sourceContolSystemId;
	private Integer primaryTestEngineId;
	
	private Integer productId;
	private String productName;
	
	@JsonProperty
	private String 	modifiedFieldTitle;
	
	
	public JsonTestCaseAutomationScripts(){
		
	}
	
	public JsonTestCaseAutomationScripts(TestCaseScript testcasescript){
		
		if(testcasescript.getScriptId() != null){
			this.scriptId = testcasescript.getScriptId();			
		}
		if(testcasescript.getScriptName() != null){
			this.scriptName = testcasescript.getScriptName();
		}
		if(testcasescript.getScriptQualifiedName() != null){
			this.scriptQualifiedName = testcasescript.getScriptQualifiedName();
		}
		
		if(testcasescript.getSource() != null){
			this.source = testcasescript.getSource();
		}
		this.primaryTestEngineId = testcasescript.getPrimaryTestEngineId();
		this.language = testcasescript.getLanguage();
		this.isLatest = testcasescript.getIsLatest();
		this.sourceContolSystemId = testcasescript.getSourceContolSystemId();
		if(testcasescript.getRevision() != null) {
			this.revision = testcasescript.getRevision();
		}
		this.uri = testcasescript.getUri();
		this.scriptVersion = testcasescript.getScriptVersion();
		
		if(testcasescript != null && testcasescript.getProduct() != null) {
			this.productId = testcasescript.getProduct().getProductId();
			this.productName = testcasescript.getProduct().getProductName();
		}
	}
	
	@JsonIgnore
	public TestCaseScript getTestCaseScript(){
		TestCaseScript testcasescript = new TestCaseScript();	
		testcasescript.setScriptId(scriptId);
		testcasescript.setScriptName(scriptName);
		testcasescript.setScriptQualifiedName(scriptQualifiedName);
		testcasescript.setSource(source);
		testcasescript.setIsLatest(isLatest);
		testcasescript.setScriptVersion(scriptVersion);
		testcasescript.setSourceContolSystemId(sourceContolSystemId);
		testcasescript.setUri(uri);
		testcasescript.setRevision(revision);
		testcasescript.setLanguage(language);
		testcasescript.setPrimaryTestEngineId(primaryTestEngineId);
		
			
		ProductMaster product=new ProductMaster();
		if(productId != null) {
			product.setProductId(productId);
			testcasescript.setProduct(product);
		}
		
		return testcasescript;
	}
	
	public Integer getScriptId() {
		return scriptId;
	}
	public void setScriptId(Integer scriptId) {
		this.scriptId = scriptId;
	}
	public String getScriptName() {
		return scriptName;
	}
	public void setScriptName(String scriptName) {
		this.scriptName = scriptName;
	}
	public String getScriptQualifiedName() {
		return scriptQualifiedName;
	}
	public void setScriptQualifiedName(String scriptQualifiedName) {
		this.scriptQualifiedName = scriptQualifiedName;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public String getSource() {
		return source;
	}
	public void setSource(String source) {
		this.source = source;
	}
	public static Log getLog() {
		return log;
	}

	public String getScriptVersion() {
		return scriptVersion;
	}

	public void setScriptVersion(String scriptVersion) {
		this.scriptVersion = scriptVersion;
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

	public Integer getIsLatest() {
		return isLatest;
	}

	public void setIsLatest(Integer isLatest) {
		this.isLatest = isLatest;
	}

	public Integer getSourceContolSystemId() {
		return sourceContolSystemId;
	}

	public void setSourceContolSystemId(Integer sourceContolSystemId) {
		this.sourceContolSystemId = sourceContolSystemId;
	}

	public Integer getPrimaryTestEngineId() {
		return primaryTestEngineId;
	}

	public void setPrimaryTestEngineId(Integer primaryTestEngineId) {
		this.primaryTestEngineId = primaryTestEngineId;
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

	public String getModifiedFieldTitle() {
		return modifiedFieldTitle;
	}

	public void setModifiedFieldTitle(String modifiedFieldTitle) {
		this.modifiedFieldTitle = modifiedFieldTitle;
	}
}