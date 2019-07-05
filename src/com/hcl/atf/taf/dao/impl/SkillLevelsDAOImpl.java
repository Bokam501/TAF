package com.hcl.atf.taf.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.hcl.atf.taf.dao.SkillLevelsDAO;
import com.hcl.atf.taf.model.SkillLevels;


@Repository
public class SkillLevelsDAOImpl implements SkillLevelsDAO {
	private static final Log log = LogFactory.getLog(SkillLevelsDAOImpl.class);
	
	@Autowired(required=true)
    private SessionFactory sessionFactory;	
	
	@Override
	@Transactional
	public List<SkillLevels> list(int status) {		
		Criteria c = sessionFactory.getCurrentSession().createCriteria(SkillLevels.class, "skilllevels");
		//As of now status in DB, once added, below code works
		if(status <2){
			c.add(Restrictions.eq("skilllevels.status", status));	
		}
			
		List<SkillLevels> skillLevelsList = c.list();
		log.info("Total UserSkills-----"+skillLevelsList.size());
		return skillLevelsList;
	}	
	
	@Override
	@Transactional
	public List<SkillLevels> listSkillLevelsforUsers(int status, int roleId, String isApprover) {		
		List<SkillLevels> skillLevelsList = null;
		try {
			Criteria c = sessionFactory.getCurrentSession().createCriteria(SkillLevels.class, "skilllevels");
			//As of now status in DB, once added, below code works
			if(status <2){
				c.add(Restrictions.eq("skilllevels.status", status));	
			}			
			if(!isApprover.equalsIgnoreCase("yes")){
				c.add(Restrictions.ne("skilllevels.skillLevelId", 6));	
			}else{					
			}				
			skillLevelsList = c.list();
		} catch (HibernateException e) {
			e.printStackTrace();
		}
		return skillLevelsList;
	}
	
	@Override
	@Transactional
	public SkillLevels getByskillLevelId(int skillLevelId) {
		log.debug("getting SkillLevels instance by id");
		SkillLevels skillLevels=null;
		List<SkillLevels> skillLevelsList = new ArrayList<SkillLevels>();		
		try {
			Criteria c = sessionFactory.getCurrentSession().createCriteria(SkillLevels.class, "skilllevels");
			c.add(Restrictions.eq("skilllevels.skillLevelId", skillLevelId));
			
			skillLevelsList = c.list();
			skillLevels=(skillLevelsList!=null && skillLevelsList.size()!=0)?(SkillLevels)skillLevelsList.get(0):null;
			
			log.debug("getByskillLevelId successful");
		} catch (RuntimeException re) {
			log.error("getByskillLevelId failed", re);
		}
		return skillLevels;
	}

	@Override
	@Transactional
	public void add(SkillLevels skillLevels) {
		log.debug("adding SkillLevels instance");
		try {
			sessionFactory.getCurrentSession().save(skillLevels);
			log.debug("add successful");
		} catch (RuntimeException re) {
			log.error("add failed", re);
		}		
	}

	@Override
	@Transactional
	public void update(SkillLevels skillLevels) {
		log.debug("updating SkillLevels instance");
		try {
			sessionFactory.getCurrentSession().saveOrUpdate(skillLevels);
			log.debug("update successful");
		} catch (RuntimeException re) {
			log.error("update failed", re);
		}		
	}

	@Override
	@Transactional
	public SkillLevels getSkillLevelsbyLevleName(String levelName) {
		
		log.debug("getting SkillLevels instance by LevelName");
		SkillLevels skillLevels=null;
		List<SkillLevels> skillLevelsList = new ArrayList<SkillLevels>();		
		try {
			Criteria c = sessionFactory.getCurrentSession().createCriteria(SkillLevels.class, "skilllevels");
		c.add(Restrictions.eq("skilllevels.levelName", levelName));
			
			skillLevelsList = c.list();
			skillLevels=(skillLevelsList!=null && skillLevelsList.size()!=0)?(SkillLevels)skillLevelsList.get(0):null;
			
			log.debug("getByskillLevelId successful");
		} catch (RuntimeException re) {
			log.error("getByskillLevelId failed", re);
	}
		return skillLevels;
	

	}
}
	

