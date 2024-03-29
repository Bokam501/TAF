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
@Table(name = "test_factory_product_core_resource")
public class TestFactoryProductCoreResource implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	
private Integer testFactoryProductCoreResourceId;

private Date fromDate;

private Date toDate;

private Date createdDate;

private Date modifiedDate;
@Column(name = "status")
private Integer status;
@Column(name = "remarks")
private String remarks;

private UserList userList;

private ProductMaster productMaster;

private UserRoleMaster userRole;

@Id
@Column(name = "testFactoryProductCoreResourceId")
@GeneratedValue(strategy = IDENTITY)
public Integer getTestFactoryProductCoreResourceId() {
	return testFactoryProductCoreResourceId;
}

public void setTestFactoryProductCoreResourceId(
		Integer testFactoryProductCoreResourceId) {
	this.testFactoryProductCoreResourceId = testFactoryProductCoreResourceId;
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

public String getRemarks() {
	return remarks;
}

public void setRemarks(String remarks) {
	this.remarks = remarks;
}
@ManyToOne(fetch = FetchType.LAZY)
@JoinColumn(name = "userId")
public UserList getUserList() {
	return userList;
}

public void setUserList(UserList userList) {
	this.userList = userList;
}
@ManyToOne(fetch = FetchType.LAZY)
@JoinColumn(name = "productId")
public ProductMaster getProductMaster() {
	return productMaster;
}

public void setProductMaster(ProductMaster productMaster) {
	this.productMaster = productMaster;
}
@ManyToOne(fetch = FetchType.LAZY)
@JoinColumn(name = "userRoleId")
public UserRoleMaster getUserRole() {
	return userRole;
}

public void setUserRole(UserRoleMaster userRole) {
	this.userRole = userRole;
}

}
