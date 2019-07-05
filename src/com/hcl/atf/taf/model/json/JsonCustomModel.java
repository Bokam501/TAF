package com.hcl.atf.taf.model.json;


import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class JsonCustomModel implements java.io.Serializable {

	@JsonProperty
	private List list;
	
	
	public JsonCustomModel() {
	}

	public List getList() {
		return list;
	}

	public void setList(List list) {
		this.list = list;
	}

	public JsonCustomModel(List list) {
		this.list = list;
	}

	
}
