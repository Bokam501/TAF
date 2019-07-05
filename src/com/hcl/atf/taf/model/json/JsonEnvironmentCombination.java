package com.hcl.atf.taf.model.json;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.hcl.atf.taf.model.EnvironmentCombination;
import com.hcl.atf.taf.model.ProductMaster;
import com.hcl.atf.taf.model.ProductType;

public class JsonEnvironmentCombination {
	@JsonProperty
	private Integer envionmentCombinationId;
	@JsonProperty
	private String environmentCombinationName;
	@JsonProperty
	private Integer status;
	@JsonProperty
	private Integer envionmentCombinationStatus;
	@JsonProperty
	private Integer productType;
	
	public JsonEnvironmentCombination(){
		
	}
public JsonEnvironmentCombination(EnvironmentCombination environmentCombination) {
		
		this.envionmentCombinationId = environmentCombination.getEnvironment_combination_id();
		this.environmentCombinationName = environmentCombination.getEnvironmentCombinationName();
		if(environmentCombination.getProductMaster() != null && environmentCombination.getProductMaster().getProductType() != null){
			this.productType=environmentCombination.getProductMaster().getProductType().getProductTypeId();
		}
		this.envionmentCombinationStatus=environmentCombination.getEnvionmentCombinationStatus();
}
@JsonIgnore
public EnvironmentCombination getEnvironment() {
	EnvironmentCombination environmentCombination = new EnvironmentCombination();
	if (envionmentCombinationId != null) {
		environmentCombination.setEnvironment_combination_id(envionmentCombinationId);
	}
	environmentCombination.setEnvironmentCombinationName(environmentCombinationName);
	
	if(this.envionmentCombinationStatus != null ){			
		environmentCombination.setEnvionmentCombinationStatus(envionmentCombinationStatus);			
	}else{
		environmentCombination.setEnvionmentCombinationStatus(0);	
	}
	
	if(this.productType != null){
		ProductMaster prodMaster = new ProductMaster();
		ProductType productType = new ProductType();
		productType.setProductTypeId(this.productType);
		prodMaster.setProductType(productType);
		environmentCombination.setProductMaster(prodMaster);
	}
	return environmentCombination;
}
public Integer getEnvionmentCombinationId() {
	return envionmentCombinationId;
}
public void setEnvionmentCombinationId(Integer envionmentCombinationId) {
	this.envionmentCombinationId = envionmentCombinationId;
}
public String getEnvironmentCombinationName() {
	return environmentCombinationName;
}
public void setEnvironmentCombinationName(String environmentCombinationName) {
	this.environmentCombinationName = environmentCombinationName;
}
public Integer getStatus() {
	return status;
}
public void setStatus(Integer status) {
	this.status = status;
}
@Override
public boolean equals(Object o) {
	JsonEnvironmentCombination jsonEnvironmentCombination = (JsonEnvironmentCombination) o;
	
	if (this.envionmentCombinationId== jsonEnvironmentCombination.getEnvionmentCombinationId()) {
		return true;
	}else{

		return false;
	}
}

@Override
public int hashCode(){
    return (int) this.envionmentCombinationId;
  }
public Integer getProductType() {
	return productType;
}
public void setProductType(Integer productType) {
	this.productType = productType;
}
public Integer getEnvionmentCombinationStatus() {
	return envionmentCombinationStatus;
}
public void setEnvionmentCombinationStatus(Integer envionmentCombinationStatus) {
	this.envionmentCombinationStatus = envionmentCombinationStatus;
}


}
