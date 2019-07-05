package com.hcl.atf.taf.model;

import static javax.persistence.GenerationType.IDENTITY;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "test_factory")
public class TestFactory implements java.io.Serializable {
private static final long serialVersionUID = 1L;
	
	public TestFactory(){
		
	}

	private Integer testFactoryId;
	@Column(name = "testFactoryName")
	private String testFactoryName;
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

	private Date createdDate;
	
	private Date modifiedDate;
	
	@Column(name = "description")
	private String description;
	
	private TestFactoryLab testFactoryLab;
	
	private EngagementTypeMaster engagementTypeMaster;
	
	private Set<TestfactoryResourcePool> TestfactoryResourcePoolList = new HashSet<TestfactoryResourcePool>(0);
	private Set<ProductMaster> productMaster = new HashSet<ProductMaster>(0);
	@ManyToMany(fetch = FetchType.LAZY, mappedBy = "testFactoryList",cascade=CascadeType.ALL)
	public Set<TestfactoryResourcePool> getTestfactoryResourcePoolList() {
		return TestfactoryResourcePoolList;
	}

	public void setTestfactoryResourcePoolList(
			Set<TestfactoryResourcePool> testfactoryResourcePoolList) {
		TestfactoryResourcePoolList = testfactoryResourcePoolList;
	}

	@Id
	@Column(name = "testFactoryId")
	@GeneratedValue(strategy = IDENTITY)
	public Integer getTestFactoryId() {
		return testFactoryId;
	}

	public void setTestFactoryId(Integer testFactoryId) {
		this.testFactoryId = testFactoryId;
	}

	public String getTestFactoryName() {
		return testFactoryName;
	}

	public void setTestFactoryName(String testFactoryName) {
		this.testFactoryName = testFactoryName;
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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "testFactoryLabId") 
	public TestFactoryLab getTestFactoryLab() {
		return testFactoryLab;
	}

	public void setTestFactoryLab(TestFactoryLab testFactoryLab) {
		this.testFactoryLab = testFactoryLab;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "engagementTypeId", nullable = false)
	public EngagementTypeMaster getEngagementTypeMaster() {
		return engagementTypeMaster;
	}

	public void setEngagementTypeMaster(EngagementTypeMaster engagementTypeMaster) {
		this.engagementTypeMaster = engagementTypeMaster;
	}

	@Override
	public boolean equals(Object o) {
		TestFactory testFactory = (TestFactory) o;
		if (this.testFactoryId == testFactory.getTestFactoryId()) {
			return true;
		}else{
			return false;
		}
	}
	
	@Override
	public int hashCode(){
	    return (int) testFactoryId;
	  }
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "testFactory",cascade=CascadeType.ALL)
	public Set<ProductMaster> getProductMaster() {
		return productMaster;
	}

	public void setProductMaster(Set<ProductMaster> productMaster) {
		this.productMaster = productMaster;
	}

	
}
