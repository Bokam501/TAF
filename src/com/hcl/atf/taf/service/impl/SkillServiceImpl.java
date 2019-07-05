package com.hcl.atf.taf.service.impl;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hcl.atf.taf.dao.CustomerDAO;
import com.hcl.atf.taf.dao.SkillDAO;
import com.hcl.atf.taf.dao.SkillLevelsDAO;
import com.hcl.atf.taf.dao.UserListDAO;
import com.hcl.atf.taf.dao.UserSkillsDAO;
import com.hcl.atf.taf.model.Skill;
import com.hcl.atf.taf.model.SkillLevels;
import com.hcl.atf.taf.model.UserSkills;
import com.hcl.atf.taf.service.SkillService;

@Service
public class SkillServiceImpl implements SkillService {
	private static final Log log = LogFactory.getLog(SkillServiceImpl.class);
	
	@Autowired
	private SkillDAO skillDAO;
	
	@Autowired
	private CustomerDAO customerDAO;
	
	@Autowired
	private UserSkillsDAO userSkillsDAO;
	
	@Autowired
	private SkillLevelsDAO skillLevelsDAO;

	@Autowired
	private UserListDAO userListDAO;
	@Override
	@Transactional
	public List<Skill> listSkill(int status) {
		return skillDAO.list(status);
	}
	
	@Override
	@Transactional
	public List<Skill> listnoRoot(int status) {
		return skillDAO.listnoRoot(status);
	}
	
	@Override
	@Transactional
	public boolean isSkillExistingByName(String skillName) {
		return skillDAO.isSkillExistingByName(skillName);
	}
	
	@Override
	@Transactional
	public Skill getSkillByName(String skillName) {
		return skillDAO.getSkillByName(skillName);
	}	
	
	@Override
	@Transactional
	public Skill getBySkillId(int skillId) {
		return skillDAO.getBySkillId(skillId);
	}
	
	@Override
	@Transactional
	public void addSkill(Skill skill) {	
		if(skill.getParentSkill() !=null && skill.getParentSkill().getSkillId() != null){
			Skill parentSkill = skillDAO.getBySkillId(skill.getParentSkill().getSkillId());
			skill.setParentSkill(parentSkill);
			//Create display name from parent 
			StringBuffer displayName = new StringBuffer();
			displayName.append(skill.getSkillName());
			boolean hasParent = true;
			while (hasParent) {
				if (parentSkill.getParentSkill() != null)
				displayName.insert(0, parentSkill.getSkillName() + " | ");
				parentSkill = parentSkill.getParentSkill();
				if (parentSkill == null)
					hasParent = false;
			}
			skill.setDisplayName(displayName.toString());
		}else{
			skill.setParentSkill(null);			
			skill.setDisplayName(skill.getSkillName());			
		}
		skillDAO.add(skill);		
	}
	
	
	@Override
	public Skill update(Skill skill) {		
		if(skill.getParentSkill() !=null && skill.getParentSkill().getSkillId() !=null && skill.getParentSkill().getSkillId().equals("0")){
			Skill parentSkill = skillDAO.getBySkillId(skill.getParentSkill().getSkillId());
			skill.setParentSkill(parentSkill);
			
			//Create display name from parent 
			StringBuffer displayName = new StringBuffer();
			displayName.append(skill.getSkillName());
			boolean hasParent = true;
			while (hasParent) {
				if (parentSkill.getParentSkill() != null)
				displayName.insert(0, parentSkill.getSkillName() + " | ");
				parentSkill = parentSkill.getParentSkill();
				if (parentSkill == null)
					hasParent = false;
			}
			skill.setDisplayName(displayName.toString());		
		}else{
			skill.setDisplayName(skill.getSkillName());
		}
		skillDAO.update(skill);		
		return skill;
	}
	
	@Override
	@Transactional
	public List<UserSkills> listUserSkills(int status) {
		return userSkillsDAO.list(status);
	}

