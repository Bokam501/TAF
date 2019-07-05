package com.hcl.atf.taf.model.json;

import java.util.Comparator;
import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.hcl.atf.taf.controller.utilities.DateUtility;
import com.hcl.atf.taf.model.Skill;
import com.hcl.atf.taf.model.UserRoleMaster;
import com.hcl.atf.taf.model.UserTypeMasterNew;
import com.hcl.atf.taf.model.WorkPackage;
import com.hcl.atf.taf.model.WorkPackageDemandProjection;


public class JsonWorkPackageDemandProjection implements java.io.Serializable{
	
	private static final Log log = LogFactory.getLog(JsonWorkPackageDemandProjection.class);
	
	@JsonProperty	
	private Integer wpDemandProjectionId;
	@JsonProperty	
	private Float resourceCount;
	
	@JsonProperty	
	private String workDate;
	@JsonProperty
	private int workPackageId;
	@JsonProperty	
	private String workPackageName;
	@JsonProperty
	private Integer shiftId;
	@JsonProperty	
	private String shiftName;
	@JsonProperty	
	private String weekStartDate;
	@JsonProperty	
	private Integer weekNo;
	@JsonProperty
	private Integer skillId;
	@JsonProperty	
	private String skillName;
	@JsonProperty
	private Integer userRoleId;
	@JsonProperty	
	private String userRoleName;
	
	@JsonProperty	
	private Float day1ResourceCount;
	@JsonProperty	
	private Float day2ResourceCount;
	@JsonProperty	
	private Float day3ResourceCount;
	@JsonProperty	
	private Float day4ResourceCount;
	@JsonProperty	
	private Float day5ResourceCount;
	@JsonProperty	
	private Float day6ResourceCount;
	@JsonProperty	
	private Float day7ResourceCount;
	
	@JsonProperty	
	private Float skillandRoleBasedresourceCount;
	
	@JsonProperty
	private Integer demandRaisedByUserId;
	@JsonProperty	
	private String demandRaisedByUserName;
	@JsonProperty	
	private String demandRaisedOnDate;
	@JsonProperty	
	private Integer workWeek;
	@JsonProperty	
	private Integer workYear;
	
	@JsonProperty	
	private String demandMode;
	
	@JsonProperty	
	private  Long groupDemandId;
	@JsonProperty	
	private String recursiveWeeks;
	@JsonProperty
	private Integer userTypeId;
	@JsonProperty	
	private String userTypeName;
	@JsonProperty
	private String modifiedField;
	@JsonProperty
	private String 	modifiedFieldTitle;
	@JsonProperty
	private String	oldFieldValue;
	@JsonProperty	
	private String modifiedFieldValue;
	
	public JsonWorkPackageDemandProjection() {
		this.day1ResourceCount = new Float(0);
		this.day2ResourceCount = new Float(0);
		this.day3ResourceCount = new Float(0);
		this.day4ResourceCount = new Float(0);
		this.day5ResourceCount = new Float(0);
		this.day6ResourceCount = new Float(0);
		this.day7ResourceCount = new Float(0);
	}	

	public JsonWorkPackageDemandProjection(WorkPackageDemandProjection workPackageDemandProjection) {
		this.wpDemandProjectionId=workPackageDemandProjection.getWpDemandProjectionId();
		if(workPackageDemandProjection.getUserTypeMasterNew()!=null){
			this.userTypeId = workPackageDemandProjection.getUserTypeMasterNew().getUserTypeId();
			this.userTypeName = workPackageDemandProjection.getUserTypeMasterNew().getUserTypeLabel();
		}
		this.workPackageId=workPackageDemandProjection.getWorkPackage().getWorkPackageId();
		this.workPackageName=workPackageDemandProjection.getWorkPackage().getName();
		this.shiftId=workPackageDemandProjection.getWorkShiftMaster().getShiftId();
		this.shiftName=workPackageDemandProjection.getWorkShiftMaster().getShiftName();
		this.skillId = workPackageDemandProjection.getSkill().getSkillId();
		this.skillName = workPackageDemandProjection.getSkill().getSkillName();
		this.userRoleId = workPackageDemandProjection.getUserRole().getUserRoleId();
		this.userRoleName = workPackageDemandProjection.getUserRole().getRoleName();
		if(workPackageDemandProjection.getWorkDate() != null){
			this.workDate = DateUtility.sdfDateformatWithOutTime(workPackageDemandProjection.getWorkDate());
		}
		
		this.resourceCount = workPackageDemandProjection.getResourceCount();
		this.skillandRoleBasedresourceCount = workPackageDemandProjection.getResourceCount();
		if(workPackageDemandProjection.getWorkWeek() != null ){
			this.workWeek = workPackageDemandProjection.getWorkWeek() ;
		}
		if(workPackageDemandProjection.getWorkYear() != null){
			this.workYear = workPackageDemandProjection.getWorkYear() ;
		}
		
		if(workPackageDemandProjection.getDemandMode() != null){
			this.demandMode = workPackageDemandProjection.getDemandMode();
		}
		if( workPackageDemandProjection.getDemandRaisedByUser()!=null){
			this.demandRaisedByUserId = workPackageDemandProjection.getDemandRaisedByUser().getUserId();
		this.demandRaisedByUserName = workPackageDemandProjection.getDemandRaisedByUser().getLoginId();
		}
		if(workPackageDemandProjection.getGroupDemandId() !=null){
			this.groupDemandId = workPackageDemandProjection.getGroupDemandId();
		}
		
		this.day1ResourceCount = new Float(0);
		this.day2ResourceCount = new Float(0);
		this.day3ResourceCount = new Float(0);
		this.day4ResourceCount = new Float(0);
		this.day5ResourceCount = new Float(0);
		this.day6ResourceCount = new Float(0);
		this.day7ResourceCount = new Float(0);
		if(workPackageDemandProjection.getDemandRaisedOn() != null){
			this.demandRaisedOnDate = DateUtility.dateToStringWithSeconds1(workPackageDemandProjection.getDemandRaisedOn());
		}
	}
	
