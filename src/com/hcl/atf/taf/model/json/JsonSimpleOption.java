package com.hcl.atf.taf.model.json;

import com.fasterxml.jackson.annotation.JsonProperty;

public class JsonSimpleOption implements java.io.Serializable {

	@JsonProperty
	public String option;

	public JsonSimpleOption(String option) {
		this.option = option;
	}
	
	public String getOption() {
		return option;
	}

	public void setOption(String option) {
		this.option = option;
	}

}
