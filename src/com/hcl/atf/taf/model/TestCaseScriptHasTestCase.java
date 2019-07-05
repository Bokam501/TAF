package com.hcl.atf.taf.model;

import static javax.persistence.GenerationType.IDENTITY;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name="testcase_script_has_test_case_list")
public class TestCaseScriptHasTestCase {
	
	private Integer id;
	private Integer scriptId;
	private String scriptName;
	private Integer testCaseId;
	private String testCaseName;
	private UserList createdBy;
	private UserList modifiedBy;
	private Date createdDate;
	private Date modifiedDate;
	private Integer productId;
	private String productName;
	private Integer testFactoryId;
	private String testFactoryName;

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	public Integer getId() {
		return id;
	}
	
	
	
	@Column(name="scriptId")
	public Integer getScriptId() {
		return scriptId;
	}
	
	public void setScriptId(Integer scriptId) {
		this.scriptId = scriptId;
	}
	
	
	@Column(name="testCaseId")
	public Integer getTestCaseId() {
		return testCaseId;
	}
	
	public void setTestCaseId(Integer testCaseId) {
		this.testCaseId = testCaseId;
	}
	
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "createdBy")
	public UserList getCreatedBy() {
		return createdBy;
	}
	
	public void setCreatedBy(UserList createdBy) {
		this.createdBy = createdBy;
	}
	
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "modifiedBy")
	public UserList getModifiedBy() {
		return modifiedBy;
	}
	
	public void setModifiedBy(UserList modifiedBy) {
		this.modifiedBy = modifiedBy;
	}
	
	
	@Column(name="createdDate")
	public Date getCreatedDate() {
		return createdDate;
	}
	
	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}
	
	
	@Column(name="modifiedDate")
	public Date getModifiedDate() {
		return modifiedDate;
	}
	
	public void setModifiedDate(Date modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

	
	@Transient
	public String getScriptName() {
		return scriptName;
	}

	public void setScriptName(String scriptName) {
		this.scriptName = scriptName;
	}

	@Transient
	public String getTestCaseName() {
		return testCaseName;
	}

	public void setTestCaseName(String testCaseName) {
		this.testCaseName = testCaseName;
	}

	@Transient
	public Integer getProductId() {
		return productId;
	}

	public void setProductId(Integer productId) {
		this.productId = productId;
	}

	@Transient
	public String getProductName() {
		return productName;
	}

	@Transient
	public void setProductName(String productName) {
		this.productName = productName;
	}

	
	@Transient
	public Integer getTestFactoryId() {
		return testFactoryId;
	}

	public void setTestFactoryId(Integer testFactoryId) {
		this.testFactoryId = testFactoryId;
	}

	@Transient
	public String getTestFactoryName() {
		return testFactoryName;
	}

	public void setTestFactoryName(String testFactoryName) {
		this.testFactoryName = testFactoryName;
	}


	public void setId(Integer id) {
		this.id = id;
	}
	
	
	
}
