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
@Table(name = "user_group")
public class UserGroup {

	private Integer userGroupId;
	private String userGroupName;
	private String description;
	private TestFactory testFactory;
	private ProductMaster product;
	private UserList createdBy;
	private Date createdDate;
	private UserList modifiedBy;
	private Date modifiedDate;
	
	public UserGroup() {
		
	}
	
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "userGroupId",unique = true, nullable = false)
	public Integer getUserGroupId() {
		return userGroupId;
	}

	public void setUserGroupId(Integer userGroupId) {
		this.userGroupId = userGroupId;
	}

	@Column(name = "userGroupName")
	public String getUserGroupName() {
		return userGroupName;
	}

	public void setUserGroupName(String userGroupName) {
		this.userGroupName = userGroupName;
	}

	@Column(name = "description")
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
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
