package com.hcl.atf.taf.model;

import static javax.persistence.GenerationType.IDENTITY;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;


@Entity
@Table(name = "test_case_scripts_version") 
public class TestCaseScriptVersion implements java.io.Serializable {
	
	private TestCaseScript testcasescript;
	private Integer scriptVersionId;
	private Integer scriptversion;
	private Integer revision;
	private String uri;
	private Integer status;
	private Integer isSelected;
	
	public TestCaseScriptVersion(){
		
	}
	
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "scriptVersionId", unique = true, nullable = false)
	public Integer getScriptVersionId() {
		return scriptVersionId;
	}

	public void setScriptVersionId(Integer scriptVersionId) {
		this.scriptVersionId = scriptVersionId;
	}
	
	@Column(name = "scriptversion")
	public Integer getScriptversion() {
		return scriptversion;
	}

	public void setScriptversion(Integer scriptversion) {
		this.scriptversion = scriptversion;
	}
	
	@Column(name = "revision")
	public Integer getRevision() {
		return revision;
	}

	public void setRevision(Integer revision) {
		this.revision = revision;
	}
	
	@Column(name = "uri")
	public String getUri() {
		return uri;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}
	
	@Column(name = "status")
	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}
	
	@Column(name = "isSelected")
	public Integer getIsSelected() {
		return isSelected;
	}

	public void setIsSelected(Integer isSelected) {
		this.isSelected = isSelected;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "scriptId")
	public TestCaseScript getTestcasescript() {
		return testcasescript;
	}

	public void setTestcasescript(TestCaseScript testcasescript) {
		this.testcasescript = testcasescript;
	}
}
