package com.hcl.atf.taf.model;

import static javax.persistence.GenerationType.IDENTITY;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

@Entity
@Table(name = "product_type")
public class ProductType implements java.io.Serializable{

	private Integer productTypeId;
	private String typeName;
	private String description;
	private Set<TestToolMaster> testToolMaster = new HashSet<TestToolMaster>(0);
	public ProductType() {
	}


	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "productTypeId", unique = true, nullable = false)
	public Integer getProductTypeId() {
		return productTypeId;
	}
	public void setProductTypeId(Integer productTypeId) {
		this.productTypeId = productTypeId;
	}
	
	@Column(name = "typeName")
	public String getTypeName() {
		return typeName;
	}
	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}
	
	@Column(name = "description")
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	@ManyToMany(fetch=FetchType.LAZY, cascade={CascadeType.ALL})
	@JoinTable(name = "producttype_testengine", joinColumns = { @JoinColumn(name = "productTypeId", nullable = false, updatable = false) }, inverseJoinColumns = { @JoinColumn(name = "testToolId", nullable = false, updatable = false) })
	public Set<TestToolMaster> getTestToolMaster() {
		return testToolMaster;
	}

	public void setTestToolMaster(Set<TestToolMaster> testToolMaster) {
		this.testToolMaster = testToolMaster;
	}
	
}
