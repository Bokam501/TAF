package com.hcl.atf.taf.model.json;



import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.hcl.atf.taf.model.CommonActiveStatusMaster;
import com.hcl.atf.taf.model.UserList;
import com.hcl.atf.taf.model.UserRoleMaster;
import com.hcl.atf.taf.model.UserTypeMaster;
public class JsonUserRoleMaster implements java.io.Serializable {

	
	@JsonProperty
	private Integer userRoleId;
	@JsonProperty	
	private String roleName;
	@JsonProperty	
	private String roleLabel;
	@JsonProperty
	private String description;
	@JsonProperty
	private Integer status;


	public JsonUserRoleMaster() {
	}

	public JsonUserRoleMaster(UserRoleMaster userRoleMaster) {
		userRoleId=userRoleMaster.getUserRoleId();
		roleName=userRoleMaster.getRoleName();
		roleLabel=userRoleMaster.getRoleLabel();
		description=userRoleMaster.getDescription();
		status=userRoleMaster.getStatus();
		
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

	public String getRoleLabel() {
		return roleLabel;
	}

	public void setRoleLabel(String roleLabel) {
		this.roleLabel = roleLabel;
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
	public UserRoleMaster getUserRoleMaster(){
		UserRoleMaster userRoleMaster = new UserRoleMaster();
		userRoleMaster.setUserRoleId(userRoleId);
		userRoleMaster.setRoleName(roleName);
		userRoleMaster.setRoleLabel(roleLabel);
		userRoleMaster.setDescription(description);
		userRoleMaster.setStatus(status);
		return userRoleMaster;
	}

}
