package com.hcl.atf.taf.model;

import static javax.persistence.GenerationType.IDENTITY;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;


@Entity
@Table(name = "test_case_scripts") 
public class TestCaseScript implements java.io.Serializable{
		
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Integer scriptId;
	private String scriptName;
	private String scriptQualifiedName;
	private String source;
	private Set<TestCaseList> testCaseList = new HashSet<TestCaseList>();
	
	private String scriptVersion;
	private Integer revision;
	private String uri;
	
	private Integer isLatest;
	private Integer sourceContolSystemId;
	private Integer primaryTestEngineId;
	private String language;
	
	private Set<TestCaseScriptVersion> testcasescriptset = new HashSet<TestCaseScriptVersion>();
	
	private ProductMaster product;
	
	public TestCaseScript(){
		
	}
	
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "scriptId", unique = true, nullable = false)
	public Integer getScriptId() {
		return scriptId;
	}

	public void setScriptId(Integer scriptId) {
		this.scriptId = scriptId;
	}	
	
	
	@Column(name = "scriptName")
	public String getScriptName() {
		return scriptName;
	}
	
	public void setScriptName(String scriptName) {
		this.scriptName = scriptName;
	}
	
	@Column(name = "scriptQualifiedName")
	public String getScriptQualifiedName() {
		return scriptQualifiedName;
	}
	public void setScriptQualifiedName(String scriptQualifiedName) {
		this.scriptQualifiedName = scriptQualifiedName;
	}	
	
	
	@Column(name = "source")
	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "testcasescript",cascade=CascadeType.ALL)
	public Set<TestCaseScriptVersion> getTestcasescriptset() {
		return testcasescriptset;
	}
	public void setTestcasescriptset(Set<TestCaseScriptVersion> testcasescriptset) {
		this.testcasescriptset = testcasescriptset;
	}
	@Column(name="scriptVersion")
	public String getScriptVersion() {
		return scriptVersion;
	}

	public void setScriptVersion(String scriptVersion) {
		this.scriptVersion = scriptVersion;
	}

	@Column(name="revision")
	public Integer getRevision() {
		return revision;
	}

	public void setRevision(Integer revision) {
		this.revision = revision;
	}

	@Column(name="uri")
	public String getUri() {
		return uri;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}

	@Column(name="isLatest")
	public Integer getIsLatest() {
		return isLatest;
	}

	public void setIsLatest(Integer isLatest) {
		this.isLatest = isLatest;
	}

	
	@Column(name="sourceContolSystemId")
	public Integer getSourceContolSystemId() {
		return sourceContolSystemId;
	}

	public void setSourceContolSystemId(Integer sourceContolSystemId) {
		this.sourceContolSystemId = sourceContolSystemId;
	}

	
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "testcase_Script_has_test_case_list", joinColumns = { @JoinColumn(name = "scriptId", nullable = false, updatable = false) }, inverseJoinColumns = { @JoinColumn(name = "testCaseId", nullable = false, updatable = false) })
	public Set<TestCaseList> getTestCaseList() {
		return testCaseList;
	}

	public void setTestCaseList(Set<TestCaseList> testCaseList) {
		this.testCaseList = testCaseList;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "productId")
	public ProductMaster getProduct() {
		return product;
	}

	public void setProduct(ProductMaster product) {
		this.product = product;
	}

	
	@Column(name = "primaryTestEngineId")
	public Integer getPrimaryTestEngineId() {
		return primaryTestEngineId;
	}

	public void setPrimaryTestEngineId(Integer primaryTestEngineId) {
		this.primaryTestEngineId = primaryTestEngineId;
	}

	
	@Column(name="languages")
	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

}

