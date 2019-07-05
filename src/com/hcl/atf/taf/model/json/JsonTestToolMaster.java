package com.hcl.atf.taf.model.json;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.hcl.atf.taf.model.ExecutionMode;
import com.hcl.atf.taf.model.Languages;
import com.hcl.atf.taf.model.TestToolMaster;

public class JsonTestToolMaster {
	@JsonProperty
	private Integer testToolMasterId;
	@JsonProperty
	private String testToolMasterName;
	@JsonProperty
	private List<String> languages = new ArrayList<String>();
	@JsonProperty
	private List<String> modes  = new ArrayList<String>();
	@JsonProperty
	private Map<Integer,String> languagesIdNamePair = new HashMap<Integer,String>();

	public	JsonTestToolMaster(){}

	public JsonTestToolMaster(TestToolMaster testToolMaster){
		testToolMasterId = testToolMaster.getTestToolId();
		testToolMasterName=testToolMaster.getTestToolName();
		if(null != testToolMaster.getLanguages()){
			for(Languages l : testToolMaster.getLanguages()){
				languages.add(l.getName());
				languagesIdNamePair.put(l.getId(), l.getName());
			}
		}

		if(null != testToolMaster.getExecutionModes()){
			for(ExecutionMode e : testToolMaster.getExecutionModes()){
				modes.add(e.getName());
			}
		}
	}
	public Map<Integer, String> getLanguagesIdNamePair() {
		return languagesIdNamePair;
	}

	public void setLanguagesIdNamePair(Map<Integer, String> languagesIdNamePair) {
		this.languagesIdNamePair = languagesIdNamePair;
	}

	public Integer getTestToolMasterId() {
		return testToolMasterId;
	}

	public void setTestToolMasterId(Integer testToolMasterId) {
		this.testToolMasterId = testToolMasterId;
	}

	public String getTestToolMasterName() {
		return testToolMasterName;
	}

	public void setTestToolMasterName(String testToolMasterName) {
		this.testToolMasterName = testToolMasterName;
	}

	public List<String> getLanguages() {
		return languages;
	}

	public void setLanguages(List<String> languages) {
		this.languages = languages;
	}

	public List<String> getModes() {
		return modes;
	}

	public void setModes(List<String> modes) {
		this.modes = modes;
	}
}
