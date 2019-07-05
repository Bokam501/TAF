package com.hcl.atf.taf.model.json;



import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.hcl.atf.taf.model.TestCaseAutomationStory;
import com.hcl.atf.taf.model.TestCaseList;

public class JsonTestCaseScript extends JsonTestCaseList implements java.io.Serializable {
	
	private static final Log log = LogFactory.getLog(JsonTestCaseScript.class);
	@JsonProperty
	private Integer testCaseAutomationScriptId;
	@JsonProperty
	private String testCaseClassName;
	@JsonProperty
	private String testCaseScript;
	@JsonProperty
	private String mainClassName;
	@JsonProperty
	private String mainClassScript;
	@JsonProperty
	private String testCaseRefClassName;
	@JsonProperty
	private String testCaseRefClassScript;
	@JsonProperty
	private String scriptType;
	@JsonProperty
	private String testEngine;
	@JsonProperty
	private List<String> keywords;
	@JsonProperty
	private String downloadPath;
	@JsonProperty
	private String mainFileCode;
	@JsonProperty
	private String identityFileCode;
	@JsonProperty
	private String scriptFileCode;
	@JsonProperty
	private Integer scriptFileLineofCode;
	@JsonProperty
	private String testEngineConfigFile;
	@JsonProperty
	private Integer versionId;
	@JsonProperty
	private String testToolName;
	@JsonProperty
	private List<String> keywordRegularExpressions;
	@JsonProperty
	private String isSCMSystemAvaialble;
	public JsonTestCaseScript() {
	}

	public JsonTestCaseScript(TestCaseList testCaseList) {

		super(testCaseList);
	}

	public JsonTestCaseScript(TestCaseAutomationStory testCaseAutomationStory, TestCaseList testCaseList) {
		super(testCaseList);
		this.testCaseAutomationScriptId = testCaseAutomationStory.getTestCaseAutomationStoryId();
		this.testCaseScript = testCaseAutomationStory.getScript();
		if(!testCaseAutomationStory.getScriptType().getScriptType().isEmpty() && testCaseAutomationStory.getScriptType().getScriptType() != null)
		this.scriptType = testCaseAutomationStory.getScriptType().getScriptType();
		if (testCaseAutomationStory.getTestTool() != null) {
			this.testEngine = testCaseAutomationStory.getTestTool().getTestToolName();
		}
		this.testCaseClassName = testCaseAutomationStory.getScriptFileName();
		if(null != testCaseAutomationStory.getTestEngineConfigFile())
			this.testEngineConfigFile = testCaseAutomationStory.getTestEngineConfigFile().trim();
		
		this.versionId = testCaseAutomationStory.getVersionId();
	}

	public String getTestCaseScript() {
		return testCaseScript;
	}

	public void setTestCaseScript(String testCaseScript) {
		this.testCaseScript = testCaseScript;
	}

	public String getMainClassScript() {
		return mainClassScript;
	}

	public void setMainClassScript(String mainClassScript) {
		this.mainClassScript = mainClassScript;
	}

	public String getScriptType() {
		return scriptType;
	}

	public void setScriptType(String scriptType) {
		this.scriptType = scriptType;
	}

	public String getTestEngine() {
		return testEngine;
	}

	public void setTestEngine(String testEngine) {
		this.testEngine = testEngine;
	}

	public String getTestCaseClassName() {
		return testCaseClassName;
	}

	public void setTestCaseClassName(String testCaseClassName) {
		this.testCaseClassName = testCaseClassName;
	}

	public String getMainClassName() {
		return mainClassName;
	}

	public void setMainClassName(String mainClassName) {
		this.mainClassName = mainClassName;
	}

	public String getTestCaseRefClassName() {
		return testCaseRefClassName;
	}

	public void setTestCaseRefClassName(String testCaseRefClassName) {
		this.testCaseRefClassName = testCaseRefClassName;
	}

	public String getTestCaseRefClassScript() {
		return testCaseRefClassScript;
	}

	public void setTestCaseRefClassScript(String testCaseRefClassScript) {
		this.testCaseRefClassScript = testCaseRefClassScript;
	}

	public List<String> getKeywords() {
		return keywords;
	}

	public void setKeywords(List<String> keywords) {
		this.keywords = keywords;
	}

	public Integer getTestCaseAutomationScriptId() {
		return testCaseAutomationScriptId;
	}

	public void setTestCaseAutomationScriptId(Integer testCaseAutomationScriptId) {
		this.testCaseAutomationScriptId = testCaseAutomationScriptId;
	}

	public String getDownloadPath() {
		return downloadPath;
	}

	public void setDownloadPath(String downloadPath) {
		this.downloadPath = downloadPath;
	}

	public String getMainFileCode() {
		return mainFileCode;
	}

	public void setMainFileCode(String mainFileCode) {
		this.mainFileCode = mainFileCode;
	}

	public String getIdentityFileCode() {
		return identityFileCode;
	}

	public void setIdentityFileCode(String identityFileCode) {
		this.identityFileCode = identityFileCode;
	}

	public String getScriptFileCode() {
		return scriptFileCode;
	}

	public void setScriptFileCode(String scriptFileCode) {
		this.scriptFileCode = scriptFileCode;
	}
	public Integer getScriptFileLineofCode() {
		return scriptFileLineofCode;
	}

	public void setScriptFileLineofCode(Integer scriptFileLineofCode) {
		this.scriptFileLineofCode = scriptFileLineofCode;
	}

	public String getTestEngineConfigFile() {
		return testEngineConfigFile;
	}

	public void setTestEngineConfigFile(String testEngineConfigFile) {
		this.testEngineConfigFile = testEngineConfigFile;
	}

	public Integer getVersionId() {
		return versionId;
	}

	public void setVersionId(Integer versionId) {
		this.versionId = versionId;
	}

	public String getTestToolName() {
		return testToolName;
	}

	public void setTestToolName(String testToolName) {
		this.testToolName = testToolName;
	}

	public List<String> getKeywordRegularExpressions() {
		return keywordRegularExpressions;
	}

	public void setKeywordRegularExpressions(List<String> keywordRegularExpressions) {
		this.keywordRegularExpressions = keywordRegularExpressions;
	}

	public String getIsSCMSystemAvaialble() {
		return isSCMSystemAvaialble;
	}

	public void setIsSCMSystemAvaialble(String isSCMSystemAvaialble) {
		this.isSCMSystemAvaialble = isSCMSystemAvaialble;
	}
	
}
