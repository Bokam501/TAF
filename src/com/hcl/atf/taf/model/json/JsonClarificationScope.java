package com.hcl.atf.taf.model.json;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.hcl.atf.taf.model.ClarificationScope;

public class JsonClarificationScope {
	
	@JsonProperty
	private Integer clarificationScopeId;
	@JsonProperty
	private String clarificationScopeName;
	@JsonProperty
	private Integer engagementId;
	public JsonClarificationScope() {
	
	}
	public JsonClarificationScope(ClarificationScope clarificationScope) {
		
		this.clarificationScopeId = clarificationScope.getClarificationScopeId();
		this.clarificationScopeName = clarificationScope.getClarificationScopeName();
		this.engagementId = clarificationScope.getEngagementId();
	}
	
	@JsonIgnore
	public ClarificationScope getClarificationScope(){
		ClarificationScope claraificationScope = new ClarificationScope();
		if(clarificationScopeId != null){
			claraificationScope.setClarificationScopeId(clarificationScopeId);
		}
		if(clarificationScopeName != null){
			claraificationScope.setClarificationScopeName(clarificationScopeName);
		}
		if(engagementId != null){
			claraificationScope.setEngagementId(engagementId);
		}
		return claraificationScope;
	}

}
