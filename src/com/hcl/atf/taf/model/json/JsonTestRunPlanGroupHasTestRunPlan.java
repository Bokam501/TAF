package com.hcl.atf.taf.model.json;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.hcl.atf.taf.model.TestRunPlan;
import com.hcl.atf.taf.model.TestRunPlanGroup;
import com.hcl.atf.taf.model.TestRunPlangroupHasTestRunPlan;

public class JsonTestRunPlanGroupHasTestRunPlan {
	
@JsonProperty	
private Integer testRunPlanId;
@JsonProperty	
private String testRunPlanName;
@JsonProperty
private Integer testRunPlanGroupId;
@JsonProperty
private Integer order;
@JsonProperty
private Integer testRunGroupId;
@JsonProperty
private Integer isSelected;

	
	public JsonTestRunPlanGroupHasTestRunPlan(){
		
	}
	
	public JsonTestRunPlanGroupHasTestRunPlan(TestRunPlangroupHasTestRunPlan testRunPlangroupHasTestRunPlan){
		
	
		if(testRunPlangroupHasTestRunPlan.getTestrunplan()!=null){
			this.testRunPlanId = testRunPlangroupHasTestRunPlan.getTestrunplan().getTestRunPlanId();
			this.testRunPlanName = testRunPlangroupHasTestRunPlan.getTestrunplan().getTestRunPlanName();

		}
			
		if(testRunPlangroupHasTestRunPlan.getTestRunGroupId()!=null){
			this.testRunPlanGroupId = testRunPlangroupHasTestRunPlan.getTestRunPlanGroup().getTestRunPlanGroupId();
		}
		
		if(testRunPlangroupHasTestRunPlan.getExecutionOrder()!=null){
			this.order = testRunPlangroupHasTestRunPlan.getExecutionOrder();
		}
		
		if(testRunPlangroupHasTestRunPlan.getTestRunGroupId()!=null){
			this.testRunGroupId = testRunPlangroupHasTestRunPlan.getTestRunGroupId();
		}
		
	}

	@JsonIgnore
	public TestRunPlangroupHasTestRunPlan gettestRunPlangroupHasTestRunPlan(){
		TestRunPlangroupHasTestRunPlan testRunPlangroupHasTestRunPlan = new TestRunPlangroupHasTestRunPlan();
		
		if(testRunPlanId != null){
			TestRunPlan testRunPlan = new TestRunPlan();
			testRunPlan.setTestRunPlanId(testRunPlanId);
			testRunPlangroupHasTestRunPlan.setTestrunplan(testRunPlan);
		}
		
		if(testRunPlanGroupId != null){
			TestRunPlanGroup testRunPlanGroup = new TestRunPlanGroup();
			testRunPlanGroup.setTestRunPlanGroupId(testRunPlanGroupId);
			testRunPlangroupHasTestRunPlan.setTestRunPlanGroup(testRunPlanGroup);
		}
		
		if(order != null){
			testRunPlangroupHasTestRunPlan.setExecutionOrder(order);
		}
		
		if(testRunGroupId != null){
			testRunPlangroupHasTestRunPlan.setTestRunGroupId(testRunGroupId);
		}
	
		return testRunPlangroupHasTestRunPlan;
	}

	public Integer getTestRunPlanId() {
		return testRunPlanId;
	}

	public void setTestRunPlanId(Integer testRunPlanId) {
		this.testRunPlanId = testRunPlanId;
	}

	public Integer getTestRunPlanGroupId() {
		return testRunPlanGroupId;
	}

	public void setTestRunPlanGroupId(Integer testRunPlanGroupId) {
		this.testRunPlanGroupId = testRunPlanGroupId;
	}

	public Integer getOrder() {
		return order;
	}

	public void setOrder(Integer order) {
		this.order = order;
	}

	public Integer getTestRunGroupId() {
		return testRunGroupId;
	}

	public void setTestRunGroupId(Integer testRunGroupId) {
		this.testRunGroupId = testRunGroupId;
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
	
	
	
	
}
