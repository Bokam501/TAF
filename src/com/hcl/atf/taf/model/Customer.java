package com.hcl.atf.taf.model;

import static javax.persistence.GenerationType.IDENTITY;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;


@Entity
@Table(name = "customer")

public class Customer implements java.io.Serializable{
	
	private static final long serialVersionUID = 1L;

	
	private Integer customerId;
	@Column(name = "customerName")
	private String customerName;
	@Column(name = "description")
	private String description;
	@Column(name = "customerRefId")
	private String customerRefId;
	@Column(name = "createdDate")
	private Date createdDate;
	@Column(name = "modifiedDate")
	private Date modifiedDate;
	@Column(name = "status")
	private Integer status;
	@Column(name = "engagementStartDate")
	private Date engagementStartDate;
	@Column(name = "engagementEndDate")
	private Date engagementEndDate;
	private Set<UserList> customerUser = new HashSet<UserList>(0);
	@Column(name = "domain")
	private String domain;
	@Column(name = "ldapURL")
	private String ldapURL;
	@Column(name = "imageURI")
	private String imageURI;
	public Customer() {
	}
	
	@Id
	@Column(name = "customerId")
	@GeneratedValue(strategy = IDENTITY)
	public Integer getCustomerId() {
		return customerId;
	}


	public void setCustomerId(Integer customerId) {
		this.customerId = customerId;
	}


	public String getCustomerName() {
		return customerName;
	}


	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}


	public String getDescription() {
		return description;
	}


	public void setDescription(String description) {
		this.description = description;
	}


	public String getCustomerRefId() {
		return customerRefId;
	}


	public void setCustomerRefId(String customerRefId) {
		this.customerRefId = customerRefId;
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


	public Date getEngagementStartDate() {
		return engagementStartDate;
	}


	public void setEngagementStartDate(Date engagementStartDate) {
		this.engagementStartDate = engagementStartDate;
	}


	public Date getEngagementEndDate() {
		return engagementEndDate;
	}


	public void setEngagementEndDate(Date engagementEndDate) {
		this.engagementEndDate = engagementEndDate;
	}
	@Override
	public boolean equals(Object o) {
		Customer customer = (Customer) o;
		if (this.customerId == customer.getCustomerId()) {
			return true;
		}else{
			return false;
		}
	}

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "customer_users", joinColumns = { @JoinColumn(name = "customerId") }, inverseJoinColumns = { @JoinColumn(name = "userId") })
	public Set<UserList> getCustomerUser() {
		return customerUser;
	}

	public void setCustomerUser(Set<UserList> customerUser) {
		this.customerUser = customerUser;
	}

	public String getDomain() {
		return domain;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}

	public String getLdapURL() {
		return ldapURL;
	}

	public void setLdapURL(String ldapURL) {
		this.ldapURL = ldapURL;
	}

	public String getImageURI() {
		return imageURI;
	}

	public void setImageURI(String imageURI) {
		this.imageURI = imageURI;
	}
}
