package com.hcl.atf.taf.service;

import java.util.List;

import com.hcl.atf.taf.model.Skill;
import com.hcl.atf.taf.model.SkillLevels;
import com.hcl.atf.taf.model.UserSkills;

public interface SkillService {
	
	List<Skill> listSkill(int status);
	List<Skill> listnoRoot(int status);
	boolean isSkillExistingByName(String skillName);
	Skill getSkillByName(String skillName);
	Skill getBySkillId(int skillId);
	
	void addSkill(Skill skill);
	Skill update(Skill skill);
	//For UserSkills
	List<UserSkills> listUserSkills(int status);
	List<UserSkills> listUserSkillsByUserId(int status, int userId,  int isApproved, int selfIsPrimary, int managerIsPrimary);
	List<UserSkills> listUserSkillsToBeApproved(int status, int userId, int isApproved, int selfIsPrimary, int managerIsPrimary);
	
	boolean isUserSkillExistingBySkillName(String UserSkills_SkillName, int CurrentUser_userId);
	UserSkills getUserSkillBySkillName(String UserSkills_SkillName, int CurrentUser_userId);
	 
	UserSkills getByUserSkillId(int userSkillId);
	void addUserSkill(UserSkills userSkills);
	UserSkills updateUserSkill(UserSkills userSkills);
	UserSkills updateUserSkillInline(int userId, int userSkillId, String modifiedField, String modifiedFieldValue);
	UserSkills updateUserSkillApprovalInline(int approverId, int userSkillId, String modifiedField, String modifiedFieldValue);
	//For SkillLevels
	List<SkillLevels> listSkillLevels(int status);
	List<SkillLevels> listSkillLevelsforUsers(int status, int roleId, String isApprover);
	SkillLevels getByskillLevelId(int skillLevelId);
	void addSkillLevels(SkillLevels skillLevels);
	SkillLevels updateSkillLevels(SkillLevels skillLevels);
	SkillLevels getSkillLevelsbyLevleName(String levelName);
	Skill getRootSkill();
	List<Skill> listChildNodesInHierarchyinLayers(Skill skill);
}
