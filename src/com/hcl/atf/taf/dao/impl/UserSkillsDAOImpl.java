package com.hcl.atf.taf.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.hcl.atf.taf.dao.UserSkillsDAO;
import com.hcl.atf.taf.model.Skill;
import com.hcl.atf.taf.model.UserSkills;


@Repository
public class UserSkillsDAOImpl implements UserSkillsDAO {
	private static final Log log = LogFactory.getLog(UserSkillsDAOImpl.class);
	
	@Autowired(required=true)
    private SessionFactory sessionFactory;	
	
	@Override
	@Transactional
	public List<UserSkills> list(int status) {		
		List<UserSkills> userSkillsList = null;
		try {
			Criteria c = sessionFactory.getCurrentSession().createCriteria(UserSkills.class, "skill");
			if(status <2){
				c.add(Restrictions.eq("skill.status", status));	
			}
				
			userSkillsList = c.list();
			for (UserSkills user : userSkillsList) {
				Hibernate.initialize(user.getSkill());
				Hibernate.initialize(user.getUser());
				Hibernate.initialize(user.getApprovingManager());
				Hibernate.initialize(user.getSelfSkillLevel());
				Hibernate.initialize(user.getManagerSkillLevel());
			}
			log.info("Total UserSkills-----"+userSkillsList.size());
			for(UserSkills usk : userSkillsList){
			}
		} catch (HibernateException e) {
			log.error("ERROR listing userSkills  ",e);
		}
		return userSkillsList;
	}
	
	@Override
	@Transactional
	public List<UserSkills> listByUserId(int status, int userId, int isApproved, int selfIsPrimary, int managerIsPrimary) {		
		List<UserSkills> userSkillsList = null;
		try {
			Criteria c = sessionFactory.getCurrentSession().createCriteria(UserSkills.class, "skill");			
			c.add(Restrictions.eq("skill.user.userId", userId));
			if(isApproved<2){
				c.add(Restrictions.eq("skill.isApproved", isApproved)); //Only UnApproved
			}
			if(selfIsPrimary<2){
				c.add(Restrictions.eq("skill.selfIsPrimary", selfIsPrimary)); //Primary skills
			}
			if(managerIsPrimary<2){
				c.add(Restrictions.eq("skill.managerIsPrimary", managerIsPrimary)); //Primary skills of Approver
			}
			if(status <2){
				c.add(Restrictions.eq("skill.status", status));	
			}				
			userSkillsList = c.list();
			for (UserSkills user : userSkillsList) {
				Hibernate.initialize(user.getSkill());
				Hibernate.initialize(user.getUser());
				Hibernate.initialize(user.getUser().getUserSkills());
				Hibernate.initialize(user.getApprovingManager());
				Hibernate.initialize(user.getSelfSkillLevel());
				Hibernate.initialize(user.getManagerSkillLevel());
				Hibernate.initialize(user.getUser().getUserSkills());
				
			}			
		} catch (HibernateException e) {
			log.error("ERROR listing userSkills by UserId ",e);
		}
		return userSkillsList;
	}
	
	@Override
	@Transactional
	public List<UserSkills> listUserSkillsToBeApproved(int status, int userId, int isApproved, int selfIsPrimary, int managerIsPrimary) {		
		List<UserSkills> userSkillsList = null;		
		try {
			Criteria c = sessionFactory.getCurrentSession().createCriteria(UserSkills.class, "skill");			
			c.add(Restrictions.eq("skill.approvingManager.userId", userId));
			if(isApproved<2){
				c.add(Restrictions.eq("skill.isApproved", isApproved)); //Only UnApproved
			}
			if(selfIsPrimary<2){
				c.add(Restrictions.eq("skill.selfIsPrimary", selfIsPrimary)); //Primary skills
			}
			if(managerIsPrimary<2){
				c.add(Restrictions.eq("skill.managerIsPrimary", managerIsPrimary)); //Primary skills of Approver
			}	
			
			if(status <2){
				c.add(Restrictions.eq("skill.status", status));	
			}				
			userSkillsList = c.list();
			for (UserSkills user : userSkillsList) {
				Hibernate.initialize(user.getSkill());
				Hibernate.initialize(user.getUser());
				Hibernate.initialize(user.getUser().getUserSkills());
				Hibernate.initialize(user.getApprovingManager());
				Hibernate.initialize(user.getSelfSkillLevel());
				Hibernate.initialize(user.getManagerSkillLevel());
				
			}			
			
		} catch (HibernateException e) {
			log.error("ERROR listing userSkills ToBeApproved ",e);
		}
		return userSkillsList;
	}
	