	@JsonIgnore
	public WorkPackageDemandProjection workPackageDemandProjection() {
	
		WorkPackageDemandProjection workPackageDemandProjection = new WorkPackageDemandProjection();
		workPackageDemandProjection.setWpDemandProjectionId(this.wpDemandProjectionId);
		
		WorkPackage workPackage = new WorkPackage();
		workPackage.setWorkPackageId(this.workPackageId);
		workPackageDemandProjection.setWorkPackage(workPackage);
		
		Skill skill = new Skill();
		skill.setSkillId(this.skillId);
		workPackageDemandProjection.setSkill(skill);
		
		UserRoleMaster userRole = new UserRoleMaster();
		userRole.setUserRoleId(this.userRoleId);
		workPackageDemandProjection.setUserRole(userRole);
		
		
		UserTypeMasterNew userType =new UserTypeMasterNew();
		userType.setUserTypeId(this.userTypeId);
		userType.setUserTypeLabel(this.userTypeName);
		workPackageDemandProjection.setUserTypeMasterNew(userType);
		
		if(this.workDate == null || this.workDate.trim().isEmpty()) {
			workPackageDemandProjection.setWorkDate(new Date(System.currentTimeMillis()));
		} else {
			workPackageDemandProjection.setWorkDate(DateUtility.dateformatWithOutTime(this.workDate));
		}
		
		if(this.demandRaisedOnDate == null || this.demandRaisedOnDate.trim().isEmpty()) {
			workPackageDemandProjection.setDemandRaisedOn(new Date(System.currentTimeMillis()));
		} else {
			workPackageDemandProjection.setDemandRaisedOn(DateUtility.toDateInSec1(this.demandRaisedOnDate));
		}
		
		if(this.workWeek != null ){
			workPackageDemandProjection.setWorkWeek(this.workWeek);
		}
		if(this.workYear != null ){
			workPackageDemandProjection.setWorkYear(this.workYear);
		}
		
		if(this.demandMode != null){
			workPackageDemandProjection.setDemandMode(this.demandMode);
		}
		
		if(this.groupDemandId != null){
			workPackageDemandProjection.setGroupDemandId(this.groupDemandId);
		}
		return workPackageDemandProjection;
	}
	
