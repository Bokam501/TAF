package com.hcl.atf.taf.model.json;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.hcl.atf.taf.controller.utilities.DateUtility;
import com.hcl.atf.taf.model.Skill;
import com.hcl.atf.taf.model.SkillLevels;
import com.hcl.atf.taf.model.UserList;
import com.hcl.atf.taf.model.UserSkills;

public class JsonUserSkills implements java.io.Serializable {

	private static final Log log = LogFactory.getLog(JsonUserSkills.class);

	private static final long serialVersionUID = 1L;
	@JsonProperty
	private Integer userSkillId;

	// Skill
	@JsonProperty
	private Integer skillId;
	@JsonProperty
	private String skillName;

	@JsonProperty
	private Integer userId;
	@JsonProperty
	private String loginId;
	@JsonProperty
	private Integer approvingManagerId;
	@JsonProperty
	private String approvingManagerName;

	@JsonProperty
	private Integer selfSkillLevelId;
	@JsonProperty
	private String selfSkillLevelName;
	@JsonProperty
	private Integer managerSkillLevelId;
	@JsonProperty
	private String managerSkillLevelName;

	@JsonProperty
	private String fromDate;
	@JsonProperty
	private String toDate;
	@JsonProperty
	private Integer selfIsPrimary;
	@JsonProperty
	private Integer managerIsPrimary;
	@JsonProperty
	private Integer isApproved;
	@JsonProperty
	private String userName;
	@JsonProperty
	private String userCode;
	@JsonProperty
	private String modifiedField;
	@JsonProperty
	private String 	modifiedFieldTitle;
	@JsonProperty
	private String	oldFieldValue;
	@JsonProperty	
	private String modifiedFieldValue;
	
	public JsonUserSkills() {
	}

