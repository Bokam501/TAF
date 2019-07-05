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
@Table(name = "user_has_roles")
public class UserRoles {

	private Integer mutliRoleId;
	private UserList userList;
	private UserRoleMaster role;
	private Date createdDate;
	private Date fromDate;
	private Date toDate;

	private UserList createdBy;
	private Integer status;
	private String comments;

	public UserRoles() {
	}

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "mutliRoleId", unique = true, nullable = false)
	public Integer getMutliRoleId() {
		return mutliRoleId;
	}

	public void setMutliRoleId(Integer mutliRoleId) {
		this.mutliRoleId = mutliRoleId;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "userId", nullable = false)
	public UserList getUserList() {
		return userList;
	}

	public void setUserList(UserList userList) {
		this.userList = userList;
	}
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "roleId", nullable = false)
	public  UserRoleMaster getRole() {
		return role;
	}

	public  void setRole(UserRoleMaster role) {
		this.role = role;
	}
	@Column(name="createdDate")
	public  Date getCreatedDate() {
		return createdDate;
	}

	public  void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}
	@Column(name="fromDate")
	public  Date getFromDate() {
		return fromDate;
	}

	public  void setFromDate(Date fromDate) {
		this.fromDate = fromDate;
	}
	@Column(name="toDate")
	public  Date getToDate() {
		return toDate;
	}

	public  void setToDate(Date toDate) {
		this.toDate = toDate;
	}
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "createdBy") 
	public  UserList getCreatedBy() {
		return createdBy;
	}

	public  void setCreatedBy(UserList createdBy) {
		this.createdBy = createdBy;
	}

	@Column(name = "status")
	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}
	@Column(name = "comments")
	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	
}
