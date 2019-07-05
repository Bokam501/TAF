package com.hcl.atf.taf.model.json;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.hcl.atf.taf.model.ProductType;
import com.hcl.atf.taf.model.TestToolMaster;

public class JsonProductType {
	@JsonProperty
	private Integer productTypeId;
	@JsonProperty
	private String typeName;
	@JsonProperty
	private String description;
	@JsonProperty
	private List<String> testTool = new ArrayList<String>();
	
	public	JsonProductType(){}
	
	public JsonProductType(ProductType productType){
		productTypeId = productType.getProductTypeId();
		typeName=productType.getTypeName();
	
		
		if(null != productType.getTestToolMaster()){
			for(TestToolMaster tm : productType.getTestToolMaster()){
				testTool.add(tm.getTestToolName());
			}
		}
	}

	public Integer getProductTypeId() {
		return productTypeId;
	}

	public String getTypeName() {
		return typeName;
	}

	public String getDescription() {
		return description;
	}

	public List<String> getTestTool() {
		return testTool;
	}

	public void setProductTypeId(Integer productTypeId) {
		this.productTypeId = productTypeId;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setTestTool(List<String> testTool) {
		this.testTool = testTool;
	}

}
