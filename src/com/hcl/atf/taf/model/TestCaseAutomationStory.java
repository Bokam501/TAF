
package com.hcl.atf.taf.model;

import com.hcl.atf.taf.model.ScriptTypeMaster;
import com.hcl.atf.taf.model.TestCaseList;
import com.hcl.atf.taf.model.TestToolMaster;
import com.hcl.atf.taf.model.UserList;
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
import org.hibernate.annotations.Type;

@Entity
@Table(name="test_case_automation_story")
public class TestCaseAutomationStory
implements Serializable {
	private Integer testCaseAutomationStoryId;
	private String name;
	private String description;
	private String scriptSource;
	private String scriptURI;
	private String script;
	private String scriptFileName;
	private TestCaseList testCase;
	private ScriptTypeMaster scriptType;
	private TestToolMaster testTool;
	private UserList user;
	private String testEngineConfigFile;
	private Date createdDate;
	private Date modifiedDate;
	private Integer versionId;
	private Integer productId;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name = "testCaseAutomationStoryId", unique = true, nullable = false)
	public Integer getTestCaseAutomationStoryId() {
		return testCaseAutomationStoryId;
	}

	public void setTestCaseAutomationStoryId(Integer testCaseAutomationStoryId) {
		this.testCaseAutomationStoryId = testCaseAutomationStoryId;
	}

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="testCaseId")
	public TestCaseList getTestCase() {
		return this.testCase;
	}

	public void setTestCase(TestCaseList testCase) {
		this.testCase = testCase;
	}

	@Column(name="name")
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name="description")
	@Type(type="text")
	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Column(name="scriptSource")
	public String getScriptSource() {
		return this.scriptSource;
	}

	public void setScriptSource(String scriptSource) {
		this.scriptSource = scriptSource;
	}

	@Column(name="scriptURI")
	@Type(type="text")
	public String getScriptURI() {
		return this.scriptURI;
	}

	public void setScriptURI(String scriptURI) {
		this.scriptURI = scriptURI;
	}

	@Column(name="script")
	@Type(type="text")
	public String getScript() {
		return this.script;
	}

	public void setScript(String script) {
		this.script = script;
	}

	@Column(name="scriptFileName")
	public String getScriptFileName() {
		return this.scriptFileName;
	}

	public void setScriptFileName(String scriptFileName) {
		this.scriptFileName = scriptFileName;
	}

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="scriptType")
	public ScriptTypeMaster getScriptType() {
		return this.scriptType;
	}

	public void setScriptType(ScriptTypeMaster scriptType) {
		this.scriptType = scriptType;
	}

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="testToolId")
	public TestToolMaster getTestTool() {
		return this.testTool;
	}

	public void setTestTool(TestToolMaster testTool) {
		this.testTool = testTool;
	}

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="userId")
	public UserList getUser() {
		return this.user;
	}

	public void setUser(UserList user) {
		this.user = user;
	}

	@Column(name="testEngineConfigFile")
	public String getTestEngineConfigFile() {
		return testEngineConfigFile;
	}

	public void setTestEngineConfigFile(String testEngineConfigFile) {
		this.testEngineConfigFile = testEngineConfigFile;
	}


	@Column(name="createdDate")
	public Date getCreatedDate() {
		return this.createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	@Column(name="modifiedDate")
	public Date getModifiedDate() {
		return this.modifiedDate;
	}

	public void setModifiedDate(Date modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

	@Column(name="versionId")
	public Integer getVersionId() {
		return versionId;
	}

	public void setVersionId(Integer versionId) {
		this.versionId = versionId;
	}

	
	@Column(name="productId")
	public Integer getProductId() {
		return productId;
	}

	public void setProductId(Integer productId) {
		this.productId = productId;
	}

	public boolean equals(Object testCaseAutomationStory) {
		if (this.testCaseAutomationStoryId == null) {
			return false;
		}
		TestCaseAutomationStory testCaseAutomationStoryId = (TestCaseAutomationStory)testCaseAutomationStory;
		if (testCaseAutomationStoryId.getTestCaseAutomationStoryId().equals(this.testCaseAutomationStoryId)) {
			return true;
		}
		return false;
	}
}