	@Override
	@Transactional
	public UserSkills getByUserSkillId(int userSkillId){
		log.debug("getting UserSkill instance by id");
		UserSkills userSkill=null;
		List<UserSkills> userSkillsList = new ArrayList<UserSkills>();		
		try {
			Criteria c = sessionFactory.getCurrentSession().createCriteria(UserSkills.class, "userskill");
			c.add(Restrictions.eq("userskill.userSkillId", userSkillId));
			
			userSkillsList = c.list();
			userSkill=(userSkillsList!=null && userSkillsList.size()!=0)?(UserSkills)userSkillsList.get(0):null;
			
			log.debug("getByuserSkillId successful");
		} catch (RuntimeException re) {
			log.error("getByuserSkillId failed", re);
			//throw re;
		}
		return userSkill;
	}	
	
	@Override
	@Transactional
	public boolean isUserSkillExistingBySkillName(String UserSkills_SkillName, int CurrentUser_userId) {
		Criteria c = sessionFactory.getCurrentSession().createCriteria(UserSkills.class, "userskill");
		c.createAlias("userskill.user", "ul");		
		c.createAlias("userskill.skill", "sk");
		c.add(Restrictions.eq("ul.userId", CurrentUser_userId));
		c.add(Restrictions.eq("sk.skillName", UserSkills_SkillName));		
		List<Skill> skills = c.list();		
		if (skills == null || skills.isEmpty()) 
		    return false;
		else 
			return true;
	}
	
	@Override
	@Transactional
	public UserSkills getUserSkillsBySkillName(String UserSkills_SkillName, int CurrentUser_userId) {
		log.debug("listing specific UserSkill instance by SkillName");
		UserSkills userSkills=null;
		try {
			Criteria c = sessionFactory.getCurrentSession().createCriteria(UserSkills.class, "userskill");
			c.createAlias("userskill.user", "ul");		
			c.createAlias("userskill.skill", "sk");
			c.add(Restrictions.eq("ul.userId", CurrentUser_userId));
			c.add(Restrictions.eq("sk.skillName", UserSkills_SkillName));
			
			List<UserSkills> userskillList = c.list();	
			
			userSkills=(userskillList!=null && userskillList.size()!=0)?(UserSkills)userskillList.get(0):null;
			
			log.debug("Obtain UserSkill object successful");
		} catch (RuntimeException re) {
			log.error("Obtain UserSkill object failed", re);
		}
		return userSkills;
	}	
	
	@Override
	@Transactional
	public void add(UserSkills userSkills) {
		log.debug("adding UserSkills instance");
		try {
			userSkills.setStatus(1);			
			sessionFactory.getCurrentSession().save(userSkills);
			log.debug("add successful");
		} catch (RuntimeException re) {
			log.error("add failed", re);
		}		
	}
	
	@Override
	@Transactional
	public void update(UserSkills userSkills) {
		log.debug("updating UserSkills instance");
		try {
			userSkills.setStatus(1);
			sessionFactory.getCurrentSession().saveOrUpdate(userSkills);
			log.debug("update successful");
		} catch (RuntimeException re) {
			log.error("update failed", re);
		}		
	}

	

	@Override
	@Transactional
	public int userSkillBulk(List<UserSkills> userSkillList,int batchSize) {
		log.info("Adding User skills in bulk");
		int count = 0;
		try {
			Session session = sessionFactory.getCurrentSession();
			if (batchSize <= 0)
				batchSize = 50;
			for (UserSkills userSkill : userSkillList ) {
				session.save(userSkill);
				log.info("UserSkill data saved successfully");
				if (count++ % batchSize == 0 ) {
					session.flush();
					session.clear();
			    }
			}
			log.info("Bulk Add UserSkills Successful");
		} catch (RuntimeException re) {
			log.error("Bulk Add failed", re);
		}
		return count;
	}
	
}
