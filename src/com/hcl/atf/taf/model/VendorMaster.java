package com.hcl.atf.taf.model;

import static javax.persistence.GenerationType.IDENTITY;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "vendor_master")
public class VendorMaster implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	
	private Integer vendorId;
	@Column(name = "registeredCompanyName")
	private String registeredCompanyName;
	@Column(name = "domain")
	private String domain;
	@Column(name = "address")
	private String address;
	@Column(name = "city")
	private String city;
	@Column(name = "contactEmailId")
	private String contactEmailId;
	@Column(name = "contactNumber")
	private String contactNumber;
	@Column(name = "createdDate")
	private Date createdDate;
	@Column(name = "modifiedDate")
	private Date modifiedDate;
	@Column(name = "spocName")
	private String spocName;
	@Column(name = "status")
	private Integer status;
	
	
	

	@Id
	@Column(name = "vendorId")
	@GeneratedValue(strategy = IDENTITY)
	public Integer getVendorId() {
		return vendorId;
	}
	
	public void setVendorId(Integer vendorId) {
		this.vendorId = vendorId;
	}
	
	public String getRegisteredCompanyName() {
		return registeredCompanyName;
	}
	
	public void setRegisteredCompanyName(String registeredCompanyName) {
		this.registeredCompanyName = registeredCompanyName;
	}
	
	public String getDomain() {
		return domain;
	}
	
	public void setDomain(String domain) {
		this.domain = domain;
	}
	
	public String getAddress() {
		return address;
	}
	
	public void setAddress(String address) {
		this.address = address;
	}
	
	public String getCity() {
		return city;
	}
	
	public void setCity(String city) {
		this.city = city;
	}
	
	public String getContactEmailId() {
		return contactEmailId;
	}
	
	public void setContactEmailId(String contactEmailId) {
		this.contactEmailId = contactEmailId;
	}
	
	public String getContactNumber() {
		return contactNumber;
	}
	
	public void setContactNumber(String contactNumber) {
		this.contactNumber = contactNumber;
	}

	@Temporal(TemporalType.TIMESTAMP)
	public Date getCreatedDate() {
		return createdDate;
	}

	
	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	@Temporal(TemporalType.TIMESTAMP)
	public Date getModifiedDate() {
		return modifiedDate;
	}

	public void setModifiedDate(Date modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

	
	public String getSpocName() {
		return spocName;
	}

	public void setSpocName(String spocName) {
		this.spocName = spocName;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}
	
}
