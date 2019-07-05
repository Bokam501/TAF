package com.hcl.atf.taf.model.json;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.hcl.atf.taf.model.UserTypeMaster;

public class JsonUserTypeMaster implements java.io.Serializable {

	@JsonProperty
	private String userType;
	
	@JsonIgnore
	private String userTypeDescription;
	
	public JsonUserTypeMaster() {
	}

	public JsonUserTypeMaster(String userType) {
		this.userType = userType;
	}

	public JsonUserTypeMaster(UserTypeMaster userTypeMaster) {
		this.userType = userTypeMaster.getUserType();
		this.userTypeDescription = userTypeMaster.getUserTypeDescription();
		
	}

		public String getUserType() {
		return this.userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}
	public String getUserTypeDescription() {
		return this.userTypeDescription;
	}

	public void setUserTypeDescription(String userTypeDescription) {
		this.userTypeDescription = userTypeDescription;
	}

	@JsonIgnore
	public UserTypeMaster getUserTypeMaster(){
		UserTypeMaster userTypeMaster = new UserTypeMaster();
		userTypeMaster.setUserType(userType);
		userTypeMaster.setUserTypeDescription(userTypeDescription);		
		return userTypeMaster;
	}
}