	@Override
	@Transactional
	public List<UserSkills> listUserSkillsByUserId(int status, int userId, int isApproved, int selfIsPrimary, int managerIsPrimary) {
		return userSkillsDAO.listByUserId(status, userId, isApproved, selfIsPrimary, managerIsPrimary);
	}
	
	@Override
	@Transactional
	public List<UserSkills> listUserSkillsToBeApproved(int status, int userId, int isApproved, int selfIsPrimary, int managerIsPrimary) {
		return userSkillsDAO.listUserSkillsToBeApproved(status, userId, isApproved, selfIsPrimary, managerIsPrimary);
	}
	
	@Override
	@Transactional
	public UserSkills getByUserSkillId(int userSkillId) {
		return userSkillsDAO.getByUserSkillId(userSkillId);
	}

	@Override
	@Transactional
	public boolean isUserSkillExistingBySkillName(String UserSkills_SkillName, int CurrentUser_userId) {
		return userSkillsDAO.isUserSkillExistingBySkillName(UserSkills_SkillName, CurrentUser_userId);
	}
	
	@Override
	@Transactional
	public UserSkills getUserSkillBySkillName(String UserSkills_SkillName, int CurrentUser_userId) {
		return userSkillsDAO.getUserSkillsBySkillName(UserSkills_SkillName, CurrentUser_userId);
	}
	
	@Override
	@Transactional
	public void addUserSkill(UserSkills userSkills) {
		userSkillsDAO.add(userSkills);		
	}

	@Override
	@Transactional
	public UserSkills updateUserSkill(UserSkills userSkills) {
		userSkillsDAO.update(userSkills);
		return userSkills;
	}

	@Override
	@Transactional
	public UserSkills updateUserSkillInline(int userId, int userSkillId,
			String modifiedField, String modifiedFieldValue) {
		UserSkills userSkills = userSkillsDAO.getByUserSkillId(userSkillId);
		log.info("modifiedField>>>>"+modifiedField);
		log.info("modifiedFieldValue>>>>"+modifiedFieldValue);
		log.info("resourceAvailabilityId>>>>"+userSkills);
		int isApproved = userSkills.getIsApproved();
		if(isApproved == 0){
			if(modifiedField.equalsIgnoreCase("selfSkillLevelId")){
				if (modifiedFieldValue != null || !modifiedFieldValue.equals("0")) {
					userSkills.setSelfSkillLevel(skillLevelsDAO.getByskillLevelId(Integer.parseInt(modifiedFieldValue)));
					log.info("Self Skill level changed to "+userSkills.getSelfSkillLevel().getLevelName());
				} else {
					//remains with old value.
				}
			}else if(modifiedField.equalsIgnoreCase("selfIsPrimary")){
				if (modifiedFieldValue != null || !modifiedFieldValue.equals("0")) {
					userSkills.setSelfIsPrimary(Integer.parseInt(modifiedFieldValue));
					log.info("Self Is primary  changed to "+userSkills.getSelfIsPrimary());
				} else {
					//remains with old value.
				}
			}else if(modifiedField.equalsIgnoreCase("approvingManagerId")){
				if (modifiedFieldValue != null || !modifiedFieldValue.equals("0")) {
					userSkills.setApprovingManager(userListDAO.getByUserId(Integer.parseInt(modifiedFieldValue)));
					log.info("Approver changed to "+userSkills.getApprovingManager().getLoginId());
				} else {
					//remains with old value.
				}
			}
			
			//
		}
		userSkillsDAO.update(userSkills);
		return userSkills;
	}
	
