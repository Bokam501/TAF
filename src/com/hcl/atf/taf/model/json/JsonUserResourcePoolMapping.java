package com.hcl.atf.taf.model.json;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.hcl.atf.taf.controller.utilities.DateUtility;
import com.hcl.atf.taf.model.TestfactoryResourcePool;
import com.hcl.atf.taf.model.UserList;
import com.hcl.atf.taf.model.UserResourcePoolMapping;
import com.hcl.atf.taf.model.UserRoleMaster;

public class JsonUserResourcePoolMapping {

	private static final Log log = LogFactory.getLog(JsonUserResourcePoolMapping.class);

	@JsonProperty
	private Integer resourcePoolMappingId;	
	
	@JsonProperty
	private Integer userId;
	
	@JsonProperty
	private Integer resourcepoolId;
	
	@JsonProperty
	private String resourcepoolName;
	
	@JsonProperty
	private String fromDate;
	
	@JsonProperty
	private String toDate;
	
	@JsonProperty
	private String mappedDate;
	
	@JsonProperty
	private Integer mappedBy;
	
	@JsonProperty
	private String remarks;
	
	
	@JsonProperty
	private Integer modifiedBy;
	@JsonProperty
	private String modifiedDate;
	@JsonProperty
	private Integer userRoleId;
	
	
	public JsonUserResourcePoolMapping() {
	}

	public JsonUserResourcePoolMapping(UserResourcePoolMapping userResourcePoolMapping) {
		
		this.resourcePoolMappingId = userResourcePoolMapping.getResourcePoolMappingId();
		this.userId = userResourcePoolMapping.getUserId().getUserId();
		if(userResourcePoolMapping.getResourcepoolId()!=null && userResourcePoolMapping.getResourcepoolId().getResourcePoolId() != null){
			this.resourcepoolId = userResourcePoolMapping.getResourcepoolId().getResourcePoolId();
			this.resourcepoolName = userResourcePoolMapping.getResourcepoolId().getResourcePoolName();
		}
		
		 if(userResourcePoolMapping.getFromDate()!=null){
			 this.fromDate = DateUtility.dateformatWithSlashWithOutTime(userResourcePoolMapping.getFromDate());	
		 }
		 if(userResourcePoolMapping.getToDate()!=null){
			 this.toDate = DateUtility.dateformatWithSlashWithOutTime(userResourcePoolMapping.getToDate());	
		 }
		 
		
		if(userResourcePoolMapping.getMappedDate() != null){
			this.mappedDate = DateUtility.dateToStringWithSeconds1(userResourcePoolMapping.getMappedDate());
		}
		if(userResourcePoolMapping.getModifiedDate() != null){
			this.modifiedDate = DateUtility.dateformatWithOutTime(userResourcePoolMapping.getMappedDate());
		}
		
		if(userResourcePoolMapping.getMappedBy() != null){
			this.mappedBy = userResourcePoolMapping.getMappedBy().getUserId();
		}
		this.remarks = userResourcePoolMapping.getRemarks();
		if(userResourcePoolMapping.getMappedBy()!= null){
			this.modifiedBy = userResourcePoolMapping.getMappedBy().getUserId();
		}
		if(userResourcePoolMapping.getUserRoleId() != null){
			this.userRoleId = userResourcePoolMapping.getUserRoleId().getUserRoleId();
		}
	}

	@JsonIgnore
	public UserResourcePoolMapping getUserResourcePoolMapping() {
		UserResourcePoolMapping userResourcePoolMapping = new UserResourcePoolMapping();
		userResourcePoolMapping.setResourcePoolMappingId(resourcePoolMappingId);
		
		if (this.userId != null) {
			UserList user = new UserList();
			user.setUserId(userId);			
			userResourcePoolMapping.setUserId(user);
		}
		
		
		if (this.resourcepoolId != null) {
			TestfactoryResourcePool rp = new TestfactoryResourcePool();
			rp.setResourcePoolId(resourcepoolId);			
			userResourcePoolMapping.setResourcepoolId(rp);
		}
			
		if(this.fromDate != null) {
			
			userResourcePoolMapping.setFromDate(DateUtility.dateformatWithOutTime(this.fromDate));
		} 
		
		if(this.toDate!= null){
			userResourcePoolMapping.setToDate(DateUtility.dateformatWithOutTime(this.toDate));
		}
		
		
		if(this.mappedDate == null || this.mappedDate.trim().isEmpty()) {
			userResourcePoolMapping.setMappedDate(DateUtility.getCurrentTime());
		} else {
			userResourcePoolMapping.setMappedDate(DateUtility.toFormatDate(this.mappedDate));
		}
		
		
		if (this.mappedBy != null) {
			UserList user = new UserList();
			user.setUserId(mappedBy);			
			userResourcePoolMapping.setMappedBy(user);
		}
		
		if (this.modifiedBy != null) {
			UserList user = new UserList();
			user.setUserId(modifiedBy);			
			userResourcePoolMapping.setModifiedBy(user);
		}
		
		
		userResourcePoolMapping.setModifiedDate(DateUtility.getCurrentTime());
		
		if (this.userRoleId != null) {
			UserRoleMaster role = new UserRoleMaster();
			role.setUserRoleId(userRoleId);			
			userResourcePoolMapping.setUserRoleId(role);
		}
		
		userResourcePoolMapping.setRemarks(remarks);
		
		return userResourcePoolMapping;
	}

	public Integer getResourcePoolMappingId() {
		return resourcePoolMappingId;
	}

	public void setResourcePoolMappingId(Integer resourcePoolMappingId) {
		this.resourcePoolMappingId = resourcePoolMappingId;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public Integer getResourcepoolId() {
		return resourcepoolId;
	}

	public void setResourcepoolId(Integer resourcepoolId) {
		this.resourcepoolId = resourcepoolId;
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

	public String getMappedDate() {
		return mappedDate;
	}

	public void setMappedDate(String mappedDate) {
		this.mappedDate = mappedDate;
	}

	public Integer getMappedBy() {
		return mappedBy;
	}

	public void setMappedBy(Integer mappedBy) {
		this.mappedBy = mappedBy;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public Integer getModifiedBy() {
		return modifiedBy;
	}

	public void setModifiedBy(Integer modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	public String getModifiedDate() {
		return modifiedDate;
	}

	public void setModifiedDate(String modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

	public Integer getUserRoleId() {
		return userRoleId;
	}

	public void setUserRoleId(Integer userRoleId) {
		this.userRoleId = userRoleId;
	}

	public String getResourcepoolName() {
		return resourcepoolName;
	}

	public void setResourcepoolName(String resourcepoolName) {
		this.resourcepoolName = resourcepoolName;
	}

		
		
}
