package com.hcl.atf.taf.model.json;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.hcl.atf.taf.controller.utilities.DateUtility;
import com.hcl.atf.taf.model.UserList;
import com.hcl.atf.taf.model.UserRoleMaster;
import com.hcl.atf.taf.model.UserRoles;

public class JsonUserRoles {
	@JsonProperty
	private Integer mutliRoleId;
	@JsonProperty
	private String loginId;
	@JsonProperty
	private Integer userId;
	@JsonProperty
	private String roleName;
	@JsonProperty
	private Integer roleId;
	@JsonProperty
	private String createdDate;
	@JsonProperty
	private String fromDate;
	@JsonProperty
	private String toDate;
	@JsonProperty
	private String createdBy;
	@JsonProperty
	private Integer status;
	@JsonProperty
	private String comments;
	
	public JsonUserRoles(){
		
	}
	public JsonUserRoles(UserRoles userRoles){
		this.mutliRoleId=userRoles.getMutliRoleId();
		if(userRoles.getUserList()!=null){
			this.loginId=userRoles.getUserList().getLoginId();
			this.userId=userRoles.getUserList().getUserId();
		}
		
		if(userRoles.getRole()!=null){
			this.roleId=userRoles.getRole().getUserRoleId();
			this.roleName=userRoles.getRole().getRoleLabel();
		}
		if(userRoles.getCreatedDate()!=null)
			this.createdDate=DateUtility.dateformatWithOutTime(userRoles.getCreatedDate());
		if(userRoles.getCreatedBy()!=null)
			this.createdBy=userRoles.getCreatedBy().getLoginId();
		if(userRoles.getFromDate()!=null)
			this.fromDate=DateUtility.dateformatWithOutTime(userRoles.getFromDate());
		if(userRoles.getToDate()!=null)
			this.toDate=DateUtility.dateformatWithOutTime(userRoles.getToDate());
		this.status=userRoles.getStatus();
		this.comments=userRoles.getComments();
	}
	
	@JsonIgnore
	public UserRoles getUserRoles(){
		
		UserRoles userRoles = new UserRoles();
		
		
		userRoles.setMutliRoleId(mutliRoleId);
		UserList userList= new UserList();
		userList.setUserId(userId);
		
		userRoles.setUserList(userList);
		
		UserRoleMaster userRoleMaster = new UserRoleMaster();
		userRoleMaster.setUserRoleId(roleId);
		userRoles.setRole(userRoleMaster);
		
		if(createdDate!=null){
			userRoles.setCreatedDate(DateUtility.dateformatWithOutTime(createdDate));
		}else{
			userRoles.setCreatedDate(DateUtility.getCurrentTime());
		}
		userRoles.setFromDate(DateUtility.dateformatWithOutTime(fromDate));
		userRoles.setToDate(DateUtility.dateformatWithOutTime(toDate));
		userRoles.setStatus(status);
		userRoles.setComments(comments);
		return userRoles;
	}
	public Integer getMutliRoleId() {
		return mutliRoleId;
	}
	public void setMutliRoleId(Integer mutliRoleId) {
		this.mutliRoleId = mutliRoleId;
	}
	public String getLoginId() {
		return loginId;
	}
	public void setLoginId(String loginId) {
		this.loginId = loginId;
	}
	public Integer getUserId() {
		return userId;
	}
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	public String getRoleName() {
		return roleName;
	}
	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}
	public Integer getRoleId() {
		return roleId;
	}
	public void setRoleId(Integer roleId) {
		this.roleId = roleId;
	}
	public String getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
	}
	public String getFromDate() {
		return fromDate;
	}
	public void setFromDate(String fromDate) {
		this.fromDate = fromDate;
	}
	public String getToDate() {
		return toDate;
	}
	public void setToDate(String toDate) {
		this.toDate = toDate;
	}
	public String getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public String getComments() {
		return comments;
	}
	public void setComments(String comments) {
		this.comments = comments;
	}
		
	
}
