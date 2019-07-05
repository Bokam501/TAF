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
@Table(name = "entity_user_group")
public class EntityUserGroup {

	private Integer entityUserGroupId;
	private EntityMaster entityTypeId;
	private Integer entityInstanceId;
	private UserList user;
	private UserList mappedBy;
	private Date mappedDate;
	private ProductMaster product;
	
	public EntityUserGroup() {
		
	}

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "entityUserGroupId",unique = true, nullable = false)
	public Integer getEntityUserGroupId() {
		return entityUserGroupId;
	}

	public void setEntityUserGroupId(Integer entityUserGroupId) {
		this.entityUserGroupId = entityUserGroupId;
	}

	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "entityTypeId")
	public EntityMaster getEntityTypeId() {
		return entityTypeId;
	}

	public void setEntityTypeId(EntityMaster entityTypeId) {
		this.entityTypeId = entityTypeId;
	}

	public Integer getEntityInstanceId() {
		return entityInstanceId;
	}

	public void setEntityInstanceId(Integer entityInstanceId) {
		this.entityInstanceId = entityInstanceId;
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
	@JoinColumn(name = "mappedBy")
	public UserList getMappedBy() {
		return mappedBy;
	}

	public void setMappedBy(UserList mappedBy) {
		this.mappedBy = mappedBy;
	}

	public Date getMappedDate() {
		return mappedDate;
	}

	public void setMappedDate(Date mappedDate) {
		this.mappedDate = mappedDate;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "productId")
	public ProductMaster getProduct() {
		return product;
	}

	public void setProduct(ProductMaster product) {
		this.product = product;
	}
	
	
	
	
	

}
