package com.hcl.atf.taf.model.json;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.hcl.atf.taf.model.UserTypeMasterNew;

public class JsonUserTypeMasterNew implements java.io.Serializable {

	private static final Log log = LogFactory.getLog(JsonUserTypeMasterNew.class);
	
	
	@JsonProperty
	private Integer userTypeId;
	@JsonProperty
	private String userTypeName;
	@JsonProperty
	private String userTypeLabel;
	@JsonProperty
	private String userTypeDescription;
	
	public JsonUserTypeMasterNew() {
	}

	public JsonUserTypeMasterNew(Integer userTypeId) {
		this.userTypeId = userTypeId;
		this.userTypeName = userTypeName;
		this.userTypeLabel = userTypeLabel;
		this.userTypeDescription = userTypeDescription;
	}

	public JsonUserTypeMasterNew(UserTypeMasterNew userTypeMasterNew) {
		this.userTypeId = userTypeMasterNew.getUserTypeId();
		this.userTypeName = userTypeMasterNew.getUserTypeName();
		this.userTypeLabel = userTypeMasterNew.getUserTypeLabel();
		this.userTypeDescription = userTypeMasterNew.getUserTypeDescription();
	}

		
	public String getUserTypeDescription() {
		return this.userTypeDescription;
	}

	public void setUserTypeDescription(String userTypeDescription) {
		this.userTypeDescription = userTypeDescription;
	}

	@JsonIgnore
	public UserTypeMasterNew getUserTypeMasterNew(){		
		UserTypeMasterNew userTypeMasterNew = new UserTypeMasterNew();
		userTypeMasterNew.setUserTypeId(userTypeId);
		userTypeMasterNew.setUserTypeName(userTypeName);
		userTypeMasterNew.setUserTypeDescription(userTypeDescription);
		userTypeMasterNew.setUserTypeLabel(userTypeLabel);
		return userTypeMasterNew;
	}
	
	public Integer getUserTypeId() {
		return userTypeId;
	}

	public void setUserTypeId(Integer userTypeId) {
		this.userTypeId = userTypeId;
	}

	public String getUserTypeName() {
		return userTypeName;
	}

	public void setUserTypeName(String userTypeName) {
		this.userTypeName = userTypeName;
	}

	public String getUserTypeLabel() {
		return userTypeLabel;
	}

	public void setUserTypeLabel(String userTypeLabel) {
		this.userTypeLabel = userTypeLabel;
	}

}
