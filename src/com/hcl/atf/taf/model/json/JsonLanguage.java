package com.hcl.atf.taf.model.json;



import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.hcl.atf.taf.model.Languages;
public class JsonLanguage implements java.io.Serializable {
	private static final Log log = LogFactory
			.getLog(JsonLanguage.class);
	@JsonProperty
	private Integer id;	
	@JsonProperty
	private String name;
	@JsonProperty
	private String country;
	@JsonProperty
	private Integer status;
	
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public JsonLanguage() {
	}

	public JsonLanguage(Languages languages){
		this.id=languages.getId();
		this.name=languages.getName();
		this.country=languages.getCountry();
		if(languages.getStatus() != null){
			this.status = languages.getStatus();
		}
		
	}	
	
	@JsonIgnore
	public Languages getLanguages(){
		Languages languages = new Languages();
		languages.setId(id);
		languages.setName(name);
		languages.setCountry(country);
		
		
		if(this.status != null ){			
			languages.setStatus(this.status);			
		}else{
			languages.setStatus(0);	
		}
	
		
		return languages;
	}
	
	

	
}
