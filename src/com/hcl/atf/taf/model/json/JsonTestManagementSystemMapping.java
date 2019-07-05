package com.hcl.atf.taf.model.json;



import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.hcl.atf.taf.model.ProductMaster;
import com.hcl.atf.taf.model.TestManagementSystem;
import com.hcl.atf.taf.model.TestManagementSystemMapping;


public class JsonTestManagementSystemMapping implements java.io.Serializable {

	@JsonProperty
	private Integer testManagementSystemMappingId;
	@JsonProperty
	private Integer testManagementSystemId;
	@JsonProperty
	private Integer productId;
	@JsonProperty
	private String mappingType;
	@JsonProperty
	private Integer mappedEntityIdInTAF;
	@JsonProperty
	private String mappingValue;
	@JsonProperty
	private String mappingValueDescription;
	@JsonProperty
	private String mappedEntityNameInTAFOptions;
	@JsonProperty
	private String mappedEntityNameInTAFId;
		
	public String getMappedEntityNameInTAFId() {
		return mappedEntityNameInTAFId;
	}


	public void setMappedEntityNameInTAFId(String mappedEntityNameInTAFId) {
		this.mappedEntityNameInTAFId = mappedEntityNameInTAFId;
	}


	public JsonTestManagementSystemMapping() {
	}


	public JsonTestManagementSystemMapping(TestManagementSystemMapping testManagementSystemMapping) {
		
		this.testManagementSystemMappingId = testManagementSystemMapping.getTestManagementSystemMappingId();
		this.testManagementSystemId = testManagementSystemMapping.getTestManagementSystem().getTestManagementSystemId();
		this.productId = testManagementSystemMapping.getProductMaster().getProductId();
		this.mappingType = testManagementSystemMapping.getMappingType();
		this.mappedEntityIdInTAF = testManagementSystemMapping.getMappedEntityIdInTAF();
		this.mappingValue = testManagementSystemMapping.getMappingValue();
		this.mappingValueDescription = testManagementSystemMapping.getMappingValueDescription();
	
	}


	public Integer getTestManagementSystemMappingId() {
		return testManagementSystemMappingId;
	}


	public void setTestManagementSystemMappingId(
			Integer testManagementSystemMappingId) {
		this.testManagementSystemMappingId = testManagementSystemMappingId;
	}


	public Integer getTestManagementSystemId() {
		return testManagementSystemId;
	}


	public void setTestManagementSystemId(Integer testManagementSystemId) {
		this.testManagementSystemId = testManagementSystemId;
	}


	public Integer getProductId() {
		return productId;
	}


	public void setProductId(Integer productId) {
		this.productId = productId;
	}


	public String getMappingType() {
		return mappingType;
	}


	public void setMappingType(String mappingType) {
		this.mappingType = mappingType;
	}


	public Integer getMappedEntityIdInTAF() {
		return mappedEntityIdInTAF;
	}


	public void setMappedEntityIdInTAF(Integer mappedEntityIdInTAF) {
		this.mappedEntityIdInTAF = mappedEntityIdInTAF;
	}

	

	public String getMappingValue() {
		return mappingValue;
	}


	public void setMappingValue(String mappingValue) {
		this.mappingValue = mappingValue;
	}


	public String getMappingValueDescription() {
		return mappingValueDescription;
	}


	public void setMappingValueDescription(String mappingValueDescription) {
		this.mappingValueDescription = mappingValueDescription;
	}


	public String getMappedEntityNameInTAFOptions() {
		return mappedEntityNameInTAFOptions;
	}
	public void setMappedEntityNameInTAFOptions(String mappedEntityNameInTAFOptions) {
		this.mappedEntityNameInTAFOptions = mappedEntityNameInTAFOptions;
	}


	@JsonIgnore
	public TestManagementSystemMapping getTestManagementSystemMapping(){
		TestManagementSystemMapping testManagementSystemMapping=new TestManagementSystemMapping();
		
		testManagementSystemMapping.setTestManagementSystemMappingId(testManagementSystemMappingId);
		
		TestManagementSystem testManagementSystem=new TestManagementSystem();
		if(testManagementSystemId!=null){
			testManagementSystem.setTestManagementSystemId(testManagementSystemId);
		}
		testManagementSystemMapping.setTestManagementSystem(testManagementSystem);
		
		ProductMaster productMaster=new ProductMaster();
		if(productId!=null){
			productMaster.setProductId(productId);
		}
		testManagementSystemMapping.setProductMaster(productMaster);
		testManagementSystemMapping.setMappingType(mappingType);
		testManagementSystemMapping.setMappedEntityIdInTAF(mappedEntityIdInTAF);
		testManagementSystemMapping.setMappingValue(mappingValue);
		testManagementSystemMapping.setMappingValueDescription(mappingValueDescription);
		
		return testManagementSystemMapping;		
	}
}
