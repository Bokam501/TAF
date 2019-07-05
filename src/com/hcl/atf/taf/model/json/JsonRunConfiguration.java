package com.hcl.atf.taf.model.json;

import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.hcl.atf.taf.model.EnvironmentCombination;
import com.hcl.atf.taf.model.GenericDevices;
import com.hcl.atf.taf.model.HostList;
import com.hcl.atf.taf.model.ProductMaster;
import com.hcl.atf.taf.model.ProductType;
import com.hcl.atf.taf.model.RunConfiguration;
import com.hcl.atf.taf.model.ScriptTypeMaster;
import com.hcl.atf.taf.model.TestCaseList;
import com.hcl.atf.taf.model.TestRunPlan;
import com.hcl.atf.taf.model.TestSuiteList;
import com.hcl.atf.taf.model.TestToolMaster;

public class JsonRunConfiguration {
	@JsonProperty
	private Integer	runconfigId;
	@JsonProperty
	private Integer environmentcombinationId;
	@JsonProperty
	private String environmentcombinationName;
	@JsonProperty
	private Integer deviceId;
	@JsonProperty
	private String deviceName;
	@JsonProperty
	private String UDID;
	@JsonProperty
	private Integer productId;
	@JsonProperty
	private String productName;
	@JsonProperty
	private String runConfigurationName;
	@JsonProperty
	private Integer status;
	
	@JsonProperty
	private Integer hostId;
	@JsonProperty
	private String hostIpAddress;
	@JsonProperty
	private String hostName;
	@JsonProperty
	private Integer availableStatus;
	@JsonProperty
	private String hostStatus;
	@JsonProperty
	private Integer productType;
	@JsonProperty
	private Integer testToolId;
	@JsonProperty
	private String testToolName;
	@JsonProperty
	private Integer testSuiteId;
	@JsonProperty
	private String testSuiteName;
	@JsonProperty
	private Integer testCaseId;
	@JsonProperty
	private String testCaseName;
	@JsonProperty
	private Integer testRunPlanId;
	@JsonProperty
	private String testRunPlanName;
	@JsonProperty
	private String testScriptType;
	@JsonProperty
	private String testScriptFileLocation;
	@JsonProperty
	private Integer isSelected;
	@JsonProperty
	private String copyTestPlanTestSuite;
	
	public JsonRunConfiguration(){
		
	}
	
