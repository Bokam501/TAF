package com.hcl.atf.taf.model.json;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.hcl.atf.taf.controller.utilities.DateUtility;
import com.hcl.atf.taf.model.EntityMaster;
import com.hcl.atf.taf.model.EntityUserGroup;
import com.hcl.atf.taf.model.UserList;

public class JsonEntityUserGroup {
	
	@JsonProperty
	private Integer entityUserGroupId;
	private Integer entityTypeId;
	private Integer entityInstanceId;
	private Integer user;
	private Integer mappedBy;
	private String mappedDate;
	
	public JsonEntityUserGroup() {
		
	}

	public JsonEntityUserGroup(EntityUserGroup entityUserGroup) {
		this.entityUserGroupId = entityUserGroup.getEntityUserGroupId();
		if(entityUserGroup.getEntityTypeId() != null){
			this.entityTypeId = entityUserGroup.getEntityTypeId().getEntitymasterid();
		}
		this.entityInstanceId = entityUserGroup.getEntityInstanceId();
		if(entityUserGroup.getUser() != null){
			this.user = entityUserGroup.getUser().getUserId();
		}
		if(entityUserGroup.getMappedBy() != null){
			this.mappedBy = entityUserGroup.getMappedBy().getUserId();
		}
		this.mappedDate =  DateUtility.dateToStringWithSeconds1(entityUserGroup.getMappedDate());
		
	}
	
	@JsonIgnore
	public EntityUserGroup getEntityUserGroup() {
		EntityUserGroup entityUserGroup = new EntityUserGroup();
		
		entityUserGroup.setEntityUserGroupId(this.entityUserGroupId);
		
		if(this.user != null && this.user != 0){
			UserList user = new UserList();
			user.setUserId(this.user);
			entityUserGroup.setUser(user);
		}
		
		if(this.entityTypeId != null && this.entityTypeId != 0){
			EntityMaster entityMaster = new EntityMaster();
			entityMaster.setEntitymasterid(this.entityTypeId);
			entityUserGroup.setEntityTypeId(entityMaster);
		}
		
		if(this.entityInstanceId != null && this.entityInstanceId != 0){
			entityUserGroup.setEntityInstanceId(this.entityTypeId);
		}
	
		if(this.mappedDate == null || this.mappedDate.trim().isEmpty()) {
			entityUserGroup.setMappedDate(DateUtility.getCurrentTime());
		} else{
			entityUserGroup.setMappedDate(DateUtility.getCurrentTime());
		}
		
		if(this.mappedBy != null && this.mappedBy != 0){
			UserList mappedBy = new UserList();
			mappedBy.setUserId(this.mappedBy);
			entityUserGroup.setMappedBy(mappedBy);
		}
		
		return entityUserGroup;
	}

	public Integer getEntityUserGroupId() {
		return entityUserGroupId;
	}

	public void setEntityUserGroupId(Integer entityUserGroupId) {
		this.entityUserGroupId = entityUserGroupId;
	}

	public Integer getEntityTypeId() {
		return entityTypeId;
	}

	public void setEntityTypeId(Integer entityTypeId) {
		this.entityTypeId = entityTypeId;
	}

	public Integer getEntityInstanceId() {
		return entityInstanceId;
	}

	public void setEntityInstanceId(Integer entityInstanceId) {
		this.entityInstanceId = entityInstanceId;
	}

	public Integer getUser() {
		return user;
	}

	public void setUser(Integer user) {
		this.user = user;
	}

	public Integer getMappedBy() {
		return mappedBy;
	}

	public void setMappedBy(Integer mappedBy) {
		this.mappedBy = mappedBy;
	}

	public String getMappedDate() {
		return mappedDate;
	}

	public void setMappedDate(String mappedDate) {
		this.mappedDate = mappedDate;
	}

	

}
