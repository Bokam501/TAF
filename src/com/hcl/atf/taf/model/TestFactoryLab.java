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
@Table(name = "test_factory_lab")
public class TestFactoryLab implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	
	public TestFactoryLab(){
		
	}
	
	private Integer testFactoryLabId;
	@Column(name = "testFactoryLabName")
	private String testFactoryLabName;
	@Column(name = "displayName")
	private String displayName;
	@Column(name = "city")
	private String city;
	@Column(name = "state")
	private String state;
	@Column(name = "country")
	private String country;
	@Column(name = "status")
	private Integer status;
	@Column(name = "createdDate")
	private Date createdDate;
	@Column(name = "modifiedDate")
	private Date modifiedDate;
	
	
	@Id
	@Column(name = "testFactoryLabId")
	@GeneratedValue(strategy = IDENTITY)
	public Integer getTestFactoryLabId() {
		return testFactoryLabId;
	}
	
	public void setTestFactoryLabId(Integer testFactoryLabId) {
		this.testFactoryLabId = testFactoryLabId;
	}
	
	public String getTestFactoryLabName() {
		return testFactoryLabName;
	}
	
	public void setTestFactoryLabName(String testFactoryLabName) {
		this.testFactoryLabName = testFactoryLabName;
	}
	
	public String getDisplayName() {
		return displayName;
	}
	
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}
	
	public String getCity() {
		return city;
	}
	
	public void setCity(String city) {
		this.city = city;
	}
	
	public String getState() {
		return state;
	}
	
	public void setState(String state) {
		this.state = state;
	}
	
	public String getCountry() {
		return country;
	}
	
	public void setCountry(String country) {
		this.country = country;
	}
	
	public Integer getStatus() {
		return status;
	}
	
	public void setStatus(Integer status) {
		this.status = status;
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
	
	@Override
	public boolean equals(Object o) {
		TestFactoryLab testFactoryLab = (TestFactoryLab) o;
		if (this.testFactoryLabId == testFactoryLab.getTestFactoryLabId()) {
			return true;
		}else{
			return false;
		}
	}
	@Override
	public int hashCode(){
	    return (int) testFactoryLabId;
	  }
}