	public JsonRunConfiguration(RunConfiguration runConfiguration) {
		
		this.runconfigId=runConfiguration.getRunconfigId();
		if(runConfiguration.getGenericDevice()!=null){
			//The mapped target is a Device
			this.deviceId=runConfiguration.getGenericDevice().getGenericsDevicesId();
			this.deviceName=runConfiguration.getGenericDevice().getName();
			this.UDID=runConfiguration.getGenericDevice().getUDID();
			this.availableStatus=runConfiguration.getGenericDevice().getAvailableStatus();
			if(runConfiguration.getGenericDevice().getHostList()!=null){
				this.hostId=runConfiguration.getGenericDevice().getHostList().getHostId();
				this.hostIpAddress=runConfiguration.getGenericDevice().getHostList().getHostIpAddress();
				this.hostName=runConfiguration.getGenericDevice().getHostList().getHostName();
				if(runConfiguration.getHostList().getCommonActiveStatusMaster() != null)
					this.hostStatus=runConfiguration.getGenericDevice().getHostList().getCommonActiveStatusMaster().getStatus();
			}
		}
		
		if(runConfiguration.getEnvironmentcombination()!=null){
			this.environmentcombinationId=runConfiguration.getEnvironmentcombination().getEnvironment_combination_id();
			this.environmentcombinationName=runConfiguration.getEnvironmentcombination().getEnvironmentCombinationName();
		}
		
		if(runConfiguration.getProduct() != null){
			this.productId=runConfiguration.getProduct().getProductId();
		}
		this.runConfigurationName=runConfiguration.getRunconfigName();
		if (runConfiguration.getTestTool() != null) {
			this.testToolId = runConfiguration.getTestTool().getTestToolId();
			this.testToolName = runConfiguration.getTestTool().getTestToolName();
		}
		
		if(runConfiguration.getTestSuiteLists()!=null && !runConfiguration.getTestSuiteLists().isEmpty()){
			Set<TestSuiteList> testSuiteLists= runConfiguration.getTestSuiteLists();
			if(testSuiteLists.size()!=0){
				TestSuiteList testSuiteList= testSuiteLists.iterator().next();
				if(testSuiteList!=null){
					testSuiteId=testSuiteList.getTestSuiteId();
					testSuiteName=testSuiteList.getTestSuiteName();
					if(testSuiteList.getTestCaseLists()!=null && !testSuiteList.getTestCaseLists().isEmpty()){
						Set<TestCaseList> testCaseLists= testSuiteList.getTestCaseLists();
						if(testCaseLists.size()!=0){
							TestCaseList testCaseList= testCaseLists.iterator().next();
							if(testCaseList!=null){
								testCaseName=testCaseList.getTestCaseName();
								testCaseId=testCaseList.getTestCaseId();
							}
						}
					}
				}
			}
		}		
		if(runConfiguration.getTestRunPlan() != null && runConfiguration.getTestRunPlan().getTestRunPlanId() != null){
			testRunPlanId = runConfiguration.getTestRunPlan().getTestRunPlanId();
		}
		if(runConfiguration.getTestRunPlan() != null && runConfiguration.getTestRunPlan().getTestRunPlanName() != null){
			testRunPlanName = runConfiguration.getTestRunPlan().getTestRunPlanName();
		}
		if (runConfiguration.getScriptTypeMaster() != null) {
			this.testScriptType = runConfiguration.getScriptTypeMaster().getScriptType();
		}
		this.testScriptFileLocation = runConfiguration.getTestScriptFileLocation();
		
		if(runConfiguration.getProductType() != null){
			if(runConfiguration.getProductType().getProductTypeId() != null)
				this.productType = runConfiguration.getProductType().getProductTypeId();
			else
				this.productType = -1;
		}
		
		if(runConfiguration.getHostList()!=null){
			this.hostId=runConfiguration.getHostList().getHostId();
			this.hostIpAddress=runConfiguration.getHostList().getHostIpAddress();
			this.hostName=runConfiguration.getHostList().getHostName();
			if(runConfiguration.getHostList().getCommonActiveStatusMaster() != null)
				this.hostStatus=runConfiguration.getHostList().getCommonActiveStatusMaster().getStatus();
		}
		if(runConfiguration.getRunConfigStatus() != null)
			this.status = runConfiguration.getRunConfigStatus();
		
		if(runConfiguration.getCopyTestPlanTestSuite() != null)
			this.copyTestPlanTestSuite = runConfiguration.getCopyTestPlanTestSuite();
		
		this.isSelected = 0;
	}
	
	
	@JsonIgnore
	public RunConfiguration getRunConfiguration() {
		
		RunConfiguration runConfiguration = new RunConfiguration();
		runConfiguration.setRunconfigId(runconfigId);
		EnvironmentCombination environmentcombination = new EnvironmentCombination();
		environmentcombination.setEnvironment_combination_id(environmentcombinationId);
		runConfiguration.setEnvironmentcombination(environmentcombination);
		GenericDevices genericDevices = new GenericDevices();
		genericDevices.setGenericsDevicesId(deviceId);
		runConfiguration.setGenericDevice(genericDevices);
		ProductMaster productMaster = new ProductMaster();
		productMaster.setProductId(productId);
		runConfiguration.setProduct(productMaster);
		HostList hostList = new HostList();
		hostList.setHostId(hostId);
		runConfiguration.setHostList(hostList);
		
		if (testToolId != null) {
			TestToolMaster testTool = new TestToolMaster();
			testTool.setTestToolId(testToolId);
			testTool.setTestToolName(testToolName);
			runConfiguration.setTestTool(testTool);
		}
		
		if(testScriptType != null){
			ScriptTypeMaster scriptTypeMaster = new ScriptTypeMaster();
			scriptTypeMaster.setScriptType(testScriptType);
			runConfiguration.setScriptTypeMaster(scriptTypeMaster);
		}
		if(productType != null){
			ProductType  productTyp = new ProductType();
			productTyp.setProductTypeId(productType);
			runConfiguration.setProductType(productTyp);
		}
		runConfiguration.setTestScriptFileLocation(testScriptFileLocation);
		if(testRunPlanId != null){
			TestRunPlan testRunPlan = new TestRunPlan();
			testRunPlan.setTestRunPlanId(testRunPlanId);
			testRunPlan.setTestRunPlanName(testRunPlanName);
		}
		
		if(copyTestPlanTestSuite != null && !copyTestPlanTestSuite.isEmpty()){
			runConfiguration.setCopyTestPlanTestSuite(copyTestPlanTestSuite);
		} 
		
		return runConfiguration;		
	}
	
