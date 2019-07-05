package com.hcl.atf.taf.dao;

import java.util.List;

import com.hcl.atf.taf.model.Skill;
import com.hcl.atf.taf.model.UserSkills;

public interface UserSkillsDAO  {	
	List<UserSkills> list(int status);
	List<UserSkills> listByUserId(int status, int userId, int isApproved, int selfIsPrimary, int managerIsPrimary);
	List<UserSkills> listUserSkillsToBeApproved(int status, int userId, int isApproved, int selfIsPrimary, int managerIsPrimary);
		
	boolean isUserSkillExistingBySkillName(String UserSkills_SkillName, int CurrentUser_userId);		
	UserSkills getUserSkillsBySkillName(String UserSkills_SkillName, int CurrentUser_userId);
	
	UserSkills getByUserSkillId(int userSkillId);	
	
	void add(UserSkills userSkills);
	void update(UserSkills userSkills);
	public int userSkillBulk(List<UserSkills> userSkillList,int batchSize);
}
