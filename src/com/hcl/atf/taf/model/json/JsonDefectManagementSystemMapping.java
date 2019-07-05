package com.hcl.atf.taf.model.json;



import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.hcl.atf.taf.model.DefectManagementSystem;
import com.hcl.atf.taf.model.DefectManagementSystemMapping;
import com.hcl.atf.taf.model.ProductMaster;


public class JsonDefectManagementSystemMapping implements java.io.Serializable {

	@JsonProperty
	private Integer defecManagementSystemMappingId;
	@JsonProperty
	private Integer defectManagementSystemId;
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


	public JsonDefectManagementSystemMapping() {
	}


	public JsonDefectManagementSystemMapping(DefectManagementSystemMapping defectManagementSystemMapping) {
		
		this.defecManagementSystemMappingId = defectManagementSystemMapping.getDefectManagementSystemMappingId();
		this.defectManagementSystemId = defectManagementSystemMapping.getDefectManagementSystem().getDefectManagementSystemId();
		this.productId = defectManagementSystemMapping.getProductMaster().getProductId();
		this.mappingType = defectManagementSystemMapping.getMappingType();
		this.mappedEntityIdInTAF = defectManagementSystemMapping.getMappedEntityIdInTAF();
		this.mappingValue = defectManagementSystemMapping.getMappingValue();
		this.mappingValueDescription = defectManagementSystemMapping.getMappingValueDescription();
	
	}


	public Integer getDefecManagementSystemMappingId() {
		return defecManagementSystemMappingId;
	}


	public void setDefecManagementSystemMappingId(
			Integer defecManagementSystemMappingId) {
		this.defecManagementSystemMappingId = defecManagementSystemMappingId;
	}


	public Integer getDefectManagementSystemId() {
		return defectManagementSystemId;
	}


	public void setDefectManagementSystemId(Integer defectManagementSystemId) {
		this.defectManagementSystemId = defectManagementSystemId;
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
	public DefectManagementSystemMapping getDefectManagementSystemMapping(){
		DefectManagementSystemMapping defectManagementSystemMapping=new DefectManagementSystemMapping();
		
		defectManagementSystemMapping.setDefectManagementSystemMappingId(defecManagementSystemMappingId);
		
		DefectManagementSystem defectManagementSystem=new DefectManagementSystem();
		if(defectManagementSystemId!=null){
			defectManagementSystem.setDefectManagementSystemId(defectManagementSystemId);
		}
		defectManagementSystemMapping.setDefectManagementSystem(defectManagementSystem);
		
		ProductMaster productMaster=new ProductMaster();
		if(productId!=null){
			productMaster.setProductId(productId);
		}
		defectManagementSystemMapping.setProductMaster(productMaster);
		
		
		defectManagementSystemMapping.setMappingType(mappingType);
		defectManagementSystemMapping.setMappedEntityIdInTAF(mappedEntityIdInTAF);
		defectManagementSystemMapping.setMappingValue(mappingValue);
		defectManagementSystemMapping.setMappingValueDescription(mappingValueDescription);
		
		return defectManagementSystemMapping;		
	}
}
