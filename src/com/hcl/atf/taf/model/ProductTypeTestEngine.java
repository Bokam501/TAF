package com.hcl.atf.taf.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.hcl.atf.taf.model.TestToolMaster;

@Entity
@Table(name = "producttype_testengine")
public class ProductTypeTestEngine implements java.io.Serializable{

	private Integer productTypeTestEngineId;
	private ProductType productType;
	private TestToolMaster testTool;
	
	public ProductTypeTestEngine() {
	}


	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "productTypeTestEngineId", unique = true, nullable = false)
	public Integer getProductTypeId() {
		return productTypeTestEngineId;
	}
	public void setProductTypeId(Integer productTypeTestEngineId) {
		this.productTypeTestEngineId = productTypeTestEngineId;
	}
	
	@ManyToOne
	@JoinColumn(name="productTypeId")
	public ProductType productType() {
		return productType;
	}

	public void setLanguage(ProductType productType) {
		this.productType = productType;
	}
	
	@ManyToOne
	@JoinColumn(name="testToolId")
	public TestToolMaster getTestTool() {
		return testTool;
	}

	public void setTestTool(TestToolMaster testTool) {
		this.testTool = testTool;
	}
	
}