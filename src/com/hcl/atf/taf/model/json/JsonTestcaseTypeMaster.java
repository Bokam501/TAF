package com.hcl.atf.taf.model.json;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.hcl.atf.taf.model.TestcaseTypeMaster;

public class JsonTestcaseTypeMaster implements java.io.Serializable {

	private static final Log log = LogFactory
			.getLog(JsonTestcaseTypeMaster.class);

	@JsonProperty
	private Integer testcaseTypeId;	
	@JsonProperty
	private String name;	
	@JsonProperty
	private String description;
	

	public Integer getTestcaseTypeId() {
		return testcaseTypeId;
	}

	public void setTestcaseTypeId(Integer testcaseTypeId) {
		this.testcaseTypeId = testcaseTypeId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public JsonTestcaseTypeMaster() {		
	}	

	public JsonTestcaseTypeMaster(TestcaseTypeMaster testcaseTypeMaster) {
		this.testcaseTypeId = testcaseTypeMaster.getTestcaseTypeId();
		this.name = testcaseTypeMaster.getName();
		this.description = testcaseTypeMaster.getDescription();					
	}

	@JsonIgnore
	public TestcaseTypeMaster getTestcaseTypeMaster() {
		TestcaseTypeMaster testcaseTypeMaster = new TestcaseTypeMaster();
		testcaseTypeMaster.setTestcaseTypeId(testcaseTypeId);
		testcaseTypeMaster.setName(name);
		testcaseTypeMaster.setDescription(description);				
		return testcaseTypeMaster;
	}

	public static boolean isInteger(String s) {
		try {
			Integer.parseInt(s);
		} catch (NumberFormatException e) {
			return false;
		}
		// only got here if we didn't return false
		return true;
	}	

}
