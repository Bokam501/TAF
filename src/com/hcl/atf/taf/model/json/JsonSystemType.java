package com.hcl.atf.taf.model.json;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.hcl.atf.taf.model.SystemType;

public class JsonSystemType {

	private static final long serialVersionUID = 1L;
	@JsonProperty
	private Integer systemTypeId;
	@JsonProperty
	private String name;
	@JsonProperty
	private String description;

public JsonSystemType(SystemType systemType) {
	this.systemTypeId=systemType.getSystemTypeId();
	this.name=systemType.getName();
	this.description=systemType.getDescription();
}
@JsonIgnore
public SystemType getSystemType() {
	SystemType systemType = new SystemType();
	return systemType;	
}
public Integer getSystemTypeId() {
	return systemTypeId;
}
public void setSystemTypeId(Integer systemTypeId) {
	this.systemTypeId = systemTypeId;
}
public String getName() {
	return name;
}
public void setName(String name) {
	this.name = name;
}
public String getDescription() {
	return description;
}
public void setDescription(String description) {
	this.description = description;
}


}
