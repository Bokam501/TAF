package com.hcl.atf.taf.model.json;

import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.hcl.atf.taf.controller.utilities.DateUtility;
import com.hcl.atf.taf.model.TestFactoryResourceReservation;
import com.hcl.atf.taf.model.UserSkills;

public class JsonTestFactoryResourceReservation implements java.io.Serializable{
	private static final Log log = LogFactory
			.getLog(JsonTestFactoryResourceReservation.class);
	@JsonProperty
	private Integer testFactoryResourceReservationId;
	@JsonProperty
	private Integer workPackageId;
	@JsonProperty
	private String  workPackageName;
	@JsonProperty
	private Integer shiftId;
	@JsonProperty
	private String  shiftName;
	@JsonProperty
	private Integer blockedUserId;
	@JsonProperty
	private String  blockedUserName;
	@JsonProperty
	private Integer blockedUserRoleId;
	@JsonProperty
	private String  blockedUserRoleName;
	@JsonProperty
	private Integer reservationActionUserId;
	@JsonProperty
	private String  reservationActionUserName;
	@JsonProperty
	private String reservationDate;
	@JsonProperty
	private String reservationActionDate;
	@JsonProperty
	private String blockedUserLogIn;
	@JsonProperty
	private Integer productId;
	@JsonProperty
	private String  productName;
	@JsonProperty
	private StringBuffer skillName;
	@JsonProperty
	private int skillNameCharacterlength;
	@JsonProperty
	private  Long bookedHrs;
	@JsonProperty
	private  String timeSheetHours;
	@JsonProperty
	private Integer reservationPercentage;
	@JsonProperty
	private Integer reservationWeek;
	@JsonProperty
	private Integer reservationYear;
	@JsonProperty
	private String reservationMode;
	@JsonProperty
	private Long groupReservationId;
	
	@JsonProperty
	private Integer userTypeId;
	@JsonProperty
	private String  userTypeName;
	
	@JsonProperty
	private Integer resourcePoolId;
	
	public String getBlockedUserLogIn() {
		return blockedUserLogIn;
	}

	public void setBlockedUserLogIn(String blockedUserLogIn) {
		this.blockedUserLogIn = blockedUserLogIn;
	}

	public JsonTestFactoryResourceReservation(){
		
	}
	
	public JsonTestFactoryResourceReservation(TestFactoryResourceReservation resourceReservation){
		this.testFactoryResourceReservationId = resourceReservation.getResourceReservationId();
		
	
		if (resourceReservation.getWorkPackage() != null) {
			workPackageId = resourceReservation.getWorkPackage().getWorkPackageId();
			workPackageName = resourceReservation.getWorkPackage().getName(); 
		}
		
		
		if (resourceReservation.getShift() != null) {
			shiftId = resourceReservation.getShift().getShiftId();
			shiftName = resourceReservation.getShift().getShiftName(); 
		}
		
		if (resourceReservation.getUserType() != null) {
			userTypeId = resourceReservation.getUserType().getUserTypeId();
			userTypeName = resourceReservation.getUserType().getUserTypeLabel(); 
		}
		
		if (resourceReservation.getBlockedUser() != null) {
			blockedUserId = resourceReservation.getBlockedUser().getUserId();
			blockedUserLogIn = resourceReservation.getBlockedUser().getLoginId();
			blockedUserName = resourceReservation.getBlockedUser().getUserDisplayName(); 
		}
		
		if (resourceReservation.getBlockedUser().getUserRoleMaster() != null) {
			blockedUserRoleId = resourceReservation.getBlockedUser().getUserRoleMaster().getUserRoleId();
			blockedUserRoleName = resourceReservation.getBlockedUser().getUserRoleMaster().getRoleName();
		}
		
		if (resourceReservation.getReservationActionUser() != null) {
			reservationActionUserId = resourceReservation.getReservationActionUser().getUserId();
			reservationActionUserName = resourceReservation.getReservationActionUser().getUserDisplayName(); 
		}
		
		if(resourceReservation.getResourcePool()!= null){
			resourcePoolId = resourceReservation.getResourcePool().getResourcePoolId();
		}
		if(resourceReservation.getReservationDate() != null){
			this.reservationDate = DateUtility.dateformatWithOutTime(resourceReservation.getReservationDate());
		}
		
		if(resourceReservation.getReservationActionDate() != null){
			this.reservationActionDate = DateUtility.dateToStringInSecond(resourceReservation.getReservationActionDate());
		}
		if(resourceReservation.getWorkPackage().getProductBuild().getProductVersion().getProductMaster()!=null){
			this.productId=resourceReservation.getWorkPackage().getProductBuild().getProductVersion().getProductMaster().getProductId();
			this.productName=resourceReservation.getWorkPackage().getProductBuild().getProductVersion().getProductMaster().getProductName();
		}
		
		StringBuffer skillarray = new StringBuffer();
		Set<UserSkills> userSkillsSet = resourceReservation.getBlockedUser().getUserSkills();
		int blocked_UserId = resourceReservation.getBlockedUser().getUserId();
		if(userSkillsSet != null && userSkillsSet.size()>0){
			skillarray= getSkillsOfUsers(userSkillsSet, blocked_UserId);			
		}
		
		if(skillarray.length() != 0){
			this.skillName = skillarray;
			this.skillNameCharacterlength = skillarray.length();
		}else{
			this.skillName = null;
			this.skillNameCharacterlength = 0;
		}
		this.bookedHrs = new Long(40);
		this.timeSheetHours = new String("40");
		log.info("Skill Name "+skillName);
		
		
		
	}
	
