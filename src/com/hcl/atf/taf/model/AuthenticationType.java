package com.hcl.atf.taf.model;

import static javax.persistence.GenerationType.IDENTITY;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "authentication_type")
public class AuthenticationType implements java.io.Serializable{
	private Integer authenticationTypeId;
	private String authenticationTypeName;
	private String authenticationTypeDescription;
	
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "authenticationTypeId", unique = true, nullable = false)
	public Integer getAuthenticationTypeId() {
		return authenticationTypeId;
	}
	public void setAuthenticationTypeId(Integer authenticationTypeId) {
		this.authenticationTypeId = authenticationTypeId;
	}
	
	@Column(name = "authenticationName")
	public String getAuthenticationTypeName() {
		return authenticationTypeName;
	}
	public void setAuthenticationTypeName(String authenticationTypeName) {
		this.authenticationTypeName = authenticationTypeName;
	}
	
	@Column(name = "authenticationDescription")
	public String getAuthenticationTypeDescription() {
		return authenticationTypeDescription;
	}
	public void setAuthenticationTypeDescription(String authenticationTypeDescription) {
		this.authenticationTypeDescription = authenticationTypeDescription;
	}
	
}
