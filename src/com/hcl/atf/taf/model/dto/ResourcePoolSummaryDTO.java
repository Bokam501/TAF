package com.hcl.atf.taf.model.dto;


public class ResourcePoolSummaryDTO {
	
	private Integer jsonResourcePoolSummaryId;
	private Integer resourcePoolId;		
	private String resourcePoolName;	
	private Integer role_Count;
	private Integer userRoleId;		
	private String roleName;
	private Integer userTypeId;	
	private String userTypeLabel;
	
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
	public Integer getRole_Count() {
		return role_Count;
	}
	public void setRole_Count(Integer role_Count) {
		this.role_Count = role_Count;
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
