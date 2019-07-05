package com.hcl.atf.taf.model.json;



import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.hcl.atf.taf.model.TestEnvironmentDevices;


public class JsonTestEnvironmentDevices implements java.io.Serializable {

	
	private static final long serialVersionUID = 1L;

	@JsonProperty
	private Integer testEnvironmentDevicesId;
	@JsonProperty
	private String name;
	
	@JsonProperty
	private String description;
	
	@JsonProperty
	private Integer status;
	
	public JsonTestEnvironmentDevices() {
	}

	public JsonTestEnvironmentDevices(TestEnvironmentDevices testEnvironmentDevices) {
		
		this.testEnvironmentDevicesId=testEnvironmentDevices.getTestEnvironmentDevicesId();
		this.name=testEnvironmentDevices.getName();
		this.description=testEnvironmentDevices.getDescription();
	}
	public Integer getTestEnvironmentDevicesId() {
		return testEnvironmentDevicesId;
	}

	public void setTestEnvironmentDevicesId(Integer testEnvironmentDevicesId) {
		this.testEnvironmentDevicesId = testEnvironmentDevicesId;
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
	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	@JsonIgnore
	public TestEnvironmentDevices getTestEnvironmentDevices(){
		TestEnvironmentDevices testEnvironmentDevices = new TestEnvironmentDevices();
		testEnvironmentDevices.setTestEnvironmentDevicesId(testEnvironmentDevicesId);
		if(name!=null){
			testEnvironmentDevices.setName(name);
		 }
		testEnvironmentDevices.setDescription(description);
	
		return testEnvironmentDevices;
	}

	
}
