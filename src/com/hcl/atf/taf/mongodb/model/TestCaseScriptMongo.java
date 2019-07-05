package com.hcl.atf.taf.mongodb.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import com.hcl.atf.taf.model.TestCaseScript;

@Document(collection = "testcasescript")
public class TestCaseScriptMongo {
	
	@Id
	private String id;
	private Integer scriptId;
	private String scriptName;
	private String scriptQualifiedName;
	private String source;
	private String scriptVersion;
	private Integer revision;
	private String uri;
	
	private Integer isLatest;
	private Integer sourceContolSystemId;
	private Integer primaryTestEngineId;
	private String language;
	private Integer productId;
	private String productName;
	private String testFactoryName;
	
	public TestCaseScriptMongo(){
		
	}
	
	public TestCaseScriptMongo(TestCaseScript testCaseScript) {
		
			this.id = testCaseScript.getScriptId()+"";
			this.scriptId = testCaseScript.getScriptId();
			this.scriptName = testCaseScript.getScriptName();
			this.scriptQualifiedName = testCaseScript.getScriptQualifiedName();
			this.source = testCaseScript.getSource();
			this.scriptVersion = testCaseScript.getScriptVersion();
			this.revision = testCaseScript.getRevision();
			this.isLatest = testCaseScript.getIsLatest();
			this.sourceContolSystemId = testCaseScript.getSourceContolSystemId();
			this.primaryTestEngineId = testCaseScript.getPrimaryTestEngineId();
			this.language = testCaseScript.getLanguage();
			
			
			if(testCaseScript.getProduct() != null){
				this.productId=testCaseScript.getProduct().getProductId();
				this.productName=testCaseScript.getProduct().getProductName();
				if(testCaseScript.getProduct().getTestFactory() != null){
					this.testFactoryName = testCaseScript.getProduct().getTestFactory().getTestFactoryName();
				}
			}
			
		}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
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

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
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

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
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

	public String getTestFactoryName() {
		return testFactoryName;
	}

	public void setTestFactoryName(String testFactoryName) {
		this.testFactoryName = testFactoryName;
	}
	
}
