package com.hcl.atf.taf.model.json;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.hcl.atf.taf.controller.utilities.DateUtility;
import com.hcl.atf.taf.model.DimensionMaster;
import com.hcl.atf.taf.model.DimensionType;
import com.hcl.atf.taf.model.UserList;

public class JsonDimensionMaster {

	@JsonProperty
	private int dimensionId;
	@JsonProperty
	private String name;
	@JsonProperty
	private String description;
	@JsonProperty
	private Integer status;
	@JsonProperty
	private Integer typeId;
	@JsonProperty
	private String typeName;
	@JsonProperty
	private Integer parentId;
	@JsonProperty
	private String parentName;
	@JsonProperty
	private Integer ownerId;
	@JsonProperty
	private String ownerName;
	@JsonProperty
	private String createdDate;
	@JsonProperty
	private Integer modifiedById;
	@JsonProperty
	private String modifiedByName;
	@JsonProperty
	private String modifiedDate;
	
	public JsonDimensionMaster(){
		
	}
	
	public JsonDimensionMaster(DimensionMaster dimensionMaster) {
		this.dimensionId = dimensionMaster.getDimensionId();
		this.name = dimensionMaster.getName();
		this.description = dimensionMaster.getDescription();
		
		if(dimensionMaster.getStatus() != null){
			this.status = dimensionMaster.getStatus();
		}else{
			this.status = 0;
		}
		if(dimensionMaster.getType() != null){
			this.typeId = dimensionMaster.getType().getDimensionTypeId();
			this.typeName = dimensionMaster.getType().getName();
		}
		if(dimensionMaster.getParent() != null){
			this.parentId = dimensionMaster.getParent().getDimensionId();
			this.parentName = dimensionMaster.getParent().getName();
		}else{
			this.parentId = 1;
			this.parentName = "--";
		}
		if(dimensionMaster.getOwner() != null){
			this.ownerId = dimensionMaster.getOwner().getUserId();
			this.ownerName = dimensionMaster.getOwner().getUserDisplayName();
		}else{
			this.ownerId = 0;
			this.ownerName = null;
		}
		if(dimensionMaster.getCreatedDate() != null){
			this.createdDate = DateUtility.dateToStringWithSeconds1(dimensionMaster.getCreatedDate());
		}else{
			this.createdDate = DateUtility.dateToStringWithSeconds1(new Date());
		}
		if(dimensionMaster.getModifiedBy() != null){
			this.modifiedById = dimensionMaster.getModifiedBy().getUserId();
			this.modifiedByName = dimensionMaster.getModifiedBy().getUserDisplayName();
		}else{
			this.modifiedById = 0;
			this.modifiedByName = null;
		}
		if(dimensionMaster.getModifiedDate() != null){
			this.modifiedDate = DateUtility.dateToStringWithSeconds1(dimensionMaster.getModifiedDate());
		}else{
			this.modifiedDate = DateUtility.dateToStringWithSeconds1(new Date());
		}
	}

	

	public int getDimensionId() {
		return dimensionId;
	}
	public void setDimensionId(int dimensionId) {
		this.dimensionId = dimensionId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
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
	public Integer getTypeId() {
		return typeId;
	}
	public void setTypeId(Integer typeId) {
		this.typeId = typeId;
	}
	public String getTypeName() {
		return typeName;
	}
	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}
	public Integer getParentId() {
		return parentId;
	}
	public void setParentId(Integer parentId) {
		this.parentId = parentId;
	}
	public String getParentName() {
		return parentName;
	}
	public void setParentName(String parentName) {
		this.parentName = parentName;
	}
	public Integer getOwnerId() {
		return ownerId;
	}
	public void setOwnerId(Integer ownerId) {
		this.ownerId = ownerId;
	}
	public String getOwnerName() {
		return ownerName;
	}
	public void setOwnerName(String ownerName) {
		this.ownerName = ownerName;
	}
	public String getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
	}
	public Integer getModifiedById() {
		return modifiedById;
	}
	public void setModifiedById(Integer modifiedById) {
		this.modifiedById = modifiedById;
	}
	public String getModifiedByName() {
		return modifiedByName;
	}
	public void setModifiedByName(String modifiedByName) {
		this.modifiedByName = modifiedByName;
	}
	public String getModifiedDate() {
		return modifiedDate;
	}
	public void setModifiedDate(String modifiedDate) {
		this.modifiedDate = modifiedDate;
	}


	@JsonIgnore
	public DimensionMaster getDimensionMaster() {
		DimensionMaster dimensionMaster = new DimensionMaster();
		dimensionMaster.setDimensionId(this.dimensionId);
		dimensionMaster.setName(this.name);
		dimensionMaster.setDescription(this.description);
		if(this.status != null){
			dimensionMaster.setStatus(this.status);
		}else{
			dimensionMaster.setStatus(0);
		}
		DimensionType dimensionType = new DimensionType();
		if(this.typeId != null){
			dimensionType.setDimensionTypeId(this.typeId);
		}
		dimensionMaster.setType(dimensionType);
		
		DimensionMaster dimensionMasterParent = new DimensionMaster();
		if(this.parentId != null){
			dimensionMasterParent.setDimensionId(this.parentId);
		}else{
			dimensionMasterParent.setDimensionId(1);
		}
		dimensionMaster.setParent(dimensionMasterParent);
		
		UserList userList = new UserList();
		if(this.ownerId != null){
			userList.setUserId(this.ownerId);
		}else{
			userList.setUserId(0);
		}
		dimensionMaster.setOwner(userList);

		if(this.createdDate != null && !this.createdDate.trim().isEmpty()){
			dimensionMaster.setCreatedDate(DateUtility.toFormatDate(this.createdDate));
		}else{
			dimensionMaster.setCreatedDate(new Date());
		}
		
		userList = new UserList();
		if(this.modifiedById != null){
			userList.setUserId(this.modifiedById);
		}else{
			userList.setUserId(0);
		}
		dimensionMaster.setModifiedBy(userList);
		
		if(this.modifiedDate != null && !this.modifiedDate.trim().isEmpty()){
			dimensionMaster.setModifiedDate(DateUtility.toFormatDate(this.modifiedDate));
		}else{
			dimensionMaster.setModifiedDate(new Date());
		}
		
		return dimensionMaster;
	}
	
}
