package com.hcl.atf.taf.model.json;

import java.io.Serializable;
import java.util.Date;

import com.hcl.atf.taf.model.Languages;
import com.hcl.atf.taf.model.TestCaseAutomationStory;
import com.hcl.atf.taf.model.TestStoryGeneratedScripts;
import com.hcl.atf.taf.model.TestToolMaster;
import com.hcl.atf.taf.model.UserList;

public class JsonTestStoryGeneratedScripts implements Serializable{

	private Integer id;
	private Integer testCaseStoryId;
    private String testScript;
    private Integer testToolId;
    private Integer languageId;
    private Integer createdBy;
    private Date modifiedDate;
    private String modifiedBy;
    private String downloadPath;
    private String outputPackage;
    private String codeGenerationMode;
    private String testScriptForGeneric;
    private String outputpackageForGeneric;
    private String downloadPathForGeneric;
    
	public JsonTestStoryGeneratedScripts() {
    	
	}
    public JsonTestStoryGeneratedScripts(TestStoryGeneratedScripts testStoryGeneratedScripts){
    	this.id = testStoryGeneratedScripts.getId();
    	this.testCaseStoryId = testStoryGeneratedScripts.getTestCaseStory().getTestCaseAutomationStoryId();
    	this.testScript = testStoryGeneratedScripts.getTestScript();
    	this.testToolId = testStoryGeneratedScripts.getTestTool().getTestToolId();
    	if(testStoryGeneratedScripts.getLanguages().getId() != null)
    	this.languageId = testStoryGeneratedScripts.getLanguages().getId();
    	if (testStoryGeneratedScripts.getUserList() != null ) {
			this.createdBy = testStoryGeneratedScripts.getUserList()
					.getUserId();
		}
		this.modifiedDate = testStoryGeneratedScripts.getModifiedDate();
		this.modifiedBy = testStoryGeneratedScripts.getModifiedBy();
		this.downloadPath = testStoryGeneratedScripts.getDownloadPath();
		this.outputPackage = testStoryGeneratedScripts.getOutputPackage();
		this.codeGenerationMode = testStoryGeneratedScripts.getCodeGenerationMode();
		this.testScriptForGeneric = testStoryGeneratedScripts.getTestScriptForGeneric();
		this.outputpackageForGeneric = testStoryGeneratedScripts.getOutputpackageForGeneric();
		this.downloadPathForGeneric = testStoryGeneratedScripts.getDownloadPathForGeneric();
    }
    public TestStoryGeneratedScripts getTestStoryGeneratedScripts(){
    	TestStoryGeneratedScripts testStoryGeneratedScripts = new TestStoryGeneratedScripts();
    	testStoryGeneratedScripts.setId(this.id);
    	TestCaseAutomationStory testCaseAutomationStory = new TestCaseAutomationStory();
    	testCaseAutomationStory.setTestCaseAutomationStoryId(this.testCaseStoryId);
    	testStoryGeneratedScripts.setTestScript(this.testScript);
    	TestToolMaster toolMaster = new TestToolMaster();
    	toolMaster.setTestToolId(this.testToolId);
    	Languages languages = new Languages();
    	languages.setId(this.id);
    	UserList userList = new UserList();
    	userList.setUserId(this.createdBy);
    	testStoryGeneratedScripts.setModifiedDate(this.modifiedDate);
    	testStoryGeneratedScripts.setModifiedBy(this.modifiedBy);
    	testStoryGeneratedScripts.setDownloadPath(this.downloadPath);
    	testStoryGeneratedScripts.setOutputPackage(this.outputPackage);
    	testStoryGeneratedScripts.setCodeGenerationMode(this.codeGenerationMode);
    	testStoryGeneratedScripts.setTestScriptForGeneric(this.testScriptForGeneric);
    	testStoryGeneratedScripts.setOutputpackageForGeneric(this.outputpackageForGeneric);
    	testStoryGeneratedScripts.setDownloadPathForGeneric(this.downloadPathForGeneric);
		return null;
    }
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getTestCaseStoryId() {
		return testCaseStoryId;
	}
	public void setTestCaseStoryId(Integer testCaseStoryId) {
		this.testCaseStoryId = testCaseStoryId;
	}
	
	public String getTestScript() {
		return testScript;
	}
	public void setTestScript(String testScript) {
		this.testScript = testScript;
	}
	public Integer getTestToolId() {
		return testToolId;
	}
	public void setTestToolId(Integer testToolId) {
		this.testToolId = testToolId;
	}
	public Integer getLanguageId() {
		return languageId;
	}
	public void setLanguageId(Integer languageId) {
		this.languageId = languageId;
	}
	public Integer getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(Integer createdBy) {
		this.createdBy = createdBy;
	}
	public Date getModifiedDate() {
		return modifiedDate;
	}
	public void setModifiedDate(Date modifiedDate) {
		this.modifiedDate = modifiedDate;
	}
	public String getModifiedBy() {
		return modifiedBy;
	}
	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}
	public String getDownloadPath() {
		return downloadPath;
	}
	public void setDownloadPath(String downloadPath) {
		this.downloadPath = downloadPath;
	}
	public String getOutputPackage() {
		return outputPackage;
	}
	public void setOutputPackage(String outputPackage) {
		this.outputPackage = outputPackage;
	}
	public String getCodeGenerationMode() {
		return codeGenerationMode;
	}
	public void setCodeGenerationMode(String codeGenerationMode) {
		this.codeGenerationMode = codeGenerationMode;
	}
	public String getTestScriptForGeneric() {
		return testScriptForGeneric;
	}
	public void setTestScriptForGeneric(String testScriptForGeneric) {
		this.testScriptForGeneric = testScriptForGeneric;
	}
	public String getOutputpackageForGeneric() {
		return outputpackageForGeneric;
	}
	public void setOutputpackageForGeneric(String outputpackageForGeneric) {
		this.outputpackageForGeneric = outputpackageForGeneric;
	}
	public String getDownloadPathForGeneric() {
		return downloadPathForGeneric;
	}
	public void setDownloadPathForGeneric(String downloadPathForGeneric) {
		this.downloadPathForGeneric = downloadPathForGeneric;
	}
	
	
}