	public Integer getRunconfigId() {
		return runconfigId;
	}
	public void setRunconfigId(Integer runconfigId) {
		this.runconfigId = runconfigId;
	}
	public Integer getEnvironmentcombinationId() {
		return environmentcombinationId;
	}
	public void setEnvironmentcombinationId(Integer environmentcombinationId) {
		this.environmentcombinationId = environmentcombinationId;
	}
	public String getEnvironmentcombinationName() {
		return environmentcombinationName;
	}
	public void setEnvironmentcombinationName(String environmentcombinationName) {
		this.environmentcombinationName = environmentcombinationName;
	}
	public Integer getDeviceId() {
		return deviceId;
	}
	public void setDeviceId(Integer deviceId) {
		this.deviceId = deviceId;
	}
	public String getDeviceName() {
		return deviceName;
	}
	public void setDeviceName(String deviceName) {
		this.deviceName = deviceName;
	}
	public Integer getProductId() {
		return productId;
	}
	public void setProductId(Integer productId) {
		this.productId = productId;
	}
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public String getRunConfigurationName() {
		return runConfigurationName;
	}
	public void setRunConfigurationName(String runConfigurationName) {
		this.runConfigurationName = runConfigurationName;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	
	
	@Override
	public boolean equals(Object o) {
		JsonRunConfiguration jsonRunConfiguration = (JsonRunConfiguration) o;
		if (this.runconfigId == jsonRunConfiguration.getRunconfigId()) {
			return true;
		}else{
			return false;
		}
	}
	
	@Override
	public int hashCode(){
	    return (int) runconfigId;
	  }
	public String getUDID() {
		return UDID;
	}
	public void setUDID(String uDID) {
		UDID = uDID;
	}
	public Integer getHostId() {
		return hostId;
	}
	public void setHostId(Integer hostId) {
		this.hostId = hostId;
	}
	public String getHostIpAddress() {
		return hostIpAddress;
	}
	public void setHostIpAddress(String hostIpAddress) {
		this.hostIpAddress = hostIpAddress;
	}
	public String getHostName() {
		return hostName;
	}
	public void setHostName(String hostName) {
		this.hostName = hostName;
	}
	public Integer getAvailableStatus() {
		return availableStatus;
	}
	public void setAvailableStatus(Integer availableStatus) {
		this.availableStatus = availableStatus;
	}
	public String getHostStatus() {
		return hostStatus;
	}
	public void setHostStatus(String hostStatus) {
		this.hostStatus = hostStatus;
	}
	public Integer getProductType() {
		return productType;
	}
	public void setProductType(Integer productType) {
		this.productType = productType;
	}
	public Integer getTestToolId() {
		return testToolId;
	}
	public void setTestToolId(Integer testToolId) {
		this.testToolId = testToolId;
	}
	public String getTestToolName() {
		return testToolName;
	}
	public void setTestToolName(String testToolName) {
		this.testToolName = testToolName;
	}	
	public Integer getTestSuiteId() {
		return testSuiteId;
	}
	public void setTestSuiteId(Integer testSuiteId) {
		this.testSuiteId = testSuiteId;
	}
	public String getTestSuiteName() {
		return testSuiteName;
	}
	public void setTestSuiteName(String testSuiteName) {
		this.testSuiteName = testSuiteName;
	}
	public Integer getTestCaseId() {
		return testCaseId;
	}
	public void setTestCaseId(Integer testCaseId) {
		this.testCaseId = testCaseId;
	}
	public String getTestCaseName() {
		return testCaseName;
	}
	public void setTestCaseName(String testCaseName) {
		this.testCaseName = testCaseName;
	}
	public String getTestScriptType() {
		return testScriptType;
	}
	public void setTestScriptType(String testScriptType) {
		this.testScriptType = testScriptType;
	}
	public String getTestScriptFileLocation() {
		return testScriptFileLocation;
	}
	public void setTestScriptFileLocation(String testScriptFileLocation) {
		this.testScriptFileLocation = testScriptFileLocation;
	}

	public Integer getTestRunPlanId() {
		return testRunPlanId;
	}

	public void setTestRunPlanId(Integer testRunPlanId) {
		this.testRunPlanId = testRunPlanId;
	}

	public String getTestRunPlanName() {
		return testRunPlanName;
	}

	public void setTestRunPlanName(String testRunPlanName) {
		this.testRunPlanName = testRunPlanName;
	}

	public Integer getIsSelected() {
		return isSelected;
	}

	public void setIsSelected(Integer isSelected) {
		this.isSelected = isSelected;
	}

	public String getCopyTestPlanTestSuite() {
		return copyTestPlanTestSuite;
	}

	public void setCopyTestPlanTestSuite(String copyTestPlanTestSuite) {
		this.copyTestPlanTestSuite = copyTestPlanTestSuite;
	}
}