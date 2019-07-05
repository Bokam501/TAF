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
@Table(name = "product_team_resources")
public class ProductTeamResources implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	
private Integer productTeamResourceId;

private Date fromDate;

private Date toDate;

private Date createdDate;

private Date modifiedDate;

private Integer status;

private String remarks;

private UserList userList;

private ProductMaster productMaster;

private UserRoleMaster productSpecificUserRole;

private DimensionMaster dimensionMaster;

private Float percentageofallocation;

@Id
@Column(name = "productTeamResourceId")
@GeneratedValue(strategy = IDENTITY)
public Integer getProductTeamResourceId() {
	return productTeamResourceId;
}

public void setProductTeamResourceId(
		Integer productTeamResourceId) {
	this.productTeamResourceId = productTeamResourceId;
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

@Column(name = "status")
public Integer getStatus() {
	return status;
}

public void setStatus(Integer status) {
	this.status = status;
}

@Column(name = "remarks")
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
public UserRoleMaster getProductSpecificUserRole() {
	return productSpecificUserRole;
}

public void setProductSpecificUserRole(UserRoleMaster productSpecificUserRole) {
	this.productSpecificUserRole = productSpecificUserRole;
}

@ManyToOne(fetch = FetchType.LAZY)
@JoinColumn(name = "competencyId")
public DimensionMaster getDimensionMaster() {
	return dimensionMaster;
}

public void setDimensionMaster(DimensionMaster dimensionMaster) {
	this.dimensionMaster = dimensionMaster;
}

public Float getPercentageofallocation() {
	return percentageofallocation;
}
@Column(name = "percentageofallocation")
public void setPercentageofallocation(Float percentageofallocation) {
	this.percentageofallocation = percentageofallocation;
}



}

