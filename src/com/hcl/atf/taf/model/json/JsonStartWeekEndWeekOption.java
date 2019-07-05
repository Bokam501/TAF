package com.hcl.atf.taf.model.json;

import com.fasterxml.jackson.annotation.JsonProperty;

public class JsonStartWeekEndWeekOption implements java.io.Serializable {

	@JsonProperty
	public String option;
	@JsonProperty
	public String value;

	public JsonStartWeekEndWeekOption(String option,String value) {
		this.option = option;
		this.value=value;
	}
	
	public String getOption() {
		return option;
	}

	public void setOption(String option) {
		this.option = option;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	
}
