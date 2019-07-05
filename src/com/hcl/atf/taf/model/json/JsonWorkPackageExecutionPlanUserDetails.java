package com.hcl.atf.taf.model.json;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.hcl.atf.taf.controller.utilities.DateUtility;
import com.hcl.atf.taf.model.dto.WorkPackageExecutionPlanUserDetails;

public class JsonWorkPackageExecutionPlanUserDetails {
	private static final Log log = LogFactory.getLog(JsonProductLocale.class);

	public JsonWorkPackageExecutionPlanUserDetails(){
		
	}
	
	
	@JsonProperty
	private String loginId;
	@JsonProperty
	private Integer resourceAllocationCount;
	@JsonProperty
	private String plannedExecutionDate;
	
	public JsonWorkPackageExecutionPlanUserDetails(WorkPackageExecutionPlanUserDetails workPackageExecutionPlanUserDetails  ){
		this.loginId=workPackageExecutionPlanUserDetails.getLoginId();
		this.resourceAllocationCount=workPackageExecutionPlanUserDetails.getTotalAllocatedCount();
		this.plannedExecutionDate=DateUtility.dateformatWithOutTime(workPackageExecutionPlanUserDetails.getPlannedExecutionDate());
	}
	
	@JsonIgnore
	public WorkPackageExecutionPlanUserDetails  getWorkPackageExecutionPlanUserDetails(){
		WorkPackageExecutionPlanUserDetails workPackageExecutionPlanUserDetails  =new WorkPackageExecutionPlanUserDetails();
		workPackageExecutionPlanUserDetails.setLoginId(this.loginId);
		workPackageExecutionPlanUserDetails.setTotalAllocatedCount(this.resourceAllocationCount);
			return workPackageExecutionPlanUserDetails;
		
	}

	public String getLoginId() {
		return loginId;
	}

	public void setLoginId(String loginId) {
		this.loginId = loginId;
	}

	public Integer getResourceAllocationCount() {
		return resourceAllocationCount;
	}

	public void setResourceAllocationCount(Integer resourceAllocationCount) {
		this.resourceAllocationCount = resourceAllocationCount;
	}

	public String getPlannedExecutionDate() {
		return plannedExecutionDate;
	}

	public void setPlannedExecutionDate(String plannedExecutionDate) {
		this.plannedExecutionDate = plannedExecutionDate;
	}
	
	
	

}
