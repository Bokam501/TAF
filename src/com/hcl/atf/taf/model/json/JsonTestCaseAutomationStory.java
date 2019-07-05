
package com.hcl.atf.taf.model.json;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.hcl.atf.taf.model.ScriptTypeMaster;
import com.hcl.atf.taf.model.TestCaseAutomationStory;
import com.hcl.atf.taf.model.TestCaseList;
import com.hcl.atf.taf.model.TestToolMaster;
import com.hcl.atf.taf.model.json.JsonTestCaseList;
import java.io.Serializable;
import java.util.Date;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class JsonTestCaseAutomationStory
implements Serializable {
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
    private String modifiedFieldTitle;
    @JsonProperty
    private String oldFieldID;
    @JsonProperty
    private String oldFieldValue;
    @JsonProperty
    private String modifiedFieldID;
    @JsonProperty
    private String modifiedFieldValue;
    @JsonProperty
    private Integer versionId;
    @JsonProperty
    private Integer productId;
    

    public JsonTestCaseAutomationStory() {
    }

    public JsonTestCaseAutomationStory(TestCaseAutomationStory testCaseAutomationStory) {
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
        this.versionId = testCaseAutomationStory.getVersionId();
        this.productId = testCaseAutomationStory.getProductId();
    }

    public TestCaseAutomationStory getTestCaseAutomationStory() {
    	TestCaseAutomationStory story = new TestCaseAutomationStory();
    	story.setTestCaseAutomationStoryId(this.testCaseAutomationStoryId);
    	story.setName(this.name);
    	story.setDescription(this.description);
    	story.setScript(this.script);
    	story.setScriptFileName(this.scriptFileName);
    	story.setScriptSource(this.scriptSource);
    	story.setScriptURI(this.scriptURI);
    	story.setCreatedDate(this.createdDate);
    	story.setModifiedDate(this.modifiedDate);
        TestToolMaster testTool = new TestToolMaster();
        testTool.setTestToolId(this.testToolId);
        testTool.setTestToolName(this.testTool);
        ScriptTypeMaster scriptType = new ScriptTypeMaster();
        scriptType.setScriptType("BDD");
        TestCaseList testCase = new TestCaseList();
        testCase.setTestCaseId(this.testCaseId);
        story.setProductId(this.productId);
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
        return this.scriptSource;
    }

    public void setScriptSource(String scriptSource) {
        this.scriptSource = scriptSource;
    }

    public String getScriptURI() {
        return this.scriptURI;
    }

    public void setScriptURI(String scriptURI) {
        this.scriptURI = scriptURI;
    }

    public String getScript() {
        return this.script;
    }

    public void setScript(String script) {
        this.script = script;
    }

    public String getScriptFileName() {
        return this.scriptFileName;
    }

    public void setScriptFileName(String scriptFileName) {
        this.scriptFileName = scriptFileName;
    }

    public Date getCreatedDate() {
        return this.createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Date getModifiedDate() {
        return this.modifiedDate;
    }

    public void setModifiedDate(Date modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    public boolean equals(Object testCaseAutomationStory) {
        if (this.testCaseAutomationStoryId == null) {
            return false;
        }
        JsonTestCaseAutomationStory testCaseAutomationStoryId = (JsonTestCaseAutomationStory)testCaseAutomationStory;
        if (testCaseAutomationStoryId.getTestCaseAutomationStoryId().equals(this.testCaseAutomationStoryId)) {
            return true;
        }
        return false;
    }

    public Integer getTestCaseId() {
        return this.testCaseId;
    }

    public void setTestCaseId(Integer testCaseId) {
        this.testCaseId = testCaseId;
    }

    public String getScriptType() {
        return this.scriptType;
    }

    public void setScriptType(String scriptType) {
        this.scriptType = scriptType;
    }

    public Integer getTestToolId() {
        return this.testToolId;
    }

    public void setTestToolId(Integer testToolId) {
        this.testToolId = testToolId;
    }

    public Integer getUserId() {
        return this.userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getTestTool() {
        return this.testTool;
    }

    public void setTestTool(String testTool) {
        this.testTool = testTool;
    }

    public String getUserName() {
        return this.userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getModifiedField() {
        return this.modifiedField;
    }

    public void setModifiedField(String modifiedField) {
        this.modifiedField = modifiedField;
    }

    public String getModifiedFieldTitle() {
        return this.modifiedFieldTitle;
    }

    public void setModifiedFieldTitle(String modifiedFieldTitle) {
        this.modifiedFieldTitle = modifiedFieldTitle;
    }

    public String getOldFieldID() {
        return this.oldFieldID;
    }

    public void setOldFieldID(String oldFieldID) {
        this.oldFieldID = oldFieldID;
    }

    public String getOldFieldValue() {
        return this.oldFieldValue;
    }

    public void setOldFieldValue(String oldFieldValue) {
        this.oldFieldValue = oldFieldValue;
    }

    public String getModifiedFieldID() {
        return this.modifiedFieldID;
    }

    public void setModifiedFieldID(String modifiedFieldID) {
        this.modifiedFieldID = modifiedFieldID;
    }

    public String getModifiedFieldValue() {
        return this.modifiedFieldValue;
    }

    public void setModifiedFieldValue(String modifiedFieldValue) {
        this.modifiedFieldValue = modifiedFieldValue;
    }

	public Integer getVersionId() {
		return versionId;
	}

	public void setVersionId(Integer versionId) {
		this.versionId = versionId;
	}

	public Integer getProductId() {
		return productId;
	}

	public void setProductId(Integer productId) {
		this.productId = productId;
	}
    
}
