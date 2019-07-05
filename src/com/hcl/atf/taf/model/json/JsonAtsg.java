package com.hcl.atf.taf.model.json;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.hcl.atf.taf.model.AtsgParameters;

public class JsonAtsg {
	@JsonProperty
	private Integer atsgId;
	@JsonProperty
	private Integer testCaseId;
	@JsonProperty
	private Integer objectRepositoryId;
	@JsonProperty
	private Integer productVersionId;
	@JsonProperty
	private String testEngine;
	
	@JsonProperty
	private Integer testDataId;
	
	
	public JsonAtsg() {
	}

	public JsonAtsg(AtsgParameters atsg) {
		if(atsg != null){
			this.atsgId = atsg.getAtsgId();
			this.testCaseId = atsg.getTestCaseId();
			this.objectRepositoryId = atsg.getObjectRepositoryId();
			this.productVersionId = atsg.getProductVersionId();
			this.testEngine = atsg.getTestEngine();
			this.testDataId = atsg.getTestDataId();
		}
		
	}

	public Integer getAtsgId() {
		return atsgId;
	}

	public void setAtsgId(Integer atsgId) {
		this.atsgId = atsgId;
	}

	
	public Integer getTestCaseId() {
		return testCaseId;
	}

	public void setTestCaseId(Integer testCaseId) {
		this.testCaseId = testCaseId;
	}

	public Integer getObjectRepositoryId() {
		return objectRepositoryId;
	}

	public void setObjectRepositoryId(Integer objectRepositoryId) {
		this.objectRepositoryId = objectRepositoryId;
	}

	public Integer getProductVersionId() {
		return productVersionId;
	}

	public void setProductVersionId(Integer productVersionId) {
		this.productVersionId = productVersionId;
	}

	public String getTestEngine() {
		return testEngine;
	}

	public void setTestEngine(String testEngine) {
		this.testEngine = testEngine;
	}

	public Integer getTestDataId() {
		return testDataId;
	}

	public void setTestDataId(Integer testDataId) {
		this.testDataId = testDataId;
	}
	
	
	
}
