package com.hcl.atf.taf.model.json;

// Generated Feb 4, 2014 4:30:16 PM by Hibernate Tools 3.4.0.CR1

import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.hcl.atf.taf.model.ScriptTypeMaster;
import com.hcl.atf.taf.model.TestCaseAutomationStory;
import com.hcl.atf.taf.model.TestCaseList;
import com.hcl.atf.taf.model.TestToolMaster;

public class JsonTestCaseAutomationScript implements java.io.Serializable {
	
	private static final Log log = LogFactory.getLog(JsonTestCaseList.class);

	@JsonProperty
	private Integer testCaseAutomationStoryId;
	@JsonProperty
	private String name;
	@JsonProperty
	private String description;

	@JsonProperty
	private String scriptSource;
	@JsonProperty
	private String scriptURI;
	@JsonProperty
	private String script;
	@JsonProperty
	private String scriptFileName;
	
	@JsonProperty
	private Integer testCaseId;
	@JsonProperty
	private String scriptType;
	@JsonProperty
	private Integer testToolId;
	@JsonProperty
	private String testTool;
	
	@JsonProperty
	private Integer userId;
	@JsonProperty
	private String userName;
	@JsonProperty
	private Date createdDate;
	@JsonProperty
	private Date modifiedDate;
	@JsonProperty
	private String modifiedField;
	@JsonProperty
	private String 	modifiedFieldTitle;	
	@JsonProperty
	private String oldFieldID;
	@JsonProperty
	private String	oldFieldValue;
	@JsonProperty
	private String	modifiedFieldID;
	@JsonProperty
	private String modifiedFieldValue;
	
	public JsonTestCaseAutomationScript() {
	}
	
	public JsonTestCaseAutomationScript(TestCaseAutomationStory testCaseAutomationStory) {
		
		this.testCaseAutomationStoryId = testCaseAutomationStory.getTestCaseAutomationStoryId();
		this.name = testCaseAutomationStory.getName();
		this.description = testCaseAutomationStory.getDescription();
		this.scriptSource = testCaseAutomationStory.getScriptSource();
		this.scriptURI = testCaseAutomationStory.getScriptURI();
		this.script = testCaseAutomationStory.getScript();
		this.scriptFileName = testCaseAutomationStory.getScriptFileName();
		
		this.createdDate = testCaseAutomationStory.getCreatedDate();
		this.modifiedDate = testCaseAutomationStory.getModifiedDate();
		
		this.testCaseId = testCaseAutomationStory.getTestCase().getTestCaseId();
		this.scriptType = testCaseAutomationStory.getScriptType().getScriptType();
		this.testToolId = testCaseAutomationStory.getTestTool().getTestToolId();
		this.testTool = testCaseAutomationStory.getTestTool().getTestToolName();
	}

	public TestCaseAutomationStory getTestCaseAutomationStory() {
		
		TestCaseAutomationStory script = new TestCaseAutomationStory();
		script.setTestCaseAutomationStoryId(this.testCaseAutomationStoryId);
		script.setName(this.name);
		script.setDescription(this.description);
		script.setScript(this.script);
		script.setScriptFileName(this.scriptFileName);
		script.setScriptSource(this.scriptSource);
		script.setScriptURI(this.scriptURI);
		
		script.setCreatedDate(this.createdDate);
		script.setModifiedDate(this.modifiedDate);
		
		TestToolMaster testTool = new TestToolMaster();
		testTool.setTestToolId(this.testToolId);
		testTool.setTestToolName(this.testTool);
		
		ScriptTypeMaster scriptType = new ScriptTypeMaster();
		scriptType.setScriptType(this.scriptType);
		
		TestCaseList testCase = new TestCaseList();
		testCase.setTestCaseId(this.testCaseId);
		
		
		return null;
	}
	
	public Integer getTestCaseAutomationStoryId() {
		return testCaseAutomationStoryId;
	}

	public void setTestCaseAutomationStoryId(Integer testCaseAutomationStoryId) {
		this.testCaseAutomationStoryId = testCaseAutomationStoryId;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getScriptSource() {
		return scriptSource;
	}

	public void setScriptSource(String scriptSource) {
		this.scriptSource = scriptSource;
	}

	public String getScriptURI() {
		return scriptURI;
	}

	public void setScriptURI(String scriptURI) {
		this.scriptURI = scriptURI;
	}

	public String getScript() {
		return script;
	}

	public void setScript(String script) {
		this.script = script;
	}

	public String getScriptFileName() {
		return scriptFileName;
	}

	public void setScriptFileName(String scriptFileName) {
		this.scriptFileName = scriptFileName;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public Date getModifiedDate() {
		return modifiedDate;
	}

	public void setModifiedDate(Date modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

	@Override
	public boolean equals(Object testCaseAutomationStory) {
	
		if (testCaseAutomationStoryId == null)
			return false;
		JsonTestCaseAutomationScript jsonTestCaseAutomationScript = (JsonTestCaseAutomationScript) testCaseAutomationStory;
		if (jsonTestCaseAutomationScript.getTestCaseAutomationStoryId().equals(this.testCaseAutomationStoryId)) {
			return true;
		} else {
			return false;
		}
	}
	
	public Integer getTestCaseId() {
		return testCaseId;
	}

	public void setTestCaseId(Integer testCaseId) {
		this.testCaseId = testCaseId;
	}

	public String getScriptType() {
		return scriptType;
	}

	public void setScriptType(String scriptType) {
		this.scriptType = scriptType;
	}

	public Integer getTestToolId() {
		return testToolId;
	}

	public void setTestToolId(Integer testToolId) {
		this.testToolId = testToolId;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public String getTestTool() {
		return testTool;
	}

	public void setTestTool(String testTool) {
		this.testTool = testTool;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getModifiedField() {
		return modifiedField;
	}

	public void setModifiedField(String modifiedField) {
		this.modifiedField = modifiedField;
	}

	public String getModifiedFieldTitle() {
		return modifiedFieldTitle;
	}

	public void setModifiedFieldTitle(String modifiedFieldTitle) {
		this.modifiedFieldTitle = modifiedFieldTitle;
	}

	public String getOldFieldID() {
		return oldFieldID;
	}

	public void setOldFieldID(String oldFieldID) {
		this.oldFieldID = oldFieldID;
	}

	public String getOldFieldValue() {
		return oldFieldValue;
	}

	public void setOldFieldValue(String oldFieldValue) {
		this.oldFieldValue = oldFieldValue;
	}

	public String getModifiedFieldID() {
		return modifiedFieldID;
	}

	public void setModifiedFieldID(String modifiedFieldID) {
		this.modifiedFieldID = modifiedFieldID;
	}

	public String getModifiedFieldValue() {
		return modifiedFieldValue;
	}

	public void setModifiedFieldValue(String modifiedFieldValue) {
		this.modifiedFieldValue = modifiedFieldValue;
	}
}
