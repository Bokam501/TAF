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
@Table(name = "user_customer_account")
public class UserCustomerAccount {

	private static final long serialVersionUID = 1L;
	
	private Integer userCustomerAccountId;
	@Column(name = "userCustomerName")
	private String userCustomerName;
	@Column(name = "userCustomerCode")
	private String userCustomerCode;
	@Column(name = "userCustomerEmailId")
	private String userCustomerEmailId;
	
	private Date validFromDate;
	
	private Date validTillDate;
	private UserList userList;
	private Customer customer;
	
	@Id
	@Column(name = "userCustomerAccountId")
	@GeneratedValue(strategy = IDENTITY)
	public Integer getUserCustomerAccountId() {
		return userCustomerAccountId;
	}
	public void setUserCustomerAccountId(Integer userCustomerAccountId) {
		this.userCustomerAccountId = userCustomerAccountId;
	}
	public String getUserCustomerName() {
		return userCustomerName;
	}
	public void setUserCustomerName(String userCustomerName) {
		this.userCustomerName = userCustomerName;
	}
	public String getUserCustomerCode() {
		return userCustomerCode;
	}
	public void setUserCustomerCode(String userCustomerCode) {
		this.userCustomerCode = userCustomerCode;
	}
	public String getUserCustomerEmailId() {
		return userCustomerEmailId;
	}
	public void setUserCustomerEmailId(String userCustomerEmailId) {
		this.userCustomerEmailId = userCustomerEmailId;
	}
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "validFromDate")
	public Date getValidFromDate() {
		return validFromDate;
	}
	public void setValidFromDate(Date validFromDate) {
		this.validFromDate = validFromDate;
	}
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "validTillDate")
	public Date getValidTillDate() {
		return validTillDate;
	}
	public void setValidTillDate(Date validTillDate) {
		this.validTillDate = validTillDate;
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
	@JoinColumn(name = "customerId")
	public Customer getCustomer() {
		return customer;
	}
	public void setCustomer(Customer customer) {
		this.customer = customer;
	}
	
	
	
}
