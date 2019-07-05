package com.hcl.atf.taf.mongodb.model;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import com.hcl.atf.taf.model.TestCaseScriptHasTestCase;

@Document(collection = "testcasescriptmapping")
public class TestCaseScriptMappingMongo {

	@Id
	private String id;
	private String _class;
	private Integer testCaseScriptMappingMongoId;
	private Integer scriptId;
	private String scriptName;
	private Integer testCaseId;
	private String testCaseName;
	
	private Integer createdById;
	private String createdByName;
	private Integer modifiedById;
	private String modifiedByName;
	private Date createdDate;
	private Date modifiedDate;
	private String productName;
	private String testFactoryName;
	
	
	public TestCaseScriptMappingMongo(TestCaseScriptHasTestCase testCaseScriptHasTestCase) {
		this.id=testCaseScriptHasTestCase.getId()+"";
		this._class = this.getClass().getName().replace("\\", ".").replace("/", ".");
		this.scriptId = testCaseScriptHasTestCase.getScriptId();
		this.testCaseId = testCaseScriptHasTestCase.getTestCaseId();
		this.createdDate = testCaseScriptHasTestCase.getCreatedDate();
		this.modifiedDate = testCaseScriptHasTestCase.getModifiedDate();
		this.scriptName = testCaseScriptHasTestCase.getScriptName();
		this.testCaseName = testCaseScriptHasTestCase.getTestCaseName();
		this.productName = testCaseScriptHasTestCase.getProductName();
		this.testFactoryName = testCaseScriptHasTestCase.getTestFactoryName();		
		
		if(testCaseScriptHasTestCase.getCreatedBy() != null){
			this.createdById = testCaseScriptHasTestCase.getCreatedBy().getUserId();
			this.createdByName = testCaseScriptHasTestCase.getCreatedBy().getFirstName();
		}
		
		if(testCaseScriptHasTestCase.getModifiedBy() != null){
			this.modifiedById = testCaseScriptHasTestCase.getModifiedBy().getUserId();
			this.modifiedByName = testCaseScriptHasTestCase.getModifiedBy().getFirstName();
		}
	}


	public String getId() {
		return id;
	}

	public void setId(String _id) {
		this.id = _id;
	}

	public String get_class() {
		return _class;
	}

	public void set_class(String _class) {
		this._class = _class;
	}

	public Integer getTestCaseScriptMappingId() {
		return testCaseScriptMappingMongoId;
	}

	public void setTestCaseScriptMappingId(Integer testCaseScriptMappingId) {
		this.testCaseScriptMappingMongoId = testCaseScriptMappingId;
	}

	public Integer getTestCaseId() {
		return testCaseId;
	}

	public void setTestCaseId(Integer testCaseId) {
		this.testCaseId = testCaseId;
	}

	public String getTestCaseName() {
		return testCaseName;
	}

	public void setTestCaseName(String testCaseName) {
		this.testCaseName = testCaseName;
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


	public Integer getCreatedById() {
		return createdById;
	}


	public void setCreatedById(Integer createdById) {
		this.createdById = createdById;
	}


	public String getCreatedByName() {
		return createdByName;
	}


	public void setCreatedByName(String createdByName) {
		this.createdByName = createdByName;
	}


	public Integer getModifiedById() {
		return modifiedById;
	}


	public void setModifiedById(Integer modifiedById) {
		this.modifiedById = modifiedById;
	}


	public String getModifiedByName() {
		return modifiedByName;
	}


	public void setModifiedByName(String modifiedByName) {
		this.modifiedByName = modifiedByName;
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


	public Integer getTestCaseScriptMappingMongoId() {
		return testCaseScriptMappingMongoId;
	}


	public void setTestCaseScriptMappingMongoId(Integer testCaseScriptMappingMongoId) {
		this.testCaseScriptMappingMongoId = testCaseScriptMappingMongoId;
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
