package com.hcl.atf.taf.model.json;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.hcl.atf.taf.model.AuthenticationMode;

public class JsonAuthenticationMode implements java.io.Serializable {
	
	@JsonProperty
	private Integer authenticationModeId;
	
	@JsonProperty	
	private String authenticationModeName;
	

	
	public JsonAuthenticationMode(){
		
	}
	
	public JsonAuthenticationMode(AuthenticationMode authenticationMode) {
		authenticationModeId = authenticationMode.getAuthenticationModeId();
		authenticationModeName = authenticationMode.getAuthenticationModeName();
	}
	
	@JsonIgnore
	public AuthenticationMode getAuthenticationMode() {
		AuthenticationMode authenticationMode = new AuthenticationMode();
		if (authenticationModeId != null) {
			authenticationMode.setAuthenticationModeId(authenticationModeId);
		}
		if (authenticationModeName != null) {
			authenticationMode.setAuthenticationModeName(authenticationModeName);
		}
		return authenticationMode;
	}

	public Integer getAuthenticationModeId() {
		return authenticationModeId;
	}

	public void setAuthenticationModeId(Integer authenticationModeId) {
		this.authenticationModeId = authenticationModeId;
	}

	public String getAuthenticationModeName() {
		return authenticationModeName;
	}

	public void setAuthenticationModeName(String authenticationModeName) {
		this.authenticationModeName = authenticationModeName;
	}

}
