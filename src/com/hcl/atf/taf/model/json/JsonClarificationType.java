package com.hcl.atf.taf.model.json;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.hcl.atf.taf.model.ClarificationTypeMaster;


public class JsonClarificationType {

	@JsonProperty
	private Integer clarificationId;
	@JsonProperty
	private String clarificationType;

	public JsonClarificationType() {

	}
	public JsonClarificationType(ClarificationTypeMaster clarificationType) {
		
		this.clarificationId = clarificationType.getClarificationId();
		this.clarificationType = clarificationType.getClarificationType();
	}
	@JsonIgnore
	public ClarificationTypeMaster getClarificationType() {
		ClarificationTypeMaster clarificationTypeMaster = new ClarificationTypeMaster();
		if (clarificationId != null) {
			clarificationTypeMaster.setClarificationId(clarificationId);
		}
		if (clarificationType != null) {
			clarificationTypeMaster.setClarificationType(clarificationType);
		}
		return clarificationTypeMaster;
	}	
}