	public Integer getWpDemandProjectionId() {
		return wpDemandProjectionId;
	}
	public void setWpDemandProjectionId(Integer wpDemandProjectionId) {
		this.wpDemandProjectionId = wpDemandProjectionId;
	}
	public Float getResourceCount() {
		return resourceCount;
	}
	public void setResourceCount(Float resourceCount) {
		this.resourceCount = resourceCount;
	}
	public String getWorkDate() {
		return workDate;
	}
	public void setWorkDate(String workDate) {
		this.workDate = workDate;
	}
	public int getWorkPackageId() {
		return workPackageId;
	}
	public void setWorkPackageId(int workPackageId) {
		this.workPackageId = workPackageId;
	}
	public String getWorkPackageName() {
		return workPackageName;
	}
	public void setWorkPackageName(String workPackageName) {
		this.workPackageName = workPackageName;
	}
	public Integer getShiftId() {
		return shiftId;
	}
	public void setShiftId(Integer shiftId) {
		this.shiftId = shiftId;
	}
	public String getShiftName() {
		return shiftName;
	}
	public void setShiftName(String shiftName) {
		this.shiftName = shiftName;
	}
	public String getWeekStartDate() {
		return weekStartDate;
	}
	public void setWeekStartDate(String weekStartDate) {
		this.weekStartDate = weekStartDate;
	}
	public Integer getWeekNo() {
		return weekNo;
	}
	public void setWeekNo(Integer weekNo) {
		this.weekNo = weekNo;
	}
	public Integer getSkillId() {
		return skillId;
	}
	public void setSkillId(Integer skillId) {
		this.skillId = skillId;
	}
	public String getSkillName() {
		return skillName;
	}
	public void setSkillName(String skillName) {
		this.skillName = skillName;
	}
	public Integer getUserRoleId() {
		return userRoleId;
	}
	public void setUserRoleId(Integer userRoleId) {
		this.userRoleId = userRoleId;
	}
	public String getUserRoleName() {
		return userRoleName;
	}
	public void setUserRoleName(String userRoleName) {
		this.userRoleName = userRoleName;
	}
	public Float getDay1ResourceCount() {
		return day1ResourceCount;
	}
	public void setDay1ResourceCount(Float day1ResourceCount) {
		this.day1ResourceCount = day1ResourceCount;
	}
	public Float getDay2ResourceCount() {
		return day2ResourceCount;
	}
	public void setDay2ResourceCount(Float day2ResourceCount) {
		this.day2ResourceCount = day2ResourceCount;
	}
	public Float getDay3ResourceCount() {
		return day3ResourceCount;
	}
	public void setDay3ResourceCount(Float day3ResourceCount) {
		this.day3ResourceCount = day3ResourceCount;
	}
	public Float getDay4ResourceCount() {
		return day4ResourceCount;
	}
	public void setDay4ResourceCount(Float day4ResourceCount) {
		this.day4ResourceCount = day4ResourceCount;
	}
	public Float getDay5ResourceCount() {
		return day5ResourceCount;
	}
	public void setDay5ResourceCount(Float day5ResourceCount) {
		this.day5ResourceCount = day5ResourceCount;
	}
	public Float getDay6ResourceCount() {
		return day6ResourceCount;
	}
	public void setDay6ResourceCount(Float day6ResourceCount) {
		this.day6ResourceCount = day6ResourceCount;
	}
	public Float getDay7ResourceCount() {
		return day7ResourceCount;
	}
	public void setDay7ResourceCount(Float day7ResourceCount) {
		this.day7ResourceCount = day7ResourceCount;
	}
	public Float getSkillandRoleBasedresourceCount() {
		return skillandRoleBasedresourceCount;
	}
	public void setSkillandRoleBasedresourceCount(
			Float skillandRoleBasedresourceCount) {
		this.skillandRoleBasedresourceCount = skillandRoleBasedresourceCount;
	}
	public Integer getDemandRaisedByUserId() {
		return demandRaisedByUserId;
	}
	public void setDemandRaisedByUserId(Integer demandRaisedByUserId) {
		this.demandRaisedByUserId = demandRaisedByUserId;
	}
	public String getDemandRaisedByUserName() {
		return demandRaisedByUserName;
	}
	public void setDemandRaisedByUserName(String demandRaisedByUserName) {
		this.demandRaisedByUserName = demandRaisedByUserName;
	}
	public String getDemandRaisedOnDate() {
		return demandRaisedOnDate;
	}
	public void setDemandRaisedOnDate(String demandRaisedOnDate) {
		this.demandRaisedOnDate = demandRaisedOnDate;
	}
	
	
	
	public static Comparator<JsonWorkPackageDemandProjection> jsonWorkPackageDemandComparator = new Comparator<JsonWorkPackageDemandProjection>() {

		public int compare(JsonWorkPackageDemandProjection shift1, JsonWorkPackageDemandProjection shift2) {
			String shift1Id = shift1.getShiftName().toUpperCase();
			String shift2Id = shift2.getShiftName().toUpperCase();

			return shift1Id.compareTo(shift2Id);
		}
	};

	public Integer getWorkWeek() {
		return workWeek;
	}

	public void setWorkWeek(Integer workWeek) {
		this.workWeek = workWeek;
	}

	public Integer getWorkYear() {
		return workYear;
	}

	public void setWorkYear(Integer workYear) {
		this.workYear = workYear;
	}

	public String getDemandMode() {
		return demandMode;
	}

	public void setDemandMode(String demandMode) {
		this.demandMode = demandMode;
	}

	public Long getGroupDemandId() {
		return groupDemandId;
	}

	public void setGroupDemandId(Long groupDemandId) {
		this.groupDemandId = groupDemandId;
	}

	public String getRecursiveWeeks() {
		return recursiveWeeks;
	}

	public void setRecursiveWeeks(String recursiveWeeks) {
		this.recursiveWeeks = recursiveWeeks;
	}

	public Integer getUserTypeId() {
		return userTypeId;
	}

	public void setUserTypeId(Integer userTypeId) {
		this.userTypeId = userTypeId;
	}

	public String getUserTypeName() {
		return userTypeName;
	}

	public void setUserTypeName(String userTypeName) {
		this.userTypeName = userTypeName;
	}

	public String getModifiedField() {
		return modifiedField;
	}

	public void setModifiedField(String modifiedField) {
		this.modifiedField = modifiedField;
	}

	public String getModifiedFieldTitle() {
		return modifiedFieldTitle;
	}

	public void setModifiedFieldTitle(String modifiedFieldTitle) {
		this.modifiedFieldTitle = modifiedFieldTitle;
	}

	public String getOldFieldValue() {
		return oldFieldValue;
	}

	public void setOldFieldValue(String oldFieldValue) {
		this.oldFieldValue = oldFieldValue;
	}

	public String getModifiedFieldValue() {
		return modifiedFieldValue;
	}

	public void setModifiedFieldValue(String modifiedFieldValue) {
		this.modifiedFieldValue = modifiedFieldValue;
	}
	
	
	
}