	public JsonUserSkills(UserSkills userSkills) {

		this.userSkillId = userSkills.getUserSkillId();
		if (userSkills.getIsApproved() != null) {
			this.isApproved = userSkills.getIsApproved();
		} else {
			this.isApproved = 0;
		}

		if (userSkills.getSelfIsPrimary() != null) {
			this.selfIsPrimary = userSkills.getSelfIsPrimary();
		}
		if (userSkills.getManagerIsPrimary() != null) {
			this.managerIsPrimary = userSkills.getManagerIsPrimary();
		}else{
			this.managerIsPrimary = 2;
		}


		Skill skills = userSkills.getSkill();
		if (skills != null) {
			this.skillId = skills.getSkillId();
			if (skills.getDisplayName() != null) {
				this.skillName = skills.getDisplayName();
			} else {
				this.skillName = null;
			}
		} else {
			this.skillId = 0;
			this.skillName = null;
		}
		UserList user = userSkills.getUser();
		if (user != null) {
			this.userId = user.getUserId();
			if (user.getLoginId() != null) {
				this.loginId = user.getLoginId();
			} else {
				this.loginId = null;
			}
		} else {
			this.userId = 0;
			this.loginId = null;
		}
		UserList approvingManager = userSkills.getApprovingManager();
		if (approvingManager != null) {
			this.approvingManagerId = approvingManager.getUserId();
			if (approvingManager.getLoginId() != null) {
				this.approvingManagerName = approvingManager.getLoginId();
			} else {
				this.approvingManagerName = null;
			}
		} else {
			this.approvingManagerId = 0;
			this.approvingManagerName = null;
		}
		SkillLevels selfSkillLevel = userSkills.getSelfSkillLevel();
		if (selfSkillLevel != null) {
			this.selfSkillLevelId = selfSkillLevel.getSkillLevelId();
			if (selfSkillLevel.getLevelName() != null) {
				this.selfSkillLevelName = selfSkillLevel.getLevelName();
			} else {
				this.selfSkillLevelName = null;
			}
		} else {
			this.selfSkillLevelId = 0;
			this.selfSkillLevelName = null;
		}
		SkillLevels managerSkillLevel = userSkills.getManagerSkillLevel();
		if (managerSkillLevel != null) {
			this.managerSkillLevelId = managerSkillLevel.getSkillLevelId();
			if (managerSkillLevel.getLevelName() != null) {
				this.managerSkillLevelName = managerSkillLevel.getLevelName();
			} else {
				this.managerSkillLevelName = null;
			}
		} else {
			this.managerSkillLevelId = 0;
			this.managerSkillLevelName = null;
		}

		if (userSkills.getFromDate() != null) {
			this.fromDate = DateUtility.sdfDateformatWithOutTime(userSkills
					.getFromDate());
		}
		if (userSkills.getToDate() != null) {
			this.toDate = DateUtility.sdfDateformatWithOutTime(userSkills
					.getToDate());
		}
		
		this.userName=userSkills.getUserName();
		this.userCode = userSkills.getUserCode();
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


	@JsonIgnore
	public UserSkills getUserSkills() {
		UserSkills userSkills = new UserSkills();

		userSkills.setUserSkillId(userSkillId);
		if (this.isApproved != null) {
			userSkills.setIsApproved(this.isApproved);
		} else {
			this.isApproved = 0;
			userSkills.setIsApproved(this.isApproved);
		}
		if (this.selfIsPrimary != null) {
			userSkills.setSelfIsPrimary(selfIsPrimary);
		} else {
			userSkills.setSelfIsPrimary(0);
		}

		if (this.managerIsPrimary != null) {
			userSkills.setManagerIsPrimary(managerIsPrimary);
		} else {
			userSkills.setManagerIsPrimary(2);// So no Tick Icon and Manager
												// will take care
		}

		if (this.fromDate == null || this.fromDate.trim().isEmpty()) {
			userSkills.setFromDate(DateUtility.getCurrentTime());
		} else {
			userSkills.setFromDate(DateUtility
					.dateformatWithOutTime(this.fromDate));
		}
		if (this.toDate == null || this.toDate.trim().isEmpty()) {
			userSkills.setToDate(DateUtility.getCurrentTime());
		} else {
			userSkills
					.setToDate(DateUtility.dateformatWithOutTime(this.toDate));
		}

		Skill skill = new Skill();
		skill.setSkillId(skillId);
		userSkills.setSkill(skill);

		UserList user = new UserList();
		user.setUserId(userId);
		userSkills.setUser(user);

		UserList approvingManager = new UserList();
		approvingManager.setUserId(approvingManagerId);
		userSkills.setUser(approvingManager);

		SkillLevels selfSkillLevel = new SkillLevels();
		selfSkillLevel.setSkillLevelId(selfSkillLevelId);

		SkillLevels managerSkillLevel = new SkillLevels();
		managerSkillLevel.setSkillLevelId(managerSkillLevelId);

		userSkills.setUserName(userName);
		userSkills.setUserCode(userCode);
		return userSkills;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public String getLoginId() {
		return loginId;
	}

	public void setLoginId(String loginId) {
		this.loginId = loginId;
	}

	public Integer getApprovingManagerId() {
		return approvingManagerId;
	}

	public void setApprovingManagerId(Integer approvingManagerId) {
		this.approvingManagerId = approvingManagerId;
	}

	public String getApprovingManagerName() {
		return approvingManagerName;
	}

	public void setApprovingManagerName(String approvingManagerName) {
		this.approvingManagerName = approvingManagerName;
	}

	public Integer getSelfSkillLevelId() {
		return selfSkillLevelId;
	}

	public void setSelfSkillLevelId(Integer selfSkillLevelId) {
		this.selfSkillLevelId = selfSkillLevelId;
	}

	public String getSelfSkillLevelName() {
		return selfSkillLevelName;
	}

	public void setSelfSkillLevelName(String selfSkillLevelName) {
		this.selfSkillLevelName = selfSkillLevelName;
	}

	public Integer getManagerSkillLevelId() {
		return managerSkillLevelId;
	}

	public void setManagerSkillLevelId(Integer managerSkillLevelId) {
		this.managerSkillLevelId = managerSkillLevelId;
	}

	public String getManagerSkillLevelName() {
		return managerSkillLevelName;
	}

	public void setManagerSkillLevelName(String managerSkillLevelName) {
		this.managerSkillLevelName = managerSkillLevelName;
	}

	public static boolean isInteger(String s) {
		try {
			Integer.parseInt(s);
		} catch (NumberFormatException e) {
			return false;
		}
		return true;
	}

	public Integer getUserSkillId() {
		return userSkillId;
	}

	public void setUserSkillId(Integer userSkillId) {
		this.userSkillId = userSkillId;
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

	public Integer getSelfIsPrimary() {
		return selfIsPrimary;
	}

	public void setSelfIsPrimary(Integer selfIsPrimary) {
		this.selfIsPrimary = selfIsPrimary;
	}

	public Integer getManagerIsPrimary() {
		return managerIsPrimary;
	}

	public void setManagerIsPrimary(Integer managerIsPrimary) {
		this.managerIsPrimary = managerIsPrimary;
	}

	public Integer getIsApproved() {
		return isApproved;
	}

	public void setIsApproved(Integer isApproved) {
		this.isApproved = isApproved;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserCode() {
		return userCode;
	}

	public void setUserCode(String userCode) {
		this.userCode = userCode;
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
