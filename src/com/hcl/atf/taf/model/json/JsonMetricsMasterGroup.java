package com.hcl.atf.taf.model.json;

import java.util.List;

import org.apache.poi.hssf.record.formula.functions.T;

import com.fasterxml.jackson.annotation.JsonProperty;



public class JsonMetricsMasterGroup implements java.io.Serializable {
	
	private static final long serialVersionUID = 2689432816943434243L;

	private String groupName;
    private List<T> groupValues;
    private String actualValue;
    private String indicator;
    private String groupType;
	

	public JsonMetricsMasterGroup() {
	}

	public JsonMetricsMasterGroup(String groupName, List groupValues, String actualValue, String indicator,String groupType) {
		this.groupName = groupName;
		this.groupValues = groupValues; 
		this.actualValue = actualValue;
		this.indicator = indicator;
		this.groupType=groupType;
	}

	public String getGroupName() {
		return groupName;
	}

	@JsonProperty("GroupName")
	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public List getGroupValues() {
		return groupValues;
	}

	@JsonProperty("GroupValues")
	public void setGroupValues(List groupValues) {
		this.groupValues = groupValues;
	}

	public String getActualValue() {
		return actualValue;
	}

	@JsonProperty("ActualValue")
	public void setActualValue(String actualValue) {
		this.actualValue = actualValue;
	}

	public String getIndicator() {
		return indicator;
	}

	@JsonProperty("Indicator")
	public void setIndicator(String indicator) {
		this.indicator = indicator;
	}

	public String getGroupType() {
		return groupType;
	}

	@JsonProperty("GroupType")
	public void setGroupType(String groupType) {
		this.groupType = groupType;
	}

	
	
		
}
	

