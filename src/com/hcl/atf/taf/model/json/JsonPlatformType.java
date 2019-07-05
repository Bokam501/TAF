package com.hcl.atf.taf.model.json;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.hcl.atf.taf.model.PlatformType;

public class JsonPlatformType {
	@JsonProperty
	private Integer platformId;
	@JsonProperty
	private String name;
	@JsonProperty
	private String version;
	
	public JsonPlatformType(){
		
	}
	public JsonPlatformType(PlatformType platformType){
		this.platformId=platformType.getPlatformId();
		this.name=platformType.getName();
		this.version=platformType.getVersion();
	}
	public PlatformType getPlatformType(){
		PlatformType platformType=new PlatformType();
		platformType.setPlatformId(platformId);
		platformType.setName(name);
		platformType.setVersion(version);
		return platformType;
	}
	public Integer getPlatformId() {
		return platformId;
	}
	public void setPlatformId(Integer platformId) {
		this.platformId = platformId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	
}
