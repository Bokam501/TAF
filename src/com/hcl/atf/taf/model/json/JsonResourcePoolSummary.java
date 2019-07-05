package com.hcl.atf.taf.model.json;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.hcl.atf.taf.model.TestfactoryResourcePool;


public class JsonResourcePoolSummary implements java.io.Serializable {
	private static final Log log = LogFactory.getLog(JsonResourcePool.class);
	
	@JsonProperty
	private Integer jsonResourcePoolSummaryId;
	@JsonProperty
	private Integer resourcePoolId;	
	@JsonProperty
	private String resourcePoolName;
	@JsonProperty
	private Integer engagementManager_Count;
	@JsonProperty
	private Integer testManager_Count;
	@JsonProperty
	private Integer testLead_Count;
	@JsonProperty
	private Integer tester_Count;
	@JsonProperty
	private Integer programManager_Count;
	@JsonProperty
	private Integer resourceManager_Count;
	@JsonProperty
	private Integer userRoleId;
	@JsonProperty	
	private String roleName;
	@JsonProperty
	private Integer userTypeId;
	@JsonProperty	
	private String userTypeLabel;
	
	public JsonResourcePoolSummary() {
	}

	public JsonResourcePoolSummary(TestfactoryResourcePool resourcePool) {		
		this.resourcePoolId = resourcePool.getResourcePoolId();
		this.resourcePoolName = resourcePool.getResourcePoolName();
		
	}

	@JsonIgnore
	public TestfactoryResourcePool getJsonResourcePoolMapping(){
		TestfactoryResourcePool resourcePool = new TestfactoryResourcePool();
		resourcePool.setResourcePoolId(resourcePoolId);
		resourcePool.setResourcePoolName(resourcePoolName);
		return resourcePool;		
	}

	public Integer getResourcePoolId() {
		return resourcePoolId;
	}

	public void setResourcePoolId(Integer resourcePoolId) {
		this.resourcePoolId = resourcePoolId;
		}



	public String getResourcePoolName() {
		return resourcePoolName;
	}

	public void setResourcePoolName(String resourcePoolName) {
		this.resourcePoolName = resourcePoolName;
	}

	public Integer getEngagementManager_Count() {
		return engagementManager_Count;
	}

	public void setEngagementManager_Count(Integer engagementManager_Count) {
		this.engagementManager_Count = engagementManager_Count;
	}

	public Integer getTestManager_Count() {
		return testManager_Count;
	}

	public void setTestManager_Count(Integer testManager_Count) {
		this.testManager_Count = testManager_Count;
	}

	public Integer getTestLead_Count() {
		return testLead_Count;
	}

	public void setTestLead_Count(Integer testLead_Count) {
		this.testLead_Count = testLead_Count;
	}

	public Integer getTester_Count() {
		return tester_Count;
	}

	public void setTester_Count(Integer tester_Count) {
		this.tester_Count = tester_Count;
	}

	public Integer getProgramManager_Count() {
		return programManager_Count;
	}

	public void setProgramManager_Count(Integer programManager_Count) {
		this.programManager_Count = programManager_Count;
	}

	public Integer getResourceManager_Count() {
		return resourceManager_Count;
	}

	public void setResourceManager_Count(Integer resourceManager_Count) {
		this.resourceManager_Count = resourceManager_Count;
	}

	public Integer getJsonResourcePoolSummaryId() {
		return jsonResourcePoolSummaryId;
	}

	public void setJsonResourcePoolSummaryId(Integer jsonResourcePoolSummaryId) {
		this.jsonResourcePoolSummaryId = jsonResourcePoolSummaryId;
	}

	public Integer getUserRoleId() {
		return userRoleId;
	}

	public void setUserRoleId(Integer userRoleId) {
		this.userRoleId = userRoleId;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public Integer getUserTypeId() {
		return userTypeId;
	}

	public void setUserTypeId(Integer userTypeId) {
		this.userTypeId = userTypeId;
	}

	public String getUserTypeLabel() {
		return userTypeLabel;
	}

	public void setUserTypeLabel(String userTypeLabel) {
		this.userTypeLabel = userTypeLabel;
	}

}
