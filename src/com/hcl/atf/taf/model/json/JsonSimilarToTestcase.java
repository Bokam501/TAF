/**
 * 
 */
package com.hcl.atf.taf.model.json;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import com.hcl.atf.taf.model.ProductMaster;
import com.hcl.atf.taf.model.SimilartoTestcaseMapping;

/**
 * @author silambarasur
 *
 */
public class JsonSimilarToTestcase {
	
	@JsonProperty
	private Integer id;
	@JsonProperty
	private Integer testCaseId;
	@JsonProperty
	private Integer similarToTestCaseId;
	@JsonProperty
	private Integer productId;
	@JsonProperty
	private String productName;
	@JsonProperty
	private Integer isMapped;
	
	public JsonSimilarToTestcase() {
		
	}
	
	public JsonSimilarToTestcase(SimilartoTestcaseMapping similartoTestcaseMapping) {
		this.id = similartoTestcaseMapping.getId();
		
		this.testCaseId = similartoTestcaseMapping.getTestCaseId();
		this.similarToTestCaseId = similartoTestcaseMapping.getSimilarToTestCaseId();
		if(similartoTestcaseMapping.getProduct() != null) {
			this.productId = similartoTestcaseMapping.getProduct().getProductId();
			this.productName = similartoTestcaseMapping.getProduct().getProductName();
		}
		this.isMapped = similartoTestcaseMapping.getIsMapped();
	}
	
	
	@JsonIgnore
	public SimilartoTestcaseMapping getSimilartoTestcaseMapping() {
		SimilartoTestcaseMapping similartoTestcaseMapping= new SimilartoTestcaseMapping();
		similartoTestcaseMapping.setId(this.id);
		similartoTestcaseMapping.setTestCaseId(this.testCaseId);
		similartoTestcaseMapping.setSimilarToTestCaseId(this.similarToTestCaseId);
		if(this.productId !=null) {
			ProductMaster product= new ProductMaster();
			product.setProductId(this.productId);
			similartoTestcaseMapping.setProduct(product);
		}
		return similartoTestcaseMapping;
	}
	public Integer getId() {
		return id;
	}
	public Integer getTestCaseId() {
		return testCaseId;
	}
	public Integer getSimilarToTestCaseId() {
		return similarToTestCaseId;
	}
	public Integer getProductId() {
		return productId;
	}
	public String getProductName() {
		return productName;
	}
	public Integer getIsMapped() {
		return isMapped;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public void setTestCaseId(Integer testCaseId) {
		this.testCaseId = testCaseId;
	}
	public void setSimilarToTestCaseId(Integer similarToTestCaseId) {
		this.similarToTestCaseId = similarToTestCaseId;
	}
	public void setProductId(Integer productId) {
		this.productId = productId;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public void setIsMapped(Integer isMapped) {
		this.isMapped = isMapped;
	} 
	

}
