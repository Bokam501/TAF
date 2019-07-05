package com.hcl.atf.taf.model.json;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.hcl.atf.taf.controller.utilities.DateUtility;
import com.hcl.atf.taf.model.DimensionMaster;
import com.hcl.atf.taf.model.CompetencyMember;
import com.hcl.atf.taf.model.UserList;

public class JsonCompetencyMember {

	@JsonProperty
	private Integer competencyMemberId;
	@JsonProperty
	private Integer dimensionId;
	@JsonProperty
	private String competencyName;
	@JsonProperty
	private Integer userId;
	@JsonProperty
	private String userName;
	@JsonProperty
	private String startDate;
	@JsonProperty
	private String endDate;
	@JsonProperty
	private Integer status;
	@JsonProperty
	private Integer mappedById;
	@JsonProperty
	private String mappedByName;
	@JsonProperty
	private String mappedDate;
	@JsonProperty
	private Integer modifiedById;
	@JsonProperty
	private String modifiedByName;
	@JsonProperty
	private String modifiedDate;
	
	public JsonCompetencyMember(){
		
	}
	
	public JsonCompetencyMember(CompetencyMember competencyMember) {
		this.competencyMemberId = competencyMember.getCompetencyMemberId();
		if(competencyMember.getDimensionId() != null){
			this.dimensionId = competencyMember.getDimensionId().getDimensionId();
			this.competencyName = competencyMember.getDimensionId().getName();
		}else{
			this.dimensionId = 0;
			this.competencyName = null;
		}
		if(competencyMember.getUserId() != null){
			this.userId = competencyMember.getUserId().getUserId();
			this.userName = competencyMember.getUserId().getUserDisplayName();
		}else{
			this.userId = 0;
			this.userName = null;
		}
		if(competencyMember.getStartDate() != null){
			this.startDate = DateUtility.dateformatWithOutTime(competencyMember.getStartDate());
		}else{
			this.startDate = DateUtility.dateformatWithOutTime(new Date());
		}
		if(competencyMember.getEndDate() != null){
			this.endDate = DateUtility.dateformatWithOutTime(competencyMember.getEndDate());
		}else{
			this.endDate = DateUtility.dateformatWithOutTime(new Date());
		}
		if(competencyMember.getStatus() != null){
			this.status = competencyMember.getStatus();
		}else{
			this.status = 0;
		}
		if(competencyMember.getMappedBy() != null){
			this.mappedById = competencyMember.getMappedBy().getUserId();
			this.mappedByName = competencyMember.getMappedBy().getUserDisplayName();
		}else{
			this.mappedById = 0;
			this.mappedByName = null;
		}
		if(competencyMember.getMappedDate() != null){
			this.mappedDate = DateUtility.dateToStringWithSeconds1(competencyMember.getMappedDate());
		}else{
			this.mappedDate = DateUtility.dateToStringWithSeconds1(new Date());
		}
		if(competencyMember.getModifiedBy() != null){
			this.modifiedById = competencyMember.getModifiedBy().getUserId();
			this.modifiedByName = competencyMember.getModifiedBy().getUserDisplayName();
		}else{
			this.modifiedById = 0;
			this.modifiedByName = null;
		}
		if(competencyMember.getModifiedDate() != null){
			this.modifiedDate = DateUtility.dateToStringWithSeconds1(competencyMember.getModifiedDate());
		}else{
			this.modifiedDate = DateUtility.dateToStringWithSeconds1(new Date());
		}
	}

	
	public Integer getCompetencyMemberId() {
		return competencyMemberId;
	}
	public void setCompetencyMemberId(Integer competencyMemberId) {
		this.competencyMemberId = competencyMemberId;
	}
	public Integer getDimensionId() {
		return dimensionId;
	}
	public void setDimensionId(Integer dimensionId) {
		this.dimensionId = dimensionId;
	}
	public String getCompetencyName() {
		return competencyName;
	}
	public void setCompetencyName(String competencyName) {
		this.competencyName = competencyName;
	}
	public Integer getUserId() {
		return userId;
	}
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getStartDate() {
		return startDate;
	}
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	public String getEndDate() {
		return endDate;
	}
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public Integer getMappedById() {
		return mappedById;
	}
	public void setMappedById(Integer mappedById) {
		this.mappedById = mappedById;
	}
	public String getMappedByName() {
		return mappedByName;
	}
	public void setMappedByName(String mappedByName) {
		this.mappedByName = mappedByName;
	}
	public String getMappedDate() {
		return mappedDate;
	}
	public void setMappedDate(String mappedDate) {
		this.mappedDate = mappedDate;
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
	public CompetencyMember getCompetencyMember() {
		CompetencyMember competencyMember = new CompetencyMember();
		
		competencyMember.setCompetencyMemberId(this.competencyMemberId);
		
		DimensionMaster dimensionMaster = new DimensionMaster();
		if(this.dimensionId != null){
			dimensionMaster.setDimensionId(this.dimensionId);
		}else{
			dimensionMaster.setDimensionId(0);
		}
		competencyMember.setDimensionId(dimensionMaster);
		
		UserList userList = new UserList();
		if(this.userId != null){
			userList.setUserId(this.userId);
		}else{
			userList.setUserId(0);
		}
		competencyMember.setUserId(userList);
		
		if(this.startDate != null && !this.startDate.trim().isEmpty()){
			competencyMember.setStartDate(DateUtility.dateformatWithOutTime(this.startDate));
		}else{
			competencyMember.setStartDate(new Date());
		}
		
		if(this.endDate != null && !this.endDate.trim().isEmpty()){
			competencyMember.setEndDate(DateUtility.dateformatWithOutTime(this.endDate));
		}else{
			competencyMember.setEndDate(new Date());
		}
		
		if(this.status != null){
			competencyMember.setStatus(this.status);
		}else{
			competencyMember.setStatus(0);
		}
		
		UserList mappedUserList = new UserList();
		if(this.mappedById != null){
			mappedUserList.setUserId(this.mappedById);
		}else{
			mappedUserList.setUserId(0);
		}
		competencyMember.setMappedBy(mappedUserList);
		
		if(this.mappedDate != null && !this.mappedDate.trim().isEmpty()){
			competencyMember.setMappedDate(DateUtility.toFormatDate(this.mappedDate));
		}else{
			competencyMember.setMappedDate(new Date());
		}
		
		UserList modifiedUserList = new UserList();
		if(this.modifiedById != null){
			modifiedUserList.setUserId(this.modifiedById);
		}else{
			modifiedUserList.setUserId(0);
		}
		competencyMember.setModifiedBy(modifiedUserList);
		
		if(this.modifiedDate != null && !this.modifiedDate.trim().isEmpty()){
			competencyMember.setModifiedDate(DateUtility.toFormatDate(this.modifiedDate));
		}else{
			competencyMember.setModifiedDate(new Date());
		}
		return competencyMember;
	}
	
	public String toString(){
		return "Selected User Id : "+this.userId+" --- DimensionId : "+dimensionId;
	}
}
