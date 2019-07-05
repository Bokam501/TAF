package com.hcl.atf.taf.model;

// Generated Feb 4, 2014 4:30:16 PM by Hibernate Tools 3.4.0.CR1

import static javax.persistence.GenerationType.IDENTITY;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;


@Entity
@Table(name = "user_resourcepool_mapping")
public class UserResourcePoolMapping implements java.io.Serializable {

	private Integer resourcePoolMappingId;	
	private UserList userId;
	private TestfactoryResourcePool resourcepoolId;
	private Date fromDate;
	private Date toDate;
	private Date mappedDate;
	private UserList mappedBy;
	private String remarks;
	
	private UserList modifiedBy;
	private Date modifiedDate;
	private UserRoleMaster userRoleId;
	
		

	public UserResourcePoolMapping() {
	}

	

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "resourcePoolMappingId", unique = true, nullable = false)
	public Integer getResourcePoolMappingId() {
		return resourcePoolMappingId;
	}

	public void setResourcePoolMappingId(Integer resourcePoolMappingId) {
		this.resourcePoolMappingId = resourcePoolMappingId;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "userId")
	public UserList getUserId() {
		return userId;
	}

	public void setUserId(UserList userId) {
		this.userId = userId;
	}

	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "resourcepoolId")
	public TestfactoryResourcePool getResourcepoolId() {
		return resourcepoolId;
	}

	public void setResourcepoolId(TestfactoryResourcePool resourcepoolId) {
		this.resourcepoolId = resourcepoolId;
	}

	public Date getFromDate() {
		return fromDate;
	}

	public void setFromDate(Date fromDate) {
		this.fromDate = fromDate;
	}

	public Date getToDate() {
		return toDate;
	}

	public void setToDate(Date toDate) {
		this.toDate = toDate;
	}

	public Date getMappedDate() {
		return mappedDate;
	}

	public void setMappedDate(Date mappedDate) {
		this.mappedDate = mappedDate;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "mappedBy")
	public UserList getMappedBy() {
		return mappedBy;
	}

	public void setMappedBy(UserList mappedBy) {
		this.mappedBy = mappedBy;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}


	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "modifiedBy")
	public UserList getModifiedBy() {
		return modifiedBy;
	}



	public void setModifiedBy(UserList modifiedBy) {
		this.modifiedBy = modifiedBy;
	}



	public Date getModifiedDate() {
		return modifiedDate;
	}



	public void setModifiedDate(Date modifiedDate) {
		this.modifiedDate = modifiedDate;
	}


	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "userRoleId")
	public UserRoleMaster getUserRoleId() {
		return userRoleId;
	}



	public void setUserRoleId(UserRoleMaster userRoleId) {
		this.userRoleId = userRoleId;
	}
	
	

}
