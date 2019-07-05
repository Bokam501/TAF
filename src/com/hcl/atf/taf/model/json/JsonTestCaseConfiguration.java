package com.hcl.atf.taf.model.json;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.hcl.atf.taf.model.EnvironmentCombination;
import com.hcl.atf.taf.model.TestCaseConfiguration;
import com.hcl.atf.taf.model.WorkPackageTestCaseExecutionPlan;

public class JsonTestCaseConfiguration {
	@JsonProperty
	private Integer testCaseConfigurationId;
	@JsonProperty
	private String environment_combination;
	@JsonProperty
	private Integer wptcepId;
	@JsonProperty
	private Integer workPackageId;
	@JsonProperty
	private Integer environment_combination_id;
	public JsonTestCaseConfiguration(TestCaseConfiguration testCaseConfiguration) {
		this.testCaseConfigurationId = testCaseConfiguration.getTestCaseConfigurationId();
		this.environment_combination=testCaseConfiguration.getEnvironmentCombination().getEnvironmentCombinationName();
		this.environment_combination_id=testCaseConfiguration.getEnvironmentCombination().getEnvironment_combination_id();
		if(testCaseConfiguration.getWorkpackage_run_list() !=null)
		{
			this.wptcepId =  testCaseConfiguration.getWorkpackage_run_list().getId();
		}
		
	}
	@JsonIgnore
	public TestCaseConfiguration  getTestCaseConfiguration(){
		TestCaseConfiguration testCaseConfiguration=new TestCaseConfiguration();
		testCaseConfiguration.setTestCaseConfigurationId(testCaseConfigurationId);
		EnvironmentCombination environmentCombination	=new EnvironmentCombination();
		environmentCombination.setEnvironment_combination_id(this.environment_combination_id);
		testCaseConfiguration.setEnvironmentCombination(environmentCombination);
		if(wptcepId!=null){
			WorkPackageTestCaseExecutionPlan workPackageTestCaseExecutionPlan=new WorkPackageTestCaseExecutionPlan();
			testCaseConfiguration.setWorkpackage_run_list(workPackageTestCaseExecutionPlan);
		}
	return testCaseConfiguration;
	}
	
	public Integer getTestCaseConfigurationId() {
		return testCaseConfigurationId;
	}
	public void setTestCaseConfigurationId(Integer testCaseConfigurationId) {
		this.testCaseConfigurationId = testCaseConfigurationId;
	}
	public String getEnvironment_combination() {
		return environment_combination;
	}
	public void setEnvironment_combination(String environment_combination) {
		this.environment_combination = environment_combination;
	}
	public Integer getWptcepId() {
		return wptcepId;
	}
	public void setWptcepId(Integer wptcepId) {
		this.wptcepId = wptcepId;
	}
	public Integer getWorkPackageId() {
		return workPackageId;
	}
	public void setWorkPackageId(Integer workPackageId) {
		this.workPackageId = workPackageId;
	}
	
}
