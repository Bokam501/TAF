package com.hcl.atf.taf.model;

import static javax.persistence.GenerationType.IDENTITY;

import java.util.Date;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;


@Entity
@Table(name = "device_lab")
public class DeviceLab implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	
	private Integer device_lab_Id;
	private Date createdDate;
	private Date modifiedDate;
	private String device_lab_name;
	private String device_lab_description;
	private Integer status;
	private TestFactoryLab testFactoryLab;
	private TestFactory testFactory;
	private ProductMaster productMaster;
	
	public DeviceLab() {
	}
	
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "device_lab_Id", unique = true, nullable = false)
	public Integer getDevice_lab_Id() {
		return device_lab_Id;
	}

	public void setDevice_lab_Id(Integer device_lab_Id) {
		this.device_lab_Id = device_lab_Id;
	}
	@Column(name = "createdDate", length = 45)
	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}
	@Column(name = "modifiedDate", length = 45)
	public Date getModifiedDate() {
		return modifiedDate;
	}

	public void setModifiedDate(Date modifiedDate) {
		this.modifiedDate = modifiedDate;
	}
	@Column(name = "device_lab_name", length = 45)
	public String getDevice_lab_name() {
		return device_lab_name;
	}

	public void setDevice_lab_name(String device_lab_name) {
		this.device_lab_name = device_lab_name;
	}
	@Column(name = "device_lab_description", length = 45)
	public String getDevice_lab_description() {
		return device_lab_description;
	}

	public void setDevice_lab_description(String device_lab_description) {
		this.device_lab_description = device_lab_description;
	}
	@Column(name = "status", length = 45)
	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "testfactoryLabId", nullable = false)
	public TestFactoryLab getTestFactoryLab() {
		return testFactoryLab;
	}

	public void setTestFactoryLab(TestFactoryLab testFactoryLab) {
		this.testFactoryLab = testFactoryLab;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "testfactoryId", nullable = false)
	public TestFactory getTestFactory() {
		return testFactory;
	}

	public void setTestFactory(TestFactory testFactory) {
		this.testFactory = testFactory;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "productId", nullable = false)
	public ProductMaster getProductMaster() {
		return productMaster;
	}

	public void setProductMaster(ProductMaster productMaster) {
		this.productMaster = productMaster;
	}

	
}