	@JsonIgnore
	public TestFactoryResourceReservation getTestFactoryResourceReservation() {
		TestFactoryResourceReservation resourceReservation = new TestFactoryResourceReservation();
		if (this.testFactoryResourceReservationId != null) {
			resourceReservation.setResourceReservationId(this.testFactoryResourceReservationId);
		}
		
		if(resourceReservation.getWorkPackage() != null){
			this.workPackageId = resourceReservation.getWorkPackage().getWorkPackageId();
			this.workPackageName = resourceReservation.getWorkPackage().getName();
		}
		
		if(resourceReservation.getShift() != null){
			this.shiftId = resourceReservation.getShift().getShiftId();
			this.shiftName = resourceReservation.getShift().getShiftName();
		}
		

		if(resourceReservation.getBlockedUser() != null){
			this.blockedUserId = resourceReservation.getBlockedUser().getUserId();
			this.blockedUserName = resourceReservation.getBlockedUser().getUserDisplayName();
		}
		
		if(resourceReservation.getReservationActionUser() != null){
			this.reservationActionUserId = resourceReservation.getReservationActionUser().getUserId();
			this.reservationActionUserName = resourceReservation.getReservationActionUser().getUserDisplayName();
		}
		
		if(this.reservationDate == null || this.reservationDate.trim().isEmpty()) {
			resourceReservation.setReservationDate(null);
		}
		else {
			resourceReservation.setReservationDate(DateUtility.dateFormatWithOutSecondsddMMyyyy(this.reservationDate));
		}
		
		if(this.reservationActionDate == null || this.reservationActionDate.trim().isEmpty()) {
			resourceReservation.setReservationActionDate(null);
		}
		else {
			resourceReservation.setReservationActionDate(DateUtility.dateFormatWithOutSecondsddMMyyyy(this.reservationActionDate));
		}
		
		
		
		return resourceReservation;
	}
	
	
	public Integer getTestFactoryResourceReservationId() {
		return testFactoryResourceReservationId;
	}
	public void setTestFactoryResourceReservationId(
			Integer testFactoryResourceReservationId) {
		this.testFactoryResourceReservationId = testFactoryResourceReservationId;
	}
	public Integer getWorkPackageId() {
		return workPackageId;
	}
	public void setWorkPackageId(Integer workPackageId) {
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
	public Integer getBlockedUserId() {
		return blockedUserId;
	}
	public void setBlockedUserId(Integer blockedUserId) {
		this.blockedUserId = blockedUserId;
	}
	public String getBlockedUserName() {
		return blockedUserName;
	}
	public void setBlockedUserName(String blockedUserName) {
		this.blockedUserName = blockedUserName;
	}
	public Integer getBlockedUserRoleId() {
		return blockedUserRoleId;
	}

	public void setBlockedUserRoleId(Integer blockedUserRoleId) {
		this.blockedUserRoleId = blockedUserRoleId;
	}

	public String getBlockedUserRoleName() {
		return blockedUserRoleName;
	}

	public void setBlockedUserRoleName(String blockedUserRoleName) {
		this.blockedUserRoleName = blockedUserRoleName;
	}

	public Integer getReservationActionUserId() {
		return reservationActionUserId;
	}
	public void setReservationActionUserId(Integer reservationActionUserId) {
		this.reservationActionUserId = reservationActionUserId;
	}
	public String getReservationActionUserName() {
		return reservationActionUserName;
	}
	public void setReservationActionUserName(String reservationActionUserName) {
		this.reservationActionUserName = reservationActionUserName;
	}
	public String getReservationDate() {
		return reservationDate;
	}
	public void setReservationDate(String reservationDate) {
		this.reservationDate = reservationDate;
	}
	public String getReservationActionDate() {
		return reservationActionDate;
	}
	public void setReservationActionDate(String reservationActionDate) {
		this.reservationActionDate = reservationActionDate;
	}

	public Integer getProductId() {
		return productId;
	}

	public void setProductId(Integer productId) {
		this.productId = productId;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public StringBuffer getSkillName() {
		return skillName;
	}

	public void setSkillName(StringBuffer skillName) {
		this.skillName = skillName;
	}
	
	public StringBuffer getSkillsOfUsers(Set<UserSkills> userSkillsSet, int blocked_UserId){
		StringBuffer skillarray = new StringBuffer();
		if(userSkillsSet != null && userSkillsSet.size()>0){
			for (UserSkills userSkillsets : userSkillsSet) {
				int user_id = userSkillsets.getUser().getUserId();
				if(user_id == blocked_UserId){
					int isPrimary = userSkillsets.getSelfIsPrimary();
					if(isPrimary == 1){
						if(skillarray.length() == 0){
							skillarray.append(userSkillsets.getSkill().getDisplayName());
						}else if(skillarray.length() >0){
							skillarray.append(",");
							skillarray.append(userSkillsets.getSkill().getDisplayName());
						}
					}					
				}				
			}
		}
		return skillarray;
	}

	public int getSkillName_Characterlength() {
		return skillNameCharacterlength;
	}

	public void setSkillName_Characterlength(int skillName_Characterlength) {
		this.skillNameCharacterlength = skillName_Characterlength;
	}

	public Long getBookedHrs() {
		return bookedHrs;
	}

	public void setBookedHrs(Long bookedHrs) {
		this.bookedHrs = bookedHrs;
	}

	public String getTimeSheetHours() {
		return timeSheetHours;
	}

	public void setTimeSheetHours(String timeSheetHours) {
		this.timeSheetHours = timeSheetHours;
	}

	public Integer getReservationPercentage() {
		return reservationPercentage;
	}

	public void setReservationPercentage(Integer reservationPercentage) {
		this.reservationPercentage = reservationPercentage;
	}

	public Integer getReservationWeek() {
		return reservationWeek;
	}

	public void setReservationWeek(Integer reservationWeek) {
		this.reservationWeek = reservationWeek;
	}

	public Integer getReservationYear() {
		return reservationYear;
	}

	public void setReservationYear(Integer reservationYear) {
		this.reservationYear = reservationYear;
	}

	public String getReservationMode() {
		return reservationMode;
	}

	public void setReservationMode(String reservationMode) {
		this.reservationMode = reservationMode;
	}

	public Long getGroupReservationId() {
		return groupReservationId;
	}

	public void setGroupReservationId(Long groupReservationId) {
		this.groupReservationId = groupReservationId;
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

	public Integer getResourcePoolId() {
		return resourcePoolId;
	}

	public void setResourcePoolId(Integer resourcePoolId) {
		this.resourcePoolId = resourcePoolId;
	}
	
	
	
}
