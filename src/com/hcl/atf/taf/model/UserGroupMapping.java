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

@Entity
@Table(name = "user_group_mapping")
public class UserGroupMapping {

	private Integer mappingId;
	private UserGroup userGroup;
	private UserList user;
	private ProductMaster product;
	private UserList createdBy;
	private Date createdDate;
	private UserList modifiedBy;
	private Date modifiedDate;
	
	public UserGroupMapping() {
		
	}
	
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "mappingId",unique = true, nullable = false)
	public Integer getMappingId() {
		return mappingId;
	}
	public void setMappingId(Integer mappingId) {
		this.mappingId = mappingId;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "userGroupId") 
	public UserGroup getUserGroup() {
		return userGroup;
	}
	public void setUserGroup(UserGroup userGroup) {
		this.userGroup = userGroup;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "userId") 
	public UserList getUser() {
		return user;
	}
	public void setUser(UserList user) {
		this.user = user;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "productId") 
	public ProductMaster getProduct() {
		return product;
	}
	public void setProduct(ProductMaster product) {
		this.product = product;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "createdBy") 
	public UserList getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(UserList createdBy) {
		this.createdBy = createdBy;
	}

	@Column(name = "createdDate")
	public Date getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "modifiedBy") 
	public UserList getModifiedBy() {
		return modifiedBy;
	}
	public void setModifiedBy(UserList modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	@Column(name = "modifiedDate")
	public Date getModifiedDate() {
		return modifiedDate;
	}
	public void setModifiedDate(Date modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

}