	@Override
	@Transactional
	public UserSkills updateUserSkillApprovalInline(int approverId,
			int userSkillId, String modifiedField, String modifiedFieldValue) {
		UserSkills userSkills = userSkillsDAO.getByUserSkillId(userSkillId);
		log.info("modifiedField>>>>"+modifiedField);
		log.info("modifiedFieldValue>>>>"+modifiedFieldValue);
		log.info("resourceAvailabilityId>>>>"+userSkills);		
			if(modifiedField.equalsIgnoreCase("selfSkillLevelId")){//User's skill id
				if (modifiedFieldValue != null || !modifiedFieldValue.equals("0")) {
					userSkills.setSelfSkillLevel(skillLevelsDAO.getByskillLevelId(Integer.parseInt(modifiedFieldValue)));
					log.info("Self Skill level changed to "+userSkills.getSelfSkillLevel().getLevelName());
				} else {
					//remains with old value.
				}
			}else if(modifiedField.equalsIgnoreCase("selfIsPrimary")){
				if (modifiedFieldValue != null || !modifiedFieldValue.equals("0")) {
					userSkills.setSelfIsPrimary(Integer.parseInt(modifiedFieldValue));
					log.info("Self Is primary  changed to "+userSkills.getSelfIsPrimary());
				} else {
					//remains with old value.
				}
			}else if(modifiedField.equalsIgnoreCase("approvingManagerId")){
				if (modifiedFieldValue != null || !modifiedFieldValue.equals("0")) {
					userSkills.setApprovingManager(userListDAO.getByUserId(Integer.parseInt(modifiedFieldValue)));
					log.info("Approver changed to "+userSkills.getApprovingManager().getLoginId());
				} else {
					//remains with old value.
				}
			}
			else if(modifiedField.equalsIgnoreCase("managerSkillLevelId")){
				if (modifiedFieldValue != null || !modifiedFieldValue.equals("0")) {
					userSkills.setManagerSkillLevel(skillLevelsDAO.getByskillLevelId(Integer.parseInt(modifiedFieldValue)));
					log.info("Approver Skill changed to "+userSkills.getManagerSkillLevel().getLevelName());
				} else {
					//remains with old value.
				}
			}else if(modifiedField.equalsIgnoreCase("managerIsPrimary")){
				if (modifiedFieldValue != null || !modifiedFieldValue.equals("0")) {
					userSkills.setManagerIsPrimary(Integer.parseInt(modifiedFieldValue));
					log.info("Manager Is primary  changed to "+userSkills.getManagerIsPrimary());
				} else {
					//remains with old value.
				}
			}else if(modifiedField.equalsIgnoreCase("isApproved")){
				if (modifiedFieldValue != null || !modifiedFieldValue.equals("0")) {
					userSkills.setIsApproved(Integer.parseInt(modifiedFieldValue));
					log.info("Approve status "+userSkills.getIsApproved());
				} else {
					//remains with old value.
				}
			}
			
			//
		
		userSkillsDAO.update(userSkills);
		return userSkills;
	}

	
	

	@Override
	@Transactional
	public List<SkillLevels> listSkillLevels(int status) {
		return skillLevelsDAO.list(status);
	}

	@Override
	@Transactional
	public SkillLevels getByskillLevelId(int skillLevelId) {		
		return skillLevelsDAO.getByskillLevelId(skillLevelId);
	}

	@Override
	@Transactional
	public void addSkillLevels(SkillLevels skillLevels) {
		
	}

	@Override
	@Transactional
	public SkillLevels updateSkillLevels(SkillLevels skillLevels) {
		return null;
	}

	@Override
	@Transactional
	public List<SkillLevels> listSkillLevelsforUsers(int status,
			int roleId, String isApprover) {		
		return skillLevelsDAO.listSkillLevelsforUsers(status, roleId, isApprover);
	}

	@Override
	@Transactional
	public SkillLevels getSkillLevelsbyLevleName(String levelName) {
	
		return skillLevelsDAO.getSkillLevelsbyLevleName(levelName);
	}

	@Override
	public Skill getRootSkill() {
		return skillDAO.getRootSkill();
	}
	
	@Override
	@Transactional
	public List<Skill> listChildNodesInHierarchyinLayers(Skill skill) {
		return skillDAO.listChildNodesInHierarchyinLayers(skill);
	}
	
}
