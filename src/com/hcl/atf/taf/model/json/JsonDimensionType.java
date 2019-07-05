package com.hcl.atf.taf.model.json;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.hcl.atf.taf.model.DimensionType;

public class JsonDimensionType {

	@JsonProperty
	private Integer dimensionTypeId;
	@JsonProperty
	private String name;
	
	public JsonDimensionType(){
		
	}
	
	public JsonDimensionType(DimensionType dimensionType) {
		this.dimensionTypeId = dimensionType.getDimensionTypeId();
		this.name = dimensionType.getName();
	}

	public Integer getDimensionTypeId() {
		return dimensionTypeId;
	}

	public void setDimensionTypeId(Integer dimensionTypeId) {
		this.dimensionTypeId = dimensionTypeId;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@JsonIgnore
	public DimensionType getDimensionType() {
		DimensionType dimensionType = new DimensionType();
		dimensionType.setDimensionTypeId(dimensionTypeId);
		dimensionType.setName(name);
		return dimensionType;
	}
	
}
