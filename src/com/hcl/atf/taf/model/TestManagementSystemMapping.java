package com.hcl.atf.taf.model;



import static javax.persistence.GenerationType.IDENTITY;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * TestManagementSystemMapping - Mapping values for TAF and Test management systems
 * 
 */
@Entity
@Table(name = "test_management_system_mapping")
public class TestManagementSystemMapping implements java.io.Serializable {

	private Integer testManagementSystemMappingId;
	private String mappingType;
	private Integer mappedEntityIdInTAF;
	private String mappingValue;
	private String mappingValueDescription;
	private ProductMaster productMaster;
	private TestManagementSystem testManagementSystem;
	
	public TestManagementSystemMapping(){
		
	}
	
	public TestManagementSystemMapping(String mappingType, Integer mappedEntityIdInTAF, String mappingValue, String mappingValueDescription,
				ProductMaster productMaster, TestManagementSystem testManagementSystem ){
		this.mappingType = mappingType;
		this.mappedEntityIdInTAF = mappedEntityIdInTAF;
		this.mappingValue = mappingValue;
		this.mappingValueDescription = mappingValueDescription;
		this.productMaster = productMaster;
		this.testManagementSystem = testManagementSystem;
	}
	
	@Id	
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "testManagementSystemMappingId", unique = true, nullable = false)	
	public Integer getTestManagementSystemMappingId() {
		return testManagementSystemMappingId;
	}
	
	public void setTestManagementSystemMappingId(Integer testManagementSystemMappingId) {
		this.testManagementSystemMappingId = testManagementSystemMappingId;
	}
	
	@Column(name = "mappingType", length= 100)
	public String getMappingType() {
		return mappingType;
	}
	
	public void setMappingType(String mappingType) {
		this.mappingType = mappingType;
	}
	
	@Column(name = "mappedEntityIdInTAF")
	public Integer getMappedEntityIdInTAF() {
		return mappedEntityIdInTAF;
	}
	
	
	public void setMappedEntityIdInTAF(Integer mappedEntityIdInTAF) {
		this.mappedEntityIdInTAF = mappedEntityIdInTAF;
	}
	
	@Column(name = "mappingValue", length = 250)
	public String getMappingValue() {
		return mappingValue;
	}
	
	public void setMappingValue(String mappingValue) {
		this.mappingValue = mappingValue;
	}
	
	@Column(name = "mappingValueDescription", length = 2000)
	public String getMappingValueDescription() {
		return mappingValueDescription;
	}
	
	public void setMappingValueDescription(String mappingValueDescription) {
		this.mappingValueDescription = mappingValueDescription;
	}
	
	@ManyToOne (fetch = FetchType.LAZY)
	@JoinColumn (name = "productId")
	public ProductMaster getProductMaster() {
		return productMaster;
	}
	
	public void setProductMaster(ProductMaster productMaster) {
		this.productMaster = productMaster;
	}
	
	
	@ManyToOne (fetch = FetchType.LAZY )
	@JoinColumn (name = "testManagementSystemId")
	public TestManagementSystem getTestManagementSystem() {
		return testManagementSystem;
	}
	
	public void setTestManagementSystem(TestManagementSystem testManagementSystem) {
		this.testManagementSystem = testManagementSystem;
	}
	
}
