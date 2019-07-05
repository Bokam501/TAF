/**
 * 
 */
package com.hcl.atf.taf.model.json;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.hcl.atf.taf.model.ProductMaster;
import com.hcl.atf.taf.model.TestCaseProductVersionMapping;

/**
 * @author silambarasur
 * 
 */
public class JsonTestCaseProductVersionMapping {

	@JsonProperty
	private Integer testCaseId;
	@JsonProperty
	private Integer versionId;
	@JsonProperty
	private Integer productId;
	
	@JsonProperty
	private Integer isMapped;

	public JsonTestCaseProductVersionMapping() {

	}

	public JsonTestCaseProductVersionMapping(TestCaseProductVersionMapping testCaseProductVersionMapping) {
			this.testCaseId = testCaseProductVersionMapping.getTestCaseId();
		
			this.versionId = testCaseProductVersionMapping.getVersionId();
		
		if(testCaseProductVersionMapping.getProduct() != null) {
			this.productId = testCaseProductVersionMapping.getProduct().getProductId();
		}
		
		if (testCaseProductVersionMapping.getIsMapped()  != null && testCaseProductVersionMapping.getIsMapped() >0) {
			this.isMapped = testCaseProductVersionMapping.getIsMapped();
		} else {
			this.isMapped=0;
		}

	}
	
	@JsonIgnore
	public TestCaseProductVersionMapping getTestCaseProductVersionMapping() {
		TestCaseProductVersionMapping testCaseProductVersionMapping = new TestCaseProductVersionMapping();
		if(this.versionId != null) {
			testCaseProductVersionMapping.setVersionId(versionId);
		}
		if(this.testCaseId !=null) {
			testCaseProductVersionMapping.setTestCaseId(testCaseId);
		}
		if(this.productId != null) {
			ProductMaster product = new ProductMaster();
			product.setProductId(productId);
			testCaseProductVersionMapping.setProduct(product);
		}
		if(this.isMapped != null) {
			testCaseProductVersionMapping.setIsMapped(isMapped);
		} else {
			testCaseProductVersionMapping.setIsMapped(0);
		}
		return testCaseProductVersionMapping;
	}
	

	
	public Integer getProductId() {
		return productId;
	}

	
	public void setProductId(Integer productId) {
		this.productId = productId;
	}

	public Integer getTestCaseId() {
		return testCaseId;
	}

	public void setTestCaseId(Integer testCaseId) {
		this.testCaseId = testCaseId;
	}

	public Integer getVersionId() {
		return versionId;
	}

	public void setVersionId(Integer versionId) {
		this.versionId = versionId;
	}

	public Integer getIsMapped() {
		return isMapped;
	}

	public void setIsMapped(Integer isMapped) {
		this.isMapped = isMapped;
	}
	
}
