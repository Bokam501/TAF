package com.hcl.atf.taf.model;

import static javax.persistence.GenerationType.IDENTITY;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "environment_group")
public class EnvironmentGroup implements java.io.Serializable {

	private Integer environmentGroupId;
	private String environmentGroupName;
	private String description;
	private Date createdDate;
	private Date modifiedDate;
	private Integer status;
	private String displayName;
	
	public EnvironmentGroup() {
	}
	
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "environment_group_id", unique = true, nullable = false)
	public Integer getEnvironmentGroupId() {
		return environmentGroupId;
	}
	public void setEnvironmentGroupId(Integer environmentGroupId) {
		this.environmentGroupId = environmentGroupId;
	}
	@Column(name = "environment_group_name", length = 100)
	public String getEnvironmentGroupName() {
		return environmentGroupName;
	}
	public void setEnvironmentGroupName(String environmentGroupName) {
		this.environmentGroupName = environmentGroupName;
	}
	@Column(name = "environment_group_description", length = 100)
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "createdDate")
	public Date getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "modifiedDate")
	public Date getModifiedDate() {
		return modifiedDate;
	}
	public void setModifiedDate(Date modifiedDate) {
		this.modifiedDate = modifiedDate;
	}
	@Column(name = "status")
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	@Column(name = "environment_group_displayName", length = 100)
	public String getDisplayName() {
		return displayName;
	}
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}
	
	
}
