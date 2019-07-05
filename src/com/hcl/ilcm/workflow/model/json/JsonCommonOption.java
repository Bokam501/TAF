/**
 * 
 */
package com.hcl.ilcm.workflow.model.json;

import org.codehaus.jackson.annotate.JsonProperty;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * @author silambarasur
 *
 */
public class JsonCommonOption {

	@JsonProperty
	private Integer Id;
	@JsonProperty
	private String value;
	
	public JsonCommonOption() {
		
	}
	
	public JsonCommonOption(Integer Id,String value) {
		this.Id=Id;
		this.value=value;
	}
	
	
	public Integer getId() {
		return Id;
	}

	public void setId(Integer id) {
		Id = id;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	@JsonIgnore
	public JsonCommonOption getIdValue() {
		JsonCommonOption jsonIdValue = new JsonCommonOption();
		jsonIdValue.setId(Id);
		jsonIdValue.setValue(value);
		return jsonIdValue;
	}
	
}
