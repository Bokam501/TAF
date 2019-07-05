package com.hcl.atf.taf.dao;

import java.util.List;

import com.hcl.atf.taf.model.Skill;
import com.hcl.atf.taf.model.dto.SkillDTO;

public interface SkillDAO  {	
	List<Skill> list(int status);
	List<Skill> listnoRoot(int status);
	boolean isSkillExistingByName(String skillName);
	Skill getSkillByName(String skillName);
	Skill getBySkillId(int skillId);
	
	Skill incrementChildIndices(int skillId, int incrementingRightIndex, int incrementingLeftIndex);
	List<Skill> getSkillstoIncrementRightIndicesofAncestors(int currentSkillRightIndex, int currentSkillLeftIndex);
	List<Skill> getSkillstoIncrementLeftnRighttIndices(int currentSkillRightIndex, int currentSkillLeftIndex);
	void add(Skill skill);
	void update(Skill skill);
	void delete(Skill skill);
	
	SkillDTO getSkillwithMaxRightIndex();
	SkillDTO getSkillwithMinRightIndex();
	SkillDTO getSkillwithMaxLeftIndex();
	SkillDTO getSkillwithMinLeftIndex();
	
	List<SkillDTO> getDescendantSkills(int parentRightIndex, int ParentLeftIndex);
	List<SkillDTO> getAscendantSkills(int parentRightIndex, int ParentLeftIndex);
	Integer getRootUserSkillId(String rootUserSkillsDescription);
	List<Skill> getSkillListByRootId(
			Integer jtStartIndex, Integer jtPageSize, Integer rootSkillId, boolean isInitializationReq);
	List<Skill> getChildSkillListByParentSkillId(Integer parentSkillId);
	List<Skill> listChildNodesInHierarchyinLayers(Skill skill);
	Skill getRootSkill();
}
