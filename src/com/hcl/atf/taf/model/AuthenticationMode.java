package com.hcl.atf.taf.model;

import static javax.persistence.GenerationType.IDENTITY;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "authentication_mode_master")
public class AuthenticationMode implements java.io.Serializable{
	private Integer authenticationModeId;
	private String authenticationModeName;
	
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "authenticationModeId", unique = true, nullable = false)
	public Integer getAuthenticationModeId() {
		return authenticationModeId;
	}
	public void setAuthenticationModeId(Integer authenticationModeId) {
		this.authenticationModeId = authenticationModeId;
	}
	
	@Column(name = "authenticationModeName")
	public String getAuthenticationModeName() {
		return authenticationModeName;
	}
	public void setAuthenticationModeName(String authenticationModeName) {
		this.authenticationModeName = authenticationModeName;
	}
}
