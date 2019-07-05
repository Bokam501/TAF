package com.hcl.atf.taf.model.json;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.hcl.atf.taf.model.AuthenticationType;

public class JsonAuthenticationType implements java.io.Serializable {
	
	@JsonProperty
	private Integer authenticationTypeId;
	
	@JsonProperty	
	private String authenticationTypeName;
	
	@JsonProperty	
	private String authenticationTypeDescription;

	
	public JsonAuthenticationType(){
		
	}
	
	public JsonAuthenticationType(AuthenticationType authenticationType) {
		authenticationTypeId = authenticationType.getAuthenticationTypeId();
		authenticationTypeName = authenticationType.getAuthenticationTypeName();
		authenticationTypeDescription = authenticationType.getAuthenticationTypeDescription();
	}
	
	@JsonIgnore
	public AuthenticationType getAuthenticationType() {
		AuthenticationType authenticationType = new AuthenticationType();
		if (authenticationTypeId != null) {
			authenticationType.setAuthenticationTypeId(authenticationTypeId);
		}
		if (authenticationTypeName != null) {
			authenticationType.setAuthenticationTypeName(authenticationTypeName);
		}
		if (authenticationTypeDescription != null) {
			authenticationType.setAuthenticationTypeDescription(authenticationTypeDescription);
		}
		return authenticationType;
	}

	public Integer getAuthenticationTypeId() {
		return authenticationTypeId;
	}

	public void setAuthenticationTypeId(Integer authenticationTypeId) {
		this.authenticationTypeId = authenticationTypeId;
	}

	public String getAuthenticationTypeName() {
		return authenticationTypeName;
	}

	public void setAuthenticationTypeName(String authenticationTypeName) {
		this.authenticationTypeName = authenticationTypeName;
	}

	public String getAuthenticationTypeDescription() {
		return authenticationTypeDescription;
	}

	public void setAuthenticationTypeDescription(
			String authenticationTypeDescription) {
		this.authenticationTypeDescription = authenticationTypeDescription;
	}


}
