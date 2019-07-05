package com.hcl.atf.taf.model;

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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "test_factory_core_resource")
public class TestFactoryCoreResource implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	
	private Integer testFactoryCoreResourceId;
	@Column(name = "remarks")
	private String remarks;
	@Column(name = "fromDate")
	private Date fromDate;
	@Column(name = "toDate")
	private Date toDate;
	@Column(name = "createdDate")
	private Date createdDate;
	@Column(name = "modifiedDate")
	private Date modifiedDate;
	@Column(name = "status")
	private Integer status;
	
	private TestFactory testFactory;
	
	private UserRoleMaster userRoleMaster;

	private UserList userList;
	
	@Id
	@Column(name = "testFactoryCoreResourceId")
	@GeneratedValue(strategy = IDENTITY)
	public Integer getTestFactoryCoreResourceId() {
		return testFactoryCoreResourceId;
	}

	public void setTestFactoryCoreResourceId(Integer testFactoryCoreResourceId) {
		this.testFactoryCoreResourceId = testFactoryCoreResourceId;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "fromDate")
	public Date getFromDate() {
		return fromDate;
	}

	public void setFromDate(Date fromDate) {
		this.fromDate = fromDate;
	}
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "toDate")
	public Date getToDate() {
		return toDate;
	}

	public void setToDate(Date toDate) {
		this.toDate = toDate;
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

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "testFactoryId")
	public TestFactory getTestFactory() {
		return testFactory;
	}

	public void setTestFactory(TestFactory testFactory) {
		this.testFactory = testFactory;
	}
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "userRoleId")
	public UserRoleMaster getUserRoleMaster() {
		return userRoleMaster;
	}

	public void setUserRoleMaster(UserRoleMaster userRoleMaster) {
		this.userRoleMaster = userRoleMaster;
	}
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "userId")
	public UserList getUserList() {
		return userList;
	}

	public void setUserList(UserList userList) {
		this.userList = userList;
	}

	
	
}
