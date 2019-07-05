package com.hcl.atf.taf.model.json;

import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.hcl.atf.taf.controller.utilities.DateUtility;
import com.hcl.atf.taf.model.ResourceAvailability;
import com.hcl.atf.taf.model.UserSkills;

public class JsonResourceAvailabilityDetails implements java.io.Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@JsonProperty	
	private Integer resourceAvailabilityDetailsId;
	@JsonProperty
	private Integer resourcePoolId;
	@JsonProperty	
	private String resourcePoolName;
	@JsonProperty
	private Integer resourceId;
	@JsonProperty	
	private String resourceName;
	@JsonProperty
	private Integer resourceRoleId;
	@JsonProperty	
	private String resourceRoleName;
	@JsonProperty
	private Integer shiftTypeId;
	@JsonProperty	
	private String shiftTypeName;
	@JsonProperty
	private Integer shiftId;
	@JsonProperty	
	private String shiftName;
	@JsonProperty	
	private String availabilityForDate;
	@JsonProperty
	private StringBuffer skillName;
	
	public JsonResourceAvailabilityDetails(){
		
	}
	
	
	public JsonResourceAvailabilityDetails(ResourceAvailability resourceAvailability){
	
		if (resourceAvailability.getResource() != null){
			this.resourceId = resourceAvailability.getResource().getUserId();
			this.resourceName = resourceAvailability.getResource().getFirstName();
			if(resourceAvailability.getResource().getUserRoleMaster() != null){
				this.resourceRoleId = resourceAvailability.getResource().getUserRoleMaster().getUserRoleId();
				this.resourceRoleName = resourceAvailability.getResource().getUserRoleMaster().getRoleName();
			}
			Set<UserSkills> userSkillsSet = resourceAvailability.getResource().getUserSkills();
			StringBuffer skillarray = new StringBuffer();
			if(userSkillsSet != null && userSkillsSet.size()>0){
				skillarray = getSkillsOfUser(userSkillsSet);
			}
			
			if(skillarray.length() != 0){
				this.skillName = skillarray;
			}else{
				this.skillName = null;
			}
			
		}
		
		if (resourceAvailability.getResource().getResourcePool() != null) {
			this.resourcePoolId = resourceAvailability.getResource().getResourcePool().getResourcePoolId();
			this.resourcePoolName = resourceAvailability.getResource().getResourcePool().getResourcePoolName();
		}
		
		if(resourceAvailability.getWorkShiftMaster() != null){
			this.shiftId = resourceAvailability.getWorkShiftMaster().getShiftId();
			this.shiftName = resourceAvailability.getWorkShiftMaster().getShiftName(); 
		}
		
		if(resourceAvailability.getShiftTypeMaster() != null){
			this.shiftTypeId = resourceAvailability.getShiftTypeMaster().getShiftTypeId();
			this.shiftTypeName = resourceAvailability.getShiftTypeMaster().getShiftName(); 
		}
		
		if(resourceAvailability.getWorkDate() != null){
			this.availabilityForDate = DateUtility.dateformatWithOutTime(resourceAvailability.getWorkDate());
		}
	}
	
	@JsonIgnore
	public ResourceAvailability getResourceAvailabilityDetails() {
		ResourceAvailability resourceAvailability = new ResourceAvailability();
		if (resourceAvailability.getResource() != null){
			this.resourceId = resourceAvailability.getResource().getUserId();
			this.resourceName = resourceAvailability.getResource().getLoginId(); 
			if(resourceAvailability.getResource().getUserRoleMaster() != null){
				this.resourceRoleId = resourceAvailability.getResource().getUserRoleMaster().getUserRoleId();
				this.resourceRoleName = resourceAvailability.getResource().getUserRoleMaster().getRoleName();
			}
			
			Set<UserSkills> userSkillsSet = resourceAvailability.getResource().getUserSkills();
			StringBuffer skillarray = new StringBuffer();
			if(userSkillsSet != null && userSkillsSet.size()>0){
				skillarray = getSkillsOfUser(userSkillsSet);
			}
			
			if(skillarray.length() != 0){
				this.skillName = skillarray;
			}else{
				this.skillName = null;
			}
			//
		}
		
		if (resourceAvailability.getResource().getResourcePool() != null) {
			this.resourcePoolId = resourceAvailability.getResource().getResourcePool().getResourcePoolId();
			this.resourcePoolName = resourceAvailability.getResource().getResourcePool().getResourcePoolName();
		}
		
		if(resourceAvailability.getWorkShiftMaster() != null){
			this.shiftId = resourceAvailability.getWorkShiftMaster().getShiftId();
			this.shiftName = resourceAvailability.getWorkShiftMaster().getShiftName(); 
		}
		
		if(resourceAvailability.getShiftTypeMaster() != null){
			this.shiftTypeId = resourceAvailability.getShiftTypeMaster().getShiftTypeId();
			this.shiftTypeName = resourceAvailability.getShiftTypeMaster().getShiftName(); 
		}
		
		if(this.availabilityForDate == null || this.availabilityForDate.trim().isEmpty()) {
			resourceAvailability.setWorkDate(null);
		}
		else {
			resourceAvailability.setWorkDate(DateUtility.dateFormatWithOutSecondsddMMyyyy(this.availabilityForDate));
		}
		return resourceAvailability;
	}

	public Integer getResourceAvailabilityDetailsId() {
		return resourceAvailabilityDetailsId;
	}
	public void setResourceAvailabilityDetailsId(
			Integer resourceAvailabilityDetailsId) {
		this.resourceAvailabilityDetailsId = resourceAvailabilityDetailsId;
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
	public Integer getResourceId() {
		return resourceId;
	}
	public void setResourceId(Integer resourceId) {
		this.resourceId = resourceId;
	}
	public String getResourceName() {
		return resourceName;
	}
	public void setResourceName(String resourceName) {
		this.resourceName = resourceName;
	}
	public Integer getResourceRoleId() {
		return resourceRoleId;
	}


	public void setResourceRoleId(Integer resourceRoleId) {
		this.resourceRoleId = resourceRoleId;
	}


	public String getResourceRoleName() {
		return resourceRoleName;
	}


	public void setResourceRoleName(String resourceRoleName) {
		this.resourceRoleName = resourceRoleName;
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
	public String getAvailabilityForDate() {
		return availabilityForDate;
	}
	public void setAvailabilityForDate(String availabilityForDate) {
		this.availabilityForDate = availabilityForDate;
	}
	
	public Integer getShiftTypeId() {
		return shiftTypeId;
	}

	public void setShiftTypeId(Integer shiftTypeId) {
		this.shiftTypeId = shiftTypeId;
	}

	public String getShiftTypeName() {
		return shiftTypeName;
	}

	public void setShiftTypeName(String shiftTypeName) {
		this.shiftTypeName = shiftTypeName;
	}

	public StringBuffer getSkillName() {
		return skillName;
	}

	public void setSkillName(StringBuffer skillName) {
		this.skillName = skillName;
	}
	
	public StringBuffer getSkillsOfUser(Set<UserSkills> userSkillsSet){
		StringBuffer skillarray = null;
		if(userSkillsSet != null && userSkillsSet.size()>0){
			skillarray = new StringBuffer();
			for (UserSkills userSkillsets : userSkillsSet) {
				int user_id = userSkillsets.getUser().getUserId();
				if(user_id == this.resourceId){
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
}
