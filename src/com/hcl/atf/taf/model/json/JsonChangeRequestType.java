package com.hcl.atf.taf.model.json;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.hcl.atf.taf.model.ChangeRequestType;

public class JsonChangeRequestType {
	
	private static final Log log = LogFactory.getLog(JsonChangeRequestType.class);
	
	@JsonProperty
	private Integer changeRequestTypeId;
	@JsonProperty
	private String changeRequestTypeName;
	public JsonChangeRequestType() {
		
	}
	public JsonChangeRequestType(ChangeRequestType changeRequestType) {		
		this.changeRequestTypeId = changeRequestType.getChangeRequestTypeId();
		this.changeRequestTypeName = changeRequestType.getChangeRequestTypeName();
	}	
	@JsonIgnore
	public ChangeRequestType getChangeRequestType() {
		ChangeRequestType changeRequestType = new ChangeRequestType();
		changeRequestType.setChangeRequestTypeId(changeRequestTypeId);
		changeRequestType.setChangeRequestTypeName(changeRequestTypeName);		
		return changeRequestType;

	}
}