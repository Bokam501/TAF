package com.hcl.atf.taf.model.json;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.hcl.atf.taf.model.DevicePlatformMaster;
import com.hcl.atf.taf.model.TestEnviromentMaster;
import com.hcl.atf.taf.model.TestToolMaster;


public class JsonTestEnvironmentMaster implements java.io.Serializable {

	@JsonProperty
	private Integer testEnvironmentId;
	@JsonProperty
	private String devicePlatformName;
	@JsonProperty
	private String testToolName;
	

	public JsonTestEnvironmentMaster() {
	}

	public JsonTestEnvironmentMaster(TestEnviromentMaster testEnviromentMaster) {
		this.testEnvironmentId=testEnviromentMaster.getTestEnvironmentId();
		if(testEnviromentMaster.getTestToolMaster()!=null)
			this.testToolName=testEnviromentMaster.getTestToolMaster().getTestToolName();
		if(testEnviromentMaster.getDevicePlatformMaster()!=null)
			this.devicePlatformName=testEnviromentMaster.getDevicePlatformMaster().getDevicePlatformName();
	}

	public Integer getTestEnvironmentId() {
		return testEnvironmentId;
	}

	public void setTestEnvironmentId(Integer testEnvironmentId) {
		this.testEnvironmentId = testEnvironmentId;
	}

	public String getDevicePlatformName() {
		return devicePlatformName;
	}

	public void setDevicePlatformName(String devicePlatformName) {
		this.devicePlatformName = devicePlatformName;
	}

	public String getTestToolName() {
		return testToolName;
	}

	public void setTestToolName(String testToolName) {
		this.testToolName = testToolName;
	}

	@JsonIgnore
	public TestEnviromentMaster getTestEnviromentMaster(){
		TestEnviromentMaster testEnviromentMaster = new TestEnviromentMaster();
		testEnviromentMaster.setTestEnvironmentId(testEnvironmentId);
		
		TestToolMaster testToolMaster=new TestToolMaster();
		testToolMaster.setTestToolName(testToolName);
		testEnviromentMaster.setTestToolMaster(testToolMaster);
		
		DevicePlatformMaster devicePlatformMaster = new DevicePlatformMaster();
		devicePlatformMaster.setDevicePlatformName(devicePlatformName);
		testEnviromentMaster.setDevicePlatformMaster(devicePlatformMaster);
		
		
		return testEnviromentMaster;
		
	}
	

}
