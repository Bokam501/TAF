package com.hcl.atf.taf.dao;

import java.util.List;

import com.hcl.atf.taf.model.SkillLevels;

public interface SkillLevelsDAO  {	
	List<SkillLevels> list(int status);	
	List<SkillLevels> listSkillLevelsforUsers(int status, int roleId, String isApprover);
	SkillLevels getByskillLevelId(int skillLevelId);	
	
	void add(SkillLevels skillLevels);
	void update(SkillLevels skillLevels);	
	SkillLevels getSkillLevelsbyLevleName(String levelName);
		
	
}
	
