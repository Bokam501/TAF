package com.hcl.atf.taf.model;

import static javax.persistence.GenerationType.IDENTITY;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "clarification_scope")
public class ClarificationScope {
		
	private Integer clarificationScopeId;
	private String clarificationScopeName;
	private Integer engagementId;
	
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "clarificationScopeId",unique = true)
	public Integer getClarificationScopeId() {
		return clarificationScopeId;
	}
	public void setClarificationScopeId(Integer clarificationScopeId) {
		this.clarificationScopeId = clarificationScopeId;
	}
	@Column(name = "clarificationScopeName")
	public String getClarificationScopeName() {
		return clarificationScopeName;
	}
	public void setClarificationScopeName(String clarificationScopeName) {
		this.clarificationScopeName = clarificationScopeName;
	}
	@Column(name = "engagementId")
	public Integer getEngagementId() {
		return engagementId;
	}
	public void setEngagementId(Integer engagementId) {
		this.engagementId = engagementId;
	}
}
