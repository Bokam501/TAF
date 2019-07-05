package com.hcl.atf.taf.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
@Entity
@Table(name="teststory_generated_scripts")
public class TestStoryGeneratedScripts implements Serializable{



    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Integer id;
	private TestCaseAutomationStory testCaseStory;
    private String testScript;
    private TestToolMaster testTool;
    private Languages languages;
    private UserList userList;
    private Date modifiedDate;
    private String modifiedBy;
    private String downloadPath;
    private String outputPackage;
    private String codeGenerationMode;
    private String testScriptForGeneric;
    private String outputpackageForGeneric;
    private String downloadPathForGeneric;
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="id", unique=true, nullable=false)
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	
	@ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="testCaseStoryId")
	public TestCaseAutomationStory getTestCaseStory() {
		return testCaseStory;
	}
	public void setTestCaseStory(TestCaseAutomationStory testCaseStory) {
		this.testCaseStory = testCaseStory;
	}
	
	
	@Column(name="testScript")
	public String getTestScript() {
		return testScript;
	}
	public void setTestScript(String testScript) {
		this.testScript = testScript;
	}
	
	@ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="testToolId")
	public TestToolMaster getTestTool() {
		return testTool;
	}
	public void setTestTool(TestToolMaster testTool) {
		this.testTool = testTool;
	}
	
	
	@ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="languageId")
	public Languages getLanguages() {
		return languages;
	}
	public void setLanguages(Languages languages) {
		this.languages = languages;
	}
	
	@ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="createdBy")
	public UserList getUserList() {
		return userList;
	}
	public void setUserList(UserList userList) {
		this.userList = userList;
	}
	
	@Column(name="modifiedDate")
	public Date getModifiedDate() {
		return modifiedDate;
	}
	public void setModifiedDate(Date modifiedDate) {
		this.modifiedDate = modifiedDate;
	}
	@Column(name="modifiedBy")
	public String getModifiedBy() {
		return modifiedBy;
	}
	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}
	
	@Column(name="downloadPath")
	public String getDownloadPath() {
		return downloadPath;
	}
	public void setDownloadPath(String downloadPath) {
		this.downloadPath = downloadPath;
	}
	
	@Column(name="outputPackage")
	public String getOutputPackage() {
		return outputPackage;
	}
	public void setOutputPackage(String outputPackage) {
		this.outputPackage = outputPackage;
	}
	
	
	@Column(name="codeGenerationMode")
	public String getCodeGenerationMode() {
		return codeGenerationMode;
	}
	public void setCodeGenerationMode(String codeGenerationMode) {
		this.codeGenerationMode = codeGenerationMode;
	}
	
	@Column(name="testScriptForGeneric")
	public String getTestScriptForGeneric() {
		return testScriptForGeneric;
	}
	public void setTestScriptForGeneric(String testScriptForGeneric) {
		this.testScriptForGeneric = testScriptForGeneric;
	}
	@Column(name="outputpackageForGeneric")
	public String getOutputpackageForGeneric() {
		return outputpackageForGeneric;
	}
	public void setOutputpackageForGeneric(String outputpackageForGeneric) {
		this.outputpackageForGeneric = outputpackageForGeneric;
	}
	@Column(name="downloadPathForGeneric")
	public String getDownloadPathForGeneric() {
		return downloadPathForGeneric;
	}
	public void setDownloadPathForGeneric(String downloadPathForGeneric) {
		this.downloadPathForGeneric = downloadPathForGeneric;
	}
	
	
}
    
