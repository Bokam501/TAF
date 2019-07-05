package com.hcl.atf.taf.model.json;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.hcl.atf.taf.model.TestCasePriority;

public class JsonTestCasePriority implements java.io.Serializable {

	private static final Log log = LogFactory
			.getLog(JsonTestCasePriority.class);

	@JsonProperty
	private Integer testcasePriorityId;	
	@JsonProperty
	private String priorityName;
	@JsonProperty
	private Integer priorityLevelValue;
	@JsonProperty
	private String description;

	public JsonTestCasePriority() {		
	}

	public Integer getTestcasePriorityId() {
		return testcasePriorityId;
	}

	public void setTestcasePriorityId(Integer testcasePriorityId) {
		this.testcasePriorityId = testcasePriorityId;
	}

	public String getPriorityName() {
		return priorityName;
	}

	public void setPriorityName(String priorityName) {
		this.priorityName = priorityName;
	}

	public Integer getPriorityLevelValue() {
		return priorityLevelValue;
	}

	public void setPriorityLevelValue(Integer priorityLevelValue) {
		this.priorityLevelValue = priorityLevelValue;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public JsonTestCasePriority(TestCasePriority testcasePriority) {
		this.testcasePriorityId= testcasePriority.getTestcasePriorityId();
		this.priorityName = testcasePriority.getPriorityName();
		this.priorityLevelValue = testcasePriority.getPriorityLevelValue();
		this.description = testcasePriority.getDescription();			
	}	

	

	@JsonIgnore
	public TestCasePriority getTestcasePriority() {
		TestCasePriority testcasePriority = new TestCasePriority();
		testcasePriority.setTestcasePriorityId(testcasePriorityId);
		testcasePriority.setPriorityName(priorityName);
		testcasePriority.setPriorityLevelValue(priorityLevelValue);
		testcasePriority.setDescription(description);		
		return testcasePriority;
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
